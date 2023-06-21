package renderer;

import geometries.Plane;
import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.MissingResourceException;

import static java.lang.Math.min;
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
    // general properties //
    /** location of the camera */
    private Point location;
    /** up vector of camera */
    private Vector vUp;
    /** right vector of camera */
    private Vector vRight;
    /** forward vector of camera */
    private Vector vTo;
    /** image writer */
    private ImageWriter imageWriter;
    /** ray tracer */
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

    /** setter for anti aliasing
     * @param amountRaysAntiAliasing amount of rays per pixel
     * @throws IllegalArgumentException if amountRaysAntiAliasing < 0
     * @return Camera
     */
    public Camera setAntiAliasing(int amountRaysAntiAliasing) {
        if (amountRaysAntiAliasing < 0) throw new IllegalArgumentException("amount of rays for anti aliasing cannot be less than 0");
        if (amountRaysAntiAliasing == 1) amountRaysAntiAliasing = 0;
        this.amountRaysAntiAliasing = amountRaysAntiAliasing;
        return this;
    }

    /**
     * setter for DOF improvment
     * @param amountRaysDOF amounts of rays of the DOF
     * @param focusDistance the distance between the focal point and the intersection point
     * @param apertureSize size of aperture side
     * @throws IllegalArgumentException if amountRaysDOF <= 0
     * @throws IllegalArgumentException if focusDistance <= 0
     * @throws IllegalArgumentException if apertureSize <= 0
     * @return Camera
     */
    public Camera setDOF(int amountRaysDOF,double focusDistance,double apertureSize) {
        if (amountRaysDOF < 1) throw new IllegalArgumentException("amount of rays for DOF amount of rays for anti aliasing cannot be less than 1");
        if (focusDistance <= 0) throw new IllegalArgumentException("focal plane distance from view plane cannot be less than or equals to 0");
        if (apertureSize <= 0) throw new IllegalArgumentException("aperture size cannot be less than or equals to 0");
        this.amountRaysDOF = amountRaysDOF;
        this.focusDistance = focusDistance;
        this.apertureSize = apertureSize;
        return this;
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
        Point pIJ = getPij(nX, nY, j, i);
        Vector vIJ = pIJ.subtract(this.location);
        return new Ray(this.location,vIJ);
    }

    /** constructs a beam of rays from camera through multiple points in a given pixel on view plane
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     * @return beam of rays from camera through center of given pixel on view plane
     */
    public LinkedList<Ray> constructBeam(int nX, int nY, int j, int i){
        Blackboard ta = new Blackboard(getPij(nX,nY,j,i),this.vUp,this.vRight, min(this.vpWidth/nX,this.vpHeight/nY), amountRaysAntiAliasing);
        return Ray.generateBeam(this.location,ta);
    }

    /** renders image
     * @throws MissingResourceException if one or more of the fields of Camera were not initialized
     * @return Camera */
    public Camera renderImage() {
        if (this.vUp == null || this.vTo == null || this.vRight == null || this.vpDistance == 0.0 || this.location == null || this.vpHeight == 0.0 || this.vpWidth == 0.0 || this.imageWriter == null || this.rayTracer == null)
            throw new MissingResourceException("one or more of the fields of Camera was not initialized", "", "");
        if (amountRaysAntiAliasing != 0) {
            int nX = this.imageWriter.getNx(), nY = this.imageWriter.getNy();
            for (int i = 0; i < nY; ++i) {
                for (int j = 0; j < nX; ++j) {
                    imageWriter.writePixel(j, i, castBeam(this.imageWriter.getNx(), this.imageWriter.getNy(), j, i));
                }
            }
        }
        else {
            int nX = this.imageWriter.getNx(), nY = this.imageWriter.getNy();
            for (int i = 0; i < nY; ++i) {
                for (int j = 0; j < nX; ++j) {
                    imageWriter.writePixel(j, i, castRay(this.imageWriter.getNx(), this.imageWriter.getNy(), j, i));
                }
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

    /** returns point in the middle of pixel i,j
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     * @return point in the middle of pixel i,j
     */
    private Point getPij(int nX, int nY, int j, int i) {
        Point pIJ = this.location.add(this.vTo.scale(this.vpDistance));
        double yI = ((double) (nY-1)/2-i)*this.vpHeight/nY;
        double xJ = (j-((double) (nX-1)/2))*this.vpWidth/nX;
        if (!isZero(xJ)) pIJ = pIJ.add(this.vRight.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(this.vUp.scale(yI));
        return pIJ;
    }

    /** casts a ray through a pixel and returns its color
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     * @return color of traced ray
     */
    private Color castRay(int nX, int nY, int j, int i){
        if (this.focusDistance != 0) { // DOF on
            return calcDOF(getPij(nX, nY, j, i));
        }
        else { // DOF off
            Ray ray = this.constructRay(nX, nY, j, i);
            return rayTracer.traceRay(ray);
        }
    }

    /** casts a beam of rays through a pixel and returns their average color
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     * @return color of average color of traced rays
     */
    private Color castBeam(int nX, int nY, int j, int i){
        if (focusDistance != 0) { // DOF active
            Blackboard ta = new Blackboard(getPij(nX,nY,j,i),this.vUp,this.vRight, min(this.vpWidth/nX,this.vpHeight/nY), amountRaysAntiAliasing);
            Color color = new Color(java.awt.Color.BLACK);
            for (Point point:ta.getPoints()) {
                color = color.add(calcDOF(point));
            }
            return color.reduce(amountRaysAntiAliasing);
        }
        else {  // DOF not active
            LinkedList<Ray> beam = this.constructBeam(nX,nY,j,i);
            return averageBeamColor(beam);
        }
    }

    /** calculates the average color of all rays in beam
     * @param beam beam of rays
     * @return  the average color of all rays in beam */
    private Color averageBeamColor(LinkedList<Ray> beam) {
        Color color = Color.BLACK;
        for (Ray ray: beam)
            color = color.add(rayTracer.traceRay(ray));
        return color.reduce(beam.size());
    }

    /** calculates color of a point in view plane with DOF improvement
     * @param point point at view plane
     * @return color of point with DOF improvement*/
    private Color calcDOF(Point point) {
        Blackboard ta = new Blackboard(point,this.vUp,this.vRight, apertureSize, amountRaysDOF);
        return averageBeamColor(Ray.generateBeam(getFocalPoint(point),ta, true));
    }

    /** getter of focal point on focal plane for a certain point in view plane
     * @param point point at view plane
     * @return focal point on focal plane for point
     */
    private Point getFocalPoint(Point point) {
        Vector dir = point.subtract(this.location).normalize();
        double distance = this.focusDistance/(dir.dotProduct(this.vTo));
        return point.add(dir.scale(distance));
    }

    // todo: implement camera rotation (bonus)
}
