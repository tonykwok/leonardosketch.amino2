package com.joshondesign.amino.tests;

import com.joshondesign.amino.*;
import com.joshondesign.amino.Color;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;
import org.gstreamer.Bus;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.elements.PlayBin;
import org.gstreamer.elements.RGBDataSink;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleVideoTest implements NodeCreator {
    private BufferedImage buf;
    private JComponent comp;

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new SimpleVideoTest());
    }

    public Node create() throws NoSuchMethodException {

        buf = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
        Gst.setUseDefaultContext(true);
        String[] args = new String[0];
        Gst.init("GSVideo", args);

        File file = new File("bbt.avi");
        if(!file.exists()) {
            p("This file doesn't exist! " + file.getAbsolutePath());
            //return;
        }

        PlayBin gplayer = new PlayBin("GSMovie Player");
        gplayer.setInputFile(file);

        RGBDataSink videoSink = new RGBDataSink("rgb",
            new RGBDataSink.Listener() {
              public void rgbFrame(int w, int h, IntBuffer buffer) {
                invokeEvent(w, h, buffer);
              }
            });
        // Setting direct buffer passing in the video sink, so no new buffers are created
        // and disposed by the GC on each frame (thanks to Octavi Estapé for pointing
        // out this one).
        videoSink.setPassDirectBuffer(true);
        gplayer.setVideoSink(videoSink);

        gplayer.getBus().connect(new Bus.EOS() {
          public void endOfStream(GstObject element) {
            eosEvent();
          }
        });

        float sec = gplayer.queryDuration().toSeconds();
        float nanosec = gplayer.queryDuration().getNanoSeconds();
        p("sec = " + sec);
        p("nano sec = " + nanosec);
        comp = new JComponent(){
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics;
                g.drawImage(buf,0,0,null);
            }
        };
        gplayer.play();
        p("done starting to play");
        JFrame frame = new JFrame("yo");
        frame.setContentPane(comp);
        frame.pack();
        frame.setSize(400,400);
        frame.show();

        return new ShapeNode() {
            @Override
            public void draw(Gfx gfx) {
                gfx.drawRect(Rect.build(0, 0, 100, 100), Color.rgb(1,0,0),null,null);
                JoglGfx g = (JoglGfx) gfx;
                if(buf != null) {
                    g.drawImage(buf,0,0);
                }
            }
        };
    }

    private static void p(String s) {
        System.out.println(s);
    }

    private void invokeEvent(int w, int h, IntBuffer buffer) {
        if(w != buf.getWidth() || h != buf.getHeight()) {
            buf = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        }

        for(int y=0; y<buf.getHeight(); y++) {
            for(int x=0; x<w; x++) {
                int i = buffer.get();
                if(x < buf.getWidth()) {
                    buf.setRGB(x,y,i);
                }
            }
        }
        comp.repaint();
    }
    private static void eosEvent() {
        p("eos event");
    }
}
