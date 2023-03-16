package legend.game.modding.events.combat;

import legend.game.combat.types.CombatantStruct1a8;
import legend.game.modding.events.Event;

import java.util.List;

public class EnemyRewardsEvent extends Event {
  public final int enemyId;
  private final List<CombatantStruct1a8.ItemDrop> drops;

  public int xp;
  public int gold;

  public EnemyRewardsEvent(final int enemyId, final int xp, final int gold, final List<CombatantStruct1a8.ItemDrop> drops) {
    this.enemyId = enemyId;
    this.drops = drops;

    this.xp = xp;
    this.gold = gold;
  }

  public void clear() {
    this.drops.clear();
  }

  public void add(final CombatantStruct1a8.ItemDrop drop) {
    this.drops.add(drop);
  }
}
