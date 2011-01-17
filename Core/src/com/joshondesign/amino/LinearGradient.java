package com.joshondesign.amino;


import java.util.ArrayList;
import java.util.List;

public class LinearGradient extends Fill{
    Point start, end;
    List<Stop> stops = new ArrayList<Stop>();

    static class Stop {
        double position;
        Color color;
        Stop(double position, Color color) {
            this.position = position;
            this.color = color;
        }
    }
    private LinearGradient() {
    }
    public static LinearGradientBuilder line(Point start, Point end) {
        return new LinearGradientBuilder(start,end);
    }

    public static class LinearGradientBuilder {
        private LinearGradient gradient;
        LinearGradientBuilder(Point start, Point end) {
            gradient = new LinearGradient();
            gradient.start = start;
            gradient.end = end;
        }
        public LinearGradientBuilder addStop(double position, Color color) {
            gradient.stops.add(new Stop(position,color));
            return this;
        }
        public LinearGradient build() {
            return gradient;
        }
    }
}

