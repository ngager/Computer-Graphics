#version 120
/**
 * flat fragment shader
 * color comes from input parameter from pipeline
 */

varying vec4 color;

void main()
{
    gl_FragColor = color;
}
