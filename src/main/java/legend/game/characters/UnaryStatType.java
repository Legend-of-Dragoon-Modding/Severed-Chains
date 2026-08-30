package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.core.tags.IntTag;
import legend.core.tags.ListTag;
import legend.core.tags.MapTag;
import legend.game.unpacker.FileData;

public class UnaryStatType extends StatType<UnaryStat> {
  @Override
  public UnaryStat make(final StatCollection stats) {
    return new UnaryStat(this, stats);
  }

  @Override
  public void serialize(final UnaryStat stat, final FileData data, final IntRef offset) {
    data.writeInt(offset, stat.getRaw());
    this.serializeMods(stat, data, offset);
  }

  @Override
  public void deserialize(final UnaryStat stat, final FileData data, final IntRef offset) {
    stat.setRaw(data.readInt(offset));
    this.deserializeMods(stat, data, offset);
  }

  @Override
  public void serialize(final UnaryStat stat, final MapTag tag) {
    tag.set("val", new IntTag(stat.getRaw()));
    final ListTag mods = new ListTag();
    this.serializeMods(stat, mods);
    tag.set("mods", mods);
  }

  @Override
  public void deserialize(final UnaryStat stat, final MapTag tag) {
    stat.setRaw(tag.get("val").asInt().get());
    this.deserializeMods(stat, tag.get("mods").asList());
  }
}
