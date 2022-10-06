package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

public class SMapStruct3c implements MemoryRef {
  private final Value ref;

  public final ShortRef _02;

  public final ShortRef _06;

  public final ShortRef _0c;
  public final ShortRef _0e;
  public final IntRef _10;
  public final IntRef _14;

  public final IntRef _1c;
  public final IntRef _20;
  public final IntRef _24;
  public final ShortRef _28;

  public final IntRef _2c;
  public final IntRef _30;
  public final IntRef z_34;
  public final Pointer<SMapStruct3c> parent_38;

  public SMapStruct3c(final Value ref) {
    this.ref = ref;

    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);

    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);

    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);

    this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this._24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this._28 = ref.offset(2, 0x28L).cast(ShortRef::new);

    this._2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this.z_34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this.parent_38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, SMapStruct3c::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
