#version 330 core

in GS_OUT {
  smooth vec2 vertUv;
  smooth vec4 vertColour;

  smooth float depth;
  smooth float depthOffset;

  flat vec2 screenspaceSize;
} gs_out;

layout(std140) uniform projectionInfo {
  float znear;
  float zfar;
  float zdiffInv;
  float projectionMode;
  vec2 screenSize;
};

layout(std140) uniform scissor {
  float scissorX;
  float scissorY;
  float scissorW;
  float scissorH;
};

out vec4 outColour;

uniform vec4 lightColour;
uniform vec4 darkColour;

float cnoise(vec2 P);
float beta(float, float);

void main() {
  // Older Intel iGPUs are buggy and don't implement scissoring properly, causing the Shirley fight to lock up when
  // she transforms into another character. This is a workaround and reimplements scissoring at the shader level.
  if(gl_FragCoord.x < scissorX || gl_FragCoord.x >= scissorX + scissorW || gl_FragCoord.y < scissorY || gl_FragCoord.y >= scissorY + scissorH) {
    discard;
  }

  // Linearize depth for perspective transforms so that we can render ortho models at specific depths
  if(projectionMode == 2) {
    gl_FragDepth = (gs_out.depth - znear) * zdiffInv + gs_out.depthOffset;
  } else {
    gl_FragDepth = gl_FragCoord.z;
  }

  vec2 topLeft = gs_out.screenspaceSize * gs_out.vertUv;
  vec2 bottomRight = gs_out.screenspaceSize * (vec2(1.0, 1.0) - gs_out.vertUv);

  //float noise = 1.0 - cnoise(gs_out.vertUv * gs_out.screenspaceSize / 1.3) / 25.0;
  float noise = cnoise(gs_out.vertUv * gs_out.screenspaceSize / 1.3);

  /*
  if(topLeft.x < 32) {
    noise -= (32 - topLeft.x) / 128;
  }

  if(topLeft.y < 32) {
    noise -= (32 - topLeft.y) / 128;
  }
  */

  outColour = mix(lightColour, darkColour, noise);

  /*
  if(topLeft.x < 16) {
    outColour.b = 1.0;
  }

  if(topLeft.y < 16) {
    outColour.b = 1.0;
  }

  if(bottomRight.x < 16) {
    outColour.b = 1.0;
  }

  if(bottomRight.y < 16) {
    outColour.b = 1.0;
  }
  */
}

float lgamma(float x) {
  // Lanczos approximation constants
  float[] c = float[] (76.18009172947146, -86.50532032941677,
                24.01409824083091, -1.231739572450155,
                0.1208650973866179e-2, -0.5395239384953e-5);
  float y = x;
  float tmp = x + 5.5;
  tmp -= (x + 0.5) * log(tmp);
  float ser = 1.000000000190015;
  for(int i = 0; i < 6; i++) {
    y += 1.0;
    ser += c[i] / y;
  }
  return -tmp + log(2.5066282746310005 * ser / x);
}

float beta(float a, float b) {
  return exp(lgamma(a) + lgamma(b) - lgamma(a + b));
}

//	Classic Perlin 2D Noise
//	by Stefan Gustavson (https://github.com/stegu/webgl-noise)
//
vec2 fade(vec2 t) {return t*t*t*(t*(t*6.0-15.0)+10.0);}
vec4 permute(vec4 x) {return mod(((x*34.0)+1.0)*x, 289.0);}

float cnoise(vec2 P){
  vec4 Pi = floor(P.xyxy) + vec4(0.0, 0.0, 1.0, 1.0);
  vec4 Pf = fract(P.xyxy) - vec4(0.0, 0.0, 1.0, 1.0);
  Pi = mod(Pi, 289.0); // To avoid truncation effects in permutation
  vec4 ix = Pi.xzxz;
  vec4 iy = Pi.yyww;
  vec4 fx = Pf.xzxz;
  vec4 fy = Pf.yyww;
  vec4 i = permute(permute(ix) + iy);
  vec4 gx = 2.0 * fract(i * 0.0243902439) - 1.0; // 1/41 = 0.024...
  vec4 gy = abs(gx) - 0.5;
  vec4 tx = floor(gx + 0.5);
  gx = gx - tx;
  vec2 g00 = vec2(gx.x,gy.x);
  vec2 g10 = vec2(gx.y,gy.y);
  vec2 g01 = vec2(gx.z,gy.z);
  vec2 g11 = vec2(gx.w,gy.w);
  vec4 norm = 1.79284291400159 - 0.85373472095314 *
    vec4(dot(g00, g00), dot(g01, g01), dot(g10, g10), dot(g11, g11));
  g00 *= norm.x;
  g01 *= norm.y;
  g10 *= norm.z;
  g11 *= norm.w;
  float n00 = dot(g00, vec2(fx.x, fy.x));
  float n10 = dot(g10, vec2(fx.y, fy.y));
  float n01 = dot(g01, vec2(fx.z, fy.z));
  float n11 = dot(g11, vec2(fx.w, fy.w));
  vec2 fade_xy = fade(Pf.xy);
  vec2 n_x = mix(vec2(n00, n01), vec2(n10, n11), fade_xy.x);
  float n_xy = mix(n_x.x, n_x.y, fade_xy.y);
  return 2.3 * n_xy;
}
