#version 330 core

layout(location = 0) in vec4 inPos; // w is vertex index
layout(location = 1) in vec3 inNorm; // unused
layout(location = 2) in vec2 inUv;
layout(location = 3) in float inTpage;
layout(location = 4) in float inClut;
layout(location = 5) in vec4 inColour;
layout(location = 6) in float inFlags;

out VS_OUT {
  smooth vec2 vertUv;
  smooth vec4 vertColour;

  smooth float depth;
  smooth float depthOffset;

  smooth vec2 screenspaceSize;
} vs_out;

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
  vec2 screenSize;
};

void main() {
  vec4 pos = vec4(inPos.xyz, 1.0f);

  int flags = int(inFlags);
  bool coloured = (flags & 0x4) != 0;

  ModelTransforms t = modelTransforms[int(modelIndex)];

  if(coloured) {
    vs_out.vertColour = inColour;
  } else {
    vs_out.vertColour = vec4(1.0, 1.0, 1.0, 1.0);
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
    vs_out.depthOffset = t.screenOffset.z * zdiffInv; // Depth is computed in the fragment shader
  } else if(t.screenOffset.z != 0) {
    gl_Position.z += t.screenOffset.z;
  }

  gl_Position.xy += t.screenOffset.xy;
  gl_Position = projection * gl_Position;
  vs_out.vertUv = inUv;

  vs_out.depth = gl_Position.z;

  vec2 ndc = gl_Position.xy / gl_Position.w;
  vs_out.screenspaceSize = (ndc * 0.5 + 0.5) * screenSize;
}
