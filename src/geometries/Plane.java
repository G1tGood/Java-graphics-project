package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * class Plane is a class representing a plane
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Plane implements Geometry{
    /** point in plane */
    Point q0;
    /** normal vector to the plane */
    Vector normal;

    /** getter to point in plane q0
     * @return q0 */
    public Point getQ0() {
        return q0;
    }

    /** getter to normal vector to plane normal
     * @return normal */
    public Vector getNormal() {
        return normal;
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

    /** Constructor to initialize Plane based on a normal vector and point in plane
     * @param q0 point in plane
     * @param normal normal vector to plane
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /** Constructor to initialize Plane based on three points in plane
     * @param p0 first point in plane
     * @param p1 second point in plane
     * @param p2 third point in plane
     * @throws IllegalArgumentException if two or three of the given points are the same point
     */
    public Plane(Point p0, Point p1, Point p2) {
        this.normal = null;
        this.q0 = p0;
    }
}
