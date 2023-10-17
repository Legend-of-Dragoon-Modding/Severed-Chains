package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class TextboxBorderMetrics0c implements MemoryRef {
  private final Value ref;

  public final ShortRef topLeftVertexIndex_00;
  public final ShortRef bottomRightVertexIndex_02;
  public final ShortRef u_04;
  public final ShortRef v_06;
  public final ShortRef w_08;
  public final ShortRef h_0a;

  public TextboxBorderMetrics0c(final Value ref) {
    this.ref = ref;

    this.topLeftVertexIndex_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.bottomRightVertexIndex_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.u_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.v_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.w_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.h_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
