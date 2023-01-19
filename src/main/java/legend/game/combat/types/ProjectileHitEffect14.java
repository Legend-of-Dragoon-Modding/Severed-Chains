package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class ProjectileHitEffect14 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedShortRef count_00;

  public final UnsignedIntRef _04;
  public final Pointer<UnboundedArrayRef<ProjectileHitEffect14Sub48>> _08;

  public ProjectileHitEffect14(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x48, ProjectileHitEffect14Sub48::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
