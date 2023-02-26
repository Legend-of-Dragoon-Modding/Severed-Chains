package legend.game.combat.types;

import legend.core.gte.USCOLOUR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BttlScriptData6cSub1c_2Sub1e implements MemoryRef {
  private final Value ref;

  public final ShortRef x_00;
  public final ShortRef y_02;
  public final USCOLOUR colour_04;
  public final USCOLOUR colour_0a;
  public final USCOLOUR svec_10;
  public final USCOLOUR svec_16;
  public final ByteRef _1c;

  public BttlScriptData6cSub1c_2Sub1e(final Value ref) {
    this.ref = ref;

    this.x_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.y_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.colour_04 = ref.offset(2, 0x04L).cast(USCOLOUR::new);
    this.colour_0a = ref.offset(2, 0x0aL).cast(USCOLOUR::new);
    this.svec_10 = ref.offset(2, 0x10L).cast(USCOLOUR::new);
    this.svec_16 = ref.offset(2, 0x16L).cast(USCOLOUR::new);
    this._1c = ref.offset(1, 0x1cL).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
