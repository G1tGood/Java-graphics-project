package primitives;

/** a DPS representing a material
 * @author Avishai Sachor and Yoav Babayof */
public class Material {
    /** Diffusion attenuation coefficient */
    public Double3 kD = Double3.ZERO;

    /** Specular attenuation coefficient */
    public Double3 kS = Double3.ZERO;

    /** Transparency attenuation coefficient */
    public Double3 kT = Double3.ZERO;

    /** Reflectance attenuation coefficient */
    public Double3 kR = Double3.ZERO;

     /** shininess of the material */
    public int nShininess = 1;

    /** setter for kD
     * @param kD diffusion attenuation coefficient
     * @return this Material object
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /** setter for kD
     * @param kD diffusion attenuation coefficient
     * @return this Material object
     */
    public Material setKd(double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /** setter for kS
     * @param kS specular attenuation coefficient
     * @return this Material object
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /** setter for kS
     * @param kS specular attenuation coefficient
     * @return this Material object
     */
    public Material setKs(double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /** setter for kT
     * @param kT transparency attenuation coefficient
     * @return this Material object
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /** setter for kT
     * @param kT transparency attenuation coefficient
     * @return this Material object
     */
    public Material setKt(double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /** setter for kR
     * @param kR reflectance attenuation coefficient
     * @return this Material object
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /** setter for kR
     * @param kR reflectance attenuation coefficient
     * @return this Material object
     */
    public Material setKr(double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /** setter for shininess
     * @param nShininess shininess of the material
     * @return this Material object
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
