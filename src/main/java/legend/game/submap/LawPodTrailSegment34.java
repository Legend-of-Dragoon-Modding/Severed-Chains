package legend.game.submap;

import legend.game.PoolList;
import org.joml.Vector3f;

public class LawPodTrailSegment34 implements PoolList.Usable {
  private boolean used;

  /** short */
  public int tick_00;

  public int tpage_04;
  public Vector3f colourStep_08 = new Vector3f();
  public Vector3f colour_14 = new Vector3f();
  public float z_20;
  public TrailSegmentVertices14 originVerts01_24 = new TrailSegmentVertices14();
  public TrailSegmentVertices14 endpointVerts23_28 = new TrailSegmentVertices14();
  public LawPodTrailData18 trailData_2c;
  // public LawPodTrailSegment34 next_30;

  public LawPodTrailSegment34() {
    this.use();
  }

  @Override
  public boolean used() {
    return this.used;
  }

  @Override
  public void use() {
    this.used = true;
  }

  @Override
  public void free() {
    this.used = false;
  }
}
