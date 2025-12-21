#version 330 core

layout(location = 0) in vec2 inPos;
layout(location = 1) in vec2 inUv;

smooth out vec2 vertUv;

uniform mat4 projection;

void main() {
  gl_Position = projection * vec4(inPos, 1.0, 1.0);
  vertUv = inUv;
}
