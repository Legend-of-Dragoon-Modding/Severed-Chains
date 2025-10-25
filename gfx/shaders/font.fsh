#version 330 core

uniform sampler2D tex;
uniform vec3 colour;

smooth in vec2 vertUv;

out vec4 FragColour;

void main() {
  FragColour = vec4(colour, texture(tex, vertUv).r);
}
