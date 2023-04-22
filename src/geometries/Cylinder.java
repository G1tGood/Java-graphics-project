package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

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
        // if the given point collides with the base point of the axis ray, just return the normal vector (dir)
        if (point.equals(this.axisRay.getP0())) return this.axisRay.getDir();

        //calculating distance of the given point from base point of the axis ray
        Point o = this.axisRay.getP0();
        double t = this.axisRay.getDir().dotProduct(point.subtract(this.axisRay.getP0()));
        //if the given point is on one of the bases of the cylinder, we just return a normal vector to the base (dir)
        if(isZero(t) || isZero(t - this.height)) return this.axisRay.getDir();
        return point.subtract(o.add(this.axisRay.getDir().scale(t))).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
