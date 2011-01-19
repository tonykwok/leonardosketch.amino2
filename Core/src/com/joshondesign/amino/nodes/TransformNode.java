package com.joshondesign.amino.nodes;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransformNode extends Node {

    private Node node;
    private double translateX;
    private double translateY;

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    private double rotate;

    public double getTranslateY() {
        return translateY;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    public TransformNode(Node node) {
        this.node = node;
    }

    public double getTranslateX() {
        return translateX;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public Node getChild() {
        return node;
    }

}
