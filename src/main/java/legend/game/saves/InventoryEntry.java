package legend.game.saves;

import legend.core.tags.Tag;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

public class InventoryEntry {
  public final RegistryId id;
  public final int size;
  public final int durability;
  @Nullable
  public final Tag extraData;

  public InventoryEntry(final RegistryId id, final int size, final int durability, @Nullable final Tag extraData) {
    this.id = id;
    this.size = size;
    this.durability = durability;
    this.extraData = extraData;
  }

  public InventoryEntry(final RegistryId id, final int size, final int durability) {
    this(id, size, durability, null);
  }
}
