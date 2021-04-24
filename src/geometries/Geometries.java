package geometries;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;

/**
 * Composite class of geometries to have a list
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Geometries implements Intersectable {

    /**
     * List of geometries
     */
    List<Intersectable> geometryList;

    /**
     * Default constructor
     */
    public Geometries() {
        geometryList = null;
    }

    /**
     * Constructor that takes a list of geometries
     * 
     * @param geometries list of geometries
     */
    public Geometries(Intersectable... geometries) {
        geometryList = List.of(geometries);
    }

    /**
     * Add new geometries to the list
     * 
     * @param geometries list of geometries to add
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(geometryList, geometries);
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        // return null if no geometries
        if (geometryList == null) {
            return null;
        }
        // initialize intersection list to null
        List<Point3D> intersections = null;
        Iterator<Intersectable> iterator = geometryList.iterator();
        while (iterator.hasNext()) {
            List<Point3D> newPoints = iterator.next().findIntersections(ray);
            // make sure there are points
            if (newPoints != null) {
                // add points if list exists already
                if (intersections != null) {
                    intersections.addAll(newPoints);
                    continue;
                }
                // otherwise, initialize with a list when first intersection found
                intersections = newPoints;
            }
        }
        return intersections;
    }
}