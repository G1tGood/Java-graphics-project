package primitivesTests;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for primitives.Point class
 * @author Avishai Schachor and Yoav Babayof
 */
class PointTest {
    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1,2,3);
        Point p2 = new Point(6,4,8);

        //TC01: test that subtraction result is right
        assertEquals(new Vector(-5,-2,-5), p1.subtract(p2), "subtract() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from subtraction of the same point from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "subtract() for same point does not throw an exception");    }

    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(3,6,8);
        Vector v1 = new Vector(6,4,8);

        // TC01: test that addition result is right
        assertEquals(new Point(9,10,16), p1.add(v1), "add() wrong result");

        // =============== Boundary Values Tests ==================
        Vector nP1 = new Vector(-3, -6, -8);

        // TC11: test addition of point to its opposite (result zero)
        try {
            assertEquals(new Point(0,0,0), p1.add(nP1), "add() subtracting point from itself result in error");
        } catch (IllegalArgumentException e) {
            fail("add() subtracting point to itself result in ERROR");
        }
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(3,6,8);
        Point p2 = new Point(15,2,9);

        // TC01: test that distance squared result is right
        assertEquals(161, p1.distanceSquared(p2), 0.00001, "distanceSquared() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test distanceSquared zero from distance squared of the same point from itself
        try {
            assertTrue(primitives.Util.isZero(p1.distanceSquared(p1)), "distanceSquared() when equals 0 wrong result");
        } catch (IllegalArgumentException e) {
            fail("distanceSquared() distance from point to itself ERROR");
        }
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(3,6,8);
        Point p2 = new Point(15,2,9);

        //TC01: test that distance result is right
        assertEquals(161, java.lang.Math.pow(p1.distance(p2), 2),0.00001, "distance() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test distance zero from distance of the same point from itself
        assertTrue(primitives.Util.isZero(p1.distance(p1)), "distance() when equals 0 wrong result");
    }
}