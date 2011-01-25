package com.joshondesign.amino.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/24/11
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventBus {

    private Map<String,List<Callback>> listeners = new HashMap<String,List<Callback>>();

    public void listen(String pressed, Callback callback) {
        if(!listeners.containsKey(pressed)) {
            listeners.put(pressed, new ArrayList<Callback>());
        }
        List<Callback> list = this.listeners.get(pressed);
        list.add(callback);
    }

    public void publish(String key, Event event) {
        if(listeners.containsKey(key)) {
            for(Callback c : listeners.get(key)) {
                c.call(event);
            }
        }
    }
}
