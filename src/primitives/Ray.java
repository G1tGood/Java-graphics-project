package primitives;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Ray {
    /** starting point of the ray */
    final Point p0;
    /** direction vector of the ray */
    final Vector dir;

    /** getter for starting point of the ray p0
     * @return p0 */
    public Point getP0() {
        return p0;
    }

    /** getter for direction vector of the ray dir
     * @return dir */
    public Vector getDir() {
        return dir;
    }

    /** Constructor to initialize Ray based on point and a vector
     * @param p0 starting point of the ray
     * @param dir direction vector of the ray */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }

    /** returns the point distant t distance from base point in direction of the ray
     * @param t distance across the ray
     * @return point distant t distance from base point in direction of the ray
     */
    public Point getPoint(double t) {
        if (t < 0) throw new IllegalArgumentException("point not on the ray");
        else return this.p0.add(this.dir.scale(t));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other)
            return super.equals(other);
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Ray: " + "starting point = " + p0.toString() + ", direction = " + dir.toString();
    }
}
