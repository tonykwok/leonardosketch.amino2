package com.joshondesign.amino;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.nio.IntBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: joshmarinacci
 * Date: 1/19/11
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
class Java2DGfx extends AbstractGfx {
    private Graphics2D g;
    private static final boolean DEBUG = false;
    private JFrame frame;
    private JComponent comp;
    private BufferedImage img;
    private Buffer defaultBuffer;
    private Effect currentEffect;

    Java2DGfx(JFrame frame, Graphics2D g2, JComponent jComponent, BufferedImage img) {
        this.g = g2;
        this.frame = frame;
        this.comp = jComponent;
        this.img = img;
    }

    public Buffer createBuffer(int width, int height) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Buffer getDefaultBuffer() {
        return defaultBuffer;
    }

    public void copyBuffer(Rect sourceRect, Buffer sourceBuffer, Rect targetRect, Buffer targetBuffer, Blend blend) {
        if(sourceBuffer == null) throw new IllegalArgumentException("source buffer cannot be null");
        if(targetBuffer == null) throw new IllegalArgumentException("target buffer cannot be null");
        if(sourceBuffer == defaultBuffer) throw new IllegalArgumentException("source buffer cannot be the default buffer");

        Graphics2D tgfx = g;
        BufferedImage simg = ((J2DBuffer)sourceBuffer).img;

        if(targetBuffer != defaultBuffer){
            tgfx = ((J2DBuffer)targetBuffer).img.createGraphics();
        }

        tgfx.drawImage(simg,
                targetRect.x, targetRect.y, targetRect.x+targetRect.w, targetRect.y+targetRect.h,
                sourceRect.x, sourceRect.y, sourceRect.x+sourceRect.w, sourceRect.y+sourceRect.h,
                null
                );

        if(targetBuffer != defaultBuffer){
            tgfx.dispose();
        }

    }

    public void applyEffect(Rect sourceRect, Buffer sourceBuffer, Buffer targetBuffer, Effect effect) {
        if(sourceBuffer == null) throw new IllegalArgumentException("source buffer cannot be null");
        if(targetBuffer == null) throw new IllegalArgumentException("target buffer cannot be null");
        if(sourceBuffer == defaultBuffer) throw new IllegalArgumentException("source buffer cannot be the default buffer");
        BufferedImage simg = ((J2DBuffer)sourceBuffer).img;

        Graphics2D tgfx = g;
        if(targetBuffer != defaultBuffer){
            tgfx = ((J2DBuffer)targetBuffer).img.createGraphics();
        }

        /*
        if(effect instanceof BoxblurEffect) {
            float[]data = new float[3*3];
            for(int i=0; i<data.length; i++){
                data[i] = 1.0f/9.0f;
            }
            Kernel kernel = new Kernel(3,3,data);
            BufferedImageOp op = new ConvolveOp(kernel);
            tgfx.drawImage(simg,op,0,0);
        }*/

        if(effect instanceof DropshadowEffect) {


            float[]data = new float[3*3];
            for(int i=0; i<data.length; i++){
                data[i] = 1.0f/9.0f;
            }
            Kernel kernel = new Kernel(3,3,data);
            BufferedImageOp op = new ConvolveOp(kernel);
            tgfx.drawImage(simg,op,5,5);
            tgfx.drawImage(simg,0,0,null);
        }

        if(targetBuffer != defaultBuffer){
            tgfx.dispose();
        }
    }

    public void fillRect(Rect rect, Fill fill, Buffer buffer, Rect clip, Blend blend) {
        setFill(g,fill);
        g.fillRect(rect.x,rect.y,rect.w,rect.h);
    }

    public void fill(Path path, Fill fill, Buffer buffer, Rect clip, Blend blend) {
        Graphics2D gfx = g;
        if(!(buffer == null || buffer == defaultBuffer)) {
            gfx = ((J2DBuffer)buffer).img.createGraphics();
            gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setFill(gfx, fill);
        } else {
            setFill(g,fill);
        }
        java.awt.geom.Path2D awtPath = toAwt(path);
        if(clip != null) {
            java.awt.Shape oldClip = gfx.getClip();
            gfx.setClip((int)clip.x,(int)clip.y,(int)clip.w,(int)clip.h);
            gfx.fill(awtPath);
            gfx.setClip(oldClip);
        } else {
            gfx.fill(awtPath);
        }
        if(!(buffer == null || buffer == defaultBuffer)) {
            gfx.dispose();
        }
    }


    public void setFill(Graphics2D g, Fill fill) {
        dp("fillRect is ",fill);

        if(fill instanceof Color) {
            Color c = (Color)fill;
            g.setPaint(new java.awt.Color((float)c.r,(float)c.g,(float)c.b,(float)c.a));
            return;
        }

        if(fill instanceof LinearGradient) {
            LinearGradient grad = (LinearGradient)fill;
            java.awt.geom.Point2D start = toAwt(grad.start);
            java.awt.geom.Point2D end = toAwt(grad.end);

            float[] fracts = new float[grad.stops.size()];
            java.awt.Color[] colors = new java.awt.Color[grad.stops.size()];
            for(int i=0; i<grad.stops.size(); i++) {
                LinearGradient.Stop stop = grad.stops.get(i);
                fracts[i] = (float)stop.position;
                colors[i] = toAwt(stop.color);
            }
            LinearGradientPaint pt = new LinearGradientPaint(
                (float)start.getX(),(float)start.getY(),
                (float)end.getX(),(float)end.getY(),
                fracts,colors
                );
            g.setPaint(pt);
        }

        if(fill instanceof RadialGradient) {
            RadialGradient grad = (RadialGradient)fill;
            java.awt.geom.Point2D start = toAwt(grad.start);
            float[] fracts = new float[grad.stops.size()];
            java.awt.Color[] colors = new java.awt.Color[grad.stops.size()];
            for(int i=0; i<grad.stops.size(); i++) {
                RadialGradient.Stop stop = grad.stops.get(i);
                fracts[i] = (float)stop.position;
                colors[i] = toAwt(stop.color);
            }
            RadialGradientPaint pt = new RadialGradientPaint(start, (float)grad.radius, fracts, colors);
            g.setPaint(pt);
        }

        if(fill instanceof TextureFill) {
            TextureFill tex = (TextureFill) fill;
            g.setPaint(new TexturePaint(tex.img, new java.awt.Rectangle(0,0,tex.img.getWidth(),tex.img.getHeight())));
        }
    }

    public void setEffect(Effect effect) {
        this.currentEffect = effect;
    }

    public void translate(double x, double y) {
        g.translate(x,y);
    }

    public void rotate(double centerX, double centerY, double angle) {
        g.rotate(Math.toRadians(angle),centerX,centerY);
    }

    public void scale(double scaleX, double scaleY) {
        g.scale(scaleX,scaleY);
    }

    private void dp(Object... objects) {
        if(DEBUG) {
            StringBuffer sb = new StringBuffer();
            for(Object o : objects) {
                sb.append(""+o);
            }
            System.out.println(sb);
        }
    }

    /*
    //fillRect impl, fast path the ellipse
    public void fill(Shape shape) {
        dp("fillRect(shape)");
        if(currentEffect instanceof DropshadowEffect) {
            drawDropShadowShape(shape,g);
            return;
        }


        if(shape instanceof Ellipse) {
            Ellipse e = (Ellipse) shape;
            g.fillOval((int)e.x,(int)e.y,(int)e.w,(int)e.h);
            return;
        }


        fill(shape.toPath(), fill, null, (Rect)null, Blend.Normal);
    }
    */

    private void drawDropShadowShape(Shape shape, Graphics2D g) {
        Rect bounds = shape.getBounds();
        BufferedImage img = new BufferedImage(bounds.w +10, bounds.h +10,BufferedImage.TYPE_INT_ARGB);
        Graphics2D gfx = img.createGraphics();
        gfx.setPaint(java.awt.Color.BLACK);
        gfx.translate(-bounds.x+5,-bounds.y+5);
        gfx.fill(toAwt(shape.toPath()));
        gfx.dispose();
        BufferedImageOp op = createBlurOp();
        g.drawImage(img,op, bounds.x +5, bounds.y +5);
        g.fill(toAwt(shape.toPath()));
    }

    private BufferedImageOp createBlurOp() {
        float[]data = new float[3*3];
        for(int i=0; i<data.length; i++){
            data[i] = 1.0f/9.0f;
        }
        Kernel kernel = new Kernel(3,3,data);
        BufferedImageOp op = new ConvolveOp(kernel);
        return op;
    }



    //fills
    public void fillRect(Rect rect, Fill fill, Buffer buffer, Rect clip) {
        Graphics2D gfx = g;
        if(!(buffer == null || buffer == defaultBuffer)) {
            gfx = ((J2DBuffer)buffer).img.createGraphics();
            //gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            setFill(gfx, fill);
        } else {
            setFill(g,fill);
        }

        if(clip != null) {
            java.awt.Shape oldclip = gfx.getClip();
            gfx.setClip((int)clip.x,(int)clip.y,(int)clip.w,(int)clip.h);
            gfx.fillRect((int)rect.x,(int)rect.y,(int)rect.w,(int)rect.h);
            gfx.setClip(oldclip);
        } else {
            gfx.fillRect((int)rect.x,(int)rect.y,(int)rect.w,(int)rect.h);
        }

        if(!(buffer == null || buffer == defaultBuffer)) {
            gfx.dispose();
        }
    }


    public void fill(Path path, Fill fill, Buffer buffer, Path clip, Blend blend) {
        setFill(fill);
        java.awt.geom.Path2D awtPath = toAwt(path);
        //drawRect with soft clipping
        if(clip != null) {
            Rect bounds = clip.getBounds();
            BufferedImage img = new BufferedImage(bounds.w,bounds.h,BufferedImage.TYPE_INT_ARGB);
            java.awt.geom.Path2D clipPath = toAwt(clip);
            Graphics2D g2 = img.createGraphics();
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, bounds.w, bounds.h);
            g2.setComposite(AlphaComposite.Src);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(java.awt.Color.WHITE);
            g2.translate(-bounds.x, -bounds.y);
            g2.fill(clipPath);

            g2.setComposite(AlphaComposite.SrcAtop);
            setFill(g2, fill);
            g2.fill(awtPath);
            g2.translate(bounds.x, bounds.y);
            g2.dispose();
            g.drawImage(img,bounds.x,bounds.y,null);
        } else {
            g.fill(awtPath);
        }
    }

    public void drawRect(Rect rect, Fill fill, Buffer buffer, Rect clip, Blend blend) {
        setFill(fill);
        if(clip != null) {
            java.awt.Shape oldclip = g.getClip();
            g.setClip((int)clip.x,(int)clip.y,(int)clip.w,(int)clip.h);
            g.drawRect((int) rect.x, (int) rect.y, (int) rect.w, (int) rect.h);
            g.setClip(oldclip);
        } else {
            g.drawRect((int) rect.x, (int) rect.y, (int) rect.w, (int) rect.h);
        }

    }

    public void draw(Path path, Fill fill, Buffer buffer, Rect clip,  Blend blend) {
        setFill(fill);
        java.awt.geom.Path2D awtPath = toAwt(path);
        if(clip != null) {
            java.awt.Shape oldClip = g.getClip();
            g.setClip(clip.x, clip.y, clip.w, clip.h);
            g.draw(awtPath);
            g.setClip(oldClip);
        } else {
            g.draw(awtPath);
        }
    }

    public void draw(Path path, Fill fill, Buffer buffer, Path clip, Blend blend) {

    }

    public void drawIntBuffer(IntBuffer lastBuffer, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public int readbackPixel(Buffer buffer, int x, int y) {
        int rgb = img.getRGB(x,y);
        return rgb;
    }

    public RadialGradient.RadialGradientBuilder buildRadialGradient() {
        return new RadialGradient.RadialGradientBuilder();
    }

    public void fill(Rect rect, Buffer sourceBuffer, Rect subRect, Buffer targetBuffer) {
    }

    static void p(String s) {
        System.out.println(s);
    }
    private java.awt.Color toAwt(Color color) {
        return new java.awt.Color((float)color.r,(float)color.g,(float)color.b);
    }
    private java.awt.geom.Point2D toAwt(Point pt) {
        return new java.awt.geom.Point2D.Double(pt.x,pt.y);
    }

    private java.awt.geom.Path2D toAwt(Path path) {
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

    private class J2DBuffer extends Buffer {
        private BufferedImage img;

        public J2DBuffer(int width, int height) {
            super();
            this.img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        }
    }
}
