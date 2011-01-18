package com.joshondesign.amino;

import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;

import static javax.media.opengl.GL.*;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinearGradientShader {
    private Shader shader;

    public LinearGradientShader(GL2 gl) {
        shader = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/LinearGradient.frag")
        );
    }

    public void enable(GL2 gl, LinearGradient fill) {
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

            /*
            try {
                ImageIO.write(im, "png", new File("testblah.png"));
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            */

            fill.texture = AWTTextureIO.newTexture(im, false);
            fill.texture.setTexParameteri(GL_TEXTURE_WRAP_S, GL_REPEAT);
            fill.texture.setTexParameteri(GL_TEXTURE_WRAP_T, GL_REPEAT);
            //fill.texture.setTexParameteri(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            //fill.texture.setTexParameteri(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        }
        shader.use(gl);

        shader.setFloatParameter(gl,"textureWidth", 100);
        shader.setFloatParameter(gl,"textureHeight", 100);
        float height = 480-22;
        shader.setFloatParameter(gl, "screenHeight", height);
        shader.setVec2Parameter(gl,  "startPoint", (float) fill.start.getX(), height-(float)fill.start.getY());
        shader.setVec2Parameter(gl,  "endPoint",  (float)fill.end.getX(), height-(float)fill.end.getY());

        gl.glActiveTexture(GL.GL_TEXTURE0);
        fill.texture.bind();
        shader.setIntParameter(gl, "tex0", 0);

    }

    public void disable(GL2 gl) {
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
