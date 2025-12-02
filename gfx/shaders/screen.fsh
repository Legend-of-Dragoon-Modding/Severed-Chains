#version 310 es

// shader inputs
smooth in highp vec2 vertUv;

// shader outputs
layout(location = 0) out highp vec4 frag;

// screen image
uniform sampler2D screen;

void main() {
  frag = vec4(texture(screen, vertUv).rgb, 1.0f);
}
