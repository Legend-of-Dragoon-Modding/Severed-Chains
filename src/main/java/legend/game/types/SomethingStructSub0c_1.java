package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class SomethingStructSub0c_1 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef count_00;
  public final BoolRef bool_01;
  public final UnsignedShortRef _02;
  public final IntRef primitivesOffset_04;
  public final IntRef _08;

  public SomethingStructSub0c_1(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.bool_01 = ref.offset(1, 0x01L).cast(BoolRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.primitivesOffset_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
