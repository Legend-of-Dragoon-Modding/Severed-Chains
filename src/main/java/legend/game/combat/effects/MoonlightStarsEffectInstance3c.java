package legend.game.combat.effects;

import legend.core.gte.SVECTOR;

public class MoonlightStarsEffectInstance3c {
  public final int index;

  /** short */
  public int currentFrame_00;

  /** ubyte */
  public boolean renderStars_03;
  /** 3 shorts */
  public SVECTOR translation_04 = new SVECTOR();

  public int maxFrameToggleThreshold_36;
  public int toggleOffFrameThreshold_38;

  public MoonlightStarsEffectInstance3c(final int index) {
    this.index = index;
  }
}
