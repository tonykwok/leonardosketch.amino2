package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.draw.*;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomNodeTest implements NodeCreator {
    private Buffer buffer;
    private Buffer buffer2;
    private Buffer buffer3;

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
                /*

                //fill with a linear gradient
                gfx.setFill(LinearGradient
                        .line(new Point(0, 0), new Point(50, 100))
                        .addStop(0.0, Util.GREEN)
                        .addStop(0.5, Util.BLUE)
                        .addStop(1.0, Util.RED)
                        .build()
                );
                gfx.translate(0,0);
                gfx.fill(Rect.build(0,0,400,400));
                gfx.translate(0,0);



                //fill with a radial gradient
                RadialGradient rgrad = new RadialGradient.RadialGradientBuilder()
                        .center(new Point(50, 25))
                        .radius(20)
                        .addStop(0.0, Util.WHITE)
                        .addStop(0.5, Color.rgb(0x783587))
                        .addStop(0.75, Util.RED)
                        .addStop(1.0, Util.WHITE)
                        .build();

                gfx.translate(0,200);
                gfx.setFill(rgrad);
                gfx.fill(Ellipse.build(0,0,100,50));
                gfx.translate(0,-200);


                gfx.translate(200,200);
                gfx.setFill(rgrad);
                gfx.fill(Ellipse.build(0,0,100,50));
                gfx.translate(-200,-200);
                //fill in a curved path
                //fill a rect with a radial gradient
                //fill an oval with a texture
                //fill an oval with a texture and a dropshadow
                //fill an oval with a texture and then render 100 times across the screen
                */





                //clear(gfx);
                /*
                //create buffer if needed
                if(buffer == null) {
                    buffer = gfx.createBuffer(100,100);
                }
                //draw two ellipses & green rect into a buffer
                Ellipse ellipse = Ellipse.build(0,0,50,30);
                //gfx.setFill(Color.rgb(1, 0, 1));
                gfx.fill(ellipse.toPath(), Util.GREEN, buffer, (Rect)null, Blend.Normal);
                gfx.fill(Rectangle.build(0,0,50,20).toPath(),  Color.rgba(1,1,0,0.5), buffer, (Rect)null, Blend.Normal);
                gfx.fill(Rectangle.build(50,50,20,30).toPath(), Color.rgb(0,0,1), buffer, (Rect)null, Blend.Normal);

                //copy buffer to screen. should see everything

                gfx.copyBuffer(
                        Rect.build(0, 0, 100, 100), buffer,
                        Rect.build(0, 0, 100, 100), gfx.getDefaultBuffer(),
                        Blend.Normal);

                //copy buffer to screen offset 100px down
                // should see everything a second time
                gfx.copyBuffer(
                        Rect.build(0,   0, 100, 100), buffer,
                        Rect.build(0, 100, 100, 100), gfx.getDefaultBuffer(),
                        Blend.Normal);


                //create a second buffer if needed
                if(buffer2 == null) {
                    buffer2 = gfx.createBuffer(110,110);
                }

                //copy the first buffer into the second buffer using a dropshadow
                //BoxblurEffect effect = BoxblurEffect.build(3);
                DropshadowEffect shadowEffect = DropshadowEffect.build(3,3,3,Util.WHITE);
                RippleEffect rippleEffect = RippleEffect.build();

                gfx.applyEffect(Rect.build(0, 0, 100, 100), buffer, buffer2, rippleEffect);

                //copy buffer2 to the screen
                gfx.copyBuffer(
                        Rect.build(0, 0, 100, 100), buffer2,
                        Rect.build(0, 200, 100, 100), gfx.getDefaultBuffer(),
                        Blend.Normal);

                */
                Fill texfill = null;
                if(buffer3 == null) {
                    buffer3 = gfx.createBuffer(300,300);
                    try {
                        BufferedImage img = ImageIO.read(BasicAnimTest.class.getResource("cat.png"));
                        texfill = TextureFill.build(img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //draw a rect clipped by the ellipse below it, then blit to the screen
                gfx.fill(Ellipse.build(60, 20, 200, 100).toPath(), Color.rgba(1,1,1,1), buffer3, null, Blend.Normal);
                gfx.fill(Rect.build(0,0,200,200).toPath(), Color.rgba(0,1,0,1), buffer3, null, Blend.SrcIn);
                gfx.copyBuffer(
                        Rect.build(0, 0, 300, 300), buffer3,
                        Rect.build(0, 0, 300, 300), gfx.getDefaultBuffer(),
                        Blend.Normal);
            }

            @Override
            public boolean contains(Point point) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
    private static void clear(Gfx gfx) {
        Rect r = Rect.build(0,0,640,480);
        gfx.fillRect(r, Color.rgb(1, 1, 1), null, (Rect) null, Blend.Normal);
    }

}
