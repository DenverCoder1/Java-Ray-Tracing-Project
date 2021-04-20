package unittests.geometries;

import static org.junit.Assert.*;
import org.junit.Test;

import geometries.*;
import primitives.*;

/**
 * Unit tests for geometries.Cylinder class
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 *
 */
public class CylinderTests {
    /**
     * Test method for {@link geometries.Cylinder#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        Ray ray = new Ray(Point3D.ZERO, new Vector(0, 0, 1));
        Tube cylinder = new Cylinder(ray, 7.0, 7.0);
        // ============ Equivalence Partitions Tests =============
        // side of cylinder
        assertEquals("normal failed on side", new Vector(0, 0, 1), cylinder.getNormal(new Point3D(7, 0, 0)));
        // bottom of the cylinder
        assertEquals("normal failed at bottom", new Vector(0, 0, 1), cylinder.getNormal(new Point3D(3, 0, 0)));
        // top of the cylinder
        assertEquals("normal failed at top", new Vector(1, 0, 0), cylinder.getNormal(new Point3D(10, 0, 14)));
    }
}
