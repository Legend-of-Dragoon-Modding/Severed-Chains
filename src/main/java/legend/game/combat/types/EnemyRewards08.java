package legend.game.combat.types;

import legend.game.inventory.InventoryEntry;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EnemyRewards08 {
  public int xp_00;
  public int gold_02;
  public int itemChance_04;
  public Supplier<? extends InventoryEntry> itemDrop_05;
  public int _06;

  public EnemyRewards08(final int xp, final int gold, final int itemChance, @Nullable final Supplier<? extends InventoryEntry> itemDrop, final int _06) {
    this.xp_00 = xp;
    this.gold_02 = gold;
    this.itemChance_04 = itemChance;
    this.itemDrop_05 = itemDrop;
    this._06 = _06;
  }
}
