package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.Random;
import java.util.LinkedList;

import static primitives.Util.isZero;

/**
 * a helper class representing a square target area and points on it
 *
 * @author Yoav Babayoff
 * @author Avishai Shachor
 */
public class Blackboard {
    /**
     * number of points in a row
     */
    private int n;
    /**
     * target area side size
     */
    private double sideSize;
    /**
     * target area vector up
     */
    private Vector vY;
    /**
     * target area vector right
     */
    private Vector vX;
    /**
     * target area middle point location
     */
    private Point location;
    /**
     * interval between points
     */
    private double interval;

    /**
     * ctor for Blackboard
     *
     * @param point          point to set target area location
     * @param vY             y-axis vector
     * @param vX             x-axis vector
     * @param sideSize       size of side of target area
     * @param amountOfPoints amount of points to generate
     * @throws IllegalArgumentException if amountOfPoints <=0
     */
    public Blackboard(Point point, Vector vY, Vector vX, double sideSize, int amountOfPoints) {
        if (amountOfPoints <= 0)
            throw new IllegalArgumentException("amount of points to generate must be bigger than 0");
        this.vX = vX;
        this.vY = vY;
        this.sideSize = sideSize;
        this.location = point;
        this.n = (int) Math.floor(Math.sqrt(amountOfPoints));
        this.interval = this.sideSize / n;
    }

    /**
     * generates a point on location (j,i) on target area
     * @param j location on x-axis
     * @param i location on y-axis
     * @return point in (j,i)
     */
    public Point generatePoint(int j, int i) {
        Point pIJ = this.location;
        double yI = ((double) (n - 1) / 2 - i) * interval;
        double xJ = (j - ((double) (n - 1) / 2)) * interval;
        if (!isZero(xJ)) pIJ = pIJ.add(this.vX.scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(this.vY.scale(yI));
        return randomMovePoint(pIJ);
    }

    /**
     * generates points on target area
     * @return points on target area
     */
    public LinkedList<Point> generatePoints() {
        LinkedList<Point> points = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                points.add(randomMovePoint(this.generatePoint(j, i)));
            }
        }
        if (n%2 == 0) points.add(location);
        return points;
    }


    /**
     * moves point randomly on grid
     * @param point given point
     * @return moved point
     */
    private Point randomMovePoint(Point point) {
        Random random = new Random();
        double n1 = random.nextDouble() * interval - interval / 2;
        double n2 = random.nextDouble() * interval - interval / 2;
        if (n1 != 0) point = point.add(this.vX.scale(n1));
        if (n2 != 0) point = point.add(this.vY.scale(n2));
        return point;
    }

    /** getter for amount of points in row
     * @return amount of points in row
     */
    public int getN() {
        return n;
    }
}
