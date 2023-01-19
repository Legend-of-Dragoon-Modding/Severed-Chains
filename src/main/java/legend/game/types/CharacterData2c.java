package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class CharacterData2c implements MemoryRef {
  private final Value ref;

  public final IntRef xp_00;
  /**
   * <ul>
   *   <li>0x1 - in party</li>
   *   <li>0x2 - can be put in main party (without this flag a char can only appear in secondary and can't be put into main)</li>
   *   <li>0x20 - can't remove (don't select, can't be taken out of main party)</li>
   * </ul>
   */
  public final IntRef partyFlags_04;
  public final UnsignedShortRef hp_08;
  public final UnsignedShortRef mp_0a;
  public final UnsignedShortRef sp_0c;
  public final UnsignedShortRef dlevelXp_0e;
  /** i.e. poison */
  public final UnsignedShortRef status_10;
  public final UnsignedByteRef level_12;
  public final UnsignedByteRef dlevel_13;
  public final ArrayRef<UnsignedByteRef> equipment_14;

  public final ByteRef selectedAddition_19;
  public final ArrayRef<UnsignedByteRef> additionLevels_1a;
  public final ArrayRef<UnsignedByteRef> additionXp_22;

  public CharacterData2c(final Value ref) {
    this.ref = ref;

    this.xp_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.partyFlags_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.hp_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.mp_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this.sp_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this.dlevelXp_0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this.status_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.level_12 = ref.offset(1, 0x12L).cast(UnsignedByteRef::new);
    this.dlevel_13 = ref.offset(1, 0x13L).cast(UnsignedByteRef::new);
    this.equipment_14 = ref.offset(1, 0x14L).cast(ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));

    this.selectedAddition_19 = ref.offset(1, 0x19L).cast(ByteRef::new);
    this.additionLevels_1a = ref.offset(1, 0x1aL).cast(ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));
    this.additionXp_22 = ref.offset(1, 0x22L).cast(ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
