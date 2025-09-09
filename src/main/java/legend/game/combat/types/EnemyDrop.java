package legend.game.combat.types;

import legend.game.inventory.ItemIcon;

import java.util.function.BooleanSupplier;

public class EnemyDrop {
  public final ItemIcon icon;
  public final String name;
  private final BooleanSupplier performDrop;
  private final Runnable overflow;

  public EnemyDrop(final ItemIcon icon, final String name, final BooleanSupplier performDrop, final Runnable overflow) {
    this.icon = icon;
    this.name = name;
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
