package com.joshondesign.amino;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_SRC_ALPHA;

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
            gl.glColor4d(color.r,color.g,color.b,color.a);
        }

    }

    public void fill(Path path, Fill fill, Buffer buffer, Rect clip) {
        gl.glEnable(GL_BLEND);
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);

        //basic transparency
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        //sort of like add
        //gl.glBlendFunc(GL_ONE, GL_ONE);
        //ADD blending using alpha
        gl.glBlendFunc(GL_ONE, GL_SRC_ALPHA);

        applyFill();
        fillComplexPoly(gl,path);
        /*
        gl.glColor3d(1.0,1.0,0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(-100, -100);
        gl.glVertex2d(100,-100);
        gl.glVertex2d(100,100);
        gl.glVertex2d(-100,100);
        gl.glEnd();
        */
        gl.glColor4f(1,1,1,1);
        gl.glDisable(GL_BLEND);
    }


    public void draw(Shape shape) {

    }


    GLU glu = new GLU();

    void fillComplexPoly(GL2 gl, Path path) {
        //create a tesselator
        GLUtessellator tobj = GLU.gluNewTess();
        TessCallback tessCallback = new TessCallback(gl,glu);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_END, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, tessCallback);


        this.gl.glPushMatrix();

        java.awt.Shape glyphShape = toAwt(path);
        PathIterator it = glyphShape.getPathIterator(new AffineTransform());
        it = new FlatteningPathIterator(it,0.5);
        double[] coords = new double[6];
        double px = 0;
        double py = 0;
        double mx = 0;
        double my = 0;
        int pointCount = 0;
        boolean useTess = false;
        GLU.gluTessBeginPolygon(tobj,null);
        while(true) {
            if(it.isDone()) break;
            int type = it.currentSegment(coords);
            //p(type, coords);
            if(type == PathIterator.SEG_MOVETO) {
                px = coords[0];
                py = coords[1];
                mx = px;
                my = py;
                GLU.gluTessBeginContour(tobj);
                double[] point = new double[3];
                point[0] = mx;
                point[1] = my;
                point[2] = 0;
                GLU.gluTessVertex(tobj, point, 0, point);
            }
            if(type == PathIterator.SEG_LINETO) {
                double[] point = new double[3];
                point[0] = coords[0];
                point[1] = coords[1];
                point[2] = 0;
                GLU.gluTessVertex(tobj, point, 0, point);
                px = coords[0];
                py = coords[1];
            }
            if(type == PathIterator.SEG_CLOSE) {
                double[] point = new double[3];
                point[0] = mx;
                point[1] = my;
                point[2] = 0;
                GLU.gluTessVertex(tobj, point, 0, point);
                GLU.gluTessEndContour(tobj);
            }
            pointCount++;
            it.next();
        }
        GLU.gluTessEndPolygon(tobj);
        //end tesselation
        GLU.gluDeleteTess(tobj);
        gl.glPopMatrix();
    }

    private static java.awt.geom.Path2D toAwt(Path path) {
        java.awt.geom.Path2D p = new java.awt.geom.Path2D.Double();
        for(Path.Segment s : path) {
            switch(s.type) {
                case MoveTo:
                    p.moveTo(s.x,s.y);
                    break;
                case LineTo:
                    p.lineTo(s.x,s.y);
                    break;
                case CloseTo:
                    p.closePath();
                    break;
                case CurveTo:
                    p.curveTo(s.cx1, s.cy1, s.cx2, s.cy2, s.x2, s.y2);
                    break;
            }
        }
        return p;
    }
}
