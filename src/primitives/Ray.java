package primitives;

import java.util.List;

import static primitives.Util.isZero;

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
     * @throws IllegalArgumentException if t < 0
     */
    public Point getPoint(double t) {
        if (isZero(t)) return this.p0;
        if (t < 0) throw new IllegalArgumentException("point not on the ray");
        else return this.p0.add(this.dir.scale(t));
    }

    /** returns the closest point from list of points to the head point of the ray
     * @param points list of points
     * @return closest point to head of the ray, or null if the given list of points is empty
     */
    public Point findClosestPoint(List<Point> points){
        if (points == null || points.size() == 0) return null;
        double distance = this.p0.distance(points.get(0));
        double temp;
        int closest = 0;
        for (int i = 1; i < points.size(); ++i){
            temp = this.p0.distance(points.get(i));
            if(temp < distance){
                distance = temp;
                closest = i;
            }
        }
        return points.get(closest);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other) {
            return this.p0.equals(other.p0) && this.dir.equals(other.dir);
        }
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
