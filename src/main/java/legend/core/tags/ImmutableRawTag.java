package legend.core.tags;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.FileData;

/** Byte payload shared safely by snapshots; callers cannot mutate the retained bytes. */
public final class ImmutableRawTag extends RawTag {
  public ImmutableRawTag(final byte[] bytes) {
    super.set(bytes.clone());
  }

  @Override
  public byte[] get() {
    return super.get().clone();
  }

  @Override
  public void set(final byte[] bytes) {
    throw new UnsupportedOperationException("Immutable save payload");
  }

  @Override
  public void deserialize(final FileData data, final IntRef offset) {
    throw new UnsupportedOperationException("Immutable save payload");
  }

  @Override
  public ImmutableRawTag clone() {
    return this;
  }
}
