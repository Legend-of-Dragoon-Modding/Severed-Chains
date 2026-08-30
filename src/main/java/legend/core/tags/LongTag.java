package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class LongTag implements Tag {
  private long val;

  public LongTag(final long val) {
    this.set(val);
  }

  public LongTag() {
  }

  public long get() {
    return this.val;
  }

  public void set(final long val) {
    this.val = val;
  }

  @Override
  public int getType() {
    return TAG_TYPE_LONG;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeVarLong(offset, this.val);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.val = data.readVarLong(offset);
  }

  @Override
  public LongTag clone() {
    return new LongTag(this.val);
  }

  @Override
  public String toString() {
    return "l:" + this.val;
  }
}
