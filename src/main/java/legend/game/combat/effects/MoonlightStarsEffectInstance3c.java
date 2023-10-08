package legend.game.combat.effects;

import org.joml.Vector3f;

public class MoonlightStarsEffectInstance3c {
  public final int index;

  /** short */
  public int currentFrame_00;

  /** ubyte */
  public boolean renderStars_03;
  /** 3 shorts */
  public Vector3f translation_04 = new Vector3f();

  public int maxToggleFrameThreshold_36;
  public int toggleOffFrameThreshold_38;

  public MoonlightStarsEffectInstance3c(final int index) {
    this.index = index;
  }
}
