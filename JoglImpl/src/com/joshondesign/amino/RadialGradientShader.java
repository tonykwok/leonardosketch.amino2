package com.joshondesign.amino;

import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.media.opengl.GL.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class RadialGradientShader {
    private Shader shader;


    public RadialGradientShader(GL2 gl) {
        shader = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/RadialGradient.frag")
        );


    }

    public void enable(GL2 gl, RadialGradient fill) {
        if(fill.texture == null) {
            //create a 1d texture containing the colors
            BufferedImage im = new BufferedImage(1,256,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = im.createGraphics();
            float[] floats = new float[fill.stops.size()];
            java.awt.Color[] colors = new Color[fill.stops.size()];
            for(int i=0; i<fill.stops.size(); i++) {
                floats[i] = (float) fill.stops.get(i).position;
                colors[i] = new java.awt.Color(fill.stops.get(i).color.toRGBInt());
            }
            g2.setPaint(new LinearGradientPaint(0,0,0,256,floats,colors));
            g2.fillRect(0,0,1,256);
            g2.dispose();


            try {
                ImageIO.write(im, "png", new File("testblah.png"));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            fill.texture = AWTTextureIO.newTexture(im, false);
            fill.texture.setTexParameteri(GL_TEXTURE_WRAP_S, GL_REPEAT);
            fill.texture.setTexParameteri(GL_TEXTURE_WRAP_T, GL_REPEAT);
            //fill.texture.setTexParameteri(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            //fill.texture.setTexParameteri(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        }

        shader.use(gl);
        shader.setFloatParameter(gl, "translateX", (float) 0.0);
        shader.setFloatParameter(gl, "translateY", (float) 200);
        shader.setFloatParameter(gl, "centerX", (float) fill.start.getX());
        shader.setFloatParameter(gl, "centerY", (float) fill.start.getY());
        shader.setFloatParameter(gl, "gradientRadius", (float) fill.radius);
        shader.setFloatParameter(gl, "screenHeight", (float) 480 - 22);
        //shader.setVec4Parameter(gl, "startColor", fill.stops.get(0).color);
        //shader.setVec4Parameter(gl, "endColor", fill.stops.get(1).color);

        gl.glActiveTexture(GL.GL_TEXTURE0);
        fill.texture.bind();
        shader.setIntParameter(gl,"tex0",0);

    }

    public void disable(GL2 gl) {
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
