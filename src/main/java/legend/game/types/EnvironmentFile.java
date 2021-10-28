package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class EnvironmentFile implements MemoryRef {
  private final Value ref;

  public final SVECTOR viewpoint_00;
  public final SVECTOR refpoint_08;
  public final UnsignedShortRef projectionDistance_10;
  public final ShortRef rotation_12;
  public final UnsignedByteRef count_14;
  public final UnsignedByteRef ub_15;
  public final UnsignedByteRef ub_16;
  public final UnboundedArrayRef<EnvironmentStruct> environments_18;

  public EnvironmentFile(final Value ref) {
    this.ref = ref;

    this.viewpoint_00 = ref.offset(4, 0x00L).cast(SVECTOR::new);
    this.refpoint_08 = ref.offset(4, 0x08L).cast(SVECTOR::new);
    this.projectionDistance_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.rotation_12 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this.count_14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);
    this.ub_15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);
    this.ub_16 = ref.offset(1, 0x16L).cast(UnsignedByteRef::new);
    this.environments_18 = ref.offset(4, 0x18L).cast(UnboundedArrayRef.of(0x24, EnvironmentStruct::new, this::getCount));
  }

  private int getCount() {
    return this.count_14.get();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
