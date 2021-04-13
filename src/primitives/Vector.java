package primitives;

import java.util.Objects;

/**
 * Class Vector is the basic class representing a vector of Euclidean geometry
 * in Cartesian 3-Dimensional coordinate system
 * 
 * @author Jonah Lawrence
 * @author Elad Harizy
 */
public class Vector {
  private Point3D head;

  /**
   * Constructor that takes 3 doubles
   * 
   * @param x The x coordinate of the head
   * @param y The y coordinate of the head
   * @param z The z coordinate of the head
   * 
   * @throws IllegalArgumentException if head point is ZERO
   */
  public Vector(double x, double y, double z) {
    // call the constructor that takes a Point3D
    this(new Point3D(x, y, z));
  }

  /**
   * Constructor that takes a Point3D
   * 
   * @param head Point3D representing the head of the vector
   * 
   * @throws IllegalArgumentException if head point is ZERO
   */
  public Vector(Point3D head) {
    if (head.equals(Point3D.ZERO)) {
      throw new IllegalArgumentException("Head of vector cannot be zero");
    }
    this.head = head;
  }

  /**
   * Getter for the head point
   * 
   * @return the head of the vector
   */
  public Point3D getHead() {
    return this.head;
  }

  /**
   * Subtract a vector from the current vector
   * 
   * @param other the other vector
   * @return vector formed by subtracting the other vector from the current vector
   */
  public Vector subtract(Vector other) {
    return this.head.subtract(other.head);
  }

  /**
   * Subtract a vector from the current vector
   * 
   * @param other the other vector
   * @return vector formed by subtracting the other vector from the current vector
   */
  public Vector add(Vector other) {
    return new Vector(this.head.add(other));
  }

  /**
   * Scale a vector by a number
   * 
   * @param scalar the number to multiply by
   * @return the new vector scaled by the scalar
   */
  public Vector scale(double scalar) {
    return new Vector(this.head.x.coord * scalar, this.head.y.coord * scalar, this.head.z.coord * scalar);
  }

  /**
   * Compute the dot product of two vectors
   * 
   * @param other the other vector
   * @return the dot product of the two vectors
   */
  public double dotProduct(Vector other) {
    return this.head.x.coord * other.head.x.coord + this.head.y.coord * other.head.y.coord
        + this.head.z.coord * other.head.z.coord;
  }

  /**
   * Compute the cross product of two vectors
   * 
   * @param other the other vector
   * @return the cross product of the two vectors
   */
  public Vector crossProduct(Vector other) {
    return new Vector(this.head.y.coord * other.head.z.coord - other.head.y.coord * this.head.z.coord,
        this.head.z.coord * other.head.x.coord - other.head.z.coord * this.head.x.coord,
        this.head.x.coord * other.head.y.coord - other.head.x.coord * this.head.y.coord);
  }

  /**
   * Calculate the length squared of the vector
   * 
   * @return the length of the vector squared
   */
  public double lengthSquared() {
    return this.head.distanceSquared(Point3D.ZERO);
  }

  /**
   * Calculate the length of the vector
   * 
   * @return the length of the vector
   */
  public double length() {
    return Math.sqrt(this.lengthSquared());
  }

  /**
   * Optimized method to update the vector to its normalized form
   * 
   * @return the modified vector
   */
  public Vector normalize() {
    double length = this.length();
    this.head = new Point3D(this.head.x.coord / length, this.head.y.coord / length, this.head.z.coord / length);
    return this;
  }

  /**
   * Calculate the normalized version of the original vector
   * 
   * @return the normalized version of the original vector
   */
  public Vector normalized() {
    return new Vector(this.head).normalize();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Vector)) {
      return false;
    }
    Vector vector = (Vector) o;
    return Objects.equals(head, vector.head);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(head);
  }

  @Override
  public String toString() {
    return String.format("(%s, %s, %s)", this.head.x.toString(), this.head.y.toString(), this.head.z.toString());
  }

}
