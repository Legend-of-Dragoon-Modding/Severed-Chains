package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.MonsterBattleEntity;

public class SingleMonsterTargetEvent extends BattleEvent {
  public final MonsterBattleEntity monster;

  public SingleMonsterTargetEvent(final MonsterBattleEntity monster) {
    this.monster = monster;
  }
}