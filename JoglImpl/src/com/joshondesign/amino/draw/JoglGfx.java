package com.joshondesign.amino.draw;

import com.joshondesign.amino.JoglEventListener;
import com.joshondesign.amino.draw.effects.DropshadowEffect;
import com.joshondesign.amino.draw.effects.Effect;
import com.joshondesign.amino.draw.effects.RippleEffect;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import java.awt.geom.AffineTransform;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static javax.media.opengl.GL.*;
import static javax.media.opengl.GL2.GL_QUADS;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/16/11
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class JoglGfx extends AbstractGfx {
    private static final boolean BUFFER_DEBUG = false;




    public GL2 gl;
    private Shader copyBufferShader;
    private LinearGradientShader linearGradientShader;
    private RadialGradientShader radialGradientShader;
    private TextureShader textureShader;
    private Buffer defaultBuffer;
    private JoglEventListener master;
    private ShadowBlurShader shadowBlurShader;
    private RippleShader rippleShader;
    private Texture movieTex;
    private BufferedImage movieBI;
    private TextureData movieTexData;
    private Shader ripple2;

    public JoglGfx(JoglEventListener master, GL2 gl) {
        this.master = master;
        this.gl = gl;

        copyBufferShader = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/CopyBuffer.frag")
        );
        ripple2 = Shader.load(gl,
                JoglGfx.class.getResource("shaders/PassThrough.vert"),
                JoglGfx.class.getResource("shaders/Ripple.frag")
        );
        rippleShader = new RippleShader(gl);
        linearGradientShader = new LinearGradientShader(gl);
        radialGradientShader = new RadialGradientShader(gl);
        textureShader = new TextureShader(gl);
        shadowBlurShader = new ShadowBlurShader(gl);
        defaultBuffer = new Buffer();
    }














    /* ================ buffer stuff ================= */

    public Buffer createBuffer(int width, int height) {
        p("buffer created: " + width + " " + height);
        FrameBufferObject buffer1 = FrameBufferObject.create(gl, width, height, false);
        buffer1.bind(gl);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        buffer1.clear(gl);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
        return buffer1;
    }

    public Buffer getDefaultBuffer() {
        return defaultBuffer;
    }

    public void copyBuffer(Rect sourceRect, Buffer sourceBuffer, Rect targetRect, Buffer targetBuffer, Blend blend) {
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glPushMatrix();
        gl.glTranslated(targetRect.x,targetRect.y,0);
        renderBufferWithShader(gl, copyBufferShader, (FrameBufferObject) sourceBuffer, targetBuffer);
        gl.glPopMatrix();
    }

    private void renderBufferWithShader(GL2 gl, Shader shader, FrameBufferObject source, Buffer target) {
        //p("rendering from a buffer with a shader, source = " + source + " target = " + target);


        //set the render target, if needed
        if(target != null && target != getDefaultBuffer()) {
            //p("rendering to a buffer");
            gl.glPushMatrix();
            gl.glTranslated(0,480-24,0);
            gl.glScaled(1,-1,1);
            FrameBufferObject tbuf = (FrameBufferObject) target;
            tbuf.bind(gl);
        }

        //turn on the shader
        shader.use(gl);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, source.colorId);
        shader.setIntParameter(gl, "tex0", 0);

        //draw
        gl.glBegin(GL_QUADS);
            gl.glTexCoord2f(0f, 0f); gl.glVertex2f(0, 0);
            gl.glTexCoord2f(0f, 1f); gl.glVertex2f(0, source.getHeight());
            gl.glTexCoord2f(1f, 1f); gl.glVertex2f( source.getWidth(), source.getHeight() );
            gl.glTexCoord2f( 1f, 0f ); gl.glVertex2f(source.getWidth(), 0);
        gl.glEnd();


        //turn off the shader again
        gl.glUseProgramObjectARB(0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D, 0);


        ///debugging
        if(BUFFER_DEBUG) {
            Rect rect = Rect.build(0,0,100,100);
            //fill with mint green
            float r = 0; float g = 1.0f; float b = 0.5f; float a = 0.2f;
            gl.glBegin(GL_QUADS);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x, rect.y);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x, rect.y+rect.h);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x+rect.w, rect.y+rect.h);
                gl.glColor4f(r,g,b,a); gl.glVertex2f(rect.x+rect.w, rect.y);
            gl.glEnd();
        }

        //restore the render target, if needed
        if(target != null && target != getDefaultBuffer()) {
            gl.glPopMatrix();
            gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
            //gl.glViewport(0, 0, joglGFX.width, joglGFX.height);
        }


    }


    public void applyEffect(Rect sourceRect, Buffer sourceBuffer, Buffer targetBuffer, Effect effect) {
        gl.glPushMatrix();
        gl.glTranslated(0,0,0);

        //if(effect instanceof BoxblurEffect) {
        //    renderBufferWithShader(gl, boxBlurShader, (FrameBufferObject) sourceBuffer, targetBuffer);
        //}
        FrameBufferObject source = (FrameBufferObject) sourceBuffer;
        if(effect instanceof DropshadowEffect) {
            DropshadowEffect ds = (DropshadowEffect) effect;
            gl.glTranslated(ds.x,-ds.y,0);
            //render blur into buffer first
            shadowBlurShader.setEffect(ds);
            renderBufferWithShader(gl, shadowBlurShader, source, targetBuffer);
            //render original shape into buffer next
            gl.glTranslated(-ds.x, ds.y, 0);
            renderBufferWithShader(gl, copyBufferShader, source, targetBuffer);
        }

        if(effect instanceof RippleEffect) {
            RippleEffect re = (RippleEffect) effect;
            renderBufferWithShader(gl, rippleShader, source, targetBuffer);
        }

        //if no effect, just copy from source to target buffer
        if(effect == null) {
            renderBufferWithShader(gl, copyBufferShader, source, targetBuffer);
        }
        gl.glPopMatrix();
    }














    /* ======== Basic Fill Routines ============ */


    public void fillRect(Rect rect, Fill fill, Buffer buffer, Rect clip, Blend blend) {
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        applyFill(fill);
        if(fill instanceof TextureFill) {
            BufferedImage source = ((TextureFill)fill).img;
            gl.glBegin(GL_QUADS);
            gl.glTexCoord2f(0f, 0f);
            gl.glVertex2d(rect.x,rect.y);
            gl.glTexCoord2f(0f, 1f);
            gl.glVertex2d(rect.x+rect.w,rect.y);
            gl.glTexCoord2f(1f, 1f);
            gl.glVertex2d(rect.x+rect.w,rect.y+rect.h);
            gl.glTexCoord2f(1f, 0f );
            gl.glVertex2d(rect.x,rect.y+rect.h);
            gl.glEnd();
        } else {
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(rect.x,rect.y);
            gl.glVertex2d(rect.x+rect.w,rect.y);
            gl.glVertex2d(rect.x+rect.w,rect.y+rect.h);
            gl.glVertex2d(rect.x,rect.y+rect.h);
            gl.glEnd();
        }
        unapplyFill();
        gl.glColor4f(1,1,1,1);
    }

    public void fill(Path path, Fill fill, Buffer buffer, Path clip, Blend blend) {
        //p("doing a standard path fill:" + path + " " + fill + " ");
        gl.glEnable(GL_BLEND);
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);

        switch(blend) {
            case Normal:
                //basic transparency
                gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
                break;
            case Add:
                //sort of like add
                //gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
                //ADD blending using alpha
                gl.glBlendFunc(GL.GL_ONE, GL.GL_SRC_ALPHA);
                break;
            case SrcIn:
                gl.glBlendFunc(GL.GL_DST_ALPHA, GL.GL_ZERO);
                break;
        }


        applyFill(fill);

        if(buffer != null && buffer != defaultBuffer) {
        //render the shape to a buffer
            FrameBufferObject buf = (FrameBufferObject) buffer;
            gl.glPushMatrix();
            gl.glTranslated(0,480-24,0);
            gl.glScaled(1,-1,1);
            buf.bind(gl);
            fillPath(path);
            gl.glPopMatrix();
            //reset
            gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
            gl.glViewport(0, 0, master.width, master.height);
        } else {
            //render shape to the screen
            if(path.geometry == null) {
                fillComplexPoly(path,true);
            } else {
                fillSavedGeometry(path);
            }
        }

        unapplyFill();
        gl.glColor4f(1,1,1,1);
    }

    private void fillPath(Path path) {
        //draw
        if(path.geometry == null) {
            fillComplexPoly(path,true);
        } else {
            fillSavedGeometry(path);
        }
    }


    public void drawRect(Rect rect, Fill fill, Buffer buffer, Rect clip, Blend blend) {
        applyFill(fill);
        gl.glBegin(GL2.GL_LINE_LOOP);
        gl.glVertex2d(rect.x, rect.y);
        gl.glVertex2d(rect.x+rect.w,rect.y);
        gl.glVertex2d(rect.x+rect.w,rect.y+rect.h);
        gl.glVertex2d(rect.x,rect.y+rect.h);
        gl.glEnd();
    }

    public void draw(Path path, Fill fill, Buffer buffer, Path clip, Blend blend) {
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        applyFill(fill);
        if(path.geometry == null) {
            if(path.isClosed()) {
                fillComplexPoly(path,false);
            } else {
                drawOpenPoly(path);
            }
        } else {
            fillSavedGeometry(path);
        }
        gl.glColor4f(1,1,1,1);
    }

    private void applyFill(Fill fill) {
        if(fill instanceof Color) {
            Color color = (Color) fill;
            gl.glColor4d(color.r, color.g, color.b, color.a);
        }
        if(fill instanceof LinearGradient) {
            linearGradientShader.enable(gl, (LinearGradient) fill);
        }
        if(fill instanceof RadialGradient) {
            radialGradientShader.enable(gl,(RadialGradient)fill);
        }
        if(fill instanceof TextureFill) {
            textureShader.enable(gl,(TextureFill)fill);
        }
    }

    private void unapplyFill() {
        if(getFill() instanceof LinearGradient) {
            linearGradientShader.disable(gl);
        }
        if(getFill() instanceof RadialGradient) {
            radialGradientShader.disable(gl);
        }
        if(getFill() instanceof TextureFill) {
            textureShader.disable(gl);
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

    @Override
    public void rotate(double centerX, double centerY, double angle) {
        gl.glTranslated(centerX,centerY,0);
        gl.glRotated(angle, 0, 0, 1);
        gl.glTranslated(-centerX, -centerY, 0);
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

    private void drawOpenPoly(Path path) {
        Path2D pth = toAwt(path);
        PathIterator it = pth.getPathIterator(new AffineTransform());
        it = new FlatteningPathIterator(it,0.5);
        boolean closed = false;
        double[] coords = new double[6];
        gl.glBegin(GL2.GL_LINE_STRIP);

        while(true) {
            if(it.isDone()) break;
            int type = it.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    gl.glVertex2d(coords[0],coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    gl.glVertex2d(coords[0],coords[1]);
                    break;
                case PathIterator.SEG_CLOSE:
                    break;
            }
            it.next();
        }
        gl.glEnd();
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

    public void drawIntBuffer(IntBuffer intBuffer, int width, int height) {
        //create a texture if needed
        if(movieTex == null) {
            movieBI = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            movieTexData = AWTTextureIO.newTextureData(movieBI, false);
            movieTex = new Texture(movieTexData);
        }

        //get an int array for the buffered image
        DataBufferInt db = (DataBufferInt) movieBI.getRaster().getDataBuffer();
        int[] data = db.getData();

        //copy the pixel data into the buffered image's data.
        intBuffer.get(data);
        //rewind the intbuffer so it can be used again
        intBuffer.rewind();
        //update the moview
        movieTex.updateImage(movieTexData);


        //turn on the shader
        copyBufferShader.use(gl);
        //rippleShader.use(gl);
        movieTex.enable();
        movieTex.bind();
        //rippleShader.setIntParameter(gl, "tex0", 0);
        copyBufferShader.setIntParameter(gl, "tex0",0);

        gl.glBegin( GL_QUADS );
            gl.glTexCoord2f(0f, 0f); gl.glVertex2f(0, 0);
            gl.glTexCoord2f( 0f, 1f ); gl.glVertex2f(0, movieTex.getImageHeight());
            gl.glTexCoord2f( 1f, 1f ); gl.glVertex2f( movieTex.getImageWidth(), movieTex.getImageHeight() );
            gl.glTexCoord2f( 1f, 0f ); gl.glVertex2f(movieTex.getImageWidth(), 0);
        gl.glEnd();
        movieTex.disable();
        gl.glUseProgramObjectARB(0);

    }

    public BulkTexture createBulkTexture(int width, int height) {
        p("created a bulk texture: " + width + " " + height);
        JOGLBulkTexture tex = new JOGLBulkTexture(width,height);
        return tex;
    }

    public void drawBulkTexture(BulkTexture texture) {
        //turn on the shader
        //p("effect = " + stack.peek().effect);

        JOGLBulkTexture bulk = (JOGLBulkTexture) texture;
        bulk.tex.enable();
        bulk.tex.bind();

        //Shader shader = null;//copyBufferShader;
        if(stack.peek().effect instanceof RippleEffect) {
            rippleShader.setEffect((RippleEffect) stack.peek().effect);
            rippleShader.use(gl);
            //shader = rippleShader;
            //p("using ripple");
        } else {
            //shader = copyBufferShader;
            //shader.use(gl);
            copyBufferShader.use(gl);
        }
        //rippleShader.use(gl);
        //rippleShader.setIntParameter(gl, "tex0", 0);


        //rippleShader.setEffect((RippleEffect) stack.peek().effect);
        //rippleShader.use(gl);

        gl.glBegin( GL_QUADS );
            gl.glTexCoord2f(0f, 0f); gl.glVertex2f(0, 0);
            gl.glTexCoord2f( 0f, 1f ); gl.glVertex2f(0, bulk.tex.getImageHeight());
            gl.glTexCoord2f( 1f, 1f ); gl.glVertex2f(bulk.tex.getImageWidth(), bulk.tex.getImageHeight());
            gl.glTexCoord2f(1f, 0f); gl.glVertex2f(bulk.tex.getImageWidth(), 0);
        gl.glEnd();
        bulk.tex.disable();
        gl.glUseProgramObjectARB(0);
    }

    public void drawImage(BufferedImage buf, int x, int y) {
        Texture tex = AWTTextureIO.newTexture(buf, false);
        tex.enable();
        tex.bind();
        //for debugging
        /*
        gl.glColor3d(1.0,0,0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(-100, -100);
        gl.glVertex2d(100,-100);
        gl.glVertex2d(100,100);
        gl.glVertex2d(-100,100);
        gl.glEnd();*/
        gl.glBegin( GL_QUADS );
            gl.glTexCoord2f(0f, 0f); gl.glVertex2f(0, 0);
            gl.glTexCoord2f( 0f, 1f ); gl.glVertex2f( 0, buf.getHeight() );
            gl.glTexCoord2f( 1f, 1f ); gl.glVertex2f( buf.getWidth(), buf.getHeight() );
            gl.glTexCoord2f( 1f, 0f ); gl.glVertex2f(buf.getWidth(), 0);
        gl.glEnd();
        tex.disable();
        tex.destroy(gl);
    }

    int count = 0;
    public int readbackPixel(int px, int py) {
        px++;
        py++;
        py = 480-22-py;
        int w = 640;
        int h = 480;


        gl.glReadBuffer(GL.GL_BACK); // or GL.GL_BACK

        ByteBuffer glBB = ByteBuffer.allocate(3 * w * h);
        gl.glReadPixels(0, 0, w, h, GL2.GL_BGR, GL.GL_BYTE, glBB);

        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int b = 2 * glBB.get();
                int g = 2 * glBB.get();
                int r = 2 * glBB.get();
                int val = (r << 16) | (g << 8) | b | 0xFF000000;
                bi.setRGB(x,y,val);
            }
        }

        if(false) {
            try {
                ImageIO.write(bi, "png", new FileOutputStream("testblah" + count + ".png"));
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bi.getRGB(px,py);
    }

    public GL2 getGL() {
        return gl;
    }

    public void switchToStandardProjection() {
        JoglUtil.viewPerspective(gl);
    }

    public void switchToStandardOrtho() {
        JoglUtil.viewOrtho(gl, 640, 480);
    }

    private class JOGLBulkTexture extends BulkTexture {
        public BufferedImage img;
        public TextureData data;
        public Texture tex;

        private JOGLBulkTexture(int width, int height) {
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            data = AWTTextureIO.newTextureData(img, false);
            tex = new Texture(data);
        }

        @Override
        public void update(IntBuffer intBuffer) {
            //get an int array for the buffered image
            DataBufferInt db = (DataBufferInt) img.getRaster().getDataBuffer();
            int[] intData = db.getData();

            //copy the pixel data into the buffered image's data.
            intBuffer.get(intData);
            //rewind the intbuffer so it can be used again
            intBuffer.rewind();
            //update the moview
            tex.updateImage(this.data);
        }
    }
}
