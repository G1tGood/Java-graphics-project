package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

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
        List<Point> intersectionPoints = this.scene.geometries.findIntersections(ray);
        if (intersectionPoints == null) return this.scene.background;
        return calcColor(ray.findClosestPoint(intersectionPoints));
    }

    // returns color of points
    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }
}
