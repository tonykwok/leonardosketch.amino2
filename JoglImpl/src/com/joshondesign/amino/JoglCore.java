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
    private List<Animateable> animateables;
    Node root;
    NodeCreator nodeCreator;

    public JoglCore() {
        this.animateables = new ArrayList<Animateable>();
    }

    @Override
    public void init(NodeCreator creator) {
        this.nodeCreator = creator;
    }

    @Override
    public void add(Animateable animateable) {
        this.animateables.add(animateable);
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
        frame.setResizable(false);

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
        //FPSAnimator animator = new FPSAnimator(canvas, 60);
        //animator.setRunAsFastAsPossible(true);
        animator.add(canvas);
        animator.start();
    }

    public Iterable<? extends Animateable> getAnimations() {
        return animateables;
    }
}
