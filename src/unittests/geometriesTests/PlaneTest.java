package unittests.geometriesTests;
import geometries.Plane;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 * @author Avishai Shachor and Yoav Babayof
 */
class PlaneTest {
    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test creating a plane with three different points not on the same line
        try {
            new Plane(new Point(1,0,0), new Point(0,1,0), new Point(0,0,1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a plane");
        }

        // =============== Boundary Values Tests ==================
        // TC11: test of boundary value where two points are the same
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(1, 0, 0)),
                "Constructed a plane with two identical points");

        // TC12: test of boundary value where all points are on the same line
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 2), new Point(0, 0, 3)),
                "Constructed a plane with 3 points on the same line");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from a point on plane
        Point[] pts = { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Plane pln = new Plane(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pln.getNormal(new Point(0, 1, 0)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = pln.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Plane's normal is not a unit vector");
        // ensure the result is orthogonal to plane
        assertTrue(pln.getNormal().equals(result) || pln.getNormal().equals(result.scale(-1)), "getNormal() wrong result");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections(){
        Plane plane = new Plane(new Point(1,0,0), new Vector(1, 0 ,0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray is not tangent nor parallel to the plane and intersect the plane (1 points)
        Point p = new Point(1,0.5,0);
        List<Point> result = plane.findIntersections(new Ray(new Point(0.5,0,0), new Vector(1, 1, 0)));
        assertNotNull(result, "TC01 ray outside the line doesnt supply any intersection point");
        assertEquals(1, result.size(), "TC01 Wrong number of points");
        assertEquals(p, result.get(0), "TC01 wrong result");

        // TC02: Ray is not tangent nor parallel to the plane and doesn't intersect the plane (0 points)
        result = plane.findIntersections(new Ray(new Point(0.5,0,0), new Vector(-1.5, 1, 0)));
        assertNull(result, "TC02 supply intersection points when it's not supposed to");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        // TC11: Ray is in the plane (0 points)
        result = plane.findIntersections(new Ray(new Point(1,1,0), new Vector(0, 1, 0)));
        assertNull(result, "TC11 supply intersection points when it's not supposed to");

        // TC12: Ray is not in the plane (0 points)
        result = plane.findIntersections(new Ray(new Point(2,0,0), new Vector(0, 1, 0)));
        assertNull(result, "TC12 supply intersection points when it's not supposed to");

        // **** Group: Ray is tangent to the plane
        // TC13: Ray starts in the plane (0 points)
        result = plane.findIntersections(new Ray(new Point(1,1,0), new Vector(1, 0, 0)));
        assertNull(result, "TC13 supply intersection points when it's not supposed to");

        // TC14: Ray starts before plane (1 points)
        p = new Point(1,0,0);
        result = plane.findIntersections(new Ray(new Point(-1,0,0), new Vector(1, 0, 0)));
        assertNotNull(result, "TC14 ray outside the line doesnt supply any intersection point");
        assertEquals(1, result.size(), "TC14 Wrong number of points");
        assertEquals(p, result.get(0), "TC14 wrong result");

        // TC15: Ray starts after in the plane (0 points)
        result = plane.findIntersections(new Ray(new Point(2,0,0), new Vector(1, 0, 0)));
        assertNull(result, "TC15 supply intersection points when it's not supposed to");

        // **** Group: Special cases
        // TC16: Ray starts in the plane but is not tangent nor parallel to in (0 points)
        result = plane.findIntersections(new Ray(new Point(1,1,0), new Vector(1, 1, 0)));
        assertNull(result, "TC16 supply intersection points when it's not supposed to");

        // TC17: Ray base point is the point that represent the plane (0 points)
        result = plane.findIntersections(new Ray(new Point(1,0,0), new Vector(1, 1, 0)));
        assertNull(result, "TC17 supply intersection points when it's not supposed to");
    }
}