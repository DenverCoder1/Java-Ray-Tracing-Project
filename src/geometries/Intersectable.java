package geometries;

import java.util.List;

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
     * Method to find intersection points of a ray to the current geometric shape
     * 
     * @param ray The ray to find intersections with
     * @return List of intersection points or null if there are none
     */
    public List<Point3D> findIntersections(Ray ray);
}
