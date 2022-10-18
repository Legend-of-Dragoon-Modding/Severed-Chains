package legend.game.types;

import legend.core.gpu.RECT;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class WMapRender40 implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<WMapRender10>> _00;
  public final ArrayRef<UnsignedIntRef> _04;
  public final ArrayRef<UnsignedIntRef> _0c;
  public final ArrayRef<Pointer<UnboundedArrayRef<WMapRender28>>> _14;
  public final Pointer<UnboundedArrayRef<RECT>> _1c;
  public final ArrayRef<UnsignedIntRef> _20;

  public final UnsignedIntRef _28;
  public final UnsignedIntRef _2c;
  public final UnsignedIntRef _30;
  public final ShortRef _34;
  public final ShortRef _36;
  public final UnsignedShortRef x_38;
  public final UnsignedShortRef y_3a;
  public final BoolRef transparency_3c;
  public final UnsignedByteRef _3d;
  public final UnsignedByteRef z_3e;
  public final UnsignedByteRef _3f;

  public WMapRender40(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, WMapRender10::new)));
    this._04 = ref.offset(4, 0x04L).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
    this._0c = ref.offset(4, 0x0cL).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
    this._14 = ref.offset(4, 0x14L).cast(ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(WMapRender28.class)), 2, 4, Pointer.deferred(4, UnboundedArrayRef.of(0x28, WMapRender28::new))));
    this._1c = ref.offset(4, 0x1cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x8, RECT::new)));
    this._20 = ref.offset(4, 0x20L).cast(ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));

    this._28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(2, 0x34L).cast(ShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(ShortRef::new);
    this.x_38 = ref.offset(2, 0x38L).cast(UnsignedShortRef::new);
    this.y_3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this.transparency_3c = ref.offset(1, 0x3cL).cast(BoolRef::new);
    this._3d = ref.offset(1, 0x3dL).cast(UnsignedByteRef::new);
    this.z_3e = ref.offset(1, 0x3eL).cast(UnsignedByteRef::new);
    this._3f = ref.offset(1, 0x3fL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
