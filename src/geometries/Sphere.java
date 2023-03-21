package geometries;
import primitives.Point;
import primitives.Vector;

/**
 * class Sphere is a class representing a sphere
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Sphere extends RadialGeometry {
    /** center point of the sphere */
    final Point center;

    /** Constructor to initialize Sphere based on a center point and a radius of the sphere
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius){
        super(radius);
        this.center = center;
    }

    /**
     * getter for the center point of the sphere
     * @return center point of the sphere
     */
    public Point getCenter() {
        return center;
    }

    @Override
    public Vector getNormal(Point point){
        return null;
    }
}
