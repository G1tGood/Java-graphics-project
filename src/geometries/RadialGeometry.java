package geometries;

import primitives.Util;

/**
 * class radialGeometry is a class representing a geometry
 * with a radius of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public abstract class RadialGeometry extends Geometry{
    /** radius of the geometry */
    final protected double radius;

    /**
     * getter for radius of the geometry
     * @return radius of the geometry
     */
    public double getRadius() {
        return radius;
    }

    /** Constructor to initialize radialGeometry based on a radius
     * @param radius radius of the geometry
     * @throws IllegalArgumentException if radius <= 0
     */
    public RadialGeometry(double radius) {
        if (Util.isZero(radius) || radius < 0)
            throw new IllegalArgumentException("radius cannot be less than or equal to zero");
        this.radius = radius;
    }
}
