package com.joshondesign.amino;

import com.joshondesign.amino.nodes.Node;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Anim {
    private Node node;
    private String property;
    private double startValue;
    private double endValue;
    private double cycleDuration;

    public Anim(Node node, String property, double startValue, double endValue, double cycleDuration) {
        this.node = node;
        this.property = property;
        this.startValue = startValue;
        this.endValue = endValue;
        this.cycleDuration = cycleDuration;
    }
}
