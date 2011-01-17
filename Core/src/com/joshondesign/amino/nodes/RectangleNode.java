package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.Rect;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class RectangleNode extends ShapeNode {
    private double x;
    private double y;
    private double width;
    private double height;

    public RectangleNode setX(double x) {
        this.x = x;
        return this;
    }

    public RectangleNode setY(double y) {
        this.y = y;
        return this;
    }

    public RectangleNode setWidth(double width) {
        this.width = width;
        return this;
    }

    public RectangleNode setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public void draw(Gfx gfx) {
        gfx.setFill(getFill());
        Rect rect = Rect.build((int) x, (int) y, (int) width, (int) height);
        gfx.fillRect(rect,getFill(),null,null);
    }

}
