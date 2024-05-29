package legend.game.submap;

import legend.core.gte.MV;
import org.joml.Vector2f;

public class SavePointRenderData44 {
  public final Vector2f vert0_00 = new Vector2f();
  public final Vector2f vert1_08 = new Vector2f();
  public final Vector2f vert2_10 = new Vector2f();
  public final Vector2f vert3_18 = new Vector2f();
  public float screenOffsetX_20;
  public float screenOffsetY_24;
  public float rotation_28;
  public float fadeAmount_2c;
//  /** 16.16 fixed-point */
//  public int fadeAccumulator_30;
  public float colour_34;
  /** 0: fade in, 1: fade out */
  public short fadeState_38;

  public float z_40;

  public final MV transforms = new MV();
}
