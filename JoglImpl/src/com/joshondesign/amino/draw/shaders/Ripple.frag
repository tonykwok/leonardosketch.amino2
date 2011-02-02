uniform sampler2D tex;
uniform float centerX;
uniform float centerY;
void main() {
    float len = length(gl_FragCoord-vec4(centerX,centerY,0,0));
    float f = 10.0;
    float f2 = 20.0;

    vec2 coord = gl_TexCoord[0].st;
    coord.x += sin(len/f)/f2;
    coord.y += cos(len/f)/f2;
    gl_FragColor = texture2D(tex,coord);
}