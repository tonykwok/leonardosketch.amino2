package com.joshondesign.amino.tests;

import com.joshondesign.amino.Core;
import com.joshondesign.amino.draw.effects.RippleEffect;
import com.joshondesign.amino.event.Callback;
import com.joshondesign.amino.event.MouseEvent;
import com.joshondesign.amino.nodes.EffectNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.joshondesign.amino.nodes.VideoNode;

import java.io.File;

/**
 * play a video by just plugging a url into
 * the media node
 */
public class VideoNodeTest implements NodeCreator {
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new VideoNodeTest());
    }

    public Node create() throws NoSuchMethodException {
        VideoNode video =  new VideoNode(new File("bbt.avi"));
        EffectNode effectNode = new EffectNode(video);
        final RippleEffect effect = RippleEffect.build();
        effectNode.setEffect(effect);
        Core.getImpl().getEventBus().listen(MouseEvent.Moved, new Callback<MouseEvent>(){
            public void call(MouseEvent event) {
                effect.setCenter(event.getPoint());
            }
        });
        return effectNode;
    }
}
