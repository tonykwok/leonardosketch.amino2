package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Fill;
import com.joshondesign.amino.Gfx;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ShapeNode extends Node {
    private Fill fill;

    public void setFill(Fill fill) {
        this.fill = fill;
    }
    public Fill getFill() {
        return fill;
    }

    public abstract void draw(Gfx gfx);
}
