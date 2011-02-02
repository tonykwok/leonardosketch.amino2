package com.joshondesign.amino.draw;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rectangle extends Shape {
    private double x;
    private double y;
    private double w;
    private double h;

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public Path toPath() {
        Path pth = Path
                .moveTo(x, y)
                .lineTo(x, y + h)
                .lineTo(x + w, y + h)
                .lineTo(x + w, y)
                .closeTo().build();
        return pth;
    }

    @Override
    public Rect getBounds() {
        return new Rect((int)x,(int)y,(int)w, (int) h);
    }

    public static Rectangle build(double x, double y, double w, double h) {
        return new Rectangle(x,y,w,h);
    }
}
