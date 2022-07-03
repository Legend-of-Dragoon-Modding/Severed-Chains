package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub14 extends BttlScriptData6cSubBase1 {
  public final UnsignedByteRef _00;
  public final UnsignedByteRef _01;

  public final UnsignedIntRef _04;
  public final UnsignedIntRef _08;
  public final UnsignedByteRef _0c;
  public final UnsignedByteRef _0d;
  public final UnsignedByteRef _0e;

  public final Pointer<QuadConsumerRef<BttlScriptData6c, Long, long[], BttlScriptData6cSub14>> renderer_10;

  public BttlScriptData6cSub14(final Value ref) {
    super(ref);

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this._0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);

    this.renderer_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, QuadConsumerRef::new));
  }
}
