package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.draw.*;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;

public class CircleOrbDemo implements NodeCreator{
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new CircleOrbDemo());
    }

    double tick = 0;
    public Node create() throws NoSuchMethodException {
        return new ShapeNode() {

            @Override
            public void draw(Gfx gfx) {
                for(int i=0; i<30; i++) {
                    double x = Math.sin(tick-i*0.05)*200 + 250;
                    double y = Math.cos((tick-i*0.05)*3)*100 + 200;
                    gfx.setFill(Color.rgba(0.5, 0.5, 0.5, 0.5));
                    gfx.setBlend(Blend.Add);
                    gfx.fill(Ellipse.build(x, y, 30, 30));
                }
                tick += 0.02;
            }

            @Override
            public boolean contains(Point point) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
}
