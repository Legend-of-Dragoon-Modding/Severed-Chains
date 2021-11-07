package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

/** 0x10 bytes long */
public class SssqEntry implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _02;
  public final UnsignedByteRef _03;

  public final UnsignedByteRef _0e;

  public SssqEntry(final Value ref) {
    this.ref = ref;

    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);

    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
