package com.joshondesign.amino;

import com.joshondesign.amino.nodes.NodeCreator;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Core {
    private static Core _impl;


    public abstract void init(NodeCreator basicAnimTest);

    public static Core getImpl() {
        return _impl;
    }

    public abstract void add(Animateable animateable);

    public static void init(String pipeline) throws Exception {
        if(pipeline.equals("java2d")) {
            Class clss = Class.forName("com.joshondesign.amino.Java2DCore");
            Core tester = (Core) clss.newInstance();
            _impl = tester;
        }
        if(pipeline.equals("jogl")) {
            Class clss = Class.forName("com.joshondesign.amino.JoglCore");
            Core tester = (Core) clss.newInstance();
            _impl = tester;
        }
        _impl.init();
    }

    protected abstract void init();

    /*
    public static abstract class GFXContext {
        public abstract void invoke(GfxCallback gfxCallback) throws Exception;

        public abstract void done();
    }*/

}
