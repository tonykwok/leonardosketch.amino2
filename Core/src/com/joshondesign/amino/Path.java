package com.joshondesign.amino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.awt.Rectangle;

public class Path extends Shape implements Iterable<Path.Segment> {
    private List<Segment> segments = new ArrayList<Segment>();
    int geometryType;
    private List<double[]> points;
    double[][] geometry = null;
    private boolean closed = false;

    private Path() {      }

    public Iterable<? extends Segment> reverse() {
        List<Segment> copy = new ArrayList<Segment>();
        copy.addAll(segments);
        Collections.reverse(copy);
        return copy;
    }

    @Override
    public Path toPath() {
        return this;
    }

    @Override
    public Rect getBounds() {
        java.awt.geom.Path2D p = new java.awt.geom.Path2D.Double();
        for(Path.Segment s : this) {
            switch(s.type) {
                case MoveTo:
                    p.moveTo(s.x,s.y);
                    break;
                case LineTo:
                    p.lineTo(s.x,s.y);
                    break;
                case CloseTo:
                    p.closePath();
                    break;
                case CurveTo:
                    p.curveTo(s.cx1, s.cy1, s.cx2, s.cy2, s.x2, s.y2);
                    break;
            }
        }
        Rectangle b = p.getBounds();
        return new Rect(b.x,b.y,b.width,b.height);
    }

    void addPoint(double[] pointer) {
        if(points == null) {
            points = new ArrayList<double[]>();
        }
        points.add(pointer);
    }

    void endPoints() {
        geometry = new double[points.size()][];
        for(int i=0; i<points.size(); i++) {
            geometry[i] = points.get(i);
        }
        points.clear();
        points = null;
    }

    public boolean isClosed() {
        return closed;
    }

    public enum Type { MoveTo, LineTo, CurveTo, CloseTo }

    public Iterator<Segment> iterator() {
        return segments.iterator();
    }

    public static class Segment {
        Type type;
        double x, y, cx1, cy1, cx2, cy2, x2, y2;

        public Segment(Type type, double x, double y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }

        public Segment(Type type, double cx1, double cy1, double cx2, double cy2,double x2, double y2 ) {
            this.type = type;
            this.cx1 = cx1;
            this.cy1 = cy1;
            this.cx2 = cx2;
            this.cy2 = cy2;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    public static PathBuilder moveTo(double x, double y) {
        return new PathBuilder(x,y);
    }

    public static class PathBuilder {

        private Path path;


        private PathBuilder(double x, double y) {
            path = new Path();
            path.segments.add(new Segment(Type.MoveTo,x,y));
        }
        public PathBuilder lineTo(double x, double y) {
            path.segments.add(new Segment(Type.LineTo,x,y));
            return this;
        }

        public PathBuilder curveTo(double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
            path.segments.add(new Segment(Type.CurveTo,cx1,cy1,cx2,cy2,x2,y2));
            return this;
        };

        public PathBuilder closeTo() {
            path.segments.add(new Segment(Type.CloseTo,0,0));
            path.closed = true;
            return this;
        }
        public Path build() {
            return path;
        }
    }
}


