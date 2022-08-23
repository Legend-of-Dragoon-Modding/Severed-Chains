package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;

public class TmdAnimationFile implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef count_0c;
  public final ShortRef _0e;
  public final UnboundedArrayRef<ModelPartTransforms> partTransforms_10;

  public TmdAnimationFile(final Value ref) {
    this.ref = ref;

    this.count_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this.partTransforms_10 = ref.offset(2, 0x10L).cast(UnboundedArrayRef.of(0xc, ModelPartTransforms::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
