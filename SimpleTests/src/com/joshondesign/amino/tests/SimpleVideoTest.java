package com.joshondesign.amino.tests;

import com.joshondesign.amino.Core;
import com.joshondesign.amino.draw.Gfx;
import com.joshondesign.amino.draw.JoglGfx;
import com.joshondesign.amino.draw.Point;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.ShapeNode;
import org.gstreamer.Bus;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.elements.PlayBin;
import org.gstreamer.elements.RGBDataSink;

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
    //private BufferedImage buf;
    //private JComponent comp;
    private IntBuffer buffer;
    private int w;
    private int h;
    private IntBuffer lastBuffer;

    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new SimpleVideoTest());
    }

    public Node create() throws NoSuchMethodException {
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
        // and disposed by the GC on each frame (thanks to Octavi Estap� for pointing
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
        gplayer.play();

        return new ShapeNode() {
            @Override
            public void draw(Gfx gfx) {
                if(buffer == null) return;
                JoglGfx g = (JoglGfx) gfx;

                if(buffer != lastBuffer) {
                    lastBuffer = buffer;
                    g.drawIntBuffer(lastBuffer,w,h);
                } else {
                    g.drawIntBuffer(lastBuffer,w,h);
                }
            }

            @Override
            public boolean contains(Point point) {
                return false;
            }
        };
    }

    private static void p(String s) {
        System.out.println(s);
    }

    private void invokeEvent(int w, int h, IntBuffer buffer) {
        this.buffer = buffer;
        this.w = w;
        this.h = h;
    }
    private static void eosEvent() {
        p("eos event");
    }
}
