
void main() {
    //simple radial gradient
    vec2 gradientCenter = vec2(300.0,300.0);
    float gradientRadius = 150.0;
    vec4 startColor = vec4(0,0,0,1);
    vec4 endColor = vec4(1,1,1,1);

    //get distance from gradient center
    float len = length(gl_FragCoord.xy - gradientCenter);
    //divide by radius to get value from 0-1
    float gradientCoeff = len / gradientRadius;
    //find the mix between the start and end colors
    gl_FragColor = mix(startColor,endColor,gradientCoeff);
}