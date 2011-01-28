package com.joshondesign.amino.tests;

import com.joshondesign.amino.Core;
import com.joshondesign.amino.Gfx;
import com.joshondesign.amino.JoglGfx;
import com.joshondesign.amino.Point;
import com.joshondesign.amino.nodes.Node;
import com.joshondesign.amino.nodes.NodeCreator;
import com.sun.opengl.util.gl2.GLUT;

import javax.media.opengl.GL2;

import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * A wireframe dodecahedon spinning in 3d
 * a bunch of glowing gauges on the sides of the screen.
 * small dotmatrix pixel font
 * a bunch of LED style meters
 * a small video playing through an LED filter
 * a time or countdown timer that spins in 3d
 *
 */

public class MovieUI implements NodeCreator {
    public static void main(String ... args) throws Exception {
        Core.init("jogl");
        Core.getImpl().init(new MovieUI());
    }

    public Node create() throws NoSuchMethodException {
        final GLUT glut = new GLUT();
        return new Node() {
            double angle;
            @Override
            public void draw(Gfx gfx) {
                JoglGfx g = (JoglGfx) gfx;
                GL2 gl = g.getGL();
                gl.glPushMatrix();
                //gl.glTranslated(200,200,0);

                g.switchToStandardProjection();
                gl.glMatrixMode(GL_MODELVIEW);
                //gl.glPushMatrix();
                gl.glLoadIdentity();
                gl.glMatrixMode(GL_PROJECTION);
                gl.glPushMatrix();
                gl.glLoadIdentity();

                //gl.glTranslated(200,200,0);
                gl.glColor3d(1.0,0,1.0);
                double s = 1.0/3.0;
                gl.glScaled(s,s,s);
                //glut.glutSolidRhombicDodecahedron();
                angle += 0.5;
                gl.glRotated(angle,0.5,1,0);
                //glut.glutWireTeapot(1);
                //glut.glutWireDodecahedron();
                //glut.glutWireIcosahedron();
                //glut.glutWireOctahedron();
                glut.glutWireTorus(0.5, 1.0, 15, 20);
                //glut.glutSolidTeapot(1);
              /*
                //for debugging
                gl.glColor3d(1.0,0,0);
                gl.glBegin(GL2.GL_QUADS);
                gl.glVertex2d(-0.5, -0.5);
                gl.glVertex2d(0.5,-0.5);
                gl.glVertex2d(0.5,0.5);
                gl.glVertex2d(-0.5,0.5);
                gl.glEnd();
                */
                gl.glPopMatrix();
                g.switchToStandardOrtho();

                //gl.glScaled(10,10,10);
                //glut.glutSolidRhombicDodecahedron();
                //gl.glScaled(1/10,1/10,1/10);
                //glut.glutWireDodecahedron();
                gl.glPopMatrix();
            }

            @Override
            public boolean contains(Point point) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
}
