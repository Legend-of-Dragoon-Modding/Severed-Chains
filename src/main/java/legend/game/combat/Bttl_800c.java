package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.QuadFunctionRef;
import legend.core.memory.types.QuintConsumerRef;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.SItem;
import legend.game.Scus94491BpeSegment;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.types.BattleCamera;
import legend.game.combat.types.BattleDisplayStats144;
import legend.game.combat.types.BattleLightStruct64;
import legend.game.combat.types.BattleMenuStruct58;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStage;
import legend.game.combat.types.BattleStageDarkening1800;
import legend.game.combat.types.BattleStruct18cb0;
import legend.game.combat.types.BattleStruct24_2;
import legend.game.combat.types.BattleStruct3c;
import legend.game.combat.types.BattleStructEf4;
import legend.game.combat.types.BttlLightStruct84;
import legend.game.combat.types.BttlScriptData6cSub0e;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.BttlStruct08;
import legend.game.combat.types.BttlStructa4;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.CombatantStruct1a8_c;
import legend.game.combat.types.CtmdUnpackingData50;
import legend.game.combat.types.DragoonSpells09;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.FloatingNumberC4;
import legend.game.combat.types.MersenneTwisterSeed;
import legend.game.combat.types.PotionEffect14;
import legend.game.combat.types.SpriteMetrics08;
import legend.game.combat.types.WeaponTrailEffect3c;
import legend.game.combat.types.WeaponTrailEffectSegment2c;
import legend.game.inventory.WhichMenu;
import legend.game.scripting.FlowControl;
import legend.game.scripting.IntParam;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.types.CharacterData2c;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.MrgFile;
import legend.game.types.SpellStats0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001af00;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.btldLoadEncounterSoundEffectsAndMusic;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.deferReallocOrFree;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment.getMallocSize;
import static legend.game.Scus94491BpeSegment.loadDir;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadMcq;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.realloc2;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setDepthResolution;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020308;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021520;
import static legend.game.Scus94491BpeSegment_8002.FUN_80029e04;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.deallocateModel;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.renderModel;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MoveImage;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040980;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.previousMainCallbackIndex_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeOut;
import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc910;
import static legend.game.Scus94491BpeSegment_800b._800bc914;
import static legend.game.Scus94491BpeSegment_800b._800bc918;
import static legend.game.Scus94491BpeSegment_800b._800bc91c;
import static legend.game.Scus94491BpeSegment_800b._800bc94c;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc968;
import static legend.game.Scus94491BpeSegment_800b._800bc974;
import static legend.game.Scus94491BpeSegment_800b._800bc97c;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemiesCount_800bc978;
import static legend.game.Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800d.FUN_800dabec;
import static legend.game.combat.Bttl_800d.FUN_800dd0d4;
import static legend.game.combat.Bttl_800d.FUN_800dd118;
import static legend.game.combat.Bttl_800e.FUN_800e8ffc;
import static legend.game.combat.Bttl_800e.FUN_800e9120;
import static legend.game.combat.Bttl_800e.FUN_800ec51c;
import static legend.game.combat.Bttl_800e.FUN_800ec744;
import static legend.game.combat.Bttl_800e.FUN_800ee610;
import static legend.game.combat.Bttl_800e.FUN_800ef9e4;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.allocateStageDarkeningStorage;
import static legend.game.combat.Bttl_800e.backupStageClut;
import static legend.game.combat.Bttl_800e.deallocateStageDarkeningStorage;
import static legend.game.combat.Bttl_800e.drawUiElements;
import static legend.game.combat.Bttl_800e.loadBattleHudDeff_;
import static legend.game.combat.Bttl_800e.loadStageTmd;
import static legend.game.combat.Bttl_800e.updateGameStateAndDeallocateMenu;
import static legend.game.combat.Bttl_800f.FUN_800f1a00;
import static legend.game.combat.Bttl_800f.FUN_800f417c;
import static legend.game.combat.Bttl_800f.FUN_800f60ac;
import static legend.game.combat.Bttl_800f.FUN_800f6134;
import static legend.game.combat.Bttl_800f.FUN_800f6330;
import static legend.game.combat.Bttl_800f.FUN_800f84c0;
import static legend.game.combat.Bttl_800f.FUN_800f8c38;
import static legend.game.combat.Bttl_800f.loadBattleHudTextures;
import static legend.game.combat.Bttl_800f.setFloatingNumCoordsAndRender;

public final class Bttl_800c {
  private Bttl_800c() { }

  public static final UnsignedShortRef _800c6690 = MEMORY.ref(2, 0x800c6690L, UnsignedShortRef::new);

  public static final UnsignedByteRef uniqueMonsterCount_800c6698 = MEMORY.ref(1, 0x800c6698L).cast(UnsignedByteRef::new);
  public static final IntRef _800c669c = MEMORY.ref(4, 0x800c669cL, IntRef::new);
  /** The number of {@link Scus94491BpeSegment_8005#combatants_8005e398}s */
  public static final Value combatantCount_800c66a0 = MEMORY.ref(4, 0x800c66a0L);
  public static final IntRef currentStage_800c66a4 = MEMORY.ref(4, 0x800c66a4L, IntRef::new);

  public static final IntRef _800c66a8 = MEMORY.ref(4, 0x800c66a8L, IntRef::new);

  public static final IntRef _800c66b0 = MEMORY.ref(4, 0x800c66b0L, IntRef::new);

  public static final Value _800c66b4 = MEMORY.ref(4, 0x800c66b4L);
  public static final BoolRef stageHasModel_800c66b8 = MEMORY.ref(1, 0x800c66b8L, BoolRef::new);
  public static final Value _800c66b9 = MEMORY.ref(1, 0x800c66b9L);

  public static ScriptState<BattleObject27c> scriptIndex_800c66bc;
  public static final Value _800c66c0 = MEMORY.ref(1, 0x800c66c0L);
  public static final Value _800c66c1 = MEMORY.ref(1, 0x800c66c1L);

  public static final Value _800c66c4 = MEMORY.ref(4, 0x800c66c4L);
  public static ScriptState<BattleObject27c> currentTurnBobj_800c66c8;
  public static final Value _800c66cc = MEMORY.ref(4, 0x800c66ccL);
  public static final IntRef _800c66d0 = MEMORY.ref(4, 0x800c66d0L, IntRef::new);
  public static final Value _800c66d4 = MEMORY.ref(1, 0x800c66d4L);

  public static ScriptFile script_800c66fc;

  public static final IntRef _800c6700 = MEMORY.ref(4, 0x800c6700L, IntRef::new);
  public static final IntRef _800c6704 = MEMORY.ref(4, 0x800c6704L, IntRef::new);

  public static final IntRef _800c6710 = MEMORY.ref(4, 0x800c6710L, IntRef::new);

  /** TODO this is a struct, added the int ref temporarily since it's referenced by the script engine */
  public static final ArrayRef<IntRef> intRef_800c6718 = MEMORY.ref(4, 0x800c6718L, ArrayRef.of(IntRef.class, 0x28 / 4, 4, IntRef::new));
  public static final Value _800c6718 = MEMORY.ref(4, 0x800c6718L);

  public static final Value _800c6724 = MEMORY.ref(4, 0x800c6724L);

  public static final Value _800c6740 = MEMORY.ref(4, 0x800c6740L);

  public static final IntRef _800c6748 = MEMORY.ref(4, 0x800c6748L, IntRef::new);
  public static ScriptState<Void> scriptState_800c674c;

  public static final IntRef _800c6754 = MEMORY.ref(4, 0x800c6754L, IntRef::new);
  public static final IntRef enemyCount_800c6758 = MEMORY.ref(4, 0x800c6758L, IntRef::new);
  public static final Value _800c675c = MEMORY.ref(4, 0x800c675cL);
  public static final IntRef _800c6760 = MEMORY.ref(4, 0x800c6760L, IntRef::new);
  public static final IntRef _800c6764 = MEMORY.ref(4, 0x800c6764L, IntRef::new);
  public static final IntRef monsterCount_800c6768 = MEMORY.ref(4, 0x800c6768L, IntRef::new);
  public static final IntRef _800c676c = MEMORY.ref(4, 0x800c676cL, IntRef::new);
  public static final IntRef _800c6770 = MEMORY.ref(4, 0x800c6770L, IntRef::new);
  public static final IntRef _800c6774 = MEMORY.ref(4, 0x800c6774L, IntRef::new);
  public static final IntRef _800c6778 = MEMORY.ref(4, 0x800c6778L, IntRef::new);
  /** The number of player chars in combat (i.e. 1-3) */
  public static final IntRef charCount_800c677c = MEMORY.ref(4, 0x800c677cL, IntRef::new);
  public static final IntRef _800c6780 = MEMORY.ref(4, 0x800c6780L, IntRef::new);

  public static final Pointer<CString> currentAddition_800c6790 = MEMORY.ref(4, 0x800c6790L, Pointer.deferred(1, CString.maxLength(30)));

  public static final MATRIX _800c6798 = MEMORY.ref(4, 0x800c6798L, MATRIX::new);
  public static final UnsignedIntRef _800c67b8 = MEMORY.ref(4, 0x800c67b8L, UnsignedIntRef::new);
  public static final IntRef screenOffsetX_800c67bc = MEMORY.ref(4, 0x800c67bcL, IntRef::new);
  public static final IntRef screenOffsetY_800c67c0 = MEMORY.ref(4, 0x800c67c0L, IntRef::new);
  public static final Value _800c67c4 = MEMORY.ref(4, 0x800c67c4L);
  public static final IntRef _800c67c8 = MEMORY.ref(4, 0x800c67c8L, IntRef::new);
  public static final IntRef _800c67cc = MEMORY.ref(4, 0x800c67ccL, IntRef::new);
  public static final IntRef _800c67d0 = MEMORY.ref(4, 0x800c67d0L, IntRef::new);

  public static final Value _800c67d4 = MEMORY.ref(4, 0x800c67d4L);
  public static final VECTOR _800c67d8 = MEMORY.ref(4, 0x800c67d8L, VECTOR::new);
  public static final Value _800c67e4 = MEMORY.ref(4, 0x800c67e4L);
  public static final Value _800c67e8 = MEMORY.ref(4, 0x800c67e8L);

  public static final BattleCamera camera_800c67f0 = new BattleCamera();

  public static final Value _800c6912 = MEMORY.ref(1, 0x800c6912L);
  public static final Value _800c6913 = MEMORY.ref(1, 0x800c6913L);
  public static ScriptState<BattleObject27c> scriptState_800c6914;
  public static final IntRef _800c6918 = MEMORY.ref(4, 0x800c6918L, IntRef::new);

  public static CtmdUnpackingData50 ctmdUnpackingData_800c6920;

  public static final Value _800c6928 = MEMORY.ref(4, 0x800c6928L);
  public static BttlLightStruct84[] lights_800c692c;
  public static BattleLightStruct64 _800c6930;

  public static BattleStruct24_2 _800c6938;
  public static DeffManager7cc deffManager_800c693c;
  /** Dunno what this is for, it's set to a pointer but unused. I removed the set for now. */
  public static final Value _800c6940 = MEMORY.ref(4, 0x800c6940L);
  public static TmdObjTable[] tmds_800c6944;
  public static SpriteMetrics08[] spriteMetrics_800c6948;

  public static final Pointer<BattleStageDarkening1800> stageDarkening_800c6958 = MEMORY.ref(4, 0x800c6958L, Pointer.deferred(4, BattleStageDarkening1800::new));
  public static final UnsignedShortRef stageDarkeningClutCount_800c695c = MEMORY.ref(2, 0x800c695cL, UnsignedShortRef::new);

  public static final ArrayRef<DragoonSpells09> dragoonSpells_800c6960 = MEMORY.ref(1, 0x800c6960L, ArrayRef.of(DragoonSpells09.class, 3, 9, DragoonSpells09::new));

  public static final Value _800c697c = MEMORY.ref(2, 0x800c697cL);
  public static final Value _800c697e = MEMORY.ref(2, 0x800c697eL);
  public static final Value _800c6980 = MEMORY.ref(2, 0x800c6980L);

  public static final Value _800c6988 = MEMORY.ref(1, 0x800c6988L);

  public static final Value _800c69c8 = MEMORY.ref(4, 0x800c69c8L);

  public static final ArrayRef<LodString> currentEnemyNames_800c69d0 = MEMORY.ref(2, 0x800c69d0L, ArrayRef.of(LodString.class, 9, 0x2c, LodString::new));

  public static final Pointer<ArrayRef<FloatingNumberC4>> floatingNumbers_800c6b5c = MEMORY.ref(4, 0x800c6b5cL, Pointer.deferred(4, ArrayRef.of(FloatingNumberC4.class, 12, 0xc4, FloatingNumberC4::new)));
  public static final Pointer<BttlStructa4> _800c6b60 = MEMORY.ref(4, 0x800c6b60L, Pointer.deferred(4, BttlStructa4::new));
  public static final Value _800c6b64 = MEMORY.ref(4, 0x800c6b64L);
  public static final Value _800c6b68 = MEMORY.ref(4, 0x800c6b68L);
  public static final Value _800c6b6c = MEMORY.ref(4, 0x800c6b6cL);
  public static final Value _800c6b70 = MEMORY.ref(2, 0x800c6b70L);

  public static final Value _800c6b78 = MEMORY.ref(4, 0x800c6b78L);

  public static final Value _800c6b9c = MEMORY.ref(4, 0x800c6b9cL);
  public static final Value _800c6ba0 = MEMORY.ref(1, 0x800c6ba0L);
  public static final Value _800c6ba1 = MEMORY.ref(1, 0x800c6ba1L);

  /** Uhh, contains the monsters that Melbu summons during his fight...? */
  public static final ArrayRef<LodString> _800c6ba8 = MEMORY.ref(2, 0x800c6ba8L, ArrayRef.of(LodString.class, 3, 0x2c, LodString::new));

  /** One per character slot */
  public static final Pointer<ArrayRef<BattleDisplayStats144>> displayStats_800c6c2c = MEMORY.ref(4, 0x800c6c2cL, Pointer.deferred(4, ArrayRef.of(BattleDisplayStats144.class, 3, 0x144, BattleDisplayStats144::new)));
  public static final Value _800c6c30 = MEMORY.ref(1, 0x800c6c30L);

  public static final Pointer<BattleMenuStruct58> battleMenu_800c6c34 = MEMORY.ref(4, 0x800c6c34L, Pointer.deferred(4, BattleMenuStruct58::new));
  public static final Value _800c6c38 = MEMORY.ref(4, 0x800c6c38L);
  public static final UnsignedShortRef usedRepeatItems_800c6c3c = MEMORY.ref(2, 0x800c6c3cL, UnsignedShortRef::new);

  public static final ArrayRef<BattleStruct3c> _800c6c40 = MEMORY.ref(2, 0x800c6c40L, ArrayRef.of(BattleStruct3c.class, 3, 0x3c, BattleStruct3c::new));

  public static final Value _800c6cf4 = MEMORY.ref(4, 0x800c6cf4L);

  public static final Value _800c6d94 = MEMORY.ref(2, 0x800c6d94L);
  public static final Value _800c6dac = MEMORY.ref(2, 0x800c6dacL);

  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#renderAdditionHitStarburst}</li>
   *   <li>{@link Bttl_800d#renderAdditionCompletedStarburst}</li>
   *   <li>{@link Bttl_800d#renderAdditionCompletedStarburst}</li>
   * </ol>
   */
  public static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] additionStarburstRenderers_800c6dc4 = new BiConsumer[3];
  static {
    additionStarburstRenderers_800c6dc4[0] = Bttl_800d::renderAdditionHitStarburst;
    additionStarburstRenderers_800c6dc4[1] = Bttl_800d::renderAdditionCompletedStarburst;
    additionStarburstRenderers_800c6dc4[2] = Bttl_800d::renderAdditionCompletedStarburst;
  }

  public static final GsF_LIGHT light_800c6ddc = MEMORY.ref(4, 0x800c6ddcL, GsF_LIGHT::new);

  public static final CString _800c6e18 = MEMORY.ref(7, 0x800c6e18L, CString::new);

  public static final ArrayRef<UnsignedShortRef> repeatItemIds_800c6e34 = MEMORY.ref(2, 0x800c6e34L, ArrayRef.of(UnsignedShortRef.class, 9, 2, UnsignedShortRef::new));

  public static final ArrayRef<DVECTOR> _800c6e48 = MEMORY.ref(2, 0x800c6e48L, ArrayRef.of(DVECTOR.class, 6, 4, DVECTOR::new));
  public static final ArrayRef<ShortRef> _800c6e60 = MEMORY.ref(2, 0x800c6e60L, ArrayRef.of(ShortRef.class, 4, 2, ShortRef::new));

  public static final ArrayRef<UnsignedIntRef> characterDragoonIndices_800c6e68 = MEMORY.ref(4, 0x800c6e68L, ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));

  public static final Value _800c6e90 = MEMORY.ref(4, 0x800c6e90L);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6e9c = MEMORY.ref(2, 0x800c6e9cL);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6ecc = MEMORY.ref(1, 0x800c6eccL);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6ef0 = MEMORY.ref(2, 0x800c6ef0L);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6f04 = MEMORY.ref(1, 0x800c6f04L);

  public static final Value _800c6f30 = MEMORY.ref(4, 0x800c6f30L);

  public static final Value _800c6f4c = MEMORY.ref(2, 0x800c6f4cL);

  public static final ArrayRef<ArrayRef<UnsignedByteRef>> _800c6fec = MEMORY.ref(1, 0x800c6fecL, ArrayRef.of(ArrayRef.classFor(UnsignedByteRef.class), 9, 3, ArrayRef.of(UnsignedByteRef.class, 3, 1, UnsignedByteRef::new)));

  public static final Value _800c7004 = MEMORY.ref(4, 0x800c7004L);

  public static final ArrayRef<ShortRef> _800c7008 = MEMORY.ref(2, 0x800c7008L, ArrayRef.of(ShortRef.class, 5, 2, ShortRef::new));

  public static final ArrayRef<UnsignedShortRef> _800c7014 = MEMORY.ref(2, 0x800c7014L, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> _800c7028 = MEMORY.ref(2, 0x800c7028L, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));
  public static final Value _800c703c = MEMORY.ref(4, 0x800c703cL);

  public static final ArrayRef<UnsignedShortRef> characterElements_800c706c = MEMORY.ref(2, 0x800c706cL, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));

  public static final Value _800c70a4 = MEMORY.ref(4, 0x800c70a4L);

  /** TODO array of shorts, 0x14 bytes total */
  public static final Value _800c70e0 = MEMORY.ref(2, 0x800c70e0L);

  /** TODO array of shorts, 0x1e bytes total */
  public static final Value _800c70f4 = MEMORY.ref(2, 0x800c70f4L);

  public static final Value _800c7114 = MEMORY.ref(2, 0x800c7114L);

  public static final Value _800c7124 = MEMORY.ref(2, 0x800c7124L);

  public static final Value _800c7190 = MEMORY.ref(1, 0x800c7190L);

  public static final Value _800c7192 = MEMORY.ref(1, 0x800c7192L);
  public static final Value _800c7193 = MEMORY.ref(1, 0x800c7193L);
  public static final ArrayRef<ShortRef> _800c7194 = MEMORY.ref(2, 0x800c7194L, ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));

  public static final ArrayRef<ShortRef> _800c71bc = MEMORY.ref(2, 0x800c71bcL, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _800c71d0 = MEMORY.ref(2, 0x800c71d0L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
  public static final ArrayRef<ShortRef> _800c71e4 = MEMORY.ref(2, 0x800c71e4L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
  public static final Value _800c71ec = MEMORY.ref(1, 0x800c71ecL);

  /** Different sets of bobjs for different target types */
  public static ScriptState<BattleObject27c>[][] targetBobjs_800c71f0;

  public static final Value _800c71fc = MEMORY.ref(4, 0x800c71fcL);

  public static final Value _800c721c = MEMORY.ref(4, 0x800c721cL);

  public static final Value _800c726c = MEMORY.ref(4, 0x800c726cL);

  public static final Value _800c723c = MEMORY.ref(4, 0x800c723cL);

  public static final Value _800c724c = MEMORY.ref(4, 0x800c724cL);

  public static final Value _800c7284 = MEMORY.ref(4, 0x800c7284L);

  public static final Value _800c729c = MEMORY.ref(4, 0x800c729cL);

  public static final Value _800c72b4 = MEMORY.ref(4, 0x800c72b4L);

  public static final ArrayRef<UnsignedShortRef> _800c72cc = MEMORY.ref(2, 0x800c72ccL, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));

  public static final Value _800d66b0 = MEMORY.ref(1, 0x800d66b0L);

  public static final Value _800d6c30 = MEMORY.ref(1, 0x800d6c30L);

  public static final ArrayRef<SpellStats0c> spellStats_800fa0b8 = MEMORY.ref(1, 0x800fa0b8L, ArrayRef.of(SpellStats0c.class, 128, 0xc, SpellStats0c::new));
  public static final Value _800fa6b8 = MEMORY.ref(2, 0x800fa6b8L);

  public static final Value _800fa6c4 = MEMORY.ref(2, 0x800fa6c4L);

  public static final Value _800fa6d0 = MEMORY.ref(2, 0x800fa6d0L);

  public static final Value _800fa6d4 = MEMORY.ref(2, 0x800fa6d4L);

  public static final IntRef mcqColour_800fa6dc = MEMORY.ref(4, 0x800fa6dcL, IntRef::new);
  public static final UnboundedArrayRef<RECT> _800fa6e0 = MEMORY.ref(2, 0x800fa6e0L, UnboundedArrayRef.of(0x8, RECT::new));

  public static final ArrayRef<ShortRef> colourMaps_800fa730 = MEMORY.ref(2, 0x800fa730L, ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));

  public static final ArrayRef<UnsignedShortRef> additionNextLevelXp_800fa744 = MEMORY.ref(2, 0x800fa744L, ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));

  /** Mersenne Twister seed */
  public static final MersenneTwisterSeed seed_800fa754 = MEMORY.ref(4, 0x800fa754L, MersenneTwisterSeed::new);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d1d3c}</li>
   *   <li>{@link Bttl_800d#FUN_800d1e80}</li>
   *   <li>{@link Bttl_800d#FUN_800d21b8}</li>
   *   <li>{@link Bttl_800d#FUN_800d1d3c}</li>
   *   <li>{@link Bttl_800d#FUN_800d21b8}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<QuintConsumerRef<EffectManagerData6c, Integer, short[], PotionEffect14, Translucency>>> effectRenderers_800fa758 = MEMORY.ref(4, 0x800fa758L, ArrayRef.of(Pointer.classFor(QuintConsumerRef.classFor(EffectManagerData6c.class, int.class, short[].class, PotionEffect14.class, Translucency.class)), 5, 4, Pointer.deferred(4, QuintConsumerRef::new)));

  public static final Value _800fa76c = MEMORY.ref(4, 0x800fa76cL);

  /** ASCII chars - [0-9][A-Z][a-z]'-& <null> */
  public static final ArrayRef<ByteRef> asciiTable_800fa788 = MEMORY.ref(1, 0x800fa788L, ArrayRef.of(ByteRef.class, 0x66, 1, ByteRef::new));
  public static final ArrayRef<IntRef> charWidthAdjustTable_800fa7cc = MEMORY.ref(4, 0x800fa7ccL, ArrayRef.of(IntRef.class, 0x66, 4, IntRef::new));

  public static final CString additionNames_800fa8d4 = MEMORY.ref(4, 0x800fa8d4L, CString.maxLength(0x1bb));

  public static final Value _800faa90 = MEMORY.ref(2, 0x800faa90L);
  public static final Value _800faa92 = MEMORY.ref(2, 0x800faa92L);
  public static final Value _800faa94 = MEMORY.ref(1, 0x800faa94L);

  public static final Value _800faa98 = MEMORY.ref(4, 0x800faa98L);
  public static final Value _800faa9c = MEMORY.ref(1, 0x800faa9cL);
  public static final Value _800faa9d = MEMORY.ref(1, 0x800faa9dL);

  public static final Value _800faaa0 = MEMORY.ref(4, 0x800faaa0L);

  public static final SVECTOR _800fab98 = MEMORY.ref(2, 0x800fab98L, SVECTOR::new);
  public static final SVECTOR _800faba0 = MEMORY.ref(2, 0x800faba0L, SVECTOR::new);
  public static final VECTOR _800faba8 = MEMORY.ref(4, 0x800faba8L, VECTOR::new);

  public static final Value _800fabb8 = MEMORY.ref(1, 0x800fabb8L);

  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dacc4}</li>
   *   <li>{@link Bttl_800d#FUN_800dad14}</li>
   *   <li>{@link Bttl_800d#FUN_800dadc0}</li>
   *   <li>{@link Bttl_800d#FUN_800dadc8}</li>
   *   <li>{@link Bttl_800d#FUN_800dadd0}</li>
   *   <li>{@link Bttl_800d#FUN_800dae3c}</li>
   *   <li>{@link Bttl_800d#FUN_800daedc}</li>
   *   <li>{@link Bttl_800d#FUN_800daf6c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<QuadConsumerRef<Integer, Integer, Integer, Integer>>> _800fabbc = MEMORY.ref(4, 0x800fabbcL, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(Integer.class, Integer.class, Integer.class, Integer.class)), 8, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800db0d8}</li>
   *   <li>{@link Bttl_800d#FUN_800db128}</li>
   *   <li>{@link Bttl_800d#FUN_800db1d4}</li>
   *   <li>{@link Bttl_800d#FUN_800db240}</li>
   *   <li>{@link Bttl_800d#FUN_800db2e0}</li>
   *   <li>{@link Bttl_800d#FUN_800db2e8}</li>
   *   <li>{@link Bttl_800d#FUN_800db2f0}</li>
   *   <li>{@link Bttl_800d#FUN_800db398}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<QuadConsumerRef<Integer, Integer, Integer, Integer>>> _800fabdc = MEMORY.ref(4, 0x800fabdcL, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(Integer.class, Integer.class, Integer.class, Integer.class)), 8, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d47dc}</li>
   *   <li>{@link Bttl_800d#FUN_800d496c}</li>
   *   <li>{@link Bttl_800d#FUN_800db564}</li>
   *   <li>{@link Bttl_800d#FUN_800db56c}</li>
   *   <li>{@link Bttl_800d#FUN_800d4bac}</li>
   *   <li>{@link Bttl_800d#FUN_800d4d7c}</li>
   *   <li>{@link Bttl_800d#FUN_800d4fbc}</li>
   *   <li>{@link Bttl_800d#FUN_800d519c}</li>
   * </ol>
   */
  public static final Value _800fabfc = MEMORY.ref(4, 0x800fabfcL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d53e4}</li>
   *   <li>{@link Bttl_800d#FUN_800d5574}</li>
   *   <li>{@link Bttl_800d#FUN_800db78c}</li>
   *   <li>{@link Bttl_800d#FUN_800db794}</li>
   *   <li>{@link Bttl_800d#FUN_800d5740}</li>
   *   <li>{@link Bttl_800d#FUN_800d5930}</li>
   *   <li>{@link Bttl_800d#FUN_800d5afc}</li>
   *   <li>{@link Bttl_800d#FUN_800d5cf4}</li>
   * </ol>
   */
  public static final Value _800fac1c = MEMORY.ref(4, 0x800fac1cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d5ec8}</li>
   *   <li>{@link Bttl_800d#FUN_800d60b0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9d0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d62d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d64e4}</li>
   *   <li>{@link Bttl_800d#FUN_800d670c}</li>
   *   <li>{@link Bttl_800d#FUN_800d6960}</li>
   * </ol>
   */
  public static final Value _800fac3c = MEMORY.ref(4, 0x800fac3cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d6b90}</li>
   *   <li>{@link Bttl_800d#FUN_800d6d18}</li>
   *   <li>{@link Bttl_800d#FUN_800d6f58}</li>
   *   <li>{@link Bttl_800d#FUN_800d7128}</li>
   *   <li>{@link Bttl_800d#FUN_800db678}</li>
   *   <li>{@link Bttl_800d#FUN_800db680}</li>
   *   <li>{@link Bttl_800d#FUN_800d7368}</li>
   *   <li>{@link Bttl_800d#FUN_800d7548}</li>
   * </ol>
   */
  public static final Value _800fac5c = MEMORY.ref(4, 0x800fac5cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d7790}</li>
   *   <li>{@link Bttl_800d#FUN_800d7920}</li>
   *   <li>{@link Bttl_800d#FUN_800d7aec}</li>
   *   <li>{@link Bttl_800d#FUN_800d7cdc}</li>
   *   <li>{@link Bttl_800d#FUN_800db8a0}</li>
   *   <li>{@link Bttl_800d#FUN_800db8a8}</li>
   *   <li>{@link Bttl_800d#FUN_800d7ea8}</li>
   *   <li>{@link Bttl_800d#FUN_800d80a0}</li>
   * </ol>
   */
  public static final Value _800fac7c = MEMORY.ref(4, 0x800fac7cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d8274}</li>
   *   <li>{@link Bttl_800d#FUN_800d8424}</li>
   *   <li>{@link Bttl_800d#FUN_800d8614}</li>
   *   <li>{@link Bttl_800d#FUN_800d8808}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb00}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb08}</li>
   *   <li>{@link Bttl_800d#FUN_800d89f8}</li>
   *   <li>{@link Bttl_800d#FUN_800d8bf4}</li>
   * </ol>
   */
  public static final Value _800fac9c = MEMORY.ref(4, 0x800fac9cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dbe40}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe60}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe80}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe8c}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe98}</li>
   *   <li>{@link Bttl_800d#FUN_800dbef0}</li>
   *   <li>{@link Bttl_800d#FUN_800dbf70}</li>
   *   <li>{@link Bttl_800d#FUN_800dbfd4}</li>
   *   <li>{@link Bttl_800d#FUN_800d90c8}</li>
   *   <li>{@link Bttl_800d#FUN_800d9154}</li>
   *   <li>{@link Bttl_800d#FUN_800dc070}</li>
   *   <li>{@link Bttl_800d#FUN_800dc078}</li>
   *   <li>{@link Bttl_800d#FUN_800d9220}</li>
   *   <li>{@link Bttl_800d#FUN_800d92bc}</li>
   *   <li>{@link Bttl_800d#FUN_800d9380}</li>
   *   <li>{@link Bttl_800d#FUN_800d9438}</li>
   *   <li>{@link Bttl_800d#FUN_800d9518}</li>
   *   <li>{@link Bttl_800d#FUN_800d9650}</li>
   *   <li>{@link Bttl_800d#FUN_800dc080}</li>
   *   <li>{@link Bttl_800d#FUN_800dc088}</li>
   *   <li>{@link Bttl_800d#FUN_800d9788}</li>
   *   <li>{@link Bttl_800d#FUN_800d98d0}</li>
   *   <li>{@link Bttl_800d#FUN_800d9a68}</li>
   *   <li>{@link Bttl_800d#FUN_800d9bd4}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<RunnableRef>> _800facbc = MEMORY.ref(4, 0x800facbcL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 24, 4, Pointer.deferred(4, RunnableRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc090}</li>
   *   <li>{@link Bttl_800d#FUN_800dc0b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc0d0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc128}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1a8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc21c}</li>
   *   <li>{@link Bttl_800d#FUN_800d9da0}</li>
   *   <li>{@link Bttl_800d#FUN_800d9e2c}</li>
   *   <li>{@link Bttl_800d#FUN_800d9ef8}</li>
   *   <li>{@link Bttl_800d#FUN_800d9f94}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2c0}</li>
   *   <li>{@link Bttl_800d#FUN_800da058}</li>
   *   <li>{@link Bttl_800d#FUN_800da110}</li>
   *   <li>{@link Bttl_800d#FUN_800da1f0}</li>
   *   <li>{@link Bttl_800d#FUN_800da328}</li>
   *   <li>{@link Bttl_800d#FUN_800da460}</li>
   *   <li>{@link Bttl_800d#FUN_800da5b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2c8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2d0}</li>
   *   <li>{@link Bttl_800d#FUN_800da750}</li>
   *   <li>{@link Bttl_800d#FUN_800da8bc}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<RunnableRef>> _800fad1c = MEMORY.ref(4, 0x800fad1cL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 24, 4, Pointer.deferred(4, RunnableRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc408}</li>
   *   <li>{@link Bttl_800d#FUN_800dc45c}</li>
   *   <li>{@link Bttl_800d#FUN_800dc504}</li>
   *   <li>{@link Bttl_800d#FUN_800dc50c}</li>
   *   <li>{@link Bttl_800d#FUN_800dc514}</li>
   *   <li>{@link Bttl_800d#FUN_800dc580}</li>
   *   <li>{@link Bttl_800d#FUN_800dc630}</li>
   *   <li>{@link Bttl_800d#FUN_800dc6d8}</li>
   * </ol>
   */
  public static final Value _800fad7c = MEMORY.ref(4, 0x800fad7cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc798}</li>
   *   <li>{@link Bttl_800d#FUN_800dc7ec}</li>
   *   <li>{@link Bttl_800d#FUN_800dc894}</li>
   *   <li>{@link Bttl_800d#FUN_800dc900}</li>
   *   <li>{@link Bttl_800d#FUN_800dc9b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc9b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc9c0}</li>
   *   <li>{@link Bttl_800d#FUN_800dca68}</li>
   * </ol>
   */
  public static final Value _800fad9c = MEMORY.ref(4, 0x800fad9cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800e#FUN_800e3f88}</li>
   *   <li>{@link Bttl_800d#FUN_800df130}</li>
   *   <li>{@link Bttl_800e#FUN_800e3f88}</li>
   *   <li>{@link Bttl_800d#FUN_800df130}</li>
   *   <li>{@link Bttl_800e#FUN_800e4184}</li>
   *   <li>{@link Bttl_800d#FUN_800df6f0}</li>
   *   <li>{@link Bttl_800e#FUN_800e4184}</li>
   *   <li>{@link Bttl_800d#FUN_800df6f0}</li>
   *   <li>{@link Bttl_800d#FUN_800de9bc}</li>
   *   <li>{@link Bttl_800d#FUN_800dee8c}</li>
   *   <li>{@link Bttl_800d#FUN_800de9bc}</li>
   *   <li>{@link Bttl_800d#FUN_800dee8c}</li>
   *   <li>{@link Bttl_800d#FUN_800dec14}</li>
   *   <li>{@link Bttl_800d#FUN_800df370}</li>
   *   <li>{@link Bttl_800d#FUN_800dec14}</li>
   *   <li>{@link Bttl_800d#FUN_800df370}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e3f88}</li>
   *   <li>{@link Bttl_800d#FUN_800df130}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e43a8}</li>
   *   <li>{@link Bttl_800d#FUN_800dffe4}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800d#FUN_800de9bc}</li>
   *   <li>{@link Bttl_800d#FUN_800dee8c}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800d#FUN_800df9e8}</li>
   *   <li>{@link Bttl_800d#FUN_800dfc5c}</li>
   *   <li>{@link Bttl_800e#FUN_800e0848}</li>
   *   <li>{@link Bttl_800e#FUN_800e1c24}</li>
   *   <li>{@link Bttl_800e#FUN_800e0848}</li>
   *   <li>{@link Bttl_800e#FUN_800e1c24}</li>
   *   <li>{@link Bttl_800e#FUN_800e121c}</li>
   *   <li>{@link Bttl_800e#FUN_800e2620}</li>
   *   <li>{@link Bttl_800e#FUN_800e121c}</li>
   *   <li>{@link Bttl_800e#FUN_800e2620}</li>
   *   <li>{@link Bttl_800e#FUN_800e02e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e16a0}</li>
   *   <li>{@link Bttl_800e#FUN_800e02e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e16a0}</li>
   *   <li>{@link Bttl_800e#FUN_800e0c98}</li>
   *   <li>{@link Bttl_800e#FUN_800e20bc}</li>
   *   <li>{@link Bttl_800e#FUN_800e0c98}</li>
   *   <li>{@link Bttl_800e#FUN_800e20bc}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e0848}</li>
   *   <li>{@link Bttl_800e#FUN_800e1c24}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e300c}</li>
   *   <li>{@link Bttl_800e#FUN_800e39e8}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e02e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e16a0}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e2a98}</li>
   *   <li>{@link Bttl_800e#FUN_800e3478}</li>
   * </ol>
   *
   * Note: blank lines are probably impossible combinations
   */
  public static final ArrayRef<Pointer<QuadFunctionRef<Long, UnboundedArrayRef<SVECTOR>, Long, Long, Long>>> ctmdRenderers_800fadbc = MEMORY.ref(4, 0x800fadbcL, ArrayRef.of(Pointer.classFor(QuadFunctionRef.classFor(Long.class, UnboundedArrayRef.classFor(SVECTOR.class), Long.class, Long.class, Long.class)), 0x40, 4, Pointer.deferred(4, QuadFunctionRef::new)));

  public static final Value _800faec4 = MEMORY.ref(2, 0x800faec4L);

  public static final Value _800fafe8 = MEMORY.ref(4, 0x800fafe8L);
  public static final Value _800fafec = MEMORY.ref(1, 0x800fafecL);
  public static final Value _800fb040 = MEMORY.ref(1, 0x800fb040L);

  public static final Value _800fb05c = MEMORY.ref(1, 0x800fb05cL);

  public static final Value stageIndices_800fb064 = MEMORY.ref(1, 0x800fb064L);

  public static final Value _800fb06c = MEMORY.ref(1, 0x800fb06cL);

  public static final Value _800fb0ec = MEMORY.ref(4, 0x800fb0ecL);

  public static final ArrayRef<UnsignedByteRef> _800fb148 = MEMORY.ref(1, 0x800fb148L, ArrayRef.of(UnsignedByteRef.class, 0x40, 1, UnsignedByteRef::new));

  /** TODO array of unsigned shorts */
  public static final Value _800fb188 = MEMORY.ref(2, 0x800fb188L);

  /** TODO array of unsigned shorts */
  public static final Value _800fb198 = MEMORY.ref(2, 0x800fb198L);

  /** Targeting ("All allies", "All players", "All") */
  public static final ArrayRef<Pointer<LodString>> targeting_800fb36c = MEMORY.ref(4, 0x800fb36cL, ArrayRef.of(Pointer.classFor(LodString.class),  3, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> playerNames_800fb378 = MEMORY.ref(4, 0x800fb378L, ArrayRef.of(Pointer.classFor(LodString.class), 11, 4, Pointer.deferred(4, LodString::new)));
  /** Dispirited, Weapon blocked, Stunned, Fearful, Confused, Bewitched, Petrified */
  public static final ArrayRef<Pointer<LodString>> ailments_800fb3a0 = MEMORY.ref(4, 0x800fb3a0L, ArrayRef.of(Pointer.classFor(LodString.class),  7, 4, Pointer.deferred(4, LodString::new)));

  /** Player names, player names, item names, dragoon spells, item descriptions, spell descriptions */
  public static final ArrayRef<Pointer<UnboundedArrayRef<Pointer<LodString>>>> allText_800fb3c0 = MEMORY.ref(4, 0x800fb3c0L, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(Pointer.classFor(LodString.class))),  6, 4, Pointer.deferred(4, UnboundedArrayRef.of(4, Pointer.deferred(4, LodString::new)))));

  /** TODO array of pointers to shorts? */
  public static final Value _800fb444 = MEMORY.ref(4, 0x800fb444L);

  public static final ArrayRef<ByteRef> _800fb46c = MEMORY.ref(1, 0x800fb46cL, ArrayRef.of(ByteRef.class, 0x10, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> _800fb47c = MEMORY.ref(1, 0x800fb47cL, ArrayRef.of(ByteRef.class, 0x10, 1, ByteRef::new));

  public static final Value _800fb4b4 = MEMORY.ref(2, 0x800fb4b4L);

  public static final Value _800fb534 = MEMORY.ref(2, 0x800fb534L);

  public static final Value _800fb548 = MEMORY.ref(2, 0x800fb548L);

  public static final Value _800fb55c = MEMORY.ref(2, 0x800fb55cL);

  public static final Value _800fb5dc = MEMORY.ref(4, 0x800fb5dcL);

  public static final Value _800fb614 = MEMORY.ref(4, 0x800fb614L);

  public static final Value _800fb674 = MEMORY.ref(4, 0x800fb674L);

  public static final Value _800fb6bc = MEMORY.ref(2, 0x800fb6bcL);

  public static final Value _800fb6f4 = MEMORY.ref(1, 0x800fb6f4L);

  public static final Value _800fb72c = MEMORY.ref(4, 0x800fb72cL);

  public static final CString _800fb954 = MEMORY.ref(5, 0x800fb954L, CString::new);

  @Method(0x800c7304L)
  public static void FUN_800c7304() {
    int a0;
    int a1;
    //LAB_800c7330
    for(a0 = 0, a1 = 0; a0 < _800c66d0.get(); a0++) {
      final ScriptState<BattleObject27c> bobjState = _8006e398.bobjIndices_e0c[a0];
      if((bobjState.storage_44[7] & 0x40) == 0) {
        _8006e398.bobjIndices_e78[a1] = bobjState;
        a1++;
      }

      //LAB_800c736c
    }

    //LAB_800c737c
    _800c669c.set(a1);

    //LAB_800c73b0
    for(a0 = 0, a1 = 0; a0 < charCount_800c677c.get(); a0++) {
      final ScriptState<BattleObject27c> bobjState = _8006e398.charBobjIndices_e40[a0];
      if((bobjState.storage_44[7] & 0x40) == 0) {
        _8006e398.bobjIndices_eac[a1] = bobjState;
        a1++;
      }

      //LAB_800c73ec
    }

    //LAB_800c73fc
    _800c6760.set(a1);

    //LAB_800c7430
    for(a0 = 0, a1 = 0; a0 < monsterCount_800c6768.get(); a0++) {
      final ScriptState<BattleObject27c> bobjState = _8006e398.bobjIndices_e50[a0];
      if((bobjState.storage_44[7] & 0x40) == 0) {
        _8006e398.enemyBobjIndices_ebc[a1] = bobjState;
        a1++;
      }

      //LAB_800c746c
    }

    //LAB_800c747c
    enemyCount_800c6758.set(a1);
  }

  @Method(0x800c7488L)
  public static int getHitMultiplier(final int charSlot, final int hitNum, final int a2) {
    if((_8006e398.charBobjIndices_e40[charSlot].storage_44[7] & 0x2) != 0) { // Is dragoon
      return _1f8003f4._38[charSlot + 3].hits_00[hitNum]._00[a2];
    }

    //LAB_800c74fc
    return _1f8003f4._38[charSlot].hits_00[hitNum]._00[a2];
  }

  @Method(0x800c7524L)
  public static void FUN_800c7524() {
    FUN_800c8624();

    gameState_800babc8._b4.incr();
    _800bc910.setu(0);
    _800bc914.setu(0);
    _800bc918.setu(0);
    goldGainedFromCombat_800bc920.set(0);

    spGained_800bc950.get(0).set(0);
    spGained_800bc950.get(1).set(0);
    spGained_800bc950.get(2).set(0);

    totalXpFromCombat_800bc95c.set(0);
    _800bc960.set(0);
    _800bc974.set(0);
    itemsDroppedByEnemiesCount_800bc978.set(0);

    int charIndex = gameState_800babc8.charIndex_88.get(1).get();
    if(charIndex < 0) {
      gameState_800babc8.charIndex_88.get(1).set(gameState_800babc8.charIndex_88.get(2).get());
      gameState_800babc8.charIndex_88.get(2).set(charIndex);
    }

    //LAB_800c75c0
    charIndex = gameState_800babc8.charIndex_88.get(0).get();
    if(charIndex < 0) {
      gameState_800babc8.charIndex_88.get(0).set(gameState_800babc8.charIndex_88.get(1).get());
      gameState_800babc8.charIndex_88.get(1).set(charIndex);
    }

    //LAB_800c75e8
    charIndex = gameState_800babc8.charIndex_88.get(1).get();
    if(charIndex < 0) {
      gameState_800babc8.charIndex_88.get(1).set(gameState_800babc8.charIndex_88.get(2).get());
      gameState_800babc8.charIndex_88.get(2).set(charIndex);
    }

    //LAB_800c760c
    allocateStageDarkeningStorage();
    btldLoadEncounterSoundEffectsAndMusic();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7648L)
  public static void FUN_800c7648() {
    loadStage(combatStage_800bb0f4.get());
    loadSupportOverlay(1, SBtld::FUN_80109050);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c76a0L)
  public static void FUN_800c76a0() {
    if((_800bc960.get() & 0x3) == 0x3) {
      setWidthAndFlags(320);
      setDepthResolution(12);
      vsyncMode_8007a3b8.set(3);
      _800bc960.or(0x40);
      setProjectionPlaneDistance(320);
      FUN_800dabec();
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7718
  }

  @Method(0x800c772cL)
  public static void FUN_800c772c() {
    FUN_800c8e48();

    _800bc94c.setu(0x1L);

    scriptStartEffect(4, 30);

    _800bc960.or(0x20);
    _8006e398.stageProgression_eec = 0;

    FUN_800ca980();
    FUN_800c8ee4();
    FUN_800cae44();

    _800c66d0.set(0);
    monsterCount_800c6768.set(0);
    charCount_800c677c.set(0);

    _8006e398.morphMode_ee4 = gameState_800babc8.morphMode_4e2.get();

    loadSupportOverlay(1, SBtld::FUN_80109250);

    //LAB_800c7830
    for(int i = 0; i < 12; i++) {
      _8006e398.bobjIndices_e0c[i] = null;
    }

    FUN_800ee610();
    FUN_800f84c0();
    FUN_800f60ac();
    FUN_800e8ffc();

    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c788cL)
  public static void deferAllocateEnemyBattleObjects() {
    loadSupportOverlay(1, SBtld::allocateEnemyBattleObjects);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c78d4L)
  public static void deferAllocatePlayerBattleObjects() {
    loadSupportOverlay(2, SItem::allocatePlayerBattleObjects);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c791cL)
  public static void deferLoadEncounterAssets() {
    loadSupportOverlay(2, SItem::loadEncounterAssets);
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7964L)
  public static void FUN_800c7964() {
    _800bc960.or(0xc);

    loadBattleHudTextures();
    loadBattleHudDeff_();

    //LAB_800c79a8
    for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
      loadAttackAnimations(combatantIndex);
    }

    //LAB_800c79c8
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c79f0L)
  public static void FUN_800c79f0() {
    currentTurnBobj_800c66c8 = _8006e398.bobjIndices_e0c[0];
    FUN_800f417c();
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7a30L)
  public static void deferDoNothing() {
    loadSupportOverlay(3, () -> { });
    pregameLoadingStage_800bb10c.incr();
  }

  @Method(0x800c7a80L)
  public static void FUN_800c7a80() {
    if(_800c66a8.get() != 0) {
      _800bc960.or(0x10);

      //LAB_800c7ae4
      for(int i = 0; i < _800c66d0.get(); i++) {
        final ScriptState<BattleObject27c> bobjState = _8006e398.bobjIndices_e0c[i];
        final BattleObject27c bobj = bobjState.innerStruct_00;

        if((bobjState.storage_44[7] & 0x4) != 0) {
          bobj.turnValue_4c = simpleRand() * 0xd9 / 0x10000;
        } else {
          //LAB_800c7b3c
          bobj.turnValue_4c = simpleRand() * 0xa7 / 0x10000 + 0x32;
        }

        //LAB_800c7b68
      }

      //LAB_800c7b80
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7b9c
  }

  @Method(0x800c7bb8L)
  public static void FUN_800c7bb8() {
    FUN_800ef9e4();
    drawUiElements();

    if(_800bc974.get() != 0) {
      pregameLoadingStage_800bb10c.incr();
      return;
    }

    if(_800c66d0.get() > 0 && _800c66b9.get() == 0 && FUN_800c7da8() != 0) {
      vsyncMode_8007a3b8.set(3);
      mcqColour_800fa6dc.set(0x80);
      currentTurnBobj_800c66c8.storage_44[7] &= 0xffff_efff;

      if(_800c6760.get() <= 0) {
        loadMusicPackage(19, 0);
        _800bc974.set(2);
      } else {
        //LAB_800c7c98
        final ScriptState<BattleObject27c> state = FUN_800c7e24();
        scriptIndex_800c66bc = state;

        if(state != null) {
          state.storage_44[7] = state.storage_44[7] & 0xffff_ffdf | 0x1008;
          currentTurnBobj_800c66c8 = state;
        } else {
          //LAB_800c7ce8
          if(enemyCount_800c6758.get() > 0) {
            //LAB_800c7d3c
            final ScriptState<BattleObject27c> a1_0 = getCurrentTurnBobj();
            currentTurnBobj_800c66c8 = a1_0;
            a1_0.storage_44[7] |= 0x1008;

            //LAB_800c7d74
          } else {
            FUN_80020308();

            if(encounterId_800bb0f8.get() != 443) { // Melbu
              _800bc974.set(1);
              FUN_8001af00(6);
            } else {
              //LAB_800c7d30
              _800bc974.set(4);
            }
          }
        }
      }
    }

    //LAB_800c7d78
    if(_800bc974.get() != 0) {
      //LAB_800c7d88
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c7d98
  }

  @Method(0x800c7da8L)
  public static long FUN_800c7da8() {
    //LAB_800c7dd8
    for(int i = 0; i < _800c66d0.get(); i++) {
      if((_8006e398.bobjIndices_e0c[i].storage_44[7] & 0x408) != 0) {
        return 0;
      }

      //LAB_800c7e10
    }

    //LAB_800c7e1c
    return 0x1L;
  }

  @Method(0x800c7e24L)
  public static ScriptState<BattleObject27c> FUN_800c7e24() {
    //LAB_800c7e54
    for(int i = 0; i < _800c669c.get(); i++) {
      final ScriptState<BattleObject27c> bobjState = _8006e398.bobjIndices_e78[i];
      if(bobjState != null && (bobjState.storage_44[7] & 0x20) != 0) {
        return bobjState;
      }

      //LAB_800c7e8c
    }

    //LAB_800c7e98
    return null;
  }

  @Method(0x800c7ea0L)
  public static ScriptState<BattleObject27c> getCurrentTurnBobj() {
    //LAB_800c7ee4
    for(int s4 = 0; s4 < 32; s4++) {
      //LAB_800c7ef0
      int highestTurnValue = 0;
      int highestCombatantindex = 0;
      for(int combatantIndex = 0; combatantIndex < _800c669c.get(); combatantIndex++) {
        final int turnValue = _8006e398.bobjIndices_e78[combatantIndex].innerStruct_00.turnValue_4c;

        if(highestTurnValue <= turnValue) {
          highestTurnValue = turnValue;
          highestCombatantindex = combatantIndex;
        }

        //LAB_800c7f30
      }

      //LAB_800c7f40
      if(highestTurnValue > 0xd9) {
        final ScriptState<BattleObject27c> state = _8006e398.bobjIndices_e78[highestCombatantindex];
        state.innerStruct_00.turnValue_4c = highestTurnValue - 0xd9;

        if((state.storage_44[7] & 0x4) == 0) {
          gameState_800babc8._b8.incr();
        }

        //LAB_800c7f9c
        return state;
      }

      //LAB_800c7fa4
      //LAB_800c7fb0
      for(int combatantIndex = 0; combatantIndex < _800c669c.get(); combatantIndex++) {
        final BattleObject27c bobj = _8006e398.bobjIndices_e78[combatantIndex].innerStruct_00;
        highestTurnValue = bobj.speed_32 * (simpleRand() + 0x4_4925);
        final int v1 = (int)(highestTurnValue * 0x35c2_9183L >>> 32) >> 16; //TODO _pretty_ sure this is roughly /312,110 (seems oddly specific?)
        bobj.turnValue_4c += v1;
      }

      //LAB_800c8028
    }

    //LAB_800c8040
    return _8006e398.bobjIndices_eac[0];
  }

  @Method(0x800c8068L)
  public static void FUN_800c8068() {
    final int s0 = _800bc974.get();

    if(_800c6690.get() == 0) {
      final int a1 = (int)_800fa6c4.offset(s0 * 0x2L).getSigned();

      if(a1 >= 0) {
        _800c6748.set(a1);
        scriptState_800c6914 = currentTurnBobj_800c66c8;
      }

      //LAB_800c80c8
      final int v0 = _800c6760.get();
      _800bc97c.setu(v0);

      //LAB_800c8104
      for(int i = 0; i < v0; i++) {
        _800bc968.offset(i * 0x4L).setu(_8006e398.bobjIndices_eac[i].innerStruct_00.charIndex_272);
      }

      //LAB_800c8144
      if(s0 == 1) {
        //LAB_800c8180
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          _8006e398.charBobjIndices_e40[_800c6690.get()].storage_44[7] |= 0x8;
        }
      }
    }

    //LAB_800c81bc
    //LAB_800c81c0
    _800c6690.incr();

    if(_800c6690.get() >= _800fa6b8.offset(s0 * 0x2L).getSigned() || (joypadPress_8007a398.get() & 0xff) != 0 && _800c6690.get() >= 0x19L) {
      //LAB_800c8214
      FUN_800e9120();
      decrementOverlayCount();
      loadSupportOverlay(2, Scus94491BpeSegment::decrementOverlayCount);

      if(_800bb168.get() == 0) {
        scriptStartEffect(1, (int)_800fa6d0.offset(s0 * 0x2L).getSigned());
      }

      //LAB_800c8274
      if(s0 == 2) {
        sssqFadeOut((short)(_800fa6d4.getSigned() - 2));
      }

      //LAB_800c8290
      _800c6690.set(0);
      pregameLoadingStage_800bb10c.incr();
    }

    //LAB_800c82a8
  }

  @Method(0x800c82b8L)
  public static void deallocateCombat() {
    if(_800bb168.get() == 0xffL) {
      updateGameStateAndDeallocateMenu();
      setStageHasNoModel();

      script_800c66fc = null;

      //LAB_800c8314
      FUN_80029e04(null);
      scriptState_800c674c.deallocateWithChildren();

      //LAB_800c8368
      //LAB_800c8394
      FUN_8001ad18();

      //LAB_800c83b8
      while(_800c66d0.get() > 0) {
        _8006e398.bobjIndices_e0c[0].deallocateWithChildren();
      }

      //LAB_800c83d8
      //LAB_800c83f4
      for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
        final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

        //LAB_800c8418
        //LAB_800c8434
        if(combatant.mrg_00 != null) {
          free(combatant.mrg_00.getAddress());
          combatant.mrg_00 = null;
        }

        if(combatant.mrg_04 != null) {
          free(combatant.mrg_04.getAddress());
          combatant.mrg_04 = null;
        }

        //LAB_800c8454
      }

      //LAB_800c847c
      FUN_800c8f18();
      FUN_800ca9b4();
      deallocateStageDarkeningStorage();
      FUN_800c8748();

      int a1 = previousMainCallbackIndex_8004dd28.get();
      if(a1 == 9) {
        a1 = 5;
      }

      //LAB_800c84b4
      switch(_800bc974.get()) {
        case 2 -> {
          final int encounter = encounterId_800bb0f8.get();
          if(encounter == 391 || encounter >= 404 && encounter < 408) { // Arena fights in Lohan
            //LAB_800c8514
            gameState_800babc8.scriptFlags2_bc.get(0x1d).or(0x800_0000);
          } else {
            //LAB_800c8534
            a1 = 7; // Game over screen
          }
        }

        case 4 -> {
          fmvIndex_800bf0dc.setu(0x10L);
          afterFmvLoadingStage_800bf0ec.set(11);
          a1 = 9;
        }
      }

      //LAB_800c8558
      _800bc91c.set(a1);

      long v1 = _800c6724.get();
      if(v1 != 0xff) {
        submapScene_80052c34.set((int)v1);
      }

      //LAB_800c8578
      v1 = _800c6740.get();
      if(v1 != 0xffff) {
        submapCut_80052c30.set((int)v1);
      }

      //LAB_800c8590
      setDepthResolution(14);
      _800bc94c.setu(0);

      switch(_800bc974.get()) {
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
    _1f8003f4 = new BattleStruct18cb0();
    _8006e398 = new BattleStructEf4();
    targetBobjs_800c71f0 = new ScriptState[][] {_8006e398.charBobjIndices_e40, _8006e398.enemyBobjIndices_ebc, _8006e398.bobjIndices_e78};
  }

  @Method(0x800c8748L)
  public static void FUN_800c8748() {
    if(_1f8003f4.stageTmdMrg_63c != null) {
      free(_1f8003f4.stageTmdMrg_63c.getAddress());
    }

    if(_1f8003f4.stageMcq_9cb0 != null) {
      free(_1f8003f4.stageMcq_9cb0.getAddress());
    }

    _1f8003f4 = null;
    _8006e398 = null;
    targetBobjs_800c71f0 = null;
  }

  @Method(0x800c8774L)
  public static void loadStageTmdAndAnim(final List<byte[]> files) {
    setStageHasNoModel();

    if(files.get(0).length > 0 && files.get(1).length > 0 && files.get(2).length > 0) {
      _800c6754.set(1);
      stageHasModel_800c66b8.set(true);

      if(_1f8003f4.stageTmdMrg_63c != null) {
        free(_1f8003f4.stageTmdMrg_63c.getAddress());
      }

      _1f8003f4.stageTmdMrg_63c = MrgFile.alloc(files);

      final BattleStage stage = _1f8003f4.stage_963c;
      loadStageTmd(stage, _1f8003f4.stageTmdMrg_63c.getFile(0, ExtendedTmd::new), _1f8003f4.stageTmdMrg_63c.getFile(1, TmdAnimationFile::new));
      stage.coord2_558.coord.transfer.set(0, 0, 0);
      stage.param_5a8.rotate.set((short)0, (short)0x400, (short)0);
    }

    //LAB_800c8818
  }

  @Method(0x800c882cL)
  public static void FUN_800c882c() {
    if(_800c6764.get() == 0 || _800c66d4.get() == 0 || (_800bc960.get() & 0x80) == 0) {
      //LAB_800c8ad8
      //LAB_800c8adc
      _800babc0.set(0);
      _800bb104.set(0);
      _8007a3a8.set(0);
    } else {
      _800c6774.add(_800c676c.get());
      _800c6778.add(_800c6770.get());
      final int x0 = ((int)_800c66cc.getSigned() * FUN_800dd118() / 0x1000 + _800c6774.get()) % _1f8003f4.stageMcq_9cb0.screenWidth_14.get() - centreScreenX_1f8003dc.get();
      final int x1 = x0 - _1f8003f4.stageMcq_9cb0.screenWidth_14.get();
      final int x2 = x0 + _1f8003f4.stageMcq_9cb0.screenWidth_14.get();
      int y = _800c6778.get() - (FUN_800dd0d4() + 0x800 & 0xfff) + 0x760 - centreScreenY_1f8003de.get();
      renderMcq(_1f8003f4.stageMcq_9cb0, 320, 0, x0, y, orderingTableSize_1f8003c8.get() - 2, mcqColour_800fa6dc.get());
      renderMcq(_1f8003f4.stageMcq_9cb0, 320, 0, x1, y, orderingTableSize_1f8003c8.get() - 2, mcqColour_800fa6dc.get());

      if(centreScreenX_1f8003dc.get() >= x2) {
        renderMcq(_1f8003f4.stageMcq_9cb0, 320, 0, x2, y, orderingTableSize_1f8003c8.get() - 2, mcqColour_800fa6dc.get());
      }

      //LAB_800c89d4
      if(_1f8003f4.stageMcq_9cb0.magic_00.get() != McqHeader.MAGIC_1) {
        //LAB_800c89f8
        y += _1f8003f4.stageMcq_9cb0.screenOffsetY_2a.get();
      }

      //LAB_800c8a04
      final int colour = mcqColour_800fa6dc.get();
      if(y >= -centreScreenY_1f8003de.get()) {
        _8007a3a8.set(_1f8003f4.stageMcq_9cb0._18.get() * colour / 0x80);
        _800bb104.set(_1f8003f4.stageMcq_9cb0._19.get() * colour / 0x80);
        _800babc0.set(_1f8003f4.stageMcq_9cb0._1a.get() * colour / 0x80);
      } else {
        //LAB_800c8a74
        _8007a3a8.set(_1f8003f4.stageMcq_9cb0._20.get() * colour / 0x80);
        _800bb104.set(_1f8003f4.stageMcq_9cb0._21.get() * colour / 0x80);
        _800babc0.set(_1f8003f4.stageMcq_9cb0._22.get() * colour / 0x80);
      }
    }

    //LAB_800c8af0
  }

  @Method(0x800c8b20L)
  public static void loadStage(final int stage) {
    loadDrgnDir(0, 2497 + stage, files -> {
      if(files.get(0).length != 0) {
        if(_1f8003f4.stageMcq_9cb0 != null) {
          free(_1f8003f4.stageMcq_9cb0.getAddress());
        }

        final McqHeader mcq = MEMORY.ref(4, mallocTail(files.get(0).length), McqHeader::new);
        MEMORY.setBytes(mcq.getAddress(), files.get(0));
        loadStageMcq(mcq);
      }

      if(files.get(1).length != 0) {
        final long tim = mallocTail(files.get(1).length);
        MEMORY.setBytes(tim, files.get(1));
        loadStageTim(tim);
        free(tim);
      }
    });

    loadDrgnDir(0, (2497 + stage) + "/0", Bttl_800c::loadStageTmdAndAnim);

    currentStage_800c66a4.set(stage);
  }

  @Method(0x800c8c84L)
  public static void loadStageTim(final long a0) {
    final TimHeader tim = parseTimHeader(MEMORY.ref(4, a0 + 0x4L));
    LoadImage(tim.getImageRect(), tim.getImageAddress());

    if(tim.hasClut()) {
      LoadImage(tim.getClutRect(), tim.getClutAddress());
    }

    //LAB_800c8ccc
    backupStageClut(a0);
  }

  @Method(0x800c8ce4L)
  public static void setStageHasNoModel() {
    stageHasModel_800c66b8.set(false);
  }

  @Method(0x800c8cf0L)
  public static void FUN_800c8cf0() {
    if(stageHasModel_800c66b8.get() && _800c6754.get() != 0 && (_800bc960.get() & 0x20) != 0) {
      FUN_800ec744(_1f8003f4.stage_963c);
      FUN_800ec51c(_1f8003f4.stage_963c);
    }

    //LAB_800c8d50
  }

  @Method(0x800c8d64L)
  public static void loadStageMcq(final McqHeader mcq) {
    final long x;
    if((_800bc960.get() & 0x80) != 0) {
      x = 320;
      _800c6764.set(1);
    } else {
      //LAB_800c8d98
      x = 512;
    }

    //LAB_800c8d9c
    loadMcq(mcq, x, 0);

    //LAB_800c8dc0
    _1f8003f4.stageMcq_9cb0 = mcq;

    _800c66d4.setu(0x1L);
    _800c66cc.setu((0x400L / mcq.screenWidth_14.get() + 0x1L) * mcq.screenWidth_14.get());
  }

  @Method(0x800c8e48L)
  public static void FUN_800c8e48() {
    if(_800c66d4.get() != 0 && (_800bc960.get() & 0x80) == 0) {
      final RECT sp0x10 = new RECT((short)512, (short)0, _1f8003f4.stageMcq_9cb0.vramWidth_08.get(), (short)256);
      MoveImage(sp0x10, 320, 0);
      _800c6764.set(1);
      _800bc960.or(0x80);
    }

    //LAB_800c8ec8
  }

  @Method(0x800c8ed8L)
  public static void FUN_800c8ed8() {
    _800c66d4.setu(0);
  }

  @Method(0x800c8ee4L)
  public static void FUN_800c8ee4() {
    //LAB_800c8ef4
    //NOTE: zeroes 0x50 bytes after this array of structs ends
    Arrays.fill(combatants_8005e398, null);

    _800c66c0.setu(0x1L);
  }

  @Method(0x800c8f18L)
  public static void FUN_800c8f18() {
    _800c66c0.setu(0);
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
        combatant.colourMap_1a0 = 0;
        combatant.charIndex_1a2 = a0;
        combatant._1a4 = -1;
        combatant._1a6 = -1;
        combatantCount_800c66a0.addu(0x1L);
        return combatantIndex;
      }
    }

    return -1;
  }

  @Method(0x800c8fd4L)
  public static void removeCombatant(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.colourMap_1a0 != 0) {
      FUN_800ca918(combatant.colourMap_1a0);
    }

    //LAB_800c9020
    //LAB_800c902c
    combatants_8005e398[combatantIndex] = null;

    combatantCount_800c66a0.subu(0x1L);
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

  @Method(0x800c90b0L)
  public static long FUN_800c90b0(final int combatantIndex) {
    //LAB_800c9114
    if((combatants_8005e398[combatantIndex]._1a4 >= 0 || combatants_8005e398[combatantIndex].mrg_00 != null && combatants_8005e398[combatantIndex].mrg_00.entries.get(32).size.get() != 0) && FUN_800ca054(combatantIndex, 0) != 0) {
      return 0x1L;
    }

    //LAB_800c9128
    //LAB_800c912c
    return 0;
  }

  @Method(0x800c913cL)
  public static ScriptFile getCombatantScript(final int index) {
    return combatants_8005e398[index].scriptPtr_10;
  }

  @Method(0x800c9170L)
  public static void deallocateCombatant(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    //LAB_800c91bc
    if(combatant.mrg_00 != null) {
      free(combatant.mrg_00.getAddress());
      combatant.mrg_00 = null;
    }

    if(combatant.mrg_04 != null) {
      //LAB_800c91e8
      free(combatant.mrg_04.getAddress());
      combatant.mrg_04 = null;
    }

    if(combatant._1a4 >= 0) {
      FUN_800cad64(combatant._1a4);
    }

    //LAB_800c921c
    if(combatant._1a6 >= 0) {
      FUN_800cad64(combatant._1a6);
    }

    //LAB_800c9234
    //LAB_800c9238
    for(int i = 0; i < 32; i++) {
      if(combatant._14[i] != null && combatant._14[i]._09 != 0) {
        FUN_800c9c7c(combatantIndex, i);
      }

      //LAB_800c9254
    }

    combatant.flags_19e &= 0xffe7;
  }

  @Method(0x800c9290L)
  public static void loadCombatantTmdAndAnims(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.charIndex_1a2 >= 0) {
      if((combatant.flags_19e & 0x8) == 0) {
        if(combatant.mrg_00 == null) {
          combatant.flags_19e |= 0x28;

          if((combatant.flags_19e & 0x4) == 0) {
            // Enemy TMDs
            final int fileIndex = 3137 + combatant.charIndex_1a2;
            loadDrgnDir(0, fileIndex, files -> Bttl_800c.combatantTmdAndAnimLoadedCallback(files, combatantIndex, true));
          } else {
            // Player TMDs
            //LAB_800c9334
            int charIndex = gameState_800babc8.charIndex_88.get(combatant.charSlot_19c).get();
            combatant.flags_19e |= 0x2;

            if((combatant.charIndex_1a2 & 0x1) != 0) {
              if(charIndex == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) {
                charIndex = 10; // Divine dragoon
              }

              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/dragoon".formatted(charName), files -> Bttl_800c.combatantTmdAndAnimLoadedCallback(files, combatantIndex, false));
            } else {
              final String charName = getCharacterName(charIndex).toLowerCase();
              loadDir("characters/%s/models/combat".formatted(charName), files -> Bttl_800c.combatantTmdAndAnimLoadedCallback(files, combatantIndex, false));
            }
          }
        }
      }
    }

    //LAB_800c940c
  }

  @Method(0x800c941cL)
  public static void combatantTmdAndAnimLoadedCallback(final List<byte[]> files, final int combatantIndex, final boolean isMonster) {
    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);
    combatant.flags_19e &= 0xffdf;

    if(!isMonster) {
      _800bc960.or(0x4);
    }

    //LAB_800c947c
    combatant.mrg_00 = MrgFile.alloc(files);
    final MrgFile mrg = combatant.mrg_00;

    // I don't think this is actually used?
    if(files.get(34).length != 0) {
      combatant.scriptPtr_10 = new ScriptFile("%s %d file 34".formatted(isMonster ? "monster" : "char", combatant.charSlot_19c), files.get(34));
    }

    //LAB_800c94a0
    //LAB_800c94a4
    for(int animIndex = 0; animIndex < 32; animIndex++) {
      final int size = mrg.entries.get(animIndex).size.get();

      if(size != 0) {
        FUN_800c9a80(mrg.getFile(animIndex), size, 1, 0, combatantIndex, animIndex);
      }

      //LAB_800c94cc
    }
  }

  @Method(0x800c94f8L)
  public static int FUN_800c94f8(final int combatantIndex, final short a1) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];
    final int oldVal = combatant._1a4;
    combatant._1a4 = a1;
    return oldVal;
  }

  @Method(0x800c952cL)
  public static void FUN_800c952c(final Model124 model, final int combatantIndex) {
    final CombatantStruct1a8 s0 = combatants_8005e398[combatantIndex];

    final ExtendedTmd tmd;
    if(s0._1a4 >= 0) {
      tmd = MEMORY.ref(4, FUN_800cad34(s0._1a4), ExtendedTmd::new);
    } else {
      //LAB_800c9590
      if(s0.mrg_00 != null && s0.mrg_00.entries.get(32).size.get() != 0) {
        tmd = s0.mrg_00.getFile(32, ExtendedTmd::new);
      } else {
        throw new RuntimeException("anim undefined");
      }
    }

    //LAB_800c95bc
    s0.tmd_08 = tmd;

    final TmdAnimationFile anim = FUN_800ca31c(combatantIndex, 0);
    if((s0.flags_19e & 0x4) != 0) {
      final BattleStruct18cb0.Rendering1298 a0_0 = _1f8003f4._9ce8[s0.charSlot_19c];

      model.dobj2ArrPtr_00 = a0_0.dobj2s_00;
      model.coord2ArrPtr_04 = a0_0.coord2s_230;
      model.coord2ParamArrPtr_08 = a0_0.params_d20;
      model.count_c8 = 35;

      final long a3;
      if((s0.charIndex_1a2 & 0x1) != 0) {
        a3 = 0x9L;
      } else {
        a3 = s0.charIndex_1a2 - 0x200 >>> 1;
      }

      //LAB_800c9650
      FUN_80021520(model, tmd, anim, a3);
    } else {
      //LAB_800c9664
      initModel(model, tmd, anim);
    }

    //LAB_800c9680
    s0._14[0]._09++;
  }

  @Method(0x800c96acL)
  public static void deallocateModelIfMonster(final Model124 model, final int combatantIndex) {
    if((combatants_8005e398[combatantIndex].flags_19e & 0x4) == 0) {
      deallocateModel(model);
    }

    //LAB_800c96f8
  }

  @Method(0x800c9708L)
  public static void loadAttackAnimations(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.charIndex_1a2 >= 0 && combatant.mrg_04 == null) {
      combatant.flags_19e |= 0x10;

      final int fileIndex;
      if((combatant.flags_19e & 0x4) == 0) {
        // Enemy attack animations
        fileIndex = 3593 + combatant.charIndex_1a2;
        loadDrgnDir(0, fileIndex, files -> Bttl_800c.attackAnimationsLoaded(files, combatantIndex, true, -1));
      } else {
        //LAB_800c97a4
        final int isDragoon = combatant.charIndex_1a2 & 0x1;
        final int charIndex = gameState_800babc8.charIndex_88.get(combatant.charSlot_19c).get();
        if(isDragoon == 0) {
          // Additions
          fileIndex = 4031 + gameState_800babc8.charData_32c.get(charIndex).selectedAddition_19.get() + charIndex * 8 - additionOffsets_8004f5ac.get(charIndex).get();
        } else if(charIndex != 0 || (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
          // Dragoon addition
          fileIndex = 4103 + charIndex;
        } else { // Divine dragoon
          // Divine dragoon addition
          fileIndex = 4112;
        }

        loadDrgnDir(0, fileIndex, files -> Bttl_800c.attackAnimationsLoaded(files, combatantIndex, false, combatant.charSlot_19c));
      }
    }

    //LAB_800c9888
  }

  @Method(0x800c9898L)
  public static void attackAnimationsLoaded(final List<byte[]> files, final int combatantIndex, final boolean isMonster, final int charSlot) {
    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);

    if(combatant.mrg_04 == null) {
      final MrgFile mrg = MrgFile.alloc(files);

      //LAB_800c9910
      if(!isMonster && files.size() == 64) {
        _8006e398.y_d80[charSlot] = 0;

        //LAB_800c9940
        for(int animIndex = 0; animIndex < 32; animIndex++) {
          final int size = files.get(32 + animIndex).length;

          if(size != 0) {
            if(combatant._14[animIndex] != null && combatant._14[animIndex]._09 != 0) {
              FUN_800c9c7c(combatantIndex, animIndex);
            }

            //LAB_800c9974
            // Type 6 - TIM file (except it's loading animation data into VRAM???) TODO
            FUN_800c9a80(mrg.getFile(32 + animIndex), size, 6, charSlot, combatantIndex, animIndex);
          }
        }
      }

      //LAB_800c99d8
      combatant.mrg_04 = mrg;

      //LAB_800c99e8
      for(int animIndex = 0; animIndex < 32; animIndex++) {
        final int size = mrg.entries.get(animIndex).size.get();

        if(size != 0) {
          if(combatant._14[animIndex] != null && combatant._14[animIndex]._09 != 0) {
            FUN_800c9c7c(combatantIndex, animIndex);
          }

          //LAB_800c9a18
          FUN_800c9a80(mrg.getFile(animIndex), size, 2, 1, combatantIndex, animIndex);
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
  public static void FUN_800c9a80(final long addr, final int size, final int type, final int a3, final int combatantIndex, final int animIndex) {
    CombatantStruct1a8_c s3 = combatants_8005e398[combatantIndex]._14[animIndex];

    if(s3 != null) {
      FUN_800c9c7c(combatantIndex, animIndex);
    }

    //LAB_800c9b28
    if(type == 1) {
      //LAB_800c9b68
      final CombatantStruct1a8_c.AnimType anim = new CombatantStruct1a8_c.AnimType(MEMORY.ref(4, addr, TmdAnimationFile::new));
      anim._08 = a3;
      anim.type_0a = 1;
      anim._0b = 1;
      s3 = anim;
    } else if(type == 2) {
      //LAB_800c9b80
      //LAB_800c9b98
      final CombatantStruct1a8_c.AnimType anim = new CombatantStruct1a8_c.AnimType(MEMORY.ref(4, addr, TmdAnimationFile::new));
      anim._08 = a3;
      anim.type_0a = 2;
      anim._0b = 1;
      s3 = anim;
      //LAB_800c9b4c
    } else if(type == 3) {
      //LAB_800c9bb0
      final CombatantStruct1a8_c.IndexType index = new CombatantStruct1a8_c.IndexType(a3);
      index._08 = -1;
      index.type_0a = 3;
      index._0b = 1;
      s3 = index;
    } else if(type == 6) {
      //LAB_800c9bcc
      final RECT sp0x10 = new RECT((short)(512 + a3 * 64), (short)_8006e398.y_d80[a3], (short)64, (short)(size / 128));
      LoadImage(sp0x10, addr);

      _8006e398.y_d80[a3] += size / 128;

      final CombatantStruct1a8_c.TimType tim = new CombatantStruct1a8_c.TimType(sp0x10.x.get(), sp0x10.y.get(), sp0x10.h.get());
      tim._08 = -1;
      tim.type_0a = 6;
      tim._0b = 0;
      s3 = tim;
    } else {
      return;
    }

    //LAB_800c9c44
    s3.BttlStruct08_index_04 = -1;
    s3.BattleStructEf4Sub08_index_06 = -1;
    s3._09 = 0;

    combatants_8005e398[combatantIndex]._14[animIndex] = s3;

    //LAB_800c9c54
  }

  @Method(0x800c9c7cL)
  public static void FUN_800c9c7c(final int combatantIndex, final int animIndex) {
    final CombatantStruct1a8_c s0 = combatants_8005e398[combatantIndex]._14[animIndex];

    if(s0 != null) {
      //LAB_800c9cec
      while(s0._09 > 0) {
        FUN_800ca194(combatantIndex, animIndex);
      }

      //LAB_800c9d04
      if(s0 instanceof CombatantStruct1a8_c.IndexType index) {
        FUN_800cad64(index.index_00);
      }

      //LAB_800c9d84
      combatants_8005e398[combatantIndex]._14[animIndex] = null;
    }

    //LAB_800c9da0
  }

  @Method(0x800c9db8L)
  public static void FUN_800c9db8(final int combatantIndex, final int animIndex, final int a2) {
    FUN_800c9c7c(combatantIndex, animIndex);
    FUN_800c9a80(0, 0, 3, a2, combatantIndex, animIndex);
  }

  @Method(0x800c9e10L)
  public static boolean FUN_800c9e10(final int combatantIndex, final int animIndex) {
    final CombatantStruct1a8_c s0 = combatants_8005e398[combatantIndex]._14[animIndex];

    if(s0 instanceof CombatantStruct1a8_c.AnimType animType) {
      return animType.anim_00 != null;
    }

    if(s0 instanceof CombatantStruct1a8_c.IndexType indexType) {
      return indexType.index_00 >= 0;
    }

    if(s0 instanceof CombatantStruct1a8_c.TimType timType) {
      if(timType._0b == 0) {
        final int s1 = FUN_800cab58(timType.h_03 * 0x80, 3, 0, 0);
        if(s1 < 0) {
          return false;
        }

        final RECT sp0x20 = new RECT((short)timType.x_00, (short)timType.y_02, (short)64, (short)timType.h_03);
        StoreImage(sp0x20, FUN_800cad34(s1));
        s0.BttlStruct08_index_04 = s1;
        s0._0b = 1;
      }

      //LAB_800c9fb4
      return true;
    }

    return false;
  }

  @Method(0x800ca054L)
  public static long FUN_800ca054(final int combatantIndex, final int animIndex) {
    if(combatants_8005e398[combatantIndex]._14[animIndex] instanceof CombatantStruct1a8_c.AnimType || combatants_8005e398[combatantIndex]._14[animIndex] instanceof CombatantStruct1a8_c.IndexType) {
      return 1;
    }

    if(combatants_8005e398[combatantIndex]._14[animIndex] instanceof CombatantStruct1a8_c.TimType timType) {
      if(timType._0b != 0) {
        return timType.BttlStruct08_index_04 >= 0 ? 1 : 0;
      }
    }

    //LAB_800ca0f8
    return 0;
  }

  @Method(0x800ca100L)
  public static void FUN_800ca100(final Model124 model, final int combatantIndex, final int animIndex) {
    loadModelStandardAnimation(model, FUN_800ca31c(combatantIndex, animIndex));
    combatants_8005e398[combatantIndex]._14[0]._09++;
  }

  @Method(0x800ca194L)
  public static boolean FUN_800ca194(final int combatantIndex, final int animIndex) {
    final CombatantStruct1a8_c s0 = combatants_8005e398[combatantIndex]._14[animIndex];

    if(s0 != null) {
      if(s0._09 > 0) {
        s0._09--;
      }

      //LAB_800ca1f4
      final int type = s0.type_0a;

      if(type < 4) {
        return true;
      }

      if(s0._09 == 0) {
        if(s0.BttlStruct08_index_04 >= 0) {
          FUN_800cad64(s0.BttlStruct08_index_04);
        }

        //LAB_800ca240
        s0.BttlStruct08_index_04 = -1;
        s0.BattleStructEf4Sub08_index_06 = -1;
        s0._0b = 0;
      }
    }

    //LAB_800ca258
    //LAB_800ca25c
    return true;
  }

  @Method(0x800ca26cL)
  public static boolean FUN_800ca26c(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    //LAB_800ca2bc
    boolean s3 = true;
    for(int i = 0; i < 32; i++) {
      if(combatant._14[i] != null && combatant._14[i]._09 == 0) {
        if(FUN_800ca194(combatantIndex, i)) {
          s3 = !s3;
        } else {
          s3 = false;
        }
      }

      //LAB_800ca2e8
    }

    return s3;
  }

  @Method(0x800ca31cL)
  public static TmdAnimationFile FUN_800ca31c(final int combatantIndex, final int animIndex) {
    final CombatantStruct1a8_c a0_0 = combatants_8005e398[combatantIndex]._14[animIndex];

    if(a0_0 instanceof CombatantStruct1a8_c.AnimType animType) {
      return animType.anim_00;
    }

    if(a0_0 instanceof CombatantStruct1a8_c.IndexType indexType) {
      final int s0 = indexType.index_00;

      if(indexType._09 == 0 || encounterId_800bb0f8.get() != 443) { // Melbu
        //LAB_800ca3c4
        reallocSomething(s0);
      }

      return MEMORY.ref(4, FUN_800cad34(s0), TmdAnimationFile::new); //TODO
    }

    if(a0_0 instanceof CombatantStruct1a8_c.TimType timType) {
      if(timType._0b != 0) {
        final int s0 = timType.BttlStruct08_index_04;

        if(s0 >= 0) {
          //LAB_800ca3f4
          return MEMORY.ref(4, FUN_800cad34(s0), TmdAnimationFile::new);
        }
      }
    }

    return null;
  }

  @Method(0x800ca418L)
  public static void FUN_800ca418(final int index) {
    final CombatantStruct1a8 combatant = combatants_8005e398[index];

    if((combatant.flags_19e & 0x4) != 0) {
      _8006e398.y_d80[combatant.charSlot_19c] = 0;
    }

    //LAB_800ca488
    //LAB_800ca494
    for(int i = 0; i < 32; i++) {
      if(combatant._14[i] instanceof CombatantStruct1a8_c.AnimType && combatant._14[i].type_0a == 2 || combatant._14[i] instanceof CombatantStruct1a8_c.TimType) {
        //LAB_800ca4c0
        FUN_800c9c7c(index, i);
      }

      //LAB_800ca4cc
    }

    if(combatant.mrg_04 != null) {
      free(combatant.mrg_04.getAddress());
      combatant.mrg_04 = null;
    }

    //LAB_800ca4f8
    combatant.flags_19e &= 0xffef;
  }

  @Method(0x800ca528L)
  public static int FUN_800ca528(final int combatantIndex, final int a1) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];
    final int oldVal = combatant._1a6;
    combatant._1a6 = a1;
    return oldVal;
  }

  @Method(0x800ca55cL)
  public static void FUN_800ca55c(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398[combatantIndex];

    if(combatant.charIndex_1a2 >= 0) {
      int fileIndex = gameState_800babc8.charIndex_88.get(combatant.charSlot_19c).get();

      if((combatant.charIndex_1a2 & 0x1) != 0) {
        if(fileIndex == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) {
          fileIndex = 10;
        }

        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/dragoon".formatted(charName), files -> Bttl_800c.FUN_800ca65c(files, combatantIndex));
      } else {
        final String charName = getCharacterName(fileIndex).toLowerCase();
        loadFile("characters/%s/textures/combat".formatted(charName), files -> Bttl_800c.FUN_800ca65c(files, combatantIndex));
      }
    }

    //LAB_800ca64c
  }

  @Method(0x800ca65cL)
  public static void FUN_800ca65c(final byte[] data, final int combatantIndex) {
    final long tim = mallocTail(data.length);
    MEMORY.setBytes(tim, data);
    loadCombatantTim(combatantIndex, tim);
    free(tim);
  }

  @Method(0x800ca75cL)
  public static void loadCombatantTim(final int combatantIndex, final long timFile) {
    final int a0;

    if(combatantIndex >= 0) {
      //LAB_800ca77c
      final CombatantStruct1a8 s0 = getCombatant(combatantIndex);

      if(s0.colourMap_1a0 == 0) {
        final int charSlot = s0.charSlot_19c;

        if(charSlot < 0) {
          s0.colourMap_1a0 = FUN_800ca89c(s0.charIndex_1a2);
        } else {
          //LAB_800ca7c4
          s0.colourMap_1a0 = charSlot + 1;
        }
      }

      a0 = s0.colourMap_1a0;
    } else {
      a0 = 0;
    }

    //LAB_800ca7d0
    loadCombatantTim2(a0, timFile);
  }

  @Method(0x800ca7ecL)
  public static void loadCombatantTim2(final int a0, final long timFile) {
    final TimHeader header = parseTimHeader(MEMORY.ref(4, timFile + 0x4L));

    if(a0 != 0) {
      //LAB_800ca83c
      final RECT s0 = _800fa6e0.get(a0);
      LoadImage(s0, header.getImageAddress());

      if((header.flags.get() & 0x8L) != 0) {
        header.clutRect.x.set(s0.x.get());
        header.clutRect.y.set((short)(s0.y.get() + 240));

        //LAB_800ca884
        LoadImage(header.clutRect, header.getClutAddress());
      }
    } else {
      LoadImage(header.imageRect, header.getImageAddress());

      if((header.flags.get() & 0x8L) != 0) {
        LoadImage(header.clutRect, header.getClutAddress());
      }
    }

    //LAB_800ca88c
  }

  @Method(0x800ca89cL)
  public static int FUN_800ca89c(final int a0) {
    //LAB_800ca8ac
    //LAB_800ca8c4
    for(int i = a0 < 0x200 ? 4 : 1; i < 9; i++) {
      final int a0_0 = 0x1 << i;

      if((_800c66c4.get() & a0_0) == 0) {
        _800c66c4.oru(a0_0);
        return i;
      }

      //LAB_800ca8e4
    }

    //LAB_800ca8f4
    return 0;
  }

  @Method(0x800ca8fcL)
  public static void FUN_800ca8fc(final int shift) {
    _800c66c4.oru(1L << shift);
  }

  @Method(0x800ca918L)
  public static void FUN_800ca918(final int shift) {
    _800c66c4.and(~(1L << shift));
  }

  @Method(0x800ca938L)
  public static short getCombatantColourMap(final int combatantIndex) {
    return colourMaps_800fa730.get(combatants_8005e398[combatantIndex].colourMap_1a0).get();
  }

  @Method(0x800ca980L)
  public static void FUN_800ca980() {
    _800c66c1.setu(0x1L);
  }

  @Method(0x800ca9b4L)
  public static void FUN_800ca9b4() {
    _800c66c1.setu(0);

    //LAB_800ca9d8
    for(int s1 = 0; s1 < 0x100; s1++) {
      final BttlStruct08 s0 = _8006e398._580[s1];
      if(s0._04 >= 2) {
        free(s0.ptr_00);
        s0._04 = 0;
      }
    }
  }

  @Method(0x800caa20L)
  public static int FUN_800caa20() {
    _800c66b4.addu(0x1L);
    if(_800c66b4.get() >= 0x100L) {
      _800c66b4.setu(0);
    }

    //LAB_800caa44
    //LAB_800caa64
    for(int i = (int)_800c66b4.get(); i < 0x100; i++) {
      final BttlStruct08 a1 = _8006e398._580[i];

      if(a1._04 == 0) {
        //LAB_800caacc
        _800c66b4.setu(i);
        a1.ptr_00 = 0;
        a1._04 = 1;
        return i;
      }
    }

    //LAB_800caa88
    //LAB_800caaa4
    for(int i = 0; i < _800c66b4.get(); i++) {
      final BttlStruct08 a1 = _8006e398._580[i];

      if(a1._04 == 0) {
        //LAB_800caacc
        _800c66b4.setu(i);
        a1.ptr_00 = 0;
        a1._04 = 1;
        return i;
      }
    }

    //LAB_800caac4
    return -1;
  }

  @Method(0x800caae4L)
  public static int FUN_800caae4(final long s0, final int a1, final int a2, final int a3) {
    final int index = FUN_800caa20();
    if(index < 0) {
      //LAB_800cab38
      return -1;
    }

    final BttlStruct08 a0 = _8006e398._580[index];
    a0.ptr_00 = s0;
    a0._04 = a1;
    a0._05 = a2;
    a0._06 = a3;

    //LAB_800cab3c
    return index;
  }

  @Method(0x800cab58L)
  public static int FUN_800cab58(final long size, final int a1, final int a2, final int a3) {
    final long s0 = mallocTail(size);
    final int v0 = FUN_800caae4(s0, a1, a2, a3);
    if(v0 < 0) {
      free(s0);
      return -1;
    }

    return v0;
  }

  @Method(0x800cac38L)
  public static int FUN_800cac38(final int drgnIndex, final int fileIndex) {
    final int s0 = FUN_800caa20();

    if(s0 < 0) {
      //LAB_800cac94
      return -1;
    }

    loadDrgnBinFile(drgnIndex, fileIndex, 0, Bttl_800c::FUN_800cacb0, s0, 0x2L);

    //LAB_800cac98
    return s0;
  }

  @Method(0x800cacb0L)
  public static void FUN_800cacb0(final long address, final int size, final int index) {
    final BttlStruct08 a1 = _8006e398._580[index];

    if(a1._04 == 1) {
      a1.ptr_00 = address;
      a1._04 = 2;
      reallocSomething(index);
    } else {
      //LAB_800cacf4
      free(address);
    }

    //LAB_800cad04
  }

  @Method(0x800cad34L)
  public static long FUN_800cad34(final int index) {
    return _8006e398._580[index].ptr_00;
  }

  @Method(0x800cad50L)
  public static BttlStruct08 FUN_800cad50(final int index) {
    return _8006e398._580[index];
  }

  @Method(0x800cad64L)
  public static void FUN_800cad64(final int index) {
    final BttlStruct08 s0 = _8006e398._580[index];

    if(s0._04 != 1) {
      deferReallocOrFree(s0.ptr_00, 0, 1);
      s0.ptr_00 = 0;
    }

    //LAB_800cada8
    s0._04 = 0;
  }

  @Method(0x800cadbcL)
  public static long reallocSomething(final int index) {
    final BttlStruct08 s1 = _8006e398._580[index];
    final long currentAddress = s1.ptr_00;

    final long newAddress = realloc2(currentAddress, getMallocSize(currentAddress));
    if(newAddress == 0 || newAddress == currentAddress) {
      //LAB_800cae1c
      return -1;
    }

    //LAB_800cae24
    s1.ptr_00 = newAddress;

    //LAB_800cae2c
    return index;
  }

  @Method(0x800cae44L)
  public static void FUN_800cae44() {
    _800c675c.setu(0);
  }

  @Method(0x800cae50L)
  public static void bobjTicker(final ScriptState<BattleObject27c> state, final BattleObject27c bobj) {
    bobj._278 = 0;

    final int v1;
    if((state.storage_44[7] & 0x4) != 0) {
      v1 = _800bc960.get() & 0x110;
    } else {
      //LAB_800cae94
      v1 = _800bc960.get() & 0x210;
    }

    //LAB_800cae98
    if(v1 != 0 && FUN_800c90b0(bobj.combatantIndex_26c) != 0) {
      bobj.model_148.colourMap_9d = getCombatantColourMap(bobj.combatantIndex_26c);
      bobj.animIndex_26e = 0;
      FUN_800c952c(bobj.model_148, bobj.combatantIndex_26c);
      bobj._278 = 1;
      bobj.animIndex_270 = -1;

      if((state.storage_44[7] & 0x800) == 0) {
        final ScriptFile script;
        if((state.storage_44[7] & 0x4) != 0) {
          script = getCombatantScript(bobj.combatantIndex_26c);
        } else {
          //LAB_800caf18
          script = script_800c66fc;
        }

        //LAB_800caf20
        state.loadScriptFile(script);
      }

      //LAB_800caf2c
      state.setTicker(Bttl_800c::FUN_800caf50);
    }

    //LAB_800caf38
  }

  @Method(0x800caf2cL)
  public static void FUN_800caf50(final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    state.setRenderer(Bttl_800c::FUN_800cb024);
    state.setTicker(Bttl_800c::FUN_800cafb4);
    FUN_800cafb4(state, data);
  }

  @Method(0x800cafb4L)
  public static void FUN_800cafb4(final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    if((state.storage_44[7] & 0x211) == 0) {
      applyModelRotationAndScale(data.model_148);
      if((state.storage_44[7] & 0x80) == 0 || data.model_148.s_9e != 0) {
        //LAB_800cb004
        animateModel(data.model_148);
      }
    }

    //LAB_800cb00c
  }

  @Method(0x800cb024L)
  public static void FUN_800cb024(final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    if((state.storage_44[7] & 0x211) == 0) {
      renderModel(data.model_148);
    }

    //LAB_800cb048
  }

  @Method(0x800cb058L)
  public static void bobjDestructor(final ScriptState<BattleObject27c> state, final BattleObject27c bobj) {
    if(bobj._278 != 0) {
      deallocateModelIfMonster(bobj.model_148, bobj.combatantIndex_26c);
    }

    //LAB_800cb088
    FUN_800ca194(bobj.combatantIndex_26c, bobj.animIndex_26e);

    _800c66d0.decr();

    //LAB_800cb0d4
    for(int i = bobj._274; i < _800c66d0.get(); i++) {
      _8006e398.bobjIndices_e0c[i] = _8006e398.bobjIndices_e0c[i + 1];
      _8006e398.bobjIndices_e0c[i].innerStruct_00._274 = i;
    }

    //LAB_800cb11c
    if((state.storage_44[7] & 0x4) != 0) {
      monsterCount_800c6768.decr();

      //LAB_800cb168
      for(int i = bobj.charSlot_276; i < monsterCount_800c6768.get(); i++) {
        _8006e398.bobjIndices_e50[i] = _8006e398.bobjIndices_e50[i + 1];
        _8006e398.bobjIndices_e50[i].innerStruct_00.charSlot_276 = i;
      }
    } else {
      //LAB_800cb1b8
      charCount_800c677c.decr();

      //LAB_800cb1f4
      for(int i = bobj.charSlot_276; i < charCount_800c677c.get(); i++) {
        _8006e398.charBobjIndices_e40[i] = _8006e398.charBobjIndices_e40[i + 1];
        _8006e398.charBobjIndices_e40[i].innerStruct_00.charSlot_276 = i;
      }
    }

    //LAB_800cb23c
  }

  @Method(0x800cb250L)
  public static boolean FUN_800cb250(final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    int x = state._e8;
    int y = state._ec;
    int z = state._f0;

    if(state.scriptState_c8 != null) {
      final BattleObject27c data2 = state.scriptState_c8.innerStruct_00;

      x += data2.model_148.coord2_14.coord.transfer.getX();
      y += data2.model_148.coord2_14.coord.transfer.getY();
      z += data2.model_148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cb2ac
    state._cc--;
    if(state._cc > 0) {
      state._d0 -= state._dc;
      state._d4 -= state._e0;
      state._d8 -= state._e4;
      data.model_148.coord2_14.coord.transfer.setX(x - (state._d0 >> 8));
      data.model_148.coord2_14.coord.transfer.setY(y - (state._d4 >> 8));
      data.model_148.coord2_14.coord.transfer.setZ(z - (state._d8 >> 8));
      state._e0 += state._f4;
      return false;
    }

    //LAB_800cb338
    data.model_148.coord2_14.coord.transfer.set(x, y, z);
    return true;
  }

  @Method(0x800cb34cL)
  public static boolean FUN_800cb34c(final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    final BattleObject27c bobj = state.scriptState_c8.innerStruct_00;
    final VECTOR vec = bobj.model_148.coord2_14.coord.transfer;
    final int a0 = ratan2(vec.getX() - data.model_148.coord2_14.coord.transfer.getX(), vec.getZ() - data.model_148.coord2_14.coord.transfer.getZ()) + 0x800;
    state._cc--;
    if(state._cc > 0) {
      state._d0 -= state._d4;
      data.model_148.coord2Param_64.rotate.setY((short)(a0 + state._d0));
      return false;
    }

    //LAB_800cb3e0
    data.model_148.coord2Param_64.rotate.setY((short)a0);

    //LAB_800cb3e8
    return true;
  }

  @Method(0x800cb3fcL)
  public static FlowControl scriptSetBobjPos(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.coord2_14.coord.transfer.setX(script.params_20[1].get());
    bobj.model_148.coord2_14.coord.transfer.setY(script.params_20[2].get());
    bobj.model_148.coord2_14.coord.transfer.setZ(script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb468L)
  public static FlowControl scriptGetBobjPos(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.model_148.coord2_14.coord.transfer.getX());
    script.params_20[2].set(bobj.model_148.coord2_14.coord.transfer.getY());
    script.params_20[3].set(bobj.model_148.coord2_14.coord.transfer.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb4c8L)
  public static FlowControl scriptSetBobjRotation(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.coord2Param_64.rotate.setX((short)script.params_20[1].get());
    bobj.model_148.coord2Param_64.rotate.setY((short)script.params_20[2].get());
    bobj.model_148.coord2Param_64.rotate.setZ((short)script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb534L)
  public static FlowControl scriptSetBobjRotationY(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.coord2Param_64.rotate.setY((short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb578L)
  public static FlowControl scriptGetBobjRotation(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.model_148.coord2Param_64.rotate.getX());
    script.params_20[2].set(bobj.model_148.coord2Param_64.rotate.getY());
    script.params_20[3].set(bobj.model_148.coord2Param_64.rotate.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb5d8L)
  public static FlowControl scriptGetMonsterStatusResistFlags(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.monsterStatusResistFlag_76);
    return FlowControl.CONTINUE;
  }

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

  @Method(0x800cb674L)
  public static FlowControl FUN_800cb674(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.ub_a2 = script.params_20[1].get() < 1 ? 1 : 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb6bcL)
  public static FlowControl FUN_800cb6bc(final RunningScript<?> a0) {
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    if((v0.storage_44[7] & 0x1) != 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    final BattleObject27c s0 = (BattleObject27c)v0.innerStruct_00;
    final int animIndex = a0.params_20[1].get();

    if(s0.animIndex_270 < 0) {
      FUN_800c9e10(s0.combatantIndex_26c, animIndex);
      s0.animIndex_270 = animIndex;
    } else if(s0.animIndex_270 != animIndex) {
      FUN_800ca194(s0.combatantIndex_26c, s0.animIndex_270);

      //LAB_800cb73c
      FUN_800c9e10(s0.combatantIndex_26c, animIndex);
      s0.animIndex_270 = animIndex;
    }

    //LAB_800cb750
    return FlowControl.PAUSE;
  }

  @Method(0x800cb764L)
  public static FlowControl FUN_800cb764(final RunningScript<?> a0) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb76cL)
  public static FlowControl FUN_800cb76c(final RunningScript<?> a0) {
    final ScriptState<?> s2 = scriptStatePtrArr_800bc1c0[a0.params_20[0].get()];
    final BattleObject27c s0 = (BattleObject27c)s2.innerStruct_00;
    if((s2.storage_44[7] & 0x1) == 0) {
      int animIndex = s0.animIndex_270;

      if(animIndex < 0) {
        animIndex = 0;
      }

      //LAB_800cb7d0
      if(FUN_800ca054(s0.combatantIndex_26c, animIndex) != 0) {
        FUN_800ca194(s0.combatantIndex_26c, s0.animIndex_26e);
        FUN_800ca100(s0.model_148, s0.combatantIndex_26c, animIndex);
        s2.storage_44[7] &= 0xffff_ff6f;
        s0.model_148.ub_9c = 1;
        s0.animIndex_26e = animIndex;
        s0.animIndex_270 = -1;
        return FlowControl.CONTINUE;
      }
    }

    //LAB_800cb830
    //LAB_800cb834
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800cb84cL)
  public static FlowControl FUN_800cb84c(final RunningScript<?> script) {
    final ScriptState<?> s2 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleObject27c s0 = (BattleObject27c)s2.innerStruct_00;

    if((s2.storage_44[7] & 0x1) == 0) {
      final int newAnim = script.params_20[1].get();
      final int currentAnim = s0.animIndex_270;

      if(currentAnim >= 0) {
        if(currentAnim != newAnim) {
          FUN_800ca194(s0.combatantIndex_26c, currentAnim);
        }

        //LAB_800cb8d0
        s0.animIndex_270 = -1;
      }

      //LAB_800cb8d4
      if(FUN_800ca054(s0.combatantIndex_26c, newAnim) != 0) {
        FUN_800ca194(s0.combatantIndex_26c, s0.animIndex_26e);
        FUN_800ca100(s0.model_148, s0.combatantIndex_26c, newAnim);
        s2.storage_44[7] &= 0xffff_ff6f;
        s0.model_148.ub_9c = 1;
        s0.animIndex_26e = newAnim;
        s0.animIndex_270 = -1;
        return FlowControl.CONTINUE;
      }

      //LAB_800cb934
      FUN_800c9e10(s0.combatantIndex_26c, newAnim);
    }

    //LAB_800cb944
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800cb95cL)
  public static FlowControl FUN_800cb95c(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800ca26c(bobj.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb9b0L)
  public static FlowControl FUN_800cb9b0(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.animIndex_26e);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cb9f0L)
  public static FlowControl FUN_800cb9f0(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.ub_9c = 2;
    return FlowControl.CONTINUE;
  }

  @Method(0x800cba28L)
  public static FlowControl FUN_800cba28(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.ub_9c = 1;
    return FlowControl.CONTINUE;
  }

  @Method(0x800cba60L)
  public static FlowControl FUN_800cba60(final RunningScript<?> script) {
    //LAB_800cbab0
    if(script.params_20[1].get() != 0) {
      scriptStatePtrArr_800bc1c0[script.params_20[0].get()].storage_44[7] &= 0xffff_ff7f;
    } else {
      //LAB_800cbaa4
      scriptStatePtrArr_800bc1c0[script.params_20[0].get()].storage_44[7] |= 0x80;
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x800cbabcL)
  public static FlowControl FUN_800cbabc(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.model_148.s_9e < 1 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cbb00L)
  public static FlowControl FUN_800cbb00(final RunningScript<?> t1) {
    final int scriptIndex = t1.params_20[0].get();
    final ScriptState<BattleObject27c> state = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex];
    BattleObject27c v1 = state.innerStruct_00;

    int x = v1.model_148.coord2_14.coord.transfer.getX();
    int y = v1.model_148.coord2_14.coord.transfer.getY();
    int z = v1.model_148.coord2_14.coord.transfer.getZ();

    final int t0 = t1.params_20[1].get();
    if(t0 >= 0) {
      state.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[t0];
      v1 = state.scriptState_c8.innerStruct_00;
      x -= v1.model_148.coord2_14.coord.transfer.getX();
      y -= v1.model_148.coord2_14.coord.transfer.getY();
      z -= v1.model_148.coord2_14.coord.transfer.getZ();
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbb98
    FUN_800cdc1c(state, x, y, z, t1.params_20[3].get(), t1.params_20[4].get(), t1.params_20[5].get(), 0, t1.params_20[2].get());
    state.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cbc14L)
  public static FlowControl FUN_800cbc14(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final ScriptState<BattleObject27c> state = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleObject27c bobj1 = state.innerStruct_00;
    final VECTOR vec = new VECTOR().set(bobj1.model_148.coord2_14.coord.transfer);
    final int scriptIndex2 = script.params_20[1].get();

    if(scriptIndex2 >= 0) {
      state.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleObject27c bobj2 = state.scriptState_c8.innerStruct_00;
      vec.sub(bobj2.model_148.coord2_14.coord.transfer);
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbcc4
    final int x = script.params_20[3].get() - vec.getX();
    final int y = script.params_20[4].get() - vec.getY();
    final int z = script.params_20[5].get() - vec.getZ();
    FUN_800cdc1c(state, vec.getX(), vec.getY(), vec.getZ(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0, SquareRoot0(x * x + y * y + z * z) / script.params_20[2].get());
    state.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cbde0L)
  public static FlowControl FUN_800cbde0(final RunningScript<?> script) {
    final ScriptState<BattleObject27c> state = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    BattleObject27c bobj = state.innerStruct_00;
    int a1 = bobj.model_148.coord2_14.coord.transfer.getX();
    int a2 = bobj.model_148.coord2_14.coord.transfer.getY();
    int a3 = bobj.model_148.coord2_14.coord.transfer.getZ();

    if(script.params_20[1].get() >= 0) {
      state.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];
      bobj = state.scriptState_c8.innerStruct_00;
      a1 -= bobj.model_148.coord2_14.coord.transfer.getX();
      a2 -= bobj.model_148.coord2_14.coord.transfer.getY();
      a3 -= bobj.model_148.coord2_14.coord.transfer.getZ();
    } else {
      state.scriptState_c8 = null;
    }

    //LAB_800cbe78
    FUN_800cdc1c(state, a1, a2, a3, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), 0x20, script.params_20[2].get());
    state.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cbef8L)
  public static FlowControl FUN_800cbef8(final RunningScript<?> state) {
    final int scriptIndex1 = state.params_20[0].get();
    final int scriptIndex2 = state.params_20[1].get();
    final ScriptState<BattleObject27c> s5 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleObject27c bobj1 = s5.innerStruct_00;
    final VECTOR vec = new VECTOR().set(bobj1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      s5.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleObject27c bobj2 = s5.scriptState_c8.innerStruct_00;
      vec.sub(bobj2.model_148.coord2_14.coord.transfer);
    } else {
      s5.scriptState_c8 = null;
    }

    //LAB_800cbfa8
    final int x = state.params_20[3].get() - vec.getX();
    final int y = state.params_20[4].get() - vec.getY();
    final int z = state.params_20[5].get() - vec.getZ();
    FUN_800cdc1c(s5, vec.getX(), vec.getY(), vec.getZ(), state.params_20[3].get(), state.params_20[4].get(), state.params_20[5].get(), 0x20, SquareRoot0(x * x + y * y + z * z) / state.params_20[2].get());
    s5.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc0c8L)
  public static FlowControl FUN_800cc0c8(final RunningScript<?> script) {
    final int s0 = script.params_20[0].get();
    final ScriptState<BattleObject27c> a0 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[s0];
    final VECTOR a1 = new VECTOR().set(a0.innerStruct_00.model_148.coord2_14.coord.transfer);
    final int t0 = script.params_20[1].get();

    if(t0 >= 0) {
      a0.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[t0];
      a1.sub(a0.scriptState_c8.innerStruct_00.model_148.coord2_14.coord.transfer);
    } else {
      a0.scriptState_c8 = null;
    }

    //LAB_800cc160
    FUN_800cdc1c(a0, a1.getX(), a1.getY(), a1.getZ(), script.params_20[3].get(), a1.getY(), script.params_20[4].get(), 0, script.params_20[2].get());
    a0.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc1ccL)
  public static FlowControl FUN_800cc1cc(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleObject27c> state1 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleObject27c bobj1 = state1.innerStruct_00;
    final VECTOR vec = new VECTOR().set(bobj1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      state1.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleObject27c bobj2 = state1.scriptState_c8.innerStruct_00;
      vec.sub(bobj2.model_148.coord2_14.coord.transfer);
    } else {
      state1.scriptState_c8 = null;
    }

    //LAB_800cc27c
    final int x = script.params_20[3].get() - vec.getX();
    final int z = script.params_20[4].get() - vec.getZ();
    FUN_800cdc1c(state1, vec.getX(), vec.getY(), vec.getZ(), script.params_20[3].get(), vec.getY(), script.params_20[4].get(), 0, SquareRoot0(x * x + z * z) / script.params_20[2].get());
    state1.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc364L)
  public static FlowControl FUN_800cc364(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleObject27c> state1 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleObject27c bobj1 = state1.innerStruct_00;
    final VECTOR vec = new VECTOR().set(bobj1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      state1.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleObject27c bobj2 = state1.scriptState_c8.innerStruct_00;
      vec.sub(bobj2.model_148.coord2_14.coord.transfer);
    } else {
      state1.scriptState_c8 = null;
    }

    //LAB_800cc3fc
    FUN_800cdc1c(state1, vec.getX(), vec.getY(), vec.getZ(), script.params_20[3].get(), vec.getY(), script.params_20[4].get(), 0x20, script.params_20[2].get());
    state1.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc46cL)
  public static FlowControl FUN_800cc46c(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleObject27c> s5 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final BattleObject27c bobj1 = s5.innerStruct_00;
    final VECTOR vec = new VECTOR().set(bobj1.model_148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      s5.scriptState_c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
      final BattleObject27c bobj2 = s5.scriptState_c8.innerStruct_00;
      vec.sub(bobj2.model_148.coord2_14.coord.transfer);
    } else {
      s5.scriptState_c8 = null;
    }

    //LAB_800cc51c
    final int x = script.params_20[3].get() - vec.getX();
    final int z = script.params_20[4].get() - vec.getZ();
    FUN_800cdc1c(s5, vec.getX(), vec.getY(), vec.getZ(), script.params_20[3].get(), vec.getY(), script.params_20[4].get(), 0x20, SquareRoot0(x * x + z * z) / script.params_20[2].get());
    s5.setTempTicker(Bttl_800c::FUN_800cb250);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc608L)
  public static FlowControl FUN_800cc608(final RunningScript<?> script) {
    final BattleObject27c s0 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleObject27c v0 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    s0.model_148.coord2Param_64.rotate.setY((short)(ratan2(v0.model_148.coord2_14.coord.transfer.getX() - s0.model_148.coord2_14.coord.transfer.getX(), v0.model_148.coord2_14.coord.transfer.getZ() - s0.model_148.coord2_14.coord.transfer.getZ()) + 0x800L));
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc698L)
  public static FlowControl FUN_800cc698(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final ScriptState<BattleObject27c> state1 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex1];
    final ScriptState<BattleObject27c> state2 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[scriptIndex2];
    final BattleObject27c bobj1 = state1.innerStruct_00;
    final BattleObject27c bobj2 = state2.innerStruct_00;
    final int s2 = script.params_20[2].get();
    int v0 = ratan2(bobj2.model_148.coord2_14.coord.transfer.getX() - bobj1.model_148.coord2_14.coord.transfer.getX(), bobj2.model_148.coord2_14.coord.transfer.getZ() - bobj1.model_148.coord2_14.coord.transfer.getZ()) - bobj1.model_148.coord2Param_64.rotate.getY() + 0x1000;
    v0 = v0 & 0xfff;
    v0 = v0 - 0x800;
    state1.scriptState_c8 = state2;
    state1._cc = s2;
    state1._d0 = v0;
    state1._d4 = v0 / s2;
    state1.setTempTicker(Bttl_800c::FUN_800cb34c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc784L)
  public static FlowControl FUN_800cc784(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800ca418(bobj.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc7d8L)
  public static FlowControl FUN_800cc7d8(final RunningScript<?> script) {
    final ScriptState<?> v1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final long s2 = v1.storage_44[7] & 0x4;
    final int s1 = ((BattleObject27c)v1.innerStruct_00).combatantIndex_26c;

    //LAB_800cc83c
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      if(i != s1) {
        final CombatantStruct1a8 combatant = getCombatant(i);

        if((combatant.flags_19e & 0x1) != 0 && combatant.mrg_04 != null && combatant.charIndex_1a2 >= 0) {
          final int v0 = combatant.flags_19e >>> 2 ^ 1;

          if(s2 == 0) {
            //LAB_800cc8ac
            if((v0 & 1) == 0) {
              //LAB_800cc8b4
              FUN_800ca418(i);
            }
          } else {
            if((v0 & 1) != 0) {
              FUN_800ca418(i);
            }
          }
        }
      }

      //LAB_800cc8bc
    }

    //LAB_800cc8d8
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc8f4L)
  public static FlowControl FUN_800cc8f4(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    loadAttackAnimations(bobj.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc948L)
  public static FlowControl FUN_800cc948(final RunningScript<?> script) {
    //LAB_800cc970
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 v1 = getCombatant(i);
      if((v1.flags_19e & 0x1) != 0 && v1.charIndex_1a2 >= 0) {
        loadAttackAnimations(i);
      }

      //LAB_800cc9a8
    }

    //LAB_800cc9c0
    return FlowControl.CONTINUE;
  }

  @Method(0x800cc9d8L)
  public static FlowControl FUN_800cc9d8(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    bobj.model_148.aub_ec[script.params_20[1].get()] = script.params_20[2].get() > 0 ? 1 : 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800cca34L)
  public static FlowControl FUN_800cca34(final RunningScript<BattleObject27c> script) {
    if(_800c675c.get() != script.params_20[0].get() || (script.scriptState_04.storage_44[7] & 0x1000) != 0) {
      //LAB_800cca7c
      final int a1 = script.params_20[0].get();
      final int a2;

      if(script.paramCount_14 == 2) {
        a2 = 0;
      } else {
        //LAB_800ccaa0
        a2 = script.params_20[1].get();
      }

      //LAB_800ccab4
      FUN_800f6134(script.scriptState_04, a1, a2);

      script.scriptState_04.storage_44[7] &= 0xffff_efff;
      _800c675c.setu(script.params_20[0].get());
    }

    //LAB_800ccaec
    FUN_800f8c38(0x1L);

    final int s0 = FUN_800f6330();
    if(s0 == 0) {
      //LAB_800ccb24
      return FlowControl.PAUSE_AND_REWIND;
    }

    FUN_800f8c38(0);
    script.params_20[2].set(s0 - 1);

    //LAB_800ccb28
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccb3cL)
  public static FlowControl scriptRenderDamage(final RunningScript<?> script) {
    Bttl_800f.renderDamage(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccb70L)
  public static FlowControl FUN_800ccb70(final RunningScript<?> script) {
    setFloatingNumCoordsAndRender(script.params_20[0].get(), script.params_20[1].get(), 0xdL);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccba4L)
  public static FlowControl FUN_800ccba4(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleObject27c bobj = (BattleObject27c)state.innerStruct_00;
    final CombatantStruct1a8 combatant = bobj.combatant_144;
    final int combatantIndex = bobj.combatantIndex_26c;

    if((state.storage_44[7] & 0x1) == 0) {
      if(bobj.animIndex_270 >= 0) {
        FUN_800ca194(combatantIndex, bobj.animIndex_270);
      }

      //LAB_800ccc24
      deallocateModelIfMonster(bobj.model_148, combatantIndex);
      deallocateCombatant(combatantIndex);

      if(script.params_20[1].get() != 0) {
        state.storage_44[7] |= 0x3;
        combatant.charIndex_1a2 |= 0x1;
      } else {
        //LAB_800ccc60
        state.storage_44[7] = state.storage_44[7] & 0xffff_fffd | 0x1;
        combatant.charIndex_1a2 &= 0xfffe;
      }

      //LAB_800ccc78
      FUN_800ca55c(combatantIndex);
      loadCombatantTmdAndAnims(combatantIndex);
      //LAB_800ccc94
    } else if((combatant.flags_19e & 0x20) == 0) {
      FUN_800c952c(bobj.model_148, combatantIndex);
      bobj.animIndex_26e = 0;
      bobj.animIndex_270 = -1;
      state.storage_44[7] &= 0xffff_fffe;
      return FlowControl.CONTINUE;
    }

    //LAB_800ccccc
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800cccf4L)
  public static FlowControl FUN_800cccf4(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.charIndex_272);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccd34L)
  public static FlowControl FUN_800ccd34(final RunningScript<?> script) {
    int v1 = script.params_20[1].get();
    if(script.params_20[2].get() == 2 && v1 < 0) {
      v1 = 0;
    }

    //LAB_800ccd8c
    final BattleObject27c a1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1.setStat(script.params_20[2].get(), v1);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccda0L)
  public static FlowControl scriptSetStat(final RunningScript<?> script) {
    final BattleObject27c a1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1.setStat(Math.max(0, script.params_20[2].get()), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cce04L)
  public static FlowControl scriptGetStat(final RunningScript<?> script) {
    final BattleObject27c a1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(script.params_20[1].get() == 2) {
      script.params_20[2].set(a1.hp_08);
    } else {
      //LAB_800cce54
      script.params_20[2].set(a1.getStat(script.params_20[1].get()));
    }

    //LAB_800cce68
    return FlowControl.CONTINUE;
  }

  @Method(0x800cce70L)
  public static FlowControl FUN_800cce70(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[2].set(bobj.getStat(script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccec8L)
  public static FlowControl FUN_800ccec8(final RunningScript<?> script) {
    FUN_800f1a00(script.params_20[0].get() > 0 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ccef8L)
  public static FlowControl FUN_800ccef8(final RunningScript<?> script) {
    _800bc974.set(3);
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800ccf0cL)
  public static FlowControl FUN_800ccf0c(final RunningScript<?> script) {
    _800bc974.set(script.params_20[0].get());
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800ccf2cL)
  public static FlowControl FUN_800ccf2c(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleObject27c data = (BattleObject27c)state.innerStruct_00;

    int s0 = state.storage_44[7];
    if(script.params_20[1].get() != 0) {
      if((s0 & 0x40) == 0) {
        s0 = s0 | 0x40;

        if((s0 & 0x4) != 0) {
          final CombatantStruct1a8 enemyCombatant = data.combatant_144;
          goldGainedFromCombat_800bc920.add(enemyCombatant.gold_196);
          totalXpFromCombat_800bc95c.add(enemyCombatant.xp_194);

          if((s0 & 0x2000) == 0) {
            if(simpleRand() * 100 >> 16 < enemyCombatant.itemChance_198) {
              if(enemyCombatant.itemDrop_199 != 0xff) {
                itemsDroppedByEnemies_800bc928.get(itemsDroppedByEnemiesCount_800bc978.get()).set(enemyCombatant.itemDrop_199);
                itemsDroppedByEnemiesCount_800bc978.incr();
              }

              //LAB_800cd044
              s0 = s0 | 0x2000;
            }
          }
        }
      }
    } else {
      //LAB_800cd04c
      s0 = s0 & 0xffff_ffbf;
    }

    //LAB_800cd054
    state.storage_44[7] = s0;
    FUN_800c7304();
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd078L)
  public static FlowControl FUN_800cd078(final RunningScript<?> script) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];

    //LAB_800cd0d0
    if(script.params_20[1].get() != 0) {
      a1.storage_44[7] |= 0x40;
    } else {
      //LAB_800cd0c4
      a1.storage_44[7] &= 0xffff_ffbf;
    }

    FUN_800c7304();
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd0ecL)
  public static FlowControl FUN_800cd0ec(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[3].set(getHitMultiplier(
      bobj.charSlot_276,
      script.params_20[1].get(),
      script.params_20[2].get()
    ));

    return FlowControl.CONTINUE;
  }

  @Method(0x800cd160L)
  public static FlowControl levelUpAddition(final RunningScript<?> a0) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[a0.params_20[0].get()].innerStruct_00;

    if(a0.params_20[1].get() != 0) {
      final int charIndex = bobj.charIndex_272;
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

      final int additionIndex = charData.selectedAddition_19.get() - additionOffsets_8004f5ac.get(charIndex).get();
      if(charIndex == 2 || charIndex == 8 || additionIndex < 0) {
        //LAB_800cd200
        return FlowControl.CONTINUE;
      }

      //LAB_800cd208
      final int additionXp = Math.min(99, charData.additionXp_22.get(additionIndex).get() + 1);

      //LAB_800cd240
      //LAB_800cd288
      while(charData.additionLevels_1a.get(additionIndex).get() < 5 && additionXp >= additionNextLevelXp_800fa744.get(charData.additionLevels_1a.get(additionIndex).get()).get()) {
        charData.additionLevels_1a.get(additionIndex).incr();
      }

      //LAB_800cd2ac
      int nonMaxedAdditions = additionCounts_8004f5c0.get(charIndex).get();
      int firstNonMaxAdditionIndex = -1;

      // Find the first addition that isn't already maxed out
      //LAB_800cd2ec
      for(int additionIndex2 = 0; additionIndex2 < additionCounts_8004f5c0.get(charIndex).get(); additionIndex2++) {
        if(charData.additionLevels_1a.get(additionIndex2).get() == 5) {
          nonMaxedAdditions--;
        } else {
          //LAB_800cd308
          firstNonMaxAdditionIndex = additionIndex2;
        }

        //LAB_800cd30c
      }

      //LAB_800cd31c
      if(nonMaxedAdditions < 2 && (charData.partyFlags_04.get() & 0x40) == 0) {
        charData.partyFlags_04.or(0x40);

        if(firstNonMaxAdditionIndex >= 0) {
          charData.additionLevels_1a.get(firstNonMaxAdditionIndex).set(1);
        }

        //LAB_800cd36c
        _800bc910.offset(bobj.charSlot_276 * 0x4L).setu(0x1L);
      }

      //LAB_800cd390
      charData.additionXp_22.get(additionIndex).set(additionXp);
    }

    //LAB_800cd3ac
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd3b4L)
  public static FlowControl FUN_800cd3b4(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    if(script.params_20[2].get() != 0) {
      bobj.model_148.ui_f4 &= ~(0x1L << script.params_20[1].get());
    } else {
      //LAB_800cd420
      bobj.model_148.ui_f4 |= 0x1L << script.params_20[1].get();
    }

    //LAB_800cd460
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd468L)
  public static FlowControl FUN_800cd468(final RunningScript<?> script) {
    script.params_20[2].set(FUN_800cac38(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd4b0L)
  public static FlowControl FUN_800cd4b0(final RunningScript<?> script) {
    final BttlStruct08 v0 = FUN_800cad50(script.params_20[0].get());
    return v0._04 == 1 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @Method(0x800cd4f0L)
  public static FlowControl FUN_800cd4f0(final RunningScript<?> script) {
    FUN_800cad64(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd52cL)
  public static FlowControl scriptAddCombatant(final RunningScript<?> script) {
    script.params_20[1].set(addCombatant(script.params_20[0].get(), -1));
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd570L)
  public static FlowControl scriptDeallocateAndRemoveCombatant(final RunningScript<?> script) {
    deallocateCombatant(script.params_20[0].get());
    removeCombatant(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd5b4L)
  public static FlowControl FUN_800cd5b4(final RunningScript<?> script) {
    final ScriptState<BattleObject27c> state = SCRIPTS.allocateScriptState(new BattleObject27c());
    script.params_20[2].set(state.index);
    state.setTicker(Bttl_800c::bobjTicker);
    state.setDestructor(Bttl_800c::bobjDestructor);
    state.loadScriptFile(script.scriptState_04.scriptPtr_14, script.params_20[0].get());
    state.storage_44[7] |= 0x804;
    _8006e398.bobjIndices_e0c[_800c66d0.get()] = state;
    _8006e398.bobjIndices_e50[monsterCount_800c6768.get()] = state;

    final BattleObject27c bobj = state.innerStruct_00;
    bobj.magic_00 = BattleScriptDataBase.BOBJ;
    final CombatantStruct1a8 combatant = getCombatant(script.params_20[1].get());
    bobj.combatant_144 = combatant;
    bobj.combatantIndex_26c = script.params_20[1].get();
    bobj.charIndex_272 = combatant.charIndex_1a2;
    bobj._274 = _800c66d0.get();
    _800c66d0.incr();
    bobj.charSlot_276 = monsterCount_800c6768.get();
    monsterCount_800c6768.incr();
    bobj.model_148.coord2_14.coord.transfer.set(0, 0, 0);
    bobj.model_148.coord2Param_64.rotate.set((short)0, (short)0, (short)0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd740L)
  public static FlowControl FUN_800cd740(final RunningScript<?> script) {
    final BttlStruct08 v0 = FUN_800cad50(script.params_20[0].get());

    if(v0._04 == 1) {
      //LAB_800cd794
      return FlowControl.PAUSE_AND_REWIND;
    }

    FUN_800c94f8(script.params_20[0].get(), (short)script.params_20[1].get());

    //LAB_800cd798
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd7a8L)
  public static FlowControl FUN_800cd7a8(final RunningScript<?> script) {
    final long v0 = script.params_20[0].get();

    if(MEMORY.ref(1, v0).offset(0x4L).get() == 1) {
      //LAB_800cd7fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    FUN_800ca528(script.params_20[0].get(), (short)script.params_20[1].get());

    //LAB_800cd800
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd810L)
  public static FlowControl FUN_800cd810(final RunningScript<?> script) {
    final int s0 = script.params_20[2].get();

    if(s0 >= 0) {
      //LAB_800cd85c
      final BttlStruct08 v0 = FUN_800cad50(s0);

      if(v0._04 == 1) {
        return FlowControl.PAUSE_AND_REWIND;
      }

      FUN_800c9db8(script.params_20[0].get(), script.params_20[1].get(), s0);
    } else {
      FUN_800c9c7c(script.params_20[0].get(), script.params_20[1].get());
    }

    //LAB_800cd890
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd8a4L)
  public static FlowControl FUN_800cd8a4(final RunningScript<?> script) {
    final BttlStruct08 a1 = FUN_800cad50(script.params_20[1].get());

    if(a1._04 == 1) {
      //LAB_800cd8fc
      return FlowControl.PAUSE_AND_REWIND;
    }

    loadCombatantTim(script.params_20[0].get(), a1.ptr_00);

    //LAB_800cd900
    return FlowControl.PAUSE;
  }

  @Method(0x800cd910L)
  public static FlowControl FUN_800cd910(final RunningScript<?> script) {
    script.params_20[2].set(FUN_800cac38(script.params_20[0].get(), script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd958L)
  public static FlowControl scriptGetCombatantIndex(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.combatantIndex_26c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd998L)
  public static FlowControl FUN_800cd998(final RunningScript<?> script) {
    final BattleObject27c v1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(script.params_20[2].get() != 0) {
      script.params_20[1].set(v1.charSlot_276);
    } else {
      //LAB_800cd9e8
      script.params_20[1].set(v1._274);
    }

    //LAB_800cd9f4
    return FlowControl.CONTINUE;
  }

  @Method(0x800cd9fcL)
  public static FlowControl scriptGetBobjNobj(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(bobj.model_148.ObjTable_0c.nobj);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cda3cL)
  public static FlowControl scriptDeallocateCombatant(final RunningScript<?> script) {
    deallocateCombatant(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cda78L)
  public static FlowControl FUN_800cda78(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if(FUN_800c90b0(bobj.combatantIndex_26c) == 0) {
      return FlowControl.PAUSE_AND_REWIND;
    }

    //LAB_800cdacc
    bobj.combatant_144.charIndex_1a2 = -1;

    if(bobj._278 != 0) {
      deallocateModelIfMonster(bobj.model_148, bobj.combatantIndex_26c);
    }

    //LAB_800cdaf4
    bobj.animIndex_26e = 0;
    FUN_800c952c(bobj.model_148, bobj.combatantIndex_26c);

    //LAB_800cdb08
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdb18L)
  public static FlowControl FUN_800cdb18(final RunningScript<?> script) {
    setStageHasNoModel();
    FUN_800c8ed8();
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdb44L)
  public static FlowControl scriptLoadStage(final RunningScript<?> script) {
    loadStage(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdb74L)
  public static FlowControl FUN_800cdb74(final RunningScript<?> script) {
    _800c66b9.setu(0x1L);

    //LAB_800cdbb8
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      _8006e398.charBobjIndices_e40[i].loadScriptFile(null);
    }

    //LAB_800cdbe0
    script_800c66fc = null;

    //LAB_800cdc00
    return FlowControl.CONTINUE;
  }

  @Method(0x800cdc1cL)
  public static void FUN_800cdc1c(final ScriptState<BattleObject27c> s1, final int x, final int y, final int z, final int a4, final int a5, final int a6, final int a7, final int a8) {
    final int v0 = a4 - x << 8;
    final int v1 = a5 - y << 8;
    final int s0 = a6 - z << 8;
    final int s3 = a7 << 8;

    s1._cc = a8;
    s1._e8 = a4;
    s1._ec = a5;
    s1._f0 = a6;
    s1._d0 = v0;
    s1._d4 = v1;
    s1._d8 = s0;

    // Fix for retail /0 bug
    if(a8 > 0) {
      s1._dc = v0 / a8;
      s1._e4 = s0 / a8;
    } else {
      s1._dc = v0 >= 0 ? -1 : 1;
      s1._e4 = s0 >= 0 ? -1 : 1;
    }

    s1._e0 = FUN_80013404(s3, v1, a8);
    s1._f4 = s3;
  }

  @Method(0x800cdcecL)
  public static void FUN_800cdcec(final Model124 model, final int dobjIndex, final VECTOR largestVertRef, final VECTOR smallestVertRef, final EffectManagerData6c manager, final IntRef largestIndexRef, final IntRef smallestIndexRef) {
    short largest = 0x7fff;
    short smallest = -1;
    int largestIndex = 0;
    int smallestIndex = 0;
    final TmdObjTable v0 = model.dobj2ArrPtr_00[dobjIndex].tmd_08;

    //LAB_800cdd24
    for(int i = 0; i < v0.n_vert_04.get(); i++) {
      final SVECTOR vert = v0.vert_top_00.deref().get(i);
      final ShortRef component = vert.component(manager._10._24);
      final short val = component.get();

      if(val <= largest) {
        largest = component.get();
        largestIndex = i;
        largestVertRef.set(vert);
        //LAB_800cdd7c
      } else if(val >= smallest) {
        smallest = component.get();
        smallestIndex = i;
        smallestVertRef.set(vert);
      }

      //LAB_800cddbc
    }

    //LAB_800cddcc
    largestIndexRef.set(largestIndex);
    smallestIndexRef.set(smallestIndex);
  }

  @Method(0x800cdde4L)
  public static WeaponTrailEffectSegment2c getRootSegment(final WeaponTrailEffect3c a0) {
    WeaponTrailEffectSegment2c v1 = a0._38;

    //LAB_800cddfc
    while(!v1._24.isNull()) {
      v1 = v1._24.deref();
    }

    //LAB_800cde14
    return v1;
  }

  @Method(0x800cde1cL)
  public static WeaponTrailEffectSegment2c FUN_800cde1c(final WeaponTrailEffect3c a0) {
    WeaponTrailEffectSegment2c v1 = a0.segments_34.get(0);

    int i = 0;
    //LAB_800cde3c
    while(v1._03.get() != 0) {
      i++;
      v1 = a0.segments_34.get(i);
    }

    //LAB_800cde50
    if(i == 64) {
      v1 = getRootSegment(a0);
      v1._03.set(0);

      if(!v1._28.isNull()) {
        v1._28.deref()._24.clear();
      }
    }

    //LAB_800cde80
    //LAB_800cde84
    return v1;
  }

  @Method(0x800cde94L)
  public static void renderWeaponTrailEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final WeaponTrailEffect3c s2 = (WeaponTrailEffect3c)data.effect_44;

    if(s2._38 != null) {
      final SVECTOR sp0x18 = new SVECTOR().set(data._10.colour_1c).shl(8);
      final SVECTOR sp0x20 = new SVECTOR().set(sp0x18).div(s2._0e);
      WeaponTrailEffectSegment2c s0 = s2._38;

      final IntRef sp0x38 = new IntRef();
      final IntRef sp0x3c = new IntRef();
      FUN_800cf244(s0._04.get(0), sp0x38, sp0x3c);

      final IntRef sp0x40 = new IntRef();
      final IntRef sp0x44 = new IntRef();
      final int z = FUN_800cf244(s0._04.get(1), sp0x40, sp0x44) >> 2;

      //LAB_800cdf94
      s0 = s0._24.derefNullable();
      for(int i = 0; i < s2._0e && s0 != null; i++) {
        final IntRef sp0x28 = new IntRef();
        final IntRef sp0x2c = new IntRef();
        FUN_800cf244(s0._04.get(0), sp0x28, sp0x2c);
        final IntRef sp0x30 = new IntRef();
        final IntRef sp0x34 = new IntRef();
        FUN_800cf244(s0._04.get(1), sp0x30, sp0x34);

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .translucent(Translucency.B_PLUS_F)
          .pos(0, sp0x38.get(), sp0x3c.get())
          .pos(1, sp0x28.get(), sp0x2c.get())
          .pos(2, sp0x40.get(), sp0x44.get())
          .pos(3, sp0x30.get(), sp0x34.get())
          .monochrome(0, 0)
          .monochrome(1, 0)
          .rgb(2, sp0x18.getX() >>> 8, sp0x18.getY() >>> 8, sp0x18.getZ() >>> 8);

        sp0x18.sub(sp0x20);

        cmd.rgb(3, sp0x18.getX() >>> 8, sp0x18.getY() >>> 8, sp0x18.getZ() >>> 8);

        int a0 = z + data._10.z_22;
        if(a0 >= 0xa0) {
          if(a0 >= 0xffe) {
            a0 = z + 0xffe - z;
          }

          //LAB_800ce138
          GPU.queueCommand(a0 >> 2, cmd);
        }

        //LAB_800ce14c
        sp0x38.set(sp0x28.get());
        sp0x3c.set(sp0x2c.get());
        sp0x40.set(sp0x30.get());
        sp0x44.set(sp0x34.get());
        s0 = s0._24.derefNullable();
      }

      //LAB_800ce1a0
      //LAB_800ce1a4
    }

    //LAB_800ce230
  }

  @Method(0x800ce254L)
  public static void tickWeaponTrailEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long s5;
    long s6;

    final WeaponTrailEffect3c effect = (WeaponTrailEffect3c)data.effect_44;
    effect._00++;
    if(effect._00 == 0) {
      final IntRef largestVertexIndex = new IntRef();
      final IntRef smallestVertexIndex = new IntRef();
      FUN_800cdcec(effect.parentModel_30, effect.dobjIndex_08, effect.largestVertex_20, effect.smallestVertex_10, data, largestVertexIndex, smallestVertexIndex);
      effect.largestVertexIndex_0c = largestVertexIndex.get();
      effect.smallestVertexIndex_0a = smallestVertexIndex.get();
      return;
    }

    //LAB_800ce2c4
    WeaponTrailEffectSegment2c s0 = FUN_800cde1c(effect);

    if(effect._38 != null) {
      effect._38._28.set(s0);
    }

    //LAB_800ce2e4
    s0._00.set(0x6c);
    s0._01.set(0x63);
    s0._02.set(0x73);
    s0._03.set(0x1);
    s0._28.clear();
    s0._24.setNullable(effect._38);
    effect._38 = s0;

    //LAB_800ce320
    for(int i = 0; i < 2; i++) {
      final MATRIX sp0x20 = new MATRIX();
      GsGetLw(effect.parentModel_30.coord2ArrPtr_04[effect.dobjIndex_08], sp0x20);
      final VECTOR sp0x40 = ApplyMatrixLV(sp0x20, i == 0 ? effect.largestVertex_20 : effect.smallestVertex_10);
      sp0x40.add(sp0x20.transfer);
      s0._04.get(i).set(sp0x40);
    }

    //LAB_800ce3e0
    s0 = effect._38;
    while(s0 != null) {
      FUN_800ce880(s0._04.get(1), s0._04.get(0), 0x1000, 0x400);
      s0 = s0._24.derefNullable();
    }

    //LAB_800ce404
    //LAB_800ce40c
    for(s5 = 0; s5 < 2; s5++) {
      s0 = effect._38;
      s6 = 0;

      //LAB_800ce41c
      while(s0 != null) {
        if(!s0._28.isNull()) {
          if(!s0._24.isNull()) {
            WeaponTrailEffectSegment2c s1 = s0._24.deref();

            //LAB_800ce444
            final WeaponTrailEffectSegment2c[] sp0x50 = new WeaponTrailEffectSegment2c[2];
            for(int s2 = 0; s2 < 2; s2++) {
              final WeaponTrailEffectSegment2c v0 = FUN_800cde1c(effect);
              sp0x50[s2] = v0;
              v0._00.set(0x6c);
              v0._01.set(0x63);
              v0._02.set(0x73);
              v0._03.set(0x1);
              v0._04.get(0).set(s1._04.get(0)).sub(s0._04.get(0)).div(3).add(s0._04.get(0));
              v0._04.get(1).set(s1._04.get(1)).sub(s0._04.get(1)).div(3).add(s0._04.get(1));
              s1 = s0._28.deref();
            }

            sp0x50[0]._24.set(s0._24.deref());
            sp0x50[1]._24.set(sp0x50[0]);
            sp0x50[1]._28.set(s0._28.deref());
            sp0x50[0]._28.set(sp0x50[1]);
            s0._28.deref()._24.set(sp0x50[1]);
            s0._24.deref()._28.set(sp0x50[0]);
            s0._03.set(0);
            s0 = s0._24.derefNullable();
            s6++;
            if(s6 > s5 * 0x2L || s0 == null) {
              break;
            }
          }
        }

        //LAB_800ce630
        s0 = s0._24.derefNullable();
      }

      //LAB_800ce640
    }

    //LAB_800ce650
  }

  @Method(0x800ce678L)
  public static void deallocateWeaponTrailEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(((WeaponTrailEffect3c)data.effect_44).segments_34.getAddress());
  }

  @Method(0x800ce6a8L)
  public static FlowControl allocateWeaponTrailEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0,
      Bttl_800c::tickWeaponTrailEffect,
      Bttl_800c::renderWeaponTrailEffect,
      Bttl_800c::deallocateWeaponTrailEffect,
      value -> new WeaponTrailEffect3c()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final WeaponTrailEffect3c effect = (WeaponTrailEffect3c)manager.effect_44;
    effect.segments_34 = MEMORY.ref(4, mallocTail(0x2c * 65), ArrayRef.of(WeaponTrailEffectSegment2c.class, 65, 0x2c, WeaponTrailEffectSegment2c::new));

    //LAB_800ce75c
    for(int i = 0; i < 65; i++) {
      final WeaponTrailEffectSegment2c segment = effect.segments_34.get(i);
      segment._00.set(0x6c);
      segment._01.set(0x63);
      segment._02.set(0x73);
      segment._03.set(0);
      segment._24.clear();
      segment._28.clear();
    }

    effect._38 = null;
    effect._00 = -1;
    effect._04 = script.params_20[1].get();
    effect.dobjIndex_08 = script.params_20[2].get();
    effect._0e = 20;
    manager._10.colour_1c.set((short)0xff, (short)0x80, (short)0x60);

    final BattleScriptDataBase parent = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;
    if(BattleScriptDataBase.EM__.equals(parent.magic_00)) {
      effect.parentModel_30 = ((BttlScriptData6cSub13c)((EffectManagerData6c)parent).effect_44).model_10;
    } else {
      //LAB_800ce7f8
      effect.parentModel_30 = ((BattleObject27c)parent).model_148;
    }

    //LAB_800ce804
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800ce83cL)
  public static void FUN_800ce83c(final int scriptIndex, int a1) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)manager.effect_44;

    a1 = a1 * 4;
    if((a1 & 0xff) > 0x40) {
      a1 = 0x40;
    }

    trail._0e = a1;
  }

  @Method(0x800ce880L)
  public static void FUN_800ce880(final VECTOR a0, final VECTOR a1, final int a2, final int a3) {
    final VECTOR sp0x00 = new VECTOR();
    final VECTOR sp0x10 = new VECTOR();
    sp0x00.set(a0).sub(a1);

    sp0x10.set(
      sp0x00.getX() * a2 >> 12,
      sp0x00.getY() * a2 >> 12,
      sp0x00.getZ() * a2 >> 12
    );

    a0.set(a1).add(sp0x10);

    sp0x10.set(
      sp0x00.getX() * a3 >> 12,
      sp0x00.getY() * a3 >> 12,
      sp0x00.getZ() * a3 >> 12
    );

    a1.add(sp0x10);
  }

  @Method(0x800ce9b0L)
  public static FlowControl FUN_800ce9b0(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final WeaponTrailEffect3c trail = (WeaponTrailEffect3c)manager.effect_44;
    FUN_800ce880(trail.smallestVertex_10, trail.largestVertex_20, script.params_20[2].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cea1cL)
  public static VECTOR scriptGetScriptedObjectPos(final int scriptIndex, final VECTOR posOut) {
    final BattleScriptDataBase data = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    final VECTOR pos;
    if(BattleScriptDataBase.EM__.equals(data.magic_00)) {
      //LAB_800cea78
      pos = ((EffectManagerData6c)data)._10.trans_04;
    } else {
      pos = ((BattleObject27c)data).model_148.coord2_14.coord.transfer;
    }

    posOut.set(pos);

    //LAB_800cea8c
    return pos;
  }

  @Method(0x800cea9cL)
  public static void FUN_800cea9c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub0e effect = (BttlScriptData6cSub0e)manager.effect_44;

    if(effect.scale_0c.get() != 0) {
      effect.svec_00.add(effect.svec_06);
      effect.scale_0c.decr();
    }

    //LAB_800ceb20
  }

  @Method(0x800ceb28L)
  public static void FUN_800ceb28(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub0e a0 = (BttlScriptData6cSub0e)manager.effect_44;

    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(manager._10.flags_00 >>> 28 & 0b11))
      .rgb(a0.svec_00.getX(), a0.svec_00.getY(), a0.svec_00.getZ())
      .pos(-160, -120, 320, 280)
    );
  }

  /** Used at the end of Rose transform */
  @Method(0x800cec8cL)
  public static FlowControl FUN_800cec8c(final RunningScript<? extends BattleScriptDataBase> script) {
    final short sp18 = (short)(script.params_20[1].get() << 8);
    final short sp1a = (short)(script.params_20[2].get() << 8);
    final short sp1c = (short)(script.params_20[3].get() << 8);
    final short sp20 = (short)(script.params_20[4].get() << 8);
    final short sp22 = (short)(script.params_20[5].get() << 8);
    final short sp24 = (short)(script.params_20[6].get() << 8);
    final short s1 = (short)script.params_20[7].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0xe,
      Bttl_800c::FUN_800cea9c,
      Bttl_800c::FUN_800ceb28,
      null,
      BttlScriptData6cSub0e::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager._10.flags_00 = 0x5000_0000;

    final BttlScriptData6cSub0e a2 = (BttlScriptData6cSub0e)manager.effect_44;
    a2.svec_00.set(sp18, sp1a, sp1c);
    a2.svec_06.set((short)((sp20 - sp18) / s1), (short)((sp22 - sp1a) / s1), (short)((sp24 - sp1c) / s1));
    a2.scale_0c.set(s1);

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cee50L)
  public static FlowControl FUN_800cee50(final RunningScript<?> script) {
    final int a2 = script.params_20[1].get();
    script.params_20[0].set((int)(seed_800fa754.advance().get() % (script.params_20[2].get() - a2 + 1) + a2));
    return FlowControl.CONTINUE;
  }

  @Method(0x800ceeccL)
  public static FlowControl FUN_800ceecc(final RunningScript<?> script) {
    FUN_800ce83c(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  /** Used in Flameshot */
  @Method(0x800cef00L)
  public static FlowControl FUN_800cef00(final RunningScript<?> script) {
    GPU.queueCommand(30, new GpuCommandQuad()
      .translucent(Translucency.of(script.params_20[3].get() + 1))
      .rgb(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get())
      .pos(-160, -120, 320, 280)
    );

    return FlowControl.CONTINUE;
  }

  @Method(0x800cf0b4L)
  public static FlowControl FUN_800cf0b4(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  /** @return Z */
  @Method(0x800cf244L)
  public static int FUN_800cf244(final VECTOR a0, final IntRef a1, final IntRef a2) {
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(0), 0);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(2), 1);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(4), 2);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(6), 3);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(8), 4);

    final SVECTOR a0s = new SVECTOR().set(a0);
    CPU.MTC2(a0s.getXY(), 0); // VXY0
    CPU.MTC2(a0s.getZ(),  1); // VZ0
    CPU.COP2(0x48_6012L); // MVMVA - multiply V0 by rotation matrix and add nothing

    final VECTOR sp0x10 = new VECTOR().set((int)CPU.MFC2(25), (int)CPU.MFC2(26), (int)CPU.MFC2(27));
    sp0x10.add(worldToScreenMatrix_800c3548.transfer);
    a1.set(MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.getX(), sp0x10.getZ()));
    a2.set(MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.getY(), sp0x10.getZ()));
    return sp0x10.getZ();
  }

  @Method(0x800cf37cL)
  public static void FUN_800cf37c(final EffectManagerData6c a0, @Nullable final SVECTOR a1, final VECTOR a2, final VECTOR a3) {
    final VECTOR sp0x10 = new VECTOR();
    final SVECTOR sp0x20 = new SVECTOR();
    final MATRIX sp0x28 = new MATRIX();

    sp0x20.set(a0._10.rot_10);

    if(a1 != null) {
      //LAB_800cf3c4
      sp0x20.add(a1);
    }

    //LAB_800cf400
    FUN_80040980(sp0x20, sp0x28);
    TransMatrix(sp0x28, sp0x10);
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);

    sp0x20.set(a2);
    CPU.MTC2(sp0x20.getXY(), 0);
    CPU.MTC2(sp0x20.getZ(),  1);
    CPU.COP2(0x480012L);
    sp0x10.setX((int)CPU.MFC2(25));
    sp0x10.setY((int)CPU.MFC2(26));
    sp0x10.setZ((int)CPU.MFC2(27));
    a3.set(sp0x10);
  }

  @Method(0x800cf4f4L)
  public static void FUN_800cf4f4(final EffectManagerData6c a0, @Nullable final SVECTOR a1, final VECTOR a2, final VECTOR a3) {
    final VECTOR sp0x10 = new VECTOR();
    final SVECTOR sp0x20 = new SVECTOR();
    final MATRIX sp0x28 = new MATRIX();

    sp0x20.set(a0._10.rot_10);

    if(a1 != null) {
      //LAB_800cf53c
      sp0x20.add(a1);
    }

    //LAB_800cf578
    sp0x10.set(a0._10.trans_04);
    FUN_80040980(sp0x20, sp0x28);
    TransMatrix(sp0x28, sp0x10);
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);
    sp0x20.set(a2);
    CPU.MTC2(sp0x20.getXY(), 0);
    CPU.MTC2(sp0x20.getZ(),  1);
    CPU.COP2(0x480012L);
    sp0x10.setX((int)CPU.MFC2(25));
    sp0x10.setY((int)CPU.MFC2(26));
    sp0x10.setZ((int)CPU.MFC2(27));
    a3.set(sp0x10);
  }

  @Method(0x800cf684L)
  public static void FUN_800cf684(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final VECTOR a3) {
    final SVECTOR sp0x20 = new SVECTOR().set(a0);
    final VECTOR sp0x10 = new VECTOR().set(a1);
    final MATRIX sp0x28 = new MATRIX();
    FUN_80040980(sp0x20, sp0x28);
    TransMatrix(sp0x28, sp0x10);
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);
    sp0x20.set(a2);
    CPU.MTC2(sp0x20.getXY(), 0);
    CPU.MTC2(sp0x20.getZ(),  1);
    CPU.COP2(0x480012L);
    sp0x10.set((int)CPU.MFC2(25), (int)CPU.MFC2(26), (int)CPU.MFC2(27));
    a3.set(sp0x10);
  }

  /** @return Z */
  @Method(0x800cf7d4L)
  public static int FUN_800cf7d4(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x30 = new SVECTOR().set(a1);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(0), 0);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(2), 1);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(4), 2);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(6), 3);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(8), 4);
    CPU.MTC2(sp0x30.getXY(), 0);
    CPU.MTC2(sp0x30.getZ(),  1);
    final SVECTOR sp0x28 = new SVECTOR().set(a0);
    CPU.COP2(0x486012L);
    final VECTOR sp0x10 = new VECTOR();
    sp0x10.setX((int)CPU.MFC2(25));
    sp0x10.setY((int)CPU.MFC2(26));
    sp0x10.setZ((int)CPU.MFC2(27));
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(0), 0);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(2), 1);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(4), 2);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(6), 3);
    CPU.CTC2(worldToScreenMatrix_800c3548.getPacked(8), 4);
    CPU.CTC2(worldToScreenMatrix_800c3548.transfer.getX(), 5);
    CPU.CTC2(worldToScreenMatrix_800c3548.transfer.getY(), 6);
    CPU.CTC2(worldToScreenMatrix_800c3548.transfer.getZ(), 7);

    final MATRIX sp0x38 = new MATRIX();
    sp0x38.setPacked(0, CPU.CFC2(0));
    sp0x38.setPacked(2, CPU.CFC2(1));
    sp0x38.setPacked(4, CPU.CFC2(2));
    sp0x38.setPacked(6, CPU.CFC2(3));
    sp0x38.setPacked(8, CPU.CFC2(4));
    sp0x38.transfer.setX((int)CPU.CFC2(5));
    sp0x38.transfer.setY((int)CPU.CFC2(6));
    sp0x38.transfer.setZ((int)CPU.CFC2(7));

    final MATRIX sp0x58 = new MATRIX();
    FUN_80040980(sp0x28, sp0x58);
    CPU.CTC2(sp0x38.getPacked(0), 0); //
    CPU.CTC2(sp0x38.getPacked(2), 1); //
    CPU.CTC2(sp0x38.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(sp0x38.getPacked(6), 3); //
    CPU.CTC2(sp0x38.getPacked(8), 4); //
    CPU.MTC2(sp0x58.get(0),  9); // IR1
    CPU.MTC2(sp0x58.get(3), 10); // IR2
    CPU.MTC2(sp0x58.get(6), 11); // IR3
    CPU.COP2(0x49e012L);
    sp0x38.set(0, (short)CPU.MFC2( 9));
    sp0x38.set(3, (short)CPU.MFC2(10));
    sp0x38.set(6, (short)CPU.MFC2(11));
    CPU.MTC2(sp0x58.get(1),  9);
    CPU.MTC2(sp0x58.get(4), 10);
    CPU.MTC2(sp0x58.get(7), 11);
    CPU.COP2(0x49e012L);
    sp0x38.set(1, (short)CPU.MFC2( 9));
    sp0x38.set(4, (short)CPU.MFC2(10));
    sp0x38.set(7, (short)CPU.MFC2(11));
    CPU.MTC2(sp0x58.get(2),  9);
    CPU.MTC2(sp0x58.get(5), 10);
    CPU.MTC2(sp0x58.get(8), 11);
    CPU.COP2(0x49e012L);
    sp0x38.set(2, (short)CPU.MFC2( 9));
    sp0x38.set(5, (short)CPU.MFC2(10));
    sp0x38.set(8, (short)CPU.MFC2(11));
    sp0x38.transfer.add(sp0x10);
    CPU.CTC2(sp0x38.getPacked(0), 0);
    CPU.CTC2(sp0x38.getPacked(2), 1);
    CPU.CTC2(sp0x38.getPacked(4), 2);
    CPU.CTC2(sp0x38.getPacked(6), 3);
    CPU.CTC2(sp0x38.getPacked(8), 4);
    CPU.CTC2(sp0x38.transfer.getX(), 5);
    CPU.CTC2(sp0x38.transfer.getY(), 6);
    CPU.CTC2(sp0x38.transfer.getZ(), 7);
    CPU.MTC2((a2.getY() & 0xffff) << 16 | a2.getX() & 0xffff, 0);
    CPU.MTC2(a2.getZ(), 1);
    CPU.COP2(0x180001L);

    final DVECTOR sp0x20 = new DVECTOR();
    sp0x20.setXY(CPU.MFC2(14)); // SXY1
    outX.set(sp0x20.getX());
    outY.set(sp0x20.getY());
    return (int)CPU.MFC2(19); // SZ3
  }

  /** @return Z */
  @Method(0x800cfb14L)
  public static int FUN_800cfb14(final EffectManagerData6c a0, final VECTOR a1, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0._10.rot_10);
    final VECTOR sp0x20 = new VECTOR().set(a0._10.trans_04);
    return FUN_800cf7d4(sp0x18, sp0x20, a1, outX, outY);
  }

  @Method(0x800cfb94L)
  public static int FUN_800cfb94(final EffectManagerData6c a0, final SVECTOR a1, final VECTOR a2, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0._10.rot_10).add(a1);
    final VECTOR sp0x20 = new VECTOR().set(a0._10.trans_04);
    return FUN_800cf7d4(sp0x18, sp0x20, a2, outX, outY);
  }

  /** Returns Z */
  @Method(0x800cfc20L)
  public static int FUN_800cfc20(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0);
    final VECTOR sp0x20 = new VECTOR().set(a1);
    return FUN_800cf7d4(sp0x18, sp0x20, a2, outX, outY);
  }

  @Method(0x800cfcccL)
  public static FlowControl FUN_800cfccc(final RunningScript<?> script) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleScriptDataBase a0 = (BattleScriptDataBase)a1.innerStruct_00;

    final Model124 model;
    if(BattleScriptDataBase.EM__.equals(a0.magic_00)) {
      model = ((BttlScriptData6cSub13c)((EffectManagerData6c)a0).effect_44).model_10;
    } else {
      //LAB_800cfd34
      model = ((BattleObject27c)a0).model_148;
    }

    //LAB_800cfd40
    final MATRIX sp0x10 = new MATRIX();
    GsGetLw(model.coord2ArrPtr_04[script.params_20[1].get()], sp0x10);
    final VECTOR sp0x40 = ApplyMatrixLV(sp0x10, new VECTOR());
    sp0x40.add(sp0x10.transfer);
    script.params_20[2].set(sp0x40.getX());
    script.params_20[3].set(sp0x40.getY());
    script.params_20[4].set(sp0x40.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800cfdf8L)
  public static FlowControl scriptGetBobjDimension(final RunningScript<?> script) {
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final int componentIndex = script.params_20[1].get();
    final GsCOORDINATE2[] coords = bobj.model_148.coord2ArrPtr_04;

    //LAB_800cfe54
    int largest = 0x8000_0001;
    int smallest = 0x7fff_ffff;
    for(int animIndex = bobj.model_148.animCount_98 - 1; animIndex >= 0; animIndex--) {
      final int component = coords[animIndex].coord.transfer.get(componentIndex);

      if(largest < component) {
        largest = component;
        //LAB_800cfe84
      } else if(smallest > component) {
        smallest = component;
      }

      //LAB_800cfe90
    }

    //LAB_800cfe9c
    script.params_20[2].set(largest - smallest);
    return FlowControl.CONTINUE;
  }

  @Method(0x800cfec8L)
  public static FlowControl FUN_800cfec8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800cfed0L)
  public static void setMtSeed(final long seed) {
    seed_800fa754.set(seed ^ 0x75b_d924L);
  }

  @Method(0x800cff24L)
  public static FlowControl scriptSetMtSeed(final RunningScript<?> script) {
    setMtSeed(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Holy crap this method was complicated... the way the stack is set up, all params after a3 are part of the sp0x90 array. Count is the number of parameters.
   * It's basically a variadic method so I'm changing the signature to that.
   * <p>
   * This method allows you to call a script function from the main game engine. Variadic params get passed in as the param array.
   */
  @Method(0x800cff54L)
  public static void callScriptFunction(final long func, final int... params) {
    final RunningScript<Void> script = new RunningScript<>(null);

    //LAB_800cff90
    for(int i = 0; i < params.length; i++) {
      script.params_20[i] = new IntParam(params[i]);
    }

    //LAB_800cffbc
    MEMORY.ref(4, func).call(script);
  }

  @Method(0x800cffd8L)
  public static void FUN_800cffd8(final int scriptIndex, final VECTOR a1, final int animIndex) {
    final MATRIX sp0x10 = new MATRIX();
    GsGetLw(((BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00).model_148.coord2ArrPtr_04[animIndex], sp0x10);
    a1.set(ApplyMatrixLV(sp0x10, new VECTOR()));
    a1.add(sp0x10.transfer);
  }
}
