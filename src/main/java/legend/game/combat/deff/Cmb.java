package legend.game.combat.deff;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;

public class Cmb extends Anim {
  public static final int MAGIC = 0x2042_4d43;

  public final UnsignedShortRef count_0c;
  public final ShortRef _0e;
  public final UnboundedArrayRef<Sub0c> _10;

  public Cmb(final Value ref) {
    super(ref);

    this.count_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnboundedArrayRef.of(0xc, Sub0c::new));
  }

  public static class Sub0c implements MemoryRef {
    private final Value ref;

    public final SVECTOR rot_00;
    public final SVECTOR trans_06;

    public Sub0c(final Value ref) {
      this.ref = ref;

      this.rot_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
      this.trans_06 = ref.offset(2, 0x06L).cast(SVECTOR::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
