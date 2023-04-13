package unittests.geometriesTests;

import geometries.Tube;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Tube class
 * @author Avishai Shachor and Yoav Babayof
 */
class TubeTest {
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from any point on tube
        Tube tube = new Tube(new Ray(new Point(1, 2, 1), new Vector(0,1,0)), 4);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> tube.getNormal(new Point(5, 5, 1)), "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = tube.getNormal(new Point(5, 5, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Tube's normal is not a unit vector");
        // ensure the result is right
        assertEquals(new Vector(1,0,0), result, "getNormal() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test of boundary value of which direction vector is orthogonal
        //       to vector from given point to base point of the ray defining the tube
        Vector result1 = tube.getNormal(new Point(5, 2, 1));
        // ensure there are no exceptions
        assertDoesNotThrow(() -> tube.getNormal(new Point(5, 2, 1)), "getNormal() throws an unexpected exception");
        // ensure |result1| = 1
        assertEquals(1, result1.length(), 0.00000001, "Tube's normal is not a unit vector");
        // ensure the result is right
        assertEquals(new Vector(1,0,0), result1, "getNormal() wrong result");
    }
}