package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class IntTag implements Tag {
  private int val;

  public IntTag(final int val) {
    this.set(val);
  }

  public IntTag() {
  }

  public int get() {
    return this.val;
  }

  public void set(final int val) {
    this.val = val;
  }

  @Override
  public int getType() {
    return TAG_TYPE_INT;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeVarInt(offset, this.val);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.val = data.readVarInt(offset);
  }

  @Override
  public IntTag clone() {
    return new IntTag(this.val);
  }

  @Override
  public String toString() {
    return "i:" + this.val;
  }
}
