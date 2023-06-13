package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class WMapStruct14 implements MemoryRef {
  private final Value ref;

  public final ShortRef areaIndex_00;
  public final ShortRef placeIndex_02;
  public final ShortRef submapCut_04;
  public final ShortRef submapScene_06;
  public final ShortRef submapCut_08;
  public final ShortRef submapScene_0a;
  public final ShortRef _0c;
  public final UnsignedByteRef _0e;
  public final UnsignedByteRef _10;
  public final UnsignedByteRef _12;

  public WMapStruct14(final Value ref) {
    this.ref = ref;

    this.areaIndex_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.placeIndex_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.submapCut_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.submapScene_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.submapCut_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.submapScene_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this._10 = ref.offset(1, 0x10L).cast(UnsignedByteRef::new);
    this._12 = ref.offset(1, 0x12L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
