package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub10_2 extends BttlScriptData6cSubBase1 {
  public final IntRef count_00;
  public final UnsignedShortRef _04;
  public final UnsignedShortRef _06;
  public final UnsignedByteRef _08;
  public final UnsignedByteRef _09;
  public final UnsignedShortRef _0a;
  public final UnsignedIntRef _0c;

  public BttlScriptData6cSub10_2(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this._08 = ref.offset(1, 0x08L).cast(UnsignedByteRef::new);
    this._09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
  }
}
