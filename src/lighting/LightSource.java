package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/** interface representing an external light source for scene
 * @author Avishai Shachor and Yoav Babayof
 */
public interface LightSource {
    /** calculates intensity of light at a given point
     * @param p given point
     * @return intensity of light at the given point */
    Color getIntensity(Point p);

     /** calculates direction vector from light source to a given point
      * @param p given point
      * @return direction vector from light source to the given point */
    Vector getL(Point p);

    /** calculates distance of light source from a point
     * @param point a point
     * @return distance of light source from a point
     */
    double getDistance(Point point);
}
