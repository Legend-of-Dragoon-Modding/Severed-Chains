package legend.core.opengl;

import legend.core.gpu.Bpp;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.MathHelper.makeClut;
import static legend.core.MathHelper.makeTpage;
import static legend.core.opengl.TmdObjLoader.CLUT_SIZE;
import static legend.core.opengl.TmdObjLoader.COLOUR_SIZE;
import static legend.core.opengl.TmdObjLoader.FLAGS_SIZE;
import static legend.core.opengl.TmdObjLoader.NORM_SIZE;
import static legend.core.opengl.TmdObjLoader.POS_SIZE;
import static legend.core.opengl.TmdObjLoader.TEXTURED_FLAG;
import static legend.core.opengl.TmdObjLoader.TPAGE_SIZE;
import static legend.core.opengl.TmdObjLoader.TRANSLUCENT_FLAG;
import static legend.core.opengl.TmdObjLoader.UV_SIZE;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class QuadBuilder {
  private final String name;
  private Translucency translucency;
  private boolean disableBackfaceCulling;
  private int flags;

  private final List<Quad> quads = new ArrayList<>();
  private Quad current;

  public QuadBuilder(final String name) {
    this.name = name;
  }

  public int currentQuadIndex() {
    return this.quads.size();
  }

  public QuadBuilder add() {
    this.current = new Quad();
    this.quads.add(this.current);
    return this;
  }

  private void addFirstQuad() {
    if(this.current == null) {
      this.add();
    }
  }

  public QuadBuilder pos(final Vector3f pos) {
    this.addFirstQuad();
    this.current.pos.set(pos);
    return this;
  }

  public QuadBuilder pos(final float x, final float y, final float z) {
    this.addFirstQuad();
    this.current.pos.set(x, y, z);
    return this;
  }

  /** Sets both position and UV size */
  public QuadBuilder size(final Vector2f size) {
    this.addFirstQuad();
    this.current.posSize.set(size);
    this.current.uvSize.set(size);
    return this;
  }

  /** Sets both position and UV size */
  public QuadBuilder size(final float w, final float h) {
    this.addFirstQuad();
    this.current.posSize.set(w, h);
    this.current.uvSize.set(w, h);
    return this;
  }

  public QuadBuilder posSize(final Vector2f size) {
    this.addFirstQuad();
    this.current.posSize.set(size);
    return this;
  }

  public QuadBuilder posSize(final float w, final float h) {
    this.addFirstQuad();
    this.current.posSize.set(w, h);
    return this;
  }

  public QuadBuilder uvSize(final Vector2f size) {
    this.addFirstQuad();
    this.current.uvSize.set(size);
    return this;
  }

  public QuadBuilder uvSize(final float w, final float h) {
    this.addFirstQuad();
    this.current.uvSize.set(w, h);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder uv(final Vector2f uv) {
    this.addFirstQuad();
    this.current.uv.set(uv);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder uv(final float u, final float v) {
    this.addFirstQuad();
    this.current.uv.set(u, v);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder vramPos(final Vector2i vramPos) {
    this.addFirstQuad();
    this.current.vramPos.set(vramPos);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder vramPos(final int x, final int y) {
    this.addFirstQuad();
    this.current.vramPos.set(x, y);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder clut(final Vector2i clut) {
    this.addFirstQuad();
    this.current.clut.set(clut);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder clut(final int x, final int y) {
    this.addFirstQuad();
    this.current.clut.set(x, y);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder rgb(final Vector3f colour) {
    this.addFirstQuad();
    Arrays.fill(this.current.colour, colour);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public QuadBuilder rgb(final int vertex, final Vector3f colour) {
    this.addFirstQuad();
    this.current.colour[vertex].set(colour);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public QuadBuilder rgb(final float r, final float g, final float b) {
    this.addFirstQuad();

    for(int i = 0; i < this.current.colour.length; i++) {
      this.current.colour[i].set(r, g, b);
    }

    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public QuadBuilder rgb(final int vertex, final float r, final float g, final float b) {
    this.addFirstQuad();
    this.current.colour[vertex].set(r, g, b);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public QuadBuilder monochrome(final float shade) {
    this.addFirstQuad();

    for(int i = 0; i < this.current.colour.length; i++) {
      this.current.colour[i].set(shade);
    }

    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public QuadBuilder monochrome(final int vertex, final float shade) {
    this.addFirstQuad();
    this.current.colour[vertex].set(shade);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public QuadBuilder bpp(final Bpp bpp) {
    this.addFirstQuad();
    this.current.bpp = bpp;
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public QuadBuilder translucency(final Translucency translucency) {
    this.addFirstQuad();
    this.translucency = translucency;
    this.flags |= TRANSLUCENT_FLAG;
    return this;
  }

  public QuadBuilder disableBackfaceCulling() {
    this.addFirstQuad();
    this.disableBackfaceCulling = true;
    return this;
  }

  private int setVertex(int offset, final float[] vertices, final float x, final float y, final float z, final float u, final float v, final int tpx, final int tpy, final int clx, final int cly, final Bpp bpp, final float r, final float g, final float b, final float flags) {
    vertices[offset++] = x;
    vertices[offset++] = y;
    vertices[offset++] = z;
    vertices[offset++] = 0.0f; // Vertex index, only used for VDF
    vertices[offset++] = 0.0f;
    vertices[offset++] = 0.0f;
    vertices[offset++] = 0.0f;
    vertices[offset++] = u;
    vertices[offset++] = v;
    vertices[offset++] = makeTpage(tpx, tpy, bpp, this.translucency);
    vertices[offset++] = makeClut(clx, cly);
    vertices[offset++] = r;
    vertices[offset++] = g;
    vertices[offset++] = b;
    vertices[offset++] = 0.0f;
    vertices[offset++] = flags;
    return offset;
  }

  private int setVertices(int offset, final float[] vertices, final Quad quad) {
    final float x0 = quad.pos.x;
    final float y0 = quad.pos.y;
    final float x1 = x0 + quad.posSize.x;
    final float y1 = y0 + quad.posSize.y;
    final float z = quad.pos.z;
    final float u0 = quad.uv.x;
    final float v0 = quad.uv.y;
    final float u1 = u0 + quad.uvSize.x;
    final float v1 = v0 + quad.uvSize.y;
    final int tpx = quad.vramPos.x;
    final int tpy = quad.vramPos.y;
    final int clx = quad.clut.x;
    final int cly = quad.clut.y;

    offset = this.setVertex(offset, vertices, x0, y0, z, u0, v0, tpx, tpy, clx, cly, quad.bpp, quad.colour[0].x, quad.colour[0].y, quad.colour[0].z, this.flags);
    offset = this.setVertex(offset, vertices, x0, y1, z, u0, v1, tpx, tpy, clx, cly, quad.bpp, quad.colour[1].x, quad.colour[1].y, quad.colour[1].z, this.flags);
    offset = this.setVertex(offset, vertices, x1, y0, z, u1, v0, tpx, tpy, clx, cly, quad.bpp, quad.colour[2].x, quad.colour[2].y, quad.colour[2].z, this.flags);
    offset = this.setVertex(offset, vertices, x1, y1, z, u1, v1, tpx, tpy, clx, cly, quad.bpp, quad.colour[3].x, quad.colour[3].y, quad.colour[3].z, this.flags);
    return offset;
  }

  public MeshObj build() {
    int vertexSize = POS_SIZE;
    vertexSize += NORM_SIZE;
    vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
    vertexSize += COLOUR_SIZE;
    vertexSize += FLAGS_SIZE;

    final float[] vertices = new float[this.quads.size() * 4 * vertexSize];
    int offset = 0;
    for(final Quad quad : this.quads) {
      offset = this.setVertices(offset, vertices, quad);
    }

    final Mesh mesh = new Mesh(GL_TRIANGLE_STRIP, vertices, this.quads.size() * 4, (this.flags & TEXTURED_FLAG) != 0, this.translucency != null, this.translucency);

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

    return new MeshObj(this.name, new Mesh[] { mesh }, (this.flags & TEXTURED_FLAG) != 0 && !this.disableBackfaceCulling);
  }

  private static class Quad {
    private final Vector3f pos = new Vector3f();
    private final Vector2f posSize = new Vector2f();
    private final Vector2f uv = new Vector2f();
    private final Vector2f uvSize = new Vector2f();
    private final Vector2i vramPos = new Vector2i();
    private final Vector2i clut = new Vector2i();
    private final Vector3f[] colour = {new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f()};
    private Bpp bpp = Bpp.BITS_4;
  }
}
