package legend.game.combat.effects;

import java.util.Arrays;

public class StarChildrenMeteorEffect10 implements BttlScriptData6cSubBase1 {
  public final int count_00;
  public final SpriteMetrics08 metrics_04 = new SpriteMetrics08();
  public StarChildrenMeteorEffectInstance10[] meteorArray_0c;

  public StarChildrenMeteorEffect10(final int count) {
    this.count_00 = count;
    this.meteorArray_0c = new StarChildrenMeteorEffectInstance10[count];
    Arrays.setAll(this.meteorArray_0c, StarChildrenMeteorEffectInstance10::new);
  }
}
