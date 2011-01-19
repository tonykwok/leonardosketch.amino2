package com.joshondesign.amino;

import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE_2D;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/18/11
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextureShader {
    private Shader shader;

    public TextureShader(GL2 gl) {
        shader = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/BasicTexture.frag")
        );
    }

    public void enable(GL2 gl, TextureFill fill) {

        if(fill.texture == null) {
            fill.texture = AWTTextureIO.newTexture(fill.img, false);
            fill.texture.setTexParameteri(GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            fill.texture.setTexParameteri(GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        }

        gl.glActiveTexture(GL.GL_TEXTURE0);
        shader.use(gl);
        fill.texture.bind();
        shader.setIntParameter(gl, "tex0", 0);
        float height = 480-22;
        shader.setVec2Parameter(gl, "textureSize", fill.texture.getImageWidth(), fill.texture.getImageHeight());
        shader.setFloatParameter(gl, "screenHeight", height);
    }

    public void disable(GL2 gl) {
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
