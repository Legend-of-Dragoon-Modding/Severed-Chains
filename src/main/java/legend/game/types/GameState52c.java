package legend.game.types;

import legend.core.Config;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class GameState52c implements MemoryRef {
  private final Value ref;

  /** Maybe flags? Maybe individual bytes? */
  public final IntRef _04;
  /**
   * <ul>
   *   <li>18 - stardust turned in</li>
   *   <li>20 - Zy: changed when I did the battles in the marshland. I believe this reset when I went back to get the stardust</li>
   *   <li>21 - Zy: changes when you are on the boat in the marshland</li>
   *   <li>27 - hero tickets - Zy: used as byte, displays as short, actually is an int, blocks if over 99, typical LOD code</li>
   * </ul>
   */
  public final ArrayRef<IntRef> scriptData_08;
  public final ArrayRef<IntRef> charIndex_88;
  public final IntRef gold_94;
  public final IntRef chapterIndex_98;
  public final IntRef stardust_9c;
  public final IntRef timestamp_a0;
  /** Not 100% sure on this */
  public final IntRef submapScene_a4;
  public final IntRef submapCut_a8;

  /** Used by the script engine */
  public final IntRef _b0;
  public final IntRef _b4;
  public final IntRef _b8;
  /**
   * <ul>
   *   <li>0x40000 - has psych bomb X</li>
   * </ul>
   */
  public final ArrayRef<IntRef> scriptFlags2_bc;
  public final ArrayRef<IntRef> scriptFlags1_13c;
  public final ArrayRef<IntRef> _15c;
  public final ArrayRef<IntRef> _17c;
  public final ArrayRef<IntRef> dragoonSpirits_19c;
  /** Not sure if this is actually 8 elements long, has at least 3. Related to submap music. */
  public final ArrayRef<IntRef> _1a4;
  /** Note: I'm not _100%_ sure this is only chest flags */
  public final ArrayRef<IntRef> chestFlags_1c4;
  public final ShortRef equipmentCount_1e4;
  public final ShortRef itemCount_1e6;
  public final ArrayRef<UnsignedByteRef> equipment_1e8;
  public final ArrayRef<UnsignedByteRef> items_2e9;

  public final ArrayRef<CharacterData2c> charData_32c;
  public final ArrayRef<IntRef> _4b8;

  // World map stuff
  public final UnsignedShortRef pathIndex_4d8;
  public final UnsignedShortRef dotIndex_4da;
  public final UnsignedByteRef dotOffset_4dc;
  public final ByteRef facing_4dd;
  public final UnsignedShortRef areaIndex_4de;

  // Config stuff
  public final UnsignedByteRef mono_4e0;
  public final UnsignedByteRef vibrationEnabled_4e1;
  public final UnsignedByteRef morphMode_4e2;
  public final UnsignedByteRef indicatorsDisabled_4e3;
  public final UnsignedByteRef isOnWorldMap_4e4;

  public final UnsignedShortRef _4e6;
  /** Controls how the indicators (triangles) are drawn (called "Note" in options menu) */
  public final UnsignedIntRef indicatorMode_4e8;

  public GameState52c(final Value ref) {
    this.ref = ref;

    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.scriptData_08 = ref.offset(4, 0x08L).cast(ArrayRef.of(IntRef.class, 0x20, 4, IntRef::new));
    this.charIndex_88 = ref.offset(4, 0x88L).cast(ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
    this.gold_94 = ref.offset(4, 0x94L).cast(IntRef::new);
    this.chapterIndex_98 = ref.offset(4, 0x98L).cast(IntRef::new);
    this.stardust_9c = ref.offset(4, 0x9cL).cast(IntRef::new);
    this.timestamp_a0 = ref.offset(4, 0xa0L).cast(IntRef::new);
    this.submapScene_a4 = ref.offset(4, 0xa4L).cast(IntRef::new);
    this.submapCut_a8 = ref.offset(4, 0xa8L).cast(IntRef::new);

    this._b0 = ref.offset(4, 0xb0L).cast(IntRef::new);
    this._b4 = ref.offset(4, 0xb4L).cast(IntRef::new);
    this._b8 = ref.offset(4, 0xb8L).cast(IntRef::new);
    this.scriptFlags2_bc = ref.offset(4, 0xbcL).cast(ArrayRef.of(IntRef.class, 0x20, 4, IntRef::new));
    this.scriptFlags1_13c = ref.offset(4, 0x13cL).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this._15c = ref.offset(4, 0x15cL).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this._17c = ref.offset(4, 0x17cL).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this.dragoonSpirits_19c = ref.offset(4, 0x19cL).cast(ArrayRef.of(IntRef.class, 2, 4, IntRef::new));
    this._1a4 = ref.offset(4, 0x1a4L).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this.chestFlags_1c4 = ref.offset(4, 0x1c4L).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this.equipmentCount_1e4 = ref.offset(2, 0x1e4L).cast(ShortRef::new);
    this.itemCount_1e6 = ref.offset(2, 0x1e6L).cast(ShortRef::new);
    this.equipment_1e8 = ref.offset(1, 0x1e8L).cast(ArrayRef.of(UnsignedByteRef.class, 0x101, 1, UnsignedByteRef::new));
    this.items_2e9 = ref.offset(1, 0x2e9L).cast(ArrayRef.of(UnsignedByteRef.class, Config.inventorySize() + 1, 1, UnsignedByteRef::new));

    this.charData_32c = ref.offset(4, 0x32cL).cast(ArrayRef.of(CharacterData2c.class, 9, 0x2c, CharacterData2c::new));
    this._4b8 = ref.offset(4, 0x4b8L).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this.pathIndex_4d8 = ref.offset(2, 0x4d8L).cast(UnsignedShortRef::new);
    this.dotIndex_4da = ref.offset(2, 0x4daL).cast(UnsignedShortRef::new);
    this.dotOffset_4dc = ref.offset(1, 0x4dcL).cast(UnsignedByteRef::new);
    this.facing_4dd = ref.offset(1, 0x4ddL).cast(ByteRef::new);
    this.areaIndex_4de = ref.offset(2, 0x4deL).cast(UnsignedShortRef::new);
    this.mono_4e0 = ref.offset(1, 0x4e0L).cast(UnsignedByteRef::new);
    this.vibrationEnabled_4e1 = ref.offset(1, 0x4e1L).cast(UnsignedByteRef::new);
    this.morphMode_4e2 = ref.offset(1, 0x4e2L).cast(UnsignedByteRef::new);
    this.indicatorsDisabled_4e3 = ref.offset(1, 0x4e3L).cast(UnsignedByteRef::new);
    this.isOnWorldMap_4e4 = ref.offset(1, 0x4e4L).cast(UnsignedByteRef::new);

    this._4e6 = ref.offset(2, 0x4e6L).cast(UnsignedShortRef::new);
    this.indicatorMode_4e8 = ref.offset(4, 0x4e8L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
