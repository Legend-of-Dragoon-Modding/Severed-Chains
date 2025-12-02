#version 310 es
#extension GL_OES_shader_io_blocks : require

layout(location = 0) in highp vec4 inPos; // w is vertex index
layout(location = 1) in highp vec3 inNorm;
layout(location = 2) in highp vec2 inUv;
layout(location = 3) in highp float inTpage;
layout(location = 4) in highp float inClut;
layout(location = 5) in highp vec4 inColour;
layout(location = 6) in highp float inFlags;

out VS_OUT {
  smooth highp vec2 vertUv;
  flat highp vec2 vertTpage;
  flat highp vec2 vertClut;
  flat int vertBpp;
  smooth highp vec4 vertColour;
  flat int vertFlags;

  flat int translucency;

  flat highp float widthMultiplier;
  flat int widthMask;
  flat int indexShift;
  flat int indexMask;

  smooth highp float viewspaceZ;

  smooth highp float depth;
  smooth highp float depthOffset;
} vs_out;

uniform highp vec2 clutOverride;
uniform highp vec2 tpageOverride;
uniform highp float modelIndex;

struct ModelTransforms {
  mat4 model;
  highp vec4 screenOffset;
};

struct Light {
  mat4 lightDirection;
  mat3 lightColour;
  highp vec4 backgroundColour;
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
  highp float znear;
  /** PS1 projection plane distance (H) */
  highp float zfar;
  highp float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  highp float projectionMode;
};

void main() {
  highp vec4 pos = vec4(inPos.xyz, 1.0f);

  vs_out.vertFlags = int(inFlags);
  bool coloured = (vs_out.vertFlags & 0x4) != 0;
  bool textured = (vs_out.vertFlags & 0x2) != 0;
  bool lit = (vs_out.vertFlags & 0x1) != 0;

  ModelTransforms t = modelTransforms[int(modelIndex)];
  Light l = lights[int(modelIndex)];

  if(lit) {
    float range = 1.0;

    // Textures use a colour range where 0xff = 200%. We've normalized that so that 0xff will equal 2.0 rather than 1.0 so we need to adjust the range
    if(textured) {
      range = 2.0;
    }

    vs_out.vertColour.rgb = clamp(clamp(l.lightColour * clamp(l.lightDirection * vec4(inNorm, 1.0), 0.0, 8.0).rgb + l.backgroundColour.rgb, 0.0, 8.0) * inColour.rgb, 0.0, range);
  } else if(coloured) {
    vs_out.vertColour = inColour;
  } else {
    vs_out.vertColour = vec4(1.0, 1.0, 1.0, 1.0);
  }

  int intTpage = int(inTpage);
  vs_out.vertBpp = intTpage >> 7 & 0x3;
  vs_out.translucency = intTpage >> 5 & 0x3;

  if(textured) {
    if(tpageOverride.x == 0.0) {
      vs_out.vertTpage = vec2((intTpage & 0xf) * 64, (intTpage & 0x10) != 0 ? 256 : 0);
    } else {
      vs_out.vertTpage = tpageOverride;
    }

    if(clutOverride.x == 0.0) {
      int intClut = int(inClut);
      vs_out.vertClut = vec2((intClut & 0x3f) * 16, intClut >> 6);
    } else {
      vs_out.vertClut = clutOverride;
    }

    if(vs_out.vertBpp == 0 || vs_out.vertBpp == 1) {
      int widthDivisor = 1 << 2 - vs_out.vertBpp;
      vs_out.widthMultiplier = 1.0 / float(widthDivisor);
      vs_out.widthMask = widthDivisor - 1;
      vs_out.indexShift = vs_out.vertBpp + 2;
      vs_out.indexMask = int(pow(16.0, float(vs_out.vertBpp + 1)) - 1.0);
    }
  }

  gl_Position = camera * t.model * pos;
  vs_out.viewspaceZ = gl_Position.z;

  if(projectionMode == 1.0) { // Low quality submap projection
    // Projection plane division
    float z = clamp(gl_Position.z, 0.0, 65536.0);
    if(z != 0.0) {
      gl_Position.xy *= zfar / z;
    }
  }

  if(projectionMode == 2.0) { // High quality projection
    vs_out.depthOffset = t.screenOffset.z * zdiffInv; // Depth is computed in the fragment shader
  } else if(t.screenOffset.z != 0.0) {
    gl_Position.z += t.screenOffset.z;
  }

  gl_Position.xy += t.screenOffset.xy;
  gl_Position = projection * gl_Position;
  vs_out.vertUv = inUv;

  vs_out.depth = gl_Position.z;
}
