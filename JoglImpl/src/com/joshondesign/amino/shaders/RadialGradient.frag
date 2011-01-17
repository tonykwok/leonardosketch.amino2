uniform float translateX;
uniform float translateY;
uniform float centerX;
uniform float centerY;
uniform float gradientRadius;
uniform float screenHeight;
uniform vec4 startColor;// = vec4(0,0,0,1);
uniform vec4 endColor;// = vec4(0,1,1,1);

//simple radial gradient
void main() {


    vec2 gradientCenter = vec2(translateX+centerX,screenHeight-translateY-centerY);
    //vec4 startColor = vec4(0,0,0,1);
    //vec4 endColor = vec4(0,1,1,1);


    //get distance from gradient center
    float len = length(gl_FragCoord.xy - gradientCenter);

    //divide by radius to get value from 0-1
    float gradientCoeff = len / gradientRadius;

    //find the mix between the start and end colors
    gl_FragColor = mix(startColor,endColor,gradientCoeff);
}