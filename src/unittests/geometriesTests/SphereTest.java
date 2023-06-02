package unittests.geometriesTests;

import geometries.Intersectable;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 * @author Avishai Shachor and Yoav Babayof
 */
class SphereTest {
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from any point on sphere
        Sphere sph = new Sphere(new Point(1, 4, 5), 3);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> sph.getNormal(new Point(4, 4, 5)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = sph.getNormal(new Point(4, 4, 5));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Sphere's normal is not a unit vector");
        // ensure the result is orthogonal to sphere at the given point
        assertEquals(new Vector(1,0,0), result,"getNormal() wrong result");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(new Point (1, 0, 0), 1d);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),
                "TC01 fail: Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(3, 1, 0)));
        assertNotNull(result, "ray outside the line doesnt supply any intersection point");
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        p1 = new Point(1.8, 0.6, 0);
        result = sphere.findIntersections(new Ray(new Point(1.5,0,0), new Vector(1, 2, 0)));
        assertNotNull(result, "ray outside the line doesnt supply any intersection point");
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(p1, result.get(0), "wrong result");

        // TC04: Ray starts after the sphere (0 points)
        result = sphere.findIntersections(new Ray(new Point(2.5,0,0), new Vector(1,2,3)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        p1 = new Point(0.10491803278688483, 0.44590163934426236, 0);
        result = sphere.findIntersections(new Ray(new Point(2,0,0), new Vector(-8.5, 2, 0)));
        assertNotNull(result, "ray outside the line doesnt supply any intersection point");
        assertEquals(1, result.size(), " Wrong number of points");
        assertEquals(p1, result.get(0), "wrong result");

        // TC12: Ray starts at sphere and goes outside (0 points)
        result = sphere.findIntersections(new Ray(new Point (1,1,0), new Vector(1,1,0)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        p1 = new Point(2, 0, 0);
        p2 = new Point(0, 0, 0);
        result = sphere.findIntersections(new Ray(new Point(3, 0, 0), new Vector(-1, 0, 0)));
        assertNotNull(result, " ray outside the line doesnt supply any intersection point");
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p2, p1), result, "Ray crosses sphere");

        // TC14: Ray starts at sphere and goes inside (1 points)
        p1 = new Point(2,0,0);
        result = sphere.findIntersections(new Ray(new Point (0,0,0), new Vector(1,0,0)));
        assertNotNull(result,"ray doesnt supply any intersection point" );
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1),result, "wrong result");

        // TC15: Ray starts inside (1 points)
        p1 = new Point(2,0,0);
        result = sphere.findIntersections(new Ray(new Point (1.5,0,0), new Vector(1,0,0)));
        assertNotNull(result,"ray doesnt supply any intersection point" );
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1),result, "wrong result");

        // TC16: Ray starts at the center (1 points)
        p1 = new Point(1.9486832980505138,-0.31622776601683794,0);
        result = sphere.findIntersections(new Ray(new Point (1,0,0), new Vector(3,-1,0)));
        assertNotNull(result,"ray doesnt supply any intersection point" );
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1),result, "wrong result");

        // TC17: Ray starts at sphere and goes outside (0 points)
        result = sphere.findIntersections(new Ray(new Point (2,0,0), new Vector(1,0,0)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // TC18: Ray starts after sphere (0 points)
        result = sphere.findIntersections(new Ray(new Point (3,0,0), new Vector(1,0,0)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        result = sphere.findIntersections(new Ray(new Point (2,-0.5,0), new Vector(0,1,0)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // TC110: Ray starts at the tangent point
        result = sphere.findIntersections(new Ray(new Point (2,0,0), new Vector(0,1,0)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // TC111: Ray starts after the tangent point
        result = sphere.findIntersections(new Ray(new Point (2,0.5,0), new Vector(0,1,0)));
        assertNull(result, "supply intersection points when it's not supposed to");

        // **** Group: Special cases
        // TC112: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        result = sphere.findIntersections(new Ray(new Point (3,0,0), new Vector(0,1,0)));
        assertNull(result, "supply intersection points when it's not supposed to");
    }

    /** Test method for {@link geometries.Sphere#findGeoIntersections(Ray, double)}. */
    @Test
    void findIntersectionsWithMaxDistanceTest() {
        Sphere sphere = new Sphere(
                new Point(0,0,0),
                5
        );
        Ray ray = new Ray(
                new Point(10,0,0),
                new Vector(-1,0,0)
        );
        // TC01: ray intersects the sphere (1 point)
        List<Intersectable.GeoPoint> result =
                sphere.findGeoIntersections(ray, 6);
        assertEquals(1, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // TC02: ray intersects the sphere (2 point)
        result = sphere.findGeoIntersections(ray, 30);
        assertEquals(2, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // TC03: ray does not intersect the sphere (0 points)
        result = sphere.findGeoIntersections(ray, 2);
        assertNull(result,
                "findGeoIntersection(Ray, MaxDistance) wrong result");
    }
}