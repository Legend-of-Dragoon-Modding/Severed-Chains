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
  public final UnsignedIntRef glyph_04;
  public final IntRef _08;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef startGlyph_10;
  public final UnsignedIntRef endGlyph_14;
  public final UnsignedIntRef _18;
  public final UnsignedIntRef _1c;
  public final Pointer<Drgn0_6666Struct> drgn0_6666_20;
  public final Pointer<UnboundedArrayRef<UnsignedIntRef>> drgn0_6666_data_24;
  public final UnsignedByteRef _28;
  public final UnsignedIntRef tpage_2c;
  public final UnsignedIntRef clut_30;
  public final UnsignedIntRef _34;
  public final UnsignedIntRef _38;
  public final UnsignedByteRef _3c;
  public final UnsignedIntRef x_40;
  public final UnsignedIntRef y_44;
  public final UnsignedIntRef _48;
  public final Pointer<Renderable58> child_50;
  public final Pointer<Renderable58> parent_54;

  public Renderable58(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.glyph_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.startGlyph_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.endGlyph_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this._1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.drgn0_6666_20 = ref.offset(4, 0x20L).cast(Pointer.deferred(4, Drgn0_6666Struct::new));
    this.drgn0_6666_data_24 = ref.offset(4, 0x24L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x4, UnsignedIntRef::new)));
    this._28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);
    this.tpage_2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this.clut_30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(UnsignedIntRef::new);
    this._3c = ref.offset(1, 0x3cL).cast(UnsignedByteRef::new);
    this.x_40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
    this.y_44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(UnsignedIntRef::new);
    this.child_50 = ref.offset(4, 0x50L).cast(Pointer.deferred(4, Renderable58::new));
    this.parent_54 = ref.offset(4, 0x54L).cast(Pointer.deferred(4, Renderable58::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
