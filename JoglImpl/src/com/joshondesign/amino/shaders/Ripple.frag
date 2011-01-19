uniform sampler2D tex;
void main() {
    float len = length(gl_FragCoord-vec4(200.0,250.0,0,0));
    float f = 8.0;
    float f2 = 30.0;

    vec2 coord = gl_TexCoord[0].st;
    coord.x += sin(len/f)/f2;
    coord.y += cos(len/f)/f2;
    gl_FragColor = texture2D(tex,coord);
}