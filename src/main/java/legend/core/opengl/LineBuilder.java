package legend.core.opengl;

import legend.game.types.Translucency;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static legend.core.opengl.TmdObjLoader.CLUT_SIZE;
import static legend.core.opengl.TmdObjLoader.COLOUR_SIZE;
import static legend.core.opengl.TmdObjLoader.FLAGS_SIZE;
import static legend.core.opengl.TmdObjLoader.NORM_SIZE;
import static legend.core.opengl.TmdObjLoader.POS_SIZE;
import static legend.core.opengl.TmdObjLoader.TPAGE_SIZE;
import static legend.core.opengl.TmdObjLoader.TRANSLUCENT_FLAG;
import static legend.core.opengl.TmdObjLoader.UV_SIZE;
import static org.lwjgl.opengl.GL11C.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11C.GL_LINE_STRIP;

public class LineBuilder {
  private final String name;
  private final List<Vector3f> pos = new ArrayList<>();
  private Translucency translucency;
  private int flags;
  private boolean closed;

  public LineBuilder(final String name) {
    this.name = name;
  }

  public LineBuilder pos(final Vector3f pos) {
    this.pos.add(pos);
    return this;
  }

  public LineBuilder pos(final float x, final float y, final float z) {
    return this.pos(new Vector3f(x, y, z));
  }

  public LineBuilder translucency(final Translucency translucency) {
    this.translucency = translucency;
    this.flags |= TRANSLUCENT_FLAG;
    return this;
  }

  /** Makes it so you only have to specify 4 vertices for a box */
  public LineBuilder closed() {
    this.closed = true;
    return this;
  }

  public MeshObj build() {
    int vertexSize = POS_SIZE;
    vertexSize += NORM_SIZE;
    vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
    vertexSize += COLOUR_SIZE;
    vertexSize += FLAGS_SIZE;

    // x y z nx ny nz u v tpx tpy clx cly bpp r g b m flags
    final float[] vertices = new float[vertexSize * this.pos.size()];
    for(int i = 0; i < this.pos.size(); i++) {
      final Vector3f pos = this.pos.get(i);
      vertices[i * vertexSize    ] = pos.x;
      vertices[i * vertexSize + 1] = pos.y;
      vertices[i * vertexSize + 2] = pos.z;
      vertices[(i + 1) * vertexSize - 1] = this.flags;
    }

    final Mesh mesh = new Mesh(this.closed ? GL_LINE_LOOP : GL_LINE_STRIP, vertices, this.pos.size());

    mesh.attribute(0, 0L, 3, vertexSize);

    int meshIndex = 1;
    int meshOffset = 3;

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

    final Mesh[] meshes = new Mesh[Translucency.values().length + 1];

    if(this.translucency == null) {
      meshes[0] = mesh;
    } else {
      meshes[this.translucency.ordinal() + 1] = mesh;
    }

    return new MeshObj(this.name, meshes);
  }
}
