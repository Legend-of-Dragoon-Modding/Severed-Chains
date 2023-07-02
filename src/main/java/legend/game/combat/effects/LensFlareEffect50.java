package legend.game.combat.effects;

import java.util.Arrays;

public class LensFlareEffect50 implements Effect {
  public int _00;
  public int _02;
  public final int[] u_04 = new int[5];
  public final int[] v_0e = new int[5];
  public final int[] w_18 = new int[5];
  public final int[] h_22 = new int[5];
  public final int[] clut_2c = new int[5];

  public final LensFlareEffectInstance3c[] instances_38 = new LensFlareEffectInstance3c[5];
  public int bobjIndex_3c;
  public short x_40;
  public short y_42;
  public short z_44;

  public short brightness_48;
  public short shouldRender_4a;

  public LensFlareEffect50() {
    Arrays.setAll(this.instances_38, i -> new LensFlareEffectInstance3c());
  }
}
