package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;

public class LightningBoltEffect14 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef unused_00;

  public final ShortRef angle_02;
  public final SVECTOR rotation_04;
  /** Average z-index */
  public final IntRef sz3_0c;
  public final Pointer<UnboundedArrayRef<LightningBoltEffectSegment30>> boltSegments_10;

  public LightningBoltEffect14(final Value ref) {
    this.ref = ref;

    this.unused_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this.angle_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.rotation_04 = ref.offset(2, 0x04L).cast(SVECTOR::new);
    this.sz3_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.boltSegments_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x30, LightningBoltEffectSegment30::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
