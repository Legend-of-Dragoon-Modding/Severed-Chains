#version 310 es

layout(location = 0) in highp vec4 inPos; // w is vertex index
layout(location = 1) in highp vec3 inNorm; // unused
layout(location = 2) in highp vec2 inUv;
layout(location = 3) in highp float inTpage;
layout(location = 4) in highp float inClut;
layout(location = 5) in highp vec4 inColour;
layout(location = 6) in highp float inFlags;

smooth out highp vec2 vertUv;
flat out highp vec2 vertTpage;
flat out highp vec2 vertClut;
flat out int vertBpp;
smooth out highp vec4 vertColour;
flat out int vertFlags;

flat out highp float widthMultiplier;
flat out int widthMask;
flat out int indexShift;
flat out int indexMask;

smooth out highp float depth;
smooth out highp float depthOffset;

uniform highp vec2 clutOverride;
uniform highp vec2 tpageOverride;
uniform highp float modelIndex;

struct ModelTransforms {
  mat4 model;
  highp vec4 screenOffset;
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
  highp float znear;
  /** PS1 projection plane distance (H) */
  highp float zfar;
  highp float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  highp float projectionMode;
};

void main() {
  highp vec4 pos = vec4(inPos.xyz, 1.0f);

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
    if(tpageOverride.x == 0.0) {
      vertTpage = vec2((intTpage & 0xf) * 64, (intTpage & 0x10) != 0 ? 256 : 0);
    } else {
      vertTpage = tpageOverride;
    }

    if(clutOverride.x == 0.0) {
      int intClut = int(inClut);
      vertClut = vec2((intClut & 0x3f) * 16, intClut >> 6);
    } else {
      vertClut = clutOverride;
    }

    if(vertBpp == 0 || vertBpp == 1) {
      int widthDivisor = 1 << 2 - vertBpp;
      widthMultiplier = 1.0 / float(widthDivisor);
      widthMask = widthDivisor - 1;
      indexShift = vertBpp + 2;
      indexMask = int(pow(16.0, float(vertBpp + 1)) - 1.0);
    }
  }

  gl_Position = camera * t.model * pos;

  if(projectionMode == 1.0) { // Low quality submap projection
    // Projection plane division
    float z = clamp(gl_Position.z, 0.0, 65536.0);
    if(z != 0.0) {
      gl_Position.xy *= zfar / z;
    }
  }

  if(projectionMode == 2.0) { // High quality projection
    depthOffset = t.screenOffset.z * zdiffInv; // Depth is computed in the fragment shader
  } else if(t.screenOffset.z != 0.0) {
    gl_Position.z += t.screenOffset.z;
  }

  gl_Position.xy += t.screenOffset.xy;
  gl_Position = projection * gl_Position;
  vertUv = inUv;

  depth = gl_Position.z;
}
