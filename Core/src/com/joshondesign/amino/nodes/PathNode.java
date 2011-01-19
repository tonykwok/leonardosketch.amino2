package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.Path;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathNode extends ShapeNode {
    private Path path;

    public PathNode(Path path) {
        this.path = path;
    }

    @Override
    public void draw(Gfx gfx) {
        gfx.setFill(getFill());
        gfx.fill(path);
    }
}
