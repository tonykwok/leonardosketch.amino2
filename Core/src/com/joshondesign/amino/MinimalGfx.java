package com.joshondesign.amino;

/*

what is the minimal 2D api?
state push/pop?
affine transforms
render to a buffer so we can do effects
fillRect a path (drawRect a path is a special case?)
     with a texture, linear grad, radial grad, conical grad, flat color
fillRect a (pixel aligned?) rect with the same
fillRect a quad with a sub texture (is just just part of filling a path with a texture?)
drawing using a clip


the larger api will delegate to the minimal api, but you can plug in delegates for each to take accelerated paths

all geometry / path classes are immutable so they can be cached on the GPU side.
all fills must be immutable so they can be cached on the GPU side
classes for
    fillcolor
    lineargradient
    radialgradient
    conicalgradient
    filltexture
    path?
    quad/rect

*/
public interface MinimalGfx {
    //setTransform management
    //public void setTransform(Transform trans);

    //buffer management, effects & blending
    public Buffer createBuffer(int width, int height);
    public Buffer getDefaultBuffer();
    public void copyBuffer(Rect sourceRect, Buffer sourceBuffer, Rect targetRect, Buffer targetBuffer, Blend blend);
    public void applyEffect(Rect sourceRect, Buffer sourceBuffer, Buffer targetBuffer, Effect effect);

    //fills
    public void fillRect(Rect rect, Fill fill, Buffer buffer, Rect clip, Blend blend);
    public void fill(Path path, Fill fill, Buffer buffer, Path clip, Blend blend);
    //public void fill(Path path, Fill fill, Buffer buffer, Path clip, Blend blend);

    //draws
    public void drawRect(Rect rect, Fill fill, Buffer buffer, Rect clip, Blend blend);
    //public void draw(Path path, Fill fill, Buffer buffer, Rect clip, Blend blend);
    public void draw(Path path, Fill fill, Buffer buffer, Path clip, Blend blend);

    //testing
    //public int readbackPixel(Buffer buffer, int x, int y);

    //value creation
    //public RadialGradient.RadialGradientBuilder buildRadialGradient();

}





