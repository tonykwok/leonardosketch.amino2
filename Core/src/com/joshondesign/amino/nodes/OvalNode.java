package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Ellipse;
import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.Point;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class OvalNode extends ShapeNode {
    private double x;
    private double y;
    private double width;
    private double height;

    public OvalNode setX(double x) {
        this.x = x;
        return this;
    }

    public OvalNode setY(double y) {
        this.y = y;
        return this;
    }

    public OvalNode setWidth(double width) {
        this.width = width;
        return this;
    }

    public OvalNode setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public void draw(Gfx gfx) {
        gfx.setFill(getFill());
        gfx.fill(Ellipse.build(x,y,width,height));
    }

    @Override
    public boolean contains(Point point) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
