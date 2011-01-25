package com.joshondesign.amino;

import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/20/11
 * Time: 7:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicDrawingTests {

    @Before
    public void setup() throws Exception {
        Core.init("jogl");
    }

    private void p(String s) {
        System.out.println(s);
    }

    @After
    public void teardown() {

    }

    private void checkPixel(int x, int y, int color) {
        Core.getImpl().waitForRedraw();
        assertTrue(near(Core.getImpl().readbackPixel(x,y), color));
    }

    private boolean near(int p1, int p2) {
        //p("comparing: " + Integer.toHexString(p1) + " " +  Integer.toHexString(p2));
        int r1 = p1>>16 & 0xFF;
        int r2 = p2>>16 & 0xFF;
        int g1 = p1>>8 & 0xFF;
        int g2 = p2>>8 & 0xFF;
        int b1 = p1 & 0xFF;
        int b2 = p2 & 0xFF;
        //p("r1 = " + Integer.toHexString(r1) + "  " + Integer.toHexString(r2));
        //p("g1 = " + Integer.toHexString(g1) + "  " + Integer.toHexString(g2));
        //p("b1 = " + Integer.toHexString(b1) + "  " + Integer.toHexString(b2));
        if(Math.abs(r1-r2)<10 && Math.abs(g1-g2)<10 && Math.abs(b1-b2)<10) {
            //p("pixelNearValue!");
            return true;
        }
        return false;
    }

    private static final int RED =      0xffff0000;
    private static final int GREEN =    0xff00ff00;
    private static final int BLUE =     0xff0000ff;
    private static final int MAGENTA =  0xffff00ff;

    @Test
    public void testRect() {
        Core.getImpl().init(new NodeCreator() {
            public Node create() throws NoSuchMethodException {
                return new ShapeNode() {
                    @Override
                    public void draw(Gfx gfx) {
                        testRectImpl(gfx);
                    }

                    @Override
                    public boolean contains(Point point) {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
            }
        });

        //fills
        checkPixel(10, 10, GREEN);
        checkPixel(10, 125, BLUE);
        checkPixel(10, 210, RED);
        checkPixel(20, 325, GREEN);

        //draw
        checkPixel(210, 0, GREEN);
        checkPixel(210, 100, MAGENTA);
        checkPixel(210, 200, 0x808080);

        //blend mode
        checkPixel(230,330,MAGENTA);
    }


    private void testRectImpl(Gfx gfx) {
        //fill rect
        gfx.fillRect(Rect.build(0,0,100,50),Color.rgb(0,1,0),null,null, Blend.Normal);
        gfx.fill(Ellipse.build(0,100,100,50).toPath(),Color.rgb(0,0,1),null,null,Blend.Normal);
        gfx.fill(Rectangle.build(0,200,100,50).toPath(),Color.rgb(1,0,0),null,(Path)null,Blend.Normal);
        gfx.setFill(Color.rgb(0,1,0));
        gfx.fill(Ellipse.build(0,300,100,50));

        //draw rect
        gfx.drawRect(Rect.build(200,0,100,50),Color.rgb(0,1,0),null,null, Blend.Normal);
        //draw rectangle with null rect clip
        gfx.draw(Rectangle.build(200,100,80,20).toPath(),Color.rgb(1,0,1),null,null, Blend.Normal);
        //draw rectangle with null path clip
        gfx.draw(Rectangle.build(200,200,80,20).toPath(),Color.rgb(0.5,0.5,0.5),null,(Path)null, Blend.Normal);

        //draw rect with add blend mode
        gfx.setFill(Color.rgb(0,1,0));
        gfx.fill(Rectangle.build(220,300,20,80).toPath(), Color.rgb(0.5,0,0.5),null,(Path)null,Blend.Add);
        gfx.fill(Rectangle.build(200,320,80,20).toPath(), Color.rgb(0.5,0,0.5),null,(Path)null,Blend.Add);

        //rect clip
        //gfx.setFill(Color.rgb(0,0,1));
        //gfx.setClip(Rect.build(0,0,40,40).toPath());
        //gfx.fill(Rectangle.build(400,0,100,100));

        //ellipse clip
        //gfx.setFill(Color.rgb(0,1,1));
        //gfx.setClip(Ellipse.build(0,0,40,40).toPath());
        //gfx.fill(Rectangle.build(400,100,100,100));
    }

    //draw other shapes with clipping
    //ngon
    //ellipse
    //path
    //convex path
    //concave path
    //concave path other direction



    //fill tests
    //fill rect with color
    //fill ellipse with color

    //fill rect with texture
    //fill ellipse with texture
    //ellipse clip ellipse with texture

    //fill rect with linear gradient
    //fill ellipse with linear gradient
    //ellipse clip ellipse with linear gradient

    //fill rect with radial gradient
    //fill ellipse with radial gradient
    //ellipse clip ellipse with radial gradient



    //blend mode tests
    //draw two rects with normal mode
    //draw two rects with add mode
    //draw two rects with srcin mode

    //draw two ellipses with normal mode
    //draw two ellipses with add mode
    //draw two ellipses with srcin mode




    private Buffer buffer;
    private Buffer buffer2;


    /*
    //buffer tests
    private void bufferTests(Gfx gfx) {
        clear(gfx);

        //create buffer if needed
        if(buffer == null) {
            buffer = gfx.createBuffer(100,100);
        }
        //draw two ellipses & green rect into a buffer
        Ellipse ellipse = Ellipse.build(0,0,50,30);
        gfx.fill(ellipse.toPath(), Color.rgb(0,1,0), buffer, (Rect)null, Blend.Normal);
        gfx.setTargetBuffer(buffer);
        gfx.fill(Rectangle.build(0,0,50,20).toPath(),   Color.rgba(1,1,0,0.5), buffer, (Rect)null, Blend.Normal);
        gfx.fill(Rectangle.build(50,50,20,30).toPath(), Color.rgb(0,0,1), buffer, (Rect)null, Blend.Normal);
        gfx.setTargetBuffer(gfx.getDefaultBuffer());

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
        DropshadowEffect effect = DropshadowEffect.build(3,3,3,Util.BLACK);
        gfx.applyEffect(Rect.build(0, 0, 100, 100), buffer, buffer2, effect);

        //copy buffer2 to the screen
        gfx.copyBuffer(
                Rect.build(0, 0, 100, 100), buffer2,
                Rect.build(100, 0, 100, 100), gfx.getDefaultBuffer(),
                Blend.Normal);
    }
    */


    /*
    //effects tests
    private void dropshadowTests(Gfx gfx) {
        //clear to white
        clear(gfx);

        //draw rect to a buffer
        Buffer buffer = gfx.createBuffer(100, 100);
        Rectangle rect = Rectangle.build(10,10,50,30);
        gfx.fill(rect.toPath(), Util.MAGENTA, buffer, (Rect) null, Blend.Normal);

        //blur the buffer contents
        DropshadowEffect effect = DropshadowEffect.build(5,5,3,Util.BLACK);
        Buffer b2 = gfx.createBuffer(110,110);
        gfx.applyEffect(Rect.build(0, 0, 100, 100), buffer, b2, effect);


        //copy to the screen
        gfx.copyBuffer(
                Rect.build(0,0,100,100), b2,
                Rect.build(0,0,100,100), gfx.getDefaultBuffer(),
                Blend.Normal);


        //draw rect directly to the screen with a dropshadow
        // should use accelerated path since it's a rectangle
        Rectangle rect2 = Rectangle.build(200,10,50,30);
        gfx.setFill(Util.MAGENTA);
        gfx.setEffect(DropshadowEffect.build(5,3,3,Util.BLACK));
        gfx.fill(rect2);


        //draw ellipse directly to the screen with a dropshadow
        //will probably fall back to a general buffer solution
        Ellipse ellipse = Ellipse.build(30,200,50,30);
        gfx.setFill(Util.GREEN);
        gfx.setEffect(effect);
        gfx.fill(ellipse);
    }
    */


    //state tests
    private void stateTests(Gfx gfx) {

        //draw four rects vertically in the colors:
        //blue, green, blue, green
        gfx.setFill(Util.BLUE);
        gfx.fill(Rectangle.build(0, 0, 20, 20));
        gfx.setFill(Util.GREEN);
        gfx.fill(Rectangle.build(0, 20, 20, 20));
        gfx.push();
        gfx.setFill(Util.BLUE);
        gfx.fill(Rectangle.build(0, 40, 20, 20));
        gfx.pop();
        //this should be drawn green
        gfx.fill(Rectangle.build(0, 60, 20, 20));
        checkPixel(10,70,GREEN);

        //should draw five rects in orange, alternating columns
        gfx.setFill(Util.ORANGE);
        gfx.fill(Rectangle.build(100, 0, 20, 20));     //normal
        //gfx.setTransform(Transform.translate(100, 0));    //
        gfx.fill(Rectangle.build(100, 0, 20, 20));     //translated to the right
        //gfx.setTransform(Transform.translate(-100, 0));   //
        gfx.fill(Rectangle.build(100, 25, 20, 20));    //normal
        gfx.push();                                    //push
        //gfx.setTransform(Transform.translate(100, 0));    //
        gfx.fill(Rectangle.build(100, 100, 20, 20));   //translated to the right
        gfx.pop();                                     //pop
        gfx.fill(Rectangle.build(100, 100, 20, 20));   //normal
    }


    private void clear(Gfx gfx) {
        Rect r = Rect.build(0,0,640,480);
        gfx.fillRect(r, Color.rgb(1, 1, 1), null, (Rect) null, Blend.Normal);
    }


}
