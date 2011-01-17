package com.joshondesign.amino;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 11:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Animateable {
    void process(long startTime, long currentTime) throws InvocationTargetException, IllegalAccessException;
}
