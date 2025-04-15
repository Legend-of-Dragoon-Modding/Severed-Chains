#version 330 core
layout (triangles_adjacency) in;
layout (triangle_strip, max_vertices = 3) out;

in VS_OUT {
  smooth vec2 vertUv;
  flat vec2 vertTpage;
  flat vec2 vertClut;
  flat int vertBpp;
  smooth vec4 vertColour;
  flat int vertFlags;

  flat int translucency;

  flat float widthMultiplier;
  flat int widthMask;
  flat int indexShift;
  flat int indexMask;

  smooth float viewspaceZ;

  smooth float depth;
  smooth float depthOffset;
} vs_out[];

out GS_OUT {
  smooth vec2 vertUv;
  flat vec2 vertTpage;
  flat vec2 vertClut;
  flat int vertBpp;
  smooth vec4 vertColour;
  flat int vertFlags;

  flat int translucency;

  flat float widthMultiplier;
  flat int widthMask;
  flat int indexShift;
  flat int indexMask;

  smooth float depth;
  smooth float depthOffset;
} gs_out;

layout(std140) uniform projectionInfo {
  float znear;
  /** PS1 projection plane distance (H) */
  float zfar;
  float zdiffInv;
  /** 0: ortho, 1: PS1 perspective, 2: modern perspective */
  float projectionMode;
};

void emit(int);

void main() {
  // If any vertex is too close to the camera, cull the whole face
  if(zfar >= vs_out[0].viewspaceZ * 2.0) {
    return;
  }

  if(zfar >= vs_out[2].viewspaceZ * 2.0) {
    return;
  }

  if(zfar >= vs_out[4].viewspaceZ * 2.0) {
    return;
  }

  // Quad, check adjacent vertex
  if((vs_out[1].vertFlags & 0x10) != 0) {
    if(zfar >= vs_out[1].viewspaceZ * 2.0) {
      return;
    }
  }

  emit(0);
  emit(2);
  emit(4);
  EndPrimitive();
}

void emit(int i) {
  gs_out.vertUv = vs_out[i].vertUv;
  gs_out.vertTpage = vs_out[i].vertTpage;
  gs_out.vertClut = vs_out[i].vertClut;
  gs_out.vertBpp = vs_out[i].vertBpp;
  gs_out.vertColour = vs_out[i].vertColour;
  gs_out.vertFlags = vs_out[i].vertFlags;
  gs_out.translucency = vs_out[i].translucency;
  gs_out.widthMultiplier = vs_out[i].widthMultiplier;
  gs_out.widthMask = vs_out[i].widthMask;
  gs_out.indexShift = vs_out[i].indexShift;
  gs_out.indexMask = vs_out[i].indexMask;
  gs_out.depth = vs_out[i].depth;
  gs_out.depthOffset = vs_out[i].depthOffset;

  gl_Position = gl_in[i].gl_Position;
  EmitVertex();
}
