package legend.game.combat.ui;

import java.util.Arrays;

public class BattleDisplayStats144 {
  public int x_00;
  public int y_02;
  public final BattleDisplayStatsDigit10[][] digits_04 = new BattleDisplayStatsDigit10[5][10];

  public BattleDisplayStats144() {
    for(final BattleDisplayStatsDigit10[] digit : this.digits_04) {
      Arrays.setAll(digit, n -> new BattleDisplayStatsDigit10());
    }
  }
}
