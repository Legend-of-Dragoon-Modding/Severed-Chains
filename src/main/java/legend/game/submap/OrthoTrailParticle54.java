package legend.game.submap;

import org.joml.Vector2f;

/** Used for ortho quad trail attached sobj effect particles (footprints and some kinds of dust). */
public class OrthoTrailParticle54 {
  public short renderMode_00;
  public int textureIndex_02;
  public short tick_04;
  public short maxTicks_06;
  public float size_08;
  public float sizeStep_0c;
  // Appears to just be a second size attribute
  // public float _10;

  public int x_18;
  public int y_1c;
  public final Vector2f sxy0_20 = new Vector2f();
  public float z0_26;
  public final Vector2f sxy1_28 = new Vector2f();
  public float z1_2e;
  public final Vector2f sxy2_30 = new Vector2f();
  public final Vector2f sxy3_38 = new Vector2f();
  /** 16.16 fixed-point */
  public int stepBrightness_40;
  /** 16.16 fixed-point */
  public int brightnessAccumulator_44;
  public int brightness_48;
  public float z_4c;
  public OrthoTrailParticle54 next_50;
}
