package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.Random;
import java.util.LinkedList;

/** a helper class representing a square target area and points on it
 * @author Yoav Babayoff
 * @author  Avishai Shachor
 */
public class Blackboard {
    /** points on target area */
    public LinkedList<Point> points;
    /** number of points */
    public int amountOfPoints;
    /** target area side size */
    private double sideSize;
    /** target area vector up */
    private Vector vY;
    /** target area vector right */
    private Vector vX;
    /** target area middle point location */
    private Point location;

    /** ctor for Blackboard
     * @param point point to set target area location
     * @param vY y-axis vector
     * @param vX x-axis vector
     * @param sideSize size of side of target area
     * @param amountOfPoints amount of points to generate
     */
    public Blackboard(Point point, Vector vY, Vector vX, double sideSize, int amountOfPoints) {
        this.vX = vX;
        this.vY = vY;
        this.sideSize = sideSize;
        this.location = point;
        generatePoints(amountOfPoints);
    }

    /** generates points on target area
     * @param amountOfPoints amount of points to generate
     * @throws IllegalArgumentException if amountOfPoints < 1
     */
    public void generatePoints(int amountOfPoints) {
        if (amountOfPoints < 1) throw new IllegalArgumentException("amount of points to generate must be bigger than 0");
        this.points = new LinkedList<Point>();
        points.add(location);
        int n = (int) Math.floor(Math.sqrt(amountOfPoints-1));
        if (n == 0) return;
        double interval = this.sideSize/n;
        int leftovers =  amountOfPoints - n*n - 1;
        Point topLeftPoint;
        if (n == 1) {
            topLeftPoint = randomMovePoint(this.location,interval);
        }
        else {
            topLeftPoint = this.location.add(this.vX.scale((float) (1 - n) / 2 * interval)
                    .add(this.vY.scale((float) (n - 1) / 2 * interval)));
        }
        this.points.add(randomMovePoint(topLeftPoint,interval));
        for (int i = 1; i < n; ++i) {
            this.points.add(randomMovePoint(topLeftPoint.add(this.vY.scale(-i*interval)), interval));
        }
        for (int i = 1; i < n; ++i) {
            this.points.add(randomMovePoint(topLeftPoint.add(this.vX.scale(i*interval)), interval));
        }
        for (int i = 0; i < n*n-2*n+1; ++i){
            this.points.add(randomMovePoint(topLeftPoint.add(this.vY.scale((-((int)(i/(n-1))+1))*interval).add(this.vX.scale((i%(n-1)+1)*interval))), interval));
        }
        for (int i = 0; i < leftovers; ++i) {
            this.points.add(randomMovePoint(this.location,sideSize/2));
        }
    }

    /** moves point randomly on grid
     * @param point given point
     * @param interval interval between points if not random
     * @return moved point
     */
    private Point randomMovePoint(Point point, double interval){
        do {
            Random random = new Random();
            double n1 = random.nextDouble() * interval - interval / 2;
            double n2 = random.nextDouble() * interval - interval / 2;
            if (n1 != 0) point = point.add(this.vX.scale(n1));
            if (n2 != 0) point = point.add(this.vY.scale(n2));
        } while (point.equals(this.location));
        return point;
    }

    /** getter for points in target area
     * @return points in target area
     */
    public LinkedList<Point> getPoints() {
        return points;
    }
}
