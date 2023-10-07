#version 330 core

layout(location = 0) in vec3 inPos;
layout(location = 1) in vec3 inNorm;
layout(location = 2) in vec2 inUv;
layout(location = 3) in vec2 inTpage;
layout(location = 4) in vec2 inClut;
layout(location = 5) in float inBpp;
layout(location = 6) in float inTranslucency;
layout(location = 7) in vec4 inColour;

smooth out vec2 vertUv;
smooth out vec2 vertTpage;
smooth out vec2 vertClut;
flat out float vertBpp;
flat out float vertTranslucency;
smooth out vec4 vertColour;

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

layout(std140) uniform transforms2 {
  mat4 model;
};

void main() {
  vec4 pos = vec4(inPos, 1.0);

  gl_Position = projection * camera * model * pos;
  vertUv = inUv;
  vertTpage = inTpage;
  vertClut = inClut;
  vertBpp = inBpp;
  vertTranslucency = inTranslucency;
  vertColour = inColour;
}
