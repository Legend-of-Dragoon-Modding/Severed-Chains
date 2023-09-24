package legend.game;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.sound.ReverbConfigAndLocation;
import legend.game.types.AdditionData0e;
import legend.game.types.LodString;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

  public static final ArrayRef<ShortRef> submapMusic_80050068 = MEMORY.ref(2, 0x80050068L, ArrayRef.of(ShortRef.class, 64, 2, ShortRef::new));
  public static final ArrayRef<IntRef> monsterSoundFileIndices_800500e8 = MEMORY.ref(4, 0x800500e8L, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<IntRef> characterSoundFileIndices_800500f8 = MEMORY.ref(4, 0x800500f8L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));

  public static final ArrayRef<IntRef> charSlotSpuOffsets_80050190 = MEMORY.ref(4, 0x80050190L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));

  public static final ArrayRef<UnsignedByteRef> combatSoundEffectsTypes_8005019c = MEMORY.ref(1, 0x8005019cL, ArrayRef.of(UnsignedByteRef.class, 32, 1, UnsignedByteRef::new));
  public static final ArrayRef<UnsignedIntRef> combatMusicFileIndices_800501bc = MEMORY.ref(4, 0x800501bcL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final IntRef _80050274 = MEMORY.ref(4, 0x80050274L, IntRef::new);

  public static final Value _8005027c = MEMORY.ref(4, 0x8005027cL);

  public static final Value _8005039c = MEMORY.ref(2, 0x8005039cL);

  public static final ArrayRef<ShortRef> _800503b0 = MEMORY.ref(2, 0x800503b0L, ArrayRef.of(ShortRef.class, 18, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _800503d4 = MEMORY.ref(2, 0x800503d4L, ArrayRef.of(ShortRef.class, 18, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _800503f8 = MEMORY.ref(2, 0x800503f8L, ArrayRef.of(ShortRef.class, 22, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _80050424 = MEMORY.ref(2, 0x80050424L, ArrayRef.of(ShortRef.class, 22, 2, ShortRef::new));

  public static final ArrayRef<Pointer<LodString>> combatItemNames_80050ae8 = MEMORY.ref(4, 0x80050ae8L, ArrayRef.of(Pointer.classFor(LodString.class), 0x40, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> itemCombatDescriptions_80051758 = MEMORY.ref(4, 0x80051758L, ArrayRef.of(Pointer.classFor(LodString.class), 0x40, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> spellCombatDescriptions_80052018 = MEMORY.ref(4, 0x80052018L, ArrayRef.of(Pointer.classFor(LodString.class), 0x54, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<LodString>> spells_80052734 = MEMORY.ref(4, 0x80052734L, ArrayRef.of(Pointer.classFor(LodString.class), 0x54, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<AdditionData0e> additionData_80052884 = MEMORY.ref(1, 0x80052884L, ArrayRef.of(AdditionData0e.class, 43, 0xe, AdditionData0e::new));

  public static final ArrayRef<Pointer<LodString>> _80052b40 = MEMORY.ref(4, 0x80052b40L, ArrayRef.of(Pointer.classFor(LodString.class), 10, 4, Pointer.deferred(2, LodString::new)));

  public static final Value _80052b68 = MEMORY.ref(2, 0x80052ba8L);

  public static final Value _80052b88 = MEMORY.ref(2, 0x80052ba8L);

  public static final Value _80052b8c = MEMORY.ref(2, 0x80052bacL);

  public static final Value _80052ba8 = MEMORY.ref(2, 0x80052ba8L);

  public static final Value _80052baa = MEMORY.ref(2, 0x80052baaL);

  public static final ArrayRef<IntRef> textboxVramX_80052bc8 = MEMORY.ref(4, 0x80052bc8L, ArrayRef.of(IntRef.class, 11, 4, IntRef::new));
  public static final ArrayRef<IntRef> textboxVramY_80052bf4 = MEMORY.ref(4, 0x80052bf4L, ArrayRef.of(IntRef.class, 11, 4, IntRef::new));
  public static final LodString _80052c20 = MEMORY.ref(2, 0x80052c20L, LodString::new);
  public static final IntRef submapCut_80052c30 = MEMORY.ref(4, 0x80052c30L, IntRef::new);
  public static final IntRef submapScene_80052c34 = MEMORY.ref(4, 0x80052c34L, IntRef::new);
  public static final IntRef index_80052c38 = MEMORY.ref(4, 0x80052c38L, IntRef::new);
  public static final IntRef submapCut_80052c3c = MEMORY.ref(4, 0x80052c3cL, IntRef::new);
  /** Moved from SMAP since it's referenced unconditionally when saving the game */
  public static int submapCutForSave_800cb450;
  /** Something related to submap camera and map transitioning */
  public static final BoolRef _80052c40 = MEMORY.ref(4, 0x80052c40L, BoolRef::new);
  public static final IntRef submapEnvState_80052c44 = MEMORY.ref(4, 0x80052c44L, IntRef::new);

  public static final Value _80052c6c = MEMORY.ref(4, 0x80052c6cL);

  public static final ArrayRef<IntRef> _80052d6c = MEMORY.ref(4, 0x80052d6cL, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final String[][] diskFmvs_80052d7c = {
    {"\\STR\\DEMOH.IKI", "\\STR\\DEMO2.IKI", "\\STR\\OPENH.IKI", "\\STR\\WAR1H.IKI"},
    {"\\STR\\TVRH.IKI", "\\STR\\GOAST.IKI", "\\STR\\ROZEH.IKI"},
    {"\\STR\\TREEH.IKI", "\\STR\\WAR2H.IKI", "\\STR\\BLACKH.IKI", "\\STR\\DRAGON1.IKI", "\\STR\\DENIN.IKI", "\\STR\\DENIN2.IKI", "\\STR\\DRAGON2.IKI", "\\STR\\DEIASH.IKI"},
    {"\\STR\\MOONH.IKI", "\\STR\\ENDING1H.IKI", "\\STR\\ENDING2H.IKI"}
  };

  public static final int vramWidth_800546c0 = 1024;
  public static final int vramHeight_800546c2 = 512;

  public static int matrixStackIndex_80054a08;
  public static final Matrix3f[] matrixStack_80054a0c = new Matrix3f[20];
  public static final Vector3f[] vectorStack_80054a0c = new Vector3f[20];
  static {
    Arrays.setAll(matrixStack_80054a0c, i -> new Matrix3f());
    Arrays.setAll(vectorStack_80054a0c, i -> new Vector3f());
  }

  /** Precomputed sin/cos table */
  public static final ArrayRef<ShortRef> sin_cos_80054d0c = MEMORY.ref(2, 0x80054d0cL, ArrayRef.of(ShortRef.class, 0x2000, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> atanTable_80058d0c = MEMORY.ref(2, 0x80058d0cL, ArrayRef.of(ShortRef.class, 0x401, 2, ShortRef::new));

  /**
   * Start of a fairly large block of data - something to do with SPU reverb initialisation. Stride is 66 bytes. Unknown length.
   */
  public static final ArrayRef<ReverbConfigAndLocation> reverbConfigs_80059f7c = MEMORY.ref(2, 0x80059f7cL, ArrayRef.of(ReverbConfigAndLocation.class, 9, 0x42, ReverbConfigAndLocation::new));

  /** short */
  public static int sssqFadeCurrent_8005a1ce;

  public static final Value _8005a1d8 = MEMORY.ref(4, 0x8005a1d8L);

  public static final BoolRef standingInSavePoint_8005a368 = MEMORY.ref(4, 0x8005a368L, BoolRef::new);

  public static final CombatantStruct1a8[] combatants_8005e398 = new CombatantStruct1a8[10];
}
