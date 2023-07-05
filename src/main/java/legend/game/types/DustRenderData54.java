package legend.game.types;

import legend.core.gte.SVECTOR;

/** Example - horses running in new game cutscene */
public class DustRenderData54 {
  public short renderMode_00;
  public short textureIndex_02;
  public short _04;
  public short _06;
  public int _08;
  public int _0c;
  public int _10;

  public int x_18;
  public int y_1c;
  public final SVECTOR v0_20 = new SVECTOR();
  public final SVECTOR v1_28 = new SVECTOR();
  public final SVECTOR v2_30 = new SVECTOR();
  public final SVECTOR v3_38 = new SVECTOR();
  /** 16.16 fixed-point */
  public int colourStep_40;
  /** 16.16 fixed-point */
  public int colourAccumulator_44;
  public int colour_48;
  public int z_4c;
  public DustRenderData54 next_50;
}
