package legend.game.types;

import javax.annotation.Nonnull;

public enum Translucency {
  /** 0.5 x background + 0.5 x foreground */
  HALF_B_PLUS_HALF_F,
  /** 1.0 x background + 1.0 x foreground */
  B_PLUS_F,
  /** 1.0 x background - 1.0 x foreground */
  B_MINUS_F,
  /** 1.0 x background + 0.25 x foreground */
  B_PLUS_QUARTER_F,
  ;

  /** NOTE: returns null if value is -1 */
  @Nonnull
  public static Translucency of(final int value) {
    if(value == -1) {
      return null;
    }

    return values()[value];
  }
}
