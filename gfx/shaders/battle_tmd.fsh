#version 330 core

smooth in vec2 vertUv;
flat in vec2 vertTpage;
flat in vec2 vertClut;
flat in int vertBpp;
smooth in vec4 vertColour;
flat in int vertFlags;

flat in int translucency;

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
uniform float discardTranslucency;
uniform int tmdTranslucency;
uniform int ctmdFlags;
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

  bool ctmd = (ctmdFlags & 0x20) != 0;
  bool uniformLit = (ctmdFlags & 0x10) != 0;
  bool translucent = (vertFlags & 0x8) != 0 || (ctmdFlags & 0x2) != 0;
  bool textured = (vertFlags & 0x2) != 0;
  outColour = vertColour;

  int translucencyMode = translucency;
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
    } else {
      texColour = texture(tex24, vertUv + uvOffset);
    }

    // Discard if (0, 0, 0)
    if(texColour.a == 0 && texColour.r == 0 && texColour.g == 0 && texColour.b == 0) {
      discard;
    }

    // If translucent primitive and texture pixel translucency bit is set, pixel is translucent so we defer rendering
    if(discardTranslucency == 1 && translucencyMode != 0 && texColour.a != 0 || discardTranslucency == 2 && (translucencyMode == 0 || texColour.a == 0)) {
      discard;
    }

    outColour *= texColour;
  } else {
    // Untextured translucent primitives don't have a translucency bit so we always discard during the appropriate discard modes
    if(discardTranslucency == 1 && translucencyMode != 0 || discardTranslucency == 2 && translucencyMode == 0) {
      discard;
    }
  }

  outColour.rgb *= recolour;

  // The or condition is to disable translucency if a texture's pixel has alpha disabled
  if(translucencyMode == 1 && (!textured || outColour.a != 0)) { // (B+F)/2 translucency
    outColour.a = 0.5;
  } else {
    outColour.a = 1.0;
  }
}
