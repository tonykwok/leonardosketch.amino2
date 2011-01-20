package com.joshondesign.amino;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglEventListener implements GLEventListener {
    int width;
    int height;
    private JoglGfx gfx;
    private JoglCore core;
    private JoglDrawer drawer;
    private long startTime;
    private long lastTime;

    public JoglEventListener(JoglCore joglCore) {
        this.core = joglCore;
    }
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        //use vsync to let the OS handle the framerate and reduce tearing
        gl.setSwapInterval(1);
        //various screen setup parameters
        gl.glShadeModel(GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glEnable( GL_TEXTURE_2D );

        //disable face culling, this lets us have clockwise and counter clockwise polys
        //gl.glEnable( GL_CULL_FACE );
        //gl.glCullFace(GL_BACK);


        width = drawable.getWidth();
        height = drawable.getHeight();
        gfx = new JoglGfx(this, gl);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glEnable(GL_BLEND);
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        drawer = new JoglDrawer();

        startTime = System.nanoTime();
        lastTime = System.nanoTime();
    }

    private long prevTime = -1;
    private double[] timeMeasurements = new double[NUM_TIME_MEASUREMENTS];
    private int iCurrentTimeRecord = 0;
    private static final int NUM_TIME_MEASUREMENTS = 30;
    private double fps;

    private int count;
    public void display(GLAutoDrawable drawable) {
        if ( prevTime != -1 ) {
            long t1 = System.nanoTime();
            long t = t1 - prevTime;
            timeMeasurements[iCurrentTimeRecord++%NUM_TIME_MEASUREMENTS] = t;
            prevTime = t1;
            //cube.dt = (float) (t*1e-9);
            double s = 0.0;
            for ( int i=0; i<NUM_TIME_MEASUREMENTS; i++ ) {
                s += timeMeasurements[i];
            }
            fps = (NUM_TIME_MEASUREMENTS*1e9)/s;
        } else {
            prevTime = System.nanoTime();
            fps = 30.0; // some initial value
            //cube.dt = (float) (1.0/fps);
        }

        count++;
        if(count % 60 == 0) {
            p("count = " + count + " fps = " + fps);
        }

        try {
            if(core.root == null && core.nodeCreator != null) {
                core.root = core.nodeCreator.create();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //process the animations first
        long currentTime = System.nanoTime();
        for(Animateable animateable : core.getAnimations()) {
            //p("processing animation: " + anim);
            try {
                animateable.process(startTime,currentTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lastTime = currentTime;

        GL2 gl = drawable.getGL().getGL2();
        width = drawable.getWidth();
        height = drawable.getHeight();

        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glDepthMask(false);
        gl.glDisable(GL_LIGHTING);
        gl.glViewport(0, 0, width, height);
        JoglUtil.viewOrtho(gl, width, height);

        gl.glPushMatrix();
        gfx.gl = gl;
        drawer.drawNode(gfx, core.root);
        gl.glPopMatrix();


        /*
        //for debugging
        gl.glColor3d(1.0,0,0);
        gl.glBegin(GL_QUADS);
        gl.glVertex2d(-100, -100);
        gl.glVertex2d(100,-100);
        gl.glVertex2d(100,100);
        gl.glVertex2d(-100,100);
        gl.glEnd();
        */

        JoglUtil.viewPerspective(gl);
        if(core.doReadback) {
            core.readbackValue = gfx.readbackPixel(core.doReadbackX, core.doReadbackY);
        }
        core.releaseWaiters();
        /*
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        */
    }

    private void p(String s) {
        System.out.println(s);
    }

    GLU glu = new GLU();


    public void reshape(GLAutoDrawable gLDrawable, int i, int i1, int i2, int i3) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        if (height <= 0) height = 1;
        float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void dispose(GLAutoDrawable drawable) {

    }

}
