uniform sampler2D tex;

uniform float translateX;
uniform float translateY;
uniform float centerX;
uniform float centerY;
uniform float gradientRadius;
uniform float screenHeight;

//simple radial gradient

void main() {

    //calc the real center of the gradient in screen coords
    vec2 gradientCenter = vec2(translateX+centerX,screenHeight-translateY-centerY);

    //get distance from gradient center
    float len = length(gl_FragCoord.xy - gradientCenter);

    //divide by radius to get value from 0-1
    float gradientCoeff = len / gradientRadius;

    //clamp to 1.0 if too far
    if(gradientCoeff > 1.0) {
        gradientCoeff = 1.0;
    }
    //look up the color from the gradient texture
    gl_FragColor = texture2D(tex,vec2(0,gradientCoeff));

}