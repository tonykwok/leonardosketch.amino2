package com.joshondesign.amino;

import com.joshondesign.amino.nodes.GroupNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.ShapeNode;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglDrawer {
    public void drawNode(JoglGfx gfx, Node root) {
        if(root instanceof GroupNode) {
            drawGroup(gfx,(GroupNode)root);
        }
        if(root instanceof ShapeNode) {
            drawShape(gfx, (ShapeNode) root);
        }

    }

    private void drawGroup(JoglGfx gfx, GroupNode group) {
        for(Node n : group.getChildren()) {
            drawNode(gfx,n);
        }
    }

    private void drawShape(JoglGfx gfx, ShapeNode shape) {
        shape.draw(gfx);
    }

}