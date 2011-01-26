package com.joshondesign.amino.tests;

import com.joshondesign.amino.Core;
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
        return new VideoNode(new File("bbt.avi"));
    }
}
