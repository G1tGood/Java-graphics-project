package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.lang.Math.pow;
import static primitives.Util.alignZero;

/** basic ray tracer
 * @author Avishai Sachor and Yoav Babayoff */
public class RayTracerBasic extends RayTracerBase {
    /**
     * ray base shifting constant
     */
    private static final double DELTA = 0.1;

    /**
     * constructor to RayTracerBasic
     *
     * @param scene scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return this.scene.background;
        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
        return calcColor(closestPoint, ray);
    }

    /**
     * calculates the color of the geometry at the point ray intersects it
     *
     * @param gp  GeoPoint (point and geometry)
     * @param ray ray that intersects the geometry at point
     * @return the color of the geometry at the point ray intersects it
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return this.scene.ambientLight.getIntensity()
                .add(calcLocalEffects(gp, ray));
    }

    /**
     * calculates local effects of intersection between ray and geometry
     *
     * @param gp  GeoPoint (point and geometry)
     * @param ray ray
     * @return final light affected by local effects of intersection between ray and geometry
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDir();
        Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v));
        if (nv == 0) return color;
        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sign(nv)
                if (unshaded(gp, lightSource, l, n, nl)) {
                    Color iL = lightSource.getIntensity(gp.point);
                    color = color.add(iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, nl, v)));
                }
            }
        }
        return color;
    }

    /**
     * calculates diffusive part of light calculation in PHONG reflectance model
     *
     * @param material material of geometry
     * @param nl       dot product between normal vector to geometry at point and direction vector between light source and point
     * @return diffusive part of light calculation
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }

    /**
     * calculates specular part of light calculation in PHONG reflectance model
     *
     * @param material material of geometry
     * @param n        normal vector to geometry at point
     * @param l        vector from light source to point
     * @param nl       n*l (dot product)
     * @param v        direction of ray
     * @return specular part of light calculation
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.subtract(n.scale(2 * nl));     // r = l-2*(l*n)n
        double nVr = alignZero(-v.dotProduct(r));
        return material.kS.scale(pow(((nVr > 0) ? nVr : 0), material.nShininess));    // ks*max(0,-v*r)^nShininess
    }

    /**
     * calculates weather or not a point is shaded from a light from a light source
     * @param gp    the GeoPoint
     * @param light the light from which we check shading
     * @param l     direction vector from light source to point
     * @param n     normal to the geometry at point
     * @param nl    dot product between normal to geometry at point direction vector from light source to point
     * @return weather or not a point is shaded from a light from a light source
     */
    private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n, double nl) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nl < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));
        return intersections == null;
    }
}