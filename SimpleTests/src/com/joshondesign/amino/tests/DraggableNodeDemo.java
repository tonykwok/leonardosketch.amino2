package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.draw.*;
import com.joshondesign.amino.event.Callback;
import com.joshondesign.amino.event.MouseEvent;
import com.joshondesign.amino.nodes.GroupNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;

import java.util.ArrayList;
import java.util.List;

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

        final List<DraggableNode> nodes = new ArrayList<DraggableNode>();
        nodes.add(dn1);
        nodes.add(dn2);

        Core.getImpl().getEventBus().listen(MouseEvent.Pressed, new Callback<MouseEvent>(){
            public void call(MouseEvent event) {
                //u.p("pressed");
                for(DraggableNode n : nodes) {
                    if(n.contains(event.getPoint())) {
                        n.setSelected(true);
                    }
                }
            }
        });
        Core.getImpl().getEventBus().listen(MouseEvent.Dragged, new Callback<MouseEvent>(){
            public void call(MouseEvent event) {
                //u.p("dragged");
                for(DraggableNode n : nodes) {
                    if(n.isSelected()) {
                        n.x = event.getPoint().getX();
                        n.y = event.getPoint().getY();
                    }
                }
            }
        });
        Core.getImpl().getEventBus().listen(MouseEvent.Released, new Callback<MouseEvent>(){
            public void call(MouseEvent event) {
                //u.p("released");
                for(DraggableNode n : nodes) {
                    n.setSelected(false);
                }
            }
        });

        return new GroupNode().add(dn1).add(dn2);
    }

    class DraggableNode extends Node {
        double x;
        double y;
        private DraggableNode target;
        private boolean selected = false;

        @Override
        public void draw(Gfx gfx) {
            gfx.translate(x,y);
            if(selected) {
                gfx.setFill(Color.rgb(1.0,1.0,0.5));
            } else {
                gfx.setFill(Color.rgb(0.5, 0.5, 0.5));
            }
            gfx.fill(Rectangle.build(0,0,50,100));
            gfx.translate(-x,-y);

            if(target != null) {
                Path path = Path
                        .moveTo(x+50,y+20)
                        .curveTo(x+50+30,y+20,
                                target.x-30, target.y+20,
                                target.x, target.y+20)
                        //.lineTo(target.x,target.y+20)
                        .build();
                gfx.draw(path, Color.rgb(0,1,0), null, null, Blend.Normal);
            }

        }

        @Override
        public boolean contains(Point point) {
            return Rect.build(0 + (int) x, 0 + (int) y, 50, 100).contains(point);
        }

        public void connect(DraggableNode dn2) {
            target = dn2;
        }

        public void setSelected(boolean b) {
            this.selected = b;
        }

        public boolean isSelected() {
            return selected;
        }
    }
}
