package com.joshondesign.amino;

import com.joshondesign.amino.event.EventBus;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.sun.opengl.util.Animator;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.*;
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
    private String monitor = "MONITOR";
    boolean doReadback;
    int readbackValue;
    int doReadbackX;
    int doReadbackY;
    private EventBus eventBus = new EventBus();

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

        frame = new Frame("AWTs Frame");
        frame.setSize(640,480);
        frame.add(canvas);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        canvas.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            public void mousePressed(MouseEvent mouseEvent) {
                publish(mouseEvent);
            }

            public void mouseReleased(MouseEvent mouseEvent) {
            }

            public void mouseEntered(MouseEvent mouseEvent) {
            }

            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
        canvas.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent mouseEvent) {
            }

            public void mouseMoved(MouseEvent mouseEvent) {
                com.joshondesign.amino.event.MouseEvent event = new com.joshondesign.amino.event.MouseEvent(
                    new Point(mouseEvent.getPoint().getX(),mouseEvent.getPoint().getY()),
                    com.joshondesign.amino.event.MouseEvent.Moved
                );
                getEventBus().publish(com.joshondesign.amino.event.MouseEvent.Moved, event);
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
        System.out.println("created a jogl frame");

        frame.requestFocus();
    }

    private void publish(MouseEvent mouseEvent) {
        com.joshondesign.amino.event.MouseEvent event = new com.joshondesign.amino.event.MouseEvent(
            new Point(mouseEvent.getPoint().getX(),mouseEvent.getPoint().getY()),
            com.joshondesign.amino.event.MouseEvent.Pressed
        );
        getEventBus().publish(com.joshondesign.amino.event.MouseEvent.Pressed, event);
    }

    @Override
    public void waitForRedraw() {
        try {
            //Thread.sleep(5000);
            synchronized (monitor) {
                monitor.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public int readbackPixel(int x, int y) {
        doReadback = true;
        doReadbackX = x;
        doReadbackY = y;
        waitForRedraw();
        return readbackValue;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    public Iterable<? extends Animateable> getAnimations() {
        return animateables;
    }

    public void releaseWaiters() {
        synchronized (monitor) {
            monitor.notify();
        }
    }
}
