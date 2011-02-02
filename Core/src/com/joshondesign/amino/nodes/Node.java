package com.joshondesign.amino.nodes;

import com.joshondesign.amino.draw.Gfx;
import com.joshondesign.amino.draw.Point;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Node {
    public abstract void draw(Gfx gfx);
    public abstract boolean contains(Point point);
}
