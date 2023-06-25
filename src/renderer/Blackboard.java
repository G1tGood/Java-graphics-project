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
     * ctor for Blackboard
     *
     * @param point          point to set target area location
     * @param vY             y-axis vector
     * @param vX             x-axis vector
     * @param sideSize       size of side of target area
     * @param amountOfPoints amount of points to generate
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

    /** generates points on target area
     * @param amountOfPoints amount of points to generate
     * @throws IllegalArgumentException if amountOfPoints < 1
     */
    public LinkedList<Point> generatePoints() {
        LinkedList<Point> points = new LinkedList<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                points.add(randomMovePoint(this.generatePoint(j, i)));
            }
        }
        return points;
    }


    /**
     * moves point randomly on grid
     * @param point given point
     * @param interval interval between points if not random
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
