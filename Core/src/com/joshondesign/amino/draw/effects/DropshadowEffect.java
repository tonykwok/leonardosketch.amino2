package com.joshondesign.amino.draw.effects;

import com.joshondesign.amino.draw.Color;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/10/11
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DropshadowEffect implements Effect {
    public double x;
    public double y;
    private double radius;
    private Color color;

    DropshadowEffect(double xOffset, double yOffset, double radius, Color color) {
        this.x = xOffset;
        this.y = yOffset;
        this.radius = radius;
        this.color = color;
    }

    public static DropshadowEffect build(double xOffset, double yOffset, double radius, Color color) {
        return new DropshadowEffect(xOffset,yOffset,radius,color);
    }

    public Color getColor() {
        return color;
    }
}
