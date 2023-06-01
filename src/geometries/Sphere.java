package geometries;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;
import static java.lang.Math.*;
import static primitives.Util.*;

/**
 * class Sphere is a class representing a sphere
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Sphere extends RadialGeometry {
    /** center point of the sphere */
    final Point center;

    /** Constructor to initialize Sphere based on a center point and a radius of the sphere
     * @param center center of the sphere
     * @param radius radius of the sphere
     */
    public Sphere(Point center, double radius){
        super(radius);
        this.center = center;
    }

    /**
     * getter for the center point of the sphere
     * @return center point of the sphere
     */
    public Point getCenter() {
        return center;
    }

    @Override
    public Vector getNormal(Point point){
        return point.subtract(this.center).normalize();
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        double tm, d, th, t1, t2;
        if (this.center.equals(ray.getP0())) return List.of(new GeoPoint(this, ray.getPoint(this.radius))); // if center of sphere and base point of ray collide, just provide the point distant distance radius from the base point
        Vector u = this.center.subtract(ray.getP0());
        tm = alignZero(ray.getDir().dotProduct(u));
        d = sqrt(u.lengthSquared() - pow(tm, 2));
        if (d > this.radius || isZero(this.radius-d)) return null;
        th = alignZero(sqrt(pow(this.radius, 2) - pow(d, 2)));
        t1 = alignZero(tm - th);
        t2 = alignZero(tm + th);
        boolean positiveT1 = t1 > 0, positiveT2 = t2 > 0;
        if (positiveT1 && positiveT2 && alignZero(t1 - maxDistance) <= 0 && alignZero(t2 - maxDistance) <= 0) return List.of(new GeoPoint(this, ray.getPoint(t1)), new GeoPoint(this, ray.getPoint(t2)));
        else if (positiveT1 && alignZero(t1 - maxDistance) <= 0) return List.of(new GeoPoint(this, ray.getPoint(t1)));
        else if (positiveT2 && alignZero(t2 - maxDistance) <= 0) return List.of(new GeoPoint(this, ray.getPoint(t2)));
        else return null;
    }
}
