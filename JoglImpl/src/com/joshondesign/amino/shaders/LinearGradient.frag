uniform sampler2D tex;

uniform float textureWidth;
uniform float textureHeight;
uniform float screenHeight;
uniform vec2 startPoint;
uniform vec2 endPoint;


void main() {
    //simple linear gradient
    //calculate the x/y in image space from 0 to 1
    float imagex = gl_FragCoord.x/textureWidth;
    float imagey = gl_FragCoord.y/textureHeight;

    //calculate the start and end points of the gradient in image space from 0 to 1

    float xs = startPoint.x/textureWidth;
    float xe = endPoint.x/textureWidth;
    float ys = startPoint.y/textureHeight;
    float ye = endPoint.y/textureHeight;

    float xd = xe-xs;
    float yd = ye-ys;
    float len = 1.0/(xd*xd+yd*yd);

    //calc the x position into the gradient line
    //plus the y position into the gradient line
    float term1 = (imagex-xs)*xd;
    //float term1 = ((gl_FragCoord.x-startPoint.x) * (endPoint.x-startPoint.x))/textureWidth;
    //float term2 = (imagey-ys)*yd;
    //float term2 = ((gl_FragCoord.y-startPoint.y) * (endPoint.y-startPoint.y))/textureHeight;
    float term2 = (gl_FragCoord.y-startPoint.y)/textureHeight * ((endPoint.y - startPoint.y)/textureHeight);
    //float term2 =   ((gl_FragCoord.y-startPoint.y) * (endPoint.y - startPoint.y))/textureHeight;
    float gradPos = (term1+term2)*len;

    gl_FragColor = texture2D(tex,vec2(0,gradPos));

            /*
    ix = x/w;
    iy = y/h;
    xs = sx/w;
    xe = ex/w;
    ys = sy/h;
    ye = ey/h;

    xd = xe-xs; ex/w - sx/w;
    yd = ye-ys;


    p = ((ix-xs) * xd +          (iy-ys)*yd)) * len;
        (x/w-sx/w) * (ex/w-sx/w);
        (x-sx)/w * (ex-sx)/w;
        ((x-sx)*(ex-sx))/w

        (iy-ys)*yd
        (y/h - sy/h)* (ye-ys)
        (y-sy)/h  * (ey/h-sy/h)
        (y-sy)/h  * (ey-sy)/h
        ((y-sy)*(ey-sy))/h

    */

}