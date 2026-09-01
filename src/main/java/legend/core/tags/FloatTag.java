package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class FloatTag implements Tag {
  private float val;

  public FloatTag(final float val) {
    this.set(val);
  }

  public FloatTag() {
  }

  public float get() {
    return this.val;
  }

  public void set(final float val) {
    this.val = val;
  }

  @Override
  public int getType() {
    return TAG_TYPE_FLOAT;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeFloat(offset, this.val);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.val = data.readFloat(offset);
  }

  @Override
  public FloatTag clone() {
    return new FloatTag(this.val);
  }

  @Override
  public String toString() {
    return "f:" + this.val;
  }
}
