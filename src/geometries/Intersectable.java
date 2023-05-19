package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

/**
 * class Intersectable is a class representing intersectable
 * geometry\ies of Euclidean geometry in Cartesian 3-Dimensional coordinate system.
 * @author Yoav Babayof and Avishai Shachor
 */
public abstract class Intersectable {
    /**
     * Returns a lists of intersection points between geometry\ies and ray
     * @param ray ray that intersects the geometry\ies
     * @return list of intersection points
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }

    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    /**
     * Returns a lists of intersection geoPoints (geometry and point) between geometry\ies and ray
     * @param ray ray that intersects the geometry\ies
     * @return list of intersection geoPoints (geometry and point)
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return this.findGeoIntersectionsHelper(ray);
    }

    /** a PDS representing a Geometry and a point on it */
    public static class GeoPoint {
        /** geometry */
        public Geometry geometry;
        /** point on geometry */
        public Point point;

        /** constructor for GeoPoint
         * @param geometry geometry
         * @param point point on geometry
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return this.geometry == geoPoint.geometry && this.point.equals(geoPoint.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(geometry, point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }
}
