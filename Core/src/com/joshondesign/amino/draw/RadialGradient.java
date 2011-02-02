package com.joshondesign.amino.draw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 12:52 AM
 * To change this template use File | Settings | File Templates.
 */

public class RadialGradient extends Fill {
    Point start;
    double radius;
    List<Stop> stops = new ArrayList<Stop>();
    com.sun.opengl.util.texture.Texture texture;

    private RadialGradient() {
    }


    static class Stop {
        double position;
        Color color;
        Stop(double position, Color color) {
            this.position = position;
            this.color = color;
        }
    }

    public static class RadialGradientBuilder {
        private RadialGradient gradient;

        public RadialGradientBuilder() {
            gradient = new RadialGradient();
        }

        public RadialGradientBuilder center(Point start) {
            gradient.start = start;
            return this;
        }

        public RadialGradientBuilder radius(double radius) {
            gradient.radius = radius;
            return this;
        }
        public RadialGradientBuilder addStop(double position, Color color) {
            gradient.stops.add(new Stop(position,color));
            return this;
        }
        public RadialGradient build() {
            return gradient;
        }
    }
}
