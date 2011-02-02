package com.joshondesign.amino.tests;

import com.joshondesign.amino.anim.Anim;
import com.joshondesign.amino.draw.Blend;
import com.joshondesign.amino.Core;
import com.joshondesign.amino.nodes.BlendNode;
import com.joshondesign.amino.nodes.ImageNode;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;

/**
 * Cycles through 5 images, cross fading and smooth scaling each one.
 */
public class PhotoSlideshow implements NodeCreator {
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new PhotoSlideshow());
    }

    public Node create() throws NoSuchMethodException {
        ImageNode image1 = new ImageNode(getClass().getResource("image1.jpg"));
        ImageNode image2 = new ImageNode(getClass().getResource("image2.jpg"));

        BlendNode node = new BlendNode();
        node.setNode1(image1);
        node.setNode2(image2);
        node.setBlend(Blend.Normal);
        node.setFactor(0.5);

        Core.getImpl().add(new Anim(node,"factor",0,1,10.0).setAutoReverse(true));
        return node;
    }
}
