package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class EnumTag implements Tag {
  private String val = "";

  public EnumTag(final Enum<?> val) {
    this.set(val);
  }

  public EnumTag() {
  }

  public <T extends Enum<T>> T get(final Class<T> cls) {
    return Enum.valueOf(cls, this.val);
  }

  public void set(final Enum<?> val) {
    this.val = val.name();
  }

  @Override
  public int getType() {
    return TAG_TYPE_ENUM;
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
  public EnumTag clone() {
    final EnumTag tag = new EnumTag();
    tag.val = this.val;
    return tag;
  }

  @Override
  public String toString() {
    return "e:" + this.val;
  }
}
