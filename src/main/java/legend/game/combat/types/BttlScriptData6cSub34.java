package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.ShortRef;

public class BttlScriptData6cSub34 extends BttlScriptData6cSubBase2 {
  public final VECTOR _0c;
  public final VECTOR _18;
  public final VECTOR _24;
  public final ByteRef scriptIndex_30;

  public final ShortRef _32;

  public BttlScriptData6cSub34(final Value ref) {
    super(ref);

    this._0c = ref.offset(4, 0x0cL).cast(VECTOR::new);
    this._18 = ref.offset(4, 0x18L).cast(VECTOR::new);
    this._24 = ref.offset(4, 0x24L).cast(VECTOR::new);
    this.scriptIndex_30 = ref.offset(1, 0x30L).cast(ByteRef::new);

    this._32 = ref.offset(2, 0x32L).cast(ShortRef::new);
  }
}
