package geometries;

import primitives.Util;

/**
 * class radialGeometry is a basic class representing a geometry
 * with radius of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public abstract class RadialGeometry implements Geometry{
    /** radius of the geometry */
    final protected double radius;

    /** Constructor to initialize radialGeometry based on a radius
     * @param radius the radius of the geometry
     * @throws IllegalArgumentException if radius is less than or equal to zero
     */
    public RadialGeometry(double radius) {
        if (Util.isZero(radius) || radius < 0)
            throw new IllegalArgumentException("radius cannot be less than or equal to zero");
        this.radius = radius;
    }
}
