package legend.game;

import legend.core.gpu.Bpp;
import legend.core.gte.MATRIX;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.EnumMapRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.types.EnemyDrop;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.ScriptState;
import legend.game.sound.EncounterSoundEffects10;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SequenceData124;
import legend.game.sound.SoundFile;
import legend.game.sound.SpuStruct08;
import legend.game.submap.SobjPos14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.EngineState;
import legend.game.types.FullScreenEffect;
import legend.game.types.GameState52c;
import legend.game.types.GsRVIEW2;
import legend.game.types.InventoryMenuState;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.Renderable58;
import legend.game.types.TexPageY;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TextboxText84;
import legend.game.types.Translucency;
import legend.game.types.UiFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.GameEngine.MEMORY;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static final IntRef clearBlue_800babc0 = MEMORY.ref(4, 0x800babc0L, IntRef::new);

  public static GameState52c gameState_800babc8;

  // End of game state 800bb0f4

  public static final IntRef combatStage_800bb0f4 = MEMORY.ref(4, 0x800bb0f4L, IntRef::new);
  public static final IntRef encounterId_800bb0f8 = MEMORY.ref(4, 0x800bb0f8L, IntRef::new);
  public static final IntRef tickCount_800bb0fc = MEMORY.ref(4, 0x800bb0fcL, IntRef::new);

  public static final IntRef clearGreen_800bb104 = MEMORY.ref(4, 0x800bb104L, IntRef::new);

  public static final IntRef pregameLoadingStage_800bb10c = MEMORY.ref(4, 0x800bb10cL, IntRef::new);
  public static final EnumMapRef<Bpp, EnumMapRef<Translucency, EnumMapRef<TexPageY, UnsignedShortRef>>> texPages_800bb110 = MEMORY.ref(2, 0x800bb110L, EnumMapRef.of(Bpp.class, EnumMapRef.classFor(EnumMapRef.classFor(UnsignedShortRef.class)), Bpp.values().length, 0x10, EnumMapRef.of(Translucency.class, EnumMapRef.classFor(UnsignedShortRef.class), Translucency.values().length, 4, EnumMapRef.of(TexPageY.class, UnsignedShortRef.class, 2, 2, UnsignedShortRef::new))));
  public static final FullScreenEffect fullScreenEffect_800bb140 = new FullScreenEffect();

  public static int drgnBinIndex_800bc058;

  public static final ScriptState<?>[] scriptStatePtrArr_800bc1c0 = new ScriptState[72];

  public static final boolean[] unlockedUltimateAddition_800bc910 = new boolean[3];
  public static EngineState postCombatMainCallbackIndex_800bc91c = EngineState.PRELOAD_00;
  public static final IntRef goldGainedFromCombat_800bc920 = MEMORY.ref(4, 0x800bc920L, IntRef::new);

  public static final List<EnemyDrop> itemsDroppedByEnemies_800bc928 = new ArrayList<>();
  public static final List<Item> itemOverflow = new ArrayList<>();
  public static final List<Equipment> equipmentOverflow = new ArrayList<>();
  public static final BoolRef battleLoaded_800bc94c = MEMORY.ref(4, 0x800bc94cL, BoolRef::new);
  public static final ArrayRef<IntRef> spGained_800bc950 = MEMORY.ref(4, 0x800bc950L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  public static final IntRef totalXpFromCombat_800bc95c = MEMORY.ref(4, 0x800bc95cL, IntRef::new);
  public static final IntRef _800bc960 = MEMORY.ref(4, 0x800bc960L, IntRef::new);

  public static final ArrayRef<IntRef> livingCharIds_800bc968 = MEMORY.ref(4, 0x800bc968L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  /**
   * <ol>
   *   <li value="1">Combat victory</li>
   *   <li value="2">Game over</li>
   *   <li value="4">FMV</li>
   * </ol>
   */
  public static final IntRef postBattleActionIndex_800bc974 = MEMORY.ref(4, 0x800bc974L, IntRef::new);

  public static final IntRef livingCharCount_800bc97c = MEMORY.ref(4, 0x800bc97cL, IntRef::new);

  /** One per voice */
  public static final SpuStruct08[] _800bc9a8 = new SpuStruct08[24];
  static {
    Arrays.setAll(_800bc9a8, i -> new SpuStruct08());
  }

  public static final Value _800bca68 = MEMORY.ref(1, 0x800bca68L);

  public static final Value _800bca6c = MEMORY.ref(4, 0x800bca6cL);

  public static final Queue<QueuedSound28> playingSoundsBackup_800bca78 = new LinkedList<>();

  /**
   * Bits:
   * 0 - MRG @ 62802 - audio
   */
  public static final AtomicInteger loadedDrgnFiles_800bcf78 = new AtomicInteger();

  public static final SoundFile[] soundFiles_800bcf80 = new SoundFile[13];
  static {
    Arrays.setAll(soundFiles_800bcf80, i -> new SoundFile());
  }

  public static final Value _800bd0f0 = MEMORY.ref(2, 0x800bd0f0L);

  public static SequenceData124 currentSequenceData_800bd0f8;

  public static final Value _800bd0fc = MEMORY.ref(4, 0x800bd0fcL);
  public static int sssqTempoScale_800bd100;
  public static int sssqTempo_800bd104;
  public static int sequenceVolume_800bd108;

  public static final Queue<QueuedSound28> queuedSounds_800bd110 = new LinkedList<>();
  /** NOTE: this used to be an array, but only the 6th element was used */
  public static final EncounterSoundEffects10 encounterSoundEffects_800bd610 = new EncounterSoundEffects10();

  public static final Value _800bd6f8 = MEMORY.ref(4, 0x800bd6f8L);

  public static final Value _800bd700 = MEMORY.ref(1, 0x800bd700L);
  public static final Value _800bd704 = MEMORY.ref(4, 0x800bd704L);
  public static final Value _800bd708 = MEMORY.ref(4, 0x800bd708L);
  public static final Value _800bd70c = MEMORY.ref(4, 0x800bd70cL);
  public static final Value _800bd710 = MEMORY.ref(4, 0x800bd710L);
  public static final Value _800bd714 = MEMORY.ref(4, 0x800bd714L);

  public static final Value _800bd740 = MEMORY.ref(4, 0x800bd740L);

  public static final Value _800bd774 = MEMORY.ref(4, 0x800bd774L);

  public static boolean melbuSoundsLoaded_800bd780;
  public static boolean melbuMusicLoaded_800bd781;
  public static boolean musicLoaded_800bd782;

  public static final Value _800bd7ac = MEMORY.ref(4, 0x800bd7acL);
  public static final IntRef _800bd7b0 = MEMORY.ref(4, 0x800bd7b0L, IntRef::new);
  public static final Value _800bd7b4 = MEMORY.ref(2, 0x800bd7b4L);

  public static final Value _800bd7b8 = MEMORY.ref(4, 0x800bd7b8L);

  public static final GsRVIEW2 rview2_800bd7e8 = new GsRVIEW2();
  public static final IntRef submapId_800bd808 = MEMORY.ref(4, 0x800bd808L, IntRef::new);

  public static final Value projectionPlaneDistance_800bd810 = MEMORY.ref(4, 0x800bd810L);

  public static final SobjPos14[] sobjPositions_800bd818 = new SobjPos14[24];
  static {
    Arrays.setAll(sobjPositions_800bd818, i -> new SobjPos14());
  }

  public static final IntRef _800bda08 = MEMORY.ref(4, 0x800bda08L, IntRef::new);
  public static BattleStage stage_800bda0c;
  public static final Model124 shadowModel_800bda10 = new Model124("Shadow");

  public static EngineState _800bdb88 = EngineState.PRELOAD_00;

  public static Renderable58 saveListUpArrow_800bdb94;
  public static Renderable58 saveListDownArrow_800bdb98;

  public static Renderable58 renderablePtr_800bdba4;
  public static Renderable58 renderablePtr_800bdba8;

  public static final ArrayRef<IntRef> characterIndices_800bdbb8 = MEMORY.ref(4, 0x800bdbb8L, ArrayRef.of(IntRef.class, 9, 0x4, IntRef::new));

  public static final ArrayRef<IntRef> secondaryCharIndices_800bdbf8 = MEMORY.ref(4, 0x800bdbf8L, ArrayRef.of(IntRef.class, 9, 4, IntRef::new));

  public static final EnumRef<InventoryMenuState> inventoryMenuState_800bdc28 = MEMORY.ref(4, 0x800bdc28L, EnumRef.of(InventoryMenuState.values()));
  public static final Value _800bdc2c = MEMORY.ref(4, 0x800bdc2cL);
  public static final EnumRef<InventoryMenuState> confirmDest_800bdc30 = MEMORY.ref(4, 0x800bdc30L, EnumRef.of(InventoryMenuState.values()));

  public static final BoolRef savedGameSelected_800bdc34 = MEMORY.ref(4, 0x800bdc34L, BoolRef::new);
  /**
   * 0xe - load game
   * 0x13 - also load game (maybe save game...?)
   * 0x18 - char swap
   *
   * Seems any other value shows the inventory
   */
  public static WhichMenu whichMenu_800bdc38 = WhichMenu.NONE_0;
  public static UiFile uiFile_800bdc3c;
  /** NOTE: same address as previous var */
  public static McqHeader gameOverMcq_800bdc3c;

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

  public static final TextboxArrow0c[] textboxArrows_800bdea0 = new TextboxArrow0c[8];

  public static final IntRef textZ_800bdf00 = MEMORY.ref(4, 0x800bdf00L, IntRef::new);
  public static final Value _800bdf04 = MEMORY.ref(4, 0x800bdf04L);
  public static final Value _800bdf08 = MEMORY.ref(4, 0x800bdf08L);

  public static final ArrayRef<IntRef> textboxVariables_800bdf10 = MEMORY.ref(4, 0x800bdf10L, ArrayRef.of(IntRef.class, 10, 0x4, IntRef::new));
  public static final TextboxText84[] textboxText_800bdf38 = new TextboxText84[8];
  public static final Textbox4c[] textboxes_800be358 = new Textbox4c[8];
  public static final Value textU_800be5c0 = MEMORY.ref(4, 0x800be5c0L);
  public static final Value textV_800be5c8 = MEMORY.ref(4, 0x800be5c8L);

  public static final Value _800be5d0 = MEMORY.ref(4, 0x800be5d0L);

  public static final ActiveStatsa0[] stats_800be5f8 = new ActiveStatsa0[9];
  static {
    Arrays.setAll(stats_800be5f8, i -> new ActiveStatsa0());
  }

  public static final Value _800bed28 = MEMORY.ref(4, 0x800bed28L);

  public static final MATRIX matrix_800bed30 = new MATRIX();
  public static final IntRef screenOffsetX_800bed50 = MEMORY.ref(4, 0x800bed50L, IntRef::new);
  public static final IntRef screenOffsetY_800bed54 = MEMORY.ref(4, 0x800bed54L, IntRef::new);
  public static final Value hasNoEncounters_800bed58 = MEMORY.ref(4, 0x800bed58L);

  /**
   * Remains set for the duration of the button press
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
  public static final IntRef input_800bee90 = MEMORY.ref(4, 0x800bee90L, IntRef::new);
  /**
   * Only set for an instant after buttons are pressed
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
  public static final IntRef press_800bee94 = MEMORY.ref(4, 0x800bee94L, IntRef::new);
  /**
   * Only set for an instant after buttons are pressed, but repeats while button is held
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
  public static final IntRef repeat_800bee98 = MEMORY.ref(4, 0x800bee98L, IntRef::new);
  public static final IntRef analogAngle_800bee9c = MEMORY.ref(4, 0x800bee9cL, IntRef::new);

  public static final IntRef _800beea4 = MEMORY.ref(4, 0x800beea4L, IntRef::new);

  public static final IntRef _800beeac = MEMORY.ref(4, 0x800beeacL, IntRef::new);

  public static final IntRef analogMagnitude_800beeb4 = MEMORY.ref(4, 0x800beeb4L, IntRef::new);

  public static final IntRef analogInput_800beebc = MEMORY.ref(4, 0x800beebcL, IntRef::new);

  public static final IntRef continentIndex_800bf0b0 = MEMORY.ref(4, 0x800bf0b0L, IntRef::new);

  public static final Value _800bf0cf = MEMORY.ref(1, 0x800bf0cfL);
  public static final Value _800bf0d0 = MEMORY.ref(1, 0x800bf0d0L);

  public static final Value fmvStage_800bf0d8 = MEMORY.ref(4, 0x800bf0d8L);

  public static int fmvIndex_800bf0dc;

  public static EngineState afterFmvLoadingStage_800bf0ec = EngineState.PRELOAD_00;
}
