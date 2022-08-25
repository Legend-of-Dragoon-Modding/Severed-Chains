package legend.game.tmd;

public class Polygon {
  public final Vertex[] vertices;
  public int colour;
  public int clut;
  public int tpage;

  public Polygon(final int vertexCount) {
    this.vertices = new Vertex[vertexCount];

    for(int i = 0; i < vertexCount; i++) {
      this.vertices[i] = new Vertex();
    }
  }
}
