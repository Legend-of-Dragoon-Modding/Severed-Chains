#version 330 core

smooth in vec2 vertUv;

out vec4 outColour;

uniform sampler2D tex;

void main() {
  outColour = texture(tex, vertUv);
  outColour.a = 1.0;
}
