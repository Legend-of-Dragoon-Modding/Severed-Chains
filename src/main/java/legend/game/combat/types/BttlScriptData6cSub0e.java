package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub0e implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedShortRef r_00;
  public final UnsignedShortRef g_02;
  public final UnsignedShortRef b_04;
  public final ShortRef stepR_06;
  public final ShortRef stepG_08;
  public final ShortRef stepB_0a;
  public final UnsignedShortRef ticksRemaining_0c;

  public BttlScriptData6cSub0e(final Value ref) {
    this.ref = ref;

    this.r_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.g_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.b_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.stepR_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.stepG_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.stepB_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.ticksRemaining_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
