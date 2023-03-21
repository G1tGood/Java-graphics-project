package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 * class Cylinder is a class representing a cylinder
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Cylinder extends Tube {
    /** height of the tube */
    final double height;

    /** Constructor to initialize Cylinder based on given axis ray, radius, and height
     * @param axisRay axis ray of the cylinder
     * @param radius radius of the cylinder
     * @param height height of the cylinder
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        this.height = height;
    }

    /**
     * getter for height of the cylinder
     * @return height of the cylinder
     */
    public double getHeight() {
        return height;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
