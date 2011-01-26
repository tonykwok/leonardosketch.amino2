package com.joshondesign.amino;

/**
 *
 * Points are immutable classes which represent a double precision
 * point.  All operations on the point, such as calling add(point) will
 * return a new instance of Point.
 */
public class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return this.x; }
    public double getY() { return this.y; }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /** returns a *new* Point object which contains the
     * current point with the x/y added
     * @param translateX
     * @param translateY
     * @return
     */
    public Point add(double translateX, double translateY) {
        return new Point(x+translateX,y+translateY);
    }

    public Point subtract(double translateX, double translateY) {
        return new Point(x-translateX,y-translateY);
    }
}


