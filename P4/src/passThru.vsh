#version 120
/**
 * Simple pass-through vertex shader: 
 *     passes position on (after multiplying by current P*MV transform
 *     passes color on.
 */
uniform   mat4 pXmv;  // Composite of projection and modelview matrices
attribute vec4 vPosition;
attribute vec4 vColor;
varying vec4 color;

void main()
{
    gl_Position = pXmv * vPosition;
    color = vColor;
}
