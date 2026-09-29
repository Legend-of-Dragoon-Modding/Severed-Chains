package legend.game.saves;

import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

/// Add data to a save. Data is loaded using {@link ReadSaveDataEvent}.
public class WriteSaveDataEvent extends Event {
  private final ListTag tags;
  private final SaveSchemaCatalog schemas;
  private final java.util.Set<RegistryId> removed = new java.util.HashSet<>();

  public WriteSaveDataEvent(final ListTag tags) {
    this(tags, SaveSchemaCatalog.current());
  }

  public WriteSaveDataEvent(final ListTag tags, final SaveSchemaCatalog schemas) {
    this.tags = tags;
    this.schemas = schemas;
  }

   /// Add a {@link Tag} to the save data. This data will be sent to your mod when the save is loaded.
   ///
   /// The registry ID **MUST** use your mod ID or you will not receive a {@link ReadSaveDataEvent} for it.
  public void add(final RegistryId id, final Tag tag) {
    final MapTag modTag = new MapTag();
    final RegistryId canonical = this.schemas.canonical(SaveSchema.Domain.MOD_DATA, id);
    modTag.set("id", new RegistryIdTag(canonical));
    final int version = this.schemas.writeVersion(canonical);
    if(version >= 0) modTag.set("version", new legend.core.tags.IntTag(version));
    modTag.set("data", tag.clone());
    this.removed.remove(canonical);
    this.tags.add(modTag);
  }

  /** Explicitly delete owned data; omission alone preserves unknown or unavailable payloads. */
  public void remove(final RegistryId id) {
    final RegistryId canonical = this.schemas.canonical(SaveSchema.Domain.MOD_DATA, id);
    this.removed.add(canonical);
    for(int i = this.tags.size() - 1; i >= 0; i--) {
      if(this.schemas.canonical(SaveSchema.Domain.MOD_DATA, this.tags.get(i).asMap().get("id").asRegistryId().get()).equals(canonical)) this.tags.remove(i);
    }
  }

  public java.util.Set<RegistryId> removedIds() {
    return java.util.Set.copyOf(this.removed);
  }
}
