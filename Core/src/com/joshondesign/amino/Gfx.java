package com.joshondesign.amino;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/6/11
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Gfx extends MinimalGfx, FullGfx {
    public void setClip(Path build);
}
