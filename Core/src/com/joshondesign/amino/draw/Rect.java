package com.joshondesign.amino.draw;

/**
   Rect represents an immutable rectangle on the integer grid. It is mainly used
 for fast path drawing and clipping. If you want a general shape use the @Rectangle class instead.
 */
public class Rect extends Shape {
    int x;
    int y;
    int w;
    int h;

    Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public static Rect build(int x, int y, int width, int height) {
        return new Rect(x,y,width,height);
    }

    @Override
    public Path toPath() {
        Path pth = Path
                .moveTo(x,y)
                .lineTo(x,y+h)
                .lineTo(x+w,y+h)
                .lineTo(x+w,y)
                .closeTo().build();
        return pth;
    }

    @Override
    public Rect getBounds() {
        return this;
    }

    public boolean contains(Point point) {
        if(point.getX() < x) return false;
        if(point.getX() > x+w) return false;
        if(point.getY() < y) return false;
        if(point.getY() > y+h) return false;
        return true;
    }
}

