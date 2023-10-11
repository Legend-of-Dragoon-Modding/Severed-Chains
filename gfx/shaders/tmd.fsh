#version 330 core

smooth in vec2 vertUv;
smooth in vec2 vertTpage;
smooth in vec2 vertClut;
flat in float vertBpp;
smooth in vec4 vertColour;
flat in float vertFlags;

uniform vec3 recolour;
uniform sampler2D tex;

layout(location = 0) out vec4 outColour;

//TODO handle more flags

void main() {
  int flags = int(vertFlags);

  outColour = vec4(1.0, 1.0, 1.0, 1.0);

  // Vertex colour
  if((flags & 0x4) != 0) {
    outColour = vertColour;
  }

  // Textured
  if((flags & 0x2) != 0) {
    // Texture recolouring uses an RGB range of 0..128 or 0.0..0.5 so we multiply by 2
    if((flags & 0x4) != 0) {
      outColour *= 2.0;
    }

    //NOTE: these only work for 4/8 bpp
    int widthDivisor = 1 << int(2 - vertBpp);
    int widthMask = widthDivisor - 1;
    int indexShift = int(vertBpp + 2);
    int indexMask = int(pow(16, vertBpp + 1) - 1);

    // Calculate CLUT index
    vec2 uv = vec2((vertTpage.x + vertUv.x / widthDivisor) / 1024.0, (vertTpage.y + vertUv.y) / 512.0);
    vec4 indexVec = texture(tex, uv);
    int r = int(indexVec.r * 31.875);
    int g = int(indexVec.g * 31.875);
    int b = int(indexVec.b * 31.875);
    int a = int(indexVec.a * 0xff);
    int index = a << 15 | b << 10 | g << 5 | r;
    int p = (index >> ((int(vertTpage.x + vertUv.x) & widthMask) << indexShift)) & indexMask;
    vec2 clutUv = vec2((vertClut.x + p) / 1024.0, vertClut.y / 512.0);

    // Pull actual pixel colour from CLUT
    vec4 texColour = texture(tex, clutUv);

    // Discard if (0, 0, 0)
    if(texColour.r == 0 && texColour.g == 0 && texColour.b == 0) {
      discard;
    }

    outColour *= texColour;
  }

  outColour.rgb *= recolour;

  if((flags & 0x8) != 0) { // (B+F)/2 translucency
    outColour.a = 0.5;
  } else {
    outColour.a = 1.0;
  }
}
