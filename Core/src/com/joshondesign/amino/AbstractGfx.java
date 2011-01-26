package com.joshondesign.amino;

import java.util.ArrayDeque;


public abstract class AbstractGfx implements Gfx {
    private ArrayDeque<Context> stack;


    public AbstractGfx() {
        initStructures();
    }


    /* full impl */

    private void initStructures() {
        stack = new ArrayDeque<Context>();
        stack.push(new Context());
    }


    private static class Context {
        public Fill fill;
        public Blend blend;
        public Effect effect;
        public Buffer buffer;
        public Shape clip;

        public Context() {
        }

        public Context(Context parent) {
            this.fill = parent.fill;
            this.blend = parent.blend;
            this.effect = parent.effect;
            this.buffer = parent.buffer;
            this.clip = parent.clip;
        }
    }

    public void setFill(Fill fill) {
        stack.peek().fill = fill;
    }
    public void setBlend(Blend blend) {
        stack.peek().blend = blend;
    }

    public Fill getFill() {
        return stack.peek().fill;
    }
    public Blend getBlend() {
        if(stack.peek().blend == null) {
            return Blend.Normal;
        }
        return stack.peek().blend;
    }

    public void setTargetBuffer(Buffer buffer) {
       stack.peek().buffer = buffer;
    }

    public void setEffect(Effect effect) {
        stack.peek().effect = effect;
    }

    public void setClip(Path path) {
        stack.peek().clip = path;
    }

    public void translate(double x, double y) {
    }

    public void rotate(double centerX, double centerY, double angle) {
    }

    public void scale(double scaleX, double scaleY) {
    }

    public void draw(Shape shape) {
        draw(shape.toPath(), stack.peek().fill, null, null, getBlend());
    }

    public void fill(Shape shape) {
        fill(shape.toPath(), stack.peek().fill, stack.peek().buffer, null, getBlend());
    }

    public void push() {
        stack.push(new Context(stack.peek()));
    }

    public void pop() {
        stack.pop();
    }


}
