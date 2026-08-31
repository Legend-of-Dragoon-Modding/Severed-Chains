package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class BoolTag implements Tag {
  private boolean val;

  public BoolTag(final boolean val) {
    this.set(val);
  }

  public BoolTag() {
  }

  public boolean get() {
    return this.val;
  }

  public void set(final boolean val) {
    this.val = val;
  }

  @Override
  public int getType() {
    return TAG_TYPE_BOOL;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeBool(offset, this.val);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.val = data.readBool(offset);
  }

  @Override
  public BoolTag clone() {
    return new BoolTag(this.val);
  }

  @Override
  public String toString() {
    return "b:" + this.val;
  }
}
