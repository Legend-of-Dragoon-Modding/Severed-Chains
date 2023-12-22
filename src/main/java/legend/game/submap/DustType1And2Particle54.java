package legend.game.submap;

import org.joml.Vector2f;

/** Example - horses running in new game cutscene */
public class DustType1And2Particle54 {
  public short renderMode_00;
  public short textureIndex_02;
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
  public DustType1And2Particle54 next_50;
}
