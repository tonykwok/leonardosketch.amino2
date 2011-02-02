uniform sampler2D tex;

uniform float screenHeight;
uniform vec2 textureSize;
void main() {
    vec2 coord = gl_TexCoord[0].st;
    vec2 pcoord = vec2(
        gl_FragCoord.x/textureSize.x,
        screenHeight-gl_FragCoord.y/textureSize.y);
    vec4 color = texture2D(tex,pcoord);
    gl_FragColor = color;
}