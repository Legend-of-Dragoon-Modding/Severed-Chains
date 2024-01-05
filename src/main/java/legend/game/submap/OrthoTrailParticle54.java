package legend.game.submap;

import legend.core.gte.MV;
import legend.game.PoolList;

/** Used for ortho quad trail attached sobj effect particles (footprints and some kinds of dust). */
public class OrthoTrailParticle54 implements PoolList.Usable {
  private boolean used;
  public MV transforms = new MV();

  // public int renderMode_00;
  public int tick_04;
  public int maxTicks_06;
  // Appears to just be a second size attribute
  // public float _10;

  /** 16.16 fixed-point */
  public float stepBrightness_40;
  // /** 16.16 fixed-point */
  // public int brightnessAccumulator_44; // No longer necessary
  public float brightness_48;
  // public OrthoTrailParticle54 next_50;

  public OrthoTrailParticle54() {
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
