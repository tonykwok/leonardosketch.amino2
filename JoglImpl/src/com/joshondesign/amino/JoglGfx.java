package com.joshondesign.amino;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;

import static javax.media.opengl.GL.GL_BLEND;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglGfx extends AbstractGfx {
    GL2 gl;
    private Shader copyBufferShader;
    private LinearGradientShader linearGradientShader;
    private RadialGradientShader radialGradientShader;

    public JoglGfx(JoglEventListener joglEventListener, GL2 gl) {
        this.gl = gl;

        copyBufferShader = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/CheckerBoard.frag")
        );
        linearGradientShader = new LinearGradientShader(gl);
        radialGradientShader = new RadialGradientShader(gl);
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

    public void fill(Path path, Fill fill, Buffer buffer, Rect clip) {
        gl.glEnable(GL_BLEND);
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);

        //basic transparency
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        //sort of like add
        //gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
        //ADD blending using alpha
        //gl.glBlendFunc(GL_ONE, GL_SRC_ALPHA);

        applyFill();
        if(path.geometry == null) {
            fillComplexPoly(path,true);
        } else {
            fillSavedGeometry(path);
        }
        unapplyFill();
        gl.glColor4f(1,1,1,1);
        gl.glDisable(GL_BLEND);
    }


    public void drawRect(Rect rect, Fill fill, Buffer buffer, Rect clip) {
        applyFill();
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2d(rect.x,rect.y);
        gl.glVertex2d(rect.x+rect.w,rect.y);
        gl.glVertex2d(rect.x+rect.w,rect.y+rect.h);
        gl.glVertex2d(rect.x,rect.y+rect.h);
        gl.glEnd();
    }

    public void draw(Path path, Fill fill, Buffer buffer, Rect clip) {
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        applyFill();
        if(path.geometry == null) {
            fillComplexPoly(path,false);
        } else {
            fillSavedGeometry(path);
        }
        gl.glColor4f(1,1,1,1);
        gl.glDisable(GL_BLEND);
    }

    private void applyFill() {
        if(this.getFill() instanceof Color) {
            Color color = (Color) this.getFill();
            gl.glColor4d(color.r, color.g, color.b, color.a);
        }
        if(getFill() instanceof LinearGradient) {
            linearGradientShader.enable(gl);
        }
        if(getFill() instanceof RadialGradient) {
            radialGradientShader.enable(gl,(RadialGradient)getFill());
        }
    }

    private void unapplyFill() {
        if(getFill() instanceof LinearGradient) {
            linearGradientShader.disable(gl);
        }
        if(getFill() instanceof RadialGradient) {
            radialGradientShader.disable(gl);
        }
    }

    private void fillSavedGeometry(Path path) {
        gl.glPushMatrix();
        gl.glBegin(path.geometryType);
        for(int i=0; i<path.geometry.length; i++) {
            gl.glVertex3dv(path.geometry[i],0);
        }
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void p(String s) {
        System.out.println(s);
    }



    @Override
    public void translate(double x, double y) {
        gl.glTranslated(x,y,0);
    }

    GLU glu = new GLU();

    private void fillComplexPoly(Path path, boolean filled) {
        //create a tesselator
        GLUtessellator tobj = GLU.gluNewTess();
        TessCallback tessCallback = new TessCallback(gl,glu,path);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_END, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, tessCallback);
        if(!filled) {
            GLU.gluTessProperty(tobj, GLU.GLU_TESS_BOUNDARY_ONLY, GL.GL_TRUE);
        }


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
        path.endPoints();
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
