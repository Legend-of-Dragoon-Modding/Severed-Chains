#version 330 core

in VS_OUT {
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
};

layout(std140) uniform projectionInfo {
  float znear;
  float zfar;
  float zdiffInv;
  float projectionMode;
};

uniform float discardTranslucency;
uniform sampler2D tex24;
uniform usampler2D tex15;

layout(location = 0) out vec3 outPos;
layout(location = 1) out vec3 outNorm;

void main() {
  bool translucent = (vertFlags & 0x8) != 0;
  bool textured = (vertFlags & 0x2) != 0;

  // Textured
  if(textured) {
    vec4 texColour;
    if(vertBpp == 0 || vertBpp == 1) {
      // Calculate CLUT index
      ivec2 uv = ivec2(vertTpage.x + vertUv.x * widthMultiplier, vertTpage.y + vertUv.y);
      ivec4 indexVec = ivec4(texelFetch(tex15, uv, 0));
      int p = (indexVec.r >> ((int(vertTpage.x + vertUv.x) & widthMask) << indexShift)) & indexMask;

      // Pull actual pixel colour from CLUT
      texColour = texelFetch(tex24, ivec2(vertClut.x + p, vertClut.y), 0);
    } else if(vertBpp == 2) {
      ivec2 uv = ivec2(vertTpage.x + vertUv.x, vertTpage.y + vertUv.y);
      texColour = texelFetch(tex24, ivec2(uv.x, uv.y), 0);
    } else {
      texColour = texture(tex24, vertUv);
    }

    // Discard if (0, 0, 0)
    if(texColour.a == 0 && texColour.r == 0 && texColour.g == 0 && texColour.b == 0) {
      discard;
    }

    // If translucent primitive and texture pixel translucency bit is set, pixel is translucent so we defer rendering
    if(discardTranslucency == 1 && translucent && texColour.a != 0 || discardTranslucency == 2 && (!translucent || texColour.a == 0)) {
      discard;
    }
  } else {
    // Untextured translucent primitives don't have a translucency bit so we always discard during the appropriate discard modes
    if(discardTranslucency == 1 && translucent || discardTranslucency == 2 && !translucent) {
      discard;
    }
  }

  outPos = pos;
  outNorm = normalize(norm);
}
