package primitivesTests;

import org.junit.jupiter.api.Test;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;


/**
 * Unit tests for primitives.Vector class
 * @author Avishai Shachor and Yoav Babayof
 */
class VectorTest {
    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(3, 6, 7);

        //TC01: test that scaling a vector by a positive factor is right
        assertEquals(new Vector(3.6,7.2,8.4), v1.scale(1.2), "scale() wrong result for scaling by a positive factor");

        //TC02: test that scaling a vector by a negative factor is right
        assertEquals(new Vector(-1.5,-3,-3.5), v1.scale(-0.5), "scale() wrong result for scaling by a negative factor");

        // =============== Boundary Values Tests ==================
        //TC11: test scaling a vector by a factor of 0
        assertThrows(IllegalArgumentException.class, () -> v1.scale(0),
                "scale() for scaling by 0 does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
     */
    @Test
    void testDotProduct() {
        Vector v1 = new Vector(3, 6, 7);

        // ============ Equivalence Partitions Tests ==============
        //TC01: Test that dot product of two vector with a acute angle is proper
        Vector v2 = new Vector(2, 7, 1);
        assertEquals(2*3 + 6*7 + 7*1, v1.dotProduct(v2), 0.00001, "dotProduct() wrong result for acute angle");

        //TC02: test that dot product of two vectors with a obtuse angle is proper
        Vector v3 = new Vector(-5,2,4);
        assertEquals((-5)*3+2*6+4*7,v1.dotProduct(v3),0.00001,"dotProduct() wrong result for obtuse angle" );


        // =============== Boundary Values Tests ==================
        //TC11: test dot product two orthogonal vectors
        Vector v4 = new Vector(-6, 3, 0);
        assertTrue(isZero(v1.dotProduct(v4)), "dotProduct() for two orthogonal vectors does not return 0");

        //TC12: test that dot product of two vectors when one of the vectors is a unit vector
        assertEquals((0.8*3+0.6*6+0*7), v1.dotProduct((new Vector(4,3,0)).normalize()),0.00001, "dotProduct() wrong result when one of the vectors is a unit vector");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
     */
    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper
        assertEquals(v1.length() * v2.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(primitives.Util.isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(primitives.Util.isZero(vr.dotProduct(v2)), "crossProduct() result is not orthogonal to 2nd operand");

        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross product of co-lined vectors
        Vector v3 = new Vector(-2, -4, -6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3),
                "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that length squared result of vector is proper
        assertEquals(115, new Vector(3, 5, 9).lengthSquared(), 0.00001, "distanceSquared() wrong result");
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test that length result of vector is proper
        assertEquals( 115,java.lang.Math.pow((new Vector (3, 5, 9)).length(), 2),0.00001,"length() wrong result");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(0.6, 0.8, 0);
        Vector u = v.normalize();

        // TC01: Test normalized vector is a unit vector
        assertEquals(1, u.length(), 0.00001, "normalize() result vector isn't a unit vector");

        // TC02: Test normalized vector is on the same line as the vector that was normalized
        assertThrows(IllegalArgumentException.class, () -> v.crossProduct(u),
                "normalize() normalized vector and the vector that was normalized are not on the same line");

        // TC02: Test normalized vector is on the same line as the vector that was normalized
        assertTrue(v.dotProduct(u) > 0, "normalize() normalized vector and the vector that was normalized are with opposite signs");
    }
}