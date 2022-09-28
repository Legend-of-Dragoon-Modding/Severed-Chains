package legend.game;

import legend.core.cdrom.CdlDIR;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.FileLoadingInfo;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.VoidRef;
import legend.game.combat.types.BattleRenderStruct;
import legend.game.types.ActiveStatsa0;
import legend.game.types.BigStruct;
import legend.game.types.Drgn0_6666File;
import legend.game.types.EquipmentStats1c;
import legend.game.types.GameState52c;
import legend.game.types.GsRVIEW2;
import legend.game.types.InventoryMenuState;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;
import legend.game.types.Renderable58;
import legend.game.types.RunningScript;
import legend.game.types.ScriptEffectStruct;
import legend.game.types.ScriptState;
import legend.game.types.SoundFile;
import legend.game.types.SpuStruct08;
import legend.game.types.SpuStruct28;
import legend.game.types.SshdFile;
import legend.game.types.SssqFile;
import legend.game.types.Struct4c;
import legend.game.types.Struct84;

import java.util.function.Function;

import static legend.core.Hardware.MEMORY;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static final int DSL_MAX_DIR = 0x80;
  public static final int DSL_MAX_FILE = 0x40;

  public static final Value _800babc0 = MEMORY.ref(4, 0x800babc0L);

  public static final GameState52c gameState_800babc8 = MEMORY.ref(4, 0x800babc8L, GameState52c::new);

  // End of game state 800bb0f4

  public static final IntRef submapStage_800bb0f4 = MEMORY.ref(4, 0x800bb0f4L, IntRef::new);
  public static final IntRef encounterId_800bb0f8 = MEMORY.ref(4, 0x800bb0f8L, IntRef::new);
  public static final Value _800bb0fc = MEMORY.ref(4, 0x800bb0fcL);

  public static final Value _800bb104 = MEMORY.ref(4, 0x800bb104L);
  public static final Value doubleBufferFrame_800bb108 = MEMORY.ref(4, 0x800bb108L);
  public static final Value pregameLoadingStage_800bb10c = MEMORY.ref(4, 0x800bb10cL);
  public static final Value _800bb110 = MEMORY.ref(2, 0x800bb110L);
  public static final Value _800bb112 = MEMORY.ref(2, 0x800bb112L);
  public static final Value _800bb114 = MEMORY.ref(2, 0x800bb114L);
  public static final Value _800bb116 = MEMORY.ref(2, 0x800bb116L);
  public static final Value _800bb118 = MEMORY.ref(2, 0x800bb118L);
  public static final Value _800bb11a = MEMORY.ref(2, 0x800bb11aL);

  public static final Value _800bb120 = MEMORY.ref(2, 0x800bb120L);

  public static final Value _800bb134 = MEMORY.ref(2, 0x800bb134L);

  public static final ScriptEffectStruct scriptEffect_800bb140 = MEMORY.ref(4, 0x800bb140L, ScriptEffectStruct::new);
  public static final Value _800bb168 = MEMORY.ref(4, 0x800bb168L); //TODO is this part of the previous struct?

  public static final ArrayRef<UnsignedIntRef> array_800bb198 = MEMORY.ref(4, 0x800bb198L, ArrayRef.of(UnsignedIntRef.class, 36, 4, UnsignedIntRef::new));

  public static final Value _800bb228 = MEMORY.ref(4, 0x800bb228L);

  public static final Value _800bb348 = MEMORY.ref(4, 0x800bb348L);

  public static final Value transferDest_800bb460 = MEMORY.ref(4, 0x800bb460L);
  public static final Value fileSize_800bb464 = MEMORY.ref(4, 0x800bb464L);
  public static final FileLoadingInfo currentlyLoadingFileInfo_800bb468 = MEMORY.ref(4, 0x800bb468L, FileLoadingInfo::new);
  public static final Value fileTransferDest_800bb488 = MEMORY.ref(4, 0x800bb488L);
  public static final Value fileSize_800bb48c = MEMORY.ref(4, 0x800bb48cL);
  public static final Value numberOfTransfers_800bb490 = MEMORY.ref(4, 0x800bb490L);

  public static final ArrayRef<CdlFILE> CdlFILE_800bb4c8 = MEMORY.ref(0x600, 0x800bb4c8L, ArrayRef.of(CdlFILE.class, 0x40, 0x18, CdlFILE::new));

  public static final Value _800bbac8 = MEMORY.ref(1, 0x800bbac8L);

  public static final Value linkedListEntry_800bbacc = MEMORY.ref(4, 0x800bbaccL);
  public static final BoolRef SInitBinLoaded_800bbad0 = MEMORY.ref(1, 0x800bbad0L, BoolRef::new);

  public static final ArrayRef<FileLoadingInfo> fileLoadingInfoArray_800bbad8 = MEMORY.ref(0x580, 0x800bbad8L, ArrayRef.of(FileLoadingInfo.class, 44, 32, FileLoadingInfo::new));
  public static final IntRef drgnBinIndex_800bc058 = MEMORY.ref(4, 0x800bc058L, IntRef::new);
  public static final Value _800bc05c = MEMORY.ref(4, 0x800bc05cL);
  public static final ArrayRef<Pointer<MrgFile>> drgnMrg_800bc060 = MEMORY.ref(4, 0x800bc060L, ArrayRef.of(Pointer.classFor(MrgFile.class), 4, 4, Pointer.deferred(4, MrgFile::new)));
  public static final RunningScript RunningScript_800bc070 = MEMORY.ref(4, 0x800bc070L, RunningScript::new);
  public static final BoolRef scriptsTickDisabled_800bc0b8 = MEMORY.ref(1, 0x800bc0b8L, BoolRef::new);
  public static final BoolRef scriptsDisabled_800bc0b9 = MEMORY.ref(1, 0x800bc0b9L, BoolRef::new);

  public static final ScriptState<VoidRef> scriptState_800bc0c0 = MEMORY.ref(4, 0x800bc0c0L, ScriptState.of(VoidRef::new));
  public static final ArrayRef<Pointer<ScriptState<? extends MemoryRef>>> scriptStatePtrArr_800bc1c0 = (ArrayRef<Pointer<ScriptState<? extends MemoryRef>>>)MEMORY.ref(4, 0x800bc1c0L, ArrayRef.of(Pointer.classFor(ScriptState.class), 0x48, 4, (Function)Pointer.deferred(4, ScriptState.of(ref -> { throw new RuntimeException("Can't auto-instantiate"); }))));
  public static final TimHeader timHeader_800bc2e0 = MEMORY.ref(0x1c, 0x800bc2e0L, TimHeader::new);

  public static final Value _800bc300 = MEMORY.ref(4, 0x800bc300L);
  public static final Value _800bc304 = MEMORY.ref(4, 0x800bc304L);
  public static final Value _800bc308 = MEMORY.ref(4, 0x800bc308L);

  public static final Value _800bc94c = MEMORY.ref(4, 0x800bc94cL);

  /** TODO vec3 or maybe 3 values indexed by char slot? */
  public static final Value _800bc910 = MEMORY.ref(4, 0x800bc910L);
  public static final Value _800bc914 = MEMORY.ref(4, 0x800bc914L);
  public static final Value _800bc918 = MEMORY.ref(4, 0x800bc918L);
  public static final Value _800bc91c = MEMORY.ref(4, 0x800bc91cL);
  public static final IntRef goldGainedFromCombat_800bc920 = MEMORY.ref(4, 0x800bc920L, IntRef::new);

  public static final Value _800bc928 = MEMORY.ref(4, 0x800bc928L);

  public static final ArrayRef<IntRef> spGained_800bc950 = MEMORY.ref(4, 0x800bc950L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  public static final IntRef totalXpFromCombat_800bc95c = MEMORY.ref(4, 0x800bc95cL, IntRef::new);
  public static final Value _800bc960 = MEMORY.ref(4, 0x800bc960L);

  public static final Value _800bc968 = MEMORY.ref(4, 0x800bc968L);

  public static final Value _800bc974 = MEMORY.ref(4, 0x800bc974L);
  public static final Value _800bc978 = MEMORY.ref(4, 0x800bc978L);
  public static final Value _800bc97c = MEMORY.ref(4, 0x800bc97cL);
  //TODO structure @ 800bc980... 3 * 12?
  public static final Value _800bc980 = MEMORY.ref(4, 0x800bc980L);

  public static final ArrayRef<SpuStruct08> _800bc9a8 = MEMORY.ref(4, 0x800bc9a8L, ArrayRef.of(SpuStruct08.class, 24, 0x8, SpuStruct08::new));

  public static final Value _800bca68 = MEMORY.ref(1, 0x800bca68L);

  public static final Value _800bca6c = MEMORY.ref(4, 0x800bca6cL);

  public static final ArrayRef<SpuStruct28> spu28Arr_800bca78 = MEMORY.ref(1, 0x800bca78L, ArrayRef.of(SpuStruct28.class, 32, 0x28, SpuStruct28::new));

  /**
   * Bits:
   * 0 - MRG @ 62802 - audio
   */
  public static final Value loadedDrgnFiles_800bcf78 = MEMORY.ref(4, 0x800bcf78L);

  public static final ArrayRef<SoundFile> soundFileArr_800bcf80 = MEMORY.ref(2, 0x800bcf80L, ArrayRef.of(SoundFile.class, 13, 0x1c, SoundFile::new));

  public static final Value _800bd0f0 = MEMORY.ref(2, 0x800bd0f0L);

  public static final Value sssqChannelIndex_800bd0f8 = MEMORY.ref(2, 0x800bd0f8L);

  public static final Value _800bd0fc = MEMORY.ref(4, 0x800bd0fcL);
  public static final Value sssqTempoScale_800bd100 = MEMORY.ref(4, 0x800bd100L);
  public static final Value sssqTempo_800bd104 = MEMORY.ref(4, 0x800bd104L);
  public static final Value _800bd108 = MEMORY.ref(2, 0x800bd108L);

  public static final ArrayRef<SpuStruct28> spu28Arr_800bd110 = MEMORY.ref(1, 0x800bd110L, ArrayRef.of(SpuStruct28.class, 32, 0x28, SpuStruct28::new));
  public static final Value _800bd610 = MEMORY.ref(2, 0x800bd610L);

  public static final Value _800bd614 = MEMORY.ref(4, 0x800bd614L);

  public static final Value _800bd61c = MEMORY.ref(2, 0x800bd61cL);

  public static final Value _800bd680 = MEMORY.ref(4, 0x800bd680L);

  public static final ArrayRef<UnsignedIntRef> _800bd6e8 = MEMORY.ref(4, 0x800bd6f8L, ArrayRef.of(UnsignedIntRef.class, 3, 4, UnsignedIntRef::new));

  public static final Value _800bd6f8 = MEMORY.ref(4, 0x800bd6f8L);

  public static final Value _800bd700 = MEMORY.ref(1, 0x800bd700L);
  public static final Value _800bd704 = MEMORY.ref(4, 0x800bd704L);
  public static final Value _800bd708 = MEMORY.ref(4, 0x800bd708L);
  public static final Value _800bd70c = MEMORY.ref(4, 0x800bd70cL);
  public static final Value _800bd710 = MEMORY.ref(4, 0x800bd710L);
  public static final Value _800bd714 = MEMORY.ref(4, 0x800bd714L);

  public static final Value _800bd740 = MEMORY.ref(4, 0x800bd740L);

  public static final Pointer<MrgFile> soundMrgPtr_800bd748 = MEMORY.ref(4, 0x800bd748L, Pointer.deferred(4, MrgFile::new));

  public static final Value _800bd758 = MEMORY.ref(4, 0x800bd758L);

  public static final Value _800bd768 = MEMORY.ref(4, 0x800bd768L);
  public static final Pointer<MrgFile> soundMrgPtr_800bd76c = MEMORY.ref(4, 0x800bd76cL, Pointer.deferred(4, MrgFile::new));

  public static final Value _800bd774 = MEMORY.ref(4, 0x800bd774L);
  public static final Value soundbank_800bd778 = MEMORY.ref(4, 0x800bd778L);

  public static final Value _800bd780 = MEMORY.ref(1, 0x800bd780L);
  public static final Value _800bd781 = MEMORY.ref(1, 0x800bd781L);
  public static final Value _800bd782 = MEMORY.ref(1, 0x800bd782L);

  public static final Pointer<SshdFile> soundMrgSshdPtr_800bd784 = MEMORY.ref(4, 0x800bd784L, Pointer.deferred(4, SshdFile::new));
  public static final Pointer<SssqFile> soundMrgSssqPtr_800bd788 = MEMORY.ref(4, 0x800bd788L, Pointer.deferred(4, SssqFile::new));

  public static final Value _800bd7a0 = MEMORY.ref(4, 0x800bd7a0L);
  public static final Value _800bd7a4 = MEMORY.ref(4, 0x800bd7a4L);
  public static final Value _800bd7a8 = MEMORY.ref(4, 0x800bd7a8L);
  public static final Value _800bd7ac = MEMORY.ref(4, 0x800bd7acL);
  public static final Value _800bd7b0 = MEMORY.ref(4, 0x800bd7b0L);
  public static final Value _800bd7b4 = MEMORY.ref(2, 0x800bd7b4L);

  public static final Value _800bd7b8 = MEMORY.ref(4, 0x800bd7b8L);

  public static final UnboundedArrayRef<GsCOORD2PARAM> _800bd7c0 = MEMORY.ref(4, 0x800bd7c0L, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new));

  public static final GsRVIEW2 rview2_800bd7e8 = MEMORY.ref(4, 0x800bd7e8L, GsRVIEW2::new);
  public static final Value _800bd808 = MEMORY.ref(4, 0x800bd808L);
  public static final Value _800bd80c = MEMORY.ref(4, 0x800bd80cL);
  public static final Value projectionPlaneDistance_800bd810 = MEMORY.ref(4, 0x800bd810L);

  public static final Value _800bd818 = MEMORY.ref(4, 0x800bd818L);

  public static final UnboundedArrayRef<GsDOBJ2> _800bd9f8 = MEMORY.ref(4, 0x800bd9f8L, UnboundedArrayRef.of(0x10, GsDOBJ2::new));

  public static final Value _800bda08 = MEMORY.ref(4, 0x800bda08L);
  public static final Pointer<BattleRenderStruct> _800bda0c = MEMORY.ref(4, 0x800bda0cL, Pointer.deferred(4, BattleRenderStruct::new));
  public static final BigStruct bigStruct_800bda10 = MEMORY.ref(4, 0x800bda10L, BigStruct::new);

  public static final UnboundedArrayRef<GsCOORDINATE2> _800bdb38 = MEMORY.ref(4, 0x800bdb38L, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new));

  public static final Value _800bdb88 = MEMORY.ref(4, 0x800bdb88L);

  public static final Value _800bdb90 = MEMORY.ref(4, 0x800bdb90L);
  public static final Pointer<Renderable58> saveListUpArrow_800bdb94 = MEMORY.ref(4, 0x800bdb94L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> saveListDownArrow_800bdb98 = MEMORY.ref(4, 0x800bdb98L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> _800bdb9c = MEMORY.ref(4, 0x800bdb9cL, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> _800bdba0 = MEMORY.ref(4, 0x800bdba0L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdba4 = MEMORY.ref(4, 0x800bdba4L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdba8 = MEMORY.ref(4, 0x800bdba8L, Pointer.deferred(4, Renderable58::new));

  public static final ArrayRef<IntRef> characterIndices_800bdbb8 = MEMORY.ref(4, 0x800bdbb8L, ArrayRef.of(IntRef.class, 9, 0x4, IntRef::new));

  public static final Pointer<Renderable58> selectedMenuOptionRenderablePtr_800bdbe0 = MEMORY.ref(4, 0x800bdbe0L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> selectedMenuOptionRenderablePtr_800bdbe4 = MEMORY.ref(4, 0x800bdbe4L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdbe8 = MEMORY.ref(4, 0x800bdbe8L, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdbec = MEMORY.ref(4, 0x800bdbecL, Pointer.deferred(4, Renderable58::new));
  public static final Pointer<Renderable58> renderablePtr_800bdbf0 = MEMORY.ref(4, 0x800bdbf0L, Pointer.deferred(4, Renderable58::new));

  public static final ArrayRef<IntRef> secondaryCharIndices_800bdbf8 = MEMORY.ref(4, 0x800bdbf8L, ArrayRef.of(IntRef.class, 9, 4, IntRef::new));

  public static final Pointer<Renderable58> renderablePtr_800bdc20 = MEMORY.ref(4, 0x800bdc20L, Pointer.deferred(4, Renderable58::new));
  public static final Value _800bdc24 = MEMORY.ref(4, 0x800bdc24L);
  public static final EnumRef<InventoryMenuState> inventoryMenuState_800bdc28 = MEMORY.ref(4, 0x800bdc28L, EnumRef.of(InventoryMenuState.values()));
  public static final Value _800bdc2c = MEMORY.ref(4, 0x800bdc2cL);
  public static final EnumRef<InventoryMenuState> confirmDest_800bdc30 = MEMORY.ref(4, 0x800bdc30L, EnumRef.of(InventoryMenuState.values()));

  public static final Value _800bdc34 = MEMORY.ref(4, 0x800bdc34L);
  /**
   * 0xe - load game
   * 0x13 - also load game (maybe save game...?)
   * 0x18 - char swap
   *
   * Seems any other value shows the inventory
   */
  public static final Value whichMenu_800bdc38 = MEMORY.ref(4, 0x800bdc38L);
  public static final Pointer<Drgn0_6666File> drgn0_6666FilePtr_800bdc3c = MEMORY.ref(4, 0x800bdc3cL, Pointer.deferred(4, Drgn0_6666File::new));
  /** NOTE: same address as previous var */
  public static final Pointer<McqHeader> gameOverMcq_800bdc3c = MEMORY.ref(4, 0x800bdc3cL, Pointer.deferred(4, McqHeader::new));
  public static final Value _800bdc40 = MEMORY.ref(4, 0x800bdc40L);
  /**
   * 0x01 - L2
   * 0x02 - R2
   * 0x04 - L1
   * 0x08 - R1
   * 0x10 - Triangle
   * 0x20 - Cross
   * 0x40 - Circle
   * 0x80 - Square
   * 0x1000 - Up
   * 0x2000 - Right
   * 0x4000 - Down
   * 0x8000 - Left
   */
  public static final Value inventoryJoypadInput_800bdc44 = MEMORY.ref(4, 0x800bdc44L);

  public static final Value _800bdc58 = MEMORY.ref(4, 0x800bdc58L);
  public static final Pointer<Renderable58> renderablePtr_800bdc5c = MEMORY.ref(4, 0x800bdc5cL, Pointer.deferred(4, Renderable58::new));

  public static final LodString currentText_800bdca0 = MEMORY.ref(2, 0x800bdca0L, LodString::new);

  public static final Value _800bdea0 = MEMORY.ref(4, 0x800bdea0L);

  public static final Value _800bdf00 = MEMORY.ref(4, 0x800bdf00L);
  public static final Value _800bdf04 = MEMORY.ref(4, 0x800bdf04L);
  public static final Value _800bdf08 = MEMORY.ref(4, 0x800bdf08L);

  public static final Value _800bdf10 = MEMORY.ref(4, 0x800bdf10L);

  public static final Value _800bdf18 = MEMORY.ref(4, 0x800bdf18L);

  public static final ArrayRef<Struct84> _800bdf38 = MEMORY.ref(4, 0x800bdf38L, ArrayRef.of(Struct84.class, 8, 0x84, Struct84::new));
  public static final ArrayRef<Struct4c> _800be358 = MEMORY.ref(4, 0x800be358L, ArrayRef.of(Struct4c.class, 8, 0x4c, Struct4c::new));
  public static final Value _800be5b8 = MEMORY.ref(4, 0x800be5b8L);
  public static final Value _800be5bc = MEMORY.ref(4, 0x800be5bcL);
  public static final Value _800be5c0 = MEMORY.ref(4, 0x800be5c0L);
  public static final Value _800be5c4 = MEMORY.ref(2, 0x800be5c4L);
  public static final Value _800be5c8 = MEMORY.ref(4, 0x800be5c8L);

  public static final Value _800be5d0 = MEMORY.ref(4, 0x800be5d0L);

  public static final EquipmentStats1c equipmentStats_800be5d8 = MEMORY.ref(1, 0x800be5d8L, EquipmentStats1c::new);

  public static final ArrayRef<ActiveStatsa0> stats_800be5f8 = MEMORY.ref(4, 0x800be5f8L, ArrayRef.of(ActiveStatsa0.class, 9, 0xa0, ActiveStatsa0::new));

  public static final Value _800beb98 = MEMORY.ref(4, 0x800beb98L);
  public static final Value _800bed28 = MEMORY.ref(4, 0x800bed28L);

  public static final MATRIX matrix_800bed30 = MEMORY.ref(4, 0x800bed30L, MATRIX::new);
  public static final IntRef screenOffsetX_800bed50 = MEMORY.ref(4, 0x800bed50L, IntRef::new);
  public static final IntRef screenOffsetY_800bed54 = MEMORY.ref(4, 0x800bed54L, IntRef::new);
  public static final Value hasNoEncounters_800bed58 = MEMORY.ref(4, 0x800bed58L);

  public static final Value _800bee90 = MEMORY.ref(4, 0x800bee90L);
  public static final Value _800bee94 = MEMORY.ref(4, 0x800bee94L);
  public static final Value _800bee98 = MEMORY.ref(4, 0x800bee98L);

  public static final Value _800bf0b0 = MEMORY.ref(4, 0x800bf0b0L);
  public static final Value _800bf0b4 = MEMORY.ref(4, 0x800bf0b4L);

  public static final Value _800bf0c0 = MEMORY.ref(4, 0x800bf0c0L);
  public static final Value _800bf0c4 = MEMORY.ref(4, 0x800bf0c4L);
  public static final Value _800bf0c8 = MEMORY.ref(4, 0x800bf0c8L);
  public static final Value _800bf0cc = MEMORY.ref(1, 0x800bf0ccL);
  public static final Value _800bf0cd = MEMORY.ref(1, 0x800bf0cdL);
  public static final Value _800bf0ce = MEMORY.ref(1, 0x800bf0ceL);
  public static final Value _800bf0cf = MEMORY.ref(1, 0x800bf0cfL);
  public static final Value _800bf0d0 = MEMORY.ref(1, 0x800bf0d0L);

  public static final Value _800bf0d8 = MEMORY.ref(4, 0x800bf0d8L);

  public static final Value _800bf0dc = MEMORY.ref(4, 0x800bf0dcL);
  public static final Value _800bf0e0 = MEMORY.ref(4, 0x800bf0e0L);

  public static final Value _800bf0ec = MEMORY.ref(4, 0x800bf0ecL);

  /**
   * CD file list
   *
   * 0x800bf7a8-0x800bfda8
   */
  public static final ArrayRef<CdlFILE> CdlFILE_800bf7a8 = MEMORY.ref(1, 0x800bf7a8L, ArrayRef.of(CdlFILE.class, DSL_MAX_FILE, 0x18, CdlFILE::new));
  public static final ArrayRef<CdlDIR> CdlDIR_800bfda8 = MEMORY.ref(4, 0x800bfda8L, ArrayRef.of(CdlDIR.class, DSL_MAX_DIR, 0x2c, CdlDIR::new));
}
