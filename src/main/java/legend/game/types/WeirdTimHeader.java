package legend.game.types;

import legend.core.gpu.RECT;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class WeirdTimHeader implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef flags;
  public final Pointer<RECT> clutRect;
  public final UnsignedIntRef clutAddress;
  public final Pointer<RECT> imageRect;
  public final UnsignedIntRef imageAddress;

  public WeirdTimHeader(final Value ref) {
    this.ref = ref;

    this.flags = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.clutRect = ref.offset(4, 0x04L).cast(Pointer.of(4, RECT::new));
    this.clutAddress = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.imageRect = ref.offset(4, 0x0cL).cast(Pointer.of(4, RECT::new));
    this.imageAddress = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
