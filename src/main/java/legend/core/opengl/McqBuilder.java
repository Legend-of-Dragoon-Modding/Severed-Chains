package legend.core.opengl;

import legend.core.gpu.Bpp;
import legend.game.types.McqHeader;
import legend.game.types.Translucency;
import org.joml.Vector2i;

import static legend.core.MathHelper.makeClut;
import static legend.core.MathHelper.makeTpage;
import static legend.core.opengl.TmdObjLoader.CLUT_SIZE;
import static legend.core.opengl.TmdObjLoader.COLOUR_SIZE;
import static legend.core.opengl.TmdObjLoader.FLAGS_SIZE;
import static legend.core.opengl.TmdObjLoader.NORM_SIZE;
import static legend.core.opengl.TmdObjLoader.POS_SIZE;
import static legend.core.opengl.TmdObjLoader.TPAGE_SIZE;
import static legend.core.opengl.TmdObjLoader.TRANSLUCENT_FLAG;
import static legend.core.opengl.TmdObjLoader.UV_SIZE;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class McqBuilder {
  private final String name;
  private final McqHeader mcq;
  private final Vector2i vramOffset = new Vector2i();
  private Translucency translucency;

  private int flags = TmdObjLoader.TEXTURED_FLAG;

  public McqBuilder(final String name, final McqHeader mcq) {
    this.name = name;
    this.mcq = mcq;
  }

  public McqBuilder vramOffset(final int x, final int y) {
    this.vramOffset.set(x, y);
    return this;
  }

  public McqBuilder translucency(final Translucency translucency) {
    this.translucency = translucency;
    this.flags |= TRANSLUCENT_FLAG;
    return this;
  }

  public Obj build() {
    int clutX = this.vramOffset.x + this.mcq.clutX_0c;
    int clutY = this.vramOffset.y + this.mcq.clutY_0e;
    final int width = this.mcq.screenWidth_14;
    final int height = this.mcq.screenHeight_16;
    int u = this.vramOffset.x + this.mcq.u_10;
    int v = this.vramOffset.y + this.mcq.v_12;
    int vramX = u & 0x3c0;
    final int vramY = v & 0x100;
    u = u * 4 & 0xfc;

    int x = 0;
    int y = 0;

    if(this.mcq.magic_00 == McqHeader.MAGIC_2) {
      x += this.mcq.screenOffsetX_28;
      y += this.mcq.screenOffsetY_2a;
    }

    final int chunkCount = width / 16 * height / 16;

    int vertexSize = POS_SIZE;
    vertexSize += NORM_SIZE;
    vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
    vertexSize += COLOUR_SIZE;
    vertexSize += FLAGS_SIZE;

    final float[] vertices = new float[chunkCount * 4 * vertexSize];
    final int[] indices = new int[chunkCount * 6];
    int vertexIndex = 0;
    int indexIndex = 0;
    int indicesVertexIndex = 0;

    //LAB_800e4ad0
    for(int chunkX = 0; chunkX < width; chunkX += 16) {
      //LAB_800e4af0
      //LAB_800e4af4
      for(int chunkY = 0; chunkY < height; chunkY += 16) {
        //LAB_800e4b14
        vertexIndex = this.setVertices(
          vertices, vertexIndex,
          x + chunkX, y + chunkY,
          u, v,
          16, 16,
          clutX, clutY,
          vramX, vramY
        );

        indices[indexIndex++] = indicesVertexIndex + 2;
        indices[indexIndex++] = indicesVertexIndex + 1;
        indices[indexIndex++] = indicesVertexIndex;
        indices[indexIndex++] = indicesVertexIndex + 1;
        indices[indexIndex++] = indicesVertexIndex + 2;
        indices[indexIndex++] = indicesVertexIndex + 3;
        indicesVertexIndex += 4;

        v = v + 16 & 0xf0;

        if(v == 0) {
          u = u + 16 & 0xfc;

          if(u == 0) {
            vramX += 64;
          }
        }

        clutY = clutY + 1 & 0xff;

        if(clutY == 0) {
          clutX += 16;
        }

        clutY |= vramY;
      }
    }

    final Mesh mesh = new Mesh(GL_TRIANGLES, vertices, indices, true, this.translucency != null, this.translucency);

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

  private int setVertices(final float[] vertices, int offset, final float x, final float y, final float u, final float v, final float w, final float h, final int clx, final int cly, final int tpx, final int tpy) {
    offset = this.setVertex(vertices, offset, x, y, u, v, clx, cly, tpx, tpy);
    offset = this.setVertex(vertices, offset, x, y + h, u, v + h, clx, cly, tpx, tpy);
    offset = this.setVertex(vertices, offset, x + w, y, u + w, v, clx, cly, tpx, tpy);
    offset = this.setVertex(vertices, offset, x + w, y + h, u + w, v + h, clx, cly, tpx, tpy);
    return offset;
  }

  private int setVertex(final float[] vertices, int offset, final float x, final float y, final float u, final float v, final int clx, final int cly, final int tpx, final int tpy) {
    vertices[offset++] = x;
    vertices[offset++] = y;
    vertices[offset++] = 0.0f; // z
    vertices[offset++] = 0.0f; // Vertex index, only used for VDF
    vertices[offset++] = 0.0f; // \
    vertices[offset++] = 0.0f; // | normals
    vertices[offset++] = 0.0f; // /
    vertices[offset++] = u;
    vertices[offset++] = v;
    vertices[offset++] = makeTpage(tpx, tpy, Bpp.BITS_4, null);
    vertices[offset++] = makeClut(clx, cly);
    vertices[offset++] = 0; // \
    vertices[offset++] = 0; // | colour
    vertices[offset++] = 0; // |
    vertices[offset++] = 0; // /
    vertices[offset++] = this.flags;
    return offset;
  }
}
