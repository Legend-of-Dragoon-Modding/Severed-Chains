#version 330 core

smooth in vec2 vertUv;
flat in vec2 vertTpage;
flat in vec2 vertClut;
flat in int vertBpp;
smooth in vec4 vertColour;
flat in int vertFlags;

flat in float widthMultiplier;
flat in int widthMask;
flat in int indexShift;
flat in int indexMask;

smooth in float depth;
smooth in float depthOffset;

layout(std140) uniform projectionInfo {
  float znear;
  float zfar;
  float zdiffInv;
  float projectionMode;
};

uniform vec3 recolour;
uniform vec2 uvOffset;
uniform float translucency;
uniform float discardTranslucency;
uniform float alpha;
uniform float useTextureAlpha;
uniform sampler2D tex24;
uniform usampler2D tex15;

layout(location = 0) out vec4 outColour;

void main() {
  // Linearize depth for perspective transforms so that we can render ortho models at specific depths
  if(projectionMode == 2) {
    gl_FragDepth = (depth - znear) * zdiffInv + depthOffset;
  } else {
    gl_FragDepth = gl_FragCoord.z;
  }

  bool translucent = (vertFlags & 0x8) != 0 || translucency != 0;
  bool textured = (vertFlags & 0x2) != 0;
  outColour = vertColour;

  int translucencyMode = int(translucency);

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

    // Discard if (0, 0, 0, 0), or if alpha is 0 and we're using texture alpha mode
    if(texColour.a == 0 && (useTextureAlpha != 0 || texColour.r == 0 && texColour.g == 0 && texColour.b == 0)) {
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

  if(alpha != -1) {
    if(useTextureAlpha == 0) {
      outColour.a = alpha;
    } else {
      outColour.a *= alpha;

      if(translucencyMode == 2 || translucencyMode == 3) {
        outColour.rgb *= outColour.a;
      }
    }
  } else if(useTextureAlpha == 0) {
    if(translucent && translucencyMode == 1) { // (B+F)/2 translucency
      outColour.a = 0.5;
    } else {
      outColour.a = 1.0;
    }
  }
}
