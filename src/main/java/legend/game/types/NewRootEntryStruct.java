package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class NewRootEntryStruct implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef ub_00;
  public final UnsignedByteRef ub_01;
  public final UnsignedByteRef ub_02;
  public final UnsignedByteRef ub_03;
  public final UnsignedByteRef ub_04;
  public final UnsignedByteRef ub_05;
  public final UnsignedByteRef ub_06;
  public final UnsignedByteRef ub_07;

  public NewRootEntryStruct(final Value ref) {
    this.ref = ref;

    this.ub_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.ub_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.ub_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.ub_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.ub_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.ub_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.ub_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this.ub_07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
