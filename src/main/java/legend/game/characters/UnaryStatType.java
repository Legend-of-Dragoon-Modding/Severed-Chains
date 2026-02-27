package legend.game.characters;

import legend.core.memory.types.IntRef;
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
}
