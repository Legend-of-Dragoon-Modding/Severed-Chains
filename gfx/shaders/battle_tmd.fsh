#version 310 es
#extension GL_OES_shader_io_blocks : require

in GS_OUT {
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

  smooth highp float depth;
  smooth highp float depthOffset;
};

layout(std140) uniform projectionInfo {
  highp float znear;
  highp float zfar;
  highp float zdiffInv;
  highp float projectionMode;
};

uniform highp vec3 recolour;
uniform highp vec2 uvOffset;
uniform highp float discardTranslucency;
uniform int tmdTranslucency;
uniform highp int ctmdFlags;
uniform highp sampler2D tex24;
uniform highp usampler2D tex15;

layout(location = 0) out highp vec4 outColour;

void main() {
  // Linearize depth for perspective transforms so that we can render ortho models at specific depths
  if(projectionMode == 2.0) {
    gl_FragDepth = (depth - znear) * zdiffInv + depthOffset;
  } else {
    gl_FragDepth = gl_FragCoord.z;
  }

  bool ctmd = (ctmdFlags & 0x20) != 0;
  bool uniformLit = (ctmdFlags & 0x10) != 0;
  bool translucent = (vertFlags & 0x8) != 0 || (ctmdFlags & 0x2) != 0;
  bool textured = (vertFlags & 0x2) != 0;
  outColour = vertColour;

  int translucencyMode = translucency + 1;
  if(translucent && (!textured || uniformLit)) {
    translucencyMode = tmdTranslucency + 1;
  }

  // Textured
  if(textured) {
    highp vec4 texColour;
    if(vertBpp == 0 || vertBpp == 1) {
      // Calculate CLUT index
      ivec2 uv = ivec2(vertTpage.x + (vertUv.x + uvOffset.x) * widthMultiplier, vertTpage.y + vertUv.y + uvOffset.y);
      ivec4 indexVec = ivec4(texelFetch(tex15, uv, 0));
      int p = (indexVec.r >> ((int(vertTpage.x + vertUv.x) & widthMask) << indexShift)) & indexMask;

      // Pull actual pixel colour from CLUT
      texColour = texelFetch(tex24, ivec2(vertClut.x + float(p), vertClut.y), 0);
    } else if(vertBpp == 2) {
      ivec2 uv = ivec2(vertTpage.x + (vertUv.x + uvOffset.x), vertTpage.y + vertUv.y + uvOffset.y);
      texColour = texelFetch(tex24, ivec2(uv.x, uv.y), 0);
    } else {
      texColour = texture(tex24, vertUv + uvOffset);
    }

    // Discard if (0, 0, 0)
    if(texColour.a == 0.0 && texColour.r == 0.0 && texColour.g == 0.0 && texColour.b == 0.0) {
      discard;
    }

    // If translucent primitive and texture pixel translucency bit is set, pixel is translucent so we defer rendering
    if(discardTranslucency == 1.0 && translucent && texColour.a != 0.0 || discardTranslucency == 2.0 && (!translucent || texColour.a == 0.0)) {
      discard;
    }

    outColour = clamp(outColour * texColour, 0.0, 1.0);
  } else {
    // Untextured translucent primitives don't have a translucency bit so we always discard during the appropriate discard modes
    if(discardTranslucency == 1.0 && translucent || discardTranslucency == 2.0 && !translucent) {
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
