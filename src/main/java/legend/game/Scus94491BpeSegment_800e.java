package legend.game;

import legend.core.DebugHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.Tmd;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.game.combat.Bttl_800c;
import legend.game.combat.Bttl_800d;
import legend.game.combat.Bttl_800e;
import legend.game.combat.Bttl_800f;
import legend.game.combat.SEffe;
import legend.game.types.BigStruct;
import legend.game.types.ExtendedTmd;
import legend.game.types.RunningScript;
import legend.game.types.TmdAnimationFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SInit.executeSInitLoadingStage;
import static legend.game.Scus94491.decompress;
import static legend.game.Scus94491BpeSegment.FUN_80019500;
import static legend.game.Scus94491BpeSegment._1f8003c0;
import static legend.game.Scus94491BpeSegment._1f8003c4;
import static legend.game.Scus94491BpeSegment._1f8003c8;
import static legend.game.Scus94491BpeSegment._1f8003cc;
import static legend.game.Scus94491BpeSegment._1f8003fc;
import static legend.game.Scus94491BpeSegment._80010004;
import static legend.game.Scus94491BpeSegment._8011e210;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.allocateLinkedList;
import static legend.game.Scus94491BpeSegment.drawTim;
import static legend.game.Scus94491BpeSegment.extendedTmd_800103d0;
import static legend.game.Scus94491BpeSegment.gameLoop;
import static legend.game.Scus94491BpeSegment.isStackPointerModified_1f8003bc;
import static legend.game.Scus94491BpeSegment.loadDRGN0_mrg_62802_sounds;
import static legend.game.Scus94491BpeSegment.loadSceaLogo;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.ovalBlobTimHeader_80010548;
import static legend.game.Scus94491BpeSegment.processControllerInput;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.tmdAnimFile_8001051c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c008;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002db2c;
import static legend.game.Scus94491BpeSegment_8002.SetMem;
import static legend.game.Scus94491BpeSegment_8002._bu_init;
import static legend.game.Scus94491BpeSegment_8002.initMemcard;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadBasicUiTexturesAndSomethingElse;
import static legend.game.Scus94491BpeSegment_8002.loadDRGN2xBIN;
import static legend.game.Scus94491BpeSegment_8002.setCdMix;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003429c;
import static legend.game.Scus94491BpeSegment_8003.FUN_80036f20;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c5e0;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsDefDispBuff;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsInitGraph;
import static legend.game.Scus94491BpeSegment_8003.InitGeom;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.ResetCallback;
import static legend.game.Scus94491BpeSegment_8003.ResetGraph;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.SetGraphDebug;
import static legend.game.Scus94491BpeSegment_8003.SetTmr0InterruptCallback;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.handleCdromDmaTimeout;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.resetCdromStuff;
import static legend.game.Scus94491BpeSegment_8003.set80053498;
import static legend.game.Scus94491BpeSegment_8003.setCdDebug;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004.enableAudioSource;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20;
import static legend.game.Scus94491BpeSegment_8004.registerJoypadVblankIrqHandler;
import static legend.game.Scus94491BpeSegment_8004.scriptSubFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8005._8005a398;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bb228;
import static legend.game.Scus94491BpeSegment_800b._800bb348;
import static legend.game.Scus94491BpeSegment_800b._800bd7c0;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bd9f8;
import static legend.game.Scus94491BpeSegment_800b._800bdb38;
import static legend.game.Scus94491BpeSegment_800b._800bdb90;
import static legend.game.Scus94491BpeSegment_800b._800bdc24;
import static legend.game.Scus94491BpeSegment_800b._800bdc40;
import static legend.game.Scus94491BpeSegment_800b._800bf0b4;
import static legend.game.Scus94491BpeSegment_800b._800bf0cd;
import static legend.game.Scus94491BpeSegment_800b._800bf0ce;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b._800bf0d0;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0dc;
import static legend.game.Scus94491BpeSegment_800b._800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.array_800bb198;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdc5c;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.scriptState_800bc0c0;
import static legend.game.Scus94491BpeSegment_800c.SInitOvlData_800c66a4;
import static legend.game.Scus94491BpeSegment_800c.SInitOvlFileName_800c66ac;
import static legend.game.Scus94491BpeSegment_800c._800c6740;
import static legend.game.Scus94491BpeSegment_800c.fileSInitOvl_800c668c;
import static legend.game.Scus94491BpeSegment_800c.sceaLogoAlpha_800c6734;
import static legend.game.Scus94491BpeSegment_800c.sceaLogoDisplayTime_800c6730;
import static legend.game.Scus94491BpeSegment_800c.sceaLogoTextureLoaded_800c672c;
import static legend.game.Scus94491BpeSegment_800c.scriptSubFunction_800ca734;
import static legend.game.Scus94491BpeSegment_800c.timHeader_800c6748;

public final class Scus94491BpeSegment_800e {
  private Scus94491BpeSegment_800e() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_800e.class);

  public static final Value ramSize_800e6f04 = MEMORY.ref(4, 0x800e6f04L);
  public static final Value stackSize_800e6f08 = MEMORY.ref(4, 0x800e6f08L);
  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment_800e#initSceaLogo()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#incrementSceaLogoAlpha()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#findSInitOvl()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#loadSInitOvl()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#loadDrgnBin()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#executeSInitLoadingStages()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#checkForSceaLogoTimeout()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#decrementSceaLogoAlpha()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#nextLoadingStage()}</li>
   *   <li>{@link Scus94491BpeSegment_800e#loadSoundsAndChangeVideoMode()}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<RunnableRef>> pregameLoadingStages_800e6f0c = MEMORY.ref(4, 0x800e6f0cL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 17, 4, Pointer.of(4, RunnableRef::new)));

  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 736</p>
   *
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d3090}</li>
   *   <li>{@link Bttl_800c#FUN_800cec8c}</li>
   *   <li>{@link Bttl_800c#FUN_800cee50}</li>
   *   <li>{@link Bttl_800c#FUN_800ceecc}</li>
   *   <li>{@link Bttl_800d#FUN_800d3098}</li>
   *   <li>{@link Bttl_800d#FUN_800d30a0}</li>
   *   <li>{@link Bttl_800d#FUN_800d30a8}</li>
   *   <li>{@link Bttl_800d#FUN_800d30b0}</li>
   *   <li>{@link Bttl_800c#FUN_800cef00}</li>
   *   <li>{@link Bttl_800c#FUN_800cf0b4}</li>
   *   <li>{@link SEffe#FUN_80102088}</li>
   *   <li>{@link SEffe#FUN_80102364}</li>
   *   <li>{@link Bttl_800d#FUN_800d30b8}</li>
   *   <li>{@link Bttl_800d#FUN_800d0564}</li>
   *   <li>{@link Bttl_800d#FUN_800d09b8}</li>
   *   <li>{@link Bttl_800d#FUN_800d0dec}</li>
   *   <li>{@link SEffe#FUN_80102608}</li>
   *   <li>{@link SEffe#FUN_801077e8}</li>
   *   <li>{@link SEffe#FUN_801077bc}</li>
   *   <li>{@link SEffe#FUN_80108de8}</li>
   *   <li>{@link Bttl_800d#FUN_800d19ec}</li>
   *   <li>{@link Bttl_800d#FUN_800d1cac}</li>
   *   <li>{@link Bttl_800d#FUN_800d1cf4}</li>
   *   <li>{@link SEffe#FUN_801078c0}</li>
   *   <li>{@link SEffe#FUN_80108df0}</li>
   *   <li>{@link Bttl_800d#FUN_800d2ff4}</li>
   *   <li>{@link Bttl_800c#FUN_800ce6a8}</li>
   *   <li>{@link Bttl_800d#FUN_800d2734}</li>
   *   <li>{@link Bttl_800d#FUN_800d3d74}</li>
   *   <li>{@link Bttl_800c#FUN_800cfccc}</li>
   *   <li>{@link Bttl_800d#FUN_800d4338}</li>
   *   <li>{@link Bttl_800d#FUN_800d4580}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e6f64 = MEMORY.ref(4, 0x800e6f64L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 832</p>
   *
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d46d4}</li>
   *   <li>{@link SEffe#FUN_80108df8}</li>
   *   <li>{@link SEffe#FUN_80102610}</li>
   *   <li>{@link Bttl_800c#FUN_800cfdf8}</li>
   *   <li>{@link SEffe#FUN_80109158}</li>
   *   <li>{@link Bttl_800c#FUN_800ce9b0}</li>
   *   <li>{@link SEffe#FUN_801052dc}</li>
   *   <li>{@link SEffe#FUN_80105604}</li>
   *   <li>{@link SEffe#FUN_801087f8}</li>
   *   <li>{@link SEffe#FUN_80105c38}</li>
   *   <li>{@link Bttl_800c#FUN_800c6968}</li>
   *   <li>{@link SEffe#FUN_80109a7c}</li>
   *   <li>{@link SEffe#FUN_801089cc}</li>
   *   <li>{@link SEffe#FUN_801023f4}</li>
   *   <li>{@link Bttl_800c#FUN_800cfec8}</li>
   *   <li>{@link SEffe#FUN_801023fc}</li>
   *   <li>{@link SEffe#FUN_8010246c}</li>
   *   <li>{@link Bttl_800c#FUN_800cff24}</li>
   *   <li>{@link SEffe#FUN_80109d30}</li>
   *   <li>{@link SEffe#FUN_8010a3fc}</li>
   *   <li>{@link Bttl_800d#FUN_800d34bc}</li>
   *   <li>{@link Bttl_800d#FUN_800d0124}</li>
   *   <li>{@link SEffe#FUN_801079a4}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e6fe4 = MEMORY.ref(4, 0x800e6fe4L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 23, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 896</p>
   *
   * <ol start="0">
   *   <li>{@link SEffe#FUN_8010a610}</li>
   *   <li>{@link SEffe#FUN_8010b1d8}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7040 = MEMORY.ref(4, 0x800e7040L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 2, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 32</p>
   *
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dabcc}</li>
   *   <li>{@link Bttl_800d#FUN_800dac20}</li>
   *   <li>{@link Bttl_800d#FUN_800db034}</li>
   *   <li>{@link Bttl_800d#FUN_800db460}</li>
   *   <li>{@link Bttl_800d#FUN_800db574}</li>
   *   <li>{@link Bttl_800d#FUN_800db688}</li>
   *   <li>{@link Bttl_800d#FUN_800db79c}</li>
   *   <li>{@link Bttl_800d#FUN_800db8b0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9e0}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb10}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2d8}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb9c}</li>
   *   <li>{@link Bttl_800d#FUN_800dcbec}</li>
   *   <li>{@link Bttl_800d#FUN_800dcb84}</li>
   *   <li>{@link Bttl_800d#scriptSetViewportTwist}</li>
   *   <li>{@link Bttl_800d#FUN_800dbc80}</li>
   *   <li>{@link Bttl_800d#FUN_800dbcc8}</li>
   *   <li>{@link Bttl_800d#scriptGetProjectionPlaneDistance}</li>
   *   <li>{@link Bttl_800d#FUN_800d8dec}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7048 = MEMORY.ref(4, 0x800e7048L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 19, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 224</p>
   *
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment#FUN_8001e640}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001e918}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001e920}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001eb30}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001eccc}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001f070}</li>
   *   <li>{@link Scus94491BpeSegment#scriptLoadMusicPackage}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001fe28}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ffdc}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8002013c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80020230}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_800202a4}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ab34}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ab98}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001abd0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ac48}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ad5c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001adc8}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ae18}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ae68}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001aec8}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001af34}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001afa4}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b014}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b094}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b134}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b13c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b144}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b14c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b17c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b208}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b27c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7094 = MEMORY.ref(4, 0x800e7094L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 704</p>
   *
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment#FUN_8001b2ac}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b310}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b33c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b3a0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b0f0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b118}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ffc0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001b1ec}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001ac88}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001acd8}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80020060}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001f250}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_800203f0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001f674}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001f560}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7114 = MEMORY.ref(4, 0x800e7114L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 15, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 800</p>
   *
   * <ol start="0">
   *   <li>{@link SEffe#FUN_8010c378}</li>
   *   <li>{@link SEffe#FUN_8010d1dc}</li>
   *   <li>{@link SEffe#FUN_8010d7dc}</li>
   *   <li>{@link SEffe#FUN_8010e04c}</li>
   *   <li>{@link SEffe#FUN_8010edc8}</li>
   *   <li>{@link SEffe#FUN_8010e89c}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001c5fc}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_8001c604}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7150 = MEMORY.ref(4, 0x800e7150L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 8, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 416</p>
   *
   * <ol start="0">
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>{@link Bttl_800e#FUN_800e473c}</li>
   *   <li>{@link Bttl_800e#FUN_800e4788}</li>
   *   <li>{@link Bttl_800e#FUN_800e47c8}</li>
   *   <li>{@link Bttl_800e#FUN_800e48a8}</li>
   *   <li>{@link Bttl_800e#FUN_800e48e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e4964}</li>
   *   <li>{@link Bttl_800e#FUN_800e4abc}</li>
   *   <li>{@link Bttl_800e#FUN_800e4c10}</li>
   *   <li>{@link Bttl_800e#FUN_800e4c90}</li>
   *   <li>{@link Bttl_800e#FUN_800e4d2c}</li>
   *   <li>{@link Bttl_800e#FUN_800e4db4}</li>
   *   <li>{@link Bttl_800e#FUN_800e4dfc}</li>
   *   <li>{@link Bttl_800e#FUN_800e4e2c}</li>
   *   <li>{@link Bttl_800e#FUN_800e4e64}</li>
   *   <li>{@link Bttl_800e#FUN_800e4ea0}</li>
   *   <li>{@link Bttl_800e#FUN_800e4fa0}</li>
   *   <li>{@link Bttl_800e#FUN_800e50e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e52f8}</li>
   *   <li>{@link Bttl_800e#FUN_800e540c}</li>
   *   <li>{@link Bttl_800e#FUN_800e54f8}</li>
   *   <li>{@link Bttl_800e#FUN_800e5528}</li>
   *   <li>{@link Bttl_800e#FUN_800e5560}</li>
   *   <li>{@link Bttl_800e#FUN_800e559c}</li>
   *   <li>{@link Bttl_800e#FUN_800e569c}</li>
   *   <li>{@link Bttl_800e#FUN_800e596c}</li>
   *   <li>{@link Bttl_800e#FUN_800e59d8}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7170 = MEMORY.ref(4, 0x800e7170L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 544</p>
   *
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801115ec}</li>
   *   <li>{@link SEffe#FUN_80111ae4}</li>
   *   <li>{@link SEffe#FUN_80112704}</li>
   *   <li>{@link SEffe#FUN_80112770}</li>
   *   <li>{@link SEffe#FUN_80113964}</li>
   *   <li>{@link SEffe#FUN_801139d0}</li>
   *   <li>{@link SEffe#FUN_8011452c}</li>
   *   <li>{@link SEffe#FUN_80114598}</li>
   *   <li>{@link SEffe#FUN_80114e0c}</li>
   *   <li>{@link SEffe#FUN_80114e60}</li>
   *   <li>{@link SEffe#FUN_80111b60}</li>
   *   <li>{@link SEffe#FUN_8011554c}</li>
   *   <li>{@link SEffe#FUN_801155a0}</li>
   *   <li>{@link SEffe#FUN_80111be8}</li>
   *   <li>{@link SEffe#FUN_80111c2c}</li>
   *   <li>{@link SEffe#FUN_80112184}</li>
   *   <li>{@link SEffe#FUN_80112274}</li>
   *   <li>{@link SEffe#FUN_80112364}</li>
   *   <li>{@link SEffe#FUN_801155f8}</li>
   *   <li>{@link SEffe#FUN_80112900}</li>
   *   <li>{@link SEffe#FUN_8011299c}</li>
   *   <li>{@link SEffe#FUN_80115440}</li>
   *   <li>{@link SEffe#FUN_80111658}</li>
   *   <li>{@link SEffe#FUN_80112aa4}</li>
   *   <li>{@link SEffe#FUN_80112bf0}</li>
   *   <li>{@link SEffe#FUN_80112e00}</li>
   *   <li>{@link SEffe#FUN_8011306c}</li>
   *   <li>{@link SEffe#FUN_801132c8}</li>
   *   <li>{@link SEffe#FUN_80115600}</li>
   *   <li>{@link Bttl_800e#FUN_800e9f68}</li>
   *   <li>{@link SEffe#FUN_80118984}</li>
   *   <li>{@link SEffe#FUN_80113c6c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e71f0 = MEMORY.ref(4, 0x800e71f0L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 576</p>
   *
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80114094}</li>
   *   <li>{@link SEffe#FUN_801143f8}</li>
   *   <li>{@link SEffe#FUN_80115608}</li>
   *   <li>{@link SEffe#FUN_80114070}</li>
   *   <li>{@link SEffe#FUN_801147c8}</li>
   *   <li>{@link SEffe#FUN_80114920}</li>
   *   <li>{@link SEffe#FUN_80114b00}</li>
   *   <li>{@link SEffe#FUN_80114eb4}</li>
   *   <li>{@link SEffe#FUN_80114f34}</li>
   *   <li>{@link SEffe#FUN_80115014}</li>
   *   <li>{@link SEffe#FUN_80115058}</li>
   *   <li>{@link SEffe#FUN_80115168}</li>
   *   <li>{@link SEffe#FUN_801152b0}</li>
   *   <li>{@link SEffe#FUN_80115324}</li>
   *   <li>{@link SEffe#FUN_80115388}</li>
   *   <li>{@link SEffe#FUN_801153e4}</li>
   *   <li>{@link Bttl_800e#FUN_800e74ac}</li>
   *   <li>{@link SEffe#FUN_80112398}</li>
   *   <li>{@link Bttl_800e#FUN_800eb518}</li>
   *   <li>{@link SEffe#FUN_8011549c}</li>
   *   <li>{@link SEffe#FUN_801122ec}</li>
   *   <li>{@link SEffe#FUN_801121fc}</li>
   *   <li>{@link SEffe#FUN_80111cc4}</li>
   *   <li>{@link SEffe#FUN_80111ed4}</li>
   *   <li>{@link Bttl_800e#FUN_800e93e0}</li>
   *   <li>{@link Bttl_800e#FUN_800e96cc}</li>
   *   <li>{@link Bttl_800e#FUN_800e9854}</li>
   *   <li>{@link Bttl_800c#FUN_800ca648}</li>
   *   <li>null</li>
   *   <li>{@link Bttl_800e#FUN_80117eb0}</li>
   *   <li>{@link Bttl_800e#FUN_801183c0}</li>
   *   <li>{@link Bttl_800e#FUN_800e99bc}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7270 = MEMORY.ref(4, 0x800e7270L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 608</p>
   *
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801181a8}</li>
   *   <li>null</li>
   *   <li>{@link Bttl_800e#FUN_800ea200}</li>
   *   <li>{@link SEffe#FUN_801156f8}</li>
   *   <li>{@link Bttl_800e#FUN_800ea13c}</li>
   *   <li>{@link Bttl_800e#FUN_800ea19c}</li>
   *   <li>{@link Bttl_800e#FUN_800eb84c}</li>
   *   <li>{@link Bttl_800e#FUN_800eb188}</li>
   *   <li>{@link Bttl_800e#FUN_800eb01c}</li>
   *   <li>{@link Bttl_800c#FUN_800caae4}</li>
   *   <li>{@link SEffe#FUN_80115690}</li>
   *   <li>{@link SEffe#FUN_80118df4}</li>
   *   <li>{@link SEffe#FUN_80111a58}</li>
   *   <li>{@link Bttl_800e#FUN_800ea384}</li>
   *   <li>{@link SEffe#FUN_80119484}</li>
   *   <li>{@link Bttl_800e#FUN_800e73ac}</li>
   *   <li>{@link Bttl_800e#FUN_800e6db4}</li>
   *   <li>{@link Bttl_800e#FUN_800e7490}</li>
   *   <li>{@link SEffe#FUN_8011574c}</li>
   *   <li>{@link SEffe#FUN_8011578c}</li>
   *   <li>{@link SEffe#FUN_801184e4}</li>
   *   <li>{@link SEffe#FUN_801157d0}</li>
   *   <li>{@link SEffe#FUN_801127e0}</li>
   *   <li>{@link SEffe#FUN_801181f0}</li>
   *   <li>{@link SEffe#FUN_801114b8}</li>
   *   <li>null</li>
   *   <li>{@link SEffe#FUN_80115ab0}</li>
   *   <li>{@link SEffe#FUN_80115a94}</li>
   *   <li>{@link SEffe#FUN_80115a58}</li>
   *   <li>{@link Bttl_800e#FUN_800e7314}</li>
   *   <li>{@link Bttl_800e#FUN_800e71e4}</li>
   *   <li>{@link Bttl_800e#FUN_800e727c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e72f0 = MEMORY.ref(4, 0x800e72f0L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 640</p>
   *
   * <ol start="0">
   *   <li>{@link SEffe#FUN_8011357c}</li>
   *   <li>{@link SEffe#FUN_80115a28}</li>
   *   <li>{@link SEffe#FUN_8011287c}</li>
   *   <li>{@link Bttl_800e#FUN_800e9798}</li>
   *   <li>{@link Bttl_800e#FUN_800ea2a0}</li>
   *   <li>{@link Bttl_800e#FUN_800ea30c}</li>
   *   <li>{@link SEffe#FUN_801188ec}</li>
   *   <li>{@link SEffe#FUN_80115ad8}</li>
   *   <li>{@link SEffe#FUN_80115ea4}</li>
   *   <li>{@link SEffe#FUN_80115ed4}</li>
   *   <li>{@link SEffe#FUN_801154f4}</li>
   *   <li>{@link SEffe#FUN_80116160}</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   *   <li>null</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7370 = MEMORY.ref(4, 0x800e7370L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 96</p>
   *
   * <ol start="0">
   *   <li>{@link SMap#FUN_800df168}</li>
   *   <li>{@link SMap#FUN_800df198}</li>
   *   <li>{@link SMap#FUN_800df1c8}</li>
   *   <li>{@link SMap#FUN_800df1f8}</li>
   *   <li>{@link SMap#FUN_800df228}</li>
   *   <li>{@link SMap#FUN_800df258}</li>
   *   <li>{@link SMap#FUN_800df2b8}</li>
   *   <li>{@link SMap#FUN_800df314}</li>
   *   <li>{@link SMap#FUN_800df374}</li>
   *   <li>{@link SMap#FUN_800df3d0}</li>
   *   <li>{@link SMap#FUN_800df410}</li>
   *   <li>{@link SMap#FUN_800df440}</li>
   *   <li>{@link SMap#FUN_800df488}</li>
   *   <li>{@link SMap#FUN_800df4d0}</li>
   *   <li>{@link SMap#FUN_800df500}</li>
   *   <li>{@link SMap#FUN_800df530}</li>
   *   <li>{@link SMap#FUN_800df560}</li>
   *   <li>{@link SMap#FUN_800df5c0}</li>
   *   <li>{@link SMap#FUN_800df590}</li>
   *   <li>{@link SMap#FUN_800df5f0}</li>
   *   <li>{@link SMap#FUN_800df620}</li>
   *   <li>{@link SMap#FUN_800df650}</li>
   *   <li>{@link SMap#FUN_800df680}</li>
   *   <li>{@link SMap#FUN_800df6a4}</li>
   *   <li>{@link SMap#FUN_800df788}</li>
   *   <li>{@link SMap#FUN_800df890}</li>
   *   <li>{@link SMap#FUN_800df904}</li>
   *   <li>{@link SMap#FUN_800de1d0}</li>
   *   <li>{@link SMap#FUN_800df954}</li>
   *   <li>{@link SMap#FUN_800df9a8}</li>
   *   <li>{@link SMap#FUN_800dfb28}</li>
   *   <li>{@link SMap#FUN_800dfb44}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e73f0 = MEMORY.ref(4, 0x800e73f0L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 288</p>
   *
   * <ol start="0">
   *   <li>{@link SMap#FUN_800dfb74}</li>
   *   <li>{@link SMap#FUN_800dfba4}</li>
   *   <li>{@link SMap#FUN_800dfbd4}</li>
   *   <li>{@link SMap#scriptScaleXyz}</li>
   *   <li>{@link SMap#scriptScaleUniform}</li>
   *   <li>{@link SMap#FUN_800dfca0}</li>
   *   <li>{@link SMap#FUN_800dfcd8}</li>
   *   <li>{@link SMap#FUN_800dfd10}</li>
   *   <li>{@link SMap#FUN_800de334}</li>
   *   <li>{@link SMap#FUN_800de4b4}</li>
   *   <li>{@link SMap#FUN_800dfd8c}</li>
   *   <li>{@link SMap#FUN_800dfdd8}</li>
   *   <li>{@link SMap#FUN_800dfd48}</li>
   *   <li>{@link SMap#FUN_800e05c8}</li>
   *   <li>{@link SMap#FUN_800e05f0}</li>
   *   <li>{@link SMap#FUN_800e0614}</li>
   *   <li>{@link SMap#FUN_800e0684}</li>
   *   <li>{@link SMap#FUN_800e06c4}</li>
   *   <li>{@link SMap#FUN_800e0710}</li>
   *   <li>{@link SMap#FUN_800e0894}</li>
   *   <li>{@link SMap#FUN_800e08f4}</li>
   *   <li>{@link SMap#scriptSetAmbientColour}</li>
   *   <li>{@link SMap#scriptResetAmbientColour}</li>
   *   <li>{@link SMap#FUN_800e09e0}</li>
   *   <li>{@link SMap#FUN_800e0a14}</li>
   *   <li>{@link SMap#FUN_800e0a48}</li>
   *   <li>{@link SMap#FUN_800e0a94}</li>
   *   <li>{@link SMap#FUN_800dee28}</li>
   *   <li>{@link SMap#FUN_800e0af4}</li>
   *   <li>{@link SMap#FUN_800e0b34}</li>
   *   <li>{@link SMap#FUN_800e0ba0}</li>
   *   <li>{@link SMap#FUN_800e0cb8}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7470 = MEMORY.ref(4, 0x800e7470L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 672</p>
   *
   * <ol start="0">
   *   <li>{@link SMap#FUN_800dfe0c}</li>
   *   <li>{@link SMap#FUN_800dfec8}</li>
   *   <li>{@link SMap#FUN_800dff68}</li>
   *   <li>{@link SMap#FUN_800dffa4}</li>
   *   <li>{@link SMap#FUN_800dffdc}</li>
   *   <li>{@link SMap#FUN_800e0018}</li>
   *   <li>{@link SMap#FUN_800e0094}</li>
   *   <li>{@link SMap#FUN_800de668}</li>
   *   <li>{@link SMap#FUN_800de944}</li>
   *   <li>{@link SMap#FUN_800e00cc}</li>
   *   <li>{@link SMap#FUN_800e0148}</li>
   *   <li>{@link SMap#FUN_800e01bc}</li>
   *   <li>{@link SMap#FUN_800e0244}</li>
   *   <li>{@link SMap#FUN_800e0204}</li>
   *   <li>{@link SMap#FUN_800e0284}</li>
   *   <li>{@link SMap#FUN_800e02c0}</li>
   *   <li>{@link SMap#FUN_800e02fc}</li>
   *   <li>{@link SMap#FUN_800deba0}</li>
   *   <li>{@link SMap#FUN_800e03a8}</li>
   *   <li>{@link SMap#FUN_800e03e4}</li>
   *   <li>{@link SMap#FUN_800e0448}</li>
   *   <li>{@link SMap#FUN_800e04b4}</li>
   *   <li>{@link SMap#FUN_800e0520}</li>
   *   <li>{@link SMap#FUN_800e057c}</li>
   *   <li>{@link SMap#FUN_800e074c}</li>
   *   <li>{@link SMap#FUN_800e07f0}</li>
   *   <li>{@link SMap#FUN_800e0184}</li>
   *   <li>{@link SMap#FUN_800e0c40}</li>
   *   <li>{@link SMap#FUN_800e0c80}</li>
   *   <li>{@link SMap#FUN_800e0c00}</li>
   *   <li>{@link SMap#FUN_800e0c24}</li>
   *   <li>{@link SMap#FUN_800e0c9c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e74f0 = MEMORY.ref(4, 0x800e74f0L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 32, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 512</p>
   *
   * <ol start="0">
   *   <li>{@link Bttl_800e#FUN_800ee2ac}</li>
   *   <li>{@link Bttl_800e#FUN_800ee2e4}</li>
   *   <li>{@link Bttl_800e#FUN_800ee324}</li>
   *   <li>{@link Bttl_800e#FUN_800ee384}</li>
   *   <li>{@link Bttl_800e#FUN_800ee468}</li>
   *   <li>{@link Bttl_800e#FUN_800ee49c}</li>
   *   <li>{@link Bttl_800e#FUN_800ee4e8}</li>
   *   <li>{@link Bttl_800e#FUN_800ee548}</li>
   *   <li>{@link Bttl_800e#FUN_800ee384}</li>
   *   <li>{@link Bttl_800e#FUN_800ee574}</li>
   *   <li>{@link Bttl_800e#FUN_800ee594}</li>
   *   <li>{@link Bttl_800e#FUN_800ee5c0}</li>
   *   <li>{@link Bttl_800e#FUN_800ee3c0}</li>
   *   <li>{@link Bttl_800e#FUN_800ee408}</li>
   *   <li>{@link Bttl_800e#FUN_800ee5f0}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7570 = MEMORY.ref(4, 0x800e7570L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 15, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 864</p>
   *
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment_8002#FUN_800244c4}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80024590}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80024480}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e75ac = MEMORY.ref(4, 0x800e75acL, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 3, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 192</p>
   *
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029b68}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029bd4}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80025158}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029c98}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029cf4}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029d34}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80025218}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029e8c}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_800254bc}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029d6c}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029e04}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029ecc}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80028ff8}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029f48}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80029f80}</li>
   *   <li>{@link Scus94491BpeSegment_8002#FUN_80025718}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e75b8 = MEMORY.ref(4, 0x800e75b8L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 16, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 480</p>
   *
   * <ol start="0">
   *   <li>{@link Bttl_800f#FUN_800f95d0}</li>
   *   <li>{@link Bttl_800f#FUN_800f2500}</li>
   *   <li>{@link Bttl_800f#FUN_800f96d4}</li>
   *   <li>{@link Bttl_800f#FUN_800f9730}</li>
   *   <li>{@link Bttl_800f#FUN_800f95d0}</li>
   *   <li>{@link Bttl_800f#FUN_800f95d0}</li>
   *   <li>{@link Bttl_800f#FUN_800f95d0}</li>
   *   <li>{@link Bttl_800f#FUN_800f43dc}</li>
   *   <li>{@link Bttl_800f#FUN_800f4518}</li>
   *   <li>{@link Bttl_800f#FUN_800f97d8}</li>
   *   <li>{@link Bttl_800f#FUN_800f4600}</li>
   *   <li>{@link Bttl_800f#FUN_800f480c}</li>
   *   <li>{@link Bttl_800f#FUN_800f2694}</li>
   *   <li>{@link Bttl_800f#FUN_800f96a8}</li>
   *   <li>{@link Bttl_800f#FUN_800f984c}</li>
   *   <li>{@link Bttl_800f#FUN_800f2838}</li>
   *   <li>{@link Bttl_800f#FUN_800f9884}</li>
   *   <li>{@link Bttl_800f#FUN_800f98b0}</li>
   *   <li>{@link Bttl_800f#FUN_800f99ec}</li>
   *   <li>{@link Bttl_800f#FUN_800f9a50}</li>
   *   <li>{@link Bttl_800f#FUN_800f9b2c}</li>
   *   <li>{@link Bttl_800f#FUN_800f9b78}</li>
   *   <li>{@link Bttl_800f#FUN_800f9b94}</li>
   *   <li>{@link Bttl_800f#FUN_800f9bd4}</li>
   *   <li>{@link Bttl_800f#FUN_800f9c00}</li>
   *   <li>{@link Bttl_800f#FUN_800f9c2c}</li>
   *   <li>{@link Bttl_800f#FUN_800f9cac}</li>
   *   <li>{@link Bttl_800f#FUN_800f9618}</li>
   *   <li>{@link Bttl_800f#FUN_800f9660}</li>
   *   <li>{@link Bttl_800f#FUN_800f9d7c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e75f8 = MEMORY.ref(4, 0x800e75f8L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 30, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 256</p>
   *
   * <ol start="0">
   *   <li>{@link SMap#FUN_800e67d4}</li>
   *   <li>{@link SMap#FUN_800e68b4}</li>
   *   <li>{@link SMap#FUN_800e6904}</li>
   *   <li>{@link SMap#FUN_800e69a4}</li>
   *   <li>{@link SMap#FUN_800e69e8}</li>
   *   <li>{@link SMap#FUN_800e69f0}</li>
   *   <li>{@link SMap#FUN_800e6a28}</li>
   *   <li>{@link SMap#FUN_800e6a64}</li>
   *   <li>{@link SMap#FUN_800e683c}</li>
   *   <li>{@link SMap#FUN_800e6af0}</li>
   *   <li>{@link SMap#FUN_800e6aa0}</li>
   *   <li>{@link SMap#FUN_800e6b64}</li>
   *   <li>{@link SMap#FUN_800e6bd8}</li>
   *   <li>{@link SMap#FUN_800e6be0}</li>
   *   <li>{@link SMap#FUN_800e6cac}</li>
   *   <li>{@link SMap#FUN_800e6ce0}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e7670 = MEMORY.ref(4, 0x800e7670L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 16, 4, Pointer.deferred(4, FunctionRef::new)));
  /**
   * <p>Copied to {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} at index 768</p>
   *
   * <ol start="0">
   *   <li>{@link SMap#FUN_800f2048}</li>
   *   <li>{@link SMap#FUN_800f1f9c}</li>
   *   <li>{@link SMap#FUN_800f1060}</li>
   *   <li>{@link SMap#FUN_800f2264}</li>
   *   <li>{@link SMap#FUN_800f179c}</li>
   *   <li>{@link SMap#FUN_800f23ec}</li>
   *   <li>{@link SMap#FUN_800f2780}</li>
   *   <li>{@link SMap#FUN_800f2090}</li>
   *   <li>{@link SMap#FUN_800f2198}</li>
   *   <li>{@link SMap#FUN_800f1eb8}</li>
   *   <li>{@link SMap#FUN_800f2618}</li>
   *   <li>{@link SMap#FUN_800f1b64}</li>
   *   <li>{@link SMap#FUN_800f26c8}</li>
   *   <li>{@link SMap#FUN_800f1d0c}</li>
   *   <li>{@link SMap#FUN_800f14f0}</li>
   *   <li>{@link SMap#FUN_800f24d8}</li>
   *   <li>{@link SMap#FUN_800f24b0}</li>
   *   <li>{@link SMap#FUN_800f23a0}</li>
   *   <li>{@link SMap#FUN_800f1634}</li>
   *   <li>{@link SMap#FUN_800f22c4}</li>
   *   <li>{@link SMap#FUN_800f2554}</li>
   *   <li>{@link SMap#FUN_800f25a8}</li>
   *   <li>{@link SMap#FUN_800f1274}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<FunctionRef<RunningScript, Long>>> scriptSubFunctions_800e76b0 = MEMORY.ref(4, 0x800e76b0L, ArrayRef.of(Pointer.classFor(FunctionRef.classFor(RunningScript.class, Long.class)), 23, 4, Pointer.deferred(4, FunctionRef::new)));

  @Method(0x800e5d44L)
  public static void main() {
    FUN_800e5d64();
    gameLoop();
  }

  @Method(0x800e5d64L) //TODO can rename most of these functions
  public static void FUN_800e5d64() {
    ResetCallback();
    SetMem(2);

    isStackPointerModified_1f8003bc.set(false);

    initMemcard(false);
    FUN_8002db2c();
    registerJoypadVblankIrqHandler();

    _bu_init();

    FUN_8002c008();
    VSync(0);
    SetDispMask(0);
    ResetGraph(0);
    SetGraphDebug(2);

//    ClearImage(new RECT((short)0, (short)0, (short)640, (short)512), (byte)0, (byte)0, (byte)0);
//    ClearImage(new RECT((short)640, (short)0, (short)384, (short)512), (byte)0, (byte)0, (byte)0);
    DrawSync(0);
    VSync(0);

    GsInitGraph((short)640, (short)480, 0b110101, true, false);
    GsDefDispBuff((short)0, (short)16, (short)0, (short)16);

    orderingTables_8005a370.get(0).length_00.set(0xeL);
    orderingTables_8005a370.get(0).org_04.set(_8005a398.get(0));
    orderingTables_8005a370.get(1).length_00.set(0xeL);
    orderingTables_8005a370.get(1).org_04.set(_8005a398.get(1));

    _1f8003c0.setu(0xeL);
    _1f8003c4.setu(0);
    _1f8003c8.setu(0x4000L);
    _1f8003cc.setu(0x3ffeL);

    FUN_8003c5e0();

    _8007a3a8.setu(0);
    _800bb104.setu(0);
    _800babc0.setu(0);

    InitGeom();
    setProjectionPlaneDistance(640);
    resetCdromStuff();
    set80053498(0x1L);
    FUN_80019500();
    setCdDebug(3); // I think 3 is the most detailed logging

    _8004dd24.setu(0);
    pregameLoadingStage_800bb10c.setu(0);
    vsyncMode_8007a3b8.setu(0x2L);
    _800bb0fc.setu(0);

    processControllerInput();
    FUN_800e60d8();
    loadSystemFont();
    FUN_800e6654();
    allocateLinkedList(_8011e210.getAddress(), 0x3d_edf0L);
    loadOvalBlobTexture();
    FUN_800e6dd4();
    FUN_800e6e3c();
    copyScriptSubFunctions_800e67ac();
    FUN_800e6888();
    FUN_800e6d60();
    FUN_800e670c();
    FUN_800e6ecc();
    FUN_800e6774();
    FUN_800e6e6c();
    SetTmr0InterruptCallback(getMethodAddress(Scus94491BpeSegment.class, "spuTimerInterruptCallback"));
  }

  @Method(0x800e5fc0L)
  public static void finalizePregameLoading() {
    final long loadingStage = pregameLoadingStage_800bb10c.get();
    if(loadingStage == 0) {
      //LAB_800e600c
      loadBasicUiTexturesAndSomethingElse();
      scriptStartEffect(0x1L, 0x1L);
      pregameLoadingStage_800bb10c.addu(1);
    } else if(loadingStage == 0x1L || loadingStage == 0x2L) {
      // Wait for all files to finish loading

      //LAB_800e6028
      if(fileCount_8004ddc8.get() == 0) {
        pregameLoadingStage_800bb10c.addu(1);
        //LAB_800e5ff8
      }
    } else if(loadingStage == 0x3L || loadingStage == 0x4L) {
      pregameLoadingStage_800bb10c.addu(1);
    } else if(loadingStage == 0x5L) {
      //LAB_800e6040
      _8004dd30.setu(0);

      if(drgnBinIndex_800bc058.get() == 0x1L) {
        _800bf0dc.setu(0);
        _800bf0ec.setu(0x2L);
        _8004dd24.setu(0x9L);
      } else {
        //LAB_800e608c
        _8004dd24.setu(mainCallbackIndex_8004dd20.get() + 1);
      }

      pregameLoadingStage_800bb10c.setu(0);
      vsyncMode_8007a3b8.setu(0x2L);

      //LAB_800e60c8
    }
  }

  @Method(0x800e60d8L)
  public static void FUN_800e60d8() {
    for(int s2 = 0; s2 < 3; s2++) {
      for(int s1 = 0; s1 < 5; s1++) {
        _800bb110.offset(s2 * 16L).offset(s1 * 4L).setu(GetTPage(s2, s1, 0, 0));
        _800bb112.offset(s2 * 16L).offset(s1 * 4L).setu(GetTPage(s2, s1, 0, 0x100L));
      }
    }
  }

  @Method(0x800e6184L)
  public static void executePregameLoadingStage() {
    pregameLoadingStages_800e6f0c.get((int)pregameLoadingStage_800bb10c.get()).deref().run();

    if(sceaLogoTextureLoaded_800c672c.get() != 0) {
      drawTim(sceaLogoAlpha_800c6734.get());
    }
  }

  @Method(0x800e61e4L)
  public static void nextLoadingStage() {
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800e61fcL)
  public static void incrementSceaLogoAlpha() {
//    sceaLogoAlpha_800c6734.addu(0x3L);

//    if(sceaLogoAlpha_800c6734.get() > 0x80L) {
      sceaLogoAlpha_800c6734.setu(0x80L);
      pregameLoadingStage_800bb10c.addu(0x1L);
//    }
  }

  @Method(0x800e6238L)
  public static void decrementSceaLogoAlpha() {
//    sceaLogoAlpha_800c6734.subu(0x3L);

//    if(sceaLogoAlpha_800c6734.getSigned() < 0) {
      sceaLogoAlpha_800c6734.set(0);
      pregameLoadingStage_800bb10c.add(0x1L);
//    }
  }

  @Method(0x800e626cL)
  public static void checkForSceaLogoTimeout() {
//    if(joypadPress_8007a398.get() != 0 || VSync(-1) - sceaLogoDisplayTime_800c6730.get() > 210L) {
      pregameLoadingStage_800bb10c.add(0x1L);
//    }
  }

  @Method(0x800e62ccL)
  public static void initSceaLogo() {
    loadSceaLogo();
    scriptStartEffect(0x2L, 0x1L);
    sceaLogoTextureLoaded_800c672c.setu(0x1L);
    sceaLogoDisplayTime_800c6730.setu(VSync(-1));
    pregameLoadingStage_800bb10c.addu(0x1L);
    sceaLogoAlpha_800c6734.setu(0);
  }

  @Method(0x800e6328L)
  public static void findSInitOvl() {
    SInitOvlFileName_800c66ac.set(String.format("%s%s;1", _800c6740.getString(), "\\OVL\\S_INIT.OV_"));

    //LAB_800e635c
    while(FUN_80036f20() != 0x1L) {
      DebugHelper.sleep(1);
    }

    if(DsSearchFile(fileSInitOvl_800c668c, SInitOvlFileName_800c66ac.getString()) == null) {
      return;
    }

    SInitOvlData_800c66a4.setu(addToLinkedListHead(fileSInitOvl_800c668c.size.get() + 0x800L));
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800e63c0L)
  public static void loadSInitOvl() {
    final CdlFILE file = fileSInitOvl_800c668c;

    LOGGER.info("Reading file %s...", file.name.get());

    final int numberOfSectors = (int)((file.size.get() + 0x7ffL) / 0x800L);
    CDROM.readFromDisk(file.pos, numberOfSectors, SInitOvlData_800c66a4.get());
    FUN_8003429c(0);
    handleCdromDmaTimeout(1);

    decompress(SInitOvlData_800c66a4.get(), _80010004.get());
    removeFromLinkedList(SInitOvlData_800c66a4.get());
    pregameLoadingStage_800bb10c.addu(0x1L);

    MEMORY.addFunctions(SInit.class);

    // Pre-optimisation
//    final long numberOfSectors = (file.size + 0x7ffL) / 0x800L;
//    if(startCdromDmaTransfer(file.pos, numberOfSectors, SInitOvlData_800c66a4.get(), new CdlMODE().doubleSpeed()) != 0) {
//      long remainingDmaTransfers;
//
//      //LAB_800e63f8
//      do {
//        remainingDmaTransfers = FUN_80035a30(sp + 0x10L);
//
//        try {
//          Thread.sleep(1);
//        } catch(final InterruptedException ignored) { }
//      } while(remainingDmaTransfers > 0);
//
//      if(remainingDmaTransfers == 0) {
//        FUN_80017c44(0, SInitOvlData_800c66a4.get(), _80010004.get());
//        removeFromLinkedList(SInitOvlData_800c66a4.getAddress());
//        loadingStage_800bb10c.addu(0x1L);
//      }
//    }

    //LAB_800e6448
  }

  @Method(0x800e6458L)
  public static void loadDrgnBin() {
    final long drgnFileIndex = loadDRGN2xBIN();

    if(drgnFileIndex >= 0) {
      drgnBinIndex_800bc058.set(drgnFileIndex);
      pregameLoadingStage_800bb10c.addu(0x1L);
    }
  }

  @Method(0x800e6498L)
  public static void executeSInitLoadingStages() {
    if(executeSInitLoadingStage(0x7fff_ffffL) != 0) {
      pregameLoadingStage_800bb10c.addu(0x1L);
    }
  }

  @Method(0x800e64d4L)
  public static void loadSoundsAndChangeVideoMode() {
    loadDRGN0_mrg_62802_sounds();
    setWidthAndFlags(320L, 0);

    pregameLoadingStage_800bb10c.setu(0);
    vsyncMode_8007a3b8.setu(0x2L);
    _8004dd24.setu(mainCallbackIndex_8004dd20).addu(0x1L);
  }

  @Method(0x800e6524L)
  public static void loadSystemFont() {
    final TimHeader header = parseTimHeader(timHeader_800c6748);

    final RECT imageRect = new RECT((short)832, (short)424, (short)64, (short)56);
    LoadImage(imageRect, header.getImageAddress());

    _800bb348.setu(_800bb112).oru(0xdL);

    if(header.hasClut()) {
      final RECT clutRect = new RECT((short)832, (short)422, (short)32, (short)1);
      LoadImage(clutRect, header.getClutAddress());
    }

    //LAB_800e65c4
    DrawSync(0);
    _1f8003fc.setu(_800bb228.getAddress());

    //LAB_800e65e8
    for(int i = 2; i < 37; i++) {
      long v1 = 0xffff_ffffL;
      long a1 = 0x1L;

      //LAB_800e65fc
      while(v1 >= i) {
        a1 *= i;
        v1 /= i;
      }

      //LAB_800e6620
      array_800bb198.get(i - 2).set(a1);
    }
  }

  @Method(0x800e6654L)
  public static void FUN_800e6654() {
    //LAB_800e666c
    for(int i = 0; i < 0x48; i++) {
      scriptStatePtrArr_800bc1c0.get(i).set(scriptState_800bc0c0);
    }
  }

  @Method(0x800e670cL)
  public static void FUN_800e670c() {
    //LAB_800e6720
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(736 + i).set(scriptSubFunctions_800e6f64.get(i).deref());
    }

    //LAB_800e6750
    for(int i = 0; i < 23; i++) {
      scriptSubFunctions_8004e29c.get(832 + i).set(scriptSubFunctions_800e6fe4.get(i).deref());
    }
  }

  @Method(0x800e6774L)
  public static void FUN_800e6774() {
    //LAB_800e6788
    for(int i = 0; i < 2; i++) {
      scriptSubFunctions_8004e29c.get(896 + i).set(scriptSubFunctions_800e7040.get(i).deref());
    }
  }

  @Method(0x800e67acL)
  private static void copyScriptSubFunctions_800e67ac() {
    //LAB_800e67c0
    for(int i = 0; i < 19; i++) {
      scriptSubFunctions_8004e29c.get(32 + i).set(scriptSubFunctions_800e7048.get(i).deref());
    }

    //LAB_800e67f0
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(224 + i).set(scriptSubFunctions_800e7094.get(i).deref());
    }

    //LAB_800e6820
    for(int i = 0; i < 15; i++) {
      scriptSubFunctions_8004e29c.get(704 + i).set(scriptSubFunctions_800e7114.get(i).deref());
    }

    //LAB_800e683c
    //LAB_800e6850
    for(int i = 0; i < 8; i++) {
      scriptSubFunctions_8004e29c.get(800 + i).set(scriptSubFunctions_800e7150.get(i).deref());
    }
  }

  @Method(0x800e6874L)
  public static void FUN_800e6874() {
    scriptSubFunctions_8004e29c.get(445).set(scriptSubFunction_800ca734);
  }

  @Method(0x800e6888L)
  public static void FUN_800e6888() {
    //LAB_800e68a4
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(416 + i).setNullable(scriptSubFunctions_800e7170.get(i).derefNullable());
    }

    //LAB_800e68d4
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(544 + i).set(scriptSubFunctions_800e71f0.get(i).deref());
    }

    //LAB_800e6904
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(576 + i).setNullable(scriptSubFunctions_800e7270.get(i).derefNullable());
    }

    //LAB_800e6934
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(608 + i).setNullable(scriptSubFunctions_800e72f0.get(i).derefNullable());
    }

    //LAB_800e6964
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(640 + i).setNullable(scriptSubFunctions_800e7370.get(i).derefNullable());
    }

    FUN_800e6874();
  }

  @Method(0x800e6998L)
  public static void loadOvalBlobTexture() {
    //LAB_800e69b8
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(96 + i).set(scriptSubFunctions_800e73f0.get(i).deref());
    }

    //LAB_800e69e8
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(288 + i).set(scriptSubFunctions_800e7470.get(i).deref());
    }

    //LAB_800e6a18
    for(int i = 0; i < 15; i++) {
      scriptSubFunctions_8004e29c.get(512 + i).set(scriptSubFunctions_800e7570.get(i).deref());
    }

    //LAB_800e6a48
    for(int i = 0; i < 32; i++) {
      scriptSubFunctions_8004e29c.get(672 + i).set(scriptSubFunctions_800e74f0.get(i).deref());
    }

    _800bd808.setu(0);

    final TimHeader header = parseTimHeader(ovalBlobTimHeader_80010548);
    LoadImage(header.getImageRect(), header.getImageAddress());

    if(header.hasClut()) {
      LoadImage(header.getClutRect(), header.getClutAddress());
    }

    //LAB_800e6af0
    DrawSync(0);

    FUN_800e6b3c(bigStruct_800bda10, extendedTmd_800103d0, tmdAnimFile_8001051c);

    bigStruct_800bda10.coord2Param_64.rotate.x.set((short)0);
    bigStruct_800bda10.coord2Param_64.rotate.y.set((short)0);
    bigStruct_800bda10.coord2Param_64.rotate.z.set((short)0);
    bigStruct_800bda10.ub_9d.set(0);
    bigStruct_800bda10.ub_cc.set(0);
  }

  /** Very similar to {@link Scus94491BpeSegment_8002#FUN_80020718(BigStruct, legend.game.types.ExtendedTmd, TmdAnimationFile)} */
  @Method(0x800e6b3cL)
  public static void FUN_800e6b3c(final BigStruct bigStruct, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    final int x = bigStruct.coord2_14.coord.transfer.getX();
    final int y = bigStruct.coord2_14.coord.transfer.getY();
    final int z = bigStruct.coord2_14.coord.transfer.getZ();

    //LAB_800e6b7c
    for(int i = 0; i < 7; i++) {
      bigStruct.aub_ec.get(i).set(0);
    }

    bigStruct.dobj2ArrPtr_00.set(_800bd9f8);
    bigStruct.coord2ArrPtr_04.set(_800bdb38);
    bigStruct.coord2ParamArrPtr_08.set(_800bd7c0);
    bigStruct.count_c8.set((short)tmdAnimFile.count_0c.get());

    final Tmd tmd = extendedTmd.tmdPtr_00.deref().tmd;
    bigStruct.tmd_8c.set(tmd);
    bigStruct.tmdNobj_ca.set((int)tmd.header.nobj.get());
    bigStruct.scaleVector_fc.setPad((int)((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff0000L) >>> 11));

    final long v0 = extendedTmd.ptr_08.get();
    if(v0 == 0) {
      //LAB_800e6c44
      bigStruct.ptr_a8.set(extendedTmd.ptr_08.getAddress());

      //LAB_800e6c54
      for(int i = 0; i < 7; i++) {
        bigStruct.aui_d0.get(i).set(0);
      }
    } else {
      bigStruct.ptr_a8.set(extendedTmd.getAddress() + v0 / 4 * 4);

      //LAB_800e6c00
      for(int i = 0; i < 7; i++) {
        bigStruct.aui_d0.get(i).set(bigStruct.ptr_a8.get() + MEMORY.ref(4, bigStruct.ptr_a8.get()).offset(i * 0x4L).get() / 4 * 4);
        FUN_8002246c(bigStruct, i);
      }
    }

    //LAB_800e6c64
    adjustTmdPointers(bigStruct.tmd_8c.deref());
    initObjTable2(bigStruct.ObjTable_0c, bigStruct.dobj2ArrPtr_00.deref(), bigStruct.coord2ArrPtr_04.deref(), bigStruct.coord2ParamArrPtr_08.deref(), bigStruct.count_c8.get());
    bigStruct.coord2_14.param.set(bigStruct.coord2Param_64);
    GsInitCoordinate2(null, bigStruct.coord2_14);
    FUN_80021ca0(bigStruct.ObjTable_0c, bigStruct.tmd_8c.deref(), bigStruct.coord2_14, bigStruct.count_c8.get(), (short)(bigStruct.tmdNobj_ca.get() + 0x1L));

    bigStruct.us_a0.set((short)0);
    bigStruct.ub_a2.set(0);
    bigStruct.ub_a3.set(0);
    bigStruct.ui_f4.set(0);
    bigStruct.ui_f8.set(0);

    FUN_80021584(bigStruct, tmdAnimFile);

    bigStruct.coord2_14.coord.transfer.setX(x);
    bigStruct.coord2_14.coord.transfer.setY(y);
    bigStruct.coord2_14.coord.transfer.setZ(z);
    bigStruct.ub_cc.set(0);
    bigStruct.scaleVector_fc.setX(0x1000);
    bigStruct.scaleVector_fc.setY(0x1000);
    bigStruct.scaleVector_fc.setZ(0x1000);
    bigStruct.vector_10c.setX(0x1000);
    bigStruct.vector_10c.setY(0x1000);
    bigStruct.vector_10c.setZ(0x1000);
    bigStruct.vector_118.setX(0);
    bigStruct.vector_118.setY(0);
    bigStruct.vector_118.setZ(0);
  }

  @Method(0x800e6d60L)
  public static void FUN_800e6d60() {
    _800bdb90.setu(0);
    _800bdc24.setu(0);
    _800bdc40.setu(0);
    renderablePtr_800bdc5c.clear();
    FUN_800e6d9c();
  }

  @Method(0x800e6d9cL)
  public static void FUN_800e6d9c() {
    for(int i = 0; i < 3; i++) {
      scriptSubFunctions_8004e29c.get(864 + i).set(scriptSubFunctions_800e75ac.get(i).deref());
    }
  }

  @Method(0x800e6dd4L)
  public static void FUN_800e6dd4() {
    //LAB_800e6de8
    for(int i = 0; i < 16; i++) {
      scriptSubFunctions_8004e29c.get(192 + i).set(scriptSubFunctions_800e75b8.get(i).deref());
    }

    //LAB_800e6e18
    for(int i = 0; i < 30; i++) {
      scriptSubFunctions_8004e29c.get(480 + i).set(scriptSubFunctions_800e75f8.get(i).deref());
    }
  }

  @Method(0x800e6e3cL)
  public static void FUN_800e6e3c() {
    memcpy(scriptSubFunctions_8004e29c.get(256).getAddress(), scriptSubFunctions_800e7670.getAddress(), 0x40);
  }

  @Method(0x800e6e6cL)
  public static void FUN_800e6e6c() {
    enableAudioSource(0x1L, 0x1L);
    setCdVolume(0x7f, 0x7f);
    setCdMix(0x3fL);

    _800bf0b4.setu(0);
    _800bf0cd.setu(0);
    _800bf0ce.setu(0x7fL);
    _800bf0cf.setu(0);
    _800bf0d0.setu(0);
    _800bf0d8.setu(0);
  }

  @Method(0x800e6eccL)
  public static void FUN_800e6ecc() {
    //LAB_800e6ee0
    for(int i = 0; i < 23; i++) {
      scriptSubFunctions_8004e29c.get(768 + i).set(scriptSubFunctions_800e76b0.get(i).deref());
    }
  }
}
