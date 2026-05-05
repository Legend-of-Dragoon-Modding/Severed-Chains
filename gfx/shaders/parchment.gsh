#version 330 core
layout (triangles) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
  smooth vec2 vertUv;
  smooth vec4 vertColour;

  smooth float depth;
  smooth float depthOffset;

  smooth vec2 screenspaceSize;
} vs_out[];

out GS_OUT {
  smooth vec2 vertUv;
  smooth vec4 vertColour;

  smooth float depth;
  smooth float depthOffset;

  flat vec2 screenspaceSize;
} gs_out;

layout(std140) uniform projectionInfo {
  float znear;
  /** PS1 projection plane distance (H) */
  float zfar;
  float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  float projectionMode;
  vec2 screenSize;
};

void emit(int, float, float);

void main() {
  float width = max(abs(vs_out[0].screenspaceSize.x - vs_out[1].screenspaceSize.x), abs(vs_out[0].screenspaceSize.x - vs_out[2].screenspaceSize.x));
  float height = max(abs(vs_out[0].screenspaceSize.y - vs_out[1].screenspaceSize.y), abs(vs_out[0].screenspaceSize.y - vs_out[2].screenspaceSize.y));

  emit(0, width, height);
  emit(1, width, height);
  emit(2, width, height);
  EndPrimitive();
}

void emit(int i, float width, float height) {
  gs_out.vertUv = vs_out[i].vertUv;
  gs_out.vertColour = vs_out[i].vertColour;
  gs_out.depth = vs_out[i].depth;
  gs_out.depthOffset = vs_out[i].depthOffset;
  gs_out.screenspaceSize = vec2(width, height);

  gl_Position = gl_in[i].gl_Position;

  EmitVertex();
}
