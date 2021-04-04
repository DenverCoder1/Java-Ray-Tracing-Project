package primitives;

import static primitives.Util.*;

/**
 * Class Coordinate is the basic class representing a coordinate for Cartesian
 * coordinate system. The class is based on Util controlling the accuracy.
 * 
 * @author Dan Zilberstein
 * @version 5780B updated according to new requirements
 */
public final class Coordinate {
    /**
     * Coordinate value, intentionally "package-friendly" due to performance
     * constraints
     */
    final double coord;

    /**
     * Coordinate constructor receiving a coordinate value
     * 
     * @param coord coordinate value
     */
    public Coordinate(double coord) {
        // if it too close to zero make it zero
        this.coord = alignZero(coord);
    }

    /*************** Admin *****************/
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Coordinate))
            return false;
        Coordinate other = (Coordinate) obj;
        return isZero(coord - other.coord);
    }

    @Override
    public String toString() {
        return "" + coord;
    }
}
