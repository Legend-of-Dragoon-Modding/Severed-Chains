package legend.game.combat.types;

import legend.game.inventory.InventoryEntry;

import java.util.function.BooleanSupplier;

public class EnemyDrop {
  public final InventoryEntry<?> item;
  private final BooleanSupplier performDrop;
  private final Runnable overflow;

  public EnemyDrop(final InventoryEntry<?> item, final BooleanSupplier performDrop, final Runnable overflow) {
    this.item = item;
    this.performDrop = performDrop;
    this.overflow = overflow;
  }

  public boolean performDrop() {
    return this.performDrop.getAsBoolean();
  }

  public void overflow() {
    this.overflow.run();
  }
}
