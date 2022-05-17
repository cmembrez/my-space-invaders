package project.utils;

import oop.lib.Degrees;

import java.util.Objects;

/**
 * @author ingold
 */
public class Vector {

    private final double dx;
    private final double dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Vector(Point pt) {
        this(pt.x(), pt.y());
    }

    public Vector(Point from, Point to) {
        this(to.x() - from.x(), to.y() - from.y());
    }

    public double dx() {
        return dx;
    }

    public double dy() {
        return dy;
    }

    public Vector add(Vector v) {
        return new Vector(dx + v.dx, dy + v.dy);
    }

    public Vector scale(double v) {
        return new Vector(v * dx, v * dy);
    }

    public Vector rotate(double angle) {
        double newDx = Degrees.cos(angle) * dx - Degrees.sin(angle) * dy;
        double newDy = Degrees.sin(angle) * dx + Degrees.cos(angle) * dy;
        return new Vector(newDx, newDy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.dx, dx) == 0 && Double.compare(vector.dy, dy) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dx, dy);
    }

    @Override
    public String toString() {
        return "oop_Vector:[dx=" + dx + ",dy=" + dy +"]";
    }

}
