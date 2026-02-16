#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aInstancePos;
layout (location = 2) in vec4 aInstanceColor;

out vec4 fColor;

uniform mat4 uProjection;
uniform mat4 uView;

void main() {
    vec3 cameraRight = vec3(uView[0][0], uView[1][0], uView[2][0]);
    vec3 cameraUp = vec3(uView[0][1], uView[1][1], uView[2][1]);

    vec3 worldPos = aInstancePos
    + cameraRight * aPos.x * 0.2
    + cameraUp * aPos.y * 0.2;

    fColor = aInstanceColor;
    gl_Position = uProjection * uView * vec4(worldPos, 1.0);
}