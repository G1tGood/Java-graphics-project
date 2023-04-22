package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

/**
 * interface Intersectable is an interface representing intersectable
 * geometry\ies of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public interface Intersectable {
    /**
     * Returns a lists of intersection points between geometry\ies and ray
     * @param ray ray that intersects the geometry\ies
     * @return list of intersection points
     */
    List<Point> findIntersections(Ray ray);
}
