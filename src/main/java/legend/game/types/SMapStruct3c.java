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

  public final ShortRef x_0c;
  public final ShortRef y_0e;
  /** 16.16 fixed-point */
  public final IntRef _10;
  /** 16.16 fixed-point */
  public final IntRef _14;

  /** 16.16 fixed-point */
  public final IntRef _1c;
  /** 16.16 fixed-point */
  public final IntRef _20;
  /** 16.16 fixed-point */
  public final IntRef _24;
  public final ShortRef size_28;

  /** 16.16 fixed-point */
  public final IntRef _2c;
  /** 16.16 fixed-point */
  public final IntRef _30;
  public final IntRef z_34;
  public final Pointer<SMapStruct3c> parent_38;

  public SMapStruct3c(final Value ref) {
    this.ref = ref;

    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);

    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);

    this.x_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.y_0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);

    this._1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this._24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this.size_28 = ref.offset(2, 0x28L).cast(ShortRef::new);

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
