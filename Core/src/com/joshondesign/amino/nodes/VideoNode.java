package com.joshondesign.amino.nodes;

import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.Point;
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
 * Date: 1/26/11
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class VideoNode extends Node {
    private File file;
    private IntBuffer buffer;
    private IntBuffer lastBuffer;
    private int w;
    private int h;

    public VideoNode(File file) {
        this.file = file;
        Gst.setUseDefaultContext(true);
        String[] args = new String[0];
        Gst.init("GSVideo", args);
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
        gplayer.play();
    }
    private void invokeEvent(int w, int h, IntBuffer buffer) {
        this.buffer = buffer;
        this.w = w;
        this.h = h;
    }
    private static void eosEvent() {
        p("eos event");
    }
    private static void p(String s) {
        System.out.println(s);
    }

    @Override
    public void draw(Gfx gfx) {
        if(buffer == null) return;

        if(buffer != lastBuffer) {
            lastBuffer = buffer;
            gfx.drawIntBuffer(lastBuffer,w,h);
        } else {
            gfx.drawIntBuffer(lastBuffer,w,h);
        }

    }

    @Override
    public boolean contains(Point point) {
        return false;
    }
}
