package legend.game.combat.types;

import java.util.Arrays;

public class FloatingNumberC4 {
  public int state_00;
  public int flags_02;
  /** Must be the bobj that the floating number is attached to */
  public int bobjIndex_04;
  public boolean translucent_08;
  /**
   * TODO not 100% sure if these are actually backwards (BGR), but pretty sure
   */
  public int b_0c;
  public int g_0d;
  public int r_0e;

  public int _10;
  public int _14;
  public int _18;
  public int x_1c;
  public int y_20;
  public final FloatingNumberC4Sub20[] digits_24 = new FloatingNumberC4Sub20[10];

  public FloatingNumberC4() {
    Arrays.setAll(this.digits_24, i -> new FloatingNumberC4Sub20());
  }
}
