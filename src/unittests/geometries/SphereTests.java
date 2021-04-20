package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Sphere class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class SphereTests {
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        Sphere sphere = new Sphere(Point3D.ZERO, 5.0);
        assertEquals(new Vector(new Point3D(0, 0, 1)), sphere.getNormal(new Point3D(0, 0, 3)));
    }
}
