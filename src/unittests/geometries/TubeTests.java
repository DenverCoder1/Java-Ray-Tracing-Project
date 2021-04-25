package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Tube class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class TubeTests {
    /**
     * Test method for {@link geometries.Tube#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        Ray ray = new Ray(Point3D.ZERO, new Vector(0, 0, 1));
        Tube tube = new Tube(ray, 7.0);
        // ============ Equivalence Partitions Tests ==============
        // inside of tube
        assertEquals("normal failed inside tube", new Vector(3 / Math.sqrt(10.0), 1 / Math.sqrt(10.0), 0),
                tube.getNormal(new Point3D(3, 1, 2)));
        // ============ Boundary Values Tests =============
        // side of tube
        assertEquals("normal failed on side of tube", new Vector(1, 0, 0), tube.getNormal(new Point3D(7, 0, 0)));
    }

    /**
     * Test method for {@link geometries.Tube#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        // TODO: Implement
    }
}
