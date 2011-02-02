package com.joshondesign.amino.tests;

import com.joshondesign.amino.anim.Anim;
import com.joshondesign.amino.draw.Color;
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
        int size = 15;
        for(int i=0; i<200; i++) {
            RectangleNode rect = new RectangleNode()
                    .setX(0).setY(0).setWidth(50).setHeight(50);
            rect.setFill(Color.rgb(0,1,0));
            Core.getImpl().add(new Anim(rect,"x",
                    (i%size)*100,  //start
                    500-(i/size)*100,          //end
                    3)
                    .setAutoReverse(true));
            Core.getImpl().add(new Anim(rect,"y",
                    (int)(i/size)*100,     //start
                    (19-i)%size*100,                  //end
                    3)
                    .setAutoReverse(true));
            group.add(rect);
        }

        return group;
    }
}
