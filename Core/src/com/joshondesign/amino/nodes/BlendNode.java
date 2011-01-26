package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Blend;
import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.Point;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/23/11
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class BlendNode extends Node {
    private double factor;
    private Blend blend;
    private Node node2;
    private Node node1;

    @Override
    public void draw(Gfx gfx) {

    }

    @Override
    public boolean contains(Point point) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setNode1(Node image1) {
        this.node1 = image1;
    }


    public void setNode2(Node image2) {
        this.node2 = image2;
    }

    public void setBlend(Blend blend) {
        this.blend = blend;
    }


    public void setFactor(double factor) {
        this.factor = factor;
    }
}
