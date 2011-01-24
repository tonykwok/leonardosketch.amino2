package com.joshondesign.amino.tests;

import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;

/**
 * Demonstrates simple interaction.
 * - A button drawing with grid9
 * with three states: normal, hover, press,
 * - transparent hover area with some nodes in it. fades in and out
 * as you mouse in to and out of it.
 * - collapsing sidebar that pops out and slides back in when you
 * click on it's expansion button
 *
 * a button that glows when you mouse into it and unglows when you
 * mouse out. it will do a true GPU glow effect (optimizable into grid9 slice later)
 *
 * this requires having a minimal mouse event. just
 * hook into the event bus and listen for stage wide mouse events
 *
 */

public class SimpleInteraction implements NodeCreator {

    public Node create() throws NoSuchMethodException {
        return null;
    }
}
