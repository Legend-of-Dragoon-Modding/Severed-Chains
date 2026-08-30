package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class StringTag implements Tag {
  private String val = "";

  public StringTag(final String val) {
    this.set(val);
  }

  public StringTag() {
  }

  public String get() {
    return this.val;
  }

  public void set(final String val) {
    this.val = val;
  }

  @Override
  public int getType() {
    return TAG_TYPE_STRING;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeString(offset, this.val);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    this.val = data.readString(offset);
  }

  @Override
  public StringTag clone() {
    return new StringTag(this.val);
  }

  @Override
  public String toString() {
    return "s:" + this.val;
  }
}
