package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * class Geometries is a class representing a composition of geometries
 * @author Yoav Babayof and Avishai Shachor
 */
public class Geometries implements Intersectable{
    /** list of Intersectables */
    private final List<Intersectable> geometries;

    /** Constructor that initializes Geometries based on a given array of geometries
     * @param geometries array of geometries
     */
    public Geometries(Intersectable... geometries) {
        this.geometries = new LinkedList<Intersectable>(List.of(geometries));
    }

    /** Constructor that initializes Geometries with an empty list
     */
    public Geometries() {
        this.geometries = new LinkedList<Intersectable>();
    }

    /** adds geometries to the list of the geometries in Geometries
     * @param geometries array of geometries
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> points = null;
        for (Intersectable geometry : this.geometries) {
            if (points == null && geometry.findIntersections(ray) != null) points = new LinkedList<>(geometry.findIntersections(ray));
            else if (points != null && geometry.findIntersections(ray) != null) points.addAll(new LinkedList<Point>(geometry.findIntersections(ray)));
        }
        return points;
    }
}
