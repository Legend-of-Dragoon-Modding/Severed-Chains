package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class AdditionStarburstEffect10 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef scriptIndex_00;
  public final UnsignedShortRef rayCount_04;

  public final UnsignedIntRef unused_08;
  /** TODO ptr */
  public final Pointer<UnboundedArrayRef<AdditionStarburstEffectRay10>> rayArray_0c;

  public AdditionStarburstEffect10(final Value ref) {
    this.ref = ref;

    this.scriptIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.rayCount_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.unused_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.rayArray_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, AdditionStarburstEffectRay10::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
