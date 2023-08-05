package legend.game.modding.events.battle;

import legend.game.combat.bobj.BattleEvent;
import legend.game.combat.bobj.MonsterBattleObject;

public class SingleMonsterTargetEvent extends BattleEvent {
  public final MonsterBattleObject monster;

  public SingleMonsterTargetEvent(MonsterBattleObject monster) {
    this.monster = monster;
  }
}
