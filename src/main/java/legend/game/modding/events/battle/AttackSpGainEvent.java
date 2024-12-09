package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

/** TODO This event is not actually deprecated, but it is only fired for Shiranda right now */
@Deprecated
public class AttackSpGainEvent extends BattleEvent {
  public final PlayerBattleEntity bent;
  public int sp;

  public AttackSpGainEvent(final PlayerBattleEntity bent, final int sp) {
    this.bent = bent;
    this.sp = sp;
  }
}
