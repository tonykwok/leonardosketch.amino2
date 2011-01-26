package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupNode extends Node {
    private List<Node> nodes = new ArrayList<Node>();

    public GroupNode add(Node node) {
        this.nodes.add(node);
        return this;
    }

    public Iterable<? extends Node> getChildren() {
        return this.nodes;
    }

    @Override
    public void draw(Gfx gfx) {

    }

    @Override
    public boolean contains(Point point) {
        for(Node n : nodes) {
            if(n.contains(point)) return true;
        }
        return false;
    }
}
