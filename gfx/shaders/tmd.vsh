#version 330 core

layout(location = 0) in vec3 inPos;
layout(location = 1) in vec3 inNorm;
layout(location = 2) in vec2 inUv;
layout(location = 3) in vec2 inTpage;
layout(location = 4) in vec2 inClut;
layout(location = 5) in float inBpp;
layout(location = 6) in vec4 inColour;
layout(location = 7) in float inFlags;

smooth out vec2 vertUv;
flat out vec2 vertTpage;
flat out vec2 vertClut;
flat out float vertBpp;
smooth out vec4 vertColour;
flat out float vertFlags;

smooth out float depth;

uniform float h;

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

layout(std140) uniform transforms2 {
  mat4 model;
  vec3 screenOffset;
};

layout(std140) uniform lighting {
  mat4 lightDirection;
  mat4 lightColour;
  vec4 backgroundColour;
};

layout(std140) uniform projectionInfo {
  float znear;
  float zfar;
  float orthographic;
};

void main() {
  vec4 pos = vec4(inPos, 1.0);

  vertColour = inColour;

  // Lit
  if((int(inFlags) & 0x1) != 0) {
    vertColour = min((lightColour * max(lightDirection * vec4(inNorm, 1.0), 0.0) + backgroundColour) * vertColour, 1.0);
  }

  gl_Position = camera * model * pos;

  // Projection plane division
  float z = clamp(gl_Position.z, 0, 65536);
  if(orthographic == 0 && z != 0) {
    gl_Position.xy *= h / z;
  }

  gl_Position = projection * gl_Position;
  gl_Position.xy += screenOffset.xy * gl_Position.w;
  vertUv = inUv;
  vertTpage = inTpage;
  vertClut = inClut;
  vertBpp = inBpp;
  vertFlags = inFlags;

  depth = screenOffset.z;
}
