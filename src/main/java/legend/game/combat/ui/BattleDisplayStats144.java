package legend.game.combat.ui;

import java.util.Arrays;

public class BattleDisplayStats144 {
  public int x_00;
  public int y_02;
  public final BattleDisplayStatsDigit10[][] _04 = new BattleDisplayStatsDigit10[5][10];

  public BattleDisplayStats144() {
    for(final BattleDisplayStatsDigit10[] struct : this._04) {
      Arrays.setAll(struct, n -> new BattleDisplayStatsDigit10());
    }
  }
}
