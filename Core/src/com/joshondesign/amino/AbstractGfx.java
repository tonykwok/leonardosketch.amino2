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
        //public Effect effect;
        //public Transform transform;

        public Context() {
        }

        public Context(Context parent) {
            this.fill = parent.fill;
            //this.effect = parent.effect;
            //this.transform = parent.transform;
        }
    }

    public void setFill(Fill fill) {
        stack.peek().fill = fill;
    }

    public Fill getFill() {
        return stack.peek().fill;
    }

    /*public void setEffect(Effect effect) {
        stack.peek().effect = effect;
    } */

    public void translate(double x, double y) {
    }

    public void rotate(double centerX, double centerY, double angle) {
    }

    public void scale(double scaleX, double scaleY) {
    }

    /*public void draw(Shape shape) {
        draw(shape.toPath(), stack.peek().fill, getDefaultBuffer(), (Rect) null);
    }*/

    public void fill(Shape shape) {
        fill(shape.toPath(), stack.peek().fill, null, (Rect)null);
    }

    public void push() {
        stack.push(new Context(stack.peek()));
    }

    public void pop() {
        stack.pop();
    }


}