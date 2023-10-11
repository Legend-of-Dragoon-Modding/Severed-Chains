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
  /** 1.0 x background + 0.0 x foreground */
  FULL_BACKGROUND,
  /** 0.75 x background + 1.0 x foreground */
  TQUATER_B_FOREGROUND,
  /** 0.50 x background + 1.0 x foreground */
  HALF_B_FOREGROUND,
  /** 0.25 x background + 1.0 x foreground */
  QUARTER_B_FOREGROUND,
  /** 0.0 x background + 1.0 x foreground */
  FULL_FOREGROUND,
  /** 0.25 x background + 0.25 x foreground */
  QUARTER_B_QUARTER_F,
  /** 0.75 x background + 0.75 x foreground */
  TQUARTER_B_TQUARTER_F,
  ;

  public static final Translucency[] FOR_RENDERING = {HALF_B_PLUS_HALF_F, B_PLUS_F, B_MINUS_F};

  /** NOTE: returns null if value is -1 */
  @Nonnull
  public static Translucency of(final int value) {
    if(value == -1) {
      return null;
    }

    return values()[value];
  }
}
