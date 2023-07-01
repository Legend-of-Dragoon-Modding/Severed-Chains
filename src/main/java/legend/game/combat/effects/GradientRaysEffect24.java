package legend.game.combat.effects;

import java.util.Arrays;

public class GradientRaysEffect24 implements BttlScriptData6cSubBase1 {
  public final GradientRaysEffectInstance04[] rays_00;
  public final int count_04;
  public int _08;
  public int _0c;
  public int _10;
  public int _14;
  public int flags_18;
  public int type_1c;
  public int projectionPlaneDistanceDiv4_20;

  public GradientRaysEffect24(final int count) {
    this.rays_00 = new GradientRaysEffectInstance04[count];
    this.count_04 = count;

    Arrays.setAll(this.rays_00, i -> new GradientRaysEffectInstance04());
  }
}
