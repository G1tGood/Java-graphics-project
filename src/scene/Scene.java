package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

/** class representing a scene
 * @author Avishai Sachor and Yoav Babayoff
 */
public class Scene {
    /** scene name */
    public String name;

    /** background color of scene */
    public Color background = Color.BLACK;

    /** ambient light of scene */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /** geometries in scene */
    public Geometries geometries = new Geometries();

    /** lights in scene */
    public List<LightSource> lights = new LinkedList<LightSource>();

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

    /** setter for lights in scene
     * @param lights lights in scene
     * @return scene
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}
