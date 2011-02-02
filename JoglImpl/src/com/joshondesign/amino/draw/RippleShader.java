package com.joshondesign.amino.draw;

import com.joshondesign.amino.draw.effects.RippleEffect;

import javax.media.opengl.GL2;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 2/1/11
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class RippleShader extends Shader {
    private Shader shader;
    private RippleEffect effect;

    public RippleShader(GL2 gl) {
        shader = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/Ripple.frag")
        );
    }
    public void enable(GL2 gl, RippleEffect effect) {
        //shader.setFloatParameter(gl, "centerX", (float) 200);
        //shader.setFloatParameter(gl, "centerY", (float) 250);
        shader.setIntParameter(gl, "tex0",0);
    }
    public void setEffect(RippleEffect effect) {
        this.effect = effect;
    }

    @Override
    public void use(GL2 gl) {
        shader.use(gl);    //To change body of overridden methods use File | Settings | File Templates.
        shader.setIntParameter(gl, "tex0",0);
        shader.setFloatParameter(gl, "centerX", (float) effect.getCenter().getX());
        shader.setFloatParameter(gl, "centerY", (480-22)-(float) effect.getCenter().getY());
    }
}
