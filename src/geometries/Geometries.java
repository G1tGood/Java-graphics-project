package geometries;

import primitives.Ray;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * class Geometries is a class representing a composition of geometries
 *
 * @author Yoav Babayof and Avishai Shachor
 */
public class Geometries extends Intersectable {
    /**
     * list of Intersectables
     */
    private List<Intersectable> geometries;

    /**
     * Constructor that initializes Geometries based on a given array of geometries
     *
     * @param geometries array of geometries
     */
    public Geometries(Intersectable... geometries) {
        this.geometries = new LinkedList<Intersectable>(List.of(geometries));
    }

    /**
     * Constructor that initializes Geometries with an empty list
     */
    public Geometries() {
        this.geometries = new LinkedList<Intersectable>();
    }

    /**
     * adds geometries to the list of the geometries in Geometries
     *
     * @param geometries array of geometries
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(Arrays.asList(geometries));
    }

    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> points = null;
        for (Intersectable geometry : this.geometries) {
            List<GeoPoint> intersections = geometry.findGeoIntersections(ray, maxDistance);
            if (intersections != null) {
                if (points == null) {
                    points = new LinkedList<GeoPoint>();
                }
                points.addAll(intersections);
            }
        }
        return points;
    }
}
