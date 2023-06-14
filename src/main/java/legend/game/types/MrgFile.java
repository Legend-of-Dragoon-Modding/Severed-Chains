package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

import java.util.function.Function;

public class MrgFile implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef magic;
  public final IntRef count;
  public final UnboundedArrayRef<MrgEntry> entries;

  public MrgFile(final Value ref) {
    this.ref = ref;

    this.magic = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.count = ref.offset(4, 0x4L).cast(IntRef::new);
    this.entries = ref.offset(4, 0x8L).cast(UnboundedArrayRef.of(8, MrgEntry::new, this.count::get));
  }

  public <T extends MemoryRef> T getFile(final int index, final Function<Value, T> constructor) {
    return this.ref.offset(4, this.entries.get(index).offset.get()).cast(constructor);
  }

  public long getFile(final int index) {
    return this.ref.offset(4, this.entries.get(index).offset.get()).getAddress();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
