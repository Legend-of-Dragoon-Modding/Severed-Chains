#version 310 es

smooth in highp vec2 vertUv;

out highp vec4 outColour;

uniform sampler2D tex;
uniform highp vec2 shiftUv;
uniform highp vec4 recolour;

void main() {
  outColour = texture(tex, vertUv + shiftUv) * recolour;
}
