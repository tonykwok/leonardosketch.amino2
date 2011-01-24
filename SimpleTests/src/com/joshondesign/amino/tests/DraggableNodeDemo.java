package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.nodes.GroupNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;

/**
 * A simple demo showing the scaling of draggable nodes
 *
 * create a graph of 5 nodes connected with bezier paths. drag the nodes
 * around and see the paths move correctly.
 * use the scroll wheel to zoom in and out smoothly. between 10x & 0.1x
 */

public class DraggableNodeDemo implements NodeCreator {

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new DraggableNodeDemo());
    }

    public Node create() throws NoSuchMethodException {
        DraggableNode dn1 = new DraggableNode();
        dn1.x = 100;
        dn1.y = 100;

        DraggableNode dn2 = new DraggableNode();
        dn2.x = 300;
        dn2.y = 100;

        dn1.connect(dn2);

        return new GroupNode().add(dn1).add(dn2);
    }

    class DraggableNode extends Node {
        double x;
        double y;
        private DraggableNode target;

        @Override
        public void draw(Gfx gfx) {
            gfx.translate(x,y);
            gfx.setFill(Color.rgb(0.5,0.5,0.5));
            gfx.fill(Rectangle.build(0,0,50,100));

            Path path = Path
                    .moveTo(50,20)
                    .lineTo(100,75)
                    .build();
            gfx.draw(path, Color.rgb(0,1,0), null, null, Blend.Normal);

            gfx.translate(-x,-y);
        }

        public void connect(DraggableNode dn2) {
            target = dn2;
        }
    }
}
