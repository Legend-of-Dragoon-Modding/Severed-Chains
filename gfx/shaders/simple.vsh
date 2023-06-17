#version 330 core

layout(location = 0) in vec2 inPos;
layout(location = 1) in vec2 inUv;

smooth out vec2 vertUv;

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

layout(std140) uniform transforms2 {
  mat4 model;
};

void main() {
  vec4 pos = vec4(inPos, 1.0, 1.0);

  gl_Position = projection * camera * model * pos;
  vertUv = inUv;
}
