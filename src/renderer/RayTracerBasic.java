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
public class RayTracerBasic extends RayTracerBase{
    /** constructor to RayTracerBasic
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

    /** calculates the color of the geometry at the point ray intersects it
     * @param gp GeoPoint (point and geometry)
     * @param ray ray that intersects the geometry at point
     * @return the color of the geometry at the point ray intersects it
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return this.scene.ambientLight.getIntensity()
                                      .add(calcLocalEffects(gp, ray));
    }

    /** calculates local effects of intersection between ray and geometry
     * @param gp GeoPoint (point and geometry)
     * @param ray ray
     * @return final light affected by local effects of intersection between ray and geometry
     */
    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
        Color color = gp.geometry.getEmission();
        Vector v = ray.getDir (); Vector n = gp.geometry.getNormal(gp.point);
        double nv = alignZero(n.dotProduct(v)); if (nv == 0) return color;
        Material material = gp.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(gp.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) { // sign(nl) == sing(nv)
                Color iL = lightSource.getIntensity(gp.point);
                color = color.add(iL.scale(calcDiffusive(material, nl)),
                        iL.scale(calcSpecular(material, n, l, nl, v)));
            }
        }
        return color;
    }

    /** calculates diffusive part of light calculation in PHONG reflectance model
     * @param material material of geometry
     * @param nl dot product between normal vector to geometry at point and direction vector between light source and point
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
        return material.kS.scale(pow( ((nVr>0)? nVr : 0), material.nShininess));    // ks*max(0,-v*r)^nShininess
    }
}
