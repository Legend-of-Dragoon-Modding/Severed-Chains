package legend.game.combat.effects;

import java.util.Arrays;

public class StarChildrenImpactEffect20 implements Effect {
  // public int count_00;
  public int currentFrame_04;
  public final StarChildrenImpactEffectInstancea8[] impactArray_08;

  public StarChildrenImpactEffect20(final int count) {
    this.impactArray_08 = new StarChildrenImpactEffectInstancea8[count];
    Arrays.setAll(this.impactArray_08, StarChildrenImpactEffectInstancea8::new);
  }
}
