uniform sampler2D tex;


const float d1 = 1.0/25.0;
const float d2 = 2.0/25.0;
const float d3 = 3.0/25.0;
const float d4 = 4.0/25.0;
const float d5 = 5.0/25.0;
const float blurSize = 3.0;

uniform vec4 color;
uniform float cx1;
uniform float cx2;
uniform float cx3;

void main() {
    vec4 sum = vec4(0.0);
    vec2 vTexCoord = gl_TexCoord[0].st;
    vec4 color = texture2D(tex,vTexCoord);

    vec4 c1 =  texture2D(tex, vec2(gl_TexCoord[0].s-0.005, gl_TexCoord[0].t-0.005));
    vec4 c2 =  texture2D(tex, vec2(gl_TexCoord[0].s+0.005, gl_TexCoord[0].t-0.005));
    vec4 c3 =  texture2D(tex, vec2(gl_TexCoord[0].s-0.005, gl_TexCoord[0].t+0.005));
    vec4 c4 =  texture2D(tex, vec2(gl_TexCoord[0].s+0.005, gl_TexCoord[0].t+0.005));
    float al = (c1.a + c2.a + c3.a + c4.a)/4.0;
    //gl_FragColor = vec4(color.r,color.g,color.b,al);
    gl_FragColor = vec4(cx1,cx2,cx3,al);

}