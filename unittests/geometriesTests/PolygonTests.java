package geometriesTests;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

import geometries.Intersectable;
import org.junit.jupiter.api.Test;

import geometries.Polygon;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/** Testing Polygons
 * @author dan */
public class PolygonTests {

   /** Test method for {@link geometries.Polygon#Polygon(primitives.Point...)}. */
   @Test
   public void testConstructor() {
      // ============ Equivalence Partitions Tests ==============

      // TC01: Correct concave quadrangular with vertices in correct order
      try {
         new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1));
      } catch (IllegalArgumentException e) {
         fail("Failed constructing a correct polygon");
      }

      // TC02: Wrong vertices order
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(0, 1, 0), new Point(1, 0, 0), new Point(-1, 1, 1)), //
                   "Constructed a polygon with wrong order of vertices");

      // TC03: Not in the same plane
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 2, 2)), //
                   "Constructed a polygon with vertices that are not in the same plane");

      // TC04: Concave quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0.5, 0.25, 0.5)), //
                   "Constructed a concave polygon");

      // =============== Boundary Values Tests ==================

      // TC10: Vertex on a side of a quadrangular
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0),
                                     new Point(0, 0.5, 0.5)),
                   "Constructed a polygon with vertex on a side");

      // TC11: Last point = first point
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 0, 1)),
                   "Constructed a polygon with vertices on a side");

      // TC12: Co-located points
      assertThrows(IllegalArgumentException.class, //
                   () -> new Polygon(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(0, 1, 0)),
                   "Constructed a polygon with vertices on a side");

   }

   /** Test method for {@link geometries.Polygon#getNormal(primitives.Point)}. */
   @Test
   public void testGetNormal() {
      // ============ Equivalence Partitions Tests ==============
      // TC01: There is a simple single test here - using a quad
      Point[] pts =
         { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
      Polygon pol = new Polygon(pts);
      // ensure there are no exceptions
      assertDoesNotThrow(() -> pol.getNormal(new Point(0, 0, 1)), "");
      // generate the test result
      Vector result = pol.getNormal(new Point(0, 0, 1));
      // ensure |result| = 1
      assertEquals(1, result.length(), 0.00000001, "Polygon's normal is not a unit vector");
      // ensure the result is orthogonal to all the edges
      for (int i = 0; i < 3; ++i)
         assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 3 : i - 1]))),
                    "Polygon's normal is not orthogonal to one of the edges");
   }

   /**
    * Test method for {@link geometries.Polygon#findIntersections(primitives.Ray)}.
    */
   @Test
   public void testFindIntersections() {
      Polygon pol = new Polygon(new Point(1,0,0), new Point(0,1,0), new Point(0,0,2));
      // ============ Equivalence Partitions Tests ==============
      //TC01: ray does not intersect with polygon - intersection point with plane is
      //      between two rays going from one of the vertexes of the polygon (0 points)
      assertDoesNotThrow(()->pol.findIntersections(new Ray(new Point(3,1,3), new Vector(-7,-5,-7))), "findIntersections() throws an unexpected exception");
      assertNull(pol.findIntersections(new Ray(new Point(3,1,3), new Vector(-7,-5,-7))), "findIntersections() returns points");

      //TC02: ray does not intersect with polygon- intersection point with plane is
      //      between two rays going from two different the vertexes of the polygon (0 points)
      assertDoesNotThrow(()->pol.findIntersections(new Ray(new Point(3,0,-1), new Vector(-7,-3,-3))), "findIntersections() throws an unexpected exception");
      assertNull(pol.findIntersections(new Ray(new Point(3,0,-1), new Vector(-7,-3,-3))), "findIntersections() returns points");

      //TC03: ray intersects with polygon (1 point)
      assertDoesNotThrow(()->pol.findIntersections(new Ray(new Point(3,3,3), new Vector(-7,-7,-7))), "findIntersections() throws an unexpected exception");
      List<Point> result = pol.findIntersections(new Ray(new Point(3,3,3), new Vector(-7,-7,-7)));
      assertNotNull(result, "found no intersection points");
      assertEquals(List.of(new Point(0.4,0.4,0.4)),result, "findIntersections() wrong result");
      // =============== Boundary Values Tests ==================
      //TC11: the point is on a continuation of one of the edges (0 point)
      assertDoesNotThrow(()->pol.findIntersections(new Ray(new Point(0.5,0,0), new Vector(0.5,-0.2,0))), "findIntersections() throws an unexpected exception");
      assertNull(pol.findIntersections(new Ray(new Point(0.5,0,0), new Vector(0.5,-0.2,0))), "TC11 supply intersection points when it's not supposed to");

      //TC12: the point is on one of the edges (0 point)
      assertDoesNotThrow(()->pol.findIntersections(new Ray(new Point(0.1,0.1,0), new Vector(0.5,0.5,0))), "findIntersections() throws an unexpected exception");
      assertNull(pol.findIntersections(new Ray(new Point(0.1,0.1,0), new Vector(0.5,0.5,0))), "TC12 supply intersection points when it's not supposed to");

      //TC13: the point is on one of the polygon's vertices (0 point)
      assertDoesNotThrow(()->pol.findIntersections(new Ray(new Point(0.4,0,0), new Vector(1,0,0))), "findIntersections() throws an unexpected exception");
      assertNull(pol.findIntersections(new Ray(new Point(0.4,0.4,0), new Vector(1,0,0))), "TC13 supply intersection points when it's not supposed to");
   }

   /** Test method for {@link geometries.Polygon#findGeoIntersections(Ray, double)}. */
   @Test
   void findIntersectionsWithMaxDistanceTest() {
      Polygon polygon = new Polygon(
              new Point(0,-1,-1),
              new Point(0, 1 ,-1),
              new Point(0,0,1)
      );
      Ray ray = new Ray(
              new Point(4,0,0),
              new Vector(-1,0,0)
      );
      // ============ Equivalence Partitions Tests ==============
      // TC01: ray intersects the polygon (1 point)
      List<Intersectable.GeoPoint> result =
              polygon.findGeoIntersections(ray, 30);
      assertEquals(1, result.size(),
              "findGeoIntersection(Ray, MaxDistance) wrong result");

      // TC02: ray does not intersect the polygon (0 points)
      result = polygon.findGeoIntersections(ray, 2);
      assertNull(result,
              "findGeoIntersection(Ray, MaxDistance) wrong result");

      // =============== Boundary Values Tests ==================
      // TC11: ray exactly intersects the polygon (1 point)
      result = polygon.findGeoIntersections(ray, 4);
      assertEquals(1, result.size(),
              "findGeoIntersection(Ray, MaxDistance) wrong result");
   }
}
