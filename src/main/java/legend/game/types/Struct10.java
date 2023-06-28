package legend.game.types;

import java.util.Arrays;

public class Struct10 {
  public int _00;
  public int _01;
  public int _02;
  public int _03;
  public final Struct0e[] ptr_04 = new Struct0e[8];
  public Struct10 next_08;
  public Struct10 prev_0c;

  public Struct10() {
    Arrays.setAll(this.ptr_04, i -> new Struct0e());
  }
}
