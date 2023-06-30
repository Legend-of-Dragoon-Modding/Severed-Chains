package legend.game.combat.effects;

import java.util.Arrays;

public class SpTextEffect40 {
  public int _00;
  public int _01;
  public int _02;

  public int _04;
  public int _08;
  public int _0c;
  public int _10;

  public int _1c;
  public int _20;

  public int _2c;
  public int _30;

  public final SpTextEffectTrail10[] charArray_3c = new SpTextEffectTrail10[8];

  public SpTextEffect40() {
    Arrays.setAll(this.charArray_3c, i -> new SpTextEffectTrail10());
  }
}
