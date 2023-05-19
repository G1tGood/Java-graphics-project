package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static java.lang.Math.pow;
import static primitives.Util.alignZero;

/** class representing a spotlight
 * @author Avishai Shachor and Yoav Babayof
 */
public class SpotLight extends PointLight implements LightSource {
    /** direction of light from spotlight */
    private Vector dir;
    /** narrowness of beam */
    private double narrowness = 1;


    /** constructor for SpotLight
     * @param dir vector representing direction of light
     * @param position position of light source
     * @param intensity original intensity of light
     */
    public SpotLight(Color intensity, Point position, Vector dir) {
        super(intensity, position);
        this.dir = dir.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        double dirL = pow(alignZero(dir.dotProduct(this.getL(p))),narrowness);
        return super.getIntensity(p).scale(0 < dirL? dirL: 0);
    }

    /** setting the narrowness of the beam
     * @param narrowness narrowness of the beam
     * @return SpotLight
     */
    public SpotLight setNarrowBeam(int narrowness){
        this.narrowness = narrowness;
        return this;
    }
}
