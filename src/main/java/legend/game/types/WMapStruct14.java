package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class WMapStruct14 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ShortRef _02;
  public final ShortRef _04;
  public final ShortRef _06;

  public final UnsignedByteRef _0e;

  public final UnsignedByteRef _12;

  public WMapStruct14(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);

    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);

    this._12 = ref.offset(1, 0x12L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
