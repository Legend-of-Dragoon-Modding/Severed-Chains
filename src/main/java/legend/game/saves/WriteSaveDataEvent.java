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

  public WriteSaveDataEvent(final ListTag tags) {
    this.tags = tags;
  }

   /// Add a {@link Tag} to the save data. This data will be sent to your mod when the save is loaded.
   ///
   /// The registry ID **MUST** use your mod ID or you will not receive a {@link ReadSaveDataEvent} for it.
  public void add(final RegistryId id, final Tag tag) {
    final MapTag modTag = new MapTag();
    modTag.set("id", new RegistryIdTag(id));
    modTag.set("data", tag);
    this.tags.add(modTag);
  }
}
