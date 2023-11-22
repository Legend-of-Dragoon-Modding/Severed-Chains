package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class Location14 implements MemoryRef {
  private final Value ref;

  public final ShortRef directionalPathIndex_00;
  public final ShortRef placeIndex_02;
  public final ShortRef submapCut_04;
  public final ShortRef submapScene_06;
  public final ShortRef submapCut_08;
  public final ShortRef submapScene_0a;
  public final ShortRef _0c;
  /** 1-based, because why not */
  public final UnsignedByteRef continentNumber_0e;
  /** ubyte */
  public final BoolRef thumbnailShouldUseFullBrightness_10;
  /**
   * <ul>
   *   <li>0x0 = atmospheric effect 0 (no-op)</li>
   *   <li>0x4 = smoke mode 1</li>
   *   <li>0x8 = smoke mode 2</li>
   *   <li>0x10 = atmospheric effect 1 (clouds)</li>
   *   <li>0x20 = atmospheric effect 2 (snow)</li>
   * </ul>
   */
  public final UnsignedByteRef effectFlags_12;

  public Location14(final Value ref) {
    this.ref = ref;

    this.directionalPathIndex_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.placeIndex_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.submapCut_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.submapScene_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.submapCut_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.submapScene_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.continentNumber_0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this.thumbnailShouldUseFullBrightness_10 = ref.offset(1, 0x10L).cast(BoolRef::new);
    this.effectFlags_12 = ref.offset(1, 0x12L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
