package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class Struct34 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _02;
  public final UnsignedShortRef _04;
  public final UnsignedShortRef _06;
  public final UnsignedShortRef _08;
  public final UnsignedShortRef _0a;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef _10;
  public final UnsignedIntRef _14;
  public final UnsignedShortRef _18;

  public final IntRef x_1c;
  public final IntRef y_20;
  public final IntRef screenOffsetX_24;
  public final IntRef screenOffsetY_28;
  public final IntRef sz3_2c;
  public final Pointer<Struct34> parent_30;

  public Struct34(final Value ref) {
    this.ref = ref;

    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);

    this.x_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.y_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.screenOffsetX_24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this.screenOffsetY_28 = ref.offset(4, 0x28L).cast(IntRef::new);
    this.sz3_2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this.parent_30 = ref.offset(4, 0x30L).cast(Pointer.deferred(4, Struct34::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
