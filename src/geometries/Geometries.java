package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * class Geometries is a class representing a composition of geometries
 * @author Yoav Babayof and Avishai Shachor
 */
public class Geometries implements Intersectable{
    /** list of Intersectables */
    private List<Intersectable> geometries;

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
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> points = null;
        for (Intersectable geometry : this.geometries) {
            if (points == null && geometry.findIntersections(ray) != null) points = new LinkedList<GeoPoint>(geometry.findGeoIntersections(ray));
            else if (points != null && geometry.findIntersections(ray) != null) points.addAll(geometry.findGeoIntersections(ray));
        }
        if (points == null) return null;
        return Collections.unmodifiableList(points);
    }
}
