package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/** base class for ray tracer
 * @author Avishai Sachor and Yoav Babayoff */
public abstract class RayTracerBase {
    /** scene */
    protected Scene scene;

    /** constructor to RayTracerBase
     * @param scene scene */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /** returns color of the closest geometry to base point of ray that ray intersects */
    public abstract Color traceRay(Ray ray);
}
