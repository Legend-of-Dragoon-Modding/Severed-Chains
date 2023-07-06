package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class battleMenuBackgroundDisplayMetrics0c implements MemoryRef {
  public final Value ref;

  public final ShortRef vertexBaseOffsetIndex_00;
  public final ShortRef vertexXMod_02;
  public final ShortRef vertexYMod_04;
  public final ShortRef w_06;
  public final ShortRef h_08;

  public battleMenuBackgroundDisplayMetrics0c(final Value ref) {
    this.ref = ref;

    this.vertexBaseOffsetIndex_00 = ref.offset(2, 0x00).cast(ShortRef::new);
    this.vertexXMod_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.vertexYMod_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.w_06 = ref.offset(2, 0x06).cast(ShortRef::new);
    this.h_08 = ref.offset(2, 0x08).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
