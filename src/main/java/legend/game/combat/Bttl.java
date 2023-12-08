package legend.game.combat;

import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.Random;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.FloatRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.opengl.McqBuilder;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.EngineStateEnum;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.characters.Element;
import legend.game.characters.TurnBasedPercentileBuff;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.AttackEvent;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityStat;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.BattleStruct24_2;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.deff.LmbType0;
import legend.game.combat.effects.AdditionCharEffectData0c;
import legend.game.combat.effects.AdditionNameTextEffect1c;
import legend.game.combat.effects.AdditionSparksEffect08;
import legend.game.combat.effects.AdditionStarburstEffect10;
import legend.game.combat.effects.Attachment18;
import legend.game.combat.effects.AttachmentHost;
import legend.game.combat.effects.BillboardSpriteEffect0c;
import legend.game.combat.effects.ButtonPressHudMetrics06;
import legend.game.combat.effects.DeffTmdRenderer14;
import legend.game.combat.effects.Effect;
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
import legend.game.combat.effects.SpTextEffect40;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.effects.TextureAnimationAttachment1c;
import legend.game.combat.effects.WeaponTrailEffect3c;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.environment.BattleStage;
import legend.game.combat.environment.BattleStageDarkening1800;
import legend.game.combat.environment.BattleStruct14;
import legend.game.combat.environment.BttlLightStruct84;
import legend.game.combat.environment.BttlLightStruct84Sub38;
import legend.game.combat.environment.EncounterData38;
import legend.game.combat.environment.StageAmbiance4c;
import legend.game.combat.environment.StageData2c;
import legend.game.combat.types.AttackType;
import legend.game.combat.types.BattleAsset08;
import legend.game.combat.types.BattleObject;
import legend.game.combat.types.BattleStateEf4;
import legend.game.combat.types.CombatantAsset0c;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.CompressedAsset08;
import legend.game.combat.types.DragoonSpells09;
import legend.game.combat.types.EnemyDrop;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.ui.BattleHud;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.CombatItem02;
import legend.game.combat.ui.SpellAndItemMenuA4;
import legend.game.combat.ui.UiBox;
import legend.game.fmv.Fmv;
import legend.game.inventory.Equipment;
import legend.game.inventory.Item;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleEndedEvent;
import legend.game.modding.events.battle.BattleEntityTurnEvent;
import legend.game.modding.events.battle.BattleStartedEvent;
import legend.game.modding.events.battle.MonsterStatsEvent;
import legend.game.modding.events.battle.SpellStatsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptEnum;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CContainer;
import legend.game.types.CContainerSubfile2;
import legend.game.types.CharacterData2c;
import legend.game.types.GsF_LIGHT;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.SpellStats0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Math;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
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
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.renderButtonPressHudElement;
import static legend.game.Scus94491BpeSegment.renderButtonPressHudTexturedRect;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.setDepthResolution;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.startEncounterSounds;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020308;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021520;
import static legend.game.Scus94491BpeSegment_8002.adjustModelUvs;
import static legend.game.Scus94491BpeSegment_8002.animateModelTextures;
import static legend.game.Scus94491BpeSegment_8002.applyInterpolationFrame;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.scriptDeallocateAllTextboxes;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
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
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8004.previousEngineState_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeOut;
import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
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
import static legend.game.Scus94491BpeSegment_800b.postBattleActionIndex_800bc974;
import static legend.game.Scus94491BpeSegment_800b.postCombatMainCallbackIndex_800bc91c;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.unlockedUltimateAddition_800bc910;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Monsters.monsterNames_80112068;
import static legend.game.combat.Monsters.monsterStats_8010ba98;
import static legend.game.combat.SBtld.loadAdditions;
import static legend.game.combat.SBtld.loadEnemyDropsAndScript;
import static legend.game.combat.SBtld.loadStageAmbiance;
import static legend.game.combat.SBtld.loadStageDataAndControllerScripts;
import static legend.game.combat.SEffe.addGenericAttachment;
import static legend.game.combat.SEffe.loadDeffStageEffects;
import static legend.game.combat.SEffe.scriptGetPositionScalerAttachmentVelocity;
import static legend.game.combat.environment.BattleCamera.UPDATE_REFPOINT;
import static legend.game.combat.environment.BattleCamera.UPDATE_VIEWPOINT;

public final class Bttl {
  private Bttl() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker CAMERA = MarkerManager.getMarker("CAMERA");
  private static final Marker EFFECTS = MarkerManager.getMarker("EFFECTS");
  private static final Marker DEFF = MarkerManager.getMarker("DEFF");

  public static final Vector3f ZERO = new Vector3f();

  public static final BattleHud hud = new BattleHud();

  public static final UnsignedShortRef currentPostCombatActionFrame_800c6690 = MEMORY.ref(2, 0x800c6690L, UnsignedShortRef::new);

  public static final IntRef aliveBentCount_800c669c = MEMORY.ref(4, 0x800c669cL, IntRef::new);
  /** The number of {@link Scus94491BpeSegment_8005#combatants_8005e398}s */
  public static final IntRef combatantCount_800c66a0 = MEMORY.ref(4, 0x800c66a0L, IntRef::new);
  public static final IntRef currentStage_800c66a4 = MEMORY.ref(4, 0x800c66a4L, IntRef::new);

  public static final IntRef _800c66a8 = MEMORY.ref(4, 0x800c66a8L, IntRef::new);
  public static final IntRef currentCompressedAssetIndex_800c66ac = MEMORY.ref(4, 0x800c66acL, IntRef::new);
  public static final IntRef currentCameraPositionIndicesIndex_800c66b0 = MEMORY.ref(4, 0x800c66b0L, IntRef::new);

  public static final IntRef currentAssetIndex_800c66b4 = MEMORY.ref(4, 0x800c66b4L, IntRef::new);
  public static final BoolRef stageHasModel_800c66b8 = MEMORY.ref(1, 0x800c66b8L, BoolRef::new);
  /** Character scripts deallocated? */
  public static final BoolRef combatDisabled_800c66b9 = MEMORY.ref(1, 0x800c66b9L, BoolRef::new);

  public static ScriptState<? extends BattleEntity27c> forcedTurnBent_800c66bc;

  public static final IntRef usedMonsterTextureSlots_800c66c4 = MEMORY.ref(4, 0x800c66c4L, IntRef::new);
  public static ScriptState<? extends BattleEntity27c> currentTurnBent_800c66c8;
  public static final IntRef mcqBaseOffsetX_800c66cc = MEMORY.ref(4, 0x800c66ccL, IntRef::new);
  public static final IntRef allBentCount_800c66d0 = MEMORY.ref(4, 0x800c66d0L, IntRef::new);
  public static final BoolRef shouldRenderMcq_800c66d4 = MEMORY.ref(1, 0x800c66d4L, BoolRef::new);

  public static ScriptFile playerBattleScript_800c66fc;

  public static final IntRef _800c6700 = MEMORY.ref(4, 0x800c6700L, IntRef::new);
  public static final IntRef _800c6704 = MEMORY.ref(4, 0x800c6704L, IntRef::new);

  public static final IntRef _800c6710 = MEMORY.ref(4, 0x800c6710L, IntRef::new);

  public static StageData2c currentStageData_800c6718;
  public static final IntRef _800c6748 = MEMORY.ref(4, 0x800c6748L, IntRef::new);
  public static ScriptState<Void> scriptState_800c674c;

  public static final IntRef _800c6754 = MEMORY.ref(4, 0x800c6754L, IntRef::new);
  public static final IntRef aliveMonsterCount_800c6758 = MEMORY.ref(4, 0x800c6758L, IntRef::new);
  public static final IntRef currentDisplayableIconsBitset_800c675c = MEMORY.ref(4, 0x800c675cL, IntRef::new);
  public static final IntRef aliveCharCount_800c6760 = MEMORY.ref(4, 0x800c6760L, IntRef::new);
  public static final IntRef shouldRenderMcq_800c6764 = MEMORY.ref(4, 0x800c6764L, IntRef::new);
  public static final IntRef monsterCount_800c6768 = MEMORY.ref(4, 0x800c6768L, IntRef::new);
  public static final IntRef mcqStepX_800c676c = MEMORY.ref(4, 0x800c676cL, IntRef::new);
  public static final IntRef mcqStepY_800c6770 = MEMORY.ref(4, 0x800c6770L, IntRef::new);
  public static final IntRef mcqOffsetX_800c6774 = MEMORY.ref(4, 0x800c6774L, IntRef::new);
  public static final IntRef mcqOffsetY_800c6778 = MEMORY.ref(4, 0x800c6778L, IntRef::new);
  /** The number of player chars in combat (i.e. 1-3) */
  public static final IntRef charCount_800c677c = MEMORY.ref(4, 0x800c677cL, IntRef::new);
  /** This may be unused. Only referenced by the script engine, but seems like there may be no real uses */
  public static final IntRef currentCameraIndex_800c6780 = MEMORY.ref(4, 0x800c6780L, IntRef::new);

  public static final IntRef screenOffsetX_800c67bc = MEMORY.ref(4, 0x800c67bcL, IntRef::new);
  public static final IntRef screenOffsetY_800c67c0 = MEMORY.ref(4, 0x800c67c0L, IntRef::new);

  public static final IntRef _800c67c8 = MEMORY.ref(4, 0x800c67c8L, IntRef::new);
  public static final IntRef _800c67cc = MEMORY.ref(4, 0x800c67ccL, IntRef::new);
  public static final IntRef _800c67d0 = MEMORY.ref(4, 0x800c67d0L, IntRef::new);

  public static final BattleCamera camera_800c67f0 = new BattleCamera();

  public static ScriptState<? extends BattleEntity27c> scriptState_800c6914;
  public static final IntRef _800c6918 = MEMORY.ref(4, 0x800c6918L, IntRef::new);

  public static final IntRef lightTicks_800c6928 = MEMORY.ref(4, 0x800c6928L, IntRef::new);
  public static BttlLightStruct84[] lights_800c692c;
  public static BattleLightStruct64 _800c6930;

  public static BattleStruct24_2 _800c6938;
  public static DeffManager7cc deffManager_800c693c;

  public static SpriteMetrics08[] spriteMetrics_800c6948;

  public static BattleStageDarkening1800 stageDarkening_800c6958;
  public static int stageDarkeningClutWidth_800c695c;

  public static final ArrayRef<DragoonSpells09> dragoonSpells_800c6960 = MEMORY.ref(1, 0x800c6960L, ArrayRef.of(DragoonSpells09.class, 3, 9, DragoonSpells09::new));

  public static final List<CombatItem02> combatItems_800c6988 = new ArrayList<>();
  public static final BoolRef itemTargetAll_800c69c8 = MEMORY.ref(4, 0x800c69c8L, BoolRef::new);

  public static final LodString[] currentEnemyNames_800c69d0 = new LodString[9];

  public static SpellAndItemMenuA4 spellAndItemMenu_800c6b60;
  public static Element dragoonSpaceElement_800c6b64;
  public static final IntRef itemTargetType_800c6b68 = MEMORY.ref(4, 0x800c6b68L, IntRef::new);

  public static final ArrayRef<IntRef> monsterBents_800c6b78 = MEMORY.ref(4, 0x800c6b78L, ArrayRef.of(IntRef.class, 9, 4, IntRef::new));
  public static final IntRef monsterCount_800c6b9c = MEMORY.ref(4, 0x800c6b9cL, IntRef::new);
  public static final ByteRef countCameraPositionIndicesIndices_800c6ba0 = MEMORY.ref(1, 0x800c6ba0L, ByteRef::new);
  public static final ByteRef currentCameraPositionIndicesIndicesIndex_800c6ba1 = MEMORY.ref(1, 0x800c6ba1L, ByteRef::new);

  /** Uhh, contains the monsters that Melbu summons during his fight...? */
  public static final LodString[] melbuMonsterNames_800c6ba8 = new LodString[3];

  public static final ArrayRef<UnsignedByteRef> cameraPositionIndicesIndices_800c6c30 = MEMORY.ref(4, 0x800c6c30L, ArrayRef.of(UnsignedByteRef.class, 4, 1, UnsignedByteRef::new));

  public static BattleMenuStruct58 battleMenu_800c6c34;

  public static final List<Item> usedRepeatItems_800c6c3c = new ArrayList<>();

  public static final IntRef countCombatUiFilesLoaded_800c6cf4 = MEMORY.ref(4, 0x800c6cf4L, IntRef::new);

  public static final GsF_LIGHT light_800c6ddc = new GsF_LIGHT(1.0f, 1.0f, 1.0f);

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
  public static final RegistryDelegate<Element>[] characterElements_800c706c = new RegistryDelegate[] {CoreMod.FIRE_ELEMENT, CoreMod.WIND_ELEMENT, CoreMod.LIGHT_ELEMENT, CoreMod.DARK_ELEMENT, CoreMod.THUNDER_ELEMENT, CoreMod.WIND_ELEMENT, CoreMod.WATER_ELEMENT, CoreMod.EARTH_ELEMENT, CoreMod.LIGHT_ELEMENT};

  public static final Vector2i[] battleUiElementClutVramXy_800c7114 = {
    new Vector2i(0x2c0, 0x1f0),
    new Vector2i(0x380, 0x130),
  };
  public static final int[] targetAllItemIds_800c7124 = {193, 207, 208, 209, 210, 214, 216, 220, 241, 242, 243, 244, 245, 246, 247, 248, 250};

  /** Different sets of bents for different target types (chars, monsters, all) */
  public static ScriptState<BattleEntity27c>[][] targetBents_800c71f0;

  public static final int[] protectedItems_800c72cc = {224, 227, 228, 230, 232, 235, 236, 237, 238, 250};

  public static final SpellStats0c[] spellStats_800fa0b8 = new SpellStats0c[128];
  public static final int[] postCombatActionTotalFrames_800fa6b8 = {0, 82, 65, 15, 10, 15};

  public static final int[] postBattleActions_800fa6c4 = {-1, 195, 211, -1, 211, -1};

  public static final int[] postCombatActionFrames_800fa6d0 = {0, 30, 45, 30, 45, 30};

  public static final IntRef mcqColour_800fa6dc = MEMORY.ref(4, 0x800fa6dcL, IntRef::new);
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

  public static final String[] additionNames_800fa8d4 = {"Double Slash", "Volcano", "Burning Rush", "Crush Dance", "Madness Hero", "Moon Strike", "Blazing Dynamo", "", "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance", "Flower Storm", "", "Whip Smack", "More & More", "Hard Blade", "Demon's Dance", "", "Pursuit", "Inferno", "Bone Crush", "", "Double Smack", "Hammer Spin", "Cool Boogie", "Cat's Cradle", "Perky Step", "", "Double Punch", "Ferry of Styx", "Summon 4 Gods", " 5-Ring Shattering", "Hex Hammer", "Omni-Sweep", "Harpoon", "Spinning Cane", "Rod Typhoon", "Gust of Wind Dance", "Flower Storm"};

  /** Next 4 globals are related to SpTextEffect40 */
  public static final ShortRef _800faa90 = MEMORY.ref(2, 0x800faa90L, ShortRef::new);
  public static final ShortRef _800faa92 = MEMORY.ref(2, 0x800faa92L, ShortRef::new);
  public static final ByteRef _800faa94 = MEMORY.ref(1, 0x800faa94L, ByteRef::new);

  /** Next global is related to AdditionNameTextEffect1c */
  public static final ByteRef _800faa9d = MEMORY.ref(1, 0x800faa9dL, ByteRef::new);

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

  public static final IntRef deffLoadingStage_800fafe8 = MEMORY.ref(4, 0x800fafe8L, IntRef::new);
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
  public static final int[] modelVramSlots_800fb06c = {0, 0, 0, 0, 0, 0, 0, 0, 14, 15, 16, 17, 10, 11, 12, 13, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0};

  @Method(0x800c7304L)
  public static void cacheLivingBents() {
    int i;
    int count;
    //LAB_800c7330
    for(i = 0, count = 0; i < allBentCount_800c66d0.get(); i++) {
      final ScriptState<? extends BattleEntity27c> bentState = battleState_8006e398.allBents_e0c[i];
      if((bentState.storage_44[7] & 0x40) == 0) {
        battleState_8006e398.aliveBents_e78[count] = bentState;
        count++;
      }

      //LAB_800c736c
    }

    //LAB_800c737c
    aliveBentCount_800c669c.set(count);

    //LAB_800c73b0
    for(i = 0, count = 0; i < charCount_800c677c.get(); i++) {
      final ScriptState<PlayerBattleEntity> playerState = battleState_8006e398.charBents_e40[i];
      if((playerState.storage_44[7] & 0x40) == 0) {
        battleState_8006e398.aliveCharBents_eac[count] = playerState;
        count++;
      }

      //LAB_800c73ec
    }

    //LAB_800c73fc
    aliveCharCount_800c6760.set(count);

    //LAB_800c7430
    for(i = 0, count = 0; i < monsterCount_800c6768.get(); i++) {
      final ScriptState<MonsterBattleEntity> monsterState = battleState_8006e398.monsterBents_e50[i];
      if((monsterState.storage_44[7] & 0x40) == 0) {
        battleState_8006e398.aliveMonsterBents_ebc[count] = monsterState;
        count++;
      }

      //LAB_800c746c
    }

    //LAB_800c747c
    aliveMonsterCount_800c6758.set(count);
  }

  @Method(0x800c7488L)
  public static int getHitProperty(final int charSlot, final int hitNum, final int hitPropertyIndex) {
    if((battleState_8006e398.charBents_e40[charSlot].storage_44[7] & 0x2) != 0) { // Is dragoon
      return battlePreloadedEntities_1f8003f4.additionHits_38[charSlot + 3].hits_00[hitNum].get(hitPropertyIndex);
    }

    //LAB_800c74fc
    return battlePreloadedEntities_1f8003f4.additionHits_38[charSlot].hits_00[hitNum].get(hitPropertyIndex);
  }

  @Method(0x800c7524L)
  public static void initBattle() {
    FUN_800c8624();

    gameState_800babc8._b4++;
    Arrays.fill(unlockedUltimateAddition_800bc910, false);
    goldGainedFromCombat_800bc920 = 0;

    spGained_800bc950[0] = 0;
    spGained_800bc950[1] = 0;
    spGained_800bc950[2] = 0;

    totalXpFromCombat_800bc95c = 0;
    battleFlags_800bc960 = 0;
    postBattleActionIndex_800bc974 = 0;
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
    allocateStageDarkeningStorage();
    loadEncounterSoundsAndMusic();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7648L)
  public static void loadStageAndControllerScripts() {
    loadStage(battleStage_800bb0f4);
    loadStageDataAndControllerScripts();
    pregameLoadingStage_800bb10c.incr();
  }

  public static void uploadBattleStageToGpu() {
    if(stageHasModel_800c66b8.get()) {
      final BattleStage stage = battlePreloadedEntities_1f8003f4.stage_963c;

      for(int i = 0; i < stage.dobj2s_00.length; i++) {
        if(stage.tmd_5d0.objTable[i] != null) {
          stage.dobj2s_00[i].obj = TmdObjLoader.fromObjTable("BattleStage (obj " + i + ')', stage.tmd_5d0.objTable[i]);
        }
      }

      pregameLoadingStage_800bb10c.incr();
    }
  }

  @Method(0x800c76a0L)
  public static void initializeViewportAndCamera() {
    if((battleFlags_800bc960 & 0x3) == 0x3) {
      resizeDisplay(320, 240);
      setDepthResolution(12);
      vsyncMode_8007a3b8 = 3;
      battleFlags_800bc960 |= 0x40;
      setProjectionPlaneDistance(320);
      camera_800c67f0.resetCameraMovement();
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7718
  }

  @Method(0x800c772cL)
  public static void battleInitiateAndPreload_800c772c() {
    FUN_800c8e48();

    battleLoaded_800bc94c = true;

    startFadeEffect(4, 30);

    battleFlags_800bc960 |= 0x20;
    battleState_8006e398.stageProgression_eec = 0;

    clearCombatants();
    clearCurrentDisplayableItems();

    allBentCount_800c66d0.set(0);
    monsterCount_800c6768.set(0);
    charCount_800c677c.set(0);

    loadAdditions();

    //LAB_800c7830
    for(int i = 0; i < 12; i++) {
      battleState_8006e398.allBents_e0c[i] = null;
    }

    initBattleMenu();
    allocateDeffManager();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c788cL)
  public static void allocateEnemyBattleEntities() {
    final BattlePreloadedEntities_18cb0 fp = battlePreloadedEntities_1f8003f4;

    //LAB_801095a0
    for(int i = 0; i < 3; i++) {
      final int enemyIndex = fp.encounterData_00.enemyIndices_00[i] & 0x1ff;
      if(enemyIndex == 0x1ff) {
        break;
      }

      loadEnemyDropsAndScript((addCombatant(enemyIndex, -1) << 16) + enemyIndex);
    }

    //LAB_801095ec
    //LAB_801095fc
    for(int i = 0; i < 6; i++) {
      final EncounterData38.EnemyInfo08 s5 = fp.encounterData_00.enemyInfo_08[i];
      final int charIndex = s5.index_00 & 0x1ff;
      if(charIndex == 0x1ff) {
        break;
      }

      final int combatantIndex = getCombatantIndex(charIndex);
      final String name = "Enemy combatant index " + combatantIndex;
      final MonsterBattleEntity bent = new MonsterBattleEntity(name);
      final ScriptState<MonsterBattleEntity> state = SCRIPTS.allocateScriptState(name, bent);
      state.setTicker(bent::bentLoadingTicker);
      state.setDestructor(bent::bentDestructor);
      battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = state;
      battleState_8006e398.monsterBents_e50[monsterCount_800c6768.get()] = state;
      bent.charId_272 = charIndex;
      bent.bentSlot_274 = allBentCount_800c66d0.get();
      bent.charSlot_276 = monsterCount_800c6768.get();
      bent.combatant_144 = getCombatant(combatantIndex);
      bent.combatantIndex_26c = combatantIndex;
      bent.model_148.coord2_14.coord.transfer.set(s5.pos_02);
      bent.model_148.coord2_14.transforms.rotate.set(0.0f, MathHelper.TWO_PI * 0.75f, 0.0f);
      state.storage_44[7] |= 0x4;
      allBentCount_800c66d0.incr();
      monsterCount_800c6768.incr();
    }

    //LAB_8010975c
    battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = null;
    battleState_8006e398.monsterBents_e50[monsterCount_800c6768.get()] = null;

    //LAB_801097ac
    for(int i = 0; i < monsterCount_800c6768.get(); i++) {
      loadMonster(battleState_8006e398.monsterBents_e50[i]);
    }

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c78d4L)
  public static void allocatePlayerBattleEntities() {
    //LAB_800fbdb8
    for(charCount_800c677c.set(0); charCount_800c677c.get() < 3; charCount_800c677c.incr()) {
      if(gameState_800babc8.charIds_88[charCount_800c677c.get()] < 0) {
        break;
      }
    }

    //LAB_800fbde8
    final int[] combatantIndices = new int[charCount_800c677c.get()];

    //LAB_800fbe18
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      combatantIndices[charSlot] = addCombatant(0x200 + gameState_800babc8.charIds_88[charSlot] * 2, charSlot);
    }

    //LAB_800fbe4c
    //LAB_800fbe70
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];
      final String name = "Char ID " + charIndex + " (bent + " + (charSlot + 6) + ')';
      final PlayerBattleEntity bent = new PlayerBattleEntity(name, charSlot + 6);
      final ScriptState<PlayerBattleEntity> state = SCRIPTS.allocateScriptState(charSlot + 6, name, 0, bent);
      state.setTicker(bent::bentLoadingTicker);
      state.setDestructor(bent::bentDestructor);
      battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = state;
      battleState_8006e398.charBents_e40[charSlot] = state;
      bent.element = characterElements_800c706c[charIndex].get();
      bent.combatant_144 = getCombatant((short)combatantIndices[charSlot]);
      bent.charId_272 = charIndex;
      bent.bentSlot_274 = allBentCount_800c66d0.get();
      bent.charSlot_276 = charSlot;
      bent.combatantIndex_26c = combatantIndices[charSlot];
      bent.model_148.coord2_14.coord.transfer.x = charCount_800c677c.get() > 2 && charSlot == 0 ? 0x900 : 0xa00;
      bent.model_148.coord2_14.coord.transfer.y = 0.0f;
      // Alternates placing characters to the right and left of the main character (offsets by -0x400 for even character counts)
      bent.model_148.coord2_14.coord.transfer.z = 0x800 * ((charSlot + 1) / 2) * (charSlot % 2 * 2 - 1) + (charCount_800c677c.get() % 2 - 1) * 0x400;
      bent.model_148.coord2_14.transforms.rotate.zero();
      allBentCount_800c66d0.incr();
    }

    //LAB_800fbf6c
    battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = null;
    battleState_8006e398.charBents_e40[charCount_800c677c.get()] = null;

    FUN_800ef28c();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c791cL)
  public static void loadEncounterAssets() {
    loadEnemyTextures(2625 + encounterId_800bb0f8);

    //LAB_800fc030
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 combatant = getCombatant(i);
      if(combatant.charSlot_19c < 0) { // I think this means it's not a player
        loadCombatantTmdAndAnims(combatant);
      }

      //LAB_800fc050
    }

    //LAB_800fc064
    //LAB_800fc09c
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      battleState_8006e398.charBents_e40[i].innerStruct_00.combatant_144.flags_19e |= 0x2a;
    }

    //LAB_800fc104
    loadPartyTims();
    loadPartyTmdAndAnims();
    battleFlags_800bc960 |= 0x400;

    pregameLoadingStage_800bb10c.incr();
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc210L)
  public static void loadCharTmdAndAnims(final List<FileData> files, final int charSlot) {
    //LAB_800fc260
    final BattleEntity27c data = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;

    //LAB_800fc298
    combatantTmdAndAnimLoadedCallback(files, data.combatant_144, false);

    //LAB_800fc34c
    battleFlags_800bc960 |= 0x4;
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc3c0L)
  public static void loadEnemyTextures(final int fileIndex) {
    // Example file: 2856
    loadDrgnDir(0, fileIndex, Bttl::enemyTexturesLoadedCallback);
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc404L)
  public static void enemyTexturesLoadedCallback(final List<FileData> files) {
    final BattlePreloadedEntities_18cb0 s2 = battlePreloadedEntities_1f8003f4;

    //LAB_800fc434
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 combatant = getCombatant(i);

      if(combatant.charSlot_19c < 0) {
        final int enemyIndex = combatant.charIndex_1a2 & 0x1ff;

        //LAB_800fc464
        for(int enemySlot = 0; enemySlot < 3; enemySlot++) {
          if((s2.encounterData_00.enemyIndices_00[enemySlot] & 0x1ff) == enemyIndex && files.get(enemySlot).hasVirtualSize()) {
            loadCombatantTim(combatant, files.get(enemySlot));
            break;
          }
        }
      }
    }
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc504L)
  public static void loadPartyTims() {
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadFile("characters/%s/textures/combat".formatted(name), files -> loadCharacterTim(files, finalCharSlot));
    }
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc548L)
  public static void loadCharacterTim(final FileData file, final int charSlot) {
    final BattleEntity27c bent = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;
    loadCombatantTim(bent.combatant_144, file);
  }

  /** Pulled from S_ITEM */
  @Method(0x800fc654L)
  public static void loadPartyTmdAndAnims() {
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final int charId = gameState_800babc8.charIds_88[charSlot];
      final String name = getCharacterName(charId).toLowerCase();
      final int finalCharSlot = charSlot;
      loadDir("characters/%s/models/combat".formatted(name), files -> loadCharTmdAndAnims(files, finalCharSlot));
    }
  }

  @Method(0x800c7964L)
  public static void loadHudAndAttackAnimations() {
    battleFlags_800bc960 |= 0xc;

    loadBattleHudTextures();
    loadBattleHudDeff();

    //LAB_800c79a8
    for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
      loadAttackAnimations(combatants_8005e398[combatantIndex]);
    }

    //LAB_800c79c8
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c79f0L)
  public static void FUN_800c79f0() {
    currentTurnBent_800c66c8 = battleState_8006e398.allBents_e0c[0];
    hud.FUN_800f417c();

    EVENTS.postEvent(new BattleStartedEvent());

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7a30L)
  public static void loadSEffe() {
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7a80L)
  public static void calculateInitialTurnValues() {
    if(_800c66a8.get() != 0) {
      battleFlags_800bc960 |= 0x10;
      battleState_8006e398.calculateInitialTurnValues();
      pregameLoadingStage_800bb10c.incr();
    }
  }

  @Method(0x800c7bb8L)
  public static void battleTick() {
    hud.draw();

    if(postBattleActionIndex_800bc974 != 0) {
      pregameLoadingStage_800bb10c.incr();
      return;
    }

    if(Unpacker.getLoadingFileCount() == 0 && allBentCount_800c66d0.get() > 0 && !combatDisabled_800c66b9.get() && FUN_800c7da8()) {
      vsyncMode_8007a3b8 = 3;
      mcqColour_800fa6dc.set(0x80);
      currentTurnBent_800c66c8.storage_44[7] &= 0xffff_efff;

      if(aliveCharCount_800c6760.get() > 0) {
        //LAB_800c7c98
        forcedTurnBent_800c66bc = battleState_8006e398.getForcedTurnBent();

        if(forcedTurnBent_800c66bc != null) { // A bent has a forced turn
          forcedTurnBent_800c66bc.storage_44[7] = forcedTurnBent_800c66bc.storage_44[7] & 0xffff_ffdf | 0x1008;
          currentTurnBent_800c66c8 = forcedTurnBent_800c66bc;
          EVENTS.postEvent(new BattleEntityTurnEvent<>(forcedTurnBent_800c66bc));
        } else { // Take regular turns
          //LAB_800c7ce8
          if(aliveMonsterCount_800c6758.get() > 0) { // Monsters alive, calculate next bent turn
            //LAB_800c7d3c
            currentTurnBent_800c66c8 = battleState_8006e398.getCurrentTurnBent();
            currentTurnBent_800c66c8.storage_44[7] |= 0x1008;
            EVENTS.postEvent(new BattleEntityTurnEvent<>(currentTurnBent_800c66c8));

            //LAB_800c7d74
          } else { // Monsters dead
            endBattle();
          }
        }
      } else { // Game over
        loadMusicPackage(19, 0);
        postBattleActionIndex_800bc974 = 2;
      }
    }

    //LAB_800c7d78
    if(postBattleActionIndex_800bc974 != 0) {
      //LAB_800c7d88
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7d98
  }

  public static void endBattle() {
    FUN_80020308();

    if(encounterId_800bb0f8 != 443) { // Standard victory
      postBattleActionIndex_800bc974 = 1;
      startEncounterSounds();
    } else { // Melbu Victory
      //LAB_800c7d30
      postBattleActionIndex_800bc974 = 4;
    }
  }

  @Method(0x800c7da8L)
  public static boolean FUN_800c7da8() {
    //LAB_800c7dd8
    for(int i = 0; i < allBentCount_800c66d0.get(); i++) {
      if((battleState_8006e398.allBents_e0c[i].storage_44[7] & 0x408) != 0) {
        return false;
      }

      //LAB_800c7e10
    }

    //LAB_800c7e1c
    return true;
  }

  @Method(0x800c8068L)
  public static void performPostBattleAction() {
    EVENTS.postEvent(new BattleEndedEvent());

    final int postBattleActionIndex = postBattleActionIndex_800bc974;

    if(currentPostCombatActionFrame_800c6690.get() == 0) {
      final int postBattleAction = postBattleActions_800fa6c4[postBattleActionIndex];

      if(postBattleAction >= 0) {
        _800c6748.set(postBattleAction);
        scriptState_800c6914 = currentTurnBent_800c66c8;
      }

      //LAB_800c80c8
      final int aliveCharBents = aliveCharCount_800c6760.get();
      livingCharCount_800bc97c = aliveCharBents;

      //LAB_800c8104
      for(int i = 0; i < aliveCharBents; i++) {
        livingCharIds_800bc968[i] = battleState_8006e398.aliveCharBents_eac[i].innerStruct_00.charId_272;
      }

      //LAB_800c8144
      if(postBattleActionIndex == 1) {
        //LAB_800c8180
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          battleState_8006e398.charBents_e40[i].storage_44[7] |= 0x8;
        }
      }
    }

    //LAB_800c81bc
    //LAB_800c81c0
    currentPostCombatActionFrame_800c6690.incr();

    if(currentPostCombatActionFrame_800c6690.get() >= postCombatActionTotalFrames_800fa6b8[postBattleActionIndex] || (press_800bee94 & 0xff) != 0 && currentPostCombatActionFrame_800c6690.get() >= 25) {
      //LAB_800c8214
      deallocateLightingControllerAndDeffManager();

      if(fullScreenEffect_800bb140.currentColour_28 == 0) {
        startFadeEffect(1, postCombatActionFrames_800fa6d0[postBattleActionIndex]);
      }

      //LAB_800c8274
      if(postBattleActionIndex == 2) {
        sssqFadeOut((short)(postCombatActionFrames_800fa6d0[2] - 2));
      }

      //LAB_800c8290
      currentPostCombatActionFrame_800c6690.set(0);
      pregameLoadingStage_800bb10c.incr();
    }
    //LAB_800c82a8
  }

  @Method(0x800c82b8L)
  public static void deallocateCombat() {
    if(fullScreenEffect_800bb140.currentColour_28 == 0xff) {
      updateGameStateAndDeallocateMenu();
      setStageHasNoModel();

      for(int i = 0; i < battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00.length; i++) {
        if(battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[i].obj != null) {
          battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[i].obj.delete();
          battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[i].obj = null;
        }
      }

      if(battlePreloadedEntities_1f8003f4.skyboxObj != null) {
        battlePreloadedEntities_1f8003f4.skyboxObj.delete();
        battlePreloadedEntities_1f8003f4.skyboxObj = null;
      }

      playerBattleScript_800c66fc = null;

      //LAB_800c8314
      scriptDeallocateAllTextboxes(null);
      scriptState_800c674c.deallocateWithChildren();

      //LAB_800c8368
      //LAB_800c8394
      stopAndResetSoundsAndSequences();

      //LAB_800c83b8
      while(allBentCount_800c66d0.get() > 0) {
        battleState_8006e398.allBents_e0c[0].deallocateWithChildren();
      }

      //LAB_800c83d8
      //LAB_800c83f4
      for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
        final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

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
      deallocateStageDarkeningStorage();
      FUN_800c8748();

      EngineStateEnum postCombatMainCallbackIndex = previousEngineState_8004dd28;
      if(postCombatMainCallbackIndex == EngineStateEnum.FMV_09) {
        postCombatMainCallbackIndex = EngineStateEnum.SUBMAP_05;
      }

      //LAB_800c84b4
      switch(postBattleActionIndex_800bc974) {
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

      final int postCombatSubmapStage = currentStageData_800c6718.postCombatSubmapStage_0c;
      if(postCombatSubmapStage != 0xff) {
        submapScene_80052c34 = postCombatSubmapStage;
      }

      //LAB_800c8578
      final int postCombatSubmapCut = currentStageData_800c6718.postCombatSubmapCut_28;
      if(postCombatSubmapCut != 0xffff) {
        submapCut_80052c30 = postCombatSubmapCut;
      }

      //LAB_800c8590
      setDepthResolution(14);
      battleLoaded_800bc94c = false;

      switch(postBattleActionIndex_800bc974) {
        case 1, 3 -> whichMenu_800bdc38 = WhichMenu.INIT_POST_COMBAT_REPORT_26;
        case 2, 4, 5 -> whichMenu_800bdc38 = WhichMenu.NONE_0;
      }

      //LAB_800c85f0
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c8604
  }

  @Method(0x800c8624L)
  public static void FUN_800c8624() {
    battlePreloadedEntities_1f8003f4 = new BattlePreloadedEntities_18cb0();
    battleState_8006e398 = new BattleStateEf4();
    targetBents_800c71f0 = new ScriptState[][] {battleState_8006e398.charBents_e40, battleState_8006e398.aliveMonsterBents_ebc, battleState_8006e398.aliveBents_e78};
  }

  @Method(0x800c8748L)
  public static void FUN_800c8748() {
    battlePreloadedEntities_1f8003f4 = null;
    battleState_8006e398 = null;
    targetBents_800c71f0 = null;
  }

  @Method(0x800c8774L)
  public static void loadStageTmdAndAnim(final String modelName, final List<FileData> files) {
    setStageHasNoModel();

    if(files.get(0).size() > 0 && files.get(1).size() > 0 && files.get(2).size() > 0) {
      _800c6754.set(1);
      stageHasModel_800c66b8.set(true);

      final BattleStage stage = battlePreloadedEntities_1f8003f4.stage_963c;
      loadStageTmd(stage, new CContainer(modelName, files.get(0), 10), new TmdAnimationFile(files.get(1)));
      stage.coord2_558.coord.transfer.set(0, 0, 0);
      stage.param_5a8.rotate.set(0.0f, MathHelper.TWO_PI / 4.0f, 0.0f);
    }

    //LAB_800c8818
  }

  @Method(0x800c882cL)
  public static void renderSkybox() {
    if(shouldRenderMcq_800c6764.get() == 0 || !shouldRenderMcq_800c66d4.get() || (battleFlags_800bc960 & 0x80) == 0) {
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

      mcqOffsetX_800c6774.add(mcqStepX_800c676c.get());
      mcqOffsetY_800c6778.add(mcqStepY_800c6770.get());
      final int x0 = (mcqBaseOffsetX_800c66cc.get() * MathHelper.radToPsxDeg(camera_800c67f0.calculateXAngleFromRefpointToViewpoint()) / 0x1000 + mcqOffsetX_800c6774.get()) % mcq.screenWidth_14;
      final int x1 = x0 - mcq.screenWidth_14;
      final int x2 = x0 + mcq.screenWidth_14;
      int y = mcqOffsetY_800c6778.get() - MathHelper.radToPsxDeg(MathHelper.floorMod(camera_800c67f0.calculateYAngleFromRefpointToViewpoint() + MathHelper.PI, MathHelper.TWO_PI)) + 1888;

      battlePreloadedEntities_1f8003f4.skyboxTransforms.transfer.set(x0, y, orderingTableSize_1f8003c8 - 8.0f);
      RENDERER.queueOrthoUnderlayModel(battlePreloadedEntities_1f8003f4.skyboxObj, battlePreloadedEntities_1f8003f4.skyboxTransforms)
        .monochrome(mcqColour_800fa6dc.get() / 128.0f);

      battlePreloadedEntities_1f8003f4.skyboxTransforms.transfer.set(x1, y, orderingTableSize_1f8003c8 - 8.0f);
      RENDERER.queueOrthoUnderlayModel(battlePreloadedEntities_1f8003f4.skyboxObj, battlePreloadedEntities_1f8003f4.skyboxTransforms)
        .monochrome(mcqColour_800fa6dc.get() / 128.0f);

      if(x2 <= centreScreenX_1f8003dc * 2) {
        battlePreloadedEntities_1f8003f4.skyboxTransforms.transfer.set(x2, y, orderingTableSize_1f8003c8 - 8.0f);
        RENDERER.queueOrthoUnderlayModel(battlePreloadedEntities_1f8003f4.skyboxObj, battlePreloadedEntities_1f8003f4.skyboxTransforms)
          .monochrome(mcqColour_800fa6dc.get() / 128.0f);
      }

      //LAB_800c89d4
      if(mcq.magic_00 != McqHeader.MAGIC_1) {
        //LAB_800c89f8
        y += mcq.screenOffsetY_2a;
      }

      //LAB_800c8a04
      final int colour = mcqColour_800fa6dc.get();
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
  public static void loadStage(final int stage) {
    if(battlePreloadedEntities_1f8003f4.skyboxObj != null) {
      battlePreloadedEntities_1f8003f4.skyboxObj.delete();
      battlePreloadedEntities_1f8003f4.skyboxObj = null;
    }

    loadDrgnDir(0, 2497 + stage, files -> {
      if(files.get(0).hasVirtualSize()) {
        loadStageMcq(new McqHeader(files.get(0)));
      }

      if(files.get(1).size() != 0) {
        loadStageTim(files.get(1));
      }
    });

    loadDrgnDir(0, (2497 + stage) + "/0", files -> loadStageTmdAndAnim("DRGN0/" + (2497 + stage) + "/0", files));

    currentStage_800c66a4.set(stage);
  }

  @Method(0x800c8c84L)
  public static void loadStageTim(final FileData data) {
    final Tim tim = new Tim(data);

    GPU.uploadData15(tim.getImageRect(), tim.getImageData());

    if(tim.hasClut()) {
      GPU.uploadData15(tim.getClutRect(), tim.getClutData());
    }

    //LAB_800c8ccc
    backupStageClut(data);
  }

  @Method(0x800c8ce4L)
  public static void setStageHasNoModel() {
    stageHasModel_800c66b8.set(false);
  }

  @Method(0x800c8cf0L)
  public static void rotateAndRenderBattleStage() {
    if(stageHasModel_800c66b8.get() && _800c6754.get() != 0 && (battleFlags_800bc960 & 0x20) != 0) {
      rotateBattleStage(battlePreloadedEntities_1f8003f4.stage_963c);
      renderBattleStage(battlePreloadedEntities_1f8003f4.stage_963c);
    }

    //LAB_800c8d50
  }

  @Method(0x800c8d64L)
  public static void loadStageMcq(final McqHeader mcq) {
    final int x;
    if((battleFlags_800bc960 & 0x80) != 0) {
      x = 320;
      shouldRenderMcq_800c6764.set(1);
    } else {
      //LAB_800c8d98
      x = 512;
    }

    //LAB_800c8d9c
    loadMcq(mcq, x, 0);

    //LAB_800c8dc0
    battlePreloadedEntities_1f8003f4.stageMcq_9cb0 = mcq;

    shouldRenderMcq_800c66d4.set(true);
    mcqBaseOffsetX_800c66cc.set((0x400 / mcq.screenWidth_14 + 1) * mcq.screenWidth_14);
  }

  @Method(0x800c8e48L)
  public static void FUN_800c8e48() {
    if(shouldRenderMcq_800c66d4.get() && (battleFlags_800bc960 & 0x80) == 0) {
      GPU.copyVramToVram(512, 0, 320, 0, battlePreloadedEntities_1f8003f4.stageMcq_9cb0.vramWidth_08, 256);
      shouldRenderMcq_800c6764.set(1);
      battleFlags_800bc960 |= 0x80;
    }
    //LAB_800c8ec8
  }

  @Method(0x800c8ed8L)
  public static void setDontRenderStageBackground() {
    shouldRenderMcq_800c66d4.set(false);
  }

  @Method(0x800c8ee4L)
  public static void clearCombatants() {
    //LAB_800c8ef4
    //NOTE: zeroes 0x50 bytes after this array of structs ends
    Arrays.fill(combatants_8005e398, null);
  }

  @Method(0x800c8f24L)
  public static CombatantStruct1a8 getCombatant(final int index) {
    return combatants_8005e398[index];
  }

  @Method(0x800c8f50L)
  public static int addCombatant(final int a0, final int charSlot) {
    //LAB_800c8f6c
    for(int combatantIndex = 0; combatantIndex < 10; combatantIndex++) {
      if(combatants_8005e398[combatantIndex] == null) {
        final CombatantStruct1a8 combatant = new CombatantStruct1a8();
        combatants_8005e398[combatantIndex] = combatant;

        if(charSlot < 0) {
          combatant.flags_19e = 1;
        } else {
          //LAB_800c8f90
          combatant.flags_19e = 5;
        }

        //LAB_800c8f94
        combatant.charSlot_19c = charSlot;
        combatant.vramSlot_1a0 = 0;
        combatant.charIndex_1a2 = a0;
        combatant._1a4 = -1;
        combatant._1a6 = -1;
        combatantCount_800c66a0.add(1);
        return combatantIndex;
      }
    }

    return -1;
  }

  @Method(0x800c8fd4L)
  public static void removeCombatant(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.vramSlot_1a0 != 0) {
      unsetMonsterTextureSlotUsed(combatant.vramSlot_1a0);
    }

    //LAB_800c9020
    //LAB_800c902c
    combatants_8005e398[combatantIndex] = null;

    combatantCount_800c66a0.sub(1);
  }

  @Method(0x800c9060L)
  public static int getCombatantIndex(final int charIndex) {
    //LAB_800c906c
    for(int i = 0; i < 10; i++) {
      final CombatantStruct1a8 combatant = combatants_8005e398[i];

      if(combatant != null && combatant.charIndex_1a2 == charIndex) {
        //LAB_800c90a8
        return i;
      }

      //LAB_800c9090
    }

    return -1;
  }

  @Method(0x800c9170L)
  public static void deallocateCombatant(final CombatantStruct1a8 combatant) {
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
        FUN_800c9c7c(combatant, i);
      }

      //LAB_800c9254
    }

    combatant.flags_19e &= 0xffe7;
  }

  @Method(0x800c9290L)
  public static void loadCombatantTmdAndAnims(final CombatantStruct1a8 combatant) {
    if(combatant.charIndex_1a2 >= 0) {
      if((combatant.flags_19e & 0x8) == 0) {
        if(combatant.mrg_00 == null) {
          combatant.flags_19e |= 0x28;

          if((combatant.flags_19e & 0x4) == 0) {
            // Enemy TMDs
            final int fileIndex = 3137 + combatant.charIndex_1a2;
            loadDrgnDir(0, fileIndex, files -> Bttl.combatantTmdAndAnimLoadedCallback(files, combatant, true));
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
              loadDir("characters/%s/models/dragoon".formatted(charName), files -> Bttl.combatantTmdAndAnimLoadedCallback(files, combatant, false));
            } else {
              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/combat".formatted(charName), files -> Bttl.combatantTmdAndAnimLoadedCallback(files, combatant, false));
            }
          }
        }
      }
    }

    //LAB_800c940c
  }

  @Method(0x800c941cL)
  public static void combatantTmdAndAnimLoadedCallback(final List<FileData> files, final CombatantStruct1a8 combatant, final boolean isMonster) {
    combatant.flags_19e &= 0xffdf;

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
        FUN_800c9a80(files.get(animIndex), 1, 0, combatant, animIndex);
      }

      //LAB_800c94cc
    }
  }

  @Method(0x800c952cL)
  public static void FUN_800c952c(final Model124 model, final CombatantStruct1a8 combatant) {
    final CContainer tmd;
    if(combatant._1a4 >= 0) {
      tmd = new CContainer(model.name, battleState_8006e398.getGlobalAsset(combatant._1a4).data_00);
    } else {
      //LAB_800c9590
      if(combatant.mrg_00 != null && combatant.mrg_00.get(32).hasVirtualSize()) {
        tmd = new CContainer(model.name, combatant.mrg_00.get(32));
      } else {
        throw new RuntimeException("anim undefined");
      }
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
      FUN_80021520(model, tmd, anim, shadowSizeIndex);
    } else {
      //LAB_800c9664
      initModel(model, tmd, anim);
    }

    for(int i = 0; i < model.modelParts_00.length; i++) {
      model.modelParts_00[i].obj = TmdObjLoader.fromObjTable(
        "CombatantModel (index " + combatant.charSlot_19c + ')' + " (part " + i + ')',
        tmd.tmdPtr_00.tmd.objTable[i]
      );
    }

    //LAB_800c9680
    combatant.assets_14[0]._09++;
  }

  @Method(0x800c9708L)
  public static void loadAttackAnimations(final CombatantStruct1a8 combatant) {
    if(combatant.charIndex_1a2 >= 0 && combatant.mrg_04 == null) {
      combatant.flags_19e |= 0x10;

      final int fileIndex;
      if((combatant.flags_19e & 0x4) == 0) {
        // Enemy attack animations
        fileIndex = 3593 + combatant.charIndex_1a2;
        loadDrgnDir(0, fileIndex, files -> attackAnimationsLoaded(files, combatant, true, -1));
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

        loadDrgnDir(0, fileIndex, files -> attackAnimationsLoaded(files, combatant, false, combatant.charSlot_19c));
      }
    }

    //LAB_800c9888
  }

  @Method(0x800c9898L)
  public static void attackAnimationsLoaded(final List<FileData> files, final CombatantStruct1a8 combatant, final boolean isMonster, final int charSlot) {
    if(combatant.mrg_04 == null) {
      //LAB_800c9910
      if(!isMonster && files.size() == 64) {
        //LAB_800c9940
        for(int animIndex = 0; animIndex < 32; animIndex++) {
          if(files.get(32 + animIndex).hasVirtualSize()) {
            if(combatant.assets_14[animIndex] != null && combatant.assets_14[animIndex]._09 != 0) {
              FUN_800c9c7c(combatant, animIndex);
            }

            //LAB_800c9974
            // Type 6 - TIM file (used to load animation data into VRAM)
            FUN_800c9a80(files.get(32 + animIndex), 6, charSlot, combatant, animIndex);
          }
        }
      }

      //LAB_800c99d8
      combatant.mrg_04 = files;

      //LAB_800c99e8
      for(int animIndex = 0; animIndex < 32; animIndex++) {
        if(files.get(animIndex).hasVirtualSize()) {
          if(combatant.assets_14[animIndex] != null && combatant.assets_14[animIndex]._09 != 0) {
            FUN_800c9c7c(combatant, animIndex);
          }

          //LAB_800c9a18
          FUN_800c9a80(files.get(animIndex), 2, 1, combatant, animIndex);
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
  public static void FUN_800c9a80(final FileData data, final int type, final int a3, final CombatantStruct1a8 combatant, final int animIndex) {
    CombatantAsset0c s3 = combatant.assets_14[animIndex];

    if(s3 != null) {
      FUN_800c9c7c(combatant, animIndex);
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
  public static void FUN_800c9c7c(final CombatantStruct1a8 combatant, final int animIndex) {
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
  public static void FUN_800c9db8(final CombatantStruct1a8 combatant, final int animIndex, final int a2) {
    FUN_800c9c7c(combatant, animIndex);
    FUN_800c9a80(null, 3, a2, combatant, animIndex);
  }

  @Method(0x800c9e10L)
  public static boolean FUN_800c9e10(final CombatantStruct1a8 combatant, final int animIndex) {
    final CombatantAsset0c asset = combatant.assets_14[animIndex];

    if(asset instanceof final CombatantAsset0c.AnimType animType) {
      return animType.anim_00 != null;
    }

    if(asset instanceof final CombatantAsset0c.GlobalAssetType globalAssetType) {
      return globalAssetType.assetIndex_00 >= 0;
    }

    if(asset instanceof final CombatantAsset0c.BpeType bpeType) {
      if(!bpeType.isLoaded_0b) {
        currentCompressedAssetIndex_800c66ac.incr().and(0xf);
        final int index = currentCompressedAssetIndex_800c66ac.get();
        battleState_8006e398.compressedAssets_d8c[index].asset_00 = bpeType;
        battleState_8006e398.compressedAssets_d8c[index].used_04 = true;
        bpeType.isLoaded_0b = true;
        bpeType.compressedAssetIndex_06 = index;

        FUN_800c9fcc(new FileData(Unpacker.decompress(bpeType.bpe_00)), battleState_8006e398.compressedAssets_d8c[index]);
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
  public static void FUN_800c9fcc(final FileData data, final CompressedAsset08 compressedAsset) {
    final CombatantAsset0c asset = compressedAsset.asset_00;

    if(compressedAsset.used_04 && asset.isLoaded_0b) {
      asset.assetIndex_04 = battleState_8006e398.loadGlobalAsset(data, 3);
      asset.compressedAssetIndex_06 = -1;
      compressedAsset.used_04 = false;
    }

    //LAB_800ca040
  }

  @Method(0x800ca100L)
  public static void loadAnimationAssetIntoModel(final Model124 model, final CombatantStruct1a8 combatant, final int animIndex) {
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
  public static void FUN_800ca26c(final CombatantStruct1a8 combatant) {
    //LAB_800ca2bc
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] != null && combatant.assets_14[i]._09 == 0) {
        FUN_800ca194(combatant.assets_14[i]);
      }
    }
  }

  @Method(0x800ca418L)
  public static void FUN_800ca418(final CombatantStruct1a8 combatant) {
    //LAB_800ca488
    //LAB_800ca494
    for(int i = 0; i < 32; i++) {
      if(combatant.assets_14[i] instanceof CombatantAsset0c.AnimType && combatant.assets_14[i].type_0a == 2 || combatant.assets_14[i] instanceof CombatantAsset0c.TimType) {
        //LAB_800ca4c0
        FUN_800c9c7c(combatant, i);
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
  public static void loadCombatantTextures(final CombatantStruct1a8 combatant) {
    if(combatant.charIndex_1a2 >= 0) {
      int fileIndex = gameState_800babc8.charIds_88[combatant.charSlot_19c];

      if((combatant.charIndex_1a2 & 0x1) != 0) {
        if(fileIndex == 0 && (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 != 0) {
          fileIndex = 10;
        }

        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/dragoon".formatted(charName), files -> Bttl.loadCombatantTim(combatant, files));
      } else {
        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/combat".formatted(charName), files -> Bttl.loadCombatantTim(combatant, files));
      }
    }

    //LAB_800ca64c
  }

  @Method(0x800ca75cL)
  public static void loadCombatantTim(@Nullable final CombatantStruct1a8 combatant, final FileData timFile) {
    final int vramSlot;

    if(combatant != null) {
      //LAB_800ca77c
      if(combatant.vramSlot_1a0 == 0) {
        final int charSlot = combatant.charSlot_19c;

        if(charSlot < 0) {
          combatant.vramSlot_1a0 = findFreeMonsterTextureSlot(combatant.charIndex_1a2);
        } else {
          //LAB_800ca7c4
          combatant.vramSlot_1a0 = charSlot + 1;
        }
      }

      vramSlot = combatant.vramSlot_1a0;
    } else {
      vramSlot = 0;
    }

    //LAB_800ca7d0
    loadCombatantTim2(vramSlot, timFile);
  }

  @Method(0x800ca7ecL)
  public static void loadCombatantTim2(final int vramSlot, final FileData timFile) {
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
  public static int findFreeMonsterTextureSlot(final int a0) {
    //LAB_800ca8ac
    //LAB_800ca8c4
    for(int i = a0 < 0x200 ? 4 : 1; i < 9; i++) {
      final int a0_0 = 0x1 << i;

      if((usedMonsterTextureSlots_800c66c4.get() & a0_0) == 0) {
        usedMonsterTextureSlots_800c66c4.or(a0_0);
        return i;
      }

      //LAB_800ca8e4
    }

    //LAB_800ca8f4
    return 0;
  }

  @Method(0x800ca8fcL)
  public static void setMonsterTextureSlotUsed(final int shift) {
    usedMonsterTextureSlots_800c66c4.or(0x1 << shift);
  }

  @Method(0x800ca918L)
  public static void unsetMonsterTextureSlotUsed(final int shift) {
    usedMonsterTextureSlots_800c66c4.and(~(0x1 << shift));
  }

  @Method(0x800cae44L)
  public static void clearCurrentDisplayableItems() {
    currentDisplayableIconsBitset_800c675c.set(0);
  }

  @Method(0x800cb250L)
  public static boolean FUN_800cb250(final ScriptState<BattleEntity27c> state, final BattleEntity27c data) {
    float x = state._e8.x;
    float y = state._e8.y;
    float z = state._e8.z;

    if(state.scriptState_c8 != null) {
      final BattleEntity27c data2 = state.scriptState_c8.innerStruct_00;

      x += data2.model_148.coord2_14.coord.transfer.x;
      y += data2.model_148.coord2_14.coord.transfer.y;
      z += data2.model_148.coord2_14.coord.transfer.z;
    }

    //LAB_800cb2ac
    state.ticks_cc--;
    if(state.ticks_cc > 0) {
      state._d0.sub(state._dc);
      data.model_148.coord2_14.coord.transfer.x = x - state._d0.x;
      data.model_148.coord2_14.coord.transfer.y = y - state._d0.y;
      data.model_148.coord2_14.coord.transfer.z = z - state._d0.z;
      state._dc.y += state._f4;
      return false;
    }

    //LAB_800cb338
    data.model_148.coord2_14.coord.transfer.set(x, y, z);
    return true;
  }

  @Method(0x800cb34cL)
  public static boolean FUN_800cb34c(final ScriptState<BattleEntity27c> state, final BattleEntity27c data) {
    final BattleEntity27c bent = state.scriptState_c8.innerStruct_00;
    final Vector3f vec = bent.model_148.coord2_14.coord.transfer;
    final float angle = MathHelper.atan2(vec.x - data.model_148.coord2_14.coord.transfer.x, vec.z - data.model_148.coord2_14.coord.transfer.z) + MathHelper.PI;

    state.ticks_cc--;
    if(state.ticks_cc > 0) {
      state._d0.x -= state._d0.y; // This is correct, sometimes this vec is used as (angle, step)
      data.model_148.coord2_14.transforms.rotate.y = angle + state._d0.x;
      return false;
    }

    //LAB_800cb3e0
    data.model_148.coord2_14.transforms.rotate.y = angle;

    //LAB_800cb3e8
    return true;
  }

  @ScriptDescription("Sets the position of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cb3fcL)
  public static FlowControl scriptSetBentPos(final RunningScript<?> script) {
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
  public static FlowControl scriptGetBentPos(final RunningScript<?> script) {
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
  public static FlowControl scriptSetBentRotation(final RunningScript<?> script) {
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
  public static FlowControl scriptSetBentRotationY(final RunningScript<?> script) {
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
  public static FlowControl scriptGetBentRotation(final RunningScript<?> script) {
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
  public static FlowControl scriptGetMonsterStatusResistFlags(final RunningScript<?> script) {
    final MonsterBattleEntity monster = (MonsterBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(monster.monsterStatusResistFlag_76);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets bit 10 of battle entity script flags (possibly whether a battle entity renders and animates?)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set the flag, false otherwise")
  @Method(0x800cb618L)
  public static FlowControl FUN_800cb618(final RunningScript<?> script) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cb668
    if(script.params_20[1].get() != 0) {
      a1.storage_44[7] &= 0xffff_ffef;
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
  public static FlowControl scriptSetInterpolationEnabled(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.disableInterpolation_a2 = script.params_20[1].get() == 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to loading a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "animIndex", description = "The animation index")
  @Method(0x800cb6bcL)
  public static FlowControl FUN_800cb6bc(final RunningScript<?> a0) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    if((state.storage_44[7] & 0x1) != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;
    final int animIndex = a0.params_20[1].get();

    if(bent.currentAnimIndex_270 < 0) {
      FUN_800c9e10(bent.combatant_144, animIndex);
      bent.currentAnimIndex_270 = animIndex;
    } else if(bent.currentAnimIndex_270 != animIndex) {
      FUN_800ca194(bent.combatant_144.assets_14[bent.currentAnimIndex_270]);

      //LAB_800cb73c
      FUN_800c9e10(bent.combatant_144, animIndex);
      bent.currentAnimIndex_270 = animIndex;
    }

    //LAB_800cb750
    return FlowControl.PAUSE;
  }

  @ScriptDescription("No-op")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @Method(0x800cb764L)
  public static FlowControl FUN_800cb764(final RunningScript<?> a0) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related battle entity animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb76cL)
  public static FlowControl FUN_800cb76c(final RunningScript<?> a0) {
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
        loadAnimationAssetIntoModel(s0.model_148, s0.combatant_144, animIndex);
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
  public static FlowControl scriptSetLoadingBentAnimationIndex(final RunningScript<?> script) {
    final ScriptState<?> s2 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c s0 = (BattleEntity27c)s2.innerStruct_00;

    if((s2.storage_44[7] & 0x1) == 0) {
      final int newAnim = script.params_20[1].get();
      final int currentAnim = s0.currentAnimIndex_270;

      if(currentAnim >= 0) {
        if(currentAnim != newAnim) {
          FUN_800ca194(s0.combatant_144.assets_14[currentAnim]);
        }

        //LAB_800cb8d0
        s0.currentAnimIndex_270 = -1;
      }

      //LAB_800cb8d4
      if(s0.combatant_144.isAssetLoaded(newAnim)) {
        FUN_800ca194(s0.combatant_144.assets_14[s0.loadingAnimIndex_26e]);
        loadAnimationAssetIntoModel(s0.model_148, s0.combatant_144, newAnim);
        s2.storage_44[7] &= 0xffff_ff6f;
        s0.model_148.animationState_9c = 1;
        s0.loadingAnimIndex_26e = newAnim;
        s0.currentAnimIndex_270 = -1;
        return FlowControl.CONTINUE;
      }

      //LAB_800cb934
      FUN_800c9e10(s0.combatant_144, newAnim);
    }

    //LAB_800cb944
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Something related battle entity animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb95cL)
  public static FlowControl FUN_800cb95c(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800ca26c(bent.combatant_144);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's loading animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "animationIndex", description = "The animation index")
  @Method(0x800cb9b0L)
  public static FlowControl scriptGetLoadingBentAnimationIndex(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.loadingAnimIndex_26e);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Pauses a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cb9f0L)
  public static FlowControl scriptPauseAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animationState_9c = 2;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Resumes a battle entity's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cba28L)
  public static FlowControl scriptResumeAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animationState_9c = 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's loop state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "loop", description = "True to enable looping, false to disable")
  @Method(0x800cba60L)
  public static FlowControl scriptSetBentAnimationLoopState(final RunningScript<?> script) {
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
  public static FlowControl scriptAnimationHasFinished(final RunningScript<?> script) {
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
  public static FlowControl FUN_800cbb00(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex];
    BattleEntity27c v1 = state.innerStruct_00;

    float x = v1.model_148.coord2_14.coord.transfer.x;
    float y = v1.model_148.coord2_14.coord.transfer.y;
    float z = v1.model_148.coord2_14.coord.transfer.z;

    final int t0 = script.params_20[1].get();
    if(t0 >= 0) {
      state.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[t0];
      v1 = state.scriptState_c8.innerStruct_00;
      x -= v1.model_148.coord2_14.coord.transfer.x;
      y -= v1.model_148.coord2_14.coord.transfer.y;
      z -= v1.model_148.coord2_14.coord.transfer.z;
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbb98
    FUN_800cdc1c(state, x, y, z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0, script.params_20[2].get());
    state.setTempTicker(Bttl::FUN_800cb250);
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
  public static FlowControl FUN_800cbc14(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = state.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);
    final int scriptIndex2 = script.params_20[1].get();

    if(scriptIndex2 >= 0) {
      state.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = state.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbcc4
    final float x = script.params_20[3].get() - vec.x;
    final float y = script.params_20[4].get() - vec.y;
    final float z = script.params_20[5].get() - vec.z;
    FUN_800cdc1c(state, vec.x, vec.y, vec.z, x, y, z, 0, Math.round(Math.sqrt(x * x + y * y + z * z) / script.params_20[2].get()));
    state.setTempTicker(Bttl::FUN_800cb250);
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
  public static FlowControl FUN_800cbde0(final RunningScript<?> script) {
    final ScriptState<BattleEntity27c> state = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    BattleEntity27c bent = state.innerStruct_00;
    float x = bent.model_148.coord2_14.coord.transfer.x;
    float y = bent.model_148.coord2_14.coord.transfer.y;
    float z = bent.model_148.coord2_14.coord.transfer.z;

    if(script.params_20[1].get() >= 0) {
      state.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
      bent = state.scriptState_c8.innerStruct_00;
      x -= bent.model_148.coord2_14.coord.transfer.x;
      y -= bent.model_148.coord2_14.coord.transfer.y;
      z -= bent.model_148.coord2_14.coord.transfer.z;
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbe78
    FUN_800cdc1c(state, x, y, z, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0x20, script.params_20[2].get());
    state.setTempTicker(Bttl::FUN_800cb250);
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
  public static FlowControl FUN_800cbef8(final RunningScript<?> state) {
    final int scriptIndex1 = state.params_20[0].get();
    final int scriptIndex2 = state.params_20[1].get();
    final ScriptState<BattleEntity27c> s5 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = s5.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      s5.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = s5.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      s5.scriptState_c8 = null;
    }

    //LAB_800cbfa8
    final float x = state.params_20[3].get() - vec.x;
    final float y = state.params_20[4].get() - vec.y;
    final float z = state.params_20[5].get() - vec.z;
    FUN_800cdc1c(s5, vec.x, vec.y, vec.z, state.params_20[3].get(), state.params_20[4].get(), state.params_20[5].get(), 0x20, Math.round(Math.sqrt(x * x + y * y + z * z) / state.params_20[2].get()));
    s5.setTempTicker(Bttl::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc0c8L)
  public static FlowControl FUN_800cc0c8(final RunningScript<?> script) {
    final int s0 = script.params_20[0].get();
    final ScriptState<BattleEntity27c> a0 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[s0];
    final Vector3f a1 = new Vector3f(a0.innerStruct_00.model_148.coord2_14.coord.transfer);
    final int t0 = script.params_20[1].get();

    if(t0 >= 0) {
      a0.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[t0];
      a1.sub(a0.scriptState_c8.innerStruct_00.model_148.coord2_14.coord.transfer);
    } else {
      a0.scriptState_c8 = null;
    }

    //LAB_800cc160
    FUN_800cdc1c(a0, a1.x, a1.y, a1.z, script.params_20[3].get(), a1.y, script.params_20[4].get(), 0, script.params_20[2].get());
    a0.setTempTicker(Bttl::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc1ccL)
  public static FlowControl FUN_800cc1cc(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      state1.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = state1.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      state1.scriptState_c8 = null;
    }

    //LAB_800cc27c
    final float x = script.params_20[3].get() - vec.x;
    final float z = script.params_20[4].get() - vec.z;
    FUN_800cdc1c(state1, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0, Math.round(Math.sqrt(x * x + z * z) / script.params_20[2].get()));
    state1.setTempTicker(Bttl::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc364L)
  public static FlowControl FUN_800cc364(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      state1.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = state1.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      state1.scriptState_c8 = null;
    }

    //LAB_800cc3fc
    FUN_800cdc1c(state1, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0x20, script.params_20[2].get());
    state1.setTempTicker(Bttl::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex0", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex1", description = "A BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @Method(0x800cc46cL)
  public static FlowControl FUN_800cc46c(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> s5 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleEntity27c bent1 = s5.innerStruct_00;
    final Vector3f vec = new Vector3f(bent1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      s5.scriptState_c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleEntity27c bent2 = s5.scriptState_c8.innerStruct_00;
      vec.sub(bent2.model_148.coord2_14.coord.transfer);
    } else {
      s5.scriptState_c8 = null;
    }

    //LAB_800cc51c
    final float x = script.params_20[3].get() - vec.x;
    final float z = script.params_20[4].get() - vec.z;
    FUN_800cdc1c(s5, vec.x, vec.y, vec.z, script.params_20[3].get(), vec.y, script.params_20[4].get(), 0x20, Math.round(Math.sqrt(x * x + z * z) / script.params_20[2].get()));
    s5.setTempTicker(Bttl::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Turn a battle entity towards another")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndexToTurn", description = "A BattleEntity27c script index to turn")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndexTarget", description = "A BattleEntity27c script index to target")
  @Method(0x800cc608L)
  public static FlowControl scriptBentLookAtBent(final RunningScript<?> script) {
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
  public static FlowControl FUN_800cc698(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleEntity27c> state1 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final ScriptState<BattleEntity27c> state2 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
    final BattleEntity27c bent1 = state1.innerStruct_00;
    final BattleEntity27c bent2 = state2.innerStruct_00;
    final int ticks = script.params_20[2].get();
    final float v0 = MathHelper.floorMod(MathHelper.atan2(bent2.model_148.coord2_14.coord.transfer.x - bent1.model_148.coord2_14.coord.transfer.x, bent2.model_148.coord2_14.coord.transfer.z - bent1.model_148.coord2_14.coord.transfer.z) - bent1.model_148.coord2_14.transforms.rotate.y, MathHelper.TWO_PI) - MathHelper.PI;
    state1.scriptState_c8 = state2;
    state1.ticks_cc = ticks;
    state1._d0.x = v0;
    state1._d0.y = v0 / ticks;
    state1.setTempTicker(Bttl::FUN_800cb34c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc784L)
  public static FlowControl FUN_800cc784(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800ca418(bent.combatant_144);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to battle entity asset loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cc7d8L)
  public static FlowControl FUN_800cc7d8(final RunningScript<?> script) {
    final ScriptState<BattleEntity27c> state = SCRIPTS.getState(script.params_20[0].get(), BattleEntity27c.class);
    final int s2 = state.storage_44[7] & 0x4;
    final CombatantStruct1a8 bentCombatant = state.innerStruct_00.combatant_144;

    //LAB_800cc83c
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 combatant = getCombatant(i);

      if(combatant != bentCombatant) {
        if((combatant.flags_19e & 0x1) != 0 && combatant.mrg_04 != null && combatant.charIndex_1a2 >= 0) {
          final int v0 = combatant.flags_19e >>> 2 ^ 1;

          if(s2 == 0) {
            //LAB_800cc8ac
            if((v0 & 1) == 0) {
              //LAB_800cc8b4
              FUN_800ca418(combatant);
            }
          } else {
            if((v0 & 1) != 0) {
              FUN_800ca418(combatant);
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
  public static FlowControl scriptLoadAttackAnimations(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    loadAttackAnimations(bent.combatant_144);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads the attack animations for every combatant")
  @Method(0x800cc948L)
  public static FlowControl loadAllCharAttackAnimations(final RunningScript<?> script) {
    //LAB_800cc970
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 combatant = getCombatant(i);
      if((combatant.flags_19e & 0x1) != 0 && combatant.charIndex_1a2 >= 0) {
        loadAttackAnimations(combatant);
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
  public static FlowControl scriptEnableBentTextureAnimation(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.animateTextures_ec[script.params_20[1].get()] = script.params_20[2].get() > 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets up battle menu, handles its input, and renders it")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "displayableIconsBitset", description = "A bitset of which icons are displayed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "disabledIconsBitset", description = "A bitset of which icons are disabled")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "selectedAction", description = "The action the player has selected")
  @Method(0x800cca34L)
  public static FlowControl scriptSetUpAndHandleCombatMenu(final RunningScript<BattleEntity27c> script) {
    final int displayableIconsBitset = script.params_20[0].get();

    if(currentDisplayableIconsBitset_800c675c.get() != displayableIconsBitset || (script.scriptState_04.storage_44[7] & 0x1000) != 0) {
      //LAB_800cca7c
      final int disabledIconsBitset;

      if(script.paramCount_14 == 2) {
        disabledIconsBitset = 0;
      } else {
        //LAB_800ccaa0
        disabledIconsBitset = script.params_20[1].get();
      }

      //LAB_800ccab4
      battleMenu_800c6c34.initializeMenuIcons(script.scriptState_04, displayableIconsBitset, disabledIconsBitset);

      script.scriptState_04.storage_44[7] &= 0xffff_efff;
      currentDisplayableIconsBitset_800c675c.set(displayableIconsBitset);
    }

    //LAB_800ccaec
    battleMenu_800c6c34.toggleHighlight(true);

    final int selectedAction = battleMenu_800c6c34.tickAndRender();
    if(selectedAction == 0) {
      //LAB_800ccb24
      return FlowControl.PAUSE_AND_REWIND;
    }

    battleMenu_800c6c34.toggleHighlight(false);
    script.params_20[2].set(selectedAction - 1);

    //LAB_800ccb28
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a damage number to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @Method(0x800ccb3cL)
  public static FlowControl scriptRenderDamage(final RunningScript<?> script) {
    hud.renderDamage(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a floating number above a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The number to show")
  @Method(0x800ccb70L)
  public static FlowControl scriptAddFloatingNumberForBent(final RunningScript<?> script) {
    hud.addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), 13);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "isDragoon", description = "Whether or not the battle entity is a dragoon")
  @Method(0x800ccba4L)
  public static FlowControl FUN_800ccba4(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;
    final CombatantStruct1a8 combatant = bent.combatant_144;

    if((state.storage_44[7] & 0x1) == 0) {
      if(bent.currentAnimIndex_270 >= 0) {
        FUN_800ca194(bent.combatant_144.assets_14[bent.currentAnimIndex_270]);
      }

      //LAB_800ccc24
      deallocateCombatant(bent.combatant_144);

      if(script.params_20[1].get() != 0) {
        state.storage_44[7] |= 0x3;
        combatant.charIndex_1a2 |= 0x1;
      } else {
        //LAB_800ccc60
        state.storage_44[7] = state.storage_44[7] & 0xffff_fffd | 0x1;
        combatant.charIndex_1a2 &= 0xfffe;
      }

      //LAB_800ccc78
      loadCombatantTextures(bent.combatant_144);
      loadCombatantTmdAndAnims(combatant);
      //LAB_800ccc94
    } else if((combatant.flags_19e & 0x20) == 0) {
      FUN_800c952c(bent.model_148, combatant);
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
  public static FlowControl scriptGetCharOrMonsterId(final RunningScript<?> script) {
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
  public static FlowControl scriptSetBentStat(final RunningScript<?> script) {
    int value = script.params_20[1].get();
    if(script.params_20[2].get() == 2 && value < 0) {
      value = 0;
    }

    //LAB_800ccd8c
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.setStat(BattleEntityStat.fromLegacy(script.params_20[2].get()), value);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new stat value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @Method(0x800ccda0L)
  public static FlowControl scriptSetBentRawStat(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.setStat(BattleEntityStat.fromLegacy(Math.max(0, script.params_20[2].get())), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The stat value")
  @Method(0x800cce04L)
  public static FlowControl scriptGetBentStat(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[2].set(bent.getStat(BattleEntityStat.fromLegacy(script.params_20[1].get())));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a stat value of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.ENUM, name = "statIndex", description = "The stat index")
  @ScriptEnum(BattleEntityStat.class)
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The stat value")
  @Method(0x800cce70L)
  public static FlowControl scriptGetBentStat2(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[2].set(bent.getStat(BattleEntityStat.fromLegacy(script.params_20[1].get())));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to HUD")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800ccec8L)
  public static FlowControl FUN_800ccec8(final RunningScript<?> script) {
    hud.FUN_800f1a00(script.params_20[0].get() > 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets post-battle action to 3")
  @Method(0x800ccef8L)
  public static FlowControl FUN_800ccef8(final RunningScript<?> script) {
    postBattleActionIndex_800bc974 = 3;
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets the post-battle action")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "action", description = "The post-battle action")
  @Method(0x800ccf0cL)
  public static FlowControl scriptSetPostBattleAction(final RunningScript<?> script) {
    postBattleActionIndex_800bc974 = script.params_20[0].get();
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Sets a battle entity as dead (or not dead) and drops its loot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dead", description = "True for dead, false otherwise")
  @Method(0x800ccf2cL)
  public static FlowControl scriptSetBentDeadAndDropLoot(final RunningScript<?> script) {
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
                  itemsDroppedByEnemies_800bc928.add(new EnemyDrop(equipment.getIcon(), equipment.getName(), () -> giveEquipment(equipment), () -> equipmentOverflow.add(equipment)));
                } else if(drop.item() instanceof final Item item) {
                  itemsDroppedByEnemies_800bc928.add(new EnemyDrop(item.getIcon(), item.getName(), () -> giveItem(item), () -> itemOverflow.add(item)));
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
    cacheLivingBents();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity as dead (or not), but doesn't drop loot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dead", description = "True for dead, false otherwise")
  @Method(0x800cd078L)
  public static FlowControl scriptSetBentDead(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cd0d0
    if(script.params_20[1].get() != 0) {
      state.storage_44[7] |= 0x40;
    } else {
      //LAB_800cd0c4
      state.storage_44[7] &= 0xffff_ffbf;
    }

    cacheLivingBents();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the hit property of a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hitNum", description = "The hit number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hitPropertyIndex", description = "The hit property index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The hit property value")
  @Method(0x800cd0ecL)
  public static FlowControl scriptGetHitProperty(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[3].set(getHitProperty(
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
  public static FlowControl scriptLevelUpAddition(final RunningScript<?> script) {
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
  public static FlowControl scriptSetModelPartVisibility(final RunningScript<?> script) {
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

  @ScriptDescription("Unknown, something to do with loading files")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "drgnIndex", description = "The DRGN#.BIN index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "index", description = "The output battleState_8006e398._580 array index")
  @Method(0x800cd468L)
  public static FlowControl FUN_800cd468(final RunningScript<?> script) {
    script.params_20[2].set(battleState_8006e398.loadGlobalAsset(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with loading files, may wait until the file is loaded")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The battleState_8006e398._580 array index")
  @Method(0x800cd4b0L)
  public static FlowControl FUN_800cd4b0(final RunningScript<?> script) {
    final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(script.params_20[0].get());
    return v0.state_04 == 1 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with loading files, may clear the file entry")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The battleState_8006e398._580 array index")
  @Method(0x800cd4f0L)
  public static FlowControl FUN_800cd4f0(final RunningScript<?> script) {
    battleState_8006e398.deallocateGlobalAsset(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a combatant to the battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charIndexAndFlags", description = "Exact use unknown, seems to be flags, and possibly character ID in the higher bits?")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The new combatant's index")
  @Method(0x800cd52cL)
  public static FlowControl scriptAddCombatant(final RunningScript<?> script) {
    script.params_20[1].set(addCombatant(script.params_20[0].get(), -1));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates and removes a combatant from the battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant's index")
  @Method(0x800cd570L)
  public static FlowControl scriptDeallocateAndRemoveCombatant(final RunningScript<?> script) {
    deallocateCombatant(combatants_8005e398[script.params_20[0].get()]);
    removeCombatant(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocate a battle entity child of this script")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "entrypointIndex", description = "The entrypoint of this script for the new battle entity to enter")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant to attach to the new battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The allocated script index")
  @Method(0x800cd5b4L)
  public static FlowControl scriptAllocateBent(final RunningScript<?> script) {
    final String name = "Bent allocated by script " + script.scriptState_04.index;
    final MonsterBattleEntity bent = new MonsterBattleEntity(name);
    final ScriptState<MonsterBattleEntity> state = SCRIPTS.allocateScriptState(name, bent);
    script.params_20[2].set(state.index);
    state.setTicker(bent::bentLoadingTicker);
    state.setDestructor(bent::bentDestructor);
    state.loadScriptFile(script.scriptState_04.scriptPtr_14, script.params_20[0].get());
    state.storage_44[7] |= 0x804;
    battleState_8006e398.allBents_e0c[allBentCount_800c66d0.get()] = state;
    battleState_8006e398.monsterBents_e50[monsterCount_800c6768.get()] = state;

    final CombatantStruct1a8 combatant = getCombatant(script.params_20[1].get());
    bent.combatant_144 = combatant;
    bent.combatantIndex_26c = script.params_20[1].get();
    bent.charId_272 = combatant.charIndex_1a2;
    bent.bentSlot_274 = allBentCount_800c66d0.get();
    allBentCount_800c66d0.incr();
    bent.charSlot_276 = monsterCount_800c6768.get();
    monsterCount_800c6768.incr();
    bent.model_148.coord2_14.coord.transfer.set(0, 0, 0);
    bent.model_148.coord2_14.transforms.rotate.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd740L)
  public static FlowControl FUN_800cd740(final RunningScript<?> script) {
    final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(script.params_20[0].get());

    if(v0.state_04 == 1) {
      //LAB_800cd794
      return FlowControl.PAUSE_AND_REWIND;
    }

    combatants_8005e398[script.params_20[0].get()]._1a4 = (short)script.params_20[1].get();

    //LAB_800cd798
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0", description = "Combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd7a8L)
  public static FlowControl FUN_800cd7a8(final RunningScript<?> script) {
    final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(script.params_20[0].get());

    if(v0.state_04 == 1) {
      //LAB_800cd7fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    combatants_8005e398[script.params_20[0].get()]._1a6 = (short)script.params_20[1].get();

    //LAB_800cd800
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatants")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @Method(0x800cd810L)
  public static FlowControl FUN_800cd810(final RunningScript<?> script) {
    final int s0 = script.params_20[2].get();

    if(s0 >= 0) {
      //LAB_800cd85c
      final BattleAsset08 v0 = battleState_8006e398.getGlobalAsset(s0);

      if(v0.state_04 == 1) {
        return FlowControl.PAUSE_AND_REWIND;
      }

      FUN_800c9db8(combatants_8005e398[script.params_20[0].get()], script.params_20[1].get(), s0);
    } else {
      FUN_800c9c7c(combatants_8005e398[script.params_20[0].get()], script.params_20[1].get());
    }

    //LAB_800cd890
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to combatant textures")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800cd8a4L)
  public static FlowControl FUN_800cd8a4(final RunningScript<?> script) {
    final BattleAsset08 a1 = battleState_8006e398.getGlobalAsset(script.params_20[1].get());

    if(a1.state_04 == 1) {
      //LAB_800cd8fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int combatantIndex = script.params_20[0].get();
    final CombatantStruct1a8 combatant = combatantIndex >= 0 ? combatants_8005e398[combatantIndex] : null; // Not sure if the else case actually happens
    loadCombatantTim(combatant, a1.data_00);

    //LAB_800cd900
    return FlowControl.PAUSE;
  }

  @ScriptDescription("Unknown, loads files")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "drgnIndex", description = "The DRGN#.BIN index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "queueIndex", description = "The index into the file queue")
  @Method(0x800cd910L)
  public static FlowControl FUN_800cd910(final RunningScript<?> script) {
    script.params_20[2].set(battleState_8006e398.loadGlobalAsset(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the combatant index for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "combatantIndex", description = "The combatant index")
  @Method(0x800cd958L)
  public static FlowControl scriptGetCombatantIndex(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the battle entity slot or char slot for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "charOrBentSlot", description = "The character or battle entity slot")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "mode", description = "If true, returns character slot, if false returns battle entity slot")
  @Method(0x800cd998L)
  public static FlowControl scriptGetBentSlot(final RunningScript<?> script) {
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
  public static FlowControl scriptGetBentNobj(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bent.model_148.modelParts_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Deallocates a battle entity's combatant")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cda3cL)
  public static FlowControl scriptDeallocateCombatant(final RunningScript<?> script) {
    deallocateCombatant(combatants_8005e398[script.params_20[0].get()]);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to asset loading")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800cda78L)
  public static FlowControl FUN_800cda78(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(!bent.combatant_144.isModelLoaded()) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800cdacc
    bent.combatant_144.charIndex_1a2 = -1;

    //LAB_800cdaf4
    bent.loadingAnimIndex_26e = 0;
    FUN_800c952c(bent.model_148, bent.combatant_144);

    //LAB_800cdb08
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stops rendering the battle stage and skybox")
  @Method(0x800cdb18L)
  public static FlowControl scriptStopRenderingStage(final RunningScript<?> script) {
    setStageHasNoModel();
    setDontRenderStageBackground();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a new battle stage")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stageIndex", description = "The stage index")
  @Method(0x800cdb44L)
  public static FlowControl scriptLoadStage(final RunningScript<?> script) {
    loadStage(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears combat scripts and sets the combat disabled flag")
  @Method(0x800cdb74L)
  public static FlowControl scriptDisableCombat(final RunningScript<?> script) {
    combatDisabled_800c66b9.set(true);

    //LAB_800cdbb8
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      battleState_8006e398.charBents_e40[i].loadScriptFile(null);
    }

    //LAB_800cdbe0
    playerBattleScript_800c66fc = null;

    //LAB_800cdc00
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdc1cL)
  public static void FUN_800cdc1c(final ScriptState<BattleEntity27c> s1, final float x0, final float y0, final float z0, final float x1, final float y1, final float z1, final float a7, final int ticks) {
    final float dx = x1 - x0;
    final float dy = y1 - y0;
    final float dz = z1 - z0;

    s1.ticks_cc = ticks;
    s1._e8.set(x1, y1, z1);
    s1._d0.set(dx, dy, dz);

    // Fix for retail /0 bug
    if(ticks > 0) {
      s1._dc.set(dx / ticks, 0.0f, dz / ticks);
    } else {
      s1._dc.zero();
    }

    s1._dc.y = FUN_80013404(a7, dy, ticks);
    s1._f4 = a7;
  }

  @ScriptDescription("Allocates a weapon trail effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The battle object index to trail behind")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "dobjIndex", description = "The model index")
  @Method(0x800ce6a8L)
  public static FlowControl scriptAllocateWeaponTrailEffect(final RunningScript<? extends BattleObject> script) {
    final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final WeaponTrailEffect3c trail = new WeaponTrailEffect3c(script.params_20[2].get(), parent);

    final ScriptState<EffectManagerData6c<EffectManagerParams.WeaponTrailType>> state = allocateEffectManager(
      "Weapon trail",
      script.scriptState_04,
      trail::tickWeaponTrailEffect,
      trail::renderWeaponTrailEffect,
      null,
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
  public static FlowControl scriptApplyWeaponTrailScaling(final RunningScript<?> script) {
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class).effect_44;
    trail.applyWeaponTrailScaling(trail.largestVertex_10, trail.smallestVertex_20, script.params_20[2].get() / (float)0x1000, script.params_20[1].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cea1cL)
  public static void scriptGetScriptedObjectPos(final int scriptIndex, final Vector3f posOut) {
    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    posOut.set(bobj.getPosition());
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
  public static FlowControl scriptAllocateFullScreenOverlay(final RunningScript<? extends BattleObject> script) {
    final int r = script.params_20[1].get() << 8;
    final int g = script.params_20[2].get() << 8;
    final int b = script.params_20[3].get() << 8;
    final int fullR = script.params_20[4].get() << 8 & 0xffff; // Retail bug in violet dragon - overflow
    final int fullG = script.params_20[5].get() << 8 & 0xffff; //
    final int fullB = script.params_20[6].get() << 8 & 0xffff; //
    final int ticks = script.params_20[7].get() & 0xffff;

    final FullScreenOverlayEffect0e effect = new FullScreenOverlayEffect0e(r, g, b, fullR, fullG, fullB, ticks);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "Full screen overlay rgb(%x, %x, %x) -> rgb(%x, %x, %x)".formatted(r, g, b, fullR, fullG, fullB),
      script.scriptState_04,
      effect::tickFullScreenOverlay,
      effect::renderFullScreenOverlay,
      null,
      effect
    );

    state.innerStruct_00.params_10.flags_00 = 0x5000_0000;

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Generates a random number using the Mersenne Twister algorithm")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The random number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "min", description = "The minimum value (inclusive)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "max", description = "The maximum value (exclusive)")
  @Method(0x800cee50L)
  public static FlowControl scriptRand(final RunningScript<?> script) {
    final int min = script.params_20[1].get();
    final int max = script.params_20[2].get();
    script.params_20[0].set(seed_800fa754.nextInt(min, max));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a weapon trail effect's segment count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The segment count")
  @Method(0x800ceeccL)
  public static FlowControl scriptSetWeaponTrailSegmentCount(final RunningScript<?> script) {
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
  public static FlowControl scriptRenderColouredQuad(final RunningScript<?> script) {
    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(script.params_20[3].get() + 1))
      .rgb(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get())
      .pos(-160, -120, 320, 280)
    );

    return FlowControl.CONTINUE;
  }

  @Method(0x800cf03cL)
  public static int FUN_800cf03c(final EffectManagerData6c<?> manager, final Attachment18 attachment) {
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
  public static FlowControl FUN_800cf0b4(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Attachment18 attachment = manager.addAttachment(0, 0, Bttl::FUN_800cf03c, new Attachment18());
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

  /** @return Z */
  @Method(0x800cf244L)
  public static float transformWorldspaceToScreenspace(final Vector3f pos, final Vector2f out) {
    final Vector3f sp0x10 = new Vector3f();
    pos.mul(worldToScreenMatrix_800c3548, sp0x10);
    sp0x10.add(worldToScreenMatrix_800c3548.transfer);
    out.x = MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.x, sp0x10.z);
    out.y = MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.y, sp0x10.z);
    return sp0x10.z;
  }

  @Method(0x800cf37cL)
  public static void rotateAndTranslateEffect(final EffectManagerData6c<?> manager, @Nullable final Vector3f extraRotation, final Vector3f vertex, final Vector3f out) {
    final Vector3f rotations = new Vector3f(manager.params_10.rot_10);

    if(extraRotation != null) {
      //LAB_800cf3c4
      rotations.add(extraRotation);
    }

    //LAB_800cf400
    final MV transforms = new MV();
    transforms.rotationXYZ(rotations);

    vertex.mul(transforms, out);
    out.add(transforms.transfer);
  }

  @Method(0x800cf4f4L)
  public static void getRelativeOffset(final EffectManagerData6c<?> manager, @Nullable final Vector3f extraRotation, final Vector3f in, final Vector3f out) {
    final MV sp0x28 = new MV();

    final Vector3f sp0x20 = new Vector3f(manager.getRotation());

    if(extraRotation != null) {
      //LAB_800cf53c
      sp0x20.add(extraRotation);
    }

    //LAB_800cf578
    sp0x28.rotationXYZ(sp0x20);
    sp0x28.transfer.set(manager.getPosition());

    in.mul(sp0x28, out);
    out.add(sp0x28.transfer);
  }

  @Method(0x800cf684L)
  public static void FUN_800cf684(final Vector3f rotation, final Vector3f translation, final Vector3f vector, final Vector3f out) {
    final MV transforms = new MV();
    transforms.rotationXYZ(rotation);
    transforms.transfer.set(translation);
    vector.mul(transforms, out);
    out.add(transforms.transfer);
  }

  /** @return Z */
  @Method(0x800cf7d4L)
  public static float FUN_800cf7d4(final Vector3f rotation, final Vector3f translation1, final Vector3f translation2, final Vector2f out) {
    final Vector3f sp0x10 = new Vector3f(translation1);
    sp0x10.mul(worldToScreenMatrix_800c3548);

    GTE.setTransforms(worldToScreenMatrix_800c3548);

    final MV oldTransforms = new MV();
    GTE.getTransforms(oldTransforms);

    final MV sp0x58 = new MV();
    sp0x58.rotationXYZ(rotation);
    oldTransforms.mul(sp0x58);
    oldTransforms.transfer.add(sp0x10);

    GTE.setTransforms(oldTransforms);
    GTE.perspectiveTransform(translation2);

    out.set(GTE.getScreenX(2), GTE.getScreenY(2));
    return GTE.getScreenZ(3);
  }

  /** @return Z */
  @Method(0x800cfb14L)
  public static float FUN_800cfb14(final EffectManagerData6c<?> manager, final Vector3f translation, final Vector2f out) {
    return FUN_800cf7d4(manager.params_10.rot_10, manager.params_10.trans_04, translation, out);
  }

  /** @return Z */
  @Method(0x800cfb94L)
  public static float FUN_800cfb94(final EffectManagerData6c<?> manager, final Vector3f rotation, final Vector3f translation, final Vector2f out) {
    final Vector3f tempRotation = new Vector3f(manager.params_10.rot_10).add(rotation);
    return FUN_800cf7d4(tempRotation, manager.params_10.trans_04, translation, out);
  }

  /** @return Z */
  @Method(0x800cfc20L)
  public static float FUN_800cfc20(final Vector3f managerRotation, final Vector3f managerTranslation, final Vector3f translation, final Vector2f out) {
    return FUN_800cf7d4(managerRotation, managerTranslation, translation, out);
  }

  @ScriptDescription("Gets a battle object's local world matrix translation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800cfcccL)
  public static FlowControl scriptGetBobjLocalWorldMatrixTranslation(final RunningScript<?> script) {
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
  public static FlowControl scriptGetBentDimension(final RunningScript<?> script) {
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
  public static FlowControl FUN_800cfec8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800cfed0L)
  public static void setMtSeed(final long seed) {
    seed_800fa754.setSeed(seed ^ 0x75b_d924L);
  }

  @ScriptDescription("Sets the Mersenne Twister random number seed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "seed", description = "The seed")
  @Method(0x800cff24L)
  public static FlowControl scriptSetMtSeed(final RunningScript<?> script) {
    setMtSeed(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /** Sets translation vector to position of individual part of model associated with scriptIndex */
  @Method(0x800cffd8L)
  public static void getModelObjectTranslation(final BattleEntity27c bent, final Vector3f translation, final int objIndex) {
    final MV transformMatrix = new MV();
    GsGetLw(bent.model_148.modelParts_00[objIndex].coord2_04, transformMatrix);
    translation.set(transformMatrix.transfer);
  }

  @ScriptDescription("Gets a battle object model's part count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "partCount", description = "The part count")
  @Method(0x800d0124L)
  public static FlowControl scriptGetBobjModelPartCount(final RunningScript<?> script) {
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
  public static FlowControl scriptAllocateProjectileHitEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();
    final int r = script.params_20[2].get();
    final int g = script.params_20[3].get();
    final int b = script.params_20[4].get();

    final ProjectileHitEffect14 effect = new ProjectileHitEffect14(count, r, g, b);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "ProjectileHitEffect14",
      script.scriptState_04,
      null,
      effect::renderProjectileHitEffect,
      null,
      effect
    );

    //LAB_800d0980
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d09b8L)
  public static FlowControl FUN_800d09b8(final RunningScript<?> script) {
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
  public static FlowControl scriptAllocateAdditionSparksEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();
    final int r = script.params_20[2].get();
    final int g = script.params_20[3].get();
    final int b = script.params_20[4].get();
    final int distance = script.params_20[5].get();
    final int ticks = script.params_20[6].get();

    final AdditionSparksEffect08 effect = new AdditionSparksEffect08(count, distance, ticks, r, g, b);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "AdditionSparksEffect08",
      script.scriptState_04,
      null,
      effect::renderAdditionSparks,
      null,
      effect
    );

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
  public static FlowControl scriptAllocateAdditionStarburstEffect(final RunningScript<? extends BattleObject> script) {
    final int parentIndex = script.params_20[1].get();
    final int rayCount = script.params_20[2].get();

    final AdditionStarburstEffect10 effect = new AdditionStarburstEffect10(parentIndex, rayCount);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "AdditionStarburstEffect10",
      script.scriptState_04,
      null,
      effect.additionStarburstRenderers_800c6dc4[script.params_20[3].get()],
      null,
      effect
    );

    //LAB_800d1c7c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d1cacL)
  public static FlowControl FUN_800d1cac(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_800d1cac)", script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d1cf4L)
  public static FlowControl FUN_800d1cf4(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_800d1cf4)", script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a radial gradient effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of subdivisions in the gradient")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The gradient type")
  @Method(0x800d2734L)
  public static FlowControl scriptAllocateRadialGradientEffect(final RunningScript<? extends BattleObject> script) {
    final int circleSubdivisionModifier = script.params_20[1].get();
    final int type = script.params_20[2].get();

    final RadialGradientEffect14 effect = new RadialGradientEffect14(type, circleSubdivisionModifier);

    final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state = allocateEffectManager(
      "RadialGradientEffect14",
      script.scriptState_04,
      null,
      effect::renderRadialGradientEffect,
      null,
      effect,
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
  public static FlowControl scriptAllocateGuardEffect(final RunningScript<? extends BattleObject> script) {
    final GuardEffect06 effect = new GuardEffect06();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "GuardEffect06",
      script.scriptState_04,
      null,
      effect::renderGuardEffect,
      null,
      effect
    );

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
  public static FlowControl FUN_800d3090(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a monster death effect effect for a monster battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spriteIndex", description = "Which sprite to use")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused in code but passed by scripts")
  @Method(0x800d34bcL)
  public static FlowControl scriptAllocateMonsterDeathEffect(final RunningScript<? extends BattleObject> script) {
    final BattleEntity27c parent = SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class);
    final SpriteMetrics08 sprite = spriteMetrics_800c6948[script.params_20[2].get() & 0xff];

    final MonsterDeathEffect34 deathEffect = new MonsterDeathEffect34(parent, new GenericSpriteEffect24(0x5400_0000, sprite));

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "MonsterDeathEffect34",
      script.scriptState_04,
      deathEffect::monsterDeathEffectTicker,
      deathEffect::monsterDeathEffectRenderer,
      null,
      deathEffect
    );

    //LAB_800d35cc
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /**
   * NOTE: changed param from reference to value
   */
  @Method(0x800d3910L)
  public static int getCharDisplayWidth(final long chr) {
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
  public static int[] setAdditionNameDisplayCoords(final int addition) {
    final String additionName = additionNames_800fa8d4[addition];

    int additionDisplayWidth = 0;
    //LAB_800d39b8
    for(int i = 0; i < additionName.length(); i++) {
      additionDisplayWidth += getCharDisplayWidth(additionName.charAt(i));
    }

    //LAB_800d39ec
    return new int[] {144 - additionDisplayWidth, 64};
  }

  @ScriptDescription("Allocates an addition name display script state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1", description = "Unknown, -1 will deallocate next tick")
  @Method(0x800d3d74L)
  public static FlowControl scriptAllocateAdditionScript(final RunningScript<?> script) {
    if(script.params_20[1].get() == -1) {
      _800faa9d.set(0);
    } else {
      //LAB_800d3dc0
      final AdditionNameTextEffect1c additionStruct = new AdditionNameTextEffect1c();
      final int addition = gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19;
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionNameTextEffect1c", additionStruct);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(additionStruct::tickAdditionNameEffect);
      final String additionName = additionNames_800fa8d4[addition];

      //LAB_800d3e5c
      //LAB_800d3e7c
      additionStruct.addition_02 = addition;
      additionStruct.length_08 = additionName.length();
      additionStruct.positionMovement_0c = 120;
      additionStruct.renderer_14 = additionStruct::renderAdditionNameChar;
      additionStruct.ptr_18 = new AdditionCharEffectData0c[additionStruct.length_08];
      Arrays.setAll(additionStruct.ptr_18, i -> new AdditionCharEffectData0c());
      _800faa9d.set(1);

      final int[] displayOffset = setAdditionNameDisplayCoords(addition);
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
        displayOffsetX += getCharDisplayWidth(additionName.charAt(charIdx));
        charPosition -= 80;
      }
    }

    //LAB_800d3f70
    return FlowControl.CONTINUE;
  }

  @Method(0x800d3f98L)
  public static void FUN_800d3f98(final short x, final short y, final int a2, final short a3, final int brightness) {
    renderButtonPressHudTexturedRect(x, y, a2 * 8 + 16 & 0xf8, 40, 8, 16, a3, Translucency.B_PLUS_F, brightness, 0x1000);
  }

  @ScriptDescription("Allocates an addition SP text display effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800d4338L)
  public static FlowControl scriptAllocateSpTextEffect(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    final int s3 = script.params_20[1].get();

    if(s2 == -1) {
      _800faa94.set(0);
    } else {
      //LAB_800d4388
      final SpTextEffect40 s1 = new SpTextEffect40();
      final ScriptState<SpTextEffect40> state = SCRIPTS.allocateScriptState("SpTextEffect40", s1);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(s1::tickSpTextEffect);

      s1._08 = s2;
      s1._1c = _800faa90.get() << 8;

      if(s3 == 1) {
        _800faa92.set((short)0);
        _800faa94.set(s3);
        s1._01 = 0;
        s1._1c = 0xffff6e00;
        _800faa90.set((short)-0x92);
      } else {
        //LAB_800d4470
        _800faa92.add((short)1);
        s1._01 = _800faa92.get();
      }

      //LAB_800d448c
      s1._2c = (s1._1c - s1._0c) / 14;

      //LAB_800d44dc
      for(int i = 0; i < 8; i++) {
        s1.charArray_3c[i]._00 = s1._0c;
        s1.charArray_3c[i]._04 = s1._10;
      }

      final int strLen = String.valueOf(s1._08).length();

      final int v1;
      if(s1._01 == 0) {
        v1 = strLen + 2;
      } else {
        v1 = strLen + 3;
      }

      //LAB_800d453c
      _800faa90.set((short)((s1._1c >> 8) + v1 * 8 - 3));
    }

    script.params_20[1].set(0);

    //LAB_800d4560
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition name effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800d4580L)
  public static FlowControl scriptAllocateAdditionNameEffect(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    if(s2 != -1) {
      final AdditionNameTextEffect1c s0 = new AdditionNameTextEffect1c();
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionScriptData1c", s0);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(s0::tickAdditionNameEffect);
      s0.ptr_18 = new AdditionCharEffectData0c[] {new AdditionCharEffectData0c()};
      s0.positionMovement_0c = 40;
      s0.renderer_14 = s0::renderAdditionNameEffect;
      s0.length_08 = 1;
      s0._10 = s2;
      final AdditionCharEffectData0c struct = s0.ptr_18[0];
      struct.scrolling_00 = 1;
      struct.dupes_02 = 8;
      struct.position_04 = -160;
      struct.offsetY_06 = 96;
      struct.offsetX_08 = 144 - (String.valueOf(s2).length() + 4) * 8;
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
  public static FlowControl scriptRenderButtonPressHudElement(final RunningScript<?> script) {
    renderButtonPressHudElement1(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), Translucency.of(script.params_20[3].get()), script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  public static void renderButtonPressHudElement1(final int type, final int x, final int y, final Translucency translucency, final int brightness) {
    final ButtonPressHudMetrics06 metrics = buttonPressHudMetrics_800faaa0[type];

    if(metrics.hudElementType_00 == 0) {
      renderButtonPressHudTexturedRect(x, y, metrics.u_01, metrics.v_02, metrics.wOrRightU_03, metrics.hOrBottomV_04, metrics.clutOffset_05, translucency, brightness, 0x1000);
    } else {
      renderButtonPressHudElement(x, y, metrics.u_01, metrics.v_02, metrics.wOrRightU_03, metrics.hOrBottomV_04, metrics.clutOffset_05, translucency, brightness, 0x1000, 0x1000);
    }
  }

  @ScriptDescription("Causes the battle camera projection plane distance to begin moving")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera should move")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "newDistance", description = "The new projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames it should take to change the distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ1")
  @Method(0x800d8decL)
  public static FlowControl scriptMoveCameraProjectionPlane(final RunningScript<?> script) {
    final int mode = script.params_20[0].get();
    final float newProjectionPlaneDistance = script.params_20[1].get();
    final int projectionPlaneChangeFrames = script.params_20[2].get();
    final int stepZ1 = script.params_20[3].get();

    LOGGER.info(CAMERA, "[CAMERA] scriptMoveCameraProjectionPlane mode=%d, new=%f, frames=%d, s4=%d", mode, newProjectionPlaneDistance, projectionPlaneChangeFrames, stepZ1);

    final BattleCamera cam = camera_800c67f0;
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
  public static FlowControl scriptResetCameraMovement(final RunningScript<?> script) {
    camera_800c67f0.resetCameraMovement();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800dac20L)
  public static FlowControl FUN_800dac20(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800dac70(script.params_20[0].get(), x, y, z, SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class));
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
  public static FlowControl FUN_800db034(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800db084(script.params_20[0].get(), x, y, z, SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class));
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
  public static FlowControl FUN_800db460(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800db4ec(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
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
  public static FlowControl FUN_800db574(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800db600(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "finalStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db688L)
  public static FlowControl FUN_800db688(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800db714(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "finalStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db79cL)
  public static FlowControl FUN_800db79c(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800db828(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
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
  public static FlowControl FUN_800db8b0(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800db950(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get() / (float)0x100, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
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
  public static FlowControl FUN_800db9e0(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    camera_800c67f0.FUN_800dba80(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get() / (float)0x100, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Whether or not the camera is currently moving")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint moving, 1 = refpoint moving")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "isMoving")
  @Method(0x800dbb10L)
  public static FlowControl scriptIsCameraMoving(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;
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
  public static FlowControl FUN_800dbb9c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Sets the viewport twist")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "twist", description = "The new viewpoint twist")
  @Method(0x800dbc2cL)
  public static FlowControl scriptSetViewportTwist(final RunningScript<?> script) {
    camera_800c67f0.rview2_00.viewpointTwist_18 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint, 1 = refpoint")
  @Method(0x800dbc80L)
  public static FlowControl FUN_800dbc80(final RunningScript<?> script) {
    final int type = script.params_20[0].get();

    if((type & UPDATE_VIEWPOINT) != 0) {
      camera_800c67f0.stepZAcceleration_e4 = 0.0f;
      camera_800c67f0.stepZAcceleration_b4 = 0.0f;
    }

    //LAB_800dbca8
    if((type & UPDATE_REFPOINT) != 0) {
      camera_800c67f0.stepZAcceleration_70 = 0.0f;
      camera_800c67f0.stepZAcceleration_40 = 0.0f;
    }

    //LAB_800dbcc0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets camera projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distance", description = "The projection plane distance")
  @Method(0x800dbcc8L)
  public static FlowControl scriptSetCameraProjectionPlaneDistance(final RunningScript<?> script) {
    LOGGER.info(CAMERA, "[CAMERA] scriptSetCameraProjectionPlaneDistance distance=%d", script.params_20[0].get());

    final BattleCamera cam = camera_800c67f0;
    cam.projectionPlaneDistance_100 = script.params_20[0].get();
    cam.projectionPlaneChangeFrames_108 = 0;
    cam.projectionPlaneChanging_118 = true;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the camera projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "distance", description = "The projection plane distance")
  @Method(0x800dbcfcL)
  public static FlowControl scriptGetProjectionPlaneDistance(final RunningScript<?> script) {
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
  public static FlowControl scriptCalculateCameraValue(final RunningScript<?> script) {
    LOGGER.info(CAMERA, "[CAMERA] Calc val: use refpoint=%b, FUN index=%d, component=%d, script index=%d", script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());

    float value = camera_800c67f0.calculateCameraValue(script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), SCRIPTS.getObject(script.params_20[3].get(), BattleObject.class));

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
  public static FlowControl scriptStopCameraMovement(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;
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
  public static FlowControl scriptWobbleCamera(final RunningScript<?> script) {
    camera_800c67f0.setWobble(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    getScreenOffset(screenOffsetX_800c67bc, screenOffsetY_800c67c0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dd15cL)
  public static MV lerp(final MV a0, final MV a1, final float ratio) {
    if(ratio > 0) {
      final float v1 = 1.0f - ratio;
      a0.lerp(a1, ratio);
      a0.transfer.x = a0.transfer.x * v1 + a1.transfer.x * ratio;
      a0.transfer.y = a0.transfer.y * v1 + a1.transfer.y * ratio;
      a0.transfer.z = a0.transfer.z * v1 + a1.transfer.z * ratio;
    }

    //LAB_800dd4b8
    //LAB_800dd4bc
    return a0;
  }

  @Method(0x800dd4ccL)
  public static int applyStandardAnimation(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return 2;
    }

    //LAB_800dd4fc
    final int totalFrames;
    final int frame;
    if(model.disableInterpolation_a2) {
      frame = animationTicks % (model.totalFrames_9a / 2);
      model.partTransforms_94 = Arrays.copyOfRange(model.partTransforms_90, frame, model.partTransforms_90.length);
      applyModelPartTransforms(model);
      totalFrames = (short)model.totalFrames_9a >> 1;
    } else {
      //LAB_800dd568
      frame = animationTicks % model.totalFrames_9a;
      model.partTransforms_94 = Arrays.copyOfRange(model.partTransforms_90, frame / 2, model.partTransforms_90.length);
      applyModelPartTransforms(model);

      if((frame & 0x1) != 0 && frame != model.totalFrames_9a - 1 && model.ub_a3 == 0) { // Interpolation frame
        final ModelPartTransforms0c[][] original = model.partTransforms_94;
        applyInterpolationFrame(model);
        model.partTransforms_94 = original;
      }

      //LAB_800dd5ec
      totalFrames = model.totalFrames_9a;
    }

    //LAB_800dd5f0
    model.remainingFrames_9e = totalFrames - frame - 1;
    model.interpolationFrameIndex = 0;

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800dd618
      model.animationState_9c = 1;
    }

    //LAB_800dd61c
    //LAB_800dd620
    return model.remainingFrames_9e;
  }

  @Method(0x800dd638L)
  public static int applyLmbAnimation(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return 2;
    }

    //LAB_800dd680
    final int count = Math.min(model.modelParts_00.length, model.partCount_98);

    //LAB_800dd69c
    final LmbType0 lmb = (LmbType0)model.lmbAnim_08.lmb_00;

    final int a0_0;
    final int frame;
    final int remainingFrames;
    final int isInterpolationFrame;
    if(model.disableInterpolation_a2) {
      isInterpolationFrame = 0;
      a0_0 = (animationTicks << 1) % model.totalFrames_9a >>> 1;
      remainingFrames = (model.totalFrames_9a >> 1) - a0_0;
    } else {
      //LAB_800dd6dc
      frame = animationTicks % model.totalFrames_9a;
      isInterpolationFrame = (animationTicks & 0x1) << 11; // Dunno why this is shifted, makes no difference
      a0_0 = frame >>> 1;
      remainingFrames = model.totalFrames_9a - frame;
    }

    //LAB_800dd700
    model.remainingFrames_9e = remainingFrames - 1;
    model.interpolationFrameIndex = 0;

    //LAB_800dd720
    for(int i = 0; i < count; i++) {
      final LmbTransforms14 transforms = lmb.partAnimations_08[i].keyframes_08[a0_0];
      final MV matrix = model.modelParts_00[i].coord2_04.coord;

      final Vector3f trans = new Vector3f();
      trans.set(transforms.trans_06);

      if(isInterpolationFrame != 0) { // Interpolation frame
        final LmbTransforms14 nextFrame;
        if(animationTicks == model.totalFrames_9a - 1) {
          nextFrame = lmb.partAnimations_08[i].keyframes_08[0]; // Wrap around to frame 0
        } else {
          //LAB_800dd7cc
          nextFrame = lmb.partAnimations_08[i].keyframes_08[a0_0 + 1];
        }

        //LAB_800dd7d0
        trans.set(
          (trans.x + nextFrame.trans_06.x) / 2,
          (trans.y + nextFrame.trans_06.y) / 2,
          (trans.z + nextFrame.trans_06.z) / 2
        );
      }

      //LAB_800dd818
      matrix.rotationZYX(transforms.rot_0c);
      matrix.transfer.set(trans);
      matrix.scaleLocal(transforms.scale_00);
    }

    //LAB_800dd84c
    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800dd864
      model.animationState_9c = 1;
    }

    //LAB_800dd868
    //LAB_800dd86c
    return model.remainingFrames_9e;
  }

  /**
   * used renderCtmd
   */
  @Method(0x800dd89cL)
  public static void FUN_800dd89c(final Model124 model, final int newAttribute) {
    zOffset_1f8003e8 = model.zOffset_a0;
    tmdGp0Tpage_1f8003ec = model.tpage_108;

    final MV lw = new MV();
    final MV ls = new MV();

    //LAB_800dd928
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 part = model.modelParts_00[i];

      //LAB_800dd940
      if((model.partInvisible_f4 & 1L << i) == 0) {
        GsGetLws(part.coord2_04, lw, ls);

        //LAB_800dd9bc
        if((newAttribute & 0x8) != 0) {
          //TODO pretty sure this is not equivalent to MATRIX#normalize
          lw.normal();
        }

        //LAB_800dd9d8
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);

        final int oldAttrib = part.attribute_00;
        part.attribute_00 = newAttribute;

        final int oldZShift = zShift_1f8003c4;
        final int oldZMax = zMax_1f8003cc;
        final int oldZMin = zMin;
        zShift_1f8003c4 = 2;
        zMax_1f8003cc = 0xffe;
        zMin = 0xb;
        Renderer.renderDobj2(part, false, 0x20);
        zShift_1f8003c4 = oldZShift;
        zMax_1f8003cc = oldZMax;
        zMin = oldZMin;

        RENDERER.queueModel(part.obj, lw)
          .lightDirection(lightDirectionMatrix_800c34e8)
          .lightColour(lightColourMatrix_800c3508)
          .backgroundColour(GTE.backgroundColour);

        part.attribute_00 = oldAttrib;
      }
    }

    //LAB_800dda54
    //LAB_800dda58
    for(int i = 0; i < 7; i++) {
      if(model.animateTextures_ec[i]) {
        animateModelTextures(model, i);
      }

      //LAB_800dda70
    }

    if(model.shadowType_cc != 0) {
      renderBttlShadow(model);
    }

    //LAB_800dda98
  }

  @Method(0x800ddac8L)
  public static void loadModelTmd(final Model124 model, final CContainer extTmd) {
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

    if((tmd.header.flags & 0x2) == 0 && model.vramSlot_9d != 0) {
      adjustModelUvs(model);
    }

    //LAB_800ddce8
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowType_cc = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();

    for(int i = 0; i < model.modelParts_00.length; i++) {
      model.modelParts_00[i].obj = TmdObjLoader.fromObjTable("BattleModel (part" + i + ')', tmd.objTable[i]);
    }
  }

  @Method(0x800ddd3cL)
  public static int applyCmbAnimation(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return 2;
    }

    //LAB_800ddd9c
    final Model124.CmbAnim cmbAnim = model.cmbAnim_08;

    if(animationTicks == cmbAnim.animationTicks_00) {
      return model.animationState_9c;
    }

    final Cmb cmb = cmbAnim.cmb_04;

    // Note: these two variables _should_ be the same
    final int modelPartCount = cmb.modelPartCount_0c;
    final int count = Math.min(model.modelParts_00.length, model.partCount_98);

    //LAB_800dddc4
    int frameIndex;
    final int a1_0;
    final int isInterpolationFrame;
    if(model.disableInterpolation_a2) {
      isInterpolationFrame = 0;
      a1_0 = (animationTicks << 1) % model.totalFrames_9a >>> 1;
      frameIndex = (cmbAnim.animationTicks_00 << 1) % model.totalFrames_9a >> 1;
      model.remainingFrames_9e = (model.totalFrames_9a >> 1) - a1_0 - 1;
    } else {
      //LAB_800dde1c
      // This modulo has to be unsigned due to a bug causing the number of ticks
      // to go negative. This matches the retail behaviour (it uses divu).
      final int frame = (int)((animationTicks & 0xffff_ffffL) % model.totalFrames_9a);
      isInterpolationFrame = (animationTicks & 0x1) << 11; // Dunno why this is shifted, makes no difference
      a1_0 = frame >>> 1;
      frameIndex = cmbAnim.animationTicks_00 % model.totalFrames_9a >> 1;
      model.remainingFrames_9e = model.totalFrames_9a - frame - 1;

      // This is another retail bug - it's possible for the frame index to go negative
      if(frameIndex < 0) {
        frameIndex = 0;
      }
    }

    model.interpolationFrameIndex = 0;

    //LAB_800dde60
    if(frameIndex > a1_0) {
      //LAB_800dde88
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final ModelPartTransforms0c fileTransforms = cmb.partTransforms_10[0][partIndex];
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rotate_00.set(fileTransforms.rotate_00);
        modelTransforms.translate_06.set(fileTransforms.translate_06);
      }

      //LAB_800ddee0
      cmbAnim.animationTicks_00 = 0;
      frameIndex = 0;
    }

    //LAB_800ddeec
    //LAB_800ddf1c
    for(; frameIndex < a1_0; frameIndex++) {
      //LAB_800ddf2c
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final Cmb.SubTransforms08 subTransforms = cmb.subTransforms[frameIndex][partIndex];
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rotate_00.add(subTransforms.rot_01);
        modelTransforms.translate_06.add(subTransforms.trans_05);
      }

      //LAB_800ddfd4
    }

    //LAB_800ddfe4
    //LAB_800de158
    if(isInterpolationFrame != 0 && model.ub_a3 == 0 && a1_0 != (model.totalFrames_9a >> 1) - 1) { // Interpolation frame
      //LAB_800de050
      for(int i = 0; i < count; i++) {
        final Cmb.SubTransforms08 subTransforms = cmb.subTransforms[a1_0][i];
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[i];

        final MV modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        modelPartMatrix.rotationZYX(modelTransforms.rotate_00);
        modelPartMatrix.transfer.set(modelTransforms.translate_06);

        final Vector3f rotation = new Vector3f();
        rotation.set(modelTransforms.rotate_00).add(subTransforms.rot_01);

        final MV translation = new MV();
        translation.rotationZYX(rotation);
        translation.transfer.set(modelTransforms.translate_06).add(subTransforms.trans_05);

        lerp(modelPartMatrix, translation, 0.5f);
      }
    } else {
      //LAB_800de164
      for(int i = 0; i < count; i++) {
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[i];
        final MV modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        modelPartMatrix.rotationZYX(modelTransforms.rotate_00);
        modelPartMatrix.transfer.set(modelTransforms.translate_06);
      }
    }

    //LAB_800de1b4
    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800de1cc
      model.animationState_9c = 1;
    }

    //LAB_800de1d0
    cmbAnim.animationTicks_00 = animationTicks;

    //LAB_800de1e0
    return model.remainingFrames_9e;
  }

  @Method(0x800de210L)
  public static void loadModelCmb(final Model124 model, final Cmb cmb) {
    final Model124.CmbAnim anim = model.cmbAnim_08;
    final int count = cmb.modelPartCount_0c;

    anim.cmb_04 = cmb;
    anim.transforms_08 = new ModelPartTransforms0c[count];

    Arrays.setAll(anim.transforms_08, i -> new ModelPartTransforms0c());

    model.animType_90 = 2;
    model.lmbUnknown_94 = 0;
    model.partCount_98 = count;
    model.totalFrames_9a = cmb.totalFrames_0e * 2;
    model.animationState_9c = 1;
    model.remainingFrames_9e = cmb.totalFrames_0e * 2;
    model.interpolationFrameIndex = 0;

    //LAB_800de270
    for(int i = 0; i < count; i++) {
      final ModelPartTransforms0c v1 = cmb.partTransforms_10[0][i];
      final ModelPartTransforms0c a1_0 = anim.transforms_08[i];
      a1_0.rotate_00.set(v1.rotate_00);
      a1_0.translate_06.set(v1.translate_06);
    }

    //LAB_800de2c8
    anim.animationTicks_00 = 1;
    applyCmbAnimation(model, 0);
  }

  @Method(0x800de2e8L)
  public static void applyAnimation(final Model124 model, final int animationTicks) {
    final int type = model.animType_90;
    if(type == 1) {
      //LAB_800de318
      applyLmbAnimation(model, animationTicks);
    } else if(type == 0 || type == 2) {
      //LAB_800de328
      applyCmbAnimation(model, animationTicks);
    } else {
      //LAB_800de338
      applyStandardAnimation(model, animationTicks);
    }

    //LAB_800de340
  }

  @Method(0x800de36cL)
  public static void loadModelAnim(final Model124 model, final Anim anim) {
    if(anim.magic_00 == Cmb.MAGIC) { // "CMB "
      loadModelCmb(model, (Cmb)anim);
      //LAB_800de398
    } else if(anim.magic_00 == Lmb.MAGIC) { // "LMB"
      final LmbType0 lmb = (LmbType0)anim;

      model.lmbAnim_08.lmb_00 = lmb;
      model.animType_90 = 1;
      model.lmbUnknown_94 = 0;
      model.partCount_98 = lmb.objectCount_04;
      model.totalFrames_9a = lmb.partAnimations_08[0].count_04 * 2;
      model.animationState_9c = 1;
      model.remainingFrames_9e = lmb.partAnimations_08[0].count_04 * 2;
      model.interpolationFrameIndex = 0;
    } else {
      //LAB_800de3dc
      loadModelStandardAnimation(model, (TmdAnimationFile)anim);
    }

    //LAB_800de3e4
  }

  /** used renderCtmd */
  @Method(0x800de3f4L)
  public static void renderTmdSpriteEffect(final TmdObjTable1c objTable, final Obj obj, final EffectManagerParams<?> effectParams, final MV transforms) {
    final MV sp0x10 = new MV();
    if((effectParams.flags_00 & 0x8) != 0) {
      //TODO pretty sure this isn't equivalent to MATRIX#normalize
      transforms.normal(sp0x10);
      GsSetLightMatrix(sp0x10);
    } else {
      //LAB_800de458
      GsSetLightMatrix(transforms);
    }

    //LAB_800de45c
    if(RenderEngine.legacyMode != 0) {
      transforms.compose(worldToScreenMatrix_800c3548, sp0x10);
    } else {
      sp0x10.set(transforms);
    }

    if((effectParams.flags_00 & 0x400_0000) == 0) {
      sp0x10.rotationXYZ(effectParams.rot_10);
      sp0x10.scaleLocal(effectParams.scale_16);
    }

    //LAB_800de4a8
    //LAB_800de50c
    GTE.setTransforms(sp0x10);

    final ModelPart10 dobj2 = new ModelPart10();
    dobj2.attribute_00 = effectParams.flags_00;
    dobj2.tmd_08 = objTable;

    final int oldZShift = zShift_1f8003c4;
    final int oldZMax = zMax_1f8003cc;
    final int oldZMin = zMin;
    zShift_1f8003c4 = 2;
    zMax_1f8003cc = 0xffe;
    zMin = 0xb;
    Renderer.renderDobj2(dobj2, false, 0x20);
    zShift_1f8003c4 = oldZShift;
    zMax_1f8003cc = oldZMax;
    zMin = oldZMin;

    RENDERER.queueModel(obj, sp0x10)
      .lightDirection(lightDirectionMatrix_800c34e8)
      .lightColour(lightColourMatrix_800c3508)
      .backgroundColour(GTE.backgroundColour);

    //LAB_800de528
  }

  @Method(0x800de544L)
  public static Vector3f getRotationFromTransforms(final Vector3f rotOut, final MV transforms) {
    final MV mat = new MV(transforms);
    rotOut.x = MathHelper.atan2(-mat.m21, mat.m22);
    mat.rotateLocalX(-rotOut.x);
    rotOut.y = MathHelper.atan2(mat.m20, mat.m22);
    mat.rotateLocalY(-rotOut.y);
    rotOut.z = MathHelper.atan2(mat.m01, mat.m00);
    return rotOut;
  }

  @Method(0x800de618L)
  public static void getRotationAndScaleFromTransforms(final Vector3f rotOut, final Vector3f scaleOut, final MV transforms) {
    final MV mat = new MV().set(transforms);
    rotOut.x = MathHelper.atan2(-mat.m21, mat.m22);
    mat.rotateLocalX(-rotOut.x);
    rotOut.y = MathHelper.atan2(mat.m20, mat.m22);
    mat.rotateLocalY(-rotOut.y);
    rotOut.z = MathHelper.atan2(mat.m01, mat.m00);
    mat.rotateLocalZ(-rotOut.z);
    scaleOut.set(mat.m00, mat.m11, mat.m22);
  }

  @Method(0x800de76cL)
  public static TmdObjTable1c optimisePacketsIfNecessary(final TmdWithId tmd, final int objIndex) {
    if((tmd.tmd.header.flags & 0x2) == 0) {
      final ModelPart10 dobj2 = new ModelPart10();
      dobj2.tmd_08 = tmd.tmd.objTable[objIndex];
      return dobj2.tmd_08;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return tmd.tmd.objTable[objIndex];
  }

  @Method(0x800e45c0L)
  public static void FUN_800e45c0(final Vector3f out, final Vector3f in) {
    final float angle = MathHelper.atan2(in.x, in.z);
    final float sin = MathHelper.sin(-angle);
    final float cos = MathHelper.cosFromSin(sin, -angle);
    out.x = MathHelper.atan2(-in.y, cos * in.z - sin * in.x); // Angle from the XZ plane
    out.y = angle; // Angle from the X axis
    out.z = 0;
  }

  @Method(0x800e4674L)
  public static Vector3f FUN_800e4674(final Vector3f out, final Vector3f rotation) {
    return out.set(0.0f, 0.0f, 1.0f).mul(new Matrix3f().rotationZYX(rotation.z, rotation.y, rotation.z));
  }

  @Method(0x800e46c8L)
  public static void resetLights() {
    final BattleLightStruct64 v1 = _800c6930;
    v1.colour_00.set(0x800, 0x800, 0x800);

    final BttlLightStruct84 a0 = lights_800c692c[0];
    a0.light_00.direction_00.set(0.0f, 1.0f, 0.0f);
    a0.light_00.r_0c = 0.5f;
    a0.light_00.g_0d = 0.5f;
    a0.light_00.b_0e = 0.5f;
    a0._10._00 = 0;
    a0._4c._00 = 0;

    //LAB_800e4720
    lights_800c692c[1].clear();
    lights_800c692c[2].clear();
  }

  @ScriptDescription("Resets battle lighting")
  @Method(0x800e473cL)
  public static FlowControl scriptResetLights(final RunningScript<?> script) {
    resetLights();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e475cL)
  public static void setLightDirection(final int lightIndex, final float x, final float y, final float z) {
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.direction_00.set(x, y, z);
    light._10._00 = 0;
  }

  @ScriptDescription("Sets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4788L)
  public static FlowControl scriptSetLightDirection(final RunningScript<?> script) {
    setLightDirection(script.params_20[0].get(), script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e47c8L)
  public static FlowControl scriptGetLightDirection(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set((int)(light.light_00.direction_00.x * 0x1000));
    script.params_20[2].set((int)(light.light_00.direction_00.y * 0x1000));
    script.params_20[3].set((int)(light.light_00.direction_00.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4824L)
  public static void FUN_800e4824(final int lightIndex, final float x, final float y, final float z) {
    final Vector3f rotation = new Vector3f();
    FUN_800e4674(rotation, new Vector3f(x, y, z));
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.direction_00.set(rotation);
    light._10._00 = 0;
  }

  @ScriptDescription("Unknown, sets the battle light direction using the vector (0, 0, 1) rotated by ZYX")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e48a8L)
  public static FlowControl FUN_800e48a8(final RunningScript<?> script) {
    FUN_800e4824(script.params_20[0].get(), MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets the battle light direction using the vector (0, 0, 1) rotated by ZYX")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e48e8L)
  public static FlowControl FUN_800e48e8(final RunningScript<?> script) {
    final Vector3f rotation = new Vector3f();
    FUN_800e45c0(rotation, lights_800c692c[script.params_20[0].get()].light_00.direction_00);
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
  public static FlowControl FUN_800e4964(final RunningScript<?> script) {
    final Vector3f rotation = new Vector3f();

    final int index = script.params_20[1].get();
    if(index != -1) {
      //LAB_800e49c0
      if(index > 0 && index - 1 < 3) {
        FUN_800e45c0(rotation, lights_800c692c[index - 1].light_00.direction_00);
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
    FUN_800e4674(direction, rotation);
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    light.light_00.direction_00.set(direction);
    light._10._00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, gets the battle light direction")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT, name = "indexAndX", description = "In: either a light index or battle entity index, out: the X direction (PSX degrees)") // why
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y direction (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z direction (PSX degrees)")
  @Method(0x800e4abcL)
  public static FlowControl FUN_800e4abc(final RunningScript<?> script) {
    final int s1 = script.params_20[1].get();

    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, lights_800c692c[script.params_20[0].get()].light_00.direction_00);

    final Vector3f s0;
    if(s1 - 1 < 3) {
      s0 = new Vector3f();
      FUN_800e45c0(s0, lights_800c692c[s1 - 1].light_00.direction_00);
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
  public static void setBattleLightColour(final int lightIndex, final float r, final float g, final float b) {
    final BttlLightStruct84 light = lights_800c692c[lightIndex];
    light.light_00.r_0c = r;
    light.light_00.g_0d = g;
    light.light_00.b_0e = b;
    light._4c._00 = 0;
  }

  @ScriptDescription("Sets the battle light colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (8-bit fixed-point)")
  @Method(0x800e4c10L)
  public static FlowControl scriptSetBattleLightColour(final RunningScript<?> script) {
    setBattleLightColour(script.params_20[0].get(), script.params_20[1].get() / (float)0x100, script.params_20[2].get() / (float)0x100, script.params_20[3].get() / (float)0x100);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle light colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The red channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The green channel (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (8-bit fixed-point)")
  @Method(0x800e4c90L)
  public static FlowControl scriptGetBattleLightColour(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    script.params_20[1].set((int)(light.light_00.r_0c * 0x100));
    script.params_20[2].set((int)(light.light_00.g_0d * 0x100));
    script.params_20[3].set((int)(light.light_00.b_0e * 0x100));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4cf8L)
  public static void setBattleBackgroundLight(final float r, final float g, final float b) {
    final BattleLightStruct64 v0 = _800c6930;
    v0.colour_00.set(r, g, b);
    v0._24 = 0;
    GTE.setBackgroundColour(r, g, b);
  }

  @ScriptDescription("Sets the battle light background colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (12-bit fixed-point)")
  @Method(0x800e4d2cL)
  public static FlowControl scriptSetBattleBackgroundLightColour(final RunningScript<?> script) {
    setBattleBackgroundLight(script.params_20[0].get() / 4096.0f, script.params_20[1].get() / 4096.0f, script.params_20[2].get() / 4096.0f);
    _800c6930._24 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e4d74L)
  public static void getBattleBackgroundLightColour(final Vector3f colour) {
    final BattleLightStruct64 light = _800c6930;
    colour.set(light.colour_00);
  }

  @ScriptDescription("Sets the battle light background colour")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The red channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The green channel (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The blue channel (12-bit fixed-point)")
  @Method(0x800e4db4L)
  public static FlowControl scriptGetBattleBackgroundLightColour(final RunningScript<?> script) {
    final BattleLightStruct64 v0 = _800c6930;
    script.params_20[0].set((int)(v0.colour_00.x * 0x1000));
    script.params_20[1].set((int)(v0.colour_00.y * 0x1000));
    script.params_20[2].set((int)(v0.colour_00.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e4dfcL)
  public static FlowControl FUN_800e4dfc(final RunningScript<?> script) {
    lights_800c692c[script.params_20[0].get()]._10._00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e4e2cL)
  public static FlowControl FUN_800e4e2c(final RunningScript<?> script) {
    return lights_800c692c[script.params_20[0].get()]._10._00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out")
  @Method(0x800e4e64L)
  public static FlowControl FUN_800e4e64(final RunningScript<?> script) {
    script.params_20[1].set(lights_800c692c[script.params_20[0].get()]._10._00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, must change battle light over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks for the change")
  @Method(0x800e4ea0L)
  public static FlowControl FUN_800e4ea0(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final int ticks = script.params_20[4].get();
    final BttlLightStruct84Sub38 t0 = light._10;

    t0._00 = 0;
    t0.angleAndColour_04.set(light.light_00.direction_00);
    t0.vec_28.set(MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    t0.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      t0.vec_10.x = (t0.vec_28.x - t0.angleAndColour_04.x) / ticks;
      t0.vec_10.y = (t0.vec_28.y - t0.angleAndColour_04.y) / ticks;
      t0.vec_10.z = (t0.vec_28.z - t0.angleAndColour_04.z) / ticks;
      t0.vec_1c.zero();
      t0._00 = 0xa001;
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
  public static FlowControl FUN_800e4fa0(final RunningScript<?> script) {
    final float x = MathHelper.psxDegToRad(script.params_20[1].get());
    final float y = MathHelper.psxDegToRad(script.params_20[2].get());
    final float z = MathHelper.psxDegToRad(script.params_20[3].get());
    final int ticks = script.params_20[4].get();

    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);
    light._10._00 = 0;

    final BttlLightStruct84Sub38 a3 = light._10;
    a3.angleAndColour_04.set(sp0x10);
    a3.vec_28.set(x, y, z);
    a3.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      a3.vec_1c.zero();
      a3.vec_10.x = (x - a3.angleAndColour_04.x) / ticks;
      a3.vec_10.y = (y - a3.angleAndColour_04.y) / ticks;
      a3.vec_10.z = (z - a3.angleAndColour_04.z) / ticks;
      a3._00 = 0xc001;
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
  public static FlowControl FUN_800e50e8(final RunningScript<?> script) {
    final int lightIndex = script.params_20[0].get();
    final int lightOrBentIndex = script.params_20[1].get();
    final int x = script.params_20[2].get();
    final int y = script.params_20[3].get();
    final int z = script.params_20[4].get();
    final int ticks = script.params_20[5].get();

    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, lights_800c692c[lightIndex].light_00.direction_00);

    final BttlLightStruct84Sub38 s0 = lights_800c692c[lightIndex]._10;
    s0._00 = 0;
    s0.angleAndColour_04.set(sp0x10);

    if(lightOrBentIndex > 0 && lightOrBentIndex < 4) {
      final Vector3f sp0x18 = new Vector3f();
      FUN_800e45c0(sp0x18, lights_800c692c[lightOrBentIndex - 1].light_00.direction_00);
      s0.vec_28.set(sp0x18);
    } else {
      //LAB_800e51e8
      final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[lightOrBentIndex].innerStruct_00;
      s0.vec_28.set(bent.model_148.coord2_14.transforms.rotate);
    }

    //LAB_800e522c
    s0.ticksRemaining_34 = ticks;
    s0.vec_28.add(x, y, z);

    if(ticks > 0) {
      s0._00 = 0xc001;
      s0.vec_10.set(s0.vec_28).sub(s0.angleAndColour_04).div(ticks);
      s0.vec_1c.zero();
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
  public static FlowControl FUN_800e52f8(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 v1 = light._10;
    v1._00 = 0x4001;
    v1.angleAndColour_04.set(sp0x10);
    v1.vec_10.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    v1.vec_1c.set(script.params_20[4].get() / (float)0x1000, script.params_20[5].get() / (float)0x1000, script.params_20[6].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c's script index")
  @Method(0x800e540cL)
  public static FlowControl FUN_800e540c(final RunningScript<?> script) {
    final int bentIndex = script.params_20[1].get();
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];

    final Vector3f sp0x10 = new Vector3f();
    FUN_800e45c0(sp0x10, light.light_00.direction_00);

    final BttlLightStruct84Sub38 a0_0 = light._10;
    a0_0._00 = 0x4002;
    light.scriptIndex_48 = bentIndex;

    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;
    a0_0.angleAndColour_04.set(sp0x10).sub(bent.model_148.coord2_14.transforms.rotate);
    a0_0.vec_10.zero();
    a0_0.vec_1c.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e54f8L)
  public static FlowControl FUN_800e54f8(final RunningScript<?> script) {
    lights_800c692c[script.params_20[0].get()]._4c._00 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @Method(0x800e5528L)
  public static FlowControl FUN_800e5528(final RunningScript<?> script) {
    return lights_800c692c[script.params_20[0].get()]._4c._00 != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value")
  @Method(0x800e5560L)
  public static FlowControl FUN_800e5560(final RunningScript<?> script) {
    script.params_20[1].set(lights_800c692c[script.params_20[0].get()]._4c._00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to battle lights")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lightIndex", description = "The light index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "The number of ticks")
  @Method(0x800e559cL)
  public static FlowControl FUN_800e559c(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final int ticks = script.params_20[4].get();

    final BttlLightStruct84Sub38 t0 = light._4c;
    t0._00 = 0;
    t0.angleAndColour_04.x = light.light_00.r_0c;
    t0.angleAndColour_04.y = light.light_00.g_0d;
    t0.angleAndColour_04.z = light.light_00.b_0e;
    t0.vec_28.set(script.params_20[1].get() / (float)0x80, script.params_20[2].get() / (float)0x80, script.params_20[3].get() / (float)0x80);
    t0.ticksRemaining_34 = ticks;

    if(ticks > 0) {
      t0.vec_1c.zero();
      t0.vec_10.set(t0.vec_28).sub(t0.angleAndColour_04).div(ticks);
      t0._00 = 0x8001;
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
  public static FlowControl FUN_800e569c(final RunningScript<?> script) {
    final BttlLightStruct84 light = lights_800c692c[script.params_20[0].get()];
    final BttlLightStruct84Sub38 v1 = light._4c;
    v1._00 = 0;
    v1.angleAndColour_04.set(light.light_00.r_0c, light.light_00.g_0d, light.light_00.b_0e);
    v1.vec_10.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    v1.vec_1c.set(script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());

    if(v1.ticksRemaining_34 > 0) {
      v1._00 = 0x1;
    }

    //LAB_800e5760
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5768L)
  public static void applyStageAmbiance(final StageAmbiance4c ambiance) {
    setBattleBackgroundLight(ambiance.ambientColour_00.x, ambiance.ambientColour_00.y, ambiance.ambientColour_00.z);

    final BattleLightStruct64 v1 = _800c6930;
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
      final BttlLightStruct84 a1 = lights_800c692c[i];
      final BattleStruct14 a0 = ambiance._10[i];
      a1.light_00.direction_00.set(a0.lightDirection_00);
      a1.light_00.r_0c = a0.lightColour_0a.x / (float)0x100;
      a1.light_00.g_0d = a0.lightColour_0a.y / (float)0x100;
      a1.light_00.b_0e = a0.lightColour_0a.z / (float)0x100;

      if(a0.x_06 != 0 || a0.y_08 != 0) {
        a1._10._00 = 0x3;
        a1._10.angleAndColour_04.set(a1.light_00.direction_00);
        a1._10.vec_10.set(a0.x_06, a0.y_08, 0);
      } else {
        //LAB_800e58cc
        a1._10._00 = 0;
      }

      //LAB_800e58d0
      if(a0.y_12 != 0) {
        a1._4c._00 = 0x3;
        a1._4c.angleAndColour_04.set(a1.light_00.r_0c, a1.light_00.g_0d, a1.light_00.b_0e);
        a1._4c.vec_10.set(a0._0d.x / (float)0x100, a0._0d.y / (float)0x100, a0._0d.z / (float)0x100);
        a1._4c.vec_28.x = a0.x_10;
        a1._4c.vec_28.y = a0.y_12;
      } else {
        //LAB_800e5944
        a1._4c._00 = 0;
      }

      //LAB_800e5948
    }
  }

  @ScriptDescription("Applies the current battle stage's ambiance (including Dragoon Space)")
  @Method(0x800e596cL)
  public static FlowControl scriptApplyStageAmbiance(final RunningScript<?> script) {
    final int v0 = currentStage_800c66a4.get() - 0x47;

    if(v0 >= 0 && v0 < 0x8) {
      applyStageAmbiance(deffManager_800c693c.dragoonSpaceAmbiance_98[v0]);
    } else {
      //LAB_800e59b0
      applyStageAmbiance(deffManager_800c693c.stageAmbiance_4c);
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets or applies stage ambiance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "-1, -2, -3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "Int or int array")
  @Method(0x800e59d8L)
  public static FlowControl FUN_800e59d8(final RunningScript<?> script) {
    final int a0 = script.params_20[0].get();

    if(a0 == -1) {
      deffManager_800c693c.stageAmbiance_4c.set(script.params_20[1]);
    } else if(a0 == -2) {
      //LAB_800e5a38
      //LAB_800e5a60
      applyStageAmbiance(new StageAmbiance4c().set(script.params_20[1]));
      //LAB_800e5a14
    } else if(a0 == -3) {
      //LAB_800e5a40
      applyStageAmbiance(deffManager_800c693c.dragoonSpaceAmbiance_98[script.params_20[1].get()]);
    }

    //LAB_800e5a68
    return FlowControl.CONTINUE;
  }

  @Method(0x800e5a78L)
  public static void tickLighting(final ScriptState<Void> state, final Void struct) {
    final BattleLightStruct64 light1 = _800c6930;

    lightTicks_800c6928.incr();

    if(light1._24 == 3) { // Dragoon space lighting is handled here, I think this is for flickering light
      final int angle = rcos(((lightTicks_800c6928.get() + light1._2c) % light1._2e << 12) / light1._2e);
      final float minAngle = (0x1000 - angle) / (float)0x1000;
      final float maxAngle = (0x1000 + angle) / (float)0x1000;
      light1.colour_00.x = (light1.colour1_0c.x * maxAngle + light1.colour2_18.x * minAngle) / 2.0f;
      light1.colour_00.y = (light1.colour1_0c.y * maxAngle + light1.colour2_18.y * minAngle) / 2.0f;
      light1.colour_00.z = (light1.colour1_0c.z * maxAngle + light1.colour2_18.z * minAngle) / 2.0f;
    }

    //LAB_800e5b98
    //LAB_800e5ba0
    for(int i = 0; i < 3; i++) {
      final BttlLightStruct84 light = lights_800c692c[i];
      final BttlLightStruct84Sub38 a2 = light._10;

      int v1 = a2._00 & 0xff;
      if(v1 == 1) {
        //LAB_800e5c50
        a2.vec_10.add(a2.vec_1c);
        a2.angleAndColour_04.add(a2.vec_10);

        if((a2._00 & 0x8000) != 0) {
          a2.ticksRemaining_34--;

          if(a2.ticksRemaining_34 <= 0) {
            a2._00 = 0;
            a2.angleAndColour_04.set(a2.vec_28);
          }
        }

        //LAB_800e5cf4
        if((a2._00 & 0x2000) != 0) {
          light.light_00.direction_00.set(a2.angleAndColour_04);
          //LAB_800e5d40
        } else if((a2._00 & 0x4000) != 0) {
          final Vector3f sp0x18 = new Vector3f();
          sp0x18.set(a2.angleAndColour_04);
          FUN_800e4674(light.light_00.direction_00, sp0x18);
        }
      } else if(v1 == 2) {
        //LAB_800e5bf0
        final Vector3f sp0x10 = new Vector3f();
        final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[light.scriptIndex_48].innerStruct_00;
        sp0x10.set(bent.model_148.coord2_14.transforms.rotate).add(a2.angleAndColour_04);
        FUN_800e4674(light.light_00.direction_00, sp0x10);
      } else if(v1 == 3) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final Vector3f sp0x18 = new Vector3f();

        final int ticks = lightTicks_800c6928.get() & 0xfff;
        sp0x18.x = a2.angleAndColour_04.x + a2.vec_10.x * ticks;
        sp0x18.y = a2.angleAndColour_04.y + a2.vec_10.y * ticks;
        sp0x18.z = a2.angleAndColour_04.z + a2.vec_10.z * ticks;

        //LAB_800e5dcc
        FUN_800e4674(light.light_00.direction_00, sp0x18);
      }

      //LAB_800e5dd4
      final BttlLightStruct84Sub38 s0 = light._4c;
      v1 = s0._00 & 0xff;
      if(v1 == 1) {
        //LAB_800e5df4
        s0.vec_10.add(s0.vec_1c);
        s0.angleAndColour_04.add(s0.vec_10);

        if((s0._00 & 0x8000) != 0) {
          s0.ticksRemaining_34--;

          if(s0.ticksRemaining_34 <= 0) {
            s0._00 = 0;
            s0.angleAndColour_04.set(s0.vec_28);
          }
        }

        //LAB_800e5e90
        lights_800c692c[i].light_00.r_0c = s0.angleAndColour_04.x;
        lights_800c692c[i].light_00.g_0d = s0.angleAndColour_04.y;
        lights_800c692c[i].light_00.b_0e = s0.angleAndColour_04.z;
      } else if(v1 == 3) {
        //LAB_800e5ed0
        final float theta = MathHelper.cos((lightTicks_800c6928.get() / (float)0x1000 + s0.vec_28.x) % s0.vec_28.y / s0.vec_28.y);
        final float ratioA = 1.0f + theta;
        final float ratioB = 1.0f - theta;
        lights_800c692c[i].light_00.r_0c = (s0.angleAndColour_04.x * ratioA + s0.vec_10.x * ratioB) / 2.0f;
        lights_800c692c[i].light_00.g_0d = (s0.angleAndColour_04.y * ratioA + s0.vec_10.y * ratioB) / 2.0f;
        lights_800c692c[i].light_00.b_0e = (s0.angleAndColour_04.z * ratioA + s0.vec_10.z * ratioB) / 2.0f;
      }

      //LAB_800e5fb8
      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public static void deallocateLighting(final ScriptState<Void> state, final Void struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c[i].light_00);
    }

    final BattleLightStruct64 v0 = _800c6930;
    GTE.setBackgroundColour(v0.colour_00.x, v0.colour_00.y, v0.colour_00.z);
    projectionPlaneDistance_1f8003f8 = getProjectionPlaneDistance();
  }

  @Method(0x800e6070L)
  public static void allocateLighting() {
    final ScriptState<Void> state = SCRIPTS.allocateScriptState(1, "Lighting controller", 0, null);
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(Bttl::tickLighting);
    state.setRenderer(Bttl::deallocateLighting);
    _800c6930.colourIndex_60 = 0;
    resetLights();
  }

  @Method(0x800e60e0L)
  public static void FUN_800e60e0(final float r, final float g, final float b) {
    if(r < 0.0f) {
      LOGGER.warn("Negative R! %f", r);
    }

    if(g < 0.0f) {
      LOGGER.warn("Negative G! %f", g);
    }

    if(b < 0.0f) {
      LOGGER.warn("Negative B! %f", b);
    }

    final BattleLightStruct64 light = _800c6930;
    final Vector3f colour = light.colours_30[light.colourIndex_60];
    getBattleBackgroundLightColour(colour);

    light.colour_00.set(r, g, b);
    light.colourIndex_60 = light.colourIndex_60 + 1 & 3;
  }

  @Method(0x800e6170L)
  public static void FUN_800e6170() {
    final BattleLightStruct64 light = _800c6930;
    light.colourIndex_60 = light.colourIndex_60 - 1 & 3;
    light.colour_00.set(light.colours_30[light.colourIndex_60]);
  }

  @Method(0x800e61e4L)
  public static void FUN_800e61e4(final float r, final float g, final float b) {
    if(r < 0.0f) {
      LOGGER.warn("Negative R! %f", r);
    }

    if(g < 0.0f) {
      LOGGER.warn("Negative G! %f", g);
    }

    if(b < 0.0f) {
      LOGGER.warn("Negative B! %f", b);
    }

    GsSetFlatLight(0, light_800c6ddc);
    GsSetFlatLight(1, light_800c6ddc);
    GsSetFlatLight(2, light_800c6ddc);
    FUN_800e60e0(r, g, b);

    final BattleLightStruct64 light = _800c6930;
    GTE.setBackgroundColour(light.colour_00.x, light.colour_00.y, light.colour_00.z);
  }

  @Method(0x800e62a8L)
  public static void FUN_800e62a8() {
    FUN_800e6170();

    final BattleLightStruct64 light = _800c6930;
    GTE.setBackgroundColour(light.colour_00.x, light.colour_00.y, light.colour_00.z);

    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, lights_800c692c[i].light_00);
    }
  }

  @Method(0x800e6314L)
  public static void scriptDeffDeallocator(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    LOGGER.info(DEFF, "Deallocating DEFF script state %d", state.index);

    final DeffManager7cc struct7cc = deffManager_800c693c;

    struct7cc.deffPackage_5a8 = null;

    deffLoadingStage_800fafe8.set(4);

    if((struct7cc.flags_20 & 0x4_0000) != 0) {
      loadDeffSounds(_800c6938.bentState_04, 1);
    }

    if((struct7cc.flags_20 & 0x10_0000) != 0) {
      //LAB_800e63d0
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 combatant = getCombatant(i);
        if((combatant.flags_19e & 0x1) != 0 && combatant.charIndex_1a2 >= 0) {
          loadAttackAnimations(combatant);
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

  @ScriptDescription("Allocates an effect manager child for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6470L)
  public static ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> scriptAllocateDeffEffectManager(final RunningScript<? extends BattleObject> script) {
    final DeffManager7cc struct7cc = deffManager_800c693c;

    final int flags = script.params_20[0].get();
    struct7cc.flags_20 |= flags & 0x1_0000 | flags & 0x2_0000 | flags & 0x10_0000;

    if((struct7cc.flags_20 & 0x10_0000) != 0) {
      //LAB_800e651c
      for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
        final CombatantStruct1a8 combatant = getCombatant(i);

        if((combatant.flags_19e & 0x1) != 0 && combatant.mrg_04 != null && combatant.charIndex_1a2 >= 0) {
          FUN_800ca418(combatant);
        }

        //LAB_800e6564
      }
    }

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "DEFF ticker for script %d (%s)".formatted(script.scriptState_04.index, script.scriptState_04.name),
      script.scriptState_04,
      Bttl::scriptDeffTicker,
      null,
      Bttl::scriptDeffDeallocator,
      null
    );

    LOGGER.info(DEFF, "Allocated DEFF script state %d", state.index);

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x600_0400;

    final BattleStruct24_2 v0 = _800c6938;
    v0.type_00 = flags & 0xffff;
    v0.bentState_04 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
    v0._08 = script.params_20[2].get();
    v0.scriptIndex_0c = script.scriptState_04.index;
    v0.scriptEntrypoint_10 = script.params_20[3].get() & 0xff;
    v0.managerState_18 = state;
    v0.init_1c = true;
    v0.frameCount_20 = -1;
    return state;
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e665cL)
  public static void loadDragoonDeff(final RunningScript<? extends BattleObject> script) {
    final int index = script.params_20[0].get() & 0xffff;
    final int scriptEntrypoint = script.params_20[3].get() & 0xff;

    LOGGER.info(DEFF, "Loading dragoon DEFF (ID: %d, flags: %x)", index, script.params_20[0].get() & 0xffff_0000);

    final DeffManager7cc deffManager = deffManager_800c693c;
    deffManager.flags_20 |= dragoonDeffFlags_800fafec[index] << 16;
    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 battle24 = _800c6938;
    battle24.type_00 |= 0x100_0000;

    if((deffManager.flags_20 & 0x4_0000) != 0) {
      //LAB_800e66fc
      //LAB_800e670c
      loadDeffSounds(battle24.bentState_04, index != 0x2e || scriptEntrypoint != 0 ? 0 : 2);
    }

    //LAB_800e6714
    if(battle24.script_14 != null) {
      battle24.script_14 = null;
    }

    //LAB_800e6738
    for(int i = 0; i < dragoonDeffsWithExtraTims_800fb040.length; i++) {
      if(dragoonDeffsWithExtraTims_800fb040[i] == index) {
        if(Unpacker.isDirectory("SECT/DRGN0.BIN/%d".formatted(4115 + i))) {
          loadDrgnDir(0, 4115 + i, Bttl::uploadTims);
        }
      }

      //LAB_800e679c
    }

    //LAB_800e67b0
    loadDrgnDir(0, 4139 + index * 2, Bttl::uploadTims);
    loadDrgnDir(0, 4140 + index * 2 + "/0", files -> {
      loadDeffPackage(files, battle24.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      loadDrgnFile(0, 4140 + index * 2 + "/1", file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        _800c6938.script_14 = new ScriptFile(4140 + index * 2 + "/1", file.getBytes());
      });
    });
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a spell or item")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6844L)
  public static void loadSpellItemDeff(final RunningScript<? extends BattleObject> script) {
    final int id = script.params_20[0].get() & 0xffff;
    final int s0 = (id - 192) * 2;

    LOGGER.info(DEFF, "Loading spell item DEFF (ID: %d, flags: %x)", id, script.params_20[0].get() & 0xffff_0000);

    deffManager_800c693c.flags_20 |= 0x40_0000;
    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 t0 = _800c6938;

    if(t0.script_14 != null) {
      t0.script_14 = null;
    }

    t0.type_00 |= 0x200_0000;
    loadDrgnDir(0, 4307 + s0, Bttl::uploadTims);
    loadDrgnDir(0, 4308 + s0 + "/0", files -> {
      loadDeffPackage(files, t0.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      loadDrgnFile(0, 4308 + s0 + "/1", file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        _800c6938.script_14 = new ScriptFile(4308 + s0 + "/1", file.getBytes());
      });
    });
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for an enemy or boss")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6920L)
  public static void loadEnemyOrBossDeff(final RunningScript<? extends BattleObject> script) {
    final int s1 = script.params_20[0].get() & 0xff_0000;
    int monsterIndex = (short)script.params_20[0].get();

    if(monsterIndex == -1) {
      final BattleEntity27c bent = SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class);
      assert false : "?"; //script.params_20.get(0).set(sp0x20);
      monsterIndex = bent.combatant_144.charIndex_1a2;
    }

    LOGGER.info(DEFF, "Loading enemy/boss DEFF (ID: %d, flags: %x)", monsterIndex, s1 & 0xffff_0000);

    //LAB_800e69a8
    deffManager_800c693c.flags_20 |= s1 & 0x10_0000;
    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 v1 = _800c6938;

    if(v1.script_14 != null) {
      v1.script_14 = null;
    }

    v1.type_00 |= 0x300_0000;

    if(monsterIndex < 256) {
      final int finalMonsterIndex = monsterIndex;
      loadDrgnDir(0, 4433 + monsterIndex * 2, Bttl::uploadTims);
      loadDrgnDir(0, 4434 + monsterIndex * 2 + "/0", files -> {
        loadDeffPackage(files, v1.managerState_18);

        // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
        loadDrgnFile(0, 4434 + finalMonsterIndex * 2 + "/1", file -> {
          LOGGER.info(DEFF, "Loading DEFF script");
          _800c6938.script_14 = new ScriptFile(4434 + finalMonsterIndex * 2 + "/1", file.getBytes());
        });
      });
    } else {
      //LAB_800e6a30
      final int a0_0 = monsterIndex >>> 4;
      int fileIndex = enemyDeffFileIndices_800faec4[a0_0 - 0x100] + (monsterIndex & 0xf);
      if(a0_0 >= 320) {
        fileIndex += 117;
      }

      //LAB_800e6a60
      fileIndex = (fileIndex - 1) * 2;
      final int finalFileIndex = fileIndex;

      loadDrgnDir(0, 4945 + fileIndex, Bttl::uploadTims);
      loadDrgnDir(0, 4946 + fileIndex + "/0", files -> {
        loadDeffPackage(files, v1.managerState_18);

        // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
        loadDrgnFile(0, 4946 + finalFileIndex + "/1", file -> {
          LOGGER.info(DEFF, "Loading DEFF script");
          _800c6938.script_14 = new ScriptFile(4946 + finalFileIndex + "/1", file.getBytes());
        });
      });
    }

    //LAB_800e6a9c
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Allocates a DEFF and effect manager child for a cutscene")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6aecL)
  public static void loadCutsceneDeff(final RunningScript<? extends BattleObject> script) {
    final int v1 = script.params_20[0].get();
    final int cutsceneIndex = v1 & 0xffff;

    LOGGER.info(DEFF, "Loading cutscene DEFF (ID: %d, flags: %x)", cutsceneIndex, v1 & 0xffff_0000);

    scriptAllocateDeffEffectManager(script);

    final BattleStruct24_2 a0_0 = _800c6938;

    if(a0_0.script_14 != null) {
      a0_0.script_14 = null;
    }

    a0_0.type_00 |= 0x500_0000;

    //LAB_800e6b5c
    for(int i = 0; i < cutsceneDeffsWithExtraTims_800fb05c.length; i++) {
      if(cutsceneDeffsWithExtraTims_800fb05c[i] == cutsceneIndex) {
        if(Unpacker.isDirectory("SECT/DRGN0.BIN/%d".formatted(5505 + i))) {
          loadDrgnDir(0, 5505 + i, Bttl::uploadTims);
        }
      }

      //LAB_800e6bc0
    }

    //LAB_800e6bd4
    loadDrgnDir(0, 5511 + cutsceneIndex * 2, Bttl::uploadTims);
    loadDrgnDir(0, 5512 + cutsceneIndex * 2 + "/0", files -> {
      loadDeffPackage(files, a0_0.managerState_18);

      // We don't want the script to load before the DEFF package, so queueing this file inside of the DEFF package callback forces serialization
      loadDrgnFile(0, 5512 + cutsceneIndex * 2 + "/1", file -> {
        LOGGER.info(DEFF, "Loading DEFF script");
        _800c6938.script_14 = new ScriptFile(5512 + cutsceneIndex * 2 + "/1", file.getBytes());
      });
    });

    //LAB_800e6d7c
    deffLoadingStage_800fafe8.set(1);
  }

  @ScriptDescription("Unknown, related to loading DEFFs")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800e6db4L)
  public static FlowControl FUN_800e6db4(final RunningScript<?> script) {
    final FlowControl flow;
    final int deffStage;
    switch(script.params_20[0].get() & 0xffff) {
      case 0, 1 -> {
        deffStage = deffLoadingStage_800fafe8.get();
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
        deffStage = deffLoadingStage_800fafe8.get();
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
          final BattleStruct24_2 struct24 = _800c6938;
          struct24.managerState_18.loadScriptFile(struct24.script_14, struct24.scriptEntrypoint_10);
          struct24.init_1c = false;
          struct24.frameCount_20 = 0;
          deffLoadingStage_800fafe8.set(3);
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined t0");
        }

        //LAB_800e6ee4
      }

      case 3 -> {
        deffStage = deffLoadingStage_800fafe8.get();
        if(deffStage == 3) {
          //LAB_800e6f10
          flow = FlowControl.PAUSE_AND_REWIND;
        } else if(deffStage == 4) {
          //LAB_800e6f18
          deffLoadingStage_800fafe8.set(0);
          flow = FlowControl.CONTINUE;
        } else {
          throw new RuntimeException("undefined a3");
        }

        //LAB_800e6f20
      }

      case 4 -> {
        switch(deffLoadingStage_800fafe8.get()) {
          case 0:
            flow = FlowControl.CONTINUE;
            break;

          case 1:
            flow = FlowControl.PAUSE_AND_REWIND;
            break;

          case 2:
          case 3:
            _800c6938.managerState_18.deallocateWithChildren();

          case 4:
            deffLoadingStage_800fafe8.set(0);
            _800c6938.managerState_18 = null;
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

  @ScriptDescription("Unknown, can allocate a DEFF and effect manager child for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flagsAndIndex", description = "The effect manager's flags in the upper 16 bits, DEFF index in the lower 16 bits")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptEntrypoint", description = "The effect manager's entrypoint into this script")
  @Method(0x800e6fb4L)
  public static FlowControl FUN_800e6fb4(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e6fec
    //LAB_800e6ff0
    final long v1 = deffLoadingStage_800fafe8.get();

    //LAB_800e7014
    if(v1 == 0) {
      loadDragoonDeff(script);
    }

    if(v1 < 4) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(v1 == 4) {
      //LAB_800e702c
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    throw new IllegalStateException("Invalid v1");
  }

  @Method(0x800e7060L)
  public static void loadDeffPackage(final List<FileData> files, final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    LOGGER.info(DEFF, "Loading DEFF files");

    deffManager_800c693c.deffPackage_5a8 = new DeffPart[files.size()];
    Arrays.setAll(deffManager_800c693c.deffPackage_5a8, i -> DeffPart.getDeffPart(files, i));
    prepareDeffFiles(files, state);
  }

  @Method(0x800e70bcL)
  public static void scriptDeffTicker(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> struct) {
    final BattleStruct24_2 a0 = _800c6938;

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
  public static FlowControl scriptLoadSpellOrItemDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e721c
    //LAB_800e7220
    final int deffStage = deffLoadingStage_800fafe8.get();

    if(deffStage == 4) {
      //LAB_800e725c
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7244
    if(deffStage == 0) {
      loadSpellItemDeff(script);
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
  public static FlowControl scriptLoadEnemyOrBossDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e72b4
    //LAB_800e72b8
    final int deffStage = deffLoadingStage_800fafe8.get();

    if(deffStage == 4) {
      //LAB_800e72f4
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e72dc
    if(deffStage == 0) {
      loadEnemyOrBossDeff(script);
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
  public static FlowControl scriptLoadCutsceneDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0 && script.scriptState_04.index != _800c6938.scriptIndex_0c) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800e734c
    //LAB_800e7350
    final int deffStage = deffLoadingStage_800fafe8.get();

    if(deffStage == 4) {
      //LAB_800e738c
      deffLoadingStage_800fafe8.set(0);
      _800c6938.managerState_18 = null;
      return FlowControl.CONTINUE;
    }

    //LAB_800e7374
    if(deffStage == 0) {
      loadCutsceneDeff(script);
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
  public static FlowControl scriptLoadDeff(final RunningScript<? extends BattleObject> script) {
    if(deffLoadingStage_800fafe8.get() != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final int type = script.params_20[4].get();
    if(type == 0x100_0000) {
      loadDragoonDeff(script);
    } else if(type == 0x200_0000) {
      loadSpellItemDeff(script);
    } else if(type == 0x300_0000 || type == 0x400_0000) {
      loadEnemyOrBossDeff(script);
    } else if(type == 0x500_0000) {
      loadCutsceneDeff(script);
    }

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = _800c6938.managerState_18.innerStruct_00;
    manager.ticker_48 = Bttl::FUN_800e74e0;

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the current DEFF loading stage")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "loadingStage")
  @Method(0x800e7490L)
  public static FlowControl scriptGetDeffLoadingStage(final RunningScript<?> script) {
    script.params_20[0].set(deffLoadingStage_800fafe8.get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns two unknown values")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "bentIndex")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800e74acL)
  public static FlowControl FUN_800e74ac(final RunningScript<?> script) {
    final BattleStruct24_2 struct24 = _800c6938;
    script.params_20[0].set(struct24.bentState_04.index);
    script.params_20[1].set(struct24._08);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e74e0L)
  public static void FUN_800e74e0(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final BattleStruct24_2 struct24 = _800c6938;

    final int deffStage = deffLoadingStage_800fafe8.get();
    if(deffStage == 1) {
      //LAB_800e7510
      if(struct24.init_1c && struct24.script_14 != null && ((deffManager_800c693c.flags_20 & 0x4_0000) == 0 || (getLoadedDrgnFiles() & 0x40) == 0)) {
        //LAB_800e756c
        deffLoadingStage_800fafe8.set(2);
      }
    } else if(deffStage == 3) {
      //LAB_800e7574
      if(struct24.frameCount_20 >= 0) {
        struct24.frameCount_20 += vsyncMode_8007a3b8;
      }
    }

    //LAB_800e759c
  }

  /** Used in Astral Drain (ground glow) */
  @Method(0x800e75acL)
  public static void FUN_800e75ac(final GenericSpriteEffect24 spriteEffect, final MV transformMatrix) {
    final MV finalTransform = new MV();
    transformMatrix.compose(worldToScreenMatrix_800c3548, finalTransform);
    final float z = java.lang.Math.min(0x3ff8, zOffset_1f8003e8 + finalTransform.transfer.z / 4.0f);

    if(z >= 40) {
      //LAB_800e7610
      GTE.setTransforms(finalTransform);

      GTE.setVertex(0, spriteEffect.x_04 * 64, spriteEffect.y_06 * 64, 0);
      GTE.setVertex(1, (spriteEffect.x_04 + spriteEffect.w_08) * 64, spriteEffect.y_06 * 64, 0);
      GTE.setVertex(2, spriteEffect.x_04 * 64, (spriteEffect.y_06 + spriteEffect.h_0a) * 64, 0);
      GTE.perspectiveTransformTriangle();
      final float sx0 = GTE.getScreenX(0);
      final float sy0 = GTE.getScreenY(0);
      final float sx1 = GTE.getScreenX(1);
      final float sy1 = GTE.getScreenY(1);
      final float sx2 = GTE.getScreenX(2);
      final float sy2 = GTE.getScreenY(2);

      GTE.perspectiveTransform((spriteEffect.x_04 + spriteEffect.w_08) * 64, (spriteEffect.y_06 + spriteEffect.h_0a) * 64, 0);
      final float sx3 = GTE.getScreenX(2);
      final float sy3 = GTE.getScreenY(2);

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .clut(spriteEffect.clutX_10, spriteEffect.clutY_12)
        .vramPos((spriteEffect.tpage_0c & 0b1111) * 64, (spriteEffect.tpage_0c & 0b10000) != 0 ? 256 : 0)
        .rgb(spriteEffect.r_14, spriteEffect.g_15, spriteEffect.b_16)
        .pos(0, sx0, sy0)
        .pos(1, sx1, sy1)
        .pos(2, sx2, sy2)
        .pos(3, sx3, sy3)
        .uv(0, spriteEffect.u_0e, spriteEffect.v_0f)
        .uv(1, spriteEffect.u_0e + spriteEffect.w_08, spriteEffect.v_0f)
        .uv(2, spriteEffect.u_0e, spriteEffect.v_0f + spriteEffect.h_0a)
        .uv(3, spriteEffect.u_0e + spriteEffect.w_08, spriteEffect.v_0f + spriteEffect.h_0a);

      if((spriteEffect.flags_00 >>> 30 & 1) != 0) {
        cmd.translucent(Translucency.of((int)spriteEffect.flags_00 >>> 28 & 0b11));
      }

      GPU.queueCommand(z / 4.0f, cmd);
    }
    //LAB_800e7930
  }

  /**
   * Renderer for some kind of effect sprites like those in HUD DEFF.
   * Used for example for sprite effect overlays on red glow in Death Dimension.
   */
  @Method(0x800e7944L)
  public static void FUN_800e7944(final GenericSpriteEffect24 spriteEffect, final Vector3f translation, final int zMod) {
    if((int)spriteEffect.flags_00 >= 0) { // No errors
      final Vector3f finalTranslation = new Vector3f();
      translation.mul(worldToScreenMatrix_800c3548, finalTranslation);
      finalTranslation.add(worldToScreenMatrix_800c3548.transfer);

      final float x0 = MathHelper.safeDiv(finalTranslation.x * projectionPlaneDistance_1f8003f8, finalTranslation.z);
      final float y0 = MathHelper.safeDiv(finalTranslation.y * projectionPlaneDistance_1f8003f8, finalTranslation.z);

      // zMod needs to be ignored in z check or poly positions will overflow at low z values
      float z = zMod + finalTranslation.z / 4.0f;
      if(finalTranslation.z / 4.0f >= 40 && z >= 40) {
        if(z > 0x3ff8) {
          z = 0x3ff8;
        }

        //LAB_800e7a38
        final float zDepth = MathHelper.safeDiv(projectionPlaneDistance_1f8003f8 * 0x1000 / 4.0f, finalTranslation.z / 4.0f);
        final float x1 = spriteEffect.x_04 * spriteEffect.scaleX_1c / 8 * zDepth / 8;
        final float x2 = x1 + spriteEffect.w_08 * spriteEffect.scaleX_1c / 8 * zDepth / 8;
        final float y1 = spriteEffect.y_06 * spriteEffect.scaleY_1e / 8 * zDepth / 8;
        final float y2 = y1 + spriteEffect.h_0a * spriteEffect.scaleY_1e / 8 * zDepth / 8;
        final float sin = MathHelper.sin(spriteEffect.angle_20);
        final float cos = MathHelper.cos(spriteEffect.angle_20);

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut(spriteEffect.clutX_10, spriteEffect.clutY_12)
          .vramPos((spriteEffect.tpage_0c & 0b1111) * 64, (spriteEffect.tpage_0c & 0b10000) != 0 ? 256 : 0)
          .rgb(spriteEffect.r_14, spriteEffect.g_15, spriteEffect.b_16)
          .pos(0, x0 + x1 * cos - y1 * sin, y0 + x1 * sin + y1 * cos)
          .pos(1, x0 + x2 * cos - y1 * sin, y0 + x2 * sin + y1 * cos)
          .pos(2, x0 + x1 * cos - y2 * sin, y0 + x1 * sin + y2 * cos)
          .pos(3, x0 + x2 * cos - y2 * sin, y0 + x2 * sin + y2 * cos)
          .uv(0, spriteEffect.u_0e, spriteEffect.v_0f)
          .uv(1, spriteEffect.w_08 + spriteEffect.u_0e - 1, spriteEffect.v_0f)
          .uv(2, spriteEffect.u_0e, spriteEffect.h_0a + spriteEffect.v_0f - 1)
          .uv(3, spriteEffect.w_08 + spriteEffect.u_0e - 1, spriteEffect.h_0a + spriteEffect.v_0f - 1);

        if((spriteEffect.flags_00 & 0x4000_0000) != 0) {
          cmd.translucent(Translucency.of((int)spriteEffect.flags_00 >>> 28 & 0b11));
        }

        GPU.queueCommand(z / 4.0f, cmd);
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7dbcL)
  public static float transformToScreenSpace(final Vector2f out, final Vector3f translation) {
    final Vector3f transformed = new Vector3f();
    translation.mul(worldToScreenMatrix_800c3548, transformed);
    transformed.add(worldToScreenMatrix_800c3548.transfer);

    if(transformed.z >= 160) {
      out.x = transformed.x * projectionPlaneDistance_1f8003f8 / transformed.z;
      out.y = transformed.y * projectionPlaneDistance_1f8003f8 / transformed.z;
      return transformed.z / 4.0f;
    }

    //LAB_800e7e8c
    //LAB_800e7e90
    return 0;
  }

  @Method(0x800e7ea4L)
  public static void renderGenericSpriteAtZOffset0(final GenericSpriteEffect24 spriteEffect, final Vector3f translation) {
    FUN_800e7944(spriteEffect, translation, 0);
  }

  @Method(0x800e7ec4L)
  public static <T extends EffectManagerParams<T>> void effectManagerDestructor(final ScriptState<EffectManagerData6c<T>> state, final EffectManagerData6c<T> struct) {
    LOGGER.info(EFFECTS, "Deallocating effect manager %d", state.index);

    if(struct.parentScript_50 != null) {
      if(struct.newChildScript_56 != null) {
        struct.newChildScript_56.innerStruct_00.oldChildScript_54 = struct.oldChildScript_54;
      } else {
        //LAB_800e7f4c
        struct.parentScript_50.innerStruct_00.childScript_52 = struct.oldChildScript_54;
      }

      //LAB_800e7f6c
      if(struct.oldChildScript_54 != null) {
        struct.oldChildScript_54.innerStruct_00.newChildScript_56 = struct.newChildScript_56;
      }

      //LAB_800e7fa0
      struct.parentScript_50 = null;
      struct.oldChildScript_54 = null;
      struct.newChildScript_56 = null;
    }

    //LAB_800e7fac
    //LAB_800e7fcc
    while(struct.childScript_52 != null) {
      EffectManagerData6c<?> child = struct.childScript_52.innerStruct_00;

      //LAB_800e7ff8
      while(child.childScript_52 != null) {
        child = child.childScript_52.innerStruct_00;
      }

      //LAB_800e8020
      child.myScriptState_0e.deallocateWithChildren();
    }

    //LAB_800e8040
    if(struct.destructor_4c != null) {
      struct.destructor_4c.accept(state, struct);
    }
  }

  public static ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> allocateEffectManager(final String name, @Nullable final ScriptState<? extends BattleObject> parentState, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>> destructor, @Nullable final Effect effect) {
    return allocateEffectManager(name, parentState, ticker, renderer, destructor, effect, new EffectManagerParams.VoidType());
  }

  @Method(0x800e80c4L)
  public static <T extends EffectManagerParams<T>> ScriptState<EffectManagerData6c<T>> allocateEffectManager(final String name, @Nullable ScriptState<? extends BattleObject> parentState, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> destructor, @Nullable final Effect effect, final T inner) {
    final ScriptState<EffectManagerData6c<T>> state = SCRIPTS.allocateScriptState(name, new EffectManagerData6c<>(name, inner));
    final EffectManagerData6c<T> manager = state.innerStruct_00;

    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(Bttl::effectManagerTicker);

    if(renderer != null) {
      state.setRenderer(renderer);
    }

    state.setDestructor(Bttl::effectManagerDestructor);

    final StackWalker.StackFrame caller = DebugHelper.getCallerFrame();

    manager.effect_44 = effect;

    if(effect != null) {
      LOGGER.info(EFFECTS, "Allocating effect manager %d for %s (parent: %d) from %s.%s(%s:%d)", state.index, manager.effect_44.getClass().getSimpleName(), parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    } else {
      LOGGER.info(EFFECTS, "Allocating empty effect manager %d (parent: %d) from %s.%s(%s:%d)", state.index, parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    }

    manager.flags_04 = 0xff00_0000;
    manager.scriptIndex_0c = -1;
    manager.coord2Index_0d = -1;
    manager.myScriptState_0e = state;
    manager.params_10.flags_00 = 0x5400_0000;
    manager.params_10.scale_16.set(1.0f, 1.0f, 1.0f);
    manager.params_10.colour_1c.set(0x80, 0x80, 0x80);
    manager.ticker_48 = ticker;
    manager.destructor_4c = destructor;

    if(parentState != null) {
      if(!BattleObject.EM__.equals(parentState.innerStruct_00.magic_00)) {
        parentState = deffManager_800c693c.scriptState_1c;
      }

      final EffectManagerData6c<?> parent = (EffectManagerData6c<?>)parentState.innerStruct_00;
      final EffectManagerData6c<?> child = state.innerStruct_00;

      child.parentScript_50 = (ScriptState<EffectManagerData6c<?>>)parentState;
      if(parent.childScript_52 != null) {
        child.oldChildScript_54 = parent.childScript_52;
        parent.childScript_52.innerStruct_00.newChildScript_56 = (ScriptState)state;
      }

      parent.childScript_52 = (ScriptState)state;
    }

    return state;
  }

  /** Considers all parents */
  @Method(0x800e8594L)
  public static void calculateEffectTransforms(final MV transformMatrix, final EffectManagerData6c<?> manager) {
    transformMatrix.rotationXYZ(manager.params_10.rot_10);
    transformMatrix.transfer.set(manager.params_10.trans_04);
    transformMatrix.scaleLocal(manager.params_10.scale_16);

    EffectManagerData6c<?> currentManager = manager;
    int scriptIndex = manager.scriptIndex_0c;

    //LAB_800e8604
    while(scriptIndex >= 0) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[scriptIndex];
      if(state == null) { // error, parent no longer exists
        manager.params_10.flags_00 |= 0x8000_0000;
        transformMatrix.transfer.z = -0x7fff;
        scriptIndex = -2;
        break;
      }

      final BattleObject base = (BattleObject)state.innerStruct_00;
      if(BattleObject.EM__.equals(base.magic_00)) {
        final EffectManagerData6c<?> baseManager = (EffectManagerData6c<?>)base;
        final MV baseTransformMatrix = new MV();
        baseTransformMatrix.rotationXYZ(baseManager.params_10.rot_10);
        baseTransformMatrix.transfer.set(baseManager.params_10.trans_04);
        baseTransformMatrix.scaleLocal(baseManager.params_10.scale_16);

        if(currentManager.coord2Index_0d != -1) {
          //LAB_800e866c
          FUN_800ea0f4(baseManager, currentManager.coord2Index_0d).coord.compose(baseTransformMatrix, baseTransformMatrix);
        }

        //LAB_800e86ac
        transformMatrix.compose(baseTransformMatrix);
        currentManager = baseManager;
        scriptIndex = currentManager.scriptIndex_0c;
        //LAB_800e86c8
      } else if(BattleObject.BOBJ.equals(base.magic_00)) {
        final BattleEntity27c bent = (BattleEntity27c)base;
        final Model124 s1 = bent.model_148;
        applyModelRotationAndScale(s1);
        final int coord2Index = currentManager.coord2Index_0d;

        final MV sp0x10 = new MV();
        if(coord2Index == -1) {
          sp0x10.set(s1.coord2_14.coord);
        } else {
          //LAB_800e8738
          GsGetLw(s1.modelParts_00[coord2Index].coord2_04, sp0x10);
          s1.modelParts_00[coord2Index].coord2_04.flg = 0;
        }

        //LAB_800e8774
        transformMatrix.compose(sp0x10);
        currentManager = null;
        scriptIndex = -1; // finished
      } else { // error, parent not a bent or effect
        //LAB_800e878c
        //LAB_800e8790
        manager.params_10.flags_00 |= 0x8000_0000;
        transformMatrix.transfer.z = -0x7fff;
        scriptIndex = -2;
        break;
      }
    }

    //LAB_800e87b4
    if(scriptIndex == -2) { // error
      final MV transposedWs = new MV();
      final Vector3f transposedTranslation = new Vector3f();
      worldToScreenMatrix_800c3548.transpose(transposedWs);
      transposedTranslation.set(worldToScreenMatrix_800c3548.transfer).negate();
      transposedTranslation.mul(transposedWs, transposedWs.transfer);
      transformMatrix.compose(transposedWs);
    }
    //LAB_800e8814
  }

  @Method(0x800e8e9cL)
  public static <T extends EffectManagerParams<T>> void effectManagerTicker(final ScriptState<EffectManagerData6c<T>> state, final EffectManagerData6c<T> data) {
    AttachmentHost subPtr = data;

    //LAB_800e8ee0
    while(subPtr.getAttachment() != null) {
      final EffectAttachment sub = subPtr.getAttachment();

      final int ret = (int)((BiFunction)sub.ticker_08).apply(data, subPtr.getAttachment());
      if(ret == 0) { // Remove this attachment
        //LAB_800e8f2c
        data.flags_04 &= ~(1 << sub.id_05);
        subPtr.setAttachment(sub.getAttachment());
      } else if(ret == 1) { // Continue
        //LAB_800e8f6c
        subPtr = sub;
        //LAB_800e8f1c
      } else if(ret == 2) { // Remove this effect entirely
        //LAB_800e8f78
        state.deallocateWithChildren();
        return;
      }

      //LAB_800e8f8c
    }

    //LAB_800e8f9c
    if(data.ticker_48 != null) {
      data.ticker_48.accept(state, data);
    }

    //LAB_800e8fb8
  }

  @Method(0x800e8ffcL)
  public static void allocateDeffManager() {
    if(deffManager_800c693c != null) {
      deffManager_800c693c.delete();
    }

    final DeffManager7cc deffManager = new DeffManager7cc();
    _800c6938 = deffManager._5b8;
    _800c6930 = deffManager._5dc;
    lights_800c692c = deffManager._640;
    deffManager.flags_20 = 0x4;
    deffManager_800c693c = deffManager;
    spriteMetrics_800c6948 = deffManager.spriteMetrics_39c;
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> manager = allocateEffectManager("DEFF manager", null, null, null, null, null);
    manager.innerStruct_00.flags_04 = 0x600_0400;
    deffManager.scriptState_1c = manager;
    allocateLighting();
    loadStageAmbiance();
  }

  @Method(0x800e9120L)
  public static void deallocateLightingControllerAndDeffManager() {
    scriptStatePtrArr_800bc1c0[1].deallocateWithChildren();
    deallocateDeffManagerScriptsArray();
    deffManager_800c693c.scriptState_1c.deallocateWithChildren();
    deffManager_800c693c.delete();
    deffManager_800c693c = null;
  }

  @Method(0x800e9178L)
  public static void FUN_800e9178(final int mode) {
    if(mode == 1) {
      //LAB_800e91a0
      deffManager_800c693c.scriptState_1c.innerStruct_00.removeAttachment(10);
    } else if(mode == 2) {
      //LAB_800e91d8
      deffManager_800c693c.scriptState_1c.innerStruct_00.removeAttachment(10);
      deallocateDeffManagerScriptsArray();
    } else {
      // This seems to be destroying and the re-creating the DEFF manager script state? Must be for ending the DEFF or something?

      //LAB_800e9214
      deallocateDeffManagerScriptsArray();
      deffManager_800c693c.scriptState_1c.deallocateWithChildren();
      final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> manager = allocateEffectManager("DEFF manager (but different)", null, null, null, null, null);
      deffManager_800c693c.scriptState_1c = manager;
      manager.innerStruct_00.flags_04 = 0x600_0400;
    }

    //LAB_800e9278
  }

  @Method(0x800e929cL)
  public static void uploadTims(final List<FileData> files) {
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
  public static FlowControl scriptAllocateEmptyEffectManagerChild(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Empty EffectManager child, allocated by script %d (%s) from FUN_800e93e0".formatted(script.scriptState_04.index, script.scriptState_04.name), script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  /** Has some relation to rendering of certain effect sprites, like ones from HUD DEFF */
  @Method(0x800e9428L)
  public static void renderBillboardSpriteEffect(final SpriteMetrics08 metrics, final EffectManagerParams<?> managerInner, final MV transformMatrix) {
    if(managerInner.flags_00 >= 0) { // No errors
      final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24(managerInner.flags_00, metrics);
      spriteEffect.r_14 = managerInner.colour_1c.x & 0xff;
      spriteEffect.g_15 = managerInner.colour_1c.y & 0xff;
      spriteEffect.b_16 = managerInner.colour_1c.z & 0xff;
      spriteEffect.scaleX_1c = managerInner.scale_16.x;
      spriteEffect.scaleY_1e = managerInner.scale_16.y;
      spriteEffect.angle_20 = managerInner.rot_10.z;

      if((managerInner.flags_00 & 0x400_0000) != 0) {
        zOffset_1f8003e8 = managerInner.z_22;
        FUN_800e75ac(spriteEffect, transformMatrix);
      } else {
        //LAB_800e9574
        FUN_800e7944(spriteEffect, transformMatrix.transfer, managerInner.z_22);
      }
    }
    //LAB_800e9580
  }

  @Method(0x800e95f0L)
  public static void getSpriteMetricsFromSource(final BillboardSpriteEffect0c spriteEffect, final int flag) {
    spriteEffect.flags_00 = flag | 0x400_0000;

    if((flag & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[flag & 0xff];
      spriteEffect.metrics_04.u_00 = metrics.u_00;
      spriteEffect.metrics_04.v_02 = metrics.v_02;
      spriteEffect.metrics_04.w_04 = metrics.w_04;
      spriteEffect.metrics_04.h_05 = metrics.h_05;
      spriteEffect.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_800e9658
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(flag | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      spriteEffect.metrics_04.u_00 = deffMetrics.u_00;
      spriteEffect.metrics_04.v_02 = deffMetrics.v_02;
      spriteEffect.metrics_04.w_04 = deffMetrics.w_04 * 4;
      spriteEffect.metrics_04.h_05 = deffMetrics.h_06;
      spriteEffect.metrics_04.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }
    //LAB_800e96bc
  }

  @ScriptDescription("Allocates a new billboard sprite effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "Flag meanings are unknown")
  @Method(0x800e96ccL)
  public static FlowControl allocateBillboardSpriteEffect(final RunningScript<? extends BattleObject> script) {
    final BillboardSpriteEffect0c effect = new BillboardSpriteEffect0c();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "BillboardSpriteEffect0c",
      script.scriptState_04,
      null,
      effect::renderBillboardSpriteEffect,
      null,
      effect
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x400_0000;
    getSpriteMetricsFromSource(effect, script.params_20[1].get());
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x800e9798L)
  public static FlowControl FUN_800e9798(final RunningScript<?> script) {
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
  public static FlowControl FUN_800e9854(final RunningScript<? extends BattleObject> script) {
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x200_0000);

    final ModelEffect13c effect = new ModelEffect13c("Script " + script.scriptState_04.index);

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      animatedTmdType.name,
      script.scriptState_04,
      effect::FUN_800ea3f8,
      effect::FUN_800ea510,
      null,
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
      model.vramSlot_9d = modelVramSlots_800fb06c[tpage];
    } else {
      model.vramSlot_9d = 0;
    }

    loadModelTmd(model, effect.extTmd_08);
    loadModelAnim(model, effect.anim_0c);
    addGenericAttachment(manager, 0, 0x100, 0);
    manager.params_10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an unknown model effect manager (used in Miranda transformation)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The DEFF flags, mostly unknown")
  @Method(0x800e99bcL)
  public static FlowControl FUN_800e99bc(final RunningScript<? extends BattleObject> script) {
    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x100_0000);

    final ModelEffect13c effect = new ModelEffect13c("Script " + script.scriptState_04.index);

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      animatedTmdType.name,
      script.scriptState_04,
      effect::FUN_800ea3f8,
      effect::FUN_800ea510,
      null,
      effect,
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x100_0000;
    effect._00 = 0;

    effect.tmdType_04 = animatedTmdType;
    effect.extTmd_08 = animatedTmdType.tmd_0c;
    effect.anim_0c = animatedTmdType.anim_14;
    effect.model_10.vramSlot_9d = 0;
    effect.model_134 = effect.model_10;
    loadModelTmd(effect.model_134, effect.extTmd_08);
    loadModelAnim(effect.model_134, effect.anim_0c);
    addGenericAttachment(manager, 0, 0x100, 0);
    manager.params_10.flags_00 = 0x5400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e9ae4L)
  public static void FUN_800e9ae4(final Model124 model, final BattleStage a1) {
    model.coord2_14.set(a1.coord2_558);
    model.coord2_14.transforms.set(a1.param_5a8);

    model.animType_90 = -1;
    model.partTransforms_90 = a1.rotTrans_5d4;
    model.partTransforms_94 = a1.rotTrans_5d8;
    model.partCount_98 = a1.partCount_5dc;
    model.totalFrames_9a = a1.totalFrames_5de;
    model.animationState_9c = a1.animationState_5e0;
    model.vramSlot_9d = 0;
    model.zOffset_a0 = 0x200;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.smallerStructPtr_a4 = null;
    model.remainingFrames_9e = a1.remainingFrames_5e2;
    model.interpolationFrameIndex = 0;
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
  public static void FUN_800e9db4(final Model124 model1, final Model124 model2) {
    //LAB_800e9dd8
    model1.set(model2);

    model1.modelParts_00 = new ModelPart10[model1.modelParts_00.length];
    Arrays.setAll(model1.modelParts_00, i -> new ModelPart10().set(model2.modelParts_00[i]));

    //LAB_800e9ee8
    for(final ModelPart10 dobj2 : model1.modelParts_00) {
      dobj2.coord2_04 = new GsCOORDINATE2();
      dobj2.coord2_04.super_ = model1.coord2_14;
    }
  }

  @ScriptDescription("Allocates an effect manager for an unknown purpose")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager's index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800e9f68L)
  public static FlowControl FUN_800e9f68(final RunningScript<? extends BattleObject> script) {
    final int s2 = script.params_20[1].get();

    final ModelEffect13c s0 = new ModelEffect13c("Script " + script.scriptState_04.index);

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      "Unknown (FUN_800e9f68, s2 = 0x%x)".formatted(s2),
      script.scriptState_04,
      s0::FUN_800ea3f8,
      s0::FUN_800ea510,
      null,
      s0,
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x200_0000;

    s0._00 = 0;
    s0.tmdType_04 = null;
    s0.extTmd_08 = null;
    s0.anim_0c = null;
    s0.model_134 = s0.model_10;

    if((s2 & 0xff00_0000) == 0x700_0000) {
      FUN_800e9ae4(s0.model_10, battlePreloadedEntities_1f8003f4.stage_963c);
    } else {
      //LAB_800ea030
      FUN_800e9db4(s0.model_10, ((BattleEntity27c)scriptStatePtrArr_800bc1c0[s2].innerStruct_00).model_148);
    }

    //LAB_800ea04c
    final Model124 model = s0.model_134;
    manager.params_10.trans_04.set(model.coord2_14.coord.transfer);
    manager.params_10.rot_10.set(model.coord2_14.transforms.rotate);
    manager.params_10.scale_16.set(model.coord2_14.transforms.scale);
    manager.params_10.flags_00 = 0x1400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ea0f4L)
  public static GsCOORDINATE2 FUN_800ea0f4(final EffectManagerData6c<?> effectManager, final int coord2Index) {
    final Model124 model = ((ModelEffect13c)effectManager.effect_44).model_10;
    applyModelRotationAndScale(model);
    return model.modelParts_00[coord2Index].coord2_04;
  }

  @ScriptDescription("Hides an effect's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.FLAG, name = "part", description = "Which model part to hide")
  @Method(0x800ea13cL)
  public static FlowControl scriptHideEffectModelPart(final RunningScript<?> script) {
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
  public static FlowControl scriptShowEffectModelPart(final RunningScript<?> script) {
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
  public static FlowControl scriptLoadCmbAnimation(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.AnimType.class));
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;

    final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(script.params_20[1].get() | 0x200_0000);
    final Anim cmb = animatedTmdType.anim_14;
    effect.anim_0c = cmb;
    loadModelAnim(effect.model_134, cmb);
    manager.params_10.ticks_24 = 0;
    addGenericAttachment(manager, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow size for a battle object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z size (12-bit fixed-point)")
  @Method(0x800ea2a0L)
  public static FlowControl scriptSetBttlShadowSize(final RunningScript<?> script) {
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
  public static FlowControl scriptSetBttlShadowOffset(final RunningScript<?> script) {
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
  public static FlowControl scriptGetEffectLoopCount(final RunningScript<?> script) {
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
  public static void prepareDeffFiles(final List<FileData> deff, final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> deffManagerState) {
    //LAB_800ea674
    for(int i = 0; i < deff.size(); i++) {
      final FileData data = deff.get(i);

      final int flags = data.readInt(0);
      final int type = flags & 0xff00_0000; // Flags
      if(type == 0x100_0000) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);
        final CContainer extTmd = tmdType.tmd_0c;
        final TmdWithId tmd = extTmd.tmdPtr_00;

        for(int objectIndex = 0; objectIndex < tmd.tmd.header.nobj; objectIndex++) {
          optimisePacketsIfNecessary(tmd, objectIndex);
        }

        if(tmdType.textureInfo_08 != null && deffManagerState.index != 0) {
          addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, extTmd, tmdType.textureInfo_08);
        }
      } else if(type == 0x200_0000) {
        final DeffPart.AnimatedTmdType animType = new DeffPart.AnimatedTmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);

        if(animType.textureInfo_08 != null && deffManagerState.index != 0) {
          addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, animType.tmd_0c, animType.textureInfo_08);
        }
      } else if(type == 0x300_0000) {
        final DeffPart.TmdType tmdType = new DeffPart.TmdType("DEFF index %d (flags %08x)".formatted(i, flags), data);
        final CContainer extTmd = tmdType.tmd_0c;

        optimisePacketsIfNecessary(extTmd.tmdPtr_00, 0);

        if(tmdType.textureInfo_08 != null && deffManagerState.index != 0) {
          addCContainerTextureAnimationAttachments(deffManagerState.innerStruct_00, extTmd, tmdType.textureInfo_08);
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
  }

  @Method(0x800ea7d0L)
  public static void hudDeffLoaded(final List<FileData> files) {
    final DeffManager7cc struct7cc = deffManager_800c693c;
    prepareDeffFiles(files, struct7cc.scriptState_1c);

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

  @Method(0x800eab8cL)
  public static void deallocateDeffManagerScriptsArray() {
    deffManager_800c693c.scripts_2c = null;
  }

  /** See {@link DeffPart#flags_00} */
  @Method(0x800eac58L)
  public static DeffPart getDeffPart(final int flags) {
    //LAB_800eac84
    for(final DeffPart deffPart : deffManager_800c693c.deffPackage_5a8) {
      if(deffPart.flags_00 == flags) {
        return deffPart;
      }
      //LAB_800eaca0
    }

    //LAB_800eacac
    throw new IllegalArgumentException("Couldn't find DEFF with flags " + Long.toHexString(flags));
  }

  @Method(0x800eacf4L)
  public static void loadBattleHudDeff() {
    loadDrgnDirSync(0, "4114/2", Bttl::hudDeffLoaded);
    loadDrgnDir(0, "4114/3", Bttl::uploadTims);
    loadDrgnDir(0, "4114/1", files -> {
      deffManager_800c693c.scripts_2c = new ScriptFile[files.size()];

      for(int i = 0; i < files.size(); i++) {
        deffManager_800c693c.scripts_2c[i] = new ScriptFile("DRGN0.4114.1." + i, files.get(i).getBytes());
      }
    });
  }

  @Method(0x800ead44L)
  public static void applyTextureAnimation(final Rect4i rect, final int h) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, rect.x, rect.y + rect.h - h, rect.w, h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x, rect.y + h, rect.x, rect.y, rect.w, rect.h - h));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(rect.x, rect.y, 960, 256, rect.w, h));
  }

  @Method(0x800eaec8L)
  public static int tickTextureAnimationAttachment(final EffectManagerData6c<?> manager, final TextureAnimationAttachment1c anim) {
    //LAB_800eaef0
    anim.accumulator_14 += anim.step_18;

    //LAB_800eaf08
    int h = (anim.step_18 >> 8) % anim.rect_0c.h;

    if(h < 0) {
      h += anim.rect_0c.h;
    }

    //LAB_800eaf30
    if(h != 0) {
      applyTextureAnimation(anim.rect_0c, h);
    }

    //LAB_800eaf44
    return 1;
  }

  @Method(0x800eaf54L)
  public static TextureAnimationAttachment1c findTextureAnimationAttachment(EffectManagerData6c<?> manager, final Rect4i vramPos) {
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
  public static FlowControl scriptRemoveTextureAnimationAttachment(final RunningScript<?> script) {
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
  public static FlowControl scriptApplyTextureAnimationAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;

    final DeffPart.TmdType tmdType = effect.tmdType_04;
    final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[script.params_20[1].get() * 2];
    final TextureAnimationAttachment1c attachment = findTextureAnimationAttachment(manager, textureInfo.vramPos_00);

    if(attachment != null) {
      int h = (-attachment.accumulator_14 >> 8) % attachment.rect_0c.h;

      if(h < 0) {
        h += attachment.rect_0c.h;
      }

      //LAB_800eb25c
      if(h != 0) {
        applyTextureAnimation(attachment.rect_0c, h);
      }
    }

    //LAB_800eb270
    return FlowControl.CONTINUE;
  }

  /** Adds a new texture update attachment, or updates an existing one's step value if one already exists for that region */
  @Method(0x800eb280L)
  public static void addOrUpdateTextureAnimationAttachment(final EffectManagerData6c<?> manager, final Rect4i vramPos, final int step) {
    TextureAnimationAttachment1c attachment = findTextureAnimationAttachment(manager, vramPos);

    if(attachment == null) {
      attachment = manager.addAttachment(10, 0, Bttl::tickTextureAnimationAttachment, new TextureAnimationAttachment1c());
      attachment.rect_0c.set(vramPos);
      attachment.accumulator_14 = 0;
    }

    //LAB_800eb2ec
    attachment.step_18 = step;
  }

  @Method(0x800eb308L)
  public static void addCContainerTextureAnimationAttachments(final EffectManagerData6c<?> manager, final CContainer cContainer, final DeffPart.TextureInfo[] textureInfo) {
    if(cContainer.ptr_08 != null) {
      final CContainerSubfile2 s2 = cContainer.ptr_08;

      //LAB_800eb348
      for(int s1 = 0; s1 < 7; s1++) {
        final short[] s0 = s2._00[s1];

        if((s0[0] & 0x4000) != 0) {
          final TextureAnimationAttachment1c sub = manager.addAttachment(10, 0, Bttl::tickTextureAnimationAttachment, new TextureAnimationAttachment1c());

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
  public static void addOrUpdateTextureAnimationAttachment(final int scriptIndex, final int textureIndex, final int step) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(scriptIndex, EffectManagerData6c.class);
    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;
    final DeffPart.TmdType tmdType = effect.tmdType_04;
    addOrUpdateTextureAnimationAttachment(manager, tmdType.textureInfo_08[textureIndex * 2].vramPos_00, step);
  }

  @ScriptDescription("Adds or updates a texture animation attachment to an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "varIndex", description = "The texture index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "step", description = "The animation movement step")
  @Method(0x800eb518L)
  public static FlowControl scriptAddOrUpdateTextureAnimationAttachment(final RunningScript<?> script) {
    addOrUpdateTextureAnimationAttachment(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Dart transform */
  @Method(0x800eb554L)
  public static void applyRedEyeDragoonTransformationFlameArmorEffectTextureAnimations(final Rect4i a0, final Vector2i a1, final int height) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(960, 256, a1.x, a1.y + a0.h - height, a0.w, height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.x, a1.y + height, a1.x, a1.y, a0.w, a0.h - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a1.x, a1.y, a0.x, a0.y + a0.h - height, a0.w, height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x, a0.y + height, a0.x, a0.y, a0.w, a0.h - height));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(a0.x, a0.y, 960, 256, a0.w, height));
  }

  @Method(0x800eb7c4L)
  public static int tickRedEyeDragoonTransformationFlameArmorEffect(final EffectManagerData6c<?> manager, final RedEyeDragoonTransformationFlameArmorEffect20 effect) {
    //LAB_800eb7e8
    effect.accumulator_14 += effect.step_18;

    //LAB_800eb800
    int a2 = (effect.step_18 >> 8) % effect.rect_0c.h;

    if(a2 < 0) {
      a2 = a2 + effect.rect_0c.h;
    }

    //LAB_800eb828
    if(a2 != 0) {
      applyRedEyeDragoonTransformationFlameArmorEffectTextureAnimations(effect.rect_0c, effect._1c, a2);
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
  public static FlowControl scriptAddRedEyeDragoonTransformationFlameArmorEffectAttachment(final RunningScript<?> script) {
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
    final RedEyeDragoonTransformationFlameArmorEffect20 attachment = manager.addAttachment(10, 0, Bttl::tickRedEyeDragoonTransformationFlameArmorEffect, new RedEyeDragoonTransformationFlameArmorEffect20());
    attachment.rect_0c.set(textureInfo1.vramPos_00);
    attachment.accumulator_14 = 0;
    attachment.step_18 = script.params_20[3].get();
    attachment._1c.x = textureInfo2.vramPos_00.x;
    attachment._1c.y = textureInfo2.vramPos_00.y;
    return FlowControl.CONTINUE;
  }

  @Method(0x800eb9acL)
  public static void loadStageTmd(final BattleStage stage, final CContainer extTmd, final TmdAnimationFile tmdAnim) {
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
        FUN_800ec86c(stage, i);
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
    applyInitialStageTransforms(stage, tmdAnim);

    stage.coord2_558.coord.transfer.set(x, y, z);
    stage.flags_5e4 = 0;
    stage.z_5e8 = 0x200;
  }

  @Method(0x800ebb58L)
  public static void applyScreenDarkening(final int multiplier) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958;

    //LAB_800ebb7c
    for(int y = 0; y < 16; y++) {
      //LAB_800ebb80
      for(int x = 0; x < stageDarkeningClutWidth_800c695c; x++) {
        final int colour = darkening.original_000[y][x] & 0xffff;
        final int mask = colour >>> 15 & 0x1;
        final int b = (colour >>> 10 & 0x1f) * multiplier >> 4 & 0x1f;
        final int g = (colour >>> 5 & 0x1f) * multiplier >> 4 & 0x1f;
        final int r = (colour & 0x1f) * multiplier >> 4 & 0x1f;

        final int newColour;
        if(r != 0 || g != 0 || b != 0 || colour == 0) {
          newColour = mask << 15 | b << 10 | g << 5 | r;
        } else {
          newColour = colour & 0xffff_8000 | 0x1;
        }

        darkening.modified_800[y][x] = newColour;
      }
    }

    for(int y = 0; y < 16; y++) {
      GPU.uploadData15(new Rect4i(448, (240 + y), 64, 1), stageDarkening_800c6958.modified_800[y]);
    }
  }

  @Method(0x800ebd34L)
  public static void applyBattleStageTextureAnimations(final BattleStage struct, final int index) {
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

  @Method(0x800ec258L)
  public static void renderBttlShadow(final Model124 model) {
    final Model124 shadow = shadowModel_800bda10;

    GsInitCoordinate2(model.coord2_14, shadow.coord2_14);

    if(model.shadowType_cc == 3) {
      //LAB_800ec2ec
      shadow.coord2_14.coord.transfer.x = model.shadowOffset_118.x + model.modelParts_00[model.modelPartWithShadowIndex_cd].coord2_04.coord.transfer.x;
      shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y - MathHelper.safeDiv(model.coord2_14.coord.transfer.y, model.coord2_14.transforms.scale.y);
      shadow.coord2_14.coord.transfer.z = model.shadowOffset_118.z + model.modelParts_00[model.modelPartWithShadowIndex_cd].coord2_04.coord.transfer.z;
    } else {
      shadow.coord2_14.coord.transfer.x = model.shadowOffset_118.x;

      if(model.shadowType_cc == 1) {
        shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y;
      } else {
        //LAB_800ec2bc
        shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y - MathHelper.safeDiv(model.coord2_14.coord.transfer.y, model.coord2_14.transforms.scale.y);
      }

      //LAB_800ec2e0
      shadow.coord2_14.coord.transfer.z = model.shadowOffset_118.z;
    }

    //LAB_800ec370
    shadow.zOffset_a0 = model.zOffset_a0 + 16;
    shadow.coord2_14.transforms.scale.set(model.shadowSize_10c.x).div(4.0f);
    shadow.coord2_14.coord.rotationXYZ(shadow.coord2_14.transforms.rotate);
    shadow.coord2_14.coord.scaleLocal(shadow.coord2_14.transforms.scale);
    shadow.coord2_14.flg = 0;
    final GsCOORDINATE2 v0 = shadow.modelParts_00[0].coord2_04;
    final Transforms s0 = v0.transforms;
    s0.rotate.zero();
    v0.coord.rotationZYX(s0.rotate);
    s0.trans.zero();
    v0.coord.transfer.set(s0.trans);

    final MV sp0x30 = new MV();
    final MV sp0x10 = new MV();
    GsGetLws(shadow.modelParts_00[0].coord2_04, sp0x30, sp0x10);
    GsSetLightMatrix(sp0x30);
    GTE.setTransforms(sp0x10);
    Renderer.renderDobj2(shadow.modelParts_00[0], true, 0);
    shadow.modelParts_00[0].coord2_04.flg--;
  }

  @Method(0x800ec4bcL)
  public static void allocateStageDarkeningStorage() {
    stageDarkening_800c6958 = new BattleStageDarkening1800();
  }

  @Method(0x800ec4f0L)
  public static void deallocateStageDarkeningStorage() {
    stageDarkening_800c6958 = null;
  }

  @Method(0x800ec51cL)
  public static void renderBattleStage(final BattleStage stage) {
    //LAB_800ec548
    for(int i = 0; i < 10; i++) {
      if(stage._618[i] != 0) {
        applyBattleStageTextureAnimations(stage, i);
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
        final MV ls = new MV();
        final MV lw = new MV();
        GsGetLws(part.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);
        Renderer.renderDobj2(part, true, 0);

        if(part.obj != null) {
          RENDERER.queueModel(part.obj, lw)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour);
        }
      }

      //LAB_800ec608
      partBit <<= 1;
    }

    //LAB_800ec618
  }

  @Method(0x800ec63cL)
  public static void applyStagePartAnimations(final BattleStage stage) {
    //LAB_800ec688
    for(int i = 0; i < stage.partCount_5dc; i++) {
      final ModelPartTransforms0c rotTrans = stage.rotTrans_5d8[0][i];
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
  public static void rotateBattleStage(final BattleStage stage) {
    stage.coord2_558.coord.rotationXYZ(stage.param_5a8.rotate);
    stage.coord2_558.flg = 0;
  }

  @Method(0x800ec774L)
  public static void applyInitialStageTransforms(final BattleStage stage, final TmdAnimationFile anim) {
    stage.rotTrans_5d4 = anim.partTransforms_10;
    stage.rotTrans_5d8 = anim.partTransforms_10;
    stage.partCount_5dc = anim.modelPartCount_0c;
    stage.totalFrames_5de = anim.totalFrames_0e;
    stage.animationState_5e0 = 0;
    applyStagePartAnimations(stage);
    stage.animationState_5e0 = 1;
    stage.remainingFrames_5e2 = stage.totalFrames_5de;
    stage.rotTrans_5d8 = stage.rotTrans_5d4;
  }

  @Method(0x800ec7e4L)
  public static Vector2f perspectiveTransformXyz(final Model124 model, final float x, final float y, final float z) {
    final MV ls = new MV();
    GsGetLs(model.coord2_14, ls);
    GTE.setTransforms(ls);

    GTE.perspectiveTransform(x, y, z);
    return new Vector2f(GTE.getScreenX(2), GTE.getScreenY(2));
  }

  @Method(0x800ec86cL)
  public static void FUN_800ec86c(final BattleStage stage, final int index) {
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
  public static void backupStageClut(final FileData timFile) {
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

  @Method(0x800ec974L)
  public static void renderBttlModel(final Model124 model) {
    tmdGp0Tpage_1f8003ec = model.tpage_108;
    zOffset_1f8003e8 = model.zOffset_a0;

    //LAB_800ec9d0
    for(int i = 0; i < model.modelParts_00.length; i++) {
      if((model.partInvisible_f4 & 1L << i) == 0) {
        final ModelPart10 s2 = model.modelParts_00[i];
        final MV lw = new MV();
        final MV ls = new MV();
        GsGetLws(s2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);
        Renderer.renderDobj2(s2, true, 0);

        if(model.modelParts_00[i].obj != null) {
          RENDERER.queueModel(model.modelParts_00[i].obj, lw)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour);
        }
      }
    }

    //LAB_800eca58
    if(model.shadowType_cc != 0) {
      renderBttlShadow(model);
    }

    //LAB_800eca70
  }

  @ScriptDescription("Copies a block of vram from one position to another")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destX", description = "The destination X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destY", description = "The destination Y coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The block width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The block height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceX", description = "The source X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceY", description = "The source Y coordinate")
  @Method(0x800ee210L)
  public static FlowControl scriptCopyVram(final RunningScript<?> script) {
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(script.params_20[4].get(), script.params_20[5].get(), script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get() / 4, (short)script.params_20[3].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ee2acL)
  public static FlowControl scriptSetBentZOffset(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.zOffset_a0 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's scale uniformly")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scale", description = "The uniform scale (12-bit fixed-point)")
  @Method(0x800ee2e4L)
  public static FlowControl scriptSetBentScaleUniform(final RunningScript<?> script) {
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
  public static FlowControl scriptSetBentScale(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.coord2_14.transforms.scale.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type to 2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee384L)
  public static FlowControl FUN_800ee384(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 2;
    bent.model_148.modelPartWithShadowIndex_cd = -1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type to 3, used when player combat script is initialized, second param is based on char ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartAttachmentIndex", description = "The model part index to attach the shadow to")
  @Method(0x800ee3c0L)
  public static FlowControl FUN_800ee3c0(final RunningScript<?> script) {
    final BattleEntity27c v1 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    v1.model_148.shadowType_cc = 3;
    v1.model_148.modelPartWithShadowIndex_cd = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, sets shadow type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800ee408L)
  public static FlowControl FUN_800ee408(final RunningScript<?> script) {
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
  public static FlowControl scriptDisableBentShadow(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowType_cc = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a battle entity's shadow size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The shadow's X size (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The shadow's Z size (12-bit fixed-point)")
  @Method(0x800ee49cL)
  public static FlowControl scriptSetBentShadowSize(final RunningScript<?> script) {
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
  public static FlowControl scriptSetBentShadowOffset(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bent.model_148.shadowOffset_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Darkens the combat stage (for things like counter-attacks, certain dragoon transformations, etc.)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "intensity", description = "How intense the darkening effect should be")
  @Method(0x800ee548L)
  public static FlowControl scriptApplyScreenDarkening(final RunningScript<?> script) {
    applyScreenDarkening(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of model parts in the battle stage's model")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "count", description = "The number of model parts")
  @Method(0x800ee574L)
  public static FlowControl scriptGetStageNobj(final RunningScript<?> script) {
    script.params_20[0].set(stage_800bda0c.dobj2s_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Shows the battle stage's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @Method(0x800ee594L)
  public static FlowControl scriptShowStageModelPart(final RunningScript<?> a0) {
    stage_800bda0c.flags_5e4 |= 0x1 << a0.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Hides the battle stage's model part")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @Method(0x800ee5c0L)
  public static FlowControl scriptHideStageModelPart(final RunningScript<?> a0) {
    stage_800bda0c.flags_5e4 &= ~(0x1 << a0.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the battle stage model's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset")
  @Method(0x800ee5f0L)
  public static FlowControl scriptSetStageZ(final RunningScript<?> script) {
    stage_800bda0c.z_5e8 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800ee610L)
  public static void initBattleMenu() {
    countCombatUiFilesLoaded_800c6cf4.set(0);
    spellAndItemMenu_800c6b60 = new SpellAndItemMenuA4(hud);
    battleMenu_800c6c34 = new BattleMenuStruct58(hud);

    hud.clear();
    spellAndItemMenu_800c6b60.clearSpellAndItemMenu();
    battleMenu_800c6c34.clear();

    monsterCount_800c6b9c.set(0);
    itemTargetAll_800c69c8.set(false);
    itemTargetType_800c6b68.set(0);

    //LAB_800ee764
    for(int combatantIndex = 0; combatantIndex < 9; combatantIndex++) {
      monsterBents_800c6b78.get(combatantIndex).set(-1);
      currentEnemyNames_800c69d0[combatantIndex] = null;
    }

    //LAB_800ee7b0
    for(int monsterSlot = 0; monsterSlot < 3; monsterSlot++) {
      //LAB_800ee7b8
      melbuMonsterNames_800c6ba8[monsterSlot] = null;
    }

    usedRepeatItems_800c6c3c.clear();

    battleMenu_800c6c34._800c697e = 0;
    battleMenu_800c6c34._800c6980 = 0;
    dragoonSpaceElement_800c6b64 = null;

    //LAB_800ee894
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      spGained_800bc950[charSlot] = 0;
    }

    sortItems();
    prepareItemList();
  }

  @Method(0x800ee8c4L)
  public static void battleHudTexturesLoadedCallback(final List<FileData> files) {
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
        countCombatUiFilesLoaded_800c6cf4.add(1);
      }
    }
    //LAB_800eeaac
  }

  @Method(0x800eeaecL)
  public static void updateGameStateAndDeallocateMenu() {
    //LAB_800eeb10
    //LAB_800eebb4
    //LAB_800eebd8
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final PlayerBattleEntity bent = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;
      final CharacterData2c charData = gameState_800babc8.charData_32c[bent.charId_272];

      //LAB_800eec10
      charData.hp_08 = java.lang.Math.max(1, bent.stats.getStat(CoreMod.HP_STAT.get()).getCurrent());

      if((gameState_800babc8.goods_19c[0] & 0x1 << characterDragoonIndices_800c6e68[bent.charId_272]) != 0) {
        charData.mp_0a = bent.stats.getStat(CoreMod.MP_STAT.get()).getCurrent();
      }

      //LAB_800eec78
      if(bent.charId_272 == 0 && (gameState_800babc8.goods_19c[0] & 0x1 << characterDragoonIndices_800c6e68[9]) != 0) {
        charData.mp_0a = bent.stats.getStat(CoreMod.MP_STAT.get()).getCurrent();
      }

      //LAB_800eecb8
      charData.status_10 = bent.status_0e & 0xc8;
      charData.sp_0c = bent.stats.getStat(CoreMod.SP_STAT.get()).getCurrent();
    }

    //LAB_800eed78
    for(final Item item : usedRepeatItems_800c6c3c) {
      giveItem(item);
    }

    usedRepeatItems_800c6c3c.clear();

    spellAndItemMenu_800c6b60.delete();
    spellAndItemMenu_800c6b60 = null;
    battleMenu_800c6c34.delete();
    battleMenu_800c6c34 = null;
    hud.deleteUiElements();
    hud.deleteFloatingTextDigits();
  }

  @Method(0x800ef28cL)
  public static void FUN_800ef28c() {
    loadCharacterStats();
    characterStatsLoaded_800be5d0 = true;

    //LAB_800ef31c
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      dragoonSpells_800c6960.get(charSlot).charIndex_00.set(-1);

      //LAB_800ef328
      for(int spellSlot = 0; spellSlot < 8; spellSlot++) {
        dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellSlot).set(-1);
      }
    }

    //LAB_800ef36c
    //LAB_800ef38c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final PlayerBattleEntity player = battleState_8006e398.charBents_e40[charSlot].innerStruct_00;
      final int[] spellIndices = new int[8];
      getUnlockedDragoonSpells(spellIndices, player.charId_272);
      dragoonSpells_800c6960.get(charSlot).charIndex_00.set(player.charId_272);

      //LAB_800ef3d8
      for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
        dragoonSpells_800c6960.get(charSlot).spellIndex_01.get(spellIndex).set(spellIndices[spellIndex]);
      }

      //LAB_800ef400
      final VitalsStat playerHp = player.stats.getStat(CoreMod.HP_STAT.get());
      final VitalsStat playerMp = player.stats.getStat(CoreMod.MP_STAT.get());
      final VitalsStat playerSp = player.stats.getStat(CoreMod.SP_STAT.get());

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
      player.stats.getStat(CoreMod.SPEED_STAT.get()).setRaw(stats.equipmentSpeed_86 + stats.bodySpeed_69);
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
      player._132 = stats.equipmentSpecial2Flag80_56;
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
  public static boolean checkHit(final int attackerIndex, final int defenderIndex, final AttackType attackType) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[attackerIndex].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[defenderIndex].innerStruct_00;
    final boolean isMonster = attacker instanceof MonsterBattleEntity;

    int effectAccuracy;
    if(attackType == AttackType.PHYSICAL) {
      setTempSpellStats(attacker);

      if(isMonster) {
        effectAccuracy = attacker.spell_94.accuracy_05;
      } else {
        //LAB_800f1bf4
        effectAccuracy = attacker.attackHit_3c;
      }
    } else if(attackType == AttackType.DRAGOON_MAGIC_STATUS_ITEMS) {
      //LAB_800f1c08
      setTempSpellStats(attacker);
      effectAccuracy = attacker.spell_94.accuracy_05;
    } else {
      //LAB_800f1c38
      setTempItemMagicStats(attacker);
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
        setTempSpellStats(attacker);
      }
    }

    //LAB_800f1d28
    if((attacker.getStat(attackType.alwaysHitStat) & attackType.alwaysHitMask) != 0) {
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
  public static int calculateMagicDamage(final BattleEntity27c attacker, final BattleEntity27c defender, final int magicType) {
    // Stat mod item
    if(magicType == 0 && attacker.item_d4.type_0b != 0) {
      //LAB_800f2404
      //LAB_800f2410
      int s1;
      // HP, MP, SP, revive, cure status, cause status
      for(s1 = 0; s1 < 8; s1++) {
        if((attacker.item_d4.type_0b & 0x80 >> s1) != 0) {
          break;
        }
      }

      //LAB_800f2430
      final int value = switch(s1) {
        case 0 -> {
          //LAB_800f2454
          attacker.status_0e |= 0x800;
          yield defender.stats.getStat(CoreMod.HP_STAT.get()).getMax();
        }

        case 1 -> {
          //LAB_800f2464
          attacker.status_0e |= 0x800;
          yield defender.stats.getStat(CoreMod.MP_STAT.get()).getMax();
        }

        //LAB_800f2478
        case 6 -> defender.stats.getStat(CoreMod.HP_STAT.get()).getMax();

        //LAB_800f2484
        case 7 -> defender.stats.getStat(CoreMod.MP_STAT.get()).getMax();

        //LAB_800f2490
        default -> 0;
      };

      //LAB_800f2494
      //LAB_800f24bc
      return value * attacker.item_d4.percentage_09 / 100;
    }

    //LAB_800f2140
    int damage;
    if(attacker.spell_94 != null && (attacker.spell_94.flags_01 & 0x4) != 0) {
      damage = defender.stats.getStat(CoreMod.HP_STAT.get()).getMax() * attacker.spell_94.multi_04 / 100;

      final List<BattleEntity27c> targets = new ArrayList<>();
      if((attacker.spell_94.targetType_00 & 0x8) != 0) { // Attack all
        if(attacker instanceof PlayerBattleEntity) {
          for(int i = 0; i < charCount_800c677c.get(); i++) {
            targets.add(battleState_8006e398.charBents_e40[i].innerStruct_00);
          }
        } else {
          for(int i = 0; i < aliveMonsterCount_800c6758.get(); i++) {
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
      final Element attackElement = magicType == 1 ? attacker.spell_94.element_08 : attacker.item_d4.element_01;
      final AttackType attackType = magicType == 1 ? AttackType.DRAGOON_MAGIC_STATUS_ITEMS : AttackType.ITEM_MAGIC;

      //LAB_800f2238
      damage = attacker.calculateMagicDamage(defender, magicType);
      damage = attackElement.adjustAttackingElementalDamage(attackType, damage, defender.getElement());
      damage = defender.getElement().adjustDefendingElementalDamage(attackType, damage, attackElement);
      damage = adjustDamageForPower(damage, attacker.powerMagicAttack_b6, defender.powerMagicDefence_ba);

      if(dragoonSpaceElement_800c6b64 != null) {
        damage = attackElement.adjustDragoonSpaceDamage(attackType, damage, dragoonSpaceElement_800c6b64);
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
  public static FlowControl scriptPhysicalAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    final int damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.PHYSICAL, CoreMod.PHYSICAL_DAMAGE_FORMULA.calculate(attacker, defender))).damage;

    script.params_20[2].set(damage);
    script.params_20[3].set(determineAttackSpecialEffects(attacker, defender, AttackType.PHYSICAL));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's magic or status attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spellId", description = "The attacker's spell ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2694L)
  public static FlowControl scriptDragoonMagicStatusItemAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.spellId_4e = script.params_20[2].get();

    attacker.clearTempWeaponAndSpellStats();
    setTempSpellStats(attacker);

    int damage = calculateMagicDamage(attacker, defender, 1);
    damage = applyMagicDamageMultiplier(attacker, damage, 0);
    damage = java.lang.Math.max(1, damage);

    //LAB_800f272c
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.DRAGOON_MAGIC_STATUS_ITEMS);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.spell_94.element_08);
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS, damage)).damage;

    //LAB_800f27ec
    script.params_20[3].set(damage);
    script.params_20[4].set(determineAttackSpecialEffects(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's item attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2838L)
  public static FlowControl scriptItemMagicAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.clearTempWeaponAndSpellStats();
    setTempItemMagicStats(attacker);

    int damage = calculateMagicDamage(attacker, defender, 0);
    damage = applyMagicDamageMultiplier(attacker, damage, 1);
    damage = java.lang.Math.max(1, damage);

    //LAB_800f28c8
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.ITEM_MAGIC);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.item_d4.element_01);
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.ITEM_MAGIC, damage)).damage;

    //LAB_800f2970
    script.params_20[3].set(damage);
    script.params_20[4].set(determineAttackSpecialEffects(attacker, defender, AttackType.ITEM_MAGIC));
    applyItemSpecialEffects(attacker, defender);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives SP to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to add")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "total", description = "The total SP after adding the amount requested")
  @Method(0x800f43dcL)
  public static FlowControl scriptGiveSp(final RunningScript<?> script) {
    //LAB_800f43f8
    //LAB_800f4410
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(battleState_8006e398.charBents_e40[charSlot].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f4430
    final PlayerBattleEntity player = (PlayerBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() + script.params_20[1].get());
    spGained_800bc950[charSlot] += script.params_20[1].get();

    //LAB_800f4500
    script.params_20[2].set(sp.getCurrent());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Consumes SP from a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to take away")
  @Method(0x800f4518L)
  public static FlowControl scriptConsumeSp(final RunningScript<?> script) {
    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < charCount_800c677c.get(); i++) {
      if(battleState_8006e398.charBents_e40[i].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f456c
    final PlayerBattleEntity player = (PlayerBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() - script.params_20[2].get());

    if(sp.getCurrent() == 0) {
      hud.clearFullSpFlags(i);
    }

    //LAB_800f45f8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, this might handle players selecting an attack target")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemOrSpellId", description = "The item or spell ID selected")
  @Method(0x800f4600L)
  public static FlowControl FUN_800f4600(final RunningScript<?> script) {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    int itemOrSpellId = menu.itemOrSpellId_1c;
    if(menu.charIndex_08 == 8 && menu.menuType_0a == 1) {
      if(itemOrSpellId == 10) {
        itemOrSpellId = 65;
      }

      //LAB_800f46ec
      if(itemOrSpellId == 11) {
        itemOrSpellId = 66;
      }

      //LAB_800f46f8
      if(itemOrSpellId == 12) {
        itemOrSpellId = 67;
      }
    }

    //LAB_800f4704
    //LAB_800f4708
    script.params_20[0].set(menu._a0);
    script.params_20[1].set(battleMenu_800c6c34.target_48);
    script.params_20[2].set(itemOrSpellId);

    //LAB_800f4770
    PlayerBattleEntity playerBent = null;
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      playerBent = battleState_8006e398.charBents_e40[i].innerStruct_00;

      if(playerBent.charId_272 == menu.charIndex_08) {
        break;
      }
    }

    //LAB_800f47ac
    playerBent.spellId_4e = itemOrSpellId;

    if(menu._a0 == 1 && menu.menuType_0a == 0) {
      //LAB_800f47e4
      for(int i = 0; i < 17; i++) {
        if(targetAllItemIds_800c7124[i] == itemOrSpellId + 0xc0) {
          //LAB_800f4674
          script.params_20[1].set(-1);
          break;
        }
      }
    }

    //LAB_800f4800
    //LAB_800f4804
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the item/spell attack target")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetMode")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @Method(0x800f480cL)
  public static FlowControl scriptGetItemOrSpellAttackTarget(final RunningScript<?> script) {
    BattleEntity27c a1 = null;
    final int[] sp0x10 = {0, 0, 1, 0, 2, 1, 1, 1};

    int targetMode = script.params_20[0].get();

    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    //LAB_800f489c
    for(int a0 = 0; a0 < charCount_800c677c.get(); a0++) {
      a1 = battleState_8006e398.charBents_e40[a0].innerStruct_00;

      if(menu.charIndex_04 == a1.charId_272) {
        break;
      }
    }

    //LAB_800f48d8
    if((a1.specialEffectFlag_14 & 0x8) != 0) { // "Attack all"
      targetMode = 3;
    }

    //LAB_800f48f4
    int ret = menu.handleTargeting(sp0x10[targetMode * 2], sp0x10[targetMode * 2 + 1] != 0);
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

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public static Obj buildBattleMenuBackground(final String name, final BattleMenuBackgroundUvMetrics04 menuBackgroundMetrics, final int x, final int y, final int w, final int h, final int baseClutOffset, @Nullable final Translucency transMode, final int uvShiftType) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    setGpuPacketParams(builder, x, y, 0, 0, w, h, false);

    // Modified 1 and 3 from retail to properly align bottom row of pixels
    if(uvShiftType == 0) {
      //LAB_800f7628
      builder
        .uv(menuBackgroundMetrics.u_00, menuBackgroundMetrics.v_01)
        .uvSize(menuBackgroundMetrics.w_02, menuBackgroundMetrics.h_03);
    } else if(uvShiftType == 1) {
      //LAB_800f7654
      builder
        .uv(menuBackgroundMetrics.u_00, menuBackgroundMetrics.v_01 + menuBackgroundMetrics.h_03)
        .uvSize(menuBackgroundMetrics.w_02, -menuBackgroundMetrics.h_03);
      //LAB_800f7610
    } else if(uvShiftType == 2) {
      //LAB_800f7680
      builder
        .uv(menuBackgroundMetrics.u_00 + menuBackgroundMetrics.w_02 - 1, menuBackgroundMetrics.v_01)
        .uvSize(-menuBackgroundMetrics.w_02, menuBackgroundMetrics.h_03);
    } else if(uvShiftType == 3) {
      //LAB_800f76d4
      builder
        .uv(menuBackgroundMetrics.u_00 + menuBackgroundMetrics.w_02 - 1, menuBackgroundMetrics.v_01 + menuBackgroundMetrics.h_03)
        .uvSize(-menuBackgroundMetrics.w_02, -menuBackgroundMetrics.h_03);
    }

    //LAB_800f7724
    //LAB_800f772c
    setGpuPacketClutAndTpageAndQueue(builder, baseClutOffset, transMode);
    return builder.build();
  }

  @Method(0x800f7a74L)
  public static void setTempItemMagicStats(final BattleEntity27c bent) {
    //LAB_800f7a98
    bent.item_d4 = itemStats_8004f2ac[bent.itemId_52];
    bent._ec = 0;
    bent._ee = 0;
    bent._f0 = 0;
    bent._f2 = 0;
  }

  @Method(0x800f7b68L)
  public static void setTempSpellStats(final BattleEntity27c bent) {
    //LAB_800f7b8c
    if(bent.spellId_4e != -1 && bent.spellId_4e <= 127) {
      bent.spell_94 = EVENTS.postEvent(new SpellStatsEvent(bent.spellId_4e, spellStats_800fa0b8[bent.spellId_4e])).spell;
    } else {
      if(bent.spellId_4e > 127) {
        LOGGER.error("Retail bug: spell index out of bounds (%d). This is known to happen during Shana/Miranda's dragoon attack.", bent.spellId_4e);
      }

      bent.spell_94 = new SpellStats0c();
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static int determineAttackSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender, final AttackType attackType) {
    final BattleEntityStat[] statusEffectChances = {BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE}; // onHitStatusChance, statusChance, onHitStatusChance, statusChance, statusChance, onHitStatusChance
    final BattleEntityStat[] statusEffectStats = {BattleEntityStat.EQUIPMENT_ON_HIT_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS}; // onHitStatus, statusType, itemStatus, statusType, statusType, itemStatus
    final BattleEntityStat[] specialEffectStats = {BattleEntityStat.SPECIAL_EFFECT_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET}; // specialEffectFlag, spellFlags, itemTarget, spellFlags, spellFlags, itemTarget
    final int[] specialEffectMasks = {0x40, 0xf0, 0x80, 0xf0, 0xf0, 0x80};

    final boolean isAttackerMonster = attacker instanceof MonsterBattleEntity;

    final int index = (!isAttackerMonster ? 0 : 3) + attackType.ordinal(); //TODO

    final int effectChance = attackType == AttackType.ITEM_MAGIC ? 101 : attacker.getStat(statusEffectChances[index]);

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
        if(
          !isAttackerMonster && attackType != AttackType.DRAGOON_MAGIC_STATUS_ITEMS ||
            specialEffects == 0x80
        ) {
          effect = 0;
        } else if(specialEffects == 0x10) {
          // I think this is vestigial, there are no spells with flag 0x10
          throw new RuntimeException("Flag 0x10 found");
          //          if((attacker.spellElement_a4 & (isDefenderMonster ? defender.elementFlag_1c : characterElements_800c706c[defender.charIndex_272].get())) != 0) {
          //            effect = 0;
          //          }
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

  @Method(0x800f83c8L)
  public static void prepareItemList() {
    //LAB_800f83dc
    combatItems_800c6988.clear();

    //LAB_800f8420
    for(int itemSlot1 = 0; itemSlot1 < gameState_800babc8.items_2e9.size(); itemSlot1++) {
      final Item item = gameState_800babc8.items_2e9.get(itemSlot1);

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : combatItems_800c6988) {
        if(combatItem.item == item) {
          found = true;
          combatItem.count++;
          break;
        }
      }

      if(!found) {
        combatItems_800c6988.add(new CombatItem02(item));
      }
    }
  }

  @Method(0x800f84c8L)
  public static void loadBattleHudTextures() {
    loadDrgnDir(0, 4113, Bttl::battleHudTexturesLoadedCallback);
  }

  @Method(0x800f8670L)
  public static void loadMonster(final ScriptState<MonsterBattleEntity> state) {
    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      melbuMonsterNames_800c6ba8[i] = monsterNames_80112068[melbuMonsterNameIndices_800c6e90[i]];
    }

    final MonsterBattleEntity monster = state.innerStruct_00;
    currentEnemyNames_800c69d0[monsterCount_800c6b9c.get()] = monsterNames_80112068[monster.charId_272];

    //LAB_800eefa8
    monsterBents_800c6b78.get(monsterCount_800c6b9c.get()).set(state.index);
    monsterCount_800c6b9c.incr();

    //LAB_800eefcc
    final MonsterStats1c monsterStats = monsterStats_8010ba98[monster.charId_272];

    final MonsterStatsEvent statsEvent = EVENTS.postEvent(new MonsterStatsEvent(monster.charId_272));

    final VitalsStat monsterHp = monster.stats.getStat(CoreMod.HP_STAT.get());
    monsterHp.setCurrent(statsEvent.hp);
    monsterHp.setMaxRaw(statsEvent.maxHp);
    monster.specialEffectFlag_14 = statsEvent.specialEffectFlag;
    //    monster.equipmentType_16 = 0;
    monster.equipment_02_18 = 0;
    monster.equipmentEquipableFlags_1a = 0;
    monster.displayElement_1c = statsEvent.elementFlag;
    monster.equipment_05_1e = monsterStats._0e;
    monster.equipmentElementalImmunity_22.set(statsEvent.elementalImmunityFlag);
    monster.equipmentStatusResist_24 = statsEvent.statusResistFlag;
    monster.equipment_09_26 = 0;
    monster.equipmentAttack1_28 = 0;
    monster._2e = 0;
    monster.equipmentIcon_30 = 0;
    monster.stats.getStat(CoreMod.SPEED_STAT.get()).setRaw(statsEvent.speed);
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
    monster._84 = monsterStats._18;
    monster._86 = monsterStats._19;
    monster._88 = monsterStats._1a;
    monster._8a = monsterStats._1b;

    monster.damageReductionFlags_6e = monster.specialEffectFlag_14;
    monster._70 = monster.equipment_05_1e;
    monster.monsterElement_72 = monster.displayElement_1c;
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
  public static void applyItemSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender) {
    setTempItemMagicStats(attacker);

    final int turnCount = attacker != defender ? 3 : 4;

    if(attacker.item_d4.powerDefence != 0) {
      defender.powerDefence_b8 = attacker.item_d4.powerDefence;
      defender.powerDefenceTurns_b9 = turnCount;
    }

    if(attacker.item_d4.powerMagicDefence != 0) {
      defender.powerMagicDefence_ba = attacker.item_d4.powerMagicDefence;
      defender.powerMagicDefenceTurns_bb = turnCount;
    }

    if(attacker.item_d4.powerAttack != 0) {
      defender.powerAttack_b4 = attacker.item_d4.powerAttack;
      defender.powerAttackTurns_b5 = turnCount;
    }

    if(attacker.item_d4.powerMagicAttack != 0) {
      defender.powerMagicAttack_b6 = attacker.item_d4.powerMagicAttack;
      defender.powerMagicAttackTurns_b7 = turnCount;
    }

    if(attacker.item_d4.powerAttackHit != 0) {
      defender.tempAttackHit_bc = attacker.item_d4.powerAttackHit;
      defender.tempAttackHitTurns_bd = turnCount;
    }

    if(attacker.item_d4.powerMagicAttackHit != 0) {
      defender.tempMagicHit_be = attacker.item_d4.powerMagicAttackHit;
      defender.tempMagicHitTurns_bf = turnCount;
    }

    if(attacker.item_d4.powerAttackAvoid != 0) {
      defender.tempAttackAvoid_c0 = attacker.item_d4.powerAttackAvoid;
      defender.tempAttackAvoidTurns_c1 = turnCount;
    }

    if(attacker.item_d4.powerMagicAttackAvoid != 0) {
      defender.tempMagicAvoid_c2 = attacker.item_d4.powerMagicAttackAvoid;
      defender.tempMagicAvoidTurns_c3 = turnCount;
    }

    if(attacker.item_d4.physicalImmunity) {
      defender.tempPhysicalImmunity_c4 = 1;
      defender.tempPhysicalImmunityTurns_c5 = turnCount;
    }

    if(attacker.item_d4.magicalImmunity) {
      defender.tempMagicalImmunity_c6 = 1;
      defender.tempMagicalImmunityTurns_c7 = turnCount;
    }

    if(attacker.item_d4.speedDown != 0) {
      defender.stats.getStat(CoreMod.SPEED_STAT.get()).addMod(new TurnBasedPercentileBuff(attacker.item_d4.speedDown, turnCount));
    }

    if(attacker.item_d4.speedUp != 0) {
      defender.stats.getStat(CoreMod.SPEED_STAT.get()).addMod(new TurnBasedPercentileBuff(attacker.item_d4.speedUp, turnCount));
    }

    if(defender instanceof final PlayerBattleEntity playerDefender) {
      if(attacker.item_d4.spPerPhysicalHit != 0) {
        playerDefender.tempSpPerPhysicalHit_cc = attacker.item_d4.spPerPhysicalHit;
        playerDefender.tempSpPerPhysicalHitTurns_cd = turnCount;
      }

      if(attacker.item_d4.mpPerPhysicalHit != 0) {
        playerDefender.tempMpPerPhysicalHit_ce = attacker.item_d4.mpPerPhysicalHit;
        playerDefender.tempMpPerPhysicalHitTurns_cf = turnCount;
      }

      if(attacker.item_d4.spPerMagicalHit != 0) {
        playerDefender.tempSpPerMagicalHit_d0 = attacker.item_d4.spPerMagicalHit;
        playerDefender.tempSpPerMagicalHitTurns_d1 = turnCount;
      }

      if(attacker.item_d4.mpPerMagicalHit != 0) {
        playerDefender.tempMpPerMagicalHit_d2 = attacker.item_d4.mpPerMagicalHit;
        playerDefender.tempMpPerMagicalHitTurns_d3 = turnCount;
      }
    }

    defender.recalculateSpeedAndPerHitStats();
  }

  @Method(0x800f8cd8L)
  public static Obj buildBattleMenuElement(final String name, final int x, final int y, final int u, final int v, final int w, final int h, final int clut, @Nullable final Translucency transMode) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    setGpuPacketParams(builder, x, y, u, v, w, h, true);
    setGpuPacketClutAndTpageAndQueue(builder, clut, transMode);

    return builder.build();
  }

  @Method(0x800f8facL)
  public static void setGpuPacketParams(final QuadBuilder cmd, final int x, final int y, final int u, final int v, final int w, final int h, final boolean textured) {
    cmd
      .pos(x, y, 0.0f)
      .size(w, h);

    if(textured) {
      cmd
        .uv(u, v);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void setGpuPacketClutAndTpageAndQueue(final QuadBuilder cmd, int clut, @Nullable final Translucency transparencyMode) {
    final int clutIndex;
    if(clut >= 0x80) {
      clutIndex = 1;
      clut -= 0x80;
    } else {
      //LAB_800f9080
      clutIndex = 0;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    final int clutX = battleUiElementClutVramXy_800c7114[clutIndex].x + clut & 0x3f0;
    final int clutY = battleUiElementClutVramXy_800c7114[clutIndex].y + clut % 16;

    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      cmd.translucency(transparencyMode);
    }
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
  public static int applyMagicDamageMultiplier(final BattleEntity27c attacker, final int damage, final int magicType) {
    final int damageMultiplier;
    if(magicType == 0) {
      damageMultiplier = spellStats_800fa0b8[attacker.spellId_4e].damageMultiplier_03;
    } else {
      //LAB_800f949c
      damageMultiplier = attacker.item_d4.damageMultiplier_02;
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
  public static FlowControl scriptCheckPhysicalHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.PHYSICAL) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's spell or status attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9618L)
  public static FlowControl scriptCheckSpellOrStatusHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.DRAGOON_MAGIC_STATUS_ITEMS) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's item magic attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9660L)
  public static FlowControl scriptCheckItemHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.ITEM_MAGIC) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f96a8L)
  public static FlowControl scriptSetTempSpellStats(final RunningScript<?> script) {
    setTempSpellStats((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800f96d4L)
  public static FlowControl scriptGetBentPos2(final RunningScript<?> script) {
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
  public static FlowControl scriptAddFloatingNumber(final RunningScript<?> script) {
    hud.addFloatingNumber(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialized the battle menu for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "menuType", description = "0 = items, 1 = spells, 2 = ?")
  @Method(0x800f97d8L)
  public static FlowControl scriptInitSpellAndItemMenu(final RunningScript<?> script) {
    spellAndItemMenu_800c6b60.clearSpellAndItemMenu();
    spellAndItemMenu_800c6b60.initSpellAndItemMenu(((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).charId_272, (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Render recovery amount for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount recovered")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "colourIndex", description = "Which colour to use (indices are unknown)")
  @Method(0x800f984cL)
  public static FlowControl scriptRenderRecover(final RunningScript<?> script) {
    hud.addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9884L)
  public static FlowControl scriptSetTempItemMagicStats(final RunningScript<?> script) {
    setTempItemMagicStats((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes a specific (or random) item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID (or -1 to take a random item)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemTaken", description = "The item ID that was taken (or -1 if none could be taken)")
  @Method(0x800f98b0L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    int itemId = script.params_20[0].get();

    if(gameState_800babc8.items_2e9.isEmpty()) {
      script.params_20[1].set(-1);
      return FlowControl.CONTINUE;
    }

    Item item = null;
    if(itemId == -1) {
      item = gameState_800babc8.items_2e9.get((simpleRand() * gameState_800babc8.items_2e9.size()) >> 16);
      itemId = LodMod.idItemMap.getInt(item.getRegistryId());

      //LAB_800f996c
      for(int i = 0; i < 10; i++) {
        if(itemId == protectedItems_800c72cc[i]) {
          //LAB_800f999c
          item = null;
          itemId = -1;
          break;
        }
      }
    }

    //LAB_800f9988
    //LAB_800f99a4
    if(item != null && takeItemId(item) != 0) {
      itemId = -1;
    }

    //LAB_800f99c0
    script.params_20[1].set(itemId);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives a specific item to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemGiven", description = "The item ID that was given (or -1 if none could be given)")
  @Method(0x800f99ecL)
  public static FlowControl scriptGiveItem(final RunningScript<?> script) {
    final int givenItem;

    final int itemId = script.params_20[0].get();
    final boolean given;
    if(itemId < 192) {
      given = giveEquipment(REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(itemId)).get());
    } else {
      given = giveItem(REGISTRIES.items.getEntry(LodMod.itemIdMap.get(itemId - 192)).get());
    }

    if(given) {
      givenItem = itemId;
    } else {
      givenItem = -1;
    }

    //LAB_800f9a2c
    script.params_20[1].set(givenItem);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to targeting")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetType", description = "0 = characters, 1 = monsters, 2 = any")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index")
  @Method(0x800f9a50L)
  public static FlowControl FUN_800f9a50(final RunningScript<?> script) {
    final int targetType = script.params_20[0].get();
    final int targetBent = script.params_20[1].get();

    final ScriptState<? extends BattleEntity27c>[] bents;
    final int count;
    if(targetType == 0) {
      bents = battleState_8006e398.charBents_e40;
      count = charCount_800c677c.get();
    } else if(targetType == 1) {
      //LAB_800f9a94
      bents = battleState_8006e398.aliveMonsterBents_ebc;
      count = aliveMonsterCount_800c6758.get();
    } else {
      //LAB_800f9aac
      bents = battleState_8006e398.aliveBents_e78;
      count = aliveBentCount_800c669c.get();
    }

    //LAB_800f9abc
    //LAB_800f9adc
    for(int i = 0; i < count; i++) {
      if(targetBent == bents[i].index) {
        if(targetType == 0) {
          battleMenu_800c6c34._800c6980 = i;
        } else if(targetType == 1) {
          //LAB_800f9b0c
          battleMenu_800c6c34._800c697e = i;
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
  public static FlowControl scriptIsFloatingNumberOnScreen(final RunningScript<?> script) {
    //LAB_800f9b3c
    //LAB_800f9b64
    script.params_20[0].set(hud.isFloatingNumberOnScreen() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the active dragoon space element")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID whose element should be used")
  @Method(0x800f9b78L)
  public static FlowControl scriptSetDragoonSpaceElementIndex(final RunningScript<?> script) {
    final int characterId = script.params_20[0].get();

    dragoonSpaceElement_800c6b64 = null;

    if(characterId != -1) {
      if(characterId == 9) { //TODO stupid special case handling for DD Dart
        dragoonSpaceElement_800c6b64 = CoreMod.DIVINE_ELEMENT.get();
      } else {
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          if(battleState_8006e398.charBents_e40[i].innerStruct_00.charId_272 == characterId) {
            dragoonSpaceElement_800c6b64 = battleState_8006e398.charBents_e40[i].innerStruct_00.element;
            break;
          }
        }
      }
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800f9b94L)
  public static FlowControl FUN_800f9b94(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9bd4L)
  public static FlowControl FUN_800f9bd4(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9c00L)
  public static FlowControl FUN_800f9c00(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  private static UiBox scriptUi;

  @ScriptDescription("Renders the battle HUD background")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The height")
  @Method(0x800f9c2cL)
  public static FlowControl scriptRenderBattleHudBackground(final RunningScript<?> script) {
    final int colourIndex = script.params_20[4].get();
    final int r = textboxColours_800c6fec[colourIndex][0];
    final int g = textboxColours_800c6fec[colourIndex][1];
    final int b = textboxColours_800c6fec[colourIndex][2];

    // This is kinda dumb since we'll have to upload a new box each frame, but there isn't a great
    // way to deal with it. Maybe check to see if any of the params have changed before deleting?

    if(scriptUi != null) {
      scriptUi.delete();
    }

    scriptUi = new UiBox(
      "Scripted Battle UI",
      (short)script.params_20[0].get() - script.params_20[2].get() / 2,
      (short)script.params_20[1].get() - script.params_20[3].get() / 2,
      (short)script.params_20[2].get(),
      (short)script.params_20[3].get()
    );

    scriptUi.render(r / 255.0f, g / 255.0f, b / 255.0f);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, disables menu icons if certain flags are set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "iconIndicesBitset", description = "The icons to disable if their flag matches a certain value (unknown)")
  @Method(0x800f9cacL)
  public static FlowControl scriptSetDisabledMenuIcons(final RunningScript<?> script) {
    final int disabledIconsBitset = script.params_20[0].get();
    battleMenu_800c6c34.setDisabledIcons(disabledIconsBitset);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Called after any battle entity finishes its turn, ticks temporary stats and calls turnFinished on custom battle entities")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9d7cL)
  public static FlowControl scriptFinishBentTurn(final RunningScript<?> script) {
    final int bentIndex = script.params_20[0].get();
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;

    bent.turnFinished();
    bent.recalculateSpeedAndPerHitStats();
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9e50L)
  public static PlayerBattleEntity setActiveCharacterSpell(final int spellId) {
    final int charIndex = spellAndItemMenu_800c6b60.charIndex_08;

    //LAB_800f9e8c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final ScriptState<PlayerBattleEntity> playerState = battleState_8006e398.charBents_e40[charSlot];
      final PlayerBattleEntity player = playerState.innerStruct_00;

      if(charIndex == player.charId_272) {
        //LAB_800f9ec8
        player.spellId_4e = spellId;
        setTempSpellStats(player);
        return player;
      }
    }

    throw new IllegalStateException();
  }
}
