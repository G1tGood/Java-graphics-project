package lighting;
import primitives.Color;
import primitives.Point;
import primitives.Vector;

/** class representing a point light source
 * @author Avishai Shachor and Yoav Babayof
 */
public class PointLight extends Light implements LightSource {
    /** position of light source */
    private Point position;

    // attenuation coefficients
    /** constant attenuation coefficient */
    private double kC = 1;
    /** linear attenuation coefficient */
    private double kL = 0;
    /** squared attenuation coefficient */
    private double kQ = 0;

    /** setter for kC
     * @param kC constant attenuation coefficient
     * @return this PointLight object */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /** setter for kL
     * @param kL linear attenuation coefficient
     * @return this PointLight object */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /** setter for kQ
     * @param kQ squared attenuation coefficient
     * @return this PointLight object */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    /** constructor for PointLight
     * @param position position of point light
     * @param intensity original intensity of light */
    public PointLight( Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    @Override
    public Color getIntensity(Point p) {
        double d = p.distance(this.position);
        return this.getIntensity().reduce(kC + kL * d + kQ * d*d);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(this.position).normalize();
    }
}
