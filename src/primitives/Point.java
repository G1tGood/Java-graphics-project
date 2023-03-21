package primitives;

/**
 * Class Point is the basic class representing a point of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Point {
    /** 3-dimensional coordinates */
    final Double3 xyz;

    /** Constructor to initialize Point based object with one 3 double numbers (Double3) value
     * @param xyz 3 double numbers (Double3) value
     */
    Point(Double3 xyz) {
        this.xyz = xyz;
    }

    /** Constructor to initialize Point based object with 3 number values
     * @param x first number value
     * @param y second number value
     * @param z third number value
     */
    public Point(double x, double y, double z) {
        this.xyz = new Double3(x,y,z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Point other)
            return this.xyz.equals(other.xyz);
        return false;
    }

    @Override
    public int hashCode() {
        return xyz.hashCode();
    }

    @Override
    public String toString() {
        return xyz.toString();
    }

    /** subtracts two points into a new vector from the second
     *  point (right handle side) to the first one
     * @param  point right handle side operand for subtraction
     * @return vector from first to second (right hand side) point
     */
    public Vector subtract(Point point) {
        return new Vector(xyz.subtract(point.xyz));
    }

    /** Sums a point and a vector into a new point where each coordinate
     * is summarized
     * @param   vector right handle side operand for addition
     * @return  point result from addition of vector and point
     */
    public Point add(Vector vector) {
        return new Point(vector.xyz.add(this.xyz));
    }

    /** calculates the distance between two points - squared
     * @param  point right handle side operand for distance squared calculation
     * @return     distance between points - squared
     */
    public double distanceSquared(Point point){
        //distance between points (x1,y1,z1), (x2,y2,z2) squared is
        // (x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2
        return  (this.xyz.d1-point.xyz.d1)*(this.xyz.d1-point.xyz.d1) +
                (this.xyz.d2-point.xyz.d2)*(this.xyz.d2-point.xyz.d2) +
                (this.xyz.d3-point.xyz.d3)*(this.xyz.d3-point.xyz.d3);
    }

    /** calculates the distance between two points
     * @param  point right handle side operand for distance calculation
     * @return     distance between points
     */
    public double distance(Point point){
        //distance between points (x1,y1,z1), (x2,y2,z2) is
        // sqrt((x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2)
        return  java.lang.Math.sqrt(this.distanceSquared(point));
    }
}
