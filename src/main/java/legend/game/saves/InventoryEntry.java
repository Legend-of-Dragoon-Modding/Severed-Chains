package legend.game.saves;

import org.legendofdragoon.modloader.registries.RegistryId;

public class InventoryEntry {
  public final RegistryId id;
  public final int size;
  public final int durability;

  public InventoryEntry(final RegistryId id, final int size, final int durability) {
    this.id = id;
    this.size = size;
    this.durability = durability;
  }
}
