package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomNodeTest implements NodeCreator {
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new CustomNodeTest());
    }

    public Node create() throws NoSuchMethodException {
        return new ShapeNode() {
            @Override
            public void draw(Gfx gfx) {
                gfx.setFill(Color.rgb(0.5,1,1));
                //gfx.fill(new Oval())
                //fill in an oval
                //fill in a curved path
                //fill a rect with a linear gradient
                //fill a rect with a radial gradient
                //fill an oval with a texture
                //fill an oval with a texture and a dropshadow
                //fill an oval with a texture and then render 100 times across the screen
            }
        };
    }
}
