package legend.game;

import legend.core.Config;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.Rect4i;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.types.Ptr;
import legend.game.fmv.Fmv;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.Param;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptStorageParam;
import legend.game.submap.EncounterRateMode;
import legend.game.submap.IndicatorMode;
import legend.game.submap.SubmapAssets;
import legend.game.submap.SubmapObject;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.AnimatedSprite08;
import legend.game.types.AnmFile;
import legend.game.types.AnmSpriteGroup;
import legend.game.types.AnmSpriteMetrics14;
import legend.game.types.BigSubStruct;
import legend.game.types.CContainer;
import legend.game.types.CharacterData2c;
import legend.game.types.CreditStruct1c;
import legend.game.types.DR_TPAGE;
import legend.game.types.DustRenderData54;
import legend.game.types.EngineState;
import legend.game.types.EnvironmentFile;
import legend.game.types.EnvironmentStruct;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import legend.game.types.MediumStruct;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.MrgFile;
import legend.game.types.NewRootStruct;
import legend.game.types.SMapStruct3c;
import legend.game.types.SavePointRenderData44;
import legend.game.types.ShopStruct40;
import legend.game.types.SmallerStruct;
import legend.game.types.SnowEffect;
import legend.game.types.SobjPos14;
import legend.game.types.SomethingStruct;
import legend.game.types.SomethingStructSub0c_1;
import legend.game.types.SomethingStructSub0c_2;
import legend.game.types.Struct0c;
import legend.game.types.Struct14;
import legend.game.types.Struct18;
import legend.game.types.Struct20;
import legend.game.types.Struct34;
import legend.game.types.Struct34_2;
import legend.game.types.Structb0;
import legend.game.types.SubmapCutInfo;
import legend.game.types.SubmapEncounterData_04;
import legend.game.types.SubmapObject210;
import legend.game.types.SubmapStruct80;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import legend.game.types.TmdSubExtension;
import legend.game.types.Translucency;
import legend.game.types.TriangleIndicator140;
import legend.game.types.TriangleIndicator44;
import legend.game.types.UnknownStruct;
import legend.game.types.UnknownStruct2;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;

import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001ada0;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment._80010544;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.getSubmapMusicChange;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadMenuSounds;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadSubmapSounds;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.reinitSound;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800217a4;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218f0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80029e04;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002aa04;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.animateModelTextures;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8002.prepareObjTable2;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderDobj2;
import static legend.game.Scus94491BpeSegment_8002.renderModel;
import static legend.game.Scus94491BpeSegment_8002.srand;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MargePrim;
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.SetDrawTPage;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_Zyx;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8004.sssqFadeIn;
import static legend.game.Scus94491BpeSegment_8005._80050274;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._80052c48;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c3c;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bda08;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.afterFmvLoadingStage_800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.fmvIndex_800bf0dc;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.matrix_800bed30;
import static legend.game.Scus94491BpeSegment_800b.model_800bda10;
import static legend.game.Scus94491BpeSegment_800b.musicLoaded_800bd782;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800b.rview2_800bd7e8;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.screenOffsetX_800bed50;
import static legend.game.Scus94491BpeSegment_800b.screenOffsetY_800bed54;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.sobjPositions_800bd818;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.submapIndex_800bd808;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;

public final class SMap {
  private SMap() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SMap.class);

  public static final GsF_LIGHT GsF_LIGHT_0_800c66d8 = new GsF_LIGHT();
  public static final GsF_LIGHT GsF_LIGHT_1_800c66e8 = new GsF_LIGHT();
  public static final GsF_LIGHT GsF_LIGHT_2_800c66f8 = new GsF_LIGHT();
  public static final UnsignedShortRef chapterTitleState_800c6708 = MEMORY.ref(2, 0x800c6708L, UnsignedShortRef::new);
  public static final Value _800c670a = MEMORY.ref(2, 0x800c670aL);
  public static final Value _800c670c = MEMORY.ref(2, 0x800c670cL);
  public static final Value _800c670e = MEMORY.ref(2, 0x800c670eL);
  public static List<FileData> chapterTitleCardMrg_800c6710;
  public static final Value _800c6714 = MEMORY.ref(4, 0x800c6714L);
  public static final Value _800c6718 = MEMORY.ref(4, 0x800c6718L);
  public static final Value _800c671c = MEMORY.ref(4, 0x800c671cL);
  public static final Value _800c6720 = MEMORY.ref(4, 0x800c6720L);
  public static final Value _800c6724 = MEMORY.ref(4, 0x800c6724L);
  public static final Value _800c6728 = MEMORY.ref(1, 0x800c6728L);

  public static final Value _800c672c = MEMORY.ref(4, 0x800c672cL);
  public static final IntRef sobjCount_800c6730 = MEMORY.ref(4, 0x800c6730L, IntRef::new);

  /**
   * Lower 4 bits are chapter title num (starting at 1), used for displaying chapter title cards
   *
   * Also has flag 0x80 OR'd with it sometimes, unknown what it means
   */
  public static final UnsignedShortRef chapterTitleNum_800c6738 = MEMORY.ref(2, 0x800c6738L, UnsignedShortRef::new);

  public static final Value _800c673c = MEMORY.ref(4, 0x800c673cL);
  public static ScriptState<Void> submapControllerState_800c6740;

  public static final Model124 playerModel_800c6748 = new Model124("Player");
  public static final Value _800c686c = MEMORY.ref(2, 0x800c686cL);
  public static final Value _800c686e = MEMORY.ref(2, 0x800c686eL);
  public static final Value _800c6870 = MEMORY.ref(2, 0x800c6870L);

  public static final BoolRef submapAssetsLoaded_800c6874 = MEMORY.ref(4, 0x800c6874L, BoolRef::new);
  public static List<FileData> submapAssetsMrg_800c6878;
  public static final Value _800c687c = MEMORY.ref(2, 0x800c687cL);
  public static final Value _800c687e = MEMORY.ref(2, 0x800c687eL);
  public static final ScriptState<SubmapObject210>[] sobjs_800c6880 = new ScriptState[20];
  public static final BoolRef submapScriptsLoaded_800c68d0 = MEMORY.ref(4, 0x800c68d0L, BoolRef::new);

  public static List<FileData> submapScriptsMrg_800c68d8;
  public static SubmapAssets submapAssets;

  public static final BoolRef chapterTitleCardLoaded_800c68e0 = MEMORY.ref(2, 0x800c68e0L, BoolRef::new);

  public static final Value loadingStage_800c68e4 = MEMORY.ref(4, 0x800c68e4L);
  public static final SubmapStruct80 _800c68e8 = new SubmapStruct80();
  public static final Value callbackIndex_800c6968 = MEMORY.ref(2, 0x800c6968L);
  public static final ArrayRef<IntRef> _800c6970 = MEMORY.ref(4, 0x800c6970L, ArrayRef.of(IntRef.class, 32, 4, IntRef::new));

  public static TriangleIndicator140 _800c69fc;

  /** TODO array, flags for submap objects - 0x80 means the model is the same as the previous one */
  public static final Value submapObjectFlags_800c6a50 = MEMORY.ref(4, 0x800c6a50L);

  public static final VECTOR cameraPos_800c6aa0 = new VECTOR();
  public static final Value _800c6aac = MEMORY.ref(2, 0x800c6aacL);
  public static final VECTOR prevPlayerPos_800c6ab0 = new VECTOR();
  public static float encounterMultiplier_800c6abc;
  public static final MATRIX matrix_800c6ac0 = new MATRIX();
  public static final Value _800c6ae0 = MEMORY.ref(4, 0x800c6ae0L);
  public static final Value _800c6ae4 = MEMORY.ref(4, 0x800c6ae4L);
  public static final IntRef encounterAccumulator_800c6ae8 = MEMORY.ref(4, 0x800c6ae8L, IntRef::new);
  public static UnknownStruct _800c6aec;

  public static WhichMenu _800caaf0;
  public static final IntRef _800caaf4 = MEMORY.ref(4, 0x800caaf4L, IntRef::new);
  public static final IntRef _800caaf8 = MEMORY.ref(4, 0x800caaf8L, IntRef::new);
  public static final IntRef _800caafc = MEMORY.ref(4, 0x800caafcL, IntRef::new);
  public static final IntRef _800cab00 = MEMORY.ref(4, 0x800cab00L, IntRef::new);
  public static NewRootStruct newrootPtr_800cab04;

  public static final Value backgroundLoaded_800cab10 = MEMORY.ref(4, 0x800cab10L);

  public static final Value _800cab20 = MEMORY.ref(4, 0x800cab20L);
  public static MediumStruct _800cab24;
  public static final Value _800cab28 = MEMORY.ref(4, 0x800cab28L);
  public static final Value _800cab2c = MEMORY.ref(4, 0x800cab2cL);
  public static EnvironmentStruct[] envStruct_800cab30;

  public static final Value smapLoadingStage_800cb430 = MEMORY.ref(4, 0x800cb430L);

  public static final Value _800cb440 = MEMORY.ref(4, 0x800cb440L);

  public static final Value _800cb448 = MEMORY.ref(4, 0x800cb448L);

  public static final Value _800cb450 = MEMORY.ref(4, 0x800cb450L);

  public static final int[] collisionAndTransitions_800cb460 = new int[64];

  public static final Value _800cb560 = MEMORY.ref(4, 0x800cb560L); //TODO something X
  public static final Value _800cb564 = MEMORY.ref(4, 0x800cb564L); //TODO something Y
  public static final IntRef screenOffsetX_800cb568 = MEMORY.ref(4, 0x800cb568L, IntRef::new);
  public static final IntRef screenOffsetY_800cb56c = MEMORY.ref(4, 0x800cb56cL, IntRef::new);
  public static final Value _800cb570 = MEMORY.ref(4, 0x800cb570L);
  public static final Value _800cb574 = MEMORY.ref(4, 0x800cb574L);
  public static final Value _800cb578 = MEMORY.ref(4, 0x800cb578L);
  public static final IntRef _800cb57c = MEMORY.ref(4, 0x800cb57cL, IntRef::new);
  public static final IntRef _800cb580 = MEMORY.ref(4, 0x800cb580L, IntRef::new);
  public static final Value backgroundObjectsCount_800cb584 = MEMORY.ref(4, 0x800cb584L);

  public static final Struct0c[] _800cb590 = new Struct0c[32];
  static {
    Arrays.setAll(_800cb590, i -> new Struct0c());
  }
  /** Array of 0x24 (tpage packet, then a quad packet, then more data used elsewhere?) TODO */
  public static final Value _800cb710 = MEMORY.ref(1, 0x800cb710L);

  public static final SVECTOR[] _800cbb90 = new SVECTOR[32];
  static {
    Arrays.setAll(_800cbb90, i -> new SVECTOR());
  }
  public static final Value _800cbc90 = MEMORY.ref(4, 0x800cbc90L);

  public static final GsRVIEW2 rview2_800cbd10 = new GsRVIEW2();
  public static final Value _800cbd30 = MEMORY.ref(4, 0x800cbd30L);
  public static final Value _800cbd34 = MEMORY.ref(4, 0x800cbd34L);
  public static UnknownStruct2 _800cbd38;
  public static UnknownStruct2 _800cbd3c;
  public static final MATRIX matrix_800cbd40 = new MATRIX();
  public static final Value _800cbd60 = MEMORY.ref(4, 0x800cbd60L);
  public static final Value _800cbd64 = MEMORY.ref(4, 0x800cbd64L);
  public static final MATRIX matrix_800cbd68 = new MATRIX();

  public static final Value _800cbd94 = MEMORY.ref(4, 0x800cbd94L);
  public static final SVECTOR _800cbd98 = new SVECTOR();

  public static final Value _800cbda4 = MEMORY.ref(4, 0x800cbda4L);
  public static final GsCOORDINATE2 GsCOORDINATE2_800cbda8 = new GsCOORDINATE2();
  public static final GsDOBJ2 GsDOBJ2_800cbdf8 = new GsDOBJ2();
  public static final SomethingStruct SomethingStruct_800cbe08 = new SomethingStruct();
  public static final Value _800cbe30 = MEMORY.ref(4, 0x800cbe30L);
  public static UnknownStruct2 _800cbe34;
  public static UnknownStruct2 _800cbe38;

  public static final Value _800cbe48 = MEMORY.ref(4, 0x800cbe48L);

  public static final Value _800cbe68 = MEMORY.ref(4, 0x800cbe68L);

  public static final Value _800d1a78 = MEMORY.ref(4, 0x800d1a78L);
  public static final Value _800d1a7c = MEMORY.ref(4, 0x800d1a7cL);
  public static final Value _800d1a80 = MEMORY.ref(4, 0x800d1a80L);
  public static final Value _800d1a84 = MEMORY.ref(4, 0x800d1a84L);

  public static SomethingStruct SomethingStructPtr_800d1a88;
  public static UnknownStruct2 _800d1a8c;
  public static final MediumStruct _800d1a90 = new MediumStruct();

  public static List<FileData> creditTims_800d1ae0;
  public static final IntRef fadeOutTicks_800d1ae4 = MEMORY.ref(4, 0x800d1ae4L, IntRef::new);
  public static final BoolRef creditTimsLoaded_800d1ae8 = MEMORY.ref(4, 0x800d1ae8L, BoolRef::new);
  public static final IntRef creditsPassed_800d1aec = MEMORY.ref(4, 0x800d1aecL, IntRef::new);
  public static final IntRef creditIndex_800d1af0 = MEMORY.ref(4, 0x800d1af0L, IntRef::new);

  public static final ArrayRef<CreditStruct1c> credits_800d1af8 = MEMORY.ref(4, 0x800d1af8L, ArrayRef.of(CreditStruct1c.class, 16, 0x1c, CreditStruct1c::new));

  public static final MATRIX matrix_800d4bb0 = new MATRIX();

  public static Structb0 _800d4bd0;
  public static FileData _800d4bd4;
  public static SnowEffect snow_800d4bd8;
  public static final BoolRef submapCutModelAndAnimLoaded_800d4bdc = MEMORY.ref(4, 0x800d4bdcL, BoolRef::new);
  public static final BoolRef submapTextureAndMatrixLoaded_800d4be0 = MEMORY.ref(4, 0x800d4be0L, BoolRef::new);
  public static final BoolRef theEndTimLoaded_800d4be4 = MEMORY.ref(4, 0x800d4be4L, BoolRef::new);
  public static CContainer submapCutModel;
  public static TmdAnimationFile submapCutAnim;
  public static Tim submapCutTexture;
  public static MATRIX submapCutMatrix;
  public static Tim theEndTim_800d4bf0;

  public static final Model124 submapModel_800d4bf8 = new Model124("Submap");

  public static final Value _800d4d20 = MEMORY.ref(4, 0x800d4d20L);

  public static final IntRef _800d4d30 = MEMORY.ref(4, 0x800d4d30L, IntRef::new);
  public static final IntRef _800d4d34 = MEMORY.ref(4, 0x800d4d34L, IntRef::new);

  public static final Model124 dustModel_800d4d40 = new Model124("Dust");

  public static final DustRenderData54 dust_800d4e68 = new DustRenderData54();

  public static final Struct20 _800d4ec0 = new Struct20();
  /** TODO struct */
  public static final Value _800d4ee0 = MEMORY.ref(4, 0x800d4ee0L);

  public static final Struct34 _800d4f18 = new Struct34();

  public static final Value _800d4f48 = MEMORY.ref(4, 0x800d4f48L);

  public static final SMapStruct3c struct3c_800d4f50 = new SMapStruct3c();

  public static final Struct34_2 _800d4f90 = new Struct34_2();

  public static final Struct14 _800d4fd0 = new Struct14();

  public static final Value _800d4fe8 = MEMORY.ref(2, 0x800d4fe8L);

  public static final ArrayRef<TriangleIndicator44> _800d4ff0 = MEMORY.ref(4, 0x800d4ff0L, ArrayRef.of(TriangleIndicator44.class, 21, 0x44, TriangleIndicator44::new));

  public static final AnimatedSprite08 playerIndicatorAnimation_800d5588 = MEMORY.ref(4, 0x800d5588L, AnimatedSprite08::new);
  public static final AnimatedSprite08 doorIndicatorAnimation_800d5590 = MEMORY.ref(4, 0x800d5590L, AnimatedSprite08::new);
  public static final SavePointRenderData44[] savePoint_800d5598 = new SavePointRenderData44[2];
  static {
    Arrays.setAll(savePoint_800d5598, i -> new SavePointRenderData44());
  }
  public static boolean hasSavePoint_800d5620;
  public static final SVECTOR savePointPos_800d5622 = new SVECTOR();

  public static final SavePointRenderData44[] savePoint_800d5630 = new SavePointRenderData44[32];
  static {
    Arrays.setAll(savePoint_800d5630, i -> new SavePointRenderData44());
  }
  public static final Model124 savePointModel_800d5eb0 = new Model124("Save point");

  public static final SMapStruct3c struct3c_800d5fd8 = new SMapStruct3c();

  public static final Struct34 struct34_800d6018 = new Struct34();

  public static final ArrayRef<UnsignedShortRef> texPages_800d6050 = MEMORY.ref(2, 0x800d6050L, ArrayRef.of(UnsignedShortRef.class, 12, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> cluts_800d6068 = MEMORY.ref(2, 0x800d6068L, ArrayRef.of(UnsignedShortRef.class, 12, 2, UnsignedShortRef::new));

  /** Maps submap cuts to their submap */
  public static final ArrayRef<UnsignedShortRef> cutToSubmap_800d610c = MEMORY.ref(2, 0x800d610cL, ArrayRef.of(UnsignedShortRef.class, 792, 2, UnsignedShortRef::new));
  /** TIM */
  public static final Value _800d673c = MEMORY.ref(4, 0x800d673cL);

  public static final Value timFile_800d689c = MEMORY.ref(4, 0x800d689cL);

  public static final RECT _800d6b48 = MEMORY.ref(4, 0x800d6b48L, RECT::new);

  public static final ArrayRef<SVECTOR> _800d6b7c = MEMORY.ref(4, 0x800d6b7cL, ArrayRef.of(SVECTOR.class, 12, 8, SVECTOR::new));
  public static final ArrayRef<IntRef> _800d6bdc = MEMORY.ref(4, 0x800d6bdcL, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<IntRef> smokeTextureWidths_800d6bec = MEMORY.ref(4, 0x800d6becL, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<IntRef> smokeTextureHeights_800d6bfc = MEMORY.ref(4, 0x800d6bfcL, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<ShortRef> _800d6c0c = MEMORY.ref(2, 0x800d6c0cL, ArrayRef.of(ShortRef.class, 4, 2, 4, ShortRef::new));
  public static final SVECTOR _800d6c18 = MEMORY.ref(4, 0x800d6c18L, SVECTOR::new);
  public static final SVECTOR _800d6c20 = MEMORY.ref(4, 0x800d6c20L, SVECTOR::new);
  public static final SVECTOR savePointV0_800d6c28 = MEMORY.ref(4, 0x800d6c28L, SVECTOR::new);
  public static final SVECTOR savePointV1_800d6c30 = MEMORY.ref(4, 0x800d6c30L, SVECTOR::new);
  public static final SVECTOR savePointV2_800d6c38 = MEMORY.ref(4, 0x800d6c38L, SVECTOR::new);
  public static final SVECTOR savePointV3_800d6c40 = MEMORY.ref(4, 0x800d6c40L, SVECTOR::new);
  public static final SVECTOR _800d6c48 = MEMORY.ref(4, 0x800d6c48L, SVECTOR::new);
  public static final SVECTOR _800d6c50 = MEMORY.ref(4, 0x800d6c50L, SVECTOR::new);
  public static final ArrayRef<IntRef> _800d6c58 = MEMORY.ref(4, 0x800d6c58L, ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
  public static final ArrayRef<ShortRef> _800d6c78 = MEMORY.ref(2, 0x800d6c78L, ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));
  public static final ArrayRef<IntRef> savePointFloatiesRotations_800d6c88 = MEMORY.ref(4, 0x800d6c88L, ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
  public static final IntRef dartArrowU_800d6ca8 = MEMORY.ref(4, 0x800d6ca8L, IntRef::new);
  public static final IntRef dartArrowV_800d6cac = MEMORY.ref(4, 0x800d6cacL, IntRef::new);
  public static final IntRef doorArrowU_800d6cb0 = MEMORY.ref(4, 0x800d6cb0L, IntRef::new);
  public static final IntRef doorArrowV_800d6cb4 = MEMORY.ref(4, 0x800d6cb4L, IntRef::new);
  public static final SVECTOR bottom_800d6cb8 = MEMORY.ref(4, 0x800d6cb8L, SVECTOR::new);
  public static final SVECTOR top_800d6cc0 = MEMORY.ref(4, 0x800d6cc0L, SVECTOR::new);
  public static final ArrayRef<IntRef> _800d6cc8 = MEMORY.ref(4, 0x800d6cc8L, ArrayRef.of(IntRef.class, 4, 4, IntRef::new));
  public static final ArrayRef<IntRef> _800d6cd8 = MEMORY.ref(4, 0x800d6cd8L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  public static final ArrayRef<IntRef> _800d6ce4 = MEMORY.ref(4, 0x800d6ce4L, ArrayRef.of(IntRef.class, 3, 4, IntRef::new));
  public static final ArrayRef<EnumRef<Translucency>> miscTextureTransModes_800d6cf0 = MEMORY.ref(4, 0x800d6cf0L, ArrayRef.of(EnumRef.classFor(Translucency.class), 11, 4, EnumRef.of(Translucency.values())));
  /**
   * Savepoint MRG (0x904 bytes)
   * <ol start="0">
   *   <li>ANM</li>
   *   <li>ANM</li>
   *   <li>Extended TMD</li>
   *   <li>Unknown - has "extended" 0xc header, then the first word is 01 00 08 00. The rest of the data is 00s.</li>
   *   <li>Extended TMD</li>
   *   <li>Unknown - has "extended" 0xc header, then the first word is 01 00 14 00. The rest of the data appears to be 16-bit words on a 32-bit boundary, i.e. 16 bits of data, followed by 16 bits of 0s.</li>
   * </ol>
   */
  public static final MrgFile mrg_800d6d1c = MEMORY.ref(4, 0x800d6d1cL, MrgFile::new);

  public static final ArrayRef<ShopStruct40> shops_800f4930 = MEMORY.ref(4, 0x800f4930L, ArrayRef.of(ShopStruct40.class, 64, 0x40, ShopStruct40::new));

  /** TODO an array of 0x14-long somethings */
  public static final Value _800f5930 = MEMORY.ref(4, 0x800f5930L);

  public static final ArrayRef<IntRef> _800f5ac0 = MEMORY.ref(4, 0x800f5ac0L, ArrayRef.of(IntRef.class, 5, 4, IntRef::new));
  /**
   * 65 - {@link SMap#FUN_800eddb4()}
   *
   * All other indices are {@link SMap#FUN_800e4994()}
   */
  public static final Runnable[] callbackArr_800f5ad4 = new Runnable[0x80];
  static {
    Arrays.setAll(callbackArr_800f5ad4, i -> SMap::FUN_800e4994);
    callbackArr_800f5ad4[65] = SMap::FUN_800eddb4;
  }
  public static final Value _800f5cd4 = MEMORY.ref(2, 0x800f5cd4L);

  public static final Value _800f64ac = MEMORY.ref(4, 0x800f64acL);

  public static final Value _800f64b0 = MEMORY.ref(2, 0x800f64b0L);

  /** Indexed by submap cut */
  public static final UnboundedArrayRef<SubmapEncounterData_04> encounterData_800f64c4 = MEMORY.ref(1, 0x800f64c4L, UnboundedArrayRef.of(4, SubmapEncounterData_04::new));

  public static final ArrayRef<ArrayRef<UnsignedShortRef>> sceneEncounterIds_800f74c4 = MEMORY.ref(2, 0x800f74c4L, ArrayRef.of(ArrayRef.classFor(UnsignedShortRef.class), 300, 8, ArrayRef.of(UnsignedShortRef.class, 4, 2, UnsignedShortRef::new)));

  public static final Value _800f7e24 = MEMORY.ref(4, 0x800f7e24L);
  public static Ptr<UnknownStruct> _800f7e28 = new Ptr<>(() -> _800c6aec, val -> _800c6aec = val);
  public static final Value _800f7e2c = MEMORY.ref(4, 0x800f7e2cL);
  public static final Value _800f7e30 = MEMORY.ref(4, 0x800f7e30L);

  public static final Value _800f7e4c = MEMORY.ref(4, 0x800f7e4cL);
  public static final Value _800f7e50 = MEMORY.ref(4, 0x800f7e50L);
  public static final Value _800f7e54 = MEMORY.ref(4, 0x800f7e54L);
  public static final Value _800f7e58 = MEMORY.ref(4, 0x800f7e58L);

  public static final Value _800f7f0c = MEMORY.ref(4, 0x800f7f0cL);
  public static final Value _800f7f10 = MEMORY.ref(4, 0x800f7f10L);
  public static final Value _800f7f14 = MEMORY.ref(4, 0x800f7f14L);

  public static final Value _800f7f6c = MEMORY.ref(2, 0x800f7f6cL);

  //TODO struct
  public static final Value _800f7f74 = MEMORY.ref(4, 0x800f7f74L);

  public static final Value _800f9374 = MEMORY.ref(4, 0x800f9374L);
  /**
   * <ol start="0">
   *   <li>{@link SMap#initCredits}</li>
   *   <li>{@link SMap#loadCredits}</li>
   *   <li>{@link SMap#waitForCreditsToLoadAndPlaySong}</li>
   *   <li>{@link SMap#fadeInCredits}</li>
   *   <li>{@link SMap#renderCredits}</li>
   *   <li>{@link SMap#fadeOutCredits}</li>
   *   <li>{@link SMap#waitForCreditsFadeOut}</li>
   *   <li>{@link SMap#deallocateCreditsAndReturnToMenu}</li>
   * </ol>
   */
  public static final Runnable[] theEndStates_800f9378 = new Runnable[8];
  static {
    theEndStates_800f9378[0] = SMap::initCredits;
    theEndStates_800f9378[1] = SMap::loadCredits;
    theEndStates_800f9378[2] = SMap::waitForCreditsToLoadAndPlaySong;
    theEndStates_800f9378[3] = SMap::fadeInCredits;
    theEndStates_800f9378[4] = SMap::renderCredits;
    theEndStates_800f9378[5] = SMap::fadeOutCredits;
    theEndStates_800f9378[6] = SMap::waitForCreditsFadeOut;
    theEndStates_800f9378[7] = SMap::deallocateCreditsAndReturnToMenu;
  }

  public static final Value _800f93b0 = MEMORY.ref(4, 0x800f93b0L);

  public static final ArrayRef<DVECTOR> creditPos_800f9670 = MEMORY.ref(2, 0x800f9670L, ArrayRef.of(DVECTOR.class, 16, 4, DVECTOR::new));

  public static final UnboundedArrayRef<ShortRef> smapFileIndices_800f982c = MEMORY.ref(2, 0x800f982cL, UnboundedArrayRef.of(2, ShortRef::new));

  public static final Value _800f9e5a = MEMORY.ref(2, 0x800f9e5aL);
  public static final UnsignedShortRef tpage_800f9e5c = MEMORY.ref(2, 0x800f9e5cL, UnsignedShortRef::new);
  public static final UnsignedShortRef clut_800f9e5e = MEMORY.ref(2, 0x800f9e5eL, UnsignedShortRef::new);
  public static final ShortRef _800f9e60 = MEMORY.ref(2, 0x800f9e60L, ShortRef::new);

  public static final IntRef snowLoadingStage_800f9e64 = MEMORY.ref(4, 0x800f9e64L, IntRef::new);

  public static final ShortRef _800f9e68 = MEMORY.ref(2, 0x800f9e68L, ShortRef::new);
  public static final ShortRef _800f9e6a = MEMORY.ref(2, 0x800f9e6aL, ShortRef::new);
  public static final ShortRef _800f9e6c = MEMORY.ref(2, 0x800f9e6cL, ShortRef::new);
  public static final ShortRef _800f9e6e = MEMORY.ref(2, 0x800f9e6eL, ShortRef::new);
  public static final Value _800f9e70 = MEMORY.ref(4, 0x800f9e70L);
  public static final Value _800f9e74 = MEMORY.ref(4, 0x800f9e74L);
  public static final Value _800f9e78 = MEMORY.ref(2, 0x800f9e78L);

  public static final Struct18[] _800f9e7c = new Struct18[8];
  public static final IntRef momentaryIndicatorTicks_800f9e9c = MEMORY.ref(4, 0x800f9e9cL, IntRef::new);

  public static final Value _800f9ea0 = MEMORY.ref(2, 0x800f9ea0L);

  public static final IntRef _800f9ea4 = MEMORY.ref(4, 0x800f9ea4L, IntRef::new);
  public static final IntRef _800f9ea8 = MEMORY.ref(4, 0x800f9ea8L, IntRef::new);
  public static final IntRef _800f9eac = MEMORY.ref(4, 0x800f9eacL, IntRef::new);
  public static final Value _800f9eb0 = MEMORY.ref(4, 0x800f9eb0L);

  @Method(0x800d9b08L)
  public static void FUN_800d9b08(final int charId) {
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

  @Method(0x800d9bc0L)
  public static FlowControl FUN_800d9bc0(final RunningScript<?> script) {
    loadSupportOverlay(2, () -> SMap.FUN_800d9b08(-1));
    return FlowControl.CONTINUE;
  }

  @Method(0x800d9bf4L)
  public static FlowControl FUN_800d9bf4(final RunningScript<?> script) {
    //LAB_800d9c04
    for(int i = 0; i < 9; i++) {
      gameState_800babc8.charData_32c[i].status_10 = 0;
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x800d9c1cL)
  public static FlowControl FUN_800d9c1c(final RunningScript<?> script) {
    //LAB_800d9c78
    gameState_800babc8.charData_32c[script.params_20[1].get()].set(gameState_800babc8.charData_32c[script.params_20[0].get()]);
    loadSupportOverlay(2, () -> SMap.FUN_800d9b08(script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800d9ce4L)
  public static FlowControl scriptSetCharAddition(final RunningScript<?> script) {
    gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800d9d20L)
  public static FlowControl scriptGetCharAddition(final RunningScript<?> script) {
    script.params_20[1].set(gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19);
    return FlowControl.CONTINUE;
  }

  /** Called when Dart is given Divine Dragon spirit */
  @Method(0x800d9d60L)
  public static FlowControl FUN_800d9d60(final RunningScript<?> script) {
    if(gameState_800babc8.charData_32c[0].dlevelXp_0e < 63901) {
      gameState_800babc8.charData_32c[0].dlevelXp_0e = 63901;
    }

    //LAB_800d9d90
    gameState_800babc8.charData_32c[0].dlevel_13 = 5;

    loadSupportOverlay(2, () -> SMap.FUN_800d9dc0(0));
    return FlowControl.CONTINUE;
  }

  @Method(0x800d9dc0L)
  public static void FUN_800d9dc0(final int charIndex) {
    gameState_800babc8.charData_32c[charIndex].sp_0c = 500;
    FUN_800d9b08(-1);
  }

  @Method(0x800d9e08L)
  public static void FUN_800d9e08() {
    pregameLoadingStage_800bb10c.incr();

    if(pregameLoadingStage_800bb10c.get() > 94) {
      fmvIndex_800bf0dc = 17;
      afterFmvLoadingStage_800bf0ec = EngineState.THE_END_04;
      pregameLoadingStage_800bb10c.set(0);
      vsyncMode_8007a3b8 = 2;
      Fmv.playCurrentFmv();
    }

    //LAB_800d9e5c
  }

  @Method(0x800d9e64L)
  public static void adjustSmapUvs(final GsDOBJ2 dobj2, final int colourMap) {
    final TmdObjTable1c objTable = dobj2.tmd_08;

    //LAB_800d9e90
    for(final TmdObjTable1c.Primitive primitive : objTable.primitives_10) {
      final int header = primitive.header();
      final int id = header & 0xff04_0000;

      if(id == 0x3400_0000 || id == 0x3600_0000) {
        FUN_800da6c8(primitive, colourMap & 0x7f);
      } else if(id == 0x3500_0000 || id == 0x3700_0000) {
        FUN_800da7f4(primitive, colourMap & 0x7f);
      } else if(id == 0x3c00_0000 || id == 0x3e00_0000) {
        FUN_800da754(primitive, colourMap & 0x7f);
      } else if(id == 0x3d00_0000 || id == 0x3f00_0000) {
        FUN_800da880(primitive, colourMap & 0x7f);
      }
    }
  }

  @Method(0x800da114L)
  public static void FUN_800da114(final Model124 struct) {
    if(struct.smallerStructPtr_a4 != null) {
      //LAB_800da138
      for(int i = 0; i < 4; i++) {
        if(struct.smallerStructPtr_a4.uba_04[i]) {
          FUN_800dde70(struct, i);
        }

        //LAB_800da15c
      }
    }

    //LAB_800da16c
    //LAB_800da174
    for(int i = 0; i < 7; i++) {
      if(struct.animateTextures_ec[i]) {
        animateModelTextures(struct, i);
      }

      //LAB_800da18c
    }

    if(struct.animationState_9c == 2) {
      return;
    }

    if(struct.animationState_9c == 0) {
      if(struct.ub_a2 == 0) {
        struct.remainingFrames_9e = struct.totalFrames_9a;
      } else {
        //LAB_800da1d0
        struct.remainingFrames_9e = struct.totalFrames_9a / 2;
      }

      //LAB_800da1e4
      struct.animationState_9c = 1;
      struct.partTransforms_94 = struct.partTransforms_90;
    }

    //LAB_800da1f8
    if((struct.remainingFrames_9e & 0x1) == 0 && struct.ub_a2 == 0) {
      final ModelPartTransforms0c[][] old = struct.partTransforms_94;

      if(struct.ub_a3 == 0) {
        applyInterpolationFrame(struct);
      } else {
        //LAB_800da23c
        applyModelPartTransforms(struct);
      }

      struct.partTransforms_94 = old;
    } else {
      //LAB_800da24c
      applyModelPartTransforms(struct);
    }

    //LAB_800da254
    struct.remainingFrames_9e--;

    if(struct.remainingFrames_9e == 0) {
      struct.animationState_9c = 0;
    }

    //LAB_800da274
  }

  @Method(0x800da524L)
  public static void FUN_800da524(final Model124 model) {
    GsInitCoordinate2(model.coord2_14, model_800bda10.coord2_14);

    model_800bda10.coord2_14.coord.transfer.set(model.vector_118);
    model_800bda10.zOffset_a0 = model.zOffset_a0 + 16;

    model_800bda10.scaleVector_fc.set(model.vector_10c).div(64.0f);

    RotMatrix_Xyz(model_800bda10.coord2Param_64.rotate, model_800bda10.coord2_14.coord);

    model_800bda10.coord2_14.coord.scaleL(model_800bda10.scaleVector_fc);
    model_800bda10.coord2_14.flg = 0;

    final MATRIX matrix = model_800bda10.coord2ArrPtr_04[0].coord;
    final GsCOORD2PARAM params = model_800bda10.coord2ArrPtr_04[0].param;

    params.rotate.zero();
    RotMatrix_Zyx(params.rotate, matrix);

    params.trans.zero();
    matrix.transfer.set(params.trans);

    final MATRIX lw = new MATRIX();
    final MATRIX ls = new MATRIX();
    GsGetLws(model_800bda10.ObjTable_0c.top[0].coord2_04, lw, ls);
    GsSetLightMatrix(lw);

    GTE.setRotationMatrix(ls);
    GTE.setTranslationVector(ls.transfer);

    Renderer.renderDobj2(model_800bda10.ObjTable_0c.top[0], false, 0);
    model_800bda10.coord2ArrPtr_04[0].flg--;
  }

  @Method(0x800da6c8L)
  public static void FUN_800da6c8(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800f5930.offset(colourMap * 0x14L).getAddress();

    //LAB_800da6e8
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800da754L)
  public static void FUN_800da754(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800f5930.offset(colourMap * 0x14L).getAddress();

    //LAB_800da774
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0xc, 4,  MathHelper.get(data, 0xc, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800da7f4L)
  public static void FUN_800da7f4(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800f5930.offset(colourMap * 0x14L).getAddress();

    //LAB_800da814
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800da880L)
  public static void FUN_800da880(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800f5930.offset(colourMap * 0x14L).getAddress();

    //LAB_800da8a0
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0xc, 4,  MathHelper.get(data, 0xc, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800da920L)
  public static void applyInterpolationFrame(final Model124 a0) {
    final ModelPartTransforms0c[][] transforms = a0.partTransforms_94;

    //LAB_800da96c
    for(int i = 0; i < a0.tmdNobj_ca; i++) {
      final GsDOBJ2 dobj2 = a0.dobj2ArrPtr_00[i];

      final GsCOORDINATE2 coord2 = dobj2.coord2_04;
      final GsCOORD2PARAM params = coord2.param;
      final MATRIX matrix = coord2.coord;

      RotMatrix_Zyx(params.rotate, matrix);

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
  public static void renderSmapModel(final Model124 a0) {
    zOffset_1f8003e8.set(a0.zOffset_a0);
    tmdGp0Tpage_1f8003ec.set(a0.tpage_108);

    //LAB_800daaa8
    final MATRIX lw = new MATRIX();
    final MATRIX ls = new MATRIX();
    for(int i = 0; i < a0.ObjTable_0c.nobj; i++) {
      if((a0.partInvisible_f4 & 1L << i) == 0) {
        final GsDOBJ2 dobj2 = a0.ObjTable_0c.top[i];

        GsGetLws(dobj2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setRotationMatrix(ls);
        GTE.setTranslationVector(ls.transfer);
        Renderer.renderDobj2(dobj2, false, 0);
      }
    }

    //LAB_800dab34
    if(a0.movementType_cc != 0) {
      FUN_800da524(a0);
    }

    //LAB_800dab4c
  }

  @Method(0x800dde70L)
  public static void FUN_800dde70(final Model124 struct, final int index) {
    final SmallerStruct smallerStruct = struct.smallerStructPtr_a4;

    if(smallerStruct.tmdSubExtensionArr_20[index] == null) {
      smallerStruct.uba_04[index] = false;
    } else {
      //LAB_800ddeac
      final int colourMap = (struct.colourMap_9d & 0x7f);
      final int x = _800503f8.get(colourMap).get();
      final int y = _80050424.get(colourMap).get() + 112;

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

  @Method(0x800de004L)
  public static void FUN_800de004(final Model124 model, final CContainer cContainer) {
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
  public static void FUN_800de138(final Model124 model, final int index) {
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

  /** TODO this method moves the player */
  @Method(0x800de1d0L)
  public static FlowControl FUN_800de1d0(final RunningScript<SubmapObject210> script) {
    final short deltaX = (short)script.params_20[0].get();
    final short deltaY = (short)script.params_20[1].get();
    final short deltaZ = (short)script.params_20[2].get();

    if(deltaX != 0 || deltaY != 0 || deltaZ != 0) {
      final SVECTOR deltaMovement = new SVECTOR();
      final SVECTOR worldspaceDeltaMovement = new SVECTOR();

      //LAB_800de218
      final SubmapObject210 player = script.scriptState_04.innerStruct_00;
      final Model124 playerModel = player.model_00;

      deltaMovement.set(deltaX, deltaY, deltaZ);
      SetRotMatrix(worldToScreenMatrix_800c3548);
      SetTransMatrix(worldToScreenMatrix_800c3548);
      transformToWorldspace(worldspaceDeltaMovement, deltaMovement);

      final int s2 = FUN_800e88a0(player.sobjIndex_12e, playerModel.coord2_14.coord, worldspaceDeltaMovement);
      if(s2 >= 0) {
        if(FUN_800e6798(s2, 0, playerModel.coord2_14.coord.transfer.getX(), playerModel.coord2_14.coord.transfer.getY(), playerModel.coord2_14.coord.transfer.getZ(), worldspaceDeltaMovement) != 0) {
          playerModel.coord2_14.coord.transfer.x.add(worldspaceDeltaMovement.getX());
          playerModel.coord2_14.coord.transfer.setY(worldspaceDeltaMovement.getY());
          playerModel.coord2_14.coord.transfer.z.add(worldspaceDeltaMovement.getZ());
        }

        //LAB_800de2c8
        player.ui_16c = s2;
      }

      //LAB_800de2cc
      player.us_170 = 0;
      sobjs_800c6880[player.sobjIndex_130].setTempTicker(SMap::FUN_800e3e60);
      _800c68e8.playerPos_00.set(worldspaceDeltaMovement);
    }

    //LAB_800de318
    return FlowControl.CONTINUE;
  }

  @Method(0x800de334L)
  public static FlowControl FUN_800de334(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    get3dAverageOfSomething(script.params_20[0].get(), sp0x10);
    playerModel_800c6748.coord2_14.coord.transfer.set(sp0x10);
    final MATRIX lw = new MATRIX();
    final MATRIX ls = new MATRIX();
    GsGetLws(playerModel_800c6748.coord2_14, lw, ls);
    GTE.setRotationMatrix(ls);
    GTE.setTranslationVector(ls.transfer);
    GTE.perspectiveTransform(0, 0, 0);
    final short x = GTE.getScreenX(2);
    final short y = GTE.getScreenY(2);

    //LAB_800de438
    final TriangleIndicator140 struct = _800c69fc;
    for(int i = 0; i < 20; i++) {
      if(struct._18[i] == -1) {
        struct.x_40[i] = x;
        struct.y_68[i] = y;
        struct._18[i] = (short)script.params_20[1].get();
        struct.screenOffsetX_90[i] = screenOffsetX_800cb568.get();
        struct.screenOffsetY_e0[i] = screenOffsetY_800cb56c.get();
        break;
      }
    }

    //LAB_800de49c
    return FlowControl.CONTINUE;
  }

  @Method(0x800de4b4L)
  public static FlowControl FUN_800de4b4(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    final MATRIX sp0x28 = new MATRIX();
    final MATRIX sp0x48 = new MATRIX();

    final Param ints = script.params_20[0];
    int s0 = 0;

    //LAB_800de4f8
    while(ints.array(s0).get() != -1) {
      get3dAverageOfSomething(ints.array(s0++).get(), sp0x10);
      playerModel_800c6748.coord2_14.coord.transfer.setX(sp0x10.getX());
      playerModel_800c6748.coord2_14.coord.transfer.setY(sp0x10.getY());
      playerModel_800c6748.coord2_14.coord.transfer.setZ(sp0x10.getZ());

      GsGetLws(playerModel_800c6748.coord2_14, sp0x48, sp0x28);
      GTE.setRotationMatrix(sp0x28);
      GTE.setTranslationVector(sp0x28.transfer);
      GTE.perspectiveTransform(0, 0, 0);
      final short x = GTE.getScreenX(2);
      final short y = GTE.getScreenY(2);

      //LAB_800de5d4
      for(int i = 0; i < 20; i++) {
        final TriangleIndicator140 a1 = _800c69fc;

        if(a1._18[i] == -1) {
          a1.x_40[i] = x;
          a1.y_68[i] = y;
          a1._18[i] = (short)ints.array(s0).get();
          a1.screenOffsetX_90[i] = screenOffsetX_800cb568.get();
          a1.screenOffsetY_e0[i] = screenOffsetY_800cb56c.get();
          break;
        }
      }

      s0++;
    }

    //LAB_800de644
    return FlowControl.CONTINUE;
  }

  /**
   * Something to do with forced animation. Used when Dart is halfway through his jump animation.
   */
  @Method(0x800de668L)
  public static FlowControl FUN_800de668(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    sobj.vec_138.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    sobj.i_144 = script.params_20[4].get();

    sobj.us_170 = 1;

    if(sobj.i_144 != 0) {
      sobj.vec_148.set(sobj.vec_138).sub(model.coord2_14.coord.transfer).div(sobj.i_144);
    } else {
      sobj.vec_148.set(0, 0, 0);
    }

    if(sobj.vec_148.getX() == 0) {
      if(sobj.vec_138.getX() < model.coord2_14.coord.transfer.getX()) {
        sobj.vec_148.setX(0x8000_0000);
      }
    }

    //LAB_800de750
    if(sobj.vec_148.getY() == 0) {
      if(sobj.vec_138.getY() < model.coord2_14.coord.transfer.getY()) {
        sobj.vec_148.setY(0x8000_0000);
      }
    }

    //LAB_800de77c
    if(sobj.vec_148.getZ() == 0) {
      if(sobj.vec_138.getZ() < model.coord2_14.coord.transfer.getZ()) {
        sobj.vec_148.setZ(0x8000_0000);
      }
    }

    //LAB_800de7a8
    int x = 0;
    int y = 0;
    int z = 0;
    if(sobj.i_144 != 0) {
      x = (sobj.vec_138.getX() - model.coord2_14.coord.transfer.getX() << 16) / sobj.i_144;
      if(sobj.vec_148.getX() < 0) {
        //LAB_800de7e0
        x = ~x + 1;
      }

      y = (sobj.vec_138.getY() - model.coord2_14.coord.transfer.getY() << 16) / sobj.i_144;
      if(sobj.vec_148.getY() < 0) {
        //LAB_800de84c
        y = ~y + 1;
      }

      z = (sobj.vec_138.getZ() - model.coord2_14.coord.transfer.getZ() << 16) / sobj.i_144;
      if(sobj.vec_148.getZ() < 0) {
        //LAB_800de8b8
        z = ~z + 1;
      }
    }

    //LAB_800de8e8
    sobj.vec_154.set(x & 0xffff, y & 0xffff, z & 0xffff);
    sobj.vec_160.set(0, 0, 0);

    sobjs_800c6880[sobj.sobjIndex_130].setTempTicker(SMap::FUN_800e1f90);

    sobj.flags_190 &= 0x7fff_ffff;
    return FlowControl.CONTINUE;
  }

  @Method(0x800de944L)
  public static FlowControl FUN_800de944(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    sobj.vec_138.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    final int a3 = script.params_20[4].get();
    sobj.i_144 = a3;

    sobj.vec_148.setX((sobj.vec_138.getX() - model.coord2_14.coord.transfer.getX()) / a3);
    sobj.vec_148.setZ((sobj.vec_138.getZ() - model.coord2_14.coord.transfer.getZ()) / a3);

    if(sobj.vec_148.getX() == 0 && sobj.vec_138.getX() < model.coord2_14.coord.transfer.getX()) {
      sobj.vec_148.setX(0x8000_0000);
    }

    //LAB_800dea08
    if(sobj.vec_148.getZ() == 0 && sobj.vec_138.getZ() < model.coord2_14.coord.transfer.getZ()) {
      sobj.vec_148.setZ(0x8000_0000);
    }

    //LAB_800dea34
    int x = (sobj.vec_138.getX() - model.coord2_14.coord.transfer.getX() << 16) / sobj.i_144;
    if(sobj.vec_148.getX() < 0) {
      //LAB_800dea6c
      x = ~x + 1;
    }

    //LAB_800dea9c
    sobj.vec_154.setX(x & 0xffff);

    int z = (sobj.vec_138.getZ() - model.coord2_14.coord.transfer.getZ() << 16) / sobj.i_144;
    if(sobj.vec_148.getZ() < 0) {
      //LAB_800dead8
      z = ~z + 1;
    }

    //LAB_800deb08
    sobj.vec_154.setZ(z & 0xffff);

    sobj.s_134 = ((sobj.vec_138.getY() - model.coord2_14.coord.transfer.getY()) * 2 - a3 * 7 * (a3 - 1)) / (a3 * 2);
    sobj.vec_160.setX(0);
    sobj.vec_160.setZ(0);
    sobj.us_170 = 2;
    sobj.s_172 = 1;
    sobj.ui_18c = 7;
    sobjs_800c6880[sobj.sobjIndex_130].setTempTicker(SMap::FUN_800e3e74);
    return FlowControl.CONTINUE;
  }

  @Method(0x800deba0L)
  public static FlowControl FUN_800deba0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.vec_138.setX(script.params_20[1].get());
    sobj.vec_138.setY(script.params_20[2].get());
    sobj.vec_138.setZ(script.params_20[3].get());
    final int a3 = script.params_20[4].get();
    sobj.i_144 = a3;
    sobj.ui_18c = _800f5ac0.get(script.params_20[5].get()).get();
    sobj.vec_148.setX((sobj.vec_138.getX() - sobj.model_00.coord2_14.coord.transfer.getX()) / a3);
    sobj.vec_148.setZ((sobj.vec_138.getZ() - sobj.model_00.coord2_14.coord.transfer.getZ()) / a3);

    if(sobj.vec_148.getX() == 0 && sobj.vec_138.getX() < sobj.model_00.coord2_14.coord.transfer.getX()) {
      sobj.vec_148.setX(0x8000_0000);
    }

    //LAB_800dec90
    if(sobj.vec_148.getZ() == 0 && sobj.vec_138.getZ() < sobj.model_00.coord2_14.coord.transfer.getZ()) {
      sobj.vec_148.setZ(0x8000_0000);
    }

    //LAB_800decbc
    int x = (sobj.vec_138.getX() - sobj.model_00.coord2_14.coord.transfer.getX() << 16) / sobj.i_144;
    if(sobj.vec_148.getX() < 0) {
      //LAB_800decf4
      x = ~x + 1;
    }

    //LAB_800ded24
    sobj.vec_154.setX(x & 0xffff);

    int z = (sobj.vec_138.getZ() - sobj.model_00.coord2_14.coord.transfer.getZ() << 16) / sobj.i_144;
    if(sobj.vec_148.getZ() < 0) {
      //LAB_800ded60
      z = ~z + 1;
    }

    //LAB_800ded90
    sobj.vec_154.setZ(z & 0xffff);

    sobj.s_174 = sobj.s_172;
    sobj.s_172 = 1;
    sobj.us_170 = 2;
    sobj.vec_160.setX(0);
    sobj.vec_160.setZ(0);
    sobj.s_134 = ((sobj.vec_138.getY() - sobj.model_00.coord2_14.coord.transfer.getY()) * 2 - a3 * sobj.ui_18c * (a3 - 1)) / (a3 * 2);
    sobjs_800c6880[sobj.sobjIndex_130].setTempTicker(SMap::FUN_800e3e74);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dee28L)
  public static FlowControl scriptCheckPlayerCollision(final RunningScript<SubmapObject210> script) {
    final SVECTOR deltaMovement = new SVECTOR();
    final SVECTOR movement = new SVECTOR();

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
      SetRotMatrix(worldToScreenMatrix_800c3548);
      SetTransMatrix(worldToScreenMatrix_800c3548);
      transformToWorldspace(movement, deltaMovement);

      final int collisionResult = FUN_800e88a0(sobj.sobjIndex_12e, model.coord2_14.coord, movement);
      if(collisionResult >= 0) {
        FUN_800e6798(collisionResult, 0, model.coord2_14.coord.transfer.getX(), model.coord2_14.coord.transfer.getY(), model.coord2_14.coord.transfer.getZ(), movement);
      }

      //LAB_800def08
      angle = MathHelper.positiveAtan2(movement.getZ(), movement.getX());
    } else {
      movement.set((short)0, (short)model.coord2_14.coord.transfer.getY(), (short)0);
      angle = model.coord2Param_64.rotate.y;
    }

    //LAB_800def28
    _800c68e8.playerMovement_0c.set(movement).add(model.coord2_14.coord.transfer);
    final int reachX = (int)(MathHelper.sin(angle) * -sobj.playerCollisionReach_1c0 / MathHelper.TWO_PI);
    final int reachZ = (int)(MathHelper.cos(angle) * -sobj.playerCollisionReach_1c0 / MathHelper.TWO_PI);
    final int colliderMinY = movement.getY() - sobj.playerCollisionSizeVertical_1bc;
    final int colliderMaxY = movement.getY() + sobj.playerCollisionSizeVertical_1bc;

    //LAB_800df008
    //LAB_800df00c
    //LAB_800df02c
    // Handle collision with other sobjs
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      final SubmapObject210 struct = sobjs_800c6880[i].innerStruct_00;

      if(struct != sobj && (struct.flags_190 & 0x10_0000) != 0) {
        final int x = struct.model_00.coord2_14.coord.transfer.getX() - (model.coord2_14.coord.transfer.getX() + movement.getX() + reachX);
        final int z = struct.model_00.coord2_14.coord.transfer.getZ() - (model.coord2_14.coord.transfer.getZ() + movement.getZ() + reachZ);
        final int size = sobj.playerCollisionSizeHorizontal_1b8 + struct.playerCollisionSizeHorizontal_1b8;
        final int collideeMinY = struct.model_00.coord2_14.coord.transfer.getY() - struct.playerCollisionSizeVertical_1bc;
        final int collideeMaxY = struct.model_00.coord2_14.coord.transfer.getY() + struct.playerCollisionSizeVertical_1bc;

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

  @Method(0x800df168L)
  public static FlowControl FUN_800df168(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800dfe0c(script);
  }

  @Method(0x800df198L)
  public static FlowControl FUN_800df198(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800dfec8(script);
  }

  @Method(0x800df1c8L)
  public static FlowControl FUN_800df1c8(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800dff68(script);
  }

  @Method(0x800df1f8L)
  public static FlowControl FUN_800df1f8(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800dffa4(script);
  }

  @Method(0x800df228L)
  public static FlowControl FUN_800df228(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800dffdc(script);
  }

  @Method(0x800df258L)
  public static FlowControl scriptSetModelPosition(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    model.coord2_14.coord.transfer.setX(script.params_20[1].get());
    model.coord2_14.coord.transfer.setY(script.params_20[2].get());
    model.coord2_14.coord.transfer.setZ(script.params_20[3].get());
    sobj.us_170 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800df2b8L)
  public static FlowControl scriptReadModelPosition(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    script.params_20[1].set(model.coord2_14.coord.transfer.getX());
    script.params_20[2].set(model.coord2_14.coord.transfer.getY());
    script.params_20[3].set(model.coord2_14.coord.transfer.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800df314L)
  public static FlowControl scriptSetModelRotate(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    model.coord2Param_64.rotate.set(MathHelper.psxDegToRad(script.params_20[1].get()), MathHelper.psxDegToRad(script.params_20[2].get()), MathHelper.psxDegToRad(script.params_20[3].get()));
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800df374L)
  public static FlowControl scriptReadModelRotate(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    script.params_20[1].set(MathHelper.radToPsxDeg(model.coord2Param_64.rotate.x));
    script.params_20[2].set(MathHelper.radToPsxDeg(model.coord2Param_64.rotate.y));
    script.params_20[3].set(MathHelper.radToPsxDeg(model.coord2Param_64.rotate.z));
    return FlowControl.CONTINUE;
  }

  @Method(0x800df3d0L)
  public static FlowControl FUN_800df3d0(final RunningScript<?> script) {
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return scriptFacePoint(script);
  }

  @Method(0x800df410L)
  public static FlowControl FUN_800df410(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e0094(script);
  }

  @Method(0x800df440L)
  public static FlowControl FUN_800df440(final RunningScript<?> script) {
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800de668(script);
  }

  @Method(0x800df488L)
  public static FlowControl FUN_800df488(final RunningScript<?> script) {
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800de944(script);
  }

  @Method(0x800df4d0L)
  public static FlowControl FUN_800df4d0(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e00cc(script);
  }

  @Method(0x800df500L)
  public static FlowControl FUN_800df500(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800df530L)
  public static FlowControl FUN_800df530(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e0184(script);
  }

  @Method(0x800df560L)
  public static FlowControl FUN_800df560(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800df590L)
  public static FlowControl FUN_800df590(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800df5c0L)
  public static FlowControl FUN_800df5c0(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return scriptEnableTextureAnimation(script);
  }

  @Method(0x800df5f0L)
  public static FlowControl FUN_800df5f0(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return scriptDisableTextureAnimation(script);
  }

  @Method(0x800df620L)
  public static FlowControl FUN_800df620(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e02c0(script);
  }

  @Method(0x800df650L)
  public static FlowControl FUN_800df650(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e02fc(script);
  }

  @Method(0x800df680L)
  public static FlowControl FUN_800df680(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)script.scriptState_04.innerStruct_00;
    script.params_20[0].set(sobj.s_178);
    return FlowControl.CONTINUE;
  }

  @Method(0x800df6a4L)
  public static FlowControl FUN_800df6a4(final RunningScript<?> script) {
    SetRotMatrix(worldToScreenMatrix_800c3548);
    SetTransMatrix(worldToScreenMatrix_800c3548);
    FUN_800e8104(new SVECTOR().set((short)script.params_20[0].get(), (short)script.params_20[0].get(), (short)script.params_20[0].get()));

    //LAB_800df744
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      final SubmapObject210 sobj = sobjs_800c6880[i].innerStruct_00;
      sobj.s_178 = 0;
    }

    //LAB_800df774
    return FlowControl.CONTINUE;
  }

  /**
   * The (x, y, z) value is the full amount to rotate, i.e. it rotates by `(x, y, z) / frames` units per frame
   *
   * Used for the little mouse thing running around in the Limestone Cave
   */
  @Method(0x800df788L)
  public static FlowControl scriptRotateSobj(final RunningScript<?> script) {
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
  @Method(0x800df890L)
  public static FlowControl scriptRotateSobjAbsolute(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.rotationAmount_17c.set(
      MathHelper.psxDegToRad(script.params_20[1].get()),
      MathHelper.psxDegToRad(script.params_20[2].get()),
      MathHelper.psxDegToRad(script.params_20[3].get())
    );
    sobj.rotationFrames_188 = script.params_20[4].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800df904L)
  public static FlowControl FUN_800df904(final RunningScript<?> script) {
    script.params_20[5] = script.params_20[4];
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800deba0(script);
  }

  @Method(0x800df954L)
  public static FlowControl scriptFacePlayer(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)script.scriptState_04.innerStruct_00;
    sobj.model_00.coord2Param_64.rotate.y = MathHelper.positiveAtan2(_800c68e8.playerPos_00.getZ(), _800c68e8.playerPos_00.getX());
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800df9a8L)
  public static FlowControl FUN_800df9a8(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final MATRIX sp0x20 = new MATRIX();
    final MATRIX sp0x40 = new MATRIX();
    GsGetLws(sobj.model_00.coord2_14, sp0x40, sp0x20);

    GTE.setRotationMatrix(sp0x20);
    GTE.setTranslationVector(sp0x20.transfer);
    GTE.perspectiveTransform(0, 0, 0);
    script.params_20[1].set(GTE.getScreenX(2) + 192);
    script.params_20[2].set(GTE.getScreenY(2) + 128);

    GTE.perspectiveTransform(0, -130, 0);
    script.params_20[3].set(GTE.getScreenX(2) + 192);
    script.params_20[4].set(GTE.getScreenY(2) + 128);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dfb28L)
  public static FlowControl FUN_800dfb28(final RunningScript<?> script) {
    script.params_20[0].set(submapIndex_800bd808.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800dfb44L)
  public static FlowControl FUN_800dfb44(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return scriptGetSobjNobj(script);
  }

  @Method(0x800dfb74L)
  public static FlowControl FUN_800dfb74(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e03e4(script);
  }

  @Method(0x800dfba4L)
  public static FlowControl FUN_800dfba4(final RunningScript<?> script) {
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e0448(script);
  }

  @Method(0x800dfbd4L)
  public static FlowControl FUN_800dfbd4(final RunningScript<?> script) {
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return scriptFaceCamera(script);
  }

  @Method(0x800dfc00L)
  public static FlowControl scriptScaleXyz(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.scaleVector_fc.set(
      (script.params_20[1].get() / (float)0x1000),
      (script.params_20[2].get() / (float)0x1000),
      (script.params_20[3].get() / (float)0x1000)
    );

    return FlowControl.CONTINUE;
  }

  @Method(0x800dfc60L)
  public static FlowControl scriptScaleUniform(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.scaleVector_fc.x = script.params_20[1].get() / (float)0x1000;
    model.scaleVector_fc.y = script.params_20[1].get() / (float)0x1000;
    model.scaleVector_fc.z = script.params_20[1].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @Method(0x800dfca0L)
  public static FlowControl FUN_800dfca0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.zOffset_a0 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800dfcd8L)
  public static FlowControl FUN_800dfcd8(final RunningScript<?> script) {
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800e0520(script);
  }

  @Method(0x800dfd10L)
  public static FlowControl FUN_800dfd10(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800dfd48L)
  public static FlowControl FUN_800dfd48(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800dfd8cL)
  public static FlowControl scriptShowAlertIndicator(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.showAlertIndicator_194 = true;
    sobj.alertIndicatorOffsetY_198 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800dfdd8L)
  public static FlowControl scriptHideAlertIndicator(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.showAlertIndicator_194 = false;
    return FlowControl.CONTINUE;
  }

  @Method(0x800dfe0cL)
  public static FlowControl FUN_800dfe0c(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final int index = script.params_20[1].get();

    sobj.sobjIndex_12e = index;
    model.colourMap_9d = (int)submapObjectFlags_800c6a50.offset(index * 0x4L).get();

    FUN_800e0d18(model, submapAssets.objects.get(index).model, submapAssets.objects.get(index).animations.get(0));

    sobj.us_12c = 0;
    sobj.rotationFrames_188 = 0;

    return FlowControl.CONTINUE;
  }

  @Method(0x800dfec8L)
  public static FlowControl FUN_800dfec8(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    sobj.animIndex_132 = script.params_20[1].get();
    model.ub_a2 = 0;
    model.ub_a3 = 0;

    loadModelStandardAnimation(model, submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));

    sobj.us_12c = 0;
    sobj.flags_190 &= 0x9fff_ffff;

    return FlowControl.CONTINUE;
  }

  @Method(0x800dff68L)
  public static FlowControl FUN_800dff68(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.animIndex_132);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dffa4L)
  public static FlowControl FUN_800dffa4(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.us_12a = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800dffdcL)
  public static FlowControl FUN_800dffdc(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.us_12c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0018L)
  public static FlowControl scriptFacePoint(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    model.coord2Param_64.rotate.y = MathHelper.positiveAtan2(script.params_20[3].get() - model.coord2_14.coord.transfer.getZ(), script.params_20[1].get() - model.coord2_14.coord.transfer.getX());
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0094L)
  public static FlowControl FUN_800e0094(final RunningScript<?> a0) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[a0.params_20[0].get()].innerStruct_00;
    sobj.s_128 = a0.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e00ccL)
  public static FlowControl FUN_800e00cc(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;
    final int v0 = FUN_800e9018(model.coord2_14.coord.transfer.getX(), model.coord2_14.coord.transfer.getY(), model.coord2_14.coord.transfer.getZ(), 0);
    script.params_20[1].set(v0);
    sobj.ui_16c = v0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0148L)
  public static FlowControl FUN_800e0148(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.s_172);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0184L)
  public static FlowControl FUN_800e0184(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.s_172 = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e01bcL)
  public static FlowControl FUN_800e01bc(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    FUN_800de138(sobj.model_00, script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0204L)
  public static FlowControl FUN_800e0204(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.smallerStructPtr_a4.uba_04[script.params_20[1].get()] = false;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0244L)
  public static FlowControl scriptEnableTextureAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.animateTextures_ec[script.params_20[1].get()] = true;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0284L)
  public static FlowControl scriptDisableTextureAnimation(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.animateTextures_ec[script.params_20[1].get()] = false;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e02c0L)
  public static FlowControl FUN_800e02c0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.us_170);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e02fcL)
  public static FlowControl FUN_800e02fc(final RunningScript<?> script) {
    final SubmapObject210 struct1 = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    struct1.s_178 = script.params_20[1].get();

    if(script.params_20[1].get() != 0) {
      //LAB_800e035c
      for(int i = 0; i < sobjCount_800c6730.get(); i++) {
        final SubmapObject210 struct2 = sobjs_800c6880[i].innerStruct_00;

        if(struct2.sobjIndex_130 != struct1.sobjIndex_130) {
          struct2.s_178 = 0;
        }

        //LAB_800e0390
      }
    }

    //LAB_800e03a0
    return FlowControl.CONTINUE;
  }

  @Method(0x800e03a8L)
  public static FlowControl scriptGetSobjNobj(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.model_00.ObjTable_0c.nobj);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e03e4L)
  public static FlowControl FUN_800e03e4(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final int shift = script.params_20[1].get();

    //LAB_800e0430
    model.partInvisible_f4 |= 0x1L << shift;

    //LAB_800e0440
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0448L)
  public static FlowControl FUN_800e0448(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    final int shift = script.params_20[1].get();

    //LAB_800e0498
    model.partInvisible_f4 &= ~(0x1L << shift);

    //LAB_800e04ac
    return FlowControl.CONTINUE;
  }

  @Method(0x800e04b4L)
  public static FlowControl scriptFaceCamera(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.coord2Param_64.rotate.y = MathHelper.positiveAtan2(cameraPos_800c6aa0.getZ(), cameraPos_800c6aa0.getX());
    sobj.rotationFrames_188 = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0520L)
  public static FlowControl FUN_800e0520(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    sobj.flags_190 = sobj.flags_190
      & ~(0x1 << script.params_20[1].get())
      | (script.params_20[2].get() & 0x1) << script.params_20[1].get();

    return FlowControl.CONTINUE;
  }

  @Method(0x800e057cL)
  public static FlowControl FUN_800e057c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800e05c8L)
  public static FlowControl FUN_800e05c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800e05f0L)
  public static FlowControl FUN_800e05f0(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800e0614L)
  public static FlowControl FUN_800e0614(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.flatLightingEnabled_1c4 = true;
    sobj.flatLightRed_1c5 = script.params_20[1].get() & 0xff;
    sobj.flatLightGreen_1c6 = script.params_20[2].get() & 0xff;
    sobj.flatLightBlue_1c7 = script.params_20[3].get() & 0xff;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0684L)
  public static FlowControl FUN_800e0684(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.flatLightingEnabled_1c4 = false;
    sobj.flatLightRed_1c5 = 0x80;
    sobj.flatLightGreen_1c6 = 0x80;
    sobj.flatLightBlue_1c7 = 0x80;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e074cL)
  public static FlowControl FUN_800e074c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800e07f0L)
  public static FlowControl FUN_800e07f0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    sobj.animIndex_132 = script.params_20[1].get();
    model.ub_a3 = 0;
    model.ub_a2 = 1;
    loadModelStandardAnimation(model, submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));
    sobj.us_12c = 0;
    sobj.flags_190 &= 0x9fff_ffff;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0894L)
  public static FlowControl FUN_800e0894(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.collisionSizeHorizontal_1ac = script.params_20[1].get();
    sobj.collisionSizeVertical_1b0 = script.params_20[2].get();
    sobj.collisionReach_1b4 = script.params_20[3].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e08f4L)
  public static FlowControl FUN_800e08f4(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(sobj.collidedWithSobjIndex_1a8);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0930L)
  public static FlowControl scriptSetAmbientColour(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.ambientColourEnabled_1c8 = true;
    sobj.ambientColour_1ca.set(
      (script.params_20[1].get() & 0xffff) / 4096.0f,
      (script.params_20[2].get() & 0xffff) / 4096.0f,
      (script.params_20[3].get() & 0xffff) / 4096.0f
    );
    return FlowControl.CONTINUE;
  }

  @Method(0x800e09a0L)
  public static FlowControl scriptResetAmbientColour(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.ambientColour_1ca.set(0.5f, 0.5f, 0.5f);
    sobj.ambientColourEnabled_1c8 = false;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e09e0L)
  public static FlowControl FUN_800e09e0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.movementType_cc = 1;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0a14L)
  public static FlowControl FUN_800e0a14(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.model_00.movementType_cc = 0;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0a48L)
  public static FlowControl FUN_800e0a48(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.vector_10c.x = script.params_20[1].get() / (float)0x1000;
    model.vector_10c.z = script.params_20[2].get() / (float)0x1000;
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0a94L)
  public static FlowControl FUN_800e0a94(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final Model124 model = sobj.model_00;

    model.vector_118.setX(script.params_20[1].get());
    model.vector_118.setY(script.params_20[2].get());
    model.vector_118.setZ(script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0af4L)
  public static FlowControl scriptGetPlayerMovement(final RunningScript<?> script) {
    script.params_20[0].set(_800c68e8.playerMovement_0c.getX());
    script.params_20[1].set(_800c68e8.playerMovement_0c.getY());
    script.params_20[2].set(_800c68e8.playerMovement_0c.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0b34L)
  public static FlowControl FUN_800e0b34(final RunningScript<?> script) {
    if(script.params_20[0].get() == 0) {
      loadTimImage(_80010544.getAddress());
    }

    //LAB_800e0b68
    if(script.params_20[0].get() == 1) {
      loadTimImage(_800d673c.getAddress());
    }

    //LAB_800e0b8c
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0ba0L)
  public static FlowControl FUN_800e0ba0(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.playerCollisionSizeHorizontal_1b8 = script.params_20[1].get();
    sobj.playerCollisionSizeVertical_1bc = script.params_20[2].get();
    sobj.playerCollisionReach_1c0 = script.params_20[3].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0c00L)
  public static FlowControl scriptLoadChapterTitleCard(final RunningScript<?> script) {
    chapterTitleCardLoaded_800c68e0.set(false);
    chapterTitleNum_800c6738.set(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0c24L)
  public static FlowControl scriptIsChapterTitleCardLoaded(final RunningScript<?> script) {
    script.params_20[0].set(chapterTitleCardLoaded_800c68e0.get() ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0c40L)
  public static FlowControl FUN_800e0c40(final RunningScript<?> script) {
    chapterTitleNum_800c6738.or(0x80);
    _800c686e.setu(0);
    _800c687c.setu(script.params_20[0].get());
    _800c687e.setu(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0c80L)
  public static FlowControl FUN_800e0c80(final RunningScript<?> script) {
    script.params_20[0].set((int)_800c686e.getSigned());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0c9cL)
  public static FlowControl FUN_800e0c9c(final RunningScript<?> script) {
    _800c673c.setu(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0cb8L)
  public static FlowControl FUN_800e0cb8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800e0d18L)
  public static void FUN_800e0d18(final Model124 model, final CContainer cContainer, final TmdAnimationFile tmdAnimFile) {
    final int transferX = model.coord2_14.coord.transfer.getX();
    final int transferY = model.coord2_14.coord.transfer.getY();
    final int transferZ = model.coord2_14.coord.transfer.getZ();

    //LAB_800e0d5c
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    final int count = cContainer.tmdPtr_00.tmd.header.nobj;
    model.count_c8 = count;
    model.dobj2ArrPtr_00 = new GsDOBJ2[count];
    model.coord2ArrPtr_04 = new GsCOORDINATE2[count];
    model.coord2ParamArrPtr_08 = new GsCOORD2PARAM[count];
    model.tmd_8c = cContainer.tmdPtr_00.tmd;
    model.tmdNobj_ca = count;

    Arrays.setAll(model.dobj2ArrPtr_00, i -> new GsDOBJ2());
    Arrays.setAll(model.coord2ArrPtr_04, i -> new GsCOORDINATE2());
    Arrays.setAll(model.coord2ParamArrPtr_08, i -> new GsCOORD2PARAM());

    if(cContainer.ext_04 != null) {
      final SmallerStruct smallerStruct = new SmallerStruct();
      model.smallerStructPtr_a4 = smallerStruct;
      smallerStruct.tmdExt_00 = cContainer.ext_04;

      //LAB_800e0e28
      for(int i = 0; i < 4; i++) {
        smallerStruct.tmdSubExtensionArr_20[i] = smallerStruct.tmdExt_00.tmdSubExtensionArr_00[i];
        FUN_800de138(model, i);
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
      model.ptr_a8 = null; //TODO was this needed? cContainer.ptr_08.getAddress();

      //LAB_800e0f00
      for(int i = 0; i < 7; i++) {
        model.ptrs_d0[i] = null;
      }
    }

    //LAB_800e0f10
    initObjTable2(model.ObjTable_0c, model.dobj2ArrPtr_00, model.coord2ArrPtr_04, model.coord2ParamArrPtr_08, model.count_c8);

    model.coord2_14.param = model.coord2Param_64;

    GsInitCoordinate2(null, model.coord2_14);
    prepareObjTable2(model.ObjTable_0c, model.tmd_8c, model.coord2_14, model.count_c8, model.tmdNobj_ca + 1);

    model.zOffset_a0 = 0;
    model.ub_a2 = 0;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;

    loadModelStandardAnimation(model, tmdAnimFile);

    model.coord2_14.coord.transfer.setX(transferX);
    model.coord2_14.coord.transfer.setY(transferY);
    model.coord2_14.coord.transfer.setZ(transferZ);

    model.scaleVector_fc.x = 1.0f;
    model.scaleVector_fc.y = 1.0f;
    model.scaleVector_fc.z = 1.0f;
  }

  @Method(0x800e0ff0L)
  public static void submapObjectTicker(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    if(sobj.s_178 != 0) {
      SetRotMatrix(worldToScreenMatrix_800c3548);
      SetTransMatrix(worldToScreenMatrix_800c3548);
      FUN_800e8104(new SVECTOR().set(model.coord2_14.coord.transfer));
    }

    if(sobj.s_128 == 0) {
      if(sobj.rotationFrames_188 != 0) {
        sobj.rotationFrames_188--;
        model.coord2Param_64.rotate.add(sobj.rotationAmount_17c);
      }

      if(sobj.sobjIndex_12e == 0) {
        FUN_800217a4(model);
      } else {
        applyModelRotationAndScale(model);
      }

      if(sobj.us_12a == 0) {
        animateModel(model);
        if(sobj.us_12c == 1 && (sobj.flags_190 & 0x2000_0000) != 0) {
          sobj.animIndex_132 = 0;
          loadModelStandardAnimation(model, submapAssets.objects.get(sobj.sobjIndex_12e).animations.get(sobj.animIndex_132));
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
      renderAlertIndicator(sobj.model_00, sobj.alertIndicatorOffsetY_198);
    }

    if((sobj.flags_190 & 0x800_0000) != 0) {
      FUN_800e4378(sobj, 0x1000_0000L);
    }

    if((sobj.flags_190 & 0x200_0000) != 0) {
      FUN_800e4378(sobj, 0x400_0000L);
    }

    if((sobj.flags_190 & 0x80_0000) != 0) {
      FUN_800e450c(sobj, 0x100_0000L);
    }

    if((sobj.flags_190 & 0x20_0000) != 0) {
      FUN_800e450c(sobj, 0x40_0000L);
    }

    if(enableCollisionDebug) {
      renderCollisionDebug(state, sobj);
    }
  }

  public static boolean enableCollisionDebug;

  private static void renderCollisionDebug(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    SetRotMatrix(worldToScreenMatrix_800c3548);
    SetTransMatrix(worldToScreenMatrix_800c3548);

    final IntRef x0 = new IntRef();
    final IntRef y0 = new IntRef();
    final IntRef x1 = new IntRef();
    final IntRef y1 = new IntRef();

    if((sobj.flags_190 & 0x200_0000) != 0 || (sobj.flags_190 & 0x800_0000) != 0) {
      transformCollisionVertices(model, sobj.collisionSizeHorizontal_1a0, 0, x0, y0, x1, y1);
      queueCollisionRectPacket(x0.get(), y0.get(), x1.get(), y1.get(), 0x80_0000);
    }

    if((sobj.flags_190 & 0x20_0000) != 0 || (sobj.flags_190 & 0x80_0000) != 0) {
      transformCollisionVertices(model, sobj.collisionSizeHorizontal_1ac, sobj.collisionReach_1b4, x0, y0, x1, y1);
      queueCollisionRectPacket(x0.get(), y0.get(), x1.get(), y1.get(), 0x8000);
    }

    if(sobjs_800c6880[0] == state) {
      transformCollisionVertices(model, sobj.playerCollisionSizeHorizontal_1b8, sobj.playerCollisionReach_1c0, x0, y0, x1, y1);
      queueCollisionRectPacket(x0.get(), y0.get(), x1.get(), y1.get(), 0x80);
    }
  }

  private static void transformCollisionVertices(final Model124 model, final int size, final int reach, final IntRef x0, final IntRef y0, final IntRef x1, final IntRef y1) {
    final int reachX;
    final int reachZ;
    if(reach != 0) {
      reachX = (int)(MathHelper.sin(model.coord2Param_64.rotate.y) * -reach);
      reachZ = (int)(MathHelper.cos(model.coord2Param_64.rotate.y) * -reach);
    } else {
      reachX = 0;
      reachZ = 0;
    }

    final SVECTOR coord = new SVECTOR().set(model.coord2_14.coord.transfer).add((short)reachX, (short)0, (short)reachZ);
    transformVertex(x0, y0, coord.sub((short)(size / 2), (short)0, (short)(size / 2)));
    transformVertex(x1, y1, coord.add((short)size, (short)0, (short)size));
  }

  private static void queueCollisionRectPacket(final int x0, final int y0, final int x1, final int y1, final int colour) {
    GPU.queueCommand(37, new GpuCommandPoly(4)
      .translucent(Translucency.B_PLUS_F)
      .rgb(colour)
      .pos(0, x0, y0)
      .pos(1, x1, y0)
      .pos(2, x0, y1)
      .pos(3, x1, y1)
    );
  }

  @Method(0x800e123cL)
  public static void submapObjectRenderer(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    if(sobj.s_128 == 0) {
      if(sobj.flatLightingEnabled_1c4) {
        final GsF_LIGHT light = new GsF_LIGHT();

        light.direction_00.setX(0);
        light.direction_00.setY(0x1000);
        light.direction_00.setZ(0);
        light.r_0c = sobj.flatLightRed_1c5;
        light.g_0d = sobj.flatLightGreen_1c6;
        light.b_0e = sobj.flatLightBlue_1c7;
        GsSetFlatLight(0, light);

        light.direction_00.setX(0x1000);
        light.direction_00.setY(0);
        light.direction_00.setZ(0);
        light.r_0c = sobj.flatLightRed_1c5;
        light.g_0d = sobj.flatLightGreen_1c6;
        light.b_0e = sobj.flatLightBlue_1c7;
        GsSetFlatLight(1, light);

        light.direction_00.setX(0);
        light.direction_00.setY(0);
        light.direction_00.setZ(0x1000);
        light.r_0c = sobj.flatLightRed_1c5;
        light.g_0d = sobj.flatLightGreen_1c6;
        light.b_0e = sobj.flatLightBlue_1c7;
        GsSetFlatLight(2, light);
      }

      //LAB_800e1310
      if(sobj.ambientColourEnabled_1c8) {
        GTE.setBackgroundColour(sobj.ambientColour_1ca.x, sobj.ambientColour_1ca.y, sobj.ambientColour_1ca.z);
      }

      //LAB_800e1334
      renderModel(sobj.model_00);

      if(sobj.flatLightingEnabled_1c4) {
        GsSetFlatLight(0, GsF_LIGHT_0_800c66d8);
        GsSetFlatLight(1, GsF_LIGHT_1_800c66e8);
        GsSetFlatLight(2, GsF_LIGHT_2_800c66f8);
      }

      //LAB_800e1374
      if(sobj.ambientColourEnabled_1c8) {
        GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
      }

      //LAB_800e1390
      FUN_800ef0f8(sobj.model_00, sobj._1d0);
    }

    //LAB_800e139c
  }

  @Method(0x800e13b0L)
  public static void executeSceneGraphicsLoadingStage(final int index) {
    switch((int)loadingStage_800c68e4.get()) {
      case 0 -> {
        loadTimImage(_80010544.getAddress());

        if(_80050274.get() != submapCut_80052c30.get()) {
          _800bda08.set(_80050274.get());
          _80050274.set(submapCut_80052c30.get());
        }

        //LAB_800e1440
        GsF_LIGHT_0_800c66d8.direction_00.setX(0);
        GsF_LIGHT_0_800c66d8.direction_00.setY(0x1000);
        GsF_LIGHT_0_800c66d8.direction_00.setZ(0);
        GsF_LIGHT_0_800c66d8.r_0c = 0x80;
        GsF_LIGHT_0_800c66d8.g_0d = 0x80;
        GsF_LIGHT_0_800c66d8.b_0e = 0x80;
        GsSetFlatLight(0, GsF_LIGHT_0_800c66d8);
        GsF_LIGHT_1_800c66e8.direction_00.setX(0);
        GsF_LIGHT_1_800c66e8.direction_00.setY(0x1000);
        GsF_LIGHT_1_800c66e8.direction_00.setZ(0);
        GsF_LIGHT_1_800c66e8.r_0c = 0;
        GsF_LIGHT_1_800c66e8.g_0d = 0;
        GsF_LIGHT_1_800c66e8.b_0e = 0;
        GsSetFlatLight(1, GsF_LIGHT_1_800c66e8);
        GsF_LIGHT_2_800c66f8.direction_00.setX(0);
        GsF_LIGHT_2_800c66f8.direction_00.setY(0x1000);
        GsF_LIGHT_2_800c66f8.direction_00.setZ(0);
        GsF_LIGHT_2_800c66f8.r_0c = 0;
        GsF_LIGHT_2_800c66f8.g_0d = 0;
        GsF_LIGHT_2_800c66f8.b_0e = 0;
        GsSetFlatLight(2, GsF_LIGHT_2_800c66f8);

        GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
        loadingStage_800c68e4.addu(0x1L);
      }

      case 1 -> {
        if(index == -1) {
          break;
        }

        final int oldSubmapIndex = submapIndex_800bd808.get();
        submapIndex_800bd808.set(cutToSubmap_800d610c.get(submapCut_80052c30.get()).get());

        //LAB_800e15b8
        //LAB_800e15ac
        //LAB_800e15b8
        //LAB_800e15b8
        if(submapIndex_800bd808.get() != oldSubmapIndex) { // Reload sounds when changing submap
          FUN_8001ad18();
          unloadSoundFile(4);
          loadSubmapSounds(submapIndex_800bd808.get());
        } else {
          //LAB_800e1550
          if(_800bda08.get() == submapCut_80052c30.get()) {
            //LAB_800e15d0
            loadingStage_800c68e4.addu(0x1L);
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
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        if(ret == -2) {
          FUN_8001ada0();

          //LAB_800e15b8
          musicLoaded_800bd782 = true;
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        if(ret == -3) {
          //LAB_800e15b8
          musicLoaded_800bd782 = true;
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        //LAB_800e15c0
        loadMusicPackage(ret, 0);
        loadingStage_800c68e4.addu(0x1L);
      }

      case 2 -> {
        if(musicLoaded_800bd782 && (getLoadedDrgnFiles() & 0x2) == 0) {
          loadingStage_800c68e4.addu(0x1L);
        }
      }

      case 3 -> {
        _800f9eac.set(0);

        loadSmapMedia();

        if(_800f9eac.get() == 0x1L) {
          loadingStage_800c68e4.addu(0x1L);
        }
      }

      case 4 -> {
        loadSmapMedia();

        if(_800f9eac.get() == 0x2L) {
          loadingStage_800c68e4.addu(0x1L);
        }
      }

      // Load map assets
      case 5 -> {
        assert submapAssetsMrg_800c6878 == null : "Submap assets MRG was not empty";
        assert submapScriptsMrg_800c68d8 == null : "Submap scripts MRG was not empty";

        submapScriptsLoaded_800c68d0.set(false);
        submapAssetsLoaded_800c6874.set(false);

        final IntRef drgnIndex = new IntRef();
        final IntRef fileIndex = new IntRef();

        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);

        if(drgnIndex.get() == 1 || drgnIndex.get() == 2 || drgnIndex.get() == 3 || drgnIndex.get() == 4) {
          //LAB_800e1720
          //LAB_800e17c4
          //LAB_800e17d8
          // Submap data (file example: 695)
          loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 1, files -> SMap.submapAssetsLoadedCallback(files, 0));
          // Submap scripts (file example: 696)
          loadDrgnDir(drgnIndex.get() + 2, fileIndex.get() + 2, files -> SMap.submapAssetsLoadedCallback(files, 1));
        }

        loadingStage_800c68e4.addu(0x1L);
      }

      // Wait for map assets to load
      case 6 -> {
        if(submapAssetsLoaded_800c6874.get() && submapScriptsLoaded_800c68d0.get()) {
          final int objCount = submapScriptsMrg_800c68d8.size() - 2;

          submapAssets = new SubmapAssets();
          submapAssets.lastEntry = submapScriptsMrg_800c68d8.get(objCount + 1).getBytes();
          submapAssets.script = new ScriptFile("Submap controller", submapScriptsMrg_800c68d8.get(0).getBytes());

          for(int objIndex = 0; objIndex < objCount; objIndex++) {
            final byte[] scriptData = submapScriptsMrg_800c68d8.get(objIndex + 1).getBytes();

            final FileData submapModel = submapAssetsMrg_800c6878.get(objIndex * 33);

            final IntRef drgnIndex = new IntRef();
            final IntRef fileIndex = new IntRef();
            getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);

            final SubmapObject obj = new SubmapObject();
            obj.script = new ScriptFile("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 2, objIndex + 1), scriptData);

            if(submapModel.hasVirtualSize() && submapModel.real()) {
              obj.model = new CContainer("Submap object %d (DRGN%d/%d/%d)".formatted(objIndex, drgnIndex.get(), fileIndex.get() + 1, objIndex * 33), new FileData(submapModel.getBytes()));
            } else {
              obj.model = null;
            }

            for(int animIndex = objIndex * 33 + 1; animIndex < (objIndex + 1) * 33; animIndex++) {
              final FileData data = submapAssetsMrg_800c6878.get(animIndex);

              // This is a stupid fix for a stupid retail bug where almost all
              // sobj animations in DRGN24.938 are symlinked to a PXL file
              // GH#292
              if(data.readInt(0) == 0x11) {
                obj.animations.add(null);
                continue;
              }

              obj.animations.add(new TmdAnimationFile(data));
            }

            submapAssets.objects.add(obj);
          }

          // Get models that are symlinked
          for(int objIndex = 0; objIndex < objCount; objIndex++) {
            final SubmapObject obj = submapAssets.objects.get(objIndex);

            if(obj.model == null) {
              final FileData submapModel = submapAssetsMrg_800c6878.get(objIndex * 33);

              obj.model = submapAssets.objects.get(submapModel.realFileIndex() / 33).model;
            }
          }

          for(int i = 0; i < 3; i++) {
            submapAssets.pxls.add(new Tim(submapAssetsMrg_800c6878.get(objCount * 34 + i)));
          }

          loadingStage_800c68e4.addu(0x1L);
        }
      }

      case 7 -> {
        _800c6870.setu(0);
        _800c686c.setu(0);
        loadingStage_800c68e4.addu(0x1L);
        callbackIndex_800c6968.setu(_800f5cd4.offset(submapCut_80052c30.get() * 0x2L));
      }

      case 8 -> {
        callbackArr_800f5ad4[(int)callbackIndex_800c6968.get()].run();

        if(_800c686c.get() != 0) {
          //LAB_800e18a4
          //LAB_800e18a8
          loadingStage_800c68e4.addu(0x1L);
        }
      }

      // Load submap objects
      case 9 -> {
        FUN_800218f0();

        final int sobjCount = submapAssets.objects.size();

        _800c672c.setu(sobjCount);
        sobjCount_800c6730.set(sobjCount);

        final long s3;
        final long s4;
        final byte[] lastEntry = submapAssets.lastEntry;
        if(lastEntry.length != 4) {
          s3 = MathHelper.get(lastEntry, 0, 4); // Second last int before padding
          s4 = MathHelper.get(lastEntry, 4, 4); // Last int before padding
        } else {
          s3 = 0;
          s4 = 0;
        }

        //LAB_800e1914
        submapAssets.pxls.get(0).uploadToGpu();
        submapAssets.pxls.get(1).uploadToGpu();

        final Tim tim = submapAssets.pxls.get(2);
        final RECT imageRect = new RECT();
        imageRect.set(tim.getImageRect());
        imageRect.h.set((short)128);

        GPU.uploadData(imageRect, tim.getImageData());

        final ScriptState<Void> submapController = SCRIPTS.allocateScriptState(0, "Submap controller", 0, null);
        submapControllerState_800c6740 = submapController;
        submapController.loadScriptFile(submapAssets.script);

        //LAB_800e1a38
        for(int i = 0; i < sobjCount_800c6730.get(); i++) {
          submapObjectFlags_800c6a50.offset(i * 0x4L).setu(i + 0x81L);

          if(i + 1 == s3) {
            submapObjectFlags_800c6a50.offset(i * 0x4L).setu(0x92L);
          }

          //LAB_800e1a74
          if(i + 1 == s4) {
            submapObjectFlags_800c6a50.offset(i * 0x4L).setu(0x93L);
          }

          //LAB_800e1a80
        }

        //LAB_800e1a8c
        //LAB_800e1abc
        //TODO make sure these loops are right
        for(int i = 0; i < sobjCount_800c6730.get(); i++) {
          //LAB_800e1ae0
          for(int n = i + 1; n < sobjCount_800c6730.get(); n++) {
            if(submapAssets.objects.get(n).model == submapAssets.objects.get(i).model) {
              submapObjectFlags_800c6a50.offset(n * 0x4L).setu(0x80L);
            }
          }
        }

        //LAB_800e1b20
        //LAB_800e1b54
        for(int i = 0; i < sobjCount_800c6730.get(); i++) {
          final SubmapObject obj = submapAssets.objects.get(i);

          final String name = "Submap object " + i + " (file " + i * 33 + ')';
          final ScriptState<SubmapObject210> state = SCRIPTS.allocateScriptState(name, new SubmapObject210(name));
          sobjs_800c6880[i] = state;
          state.setTicker(SMap::submapObjectTicker);
          state.setRenderer(SMap::submapObjectRenderer);
          state.setDestructor(SMap::scriptDestructor);
          state.loadScriptFile(obj.script);

          final Model124 model = state.innerStruct_00.model_00;
          model.colourMap_9d = (int)submapObjectFlags_800c6a50.offset(1, i * 0x4L).get();

          final CContainer tmd = submapAssets.objects.get(i).model;
          final TmdAnimationFile anim = obj.animations.get(0);
          initModel(model, tmd, anim);

          if(i == 0) { // Player
            FUN_800e0d18(playerModel_800c6748, tmd, anim);
            playerModel_800c6748.coord2_14.coord.transfer.set(0, 0, 0);
            playerModel_800c6748.coord2Param_64.rotate.zero();
          }

          //LAB_800e1c50
          state.innerStruct_00.s_128 = 0;
          state.innerStruct_00.us_12a = 0;
          state.innerStruct_00.us_12c = 0;
          state.innerStruct_00.sobjIndex_12e = i;
          state.innerStruct_00.sobjIndex_130 = i;
          state.innerStruct_00.animIndex_132 = 0;
          state.innerStruct_00.s_134 = 0;
          state.innerStruct_00.i_144 = 0;
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

          if(i == 0) { // Player
            state.innerStruct_00.s_178 = 1;
          } else {
            //LAB_800e1ce0
            state.innerStruct_00.s_178 = 0;
          }

          //LAB_800e1ce4
          final SobjPos14 pos = sobjPositions_800bd818[i];
          model.coord2_14.coord.transfer.set(pos.pos_00);
          model.coord2Param_64.rotate.set(pos.rot_0c);

          state.innerStruct_00.ui_18c = 7;
          state.innerStruct_00.flags_190 = 0;

          if(i == 0) {
            state.innerStruct_00.flags_190 = 0x1;
            FUN_800e4d88(index, model);
          }

          //LAB_800e1d60
          FUN_800f04ac(state.innerStruct_00._1d0);
        }

        //LAB_800e1d88
        chapterTitleState_800c6708.set(0);
        _800c670a.setu(0);
        _800c670c.setu(0);
        _800c670e.setu(0);
        chapterTitleCardMrg_800c6710 = null;
        _800c6714.setu(0);
        _800c6718.setu(0);
        _800c671c.setu(0);
        _800c6720.setu(0);

        chapterTitleNum_800c6738.set(0);
        _800c673c.setu(0x3cL);

        _800c686e.setu(0);
        _800c687c.setu(0);
        _800c687e.setu(0);
        chapterTitleCardLoaded_800c68e0.set(false);
        loadingStage_800c68e4.addu(0x1L);

        _800c69fc = new TriangleIndicator140();

        cameraPos_800c6aa0.set(rview2_800bd7e8.viewpoint_00).sub(rview2_800bd7e8.refpoint_0c);

        loadTimImage(timFile_800d689c.getAddress());
        resetTriangleIndicators();

        //LAB_800e1ecc
        for(int i = 0; i < 32; i++) {
          _800c6970.get(i).set(-1);
        }

        _800c6aac.setu(10);
        _800bd7b8.setu(0);
      }

      case 10 -> {
        if(fullScreenEffect_800bb140.currentColour_28 != 0 || _800c6aac.get() != 0) {
          //LAB_800e1f24
          if(_800c6aac.get() != 0) {
            _800c6aac.subu(0x1L);
          }
        }

        //LAB_800e1f40
        _800bd7b4.setu(0x1L);
        if(chapterTitleNum_800c6738.get() != 0) {
          handleAndRenderChapterTitle();
        }
      }
    }
  }

  /** Handles cutscene movement */
  @Method(0x800e1f90L)
  public static boolean FUN_800e1f90(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    if((sobj.flags_190 & 0x8000_0000) != 0) {
      return false;
    }

    int x = 0;
    if((sobj.vec_148.getX() & 0x7fff_ffff) != 0) {
      x = sobj.vec_148.getX();
    }

    //LAB_800e1fe4
    int y = 0;
    if((sobj.vec_148.getY() & 0x7fff_ffff) != 0) {
      y = sobj.vec_148.getY();
    }

    //LAB_800e1ffc
    int z = 0;
    if((sobj.vec_148.getZ() & 0x7fff_ffff) != 0) {
      z = sobj.vec_148.getZ();
    }

    //LAB_800e2014
    sobj.vec_160.add(sobj.vec_154);

    if((sobj.vec_160.getX() & 0x1_0000) != 0) {
      sobj.vec_160.x.and(0xffff);

      if(sobj.vec_148.getX() >= 0) {
        x++;
      } else {
        x--;
      }
    }

    //LAB_800e2078
    if((sobj.vec_160.getY() & 0x1_0000) != 0) {
      sobj.vec_160.y.and(0xffff);

      if(sobj.vec_148.getY() >= 0) {
        y++;
      } else {
        y--;
      }
    }

    //LAB_800e20a8
    if((sobj.vec_160.getZ() & 0x1_0000) != 0) {
      sobj.vec_160.z.and(0xffff);

      if(sobj.vec_148.getZ() >= 0) {
        z++;
      } else {
        z--;
      }
    }

    //LAB_800e20d8
    sobj.i_144--;

    if(sobj.s_172 == 0) {
      final SVECTOR sp0x20 = new SVECTOR();

      if((sobj.flags_190 & 0x1) != 0) { // Is player
        final SVECTOR sp0x18 = new SVECTOR();
        sp0x18.set((short)x, (short)y, (short)z);
        SetRotMatrix(worldToScreenMatrix_800c3548);
        SetTransMatrix(worldToScreenMatrix_800c3548);
        transformToWorldspace(sp0x20, sp0x18);
      } else {
        //LAB_800e2134
        sp0x20.set((short)x, (short)y, (short)z);
      }

      //LAB_800e2140
      final int s3 = FUN_800e88a0(sobj.sobjIndex_12e, model.coord2_14.coord, sp0x20);
      if(s3 >= 0) {
        if(FUN_800e6798(s3, 0, model.coord2_14.coord.transfer.getX(), model.coord2_14.coord.transfer.getY(), model.coord2_14.coord.transfer.getZ(), sp0x20) != 0) {
          model.coord2_14.coord.transfer.x.add(sp0x20.getX());
          model.coord2_14.coord.transfer.setY(sp0x20.getY());
          model.coord2_14.coord.transfer.z.add(sp0x20.getZ());
        }
      }

      //LAB_800e21bc
      sobj.ui_16c = s3;
    } else {
      //LAB_800e21c4
      model.coord2_14.coord.transfer.add(x, y, z);
    }

    //LAB_800e21e8
    if(sobj.i_144 != 0) {
      //LAB_800e21f8
      return false;
    }

    //LAB_800e2200
    sobj.us_170 = 0;

    //LAB_800e2204
    return true;
  }

  @Method(0x800e2220L)
  public static void unloadSmap() {
    submapControllerState_800c6740.deallocateWithChildren();

    if(chapterTitleCardMrg_800c6710 != null) {
      chapterTitleCardMrg_800c6710 = null;
    }

    //LAB_800e226c
    _800bd7b4.setu(0);

    //LAB_800e229c
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      final SobjPos14 pos = sobjPositions_800bd818[i];

      final ScriptState<SubmapObject210> sobjState = sobjs_800c6880[i];
      if(sobjState != null) {
        final SubmapObject210 sobj = sobjState.innerStruct_00;
        final Model124 model = sobj.model_00;
        pos.pos_00.set(model.coord2_14.coord.transfer);
        pos.rot_0c.set(model.coord2Param_64.rotate);
        sobjs_800c6880[i].deallocateWithChildren();
      } else {
        //LAB_800e231c
        pos.pos_00.set(0, 0, 0);
        pos.rot_0c.zero();
      }
    }

    //LAB_800e2350
    _800bd7b0.set(1);

    submapAssets = null;
    submapAssetsMrg_800c6878 = null;
    submapScriptsMrg_800c68d8 = null;

    FUN_80029e04(null);

    _800c6870.setu(-0x1L);
    callbackArr_800f5ad4[(int)callbackIndex_800c6968.get()].run();

    _800f9eac.set(-1);
    loadSmapMedia();
    _800c69fc = null;
    loadTimImage(_80010544.getAddress());
  }

  @Method(0x800e2428L)
  public static void FUN_800e2428(final int sobjIndex) {
    final MATRIX ls = new MATRIX();
    final MATRIX lw = new MATRIX();

    final SubmapStruct80 a0 = _800c68e8;

    GsGetLws(((SubmapObject210)scriptStatePtrArr_800bc1c0[sobjIndex].innerStruct_00).model_00.coord2_14, lw, ls);
    GTE.setRotationMatrix(ls);
    GTE.setTranslationVector(ls.transfer);

    GTE.perspectiveTransform(0, 0, 0);
    a0.x2_70 = GTE.getScreenX(2) + 192;
    a0.y2_74 = GTE.getScreenY(2) + 128;

    GTE.perspectiveTransform(0, -130, 0);
    a0.x3_78 = GTE.getScreenX(2) + 192;
    a0.y3_7c = GTE.getScreenY(2) + 128;

    GTE.perspectiveTransform(-20, 0, 0);
    a0.x1_68 = GTE.getScreenX(2) + 192;
    a0.y1_6c = GTE.getScreenY(2) + 128;

    GTE.perspectiveTransform(20, 0, 0);
    a0.x0_60 = GTE.getScreenX(2) + 192;
    a0.y0_64 = GTE.getScreenY(2) + 128;
  }

  @Method(0x800e2648L)
  public static void handleAndRenderChapterTitle() {
    final int chapterTitleState = chapterTitleState_800c6708.get();

    if(chapterTitleState == 0) {
      //LAB_800e26c0
      final int chapterTitleNum = chapterTitleNum_800c6738.get();

      if(chapterTitleNum == 1) {
        //LAB_800e2700
        //LAB_800e2794
        loadDrgnDir(0, 6670, files -> SMap.submapAssetsLoadedCallback(files, 0x10));
      } else if(chapterTitleNum == 2) {
        //LAB_800e2728
        //LAB_800e2794
        loadDrgnDir(0, 6671, files -> SMap.submapAssetsLoadedCallback(files, 0x10));
        //LAB_800e26e8
      } else if(chapterTitleNum == 3) {
        //LAB_800e2750
        //LAB_800e2794
        loadDrgnDir(0, 6672, files -> SMap.submapAssetsLoadedCallback(files, 0x10));
      } else if(chapterTitleNum == 4) {
        //LAB_800e2778
        //LAB_800e2794
        loadDrgnDir(0, 6673, files -> SMap.submapAssetsLoadedCallback(files, 0x10));
      }

      //LAB_800e27a4
      chapterTitleState_800c6708.incr();
      return;
    }

    if(chapterTitleState == 1) {
      //LAB_800e27b8
      if(chapterTitleCardLoaded_800c68e0.get() && (chapterTitleNum_800c6738.get() & 0x80) != 0) {
        chapterTitleState_800c6708.incr();
      }

      return;
    }

    if(chapterTitleState == 2) {
      //LAB_800e27e8
      final long v1 = _800c670a.getSigned();

      //LAB_800e284c
      if(v1 == 0) {
        //LAB_800e2860
        new Tim(chapterTitleCardMrg_800c6710.get(5)).uploadToGpu();
        new Tim(chapterTitleCardMrg_800c6710.get(13)).uploadToGpu();

        //LAB_800e2980
        _800c6728.setu(0);
        _800c6724.setu(0);
        _800c6714.setu(0x20L);
        _800c6718.setu(0x10L);
        _800c671c.setu(0x40L);
        _800c6720.setu(0x10L);
        _800c670a.addu(0x1L);
      } else if(v1 == 0x22L) {
        //LAB_800e30c0
        _800c670c.addu(0x1L);

        if(_800c670c.getSigned() == 0x3L) {
          _800c670e.setu(0x1L);
          _800c670a.addu(0x1L);
        }
      } else if(v1 == 0x23L) {
        //LAB_800e30f8
        _800c673c.subu(0x1L);

        if(_800c673c.get() == 0) {
          _800c670a.setu(0xc9L);
        }
      } else if(v1 == 0xe9L) {
        //LAB_800e376c
        chapterTitleCardMrg_800c6710 = null;
        chapterTitleState_800c6708.incr();
      } else if((int)v1 >= 0x23L) {
        //LAB_800e2828
        if((int)v1 < 0xe9L) {
          if((int)v1 >= 0xc9L) {
            //LAB_800e311c
            if(v1 == 0xd4L) {
              new Tim(chapterTitleCardMrg_800c6710.get(1)).uploadToGpu();
              new Tim(chapterTitleCardMrg_800c6710.get(9)).uploadToGpu();

              //LAB_800e3248
              //LAB_800e3254
            } else if(v1 == 0xd8L) {
              new Tim(chapterTitleCardMrg_800c6710.get(2)).uploadToGpu();
              new Tim(chapterTitleCardMrg_800c6710.get(10)).uploadToGpu();

              //LAB_800e3384
              //LAB_800e3390
            } else if(v1 == 0xdcL) {
              new Tim(chapterTitleCardMrg_800c6710.get(3)).uploadToGpu();
              new Tim(chapterTitleCardMrg_800c6710.get(11)).uploadToGpu();

              //LAB_800e34c0
              //LAB_800e34cc
            } else if(v1 == 0xe0L) {
              new Tim(chapterTitleCardMrg_800c6710.get(4)).uploadToGpu();
              new Tim(chapterTitleCardMrg_800c6710.get(12)).uploadToGpu();

              //LAB_800e35fc
              //LAB_800e3608
            } else if(v1 == 0xe4L) {
              new Tim(chapterTitleCardMrg_800c6710.get(5)).uploadToGpu();
              new Tim(chapterTitleCardMrg_800c6710.get(13)).uploadToGpu();
            }

            //LAB_800e3744
            _800c6724.setu(0);
            _800c6728.subu(0x4L);
          }

          //LAB_800e3790
          _800c670a.addu(0x1L);
        }
      } else if((int)v1 >= 0x21L) {
        //LAB_800e3070
        _800c6724.setu(0x1L);
        _800c6720.setu(0);
        _800c671c.setu(0);
        _800c6718.setu(0);
        _800c6714.setu(0);
        _800c6728.setu(0x80L);
        _800c670a.addu(0x1L);
        _800c670c.setu(0x1L);
        _800c670e.setu(0);
      } else if((int)v1 > 0) {
        //LAB_800e29d4
        if(v1 == 0x4L) {
          new Tim(chapterTitleCardMrg_800c6710.get(4)).uploadToGpu();
          new Tim(chapterTitleCardMrg_800c6710.get(12)).uploadToGpu();

          //LAB_800e2afc
          //LAB_800e2b08
        } else if(v1 == 0x8L) {
          new Tim(chapterTitleCardMrg_800c6710.get(3)).uploadToGpu();
          new Tim(chapterTitleCardMrg_800c6710.get(11)).uploadToGpu();

          //LAB_800e2c38
          //LAB_800e2c44
        } else if(v1 == 0xcL) {
          new Tim(chapterTitleCardMrg_800c6710.get(2)).uploadToGpu();
          new Tim(chapterTitleCardMrg_800c6710.get(10)).uploadToGpu();

          //LAB_800e2d74
          //LAB_800e2d80
        } else if(v1 == 0x10L) {
          new Tim(chapterTitleCardMrg_800c6710.get(1)).uploadToGpu();
          new Tim(chapterTitleCardMrg_800c6710.get(9)).uploadToGpu();

          //LAB_800e2eb0
          //LAB_800e2ebc
        } else if(v1 == 0x14L) {
          new Tim(chapterTitleCardMrg_800c6710.get(0)).uploadToGpu();
          new Tim(chapterTitleCardMrg_800c6710.get(8)).uploadToGpu();

          //LAB_800e2fec
        }

        //LAB_800e2ff8
        _800c6728.addu(0x4L);
        _800c6714.subu(0x1L);
        _800c671c.subu(0x2L);

        if((_800c670a.get() & 0x1L) == 0) {
          _800c6718.subu(0x1L);
          _800c6720.subu(0x1L);
        }

        //LAB_800e3038
        //LAB_800e3064
        _800c670a.addu(0x1L);
      } else {
        //LAB_800e3790
        _800c670a.addu(0x1L);
      }

      //LAB_800e37a0
      int left = (int)(_800c687c.getSigned() + _800c6714.getSigned() - 58);
      int top = (int)(_800c687e.getSigned() + _800c6718.getSigned() - 66);
      int right = (int)(_800c687c.getSigned() - (_800c6714.getSigned() - 34));
      int bottom = (int)(_800c687e.getSigned() - (_800c6718.getSigned() + 30));

      final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .monochrome((int)_800c6728.get())
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

      if(_800c6724.get() == 0) {
        cmd1.translucent(Translucency.B_PLUS_F);
      }

      GPU.queueCommand(28, cmd1);

      left = (int)(_800c687c.get() - (_800c671c.get() + 140));
      top = (int)(_800c687e.get() - (_800c6720.get() + 16));
      right = (int)(_800c687c.get() + _800c671c.get() + 116);
      bottom = (int)(_800c687e.get() + _800c6720.get() + 45);

      final GpuCommandPoly cmd2 = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .monochrome((int)_800c6728.get())
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

      if(_800c6724.get() == 0) {
        cmd2.translucent(Translucency.B_PLUS_F);
      }

      GPU.queueCommand(28, cmd2);

      if(_800c670c.getSigned() != 0) {
        left = (int)(_800c670c.get() + _800c687c.get() + _800c6714.get() - 58);
        top = (int)(_800c670e.get() + _800c687e.get() + _800c6718.get() - 66);
        right = (int)(_800c670c.get() + _800c687c.get() - (_800c6714.get() - 34));
        bottom = (int)(_800c670e.get() + _800c687e.get() - (_800c6718.get() + 30));

        final GpuCommandPoly cmd3 = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .monochrome((int)_800c6728.get())
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

        if((chapterTitleNum_800c6738.get() & 0xf) - 2 < 3) {
          cmd3.translucent(Translucency.B_MINUS_F);
        }

        //LAB_800e3afc
        if((chapterTitleNum_800c6738.get() & 0xf) == 1) {
          cmd3.translucent(Translucency.B_PLUS_F);
        }

        //LAB_800e3b14
        GPU.queueCommand(28, cmd3);

        left = (int)(_800c670c.get() + _800c687c.get() - (_800c671c.get() + 140));
        top = (int)(_800c670e.get() + _800c687e.get() - (_800c6720.get() + 16));
        right = (int)(_800c670c.get() + _800c687c.get() + _800c671c.get() + 116);
        bottom = (int)(_800c670e.get() + _800c687e.get() + _800c6720.get() + 45);

        final GpuCommandPoly cmd4 = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.HALF_B_PLUS_HALF_F)
          .monochrome((int)_800c6728.get())
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

        if((chapterTitleNum_800c6738.get() & 0xf) - 2 < 3) {
          cmd4.translucent(Translucency.B_MINUS_F);
        }

        //LAB_800e3c20
        if((chapterTitleNum_800c6738.get() & 0xf) == 1) {
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
      chapterTitleNum_800c6738.set(0);
      _800c687e.setu(0);
      _800c687c.setu(0);
      chapterTitleCardLoaded_800c68e0.set(false);
      _800c670c.setu(0);
      _800c670a.setu(0);
      chapterTitleState_800c6708.set(0);
      _800c686e.setu(0x1L);
    }
  }

  @Method(0x800e3cc8L)
  public static void loadTimImage(final long address) {
    final TimHeader header = parseTimHeader(MEMORY.ref(4, address).offset(0x4L));
    LoadImage(header.imageRect, header.imageAddress);

    if(header.hasClut()) {
      LoadImage(header.clutRect, header.clutAddress);
    }
  }

  @Method(0x800e3d80L)
  public static void submapAssetsLoadedCallback(final List<FileData> files, final int assetType) {
    switch(assetType) {
      // Submap assets
      case 0x0 -> {
        submapAssetsLoaded_800c6874.set(true);
        submapAssetsMrg_800c6878 = files;
      }

      // Submap scripts
      case 0x1 -> {
        submapScriptsLoaded_800c68d0.set(true);
        submapScriptsMrg_800c68d8 = files;
      }

      // Chapter title cards
      case 0x10 -> {
        chapterTitleCardLoaded_800c68e0.set(true);
        chapterTitleCardMrg_800c6710 = files;
      }
    }
  }

  @Method(0x800e3df4L)
  public static void scriptDestructor(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    //LAB_800e3e24
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      if(sobjs_800c6880[i] == state) {
        sobjs_800c6880[i] = null;
      }

      //LAB_800e3e38
    }

    //LAB_800e3e48
  }

  @Method(0x800e3e60L)
  public static boolean FUN_800e3e60(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    sobj.us_170 = 0;
    return true;
  }

  @Method(0x800e3e74L)
  public static boolean FUN_800e3e74(final ScriptState<SubmapObject210> state, final SubmapObject210 sobj) {
    final Model124 model = sobj.model_00;

    model.coord2_14.coord.transfer.y.add(sobj.s_134);

    int x = 0;
    if((sobj.vec_148.getX() & 0x7fff_ffff) != 0) {
      x = sobj.vec_148.getX();
    }

    //LAB_800e3ea8
    int z = 0;
    if((sobj.vec_148.getZ() & 0x7fff_ffff) != 0) {
      z = sobj.vec_148.getZ();
    }

    //LAB_800e3ec0
    sobj.vec_160.x.add(sobj.vec_154.getX());
    sobj.vec_160.z.add(sobj.vec_154.getZ());

    if((sobj.vec_160.getX() & 0x1_0000) != 0) {
      sobj.vec_160.x.and(0xffff);

      if(sobj.vec_148.getX() >= 0) {
        x++;
      } else {
        //LAB_800e3f08
        x--;
      }
    }

    //LAB_800e3f0c
    if((sobj.vec_160.getZ() & 0x1_0000) != 0) {
      sobj.vec_160.z.and(0xffff);

      if(sobj.vec_148.getZ() >= 0) {
        z++;
      } else {
        //LAB_800e3f38
        z--;
      }
    }

    //LAB_800e3f3c
    model.coord2_14.coord.transfer.x.add(x);
    model.coord2_14.coord.transfer.z.add(z);
    sobj.s_134 += sobj.ui_18c;
    sobj.i_144--;
    if(sobj.i_144 != 0) {
      return false;
    }

    //LAB_800e3f7c
    sobj.us_170 = 0;
    sobj.s_134 = 0;
    model.coord2_14.coord.transfer.set(sobj.vec_138);
    sobj.s_172 = sobj.s_174;
    return true;
  }

  @Method(0x800e3facL)
  public static void FUN_800e3fac() {
    if(_800bd7b8.get() == 0) {
      submapAssets.pxls.get(0).uploadToGpu();
      submapAssets.pxls.get(1).uploadToGpu();
    }

    //LAB_800e4008
  }

  @Method(0x800e4018L)
  public static void FUN_800e4018() {
    if(gameState_800babc8.indicatorsDisabled_4e3) {
      if(_800f64ac.get() == 0) {
        _800f64ac.setu(0x1L);
      }
    } else if(_800f64ac.get() == 0x1L) {
      _800f64ac.setu(0);
      _800c6970.get(31).set(0);
    }
  }

  /** sobj/sobj collision */
  @Method(0x800e4378L)
  public static void FUN_800e4378(final SubmapObject210 sobj, final long a1) {
    final Model124 model = sobj.model_00;

    sobj.collidedWithSobjIndex_19c = -1;

    final int colliderMinY = model.coord2_14.coord.transfer.getY() - sobj.collisionSizeVertical_1a4;
    final int colliderMaxY = model.coord2_14.coord.transfer.getY() + sobj.collisionSizeVertical_1a4;

    //LAB_800e43b8
    //LAB_800e43ec
    //LAB_800e43f0
    //LAB_800e4414
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      final SubmapObject210 sobj2 = sobjs_800c6880[i].innerStruct_00;
      final Model124 model2 = sobj2.model_00;

      if(sobj2 != sobj && (sobj2.flags_190 & a1) != 0) {
        final int dx = model2.coord2_14.coord.transfer.getX() - model.coord2_14.coord.transfer.getX();
        final int dz = model2.coord2_14.coord.transfer.getZ() - model.coord2_14.coord.transfer.getZ();
        final int size = sobj.collisionSizeHorizontal_1a0 + sobj2.collisionSizeHorizontal_1a0;
        final int collideeMinY = model2.coord2_14.coord.transfer.getY() - sobj2.collisionSizeVertical_1a4;
        final int collideeMaxY = model2.coord2_14.coord.transfer.getY() + sobj2.collisionSizeVertical_1a4;

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
  public static void FUN_800e450c(final SubmapObject210 sobj, final long a1) {
    final Model124 model = sobj.model_00;

    sobj.collidedWithSobjIndex_1a8 = -1;

    final int reachX = (int)(MathHelper.sin(model.coord2Param_64.rotate.y) * -sobj.collisionReach_1b4);
    final int reachZ = (int)(MathHelper.cos(model.coord2Param_64.rotate.y) * -sobj.collisionReach_1b4);
    final int colliderMinY = model.coord2_14.coord.transfer.getY() - sobj.collisionSizeVertical_1b0;
    final int colliderMaxY = model.coord2_14.coord.transfer.getY() + sobj.collisionSizeVertical_1b0;

    //LAB_800e45d8
    //LAB_800e45dc
    //LAB_800e4600
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      final SubmapObject210 sobj2 = sobjs_800c6880[i].innerStruct_00;
      final Model124 model2 = sobj2.model_00;

      if(sobj2 != sobj && (sobj2.flags_190 & a1) != 0) {
        final int dx = model2.coord2_14.coord.transfer.getX() - (model.coord2_14.coord.transfer.getX() + reachX);
        final int dz = model2.coord2_14.coord.transfer.getZ() - (model.coord2_14.coord.transfer.getZ() + reachZ);
        final int size = sobj.collisionSizeHorizontal_1ac + sobj2.collisionSizeHorizontal_1ac;

        final int collideeMinY = model2.coord2_14.coord.transfer.getY() - sobj2.collisionSizeVertical_1b0;
        final int collideeMaxY = model2.coord2_14.coord.transfer.getY() + sobj2.collisionSizeVertical_1b0;

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
  public static void FUN_800e4708() {
    FUN_800f047c();
    renderSubmapOverlays();
    FUN_800f4354();
    applyModelRotationAndScale(playerModel_800c6748);
    callbackArr_800f5ad4[(int)callbackIndex_800c6968.get()].run();
  }

  /**
   * @param parent The model that the indicator is attached to
   * @param y      The Y offset from the model
   */
  @Method(0x800e4774L)
  public static void renderAlertIndicator(final Model124 parent, final int y) {
    final MATRIX ls = new MATRIX();
    final MATRIX lw = new MATRIX();
    GsGetLws(parent.coord2_14, lw, ls);
    GTE.setRotationMatrix(ls);
    GTE.setTranslationVector(ls.transfer);
    GTE.perspectiveTransform(0, y - 64, 0);
    final short sx = GTE.getScreenX(2);
    final short sy = GTE.getScreenY(2);

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .bpp(Bpp.BITS_4)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(0x80)
      .clut(976, 464)
      .vramPos(960, 256)
      .pos(0, (int)_800f64b0.offset(0x0L).getSigned() + sx, (int)_800f64b0.offset(0x4L).getSigned() + sy)
      .pos(1, (int)_800f64b0.offset(0x2L).getSigned() + sx, (int)_800f64b0.offset(0x4L).getSigned() + sy)
      .pos(2, (int)_800f64b0.offset(0x0L).getSigned() + sx, (int)_800f64b0.offset(0x6L).getSigned() + sy)
      .pos(3, (int)_800f64b0.offset(0x2L).getSigned() + sx, (int)_800f64b0.offset(0x6L).getSigned() + sy)
      .uv(0, (int)_800f64b0.offset(1, 0x8L).get(), (int)_800f64b0.offset(1, 0xcL).get())
      .uv(1, (int)_800f64b0.offset(1, 0xaL).get(), (int)_800f64b0.offset(1, 0xcL).get())
      .uv(2, (int)_800f64b0.offset(1, 0x8L).get(), (int)_800f64b0.offset(1, 0xeL).get())
      .uv(3, (int)_800f64b0.offset(1, 0xaL).get(), (int)_800f64b0.offset(1, 0xeL).get());

    GPU.queueCommand(37, cmd);
  }

  @Method(0x800e4994L)
  public static void FUN_800e4994() {
    _800c686c.setu(0x1L);
  }

  @Method(0x800e49a4L)
  public static int randomEncounterIndex() {
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
  public static boolean hasPlayerMoved(final MATRIX mat) {
    //LAB_800e4a44
    final boolean moved = prevPlayerPos_800c6ab0.getX() != mat.transfer.getX() || prevPlayerPos_800c6ab0.getY() != mat.transfer.getY() || prevPlayerPos_800c6ab0.getZ() != mat.transfer.getZ();

    //LAB_800e4a4c
    final EncounterRateMode mode = CONFIG.getConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get());

    final int dist = mode.modifyDistance((prevPlayerPos_800c6ab0.getX() - mat.transfer.getX() ^ 2) + (prevPlayerPos_800c6ab0.getZ() - mat.transfer.getZ() ^ 2));

    if(dist < 9) {
      //LAB_800e4a98
      encounterMultiplier_800c6abc = mode.walkModifier;
    } else {
      encounterMultiplier_800c6abc = mode.runModifier;
    }

    //LAB_800e4aa0
    prevPlayerPos_800c6ab0.set(mat.transfer);
    return moved;
  }

  @Method(0x800e4ac8L)
  public static void cacheHasNoEncounters() {
    hasNoEncounters_800bed58.setu(encounterData_800f64c4.get(submapCut_80052c30.get()).rate_02.get() == 0 ? 1 : 0);
  }

  @Method(0x800e4b20L)
  public static long handleEncounters() {
    if(_800c6ae0.get() < 0xfL) {
      return 0;
    }

    if(gameState_800babc8.indicatorsDisabled_4e3) {
      return 0;
    }

    _800c6ae4.addu(0x1L);

    if(_800c6ae4.getSigned() < 0) {
      return 0;
    }

    // The first condition is to fix what we believe is caused by menus loading too fast in SC. Submaps still take several frames to initialize,
    // and if you spam triangle and escape immediately after the post-combat screen it's possible to get into this method when index_80052c38 is
    // still set to -1. See #304 for more details.
    if(index_80052c38.get() >= 0 && index_80052c38.get() < 0x40L && collisionAndTransitions_800cb460[index_80052c38.get()] != 0) {
      return 0;
    }

    //LAB_800e4bc0
    if(!isScriptLoaded(0)) {
      return 0;
    }

    if(!hasPlayerMoved(sobjs_800c6880[0].innerStruct_00.model_00.coord2_14.coord)) {
      return 0;
    }

    encounterAccumulator_800c6ae8.add(Math.round(encounterData_800f64c4.get(submapCut_80052c30.get()).rate_02.get() * encounterMultiplier_800c6abc));

    if(encounterAccumulator_800c6ae8.get() > 0x1400) {
      // Start combat
      encounterId_800bb0f8.set(sceneEncounterIds_800f74c4.get(encounterData_800f64c4.get(submapCut_80052c30.get()).scene_00.get()).get(randomEncounterIndex()).get());
      combatStage_800bb0f4.set(encounterData_800f64c4.get(submapCut_80052c30.get()).stage_03.get());
      if(Config.combatStage()) {
        combatStage_800bb0f4.set(Config.getCombatStage());
      }
      return 0x1L;
    }

    //LAB_800e4ce4
    //LAB_800e4ce8
    return 0;
  }

  @Method(0x800e4d00L)
  public static void FUN_800e4d00(final int submapCut, final int index) {
    if(FUN_800e5264(matrix_800c6ac0, submapCut) == 0) {
      //LAB_800e4d34
      final SVECTOR avg = new SVECTOR();
      get3dAverageOfSomething(index, avg);
      matrix_800c6ac0.transfer.set(avg);
      _800f7e24.setu(0x2L);
    } else {
      _800f7e24.setu(0x1L);
    }

    //LAB_800e4d74
  }

  @Method(0x800e4d88L)
  public static void FUN_800e4d88(final int index, final Model124 model) {
    if(_800f7e24.get() != 0) {
      if(_800f7e24.get() == 0x1L) {
        model.coord2_14.coord.set(matrix_800c6ac0);
      } else {
        //LAB_800e4e04
        model.coord2_14.coord.transfer.set(matrix_800c6ac0.transfer);
      }

      //LAB_800e4e18
      _800f7e24.setu(0);
    } else {
      //LAB_800e4e20
      final SVECTOR sp10 = new SVECTOR();
      get3dAverageOfSomething(index, sp10);
      model.coord2_14.coord.transfer.set(sp10);
    }

    //LAB_800e4e4c
  }

  @Method(0x800e4e5cL)
  public static void FUN_800e4e5c() {
    //LAB_800e4ecc
    GPU.uploadData(new Rect4i(640, 0, 368, 240), GPU.getDisplayBuffer().getData());
    _80052c48.setu(0x1L);
  }

  @Method(0x800e4f8cL)
  public static void FUN_800e4f8c() {
    //LAB_800e4fd4
    _800f7e28 = new Ptr<>(() -> _800c6aec, val -> _800c6aec = val);
  }

  @Method(0x800e4ff4L)
  public static void FUN_800e4ff4() {
    UnknownStruct s0 = _800c6aec;
    Ptr<UnknownStruct> s1 = new Ptr<>(() -> _800c6aec, val -> _800c6aec = val);

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
    _800f7e28 = s1;
  }

  @Method(0x800e5084L)
  public static long FUN_800e5084(final UnknownStruct2 a1) {
    final UnknownStruct v0 = new UnknownStruct();
    v0.inner_08 = a1;

    _800f7e28.set(v0);
    _800f7e28 = new Ptr<>(() -> v0.parent_00, val -> v0.parent_00 = val);

    //LAB_800e50ec
    return 0x1L;
  }

  @Method(0x800e5104L)
  public static void FUN_800e5104(final int index, final MediumStruct a1) {
    executeSceneGraphicsLoadingStage(index);

    a1.callback_48.accept(a1);

    _800c6ae0.addu(0x1L);

    if(gameState_800babc8.indicatorsDisabled_4e3) {
      _800c6ae4.setu(-30);
    }

    //LAB_800e5184
    FUN_800e4ff4();
  }

  @Method(0x800e519cL)
  public static void FUN_800e519c() {
    //LAB_800e51e8
    final MATRIX[] matrices = new MATRIX[sobjCount_800c6730.get()];
    for(int i = 0; i < sobjCount_800c6730.get(); i++) {
      if(!isScriptLoaded(i)) {
        return;
      }

      matrices[i] = sobjs_800c6880[i].innerStruct_00.model_00.coord2_14.coord;
    }

    //LAB_800e5234
    FUN_800e7954(matrices, sobjCount_800c6730.get());

    //LAB_800e5248
  }

  @Method(0x800e5264L)
  public static long FUN_800e5264(final MATRIX mat, final int submapCut) {
    if(submapCut_80052c3c.get() != submapCut) {
      _800cb448.setu(0);
      return 0;
    }

    //LAB_800e5294
    if(_80052c40.get() == 0) {
      return 0;
    }

    //LAB_800e52b0
    setScreenOffsetIfNotSet(screenOffsetX_800bed50.get(), screenOffsetY_800bed54.get());

    mat.set(matrix_800bed30);

    _80052c40.setu(0);
    _800cb448.setu(0x1L);

    //LAB_800e5320
    return 0x1L;
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
  public static void loadBackground(final String mapName, final List<FileData> files) {
    backgroundLoaded_800cab10.setu(0x1L);

    //LAB_800e5374
    for(int i = 3; i < files.size(); i++) {
      new Tim(files.get(i)).uploadToGpu();
    }

    //LAB_800e5430
    loadEnvironment(new EnvironmentFile(files.get(0)));
    FUN_800e8cd0(new TmdWithId("Background " + mapName, files.get(2)), files.get(1));

    _80052c44.setu(0x2L);
    _80052c48.setu(0);
    _800cab20.setu(0x2L);
  }

  @Method(0x800e5518L)
  public static boolean isScriptLoaded(final int index) {
    return sobjs_800c6880[index] != null;
  }

  /** Part of map transitioning */
  @Method(0x800e5534L)
  public static long FUN_800e5534(final int newCut, final int newScene) {
    if(smapLoadingStage_800cb430.get() != 0xcL) {
      return 0;
    }

    if((int)_800c6ae0.get() < 0x3L) {
      return 0;
    }

    if(_800f7e4c.get() == 0x1L || (loadedDrgnFiles_800bcf78.get() & 0x82L) != 0) {
      return 0;
    }

    if((int)_800c6ae0.get() > 0xfL) {
      _800cb448.setu(0);
    }

    _800c6aac.setu(0xaL);
    _800bd7b4.setu(0);
    if(_800cab28.get() == 0) {
      if(fullScreenEffect_800bb140._24 == 0) {
        scriptStartEffect(1, 10);
        _800cab28.addu(0x1L);
      }
    } else {
      _800cab28.addu(0x1L);
    }

    final int cut = submapCut_80052c30.get();
    _800f7e4c.setu(0x1L);

    if(newCut > 0x7ff) {
      fmvIndex_800bf0dc = newCut - 0x800;
      afterFmvLoadingStage_800bf0ec = EngineState.values()[newScene];
      smapLoadingStage_800cb430.setu(0x15L);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newCut >= 0 && newCut < 2) {
      smapLoadingStage_800cb430.setu(0x12L);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newCut > -1) {
      submapCut_80052c30.set(newCut);
      submapScene_80052c34.set(newScene);
      smapLoadingStage_800cb430.setu(0x4L);
      _800cb450.setu(newCut);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fc) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_TOO_MANY_ITEMS_MENU_31;
      smapLoadingStage_800cb430.setu(0xdL);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fa) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_CHAR_SWAP_MENU_21;
      smapLoadingStage_800cb430.setu(0xdL);
      _800cb450.setu(cut);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fb) {
      SCRIPTS.pause();
      smapLoadingStage_800cb430.setu(0x14L);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fe) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_SHOP_MENU_6;
      smapLoadingStage_800cb430.setu(0xdL);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fd) {
      whichMenu_800bdc38 = WhichMenu.INIT_SAVE_GAME_MENU_16;
      smapLoadingStage_800cb430.setu(0xdL);
      _800f7e30.setu(index_80052c38.get());
      index_80052c38.set((int)_800f7e30.offset(gameState_800babc8.chapterIndex_98 * 0x8L).get());
      _800cb450.setu(_800f7e2c.offset(gameState_800babc8.chapterIndex_98 * 0x8L).get());
      _800cab24 = FUN_800ea974(-0x1L);
      SCRIPTS.pause();
      return 1;
    }

    if(newScene == 0x3ff) {
      SCRIPTS.pause();
      whichMenu_800bdc38 = WhichMenu.INIT_INVENTORY_MENU_1;
      smapLoadingStage_800cb430.setu(0xdL);
      _800cb450.setu(cut);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    final int scene;
    if(newScene == 0) {
      scene = encounterId_800bb0f8.get();
    } else {
      if(newScene > 0x1ff) {
        SCRIPTS.pause();
        _800f7e4c.setu(0x1L);
        return 1;
      }

      scene = newScene;
    }

    encounterId_800bb0f8.set(scene);

    if(isScriptLoaded(0)) {
      final SubmapObject210 sobj = sobjs_800c6880[0].innerStruct_00;
      final Model124 model = sobj.model_00;

      screenOffsetX_800bed50.set(screenOffsetX_800cb568.get());
      screenOffsetY_800bed54.set(screenOffsetY_800cb56c.get());
      submapCut_80052c3c.set(cut);
      matrix_800bed30.set(model.coord2_14.coord);
      matrix_800bed30.transfer.set(model.coord2_14.coord.transfer);
      _80052c40.setu(0x1L);
    }

    SCRIPTS.pause();
    smapLoadingStage_800cb430.setu(0x13L);
    return 1;
  }

  @Method(0x800e5914L)
  public static void executeSmapLoadingStage() {
    executeSmapLoadingStage_2();
  }

  @Method(0x800e5934L)
  public static void FUN_800e5934() {
    _800cab2c.addu(0x1L);

    if(_800cab2c.get() == 0x1L) {
      FUN_8002aa04();
      smapLoadingStage_800cb430.setu(0);
      smapLoadingStage_800cb430.offset(0x10L).setu(_800cab2c.get());
    }

    //LAB_800e5984
    executeSmapLoadingStage_2();
  }

  @Method(0x800e59a4L)
  public static void executeSmapLoadingStage_2() {
    final boolean a0;

    if(_800cb440.get() == 0) {
      _800cab20.subu(0x1L);

      if(_800cab20.getSigned() >= 0) {
        resizeDisplay(384, 240);
        _800caaf4.set(submapCut_80052c30.get());
        _800caaf8.set(submapScene_80052c34.get());
        return;
      }
    }

    //LAB_800e5a30
    //LAB_800e5a34
    if(pregameLoadingStage_800bb10c.get() == 0) {
      pregameLoadingStage_800bb10c.set(1);
      _800caaf4.set(submapCut_80052c30.get());
      _800caaf8.set(submapScene_80052c34.get());
      _80052c44.setu(0x2L);

      if(_800cb440.get() != 0) {
        if(smapLoadingStage_800cb430.get() == 0x8L) {
          smapLoadingStage_800cb430.setu(0x9L);
        }

        //LAB_800e5aac
        _800cb440.setu(0);
        return;
      }

      //LAB_800e5abc
      smapLoadingStage_800cb430.setu(0);
    }

    //LAB_800e5ac4
    switch((int)smapLoadingStage_800cb430.get()) {
      case 0x0 -> {
        srand((int)System.nanoTime());
        if(_800cb440.get() == 0) {
          resizeDisplay(384, 240);
        }

        //LAB_800e5b2c
        _80052c44.setu(0x2L);
        encounterAccumulator_800c6ae8.set(0);
        smapLoadingStage_800cb430.setu(0x1L);
      }

      case 0x1 -> { // Load newroot
        loadFile("\\SUBMAP\\NEWROOT.RDT", data -> newrootPtr_800cab04 = new NewRootStruct(data));
        smapLoadingStage_800cb430.setu(0x2L);
      }

      case 0x2 -> { // Wait for newroot to load
        if(newrootPtr_800cab04 != null) {
          smapLoadingStage_800cb430.setu(0x3L);
        }
      }

      case 0x3 -> {
        _800cab28.setu(0);
        _80052c44.setu(0x1L);
        _800caaf4.set(submapCut_80052c30.get());
        _800caaf8.set(submapScene_80052c34.get());

        // Detect if we need to change disks
        final IntRef drgnIndex = new IntRef();
        final IntRef fileIndex = new IntRef();

        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);
        if(drgnIndex.get() != drgnBinIndex_800bc058) {
          //LAB_800e5c9c
          drgnBinIndex_800bc058 = drgnIndex.get();

          // Not sure if we still need to reinit the sound or not, but this is what retail did
          reinitSound();
          loadMenuSounds();
          sssqFadeIn(0x3c, 0x7f);
        }

        //LAB_800e5ccc
        backgroundLoaded_800cab10.setu(0);
        loadDrgnDir(2, fileIndex.get(), files -> loadBackground("DRGN2%d/%d".formatted(drgnIndex.get(), fileIndex.get()), files));
        smapLoadingStage_800cb430.setu(0x6L);
      }

      case 0x4 -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        _800caafc.set(submapCut_80052c30.get());
        _800cab00.set(submapScene_80052c34.get());
        smapLoadingStage_800cb430.setu(0x11L);
      }

      case 0x6 -> {
        if(backgroundLoaded_800cab10.get() != 0) {
          smapLoadingStage_800cb430.setu(0x7L);
        }
      }

      case 0x7 -> {
        if(_800cb440.get() == 0) {
          //LAB_800e5d60
          smapLoadingStage_800cb430.setu(0x9L);
        } else {
          smapLoadingStage_800cb430.setu(0x8L);
        }
      }

      case 0x9 -> {
        FUN_800e4d00(submapCut_80052c30.get(), submapScene_80052c34.get());
        FUN_800e81a0(submapScene_80052c34.get());
        loadCollisionAndTransitions(submapCut_80052c30.get());
        FUN_800e6d4c();
        if(_800cab2c.get() != 0) { // This might be to transition to another map or something?
          FUN_800e7328();
          buildBackgroundRenderingPacket(envStruct_800cab30);
          FUN_800e74d0();
          _800cab2c.setu(0);
        }

        //LAB_800e5e20
        smapLoadingStage_800cb430.setu(0xaL);
      }

      case 0xa -> {
        loadingStage_800c68e4.setu(0);
        executeSceneGraphicsLoadingStage(_800caaf8.get());
        smapLoadingStage_800cb430.setu(0xbL);
      }

      case 0xb -> {
        executeSceneGraphicsLoadingStage(_800caaf8.get());
        if(loadingStage_800c68e4.get() == 0xaL) {
          if(isScriptLoaded(0)) {
            sobjs_800c6880[0].innerStruct_00.ui_16c = _800caaf8.get();
          }

          //LAB_800e5e94
          FUN_800e770c();
          savedGameSelected_800bdc34.set(false);
          _80052c44.setu(0);
          scriptStartEffect(2, 10);
          _800cab24 = FUN_800ea974(_800caaf4.get());
          cacheHasNoEncounters();
          smapLoadingStage_800cb430.setu(0xcL);
          SCRIPTS.resume();
          _800c6ae0.setu(0);
        }
      }

      case 0xc -> {
        _80052c44.setu(0);
        FUN_800e5104(_800caaf8.get(), _800cab24);
        if(Input.pressedThisFrame(InputAction.BUTTON_NORTH) && !gameState_800babc8.indicatorsDisabled_4e3) {
          FUN_800e5534(-1, 0x3ff);
        }
      }

      case 0xd -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            scriptStartEffect(1, 10);
          }

          //LAB_800e5fa4
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e5fc0
        if(a0) {
          smapLoadingStage_800cb430.setu(0xeL);
          _800cab28.setu(0);
        }
      }

      case 0xe -> {
        _800caaf0 = whichMenu_800bdc38;
        _80052c44.setu(0x2L);
        if(whichMenu_800bdc38 != WhichMenu.NONE_0) {
          loadAndRenderMenus();

          if(whichMenu_800bdc38 != WhichMenu.NONE_0) {
            break;
          }
        }

        //LAB_800e6018
        _800c6aac.setu(0xaL);
        switch(_800caaf0) {
          case UNLOAD_INVENTORY_MENU_5:
            if(gameState_800babc8.isOnWorldMap_4e4) {
              smapLoadingStage_800cb430.setu(0x12L);
              _800f7e4c.setu(0);
              break;
            }

            // Fall through

          case UNLOAD_CHAR_SWAP_MENU_25:
          case UNLOAD_TOO_MANY_ITEMS_MENU_35:
          case UNLOAD_SHOP_MENU_10:
            smapLoadingStage_800cb430.setu(0xfL);
            break;

          case UNLOAD_SAVE_GAME_MENU_20:
            smapLoadingStage_800cb430.setu(0xcL);
            _800f7e4c.setu(0);
            FUN_800e5534((int)_800f7e2c.offset(gameState_800babc8.chapterIndex_98 * 8).get(), (int)_800f7e30.offset(gameState_800babc8.chapterIndex_98 * 8).get());
            index_80052c38.set((int)_800f7e30.get());
        }
      }

      case 0xf -> {
        _80052c44.setu(0);
        FUN_800e5104(_800caaf8.get(), _800cab24);
        SCRIPTS.resume();
        _800f7e4c.setu(0);
        smapLoadingStage_800cb430.setu(0xcL);
        if(savedGameSelected_800bdc34.get()) {
          FUN_800e5534(submapCut_80052c30.get(), submapScene_80052c34.get());
        }
      }

      case 0x10 -> {
        smapLoadingStage_800cb430.setu(0x3L);
        _80052c44.setu(0x3L);
        _800f7e4c.setu(0);
      }

      case 0x11 -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        if(isScriptLoaded(0)) {
          sobjs_800c6880[0].innerStruct_00.us_12a = 1;
        }

        //LAB_800e61bc
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            scriptStartEffect(1, 10);
          }

          //LAB_800e61fc
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e6218
        if(a0) {
          smapLoadingStage_800cb430.setu(0x3L);

          //LAB_800e6248
          if(_800cb448.get() != 0) {
            _80052c44.setu(0x3L);
          } else {
            _80052c44.setu(0x4L);
          }

          //LAB_800e624c
          //LAB_800e6250
          _800f7e4c.setu(0);
        }
      }

      case 0x12 -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            scriptStartEffect(1, 10);
          }

          //LAB_800e62b0
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e62cc
        if(a0) {
          engineStateOnceLoaded_8004dd24 = EngineState.WORLD_MAP_08;
          pregameLoadingStage_800bb10c.set(0);
          vsyncMode_8007a3b8 = 2;
          _80052c44.setu(0x5L);
          _800f7e4c.setu(0);
          SCRIPTS.resume();
        }
      }

      case 0x13 -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        _80052c44.setu(0x5L);
        engineStateOnceLoaded_8004dd24 = EngineState.COMBAT_06;
        pregameLoadingStage_800bb10c.set(0);
        vsyncMode_8007a3b8 = 2;
        _800f7e4c.setu(0);
        SCRIPTS.resume();
      }

      case 0x14 -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            scriptStartEffect(1, 10);
          }

          //LAB_800e643c
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e6458
        if(a0) {
          FUN_8002a9c0();
          engineStateOnceLoaded_8004dd24 = EngineState.TITLE_02;
          vsyncMode_8007a3b8 = 2;
          pregameLoadingStage_800bb10c.set(0);

          //LAB_800e6484
          _80052c44.setu(0x5L);

          //LAB_800e6490
          _800f7e4c.setu(0);
          SCRIPTS.resume();
        }
      }

      case 0x15 -> {
        FUN_800e5104(_800caaf8.get(), _800cab24);
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || fullScreenEffect_800bb140._24 == 0) {
          if(fullScreenEffect_800bb140._24 == 0) {
            scriptStartEffect(1, 10);
          }

          //LAB_800e6394
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e63b0
        if(a0) {
          _80052c44.setu(0x5L);
          Fmv.playCurrentFmv();
          pregameLoadingStage_800bb10c.set(0);
          _800f7e4c.setu(0);
          SCRIPTS.resume();
        }
      }

      case 0x17 -> {
        engineStateOnceLoaded_8004dd24 = EngineState.TITLE_02;
        vsyncMode_8007a3b8 = 2;
        pregameLoadingStage_800bb10c.set(0);
      }
    }

    //caseD_5
  }

  @Method(0x800e6504L)
  public static void getDrgnFileFromNewRoot(final int submapCut, final IntRef drgnIndexOut, final IntRef fileIndexOut) {
    final SubmapCutInfo entry = newrootPtr_800cab04.submapCutInfo_0000[submapCut];

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
  public static void loadCollisionAndTransitions(final int submapCut) {
    Arrays.fill(collisionAndTransitions_800cb460, 0);

    final SubmapCutInfo entry = newrootPtr_800cab04.submapCutInfo_0000[submapCut];
    final short offset = entry.collisionAndTransitionOffset_04;

    if(offset < 0) {
      return;
    }

    //LAB_800e66dc
    for(int i = 0; i < entry.collisionAndTransitionCount_06; i++) {
      final int v1 = newrootPtr_800cab04.collisionAndTransitions_2000[offset / 4 + i];
      collisionAndTransitions_800cb460[(v1 >> 8 & 0xfc) / 4] = v1;
    }

    //LAB_800e671c
  }

  @Method(0x800e6730L)
  public static int getCollisionAndTransitionInfo(final int index) {
    // This did unsigned comparison, so -1 was >= 0x40
    if(index < 0 || index >= 0x40) {
      return 0;
    }

    return collisionAndTransitions_800cb460[index];
  }

  @Method(0x800e675cL)
  public static void FUN_800e675c(final int a0) {
    collisionAndTransitions_800cb460[(a0 >> 8 & 0xfc) / 4] = a0;
  }

  @Method(0x800e6798L)
  public static int FUN_800e6798(final int index, final long a1, final long x, final long y, final long z, final SVECTOR playerMovement) {
    final int v1 = getCollisionAndTransitionInfo(index);

    if((v1 & 0x8) != 0) {
      return 0;
    }

    //LAB_800e67c4
    return 1;
  }

  @Method(0x800e67d4L)
  public static FlowControl FUN_800e67d4(final RunningScript<?> script) {
    final int scene = script.params_20[1].get();

    FUN_800e5534(script.params_20[0].get(), scene);

    if(scene == 0x3fe || scene == 0x3fa || scene == 0x3ff) {
      return FlowControl.CONTINUE;
    }

    //LAB_800e6828
    return scene != 0x3fc ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @Method(0x800e683cL)
  public static FlowControl FUN_800e683c(final RunningScript<?> script) {
    if(script.params_20[0].get() < 3) {
      _800f7e50.setu(script.params_20[0].get());
    }

    //LAB_800e686c
    if(script.params_20[0].get() == 3) {
      FUN_800e76b0(0x400, 0x400, script.params_20[1].get());
    }

    //LAB_800e688c
    script.params_20[2].set((int)_800f7e50.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e68b4L)
  public static FlowControl FUN_800e68b4(final RunningScript<?> script) {
    script.params_20[0].set(screenOffsetX_800cb568.get());
    script.params_20[1].set(screenOffsetY_800cb56c.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6904L)
  public static FlowControl FUN_800e6904(final RunningScript<?> script) {
    final int x = script.params_20[0].get();
    final int y = script.params_20[1].get();
    final long v1 = _800f7e50.get();

    if(v1 == 0x1L) {
      //LAB_800e695c
      setGeomOffsetIfNotSet(x, y);
    } else {
      if(v1 == 0x2L) {
        //LAB_800e6970
        setGeomOffsetIfNotSet(x, y);
      }

      //LAB_800e697c
      setScreenOffsetIfNotSet(x, y);
    }

    //LAB_800e6988
    _800f7e50.setu(0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e69a4L)
  public static FlowControl FUN_800e69a4(final RunningScript<?> script) {
    script.params_20[0].set(getCollisionAndTransitionInfo(script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e69e8L)
  public static FlowControl FUN_800e69e8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800e69f0L)
  public static FlowControl FUN_800e69f0(final RunningScript<?> script) {
    FUN_800e675c(getCollisionAndTransitionInfo(script.params_20[0].get()) | 0x8);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6a28L)
  public static FlowControl FUN_800e6a28(final RunningScript<?> script) {
    FUN_800e675c(getCollisionAndTransitionInfo(script.params_20[0].get()) & 0xffff_fff7);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6a64L)
  public static FlowControl FUN_800e6a64(final RunningScript<?> script) {
    FUN_800e76b0(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6aa0L)
  public static FlowControl FUN_800e6aa0(final RunningScript<?> script) {
    script.params_20[3].set((int)FUN_800e7728(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6af0L)
  public static FlowControl FUN_800e6af0(final RunningScript<?> script) {
    if(script.params_20[1].get() == 1) {
      if(script.params_20[0].get() != 0) {
        _800f7e54.oru(0x1L);
      } else {
        //LAB_800e6b34
        _800f7e54.and(0xffff_fffeL);
      }
    }

    //LAB_800e6b48
    script.params_20[2].set((int)_800f7e54.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6b64L)
  public static FlowControl FUN_800e6b64(final RunningScript<?> script) {
    if(script.params_20[0].get() >= 0) {
      final SVECTOR sp0x10 = new SVECTOR();
      get3dAverageOfSomething(script.params_20[0].get(), sp0x10);

      script.params_20[1].set(sp0x10.getX());
      script.params_20[2].set(sp0x10.getY());
      script.params_20[3].set(sp0x10.getZ());
    }

    //LAB_800e6bc8
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6bd8L)
  public static FlowControl FUN_800e6bd8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6be0L)
  public static FlowControl FUN_800e6be0(final RunningScript<?> script) {
    final MATRIX coord = sobjs_800c6880[script.params_20[0].get()].innerStruct_00.model_00.coord2_14.coord;
    script.params_20[1].set((worldToScreenMatrix_800c3548.get(6) * coord.transfer.getX() + worldToScreenMatrix_800c3548.get(7) * coord.transfer.getY() + worldToScreenMatrix_800c3548.get(8) * coord.transfer.getZ() >> 12) + worldToScreenMatrix_800c3548.transfer.getZ() >> 16 - orderingTableBits_1f8003c0.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6cacL)
  public static FlowControl FUN_800e6cac(final RunningScript<?> script) {
    FUN_800e80e4(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800e6ce0L)
  public static FlowControl FUN_800e6ce0(final RunningScript<?> script) {
    FUN_800e5534(script.params_20[0].get() + 0x800, script.params_20[1].get());
    submapCut_80052c30.set(script.params_20[2].get());
    _800cb450.setu(submapCut_80052c30.get());
    submapScene_80052c34.set(script.params_20[3].get());
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x800e6d4cL)
  public static void FUN_800e6d4c() {
    _800f7e54.setu(0);
  }

  @Method(0x800e6d58L)
  public static long FUN_800e6d58(final long submapCut) {
    for(int i = 0; i < 0x2d; i++) {
      if(_800f7e58.offset(i * 0x4L).get() == submapCut) {
        return 0x1L;
      }
    }

    return 0;
  }

  @Method(0x800e6d9cL)
  public static void FUN_800e6d9c(final EnvironmentStruct[] envs, final long count) {
    final long s0;
    final long s1;
    long t0;
    long t1;
    long t2;
    long t3;
    long v1;

    t3 = 0x7fffL;
    t1 = -0x8000L;
    t2 = t3;
    t0 = t1;

    //LAB_800e6dc8
    for(int i = 0; i < count; i++) {
      final EnvironmentStruct env = envs[i];

      if(env.s_06 == 0x4e) {
        v1 = env.pos_08.w.get() + env.us_10;
        if(v1 < t1) {
          v1 = t1;
        }

        //LAB_800e6e00
        t1 = v1;
        v1 = env.us_10;
        if(t3 < v1) {
          v1 = t3;
        }

        //LAB_800e6e1c
        t3 = v1;
        v1 = env.pos_08.h.get() + env.us_12;
        if(v1 < t0) {
          v1 = t0;
        }

        //LAB_800e6e44
        t0 = v1;
        v1 = env.us_12;
        if(t2 < v1) {
          v1 = t2;
        }

        //LAB_800e6e60
        t2 = v1;
      }

      //LAB_800e6e64
    }

    //LAB_800e6e74
    s0 = t1 - t3;
    s1 = t0 - t2;
    _800cb560.setu(-s0 / 2);
    _800cb564.setu(-s1 / 2);
    _800cb570.setu((s0 - 0x180L) / 2);

    //LAB_800e6ecc
    if(s0 == 0x180L && s1 == 0x100L || FUN_800e6d58(submapCut_80052c30.get()) != 0) {
      //LAB_800e6ee0
      _800cb574.setu((0x178L - s0) / 2);
    } else {
      //LAB_800e6f00
      _800cb574.setu(-_800cb570.get());
    }

    //LAB_800e6f14
    _800cb578.setu((s1 - 0xf0L) / 2);
  }

  @Method(0x800e6f38L)
  public static void buildBackgroundRenderingPacket(final EnvironmentStruct[] env) {
    //LAB_800e6f9c
    for(int i = 0; i < backgroundObjectsCount_800cb584.get(); i++) {
      final EnvironmentStruct s0 = env[i];
      final long renderPacket = _800cb710.offset(i * 0x24L).offset(0x8L).getAddress();

      MEMORY.ref(1, renderPacket).offset(0x3L).setu(0x4L); // 4 words
      MEMORY.ref(4, renderPacket).offset(0x4L).setu(0x6480_8080L); // Textured rectangle, variable size, opaque, texture-blending

      int clutY = Math.abs(s0.clutY_22);
      if(i < _800cb57c.get()) {
        if(clutY - 0x1f0 >= 0x10) {
          clutY = i + 0x1f0;
        }
      } else {
        //LAB_800e7010
        _800cbb90[i - _800cb57c.get()].set(s0.svec_14);
        _800cbc90.offset((i - _800cb57c.get()) * 0x4L).setu(s0.ui_1c);
      }

      //LAB_800e7004
      MEMORY.ref(2, renderPacket).offset(0xeL).setu(clutY << 6 | 0x30L); // CLUT

      //LAB_800e7074
      MEMORY.ref(1, renderPacket).offset(0x4L).setu(0x80L); // R
      MEMORY.ref(1, renderPacket).offset(0x5L).setu(0x80L); // G
      MEMORY.ref(1, renderPacket).offset(0x6L).setu(0x80L); // B
      MEMORY.ref(1, renderPacket).offset(0x0cL).setu(s0.pos_08.x.get()); // X
      MEMORY.ref(1, renderPacket).offset(0x0dL).setu(s0.pos_08.y.get()); // Y
      MEMORY.ref(2, renderPacket).offset(0x10L).setu(s0.pos_08.w.get()); // Width
      MEMORY.ref(2, renderPacket).offset(0x12L).setu(s0.pos_08.h.get()); // Height

      final long s0_0 = _800cb710.offset(i * 0x24L).getAddress();
      SetDrawTPage(MEMORY.ref(4, s0_0, DR_TPAGE::new), false, true, s0.tpage_20);

      if(s0.clutY_22 < 0) {
        gpuLinkedListSetCommandTransparency(s0_0, true);
      }

      //LAB_800e70ec
      final long tpagePacket = _800cb710.offset(i * 0x24L).getAddress();
      MargePrim(tpagePacket, renderPacket);

      MEMORY.ref(2, tpagePacket).offset(0x1cL).setu(s0.us_10);
      MEMORY.ref(2, tpagePacket).offset(0x1eL).setu(s0.us_12);

      if(s0.s_06 == 0x4e) {
        //LAB_800e7148
        MEMORY.ref(2, tpagePacket).offset(0x20L).setu((0x1L << orderingTableBits_1f8003c0.get()) - 0x1L);
      } else if(s0.s_06 == 0x4f) {
        MEMORY.ref(2, tpagePacket).offset(0x20L).setu(0x28L);
      } else {
        //LAB_800e7194
        long a0 =
          worldToScreenMatrix_800c3548.get(6) * s0.svec_00.getX() +
          worldToScreenMatrix_800c3548.get(7) * s0.svec_00.getY() +
          worldToScreenMatrix_800c3548.get(8) * s0.svec_00.getZ();
        a0 >>= 12;
        a0 += worldToScreenMatrix_800c3548.transfer.z.get();
        a0 >>= 16 - orderingTableBits_1f8003c0.get();
        MEMORY.ref(2, tpagePacket).offset(0x20L).setu(a0);
      }

      //LAB_800e7210
      MEMORY.ref(2, tpagePacket).offset(0x22L).and(0x3fffL);
    }

    //LAB_800e724c
    FUN_800e6d9c(env, _800cb57c.get());
  }

  @Method(0x800e728cL)
  public static void clearSmallValuesFromMatrix(final MATRIX matrix) {
    //LAB_800e72b4
    for(int x = 0; x < 3; x++) {
      //LAB_800e72c4
      for(int y = 0; y < 3; y++) {
        if(Math.abs(matrix.get(x, y)) < 0x40L) {
          matrix.set(x, y, (short)0);
        }

        //LAB_800e72e8
      }
    }
  }

  @Method(0x800e7328L)
  public static void FUN_800e7328() {
    setProjectionPlaneDistance((int)projectionPlaneDistance_800bd810.get());
    GsSetRefView2L(rview2_800cbd10);
    clearSmallValuesFromMatrix(worldToScreenMatrix_800c3548);
    matrix_800cbd68.set(worldToScreenMatrix_800c3548);
    matrix_800cbd68.transpose(matrix_800cbd40);
    rview2_800bd7e8.set(rview2_800cbd10);
  }

  @Method(0x800e7418L)
  public static void updateRview2(final SVECTOR viewpoint, final SVECTOR refpoint, final int rotation, final long projectionDistance) {
    rview2_800cbd10.viewpoint_00.set(viewpoint);
    rview2_800cbd10.refpoint_0c.set(refpoint);
    rview2_800cbd10.viewpointTwist_18 = (short)rotation << 12;
    rview2_800cbd10.super_1c = null;
    projectionPlaneDistance_800bd810.setu(projectionDistance & 0xffffL);

    if(_800cab2c.get() == 0) {
      FUN_800e7328();
    }
  }

  @Method(0x800e74d0L)
  public static void FUN_800e74d0() {
    SetRotMatrix(matrix_800cbd68);
    SetTransMatrix(matrix_800cbd68);
  }

  @Method(0x800e7500L)
  public static void loadEnvironment(final EnvironmentFile envFile) {
    backgroundObjectsCount_800cb584.setu(envFile.count_14);
    _800cb57c.set(envFile.ub_15);
    _800cb580.set(envFile.ub_16);

    updateRview2(
      envFile.viewpoint_00,
      envFile.refpoint_08,
      envFile.rotation_12,
      envFile.projectionDistance_10
    );

    envStruct_800cab30 = envFile.environments_18;
    buildBackgroundRenderingPacket(envStruct_800cab30);

    for(final Struct0c struct : _800cb590) {
      struct.clear();
    }
  }

  @Method(0x800e7604L)
  public static void setGeomOffsetIfNotSet(final int x, final int y) {
    if(!_800cbd3c._00) {
      _800cbd3c._00 = true;
      SetGeomOffset(x, y);
    }
  }

  @Method(0x800e7650L)
  public static void setScreenOffsetIfNotSet(final int x, final int y) {
    // Added null check - bug in game code
    if(_800cbd38 != null && !_800cbd38._00) {
      _800cbd38._00 = true;
      screenOffsetX_800cb568.set(x);
      screenOffsetY_800cb56c.set(y);
    }
  }

  @Method(0x800e76b0L)
  public static void FUN_800e76b0(final int x, final int y, final int index) {
    if(x == 0x400 && y == 0x400) {
      _800cb590[index]._08 = true;
      return;
    }

    //LAB_800e76e8
    //LAB_800e76ec
    _800cb590[index].x_00 = x;
    _800cb590[index].y_04 = y;
    _800cb590[index]._08 = false;
  }

  @Method(0x800e770cL)
  public static void FUN_800e770c() {
    _800cbd60.setu(0);
    _800cbd64.setu(sobjCount_800c6730.get());
  }

  @Method(0x800e7728L)
  public static long FUN_800e7728(final long a0, final long a1, long a2) {
    final long a3 = _800cb57c.get() + a1;

    if(a0 == 0x1L && (int)a1 == -0x1L) {
      //LAB_800e7780
      for(int i = 0; i < _800cb580.get(); i++) {
        _800cb710.offset(2, 0x22L).offset((_800cb57c.get() + i) * 0x24L).and(0x3fffL).oru(0x4000L);
      }

      //LAB_800e77b4
      return _800cb710.offset(2, 0x20L).offset(a3 * 0x24L).getSigned();
    }

    //LAB_800e77d8
    if(a3 > 0x20L) {
      return -0x1L;
    }

    if(a0 == 0) {
      //LAB_800e7860
      _800cb710.offset(2, 0x22L).offset(a3 * 0x24L).and(0x3fffL);
    } else if(a0 == 0x1L) {
      //LAB_800e77e8
      //LAB_800e7830
      _800cb710.offset(2, 0x22L).offset(a3 * 0x24L).and(0x3fffL).oru(0x4000L);
      //LAB_800e7808
    } else if(a0 == 0x2L) {
      //LAB_800e788c
      _800cb710.offset(2, 0x22L).offset(a3 * 0x24L).and(0x3fffL).oru(0x8000L);

      if(a2 < 0x28L) {
        //LAB_800e78fc
        a2 = 0x28L;
      } else if((0x1L << orderingTableBits_1f8003c0.get()) - 0x1L < a2) {
        a2 = (0x1L << orderingTableBits_1f8003c0.get()) - 0x1L;
      }

      //LAB_800e7900
      _800cb710.offset(2, 0x22L).offset(a3 * 0x24L).and(0xc000L).oru(a2 & 0x3fffL);
    } else if(a0 == 0x5L) {
      _800cbd60.setu(a1);
      _800cbd64.setu(a2 + 0x1L);
    }

    //LAB_800e7930
    //LAB_800e7934
    //LAB_800e7938
    return _800cb710.offset(2, 0x20L).offset(a3 * 0x24L).getSigned();
  }

  /**
   * Renders the background?
   */
  @Method(0x800e7954L)
  public static void FUN_800e7954(final MATRIX[] a1, final int count) {
    final long s0;
    long s1;
    final long s3;
    long v1;
    long a3;
    long t0;
    long t1;
    long t3;
    final long[] sp10 = new long[count];
    final short[] sp38 = new short[(int)_800cb580.get()];

    s1 = _800cb710.getAddress();

    //LAB_800e79b8
    for(int i = 0; i < _800cb57c.get(); i++) {
      MEMORY.ref(2, s1).offset(0x10L).setu(_800cb560.getSigned() + screenOffsetX_800cb568.get() + MEMORY.ref(2, s1).offset(0x1cL).get()); // X
      MEMORY.ref(2, s1).offset(0x12L).setu(_800cb564.getSigned() + screenOffsetY_800cb56c.get() + MEMORY.ref(2, s1).offset(0x1eL).get()); // Y

      final int clut = (int)MEMORY.get(s1 + 0x16L, 2);
      final int tpage = (int)MEMORY.get(s1 + 0x4L, 3);

      GPU.queueCommand((int)MEMORY.ref(2, s1).offset(0x20L).get(), new GpuCommandQuad()
        .rgb((int)MEMORY.get(s1 + 0xcL, 3))
        .pos((short)MEMORY.get(s1 + 0x10L, 2), (short)MEMORY.get(s1 + 0x12L, 2), (short)MEMORY.get(s1 + 0x18L, 2), (short)MEMORY.get(s1 + 0x1aL, 2))
        .uv((int)MEMORY.get(s1 + 0x14L, 1), (int)MEMORY.get(s1 + 0x15L, 1))
        .clut((clut & 0b111111) * 16, clut >>> 6)
        .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
        .bpp(Bpp.of(tpage >>> 7 & 0b11)));

      s1 += 0x24L;
    }

    //LAB_800e7a60
    //LAB_800e7a7c
    for(int i = 0; i < count; i++) {
      sp10[i] = (worldToScreenMatrix_800c3548.get(6) * a1[i].transfer.getX() +
        worldToScreenMatrix_800c3548.get(7) * a1[i].transfer.getY() +
        worldToScreenMatrix_800c3548.get(8) * a1[i].transfer.getZ() >> 12) + worldToScreenMatrix_800c3548.transfer.getZ() >> 0x10L - orderingTableBits_1f8003c0.get();
    }

    //LAB_800e7b08
    s3 = _800cb710.getAddress();
    s0 = _800cb57c.get();

    //LAB_800e7b40
    outerForLoop:
    for(int i = 0; i < _800cb580.get(); i++) {
      t3 = 0x7fff_ffffL;
      v1 = MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x22L).getSigned(0xc000L) >> 14;
      if(v1 != 0x1L) {
        if(v1 != 2) {
          //LAB_800e7d78
          sp38[i] = (short)MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).get();
        } else {
          sp38[i] = (short)(MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x22L).get() & 0x3fff);
        }

        continue;
      }

      //LAB_800e7bb4
      a3 = 0;
      do {
        //LAB_800e7bbc
        long a2_0 = _800cbd64.get() - 0x1L;
        t0 = 0;
        t1 = 0;

        //LAB_800e7c0c
        while(a2_0 >= _800cbd60.get()) {
          v1 = _800cbc90.offset(i * 0x4L).get();
          v1 += _800cbb90[i].getX() * a1[(int)a2_0].transfer.getX();
          v1 += _800cbb90[i].getY() * a1[(int)a2_0].transfer.getY();
          v1 += _800cbb90[i].getZ() * a1[(int)a2_0].transfer.getZ();
          final long a1_0 = sp10[(int)a2_0] & 0xffffL;
          if(a1_0 != 0xfffbL) {
            if((int)v1 < 0) {
              //TODO unused? MEMORY.ref(4, sp0xc8).offset(a2_0 * 0x2L).setu(a1_0);
              t1++;
              if(t3 > a1_0) {
                t3 = a1_0;
              }
            } else {
              //LAB_800e7cac
              //TODO unused? MEMORY.ref(4, sp0x78).offset(a2_0 * 0x2L).setu(a1_0);
              t0++;
              if(a3 < a1_0) {
                a3 = a1_0;
              }
            }
          }

          //LAB_800e7cc8
          a2_0--;
        }

        //LAB_800e7cd8
        if(t0 == 0) {
          //LAB_800e7cf8
          sp38[i] = (short)Math.max(a3 - 50, 40);
          continue outerForLoop;
        }

        //LAB_800e7d00
        if(t1 == 0) {
          //LAB_800e7d3c
          sp38[i] = (short)Math.min(a3 + 50, (1 << orderingTableBits_1f8003c0.get()) - 1);
          continue outerForLoop;
        }

        break;
      } while(true);

      //LAB_800e7d50
      if(a3 > MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).getSigned() || t3 < MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).getSigned()) {
        //LAB_800e7d64
        sp38[i] = (short)(((short)a3 + t3) / 2);
      } else {
        //LAB_800e7d78
        sp38[i] = (short)MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).getSigned();
      }

      //LAB_800e7d80
    }

    //LAB_800e7d9c
    s1 = _800cb710.offset(_800cb57c.get() * 0x24L).getAddress();

    //LAB_800e7de0
    for(int i = 0; i < _800cb580.get(); i++) {
      if(!_800cb590[i]._08) {
        MEMORY.ref(2, s1).offset(0x10L).setu(_800cb560.getSigned() + screenOffsetX_800cb568.get() + MEMORY.ref(2, s1).offset(0x1cL).get() + _800cb590[i].x_00);
        MEMORY.ref(2, s1).offset(0x12L).setu(_800cb564.getSigned() + screenOffsetY_800cb56c.get() + MEMORY.ref(2, s1).offset(0x1eL).get() + _800cb590[i].y_04);

        final int clut = (int)MEMORY.get(s1 + 0x16L, 2);
        final int tpage = (int)MEMORY.get(s1 + 0x4L, 3);

        // This was causing a problem when moving left from the room before Zackwell. Not sure if this is a retail issue or SC-specific. GH#332
        int z = sp38[i];
        if(z < 0) {
          z = (1 << orderingTableBits_1f8003c0.get()) - 1;
        }

        GPU.queueCommand(z, new GpuCommandQuad()
          .rgb((int)MEMORY.get(s1 + 0xcL, 3))
          .pos((short)MEMORY.get(s1 + 0x10L, 2), (short)MEMORY.get(s1 + 0x12L, 2), (short)MEMORY.get(s1 + 0x18L, 2), (short)MEMORY.get(s1 + 0x1aL, 2))
          .uv((int)MEMORY.get(s1 + 0x14L, 1), (int)MEMORY.get(s1 + 0x15L, 1))
          .clut((clut & 0b111111) * 16, clut >>> 6)
          .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
          .bpp(Bpp.of(tpage >>> 7 & 0b11)));
      }

      //LAB_800e7eb4
      s1 += 0x24L;
    }

    //LAB_800e7ed0
  }

  @Method(0x800e7f00L)
  public static void transformVertex(final IntRef outX, final IntRef outY, final SVECTOR v0) {
    GTE.perspectiveTransform(v0);
    outX.set(GTE.getScreenX(2));
    outY.set(GTE.getScreenY(2));
  }

  @Method(0x800e7f68L)
  public static void FUN_800e7f68(final int x, final int y) {
    if(x < -80) {
      screenOffsetX_800cb568.sub(80).sub(x);
      //LAB_800e7f80
    } else if(x > 80) {
      //LAB_800e7f9c
      screenOffsetX_800cb568.add(80).sub(x);
    }

    //LAB_800e7fa8
    if(y < -40) {
      screenOffsetY_800cb56c.sub(40).sub(y);
      //LAB_800e7fbc
    } else if(y > 40) {
      //LAB_800e7fd4
      screenOffsetY_800cb56c.add(40).sub(y);
    }

    //LAB_800e7fdc
    if(_800f7f0c.get() != 0) {
      screenOffsetX_800cb568.add((int)_800cbd30.get());
      screenOffsetY_800cb56c.add((int)_800cbd34.get());
      _800f7f0c.setu(0);
      return;
    }

    //LAB_800e8030
    if(screenOffsetX_800cb568.get() < _800cb574.getSigned()) {
      //LAB_800e807c
      screenOffsetX_800cb568.set((int)_800cb574.get());
    } else {
      //LAB_800e8070
      screenOffsetX_800cb568.set(Math.min((int)_800cb570.getSigned(), screenOffsetX_800cb568.get()));
    }

    //LAB_800e8080
    //LAB_800e8088
    if(screenOffsetY_800cb56c.get() < -_800cb578.getSigned()) {
      screenOffsetY_800cb56c.set((int)-_800cb578.getSigned());
    } else {
      //LAB_800e80d0
      //LAB_800e80d8
      screenOffsetY_800cb56c.set(Math.min((int)_800cb578.getSigned(), screenOffsetY_800cb56c.get()));
    }

    //LAB_800e80dc
  }

  @Method(0x800e06c4L)
  public static FlowControl FUN_800e06c4(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    sobj.collisionSizeHorizontal_1a0 = script.params_20[1].get();
    sobj.collisionSizeVertical_1a4 = script.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800e0710L)
  public static FlowControl FUN_800e0710(final RunningScript<?> script) {
    script.params_20[1].set(((SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).collidedWithSobjIndex_19c);
    return FlowControl.CONTINUE;
  }

  @Method(0x800e80e4L)
  public static void FUN_800e80e4(final int a0, final int a1) {
    _800cbd30.setu(a0);
    _800cbd34.setu(a1);
    _800f7f0c.setu(0x1L);
  }

  @Method(0x800e8104L)
  public static void FUN_800e8104(final SVECTOR v0) {
    if(!_800cbd38._00) {
      _800cbd38._00 = true;

      final IntRef transformedX = new IntRef();
      final IntRef transformedY = new IntRef();
      transformVertex(transformedX, transformedY, v0);
      FUN_800e7f68(transformedX.get(), transformedY.get());
    }

    //LAB_800e8164
    setScreenOffsetIfNotSet(screenOffsetX_800cb568.get(), screenOffsetY_800cb56c.get());
    setGeomOffsetIfNotSet(screenOffsetX_800cb568.get(), screenOffsetY_800cb56c.get());
  }

  @Method(0x800e81a0L)
  public static void FUN_800e81a0(final int index) {
    final UnknownStruct2 s0_0 = new UnknownStruct2();
    FUN_800e5084(s0_0);
    _800cbd38 = s0_0;

    final UnknownStruct2 s0_1 = new UnknownStruct2();
    FUN_800e5084(s0_1);
    _800cbd3c = s0_1;

    final SVECTOR avg = new SVECTOR();
    get3dAverageOfSomething(index, avg);
    FUN_800e8104(avg);
  }

  @Method(0x800e828cL)
  public static void FUN_800e828c() {
    _800cbd38 = null;
    _800cbd3c = null;
  }

  @Method(0x800e82ccL)
  public static void transformToWorldspace(final SVECTOR out, final SVECTOR in) {
    if(matrix_800cbd40.get(2) == 0) {
      out.set(in);
    } else {
      //LAB_800e8318
      in.mul(matrix_800cbd40, out);
    }

    //LAB_800e833c
  }

  @Method(0x800e866cL)
  public static void FUN_800e866c() {
    //LAB_800e86a4
    for(int i = 0; i < SomethingStructPtr_800d1a88.count_0c; i++) {
      final int y = Math.abs(SomethingStructPtr_800d1a88.normals_08[i].getY());
      SomethingStructPtr_800d1a88.ptr_14[i].bool_01 = y > 0x400;
    }

    //LAB_800e86f0
  }

  @Method(0x800e88a0L)
  public static int FUN_800e88a0(final long a0, final MATRIX playerTransforms, final SVECTOR playerMovement) {
    if(a0 != 0) {
      return FUN_800e9430(0, playerTransforms.transfer.getX(), playerTransforms.transfer.getY(), playerTransforms.transfer.getZ(), playerMovement);
    }

    //LAB_800e88d8
    if(!_800cbe34._00) {
      _800cbe34._00 = true;

      //LAB_800e8908
      _800cbd94.setu(FUN_800e9430(0, playerTransforms.transfer.getX(), playerTransforms.transfer.getY(), playerTransforms.transfer.getZ(), playerMovement));
      _800cbd98.set(playerMovement);
    } else {
      //LAB_800e8954
      playerMovement.set(_800cbd98);
    }

    //LAB_800e897c
    //LAB_800e8980
    return (int)_800cbd94.get();
  }

  @Method(0x800e8990L)
  public static int FUN_800e8990(final int x, final int z) {
    final SVECTOR vec = new SVECTOR();

    int farthestIndex = 0;
    int farthest = 0x7fff_ffff;
    final SomethingStruct struct = SomethingStructPtr_800d1a88;

    //LAB_800e89b8
    for(int i = 0; i < struct.count_0c; i++) {
      //LAB_800e89e0
      if(_800f7f14.get() == 0) {
        //LAB_800e89e8
        vec.setX((short)0);
        vec.setY((short)0);
        vec.setZ((short)0);
      } else {
        //LAB_800e89f8
        final SomethingStructSub0c_1 struct2 = struct.ptr_14[i];
        final TmdObjTable1c.Primitive primitive = struct.getPrimitiveForOffset(struct2.primitivesOffset_04);
        final int packetOffset = struct2.primitivesOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        vec.set((short)0, (short)0, (short)0);

        //LAB_800e8a38
        for(int t0 = 0; t0 < struct2.count_00; t0++) {
          vec.add(struct.verts_04[IoHelper.readUShort(packet, remainder + 2 + t0 * 2)]);
        }

        //LAB_800e8a9c
        vec.div(struct2.count_00);
      }

      //LAB_800e8ae4
      final int dx = x - vec.getX();
      final int dz = z - vec.getZ();
      final int distSqr = dx * dx + dz * dz;
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
  public static void FUN_800e8b40(final SomethingStruct a0, FileData a1) {
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
  public static void FUN_800e8bd8(final SomethingStruct a0) {
    final TmdObjTable1c objTable = a0.objTableArrPtr_00[0];
    a0.verts_04 = objTable.vert_top_00;
    a0.normals_08 = new SVECTOR[objTable.normal_top_08.length];
    Arrays.setAll(a0.normals_08, i -> new SVECTOR().set((short)(objTable.normal_top_08[i].x * 4096.0f), (short)(objTable.normal_top_08[i].y * 4096.0f), (short)(objTable.normal_top_08[i].z * 4096.0f)));
    a0.count_0c = objTable.n_primitive_14;
    a0.primitives_10 = objTable.primitives_10;
  }

  @Method(0x800e8c50L)
  public static void FUN_800e8c50(final GsDOBJ2 dobj2, final SomethingStruct a1, final TmdWithId tmd) {
    a1.tmdPtr_1c = tmd;
    final TmdObjTable1c[] objTables = tmd.tmd.objTable;
    dobj2.tmd_08 = objTables[0];
    a1.objTableArrPtr_00 = objTables;
    FUN_800e8bd8(a1);
  }

  @Method(0x800e8cd0L)
  public static void FUN_800e8cd0(final TmdWithId tmd, final FileData a2) {
    SomethingStructPtr_800d1a88 = SomethingStruct_800cbe08;
    SomethingStruct_800cbe08.dobj2Ptr_20 = GsDOBJ2_800cbdf8;
    SomethingStruct_800cbe08.coord2Ptr_24 = GsCOORDINATE2_800cbda8;
    GsInitCoordinate2(null, GsCOORDINATE2_800cbda8);

    SomethingStructPtr_800d1a88.dobj2Ptr_20.coord2_04 = SomethingStructPtr_800d1a88.coord2Ptr_24;
    SomethingStructPtr_800d1a88.dobj2Ptr_20.attribute_00 = 0x4000_0000;

    FUN_800e8c50(SomethingStructPtr_800d1a88.dobj2Ptr_20, SomethingStructPtr_800d1a88, tmd);
    FUN_800e8b40(SomethingStructPtr_800d1a88, a2);

    _800f7f10.setu(0);
    _800f7f14.setu(0x1L);

    final UnknownStruct2 s0_0 = new UnknownStruct2();
    FUN_800e5084(s0_0);
    _800cbe34 = s0_0;

    final UnknownStruct2 s0_1 = new UnknownStruct2();
    FUN_800e5084(s0_1);
    _800d1a8c = s0_1;

    final UnknownStruct2 s0_2 = new UnknownStruct2();
    FUN_800e5084(s0_2);
    _800cbe38 = s0_2;

    FUN_800e866c();
  }

  /** Unloads data when transitioning */
  @Method(0x800e8e50L)
  public static void FUN_800e8e50() {
    _800f7f14.setu(0);

    _800cbe34 = null;
    _800d1a8c = null;
    _800cbe38 = null;
  }

  @Method(0x800e9018L)
  public static int FUN_800e9018(final int x, final int y, final int z, final int a3) {
    int t2 = 0;

    //LAB_800e9040
    for(int i = 0; i < SomethingStructPtr_800d1a88.count_0c; i++) {
      final SomethingStructSub0c_1 a1 = SomethingStructPtr_800d1a88.ptr_14[i];
      if(a3 != 1 || a1.bool_01) {
        //LAB_800e9078
        //LAB_800e90a0
        boolean v0 = true;
        for(int n = 0; n < a1.count_00; n++) {
          final SomethingStructSub0c_2 a0 = SomethingStructPtr_800d1a88.ptr_18[a1._02 + n];

          if(a0.x_00 * x + a0.y_02 * z + a0._04 < 0) {
            //LAB_800e910c
            v0 = false;
            break;
          }
        }

        //LAB_800e90f0
        if(v0) {
          _800cbe48.offset(t2 * 0x4L).setu(i);
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
      return (int)_800cbe48.get();
    }

    //LAB_800e9134
    int t0 = 0x7fff_ffff;
    int t3 = -1;
    final SVECTOR[] normals = SomethingStructPtr_800d1a88.normals_08;

    //LAB_800e9164
    int v1;
    for(int i = 0; i < t2; i++) {
      final int a3_0 = (int)_800cbe48.offset(i * 0x4L).get();
      final SomethingStructSub0c_1 t5 = SomethingStructPtr_800d1a88.ptr_14[a3_0];

      v1 = -normals[a3_0].getX() * x - normals[a3_0].getZ() * z - t5._08;

      if(normals[a3_0].getY() != 0) {
        v1 = v1 / normals[a3_0].getY();
      } else {
        v1 = -1;
      }

      v1 = v1 - (y - 20);
      if(v1 > 0 && v1 < t0) {
        t3 = a3_0;
        t0 = v1;
      }

      //LAB_800e91ec
    }

    //LAB_800e91fc
    if(t0 == 0x7fff_ffff) {
      //LAB_800e920c
      return -1;
    }

    //LAB_800e9210
    //LAB_800e9214
    return t3;
  }

  @Method(0x800e92dcL)
  public static long get3dAverageOfSomething(final int index, final SVECTOR out) {
    out.set((short)0, (short)0, (short)0);

    final SomethingStruct ss = SomethingStructPtr_800d1a88;

    if(_800f7f14.get() == 0 || index < 0 || index >= ss.count_0c) {
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
  public static int FUN_800e9430(final int unused, final int x, final int y, final int z, final SVECTOR playerMovement) {
    int v0;
    int v1;
    int a0;
    int a1;
    int a2;
    int a3;
    long t0;
    int t5;
    int t6;
    int s0;
    int s1;
    int s2;
    int s4;
    final int s5;
    final int s6;
    final SVECTOR sp0x28 = new SVECTOR();
    int sp30 = 0; //TODO was uninitialized
    int sp34 = 0; //TODO was uninitialized
    int sp38 = 0; //TODO was uninitialized

    if(smapLoadingStage_800cb430.get() != 0xcL) {
      return -1;
    }

    if(playerMovement.getX() == 0 && playerMovement.getZ() == 0) {
      return -1;
    }

    int s3 = 0;

    //LAB_800e94a4
    if(playerMovement.getX() * playerMovement.getX() + playerMovement.getZ() * playerMovement.getZ() > 0x40L) {
      _800cbe30.setu(0xcL);
    } else {
      //LAB_800e94e4
      _800cbe30.setu(0x4L);
    }

    //LAB_800e94ec
    s6 = x + playerMovement.getX();
    s5 = z + playerMovement.getZ();
    t6 = y - 20;
    t0 = 0;
    int t1;
    int t2;

    //LAB_800e9538
    for(a3 = 0; a3 < SomethingStructPtr_800d1a88.count_0c; a3++) {
      if(SomethingStructPtr_800d1a88.ptr_14[a3].bool_01) {
        //LAB_800e9594
        v0 = 1;
        for(a2 = 0; a2 < SomethingStructPtr_800d1a88.ptr_14[a3].count_00; a2++) {
          final SomethingStructSub0c_2 struct = SomethingStructPtr_800d1a88.ptr_18[SomethingStructPtr_800d1a88.ptr_14[a3]._02 + a2];

          if(struct.x_00 * x + struct.y_02 * z + struct._04 < 0) {
            //LAB_800e9604
            v0 = 0;
            break;
          }
        }

        //LAB_800e95e8
        if(v0 != 0) {
          _800cbe48.offset(t0 * 0x4L).setu(a3);
          t0++;
        }
      }

      //LAB_800e95fc
    }

    //LAB_800e960c
    if(t0 == 0) {
      s4 = -1;
    } else if(t0 == 1) {
      s4 = (int)_800cbe48.get();
    } else {
      //LAB_800e962c
      t1 = 0x7fff_ffff;
      t2 = -1;
      if((int)t0 > 0) {
        final SomethingStruct struct = SomethingStructPtr_800d1a88;
        final SVECTOR[] normals = struct.normals_08;

        //LAB_800e965c
        for(int i = 0; i < t0; i++) {
          a2 = (int)_800cbe48.offset(i * 0x4L).get();
          v1 = (-normals[a2].getX() * x - normals[a2].getZ() * z - struct.ptr_14[a2]._08) / normals[a2].getY() - t6;

          if(v1 > 0 && v1 < t1) {
            t2 = a2;
            t1 = v1;
          }

          //LAB_800e96e8
        }
      }

      //LAB_800e96f8
      if(t1 != 0x7fff_ffff) {
        s4 = t2;
      } else {
        //LAB_800e9708
        s4 = -1;
      }

      //LAB_800e970c
    }

    //LAB_800e9710
    if(s4 < 0) {
      s4 = FUN_800e8990(x, z);

      if(_800f7f14.get() != 0 && s4 >= 0 && s4 < SomethingStructPtr_800d1a88.count_0c) {
        v0 = 1;
      } else {
        v0 = 0;
      }

      //LAB_800e975c
      //LAB_800e9764
      sp0x28.set((short)0, (short)0, (short)0);

      if(v0 == 0) {
        //LAB_800e9774
        final SomethingStructSub0c_1 ss2 = SomethingStructPtr_800d1a88.ptr_14[s4];
        final TmdObjTable1c.Primitive primitive = SomethingStructPtr_800d1a88.getPrimitiveForOffset(ss2.primitivesOffset_04);
        final int packetOffset = ss2.primitivesOffset_04 - primitive.offset();
        final int packetIndex = packetOffset / (primitive.width() + 4);
        final int remainder = packetOffset % (primitive.width() + 4);
        final byte[] packet = primitive.data()[packetIndex];

        //LAB_800e97c4
        for(a2 = 0; a2 < SomethingStructPtr_800d1a88.ptr_14[s4].count_00; a2++) {
          sp0x28.add(SomethingStructPtr_800d1a88.verts_04[IoHelper.readUShort(packet, remainder + 2 + a2 * 2)]);
        }

        //LAB_800e9828
        sp0x28.div(SomethingStructPtr_800d1a88.ptr_14[s4].count_00);
      }

      //LAB_800e9870
      playerMovement.setX((short)(sp0x28.getX() - x));
      playerMovement.setZ((short)(sp0x28.getZ() - z));

      final SVECTOR normal = SomethingStructPtr_800d1a88.normals_08[s4];
      playerMovement.setY((short)((-normal.getX() * sp0x28.getX() - normal.getZ() * sp0x28.getZ() - SomethingStructPtr_800d1a88.ptr_14[s4]._08) / normal.getY()));
    } else {
      //LAB_800e990c
      t6 = y - 20;
      t0 = 0;

      //LAB_800e992c
      for(a3 = 0; a3 < SomethingStructPtr_800d1a88.count_0c; a3++) {
        if(SomethingStructPtr_800d1a88.ptr_14[a3].bool_01) {
          //LAB_800e9988
          v0 = 1;
          for(a2 = 0; a2 < SomethingStructPtr_800d1a88.ptr_14[a3].count_00; a2++) {
            final SomethingStructSub0c_2 struct = SomethingStructPtr_800d1a88.ptr_18[SomethingStructPtr_800d1a88.ptr_14[a3]._02 + a2];
            if(struct.x_00 * s6 + struct.y_02 * s5 + struct._04 < 0) {
              //LAB_800e99f4
              v0 = 0;
              break;
            }
          }

          //LAB_800e99d8
          if(v0 != 0) {
            _800cbe48.offset(t0 * 0x4L).setu(a3);
            t0++;
          }
        }

        //LAB_800e99ec
      }

      //LAB_800e99fc
      if(t0 != 0) {
        if(t0 != 1) {
          //LAB_800e9a1c
          t1 = 0x7fff_ffff;
          t2 = -1;

          //LAB_800e9a4c
          for(a3 = 0; a3 < t0; a3++) {
            a2 = (int)_800cbe48.offset(a3 * 0x4L).get();

            final SVECTOR normal = SomethingStructPtr_800d1a88.normals_08[a2];

            v1 = (-normal.getX() * s6 - normal.getZ() * s5 - SomethingStructPtr_800d1a88.ptr_14[a2]._08) / normal.getY() - t6;
            if(v1 > 0 && v1 < t1) {
              t2 = a2;
              t1 = v1;
            }

            //LAB_800e9ad4
          }

          //LAB_800e9ae4
          if(t1 != 0x7fff_ffff) {
            v1 = t2;
          } else {
            //LAB_800e9af4
            v1 = -1;
          }
        } else {
          v1 = (int)_800cbe48.get();
        }

        //LAB_800e9af8
        s3 = v1;
      } else {
        s3 = -1;
      }

      //LAB_800e9afc
      v0 = -1;
      if(s3 >= 0) {
        final SomethingStructSub0c_1 struct = SomethingStructPtr_800d1a88.ptr_14[s3];

        //LAB_800e9b50
        for(s1 = 0; s1 < struct.count_00; s1++) {
          final SomethingStructSub0c_2 struct2 = SomethingStructPtr_800d1a88.ptr_18[struct._02 + s1];
          if(struct2._08 != 0) {
            if(Math.abs(struct2.x_00 * s6 + struct2.y_02 * s5 + struct2._04 >> 10) < 10) {
              v0 = s1;
              break;
            }
          }
        }
      }

      //LAB_800e9bbc
      //LAB_800e9bc0
      if(s3 >= 0 && v0 < 0) {
        final SVECTOR normal = SomethingStructPtr_800d1a88.normals_08[s3];
        final SomethingStructSub0c_1 struct = SomethingStructPtr_800d1a88.ptr_14[s3];

        if(Math.abs(y - (-normal.getX() * s6 - normal.getZ() * s5 - struct._08) / normal.getY()) < 50) {
          //LAB_800e9e64
          playerMovement.setY((short)((-normal.getX() * (x + playerMovement.getX()) - normal.getZ() * (z + playerMovement.getZ()) - struct._08) / normal.getY()));

          //LAB_800ea390
          if(!_800d1a8c._00) {
            _800d1a8c._00 = true;
            //LAB_800ea3b4
            _800d1a84.setu(ratan2(playerMovement.getX(), playerMovement.getZ()) + 0x800 & 0xfff);
          }

          //LAB_800ea3e0
          return s3;
        }
      }

      //LAB_800e9c58
      if((getCollisionAndTransitionInfo(s4) & 0x20) != 0) {
        return -1;
      }

      t1 = SomethingStructPtr_800d1a88.ptr_14[s4].count_00;

      //LAB_800e9ca0
      a1 = -1;
      for(a2 = 1; a2 < 4; a2++) {
        t0 = x + playerMovement.getX() * a2;
        a3 = z + playerMovement.getZ() * a2;

        //LAB_800e9ce8
        for(int a1_0 = 0; a1_0 < t1; a1_0++) {
          final SomethingStructSub0c_2 struct = SomethingStructPtr_800d1a88.ptr_18[SomethingStructPtr_800d1a88.ptr_14[s4]._02 + a1_0];

          if(struct._08 != 0) {
            if(struct.x_00 * t0 + struct.y_02 * a3 + struct._04 >> 10 <= 0) {
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
        final SomethingStructSub0c_2 struct = SomethingStructPtr_800d1a88.ptr_18[SomethingStructPtr_800d1a88.ptr_14[s2]._02 + a1];
        s3 = ratan2(s5 - z, s6 - x);
        s0 = ratan2(-struct.x_00, struct.y_02);
        v1 = Math.abs(s3 - s0);
        if(v1 > 0x800) {
          v1 = 0x1000 - v1;
        }

        //LAB_800e9f38
        _800cbe68.setu(0);
        if((v1 - 0x341 & 0xffff_ffffL) > 0x17e) { // Unsigned comparison
          if(v1 > 0x400) {
            _800cbe68.setu(0x1L);
            if(s0 > 0) {
              s0 = s0 - 0x800;
            } else {
              //LAB_800e9f6c
              s0 = s0 + 0x800;
            }
          }

          //LAB_800e9f70
          sp38 = s0;

          if(!_800cbe38._00) {
            _800cbe38._00 = true;
          }

          v1 = s0 - s3;

          //LAB_800e9f98
          if(v1 < 0x400 || v1 < -0x800) {
            //LAB_800e9fb4
            v0 = 1;
          } else {
            v0 = 0;
          }
        } else {
          v0 = -1;
        }

        //LAB_800e9fbc
        if(v0 >= 0) {
          if(v0 == 0) {
            s3 = -0x40;
          } else {
            s3 = 0x40;
          }

          //LAB_800e9fd0

          sp38 = sp38 - s3;

          //LAB_800e9ff4
          s1 = 8;
          do {
            sp38 = sp38 + s3;
            s0 = x + (rcos(sp38) * (int)_800cbe30.get() >> 12);
            t5 = z + (rsin(sp38) * (int)_800cbe30.get() >> 12);

            s1--;
            if(s1 <= 0) {
              break;
            }

            t6 = y - 20;
            t0 = 0;
            final SomethingStruct t1_0 = SomethingStructPtr_800d1a88;

            //LAB_800ea064
            for(a2 = 0; a2 < t1_0.count_0c; a2++) {
              final SomethingStructSub0c_1 a1_0 = t1_0.ptr_14[a2];

              if(a1_0.bool_01) {
                //LAB_800ea0c4
                v0 = 1;
                for(a3 = 0; a3 < a1_0.count_00; a3++) {
                  final SomethingStructSub0c_2 a0_0 = t1_0.ptr_18[a1_0._02 + a3];
                  if((a0_0.x_00 * s0 + a0_0.y_02 * t5 + a0_0._04) < 0) {
                    //LAB_800ea130
                    v0 = 0;
                    break;
                  }
                }

                //LAB_800ea114
                if(v0 != 0) {
                  _800cbe48.offset(t0 * 0x4L).setu(a2);
                  t0 = t0 + 0x1L;
                }
              }

              //LAB_800ea128
            }

            //LAB_800ea138
            if(t0 != 0) {
              if(t0 == 1) {
                a0 = (int)_800cbe48.get();
              } else {
                //LAB_800ea158
                t1 = 0x7fff_ffff;
                t2 = -1;

                final SomethingStruct v0_0 = SomethingStructPtr_800d1a88;

                //LAB_800ea17c
                for(a2 = 0; a2 < (int)t0; a2++) {
                  a3 = (int)_800cbe48.offset(a2 * 0x4L).get();

                  final SVECTOR normal = v0_0.normals_08[a3];

                  v1 = (-normal.getX() * s0 - normal.getZ() * t5 - v0_0.ptr_14[a3]._08) / normal.getY() - t6;
                  if(v1 > 0 && v1 < t1) {
                    t2 = a3;
                    t1 = v1;
                  }

                  //LAB_800ea204
                }

                //LAB_800ea214
                if(t1 != 0x7fff_ffff) {
                  a0 = t2;
                } else {
                  //LAB_800ea224
                  a0 = -1;
                }
              }

              //LAB_800ea228
              s2 = a0;
            } else {
              s2 = -1;
            }

            //LAB_800ea22c
          } while(s2 < 0);

          //LAB_800ea234
          s3 = s2;
          if(s2 >= 0) {
            sp30 = s0;
            sp34 = t5;
          }
        } else {
          s3 = -1;
        }

        //LAB_800ea254
        if(s3 < 0) {
          return -1;
        }

        final SVECTOR normal = SomethingStructPtr_800d1a88.normals_08[s3];

        if(Math.abs(y - (-normal.getX() * sp30 - normal.getZ() * sp34 - SomethingStructPtr_800d1a88.ptr_14[s3]._08) / normal.getY()) >= 50) {
          return -1;
        }

        playerMovement.setY((short)((-normal.getX() * sp30 - normal.getZ() * sp34 - SomethingStructPtr_800d1a88.ptr_14[s3]._08) / normal.getY()));
        playerMovement.setX((short)(sp30 - x));
        playerMovement.setZ((short)(sp34 - z));
      } else {
        if(s3 < 0) {
          return -1;
        }

        final SVECTOR normal = SomethingStructPtr_800d1a88.normals_08[s3];

        if(Math.abs(y - (-normal.getX() * s6 - normal.getZ() * s5 - SomethingStructPtr_800d1a88.ptr_14[s3]._08) / normal.getY()) >= 50) {
          return -1;
        }

        //LAB_800e9df4
        final SomethingStructSub0c_1 struct = SomethingStructPtr_800d1a88.ptr_14[s3];

        //LAB_800e9e64
        playerMovement.setY((short)((-normal.getX() * (x + playerMovement.getX()) - normal.getZ() * (z + playerMovement.getZ()) - struct._08) / normal.getY()));
      }
    }

    //LAB_800ea390
    if(!_800d1a8c._00) {
      _800d1a8c._00 = true;
      //LAB_800ea3b4
      _800d1a84.setu(ratan2(playerMovement.getX(), playerMovement.getZ()) + 0x800 & 0xfff);
    }

    //LAB_800ea3e0
    return s3;
  }

  @Method(0x800ea4c8L)
  public static short FUN_800ea4c8(final int a0) {
    _800d1a78.subu(0x1L);

    if((int)_800d1a78.get() > 0) {
      _800d1a84.setu(_800d1a80);

      if(!_800d1a8c._00) {
        _800d1a8c._00 = true;
      }
    }

    //LAB_800ea534
    //LAB_800ea538
    final boolean bool;
    if((int)_800c6ae0.get() <= 0x400) {
      bool = true;
    } else if(_800d1a8c._00) {
      bool = false;
    } else {
      bool = true;
      _800d1a8c._00 = true;
    }

    //LAB_800ea570
    if(bool || _800d1a7c.get() != 0) {
      //LAB_800ea6d0
      //LAB_800ea6d4
      _800d1a7c.setu(0);
      return (short)a0;
    }

    final int s1 = (int)((_800c6ae0.get() - 1) % 4);
    final int s2 = (int)(_800c6ae0.get() % 4);
    int s0 = (int)(_800f7f6c.offset(s1 * 0x2L).getSigned() - _800d1a84.get());

    if(Math.abs(s0) > 0x800) {
      _800cbda4.setu(s0 > 0 ? 1 : 0);
      s0 = 0x1000 - Math.abs(s0);
    } else {
      //LAB_800ea628
      _800cbda4.setu(s0 < 1 ? 1 : 0);
      s0 = Math.abs(s0);
    }

    //LAB_800ea63c
    if(s0 > 0x200 || (int)_800d1a78.get() > 0) {
      s0 = s0 / 4;
    }

    //LAB_800ea66c
    final int v1 = (int)_800f7f6c.offset(s1 * 0x2L).getSigned();

    final int v0;
    if(_800cbda4.get() == 0) {
      v0 = v1 - s0;
    } else {
      //LAB_800ea6a0
      v0 = v1 + s0;
    }

    //LAB_800ea6a4
    _800f7f6c.offset(s2 * 0x2L).setu(v0);

    //LAB_800ea6dc
    return (short)v0;
  }

  @Method(0x800ea84cL)
  public static void FUN_800ea84c(final MediumStruct a0) {
    if(isScriptLoaded(0)) {
      if(a0._44) {
        index_80052c38.set(sobjs_800c6880[0].innerStruct_00.ui_16c);

        //LAB_800ea8d4
        for(int i = 0; i < a0.count_40; i++) {
          if(index_80052c38.get() == a0.arr_00[i]) {
            a0._44 = false;
          }

          //LAB_800ea8ec
        }
      }
    }

    //LAB_800ea8fc
  }

  @Method(0x800ea90cL)
  public static void FUN_800ea90c(final MediumStruct a0) {
    if(isScriptLoaded(0)) {
      index_80052c38.set(sobjs_800c6880[0].innerStruct_00.ui_16c);
    }
  }

  @Method(0x800ea96cL)
  public static void FUN_800ea96c(final MediumStruct a0) {
    // no-op
  }

  @Method(0x800ea974L)
  public static MediumStruct FUN_800ea974(final long a0) {
    if((int)a0 < 0) {
      _800d1a90.callback_48 = SMap::FUN_800ea96c;
    } else {
      //LAB_800ea9a4
      _800d1a90.clear();

      final long a3 = _800f7f74.getAddress();
      final MediumStruct a2 = _800d1a90;

      //LAB_800ea9d8
      for(int i = 0; i < _800f9374.get(); i++) {
        if(a0 != 0) {
          if(MEMORY.ref(2, a3).offset(i * 0x14L).offset(0x4L).get() == a0) {
            a2.arr_00[a2.count_40] = (int)MEMORY.ref(2, a3).offset(i * 0x14L).offset(0x6L).get();
            a2.count_40++;
          }
        }

        //LAB_800eaa20
      }

      //LAB_800eaa30
      if(_800d1a90.count_40 != 0) {
        _800d1a90.callback_48 = SMap::FUN_800ea84c;
        _800d1a90._44 = true;
      } else {
        //LAB_800eaa5c
        _800d1a90.callback_48 = SMap::FUN_800ea90c;
      }

      //LAB_800eaa6c
    }

    //LAB_800eaa74
    return _800d1a90;
  }

  @Method(0x800eaa88L)
  public static void theEnd() {
    theEndStates_800f9378[pregameLoadingStage_800bb10c.get()].run();
  }

  @Method(0x800eaad4L)
  public static void initCredits() {
    resizeDisplay(384, 240);
    vsyncMode_8007a3b8 = 2;

    //LAB_800eab00
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

      //LAB_800eab1c
      credit.colour_00.set(0x80, 0x80, 0x80);
      credit.scroll_12.set(0);
      credit._14.set(0);
      credit.state_16.set(0);
    }

    //LAB_800eac18
    //LAB_800eac20
    credits_800d1af8.get(0).prevCreditSlot_04.set(15);
    for(int i = 1; i < 16; i++) {
      //LAB_800eac3c
      final CreditStruct1c credit = credits_800d1af8.get(i);
      credit.prevCreditSlot_04.set(i - 1);
    }

    //LAB_800eac84
    fadeOutTicks_800d1ae4.set(0);
    creditsPassed_800d1aec.set(0);
    creditIndex_800d1af0.set(0);
    pregameLoadingStage_800bb10c.set(1);
  }

  @Method(0x800eacc8L)
  public static void creditsLoaded(final List<FileData> files) {
    creditTims_800d1ae0 = files;
    creditTimsLoaded_800d1ae8.set(true);
  }

  @Method(0x800eacfcL)
  public static void loadCredits() {
    creditTimsLoaded_800d1ae8.set(false);
    loadDrgnDir(0, 5720, SMap::creditsLoaded);
    pregameLoadingStage_800bb10c.set(2);
  }

  @Method(0x800ead58L)
  public static void waitForCreditsToLoadAndPlaySong() {
    if(creditTimsLoaded_800d1ae8.get()) {
      //LAB_800ead7c
      playXaAudio(3, 3, 1);
      pregameLoadingStage_800bb10c.set(3);
    }

    //LAB_800ead9c
  }

  @Method(0x800eadacL)
  public static void fadeInCredits() {
    if(_800bf0cf.get() == 4) {
      //LAB_800eadd0
      scriptStartEffect(2, 15);
      pregameLoadingStage_800bb10c.set(4);
    }

    //LAB_800eadec
  }

  @Method(0x800eadfcL)
  public static void renderCredits() {
    renderCreditsGradient();

    if(loadAndRenderCredits() != 0) {
      pregameLoadingStage_800bb10c.set(5);
    }

    //LAB_800eae28
  }

  @Method(0x800eae38L)
  public static void fadeOutCredits() {
    scriptStartEffect(1, 15);
    pregameLoadingStage_800bb10c.set(6);
  }

  @Method(0x800eae6cL)
  public static void waitForCreditsFadeOut() {
    fadeOutTicks_800d1ae4.incr();

    if(fadeOutTicks_800d1ae4.get() >= 16) {
      //LAB_800eaea0
      pregameLoadingStage_800bb10c.set(7);
    }

    //LAB_800eaeac
  }

  @Method(0x800eaeb8L)
  public static void deallocateCreditsAndReturnToMenu() {
    //LAB_800eaedc
    creditTims_800d1ae0 = null;
    engineStateOnceLoaded_8004dd24 = EngineState.SUBMAP_05;
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8 = 2;

    //LAB_800eaf14
  }

  @Method(0x800eaf24L)
  public static void renderCreditsGradient() {
    GPU.queueCommand(10, new GpuCommandPoly(4)
      .translucent(Translucency.B_MINUS_F)
      .monochrome(0, 0xff)
      .monochrome(1, 0xff)
      .monochrome(2, 0)
      .monochrome(3, 0)
      .pos(0, -192, -120)
      .pos(1, 192, -120)
      .pos(2, -192, -64)
      .pos(3, 192, -64)
    );

    GPU.queueCommand(10, new GpuCommandPoly(4)
      .translucent(Translucency.B_MINUS_F)
      .monochrome(0, 0)
      .monochrome(1, 0)
      .monochrome(2, 0xff)
      .monochrome(3, 0xff)
      .pos(0, -192, 64)
      .pos(1, 192, 64)
      .pos(2, -192, 120)
      .pos(3, 192, 120)
    );
  }

  @Method(0x800eb304L)
  public static int loadAndRenderCredits() {
    //LAB_800eb318
    //LAB_800ebc0c
    for(int creditSlot = 0; creditSlot < 16; creditSlot++) {
      //LAB_800eb334
      if(creditsPassed_800d1aec.get() >= 357) {
        return 1;
      }

      final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

      //LAB_800eb358
      final int state = credit.state_16.get();
      if(state == 0) {
        //LAB_800eb3b8
        if(shouldLoadNewCredit(creditSlot) != 0) {
          credit.state_16.set(2);
          loadCreditTims(creditSlot);
        }
      } else if(state == 2) {
        //LAB_800eb408
        moveCredits(creditSlot);

        final int w = credit.width_0e.get() * 4;
        final int h = credit.height_10.get();
        final int x = -w / 2 - 8;
        final int y = credit.y_0c.get();
        final int tpage = texPages_800bb110.get(Bpp.BITS_4).get(Translucency.HALF_B_PLUS_HALF_F).get(TexPageY.Y_0).get() | (creditSlot / 8 * 128 + 512 & 0x3c0) >> 6;
        final int clut = creditSlot << 6 | 0x38;

        //LAB_800eb8e8
        renderQuad(
          tpage, clut,
          credit.colour_00.getR(), credit.colour_00.getG(), credit.colour_00.getB(),
          0, creditSlot % 8 * 64,
          w, h,
          x, y,
          w, h,
          orderingTableSize_1f8003c8.get() - 3,
          0
        );

        //LAB_800eba4c
        credit.scroll_12.incr();
        //LAB_800eb3a4
      } else if(state == 3) {
        //LAB_800ebabc
        if(credits_800d1af8.get((creditSlot + 1) % 16).state_16.get() != 0) {
          credit.scroll_12.set(0);
          credit._14.set(0);
          credit.state_16.set(0);
        } else {
          //LAB_800ebb84
          credit.scroll_12.incr();
        }
      }
    }

    //LAB_800ebc18
    return 0;
  }

  @Method(0x800ebc2cL)
  public static int shouldLoadNewCredit(final int creditSlot) {
    if(creditIndex_800d1af0.get() >= 357) {
      return 0;
    }

    //LAB_800ebc5c
    boolean sp4 = false;

    //LAB_800ebc64
    int i;
    for(i = 0; i < 357; i++) {
      if(_800f93b0.offset(i * 0x8L).getSigned() == -1) {
        //LAB_800ebca8
        break;
      }

      //LAB_800ebcb0
      if(_800f93b0.offset(i * 0x8L).get() == creditIndex_800d1af0.get()) {
        sp4 = true;
        break;
      }
    }

    final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

    //LAB_800ebd08
    if(sp4) {
      credit.type_08.set((int)_800f93b0.offset((i * 2 + 1) * 0x4L).get());
    } else {
      //LAB_800ebd6c
      credit.type_08.set(2);
    }

    //LAB_800ebd94
    if(creditIndex_800d1af0.get() == 0) {
      return 1;
    }

    //LAB_800ebdb4
    final int prevCreditSlot = credit.prevCreditSlot_04.get();
    final CreditStruct1c prevCredit = credits_800d1af8.get(prevCreditSlot);

    if(prevCredit.state_16.get() == 0) {
      return 0;
    }

    //LAB_800ebe1c
    switch(credit.type_08.get()) {
      case 0 -> {
        final int type = prevCredit.type_08.get();

        if(type == 0 || type == 1 || type == 2) {
          //LAB_800ebee4
          if(prevCredit.scroll_12.get() >= 66) {
            return 1;
          }

          //LAB_800ebf24
        } else if(type == 3) {
          //LAB_800ebf2c
          if(prevCredit.scroll_12.get() >= 64) {
            return 1;
          }

          //LAB_800ebf6c
          //LAB_800ebed0
        } else if(type == 4) {
          //LAB_800ebf74
          if(prevCredit.scroll_12.get() >= 144) {
            return 1;
          }

          //LAB_800ebfb4
        }

        //LAB_800ebfbc
      }

      case 1 -> {
        final int type = prevCredit.type_08.get();

        if(type == 0 || type == 1 || type == 2) {
          //LAB_800ec024
          if(prevCredit.scroll_12.get() >= 36) {
            return 1;
          }

          //LAB_800ec064
        } else if(type == 3) {
          //LAB_800ec06c
          if(prevCredit.scroll_12.get() >= 64) {
            return 1;
          }

          //LAB_800ec0ac
          //LAB_800ec010
        } else if(type == 4) {
          //LAB_800ec0b4
          if(prevCredit.scroll_12.get() >= 144) {
            return 1;
          }

          //LAB_800ec0f4
        }

        //LAB_800ec0fc
      }

      case 2 -> {
        switch(prevCredit.type_08.get()) {
          case 0, 1 -> {
            if(prevCredit.scroll_12.get() >= 27) {
              return 1;
            }
          }

          //LAB_800ec1ac
          case 2 -> {
            if(prevCredit.scroll_12.get() >= 23) {
              return 1;
            }
          }

          //LAB_800ec1f4
          case 3 -> {
            if(prevCredit.scroll_12.get() >= 80) {
              return 1;
            }
          }

          //LAB_800ec23c
          case 4 -> {
            if(prevCredit.scroll_12.get() >= 144) {
              return 1;
            }
          }

          //LAB_800ec284
        }

        //LAB_800ec28c
      }

      case 3 -> {
        if(prevCredit.type_08.get() == 3) {
          return 1;
        }

        //LAB_800ec2d0
        if(prevCredit.scroll_12.get() > 16) {
          return 1;
        }

        //LAB_800ec310
      }

      case 4 -> {
        if(prevCredit.scroll_12.get() >= 130) {
          return 1;
        }

        //LAB_800ec358
      }
    }

    //LAB_800ec360
    //LAB_800ec36c
    return 0;
  }

  @Method(0x800ec37cL)
  public static void moveCredits(final int creditSlot) {
    final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

    final int scroll = credit.scroll_12.get();

    switch(credit.type_08.get()) {
      case 0, 1 -> {
        credit.y_0c.set((short)(136 - scroll));
        credit.colour_00.set(192, 93, 81);

        if(scroll > 304) {
          credit.state_16.set(3);
          creditsPassed_800d1aec.incr();
        }
      }

      //LAB_800ec51c
      case 2 -> {
        credit.y_0c.set((short)(136 - scroll));
        credit.colour_00.set(118, 107, 195);

        if(scroll > 304) {
          credit.state_16.set(3);
          creditsPassed_800d1aec.incr();
        }
      }

      //LAB_800ec620
      case 3 -> {
        final int prevCreditSlot = credit.prevCreditSlot_04.get();
        final CreditStruct1c prevCredit = credits_800d1af8.get(prevCreditSlot);

        if(credit.scroll_12.get() < 64) {
          if(credit.type_08.get() == 3) {
            credit.y_0c.set((short)(-credit.height_10.get() / 2 + 13));
            credit.colour_00.setR(rsin(credit.scroll_12.get() * 16) * 118 >> 12);
            credit.colour_00.setG(rsin(credit.scroll_12.get() * 16) * 107 >> 12);
            credit.colour_00.setB(rsin(credit.scroll_12.get() * 16) * 195 >> 12);
          } else {
            //LAB_800ec89c
            credit.y_0c.set((short)(-credit.height_10.get() / 2 - 13));
            credit.colour_00.setR(rsin(credit.scroll_12.get() * 16) * 192 >> 12);
            credit.colour_00.setG(rsin(credit.scroll_12.get() * 16) * 93 >> 12);
            credit.colour_00.setB(rsin(credit.scroll_12.get() * 16) * 81 >> 12);
          }

          //LAB_800eca68
        } else {
          //LAB_800eca70
          credit._14.incr();

          final int sp14 = credit._14.get();
          if(prevCredit.type_08.get() == 3) {
            credit.y_0c.set((short)(-credit.height_10.get() / 2 - sp14 + 13));
            credit.colour_00.set(118, 107, 195);
          } else {
            //LAB_800ecc2c
            credit.y_0c.set((short)(-credit.height_10.get() / 2 - sp14 - 13));
            credit.colour_00.set(192, 93, 81);
          }

          //LAB_800ecd1c
          if(credit.y_0c.get() < -184) {
            credit.state_16.set(3);
            creditsPassed_800d1aec.incr();
          }
        }
      }

      //LAB_800ecd90
      case 4 -> {
        if(credit.y_0c.get() < -credit.height_10.get() / 2 && scroll != 0) {
          credit._14.add(6);
          final int sp14 = credit._14.get();
          credit.colour_00.setR(rcos(sp14) * 128 >> 12);
          credit.colour_00.setG(rcos(sp14) * 128 >> 12);
          credit.colour_00.setB(rcos(sp14) * 128 >> 12);

          if(sp14 > 0x400) {
            credit.colour_00.set(0, 0, 0);
            credit.state_16.set(3);
            creditsPassed_800d1aec.incr();
          }

          //LAB_800ecff8
        } else {
          //LAB_800ed000
          credit.y_0c.set((short)(136 - scroll));
          credit.colour_00.set(0x80, 0x80, 0x80);
        }
      }

      //LAB_800ed0a8
    }

    //LAB_800ed0b0
  }

  @Method(0x800ed0c4L)
  public static void loadCreditTims(final int creditSlot) {
    if(creditIndex_800d1af0.get() < creditTims_800d1ae0.size()) {
      //LAB_800ed100
      if(creditTims_800d1ae0.get(creditIndex_800d1af0.get()).size() != 0) {
        //LAB_800ed138
        loadCreditTim(creditSlot);
      }
    }

    //LAB_800ed150
  }

  @Method(0x800ed160L)
  public static void loadCreditTim(final int creditSlot) {
    final Tim tim = new Tim(creditTims_800d1ae0.get(creditIndex_800d1af0.get()));

    creditIndex_800d1af0.incr();

    if(creditIndex_800d1af0.get() > 357) {
      creditIndex_800d1af0.set(357);
    }

    final CreditStruct1c credit = credits_800d1af8.get(creditSlot);

    //LAB_800ed1f8
    final RECT imageRect = tim.getImageRect();

    final RECT rect = new RECT(
      creditPos_800f9670.get(creditSlot).getX(),
      creditPos_800f9670.get(creditSlot).getY(),
      imageRect.w.get(),
      imageRect.h.get()
    );

    credit.width_0e.set(imageRect.w.get());
    credit.height_10.set(imageRect.h.get());

    LoadImage(rect, tim.getImageData());

    if(tim.hasClut()) {
      LoadImage(new RECT((short)896, (short)creditSlot, (short)16, (short)1), tim.getClutData());
    }

    //LAB_800ed32c
  }

  @Method(0x800ed3b0L)
  public static void renderQuad(final int tpage, final int clut, final int r, final int g, final int b, final int u, final int v, final int tw, final int th, final int x, final int y, final int w, final int h, final int z, final int translucent) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .bpp(Bpp.of(tpage >>> 7 & 0b11))
      .clut((clut & 0b111111) * 16, clut >>> 6)
      .vramPos((tpage & 0b1111) * 64, (tpage & 0x10000) != 0 ? 256 : 0)
      .rgb(r, g, b)
      .pos(0, x, y)
      .pos(1, x + w, y)
      .pos(2, x, y + h)
      .pos(3, x + w, y + h)
      .uv(0, u, v)
      .uv(1, u + tw, v)
      .uv(2, u, v + th)
      .uv(3, u + tw, v + th);

    if(translucent != 0) {
      cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
    }

    GPU.queueCommand(z, cmd);
  }

  @Method(0x800ed5b0L)
  public static void startFmvLoadingStage() {
    throw new RuntimeException("No longer used");
  }

  @Method(0x800eddb4L)
  public static void FUN_800eddb4() {
    if(_800c6870.getSigned() == -1) {
      _800f9e5a.setu(-1);
    }

    //LAB_800ede14
    switch((short)(_800f9e5a.get() + 1)) {
      case 0x0 -> {
        _800d4bd0 = null;
        _800d4bd4 = null;

        //LAB_800ee1b8
        submapCutModel = null;
        submapCutAnim = null;

        _800f9e5a.setu(0);

        //LAB_800ee1e4
      }

      case 0x1 -> {
        _800d4bd0 = null;
        _800d4bd4 = null;

        if(submapCut_80052c30.get() == 673) { // End cutscene
          _800d4bd0 = new Structb0();
          _800d4bd4 = new FileData(new byte[0x20]);

          _800d4bd0._00 = 0;
          _800d4bd0._02 = 0;
          _800d4bd0._04 = 0;
          _800d4bd0._06 = 0;
          _800d4bd0._08 = 0;
          _800d4bd0._0c = 0;
        }

        //LAB_800edeb4
        assert submapCutModel == null;
        assert submapCutAnim == null;
        assert submapCutTexture == null;
        assert submapCutMatrix == null;

        submapCutModelAndAnimLoaded_800d4bdc.set(false);
        submapTextureAndMatrixLoaded_800d4be0.set(false);
        theEndTimLoaded_800d4be4.set(false);
        theEndTim_800d4bf0 = null;

        final int fileIndex = smapFileIndices_800f982c.get(submapCut_80052c30.get()).get();
        if(fileIndex != 0) {
          // File example: 7508
          loadDrgnDir(0, fileIndex, files -> {
            submapCutModelAndAnimLoaded_800d4bdc.set(true);

            submapCutModel = new CContainer("DRGN0/" + fileIndex, files.get(0));
            submapCutAnim = new TmdAnimationFile(files.get(1));
          });

          loadDrgnDir(0, fileIndex + 1, files -> {
            submapTextureAndMatrixLoaded_800d4be0.set(true);

            submapCutTexture = new Tim(files.get(0));
            submapCutMatrix = new MATRIX();

            final FileData matrixData = files.get(1);

            for(int i = 0; i < 9; i++) {
              submapCutMatrix.set(i, matrixData.readShort(i * 2));
            }

            for(int i = 0; i < 3; i++) {
              submapCutMatrix.transfer.component(i).set(matrixData.readShort(18 + i * 2));
            }
          });

          if(submapCut_80052c30.get() == 673) { // End cutscene, loads "The End" TIM
            loadDrgnFile(0, 7610, data -> {
              theEndTimLoaded_800d4be4.set(true);
              theEndTim_800d4bf0 = new Tim(data);
            });
          }
        }

        _800f9e5a.addu(0x1L);
      }

      case 0x2 -> {
        if(submapCutModelAndAnimLoaded_800d4bdc.get() && submapTextureAndMatrixLoaded_800d4be0.get()) {
          GPU.uploadData(new RECT((short)1008, (short)256, submapCutTexture.getImageRect().w.get(), submapCutTexture.getImageRect().h.get()), submapCutTexture.getImageData());
          matrix_800d4bb0.set(submapCutMatrix);

          _800f9e5a.addu(0x1L);
          _800c686c.setu(0x1L);

          submapCutTexture = null;
          submapCutMatrix = null;
        }
      }

      case 0x3 -> {
        if(submapCut_80052c30.get() == 673) { // End cutscene
          if(!theEndTimLoaded_800d4be4.get()) {
            break;
          }

          FUN_800f4244(theEndTim_800d4bf0, tpage_800f9e5c, clut_800f9e5e, Translucency.B_PLUS_F);
          StoreImage(_800d6b48, _800d4bd4);
        }

        _800f9e5a.addu(0x1L);
      }

      case 0x4 -> {
        submapModel_800d4bf8.colourMap_9d = 0x91;

        initModel(submapModel_800d4bf8, submapCutModel, submapCutAnim);

        if(submapCut_80052c30.get() == 673) { // End cutscene
          FUN_800eef6c(_800d6b48, _800d4bd4, _800d4bd0);
        }

        //LAB_800ee10c
        //LAB_800ee110
        _800f9e5a.addu(0x1L);
      }

      case 0x5 -> {
        FUN_800eece0(matrix_800d4bb0);

        if(_800d4bd0 != null && _800d4bd4 != null) {
          FUN_800ee9e0(_800d6b48, _800d4bd4, _800d4bd0, tpage_800f9e5c, clut_800f9e5e);
          LoadImage(_800d6b48, _800d4bd4);
        }
      }
    }

    //caseD_6
  }

  /** Used in Snow Field (disk 3) */
  @Method(0x800ee20cL)
  public static void handleSnow() {
    if(_800f9eac.get() == -1) {
      snowLoadingStage_800f9e64.set(-1);
    }

    //LAB_800ee234
    switch(snowLoadingStage_800f9e64.get()) {
      case 0 -> {
        snow_800d4bd8 = new SnowEffect();
        snowLoadingStage_800f9e64.incr();
      }

      case 1 -> {
        if(allocateSnowEffect(snow_800d4bd8)) {
          //LAB_800ee2fc
          snowLoadingStage_800f9e64.incr();
        }
      }

      case 2 -> {
        SnowEffect snow = snow_800d4bd8.next_38;

        //LAB_800ee2d8
        int count;
        for(count = 0; snow != null; count++) {
          initSnowEffect(snow);
          snow = snow.next_38;
        }

        //LAB_800ee2f0
        if(count >= 256) {
          //LAB_800ee2fc
          snowLoadingStage_800f9e64.incr();
        }
      }

      case 3 -> renderSnowEffect(snow_800d4bd8);

      case -1 -> {
        if(_800f9e60.get() != 0) {
          deallocateSnowEffect(snow_800d4bd8);
        }

        //LAB_800ee348
        _800f9e60.set((short)0);
        snowLoadingStage_800f9e64.set(0);
      }
    }

    //LAB_800ee354
  }

  @Method(0x800ee368L)
  public static void renderSnowEffect(final SnowEffect root) {
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
          wrapAroundSnowEffect(snow);
        }
      }

      //LAB_800ee534
      snow = snow.next_38;
    }

    //LAB_800ee544
  }

  @Method(0x800ee558L)
  public static void initSnowEffect(final SnowEffect snow) {
    snow._00 = 1;
    snow.x_16 = (short)(rand() % 384 - 192 + _800f9e6a.get());
    snow.y_18 = (short)(rand() % 240 - 120);

    int a0 = _800d4d30.get();
    if(a0 == 0) {
      snow.xStep_1c = 0;
    } else {
      //LAB_800ee62c
      snow.xStep_1c = 0x20_0000 / (a0 - (simpleRand() * a0 / 2 >> 16));
    }

    //LAB_800ee644
    snow.xAccumulator_24 = snow.x_16 << 16;

    int s2 = 0;
    a0 = _800f9e68.get();
    if(a0 < 35) {
      snow.size_14 = 3;
      //LAB_800ee66c
    } else if(a0 < 150) {
      snow.size_14 = 2;
      s2 = _800d4d34.get() * 0x5555 >> 16;
      //LAB_800ee6ac
    } else if(a0 < 256) {
      snow.size_14 = 1;
      s2 = _800d4d34.get() * 0x5555 >> 15;
    }

    //LAB_800ee6e8
    final long s1 = _800d4d20.getAddress();
    snow.colour_34 = 0xff;
    snow.yAccumulator_28 = snow.y_18 << 16;
    snow.yStep_20 = 0x20_0000 / ((int)MEMORY.ref(4, s1).offset(0x14L).get() + s2);
    snow._10 = (int)MEMORY.ref(4, s1).offset(0xcL).get();
    final int angle = simpleRand() << 11 >> 16;
    MEMORY.ref(4, s1).offset(0x4L).setu(angle);
    snow.angle_08 = angle;

    if(MEMORY.ref(4, s1).offset(0x8L).get() == 0) {
      snow.angleStep_0c = 0;
    } else {
      //LAB_800ee750
      snow.angleStep_0c = simpleRand() * (int)MEMORY.ref(4, s1).offset(0x8L).get() >> 16;
    }

    //LAB_800ee770
    _800f9e68.incr().and(0xff);
    _800f9e6a.incr().and(0x0f);
  }

  /** Reuse snow effect when it reaches the bottom of the screen */
  @Method(0x800ee7b0L)
  public static void wrapAroundSnowEffect(final SnowEffect snow) {
    snow._00 = 0;
    snow.x_16 = (short)(rand() % 384 - 192 + _800f9e6e.get());
    snow.y_18 = (short)-120;

    final int a0 = _800d4d30.get();
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

    final int v1 = _800f9e6c.get();
    int s2 = 0;
    if(v1 == 0 || v1 == 2 || v1 == 4) {
      //LAB_800ee890
      snow.size_14 = 1;
      s2 = _800d4d34.get() * 0x5555 >> 15;
      //LAB_800ee8c0
    } else if(v1 == 1) {
      snow.size_14 = 2;
      s2 = _800d4d34.get() * 0x5555 >> 16;
      //LAB_800ee8f4
    } else if(v1 == 3) {
      snow.size_14 = 3;
    }

    //LAB_800ee900
    //LAB_800ee904
    final long s1 = _800d4d20.getAddress();
    snow.yStep_20 = 0x20_0000 / ((int)MEMORY.ref(4, s1).offset(0x14L).get() + s2);
    snow._10 = (int)MEMORY.ref(4, s1).offset(0xcL).get();
    final int v0 = simpleRand() << 11 >> 16;
    MEMORY.ref(4, s1).offset(0x4L).setu(v0);
    snow.angle_08 = v0;

    if(MEMORY.ref(4, s1).offset(0x8L).get() == 0) {
      snow.angleStep_0c = 0;
    } else {
      //LAB_800ee968
      snow.angleStep_0c = simpleRand() * (int)MEMORY.ref(4, s1).offset(0x8L).get() >> 16;
    }

    //LAB_800ee988
    _800f9e6c.incr();
    if(_800f9e6c.get() >= 6) {
      _800f9e6c.set((short)0);
    }

    //LAB_800ee9b4
    _800f9e6e.incr().and(0xf);
  }

  @Method(0x800ee9e0L)
  public static void FUN_800ee9e0(final RECT s0, final FileData a1, final Structb0 a2, final UnsignedShortRef tpage, final UnsignedShortRef clut) {
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
        .vramPos((tpage.get() & 0b1111) * 64, (tpage.get() & 0b10000) != 0 ? 256 : 0)
        .clut((clut.get() & 0b111111) * 16, clut.get() >>> 6)
        .monochrome(a2._0c >> 16)
        .translucent(Translucency.of(tpage.get() >>> 5 & 0b11))
        .pos(-188, 18, 192, 72)
        .uv(0, 128)
      );
    }

    //LAB_800eeb78
    if(a2._06 != 0) {
      FUN_800eec10(s0, a1, a2, tpage);

      if(a2._08 == 561) {
        a2._06 = 0;
      }
    }

    //LAB_800eeba8
    //LAB_800eebac
    a2._08++;
  }

  @Method(0x800eec10L)
  public static void FUN_800eec10(final RECT a0, final FileData a1, final Structb0 a2, final UnsignedShortRef tpage) {
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
  public static void FUN_800eece0(final MATRIX matrix) {
    submapModel_800d4bf8.coord2_14.coord.transfer.setX(0);
    submapModel_800d4bf8.coord2_14.coord.transfer.setY(0);
    submapModel_800d4bf8.coord2_14.coord.transfer.setZ(0);

    submapModel_800d4bf8.coord2Param_64.rotate.zero();

    applyModelRotationAndScale(submapModel_800d4bf8);
    animateModel(submapModel_800d4bf8);
    FUN_800eee48(submapModel_800d4bf8, matrix);
  }

  @Method(0x800eed44L)
  public static SMapStruct3c FUN_800eed44(final SMapStruct3c a0) {
    final SMapStruct3c v0 = new SMapStruct3c();
    v0.parent_38 = a0.parent_38;
    a0.parent_38 = v0;
    return v0;
  }

  @Method(0x800eed84L)
  public static void FUN_800eed84(final SMapStruct3c a0) {
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
  public static void FUN_800eee48(final Model124 model, final MATRIX matrix) {
    zOffset_1f8003e8.set(model.zOffset_a0);
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);

    final MATRIX lw = new MATRIX();

    //LAB_800eee94
    for(int i = 0; i < model.ObjTable_0c.nobj; i++) {
      GsGetLw(model.ObjTable_0c.top[0].coord2_04, lw);
      GsSetLightMatrix(lw);

      PushMatrix();
      GTE.setRotationMatrix(matrix);
      GTE.setTranslationVector(matrix.transfer);
      renderDobj2(model.ObjTable_0c.top[i]);
      PopMatrix();
    }

    //LAB_800eef0c
  }

  @Method(0x800eef6cL)
  public static void FUN_800eef6c(final RECT imageRect, final FileData imageAddress, final Structb0 a2) {
    //LAB_800eef94
    for(int i = 0; i < 16; i++) {
      //LAB_800eefac
      a2._90[i] = imageAddress.readUShort(i * 0x2) & 0x1f;
      a2._10[i] = (a2._90[i] << 16) / 60;
      a2._50[i] = 0;
      imageAddress.writeShort(i * 0x2, 0x8000);
    }

    LoadImage(imageRect, imageAddress);
  }

  @Method(0x800ef034L)
  public static boolean allocateSnowEffect(final SnowEffect root) {
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
  public static void deallocateSnowEffect(final SnowEffect a0) {
    SnowEffect s0 = a0.next_38;

    //LAB_800ef0b4
    while(s0 != null) {
      s0 = s0.next_38;
      a0.next_38 = s0;
    }
  }

  @Method(0x800ef0f8L)
  public static void FUN_800ef0f8(final Model124 model, final BigSubStruct a1) {
    if(a1._1e.getX() != model.coord2_14.coord.transfer.getX() || a1._1e.getY() != model.coord2_14.coord.transfer.getY() || a1._1e.getZ() != model.coord2_14.coord.transfer.getZ()) {
      //LAB_800ef154
      if(a1._04 != 0) {
        if(a1._00 % a1._30 == 0) {
          final Struct20 a0 = FUN_800f03c0(_800d4ec0);
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
          final DustRenderData54 dust = addDust(dust_800d4e68);

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
          dust.x_18 = screenOffsetX_800cb568.get();
          dust.y_1c = screenOffsetY_800cb56c.get();
          dust._04 = 0;
          dust._06 = 150;

          final MATRIX ls = new MATRIX();
          GsGetLs(model.coord2_14, ls);
          GTE.setRotationMatrix(ls);
          GTE.setTranslationVector(ls.transfer);

          final int type = dust.textureIndex_02;
          if(type == 0) {
            //LAB_800ef4b4
            dust.z_4c = RotTransPers4(_800d6b7c.get( 4), _800d6b7c.get( 5), _800d6b7c.get( 6), _800d6b7c.get( 7), dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);
          } else if(type == 1) {
            //LAB_800ef484
            //LAB_800ef4b4
            dust.z_4c = RotTransPers4(_800d6b7c.get( 8), _800d6b7c.get( 9), _800d6b7c.get(10), _800d6b7c.get(11), dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);
          } else if(type == 3) {
            //LAB_800ef4a0
            //LAB_800ef4b4
            dust.z_4c = RotTransPers4(_800d6b7c.get( 0), _800d6b7c.get( 1), _800d6b7c.get( 2), _800d6b7c.get( 3), dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);
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
          final DustRenderData54 dust = addDust(dust_800d4e68);
          dust.renderMode_00 = 1;
          dust.textureIndex_02 = 2;
          dust.x_18 = screenOffsetX_800cb568.get();
          dust.y_1c = screenOffsetY_800cb56c.get();

          final SVECTOR vert0 = new SVECTOR().set((short)-a1.size_28, (short)0, (short)-a1.size_28);
          final SVECTOR vert1 = new SVECTOR().set((short) a1.size_28, (short)0, (short)-a1.size_28);
          final SVECTOR vert2 = new SVECTOR().set((short)-a1.size_28, (short)0, (short) a1.size_28);
          final SVECTOR vert3 = new SVECTOR().set((short) a1.size_28, (short)0, (short) a1.size_28);

          dust._04 = 0;
          dust._06 = (short)a1._38;

          final MATRIX ls = new MATRIX();
          GsGetLs(model.coord2_14, ls);
          GTE.setRotationMatrix(ls);
          GTE.setTranslationVector(ls.transfer);

          //TODO The real code actually passes the same reference for sxyz 1 and 2, is that a bug?
          dust.z_4c = RotTransPers4(vert0, vert1, vert2, vert3, dust.v0_20, dust.v1_28, dust.v2_30, dust.v3_38);

          if(dust.z_4c < 41) {
            dust.z_4c = 41;
          }

          //LAB_800ef6a0
          final int a0_0 = dust.v3_38.getX() - dust.v0_20.getX() >> 1 << 16;
          dust._08 = a0_0;
          dust._0c = a0_0 / a1._38;
          dust._10 = 0;

          dust.v0_20.setZ((short)((dust.v3_38.getX() + dust.v0_20.getX()) / 2));
          dust.v1_28.setZ((short)((dust.v3_38.getY() + dust.v0_20.getY()) / 2));

          dust.colourStep_40 = 0x80_0000 / a1._38;
          dust.colourAccumulator_44 = 0x80_0000;
          dust.colour_48 = 0x80;
        }
      }

      //LAB_800ef728
      if(a1._18 == 1) {
        if(_800c6870.getSigned() != -1) {
          FUN_800f0644(model, a1);
        }
      }
    }

    //LAB_800ef750
    a1._1e.set(model.coord2_14.coord.transfer);
    a1._00++;
  }

  @Method(0x800ef798L)
  public static void FUN_800ef798() {
    Struct20 s1 = _800d4ec0;
    Struct20 s0 = s1.next_1c;

    //LAB_800ef7c8
    while(s0 != null) {
      if(s0._00 >= s0._18) {
        s1.next_1c = s0.next_1c;
        s0 = s1.next_1c;
      } else {
        //LAB_800ef804
        s0.transfer.y.decr();

        dustModel_800d4d40.coord2_14.coord.transfer.set(s0.transfer);

        s0.scale_08 += s0.scaleStep_04;

        dustModel_800d4d40.scaleVector_fc.x = s0.scale_08;
        dustModel_800d4d40.scaleVector_fc.y = s0.scale_08;
        dustModel_800d4d40.scaleVector_fc.z = s0.scale_08;

        applyModelRotationAndScale(dustModel_800d4d40);
        renderModel(dustModel_800d4d40);

        dustModel_800d4d40.remainingFrames_9e = 0;
        dustModel_800d4d40.coord2ArrPtr_04[0].flg--;
        s0._00++;

        s1 = s0;
        s0 = s0.next_1c;
      }

      //LAB_800ef888
    }

    //LAB_800ef894
  }

  @Method(0x800ef8acL)
  public static void renderDust() {
    final int[] u = new int[4];
    for(int i = 0; i < 4; i++) {
      u[i] = _800d6bdc.get(i).get();
    }

    final int[] v = new int[4];
    v[3] = 64; // Other values are 0

    final int screenOffsetX = screenOffsetX_800cb568.get();
    final int screenOffsetY = screenOffsetY_800cb56c.get();

    //LAB_800ef9cc
    DustRenderData54 s1 = dust_800d4e68;
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
          final int offsetX = screenOffsetX - s0.x_18;
          final int offsetY = screenOffsetY - s0.y_1c;

          cmd
            .pos(0, offsetX + s0.v0_20.getX(), offsetY + s0.v0_20.getY())
            .pos(1, offsetX + s0.v1_28.getX(), offsetY + s0.v1_28.getY())
            .pos(2, offsetX + s0.v2_30.getX(), offsetY + s0.v2_30.getY())
            .pos(3, offsetX + s0.v3_38.getX(), offsetY + s0.v3_38.getY());

          if(mode == 2) {
            cmd
              .clut(960, 464)
              .vramPos(960, 256)
              .bpp(Bpp.BITS_4)
              .translucent(Translucency.B_MINUS_F);
          } else {
            //LAB_800efb64
            cmd
              .clut((cluts_800d6068.get(7).get() & 0b111111) * 16, cluts_800d6068.get(7).get() >>> 6)
              .vramPos((texPages_800d6050.get(7).get() & 0b1111) * 64, (texPages_800d6050.get(7).get() & 0b10000) != 0 ? 256 : 0)
              .translucent(Translucency.of(texPages_800d6050.get(7).get() >>> 5 & 0b11))
              .bpp(Bpp.of(texPages_800d6050.get(7).get() >>> 7 & 0b11));
          }
        } else if(mode == 1) {
          //LAB_800efb7c
          s0._08 += s0._0c;
          s0._10 = s0._08 >> 16;
          s0.v0_20.setX((short)(s0.v0_20.getZ() - (s0._08 >> 17)));
          s0.v0_20.setY((short)(s0.v1_28.getZ() - (s0._08 >> 17)));
          final int x = screenOffsetX - s0.x_18 + s0.v0_20.getX();
          final int y = screenOffsetY - s0.y_1c + s0.v0_20.getY();

          cmd
            .pos(0, x, y)
            .pos(1, x + s0._10, y)
            .pos(2, x, y + s0._10)
            .pos(3, x + s0._10, y + s0._10);

          if((s0._04 & 0x3) == 0) {
            s0.v1_28.z.decr();
          }

          //LAB_800efc4c
          cmd
            .clut((cluts_800d6068.get(6).get() & 0b111111) * 16, cluts_800d6068.get(6).get() >>> 6)
            .vramPos((texPages_800d6050.get(6).get() & 0b1111) * 64, (texPages_800d6050.get(6).get() & 0b10000) != 0 ? 256 : 0)
            .translucent(Translucency.of(texPages_800d6050.get(6).get() >>> 5 & 0b11))
            .bpp(Bpp.of(texPages_800d6050.get(6).get() >>> 7 & 0b11));
        }

        //LAB_800efc64
        if(s0._04 >= _800d6c0c.get(s0.renderMode_00).get()) {
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
          .uv(1, u[s0.textureIndex_02] + smokeTextureWidths_800d6bec.get(s0.textureIndex_02).get(), v[s0.textureIndex_02])
          .uv(2, u[s0.textureIndex_02], v[s0.textureIndex_02] + smokeTextureHeights_800d6bfc.get(s0.textureIndex_02).get())
          .uv(3, u[s0.textureIndex_02] + smokeTextureWidths_800d6bec.get(s0.textureIndex_02).get(), v[s0.textureIndex_02] + smokeTextureHeights_800d6bfc.get(s0.textureIndex_02).get());

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
  public static void FUN_800efe7c() {
    SMapStruct3c s1 = struct3c_800d4f50;
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

        final int clut = cluts_800d6068.get(6).get();
        final int tpage = texPages_800d6050.get(6).get();

        final int x = screenOffsetX_800cb568.get() - s0.x_0c + (s0._10 & 0xffff);
        final int y = screenOffsetY_800cb56c.get() - s0.y_0e + (s0._14 >> 16) - (s0._24 >> 16);

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
  public static void FUN_800f00a4(final long a0, final long a1) {
    if(MEMORY.ref(4, a0).get() == 0x1L) {
      Struct34 s2 = _800d4f18;
      Struct34 s1 = s2.parent_30;

      //LAB_800f0100
      while(s1 != null) {
        if(s1._08 >= s1._02) {
          if(s1._02 % s1._04 == 0) {
            //LAB_800f0148
            for(int i = 0; i < 4; i++) {
              final SMapStruct3c s0 = FUN_800eed44(struct3c_800d4f50);
              s0._02 = 0;
              s0._06 = s1._06;
              s0.x_0c = (short)screenOffsetX_800cb568.get();
              s0.y_0e = (short)screenOffsetY_800cb56c.get();
              s0._10 = s1.x_1c + (simpleRand() * s1._18 >> 16);
              s0._14 = s1.y_20 << 16;
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

          if(_800d4f48.get() == 0) {
            MEMORY.ref(4, a0).setu(0);
          }

          s1 = s0;
        }
      }
    }

    //LAB_800f023c
    if(MEMORY.ref(4, a1).get() == 0x1L) {
      final long s1 = _800d4ee0.getAddress();

      if(MEMORY.ref(2, s1).offset(0x02L).getSigned() % MEMORY.ref(2, s1).offset(0x04L).getSigned() == 0) {
        //LAB_800f0284
        for(int i = 0; i < 1; i++) {
          final SMapStruct3c s0 = FUN_800eed44(struct3c_800d4f50);
          s0._02 = 0;
          s0._06 = (short)MEMORY.ref(2, s1).offset(0x06L).get();
          s0.x_0c = (short)screenOffsetX_800cb568.get();
          s0.y_0e = (short)screenOffsetY_800cb56c.get();
          s0._10 = (int)(MEMORY.ref(4, s1).offset(0x1cL).get() + (simpleRand() * MEMORY.ref(2, s1).offset(0x18L).getSigned() >> 16));
          s0._14 = (int)(MEMORY.ref(4, s1).offset(0x20L).get() << 16);
          s0._1c = (int)-MEMORY.ref(4, s1).offset(0x0cL).get();
          s0._20 = (int)MEMORY.ref(4, s1).offset(0x14L).get();
          s0._24 = (int)MEMORY.ref(4, s1).offset(0x10L).get();
          s0.size_28 = 0;
          s0._2c = 0x80_0000 / s0._06;
          s0._30 = 0x80_0000;
        }
      }

      //LAB_800f032c
      _800d4ee0.offset(2, 0x2L).addu(0x1L);
    }

    //LAB_800f0344
  }

  @Method(0x800f0370L)
  public static void FUN_800f0370() {
    initModel(dustModel_800d4d40, new CContainer("Dust", new FileData(MEMORY.getBytes(mrg_800d6d1c.getFile(4), mrg_800d6d1c.entries.get(4).size.get()))), new TmdAnimationFile(new FileData(MEMORY.getBytes(mrg_800d6d1c.getFile(5), mrg_800d6d1c.entries.get(5).size.get()))));
    dust_800d4e68.next_50 = null;
    _800d4ec0.next_1c = null;
    FUN_800f0e60();
  }

  @Method(0x800f03c0L)
  public static Struct20 FUN_800f03c0(final Struct20 a0) {
    final Struct20 v0 = new Struct20();
    v0.next_1c = a0.next_1c;
    a0.next_1c = v0;
    return v0;
  }

  @Method(0x800f0400L)
  public static DustRenderData54 addDust(final DustRenderData54 parent) {
    final DustRenderData54 child = new DustRenderData54();
    child.next_50 = parent.next_50;
    parent.next_50 = child;
    return child;
  }

  @Method(0x800f0440L)
  public static void FUN_800f0440() {
    FUN_800f058c();
    deallocateDust();
    FUN_800f0e7c();
  }

  @Method(0x800f047cL)
  public static void FUN_800f047c() {
    FUN_800ef798();
    renderDust();
    FUN_800f0970();
  }

  @Method(0x800f04acL)
  public static void FUN_800f04ac(final BigSubStruct a0) {
    a0._00 = 0;
    a0._04 = 0;
    a0._08 = 0;
    a0._0c = 0;
    a0._10 = 0;
    a0._18 = 0;
    a0._1c = 0;
    a0._1e.set((short)0, (short)0, (short)0);
    a0.size_28 = 0;
    a0._2c = 0;
    a0._30 = 0;
    a0._34 = 0;
    a0._38 = 0;
    a0.ptr_3c = null;
  }

  @Method(0x800f0514L)
  public static void FUN_800f0514() {
    final Struct34 v1 = _800d4f18;

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
    _800f9e74.setu(0);
    FUN_800eed84(struct3c_800d4f50);
    _800f9e70.setu(0);
  }

  @Method(0x800f058cL)
  public static void FUN_800f058c() {
    final Struct20 v1 = _800d4ec0;

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
  public static void deallocateDust() {
    final DustRenderData54 v1 = dust_800d4e68;

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
  public static void FUN_800f0644(final Model124 model, final BigSubStruct a1) {
    if((a1._00 & 0x1) == 0) {
      final Struct18 s2 = a1.ptr_3c;

      if(s2._01 < s2._00) {
        final Struct34_2 s0 = _800d4f90;
        final Struct34_2 s3 = new Struct34_2();
        s3.next_30 = s0.next_30;
        s0.next_30 = s3;

        final Struct14 s4 = _800d4fd0;
        Struct14 s1 = new Struct14();
        s1.next_10 = s4.next_10;
        s4.next_10 = s1;

        final MATRIX sp0x20 = new MATRIX();
        GsGetLs(model.coord2_14, sp0x20);

        PushMatrix();
        GTE.setRotationMatrix(sp0x20);
        GTE.setTranslationVector(sp0x20.transfer);
        GTE.perspectiveTransform(-s2.width_08, _800d6c18.getY(), _800d6c18.getZ());
        s1.vert0_00.setX(GTE.getScreenX(2));
        s1.vert0_00.setY(GTE.getScreenY(2));
        s3.z_20 = GTE.getScreenZ(3) >> 2;

        GTE.perspectiveTransform(s2.width_08, _800d6c20.getY(), _800d6c20.getZ());
        s1.vert1_08.setX(GTE.getScreenX(2));
        s1.vert1_08.setY(GTE.getScreenY(2));
        s3.z_20 = GTE.getScreenZ(3) >> 2;
        PopMatrix();

        s3._00 = 0;
        s3.tpage_04 = GetTPage(Bpp.BITS_4, Translucency.of(s2.translucency_0c), 972, 320);
        FUN_800f0df0(s2, s3);
        s1.vert0_00.x.sub((short)screenOffsetX_800cb568.get());
        s1.vert0_00.y.sub((short)screenOffsetY_800cb56c.get());
        s1.vert1_08.x.sub((short)screenOffsetX_800cb568.get());
        s1.vert1_08.y.sub((short)screenOffsetY_800cb56c.get());

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
  public static void FUN_800f0970() {
    Struct14 a0;
    Struct34_2 s3 = _800d4f90;
    Struct34_2 s1 = s3.next_30;

    final int screenOffsetX = screenOffsetX_800cb568.get();
    final int screenOffsetY = screenOffsetY_800cb56c.get();

    //LAB_800f09c0
    while(s1 != null) {
      final Struct18 s2 = s1._2c;
      if(s2._06 >= s1._00) {
        final int tpage = s1.tpage_04;

        //LAB_800f0b04
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .translucent(Translucency.of(tpage >>> 5 & 0b11))
          .pos(0, screenOffsetX + s1._24.vert0_00.getX(), screenOffsetY + s1._24.vert0_00.getY())
          .pos(1, screenOffsetX + s1._24.vert1_08.getX(), screenOffsetY + s1._24.vert1_08.getY())
          .pos(2, screenOffsetX + s1._28.vert0_00.getX(), screenOffsetY + s1._28.vert0_00.getY())
          .pos(3, screenOffsetX + s1._28.vert1_08.getX(), screenOffsetY + s1._28.vert1_08.getY());

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
        a0 = _800d4fd0.next_10;
        Struct14 v0 = s1._24;
        Struct14 v1 = _800d4fd0;

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
          a0 = _800d4fd0.next_10;
          v0 = s1._28;
          v1 = _800d4fd0;

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
          if(_800d4f90.next_30.next_30 == null) {
            //LAB_800f0aa4
            while(_800d4fd0.next_10 != null) {
              a0 = _800d4fd0.next_10;
              _800d4fd0.next_10 = a0.next_10;
            }
          }
        }
      }

      //LAB_800f0d68
    }

    //LAB_800f0d74
    if(_800f9e78.get() == 0 && _800d4f90.next_30 == null) {
      //LAB_800f0da8
      while(_800d4fd0.next_10 != null) {
        a0 = _800d4fd0.next_10;
        _800d4fd0.next_10 = a0.next_10;
      }
    }

    //LAB_800f0dc8
  }

  @Method(0x800f0df0L)
  public static void FUN_800f0df0(final Struct18 a0, final Struct34_2 a1) {
    a1.rAccumulator_14 = a0.r_10 << 16;
    a1.gAccumulator_18 = a0.g_11 << 16;
    a1.bAccumulator_1c = a0.b_12 << 16;
    a1.rStep_08 = a1.rAccumulator_14 / a0._04;
    a1.gStep_0c = a1.gAccumulator_18 / a0._04;
    a1.bStep_10 = a1.bAccumulator_1c / a0._04;
  }

  @Method(0x800f0e60L)
  public static void FUN_800f0e60() {
    _800f9e78.setu(0);
    _800d4f90.next_30 = null;
    _800d4fd0.next_10 = null;
  }

  @Method(0x800f0e7cL)
  public static void FUN_800f0e7c() {
    final Struct34_2 s1 = _800d4f90;
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
    final Struct14 s1_0 = _800d4fd0;

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
    FUN_800f0fe8();
    _800f9e78.setu(0);
  }

  @Method(0x800f0f20L)
  public static Struct18 FUN_800f0f20() {
    Struct18 v1 = null;

    //LAB_800f0f3c
    for(int a0 = 0; a0 < 8; a0++) {
      if(_800f9e7c[a0] == null) {
        v1 = new Struct18();
        _800f9e7c[a0] = v1;
        break;
      }
    }

    //LAB_800f0f78
    return v1;
  }

  @Method(0x800f0fe8L)
  public static void FUN_800f0fe8() {
    for(int i = 0; i < 8; i++) {
      if(_800f9e7c[i] != null) {
        _800f9e7c[i] = null;
        _800f9e78.subu(0x1L);
      }
    }
  }

  @Method(0x800f1060L)
  public static FlowControl FUN_800f1060(final RunningScript<?> script) {
    if(_800d4fe8.getSigned() != 0) {
      _800d4fe8.addu(0x1L);
      return FlowControl.CONTINUE;
    }

    final MATRIX sp0x20 = new MATRIX();
    final GsCOORDINATE2 sp0x40 = new GsCOORDINATE2();

    //LAB_800f10ac
    struct34_800d6018.parent_30 = null;
    GsInitCoordinate2(null, sp0x40);

    final Param ints = script.params_20[0];
    int s1 = 0;

    //LAB_800f10dc
    while(ints.array(s1).get() != -1) {
      final Struct34 struct = new Struct34();
      struct.parent_30 = struct34_800d6018.parent_30;
      struct34_800d6018.parent_30 = struct;

      sp0x40.coord.transfer.setX(ints.array(s1++).get());
      sp0x40.coord.transfer.setY(ints.array(s1++).get());
      sp0x40.coord.transfer.setZ(ints.array(s1++).get());
      GsGetLs(sp0x40, sp0x20);

      PushMatrix();
      GTE.setRotationMatrix(sp0x20);
      GTE.setTranslationVector(sp0x20.transfer);
      GTE.perspectiveTransform(0, 0, 0);
      final short sx = GTE.getScreenX(2);
      final short sy = GTE.getScreenY(2);

      struct.sz3_2c = GTE.getScreenZ(3) >> 2;
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
      struct.screenOffsetX_24 = screenOffsetX_800cb568.get();
      struct.screenOffsetY_28 = screenOffsetY_800cb56c.get();
    }

    //LAB_800f123c
    _800d4fe8.addu(0x1L);

    //LAB_800f1250
    return FlowControl.CONTINUE;
  }

  @Method(0x800f1274L)
  public static FlowControl FUN_800f1274(final RunningScript<?> script) {
    _800f9e74.setu(0x1L);

    final Struct34 v1 = _800d4f18;
    final Struct34 s1 = new Struct34();
    s1.parent_30 = v1.parent_30;
    v1.parent_30 = s1;

    final GsCOORDINATE2 sp0x48 = new GsCOORDINATE2();
    GsInitCoordinate2(null, sp0x48);

    sp0x48.coord.transfer.set(script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    final MATRIX sp0x28 = new MATRIX();
    GsGetLs(sp0x48, sp0x28);

    PushMatrix();
    GTE.setRotationMatrix(sp0x28);
    GTE.setTranslationVector(sp0x28.transfer);
    GTE.perspectiveTransform(0, 0, 0);

    final short sx = GTE.getScreenX(2);
    final short sy = GTE.getScreenY(2);
    final int sz = GTE.getScreenZ(3) >> 2;
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
    s1.screenOffsetX_24 = screenOffsetX_800cb568.get();
    s1.screenOffsetY_28 = screenOffsetY_800cb56c.get();
    s1.sz3_2c = sz;

    return FlowControl.CONTINUE;
  }

  @Method(0x800f14f0L)
  public static FlowControl FUN_800f14f0(final RunningScript<?> script) {
    final int v1 = script.params_20[0].get();
    _800f9e70.setu(v1);

    final long a1 = _800d4ee0.getAddress();
    if(v1 != 0) {
      MEMORY.ref(2, a1).offset(0x2L).setu(0);
      MEMORY.ref(2, a1).offset(0x4L).setu(script.params_20[1].get());

      if(script.params_20[2].get() == 0) {
        script.params_20[2].set(1);
      }

      //LAB_800f154c
      MEMORY.ref(2, a1).offset(0x6L).setu(script.params_20[2].get());
      MEMORY.ref(4, a1).offset(0x1cL).setu(script.params_20[3].get());
      MEMORY.ref(4, a1).offset(0x20L).setu(script.params_20[4].get());
      MEMORY.ref(2, a1).offset(0x18L).setu(script.params_20[5].get());
      MEMORY.ref(4, a1).offset(0xcL).setu((script.params_20[6].get() << 16) / script.params_20[2].get());
      MEMORY.ref(4, a1).offset(0x10L).setu(script.params_20[7].get() << 16);
      MEMORY.ref(4, a1).offset(0x14L).setu((script.params_20[8].get() << 16) / script.params_20[2].get());

      //LAB_800f15fc
    } else {
      MEMORY.ref(2, a1).offset(0x04L).setu(0);
      MEMORY.ref(2, a1).offset(0x02L).setu(0);
      MEMORY.ref(2, a1).offset(0x06L).setu(0);
      MEMORY.ref(4, a1).offset(0x0cL).setu(0);
      MEMORY.ref(4, a1).offset(0x10L).setu(0);
      MEMORY.ref(4, a1).offset(0x14L).setu(0);
      MEMORY.ref(2, a1).offset(0x18L).setu(0);
      MEMORY.ref(4, a1).offset(0x1cL).setu(0);
      MEMORY.ref(4, a1).offset(0x20L).setu(0);
    }

    //LAB_800f162c
    return FlowControl.CONTINUE;
  }

  @Method(0x800f1634L)
  public static FlowControl FUN_800f1634(final RunningScript<?> script) {
    final ScriptState<?> state = script.scriptState_04;

    script.params_20[9] = new ScriptStorageParam(state, 0);

    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[state.storage_44[0]].innerStruct_00;
    if(script.params_20[0].get() == 0 || _800f9e78.getSigned() >= 8) {
      //LAB_800f1698
      sobj._1d0._18 = 0;
    } else {
      //LAB_800f16a4
      final Struct18 a1 = FUN_800f0f20();
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
      _800f9e78.addu(0x1L);
    }

    //LAB_800f1784
    return FlowControl.CONTINUE;
  }

  @Method(0x800f179cL)
  public static FlowControl scriptAddSavePoint(final RunningScript<?> script) {
    final DVECTOR sp0x48 = new DVECTOR();
    final DVECTOR sp0x50 = new DVECTOR();
    final GsCOORDINATE2 coord2 = new GsCOORDINATE2();

    hasSavePoint_800d5620 = script.params_20[0].get() != 0;
    GsInitCoordinate2(null, coord2);

    coord2.coord.transfer.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    savePointPos_800d5622.set(coord2.coord.transfer);

    final MATRIX screenMatrix = new MATRIX();
    GsGetLs(coord2, screenMatrix);
    PushMatrix();

    GTE.setRotationMatrix(screenMatrix);
    GTE.setTranslationVector(screenMatrix.transfer);

    //LAB_800f195c
    for(int s3 = 0; s3 < 2; s3++) {
      final SavePointRenderData44 struct = savePoint_800d5598[s3];

      struct.z_40 = RotTransPers4(savePointV0_800d6c28, savePointV1_800d6c30, savePointV2_800d6c38, savePointV3_800d6c40, struct.vert0_00, struct.vert1_08, struct.vert2_10, struct.vert3_18);

      if(s3 == 0) {
        perspectiveTransform(_800d6c48, sp0x48);
        perspectiveTransform(_800d6c50, sp0x50);

        sp0x48.setX((short)(sp0x50.getY() - sp0x48.getY()));
      }

      //LAB_800f1a34
      final int a3 = (struct.vert0_00.getX() + struct.vert3_18.getX()) / 2;
      final int t0 = (struct.vert0_00.getY() + struct.vert3_18.getY()) / 2;
      final int a2 = (struct.vert0_00.getX() - struct.vert3_18.getX()) / 2;

      final int x0 = a3 - a2;
      struct.vert0_00.setX((short)x0);
      struct.vert2_10.setX((short)x0);
      final int y0 = t0 - a2 - sp0x48.getX();
      struct.vert0_00.setY((short)y0);
      struct.vert1_08.setY((short)y0);
      final int x1 = a3 + a2;
      struct.vert1_08.setX((short)x1);
      struct.vert3_18.setX((short)x1);
      final int y1 = t0 + a2 - sp0x48.getX();
      struct.vert2_10.setY((short)y1);
      struct.vert3_18.setY((short)y1);

      //LAB_800f1b04
      struct.screenOffsetX_20 = screenOffsetX_800cb568.get();
      struct.screenOffsetY_24 = screenOffsetY_800cb56c.get();
    }

    PopMatrix();

    return FlowControl.CONTINUE;
  }

  @Method(0x800f1b64L)
  public static FlowControl FUN_800f1b64(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    final GsCOORDINATE2 sp0x18 = new GsCOORDINATE2();
    final MATRIX sp0x70 = new MATRIX();

    GsInitCoordinate2(null, sp0x18);

    final TriangleIndicator140 a0 = _800c69fc;

    //LAB_800f1ba8
    final Param ints = script.params_20[0];
    int s0 = 0;
    for(int i = 0; ints.array(s0).get() != -1; i++) {
      get3dAverageOfSomething(ints.array(s0++).get(), sp0x10);

      sp0x18.coord.transfer.set(sp0x10);
      GsGetLs(sp0x18, sp0x70);

      PushMatrix();
      GTE.setRotationMatrix(sp0x70);
      GTE.setTranslationVector(sp0x70.transfer);
      GTE.perspectiveTransform(0, 0, 0);
      final short sx = GTE.getScreenX(2);
      final short sy = GTE.getScreenY(2);
      PopMatrix();

      a0._18[i] = (short)ints.array(s0++).get();
      a0.x_40[i] = (short)(sx + ints.array(s0++).get());
      a0.y_68[i] = (short)(sy + ints.array(s0++).get());
      a0.screenOffsetX_90[i] = screenOffsetX_800cb568.get();
      a0.screenOffsetY_e0[i] = screenOffsetY_800cb56c.get();
    }

    //LAB_800f1cf0
    return FlowControl.CONTINUE;
  }

  @Method(0x800f1d0cL)
  public static FlowControl FUN_800f1d0c(final RunningScript<?> script) {
    final GsCOORDINATE2 sp0x40 = new GsCOORDINATE2();
    GsInitCoordinate2(null, sp0x40);
    final SVECTOR sp0x10 = new SVECTOR();
    get3dAverageOfSomething(script.params_20[0].get(), sp0x10);
    sp0x40.coord.transfer.set(sp0x10);
    final MATRIX sp0x20 = new MATRIX();
    GsGetLs(sp0x40, sp0x20);

    PushMatrix();
    GTE.setRotationMatrix(sp0x20);
    GTE.setTranslationVector(sp0x20.transfer);
    GTE.perspectiveTransform(0, 0, 0);
    final short sx = GTE.getScreenX(2);
    final short sy = GTE.getScreenY(2);
    PopMatrix();

    //LAB_800f1e20
    for(int i = 0; i < 20; i++) {
      final TriangleIndicator140 a1 = _800c69fc;

      if(a1._18[i] == -1) {
        a1._18[i] = (short)script.params_20[1].get();
        a1.x_40[i] = (short)(sx + script.params_20[2].get());
        a1.y_68[i] = (short)(sy + script.params_20[3].get());
        a1.screenOffsetX_90[i] = screenOffsetX_800cb568.get();
        a1.screenOffsetY_e0[i] = screenOffsetY_800cb56c.get();
        break;
      }
    }

    //LAB_800f1ea0
    return FlowControl.CONTINUE;
  }

  @Method(0x800f1eb8L)
  public static FlowControl FUN_800f1eb8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800f1f9cL)
  public static FlowControl FUN_800f1f9c(final RunningScript<?> script) {
    final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
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
      sobj._1d0._1e.set((short)0, (short)0, (short)0);
      sobj._1d0.size_28 = 1;
      sobj._1d0._30 = 0;
      sobj._1d0._38 = 0;
    }

    //LAB_800f2040
    return FlowControl.CONTINUE;
  }

  @Method(0x800f2048L)
  public static FlowControl FUN_800f2048(final RunningScript<?> script) {
    script.params_20[4] = script.params_20[3];
    script.params_20[3] = script.params_20[2];
    script.params_20[2] = script.params_20[1];
    script.params_20[1] = script.params_20[0];
    script.params_20[0] = new ScriptStorageParam(script.scriptState_04, 0);
    return FUN_800f1f9c(script);
  }

  @Method(0x800f2090L)
  public static FlowControl FUN_800f2090(final RunningScript<?> script) {
    final Param ints = script.params_20[0];
    int a0 = 0;
    Struct34 a1 = struct34_800d6018.parent_30;
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

  @Method(0x800f2198L)
  public static FlowControl FUN_800f2198(final RunningScript<?> script) {
    final short a1 = (short)script.params_20[0].get();
    final long a2 = _800d4d20.getAddress();

    MEMORY.ref(2, a2).offset(0x02L).setu(a1);

    if(a1 == 0) {
      _800f9e60.set((short)0);
      //LAB_800f21d0
    } else if(a1 == 1) {
      _800f9e60.set(a1);

      if(script.params_20[2].get() < 0) {
        script.params_20[2].neg();
      }

      //LAB_800f2210
      MEMORY.ref(4, a2).offset(0x04L).setu(0);
      MEMORY.ref(4, a2).offset(0x08L).setu(script.params_20[4].get());
      MEMORY.ref(4, a2).offset(0x0cL).setu(script.params_20[3].get());
      MEMORY.ref(4, a2).offset(0x10L).setu(script.params_20[1].get());
      MEMORY.ref(4, a2).offset(0x14L).setu(script.params_20[2].get());
      //LAB_800f2250
    } else if(a1 == 2) {
      _800f9e60.set(a1);
    }

    //LAB_800f225c
    return FlowControl.CONTINUE;
  }

  @Method(0x800f2264L)
  public static FlowControl FUN_800f2264(final RunningScript<?> script) {
    final ScriptState<?> sobj1 = script.scriptState_04;
    script.params_20[1] = new ScriptStorageParam(sobj1, 0);

    final SubmapObject210 sobj2 = (SubmapObject210)scriptStatePtrArr_800bc1c0[sobj1.storage_44[0]].innerStruct_00;
    if(script.params_20[0].get() - 1 < 2) {
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

  @Method(0x800f22c4L)
  public static FlowControl FUN_800f22c4(final RunningScript<?> script) {
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
      a0._1d0._1e.set((short)0, (short)0, (short)0);
    }

    //LAB_800f2398
    return FlowControl.CONTINUE;
  }

  @Method(0x800f23a0L)
  public static FlowControl FUN_800f23a0(final RunningScript<?> script) {
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

  @Method(0x800f23ecL)
  public static FlowControl FUN_800f23ec(final RunningScript<?> script) {
    script.params_20[4] = new ScriptStorageParam(script.scriptState_04, 0);
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
      a1._1d0._1e.set((short)0, (short)0, (short)0);
    }

    //LAB_800f24a8
    return FlowControl.CONTINUE;
  }

  @Method(0x800f24b0L)
  public static FlowControl FUN_800f24b0(final RunningScript<?> script) {
    if(script.params_20[0].get() == 1) {
      _800f9e70.setu(0x2L);
    }

    //LAB_800f24d0
    return FlowControl.CONTINUE;
  }

  @Method(0x800f24d8L)
  public static FlowControl FUN_800f24d8(final RunningScript<?> script) {
    if(script.params_20[0].get() != 0) {
      _800f9e70.setu(0);
    }

    //LAB_800f24fc
    if(_800f9e70.get() == 0) {
      final long v0 = _800d4ee0.getAddress();
      MEMORY.ref(2, v0).offset(0x02L).setu(0);
      MEMORY.ref(2, v0).offset(0x04L).setu(0);
      MEMORY.ref(2, v0).offset(0x06L).setu(0);
      MEMORY.ref(4, v0).offset(0x0cL).setu(0);
      MEMORY.ref(4, v0).offset(0x10L).setu(0);
      MEMORY.ref(4, v0).offset(0x14L).setu(0);
      MEMORY.ref(2, v0).offset(0x18L).setu(0);
      MEMORY.ref(4, v0).offset(0x1cL).setu(0);
      MEMORY.ref(4, v0).offset(0x20L).setu(0);
      FUN_800eed84(struct3c_800d4f50);
    }

    //LAB_800f2544
    return FlowControl.CONTINUE;
  }

  @Method(0x800f2554L)
  public static FlowControl FUN_800f2554(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800f25a8L)
  public static FlowControl FUN_800f25a8(final RunningScript<?> script) {
    final ScriptState<?> v1 = script.scriptState_04;
    script.params_20[1] = new ScriptStorageParam(v1, 0);

    if(script.params_20[0].get() == 1) {
      FUN_800f0e7c();
      final SubmapObject210 sobj = (SubmapObject210)scriptStatePtrArr_800bc1c0[v1.storage_44[0]].innerStruct_00;
      sobj._1d0._18 = 0;
    }

    //LAB_800f2604
    return FlowControl.CONTINUE;
  }

  @Method(0x800f2618L)
  public static FlowControl FUN_800f2618(final RunningScript<?> script) {
    final int x = screenOffsetX_800cb568.get();
    final int y = screenOffsetY_800cb56c.get();

    final TriangleIndicator140 v1 = _800c69fc;

    //LAB_800f266c
    int i = 0;
    final Param a0 = script.params_20[0];
    for(int a1 = 0; a0.array(i).get() != -1; a1++) {
      v1._18[a1] = (short)a0.array(i++).get();
      v1.x_40[a1] = (short)(a0.array(i++).get() + x);
      v1.y_68[a1] = (short)(a0.array(i++).get() + y);
      v1.screenOffsetX_90[a1] = x;
      v1.screenOffsetY_e0[a1] = y;
    }

    //LAB_800f26b4
    return FlowControl.CONTINUE;
  }

  @Method(0x800f26c8L)
  public static FlowControl FUN_800f26c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800f2780L)
  public static FlowControl FUN_800f2780(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800f2788L)
  public static void initSavePoint() {
    initModel(savePointModel_800d5eb0, new CContainer("Save point", new FileData(MEMORY.getBytes(mrg_800d6d1c.getFile(2), mrg_800d6d1c.entries.get(2).size.get()))), new TmdAnimationFile(new FileData(MEMORY.getBytes(mrg_800d6d1c.getFile(3), mrg_800d6d1c.entries.get(3).size.get()))));
    savePoint_800d5598[0].rotation_28 = 0.0f;
    savePoint_800d5598[0].colour_34 = 0.3125f;
    savePoint_800d5598[1].rotation_28 = 0.0f;
    savePoint_800d5598[1].fadeAmount_2c = 0.0077f;
    savePoint_800d5598[1].colour_34 = 0.0f;
    savePoint_800d5598[1].fadeState_38 = 0;

    //LAB_800f285c
    for(int i = 0; i < 8; i++) {
      final SavePointRenderData44 struct0 = savePoint_800d5630[i * 4];
      final SavePointRenderData44 struct1 = savePoint_800d5630[i * 4 + 1];
      final SavePointRenderData44 struct2 = savePoint_800d5630[i * 4 + 2];
      final SavePointRenderData44 struct3 = savePoint_800d5630[i * 4 + 3];
      struct0.colour_34 = 0.5f;
      struct0.fadeAmount_2c = 0.0078f;
      struct0.fadeState_38 = 0;
      struct0.rotation_28 = MathHelper.psxDegToRad(_800d6c58.get(i).get());
      struct1.colour_34 = 0.375f;
      struct2.colour_34 = struct0.colour_34 - 0.25f;
      struct3.colour_34 = struct0.colour_34 - 0.375f;
    }
  }

  @Method(0x800f28d8L)
  public static void renderSavePoint() {
    int minX = 0;
    int maxX = 0;
    int minY = 0;
    int maxY = 0;

    final int screenOffsetX = screenOffsetX_800cb568.get();
    final int screenOffsetY = screenOffsetY_800cb56c.get();

    final Model124 model = savePointModel_800d5eb0;
    model.scaleVector_fc.set(1.5f, 3.0f, 1.5f);
    model.coord2_14.coord.transfer.set(savePointPos_800d5622);

    applyModelRotationAndScale(model);
    animateModel(model);
    renderModel(model);

    GPU.queueCommand(1, new GpuCommandCopyVramToVram(984, 288 + (short)_800f9ea0.get(), 992, 288, 8, 64 - (short)_800f9ea0.get()));
    GPU.queueCommand(1, new GpuCommandCopyVramToVram(984, 288, 992, 352 - (short)_800f9ea0.getSigned(), 8, (short)_800f9ea0.get()));

    _800f9ea0.addu(0x1L).and(0x3f);

    //LAB_800f2a44
    // This loop renders the central circle
    for(int i = 0; i < 2; i++) {
      final SavePointRenderData44 s0 = savePoint_800d5598[i];

      final int offsetX = screenOffsetX - s0.screenOffsetX_20;
      final int offsetY = screenOffsetY - s0.screenOffsetY_24;

      final int x0 = offsetX + s0.vert0_00.getX();
      final int y0 = offsetY + s0.vert0_00.getY();
      final int x1 = offsetX + s0.vert1_08.getX();
      final int y1 = offsetY + s0.vert1_08.getY();
      final int x2 = offsetX + s0.vert2_10.getX();
      final int y2 = offsetY + s0.vert2_10.getY();
      final int x3 = offsetX + s0.vert3_18.getX();
      final int y3 = offsetY + s0.vert3_18.getY();

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
        .bpp(Bpp.of(texPages_800d6050.get(5).get() >>> 7 * 0b11))
        .translucent(Translucency.B_PLUS_F)
        .monochrome((int)(s0.colour_34 * 255.0f))
        .clut((cluts_800d6068.get(5).get() & 0b111111) * 16, cluts_800d6068.get(5).get() >>> 6)
        .vramPos((texPages_800d6050.get(5).get() & 0b1111) * 64, (texPages_800d6050.get(5).get() & 0b10000) != 0 ? 256 : 0)
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

    final int sp80 = ((short)minX - (short)maxX) / 2;
    final int sp68 = ((short)minX + (short)maxX) / 2;
    final int sp78 = ((short)maxY - (short)minY) / 2;
    final int sp6a = ((short)maxY + (short)minY) / 2;

    //LAB_800f2de8
    for(int fp = 0; fp < 8; fp++) {
      final SavePointRenderData44 struct0 = savePoint_800d5630[fp * 4];
      final SavePointRenderData44 struct1 = savePoint_800d5630[fp * 4 + 1];
      final SavePointRenderData44 struct2 = savePoint_800d5630[fp * 4 + 2];
      final SavePointRenderData44 struct3 = savePoint_800d5630[fp * 4 + 3];
      struct3.vert0_00.setX(struct2.vert0_00.getX());
      struct3.vert0_00.setY(struct2.vert0_00.getY());
      struct2.vert0_00.setX(struct1.vert0_00.getX());
      struct2.vert0_00.setY(struct1.vert0_00.getY());
      struct1.vert0_00.setX(struct0.vert0_00.getX());
      struct1.vert0_00.setY(struct0.vert0_00.getY());
      struct0.vert0_00.setX((short)(sp68 + (sp80 + _800d6c78.get(fp).get()) * MathHelper.sin(struct0.rotation_28)));
      struct0.vert0_00.setY((short)(sp6a + (sp78 + _800d6c78.get(fp).get()) * MathHelper.cos(struct0.rotation_28)));

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
        final SavePointRenderData44 struct = savePoint_800d5630[fp + s4];

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .bpp(Bpp.of(texPages_800d6050.get(4).get() >>> 7 * 0b11))
          .translucent(Translucency.B_PLUS_F)
          .monochrome((int)(struct.colour_34 * 255.0f))
          .clut((cluts_800d6068.get(4).get() & 0b111111) * 16, cluts_800d6068.get(4).get() >>> 6)
          .vramPos((texPages_800d6050.get(4).get() & 0b1111) * 64, (texPages_800d6050.get(4).get() & 0b10000) != 0 ? 256 : 0)
          .pos(0, struct.vert0_00.getX(), struct.vert0_00.getY())
          .pos(1, struct.vert0_00.getX() + 6, struct.vert0_00.getY())
          .pos(2, struct.vert0_00.getX(), struct.vert0_00.getY() + 6)
          .pos(3, struct.vert0_00.getX() + 6, struct.vert0_00.getY() + 6);

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

      struct0.rotation_28 += MathHelper.psxDegToRad(savePointFloatiesRotations_800d6c88.get(fp).get());
      struct0.rotation_28 %= MathHelper.TWO_PI;
    }

    _800f9ea4.incr();
  }

  @Method(0x800f31bcL)
  public static void handleTriangleIndicators() {
    _800c69fc.screenOffsetX_10 = screenOffsetX_800cb568.get();
    _800c69fc.screenOffsetY_14 = screenOffsetY_800cb56c.get();

    if(gameState_800babc8.indicatorsDisabled_4e3) {
      return;
    }

    if(fullScreenEffect_800bb140.currentColour_28 != 0) {
      return;
    }

    final IndicatorMode indicatorMode = CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get());
    if(indicatorMode != IndicatorMode.MOMENTARY) {
      momentaryIndicatorTicks_800f9e9c.set(0);
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
        momentaryIndicatorTicks_800f9e9c.set(0);
      }
      //LAB_800f3260
    } else if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_1)) { // L1
      if(indicatorMode == IndicatorMode.OFF) {
        //LAB_800f3274
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.ON);
        //LAB_800f3280
      } else if(indicatorMode == IndicatorMode.MOMENTARY) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.OFF);
        momentaryIndicatorTicks_800f9e9c.set(0);
        //LAB_800f3294
      } else if(indicatorMode == IndicatorMode.ON) {
        CONFIG.setConfig(CoreMod.INDICATOR_MODE_CONFIG.get(), IndicatorMode.MOMENTARY);

        //LAB_800f32a4
        momentaryIndicatorTicks_800f9e9c.set(0);
      }
    }

    //LAB_800f32a8
    //LAB_800f32ac
    if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) == IndicatorMode.OFF) {
      return;
    }

    final MATRIX ls = new MATRIX();
    GsGetLs(sobjs_800c6880[0].innerStruct_00.model_00.coord2_14, ls);

    PushMatrix();
    GTE.setRotationMatrix(ls);
    GTE.setTranslationVector(ls.transfer);

    GTE.perspectiveTransform(bottom_800d6cb8);
    final short bottomY = GTE.getScreenY(2);

    GTE.perspectiveTransform(top_800d6cc0);
    final short topY = GTE.getScreenY(2);

    GTE.perspectiveTransform(0, -(topY - bottomY) - 48, 0);
    _800c69fc.playerX_08 = GTE.getScreenX(2);
    _800c69fc.playerY_0c = GTE.getScreenY(2);

    PopMatrix();

    if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) == IndicatorMode.MOMENTARY) {
      if(momentaryIndicatorTicks_800f9e9c.get() < 33) {
        renderTriangleIndicators();
        momentaryIndicatorTicks_800f9e9c.incr();
      }
      //LAB_800f3508
    } else if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) == IndicatorMode.ON) {
      renderTriangleIndicators();
    }

    //LAB_800f3518
  }

  @Method(0x800f352cL)
  public static void renderTriangleIndicators() {
    final TriangleIndicator140 s5 = _800c69fc;

    //LAB_800f35b0
    for(int indicatorIndex = 0; indicatorIndex < 21; indicatorIndex++) {
      final TriangleIndicator44 s1 = _800d4ff0.get(indicatorIndex);

      final AnmFile anm;
      if(indicatorIndex == 0) {
        // Player indicator

        s1.x_34.set(s5.playerX_08);
        s1.y_38.set(s5.playerY_0c - 28);

        anm = playerIndicatorAnimation_800d5588.anm_00.deref();
      } else {
        // Door indicators

        //LAB_800f35f4
        if(s5._18[indicatorIndex - 1] < 0) {
          break;
        }

        s1.x_34.set(s5.screenOffsetX_10 - s5.screenOffsetX_90[indicatorIndex - 1] + s5.x_40[indicatorIndex - 1] -  2);
        s1.y_38.set(s5.screenOffsetY_14 - s5.screenOffsetY_e0[indicatorIndex - 1] + s5.y_68[indicatorIndex - 1] - 32);

        anm = doorIndicatorAnimation_800d5590.anm_00.deref();
      }

      final UnboundedArrayRef<RelativePointer<AnmSpriteGroup>> spriteGroups = anm.getSpriteGroups();

      //LAB_800f365c
      if((s1._00.get() & 0x1) == 0) {
        s1.time_08.decr();

        if(s1.time_08.get() < 0) {
          s1.sequence_04.incr();

          if(s1.sequence_04.get() > s1.sequenceCount_14.get()) {
            s1.sequence_04.set(s1._10.get());
            s1._0c.incr();
          }

          //LAB_800f36b0
          s1.time_08.set(anm.getSequences().get(s1.sequence_04.get()).time_02.get() - 1);
        }
      }

      //LAB_800f36d0
      final AnmSpriteGroup group = spriteGroups.get(anm.getSequences().get(s1.sequence_04.get()).spriteGroupNumber_00.get()).deref();
      final int count = group.n_sprite_00.get();

      //LAB_800f3724
      for(int s6 = count - 1; s6 >= 0; s6--) {
        final AnmSpriteMetrics14 sprite = group.metrics_04.get(s6);

        final int x = s1.x_34.get() - sprite.w_08.get() / 2;
        final int y = s1.y_38.get();
        final int u = s1.u_1c.get() + sprite.u_00.get();
        final int v = s1.v_20.get() + sprite.v_01.get();
        final int tpage = s1.tpage_18.get() | sprite.flag_06.get() & 0x60;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
          .bpp(Bpp.of(tpage >>> 7 & 0b11))
          .rgb(s1.r_24.get(), s1.g_25.get(), s1.b_26.get())
          .pos(0, x, y)
          .pos(1, x + sprite.w_08.get(), y)
          .pos(2, x, y + sprite.h_0a.get())
          .pos(3, x + sprite.w_08.get(), y + sprite.h_0a.get())
          .uv(0, u, v)
          .uv(1, u + sprite.w_08.get(), v)
          .uv(2, u, v + sprite.h_0a.get())
          .uv(3, u + sprite.w_08.get(), v + sprite.h_0a.get());

        if(indicatorIndex == 0) { // Player indicator
          final int triangleIndex = getEncounterTriangleColour();
          cmd.clut(_800d6cd8.get(triangleIndex).get() & 0x3f0, (sprite.cba_04.get() >>> 6 & 0x1ff) - _800d6ce4.get(triangleIndex).get());
        } else { // Door indicators
          //LAB_800f3884
          if((sprite.cba_04.get() & 0x8000L) != 0) {
            cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
          }

          cmd.clut(992, (sprite.cba_04.get() >>> 6 & 0x1ff) - _800d6cc8.get(s5._18[indicatorIndex - 1]).get());
        }

        //LAB_800f38b0
        GPU.queueCommand(38, cmd);
      }
    }

    //LAB_800f39d0
  }

  @Method(0x800f3a00L)
  public static int getEncounterTriangleColour() {
    final int acc = encounterAccumulator_800c6ae8.get();

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
  public static void initTriangleIndicators() {
    parseAnmFile(mrg_800d6d1c.getFile(0, AnmFile::new), playerIndicatorAnimation_800d5588);
    parseAnmFile(mrg_800d6d1c.getFile(1, AnmFile::new), doorIndicatorAnimation_800d5590);
    FUN_800f3b64(mrg_800d6d1c.getFile(0, AnmFile::new), mrg_800d6d1c.getFile(1, AnmFile::new), _800d4ff0, 0x15L);
  }

  @Method(0x800f3abcL)
  public static void renderSubmapOverlays() {
    handleTriangleIndicators();

    if(hasSavePoint_800d5620) {
      renderSavePoint();
    }
  }

  @Method(0x800f3af8L)
  public static void resetTriangleIndicators() {
    if(CONFIG.getConfig(CoreMod.INDICATOR_MODE_CONFIG.get()) != IndicatorMode.OFF) {
      momentaryIndicatorTicks_800f9e9c.set(0);
    }

    //LAB_800f3b14
    final TriangleIndicator140 v0 = _800c69fc;

    //LAB_800f3b24
    for(int i = 0; i < 20; i++) {
      v0._18[i] = -1;
    }
  }

  @Method(0x800f3b64L)
  public static void FUN_800f3b64(final AnmFile anm1, final AnmFile anm2, final ArrayRef<TriangleIndicator44> a2, final long count) {
    //LAB_800f3bc4
    for(int i = 0; i < count; i++) {
      final TriangleIndicator44 t2 = a2.get(i);

      final AnmFile anm;
      if(i == 0) {
        t2.tpage_18.set(texPages_800d6050.get(0).get());
        t2.clut_1a.set(cluts_800d6068.get(0).get());
        t2.u_1c.set(dartArrowU_800d6ca8.get());
        t2.v_20.set(dartArrowV_800d6cac.get());
        anm = anm1;
      } else {
        //LAB_800f3bfc
        t2.tpage_18.set(texPages_800d6050.get(1).get());
        t2.clut_1a.set(cluts_800d6068.get(1).get());
        t2.u_1c.set(doorArrowU_800d6cb0.get());
        t2.v_20.set(doorArrowV_800d6cb4.get());
        anm = anm2;
      }

      //LAB_800f3c24
      t2._00.set(0);
      t2.sequence_04.set(0);
      t2.time_08.set(anm.getSequences().get(0).time_02.get());
      t2._0c.set(0);
      t2._10.set(0);
      t2.sequenceCount_14.set(anm.n_sequence_06.get() - 1);

      t2.r_24.set(0x80);
      t2.g_25.set(0x80);
      t2.b_26.set(0x80);

      t2._28.set(0x1000);
      t2._2c.set(0x1000);
      t2._30.set(0);
      t2.x_34.set(0);
      t2.y_38.set(0);
      t2._3c.set(0);
    }
  }

  @Method(0x800f3b3cL)
  public static void deallocateSavePoint() {
    hasSavePoint_800d5620 = false;
  }

  @Method(0x800f3c98L)
  public static void parseAnmFile(final AnmFile anmFile, final AnimatedSprite08 a1) {
    a1.anm_00.set(anmFile);
    a1.spriteGroup_04.set(anmFile.getSpriteGroups());
  }

  @Method(0x800f3cb8L)
  public static void FUN_800f3cb8() {
    Struct34 s1 = struct34_800d6018.parent_30;

    //LAB_800f3ce8
    while(s1 != null) {
      if(s1._08 == 0) {
        if(s1._02 % s1._04 == 0) {
          final SMapStruct3c s0 = new SMapStruct3c();

          s0._02 = 0;
          s0._06 = s1._06;
          s0.x_0c = (short)s1.screenOffsetX_24;
          s0.y_0e = (short)s1.screenOffsetY_28;
          s0._10 = (s1.x_1c << 16) - simpleRand() * s1._18;
          s0._14 = s1.y_20 << 16;
          s0._1c = 0x8_0000 / s1._0c;
          s0._20 = (s1._14 << 16) / s1._06;
          s0._24 = s1._10 << 16;
          s0.size_28 = 0;
          s0._2c = 0x80_0000 / s0._06;
          s0._30 = 0x80_0000;
          s0.z_34 = s1.sz3_2c;
          s0.parent_38 = struct3c_800d5fd8.parent_38;

          struct3c_800d5fd8.parent_38 = s0;
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
            s0._10 = (s1.x_1c << 16) - simpleRand() * s1._18;
            s0._14 = s1.y_20 << 16;
            s0._1c = 0x8_0000 / s1._0c;
            s0._20 = (s1._14 << 16) / s1._06;
            s0._24 = s1._10 << 16;
            s0.size_28 = 0;
            s0._2c = 0x80_0000 / s0._06;
            s0._30 = 0x80_0000;
            s0.z_34 = s1.sz3_2c;
            s0.parent_38 = struct3c_800d5fd8.parent_38;

            struct3c_800d5fd8.parent_38 = s0;
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
  public static void renderSmoke() {
    SMapStruct3c s1 = struct3c_800d5fd8;
    SMapStruct3c s0 = s1.parent_38;

    final int sp20 = screenOffsetX_800cb568.get();
    final int sp24 = screenOffsetY_800cb56c.get();

    //LAB_800f3fb0
    while(s0 != null) {
      if(s0._02 < s0._06) {
        //LAB_800f3fe8
        s0._24 += s0._20;
        s0._14 -= s0._1c;
        s0._30 -= s0._2c;
        s0.size_28 = (short)(s0._24 >> 16);
        final int x = sp20 - s0.x_0c + (s0._10 >> 16);
        final int y = sp24 - s0.y_0e + (s0._14 >> 16);
        final int size = s0.size_28 / 2;
        final int left = x - size;
        final int right = x + size;
        final int top = y - size;
        final int bottom = y + size;
        final int v0 = s0._30 >> 16;
        final int colour = v0 > 0x80 ? 0 : v0;

        //LAB_800f4084
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .monochrome(colour)
          .clut((cluts_800d6068.get(9).get() & 0b111111) * 16, cluts_800d6068.get(9).get() >>> 6)
          .vramPos((texPages_800d6050.get(9).get() & 0b1111) * 64, (texPages_800d6050.get(9).get() & 0b10000) != 0 ? 256 : 0)
          .translucent(Translucency.of(texPages_800d6050.get(9).get() >>> 5 & 0b11))
          .bpp(Bpp.of(texPages_800d6050.get(9).get() >>> 7 & 0b11))
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
  public static void FUN_800f41dc() {
    //LAB_800f4204
    while(struct34_800d6018.parent_30 != null) {
      final Struct34 a0 = struct34_800d6018.parent_30;
      struct34_800d6018.parent_30 = a0.parent_30;
    }

    //LAB_800f4224
    FUN_800eed84(struct3c_800d5fd8);
  }

  @Method(0x800f4244L)
  public static void FUN_800f4244(final Tim tim, final UnsignedShortRef tpageOut, final UnsignedShortRef clutOut, final Translucency transMode) {
    //LAB_800f427c
    if(tim.hasClut()) {
      final RECT clutRect = tim.getClutRect();
      clutOut.set(clutRect.y.get() << 6 | (clutRect.x.get() & 0x3f0) >> 4);
      LoadImage(clutRect, tim.getClutData());
    }

    //LAB_800f42d0
    final RECT imageRect = tim.getImageRect();
    tpageOut.set(texPages_800bb110.get(Bpp.values()[tim.getFlags() & 0b11]).get(transMode).get(TexPageY.fromY(imageRect.y.get())).get() | (imageRect.x.get() & 0x3c0) >> 6);
    LoadImage(imageRect, tim.getImageData());

    //LAB_800f4338
  }

  @Method(0x800f4354L)
  public static void FUN_800f4354() {
    if(_800c6870.getSigned() == -1) {
      FUN_800f41dc();

      if(_800f9e60.get() - 0x1L < 0x2L) {
        handleSnow();
      }

      FUN_800f0514();
    } else {
      FUN_800f3cb8();
      renderSmoke();

      if(_800f9e60.get() == 0x1L) {
        handleSnow();
      }

      if(_800f9e74.get() != 0 || _800f9e70.get() != 0) {
        FUN_800f00a4(_800f9e74.getAddress(), _800f9e70.getAddress());
        FUN_800efe7c();
      }
    }
  }

  @Method(0x800f4420L)
  public static void FUN_800f4420() {
    FUN_800f41dc();

    if(_800f9e60.get() - 0x1L < 0x2L) {
      handleSnow();
    }

    //LAB_800f4454
    FUN_800f0514();
  }

  /** Things such as the save point, &lt;!&gt; action icon, encounter icon, etc. */
  @Method(0x800f45f8L)
  public static void loadSmapMedia() {
    if(_800f9eac.get() == -1) {
      _800f9ea8.set(-1);
    }

    //LAB_800f4624
    final int v1 = _800f9ea8.get();
    if(v1 == 0) {
      //LAB_800f4660
      loadMiscTextures(11);
      GPU.queueCommand(1, new GpuCommandCopyVramToVram(992, 288, 984, 288, 8, 64)); // Copies the save point texture beside itself
      _800f9ea8.incr();
      _800f9eac.set(1);
    } else if(v1 == 1) {
      //LAB_800f4650
      //LAB_800f46d8
      _800d4fe8.setu(0);
      initTriangleIndicators();
      initSavePoint();
      FUN_800f0370();
      _800f9ea8.incr();
      _800f9eac.set(2);
    } else if(v1 == -1) {
      //LAB_800f4714
      deallocateSavePoint();
      FUN_800f4420();
      FUN_800f0440();
      _800d4fe8.setu(0);
      _800f9ea8.set(0);
      _800f9eac.set(0);
    }

    //LAB_800f473c
  }

  /**
   * Textures such as footsteps, encounter indicator, yellow &lt;!&gt; sign, save point, etc.
   */
  @Method(0x800f4754L)
  public static void loadMiscTextures(final int textureCount) {
    //LAB_800f47f0
    for(int textureIndex = 0; textureIndex < textureCount; textureIndex++) {
      final TimHeader header = parseTimHeader(_800f9eb0.offset(textureIndex * 0x4L).deref(4).offset(0x4L));
      LoadImage(header.imageRect, header.imageAddress);

      texPages_800d6050.get(textureIndex).set(texPages_800bb110.get(Bpp.values()[header.flags & 0b11]).get(miscTextureTransModes_800d6cf0.get(textureIndex).get()).get(TexPageY.fromY(header.imageRect.y.get())).get() | (header.imageRect.x.get() & 0x3c0) >>> 6);
      cluts_800d6068.get(textureIndex).set(header.clutRect.y.get() << 6 | (header.clutRect.x.get() & 0x3f0) >>> 4);

      LoadImage(header.clutRect, header.clutAddress);
    }

    //LAB_800f48a8
  }
}
