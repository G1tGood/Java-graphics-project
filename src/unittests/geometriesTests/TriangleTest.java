package unittests.geometriesTests;

import geometries.Intersectable;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for geometries.Triangle class
 * @author Avishai Shachor and Yoav Babayof
 */
class TriangleTest {
    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from a point on triangle
        Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Triangle tri = new Triangle(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> tri.getNormal(new Point(0, 1, 0)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = tri.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1]))),
                    "Triangle's normal is not orthogonal to one of the vectors of the plane");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle tri = new Triangle(new Point(1,0,0), new Point(0,1,0), new Point(0,0,2));
        // ============ Equivalence Partitions Tests ==============
        //TC01: ray does not intersect with triangle - intersection point with plane is
        //      between two rays going from one of the vertexes of the triangle (0 points)
        assertDoesNotThrow(()->tri.findIntersections(new Ray(new Point(3,1,3), new Vector(-7,-5,-7))), "findIntersections() throws an unexpected exception");
        assertNull(tri.findIntersections(new Ray(new Point(3,1,3), new Vector(-7,-5,-7))), "findIntersections() returns points");
        
        //TC02: ray does not intersect with triangle - intersection point with plane is
        //      between two rays going from two different the vertexes of the triangle (0 points)
        assertDoesNotThrow(()->tri.findIntersections(new Ray(new Point(3,0,-1), new Vector(-7,-3,-3))), "findIntersections() throws an unexpected exception");
        assertNull(tri.findIntersections(new Ray(new Point(3,0,-1), new Vector(-7,-3,-3))), "findIntersections() returns points");

        //TC03: ray intersects with triangle (1 point)
        assertDoesNotThrow(()->tri.findIntersections(new Ray(new Point(3,3,3), new Vector(-7,-7,-7))), "findIntersections() throws an unexpected exception");
        List<Point> result = tri.findIntersections(new Ray(new Point(3,3,3), new Vector(-7,-7,-7)));
        assertNotNull(result, "found no intersection points");
        assertEquals(List.of(new Point(0.4,0.4,0.4)),result, "findIntersections() wrong result");
        // =============== Boundary Values Tests ==================
        //TC11: the point is on a continuation of one of the edges (0 point)
        assertDoesNotThrow(()->tri.findIntersections(new Ray(new Point(0.5,0,0), new Vector(0.5,-0.2,0))), "findIntersections() throws an unexpected exception");
        assertNull(tri.findIntersections(new Ray(new Point(0.5,0,0), new Vector(0.5,-0.2,0))), "TC11 supply intersection points when it's not supposed to");

        //TC12: the point is on one of the edges (0 point)
        assertDoesNotThrow(()->tri.findIntersections(new Ray(new Point(0.1,0.1,0), new Vector(0.5,0.5,0))), "findIntersections() throws an unexpected exception");
        assertNull(tri.findIntersections(new Ray(new Point(0.1,0.1,0), new Vector(0.5,0.5,0))), "TC12 supply intersection points when it's not supposed to");

        //TC13: the point is on one of the triangle's vertices (0 point)
        assertDoesNotThrow(()->tri.findIntersections(new Ray(new Point(0.4,0,0), new Vector(1,0,0))), "findIntersections() throws an unexpected exception");
        assertNull(tri.findIntersections(new Ray(new Point(0.4,0.4,0), new Vector(1,0,0))), "TC13 supply intersection points when it's not supposed to");
    }

    /** Test method for {@link geometries.Triangle#findGeoIntersections(Ray, double)}. */
    @Test
    void findIntersectionsWithMaxDistanceTest() {
        Triangle triangle = new Triangle(
                new Point(0,-1,-1),
                new Point(0, 1 ,-1),
                new Point(0,0,1)
        );
        Ray ray = new Ray(
                new Point(4,0,0),
                new Vector(-1,0,0)
        );
        // ============ Equivalence Partitions Tests ==============
        // TC01: ray intersects the triangle (1 point)
        List<Intersectable.GeoPoint> result =
                triangle.findGeoIntersections(ray, 30);
        assertEquals(1, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // TC02: ray does not intersect the triangle (0 points)
        result = triangle.findGeoIntersections(ray, 2);
        assertNull(result,
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: ray exactly intersects the triangle (1 point)
        result = triangle.findGeoIntersections(ray, 4);
        assertEquals(1, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");
    }
}