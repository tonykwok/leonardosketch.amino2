package com.joshondesign.amino.nodes;

import com.joshondesign.amino.draw.Gfx;
import com.joshondesign.amino.draw.Point;
import com.joshondesign.amino.draw.effects.Effect;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 2/1/11
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class EffectNode extends Node {
    private Effect effect;
    private VideoNode child;

    public EffectNode(VideoNode video) {
        this.child = video;
    }

    @Override
    public void draw(Gfx gfx) {
        gfx.setEffect(effect);
        child.draw(gfx);
        gfx.setEffect(null);
    }

    @Override
    public boolean contains(Point point) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
