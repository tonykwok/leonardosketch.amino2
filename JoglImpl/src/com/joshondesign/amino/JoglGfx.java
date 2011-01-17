package com.joshondesign.amino;

import javax.media.opengl.GL2;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglGfx extends AbstractGfx {
    GL2 gl;

    public JoglGfx(JoglEventListener joglEventListener, GL2 gl) {
        this.gl = gl;
    }

    public void fillRect(Rect rect, Fill fill, Buffer buffer, Rect clip) {
        applyFill();
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(rect.x,rect.y);
        gl.glVertex2d(rect.x+rect.w,rect.y);
        gl.glVertex2d(rect.x+rect.w,rect.y+rect.h);
        gl.glVertex2d(rect.x,rect.y+rect.h);
        gl.glEnd();
    }

    private void applyFill() {
        if(this.getFill() instanceof Color) {
            Color color = (Color) this.getFill();
            gl.glColor3d(color.r,color.g,color.b);
        }

    }

    public void fill(Path path, Fill fill, Buffer buffer, Rect clip) {
        gl.glColor3d(1.0,1.0,0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(-100, -100);
        gl.glVertex2d(100,-100);
        gl.glVertex2d(100,100);
        gl.glVertex2d(-100,100);
        gl.glEnd();
    }


    public void draw(Shape shape) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
