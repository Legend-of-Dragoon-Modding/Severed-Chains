package legend.game.submap;

import org.joml.Vector3f;

public class AttachedSobjEffectData40 {
  public int tick_00;
  public boolean shouldRenderTmdDust_04;
  public boolean shouldRenderFootprints_08;
  public boolean shouldRenderOrthoDust_0c;
  public int footprintMode_10;

  public boolean shouldRenderLawPodTrail_18;
  public int textureIndexType1_1c;
  public final Vector3f transfer_1e = new Vector3f();

  public int size_28;
  public int oldFootprintInstantiationInterval_2c;
  public int instantiationIntervalDust_30;
  public int instantiationIntervalFootprints_34;
  /** short */
  public int maxTicks_38;

  public LawPodTrailData18 trailData_3c;

  public AttachedSobjEffectData40() {
    this.tick_00 = 0;
    this.shouldRenderTmdDust_04 = false;
    this.shouldRenderFootprints_08 = false;
    this.shouldRenderOrthoDust_0c = false;
    this.footprintMode_10 = 0;
    this.shouldRenderLawPodTrail_18 = false;
    this.textureIndexType1_1c = 0;
    this.transfer_1e.zero();
    this.size_28 = 0;
    this.oldFootprintInstantiationInterval_2c = 0;
    this.instantiationIntervalDust_30 = 0;
    this.instantiationIntervalFootprints_34 = 0;
    this.maxTicks_38 = 0;
    this.trailData_3c = null;}
}
