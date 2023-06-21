package primitives;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

import geometries.Intersectable.GeoPoint;
import renderer.Blackboard;

/**
 * Class Ray is the basic class representing a ray of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Ray {
    /** ray base shifting constant */
    private static final double DELTA = 0.1;

    /** starting point of the ray */
    final Point p0;
    /** direction vector of the ray */
    final Vector dir;

    /** generates a beam of rays from an origin point to points on a target area
     * @param originPoint origin point for rays
     * @param ta target area
     * @return returns a beam of rays from an origin point to points on a target area
     */
    public static LinkedList<Ray> generateBeam(Point originPoint, Blackboard ta) {
        LinkedList<Ray> rays = new LinkedList<>();
        for (Point targetPoint:ta.getPoints()){
            rays.add(new Ray(originPoint ,targetPoint.subtract(originPoint)));
        }
        return rays;
    }

    /** generates a beam of rays from an origin point to points on a target area
     * @param originPoint origin point for rays
     * @param ta target area
     * @param isReversed are the points from the target area to the origin point or the other way around
     * @return returns a beam of rays from an origin point to points on a target area
     */
    public static LinkedList<Ray> generateBeam(Point originPoint, Blackboard ta, boolean isReversed) {
        LinkedList<Ray> rays = new LinkedList<>();
        if (isReversed) {
            for (Point targetPoint : ta.getPoints()) {
                rays.add(new Ray(targetPoint, originPoint.subtract(targetPoint)));
            }
        }
        else {
            for (Point targetPoint : ta.getPoints()) {
                rays.add(new Ray(originPoint, targetPoint.subtract(originPoint)));
            }
        }
        return rays;
    }

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

    /** Constructor to initialize Ray based on a point, light direction and a given normal to geometry at a certain point
     * the constructor builds the ray moved in the towards the light source on normal by DELTA
     * @param p0 starting point of the ray
     * @param dir direction of light
     * @param n given normal to geometry at a certain point
     */
    public Ray(Point p0, Vector dir, Vector n) {
        this.dir = dir.normalize();
        double nDir = alignZero(dir.dotProduct(n));
        if (isZero(nDir)) this.p0 = p0;
        else if (nDir < 0) this.p0 = p0.add(n.scale(-DELTA));
        else this.p0 = p0.add(n.scale(DELTA));
    }

    /** returns the point distant t distance from base point in direction of the ray
     * @param t distance across the ray
     * @return point distant t distance from base point in direction of the ray
     */
    public Point getPoint(double t) {
        if (isZero(t)) return this.p0;
        else return this.p0.add(this.dir.scale(t));
    }

    /** returns the closest GeoPoint from list of GeoPoints to the head point of the ray
     * @param geoPoints list of points
     * @return GeoPoint with point closest to head of the ray, or null if the given list of geoPoints is empty
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPoints){
        if (geoPoints == null || geoPoints.size() == 0) return null;
        double distance = this.p0.distance(geoPoints.get(0).point);
        double temp;
        int closest = 0;
        for (int i = 1; i < geoPoints.size(); ++i){
            temp = this.p0.distance(geoPoints.get(i).point);
            if(temp < distance){
                distance = temp;
                closest = i;
            }
        }
        return geoPoints.get(closest);
    }

    /** returns the closest point from list of points to the head point of the ray
     * @param points list of points
     * @return closest point to head of the ray, or null if the given list of points is empty
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
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
