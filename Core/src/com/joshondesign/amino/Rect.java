package com.joshondesign.amino;

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
        return null;
    }

    @Override
    public Rect getBounds() {
        return this;
    }
}

