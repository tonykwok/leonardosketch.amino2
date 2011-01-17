package com.joshondesign.amino;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE_2D;

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

    public void enable(GL2 gl) {
        shader.use(gl);
    }

    public void disable(GL2 gl) {
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
