#version 140

in vec3 verts;

void main() {
    gl_Position.xyz = verts;
    gl_Position.w = 1.0;
}
