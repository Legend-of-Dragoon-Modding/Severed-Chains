package legend.game.combat.effects;

import java.util.Arrays;

public class AdditionStarburstEffect10 implements Effect {
  public int parentIndex_00;
  /** ushort */
  public int rayCount_04;

  /** Set to 0 and never used */
  public int unused_08;
  public AdditionStarburstEffectRay10[] rayArray_0c;

  public AdditionStarburstEffect10(final int rayCount) {
    this.rayCount_04 = rayCount;
    this.rayArray_0c = new AdditionStarburstEffectRay10[rayCount];
    Arrays.setAll(this.rayArray_0c, AdditionStarburstEffectRay10::new);
  }
}
