#version 330 core

in vec2 vertUv;

uniform sampler2D texPos;
uniform sampler2D texNorm;
uniform sampler2D texNoise;

uniform vec2 screenSize;
uniform vec3 samples[64];

// parameters (you'd probably want to use them as uniforms to more easily tweak the effect)
int kernelSize = 64;
float invKernelSize = 1.0 / float(kernelSize);
float radius = 0.1;
float bias = 0.00025;

layout(std140) uniform transforms {
  mat4 camera;
  mat4 projection;
};

layout(location = 0) out float outColour;

void main() {
  vec3 viewPos = texture(texPos, vertUv).xyz;
  vec3 viewNormal = normalize(texture(texNorm, vertUv).rgb);

  // calculate a random offset using the noise texture sample
  // this will be applied as rotation to all samples for our current fragments
  vec3 randomVec = normalize(texture(texNoise, vertUv * (screenSize / 4)).xyz);

  // apply the Gram-Schmidt process to calculate the TBN matrix with a random offset
  vec3 tangent = normalize(randomVec - viewNormal * dot(randomVec, viewNormal));
  vec3 bitangent = cross(viewNormal, tangent);
  mat3 TBN = mat3(tangent, bitangent, viewNormal);

  float occlusion_factor = 0.0;
  for(int i = 0; i < kernelSize; i++) {
    vec3 samplePos = TBN * samples[i];

    // calculate sampling point in viewspace
    samplePos = viewPos + samplePos * radius;
    
    vec4 offset = vec4(samplePos, 1.0);
    offset = projection * offset;
    offset.xy /= offset.w;
    offset.xy = offset.xy * 0.5 + 0.5;

    float geometryDepth = texture(texPos, offset.xy).z;

    float rangeCheck = smoothstep(0.0, 1.0, radius / abs(viewPos.z - geometryDepth));

    occlusion_factor += (geometryDepth >= samplePos.z + bias ? 1.0 : 0.0) * rangeCheck;
  }

  // we will devide the accmulated occlusion by the number of samples to get the average occlusion value.
  float average_occlusion_factor = occlusion_factor * invKernelSize;

  float visibility_factor = 1.0 - average_occlusion_factor;

  // We can raise the visibility factor to a power to make the transition
  // more sharp. Experiment with the value of this power to see what works best for you.
  // Even after raising visibility to a power > 1, the range still remains between [0.0, 1.0].
  //visibility_factor = pow(visibility_factor, 2.0);

  outColour = 1.0 - visibility_factor;
}
