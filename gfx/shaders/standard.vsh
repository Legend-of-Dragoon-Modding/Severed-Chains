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

layout(std140) uniform clutAnimation {
  vec4[1024] clutAnimations;
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
  bool lit = (vertFlags & 0x1) != 0;

  ModelTransforms t = modelTransforms[int(modelIndex)];

  if(lit) {
    Light l = lights[int(modelIndex)];
    float range = 1.0;

    // Textures use a colour range where 0xff = 200%. We've normalized that so that 0xff will equal 2.0 rather than 1.0 so we need to adjust the range
    if(textured) {
      range = 2.0;
    }

    vertColour.rgb = clamp(clamp(l.lightColour * clamp(l.lightDirection * vec4(inNorm, 1.0), 0.0, 8.0).rgb + l.backgroundColour.rgb, 0.0, 8.0) * inColour.rgb, 0.0, range);
  } else if(coloured) {
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

    for(int clutAnimationIndex = 0; clutAnimationIndex < 1024; clutAnimationIndex++) {
      vec4 anim = clutAnimations[clutAnimationIndex];

      if(anim.x == -1) {
        break;
      }

      if(anim.xy == vertClut.xy) {
        vertClut.xy = anim.zw;
        break;
      }
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
