package unittests;

import static org.junit.jupiter.api.Assertions.*;
import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.Camera;

import java.util.List;

/**
 * integration tests for constructRay and findIntersections
 * @author Avishai Schachor and Yoav Babayof
 */
public class IntegrationTests {
    static final Point ZERO_POINT = new Point(0, 0, 0);

    /**
     * Test method for integration of {@link renderer.Camera#constructRay(int, int, int, int)} and {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    void CameraSphereIntersections() {
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, 1, 0)).setVPSize(3,3).setVPDistance(1);
        int nX = 3, nY = 3;

        // TC01: sphere in front of view plane and camera, r=1 (2 points)
        assertEquals(2, findAmountOfIntersectionPoints(nX,nY,new Sphere(new Point(0,0,-3), 1),camera),
                "sphere findIntersections and constructRay integration failed: wrong amount of points");

        // TC02: sphere intersects with view plane, r=2.5 (18 points)
        camera = new Camera(new Point(0,0,0.5), new Vector(0, 0, -1), new Vector(0, 1, 0)).setVPSize(3,3).setVPDistance(1);
        assertEquals(18, findAmountOfIntersectionPoints(nX,nY,new Sphere(new Point(0,0,-2.5), 2.5),camera),
                "sphere findIntersections and constructRay integration failed: wrong amount of points");

        // TC03: sphere intersects with view plane, r=2 (10 points)
        assertEquals(10, findAmountOfIntersectionPoints(nX,nY,new Sphere(new Point(0,0,-2), 2),camera),
                "sphere findIntersections and constructRay integration failed: wrong amount of points");

        // TC04: sphere contains view plane and camera, r=4 (9 points)
        camera = new Camera(new Point(0,0,0), new Vector(0, 0, -1), new Vector(0, 1, 0)).setVPSize(3,3).setVPDistance(1);
        assertEquals(9, findAmountOfIntersectionPoints(nX,nY,new Sphere(new Point(0,0,-1.5), 4),camera),
                "sphere findIntersections and constructRay integration failed: wrong amount of points");

        // TC05: is behind view plane and camera, r=4 (0 points)
        assertEquals(0, findAmountOfIntersectionPoints(nX,nY,new Sphere(new Point(0,0,1), 0.5),camera),
                "sphere findIntersections and constructRay integration failed: wrong amount of points");
    }

    /**
     * Test method for integration of {@link renderer.Camera#constructRay(int, int, int, int)} and {@link geometries.Plane#findIntersections(Ray)}.
     */
    @Test
    void CameraPlaneIntersections() {
        Camera camera = new Camera(ZERO_POINT,new Vector(0,0,-1),new Vector(0,1,0)).setVPSize(3,3).setVPDistance(1);
        int nX = 3, nY = 3;

        // TC01: view plane is parallel to plane (9 points)
        assertEquals(9,findAmountOfIntersectionPoints(nX,nY,new Plane(new Point(0,0,-1),new Vector(0,0,1)),camera),
                "plane findIntersections and constructRay integration failed: wrong amount of points");

        // TC02: plane intersects view plane's plane (9 points)
        assertEquals(9,findAmountOfIntersectionPoints(nX,nY,new Plane(new Point(0,0,-1),new Vector(0,-3,6)),camera),
                "ConstructRay from Camera to Plane failed when the angle between camera and plane is small");

        // TC03: plane intersects view plane's plane (6 points)
        assertEquals(6,findAmountOfIntersectionPoints(nX,nY, new Plane(new Point(0,0,-1),new Vector(0,-4,1)),camera),
                "ConstructRay from Camera to Plane failed when the angle between camera and plane is large");

    }

    /**
     * Test method for integration of {@link renderer.Camera#constructRay(int, int, int, int)} and {@link geometries.Triangle#findIntersections(Ray)}.
     */
    @Test
    void CameraTriangleIntersections() {
        Camera camera = new Camera(ZERO_POINT, new Vector(0, 0, -1), new Vector(0, 1, 0)).setVPSize(3,3).setVPDistance(1);
        int nX = 3, nY = 3;

        //TC01 - triangle in front of the view plane (1 point)
        assertEquals(1, findAmountOfIntersectionPoints(nX,nY,new Triangle(new Point(0,1,-2), new Point(1,-1,-2), new Point(-1,-1,-2)), camera),"triangle findIntersections and constructRay integration failed: wrong amount of points");

        //TC02 - triangle in front of the view plane (2 points)
        assertEquals(2, findAmountOfIntersectionPoints(nX,nY,new Triangle(new Point(0,20,-2), new Point(1,-1,-2), new Point(-1,-1,-2)), camera),"triangle findIntersections and constructRay integration failed: wrong amount of points");
    }

    // help function: gets amount of pixels in a row, column, a geometry and a camera and returns how many intersection points there are
    private int findAmountOfIntersectionPoints(int nX, int nY, Intersectable geometry, Camera camera){
        int count = 0;
        List<Point> intersectionPoints = null;
        for (int i = 0; i < nX; ++i) {
            for (int j = 0; j < nY; ++j) {
                intersectionPoints = geometry.findIntersections(camera.constructRay(nX,nY,j,i));
                if (intersectionPoints != null) count += intersectionPoints.size();
            }
        }
        return count;
    }
}