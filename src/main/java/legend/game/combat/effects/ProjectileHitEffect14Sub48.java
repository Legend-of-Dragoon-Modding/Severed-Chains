package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class ProjectileHitEffect14Sub48 implements MemoryRef {
  private final Value ref;

  public final BoolRef used_00;

  public final ArrayRef<VECTOR> _04;
  public final ArrayRef<SVECTOR> _24;
  public final UnsignedShortRef r_34;
  public final UnsignedShortRef g_36;
  public final UnsignedShortRef b_38;
  /** Amount to fade R each frame */
  public final UnsignedShortRef fadeR_3a;
  /** Amount to fade G each frame */
  public final UnsignedShortRef fadeG_3c;
  /** Amount to fade B each frame */
  public final UnsignedShortRef fadeB_3e;

  public final IntRef _40;
  /** Number of frames to decrease the colour over */
  public final UnsignedShortRef frames_44;

  public ProjectileHitEffect14Sub48(final Value ref) {
    this.ref = ref;

    this.used_00 = ref.offset(1, 0x00L).cast(BoolRef::new);

    this._04 = ref.offset(4, 0x04L).cast(ArrayRef.of(VECTOR.class, 2, 0x10, VECTOR::new));
    this._24 = ref.offset(2, 0x24L).cast(ArrayRef.of(SVECTOR.class, 2, 0x8, SVECTOR::new));
    this.r_34 = ref.offset(2, 0x34L).cast(UnsignedShortRef::new);
    this.g_36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this.b_38 = ref.offset(2, 0x38L).cast(UnsignedShortRef::new);
    this.fadeR_3a = ref.offset(2, 0x3aL).cast(UnsignedShortRef::new);
    this.fadeG_3c = ref.offset(2, 0x3cL).cast(UnsignedShortRef::new);
    this.fadeB_3e = ref.offset(2, 0x3eL).cast(UnsignedShortRef::new);

    this._40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this.frames_44 = ref.offset(2, 0x44L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
