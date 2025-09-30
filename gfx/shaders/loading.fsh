#version 330 core

smooth in vec2 vertUv;

out vec4 outColour;

uniform float alpha;
uniform float ticks;
uniform sampler2D tex;

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
  vec3 rainbow = hsv2rgb(vec3(ticks, 1.0, 1.0));
  outColour = texture(tex, vertUv) * vec4(rainbow, alpha);
}
