#version 330 core

smooth in vec2 vertUv;
uniform vec2 shiftUv;
uniform vec4 recolour;

out vec4 outColour;

uniform sampler2D tex;

void main() {
  outColour = texture(tex, vertUv + shiftUv) * recolour;
}
