package com.joshondesign.amino;

import com.sun.opengl.util.texture.Texture;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextureFill extends Fill {
    BufferedImage img;
    public Texture texture;


    TextureFill(BufferedImage img) {
        this.img = img;
    }

    public static Fill build(BufferedImage img) {
        return new TextureFill(img);
    }
}
