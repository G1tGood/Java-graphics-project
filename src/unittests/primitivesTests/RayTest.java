package unittests.primitivesTests;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Ray class
 * @author Avishai Shachor and Yoav Babayof
 */
public class RayTest {
    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint(){
        Ray ray = new Ray(new Point(1,2,3), new Vector(1,0,0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: t > 0
        assertDoesNotThrow(()->ray.getPoint(3),
                "getPoint throws an and unexpected exception for t > 0");
        assertEquals(new Point(4, 2, 3), ray.getPoint(3),
                "getPoint() wrong result for t > 0");

        // TC01: t < 0
        assertDoesNotThrow(()->ray.getPoint(-3), "getPoint throws an and unexpected exception for t < 0");
        assertEquals(new Point(-2, 2, 3), ray.getPoint(-3), "getPoint() wrong result for t < 0");

        // =============== Boundary Values Tests ==================
        //TC11: t = 0
        assertDoesNotThrow(()->ray.getPoint(0), "getPoint throws an and unexpected exception for t = 0");
        assertEquals(new Point(1, 2, 3), ray.getPoint(0), "getPoint() wrong result for t = 0");
    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint(){
        Ray ray = new Ray(new Point(5,6,8), new Vector(1,1,1));

        // ============ Equivalence Partitions Tests ==============
        // TC01: closest point is in the middle of the list
        List<Point> points1 = List.of(new Point(0,5,8),new Point(2,5,8), new Point(4,6,8), new Point(5,6,10), new Point(6,8,10));
        assertEquals(new Point(4,6,8), ray.findClosestPoint(points1), "findClosestPoint does not return the proper point");

        // =============== Boundary Values Tests ==================
        // TC11: empty list
        List<Point> points2 = List.of();
        assertNull(ray.findClosestPoint(points2), "findClosestPoint() for empty list of points does not return null");

        // TC12: first point is closest to head of ray
        List<Point> points3 = List.of(new Point(5.5,6.2,8.2), new Point(1,1,1), new Point(2,2,2));
        assertEquals(new Point(5.5,6.2,8.2), ray.findClosestPoint(points3), "findClosestPoint() wrong result");

        // TC13: last point is closest to head of ray
        List<Point> points4 = List.of(new Point(2,2,2), new Point(5.5,6.2,8.2), new Point(1,1,1));
        assertEquals(new Point(5.5,6.2,8.2), ray.findClosestPoint(points4), "findClosestPoint() wrong result");
    }
}
