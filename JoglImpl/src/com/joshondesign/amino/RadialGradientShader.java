package com.joshondesign.amino;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL.GL_TEXTURE0;
import static javax.media.opengl.GL.GL_TEXTURE_2D;

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
        shader.use(gl);
        shader.setFloatParameter(gl,"translateX", (float) 0.0);
        shader.setFloatParameter(gl,"translateY", (float) 200);
        shader.setFloatParameter(gl,"centerX", (float) fill.start.getX());
        shader.setFloatParameter(gl,"centerY", (float) fill.start.getY());
        shader.setFloatParameter(gl,"gradientRadius", (float) fill.radius);
        shader.setFloatParameter(gl,"screenHeight", (float) 480-22);
        shader.setVec4Parameter(gl,"startColor",fill.stops.get(0).color);
        shader.setVec4Parameter(gl,"endColor",fill.stops.get(1).color);

    }

    public void disable(GL2 gl) {
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
    }
}
