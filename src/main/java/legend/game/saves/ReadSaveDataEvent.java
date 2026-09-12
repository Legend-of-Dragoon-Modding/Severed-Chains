package legend.game.saves;

import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.Tag;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;

/// Load data from a save. Data is saved using {@link WriteSaveDataEvent}.
public class ReadSaveDataEvent extends Event {
  private final ListTag tags;

  public ReadSaveDataEvent(final ListTag tags) {
    this.tags = tags;
  }

  @Nullable
  public Tag get(final RegistryId id) {
    for(int i = 0; i < this.tags.size(); i++) {
      final MapTag tag = this.tags.get(i).asMap();
      final RegistryId tagId = tag.get("id").asRegistryId().get();

      if(tagId.equals(id)) {
        return tag.get("data");
      }
    }

    return null;
  }
}
