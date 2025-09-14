#version 330 core

in GS_OUT {
  smooth vec3 vertNorm;
  smooth vec2 vertUv;
  flat vec2 vertTpage;
  flat vec2 vertClut;
  flat int vertBpp;
  smooth vec4 vertColour;
  flat int vertFlags;

  flat int translucency;

  flat float widthMultiplier;
  flat int widthMask;
  flat int indexShift;
  flat int indexMask;

  smooth float depth;
  smooth float depthOffset;
};

struct Light {
  mat4 lightDirection;
  mat3 lightColour;
  vec4 backgroundColour;
};

/** 32-float (128-byte) stride */
layout(std140) uniform lighting {
  Light[128] lights;
};

layout(std140) uniform projectionInfo {
  float znear;
  float zfar;
  float zdiffInv;
  float projectionMode;
};

uniform float modelIndex;
uniform vec3 recolour;
uniform vec2 uvOffset;
uniform float discardTranslucency;
uniform int tmdTranslucency;
uniform int ctmdFlags;
uniform sampler2D tex24;
uniform usampler2D tex15;
uniform sampler2D texSsao;

layout(location = 0) out vec4 outColour;

void main() {
  // Linearize depth for perspective transforms so that we can render ortho models at specific depths
  if(projectionMode == 2) {
    gl_FragDepth = (depth - znear) * zdiffInv + depthOffset;
  } else {
    gl_FragDepth = gl_FragCoord.z;
  }

  bool ctmd = (ctmdFlags & 0x20) != 0;
  bool uniformLit = (ctmdFlags & 0x10) != 0;
  bool translucent = (vertFlags & 0x8) != 0 || (ctmdFlags & 0x2) != 0;
  bool textured = (vertFlags & 0x2) != 0;
  bool lit = (vertFlags & 0x1) != 0;
  outColour = vertColour;

  if(lit) {
    Light l = lights[int(modelIndex)];
    float range = 1.0;

    // Textures use a colour range where 0xff = 200%. We've normalized that so that 0xff will equal 2.0 rather than 1.0 so we need to adjust the range
    if(textured) {
      range = 2.0;
    }

    outColour.rgb = clamp(clamp(l.lightColour * clamp(l.lightDirection * vec4(vertNorm, 1.0), 0.0, 8.0).rgb + l.backgroundColour.rgb, 0.0, 8.0) * outColour.rgb, 0.0, range);
  }

  vec2 fragPos = vec2(gl_FragCoord.x / 3195, gl_FragCoord.y / 1680);
  float ssao = texture(texSsao, fragPos).r;

  if(ssao != 0) {
    outColour.rgb *= ssao;
  }

  int translucencyMode = translucency + 1;
  if(translucent && (!textured || uniformLit)) {
    translucencyMode = tmdTranslucency + 1;
  }

  // Textured
  if(textured) {
    vec4 texColour;
    if(vertBpp == 0 || vertBpp == 1) {
      // Calculate CLUT index
      ivec2 uv = ivec2(vertTpage.x + (vertUv.x + uvOffset.x) * widthMultiplier, vertTpage.y + vertUv.y + uvOffset.y);
      ivec4 indexVec = ivec4(texelFetch(tex15, uv, 0));
      int p = (indexVec.r >> ((int(vertTpage.x + vertUv.x) & widthMask) << indexShift)) & indexMask;

      // Pull actual pixel colour from CLUT
      texColour = texelFetch(tex24, ivec2(vertClut.x + p, vertClut.y), 0);
    } else if(vertBpp == 2) {
      ivec2 uv = ivec2(vertTpage.x + (vertUv.x + uvOffset.x), vertTpage.y + vertUv.y + uvOffset.y);
      texColour = texelFetch(tex24, ivec2(uv.x, uv.y), 0);
    } else {
      texColour = texture(tex24, vertUv + uvOffset);
    }

    // Discard if (0, 0, 0)
    if(texColour.a == 0 && texColour.r == 0 && texColour.g == 0 && texColour.b == 0) {
      discard;
    }

    // If translucent primitive and texture pixel translucency bit is set, pixel is translucent so we defer rendering
    if(discardTranslucency == 1 && translucent && texColour.a != 0 || discardTranslucency == 2 && (!translucent || texColour.a == 0)) {
      discard;
    }

    outColour = clamp(outColour * texColour, 0.0, 1.0);
  } else {
    // Untextured translucent primitives don't have a translucency bit so we always discard during the appropriate discard modes
    if(discardTranslucency == 1 && translucent || discardTranslucency == 2 && !translucent) {
      discard;
    }
  }

  outColour.rgb *= recolour;

  if(translucent && translucencyMode == 1) { // (B+F)/2 translucency
    outColour.a = 0.5;
  } else {
    outColour.a = 1.0;
  }
}
