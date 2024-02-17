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
  float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  float projectionMode;
};

void main() {
  vec4 pos = vec4(inPos, 1.0);

  int flags = int(inFlags);
  bool lit = (flags & 0x1) != 0;
  bool textured = (flags & 0x2) != 0;
  bool coloured = (flags & 0x4) != 0;

  ModelTransforms t = modelTransforms[int(modelIndex)];
  Light l = lights[int(modelIndex)];

  // Lit
  if(lit) {
    vertColour.rgb = min((l.lightColour * max(l.lightDirection * vec4(inNorm, 1.0), 0.0).rgb + l.backgroundColour.rgb) * inColour.rgb, 1.0);
  } else if(coloured) {
    vertColour = inColour;
  } else {
    vertColour = vec4(1.0, 1.0, 1.0, 1.0);
  }

  if(textured) {
    if(coloured) {
      // Texture recolouring uses an RGB range of 0..128 or 0.0..0.5 so we multiply by 2
      vertColour.rgb *= 2.0;
      vertColour.a = 1.0;
    }

    if(clutOverride.x == 0) {
      vertTpage = inTpage;
    } else {
      vertTpage = tpageOverride;
    }

    if(clutOverride.x == 0) {
      vertClut = inClut;
    } else {
      vertClut = clutOverride;
    }

    if(inBpp == 0 || inBpp == 1) {
      int widthDivisor = 1 << int(2 - inBpp);
      widthMultiplier = 1.0 / widthDivisor;
      widthMask = widthDivisor - 1;
      indexShift = int(inBpp + 2);
      indexMask = int(pow(16, inBpp + 1) - 1);
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

  gl_Position.xy += t.screenOffset.xy;
  gl_Position = projection * gl_Position;
  vertUv = inUv;
  vertBpp = inBpp;
  vertFlags = inFlags;

  depth = gl_Position.z;
  depthOffset = t.screenOffset.z;
}
