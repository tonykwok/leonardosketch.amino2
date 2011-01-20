package com.joshondesign.amino;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class Ellipse extends Shape {
    double x;
    double y;
    double w;
    double h;
    Ellipse() {

    }
    private Ellipse(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public static Ellipse build(double x, double y, double w, double h) {
        return new Ellipse(x,y,w,h);
    }

    @Override
    public Path toPath() {
        Path pth = Path
                .moveTo(x,y+h/2)
                .curveTo(x,y+h/4,
                        x+w/4,y,
                        x+w/2,y)
                .curveTo(x+w*3/4,y,
                        x+w,y+h*1/4,
                        x+w,y+h/2)
                .curveTo(x+w,y+h*3/4,
                        x+w*3/4,y+h,
                        x+w/2,y+h)
                .curveTo(x+w*1/4,y+h,
                        x,y+h*3/4,
                        x,y+h/2
                )
                .closeTo().build();
        return pth;
    }

    @Override
    public Rect getBounds() {
        return new Rect((int)x,(int)y,(int) w,(int) h);
    }

}
