package com.joshondesign.amino.draw.effects;

import com.joshondesign.amino.draw.Point;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 10:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class RippleEffect implements Effect {
    private Point center;

    RippleEffect() {
        center = new Point(0,0);
    }

    public static RippleEffect build() {
        return new RippleEffect();
    }

    public void setCenter(Point point) {
        this.center = point;
    }

    public Point getCenter() {
        return center;
    }
}
