package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class MemcardDataStruct3c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef fileIndex_04;

  public final UnsignedIntRef char0Index_08;
  public final UnsignedIntRef char1Index_0c;
  public final UnsignedIntRef char2Index_10;

  public final UnsignedByteRef level_14;
  public final UnsignedByteRef dlevel_15;
  public final ShortRef currentHp_16;
  public final ShortRef maxHp_18;

  public final UnsignedIntRef gold_1c;
  public final UnsignedIntRef time_20;
  public final UnsignedIntRef dragoonSpirits_24;
  public final UnsignedIntRef stardust_28;
  public final UnsignedByteRef locationIndex_2c;
  /**
   * 1 - world map
   * 3 - chapter title
   * other - submap
   */
  public final UnsignedByteRef saveType_2d;

  public MemcardDataStruct3c(final Value ref) {
    this.ref = ref;

    this.fileIndex_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);

    this.char0Index_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.char1Index_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.char2Index_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);

    this.level_14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);
    this.dlevel_15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);
    this.currentHp_16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this.maxHp_18 = ref.offset(2, 0x18L).cast(ShortRef::new);

    this.gold_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.time_20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this.dragoonSpirits_24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this.stardust_28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this.locationIndex_2c = ref.offset(1, 0x2cL).cast(UnsignedByteRef::new);
    this.saveType_2d = ref.offset(1, 0x2dL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
