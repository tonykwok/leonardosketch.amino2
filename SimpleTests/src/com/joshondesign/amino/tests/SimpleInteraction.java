package com.joshondesign.amino.tests;

import com.joshondesign.amino.Color;
import com.joshondesign.amino.*;
import com.joshondesign.amino.Point;
import com.joshondesign.amino.event.MouseEvent;
import com.joshondesign.amino.nodes.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Demonstrates simple interaction.
 *  // a grid background
 *
 * - A button drawing with grid9
 * with three states: normal, hover, press,
 *
 * // - transparent hover area with some nodes in it. fades in and out
 * // as you mouse in to and out of it.
 *
 * // - collapsing sidebar that pops out and slides back in when you
 * click on it's expansion button
 *
 * a button that glows when you mouse into it and unglows when you
 * mouse out. it will do a true GPU glow effect (optimizable into grid9 slice later)
 *
 * // this requires having a minimal mouse event. just
 * // hook into the event bus and listen for stage wide mouse events
 *
 *
 *
 */

// make opacity work on groups
// make mouse press event work on groups

public class SimpleInteraction implements NodeCreator {

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new SimpleInteraction());
    }


    public Node create() throws NoSuchMethodException {
        BufferedImage im = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setPaint(new java.awt.Color(1,1,1));
        g.fillRect(0, 0, 32, 32);
        g.setPaint(java.awt.Color.GRAY);
        g.drawLine(0, 10, 32, 10);
        g.drawLine(10,0,10,32);
        RectangleNode background = new RectangleNode().setWidth(640).setHeight(480);
        background.setFill(TextureFill.build(im));


        RectangleNode sidebar = new RectangleNode()
                .setX(0).setY(40).setWidth(100).setHeight(300)
                .setCorner(20);
        sidebar.setFill(Color.rgba(0.8, 0.8, 0.8, 0.8));
        //sidebar.setStroke(Color.rgb(1,1,1));
        sidebar.setStrokeWidth(10);

        GroupNode sidebarGroup = new GroupNode();
        sidebarGroup.add(sidebar);
        TransformNode sidebarTrans = new TransformNode(sidebarGroup);

        Core.getImpl().add(new ToggleAnimation(sidebarTrans,"translateX",-80,10,0.20, MouseEvent.Pressed));

        RectangleNode fadebar = new RectangleNode()
                .setX(100)
                .setY(10)
                .setWidth(300)
                .setHeight(50);
        fadebar.setFill(Color.rgb(0,0,1));
        fadebar.setOpacity(0.5);
        Core.getImpl().add(new ToggleAnimation(fadebar,"opacity",0.3,0.95,0.2, MouseEvent.Moved));


        RectangleNode glowRect = new RectangleNode().setX(200).setY(200).setWidth(50).setHeight(50);
        glowRect.setFill(Color.rgb(0, 1, 1));
        DropshadowEffect glow = DropshadowEffect.build(0, 0, 10, Color.rgb(1, 1, 0));
        EffectBuffer buff = new EffectBuffer(glowRect,glow);

        //tell her we'll call her on sunday when we get home.
        //Core.getImpl().add(new Anim(glow, "x", 200, 220, 2.0));

        GroupNode group = new GroupNode()
                .add(background)
                .add(sidebarTrans)
                .add(fadebar)
                .add(buff)
                ;
        return group;
    }


    private class EffectBuffer extends Node {
        private RectangleNode node;
        private DropshadowEffect effect;
        private Buffer buffer;

        public EffectBuffer(RectangleNode glowRect, DropshadowEffect glow) {
            this.node = glowRect;
            this.effect = glow;
        }

        @Override
        public void draw(Gfx gfx) {
            if(buffer == null) {
                buffer = gfx.createBuffer(300,300);
            }
            gfx.setTargetBuffer(buffer);
            node.draw(gfx);
            gfx.setTargetBuffer(gfx.getDefaultBuffer());
            gfx.applyEffect(null,buffer,gfx.getDefaultBuffer(),effect);
        }

        @Override
        public boolean contains(Point point) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
