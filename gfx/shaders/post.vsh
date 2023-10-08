#version 330 core

layout(location = 0) in vec2 inPos;
layout(location = 1) in vec2 inUv;

smooth out vec2 vertUv;

void main() {
  gl_Position = vec4(inPos.x, inPos.y, 0.0, 1.0);

  vertUv = inUv;
}
