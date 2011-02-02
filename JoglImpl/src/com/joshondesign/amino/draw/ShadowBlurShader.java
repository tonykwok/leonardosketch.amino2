package com.joshondesign.amino.draw;

import com.joshondesign.amino.draw.effects.DropshadowEffect;

import javax.media.opengl.GL2;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/25/11
 * Time: 8:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShadowBlurShader extends Shader {
    private Shader shader;
    private DropshadowEffect effect;

    public ShadowBlurShader(GL2 gl) {
        shader = load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/ShadowBlur.frag")
        );
    }

    public void setEffect(DropshadowEffect effect) {
        this.effect = effect;
    }

    public void use(GL2 gl) {
        shader.use(gl);

        Color color = effect.getColor();

        shader.setFloatParameter(gl,"cx1",(float)color.r);
        shader.setFloatParameter(gl,"cx2",(float)color.g);
        shader.setFloatParameter(gl,"cx3",(float)color.b);
    }
}
