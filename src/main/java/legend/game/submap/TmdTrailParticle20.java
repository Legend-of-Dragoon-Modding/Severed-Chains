package legend.game.submap;

import legend.game.PoolList;
import org.joml.Vector3f;

/**
 * Used for dust trail attached sobj effect particles that use TMD models.
 */
public class TmdTrailParticle20 implements PoolList.Usable {
  private boolean used;

  public int tick_00;
  public float stepSize_04;
  public float size_08;
  public final Vector3f transfer = new Vector3f();
  public int maxTicks_18;
  //public TmdTrailParticle20 next_1c;

  public TmdTrailParticle20() {
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
