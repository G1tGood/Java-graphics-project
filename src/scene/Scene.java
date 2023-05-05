package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/** class representing a scene
 * @author Avishai Sachor and Yoav Babayoff
 */
public class Scene {
    /** scene name */
    public String name;

    /** background color of scene */
    public Color background;

    /** ambient light of scene */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** geometries in scene */
    public Geometries geometries;

    /** constructor for Scene
     * @param name name of scene
     */
     public Scene(String name){
         this.name = name;
         this.setGeometries(new Geometries());
     }

    /** setter for background color
     * @param background background color of scene
     * @return scene
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**setter for ambient light
     * @param ambientLight ambient light of the scene
     * @return scene
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /** setter for geometries in scene
     * @param geometries geometries in scene
     * @return scene
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
