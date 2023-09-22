package legend.game.tmd;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class UvAdjustmentMetrics14 implements MemoryRef {
  public final Value ref;

  public final IntRef clutMaskOff_00;
  public final IntRef clutMaskOn_04;
  public final IntRef tpageMaskOff_08;
  public final IntRef tpageMaskOn_0c;
  public final IntRef uvOffset_10;

  public UvAdjustmentMetrics14(final Value ref) {
    this.ref = ref;

    this.clutMaskOff_00 = ref.offset(4, 0x00).cast(IntRef::new);
    this.clutMaskOn_04 = ref.offset(4, 0x04).cast(IntRef::new);
    this.tpageMaskOff_08 = ref.offset(4, 0x08).cast(IntRef::new);
    this.tpageMaskOn_0c = ref.offset(4, 0x0c).cast(IntRef::new);
    this.uvOffset_10 = ref.offset(4, 0x10).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
