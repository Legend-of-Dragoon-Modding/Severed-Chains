package legend.turnorder;

import legend.game.combat.bent.BattleEntity27c;

public class TurnOrder {
  public final BattleEntity27c bent;
  public int turnValue;
  public boolean interrupt;

  public TurnOrder(final BattleEntity27c bent) {
    this.bent = bent;
    this.turnValue = bent.turnValue_4c;
  }

  public static TurnOrder interrupt(final BattleEntity27c bent) {
    final TurnOrder turn = new TurnOrder(bent);
    turn.interrupt = true;
    return turn;
  }
}
