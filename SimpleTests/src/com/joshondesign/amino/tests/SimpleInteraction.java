package com.joshondesign.amino.tests;

import com.joshondesign.amino.Color;
import com.joshondesign.amino.Core;
import com.joshondesign.amino.TextureFill;
import com.joshondesign.amino.event.MouseEvent;
import com.joshondesign.amino.nodes.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Demonstrates simple interaction.
 * a grid background
 *
 * - A button drawing with grid9
 * with three states: normal, hover, press,
 *
 * - transparent hover area with some nodes in it. fades in and out
 * as you mouse in to and out of it.
 *
 * - collapsing sidebar that pops out and slides back in when you
 * click on it's expansion button
 *
 * a button that glows when you mouse into it and unglows when you
 * mouse out. it will do a true GPU glow effect (optimizable into grid9 slice later)
 *
 * this requires having a minimal mouse event. just
 * hook into the event bus and listen for stage wide mouse events
 *
 */

public class SimpleInteraction implements NodeCreator {

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new SimpleInteraction());
    }


    public Node create() throws NoSuchMethodException {
        BufferedImage im = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = im.createGraphics();
        g.setPaint(new java.awt.Color(1,1,1));
        g.fillRect(0,0,32,32);
        g.setPaint(java.awt.Color.GRAY);
        g.drawLine(0,10,32,10);
        g.drawLine(10,0,10,32);
        RectangleNode background = new RectangleNode().setWidth(640).setHeight(480);
        background.setFill(TextureFill.build(im));


        RectangleNode sidebar = new RectangleNode()
                .setX(-80).setY(40).setWidth(100).setHeight(300)
                .setCorner(20);
        sidebar.setFill(Color.rgba(0.8, 0.8, 0.8, 0.8));
        sidebar.setStroke(Color.rgb(1,1,1));
        sidebar.setStrokeWidth(10);
        Core.getImpl().add(new ToggleAnimation(sidebar,"x",-80,10,0.20, MouseEvent.Pressed));


        RectangleNode fadebar = new RectangleNode()
                .setX(100)
                .setY(10)
                .setWidth(300)
                .setHeight(50);
        fadebar.setFill(Color.rgb(0,0,1));
        fadebar.setOpacity(0.5);
        Core.getImpl().add(new ToggleAnimation(fadebar,"opacity",0,1.0,1.0, MouseEvent.Moved));

        GroupNode group = new GroupNode()
                .add(background)
                .add(sidebar)
                .add(fadebar)
                ;
        return group;
    }


}
