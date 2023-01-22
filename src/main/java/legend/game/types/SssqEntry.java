package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

/** 0x10 bytes long */
public class SssqEntry implements MemoryRef {
  private final Value ref;

  /** -1 means none */
  public final ByteRef patchNumber_02;
  public final UnsignedByteRef volume_03;
  /** 0x40 seems to be middle - most common value. That would make 0 left, and 0x7f right. */
  public final UnsignedByteRef pan_04;

  public final UnsignedByteRef modulation_09;
  public final UnsignedByteRef pitchBend_0a;
  public final UnsignedByteRef _0b;
  public final UnsignedByteRef breath_0c;

  public final UnsignedByteRef volume_0e;

  public SssqEntry(final Value ref) {
    this.ref = ref;

    this.patchNumber_02 = ref.offset(1, 0x02L).cast(ByteRef::new);
    this.volume_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.pan_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);

    this.modulation_09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this.pitchBend_0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this._0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
    this.breath_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);

    this.volume_0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
