package com.joshondesign.amino;

import com.joshondesign.amino.anim.Animateable;
import com.joshondesign.amino.draw.Java2DGfx;
import com.joshondesign.amino.event.EventBus;
import com.joshondesign.amino.nodes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/10/11
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
class Java2DCore extends Core {
    private List<Animateable> animateables;
    private NodeCreator nodeCreator;
    private Node root;
    private long startTime;
    private long lastTime;
    private Timer timer;
    private static final boolean RENDER_TO_BUFFER = false;

    Java2DCore() {
        this.animateables = new ArrayList<Animateable>();
    }

    public static void p(String s) {
        System.out.println(s);
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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame frame = new JFrame("title");
                startTime = System.nanoTime();
                lastTime = System.nanoTime();

                final BufferedImage img = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
                final JComponent comp = new RootRenderComponent(img, frame);
                frame.add(comp);
                frame.pack();
                frame.setSize(640, 480 + 22);
                frame.setVisible(true);

                timer = new Timer(10, new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        comp.repaint();
                    }
                });
                timer.start();

            }
        });
    }

    @Override
    public void waitForRedraw() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int readbackPixel(int x, int y) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EventBus getEventBus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void drawNode(Java2DGfx gfx, Node root) {
        if(root instanceof GroupNode) {
            drawGroup(gfx,(GroupNode)root);
        }
        if(root instanceof ShapeNode) {
            drawShape(gfx, (ShapeNode) root);
        }
        if(root instanceof TransformNode) {
            drawTransform(gfx, (TransformNode) root);
        }

    }
    private void drawTransform(Java2DGfx gfx, TransformNode root) {
        gfx.translate(root.getTranslateX(),root.getTranslateY());
        gfx.rotate(0,0,root.getRotate());
        drawNode(gfx,root.getChild());
        gfx.rotate(0,0,-root.getRotate());
        gfx.translate(-root.getTranslateX(), -root.getTranslateY());
    }

    private void drawGroup(Java2DGfx gfx, GroupNode group) {
        for(Node n : group.getChildren()) {
            drawNode(gfx,n);
        }
    }

    private void drawShape(Java2DGfx gfx, ShapeNode shape) {
        shape.draw(gfx);
    }

    public Iterable<? extends Animateable> getAnimations() {
        return animateables;
    }

    private class RootRenderComponent extends JComponent {
        private final BufferedImage img;
        private final JFrame frame;

        public RootRenderComponent(BufferedImage img, JFrame frame) {
            this.img = img;
            this.frame = frame;
        }

        public void paintComponent(Graphics g) {


            //process the animations first
            long currentTime = System.nanoTime();
            for(Animateable animateable : getAnimations()) {
                try {
                    animateable.process(startTime,currentTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            lastTime = currentTime;

            //create root node if needed
            if(root == null && nodeCreator != null) {
                try {
                    root = nodeCreator.create();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            if(RENDER_TO_BUFFER) {
                //render to a buffer so we can capture output
                Graphics2D g2 = (Graphics2D) g;
                Graphics2D g3 = img.createGraphics();
                g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g3.clearRect(0, 0, 640, 480);
                Java2DGfx gfx = new Java2DGfx(frame, g3, this, img);
                drawNode(gfx,root);
                g3.dispose();
                g2.drawImage(img, 0, 0, null);
            } else {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(java.awt.Color.BLACK);
                g2.fillRect(0,0,640,480);
                Java2DGfx gfx = new Java2DGfx(frame, g2, this, img);
                drawNode(gfx,root);
            }
        }
    }
}

