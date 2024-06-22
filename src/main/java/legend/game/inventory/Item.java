package legend.game.inventory;

import legend.core.memory.Method;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class Item extends RegistryEntry implements InventoryEntry {
  private final String name;
  private final String description;
  private final String combatDescription;
  private final int icon;
  private final int price;

  public Item(final String name, final String description, final String combatDescription, final int icon, final int price) {
    this.name = name;
    this.description = description;
    this.combatDescription = combatDescription;
    this.icon = icon;
    this.price = price;
  }

  @Override
  public int getIcon() {
    return this.icon;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  public String getCombatDescription() {
    return this.combatDescription;
  }

  @Override
  public int getPrice() {
    return this.price;
  }

  /** Item can't be stolen by enemies */
  public boolean isProtected() {
    return false;
  }

  /** Check if an item can ever be used in this location */
  public abstract boolean canBeUsed(final UsageLocation location);

  /** Check if an item can be used in this location right now */
  public boolean canBeUsedNow(final UsageLocation location) {
    return this.canBeUsed(location);
  }

  public abstract boolean canTarget(final TargetType type);

  /** TODO refactor, use UsageLocation */
  @Method(0x80022d88L)
  public abstract void useItemInMenu(final UseItemResponse response, final int charIndex);

  public enum UsageLocation {
    MENU,
    BATTLE,
  }

  public enum TargetType {
    ALLIES,
    ENEMIES,
    ALL,
  }
}
