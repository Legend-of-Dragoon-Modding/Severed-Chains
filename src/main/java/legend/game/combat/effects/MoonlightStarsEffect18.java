package legend.game.combat.effects;

import java.util.Arrays;

public class MoonlightStarsEffect18 implements Effect {
  /** ushort */
  public int count_00;

  public final SpriteMetrics08 metrics_04 = new SpriteMetrics08();
  public MoonlightStarsEffectInstance3c[] starArray_0c;

  public MoonlightStarsEffect18(final int count) {
    this.count_00 = count;
    this.starArray_0c = new MoonlightStarsEffectInstance3c[count];
    Arrays.setAll(this.starArray_0c, MoonlightStarsEffectInstance3c::new);
  }
}
