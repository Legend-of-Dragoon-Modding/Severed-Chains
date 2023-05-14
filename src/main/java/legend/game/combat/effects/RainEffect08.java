package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

public class RainEffect08 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef count_00;
  public final Pointer<UnboundedArrayRef<RaindropEffect0c>> raindropArray_04;

  public RainEffect08(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(4, 0x00).cast(IntRef::new);
    this.raindropArray_04 = ref.offset(4, 0x04).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x0c, RaindropEffect0c::new, this.count_00::get)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
