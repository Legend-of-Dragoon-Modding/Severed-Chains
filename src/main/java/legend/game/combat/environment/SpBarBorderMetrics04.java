package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;

public class SpBarBorderMetrics04 implements MemoryRef {
  public final Value ref;

  public final ByteRef x1_00;
  public final ByteRef y1_01;
  public final ByteRef x2_02;
  public final ByteRef y2_03;

  public SpBarBorderMetrics04(final Value ref) {
    this.ref = ref;

    this.x1_00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this.y1_01 = ref.offset(1, 0x01).cast(ByteRef::new);
    this.x2_02 = ref.offset(1, 0x02).cast(ByteRef::new);
    this.y2_03 = ref.offset(1, 0x03).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
