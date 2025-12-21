package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.MonsterBattleEntity;

public class SingleMonsterTargetEvent extends BattleEvent {
  public final MonsterBattleEntity monster;

  public SingleMonsterTargetEvent(final Battle battle, final MonsterBattleEntity monster) {
    super(battle);
    this.monster = monster;
  }
}