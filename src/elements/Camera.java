package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;

import java.util.ArrayList;
import java.util.List;

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
        if (!isZero(newVUp.dotProduct(newVTo))) {
            throw new IllegalArgumentException("The vectors are not orthogonal.");
        }
        origin = newOrigin;
        vUp = newVUp.normalized();
        vTo = newVTo.normalized();
        vRight = newVTo.crossProduct(newVUp).normalize();
    }

    /**
     * getter for height
     * 
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * getter for width
     * 
     * @return width
     */
    public double getWidth() {
        return width;
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

    /**
     * Gets parameters that define the view plane matrix, and the index of the pixel
     * on it, and returns a ray from the place point of the camera through the pixel
     * 
     * @param point point to construct ray through
     * @return Ray from the camera through the point
     */
    public Ray constructRayThroughPoint(Point3D point) {
        return new Ray(origin, point.subtract(origin));
    }

    /**
     * construct an equidistant grid of rays through a pixel
     * 
     * @param center      ray for original pixel location
     * @param gridSize    number of rows and columns for dividing pixel
     * @param pixelWidth  width of pixel
     * @param pixelHeight height of pixel
     * @return rays
     */
    public List<Ray> getSupersamplingRays(Ray center, int gridSize, double pixelWidth, double pixelHeight) {
        // get spacing amount based on grid size
        double spacingVertical = pixelHeight / (gridSize + 1);
        double spacingHorizontal = pixelWidth / (gridSize + 1);
        // position of top left ray intersection with view plane
        Point3D topLeft = center.getPoint(distance).add(vRight.scale(-pixelWidth / 2 - spacingHorizontal))
                .add(vUp.scale(pixelHeight / 2 - spacingVertical));
        return constructGridOfRays(topLeft, gridSize, spacingVertical, spacingHorizontal);
    }

    /**
     * construct rays through each of four quadrants of a cell
     * 
     * @param center         center of cell ray
     * @param halfCellWidth  half width of cell
     * @param halfCellHeight half height of cell
     * @return rays
     */
    public List<Ray> getAdaptiveSupersamplingRays(Ray center, double halfCellWidth, double halfCellHeight) {
        // position of top left ray center ray
        Point3D topLeft = center.getPoint(distance).add(vRight.scale(-halfCellWidth / 2))
                .add(vUp.scale(halfCellHeight / 2));
        return constructGridOfRays(topLeft, 2, halfCellHeight, halfCellWidth);
    }

    /**
     * construct a grid of rays from a given point
     * 
     * @param topLeft           position of top left ray intersection
     * @param gridSize          number of rows and columns for dividing pixel
     * @param spacingVertical   distance from one pixel to another vertically
     * @param spacingHorizontal distance from one pixel to another horizontally
     * @return rays
     */
    public List<Ray> constructGridOfRays(Point3D topLeft, int gridSize, double spacingVertical,
            double spacingHorizontal) {
        List<Ray> rays = new ArrayList<>();
        // create grid of rays for supersampling
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Point3D newPoint = topLeft;
                if (row > 0) {
                    newPoint = newPoint.add(vUp.scale(-row * spacingVertical));
                }
                if (col > 0) {
                    newPoint = newPoint.add(vRight.scale(col * spacingHorizontal));
                }
                rays.add(constructRayThroughPoint(newPoint));
            }
        }
        return rays;
    }

}