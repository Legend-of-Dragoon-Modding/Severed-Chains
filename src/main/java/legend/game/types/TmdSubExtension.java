package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;

public class TmdSubExtension implements MemoryRef {
  private final Value ref;

  public final ShortRef s_02;
  public final UnboundedArrayRef<ShortRef> sa_04;

  public TmdSubExtension(final Value ref) {
    this.ref = ref;

    this.s_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.sa_04 = ref.offset(2, 0x04L).cast(UnboundedArrayRef.of(0x2, ShortRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
