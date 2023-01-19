package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.deff.Anim;

public class TmdAnimationFile extends Anim {
  public final UnsignedShortRef count_0c;
  public final ShortRef _0e;
  public final UnboundedArrayRef<ModelPartTransforms> partTransforms_10;

  public TmdAnimationFile(final Value ref) {
    super(ref);

    this.count_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this.partTransforms_10 = ref.offset(2, 0x10L).cast(UnboundedArrayRef.of(0xc, ModelPartTransforms::new));
  }
}
