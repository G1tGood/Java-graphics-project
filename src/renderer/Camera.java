package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.*;

/** This class represents camera of scene
 * @author Yoav Babayof and Avishai Shachor
 */
public class Camera {
    // ------[PUBLIC FIELDS]------- //
    /** View Plane height */
    double vpHeight;
    /** View Plane width */
    double vpWidth;
    /** View Plane distance from camera */
    double vpDistance;

    // ------[PRIVATE FIELDS]------- //
    // location of the camera
    private Point location;
    // up vector of camera
    private Vector vUp;
    // right vector of camera
    private Vector vRight;
    // forward vector of camera
    private Vector vTo;
    // image writer
    private ImageWriter imageWriter;
    // ray tracer
    private RayTracerBase rayTracer;

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

    /** setter for image writer
     * @param imageWriter image writer
     * @return Camera
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    /** setter for ray tracer
     * @param rayTracerBase ray tracer
     * @return Camera
     */
    public Camera setRayTracer(RayTracerBase rayTracerBase) {
        this.rayTracer = rayTracerBase;
        return this;
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

    /** renders image
     * @throws MissingResourceException if one or more of the fields of Camera were not initialized
     * @return Camera */
    public Camera renderImage() {
        if (this.vUp == null || this.vTo == null || this.vRight == null || this.vpDistance == 0.0 || this.location == null || this.vpHeight == 0.0 || this.vpWidth == 0.0 || this.imageWriter == null || this.rayTracer == null)
            throw new MissingResourceException("one or more of the fields of Camera was not initialized", "", "");
        int nX = this.imageWriter.getNx(), nY = this.imageWriter.getNy();
        for (int i = 0; i < nY; ++i){
            for (int j = 0; j < nX; ++j) {
                imageWriter.writePixel(j, i, castRay(this.imageWriter.getNx(),this.imageWriter.getNy(),j,i));
            }
        }
        return this;
    }

    /** prints grid of squares with a certain color of lines between
     *  @param interval interval between lines of grid
     *  @param color color of lines between squares
     *  @throws MissingResourceException if image writer field was not initialized */
    public void printGrid(int interval, Color color) {
        if (imageWriter == null) throw new MissingResourceException("image writer field was not initialized", "ImageWriter", "imageWriter");
        int nX = this.imageWriter.getNx(), nY = this.imageWriter.getNy();
        for (int j = 0; j < nX; j+=interval) {
            for (int i = 0; i < nY; ++i) {
                imageWriter.writePixel(j,i, color);
            }
        }
        for (int i = 0; i < nY; i+=interval) {
            for (int j = 0; j < nX; ++j) {
                imageWriter.writePixel(j,i, color);
            }
        }
    }

    /** Function writeToImage produces unoptimized png file of the image according to
	 * pixel color matrix in the directory of the project
     *  @throws MissingResourceException if image writer field was not initialized */
    public void writeToImage() {
        if (imageWriter == null) throw new MissingResourceException("image writer field was not initialized", "ImageWriter", "imageWriter");
        this.imageWriter.writeToImage();
    }

    //casts a ray through a pixel and returns its color
    private Color castRay(int nX, int nY, int j, int i){
        Ray ray = this.constructRay(nX,nY,j,i);
        return rayTracer.traceRay(ray);
    }

    // TODO: implement camera rotation (bonus)
}
