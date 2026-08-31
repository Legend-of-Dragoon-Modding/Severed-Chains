package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.game.unpacker.FileData;

public class VitalsStatType extends StatType<VitalsStat> {
  @Override
  public VitalsStat make(final StatCollection stats) {
    return new VitalsStat(this, stats);
  }

  @Override
  public void deserialize(final VitalsStat stat, final FileData data, final IntRef offset) {
    stat.setCurrent(data.readInt(offset));
    stat.setMaxRaw(data.readInt(offset));
    this.deserializeMods(stat, data, offset);
  }

  @Override
  public void serialize(final VitalsStat stat, final MapTag tag) {
    tag.set("current", new IntTag(stat.getCurrent()));
    tag.set("max", new IntTag(stat.getMaxRaw()));

    final ListTag mods = new ListTag();
    this.serializeMods(stat, mods);
    tag.set("mods", mods);
  }

  @Override
  public void deserialize(final VitalsStat stat, final MapTag tag) {
    stat.setCurrent(tag.get("current").asInt().get());
    stat.setMaxRaw(tag.get("max").asInt().get());
    this.deserializeMods(stat, tag.get("mods").asList());
  }
}
