package legend.core.opengl;

import legend.core.gpu.Bpp;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.ArrayList;
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
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class PolyBuilder {
  private static final int VERTEX_SIZE = POS_SIZE + NORM_SIZE + UV_SIZE + TPAGE_SIZE + CLUT_SIZE + COLOUR_SIZE + FLAGS_SIZE;

  private final String name;
  private final int type;
  private final List<Vertex> vertices = new ArrayList<>();
  private final Vector2i lastVramPos = new Vector2i();
  private final Vector2i lastClut = new Vector2i();
  private Vertex current;
  private Bpp bpp = Bpp.BITS_4;
  private Translucency translucency;

  private int flags;

  public PolyBuilder(final String name) {
    this(name, GL_TRIANGLES);
  }

  public PolyBuilder(final String name, final int glType) {
    this.name = name;
    this.type = glType;
  }

  public int count() {
    return this.vertices.size();
  }

  public PolyBuilder addVertex(final Vector3f pos) {
    this.current = new Vertex(pos.x, pos.y, pos.z);
    this.current.clut = new Vector2i().set(this.lastClut);
    this.current.vramPos = new Vector2i().set(this.lastVramPos);
    this.vertices.add(this.current);
    return this;
  }

  public PolyBuilder addVertex(final float x, final float y, final float z) {
    this.current = new Vertex(x, y, z);
    this.current.clut = new Vector2i().set(this.lastClut);
    this.current.vramPos = new Vector2i().set(this.lastVramPos);
    this.vertices.add(this.current);
    return this;
  }

  public PolyBuilder uv(final Vector2f uv) {
    this.current.uv.set(uv);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder uv(final float u, final float v) {
    this.current.uv.set(u, v);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder vramPos(final Vector2i vramPos) {
    this.current.vramPos.set(vramPos);
    this.lastVramPos.set(vramPos);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder vramPos(final int x, final int y) {
    this.current.vramPos.set(x, y);
    this.lastVramPos.set(x, y);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder clut(final Vector2i clut) {
    this.current.clut.set(clut);
    this.lastClut.set(clut);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder clut(final int x, final int y) {
    this.current.clut.set(x, y);
    this.lastClut.set(x, y);
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder rgb(final Vector3f colour) {
    this.current.colour.set(colour);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public PolyBuilder rgb(final float r, final float g, final float b) {
    this.current.colour.set(r, g, b);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public PolyBuilder monochrome(final float shade) {
    this.current.colour.set(shade);
    this.flags |= TmdObjLoader.COLOURED_FLAG;
    return this;
  }

  public PolyBuilder bpp(final Bpp bpp) {
    this.bpp = bpp;
    this.flags |= TmdObjLoader.TEXTURED_FLAG;
    return this;
  }

  public PolyBuilder translucency(final Translucency translucency) {
    this.translucency = translucency;
    this.flags |= TRANSLUCENT_FLAG;
    return this;
  }

  private void setVertex(final float[] vertices, final int index) {
    final Vertex vert = this.vertices.get(index);
    int i = index * VERTEX_SIZE;
    vertices[i++] = vert.pos.x;
    vertices[i++] = vert.pos.y;
    vertices[i++] = vert.pos.z;
    vertices[i++] = 0.0f; // Vertex index, only used for VDF
    vertices[i++] = 0.0f;
    vertices[i++] = 0.0f;
    vertices[i++] = 0.0f;
    vertices[i++] = vert.uv.x;
    vertices[i++] = vert.uv.y;
    vertices[i++] = makeTpage(vert.vramPos.x, vert.vramPos.y, this.bpp, this.translucency);
    vertices[i++] = makeClut(vert.clut.x, vert.clut.y);
    vertices[i++] = vert.colour.x;
    vertices[i++] = vert.colour.y;
    vertices[i++] = vert.colour.z;
    vertices[i++] = 0.0f;
    vertices[i  ] = this.flags;
  }

  public MeshObj build() {
    // x y z nx ny nz u v tpx tpy clx cly bpp r g b m flags
    final float[] vertices = new float[this.vertices.size() * VERTEX_SIZE];

    for(int i = 0; i < this.vertices.size(); i++) {
      this.setVertex(vertices, i);
    }

    final Mesh mesh = new Mesh(this.type, vertices, this.vertices.size(), (this.flags & TEXTURED_FLAG) != 0, this.translucency != null, this.translucency);

    mesh.attribute(0, 0L, POS_SIZE, VERTEX_SIZE);

    int meshIndex = 1;
    int meshOffset = POS_SIZE;

    mesh.attribute(meshIndex, meshOffset, NORM_SIZE, VERTEX_SIZE);
    meshIndex++;
    meshOffset += NORM_SIZE;

    mesh.attribute(meshIndex, meshOffset, UV_SIZE, VERTEX_SIZE);
    meshIndex++;
    meshOffset += UV_SIZE;

    mesh.attribute(meshIndex, meshOffset, TPAGE_SIZE, VERTEX_SIZE);
    meshIndex++;
    meshOffset += TPAGE_SIZE;

    mesh.attribute(meshIndex, meshOffset, CLUT_SIZE, VERTEX_SIZE);
    meshIndex++;
    meshOffset += CLUT_SIZE;

    mesh.attribute(meshIndex, meshOffset, COLOUR_SIZE, VERTEX_SIZE);
    meshIndex++;
    meshOffset += COLOUR_SIZE;

    mesh.attribute(meshIndex, meshOffset, FLAGS_SIZE, VERTEX_SIZE);

    return new MeshObj(this.name, new Mesh[] { mesh }, (this.flags & TEXTURED_FLAG) != 0);
  }

  private static class Vertex {
    private final Vector3f pos = new Vector3f();
    private final Vector2f uv = new Vector2f();
    private final Vector3f colour = new Vector3f();
    private Vector2i clut;
    private Vector2i vramPos;

    private Vertex(final float x, final float y, final float z) {
      this.pos.set(x, y, z);
    }
  }
}
