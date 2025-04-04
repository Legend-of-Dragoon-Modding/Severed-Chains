package legend.core.opengl;

import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class TmdObjLoaderMesh {
  public final float[] vertices;
  public final int[] indices;
  public final boolean textured;
  public final boolean translucent;
  public final Translucency translucency;

  public int vertexIndex;
  public int vertexOffset;
  public int indexOffset;

  public TmdObjLoaderMesh(final int vertexCount, final int indexCount, final boolean textured, final boolean translucent, @Nullable final Translucency translucency) {
    this.vertices = new float[vertexCount];
    this.indices = new int[indexCount * 2]; // Double size for TRIANGLES_ADJACENCY
    this.textured = textured;
    this.translucent = translucent;
    this.translucency = translucency;
  }
}
