#version 330 core

layout(location = 0) in vec4 inPos; // w is vertex index
layout(location = 1) in vec3 inNorm;
layout(location = 2) in vec2 inUv;
layout(location = 3) in float inTpage;
layout(location = 4) in float inClut;
layout(location = 5) in vec4 inColour;
layout(location = 6) in float inFlags;

out VS_OUT {
  smooth vec2 vertUv;
  flat vec2 vertTpage;
  flat vec2 vertClut;
  flat int vertBpp;
  flat int vertFlags;

  flat float widthMultiplier;
  flat int widthMask;
  flat int indexShift;
  flat int indexMask;

  smooth vec3 pos;
  smooth vec3 norm;

  smooth float depth;
  smooth float depthOffset;
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
  /** PS1 projection plane distance (H) when in PS1 perspective mode */
  float zfar;
  float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  float projectionMode;
};

void main() {
  vec4 pos = vec4(inPos.xyz, 1.0f);

  vs_out.vertFlags = int(inFlags);

  bool textured = (vs_out.vertFlags & 0x2) != 0;

  ModelTransforms t = modelTransforms[int(modelIndex)];

  int intTpage = int(inTpage);
  vs_out.vertBpp = intTpage >> 7 & 0x3;

  if(textured) {
    vs_out.vertTpage = vec2((intTpage & 0xf) * 64, (intTpage & 0x10) != 0 ? 256 : 0);

    int intClut = int(inClut);
    vs_out.vertClut = vec2((intClut & 0x3f) * 16, intClut >> 6);

    if(vs_out.vertBpp == 0 || vs_out.vertBpp == 1) {
      int widthDivisor = 1 << 2 - vs_out.vertBpp;
      vs_out.widthMultiplier = 1.0 / widthDivisor;
      vs_out.widthMask = widthDivisor - 1;
      vs_out.indexShift = vs_out.vertBpp + 2;
      vs_out.indexMask = int(pow(16, vs_out.vertBpp + 1) - 1);
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
    vs_out.depthOffset = t.screenOffset.z * zdiffInv; // Depth is computed in the fragment shader
  } else if(t.screenOffset.z != 0) {
    gl_Position.z += t.screenOffset.z;
  }

  gl_Position.xy += t.screenOffset.xy;
  vs_out.pos = gl_Position.xyz;

  gl_Position = projection * gl_Position;

  vs_out.vertUv = inUv;

  vs_out.depth = gl_Position.z;
  vs_out.norm = normalize(transpose(inverse(mat3(camera * t.model))) * inNorm);
}
