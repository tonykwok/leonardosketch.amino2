package com.joshondesign.amino.event;

import com.joshondesign.amino.draw.Point;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/24/11
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class MouseEvent extends Event {
    public static final String Pressed = new String("MOUSEEVENT_PRESSED");
    private Point point;
    private String type;
    public static final String Clicked = new String("MOUSEEVENT_CLICKED");
    public static final String Entered = new String("MOUSEEVENT_ENTERED");
    public static final String Exited = new String("MOUSEEVENT_EXITED");
    public static final String Moved = new String("MOUSEEVENT_MOVED");
    public static final String Dragged = new String("MOUSEEVENT_DRAGGED");
    public static final String Released = new String("MOUSEEVENT_RELEASED");

    public MouseEvent(Point point, String type) {
        this.point = point;
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public String getType() {
        return type;
    }
}
