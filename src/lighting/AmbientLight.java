package lighting;

import primitives.Color;
import primitives.Double3;

/** class representing ambient lighting for scene
 * @author Avishai Shachor and Yoav Babayoff
 */
public class AmbientLight extends Light {
    /** no ambient light */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /** constructor for AmbientLight
     * @param iA original intensity of light
     * @param kA attenuation coefficient of light
     */
    public AmbientLight(Color iA, double kA){
        super(iA.scale(kA));
    }

    /** constructor for AmbientLight
     * @param iA original intensity of light
     * @param kA attenuation coefficient of light
     */
    public AmbientLight(Color iA, Double3 kA){
        super(iA.scale(kA));
    }
}
