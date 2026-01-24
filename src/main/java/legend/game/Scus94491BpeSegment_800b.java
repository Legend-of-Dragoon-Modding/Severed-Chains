package legend.game;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.gte.MV;
import legend.game.combat.encounters.Encounter;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.types.EnemyDrop;
import legend.game.inventory.Equipment;
import legend.game.inventory.ItemStack;
import legend.game.saves.CampaignType;
import legend.game.submap.SobjPos14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.GameState52c;
import legend.game.types.GsRVIEW2;
import legend.game.types.Model124;
import org.joml.Vector2f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class Scus94491BpeSegment_800b {
  private Scus94491BpeSegment_800b() { }

  public static GameState52c gameState_800babc8;
  public static int battleStage_800bb0f4;
  public static int encounterId_800bb0f8;
  public static Encounter encounter;
  public static int tickCount_800bb0fc;

  public static int pregameLoadingStage_800bb10c;

  public static int goldGainedFromCombat_800bc920;

  public static final List<EnemyDrop> itemsDroppedByEnemies_800bc928 = new ArrayList<>();
  public static final List<ItemStack> itemOverflow = new ArrayList<>();
  public static final List<Equipment> equipmentOverflow = new ArrayList<>();
  public static boolean battleLoaded_800bc94c;
  public static final Int2IntMap spGained_800bc950 = new Int2IntOpenHashMap();
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
  public static final AtomicInteger loadingMonsterModels = new AtomicInteger();

  public static final IntList livingCharIds_800bc968 = new IntArrayList();
  /**
   * <ol>
   *   <li value="1">Combat victory</li>
   *   <li value="2">Game over</li>
   *   <li value="4">FMV</li>
   * </ol>
   */
  public static int postBattleAction_800bc974;

  public static boolean _800bd7ac;
  public static int _800bd7b0;
  public static boolean submapFullyLoaded_800bd7b4;

  public static boolean transitioningFromCombatToSubmap_800bd7b8;

  public static final GsRVIEW2 rview2_800bd7e8 = new GsRVIEW2();
  public static int submapId_800bd808;

  public static final SobjPos14[] sobjPositions_800bd818 = new SobjPos14[24];
  static {
    Arrays.setAll(sobjPositions_800bd818, i -> new SobjPos14());
  }

  public static int previousSubmapCut_800bda08;
  public static BattleStage stage_800bda0c;
  public static final Model124 shadowModel_800bda10 = new Model124("Shadow");

  public static final int[] characterIndices_800bdbb8 = new int[9];
  public static final int[] secondaryCharIds_800bdbf8 = new int[9];

  public static RegistryDelegate<CampaignType> campaignType;
  public static boolean loadingNewGameState_800bdc34;

  public static boolean characterStatsLoaded_800be5d0;

  public static final ActiveStatsa0[] stats_800be5f8 = new ActiveStatsa0[9];
  static {
    Arrays.setAll(stats_800be5f8, i -> new ActiveStatsa0());
  }

  // These are outside of SMAP because they have to persist between engine states
  public static final MV playerPositionBeforeBattle_800bed30 = new MV();
  public static final Vector2f screenOffsetBeforeBattle_800bed50 = new Vector2f();

  public static int _800beea4;

  public static int _800beeac;

  public static int continentIndex_800bf0b0;
}
