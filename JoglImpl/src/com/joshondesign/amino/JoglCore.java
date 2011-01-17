package com.joshondesign.amino;

import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.sun.opengl.util.Animator;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglCore extends Core {
    private GLCanvas canvas;
    private Frame frame;
    private List<Anim> anims;
    Node root;

    public JoglCore() {
        this.anims = new ArrayList<Anim>();
    }

    @Override
    public void init(NodeCreator basicAnimTest) {
        try {
            this.root = basicAnimTest.create();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void add(Anim anim) {
        this.anims.add(anim);
    }

    @Override
    protected void init() {
        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        frame = new Frame("AWT Frame");
        frame.setSize(640,480);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        //bind to the gl event listener
        canvas.addGLEventListener(new JoglEventListener(this));

        //use a regular animator to let the OS handle frame rate throttling for us
        Animator animator =new Animator(canvas);
        animator.add(canvas);
        animator.start();
    }

    public Iterable<? extends Anim> getAnimations() {
        return anims;
    }
}
