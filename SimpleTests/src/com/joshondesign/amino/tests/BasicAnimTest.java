package com.joshondesign.amino.tests;

import com.joshondesign.amino.Anim;
import com.joshondesign.amino.Color;
import com.joshondesign.amino.Core;
import com.joshondesign.amino.nodes.GroupNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.RectangleNode;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicAnimTest implements NodeCreator {

    public BasicAnimTest() {
    }

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new BasicAnimTest());
    }

    public Node create() throws NoSuchMethodException {
        RectangleNode rect = new RectangleNode()
                .setX(0).setY(0).setWidth(200).setHeight(100);
        rect.setFill(Color.rgb(1, 1, 1));
        Core.getImpl().add(new Anim(rect, "x", 0, 600, 6.0));
        Core.getImpl().add(new Anim(rect,"y", 300, 0, 10.0));
        Core.getImpl().add(new Anim(rect,"width",10,300,5.0));
        Core.getImpl().add(new Anim(rect,"height",300,10,10.0));

        GroupNode group = new GroupNode();
        group.add(rect);
        return group;
    }
}

