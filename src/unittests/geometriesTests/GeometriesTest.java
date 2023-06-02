package unittests.geometriesTests;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Geometries class
 * @author Avishai Shachor and Yoav Babayof
 */
public class GeometriesTest {
    /**
     * Test method for {@link geometries.Geometries#findIntersections(primitives.Ray)}.
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: some geometries intersect with ray and some do not
        Geometries geometries = new Geometries(
                new Sphere(new Point (1, 0, 0), 1d),
                new Triangle(new Point(1,0,0), new Point(0,1,0), new Point(0,0,2)),
                new Plane(new Point(1,0,0), new Point(0,1,0), new Point(0,0,1))
        );
        Ray finalRay = new Ray(new Point(1,1,1.5),new Vector(-3,-2,-2.5));
        assertDoesNotThrow(()->geometries.findIntersections(finalRay),
                "findIntersection() throws an unexpected exception");
        List<Point> result = geometries.findIntersections(finalRay);
        assertNotNull(result,
                "findIntersection() does not find any intersections");
        assertEquals(
                2,
                result.size(),
                "findIntersection() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: Geometries does not contain any geometries - empty list (0 points)
        Geometries geo = new Geometries();
        Ray finalRay1 = new Ray(new Point(1,1,1.5),new Vector(-3,-2,-2.5));
        assertDoesNotThrow(()->geo.findIntersections(finalRay1), "findIntersection() throws an unexpected exception");
        assertNull(geo.findIntersections(finalRay1), "find intersection find intersection points when it's not suppose to");

        // TC12: Geometries contain geometries but no geometry intersects with ray (0 points)
        Ray finalRay2 = new Ray(new Point(3,1,1.5), new Vector(1,0,-0.5));
        assertDoesNotThrow(()->geometries.findIntersections(finalRay2), "findIntersection() throws an unexpected exception");
        assertNull(geometries.findIntersections(finalRay2), "find intersection finds intersection points when it's not suppose to");

        // TC13: only one geometry in geometrics intersect with ray
        Ray finalRay3 = new Ray(new Point(4,4,1.5), new Vector(-8,-5,-2.5));
        assertDoesNotThrow(()->geometries.findIntersections(finalRay3), "findIntersection() throws an unexpected exception");
        result = geometries.findIntersections(finalRay3);
        assertNotNull(result, "findIntersection() does not find any intersections");
        assertEquals(1, result.size(), "findIntersection() wrong result");

        // TC14: all geometries in Geometries intersect in ray
        Ray finalRay4 = new Ray(new Point(3,1,1.5),new Vector(-7,-2,-2.5));
        assertDoesNotThrow(()->geometries.findIntersections(finalRay4), "findIntersection() throws an unexpected exception");
        result = geometries.findIntersections(finalRay4);
        assertNotNull(result, "findIntersection() does not find any intersections");
        assertEquals(4, result.size(), "findIntersection() wrong result");
    }

    /** Test method for {@link geometries.Geometries#findGeoIntersections(Ray, double)}. */
    @Test
    void findIntersectionsWithMaxDistanceTest() {
        Geometries geometries = new Geometries(
                new Sphere(
                        new Point (5, 5, 0),
                        1d),
                new Triangle(
                        new Point(1,0,0),
                        new Point(0,1,0),
                        new Point(0,0,1))
                );
        Ray ray = new Ray(
                new Point(6,6,0),
                new Vector(-1,-1,0.05)
        );
        // ============ Equivalence Partitions Tests ==============
        // TC01: intersects sphere twice and triangle once (3 points)
        List<Intersectable.GeoPoint> result =
                geometries.findGeoIntersections(ray, 30);
        assertEquals(3, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // TC02: intersects sphere twice (2 points)
        result = geometries.findGeoIntersections(ray, 3);
        assertEquals(2, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // TC03: intersects sphere once (1 point)
        result = geometries.findGeoIntersections(ray, 1);
        assertEquals(1, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // TC04: does not intersect anything (0 points)
        result = geometries.findGeoIntersections(ray, 0.001);
        assertNull(result,
                "findGeoIntersection(Ray, MaxDistance) wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: ray exactly intersects plane (3 points)
        geometries = new Geometries(
                new Sphere(
                        new Point (5, 0, 0),
                        1d
                ),
                new Plane(
                        new Point(0,0,0),
                        new Vector(1,0,0)
                )
        );
        ray = new Ray(
                new Point(7,0,0),
                new Vector(-1,0,0)
        );
        result = geometries.findGeoIntersections(ray, 7);
        assertEquals(3, result.size(),
                "findGeoIntersection(Ray, MaxDistance) wrong result");
    }
}
