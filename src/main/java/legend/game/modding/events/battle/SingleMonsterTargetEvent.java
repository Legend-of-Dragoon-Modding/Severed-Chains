package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.MonsterBattleEntity;
import org.legendofdragoon.modloader.events.Event;

public class SingleMonsterTargetEvent extends Event {
  public final Battle battle;
  public final MonsterBattleEntity monster;

  public SingleMonsterTargetEvent(final Battle battle, final MonsterBattleEntity monster) {
    this.battle = battle;
    this.monster = monster;
  }
}