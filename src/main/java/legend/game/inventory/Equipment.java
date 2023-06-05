package legend.game.inventory;

import legend.game.modding.registries.RegistryEntry;
import legend.game.types.EquipmentStats1c;

public class Equipment extends RegistryEntry {
  public final String name;

  public Equipment(final String name, final EquipmentStats1c stats) {
    this(name);
  }

  public Equipment(final String name) {
    this.name = name;
  }
}
