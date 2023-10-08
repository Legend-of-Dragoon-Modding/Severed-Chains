package legend.game.combat.effects;

public class AdditionStarburstEffectRay10 {
  public final int index;

  /** byte */
  public boolean renderRay_00;

  public float angle_02;
  /** short; Set to 0x10 and never used */
  public int unused_04;
  public short endpointTranslationMagnitude_06;
  public short endpointTranslationMagnitudeVelocity_08;
  public float angleModifier_0a;
  /** Set to 0 and never used */
  public int unused_0c;

  public AdditionStarburstEffectRay10(final int index) {
    this.index = index;
  }
}
