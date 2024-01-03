package legend.game.submap;

import org.joml.Vector2f;

public class TrailSegmentVertices14 {
  public boolean used;

  public final Vector2f vert0_00 = new Vector2f();

  public final Vector2f vert1_08 = new Vector2f();

  // public TrailSegmentVertices14 next_10;

  public void set(final TrailSegmentVertices14 other) {
    this.vert0_00.set(other.vert0_00);
    this.vert1_08.set(other.vert1_08);
  }

  public void zero() {
    this.vert0_00.set(0.0f, 0.0f);
    this.vert1_08.set(0.0f, 0.0f);
  }
}
