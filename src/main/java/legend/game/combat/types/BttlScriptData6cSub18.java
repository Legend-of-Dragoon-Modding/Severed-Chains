package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub18 extends BttlScriptData6cSubBase1 {
  public final UnsignedShortRef count_00;

  public final UnsignedShortRef _04;
  public final UnsignedShortRef _06;
  public final ByteRef width_08;
  public final UnsignedByteRef height_09;
  public final UnsignedShortRef clut_0a;
  public final Pointer<UnboundedArrayRef<BttlScriptData6cSub18Sub3c>> ptr_0c;

  public BttlScriptData6cSub18(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.width_08 = ref.offset(1, 0x08L).cast(ByteRef::new);
    this.height_09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this.clut_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this.ptr_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x3c, BttlScriptData6cSub18Sub3c::new)));
  }
}
