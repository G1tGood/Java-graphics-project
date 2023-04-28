package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.*;

/** This class represents camera of scene
 * @author Yoav Babayof and Avishai Shachor
 */
public class Camera {
    /** location of the camera */
    private Point location;
    /** up vector of camera */
    private Vector vUp;
    /** right vector of camera */
    private Vector vRight;
    /** forward vector of camera */
    private Vector vTo;
    /** View Plane height */
    double vpHeight;
    /** View Plane width */
    double vpWidth;
    /** View Plane distance from camera */
    double vpDistance;

    /** getter for height of View Plane
     * @return View Plane height */
    public double getVpHeight() {
        return vpHeight;
    }

    /** getter for width of View Plane
     * @return View Plane width */
    public double getVpWidth() {
        return vpWidth;
    }

    /** getter for distance of View Plane from Camera
     * @return distance of View Plane from Camera */
    public double getVpDistance() {
        return vpDistance;
    }

    /** constructor for Camera
     * @param location location of the camera
     * @param vTo up vector of the camera
     * @param vUp forward vector of the camera
     * @throws IllegalArgumentException if up vector and forward vector are not orthogonal
     */
    public Camera(Point location, Vector vTo, Vector vUp) {
        this.location = location;
        this.vUp = vUp.normalize();
        this.vTo = vTo.normalize();
        if (this.vUp.dotProduct(this.vTo) == 0) this.vRight = this.vTo.crossProduct(this.vUp);
        else throw new IllegalArgumentException("up vector and forward vector must be orthogonal");
    }

    /** setter for View Plane size
     * @param width width of the View Plane
     * @param height height of the View Plane
     * @return the camera
     * @throws IllegalArgumentException if width or height are not positive numbers
     */
    public Camera setVPSize(double width, double height){
        if (width < 0 || isZero(width) || height < 0 || isZero(height)) throw new IllegalArgumentException("height and width of view plane must be positive");
        this.vpWidth = width;
        this.vpHeight = height;
        return this;
    }

    /** setter for View Plane size
     * @param distance distance of view plane from camera
     * @return the camera
     * @throws IllegalArgumentException if distance is not positive
     */
    public Camera setVPDistance(double distance) {
        if (distance < 0 || isZero(distance)) throw new IllegalArgumentException("distance of view plane from camera must be positive");
        this.vpDistance = distance;
        return this;
    }

    /** constructs a ray from camera through center of a given pixel on view plane
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     * @return ray from camera through center of given pixel on view plane
     */
    public Ray constructRay(int nX, int nY, int j, int i){
        Point pIJ = this.location.add(this.vTo.scale(this.vpDistance));
        double yI = ((double) (nY-1)/2-i)*this.vpHeight/nY;
        double xJ = (j-((double) (nX-1)/2))*this.vpWidth/nX;
        if (!isZero(xJ)) pIJ = pIJ.add(this.vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(this.vUp.scale(yI));
        Vector vIJ = pIJ.subtract(this.location);
        return new Ray(this.location,vIJ);
    }
}
