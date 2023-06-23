package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.*;

import static java.lang.Math.min;
import static primitives.Util.*;

/** This class represents camera of scene
 * @author Yoav Babayof and Avishai Shachor
 */
public class Camera {
    /** interface for four-variables lambda function */
    @FunctionalInterface
    interface FourConsumer<A,B,C,D> {
        void accept(A a, B b, C c, D d);
    }

    // ------[PUBLIC FIELDS]------- //
    /** View Plane height */
    double vpHeight;
    /** View Plane width */
    double vpWidth;
    /** View Plane distance from camera */
    double vpDistance;

    // ------[PRIVATE FIELDS]------- //
    // pixel manager
    /** Pixel manager for supporting:
     * <ul>
     * <li>multi-threading</li>
     * <li>debug print of progress percentage in Console window/tab</li>
     * <ul>
     */
    private PixelManager pixelManager;
    /** time every each printing of percentage of completion */
    private long printInterval;
    /** amount of simultaneous threads running */
    int threadsCount;
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

    // improved ray tracing //
    // anti aliasing:
    /** amount of rays for anti aliasing */
    private int amountRaysAntiAliasing;
    // depth of field:
    /** amount of rays for depth of field */
    private int amountRaysDOF;
    /** distance of focal plane from camera */
    private double focusDistance;
    /** aperture side length */
    private double apertureSize;
    /** ray(s) casting function */
    FourConsumer<Integer, Integer, Integer, Integer> rayCastFunc = this::castRay;
    /** function to calculate ray color */
    Function<Ray,Color> rayColorFunc = ray -> rayTracer.traceRay(ray);

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

    /** setter for multithreading improvement
     * @param threadsCount number of threads that we want to use for the program
     * @param printInterval interval between prints
     * @return
     */
    public Camera setMultiThreading(int threadsCount, long printInterval) {
        if (threadsCount < 0) throw new IllegalArgumentException("thread amount cannot be less than 0 threads");
        if (printInterval <= 0) throw new IllegalArgumentException("print interval cannot be less than or equal to 0");
        this.threadsCount = threadsCount;
        this.printInterval = printInterval;
        return this;
    }

    /** setter for anti aliasing
     * @param amountRaysAntiAliasing amount of rays per pixel
     * @throws IllegalArgumentException if amountRaysAntiAliasing < 0
     * @return Camera
     */
    public Camera setAntiAliasing(int amountRaysAntiAliasing) {
        if (amountRaysAntiAliasing < 0) throw new IllegalArgumentException("amount of rays for anti aliasing cannot be less than 0");
        if (amountRaysAntiAliasing == 0 || amountRaysAntiAliasing == 1) return this;
        this.amountRaysAntiAliasing = amountRaysAntiAliasing;
        this.rayCastFunc = this::castBeam;
        return this;
    }

    /**
     * setter for DOF improvement
     * @param amountRaysDOF amounts of rays of the DOF
     * @param focusDistanceFromVp the distance between the focal plane and view plane
     * @param apertureSize size of aperture side
     * @throws IllegalArgumentException if amountRaysDOF <= 0
     * @throws IllegalArgumentException if focusDistance <= 0
     * @throws IllegalArgumentException if apertureSize <= 0
     * @return Camera
     */
    public Camera setDOF(int amountRaysDOF,double focusDistanceFromVp,double apertureSize) {
        if (amountRaysDOF <= 0) throw new IllegalArgumentException("amount of rays for DOF amount of rays for anti aliasing cannot be less than 1");
        if (focusDistanceFromVp <= 0) throw new IllegalArgumentException("focal plane distance from view plane cannot be less than or equals to 0");
        if (apertureSize <= 0) throw new IllegalArgumentException("aperture size cannot be less than or equals to 0");
        this.amountRaysDOF = amountRaysDOF;
        this.focusDistance = focusDistanceFromVp + this.vpDistance;
        this.apertureSize = apertureSize;
        this.rayColorFunc = this::calcDOF;
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
        final int nX = this.imageWriter.getNx(), nY = this.imageWriter.getNy();
        pixelManager = new PixelManager(nY, nX, printInterval);
        if (threadsCount == 0) {
            for (int i = 0; i < nY; ++i) {
                for (int j = 0; j < nX; ++j) {
                    rayCastFunc.accept(nX, nY, j, i);
                }
            }
        }
        else {
            var threads = new LinkedList<Thread>(); // list of threads
            while (threadsCount-- > 0) // add appropriate number of threads
                threads.add(new Thread(() -> { // add a thread with its code
                    PixelManager.Pixel pixel; // current pixel(row,col)
                    // allocate pixel(row,col) in loop until there are no more pixels
                    while ((pixel = pixelManager.nextPixel()) != null)
                        // cast ray through pixel (and color it – inside rayCastFunc)
                        rayCastFunc.accept(nX, nY, pixel.col(), pixel.row());
                }));
            // start all the threads
            for (var thread : threads) thread.start();
            // wait until all the threads have finished
            try { for (var thread : threads) thread.join(); } catch (InterruptedException ignore) {}
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

    /** casts a ray through a pixel and paints it with its color
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     */
    private void castRay(int nX, int nY, int j, int i){
        imageWriter.writePixel(j,i,rayColorFunc.apply(constructRay(nX, nY, j, i)));
        pixelManager.pixelDone();
    }

    /** casts a beam of rays through a pixel and paints it with their average color
     * @param nX amount of pixels in a row (amount of columns in view plane)
     * @param nY amount of pixels in a column (amount of rows in view plane)
     * @param j column of pixel
     * @param i row of pixel
     */
    private void castBeam(int nX, int nY, int j, int i){
        imageWriter.writePixel(j,i,averageBeamColor(constructBeam(nX,nY,j,i), rayColorFunc));
        pixelManager.pixelDone();
    }

    /** calculates the average color of all rays in beam
     * @param beam beam of rays
     * @return  the average color of all rays in beam */
    private Color averageBeamColor(LinkedList<Ray> beam, Function<Ray,Color> func) {
        Color color = Color.BLACK;
        for (Ray ray: beam)
            color = color.add(func.apply(ray));
        return color.reduce(beam.size());
    }

    /** calculates color of a point in view plane with DOF improvement
     * @param ray ray from camera to point at view plane
     * @return color of point with DOF improvement*/
    private Color calcDOF(Ray ray) {
        Blackboard ta = new Blackboard(getPointHorizontalDistance(ray,this.vpDistance), this.vUp, this.vRight, apertureSize, amountRaysDOF);
        return averageBeamColor(Ray.generateBeam(getPointHorizontalDistance(ray,this.focusDistance),ta, true), ray1 -> rayTracer.traceRay(ray1));
    }

    /** calculates point on a certain horizontal distance from ray origin point (horizontal - on Vto axis)
     * @param ray given ray
     * @param distance given horizontal distance
     * @return the point on a certain horizontal distance from ray origin point (horizontal - on Vto axis)
     */
    private Point getPointHorizontalDistance(Ray ray,double distance) {
        return ray.getPoint((distance)/(ray.getDir().dotProduct(this.vTo)));
    }

    // todo: implement camera rotation (bonus)
}
