package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;

public class Struct54 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ShortRef _02;
  public final ShortRef _04;
  public final ShortRef _06;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;

  public final IntRef x_18;
  public final IntRef y_1c;
  public final SVECTOR sxyz0_20;
  public final SVECTOR sxyz1_28;
  public final SVECTOR sxyz2_30;
  public final SVECTOR sxyz3_38;
  public final UnsignedIntRef _40;
  public final UnsignedIntRef _44;
  public final UnsignedIntRef colour_48;
  public final UnsignedIntRef sz3div4_4c;
  public final Pointer<Struct54> next_50;

  public Struct54(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);

    this.x_18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this.y_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.sxyz0_20 = ref.offset(4, 0x20L).cast(SVECTOR::new);
    this.sxyz1_28 = ref.offset(4, 0x28L).cast(SVECTOR::new);
    this.sxyz2_30 = ref.offset(4, 0x30L).cast(SVECTOR::new);
    this.sxyz3_38 = ref.offset(4, 0x38L).cast(SVECTOR::new);
    this._40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this.colour_48 = ref.offset(4, 0x48L).cast(UnsignedIntRef::new);
    this.sz3div4_4c = ref.offset(4, 0x4cL).cast(UnsignedIntRef::new);
    this.next_50 = ref.offset(4, 0x50L).cast(Pointer.deferred(4, Struct54::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
