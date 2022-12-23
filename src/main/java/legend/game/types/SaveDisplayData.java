package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class SaveDisplayData implements MemoryRef {
  private final Value ref;

  public final IntRef magic_00;
  public final ArrayRef<IntRef> characterIds_08;
  public final UnsignedByteRef level_14;
  public final UnsignedByteRef dlevel_15;
  public final UnsignedShortRef hp_16;
  public final UnsignedShortRef maxHp_18;
  public final IntRef gold_1c;
  public final IntRef timestamp_20;
  public final IntRef dragoonSpirits_24;
  public final IntRef stardust_28;
  public final UnsignedByteRef placeIndex_2c;
  /**
   * <ul>
   *   <li>0 - place (submap)</li>
   *   <li>1 - continent (world map)</li>
   *   <li>3 - chapter (between-disk saves)</li>
   * </ul>
   */
  public final UnsignedByteRef placeType_2d;

  public final IntRef _30;

  public SaveDisplayData(final Value ref) {
    this.ref = ref;

    this.magic_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.characterIds_08 = ref.offset(4, 0x08L).cast(ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
    this.level_14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);
    this.dlevel_15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);
    this.hp_16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this.maxHp_18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this.gold_1c = ref.offset(4, 0x1cL).cast(IntRef::new);
    this.timestamp_20 = ref.offset(4, 0x20L).cast(IntRef::new);
    this.dragoonSpirits_24 = ref.offset(4, 0x24L).cast(IntRef::new);
    this.stardust_28 = ref.offset(4, 0x28L).cast(IntRef::new);
    this.placeIndex_2c = ref.offset(1, 0x2cL).cast(UnsignedByteRef::new);
    this.placeType_2d = ref.offset(1, 0x2dL).cast(UnsignedByteRef::new);

    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
