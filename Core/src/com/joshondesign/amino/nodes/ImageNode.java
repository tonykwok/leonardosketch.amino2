package com.joshondesign.amino.nodes;

import com.joshondesign.amino.draw.Gfx;
import com.joshondesign.amino.draw.Point;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/23/11
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageNode extends Node {

    public ImageNode(URL resource) {
    }

    @Override
    public void draw(Gfx gfx) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean contains(Point point) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
