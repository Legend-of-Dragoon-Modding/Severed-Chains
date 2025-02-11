package legend.game.combat;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.QueuedModelBattleTmd;
import legend.core.QueuedModelStandard;
import legend.core.Random;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdWithId;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.memory.types.FloatRef;
import legend.core.opengl.McqBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.Scus94491BpeSegment;
import legend.game.characters.Element;
import legend.game.characters.Stat;
import legend.game.characters.StatMod;
import legend.game.characters.StatModConfig;
import legend.game.characters.StatModType;
import legend.game.characters.StatType;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.AttackEvent;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityStat;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.deff.LoadedDeff24;
import legend.game.combat.effects.AdditionCharEffectData0c;
import legend.game.combat.effects.AdditionNameTextEffect1c;
import legend.game.combat.effects.AdditionSparksEffect08;
import legend.game.combat.effects.AdditionStarburstEffect10;
import legend.game.combat.effects.Attachment18;
import legend.game.combat.effects.AttachmentHost;
import legend.game.combat.effects.BillboardSpriteEffect0c;
import legend.game.combat.effects.ButtonPressHudMetrics06;
import legend.game.combat.effects.DeffTmdRenderer14;
import legend.game.combat.effects.EffectAttachment;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.effects.FullScreenOverlayEffect0e;
import legend.game.combat.effects.GenericSpriteEffect24;
import legend.game.combat.effects.GuardEffect06;
import legend.game.combat.effects.ModelEffect13c;
import legend.game.combat.effects.MonsterDeathEffect34;
import legend.game.combat.effects.ProjectileHitEffect14;
import legend.game.combat.effects.RadialGradientEffect14;
import legend.game.combat.effects.RedEyeDragoonTransformationFlameArmorEffect20;
import legend.game.combat.effects.ScriptDeffEffect;
import legend.game.combat.effects.ScriptDeffManualLoadingEffect;
import legend.game.combat.effects.SpTextEffect40;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.effects.TextureAnimationAttachment1c;
import legend.game.combat.effects.WeaponTrailEffect3c;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.environment.BattleStageDarkening1800;
import legend.game.combat.environment.BattleStruct14;
import legend.game.combat.environment.BttlLightStruct84;
import legend.game.combat.environment.BttlLightStruct84Sub38;
import legend.game.combat.environment.EncounterData38;
import legend.game.combat.environment.StageAmbiance4c;
import legend.game.combat.environment.StageData2c;
import legend.game.combat.particles.ParticleManager;
import legend.game.combat.types.AttackType;
import legend.game.combat.types.BattleAsset08;
import legend.game.combat.types.BattleObject;
import legend.game.combat.types.BattleStateEf4;
import legend.game.combat.types.CombatantAsset0c;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.CompressedAsset08;
import legend.game.combat.types.DragoonSpells09;
import legend.game.combat.types.EnemyDrop;
import legend.game.combat.types.EnemyRewards08;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.types.StageDeffThing08;
import legend.game.combat.ui.BattleHud;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.UiBox;
import legend.game.fmv.Fmv;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.PostBattleScreen;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleEndedEvent;
import legend.game.modding.events.battle.BattleEntityTurnEvent;
import legend.game.modding.events.battle.BattleStartedEvent;
import legend.game.modding.events.battle.EnemyRewardsEvent;
import legend.game.modding.events.battle.MonsterStatsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptEnum;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.sound.QueuedSound28;
import legend.game.sound.SoundFile;
import legend.game.sound.SpuStruct08;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CContainer;
import legend.game.types.CContainerSubfile2;
import legend.game.types.CharacterData2c;
import legend.game.types.EquipmentSlot;
import legend.game.types.Keyframe0c;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.SpellStats0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.game.unpacker.Unpacker;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDeffSounds;
import static legend.game.Scus94491BpeSegment.loadDir;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnDirSync;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadEncounterSoundsAndMusic;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadMcq;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.setDepthResolution;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.startEncounterSounds;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020308;
import static legend.game.Scus94491BpeSegment_8002.adjustModelUvs;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.loadPlayerModelAndAnimation;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.scriptDeallocateAllTextboxes;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.sssqResetStuff;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.getScreenOffset;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_8004.previousEngineState_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeOut;
import static legend.game.Scus94491BpeSegment_8004.stopSoundSequence;
import static legend.game.Scus94491BpeSegment_8005.characterSoundFileIndices_800500f8;
import static legend.game.Scus94491BpeSegment_8005.monsterSoundFileIndices_800500e8;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8005.vramSlots_8005027c;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bc9a8;
import static legend.game.Scus94491BpeSegment_800b.battleFlags_800bc960;
import static legend.game.Scus94491BpeSegment_800b.battleLoaded_800bc94c;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.equipmentOverflow;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.itemOverflow;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.livingCharCount_800bc97c;
import static legend.game.Scus94491BpeSegment_800b.livingCharIds_800bc968;
import static legend.game.Scus94491BpeSegment_800b.loadingMonsterModels;
import static legend.game.Scus94491BpeSegment_800b.postBattleAction_800bc974;
import static legend.game.Scus94491BpeSegment_800b.postCombatMainCallbackIndex_800bc91c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.queuedSounds_800bd110;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.unlockedUltimateAddition_800bc910;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;
import static legend.game.combat.Monsters.enemyRewards_80112868;
import static legend.game.combat.Monsters.monsterNames_80112068;
import static legend.game.combat.Monsters.monsterStats_8010ba98;
import static legend.game.combat.SBtld._8011517c;
import static legend.game.combat.SBtld.loadAdditions;
import static legend.game.combat.SEffe.addGenericAttachment;
import static legend.game.combat.SEffe.allocateEffectManager;
import static legend.game.combat.SEffe.applyScreenDarkening;
import static legend.game.combat.SEffe.loadDeffStageEffects;
import static legend.game.combat.SEffe.renderButtonPressHudElement1;
import static legend.game.combat.SEffe.scriptGetPositionScalerAttachmentVelocity;
import static legend.game.combat.environment.Ambiance.stageAmbiance_801134fc;
import static legend.game.combat.environment.BattleCamera.UPDATE_REFPOINT;
import static legend.game.combat.environment.BattleCamera.UPDATE_VIEWPOINT;
import static legend.game.combat.environment.StageData.getEncounterStageData;

public class Battle extends EngineState {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Battle.class);
  private static final Marker CAMERA = MarkerManager.getMarker("CAMERA");
  private static final Marker DEFF = MarkerManager.getMarker("DEFF");

  public static final Vector3f ZERO = new Vector3f();

  public final BattleHud hud = new BattleHud(this);
  public final BattleCamera camera_800c67f0 = new BattleCamera();
  public final ParticleManager particles = new ParticleManager(this.camera_800c67f0);

  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #initBattle}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #loadStageAndControllerScripts}</li>
   *   <li>{@link #initializeViewportAndCamera}</li>
   *   <li>{@link Scus94491BpeSegment#nextLoadingStage}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#nextLoadingStage}</li>
   *   <li>{@link #battleInitiateAndPreload_800c772c}</li>
   *   <li>{@link #allocateEnemyBattleEntities()}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #allocatePlayerBattleEntities()}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #loadEncounterAssets}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #loadHudAndAttackAnimations}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #FUN_800c79f0}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #loadSEffe}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #calculateInitialTurnValues}</li>
   *   <li>{@link #battleTick}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link #performPostBattleAction}</li>
   *   <li>{@link #deallocateCombat}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#nextLoadingStage}</li>
   *   <li>{@link Scus94491BpeSegment#renderPostCombatScreen}</li>
   *   <li>{@link Scus94491BpeSegment#transitionBackFromBattle}</li>
   * </ol>
   */
  private final Runnable[] battleLoadingStage_8004f5d4 = {
    Scus94491BpeSegment::waitForFilesToLoad,
    this::initBattle,
    Scus94491BpeSegment::waitForFilesToLoad,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::loadStageAndControllerScripts,
    this::initializeViewportAndCamera,
    Scus94491BpeSegment::nextLoadingStage,
    Scus94491BpeSegment::waitForFilesToLoad,
    Scus94491BpeSegment::nextLoadingStage,
    this::battleInitiateAndPreload_800c772c,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::allocateEnemyBattleEntities,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::allocatePlayerBattleEntities,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::loadEncounterAssets,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::loadHudAndAttackAnimations,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::FUN_800c79f0,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::loadSEffe,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::calculateInitialTurnValues,
    this::battleTick,
    Scus94491BpeSegment::waitForFilesToLoad,
    this::performPostBattleAction,
    this::deallocateCombat,
    Scus94491BpeSegment::waitForFilesToLoad,
    Scus94491BpeSegment::nextLoadingStage,
    Scus94491BpeSegment::renderPostCombatScreen,
    Scus94491BpeSegment::transitionBackFromBattle,
  };

  private int currentPostCombatActionFrame_800c6690;

  private final CombatantStruct1a8[] combatants_8005e398 = new CombatantStruct1a8[10];
  /** The number of {@link #combatants_8005e398}s */
  private int combatantCount_800c66a0;
  public int currentStage_800c66a4;

  public boolean battleInitialCameraMovementFinished_800c66a8;
  private int currentCompressedAssetIndex_800c66ac;

  private boolean stageHasModel_800c66b8;
  /** Character scripts deallocated? */
  private boolean combatDisabled_800c66b9;

  public ScriptState<? extends BattleEntity27c> forcedTurnBent_800c66bc;

  private final Object usedMonsterTextureSlotsLock = new Object();
  private int usedMonsterTextureSlots_800c66c4;
  public ScriptState<? extends BattleEntity27c> currentTurnBent_800c66c8;
  private int mcqBaseOffsetX_800c66cc;

  private boolean shouldRenderMcq_800c66d4;

  private ScriptFile playerBattleScript_800c66fc;

  public int cameraScriptSubtableJumpIndex_800c6700;
  public int cameraScriptSubtableJumpIndex_800c6704;

  public int _800c6710;

  public StageData2c currentStageData_800c6718;
  public int cameraScriptMainTableJumpIndex_800c6748;
  private ScriptState<Void> scriptState_800c674c;

  public boolean shouldRenderStage_800c6754;

  private int currentDisplayableIconsBitset_800c675c;

  public boolean shouldRenderMcq_800c6764;

  public int mcqStepX_800c676c;
  public int mcqStepY_800c6770;
  public int mcqOffsetX_800c6774;
  public int mcqOffsetY_800c6778;

  /** This may be unused. Only referenced by the script engine, but seems like there may be no real uses */
  public int currentCameraIndex_800c6780;

  public int _800c67c8;
  public int _800c67cc;
  public int _800c67d0;

  /** script using attack item? */
  public ScriptState<? extends BattleEntity27c> scriptState_800c6914;
  /** bent index, used in pcs and possibly elsewhere */
  public int _800c6918;

  private int lightTicks_800c6928;
  public BttlLightStruct84[] lights_800c692c;
  public BattleLightStruct64 _800c6930;

  private LoadedDeff24 loadedDeff_800c6938;
  public static DeffManager7cc deffManager_800c693c;

  public static BattleStageDarkening1800 stageDarkening_800c6958;
  public static int stageDarkeningClutWidth_800c695c;

  public final DragoonSpells09[] dragoonSpells_800c6960 = new DragoonSpells09[3];
  {
    Arrays.setAll(this.dragoonSpells_800c6960, i -> new DragoonSpells09());
  }

  public final String[] currentEnemyNames_800c69d0 = new String[9];

  public Element dragoonSpaceElement_800c6b64;

  public final int[] monsterBents_800c6b78 = new int[9];
  private int monsterCount_800c6b9c;

  public final String[] melbuMonsterNames_800c6ba8 = new String[3];

  public final List<Item> usedRepeatItems_800c6c3c = new ArrayList<>();

  public int countCombatUiFilesLoaded_800c6cf4;

  public static final Vector2i[] combatUiElementRectDimensions_800c6e48 = {
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
    new Vector2i(16, 16),
  };
  /** Note: retail overlay doesn't have the last two elements, but the method that uses this copies the array and adds new elements */
  public static final int[] battleHudTextureVramXOffsets_800c6e60 = {0, 0x10, 0x20, 0x30, 0, 0x10};
  public static final int[] characterDragoonIndices_800c6e68 = {0, 2, 5, 6, 4, 2, 1, 3, 5, 7};

  public static final int[] melbuMonsterNameIndices_800c6e90 = {395, 396, 397};

  public static final int[] melbuStageToMonsterNameIndices_800c6f30 = {0, 0, 0, 0, 1, 0, 2};
  public static final int[][] textboxColours_800c6fec = {{76, 183, 225}, {182, 112, 0}, {25, 15, 128}, {128, 128, 128}, {129, 9, 236}, {213, 197, 58}, {72, 255, 159}, {238, 9, 9}, {0, 41, 159}};

  @SuppressWarnings("unchecked")
  public static final RegistryDelegate<Element>[] characterElements_800c706c = new RegistryDelegate[] {LodMod.FIRE_ELEMENT, LodMod.WIND_ELEMENT, LodMod.LIGHT_ELEMENT, LodMod.DARK_ELEMENT, LodMod.THUNDER_ELEMENT, LodMod.WIND_ELEMENT, LodMod.WATER_ELEMENT, LodMod.EARTH_ELEMENT, LodMod.LIGHT_ELEMENT};

  /** Different sets of bents for different target types (chars, monsters, all) */
  public ScriptState<BattleEntity27c>[][] targetBents_800c71f0;

  public static final SpellStats0c[] spellStats_800fa0b8 = new SpellStats0c[128];
  public static final int[] postCombatActionTotalFrames_800fa6b8 = {0, 82, 65, 15, 10, 15};

  public static final int[] postBattleCamera_800fa6c4 = {-1, 195, 211, -1, 211, -1};

  public static final int[] postCombatActionFrames_800fa6d0 = {0, 30, 45, 30, 45, 30};

  public int mcqColour_800fa6dc = 0x80;
  public static final Rect4i[] combatantTimRects_800fa6e0 = {
    new Rect4i(0, 0, 0, 0), new Rect4i(320, 256, 64, 256),
    new Rect4i(384, 256, 64, 256), new Rect4i(448, 256, 64, 256),
    new Rect4i(512, 256, 64, 256), new Rect4i(576, 256, 64, 256),
    new Rect4i(640, 256, 64, 256), new Rect4i(512, 0, 64, 256),
    new Rect4i(576, 0, 64, 256), new Rect4i(640, 0, 64, 256),
  };

  public static final Random seed_800fa754 = new Random();

  /** ASCII chars - [0-9][A-Z][a-z]'-& <null> */
  public static final int[] asciiTable_800fa788 = {
    48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 82, 81, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
    112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 39, 45, 38, 32, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0,
  };
  public static final int[] charWidthAdjustTable_800fa7cc = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, -1, -1, 0, 1, -1, 0, 4, 2, -1, 1, -2, 0, -2, 0, -1, -2, 0, 1, -1, -1, -2, -1, 0, 0, 1, 1, 1, 1, 1, 3, 1, 1, 5, 4, 1, 5, -2, 1, 1, 1, 1, 3, 2, 3, 1, 1, -3, 1, 1, 2, 4, 2, -1, 6};

  public static final String[] additionNames_800fa8d4 = {"Double Slash", "Volcano", "Burning Rush", "Crush Dance", "Madness Hero", "Moon Strike", "Blazing Dynamo", "", "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance", "Flower Storm", "", "Whip Smack", "More & More", "Hard Blade", "Demon's Dance", "", "Pursuit", "Inferno", "Bone Crush", "", "Double Smack", "Hammer Spin", "Cool Boogie", "Cat's Cradle", "Perky Step", "", "Double Punch", "Ferry of Styx", "Summon 4 Gods", "5-Ring Shattering", "Hex Hammer", "Omni-Sweep", "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance", "Flower Storm"};

  /** Next 4 globals are related to SpTextEffect40 */
  private int _800faa90;
  private int _800faa92;
  private int _800faa94;

  /** Next global is related to AdditionNameTextEffect1c */
  private int _800faa9d;

  public static final ButtonPressHudMetrics06[] buttonPressHudMetrics_800faaa0 = {
    new ButtonPressHudMetrics06(0, 184, 96, 24, 16, 42),
    new ButtonPressHudMetrics06(0, 208, 96, 24, 16, 42),
    new ButtonPressHudMetrics06(0, 232, 96, 23, 23, 42),
    new ButtonPressHudMetrics06(0, 16, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 24, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 32, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 40, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 48, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 56, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 64, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 72, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 80, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 88, 40, 8, 16, 33),
    new ButtonPressHudMetrics06(0, 176, 32, 16, 16, 42),
    new ButtonPressHudMetrics06(0, 192, 128, 40, 16, 59),
    new ButtonPressHudMetrics06(0, 152, 112, 40, 16, 60),
    new ButtonPressHudMetrics06(0, 192, 112, 40, 16, 62),
    new ButtonPressHudMetrics06(0, 72, 128, 40, 16, 59),
    new ButtonPressHudMetrics06(0, 184, 96, 16, 16, 27),
    new ButtonPressHudMetrics06(0, 200, 96, 16, 16, 19),
    new ButtonPressHudMetrics06(0, 232, 120, 23, 24, 12),
    new ButtonPressHudMetrics06(0, 0, 208, 32, 32, 58),
    new ButtonPressHudMetrics06(0, 32, 208, 32, 32, 58),
    new ButtonPressHudMetrics06(0, 40, 128, 32, 16, 61),
    new ButtonPressHudMetrics06(1, 176, 64, 180, 68, 43),
    new ButtonPressHudMetrics06(1, 180, 64, 176, 68, 43),
    new ButtonPressHudMetrics06(1, 176, 68, 180, 64, 43),
    new ButtonPressHudMetrics06(1, 180, 68, 176, 64, 43),
    new ButtonPressHudMetrics06(1, 183, 64, 191, 68, 43),
    new ButtonPressHudMetrics06(1, 191, 68, 183, 64, 43),
    new ButtonPressHudMetrics06(1, 176, 71, 180, 79, 43),
    new ButtonPressHudMetrics06(1, 180, 79, 176, 71, 43),
    new ButtonPressHudMetrics06(1, 183, 71, 191, 79, 43),
    new ButtonPressHudMetrics06(0, 0, 208, 24, 24, 29),
    new ButtonPressHudMetrics06(0, 24, 208, 24, 24, 29),
    new ButtonPressHudMetrics06(0, 48, 208, 24, 24, 29),
    new ButtonPressHudMetrics06(1, 239, 15, 224, 8, 18),
    new ButtonPressHudMetrics06(0, 232, 120, 23, 24, 12),
    new ButtonPressHudMetrics06(0, 184, 96, 24, 24, 19),
    new ButtonPressHudMetrics06(0, 208, 96, 24, 24, 19),
    new ButtonPressHudMetrics06(0, 232, 96, 23, 24, 19),
  };

  public static final int[] enemyDeffFileIndices_800faec4 = {
    1, 2, 3, 7, 8, 9, 10, 12, 14, 16, 19, 22, 24, 27, 28, 33, 34, 35, 37, 38, 41, 42, 43, 44, 47, 48, 49, 50, 56, 57, 58, 59, 61, 62, 63, 64, 65, 66, 69, 73, 77, 81, 85, 89, 90, 91, 92, 93, 94, 95,
    98, 99, 100, 101, 102, 103, 107, 108, 109, 110, 111, 114, 115, 116, 0, 5, 6, 7, 8, 9, 10, 11, 12, 13, 17, 18, 19, 20, 22, 23, 26, 27, 28, 29, 30, 31, 32, 33, 34, 39, 40, 43, 44, 45, 46, 47, 48, 53, 56, 59,
    60, 61, 62, 63, 64, 67, 72, 75, 80, 85, 88, 89, 90, 91, 92, 96, 97, 98, 99, 100, 102, 103, 104, 105, 106, 107, 108, 110, 111, 112, 113, 114, 118, 122, 126, 130, 138, 142, 146, 148, 153, 154, 155, 156, 157, 0,
  };

  private int deffLoadingStage_800fafe8;
  public static final int[] dragoonDeffFlags_800fafec = {
    112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 64, 64,
    64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 64, 20, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
  };
  public static final int[] dragoonDeffsWithExtraTims_800fb040 = {4, 9, 10, 11, 11, 13, 20, 22, 27, 28, 30, 36, 40, 42, 44, 46, 65, 66, 70, 71, 73, 75, 78, 82};
  /**
   * <ol start="0">
   *   <li>10</li>
   *   <li>67</li>
   *   <li>69</li>
   *   <li>70 Melbu "generation" paintings</li>
   *   <li>70 Melbu "generation" paintings</li>
   *   <li>70 Melbu "generation" paintings</li>
   *   <li>0xff (end)</li>
   *   <li>0</li>
   * </ol>
   */
  public static final int[] cutsceneDeffsWithExtraTims_800fb05c = {10, 67, 69, 70, 70, 70};
  /**
   * <ol start="0">
   *   <li>Melbu 2-1</li>
   *   <li>Melbu 3-1</li>
   *   <li>Melbu 3-2</li>
   *   <li>Melbu 3</li>
   *   <li>Melbu 4</li>
   * </ol>
   *
   * The rest are -1
   */
  public static final int[] melbuStageIndices_800fb064 = {93, 94, 95, 25, 52, -1, -1, -1};
  public static final int[] modelVramSlotIndices_800fb06c = {0, 0, 0, 0, 0, 0, 0, 0, 14, 15, 16, 17, 10, 11, 12, 13, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0};

  @Override
  public int tickMultiplier() {
    return 1;
  }

  @Override
  @Method(0x800186a0L)
  public void tick() {
    if(battleLoaded_800bc94c) {
      this.checkIfCharacterAndMonsterModelsAreLoadedAndCacheLivingBents();
      this.battleLoadingStage_8004f5d4[pregameLoadingStage_800bb10c].run();

      if(battleLoaded_800bc94c) {
        this.renderBattleEnvironment();
      }
    } else {
      //LAB_8001870c
      this.battleLoadingStage_8004f5d4[pregameLoadingStage_800bb10c].run();
    }

    //LAB_80018734
  }

  @Override
  public void restoreMusicAfterMenu() {
    //LAB_8001e264
    sssqResetStuff();
  }

  @Override
  public Function<RunningScript, FlowControl>[] getScriptFunctions() {
    final Function<RunningScript, FlowControl>[] functions = new Function[1024];

    functions[32] = this::scriptResetCameraMovement;
    functions[33] = this::FUN_800dac20;
    functions[34] = this::FUN_800db034;
    functions[35] = this::FUN_800db460;
    functions[36] = this::FUN_800db574;
    functions[37] = this::FUN_800db688;
    functions[38] = this::FUN_800db79c;
    functions[39] = this::FUN_800db8b0;
    functions[40] = this::FUN_800db9e0;
    functions[41] = this::scriptIsCameraMoving;
    functions[42] = this::scriptCalculateCameraValue;
    functions[43] = this::FUN_800dbb9c;
    functions[44] = this::scriptWobbleCamera;
    functions[45] = this::scriptStopCameraMovement;
    functions[46] = this::scriptSetViewportTwist;
    functions[47] = this::scriptStopCameraZAcceleration;
    functions[48] = this::scriptSetCameraProjectionPlaneDistance;
    functions[49] = this::scriptGetProjectionPlaneDistance;
    functions[50] = this::scriptMoveCameraProjectionPlane;

    functions[128] = this::scriptSetBentPos;
    functions[129] = this::scriptGetBentPos;
    functions[130] = this::scriptSetBentRotation;
    functions[131] = this::FUN_800cc7d8;
    functions[132] = this::scriptSetBentRotationY;
    functions[133] = this::loadAllCharAttackAnimations;
    functions[134] = this::scriptGetBentRotation;
    functions[135] = Scus94491BpeSegment::scriptRewindAndPause2;
    functions[136] = this::scriptGetMonsterStatusResistFlags;
    functions[137] = Scus94491BpeSegment::scriptRewindAndPause2;
    functions[138] = this::FUN_800cb618;
    functions[139] = this::scriptSetInterpolationEnabled;
    functions[140] = this::FUN_800cb6bc;
    functions[141] = this::FUN_800cb764;
    functions[142] = this::FUN_800cb76c;
    functions[143] = this::scriptGetLoadingBentAnimationIndex;
    functions[144] = this::scriptPauseAnimation;
    functions[145] = this::scriptResumeAnimation;
    functions[146] = this::scriptSetBentAnimationLoopState;
    functions[147] = this::scriptAnimationHasFinished;
    functions[148] = this::FUN_800cbb00;
    functions[149] = this::FUN_800cbc14;
    functions[150] = this::FUN_800cbde0;
    functions[151] = this::FUN_800cbef8;
    functions[152] = this::FUN_800cc0c8;
    functions[153] = this::FUN_800cc1cc;
    functions[154] = this::FUN_800cc364;
    functions[155] = this::FUN_800cc46c;
    functions[156] = this::scriptBentLookAtBent;
    functions[157] = this::FUN_800cc698;
    functions[158] = this::FUN_800cc784;
    functions[159] = this::scriptLoadAttackAnimations;
    functions[160] = this::scriptSetUpAndHandleCombatMenu;
    functions[161] = Scus94491BpeSegment::scriptRewindAndPause2;
    functions[162] = Scus94491BpeSegment::scriptRewindAndPause2;
    functions[163] = Scus94491BpeSegment::scriptRewindAndPause2;
    functions[164] = this::scriptRenderDamage;
    functions[165] = this::scriptAddFloatingNumberForBent;
    functions[166] = this::FUN_800ccba4;
    functions[167] = this::scriptGetCharOrMonsterId;
    functions[168] = this::scriptSetBentStat;
    functions[169] = this::scriptGetBentStat;
    functions[170] = this::scriptSetPostBattleAction;
    functions[171] = this::scriptSetBattleHudVisibility;
    functions[172] = this::FUN_800ccef8;
    functions[173] = this::scriptSetBentDeadAndDropLoot;
    functions[174] = this::scriptGetHitProperty;
    functions[175] = this::scriptSetBentDead;
    functions[176] = this::scriptLevelUpAddition;
    functions[177] = this::scriptGetBentStat2;
    functions[178] = this::scriptSetBentRawStat;

    functions[238] = this::scriptPlayBentSound;
    functions[239] = this::scriptStopBentSound;

    functions[320] = this::scriptEnableBentTextureAnimation;
    functions[321] = Scus94491BpeSegment::scriptRewindAndPause2;
    functions[322] = this::scriptSetLoadingBentAnimationIndex;
    functions[323] = this::FUN_800cb95c;
    functions[324] = Scus94491BpeSegment::scriptRewindAndPause2;

    functions[352] = this::scriptSetModelPartVisibility;
    functions[353] = this::scriptCopyVram;
    functions[354] = this::scriptLoadGlobalAsset;
    functions[355] = this::scriptWaitGlobalAssetAllocation;
    functions[356] = this::scriptDeallocateGlobalAsset;
    functions[357] = this::scriptAddCombatant;
    functions[358] = this::scriptDeallocateAndRemoveCombatant;
    functions[359] = this::FUN_800cda78;
    functions[360] = this::scriptAllocateBent;
    functions[361] = this::scriptLoadModelToCombatantFromGlobalAssets;
    functions[362] = this::FUN_800cd7a8;
    functions[363] = this::scriptLoadAnimToCombatantFromGlobalAssetsOrDeallocate;
    functions[364] = this::scriptLoadTextureToCombatantFromGlobalAssets;
    functions[365] = this::scriptGetBentNobj;
    functions[366] = this::scriptDeallocateCombatant;
    functions[367] = this::scriptStopRenderingStage;
    functions[368] = this::scriptLoadStage;
    functions[369] = this::FUN_800cd910;
    functions[370] = this::scriptGetCombatantIndex;
    functions[371] = this::scriptGetBentSlot;
    functions[372] = this::scriptDisableCombat;

    functions[416] = this::scriptLoadDragoonDeffSync;

    functions[419] = this::scriptResetLights;
    functions[420] = this::scriptSetLightDirection;
    functions[421] = this::scriptGetLightDirection;
    functions[422] = this::FUN_800e48a8;
    functions[423] = this::FUN_800e48e8;
    functions[424] = this::FUN_800e4964;
    functions[425] = this::FUN_800e4abc;
    functions[426] = this::scriptSetBattleLightColour;
    functions[427] = this::scriptGetBattleLightColour;
    functions[428] = this::scriptSetBattleBackgroundLightColour;
    functions[429] = this::scriptGetBattleBackgroundLightColour;
    functions[430] = this::FUN_800e4dfc;
    functions[431] = this::FUN_800e4e2c;
    functions[432] = this::FUN_800e4e64;
    functions[433] = this::FUN_800e4ea0;
    functions[434] = this::FUN_800e4fa0;
    functions[435] = this::FUN_800e50e8;
    functions[436] = this::FUN_800e52f8;
    functions[437] = this::FUN_800e540c;
    functions[438] = this::FUN_800e54f8;
    functions[439] = this::FUN_800e5528;
    functions[440] = this::FUN_800e5560;
    functions[441] = this::FUN_800e559c;
    functions[442] = this::FUN_800e569c;
    functions[443] = this::scriptApplyStageAmbiance;
    functions[444] = this::FUN_800e59d8;
    // functions[445] = Temp::FUN_800ca734;

    functions[480] = this::scriptCheckPhysicalHit;
    functions[481] = this::scriptPhysicalAttack;
    functions[482] = this::scriptGetBentPos2;
    functions[483] = this::scriptAddFloatingNumber;
    functions[484] = this::scriptCheckPhysicalHit;
    functions[485] = this::scriptCheckPhysicalHit;
    functions[486] = this::scriptCheckPhysicalHit;
    functions[487] = this::scriptGiveSp;
    functions[488] = this::scriptConsumeSp;
    functions[489] = this::scriptInitSpellAndItemMenu;
    functions[490] = this::scriptGetItemOrSpellTargetingInfo;
    functions[491] = this::scriptGetItemOrSpellAttackTarget;
    functions[492] = this::scriptDragoonMagicStatusItemAttack;
    functions[493] = this::scriptSetTempSpellStats;
    functions[494] = this::scriptRenderRecover;
    functions[495] = this::scriptItemMagicAttack;
    functions[496] = this::scriptSetTempItemMagicStats;
    functions[497] = this::scriptTakeItem;
    functions[498] = this::scriptGiveItem;
    functions[499] = this::scriptSetHudTargetBobj;
    functions[500] = this::scriptIsFloatingNumberOnScreen;
    functions[501] = this::scriptSetDragoonSpaceElementIndex;
    functions[502] = this::FUN_800f9b94;
    functions[503] = this::FUN_800f9bd4;
    functions[504] = this::FUN_800f9c00;
    functions[505] = this::scriptRenderBattleHudBackground;
    functions[506] = this::scriptSetDisabledMenuIcons;
    functions[507] = this::scriptCheckSpellOrStatusHit;
    functions[508] = this::scriptCheckItemHit;
    functions[509] = this::scriptFinishBentTurn;

    functions[512] = this::scriptSetBentZOffset;
    functions[513] = this::scriptSetBentScaleUniform;
    functions[514] = this::scriptSetBentScale;
    functions[515] = this::scriptAttachShadowToBottomOfBentModel;
    functions[516] = this::scriptDisableBentShadow;
    functions[517] = this::scriptSetBentShadowSize;
    functions[518] = this::scriptSetBentShadowOffset;
    functions[519] = this::scriptApplyScreenDarkening;
    functions[520] = this::scriptAttachShadowToBottomOfBentModel;
    functions[521] = this::scriptGetStageNobj;
    functions[522] = this::scriptShowStageModelPart;
    functions[523] = this::scriptHideStageModelPart;
    functions[524] = this::scriptAttachShadowToBentModelPart;
    functions[525] = this::scriptUpdateBentShadowType;
    functions[526] = this::scriptSetStageZ;

    functions[544] = SEffe::scriptGetRelativePosition;
    functions[545] = SEffe::scriptSetRelativePosition;
    functions[546] = SEffe::scriptGetRotationDifference;
    functions[547] = SEffe::scriptSetRelativeRotation;
    functions[548] = SEffe::scriptGetScaleRatio;
    functions[549] = SEffe::scriptSetRelativeScale;
    functions[550] = SEffe::scriptGetColourDifference;
    functions[551] = SEffe::scriptSetRelativeColour;
    functions[552] = SEffe::scriptGetGenericEffectValue;
    functions[553] = SEffe::scriptSetGenericEffectValue;
    functions[554] = SEffe::scriptTransformBobjPositionToScreenSpace;
    functions[555] = SEffe::scriptRemoveEffectAttachment;
    functions[556] = SEffe::scriptHasEffectAttachment;
    functions[557] = SEffe::scriptWaitForPositionScalerToFinish;
    functions[558] = SEffe::scriptAddPositionScalerAttachment;
    functions[559] = SEffe::scriptAddRelativePositionScalerTicks0;
    functions[560] = SEffe::scriptAddRelativePositionScalerDistance0;
    functions[561] = SEffe::scriptAddPositionScalerMoveToParent;
    functions[562] = SEffe::FUN_801155f8; // no-op
    functions[563] = SEffe::scriptGetRelativeAngleBetweenBobjs;
    functions[564] = SEffe::scriptRotateBobjTowardsPoint;
    functions[565] = SEffe::scriptSetEffectApplyRotationAndScaleFlag;
    functions[566] = SEffe::scriptGetEffectTranslationRelativeToParent;
    functions[567] = SEffe::scriptAddRotationScalerAttachment;
    functions[568] = SEffe::scriptAddRotationScalerAttachmentTicks;
    functions[569] = SEffe::scriptAddRotationScalerAttachmentDistance;
    functions[570] = SEffe::scriptAddRotationScalerAttachmentTowardsPointTicks;
    functions[571] = SEffe::scriptAddRotationScalerAttachmentTowardsPointDistance;
    functions[572] = SEffe::FUN_80115600; // no-op
    functions[573] = this::scriptAllocateClonedModelEffect;
    functions[574] = SEffe::scriptLoadEffectModelAnimation;
    functions[575] = SEffe::scriptAddScaleScalerAttachment;
    functions[576] = SEffe::scriptAddScaleScalerMultiplicativeAttachmentTicks;
    functions[577] = SEffe::scriptAddScaleScalerMultiplicativeAttachmentDistance; // not implemented
    functions[578] = SEffe::scriptSetScriptScript;
    functions[579] = SEffe::scriptAddScaleScalerDifferenceAttachmentTicks;
    functions[580] = SEffe::scriptAddColourScalerAttachment;
    functions[581] = SEffe::scriptAddConstantColourScalerAttachment;
    functions[582] = SEffe::scriptAddConstantColourScalerDistance; // not implemented
    functions[583] = SEffe::scriptRemoveGenericEffectAttachment;
    functions[584] = SEffe::FUN_80114f34; // no-op
    functions[585] = SEffe::scriptAddGenericAttachment;
    functions[586] = SEffe::scriptAddGenericAttachmentTicks;
    functions[587] = SEffe::scriptAddGenericAttachmentSpeed;
    functions[588] = SEffe::scriptAddLifespanAttachment;
    functions[589] = SEffe::scriptSetEffectErrorFlag;
    functions[590] = SEffe::scriptSetEffectTranslucencySourceFlag;
    functions[591] = SEffe::scriptSetEffectTranslucencyModeFlag;
    functions[592] = this::FUN_800e74ac;
    functions[593] = SEffe::scriptGetPositionScalerAttachmentVelocity;
    functions[594] = this::scriptAddOrUpdateTextureAnimationAttachment;
    functions[595] = SEffe::scriptSetEffectLightingDisableFlag;
    functions[596] = SEffe::scriptAddRelativePositionScalerDistance1;
    functions[597] = SEffe::scriptAddRelativePositionScalerTicks1;
    functions[598] = SEffe::scriptAddOrUpdatePositionScalerAttachment;
    functions[599] = SEffe::scriptUpdateParabolicPositionScalerAttachment;
    functions[600] = this::scriptAllocateEmptyEffectManagerChild;
    functions[601] = this::allocateBillboardSpriteEffect;
    functions[602] = this::FUN_800e9854;
    // functions[603] = Temp::FUN_800ca648;

    functions[605] = SEffe::scriptAllocateLmbAnimation;
    functions[606] = SEffe::allocateDeffTmd;
    functions[607] = this::FUN_800e99bc;
    functions[608] = SEffe::scriptSetLmbDeffFlag;

    functions[610] = this::scriptLoadCmbAnimation;
    functions[611] = SEffe::scriptAttachEffectToBobj;
    functions[612] = this::scriptHideEffectModelPart;
    functions[613] = this::scriptShowEffectModelPart;
    functions[614] = this::scriptAddRedEyeDragoonTransformationFlameArmorEffectAttachment;
    functions[615] = this::scriptApplyTextureAnimationAttachment;
    functions[616] = this::scriptRemoveTextureAnimationAttachment;
    // functions[617] = Temp::FUN_800caae4;
    functions[618] = SEffe::scriptLoadSameScriptAndJump;
    functions[619] = SEffe::allocateShirleyTransformWipeEffect;
    functions[620] = SEffe::FUN_80111a58;
    functions[621] = this::scriptGetEffectLoopCount;
    functions[622] = SEffe::allocateSpriteWithTrailEffect;
    functions[623] = this::scriptLoadDeff;
    functions[624] = this::scriptTickDeffLoadingStage;
    functions[625] = this::scriptGetDeffLoadingStage;
    functions[626] = SEffe::scriptGetEffectZ;
    functions[627] = SEffe::scriptSetEffectZ;
    functions[628] = SEffe::allocateDeffTmdRenderer;
    functions[629] = SEffe::scriptAttackEffectToBobjRelative;
    functions[630] = SEffe::scriptGetEffectRotation;
    functions[631] = SEffe::scriptSetLmbManagerTransformMetrics;
    functions[632] = SEffe::scriptAllocateBuggedEffect;

    functions[634] = SEffe::scriptWaitForXaToLoad;
    functions[635] = SEffe::scriptGetXaLoadingStage;
    functions[636] = SEffe::scriptPlayXaAudio;
    functions[637] = this::scriptLoadCutsceneDeff;
    functions[638] = this::scriptLoadSpellOrItemDeff;
    functions[639] = this::scriptLoadEnemyOrBossDeff;

    functions[640] = SEffe::scriptConvertRotationYxzToXyz;
    functions[641] = SEffe::scriptResetDeffManager;
    functions[642] = SEffe::FUN_8011287c;
    functions[643] = this::scriptSetModelShadow;
    functions[644] = this::scriptSetBttlShadowSize;
    functions[645] = this::scriptSetBttlShadowOffset;
    functions[646] = SEffe::scriptAllocateShadowEffect;
    functions[647] = SEffe::scriptUpdateDeffManagerFlags;
    functions[648] = SEffe::scriptLoadDeffStageEffects;
    functions[649] = SEffe::scriptGetEffectTextureMetrics;
    functions[650] = SEffe::scriptSetEffectNormalizeLightMatrixFlag;
    functions[651] = SEffe::scriptConsolidateEffectMemory;

    functions[712] = this::scriptPlayCombatantSound;
    functions[713] = this::scriptStopBentSound2;

    functions[736] = this::FUN_800d3090;
    functions[737] = this::scriptAllocateFullScreenOverlay;
    functions[738] = this::scriptRand;
    functions[739] = this::scriptSetWeaponTrailSegmentCount;
    functions[740] = this::FUN_800d3090;
    functions[741] = this::FUN_800d3090;
    functions[742] = this::FUN_800d3090;
    functions[743] = this::FUN_800d3090;
    functions[744] = this::scriptRenderColouredQuad;
    functions[745] = this::FUN_800cf0b4;
    functions[746] = SEffe::scriptAllocateParticleEffect;
    functions[747] = SEffe::scriptSetParticleAcceleration;
    functions[748] = this::FUN_800d3090;
    functions[749] = this::scriptAllocateProjectileHitEffect;
    functions[750] = this::FUN_800d09b8;
    functions[751] = this::scriptAllocateAdditionSparksEffect;
    functions[752] = SEffe::FUN_80102608;
    functions[753] = SEffe::scriptAllocateAdditionOverlaysEffect;
    functions[754] = SEffe::scriptGetHitCompletionState;
    functions[755] = SEffe::FUN_80108de8;
    functions[756] = this::scriptAllocateAdditionStarburstEffect;
    functions[757] = this::FUN_800d1cac;
    functions[758] = this::FUN_800d1cf4;
    functions[759] = SEffe::scriptAlterAdditionContinuationState;
    functions[760] = SEffe::FUN_80108df0;
    functions[761] = this::scriptAllocateGuardEffect;
    functions[762] = this::scriptAllocateWeaponTrailEffect;
    functions[763] = this::scriptAllocateRadialGradientEffect;
    functions[764] = this::scriptAllocateAdditionScript;
    functions[765] = this::scriptGetBobjLocalWorldMatrixTranslation;
    functions[766] = this::scriptAllocateSpTextEffect;
    functions[767] = this::scriptAllocateAdditionNameEffect;

    functions[800] = SEffe::scriptAllocateLensFlareEffect;
    functions[801] = SEffe::scriptAllocateWsDragoonTransformationFeathersEffect;
    functions[802] = SEffe::scriptAllocateGoldDragoonTransformEffect;
    functions[803] = SEffe::scriptAllocateStarChildrenMeteorEffect;
    functions[804] = SEffe::scriptAllocateStarChildrenImpactEffect;
    functions[805] = SEffe::scriptAllocateMoonlightStarsEffect;
    functions[806] = Scus94491BpeSegment::FUN_8001c5fc;
    functions[807] = Scus94491BpeSegment::FUN_8001c604;

    functions[832] = this::scriptRenderButtonPressHudElement;
    functions[833] = SEffe::FUN_80108df8;
    functions[834] = SEffe::FUN_80102610;
    functions[835] = this::scriptGetBentDimension;
    functions[836] = SEffe::scriptAllocateRainEffect;
    functions[837] = this::scriptApplyWeaponTrailScaling;
    functions[838] = SEffe::scriptAllocateElectricityEffect;
    functions[839] = SEffe::scriptGetBoltSegmentEnd;
    functions[840] = SEffe::scriptAllocateDragoonAdditionScript;
    functions[841] = SEffe::scriptAllocateThunderArrowEffect;
    // functions[842] = Temp::FUN_800c6968;
    functions[843] = SEffe::scriptAllocateScreenDistortionEffect;
    functions[844] = SEffe::scriptGetDragoonAdditionHitsCompleted;
    functions[845] = SEffe::FUN_801023f4;
    functions[846] = this::FUN_800cfec8;
    functions[847] = SEffe::scriptGetAliveParticles;
    functions[848] = SEffe::scriptGetParticlePosition;
    functions[849] = this::scriptSetMtSeed;
    functions[850] = SEffe::scriptAllocateVertexDifferenceAnimation;
    functions[851] = SEffe::scriptAllocateFrozenJetEffect;
    functions[852] = this::scriptAllocateMonsterDeathEffect;
    functions[853] = this::scriptGetBobjModelPartCount;
    functions[854] = SEffe::scriptGetAdditionOverlayActiveStatus;

    functions[896] = SEffe::scriptAllocateGradientRaysEffect;
    functions[897] = SEffe::scriptAllocateScreenCaptureEffect;

    functions[1000] = this::scriptHasStatMod;
    functions[1001] = this::scriptAddStatMod;
    functions[1002] = this::scriptRemoveStatMod;
    functions[1003] = this::scriptGetStatModType;
    functions[1004] = this::scriptUpdateStatModParams;
    functions[1005] = this::scriptGetStatModParams;

    functions[1010] = this::scriptUseItem;
    functions[1011] = this::scriptApplyEquipmentEffect;

    functions[1020] = this::scriptSetCombatantVramSlot;
    return functions;
  }

  @ScriptDescription("Check if a stat modifier is present on a battle entity stat")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id", description = "A unique identifier to assign to this stat mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentId", description = "The battle entity to apply the stat mod to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statType", description = "The stat type to mod")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "present", description = "True if present, false otherwise")
  private FlowControl scriptHasStatMod(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final BattleEntity27c bent = battleState_8006e398.allBents_e0c[script.params_20[1].get()].innerStruct_00;
    final StatType statType = REGISTRIES.statTypes.getEntry(script.params_20[2].getRegistryId()).get();

    script.params_20[3].set(bent.stats.getStat(statType).hasMod(id) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Add a stat modifier to a battle entity stat")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id", description = "A unique identifier to assign to this stat mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentId", description = "The battle entity to apply the stat mod to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statType", description = "The stat type to mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statModType", description = "The stat mod type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "statModParams", description = "The implementation-specific configuration for the stat mod type")
  private FlowControl scriptAddStatMod(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final BattleEntity27c bent = battleState_8006e398.allBents_e0c[script.params_20[1].get()].innerStruct_00;
    final StatType statType = REGISTRIES.statTypes.getEntry(script.params_20[2].getRegistryId()).get();
    final StatModType statModType = REGISTRIES.statModTypes.getEntry(script.params_20[3].getRegistryId()).get();
    final Param params = script.params_20[4];

    final StatModConfig config = statModType.makeConfig();
    statModType.readConfigFromScript(config, params);
    bent.stats.getStat(statType).addMod(id, statModType.make(config));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Remove a stat modifier from a battle entity stat")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id", description = "A unique identifier to assign to this stat mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentId", description = "The battle entity to apply the stat mod to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statType", description = "The stat type to mod")
  private FlowControl scriptRemoveStatMod(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final BattleEntity27c bent = battleState_8006e398.allBents_e0c[script.params_20[1].get()].innerStruct_00;
    final StatType statType = REGISTRIES.statTypes.getEntry(script.params_20[2].getRegistryId()).get();

    bent.stats.getStat(statType).removeMod(id);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a stat modifier's type from a battle entity stat")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id", description = "A unique identifier to assign to this stat mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentId", description = "The battle entity to apply the stat mod to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statType", description = "The stat type to mod")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "statModType", description = "The stat mod type")
  private FlowControl scriptGetStatModType(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final BattleEntity27c bent = battleState_8006e398.allBents_e0c[script.params_20[1].get()].innerStruct_00;
    final StatType statType = REGISTRIES.statTypes.getEntry(script.params_20[2].getRegistryId()).get();

    script.params_20[3].set(bent.stats.getStat(statType).getMod(id).getType().getRegistryId());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Update a stat modifier on a battle entity stat")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id", description = "A unique identifier to assign to this stat mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentId", description = "The battle entity to apply the stat mod to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statType", description = "The stat type to mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "statModParams", description = "The implementation-specific configuration for the stat mod type")
  private FlowControl scriptUpdateStatModParams(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final BattleEntity27c bent = battleState_8006e398.allBents_e0c[script.params_20[1].get()].innerStruct_00;
    final StatType statType = REGISTRIES.statTypes.getEntry(script.params_20[2].getRegistryId()).get();
    final Param params = script.params_20[3];

    final Stat stat = bent.stats.getStat(statType);
    final StatMod statMod = stat.getMod(id);
    final StatModType statModType = statMod.getType();

    final StatModConfig config = statModType.makeConfig();
    statModType.readConfigFromScript(config, params);
    statModType.update(statMod, config);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a stat modifier's params on a battle entity stat")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "id", description = "A unique identifier to assign to this stat mod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentId", description = "The battle entity to apply the stat mod to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "statType", description = "The stat type to mod")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT_ARRAY, name = "statModParams", description = "The implementation-specific configuration for the stat mod type")
  private FlowControl scriptGetStatModParams(final RunningScript<?> script) {
    final RegistryId id = script.params_20[0].getRegistryId();
    final BattleEntity27c bent = battleState_8006e398.allBents_e0c[script.params_20[1].get()].innerStruct_00;
    final StatType statType = REGISTRIES.statTypes.getEntry(script.params_20[2].getRegistryId()).get();
    final Param params = script.params_20[3];

    final Stat stat = bent.stats.getStat(statType);
    final StatMod statMod = stat.getMod(id);
    final StatModType statModType = statMod.getType();

    final StatModConfig config = statModType.makeConfig();
    statModType.writeConfigToScript(config, params);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Calls the use method for the given item")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "itemId", description = "The ID of the item being used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The ID of the bent being targeted")
  private FlowControl scriptUseItem(final RunningScript<BattleEntity27c> script) {
    final Item item = REGISTRIES.items.getEntry(script.params_20[0].getRegistryId()).get();
    final int targetIndex = script.params_20[1].get();

    return item.useInBattle(script.scriptState_04, targetIndex);
  }

  @ScriptDescription("Calls the applyEffect method for the given equipment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "equipmentId", description = "The ID of the equipment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "wearerBentIndex", description = "The ID of the bent wearing the equipment")
  private FlowControl scriptApplyEquipmentEffect(final RunningScript<BattleEntity27c> script) {
    final Equipment equipment = REGISTRIES.equipment.getEntry(script.params_20[0].getRegistryId()).get();
    final BattleEntity27c wearer = SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class);

    equipment.applyEffect(wearer);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Changes vram slot of a combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "Combatant ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "vramSlot", description = "Target vram slot")
  private FlowControl scriptSetCombatantVramSlot(final RunningScript<BattleEntity27c> script) {
    this.unsetMonsterTextureSlotUsed(this.combatants_8005e398[script.params_20[0].get()].vramSlot_1a0);
    this.combatants_8005e398[script.params_20[0].get()].vramSlot_1a0 = script.params_20[1].get();
    this.setMonsterTextureSlotUsed(this.combatants_8005e398[script.params_20[0].get()].vramSlot_1a0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80018744L)
  private void checkIfCharacterAndMonsterModelsAreLoadedAndCacheLivingBents() {
    if((battleFlags_800bc960 & 0x400) != 0) { // Encounter asset files have been requested
      if((battleFlags_800bc960 & 0x8) == 0 && battleState_8006e398.areCharacterModelsLoaded()) {
        battleFlags_800bc960 |= 0x8;
      }

      //LAB_80018790
      if((battleFlags_800bc960 & 0x4) == 0 && battleState_8006e398.areMonsterModelsLoaded()) {
        battleFlags_800bc960 |= 0x4;
      }
    }

    //LAB_800187b0
    battleState_8006e398.cacheLivingBents();
  }

  @Method(0x8001890cL)
  private void renderBattleEnvironment() {
    this.camera_800c67f0.updateBattleCamera();
    battleState_8006e398.cacheLivingBents();
    this.rotateAndRenderBattleStage();
    this.renderSkybox();
  }

  /**
   * @param type 1 - player, 2 - monster
   */
  @Method(0x80019e24L)
  private void playBentSound(final int type, final ScriptState<BattleEntity27c> state, final int soundIndex, final int a3, final int a4, final int initialDelay, final int repeatDelay) {
    final BattleEntity27c bent = state.innerStruct_00;

    int soundFileIndex = 0;
    if(type == 1) {
      //LAB_80019e68
      for(int charSlot = 0; charSlot < 3; charSlot++) {
        final int index = characterSoundFileIndices_800500f8[charSlot];
        if(soundFiles_800bcf80[index].id_02 == bent.charId_272) {
          //LAB_80019ea4
          soundFileIndex = index;
          break;
        }
      }
    } else {
      //LAB_80019f18
      //LAB_80019f30
      for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
        final int index = monsterSoundFileIndices_800500e8[monsterSlot];
        if(soundFiles_800bcf80[index].id_02 == bent.charId_272) {
          //LAB_80019ea4
          soundFileIndex = index;
          break;
        }

        if(monsterSlot == 3) {
          return;
        }
      }
    }

    //LAB_80019f70
    //LAB_80019f74
    //LAB_80019f7c
    //LAB_80019eac
    final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

    // Retail bug: one of the Divine Dragon Spirit's attack scripts tries to play soundIndex 10 but there are only 10 elements in the patch/sequence file (DRGN0.1225.1.1)
    if(soundIndex < soundFile.indices_08.length) {
      final QueuedSound28 queuedSound = new QueuedSound28();
      queuedSounds_800bd110.add(queuedSound);

      playSound(type, soundFile, soundIndex, queuedSound, soundFile.playableSound_10, soundFile.indices_08[soundIndex], 0, (short)-1, (short)-1, (short)-1, (short)repeatDelay, (short)initialDelay, bent);
    }

    //LAB_80019f9c
  }

  /** Same as playBentSound, but looks up bent by combatant index */
  @Method(0x80019facL)
  private void playCombatantSound(final int type, final int charOrMonsterIndex, final int soundIndex, final short initialDelay, final short repeatDelay) {
    int soundFileIndex = 0;
    final MonsterBattleEntity monster = battleState_8006e398.getMonsterById(charOrMonsterIndex);

    //LAB_8001a018
    if(type == 1) {
      //LAB_8001a034
      for(int charSlot = 0; charSlot < 3; charSlot++) {
        final int index = characterSoundFileIndices_800500f8[charSlot];

        if(soundFiles_800bcf80[index].id_02 == charOrMonsterIndex) {
          soundFileIndex = index;
          break;
        }
      }
    } else {
      //LAB_8001a0e4
      //LAB_8001a0f4
      for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
        final int index = monsterSoundFileIndices_800500e8[monsterSlot];

        if(soundFiles_800bcf80[index].id_02 == charOrMonsterIndex) {
          //LAB_8001a078
          soundFileIndex = index;
          break;
        }
      }
    }

    //LAB_8001a128
    //LAB_8001a12c
    //LAB_8001a134
    //LAB_8001a080
    final QueuedSound28 queuedSound = new QueuedSound28();
    queuedSounds_800bd110.add(queuedSound);

    final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];
    playSound(type, soundFile, soundIndex, queuedSound, soundFile.playableSound_10, soundFile.indices_08[soundIndex], 0, (short)-1, (short)-1, (short)-1, repeatDelay, initialDelay, monster);

    //LAB_8001a154
  }

  @Method(0x8001a164L)
  private void stopBentSound(final BattleEntity27c bent, final int soundIndex, final int mode) {
    //LAB_8001a1a8
    for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
      final SpuStruct08 s0 = _800bc9a8[sequenceIndex];

      if(s0.soundIndex_03 == soundIndex && s0.bent_04 == bent) {
        stopSoundSequence(sequenceData_800c4ac8[sequenceIndex], true);

        if((mode & 0x1) == 0) {
          break;
        }
      }

      //LAB_8001a1e0
    }

    //LAB_8001a1f4
    if((mode & 0x2) != 0) {
      //LAB_8001a208
      queuedSounds_800bd110.removeIf(queuedSound -> queuedSound.type_00 != 0 && (queuedSound.repeatDelayTotal_20 != 0 || queuedSound.initialDelay_24 != 0) && queuedSound.soundIndex_0c == soundIndex && queuedSound.bent_04 == bent);
    }

    //LAB_8001a270
  }

  @ScriptDescription("Play a battle entity sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "1 = player, 2 = monster")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "a3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "a4")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialDelay", description = "The initial delay before the sound starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "repeatDelay", description = "The delay before a sound repeats")
  @Method(0x8001abd0L)
  private FlowControl scriptPlayBentSound(final RunningScript<?> script) {
    this.playBentSound(script.params_20[0].get(), SCRIPTS.getState(script.params_20[1].get(), BattleEntity27c.class), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stop a battle entity sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x8001ac48L)
  private FlowControl scriptStopBentSound(final RunningScript<?> script) {
    this.stopBentSound(SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Plays a combatant sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "1 = player, 2 = monster")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charOrMonsterIndex", description = "The character or monster index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialDelay", description = "The initial delay before the sound starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "repeatDelay", description = "The delay before a sound repeats")
  @Method(0x8001ac88L)
  private FlowControl scriptPlayCombatantSound(final RunningScript<?> script) {
    this.playCombatantSound(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), (short)script.params_20[3].get(), (short)script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stop a battle entity sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x8001acd8L)
  private FlowControl scriptStopBentSound2(final RunningScript<?> script) {
    return this.scriptStopBentSound(script);
  }

  @Method(0x800c7524L)
  public void initBattle() {
    new Tim(Loader.loadFile("shadow.tim")).uploadToGpu();

    this.FUN_800c8624();

    gameState_800babc8._b4++;
    Arrays.fill(unlockedUltimateAddition_800bc910, false);
    goldGainedFromCombat_800bc920 = 0;

    spGained_800bc950[0] = 0;
    spGained_800bc950[1] = 0;
    spGained_800bc950[2] = 0;

    totalXpFromCombat_800bc95c = 0;
    battleFlags_800bc960 = 0;
    postBattleAction_800bc974 = 0;
    itemsDroppedByEnemies_800bc928.clear();
    itemOverflow.clear();
    equipmentOverflow.clear();

    int charIndex = gameState_800babc8.charIds_88[1];
    if(charIndex < 0) {
      gameState_800babc8.charIds_88[1] = gameState_800babc8.charIds_88[2];
      gameState_800babc8.charIds_88[2] = charIndex;
    }

    //LAB_800c75c0
    charIndex = gameState_800babc8.charIds_88[0];
    if(charIndex < 0) {
      gameState_800babc8.charIds_88[0] = gameState_800babc8.charIds_88[1];
      gameState_800babc8.charIds_88[1] = charIndex;
    }

    //LAB_800c75e8
    charIndex = gameState_800babc8.charIds_88[1];
    if(charIndex < 0) {
      gameState_800babc8.charIds_88[1] = gameState_800babc8.charIds_88[2];
      gameState_800babc8.charIds_88[2] = charIndex;
    }

    //LAB_800c760c
    this.allocateStageDarkeningStorage();
    loadEncounterSoundsAndMusic();

    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c7648L)
  public void loadStageAndControllerScripts() {
    this.loadStage(battleStage_800bb0f4);
    this.loadStageDataAndControllerScripts();
    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c76a0L)
  public void initializeViewportAndCamera() {
    if((battleFlags_800bc960 & 0x3) == 0x3) {
      resizeDisplay(320, 240);
      setDepthResolution(12);
      vsyncMode_8007a3b8 = 3;
      battleFlags_800bc960 |= 0x40;
      setProjectionPlaneDistance(320);
      this.camera_800c67f0.resetCameraMovement();
      pregameLoadingStage_800bb10c++;
    }

    //LAB_800c7718
  }

  @Method(0x800c772cL)
  public void battleInitiateAndPreload_800c772c() {
    this.FUN_800c8e48();

    battleLoaded_800bc94c = true;

    startFadeEffect(4, 30);

    battleFlags_800bc960 |= 0x20;
    battleState_8006e398.battlePhase_eec = 0;

    this.clearCombatants();
    this.clearCurrentDisplayableItems();

    battleState_8006e398.clear();

    loadAdditions();

    //LAB_800c7830
    for(int i = 0; i < 12; i++) {
      battleState_8006e398.allBents_e0c[i] = null;
    }

    this.initBattleMenu();
    this.allocateDeffManager();

    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c788cL)
  public void allocateEnemyBattleEntities() {
    final BattlePreloadedEntities_18cb0 fp = battlePreloadedEntities_1f8003f4;

    //LAB_801095a0
    for(int i = 0; i < 3; i++) {
      final int enemyIndex = fp.encounterData_00.enemyIndices_00[i] & 0x1ff;
      if(enemyIndex == 0x1ff) {
        break;
      }

      this.loadEnemyDropsAndScript((this.addCombatant(enemyIndex, -1) << 16) + enemyIndex);
    }

    //LAB_801095ec
    //LAB_801095fc
    for(int i = 0; i < 6; i++) {
      final EncounterData38.EnemyInfo08 s5 = fp.encounterData_00.enemyInfo_08[i];
      final int charIndex = s5.index_00 & 0x1ff;
      if(charIndex == 0x1ff) {
        break;
      }

      final int combatantIndex = this.getCombatantIndex(charIndex);
      final String name = "Enemy combatant index " + combatantIndex;
      final MonsterBattleEntity bent = new MonsterBattleEntity(name);
      final ScriptState<MonsterBattleEntity> state = SCRIPTS.allocateScriptState(name, bent);
      state.setTicker(bent::bentLoadingTicker);
      state.setDestructor(bent::bentDestructor);
      bent.charId_272 = charIndex;
      bent.combatant_144 = this.getCombatant(combatantIndex);
      bent.combatantIndex_26c = combatantIndex;
      bent.model_148.coord2_14.coord.transfer.set(s5.pos_02);
      bent.model_148.coord2_14.transforms.rotate.set(0.0f, MathHelper.TWO_PI * 0.75f, 0.0f);
      state.storage_44[7] |= 0x4;
      battleState_8006e398.addMonster(state);
      this.loadMonster(state);
    }

    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c78d4L)
  public void allocatePlayerBattleEntities() {
    //LAB_800fbdb8
    int charCount;
    for(charCount = 0; charCount < 3; charCount++) {
      if(gameState_800babc8.charIds_88[charCount] < 0) {
        break;
      }
    }

    //LAB_800fbde8
    final int[] combatantIndices = new int[charCount];

    //LAB_800fbe18
    for(int charSlot = 0; charSlot < charCount; charSlot++) {
      combatantIndices[charSlot] = this.addCombatant(0x200 + gameState_800babc8.charIds_88[charSlot] * 2, charSlot);
    }

    //LAB_800fbe4c
    //LAB_800fbe70
    for(int charSlot = 0; charSlot < charCount; charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];
      final String name = "Char ID " + charIndex + " (bent + " + (charSlot + 6) + ')';
      final PlayerBattleEntity bent = new PlayerBattleEntity(name, charSlot + 6, this.playerBattleScript_800c66fc);
      final ScriptState<PlayerBattleEntity> state = SCRIPTS.allocateScriptState(charSlot + 6, name, bent);
      state.setTicker(bent::bentLoadingTicker);
      state.setDestructor(bent::bentDestructor);
      bent.element = characterElements_800c706c[charIndex].get();
      bent.combatant_144 = this.getCombatant((short)combatantIndices[charSlot]);
      bent.charId_272 = charIndex;
      bent.combatantIndex_26c = combatantIndices[charSlot];
      bent.model_148.coord2_14.coord.transfer.x = charCount > 2 && charSlot == 0 ? 0x900 : 0xa00;
      bent.model_148.coord2_14.coord.transfer.y = 0.0f;
      // Alternates placing characters to the right and left of the main character (offsets by -0x400 for even character counts)
      bent.model_148.coord2_14.coord.transfer.z = 0x800 * ((charSlot + 1) / 2) * (charSlot % 2 * 2 - 1) + (charCount % 2 - 1) * 0x400;
      bent.model_148.coord2_14.transforms.rotate.zero();
      battleState_8006e398.addPlayer(state);
    }

    this.initPlayerBattleEntityStats();

    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c791cL)
  public void loadEncounterAssets() {
    this.loadEnemyTextures();

    // Count total monsters
    loadingMonsterModels.set(0);
    for(int i = 0; i < this.combatantCount_800c66a0; i++) {
      final CombatantStruct1a8 combatant = this.getCombatant(i);
      if(combatant.charSlot_19c < 0) { // Monster
        loadingMonsterModels.incrementAndGet();
      }

      //LAB_800fc050
    }

    //LAB_800fc030
    for(int i = 0; i < this.combatantCount_800c66a0; i++) {
      final CombatantStruct1a8 combatant = this.getCombatant(i);
      if(combatant.charSlot_19c < 0) { // Monster
        this.loadCombatantTmdAndAnims(combatant);
      }

      //LAB_800fc050
    }

    //LAB_800fc064
    //LAB_800fc09c
    for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
      battleState_8006e398.playerBents_e40[i].innerStruct_00.combatant_144.flags_19e |= 0x2a;
    }

    //LAB_800fc104
    this.loadPartyTims();
    this.loadPartyTmdAndAnims();
    battleFlags_800bc960 |= 0x400;

    pregameLoadingStage_800bb10c++;
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc210L)
  public void loadCharTmdAndAnims(final List<FileData> files, final int charSlot) {
    //LAB_800fc260
    final BattleEntity27c data = battleState_8006e398.playerBents_e40[charSlot].innerStruct_00;

    //LAB_800fc298
    this.combatantTmdAndAnimLoadedCallback(files, data.combatant_144, false);

    //LAB_800fc34c
    battleFlags_800bc960 |= 0x4;
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc3c0L)
  public void loadEnemyTextures() {
    for(int i = 0; i < this.combatantCount_800c66a0; i++) {
      final CombatantStruct1a8 a0 = this.getCombatant(i);

      if(a0.charSlot_19c >= 0) {
        continue;
      }

      final int enemyIndex = a0.charIndex_1a2 & 0x1ff;

      if(Loader.exists("monsters/%d/textures/combat".formatted(enemyIndex))) {
        loadFile("monsters/%d/textures/combat".formatted(enemyIndex), files -> this.loadCombatantTim(a0, files));
      }
    }
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc504L)
  public void loadPartyTims() {
    for(int charSlot = 0; charSlot < battleState_8006e398.getPlayerCount(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadFile("characters/%s/textures/combat".formatted(name), files -> this.loadCharacterTim(files, finalCharSlot));
    }
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc548L)
  public void loadCharacterTim(final FileData file, final int charSlot) {
    final BattleEntity27c bent = battleState_8006e398.playerBents_e40[charSlot].innerStruct_00;
    this.loadCombatantTim(bent.combatant_144, file);
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc654L)
  public void loadPartyTmdAndAnims() {
    for(int charSlot = 0; charSlot < battleState_8006e398.getPlayerCount(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadDir("characters/%s/models/combat".formatted(name), files -> this.loadCharTmdAndAnims(files, finalCharSlot));
    }
  }

  @Method(0x800c7964L)
  public void loadHudAndAttackAnimations() {
    battleFlags_800bc960 |= 0xc;

    this.loadBattleHudTextures();
    this.loadBattleHudDeff();

    //LAB_800c79a8
    for(int combatantIndex = 0; combatantIndex < this.combatantCount_800c66a0; combatantIndex++) {
      this.loadAttackAnimations(this.combatants_8005e398[combatantIndex]);
    }

    //LAB_800c79c8
    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c79f0L)
  public void FUN_800c79f0() {
    this.currentTurnBent_800c66c8 = battleState_8006e398.allBents_e0c[0];
    this.hud.FUN_800f417c();

    EVENTS.postEvent(new BattleStartedEvent());

    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c7a30L)
  public void loadSEffe() {
    pregameLoadingStage_800bb10c++;
  }

  @Method(0x800c7a80L)
  public void calculateInitialTurnValues() {
    if(this.battleInitialCameraMovementFinished_800c66a8) {
      battleFlags_800bc960 |= 0x10;
      battleState_8006e398.calculateInitialTurnValues();
      pregameLoadingStage_800bb10c++;
    }
  }

  @Method(0x800c7bb8L)
  public void battleTick() {
    this.hud.draw();

    if(postBattleAction_800bc974 != 0) {
      pregameLoadingStage_800bb10c++;
      return;
    }

    if(Loader.getLoadingFileCount() == 0 && battleState_8006e398.hasBents() && !this.combatDisabled_800c66b9 && this.FUN_800c7da8()) {
      vsyncMode_8007a3b8 = 3;
      this.mcqColour_800fa6dc = 0x80;
      this.currentTurnBent_800c66c8.storage_44[7] &= 0xffff_efff;

      if(battleState_8006e398.hasAlivePlayers()) {
        //LAB_800c7c98
        this.forcedTurnBent_800c66bc = battleState_8006e398.getForcedTurnBent();

        if(this.forcedTurnBent_800c66bc != null) { // A bent has a forced turn
          this.forcedTurnBent_800c66bc.storage_44[7] = this.forcedTurnBent_800c66bc.storage_44[7] & 0xffff_ffdf | 0x1008;
          this.currentTurnBent_800c66c8 = this.forcedTurnBent_800c66bc;
          EVENTS.postEvent(new BattleEntityTurnEvent<>(this.forcedTurnBent_800c66bc));
        } else { // Take regular turns
          //LAB_800c7ce8
          if(battleState_8006e398.hasAliveMonsters()) { // Monsters alive, calculate next bent turn
            //LAB_800c7d3c
            this.currentTurnBent_800c66c8 = battleState_8006e398.getCurrentTurnBent();
            this.currentTurnBent_800c66c8.storage_44[7] |= 0x1008;
            EVENTS.postEvent(new BattleEntityTurnEvent<>(this.currentTurnBent_800c66c8));

            //LAB_800c7d74
          } else { // Monsters dead
            this.endBattle();
          }
        }
      } else { // Game over
        loadMusicPackage(19);
        postBattleAction_800bc974 = 2;
      }
    }

    //LAB_800c7d78
    if(postBattleAction_800bc974 != 0) {
      //LAB_800c7d88
      pregameLoadingStage_800bb10c++;
    }

    //LAB_800c7d98
  }

  public void endBattle() {
    FUN_80020308();

    if(encounterId_800bb0f8 != 443) { // Standard victory
      postBattleAction_800bc974 = 1;
      startEncounterSounds();
    } else { // Melbu Victory
      //LAB_800c7d30
      postBattleAction_800bc974 = 4;
    }
  }

  @Method(0x800c7da8L)
  public boolean FUN_800c7da8() {
    //LAB_800c7dd8
    for(int i = 0; i < battleState_8006e398.getAllBentCount(); i++) {
      if((battleState_8006e398.allBents_e0c[i].storage_44[7] & 0x408) != 0) {
        return false;
      }

      //LAB_800c7e10
    }

    //LAB_800c7e1c
    return true;
  }

  @Method(0x800c8068L)
  public void performPostBattleAction() {
    EVENTS.postEvent(new BattleEndedEvent());

    final int postBattleAction = postBattleAction_800bc974;

    if(this.currentPostCombatActionFrame_800c6690 == 0) {
      final int postBattleCamera = postBattleCamera_800fa6c4[postBattleAction];

      if(postBattleCamera >= 0) {
        this.cameraScriptMainTableJumpIndex_800c6748 = postBattleCamera;
        this.scriptState_800c6914 = this.currentTurnBent_800c66c8;
      }

      //LAB_800c80c8
      final int aliveCharBents = battleState_8006e398.getAlivePlayerCount();
      livingCharCount_800bc97c = aliveCharBents;

      //LAB_800c8104
      for(int i = 0; i < aliveCharBents; i++) {
        livingCharIds_800bc968[i] = battleState_8006e398.alivePlayerBents_eac[i].innerStruct_00.charId_272;
      }

      //LAB_800c8144
      if(postBattleAction == 1) {
        //LAB_800c8180
        for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
          battleState_8006e398.playerBents_e40[i].storage_44[7] |= 0x8;
        }
      }
    }

    //LAB_800c81bc
    //LAB_800c81c0
    this.currentPostCombatActionFrame_800c6690++;

    if(this.currentPostCombatActionFrame_800c6690 >= postCombatActionTotalFrames_800fa6b8[postBattleAction] || (press_800bee94 & 0xff) != 0 && this.currentPostCombatActionFrame_800c6690 >= 25) {
      //LAB_800c8214
      this.deallocateLightingControllerAndDeffManager();

      if(fullScreenEffect_800bb140.currentColour_28 == 0) {
        startFadeEffect(1, postCombatActionFrames_800fa6d0[postBattleAction]);
      }

      //LAB_800c8274
      if(postBattleAction == 2) {
        sssqFadeOut((short)(postCombatActionFrames_800fa6d0[2] - 2));
      }

      //LAB_800c8290
      this.currentPostCombatActionFrame_800c6690 = 0;
      pregameLoadingStage_800bb10c++;
    }
    //LAB_800c82a8
  }

  @Method(0x800c82b8L)
  public void deallocateCombat() {
    if(fullScreenEffect_800bb140.currentColour_28 == 0xff) {
      this.updateGameStateAndDeallocateMenu();
      this.setStageHasNoModel();

      if(battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00 != null) {
        for(int i = 0; i < battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00.length; i++) {
          if(battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[i].obj != null) {
            battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[i].obj.delete();
            battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[i].obj = null;
          }
        }

        battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00 = null;
      }

      if(battlePreloadedEntities_1f8003f4.skyboxObj != null) {
        battlePreloadedEntities_1f8003f4.skyboxObj.delete();
        battlePreloadedEntities_1f8003f4.skyboxObj = null;
      }

      this.playerBattleScript_800c66fc = null;

      //LAB_800c8314
      scriptDeallocateAllTextboxes(null);
      this.scriptState_800c674c.deallocateWithChildren();

      //LAB_800c8368
      //LAB_800c8394
      stopAndResetSoundsAndSequences();

      //LAB_800c83b8
      battleState_8006e398.deallocateBents();

      //LAB_800c83d8
      //LAB_800c83f4
      for(int combatantIndex = 0; combatantIndex < this.combatantCount_800c66a0; combatantIndex++) {
        final CombatantStruct1a8 combatant = this.combatants_8005e398[combatantIndex];

        if(combatant != null) {
          //LAB_800c8418
          //LAB_800c8434
          if(combatant.mrg_00 != null) {
            combatant.mrg_00 = null;
          }

          if(combatant.mrg_04 != null) {
            combatant.mrg_04 = null;
          }
        }

        //LAB_800c8454
      }

      //LAB_800c847c
      battleState_8006e398.deallocateLoadedGlobalAssets();
      this.deallocateStageDarkeningStorage();
      this.FUN_800c8748();

      EngineStateEnum postCombatMainCallbackIndex = previousEngineState_8004dd28;
      if(postCombatMainCallbackIndex == EngineStateEnum.FMV_09) {
        postCombatMainCallbackIndex = EngineStateEnum.SUBMAP_05;
      }

      //LAB_800c84b4
      switch(postBattleAction_800bc974) {
        case 2 -> {
          if(encounterId_800bb0f8 == 391 || encounterId_800bb0f8 >= 404 && encounterId_800bb0f8 < 408) { // Arena fights in Lohan
            //LAB_800c8514
            gameState_800babc8.scriptFlags2_bc.set(29, 27, true); // Died in arena fight
          } else {
            //LAB_800c8534
            postCombatMainCallbackIndex = EngineStateEnum.GAME_OVER_07;
          }
        }

        case 4 -> Fmv.playCurrentFmv(16, EngineStateEnum.FINAL_FMV_11);
      }

      //LAB_800c8558
      postCombatMainCallbackIndex_800bc91c = postCombatMainCallbackIndex;

      final int postCombatSubmapScene = this.currentStageData_800c6718.postCombatSubmapScene_0c;
      if(postCombatSubmapScene != 0xff) {
        submapScene_80052c34 = postCombatSubmapScene;
      }

      //LAB_800c8578
      final int postCombatSubmapCut = this.currentStageData_800c6718.postCombatSubmapCut_28;
      if(postCombatSubmapCut != 0xffff) {
        submapCut_80052c30 = postCombatSubmapCut;
      }

      //LAB_800c8590
      setDepthResolution(14);
      battleLoaded_800bc94c = false;

      switch(postBattleAction_800bc974) {
        case 1, 3 -> {
          whichMenu_800bdc38 = WhichMenu.RENDER_NEW_MENU;
          menuStack.pushScreen(new PostBattleScreen());
        }
        case 2, 4, 5 -> whichMenu_800bdc38 = WhichMenu.NONE_0;
      }

      //LAB_800c85f0
      pregameLoadingStage_800bb10c++;
    }

    //LAB_800c8604
  }

  @Method(0x800c8624L)
  public void FUN_800c8624() {
    battlePreloadedEntities_1f8003f4 = new BattlePreloadedEntities_18cb0();
    battleState_8006e398 = new BattleStateEf4();
    this.targetBents_800c71f0 = new ScriptState[][] {battleState_8006e398.playerBents_e40, battleState_8006e398.aliveMonsterBents_ebc, battleState_8006e398.aliveBents_e78};
  }

  @Method(0x800c8748L)
  public void FUN_800c8748() {
    battlePreloadedEntities_1f8003f4 = null;
    battleState_8006e398 = null;
    this.targetBents_800c71f0 = null;
  }

  @Method(0x800c8774L)
  public void loadStageTmdAndAnim(final String modelName, final List<FileData> files) {
    LOGGER.info("Battle stage %s loaded", modelName);

    this.setStageHasNoModel();

    if(files.get(0).size() > 0 && files.get(1).size() > 0 && files.get(2).size() > 0) {
      final BattleStage stage = battlePreloadedEntities_1f8003f4.stage_963c;
      stage.name = modelName;
      this.loadStageTmd(stage, new CContainer(modelName, files.get(0), 10), new TmdAnimationFile(files.get(1)));
      stage.coord2_558.coord.transfer.set(0, 0, 0);
      stage.param_5a8.rotate.set(0.0f, MathHelper.TWO_PI / 4.0f, 0.0f);

      this.shouldRenderStage_800c6754 = true;
      this.stageHasModel_800c66b8 = true;
    }

    //LAB_800c8818
  }

  @Method(0x800c882cL)
  public void renderSkybox() {
    if(!this.shouldRenderMcq_800c6764 || !this.shouldRenderMcq_800c66d4 || (battleFlags_800bc960 & 0x80) == 0) {
      //LAB_800c8ad8
      //LAB_800c8adc
      clearBlue_800babc0 = 0;
      clearGreen_800bb104 = 0;
      clearRed_8007a3a8 = 0;
    } else {
      final McqHeader mcq = battlePreloadedEntities_1f8003f4.stageMcq_9cb0;

      if(mcq.screenWidth_14 < 16 || mcq.screenHeight_16 < 16) {
        return;
      }

      if(battlePreloadedEntities_1f8003f4.skyboxObj == null) {
        battlePreloadedEntities_1f8003f4.skyboxObj = new McqBuilder("Battle Skybox", mcq)
          .vramOffset(320, 0)
          .build();
      }

      this.mcqOffsetX_800c6774 += this.mcqStepX_800c676c;
      this.mcqOffsetY_800c6778 += this.mcqStepY_800c6770;
      final int x0 = (this.mcqBaseOffsetX_800c66cc * MathHelper.radToPsxDeg(this.camera_800c67f0.calculateXAngleFromRefpointToViewpoint()) / 0x1000 + this.mcqOffsetX_800c6774) % mcq.screenWidth_14;
      int y = this.mcqOffsetY_800c6778 - MathHelper.radToPsxDeg(MathHelper.floorMod(this.camera_800c67f0.calculateYAngleFromRefpointToViewpoint() + MathHelper.PI, MathHelper.TWO_PI)) + 1888;

      final float totalWidth = RENDERER.getProjectionWidth() * RENDERER.getRenderAspectRatio() / RENDERER.getNativeAspectRatio();
      final int segments = (int)Math.ceil(totalWidth / mcq.screenWidth_14);

      for(int i = -1; i < segments + 1; i++) {
        battlePreloadedEntities_1f8003f4.skyboxTransforms.transfer.set(-totalWidth / 2.0f + i * mcq.screenWidth_14 + x0, y, 60000.0f);
        RENDERER.queueOrthoModel(battlePreloadedEntities_1f8003f4.skyboxObj, battlePreloadedEntities_1f8003f4.skyboxTransforms, QueuedModelStandard.class)
          .monochrome(this.mcqColour_800fa6dc / 128.0f);
      }

      //LAB_800c89d4
      if(mcq.magic_00 != McqHeader.MAGIC_1) {
        //LAB_800c89f8
        y += mcq.screenOffsetY_2a;
      }

      //LAB_800c8a04
      final int colour = this.mcqColour_800fa6dc;
      if(y >= -centreScreenY_1f8003de) {
        clearRed_8007a3a8 = mcq.colour0_18.x * colour / 0x80;
        clearGreen_800bb104 = mcq.colour0_18.y * colour / 0x80;
        clearBlue_800babc0 = mcq.colour0_18.z * colour / 0x80;
      } else {
        //LAB_800c8a74
        clearRed_8007a3a8 = mcq.colour1_20.x * colour / 0x80;
        clearGreen_800bb104 = mcq.colour1_20.y * colour / 0x80;
        clearBlue_800babc0 = mcq.colour1_20.z * colour / 0x80;
      }
    }

    //LAB_800c8af0
  }

  @Method(0x800c8b20L)
  public void loadStage(final int stage) {
    LOGGER.info("Loading battle stage %d", stage);

    if(battlePreloadedEntities_1f8003f4.skyboxObj != null) {
      battlePreloadedEntities_1f8003f4.skyboxObj.delete();
      battlePreloadedEntities_1f8003f4.skyboxObj = null;
    }

    // GH#1931
    // Disable texture animations so we don't corrupt the texture of the loading stage due to the old stage model still being loaded...
    if(stage_800bda0c != null) {
      for(int i = 0; i < 10; i++) {
        stage_800bda0c._618[i] = 0;
      }
    }

    // ... and defer loading to the next frame so that any texture animations currently in the pipeline finish
    RENDERER.addTask(() -> {
      loadDrgnDir(0, 2497 + stage, files -> {
        if(files.get(1).hasVirtualSize()) {
          this.loadStageMcq(new McqHeader(files.get(1)));
        }

        if(files.get(2).size() != 0) {
          this.loadStageTim(files.get(2));
        }
      });

      loadDrgnDir(0, (2497 + stage) + "/0", files -> this.loadStageTmdAndAnim("DRGN0/" + (2497 + stage) + "/0", files));
    });

    this.currentStage_800c66a4 = stage;
  }

  @Method(0x800c8c84L)
  public void loadStageTim(final FileData data) {
    LOGGER.info("Battle stage texture loaded");

    final Tim tim = new Tim(data);

    GPU.uploadData15(tim.getImageRect(), tim.getImageData());

    if(tim.hasClut()) {
      GPU.uploadData15(tim.getClutRect(), tim.getClutData());
    }

    //LAB_800c8ccc
    this.backupStageClut(data);
  }

  @Method(0x800c8ce4L)
  public void setStageHasNoModel() {
    this.stageHasModel_800c66b8 = false;
  }

  @Method(0x800c8cf0L)
  public void rotateAndRenderBattleStage() {
    if(this.stageHasModel_800c66b8 && this.shouldRenderStage_800c6754 && (battleFlags_800bc960 & 0x20) != 0) {
      this.rotateBattleStage(battlePreloadedEntities_1f8003f4.stage_963c);
      this.renderBattleStage(battlePreloadedEntities_1f8003f4.stage_963c);
    }

    //LAB_800c8d50
  }

  @Method(0x800c8d64L)
  public void loadStageMcq(final McqHeader mcq) {
    LOGGER.info("Battle stage skybox loaded");

    final int x;
    if((battleFlags_800bc960 & 0x80) != 0) {
      x = 320;
      this.shouldRenderMcq_800c6764 = true;
    } else {
      //LAB_800c8d98
      x = 512;
    }

    //LAB_800c8d9c
    loadMcq(mcq, x, 0);

    //LAB_800c8dc0
    battlePreloadedEntities_1f8003f4.stageMcq_9cb0 = mcq;

    this.shouldRenderMcq_800c66d4 = true;
    this.mcqBaseOffsetX_800c66cc = (0x400 / mcq.screenWidth_14 + 1) * mcq.screenWidth_14;
  }

  @Method(0x800c8e48L)
  public void FUN_800c8e48() {
    if(this.shouldRenderMcq_800c66d4 && (battleFlags_800bc960 & 0x80) == 0) {
      GPU.copyVramToVram(512, 0, 320, 0, battlePreloadedEntities_1f8003f4.stageMcq_9cb0.vramWidth_08, 256);
      this.shouldRenderMcq_800c6764 = true;
      battleFlags_800bc960 |= 0x80;
    }
    //LAB_800c8ec8
  }

  @Method(0x800c8ed8L)
  public void setDontRenderStageBackground() {
    this.shouldRenderMcq_800c66d4 = false;
  }

  @Method(0x800c8ee4L)
  public void clearCombatants() {
    //LAB_800c8ef4
    //NOTE: zeroes 0x50 bytes after this array of structs ends
    Arrays.fill(this.combatants_8005e398, null);
  }

  @Method(0x800c8f24L)
  public CombatantStruct1a8 getCombatant(final int index) {
    return this.combatants_8005e398[index];
  }

  @Method(0x800c8f50L)
  public int addCombatant(final int a0, final int charSlot) {
    //LAB_800c8f6c
    for(int combatantIndex = 0; combatantIndex < 10; combatantIndex++) {
      if(this.combatants_8005e398[combatantIndex] == null) {
        final CombatantStruct1a8 combatant = new CombatantStruct1a8();
        this.combatants_8005e398[combatantIndex] = combatant;

        if(charSlot < 0) {
          combatant.flags_19e = 0x1;
          combatant.vramSlot_1a0 = this.findFreeMonsterTextureSlot(a0);
        } else {
          //LAB_800c8f90
          combatant.flags_19e = 0x5;
          combatant.vramSlot_1a0 = charSlot + 1;
        }

        //LAB_800c8f94
        combatant.charSlot_19c = charSlot;
        combatant.charIndex_1a2 = a0;
        combatant._1a4 = -1;
        combatant._1a6 = -1;
        this.combatantCount_800c66a0++;
        return combatantIndex;
      }
    }

    return -1;
  }

  @Method(0x800c8fd4L)
  public void removeCombatant(final int combatantIndex) {
    final CombatantStruct1a8 combatant = this.combatants_8005e398[combatantIndex];

    if(combatant.vramSlot_1a0 != 0) {
      this.unsetMonsterTextureSlotUsed(combatant.vramSlot_1a0);
    }

    //LAB_800c9020
    //LAB_800c902c
    this.combatants_8005e398[combatantIndex] = null;

    this.combatantCount_800c66a0--;
  }

  @Method(0x800c9060L)
  public int getCombatantIndex(final int charIndex) {
    //LAB_800c906c
    for(int i = 0; i < 10; i++) {
      final CombatantStruct1a8 combatant = this.combatants_8005e398[i];

      if(combatant != null && combatant.charIndex_1a2 == charIndex) {
        //LAB_800c90a8
        return i;
      }

      //LAB_800c9090
    }

    return -1;
  }

  @Method(0x800c9170L)
  public void deallocateCombatant(final CombatantStruct1a8 combatant) {
    //LAB_800c91bc
    if(combatant.mrg_00 != null) {
      combatant.mrg_00 = null;
    }

    if(combatant.mrg_04 != null) {
      //LAB_800c91e8
      combatant.mrg_04 = null;
    }

    if(combatant._1a4 >= 0) {
      battleState_8006e398.deallocateGlobalAsset(combatant._1a4);
    }

    //LAB_800c921c
    if(combatant._1a6 >= 0) {
      battleState_8006e398.deallocateGlobalAsset(combatant._1a6);
    }

    //LAB_800c9234
    //LAB_800c9238
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] != null && combatant.assets_14[i]._09 != 0) {
        this.deallocateCombatantAnimation(combatant, i);
      }

      //LAB_800c9254
    }

    combatant.flags_19e &= 0xffe7;
  }

  @Method(0x800c9290L)
  public void loadCombatantTmdAndAnims(final CombatantStruct1a8 combatant) {
    if(combatant.charIndex_1a2 >= 0) {
      if((combatant.flags_19e & 0x8) == 0) {
        if(combatant.mrg_00 == null) {
          combatant.flags_19e |= 0x28;

          if((combatant.flags_19e & 0x4) == 0) {
            // Enemy TMDs
            final int fileIndex = 3137 + combatant.charIndex_1a2;
            loadDrgnDir(0, fileIndex, files -> this.combatantTmdAndAnimLoadedCallback(files, combatant, true));
          } else {
            // Player TMDs
            //LAB_800c9334
            int charIndex = gameState_800babc8.charIds_88[combatant.charSlot_19c];
            combatant.flags_19e |= 0x2;

            if((combatant.charIndex_1a2 & 0x1) != 0) {
              if(charIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
                charIndex = 10; // Divine dragoon
              }

              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/dragoon".formatted(charName), files -> this.combatantTmdAndAnimLoadedCallback(files, combatant, false));
            } else {
              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/combat".formatted(charName), files -> this.combatantTmdAndAnimLoadedCallback(files, combatant, false));
            }
          }
        }
      }
    }

    //LAB_800c940c
  }

  @Method(0x800c941cL)
  public void combatantTmdAndAnimLoadedCallback(final List<FileData> files, final CombatantStruct1a8 combatant, final boolean isMonster) {
    if(!isMonster) {
      battleFlags_800bc960 |= 0x4;
    }

    //LAB_800c947c
    combatant.mrg_00 = files;

    // I don't think this is actually used?
    if(files.get(34).hasVirtualSize()) {
      combatant.scriptPtr_10 = new ScriptFile("%s %d file 34".formatted(isMonster ? "monster" : "char", combatant.charSlot_19c), files.get(34).getBytes());
    }

    //LAB_800c94a0
    //LAB_800c94a4
    for(int animIndex = 0; animIndex < 32; animIndex++) {
      if(files.get(animIndex).hasVirtualSize()) {
        this.FUN_800c9a80(files.get(animIndex), 1, 0, combatant, animIndex);
      }

      //LAB_800c94cc
    }

    combatant.flags_19e &= 0xffdf;

    if(isMonster) {
      loadingMonsterModels.decrementAndGet();
    }
  }

  @Method(0x800c952cL)
  public static void loadCombatantModelAndAnimation(final Model124 model, final CombatantStruct1a8 combatant) {
    final CContainer tmd;
    if(combatant._1a4 >= 0) {
      tmd = new CContainer(model.name, battleState_8006e398.getGlobalAsset(combatant._1a4).data_00);
      //LAB_800c9590
    } else if(combatant.mrg_00 != null && combatant.mrg_00.get(32).hasVirtualSize()) {
      tmd = new CContainer(model.name, combatant.mrg_00.get(32));
    } else {
      throw new RuntimeException("Invalid state");
    }

    //LAB_800c95bc
    combatant.tmd_08 = tmd;

    final TmdAnimationFile anim = battleState_8006e398.getAnimationGlobalAsset(combatant, 0);
    if((combatant.flags_19e & 0x4) != 0) {
      final BattlePreloadedEntities_18cb0.Rendering1298 a0_0 = battlePreloadedEntities_1f8003f4._9ce8[combatant.charSlot_19c];

      a0_0.dobj2s_00 = new ModelPart10[tmd.tmdPtr_00.tmd.header.nobj];
      Arrays.setAll(a0_0.dobj2s_00, i -> new ModelPart10());

      model.modelParts_00 = a0_0.dobj2s_00;

      final int shadowSizeIndex;
      if((combatant.charIndex_1a2 & 0x1) != 0) {
        shadowSizeIndex = 9;
      } else {
        shadowSizeIndex = combatant.charIndex_1a2 - 0x200 >>> 1;
      }

      //LAB_800c9650
      loadPlayerModelAndAnimation(model, tmd, anim, shadowSizeIndex);
    } else {
      //LAB_800c9664
      initModel(model, tmd, anim);
    }

    TmdObjLoader.fromModel("CombatantModel (index " + combatant.charSlot_19c + ')', model);

    //LAB_800c9680
    combatant.assets_14[0]._09++;
  }

  @Method(0x800c9708L)
  public void loadAttackAnimations(final CombatantStruct1a8 combatant) {
    if(combatant.charIndex_1a2 >= 0 && combatant.mrg_04 == null) {
      combatant.flags_19e |= 0x10;

      final int fileIndex;
      if((combatant.flags_19e & 0x4) == 0) {
        // Enemy attack animations
        fileIndex = 3593 + combatant.charIndex_1a2;
        loadDrgnDir(0, fileIndex, files -> this.attackAnimationsLoaded(files, combatant, true, -1));
      } else {
        //LAB_800c97a4
        final int isDragoon = combatant.charIndex_1a2 & 0x1;
        final int charId = gameState_800babc8.charIds_88[combatant.charSlot_19c];
        if(isDragoon == 0) {
          // Additions
          if(charId != 2 && charId != 8) {
            fileIndex = 4031 + gameState_800babc8.charData_32c[charId].selectedAddition_19 + charId * 8 - additionOffsets_8004f5ac[charId];
          } else {
            // Retail fix: Shana/??? have selectedAddition 255 which loads a random file... just load Dart's first addition here, it isn't used (see GH#357)
            fileIndex = 4031 + charId * 8;
          }
        } else if(charId != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          // Dragoon addition
          fileIndex = 4103 + charId;
        } else { // Divine dragoon
          // Divine dragoon addition
          fileIndex = 4112;
        }

        loadDrgnDir(0, fileIndex, files -> this.attackAnimationsLoaded(files, combatant, false, combatant.charSlot_19c));
      }
    }

    //LAB_800c9888
  }

  @Method(0x800c9898L)
  public void attackAnimationsLoaded(final List<FileData> files, final CombatantStruct1a8 combatant, final boolean isMonster, final int charSlot) {
    if(combatant.mrg_04 == null) {
      //LAB_800c9910
      if(!isMonster && files.size() == 64) {
        //LAB_800c9940
        for(int animIndex = 0; animIndex < 32; animIndex++) {
          if(files.get(32 + animIndex).hasVirtualSize()) {
            if(combatant.assets_14[animIndex] != null && combatant.assets_14[animIndex]._09 != 0) {
              this.deallocateCombatantAnimation(combatant, animIndex);
            }

            //LAB_800c9974
            // Type 6 - TIM file (used to load animation data into VRAM)
            this.FUN_800c9a80(files.get(32 + animIndex), 6, charSlot, combatant, animIndex);
          }
        }
      }

      //LAB_800c99d8
      combatant.mrg_04 = files;

      //LAB_800c99e8
      for(int animIndex = 0; animIndex < 32; animIndex++) {
        if(files.get(animIndex).hasVirtualSize()) {
          if(combatant.assets_14[animIndex] != null && combatant.assets_14[animIndex]._09 != 0) {
            this.deallocateCombatantAnimation(combatant, animIndex);
          }

          //LAB_800c9a18
          this.FUN_800c9a80(files.get(animIndex), 2, 1, combatant, animIndex);
        }

        //LAB_800c9a34
      }
    }

    //LAB_800c9a48
  }

  /**
   * @param type <ol>
   *               <li>Animation</li>
   *               <li>Animation</li>
   *               <li>Index</li>
   *               <li value="6">TIM</li>
   *             </ol>
   */
  @Method(0x800c9a80L)
  public void FUN_800c9a80(final FileData data, final int type, final int a3, final CombatantStruct1a8 combatant, final int animIndex) {
    CombatantAsset0c s3 = combatant.assets_14[animIndex];

    if(s3 != null) {
      this.deallocateCombatantAnimation(combatant, animIndex);
    }

    //LAB_800c9b28
    if(type == 1) {
      //LAB_800c9b68
      if(data.readInt(0x4) == 0x1a45_5042) { // BPE
        final CombatantAsset0c.BpeType bpe = new CombatantAsset0c.BpeType(data);
        bpe._08 = a3;
        bpe.type_0a = 4;
        bpe.isLoaded_0b = false;
        s3 = bpe;
      } else {
        final CombatantAsset0c.AnimType anim = new CombatantAsset0c.AnimType(new TmdAnimationFile(data));
        anim._08 = a3;
        anim.type_0a = 1;
        anim.isLoaded_0b = false;
        s3 = anim;
      }
    } else if(type == 2) {
      //LAB_800c9b80
      //LAB_800c9b98
      if(data.readInt(0x4) == 0x1a45_5042) { // BPE
        //LAB_800c9b88
        final CombatantAsset0c.BpeType bpe = new CombatantAsset0c.BpeType(data);
        bpe._08 = a3;
        bpe.type_0a = 5;
        bpe.isLoaded_0b = false;
        s3 = bpe;
      } else {
        final CombatantAsset0c.AnimType anim = new CombatantAsset0c.AnimType(new TmdAnimationFile(data));
        anim._08 = a3;
        anim.type_0a = 2;
        anim.isLoaded_0b = false;
        s3 = anim;
      }
      //LAB_800c9b4c
    } else if(type == 3) {
      //LAB_800c9bb0
      final CombatantAsset0c.GlobalAssetType asset = new CombatantAsset0c.GlobalAssetType(a3);
      asset._08 = -1;
      asset.type_0a = 3;
      asset.isLoaded_0b = true;
      s3 = asset;
    } else if(type == 6) {
      //LAB_800c9bcc
      final CombatantAsset0c.TimType tim = new CombatantAsset0c.TimType(data);
      tim._08 = -1;
      tim.type_0a = 6;
      tim.isLoaded_0b = false;
      s3 = tim;
    } else {
      return;
    }

    //LAB_800c9c44
    s3.assetIndex_04 = -1;
    s3.compressedAssetIndex_06 = -1;
    s3._09 = 0;

    combatant.assets_14[animIndex] = s3;

    //LAB_800c9c54
  }

  @Method(0x800c9c7cL)
  public void deallocateCombatantAnimation(final CombatantStruct1a8 combatant, final int animIndex) {
    final CombatantAsset0c asset = combatant.assets_14[animIndex];

    if(asset != null) {
      //LAB_800c9cec
      while(asset._09 > 0) {
        FUN_800ca194(combatant.assets_14[animIndex]);
      }

      //LAB_800c9d04
      if(asset instanceof final CombatantAsset0c.GlobalAssetType index) {
        battleState_8006e398.deallocateGlobalAsset(index.assetIndex_00);
      } else if(asset instanceof final CombatantAsset0c.BpeType bpe) {
        if(bpe.isLoaded_0b) {
          if(bpe.assetIndex_04 >= 0) {
            //LAB_800c9d78
            battleState_8006e398.deallocateGlobalAsset(bpe.assetIndex_04);
          } else {
            battleState_8006e398.compressedAssets_d8c[bpe.compressedAssetIndex_06].used_04 = false;
          }
        }
      }

      //LAB_800c9d84
      combatant.assets_14[animIndex] = null;
    }

    //LAB_800c9da0
  }

  @Method(0x800c9db8L)
  public void loadAnimationIntoCombatant(final CombatantStruct1a8 combatant, final int animIndex, final int assetIndex) {
    this.deallocateCombatantAnimation(combatant, animIndex);
    this.FUN_800c9a80(null, 3, assetIndex, combatant, animIndex);
  }

  @Method(0x800c9e10L)
  public boolean FUN_800c9e10(final CombatantStruct1a8 combatant, final int animIndex) {
    final CombatantAsset0c asset = combatant.assets_14[animIndex];

    if(asset instanceof final CombatantAsset0c.AnimType animType) {
      return animType.anim_00 != null;
    }

    if(asset instanceof final CombatantAsset0c.GlobalAssetType globalAssetType) {
      return globalAssetType.assetIndex_00 >= 0;
    }

    if(asset instanceof final CombatantAsset0c.BpeType bpeType) {
      if(!bpeType.isLoaded_0b) {
        this.currentCompressedAssetIndex_800c66ac = this.currentCompressedAssetIndex_800c66ac + 1 & 0xf;
        final int index = this.currentCompressedAssetIndex_800c66ac;
        battleState_8006e398.compressedAssets_d8c[index].asset_00 = bpeType;
        battleState_8006e398.compressedAssets_d8c[index].used_04 = true;
        bpeType.isLoaded_0b = true;
        bpeType.compressedAssetIndex_06 = index;

        this.FUN_800c9fcc(new FileData(Unpacker.decompress(bpeType.bpe_00)), battleState_8006e398.compressedAssets_d8c[index]);
      }

      return true;
    }

    if(asset instanceof final CombatantAsset0c.TimType timType) {
      if(!timType.isLoaded_0b) {
        asset.assetIndex_04 = battleState_8006e398.loadGlobalAsset(timType.data, 3);
        asset.isLoaded_0b = true;
      }

      //LAB_800c9fb4
      return true;
    }

    return false;
  }

  @Method(0x800c9fccL)
  public void FUN_800c9fcc(final FileData data, final CompressedAsset08 compressedAsset) {
    final CombatantAsset0c asset = compressedAsset.asset_00;

    if(compressedAsset.used_04 && asset.isLoaded_0b) {
      asset.assetIndex_04 = battleState_8006e398.loadGlobalAsset(data, 3);
      asset.compressedAssetIndex_06 = -1;
      compressedAsset.used_04 = false;
    }

    //LAB_800ca040
  }

  @Method(0x800ca100L)
  public void loadAnimationAssetIntoModel(final Model124 model, final CombatantStruct1a8 combatant, final int animIndex) {
    loadModelStandardAnimation(model, battleState_8006e398.getAnimationGlobalAsset(combatant, animIndex));
    combatant.assets_14[0]._09++;
  }

  @Method(0x800ca194L)
  public static void FUN_800ca194(@Nullable final CombatantAsset0c asset) {
    if(asset != null) {
      if(asset._09 > 0) {
        asset._09--;
      }

      //LAB_800ca1f4
      if(asset.type_0a >= 4 && asset._09 == 0) {
        if(asset.assetIndex_04 >= 0) {
          battleState_8006e398.deallocateGlobalAsset(asset.assetIndex_04);
        }

        //LAB_800ca240
        asset.assetIndex_04 = -1;
        asset.compressedAssetIndex_06 = -1;
        asset.isLoaded_0b = false;
      }
    }
  }

  @Method(0x800ca26cL)
  public void FUN_800ca26c(final CombatantStruct1a8 combatant) {
    //LAB_800ca2bc
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] != null && combatant.assets_14[i]._09 == 0) {
        FUN_800ca194(combatant.assets_14[i]);
      }
    }
  }

  @Method(0x800ca418L)
  public void FUN_800ca418(final CombatantStruct1a8 combatant) {
    //LAB_800ca488
    //LAB_800ca494
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] instanceof CombatantAsset0c.AnimType && combatant.assets_14[i].type_0a == 2 || combatant.assets_14[i] instanceof CombatantAsset0c.TimType) {
        //LAB_800ca4c0
        this.deallocateCombatantAnimation(combatant, i);
      }

      //LAB_800ca4cc
    }

    if(combatant.mrg_04 != null) {
      combatant.mrg_04 = null;
    }

    //LAB_800ca4f8
    combatant.flags_19e &= 0xffef;
  }

  @Method(0x800ca55cL)
  public void loadCombatantTextures(final CombatantStruct1a8 combatant) {
    if(combatant.charIndex_1a2 >= 0) {
      int fileIndex = gameState_800babc8.charIds_88[combatant.charSlot_19c];

      if((combatant.charIndex_1a2 & 0x1) != 0) {
        if(fileIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
          fileIndex = 10;
        }

        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/dragoon".formatted(charName), files -> this.loadCombatantTim(combatant, files));
      } else {
        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/combat".formatted(charName), files -> this.loadCombatantTim(combatant, files));
      }
    }

    //LAB_800ca64c
  }

  @Method(0x800ca75cL)
  public void loadCombatantTim(@Nullable final CombatantStruct1a8 combatant, final FileData timFile) {
    if(timFile.size() == 0) {
      return;
    }

    final int vramSlot;

    if(combatant != null) {
      vramSlot = combatant.vramSlot_1a0;
    } else {
      vramSlot = 0;
    }

    //LAB_800ca7d0
    this.loadCombatantTim2(vramSlot, timFile);
  }

  @Method(0x800ca7ecL)
  public void loadCombatantTim2(final int vramSlot, final FileData timFile) {
    final Tim tim = new Tim(timFile);

    if(vramSlot != 0) {
      //LAB_800ca83c
      final Rect4i combatantTimRect = combatantTimRects_800fa6e0[vramSlot];
      GPU.uploadData15(combatantTimRect, tim.getImageData());

      if(tim.hasClut()) {
        final Rect4i clutRect = tim.getClutRect();
        clutRect.x = combatantTimRect.x;
        clutRect.y = combatantTimRect.y + 240;

        //LAB_800ca884
        GPU.uploadData15(clutRect, tim.getClutData());
      }
    } else {
      final Rect4i imageRect = tim.getImageRect();

      // This is a fix for a retail bug where they try to load a TMD as a TIM (it has a 0 w/h anyway so no data gets loaded) see GH#330b
      if(imageRect.x == 0x41 && imageRect.y == 0 && imageRect.w == 0 && imageRect.h == 0) {
        return;
      }

      tim.uploadToGpu();
    }

    //LAB_800ca88c
  }

  @Method(0x800ca89cL)
  public int findFreeMonsterTextureSlot(final int a0) {
    //LAB_800ca8ac
    //LAB_800ca8c4
    synchronized(this.usedMonsterTextureSlotsLock) {
      for(int i = a0 < 0x200 ? 4 : 1; i < 9; i++) {
        final int a0_0 = 0x1 << i;

        if((this.usedMonsterTextureSlots_800c66c4 & a0_0) == 0) {
          this.usedMonsterTextureSlots_800c66c4 |= a0_0;
          return i;
        }

        //LAB_800ca8e4
      }

      //LAB_800ca8f4
      return 0;
    }
  }

  @Method(0x800ca8fcL)
  public void setMonsterTextureSlotUsed(final int shift) {
    synchronized(this.usedMonsterTextureSlotsLock) {
      this.usedMonsterTextureSlots_800c66c4 |= 0x1 << shift;
    }
  }

  @Method(0x800ca918L)
  public void unsetMonsterTextureSlotUsed(final int shift) {
    synchronized(this.usedMonsterTextureSlotsLock) {
      this.usedMonsterTextureSlots_800c66c4 &= ~(0x1 << shift);
    }
  }

  @Method(0x800cae44L)
  public void clearCurrentDisplayableItems() {
    this.currentDisplayableIconsBitset_800c675c = 0;
  }

  @Method(0x800cb250L)
  public boolean FUN_800cb250(final ScriptState<BattleEntity27c> state, final BattleEntity27c bent) {
    float x = bent.movementDestination_e8.x;
    float y = bent.movementDestination_e8.y;
    float z = bent.movementDestination_e8.z;

    if(bent.movementParent_c8 != null) {
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;

      x += parent.model_148.coord2_14.coord.transfer.x;
      y += parent.model_148.coord2_14.coord.transfer.y;
      z += parent.model_148.coord2_14.coord.transfer.z;
    }

    //LAB_800cb2ac
    bent.movementTicks_cc--;
    if(bent.movementTicks_cc > 0) {
      bent.movementRemaining_d0.sub(bent.movementStep_dc);
      bent.model_148.coord2_14.coord.transfer.x = x - bent.movementRemaining_d0.x;
      bent.model_148.coord2_14.coord.transfer.y = y - bent.movementRemaining_d0.y;
      bent.model_148.coord2_14.coord.transfer.z = z - bent.movementRemaining_d0.z;
      bent.movementStep_dc.y += bent.movementStepYAcceleration_f4;
      return false;
    }

    //LAB_800cb338
    bent.model_148.coord2_14.coord.transfer.set(x, y, z);
    return true;
  }

  @Method(0x800cb34cL)
  public boolean FUN_800cb34c(final ScriptState<BattleEntity27c> state, final BattleEntity27c bent) {
    final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
    final Vector3f vec = parent.model_148.coord2_14.coord.transfer;
    final float angle = MathHelper.atan2(vec.x - bent.model_148.coord2_14.coord.transfer.x, vec.z - bent.model_148.coord2_14.coord.transfer.z) + MathHelper.PI;

    bent.movementTicks_cc--;
    if(bent.movementTicks_cc > 0) {
      bent.movementRemaining_d0.x -= bent.movementRemaining_d0.y; // This is correct, sometimes this vec is used as (angle, step)
      bent.model_148.coord2_14.transforms.rotate.y = angle + bent.movementRemaining_d0.x;
      return false;
    }

    //LAB_800cb3e0
    bent.model_148.coord2_14.transforms.rotate.y = angle;

    //LAB_800cb3e8
    return true;
  }

  @ScriptDescription("Sets the position of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cb3fcL)
  public FlowControl scriptSetBentPos(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.coord.transfer.x = script.params_20[1].get();
    bent.model_148.coord2_14.coord.transfer.y = script.params_20[2].get();
    bent.model_148.coord2_14.coord.transfer.z = script.params_20[3].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the position of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cb468L)
  public FlowControl scriptGetBentPos(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(Math.round(bent.model_148.coord2_14.coord.transfer.x));
    script.params_20[2].set(Math.round(bent.model_148.coord2_14.coord.transfer.y));
    script.params_20[3].set(Math.round(bent.model_148.coord2_14.coord.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the rotation of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x800cb4c8L)
  public FlowControl scriptSetBentRotation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.rotate.x = MathHelper.psxDegToRad(script.params_20[1].get());
    bent.model_148.coord2_14.transforms.rotate.y = MathHelper.psxDegToRad(script.params_20[2].get());
    bent.model_148.coord2_14.transforms.rotate.z = MathHelper.psxDegToRad(script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the Y rotation of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @Method(0x800cb534L)
  public FlowControl scriptSetBentRotationY(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.rotate.y = MathHelper.psxDegToRad(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the rotation of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x800cb578L)
  public FlowControl scriptGetBentRotation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(MathHelper.radToPsxDeg(bent.model_148.coord2_14.transforms.rotate.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(bent.model_148.coord2_14.transforms.rotate.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(bent.model_148.coord2_14.transforms.rotate.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the status resist flags of a monster battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The status resist flags")
  @Method(0x800cb5d8L)
  public FlowControl scriptGetMonsterStatusResistFlags(final RunningScript<?> script) {
    final MonsterBattleEntity monster = (MonsterBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(monster.monsterStatusResistFlag_76);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets bit 10 of battle entity script flags (possibly whether a battle entity renders and animates?)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set the flag, false otherwise")
  @Method(0x800cb618L)
  public FlowControl FUN_800cb618(final RunningScript<?> script) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cb668
    if(script.params_20[1].get() != 0) {
      a1.storage_44[7] &= ~0x10;
    } else {
      //LAB_800cb65c
      a1.storage_44[7] |= 0x10;
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets whether or not a battle entity's animation interpolation is enabled")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to enable interpolation, false otherwise")
  @Method(0x800cb674L)
  public FlowControl scriptSetInterpolationEnabled(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.disableInterpolation_a2 = script.params_20[1].get() == 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to loading a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "animIndex", description = "The animation index")
  @Method(0x800cb6bcL)
  public FlowControl FUN_800cb6bc(final RunningScript<?> a0) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    if((state.storage_44[7] & 0x1) != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;
    final int animIndex = a0.params_20[1].get();

    if(bent.currentAnimIndex_270 < 0) {
      this.FUN_800c9e10(bent.combatant_144, animIndex);
      bent.currentAnimIndex_270 = animIndex;
    } else if(bent.currentAnimIndex_270 != animIndex) {
      FUN_800ca194(bent.combatant_144.assets_14[bent.currentAnimIndex_270]);

      //LAB_800cb73c
      this.FUN_800c9e10(bent.combatant_144, animIndex);
      bent.currentAnimIndex_270 = animIndex;
    }

    //LAB_800cb750
    return FlowControl.PAUSE;
  }

  @ScriptDescription("No-op")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @Method(0x800cb764L)
  public FlowControl FUN_800cb764(final RunningScript<?> a0) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related battle entity animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb76cL)
  public FlowControl FUN_800cb76c(final RunningScript<?> a0) {
    final ScriptState<?> s2 = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    final BattleEntity27c s0 = (BattleEntity27c)s2.innerStruct_00;
    if((s2.storage_44[7] & 0x1) == 0) {
      int animIndex = s0.currentAnimIndex_270;

      if(animIndex < 0) {
        animIndex = 0;
      }

      //LAB_800cb7d0
      if(s0.combatant_144.isAssetLoaded(animIndex)) {
        FUN_800ca194(s0.combatant_144.assets_14[s0.loadingAnimIndex_26e]);
        this.loadAnimationAssetIntoModel(s0.model_148, s0.combatant_144, animIndex);
        s2.storage_44[7] &= 0xffff_ff6f;
        s0.model_148.animationState_9c = 1;
        s0.loadingAnimIndex_26e = animIndex;
        s0.currentAnimIndex_270 = -1;
        return FlowControl.CONTINUE;
      }
    }

    //LAB_800cb830
    //LAB_800cb834
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets a battle entity's loading animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "animIndex", description = "The animation index")
  @Method(0x800cb84cL)
  public FlowControl scriptSetLoadingBentAnimationIndex(final RunningScript<?> script) {
    final ScriptState<BattleEntity27c> state = SCRIPTS.getState(script.params_20[0].get(), BattleEntity27c.class);
    final BattleEntity27c bent = state.innerStruct_00;

    if((state.storage_44[7] & 0x1) == 0) {
      int newAnim = script.params_20[1].get();
      final int currentAnim = bent.currentAnimIndex_270;

      // GH#1550 Fix Shana softlock trying to load finisher
      if(bent instanceof PlayerBattleEntity && bent.combatant_144.assets_14[newAnim] == null) {
        newAnim = 1;
      }

      if(currentAnim >= 0) {
        if(currentAnim != newAnim) {
          FUN_800ca194(bent.combatant_144.assets_14[currentAnim]);
        }

        //LAB_800cb8d0
        bent.currentAnimIndex_270 = -1;
      }

      //LAB_800cb8d4
      if(bent.combatant_144.isAssetLoaded(newAnim)) {
        FUN_800ca194(bent.combatant_144.assets_14[bent.loadingAnimIndex_26e]);
        this.loadAnimationAssetIntoModel(bent.model_148, bent.combatant_144, newAnim);
        state.storage_44[7] &= 0xffff_ff6f;
        bent.model_148.animationState_9c = 1;
        bent.loadingAnimIndex_26e = newAnim;
        bent.currentAnimIndex_270 = -1;
        return FlowControl.CONTINUE;
      }

      //LAB_800cb934
      this.FUN_800c9e10(bent.combatant_144, newAnim);
    }

    //LAB_800cb944
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Something related battle entity animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb95cL)
  public FlowControl FUN_800cb95c(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    this.FUN_800ca26c(bent.combatant_144);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's loading animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "animationIndex", description = "The animation index")
  @Method(0x800cb9b0L)
  public FlowControl scriptGetLoadingBentAnimationIndex(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.loadingAnimIndex_26e);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Pauses a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb9f0L)
  public FlowControl scriptPauseAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animationState_9c = 2;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Resumes a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cba28L)
  public FlowControl scriptResumeAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animationState_9c = 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's loop state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "loop", description = "True to enable looping, false to disable")
  @Method(0x800cba60L)
  public FlowControl scriptSetBentAnimationLoopState(final RunningScript<?> script) {
    //LAB_800cbab0
    if(script.params_20[1].get() != 0) {
      scriptStatePtrArr_800bc1c0[script.params_20[0].get()].storage_44[7] &= 0xffff_ff7f;
    } else {
      //LAB_800cbaa4
      scriptStatePtrArr_800bc1c0[script.params_20[0].get()].storage_44[7] |= 0x80;
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Check if a battle entity's animation has finished")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "finished", description = "True if finished, false otherwise")
  @Method(0x800cbabcL)
  public FlowControl scriptAnimationHasFinished(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.model_148.remainingFrames_9e > 0 ? 0 : 1);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbb00L)
  public FlowControl FUN_800cbb00(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex];
    final BattleEntity27c v1 = state.innerStruct_00;

    float x = v1.model_148.coord2_14.coord.transfer.x;
    float y = v1.model_148.coord2_14.coord.transfer.y;
    float z = v1.model_148.coord2_14.coord.transfer.z;

    final int t0 = script.params_20[1].get();
    if(t0 >= 0) {
      v1.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[t0];
      final BattleEntity27c parent = v1.movementParent_c8.innerStruct_00;
      x -= parent.model_148.coord2_14.coord.transfer.x;
      y -= parent.model_148.coord2_14.coord.transfer.y;
      z -= parent.model_148.coord2_14.coord.transfer.z;
    } else {
      v1.movementParent_c8 = null;
    }

    //LAB_800cbb98
    this.FUN_800cdc1c(state, x, y, z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0, script.params_20[2].get());
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbc14L)
  public FlowControl FUN_800cbc14(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent.model_148.coord2_14.coord.transfer);
    final int scriptIndex2 = script.params_20[1].get();

    if(scriptIndex2 >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
      vec.sub(parent.model_148.coord2_14.coord.transfer);
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cbcc4
    final float x = script.params_20[3].get() - vec.x;
    final float y = script.params_20[4].get() - vec.y;
    final float z = script.params_20[5].get() - vec.z;
    this.FUN_800cdc1c(state, vec.x, vec.y, vec.z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0, Math.round(Math.sqrt(x * x + y * y + z * z) / script.params_20[2].get()));
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbde0L)
  public FlowControl FUN_800cbde0(final RunningScript<?> script) {
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c bent = state.innerStruct_00;
    float x = bent.model_148.coord2_14.coord.transfer.x;
    float y = bent.model_148.coord2_14.coord.transfer.y;
    float z = bent.model_148.coord2_14.coord.transfer.z;

    if(script.params_20[1].get() >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
      x -= parent.model_148.coord2_14.coord.transfer.x;
      y -= parent.model_148.coord2_14.coord.transfer.y;
      z -= parent.model_148.coord2_14.coord.transfer.z;
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cbe78
    this.FUN_800cdc1c(state, x, y, z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0x20, script.params_20[2].get());
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cbef8L)
  public FlowControl FUN_800cbef8(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
      vec.sub(parent.model_148.coord2_14.coord.transfer);
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cbfa8
    final float x = script.params_20[3].get() - vec.x;
    final float y = script.params_20[4].get() - vec.y;
    final float z = script.params_20[5].get() - vec.z;
    this.FUN_800cdc1c(state, vec.x, vec.y, vec.z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0x20, Math.round(Math.sqrt(x * x + y * y + z * z) / script.params_20[2].get()));
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc0c8L)
  public FlowControl FUN_800cc0c8(final RunningScript<?> script) {
    final int s0 = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[s0];
    final BattleEntity27c bent = state.innerStruct_00;
    final Vector3f translation = new Vector3f(bent.model_148.coord2_14.coord.transfer);
    final int parentIndex = script.params_20[1].get();

    if(parentIndex >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[parentIndex];
      translation.sub(bent.movementParent_c8.innerStruct_00.model_148.coord2_14.coord.transfer);
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cc160
    this.FUN_800cdc1c(state, translation.x, translation.y, translation.z, script.params_20[3].get(), translation.y, script.params_20[4].get(), 0, script.params_20[2].get());
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc1ccL)
  public FlowControl FUN_800cc1cc(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int parentIndex = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent.model_148.coord2_14.coord.transfer);

    if(parentIndex >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[parentIndex];
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
      vec.sub(parent.model_148.coord2_14.coord.transfer);
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cc27c
    final float x = script.params_20[3].get() - vec.x;
    final float z = script.params_20[4].get() - vec.z;
    this.FUN_800cdc1c(state, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0, Math.round(Math.sqrt(x * x + z * z) / script.params_20[2].get()));
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc364L)
  public FlowControl FUN_800cc364(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int parentIndex = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent.model_148.coord2_14.coord.transfer);

    if(parentIndex >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[parentIndex];
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
      vec.sub(parent.model_148.coord2_14.coord.transfer);
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cc3fc
    this.FUN_800cdc1c(state, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0x20, script.params_20[2].get());
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc46cL)
  public FlowControl FUN_800cc46c(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int parentIndex = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent.model_148.coord2_14.coord.transfer);

    if(parentIndex >= 0) {
      bent.movementParent_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[parentIndex];
      final BattleEntity27c parent = bent.movementParent_c8.innerStruct_00;
      vec.sub(parent.model_148.coord2_14.coord.transfer);
    } else {
      bent.movementParent_c8 = null;
    }

    //LAB_800cc51c
    final float x = script.params_20[3].get() - vec.x;
    final float z = script.params_20[4].get() - vec.z;
    this.FUN_800cdc1c(state, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0x20, Math.round(Math.sqrt(x * x + z * z) / script.params_20[2].get()));
    state.setTempTicker(this::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Turn a battle entity towards another")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndexToTurn", description = "A BattleEntity27c script index to turn")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndexTarget", description = "A BattleEntity27c script index to target")
  @Method(0x800cc608L)
  public FlowControl scriptBentLookAtBent(final RunningScript<?> script) {
    final BattleEntity27c s0 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c v0 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    s0.model_148.coord2_14.transforms.rotate.y = MathHelper.atan2(v0.model_148.coord2_14.coord.transfer.x - s0.model_148.coord2_14.coord.transfer.x, v0.model_148.coord2_14.coord.transfer.z - s0.model_148.coord2_14.coord.transfer.z) + MathHelper.PI;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @Method(0x800cc698L)
  public FlowControl FUN_800cc698(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final ScriptState<BattleEntity27c> state2 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final BattleEntity27c bent2 = state2.innerStruct_00;
    final int ticks = script.params_20[2].get();
    final float v0 = MathHelper.floorMod(MathHelper.atan2(bent2.model_148.coord2_14.coord.transfer.x - bent1.model_148.coord2_14.coord.transfer.x, bent2.model_148.coord2_14.coord.transfer.z - bent1.model_148.coord2_14.coord.transfer.z) - bent1.model_148.coord2_14.transforms.rotate.y, MathHelper.TWO_PI) - MathHelper.PI;
    bent1.movementParent_c8 = state2;
    bent1.movementTicks_cc = ticks;
    bent1.movementRemaining_d0.x = v0;
    bent1.movementRemaining_d0.y = v0 / ticks;
    state1.setTempTicker(this::FUN_800cb34c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc784L)
  public FlowControl FUN_800cc784(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    this.FUN_800ca418(bent.combatant_144);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to battle entity asset loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc7d8L)
  public FlowControl FUN_800cc7d8(final RunningScript<?> script) {
    final ScriptState<BattleEntity27c> state = SCRIPTS.getState(script.params_20[0].get(), BattleEntity27c.class);
    final int s2 = state.storage_44[7] & 0x4;
    final CombatantStruct1a8 bentCombatant = state.innerStruct_00.combatant_144;

    //LAB_800cc83c
    for(int i = 0; i < this.combatantCount_800c66a0; i++) {
      final CombatantStruct1a8 combatant = this.getCombatant(i);

      if(combatant != bentCombatant) {
        if((combatant.flags_19e & 0x1) != 0 && combatant.mrg_04 != null && combatant.charIndex_1a2 >= 0) {
          final int v0 = combatant.flags_19e >>> 2 ^ 1;

          if(s2 == 0) {
            //LAB_800cc8ac
            if((v0 & 1) == 0) {
              //LAB_800cc8b4
              this.FUN_800ca418(combatant);
            }
          } else {
            if((v0 & 1) != 0) {
              this.FUN_800ca418(combatant);
            }
          }
        }
      }

      //LAB_800cc8bc
    }

    //LAB_800cc8d8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads the attack animations for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc8f4L)
  public FlowControl scriptLoadAttackAnimations(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    this.loadAttackAnimations(bent.combatant_144);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads the attack animations for every combatant")
  @Method(0x800cc948L)
  public FlowControl loadAllCharAttackAnimations(final RunningScript<?> script) {
    //LAB_800cc970
    for(int i = 0; i < this.combatantCount_800c66a0; i++) {
      final CombatantStruct1a8 combatant = this.getCombatant(i);
      if((combatant.flags_19e & 0x1) != 0 && combatant.charIndex_1a2 >= 0) {
        this.loadAttackAnimations(combatant);
      }

      //LAB_800cc9a8
    }

    //LAB_800cc9c0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Enables or disables a battle entity's texture's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "The texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "enabled", description = "True to enable, false otherwise")
  @Method(0x800cc9d8L)
  public FlowControl scriptEnableBentTextureAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animateTextures_ec[script.params_20[1].get()] = script.params_20[2].get() > 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets up battle menu, handles its input, and renders it")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "displayableIconsBitset", description = "A bitset of which icons are displayed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "disabledIconsBitset", description = "A bitset of which icons are disabled")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "selectedAction", description = "The action the player has selected (defend, transform, d-magic, attack, item, run, special, ?, d-attack)")
  @Method(0x800cca34L)
  public FlowControl scriptSetUpAndHandleCombatMenu(final RunningScript<BattleEntity27c> script) {
    if(this.hud.listMenu_800c6b60 != null) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int displayableIconsBitset = script.params_20[0].get();

    if(this.currentDisplayableIconsBitset_800c675c != displayableIconsBitset || (script.scriptState_04.storage_44[7] & 0x1000) != 0) {
      //LAB_800cca7c
      final int disabledIconsBitset;

      if(script.paramCount_14 == 2) {
        disabledIconsBitset = 0;
      } else {
        //LAB_800ccaa0
        disabledIconsBitset = script.params_20[1].get();
      }

      //LAB_800ccab4
      this.hud.initializeMenuIcons(script.scriptState_04, displayableIconsBitset, disabledIconsBitset);

      script.scriptState_04.storage_44[7] &= 0xffff_efff;
      this.currentDisplayableIconsBitset_800c675c = displayableIconsBitset;
    }

    //LAB_800ccaec
    this.hud.toggleHighlight(true);

    final int selectedAction = this.hud.tickAndRender();
    if(selectedAction == 0) {
      //LAB_800ccb24
      return FlowControl.PAUSE_AND_REWIND;
    }

    this.hud.toggleHighlight(false);
    script.params_20[2].set(selectedAction - 1);

    //LAB_800ccb28
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a damage number to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @Method(0x800ccb3cL)
  public FlowControl scriptRenderDamage(final RunningScript<?> script) {
    this.hud.renderDamage(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a floating number above a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The number to show")
  @Method(0x800ccb70L)
  public FlowControl scriptAddFloatingNumberForBent(final RunningScript<?> script) {
    this.hud.addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), 13);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "isDragoon", description = "Whether or not the battle entity is a dragoon")
  @Method(0x800ccba4L)
  public FlowControl FUN_800ccba4(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;
    final CombatantStruct1a8 combatant = bent.combatant_144;

    if((state.storage_44[7] & 0x1) == 0) {
      if(bent.currentAnimIndex_270 >= 0) {
        FUN_800ca194(bent.combatant_144.assets_14[bent.currentAnimIndex_270]);
      }

      //LAB_800ccc24
      this.deallocateCombatant(bent.combatant_144);

      if(script.params_20[1].get() != 0) {
        state.storage_44[7] |= 0x3;
        combatant.charIndex_1a2 |= 0x1;
      } else {
        //LAB_800ccc60
        state.storage_44[7] = state.storage_44[7] & 0xffff_fffd | 0x1;
        combatant.charIndex_1a2 &= 0xfffe;
      }

      //LAB_800ccc78
      this.loadCombatantTextures(bent.combatant_144);
      this.loadCombatantTmdAndAnims(combatant);
      //LAB_800ccc94
    } else if((combatant.flags_19e & 0x20) == 0) {
      loadCombatantModelAndAnimation(bent.model_148, combatant);
      bent.loadingAnimIndex_26e = 0;
      bent.currentAnimIndex_270 = -1;
      state.storage_44[7] &= 0xffff_fffe;
      return FlowControl.CONTINUE;
    }

    //LAB_800ccccc
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Gets the character or monster ID for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "id", description = "The character or monster ID")
  @Method(0x800cccf4L)
  public FlowControl scriptGetCharOrMonsterId(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.charId_272);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a stat value of a battle entity (doesn't allow setting negative HP)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new stat value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @Method(0x800ccd34L)
  public FlowControl scriptSetBentStat(final RunningScript<?> script) {
    //LAB_800ccd8c
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntityStat stat = BattleEntityStat.fromLegacy(Math.max(0, script.params_20[2].get()));

    switch(stat) {
      case ITEM_ID -> bent.item_d4 = REGISTRIES.items.getEntry(script.params_20[1].getRegistryId()).get();
      default -> {
        int value = script.params_20[1].get();
        if(script.params_20[2].get() == 2 && value < 0) {
          value = 0;
        }

        bent.setStat(stat, value);
      }
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new stat value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @Method(0x800ccda0L)
  public FlowControl scriptSetBentRawStat(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntityStat stat = BattleEntityStat.fromLegacy(Math.max(0, script.params_20[2].get()));

    switch(stat) {
      case ITEM_ID -> bent.item_d4 = REGISTRIES.items.getEntry(script.params_20[1].getRegistryId()).get();
      default -> bent.setStat(stat, script.params_20[1].get());
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The stat value")
  @Method(0x800cce04L)
  public FlowControl scriptGetBentStat(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntityStat stat = BattleEntityStat.fromLegacy(script.params_20[1].get());

    switch(stat) {
      case EQUIPMENT_WEAPON_SLOT -> script.params_20[2].set(((PlayerBattleEntity)bent).equipment_11e.get(EquipmentSlot.WEAPON).getRegistryId());
      case EQUIPMENT_HELMET_SLOT -> script.params_20[2].set(((PlayerBattleEntity)bent).equipment_11e.get(EquipmentSlot.HELMET).getRegistryId());
      case EQUIPMENT_ARMOUR_SLOT -> script.params_20[2].set(((PlayerBattleEntity)bent).equipment_11e.get(EquipmentSlot.ARMOUR).getRegistryId());
      case EQUIPMENT_BOOTS_SLOT -> script.params_20[2].set(((PlayerBattleEntity)bent).equipment_11e.get(EquipmentSlot.BOOTS).getRegistryId());
      case EQUIPMENT_ACCESSORY_SLOT -> script.params_20[2].set(((PlayerBattleEntity)bent).equipment_11e.get(EquipmentSlot.ACCESSORY).getRegistryId());
      case ITEM_ID -> script.params_20[2].set(bent.item_d4.getRegistryId());
      case ITEM_ELEMENT -> script.params_20[2].set(bent.item_d4.getAttackElement().getRegistryId());
      case ELEMENT -> script.params_20[2].set(bent.getElement().getRegistryId());
      default -> script.params_20[2].set(bent.getStat(stat));
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The stat value")
  @Method(0x800cce70L)
  public FlowControl scriptGetBentStat2(final RunningScript<?> script) {
    return this.scriptGetBentStat(script);
  }

  @ScriptDescription("Shows or hides the battle HUD")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "visible", description = "True to show HUD, false to hide")
  @Method(0x800ccec8L)
  public FlowControl scriptSetBattleHudVisibility(final RunningScript<?> script) {
    this.hud.setBattleHudVisibility(script.params_20[0].get() > 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets post-battle action to 3")
  @Method(0x800ccef8L)
  public FlowControl FUN_800ccef8(final RunningScript<?> script) {
    postBattleAction_800bc974 = 3;
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets the post-battle action")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "action", description = "The post-battle action")
  @Method(0x800ccf0cL)
  public FlowControl scriptSetPostBattleAction(final RunningScript<?> script) {
    postBattleAction_800bc974 = script.params_20[0].get();
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets a battle entity as dead (or not dead) and drops its loot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dead", description = "True for dead, false otherwise")
  @Method(0x800ccf2cL)
  public FlowControl scriptSetBentDeadAndDropLoot(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c data = (BattleEntity27c)state.innerStruct_00;

    int flags = state.storage_44[7];
    if(script.params_20[1].get() != 0) {
      if((flags & 0x40) == 0) { // Not dead
        flags = flags | 0x40; // Set dead

        if((flags & 0x4) != 0) { // Monster
          final CombatantStruct1a8 enemyCombatant = data.combatant_144;
          goldGainedFromCombat_800bc920 += enemyCombatant.gold_196;
          totalXpFromCombat_800bc95c += enemyCombatant.xp_194;

          if((flags & 0x2000) == 0) { // Hasn't already dropped loot
            for(final CombatantStruct1a8.ItemDrop drop : enemyCombatant.drops) {
              if(simpleRand() * 100 >> 16 < drop.chance()) {
                if(drop.item() instanceof final Equipment equipment) {
                  itemsDroppedByEnemies_800bc928.add(new EnemyDrop(equipment.getIcon(), I18n.translate(equipment), () -> giveEquipment(equipment), () -> equipmentOverflow.add(equipment)));
                } else if(drop.item() instanceof final Item item) {
                  itemsDroppedByEnemies_800bc928.add(new EnemyDrop(item.getIcon(), I18n.translate(item), () -> giveItem(item), () -> itemOverflow.add(item)));
                }

                flags |= 0x2000;
              }
            }
          }
        }
      }
    } else {
      //LAB_800cd04c
      flags = flags & 0xffff_ffbf;
    }

    //LAB_800cd054
    state.storage_44[7] = flags;
    battleState_8006e398.cacheLivingBents();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity as dead (or not), but doesn't drop loot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dead", description = "True for dead, false otherwise")
  @Method(0x800cd078L)
  public FlowControl scriptSetBentDead(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cd0d0
    if(script.params_20[1].get() != 0) {
      state.storage_44[7] |= 0x40;
    } else {
      //LAB_800cd0c4
      state.storage_44[7] &= 0xffff_ffbf;
    }

    battleState_8006e398.cacheLivingBents();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the hit property of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hitNum", description = "The hit number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hitPropertyIndex", description = "The hit property index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The hit property value")
  @Method(0x800cd0ecL)
  public FlowControl scriptGetHitProperty(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[3].set(battlePreloadedEntities_1f8003f4.getHitProperty(
      bent.charSlot_276,
      script.params_20[1].get(),
      script.params_20[2].get()
    ));

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Levels up a character's addition")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "doIt", description = "If false, this method does nothing")
  @Method(0x800cd160L)
  public FlowControl scriptLevelUpAddition(final RunningScript<?> script) {
    if(script.params_20[1].get() != 0) {
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

      final int charIndex = bent.charId_272;
      final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];

      final int additionIndex = charData.selectedAddition_19 - additionOffsets_8004f5ac[charIndex];
      if(charIndex == 2 || charIndex == 8 || additionIndex < 0) {
        //LAB_800cd200
        return FlowControl.CONTINUE;
      }

      //LAB_800cd208
      final int additionXp = Math.min(99, charData.additionXp_22[additionIndex] + 1);

      //LAB_800cd240
      //LAB_800cd288
      while(charData.additionLevels_1a[additionIndex] < 5 && additionXp >= charData.additionLevels_1a[additionIndex] * 20) {
        charData.additionLevels_1a[additionIndex]++;
      }

      //LAB_800cd2ac
      int nonMaxedAdditions = additionCounts_8004f5c0[charIndex];
      int lastNonMaxAdditionIndex = -1;

      // Find the first addition that isn't already maxed out
      //LAB_800cd2ec
      for(int additionIndex2 = 0; additionIndex2 < additionCounts_8004f5c0[charIndex]; additionIndex2++) {
        if(charData.additionLevels_1a[additionIndex2] == 5) {
          nonMaxedAdditions--;
        } else {
          //LAB_800cd308
          lastNonMaxAdditionIndex = additionIndex2;
        }

        //LAB_800cd30c
      }

      // If there's only one addition that isn't maxed (the ultimate addition), unlock it
      //LAB_800cd31c
      if(nonMaxedAdditions < 2 && (charData.partyFlags_04 & 0x40) == 0) {
        charData.partyFlags_04 |= 0x40;

        if(lastNonMaxAdditionIndex >= 0) {
          charData.additionLevels_1a[lastNonMaxAdditionIndex] = 1;
        }

        //LAB_800cd36c
        unlockedUltimateAddition_800bc910[bent.charSlot_276] = true;
      }

      //LAB_800cd390
      charData.additionXp_22[additionIndex] = additionXp;
    }

    //LAB_800cd3ac
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Changes the visibility of a battle entity's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "visible", description = "True for visible, false otherwise")
  @Method(0x800cd3b4L)
  public FlowControl scriptSetModelPartVisibility(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    if(script.params_20[2].get() != 0) {
      bent.model_148.partInvisible_f4 &= ~(0x1L << script.params_20[1].get());
    } else {
      //LAB_800cd420
      bent.model_148.partInvisible_f4 |= 0x1L << script.params_20[1].get();
    }

    //LAB_800cd460
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a global asset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "drgnIndex", description = "The DRGN#.BIN index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "index", description = "The output battleState_8006e398._580 array index")
  @Method(0x800cd468L)
  public FlowControl scriptLoadGlobalAsset(final RunningScript<?> script) {
    script.params_20[2].set(battleState_8006e398.loadGlobalAsset(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Waits until global asset is allocated")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The battleState_8006e398._580 array index")
  @Method(0x800cd4b0L)
  public FlowControl scriptWaitGlobalAssetAllocation(final RunningScript<?> script) {
    final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(script.params_20[0].get());
    return v0.state_04 == 1 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a global asset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The input battleState_8006e398._580 array index")
  @Method(0x800cd4f0L)
  public FlowControl scriptDeallocateGlobalAsset(final RunningScript<?> script) {
    battleState_8006e398.deallocateGlobalAsset(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a combatant to the battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charIndexAndFlags", description = "Exact use unknown, seems to be flags, and possibly character ID in the higher bits?")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The new combatant's index")
  @Method(0x800cd52cL)
  public FlowControl scriptAddCombatant(final RunningScript<?> script) {
    script.params_20[1].set(this.addCombatant(script.params_20[0].get(), -1));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates and removes a combatant from the battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant's index")
  @Method(0x800cd570L)
  public FlowControl scriptDeallocateAndRemoveCombatant(final RunningScript<?> script) {
    this.deallocateCombatant(this.combatants_8005e398[script.params_20[0].get()]);
    this.removeCombatant(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocate a battle entity child of this script")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "entrypointIndex", description = "The entrypoint of this script for the new battle entity to enter")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant to attach to the new battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The allocated script index")
  @Method(0x800cd5b4L)
  public FlowControl scriptAllocateBent(final RunningScript<?> script) {
    final String name = "Bent allocated by script " + script.scriptState_04.index;
    final MonsterBattleEntity bent = new MonsterBattleEntity(name);
    final ScriptState<MonsterBattleEntity> state = SCRIPTS.allocateScriptState(name, bent);
    script.params_20[2].set(state.index);
    state.setTicker(bent::bentLoadingTicker);
    state.setDestructor(bent::bentDestructor);
    state.loadScriptFile(script.scriptState_04.frame().file, script.params_20[0].get());
    state.storage_44[7] |= 0x804;

    final CombatantStruct1a8 combatant = this.getCombatant(script.params_20[1].get());
    bent.combatant_144 = combatant;
    bent.combatantIndex_26c = script.params_20[1].get();
    bent.charId_272 = combatant.charIndex_1a2;
    bent.model_148.coord2_14.coord.transfer.set(0, 0, 0);
    bent.model_148.coord2_14.transforms.rotate.zero();
    battleState_8006e398.addMonster(state);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets global asset index of a model for combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The input battleState_8006e398._580 array index")
  @Method(0x800cd740L)
  public FlowControl scriptLoadModelToCombatantFromGlobalAssets(final RunningScript<?> script) {
    final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(script.params_20[0].get());

    if(v0.state_04 == 1) {
      //LAB_800cd794
      return FlowControl.PAUSE_AND_REWIND;
    }

    this.combatants_8005e398[script.params_20[0].get()]._1a4 = (short)script.params_20[1].get();

    //LAB_800cd798
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0", description = "Combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd7a8L)
  public FlowControl FUN_800cd7a8(final RunningScript<?> script) {
    final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(script.params_20[0].get());

    if(v0.state_04 == 1) {
      //LAB_800cd7fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    this.combatants_8005e398[script.params_20[0].get()]._1a6 = (short)script.params_20[1].get();

    //LAB_800cd800
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads animation from global assets into combatant assets or deallocates an animation from combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animIndex", description = "Combatant's assets_14 index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The input battleState_8006e398._580 array index")
  @Method(0x800cd810L)
  public FlowControl scriptLoadAnimToCombatantFromGlobalAssetsOrDeallocate(final RunningScript<?> script) {
    final int assetIndex = script.params_20[2].get();

    if(assetIndex >= 0) {
      //LAB_800cd85c
      final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(assetIndex);

      if(v0.state_04 == 1) {
        return FlowControl.PAUSE_AND_REWIND;
      }

      this.loadAnimationIntoCombatant(this.combatants_8005e398[script.params_20[0].get()], script.params_20[1].get(), assetIndex);
    } else {
      this.deallocateCombatantAnimation(this.combatants_8005e398[script.params_20[0].get()], script.params_20[1].get());
    }

    //LAB_800cd890
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a texture from global assets to combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The input battleState_8006e398._580 array index")
  @Method(0x800cd8a4L)
  public FlowControl scriptLoadTextureToCombatantFromGlobalAssets(final RunningScript<?> script) {
    final BattleAsset08 a1 = battleState_8006e398.getGlobalAsset(script.params_20[1].get());

    if(a1.state_04 == 1) {
      //LAB_800cd8fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int combatantIndex = script.params_20[0].get();
    final CombatantStruct1a8 combatant = combatantIndex >= 0 ? this.combatants_8005e398[combatantIndex] : null; // Not sure if the else case actually happens
    this.loadCombatantTim(combatant, a1.data_00);

    //LAB_800cd900
    return FlowControl.PAUSE;
  }

  @ScriptDescription("Unknown, loads files")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "drgnIndex", description = "The DRGN#.BIN index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "queueIndex", description = "The index into the file queue")
  @Method(0x800cd910L)
  public FlowControl FUN_800cd910(final RunningScript<?> script) {
    script.params_20[2].set(battleState_8006e398.loadGlobalAsset(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the combatant index for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @Method(0x800cd958L)
  public FlowControl scriptGetCombatantIndex(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the battle entity slot or char slot for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "charOrBentSlot", description = "The character or battle entity slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "mode", description = "If true, returns character slot, if false returns battle entity slot")
  @Method(0x800cd998L)
  public FlowControl scriptGetBentSlot(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(script.params_20[2].get() != 0) {
      script.params_20[1].set(bent.charSlot_276);
    } else {
      //LAB_800cd9e8
      script.params_20[1].set(bent.bentSlot_274);
    }

    //LAB_800cd9f4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of parts in a battle entity's model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c's script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "count")
  @Method(0x800cd9fcL)
  public FlowControl scriptGetBentNobj(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.model_148.modelParts_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a battle entity's combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cda3cL)
  public FlowControl scriptDeallocateCombatant(final RunningScript<?> script) {
    this.deallocateCombatant(this.combatants_8005e398[script.params_20[0].get()]);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to asset loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cda78L)
  public FlowControl FUN_800cda78(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(!bent.combatant_144.isModelLoaded()) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800cdacc
    bent.combatant_144.charIndex_1a2 = -1;

    //LAB_800cdaf4
    bent.loadingAnimIndex_26e = 0;
    loadCombatantModelAndAnimation(bent.model_148, bent.combatant_144);

    //LAB_800cdb08
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stops rendering the battle stage and skybox")
  @Method(0x800cdb18L)
  public FlowControl scriptStopRenderingStage(final RunningScript<?> script) {
    this.setStageHasNoModel();
    this.setDontRenderStageBackground();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a new battle stage")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stageIndex", description = "The stage index")
  @Method(0x800cdb44L)
  public FlowControl scriptLoadStage(final RunningScript<?> script) {
    this.loadStage(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears combat scripts and sets the combat disabled flag")
  @Method(0x800cdb74L)
  public FlowControl scriptDisableCombat(final RunningScript<?> script) {
    this.combatDisabled_800c66b9 = true;

    //LAB_800cdbb8
    battleState_8006e398.disableBents();

    //LAB_800cdbe0
    this.playerBattleScript_800c66fc = null;

    //LAB_800cdc00
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdc1cL)
  public void FUN_800cdc1c(final ScriptState<BattleEntity27c> s1, final float x0, final float y0, final float z0, final float x1, final float y1, final float z1, final float a7, final int ticks) {
    final float dx = x1 - x0;
    final float dy = y1 - y0;
    final float dz = z1 - z0;

    final BattleEntity27c bent = s1.innerStruct_00;

    bent.movementTicks_cc = ticks;
    bent.movementDestination_e8.set(x1, y1, z1);
    bent.movementRemaining_d0.set(dx, dy, dz);

    // Fix for retail /0 bug
    if(ticks > 0) {
      bent.movementStep_dc.set(dx / ticks, 0.0f, dz / ticks);
    } else {
      bent.movementStep_dc.zero();
    }

    bent.movementStep_dc.y = FUN_80013404(a7, dy, ticks);
    bent.movementStepYAcceleration_f4 = a7;
  }

  @ScriptDescription("Allocates a weapon trail effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The battle object index to trail behind")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "dobjIndex", description = "The model index")
  @Method(0x800ce6a8L)
  public FlowControl scriptAllocateWeaponTrailEffect(final RunningScript<? extends BattleObject> script) {
    final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final WeaponTrailEffect3c trail = new WeaponTrailEffect3c(script.params_20[2].get(), parent);

    final ScriptState<EffectManagerData6c<EffectManagerParams.WeaponTrailType>> state = allocateEffectManager(
      "Weapon trail",
      script.scriptState_04,
      trail,
      new EffectManagerParams.WeaponTrailType()
    );

    state.innerStruct_00.params_10.colour_1c.set(0xff, 0x80, 0x60);

    //LAB_800ce804
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Rescales the weapon trail effect as it progresses")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The weapon trail effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "smallScalingFactor", description = "The scaling factor for the trailing end")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "largeScalingFactor", description = "The scaling factor for the leading end")
  @Method(0x800ce9b0L)
  public FlowControl scriptApplyWeaponTrailScaling(final RunningScript<?> script) {
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class).effect_44;
    trail.applyWeaponTrailScaling(trail.largestVertex_10, trail.smallestVertex_20, script.params_20[2].get() / (float)0x1000, script.params_20[1].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  /** Used at the end of Rose transform, lots during Albert transform */
  @ScriptDescription("Allocates a full-screen overlay effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "startR", description = "The starting red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "startG", description = "The starting green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "startB", description = "The starting blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "endR", description = "The ending red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "endG", description = "The ending green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "endB", description = "The ending blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks before the colour transition finishes")
  @Method(0x800cec8cL)
  public FlowControl scriptAllocateFullScreenOverlay(final RunningScript<? extends BattleObject> script) {
    final int r = script.params_20[1].get() << 8;
    final int g = script.params_20[2].get() << 8;
    final int b = script.params_20[3].get() << 8;
    final int fullR = script.params_20[4].get() << 8 & 0xffff; // Retail bug in violet dragon - overflow
    final int fullG = script.params_20[5].get() << 8 & 0xffff; //
    final int fullB = script.params_20[6].get() << 8 & 0xffff; //
    final int ticks = script.params_20[7].get() & 0xffff;

    final FullScreenOverlayEffect0e effect = new FullScreenOverlayEffect0e(r, g, b, fullR, fullG, fullB, ticks);
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("Full screen overlay rgb(%x, %x, %x) -> rgb(%x, %x, %x)".formatted(r, g, b, fullR, fullG, fullB), script.scriptState_04, effect);
    state.innerStruct_00.params_10.flags_00 = 0x5000_0000;

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Generates a random number")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The random number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "min", description = "The minimum value (inclusive)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "max", description = "The maximum value (inclusive)")
  @Method(0x800cee50L)
  public FlowControl scriptRand(final RunningScript<?> script) {
    final int min = script.params_20[1].get();
    final int max = script.params_20[2].get();
    final int range = max - min;

    final int rand;
    if(range >= 0) {
      rand = seed_800fa754.nextInt(range + 1) + min;
    } else {
      rand = -seed_800fa754.nextInt(-range + 1) + min;
    }

    script.params_20[0].set(rand);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a weapon trail effect's segment count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The segment count")
  @Method(0x800ceeccL)
  public FlowControl scriptSetWeaponTrailSegmentCount(final RunningScript<?> script) {
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class).effect_44;
    trail.setWeaponTrailSegmentCount(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Flameshot */
  @ScriptDescription("Renders a full-screen, coloured quad")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "translucency", description = "The translucency mode")
  @Method(0x800cef00L)
  public FlowControl scriptRenderColouredQuad(final RunningScript<?> script) {
    // Make sure effect fills the whole screen
    final float fullWidth = java.lang.Math.max(displayWidth_1f8003e0, RENDERER.window().getWidth() / (float)RENDERER.window().getHeight() * displayHeight_1f8003e4);
    final float extraWidth = fullWidth - displayWidth_1f8003e0;
    fullScreenEffect_800bb140.transforms.scaling(fullWidth, displayHeight_1f8003e4, 1.0f);
    fullScreenEffect_800bb140.transforms.transfer.set(-extraWidth / 2, 0.0f, 120.0f);

    //LAB_800139c4
    RENDERER.queueOrthoModel(RENDERER.plainQuads.get(Translucency.of(script.params_20[3].get() + 1)), fullScreenEffect_800bb140.transforms, QueuedModelStandard.class)
      .colour(script.params_20[0].get() / 255.0f, script.params_20[1].get() / 255.0f, script.params_20[2].get() / 255.0f);

    return FlowControl.CONTINUE;
  }

  @Method(0x800cf03cL)
  public int FUN_800cf03c(final EffectManagerData6c<?> manager, final Attachment18 attachment) {
    manager.params_10.trans_04.x += attachment._0c.x * attachment.direction_14;
    manager.params_10.trans_04.y += attachment._0c.y * attachment.direction_14;
    manager.params_10.trans_04.z += attachment._0c.z * attachment.direction_14;
    attachment.direction_14 = (byte)-attachment.direction_14;
    return 1;
  }

  @ScriptDescription("Adds an unknown, unused attachment to an effect. Seems to oscillate an effect's position back and forth by a certain amount.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @Method(0x800cf0b4L)
  public FlowControl FUN_800cf0b4(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Attachment18 attachment = manager.addAttachment(0, 0, this::FUN_800cf03c, new Attachment18());
    attachment.direction_14 = 1;

    if(script.params_20[4].get() != 0) {
      script.params_20[1].set(0);

      scriptGetPositionScalerAttachmentVelocity(script);

      final int p1 = script.params_20[1].get();
      attachment._0c.x = p1 * script.params_20[2].get() >> 16;
      attachment._0c.y = p1 * script.params_20[3].get() >> 16;
      attachment._0c.z = p1 * script.params_20[4].get() >> 16;
    } else {
      //LAB_800cf1e0
      attachment._0c.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    }

    //LAB_800cf1f8
    if(manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    //LAB_800cf218
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle object's local world matrix translation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cfcccL)
  public FlowControl scriptGetBobjLocalWorldMatrixTranslation(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_10;
    } else {
      //LAB_800cfd34
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800cfd40
    final MV lw = new MV();
    GsGetLw(model.modelParts_00[script.params_20[1].get()].coord2_04, lw);
    // This was multiplying vector (0, 0, 0) so I removed it
    script.params_20[2].set(Math.round(lw.transfer.x));
    script.params_20[3].set(Math.round(lw.transfer.y));
    script.params_20[4].set(Math.round(lw.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a bounding box dimension for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "axis", description = "The axis index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "size", description = "The size")
  @Method(0x800cfdf8L)
  public FlowControl scriptGetBentDimension(final RunningScript<?> script) {
    final BattleEntity27c bent = SCRIPTS.getObject(script.params_20[0].get(), BattleEntity27c.class);
    final int componentIndex = script.params_20[1].get();

    //LAB_800cfe54
    float largest = -Float.MAX_VALUE;
    float smallest = Float.MAX_VALUE;
    for(int animIndex = bent.model_148.partCount_98 - 1; animIndex >= 0; animIndex--) {
      final float component = bent.model_148.modelParts_00[animIndex].coord2_04.coord.transfer.get(componentIndex);

      if(largest < component) {
        largest = component;
        //LAB_800cfe84
      } else if(smallest > component) {
        smallest = component;
      }

      //LAB_800cfe90
    }

    //LAB_800cfe9c
    script.params_20[2].set(Math.round(largest - smallest));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800cfec8L)
  public FlowControl FUN_800cfec8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800cfed0L)
  public void setMtSeed(final long seed) {
    seed_800fa754.setSeed(seed ^ 0x75b_d924L);
  }

  @ScriptDescription("Sets the Mersenne Twister random number seed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "seed", description = "The seed")
  @Method(0x800cff24L)
  public FlowControl scriptSetMtSeed(final RunningScript<?> script) {
    this.setMtSeed(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle object model's part count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "partCount", description = "The part count")
  @Method(0x800d0124L)
  public FlowControl scriptGetBobjModelPartCount(final RunningScript<?> script) {
    final BattleObject data = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    if(BattleObject.EM__.equals(data.magic_00)) {
      script.params_20[1].set(((ModelEffect13c)((EffectManagerData6c<?>)data).effect_44).model_10.partCount_98);
    } else {
      //LAB_800d017c
      script.params_20[1].set(((BattleEntity27c)data).model_148.partCount_98);
    }

    //LAB_800d0194
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a projectile hit effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The effect count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @Method(0x800d0564L)
  public FlowControl scriptAllocateProjectileHitEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();
    final int r = script.params_20[2].get();
    final int g = script.params_20[3].get();
    final int b = script.params_20[4].get();

    final ProjectileHitEffect14 effect = new ProjectileHitEffect14(count, r, g, b);
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("ProjectileHitEffect14", script.scriptState_04, effect);

    //LAB_800d0980
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d09b8L)
  public FlowControl FUN_800d09b8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition sparks effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The effect count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @Method(0x800d0decL)
  public FlowControl scriptAllocateAdditionSparksEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();
    final int r = script.params_20[2].get();
    final int g = script.params_20[3].get();
    final int b = script.params_20[4].get();
    final int distance = script.params_20[5].get();
    final int ticks = script.params_20[6].get();

    final AdditionSparksEffect08 effect = new AdditionSparksEffect08(count, distance, ticks, r, g, b);
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("AdditionSparksEffect08", script.scriptState_04, effect);

    //LAB_800d1154
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition starburst effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "rayCount", description = "The number of rays")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "Controls how the effect behaves")
  @Method(0x800d19ecL)
  public FlowControl scriptAllocateAdditionStarburstEffect(final RunningScript<? extends BattleObject> script) {
    final int parentIndex = script.params_20[1].get();
    final int rayCount = script.params_20[2].get();
    final int type = script.params_20[3].get();

    final AdditionStarburstEffect10 effect = new AdditionStarburstEffect10(type, parentIndex, rayCount);
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("AdditionStarburstEffect10", script.scriptState_04, effect);

    //LAB_800d1c7c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d1cacL)
  public FlowControl FUN_800d1cac(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_800d1cac)", script.scriptState_04, null).index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d1cf4L)
  public FlowControl FUN_800d1cf4(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_800d1cf4)", script.scriptState_04, null).index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a radial gradient effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of subdivisions in the gradient")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The gradient type")
  @Method(0x800d2734L)
  public FlowControl scriptAllocateRadialGradientEffect(final RunningScript<? extends BattleObject> script) {
    final int circleSubdivisionModifier = script.params_20[1].get();
    final int type = script.params_20[2].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state = allocateEffectManager(
      "RadialGradientEffect14",
      script.scriptState_04,
      new RadialGradientEffect14(type, circleSubdivisionModifier),
      new EffectManagerParams.RadialGradientType()
    );

    //LAB_800d27b4
    state.innerStruct_00.params_10.scale_16.set(1.0f, 1.0f, 1.0f);
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a guard effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d2ff4L)
  public FlowControl scriptAllocateGuardEffect(final RunningScript<? extends BattleObject> script) {
    final GuardEffect06 effect = new GuardEffect06();
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("GuardEffect06", script.scriptState_04, effect);

    // Hack to make shield color default if counter overlay color is default
    // Otherwise, just use the overlay color. Maybe we can make shields toggleable later.
    final int rgb = Config.getCounterOverlayRgb();
    if(Config.changeAdditionOverlayRgb() && rgb != 0x2060d8) {
      state.innerStruct_00.params_10.colour_1c.set(rgb & 0xff, rgb >> 8 & 0xff, rgb >> 16 & 0xff);
    } else {
      state.innerStruct_00.params_10.colour_1c.set(255, 0, 0);
    }

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d3090L)
  public FlowControl FUN_800d3090(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a monster death effect effect for a monster battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spriteIndex", description = "Which sprite to use")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused in code but passed by scripts")
  @Method(0x800d34bcL)
  public FlowControl scriptAllocateMonsterDeathEffect(final RunningScript<? extends BattleObject> script) {
    final BattleEntity27c parent = SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class);
    final SpriteMetrics08 sprite = deffManager_800c693c.spriteMetrics_39c[script.params_20[2].get() & 0xff];

    final MonsterDeathEffect34 deathEffect = new MonsterDeathEffect34(parent, new GenericSpriteEffect24(0x5400_0000, sprite));
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("MonsterDeathEffect34", script.scriptState_04, deathEffect);

    //LAB_800d35cc
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /**
   * NOTE: changed param from reference to value
   */
  @Method(0x800d3910L)
  public int getCharDisplayWidth(final long chr) {
    //LAB_800d391c
    int charTableOffset;
    for(charTableOffset = 0; ; charTableOffset++) {
      if(asciiTable_800fa788[charTableOffset] == 0) {
        charTableOffset = 0;
        break;
      }

      if(chr == asciiTable_800fa788[charTableOffset]) {
        break;
      }
    }

    //LAB_800d3944
    //LAB_800d3948
    return 10 - charWidthAdjustTable_800fa7cc[charTableOffset];
  }

  @Method(0x800d3968L)
  public int[] setAdditionNameDisplayCoords(final int addition) {
    final String additionName = additionNames_800fa8d4[addition];

    int additionDisplayWidth = 0;
    //LAB_800d39b8
    for(int i = 0; i < additionName.length(); i++) {
      additionDisplayWidth += this.getCharDisplayWidth(additionName.charAt(i));
    }

    //LAB_800d39ec
    return new int[] {144 - additionDisplayWidth, 64};
  }

  @ScriptDescription("Allocates an addition name display script state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1", description = "Unknown, -1 will deallocate next tick")
  @Method(0x800d3d74L)
  public FlowControl scriptAllocateAdditionScript(final RunningScript<?> script) {
    if(script.params_20[1].get() == -1) {
      this._800faa9d = 0;
    } else {
      //LAB_800d3dc0
      final AdditionNameTextEffect1c additionStruct = new AdditionNameTextEffect1c();
      final int addition = gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19;
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionNameTextEffect1c", additionStruct);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker((s, effect) -> additionStruct.tickAdditionNameEffect(s, this._800faa9d));
      final String additionName = additionNames_800fa8d4[addition];

      //LAB_800d3e5c
      //LAB_800d3e7c
      additionStruct.additionId_02 = addition;
      additionStruct.length_08 = additionName.length();
      additionStruct.positionMovement_0c = 120;
      additionStruct.renderer_14 = additionStruct::renderAdditionNameChar;
      additionStruct.ptr_18 = new AdditionCharEffectData0c[additionStruct.length_08];
      Arrays.setAll(additionStruct.ptr_18, i -> new AdditionCharEffectData0c());
      this._800faa9d = 1;

      final int[] displayOffset = this.setAdditionNameDisplayCoords(addition);
      int charPosition = -160;
      int displayOffsetX = displayOffset[0];
      final int displayOffsetY = displayOffset[1];

      //LAB_800d3f18
      for(int charIdx = 0; charIdx < additionName.length(); charIdx++) {
        final AdditionCharEffectData0c charStruct = additionStruct.ptr_18[charIdx];
        charStruct.scrolling_00 = 1;
        charStruct.dupes_02 = 8;
        charStruct.position_04 = charPosition;
        charStruct.offsetY_06 = displayOffsetY;
        charStruct.offsetX_08 = displayOffsetX;
        charStruct.offsetY_0a = displayOffsetY;
        displayOffsetX += this.getCharDisplayWidth(additionName.charAt(charIdx));
        charPosition -= 80;
      }
    }

    //LAB_800d3f70
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition SP text display effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800d4338L)
  public FlowControl scriptAllocateSpTextEffect(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    final int s3 = script.params_20[1].get();

    if(s2 == -1) {
      this._800faa94 = 0;
    } else {
      //LAB_800d4388
      final SpTextEffect40 s1 = new SpTextEffect40();
      final ScriptState<SpTextEffect40> state = SCRIPTS.allocateScriptState("SpTextEffect40", s1);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker((s, effect) -> s1.tickSpTextEffect(s, this._800faa94));

      s1.value_08 = s2;
      s1.destX_1c = this._800faa90 << 8;

      if(s3 == 1) {
        this._800faa92 = 0;
        this._800faa94 = s3;
        s1._01 = 0;
        s1.destX_1c = 0xffff6e00;
        this._800faa90 = -146;
      } else {
        //LAB_800d4470
        this._800faa92++;
        s1._01 = this._800faa92;
      }

      //LAB_800d448c
      s1.stepX_2c = (s1.destX_1c - s1.x_0c) / 14;

      //LAB_800d44dc
      for(int i = 0; i < 8; i++) {
        s1.charArray_3c[i].x_00 = s1.x_0c;
        s1.charArray_3c[i].y_04 = s1.y_10;
      }

      final int strLen = String.valueOf(s1.value_08).length();

      final int v1;
      if(s1._01 == 0) {
        v1 = strLen + 2;
      } else {
        v1 = strLen + 3;
      }

      //LAB_800d453c
      this._800faa90 = (s1.destX_1c >> 8) + v1 * 8 - 3;
    }

    script.params_20[1].set(0);

    //LAB_800d4560
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition name effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "totalSp", description = "The amount of SP gained from this addition")
  @Method(0x800d4580L)
  public FlowControl scriptAllocateAdditionNameEffect(final RunningScript<?> script) {
    final int totalSp = script.params_20[0].get();
    if(totalSp != -1) {
      final AdditionNameTextEffect1c s0 = new AdditionNameTextEffect1c();
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionScriptData1c", s0);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker((s, effect) -> s0.tickAdditionNameEffect(s, this._800faa9d));
      s0.ptr_18 = new AdditionCharEffectData0c[] {new AdditionCharEffectData0c()};
      s0.positionMovement_0c = 40;
      s0.renderer_14 = s0::renderAdditionSpGain;
      s0.length_08 = 1;
      s0.totalSp_10 = totalSp;
      final AdditionCharEffectData0c struct = s0.ptr_18[0];
      struct.scrolling_00 = 1;
      struct.dupes_02 = 8;
      struct.position_04 = -160;
      struct.offsetY_06 = 96;
      struct.offsetX_08 = 144 - (String.valueOf(totalSp).length() + 4) * 8;
      struct.offsetY_0a = 96;
    }

    //LAB_800d46bc
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates the button press HUD for additions")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The button press type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "translucency", description = "The translucency mode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "brightness", description = "The brightness")
  @Method(0x800d46d4L)
  public FlowControl scriptRenderButtonPressHudElement(final RunningScript<?> script) {
    renderButtonPressHudElement1(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), Translucency.of(script.params_20[3].get()), script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Causes the battle camera projection plane distance to begin moving")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera should move")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "newDistance", description = "The new projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames it should take to change the distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ1")
  @Method(0x800d8decL)
  public FlowControl scriptMoveCameraProjectionPlane(final RunningScript<?> script) {
    final int mode = script.params_20[0].get();
    final float newProjectionPlaneDistance = script.params_20[1].get();
    final int projectionPlaneChangeFrames = script.params_20[2].get();
    final int stepZ1 = script.params_20[3].get();

    LOGGER.info(CAMERA, "[CAMERA] scriptMoveCameraProjectionPlane mode=%d, new=%f, frames=%d, s4=%d", mode, newProjectionPlaneDistance, projectionPlaneChangeFrames, stepZ1);

    final BattleCamera cam = this.camera_800c67f0;
    cam.projectionPlaneDistance_100 = getProjectionPlaneDistance();
    cam.newProjectionPlaneDistance_104 = newProjectionPlaneDistance;
    cam.projectionPlaneChanging_118 = true;

    if(newProjectionPlaneDistance < getProjectionPlaneDistance()) {
      //LAB_800d8e64
      cam.projectionPlaneMovementDirection_114 = 1;
    } else {
      cam.projectionPlaneMovementDirection_114 = 0;
    }

    //LAB_800d8e68
    final float projectionPlaneDelta = Math.abs(newProjectionPlaneDistance - getProjectionPlaneDistance());
    cam.projectionPlaneChangeFrames_108 = projectionPlaneChangeFrames;
    if(mode == 0) {
      cam.projectionPlaneDistanceStep_10c = projectionPlaneDelta / projectionPlaneChangeFrames;
      cam.projectionPlaneDistanceStepAcceleration_110 = 0.0f;
    } else {
      //LAB_800d8ea0
      final FloatRef initialStepZ = new FloatRef();
      final FloatRef finalStepZ = new FloatRef();
      cam.setInitialAndFinalCameraVelocities(mode - 1, stepZ1, projectionPlaneDelta, projectionPlaneChangeFrames, initialStepZ, finalStepZ);
      cam.projectionPlaneDistanceStep_10c = initialStepZ.get();
      cam.projectionPlaneDistanceStepAcceleration_110 = (finalStepZ.get() - initialStepZ.get()) / projectionPlaneChangeFrames;
    }

    //LAB_800d8eec
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Resets battle camera movement")
  @Method(0x800dabccL)
  public FlowControl scriptResetCameraMovement(final RunningScript<?> script) {
    this.camera_800c67f0.resetCameraMovement();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800dac20L)
  public FlowControl FUN_800dac20(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    // Three Executioners instakill sends a bad param for scriptIndex (0xa0), but this param isn't used for the camera function they're calling so we can just pass null
    // File 5316/1[addr 0x4078]
    // Parameters:
    //   Op param: 0x21
    //   0: script[0x1021] 0x0
    //   1: script[0x1023] 0xffe8e600
    //   2: script[0x1025] 0xfff76300
    //   3: script[0x1026] 0x0
    //   4: script[0x1027] 0xa0
    final BattleObject bobj;
    if(script.params_20[4].get() < 72) {
      bobj = SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class);
    } else {
      bobj = null;
    }

    this.camera_800c67f0.FUN_800dac70(script.params_20[0].get(), x, y, z, bobj);
    return FlowControl.CONTINUE;
  }

  /** Note: sometimes nonsense values are passed for the script index (assassin cock passes -2 during yell attack) */
  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db034L)
  public FlowControl FUN_800db034(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    // Three Executioners instakill sends a bad param for scriptIndex (0xc8), but this param isn't used for the camera function they're calling so we can just pass null
    // File 5316/1[addr 0x4078]
    // Parameters:
    //   Op param: 0x22
    //   0: script[0x1029] 0x0
    //   1: script[0x102b] 0xfff59900
    //   2: script[0x102d] 0xfff8f300
    //   3: script[0x102e] 0x0
    //   4: script[0x102f] 0xc8
    final BattleObject bobj;
    if(script.params_20[4].get() < 72) {
      bobj = SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class);
    } else {
      bobj = null;
    }

    this.camera_800c67f0.FUN_800db084(script.params_20[0].get(), x, y, z, bobj);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db460L)
  public FlowControl FUN_800db460(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    this.camera_800c67f0.FUN_800db4ec(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db574L)
  public FlowControl FUN_800db574(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    this.camera_800c67f0.FUN_800db600(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialStepZ, 8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "finalStepZ, 8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db688L)
  public FlowControl FUN_800db688(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;
    float initialStepZ = script.params_20[4].get() / (float)0x100;
    float finalStepZ = script.params_20[5].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
      initialStepZ = MathHelper.psxDegToRad(initialStepZ);
      finalStepZ = MathHelper.psxDegToRad(finalStepZ);
    }

    this.camera_800c67f0.FUN_800db714(script.params_20[0].get(), x, y, z, initialStepZ, finalStepZ, script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialStepZ, 8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "finalStepZ, 8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db79cL)
  public FlowControl FUN_800db79c(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;
    float initialStepZ = script.params_20[4].get() / (float)0x100;
    float finalStepZ = script.params_20[5].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
      initialStepZ = MathHelper.psxDegToRad(initialStepZ);
      finalStepZ = MathHelper.psxDegToRad(finalStepZ);
    }

    this.camera_800c67f0.FUN_800db828(script.params_20[0].get(), x, y, z, initialStepZ, finalStepZ, script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepSmoothingMode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ", description = "8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db8b0L)
  public FlowControl FUN_800db8b0(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;
    float stepZ = script.params_20[6].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
      stepZ = MathHelper.psxDegToRad(stepZ);
    }

    this.camera_800c67f0.FUN_800db950(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), stepZ, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepSmoothingMode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ", description = "8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db9e0L)
  public FlowControl FUN_800db9e0(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;
    float stepZ = script.params_20[6].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
      stepZ = MathHelper.psxDegToRad(stepZ);
    }

    this.camera_800c67f0.FUN_800dba80(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), stepZ, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Whether or not the camera is currently moving")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint moving, 1 = refpoint moving")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "isMoving")
  @Method(0x800dbb10L)
  public FlowControl scriptIsCameraMoving(final RunningScript<?> script) {
    final BattleCamera cam = this.camera_800c67f0;
    final int type = script.params_20[0].get();
    final boolean moving;
    if(type == 0) {
      //LAB_800dbb3c
      moving = cam.viewpointMoving_122;
    } else if(type == 1) {
      //LAB_800dbb48
      moving = cam.refpointMoving_123;
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    script.params_20[1].set(moving ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @Method(0x800dbb9cL)
  public FlowControl FUN_800dbb9c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Sets the viewport twist")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "twist", description = "The new viewpoint twist")
  @Method(0x800dbc2cL)
  public FlowControl scriptSetViewportTwist(final RunningScript<?> script) {
    this.camera_800c67f0.rview2_00.viewpointTwist_18 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the camera viewpoint and/or refpoint Z acceleration to 0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "Bitfield, 0x1 = viewpoint, 0x2 = refpoint")
  @Method(0x800dbc80L)
  public FlowControl scriptStopCameraZAcceleration(final RunningScript<?> script) {
    final int type = script.params_20[0].get();

    if((type & UPDATE_VIEWPOINT) != 0) {
      this.camera_800c67f0.stepZAcceleration_e4 = 0.0f;
      this.camera_800c67f0.stepZAcceleration_b4 = 0.0f;
    }

    //LAB_800dbca8
    if((type & UPDATE_REFPOINT) != 0) {
      this.camera_800c67f0.stepZAcceleration_70 = 0.0f;
      this.camera_800c67f0.stepZAcceleration_40 = 0.0f;
    }

    //LAB_800dbcc0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets camera projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distance", description = "The projection plane distance")
  @Method(0x800dbcc8L)
  public FlowControl scriptSetCameraProjectionPlaneDistance(final RunningScript<?> script) {
    LOGGER.info(CAMERA, "[CAMERA] scriptSetCameraProjectionPlaneDistance distance=%d", script.params_20[0].get());

    final BattleCamera cam = this.camera_800c67f0;
    cam.projectionPlaneDistance_100 = script.params_20[0].get();
    cam.projectionPlaneChangeFrames_108 = 0;
    cam.projectionPlaneChanging_118 = true;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the camera projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "distance", description = "The projection plane distance")
  @Method(0x800dbcfcL)
  public FlowControl scriptGetProjectionPlaneDistance(final RunningScript<?> script) {
    script.params_20[0].set(Math.round(getProjectionPlaneDistance()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Calculates multiple different absolute or relative camera values")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint, 1 = refpoint")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "Which calculation to perform")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "component", description = "0 = X, 1 = Y, 2 = Z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out", description = "If mode is even, values are 8-bit fixed-point; if mode is odd, values are PSX degrees")
  @Method(0x800dc2d8L)
  public FlowControl scriptCalculateCameraValue(final RunningScript<?> script) {
    LOGGER.info(CAMERA, "[CAMERA] Calc val: use refpoint=%b, FUN index=%d, component=%d, script index=%d", script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());

    float value = this.camera_800c67f0.calculateCameraValue(script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), SCRIPTS.getObject(script.params_20[3].get(), BattleObject.class));

    // Odd funcs operate on angles, but Z values in these methods are delta vector mag, not angles
    if((script.params_20[1].get() & 1) != 0 && script.params_20[2].get() != 2) {
      value = MathHelper.radToPsxDeg(value);
    }

    script.params_20[4].set((int)value);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stops camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint, 1 = refpoint")
  @Method(0x800dcb84L)
  public FlowControl scriptStopCameraMovement(final RunningScript<?> script) {
    final BattleCamera cam = this.camera_800c67f0;
    final int type = script.params_20[0].get();

    LOGGER.info(CAMERA, "[CAMERA] type=%d", type);

    if((type & UPDATE_VIEWPOINT) != 0) {
      cam.viewpointCallbackIndex_120 = 0;
      cam.viewpointMoving_122 = false;
      cam.flags_11c &= ~UPDATE_VIEWPOINT;
    }

    //LAB_800dcbbc
    if((type & UPDATE_REFPOINT) != 0) {
      cam.refpointCallbackIndex_121 = 0;
      cam.refpointMoving_123 = false;
      cam.flags_11c &= ~UPDATE_REFPOINT;
    }

    //LAB_800dcbe4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the camera wobble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames to perform the wobble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "framesUntilWobble", description = "The number of frames until the wobble starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Wobble offset X")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Wobble offset Y")
  @Method(0x800dcbecL)
  public FlowControl scriptWobbleCamera(final RunningScript<?> script) {
    this.camera_800c67f0.setWobble(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    getScreenOffset(this.camera_800c67f0.screenOffset_800c67bc);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ddac8L)
  public void loadModelTmd(final Model124 model, final CContainer extTmd) {
    final Vector3f sp0x18 = new Vector3f(model.coord2_14.coord.transfer);

    //LAB_800ddb18
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    final TmdWithId tmdWithId = extTmd.tmdPtr_00;
    final Tmd tmd = tmdWithId.tmd;

    final int count = tmd.header.nobj;
    model.modelParts_00 = new ModelPart10[count];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    model.tpage_108 = (int)((tmdWithId.id & 0xffff_0000L) >>> 11);
    initObjTable2(model.modelParts_00);
    GsInitCoordinate2(null, model.coord2_14);

    //LAB_800ddc0c
    for(int i = 0; i < count; i++) {
      if((tmd.header.flags & 0x2) != 0) { // CTMD, no longer used
        model.modelParts_00[i].tmd_08 = tmd.objTable[i];
      } else {
        final ModelPart10 dobj2 = new ModelPart10();
        dobj2.tmd_08 = tmd.objTable[i];
        model.modelParts_00[i].tmd_08 = dobj2.tmd_08;
      }

      //LAB_800ddc34
    }

    //LAB_800ddc54
    //LAB_800ddc64
    for(int i = 0; i < count; i++) {
      model.modelParts_00[i].coord2_04.super_ = model.coord2_14;
    }

    //LAB_800ddc80
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;
    model.zOffset_a0 = 0;
    model.coord2_14.coord.transfer.set(sp0x18);

    if((tmd.header.flags & 0x2) == 0) {
      adjustModelUvs(model);
    }

    //LAB_800ddce8
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowType_cc = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();

    TmdObjLoader.fromModel("BattleModel", model);
  }

  @Method(0x800e45c0L)
  public void FUN_800e45c0(final Vector3f out, final Vector3f in) {
    final float angle = MathHelper.atan2(in.x, in.z);
    final float sin = MathHelper.sin(-angle);
    final float cos = MathHelper.cosFromSin(sin, -angle);
    out.x = MathHelper.atan2(-in.y, cos * in.z - sin * in.x); // Angle from the XZ plane
    out.y = angle; // Angle from the X axis
    out.z = 0;
  }

  @Method(0x800e4674L)
  public Vector3f FUN_800e4674(final Vector3f out, final Vector3f rotation) {
    return out.set(0.0f, 0.0f, 1.0f).mul(new Matrix3f().rotationZYX(rotation.z, rotation.y, rotation.x));
  }

  @Method(0x800e46c8L)
  public void resetLights() {
    final BattleLightStruct64 v1 = this._800c6930;
    v1.colour_00.set(0.5f, 0.5f, 0.5f);

    final BttlLightStruct84 a0 = this.lights_800c692c[0];
    a0.light_00.direction_00.set(0.0f, 1.0f, 0.0f);
    a0.light_00.r_0c = 0.5f;
    a0.light_00.g_0d = 0.5f;
    a0.light_00.b_0e = 0.5f;
    a0._10.typeAndFlags_00 = 0;
    a0._4c.typeAndFlags_00 = 0;

    //LAB_800e4720
    this.lights_800c692c[1].clear();
    this.lights_800c692c[2].clear();
  }

  @ScriptDescription("Resets battle lighting")
  @Method(0x800e473cL)
  public FlowControl scriptResetLights(final RunningScript<?> script) {
    this.resetLights();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e475cL)
  public void setLightDirection(final int lightIndex, final float x, final float y, final float z) {
    final BttlLightStruct84 light = this.lights_800c692c[lightIndex];
    light.light_00.direction_00.set(x, y, z);
    light._10.typeAndFlags_00 = 0;
  }

  @ScriptDescription("Sets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4788L)
  public FlowControl scriptSetLightDirection(final RunningScript<?> script) {
    this.setLightDirection(script.params_20[0].get(), script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e47c8L)
  public FlowControl scriptGetLightDirection(final RunningScript<?> script) {
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set((int)(light.light_00.direction_00.x * 0x1000));
    script.params_20[2].set((int)(light.light_00.direction_00.y * 0x1000));
    script.params_20[3].set((int)(light.light_00.direction_00.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4824L)
  public void FUN_800e4824(final int lightIndex, final float x, final float y, final float z) {
    final Vector3f rotation = new Vector3f();
    this.FUN_800e4674(rotation, new Vector3f(x, y, z));
    final BttlLightStruct84 light = this.lights_800c692c[lightIndex];
    light.light_00.direction_00.set(rotation);
    light._10.typeAndFlags_00 = 0;
  }

  @ScriptDescription("Unknown, sets the battle light direction using the vector (0, 0, 1) rotated by ZYX")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e48a8L)
  public FlowControl FUN_800e48a8(final RunningScript<?> script) {
    this.FUN_800e4824(script.params_20[0].get(), MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets the battle light direction using the vector (0, 0, 1) rotated by ZYX")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e48e8L)
  public FlowControl FUN_800e48e8(final RunningScript<?> script) {
    final Vector3f rotation = new Vector3f();
    this.FUN_800e45c0(rotation, this.lights_800c692c[script.params_20[0].get()].light_00.direction_00);
    script.params_20[1].set(MathHelper.radToPsxDeg(rotation.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(rotation.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(rotation.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "Either a light index or battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4964L)
  public FlowControl FUN_800e4964(final RunningScript<?> script) {
    final Vector3f rotation = new Vector3f();

    final int index = script.params_20[1].get();
    if(index != -1) {
      //LAB_800e49c0
      if(index >= 1 && index <= 3) {
        this.FUN_800e45c0(rotation, this.lights_800c692c[index - 1].light_00.direction_00);
      } else {
        //LAB_800e49f4
        final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[index].innerStruct_00;
        rotation.x = bent.model_148.coord2_14.transforms.rotate.x;
        rotation.z = bent.model_148.coord2_14.transforms.rotate.z;
      }
    }

    //LAB_800e4a34
    //LAB_800e4a38
    final Vector3f direction = new Vector3f();
    rotation.x += MathHelper.psxDegToRad(script.params_20[2].get());
    rotation.y += MathHelper.psxDegToRad(script.params_20[3].get());
    rotation.z += MathHelper.psxDegToRad(script.params_20[4].get());
    this.FUN_800e4674(direction, rotation);
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    light.light_00.direction_00.set(direction);
    light._10.typeAndFlags_00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT, name = "indexAndX", description = "In: either a light index or battle entity index, out: the X direction (PSX degrees)") // why
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4abcL)
  public FlowControl FUN_800e4abc(final RunningScript<?> script) {
    final int s1 = script.params_20[1].get();

    final Vector3f sp0x10 = new Vector3f();
    this.FUN_800e45c0(sp0x10, this.lights_800c692c[script.params_20[0].get()].light_00.direction_00);

    final Vector3f s0;
    if(s1 >= 1 && s1 <= 3) {
      s0 = new Vector3f();
      this.FUN_800e45c0(s0, this.lights_800c692c[s1 - 1].light_00.direction_00);
    } else {
      //LAB_800e4b40
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[s1].innerStruct_00;
      s0 = bent.model_148.coord2_14.transforms.rotate;
    }

    //LAB_800e4b64
    script.params_20[1].set(MathHelper.radToPsxDeg(sp0x10.x - s0.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(sp0x10.y - s0.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(sp0x10.z - s0.z));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4bc0L)
  public void setBattleLightColour(final int lightIndex, final float r, final float g, final float b) {
    final BttlLightStruct84 light = this.lights_800c692c[lightIndex];
    light.light_00.r_0c = r;
    light.light_00.g_0d = g;
    light.light_00.b_0e = b;
    light._4c.typeAndFlags_00 = 0;
  }

  @ScriptDescription("Sets the battle light colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (8-bit fixed-point)")
  @Method(0x800e4c10L)
  public FlowControl scriptSetBattleLightColour(final RunningScript<?> script) {
    this.setBattleLightColour(script.params_20[0].get(), script.params_20[1].get() / (float)0x100, script.params_20[2].get() / (float)0x100, script.params_20[3].get() / (float)0x100);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle light colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The red channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The green channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (8-bit fixed-point)")
  @Method(0x800e4c90L)
  public FlowControl scriptGetBattleLightColour(final RunningScript<?> script) {
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set((int)(light.light_00.r_0c * 0x100));
    script.params_20[2].set((int)(light.light_00.g_0d * 0x100));
    script.params_20[3].set((int)(light.light_00.b_0e * 0x100));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4cf8L)
  public void setBattleBackgroundLight(final float r, final float g, final float b) {
    final BattleLightStruct64 v0 = this._800c6930;
    v0.colour_00.set(r, g, b);
    v0._24 = 0;
    GTE.setBackgroundColour(r, g, b);
  }

  @ScriptDescription("Sets the battle light background colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (12-bit fixed-point)")
  @Method(0x800e4d2cL)
  public FlowControl scriptSetBattleBackgroundLightColour(final RunningScript<?> script) {
    this.setBattleBackgroundLight(script.params_20[0].get() / 4096.0f, script.params_20[1].get() / 4096.0f, script.params_20[2].get() / 4096.0f);
    this._800c6930._24 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle light background colour")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The red channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The green channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (12-bit fixed-point)")
  @Method(0x800e4db4L)
  public FlowControl scriptGetBattleBackgroundLightColour(final RunningScript<?> script) {
    final BattleLightStruct64 v0 = this._800c6930;
    script.params_20[0].set((int)(v0.colour_00.x * 0x1000));
    script.params_20[1].set((int)(v0.colour_00.y * 0x1000));
    script.params_20[2].set((int)(v0.colour_00.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e4dfcL)
  public FlowControl FUN_800e4dfc(final RunningScript<?> script) {
    this.lights_800c692c[script.params_20[0].get()]._10.typeAndFlags_00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e4e2cL)
  public FlowControl FUN_800e4e2c(final RunningScript<?> script) {
    return this.lights_800c692c[script.params_20[0].get()]._10.typeAndFlags_00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out")
  @Method(0x800e4e64L)
  public FlowControl FUN_800e4e64(final RunningScript<?> script) {
    script.params_20[1].set(this.lights_800c692c[script.params_20[0].get()]._10.typeAndFlags_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e4ea0L)
  public FlowControl FUN_800e4ea0(final RunningScript<?> script) {
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    final int ticks = script.params_20[4].get();
    final BttlLightStruct84Sub38 t0 = light._10;

    t0.typeAndFlags_00 = 0;
    t0.directionOrColour_04.set(light.light_00.direction_00);
    t0.directionOrColourDest_28.set(MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    t0.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      t0.directionOrColourSpeed_10.x = (t0.directionOrColourDest_28.x - t0.directionOrColour_04.x) / ticks;
      t0.directionOrColourSpeed_10.y = (t0.directionOrColourDest_28.y - t0.directionOrColour_04.y) / ticks;
      t0.directionOrColourSpeed_10.z = (t0.directionOrColourDest_28.z - t0.directionOrColour_04.z) / ticks;
      t0.directionOrColourAcceleration_1c.zero();
      t0.typeAndFlags_00 = 0xa001;
    }

    //LAB_800e4f98
    return FlowControl.CONTINUE;
  }

  /** Used in gates of heaven */
  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e4fa0L)
  public FlowControl FUN_800e4fa0(final RunningScript<?> script) {
    final float x = MathHelper.psxDegToRad(script.params_20[1].get());
    final float y = MathHelper.psxDegToRad(script.params_20[2].get());
    final float z = MathHelper.psxDegToRad(script.params_20[3].get());
    final int ticks = script.params_20[4].get();

    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    final Vector3f sp0x10 = new Vector3f();
    this.FUN_800e45c0(sp0x10, light.light_00.direction_00);
    light._10.typeAndFlags_00 = 0;

    final BttlLightStruct84Sub38 a3 = light._10;
    a3.directionOrColour_04.set(sp0x10);
    a3.directionOrColourDest_28.set(x, y, z);
    a3.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      a3.directionOrColourAcceleration_1c.zero();
      a3.directionOrColourSpeed_10.x = (x - a3.directionOrColour_04.x) / ticks;
      a3.directionOrColourSpeed_10.y = (y - a3.directionOrColour_04.y) / ticks;
      a3.directionOrColourSpeed_10.z = (z - a3.directionOrColour_04.z) / ticks;
      a3.typeAndFlags_00 = 0xc001;
    }

    //LAB_800e50c0
    //LAB_800e50c4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightOrBentIndex", description = "Either a light index or battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e50e8L)
  public FlowControl FUN_800e50e8(final RunningScript<?> script) {
    final int lightIndex = script.params_20[0].get();
    final int lightOrBentIndex = script.params_20[1].get();
    final int x = script.params_20[2].get();
    final int y = script.params_20[3].get();
    final int z = script.params_20[4].get();
    final int ticks = script.params_20[5].get();

    final Vector3f sp0x10 = new Vector3f();
    this.FUN_800e45c0(sp0x10, this.lights_800c692c[lightIndex].light_00.direction_00);

    final BttlLightStruct84Sub38 s0 = this.lights_800c692c[lightIndex]._10;
    s0.typeAndFlags_00 = 0;
    s0.directionOrColour_04.set(sp0x10);

    if(lightOrBentIndex >= 1 && lightOrBentIndex <= 3) {
      final Vector3f sp0x18 = new Vector3f();
      this.FUN_800e45c0(sp0x18, this.lights_800c692c[lightOrBentIndex - 1].light_00.direction_00);
      s0.directionOrColourDest_28.set(sp0x18);
    } else {
      //LAB_800e51e8
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[lightOrBentIndex].innerStruct_00;
      s0.directionOrColourDest_28.set(bent.model_148.coord2_14.transforms.rotate);
    }

    //LAB_800e522c
    s0.ticksRemaining_34 = ticks;
    s0.directionOrColourDest_28.add(x, y, z);

    if(ticks > 0) {
      s0.typeAndFlags_00 = 0xc001;
      s0.directionOrColourSpeed_10.set(s0.directionOrColourDest_28).sub(s0.directionOrColour_04).div(ticks);
      s0.directionOrColourAcceleration_1c.zero();
    }

    //LAB_800e52c8
    //LAB_800e52cc
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z1")
  @Method(0x800e52f8L)
  public FlowControl FUN_800e52f8(final RunningScript<?> script) {
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    final Vector3f sp0x10 = new Vector3f();
    this.FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 v1 = light._10;
    v1.typeAndFlags_00 = 0x4001;
    v1.directionOrColour_04.set(sp0x10);
    v1.directionOrColourSpeed_10.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    v1.directionOrColourAcceleration_1c.set(script.params_20[4].get() / (float)0x1000, script.params_20[5].get() / (float)0x1000, script.params_20[6].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c's script index")
  @Method(0x800e540cL)
  public FlowControl FUN_800e540c(final RunningScript<?> script) {
    final int bentIndex = script.params_20[1].get();
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];

    final Vector3f sp0x10 = new Vector3f();
    this.FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 a0_0 = light._10;
    a0_0.typeAndFlags_00 = 0x4002;
    light.scriptIndex_48 = bentIndex;

    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;
    a0_0.directionOrColour_04.set(sp0x10).sub(bent.model_148.coord2_14.transforms.rotate);
    a0_0.directionOrColourSpeed_10.zero();
    a0_0.directionOrColourAcceleration_1c.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e54f8L)
  public FlowControl FUN_800e54f8(final RunningScript<?> script) {
    this.lights_800c692c[script.params_20[0].get()]._4c.typeAndFlags_00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e5528L)
  public FlowControl FUN_800e5528(final RunningScript<?> script) {
    return this.lights_800c692c[script.params_20[0].get()]._4c.typeAndFlags_00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x800e5560L)
  public FlowControl FUN_800e5560(final RunningScript<?> script) {
    script.params_20[1].set(this.lights_800c692c[script.params_20[0].get()]._4c.typeAndFlags_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "The number of ticks")
  @Method(0x800e559cL)
  public FlowControl FUN_800e559c(final RunningScript<?> script) {
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    final int ticks = script.params_20[4].get();

    final BttlLightStruct84Sub38 t0 = light._4c;
    t0.typeAndFlags_00 = 0;
    t0.directionOrColour_04.x = light.light_00.r_0c;
    t0.directionOrColour_04.y = light.light_00.g_0d;
    t0.directionOrColour_04.z = light.light_00.b_0e;
    t0.directionOrColourDest_28.set(script.params_20[1].get() / (float)0x80, script.params_20[2].get() / (float)0x80, script.params_20[3].get() / (float)0x80);
    t0.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      t0.directionOrColourAcceleration_1c.zero();
      t0.directionOrColourSpeed_10.set(t0.directionOrColourDest_28).sub(t0.directionOrColour_04).div(ticks);
      t0.typeAndFlags_00 = 0x8001;
    }

    //LAB_800e5694
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z1")
  @Method(0x800e569cL)
  public FlowControl FUN_800e569c(final RunningScript<?> script) {
    final BttlLightStruct84 light = this.lights_800c692c[script.params_20[0].get()];
    final BttlLightStruct84Sub38 v1 = light._4c;
    v1.typeAndFlags_00 = 0;
    v1.directionOrColour_04.set(light.light_00.r_0c, light.light_00.g_0d, light.light_00.b_0e);
    v1.directionOrColourSpeed_10.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    v1.directionOrColourAcceleration_1c.set(script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());

    if(v1.ticksRemaining_34 > 0) {
      v1.typeAndFlags_00 = 0x1;
    }

    //LAB_800e5760
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5768L)
  public void applyStageAmbiance(final StageAmbiance4c ambiance) {
    this.setBattleBackgroundLight(ambiance.ambientColour_00.x, ambiance.ambientColour_00.y, ambiance.ambientColour_00.z);

    final BattleLightStruct64 v1 = this._800c6930;
    if(ambiance._0e > 0) {
      v1.colour1_0c.set(ambiance.ambientColour_00);
      v1.colour2_18.set(ambiance._06);
      v1._24 = 3;
      v1._2c = (short)ambiance._0c;
      v1._2e = (short)ambiance._0e;
    } else {
      //LAB_800e5808
      v1._24 = 0;
    }

    //LAB_800e5814
    //LAB_800e5828
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 a1 = this.lights_800c692c[i];
      final BattleStruct14 a0 = ambiance._10[i];
      a1.light_00.direction_00.set(a0.lightDirection_00);
      a1.light_00.r_0c = a0.lightColour_0a.x / (float)0x100;
      a1.light_00.g_0d = a0.lightColour_0a.y / (float)0x100;
      a1.light_00.b_0e = a0.lightColour_0a.z / (float)0x100;

      if(a0.x_06 != 0 || a0.y_08 != 0) {
        a1._10.typeAndFlags_00 = 0x3;
        a1._10.directionOrColour_04.set(a1.light_00.direction_00);
        a1._10.directionOrColourSpeed_10.set(a0.x_06, a0.y_08, 0);
      } else {
        //LAB_800e58cc
        a1._10.typeAndFlags_00 = 0;
      }

      //LAB_800e58d0
      if(a0.y_12 != 0) {
        a1._4c.typeAndFlags_00 = 0x3;
        a1._4c.directionOrColour_04.set(a1.light_00.r_0c, a1.light_00.g_0d, a1.light_00.b_0e);
        a1._4c.directionOrColourSpeed_10.set(a0._0d.x / (float)0x100, a0._0d.y / (float)0x100, a0._0d.z / (float)0x100);
        a1._4c.directionOrColourDest_28.x = a0.x_10;
        a1._4c.directionOrColourDest_28.y = a0.y_12;
      } else {
        //LAB_800e5944
        a1._4c.typeAndFlags_00 = 0;
      }

      //LAB_800e5948
    }
  }

  @ScriptDescription("Applies the current battle stage's ambiance (including Dragoon Space)")
  @Method(0x800e596cL)
  public FlowControl scriptApplyStageAmbiance(final RunningScript<?> script) {
    final int dragoonStage = this.currentStage_800c66a4 - 71;

    if(dragoonStage >= 0 && dragoonStage < 8) {
      this.applyStageAmbiance(deffManager_800c693c.dragoonSpaceAmbiance_98[dragoonStage]);
    } else {
      //LAB_800e59b0
      this.applyStageAmbiance(deffManager_800c693c.stageAmbiance_4c);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets or applies stage ambiance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "-1, -2, -3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "Int or int array")
  @Method(0x800e59d8L)
  public FlowControl FUN_800e59d8(final RunningScript<?> script) {
    final int a0 = script.params_20[0].get();

    if(a0 == -1) {
      deffManager_800c693c.stageAmbiance_4c.set(script.params_20[1]);
    } else if(a0 == -2) {
      //LAB_800e5a38
      //LAB_800e5a60
      this.applyStageAmbiance(new StageAmbiance4c().set(script.params_20[1]));
      //LAB_800e5a14
    } else if(a0 == -3) {
      //LAB_800e5a40
      this.applyStageAmbiance(deffManager_800c693c.dragoonSpaceAmbiance_98[script.params_20[1].get()]);
    }

    //LAB_800e5a68
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5a78L)
  public void tickLighting(final ScriptState<Void> state, final Void struct) {
    final BattleLightStruct64 light1 = this._800c6930;

    this.lightTicks_800c6928++;

    if(light1._24 == 3) { // Dragoon space lighting is handled here, I think this is for flickering light
      final int angle = rcos(((this.lightTicks_800c6928 + light1._2c) % light1._2e << 12) / light1._2e);
      final float minAngle = (0x1000 - angle) / (float)0x1000;
      final float maxAngle = (0x1000 + angle) / (float)0x1000;
      light1.colour_00.x = (light1.colour1_0c.x * maxAngle + light1.colour2_18.x * minAngle) / 2.0f;
      light1.colour_00.y = (light1.colour1_0c.y * maxAngle + light1.colour2_18.y * minAngle) / 2.0f;
      light1.colour_00.z = (light1.colour1_0c.z * maxAngle + light1.colour2_18.z * minAngle) / 2.0f;
    }

    //LAB_800e5b98
    //LAB_800e5ba0
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 light = this.lights_800c692c[i];
      final BttlLightStruct84Sub38 a2 = light._10;

      int type = a2.typeAndFlags_00 & 0xff;
      if(type == 1) {
        //LAB_800e5c50
        a2.directionOrColourSpeed_10.add(a2.directionOrColourAcceleration_1c);
        a2.directionOrColour_04.add(a2.directionOrColourSpeed_10);

        if((a2.typeAndFlags_00 & 0x8000) != 0) {
          a2.ticksRemaining_34--;

          if(a2.ticksRemaining_34 <= 0) {
            a2.typeAndFlags_00 = 0;
            a2.directionOrColour_04.set(a2.directionOrColourDest_28);
          }
        }

        //LAB_800e5cf4
        if((a2.typeAndFlags_00 & 0x2000) != 0) {
          light.light_00.direction_00.set(a2.directionOrColour_04);
          //LAB_800e5d40
        } else if((a2.typeAndFlags_00 & 0x4000) != 0) {
          final Vector3f sp0x18 = new Vector3f();
          sp0x18.set(a2.directionOrColour_04);
          this.FUN_800e4674(light.light_00.direction_00, sp0x18);
        }
      } else if(type == 2) {
        //LAB_800e5bf0
        final Vector3f sp0x10 = new Vector3f();
        final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[light.scriptIndex_48].innerStruct_00;
        sp0x10.set(bent.model_148.coord2_14.transforms.rotate).add(a2.directionOrColour_04);
        this.FUN_800e4674(light.light_00.direction_00, sp0x10);
      } else if(type == 3) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final Vector3f sp0x18 = new Vector3f();

        final int ticks = this.lightTicks_800c6928 & 0xfff;
        sp0x18.x = a2.directionOrColour_04.x + a2.directionOrColourSpeed_10.x * ticks;
        sp0x18.y = a2.directionOrColour_04.y + a2.directionOrColourSpeed_10.y * ticks;
        sp0x18.z = a2.directionOrColour_04.z + a2.directionOrColourSpeed_10.z * ticks;

        //LAB_800e5dcc
        this.FUN_800e4674(light.light_00.direction_00, sp0x18);
      }

      //LAB_800e5dd4
      final BttlLightStruct84Sub38 s0 = light._4c;
      type = s0.typeAndFlags_00 & 0xff;
      if(type == 1) {
        //LAB_800e5df4
        s0.directionOrColourSpeed_10.add(s0.directionOrColourAcceleration_1c);
        s0.directionOrColour_04.add(s0.directionOrColourSpeed_10);

        if((s0.typeAndFlags_00 & 0x8000) != 0) {
          s0.ticksRemaining_34--;

          if(s0.ticksRemaining_34 <= 0) {
            s0.typeAndFlags_00 = 0;
            s0.directionOrColour_04.set(s0.directionOrColourDest_28);
          }
        }

        //LAB_800e5e90
        this.lights_800c692c[i].light_00.r_0c = s0.directionOrColour_04.x;
        this.lights_800c692c[i].light_00.g_0d = s0.directionOrColour_04.y;
        this.lights_800c692c[i].light_00.b_0e = s0.directionOrColour_04.z;
      } else if(type == 3) {
        //LAB_800e5ed0
        final float theta = MathHelper.cos((this.lightTicks_800c6928 / (float)0x1000 + s0.directionOrColourDest_28.x) % s0.directionOrColourDest_28.y / s0.directionOrColourDest_28.y);
        final float ratioA = 1.0f + theta;
        final float ratioB = 1.0f - theta;
        this.lights_800c692c[i].light_00.r_0c = (s0.directionOrColour_04.x * ratioA + s0.directionOrColourSpeed_10.x * ratioB) / 2.0f;
        this.lights_800c692c[i].light_00.g_0d = (s0.directionOrColour_04.y * ratioA + s0.directionOrColourSpeed_10.y * ratioB) / 2.0f;
        this.lights_800c692c[i].light_00.b_0e = (s0.directionOrColour_04.z * ratioA + s0.directionOrColourSpeed_10.z * ratioB) / 2.0f;
      }

      //LAB_800e5fb8
      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public void deallocateLighting(final ScriptState<Void> state, final Void struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, this.lights_800c692c[i].light_00);
    }

    final BattleLightStruct64 v0 = this._800c6930;
    GTE.setBackgroundColour(v0.colour_00.x, v0.colour_00.y, v0.colour_00.z);
    projectionPlaneDistance_1f8003f8 = getProjectionPlaneDistance();
  }

  @Method(0x800e6070L)
  public void allocateLighting() {
    final ScriptState<Void> state = SCRIPTS.allocateScriptState(1, "Lighting controller", null);
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(this::tickLighting);
    state.setRenderer(this::deallocateLighting);
    this._800c6930.colourIndex_60 = 0;
    this.resetLights();
  }

  @Method(0x800e6314L)
  public void scriptDeffDeallocator(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    LOGGER.info(DEFF, "Deallocating DEFF script state %d", state.index);

    final DeffManager7cc struct7cc = deffManager_800c693c;

    struct7cc.deffPackage_5a8 = null;

    this.deffLoadingStage_800fafe8 = 4;

    if((struct7cc.flags_20 & 0x4_0000) != 0) {
      loadDeffSounds(this.loadedDeff_800c6938.bentState_04, 1);
    }

    if((struct7cc.flags_20 & 0x10_0000) != 0) {
      //LAB_800e63d0
      for(int i = 0; i < this.combatantCount_800c66a0; i++) {
        final CombatantStruct1a8 combatant = this.getCombatant(i);
        if((combatant.flags_19e & 0x1) != 0 && combatant.charIndex_1a2 >= 0) {
          this.loadAttackAnimations(combatant);
        }

        //LAB_800e6408
      }
    }

    //LAB_800e641c
    if((struct7cc.flags_20 & 0x60_0000) != 0) {
      loadDeffStageEffects(0);
    }

    //LAB_800e6444
    struct7cc.flags_20 &= 0xff80_ffff;
  }

  @Method(0x800e6470L)
  public ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> allocateDeffEffectManager(final ScriptState<? extends BattleObject> parent, final int flags, final int bentIndex, final int param, final int entrypoint, final ScriptDeffEffect effect) {
    deffManager_800c693c.flags_20 |= flags & 0x1_0000 | flags & 0x2_0000 | flags & 0x10_0000;

    if((deffManager_800c693c.flags_20 & 0x10_0000) != 0) {
      //LAB_800e651c
      for(int i = 0; i < this.combatantCount_800c66a0; i++) {
        final CombatantStruct1a8 combatant = this.getCombatant(i);

        if((combatant.flags_19e & 0x1) != 0 && combatant.mrg_04 != null && combatant.charIndex_1a2 >= 0) {
          this.FUN_800ca418(combatant);
        }

        //LAB_800e6564
      }
    }

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("DEFF ticker for script %d (%s)".formatted(parent.index, parent.name), parent, effect);

    LOGGER.info(DEFF, "Allocated DEFF script state %d for bent %d, param %d, parent %d, entrypoint %d", state.index, bentIndex, param, parent.index, entrypoint);

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x600_0400;

    final LoadedDeff24 v0 = this.loadedDeff_800c6938;
    v0.bentState_04 = SCRIPTS.getState(bentIndex, BattleEntity27c.class);
    v0.param_08 = param;
    v0.scriptIndex_0c = parent.index;
    v0.scriptEntrypoint_10 = entrypoint & 0xff;
    v0.managerState_18 = state;
    v0.init_1c = true;
    v0.frameCount_20 = -1;
    return state;
  }

  @Method(0x800e665cL)
  public void loadDragoonDeff(final RunningScript<? extends BattleObject> script, final ScriptDeffEffect effect) {
    final int index = script.params_20[0].get() & 0xffff;
    final int scriptEntrypoint = script.params_20[3].get() & 0xff;

    LOGGER.info(DEFF, "Loading dragoon DEFF (ID: %d, flags: %x)", index, script.params_20[0].get() & 0xffff_0000);

    deffManager_800c693c.flags_20 |= dragoonDeffFlags_800fafec[index] << 16;
    this.allocateDeffEffectManager(script.scriptState_04, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), effect);

    if((deffManager_800c693c.flags_20 & 0x4_0000) != 0) {
      loadDeffSounds(this.loadedDeff_800c6938.bentState_04, index != 0x2e || scriptEntrypoint != 0 ? 0 : 2);
    }

    for(int i = 0; i < dragoonDeffsWithExtraTims_800fb040.length; i++) {
      if(dragoonDeffsWithExtraTims_800fb040[i] == index) {
        if(Loader.isDirectory("SECT/DRGN0.BIN/%d".formatted(4115 + i))) {
          loadDrgnDir(0, 4115 + i, this::uploadTims);
        }
      }
    }

    this.loadDeff(
      Loader.resolve("SECT/DRGN0.BIN/" + (4139 + index * 2)),
      Loader.resolve("SECT/DRGN0.BIN/" + (4140 + index * 2))
    );
  }

  @Method(0x800e6844L)
  public void loadSpellItemDeff(final RunningScript<? extends BattleObject> script, final ScriptDeffEffect effect) {
    this.loadSpellItemDeff(script.scriptState_04, script.params_20[0].get() & 0xffff, script.params_20[0].get() & 0xffff_0000, script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), effect);
  }

  public void loadSpellItemDeff(final ScriptState<? extends BattleObject> parent, final int id, final int flags, final int bentIndex, final int _08, final int entrypoint, final ScriptDeffEffect effect) {
    final int s0 = (id - 192) * 2;

    LOGGER.info(DEFF, "Loading spell item DEFF (ID: %d, flags: %x)", id, flags);

    deffManager_800c693c.flags_20 |= 0x40_0000;
    this.allocateDeffEffectManager(parent, flags, bentIndex, _08, entrypoint, effect);

    this.loadDeff(
      Loader.resolve("SECT/DRGN0.BIN/" + (4307 + s0)),
      Loader.resolve("SECT/DRGN0.BIN/" + (4308 + s0))
    );
  }

  @Method(0x800e6920L)
  public void loadEnemyOrBossDeff(final RunningScript<? extends BattleObject> script, final ScriptDeffEffect effect) {
    final int flags = script.params_20[0].get() & 0xff_0000;
    final int monsterIndex = script.params_20[0].get() & 0xffff;

    LOGGER.info(DEFF, "Loading enemy/boss DEFF (ID: %d, flags: %x)", monsterIndex, flags);

    deffManager_800c693c.flags_20 |= flags & 0x10_0000;
    this.allocateDeffEffectManager(script.scriptState_04, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), effect);

    if(monsterIndex < 256) {
      this.loadDeff(
        Loader.resolve("SECT/DRGN0.BIN/" + (4433 + monsterIndex * 2)),
        Loader.resolve("SECT/DRGN0.BIN/" + (4434 + monsterIndex * 2))
      );
    } else {
      final int a0_0 = monsterIndex >>> 4;
      int fileIndex = enemyDeffFileIndices_800faec4[a0_0 - 0x100] + (monsterIndex & 0xf);
      if(a0_0 >= 320) {
        fileIndex += 117;
      }

      fileIndex = (fileIndex - 1) * 2;

      this.loadDeff(
        Loader.resolve("SECT/DRGN0.BIN/" + (4945 + fileIndex)),
        Loader.resolve("SECT/DRGN0.BIN/" + (4946 + fileIndex))
      );
    }
  }

  @Method(0x800e6aecL)
  public void loadCutsceneDeff(final RunningScript<? extends BattleObject> script, final ScriptDeffEffect effect) {
    final int v1 = script.params_20[0].get();
    final int cutsceneIndex = v1 & 0xffff;

    LOGGER.info(DEFF, "Loading cutscene DEFF (ID: %d, flags: %x)", cutsceneIndex, v1 & 0xffff_0000);

    this.allocateDeffEffectManager(script.scriptState_04, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), effect);

    for(int i = 0; i < cutsceneDeffsWithExtraTims_800fb05c.length; i++) {
      if(cutsceneDeffsWithExtraTims_800fb05c[i] == cutsceneIndex) {
        if(Loader.isDirectory("SECT/DRGN0.BIN/%d".formatted(5505 + i))) {
          loadDrgnDir(0, 5505 + i, this::uploadTims);
        }
      }
    }

    this.loadDeff(
      Loader.resolve("SECT/DRGN0.BIN/" + (5511 + cutsceneIndex * 2)),
      Loader.resolve("SECT/DRGN0.BIN/" + (5512 + cutsceneIndex * 2))
    );
  }

  public void loadDeff(final Path tims, final Path deff) {
    this.loadedDeff_800c6938.script_14 = null;
    this.deffLoadingStage_800fafe8 = 1;

    Loader.loadDirectory(tims, this::uploadTims);
    Loader.loadDirectory(deff.resolve("0"), files -> {
      this.loadDeffPackage(files, this.loadedDeff_800c6938.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      Loader.loadFile(deff.resolve("1"), file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        this.loadedDeff_800c6938.script_14 = new ScriptFile(deff.toString(), file.getBytes());
      });
    });
  }

  @ScriptDescription("Ticks the DEFF loader for DEFFs that are not set up to tick themselves. May pause and rewind if the DEFF is not yet ready for that stage.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "loadingStage", description = "The loading stage to run (ranges from 0-4 inclusive)")
  @Method(0x800e6db4L)
  public FlowControl scriptTickDeffLoadingStage(final RunningScript<?> script) {
    final FlowControl flow;
    final int deffStage;
    switch(script.params_20[0].get() & 0xffff) {
      case 0, 1 -> {
        deffStage = this.deffLoadingStage_800fafe8;
        if(deffStage == 1) {
          //LAB_800e6e20
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 2) {
          //LAB_800e6e28
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a2");
        }

        //LAB_800e6e2c
      }

      case 2 -> {
        deffStage = this.deffLoadingStage_800fafe8;
        if(deffStage == 1) {
          //LAB_800e6e58
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 2) {
          final DeffManager7cc struct7cc = deffManager_800c693c;

          //LAB_800e6e60
          if((struct7cc.flags_20 & 0x20_0000) != 0) {
            loadDeffStageEffects(1);
          }

          //LAB_800e6e88
          if((struct7cc.flags_20 & 0x40_0000) != 0) {
            loadDeffStageEffects(3);
          }

          //LAB_800e6eb0
          final LoadedDeff24 struct24 = this.loadedDeff_800c6938;
          struct24.managerState_18.loadScriptFile(struct24.script_14, struct24.scriptEntrypoint_10);
          struct24.init_1c = false;
          struct24.frameCount_20 = 0;
          this.deffLoadingStage_800fafe8 = 3;
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined t0");
        }

        //LAB_800e6ee4
      }

      case 3 -> {
        deffStage = this.deffLoadingStage_800fafe8;
        if(deffStage == 3) {
          //LAB_800e6f10
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 4) {
          //LAB_800e6f18
          this.deffLoadingStage_800fafe8 = 0;
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a3");
        }

        //LAB_800e6f20
      }

      case 4 -> {
        switch(this.deffLoadingStage_800fafe8) {
          case 0:
            flow = FlowControl.CONTINUE;
            break;

          case 1:
            flow = FlowControl.PAUSE_AND_REWIND;
            break;

          case 2:
          case 3:
            this.loadedDeff_800c6938.managerState_18.deallocateWithChildren();

          case 4:
            this.deffLoadingStage_800fafe8 = 0;
            this.loadedDeff_800c6938.managerState_18 = null;
            flow = FlowControl.CONTINUE;
            break;

          default:
            throw new RuntimeException("Undefined a1");
        }

        //LAB_800e6f9c
      }

      default -> throw new RuntimeException("Undefined flow");
    }

    //LAB_800e6fa0
    return flow;
  }

  @ScriptDescription("Waits for any currently-loading DEFFs to finish loading, loads a DEFF, and waits for it to finish loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6fb4L)
  public FlowControl scriptLoadDragoonDeffSync(final RunningScript<? extends BattleObject> script) {
    if(this.deffLoadingStage_800fafe8 != 0 && script.scriptState_04.index != this.loadedDeff_800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e6fec
    //LAB_800e6ff0
    //LAB_800e7014
    if(this.deffLoadingStage_800fafe8 == 0) {
      this.loadDragoonDeff(script, new ScriptDeffEffect());
    }

    if(this.deffLoadingStage_800fafe8 < 4) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(this.deffLoadingStage_800fafe8 == 4) {
      //LAB_800e702c
      this.deffLoadingStage_800fafe8 = 0;
      this.loadedDeff_800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    throw new IllegalStateException("Invalid v1");
  }

  @Method(0x800e7060L)
  public void loadDeffPackage(final List<FileData> files, final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    LOGGER.info(DEFF, "Loading DEFF files");

    deffManager_800c693c.deffPackage_5a8 = new DeffPart[files.size()];
    Arrays.setAll(deffManager_800c693c.deffPackage_5a8, i -> DeffPart.getDeffPart(files, i));
    this.prepareDeffFiles(files, state);
  }

  @Method(0x800e70bcL)
  public void scriptDeffTicker(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> struct) {
    final LoadedDeff24 a0 = this.loadedDeff_800c6938;

    if(a0.frameCount_20 != -1) {
      a0.frameCount_20 += vsyncMode_8007a3b8;
    }

    //LAB_800e70fc
    if(a0.init_1c && a0.script_14 != null) {
      final DeffManager7cc struct7cc = deffManager_800c693c;

      if((struct7cc.flags_20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40) == 0) {
        //LAB_800e7154
        if((struct7cc.flags_20 & 0x20_0000) != 0) {
          loadDeffStageEffects(1);
        }

        //LAB_800e7178
        if((struct7cc.flags_20 & 0x40_0000) != 0) {
          loadDeffStageEffects(3);
        }

        //LAB_800e719c
        state.loadScriptFile(a0.script_14, a0.scriptEntrypoint_10);
        a0.init_1c = false;
        a0.frameCount_20 = 0;
      }
    }

    //LAB_800e71c4
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a spell or item")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e71e4L)
  public FlowControl scriptLoadSpellOrItemDeff(final RunningScript<? extends BattleObject> script) {
    if(this.deffLoadingStage_800fafe8 != 0 && script.scriptState_04.index != this.loadedDeff_800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e721c
    //LAB_800e7220
    final int deffStage = this.deffLoadingStage_800fafe8;

    if(deffStage == 4) {
      //LAB_800e725c
      this.deffLoadingStage_800fafe8 = 0;
      this.loadedDeff_800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7244
    if(deffStage == 0) {
      this.loadSpellItemDeff(script, new ScriptDeffEffect());
    }

    //LAB_800e726c
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for an enemy or boss")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e727cL)
  public FlowControl scriptLoadEnemyOrBossDeff(final RunningScript<? extends BattleObject> script) {
    if(this.deffLoadingStage_800fafe8 != 0 && script.scriptState_04.index != this.loadedDeff_800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e72b4
    //LAB_800e72b8
    final int deffStage = this.deffLoadingStage_800fafe8;

    if(deffStage == 4) {
      //LAB_800e72f4
      this.deffLoadingStage_800fafe8 = 0;
      this.loadedDeff_800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e72dc
    if(deffStage == 0) {
      this.loadEnemyOrBossDeff(script, new ScriptDeffEffect());
    }

    //LAB_800e7304
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a cutscene")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e7314L)
  public FlowControl scriptLoadCutsceneDeff(final RunningScript<? extends BattleObject> script) {
    if(this.deffLoadingStage_800fafe8 != 0 && script.scriptState_04.index != this.loadedDeff_800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e734c
    //LAB_800e7350
    final int deffStage = this.deffLoadingStage_800fafe8;

    if(deffStage == 4) {
      //LAB_800e738c
      this.deffLoadingStage_800fafe8 = 0;
      this.loadedDeff_800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7374
    if(deffStage == 0) {
      this.loadCutsceneDeff(script, new ScriptDeffEffect());
    }

    //LAB_800e739c
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0x100_0000 = dragoon, 0x200_0000 = spell/item, 0x300_0000/0x400_0000 = enemy/boss, 0x500_0000 = cutscene")
  @Method(0x800e73acL)
  public FlowControl scriptLoadDeff(final RunningScript<? extends BattleObject> script) {
    if(this.deffLoadingStage_800fafe8 != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int type = script.params_20[4].get();
    if(type == 0x100_0000) {
      this.loadDragoonDeff(script, new ScriptDeffManualLoadingEffect());
    } else if(type == 0x200_0000) {
      this.loadSpellItemDeff(script, new ScriptDeffManualLoadingEffect());
    } else if(type == 0x300_0000 || type == 0x400_0000) {
      this.loadEnemyOrBossDeff(script, new ScriptDeffManualLoadingEffect());
    } else if(type == 0x500_0000) {
      this.loadCutsceneDeff(script, new ScriptDeffManualLoadingEffect());
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the current DEFF loading stage")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "loadingStage")
  @Method(0x800e7490L)
  public FlowControl scriptGetDeffLoadingStage(final RunningScript<?> script) {
    script.params_20[0].set(this.deffLoadingStage_800fafe8);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns two unknown values")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "bentIndex")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800e74acL)
  public FlowControl FUN_800e74ac(final RunningScript<?> script) {
    final LoadedDeff24 struct24 = this.loadedDeff_800c6938;
    script.params_20[0].set(struct24.bentState_04.index);
    script.params_20[1].set(struct24.param_08);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e74e0L)
  public void FUN_800e74e0(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final LoadedDeff24 struct24 = this.loadedDeff_800c6938;

    final int deffStage = this.deffLoadingStage_800fafe8;
    if(deffStage == 1) {
      //LAB_800e7510
      if(struct24.init_1c && struct24.script_14 != null && ((deffManager_800c693c.flags_20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40) == 0)) {
        //LAB_800e756c
        this.deffLoadingStage_800fafe8 = 2;
      }
    } else if(deffStage == 3) {
      //LAB_800e7574
      if(struct24.frameCount_20 >= 0) {
        struct24.frameCount_20 += vsyncMode_8007a3b8;
      }
    }

    //LAB_800e759c
  }

  @Method(0x800e8ffcL)
  public void allocateDeffManager() {
    if(deffManager_800c693c != null) {
      deffManager_800c693c.delete();
    }

    final DeffManager7cc deffManager = new DeffManager7cc();
    this.loadedDeff_800c6938 = deffManager._5b8;
    this._800c6930 = deffManager._5dc;
    this.lights_800c692c = deffManager._640;
    deffManager.flags_20 = 0x4;
    deffManager_800c693c = deffManager;
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> manager = allocateEffectManager("DEFF manager", null, null);
    manager.innerStruct_00.flags_04 = 0x600_0400;
    deffManager.scriptState_1c = manager;
    this.allocateLighting();
    this.loadStageAmbiance();
  }

  @Method(0x800e9120L)
  public void deallocateLightingControllerAndDeffManager() {
    scriptStatePtrArr_800bc1c0[1].deallocateWithChildren();
    deffManager_800c693c.deallocateScriptsArray();
    deffManager_800c693c.scriptState_1c.deallocateWithChildren();
    deffManager_800c693c.delete();
    deffManager_800c693c = null;
  }

  @Method(0x800e929cL)
  public void uploadTims(final List<FileData> files) {
    LOGGER.info(DEFF, "Loading DEFF TIMs");

    //LAB_800e92d4
    for(final FileData file : files) {
      if(file.hasVirtualSize()) {
        new Tim(file).uploadToGpu();
      }
    }

    //LAB_800e9354
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800e93e0L)
  public FlowControl scriptAllocateEmptyEffectManagerChild(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Empty EffectManager child, allocated by script %d (%s) from scriptAllocateEmptyEffectManagerChild".formatted(script.scriptState_04.index, script.scriptState_04.name), script.scriptState_04, null).index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a new billboard sprite effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "Flag meanings are unknown")
  @Method(0x800e96ccL)
  public FlowControl allocateBillboardSpriteEffect(final RunningScript<? extends BattleObject> script) {
    final BillboardSpriteEffect0c effect = new BillboardSpriteEffect0c();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager("BillboardSpriteEffect0c", script.scriptState_04, effect);

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x400_0000;
    effect.set(script.params_20[1].get());
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow for a BattleObject with a models")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The BattleObject or ModelEffect13c index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "-1 = shadow type 2, -2 = shadow type 3, -3 = shadow type 0, all other values are model part index to attach type 2 shadow to")
  @Method(0x800e9798L)
  public FlowControl scriptSetModelShadow(final RunningScript<?> script) {
    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_134;
    } else {
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800e97e8
    //LAB_800e97ec
    final int a0 = script.params_20[1].get();
    if(a0 == -1) {
      model.shadowType_cc = 2;
      model.modelPartWithShadowIndex_cd = -1;
    } else if(a0 == -2) {
      //LAB_800e982c
      model.shadowType_cc = 3;
      //LAB_800e980c
    } else if(a0 == -3) {
      //LAB_800e983c
      model.shadowType_cc = 0;
    } else {
      //LAB_800e9844
      //LAB_800e9848
      model.shadowType_cc = 3;
      model.modelPartWithShadowIndex_cd = a0;
    }

    //LAB_800e984c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an unknown model effect (used in Miranda transformation)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0", description = "Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800e9854L)
  public FlowControl FUN_800e9854(final RunningScript<? extends BattleObject> script) {
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)deffManager_800c693c.getDeffPart(script.params_20[1].get() | 0x200_0000);

    final ModelEffect13c effect = new ModelEffect13c("Script " + script.scriptState_04.index);

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      animatedTmdType.name,
      script.scriptState_04,
      effect,
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    effect._00 = 0;
    effect.tmdType_04 = animatedTmdType;
    effect.extTmd_08 = animatedTmdType.tmd_0c;
    effect.anim_0c = animatedTmdType.anim_14;
    effect.model_134 = effect.model_10;
    final Model124 model = effect.model_134;

    // Retail bug? Trying to read textureInfo from a DEFF container that doesn't have it
    if(animatedTmdType.textureInfo_08 != null) {
      final DeffPart.TextureInfo textureInfo = animatedTmdType.textureInfo_08[0];
      final int tpage = GetTPage(Bpp.BITS_4, Translucency.HALF_B_PLUS_HALF_F, textureInfo.vramPos_00.x, textureInfo.vramPos_00.y);
      model.uvAdjustments_9d = vramSlots_8005027c[modelVramSlotIndices_800fb06c[tpage]];
    } else {
      model.uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
    }

    this.loadModelTmd(model, effect.extTmd_08);
    effect.anim_0c.loadIntoModel(model);
    addGenericAttachment(manager, 0, 0x100, 0);
    manager.params_10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an unknown model effect manager (used in Miranda transformation)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The DEFF flags, mostly unknown")
  @Method(0x800e99bcL)
  public FlowControl FUN_800e99bc(final RunningScript<? extends BattleObject> script) {
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)deffManager_800c693c.getDeffPart(script.params_20[1].get() | 0x100_0000);

    final ModelEffect13c effect = new ModelEffect13c("Script " + script.scriptState_04.index);

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      animatedTmdType.name,
      script.scriptState_04,
      effect,
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x100_0000;
    effect._00 = 0;

    effect.tmdType_04 = animatedTmdType;
    effect.extTmd_08 = animatedTmdType.tmd_0c;
    effect.anim_0c = animatedTmdType.anim_14;
    effect.model_10.uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
    effect.model_134 = effect.model_10;
    this.loadModelTmd(effect.model_134, effect.extTmd_08);
    effect.anim_0c.loadIntoModel(effect.model_134);
    addGenericAttachment(manager, 0, 0x100, 0);
    manager.params_10.flags_00 = 0x5400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9ae4L)
  public void copyBattleStageModel(final Model124 model, final BattleStage a1) {
    model.coord2_14.set(a1.coord2_558);
    model.coord2_14.transforms.set(a1.param_5a8);

    model.keyframes_90 = a1.rotTrans_5d4;
    model.currentKeyframe_94 = 0;
    model.partCount_98 = a1.partCount_5dc;
    model.totalFrames_9a = a1.totalFrames_5de;
    model.animationState_9c = a1.animationState_5e0;
    model.uvAdjustments_9d = UvAdjustmentMetrics14.NONE;
    model.zOffset_a0 = 0x200;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.smallerStructPtr_a4 = null;
    model.remainingFrames_9e = a1.remainingFrames_5e2;
    model.subFrameIndex = 0;
    model.ptr_a8 = a1._5ec;

    //LAB_800e9c0c
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    model.partInvisible_f4 = a1.flags_5e4;
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.tpage_108 = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();
    model.shadowType_cc = 0;
    model.modelPartWithShadowIndex_cd = 0;

    model.modelParts_00 = new ModelPart10[a1.dobj2s_00.length];
    Arrays.setAll(model.modelParts_00, i -> new ModelPart10().set(a1.dobj2s_00[i]));

    //LAB_800e9d34
    for(final ModelPart10 dobj2 : model.modelParts_00) {
      dobj2.coord2_04 = new GsCOORDINATE2();
      dobj2.coord2_04.super_ = model.coord2_14;
    }
  }

  @Method(0x800e9db4L)
  public void copyModel(final Model124 model1, final Model124 model2) {
    //LAB_800e9dd8
    model1.set(model2);

    model1.modelParts_00 = new ModelPart10[model1.modelParts_00.length];
    Arrays.setAll(model1.modelParts_00, i -> new ModelPart10().set(model2.modelParts_00[i]));

    //LAB_800e9ee8
    for(int i = 0; i < model1.partCount_98; i++) {
      final ModelPart10 dobj2 = model1.modelParts_00[i];
      dobj2.coord2_04 = new GsCOORDINATE2().set(model2.modelParts_00[i].coord2_04);
      dobj2.coord2_04.super_ = model1.coord2_14;
    }
  }

  @ScriptDescription("Allocates an effect manager for a cloned model")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager's index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "cloneId", description = "Battle entity ID or flag 0x700_0000 to clone battle stage model")
  @Method(0x800e9f68L)
  public FlowControl scriptAllocateClonedModelEffect(final RunningScript<? extends BattleObject> script) {
    final int id = script.params_20[1].get();

    final ModelEffect13c effect = new ModelEffect13c("Script " + script.scriptState_04.index);

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      (id & 0x700_0000) != 0 ? "Cloned battle stage model" : "Cloned bent model %d".formatted(id),
      script.scriptState_04,
      effect,
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    effect._00 = 0;
    effect.tmdType_04 = null;
    effect.extTmd_08 = null;
    effect.anim_0c = null;
    effect.model_134 = effect.model_10;

    if((id & 0xff00_0000) == 0x700_0000) {
      this.copyBattleStageModel(effect.model_10, battlePreloadedEntities_1f8003f4.stage_963c);
    } else {
      //LAB_800ea030
      this.copyModel(effect.model_10, ((BattleEntity27c)scriptStatePtrArr_800bc1c0[id].innerStruct_00).model_148);
    }

    //LAB_800ea04c
    final Model124 model = effect.model_134;
    manager.params_10.trans_04.set(model.coord2_14.coord.transfer);
    manager.params_10.rot_10.set(model.coord2_14.transforms.rotate);
    manager.params_10.scale_16.set(model.coord2_14.transforms.scale);
    manager.params_10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Hides an effect's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "part", description = "Which model part to hide")
  @Method(0x800ea13cL)
  public FlowControl scriptHideEffectModelPart(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Model124 model = ((ModelEffect13c)manager.effect_44).model_134;
    final int flag = script.params_20[1].get() & 0xffff;

    final int index = flag >>> 5;
    final int shift = flag & 0x1f;

    model.partInvisible_f4 |= 0x1L << shift + index * 32;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Shows an effect's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "part", description = "Which model part to show")
  @Method(0x800ea19cL)
  public FlowControl scriptShowEffectModelPart(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Model124 model = ((ModelEffect13c)manager.effect_44).model_134;
    final int flag = script.params_20[1].get() & 0xffff;

    final int index = flag >>> 5;
    final int shift = flag & 0x1f;

    model.partInvisible_f4 &= ~(0x1L << shift + index * 32);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a CMB animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The DEFF flags, mostly unknown")
  @Method(0x800ea200L)
  public FlowControl scriptLoadCmbAnimation(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.AnimType.class));
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;

    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)deffManager_800c693c.getDeffPart(script.params_20[1].get() | 0x200_0000);
    final Anim cmb = animatedTmdType.anim_14;
    effect.anim_0c = cmb;
    cmb.loadIntoModel(effect.model_134);
    manager.params_10.ticks_24 = 0;
    addGenericAttachment(manager, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow size for a battle object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z size (12-bit fixed-point)")
  @Method(0x800ea2a0L)
  public FlowControl scriptSetBttlShadowSize(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_134;
    } else {
      //LAB_800ea2f8
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800ea300
    model.shadowSize_10c.x = script.params_20[1].get() / (float)0x1000;
    model.shadowSize_10c.z = script.params_20[2].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow offset for a battle object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ea30cL)
  public FlowControl scriptSetBttlShadowOffset(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    final Model124 model;
    if(BattleObject.EM__.equals(bobj.magic_00)) {
      model = ((ModelEffect13c)((EffectManagerData6c<?>)bobj).effect_44).model_134;
    } else {
      //LAB_800ea36c
      model = ((BattleEntity27c)bobj).model_148;
    }

    //LAB_800ea374
    model.shadowOffset_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of times an effect's animation has looped")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "loopCount", description = "The number of loops")
  @Method(0x800ea384L)
  public FlowControl scriptGetEffectLoopCount(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.AnimType.class));
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;

    if(effect.anim_0c == null) {
      script.params_20[1].set(0);
    } else {
      //LAB_800ea3cc
      script.params_20[1].set((manager.params_10.ticks_24 + 2) / effect.model_134.totalFrames_9a);
    }

    //LAB_800ea3e4
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea620L)
  public void prepareDeffFiles(final List<FileData> deff, final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> deffManagerState) {
    //LAB_800ea674
    for(int i = 0; i < deff.size(); i++) {
      final FileData data = deff.get(i);

      final int flags = data.readInt(0);
      final int type = flags & 0xff00_0000; // Flags
      if(type == 0x100_0000) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);
        final CContainer extTmd = tmdType.tmd_0c;
        final TmdWithId tmd = extTmd.tmdPtr_00;

        //TODO I don't think this is doing anything
        for(int objectIndex = 0; objectIndex < tmd.tmd.header.nobj; objectIndex++) {
          tmd.optimisePacketsIfNecessary(objectIndex);
        }

        if(tmdType.textureInfo_08 != null && deffManagerState.index != 0) {
          this.addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, extTmd, tmdType.textureInfo_08);
        }
      } else if(type == 0x200_0000) {
        final DeffPart.AnimatedTmdType animType = new DeffPart.AnimatedTmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);

        if(animType.textureInfo_08 != null && deffManagerState.index != 0) {
          this.addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, animType.tmd_0c, animType.textureInfo_08);
        }
      } else if(type == 0x300_0000) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);
        final CContainer extTmd = tmdType.tmd_0c;

        //TODO I don't think this is doing anything
        extTmd.tmdPtr_00.optimisePacketsIfNecessary(0);

        if(tmdType.textureInfo_08 != null && deffManagerState.index != 0) {
          this.addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, extTmd, tmdType.textureInfo_08);
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
  }

  @Method(0x800ea7d0L)
  public void hudDeffLoaded(final List<FileData> files) {
    final DeffManager7cc struct7cc = deffManager_800c693c;
    this.prepareDeffFiles(files, struct7cc.scriptState_1c);

    //LAB_800ea814
    int i;
    for(i = 0; i < files.size(); i++) {
      final int flags = files.get(i).readInt(0); // Flags

      if((flags & 0xff00_0000) != 0) {
        break;
      }

      struct7cc.lmbs_390[flags & 0xff] = new DeffPart.LmbType(files.get(i));
    }

    //LAB_800ea850
    //LAB_800ea874
    for(; i < files.size(); i++) {
      if((files.get(i).readInt(0) & 0xff00_0000) != 0x100_0000) { // Flags
        break;
      }
    }

    //LAB_800ea89c
    //LAB_800ea8a8
    //LAB_800ea8e0
    for(; i < files.size(); i++) {
      final int flags = files.get(i).readInt(0);

      if((flags & 0xff00_0000) != 0x300_0000) {
        break;
      }

      final int index = flags & 0xff;
      if(index >= 5) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("HUD DEFF file " + i, files.get(i));
        struct7cc.tmds_2f8[index] = tmdType.tmd_0c.tmdPtr_00.tmd.objTable[0];
        struct7cc.objs[index] = TmdObjLoader.fromObjTable(tmdType.name, struct7cc.tmds_2f8[index]);
      }

      //LAB_800ea928
    }

    //LAB_800ea93c
    //LAB_800ea964
    for(; i < files.size(); i++) {
      final int flags = files.get(i).readInt(0);

      if((flags & 0xff00_0000) != 0x400_0000) {
        break;
      }

      final DeffPart.SpriteType spriteType = new DeffPart.SpriteType(files.get(i));
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      final SpriteMetrics08 metrics = struct7cc.spriteMetrics_39c[flags & 0xff];
      metrics.u_00 = deffMetrics.u_00;
      metrics.v_02 = deffMetrics.v_02;
      metrics.w_04 = deffMetrics.w_04 * 4;
      metrics.h_05 = deffMetrics.h_06;
      metrics.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }

    //LAB_800eaa00
    //LAB_800eaa04
  }

  @Method(0x800eacf4L)
  public void loadBattleHudDeff() {
    loadDrgnDirSync(0, "4114/2", this::hudDeffLoaded);
    loadDrgnDir(0, "4114/3", this::uploadTims);
    loadDrgnDir(0, "4114/1", files -> {
      deffManager_800c693c.scripts_2c = new ScriptFile[files.size()];

      for(int i = 0; i < files.size(); i++) {
        deffManager_800c693c.scripts_2c[i] = new ScriptFile("DRGN0.4114.1." + i, files.get(i).getBytes());
      }
    });
  }

  @Method(0x800ead44L)
  public void applyTextureAnimation(final Rect4i rect, final int h) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, rect.x, rect.y + rect.h - h, rect.w, h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x, rect.y + h, rect.x, rect.y, rect.w, rect.h - h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x, rect.y, 960, 256, rect.w, h));
  }

  @Method(0x800eaec8L)
  public int tickTextureAnimationAttachment(final EffectManagerData6c<?> manager, final TextureAnimationAttachment1c anim) {
    //LAB_800eaef0
    anim.accumulator_14 += anim.step_18;

    //LAB_800eaf08
    int h = (anim.step_18 >> 8) % anim.rect_0c.h;

    if(h < 0) {
      h += anim.rect_0c.h;
    }

    //LAB_800eaf30
    if(h != 0) {
      this.applyTextureAnimation(anim.rect_0c, h);
    }

    //LAB_800eaf44
    return 1;
  }

  @Method(0x800eaf54L)
  public TextureAnimationAttachment1c findTextureAnimationAttachment(EffectManagerData6c<?> manager, final Rect4i vramPos) {
    // Find manager with attachment 10
    //LAB_800eaf80
    while(!manager.hasAttachment(10)) {
      if(manager.parentScript_50 == null) {
        break;
      }

      manager = manager.parentScript_50.innerStruct_00;
    }

    //LAB_800eafb8
    TextureAnimationAttachment1c attachment = (TextureAnimationAttachment1c)manager.findAttachment(10);

    //LAB_800eafcc
    while(attachment != null) {
      if(attachment.rect_0c.x == vramPos.x && attachment.rect_0c.y == vramPos.y) {
        break;
      }

      //LAB_800eaff4
      attachment = (TextureAnimationAttachment1c)attachment.findAttachment(10);
    }

    //LAB_800eb00c
    return attachment;
  }

  @ScriptDescription("Removes a texture animation attachment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "The texture index")
  @Method(0x800eb01cL)
  public FlowControl scriptRemoveTextureAnimationAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[script.params_20[1].get() * 2];

    EffectManagerData6c<?> managerWithTextureAnimationAttachment = manager;

    //LAB_800eb0c0
    while(!managerWithTextureAnimationAttachment.hasAttachment(10)) {
      final ScriptState<EffectManagerData6c<?>> parent = managerWithTextureAnimationAttachment.parentScript_50;

      if(parent == null) {
        break;
      }

      managerWithTextureAnimationAttachment = parent.innerStruct_00;
    }

    //LAB_800eb0f8
    AttachmentHost current = managerWithTextureAnimationAttachment;

    //LAB_800eb10c
    while(current.getAttachment() != null) {
      final EffectAttachment attachment = current.getAttachment();

      if(attachment.id_05 == 10 && attachment instanceof final TextureAnimationAttachment1c attachment10) {
        if(attachment10.rect_0c.x == textureInfo.vramPos_00.x) {
          if(attachment10.rect_0c.y == textureInfo.vramPos_00.y) {
            current.setAttachment(attachment.getAttachment());
            break;
          }
        }
      }

      //LAB_800eb15c
      current = attachment;
    }

    //LAB_800eb174
    //LAB_800eb178
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Applies a texture animation attachment update")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "The texture index")
  @Method(0x800eb188L)
  public FlowControl scriptApplyTextureAnimationAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;

    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[script.params_20[1].get() * 2];
    final TextureAnimationAttachment1c attachment = this.findTextureAnimationAttachment(manager, textureInfo.vramPos_00);

    if(attachment != null) {
      int h = (-attachment.accumulator_14 >> 8) % attachment.rect_0c.h;

      if(h < 0) {
        h += attachment.rect_0c.h;
      }

      //LAB_800eb25c
      if(h != 0) {
        this.applyTextureAnimation(attachment.rect_0c, h);
      }
    }

    //LAB_800eb270
    return FlowControl.CONTINUE;
  }

  /** Adds a new texture update attachment, or updates an existing one's step value if one already exists for that region */
  @Method(0x800eb280L)
  public void addOrUpdateTextureAnimationAttachment(final EffectManagerData6c<?> manager, final Rect4i vramPos, final int step) {
    TextureAnimationAttachment1c attachment = this.findTextureAnimationAttachment(manager, vramPos);

    if(attachment == null) {
      attachment = manager.addAttachment(10, 0, this::tickTextureAnimationAttachment, new TextureAnimationAttachment1c());
      attachment.rect_0c.set(vramPos);
      attachment.accumulator_14 = 0;
    }

    //LAB_800eb2ec
    attachment.step_18 = step;
  }

  @Method(0x800eb308L)
  public void addCContainerTextureAnimationAttachments(final EffectManagerData6c<?> manager, final CContainer cContainer, final DeffPart.TextureInfo[] textureInfo) {
    if(cContainer.ptr_08 != null) {
      final CContainerSubfile2 s2 = cContainer.ptr_08;

      //LAB_800eb348
      for(int s1 = 0; s1 < 7; s1++) {
        final short[] s0 = s2._00[s1];

        if((s0[0] & 0x4000) != 0) {
          final TextureAnimationAttachment1c sub = manager.addAttachment(10, 0, this::tickTextureAnimationAttachment, new TextureAnimationAttachment1c());

          if((s0[1] & 0x3c0) == 0) {
            sub.rect_0c.x = textureInfo[0].vramPos_00.x & 0x3c0 | s0[1];
            sub.rect_0c.y = textureInfo[0].vramPos_00.y & 0x100 | s0[2];
          } else {
            //LAB_800eb3cc
            sub.rect_0c.x = s0[1];
            sub.rect_0c.y = s0[2];
          }

          //LAB_800eb3dc
          //LAB_800eb3f8
          sub.rect_0c.w = s0[3] / 4;
          sub.rect_0c.h = s0[4];
          sub.accumulator_14 = 0;

          final int v0;
          if(s0[6] >= 0x10) {
            v0 = s0[6] * 0x10;
          } else {
            //LAB_800eb42c
            v0 = 0x100 / s0[6];
          }

          //LAB_800eb434
          sub.step_18 = v0;

          if(s0[5] == 0) {
            sub.step_18 = -sub.step_18;
          }
        }

        //LAB_800eb45c
      }
    }

    //LAB_800eb46c
  }

  @Method(0x800eb48cL)
  public void addOrUpdateTextureAnimationAttachment(final int scriptIndex, final int textureIndex, final int step) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(scriptIndex, EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    this.addOrUpdateTextureAnimationAttachment(manager, tmdType.textureInfo_08[textureIndex * 2].vramPos_00, step);
  }

  @ScriptDescription("Adds or updates a texture animation attachment to an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "varIndex", description = "The texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "step", description = "The animation movement step")
  @Method(0x800eb518L)
  public FlowControl scriptAddOrUpdateTextureAnimationAttachment(final RunningScript<?> script) {
    this.addOrUpdateTextureAnimationAttachment(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Dart transform */
  @Method(0x800eb554L)
  public void applyRedEyeDragoonTransformationFlameArmorEffectTextureAnimations(final Rect4i a0, final Vector2i a1, final int height) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, a1.x, a1.y + a0.h - height, a0.w, height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.x, a1.y + height, a1.x, a1.y, a0.w, a0.h - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.x, a1.y, a0.x, a0.y + a0.h - height, a0.w, height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x, a0.y + height, a0.x, a0.y, a0.w, a0.h - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x, a0.y, 960, 256, a0.w, height));
  }

  @Method(0x800eb7c4L)
  public int tickRedEyeDragoonTransformationFlameArmorEffect(final EffectManagerData6c<?> manager, final RedEyeDragoonTransformationFlameArmorEffect20 effect) {
    //LAB_800eb7e8
    effect.accumulator_14 += effect.step_18;

    //LAB_800eb800
    int a2 = (effect.step_18 >> 8) % effect.rect_0c.h;

    if(a2 < 0) {
      a2 = a2 + effect.rect_0c.h;
    }

    //LAB_800eb828
    if(a2 != 0) {
      this.applyRedEyeDragoonTransformationFlameArmorEffectTextureAnimations(effect.rect_0c, effect._1c, a2);
    }

    //LAB_800eb838
    return 1;
  }

  @ScriptDescription("Attached the red eye dragoon transformation flame armour effect to an effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex1", description = "The first texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex2", description = "The second texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attachmentStep", description = "The attachment step")
  @Method(0x800eb84cL)
  public FlowControl scriptAddRedEyeDragoonTransformationFlameArmorEffectAttachment(final RunningScript<?> script) {
    EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo1 = tmdType.textureInfo_08[script.params_20[1].get() * 2];
    final DeffPart.TextureInfo textureInfo2 = tmdType.textureInfo_08[script.params_20[2].get() * 2];

    //LAB_800eb8fc
    while(!manager.hasAttachment(10)) {
      final ScriptState<EffectManagerData6c<?>> parent = manager.parentScript_50;

      if(parent == null) {
        break;
      }

      manager = parent.innerStruct_00;
    }

    //LAB_800eb934
    final RedEyeDragoonTransformationFlameArmorEffect20 attachment = manager.addAttachment(10, 0, this::tickRedEyeDragoonTransformationFlameArmorEffect, new RedEyeDragoonTransformationFlameArmorEffect20());
    attachment.rect_0c.set(textureInfo1.vramPos_00);
    attachment.accumulator_14 = 0;
    attachment.step_18 = script.params_20[3].get();
    attachment._1c.x = textureInfo2.vramPos_00.x;
    attachment._1c.y = textureInfo2.vramPos_00.y;
    return FlowControl.CONTINUE;
  }

  @Method(0x800eb9acL)
  public void loadStageTmd(final BattleStage stage, final CContainer extTmd, final TmdAnimationFile tmdAnim) {
    final float x = stage.coord2_558.coord.transfer.x;
    final float y = stage.coord2_558.coord.transfer.y;
    final float z = stage.coord2_558.coord.transfer.z;

    stage_800bda0c = stage;

    //LAB_800eb9fc
    for(int i = 0; i < 10; i++) {
      stage._618[i] = 0;
    }

    stage.tmd_5d0 = extTmd.tmdPtr_00.tmd;

    if(extTmd.ptr_08 != null) {
      stage._5ec = extTmd.ptr_08;

      //LAB_800eba38
      for(int i = 0; i < 10; i++) {
        stage._5f0[i] = stage._5ec._00[i];
        this.FUN_800ec86c(stage, i);
      }
    } else {
      //LAB_800eba74
      //LAB_800eba7c
      for(int i = 0; i < 10; i++) {
        stage._5f0[i] = null;
      }
    }

    //LAB_800eba8c
    stage.dobj2s_00 = new ModelPart10[stage.tmd_5d0.header.nobj];
    Arrays.setAll(stage.dobj2s_00, i -> new ModelPart10());
    initObjTable2(stage.dobj2s_00);
    stage.coord2_558.transforms = stage.param_5a8;
    GsInitCoordinate2(null, stage.coord2_558);
    prepareObjTable2(stage.dobj2s_00, stage.tmd_5d0, stage.coord2_558);
    this.applyInitialStageTransforms(stage, tmdAnim);

    stage.coord2_558.coord.transfer.set(x, y, z);
    stage.flags_5e4 = 0;
    stage.z_5e8 = 0x200;
  }

  @Method(0x800ebd34L)
  public void applyBattleStageTextureAnimations(final BattleStage struct, final int index) {
    final short[] v0 = struct._5f0[index];

    if(v0 == null) {
      struct._618[index] = 0;
      return;
    }

    //LAB_800ebd84
    final int x = v0[0];
    final int y = v0[1];
    final int w = (short)(v0[2] / 4);
    final int h = v0[3];

    //LAB_800ebdcc
    int a2 = 4;

    // There was a loop here, but each iteration overwrote the results from the previous iteration... I collapsed it into a single iteration
    a2 += (struct._65e[index] - 1) * 2;
    final int t1 = (short)(v0[a2] & 1);
    final int t0 = (short)(v0[a2] >>> 1);
    int s0 = v0[a2 + 1];
    a2 += 2;

    //LAB_800ebdf0
    if((s0 & 0xf) != 0 && (struct._622[index] & 0xf) != 0) {
      struct._622[index]--;

      if(struct._622[index] == 0) {
        struct._622[index] = s0;
        s0 = 16;
      } else {
        //LAB_800ebe34
        s0 = 0;
      }
    }

    //LAB_800ebe38
    struct._64a[index]++;

    if(struct._64a[index] >= (short)t0) {
      struct._64a[index] = 0;

      if(v0[a2] != -1) {
        struct._65e[index]++;
      } else {
        //LAB_800ebe88
        struct._65e[index] = 1;
      }
    }

    //LAB_800ebe94
    if(s0 != 0) {
      final int s1 = s0 / 16;
      final int s4 = h - s1;

      final int s6;
      if(t1 == 0) {
        s6 = 256 + s1;

        GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, x, y, w, h));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + s4, 960, 256, w, s1));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, s6, w, s4));
      } else {
        //LAB_800ebf88
        s6 = 256 + s4;

        GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, x, y, w, h));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y, 960, s6, w, s1));
        GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + s1, 960, 256, w, s4));
      }
    }

    //LAB_800ec080
  }

  @Method(0x800ec4bcL)
  public void allocateStageDarkeningStorage() {
    stageDarkening_800c6958 = new BattleStageDarkening1800();
  }

  @Method(0x800ec4f0L)
  public void deallocateStageDarkeningStorage() {
    stageDarkening_800c6958 = null;
  }

  @Method(0x800ec51cL)
  public void renderBattleStage(final BattleStage stage) {
    //LAB_800ec548
    for(int i = 0; i < 10; i++) {
      if(stage._618[i] != 0) {
        this.applyBattleStageTextureAnimations(stage, i);
      }

      //LAB_800ec560
    }

    tmdGp0Tpage_1f8003ec = 0;
    zOffset_1f8003e8 = stage.z_5e8;

    //LAB_800ec5a0
    int partBit = 0x1;
    for(int i = 0; i < stage.dobj2s_00.length; i++) {
      final ModelPart10 part = stage.dobj2s_00[i];

      if((partBit & stage.flags_5e4) == 0) {
        if(stage.tmd_5d0.objTable[i] != null && stage.dobj2s_00[i].obj == null) {
          stage.dobj2s_00[i].obj = TmdObjLoader.fromObjTable("BattleStage (obj " + i + ')', stage.tmd_5d0.objTable[i]);
        }

        final MV ls = new MV();
        final MV lw = new MV();
        GsGetLws(part.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);
        Renderer.renderDobj2(part, true, 0);

        if(part.obj != null) {
          RENDERER.queueModel(part.obj, lw, QueuedModelBattleTmd.class)
            .depthOffset(stage.z_5e8 * 4)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour)
            .ctmdFlags((part.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0)
            .battleColour(this._800c6930.colour_00);
        }
      }

      //LAB_800ec608
      partBit <<= 1;
    }

    //LAB_800ec618
  }

  @Method(0x800ec63cL)
  public void applyStagePartAnimations(final BattleStage stage) {
    //LAB_800ec688
    for(int i = 0; i < stage.partCount_5dc; i++) {
      final Keyframe0c rotTrans = stage.rotTrans_5d8[0][i];
      final GsCOORDINATE2 coord2 = stage.dobj2s_00[i].coord2_04;
      final Transforms param = coord2.transforms;

      param.rotate.set(rotTrans.rotate_00);
      coord2.coord.rotationZYX(param.rotate);

      param.trans.set(rotTrans.translate_06);
      coord2.coord.transfer.set(param.trans);
    }

    //LAB_800ec710
    stage.rotTrans_5d8 = Arrays.copyOfRange(stage.rotTrans_5d8, 1, stage.rotTrans_5d8.length);
  }

  @Method(0x800ec744L)
  public void rotateBattleStage(final BattleStage stage) {
    stage.coord2_558.coord.rotationXYZ(stage.param_5a8.rotate);
    stage.coord2_558.flg = 0;
  }

  @Method(0x800ec774L)
  public void applyInitialStageTransforms(final BattleStage stage, final TmdAnimationFile anim) {
    stage.rotTrans_5d4 = anim.partTransforms_10;
    stage.rotTrans_5d8 = anim.partTransforms_10;
    stage.partCount_5dc = anim.modelPartCount_0c;
    stage.totalFrames_5de = anim.totalFrames_0e;
    stage.animationState_5e0 = 0;
    this.applyStagePartAnimations(stage);
    stage.animationState_5e0 = 1;
    stage.remainingFrames_5e2 = stage.totalFrames_5de;
    stage.rotTrans_5d8 = stage.rotTrans_5d4;
  }

  @Method(0x800ec86cL)
  public void FUN_800ec86c(final BattleStage stage, final int index) {
    final short[] a2 = stage._5f0[index];

    if(a2 == null) {
      stage._618[index] = 0;
      return;
    }

    //LAB_800ec890
    if(a2[0] == -1) {
      stage._5f0[index] = null;
      return;
    }

    //LAB_800ec8a8
    stage._618[index] = 1;
    stage._622[index] = a2[5];
    stage._64a[index] = 0;
    stage._65e[index] = 1;
  }

  /** Stage darkening for counterattacks change the clut, this saves a backup copy */
  @Method(0x800ec8d0L)
  public void backupStageClut(final FileData timFile) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958;

    //LAB_800ec8ec
    for(int a1 = 0; a1 < 0x10; a1++) {
      //LAB_800ec8f4
      for(int a3 = 0; a3 < 0x40; a3++) {
        darkening.original_000[a1][a3] = timFile.readShort(0x14 + a1 * 0x80 + a3 * 0x2);
      }
    }

    // I don't think this condition is used?
    if(timFile.readUShort(0x8812) == 0x7422) {
      stageDarkeningClutWidth_800c695c = timFile.readUShort(0x8812 + 0x4) + 1;
    } else {
      //LAB_800ec954
      stageDarkeningClutWidth_800c695c = 64;
    }
  }

  @ScriptDescription("Copies a block of vram from one position to another")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destX", description = "The destination X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destY", description = "The destination Y coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The block width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The block height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceX", description = "The source X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceY", description = "The source Y coordinate")
  @Method(0x800ee210L)
  public FlowControl scriptCopyVram(final RunningScript<?> script) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(script.params_20[4].get(), script.params_20[5].get(), script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get() / 4, (short)script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ee2acL)
  public FlowControl scriptSetBentZOffset(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.zOffset_a0 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's scale uniformly")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scale", description = "The uniform scale (12-bit fixed-point)")
  @Method(0x800ee2e4L)
  public FlowControl scriptSetBentScaleUniform(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final float scale = script.params_20[1].get() / (float)0x1000;
    bent.model_148.coord2_14.transforms.scale.set(scale, scale, scale);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's scale")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z scale (12-bit fixed-point)")
  @Method(0x800ee324L)
  public FlowControl scriptSetBentScale(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.scale.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Attaches a shadow to the bottom of a bent's model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee384L)
  public FlowControl scriptAttachShadowToBottomOfBentModel(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 2;
    bent.model_148.modelPartWithShadowIndex_cd = -1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Attaches a shadow to a bent's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartAttachmentIndex", description = "The model part index to attach the shadow to")
  @Method(0x800ee3c0L)
  public FlowControl scriptAttachShadowToBentModelPart(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 3;
    bent.model_148.modelPartWithShadowIndex_cd = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Updates a bent's shadow type based on its modelPartWithShadowIndex_cd")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee408L)
  public FlowControl scriptUpdateBentShadowType(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final int index = bent.model_148.modelPartWithShadowIndex_cd;
    if(index == -2) {
      //LAB_800ee450
      bent.model_148.shadowType_cc = 0;
    } else if(index == -1) {
      bent.model_148.shadowType_cc = 2;
    } else {
      //LAB_800ee458
      bent.model_148.shadowType_cc = 3;
    }

    //LAB_800ee460
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Disables a battle entity's shadow")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee468L)
  public FlowControl scriptDisableBentShadow(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's shadow size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The shadow's X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The shadow's Z size (12-bit fixed-point)")
  @Method(0x800ee49cL)
  public FlowControl scriptSetBentShadowSize(final RunningScript<?> script) {
    final BattleEntity27c a1 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1.model_148.shadowSize_10c.x = script.params_20[1].get() / (float)0x1000;
    a1.model_148.shadowSize_10c.z = script.params_20[2].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's shadow offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The shadow's X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The shadow's Y size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The shadow's Z size (12-bit fixed-point)")
  @Method(0x800ee4e8L)
  public FlowControl scriptSetBentShadowOffset(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowOffset_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Darkens the combat stage (for things like counter-attacks, certain dragoon transformations, etc.)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "intensity", description = "How intense the darkening effect should be")
  @Method(0x800ee548L)
  public FlowControl scriptApplyScreenDarkening(final RunningScript<?> script) {
    applyScreenDarkening(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of model parts in the battle stage's model")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "count", description = "The number of model parts")
  @Method(0x800ee574L)
  public FlowControl scriptGetStageNobj(final RunningScript<?> script) {
    script.params_20[0].set(stage_800bda0c.dobj2s_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Shows the battle stage's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @Method(0x800ee594L)
  public FlowControl scriptShowStageModelPart(final RunningScript<?> a0) {
    stage_800bda0c.flags_5e4 |= 0x1 << a0.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Hides the battle stage's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @Method(0x800ee5c0L)
  public FlowControl scriptHideStageModelPart(final RunningScript<?> a0) {
    stage_800bda0c.flags_5e4 &= ~(0x1 << a0.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle stage model's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ee5f0L)
  public FlowControl scriptSetStageZ(final RunningScript<?> script) {
    stage_800bda0c.z_5e8 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee610L)
  public void initBattleMenu() {
    this.countCombatUiFilesLoaded_800c6cf4 = 0;

    this.hud.clear();

    this.monsterCount_800c6b9c = 0;

    //LAB_800ee764
    for(int combatantIndex = 0; combatantIndex < 9; combatantIndex++) {
      this.monsterBents_800c6b78[combatantIndex] = -1;
      this.currentEnemyNames_800c69d0[combatantIndex] = null;
    }

    //LAB_800ee7b0
    for(int monsterSlot = 0; monsterSlot < 3; monsterSlot++) {
      //LAB_800ee7b8
      this.melbuMonsterNames_800c6ba8[monsterSlot] = null;
    }

    this.usedRepeatItems_800c6c3c.clear();
    this.dragoonSpaceElement_800c6b64 = null;

    //LAB_800ee894
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      spGained_800bc950[charSlot] = 0;
    }

    sortItems();
  }

  @Method(0x800ee8c4L)
  public void battleHudTexturesLoadedCallback(final List<FileData> files) {
    //LAB_800ee9c0
    for(int fileIndex = 0; fileIndex < files.size(); fileIndex++) {
      if(files.get(fileIndex).hasVirtualSize()) {
        final Tim tim = new Tim(files.get(fileIndex));

        if(fileIndex == 0) {
          GPU.uploadData15(new Rect4i(704, 256, 64, 256), tim.getImageData());
        }

        //LAB_800eea20
        final Rect4i rect = new Rect4i();
        if(fileIndex < 4) {
          rect.x = battleHudTextureVramXOffsets_800c6e60[fileIndex] + 704;
          rect.y = 496;
        } else {
          //LAB_800eea3c
          rect.x = battleHudTextureVramXOffsets_800c6e60[fileIndex] + 896;
          rect.y = 304;
        }

        //LAB_800eea50
        rect.w = combatUiElementRectDimensions_800c6e48[fileIndex].x;
        rect.h = combatUiElementRectDimensions_800c6e48[fileIndex].y;
        GPU.uploadData15(rect, tim.getClutData());
        this.countCombatUiFilesLoaded_800c6cf4++;
      }
    }
    //LAB_800eeaac
  }

  @Method(0x800eeaecL)
  public void updateGameStateAndDeallocateMenu() {
    //LAB_800eeb10
    //LAB_800eebb4
    //LAB_800eebd8
    for(int charSlot = 0; charSlot < battleState_8006e398.getPlayerCount(); charSlot++) {
      final PlayerBattleEntity bent = battleState_8006e398.playerBents_e40[charSlot].innerStruct_00;
      final CharacterData2c charData = gameState_800babc8.charData_32c[bent.charId_272];

      //LAB_800eec10
      charData.hp_08 = java.lang.Math.max(1, bent.stats.getStat(LodMod.HP_STAT.get()).getCurrent());

      if((gameState_800babc8.goods_19c[0] & 0x1 << characterDragoonIndices_800c6e68[bent.charId_272]) != 0) {
        charData.mp_0a = bent.stats.getStat(LodMod.MP_STAT.get()).getCurrent();
      }

      //LAB_800eec78
      if(bent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0x1 << characterDragoonIndices_800c6e68[9]) != 0) {
        charData.mp_0a = bent.stats.getStat(LodMod.MP_STAT.get()).getCurrent();
      }

      //LAB_800eecb8
      charData.status_10 = bent.status_0e & 0xc8;
      charData.sp_0c = bent.stats.getStat(LodMod.SP_STAT.get()).getCurrent();
    }

    //LAB_800eed78
    for(final Item item : this.usedRepeatItems_800c6c3c) {
      giveItem(item);
    }

    this.usedRepeatItems_800c6c3c.clear();

    this.hud.delete();
  }

  @Method(0x800ef28cL)
  public void initPlayerBattleEntityStats() {
    loadCharacterStats();
    characterStatsLoaded_800be5d0 = true;

    //LAB_800ef31c
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      this.dragoonSpells_800c6960[charSlot].charId_00 = -1;

      //LAB_800ef328
      for(int spellSlot = 0; spellSlot < 8; spellSlot++) {
        this.dragoonSpells_800c6960[charSlot].spellIndex_01[spellSlot] = -1;
      }
    }

    //LAB_800ef36c
    //LAB_800ef38c
    for(int charSlot = 0; charSlot < battleState_8006e398.getPlayerCount(); charSlot++) {
      final PlayerBattleEntity player = battleState_8006e398.playerBents_e40[charSlot].innerStruct_00;
      final int[] spellIndices = new int[8];
      getUnlockedDragoonSpells(spellIndices, player.charId_272);
      this.dragoonSpells_800c6960[charSlot].charId_00 = player.charId_272;

      //LAB_800ef3d8
      System.arraycopy(spellIndices, 0, this.dragoonSpells_800c6960[charSlot].spellIndex_01, 0, 8);

      //LAB_800ef400
      final VitalsStat playerHp = player.stats.getStat(LodMod.HP_STAT.get());
      final VitalsStat playerMp = player.stats.getStat(LodMod.MP_STAT.get());
      final VitalsStat playerSp = player.stats.getStat(LodMod.SP_STAT.get());

      final ActiveStatsa0 stats = stats_800be5f8[player.charId_272];
      player.level_04 = stats.level_0e;
      player.dlevel_06 = stats.dlevel_0f;
      playerHp.setCurrent(stats.hp_04);
      playerHp.setMaxRaw(stats.maxHp_66);
      playerSp.setCurrent(stats.sp_08);
      playerSp.setMaxRaw(stats.dlevel_0f * 100);
      playerMp.setCurrent(stats.mp_06);
      playerMp.setMaxRaw(stats.maxMp_6e);
      player.status_0e = stats.flags_0c;
      player.specialEffectFlag_14 = stats.specialEffectFlag_76;
      //      player.equipmentType_16 = stats.equipmentType_77;
      player.equipment_02_18 = stats.equipment_02_78;
      player.equipmentEquipableFlags_1a = stats.equipmentEquipableFlags_79;
      player.equipmentAttackElements_1c.set(stats.equipmentAttackElements_7a);
      player.equipment_05_1e = stats.equipment_05_7b;
      player.equipmentElementalResistance_20.set(stats.equipmentElementalResistance_7c);
      player.equipmentElementalImmunity_22.set(stats.equipmentElementalImmunity_7d);
      player.equipmentStatusResist_24 = stats.equipmentStatusResist_7e;
      player.equipment_09_26 = stats.equipment_09_7f;
      player.equipmentAttack1_28 = stats.equipmentAttack1_80;
      player._2e = stats._83;
      player.equipmentIcon_30 = stats.equipmentIcon_84;
      player.stats.getStat(LodMod.SPEED_STAT.get()).setRaw(stats.equipmentSpeed_86 + stats.bodySpeed_69);
      player.attack_34 = stats.equipmentAttack_88 + stats.bodyAttack_6a;
      player.magicAttack_36 = stats.equipmentMagicAttack_8a + stats.bodyMagicAttack_6b;
      player.defence_38 = stats.equipmentDefence_8c + stats.bodyDefence_6c;
      player.magicDefence_3a = stats.equipmentMagicDefence_8e + stats.bodyMagicDefence_6d;
      player.attackHit_3c = stats.equipmentAttackHit_90;
      player.magicHit_3e = stats.equipmentMagicHit_92;
      player.attackAvoid_40 = stats.equipmentAttackAvoid_94;
      player.magicAvoid_42 = stats.equipmentMagicAvoid_96;
      player.onHitStatusChance_44 = stats.equipmentOnHitStatusChance_98;
      player.equipment_19_46 = stats.equipment_19_99;
      player.equipment_1a_48 = stats.equipment_1a_9a;
      player.equipmentOnHitStatus_4a = stats.equipmentOnHitStatus_9b;
      player.spellId_4e = stats.equipmentOnHitStatus_9b; //TODO retail bug?
      player.selectedAddition_58 = stats.selectedAddition_35;
      player.dragoonAttack_ac = stats.dragoonAttack_72;
      player.dragoonMagic_ae = stats.dragoonMagicAttack_73;
      player.dragoonDefence_b0 = stats.dragoonDefence_74;
      player.dragoonMagicDefence_b2 = stats.dragoonMagicDefence_75;
      player.physicalImmunity_110 = stats.equipmentPhysicalImmunity_46;
      player.magicalImmunity_112 = stats.equipmentMagicalImmunity_48;
      player.physicalResistance_114 = stats.equipmentPhysicalResistance_4a;
      player.magicalResistance_116 = stats.equipmentMagicalResistance_60;
      player._118 = stats.addition_00_9c;
      player.additionSpMultiplier_11a = stats.additionSpMultiplier_9e;
      player.additionDamageMultiplier_11c = stats.additionDamageMultiplier_9f;
      player.equipment_11e.clear();
      player.equipment_11e.putAll(stats.equipment_30);
      player.spMultiplier_128 = stats.equipmentSpMultiplier_4c;
      player.spPerPhysicalHit_12a = stats.equipmentSpPerPhysicalHit_4e;
      player.mpPerPhysicalHit_12c = stats.equipmentMpPerPhysicalHit_50;
      player.spPerMagicalHit_12e = stats.equipmentSpPerMagicalHit_52;
      player.mpPerMagicalHit_130 = stats.equipmentMpPerMagicalHit_54;
      player.escapeBonus_132 = stats.equipmentEscapeBonus_56;
      player.hpRegen_134 = stats.equipmentHpRegen_58;
      player.mpRegen_136 = stats.equipmentMpRegen_5a;
      player.spRegen_138 = stats.equipmentSpRegen_5c;
      player.revive_13a = stats.equipmentRevive_5e;
      player.hpMulti_13c = stats.equipmentHpMulti_62;
      player.mpMulti_13e = stats.equipmentMpMulti_64;
      player._142 = stats.equipmentOnHitStatus_9b;
    }

    //LAB_800ef798
  }

  @Method(0x800f1aa8L)
  public boolean checkHit(final int attackerIndex, final int defenderIndex, final AttackType attackType) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[attackerIndex].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[defenderIndex].innerStruct_00;
    final boolean isMonster = attacker instanceof MonsterBattleEntity;

    int effectAccuracy;
    if(attackType == AttackType.PHYSICAL) {
      attacker.setTempSpellStats();

      if(isMonster) {
        effectAccuracy = attacker.spell_94.accuracy_05;
      } else {
        //LAB_800f1bf4
        effectAccuracy = attacker.attackHit_3c;
      }
    } else if(attackType == AttackType.DRAGOON_MAGIC_STATUS_ITEMS) {
      //LAB_800f1c08
      attacker.setTempSpellStats();
      effectAccuracy = attacker.spell_94.accuracy_05;
    } else {
      //LAB_800f1c38
      effectAccuracy = 100;
    }

    //LAB_800f1c44
    final int hitStat = (byte)attacker.getStat(attackType.tempHitStat);
    effectAccuracy = effectAccuracy * (hitStat + 100) / 100;

    final int avoidChance;
    if(attackType == AttackType.PHYSICAL) {
      avoidChance = defender.attackAvoid_40;
    } else {
      //LAB_800f1c9c
      avoidChance = defender.magicAvoid_42;
    }

    boolean effectHit = false;

    //LAB_800f1ca8
    final int modifiedAvoidChance = (avoidChance * ((byte)attacker.getStat(attackType.tempAvoidStat) + 100)) / 100;
    if(modifiedAvoidChance < effectAccuracy && effectAccuracy - modifiedAvoidChance >= (simpleRand() * 101 >> 16)) {
      effectHit = true;

      if(isMonster) {
        attacker.setTempSpellStats();
      }
    }

    //LAB_800f1d28
    if(attackType == AttackType.ITEM_MAGIC) {
      if(attacker.item_d4.alwaysHits()) {
        effectHit = true;
      }
    } else if((attacker.getStat(attackType.alwaysHitStat) & attackType.alwaysHitMask) != 0) {
      effectHit = true;
    }

    //LAB_800f1d5c
    return effectHit;
  }

  public static int adjustDamageForPower(final int damage, final int attackerStat, final int defenderStat) {
    float base = 1.0f;

    if(attackerStat < 0) {
      base /= 2.0f;
    }

    if(defenderStat > 0) {
      base /= 2.0f;
    }

    if(attackerStat > 0) {
      base += 0.5f;
    }

    if(defenderStat < 0) {
      base += 0.5f;
    }

    return (int)(damage * base);
  }

  /**
   * @param magicType item (0), spell (1)
   */
  @Method(0x800f204cL)
  public int calculateMagicDamage(final BattleEntity27c attacker, final BattleEntity27c defender, final int magicType) {
    // Stat mod item
    if(magicType == 0 && attacker.item_d4.isStatMod()) {
      return attacker.item_d4.calculateStatMod(attacker, defender);
    }

    //LAB_800f2140
    int damage;
    if(attacker.spell_94 != null && (attacker.spell_94.flags_01 & 0x4) != 0) {
      damage = defender.stats.getStat(LodMod.HP_STAT.get()).getMax() * attacker.spell_94.multi_04 / 100;

      final List<BattleEntity27c> targets = new ArrayList<>();
      if((attacker.spell_94.targetType_00 & 0x8) != 0) { // Attack all
        if(attacker instanceof PlayerBattleEntity) {
          for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
            targets.add(battleState_8006e398.playerBents_e40[i].innerStruct_00);
          }
        } else {
          for(int i = 0; i < battleState_8006e398.getAliveMonsterCount(); i++) {
            targets.add(battleState_8006e398.aliveMonsterBents_ebc[i].innerStruct_00);
          }
        }
      } else { // Attack single
        targets.add(defender);
      }

      for(final BattleEntity27c target : targets) {
        applyBuffOrDebuff(attacker, target);
      }

      //LAB_800f2224
      attacker.status_0e |= 0x800;
    } else {
      final Element attackElement = magicType == 1 ? attacker.spell_94.element_08.get() : attacker.item_d4.getAttackElement();
      final AttackType attackType = magicType == 1 ? AttackType.DRAGOON_MAGIC_STATUS_ITEMS : AttackType.ITEM_MAGIC;

      //LAB_800f2238
      damage = attacker.calculateMagicDamage(defender, magicType);
      damage = attackElement.adjustAttackingElementalDamage(attackType, damage, defender.getElement());
      damage = defender.getElement().adjustDefendingElementalDamage(attackType, damage, attackElement);
      damage = adjustDamageForPower(damage, attacker.powerMagicAttack_b6, defender.powerMagicDefence_ba);

      if(this.dragoonSpaceElement_800c6b64 != null) {
        damage = attackElement.adjustDragoonSpaceDamage(attackType, damage, this.dragoonSpaceElement_800c6b64);
      }
    }

    //LAB_800f24c0
    if(damage < 0) {
      damage = 0;
    }

    //LAB_800f24d0
    return damage;
  }

  @ScriptDescription("Perform a battle entity's physical attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2500L)
  public FlowControl scriptPhysicalAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    final int damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.PHYSICAL, CoreMod.PHYSICAL_DAMAGE_FORMULA.calculate(attacker, defender))).damage;

    script.params_20[2].set(damage);
    script.params_20[3].set(this.determineAttackSpecialEffects(attacker, defender, AttackType.PHYSICAL));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's magic or status attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spellId", description = "The attacker's spell ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2694L)
  public FlowControl scriptDragoonMagicStatusItemAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.spellId_4e = script.params_20[2].get();

    attacker.clearTempWeaponAndSpellStats();
    attacker.setTempSpellStats();

    int damage = this.calculateMagicDamage(attacker, defender, 1);
    damage = applyMagicDamageMultiplier(attacker, defender, damage, 0);
    damage = java.lang.Math.max(1, damage);

    //LAB_800f272c
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.DRAGOON_MAGIC_STATUS_ITEMS);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.spell_94.element_08.get());
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS, damage)).damage;

    //LAB_800f27ec
    script.params_20[3].set(damage);
    script.params_20[4].set(this.determineAttackSpecialEffects(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's item attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused parameter")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2838L)
  public FlowControl scriptItemMagicAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.spell_94 = null;

    int damage = this.calculateMagicDamage(attacker, defender, 0);
    damage = applyMagicDamageMultiplier(attacker, defender, damage, 1);
    damage = java.lang.Math.max(1, damage);

    //LAB_800f28c8
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.ITEM_MAGIC);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.item_d4.getAttackElement());
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.ITEM_MAGIC, damage)).damage;

    //LAB_800f2970
    script.params_20[3].set(damage);
    script.params_20[4].set(this.determineAttackSpecialEffects(attacker, defender, AttackType.ITEM_MAGIC));
    this.applyItemSpecialEffects(attacker, defender);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives SP to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to add")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "total", description = "The total SP after adding the amount requested")
  @Method(0x800f43dcL)
  public FlowControl scriptGiveSp(final RunningScript<?> script) {
    //LAB_800f43f8
    //LAB_800f4410
    //LAB_800f4430
    final PlayerBattleEntity player = SCRIPTS.getObject(script.params_20[0].get(), PlayerBattleEntity.class);
    final VitalsStat sp = player.stats.getStat(LodMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() + script.params_20[1].get());
    spGained_800bc950[player.charSlot_276] += script.params_20[1].get();

    //LAB_800f4500
    script.params_20[2].set(sp.getCurrent());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Consumes SP from a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to take away")
  @Method(0x800f4518L)
  public FlowControl scriptConsumeSp(final RunningScript<?> script) {
    //LAB_800f4534
    //LAB_800f454c
    //LAB_800f456c
    final PlayerBattleEntity player = SCRIPTS.getObject(script.params_20[0].get(), PlayerBattleEntity.class);
    final VitalsStat sp = player.stats.getStat(LodMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() - script.params_20[2].get());

    if(sp.getCurrent() == 0) {
      this.hud.clearFullSpFlags(player.charSlot_276);
    }

    //LAB_800f45f8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, this might handle players selecting an attack target")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "selectionState", description = "0 - nothing selected, 1 - item/spell selected, -1 - menu unloading")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemOrSpellId", description = "The item or spell ID selected")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "registryId", description = "The item registry ID (not used for spells yet")
  @Method(0x800f4600L)
  public FlowControl scriptGetItemOrSpellTargetingInfo(final RunningScript<?> script) {
    this.hud.listMenu_800c6b60.getTargetingInfo(script);

    //LAB_800f4800
    //LAB_800f4804
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the item/spell attack target")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetMode")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @Method(0x800f480cL)
  public FlowControl scriptGetItemOrSpellAttackTarget(final RunningScript<?> script) {
    final int[] sp0x10 = {0, 0, 1, 0, 2, 1, 1, 1};

    int targetMode = script.params_20[0].get();

    final BattleMenuStruct58 menu = this.hud.battleMenu_800c6c34;

    //LAB_800f489c
    //LAB_800f48d8
    if((menu.player_04.specialEffectFlag_14 & 0x8) != 0) { // "Attack all"
      targetMode = 3;
    }

    //LAB_800f48f4
    int ret = this.hud.handleTargeting(sp0x10[targetMode * 2], sp0x10[targetMode * 2 + 1] != 0);
    if(ret == 0) { // No buttons pressed
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(ret == 1) { // Pressed X
      //LAB_800f4930
      ret = menu.target_48;
    } else { // Pressed O
      //LAB_800f4944
      //LAB_800f4948
      ret = -1;
    }

    //LAB_800f4950
    script.params_20[1].set(ret);

    //LAB_800f4954
    return FlowControl.CONTINUE;
  }

  @Method(0x800f7c5cL)
  public int determineAttackSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender, final AttackType attackType) {
    if(attackType == AttackType.ITEM_MAGIC) {
      return attacker.item_d4.getSpecialEffect(attacker, defender);
    }

    final BattleEntityStat[] statusEffectChances = {BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE}; // onHitStatusChance, statusChance, onHitStatusChance, statusChance, statusChance, onHitStatusChance
    final BattleEntityStat[] statusEffectStats = {BattleEntityStat.EQUIPMENT_ON_HIT_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS}; // onHitStatus, statusType, itemStatus, statusType, statusType, itemStatus
    final BattleEntityStat[] specialEffectStats = {BattleEntityStat.SPECIAL_EFFECT_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET}; // specialEffectFlag, spellFlags, itemTarget, spellFlags, spellFlags, itemTarget
    final int[] specialEffectMasks = {0x40, 0xf0, 0x80, 0xf0, 0xf0, 0x80};

    final boolean isAttackerMonster = attacker instanceof MonsterBattleEntity;

    final int index = (!isAttackerMonster ? 0 : 3) + attackType.ordinal(); //TODO

    final int effectChance = attacker.getStat(statusEffectChances[index]);

    //LAB_800f7e98
    int effect = -1;
    if(simpleRand() * 101 >> 16 < effectChance) {
      final int statusType = attacker.getStat(statusEffectStats[index]);

      if((statusType & 0xff) != 0) {
        //LAB_800f7eec
        int statusIndex;
        for(statusIndex = 0; statusIndex < 8; statusIndex++) {
          if((statusType & (0x80 >> statusIndex)) != 0) {
            break;
          }
        }

        //LAB_800f7f0c
        effect = 0x80 >> statusIndex;
      }

      //LAB_800f7f14
      final int specialEffects = attacker.getStat(specialEffectStats[index]) & specialEffectMasks[index];
      if(specialEffects != 0) {
        if(!isAttackerMonster && attackType != AttackType.DRAGOON_MAGIC_STATUS_ITEMS || specialEffects == 0x80) {
          effect = 0;
        }

        //LAB_800f7fc8
        if((defender.specialEffectFlag_14 & 0x80) != 0) { // Resistance
          effect = -1;
        }
      }
    }

    //LAB_800f7fe0
    //LAB_800f7fe4
    return effect;
  }

  @Method(0x800f84c8L)
  public void loadBattleHudTextures() {
    loadDrgnDir(0, 4113, this::battleHudTexturesLoadedCallback);
  }

  @Method(0x800f8670L)
  public void loadMonster(final ScriptState<MonsterBattleEntity> state) {
    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      this.melbuMonsterNames_800c6ba8[i] = monsterNames_80112068[melbuMonsterNameIndices_800c6e90[i]];
    }

    final MonsterBattleEntity monster = state.innerStruct_00;
    this.currentEnemyNames_800c69d0[this.monsterCount_800c6b9c] = monsterNames_80112068[monster.charId_272];

    //LAB_800eefa8
    this.monsterBents_800c6b78[this.monsterCount_800c6b9c] = state.index;
    this.monsterCount_800c6b9c++;

    //LAB_800eefcc
    final MonsterStats1c monsterStats = monsterStats_8010ba98[monster.charId_272];

    final MonsterStatsEvent statsEvent = EVENTS.postEvent(new MonsterStatsEvent(monster.charId_272));

    final VitalsStat monsterHp = monster.stats.getStat(LodMod.HP_STAT.get());
    monsterHp.setCurrent(statsEvent.hp);
    monsterHp.setMaxRaw(statsEvent.maxHp);
    monster.specialEffectFlag_14 = statsEvent.specialEffectFlag;
//    monster.equipmentType_16 = 0;
    monster.equipment_02_18 = 0;
    monster.equipmentEquipableFlags_1a = 0;
    monster.element = statsEvent.elementFlag;
    monster.equipment_05_1e = monsterStats._0e;
    monster.equipmentElementalImmunity_22.set(statsEvent.elementalImmunityFlag);
    monster.equipmentStatusResist_24 = statsEvent.statusResistFlag;
    monster.equipment_09_26 = 0;
    monster.equipmentAttack1_28 = 0;
    monster._2e = 0;
    monster.equipmentIcon_30 = 0;
    monster.stats.getStat(LodMod.SPEED_STAT.get()).setRaw(statsEvent.speed);
    monster.attack_34 = statsEvent.attack;
    monster.magicAttack_36 = statsEvent.magicAttack;
    monster.defence_38 = statsEvent.defence;
    monster.magicDefence_3a = statsEvent.magicDefence;
    monster.attackHit_3c = 0;
    monster.magicHit_3e = 0;
    monster.attackAvoid_40 = statsEvent.attackAvoid;
    monster.magicAvoid_42 = statsEvent.magicAvoid;
    monster.onHitStatusChance_44 = 0;
    monster.equipment_19_46 = 0;
    monster.equipment_1a_48 = 0;
    monster.equipmentOnHitStatus_4a = 0;
    monster.targetArrowPos_78.set(monsterStats.targetArrowX_12, monsterStats.targetArrowY_13, monsterStats.targetArrowZ_14);
    monster.hitCounterFrameThreshold_7e = monsterStats.hitCounterFrameThreshold_15;
    monster._80 = monsterStats._16;
    monster._82 = monsterStats._17;
    monster.middleOffsetX_84 = monsterStats.middleOffsetX_18;
    monster.middleOffsetY_86 = monsterStats.middleOffsetY_19;
    monster._88 = monsterStats._1a;
    monster._8a = monsterStats._1b;

    monster.damageReductionFlags_6e = monster.specialEffectFlag_14;
    monster._70 = monster.equipment_05_1e;
    monster.monsterElementalImmunity_74.set(monster.equipmentElementalImmunity_22);
    monster.monsterStatusResistFlag_76 = monster.equipmentStatusResist_24;

    if((monster.damageReductionFlags_6e & 0x8) != 0) {
      monster.physicalImmunity_110 = true;
    }

    //LAB_800ef25c
    if((monster.damageReductionFlags_6e & 0x4) != 0) {
      monster.magicalImmunity_112 = true;
    }
  }

  @Method(0x800f8854L)
  public void applyItemSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender) {
    attacker.item_d4.applyBuffs(attacker, defender);
    defender.recalculateSpeedAndPerHitStats();
  }

  @Method(0x800f9380L)
  public static void applyBuffOrDebuff(final BattleEntity27c attacker, final BattleEntity27c defender) {
    final BattleEntityStat[] stats = {BattleEntityStat.POWER_DEFENCE, BattleEntityStat.POWER_MAGIC_DEFENCE, BattleEntityStat.POWER_ATTACK, BattleEntityStat.POWER_MAGIC_ATTACK};

    for(int i = 0; i < 8; i++) {
      // This has been intentionally changed to attacker.buffType. Defender.buffType was always set to attacker.buffType anyway.
      if((attacker.spell_94.buffType_0a & (0x80 >> i)) != 0) {
        final int turnCount = attacker.charId_272 != defender.charId_272 ? 3 : 4;
        final int amount = i < 4 ? 50 : -50;

        defender.setStat(stats[i % 4], turnCount << 8 | (amount & 0xff));
      }
    }
  }

  /**
   * @param magicType spell (0), item (1)
   */
  @Method(0x800f946cL)
  public static int applyMagicDamageMultiplier(final BattleEntity27c attacker, final BattleEntity27c defender, final int damage, final int magicType) {
    final int damageMultiplier;
    if(magicType == 0) {
      damageMultiplier = spellStats_800fa0b8[attacker.spellId_4e].damageMultiplier_03;
    } else {
      //LAB_800f949c
      damageMultiplier = attacker.item_d4.getAttackDamageMultiplier(attacker, defender);
    }

    if(damageMultiplier == 0x1) {
      //LAB_800f9570
      return damage * 8;
    }

    if(damageMultiplier == 0x2) {
      //LAB_800f9564
      return damage * 6;
    }

    //LAB_800f94d8
    if(damageMultiplier == 0x4) {
      //LAB_800f955c
      return damage * 5;
    }

    //LAB_800f94a0
    if(damageMultiplier == 0x8) {
      //LAB_800f9554
      return damage * 4;
    }

    if(damageMultiplier == 0x10) {
      //LAB_800f954c
      return damage * 3;
    }

    //LAB_800f94ec
    if(damageMultiplier == 0x20) {
      //LAB_800f9544
      return damage * 2;
    }

    //LAB_800f9510
    if(damageMultiplier == 0x40) {
      //LAB_800f9534
      return damage + damage / 2;
    }

    if(damageMultiplier == 0x80) {
      return damage / 2;
    }

    //LAB_800f9578
    //LAB_800f957c
    return damage;
  }

  @ScriptDescription("Checks if a battle entity's physical attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f95d0L)
  public FlowControl scriptCheckPhysicalHit(final RunningScript<?> script) {
    script.params_20[2].set(this.checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.PHYSICAL) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's spell or status attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9618L)
  public FlowControl scriptCheckSpellOrStatusHit(final RunningScript<?> script) {
    script.params_20[2].set(this.checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.DRAGOON_MAGIC_STATUS_ITEMS) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's item magic attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9660L)
  public FlowControl scriptCheckItemHit(final RunningScript<?> script) {
    script.params_20[2].set(this.checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.ITEM_MAGIC) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f96a8L)
  public FlowControl scriptSetTempSpellStats(final RunningScript<?> script) {
    SCRIPTS.getObject(script.params_20[0].get(), BattleEntity27c.class).setTempSpellStats();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800f96d4L)
  public FlowControl scriptGetBentPos2(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(java.lang.Math.round(bent.model_148.coord2_14.coord.transfer.x));
    script.params_20[2].set(java.lang.Math.round(bent.model_148.coord2_14.coord.transfer.y));
    script.params_20[3].set(java.lang.Math.round(bent.model_148.coord2_14.coord.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a floating number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "number", description = "The number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y coordinate")
  @Method(0x800f9730L)
  public FlowControl scriptAddFloatingNumber(final RunningScript<?> script) {
    this.hud.addFloatingNumber(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialized the battle menu for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "menuType", description = "0 = items, 1 = spells, 2 = ?")
  @Method(0x800f97d8L)
  public FlowControl scriptInitSpellAndItemMenu(final RunningScript<?> script) {
    playSound(0, 4, (short)0, (short)0);
    this.hud.initListMenu(SCRIPTS.getObject(script.params_20[0].get(), PlayerBattleEntity.class), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Render recovery amount for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount recovered")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "colourIndex", description = "Which colour to use (indices are unknown)")
  @Method(0x800f984cL)
  public FlowControl scriptRenderRecover(final RunningScript<?> script) {
    this.hud.addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9884L)
  public FlowControl scriptSetTempItemMagicStats(final RunningScript<?> script) {
//    SCRIPTS.getObject(script.params_20[0].get(), BattleEntity27c.class).setActiveItem();
    //TODO item should already be set on bent?
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes a specific (or random) item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "itemId", description = "The item ID (or null to take a random item)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "itemTaken", description = "The item ID that was taken (or null if none could be taken)")
  @Method(0x800f98b0L)
  public FlowControl scriptTakeItem(final RunningScript<?> script) {
    final RegistryId itemId = script.params_20[0].getRegistryId();

    if(gameState_800babc8.items_2e9.isEmpty()) {
      script.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    Item item;
    if(itemId == null) {
      item = gameState_800babc8.items_2e9.get((simpleRand() * gameState_800babc8.items_2e9.size()) >> 16);

      if(item.isProtected()) {
        item = null;
      }
    } else {
      item = REGISTRIES.items.getEntry(itemId).get();
    }

    //LAB_800f9988
    //LAB_800f99a4
    if(item == null || !takeItemId(item)) {
      script.params_20[1].set(0);
    } else {
      script.params_20[1].set(item.getRegistryId());
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives a specific item to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.REG, name = "itemId", description = "The item ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.REG, name = "itemGiven", description = "The item ID that was given (or null if none could be given)")
  @Method(0x800f99ecL)
  public FlowControl scriptGiveItem(final RunningScript<?> script) {
    final RegistryId itemId = script.params_20[0].getRegistryId();

    if(giveItem(REGISTRIES.items.getEntry(itemId).get())) {
      script.params_20[1].set(itemId);
    } else {
      script.params_20[1].set(0);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the current target in the battle HUD")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetType", description = "0 = characters, 1 = monsters, 2 = any")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index")
  @Method(0x800f9a50L)
  public FlowControl scriptSetHudTargetBobj(final RunningScript<?> script) {
    final int targetType = script.params_20[0].get();
    final int targetBent = script.params_20[1].get();

    final ScriptState<? extends BattleEntity27c>[] bents = battleState_8006e398.getBentsForTargetType(targetType);

    //LAB_800f9abc
    //LAB_800f9adc
    for(int i = 0; i < bents.length; i++) {
      if(bents[i] != null && targetBent == bents[i].index) {
        if(targetType == 0) {
          this.hud.battleMenu_800c6c34.targetedPlayerSlot_800c6980 = i;
        } else if(targetType == 1) {
          //LAB_800f9b0c
          this.hud.battleMenu_800c6c34.targetedMonsterSlot_800c697e = i;
        }

        break;
      }

      //LAB_800f9b14
    }

    //LAB_800f9b24
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if any floating number is on the screen")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "onScreen", description = "True if any floating number is on screen, false otherwise")
  @Method(0x800f9b2cL)
  public FlowControl scriptIsFloatingNumberOnScreen(final RunningScript<?> script) {
    //LAB_800f9b3c
    //LAB_800f9b64
    script.params_20[0].set(this.hud.isFloatingNumberOnScreen() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the active dragoon space element")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID whose element should be used")
  @Method(0x800f9b78L)
  public FlowControl scriptSetDragoonSpaceElementIndex(final RunningScript<?> script) {
    final int characterId = script.params_20[0].get();

    this.dragoonSpaceElement_800c6b64 = null;

    if(characterId != -1) {
      if(characterId == 9) { //TODO stupid special case handling for DD Dart
        this.dragoonSpaceElement_800c6b64 = LodMod.DIVINE_ELEMENT.get();
      } else {
        this.dragoonSpaceElement_800c6b64 = battleState_8006e398.getPlayerById(characterId).element;
      }
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800f9b94L)
  public FlowControl FUN_800f9b94(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9bd4L)
  public FlowControl FUN_800f9bd4(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9c00L)
  public FlowControl FUN_800f9c00(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  private UiBox scriptUi;

  @ScriptDescription("Renders the battle HUD background")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "colourIndex", description = "The textboxColour index")
  @Method(0x800f9c2cL)
  public FlowControl scriptRenderBattleHudBackground(final RunningScript<?> script) {
    final int colourIndex = script.params_20[4].get();
    final int r = textboxColours_800c6fec[colourIndex][0];
    final int g = textboxColours_800c6fec[colourIndex][1];
    final int b = textboxColours_800c6fec[colourIndex][2];

    // This is kinda dumb since we'll have to upload a new box each frame, but there isn't a great
    // way to deal with it. Maybe check to see if any of the params have changed before deleting?

    if(this.scriptUi != null) {
      this.scriptUi.delete();
    }

    this.scriptUi = new UiBox(
      "Scripted Battle UI",
      (short)script.params_20[0].get() - script.params_20[2].get() / 2,
      (short)script.params_20[1].get() - script.params_20[3].get() / 2,
      (short)script.params_20[2].get(),
      (short)script.params_20[3].get()
    );

    this.scriptUi.render(r / 255.0f, g / 255.0f, b / 255.0f);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, disables menu icons if certain flags are set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "iconIndicesBitset", description = "The icons to disable if their flag matches a certain value (unknown)")
  @Method(0x800f9cacL)
  public FlowControl scriptSetDisabledMenuIcons(final RunningScript<?> script) {
    final int disabledIconsBitset = script.params_20[0].get();
    this.hud.setDisabledIcons(disabledIconsBitset);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Called after any battle entity finishes its turn, ticks temporary stats and calls turnFinished on custom battle entities")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9d7cL)
  public FlowControl scriptFinishBentTurn(final RunningScript<?> script) {
    final int bentIndex = script.params_20[0].get();
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;

    bent.turnFinished();
    bent.recalculateSpeedAndPerHitStats();
    return FlowControl.CONTINUE;
  }

  @Method(0x80109050L)
  private void loadStageDataAndControllerScripts() {
    this.currentStageData_800c6718 = getEncounterStageData(encounterId_800bb0f8);

    this.playerBattleScript_800c66fc = new ScriptFile("player_combat_script", Loader.loadFile("player_combat_script").getBytes());

    loadDrgnFile(1, "401", this::combatControllerScriptLoaded);
  }

  @Method(0x80109170L)
  private void combatControllerScriptLoaded(final FileData file) {
    this.scriptState_800c674c = SCRIPTS.allocateScriptState(5, "DRGN1.401", null);
    this.scriptState_800c674c.loadScriptFile(new ScriptFile("DRGN1.401", file.getBytes()));

    final int openingCamera;
    if((simpleRand() & 0x8000) == 0) {
      openingCamera = this.currentStageData_800c6718.monsterOpeningCamera_14;
    } else {
      openingCamera = this.currentStageData_800c6718.playerOpeningCamera_10;
    }

    //LAB_801091dc
    this.cameraScriptMainTableJumpIndex_800c6748 = openingCamera + 1;
    this.hud.currentCameraPositionIndicesIndex_800c66b0 = simpleRand() & 3;
    this.currentCameraIndex_800c6780 = this.currentStageData_800c6718.cameraPosIndices_18[this.hud.currentCameraPositionIndicesIndex_800c66b0];
    battleFlags_800bc960 |= 0x2;
  }

  @Method(0x80109808L)
  public void loadEnemyDropsAndScript(final int enemyAndCombatantId) {
    final int enemyId = enemyAndCombatantId & 0xffff;
    final int combatantIndex = enemyAndCombatantId >>> 16;
    final CombatantStruct1a8 combatant = this.getCombatant(combatantIndex);
    final EnemyRewards08 rewards = enemyRewards_80112868[enemyId];

    combatant.drops.clear();
    if(rewards.itemDrop_05 != null) {
      combatant.drops.add(new CombatantStruct1a8.ItemDrop(rewards.itemChance_04, rewards.itemDrop_05.get()));
    }

    final EnemyRewardsEvent event = EVENTS.postEvent(new EnemyRewardsEvent(enemyId, rewards.xp_00, rewards.gold_02, combatant.drops));

    combatant.xp_194 = event.xp;
    combatant.gold_196 = event.gold;
    combatant._19a = rewards._06;

    loadDrgnFile(1, Integer.toString(enemyId + 1), file -> this.loadCombatantScript(file.getBytes(), combatantIndex));
  }

  @Method(0x8010989cL)
  public void loadCombatantScript(final byte[] file, final int index) {
    this.getCombatant(index).scriptPtr_10 = new ScriptFile("Combatant " + index, file);
  }

  @Method(0x801098f4L)
  public void loadStageAmbiance() {
    final DeffManager7cc deffManager = deffManager_800c693c;
    final int stage = java.lang.Math.max(0, battleStage_800bb0f4);

    //LAB_8010993c
    //LAB_80109954
    deffManager.stageAmbiance_4c.set(stageAmbiance_801134fc[stage]);
    this.applyStageAmbiance(deffManager.stageAmbiance_4c);

    //LAB_8010999c
    for(int i = 0; i < deffManager.dragoonSpaceAmbiance_98.length; i++) {
      deffManager.dragoonSpaceAmbiance_98[i].set(stageAmbiance_801134fc[71 + i]);
    }

    final StageDeffThing08 thing = _8011517c[battleStage_800bb0f4];
    deffManager._00._00 = thing._00;
    deffManager._00._02 = thing._02;
    deffManager._00._04 = thing._04;

    //LAB_80109a30
    for(int i = 0; melbuStageIndices_800fb064[i] != -1; i++) {
      deffManager._08[i]._00 = thing._00;
      deffManager._08[i]._02 = thing._02;
    }
  }
}
