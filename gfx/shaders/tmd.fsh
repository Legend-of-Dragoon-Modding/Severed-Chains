#version 330 core

smooth in vec2 vertUv;
smooth in vec2 vertTpage;
smooth in vec2 vertClut;
flat in float vertBpp;
flat in float vertTranslucency;
smooth in vec4 vertColour;

out vec4 outColour;

uniform sampler2D tex;

//TODO
uniform int lit;
uniform int textured;
uniform int coloured;

void main() {
  //NOTE: these only work for 4/8 bpp
  int widthDivisor = 1 << int(2 - vertBpp);
  int widthMask = widthDivisor - 1;
  int indexShift = int(vertBpp + 2);
  int indexMask = int(pow(16, vertBpp + 1) - 1);

  vec2 uv = vec2((vertTpage.x + vertUv.x / widthDivisor) / 1024.0, (vertTpage.y + vertUv.y) / 512.0);
  vec4 indexVec = texture(tex, uv);
  int r = int(indexVec.r * 31.875);
  int g = int(indexVec.g * 31.875);
  int b = int(indexVec.b * 31.875);
  int a = int(indexVec.a * 0xff);
  int index = a << 15 | b << 10 | g << 5 | r;
  int p = (index >> ((int(vertTpage.x + vertUv.x) & widthMask) << indexShift)) & indexMask;
  vec2 clutUv = vec2((vertClut.x + p) / 1024.0, vertClut.y / 512.0);
  outColour = texture(tex, clutUv);

  if(coloured != 0) {
    outColour *= vertColour;
  }

  outColour.a = 1.0;
}
