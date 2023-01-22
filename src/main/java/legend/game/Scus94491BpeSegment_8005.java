package legend.game;

import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.types.AdditionData0e;
import legend.game.types.DeferredReallocOrFree0c;
import legend.game.types.FileEntry08;
import legend.game.types.LoadingOverlay;
import legend.game.types.LodString;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_8005 {
  private Scus94491BpeSegment_8005() { }

  public static final ArrayRef<ShortRef> submapMusic_80050068 = MEMORY.ref(2, 0x80050068L, ArrayRef.of(ShortRef.class, 64, 2, ShortRef::new));
  public static final ArrayRef<IntRef> monsterSoundFileIndices_800500e8 = MEMORY.ref(4, 0x800500e8L, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<IntRef> characterSoundFileIndices_800500f8 = MEMORY.ref(4, 0x800500f8L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  public static final ArrayRef<IntRef> partySoundEffectPermutationFileIndices_80050104 = MEMORY.ref(4, 0x80050104L, ArrayRef.of(IntRef.class, 26, 4, IntRef::new));

  public static final Value _80050190 = MEMORY.ref(4, 0x80050190L);

  public static final ArrayRef<UnsignedByteRef> combatSoundEffectsTypes_8005019c = MEMORY.ref(1, 0x8005019cL, ArrayRef.of(UnsignedByteRef.class, 32, 1, UnsignedByteRef::new));
  public static final ArrayRef<UnsignedIntRef> combatMusicFileIndices_800501bc = MEMORY.ref(4, 0x800501bcL, ArrayRef.of(UnsignedIntRef.class, 32, 4, UnsignedIntRef::new));

  public static final IntRef _80050274 = MEMORY.ref(4, 0x80050274L, IntRef::new);

  public static final Value _8005027c = MEMORY.ref(4, 0x8005027cL);

  public static final Value _8005039c = MEMORY.ref(2, 0x8005039cL);

  public static final Value _800503b0 = MEMORY.ref(2, 0x800503b0L);

  public static final Value _800503d4 = MEMORY.ref(2, 0x800503d4L);

  public static final Value _800503f8 = MEMORY.ref(2, 0x800503f8L);

  public static final Value _80050424 = MEMORY.ref(2, 0x80050424L);

  public static final ArrayRef<Pointer<LodString>> _80050ae8 = MEMORY.ref(4, 0x80050ae8L, ArrayRef.of(Pointer.classFor(LodString.class), 0x40, 4, Pointer.deferred(4, LodString::new)));

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
  public static final Value _80052c40 = MEMORY.ref(4, 0x80052c40L);
  public static final Value _80052c44 = MEMORY.ref(4, 0x80052c44L);
  public static final Value _80052c48 = MEMORY.ref(4, 0x80052c48L);
  public static final FileEntry08 _80052c4c = MEMORY.ref(2, 0x80052c4cL, FileEntry08::new);

  public static final Value _80052c6c = MEMORY.ref(4, 0x80052c6cL);

  public static final FileEntry08 lodXa00Xa_80052c74 = MEMORY.ref(2, 0x80052c74L, FileEntry08::new);

  public static final FileEntry08 lodXa00Xa_80052c94 = MEMORY.ref(2, 0x80052c94L, FileEntry08::new);

  public static final ArrayRef<IntRef> _80052d6c = MEMORY.ref(4, 0x80052d6cL, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<Pointer<UnboundedArrayRef<FileEntry08>>> diskFmvs_80052d7c = MEMORY.ref(4, 0x80052d7cL, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(FileEntry08.class)), 5, 0x4, Pointer.deferred(4, UnboundedArrayRef.of(0x8, FileEntry08::new))));

  public static final Value _80054674 = MEMORY.ref(4, 0x80054674L);

  public static final Value _800546bc = MEMORY.ref(1, 0x800546bcL);
  public static final Value _800546bd = MEMORY.ref(1, 0x800546bdL);
  public static final Value gpu_debug = MEMORY.ref(1, 0x800546beL);

  /**
   * Max RECT width?
   */
  public static final Value _800546c0 = MEMORY.ref(2, 0x800546c0L);
  /**
   * Max RECT height?
   */
  public static final Value _800546c2 = MEMORY.ref(2, 0x800546c2L);

  public static final DRAWENV DRAWENV_800546cc = MEMORY.ref(4, 0x800546ccL, DRAWENV::new);
  public static final DISPENV DISPENV_80054728 = MEMORY.ref(4, 0x80054728L, DISPENV::new);

  public static final Value array_8005473c = MEMORY.ref(2, 0x8005473cL);
  public static final Value array_80054748 = MEMORY.ref(2, 0x80054748L);

  public static final Value GsOUT_PACKET_P = MEMORY.ref(4, 0x8005480cL);

  public static final Value matrixStackIndex_80054a08 = MEMORY.ref(4, 0x80054a08L);
  public static final ArrayRef<MATRIX> matrixStack_80054a0c = MEMORY.ref(4, 0x80054a0cL, ArrayRef.of(MATRIX.class, 20, 32, MATRIX::new));

  /** Precomputed sin/cos table */
  public static final Value sin_cos_80054d0c = MEMORY.ref(4, 0x80054d0cL);

  public static final Value atanTable_80058d0c = MEMORY.ref(2, 0x80058d0cL);

  public static final Value _8005967c = MEMORY.ref(2, 0x8005967cL);

  public static final Value _80059b3c = MEMORY.ref(1, 0x80059b3cL);

  public static final Value _80059f3c = MEMORY.ref(1, 0x80059f3cL);

  /**
   * Start of a fairly large block of data - something to do with SPU reverb initialisation. Stride is 66 bytes. Unknown length.
   */
  public static final Value _80059f7c = MEMORY.ref(2, 0x80059f7cL);

  public static final Value sssqFadeCurrent_8005a1ce = MEMORY.ref(2, 0x8005a1ceL);
  public static final Value sssqStatus_8005a1d0 = MEMORY.ref(4, 0x8005a1d0L);

  public static final Value _8005a1d8 = MEMORY.ref(4, 0x8005a1d8L);

  public static final ArrayRef<DeferredReallocOrFree0c> deferredReallocOrFree_8005a1e0 = MEMORY.ref(4, 0x8005a1e0L, ArrayRef.of(DeferredReallocOrFree0c.class, 16, 0xc, DeferredReallocOrFree0c::new));
  public static final Value heapHead_8005a2a0 = MEMORY.ref(4, 0x8005a2a0L);
  public static final Value heapTail_8005a2a4 = MEMORY.ref(4, 0x8005a2a4L);
  public static LoadingOverlay loadingOverlay_8005a2a8;
  public static final IntRef standingInSavePoint_8005a368 = MEMORY.ref(4, 0x8005a368L, IntRef::new);

  public static final CombatantStruct1a8[] combatants_8005e398 = new CombatantStruct1a8[10];
}
