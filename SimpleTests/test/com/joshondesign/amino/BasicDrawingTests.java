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
        p("comparing: " + Integer.toHexString(p1) + " " +  Integer.toHexString(p2));
        int r1 = p1>>16 & 0xFF;
        int r2 = p2>>16 & 0xFF;
        int g1 = p1>>8 & 0xFF;
        int g2 = p2>>8 & 0xFF;
        //p("g1/2 = " + g1 + " " + g2);
        if(Math.abs(r1-r2)<10 && Math.abs(g1-g2)<10) {
            //p("pixelNearValue!");
            return true;
        }
        return false;
    }

    private static final int RED = 0xffff0000;
    private static final int GREEN = 0xff0000ff;
    private static final int BLUE = 0xff00ff00;
    @Test
    public void testRect() {
        Core.getImpl().init(new NodeCreator() {
            public Node create() throws NoSuchMethodException {
                return new ShapeNode() {
                    @Override
                    public void draw(Gfx gfx) {
                        testRectImpl(gfx);
                    }
                };
            }
        });


        checkPixel(10, 10, BLUE);
        checkPixel(10, 110, GREEN);
        checkPixel(10, 210, RED);
    }
    private void testRectImpl(Gfx gfx) {
        gfx.fillRect(Rect.build(0,0,100,50),Color.rgb(0,1,0),null,null, Blend.Normal);
        gfx.fill(Ellipse.build(0,100,100,50).toPath(),Color.rgb(0,0,1),null,null,Blend.Normal);
        gfx.fill(Rectangle.build(0,200,100,50).toPath(),Color.rgb(1,0,0),null,null,Blend.Normal);
    }



}
