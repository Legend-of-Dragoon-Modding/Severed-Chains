#version 310 es

layout(location = 0) in highp vec3 inPos;
layout(location = 1) in highp vec2 inUv;

smooth out highp vec2 vertUv;

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

layout(std140) uniform transforms2 {
  mat4 model;
};

void main() {
  gl_Position = projection * camera * model * vec4(inPos, 1.0);
  vertUv = inUv;
}
