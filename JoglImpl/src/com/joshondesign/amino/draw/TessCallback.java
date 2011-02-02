package com.joshondesign.amino.draw;

import com.joshondesign.amino.draw.Path;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellatorCallback;

/**
* Created by IntelliJ IDEA.
* User: joshmarinacci
* Date: 1/12/11
* Time: 3:12 PM
* To change this template use File | Settings | File Templates.
*/
class TessCallback implements GLUtessellatorCallback {
    private GL2 gl;
    private GLU glu;
    private Path targetPath;

    TessCallback(GL2 gl, GLU glu, Path path) {
        this.gl = gl;
        this.glu = glu;
        this.targetPath = path;
    }

    public void begin(int type) {
        //p("beginning type: " + type);
        /*
        switch(type) {
            case GL.GL_TRIANGLE_FAN: p("   triangle fan"); break;
            case GL.GL_LINES: p("   lines"); break;
            case GL2.GL_POLYGON: p("   polygon"); break;
            case GL.GL_TRIANGLES: p("   GL_TRIANGLES"); break;
        }
        */
        //p("lines = " + GL_LINES + " " + GL_LINE + " " + GL_POLYGON + " "
        // + GL_TRIANGLE_FAN + " " + GL_TRIANGLE_STRIP + " " + GL_TRIANGLES);

        gl.glBegin(type);
        if(targetPath != null) {
            targetPath.geometryType = type;
        }
    }

    public void beginData(int i, Object o) {
        p("begin data");
    }

    public void edgeFlag(boolean b) {
        p("edge flag");
    }

    public void edgeFlagData(boolean b, Object o) {
        p("edge flag data");
    }

    public void vertex(Object vertexData) {
        //p("vertex");
        double[] pointer;
        if (vertexData instanceof double[])
        {
            pointer = (double[]) vertexData;
            //p("vd = " + pointer[0] + " " + pointer[1] + " " + pointer[2]);
            if (pointer.length == 6) {
                //p("adding color");
                gl.glColor3dv(pointer, 3);
            }
            gl.glVertex3dv(pointer, 0);
            if(targetPath != null) {
                targetPath.addPoint(pointer);
            }
        }
    }

    private void p(String vertex) {
        System.out.println(vertex);
    }

    public void vertexData(Object o, Object o1) {
        p("vertex data");
    }

    public void end() {
        //p("glEnd");
        gl.glEnd();
    }

    public void endData(Object o) {
        p("end data");
    }

    public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {
        p("combine");

        double[] vertex = new double[3];

        vertex[0] = coords[0];
        vertex[1] = coords[1];
        vertex[2] = coords[2];
        outData[0] = vertex;


        /*double[] vertex = new double[6];
        int i;

        vertex[0] = coords[0];
        vertex[1] = coords[1];
        vertex[2] = coords[2];
        for (i = 3; i < 6/* 7OutOfBounds from C! *//*;i++)
            vertex[i] = weight[0] //
                    * ((double[]) data[0])[i] + weight[1]
                    * ((double[]) data[1])[i] + weight[2]
                    * ((double[]) data[2])[i] + weight[3]
                    * ((double[]) data[3])[i];
        outData[0] = vertex;*/
    }

    public void combineData(double[] doubles, Object[] objects, float[] floats, Object[] objects1, Object o) {
        p("combine data");
    }

    public void error(int errnum) {
        String estring;

        estring = glu.gluErrorString(errnum);
        System.err.println("Tessellation Error: " + estring);
        System.exit(0);
    }

    public void errorData(int i, Object o) {
        p("error data");
    }
}
