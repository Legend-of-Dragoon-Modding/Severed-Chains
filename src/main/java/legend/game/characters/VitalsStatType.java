package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class VitalsStatType extends StatType<VitalsStat> {
  @Override
  public VitalsStat make(final StatCollection stats) {
    return new VitalsStat(this, stats);
  }

  @Override
  public void serialize(final VitalsStat stat, final FileData data, final IntRef offset) {
    data.writeInt(offset, stat.getCurrent());
    data.writeInt(offset, stat.getMaxRaw());
    this.serializeMods(stat, data, offset);
  }

  @Override
  public void deserialize(final VitalsStat stat, final FileData data, final IntRef offset) {
    stat.setCurrent(data.readInt(offset));
    stat.setMaxRaw(data.readInt(offset));
    this.deserializeMods(stat, data, offset);
  }
}
