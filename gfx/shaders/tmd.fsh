#version 330 core

smooth in vec2 vertUv;
flat in vec2 vertTpage;
flat in vec2 vertClut;
flat in float vertBpp;
smooth in vec4 vertColour;
flat in float vertFlags;

smooth in float depth;
smooth in float depthOffset;

layout(std140) uniform projectionInfo {
  float znear;
  float zfar;
  float projectionMode;
};

uniform vec3 recolour;
uniform vec2 clutOverride;
uniform vec2 tpageOverride;
uniform vec2 uvOffset;
uniform float discardTranslucency;
uniform sampler2D tex24;
uniform usampler2D tex15;

layout(location = 0) out vec4 outColour;

void main() {
  // Linearize depth for perspective transforms so that we can render ortho models at specific depths
  if(projectionMode == 2) {
    gl_FragDepth = (depth - znear) / (zfar - znear);
  } else {
    gl_FragDepth = gl_FragCoord.z;
  }

  gl_FragDepth += depthOffset;

  int flags = int(vertFlags);

  outColour = vec4(1.0, 1.0, 1.0, 1.0);

  // Vertex colour or lit
  if((flags & 0x5) != 0) {
    outColour = vertColour;
  }

  // Textured
  if((flags & 0x2) != 0) {
    // Texture recolouring uses an RGB range of 0..128 or 0.0..0.5 so we multiply by 2
    if((flags & 0x4) != 0) {
      outColour.rgb *= 2.0;
      outColour.a = 1.0;
    }

    vec4 texColour;
    if(vertBpp == 0 || vertBpp == 1) {
      int widthDivisor = 1 << int(2 - vertBpp);
      int widthMask = widthDivisor - 1;
      int indexShift = int(vertBpp + 2);
      int indexMask = int(pow(16, vertBpp + 1) - 1);

      // Calculate CLUT index
      vec2 tpage;
      if(tpageOverride.x != 0) {
        tpage = tpageOverride;
      } else {
        tpage = vertTpage;
      }

      ivec2 uv = ivec2(tpage.x + (vertUv.x + uvOffset.x) / widthDivisor, tpage.y + vertUv.y + uvOffset.y);
      ivec4 indexVec = ivec4(texelFetch(tex15, uv, 0));
      int p = (indexVec.r >> ((int(tpage.x + vertUv.x) & widthMask) << indexShift)) & indexMask;

      ivec2 clutUv;
      if(clutOverride.x == 0) {
        clutUv = ivec2(vertClut.x + p, vertClut.y);
      } else {
        clutUv = ivec2(clutOverride.x + p, clutOverride.y);
      }

      // Pull actual pixel colour from CLUT
      texColour = texelFetch(tex24, clutUv, 0);
    } else {
      texColour = texture(tex24, vertUv);
    }

    // Discard if (0, 0, 0)
    if(texColour.a == 0 && texColour.r == 0 && texColour.g == 0 && texColour.b == 0) {
      discard;
    }

    // If translucent primitive and texture pixel translucency bit is set, pixel is translucent so we defer rendering
    if(discardTranslucency == 1 && (flags & ~0x7) != 0 && texColour.a != 0 || discardTranslucency == 2 && ((flags & ~0x7) == 0 || texColour.a == 0)) {
      discard;
    }

    outColour *= texColour;
  } else {
    // Untextured translucent primitives don't have a translucency bit so we always discard during the appropriate discard modes
    if(discardTranslucency == 1 && (flags & ~0x7) != 0 || discardTranslucency == 2 && (flags & ~0x7) == 0) {
      discard;
    }
  }

  outColour.rgb *= recolour;

  // The or condition is to disable translucency if a texture's pixel has alpha disabled
  if((flags & 0x8) != 0 && ((flags & 0x2) == 0 || outColour.a != 0)) { // (B+F)/2 translucency
    outColour.a = 0.5;
  } else {
    outColour.a = 1.0;
  }
}
