package legend.game;

import legend.core.gpu.Bpp;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.EnumMapRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.types.BattleStage;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.ScriptState;
import legend.game.types.ActiveStatsa0;
import legend.game.types.Drgn0_6666File;
import legend.game.types.EquipmentStats1c;
import legend.game.types.GameState52c;
import legend.game.types.GsRVIEW2;
import legend.game.types.InventoryMenuState;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.Renderable58;
import legend.game.types.ScriptEffectStruct;
import legend.game.types.SobjPos14;
import legend.game.types.SoundFile;
import legend.game.types.SpuStruct08;
import legend.game.types.SpuStruct10;
import legend.game.types.SpuStruct28;
import legend.game.types.SshdFile;
import legend.game.types.SssqFile;
import legend.game.types.Struct84;
import legend.game.types.TexPageY;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.Translucency;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static final Value _800ba3b8 = MEMORY.ref(4, 0x800ba3b8L);

  public static final IntRef _800babc0 = MEMORY.ref(4, 0x800babc0L, IntRef::new);

  public static final GameState52c gameState_800babc8 = MEMORY.ref(4, 0x800babc8L, GameState52c::new);

  // End of game state 800bb0f4

  public static final IntRef combatStage_800bb0f4 = MEMORY.ref(4, 0x800bb0f4L, IntRef::new);
  public static final IntRef encounterId_800bb0f8 = MEMORY.ref(4, 0x800bb0f8L, IntRef::new);
  public static final IntRef tickCount_800bb0fc = MEMORY.ref(4, 0x800bb0fcL, IntRef::new);

  public static final IntRef _800bb104 = MEMORY.ref(4, 0x800bb104L, IntRef::new);
  public static final IntRef doubleBufferFrame_800bb108 = MEMORY.ref(4, 0x800bb108L, IntRef::new);
  public static final IntRef pregameLoadingStage_800bb10c = MEMORY.ref(4, 0x800bb10cL, IntRef::new);
  public static final EnumMapRef<Bpp, EnumMapRef<Translucency, EnumMapRef<TexPageY, UnsignedShortRef>>> texPages_800bb110 = MEMORY.ref(2, 0x800bb110L, EnumMapRef.of(Bpp.class, EnumMapRef.classFor(EnumMapRef.classFor(UnsignedShortRef.class)), Bpp.values().length, 0x10, EnumMapRef.of(Translucency.class, EnumMapRef.classFor(UnsignedShortRef.class), Translucency.values().length, 4, EnumMapRef.of(TexPageY.class, UnsignedShortRef.class, 2, 2, UnsignedShortRef::new))));
  public static final ScriptEffectStruct scriptEffect_800bb140 = MEMORY.ref(4, 0x800bb140L, ScriptEffectStruct::new);
  public static final IntRef _800bb168 = MEMORY.ref(4, 0x800bb168L, IntRef::new); //TODO is this part of the previous struct?

  public static final ArrayRef<UnsignedIntRef> array_800bb198 = MEMORY.ref(4, 0x800bb198L, ArrayRef.of(UnsignedIntRef.class, 36, 4, UnsignedIntRef::new));

  public static final Value _800bb228 = MEMORY.ref(4, 0x800bb228L);

  public static final Value _800bb348 = MEMORY.ref(4, 0x800bb348L);

  public static final IntRef drgnBinIndex_800bc058 = MEMORY.ref(4, 0x800bc058L, IntRef::new);
  public static final IntRef _800bc05c = MEMORY.ref(4, 0x800bc05cL, IntRef::new);

  public static final ScriptState<?>[] scriptStatePtrArr_800bc1c0 = new ScriptState[72];

  /** TODO vec3 or maybe 3 values indexed by char slot? */
  public static final Value _800bc910 = MEMORY.ref(4, 0x800bc910L);
  public static final Value _800bc914 = MEMORY.ref(4, 0x800bc914L);
  public static final Value _800bc918 = MEMORY.ref(4, 0x800bc918L);
  public static final IntRef _800bc91c = MEMORY.ref(4, 0x800bc91cL, IntRef::new);
  public static final IntRef goldGainedFromCombat_800bc920 = MEMORY.ref(4, 0x800bc920L, IntRef::new);

  public static final ArrayRef<IntRef> itemsDroppedByEnemies_800bc928 = MEMORY.ref(4, 0x800bc928L, ArrayRef.of(IntRef.class, 9, 4, IntRef::new));
  public static final Value _800bc94c = MEMORY.ref(4, 0x800bc94cL);
  public static final ArrayRef<IntRef> spGained_800bc950 = MEMORY.ref(4, 0x800bc950L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  public static final IntRef totalXpFromCombat_800bc95c = MEMORY.ref(4, 0x800bc95cL, IntRef::new);
  public static final IntRef _800bc960 = MEMORY.ref(4, 0x800bc960L, IntRef::new);

  public static final Value _800bc968 = MEMORY.ref(4, 0x800bc968L);

  public static final IntRef _800bc974 = MEMORY.ref(4, 0x800bc974L, IntRef::new);
  public static final IntRef itemsDroppedByEnemiesCount_800bc978 = MEMORY.ref(4, 0x800bc978L, IntRef::new);
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

  public static final ShortRef sssqChannelIndex_800bd0f8 = MEMORY.ref(2, 0x800bd0f8L, ShortRef::new);

  public static final Value _800bd0fc = MEMORY.ref(4, 0x800bd0fcL);
  public static final IntRef sssqTempoScale_800bd100 = MEMORY.ref(4, 0x800bd100L, IntRef::new);
  public static final IntRef sssqTempo_800bd104 = MEMORY.ref(4, 0x800bd104L, IntRef::new);
  public static final Value _800bd108 = MEMORY.ref(2, 0x800bd108L);

  public static final ArrayRef<SpuStruct28> spu28Arr_800bd110 = MEMORY.ref(1, 0x800bd110L, ArrayRef.of(SpuStruct28.class, 32, 0x28, SpuStruct28::new));
  public static final SpuStruct10[] spu10Arr_800bd610 = {new SpuStruct10(), new SpuStruct10(), new SpuStruct10(), new SpuStruct10(), new SpuStruct10(), new SpuStruct10(), new SpuStruct10()};
  public static final Value _800bd680 = MEMORY.ref(4, 0x800bd680L);

  public static final Value _800bd6f8 = MEMORY.ref(4, 0x800bd6f8L);

  public static final Value _800bd700 = MEMORY.ref(1, 0x800bd700L);
  public static final Value _800bd704 = MEMORY.ref(4, 0x800bd704L);
  public static final Value _800bd708 = MEMORY.ref(4, 0x800bd708L);
  public static final Value _800bd70c = MEMORY.ref(4, 0x800bd70cL);
  public static final Value _800bd710 = MEMORY.ref(4, 0x800bd710L);
  public static final Value _800bd714 = MEMORY.ref(4, 0x800bd714L);

  public static final Value _800bd740 = MEMORY.ref(4, 0x800bd740L);

  public static final Value _800bd774 = MEMORY.ref(4, 0x800bd774L);

  public static final BoolRef melbuSoundsLoaded_800bd780 = MEMORY.ref(1, 0x800bd780L, BoolRef::new);
  public static final BoolRef melbuMusicLoaded_800bd781 = MEMORY.ref(1, 0x800bd781L, BoolRef::new);
  public static final UnsignedByteRef musicLoaded_800bd782 = MEMORY.ref(1, 0x800bd782L, UnsignedByteRef::new);

  public static final Pointer<SshdFile> melbuSoundMrgSshdPtr_800bd784 = MEMORY.ref(4, 0x800bd784L, Pointer.deferred(4, SshdFile::new));
  public static final Pointer<SssqFile> melbuSoundMrgSssqPtr_800bd788 = MEMORY.ref(4, 0x800bd788L, Pointer.deferred(4, SssqFile::new));

  public static final Value _800bd7ac = MEMORY.ref(4, 0x800bd7acL);
  public static final IntRef _800bd7b0 = MEMORY.ref(4, 0x800bd7b0L, IntRef::new);
  public static final Value _800bd7b4 = MEMORY.ref(2, 0x800bd7b4L);

  public static final Value _800bd7b8 = MEMORY.ref(4, 0x800bd7b8L);

  public static final GsRVIEW2 rview2_800bd7e8 = new GsRVIEW2();
  public static final IntRef submapIndex_800bd808 = MEMORY.ref(4, 0x800bd808L, IntRef::new);
  public static final Value _800bd80c = MEMORY.ref(4, 0x800bd80cL);
  public static final Value projectionPlaneDistance_800bd810 = MEMORY.ref(4, 0x800bd810L);

  public static final ArrayRef<SobjPos14> sobjPositions_800bd818 = MEMORY.ref(4, 0x800bd818L, ArrayRef.of(SobjPos14.class, 24, 0x14, SobjPos14::new));

  public static final IntRef _800bda08 = MEMORY.ref(4, 0x800bda08L, IntRef::new);
  public static BattleStage stage_800bda0c;
  public static final Model124 model_800bda10 = new Model124();

  public static final IntRef _800bdb88 = MEMORY.ref(4, 0x800bdb88L, IntRef::new);

  public static final Value _800bdb90 = MEMORY.ref(4, 0x800bdb90L);
  public static Renderable58 saveListUpArrow_800bdb94;
  public static Renderable58 saveListDownArrow_800bdb98;

  public static Renderable58 renderablePtr_800bdba4;
  public static Renderable58 renderablePtr_800bdba8;

  public static final ArrayRef<IntRef> characterIndices_800bdbb8 = MEMORY.ref(4, 0x800bdbb8L, ArrayRef.of(IntRef.class, 9, 0x4, IntRef::new));

  public static final ArrayRef<IntRef> secondaryCharIndices_800bdbf8 = MEMORY.ref(4, 0x800bdbf8L, ArrayRef.of(IntRef.class, 9, 4, IntRef::new));

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
  public static WhichMenu whichMenu_800bdc38 = WhichMenu.NONE_0;
  public static final Pointer<Drgn0_6666File> drgn0_6666FilePtr_800bdc3c = MEMORY.ref(4, 0x800bdc3cL, Pointer.deferred(4, Drgn0_6666File::new));
  /** NOTE: same address as previous var */
  public static final Pointer<McqHeader> gameOverMcq_800bdc3c = MEMORY.ref(4, 0x800bdc3cL, Pointer.deferred(4, McqHeader::new));

  /**
   * <ul>
   *   <li>0x01 - L2</li>
   *   <li>0x02 - R2</li>
   *   <li>0x04 - L1</li>
   *   <li>0x08 - R1</li>
   *   <li>0x10 - Triangle</li>
   *   <li>0x20 - Cross</li>
   *   <li>0x40 - Circle</li>
   *   <li>0x80 - Square</li>
   *   <li>0x1000 - Up</li>
   *   <li>0x2000 - Right</li>
   *   <li>0x4000 - Down</li>
   *   <li>0x8000 - Left</li>
   * </ul>
   */
  public static final Value inventoryJoypadInput_800bdc44 = MEMORY.ref(4, 0x800bdc44L);

  public static final Value _800bdc58 = MEMORY.ref(4, 0x800bdc58L);
  public static Renderable58 renderablePtr_800bdc5c;

  public static final LodString currentText_800bdca0 = MEMORY.ref(2, 0x800bdca0L, LodString::new);

  public static final Value _800bdd24 = MEMORY.ref(4, 0x800bdd24L);

  public static final TextboxArrow0c[] textboxArrows_800bdea0 = new TextboxArrow0c[8];

  public static final IntRef textZ_800bdf00 = MEMORY.ref(4, 0x800bdf00L, IntRef::new);
  public static final Value _800bdf04 = MEMORY.ref(4, 0x800bdf04L);
  public static final Value _800bdf08 = MEMORY.ref(4, 0x800bdf08L);

  public static final Value _800bdf10 = MEMORY.ref(4, 0x800bdf10L);

  public static final Value _800bdf18 = MEMORY.ref(4, 0x800bdf18L);

  public static final Struct84[] _800bdf38 = new Struct84[8];
  public static final Textbox4c[] textboxes_800be358 = new Textbox4c[8];
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

  public static final IntRef _800bee90 = MEMORY.ref(4, 0x800bee90L, IntRef::new);
  public static final IntRef _800bee94 = MEMORY.ref(4, 0x800bee94L, IntRef::new);
  public static final IntRef _800bee98 = MEMORY.ref(4, 0x800bee98L, IntRef::new);
  public static final IntRef _800bee9c = MEMORY.ref(4, 0x800bee9cL, IntRef::new);

  public static final IntRef _800beea4 = MEMORY.ref(4, 0x800beea4L, IntRef::new);

  public static final IntRef _800beeac = MEMORY.ref(4, 0x800beeacL, IntRef::new);

  public static final IntRef _800beeb4 = MEMORY.ref(4, 0x800beeb4L, IntRef::new);

  public static final IntRef _800beebc = MEMORY.ref(4, 0x800beebcL, IntRef::new);

  public static final IntRef continentIndex_800bf0b0 = MEMORY.ref(4, 0x800bf0b0L, IntRef::new);

  public static final Value _800bf0cf = MEMORY.ref(1, 0x800bf0cfL);
  public static final Value _800bf0d0 = MEMORY.ref(1, 0x800bf0d0L);

  public static final Value fmvStage_800bf0d8 = MEMORY.ref(4, 0x800bf0d8L);

  public static final Value fmvIndex_800bf0dc = MEMORY.ref(4, 0x800bf0dcL);

  public static final IntRef afterFmvLoadingStage_800bf0ec = MEMORY.ref(4, 0x800bf0ecL, IntRef::new);
}
