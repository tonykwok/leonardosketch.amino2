package com.joshondesign.amino;

import com.joshondesign.amino.nodes.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    private Method getMethod;
    private Method setMethod;
    private double currentValue;
    private long personalStart;

    public Anim(Node node, String property, double startValue, double endValue, double cycleDuration) throws NoSuchMethodException {
        this.node = node;
        this.property = property;
        this.startValue = startValue;
        this.endValue = endValue;
        this.cycleDuration = cycleDuration;

        String getter = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
        String setter = "set"+property.substring(0,1).toUpperCase()+property.substring(1);
        System.out.println("trying to animation the property: " + property);
        getMethod = this.node.getClass().getMethod(getter);
        setMethod = this.node.getClass().getMethod(setter,double.class);
        this.currentValue = startValue;
        this.personalStart = System.nanoTime();
    }

    public void process(long startTime, long currentTime) throws InvocationTargetException, IllegalAccessException {
        long elapsedNano = currentTime/1000-personalStart/1000;
        //p("ellapsed = " + elapsedNano);
        long duration = (int)(cycleDuration*1000*1000);
        //p("duration = " + duration);
        double fract = ((double)elapsedNano)/((double)duration);
        //p("fract = " + fract);
        if(fract >= 1.0) {
            personalStart = System.nanoTime();
        }
        double value = startValue + (endValue-startValue)*fract;
        setMethod.invoke(node,new Object[]{value});
    }

    private void p(String s) {
        System.out.println(s);
    }
}
