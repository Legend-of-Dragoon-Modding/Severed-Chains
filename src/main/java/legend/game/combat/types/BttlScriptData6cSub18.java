package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub18 extends BttlScriptData6cSubBase1 {
  public final UnsignedShortRef count_00;

  public final SpriteMetrics08 metrics_04;
  public final Pointer<UnboundedArrayRef<BttlScriptData6cSub18Sub3c>> ptr_0c;

  public BttlScriptData6cSub18(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this.metrics_04 = ref.offset(4, 0x04L).cast(SpriteMetrics08::new);
    this.ptr_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x3c, BttlScriptData6cSub18Sub3c::new)));
  }
}
