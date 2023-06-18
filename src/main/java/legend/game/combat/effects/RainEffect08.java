package legend.game.combat.effects;

import java.util.Arrays;

public class RainEffect08 implements BttlScriptData6cSubBase1 {
  public int count_00;
  public RaindropEffect0c[] raindropArray_04;

  public RainEffect08(final int count) {
    this.count_00 = count;
    this.raindropArray_04 = new RaindropEffect0c[count];
    Arrays.setAll(this.raindropArray_04, RaindropEffect0c::new);
  }
}
