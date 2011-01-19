package com.joshondesign.amino;

/**
 * FullGFX adds additional functionality to the minimum required to draw.
 * App developers will use this API.  GFX implementers can directly implement
 * this API to get greater speed, for fallback to the default implementations
 * in the DelegatingGFX and only implement the minimum api specified in @see MinimalGfx
 */
public interface FullGfx {

    //convenience methods
    public void draw(Shape shape);
    public void fill(Shape shape);

    //state management
    public void push();
    public void pop();
    public void setFill(Fill fill);
    public void setBlend(Blend blend);
    //public void setEffect(Effect effect);

    //additive affine transforms
    public void translate(double x, double y);
    public void rotate(double centerX, double centerY, double angle);
    public void scale(double scaleX, double scaleY);
}
