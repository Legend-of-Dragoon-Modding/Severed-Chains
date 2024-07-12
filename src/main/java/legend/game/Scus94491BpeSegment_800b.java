package legend.game;

import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.gte.MV;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.types.EnemyDrop;
import legend.game.combat.ui.BattleDissolveDarkeningMetrics10;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.ScriptState;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SoundFile;
import legend.game.sound.SpuStruct08;
import legend.game.submap.SobjPos14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.FullScreenEffect;
import legend.game.types.GameState52c;
import legend.game.types.GsRVIEW2;
import legend.game.types.InventoryMenuState;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.Renderable58;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxArrow0c;
import legend.game.types.TextboxText84;
import legend.game.types.UiFile;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static int clearBlue_800babc0;

  public static GameState52c gameState_800babc8;
  public static int battleStage_800bb0f4;
  public static int encounterId_800bb0f8;
  public static int tickCount_800bb0fc;

  public static int clearGreen_800bb104;

  public static int pregameLoadingStage_800bb10c;

  public static final FullScreenEffect fullScreenEffect_800bb140 = new FullScreenEffect();

  public static int drgnBinIndex_800bc058;

  public static final ScriptState<?>[] scriptStatePtrArr_800bc1c0 = new ScriptState[72];

  public static final boolean[] unlockedUltimateAddition_800bc910 = new boolean[3];
  public static EngineStateEnum postCombatMainCallbackIndex_800bc91c = EngineStateEnum.PRELOAD_00;
  public static int goldGainedFromCombat_800bc920;

  public static final List<EnemyDrop> itemsDroppedByEnemies_800bc928 = new ArrayList<>();
  public static final List<Item> itemOverflow = new ArrayList<>();
  public static final List<Equipment> equipmentOverflow = new ArrayList<>();
  public static boolean battleLoaded_800bc94c;
  public static final int[] spGained_800bc950 = new int[3];
  public static int totalXpFromCombat_800bc95c;
  /**
   * <ul>
   *   <li>0x1 - battle start delay period has elapsed</li>
   *   <li>0x2 - combat controller script loaded</li>
   *   <li>0x4 - monster models loaded</li>
   *   <li>0x8 - character models loaded</li>
   *   <li>0x10 - initial turn values calculated</li>
   *   <li>0x40 - viewport/camera initialized</li>
   *   <li>0x80 - something related to skybox MCQ</li>
   *   <li>0x100 - possibly set by a script to signify monsters are fully loaded</li>
   *   <li>0x200 - possibly set by a script to signify characters are fully loaded</li>
   *   <li>0x400 - encounter assets have been requested from the filesystem (may still be loading)</li>
   * </ul>
   */
  public static int battleFlags_800bc960;

  public static int[] livingCharIds_800bc968 = new int[3];
  /**
   * <ol>
   *   <li value="1">Combat victory</li>
   *   <li value="2">Game over</li>
   *   <li value="4">FMV</li>
   * </ol>
   */
  public static int postBattleAction_800bc974;

  public static int livingCharCount_800bc97c;

  /** One per voice */
  public static final SpuStruct08[] _800bc9a8 = new SpuStruct08[24];
  static {
    Arrays.setAll(_800bc9a8, i -> new SpuStruct08());
  }

  public static final Queue<QueuedSound28> playingSoundsBackup_800bca78 = new LinkedList<>();

  /**
   * 0x1 - menu sounds
   * 0x2 - submap sounds
   * 0x4 - battle cutscene sounds
   * 0x8 - battle character sounds
   * 0x10 - battle phase sounds
   * 0x20 - battle monster sounds
   * 0x40 - battle DEFF sounds
   * 0x80 - music
   * 0x4000 - victory music
   * 0x8000 - world map destination sounds
   * 0x10000 - different battle character attack sounds?
   */
  public static final AtomicInteger loadedDrgnFiles_800bcf78 = new AtomicInteger();

  public static final SoundFile[] soundFiles_800bcf80 = new SoundFile[13];
  static {
    Arrays.setAll(soundFiles_800bcf80, i -> new SoundFile());
  }

  public static int _800bd0f0;

  /** .8 */
  public static int sssqTempoScale_800bd100;

  public static final Queue<QueuedSound28> queuedSounds_800bd110 = new LinkedList<>();
  public static BackgroundMusic victoryMusic;

  public static final BattleDissolveDarkeningMetrics10 dissolveDarkening_800bd700 = new BattleDissolveDarkeningMetrics10();
  public static int _800bd710;
  public static int _800bd714;
  public static int battleDissolveTicks;

  public static int _800bd740;

  public static boolean musicLoaded_800bd782;

  public static boolean _800bd7ac;
  public static int _800bd7b0;
  public static boolean submapFullyLoaded_800bd7b4;

  public static boolean transitioningFromCombatToSubmap_800bd7b8;

  public static final GsRVIEW2 rview2_800bd7e8 = new GsRVIEW2();
  public static int submapId_800bd808;

  public static int projectionPlaneDistance_800bd810;

  public static final SobjPos14[] sobjPositions_800bd818 = new SobjPos14[24];
  static {
    Arrays.setAll(sobjPositions_800bd818, i -> new SobjPos14());
  }

  public static int previousSubmapCut_800bda08;
  public static BattleStage stage_800bda0c;
  public static final Model124 shadowModel_800bda10 = new Model124("Shadow");

  public static EngineStateEnum previousEngineState_800bdb88 = EngineStateEnum.PRELOAD_00;

  public static Renderable58 saveListUpArrow_800bdb94;
  public static Renderable58 saveListDownArrow_800bdb98;

  public static Renderable58 renderablePtr_800bdba4;
  public static Renderable58 renderablePtr_800bdba8;

  public static final int[] characterIndices_800bdbb8 = new int[9];
  public static final int[] secondaryCharIds_800bdbf8 = new int[9];

  public static InventoryMenuState inventoryMenuState_800bdc28 = InventoryMenuState.INIT_0;

  public static boolean loadingNewGameState_800bdc34;
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
  public static int inventoryJoypadInput_800bdc44;

  public static Renderable58 renderablePtr_800bdc5c;

  public static final TextboxArrow0c[] textboxArrows_800bdea0 = new TextboxArrow0c[8];

  public static int textZ_800bdf00;

  public static final int[] textboxVariables_800bdf10 = new int[10];
  public static final TextboxText84[] textboxText_800bdf38 = new TextboxText84[8];
  public static final Textbox4c[] textboxes_800be358 = new Textbox4c[8];

  public static boolean characterStatsLoaded_800be5d0;

  public static final ActiveStatsa0[] stats_800be5f8 = new ActiveStatsa0[9];
  static {
    Arrays.setAll(stats_800be5f8, i -> new ActiveStatsa0());
  }

  // These are outside of SMAP because they have to persist between engine states
  public static final MV playerPositionBeforeBattle_800bed30 = new MV();
  public static final Vector2f screenOffsetBeforeBattle_800bed50 = new Vector2f();

  public static float rumbleDampener_800bee80;

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
  public static int input_800bee90;
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
  public static int press_800bee94;
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
  public static int repeat_800bee98;
  public static int analogAngle_800bee9c;

  public static int _800beea4;

  public static int _800beeac;

  public static int analogMagnitude_800beeb4;

  public static int analogInput_800beebc;

  public static int continentIndex_800bf0b0;

  public static int _800bf0cf;
}
