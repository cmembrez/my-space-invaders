package project.utils;

import oop.lib.Degrees;

/**
 * @author ingold
 */
public class Point {

    public static final double DEFAULT_TOLERANCE = 1E-9;

    private static double tolerance = DEFAULT_TOLERANCE;

    public static void setTolerance(double epsilon) {
        tolerance = epsilon;
    }

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point pt) {
        this(pt.x, pt.y);
    }

    public Point copy() {
        return new Point(x, y);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public boolean equals(Point other) {
        return x == other.x && y == other.y;
    }

    public double distance(Point other) {
        return Math.sqrt((other.x - x) * (other.x - x) + (other.y - y) * (other.y - y));
    }

    public boolean approximates(Point other) {
        return distance(other) <= tolerance;
    }

    public void translate(Vector delta) {
        x = x + delta.dx();
        y = y + delta.dy();
    }

    public void rotate(double angle, Point center) {
        double dx = x - center.x;
        double dy = y - center.y;
        x = center.x + dx * Degrees.cos(angle) - dy * Degrees.sin(angle);
        y = center.y + dx * Degrees.sin(angle) + dy * Degrees.cos(angle);
    }

    public void scale(double factor, Point center) {
        x = center.x + factor * (x - center.x);
        y = center.y + factor * (y - center.y);
    }

    public double[] asArray() {
        return new double[] {x, y};
    }

    @Override
    public String toString() {
        return "oop_Point:[x=" + x + ",y=" + y +"]";
    }

}
