package unittests.primitivesTests;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
        assertDoesNotThrow(()->ray.getPoint(3), "getPoint throws an and unexpected exception for t > 0");
        assertEquals(new Point(4, 2, 3), ray.getPoint(3), "getPoint() wrong result for t > 0");

        // TC01: t < 0
        assertThrows(IllegalArgumentException.class, //
                () -> ray.getPoint(-4), "getPoint() does not throw an exception for t < 0");

        // =============== Boundary Values Tests ==================
        //TC11: t = 0
        assertThrows(IllegalArgumentException.class,
                ()->ray.getPoint(0), "getPoint does not throw an exception for t = 0");
    }
}
