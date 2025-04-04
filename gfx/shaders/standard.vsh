#version 330 core

layout(location = 0) in vec4 inPos; // w is vertex index
layout(location = 1) in vec3 inNorm; // unused
layout(location = 2) in vec2 inUv;
layout(location = 3) in float inTpage;
layout(location = 4) in float inClut;
layout(location = 5) in vec4 inColour;
layout(location = 6) in float inFlags;

smooth out vec2 vertUv;
flat out vec2 vertTpage;
flat out vec2 vertClut;
flat out int vertBpp;
smooth out vec4 vertColour;
flat out int vertFlags;

flat out float widthMultiplier;
flat out int widthMask;
flat out int indexShift;
flat out int indexMask;

smooth out float depth;
smooth out float depthOffset;

uniform vec2 clutOverride;
uniform vec2 tpageOverride;
uniform float modelIndex;

struct ModelTransforms {
  mat4 model;
  vec4 screenOffset;
};

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

/** 20-float (80-byte) stride */
layout(std140) uniform transforms2 {
  ModelTransforms[128] modelTransforms;
};

layout(std140) uniform projectionInfo {
  float znear;
  /** PS1 projection plane distance (H) */
  float zfar;
  float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  float projectionMode;
};

void main() {
  vec4 pos = vec4(inPos.xyz, 1.0f);

  vertFlags = int(inFlags);
  bool coloured = (vertFlags & 0x4) != 0;
  bool textured = (vertFlags & 0x2) != 0;

  ModelTransforms t = modelTransforms[int(modelIndex)];

  if(coloured) {
    vertColour = inColour;
  } else {
    vertColour = vec4(1.0, 1.0, 1.0, 1.0);
  }

  int intTpage = int(inTpage);
  vertBpp = intTpage >> 7 & 0x3;

  if(textured) {
    if(tpageOverride.x == 0) {
      vertTpage = vec2((intTpage & 0xf) * 64, (intTpage & 0x10) != 0 ? 256 : 0);
    } else {
      vertTpage = tpageOverride;
    }

    if(clutOverride.x == 0) {
      int intClut = int(inClut);
      vertClut = vec2((intClut & 0x3f) * 16, intClut >> 6);
    } else {
      vertClut = clutOverride;
    }

    if(vertBpp == 0 || vertBpp == 1) {
      int widthDivisor = 1 << 2 - vertBpp;
      widthMultiplier = 1.0 / widthDivisor;
      widthMask = widthDivisor - 1;
      indexShift = vertBpp + 2;
      indexMask = int(pow(16, vertBpp + 1) - 1);
    }
  }

  gl_Position = camera * t.model * pos;

  if(projectionMode == 1) { // Low quality submap projection
    // Projection plane division
    float z = clamp(gl_Position.z, 0, 65536);
    if(z != 0) {
      gl_Position.xy *= zfar / z;
    }
  }

  if(projectionMode == 2) { // High quality projection
    depthOffset = t.screenOffset.z * zdiffInv; // Depth is computed in the fragment shader
  } else if(t.screenOffset.z != 0) {
    gl_Position.z += t.screenOffset.z;
  }

  gl_Position.xy += t.screenOffset.xy;
  gl_Position = projection * gl_Position;
  vertUv = inUv;

  depth = gl_Position.z;
}
