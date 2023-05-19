package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/** class representing a directional light source
 * @author Avishai Shachor and Yoav Babayof
 */
public class DirectionalLight extends Light implements LightSource {
    /** direction of light source*/
    private Vector dir;

    @Override
    public Color getIntensity(Point p) {
        return this.getIntensity();
    }

    @Override
    public Vector getL(Point p) {
        return this.dir;
    }

    /** constructor for DirectionalLight
     * @param intensity original light intensity
     * @param dir vector representing direction of light
     */
    public DirectionalLight(Color intensity, Vector dir) {
        super(intensity);
        this.dir = dir.normalize();
    }
}
