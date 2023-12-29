package legend.game.submap;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Texture;

public class EnvironmentRenderingMetrics24 {
  public int tpage_04;

  public int r_0c;
  public int g_0d;
  public int b_0e;

  public int x_10;
  public int y_12;

  public int u_14;
  public int v_15;
  public int clut_16;
  public int w_18;
  public int h_1a;
  /** Part of the render X calculation */
  public int offsetX_1c;
  /** Part of the render Y calculation */
  public int offsetY_1e;
  public int z_20;
  /** Flags, but lower 0x3fff is Z? Flags 0x4000 and 0x8000 seen */
  public int flags_22;

  public Obj obj;
  public Texture texture;
  public final MV transforms = new MV();
}
