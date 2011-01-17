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
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PhotoJumper implements NodeCreator{
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new PhotoJumper());
    }

    public Node create() throws NoSuchMethodException {
        GroupNode group = new GroupNode();
        for(int i=0; i<20; i++) {
            RectangleNode rect = new RectangleNode()
                    .setX(0).setY(0).setWidth(50).setHeight(50);
            rect.setFill(Color.rgb(0,1,0));
            Core.getImpl().add(new Anim(rect,"x",
                    (i%6)*100,  //start
                    500-(i/6)*100,          //end
                    3)
                    .setAutoReverse(true));
            Core.getImpl().add(new Anim(rect,"y",
                    (int)(i/6)*100,     //start
                    (19-i)%6*100,                  //end
                    3)
                    .setAutoReverse(true));
            group.add(rect);
        }

        return group;
    }
}
