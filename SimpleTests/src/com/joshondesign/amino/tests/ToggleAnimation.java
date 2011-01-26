package com.joshondesign.amino.tests;

import com.joshondesign.amino.Anim;
import com.joshondesign.amino.Core;
import com.joshondesign.amino.event.Callback;
import com.joshondesign.amino.event.MouseEvent;
import com.joshondesign.amino.nodes.Node;

import java.lang.reflect.InvocationTargetException;

/**
* Created by IntelliJ IDEA.
* User: joshmarinacci
* Date: 1/25/11
* Time: 11:13 AM
* To change this template use File | Settings | File Templates.
*/
class ToggleAnimation extends Anim implements Callback<MouseEvent> {

    private Node shape;
    private boolean going = false;
    private boolean opened;
    private MouseEvent last;

    public ToggleAnimation(Node node, String property, double going, double end, double time, String ... eventKey) throws NoSuchMethodException {
        super(node,property, going,end,time);
        shape = node;
        setAutoReverse(true);
        for(String key : eventKey) {
            Core.getImpl().getEventBus().listen(key, this);
        }
    }

    @Override
    public void process(long startTime, long currentTime) throws InvocationTargetException, IllegalAccessException {
        if(going) {
            long elapsedNano = currentTime/1000-personalStart/1000;
            long duration = (int)(cycleDuration*1000*1000);
            double fract = ((double)elapsedNano)/((double)duration);
            if(opened) {
                fract = 1.0-fract;
            }
            if(between(fract,0.0,1.0)) {
                setFraction(fract);
            }
            if(fract >= 1.0) {
                opened = true;
                going = false;
            }
            if(fract <= 0) {
                opened = false;
                going = false;
            }
        }
    }

    private boolean between(double fract, double min, double max) {
        if(fract < min) return false;
        if(fract > max) return false;
        return true;
    }

    public void call(MouseEvent event) {
        if(event.getType() == MouseEvent.Moved) {
            if(last == null && shape.contains(event.getPoint())) {
                //entered
                trigger();
                last = event;
            }
            if(last != null && !shape.contains(event.getPoint())) {
                //exited
                trigger();
                last = null;
            }
            return;
        }
        if(shape.contains(event.getPoint())) {
            personalStart = System.nanoTime();
            going = !going;
        }
    }

    private void trigger() {
        personalStart = System.nanoTime();
        going = !going;
    }

}
