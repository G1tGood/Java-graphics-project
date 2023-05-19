package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * class Geometry is a class representing a geometrical object
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public abstract class Geometry extends Intersectable {
    // material of geometry //
    private Material material = new Material();

    /** emission light */
    protected Color emission = Color.BLACK;

    /** setter fo emission light
     * @param emission emission light
     * @return Geometry object */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /** setter for material of geometry
     * @param material material of geometry
     * @return this*/
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /** getter for emission light
     * @return emission light */
    public Color getEmission() {
        return emission;
    }

    /** getter for material of geometry
     * @return material of geometry */
    public Material getMaterial() {
        return material;
    }

    /**
     * Returns the normal vector of the geometry at the given point
     * @param point the given point for which we return the normal vector
     * @return the normal vector at the given point
     */
    public abstract Vector getNormal(Point point);
}
