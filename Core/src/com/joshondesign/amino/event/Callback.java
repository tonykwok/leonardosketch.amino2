package com.joshondesign.amino.event;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/24/11
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Callback<E extends Event> {
    public void call(E event);
}
