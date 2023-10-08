package legend.game.credits;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class CreditType08 implements MemoryRef {
  private final Value ref;

  public final IntRef creditIndex_00;
  public final IntRef type_04;

  public CreditType08(final Value ref) {
    this.ref = ref;

    this.creditIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.type_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
