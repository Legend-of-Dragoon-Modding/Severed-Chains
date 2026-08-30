package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.REGISTRIES;

public abstract class StatType<StatClass extends Stat> extends RegistryEntry {
  public abstract StatClass make(final StatCollection stats);
  public abstract void serialize(final StatClass stat, final FileData data, final IntRef offset);
  public abstract void deserialize(final StatClass stat, final FileData data, final IntRef offset);
  public abstract void serialize(final StatClass stat, final MapTag tag);
  public abstract void deserialize(final StatClass stat, final MapTag tag);

  protected void serializeMods(final StatClass stat, final FileData data, final IntRef offset) {
    data.writeInt(offset, stat.mods.size());

    for(final var entry : stat.mods.entrySet()) {
      final StatMod mod = entry.getValue();
      data.writeRegistryId(offset, entry.getKey());
      data.writeRegistryId(offset, mod.getType().getRegistryId());
      mod.getType().serialize(mod, data, offset);
    }
  }

  protected void deserializeMods(final StatClass stat, final FileData data, final IntRef offset) {
    stat.mods.clear();

    final int modCount = data.readInt(offset);

    for(int i = 0; i < modCount; i++) {
      final RegistryId modId = data.readRegistryId(offset);
      final RegistryId modTypeId = data.readRegistryId(offset);
      final StatModType modType = REGISTRIES.statModTypes.getEntry(modTypeId).get();
      stat.mods.put(modId, modType.deserialize(data, offset));
    }
  }

  protected void serializeMods(final StatClass stat, final ListTag tags) {
    for(final var entry : stat.mods.entrySet()) {
      final MapTag modTag = new MapTag();
      final StatMod mod = entry.getValue();
      modTag.set("modId", new RegistryIdTag(entry.getKey()));
      modTag.set("typeId", new RegistryIdTag(mod.getType()));
      mod.getType().serialize(mod, modTag);
      tags.add(modTag);
    }
  }

  protected void deserializeMods(final StatClass stat, final ListTag tags) {
    stat.mods.clear();

    for(final Tag tag : tags) {
      final MapTag modTag = tag.asMap();
      final RegistryId modId = modTag.get("modId").asRegistryId().get();
      final RegistryId modTypeId = modTag.get("typeId").asRegistryId().get();
      final StatModType modType = REGISTRIES.statModTypes.getEntry(modTypeId).get();
      stat.mods.put(modId, modType.deserialize(modTag));
    }
  }
}
