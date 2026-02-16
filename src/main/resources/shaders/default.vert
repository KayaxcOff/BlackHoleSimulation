#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 fColor;

uniform mat4 uTransform;
uniform mat4 uProjection;
uniform mat4 uView;

void main() {
    fColor = aColor;
    gl_Position = uProjection * uView * uTransform * vec4(aPos, 1.0);
}