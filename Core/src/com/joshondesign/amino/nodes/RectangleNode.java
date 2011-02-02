package com.joshondesign.amino.nodes;

import com.joshondesign.amino.draw.*;

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
    private double corner;
    private double opacity = 1.0;

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
        if(corner > 0) {
            Path path = Path.moveTo(x+corner,y)
                    .lineTo(x + width - corner, y)
                    .curveTo(x + width - corner / 2, y, x + width, y + corner / 2, x + width, y + corner)
                    .lineTo(x + width, y + height - corner)
                    .curveTo(x + width, y + height - corner / 2, x + width - corner / 2, y + height, x + width - corner, y + height)
                    .lineTo(x + corner, y + height)
                    .curveTo(x + corner / 2, y + height, x, y + height - corner / 2, x, y + height - corner)
                    .lineTo(x, y + corner)
                    .curveTo(x, y + corner / 2, x + corner / 2, y, x + corner, y)
                    .closeTo()
                    .build();
            gfx.fill(path, getFill(), null, null, Blend.Normal);
            if(getStroke() != null) {
                gfx.draw(path, getStroke(), null, null, Blend.Normal);
            }
        } else {
            Rectangle rect = Rectangle.build(x, y, width, height);
            if(getOpacity() != 1.0) {
                Color color = (Color) getFill();
                color = color.deriveAlpha(getOpacity());
                gfx.setFill(color);
                gfx.fill(rect.toPath());
            } else {
                gfx.setFill(getFill());
                gfx.fill(rect.toPath());
            }
            if(getStroke() != null) {
                gfx.draw(rect.toPath(), getStroke(), null, null, Blend.Normal);
            }
        }
    }

    @Override
    public boolean contains(Point point) {
        if(point.getX() < getX()) return false;
        if(point.getX() > getX()+getWidth()) return false;
        if(point.getY() < getY()) return false;
        if(point.getY() > getY()+getHeight()) return false;
        return true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getCorner() {
        return corner;
    }

    public RectangleNode setCorner(double corner) {
        this.corner = corner;
        return this;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public double getOpacity() {
        return opacity;
    }
}
