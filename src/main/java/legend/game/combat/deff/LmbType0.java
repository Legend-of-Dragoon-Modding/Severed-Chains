package legend.game.combat.deff;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;

public class LmbType0 extends Lmb {
  public final UnboundedArrayRef<PartInfo0c> _08;

  /** TODO not sure if this actually belongs here */
  public final ShortRef _0c;

  public LmbType0(final Value ref) {
    super(ref);

    this._08 = ref.offset(4, 0x08L).cast(UnboundedArrayRef.of(0xc, value -> new PartInfo0c(value, this)));

    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
  }

  public static class PartInfo0c implements MemoryRef {
    private final Value ref;

    public final RelativePointer<UnboundedArrayRef<LmbTransforms14>> _08;

    public PartInfo0c(final Value ref, final LmbType0 lmb) {
      this.ref = ref;

      this._08 = ref.offset(4, 0x08L).cast(RelativePointer.deferred(4, lmb.getAddress(), UnboundedArrayRef.of(0x14, LmbTransforms14::new)));
    }

    @Override
    public long getAddress() {
      return this.ref.get();
    }
  }
}
