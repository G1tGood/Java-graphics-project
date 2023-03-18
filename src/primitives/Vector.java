package primitives;

/**
 * Class Vector is the basic class representing a vector of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Vector extends Point{
    /** Constructor to initialize Vector based on a Double3 value
     * @param xyz number value for all 3 numbers
     * @throws IllegalArgumentException if xyz = (0,0,0) */
    Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("cannot create a zero vector");
    }

    /** Constructor to initialize Vector based on 3 number values
     * @param x number value for x coordinate
     * @param y number value for y coordinate
     * @param z number value for z coordinate
     * @throws IllegalArgumentException if xyz = (0,0,0) */
    public Vector(Double x, Double y, Double z) {
        super(x, y, z);
        if (Util.isZero(x) && Util.isZero(y) && Util.isZero(z))
            throw new IllegalArgumentException("cannot create a zero vector");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Vector other)
            return super.equals(other);
        return false;
    }

    @Override
    public int hashCode() { return xyz.hashCode(); }

    @Override
    public String toString() { return "‐>" + super.toString(); }

    /** Sums two vectors into a new vector where each coordinate
     * is summarized
     * @param  vector right handle side operand for addition
     * @return     result of add */
    public Vector add(Vector vector) {
        return new Vector(vector.xyz.add(this.xyz));
    }

    /** scales a vector into a new vector where each coordinate
     * is multiplied by the scale factor
     * @param  factor factor for scaling the vector
     * @return     result of scale */
    public Vector scale(double factor) {
        return new Vector(this.xyz.scale(factor));
    }

    /** calculates the dot product of two vectors
     * @param  vector right handle side operand for dot product calculation
     * @return     result of dot product */
    public Double dotProduct(Vector vector){
        //dot product of two vectors (x1,y1,z1) and (x2,y2,z2)
        //is x1*x2 + y1*y2 + z1*z2
        return  this.xyz.d1 * vector.xyz.d1 +
                this.xyz.d2 * vector.xyz.d2 +
                this.xyz.d3 * vector.xyz.d3;
    }

    /** calculates the cross product of two vectors
     * @param  vector right handle side operand for cross product calculation
     * @return     result of cross product */
    public Vector crossProduct(Vector vector){
        //cross product of two vectors (x1,y1,z1) and (x2,y2,z2)
        //is (y1*z2-z1*y2, z1*x2-x1*z2, x1*y2-y1*x2)
        return new Vector(
                this.xyz.d2*vector.xyz.d3-this.xyz.d3*vector.xyz.d2,
                this.xyz.d3*vector.xyz.d1-this.xyz.d1*vector.xyz.d3,
                this.xyz.d1*vector.xyz.d2-this.xyz.d2*vector.xyz.d1);
    }

    /** calculates length of the vector squared
     * @return     length of vector squared */
    public Double lengthSquared(){
        //for any vector v, |V|^2 = v * v [dot product]
        return dotProduct(this);
    }

    /** calculates length of the vector
     * @return     length of the vector */
    public Double length(){
        //for any vector v, |V| = sqrt(v * v) [dot product]
        return java.lang.Math.sqrt(this.lengthSquared());
    }

    /** normalizes the vector
     * @return     normalized vector */
    public Vector normalize(){
        return new Vector(this.xyz.reduce(this.length()));
    }
}
