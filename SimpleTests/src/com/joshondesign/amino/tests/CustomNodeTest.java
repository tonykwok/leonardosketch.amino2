package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;
import static com.joshondesign.amino.Util.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomNodeTest implements NodeCreator {
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new CustomNodeTest());
    }

    public Node create() throws NoSuchMethodException {
        return new ShapeNode() {
            @Override
            public void draw(Gfx gfx) {
                //fill in an oval
                //gfx.setFill(Color.rgb(0.5,1,1));
                //gfx.fill(Ellipse.build(30,60,80,30));

                //fill with a linear gradient
                gfx.setFill(LinearGradient
                        .line(new Point(0, 0), new Point(0, 100))
                        .addStop(0.0, GREEN)
                        .addStop(0.5, BLUE)
                        .addStop(1.0, RED)
                        .build()
                );
                gfx.translate(0,0);
                gfx.fill(Rect.build(0,0,400,400));
                gfx.translate(0,0);



                //fill with a radial gradient
                RadialGradient rgrad = new RadialGradient.RadialGradientBuilder()
                        .center(new Point(50, 25))
                        .radius(20)
                        .addStop(0.0, WHITE)
                        .addStop(0.5, Color.rgb(0x783587))
                        .addStop(0.75, RED)
                        .addStop(1.0, WHITE)
                        .build();

                gfx.translate(0,200);
                gfx.setFill(rgrad);
                gfx.fill(Ellipse.build(0,0,100,50));
                gfx.translate(0,-200);


                gfx.translate(200,200);
                gfx.setFill(rgrad);
                gfx.fill(Ellipse.build(0,0,100,50));
                gfx.translate(200,-200);
                //fill in a curved path
                //fill a rect with a radial gradient
                //fill an oval with a texture
                //fill an oval with a texture and a dropshadow
                //fill an oval with a texture and then render 100 times across the screen
            }
        };
    }
}
