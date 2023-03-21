package geometries;

import primitives.Point;

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
}
