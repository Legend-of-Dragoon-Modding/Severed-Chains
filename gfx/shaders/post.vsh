#version 310 es

layout(location = 0) in highp vec2 inPos;
layout(location = 1) in highp vec2 inUv;

smooth out highp vec2 vertUv;

void main() {
  gl_Position = vec4(inPos.x, inPos.y, 0.0, 1.0);

  vertUv = inUv;
}
