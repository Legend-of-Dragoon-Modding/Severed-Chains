package legend.game.combat.deff;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class LmbType1 extends Lmb {
  public final ShortRef _08;
  public final ShortRef _0a;

  public final RelativePointer<UnboundedArrayRef<Sub04>> _0c;
  public final RelativePointer<UnboundedArrayRef<LmbTransforms14>> _10;
  public final RelativePointer<UnboundedArrayRef<ShortRef>> _14;

  public LmbType1(final Value ref) {
    super(ref);

    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);

    this._0c = ref.offset(4, 0x0cL).cast(RelativePointer.deferred(4, ref.getAddress(), UnboundedArrayRef.of(0x04, Sub04::new, this.count_04::get)));
    this._10 = ref.offset(4, 0x10L).cast(RelativePointer.deferred(4, ref.getAddress(), UnboundedArrayRef.of(0x14, LmbTransforms14::new, this.count_04::get)));
    this._14 = ref.offset(4, 0x14L).cast(RelativePointer.deferred(2, ref.getAddress(), UnboundedArrayRef.of(0x02, ShortRef::new)));
  }

  public static class Sub04 implements MemoryRef {
    private final Value ref;

    public final UnsignedShortRef _00;
    public final UnsignedByteRef _03;

    public Sub04(final Value ref) {
      this.ref = ref;

      this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
      this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    }

    @Override
    public long getAddress() {
      return this.ref.getAddress();
    }
  }
}
