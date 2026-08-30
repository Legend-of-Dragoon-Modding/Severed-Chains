package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

public class RawTag implements Tag {
  private byte[] val;

  public RawTag(final byte[] val) {
    this.set(val);
  }

  public RawTag() {
  }

  public byte[] get() {
    return this.val;
  }

  public void set(final byte[] val) {
    this.val = val;
  }

  @Override
  public int getType() {
    return TAG_TYPE_RAW;
  }

  @Override
  public void serialize(final FileData data, final IntRef offset) {
    data.writeVarInt(offset, this.val.length);
    data.write(0, this.val, offset, this.val.length);
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    final int size = data.readVarInt(offset);
    this.val = new byte[size];
    data.read(offset, this.val, 0, size);
  }

  @Override
  public RawTag clone() {
    return new RawTag(this.val.clone());
  }
}
