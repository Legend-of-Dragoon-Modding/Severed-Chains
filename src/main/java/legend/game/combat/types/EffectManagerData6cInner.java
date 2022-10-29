package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;

public class EffectManagerData6cInner implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final VECTOR vec_04;
  public final SVECTOR svec_10;
  public final SVECTOR svec_16;
  public final SVECTOR svec_1c;
  public final ShortRef z_22;
  public final UnsignedIntRef _24;
  public final VECTOR vec_28;

  public EffectManagerData6cInner(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.vec_04 = ref.offset(4, 0x04L).cast(VECTOR::new);
    this.svec_10 = ref.offset(2, 0x10L).cast(SVECTOR::new);
    this.svec_16 = ref.offset(2, 0x16L).cast(SVECTOR::new);
    this.svec_1c = ref.offset(2, 0x1cL).cast(SVECTOR::new);
    this.z_22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this.vec_28 = ref.offset(4, 0x28L).cast(VECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
