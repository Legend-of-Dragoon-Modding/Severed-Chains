package legend.game.submap;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.Config;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.memory.types.IntRef;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.combat.types.Ptr;
import legend.game.fmv.Fmv;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptStorageParam;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.tmd.UvAdjustmentMetrics14;
import legend.game.types.ActiveStatsa0;
import legend.game.types.AnimatedSprite08;
import legend.game.types.AnmFile;
import legend.game.types.AnmSpriteGroup;
import legend.game.types.AnmSpriteMetrics14;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import legend.game.types.LodString;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.NewRootStruct;
import legend.game.types.ShopStruct40;
import legend.game.types.SmallerStruct;
import legend.game.types.Textbox4c;
import legend.game.types.TextboxChar08;
import legend.game.types.TextboxText84;
import legend.game.types.TextboxType;
import legend.game.types.TmdAnimationFile;
import legend.game.types.TmdSubExtension;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.MathHelper.flEq;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.getSubmapMusicChange;
import static legend.game.Scus94491BpeSegment.loadDir;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadMenuSounds;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadSubmapSounds;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.reinitSound;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.startCurrentMusicSequence;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment.stopAndResetSoundsAndSequences;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218f0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.animateModelTextures;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.calculateAppropriateTextboxBounds;
import static legend.game.Scus94491BpeSegment_8002.clearTextbox;
import static legend.game.Scus94491BpeSegment_8002.clearTextboxText;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.scriptDeallocateAllTextboxes;
import static legend.game.Scus94491BpeSegment_8002.srand;
import static legend.game.Scus94491BpeSegment_8002.textboxFits;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetSmapRefView2L;
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeIn;
import static legend.game.Scus94491BpeSegment_8005._80050274;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.renderBorder_80052b68;
import static legend.game.Scus94491BpeSegment_8005.submapCutForSave_800cb450;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapEnvState_80052c44;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8005.textboxMode_80052b88;
import static legend.game.Scus94491BpeSegment_8005.textboxTextType_80052ba8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bda08;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.matrix_800bed30;
import static legend.game.Scus94491BpeSegment_800b.musicLoaded_800bd782;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800b.rview2_800bd7e8;
import static legend.game.Scus94491BpeSegment_800b.screenOffsetX_800bed50;
import static legend.game.Scus94491BpeSegment_800b.screenOffsetY_800bed54;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.sobjPositions_800bd818;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.submapFullyLoaded_800bd7b4;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.textboxText_800bdf38;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.transitioningFromCombatToSubmap_800bd7b8;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public class SMap extends EngineState {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SMap.class);

  private int fmvIndex_800bf0dc;

  private EngineStateEnum afterFmvLoadingStage_800bf0ec = EngineStateEnum.PRELOAD_00;

  private final GsF_LIGHT GsF_LIGHT_0_800c66d8 = new GsF_LIGHT();
  private final GsF_LIGHT GsF_LIGHT_1_800c66e8 = new GsF_LIGHT();
  private final GsF_LIGHT GsF_LIGHT_2_800c66f8 = new GsF_LIGHT();
  private int chapterTitleState_800c6708;
  private int chapterTitleAnimationTicksRemaining_800c670a;
  private int chapterTitleDropShadowOffsetX_800c670c;
  private int chapterTitleDropShadowOffsetY_800c670e;
  private List<FileData> chapterTitleCardMrg_800c6710;
  private int chapterTitleNumberOffsetX_800c6714;
  private int chapterTitleNumberOffsetY_800c6718;
  private int chapterTitleNameOffsetX_800c671c;
  private int chapterTitleNameOffsetY_800c6720;
  /** Inverted condition from retail */
  private boolean chapterTitleIsTranslucent_800c6724;
  private int chapterTitleBrightness_800c6728;

  public int sobjCount_800c6730;

  /**
   * Lower 4 bits are chapter title num (starting at 1), used for displaying chapter title cards
   *
   * 0x80 bit indicates that the origin XY of the title card have been set and the animation is ready to start rendering
   */
  private int chapterTitleNum_800c6738;

  private int chapterTitleAnimationPauseTicksRemaining_800c673c;
  public ScriptState<Void> submapControllerState_800c6740;

  private final Model124 playerModel_800c6748 = new Model124("Player");
  private boolean submapModelLoaded_800c686c;
  private boolean chapterTitleAnimationComplete_800c686e;
  private int _800c6870;

  private boolean submapAssetsLoaded_800c6874;
  private List<FileData> submapAssetsMrg_800c6878;
  private int chapterTitleOriginX_800c687c;
  private int chapterTitleOriginY_800c687e;
  public final ScriptState<SubmapObject210>[] sobjs_800c6880 = new ScriptState[20];
  private boolean submapScriptsLoaded_800c68d0;

  private List<FileData> submapScriptsMrg_800c68d8;
  private SubmapAssets submapAssets;

  private boolean chapterTitleCardLoaded_800c68e0;

  private SubmapMediaState mediaLoadingStage_800c68e4;
  private final SubmapCaches80 caches_800c68e8 = new SubmapCaches80();
  private int submapType_800c6968;
  /** Index 31 tracks the current tick since indicator last enabled. Have not yet seen other elements set to anything but -1 */
  public final int[] indicatorTickCountArray_800c6970 = new int[32];

  private TriangleIndicator140 triangleIndicator_800c69fc;

  /** TODO array, flags for submap objects - 0x80 means the model is the same as the previous one */
  private final IntList submapObjectFlags_800c6a50 = new IntArrayList();

  private final Vector3f cameraPos_800c6aa0 = new Vector3f();

  private final Vector3f prevPlayerPos_800c6ab0 = new Vector3f();
  private float encounterMultiplier_800c6abc;
  private final MV matrix_800c6ac0 = new MV();
  private int _800c6ae0;
  private int _800c6ae4;
  public int encounterAccumulator_800c6ae8;
  private UnknownStruct _800c6aec;

  private int _800caaf4;
  private int _800caaf8;

  private NewRootStruct newrootPtr_800cab04;

  private boolean backgroundLoaded_800cab10;

  private int _800cab20;
  private MediumStruct _800cab24;
  private int mapTransitionTicks_800cab28;

  public SubmapState smapLoadingStage_800cb430 = SubmapState.INIT_0;

  private boolean _800cb448;

  private final int[] collisionAndTransitions_800cb460 = new int[64];

  private int submapOffsetX_800cb560;
  private int submapOffsetY_800cb564;
  private int screenOffsetX_800cb568;
  private int screenOffsetY_800cb56c;
  private int _800cb570;
  private int _800cb574;
  private int _800cb578;
  private int envBackgroundTextureCount_800cb57c;
  private int envForegroundTextureCount_800cb580;
  private int envTextureCount_800cb584;

  private final EnvironmentForegroundTextureMetrics[] envForegroundMetrics_800cb590 = new EnvironmentForegroundTextureMetrics[32];
  private final EnvironmentRenderingMetrics24[] envRenderMetrics_800cb710 = new EnvironmentRenderingMetrics24[32];
  {
    Arrays.setAll(this.envForegroundMetrics_800cb590, i -> new EnvironmentForegroundTextureMetrics());
    Arrays.setAll(this.envRenderMetrics_800cb710, i -> new EnvironmentRenderingMetrics24());
  }

  private final Vector3f[] _800cbb90 = new Vector3f[32];
  {
    Arrays.setAll(this._800cbb90, i -> new Vector3f());
  }
  private final float[] _800cbc90 = new float[32];

  private final GsRVIEW2 rview2_800cbd10 = new GsRVIEW2();
  private int _800cbd30;
  private int _800cbd34;
  private UnknownStruct2 _800cbd38;
  private UnknownStruct2 _800cbd3c;
  private final MV screenToWorldMatrix_800cbd40 = new MV();
  private int minSobj_800cbd60;
  private int maxSobj_800cbd64;
  /** A copy of the WS matrix */
  private final MV worldToScreenMatrix_800cbd68 = new MV();

  private int _800cbd94;
  private final Vector3f _800cbd98 = new Vector3f();

  private final GsCOORDINATE2 GsCOORDINATE2_800cbda8 = new GsCOORDINATE2();
  private final ModelPart10 GsDOBJ2_800cbdf8 = new ModelPart10();
  private final SomethingStruct SomethingStruct_800cbe08 = new SomethingStruct();
  private UnknownStruct2 _800cbe34;
  private UnknownStruct2 _800cbe38;

  private final int[] collisionPrimitiveIndices_800cbe48 = new int[8];

  private int _800d1a78;
  private int _800d1a7c;
  private int _800d1a80;
  private float _800d1a84;
  private SomethingStruct SomethingStructPtr_800d1a88;
  private UnknownStruct2 _800d1a8c;
  private final MediumStruct _800d1a90 = new MediumStruct();

  private final Matrix4f submapCutMatrix_800d4bb0 = new Matrix4f();

  private Structb0 _800d4bd0;
  private FileData _800d4bd4;
  private SnowEffect snow_800d4bd8;
  private boolean submapCutModelAndAnimLoaded_800d4bdc;
  private boolean submapTextureAndMatrixLoaded_800d4be0;
  private boolean theEndTimLoaded_800d4be4;
  private CContainer submapCutModel;
  private TmdAnimationFile submapCutAnim;
  private Tim submapCutTexture;
  private MV submapCutMatrix;
  private Tim theEndTim_800d4bf0;

  private final Model124 submapModel_800d4bf8 = new Model124("Submap");

  private final SnowStruct18 snowStuff_800d4d20 = new SnowStruct18();

  private final Model124 dustModel_800d4d40 = new Model124("Dust");

  private final DustRenderData54 dust_800d4e68 = new DustRenderData54();

  private final Struct20 _800d4ec0 = new Struct20();
  private final Struct24 _800d4ee0 = new Struct24();

  private final Struct34 _800d4f18 = new Struct34();

  private final SMapStruct3c struct3c_800d4f50 = new SMapStruct3c();

  private final Struct34_2 _800d4f90 = new Struct34_2();

  private final Struct14 _800d4fd0 = new Struct14();

  private int _800d4fe8;

  private final TriangleIndicator44[] _800d4ff0 = new TriangleIndicator44[21];
  {
    Arrays.setAll(this._800d4ff0, i -> new TriangleIndicator44());
  }

  private final AnimatedSprite08 playerIndicatorAnimation_800d5588 = new AnimatedSprite08();
  private final AnimatedSprite08 doorIndicatorAnimation_800d5590 = new AnimatedSprite08();
  private final SavePointRenderData44[] savePoint_800d5598 = new SavePointRenderData44[2];
  {
    Arrays.setAll(this.savePoint_800d5598, i -> new SavePointRenderData44());
  }
  private boolean hasSavePoint_800d5620;
  private final Vector3f savePointPos_800d5622 = new Vector3f();

  private final SavePointRenderData44[] savePoint_800d5630 = new SavePointRenderData44[32];
  {
    Arrays.setAll(this.savePoint_800d5630, i -> new SavePointRenderData44());
  }
  private final Model124 savePointModel_800d5eb0 = new Model124("Save point");

  private final SMapStruct3c struct3c_800d5fd8 = new SMapStruct3c();

  private final Struct34 struct34_800d6018 = new Struct34();

  private final int[] texPages_800d6050 = new int[12];
  private final int[] cluts_800d6068 = new int[12];

  /** Maps submap cuts to their submap */
  private final int[] cutToSubmap_800d610c = {
    1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6,
    6, 6, 6, 6, 6, 6, 6, 1, 4, 3, 8, 7, 7, 7, 7, 7,
    7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
    8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10,
    10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11,
    10, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
    12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14,
    14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 15,
    15, 15, 15, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 17, 17,
    17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 17, 18, 18, 18, 18, 18,
    18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19, 19, 19, 19,
    19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 20, 20, 20,
    20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 19, 19, 20, 20, 20, 22,
    22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23,
    23, 23, 23, 22, 20, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25,
    25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 27,
    27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 29, 29, 29,
    29, 29, 29, 29, 29, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30,
    30, 27, 27, 31, 31, 31, 31, 32, 32, 32, 32, 32, 32, 32, 32, 32,
    32, 32, 32, 33, 33, 33, 33, 33, 33, 33, 33, 34, 34, 34, 34, 34,
    34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 28, 28, 28, 35,
    35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 36, 36, 37, 37, 37,
    37, 37, 37, 37, 37, 34, 37, 37, 37, 38, 38, 38, 38, 38, 38, 38,
    38, 38, 38, 38, 38, 38, 39, 39, 39, 39, 39, 39, 39, 40, 40, 40,
    40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 41, 41, 41,
    41, 42, 42, 42, 42, 42, 42, 42, 42, 43, 43, 43, 43, 43, 43, 43,
    43, 43, 43, 43, 43, 43, 43, 43, 43, 44, 44, 44, 44, 44, 44, 44,
    44, 45, 45, 45, 58, 58, 58, 58, 58, 58, 58, 58, 58, 46, 46, 46,
    46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46,
    46, 46, 46, 46, 46, 46, 46, 46, 47, 58, 58, 58, 58, 58, 48, 48,
    49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49,
    50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51,
    51, 51, 51, 51, 51, 51, 51, 51, 51, 48, 48, 48, 48, 48, 48, 48,
    48, 56, 52, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54,
    54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55,
    55, 55, 54, 53, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56,
    56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 8,
    2, 2, 10, 10, 9, 53, 28, 8, 10, 10, 14, 14, 14, 14, 14, 14,
    4, 4, 4, 4, 4, 4, 14, 14, 49, 49, 58, 58, 58, 58, 58, 58,
    13, 10, 18, 18, 18, 53, 20, 20, 20, 20, 20, 20, 20, 20, 32, 32,
    58, 58, 10, 57, 57, 57, 28, 58, 58, 58, 58, 58, 58, 58, 58, 58,
    58, 58, 58, 58, 36, 36, 3, 3, 3, 5, 20, 56, 53, 53, 20, 57,
    57, 51, 51, 51, 51, 19, 32, 54, 54, 54, 54, 51, 13, 50, 50, 50,
    30, 51, 51, 36, 36, 3, 10, 56, 56, 56, 56, 56, 56, 56, 56, 56,
    56, 56, 56, 56, 5, 5, 3, 3, 19, 48, 48, 48, 48, 48, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48,
    48, 48, 48, 48, 48, 48, 48, 0,
  };

  private final Rect4i _800d6b48 = new Rect4i(576, 368, 16, 1);

  private final Vector3f[] _800d6b7c = {
    new Vector3f(-10.0f, 0.0f, -22.0f),
    new Vector3f( 10.0f, 0.0f, -22.0f),
    new Vector3f(-10.0f, 0.0f,  22.0f),
    new Vector3f( 10.0f, 0.0f,  22.0f),
    new Vector3f(-12.0f, 0.0f, - 8.0f),
    new Vector3f(- 2.0f, 0.0f, - 8.0f),
    new Vector3f(-12.0f, 0.0f,   8.0f),
    new Vector3f(- 2.0f, 0.0f,   8.0f),
    new Vector3f(  2.0f, 0.0f, - 8.0f),
    new Vector3f( 12.0f, 0.0f, - 8.0f),
    new Vector3f(  2.0f, 0.0f,   8.0f),
    new Vector3f( 12.0f, 0.0f,   8.0f),
  };
  private final int[] _800d6bdc = {96, 112, 64, 0};
  private final int[] smokeTextureWidths_800d6bec = {15, 15, 31, 23};
  private final int[] smokeTextureHeights_800d6bfc = {31, 31, 31, 23};
  private final int[] _800d6c0c = {120, 0, 0, 0};

  private final Vector3f _800d6c18 = new Vector3f(-8.0f, 0.0f, 0.0f);
  private final Vector3f _800d6c20 = new Vector3f( 8.0f, 0.0f, 0.0f);
  private final Vector3f savePointV0_800d6c28 = new Vector3f(-24.0f, -32.0f,  24.0f);
  private final Vector3f savePointV1_800d6c30 = new Vector3f( 24.0f, -32.0f,  24.0f);
  private final Vector3f savePointV2_800d6c38 = new Vector3f(-24.0f, -32.0f, -24.0f);
  private final Vector3f savePointV3_800d6c40 = new Vector3f( 24.0f, -32.0f, -24.0f);
  private final Vector3f _800d6c48 = new Vector3f(0.0f, -24.0f, 0.0f);
  private final Vector3f _800d6c50 = new Vector3f(0.0f,  24.0f, 0.0f);
  private final int[] _800d6c58 = {0x200, 0x800, 0x400, 0x800, 0x100, 0x61c, 0x960, 0xe10};
  private final int[] _800d6c78 = {6, 4, 5, 3, 5, 7, 5, 6};
  private final int[] savePointFloatiesRotations_800d6c88 = {-14, -55, 22, 16, -28, -14, 24, 27};
  private final int dartArrowU_800d6ca8 = 0;
  private final int dartArrowV_800d6cac = 96;
  private final int doorArrowU_800d6cb0 = 128;
  private final int doorArrowV_800d6cb4 = 0;
  private final Vector3f bottom_800d6cb8 = new Vector3f();
  private final Vector3f top_800d6cc0 = new Vector3f(0.0f, 40.0f, 0.0f);
  private final int[] _800d6cc8 = {206, 206, 207, 208};
  private final int[] _800d6cd8 = {992, 992, 976};
  private final int[] _800d6ce4 = {208, 207, 8};
  private final Translucency[] miscTextureTransModes_800d6cf0 = {Translucency.B_PLUS_F, Translucency.B_PLUS_F, Translucency.B_PLUS_F, Translucency.B_PLUS_F, Translucency.B_PLUS_QUARTER_F, Translucency.B_PLUS_F, Translucency.B_PLUS_F, Translucency.B_MINUS_F, Translucency.B_MINUS_F, Translucency.B_PLUS_F, Translucency.B_PLUS_F};

  private AnmFile savepointAnm1;
  private AnmFile savepointAnm2;
  private CContainer savepointTmd;
  private TmdAnimationFile savepointAnimation;
  private CContainer dustTmd;
  private TmdAnimationFile dustAnimation;

  private final String bigArrow_800d7620 = "big_arrow.tim";
  private final String smallArrow_800d7c60 = "small_arrow.tim";
  private final String savepoint_800d7ee0 = "savepoint.tim";
  private final String tim_800d8520 = "800d8520.tim";
  private final String tim_800d85e0 = "800d85e0.tim";
  private final String savepointBigCircle_800d8720 = "savepoint_big_circle.tim";
  private final String dust_800d8960 = "dust.tim";
  private final String leftFoot_800d8ba0 = "left_foot.tim";
  private final String rightFoot_800d8ce0 = "right_foot.tim";
  private final String smoke1_800d8e20 = "smoke_1.tim";
  private final String smoke2_800d9060 = "smoke_2.tim";

  public static final ShopStruct40[] shops_800f4930 = {
    new ShopStruct40(0, 1, 28, 47, 63, 77, 103, 106, 107, 108, 112, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 222, 205, 206, 198, 208, 223, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 21, 83, 89, 104, 105, 106, 111, 156, 149, 150, 151, 157, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 229, 222, 205, 206, 199, 209, 223, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 2, 29, 78, 94, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 229, 222, 202, 214, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 14, 48, 97, 103, 104, 105, 106, 107, 108, 109, 111, 112, 156, 255, 255, 255),
    new ShopStruct40(1, 203, 249, 229, 222, 205, 206, 201, 216, 223, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 64, 84, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 229, 222, 205, 206, 220, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 22, 41, 58, 107, 109, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 249, 229, 222, 205, 206, 201, 210, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 3, 30, 35, 49, 65, 79, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 249, 229, 222, 205, 223, 195, 209, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 15, 80, 95, 110, 115, 118, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 229, 222, 204, 207, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 9, 23, 42, 54, 59, 81, 85, 98, 105, 108, 110, 74, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 249, 229, 222, 204, 205, 206, 194, 207, 223, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 36, 113, 114, 119, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 229, 249, 222, 205, 206, 202, 216, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 24, 37, 55, 60, 117, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 249, 229, 222, 205, 206, 223, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 5, 33, 43, 99, 113, 114, 118, 119, 120, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 249, 229, 222, 223, 212, 215, 217, 218, 200, 225, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 115, 116, 117, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 249, 229, 222, 205, 206, 212, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 6, 25, 38, 44, 51, 52, 56, 61, 67, 68, 69, 113, 114, 115, 116, 117),
    new ShopStruct40(1, 231, 249, 229, 222, 204, 205, 206, 212, 217, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(3, 203, 222, 194, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 4, 10, 16, 31, 50, 66, 116, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 249, 229, 222, 206, 223, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 142, 143, 146, 148, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 206, 195, 223, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 153, 154, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 203, 229, 222, 205, 206, 210, 197, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 11, 17, 51, 52, 68, 69, 106, 108, 110, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(1, 231, 229, 249, 222, 204, 205, 206, 223, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(3, 203, 229, 222, 205, 218, 207, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 203, 229, 222, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
    new ShopStruct40(0, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255),
  };

  private final UvAdjustmentMetrics14[] uvAdjustments_800f5930 = {
    new UvAdjustmentMetrics14(       0x0, 0xffffffff,      0x0, 0xffffffff,    0x0),
	  new UvAdjustmentMetrics14(0x5c260000,  0x3c0ffff, 0x190000, 0xffe0ffff,   0x80),
	  new UvAdjustmentMetrics14(0x5c270000,  0x3c0ffff, 0x190000, 0xffe0ffff,   0xc0),
	  new UvAdjustmentMetrics14(0x7c240000,  0x3c0ffff, 0x190000, 0xffe0ffff, 0x8000),
	  new UvAdjustmentMetrics14(0x7c250000,  0x3c0ffff, 0x190000, 0xffe0ffff, 0x8040),
	  new UvAdjustmentMetrics14(0x7c260000,  0x3c0ffff, 0x190000, 0xffe0ffff, 0x8080),
	  new UvAdjustmentMetrics14(0x7c270000,  0x3c0ffff, 0x190000, 0xffe0ffff, 0x80c0),
	  new UvAdjustmentMetrics14(0x5c2a0000,  0x3c0ffff, 0x1a0000, 0xffe0ffff,   0x80),
	  new UvAdjustmentMetrics14(0x5c2b0000,  0x3c0ffff, 0x1a0000, 0xffe0ffff,   0xc0),
	  new UvAdjustmentMetrics14(0x7c280000,  0x3c0ffff, 0x1a0000, 0xffe0ffff, 0x8000),
	  new UvAdjustmentMetrics14(0x7c290000,  0x3c0ffff, 0x1a0000, 0xffe0ffff, 0x8040),
	  new UvAdjustmentMetrics14(0x7c2a0000,  0x3c0ffff, 0x1a0000, 0xffe0ffff, 0x8080),
	  new UvAdjustmentMetrics14(0x7c2b0000,  0x3c0ffff, 0x1a0000, 0xffe0ffff, 0x80c0),
	  new UvAdjustmentMetrics14(0x5c2e0000,  0x3c0ffff, 0x1b0000, 0xffe0ffff,   0x80),
	  new UvAdjustmentMetrics14(0x5c2f0000,  0x3c0ffff, 0x1b0000, 0xffe0ffff,   0xc0),
	  new UvAdjustmentMetrics14(0x5c2c0000,  0x3c0ffff, 0x1b0000, 0xffe0ffff,    0x0),
	  new UvAdjustmentMetrics14(0x5c2d0000,  0x3c0ffff, 0x1b0000, 0xffe0ffff,   0x40),
	  new UvAdjustmentMetrics14(0x5c3f0000,  0x3c0ffff, 0x1f0000, 0xffe0ffff,   0xc0),
	  new UvAdjustmentMetrics14(0x5c240000, 0x83c3ffff, 0x190000, 0xffe0ffff,    0x0),
	  new UvAdjustmentMetrics14(0x5c280000, 0x83c3ffff, 0x1a0000, 0xffe0ffff,    0x0)
  };

  /**
   * 65 - {@link SMap#handleAndRenderSubmapModel()}
   *
   * All other indices are {@link SMap#noSubmapModel()}
   */
  private final Runnable[] submapModelRenderers_800f5ad4 = new Runnable[0x80];
  {
    Arrays.setAll(this.submapModelRenderers_800f5ad4, i -> this::noSubmapModel);
    this.submapModelRenderers_800f5ad4[65] = this::handleAndRenderSubmapModel;
  }
  /**
   * These are indices into the above table
   *
   * <ul>
   *   <li>0 - submap has no model</li>
   *   <li>65 - submap has model</li>
   * </ul>
   */
  private final int[] submapTypes_800f5cd4 = {
    0, 0, 0, 0, 0, 65, 65, 0, 0, 65, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65,
    65, 65, 65, 65, 0, 0, 65, 0, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65, 0, 65, 65, 0, 65, 65, 65, 65, 0, 0, 65, 0, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65,
    0, 0, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 0, 65, 65, 0, 65, 65, 65, 65, 0, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 0, 65,
    65, 65, 0, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 65, 0, 0, 0, 65, 0, 0, 65, 0, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 0, 0, 0, 65, 65, 0, 65, 65,
    65, 65, 65, 0, 65, 0, 0, 0, 0, 0, 65, 65, 0, 65, 65, 0, 0, 65, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 65, 65, 65, 0, 0, 0, 0, 65, 65, 0, 65, 65, 65, 0, 65, 0, 65, 0, 65, 0, 0, 0,
    0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 0, 0, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 65, 0, 0, 0, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0,
    0, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 0, 0, 0, 0, 65, 0, 65, 0, 0, 0, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65,
    65, 0, 65, 65, 0, 0, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 65, 0, 65, 65, 65, 65, 0,
    65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 0, 65, 65, 0, 65, 65, 0,
    0, 65, 0, 0, 0, 0, 0, 0, 65, 0, 0, 65, 0, 65, 0, 0, 65, 65, 65, 0, 65, 65, 0, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65,
    0, 65, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
    65, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
    0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 0, 0, 0, 65, 65, 65, 65, 65, 0, 0, 0, 0, 0, 65, 0, 65, 0, 65, 65, 65, 65, 0, 0, 65, 65,
    0, 0, 0, 0, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 0, 65, 0, 65, 65, 0, 0, 65, 65, 65, 65, 65, 65, 65, 65,
    65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 65, 65, 65, 65, 0, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 0, 0, 0, 0, 0, 0, 0, 65, 65, 65, 65, 0, 0, 0, 65, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 65, 65, 65, 65, 0, 0, 0, 0, 65, 65, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
  };

  /** Related to indicator being disabled for cutscenes/conversations */
  private boolean indicatorDisabledForCutscene_800f64ac;

  private final AlertIndicator14 alertIndicatorMetrics_800f64b0 = new AlertIndicator14(-12, 12, -12, 12, 24, 48, 64, 88);

  /** Indexed by submap cut */
  public static final SubmapEncounterData_04[] encounterData_800f64c4 = {
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 10, 1), new SubmapEncounterData_04(1, 7, 1), new SubmapEncounterData_04(2, 10, 1), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(10, 10, 6), new SubmapEncounterData_04(11, 10, 6), new SubmapEncounterData_04(12, 10, 6), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(14, 10, 6), new SubmapEncounterData_04(15, 14, 6), new SubmapEncounterData_04(16, 14, 6), new SubmapEncounterData_04(20, 10, 7), new SubmapEncounterData_04(21, 10, 7), new SubmapEncounterData_04(22, 10, 7),
    new SubmapEncounterData_04(23, 10, 7), new SubmapEncounterData_04(24, 10, 7), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(26, 10, 7), new SubmapEncounterData_04(27, 4, 7), new SubmapEncounterData_04(28, 10, 7), new SubmapEncounterData_04(29, 9, 7), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(30, 9, 10), new SubmapEncounterData_04(31, 14, 10), new SubmapEncounterData_04(32, 14, 10), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(34, 14, 10), new SubmapEncounterData_04(35, 14, 10),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(40, 9, 11), new SubmapEncounterData_04(41, 14, 11), new SubmapEncounterData_04(42, 14, 11), new SubmapEncounterData_04(43, 19, 11), new SubmapEncounterData_04(44, 19, 11), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(47, 9, 11), new SubmapEncounterData_04(48, 9, 11), new SubmapEncounterData_04(49, 9, 11), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(50, 14, 13), new SubmapEncounterData_04(51, 10, 13), new SubmapEncounterData_04(52, 7, 13), new SubmapEncounterData_04(53, 10, 13), new SubmapEncounterData_04(54, 10, 92), new SubmapEncounterData_04(55, 7, 92), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(60, 10, 15), new SubmapEncounterData_04(61, 10, 15), new SubmapEncounterData_04(62, 7, 15), new SubmapEncounterData_04(63, 7, 15), new SubmapEncounterData_04(64, 7, 15), new SubmapEncounterData_04(0, 0, 15), new SubmapEncounterData_04(66, 14, 15),
    new SubmapEncounterData_04(67, 17, 15), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(80, 8, 23), new SubmapEncounterData_04(81, 8, 23), new SubmapEncounterData_04(82, 5, 23), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(91, 8, 26), new SubmapEncounterData_04(92, 8, 26), new SubmapEncounterData_04(93, 8, 26), new SubmapEncounterData_04(94, 8, 26),
    new SubmapEncounterData_04(95, 8, 26), new SubmapEncounterData_04(96, 8, 26), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(100, 10, 28), new SubmapEncounterData_04(101, 17, 2), new SubmapEncounterData_04(102, 17, 28), new SubmapEncounterData_04(103, 10, 28), new SubmapEncounterData_04(104, 10, 29), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(120, 21, 33), new SubmapEncounterData_04(121, 10, 33), new SubmapEncounterData_04(122, 8, 33),
    new SubmapEncounterData_04(123, 8, 33), new SubmapEncounterData_04(124, 14, 33), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(130, 10, 36), new SubmapEncounterData_04(131, 10, 36), new SubmapEncounterData_04(132, 10, 36), new SubmapEncounterData_04(133, 10, 36), new SubmapEncounterData_04(134, 10, 36), new SubmapEncounterData_04(135, 14, 36), new SubmapEncounterData_04(136, 10, 36), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(140, 7, 37), new SubmapEncounterData_04(141, 7, 37), new SubmapEncounterData_04(142, 7, 37), new SubmapEncounterData_04(143, 7, 37), new SubmapEncounterData_04(144, 7, 37), new SubmapEncounterData_04(145, 7, 37), new SubmapEncounterData_04(146, 7, 37),
    new SubmapEncounterData_04(147, 7, 37), new SubmapEncounterData_04(148, 7, 37), new SubmapEncounterData_04(149, 7, 37), new SubmapEncounterData_04(150, 7, 37), new SubmapEncounterData_04(151, 7, 37), new SubmapEncounterData_04(152, 7, 37), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(106, 10, 39), new SubmapEncounterData_04(107, 10, 39), new SubmapEncounterData_04(108, 10, 39),
    new SubmapEncounterData_04(109, 10, 39), new SubmapEncounterData_04(110, 10, 39), new SubmapEncounterData_04(111, 10, 39), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(112, 10, 39), new SubmapEncounterData_04(113, 10, 39), new SubmapEncounterData_04(114, 10, 39), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(115, 10, 39), new SubmapEncounterData_04(116, 10, 39), new SubmapEncounterData_04(117, 10, 39), new SubmapEncounterData_04(118, 10, 39), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(160, 10, 42), new SubmapEncounterData_04(161, 10, 42), new SubmapEncounterData_04(162, 10, 42), new SubmapEncounterData_04(163, 7, 42), new SubmapEncounterData_04(164, 7, 42), new SubmapEncounterData_04(165, 10, 42), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(166, 10, 42), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 45),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(173, 14, 44), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(174, 14, 44), new SubmapEncounterData_04(0, 0, 44), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(180, 14, 46), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(182, 14, 46), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(184, 14, 46), new SubmapEncounterData_04(185, 14, 46), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(186, 14, 47), new SubmapEncounterData_04(187, 14, 47), new SubmapEncounterData_04(0, 0, 47), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(190, 14, 49), new SubmapEncounterData_04(191, 14, 49), new SubmapEncounterData_04(192, 10, 49),
    new SubmapEncounterData_04(193, 14, 49), new SubmapEncounterData_04(194, 14, 49), new SubmapEncounterData_04(195, 10, 49), new SubmapEncounterData_04(196, 10, 49), new SubmapEncounterData_04(197, 10, 49), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(198, 7, 49), new SubmapEncounterData_04(199, 7, 50), new SubmapEncounterData_04(200, 7, 50), new SubmapEncounterData_04(201, 7, 50),
    new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(203, 7, 50), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(231, 10, 53), new SubmapEncounterData_04(232, 10, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(234, 10, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 53), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(244, 21, 54), new SubmapEncounterData_04(245, 21, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(247, 14, 54),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(249, 14, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(251, 14, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(253, 10, 54), new SubmapEncounterData_04(254, 21, 54), new SubmapEncounterData_04(255, 21, 54), new SubmapEncounterData_04(256, 21, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(211, 17, 55), new SubmapEncounterData_04(212, 17, 55), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(214, 21, 55), new SubmapEncounterData_04(215, 21, 55), new SubmapEncounterData_04(0, 0, 2),
    new SubmapEncounterData_04(217, 21, 55), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(219, 21, 55), new SubmapEncounterData_04(220, 21, 55), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(222, 21, 55), new SubmapEncounterData_04(223, 21, 55), new SubmapEncounterData_04(260, 17, 56), new SubmapEncounterData_04(261, 17, 56), new SubmapEncounterData_04(262, 17, 56), new SubmapEncounterData_04(263, 14, 56), new SubmapEncounterData_04(264, 14, 56), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(265, 17, 56), new SubmapEncounterData_04(266, 17, 56), new SubmapEncounterData_04(267, 14, 56),
    new SubmapEncounterData_04(268, 17, 56), new SubmapEncounterData_04(0, 0, 56), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(269, 10, 58), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(270, 10, 58), new SubmapEncounterData_04(271, 14, 58), new SubmapEncounterData_04(272, 14, 66), new SubmapEncounterData_04(273, 14, 66), new SubmapEncounterData_04(274, 14, 80), new SubmapEncounterData_04(275, 14, 79),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(276, 10, 59), new SubmapEncounterData_04(277, 10, 59), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(278, 14, 81), new SubmapEncounterData_04(279, 14, 81), new SubmapEncounterData_04(280, 14, 81), new SubmapEncounterData_04(281, 8, 83), new SubmapEncounterData_04(282, 14, 83), new SubmapEncounterData_04(283, 8, 83), new SubmapEncounterData_04(284, 14, 84), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(285, 10, 61), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(3, 14, 1), new SubmapEncounterData_04(4, 10, 1), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(286, 10, 58), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(257, 21, 54), new SubmapEncounterData_04(258, 21, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(225, 21, 55), new SubmapEncounterData_04(226, 21, 55), new SubmapEncounterData_04(0, 0, 69), new SubmapEncounterData_04(259, 21, 54), new SubmapEncounterData_04(0, 0, 14), new SubmapEncounterData_04(241, 14, 53), new SubmapEncounterData_04(242, 10, 67), new SubmapEncounterData_04(0, 0, 53),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 54), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(287, 14, 61), new SubmapEncounterData_04(288, 14, 61), new SubmapEncounterData_04(289, 14, 62), new SubmapEncounterData_04(290, 14, 63), new SubmapEncounterData_04(291, 14, 63), new SubmapEncounterData_04(292, 10, 63), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2),
    new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(0, 0, 2), new SubmapEncounterData_04(293, 10, 81), new SubmapEncounterData_04(294, 14, 59), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
    new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0), new SubmapEncounterData_04(0, 0, 0),
  };

  private final int[][] sceneEncounterIds_800f74c4 = {
    {7, 5, 2, 1}, {7, 8, 9, 2}, {9, 8, 7, 2}, {8, 6, 4, 0}, {4, 5, 6, 3}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {14, 15, 13, 12}, {16, 14, 18, 11}, {17, 18, 19, 10}, {0, 0, 0, 0}, {10, 13, 12, 11}, {15, 17, 19, 13}, {19, 18, 17, 10}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {24, 25, 26, 20}, {26, 27, 28, 21}, {25, 28, 29, 22}, {27, 29, 24, 23}, {25, 26, 27, 24}, {0, 0, 0, 0}, {26, 25, 24, 20}, {27, 26, 25, 21}, {28, 27, 26, 22}, {29, 28, 27, 23}, {34, 35, 36, 30}, {35, 36, 37, 31}, {36, 37, 34, 32}, {0, 0, 0, 0}, {38, 37, 39, 33}, {39, 38, 37, 30}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {44, 45, 46, 40}, {45, 46, 47, 41}, {46, 47, 48, 42}, {47, 48, 49, 43}, {48, 49, 44, 40}, {0, 0, 0, 0}, {0, 0, 0, 0}, {47, 46, 45, 41}, {48, 47, 46, 42}, {49, 48, 47, 43}, {54, 55, 56, 50}, {55, 56, 57, 51}, {56, 57, 58, 52}, {57, 58, 59, 53}, {56, 55, 54, 52}, {59, 58, 57, 53}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {65, 66, 60, 61}, {60, 66, 67, 62}, {66, 67, 68, 63}, {60, 65, 66, 64}, {67, 66, 65, 61}, {0, 0, 0, 0}, {68, 67, 69, 62}, {69, 68, 67, 63}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {85, 86, 87, 82}, {87, 86, 89, 84}, {89, 88, 82, 81}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {92, 95, 96, 90}, {95, 96, 97, 91}, {96, 97, 92, 93}, {97, 96, 95, 92}, {98, 97, 99, 92}, {99, 98, 97, 92}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {104, 105, 106, 100}, {105, 106, 107, 101}, {106, 107, 108, 102}, {107, 108, 109, 103}, {108, 109, 104, 102}, {0, 0, 0, 0}, {154, 155, 156, 150}, {155, 156, 157, 151}, {156, 157, 158, 152}, {157, 158, 159, 153}, {158, 159, 154, 150}, {159, 154, 155, 151}, {154, 155, 156, 152}, {155, 156, 157, 153}, {156, 157, 158, 150}, {157, 158, 159, 151}, {158, 159, 154, 152}, {159, 154, 155, 153}, {154, 155, 156, 150}, {0, 0, 0, 0},
    {125, 126, 127, 120}, {126, 127, 128, 121}, {128, 129, 125, 122}, {129, 125, 126, 123}, {129, 128, 127, 124}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {132, 135, 136, 130}, {135, 136, 137, 131}, {136, 137, 138, 133}, {137, 138, 139, 134}, {138, 139, 132, 130}, {139, 132, 135, 131}, {139, 138, 137, 133}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {145, 146, 147, 140}, {146, 147, 148, 141}, {147, 148, 149, 142}, {148, 149, 145, 143}, {149, 145, 146, 144}, {145, 146, 147, 140}, {146, 147, 148, 141}, {147, 148, 149, 142}, {148, 149, 145, 143}, {149, 145, 146, 144}, {145, 146, 147, 140}, {146, 147, 148, 141}, {149, 148, 147, 142}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {161, 165, 166, 160}, {165, 166, 167, 162}, {166, 167, 168, 163}, {167, 168, 169, 164}, {168, 169, 161, 160}, {169, 161, 165, 162}, {161, 165, 166, 163}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {174, 175, 176, 179}, {177, 178, 179, 176}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {185, 186, 187, 180}, {0, 0, 0, 0}, {186, 187, 188, 181}, {0, 0, 0, 0}, {187, 188, 189, 182}, {188, 189, 185, 183}, {189, 185, 186, 184}, {189, 188, 187, 184}, {0, 0, 0, 0}, {0, 0, 0, 0}, {195, 196, 197, 190}, {196, 197, 198, 191}, {197, 198, 199, 192}, {198, 199, 195, 193}, {199, 195, 196, 194}, {195, 196, 197, 190}, {196, 197, 198, 191}, {197, 198, 199, 192}, {198, 199, 195, 193}, {199, 195, 196, 194},
    {195, 196, 197, 190}, {196, 197, 198, 191}, {0, 0, 0, 0}, {199, 198, 197, 192}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {232, 235, 236, 230}, {235, 236, 237, 231}, {0, 0, 0, 0}, {236, 237, 238, 233}, {237, 238, 239, 234}, {0, 0, 0, 0}, {238, 239, 232, 230}, {0, 0, 0, 0}, {232, 235, 236, 231},
    {235, 236, 237, 232}, {0, 0, 0, 0}, {236, 237, 238, 233}, {237, 238, 239, 234}, {0, 0, 0, 0}, {238, 239, 232, 230}, {239, 232, 235, 231}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {245, 246, 247, 240}, {246, 247, 248, 241}, {0, 0, 0, 0}, {247, 248, 249, 242}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
    {0, 0, 0, 0}, {248, 249, 245, 243}, {249, 245, 246, 244}, {0, 0, 0, 0}, {255, 256, 257, 250}, {256, 257, 258, 251}, {0, 0, 0, 0}, {257, 258, 259, 252}, {0, 0, 0, 0}, {258, 259, 255, 253}, {0, 0, 0, 0}, {259, 255, 256, 254}, {255, 256, 257, 250}, {256, 257, 258, 251}, {257, 258, 259, 252}, {258, 259, 255, 253}, {259, 255, 256, 254}, {255, 256, 257, 250}, {256, 257, 258, 251}, {257, 258, 259, 252},
    {265, 266, 267, 260}, {266, 267, 268, 261}, {267, 268, 269, 262}, {268, 269, 265, 263}, {269, 265, 266, 264}, {265, 266, 267, 260}, {266, 267, 268, 261}, {267, 268, 269, 262}, {268, 269, 265, 263}, {290, 293, 294, 291}, {293, 294, 290, 291}, {294, 290, 293, 291}, {300, 301, 302, 303}, {301, 302, 303, 304}, {302, 303, 304, 301}, {303, 304, 300, 301}, {290, 293, 294, 292}, {293, 294, 290, 292}, {305, 306, 307, 308}, {306, 307, 308, 309},
    {307, 308, 309, 305}, {310, 311, 312, 313}, {311, 312, 313, 314}, {312, 313, 314, 310}, {313, 314, 310, 311}, {315, 316, 317, 318}, {294, 290, 293, 291}, {316, 317, 318, 319}, {317, 318, 319, 315}, {318, 319, 315, 316}, {295, 296, 297, 298}, {296, 297, 299, 298}, {297, 299, 295, 298}, {308, 309, 305, 306}, {290, 293, 294, 292}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
  };

  private int _800f7e24;
  private Ptr<UnknownStruct> _800f7e28 = new Ptr<>(() -> this._800c6aec, val -> this._800c6aec = val);
  private final ChapterStruct08[] _800f7e2c = {
    new ChapterStruct08(0, 0),
    new ChapterStruct08(709, 0),
    new ChapterStruct08(710, 0),
    new ChapterStruct08(745, 58),
  };
  private boolean _800f7e4c;
  private int scriptSetOffsetMode_800f7e50;
  /**
   * <ul>
   *   <li>0x1 - disable encounters</li>
   * </ul>
   */
  private int submapFlags_800f7e54;
  /** A hard-coded list of submap cuts, related to submap bounds for camera control */
  private final int[] _800f7e58 = {140, 142, 155, 193, 197, 253, 310, 434, 665, 747, 748, 749, 750, 751, 752, 753, 754, 755, 756, 757, 758, 760, 761, 762, 765, 766, 767, 768, 769, 770, 771, 772, 773, 774, 775, 776, 777, 778, 780, 781, 782, 783, 784, 785, 786};

  private boolean _800f7f0c;

  private boolean _800f7f14;

  private final float[] _800f7f6c = new float[4];
  private final Struct14_2[] _800f7f74 = {
    new Struct14_2(7, 13), new Struct14_2(624, 11), new Struct14_2(7, 16), new Struct14_2(11, 30), new Struct14_2(9, 9), new Struct14_2(696, 13), new Struct14_2(695, 4), new Struct14_2(13, 17),
    new Struct14_2(38, 28), new Struct14_2(39, 25), new Struct14_2(44, 6), new Struct14_2(45, 1), new Struct14_2(54, 6), new Struct14_2(66, 41), new Struct14_2(95, 1), new Struct14_2(96, 1),
    new Struct14_2(106, 10), new Struct14_2(106, 16), new Struct14_2(106, 20), new Struct14_2(111, 11), new Struct14_2(114, 0), new Struct14_2(122, 8), new Struct14_2(130, 17), new Struct14_2(133, 57),
    new Struct14_2(132, 22), new Struct14_2(140, 2), new Struct14_2(153, 39), new Struct14_2(171, 1), new Struct14_2(201, 1), new Struct14_2(201, 32), new Struct14_2(744, 2), new Struct14_2(224, 35),
    new Struct14_2(231, 1), new Struct14_2(233, 13), new Struct14_2(232, 11), new Struct14_2(239, 26), new Struct14_2(242, 3), new Struct14_2(251, 10), new Struct14_2(258, 10), new Struct14_2(256, 9),
    new Struct14_2(261, 1), new Struct14_2(264, 28), new Struct14_2(297, 0), new Struct14_2(297, 3), new Struct14_2(301, 19), new Struct14_2(302, 19), new Struct14_2(301, 1), new Struct14_2(302, 1),
    new Struct14_2(309, 32), new Struct14_2(311, 18), new Struct14_2(328, 54), new Struct14_2(330, 25), new Struct14_2(330, 19), new Struct14_2(339, 26), new Struct14_2(344, 17), new Struct14_2(344, 18),
    new Struct14_2(341, 14), new Struct14_2(0, 0), new Struct14_2(342, 38), new Struct14_2(347, 31), new Struct14_2(349, 1), new Struct14_2(379, 0), new Struct14_2(0, 0), new Struct14_2(413, 1),
    new Struct14_2(433, 3), new Struct14_2(434, 1), new Struct14_2(457, 0), new Struct14_2(459, 1), new Struct14_2(477, 0), new Struct14_2(787, 1), new Struct14_2(513, 1), new Struct14_2(526, 36),
    new Struct14_2(528, 14), new Struct14_2(528, 13), new Struct14_2(539, 0), new Struct14_2(540, 19), new Struct14_2(572, 23), new Struct14_2(528, 15), new Struct14_2(563, 25), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(15, 8), new Struct14_2(346, 1), new Struct14_2(529, 41), new Struct14_2(38, 7), new Struct14_2(994, 0), new Struct14_2(996, 0), new Struct14_2(993, 0),
    new Struct14_2(992, 0), new Struct14_2(992, 0), new Struct14_2(990, 0), new Struct14_2(991, 0), new Struct14_2(285, 0), new Struct14_2(279, 31), new Struct14_2(527, 35), new Struct14_2(528, 15),
    new Struct14_2(327, 54), new Struct14_2(97, 1), new Struct14_2(12, 30), new Struct14_2(0, 0), new Struct14_2(999, 0), new Struct14_2(999, 1), new Struct14_2(999, 2), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
    new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0), new Struct14_2(0, 0),
  };

  /** Seems to be missing one element at the end, there are 792 cuts */
  private final int[] smapFileIndices_800f982c = {
    0, 0, 0, 0, 0, 6674, 6676, 0, 0, 6678, 0, 0, 0, 6680, 6682, 6684, 6686, 6688, 6690, 6692, 6694, 6696, 6698, 6700, 6702, 6704, 6706, 6708, 6710, 6712, 6714, 6716, 6718, 6720, 6722, 6724, 6726, 6684, 6728, 0, 0, 0, 6730, 6732, 6734, 6736, 6738, 6740, 6742, 6744,
    6746, 6748, 6750, 6752, 0, 0, 6682, 0, 6754, 0, 0, 0, 0, 0, 0, 0, 6754, 6756, 0, 6760, 6762, 0, 6764, 6766, 6768, 6770, 0, 0, 6772, 0, 6774, 0, 0, 6776, 6778, 6780, 6782, 6784, 6786, 6788, 6790, 0, 0, 0, 0, 0, 0, 0, 6792, 6794,
    0, 0, 6796, 0, 6798, 6800, 6802, 6804, 6806, 6808, 6810, 6812, 0, 0, 6814, 6816, 6818, 6820, 6822, 6824, 6826, 6828, 6830, 6832, 0, 0, 0, 0, 0, 0, 6834, 6836, 0, 6838, 6840, 6842, 6844, 0, 0, 0, 0, 0, 6846, 6848, 6850, 6852, 6854, 6856, 0, 6858,
    6860, 6862, 0, 6864, 6866, 6868, 6870, 6872, 0, 6874, 6876, 6878, 6880, 6872, 6868, 6866, 6870, 0, 0, 0, 0, 6882, 6884, 6886, 0, 0, 0, 6888, 0, 0, 6890, 0, 6892, 0, 0, 0, 0, 6894, 6896, 6898, 6900, 6902, 0, 0, 0, 6904, 6906, 0, 6908, 6910,
    6912, 6914, 6916, 0, 6918, 0, 0, 0, 0, 0, 6920, 6922, 0, 6924, 6926, 0, 0, 6928, 6930, 0, 0, 0, 0, 6932, 6934, 0, 0, 0, 6936, 6938, 6940, 0, 0, 0, 0, 6942, 6940, 0, 6938, 6944, 6946, 0, 6948, 0, 6950, 0, 6952, 0, 0, 0,
    0, 6954, 6956, 6958, 6960, 6962, 6964, 6966, 6968, 6944, 0, 0, 6970, 0, 0, 6972, 6974, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6976, 6978, 0, 0, 0, 6980, 6982, 6984, 0, 6986, 6988, 6990, 6992, 6994, 6996, 6998, 7000, 7002, 7004, 7006, 0, 0,
    0, 7008, 7010, 7012, 7014, 7016, 0, 0, 7018, 7020, 7022, 0, 0, 0, 0, 7024, 0, 7026, 0, 0, 0, 7028, 7030, 7032, 7034, 7036, 0, 7038, 7038, 7040, 7042, 7044, 7046, 7048, 7050, 7052, 7054, 7056, 7058, 0, 0, 0, 0, 7060, 7062, 7064, 7062, 7066, 7068, 7070,
    7072, 0, 7074, 7076, 0, 0, 7078, 7080, 7082, 7084, 0, 7086, 7088, 7090, 7092, 7094, 7096, 7098, 7100, 7102, 7104, 7106, 7108, 7110, 7112, 7114, 7114, 0, 0, 0, 0, 7116, 7118, 7120, 7122, 7124, 7126, 7128, 7130, 0, 0, 0, 0, 7132, 0, 7134, 7136, 7138, 7140, 0,
    7142, 7144, 7146, 7148, 7150, 7152, 0, 0, 0, 0, 0, 0, 0, 7154, 7156, 7158, 7160, 7162, 7164, 7166, 7168, 7170, 7172, 7174, 7176, 7160, 7160, 7160, 0, 0, 0, 0, 0, 7178, 7180, 7182, 7184, 7186, 7188, 0, 0, 7190, 7192, 0, 7194, 7196, 0, 7198, 7200, 0,
    0, 7202, 0, 0, 0, 0, 0, 0, 7204, 0, 0, 7206, 0, 7138, 0, 0, 7208, 7210, 7212, 0, 7214, 7216, 0, 0, 0, 0, 7214, 7218, 7220, 7222, 7224, 7226, 7228, 7230, 7232, 0, 0, 7234, 7236, 7238, 7240, 7242, 7244, 0, 7246, 7248, 7250, 7252, 7254, 7256,
    0, 7258, 7260, 0, 0, 0, 0, 6782, 6782, 0, 0, 0, 7594, 7262, 7264, 7266, 7268, 7270, 7272, 7274, 7276, 7278, 7280, 7282, 7284, 7286, 0, 7288, 7290, 7292, 7294, 7296, 7298, 7300, 7302, 7304, 7306, 7308, 7310, 7312, 7314, 7316, 7318, 7320, 7322, 7324, 7326, 7328, 7330, 7332,
    7334, 7336, 7338, 0, 0, 0, 0, 0, 0, 0, 0, 7586, 0, 7340, 7342, 7344, 7346, 7348, 7350, 7352, 7354, 7356, 7358, 7360, 7362, 7364, 7366, 7368, 7370, 7372, 7374, 7376, 7378, 7380, 7382, 7384, 7386, 7388, 0, 7390, 7392, 7394, 7396, 7398, 7376, 7520, 7400, 7402, 7404, 7406,
    0, 7408, 7410, 7412, 7414, 7416, 7418, 7420, 7422, 7424, 7426, 7428, 7430, 7432, 7434, 7436, 7438, 7440, 7442, 7444, 7446, 7448, 7450, 0, 7452, 0, 0, 0, 7454, 7456, 7458, 6760, 7460, 0, 0, 0, 0, 0, 7462, 0, 7464, 0, 7466, 7468, 7470, 7472, 0, 0, 7474, 7476,
    0, 0, 0, 0, 7214, 7214, 0, 0, 7478, 7480, 7482, 7484, 7486, 7488, 0, 0, 7490, 7492, 7494, 7496, 7498, 7500, 7502, 7504, 7506, 7508, 7510, 0, 7512, 6914, 6984, 7514, 7516, 7518, 7268, 0, 7542, 0, 7520, 6782, 0, 0, 7522, 7524, 7468, 6698, 7482, 7522, 7530, 7526,
    7528, 7368, 7530, 7532, 0, 7534, 7536, 7538, 7540, 7542, 7544, 7546, 7548, 7550, 7552, 7554, 0, 7556, 7558, 7560, 7024, 7562, 7564, 7566, 7568, 0, 0, 7570, 7572, 7574, 7576, 0, 7578, 7580, 7582, 7584, 7586, 7588, 7590, 7592, 7524, 7566, 6680, 6896, 7542, 7594, 0, 0, 0, 0,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 7596, 0, 0, 0, 0, 7598, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7600, 0, 0, 0, 0, 0, 0, 0, 7602, 7604, 7606, 7608,
  };
  private int submapModelLoadingStage_800f9e5a;
  private final Vector2i tpage_800f9e5c = new Vector2i();
  private final Vector2i clut_800f9e5e = new Vector2i();
  private int _800f9e60;

  private int snowLoadingStage_800f9e64;

  private int _800f9e68;
  private int _800f9e6a;
  private int _800f9e6c;
  private int _800f9e6e;
  private int _800f9e70;
  private boolean _800f9e74;
  private int _800f9e78;

  private final Struct18[] _800f9e7c = new Struct18[8];
  private int momentaryIndicatorTicks_800f9e9c;

  private int _800f9ea0;

  private int submapEffectsLoadMode_800f9ea8;
  /**
   * <ul>
   *   <li>-1 - unload</li>
   *   <li>0 - not loaded</li>
   *   <li>1 - textures loaded</li>
   *   <li>2 - fully loaded</li>
   * </ul>
   */
  private int submapEffectsState_800f9eac;
  private final String[] miscTextures_800f9eb0 = {
    this.bigArrow_800d7620,
    this.smallArrow_800d7c60,
    this.savepoint_800d7ee0,
    this.tim_800d8520,
    this.tim_800d85e0,
    this.savepointBigCircle_800d8720,
    this.dust_800d8960,
    this.leftFoot_800d8ba0,
    this.rightFoot_800d8ce0,
    this.smoke1_800d8e20,
    this.smoke2_800d9060,
  };

  @Override
  public Function<RunningScript, FlowControl>[] getScriptFunctions() {
    final Function<RunningScript, FlowControl>[] functions = new Function[1024];
    functions[12] = this::scriptRestoreCharDataVitals;
    functions[13] = this::scriptClearStatusEffects;
    functions[14] = this::scriptCloneCharacterData;
    functions[15] = this::scriptSetCharAddition;

    functions[18] = this::scriptGetCharAddition;
    functions[19] = this::scriptMaxOutDartDragoon;

    functions[96] = this::scriptSelfLoadSobjModelAndAnimation;
    functions[97] = this::scriptSelfLoadSobjAnimation;
    functions[98] = this::scriptSelfGetSobjAnimation;
    functions[99] = this::FUN_800df1f8;
    functions[100] = this::FUN_800df228;
    functions[101] = this::scriptSetModelPosition;
    functions[102] = this::scriptReadModelPosition;
    functions[103] = this::scriptSetModelRotate;
    functions[104] = this::scriptReadModelRotate;
    functions[105] = this::scriptSelfFacePoint;
    functions[106] = this::FUN_800df410;
    functions[107] = this::FUN_800df440;
    functions[108] = this::FUN_800df488;
    functions[109] = this::FUN_800df4d0;
    functions[110] = this::FUN_800df500;
    functions[111] = this::FUN_800df530;
    functions[112] = this::FUN_800df560;
    functions[113] = this::scriptSelfEnableTextureAnimation;
    functions[114] = this::FUN_800df590;
    functions[115] = this::scriptSelfDisableTextureAnimation;
    functions[116] = this::FUN_800df620;
    functions[117] = this::scriptSelfAttachCameraToSobj;
    functions[118] = this::scriptSelfIsCameraAttached;
    functions[119] = this::scriptSetCameraPos;
    functions[120] = this::scriptRotateSobj;
    functions[121] = this::scriptRotateSobjAbsolute;
    functions[122] = this::FUN_800df904;
    functions[123] = this::scriptMovePlayer;
    functions[124] = this::scriptFacePlayer;
    functions[125] = this::FUN_800df9a8;
    functions[126] = this::scriptGetCurrentSubmapId;
    functions[127] = this::scriptSelfGetSobjNobj;

    functions[198] = this::scriptAddSobjTextbox;

    functions[256] = this::scriptMapTransition;
    functions[257] = this::scriptGetCameraOffset;
    functions[258] = this::scriptSetCameraOffset;
    functions[259] = this::scriptGetCollisionAndTransitionInfo;
    functions[260] = this::FUN_800e69e8;
    functions[261] = this::FUN_800e69f0;
    functions[262] = this::FUN_800e6a28;
    functions[263] = this::scriptSetEnvForegroundPosition;
    functions[264] = this::scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject;
    functions[265] = this::scriptGetSetEncountersDisabled;
    functions[266] = this::FUN_800e6aa0;
    functions[267] = this::FUN_800e6b64;
    functions[268] = this::FUN_800e6bd8;
    functions[269] = this::FUN_800e6be0;
    functions[270] = this::FUN_800e6cac;
    functions[271] = this::scriptStartFmv;

    functions[288] = this::scriptSelfHideModelPart;
    functions[289] = this::scriptSelfShowModelPart;
    functions[290] = this::scriptSelfFaceCamera;
    functions[291] = this::scriptScaleXyz;
    functions[292] = this::scriptScaleUniform;
    functions[293] = this::scriptSetModelZOffset;
    functions[294] = this::scriptSelfSetSobjFlag;
    functions[295] = this::FUN_800dfd10;
    functions[296] = this::FUN_800de334;
    functions[297] = this::FUN_800de4b4;
    functions[298] = this::scriptShowAlertIndicator;
    functions[299] = this::scriptHideAlertIndicator;
    functions[300] = this::FUN_800dfd48;
    functions[301] = this::FUN_800e05c8;
    functions[302] = this::FUN_800e05f0;
    functions[303] = this::scriptEnableSobjFlatLight;
    functions[304] = this::scriptDisableSobjFlatLight;
    functions[305] = this::scriptSetSobjCollision1;
    functions[306] = this::scriptGetSobjCollidedWith1;
    functions[307] = this::scriptSetSobjCollision2;
    functions[308] = this::scriptGetSobjCollidedWith2;
    functions[309] = this::scriptSetAmbientColour;
    functions[310] = this::scriptResetAmbientColour;
    functions[311] = this::scriptEnableShadow;
    functions[312] = this::scriptDisableShadow;
    functions[313] = this::scriptSetShadowSize;
    functions[314] = this::scriptSetShadowOffset;
    functions[315] = this::scriptCheckPlayerCollision;
    functions[316] = this::scriptGetPlayerMovement;
    functions[317] = this::scriptSwapShadowTexture;
    functions[318] = this::scriptSetSobjPlayerCollisionMetrics;
    functions[319] = this::FUN_800e0cb8;

    functions[672] = this::scriptLoadSobjModelAndAnimation;
    functions[673] = this::scriptLoadSobjAnimation;
    functions[674] = this::scriptGetSobjAnimation;
    functions[675] = this::FUN_800dffa4;
    functions[676] = this::FUN_800dffdc;
    functions[677] = this::scriptFacePoint;
    functions[678] = this::FUN_800e0094;
    functions[679] = this::FUN_800de668;
    functions[680] = this::FUN_800de944;
    functions[681] = this::FUN_800e00cc;
    functions[682] = this::FUN_800e0148;
    functions[683] = this::FUN_800e01bc;
    functions[684] = this::scriptEnableTextureAnimation;
    functions[685] = this::FUN_800e0204;
    functions[686] = this::scriptDisableTextureAnimation;
    functions[687] = this::FUN_800e02c0;
    functions[688] = this::scriptAttachCameraToSobj;
    functions[689] = this::FUN_800deba0;
    functions[690] = this::scriptGetSobjNobj;
    functions[691] = this::scriptHideModelPart;
    functions[692] = this::scriptShowModelPart;
    functions[693] = this::scriptFaceCamera;
    functions[694] = this::scriptSetSobjFlag;
    functions[695] = this::scriptGetSobjFlag;
    functions[696] = this::loadInterpolatedSobjAnimation;
    functions[697] = this::loadUninterpolatedSobjAnimation;
    functions[698] = this::FUN_800e0184;
    functions[699] = this::scriptSetChapterTitleCardReadyToRender;
    functions[700] = this::scriptGetChapterTitleCardAnimationComplete;
    functions[701] = this::scriptLoadChapterTitleCard;
    functions[702] = this::scriptIsChapterTitleCardLoaded;
    functions[703] = this::scriptSetTitleCardAnimationPauseTicks;

    functions[768] = this::FUN_800f2048;
    functions[769] = this::FUN_800f1f9c;
    functions[770] = this::FUN_800f1060;
    functions[771] = this::FUN_800f2264;
    functions[772] = this::scriptAddSavePoint;
    functions[773] = this::FUN_800f23ec;
    functions[774] = this::FUN_800f2780;
    functions[775] = this::FUN_800f2090;
    functions[776] = this::FUN_800f2198;
    functions[777] = this::FUN_800f1eb8;
    functions[778] = this::scriptAllocateTriangleIndicatorArray;
    functions[779] = this::FUN_800f1b64;
    functions[780] = this::FUN_800f26c8;
    functions[781] = this::FUN_800f1d0c;
    functions[782] = this::FUN_800f14f0;
    functions[783] = this::FUN_800f24d8;
    functions[784] = this::FUN_800f24b0;
    functions[785] = this::FUN_800f23a0;
    functions[786] = this::FUN_800f1634;
    functions[787] = this::FUN_800f22c4;
    functions[788] = this::FUN_800f2554;
    functions[789] = this::FUN_800f25a8;
    functions[790] = this::FUN_800f1274;
    return functions;
  }

  @Override
  public boolean allowsWidescreen() {
    return false;
  }

  /** Pulled from BPE segment */
  @Method(0x800217a4L)
  private void FUN_800217a4(final Model124 model) {
    model.coord2_14.transforms.rotate.y = this.FUN_800ea4c8(model.coord2_14.transforms.rotate.y);
    model.coord2_14.coord.rotationXYZ(model.coord2_14.transforms.rotate);
    model.coord2_14.coord.scale(model.coord2_14.transforms.scale);
    model.coord2_14.flg = 0;
  }

  @ScriptDescription("Adds a textbox to a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The textbox index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "submapObjectIndex", description = "The submap object, but may also have the flag 0x1000 set (unknown meaning)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "packedData", description = "Unknown data, 3 nibbles")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The textbox width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The textbox height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.STRING, name = "text", description = "The textbox text")
  @Method(0x80025218L)
  private FlowControl scriptAddSobjTextbox(final RunningScript<?> script) {
    if(script.params_20[2].get() == 0) {
      return FlowControl.CONTINUE;
    }

    final int textboxIndex = script.params_20[0].get();
    final int textType = textboxTextType_80052ba8[script.params_20[2].get() >>> 8 & 0xf];
    clearTextbox(textboxIndex);

    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    textbox.type_04 = TextboxType.fromInt(textboxMode_80052b88[script.params_20[2].get() >>> 4 & 0xf]);
    textbox.renderBorder_06 = renderBorder_80052b68[script.params_20[2].get() & 0xf];
    textbox.x_14 = 0;
    textbox.y_16 = 0;
    textbox.chars_18 = script.params_20[3].get() + 1;
    textbox.lines_1a = script.params_20[4].get() + 1;
    clearTextboxText(textboxIndex);

    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];
    textboxText.type_04 = textType;
    textboxText.str_24 = LodString.fromParam(script.params_20[5]);

    if(textType == 1 && (script.params_20[1].get() & 0x1000) > 0) {
      textboxText.flags_08 |= 0x20;
    }

    //LAB_80025370
    //LAB_80025374
    if(textType == 3) {
      textboxText.selectionIndex_6c = -1;
    }

    //LAB_800253a4
    if(textType == 4) {
      textboxText.flags_08 |= TextboxText84.HAS_NAME;
    }

    //LAB_800253d4
    textboxText.flags_08 |= TextboxText84.SHOW_ARROW;
    textboxText.chars_58 = new TextboxChar08[textboxText.chars_1c * (textboxText.lines_1e + 1)];
    Arrays.setAll(textboxText.chars_58, i -> new TextboxChar08());
    this.positionSobjTextbox(textboxIndex, script.params_20[1].get());

    if(textType == 2) {
      textbox._38 = textbox.x_14;
      textbox._3c = textbox.y_16;
      textbox.x_14 = textbox.currentX_28;
      textbox.y_16 = textbox.currentY_2c;
      textbox.flags_08 |= 0x2;
    }

    //LAB_80025494
    //LAB_80025498
    return FlowControl.CONTINUE;
  }

  /** Calculates a good position to place a textbox for a specific sobj */
  @Method(0x80028938L)
  private void positionSobjTextbox(final int textboxIndex, final int sobjIndex) {
    final Textbox4c textbox = textboxes_800be358[textboxIndex];
    final TextboxText84 textboxText = textboxText_800bdf38[textboxIndex];

    this.cacheSobjScreenBounds(sobjIndex);
    final SubmapCaches80 caches = this.caches_800c68e8;
    final float offsetX = caches.bottomMiddle_70.x - caches.bottomLeft_68.x;
    final float offsetY = (caches.bottomMiddle_70.y - caches.topMiddle_78.y) / 2;
    textbox.currentX_28 = caches.bottomMiddle_70.x;
    textbox.currentY_2c = caches.bottomMiddle_70.y - offsetY;
    final int textWidth = textbox.chars_18 * 9 / 2;
    final int textHeight = textbox.lines_1a * 6;

    final int width;
    if(engineState_8004dd20 != EngineStateEnum.SUBMAP_05) {
      width = 320;
    } else {
      width = 360;
    }

    //LAB_80028a20
    if(textboxText.chars_1c >= 17) {
      if(textbox.currentY_2c >= 121) {
        //LAB_80028acc
        final int x = width / 2;
        final float y = textbox.currentY_2c - offsetY - textHeight;
        textbox.x_14 = x;
        textbox.y_16 = y;

        textboxText.x_14 = x;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
        return;
      }

      final int x = width / 2;
      final float y = textbox.currentY_2c + offsetY + textHeight;
      textbox.x_14 = x;
      textbox.y_16 = y;

      textboxText.x_14 = x;
      textboxText.y_16 = y;
      textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
      textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
      return;
    }

    //LAB_80028b38
    float y = textbox.currentY_2c - offsetY - textHeight;
    if(textboxFits(textboxIndex, textbox.currentX_28, y)) {
      textbox.x_14 = textbox.currentX_28;
      textbox.y_16 = y;

      textboxText.x_14 = textbox.currentX_28;
      textboxText.y_16 = y;
      textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
      textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
      return;
    }

    //LAB_80028bc4
    y = textbox.currentY_2c + offsetY + textHeight;
    if(textboxFits(textboxIndex, textbox.currentX_28, y)) {
      textbox.x_14 = textbox.currentX_28;
      textbox.y_16 = y;

      textboxText.x_14 = textbox.currentX_28;
      textboxText.y_16 = y;
      textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
      textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
      return;
    }

    //LAB_80028c44
    if(width / 2.0f < textbox.currentX_28) {
      //LAB_80028d58
      final float x = textbox.currentX_28 - offsetX - textWidth;
      y = textbox.currentY_2c - offsetY - textHeight / 2.0f;
      if(textboxFits(textboxIndex, x, y)) {
        textbox.x_14 = x;
        textbox.y_16 = y;

        textboxText.x_14 = x;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
        return;
      }

      //LAB_80028df0
      y = textbox.currentY_2c + offsetY + textHeight / 2.0f;
      if(textboxFits(textboxIndex, x, y)) {
        textbox.x_14 = x;
        textbox.y_16 = y;

        textboxText.x_14 = x;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
        return;
      }
    } else {
      final float x = textbox.currentX_28 + offsetX + textWidth;
      y = textbox.currentY_2c - offsetY - textHeight / 2.0f;
      if(textboxFits(textboxIndex, x, y)) {
        textbox.x_14 = x;
        textbox.y_16 = y;

        textboxText.x_14 = x;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
        return;
      }

      //LAB_80028ce4
      y = textbox.currentY_2c + offsetY + textHeight / 2.0f;
      if(textboxFits(textboxIndex, x, y)) {
        textbox.x_14 = x;
        textbox.y_16 = y;

        textboxText.x_14 = x;
        textboxText.y_16 = y;
        textboxText._18 = textboxText.x_14 - textboxText.chars_1c * 4.5f;
        textboxText._1a = textboxText.y_16 - textboxText.lines_1e * 6.0f;
        return;
      }
    }

    //LAB_80028e68
    final float x;
    if(width / 2.0f >= textbox.currentX_28) {
      x = textbox.currentX_28 + offsetX + textWidth;
    } else {
      //LAB_80028e8c
      x = textbox.currentX_28 - offsetX - textWidth;
    }

    //LAB_80028e9c
    calculateAppropriateTextboxBounds(textboxIndex, x, textbox.currentY_2c + offsetY + textHeight);

    //LAB_80028ef0
  }

  /** Pulled from BPE segment */
  @Method(0x8002aae8L)
  private boolean FUN_8002aae8() {
    boolean s0 = false;
    switch(submapEnvState_80052c44) {
      case 0 -> {
        s0 = true;
        this.renderEnvironment();
      }

      case 1, 2 -> {
        return false;
      }

      case 3 -> {
        this.FUN_800e8e50();
        this.FUN_800e828c();
        this.FUN_800e4f8c();
        this.unloadSmap();
        this.FUN_800e4e5c();

        //LAB_8002ab98
        submapEnvState_80052c44 = 2;
      }

      case 4 -> {
        this.renderEnvironment();
        this.FUN_800e8e50();
        this.FUN_800e828c();
        this.FUN_800e4f8c();
        this.unloadSmap();
        this.FUN_800e4e5c();

        //LAB_8002ab98
        submapEnvState_80052c44 = 2;
      }

      case 5 -> {
        this.renderEnvironment();
        this.FUN_800e8e50();
        this.FUN_800e828c();
        this.FUN_800e4f8c();
        this.unloadSmap();
        submapEnvState_80052c44 = 2;
      }
    }

    //caseD_6
    if((this.submapFlags_800f7e54 & 0x1) == 0) {
      // If an encounter should start
      if(this.handleEncounters() != 0) {
        this.mapTransition(-1, 0);
      }
    }

    //LAB_8002abdc
    //LAB_8002abe0
    final int collisionAndTransitionInfo = this.getCollisionAndTransitionInfo(index_80052c38);
    if((collisionAndTransitionInfo & 0x10) != 0) {
      this.mapTransition(collisionAndTransitionInfo >>> 22, collisionAndTransitionInfo >>> 16 & 0x3f);
    }

    //LAB_8002ac10
    //LAB_8002ac14
    return s0;
  }

  @Method(0x800d9b08L)
  private void restoreCharDataVitals(final int charId) {
    loadCharacterStats();

    if(charId >= 0) {
      final ActiveStatsa0 stats = stats_800be5f8[charId];
      final CharacterData2c charData = gameState_800babc8.charData_32c[charId];
      charData.hp_08 = stats.maxHp_66;
      charData.mp_0a = stats.maxMp_6e;
    } else {
      //LAB_800d9b70
      //LAB_800d9b84
      for(int charSlot = 0; charSlot < 9; charSlot++) {
        final ActiveStatsa0 stats = stats_800be5f8[charSlot];
        final CharacterData2c charData = gameState_800babc8.charData_32c[charSlot];
        charData.hp_08 = stats.maxHp_66;
        charData.mp_0a = stats.maxMp_6e;
      }
    }
  }

  @ScriptDescription("Restore vitals for all characters")
  @Method(0x800d9bc0L)
  private FlowControl scriptRestoreCharDataVitals(final RunningScript<?> script) {
    this.restoreCharDataVitals(-1);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears status effects for all characters")
  @Method(0x800d9bf4L)
  private FlowControl scriptClearStatusEffects(final RunningScript<?> script) {
    //LAB_800d9c04
    for(int i = 0; i < 9; i++) {
      gameState_800babc8.charData_32c[i].status_10 = 0;
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Overwrite one character's CharData with that of another character (also restores the destination character's vitals)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sourceCharId", description = "The source character")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "destCharId", description = "The destination character")
  @Method(0x800d9c1cL)
  private FlowControl scriptCloneCharacterData(final RunningScript<?> script) {
    //LAB_800d9c78
    gameState_800babc8.charData_32c[script.params_20[1].get()].set(gameState_800babc8.charData_32c[script.params_20[0].get()]);
    this.restoreCharDataVitals(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set a character's addition")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "additionId", description = "The addition ID")
  @Method(0x800d9ce4L)
  private FlowControl scriptSetCharAddition(final RunningScript<?> script) {
    gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a character's addition")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "additionId", description = "The addition ID")
  @Method(0x800d9d20L)
  private FlowControl scriptGetCharAddition(final RunningScript<?> script) {
    script.params_20[1].set(gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19);
    return FlowControl.CONTINUE;
  }

  /** Called when Dart is given Divine Dragon spirit */
  @ScriptDescription("Maxes out Dart's Dragoon and fully restores his HP/MP/SP")
  @Method(0x800d9d60L)
  private FlowControl scriptMaxOutDartDragoon(final RunningScript<?> script) {
    if(gameState_800babc8.charData_32c[0].dlevelXp_0e < 63901) {
      gameState_800babc8.charData_32c[0].dlevelXp_0e = 63901;
    }

    //LAB_800d9d90
    gameState_800babc8.charData_32c[0].dlevel_13 = 5;

    this.restoreVitalsAndSp(0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d9dc0L)
  private void restoreVitalsAndSp(final int charIndex) {
    gameState_800babc8.charData_32c[charIndex].sp_0c = 500;
    this.restoreCharDataVitals(-1);
  }

  @Method(0x800d9e64L)
  private void adjustSmapUvs(final ModelPart10 dobj2, final int colourMap) {
    final TmdObjTable1c objTable = dobj2.tmd_08;

    //LAB_800d9e90
    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      final int header = primitive.header();
      final int id = header & 0xff04_0000;

      if(id == 0x3400_0000 || id == 0x3600_0000 || id == 0x3500_0000 || id == 0x3700_0000) {
        this.adjustSmapTriPrimitiveUvs(primitive, colourMap & 0x7f);
      } else if(id == 0x3c00_0000 || id == 0x3e00_0000 || id == 0x3d00_0000 || id == 0x3f00_0000) {
        this.adjustSmapQuadPrimitiveUvs(primitive, colourMap & 0x7f);
      }
    }
  }

  @Method(0x800da114L)
  private void animateSmapModel(final Model124 model) {
    if(model.smallerStructPtr_a4 != null) {
      //LAB_800da138
      for(int i = 0; i < 4; i++) {
        if(model.smallerStructPtr_a4.uba_04[i]) {
          this.FUN_800dde70(model, i);
        }

        //LAB_800da15c
      }
    }

    //LAB_800da16c
    //LAB_800da174
    for(int i = 0; i < 7; i++) {
      if(model.animateTextures_ec[i]) {
        animateModelTextures(model, i);
      }

      //LAB_800da18c
    }

    if(model.animationState_9c == 2) {
      return;
    }

    if(model.animationState_9c == 0) {
      if(model.disableInterpolation_a2) {
        //LAB_800da1d0
        model.remainingFrames_9e = model.totalFrames_9a / 2;
      } else {
        model.remainingFrames_9e = model.totalFrames_9a;
      }

      model.interpolationFrameIndex = 0;

      //LAB_800da1e4
      model.animationState_9c = 1;
      model.partTransforms_94 = model.partTransforms_90;
    }

    //LAB_800da1f8
    if((model.remainingFrames_9e & 0x1) == 0 && !model.disableInterpolation_a2) {
      final ModelPartTransforms0c[][] old = model.partTransforms_94;

      if(model.ub_a3 == 0) {
        this.applyInterpolationFrame(model);
      } else {
        //LAB_800da23c
        applyModelPartTransforms(model);
      }

      model.partTransforms_94 = old;
    } else {
      //LAB_800da24c
      applyModelPartTransforms(model);
    }

    //LAB_800da254
    model.remainingFrames_9e--;

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    }

    //LAB_800da274
  }

  @Method(0x800da524L)
  private void renderSmapShadow(final Model124 model) {
    GsInitCoordinate2(model.coord2_14, shadowModel_800bda10.coord2_14);

    shadowModel_800bda10.zOffset_a0 = model.zOffset_a0 + 16;
    shadowModel_800bda10.coord2_14.transforms.scale.set(model.shadowSize_10c).div(64.0f);

    shadowModel_800bda10.coord2_14.coord.rotationXYZ(shadowModel_800bda10.coord2_14.transforms.rotate);
    shadowModel_800bda10.coord2_14.coord.scaleLocal(shadowModel_800bda10.coord2_14.transforms.scale);
    shadowModel_800bda10.coord2_14.coord.transfer.set(model.shadowOffset_118);

    final ModelPart10 modelPart = shadowModel_800bda10.modelParts_00[0];
    final GsCOORDINATE2 partCoord = modelPart.coord2_04;

    partCoord.transforms.rotate.zero();
    partCoord.transforms.trans.zero();

    partCoord.coord.rotationZYX(partCoord.transforms.rotate);
    partCoord.coord.transfer.set(partCoord.transforms.trans);

    final MV lw = new MV();
    final MV ls = new MV();
    GsGetLws(partCoord, lw, ls);
    GsSetLightMatrix(lw);

    GTE.setTransforms(ls);

    Renderer.renderDobj2(modelPart, false, 0);
    partCoord.flg--;
  }

  @Method(0x800da6c8L)
  private void adjustSmapTriPrimitiveUvs(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final UvAdjustmentMetrics14 metrics = this.uvAdjustments_800f5930[colourMap];

    //LAB_800da6e8
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & metrics.clutMaskOn_04 | metrics.clutMaskOff_00) + metrics.uvOffset_10);
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & metrics.tpageMaskOn_0c | metrics.tpageMaskOff_08) + metrics.uvOffset_10);
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + metrics.uvOffset_10);
    }
  }

  @Method(0x800da754L)
  private void adjustSmapQuadPrimitiveUvs(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final UvAdjustmentMetrics14 metrics = this.uvAdjustments_800f5930[colourMap];

    //LAB_800da774
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & metrics.clutMaskOn_04 | metrics.clutMaskOff_00) + metrics.uvOffset_10);
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & metrics.tpageMaskOn_0c | metrics.tpageMaskOff_08) + metrics.uvOffset_10);
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + metrics.uvOffset_10);
      MathHelper.set(data, 0xc, 4,  MathHelper.get(data, 0xc, 4) + metrics.uvOffset_10);
    }
  }

  @Method(0x800da920L)
  private void applyInterpolationFrame(final Model124 a0) {
    final ModelPartTransforms0c[][] transforms = a0.partTransforms_94;

    //LAB_800da96c
    for(int i = 0; i < a0.modelParts_00.length; i++) {
      final ModelPart10 dobj2 = a0.modelParts_00[i];

      final GsCOORDINATE2 coord2 = dobj2.coord2_04;
      final Transforms params = coord2.transforms;
      final MV matrix = coord2.coord;

      matrix.rotationZYX(params.rotate);

      params.trans.set(
        (params.trans.x + transforms[0][i].translate_06.x) / 2.0f,
        (params.trans.y + transforms[0][i].translate_06.y) / 2.0f,
        (params.trans.z + transforms[0][i].translate_06.z) / 2.0f
      );

      matrix.transfer.set(params.trans);
    }

    //LAB_800daa0c
    a0.partTransforms_94 = Arrays.copyOfRange(transforms, 1, transforms.length);
  }

  @Method(0x800daa3cL)
  private void renderSmapModel(final Model124 model) {
    zOffset_1f8003e8 = model.zOffset_a0;
    tmdGp0Tpage_1f8003ec = model.tpage_108;

    //LAB_800daaa8
    final MV lw = new MV();
    final MV ls = new MV();
    for(int i = 0; i < model.modelParts_00.length; i++) {
      if((model.partInvisible_f4 & 1L << i) == 0) {
        final ModelPart10 dobj2 = model.modelParts_00[i];

        GsGetLws(dobj2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);
        Renderer.renderDobj2(dobj2, false, 0);

        if(dobj2.obj != null) {
          RENDERER.queueModel(dobj2.obj, lw)
            .screenspaceOffset(this.screenOffsetX_800cb568 + 8, -this.screenOffsetY_800cb56c)
            .lightDirection(lightDirectionMatrix_800c34e8)
            .lightColour(lightColourMatrix_800c3508)
            .backgroundColour(GTE.backgroundColour);
        }
      }
    }

    //LAB_800dab34
    if(model.shadowType_cc != 0) {
      this.renderSmapShadow(model);
    }

    //LAB_800dab4c
  }

  @Method(0x800dde70L)
  private void FUN_800dde70(final Model124 struct, final int index) {
    final SmallerStruct smallerStruct = struct.smallerStructPtr_a4;

    if(smallerStruct.tmdSubExtensionArr_20[index] == null) {
      smallerStruct.uba_04[index] = false;
    } else {
      //LAB_800ddeac
      final int colourMap = struct.vramSlot_9d & 0x7f;
      final int x = _800503f8[colourMap];
      final int y = _80050424[colourMap] + 112;

      final TmdSubExtension v = smallerStruct.tmdSubExtensionArr_20[index];
      int a1 = 0;

      //LAB_800ddef8
      for(int i = 0; i < smallerStruct.sa_08[index]; i++) {
        a1 += 2;
      }

      //LAB_800ddf08
      final int sourceYOffset = v.sa_04[a1];
      a1++;

      smallerStruct.sa_10[index]++;

      if(smallerStruct.sa_10[index] == v.sa_04[a1]) {
        smallerStruct.sa_10[index] = 0;

        if(v.sa_04[a1 + 1] == -1) {
          smallerStruct.sa_08[index] = 0;
        } else {
          //LAB_800ddf70
          smallerStruct.sa_08[index]++;
        }
      }

      //LAB_800ddf8c
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(x, y + sourceYOffset, x, y + smallerStruct.sa_18[index], 16, 1));
    }

    //LAB_800ddff4
  }

  @Override
  @Method(0x800de004L)
  public void modelLoaded(final Model124 model, final CContainer cContainer) {
    if(cContainer.ext_04 == null) {
      //LAB_800de120
      model.smallerStructPtr_a4 = null;
      return;
    }

    final SmallerStruct smallerStruct = new SmallerStruct();
    model.smallerStructPtr_a4 = smallerStruct;

    smallerStruct.tmdExt_00 = cContainer.ext_04;

    //LAB_800de05c
    for(int i = 0; i < 4; i++) {
      smallerStruct.tmdSubExtensionArr_20[i] = smallerStruct.tmdExt_00.tmdSubExtensionArr_00[i];

      if(smallerStruct.tmdSubExtensionArr_20[i] == null) {
        smallerStruct.uba_04[i] = false;
      } else {
        smallerStruct.sa_08[i] = 0;
        smallerStruct.sa_10[i] = 0;
        smallerStruct.sa_18[i] = smallerStruct.tmdSubExtensionArr_20[i].s_02;
        smallerStruct.uba_04[i] = smallerStruct.sa_18[i] != -1;
      }

      //LAB_800de108
    }

    //LAB_800de124
  }

  @Method(0x800de138L)
  private void FUN_800de138(final Model124 model, final int index) {
    final SmallerStruct smallerStruct = model.smallerStructPtr_a4;

    if(smallerStruct.tmdSubExtensionArr_20[index] == null) {
      smallerStruct.uba_04[index] = false;
      return;
    }

    //LAB_800de164
    smallerStruct.sa_08[index] = 0;
    smallerStruct.sa_10[index] = 0;
    smallerStruct.sa_18[index] = smallerStruct.tmdSubExtensionArr_20[index].s_02;
    smallerStruct.uba_04[index] = smallerStruct.sa_18[index] != -1;
  }

  @ScriptDescription("Moves the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Delta X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Delta Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Delta Z position")
  @Method(0x800de1d0L)
  private FlowControl scriptMovePlayer(final RunningScript<SubmapObject210> script) {
    final short deltaX = (short)script.params_20[0].get();
    final short deltaY = (short)script.params_20[1].get();
    final short deltaZ = (short)script.params_20[2].get();

    if(deltaX != 0 || deltaY != 0 || deltaZ != 0) {
      final Vector3f deltaMovement = new Vector3f();
      final Vector3f worldspaceDeltaMovement = new Vector3f();

      //LAB_800de218
      final SubmapObject210 player = script.scriptState_04.innerStruct_00;
      final Model124 playerModel = player.model_00;

      deltaMovement.set(deltaX, deltaY, deltaZ);
      GTE.setTransforms(worldToScreenMatrix_800c3548);
      this.transformToWorldspace(worldspaceDeltaMovement, deltaMovement);

      final int s2 = this.FUN_800e88a0(player.sobjIndex_12e, playerModel.coord2_14.coord.transfer, worldspaceDeltaMovement);
      if(s2 >= 0) {
        if(this.FUN_800e6798(s2) != 0) {
          playerModel.coord2_14.coord.transfer.x += worldspaceDeltaMovement.x;
          playerModel.coord2_14.coord.transfer.y = worldspaceDeltaMovement.y;
          playerModel.coord2_14.coord.transfer.z += worldspaceDeltaMovement.z;
        }

        //LAB_800de2c8
        player.ui_16c = s2;
      }

      //LAB_800de2cc
      player.us_170 = 0;
      this.sobjs_800c6880[player.sobjIndex_130].setTempTicker(this::FUN_800e3e60);
      this.caches_800c68e8.playerPos_00.set(worldspaceDeltaMovement);
    }

    //LAB_800de318
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to triangle indicators")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800de334L)
  private FlowControl FUN_800de334(final RunningScript<?> script) {
    final Vector3f sp0x10 = new Vector3f();
    this.get3dAverageOfSomething(script.params_20[0].get(), sp0x10);
    this.playerModel_800c6748.coord2_14.coord.transfer.set(sp0x10);
    final MV lw = new MV();
    final MV ls = new MV();
    GsGetLws(this.playerModel_800c6748.coord2_14, lw, ls);
    GTE.setTransforms(ls);
    GTE.perspectiveTransform(0, 0, 0);
    final float x = GTE.getScreenX(2);
    final float y = GTE.getScreenY(2);

    //LAB_800de438
    final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;
    for(int i = 0; i < 20; i++) {
      if(indicator._18[i] == -1) {
        indicator.x_40[i] = x;
        indicator.y_68[i] = y;
        indicator._18[i] = (short)script.params_20[1].get();
        indicator.screenOffsetX_90[i] = this.screenOffsetX_800cb568;
        indicator.screenOffsetY_e0[i] = this.screenOffsetY_800cb56c;
        break;
      }
    }

    //LAB_800de49c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to triangle indicators")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "p0")
  @Method(0x800de4b4L)
  private FlowControl FUN_800de4b4(final RunningScript<?> script) {
    final Vector3f sp0x10 = new Vector3f();
    final MV sp0x28 = new MV();
    final MV sp0x48 = new MV();

    final Param ints = script.params_20[0];
    int s0 = 0;

    //LAB_800de4f8
    while(ints.array(s0).get() != -1) {
      this.get3dAverageOfSomething(ints.array(s0++).get(), sp0x10);
      this.playerModel_800c6748.coord2_14.coord.transfer.set(sp0x10);

      GsGetLws(this.playerModel_800c6748.coord2_14, sp0x48, sp0x28);
      GTE.setTransforms(sp0x28);
      GTE.perspectiveTransform(0, 0, 0);
      final float x = GTE.getScreenX(2);
      final float y = GTE.getScreenY(2);

      //LAB_800de5d4
      for(int i = 0; i < 20; i++) {
        final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;

        if(indicator._18[i] == -1) {
          indicator.x_40[i] = x;
          indicator.y_68[i] = y;
          indicator._18[i] = (short)ints.array(s0).get();
          indicator.screenOffsetX_90[i] = this.screenOffsetX_800cb568;
          indicator.screenOffsetY_e0[i] = this.screenOffsetY_800cb56c;
          break;
        }
      }

      s0++;
    }

    //LAB_800de644
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something to do with forced movement. Used when Dart is halfway through his jump animation in volcano.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Possibly movement destination X")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Possibly movement destination Y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Possibly movement destination Z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "i_144", description = "Possibly movement frames")
  @Method(0x800de668L)
  private FlowControl FUN_800de668(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    sobj.movementDestination_138.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    sobj.movementTicks_144 = script.params_20[4].get();

    sobj.us_170 = 1;

    if(sobj.movementTicks_144 != 0) {
      sobj.movementStep_148.set(sobj.movementDestination_138).sub(model.coord2_14.coord.transfer).div(sobj.movementTicks_144);
    } else {
      sobj.movementStep_148.zero();
    }

    //LAB_800de7a8
    float x = 0.0f;
    float y = 0.0f;
    float z = 0.0f;
    if(sobj.movementTicks_144 != 0) {
      x = Math.abs((sobj.movementDestination_138.x - model.coord2_14.coord.transfer.x) / sobj.movementTicks_144);
      y = Math.abs((sobj.movementDestination_138.y - model.coord2_14.coord.transfer.y) / sobj.movementTicks_144);
      z = Math.abs((sobj.movementDestination_138.z - model.coord2_14.coord.transfer.z) / sobj.movementTicks_144);
    }

    //LAB_800de8e8
    sobj.movementStep12_154.set(x, y, z);
    sobj.movementDistanceMoved12_160.zero();

    this.sobjs_800c6880[sobj.sobjIndex_130].setTempTicker(this::FUN_800e1f90);

    sobj.flags_190 &= 0x7fff_ffff;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something to do with forced movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Movement destination X")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Movement destination Y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Movement destination Z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Movement ticks")
  @Method(0x800de944L)
  private FlowControl FUN_800de944(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    sobj.movementDestination_138.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    sobj.movementTicks_144 = script.params_20[4].get();

    sobj.movementStep_148.x = (sobj.movementDestination_138.x - model.coord2_14.coord.transfer.x) / sobj.movementTicks_144;
    sobj.movementStep_148.z = (sobj.movementDestination_138.z - model.coord2_14.coord.transfer.z) / sobj.movementTicks_144;

    //LAB_800dea34
    sobj.movementStep12_154.x = Math.abs((sobj.movementDestination_138.x - model.coord2_14.coord.transfer.x) / sobj.movementTicks_144);
    sobj.movementStep12_154.z = Math.abs((sobj.movementDestination_138.z - model.coord2_14.coord.transfer.z) / sobj.movementTicks_144);

    sobj.movementStepY_134 = ((sobj.movementDestination_138.y - model.coord2_14.coord.transfer.y) * 2 - sobj.movementTicks_144 * 7 * (sobj.movementTicks_144 - 1)) / (sobj.movementTicks_144 * 2);
    sobj.movementDistanceMoved12_160.x = 0;
    sobj.movementDistanceMoved12_160.z = 0;
    sobj.us_170 = 2;
    sobj.s_172 = 1;
    sobj.movementStepAccelerationY_18c = 7;
    this.sobjs_800c6880[sobj.sobjIndex_130].setTempTicker(this::tickSobjMovement);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something to do with forced movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Movement destination X")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Movement destination Y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Movement destination Z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Movement ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepAccelerationY", description = "Y step acceleration")
  @Method(0x800deba0L)
  private FlowControl FUN_800deba0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.movementDestination_138.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    sobj.movementTicks_144 = script.params_20[4].get();
    sobj.movementStepAccelerationY_18c = script.params_20[5].get() + 5;
    sobj.movementStep_148.x = (sobj.movementDestination_138.x - sobj.model_00.coord2_14.coord.transfer.x) / sobj.movementTicks_144;
    sobj.movementStep_148.z = (sobj.movementDestination_138.z - sobj.model_00.coord2_14.coord.transfer.z) / sobj.movementTicks_144;

    //LAB_800decbc
    sobj.movementStep12_154.x = Math.abs((sobj.movementDestination_138.x - sobj.model_00.coord2_14.coord.transfer.x) / sobj.movementTicks_144);
    sobj.movementStep12_154.z = Math.abs((sobj.movementDestination_138.z - sobj.model_00.coord2_14.coord.transfer.z) / sobj.movementTicks_144);

    sobj.s_174 = sobj.s_172;
    sobj.s_172 = 1;
    sobj.us_170 = 2;
    sobj.movementDistanceMoved12_160.x = 0;
    sobj.movementDistanceMoved12_160.z = 0;
    sobj.movementStepY_134 = ((sobj.movementDestination_138.y - sobj.model_00.coord2_14.coord.transfer.y) * 2 - sobj.movementTicks_144 * sobj.movementStepAccelerationY_18c * (sobj.movementTicks_144 - 1)) / (sobj.movementTicks_144 * 2);
    this.sobjs_800c6880[sobj.sobjIndex_130].setTempTicker(this::tickSobjMovement);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks player collision")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deltaX", description = "Movement along the X axis")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deltaY", description = "Movement along the Y axis")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deltaZ", description = "Movement along the Z axis")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "collidee", description = "The SubmapObject210 script index collided with, or -1 if not collided")
  @Method(0x800dee28L)
  private FlowControl scriptCheckPlayerCollision(final RunningScript<SubmapObject210> script) {
    final Vector3f deltaMovement = new Vector3f();
    final Vector3f movement = new Vector3f();

    final SubmapObject210 sobj = script.scriptState_04.innerStruct_00;
    final Model124 model = sobj.model_00;

    final short deltaX = (short)script.params_20[0].get();
    final short deltaY = (short)script.params_20[1].get();
    final short deltaZ = (short)script.params_20[2].get();

    final float angle;
    if(deltaX != 0 || deltaY != 0 || deltaZ != 0) {
      //LAB_800dee98
      //LAB_800dee9c
      //LAB_800deea0
      deltaMovement.set(deltaX, deltaY, deltaZ);
      GTE.setTransforms(worldToScreenMatrix_800c3548);
      this.transformToWorldspace(movement, deltaMovement);

      final int collisionResult = this.FUN_800e88a0(sobj.sobjIndex_12e, model.coord2_14.coord.transfer, movement);
      if(collisionResult >= 0) {
        this.FUN_800e6798(collisionResult); //TODO does nothing?
      }

      //LAB_800def08
      angle = MathHelper.positiveAtan2(movement.z, movement.x);
    } else {
      movement.set(0.0f, model.coord2_14.coord.transfer.y, 0.0f);
      angle = model.coord2_14.transforms.rotate.y;
    }

    //LAB_800def28
    this.caches_800c68e8.playerMovement_0c.set(movement).add(model.coord2_14.coord.transfer);
    final int reachX = Math.round(MathHelper.sin(angle) * -sobj.playerCollisionReach_1c0);
    final int reachZ = Math.round(MathHelper.cos(angle) * -sobj.playerCollisionReach_1c0);
    final float colliderMinY = movement.y - sobj.playerCollisionSizeVertical_1bc;
    final float colliderMaxY = movement.y + sobj.playerCollisionSizeVertical_1bc;

    //LAB_800df008
    //LAB_800df00c
    //LAB_800df02c
    // Handle collision with other sobjs
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      final SubmapObject210 struct = this.sobjs_800c6880[i].innerStruct_00;

      if(struct != sobj && (struct.flags_190 & 0x10_0000) != 0) {
        final float x = struct.model_00.coord2_14.coord.transfer.x - (model.coord2_14.coord.transfer.x + movement.x + reachX);
        final float z = struct.model_00.coord2_14.coord.transfer.z - (model.coord2_14.coord.transfer.z + movement.z + reachZ);
        final int size = sobj.playerCollisionSizeHorizontal_1b8 + struct.playerCollisionSizeHorizontal_1b8;
        final float collideeMinY = struct.model_00.coord2_14.coord.transfer.y - struct.playerCollisionSizeVertical_1bc;
        final float collideeMaxY = struct.model_00.coord2_14.coord.transfer.y + struct.playerCollisionSizeVertical_1bc;

        //LAB_800df104
        if(size * size >= x * x + z * z && (collideeMinY >= colliderMinY && collideeMinY <= colliderMaxY || collideeMaxY >= colliderMinY && collideeMaxY <= colliderMaxY)) {
          //LAB_800df118
          script.params_20[3].set(i);
          return FlowControl.CONTINUE;
        }
      }

      //LAB_800df128
    }

    //LAB_800df13c
    script.params_20[3].set(-1);

    //LAB_800df14c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Load a model/animation into this submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "objectIndex", description = "The map object index (0-n)")
  @Method(0x800df168L)
  private FlowControl scriptSelfLoadSobjModelAndAnimation(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptLoadSobjModelAndAnimation(script);
  }

  @ScriptDescription("Load a new animation into this submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animIndex", description = "The anim index")
  @Method(0x800df198L)
  private FlowControl scriptSelfLoadSobjAnimation(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptLoadSobjAnimation(script);
  }

  @ScriptDescription("Get a submap object's animation")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "animIndex", description = "The anim index")
  @Method(0x800df1c8L)
  private FlowControl scriptSelfGetSobjAnimation(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptGetSobjAnimation(script);
  }

  @ScriptDescription("Set us_12a on this submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new value")
  @Method(0x800df1f8L)
  private FlowControl FUN_800df1f8(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800dffa4(script);
  }

  @ScriptDescription("Get us_12a from this submap object")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The value")
  @Method(0x800df228L)
  private FlowControl FUN_800df228(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800dffdc(script);
  }

  @ScriptDescription("Set a submap object's position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The new X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The new Y coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The new Z coordinate")
  @Method(0x800df258L)
  private FlowControl scriptSetModelPosition(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    model.coord2_14.coord.transfer.x = script.params_20[1].get();
    model.coord2_14.coord.transfer.y = script.params_20[2].get();
    model.coord2_14.coord.transfer.z = script.params_20[3].get();
    sobj.us_170 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a submap object's position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y coordinate")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z coordinate")
  @Method(0x800df2b8L)
  private FlowControl scriptReadModelPosition(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    script.params_20[1].set(Math.round(model.coord2_14.coord.transfer.x));
    script.params_20[2].set(Math.round(model.coord2_14.coord.transfer.y));
    script.params_20[3].set(Math.round(model.coord2_14.coord.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set a submap object's rotation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The new X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The new Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The new Z rotation (PSX degrees)")
  @Method(0x800df314L)
  private FlowControl scriptSetModelRotate(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    model.coord2_14.transforms.rotate.set(MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a submap object's rotation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x800df374L)
  private FlowControl scriptReadModelRotate(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    script.params_20[1].set(MathHelper.radToPsxDeg(model.coord2_14.transforms.rotate.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(model.coord2_14.transforms.rotate.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(model.coord2_14.transforms.rotate.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Rotates this submap object to face a point in 3D space")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position to face")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position to face")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position to face")
  @Method(0x800df3d0L)
  private FlowControl scriptSelfFacePoint(final RunningScript<?> script) {
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptFacePoint(script);
  }

  @ScriptDescription("Set us_128 on a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new value")
  @Method(0x800df410L)
  private FlowControl FUN_800df410(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800e0094(script);
  }

  @ScriptDescription("Something to do with forced movement. Used when Dart is halfway through his jump animation in volcano.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "i_144", description = "Use unknown")
  @Method(0x800df440L)
  private FlowControl FUN_800df440(final RunningScript<?> script) {
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800de668(script);
  }

  @ScriptDescription("Something to do with forced movement. Used when Dart is halfway through his jump animation in volcano.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "i_144", description = "Use unknown")
  @Method(0x800df488L)
  private FlowControl FUN_800df488(final RunningScript<?> script) {
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800de944(script);
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "Return value")
  @Method(0x800df4d0L)
  private FlowControl FUN_800df4d0(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800e00cc(script);
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "Return value")
  @Method(0x800df500L)
  private FlowControl FUN_800df500(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Sets submap object s_172")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new value")
  @Method(0x800df530L)
  private FlowControl FUN_800df530(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800e0184(script);
  }

  @ScriptDescription("Something related to submap object animated textures")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800df560L)
  private FlowControl FUN_800df560(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "Unknown")
  @Method(0x800df590L)
  private FlowControl FUN_800df590(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Enable this submap object animated texture")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800df5c0L)
  private FlowControl scriptSelfEnableTextureAnimation(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptEnableTextureAnimation(script);
  }

  @ScriptDescription("Disable this submap object animated texture")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800df5f0L)
  private FlowControl scriptSelfDisableTextureAnimation(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptDisableTextureAnimation(script);
  }

  @ScriptDescription("Get a submap object's us_170")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The return value")
  @Method(0x800df620L)
  private FlowControl FUN_800df620(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800e02c0(script);
  }

  @ScriptDescription("Attaches the camera to this submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "attach", description = "True to attach, false to detach")
  @Method(0x800df650L)
  private FlowControl scriptSelfAttachCameraToSobj(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptAttachCameraToSobj(script);
  }

  @ScriptDescription("Checks to see if the camera is attached to this submap object")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "attached", description = "True if attached, false otherwise")
  @Method(0x800df680L)
  private FlowControl scriptSelfIsCameraAttached(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)script.scriptState_04.innerStruct_00;
    script.params_20[0].set(sobj.cameraAttached_178 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the camera position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The new camera X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The new camera Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The new camera Z position")
  @Method(0x800df6a4L)
  private FlowControl scriptSetCameraPos(final RunningScript<?> script) {
    GTE.setTransforms(worldToScreenMatrix_800c3548);
    this.setCameraPos(new Vector3f().set(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get())); // Retail bugfix - these were all 0

    //LAB_800df744
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      final SubmapObject210 sobj = this.sobjs_800c6880[i].innerStruct_00;
      sobj.cameraAttached_178 = false;
    }

    //LAB_800df774
    return FlowControl.CONTINUE;
  }

  /**
   * The (x, y, z) value is the full amount to rotate, i.e. it rotates by `(x, y, z) / frames` units per frame
   *
   * Used for the little mouse thing running around in the Limestone Cave
   */
  @ScriptDescription("Rotates a submap object by a relative amount over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The relative X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The relative Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The relative Z rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames before the rotation completes")
  @Method(0x800df788L)
  private FlowControl scriptRotateSobj(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final int frames = script.params_20[4].get();
    sobj.rotationFrames_188 = frames;

    // Added this to fix a /0 error in the retail code
    if(frames == 0) {
      sobj.rotationAmount_17c.zero();
      return FlowControl.CONTINUE;
    }

    sobj.rotationAmount_17c.set(
      MathHelper.psxDegToRad(script.params_20[1].get()) / frames,
      MathHelper.psxDegToRad(script.params_20[2].get()) / frames,
      MathHelper.psxDegToRad(script.params_20[3].get()) / frames
    );

    //LAB_800df888
    return FlowControl.CONTINUE;
  }

  /**
   * The (x, y, z) value is the amount to rotate per frame
   */
  @ScriptDescription("Rotates a submap object to an absolute rotation over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The absolute X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The absolute Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The absolute Z rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames before the rotation completes")
  @Method(0x800df890L)
  private FlowControl scriptRotateSobjAbsolute(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.rotationAmount_17c.set(
      MathHelper.psxDegToRad(script.params_20[1].get()),
      MathHelper.psxDegToRad(script.params_20[2].get()),
      MathHelper.psxDegToRad(script.params_20[3].get())
    );
    sobj.rotationFrames_188 = script.params_20[4].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something to do with forced movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "i_144", description = "Use unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ui_18c", description = "Use unknown")
  @Method(0x800df904L)
  private FlowControl FUN_800df904(final RunningScript<?> script) {
    script.params_20[5] = script.params_20[4];
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800deba0(script);
  }

  @ScriptDescription("Rotates this submap object to face the player")
  @Method(0x800df954L)
  private FlowControl scriptFacePlayer(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)script.scriptState_04.innerStruct_00;
    sobj.model_00.coord2_14.transforms.rotate.y = MathHelper.positiveAtan2(this.caches_800c68e8.playerPos_00.z, this.caches_800c68e8.playerPos_00.x);
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, may get submap object's position in screen space")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x0", description = "Screen X (head?)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y0", description = "Screen Y (head?)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x1", description = "Screen X (feet?)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y1", description = "Screen Y (feet?)")
  @Method(0x800df9a8L)
  private FlowControl FUN_800df9a8(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final MV ls = new MV();
    final MV lw = new MV();
    GsGetLws(sobj.model_00.coord2_14, lw, ls);

    GTE.setTransforms(ls);
    GTE.perspectiveTransform(0, 0, 0);
    script.params_20[1].set(Math.round(GTE.getScreenX(2) + 192));
    script.params_20[2].set(Math.round(GTE.getScreenY(2) + 128));

    GTE.perspectiveTransform(0, -130, 0);
    script.params_20[3].set(Math.round(GTE.getScreenX(2) + 192));
    script.params_20[4].set(Math.round(GTE.getScreenY(2) + 128));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the ID of the current submap")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "submapId", description = "The current submap id")
  @Method(0x800dfb28L)
  private FlowControl scriptGetCurrentSubmapId(final RunningScript<?> script) {
    script.params_20[0].set(submapId_800bd808);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the number of model parts in this submap object model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "numberOfParts", description = "The number of model parts")
  @Method(0x800dfb44L)
  private FlowControl scriptSelfGetSobjNobj(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptGetSobjNobj(script);
  }

  @ScriptDescription("Hide a model part in this submap object model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @Method(0x800dfb74L)
  private FlowControl scriptSelfHideModelPart(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptHideModelPart(script);
  }

  @ScriptDescription("Show a model part in this submap object model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @Method(0x800dfba4L)
  private FlowControl scriptSelfShowModelPart(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptShowModelPart(script);
  }

  @ScriptDescription("Make this submap object face the camera")
  @Method(0x800dfbd4L)
  private FlowControl scriptSelfFaceCamera(final RunningScript<?> script) {
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptFaceCamera(script);
  }

  @ScriptDescription("Scale a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The new X scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The new Y scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The new Z scale (12-bit fixed-point)")
  @Method(0x800dfc00L)
  private FlowControl scriptScaleXyz(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.coord2_14.transforms.scale.set(
      script.params_20[1].get() / (float)0x1000,
      script.params_20[2].get() / (float)0x1000,
      script.params_20[3].get() / (float)0x1000
    );

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Scale a submap object uniformly")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scale", description = "The new XYZ scale (12-bit fixed-point)")
  @Method(0x800dfc60L)
  private FlowControl scriptScaleUniform(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.coord2_14.transforms.scale.x = script.params_20[1].get() / (float)0x1000;
    model.coord2_14.transforms.scale.y = script.params_20[1].get() / (float)0x1000;
    model.coord2_14.transforms.scale.z = script.params_20[1].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set a submap object's Z offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "offset", description = "The new Z offset")
  @Method(0x800dfca0L)
  private FlowControl scriptSetModelZOffset(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.zOffset_a0 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set this submap object's flag")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bitIndex", description = "The flag to set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "value", description = "The flag value")
  @Method(0x800dfcd8L)
  private FlowControl scriptSelfSetSobjFlag(final RunningScript<?> script) {
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptSetSobjFlag(script);
  }

  @ScriptDescription("Get this submap object's flag")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bitIndex", description = "The flag to get")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "value", description = "The flag value")
  @Method(0x800dfd10L)
  private FlowControl FUN_800dfd10(final RunningScript<?> script) {
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.scriptGetSobjFlag(script);
  }

  @ScriptDescription("Unknown, related to triangle indicators")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800dfd48L)
  private FlowControl FUN_800dfd48(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Show the triangle indicator for this submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y offset for the indicator")
  @Method(0x800dfd8cL)
  private FlowControl scriptShowAlertIndicator(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.showAlertIndicator_194 = true;
    sobj.alertIndicatorOffsetY_198 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Hide the triangle indicator for this submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800dfdd8L)
  private FlowControl scriptHideAlertIndicator(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.showAlertIndicator_194 = false;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Load a model/animation into a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "objectIndex", description = "The map object index (0-n)")
  @Method(0x800dfe0cL)
  private FlowControl scriptLoadSobjModelAndAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final int index = script.params_20[1].get();

    sobj.sobjIndex_12e = index;
    model.vramSlot_9d = this.submapObjectFlags_800c6a50.getInt(index);

    this.loadModelAndAnimation(model, this.submapAssets.objects.get(index).model, this.submapAssets.objects.get(index).animations.get(0));

    for(final ModelPart10 part : model.modelParts_00) {
      part.obj = TmdObjLoader.fromObjTable("SobjModel (index " + index + ')', part.tmd_08);
    }

    sobj.us_12c = 0;
    sobj.rotationFrames_188 = 0;

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Load a new animation into a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animIndex", description = "The anim index")
  @Method(0x800dfec8L)
  private FlowControl scriptLoadSobjAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    sobj.animIndex_132 = script.params_20[1].get();
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;

    loadModelStandardAnimation(model, this.submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));

    sobj.us_12c = 0;
    sobj.flags_190 &= 0x9fff_ffff;

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a submap object's animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "animIndex", description = "The anim index")
  @Method(0x800dff68L)
  private FlowControl scriptGetSobjAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.animIndex_132);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set us_12a on a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new value")
  @Method(0x800dffa4L)
  private FlowControl FUN_800dffa4(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.us_12a = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get us_12a from a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The value")
  @Method(0x800dffdcL)
  private FlowControl FUN_800dffdc(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.us_12c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Rotates a submap object to face a point in 3D space")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position to face")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position to face (unused)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position to face")
  @Method(0x800e0018L)
  private FlowControl scriptFacePoint(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final float deltaX = script.params_20[1].get() - model.coord2_14.coord.transfer.x;
    final float deltaZ = script.params_20[3].get() - model.coord2_14.coord.transfer.z;

    if(deltaX != 0.0f || deltaZ != 0.0f) {
      model.coord2_14.transforms.rotate.y = MathHelper.positiveAtan2(deltaZ, deltaX);
    }

    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set us_128 on a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new value")
  @Method(0x800e0094L)
  private FlowControl FUN_800e0094(final RunningScript<?> a0) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[a0.params_20[0].get()].innerStruct_00;
    sobj.s_128 = a0.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "Return value")
  @Method(0x800e00ccL)
  private FlowControl FUN_800e00cc(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    final int v0 = this.FUN_800e9018(model.coord2_14.coord.transfer.x, model.coord2_14.coord.transfer.y, model.coord2_14.coord.transfer.z, 0);
    script.params_20[1].set(v0);
    sobj.ui_16c = v0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns submap object s_172")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "Return value")
  @Method(0x800e0148L)
  private FlowControl FUN_800e0148(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.s_172);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets submap object s_172")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The new value")
  @Method(0x800e0184L)
  private FlowControl FUN_800e0184(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.s_172 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to texture animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800e01bcL)
  private FlowControl FUN_800e01bc(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    this.FUN_800de138(sobj.model_00, script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Something related to texture animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800e0204L)
  private FlowControl FUN_800e0204(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.smallerStructPtr_a4.uba_04[script.params_20[1].get()] = false;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Enable a submap object animated texture")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800e0244L)
  private FlowControl scriptEnableTextureAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.animateTextures_ec[script.params_20[1].get()] = true;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Disable a submap object animated texture")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animatedTextureIndex", description = "The animated texture index")
  @Method(0x800e0284L)
  private FlowControl scriptDisableTextureAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.animateTextures_ec[script.params_20[1].get()] = false;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a submap object's us_170")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The return value")
  @Method(0x800e02c0L)
  private FlowControl FUN_800e02c0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.us_170);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Attaches the camera to a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "attach", description = "True to attach, false to detach")
  @Method(0x800e02fcL)
  private FlowControl scriptAttachCameraToSobj(final RunningScript<?> script) {
    final SubmapObject210 struct1 = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    struct1.cameraAttached_178 = script.params_20[1].get() != 0;

    if(script.params_20[1].get() != 0) {
      //LAB_800e035c
      for(int i = 0; i < this.sobjCount_800c6730; i++) {
        final SubmapObject210 struct2 = this.sobjs_800c6880[i].innerStruct_00;

        if(struct2.sobjIndex_130 != struct1.sobjIndex_130) {
          struct2.cameraAttached_178 = false;
        }

        //LAB_800e0390
      }
    }

    //LAB_800e03a0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the number of model parts in a submap object model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "numberOfParts", description = "The number of model parts")
  @Method(0x800e03a8L)
  private FlowControl scriptGetSobjNobj(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.model_00.modelParts_00.length);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Hide a model part in a submap object model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @Method(0x800e03e4L)
  private FlowControl scriptHideModelPart(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final int shift = script.params_20[1].get();

    //LAB_800e0430
    model.partInvisible_f4 |= 0x1L << shift;

    //LAB_800e0440
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Show a model part in a submap object model")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @Method(0x800e0448L)
  private FlowControl scriptShowModelPart(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final int shift = script.params_20[1].get();

    //LAB_800e0498
    model.partInvisible_f4 &= ~(0x1L << shift);

    //LAB_800e04ac
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Make a submap object face the camera")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800e04b4L)
  private FlowControl scriptFaceCamera(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.coord2_14.transforms.rotate.y = MathHelper.positiveAtan2(this.cameraPos_800c6aa0.z, this.cameraPos_800c6aa0.x);
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set a submap object's flag")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bitIndex", description = "The flag to set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "value", description = "The flag value")
  @Method(0x800e0520L)
  private FlowControl scriptSetSobjFlag(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    sobj.flags_190 = sobj.flags_190
      & ~(0x1 << script.params_20[1].get())
      | (script.params_20[2].get() & 0x1) << script.params_20[1].get();

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get a submap object's flag")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bitIndex", description = "The flag to get")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "value", description = "The flag value")
  @Method(0x800e057cL)
  private FlowControl scriptGetSobjFlag(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value")
  @Method(0x800e05c8L)
  private FlowControl FUN_800e05c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index")
  @Method(0x800e05f0L)
  private FlowControl FUN_800e05f0(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Enable flag lighting for a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green colour")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue colour")
  @Method(0x800e0614L)
  private FlowControl scriptEnableSobjFlatLight(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.flatLightingEnabled_1c4 = true;
    sobj.flatLightRed_1c5 = script.params_20[1].get() & 0xff;
    sobj.flatLightGreen_1c6 = script.params_20[2].get() & 0xff;
    sobj.flatLightBlue_1c7 = script.params_20[3].get() & 0xff;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Disable flag lighting for a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800e0684L)
  private FlowControl scriptDisableSobjFlatLight(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.flatLightingEnabled_1c4 = false;
    sobj.flatLightRed_1c5 = 0x80;
    sobj.flatLightGreen_1c6 = 0x80;
    sobj.flatLightBlue_1c7 = 0x80;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads an interpolated animation into a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animIndex", description = "The animation index")
  @Method(0x800e074cL)
  private FlowControl loadInterpolatedSobjAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = SCRIPTS.getObject(script.params_20[0].get(), SubmapObject210.class);
    final Model124 model = sobj.model_00;

    sobj.animIndex_132 = script.params_20[1].get();
    model.ub_a3 = 1;
    model.disableInterpolation_a2 = false;
    loadModelStandardAnimation(model, this.submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));
    sobj.us_12c = 0;
    sobj.flags_190 &= 0x9fff_ffff;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads an uninterpolated animation into a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "animIndex", description = "The animation index")
  @Method(0x800e07f0L)
  private FlowControl loadUninterpolatedSobjAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = SCRIPTS.getObject(script.params_20[0].get(), SubmapObject210.class);
    final Model124 model = sobj.model_00;

    sobj.animIndex_132 = script.params_20[1].get();
    model.ub_a3 = 0;
    model.disableInterpolation_a2 = true;
    loadModelStandardAnimation(model, this.submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));
    sobj.us_12c = 0;
    sobj.flags_190 &= 0x9fff_ffff;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets collision metrics for a submap object (collision type unknown)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "horizontalSize", description = "The horizontal collision size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "verticalSize", description = "The vertical collision size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "reach", description = "The collision reach")
  @Method(0x800e0894L)
  private FlowControl scriptSetSobjCollision2(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.collisionSizeHorizontal_1ac = script.params_20[1].get();
    sobj.collisionSizeVertical_1b0 = script.params_20[2].get();
    sobj.collisionReach_1b4 = script.params_20[3].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a submap object is collided (collision type unknown)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "collided", description = "True if the submap object is collided")
  @Method(0x800e08f4L)
  private FlowControl scriptGetSobjCollidedWith2(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.collidedWithSobjIndex_1a8);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the ambient colour of a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @Method(0x800e0930L)
  private FlowControl scriptSetAmbientColour(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.ambientColourEnabled_1c8 = true;
    sobj.ambientColour_1ca.set(
      (script.params_20[1].get() & 0xffff) / 4096.0f,
      (script.params_20[2].get() & 0xffff) / 4096.0f,
      (script.params_20[3].get() & 0xffff) / 4096.0f
    );
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Disables the ambient colour of a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800e09a0L)
  private FlowControl scriptResetAmbientColour(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.ambientColour_1ca.set(0.5f, 0.5f, 0.5f);
    sobj.ambientColourEnabled_1c8 = false;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Enable shadows for a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800e09e0L)
  private FlowControl scriptEnableShadow(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.shadowType_cc = 1;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Disables shadows for a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800e0a14L)
  private FlowControl scriptDisableShadow(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.shadowType_cc = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow size of a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X size of the shadow")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z size of the shadow")
  @Method(0x800e0a48L)
  private FlowControl scriptSetShadowSize(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.shadowSize_10c.x = script.params_20[1].get() / (float)0x1000;
    model.shadowSize_10c.z = script.params_20[2].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the shadow offset of a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X offset of the shadow")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y offset of the shadow")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z offset of the shadow")
  @Method(0x800e0a94L)
  private FlowControl scriptSetShadowOffset(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.shadowOffset_118.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the movement vector of the player submap object")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X movement")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y movement")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z movement")
  @Method(0x800e0af4L)
  private FlowControl scriptGetPlayerMovement(final RunningScript<?> script) {
    script.params_20[0].set(Math.round(this.caches_800c68e8.playerMovement_0c.x));
    script.params_20[1].set(Math.round(this.caches_800c68e8.playerMovement_0c.y));
    script.params_20[2].set(Math.round(this.caches_800c68e8.playerMovement_0c.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Swaps between the normal and darker shadow textures")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = normal, 1 = darker")
  @Method(0x800e0b34L)
  private FlowControl scriptSwapShadowTexture(final RunningScript<?> script) {
    if(script.params_20[0].get() == 0) {
      new Tim(Unpacker.loadFile("shadow.tim")).uploadToGpu();
    }

    //LAB_800e0b68
    if(script.params_20[0].get() == 1) {
      new Tim(Unpacker.loadFile("SUBMAP/darker_shadow.tim")).uploadToGpu();
    }

    //LAB_800e0b8c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets collision metrics for a submap object (collision type unknown)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "horizontalSize", description = "The horizontal collision size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "verticalSize", description = "The vertical collision size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "reach", description = "The collision reach")
  @Method(0x800e0ba0L)
  private FlowControl scriptSetSobjPlayerCollisionMetrics(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.playerCollisionSizeHorizontal_1b8 = script.params_20[1].get();
    sobj.playerCollisionSizeVertical_1bc = script.params_20[2].get();
    sobj.playerCollisionReach_1c0 = script.params_20[3].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Starts loading a chapter title card")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "titleNum", description = "The title card index")
  @Method(0x800e0c00L)
  private FlowControl scriptLoadChapterTitleCard(final RunningScript<?> script) {
    this.chapterTitleCardLoaded_800c68e0 = false;
    this.chapterTitleNum_800c6738 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if the chapter title card has finished loading")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "loaded", description = "True if loaded, false otherwise")
  @Method(0x800e0c24L)
  private FlowControl scriptIsChapterTitleCardLoaded(final RunningScript<?> script) {
    script.params_20[0].set(this.chapterTitleCardLoaded_800c68e0 ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets origin XY of chapter title card and flags it to move to the rendering state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "originX", description = "X origin position of chapter title")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "originY", description = "Y origin position of chapter title")
  @Method(0x800e0c40L)
  private FlowControl scriptSetChapterTitleCardReadyToRender(final RunningScript<?> script) {
    this.chapterTitleNum_800c6738 |= 0x80;
    this.chapterTitleAnimationComplete_800c686e = false;
    this.chapterTitleOriginX_800c687c = script.params_20[0].get();
    this.chapterTitleOriginY_800c687e = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets completion status of chapter title card animation")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "complete", description = "0 = not complete, 1 = complete")
  @Method(0x800e0c80L)
  private FlowControl scriptGetChapterTitleCardAnimationComplete(final RunningScript<?> script) {
    script.params_20[0].set(this.chapterTitleAnimationComplete_800c686e ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Set the number of ticks to hold on the title card before fading it out")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks")
  @Method(0x800e0c9cL)
  private FlowControl scriptSetTitleCardAnimationPauseTicks(final RunningScript<?> script) {
    this.chapterTitleAnimationPauseTicksRemaining_800c673c = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800e0cb8L)
  private FlowControl FUN_800e0cb8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800e0d18L)
  private void loadModelAndAnimation(final Model124 model, final CContainer cContainer, final TmdAnimationFile tmdAnimFile) {
    final float transferX = model.coord2_14.coord.transfer.x;
    final float transferY = model.coord2_14.coord.transfer.y;
    final float transferZ = model.coord2_14.coord.transfer.z;

    //LAB_800e0d5c
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    final int count = cContainer.tmdPtr_00.tmd.header.nobj;
    model.modelParts_00 = new ModelPart10[count];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    if(cContainer.ext_04 != null) {
      final SmallerStruct smallerStruct = new SmallerStruct();
      model.smallerStructPtr_a4 = smallerStruct;
      smallerStruct.tmdExt_00 = cContainer.ext_04;

      //LAB_800e0e28
      for(int i = 0; i < 4; i++) {
        smallerStruct.tmdSubExtensionArr_20[i] = smallerStruct.tmdExt_00.tmdSubExtensionArr_00[i];
        this.FUN_800de138(model, i);
      }
    } else {
      //LAB_800e0e70
      model.smallerStructPtr_a4 = null;
    }

    //LAB_800e0e74
    model.tpage_108 = (int)((cContainer.tmdPtr_00.id & 0xffff_0000L) >> 11);

    if(cContainer.ptr_08 != null) {
      model.ptr_a8 = cContainer.ptr_08;

      //LAB_800e0eac
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = model.ptr_a8._00[i];
        FUN_8002246c(model, i);
      }
    } else {
      //LAB_800e0ef0
      model.ptr_a8 = null;

      //LAB_800e0f00
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = null;
      }
    }

    //LAB_800e0f10
    initObjTable2(model.modelParts_00);
    GsInitCoordinate2(null, model.coord2_14);
    prepareObjTable2(model.modelParts_00, cContainer.tmdPtr_00.tmd, model.coord2_14);

    model.zOffset_a0 = 0;
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;

    loadModelStandardAnimation(model, tmdAnimFile);

    model.coord2_14.coord.transfer.set(transferX, transferY, transferZ);
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
  }

  @Method(0x800e0ff0L)
  private void submapObjectTicker(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    if(sobj.cameraAttached_178) {
      GTE.setTransforms(worldToScreenMatrix_800c3548);
      this.setCameraPos(model.coord2_14.coord.transfer);
    }

    if(sobj.s_128 == 0) {
      if(sobj.rotationFrames_188 != 0) {
        sobj.rotationFrames_188--;
        model.coord2_14.transforms.rotate.add(sobj.rotationAmount_17c);
      }

      if(sobj.sobjIndex_12e == 0) {
        this.FUN_800217a4(model);
      } else {
        applyModelRotationAndScale(model);
      }

      if(sobj.us_12a == 0) {
        this.animateSmapModel(model);
        if(sobj.us_12c == 1 && (sobj.flags_190 & 0x2000_0000) != 0) {
          sobj.animIndex_132 = 0;
          loadModelStandardAnimation(model, this.submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));
          sobj.us_12c = 0;
          sobj.flags_190 &= 0x9fff_ffff;
        }
      }
    }

    if(model.remainingFrames_9e == 0) {
      sobj.us_12c = 1;

      if((sobj.flags_190 & 0x4000_0000) != 0) {
        sobj.us_12a = 1;
      }
    } else {
      sobj.us_12c = 0;
    }

    if(sobj.showAlertIndicator_194) {
      this.renderAlertIndicator(sobj.model_00, sobj.alertIndicatorOffsetY_198);
    }

    if((sobj.flags_190 & 0x800_0000) != 0) {
      this.FUN_800e4378(sobj, 0x1000_0000L);
    }

    if((sobj.flags_190 & 0x200_0000) != 0) {
      this.FUN_800e4378(sobj, 0x400_0000L);
    }

    if((sobj.flags_190 & 0x80_0000) != 0) {
      this.FUN_800e450c(sobj, 0x100_0000L);
    }

    if((sobj.flags_190 & 0x20_0000) != 0) {
      this.FUN_800e450c(sobj, 0x40_0000L);
    }

    if(enableCollisionDebug) {
      this.renderCollisionDebug(state, sobj);
    }
  }

  public static boolean enableCollisionDebug;

  private void renderCollisionDebug(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    GTE.setTransforms(worldToScreenMatrix_800c3548);

    final Vector2f v0 = new Vector2f();
    final Vector2f v1 = new Vector2f();

    if((sobj.flags_190 & 0x200_0000) != 0 || (sobj.flags_190 & 0x800_0000) != 0) {
      this.transformCollisionVertices(model, sobj.collisionSizeHorizontal_1a0, 0, v0, v1);
      this.queueCollisionRectPacket(v0, v1, 0x800000);
    }

    if((sobj.flags_190 & 0x20_0000) != 0 || (sobj.flags_190 & 0x80_0000) != 0) {
      this.transformCollisionVertices(model, sobj.collisionSizeHorizontal_1ac, sobj.collisionReach_1b4, v0, v1);
      this.queueCollisionRectPacket(v0, v1, 0x8000);
    }

    if(this.sobjs_800c6880[0] == state) {
      this.transformCollisionVertices(model, sobj.playerCollisionSizeHorizontal_1b8, sobj.playerCollisionReach_1c0, v0, v1);
      this.queueCollisionRectPacket(v0, v1, 0x80);
    }

    if(sobj.us_170 != 0) {
      this.transformVertex(v0, sobj.model_00.coord2_14.coord.transfer);
      this.transformVertex(v1, sobj.movementDestination_138);
      this.queueMovementLinePacket(v0, v1, 0x800080);
    }
  }

  private void transformCollisionVertices(final Model124 model, final int size, final int reach, final Vector2f v0, final Vector2f v1) {
    final float reachX;
    final float reachZ;
    if(reach != 0) {
      reachX = MathHelper.sin(model.coord2_14.transforms.rotate.y) * -reach;
      reachZ = MathHelper.cos(model.coord2_14.transforms.rotate.y) * -reach;
    } else {
      reachX = 0.0f;
      reachZ = 0.0f;
    }

    final Vector3f coord = new Vector3f().set(model.coord2_14.coord.transfer).add(reachX, 0.0f, reachZ);
    this.transformVertex(v0, coord.sub(size / 2.0f, 0.0f, size / 2.0f));
    this.transformVertex(v1, coord.add(size, 0.0f, size));
  }

  private void queueCollisionRectPacket(final Vector2f v0, final Vector2f v1, final int colour) {
    GPU.queueCommand(37, new GpuCommandPoly(4)
      .translucent(Translucency.B_PLUS_F)
      .rgb(colour)
      .pos(0, v0.x, v0.y)
      .pos(1, v1.x, v0.y)
      .pos(2, v0.x, v1.y)
      .pos(3, v1.x, v1.y)
    );
  }

  private void queueMovementLinePacket(final Vector2f v0, final Vector2f v1, final int colour) {
    GPU.queueCommand(37, new GpuCommandLine()
      .translucent(Translucency.B_PLUS_F)
      .rgb(colour)
      .pos(0, v0.x, v0.y)
      .pos(1, v1.x, v1.y)
    );
  }

  @Method(0x800e123cL)
  private void submapObjectRenderer(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    if(sobj.s_128 == 0) {
      if(sobj.flatLightingEnabled_1c4) {
        final GsF_LIGHT light = new GsF_LIGHT();

        light.direction_00.set(0.0f, 1.0f, 0.0f);
        light.r_0c = sobj.flatLightRed_1c5 / (float)0x100;
        light.g_0d = sobj.flatLightGreen_1c6 / (float)0x100;
        light.b_0e = sobj.flatLightBlue_1c7 / (float)0x100;
        GsSetFlatLight(0, light);

        light.direction_00.set(1.0f, 0.0f, 0.0f);
        light.r_0c = sobj.flatLightRed_1c5 / (float)0x100;
        light.g_0d = sobj.flatLightGreen_1c6 / (float)0x100;
        light.b_0e = sobj.flatLightBlue_1c7 / (float)0x100;
        GsSetFlatLight(1, light);

        light.direction_00.set(0.0f, 0.0f, 1.0f);
        light.r_0c = sobj.flatLightRed_1c5 / (float)0x100;
        light.g_0d = sobj.flatLightGreen_1c6 / (float)0x100;
        light.b_0e = sobj.flatLightBlue_1c7 / (float)0x100;
        GsSetFlatLight(2, light);
      }

      //LAB_800e1310
      if(sobj.ambientColourEnabled_1c8) {
        GTE.setBackgroundColour(sobj.ambientColour_1ca.x, sobj.ambientColour_1ca.y, sobj.ambientColour_1ca.z);
      }

      //LAB_800e1334
      this.renderSmapModel(sobj.model_00);

      if(sobj.flatLightingEnabled_1c4) {
        GsSetFlatLight(0, this.GsF_LIGHT_0_800c66d8);
        GsSetFlatLight(1, this.GsF_LIGHT_1_800c66e8);
        GsSetFlatLight(2, this.GsF_LIGHT_2_800c66f8);
      }

      //LAB_800e1374
      if(sobj.ambientColourEnabled_1c8) {
        GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
      }

      //LAB_800e1390
      this.FUN_800ef0f8(sobj.model_00, sobj._1d0);
    }

    //LAB_800e139c
  }

  @Method(0x800e13b0L)
  private void executeSubmapMediaLoadingStage(final int index) {
    switch(this.mediaLoadingStage_800c68e4) {
      case LOAD_SHADOW_AND_RESET_LIGHTING_0 -> {
        new Tim(Unpacker.loadFile("shadow.tim")).uploadToGpu();

        if(_80050274 != submapCut_80052c30) {
          _800bda08 = _80050274;
          _80050274 = submapCut_80052c30;
        }

        //LAB_800e1440
        this.GsF_LIGHT_0_800c66d8.direction_00.set(0.0f, 1.0f, 0.0f);
        this.GsF_LIGHT_0_800c66d8.r_0c = 0.5f;
        this.GsF_LIGHT_0_800c66d8.g_0d = 0.5f;
        this.GsF_LIGHT_0_800c66d8.b_0e = 0.5f;
        GsSetFlatLight(0, this.GsF_LIGHT_0_800c66d8);
        this.GsF_LIGHT_1_800c66e8.direction_00.set(0.0f, 1.0f, 0.0f);
        this.GsF_LIGHT_1_800c66e8.r_0c = 0.0f;
        this.GsF_LIGHT_1_800c66e8.g_0d = 0.0f;
        this.GsF_LIGHT_1_800c66e8.b_0e = 0.0f;
        GsSetFlatLight(1, this.GsF_LIGHT_1_800c66e8);
        this.GsF_LIGHT_2_800c66f8.direction_00.set(0.0f, 1.0f, 0.0f);
        this.GsF_LIGHT_2_800c66f8.r_0c = 0.0f;
        this.GsF_LIGHT_2_800c66f8.g_0d = 0.0f;
        this.GsF_LIGHT_2_800c66f8.b_0e = 0.0f;
        GsSetFlatLight(2, this.GsF_LIGHT_2_800c66f8);

        GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
        this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_MUSIC_1;
      }

      case LOAD_MUSIC_1 -> {
        if(index == -1) {
          break;
        }

        final int oldSubmapIndex = submapId_800bd808;
        submapId_800bd808 = this.cutToSubmap_800d610c[submapCut_80052c30];

        //LAB_800e15b8
        //LAB_800e15ac
        //LAB_800e15b8
        //LAB_800e15b8
        if(submapId_800bd808 != oldSubmapIndex) { // Reload sounds when changing submap
          stopAndResetSoundsAndSequences();
          unloadSoundFile(4);
          loadSubmapSounds(submapId_800bd808);
        } else {
          //LAB_800e1550
          if(_800bda08 == submapCut_80052c30) {
            //LAB_800e15d0
            this.mediaLoadingStage_800c68e4 = SubmapMediaState.WAIT_FOR_MUSIC_2;
            break;
          }

          //LAB_800e1594
          //LAB_800e1584
        }

        musicLoaded_800bd782 = false;

        final int ret = getSubmapMusicChange();
        if(ret == -1) {
          FUN_8001ae90();

          //LAB_800e15b8
          musicLoaded_800bd782 = true;
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.WAIT_FOR_MUSIC_2;
          break;
        }

        if(ret == -2) {
          startCurrentMusicSequence();

          //LAB_800e15b8
          musicLoaded_800bd782 = true;
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.WAIT_FOR_MUSIC_2;
          break;
        }

        if(ret == -3) {
          //LAB_800e15b8
          musicLoaded_800bd782 = true;
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.WAIT_FOR_MUSIC_2;
          break;
        }

        //LAB_800e15c0
        loadMusicPackage(ret, 0);
        this.mediaLoadingStage_800c68e4 = SubmapMediaState.WAIT_FOR_MUSIC_2;
      }

      case WAIT_FOR_MUSIC_2 -> {
        if(musicLoaded_800bd782 && (getLoadedDrgnFiles() & 0x2) == 0) {
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_EFFECT_TEXTURES_3;
        }
      }

      case LOAD_EFFECT_TEXTURES_3 -> {
        this.submapEffectsState_800f9eac = 0;

        this.loadSubmapEffects();

        if(this.submapEffectsState_800f9eac == 1) {
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_EFFECT_MODELS_4;
        }
      }

      case LOAD_EFFECT_MODELS_4 -> {
        this.loadSubmapEffects();

        if(this.submapEffectsState_800f9eac == 2) {
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_SOBJ_ASSETS_AND_SCRIPTS_5;
        }
      }

      // Load map assets
      case LOAD_SOBJ_ASSETS_AND_SCRIPTS_5 -> {
        assert this.submapAssetsMrg_800c6878 == null : "Submap assets MRG was not empty";
        assert this.submapScriptsMrg_800c68d8 == null : "Submap scripts MRG was not empty";

        this.submapScriptsLoaded_800c68d0 = false;
        this.submapAssetsLoaded_800c6874 = false;

        final IntRef drgnIndex = new IntRef();
        final IntRef fileIndex = new IntRef();

        this.getDrgnFileFromNewRoot(submapCut_80052c30, drgnIndex, fileIndex);

        if(drgnIndex.get() == 1 || drgnIndex.get() == 2 || drgnIndex.get() == 3 || drgnIndex.get() == 4) {
          //LAB_800e1720
          //LAB_800e17c4
          //LAB_800e17d8
          // Submap data (file example: 695)
          loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 1, files -> this.submapAssetsLoadedCallback(files, 0));
          // Submap scripts (file example: 696)
          loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 2, files -> this.submapAssetsLoadedCallback(files, 1));
        }

        this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_SOBJS_6;
      }

      // Wait for map assets to load
      case LOAD_SOBJS_6 -> {
        if(this.submapAssetsLoaded_800c6874 && this.submapScriptsLoaded_800c68d0) {
          final int objCount = this.submapScriptsMrg_800c68d8.size() - 2;

          this.submapAssets = new SubmapAssets();
          this.submapAssets.lastEntry = this.submapScriptsMrg_800c68d8.get(objCount + 1).getBytes();
          this.submapAssets.script = new ScriptFile("Submap controller", this.submapScriptsMrg_800c68d8.get(0).getBytes());

          for(int objIndex = 0; objIndex < objCount; objIndex++) {
            final byte[] scriptData = this.submapScriptsMrg_800c68d8.get(objIndex + 1).getBytes();

            final FileData submapModel = this.submapAssetsMrg_800c6878.get(objIndex * 33);

            final IntRef drgnIndex = new IntRef();
            final IntRef fileIndex = new IntRef();
            this.getDrgnFileFromNewRoot(submapCut_80052c30, drgnIndex, fileIndex);

            final SubmapObject obj = new SubmapObject();
            obj.script = new ScriptFile("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 2, objIndex + 1), scriptData);

            if(submapModel.hasVirtualSize() && submapModel.real()) {
              obj.model = new CContainer("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 1, objIndex * 33), new FileData(submapModel.getBytes()));
            } else {
              obj.model = null;
            }

            for(int animIndex = objIndex * 33 + 1; animIndex < (objIndex + 1) * 33; animIndex++) {
              final FileData data = this.submapAssetsMrg_800c6878.get(animIndex);

              // This is a stupid fix for a stupid retail bug where almost all
              // sobj animations in DRGN24.938 are symlinked to a PXL file
              // GH#292
              if(data.readInt(0) == 0x11) {
                obj.animations.add(null);
                continue;
              }

              obj.animations.add(new TmdAnimationFile(data));
            }

            this.submapAssets.objects.add(obj);
          }

          // Get models that are symlinked
          for(int objIndex = 0; objIndex < objCount; objIndex++) {
            final SubmapObject obj = this.submapAssets.objects.get(objIndex);

            if(obj.model == null) {
              final FileData submapModel = this.submapAssetsMrg_800c6878.get(objIndex * 33);

              obj.model = this.submapAssets.objects.get(submapModel.realFileIndex() / 33).model;
            }
          }

          for(int i = 0; i < 3; i++) {
            this.submapAssets.pxls.add(new Tim(this.submapAssetsMrg_800c6878.get(objCount * 34 + i)));
          }

          this.mediaLoadingStage_800c68e4 = SubmapMediaState.PREPARE_TO_LOAD_SUBMAP_MODEL_7;
        }
      }

      case PREPARE_TO_LOAD_SUBMAP_MODEL_7 -> {
        this._800c6870 = 0;
        this.submapModelLoaded_800c686c = false;
        this.submapType_800c6968 = this.submapTypes_800f5cd4[submapCut_80052c30];
        this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_SUBMAP_MODEL_8;
      }

      case LOAD_SUBMAP_MODEL_8 -> {
        this.submapModelRenderers_800f5ad4[this.submapType_800c6968].run();

        if(this.submapModelLoaded_800c686c) {
          //LAB_800e18a4
          //LAB_800e18a8
          this.mediaLoadingStage_800c68e4 = SubmapMediaState.FINALIZE_SUBMAP_LOADING_9;
        }
      }

      // Load submap objects
      case FINALIZE_SUBMAP_LOADING_9 -> {
        FUN_800218f0();

        // Removed setting of unused sobjCount static
        this.sobjCount_800c6730 = this.submapAssets.objects.size();

        final long s3;
        final long s4;
        final byte[] lastEntry = this.submapAssets.lastEntry;
        if(lastEntry.length != 4) {
          s3 = MathHelper.get(lastEntry, 0, 4); // Second last int before padding
          s4 = MathHelper.get(lastEntry, 4, 4); // Last int before padding
        } else {
          s3 = 0;
          s4 = 0;
        }

        //LAB_800e1914
        this.submapAssets.pxls.get(0).uploadToGpu();
        this.submapAssets.pxls.get(1).uploadToGpu();

        final Tim tim = this.submapAssets.pxls.get(2);
        final Rect4i imageRect = tim.getImageRect();
        imageRect.h = 128;

        GPU.uploadData15(imageRect, tim.getImageData());

        final ScriptState<Void> submapController = SCRIPTS.allocateScriptState(0, "Submap controller", 0, null);
        this.submapControllerState_800c6740 = submapController;
        submapController.loadScriptFile(this.submapAssets.script);

        //LAB_800e1a38
        this.submapObjectFlags_800c6a50.clear();
        for(int i = 0; i < this.sobjCount_800c6730; i++) {
          this.submapObjectFlags_800c6a50.add(i + 0x81);

          if(i + 1 == s3) {
            this.submapObjectFlags_800c6a50.set(i, 0x92);
          }

          if(i + 1 == s4) {
            this.submapObjectFlags_800c6a50.set(i, 0x93);
          }

          //LAB_800e1a80
        }

        //LAB_800e1a8c
        //LAB_800e1abc
        for(int i = 0; i < this.sobjCount_800c6730; i++) {
          //LAB_800e1ae0
          for(int n = i + 1; n < this.sobjCount_800c6730; n++) {
            if(this.submapAssets.objects.get(n).model == this.submapAssets.objects.get(i).model) {
              this.submapObjectFlags_800c6a50.set(n, 0x80);
            }
          }
        }

        //LAB_800e1b20
        //LAB_800e1b54
        for(int i = 0; i < this.sobjCount_800c6730; i++) {
          final SubmapObject obj = this.submapAssets.objects.get(i);

          final String name = "Submap object " + i + " (file " + i * 33 + ')';
          final ScriptState<SubmapObject210> state = SCRIPTS.allocateScriptState(name, new SubmapObject210(name));
          this.sobjs_800c6880[i] = state;
          state.setTicker(this::submapObjectTicker);
          state.setRenderer(this::submapObjectRenderer);
          state.setDestructor(this::scriptDestructor);
          state.loadScriptFile(obj.script);

          final Model124 model = state.innerStruct_00.model_00;
          model.vramSlot_9d = this.submapObjectFlags_800c6a50.getInt(i);

          final CContainer tmd = this.submapAssets.objects.get(i).model;
          final TmdAnimationFile anim = obj.animations.get(0);
          initModel(model, tmd, anim);

          if(i == 0) { // Player
            this.loadModelAndAnimation(this.playerModel_800c6748, tmd, anim);
            this.playerModel_800c6748.coord2_14.coord.transfer.set(0, 0, 0);
            this.playerModel_800c6748.coord2_14.transforms.rotate.zero();
          }

          //LAB_800e1c50
          state.innerStruct_00.s_128 = 0;
          state.innerStruct_00.us_12a = 0;
          state.innerStruct_00.us_12c = 0;
          state.innerStruct_00.sobjIndex_12e = i;
          state.innerStruct_00.sobjIndex_130 = i;
          state.innerStruct_00.animIndex_132 = 0;
          state.innerStruct_00.movementStepY_134 = 0.0f;
          state.innerStruct_00.movementTicks_144 = 0;
          state.innerStruct_00.ui_16c = -1;
          state.innerStruct_00.us_170 = 0;
          state.innerStruct_00.s_172 = 0;
          state.innerStruct_00.rotationFrames_188 = 0;
          state.innerStruct_00.showAlertIndicator_194 = false;
          state.innerStruct_00.collidedWithSobjIndex_19c = -1;
          state.innerStruct_00.collisionSizeHorizontal_1a0 = 20;
          state.innerStruct_00.collisionSizeVertical_1a4 = 20;
          state.innerStruct_00.collidedWithSobjIndex_1a8 = -1;
          state.innerStruct_00.collisionSizeHorizontal_1ac = 20;
          state.innerStruct_00.collisionSizeVertical_1b0 = 20;
          state.innerStruct_00.collisionReach_1b4 = 50;
          state.innerStruct_00.playerCollisionSizeHorizontal_1b8 = 20;
          state.innerStruct_00.playerCollisionSizeVertical_1bc = 20;
          state.innerStruct_00.playerCollisionReach_1c0 = 50;
          state.innerStruct_00.flatLightingEnabled_1c4 = false;
          state.innerStruct_00.flatLightRed_1c5 = 0x80;
          state.innerStruct_00.flatLightGreen_1c6 = 0x80;
          state.innerStruct_00.flatLightBlue_1c7 = 0x80;

          state.innerStruct_00.cameraAttached_178 = i == 0; // Player

          //LAB_800e1ce4
          final SobjPos14 pos = sobjPositions_800bd818[i];
          model.coord2_14.coord.transfer.set(pos.pos_00);
          model.coord2_14.transforms.rotate.set(pos.rot_0c);

          state.innerStruct_00.movementStepAccelerationY_18c = 7;
          state.innerStruct_00.flags_190 = 0;

          if(i == 0) {
            state.innerStruct_00.flags_190 = 0x1;
            this.FUN_800e4d88(index, model);
          }

          //LAB_800e1d60
          this.FUN_800f04ac(state.innerStruct_00._1d0);

          for(final ModelPart10 part : model.modelParts_00) {
            part.obj = TmdObjLoader.fromObjTable("SobjModel (index " + i + ')', part.tmd_08);
          }
        }

        //LAB_800e1d88
        this.chapterTitleState_800c6708 = 0;
        this.chapterTitleAnimationTicksRemaining_800c670a = 0;
        this.chapterTitleDropShadowOffsetX_800c670c = 0;
        this.chapterTitleDropShadowOffsetY_800c670e = 0;
        this.chapterTitleCardMrg_800c6710 = null;
        this.chapterTitleNumberOffsetX_800c6714 = 0;
        this.chapterTitleNumberOffsetY_800c6718 = 0;
        this.chapterTitleNameOffsetX_800c671c = 0;
        this.chapterTitleNameOffsetY_800c6720 = 0;

        this.chapterTitleNum_800c6738 = 0;
        this.chapterTitleAnimationPauseTicksRemaining_800c673c = 60;

        this.chapterTitleAnimationComplete_800c686e = false;
        this.chapterTitleOriginX_800c687c = 0;
        this.chapterTitleOriginY_800c687e = 0;
        this.chapterTitleCardLoaded_800c68e0 = false;
        this.mediaLoadingStage_800c68e4 = SubmapMediaState.DONE;

        this.triangleIndicator_800c69fc = new TriangleIndicator140();

        this.cameraPos_800c6aa0.set(rview2_800bd7e8.viewpoint_00).sub(rview2_800bd7e8.refpoint_0c);

        new Tim(Unpacker.loadFile("SUBMAP/alert.tim")).uploadToGpu();
        this.resetTriangleIndicators();

        //LAB_800e1ecc
        for(int i = 0; i < 32; i++) {
          this.indicatorTickCountArray_800c6970[i] = -1;
        }

        transitioningFromCombatToSubmap_800bd7b8 = false;
      }

      case DONE -> {
        //LAB_800e1f40
        submapFullyLoaded_800bd7b4 = true;

        if(this.chapterTitleNum_800c6738 != 0) {
          this.handleAndRenderChapterTitle();
        }
      }
    }
  }

  /** Handles cutscene movement */
  @Method(0x800e1f90L)
  private boolean FUN_800e1f90(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    if((sobj.flags_190 & 0x8000_0000) != 0) {
      return false;
    }

    //LAB_800e2014
    sobj.movementDistanceMoved12_160.add(sobj.movementStep12_154);

    //LAB_800e20d8
    sobj.movementTicks_144--;

    if(sobj.s_172 == 0) {
      final Vector3f sp0x20 = new Vector3f();

      if((sobj.flags_190 & 0x1) != 0) { // Is player
        final Vector3f sp0x18 = new Vector3f();
        sp0x18.set(sobj.movementStep_148);
        GTE.setTransforms(worldToScreenMatrix_800c3548);
        this.transformToWorldspace(sp0x20, sp0x18);
      } else {
        //LAB_800e2134
        sp0x20.set(sobj.movementStep_148);
      }

      //LAB_800e2140
      final int s3 = this.FUN_800e88a0(sobj.sobjIndex_12e, model.coord2_14.coord.transfer, sp0x20);
      if(s3 >= 0 && this.FUN_800e6798(s3) != 0) {
        model.coord2_14.coord.transfer.x += sp0x20.x;
        model.coord2_14.coord.transfer.y = sp0x20.y;
        model.coord2_14.coord.transfer.z += sp0x20.z;
      }

      //LAB_800e21bc
      sobj.ui_16c = s3;
    } else {
      //LAB_800e21c4
      model.coord2_14.coord.transfer.add(sobj.movementStep_148);
    }

    //LAB_800e21e8
    if(sobj.movementTicks_144 != 0) {
      //LAB_800e21f8
      return false;
    }

    //LAB_800e2200
    sobj.us_170 = 0;

    //LAB_800e2204
    return true;
  }

  @Method(0x800e2220L)
  private void unloadSmap() {
    this.submapControllerState_800c6740.deallocateWithChildren();

    for(final EnvironmentRenderingMetrics24 environmentRenderingMetrics24 : this.envRenderMetrics_800cb710) {
      if(environmentRenderingMetrics24.obj != null) {
        environmentRenderingMetrics24.obj.delete();
        environmentRenderingMetrics24.obj = null;
      }
    }

    if(this.chapterTitleCardMrg_800c6710 != null) {
      this.chapterTitleCardMrg_800c6710 = null;
    }

    //LAB_800e226c
    submapFullyLoaded_800bd7b4 = false;

    //LAB_800e229c
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      final SobjPos14 pos = sobjPositions_800bd818[i];

      final ScriptState<SubmapObject210> sobjState = this.sobjs_800c6880[i];
      if(sobjState != null) {
        final SubmapObject210 sobj = sobjState.innerStruct_00;
        final Model124 model = sobj.model_00;
        pos.pos_00.set(model.coord2_14.coord.transfer);
        pos.rot_0c.set(model.coord2_14.transforms.rotate);
        this.sobjs_800c6880[i].deallocateWithChildren();
      } else {
        //LAB_800e231c
        pos.pos_00.zero();
        pos.rot_0c.zero();
      }
    }

    //LAB_800e2350
    _800bd7b0 = 1;

    this.submapAssets = null;
    this.submapAssetsMrg_800c6878 = null;
    this.submapScriptsMrg_800c68d8 = null;

    scriptDeallocateAllTextboxes(null);

    this._800c6870 = -1;
    this.submapModelRenderers_800f5ad4[this.submapType_800c6968].run();

    this.submapEffectsState_800f9eac = -1;
    this.loadSubmapEffects();
    this.triangleIndicator_800c69fc = null;
    new Tim(Unpacker.loadFile("shadow.tim")).uploadToGpu();
  }

  @Method(0x800e2428L)
  private void cacheSobjScreenBounds(final int sobjIndex) {
    final MV ls = new MV();

    final SubmapCaches80 caches = this.caches_800c68e8;

    GsGetLs(SCRIPTS.getObject(sobjIndex, SubmapObject210.class).model_00.coord2_14, ls);
    GTE.setTransforms(ls);

    GTE.perspectiveTransform(0, 0, 0);
    caches.bottomMiddle_70.set(GTE.getScreenX(2) + 192, GTE.getScreenY(2) + 128);

    GTE.perspectiveTransform(0, -130, 0);
    caches.topMiddle_78.set(GTE.getScreenX(2) + 192, GTE.getScreenY(2) + 128);

    GTE.perspectiveTransform(-20, 0, 0);
    caches.bottomLeft_68.set(GTE.getScreenX(2) + 192, GTE.getScreenY(2) + 128);

    GTE.perspectiveTransform(20, 0, 0);
    caches.bottomRight_60.set(GTE.getScreenX(2) + 192, GTE.getScreenY(2) + 128);
  }

  @Method(0x800e2648L)
  private void handleAndRenderChapterTitle() {
    final int chapterTitleState = this.chapterTitleState_800c6708;

    if(chapterTitleState == 0) {
      //LAB_800e26c0
      final int chapterTitleNum = this.chapterTitleNum_800c6738;

      if(chapterTitleNum == 1) {
        //LAB_800e2700
        //LAB_800e2794
        loadDrgnDir(0, 6670, files -> this.submapAssetsLoadedCallback(files, 0x10));
      } else if(chapterTitleNum == 2) {
        //LAB_800e2728
        //LAB_800e2794
        loadDrgnDir(0, 6671, files -> this.submapAssetsLoadedCallback(files, 0x10));
        //LAB_800e26e8
      } else if(chapterTitleNum == 3) {
        //LAB_800e2750
        //LAB_800e2794
        loadDrgnDir(0, 6672, files -> this.submapAssetsLoadedCallback(files, 0x10));
      } else if(chapterTitleNum == 4) {
        //LAB_800e2778
        //LAB_800e2794
        loadDrgnDir(0, 6673, files -> this.submapAssetsLoadedCallback(files, 0x10));
      }

      //LAB_800e27a4
      this.chapterTitleState_800c6708++;
      return;
    }

    if(chapterTitleState == 1) {
      //LAB_800e27b8
      if(this.chapterTitleCardLoaded_800c68e0 && (this.chapterTitleNum_800c6738 & 0x80) != 0) {
        this.chapterTitleState_800c6708++;
      }

      return;
    }

    if(chapterTitleState == 2) {
      //LAB_800e27e8
      final long currentTick = this.chapterTitleAnimationTicksRemaining_800c670a;

      //LAB_800e284c
      if(currentTick == 0) {
        //LAB_800e2860
        new Tim(this.chapterTitleCardMrg_800c6710.get(5)).uploadToGpu();
        new Tim(this.chapterTitleCardMrg_800c6710.get(13)).uploadToGpu();

        //LAB_800e2980
        this.chapterTitleBrightness_800c6728 = 0;
        this.chapterTitleIsTranslucent_800c6724 = true;
        this.chapterTitleNumberOffsetX_800c6714 = 32;
        this.chapterTitleNumberOffsetY_800c6718 = 16;
        this.chapterTitleNameOffsetX_800c671c = 64;
        this.chapterTitleNameOffsetY_800c6720 = 16;
        this.chapterTitleAnimationTicksRemaining_800c670a++;
      } else if(currentTick == 34) {
        //LAB_800e30c0
        this.chapterTitleDropShadowOffsetX_800c670c++;

        if(this.chapterTitleDropShadowOffsetX_800c670c == 3) {
          this.chapterTitleDropShadowOffsetY_800c670e = 1;
          this.chapterTitleAnimationTicksRemaining_800c670a++;
        }
      } else if(currentTick == 35) {
        //LAB_800e30f8
        this.chapterTitleAnimationPauseTicksRemaining_800c673c--;

        if(this.chapterTitleAnimationPauseTicksRemaining_800c673c == 0) {
          this.chapterTitleAnimationTicksRemaining_800c670a = 201;
        }
      } else if(currentTick == 233) {
        //LAB_800e376c
        this.chapterTitleCardMrg_800c6710 = null;
        this.chapterTitleState_800c6708++;
      } else if(currentTick >= 35) {
        //LAB_800e2828
        if(currentTick < 233) {
          if(currentTick >= 201) {
            //LAB_800e311c
            if(currentTick == 212) {
              new Tim(this.chapterTitleCardMrg_800c6710.get(1)).uploadToGpu();
              new Tim(this.chapterTitleCardMrg_800c6710.get(9)).uploadToGpu();

              //LAB_800e3248
              //LAB_800e3254
            } else if(currentTick == 216) {
              new Tim(this.chapterTitleCardMrg_800c6710.get(2)).uploadToGpu();
              new Tim(this.chapterTitleCardMrg_800c6710.get(10)).uploadToGpu();

              //LAB_800e3384
              //LAB_800e3390
            } else if(currentTick == 220) {
              new Tim(this.chapterTitleCardMrg_800c6710.get(3)).uploadToGpu();
              new Tim(this.chapterTitleCardMrg_800c6710.get(11)).uploadToGpu();

              //LAB_800e34c0
              //LAB_800e34cc
            } else if(currentTick == 224) {
              new Tim(this.chapterTitleCardMrg_800c6710.get(4)).uploadToGpu();
              new Tim(this.chapterTitleCardMrg_800c6710.get(12)).uploadToGpu();

              //LAB_800e35fc
              //LAB_800e3608
            } else if(currentTick == 228) {
              new Tim(this.chapterTitleCardMrg_800c6710.get(5)).uploadToGpu();
              new Tim(this.chapterTitleCardMrg_800c6710.get(13)).uploadToGpu();
            }

            //LAB_800e3744
            this.chapterTitleIsTranslucent_800c6724 = true;
            this.chapterTitleBrightness_800c6728 -= 4;
          }

          //LAB_800e3790
          this.chapterTitleAnimationTicksRemaining_800c670a++;
        }
      } else if(currentTick >= 33) {
        //LAB_800e3070
        this.chapterTitleIsTranslucent_800c6724 = false;
        this.chapterTitleNameOffsetY_800c6720 = 0;
        this.chapterTitleNameOffsetX_800c671c = 0;
        this.chapterTitleNumberOffsetY_800c6718 = 0;
        this.chapterTitleNumberOffsetX_800c6714 = 0;
        this.chapterTitleBrightness_800c6728 = 128;
        this.chapterTitleAnimationTicksRemaining_800c670a++;
        this.chapterTitleDropShadowOffsetX_800c670c = 1;
        this.chapterTitleDropShadowOffsetY_800c670e = 0;
      } else if((int)currentTick > 0) {
        //LAB_800e29d4
        if(currentTick == 4) {
          new Tim(this.chapterTitleCardMrg_800c6710.get(4)).uploadToGpu();
          new Tim(this.chapterTitleCardMrg_800c6710.get(12)).uploadToGpu();

          //LAB_800e2afc
          //LAB_800e2b08
        } else if(currentTick == 8) {
          new Tim(this.chapterTitleCardMrg_800c6710.get(3)).uploadToGpu();
          new Tim(this.chapterTitleCardMrg_800c6710.get(11)).uploadToGpu();

          //LAB_800e2c38
          //LAB_800e2c44
        } else if(currentTick == 12) {
          new Tim(this.chapterTitleCardMrg_800c6710.get(2)).uploadToGpu();
          new Tim(this.chapterTitleCardMrg_800c6710.get(10)).uploadToGpu();

          //LAB_800e2d74
          //LAB_800e2d80
        } else if(currentTick == 16) {
          new Tim(this.chapterTitleCardMrg_800c6710.get(1)).uploadToGpu();
          new Tim(this.chapterTitleCardMrg_800c6710.get(9)).uploadToGpu();

          //LAB_800e2eb0
          //LAB_800e2ebc
        } else if(currentTick == 20) {
          new Tim(this.chapterTitleCardMrg_800c6710.get(0)).uploadToGpu();
          new Tim(this.chapterTitleCardMrg_800c6710.get(8)).uploadToGpu();

          //LAB_800e2fec
        }

        //LAB_800e2ff8
        this.chapterTitleBrightness_800c6728 += 4;
        this.chapterTitleNumberOffsetX_800c6714--;
        this.chapterTitleNameOffsetX_800c671c -= 2;

        // Decrement Y-offset every other tick
        if((this.chapterTitleAnimationTicksRemaining_800c670a & 0x1) == 0) {
          this.chapterTitleNumberOffsetY_800c6718--;
          this.chapterTitleNameOffsetY_800c6720--;
        }

        //LAB_800e3038
        //LAB_800e3064
        this.chapterTitleAnimationTicksRemaining_800c670a++;
      } else {
        //LAB_800e3790
        this.chapterTitleAnimationTicksRemaining_800c670a++;
      }

      //LAB_800e37a0
      int left = this.chapterTitleOriginX_800c687c + this.chapterTitleNumberOffsetX_800c6714 - 58;
      int top = this.chapterTitleOriginY_800c687e + this.chapterTitleNumberOffsetY_800c6718 - 66;
      int right = this.chapterTitleOriginX_800c687c - (this.chapterTitleNumberOffsetX_800c6714 - 34);
      int bottom = this.chapterTitleOriginY_800c687e - (this.chapterTitleNumberOffsetY_800c6718 + 30);

      // Chapter number text
      final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .monochrome(this.chapterTitleBrightness_800c6728)
        .clut(512, 510)
        .vramPos(512, 256)
        .uv(0,  0, 64)
        .uv(1, 91, 64)
        .uv(2,  0, 99)
        .uv(3, 91, 99)
        .pos(0, left, top)
        .pos(1, right, top)
        .pos(2, left, bottom)
        .pos(3, right, bottom);

      if(this.chapterTitleIsTranslucent_800c6724) {
        cmd1.translucent(Translucency.B_PLUS_F);
      }

      GPU.queueCommand(28, cmd1);

      left = this.chapterTitleOriginX_800c687c - (this.chapterTitleNameOffsetX_800c671c + 140);
      top = this.chapterTitleOriginY_800c687e - (this.chapterTitleNameOffsetY_800c6720 + 16);
      right = this.chapterTitleOriginX_800c687c + this.chapterTitleNameOffsetX_800c671c + 116;
      bottom = this.chapterTitleOriginY_800c687e + this.chapterTitleNameOffsetY_800c6720 + 45;

      // Chapter name text
      final GpuCommandPoly cmd2 = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .monochrome(this.chapterTitleBrightness_800c6728)
        .clut(512, 508)
        .vramPos(512, 256)
        .uv(0,   0,  0)
        .uv(1, 255,  0)
        .uv(2,   0, 60)
        .uv(3, 255, 60)
        .pos(0, left, top)
        .pos(1, right, top)
        .pos(2, left, bottom)
        .pos(3, right, bottom);

      if(this.chapterTitleIsTranslucent_800c6724) {
        cmd2.translucent(Translucency.B_PLUS_F);
      }

      GPU.queueCommand(28, cmd2);

      if(this.chapterTitleDropShadowOffsetX_800c670c != 0) {
        left = this.chapterTitleDropShadowOffsetX_800c670c + this.chapterTitleOriginX_800c687c + this.chapterTitleNumberOffsetX_800c6714 - 58;
        top = this.chapterTitleDropShadowOffsetY_800c670e + this.chapterTitleOriginY_800c687e + this.chapterTitleNumberOffsetY_800c6718 - 66;
        right = this.chapterTitleDropShadowOffsetX_800c670c + this.chapterTitleOriginX_800c687c - (this.chapterTitleNumberOffsetX_800c6714 - 34);
        bottom = this.chapterTitleDropShadowOffsetY_800c670e + this.chapterTitleOriginY_800c687e - (this.chapterTitleNumberOffsetY_800c6718 + 30);

        // Chapter number drop shadow
        final GpuCommandPoly cmd3 = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .monochrome(this.chapterTitleBrightness_800c6728)
          .clut(512, 511)
          .vramPos(512, 256)
          .uv(0,  0, 64)
          .uv(1, 91, 64)
          .uv(2,  0, 99)
          .uv(3, 91, 99)
          .pos(0, left, top)
          .pos(1, right, top)
          .pos(2, left, bottom)
          .pos(3, right, bottom);

        if((this.chapterTitleNum_800c6738 & 0xf) - 2 < 3) {
          cmd3.translucent(Translucency.B_MINUS_F);
        }

        //LAB_800e3afc
        if((this.chapterTitleNum_800c6738 & 0xf) == 1) {
          cmd3.translucent(Translucency.B_PLUS_F);
        }

        //LAB_800e3b14
        GPU.queueCommand(28, cmd3);

        left = this.chapterTitleDropShadowOffsetX_800c670c + this.chapterTitleOriginX_800c687c - (this.chapterTitleNameOffsetX_800c671c + 140);
        top = this.chapterTitleDropShadowOffsetY_800c670e + this.chapterTitleOriginY_800c687e - (this.chapterTitleNameOffsetY_800c6720 + 16);
        right = this.chapterTitleDropShadowOffsetX_800c670c + this.chapterTitleOriginX_800c687c + this.chapterTitleNameOffsetX_800c671c + 116;
        bottom = this.chapterTitleDropShadowOffsetY_800c670e + this.chapterTitleOriginY_800c687e + this.chapterTitleNameOffsetY_800c6720 + 45;

        // Chapter name drop shadow
        final GpuCommandPoly cmd4 = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .monochrome(this.chapterTitleBrightness_800c6728)
          .clut(512, 509)
          .vramPos(512, 256)
          .uv(0,   0,  0)
          .uv(1, 255,  0)
          .uv(2,   0, 60)
          .uv(3, 255, 60)
          .pos(0, left, top)
          .pos(1, right, top)
          .pos(2, left, bottom)
          .pos(3, right, bottom);

        if((this.chapterTitleNum_800c6738 & 0xf) - 2 < 3) {
          cmd4.translucent(Translucency.B_MINUS_F);
        }

        //LAB_800e3c20
        if((this.chapterTitleNum_800c6738 & 0xf) == 1) {
          cmd4.translucent(Translucency.B_PLUS_F);
        }

        //LAB_800e3c3c
        GPU.queueCommand(28, cmd4);
      }

      return;
    }

    //LAB_800e26a4
    if(chapterTitleState == 3) {
      //LAB_800e3c60
      this.chapterTitleNum_800c6738 = 0;
      this.chapterTitleOriginY_800c687e = 0;
      this.chapterTitleOriginX_800c687c = 0;
      this.chapterTitleCardLoaded_800c68e0 = false;
      this.chapterTitleDropShadowOffsetX_800c670c = 0;
      this.chapterTitleAnimationTicksRemaining_800c670a = 0;
      this.chapterTitleState_800c6708 = 0;
      this.chapterTitleAnimationComplete_800c686e = true;
    }
  }

  @Method(0x800e3d80L)
  private void submapAssetsLoadedCallback(final List<FileData> files, final int assetType) {
    switch(assetType) {
      // Submap assets
      case 0x0 -> {
        this.submapAssetsLoaded_800c6874 = true;
        this.submapAssetsMrg_800c6878 = files;
      }

      // Submap scripts
      case 0x1 -> {
        this.submapScriptsLoaded_800c68d0 = true;
        this.submapScriptsMrg_800c68d8 = files;
      }

      // Chapter title cards
      case 0x10 -> {
        this.chapterTitleCardLoaded_800c68e0 = true;
        this.chapterTitleCardMrg_800c6710 = files;
      }
    }
  }

  @Method(0x800e3df4L)
  private void scriptDestructor(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    //LAB_800e3e24
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      if(this.sobjs_800c6880[i] == state) {
        this.sobjs_800c6880[i] = null;
      }

      //LAB_800e3e38
    }

    sobj.model_00.deleteModelParts();

    //LAB_800e3e48
  }

  @Method(0x800e3e60L)
  private boolean FUN_800e3e60(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    sobj.us_170 = 0;
    return true;
  }

  /** Used in teleporter just before Melbu */
  @Method(0x800e3e74L)
  private boolean tickSobjMovement(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    model.coord2_14.coord.transfer.y += sobj.movementStepY_134;

    //LAB_800e3ec0
    sobj.movementDistanceMoved12_160.x += sobj.movementStep12_154.x;
    sobj.movementDistanceMoved12_160.z += sobj.movementStep12_154.z;

    //LAB_800e3f3c
    model.coord2_14.coord.transfer.x += sobj.movementStep_148.x;
    model.coord2_14.coord.transfer.z += sobj.movementStep_148.z;
    sobj.movementStepY_134 += sobj.movementStepAccelerationY_18c;
    sobj.movementTicks_144--;
    if(sobj.movementTicks_144 != 0) {
      return false;
    }

    //LAB_800e3f7c
    sobj.us_170 = 0;
    sobj.movementStepY_134 = 0;
    model.coord2_14.coord.transfer.set(sobj.movementDestination_138);
    sobj.s_172 = sobj.s_174;
    return true;
  }

  @Override
  @Method(0x800e3facL)
  public void menuClosed() {
    if(!transitioningFromCombatToSubmap_800bd7b8) {
      this.submapAssets.pxls.get(0).uploadToGpu();
      this.submapAssets.pxls.get(1).uploadToGpu();
    }

    //LAB_800e4008
  }

  @Method(0x800e4018L)
  private void setIndicatorStatusAndResetIndicatorTickCountOnReenable() {
    if(gameState_800babc8.indicatorsDisabled_4e3) {
      if(!this.indicatorDisabledForCutscene_800f64ac) {
        this.indicatorDisabledForCutscene_800f64ac = true;
      }
    } else if(this.indicatorDisabledForCutscene_800f64ac) {
      this.indicatorDisabledForCutscene_800f64ac = false;
      this.indicatorTickCountArray_800c6970[31] = 0;
    }
  }

  /** sobj/sobj collision */
  @Method(0x800e4378L)
  private void FUN_800e4378(final SubmapObject210 sobj, final long a1) {
    final Model124 model = sobj.model_00;

    sobj.collidedWithSobjIndex_19c = -1;

    final float colliderMinY = model.coord2_14.coord.transfer.y - sobj.collisionSizeVertical_1a4;
    final float colliderMaxY = model.coord2_14.coord.transfer.y + sobj.collisionSizeVertical_1a4;

    //LAB_800e43b8
    //LAB_800e43ec
    //LAB_800e43f0
    //LAB_800e4414
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      final SubmapObject210 sobj2 = this.sobjs_800c6880[i].innerStruct_00;
      final Model124 model2 = sobj2.model_00;

      if(sobj2 != sobj && (sobj2.flags_190 & a1) != 0) {
        final float dx = model2.coord2_14.coord.transfer.x - model.coord2_14.coord.transfer.x;
        final float dz = model2.coord2_14.coord.transfer.z - model.coord2_14.coord.transfer.z;
        final int size = sobj.collisionSizeHorizontal_1a0 + sobj2.collisionSizeHorizontal_1a0;
        final float collideeMinY = model2.coord2_14.coord.transfer.y - sobj2.collisionSizeVertical_1a4;
        final float collideeMaxY = model2.coord2_14.coord.transfer.y + sobj2.collisionSizeVertical_1a4;

        //LAB_800e44d0
        //LAB_800e44e0
        if(size * size >= dx * dx + dz * dz && (colliderMaxY >= collideeMinY && colliderMinY <= collideeMinY || colliderMaxY >= collideeMaxY && colliderMinY <= collideeMaxY) && sobj.collidedWithSobjIndex_19c == -1) {
          sobj.collidedWithSobjIndex_19c = i;
        }
      }
    }
  }

  /** sobj/sobj collision */
  @Method(0x800e450cL)
  private void FUN_800e450c(final SubmapObject210 sobj, final long a1) {
    final Model124 model = sobj.model_00;

    sobj.collidedWithSobjIndex_1a8 = -1;

    final int reachX = Math.round(MathHelper.sin(model.coord2_14.transforms.rotate.y) * -sobj.collisionReach_1b4);
    final int reachZ = Math.round(MathHelper.cos(model.coord2_14.transforms.rotate.y) * -sobj.collisionReach_1b4);
    final float colliderMinY = model.coord2_14.coord.transfer.y - sobj.collisionSizeVertical_1b0;
    final float colliderMaxY = model.coord2_14.coord.transfer.y + sobj.collisionSizeVertical_1b0;

    //LAB_800e45d8
    //LAB_800e45dc
    //LAB_800e4600
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      final SubmapObject210 sobj2 = this.sobjs_800c6880[i].innerStruct_00;
      final Model124 model2 = sobj2.model_00;

      if(sobj2 != sobj && (sobj2.flags_190 & a1) != 0) {
        final float dx = model2.coord2_14.coord.transfer.x - (model.coord2_14.coord.transfer.x + reachX);
        final float dz = model2.coord2_14.coord.transfer.z - (model.coord2_14.coord.transfer.z + reachZ);
        final int size = sobj.collisionSizeHorizontal_1ac + sobj2.collisionSizeHorizontal_1ac;

        final float collideeMinY = model2.coord2_14.coord.transfer.y - sobj2.collisionSizeVertical_1b0;
        final float collideeMaxY = model2.coord2_14.coord.transfer.y + sobj2.collisionSizeVertical_1b0;

        //LAB_800e46bc
        //LAB_800e46cc
        if(size * size >= dx * dx + dz * dz && (collideeMinY >= colliderMinY && collideeMinY <= colliderMaxY || collideeMaxY >= colliderMinY && collideeMaxY <= colliderMaxY) && sobj.collidedWithSobjIndex_1a8 == -1) {
          sobj.collidedWithSobjIndex_1a8 = i;
        }
      }

      //LAB_800e46e0
    }

    //LAB_800e46f0
  }

  @Method(0x800e4708L)
  private void renderSubmap() {
    this.FUN_800f047c();
    this.renderSubmapOverlays();
    this.handleAndRenderSubmapEffects();
    applyModelRotationAndScale(this.playerModel_800c6748);
    this.submapModelRenderers_800f5ad4[this.submapType_800c6968].run();
  }

  /**
   * @param parent The model that the indicator is attached to
   * @param y      The Y offset from the model
   */
  @Method(0x800e4774L)
  private void renderAlertIndicator(final Model124 parent, final int y) {
    final MV ls = new MV();
    final MV lw = new MV();
    GsGetLws(parent.coord2_14, lw, ls);
    GTE.setTransforms(ls);
    GTE.perspectiveTransform(0, y - 64, 0);
    final float sx = GTE.getScreenX(2);
    final float sy = GTE.getScreenY(2);

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .bpp(Bpp.BITS_4)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0x80)
      .clut(976, 464)
      .vramPos(960, 256)
      .pos(0, this.alertIndicatorMetrics_800f64b0.x0_00 + sx, this.alertIndicatorMetrics_800f64b0.y0_04 + sy)
      .pos(1, this.alertIndicatorMetrics_800f64b0.x1_02 + sx, this.alertIndicatorMetrics_800f64b0.y0_04 + sy)
      .pos(2, this.alertIndicatorMetrics_800f64b0.x0_00 + sx, this.alertIndicatorMetrics_800f64b0.y1_06 + sy)
      .pos(3, this.alertIndicatorMetrics_800f64b0.x1_02 + sx, this.alertIndicatorMetrics_800f64b0.y1_06 + sy)
      .uv(0, this.alertIndicatorMetrics_800f64b0.u0_08, this.alertIndicatorMetrics_800f64b0.v0_0c)
      .uv(1, this.alertIndicatorMetrics_800f64b0.u1_0a, this.alertIndicatorMetrics_800f64b0.v0_0c)
      .uv(2, this.alertIndicatorMetrics_800f64b0.u0_08, this.alertIndicatorMetrics_800f64b0.v1_0e)
      .uv(3, this.alertIndicatorMetrics_800f64b0.u1_0a, this.alertIndicatorMetrics_800f64b0.v1_0e);

    GPU.queueCommand(37, cmd);
  }

  @Method(0x800e4994L)
  private void noSubmapModel() {
    this.submapModelLoaded_800c686c = true;
  }

  @Method(0x800e49a4L)
  private int randomEncounterIndex() {
    final int rand = rand();

    if(rand < 0x2ccc) {
      return 0;
    }

    if(rand < 0x5999) {
      return 1;
    }

    if(rand < 0x7333) {
      return 2;
    }

    return 3;
  }

  @Method(0x800e49f0L)
  private boolean hasPlayerMoved(final MV mat) {
    //LAB_800e4a44
    final boolean moved = !flEq(this.prevPlayerPos_800c6ab0.x, mat.transfer.x) || !flEq(this.prevPlayerPos_800c6ab0.y, mat.transfer.y) || !flEq(this.prevPlayerPos_800c6ab0.z, mat.transfer.z);

    //LAB_800e4a4c
    final EncounterRateMode mode = CONFIG.getConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get());

    final float dist = mode.modifyDistance(this.prevPlayerPos_800c6ab0.x - mat.transfer.x + (this.prevPlayerPos_800c6ab0.z - mat.transfer.z));

    if(dist < 9.0f) {
      //LAB_800e4a98
      this.encounterMultiplier_800c6abc = mode.walkModifier;
    } else {
      this.encounterMultiplier_800c6abc = mode.runModifier;
    }

    //LAB_800e4aa0
    this.prevPlayerPos_800c6ab0.set(mat.transfer);
    return moved;
  }

  @Method(0x800e4ac8L)
  private void cacheHasNoEncounters() {
    hasNoEncounters_800bed58 = encounterData_800f64c4[submapCut_80052c30].rate_02 == 0;
  }

  @Method(0x800e4b20L)
  private long handleEncounters() {
    if(this._800c6ae0 < 15 || Unpacker.getLoadingFileCount() != 0 || gameState_800babc8.indicatorsDisabled_4e3) {
      return 0;
    }

    this._800c6ae4++;

    if(this._800c6ae4 < 0) {
      return 0;
    }

    // The first condition is to fix what we believe is caused by menus loading too fast in SC. Submaps still take several frames to initialize,
    // and if you spam triangle and escape immediately after the post-combat screen it's possible to get into this method when index_80052c38 is
    // still set to -1. See #304 for more details.
    if(index_80052c38 >= 0 && index_80052c38 < 0x40 && this.collisionAndTransitions_800cb460[index_80052c38] != 0) {
      return 0;
    }

    //LAB_800e4bc0
    if(!this.isScriptLoaded(0)) {
      return 0;
    }

    if(!this.hasPlayerMoved(this.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord)) {
      return 0;
    }

    this.encounterAccumulator_800c6ae8 += Math.round(encounterData_800f64c4[submapCut_80052c30].rate_02 * this.encounterMultiplier_800c6abc);

    if(this.encounterAccumulator_800c6ae8 > 0x1400) {
      // Start combat
      encounterId_800bb0f8 = this.sceneEncounterIds_800f74c4[encounterData_800f64c4[submapCut_80052c30].scene_00][this.randomEncounterIndex()];
      battleStage_800bb0f4 = encounterData_800f64c4[submapCut_80052c30].stage_03;
      if(Config.combatStage()) {
        battleStage_800bb0f4 = Config.getCombatStage();
      }
      return 0x1L;
    }

    //LAB_800e4ce4
    //LAB_800e4ce8
    return 0;
  }

  @Method(0x800e4d00L)
  private void FUN_800e4d00(final int submapCut, final int index) {
    if(!this.FUN_800e5264(this.matrix_800c6ac0, submapCut)) {
      //LAB_800e4d34
      final Vector3f avg = new Vector3f();
      this.get3dAverageOfSomething(index, avg);
      this.matrix_800c6ac0.transfer.set(avg);
      this._800f7e24 = 2;
    } else {
      this._800f7e24 = 1;
    }

    //LAB_800e4d74
  }

  @Method(0x800e4d88L)
  private void FUN_800e4d88(final int index, final Model124 model) {
    if(this._800f7e24 != 0) {
      if(this._800f7e24 == 1) {
        model.coord2_14.coord.set(this.matrix_800c6ac0);
      } else {
        //LAB_800e4e04
        model.coord2_14.coord.transfer.set(this.matrix_800c6ac0.transfer);
      }

      //LAB_800e4e18
      this._800f7e24 = 0;
    } else {
      //LAB_800e4e20
      final Vector3f sp10 = new Vector3f();
      this.get3dAverageOfSomething(index, sp10);
      model.coord2_14.coord.transfer.set(sp10);
    }

    //LAB_800e4e4c
  }

  @Method(0x800e4e5cL)
  private void FUN_800e4e5c() {
    //LAB_800e4ecc
    GPU.uploadData24(new Rect4i(640, 0, 368, 240), GPU.getDisplayBuffer().getData());
  }

  @Method(0x800e4f8cL)
  private void FUN_800e4f8c() {
    //LAB_800e4fd4
    this._800f7e28 = new Ptr<>(() -> this._800c6aec, val -> this._800c6aec = val);
  }

  @Method(0x800e4ff4L)
  private void FUN_800e4ff4() {
    UnknownStruct s0 = this._800c6aec;
    Ptr<UnknownStruct> s1 = new Ptr<>(() -> this._800c6aec, val -> this._800c6aec = val);

    //LAB_800e5018
    while(s0 != null) {
      s0.inner_08._00 = false;

      //LAB_800e5054
      final UnknownStruct finalS0 = s0;
      s1 = new Ptr<>(() -> finalS0.parent_00, val -> finalS0.parent_00 = val);

      //LAB_800e5058
      s0 = s1.get();
    }

    //LAB_800e5068
    this._800f7e28 = s1;
  }

  @Method(0x800e5084L)
  private long FUN_800e5084(final UnknownStruct2 a1) {
    final UnknownStruct v0 = new UnknownStruct();
    v0.inner_08 = a1;

    this._800f7e28.set(v0);
    this._800f7e28 = new Ptr<>(() -> v0.parent_00, val -> v0.parent_00 = val);

    //LAB_800e50ec
    return 0x1L;
  }

  @Method(0x800e5104L)
  private void loadAndRenderSubmapModelAndEffects(final int index, final MediumStruct a1) {
    this.executeSubmapMediaLoadingStage(index);

    a1.callback_48.accept(a1);

    this._800c6ae0++;

    if(gameState_800babc8.indicatorsDisabled_4e3) {
      this._800c6ae4 = -30;
    }

    //LAB_800e5184
    this.FUN_800e4ff4();

    if(submapFullyLoaded_800bd7b4) {
      this.renderSubmap();
    }
  }

  @Method(0x800e519cL)
  private void renderEnvironment() {
    //LAB_800e51e8
    final MV[] matrices = new MV[this.sobjCount_800c6730];
    for(int i = 0; i < this.sobjCount_800c6730; i++) {
      if(!this.isScriptLoaded(i)) {
        return;
      }

      matrices[i] = this.sobjs_800c6880[i].innerStruct_00.model_00.coord2_14.coord;
    }

    //LAB_800e5234
    this.renderEnvironment(matrices, this.sobjCount_800c6730);

    if(enableCollisionDebug) {
      if(this.SomethingStruct_800cbe08.dobj2Ptr_20.obj == null) {
        this.SomethingStruct_800cbe08.dobj2Ptr_20.obj = TmdObjLoader.fromObjTable("EnvironmentSomethingModel", this.SomethingStruct_800cbe08.dobj2Ptr_20.tmd_08);
      }

      final MV lw = new MV();
      final MV ls = new MV();
      GsGetLws(this.SomethingStructPtr_800d1a88.dobj2Ptr_20.coord2_04, lw, ls);
      GsSetLightMatrix(lw);
      GTE.setTransforms(ls);
      Renderer.renderDobj2(this.SomethingStructPtr_800d1a88.dobj2Ptr_20, false, 0);

      RENDERER.queueModel(this.SomethingStruct_800cbe08.dobj2Ptr_20.obj, lw)
        .screenspaceOffset(this.screenOffsetX_800cb568 + 8, -this.screenOffsetY_800cb56c)
      ;
    } else if(this.SomethingStruct_800cbe08.dobj2Ptr_20.obj != null) {
      this.SomethingStruct_800cbe08.dobj2Ptr_20.obj.delete();
      this.SomethingStruct_800cbe08.dobj2Ptr_20.obj = null;
    }
  }

  @Method(0x800e5264L)
  private boolean FUN_800e5264(final MV mat, final int submapCut) {
    if(submapCut_80052c3c != submapCut) {
      this._800cb448 = false;
      return false;
    }

    //LAB_800e5294
    if(!_80052c40) {
      return false;
    }

    //LAB_800e52b0
    this.setScreenOffsetIfNotSet(screenOffsetX_800bed50, screenOffsetY_800bed54);

    mat.set(matrix_800bed30);

    _80052c40 = false;
    this._800cb448 = true;

    //LAB_800e5320
    return true;
  }

  /**
   * Loads DRGN21 MRG @ 136653 - contains graphics for intro cutscene with Rose and Feyrbrand
   *
   * <ol start="0">
   *   <li>
   *     {@link EnvironmentFile} with 11 slices.
   *     <ol start="0">
   *       <li>Background slice 0</li>
   *       <li>Background slice 1</li>
   *       <li>Background slice 2</li>
   *       <li>Background slice 3</li>
   *       <li>Sky overlay 0</li>
   *       <li>Sky overlay 1</li>
   *       <li>Sky overlay 2</li>
   *       <li>Sky overlay 3</li>
   *       <li>Cliff overlay 0</li>
   *       <li>Cliff overlay 1</li>
   *       <li>Forest overlay</li>
   *     </ol>
   *   </li>
   *   <li>Unknown - related to the TMD TODO</li>
   *   <li>TMD - appears to be geometry of where Rose hops, and Feyrbrand's position?</li>
   *   <li>Background slice 0</li>
   *   <li>Background slice 1</li>
   *   <li>Background slice 2</li>
   *   <li>Background slice 3</li>
   *   <li>Background overlays and animated sky (lightning bolt)</li>
   * </ol>
   */
  @Method(0x800e5330L)
  private void loadBackground(final String mapName, final List<FileData> files) {
    this.backgroundLoaded_800cab10 = true;

    //LAB_800e5374
    for(int i = 3; i < files.size(); i++) {
      new Tim(files.get(i)).uploadToGpu();
    }

    //LAB_800e5430
    this.loadEnvironment(new EnvironmentFile(files.get(0)));
    this.loadCollision(new TmdWithId("Background " + mapName, files.get(2)), files.get(1));

    submapEnvState_80052c44 = 2;
    this._800cab20 = 2;
  }

  @Method(0x800e5518L)
  private boolean isScriptLoaded(final int index) {
    return this.sobjs_800c6880[index] != null;
  }

  /**
   * Also does things like open menus using special values. If cut >= 0x800, plays FMV (cut - 0x800) and transitions to engine state newScene once FMV is finished.
   *
   * <ul>
   *   <li>Scene 0x3fa - char swap</li>
   *   <li>Scene 0x3fb - ?</li>
   *   <li>Scene 0x3fc - too many items</li>
   *   <li>Scene 0x3fd - save</li>
   *   <li>Scene 0x3fe - shop</li>
   *   <li>Scene 0x3ff - inventory</li>
   * </ul>
   */
  @Method(0x800e5534L)
  public boolean mapTransition(final int newCut, final int newScene) {
    if(this.smapLoadingStage_800cb430 != SubmapState.RENDER_SUBMAP_12) {
      return false;
    }

    if(this._800c6ae0 < 3) {
      return false;
    }

    if(this._800f7e4c || (loadedDrgnFiles_800bcf78.get() & 0x82) != 0) {
      return false;
    }

    if(this._800c6ae0 > 15) {
      this._800cb448 = false;
    }

    submapFullyLoaded_800bd7b4 = false;

    if(this.mapTransitionTicks_800cab28 == 0) {
      if(fullScreenEffect_800bb140._24 == 0) {
        startFadeEffect(1, 10);
        this.mapTransitionTicks_800cab28++;
      }
    } else {
      this.mapTransitionTicks_800cab28++;
    }

    final int cut = submapCut_80052c30;
    this._800f7e4c = true;

    if(newCut > 0x7ff) {
      this.fmvIndex_800bf0dc = newCut - 0x800;
      this.afterFmvLoadingStage_800bf0ec = EngineStateEnum.values()[newScene];
      this.smapLoadingStage_800cb430 = SubmapState.TRANSITION_TO_FMV_21;
      return true;
    }

    if(newCut >= 0 && newCut < 2) {
      this.smapLoadingStage_800cb430 = SubmapState.TRANSITION_TO_WORLD_MAP_18;
      return true;
    }

    if(newCut > -1) {
      submapCut_80052c30 = newCut;
      submapScene_80052c34 = newScene;
      this.smapLoadingStage_800cb430 = SubmapState.CHANGE_SUBMAP_4;
      submapCutForSave_800cb450 = newCut;
      return true;
    }

    if(newScene == 0x3fc) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_TOO_MANY_ITEMS_MENU_31;
      this.smapLoadingStage_800cb430 = SubmapState.LOAD_MENU_13;
      return true;
    }

    if(newScene == 0x3fa) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_CHAR_SWAP_MENU_21;
      this.smapLoadingStage_800cb430 = SubmapState.LOAD_MENU_13;
      submapCutForSave_800cb450 = cut;
      return true;
    }

    if(newScene == 0x3fb) {
      SCRIPTS.pause();
      this.smapLoadingStage_800cb430 = SubmapState.TRANSITION_TO_TITLE_20;
      return true;
    }

    if(newScene == 0x3fe) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_SHOP_MENU_6;
      this.smapLoadingStage_800cb430 = SubmapState.LOAD_MENU_13;
      return true;
    }

    if(newScene == 0x3fd) {
      whichMenu_800bdc38 = WhichMenu.INIT_SAVE_GAME_MENU_16;
      this.smapLoadingStage_800cb430 = SubmapState.LOAD_MENU_13;
      this._800f7e2c[0]._04 = index_80052c38;
      index_80052c38 = this._800f7e2c[gameState_800babc8.chapterIndex_98]._04;
      submapCutForSave_800cb450 = this._800f7e2c[gameState_800babc8.chapterIndex_98]._00;
      this._800cab24 = this.FUN_800ea974(-1);
      SCRIPTS.pause();
      return true;
    }

    if(newScene == 0x3ff) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_INVENTORY_MENU_1;
      this.smapLoadingStage_800cb430 = SubmapState.LOAD_MENU_13;
      submapCutForSave_800cb450 = cut;
      return true;
    }

    final int scene;
    if(newScene == 0) {
      scene = encounterId_800bb0f8;
    } else {
      if(newScene > 0x1ff) {
        SCRIPTS.pause();
        return true;
      }

      scene = newScene;
    }

    encounterId_800bb0f8 = scene;

    if(this.isScriptLoaded(0)) {
      final SubmapObject210 sobj = this.sobjs_800c6880[0].innerStruct_00;
      final Model124 model = sobj.model_00;

      screenOffsetX_800bed50 = this.screenOffsetX_800cb568;
      screenOffsetY_800bed54 = this.screenOffsetY_800cb56c;
      submapCut_80052c3c = cut;
      matrix_800bed30.set(model.coord2_14.coord);
      matrix_800bed30.transfer.set(model.coord2_14.coord.transfer);
      _80052c40 = true;
    }

    SCRIPTS.pause();
    this.smapLoadingStage_800cb430 = SubmapState.TRANSITION_TO_COMBAT_19;
    return true;
  }

  @Override
  @Method(0x800e5914L)
  public void tick() {
    this.executeSmapLoadingStage_2();
  }

  @Override
  public void adjustModelPartUvs(final Model124 model, final ModelPart10 part) {
    this.adjustSmapUvs(part, model.vramSlot_9d);
  }

  @Method(0x800e59a4L)
  private void executeSmapLoadingStage_2() {
    this._800cab20--;

    if(this._800cab20 >= 0) {
      resizeDisplay(384, 240);
      this._800caaf4 = submapCut_80052c30;
      this._800caaf8 = submapScene_80052c34;
      return;
    }

    //LAB_800e5a30
    //LAB_800e5a34
    if(pregameLoadingStage_800bb10c == 0) {
      pregameLoadingStage_800bb10c = 1;
      this._800caaf4 = submapCut_80052c30;
      this._800caaf8 = submapScene_80052c34;
      submapEnvState_80052c44 = 2;
      this.smapLoadingStage_800cb430 = SubmapState.INIT_0;
    }

    //LAB_800e5ac4
    switch(this.smapLoadingStage_800cb430) {
      case INIT_0 -> {
        srand((int)System.nanoTime());
        resizeDisplay(384, 240);

        //LAB_800e5b2c
        submapEnvState_80052c44 = 2;
        this.encounterAccumulator_800c6ae8 = 0;
        this.smapLoadingStage_800cb430 = SubmapState.LOAD_NEWROOT_1;
      }

      case LOAD_NEWROOT_1 -> {
        loadFile("\\SUBMAP\\NEWROOT.RDT", data -> this.newrootPtr_800cab04 = new NewRootStruct(data));
        loadDir("\\SUBMAP\\savepoint", files -> {
          this.savepointAnm1 = new AnmFile(files.get(0));
          this.savepointAnm2 = new AnmFile(files.get(1));
          this.savepointTmd = new CContainer("Savepoint", files.get(2));
          this.savepointAnimation = new TmdAnimationFile(files.get(3));
          this.dustTmd = new CContainer("Dust", files.get(4));
          this.dustAnimation = new TmdAnimationFile(files.get(5));
        });
        this.smapLoadingStage_800cb430 = SubmapState.WAIT_FOR_NEWROOT_2;
      }

      case WAIT_FOR_NEWROOT_2 -> {
        if(Unpacker.getLoadingFileCount() == 0) {
          this.smapLoadingStage_800cb430 = SubmapState.LOAD_ENVIRONMENT_3;
        }
      }

      case LOAD_ENVIRONMENT_3 -> {
        this.mapTransitionTicks_800cab28 = 0;
        submapEnvState_80052c44 = 1;
        this._800caaf4 = submapCut_80052c30;
        this._800caaf8 = submapScene_80052c34;

        // Detect if we need to change disks
        final IntRef drgnIndex = new IntRef();
        final IntRef fileIndex = new IntRef();

        this.getDrgnFileFromNewRoot(submapCut_80052c30, drgnIndex, fileIndex);
        if(drgnIndex.get() != drgnBinIndex_800bc058) {
          //LAB_800e5c9c
          drgnBinIndex_800bc058 = drgnIndex.get();

          // Not sure if we still need to reinit the sound or not, but this is what retail did
          reinitSound();
          loadMenuSounds();
          sssqFadeIn(0x3c, 0x7f);
        }

        //LAB_800e5ccc
        this.backgroundLoaded_800cab10 = false;
        loadDrgnDir(2, fileIndex.get(), files -> this.loadBackground("DRGN2%d/%d".formatted(drgnIndex.get(), fileIndex.get()), files));
        this.smapLoadingStage_800cb430 = SubmapState.WAIT_FOR_ENVIRONMENT_6;
      }

      case CHANGE_SUBMAP_4 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);
        this.smapLoadingStage_800cb430 = SubmapState.TRANSITION_TO_SUBMAP_17;
      }

      case WAIT_FOR_ENVIRONMENT_6 -> {
        if(this.backgroundLoaded_800cab10) {
          this.smapLoadingStage_800cb430 = SubmapState.LOAD_MAP_POINTS_9;
        }
      }

      case LOAD_MAP_POINTS_9 -> {
        this.FUN_800e4d00(submapCut_80052c30, submapScene_80052c34);
        this.FUN_800e81a0(submapScene_80052c34);
        this.loadCollisionAndTransitions(submapCut_80052c30);
        this.clearSubmapFlags();

        //LAB_800e5e20
        this.smapLoadingStage_800cb430 = SubmapState.START_LOADING_MEDIA_10;
      }

      case START_LOADING_MEDIA_10 -> {
        this.mediaLoadingStage_800c68e4 = SubmapMediaState.LOAD_SHADOW_AND_RESET_LIGHTING_0;
        this.executeSubmapMediaLoadingStage(this._800caaf8);
        this.smapLoadingStage_800cb430 = SubmapState.FINISH_LOADING_AND_FADE_IN_11;
      }

      case FINISH_LOADING_AND_FADE_IN_11 -> {
        this.executeSubmapMediaLoadingStage(this._800caaf8);

        // Wait for media to finish loading
        if(this.mediaLoadingStage_800c68e4 != SubmapMediaState.DONE) {
          return;
        }

        if(this.isScriptLoaded(0)) {
          this.sobjs_800c6880[0].innerStruct_00.ui_16c = this._800caaf8;
        }

        //LAB_800e5e94
        this.FUN_800e770c();
        loadingNewGameState_800bdc34 = false;
        submapEnvState_80052c44 = 0;
        startFadeEffect(2, 10);
        this._800cab24 = this.FUN_800ea974(this._800caaf4);
        this.cacheHasNoEncounters();
        SCRIPTS.resume();
        this._800c6ae0 = 0;
        this.smapLoadingStage_800cb430 = SubmapState.RENDER_SUBMAP_12;
      }

      case RENDER_SUBMAP_12 -> {
        submapEnvState_80052c44 = 0;

        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);

        if(Input.pressedThisFrame(InputAction.BUTTON_NORTH) && !gameState_800babc8.indicatorsDisabled_4e3) {
          this.mapTransition(-1, 0x3ff); // Open inv
        }
      }

      case LOAD_MENU_13 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);

        submapFullyLoaded_800bd7b4 = false;

        if(this.mapTransitionTicks_800cab28 != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            startFadeEffect(1, 10);
          }

          //LAB_800e5fa4
          this.mapTransitionTicks_800cab28++;

          if(this.mapTransitionTicks_800cab28 <= 10) {
            break;
          }
        }

        //LAB_800e5fc0
        this.smapLoadingStage_800cb430 = SubmapState.RENDER_MENU_14;
        this.mapTransitionTicks_800cab28 = 0;
      }

      case RENDER_MENU_14 -> {
        final WhichMenu menu = whichMenu_800bdc38; // copy menu state since some of these functions may change it
        submapEnvState_80052c44 = 2;

        if(whichMenu_800bdc38 != WhichMenu.NONE_0) {
          loadAndRenderMenus();

          if(whichMenu_800bdc38 == WhichMenu.QUIT) {
            this.smapLoadingStage_800cb430 = SubmapState.RENDER_SUBMAP_12;
            this._800f7e4c = false;
            this.mapTransition(-1, 0x3fb);
            drgnBinIndex_800bc058 = 1;
            break;
          }

          if(whichMenu_800bdc38 != WhichMenu.NONE_0) {
            break;
          }
        }

        //LAB_800e6018
        switch(menu) {
          case UNLOAD_INVENTORY_MENU_5:
            if(gameState_800babc8.isOnWorldMap_4e4) {
              this.smapLoadingStage_800cb430 = SubmapState.TRANSITION_TO_WORLD_MAP_18;
              this._800f7e4c = false;
              break;
            }

            // Fall through

          case UNLOAD_CHAR_SWAP_MENU_25:
          case UNLOAD_TOO_MANY_ITEMS_MENU_35:
          case UNLOAD_SHOP_MENU_10:
            this.smapLoadingStage_800cb430 = SubmapState.UNLOAD_MENU_15;
            break;

          case UNLOAD_SAVE_GAME_MENU_20:
            this.smapLoadingStage_800cb430 = SubmapState.RENDER_SUBMAP_12;
            this._800f7e4c = false;
            this.mapTransition(this._800f7e2c[gameState_800babc8.chapterIndex_98]._00, this._800f7e2c[gameState_800babc8.chapterIndex_98]._00);
            index_80052c38 = this._800f7e2c[0]._04;
            break;
        }
      }

      case UNLOAD_MENU_15 -> {
        submapEnvState_80052c44 = 0;
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);
        SCRIPTS.resume();
        this._800f7e4c = false;
        this.smapLoadingStage_800cb430 = SubmapState.RENDER_SUBMAP_12;

        if(loadingNewGameState_800bdc34) {
          this.mapTransition(submapCut_80052c30, submapScene_80052c34);
        }
      }

      case TRANSITION_TO_SUBMAP_17 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);

        if(this.isScriptLoaded(0)) {
          this.sobjs_800c6880[0].innerStruct_00.us_12a = 1;
        }

        //LAB_800e61bc
        submapFullyLoaded_800bd7b4 = false;

        if(this.mapTransitionTicks_800cab28 != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            startFadeEffect(1, 10);
          }

          //LAB_800e61fc
          this.mapTransitionTicks_800cab28++;

          if(this.mapTransitionTicks_800cab28 <= 10) {
            break;
          }
        }

        //LAB_800e6218
        this.smapLoadingStage_800cb430 = SubmapState.LOAD_ENVIRONMENT_3;

        //LAB_800e6248
        if(this._800cb448) {
          submapEnvState_80052c44 = 3;
        } else {
          submapEnvState_80052c44 = 4;
        }

        //LAB_800e624c
        //LAB_800e6250
        this._800f7e4c = false;
      }

      case TRANSITION_TO_WORLD_MAP_18 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);

        submapFullyLoaded_800bd7b4 = false;

        if(this.mapTransitionTicks_800cab28 != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            startFadeEffect(1, 10);
          }

          //LAB_800e62b0
          this.mapTransitionTicks_800cab28++;

          if(this.mapTransitionTicks_800cab28 <= 10) {
            break;
          }
        }

        //LAB_800e62cc
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.WORLD_MAP_08;
        pregameLoadingStage_800bb10c = 0;
        vsyncMode_8007a3b8 = 2;
        submapEnvState_80052c44 = 5;
        this._800f7e4c = false;
        SCRIPTS.resume();
      }

      case TRANSITION_TO_COMBAT_19 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);
        submapEnvState_80052c44 = 5;
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.COMBAT_06;
        pregameLoadingStage_800bb10c = 0;
        vsyncMode_8007a3b8 = 2;
        this._800f7e4c = false;
        SCRIPTS.resume();
      }

      case TRANSITION_TO_TITLE_20 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);

        submapFullyLoaded_800bd7b4 = false;

        if(this.mapTransitionTicks_800cab28 != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            startFadeEffect(1, 10);
          }

          //LAB_800e643c
          this.mapTransitionTicks_800cab28++;

          if(this.mapTransitionTicks_800cab28 <= 10) {
            break;
          }
        }

        //LAB_800e6458
        FUN_8002a9c0();
        engineStateOnceLoaded_8004dd24 = EngineStateEnum.TITLE_02;
        vsyncMode_8007a3b8 = 2;
        pregameLoadingStage_800bb10c = 0;

        //LAB_800e6484
        submapEnvState_80052c44 = 5;

        //LAB_800e6490
        this._800f7e4c = false;
        SCRIPTS.resume();
      }

      case TRANSITION_TO_FMV_21 -> {
        this.loadAndRenderSubmapModelAndEffects(this._800caaf8, this._800cab24);

        submapFullyLoaded_800bd7b4 = false;

        if(this.mapTransitionTicks_800cab28 != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            startFadeEffect(1, 10);
          }

          //LAB_800e6394
          this.mapTransitionTicks_800cab28++;

          if(this.mapTransitionTicks_800cab28 <= 10) {
            break;
          }
        }

        //LAB_800e63b0
        submapEnvState_80052c44 = 5;
        Fmv.playCurrentFmv(this.fmvIndex_800bf0dc, this.afterFmvLoadingStage_800bf0ec);
        pregameLoadingStage_800bb10c = 0;
        this._800f7e4c = false;
        SCRIPTS.resume();
      }
    }
  }

  /** Has to be done after scripts are ticked since the camera is attached to a sobj and it would use the position from the previous frame */
  @Override
  public void postScriptTick() {
    //LAB_80020f20
    this.FUN_8002aae8();
    this.setIndicatorStatusAndResetIndicatorTickCountOnReenable();
  }

  @Method(0x800e6504L)
  private void getDrgnFileFromNewRoot(final int submapCut, final IntRef drgnIndexOut, final IntRef fileIndexOut) {
    final SubmapCutInfo entry = this.newrootPtr_800cab04.submapCutInfo_0000[submapCut];

    final int drgnIndex1 = entry.earlyGameFile_00 >>> 13;
    final int drgnIndex2 = entry.lateGameFile_02 >>> 13;

    // Once you reach a certain chapter, some maps will load from a different disk (like Fletz with the docks in chapter 4)
    final boolean useLateGameMap;
    if(drgnIndex1 == drgnBinIndex_800bc058 - 1 || drgnIndex2 > gameState_800babc8.chapterIndex_98) {
      drgnIndexOut.set(drgnIndex1);
      useLateGameMap = false;
    } else {
      drgnIndexOut.set(drgnIndex2);
      useLateGameMap = true;
    }

    final int t0 = drgnIndexOut.get() >= 0 && drgnIndexOut.get() <= 3 ? 4 : 0;

    drgnIndexOut.incr();
    fileIndexOut.set(((useLateGameMap ? entry.lateGameFile_02 : entry.earlyGameFile_00) & 0x1fff) * 3 + t0);
  }

  @Method(0x800e664cL)
  private void loadCollisionAndTransitions(final int submapCut) {
    Arrays.fill(this.collisionAndTransitions_800cb460, 0);

    final SubmapCutInfo entry = this.newrootPtr_800cab04.submapCutInfo_0000[submapCut];
    final short offset = entry.collisionAndTransitionOffset_04;

    if(offset < 0) {
      return;
    }

    //LAB_800e66dc
    for(int i = 0; i < entry.collisionAndTransitionCount_06; i++) {
      final int v1 = this.newrootPtr_800cab04.collisionAndTransitions_2000[offset / 4 + i];
      this.setCollisionAndTransitionInfo(v1);
    }

    //LAB_800e671c
  }

  @Method(0x800e6730L)
  private int getCollisionAndTransitionInfo(final int index) {
    // This did unsigned comparison, so -1 was >= 0x40
    if(index < 0 || index >= 0x40) {
      return 0;
    }

    return this.collisionAndTransitions_800cb460[index];
  }

  @Method(0x800e675cL)
  private void setCollisionAndTransitionInfo(final int a0) {
    this.collisionAndTransitions_800cb460[(a0 >> 8 & 0xfc) / 4] = a0;
  }

  @Method(0x800e6798L)
  private int FUN_800e6798(final int index) {
    final int v1 = this.getCollisionAndTransitionInfo(index);

    if((v1 & 0x8) != 0) {
      return 0;
    }

    //LAB_800e67c4
    return 1;
  }

  @ScriptDescription("Transitions to another submap cut/scene (certain values have special meanings - FMVs, menus)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "submapCut", description = "The submap cut")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "submapScene", description = "The submap scene")
  @Method(0x800e67d4L)
  private FlowControl scriptMapTransition(final RunningScript<?> script) {
    final int scene = script.params_20[1].get();

    this.mapTransition(script.params_20[0].get(), scene);

    if(scene == 0x3fa || scene == 0x3fc || scene == 0x3fe || scene == 0x3ff) {
      return FlowControl.CONTINUE;
    }

    //LAB_800e6828
    return FlowControl.PAUSE_AND_REWIND;
  }

  @ScriptDescription("Either sets the mode for a subsequent call to scriptSetCameraOffset, or hides a submap foreground overlay. May be for revealing areas when walking through doors, for example.")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "< 3, sets the mode for a subsequent call to scriptSetCameraOffset; 3 hides foreground object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "foregroundObjectIndex", description = "The foreground object index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out", description = "The value passed in for mode, unless mode is 3, in which case the mode variable isn't updated and the previous value is returned")
  @Method(0x800e683cL)
  private FlowControl scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject(final RunningScript<?> script) {
    if(script.params_20[0].get() < 3) {
      this.scriptSetOffsetMode_800f7e50 = script.params_20[0].get();
    }

    //LAB_800e686c
    if(script.params_20[0].get() == 3) {
      this.setEnvForegroundPosition(1024, 1024, script.params_20[1].get()); // Hide foreground object
    }

    //LAB_800e688c
    script.params_20[2].set(this.scriptSetOffsetMode_800f7e50);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the camera offset")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The camera X offset")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The camera Y offset")
  @Method(0x800e68b4L)
  private FlowControl scriptGetCameraOffset(final RunningScript<?> script) {
    script.params_20[0].set(this.screenOffsetX_800cb568);
    script.params_20[1].set(this.screenOffsetY_800cb56c);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the camera offset (mode is based on the last call to scriptSetModeParamForNextCallToScriptSetCameraOffsetOrHideSubmapForegroundObject)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The camera X offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The camera Y offset")
  @Method(0x800e6904L)
  private FlowControl scriptSetCameraOffset(final RunningScript<?> script) {
    final int x = script.params_20[0].get();
    final int y = script.params_20[1].get();
    final int mode = this.scriptSetOffsetMode_800f7e50;

    if(mode == 1 || mode == 2) {
      //LAB_800e695c
      this.setGeomOffsetIfNotSet(x, y);
    }

    if(mode == 2 || mode == 3) {
      this.setScreenOffsetIfNotSet(x, y);
    }

    //LAB_800e6988
    this.scriptSetOffsetMode_800f7e50 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets collision and transition info (not fully understood, don't know what index is, or what the flags are)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The collision and transition info")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The index")
  @Method(0x800e69a4L)
  private FlowControl scriptGetCollisionAndTransitionInfo(final RunningScript<?> script) {
    script.params_20[0].set(this.getCollisionAndTransitionInfo(script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800e69e8L)
  private FlowControl FUN_800e69e8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets collision and transition flag 0x8 (not fully understood, don't know what index is, or what the flags are)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The index")
  @Method(0x800e69f0L)
  private FlowControl FUN_800e69f0(final RunningScript<?> script) {
    this.setCollisionAndTransitionInfo(this.getCollisionAndTransitionInfo(script.params_20[0].get()) | 0x8);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears collision and transition flag 0x8 (not fully understood, don't know what index is, or what the flags are)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The index")
  @Method(0x800e6a28L)
  private FlowControl FUN_800e6a28(final RunningScript<?> script) {
    this.setCollisionAndTransitionInfo(this.getCollisionAndTransitionInfo(script.params_20[0].get()) & 0xffff_fff7);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the position of an environment foreground overlay")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The new X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The new Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "overlayIndex", description = "The overlay index")
  @Method(0x800e6a64L)
  private FlowControl scriptSetEnvForegroundPosition(final RunningScript<?> script) {
    this.setEnvForegroundPosition(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to environment foreground overlays")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "The mode (maybe layering mode?)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "overlayIndex", description = "The overlay index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The new Z position (only applies in modes 2 and 5")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out", description = "The overlay's Z position")
  @Method(0x800e6aa0L)
  private FlowControl FUN_800e6aa0(final RunningScript<?> script) {
    script.params_20[3].set(this.FUN_800e7728(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets (and optionally sets) whether submap encounters are disabled")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to disable encounters, false otherwise")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "apply", description = "If false, does not update value, only returns it")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The value of the submap flags (0x1 - encounters disabled)")
  @Method(0x800e6af0L)
  private FlowControl scriptGetSetEncountersDisabled(final RunningScript<?> script) {
    if(script.params_20[1].get() == 1) {
      if(script.params_20[0].get() != 0) {
        this.submapFlags_800f7e54 |= 0x1;
      } else {
        //LAB_800e6b34
        this.submapFlags_800f7e54 &= 0xffff_fffe;
      }
    }

    //LAB_800e6b48
    script.params_20[2].set(this.submapFlags_800f7e54);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, returns vector possibly related to collision")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X value")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y value")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z value")
  @Method(0x800e6b64L)
  private FlowControl FUN_800e6b64(final RunningScript<?> script) {
    if(script.params_20[0].get() >= 0) {
      final Vector3f sp0x10 = new Vector3f();
      this.get3dAverageOfSomething(script.params_20[0].get(), sp0x10);

      script.params_20[1].set(Math.round(sp0x10.x));
      script.params_20[2].set(Math.round(sp0x10.y));
      script.params_20[3].set(Math.round(sp0x10.z));
    }

    //LAB_800e6bc8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800e6bd8L)
  private FlowControl FUN_800e6bd8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown usage, I think this calculates a Z sorting value for a submap object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The calculated Z value")
  @Method(0x800e6be0L)
  private FlowControl FUN_800e6be0(final RunningScript<?> script) {
    final MV coord = this.sobjs_800c6880[script.params_20[0].get()].innerStruct_00.model_00.coord2_14.coord;
    script.params_20[1].set(Math.round((worldToScreenMatrix_800c3548.m02 * coord.transfer.x + worldToScreenMatrix_800c3548.m12 * coord.transfer.y + worldToScreenMatrix_800c3548.m22 * coord.transfer.z + worldToScreenMatrix_800c3548.transfer.z) / (1 << 16 - orderingTableBits_1f8003c0)));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, seems to be a temporary X/Y camera offset for the next time the offset is calculated")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The camera X offset")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "Y", description = "The camera Y offset")
  @Method(0x800e6cacL)
  private FlowControl FUN_800e6cac(final RunningScript<?> script) {
    this.FUN_800e80e4(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Starts an FMV")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fmvIndex", description = "The FMV index to play")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "engineStateAfterFmv", description = "The engine state to transition to after the FMV")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "submapCutAfterFmv", description = "The submap cut after the FMV")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "submapSceneAfterFmv", description = "The submap scene after the FMV")
  @Method(0x800e6ce0L)
  private FlowControl scriptStartFmv(final RunningScript<?> script) {
    this.mapTransition(script.params_20[0].get() + 0x800, script.params_20[1].get());
    submapCut_80052c30 = script.params_20[2].get();
    submapScene_80052c34 = script.params_20[3].get();
    submapCutForSave_800cb450 = submapCut_80052c30;
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800e6d4cL)
  private void clearSubmapFlags() {
    this.submapFlags_800f7e54 = 0;
  }

  @Method(0x800e6d58L)
  private boolean FUN_800e6d58(final int submapCut) {
    for(int i = 0; i < 45; i++) {
      if(this._800f7e58[i] == submapCut) {
        return true;
      }
    }

    return false;
  }

  @Method(0x800e6d9cL)
  private void calculateSubmapBounds(final EnvironmentStruct[] envs, final int backgroundTextureCount) {
    int left = 0x7fff;
    int right = -0x8000;
    int top = 0x7fff;
    int bottom = -0x8000;

    //LAB_800e6dc8
    for(int i = 0; i < backgroundTextureCount; i++) {
      final EnvironmentStruct env = envs[i];

      if(env.s_06 == 0x4e) {
        if(right < env.pos_08.w + env.textureOffsetX_10) {
          right = env.pos_08.w + env.textureOffsetX_10;
        }

        if(left > env.textureOffsetX_10) {
          left = env.textureOffsetX_10;
        }

        if(bottom < env.pos_08.h + env.textureOffsetY_12) {
          bottom = env.pos_08.h + env.textureOffsetY_12;
        }

        if(top > env.textureOffsetY_12) {
          top = env.textureOffsetY_12;
        }
      }

      //LAB_800e6e64
    }

    //LAB_800e6e74
    final int width = right - left;
    final int height = bottom - top;
    this.submapOffsetX_800cb560 = -width / 2;
    this.submapOffsetY_800cb564 = -height / 2;
    this._800cb570 = (width - 384) / 2;

    if(width == 384 && height == 256 || this.FUN_800e6d58(submapCut_80052c30)) {
      this._800cb574 = (376 - width) / 2;
    } else {
      this._800cb574 = -this._800cb570;
    }

    this._800cb578 = (height - 240) / 2;
  }

  @Method(0x800e6f38L)
  private void buildBackgroundRenderingPacket(final EnvironmentStruct[] env) {
    //LAB_800e6f9c
    for(int i = 0; i < this.envTextureCount_800cb584; i++) {
      final EnvironmentStruct s0 = env[i];

      int clutY = Math.abs(s0.clutY_22); // Negative means translucent
      if(i < this.envBackgroundTextureCount_800cb57c) { // It's a background texture
        if(clutY < 0x1f0 || clutY >= 0x200) {
          clutY = i + 0x1f0;
        }
      } else { // It's a foreground texture
        //LAB_800e7010
        this._800cbb90[i - this.envBackgroundTextureCount_800cb57c].set(s0.svec_14);
        this._800cbc90[i - this.envBackgroundTextureCount_800cb57c] = s0.ui_1c;
      }

      //LAB_800e7004
      //LAB_800e7074
      final EnvironmentRenderingMetrics24 renderPacket = this.envRenderMetrics_800cb710[i];

      float z;
      if(s0.s_06 == 0x4e) {
        //LAB_800e7148
        z = (0x1 << orderingTableBits_1f8003c0) - 1;
      } else if(s0.s_06 == 0x4f) {
        z = 40;
      } else {
        //LAB_800e7194
        z =
          worldToScreenMatrix_800c3548.m02 * s0.svec_00.x +
            worldToScreenMatrix_800c3548.m12 * s0.svec_00.y +
            worldToScreenMatrix_800c3548.m22 * s0.svec_00.z;
        z += worldToScreenMatrix_800c3548.transfer.z;
        z /= 1 << 16 - orderingTableBits_1f8003c0;
      }

      renderPacket.z_20 = Math.round(z);
      renderPacket.tpage_04 = s0.tpage_20;
      renderPacket.r_0c = 0x80;
      renderPacket.g_0d = 0x80;
      renderPacket.b_0e = 0x80;
      renderPacket.u_14 = s0.pos_08.x;
      renderPacket.v_15 = s0.pos_08.y;
      renderPacket.clut_16 = clutY << 6 | 0x30;
      renderPacket.w_18 = s0.pos_08.w;
      renderPacket.h_1a = s0.pos_08.h;

      //LAB_800e70ec
      renderPacket.offsetX_1c = s0.textureOffsetX_10;
      renderPacket.offsetY_1e = s0.textureOffsetY_12;

      //LAB_800e7210
      renderPacket.flags_22 &= 0x3fff;
    }

    //LAB_800e724c
    this.calculateSubmapBounds(env, this.envBackgroundTextureCount_800cb57c);
  }

  @Method(0x800e728cL)
  private void clearSmallValuesFromMatrix(final MV matrix) {
    //LAB_800e72b4
    for(int x = 0; x < 3; x++) {
      //LAB_800e72c4
      for(int y = 0; y < 3; y++) {
        if(Math.abs(matrix.get(x, y)) < 0.015625f) {
          matrix.set(x, y, 0.0f);
        }

        //LAB_800e72e8
      }
    }
  }

  @Method(0x800e7328L)
  private void updateCamera() {
    setProjectionPlaneDistance(projectionPlaneDistance_800bd810);
    GsSetSmapRefView2L(this.rview2_800cbd10);
    this.clearSmallValuesFromMatrix(worldToScreenMatrix_800c3548);
    this.worldToScreenMatrix_800cbd68.set(worldToScreenMatrix_800c3548);
    this.worldToScreenMatrix_800cbd68.transpose(this.screenToWorldMatrix_800cbd40);
    rview2_800bd7e8.set(this.rview2_800cbd10);
  }

  @Method(0x800e7418L)
  private void updateRview2(final Vector3f viewpoint, final Vector3f refpoint, final int rotation, final int projectionDistance) {
    this.rview2_800cbd10.viewpoint_00.set(viewpoint);
    this.rview2_800cbd10.refpoint_0c.set(refpoint);
    this.rview2_800cbd10.viewpointTwist_18 = (short)rotation << 12;
    this.rview2_800cbd10.super_1c = null;
    projectionPlaneDistance_800bd810 = projectionDistance;

    this.updateCamera();
  }

  @Method(0x800e7500L)
  private void loadEnvironment(final EnvironmentFile envFile) {
    this.envTextureCount_800cb584 = envFile.allTextureCount_14;
    this.envBackgroundTextureCount_800cb57c = envFile.backgroundTextureCount_15;
    this.envForegroundTextureCount_800cb580 = envFile.foregroundTextureCount_16;

    this.updateRview2(
      envFile.viewpoint_00,
      envFile.refpoint_08,
      envFile.rotation_12,
      envFile.projectionDistance_10
    );

    this.buildBackgroundRenderingPacket(envFile.environments_18);

    for(final EnvironmentForegroundTextureMetrics struct : this.envForegroundMetrics_800cb590) {
      struct.clear();
    }
  }

  @Method(0x800e7604L)
  private void setGeomOffsetIfNotSet(final int x, final int y) {
    if(!this._800cbd3c._00) {
      this._800cbd3c._00 = true;
      GTE.setScreenOffset(x, y);
    }
  }

  @Method(0x800e7650L)
  private void setScreenOffsetIfNotSet(final int x, final int y) {
    // Added null check - bug in game code
    if(this._800cbd38 != null && !this._800cbd38._00) {
      this._800cbd38._00 = true;
      this.screenOffsetX_800cb568 = x;
      this.screenOffsetY_800cb56c = y;
    }
  }

  @Method(0x800e76b0L)
  private void setEnvForegroundPosition(final int x, final int y, final int index) {
    if(x == 1024 && y == 1024) {
      this.envForegroundMetrics_800cb590[index].hidden_08 = true;
      return;
    }

    //LAB_800e76e8
    //LAB_800e76ec
    this.envForegroundMetrics_800cb590[index].x_00 = x;
    this.envForegroundMetrics_800cb590[index].y_04 = y;
    this.envForegroundMetrics_800cb590[index].hidden_08 = false;
  }

  @Method(0x800e770cL)
  private void FUN_800e770c() {
    this.minSobj_800cbd60 = 0;
    this.maxSobj_800cbd64 = this.sobjCount_800c6730;
  }

  @Method(0x800e7728L)
  private int FUN_800e7728(final int mode, final int foregroundTextureIndex, int z) {
    final int textureIndex = this.envBackgroundTextureCount_800cb57c + foregroundTextureIndex;

    if(mode == 1 && foregroundTextureIndex == -1) {
      //LAB_800e7780
      for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
        this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i].flags_22 &= 0x3fff;
        this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i].flags_22 |= 0x4000;
      }

      //LAB_800e77b4
      return this.envRenderMetrics_800cb710[textureIndex].z_20;
    }

    //LAB_800e77d8
    if(textureIndex >= this.envRenderMetrics_800cb710.length) {
      return -1;
    }

    if(mode == 0) {
      //LAB_800e7860
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0x3fff;
    } else if(mode == 1) {
      //LAB_800e77e8
      //LAB_800e7830
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0x3fff;
      this.envRenderMetrics_800cb710[textureIndex].flags_22 |= 0x4000;
      //LAB_800e7808
    } else if(mode == 2) {
      //LAB_800e788c
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0x3fff;
      this.envRenderMetrics_800cb710[textureIndex].flags_22 |= 0x8000;

      if(z < 40) {
        //LAB_800e78fc
        z = 40;
      } else if((0x1 << orderingTableBits_1f8003c0) - 1 < z) {
        z = (0x1 << orderingTableBits_1f8003c0) - 1;
      }

      //LAB_800e7900
      this.envRenderMetrics_800cb710[textureIndex].flags_22 &= 0xc000;
      this.envRenderMetrics_800cb710[textureIndex].flags_22 |= z & 0x3fff;
    } else if(mode == 5) {
      this.minSobj_800cbd60 = foregroundTextureIndex;
      this.maxSobj_800cbd64 = z + 1;
    }

    //LAB_800e7930
    //LAB_800e7934
    //LAB_800e7938
    return this.envRenderMetrics_800cb710[textureIndex].z_20;
  }

  @Method(0x800e7954L)
  private void renderEnvironment(final MV[] sobjMatrices, final int sobjCount) {
    final float[] sobjZs = new float[sobjCount];
    final float[] envZs = new float[this.envForegroundTextureCount_800cb580];

    //LAB_800e79b8
    // Render background
    for(int i = 0; i < this.envBackgroundTextureCount_800cb57c; i++) {
      final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[i];

      if(metrics.obj == null) {
        metrics.obj = new QuadBuilder("BackgroundTexture (index " + i + ')')
          .bpp(Bpp.of(metrics.tpage_04 >>> 7 & 0b11))
          .clut(768, metrics.clut_16 >>> 6)
          .vramPos((metrics.tpage_04 & 0b1111) * 64, (metrics.tpage_04 & 0b10000) != 0 ? 256 : 0)
          .pos(metrics.offsetX_1c, metrics.offsetY_1e, metrics.z_20 * 4.0f)
          .uv(metrics.u_14, metrics.v_15)
          .size(metrics.w_18, metrics.h_1a)
          .build();
      }

      metrics.transforms.identity();
      metrics.transforms.transfer.set(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffsetX_800cb568, GPU.getOffsetY() + this.submapOffsetY_800cb564 + this.screenOffsetY_800cb56c, 0.0f);
      RENDERER.queueOrthoModel(metrics.obj, metrics.transforms);
    }

    //LAB_800e7a60
    //LAB_800e7a7c
    for(int i = 0; i < sobjCount; i++) {
      sobjZs[i] = (worldToScreenMatrix_800c3548.m02 * sobjMatrices[i].transfer.x +
        worldToScreenMatrix_800c3548.m12 * sobjMatrices[i].transfer.y +
        worldToScreenMatrix_800c3548.m22 * sobjMatrices[i].transfer.z + worldToScreenMatrix_800c3548.transfer.z) / (1 << 16 - orderingTableBits_1f8003c0);
    }

    //LAB_800e7b08
    //LAB_800e7b40
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];

      final int flags = (metrics.flags_22 & 0xc000) >> 14;
      if(flags == 0x1) {
        //LAB_800e7bb4
        float minZ = Float.MAX_VALUE;
        float maxZ = 0;

        //LAB_800e7bbc
        int positiveZCount = 0;
        int negativeZCount = 0;

        //LAB_800e7c0c
        for(int sobjIndex = this.maxSobj_800cbd64 - 1; sobjIndex >= this.minSobj_800cbd60; sobjIndex--) {
          final float v1_0 = this._800cbc90[i] +
            this._800cbb90[i].x * sobjMatrices[sobjIndex].transfer.x +
            this._800cbb90[i].y * sobjMatrices[sobjIndex].transfer.y +
            this._800cbb90[i].z * sobjMatrices[sobjIndex].transfer.z;
          final float sobjZ = sobjZs[sobjIndex];

          if(sobjZ != 0xfffb) {
            if(v1_0 < 0) {
              negativeZCount++;
              if(minZ > sobjZ) {
                minZ = sobjZ;
              }
            } else {
              //LAB_800e7cac
              positiveZCount++;
              if(maxZ < sobjZ) {
                maxZ = sobjZ;
              }
            }
          }
        }

        //LAB_800e7cd8
        if(positiveZCount == 0) {
          //LAB_800e7cf8
          envZs[i] = Math.max(maxZ - 50, 40);
          continue;
        }

        //LAB_800e7d00
        if(negativeZCount == 0) {
          //LAB_800e7d3c
          envZs[i] = Math.min(maxZ + 50, (1 << orderingTableBits_1f8003c0) - 1);
          continue;
        }

        //LAB_800e7d50
        if(maxZ > metrics.z_20 || minZ < metrics.z_20) {
          //LAB_800e7d64
          envZs[i] = (maxZ + minZ) / 2;
        } else {
          //LAB_800e7d78
          envZs[i] = metrics.z_20;
        }

        //LAB_800e7d80
      } else if(flags == 0x2) {
        envZs[i] = metrics.flags_22 & 0x3fff;
      } else {
        //LAB_800e7d78
        envZs[i] = metrics.z_20;
      }
    }

    //LAB_800e7d9c
    //LAB_800e7de0
    // Render overlays
    for(int i = 0; i < this.envForegroundTextureCount_800cb580; i++) {
      if(!this.envForegroundMetrics_800cb590[i].hidden_08) {
        final EnvironmentRenderingMetrics24 metrics = this.envRenderMetrics_800cb710[this.envBackgroundTextureCount_800cb57c + i];

        if(metrics.obj == null) {
          metrics.obj = new QuadBuilder("CutoutTexture (index " + i + ')')
            .bpp(Bpp.of(metrics.tpage_04 >>> 7 & 0b11))
            .clut(768, metrics.clut_16 >>> 6)
            .vramPos((metrics.tpage_04 & 0b1111) * 64, (metrics.tpage_04 & 0b10000) != 0 ? 256 : 0)
            .pos(metrics.offsetX_1c, metrics.offsetY_1e, metrics.z_20 * 4.0f)
            .uv(metrics.u_14, metrics.v_15)
            .size(metrics.w_18, metrics.h_1a)
            .build();
        }

        // This was causing a problem when moving left from the room before Zackwell. Not sure if this is a retail issue or SC-specific. GH#332
        final float z = envZs[i];
        if(z < 0) {
          continue;
        }

        metrics.transforms.identity();
        metrics.transforms.transfer.set(GPU.getOffsetX() + this.submapOffsetX_800cb560 + this.screenOffsetX_800cb568 + this.envForegroundMetrics_800cb590[i].x_00, GPU.getOffsetY() + this.submapOffsetY_800cb564 + this.screenOffsetY_800cb56c + this.envForegroundMetrics_800cb590[i].y_04, 0.0f);
        RENDERER.queueOrthoModel(metrics.obj, metrics.transforms);
      }
    }

    //LAB_800e7ed0
  }

  @Method(0x800e7f00L)
  private void transformVertex(final Vector2f out, final Vector3f v0) {
    GTE.perspectiveTransform(v0);
    out.set(GTE.getScreenX(2), GTE.getScreenY(2));
  }

  @Method(0x800e7f68L)
  private void calcGoodScreenOffset(final float x, final float y) {
    if(x < -80) {
      this.screenOffsetX_800cb568 -= 80 + x;
      //LAB_800e7f80
    } else if(x > 80) {
      //LAB_800e7f9c
      this.screenOffsetX_800cb568 += 80 - x;
    }

    //LAB_800e7fa8
    if(y < -40) {
      this.screenOffsetY_800cb56c -= 40 + y;
      //LAB_800e7fbc
    } else if(y > 40) {
      //LAB_800e7fd4
      this.screenOffsetY_800cb56c += 40 - y;
    }

    //LAB_800e7fdc
    if(this._800f7f0c) {
      this.screenOffsetX_800cb568 += this._800cbd30;
      this.screenOffsetY_800cb56c += this._800cbd34;
      this._800f7f0c = false;
      return;
    }

    //LAB_800e8030
    if(this.screenOffsetX_800cb568 < this._800cb574) {
      //LAB_800e807c
      this.screenOffsetX_800cb568 = this._800cb574;
    } else {
      //LAB_800e8070
      this.screenOffsetX_800cb568 = Math.min(this._800cb570, this.screenOffsetX_800cb568);
    }

    //LAB_800e8080
    //LAB_800e8088
    if(this.screenOffsetY_800cb56c < -this._800cb578) {
      this.screenOffsetY_800cb56c = -this._800cb578;
    } else {
      //LAB_800e80d0
      //LAB_800e80d8
      this.screenOffsetY_800cb56c = Math.min(this._800cb578, this.screenOffsetY_800cb56c);
    }

    //LAB_800e80dc
  }

  @ScriptDescription("Sets collision metrics for a submap object (collision type unknown)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "horizontalSize", description = "The horizontal collision size")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "verticalSize", description = "The vertical collision size")
  @Method(0x800e06c4L)
  private FlowControl scriptSetSobjCollision1(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.collisionSizeHorizontal_1a0 = script.params_20[1].get();
    sobj.collisionSizeVertical_1a4 = script.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a submap object is collided (collision type unknown)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "collided", description = "True if the submap object is collided")
  @Method(0x800e0710L)
  private FlowControl scriptGetSobjCollidedWith1(final RunningScript<?> script) {
    script.params_20[1].set(((SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).collidedWithSobjIndex_19c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e80e4L)
  private void FUN_800e80e4(final int x, final int y) {
    this._800cbd30 = x;
    this._800cbd34 = y;
    this._800f7f0c = true;
  }

  @Method(0x800e8104L)
  private void setCameraPos(final Vector3f cameraPos) {
    if(!this._800cbd38._00) {
      this._800cbd38._00 = true;

      final Vector2f transformed = new Vector2f();
      this.transformVertex(transformed, cameraPos);
      this.calcGoodScreenOffset(transformed.x, transformed.y);
    }

    //LAB_800e8164
    this.setScreenOffsetIfNotSet(this.screenOffsetX_800cb568, this.screenOffsetY_800cb56c);
    this.setGeomOffsetIfNotSet(this.screenOffsetX_800cb568, this.screenOffsetY_800cb56c);
  }

  @Method(0x800e81a0L)
  private void FUN_800e81a0(final int index) {
    final UnknownStruct2 s0_0 = new UnknownStruct2();
    this.FUN_800e5084(s0_0);
    this._800cbd38 = s0_0;

    final UnknownStruct2 s0_1 = new UnknownStruct2();
    this.FUN_800e5084(s0_1);
    this._800cbd3c = s0_1;

    final Vector3f avg = new Vector3f();
    this.get3dAverageOfSomething(index, avg);
    this.setCameraPos(avg);
  }

  @Method(0x800e828cL)
  private void FUN_800e828c() {
    this._800cbd38 = null;
    this._800cbd3c = null;
  }

  @Method(0x800e82ccL)
  private void transformToWorldspace(final Vector3f out, final Vector3f in) {
    //TODO does this need to be transposed?
    if(this.screenToWorldMatrix_800cbd40.m02 == 0.0f) {
      out.set(in);
    } else {
      //LAB_800e8318
      in.mul(this.screenToWorldMatrix_800cbd40, out);
    }

    //LAB_800e833c
  }

  @Method(0x800e866cL)
  private void FUN_800e866c() {
    //LAB_800e86a4
    for(int i = 0; i < this.SomethingStructPtr_800d1a88.count_0c; i++) {
      final float y = Math.abs(this.SomethingStructPtr_800d1a88.normals_08[i].y);
      this.SomethingStructPtr_800d1a88.ptr_14[i].bool_01 = y > 0x400;
    }

    //LAB_800e86f0
  }

  @Method(0x800e88a0L)
  private int FUN_800e88a0(final long a0, final Vector3f playerPosition, final Vector3f playerMovement) {
    if(a0 != 0) {
      return this.FUN_800e9430(playerPosition.x, playerPosition.y, playerPosition.z, playerMovement);
    }

    //LAB_800e88d8
    if(!this._800cbe34._00) {
      this._800cbe34._00 = true;

      //LAB_800e8908
      this._800cbd94 = this.FUN_800e9430(playerPosition.x, playerPosition.y, playerPosition.z, playerMovement);
      this._800cbd98.set(playerMovement);
    } else {
      //LAB_800e8954
      playerMovement.set(this._800cbd98);
    }

    //LAB_800e897c
    //LAB_800e8980
    return this._800cbd94;
  }

  @Method(0x800e8990L)
  private int FUN_800e8990(final float x, final float z) {
    final Vector3f vec = new Vector3f();

    int farthestIndex = 0;
    float farthest = Float.MAX_VALUE;
    final SomethingStruct struct = this.SomethingStructPtr_800d1a88;

    //LAB_800e89b8
    for(int i = 0; i < struct.count_0c; i++) {
      vec.zero();

      //LAB_800e89e0
      if(this._800f7f14) {
        //LAB_800e89f8
        final SomethingStructSub0c_1 struct2 = struct.ptr_14[i];
        final TmdObjTable1c.Primitive primitive = struct.getPrimitiveForOffset(struct2.primitivesOffset_04);
        final int packetOffset = struct2.primitivesOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        //LAB_800e8a38
        for(int t0 = 0; t0 < struct2.count_00; t0++) {
          vec.add(struct.verts_04[IoHelper.readUShort(packet, remainder + 2 + t0 * 2)]);
        }

        //LAB_800e8a9c
        vec.div(struct2.count_00);
      }

      //LAB_800e8ae4
      final float dx = x - vec.x;
      final float dz = z - vec.z;
      final float distSqr = dx * dx + dz * dz;
      if(distSqr < farthest) {
        farthest = distSqr;
        farthestIndex = i;
      }

      //LAB_800e8b2c
    }

    //LAB_800e8b34
    return farthestIndex;
  }

  @Method(0x800e8b40L)
  private void FUN_800e8b40(final SomethingStruct a0, FileData a1) {
    final int count = a0.count_0c;

    if(a1.size() < count * 5 * 0xc) {
      LOGGER.warn("Submap file too short, padding with 0's");
      final byte[] newData = new byte[count * 5 * 0xc];
      a1.copyFrom(0, newData, 0, a1.size());
      a1 = new FileData(newData);
    }

    a0.ptr_14 = new SomethingStructSub0c_1[count];
    a0.ptr_18 = new SomethingStructSub0c_2[count * 4];

    final FileData finalA1 = a1;
    Arrays.setAll(a0.ptr_14, i -> new SomethingStructSub0c_1(finalA1.slice(i * 0xc, 0xc)));
    Arrays.setAll(a0.ptr_18, i -> new SomethingStructSub0c_2(finalA1.slice((count + i) * 0xc, 0xc)));
  }

  @Method(0x800e8bd8L)
  private void FUN_800e8bd8(final SomethingStruct a0) {
    final TmdObjTable1c objTable = a0.objTableArrPtr_00[0];
    a0.verts_04 = objTable.vert_top_00;
    a0.normals_08 = new Vector3f[objTable.normal_top_08.length];
    Arrays.setAll(a0.normals_08, i -> new Vector3f(objTable.normal_top_08[i].x * 4096.0f, objTable.normal_top_08[i].y * 4096.0f, objTable.normal_top_08[i].z * 4096.0f));
    a0.count_0c = objTable.n_primitive_14;
    a0.primitives_10 = objTable.primitives_10;
  }

  @Method(0x800e8c50L)
  private void FUN_800e8c50(final ModelPart10 dobj2, final SomethingStruct a1, final TmdWithId tmd) {
    a1.tmdPtr_1c = tmd;
    final TmdObjTable1c[] objTables = tmd.tmd.objTable;
    dobj2.tmd_08 = objTables[0];
    a1.objTableArrPtr_00 = objTables;
    this.FUN_800e8bd8(a1);
  }

  @Method(0x800e8cd0L)
  private void loadCollision(final TmdWithId tmd, final FileData a2) {
    this.SomethingStructPtr_800d1a88 = this.SomethingStruct_800cbe08;
    this.SomethingStruct_800cbe08.dobj2Ptr_20 = this.GsDOBJ2_800cbdf8;
    this.SomethingStruct_800cbe08.coord2Ptr_24 = this.GsCOORDINATE2_800cbda8;
    GsInitCoordinate2(null, this.GsCOORDINATE2_800cbda8);

    this.SomethingStructPtr_800d1a88.dobj2Ptr_20.coord2_04 = this.SomethingStructPtr_800d1a88.coord2Ptr_24;
    this.SomethingStructPtr_800d1a88.dobj2Ptr_20.attribute_00 = 0x4000_0000;

    this.FUN_800e8c50(this.SomethingStructPtr_800d1a88.dobj2Ptr_20, this.SomethingStructPtr_800d1a88, tmd);
    this.FUN_800e8b40(this.SomethingStructPtr_800d1a88, a2);

    this._800f7f14 = true;

    final UnknownStruct2 s0_0 = new UnknownStruct2();
    this.FUN_800e5084(s0_0);
    this._800cbe34 = s0_0;

    final UnknownStruct2 s0_1 = new UnknownStruct2();
    this.FUN_800e5084(s0_1);
    this._800d1a8c = s0_1;

    final UnknownStruct2 s0_2 = new UnknownStruct2();
    this.FUN_800e5084(s0_2);
    this._800cbe38 = s0_2;

    this.FUN_800e866c();
  }

  /** Unloads data when transitioning */
  @Method(0x800e8e50L)
  private void FUN_800e8e50() {
    this._800f7f14 = false;

    this._800cbe34 = null;
    this._800d1a8c = null;
    this._800cbe38 = null;
  }

  @Method(0x800e9018L)
  private int FUN_800e9018(final float x, final float y, final float z, final int a3) {
    int t2 = 0;

    //LAB_800e9040
    for(int i = 0; i < this.SomethingStructPtr_800d1a88.count_0c; i++) {
      final SomethingStructSub0c_1 a1 = this.SomethingStructPtr_800d1a88.ptr_14[i];
      if(a3 != 1 || a1.bool_01) {
        //LAB_800e9078
        //LAB_800e90a0
        boolean v0 = true;
        for(int n = 0; n < a1.count_00; n++) {
          final SomethingStructSub0c_2 a0 = this.SomethingStructPtr_800d1a88.ptr_18[a1._02 + n];

          if(a0.x_00 * x + a0.z_02 * z + a0._04 < 0) {
            //LAB_800e910c
            v0 = false;
            break;
          }
        }

        //LAB_800e90f0
        if(v0) {
          this.collisionPrimitiveIndices_800cbe48[t2] = i;
          t2++;
        }
      }

      //LAB_800e9104
    }

    //LAB_800e9114
    if(t2 == 0) {
      return -1;
    }

    if(t2 == 1) {
      return this.collisionPrimitiveIndices_800cbe48[0];
    }

    //LAB_800e9134
    float t0 = Float.MAX_VALUE;
    int t3 = -1;
    final Vector3f[] normals = this.SomethingStructPtr_800d1a88.normals_08;

    //LAB_800e9164
    for(int i = 0; i < t2; i++) {
      final int a3_0 = this.collisionPrimitiveIndices_800cbe48[i];
      final SomethingStructSub0c_1 t5 = this.SomethingStructPtr_800d1a88.ptr_14[a3_0];

      float v1 = -normals[a3_0].x * x - normals[a3_0].z * z - t5._08;

      if(normals[a3_0].y != 0) {
        v1 = v1 / normals[a3_0].y;
      } else {
        v1 = 0;
      }

      v1 -= y - 20;
      if(v1 > 0 && v1 < t0) {
        t3 = a3_0;
        t0 = v1;
      }

      //LAB_800e91ec
    }

    //LAB_800e91fc
    if(t0 == Float.MAX_VALUE) {
      //LAB_800e920c
      return -1;
    }

    //LAB_800e9210
    //LAB_800e9214
    return t3;
  }

  @Method(0x800e92dcL)
  private long get3dAverageOfSomething(final int index, final Vector3f out) {
    out.zero();

    final SomethingStruct ss = this.SomethingStructPtr_800d1a88;

    if(!this._800f7f14 || index < 0 || index >= ss.count_0c) {
      //LAB_800e9318
      return 0;
    }

    //LAB_800e932c
    final SomethingStructSub0c_1 ss2 = ss.ptr_14[index];

    final TmdObjTable1c.Primitive primitive = ss.getPrimitiveForOffset(ss2.primitivesOffset_04);
    final int packetOffset = ss2.primitivesOffset_04 - primitive.offset();
    final int packetIndex = packetOffset / (primitive.width() + 4);
    final int remainder = packetOffset % (primitive.width() + 4);
    final byte[] packet = primitive.data()[packetIndex];

    final int count = ss2.count_00;

    //LAB_800e937c
    for(int i = 0; i < count; i++) {
      out.add(ss.verts_04[IoHelper.readUShort(packet, remainder + 2 + i * 2)]);
    }

    //LAB_800e93e0
    out.div(count);
    return 0x1L;
  }

  /** TODO collision? */
  @Method(0x800e9430L) //TODO this is almost definitely wrong
  private int FUN_800e9430(final float x, final float y, final float z, final Vector3f playerMovement) {
    int a1;
    int s1;
    int s2;
    final int s4;
    final Vector3f sp0x28 = new Vector3f();

    if(this.smapLoadingStage_800cb430 != SubmapState.RENDER_SUBMAP_12) {
      return -1;
    }

    if(flEq(playerMovement.x, 0.0f) && flEq(playerMovement.z, 0.0f)) {
      return -1;
    }

    int s3 = 0;

    //LAB_800e94a4
    final int distanceMultiplier;
    if(playerMovement.x * playerMovement.x + playerMovement.z * playerMovement.z > 64.0f) {
      distanceMultiplier = 12;
    } else {
      //LAB_800e94e4
      distanceMultiplier = 4;
    }

    //LAB_800e94ec
    final float endX = x + playerMovement.x;
    final float endZ = z + playerMovement.z;
    final float t6 = y - 20;
    int t0 = 0;

    //LAB_800e9538
    for(int primitiveIndex = 0; primitiveIndex < this.SomethingStructPtr_800d1a88.count_0c; primitiveIndex++) {
      if(this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex].bool_01) {
        //LAB_800e9594
        boolean found = false;
        for(int i = 0; i < this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex].count_00; i++) {
          final SomethingStructSub0c_2 struct = this.SomethingStructPtr_800d1a88.ptr_18[this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex]._02 + i];

          if(struct.x_00 * x + struct.z_02 * z + struct._04 < 0) {
            //LAB_800e9604
            found = true;
            break;
          }
        }

        //LAB_800e95e8
        if(!found) {
          this.collisionPrimitiveIndices_800cbe48[t0] = primitiveIndex;
          t0++;
        }
      }

      //LAB_800e95fc
    }

    //LAB_800e960c
    if(t0 == 0) {
      s4 = -1;
    } else if(t0 == 1) {
      s4 = this.collisionPrimitiveIndices_800cbe48[0];
    } else {
      //LAB_800e962c
      float t1 = Float.MAX_VALUE;
      int t2 = -1;

      //LAB_800e965c
      for(int i = 0; i < t0; i++) {
        final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
        final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[primitiveIndex];
        final float v1 = (-normal.x * x - normal.z * z - this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex]._08) / normal.y - t6;

        if(v1 > 0 && v1 < t1) {
          t2 = primitiveIndex;
          t1 = v1;
        }

        //LAB_800e96e8
      }

      //LAB_800e96f8
      if(t1 != Float.MAX_VALUE) {
        s4 = t2;
      } else {
        //LAB_800e9708
        s4 = -1;
      }

      //LAB_800e970c
    }

    //LAB_800e9710
    if(s4 < 0) {
      final int primitiveIndex = this.FUN_800e8990(x, z);

      //LAB_800e975c
      //LAB_800e9764
      sp0x28.zero();

      if(!this._800f7f14 || primitiveIndex < 0 || primitiveIndex >= this.SomethingStructPtr_800d1a88.count_0c) {
        //LAB_800e9774
        final SomethingStructSub0c_1 ss2 = this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex];
        final TmdObjTable1c.Primitive primitive = this.SomethingStructPtr_800d1a88.getPrimitiveForOffset(ss2.primitivesOffset_04);
        final int packetOffset = ss2.primitivesOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        //LAB_800e97c4
        for(int i = 0; i < this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex].count_00; i++) {
          sp0x28.add(this.SomethingStructPtr_800d1a88.verts_04[IoHelper.readUShort(packet, remainder + 2 + i * 2)]);
        }

        //LAB_800e9828
        sp0x28.div(this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex].count_00);
      }

      //LAB_800e9870
      playerMovement.x = Math.round(sp0x28.x - x);
      playerMovement.z = Math.round(sp0x28.z - z);

      final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[primitiveIndex];
      playerMovement.y = (-normal.x * sp0x28.x - normal.z * sp0x28.z - this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex]._08) / normal.y;
    } else {
      //LAB_800e990c
      t0 = 0;

      //LAB_800e992c
      for(int n = 0; n < this.SomethingStructPtr_800d1a88.count_0c; n++) {
        if(this.SomethingStructPtr_800d1a88.ptr_14[n].bool_01) {
          //LAB_800e9988
          boolean found = false;
          for(int i = 0; i < this.SomethingStructPtr_800d1a88.ptr_14[n].count_00; i++) {
            final SomethingStructSub0c_2 struct = this.SomethingStructPtr_800d1a88.ptr_18[this.SomethingStructPtr_800d1a88.ptr_14[n]._02 + i];
            if(struct.x_00 * endX + struct.z_02 * endZ + struct._04 < 0) {
              //LAB_800e99f4
              found = true;
              break;
            }
          }

          //LAB_800e99d8
          if(!found) {
            this.collisionPrimitiveIndices_800cbe48[t0] = n;
            t0++;
          }
        }

        //LAB_800e99ec
      }

      //LAB_800e99fc
      if(t0 == 0) {
        s3 = -1;
      } else if(t0 == 1) {
        s3 = this.collisionPrimitiveIndices_800cbe48[0];
      } else {
        //LAB_800e9a1c
        float t1 = Float.MAX_VALUE;
        int t2 = -1;

        //LAB_800e9a4c
        for(int n = 0; n < t0; n++) {
          final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[n];
          final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[primitiveIndex];

          final float v1 = (-normal.x * endX - normal.z * endZ - this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex]._08) / normal.y - t6;
          if(v1 > 0 && v1 < t1) {
            t2 = primitiveIndex;
            t1 = v1;
          }

          //LAB_800e9ad4
        }

        //LAB_800e9ae4
        if(t1 != Float.MAX_VALUE) {
          s3 = t2;
        } else {
          //LAB_800e9af4
          s3 = -1;
        }
      }

      //LAB_800e9afc
      int v0 = -1;
      if(s3 >= 0) {
        final SomethingStructSub0c_1 struct = this.SomethingStructPtr_800d1a88.ptr_14[s3];

        //LAB_800e9b50
        for(s1 = 0; s1 < struct.count_00; s1++) {
          final SomethingStructSub0c_2 struct2 = this.SomethingStructPtr_800d1a88.ptr_18[struct._02 + s1];
          if(struct2._08 != 0) {
            if(Math.abs((struct2.x_00 * endX + struct2.z_02 * endZ + struct2._04) / 0x400) < 10) {
              v0 = s1;
              break;
            }
          }
        }
      }

      //LAB_800e9bbc
      //LAB_800e9bc0
      if(s3 >= 0 && v0 < 0) {
        final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[s3];
        final SomethingStructSub0c_1 struct = this.SomethingStructPtr_800d1a88.ptr_14[s3];

        if(Math.abs(y - (-normal.x * endX - normal.z * endZ - struct._08) / normal.y) < 50) {
          //LAB_800e9e64
          playerMovement.y = (-normal.x * (x + playerMovement.x) - normal.z * (z + playerMovement.z) - struct._08) / normal.y;

          //LAB_800ea390
          if(!this._800d1a8c._00) {
            this._800d1a8c._00 = true;
            //LAB_800ea3b4
            this._800d1a84 = MathHelper.floorMod(MathHelper.atan2(playerMovement.x, playerMovement.z) + MathHelper.PI, MathHelper.TWO_PI);
          }

          //LAB_800ea3e0
          return s3;
        }
      }

      //LAB_800e9c58
      if((this.getCollisionAndTransitionInfo(s4) & 0x20) != 0) {
        return -1;
      }

      //LAB_800e9ca0
      a1 = -1;
      for(int i = 1; i < 4; i++) {
        final float endX2 = x + playerMovement.x * i;
        final float endZ2 = z + playerMovement.z * i;

        //LAB_800e9ce8
        for(int a1_0 = 0; a1_0 < this.SomethingStructPtr_800d1a88.ptr_14[s4].count_00; a1_0++) {
          final SomethingStructSub0c_2 struct = this.SomethingStructPtr_800d1a88.ptr_18[this.SomethingStructPtr_800d1a88.ptr_14[s4]._02 + a1_0];

          if(struct._08 != 0) {
            if((struct.x_00 * endX2 + struct.z_02 * endZ2 + struct._04) / 0x400 <= 0) {
              a1 = a1_0;
              break;
            }
          }
        }

        //LAB_800e9d44
        //LAB_800e9d48
        if(a1 >= 0) {
          break;
        }
      }

      if(a1 >= 0) {
        //LAB_800e9e78
        s2 = s4;

        //LAB_800e9e7c
        final SomethingStructSub0c_2 struct = this.SomethingStructPtr_800d1a88.ptr_18[this.SomethingStructPtr_800d1a88.ptr_14[s4]._02 + a1];
        final float angle1 = MathHelper.atan2(endZ - z, endX - x);
        float angle2 = MathHelper.atan2(-struct.x_00, struct.z_02);
        float angleDeltaAbs = Math.abs(angle1 - angle2);
        if(angleDeltaAbs > MathHelper.PI) {
          angleDeltaAbs = MathHelper.TWO_PI - angleDeltaAbs;
        }

        //LAB_800e9f38
        // About 73 to 107 degrees (90 +- 17)
        final float baseAngle = MathHelper.PI / 2.0f; // 90 degrees
        final float deviation = 0.29670597283903602807702743064306f; // 17 degrees
        if(angleDeltaAbs >= baseAngle - deviation && angleDeltaAbs <= baseAngle + deviation) {
          return -1;
        }

        if(angleDeltaAbs > baseAngle) {
          if(angle2 > 0) {
            angle2 -= MathHelper.PI;
          } else {
            //LAB_800e9f6c
            angle2 += MathHelper.PI;
          }
        }

        //LAB_800e9f70
        if(!this._800cbe38._00) {
          this._800cbe38._00 = true;
        }

        final float angleDelta = angle2 - angle1;

        //LAB_800e9f98
        final int direction;
        if(angleDelta > 0 && angleDelta < MathHelper.PI / 2.0f || angleDelta < -MathHelper.PI) {
          //LAB_800e9fb4
          direction = 1;
        } else {
          direction = 0;
        }

        //LAB_800e9fbc
        final float angleStep;
        if(direction == 0) {
          angleStep = -0.09817477f; // 5.625 degrees
        } else {
          angleStep = 0.09817477f; // 5.625 degrees
        }

        //LAB_800e9fd0
        angle2 -= angleStep;

        //LAB_800e9ff4
        s1 = 8;
        float offsetX;
        float offsetZ;
        do {
          angle2 += angleStep;

          final float sin = MathHelper.sin(angle2);
          final float cos = MathHelper.cosFromSin(sin, angle2);
          offsetX = x + cos * distanceMultiplier;
          offsetZ = z + sin * distanceMultiplier;

          s1--;
          if(s1 <= 0) {
            break;
          }

          t0 = 0;

          //LAB_800ea064
          for(int i = 0; i < this.SomethingStructPtr_800d1a88.count_0c; i++) {
            final SomethingStructSub0c_1 a1_0 = this.SomethingStructPtr_800d1a88.ptr_14[i];

            if(a1_0.bool_01) {
              //LAB_800ea0c4
              boolean found = false;
              for(int n = 0; n < a1_0.count_00; n++) {
                final SomethingStructSub0c_2 a0_0 = this.SomethingStructPtr_800d1a88.ptr_18[a1_0._02 + n];
                if(a0_0.x_00 * offsetX + a0_0.z_02 * offsetZ + a0_0._04 < 0) {
                  //LAB_800ea130
                  found = true;
                  break;
                }
              }

              //LAB_800ea114
              if(!found) {
                this.collisionPrimitiveIndices_800cbe48[t0] = i;
                t0++;
              }
            }

            //LAB_800ea128
          }

          //LAB_800ea138
          if(t0 == 0) {
            s2 = -1;
          } else if(t0 == 1) {
            s2 = this.collisionPrimitiveIndices_800cbe48[0];
          } else {
            //LAB_800ea158
            float t1 = Float.MAX_VALUE;
            int t2 = -1;

            //LAB_800ea17c
            for(int i = 0; i < t0; i++) {
              final int primitiveIndex = this.collisionPrimitiveIndices_800cbe48[i];
              final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[primitiveIndex];

              final float v1_0 = (-normal.x * offsetX - normal.z * offsetZ - this.SomethingStructPtr_800d1a88.ptr_14[primitiveIndex]._08) / normal.y - t6;
              if(v1_0 > 0 && v1_0 < t1) {
                t2 = primitiveIndex;
                t1 = v1_0;
              }

              //LAB_800ea204
            }

            //LAB_800ea214
            if(t1 != Float.MAX_VALUE) {
              s2 = t2;
            } else {
              //LAB_800ea224
              s2 = -1;
            }
          }

          //LAB_800ea22c
        } while(s2 < 0);

        //LAB_800ea254
        if(s2 < 0) {
          return -1;
        }

        //LAB_800ea234
        final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[s2];

        if(Math.abs(y - (-normal.x * offsetX - normal.z * offsetZ - this.SomethingStructPtr_800d1a88.ptr_14[s2]._08) / normal.y) >= 50) {
          return -1;
        }

        playerMovement.y = (-normal.x * offsetX - normal.z * offsetZ - this.SomethingStructPtr_800d1a88.ptr_14[s2]._08) / normal.y;
        playerMovement.x = offsetX - x;
        playerMovement.z = offsetZ - z;

        return s2;
      }

      if(s3 < 0) {
        return -1;
      }

      final Vector3f normal = this.SomethingStructPtr_800d1a88.normals_08[s3];

      if(Math.abs(y - (-normal.x * endX - normal.z * endZ - this.SomethingStructPtr_800d1a88.ptr_14[s3]._08) / normal.y) >= 50) {
        return -1;
      }

      //LAB_800e9df4
      final SomethingStructSub0c_1 struct = this.SomethingStructPtr_800d1a88.ptr_14[s3];

      //LAB_800e9e64
      playerMovement.y = (-normal.x * (x + playerMovement.x) - normal.z * (z + playerMovement.z) - struct._08) / normal.y;
    }

    //LAB_800ea390
    if(!this._800d1a8c._00) {
      this._800d1a8c._00 = true;
      //LAB_800ea3b4
      this._800d1a84 = MathHelper.floorMod(MathHelper.atan2(playerMovement.x, playerMovement.z) + MathHelper.PI, MathHelper.TWO_PI);
    }

    //LAB_800ea3e0
    return s3;
  }

  @Method(0x800ea4c8L)
  private float FUN_800ea4c8(final float a0) {
    this._800d1a78--;

    if(this._800d1a78 > 0) {
      this._800d1a84 = this._800d1a80;

      if(!this._800d1a8c._00) {
        this._800d1a8c._00 = true;
      }
    }

    //LAB_800ea534
    //LAB_800ea538
    final boolean bool;
    if(this._800c6ae0 <= 0x400) {
      bool = true;
    } else if(this._800d1a8c._00) {
      bool = false;
    } else {
      bool = true;
      this._800d1a8c._00 = true;
    }

    //LAB_800ea570
    if(bool || this._800d1a7c != 0) {
      //LAB_800ea6d0
      //LAB_800ea6d4
      this._800d1a7c = 0;
      return a0;
    }

    final int s1 = (this._800c6ae0 - 1) % 4;
    final int s2 = this._800c6ae0 % 4;
    float s0 = this._800f7f6c[s1] - this._800d1a84;

    final boolean _800cbda4;
    if(Math.abs(s0) > MathHelper.PI) {
      _800cbda4 = s0 > 0;
      s0 = MathHelper.TWO_PI - Math.abs(s0);
    } else {
      //LAB_800ea628
      _800cbda4 = s0 <= 0;
      s0 = Math.abs(s0);
    }

    //LAB_800ea63c
    if(s0 > 0.125f * MathHelper.TWO_PI || this._800d1a78 > 0) {
      s0 /= 4.0f;
    }

    //LAB_800ea66c
    final float v1 = this._800f7f6c[s1];

    final float v0;
    if(!_800cbda4) {
      v0 = v1 - s0;
    } else {
      //LAB_800ea6a0
      v0 = v1 + s0;
    }

    //LAB_800ea6a4
    this._800f7f6c[s2] = v0;

    //LAB_800ea6dc
    return v0;
  }

  @Method(0x800ea84cL)
  private void FUN_800ea84c(final MediumStruct a0) {
    if(this.isScriptLoaded(0)) {
      if(a0._44) {
        index_80052c38 = this.sobjs_800c6880[0].innerStruct_00.ui_16c;

        //LAB_800ea8d4
        for(int i = 0; i < a0.count_40; i++) {
          if(index_80052c38 == a0.arr_00[i]) {
            a0._44 = false;
            break;
          }

          //LAB_800ea8ec
        }
      }
    }

    //LAB_800ea8fc
  }

  @Method(0x800ea90cL)
  private void FUN_800ea90c(final MediumStruct a0) {
    if(this.isScriptLoaded(0)) {
      index_80052c38 = this.sobjs_800c6880[0].innerStruct_00.ui_16c;
    }
  }

  @Method(0x800ea96cL)
  private void FUN_800ea96c(final MediumStruct a0) {
    // no-op
  }

  @Method(0x800ea974L)
  private MediumStruct FUN_800ea974(final int a0) {
    if(a0 < 0) {
      this._800d1a90.callback_48 = this::FUN_800ea96c;
    } else {
      //LAB_800ea9a4
      this._800d1a90.clear();

      final MediumStruct a2 = this._800d1a90;

      //LAB_800ea9d8
      for(int i = 0; i < 256; i++) {
        if(a0 != 0) {
          final Struct14_2 a3 = this._800f7f74[i];
          if(a3._04 == a0) {
            a2.arr_00[a2.count_40] = a3._06;
            a2.count_40++;
          }
        }

        //LAB_800eaa20
      }

      //LAB_800eaa30
      if(this._800d1a90.count_40 != 0) {
        this._800d1a90.callback_48 = this::FUN_800ea84c;
        this._800d1a90._44 = true;
      } else {
        //LAB_800eaa5c
        this._800d1a90.callback_48 = this::FUN_800ea90c;
      }

      //LAB_800eaa6c
    }

    //LAB_800eaa74
    return this._800d1a90;
  }

  @Method(0x800eddb4L)
  private void handleAndRenderSubmapModel() {
    if(this._800c6870 == -1) {
      this.submapModelLoadingStage_800f9e5a = -1;
    }

    //LAB_800ede14
    switch(this.submapModelLoadingStage_800f9e5a + 1) {
      case 0x0 -> {
        this.submapModel_800d4bf8.deleteModelParts();

        this._800d4bd0 = null;
        this._800d4bd4 = null;

        //LAB_800ee1b8
        this.submapCutModel = null;
        this.submapCutAnim = null;

        this.submapModelLoadingStage_800f9e5a = 0;

        //LAB_800ee1e4
      }

      case 0x1 -> {
        this._800d4bd0 = null;
        this._800d4bd4 = null;

        if(submapCut_80052c30 == 673) { // End cutscene
          this._800d4bd0 = new Structb0();
          this._800d4bd4 = new FileData(new byte[0x20]);

          this._800d4bd0._00 = 0;
          this._800d4bd0._02 = 0;
          this._800d4bd0._04 = 0;
          this._800d4bd0._06 = 0;
          this._800d4bd0._08 = 0;
          this._800d4bd0._0c = 0;
        }

        //LAB_800edeb4
        assert this.submapCutModel == null;
        assert this.submapCutAnim == null;
        assert this.submapCutTexture == null;
        assert this.submapCutMatrix == null;

        this.submapCutModelAndAnimLoaded_800d4bdc = false;
        this.submapTextureAndMatrixLoaded_800d4be0 = false;
        this.theEndTimLoaded_800d4be4 = false;
        this.theEndTim_800d4bf0 = null;

        final int fileIndex = this.smapFileIndices_800f982c[submapCut_80052c30];
        if(fileIndex != 0) {
          // File example: 7508
          loadDrgnDir(0, fileIndex, files -> {
            this.submapCutModelAndAnimLoaded_800d4bdc = true;

            this.submapCutModel = new CContainer("DRGN0/" + fileIndex, files.get(0));
            this.submapCutAnim = new TmdAnimationFile(files.get(1));
          });

          loadDrgnDir(0, fileIndex + 1, files -> {
            this.submapTextureAndMatrixLoaded_800d4be0 = true;

            this.submapCutTexture = new Tim(files.get(0));
            this.submapCutMatrix = new MV();

            final FileData matrixData = files.get(1);

            //TODO this might be transposed
            this.submapCutMatrix.m00 = matrixData.readShort(0) / (float)0x1000;
            this.submapCutMatrix.m10 = matrixData.readShort(2) / (float)0x1000;
            this.submapCutMatrix.m20 = matrixData.readShort(4) / (float)0x1000;
            this.submapCutMatrix.m01 = matrixData.readShort(6) / (float)0x1000;
            this.submapCutMatrix.m11 = matrixData.readShort(8) / (float)0x1000;
            this.submapCutMatrix.m21 = matrixData.readShort(10) / (float)0x1000;
            this.submapCutMatrix.m02 = matrixData.readShort(12) / (float)0x1000;
            this.submapCutMatrix.m12 = matrixData.readShort(14) / (float)0x1000;
            this.submapCutMatrix.m22 = matrixData.readShort(16) / (float)0x1000;

            for(int i = 0; i < 3; i++) {
              this.submapCutMatrix.transfer.setComponent(i, matrixData.readShort(18 + i * 2));
            }
          });

          if(submapCut_80052c30 == 673) { // End cutscene, loads "The End" TIM
            loadDrgnFile(0, 7610, data -> {
              this.theEndTimLoaded_800d4be4 = true;
              this.theEndTim_800d4bf0 = new Tim(data);
            });
          }
        }

        this.submapModelLoadingStage_800f9e5a++;
      }

      case 0x2 -> {
        if(this.submapCutModelAndAnimLoaded_800d4bdc && this.submapTextureAndMatrixLoaded_800d4be0) {
          GPU.uploadData15(new Rect4i(1008, 256, this.submapCutTexture.getImageRect().w, this.submapCutTexture.getImageRect().h), this.submapCutTexture.getImageData());

          // The submap cut model is rendered without using the camera matrix, so we multiply its transforms
          // by the inverse of the camera matrix to cancel out the camera multiplication in the shader
          final Matrix4f inverseW2s = new Matrix4f(worldToScreenMatrix_800c3548).setTranslation(worldToScreenMatrix_800c3548.transfer)
            .invert();
          this.submapCutMatrix_800d4bb0
            .set(this.submapCutMatrix).setTranslation(this.submapCutMatrix.transfer)
            .mul(inverseW2s);

          this.submapModelLoadingStage_800f9e5a++;
          this.submapModelLoaded_800c686c = true;

          this.submapCutTexture = null;
          this.submapCutMatrix = null;
        }
      }

      case 0x3 -> {
        if(submapCut_80052c30 == 673) { // End cutscene
          if(!this.theEndTimLoaded_800d4be4) {
            break;
          }

          this.FUN_800f4244(this.theEndTim_800d4bf0, this.tpage_800f9e5c, this.clut_800f9e5e, Translucency.B_PLUS_F);
          GPU.downloadData15(this._800d6b48, this._800d4bd4);
        }

        this.submapModelLoadingStage_800f9e5a++;
      }

      case 0x4 -> {
        this.submapModel_800d4bf8.vramSlot_9d = 0x91;

        initModel(this.submapModel_800d4bf8, this.submapCutModel, this.submapCutAnim);

        for(int i = 0; i < this.submapModel_800d4bf8.modelParts_00.length; i++) {
          this.submapModel_800d4bf8.modelParts_00[i].obj = TmdObjLoader.fromObjTable("Submap model part " + i, this.submapModel_800d4bf8.modelParts_00[i].tmd_08);
        }

        if(submapCut_80052c30 == 673) { // End cutscene
          this.FUN_800eef6c(this._800d6b48, this._800d4bd4, this._800d4bd0);
        }

        //LAB_800ee10c
        //LAB_800ee110
        this.submapModelLoadingStage_800f9e5a++;
      }

      case 0x5 -> {
        this.animateAndRenderSubmapModel(this.submapCutMatrix_800d4bb0);

        if(this._800d4bd0 != null && this._800d4bd4 != null) {
          this.FUN_800ee9e0(this._800d4bd4, this._800d4bd0, this.tpage_800f9e5c, this.clut_800f9e5e, Translucency.B_PLUS_F);
          GPU.uploadData15(this._800d6b48, this._800d4bd4);
        }
      }
    }

    //caseD_6
  }

  /** Used in Snow Field (disk 3) */
  @Method(0x800ee20cL)
  private void handleSnow() {
    if(this.submapEffectsState_800f9eac == -1) {
      this.snowLoadingStage_800f9e64 = -1;
    }

    //LAB_800ee234
    switch(this.snowLoadingStage_800f9e64) {
      case 0 -> {
        this.snow_800d4bd8 = new SnowEffect();
        this.snowLoadingStage_800f9e64++;
      }

      case 1 -> {
        if(this.allocateSnowEffect(this.snow_800d4bd8)) {
          //LAB_800ee2fc
          this.snowLoadingStage_800f9e64++;
        }
      }

      case 2 -> {
        SnowEffect snow = this.snow_800d4bd8.next_38;

        //LAB_800ee2d8
        int count;
        for(count = 0; snow != null; count++) {
          this.initSnowEffect(snow);
          snow = snow.next_38;
        }

        //LAB_800ee2f0
        if(count >= 256) {
          //LAB_800ee2fc
          this.snowLoadingStage_800f9e64++;
        }
      }

      case 3 -> this.renderSnowEffect(this.snow_800d4bd8);

      case -1 -> {
        if(this._800f9e60 != 0) {
          this.deallocateSnowEffect(this.snow_800d4bd8);
        }

        //LAB_800ee348
        this._800f9e60 = 0;
        this.snowLoadingStage_800f9e64 = 0;
      }
    }

    //LAB_800ee354
  }

  @Method(0x800ee368L)
  private void renderSnowEffect(final SnowEffect root) {
    SnowEffect snow = root.next_38;

    //LAB_800ee38c
    while(snow != null) {
      if(snow._00 == 0) {
        snow._00 = 1;
      }

      //LAB_800ee3a0
      if(snow._00 == 1) {
        if((snow.y_18 + 120 & 0xffff) < 241) {
          snow.xAccumulator_24 += snow.xStep_1c;
          snow.x_16 = (short)((snow.xAccumulator_24 >> 16) + (snow._10 * rsin(snow.angle_08) >> 12));

          final short x = snow.x_16;
          if(x < -192) {
            snow.x_16 = 192;
            snow.xAccumulator_24 = 0xc0_0000;
            snow.angle_08 = 0;
            //LAB_800ee42c
          } else if(x > 0xc0) {
            snow.x_16 = -192;
            snow.xAccumulator_24 = -0xc0_0000;
            snow.angle_08 = 0;
          }

          //LAB_800ee448
          snow.yAccumulator_28 += snow.yStep_20;
          snow.y_18 = (short)(snow.yAccumulator_28 >> 16);

          GPU.queueCommand(40, new GpuCommandQuad()
            .monochrome(snow.colour_34)
            .pos(snow.x_16, snow.y_18, snow.size_14, snow.size_14)
          );

          snow.angle_08 += snow.angleStep_0c;
          snow.angle_08 &= 0xfff;
        } else {
          //LAB_800ee52c
          this.wrapAroundSnowEffect(snow);
        }
      }

      //LAB_800ee534
      snow = snow.next_38;
    }

    //LAB_800ee544
  }

  @Method(0x800ee558L)
  private void initSnowEffect(final SnowEffect snow) {
    snow._00 = 1;
    snow.x_16 = (short)(rand() % 384 - 192 + this._800f9e6a);
    snow.y_18 = (short)(rand() % 240 - 120);

    final SnowStruct18 s1 = this.snowStuff_800d4d20;
    int a0 = s1._10;
    if(a0 == 0) {
      snow.xStep_1c = 0;
    } else {
      //LAB_800ee62c
      snow.xStep_1c = 0x20_0000 / (a0 - (simpleRand() * a0 / 2 >> 16));
    }

    //LAB_800ee644
    snow.xAccumulator_24 = snow.x_16 << 16;

    int s2 = 0;
    a0 = this._800f9e68;
    if(a0 < 35) {
      snow.size_14 = 3;
      //LAB_800ee66c
    } else if(a0 < 150) {
      snow.size_14 = 2;
      s2 = s1._14 * 0x5555 >> 16;
      //LAB_800ee6ac
    } else if(a0 < 256) {
      snow.size_14 = 1;
      s2 = s1._14 * 0x5555 >> 15;
    }

    //LAB_800ee6e8
    snow.colour_34 = 0xff;
    snow.yAccumulator_28 = snow.y_18 << 16;
    snow.yStep_20 = 0x20_0000 / (s1._14 + s2);
    snow._10 = s1._0c;
    final int angle = simpleRand() << 11 >> 16;
    s1.angle_04 = angle;
    snow.angle_08 = angle;

    if(s1._08 == 0) {
      snow.angleStep_0c = 0;
    } else {
      //LAB_800ee750
      snow.angleStep_0c = simpleRand() * s1._08 >> 16;
    }

    //LAB_800ee770
    this._800f9e68 = this._800f9e68 + 1 & 0xff;
    this._800f9e6a = this._800f9e6a + 1 & 0x0f;
  }

  /** Reuse snow effect when it reaches the bottom of the screen */
  @Method(0x800ee7b0L)
  private void wrapAroundSnowEffect(final SnowEffect snow) {
    snow._00 = 0;
    snow.x_16 = (short)(rand() % 384 - 192 + this._800f9e6e);
    snow.y_18 = (short)-120;

    final SnowStruct18 s1 = this.snowStuff_800d4d20;
    final int a0 = s1._10;
    if(a0 == 0) {
      snow.xStep_1c = 0;
    } else {
      //LAB_800ee84c
      snow.xStep_1c = 0x20_0000 / (a0 - (simpleRand() * a0 / 2 >> 16));
    }

    //LAB_800ee864
    snow.xAccumulator_24 = snow.x_16 << 16;
    snow.yAccumulator_28 = snow.y_18 << 16;
    snow.colour_34 = 0xd8;

    final int v1 = this._800f9e6c;
    int s2 = 0;
    if(v1 == 0 || v1 == 2 || v1 == 4) {
      //LAB_800ee890
      snow.size_14 = 1;
      s2 = s1._14 * 0x5555 >> 15;
      //LAB_800ee8c0
    } else if(v1 == 1) {
      snow.size_14 = 2;
      s2 = s1._14 * 0x5555 >> 16;
      //LAB_800ee8f4
    } else if(v1 == 3) {
      snow.size_14 = 3;
    }

    //LAB_800ee900
    //LAB_800ee904
    snow.yStep_20 = 0x20_0000 / (s1._14 + s2);
    snow._10 = s1._0c;
    final int v0 = simpleRand() << 11 >> 16;
    s1.angle_04 = v0;
    snow.angle_08 = v0;

    if(s1._08 == 0) {
      snow.angleStep_0c = 0;
    } else {
      //LAB_800ee968
      snow.angleStep_0c = simpleRand() * s1._08 >> 16;
    }

    //LAB_800ee988
    this._800f9e6c++;
    if(this._800f9e6c >= 6) {
      this._800f9e6c = 0;
    }

    //LAB_800ee9b4
    this._800f9e6e = this._800f9e6e + 1 & 0xf;
  }

  @Method(0x800ee9e0L)
  private void FUN_800ee9e0(final FileData a1, final Structb0 a2, final Vector2i tpage, final Vector2i clut, final Translucency transMode) {
    if(a2._08 == 500) {
      a2._00 = 1;
      a2._02 = 0;
      a2._06 = 1;
    }

    //LAB_800eea24
    if(a2._00 != 0) {
      if(a2._04 == 0) {
        if(a2._02 == 0) {
          a2._0c += 0x2_a800;

          if(a2._0c >>> 16 >= 0x100) {
            a2._0c = 0xff_0000;
            a2._02 = 1;
          }
        } else {
          //LAB_800eead8
          a2._0c -= 0x2_a800;

          if(a2._0c >>> 16 < 0x80) {
            a2._0c = 0x80_0000;
            a2._04 = 1;
          }
        }
      } else {
        //LAB_800eeb08
        a2._0c = 0x80_0000;
      }

      //LAB_800eeb0c
      GPU.queueCommand(40, new GpuCommandQuad()
        .vramPos(tpage.x, tpage.y >= 256 ? 256 : 0)
        .clut(clut.x, clut.y)
        .monochrome(a2._0c >> 16)
        .translucent(transMode)
        .pos(-188, 18, 192, 72)
        .uv(0, 128)
      );
    }

    //LAB_800eeb78
    if(a2._06 != 0) {
      this.FUN_800eec10(a1, a2);

      if(a2._08 == 561) {
        a2._06 = 0;
      }
    }

    //LAB_800eeba8
    //LAB_800eebac
    a2._08++;
  }

  @Method(0x800eec10L)
  private void FUN_800eec10(final FileData a1, final Structb0 a2) {
    //LAB_800eec1c
    for(int i = 0; i < 16; i++) {
      a2._50[i] += a2._10[i];

      final int v1 = a2._90[i];
      if(v1 < a2._50[i] >>> 16) {
        a2._50[i] = v1 << 16;
      }

      //LAB_800eec5c
      final int sp0 = a2._50[i] >> 16 << 10;
      final int sp2 = a2._50[i] >> 16 << 5;
      final int sp4 = a2._50[i] >> 16;
      a1.writeShort(i * 0x2, 0x8000 | sp0 | sp2 | sp4);
    }
  }

  @Method(0x800eece0L)
  private void animateAndRenderSubmapModel(final Matrix4f matrix) {
    this.submapModel_800d4bf8.coord2_14.coord.transfer.zero();
    this.submapModel_800d4bf8.coord2_14.transforms.rotate.zero();

    applyModelRotationAndScale(this.submapModel_800d4bf8);
    this.animateSmapModel(this.submapModel_800d4bf8);
    this.renderSubmapModel(this.submapModel_800d4bf8, matrix);
  }

  @Method(0x800eed44L)
  private SMapStruct3c FUN_800eed44(final SMapStruct3c a0) {
    final SMapStruct3c v0 = new SMapStruct3c();
    v0.parent_38 = a0.parent_38;
    a0.parent_38 = v0;
    return v0;
  }

  @Method(0x800eed84L)
  private void FUN_800eed84(final SMapStruct3c a0) {
    if(a0.parent_38 != null) {
      //LAB_800eeda8
      SMapStruct3c s0;
      do {
        final SMapStruct3c a0_0 = a0.parent_38;
        s0 = a0_0.parent_38;
        a0.parent_38 = s0;
      } while(s0 != null);
    }

    //LAB_800eedc8
  }

  @Method(0x800eee48L)
  private void renderSubmapModel(final Model124 model, final Matrix4f matrix) {
    zOffset_1f8003e8 = model.zOffset_a0;
    tmdGp0Tpage_1f8003ec = model.tpage_108;

    final MV lw = new MV();

    //LAB_800eee94
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 dobj2 = model.modelParts_00[i];

      GsGetLw(dobj2.coord2_04, lw);

      RENDERER.queueModel(dobj2.obj, matrix, lw)
        .screenspaceOffset(this.screenOffsetX_800cb568 + 8, -this.screenOffsetY_800cb56c)
        .lightDirection(lightDirectionMatrix_800c34e8)
        .lightColour(lightColourMatrix_800c3508)
        .backgroundColour(GTE.backgroundColour);
    }

    //LAB_800eef0c
  }

  @Method(0x800eef6cL)
  private void FUN_800eef6c(final Rect4i imageRect, final FileData imageAddress, final Structb0 a2) {
    //LAB_800eef94
    for(int i = 0; i < 16; i++) {
      //LAB_800eefac
      a2._90[i] = imageAddress.readUShort(i * 0x2) & 0x1f;
      a2._10[i] = (a2._90[i] << 16) / 60;
      a2._50[i] = 0;
      imageAddress.writeShort(i * 0x2, 0x8000);
    }

    GPU.uploadData15(imageRect, imageAddress);
  }

  @Method(0x800ef034L)
  private boolean allocateSnowEffect(final SnowEffect root) {
    SnowEffect current = root;

    //LAB_800ef04c
    for(int i = 0; i < 0x100; i++) {
      current = new SnowEffect();
      current.next_38 = root.next_38;
      root.next_38 = current;
    }

    return current != null;
  }

  @Method(0x800ef090L)
  private void deallocateSnowEffect(final SnowEffect a0) {
    SnowEffect s0 = a0.next_38;

    //LAB_800ef0b4
    while(s0 != null) {
      s0 = s0.next_38;
      a0.next_38 = s0;
    }
  }

  @Method(0x800ef0f8L)
  private void FUN_800ef0f8(final Model124 model, final BigSubStruct a1) {
    if(!flEq(a1._1e.x, model.coord2_14.coord.transfer.x) || !flEq(a1._1e.y, model.coord2_14.coord.transfer.y) || !flEq(a1._1e.z, model.coord2_14.coord.transfer.z)) {
      //LAB_800ef154
      if(a1._04 != 0) {
        if(a1._00 % a1._30 == 0) {
          final Struct20 a0 = this.FUN_800f03c0(this._800d4ec0);
          a0._00 = 0;
          a0._18 = (short)a1._38;

          final int size = a1.size_28;
          if(size < 0) {
            a0.scale_08 = -size;
            a0.scaleStep_04 = -a0.scaleStep_04 / 20.0f;
          } else if(size > 0) {
            //LAB_800ef1e0
            a0.scale_08 = 0.0f;
            a0.scaleStep_04 = size / 202.0f;
          } else {
            //LAB_800ef214
            a0.scale_08 = 0.0f;
            a0.scaleStep_04 = 0.0f;
          }

          //LAB_800ef21c
          a0.transfer.set(model.coord2_14.coord.transfer);
        }
      }

      //LAB_800ef240
      if(a1._08 != 0) {
        if(a1._00 % a1._34 == 0) {
          //LAB_800ef394
          final DustRenderData54 dust = this.addDust(this.dust_800d4e68);

          if(a1._10 != 0) {
            //LAB_800ef3e8
            dust.renderMode_00 = 2;
            dust.textureIndex_02 = 3;

            //LAB_800ef3f8
          } else {
            dust.renderMode_00 = 0;
            dust.textureIndex_02 = (short)a1._1c;

            final int v1 = a1._1c;
            if(v1 == 0) {
              a1._1c = 1;
              //LAB_800ef3f8
            } else if(v1 == 1) {
              //LAB_800ef3d8
              a1._1c = 0;
            }
          }

          //LAB_800ef3fc
          dust.x_18 = this.screenOffsetX_800cb568;
          dust.y_1c = this.screenOffsetY_800cb56c;
          dust._04 = 0;
          dust._06 = 150;

          final MV ls = new MV();
          GsGetLs(model.coord2_14, ls);
          GTE.setTransforms(ls);

          final int type = dust.textureIndex_02;
          if(type == 0) {
            //LAB_800ef4b4
            dust.z_4c = RotTransPers4(this._800d6b7c[4], this._800d6b7c[5], this._800d6b7c[6], this._800d6b7c[7], dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);
          } else if(type == 1) {
            //LAB_800ef484
            //LAB_800ef4b4
            dust.z_4c = RotTransPers4(this._800d6b7c[8], this._800d6b7c[9], this._800d6b7c[10], this._800d6b7c[11], dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);
          } else if(type == 3) {
            //LAB_800ef4a0
            //LAB_800ef4b4
            dust.z_4c = RotTransPers4(this._800d6b7c[0], this._800d6b7c[1], this._800d6b7c[2], this._800d6b7c[3], dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);
          }

          //LAB_800ef4ec
          if(dust.z_4c < 41) {
            dust.z_4c = 41;
          }

          //LAB_800ef504
          dust.colourStep_40 = 0x4_4444;
          dust.colourAccumulator_44 = 0x80_0000;
          dust.colour_48 = 0x80;
        }
      }

      //LAB_800ef520
      if(a1._0c != 0) {
        if(a1._00 % a1._30 == 0) {
          final DustRenderData54 dust = this.addDust(this.dust_800d4e68);
          dust.renderMode_00 = 1;
          dust.textureIndex_02 = 2;
          dust.x_18 = this.screenOffsetX_800cb568;
          dust.y_1c = this.screenOffsetY_800cb56c;

          final Vector3f vert0 = new Vector3f(-a1.size_28, 0.0f, -a1.size_28);
          final Vector3f vert1 = new Vector3f( a1.size_28, 0.0f, -a1.size_28);
          final Vector3f vert2 = new Vector3f(-a1.size_28, 0.0f,  a1.size_28);
          final Vector3f vert3 = new Vector3f( a1.size_28, 0.0f,  a1.size_28);

          dust._04 = 0;
          dust._06 = (short)a1._38;

          final MV ls = new MV();
          GsGetLs(model.coord2_14, ls);
          GTE.setTransforms(ls);

          //TODO The real code actually passes the same reference for sxyz 1 and 2, is that a bug?
          dust.z_4c = RotTransPers4(vert0, vert1, vert2, vert3, dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);

          if(dust.z_4c < 41) {
            dust.z_4c = 41;
          }

          //LAB_800ef6a0
          final float a0_0 = (dust.v3_38.x - dust.v0_20.x) / 2.0f;
          dust._08 = a0_0;
          dust._0c = a0_0 / a1._38;
          dust._10 = 0;

          dust.z0_26 = (dust.v3_38.x + dust.v0_20.x) / 2.0f;
          dust.z1_2e = (dust.v3_38.y + dust.v0_20.y) / 2.0f;

          dust.colourStep_40 = 0x80_0000 / a1._38;
          dust.colourAccumulator_44 = 0x80_0000;
          dust.colour_48 = 0x80;
        }
      }

      //LAB_800ef728
      if(a1._18 == 1) {
        if(this._800c6870 != -1) {
          this.FUN_800f0644(model, a1);
        }
      }
    }

    //LAB_800ef750
    a1._1e.set(model.coord2_14.coord.transfer);
    a1._00++;
  }

  @Method(0x800ef798L)
  private void FUN_800ef798() {
    Struct20 s1 = this._800d4ec0;
    Struct20 s0 = s1.next_1c;

    //LAB_800ef7c8
    while(s0 != null) {
      if(s0._00 >= s0._18) {
        s1.next_1c = s0.next_1c;
        s0 = s1.next_1c;
      } else {
        //LAB_800ef804
        s0.transfer.y--;

        this.dustModel_800d4d40.coord2_14.coord.transfer.set(s0.transfer);

        s0.scale_08 += s0.scaleStep_04;

        this.dustModel_800d4d40.coord2_14.transforms.scale.set(s0.scale_08, s0.scale_08, s0.scale_08);

        applyModelRotationAndScale(this.dustModel_800d4d40);
        this.renderSmapModel(this.dustModel_800d4d40);

        this.dustModel_800d4d40.remainingFrames_9e = 0;
        this.dustModel_800d4d40.interpolationFrameIndex = 0;
        this.dustModel_800d4d40.modelParts_00[0].coord2_04.flg--;
        s0._00++;

        s1 = s0;
        s0 = s0.next_1c;
      }

      //LAB_800ef888
    }

    //LAB_800ef894
  }

  @Method(0x800ef8acL)
  private void renderDust() {
    final int[] u = new int[4];
    System.arraycopy(this._800d6bdc, 0, u, 0, 4);

    final int[] v = new int[4];
    v[3] = 64; // Other values are 0

    //LAB_800ef9cc
    DustRenderData54 s1 = this.dust_800d4e68;
    DustRenderData54 s0 = s1.next_50;
    while(s0 != null) {
      if(s0._04 >= s0._06) {
        s1.next_50 = s0.next_50;
        s0 = s1.next_50;
      } else {
        //LAB_800efa08
        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        final int mode = s0.renderMode_00;
        if(mode == 0 || mode == 2) {
          //LAB_800efa44
          final int offsetX = this.screenOffsetX_800cb568 - s0.x_18;
          final int offsetY = this.screenOffsetY_800cb56c - s0.y_1c;

          cmd
            .pos(0, offsetX + s0.v0_20.x, offsetY + s0.v0_20.y)
            .pos(1, offsetX + s0.v1_28.x, offsetY + s0.v1_28.y)
            .pos(2, offsetX + s0.v2_30.x, offsetY + s0.v2_30.y)
            .pos(3, offsetX + s0.v3_38.x, offsetY + s0.v3_38.y);

          if(mode == 2) {
            cmd
              .clut(960, 464)
              .vramPos(960, 256)
              .bpp(Bpp.BITS_4)
              .translucent(Translucency.B_MINUS_F);
          } else {
            //LAB_800efb64
            cmd
              .clut((this.cluts_800d6068[7] & 0b111111) * 16, this.cluts_800d6068[7] >>> 6)
              .vramPos((this.texPages_800d6050[7] & 0b1111) * 64, (this.texPages_800d6050[7] & 0b10000) != 0 ? 256 : 0)
              .translucent(Translucency.of(this.texPages_800d6050[7] >>> 5 & 0b11))
              .bpp(Bpp.of(this.texPages_800d6050[7] >>> 7 & 0b11));
          }
        } else if(mode == 1) {
          //LAB_800efb7c
          s0._08 += s0._0c;
          s0._10 = s0._08;
          s0.v0_20.x = s0.z0_26 - s0._08 / 2.0f;
          s0.v0_20.y = s0.z1_2e - s0._08 / 2.0f;
          final float x = this.screenOffsetX_800cb568 - s0.x_18 + s0.v0_20.x;
          final float y = this.screenOffsetY_800cb56c - s0.y_1c + s0.v0_20.y;

          cmd
            .pos(0, x, y)
            .pos(1, x + s0._10, y)
            .pos(2, x, y + s0._10)
            .pos(3, x + s0._10, y + s0._10);

          if((s0._04 & 0x3) == 0) {
            s0.z1_2e--;
          }

          //LAB_800efc4c
          cmd
            .clut((this.cluts_800d6068[6] & 0b111111) * 16, this.cluts_800d6068[6] >>> 6)
            .vramPos((this.texPages_800d6050[6] & 0b1111) * 64, (this.texPages_800d6050[6] & 0b10000) != 0 ? 256 : 0)
            .translucent(Translucency.of(this.texPages_800d6050[6] >>> 5 & 0b11))
            .bpp(Bpp.of(this.texPages_800d6050[6] >>> 7 & 0b11));
        }

        //LAB_800efc64
        if(s0._04 >= this._800d6c0c[s0.renderMode_00]) {
          s0.colourAccumulator_44 -= s0.colourStep_40;

          final int colour = s0.colourAccumulator_44 >>> 16;
          if(colour >= 0x100) {
            s0.colour_48 = 0;
          } else {
            s0.colour_48 = colour;
          }
        }

        //LAB_800efcb8
        cmd
          .monochrome(s0.colour_48)
          .uv(0, u[s0.textureIndex_02], v[s0.textureIndex_02])
          .uv(1, u[s0.textureIndex_02] + this.smokeTextureWidths_800d6bec[s0.textureIndex_02], v[s0.textureIndex_02])
          .uv(2, u[s0.textureIndex_02], v[s0.textureIndex_02] + this.smokeTextureHeights_800d6bfc[s0.textureIndex_02])
          .uv(3, u[s0.textureIndex_02] + this.smokeTextureWidths_800d6bec[s0.textureIndex_02], v[s0.textureIndex_02] + this.smokeTextureHeights_800d6bfc[s0.textureIndex_02]);

        GPU.queueCommand(s0.z_4c, cmd);

        s0._04++;
        s1 = s0;
        s0 = s0.next_50;
      }

      //LAB_800efe48
    }

    //LAB_800efe54
  }

  @Method(0x800efe7cL)
  private void FUN_800efe7c() {
    SMapStruct3c s1 = this.struct3c_800d4f50;
    SMapStruct3c s0 = s1.parent_38;

    //LAB_800efecc
    while(s0 != null) {
      if(s0._06 < s0._02) {
        s1.parent_38 = s0.parent_38;
        s0 = s1.parent_38;
      } else {
        //LAB_800eff04
        final int colour = Math.max(s0._30 - s0._2c >> 16, 0);

        s0._14 += s0._1c;
        s0._24 += s0._20;
        s0.size_28 = (short)(s0._24 >> 16);
        s0._30 -= s0._2c;

        final int clut = this.cluts_800d6068[6];
        final int tpage = this.texPages_800d6050[6];

        final float x = this.screenOffsetX_800cb568 - s0.x_0c + s0._10 % 0x1_0000;
        final float y = this.screenOffsetY_800cb56c - s0.y_0e + s0._14 / 0x1_0000 - (s0._24 >> 16);

        //LAB_800eff7c
        GPU.queueCommand(40, new GpuCommandPoly(4)
          .bpp(Bpp.of(tpage >>> 7 & 0b11))
          .translucent(Translucency.of(tpage >>> 5 & 0b11))
          .clut((clut & 0b111111) * 16, clut >>> 6)
          .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
          .monochrome(colour)
          .pos(0, x, y)
          .pos(1, x + s0.size_28, y)
          .pos(2, x, y + s0.size_28)
          .pos(3, x + s0.size_28, y + s0.size_28)
          .uv(0, 64, 64)
          .uv(1, 95, 64)
          .uv(2, 64, 95)
          .uv(3, 95, 95)
        );

        s0._02++;
        s1 = s0;
        s0 = s0.parent_38;
      }
    }
  }

  @Method(0x800f00a4L)
  private void FUN_800f00a4() {
    if(this._800f9e74) {
      Struct34 s2 = this._800d4f18;
      Struct34 s1 = s2.parent_30;

      //LAB_800f0100
      while(s1 != null) {
        if(s1._08 >= s1._02) {
          if(s1._02 % s1._04 == 0) {
            //LAB_800f0148
            for(int i = 0; i < 4; i++) {
              final SMapStruct3c s0 = this.FUN_800eed44(this.struct3c_800d4f50);
              s0._02 = 0;
              s0._06 = s1._06;
              s0.x_0c = (short)this.screenOffsetX_800cb568;
              s0.y_0e = (short)this.screenOffsetY_800cb56c;
              s0._10 = s1.x_1c + (simpleRand() * s1._18 >> 16);
              s0._14 = s1.y_20 * 0x1_0000;
              s0._1c = -s1._0c;
              s0._20 = s1._14;
              s0._24 = s1._10;
              s0.size_28 = 0;
              s0._2c = 0x80_0000 / s0._06;
              s0._30 = 0x80_0000;
            }
          }

          //LAB_800f01ec
          s2 = s1;
          s1._02++;
          s1 = s1.parent_30;
        } else {
          //LAB_800f0208
          final Struct34 s0 = s1.parent_30;
          s2.parent_30 = s0;

          if(this._800d4f18.parent_30 == null) {
            this._800f9e74 = false;
          }

          s1 = s0;
        }
      }
    }

    //LAB_800f023c
    if(this._800f9e70 == 1) {
      final Struct24 s1 = this._800d4ee0;

      if(s1._02 % s1._04 == 0) {
        //LAB_800f0284
        for(int i = 0; i < 1; i++) {
          final SMapStruct3c s0 = this.FUN_800eed44(this.struct3c_800d4f50);
          s0._02 = 0;
          s0._06 = s1._06;
          s0.x_0c = (short)this.screenOffsetX_800cb568;
          s0.y_0e = (short)this.screenOffsetY_800cb56c;
          s0._10 = s1._1c + (simpleRand() * s1._18 >> 16);
          s0._14 = s1._20 << 16;
          s0._1c = -s1._0c;
          s0._20 = s1._14;
          s0._24 = s1._10;
          s0.size_28 = 0;
          s0._2c = 0x80_0000 / s0._06;
          s0._30 = 0x80_0000;
        }
      }

      //LAB_800f032c
      s1._02++;
    }

    //LAB_800f0344
  }

  @Method(0x800f0370L)
  private void FUN_800f0370() {
    initModel(this.dustModel_800d4d40, this.dustTmd, this.dustAnimation);
    this.dust_800d4e68.next_50 = null;
    this._800d4ec0.next_1c = null;
    this.FUN_800f0e60();
  }

  @Method(0x800f03c0L)
  private Struct20 FUN_800f03c0(final Struct20 a0) {
    final Struct20 v0 = new Struct20();
    v0.next_1c = a0.next_1c;
    a0.next_1c = v0;
    return v0;
  }

  @Method(0x800f0400L)
  private DustRenderData54 addDust(final DustRenderData54 parent) {
    final DustRenderData54 child = new DustRenderData54();
    child.next_50 = parent.next_50;
    parent.next_50 = child;
    return child;
  }

  @Method(0x800f0440L)
  private void FUN_800f0440() {
    this.FUN_800f058c();
    this.deallocateDust();
    this.FUN_800f0e7c();
  }

  @Method(0x800f047cL)
  private void FUN_800f047c() {
    this.FUN_800ef798();
    this.renderDust();
    this.FUN_800f0970();
  }

  @Method(0x800f04acL)
  private void FUN_800f04ac(final BigSubStruct a0) {
    a0._00 = 0;
    a0._04 = 0;
    a0._08 = 0;
    a0._0c = 0;
    a0._10 = 0;
    a0._18 = 0;
    a0._1c = 0;
    a0._1e.zero();
    a0.size_28 = 0;
    a0._2c = 0;
    a0._30 = 0;
    a0._34 = 0;
    a0._38 = 0;
    a0.ptr_3c = null;
  }

  @Method(0x800f0514L)
  private void FUN_800f0514() {
    final Struct34 v1 = this._800d4f18;

    if(v1.parent_30 != null) {
      //LAB_800f053c
      Struct34 s0;
      do {
        final Struct34 a0 = v1.parent_30;
        s0 = a0.parent_30;
        v1.parent_30 = s0;
      } while(s0 != null);
    }

    //LAB_800f055c
    this._800f9e74 = false;
    this.FUN_800eed84(this.struct3c_800d4f50);
    this._800f9e70 = 0;
  }

  @Method(0x800f058cL)
  private void FUN_800f058c() {
    final Struct20 v1 = this._800d4ec0;

    if(v1.next_1c != null) {
      //LAB_800f05b4
      Struct20 s0;
      do {
        final Struct20 a0 = v1.next_1c;
        s0 = a0.next_1c;
        v1.next_1c = s0;
      } while(s0 != null);
    }

    //LAB_800f05d4
  }

  @Method(0x800f05e8L)
  private void deallocateDust() {
    final DustRenderData54 v1 = this.dust_800d4e68;

    if(v1.next_50 != null) {
      //LAB_800f0610
      DustRenderData54 s0;
      do {
        final DustRenderData54 a0 = v1.next_50;
        s0 = a0.next_50;
        v1.next_50 = s0;
      } while(s0 != null);
    }

    //LAB_800f0630
  }

  @Method(0x800f0644L)
  private void FUN_800f0644(final Model124 model, final BigSubStruct a1) {
    if((a1._00 & 0x1) == 0) {
      final Struct18 s2 = a1.ptr_3c;

      if(s2._01 < s2._00) {
        final Struct34_2 s0 = this._800d4f90;
        final Struct34_2 s3 = new Struct34_2();
        s3.next_30 = s0.next_30;
        s0.next_30 = s3;

        final Struct14 s4 = this._800d4fd0;
        Struct14 s1 = new Struct14();
        s1.next_10 = s4.next_10;
        s4.next_10 = s1;

        final MV sp0x20 = new MV();
        GsGetLs(model.coord2_14, sp0x20);

        PushMatrix();
        GTE.setTransforms(sp0x20);
        GTE.perspectiveTransform(-s2.width_08, this._800d6c18.y, this._800d6c18.z);
        s1.vert0_00.x = GTE.getScreenX(2);
        s1.vert0_00.y = GTE.getScreenY(2);
        s3.z_20 = GTE.getScreenZ(3) / 4.0f;

        GTE.perspectiveTransform(s2.width_08, this._800d6c20.y, this._800d6c20.z);
        s1.vert1_08.x = GTE.getScreenX(2);
        s1.vert1_08.y = GTE.getScreenY(2);
        s3.z_20 = GTE.getScreenZ(3) / 4.0f;
        PopMatrix();

        s3._00 = 0;
        s3.tpage_04 = GetTPage(Bpp.BITS_4, Translucency.of(s2.translucency_0c), 972, 320);
        this.FUN_800f0df0(s2, s3);
        s1.vert0_00.x -= this.screenOffsetX_800cb568;
        s1.vert0_00.y -= this.screenOffsetY_800cb56c;
        s1.vert1_08.x -= this.screenOffsetX_800cb568;
        s1.vert1_08.y -= this.screenOffsetY_800cb56c;

        if(s2._01 == 0) {
          s2._14 = s1;
          s1 = new Struct14();
          s1.next_10 = s4.next_10;
          s4.next_10 = s1;
          final Struct14 v1 = s2._14;
          s1.vert0_00.set(v1.vert0_00);
          s1.vert1_08.set(v1.vert1_08);
        }

        //LAB_800f0928
        s3._28 = s1;
        s3._24 = s2._14;
        s2._14 = s1;
        s3._2c = s2;
        s2._01++;
      }
    }

    //LAB_800f094c
  }

  @Method(0x800f0970L)
  private void FUN_800f0970() {
    Struct14 a0;
    Struct34_2 s3 = this._800d4f90;
    Struct34_2 s1 = s3.next_30;

    //LAB_800f09c0
    while(s1 != null) {
      final Struct18 s2 = s1._2c;
      if(s2._06 >= s1._00) {
        final int tpage = s1.tpage_04;

        //LAB_800f0b04
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .translucent(Translucency.of(tpage >>> 5 & 0b11))
          .pos(0, this.screenOffsetX_800cb568 + s1._24.vert0_00.x, this.screenOffsetY_800cb56c + s1._24.vert0_00.y)
          .pos(1, this.screenOffsetX_800cb568 + s1._24.vert1_08.x, this.screenOffsetY_800cb56c + s1._24.vert1_08.y)
          .pos(2, this.screenOffsetX_800cb568 + s1._28.vert0_00.x, this.screenOffsetY_800cb56c + s1._28.vert0_00.y)
          .pos(3, this.screenOffsetX_800cb568 + s1._28.vert1_08.x, this.screenOffsetY_800cb56c + s1._28.vert1_08.y);

        final int r;
        final int g;
        final int b;
        if(s2._02 - 1 >= s1._00) {
          //LAB_800f0d0c
          r = s1.rAccumulator_14 >> 16;
          g = s1.gAccumulator_18 >> 16;
          b = s1.bAccumulator_1c >> 16;
        } else {
          s1.rAccumulator_14 -= s1.rStep_08;
          s1.gAccumulator_18 -= s1.gStep_0c;
          s1.bAccumulator_1c -= s1.bStep_10;
          r = Math.max(0, s1.rAccumulator_14 >> 16);
          g = Math.max(0, s1.gAccumulator_18 >> 16);
          b = Math.max(0, s1.bAccumulator_1c >> 16);
        }

        //LAB_800f0d18
        cmd.rgb(r, g, b);
        GPU.queueCommand(s1.z_20, cmd);

        s3 = s1;
        s1._00++;
        s1 = s1.next_30;
      } else {
        s2._01--;
        a0 = this._800d4fd0.next_10;
        Struct14 v0 = s1._24;
        Struct14 v1 = this._800d4fd0;

        //LAB_800f09fc
        while(a0 != null) {
          if(a0 == v0) {
            //LAB_800f0ae8
            v1.next_10 = a0.next_10;
            break;
          }

          v1 = a0;
          a0 = a0.next_10;
        }

        //LAB_800f0a18
        if(s2._01 == 0) {
          a0 = this._800d4fd0.next_10;
          v0 = s1._28;
          v1 = this._800d4fd0;

          //LAB_800f0a38
          while(a0 != null) {
            if(a0 == v0) {
              //LAB_800f0acc
              v0 = a0.next_10;
              v1.next_10 = v0;
              break;
            }

            v1 = a0;
            a0 = a0.next_10;
          }

          //LAB_800f0a54
        }

        //LAB_800f0a58
        s3.next_30 = s1.next_30;

        s1 = s3.next_30;
        if(s2._01 == 0) {
          if(this._800d4f90.next_30.next_30 == null) {
            //LAB_800f0aa4
            while(this._800d4fd0.next_10 != null) {
              a0 = this._800d4fd0.next_10;
              this._800d4fd0.next_10 = a0.next_10;
            }
          }
        }
      }

      //LAB_800f0d68
    }

    //LAB_800f0d74
    if(this._800f9e78 == 0 && this._800d4f90.next_30 == null) {
      //LAB_800f0da8
      while(this._800d4fd0.next_10 != null) {
        a0 = this._800d4fd0.next_10;
        this._800d4fd0.next_10 = a0.next_10;
      }
    }

    //LAB_800f0dc8
  }

  @Method(0x800f0df0L)
  private void FUN_800f0df0(final Struct18 a0, final Struct34_2 a1) {
    a1.rAccumulator_14 = a0.r_10 << 16;
    a1.gAccumulator_18 = a0.g_11 << 16;
    a1.bAccumulator_1c = a0.b_12 << 16;
    a1.rStep_08 = a1.rAccumulator_14 / a0._04;
    a1.gStep_0c = a1.gAccumulator_18 / a0._04;
    a1.bStep_10 = a1.bAccumulator_1c / a0._04;
  }

  @Method(0x800f0e60L)
  private void FUN_800f0e60() {
    this._800f9e78 = 0;
    this._800d4f90.next_30 = null;
    this._800d4fd0.next_10 = null;
  }

  @Method(0x800f0e7cL)
  private void FUN_800f0e7c() {
    final Struct34_2 s1 = this._800d4f90;
    if(s1.next_30 != null) {
      //LAB_800f0ea4
      Struct34_2 s0;
      do {
        final Struct34_2 a0 = s1.next_30;
        s0 = a0.next_30;
        s1.next_30 = s0;
      } while(s0 != null);
    }

    //LAB_800f0ec8
    final Struct14 s1_0 = this._800d4fd0;

    if(s1_0.next_10 != null) {
      //LAB_800f0edc
      Struct14 s0;
      do {
        final Struct14 a0 = s1_0.next_10;
        s0 = a0.next_10;
        s1_0.next_10 = s0;
      } while(s0 != null);
    }

    //LAB_800f0efc
    this.FUN_800f0fe8();
    this._800f9e78 = 0;
  }

  @Method(0x800f0f20L)
  private Struct18 FUN_800f0f20() {
    Struct18 v1 = null;

    //LAB_800f0f3c
    for(int a0 = 0; a0 < 8; a0++) {
      if(this._800f9e7c[a0] == null) {
        v1 = new Struct18();
        this._800f9e7c[a0] = v1;
        break;
      }
    }

    //LAB_800f0f78
    return v1;
  }

  @Method(0x800f0fe8L)
  private void FUN_800f0fe8() {
    for(int i = 0; i < 8; i++) {
      if(this._800f9e7c[i] != null) {
        this._800f9e7c[i] = null;
        this._800f9e78--;
      }
    }
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "An array of unknown struct data")
  @Method(0x800f1060L)
  private FlowControl FUN_800f1060(final RunningScript<?> script) {
    if(this._800d4fe8 != 0) {
      this._800d4fe8++;
      return FlowControl.CONTINUE;
    }

    final MV sp0x20 = new MV();
    final GsCOORDINATE2 sp0x40 = new GsCOORDINATE2();

    //LAB_800f10ac
    this.struct34_800d6018.parent_30 = null;
    GsInitCoordinate2(null, sp0x40);

    final Param ints = script.params_20[0];
    int s1 = 0;

    //LAB_800f10dc
    while(ints.array(s1).get() != -1) {
      final Struct34 struct = new Struct34();
      struct.parent_30 = this.struct34_800d6018.parent_30;
      this.struct34_800d6018.parent_30 = struct;

      sp0x40.coord.transfer.x = ints.array(s1++).get();
      sp0x40.coord.transfer.y = ints.array(s1++).get();
      sp0x40.coord.transfer.z = ints.array(s1++).get();
      GsGetLs(sp0x40, sp0x20);

      PushMatrix();
      GTE.setTransforms(sp0x20);
      GTE.perspectiveTransform(0, 0, 0);
      final float sx = GTE.getScreenX(2);
      final float sy = GTE.getScreenY(2);

      struct.sz3_2c = GTE.getScreenZ(3) / 4.0f;
      PopMatrix();

      struct._02 = 0;
      struct._04 = 17;
      struct._06 = 100;
      struct._08 = 0;
      struct._0a = 0;
      struct._0c = ints.array(s1++).get();
      struct._10 = ints.array(s1++).get();
      struct._14 = ints.array(s1++).get();
      struct._18 = (short)ints.array(s1++).get();
      struct.x_1c = sx;
      struct.y_20 = sy;
      struct.screenOffsetX_24 = this.screenOffsetX_800cb568;
      struct.screenOffsetY_28 = this.screenOffsetY_800cb56c;
    }

    //LAB_800f123c
    this._800d4fe8++;

    //LAB_800f1250
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p6")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p7")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p8")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p9")
  @Method(0x800f1274L)
  private FlowControl FUN_800f1274(final RunningScript<?> script) {
    this._800f9e74 = true;

    final Struct34 v1 = this._800d4f18;
    final Struct34 s1 = new Struct34();
    s1.parent_30 = v1.parent_30;
    v1.parent_30 = s1;

    final GsCOORDINATE2 sp0x48 = new GsCOORDINATE2();
    GsInitCoordinate2(null, sp0x48);

    sp0x48.coord.transfer.set(script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    final MV sp0x28 = new MV();
    GsGetLs(sp0x48, sp0x28);

    PushMatrix();
    GTE.setTransforms(sp0x28);
    GTE.perspectiveTransform(0, 0, 0);

    final float sx = GTE.getScreenX(2);
    final float sy = GTE.getScreenY(2);
    final float sz = GTE.getScreenZ(3) / 4.0f;
    PopMatrix();

    if(script.params_20[2].get() < script.params_20[1].get()) {
      script.params_20[2].add(script.params_20[1].get());
    }

    //LAB_800f13f0
    s1._02 = 0;
    s1._04 = (short)script.params_20[0].get();
    s1._06 = (short)script.params_20[1].get();
    s1._08 = (short)script.params_20[2].get();
    s1._0a = 0;
    s1._0c = (script.params_20[6].get() << 16) / script.params_20[1].get();
    s1._10 = script.params_20[8].get() << 16;
    s1._14 = (script.params_20[9].get() << 16) / script.params_20[1].get();
    s1._18 = (short)script.params_20[7].get();
    s1.x_1c = sx;
    s1.y_20 = sy;
    s1.screenOffsetX_24 = this.screenOffsetX_800cb568;
    s1.screenOffsetY_28 = this.screenOffsetY_800cb56c;
    s1.sz3_2c = sz;

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p5")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p6")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p7")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p8")
  @Method(0x800f14f0L)
  private FlowControl FUN_800f14f0(final RunningScript<?> script) {
    final int v1 = script.params_20[0].get();
    this._800f9e70 = v1;

    final Struct24 a1 = this._800d4ee0;
    if(v1 != 0) {
      a1._02 = 0;
      a1._04 = (short)script.params_20[1].get();

      if(script.params_20[2].get() == 0) {
        script.params_20[2].set(1);
      }

      //LAB_800f154c
      a1._06 = (short)script.params_20[2].get();
      a1._1c = script.params_20[3].get();
      a1._20 = script.params_20[4].get();
      a1._18 = (short)script.params_20[5].get();
      a1._0c = (script.params_20[6].get() << 16) / script.params_20[2].get();
      a1._10 = script.params_20[7].get() << 16;
      a1._14 = (script.params_20[8].get() << 16) / script.params_20[2].get();

      //LAB_800f15fc
    } else {
      a1._04 = 0;
      a1._02 = 0;
      a1._06 = 0;
      a1._0c = 0;
      a1._10 = 0;
      a1._14 = 0;
      a1._18 = 0;
      a1._1c = 0;
      a1._20 = 0;
    }

    //LAB_800f162c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "translucency")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b")
  @Method(0x800f1634L)
  private FlowControl FUN_800f1634(final RunningScript<?> script) {
    final ScriptState<?> state = script.scriptState_04;

    script.params_20[9] = new ScriptStorageParam(state, 0);

    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[state.storage_44[0]].innerStruct_00;
    if(script.params_20[0].get() == 0 || this._800f9e78 >= 8) {
      //LAB_800f1698
      sobj._1d0._18 = 0;
    } else {
      //LAB_800f16a4
      final Struct18 a1 = this.FUN_800f0f20();
      a1._00 = script.params_20[1].get();
      a1._01 = 0;
      a1._02 = (short)script.params_20[2].get();
      a1._04 = (short)script.params_20[3].get();
      a1._06 = (short)(a1._02 + a1._04);
      a1.width_08 = (short)script.params_20[4].get();
      a1.translucency_0c = script.params_20[5].get();
      a1.r_10 = script.params_20[6].get();
      a1.g_11 = script.params_20[7].get();
      a1.b_12 = script.params_20[8].get();
      a1._14 = null;
      sobj._1d0._18 = script.params_20[0].get();
      sobj._1d0.ptr_3c = a1;
      this._800f9e78++;
    }

    //LAB_800f1784
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a save point to the map")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "hasSavePoint", description = "True to add a savepoint, false otherwise")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800f179cL)
  private FlowControl scriptAddSavePoint(final RunningScript<?> script) {
    final Vector2f sp0x48 = new Vector2f();
    final Vector2f sp0x50 = new Vector2f();
    final GsCOORDINATE2 coord2 = new GsCOORDINATE2();

    this.hasSavePoint_800d5620 = script.params_20[0].get() != 0;
    GsInitCoordinate2(null, coord2);

    coord2.coord.transfer.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    this.savePointPos_800d5622.set(coord2.coord.transfer);

    final MV screenMatrix = new MV();
    GsGetLs(coord2, screenMatrix);
    PushMatrix();

    GTE.setTransforms(screenMatrix);

    //LAB_800f195c
    for(int s3 = 0; s3 < 2; s3++) {
      final SavePointRenderData44 struct = this.savePoint_800d5598[s3];

      struct.z_40 = RotTransPers4(this.savePointV0_800d6c28, this.savePointV1_800d6c30, this.savePointV2_800d6c38, this.savePointV3_800d6c40, struct.vert0_00, struct.vert1_08, struct.vert2_10, struct.vert3_18);

      if(s3 == 0) {
        perspectiveTransform(this._800d6c48, sp0x48);
        perspectiveTransform(this._800d6c50, sp0x50);

        sp0x48.x = sp0x50.y - sp0x48.y;
      }

      //LAB_800f1a34
      final float a3 = (struct.vert0_00.x + struct.vert3_18.x) / 2.0f;
      final float t0 = (struct.vert0_00.y + struct.vert3_18.y) / 2.0f;
      final float a2 = (struct.vert0_00.x - struct.vert3_18.x) / 2.0f;

      final float x0 = a3 - a2;
      struct.vert0_00.x = x0;
      struct.vert2_10.x = x0;
      final float y0 = t0 - a2 - sp0x48.x;
      struct.vert0_00.y = y0;
      struct.vert1_08.y = y0;
      final float x1 = a3 + a2;
      struct.vert1_08.x = x1;
      struct.vert3_18.x = x1;
      final float y1 = t0 + a2 - sp0x48.x;
      struct.vert2_10.y = y1;
      struct.vert3_18.y = y1;

      //LAB_800f1b04
      struct.screenOffsetX_20 = this.screenOffsetX_800cb568;
      struct.screenOffsetY_24 = this.screenOffsetY_800cb56c;
    }

    PopMatrix();

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, adds triangle indicators (possibly for doors)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "The struct data")
  @Method(0x800f1b64L)
  private FlowControl FUN_800f1b64(final RunningScript<?> script) {
    final Vector3f sp0x10 = new Vector3f();
    final GsCOORDINATE2 sp0x18 = new GsCOORDINATE2();
    final MV sp0x70 = new MV();

    GsInitCoordinate2(null, sp0x18);

    final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;

    //LAB_800f1ba8
    final Param ints = script.params_20[0];
    int s0 = 0;
    for(int i = 0; ints.array(s0).get() != -1; i++) {
      this.get3dAverageOfSomething(ints.array(s0++).get(), sp0x10);

      sp0x18.coord.transfer.set(sp0x10);
      GsGetLs(sp0x18, sp0x70);

      PushMatrix();
      GTE.setTransforms(sp0x70);
      GTE.perspectiveTransform(0, 0, 0);
      final float sx = GTE.getScreenX(2);
      final float sy = GTE.getScreenY(2);
      PopMatrix();

      indicator._18[i] = (short)ints.array(s0++).get();
      indicator.x_40[i] = sx + ints.array(s0++).get();
      indicator.y_68[i] = sy + ints.array(s0++).get();
      indicator.screenOffsetX_90[i] = this.screenOffsetX_800cb568;
      indicator.screenOffsetY_e0[i] = this.screenOffsetY_800cb56c;
    }

    //LAB_800f1cf0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, adds a triangle indicator (possibly for a door)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y")
  @Method(0x800f1d0cL)
  private FlowControl FUN_800f1d0c(final RunningScript<?> script) {
    final GsCOORDINATE2 sp0x40 = new GsCOORDINATE2();
    GsInitCoordinate2(null, sp0x40);
    final Vector3f sp0x10 = new Vector3f();
    this.get3dAverageOfSomething(script.params_20[0].get(), sp0x10);
    sp0x40.coord.transfer.set(sp0x10);
    final MV sp0x20 = new MV();
    GsGetLs(sp0x40, sp0x20);

    PushMatrix();
    GTE.setTransforms(sp0x20);
    GTE.perspectiveTransform(0, 0, 0);
    final float sx = GTE.getScreenX(2);
    final float sy = GTE.getScreenY(2);
    PopMatrix();

    //LAB_800f1e20
    for(int i = 0; i < 20; i++) {
      final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;

      if(indicator._18[i] == -1) {
        indicator._18[i] = (short)script.params_20[1].get();
        indicator.x_40[i] = sx + script.params_20[2].get();
        indicator.y_68[i] = sy + script.params_20[3].get();
        indicator.screenOffsetX_90[i] = this.screenOffsetX_800cb568;
        indicator.screenOffsetY_e0[i] = this.screenOffsetY_800cb56c;
        break;
      }
    }

    //LAB_800f1ea0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f1eb8L)
  private FlowControl FUN_800f1eb8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @Method(0x800f1f9cL)
  private FlowControl FUN_800f1f9c(final RunningScript<?> script) {
    final SubmapObject210 sobj = SCRIPTS.getObject(script.params_20[0].get(), SubmapObject210.class);
    sobj._1d0._04 = script.params_20[1].get();
    sobj._1d0.size_28 = script.params_20[2].get();
    sobj._1d0._30 = script.params_20[3].get();

    if(script.params_20[4].get() == 0) {
      sobj._1d0._38 = 1;
    } else {
      sobj._1d0._38 = script.params_20[4].get();
    }

    //LAB_800f2018
    if(sobj._1d0._04 != 1) {
      sobj._1d0._1e.zero();
      sobj._1d0.size_28 = 1;
      sobj._1d0._30 = 0;
      sobj._1d0._38 = 0;
    }

    //LAB_800f2040
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, self version of FUN_800f1f9c")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @Method(0x800f2048L)
  private FlowControl FUN_800f2048(final RunningScript<?> script) {
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return this.FUN_800f1f9c(script);
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "An array of struct data")
  @Method(0x800f2090L)
  private FlowControl FUN_800f2090(final RunningScript<?> script) {
    final Param ints = script.params_20[0];
    int a0 = 0;
    Struct34 a1 = this.struct34_800d6018.parent_30;
    Struct34 sp30 = null;

    //LAB_800f20a8
    while(a1 != null) {
      final Struct34 v1 = a1.parent_30;
      a1.parent_30 = sp30;
      sp30 = a1;
      a1 = v1;
    }

    //LAB_800f20c4
    a1 = sp30;

    //LAB_800f20e4
    //TODO I'm not sure this is right
    while(a1 != null && ints.array(a0).get() != -2) {
      if(ints.array(a0).get() == -1) {
        a0++;
        a1._04 = 17;
        a1._06 = 100;
        a1._08 = 0;
        a1._0a = 0;
      } else {
        //LAB_800f2108
        a1._08 = (short)ints.array(a0++).get();
        a1._0a = (short)ints.array(a0++).get();
        a1._04 = (short)ints.array(a0++).get();
        a1._06 = (short)ints.array(a0++).get();
      }

      //LAB_800f2138
      a1._00 = (short)ints.array(a0++).get();
      a1 = a1.parent_30;
    }

    a1 = sp30;

    //LAB_800f2164
    sp30 = null;

    //LAB_800f2170
    while(a1 != null) {
      final Struct34 v1 = a1.parent_30;
      a1.parent_30 = sp30;
      sp30 = a1;
      a1 = v1;
    }

    //LAB_800f218c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialized parameters for snow")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @Method(0x800f2198L)
  private FlowControl FUN_800f2198(final RunningScript<?> script) {
    final short a1 = (short)script.params_20[0].get();
    final SnowStruct18 a2 = this.snowStuff_800d4d20;

    a2._02 = a1;

    if(a1 == 0) {
      this._800f9e60 = 0;
      //LAB_800f21d0
    } else if(a1 == 1) {
      this._800f9e60 = 1;

      if(script.params_20[2].get() < 0) {
        script.params_20[2].neg();
      }

      //LAB_800f2210
      a2.angle_04 = 0;
      a2._08 = script.params_20[4].get();
      a2._0c = script.params_20[3].get();
      a2._10 = script.params_20[1].get();
      a2._14 = script.params_20[2].get();
      //LAB_800f2250
    } else if(a1 == 2) {
      this._800f9e60 = 2;
    }

    //LAB_800f225c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f2264L)
  private FlowControl FUN_800f2264(final RunningScript<?> script) {
    final ScriptState<?> sobj1 = script.scriptState_04;
    script.params_20[1] = new ScriptStorageParam(sobj1, 0); // Unused? Why?

    final SubmapObject210 sobj2 = (SubmapObject210)scriptStatePtrArr_800bc1c0[sobj1.storage_44[0]].innerStruct_00; // Storage 0 is my script index, isn't this just getting the same state?
    if((script.params_20[0].get() - 1 & 0xffff_ffffL) < 2) {
      sobj2._1d0._08 = 1;
      sobj2._1d0._10 = 0;
      sobj2._1d0._34 = 9;
    } else {
      //LAB_800f22b8
      sobj2._1d0._08 = 0;
    }

    //LAB_800f22bc
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "sobjIndex", description = "The SubmapObject210 script index")
  @Method(0x800f22c4L)
  private FlowControl FUN_800f22c4(final RunningScript<?> script) {
    script.params_20[2] = new ScriptStorageParam(script.scriptState_04, 0);
    final SubmapObject210 a0 = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[2].get()].innerStruct_00;

    int v1 = script.params_20[0].get();
    a0._1d0._10 = v1;
    if(v1 == 0) {
      v1 = script.params_20[1].get();

      if(v1 == 0) {
        a0._1d0._08 = 0;
        a0._1d0._34 = 0;
        //LAB_800f2328
      } else if(v1 == 1) {
        a0._1d0._08 = v1;
        a0._1d0._34 = a0._1d0._2c;
      }
      //LAB_800f2340
    } else if(v1 == 1) {
      a0._1d0._08 = 1;
      a0._1d0._2c = a0._1d0._34;
      a0._1d0._34 = script.params_20[1].get();

      if(a0._1d0._34 == 0) {
        a0._1d0._34 = 1;
      }
    }

    //LAB_800f2374
    if(a0._1d0._10 >= 2) {
      a0._1d0._08 = 0;
      a0._1d0._1e.zero();
    }

    //LAB_800f2398
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f23a0L)
  private FlowControl FUN_800f23a0(final RunningScript<?> script) {
    script.params_20[1] = new ScriptStorageParam(script.scriptState_04, 0);

    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.scriptState_04.storage_44[0]].innerStruct_00;

    final int v0 = script.params_20[0].get();

    if(v0 != 0) {
      sobj._1d0._34 = v0;
    } else {
      sobj._1d0._34 = 1;
    }

    //LAB_800f23e4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @Method(0x800f23ecL)
  private FlowControl FUN_800f23ec(final RunningScript<?> script) {
    script.params_20[4] = new ScriptStorageParam(script.scriptState_04, 0); //TODO Does nothing, why?
    final int a2 = script.params_20[0].get();
    final SubmapObject210 a1 = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.scriptState_04.storage_44[0]].innerStruct_00;

    if(a2 == 1 || a2 == 3) {
      //LAB_800f2430
      a1._1d0._0c = 1;
      a1._1d0.size_28 = script.params_20[1].get();
      a1._1d0._30 = script.params_20[2].get();
      a1._1d0._38 = script.params_20[3].get();

      if(a1._1d0._38 == 0) {
        a1._1d0._38++;
      }
    } else {
      //LAB_800f2484
      a1._1d0._0c = 0;
    }

    //LAB_800f2488
    if(a1._1d0._0c != 1) {
      a1._1d0._0c = 0;
      a1._1d0._1e.zero();
    }

    //LAB_800f24a8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f24b0L)
  private FlowControl FUN_800f24b0(final RunningScript<?> script) {
    if(script.params_20[0].get() == 1) {
      this._800f9e70 = 2;
    }

    //LAB_800f24d0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f24d8L)
  private FlowControl FUN_800f24d8(final RunningScript<?> script) {
    if(script.params_20[0].get() != 0) {
      this._800f9e70 = 0;
    }

    //LAB_800f24fc
    if(this._800f9e70 == 0) {
      final Struct24 v0 = this._800d4ee0;
      v0._02 = 0;
      v0._04 = 0;
      v0._06 = 0;
      v0._0c = 0;
      v0._10 = 0;
      v0._14 = 0;
      v0._18 = 0;
      v0._1c = 0;
      v0._20 = 0;
      this.FUN_800eed84(this.struct3c_800d4f50);
    }

    //LAB_800f2544
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800f2554L)
  private FlowControl FUN_800f2554(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f25a8L)
  private FlowControl FUN_800f25a8(final RunningScript<?> script) {
    final ScriptState<?> v1 = script.scriptState_04;
    script.params_20[1] = new ScriptStorageParam(v1, 0);

    if(script.params_20[0].get() == 1) {
      this.FUN_800f0e7c();
      final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[v1.storage_44[0]].innerStruct_00;
      sobj._1d0._18 = 0;
    }

    //LAB_800f2604
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates triangle indicators from an array of struct data")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT_ARRAY, name = "data", description = "The struct data")
  @Method(0x800f2618L)
  private FlowControl scriptAllocateTriangleIndicatorArray(final RunningScript<?> script) {
    final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;

    //LAB_800f266c
    int i = 0;
    final Param a0 = script.params_20[0];
    for(int a1 = 0; a0.array(i).get() != -1; a1++) {
      indicator._18[a1] = (short)a0.array(i++).get();
      indicator.x_40[a1] = a0.array(i++).get() + this.screenOffsetX_800cb568;
      indicator.y_68[a1] = a0.array(i++).get() + this.screenOffsetY_800cb56c;
      indicator.screenOffsetX_90[a1] = this.screenOffsetX_800cb568;
      indicator.screenOffsetY_e0[a1] = this.screenOffsetY_800cb56c;
    }

    //LAB_800f26b4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @Method(0x800f26c8L)
  private FlowControl FUN_800f26c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("No-op")
  @Method(0x800f2780L)
  private FlowControl FUN_800f2780(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800f2788L)
  private void initSavePoint() {
    initModel(this.savePointModel_800d5eb0, this.savepointTmd, this.savepointAnimation);
    this.savePoint_800d5598[0].rotation_28 = 0.0f;
    this.savePoint_800d5598[0].colour_34 = 0.3125f;
    this.savePoint_800d5598[1].rotation_28 = 0.0f;
    this.savePoint_800d5598[1].fadeAmount_2c = 0.0077f;
    this.savePoint_800d5598[1].colour_34 = 0.0f;
    this.savePoint_800d5598[1].fadeState_38 = 0;

    //LAB_800f285c
    for(int i = 0; i < 8; i++) {
      final SavePointRenderData44 struct0 = this.savePoint_800d5630[i * 4];
      final SavePointRenderData44 struct1 = this.savePoint_800d5630[i * 4 + 1];
      final SavePointRenderData44 struct2 = this.savePoint_800d5630[i * 4 + 2];
      final SavePointRenderData44 struct3 = this.savePoint_800d5630[i * 4 + 3];
      struct0.colour_34 = 0.5f;
      struct0.fadeAmount_2c = 0.0078f;
      struct0.fadeState_38 = 0;
      struct0.rotation_28 = MathHelper.psxDegToRad(this._800d6c58[i]);
      struct1.colour_34 = 0.375f;
      struct2.colour_34 = struct0.colour_34 - 0.25f;
      struct3.colour_34 = struct0.colour_34 - 0.375f;
    }
  }

  @Method(0x800f28d8L)
  private void renderSavePoint() {
    float minX = 0.0f;
    float maxX = 0.0f;
    float minY = 0.0f;
    float maxY = 0.0f;

    final Model124 model = this.savePointModel_800d5eb0;
    model.coord2_14.transforms.scale.set(1.5f, 3.0f, 1.5f);
    model.coord2_14.coord.transfer.set(this.savePointPos_800d5622);

    applyModelRotationAndScale(model);
    this.animateSmapModel(model);
    this.renderSmapModel(model);

    GPU.queueCommand(1, new GpuCommandCopyVramToVram(984, 288 + this._800f9ea0, 992, 288, 8, 64 - this._800f9ea0));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(984, 288, 992, 352 - this._800f9ea0, 8, this._800f9ea0));

    this._800f9ea0 = this._800f9ea0 + 1 & 0x3f;

    //LAB_800f2a44
    // This loop renders the central circle
    for(int i = 0; i < 2; i++) {
      final SavePointRenderData44 s0 = this.savePoint_800d5598[i];

      final int offsetX = this.screenOffsetX_800cb568 - s0.screenOffsetX_20;
      final int offsetY = this.screenOffsetY_800cb56c - s0.screenOffsetY_24;

      final float x0 = offsetX + s0.vert0_00.x;
      final float y0 = offsetY + s0.vert0_00.y;
      final float x1 = offsetX + s0.vert1_08.x;
      final float y1 = offsetY + s0.vert1_08.y;
      final float x2 = offsetX + s0.vert2_10.x;
      final float y2 = offsetY + s0.vert2_10.y;
      final float x3 = offsetX + s0.vert3_18.x;
      final float y3 = offsetY + s0.vert3_18.y;

      if(i == 0) {
        minX = x0;
        minY = y0;
        maxX = x3;
        maxY = y3;
      }

      //LAB_800f2af8
      if(i == 1) {
        if(s0.fadeState_38 == 0) {
          //LAB_800f2b44
          s0.colour_34 += s0.fadeAmount_2c;

          if(s0.colour_34 > 0.5f) {
            s0.colour_34 = 0.5f;
            s0.fadeState_38 = 1;
          }
        } else {
          s0.colour_34 -= s0.fadeAmount_2c;

          if(s0.colour_34 < 0.0f) {
            s0.colour_34 = 0.0f;
            s0.fadeState_38 = 0;
          }
        }
      }

      //LAB_800f2b80
      if(s0.z_40 == 0) {
        s0.z_40++;
      }

      GPU.queueCommand(s0.z_40, new GpuCommandPoly(4)
        .bpp(Bpp.of(this.texPages_800d6050[5] >>> 7 * 0b11))
        .translucent(Translucency.B_PLUS_F)
        .monochrome((int)(s0.colour_34 * 255.0f))
        .clut((this.cluts_800d6068[5] & 0b111111) * 16, this.cluts_800d6068[5] >>> 6)
        .vramPos((this.texPages_800d6050[5] & 0b1111) * 64, (this.texPages_800d6050[5] & 0b10000) != 0 ? 256 : 0)
        .pos(0, x0, y0)
        .uv(0, 160, 64)
        .pos(1, x1, y1)
        .uv(1, 191, 64)
        .pos(2, x2, y2)
        .uv(2, 160, 95)
        .pos(3, x3, y3)
        .uv(3, 191, 95)
      );
    }

    final float sp80 = (minX - maxX) / 2.0f;
    final float sp68 = (minX + maxX) / 2.0f;
    final float sp78 = (maxY - minY) / 2.0f;
    final float sp6a = (maxY + minY) / 2.0f;

    //LAB_800f2de8
    for(int fp = 0; fp < 8; fp++) {
      final SavePointRenderData44 struct0 = this.savePoint_800d5630[fp * 4];
      final SavePointRenderData44 struct1 = this.savePoint_800d5630[fp * 4 + 1];
      final SavePointRenderData44 struct2 = this.savePoint_800d5630[fp * 4 + 2];
      final SavePointRenderData44 struct3 = this.savePoint_800d5630[fp * 4 + 3];
      struct3.vert0_00.x = struct2.vert0_00.x;
      struct3.vert0_00.y = struct2.vert0_00.y;
      struct2.vert0_00.x = struct1.vert0_00.x;
      struct2.vert0_00.y = struct1.vert0_00.y;
      struct1.vert0_00.x = struct0.vert0_00.x;
      struct1.vert0_00.y = struct0.vert0_00.y;
      struct0.vert0_00.x = sp68 + (sp80 + this._800d6c78[fp]) * MathHelper.sin(struct0.rotation_28);
      struct0.vert0_00.y = sp6a + (sp78 + this._800d6c78[fp]) * MathHelper.cos(struct0.rotation_28);

      if(struct0.fadeState_38 != 0) {
        struct0.colour_34 -= struct0.fadeAmount_2c;

        if(struct0.colour_34 < 0.0f) {
          struct0.colour_34 = 0.0f;
          struct0.fadeState_38 = 0;
        }
      } else {
        //LAB_800f2f0c
        struct0.colour_34 += struct0.fadeAmount_2c;

        if(struct0.colour_34 > 0.5f) {
          struct0.colour_34 = 0.5f;
          struct0.fadeState_38 = 1;
        }
      }

      //LAB_800f2f4c
      //LAB_800f2f50
      //LAB_800f2f78
      for(int s4 = 0; s4 < 4; s4++) {
        final SavePointRenderData44 struct = this.savePoint_800d5630[fp + s4];

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .bpp(Bpp.of(this.texPages_800d6050[4] >>> 7 * 0b11))
          .translucent(Translucency.B_PLUS_F)
          .monochrome((int)(struct.colour_34 * 255.0f))
          .clut((this.cluts_800d6068[4] & 0b111111) * 16, this.cluts_800d6068[4] >>> 6)
          .vramPos((this.texPages_800d6050[4] & 0b1111) * 64, (this.texPages_800d6050[4] & 0b10000) != 0 ? 256 : 0)
          .pos(0, struct.vert0_00.x, struct.vert0_00.y)
          .pos(1, struct.vert0_00.x + 6.0f, struct.vert0_00.y)
          .pos(2, struct.vert0_00.x, struct.vert0_00.y + 6.0f)
          .pos(3, struct.vert0_00.x + 6.0f, struct.vert0_00.y + 6.0f);

        if(s4 % 3 == 0) {
          //LAB_800f30d8
          cmd
            .uv(0, 176, 48)
            .uv(1, 183, 48)
            .uv(2, 176, 55)
            .uv(3, 183, 55);
        } else {
          cmd
            .uv(0, 184, 48)
            .uv(1, 191, 48)
            .uv(2, 184, 55)
            .uv(3, 191, 55);
        }

        GPU.queueCommand(41, cmd);
      }

      struct0.rotation_28 += MathHelper.psxDegToRad(this.savePointFloatiesRotations_800d6c88[fp]);
      struct0.rotation_28 %= MathHelper.TWO_PI;
    }
  }

  @Method(0x800f31bcL)
  private void handleTriangleIndicators() {
    this.triangleIndicator_800c69fc.screenOffsetX_10 = this.screenOffsetX_800cb568;
    this.triangleIndicator_800c69fc.screenOffsetY_14 = this.screenOffsetY_800cb56c;

    if(gameState_800babc8.indicatorsDisabled_4e3) {
      return;
    }

    if(fullScreenEffect_800bb140.currentColour_28 != 0) {
      return;
    }

    final IndicatorMode indicatorMode = CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get());
    if(indicatorMode != IndicatorMode.MOMENTARY) {
      this.momentaryIndicatorTicks_800f9e9c = 0;
    }

    //LAB_800f321c
    if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_1)) { // R1
      if(indicatorMode == IndicatorMode.OFF) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.MOMENTARY);
        //LAB_800f3244
      } else if(indicatorMode == IndicatorMode.MOMENTARY) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.ON);
      } else if(indicatorMode == IndicatorMode.ON) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.OFF);
        this.momentaryIndicatorTicks_800f9e9c = 0;
      }
      //LAB_800f3260
    } else if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_1)) { // L1
      if(indicatorMode == IndicatorMode.OFF) {
        //LAB_800f3274
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.ON);
        //LAB_800f3280
      } else if(indicatorMode == IndicatorMode.MOMENTARY) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.OFF);
        this.momentaryIndicatorTicks_800f9e9c = 0;
        //LAB_800f3294
      } else if(indicatorMode == IndicatorMode.ON) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.MOMENTARY);

        //LAB_800f32a4
        this.momentaryIndicatorTicks_800f9e9c = 0;
      }
    }

    //LAB_800f32a8
    //LAB_800f32ac
    if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) == IndicatorMode.OFF) {
      return;
    }

    final MV ls = new MV();
    GsGetLs(this.sobjs_800c6880[0].innerStruct_00.model_00.coord2_14, ls);

    PushMatrix();
    GTE.setTransforms(ls);

    GTE.perspectiveTransform(this.bottom_800d6cb8);
    final float bottomY = GTE.getScreenY(2);

    GTE.perspectiveTransform(this.top_800d6cc0);
    final float topY = GTE.getScreenY(2);

    GTE.perspectiveTransform(0, -(topY - bottomY) - 48, 0);
    this.triangleIndicator_800c69fc.playerX_08 = GTE.getScreenX(2);
    this.triangleIndicator_800c69fc.playerY_0c = GTE.getScreenY(2);

    PopMatrix();

    if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) == IndicatorMode.MOMENTARY) {
      if(this.momentaryIndicatorTicks_800f9e9c < 33) {
        this.renderTriangleIndicators();
        this.momentaryIndicatorTicks_800f9e9c++;
      }
      //LAB_800f3508
    } else if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) == IndicatorMode.ON) {
      this.renderTriangleIndicators();
    }

    //LAB_800f3518
  }

  @Method(0x800f352cL)
  private void renderTriangleIndicators() {
    final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;

    //LAB_800f35b0
    for(int indicatorIndex = 0; indicatorIndex < 21; indicatorIndex++) {
      final TriangleIndicator44 s1 = this._800d4ff0[indicatorIndex];

      final AnmFile anm;
      if(indicatorIndex == 0) {
        // Player indicator

        s1.x_34 = indicator.playerX_08;
        s1.y_38 = indicator.playerY_0c - 28;

        anm = this.playerIndicatorAnimation_800d5588.anm_00;
      } else {
        // Door indicators

        //LAB_800f35f4
        if(indicator._18[indicatorIndex - 1] < 0) {
          break;
        }

        s1.x_34 = indicator.screenOffsetX_10 - indicator.screenOffsetX_90[indicatorIndex - 1] + indicator.x_40[indicatorIndex - 1] -  2;
        s1.y_38 = indicator.screenOffsetY_14 - indicator.screenOffsetY_e0[indicatorIndex - 1] + indicator.y_68[indicatorIndex - 1] - 32;

        anm = this.doorIndicatorAnimation_800d5590.anm_00;
      }

      final AnmSpriteGroup[] spriteGroups = anm.spriteGroups;

      //LAB_800f365c
      if((s1._00 & 0x1) == 0) {
        s1.time_08--;

        if(s1.time_08 < 0) {
          s1.sequence_04++;

          if(s1.sequence_04 > s1.sequenceCount_14) {
            s1.sequence_04 = s1._10;
            s1._0c++;
          }

          //LAB_800f36b0
          s1.time_08 = anm.sequences_08[s1.sequence_04].time_02 - 1;
        }
      }

      //LAB_800f36d0
      final AnmSpriteGroup group = spriteGroups[anm.sequences_08[s1.sequence_04].spriteGroupNumber_00];
      final int count = group.n_sprite_00;

      //LAB_800f3724
      for(int s6 = count - 1; s6 >= 0; s6--) {
        final AnmSpriteMetrics14 sprite = group.metrics_04[s6];

        final float x = s1.x_34 - sprite.w_08 / 2.0f;
        final float y = s1.y_38;
        final int u = s1.u_1c + sprite.u_00;
        final int v = s1.v_20 + sprite.v_01;
        final int tpage = s1.tpage_18 | sprite.flag_06 & 0x60;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
          .bpp(Bpp.of(tpage >>> 7 & 0b11))
          .rgb(s1.r_24, s1.g_25, s1.b_26)
          .pos(0, x, y)
          .pos(1, x + sprite.w_08, y)
          .pos(2, x, y + sprite.h_0a)
          .pos(3, x + sprite.w_08, y + sprite.h_0a)
          .uv(0, u, v)
          .uv(1, u + sprite.w_08, v)
          .uv(2, u, v + sprite.h_0a)
          .uv(3, u + sprite.w_08, v + sprite.h_0a);

        if(indicatorIndex == 0) { // Player indicator
          final int triangleIndex = this.getEncounterTriangleColour();
          cmd.clut(this._800d6cd8[triangleIndex] & 0x3f0, (sprite.cba_04 >>> 6 & 0x1ff) - this._800d6ce4[triangleIndex]);
        } else { // Door indicators
          //LAB_800f3884
          if((sprite.cba_04 & 0x8000) != 0) {
            cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
          }

          cmd.clut(992, (sprite.cba_04 >>> 6 & 0x1ff) - this._800d6cc8[indicator._18[indicatorIndex - 1]]);
        }

        //LAB_800f38b0
        GPU.queueCommand(38, cmd);
      }
    }

    //LAB_800f39d0
  }

  @Method(0x800f3a00L)
  private int getEncounterTriangleColour() {
    final int acc = this.encounterAccumulator_800c6ae8;

    if(acc <= 0xa00) {
      return 0;
    }

    //LAB_800f3a20
    if(acc <= 0xf00) {
      //LAB_800f3a40
      return 1;
    }

    //LAB_800f3a34
    return 2;
  }

  @Method(0x800f3a48L)
  private void initTriangleIndicators() {
    this.parseAnmFile(this.savepointAnm1, this.playerIndicatorAnimation_800d5588);
    this.parseAnmFile(this.savepointAnm2, this.doorIndicatorAnimation_800d5590);
    this.FUN_800f3b64(this.savepointAnm1, this.savepointAnm2, this._800d4ff0, 21);
  }

  @Method(0x800f3abcL)
  private void renderSubmapOverlays() {
    this.handleTriangleIndicators();

    if(this.hasSavePoint_800d5620) {
      this.renderSavePoint();
    }
  }

  @Method(0x800f3af8L)
  private void resetTriangleIndicators() {
    if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) != IndicatorMode.OFF) {
      this.momentaryIndicatorTicks_800f9e9c = 0;
    }

    //LAB_800f3b14
    final TriangleIndicator140 indicator = this.triangleIndicator_800c69fc;

    //LAB_800f3b24
    for(int i = 0; i < 20; i++) {
      indicator._18[i] = -1;
    }
  }

  @Method(0x800f3b64L)
  private void FUN_800f3b64(final AnmFile anm1, final AnmFile anm2, final TriangleIndicator44[] a2, final int count) {
    //LAB_800f3bc4
    for(int i = 0; i < count; i++) {
      final TriangleIndicator44 t2 = a2[i];

      final AnmFile anm;
      if(i == 0) {
        t2.tpage_18 = this.texPages_800d6050[0];
        t2.clut_1a = this.cluts_800d6068[0];
        t2.u_1c = this.dartArrowU_800d6ca8;
        t2.v_20 = this.dartArrowV_800d6cac;
        anm = anm1;
      } else {
        //LAB_800f3bfc
        t2.tpage_18 = this.texPages_800d6050[1];
        t2.clut_1a = this.cluts_800d6068[1];
        t2.u_1c = this.doorArrowU_800d6cb0;
        t2.v_20 = this.doorArrowV_800d6cb4;
        anm = anm2;
      }

      //LAB_800f3c24
      t2._00 = 0;
      t2.sequence_04 = 0;
      t2.time_08 = anm.sequences_08[0].time_02;
      t2._0c = 0;
      t2._10 = 0;
      t2.sequenceCount_14 = anm.n_sequence_06 - 1;

      t2.r_24 = 0x80;
      t2.g_25 = 0x80;
      t2.b_26 = 0x80;

      t2._28 = 0x1000;
      t2._2c = 0x1000;
      t2._30 = 0;
      t2.x_34 = 0.0f;
      t2.y_38 = 0.0f;
      t2._3c = 0;
    }
  }

  @Method(0x800f3b3cL)
  private void deallocateSavePoint() {
    this.hasSavePoint_800d5620 = false;
  }

  @Method(0x800f3c98L)
  private void parseAnmFile(final AnmFile anmFile, final AnimatedSprite08 a1) {
    a1.anm_00 = anmFile;
    a1.spriteGroup_04 = anmFile.spriteGroups;
  }

  @Method(0x800f3cb8L)
  private void FUN_800f3cb8() {
    Struct34 s1 = this.struct34_800d6018.parent_30;

    //LAB_800f3ce8
    while(s1 != null) {
      if(s1._08 == 0) {
        if(s1._02 % s1._04 == 0) {
          final SMapStruct3c s0 = new SMapStruct3c();

          s0._02 = 0;
          s0._06 = s1._06;
          s0.x_0c = (short)s1.screenOffsetX_24;
          s0.y_0e = (short)s1.screenOffsetY_28;
          s0._10 = s1.x_1c * 0x1_0000 - simpleRand() * s1._18;
          s0._14 = s1.y_20 * 0x1_0000;
          s0._1c = 0x8_0000 / s1._0c;
          s0._20 = (s1._14 << 16) / s1._06;
          s0._24 = s1._10 << 16;
          s0.size_28 = 0;
          s0._2c = 0x80_0000 / s0._06;
          s0._30 = 0x80_0000;
          s0.z_34 = s1.sz3_2c;
          s0.parent_38 = this.struct3c_800d5fd8.parent_38;

          this.struct3c_800d5fd8.parent_38 = s0;
        }

        //LAB_800f3df4
        s1._02++;
      } else {
        //LAB_800f3e08
        if(s1._02 >= s1._08) {
          if(s1._02 % s1._04 == 0) {
            final SMapStruct3c s0 = new SMapStruct3c();

            s0._02 = 0;
            s0._06 = s1._06;
            s0.x_0c = (short)s1.screenOffsetX_24;
            s0.y_0e = (short)s1.screenOffsetY_28;
            s0._10 = s1.x_1c * 0x1_0000 - simpleRand() * s1._18;
            s0._14 = s1.y_20 * 0x1_0000;
            s0._1c = 0x8_0000 / s1._0c;
            s0._20 = (s1._14 << 16) / s1._06;
            s0._24 = s1._10 << 16;
            s0.size_28 = 0;
            s0._2c = 0x80_0000 / s0._06;
            s0._30 = 0x80_0000;
            s0.z_34 = s1.sz3_2c;
            s0.parent_38 = this.struct3c_800d5fd8.parent_38;

            this.struct3c_800d5fd8.parent_38 = s0;
          }
        }

        //LAB_800f3f14
        s1._02++;

        if(s1._02 >= s1._0a) {
          s1._02 = 0;
        }
      }

      //LAB_800f3f3c
      s1 = s1.parent_30;
    }

    //LAB_800f3f4c
  }

  @Method(0x800f3f68L)
  private void renderSmoke() {
    SMapStruct3c s1 = this.struct3c_800d5fd8;
    SMapStruct3c s0 = s1.parent_38;

    //LAB_800f3fb0
    while(s0 != null) {
      if(s0._02 < s0._06) {
        //LAB_800f3fe8
        s0._24 += s0._20;
        s0._14 -= s0._1c;
        s0._30 -= s0._2c;
        s0.size_28 = (short)(s0._24 >> 16);
        final float x = this.screenOffsetX_800cb568 - s0.x_0c + s0._10 / 0x1_0000;
        final float y = this.screenOffsetY_800cb56c - s0.y_0e + s0._14 / 0x1_0000;
        final int size = s0.size_28 / 2;
        final float left = x - size;
        final float right = x + size;
        final float top = y - size;
        final float bottom = y + size;
        final int v0 = s0._30 >> 16;
        final int colour = v0 > 0x80 ? 0 : v0;

        //LAB_800f4084
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .monochrome(colour)
          .clut((this.cluts_800d6068[9] & 0b111111) * 16, this.cluts_800d6068[9] >>> 6)
          .vramPos((this.texPages_800d6050[9] & 0b1111) * 64, (this.texPages_800d6050[9] & 0b10000) != 0 ? 256 : 0)
          .translucent(Translucency.of(this.texPages_800d6050[9] >>> 5 & 0b11))
          .bpp(Bpp.of(this.texPages_800d6050[9] >>> 7 & 0b11))
          .pos(0, left, top)
          .pos(1, right, top)
          .pos(2, left, bottom)
          .pos(3, right, bottom)
          .uv(0, 64, 32)
          .uv(1, 95, 32)
          .uv(2, 64, 63)
          .uv(3, 95, 63);

        GPU.queueCommand(s0.z_34, cmd);

        s1 = s0;
        s0._02++;
        s0 = s0.parent_38;
      } else {
        s1.parent_38 = s0.parent_38;
        s0 = s1.parent_38;
      }

      //LAB_800f41b0
    }

    //LAB_800f41bc
  }

  @Method(0x800f41dcL)
  private void FUN_800f41dc() {
    //LAB_800f4204
    while(this.struct34_800d6018.parent_30 != null) {
      final Struct34 a0 = this.struct34_800d6018.parent_30;
      this.struct34_800d6018.parent_30 = a0.parent_30;
    }

    //LAB_800f4224
    this.FUN_800eed84(this.struct3c_800d5fd8);
  }

  @Method(0x800f4244L)
  private void FUN_800f4244(final Tim tim, final Vector2i tpageOut, final Vector2i clutOut, final Translucency transMode) {
    //LAB_800f427c
    if(tim.hasClut()) {
      final Rect4i clutRect = tim.getClutRect();
      clutOut.set(clutRect.x, clutRect.y);
      GPU.uploadData15(clutRect, tim.getClutData());
    }

    //LAB_800f42d0
    final Rect4i imageRect = tim.getImageRect();
    tpageOut.set(imageRect.x, imageRect.y);
    GPU.uploadData15(imageRect, tim.getImageData());

    //LAB_800f4338
  }

  @Method(0x800f4354L)
  private void handleAndRenderSubmapEffects() {
    if(this._800c6870 == -1) {
      this.FUN_800f41dc();

      if(this._800f9e60 > 0 && this._800f9e60 < 3) {
        this.handleSnow();
      }

      this.FUN_800f0514();
    } else {
      this.FUN_800f3cb8();
      this.renderSmoke();

      if(this._800f9e60 == 1) {
        this.handleSnow();
      }

      if(this._800f9e74 || this._800f9e70 != 0) {
        this.FUN_800f00a4();
        this.FUN_800efe7c();
      }
    }
  }

  @Method(0x800f4420L)
  private void FUN_800f4420() {
    this.FUN_800f41dc();

    if(this._800f9e60 > 0 && this._800f9e60 < 3) {
      this.handleSnow();
    }

    //LAB_800f4454
    this.FUN_800f0514();
  }

  /** Things such as the save point, &lt;!&gt; action icon, encounter icon, etc. */
  @Method(0x800f45f8L)
  private void loadSubmapEffects() {
    if(this.submapEffectsState_800f9eac == -1) {
      //LAB_800f4714
      this.deallocateSavePoint();
      this.FUN_800f4420();
      this.FUN_800f0440();
      this._800d4fe8 = 0;
      this.submapEffectsLoadMode_800f9ea8 = 0;
      this.submapEffectsState_800f9eac = 0;
      return;
    }

    //LAB_800f4624
    final int loadMode = this.submapEffectsLoadMode_800f9ea8;
    if(loadMode == 0) {
      //LAB_800f4660
      this.loadMiscTextures(11);
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(992, 288, 984, 288, 8, 64)); // Copies the save point texture beside itself
      this.submapEffectsLoadMode_800f9ea8++;
      this.submapEffectsState_800f9eac = 1;
    } else if(loadMode == 1) {
      //LAB_800f4650
      //LAB_800f46d8
      this._800d4fe8 = 0;
      this.initTriangleIndicators();
      this.initSavePoint();
      this.FUN_800f0370();
      this.submapEffectsLoadMode_800f9ea8++;
      this.submapEffectsState_800f9eac = 2;
    }

    //LAB_800f473c
  }

  /**
   * Textures such as footsteps, encounter indicator, yellow &lt;!&gt; sign, save point, etc.
   */
  @Method(0x800f4754L)
  private void loadMiscTextures(final int textureCount) {
    //LAB_800f47f0
    for(int textureIndex = 0; textureIndex < textureCount; textureIndex++) {
      final Tim tim = new Tim(Unpacker.loadFile("SUBMAP/" + this.miscTextures_800f9eb0[textureIndex]));
      GPU.uploadData15(tim.getImageRect(), tim.getImageData());

      this.texPages_800d6050[textureIndex] = GetTPage(Bpp.values()[tim.getFlags() & 0b11], this.miscTextureTransModes_800d6cf0[textureIndex], tim.getImageRect().x, tim.getImageRect().y);
      this.cluts_800d6068[textureIndex] = tim.getClutRect().y << 6 | (tim.getClutRect().x & 0x3f0) >>> 4;

      GPU.uploadData15(tim.getClutRect(), tim.getClutData());
    }

    //LAB_800f48a8
  }
}
