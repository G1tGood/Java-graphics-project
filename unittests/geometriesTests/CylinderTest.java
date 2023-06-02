package geometriesTests;

import geometries.Cylinder;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Cylinder class
 * @author Avishai Shachor and Yoav Babayof
 */
class CylinderTest {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        Cylinder cyl = new Cylinder(new Ray(new Point(1, 2, 1), new Vector(0,1,0)), 4, 5);

        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting normal of a point around face of the cylinder
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cyl.getNormal(new Point(5, 3, 1)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result1 = cyl.getNormal(new Point(5, 3, 1));
        // ensure |result1| = 1
        assertEquals(1, result1.length(), 0.00000001, "Cylinder's normal is not a unit vector");
        // ensure the result is right
        assertEquals(new Vector(1,0,0), result1, "getNormal() wrong result");

        // TC02: test getting normal of a point on one of the bases of cylinder
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cyl.getNormal(new Point(4, 2, 1)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result2 = cyl.getNormal(new Point(4, 2, 1));
        // ensure |result2| = 1
        assertEquals(1, result2.length(), 0.00000001, "Cylinder's normal is not a unit vector");
        // ensure the result is right
        assertTrue(result2.equals(new Vector(0,1,0)) || result2.equals(new Vector(0,-1,0)), "getNormal() wrong result");

        // TC03: test getting normal of a point on the other base of the cylinder
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cyl.getNormal(new Point(4, 7, 1)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result3 = cyl.getNormal(new Point(4, 7, 1));
        // ensure |result3| = 1
        assertEquals(1, result3.length(), 0.00000001, "Cylinder's normal is not a unit vector");
        // ensure the result is right
        assertTrue(result3.equals(new Vector(0,1,0)) || result3.equals(new Vector(0,-1,0)), "getNormal() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: the given point in the center of one of the bases of the cylinder
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cyl.getNormal(new Point(1, 7, 1)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result4 = cyl.getNormal(new Point(1, 7, 1));
        // ensure |result4| = 1
        assertEquals(1, result4.length(), 0.00000001, "Cylinder's normal is not a unit vector");
        // ensure the result is right
        assertTrue(result4.equals(new Vector(0,1,0)) || result4.equals(new Vector(0,-1,0)), "getNormal() wrong result");

        // TC11: the given point in the center of the other base of the cylinder
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cyl.getNormal(new Point(1, 2, 1)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result5 = cyl.getNormal(new Point(1, 2, 1));
        // ensure |result5| = 1
        assertEquals(1, result5.length(), 0.00000001, "Cylinder's normal is not a unit vector");
        // ensure the result is right
        assertTrue(result5.equals(new Vector(0,1,0)) || result5.equals(new Vector(0,-1,0)), "getNormal() wrong result");
    }
}