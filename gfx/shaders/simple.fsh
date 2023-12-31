#version 330 core

smooth in vec2 vertUv;

out vec4 outColour;

uniform sampler2D tex;
uniform vec2 shiftUv;
uniform vec4 recolour;

void main() {
  outColour = texture(tex, vertUv + shiftUv) * recolour;
}
