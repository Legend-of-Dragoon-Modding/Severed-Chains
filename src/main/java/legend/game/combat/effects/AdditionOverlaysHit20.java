package legend.game.combat.effects;

import java.util.Arrays;

public class AdditionOverlaysHit20 {
  public final int index;

  /** ubytes */
  public int unused_00;
  public boolean hitSuccessful_01;
  /** Only ever set to 3 */
  public int borderColoursArrayIndex_02;

  /** shorts */
  public int shadowColour_08;
  public int totalHitFrames_0a;
  public int frameBeginDisplay_0c;
  public int numSuccessFrames_0e;
  public int frameSuccessLowerBound_10;
  public int frameSuccessUpperBound_12;
  // Replaced with references to borderArray_18
  // public AdditionOverlaysBorder0e[] targetBorderArray_14;
  /** 0-13: rotating borders, 14 and 16: target border frames, 15: target border */
  public AdditionOverlaysBorder0e[] borderArray_18 = new AdditionOverlaysBorder0e[17];
  /** byte */
  public boolean isCounter_1c;

  public AdditionOverlaysHit20(final int index) {
    this.index = index;
    Arrays.setAll(this.borderArray_18, AdditionOverlaysBorder0e::new);
  }
}
