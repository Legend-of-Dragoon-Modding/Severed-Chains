package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

/** 0xc bytes long */
public class ModelPartTransforms implements MemoryRef {
  private final Value ref;

  public final SVECTOR rotate_00;
  public final SVECTOR translate_06;

  public ModelPartTransforms(final Value ref) {
    this.ref = ref;

    this.rotate_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.translate_06 = ref.offset(2, 0x06L).cast(SVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
