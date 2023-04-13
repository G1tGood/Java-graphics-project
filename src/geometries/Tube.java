package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * class Tube is a class representing a tube
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Tube extends RadialGeometry {
    /** axis ray of the tube */
    final Ray axisRay;

    /** Constructor to initialize Tube based on an axis ray and the radius of the tube
     * @param axisRay axis ray of the tube
     * @param radius radius of the tube
     */
    public Tube(Ray axisRay, double radius){
        super(radius);
        this.axisRay = axisRay;
    }

    /**
     * getter for the axis ray of the Tube
     * @return axis ray of the tube
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
