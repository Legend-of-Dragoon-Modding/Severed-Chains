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
smooth out float depthOffset;

uniform float modelIndex;

struct ModelTransforms {
  mat4 model;
  vec4 screenOffset;
};

struct Light {
  mat4 lightDirection;
  mat3 lightColour;
  vec4 backgroundColour;
};

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

/** 20-float (80-byte) stride */
layout(std140) uniform transforms2 {
  ModelTransforms[128] modelTransforms;
};

/** 32-float (128-byte) stride */
layout(std140) uniform lighting {
  Light[128] lights;
};

layout(std140) uniform projectionInfo {
  float znear;
  /** PS1 projection plane distance (H) when in PS1 perspective mode */
  float zfar;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  float projectionMode;
};

void main() {
  vec4 pos = vec4(inPos, 1.0);

  vertColour = inColour;

  ModelTransforms t = modelTransforms[int(modelIndex)];
  Light l = lights[int(modelIndex)];

  // Lit
  if((int(inFlags) & 0x1) != 0) {
    vertColour.rgb = min((l.lightColour * max(l.lightDirection * vec4(inNorm, 1.0), 0.0).rgb + l.backgroundColour.rgb) * vertColour.rgb, 1.0);
  }

  gl_Position = camera * t.model * pos;

  if(projectionMode == 1) { // Low quality submap projection
    // Projection plane division
    float z = clamp(gl_Position.z, 0, 65536);
    if(z != 0) {
      gl_Position.xy *= zfar / z;
    }
  }

  gl_Position.xy += t.screenOffset.xy;
  gl_Position = projection * gl_Position;
  vertUv = inUv;
  vertTpage = inTpage;
  vertClut = inClut;
  vertBpp = inBpp;
  vertFlags = inFlags;

  depth = gl_Position.z;
  depthOffset = t.screenOffset.z;
}
