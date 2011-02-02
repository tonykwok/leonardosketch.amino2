package com.joshondesign.amino.draw;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/6/11
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Gfx extends MinimalGfx, FullGfx {

    public BulkTexture createBulkTexture(int width, int height);

    public void drawBulkTexture(BulkTexture texture);
}
