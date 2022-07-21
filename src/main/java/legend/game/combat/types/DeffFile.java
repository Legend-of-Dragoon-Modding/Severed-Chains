package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class DeffFile implements MemoryRef {
  private final Value ref;

  /** ascii DEFF */
  public final UnsignedIntRef magic_00;

  public final UnsignedShortRef pointerCount_06;
  public final UnboundedArrayRef<DeffPointerTable08> pointers_08;

  public DeffFile(final Value ref) {
    this.ref = ref;

    this.magic_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);

    this.pointerCount_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.pointers_08 = ref.offset(4, 0x08L).cast(UnboundedArrayRef.of(0x8, DeffPointerTable08.of(this)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
