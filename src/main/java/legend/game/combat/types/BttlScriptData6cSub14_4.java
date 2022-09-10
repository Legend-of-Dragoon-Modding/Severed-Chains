package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub14_4 extends BttlScriptData6cSubBase1 {
  public final UnsignedShortRef count_00;
  public final UnsignedShortRef _02;

  public final ShortRef _06;
  public final ShortRef _08;
  public final ShortRef width_0a;
  public final ShortRef height_0c;
  public final ShortRef clut_0e;
  public final Pointer<UnboundedArrayRef<BttlScriptData6cSub14_4Sub70>> ptr_10;

  public BttlScriptData6cSub14_4(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);

    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.width_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.height_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.clut_0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this.ptr_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x70, BttlScriptData6cSub14_4Sub70::new)));
  }
}
