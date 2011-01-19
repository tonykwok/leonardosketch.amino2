package com.joshondesign.amino.tests;

import com.joshondesign.amino.Anim;
import com.joshondesign.amino.Core;
import com.joshondesign.amino.Path;
import com.joshondesign.amino.Util;
import com.joshondesign.amino.nodes.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomShapeDemo implements NodeCreator{
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new RandomShapeDemo());
    }

    public Node create() throws NoSuchMethodException {
        GroupNode group = new GroupNode();
        for(int i=0; i<1000; i++) {
            double x = Util.random(0,800);
            double y= Util.random(0,600);
            Path path = Path.moveTo(0,0)
                    .lineTo(Util.random(90,110),Util.random(-10,10))
                    .lineTo(Util.random(90,110),Util.random(90,110))
                    .curveTo(Util.random(30,60),130, 40,130, 0,100)
                    .lineTo(0,0)
                    .closeTo()
                    .build();
            PathNode pathNode = new PathNode(path);
            pathNode.setFill(Util.randomHue());
            TransformNode transformNode = new TransformNode(pathNode);
            transformNode.setTranslateX(100);
            group.add(transformNode);
            Core.getImpl().add(new Anim(transformNode,"translateX",x,y, Util.random(3,10)).setAutoReverse(true));
            Core.getImpl().add(new Anim(transformNode,"translateY",y,x, Util.random(3,10)).setAutoReverse(true));
        }
        return group;
    }
}
