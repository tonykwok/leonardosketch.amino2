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

    vec4 startColor = vec4(1,0,0,1);
    vec4 endColor = vec4(0,1,0,1);

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
    float gradPos = ((imagex-xs)*xd+(imagey-ys)*yd)*len;

    gl_FragColor = texture2D(tex,vec2(0,gradPos));

    //gl_FragColor = mix(startColor,endColor,gradPos);

}