package legend.game.combat.ui;

import java.util.Arrays;

public class BattleDisplayStats144 {
  public int x_00;
  public int y_02;
  public final BattleDisplayStats144Sub10[][] _04 = new BattleDisplayStats144Sub10[5][10];

  public BattleDisplayStats144() {
    for(final BattleDisplayStats144Sub10[] struct : this._04) {
      Arrays.setAll(struct, n -> new BattleDisplayStats144Sub10());
    }
  }
}
