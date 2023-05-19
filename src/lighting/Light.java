package lighting;

import primitives.Color;

/** class representing an external light for scene
 * @author Avishai Shachor and Yoav Babayoff
 */
abstract class Light {
    /** intensity of light */
    private Color intensity;

    /** constructor for Light
     * @param intensity intensity of light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /** getter for intensity
     * @return intensity of light
     */
    public Color getIntensity() {
        return intensity;
    }
}
