package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.util.List;

import static java.lang.Math.pow;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/** basic ray tracer
 * @author Avishai Sachor and Yoav Babayoff */
public class RayTracerBasic extends RayTracerBase {
    /** maximum level of recursion for reflectance and refractions calculations */
    private static final int MAX_CALC_COLOR_LEVEL = 10;

    /** minimum value of parameter k for recursion */
    private static final double MIN_CALC_COLOR_K = 0.001;

    /** initial value of parameter k for recursion */
    private static final Double3 INITIAL_K = new Double3(1);

    /**
     * constructor to RayTracerBasic
     * @param scene scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        return closestPoint == null ? scene.background
                                    : calcColor(closestPoint, ray);
    }

    /**
     * calculates the color of the geometry at the intersection point
     * @param gp  GeoPoint (intersection point and geometry)
     * @param ray ray that intersects the geometry at point
     * @return the color of the geometry at the point ray intersects it
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }

    /**
     * recursive calculation of color of the geometry at the intersection point starting at a certain level and accumulating attenuation coefficient
     * @param intersection intersection GeoPoint
     * @param ray ray that intersects the geometry at point
     * @param level level of recursion
     * @param k accumulating attenuation coefficient
     * @return color of the geometry at the intersection point starting at a certain level and accumulating attenuation coefficient
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, ray);
        return 1 == level ? color
                          : color.add(calcGlobalEffects(intersection, ray, level, k));
    }

    /**
     * calculates global effects of intersection between ray and geometry
     * @param gp GeoPoint (intersection point and geometry)
     * @param ray ray that intersects the geometry at point
     * @param level level of recursion
     * @param k accumulating attenuation coefficient
     * @return global effects of intersection between ray and geometry
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDir();
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        return calcColorGlobalEffect(constructReflectedRay(gp, v, n),level, k, material.kR)
                .add(calcColorGlobalEffect(constructRefractedRay(gp, v, n),level, k, material.kT));
    }

    /**
     * constructs refracted ray from geometry at a certain point
     * @param gp GeoPoint (geometry and point)
     * @param v  original ray
     * @param n  normal to geometry at point
     * @return refracted ray from geometry at point
     */
    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v, n);
    }

    /**
     * constructs reflected ray from geometry at a certain point
     * @param gp GeoPoint (geometry and point)
     * @param v  original ray
     * @param n  normal to geometry at point
     * @return reflected ray from geometry at point
     */
    private Ray constructReflectedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, v.subtract(n.scale(2*v.dotProduct(n))), n);
    }

    /** recursive calculation of a certain global effect of intersection between ray and geometry
     * @param ray ray calculated for the certain effect
     * @param level level of recursion
     * @param k accumulating attenuation coefficient
     * @param kx attenuation coefficient of the certain effect
     * @return calculation of a certain global effect of intersection between ray and geometry
     */
    private Color calcColorGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        if (gp == null) return scene.background.scale(kx);
        return isZero(gp.geometry.getNormal(gp.point).dotProduct(ray.getDir()))? Color.BLACK
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     * calculates local effects of intersection between ray and geometry
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
                if (unshaded(gp, lightSource, l, n)) {
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
     * @return weather or not a point is shaded from a light from a light source
     */
    private boolean unshaded(GeoPoint gp, LightSource light, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(gp.point, lightDirection, n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, light.getDistance(gp.point));

        if (intersections == null) return true;
        for (GeoPoint intersection: intersections) {
            if (intersection.geometry.getMaterial().kT.equals(Double3.ZERO))
                return false;
        }
        return true;
    }

    /**
     * finds the closest intersection GeoPoint between ray and the geometries in scene
     * @param ray the ray
     * @return the closest intersection GeoPoint between ray and the geometries in scene
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);
        return ray.findClosestGeoPoint(intersections);
    }
}