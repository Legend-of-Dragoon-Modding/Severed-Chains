package legend.game.modding.events.battle;

import legend.game.combat.bobj.BattleEvent;
import legend.game.combat.bobj.PlayerBattleObject;

/** TODO This event is not actually deprecated, but it is only fired for Shiranda right now */
@Deprecated
public class AttackSpGainEvent extends BattleEvent {
  public final PlayerBattleObject bobj;
  public int sp;

  public AttackSpGainEvent(final PlayerBattleObject bobj, final int sp) {
    this.bobj = bobj;
    this.sp = sp;
  }
}
