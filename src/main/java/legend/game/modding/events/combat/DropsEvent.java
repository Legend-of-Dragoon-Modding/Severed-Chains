package legend.game.modding.events.combat;

import legend.game.combat.types.CombatantStruct1a8;
import legend.game.modding.events.Event;

import java.util.List;

public class DropsEvent extends Event {
  public final int enemyId;
  private final List<CombatantStruct1a8.ItemDrop> drops;

  public DropsEvent(final int enemyId, final List<CombatantStruct1a8.ItemDrop> drops) {
    this.enemyId = enemyId;
    this.drops = drops;
  }

  public void clear() {
    this.drops.clear();
  }

  public void add(final CombatantStruct1a8.ItemDrop drop) {
    this.drops.add(drop);
  }
}
