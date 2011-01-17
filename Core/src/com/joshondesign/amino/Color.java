package com.joshondesign.amino;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:32 PM
 * To change this template use File | Settings | File Templates.
 */

public class Color extends Fill {
    double r, g, b, a;

    private Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public static Color rgb(double r, double g, double b) {
        return new Color(r,g,b,1);
    }
    public static Color rgba(double r, double g, double b, double a) {
        return new Color(r,g,b,a);
    }
    public static Color hex(String hex) {
        return null;
    }
    public static Color rgb(int rgb) {
        return new Color(
                ((rgb>>16)&255)/255.0,
                ((rgb>>8)&255)/255.0,
                ((rgb&255)/255),
                1
        );
    }
    public static Color rgba(int rgba) {
        return null;
    }
    public static Color hsv(double h, double s, double v) {
        return null;
    }

    public String toString() {
        return "rgb("+r+", "+g+", "+b+"  a=" + a + ")";
    }

    public int toRGBInt() {
        return (((int)(r*255))<<16)
                + (((int)(g*255))<<8)
                + (((int)(b*255))<<0)
                ;
    }
}


