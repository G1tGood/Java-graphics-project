package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;

/**
 * class Triangle is a basic class representing a triangle
 * of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public class Triangle extends Polygon {
    /** Constructor to initialize Triangle based on a normal vector and 3 points of the plane
     * @param p1 first point of the triangle
     * @param p2 second point of the triangle
     * @param p3 third point of the triangle
     */
    public Triangle(Point p1, Point p2, Point p3){
        super(p1, p2, p3);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p = this.plane.findIntersections(ray).get(0);
        Vector v1 = this.vertices.get(1).subtract(p);
        Vector v2 = this.vertices.get(2).subtract(p);
        Vector v3 = this.vertices.get(3).subtract(p);
        Vector n1 = v1.crossProduct(v2);
        Vector n2 = v2.crossProduct(v3);
        Vector n3 = v3.crossProduct(v1);
        Vector rayDir = ray.getDir();
        double vn1 = alignZero(rayDir.dotProduct(n1)), vn2 = alignZero(rayDir.dotProduct(n2)), vn3 = alignZero(rayDir.dotProduct(n3));
        if (isZero(vn1) || isZero(vn2) || isZero(vn3)) return null;
        if ((vn1 > 0 && vn2 > 0 && vn3 > 0) || (vn1 < 0 && vn2 < 0 && vn3 < 0)) return List.of(p);
        return null;
    }
}
