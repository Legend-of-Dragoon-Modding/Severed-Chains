package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

/** Main menu renderable? */
public class Renderable58 implements MemoryRef {
  private final Value ref;

  /**
   * 0x4 - start and end glyph is the same - no transition
   */
  public final UnsignedIntRef flags_00;
  public final IntRef glyph_04;
  public final IntRef _08;
  public final UnsignedIntRef _0c;
  public final IntRef startGlyph_10;
  public final IntRef endGlyph_14;
  public final IntRef _18;
  public final IntRef _1c;
  public final Pointer<Drgn0_6666Struct> drgn0_6666_20;
  public final Pointer<UnboundedArrayRef<UnsignedIntRef>> drgn0_6666_data_24;
  public final UnsignedByteRef _28;
  public final IntRef tpage_2c;
  public final IntRef clut_30;
  public final UnsignedIntRef _34;
  public final UnsignedIntRef _38;
  public final UnsignedByteRef z_3c;
  public final IntRef x_40;
  public final IntRef y_44;
  public final UnsignedIntRef _48;
  public final Pointer<Renderable58> child_50;
  public final Pointer<Renderable58> parent_54;

  public Renderable58(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.glyph_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.startGlyph_10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.endGlyph_14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(IntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.drgn0_6666_20 = ref.offset(4, 0x20L).cast(Pointer.deferred(4, Drgn0_6666Struct::new));
    this.drgn0_6666_data_24 = ref.offset(4, 0x24L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x4, UnsignedIntRef::new)));
    this._28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);
    this.tpage_2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this.clut_30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(UnsignedIntRef::new);
    this.z_3c = ref.offset(1, 0x3cL).cast(UnsignedByteRef::new);
    this.x_40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this.y_44 = ref.offset(4, 0x44L).cast(IntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(UnsignedIntRef::new);
    this.child_50 = ref.offset(4, 0x50L).cast(Pointer.deferred(4, Renderable58::new));
    this.parent_54 = ref.offset(4, 0x54L).cast(Pointer.deferred(4, Renderable58::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
