package com.joshondesign.amino;

import com.joshondesign.amino.draw.Color;
import com.joshondesign.amino.draw.Fill;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/17/11
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static final Color BLACK = Color.rgb(0x000000);
    public static final Color WHITE = Color.rgb(0xffffff);
    public static final Color RED = Color.rgb(0xff0000);
    public static final Color GREEN = Color.rgb(0x00ff00);
    public static final Color BLUE = Color.rgb(0x0000ff);
    public static final Color MAGENTA = Color.rgb(0xff00ff);
    public static final Color ORANGE = Color.rgb(0x00ffff);

    public static double random(double min, double max) {
        return Math.random()*(max-min) + min;
    }

    public static Fill randomHue() {
        return Color.hsv(Math.random()*360,1,1);
    }
}
