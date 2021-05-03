package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * class Camera has place of the camera, were all the rays starts
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Camera {

    private Point3D origin;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private double width;
    private double height;
    private double distance;

    /**
     * Constructor for Camera
     * 
     * @param newOrigin
     * @param newVTo
     * @param newVUp
     * 
     * @throws IllegalArumentException
     */
    public Camera(Point3D newOrigin, Vector newVTo, Vector newVUp) {
        if (!Util.isZero(newVUp.dotProduct(newVTo))) {
            throw new IllegalArgumentException("The vectors are not orthogonal.");
        }
        origin = newOrigin;
        vUp = newVUp.normalized();
        vTo = newVTo.normalized();
        vRight = newVTo.crossProduct(newVUp).normalize();
    }

    /**
     * getter for origin
     * 
     * @return origin
     */
    public Point3D getOrigin() {
        return origin;
    }

    /**
     * getter for vUp
     * 
     * @return vUp
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * getter for vTo
     * 
     * @return vTo
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * getter for vRight
     * 
     * @return vRight
     */
    public Vector getVRight() {
        return vRight;
    }

    /**
     * Set view plane size
     *
     * @param width
     * @param height
     * @return Camera instance
     */
    public Camera setViewPlaneSize(double newWidth, double newHeight) {
        height = newHeight;
        width = newWidth;
        return this;
    }

    /**
     * Set distance
     * 
     * @param distance
     * @return
     */
    public Camera setDistance(double newDistance) {
        distance = newDistance;
        return this;
    }

    /**
     * Gets parameters that define the view plane matrix, and the index of the pixel
     * on it, and returns a ray from the place point of the camera through the pixel
     * 
     * @param nX Number of pixels in x axis
     * @param nY Number of pixels in y axis
     * @param j  Columns of pixels in the matrix
     * @param i  Rows of the pixels in the matrix
     * @return Ray from the camera through the pixel i,j
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        Point3D pc = origin.add(vTo.scale(distance));
        double rY = height / nY;
        double rX = width / nX;
        double xj = (j - ((nX - 1) / 2.0)) * rX;
        double yi = -(i - ((nY - 1) / 2.0)) * rY;

        Point3D pij = pc;
        if (!isZero(xj))
            pij = pij.add(vRight.scale(xj));
        if (!isZero(yi))
            pij = pij.add(vUp.scale(yi));

        Vector vij = pij.subtract(origin).normalize();
        return new Ray(origin, vij);
    }

}