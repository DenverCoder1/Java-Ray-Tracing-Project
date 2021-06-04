package geometries;

import java.util.List;
import java.util.stream.Collectors;

import primitives.Point3D;
import primitives.Ray;

/**
 * Interface for intersectable geometries
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public interface Intersectable {

    /**
     * Static class for geometry/point for shape intersections
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point3D point;

        /**
         * GeoPoint constructor
         * 
         * @param geometery
         * @param point
         */
        public GeoPoint(Geometry g, Point3D p) {
            geometry = g;
            point = p;
        }

        @Override
        public boolean equals(Object o) {
            // object is self
            if (o == this) {
                return true;
            }

            // check that object is an employee
            if (!(o instanceof GeoPoint)) {
                return false;
            }

            // cast object to GeoPoint
            GeoPoint g = (GeoPoint) o;

            // Compare the data members and return accordingly
            return g.geometry.equals(geometry) && g.point.equals(point);
        }
    }

    default List<Point3D> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).collect(Collectors.toList());
    }

    /**
     * Method to find intersection points of a ray to the current geometric shape
     * 
     * @param ray The ray to find intersections with
     * @return List of intersection points or null if there are none
     */
    public List<GeoPoint> findGeoIntersections(Ray ray);
}
