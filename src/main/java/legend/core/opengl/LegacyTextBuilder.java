package legend.core.opengl;

import legend.core.gpu.Bpp;

import static legend.core.MathHelper.makeClut;
import static legend.core.MathHelper.makeTpage;
import static legend.core.opengl.TmdObjLoader.CLUT_SIZE;
import static legend.core.opengl.TmdObjLoader.COLOURED_FLAG;
import static legend.core.opengl.TmdObjLoader.COLOUR_SIZE;
import static legend.core.opengl.TmdObjLoader.FLAGS_SIZE;
import static legend.core.opengl.TmdObjLoader.NORM_SIZE;
import static legend.core.opengl.TmdObjLoader.POS_SIZE;
import static legend.core.opengl.TmdObjLoader.TEXTURED_FLAG;
import static legend.core.opengl.TmdObjLoader.TPAGE_SIZE;
import static legend.core.opengl.TmdObjLoader.UV_SIZE;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class LegacyTextBuilder {
  private static final int CHAR_COUNT = 0x100;

  private final String name;

  public LegacyTextBuilder(final String name) {
    this.name = name;
  }

  private int setVertex(int offset, final float[] vertices, final float x, final float y, final float u, final float v) {
    vertices[offset++] = x;
    vertices[offset++] = y;
    vertices[offset++] = 0.0f;
    vertices[offset++] = 0.0f; // Vertex index, only used for VDF
    vertices[offset++] = 0.0f;
    vertices[offset++] = 0.0f;
    vertices[offset++] = 0.0f;
    vertices[offset++] = u / 256.0f;
    vertices[offset++] = v / 256.0f;
    vertices[offset++] = makeTpage(832, 256, Bpp.BITS_24, null);
    vertices[offset++] = makeClut(832, 480);
    vertices[offset++] = 1.0f; // r
    vertices[offset++] = 1.0f; // g
    vertices[offset++] = 1.0f; // b
    vertices[offset++] = 0.0f; // m
    vertices[offset++] = TEXTURED_FLAG | COLOURED_FLAG;
    return offset;
  }

  private int setVertices(int offset, final float[] vertices, final int chr) {
    final int u = (chr & 0xf) * 16;
    final int v = chr / 16 * 16;
    offset = this.setVertex(offset, vertices, 0.0f,  0.0f, u, v);
    offset = this.setVertex(offset, vertices, 0.0f, 16.0f, u, v + 16.0f);
    offset = this.setVertex(offset, vertices, 8.0f,  0.0f, u + 8.0f, v);
    offset = this.setVertex(offset, vertices, 8.0f, 16.0f, u + 8.0f, v + 16.0f);
    return offset;
  }

  public MeshObj build() {
    // x y z nx ny nz u v tpx tpy clx cly bpp r g b m flags
    int vertexSize = POS_SIZE;
    vertexSize += NORM_SIZE;
    vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
    vertexSize += COLOUR_SIZE;
    vertexSize += FLAGS_SIZE;

    final float[] vertices = new float[CHAR_COUNT * 4 * vertexSize];
    int offset = 0;
    for(int chr = 0; chr < CHAR_COUNT; chr++) {
      offset = this.setVertices(offset, vertices, chr);
    }

    final Mesh mesh = new Mesh(GL_TRIANGLE_STRIP, vertices, CHAR_COUNT * 4);

    mesh.attribute(0, 0L, POS_SIZE, vertexSize);

    int meshIndex = 1;
    int meshOffset = POS_SIZE;

    mesh.attribute(meshIndex, meshOffset, NORM_SIZE, vertexSize);
    meshIndex++;
    meshOffset += NORM_SIZE;

    mesh.attribute(meshIndex, meshOffset, UV_SIZE, vertexSize);
    meshIndex++;
    meshOffset += UV_SIZE;

    mesh.attribute(meshIndex, meshOffset, TPAGE_SIZE, vertexSize);
    meshIndex++;
    meshOffset += TPAGE_SIZE;

    mesh.attribute(meshIndex, meshOffset, CLUT_SIZE, vertexSize);
    meshIndex++;
    meshOffset += CLUT_SIZE;

    mesh.attribute(meshIndex, meshOffset, COLOUR_SIZE, vertexSize);
    meshIndex++;
    meshOffset += COLOUR_SIZE;

    mesh.attribute(meshIndex, meshOffset, FLAGS_SIZE, vertexSize);

    return new MeshObj(this.name, new Mesh[] { mesh }, true);
  }
}
