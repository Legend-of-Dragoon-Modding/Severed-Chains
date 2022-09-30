package legend.game;

import legend.core.cdrom.CdlFILE;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.SupplierRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.tmd.Renderer;
import legend.game.types.ActiveStatsa0;
import legend.game.types.AnmFile;
import legend.game.types.AnmStruct;
import legend.game.types.BigStruct;
import legend.game.types.BigSubStruct;
import legend.game.types.CharacterData2c;
import legend.game.types.DR_MODE;
import legend.game.types.DR_MOVE;
import legend.game.types.DR_TPAGE;
import legend.game.types.EnvironmentFile;
import legend.game.types.EnvironmentStruct;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsOT;
import legend.game.types.GsRVIEW2;
import legend.game.types.McqHeader;
import legend.game.types.MediumStruct;
import legend.game.types.ModelPartTransforms;
import legend.game.types.MrgEntry;
import legend.game.types.MrgFile;
import legend.game.types.NewRootEntryStruct;
import legend.game.types.NewRootStruct;
import legend.game.types.RunningScript;
import legend.game.types.SMapStruct44;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;
import legend.game.types.SmallerStruct;
import legend.game.types.SomethingStruct;
import legend.game.types.SomethingStruct2;
import legend.game.types.Struct20;
import legend.game.types.Struct34;
import legend.game.types.Struct54;
import legend.game.types.SubmapEncounterData_04;
import legend.game.types.TexPageBpp;
import legend.game.types.TexPageTrans;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import legend.game.types.TmdExtension;
import legend.game.types.UnknownStruct;
import legend.game.types.UnknownStruct2;
import legend.game.types.WeirdTimHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SInit.executeSInitLoadingStage;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001ada0;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001c60c;
import static legend.game.Scus94491BpeSegment.FUN_8001eadc;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._80010544;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.cdName_80011700;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.deallocateScriptAndChildren;
import static legend.game.Scus94491BpeSegment.fillMemory;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadAndRunOverlay;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadMcq;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.orderingTableBits_1f8003c0;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setScriptTicker;
import static legend.game.Scus94491BpeSegment.setScriptRenderer;
import static legend.game.Scus94491BpeSegment.setScriptTempTicker;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020a00;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020b98;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020fe0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021048;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021050;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021058;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021060;
import static legend.game.Scus94491BpeSegment_8002.FUN_800211d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_800217a4;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218f0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022018;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022590;
import static legend.game.Scus94491BpeSegment_8002.FUN_80029e04;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002aa04;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.getTimerValue;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderDobj2;
import static legend.game.Scus94491BpeSegment_8002.srand;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixSV;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.DsNewMedia;
import static legend.game.Scus94491BpeSegment_8003.DsSearchFile;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b8f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b900;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetAmbient;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MargePrim;
import static legend.game.Scus94491BpeSegment_8003.MoveImage;
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.RotTransPersN;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMove;
import static legend.game.Scus94491BpeSegment_8003.SetDrawTPage;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040780;
import static legend.game.Scus94491BpeSegment_8004._8004dd30;
import static legend.game.Scus94491BpeSegment_8004.diskNum_8004ddc0;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.fileLoadingCallbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8005._80050274;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._80052c3c;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._80052c48;
import static legend.game.Scus94491BpeSegment_8005._80052c4c;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.SInitBinLoaded_800bbad0;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc05c;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static legend.game.Scus94491BpeSegment_800b.scriptsTickDisabled_800bc0b8;
import static legend.game.Scus94491BpeSegment_800b._800bd782;
import static legend.game.Scus94491BpeSegment_800b._800bd7b0;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bd818;
import static legend.game.Scus94491BpeSegment_800b._800bda08;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bf0b4;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0dc;
import static legend.game.Scus94491BpeSegment_800b._800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.hasNoEncounters_800bed58;
import static legend.game.Scus94491BpeSegment_800b.loadedDrgnFiles_800bcf78;
import static legend.game.Scus94491BpeSegment_800b.matrix_800bed30;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800b.rview2_800bd7e8;
import static legend.game.Scus94491BpeSegment_800b.screenOffsetX_800bed50;
import static legend.game.Scus94491BpeSegment_800b.screenOffsetY_800bed54;
import static legend.game.Scus94491BpeSegment_800b.scriptEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;

public final class SMap {
  private SMap() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SMap.class);

  public static final Value diskSwapAttempts_800c6690 = MEMORY.ref(4, 0x800c6690L);
  public static final Value S_InitLoaded_800c6694 = MEMORY.ref(4, 0x800c6694L);
  public static final Value diskSwapMcqLoaded_800c6698 = MEMORY.ref(1, 0x800c6698L);

  public static final McqHeader mcq_800c66a0 = MEMORY.ref(4, 0x800c66a0L, McqHeader::new);
  public static final ArrayRef<Pointer<McqHeader>> mcqs_800c66d0 = MEMORY.ref(4, 0x800c66d0L, ArrayRef.of(Pointer.classFor(McqHeader.class), 2, 4, Pointer.deferred(4, McqHeader::new)));
  public static final GsF_LIGHT GsF_LIGHT_0_800c66d8 = MEMORY.ref(4, 0x800c66d8L, GsF_LIGHT::new);
  public static final GsF_LIGHT GsF_LIGHT_1_800c66e8 = MEMORY.ref(4, 0x800c66e8L, GsF_LIGHT::new);
  public static final GsF_LIGHT GsF_LIGHT_2_800c66f8 = MEMORY.ref(4, 0x800c66f8L, GsF_LIGHT::new);
  public static final Value _800c6708 = MEMORY.ref(2, 0x800c6708L);
  public static final Value _800c670a = MEMORY.ref(2, 0x800c670aL);
  public static final Value _800c670c = MEMORY.ref(2, 0x800c670cL);
  public static final Value _800c670e = MEMORY.ref(2, 0x800c670eL);
  public static final Pointer<MrgFile> mrg10Addr_800c6710 = MEMORY.ref(4, 0x800c6710L, Pointer.deferred(4, MrgFile::new));
  public static final Value _800c6714 = MEMORY.ref(4, 0x800c6714L);
  public static final Value _800c6718 = MEMORY.ref(4, 0x800c6718L);
  public static final Value _800c671c = MEMORY.ref(4, 0x800c671cL);
  public static final Value _800c6720 = MEMORY.ref(4, 0x800c6720L);
  public static final Value _800c6724 = MEMORY.ref(4, 0x800c6724L);

  public static final Value _800c672c = MEMORY.ref(4, 0x800c672cL);
  public static final Value scriptCount_800c6730 = MEMORY.ref(4, 0x800c6730L);
  public static final Value _800c6734 = MEMORY.ref(4, 0x800c6734L);
  public static final Value _800c6738 = MEMORY.ref(2, 0x800c6738L);

  public static final Value _800c673c = MEMORY.ref(4, 0x800c673cL);
  public static final IntRef scriptIndex_800c6740 = MEMORY.ref(4, 0x800c6740L, IntRef::new);

  /** TODO some of the values below might overlap this struct (note: data sizes of below vars don't match up with what's in the struct?) */
  public static final BigStruct bigStruct_800c6748 = MEMORY.ref(4, 0x800c6748L, BigStruct::new);

  public static final Value _800c686c = MEMORY.ref(2, 0x800c686cL);
  public static final Value _800c686e = MEMORY.ref(2, 0x800c686eL);
  public static final Value _800c6870 = MEMORY.ref(2, 0x800c6870L);

  public static final Value mrg0Loaded_800c6874 = MEMORY.ref(4, 0x800c6874L);
  public static final Pointer<MrgFile> mrg0Addr_800c6878 = MEMORY.ref(4, 0x800c6878L, Pointer.deferred(4, MrgFile::new));
  public static final Value _800c687c = MEMORY.ref(2, 0x800c687cL);
  public static final Value _800c687e = MEMORY.ref(2, 0x800c687eL);
  /** One index per loaded script */
  public static final ArrayRef<IntRef> scriptStateIndices_800c6880 = MEMORY.ref(4, 0x800c6880L, ArrayRef.of(IntRef.class, 20, 4, IntRef::new));
  public static final Value mrg1Loaded_800c68d0 = MEMORY.ref(4, 0x800c68d0L);

  public static final Pointer<MrgFile> mrg1Addr_800c68d8 = MEMORY.ref(4, 0x800c68d8L, Pointer.deferred(4, MrgFile::new));

  public static final Value mrg10Loaded_800c68e0 = MEMORY.ref(2, 0x800c68e0L);

  public static final Value loadingStage_800c68e4 = MEMORY.ref(4, 0x800c68e4L);

  public static final Value _800c68e8 = MEMORY.ref(4, 0x800c68e8L);

  public static final Value callbackIndex_800c6968 = MEMORY.ref(2, 0x800c6968L);
  public static final Value _800c6970 = MEMORY.ref(4, 0x800c6970L);

  public static final Value _800c69ec = MEMORY.ref(4, 0x800c69ecL);
  public static final Value _800c69f0 = MEMORY.ref(4, 0x800c69f0L);
  public static final Value _800c69f4 = MEMORY.ref(4, 0x800c69f4L);
  public static final Value _800c69f8 = MEMORY.ref(4, 0x800c69f8L);
  public static final Value _800c69fc = MEMORY.ref(4, 0x800c69fcL);
  public static final UnboundedArrayRef<Pointer<ExtendedTmd>> extendedTmdArr_800c6a00 = MEMORY.ref(4, 0x800c6a00L, UnboundedArrayRef.of(4, Pointer.deferred(4, ExtendedTmd::new)));

  /** Written as int, read as bytes? TODO array */
  public static final Value _800c6a50 = MEMORY.ref(4, 0x800c6a50L);

  public static final Value _800c6aa0 = MEMORY.ref(4, 0x800c6aa0L);
  public static final Value _800c6aa4 = MEMORY.ref(4, 0x800c6aa4L);
  public static final Value _800c6aa8 = MEMORY.ref(4, 0x800c6aa8L);
  public static final Value _800c6aac = MEMORY.ref(2, 0x800c6aacL);
  public static final VECTOR prevPlayerPos_800c6ab0 = MEMORY.ref(2, 0x800c6ab0L, VECTOR::new);
  public static final Value encounterMultiplier_800c6abc = MEMORY.ref(4, 0x800c6abcL); // Overlaps previous vector padding
  public static final MATRIX matrix_800c6ac0 = MEMORY.ref(4, 0x800c6ac0L, MATRIX::new);
  public static final Value _800c6ae0 = MEMORY.ref(4, 0x800c6ae0L);
  public static final Value _800c6ae4 = MEMORY.ref(4, 0x800c6ae4L);
  public static final Value encounterAccumulator_800c6ae8 = MEMORY.ref(4, 0x800c6ae8L);
  public static final Pointer<UnknownStruct> _800c6aec = MEMORY.ref(4, 0x800c6aecL, Pointer.deferred(4, UnknownStruct::new));
  /** 14576 bytes - contains the contents of NEWROOT.RDT */
  public static final NewRootStruct newroot_800c6af0 = MEMORY.ref(4, 0x800c6af0L, NewRootStruct::new);

  public static final Value _800caaf0 = MEMORY.ref(4, 0x800caaf0L);
  public static final Value _800caaf4 = MEMORY.ref(4, 0x800caaf4L);
  public static final Value _800caaf8 = MEMORY.ref(4, 0x800caaf8L);
  public static final Value _800caafc = MEMORY.ref(4, 0x800caafcL);
  public static final Value _800cab00 = MEMORY.ref(4, 0x800cab00L);
  public static final Pointer<NewRootStruct> newrootPtr_800cab04 = MEMORY.ref(4, 0x800cab04L, Pointer.deferred(4, NewRootStruct::new));

  public static final Value backgroundLoaded_800cab10 = MEMORY.ref(4, 0x800cab10L);

  public static final Value newrootLoaded_800cab1c = MEMORY.ref(4, 0x800cab1cL);
  public static final Value _800cab20 = MEMORY.ref(4, 0x800cab20L);
  public static final Pointer<MediumStruct> _800cab24 = MEMORY.ref(4, 0x800cab24L, Pointer.deferred(4, MediumStruct::new));
  public static final Value _800cab28 = MEMORY.ref(4, 0x800cab28L);
  public static final Value _800cab2c = MEMORY.ref(4, 0x800cab2cL);
  public static final UnboundedArrayRef<EnvironmentStruct> envStruct_800cab30 = MEMORY.ref(4, 0x800cab30L, UnboundedArrayRef.of(0x24, EnvironmentStruct::new));

  public static final Value smapLoadingStage_800cb430 = MEMORY.ref(4, 0x800cb430L);

  public static final Value _800cb440 = MEMORY.ref(4, 0x800cb440L);

  public static final Value _800cb448 = MEMORY.ref(4, 0x800cb448L);

  public static final Value _800cb450 = MEMORY.ref(4, 0x800cb450L);

  public static final Pointer<NewRootStruct> newRootPtr_800cb458 = MEMORY.ref(4, 0x800cb458L, Pointer.deferred(4, NewRootStruct::new));

  public static final ArrayRef<IntRef> arr_800cb460 = MEMORY.ref(4, 0x800cb460L, ArrayRef.of(IntRef.class, 0x40, 4, IntRef::new));

  public static final Value _800cb560 = MEMORY.ref(4, 0x800cb560L); //TODO something X
  public static final Value _800cb564 = MEMORY.ref(4, 0x800cb564L); //TODO something Y
  public static final Value screenOffsetX_800cb568 = MEMORY.ref(4, 0x800cb568L);
  public static final Value screenOffsetY_800cb56c = MEMORY.ref(4, 0x800cb56cL);
  public static final Value _800cb570 = MEMORY.ref(4, 0x800cb570L);
  public static final Value _800cb574 = MEMORY.ref(4, 0x800cb574L);
  public static final Value _800cb578 = MEMORY.ref(4, 0x800cb578L);
  public static final Value _800cb57c = MEMORY.ref(4, 0x800cb57cL);
  public static final Value _800cb580 = MEMORY.ref(4, 0x800cb580L);
  public static final Value backgroundObjectsCount_800cb584 = MEMORY.ref(4, 0x800cb584L);

  //TODO array of VECTORs?
  public static final Value _800cb590 = MEMORY.ref(4, 0x800cb590L);

  /** Array of 0x24 (tpage packet, then a quad packet, then more data used elsewhere?) TODO */
  public static final Value _800cb710 = MEMORY.ref(1, 0x800cb710L);

  /** TODO svec array */
  public static final Value _800cbb90 = MEMORY.ref(2, 0x800cbb90L);

  public static final Value _800cbc90 = MEMORY.ref(4, 0x800cbc90L);

  public static final GsRVIEW2 rview2_800cbd10 = MEMORY.ref(4, 0x800cbd10L, GsRVIEW2::new);
  public static final Value _800cbd30 = MEMORY.ref(4, 0x800cbd30L);
  public static final Value _800cbd34 = MEMORY.ref(4, 0x800cbd34L);
  public static final Pointer<UnknownStruct2> _800cbd38 = MEMORY.ref(4, 0x800cbd38L, Pointer.deferred(4, UnknownStruct2::new));
  public static final Pointer<UnknownStruct2> _800cbd3c = MEMORY.ref(4, 0x800cbd3cL, Pointer.deferred(4, UnknownStruct2::new));
  public static final MATRIX matrix_800cbd40 = MEMORY.ref(4, 0x800cbd40L, MATRIX::new);
  public static final Value _800cbd60 = MEMORY.ref(4, 0x800cbd60L);
  public static final Value _800cbd64 = MEMORY.ref(4, 0x800cbd64L);
  public static final MATRIX matrix_800cbd68 = MEMORY.ref(4, 0x800cbd68L, MATRIX::new);

  public static final Value _800cbd94 = MEMORY.ref(4, 0x800cbd94L);
  public static final SVECTOR _800cbd98 = MEMORY.ref(2, 0x800cbd98L, SVECTOR::new);
  public static final Value _800cbda0 = MEMORY.ref(2, 0x800cbda0L);

  public static final Value _800cbda4 = MEMORY.ref(4, 0x800cbda4L);
  public static final GsCOORDINATE2 GsCOORDINATE2_800cbda8 = MEMORY.ref(4, 0x800cbda8L, GsCOORDINATE2::new);
  public static final GsDOBJ2 GsDOBJ2_800cbdf8 = MEMORY.ref(4, 0x800cbdf8L, GsDOBJ2::new);
  public static final SomethingStruct SomethingStruct_800cbe08 = MEMORY.ref(4, 0x800cbe08L, SomethingStruct::new);
  public static final Value _800cbe30 = MEMORY.ref(4, 0x800cbe30L);
  public static final Pointer<UnknownStruct2> _800cbe34 = MEMORY.ref(4, 0x800cbe34L, Pointer.deferred(4, UnknownStruct2::new));
  public static final Pointer<UnknownStruct2> _800cbe38 = MEMORY.ref(4, 0x800cbe38L, Pointer.deferred(4, UnknownStruct2::new));

  public static final Value _800cbe48 = MEMORY.ref(4, 0x800cbe48L);

  public static final Value _800cbe68 = MEMORY.ref(4, 0x800cbe68L);

  public static final UnboundedArrayRef<SomethingStruct2> SomethingStruct2Arr_800cbe78 = MEMORY.ref(4, 0x800cbe78L, UnboundedArrayRef.of(0xc, SomethingStruct2::new));

  /** unknown size */
  public static final Value _800cca78 = MEMORY.ref(4, 0x800cca78L);

  public static final TmdWithId tmd_800cfa78 = MEMORY.ref(4, 0x800cfa78L, TmdWithId::new);

  public static final Value _800d1a78 = MEMORY.ref(4, 0x800d1a78L);
  public static final Value _800d1a7c = MEMORY.ref(4, 0x800d1a7cL);
  public static final Value _800d1a80 = MEMORY.ref(4, 0x800d1a80L);
  public static final Value _800d1a84 = MEMORY.ref(4, 0x800d1a84L);

  public static final Pointer<SomethingStruct> SomethingStructPtr_800d1a88 = MEMORY.ref(4, 0x800d1a88L, Pointer.deferred(4, SomethingStruct::new));
  public static final Pointer<UnknownStruct2> _800d1a8c = MEMORY.ref(4, 0x800d1a8cL, Pointer.deferred(4, UnknownStruct2::new));
  public static final MediumStruct _800d1a90 = MEMORY.ref(4, 0x800d1a90L, MediumStruct::new);

  public static final Value creditsLoaded_800d1cb8 = MEMORY.ref(4, 0x800d1cb8L);

  public static final Value _800d1cc0 = MEMORY.ref(4, 0x800d1cc0L);

  public static final ArrayRef<UnsignedIntRef> textureDataPtrArray3_800d4ba0 = MEMORY.ref(4, 0x800d4ba0L, ArrayRef.of(UnsignedIntRef.class, 3, 4, UnsignedIntRef::new));

  public static final MATRIX matrix_800d4bb0 = MEMORY.ref(4, 0x800d4bb0L, MATRIX::new);

  public static final Value _800d4bd0 = MEMORY.ref(4, 0x800d4bd0L);
  public static final Value _800d4bd4 = MEMORY.ref(4, 0x800d4bd4L);

  public static final Value _800d4bdc = MEMORY.ref(4, 0x800d4bdcL);
  public static final Value drgn0_mrg_80428_loaded_800d4be0 = MEMORY.ref(4, 0x800d4be0L);
  public static final Value _800d4be4 = MEMORY.ref(4, 0x800d4be4L);
  public static final Value _800d4be8 = MEMORY.ref(4, 0x800d4be8L);
  /** Contains fog texture and a raw matrix in a file */
  public static final Value drgn0_mrg_80428_address_800d4bec = MEMORY.ref(4, 0x800d4becL);
  public static final Value _800d4bf0 = MEMORY.ref(4, 0x800d4bf0L);

  public static final BigStruct bigStruct_800d4bf8 = MEMORY.ref(4, 0x800d4bf8L, BigStruct::new);

  public static final BigStruct _800d4d40 = MEMORY.ref(4, 0x800d4d40L, BigStruct::new);

  /** TODO part of previous struct? */
  public static final Struct54 _800d4e68 = MEMORY.ref(4, 0x800d4e68L, Struct54::new);

  /** TODO part of previous struct? */
  public static final Struct20 _800d4ec0 = MEMORY.ref(4, 0x800d4ec0L, Struct20::new);
  /** TODO struct */
  public static final Value _800d4ee0 = MEMORY.ref(4, 0x800d4ee0L);

  public static final Struct34 _800d4f18 = MEMORY.ref(4, 0x800d4f18L, Struct34::new);

  public static final Value _800d4f48 = MEMORY.ref(4, 0x800d4f48L);

  /** TODO struct */
  public static final Value _800d4f50 = MEMORY.ref(4, 0x800d4f50L);

  public static final Value _800d4f90 = MEMORY.ref(4, 0x800d4f90L);

  public static final Value _800d4fc0 = MEMORY.ref(4, 0x800d4fc0L);

  public static final Value _800d4fd0 = MEMORY.ref(4, 0x800d4fd0L);

  public static final Value _800d4fe0 = MEMORY.ref(4, 0x800d4fe0L);

  public static final Value _800d4fe8 = MEMORY.ref(2, 0x800d4fe8L);

  public static final Value _800d4ff0 = MEMORY.ref(4, 0x800d4ff0L);

  public static final AnmStruct anmStruct_800d5588 = MEMORY.ref(4, 0x800d5588L, AnmStruct::new);
  public static final AnmStruct anmStruct_800d5590 = MEMORY.ref(4, 0x800d5590L, AnmStruct::new);
  public static final ArrayRef<SMapStruct44> _800d5598 = MEMORY.ref(4, 0x800d5598L, ArrayRef.of(SMapStruct44.class, 2, 0x44, SMapStruct44::new));
  public static final Value _800d5620 = MEMORY.ref(2, 0x800d5620L);

  public static final Value _800d5630 = MEMORY.ref(2, 0x800d5630L);

  public static final BigStruct _800d5eb0 = MEMORY.ref(4, 0x800d5eb0L, BigStruct::new);

  public static final Value _800d5fd8 = MEMORY.ref(4, 0x800d5fd8L);

  public static final Struct34 struct34_800d6018 = MEMORY.ref(4, 0x800d6018L, Struct34::new);

  public static final Value _800d6050 = MEMORY.ref(2, 0x800d6050L);
  public static final Value _800d6052 = MEMORY.ref(2, 0x800d6052L);

  public static final Value _800d6068 = MEMORY.ref(2, 0x800d6068L);
  public static final Value _800d606a = MEMORY.ref(2, 0x800d606aL);

  public static final Value _800d6074 = MEMORY.ref(2, 0x800d6074L);

  public static final Value _800d610c = MEMORY.ref(2, 0x800d610cL);

  /** TIM */
  public static final Value _800d673c = MEMORY.ref(4, 0x800d673cL);

  public static final Value timFile_800d689c = MEMORY.ref(4, 0x800d689cL);

  public static final RECT _800d69fc = MEMORY.ref(4, 0x800d69fcL, RECT::new);
  public static final RECT _800d6a04 = MEMORY.ref(4, 0x800d6a04L, RECT::new);

  public static final RECT _800d6b48 = MEMORY.ref(4, 0x800d6b48L, RECT::new);

  public static final ArrayRef<SVECTOR> _800d6b7c = MEMORY.ref(4, 0x800d6b7cL, ArrayRef.of(SVECTOR.class, 12, 8, SVECTOR::new));

  public static final Value _800d6bdc = MEMORY.ref(4, 0x800d6bdcL);
  public static final Value _800d6be0 = MEMORY.ref(4, 0x800d6be0L);
  public static final Value _800d6be4 = MEMORY.ref(4, 0x800d6be4L);
  public static final Value _800d6be8 = MEMORY.ref(4, 0x800d6be8L);
  public static final Value _800d6bec = MEMORY.ref(4, 0x800d6becL);
  public static final Value _800d6bf0 = MEMORY.ref(4, 0x800d6bf0L);
  public static final Value _800d6bf4 = MEMORY.ref(4, 0x800d6bf4L);
  public static final Value _800d6bf8 = MEMORY.ref(4, 0x800d6bf8L);
  public static final Value _800d6bfc = MEMORY.ref(4, 0x800d6bfcL);
  public static final Value _800d6c00 = MEMORY.ref(4, 0x800d6c00L);
  public static final Value _800d6c04 = MEMORY.ref(4, 0x800d6c04L);
  public static final Value _800d6c08 = MEMORY.ref(4, 0x800d6c08L);
  public static final Value _800d6c0c = MEMORY.ref(4, 0x800d6c0cL);
  public static final Value _800d6c10 = MEMORY.ref(4, 0x800d6c10L);
  public static final Value _800d6c14 = MEMORY.ref(4, 0x800d6c14L);
  public static final Value _800d6c18 = MEMORY.ref(4, 0x800d6c18L);

  public static final SVECTOR _800d6c28 = MEMORY.ref(4, 0x800d6c28L, SVECTOR::new);
  public static final SVECTOR _800d6c30 = MEMORY.ref(4, 0x800d6c30L, SVECTOR::new);
  public static final SVECTOR _800d6c38 = MEMORY.ref(4, 0x800d6c38L, SVECTOR::new);
  public static final SVECTOR _800d6c40 = MEMORY.ref(4, 0x800d6c40L, SVECTOR::new);
  public static final SVECTOR _800d6c48 = MEMORY.ref(4, 0x800d6c48L, SVECTOR::new);
  public static final SVECTOR _800d6c50 = MEMORY.ref(4, 0x800d6c50L, SVECTOR::new);

  public static final Value _800d6c58 = MEMORY.ref(4, 0x800d6c58L);
  public static final Value _800d6c5c = MEMORY.ref(4, 0x800d6c5cL);
  public static final Value _800d6c60 = MEMORY.ref(4, 0x800d6c60L);
  public static final Value _800d6c64 = MEMORY.ref(4, 0x800d6c64L);
  public static final Value _800d6c68 = MEMORY.ref(4, 0x800d6c68L);
  public static final Value _800d6c6c = MEMORY.ref(4, 0x800d6c6cL);
  public static final Value _800d6c70 = MEMORY.ref(4, 0x800d6c70L);
  public static final Value _800d6c74 = MEMORY.ref(4, 0x800d6c74L);
  public static final ArrayRef<ShortRef> _800d6c78 = MEMORY.ref(2, 0x800d6c78L, ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));
  public static final ArrayRef<IntRef> _800d6c88 = MEMORY.ref(4, 0x800d6c88L, ArrayRef.of(IntRef.class, 8, 4, IntRef::new));

  public static final Value _800d6ca8 = MEMORY.ref(4, 0x800d6ca8L);
  public static final Value _800d6cac = MEMORY.ref(4, 0x800d6cacL);
  public static final Value _800d6cb0 = MEMORY.ref(4, 0x800d6cb0L);
  public static final Value _800d6cb4 = MEMORY.ref(4, 0x800d6cb4L);
  public static final Value _800d6cb8 = MEMORY.ref(4, 0x800d6cb8L);

  public static final ArrayRef<UnsignedIntRef> _800d6cc8 = MEMORY.ref(4, 0x800d6cc8L, ArrayRef.of(UnsignedIntRef.class, 4, 4, UnsignedIntRef::new));

  public static final ArrayRef<EnumRef<TexPageTrans>> miscTextureTransModes_800d6cf0 = MEMORY.ref(4, 0x800d6cf0L, ArrayRef.of(EnumRef.classFor(TexPageTrans.class), 11, 4, EnumRef.of(TexPageTrans.values())));
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

  public static final ArrayRef<UnsignedShortRef> mcqPleaseInsertDiskN_800f48d0 = MEMORY.ref(2, 0x800f48d0L, ArrayRef.of(UnsignedShortRef.class, 4, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> mcqWrongDisk_800f48d8 = MEMORY.ref(2, 0x800f48d8L, ArrayRef.of(UnsignedShortRef.class, 4, 2, UnsignedShortRef::new));
  public static final ArrayRef<UnsignedShortRef> mcqPleaseWait_800f48e0 = MEMORY.ref(2, 0x800f48e0L, ArrayRef.of(UnsignedShortRef.class, 4, 2, UnsignedShortRef::new));
  /**
   * <ol start="0">
   *   <li>{@link SMap#loadDiskSwapScreen}</li>
   *   <li>{@link SMap#waitForSInitToLoad}</li>
   *   <li>{@link SMap#waitForSInitToLoad}</li>
   *   <li>{@link SMap#stopCdrom}</li>
   *   <li>{@link SMap#FUN_800d96b8}</li>
   *   <li>{@link SMap#isCdromDriveOpen}</li>
   *   <li>{@link SMap#prepareForDiskSwap}</li>
   *   <li>{@link SMap#loadNewDisk}</li>
   *   <li>{@link SMap#checkDiskSwapSuccess}</li>
   *   <li>{@link SMap#FUN_800d98b0}</li>
   *   <li>{@link SMap#FUN_800d992c}</li>
   *   <li>{@link SMap#authenticateDisk}</li>
   *   <li>{@link SMap#FUN_800d9994}</li>
   *   <li>{@link SMap#waitForSInitToLoad}</li>
   *   <li>{@link SMap#waitForSInitToLoad}</li>
   *   <li>{@link SMap#waitForSInitToLoad}</li>
   *   <li>{@link SMap#waitForSInitToLoad}</li>
   *   <li>{@link SMap#FUN_800d99f0}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<SupplierRef<Long>>> diskSwapLoadingStages_800f48e8 = MEMORY.ref(4, 0x800f48e8L, ArrayRef.of(Pointer.classFor(SupplierRef.classFor(Long.class)), 18, 4, Pointer.deferred(4, SupplierRef::new)));
  public static final Value _800f4930 = MEMORY.ref(4, 0x800f4930L);

  /** TODO an array of 0x14-long somethings */
  public static final Value _800f5930 = MEMORY.ref(4, 0x800f5930L);

  /**
   * 65 - {@link SMap#FUN_800eddb4()}
   *
   * All other indices are {@link SMap#FUN_800e4994()}
   */
  public static final ArrayRef<Pointer<RunnableRef>> callbackArr_800f5ad4 = MEMORY.ref(4, 0x800f5ad4L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 0x80, 4, Pointer.deferred(4, RunnableRef::new)));
  public static final Value _800f5cd4 = MEMORY.ref(2, 0x800f5cd4L);

  public static final Value _800f64ac = MEMORY.ref(4, 0x800f64acL);

  public static final Value _800f64b0 = MEMORY.ref(2, 0x800f64b0L);

  public static final Pointer<GsOT> GsOTPtr_800f64c0 = MEMORY.ref(4, 0x800f64c0L, Pointer.deferred(4, GsOT::new));
  /** Indexed by submap cut */
  public static final UnboundedArrayRef<SubmapEncounterData_04> encounterData_800f64c4 = MEMORY.ref(1, 0x800f64c4L, UnboundedArrayRef.of(4, SubmapEncounterData_04::new));

  public static final Value _800f74c4 = MEMORY.ref(1, 0x800f74c4L);

  public static final Value _800f7e24 = MEMORY.ref(4, 0x800f7e24L);
  public static final Pointer<Pointer<UnknownStruct>> _800f7e28 = MEMORY.ref(4, 0x800f7e28L, Pointer.deferred(4, Pointer.deferred(4, UnknownStruct::new)));
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

  public static final ArrayRef<RECT> rectArray3_800f96f4 = MEMORY.ref(2, 0x800f96f4L, ArrayRef.of(RECT.class, 3, 8, RECT::new));

  public static final Value _800f970c = MEMORY.ref(4, 0x800f970cL);

  public static final Value _800f9718 = MEMORY.ref(2, 0x800f9718L);

  public static final UnboundedArrayRef<ShortRef> smapFileIndices_800f982c = MEMORY.ref(2, 0x800f982cL, UnboundedArrayRef.of(2, ShortRef::new));

  public static final Value _800f9e5a = MEMORY.ref(2, 0x800f9e5aL);
  public static final Value _800f9e5c = MEMORY.ref(2, 0x800f9e5cL);
  public static final Value _800f9e5e = MEMORY.ref(2, 0x800f9e5eL);
  public static final Value _800f9e60 = MEMORY.ref(2, 0x800f9e60L);

  public static final Value _800f9e70 = MEMORY.ref(4, 0x800f9e70L);
  public static final Value _800f9e74 = MEMORY.ref(4, 0x800f9e74L);
  public static final Value _800f9e78 = MEMORY.ref(2, 0x800f9e78L);

  public static final Value _800f9e9c = MEMORY.ref(4, 0x800f9e9cL);

  public static final Value _800f9ea0 = MEMORY.ref(2, 0x800f9ea0L);

  public static final Value _800f9ea8 = MEMORY.ref(4, 0x800f9ea8L);
  public static final Value _800f9eac = MEMORY.ref(4, 0x800f9eacL);
  public static final Value _800f9eb0 = MEMORY.ref(4, 0x800f9eb0L);
  
  @Method(0x800d92a0L)
  public static void swapDiskLoadingStage() {
    LOGGER.info("Disk swap loading stage %d", pregameLoadingStage_800bb10c.get());

    final long v1 = diskSwapLoadingStages_800f48e8.get((int)pregameLoadingStage_800bb10c.get()).deref().run();

    if(v1 == 1) {
      //LAB_800d930c
      diskSwapAttempts_800c6690.setu(0);
      pregameLoadingStage_800bb10c.addu(0x1L);
    } else if(v1 == 2) {
      //LAB_800d9320
      diskSwapAttempts_800c6690.setu(0);
      mainCallbackIndexOnceLoaded_8004dd24.setu(_800bc05c.get());
      pregameLoadingStage_800bb10c.setu(0);
      vsyncMode_8007a3b8.setu(0x2L);
    } else if(v1 == 3) {
      //LAB_800d9354
      loadWrongDiskOrPleaseWaitMcq(0);
      diskSwapAttempts_800c6690.setu(0);

      //LAB_800d936c
      pregameLoadingStage_800bb10c.setu(0x3L);
    }

    //LAB_800d9370
    //LAB_800d9374
    if(diskSwapMcqLoaded_800c6698.get() != 0) {
      renderMcq(mcq_800c66a0, 640, 0, -centreScreenX_1f8003dc.get(), -centreScreenY_1f8003de.get(), 0x24L, 0x80L);
    }

    //LAB_800d93c4
  }

  @Method(0x800d93dcL)
  public static long loadDiskSwapScreen() {
    setWidthAndFlags(640, 1);
    _800babc0.setu(0);
    _800bb104.setu(0);
    _8007a3a8.setu(0);
    scriptStartEffect(1, 1);
    _8004dd30.setu(0x1L);
    setMainVolume(0, 0);
    vsyncMode_8007a3b8.setu(0);
    S_InitLoaded_800c6694.setu(0);
    diskSwapMcqLoaded_800c6698.setu(0);
    loadDrgnBinFile(0, mcqPleaseInsertDiskN_800f48d0.get(diskNum_8004ddc0.get() - 1).get(), 0, getMethodAddress(SMap.class, "FUN_800d956c", long.class, long.class, long.class), 0, 0x2L);
    loadDrgnBinFile(0, mcqWrongDisk_800f48d8.get(diskNum_8004ddc0.get() - 1).get(), 0, getMethodAddress(SMap.class, "FUN_800d9614", long.class, long.class, long.class), 0, 0x4L);
    loadDrgnBinFile(0, mcqPleaseWait_800f48e0.get(diskNum_8004ddc0.get() - 1).get(), 0, getMethodAddress(SMap.class, "FUN_800d9614", long.class, long.class, long.class), 1, 0x4L);
    loadAndRunOverlay(0, getMethodAddress(SMap.class, "FUN_800d962c", long.class), 0);
    return 1;
  }

  @Method(0x800d956cL)
  public static void FUN_800d956c(final long address, final long size, final long param) {
    memcpy(mcq_800c66a0.getAddress(), address, 0x2c);

    loadMcq(mcq_800c66a0, 640, 0);
    diskSwapMcqLoaded_800c6698.setu(0x1L);
    FUN_800127cc(address, 0, 0x1L);
  }

  @Method(0x800d9614L)
  public static void FUN_800d9614(final long address, final long size, final long param) {
    mcqs_800c66d0.get((int)param).setPointer(address);
  }

  @Method(0x800d962cL)
  public static void FUN_800d962c(final long a0) {
    S_InitLoaded_800c6694.setu(0x1L);
  }

  @Method(0x800d963cL)
  public static long waitForSInitToLoad() {
    if(S_InitLoaded_800c6694.get() == 0 || fileCount_8004ddc8.get() != 0) {
      //LAB_800d9660
      return 0;
    }

    //LAB_800d9668
    return 1;
  }

  @Method(0x800d9670L)
  public static long stopCdrom() {
    //LAB_800d96a8
    return 1;
  }

  @Method(0x800d96b8L)
  public static long FUN_800d96b8() {
    if(S_InitLoaded_800c6694.get() != 0 && fileCount_8004ddc8.get() == 0) {
      if(_800bb168.get() == 0) {
        return 1;
      }

      //LAB_800d9704
      if(diskSwapAttempts_800c6690.get() == 0) {
        scriptStartEffect(2, 15);
        diskSwapAttempts_800c6690.addu(0x1L);
      }
    }

    //LAB_800d9730
    //LAB_800d9734
    return 0;
  }

  @Method(0x800d9744L)
  public static long isCdromDriveOpen() {
    return 1;
  }

  @Method(0x800d976cL)
  public static long prepareForDiskSwap() {
    loadWrongDiskOrPleaseWaitMcq(1);
    return 1;
  }

  @Method(0x800d97c4L)
  public static long loadNewDisk() {
    CDROM.loadDisk(diskNum_8004ddc0.get());
    cdName_80011700.set("CD00" + diskNum_8004ddc0.get());
    DsNewMedia();
    return 1;
  }

  @Method(0x800d9814L)
  public static long checkDiskSwapSuccess() {
    final CdlFILE file = new CdlFILE();
    if(DsSearchFile(file, "\\SECT\\DRGN2%d.BIN;1".formatted(diskNum_8004ddc0.get())) != null) {
      //LAB_800d988c
      drgnBinIndex_800bc058.set(diskNum_8004ddc0.get());
      return 1;
    }

    //LAB_800d98a0
    diskSwapAttempts_800c6690.addu(0x1L);
    return diskSwapAttempts_800c6690.get() < 0x15 ? 0 : 3;
  }

  @Method(0x800d98b0L)
  public static long FUN_800d98b0() {
    final int v0 = (int)executeSInitLoadingStage(3);

    if(v0 == 0) {
      return 0;
    }

    //LAB_800d98d8
    if(v0 < 0) {
      //LAB_800d9914
      return 3;
    }

    removeFromLinkedList(mcqs_800c66d0.get(0).getPointer());
    removeFromLinkedList(mcqs_800c66d0.get(1).getPointer());
    mcqs_800c66d0.get(0).clear();
    mcqs_800c66d0.get(1).clear();

    //LAB_800d9918
    return 1;
  }

  @Method(0x800d992cL)
  public static long FUN_800d992c() {
    if(fileCount_8004ddc8.get() != 0) {
      return 0;
    }

    SInitBinLoaded_800bbad0.set(false);
//    FUN_80036e80();
//    FUN_800fbff8();

    //LAB_800d9960
    return 1;
  }

  @Method(0x800d9970L)
  public static long authenticateDisk() {
    return 1;
  }

  @Method(0x800d9994L)
  public static long FUN_800d9994() {
    SInitBinLoaded_800bbad0.set(true);

    //TODO GH#3
//    FUN_80019610();
//    loadDRGN0_mrg_62802_sounds();
//    sssqFadeIn(0x3cL, 0x7fL);

    //LAB_800d99e0
    return 1;
  }

  @Method(0x800d99f0L)
  public static long FUN_800d99f0() {
    if(diskSwapAttempts_800c6690.get() == 0) {
      scriptStartEffect(1, 15);
      diskSwapAttempts_800c6690.addu(0x1L);
      return 0;
    }

    //LAB_800d9a30
    if(_800bb168.get() != 0xff) {
      //LAB_800d9a68
      return 0;
    }

    FUN_80012bb4();
    S_InitLoaded_800c6694.setu(0);
    diskSwapMcqLoaded_800c6698.setu(0);
    _8004dd30.setu(0);

    //LAB_800d9a6c
    return 2;
  }

  /**
   * @param index 0 = wrong disk, 1 = please wait
   */
  @Method(0x800d9a7cL)
  public static void loadWrongDiskOrPleaseWaitMcq(final int index) {
    final McqHeader mcq = mcqs_800c66d0.get(index).deref();
    memcpy(mcq_800c66a0.getAddress(), mcq.getAddress(), 0x2c);

    //LAB_800d9aa8
    loadMcq(mcq, 640, 0);
  }

  @Method(0x800d9b08L)
  public static void FUN_800d9b08(final long a0) {
    loadCharacterStats(0);

    if((int)a0 >= 0) {
      final ActiveStatsa0 stats = stats_800be5f8.get((int)a0);
      final CharacterData2c charData = gameState_800babc8.charData_32c.get((int)a0);
      charData.hp_08.set(stats.maxHp_66.get());
      charData.mp_0a.set(stats.maxMp_6e.get());
    } else {
      //LAB_800d9b70
      //LAB_800d9b84
      for(int charSlot = 0; charSlot < 9; charSlot++) {
        final ActiveStatsa0 stats = stats_800be5f8.get(charSlot);
        final CharacterData2c charData = gameState_800babc8.charData_32c.get(charSlot);
        charData.hp_08.set(stats.maxHp_66.get());
        charData.mp_0a.set(stats.maxMp_6e.get());
      }
    }

    //LAB_800d9ba8
    FUN_80012bb4();
  }

  @Method(0x800d9bc0L)
  public static long FUN_800d9bc0(final RunningScript a0) {
    loadAndRunOverlay(2, getMethodAddress(SMap.class, "FUN_800d9b08", long.class), -1);
    return 0;
  }

  @Method(0x800d9bf4L)
  public static long FUN_800d9bf4(final RunningScript a0) {
    //LAB_800d9c04
    for(int i = 0; i < 9; i++) {
      gameState_800babc8.charData_32c.get(i).status_10.set(0);
    }

    return 0;
  }

  @Method(0x800d9c1cL)
  public static long FUN_800d9c1c(final RunningScript a0) {
    //LAB_800d9c78
    memcpy(gameState_800babc8.charData_32c.get(a0.params_20.get(1).deref().get()).getAddress(), gameState_800babc8.charData_32c.get(a0.params_20.get(0).deref().get()).getAddress(), 0x2c);
    loadAndRunOverlay(2, getMethodAddress(SMap.class, "FUN_800d9b08", long.class), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800d9ce4L)
  public static long scriptSetCharAddition(final RunningScript a0) {
    gameState_800babc8.charData_32c.get(a0.params_20.get(0).deref().get()).selectedAddition_19.set(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800d9d20L)
  public static long scriptGetCharAddition(final RunningScript a0) {
    a0.params_20.get(1).deref().set(gameState_800babc8.charData_32c.get(a0.params_20.get(0).deref().get()).selectedAddition_19.get());
    return 0;
  }

  @Method(0x800d9d60L)
  public static long FUN_800d9d60(final RunningScript a0) {
    if(gameState_800babc8.charData_32c.get(0).dlevelXp_0e.get() < 63901) {
      gameState_800babc8.charData_32c.get(0).dlevelXp_0e.set(63901);
    }

    //LAB_800d9d90
    gameState_800babc8.charData_32c.get(0).dlevel_13.set(5);

    loadAndRunOverlay(2, getMethodAddress(SMap.class, "FUN_800d9dc0", long.class), 0);
    return 0;
  }

  @Method(0x800d9dc0L)
  public static void FUN_800d9dc0(final long charIndex) {
    gameState_800babc8.charData_32c.get((int)charIndex).sp_0c.set(500);
    FUN_800d9b08(-1);
  }

  @Method(0x800d9e64L)
  public static void FUN_800d9e64(final GsDOBJ2 dobj2, final long a1) {
    final TmdObjTable objTable = dobj2.tmd_08.deref();
    long count = objTable.n_primitive_14.get();
    long primitives = objTable.primitives_10.getPointer();
    final long s2 = a1 & 0x7fL;

    //LAB_800d9e90
    while(count != 0) {
      final long id = MEMORY.ref(4, primitives).get(0xff04_0000L);
      final long primitiveCount = MEMORY.ref(2, primitives).get();

      if(id == 0x3000_0000L || id == 0x3200_0000L) {
        FUN_80021048(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x14L;
      } else if(id == 0x3004_0000L || id == 0x3204_0000L) {
        FUN_80021058(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x1cL;
      } else if(id == 0x3400_0000L || id == 0x3600_0000L) {
        FUN_800da6c8(primitives, primitiveCount, s2);
        count -= primitiveCount;
        primitives += primitiveCount * 0x1cL;
      } else if(id == 0x3500_0000L || id == 0x3700_0000L) {
        FUN_800da7f4(primitives, primitiveCount, s2);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(id == 0x3800_0000L || id == 0x3a00_0000L) {
        FUN_80021050(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x18L;
      } else if(id == 0x3804_0000L || id == 0x3a04_0000L) {
        FUN_80021060(primitives, primitiveCount);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(id == 0x3c00_0000L || id == 0x3e00_0000L) {
        FUN_800da754(primitives, primitiveCount, s2);
        count -= primitiveCount;
        primitives += primitiveCount * 0x24L;
      } else if(id == 0x3d00_0000L || id == 0x3f00_0000L) {
        FUN_800da880(primitives, primitiveCount, s2);
        count -= primitiveCount;
        primitives += primitiveCount * 0x2cL;
      }
    }
  }

  @Method(0x800da114L)
  public static void FUN_800da114(final BigStruct struct) {
    if(!struct.smallerStructPtr_a4.isNull()) {
      //LAB_800da138
      for(int i = 0; i < 4; i++) {
        if(struct.smallerStructPtr_a4.deref().uba_04.get(i).get() != 0) {
          FUN_800dde70(struct, i);
        }

        //LAB_800da15c
      }
    }

    //LAB_800da16c
    //LAB_800da174
    for(int i = 0; i < 7; i++) {
      if(struct.aub_ec.get(i).get() != 0) {
        FUN_80022018(struct, i);
      }

      //LAB_800da18c
    }

    final int v1 = struct.ub_9c.get();
    if(v1 == 2) {
      return;
    }

    if(v1 == 0) {
      if(struct.ub_a2.get() == 0) {
        struct.s_9e.set(struct.s_9a.get());
      } else {
        //LAB_800da1d0
        struct.s_9e.set((short)(struct.s_9a.get() >> 1));
      }

      //LAB_800da1e4
      struct.ub_9c.incr();
      struct.partTransforms_94.set(struct.partTransforms_90.deref());
    }

    //LAB_800da1f8
    if((struct.s_9e.get() & 0x1L) == 0 && struct.ub_a2.get() == 0) {
      final UnboundedArrayRef<ModelPartTransforms> old = struct.partTransforms_94.deref();

      if(struct.ub_a3.get() == 0) {
        FUN_800da920(struct);
      } else {
        //LAB_800da23c
        applyModelPartTransforms(struct);
      }

      struct.partTransforms_94.set(old);
    } else {
      //LAB_800da24c
      applyModelPartTransforms(struct);
    }

    //LAB_800da254
    struct.s_9e.decr();

    if(struct.s_9e.get() == 0) {
      struct.ub_9c.set(0);
    }

    //LAB_800da274
  }

  @Method(0x800da524L)
  public static void FUN_800da524(final BigStruct param_1) {
    GsInitCoordinate2(param_1.coord2_14, bigStruct_800bda10.coord2_14);

    bigStruct_800bda10.coord2_14.coord.transfer.set(param_1.vector_118);
    bigStruct_800bda10.zOffset_a0.set((short)(param_1.zOffset_a0.get() + 0x10));

    bigStruct_800bda10.scaleVector_fc.setX(param_1.vector_10c.x.get() >> 6);
    bigStruct_800bda10.scaleVector_fc.setY(param_1.vector_10c.y.get() >> 6);
    bigStruct_800bda10.scaleVector_fc.setZ(param_1.vector_10c.z.get() >> 6);

    RotMatrix_8003faf0(bigStruct_800bda10.coord2Param_64.rotate, bigStruct_800bda10.coord2_14.coord);

    final VECTOR scale = new VECTOR();
    scale.set(bigStruct_800bda10.scaleVector_fc);
    ScaleMatrixL(bigStruct_800bda10.coord2_14.coord, scale);

    bigStruct_800bda10.coord2_14.flg.set(0);

    final MATRIX matrix = bigStruct_800bda10.coord2ArrPtr_04.deref().get(0).coord;
    final GsCOORD2PARAM params = bigStruct_800bda10.coord2ArrPtr_04.deref().get(0).param.deref();

    params.rotate.set((short)0, (short)0, (short)0);
    RotMatrix_80040780(params.rotate, matrix);

    params.trans.setX(0);
    params.trans.setY(0);
    params.trans.setZ(0);
    TransMatrix(matrix, params.trans);

    final MATRIX lw = new MATRIX();
    final MATRIX ls = new MATRIX();
    GsGetLws(bigStruct_800bda10.ObjTable_0c.top.deref().get(0).coord2_04.deref(), lw, ls);
    GsSetLightMatrix(lw);

    CPU.CTC2(ls.getPacked(0), 0);
    CPU.CTC2(ls.getPacked(2), 1);
    CPU.CTC2(ls.getPacked(4), 2);
    CPU.CTC2(ls.getPacked(6), 3);
    CPU.CTC2(ls.getPacked(8), 4);
    CPU.CTC2(ls.transfer.getX(), 5);
    CPU.CTC2(ls.transfer.getY(), 6);
    CPU.CTC2(ls.transfer.getZ(), 7);

    Renderer.renderDobj2(bigStruct_800bda10.ObjTable_0c.top.deref().get(0), false);
    bigStruct_800bda10.coord2ArrPtr_04.deref().get(0).flg.decr();
  }

  @Method(0x800da6c8L)
  public static void FUN_800da6c8(long primitives, final long count, final long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();

    //LAB_800da6e8
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x4L).and(MEMORY.ref(4, a3).offset(0x4L).get()).oru(MEMORY.ref(4, a3).offset(0x0L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x8L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0xcL).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      primitives += 0x1cL;
    }

    //LAB_800da74c
  }

  @Method(0x800da754L)
  public static void FUN_800da754(long primitives, final long count, final long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();

    //LAB_800da774
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x04L).and(MEMORY.ref(4, a3).get()).oru(MEMORY.ref(4, a3).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x08L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x0cL).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(0x10L).addu(MEMORY.ref(4, a3).offset(0x10L));
      primitives += 0x24L;
    }

    //LAB_800da7ec
  }

  @Method(0x800da7f4L)
  public static void FUN_800da7f4(long primitives, final long count, final long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();

    //LAB_800da814
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x4L).and(MEMORY.ref(4, a3).offset(0x4L).get()).oru(MEMORY.ref(4, a3).offset(0x0L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x8L).and(MEMORY.ref(4, a3).offset(0xcL).get()).oru(MEMORY.ref(4, a3).offset(0x8L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0xcL).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      primitives += 0x24L;
    }

    //LAB_800da878
  }

  @Method(0x800da880L)
  public static void FUN_800da880(long primitives, final long count, final long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();

    //LAB_800da8a0
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, primitives).offset(0x04L).and(MEMORY.ref(4, a3).offset(0x04L).get()).oru(MEMORY.ref(4, a3).offset(0x00L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x08L).and(MEMORY.ref(4, a3).offset(0x0cL).get()).oru(MEMORY.ref(4, a3).offset(0x08L).get()).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x0cL).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      MEMORY.ref(4, primitives).offset(0x10L).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      primitives += 0x2cL;
    }

    //LAB_800da918
  }

  @Method(0x800da920L)
  public static void FUN_800da920(final BigStruct a0) {
    final UnboundedArrayRef<ModelPartTransforms> transforms = a0.partTransforms_94.deref();

    //LAB_800da96c
    for(int i = 0; i < a0.tmdNobj_ca.get(); i++) {
      final GsDOBJ2 dobj2 = a0.dobj2ArrPtr_00.deref().get(i);

      final GsCOORDINATE2 coord2 = dobj2.coord2_04.deref();
      final GsCOORD2PARAM params = coord2.param.deref();
      final MATRIX matrix = coord2.coord;

      RotMatrix_80040780(params.rotate, matrix);

      params.trans.set(
        (params.trans.getX() + transforms.get(i).translate_06.getX()) / 2,
        (params.trans.getY() + transforms.get(i).translate_06.getY()) / 2,
        (params.trans.getZ() + transforms.get(i).translate_06.getZ()) / 2
      );

      TransMatrix(matrix, params.trans);
    }

    //LAB_800daa0c
    a0.partTransforms_94.set(transforms.slice(a0.tmdNobj_ca.get()));
  }

  @Method(0x800daa3cL)
  public static void FUN_800daa3c(final BigStruct a0) {
    long s0 = 0x1L;
    long s6 = a0.ui_f4.get();

    zOffset_1f8003e8.set(a0.zOffset_a0.get());
    _1f8003ec.setu(a0.ui_108.get());

    //LAB_800daaa8
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      if((s0 & s6) == 0) {
        final GsDOBJ2 dobj2 = a0.ObjTable_0c.top.deref().get(i);

        final MATRIX lw = new MATRIX();
        final MATRIX ls = new MATRIX();
        GsGetLws(dobj2.coord2_04.deref(), lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2(ls.getPacked(0), 0);
        CPU.CTC2(ls.getPacked(2), 1);
        CPU.CTC2(ls.getPacked(4), 2);
        CPU.CTC2(ls.getPacked(6), 3);
        CPU.CTC2(ls.getPacked(8), 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        Renderer.renderDobj2(dobj2, false);
      }

      //LAB_800dab10
      s0 = (int)(s0 << 1);
      if(s0 == 0) {
        s0 = 0x1L;
        s6 = a0.ui_f8.get();
      }

      //LAB_800dab24
    }

    //LAB_800dab34
    if(a0.b_cc.get() != 0) {
      FUN_800da524(a0);
    }

    //LAB_800dab4c
  }

  @Method(0x800dde70L)
  public static void FUN_800dde70(final BigStruct struct, final int index) {
    final SmallerStruct smallerStruct = struct.smallerStructPtr_a4.deref();

    if(smallerStruct.tmdSubExtensionArr_20.get(index).isNull()) {
      smallerStruct.uba_04.get(index).set(0);
    } else {
      //LAB_800ddeac
      final long v1 = (struct.ub_9d.get() & 0x7fL) * 0x2L;
      final long t2 = _80050424.offset(v1).get() + 0x70L;
      final long t1 = _800503f8.offset(v1).getSigned();

      long a1 = smallerStruct.tmdSubExtensionArr_20.get(index).getPointer() + 0x4L; //TODO

      //LAB_800ddef8
      for(int i = 0; i < smallerStruct.sa_08.get(index).get(); i++) {
        a1 += 0x4L;
      }

      //LAB_800ddf08
      final long t3 = MEMORY.ref(2, a1).get();
      a1 += 0x2L;

      smallerStruct.sa_10.get(index).incr();

      if(smallerStruct.sa_10.get(index).get() == (short)MEMORY.ref(2, a1).get()) {
        smallerStruct.sa_10.get(index).set((short)0);

        if(MEMORY.ref(2, a1).offset(0x2L).get() == 0xffffL) {
          smallerStruct.sa_08.get(index).set((short)0);
        } else {
          //LAB_800ddf70
          smallerStruct.sa_08.get(index).incr();
        }
      }

      //LAB_800ddf8c
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)t1, (short)(t2 + t3), (short)16, (short)1), t1 & 0xffffL, smallerStruct.sa_18.get(index).get() + t2 & 0xffffL);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0x18L);
    }

    //LAB_800ddff4
  }

  @Method(0x800de004L)
  public static void FUN_800de004(final BigStruct bigStruct, final ExtendedTmd extendedTmd) {
    if(extendedTmd.ext_04.isNull()) {
      //LAB_800de120
      bigStruct.smallerStructPtr_a4.clear();
      return;
    }

    final SmallerStruct smallerStruct = MEMORY.ref(4, addToLinkedListTail(0x30L), SmallerStruct::new);
    bigStruct.smallerStructPtr_a4.set(smallerStruct);

    final TmdExtension ext = extendedTmd.ext_04.deref();
    smallerStruct.tmdExt_00.set(ext);

    //LAB_800de05c
    for(int i = 0; i < 4; i++) {
      smallerStruct.tmdSubExtensionArr_20.get(i).setNullable(smallerStruct.tmdExt_00.deref().tmdSubExtensionArr_00.get(i).derefNullable());

      if(smallerStruct.tmdSubExtensionArr_20.get(i).isNull()) {
        smallerStruct.uba_04.get(i).set(0);
      } else {
        smallerStruct.sa_08.get(i).set((short)0);
        smallerStruct.sa_10.get(i).set((short)0);
        smallerStruct.sa_18.get(i).set((short)smallerStruct.tmdSubExtensionArr_20.get(i).deref().us_02.get());

        if(smallerStruct.sa_18.get(i).get() == -1) {
          //LAB_800de0f8
          smallerStruct.uba_04.get(i).set(0);
        } else {
          //LAB_800de104
          smallerStruct.uba_04.get(i).set(1);
        }
      }

      //LAB_800de108
    }

    //LAB_800de124
  }

  @Method(0x800de138L)
  public static void FUN_800de138(final BigStruct struct, final int index) {
    final SmallerStruct smallerStruct = struct.smallerStructPtr_a4.deref();

    if(smallerStruct.tmdSubExtensionArr_20.get(index).isNull()) {
      smallerStruct.uba_04.get(index).set(0);
      return;
    }

    //LAB_800de164
    smallerStruct.sa_08.get(index).set((short)0);
    smallerStruct.sa_10.get(index).set((short)0);
    smallerStruct.sa_18.get(index).set((short)smallerStruct.tmdSubExtensionArr_20.get(index).deref().us_02.get());

    if(smallerStruct.sa_18.get(index).get() == -1) {
      smallerStruct.uba_04.get(index).set(0);
    } else {
      //LAB_800de1c4
      smallerStruct.uba_04.get(index).set(1);
    }
  }

  /** TODO this method moves the player */
  @Method(0x800de1d0L)
  public static long FUN_800de1d0(final RunningScript a0) {
    final short deltaX = (short)a0.params_20.get(0).deref().get();
    final short deltaY = (short)a0.params_20.get(1).deref().get();
    final short deltaZ = (short)a0.params_20.get(2).deref().get();

    if(deltaX != 0 || deltaY != 0 || deltaZ != 0) {
      final SVECTOR deltaMovement = new SVECTOR();
      final SVECTOR worldspaceDeltaMovement = new SVECTOR();

      //LAB_800de218
      final BigStruct player = a0.scriptState_04.deref().innerStruct_00.derefAs(BigStruct.class);
      deltaMovement.set(deltaX, deltaY, deltaZ);
      SetRotMatrix(matrix_800c3548);
      SetTransMatrix(matrix_800c3548);
      transformToWorldspace(worldspaceDeltaMovement, deltaMovement);

      final int s2 = FUN_800e88a0(player.mrgAnimGroup_12e.get(), player.coord2_14.coord, worldspaceDeltaMovement);
      if(s2 >= 0) {
        if(FUN_800e6798(s2, 0, player.coord2_14.coord.transfer.getX(), player.coord2_14.coord.transfer.getY(), player.coord2_14.coord.transfer.getZ(), worldspaceDeltaMovement) != 0) {
          player.coord2_14.coord.transfer.x.add(worldspaceDeltaMovement.getX());
          player.coord2_14.coord.transfer.setY(worldspaceDeltaMovement.getY());
          player.coord2_14.coord.transfer.z.add(worldspaceDeltaMovement.getZ());
        }

        //LAB_800de2c8
        player.ui_16c.set(s2);
      }

      //LAB_800de2cc
      player.us_170.set(0);
      setScriptTempTicker(scriptStateIndices_800c6880.get(player.scriptFileIndex_130.get()).get(), MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e3e60", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
      bigStruct_800c6748.i_1a0.set(worldspaceDeltaMovement.getX());
      bigStruct_800c6748.i_1a4.set(worldspaceDeltaMovement.getY());
      bigStruct_800c6748.i_1a8.set(worldspaceDeltaMovement.getZ());
    }

    //LAB_800de318
    return 0;
  }

  @Method(0x800de4b4L) //TODO needs more refactoring
  public static long FUN_800de4b4(final RunningScript a0) {
    long v1;
    long a1;
    long a2;
    long a3;
    long sp20;
    long sp68;
    long sp6c;
    long sp70;

    final SVECTOR sp0x10 = new SVECTOR();
    final MATRIX sp0x28 = new MATRIX();
    final MATRIX sp0x48 = new MATRIX();

    long s0 = a0.params_20.get(0).getPointer();

    //LAB_800de4f8
    while(MEMORY.ref(4, s0).getSigned() != -0x1L) {
      get3dAverageOfSomething((int)MEMORY.ref(4, s0).get(), sp0x10);
      bigStruct_800c6748.coord2_14.coord.transfer.setX(sp0x10.getX());
      bigStruct_800c6748.coord2_14.coord.transfer.setY(sp0x10.getY());
      bigStruct_800c6748.coord2_14.coord.transfer.setZ(sp0x10.getZ());

      GsGetLws(bigStruct_800c6748.coord2_14, sp0x48, sp0x28);
      CPU.CTC2(sp0x28.getPacked(0), 0);
      CPU.CTC2(sp0x28.getPacked(2), 1);
      CPU.CTC2(sp0x28.getPacked(4), 2);
      CPU.CTC2(sp0x28.getPacked(6), 3);
      CPU.CTC2(sp0x28.getPacked(8), 4);
      CPU.CTC2(sp0x28.transfer.getX(), 5);
      CPU.CTC2(sp0x28.transfer.getY(), 6);
      CPU.CTC2(sp0x28.transfer.getZ(), 7);
      CPU.MTC2(0, 0);
      CPU.MTC2(0, 1);
      CPU.COP2(0x18_0001L);
      sp20 = CPU.MFC2(14);
      sp68 = CPU.MFC2(8);
      sp6c = CPU.CFC2(31);
      sp70 = CPU.MFC2(19) >> 2;
      s0 = s0 + 0x4L;
      a3 = 0xe0L;
      a2 = 0x90L;

      //LAB_800de5d4
      for(int i = 0; i < 0x14L; i++) {
        a1 = _800c69fc.get();
        v1 = a1 + i * 0x2L;

        if(MEMORY.ref(2, v1).offset(0x18L).getSigned() == -0x1L) {
          MEMORY.ref(2, v1).offset(0x40L).setu(sp20 & 0xffffL);
          MEMORY.ref(2, v1).offset(0x68L).setu(sp20 >>> 16 & 0xffffL);
          MEMORY.ref(2, v1).offset(0x18L).setu(MEMORY.ref(2, s0).offset(0x0L).get());
          getScreenOffset(MEMORY.ref(4, a1 + a2, IntRef::new), MEMORY.ref(4, a1 + a3, IntRef::new));
          break;
        }

        //LAB_800de620
        a3 = a3 + 0x4L;
        a2 = a2 + 0x4L;
      }

      s0 = s0 + 0x4L;

      //LAB_800de634
    }

    //LAB_800de644
    return 0;
  }

  @Method(0x800de668L)
  public static long FUN_800de668(final RunningScript a0) {
    final BigStruct s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    s0.vec_138.x.set(a0.params_20.get(1).deref().get());
    s0.vec_138.y.set(a0.params_20.get(2).deref().get());
    s0.vec_138.z.set(a0.params_20.get(3).deref().get());
    s0.ui_144.set(a0.params_20.get(4).deref().get());

    s0.us_170.set(1);

    s0.vec_148.set(s0.vec_138).sub(s0.coord2_14.coord.transfer).div(s0.ui_144.get());

    if(s0.vec_148.x.get() == 0) {
      if(s0.vec_138.x.get() < s0.coord2_14.coord.transfer.getX()) {
        s0.vec_148.x.set(0x8000_0000);
      }
    }

    //LAB_800de750
    if(s0.vec_148.y.get() == 0) {
      if(s0.vec_138.y.get() < s0.coord2_14.coord.transfer.getY()) {
        s0.vec_148.y.set(0x8000_0000);
      }
    }

    //LAB_800de77c
    if(s0.vec_148.z.get() == 0) {
      if(s0.vec_138.z.get() < s0.coord2_14.coord.transfer.getZ()) {
        s0.vec_148.z.set(0x8000_0000);
      }
    }

    //LAB_800de7a8
    int v0;
    v0 = s0.vec_138.x.get() - s0.coord2_14.coord.transfer.getX();
    v0 = v0 << 16;
    v0 = v0 / s0.ui_144.get();

    if(s0.vec_148.x.get() < 0) {
      //LAB_800de7e0
      v0 = ~v0 + 1;
    }

    //LAB_800de810
    s0.vec_154.x.set(v0 & 0xffff);

    v0 = s0.vec_138.y.get() - s0.coord2_14.coord.transfer.getY();
    v0 = v0 << 16;
    v0 = v0 / s0.ui_144.get();

    if(s0.vec_148.y.get() < 0) {
      //LAB_800de84c
      v0 = ~v0 + 1;
    }

    //LAB_800de87c
    s0.vec_154.y.set(v0 & 0xffff);

    v0 = s0.vec_138.z.get() - s0.coord2_14.coord.transfer.getZ();
    v0 = v0 << 16;
    v0 = v0 / s0.ui_144.get();

    if(s0.vec_148.z.get() < 0) {
      //LAB_800de8b8
      v0 = ~v0 + 1;
    }

    //LAB_800de8e8
    s0.vec_154.z.set(v0 & 0xffff);

    s0.vec_160.x.set(0);
    s0.vec_160.y.set(0);
    s0.vec_160.z.set(0);

    setScriptTempTicker(scriptStateIndices_800c6880.get(s0.scriptFileIndex_130.get()).get(), MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e1f90", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));

    s0.ui_190.and(0x7fff_ffffL);
    return 0;
  }

  @Method(0x800de944L)
  public static long FUN_800de944(final RunningScript a0) {
    final BigStruct a2 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    a2.vec_138.set(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get());
    final int a3 = a0.params_20.get(4).deref().get();
    a2.ui_144.set(a3);
    a2.vec_148.setX((a2.vec_138.getX() - a2.coord2_14.coord.transfer.getX()) / a3);
    a2.vec_148.setZ((a2.vec_138.getZ() - a2.coord2_14.coord.transfer.getZ()) / a3);

    if(a2.vec_148.getX() == 0 && a2.vec_138.getX() < a2.coord2_14.coord.transfer.getX()) {
      a2.vec_148.setX(0x8000_0000);
    }

    //LAB_800dea08
    if(a2.vec_148.getZ() == 0 && a2.vec_138.getZ() < a2.coord2_14.coord.transfer.getZ()) {
      a2.vec_148.setZ(0x8000_0000);
    }

    //LAB_800dea34
    int v0;
    if(a2.vec_148.getX() >= 0) {
      v0 = a2.vec_138.getX() - a2.coord2_14.coord.transfer.getX();
      v0 = v0 << 16;
      v0 = v0 / a2.ui_144.get();
      v0 = v0 & 0xffff;
    } else {
      //LAB_800dea6c
      v0 = a2.vec_138.getX() - a2.coord2_14.coord.transfer.getX();
      v0 = v0 << 16;
      v0 = v0 / a2.ui_144.get();
      v0 = ~v0;
      v0 = v0 + 1;
      v0 = v0 & 0xffff;
    }

    //LAB_800dea9c
    a2.vec_154.setX(v0);

    if(a2.vec_148.getZ() >= 0) {
      v0 = a2.vec_138.getZ() - a2.coord2_14.coord.transfer.getZ();
      v0 = v0 << 16;
      v0 = v0 / a2.ui_144.get();
      v0 = v0 & 0xffff;
    } else {
      //LAB_800dead8
      v0 = a2.vec_138.getZ() - a2.coord2_14.coord.transfer.getZ();
      v0 = v0 << 16;
      v0 = v0 / a2.ui_144.get();
      v0 = ~v0;
      v0 = v0 + 1;
      v0 = v0 & 0xffff;
    }

    //LAB_800deb08
    a2.vec_154.setZ(v0);

    a2.s_134.set((short)(((a2.vec_138.getY() - a2.coord2_14.coord.transfer.getY()) * 2 - a3 * 7 * (a3 - 1)) / (a3 * 2)));
    a2.vec_160.setX(0);
    a2.vec_160.setZ(0);
    a2.us_170.set(2);
    a2.s_172.set((short)1);
    a2.ui_18c.set(0x7L);
    setScriptTempTicker(scriptStateIndices_800c6880.get(a2.scriptFileIndex_130.get()).get(), MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e3e74", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800deba0L)
  public static long FUN_800deba0(final RunningScript a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    final long a3;
    final long t0;
    long lo;
    v1 = 0x800c_0000L;
    v0 = a0.params_20.get(0).deref().get();
    v1 = v1 - 0x3e40L;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v0 = MEMORY.ref(4, v0).offset(0x0L).get();
    t0 = MEMORY.ref(4, v0).offset(0x0L).get();
    v0 = a0.params_20.get(1).deref().get();
    MEMORY.ref(4, t0).offset(0x138L).setu(v0);
    v0 = a0.params_20.get(2).deref().get();
    MEMORY.ref(4, t0).offset(0x13cL).setu(v0);
    a2 = MEMORY.ref(4, t0).offset(0x138L).get();
    v0 = a0.params_20.get(3).deref().get();
    MEMORY.ref(4, t0).offset(0x140L).setu(v0);
    v1 = MEMORY.ref(4, t0).offset(0x2cL).get();
    a3 = a0.params_20.get(4).deref().get();
    a2 = a2 - v1;
    MEMORY.ref(4, t0).offset(0x144L).setu(a3);
    v0 = a3;
    lo = (int)a2 / (int)v0;
    a2 = lo;
    v1 = MEMORY.ref(4, t0).offset(0x34L).get();
    a1 = MEMORY.ref(4, t0).offset(0x140L).get();
    a1 = a1 - v1;
    v0 = a3;
    lo = (int)a1 / (int)v0;
    a1 = lo;
    v1 = 0x800f_0000L;
    v0 = a0.params_20.get(5).deref().get();
    v1 = v1 + 0x5ac0L;
    v0 = v0 << 2;
    v0 = v0 + v1;
    v1 = MEMORY.ref(4, v0).offset(0x0L).get();
    final long a0_0 = v1;
    MEMORY.ref(4, t0).offset(0x18cL).setu(v1);
    MEMORY.ref(4, t0).offset(0x148L).setu(a2);
    MEMORY.ref(4, t0).offset(0x150L).setu(a1);
    v0 = a2;
    if(v0 == 0) {
      v0 = MEMORY.ref(4, t0).offset(0x138L).get();
      v1 = MEMORY.ref(4, t0).offset(0x2cL).get();

      if((int)v0 < (int)v1) {
        v0 = 0x8000_0000L;
        MEMORY.ref(4, t0).offset(0x148L).setu(v0);
      }
    }

    //LAB_800dec90
    v0 = MEMORY.ref(4, t0).offset(0x150L).get();

    if(v0 == 0) {
      v0 = MEMORY.ref(4, t0).offset(0x140L).get();
      v1 = MEMORY.ref(4, t0).offset(0x34L).get();

      if((int)v0 < (int)v1) {
        v0 = 0x8000_0000L;
        MEMORY.ref(4, t0).offset(0x150L).setu(v0);
      }
    }

    //LAB_800decbc
    v0 = MEMORY.ref(4, t0).offset(0x148L).get();

    if((int)v0 >= 0) {
      v0 = MEMORY.ref(4, t0).offset(0x138L).get();
      v1 = MEMORY.ref(4, t0).offset(0x2cL).get();
      v0 = v0 - v1;
      v1 = MEMORY.ref(4, t0).offset(0x144L).get();
      v0 = v0 << 16;
      lo = (int)v0 / (int)v1;
      v0 = lo;
      v0 = v0 & 0xffffL;
    } else {
      //LAB_800decf4
      v0 = MEMORY.ref(4, t0).offset(0x138L).get();
      v1 = MEMORY.ref(4, t0).offset(0x2cL).get();
      v0 = v0 - v1;
      v1 = MEMORY.ref(4, t0).offset(0x144L).get();
      v0 = v0 << 16;
      lo = (int)v0 / (int)v1;
      v0 = lo;
      v0 = ~v0;
      v0 = v0 + 0x1L;
      v0 = v0 & 0xffffL;
    }

    //LAB_800ded24
    MEMORY.ref(4, t0).offset(0x154L).setu(v0);
    v0 = MEMORY.ref(4, t0).offset(0x150L).get();

    if((int)v0 >= 0) {
      v0 = MEMORY.ref(4, t0).offset(0x140L).get();
      v1 = MEMORY.ref(4, t0).offset(0x34L).get();
      v0 = v0 - v1;
      v1 = MEMORY.ref(4, t0).offset(0x144L).get();
      v0 = v0 << 16;
      lo = (int)v0 / (int)v1;
      v0 = lo;
      v0 = v0 & 0xffffL;
    } else {
      //LAB_800ded60
      v0 = MEMORY.ref(4, t0).offset(0x140L).get();
      v1 = MEMORY.ref(4, t0).offset(0x34L).get();
      v0 = v0 - v1;
      v1 = MEMORY.ref(4, t0).offset(0x144L).get();
      v0 = v0 << 16;
      lo = (int)v0 / (int)v1;
      v0 = lo;
      v0 = ~v0;
      v0 = v0 + 0x1L;
      v0 = v0 & 0xffffL;
    }

    //LAB_800ded90
    MEMORY.ref(4, t0).offset(0x15cL).setu(v0);
    lo = (long)(int)a3 * (int)a0_0 & 0xffff_ffffL;
    v1 = lo;
    v0 = a3 - 0x1L;
    lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
    v1 = MEMORY.ref(4, t0).offset(0x13cL).get();
    v0 = MEMORY.ref(4, t0).offset(0x30L).get();
    v1 = v1 - v0;
    v1 = v1 << 1;
    a2 = lo;
    v0 = a3 << 1;
    v1 = v1 - a2;
    lo = (int)v1 / (int)v0;
    v1 = lo;
    MEMORY.ref(4, t0).offset(0x174L).setu(MEMORY.ref(2, t0).offset(0x172L).getSigned());
    v0 = 0x1L;
    MEMORY.ref(2, t0).offset(0x172L).setu(v0);
    v0 = 0x2L;
    MEMORY.ref(2, t0).offset(0x170L).setu(v0);
    v0 = MEMORY.ref(2, t0).offset(0x130L).getSigned();
    MEMORY.ref(4, t0).offset(0x168L).setu(0);
    MEMORY.ref(4, t0).offset(0x160L).setu(0);
    v0 = v0 << 2;
    MEMORY.ref(2, t0).offset(0x134L).setu(v1);
    v1 = 0x800c_0000L;
    v1 = v1 + 0x6880L;
    v0 = v0 + v1;
    setScriptTempTicker((int)MEMORY.ref(4, v0).offset(0x0L).get(), MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e3e74", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800dee28L)
  public static long FUN_800dee28(final RunningScript a0) {
    long v0;
    long v1;
    long a1;
    long a3;
    long t0;
    final long t2;
    long t6;
    final long s0;
    final SVECTOR deltaMovement = new SVECTOR();
    final SVECTOR sp0x20 = new SVECTOR();

    final BigStruct s2 = a0.scriptState_04.deref().innerStruct_00.derefAs(BigStruct.class);
    final short deltaX = (short)a0.params_20.get(0).deref().get();
    final short deltaY = (short)a0.params_20.get(1).deref().get();
    final short deltaZ = (short)a0.params_20.get(2).deref().get();

    if(deltaX != 0 || deltaY != 0 || deltaZ != 0) {
      //LAB_800dee98
      //LAB_800dee9c
      //LAB_800deea0
      deltaMovement.set(deltaX, deltaY, deltaZ);
      SetRotMatrix(matrix_800c3548);
      SetTransMatrix(matrix_800c3548);
      transformToWorldspace(sp0x20, deltaMovement);

      final int collisionResult = FUN_800e88a0(s2.mrgAnimGroup_12e.get(), s2.coord2_14.coord, sp0x20);
      if(collisionResult >= 0) {
        FUN_800e6798(collisionResult, 0, s2.coord2_14.coord.transfer.getX(), s2.coord2_14.coord.transfer.getY(), s2.coord2_14.coord.transfer.getZ(), sp0x20);
      }

      //LAB_800def08
      s0 = -ratan2(sp0x20.getZ(), sp0x20.getX()) + 0xc01 & 0xfff; // Z, X is correct
    } else {
      sp0x20.set((short)0, (short)s2.coord2_14.coord.transfer.getY(), (short)0);
      s0 = s2.coord2Param_64.rotate.getY();
    }

    //LAB_800def28
    a1 = _800c68e8.getAddress();
    v0 = sp0x20.getX() + s2.coord2_14.coord.transfer.getX();
    MEMORY.ref(4, a1).offset(0xcL).setu(v0);
    v0 = sp0x20.getY() + s2.coord2_14.coord.transfer.getY();
    MEMORY.ref(4, a1).offset(0x10L).setu(v0);
    v0 = sp0x20.getZ() + s2.coord2_14.coord.transfer.getZ();
    MEMORY.ref(4, a1).offset(0x14L).setu(v0);
    final int sin = rsin(s0) * -s2.i_1c0.get() >> 12;
    final int cos = rcos(s0) * -s2.i_1c0.get() >> 12;
    t2 = sp0x20.getY() - s2.i_1bc.get();
    final long a0_0 = sp0x20.getY() + s2.i_1bc.get();

    //LAB_800defd4
    //TODO this is doing nothing...?
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      if(scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.getPointer() == s2.getAddress()) {
        break;
      }
    }

    //LAB_800df008
    //LAB_800df00c
    //LAB_800df02c
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      final BigStruct struct = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.derefAs(BigStruct.class);

      if(struct.getAddress() != s2.getAddress()) {
        if((struct.ui_190.get() & 0x10_0000L) != 0) {
          v0 = struct.coord2_14.coord.transfer.getX() - (s2.coord2_14.coord.transfer.getX() + sp0x20.getX() + sin);
          t0 = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
          v0 = struct.coord2_14.coord.transfer.getZ() - (s2.coord2_14.coord.transfer.getZ() + sp0x20.getZ() + cos);
          a3 = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
          v0 = s2.i_1b8.get() + struct.i_1b8.get();
          t6 = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
          a1 = struct.coord2_14.coord.transfer.getY() - struct.i_1bc.get();
          v1 = struct.coord2_14.coord.transfer.getY() + struct.i_1bc.get();
          v0 = t0 + a3;

          if((int)t6 >= (int)v0) {
            //LAB_800df104
            if((int)a1 >= (int)t2 && (int)a0_0 >= (int)a1 || (int)v1 >= (int)t2 && (int)a0_0 >= (int)v1) {
              //LAB_800df118
              a0.params_20.get(3).deref().set(i);
              return 0;
            }
          }
        }
      }

      //LAB_800df128
    }

    //LAB_800df13c
    a0.params_20.get(3).deref().set(-1);

    //LAB_800df14c
    return 0;
  }

  @Method(0x800df168L)
  public static long FUN_800df168(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800dfe0c(a0);
  }

  @Method(0x800df198L)
  public static long FUN_800df198(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800dfec8(a0);
  }

  @Method(0x800df1c8L)
  public static long FUN_800df1c8(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800dff68(a0);
  }

  @Method(0x800df1f8L)
  public static long FUN_800df1f8(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800dffa4(a0);
  }

  @Method(0x800df228L)
  public static long FUN_800df228(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800dffdc(a0);
  }

  @Method(0x800df258L)
  public static long FUN_800df258(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.coord2_14.coord.transfer.setX(a0.params_20.get(1).deref().get());
    struct.coord2_14.coord.transfer.setY(a0.params_20.get(2).deref().get());
    struct.coord2_14.coord.transfer.setZ(a0.params_20.get(3).deref().get());
    struct.us_170.set(0);
    return 0;
  }

  @Method(0x800df2b8L)
  public static long FUN_800df2b8(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    a0.params_20.get(1).deref().set(struct.coord2_14.coord.transfer.getX());
    a0.params_20.get(2).deref().set(struct.coord2_14.coord.transfer.getY());
    a0.params_20.get(3).deref().set(struct.coord2_14.coord.transfer.getZ());
    return 0;
  }

  @Method(0x800df314L)
  public static long FUN_800df314(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.coord2Param_64.rotate.x.set((short)a0.params_20.get(1).deref().get());
    struct.coord2Param_64.rotate.y.set((short)a0.params_20.get(2).deref().get());
    struct.coord2Param_64.rotate.z.set((short)a0.params_20.get(3).deref().get());
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800df374L)
  public static long FUN_800df374(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    a0.params_20.get(1).deref().set(struct.coord2Param_64.rotate.getX());
    a0.params_20.get(2).deref().set(struct.coord2Param_64.rotate.getY());
    a0.params_20.get(3).deref().set(struct.coord2Param_64.rotate.getZ());
    return 0;
  }

  @Method(0x800df3d0L)
  public static long FUN_800df3d0(final RunningScript a0) {
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e0018(a0);
  }

  @Method(0x800df410L)
  public static long FUN_800df410(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e0094(a0);
  }

  @Method(0x800df440L)
  public static long FUN_800df440(final RunningScript a0) {
    a0.params_20.get(4).set(a0.params_20.get(3).deref());
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800de668(a0);
  }

  @Method(0x800df488L)
  public static long FUN_800df488(final RunningScript a0) {
    a0.params_20.get(4).set(a0.params_20.get(3).deref());
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800de944(a0);
  }

  @Method(0x800df4d0L)
  public static long FUN_800df4d0(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e00cc(a0);
  }

  @Method(0x800df530L)
  public static long FUN_800df530(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e0184(a0);
  }

  @Method(0x800df620L)
  public static long FUN_800df620(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e02c0(a0);
  }

  @Method(0x800df650L)
  public static long FUN_800df650(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e02fc(a0);
  }

  @Method(0x800df680L)
  public static long FUN_800df680(final RunningScript a0) {
    a0.params_20.get(0).deref().set(a0.scriptState_04.deref().innerStruct_00.derefAs(BigStruct.class).s_178.get());
    return 0;
  }

  @Method(0x800df6a4L)
  public static long FUN_800df6a4(final RunningScript s1) {
    SetRotMatrix(matrix_800c3548);
    SetTransMatrix(matrix_800c3548);
    FUN_800e8104(new SVECTOR().set((short)s1.params_20.get(0).deref().get(), (short)s1.params_20.get(0).deref().get(), (short)s1.params_20.get(0).deref().get()));

    //LAB_800df744
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.derefAs(BigStruct.class).s_178.set((short)0);
    }

    //LAB_800df774
    return 0;
  }

  /** Script to rotate an object on the map (used for the little mouse thing running around in the Limestone Cave) */
  @Method(0x800df788L)
  public static long FUN_800df788(final RunningScript a0) {
    final BigStruct a3 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final int a2 = a0.params_20.get(4).deref().get();
    a3.ui_188.set(a2);

    // Added this to fix a /0 error in the retail code
    if(a2 == 0) {
      a3.s_17c.set((short)-1);
      a3.s_180.set((short)-1);
      a3.s_184.set((short)-1);
      return 0;
    }

    final int x = a0.params_20.get(1).deref().get();
    if(x >= 0) {
      a3.s_17c.set((short)(x / a2));
    } else {
      //LAB_800df7e0
      a3.s_17c.set((short)(~((~x + 1) / a2) + 1));
    }

    //LAB_800df800
    final int y = a0.params_20.get(2).deref().get();
    if(y >= 0) {
      a3.s_180.set((short)(y / a2));
    } else {
      //LAB_800df828
      a3.s_180.set((short)(~((~y + 1) / a2) + 1));
    }

    //LAB_800df844
    final int z = a0.params_20.get(3).deref().get();
    if(z >= 0) {
      a3.s_184.set((short)(z / a2));
    } else {
      //LAB_800df86c
      a3.s_184.set((short)(~((~z + 1) / a2) + 1));
    }

    //LAB_800df888
    return 0;
  }

  @Method(0x800df890L)
  public static long FUN_800df890(final RunningScript a0) {
    final BigStruct a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    final int p1 = a0.params_20.get(1).deref().get();
    final int p2 = a0.params_20.get(2).deref().get();
    final int p3 = a0.params_20.get(3).deref().get();

    // These are used as shorts everywhere else, but ints here... make sure there's no funny business going on
    assert p1 == (short)p1;
    assert p2 == (short)p2;
    assert p3 == (short)p3;

    a1.s_17c.set((short)p1);
    a1.s_180.set((short)p2);
    a1.s_184.set((short)p3);
    a1.ui_188.set(a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800df904L)
  public static long FUN_800df904(final RunningScript a0) {
    a0.params_20.get(5).set(a0.params_20.get(4).deref());
    a0.params_20.get(4).set(a0.params_20.get(3).deref());
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800deba0(a0);
  }

  @Method(0x800df954L)
  public static long FUN_800df954(final RunningScript a0) {
    final BigStruct s0 = a0.scriptState_04.deref().innerStruct_00.derefAs(BigStruct.class);
    s0.coord2Param_64.rotate.setY((short)(-ratan2((int)_800c68e8.offset(0x8L).get(), (int)_800c68e8.get()) + 0xc01 & 0xfff));
    s0.ui_188.set(0);
    return 0;
  }

  @Method(0x800df9a8L)
  public static long FUN_800df9a8(final RunningScript a0) {
    final DVECTOR sp0x18 = new DVECTOR();
    final MATRIX sp0x20 = new MATRIX();
    final MATRIX sp0x40 = new MATRIX();
    GsGetLws(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14, sp0x40, sp0x20);
    CPU.CTC2(sp0x20.getPacked(0), 0);
    CPU.CTC2(sp0x20.getPacked(2), 1);
    CPU.CTC2(sp0x20.getPacked(4), 2);
    CPU.CTC2(sp0x20.getPacked(6), 3);
    CPU.CTC2(sp0x20.getPacked(8), 4);
    CPU.CTC2(sp0x20.transfer.getX(), 5);
    CPU.CTC2(sp0x20.transfer.getY(), 6);
    CPU.CTC2(sp0x20.transfer.getZ(), 7);
    // 0, 0, 0
    CPU.MTC2(0, 0);
    CPU.MTC2(0, 1);
    CPU.COP2(0x180001L);
    sp0x18.setXY(CPU.MFC2(14));
    long sp60 = CPU.MFC2(8);
    long sp64 = CPU.CFC2(31);
    long sp68 = (int)CPU.MFC2(19) >> 2;
    a0.params_20.get(1).deref().set(sp0x18.getX() + 192);
    a0.params_20.get(2).deref().set(sp0x18.getY() + 128);
    // 0, -130, 0
    CPU.MTC2(-130 << 16, 0);
    CPU.MTC2(0, 1);
    CPU.COP2(0x180001L);
    sp0x18.setXY(CPU.MFC2(14));
    sp60 = CPU.MFC2(8);
    sp64 = CPU.CFC2(31);
    sp68 = (int)CPU.MFC2(19) >> 2;
    a0.params_20.get(3).deref().set(sp0x18.getX() + 192);
    a0.params_20.get(4).deref().set(sp0x18.getY() + 128);
    return 0;
  }

  @Method(0x800dfb28L)
  public static long FUN_800dfb28(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800bd808.get());
    return 0;
  }

  @Method(0x800dfb44L)
  public static long FUN_800dfb44(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e03a8(a0);
  }

  @Method(0x800dfb74L)
  public static long FUN_800dfb74(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e03e4(a0);
  }

  @Method(0x800dfba4L)
  public static long FUN_800dfba4(final RunningScript a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e0448(a0);
  }

  @Method(0x800dfbd4L)
  public static long FUN_800dfbd4(final RunningScript a0) {
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e04b4(a0);
  }

  @Method(0x800dfc00L)
  public static long scriptScaleXyz(final RunningScript a0) {
    final BigStruct a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    a1.scaleVector_fc.set(
      a0.params_20.get(1).deref().get(),
      a0.params_20.get(2).deref().get(),
      a0.params_20.get(3).deref().get()
    );
    return 0;
  }

  @Method(0x800dfc60L)
  public static long scriptScaleUniform(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.scaleVector_fc.setX(a0.params_20.get(1).deref().get());
    struct.scaleVector_fc.setY(a0.params_20.get(1).deref().get());
    struct.scaleVector_fc.setZ(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800dfca0L)
  public static long FUN_800dfca0(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).zOffset_a0.set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800dfcd8L)
  public static long FUN_800dfcd8(final RunningScript a0) {
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800e0520(a0);
  }

  @Method(0x800dfd8cL)
  public static long FUN_800dfd8c(final RunningScript a0) {
    final long v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, v1).offset(0x194L).setu(0x1L);
    MEMORY.ref(4, v1).offset(0x198L).setu(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800dfdd8L)
  public static long FUN_800dfdd8(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_194.set(0);
    return 0;
  }

  @Method(0x800dfe0cL)
  public static long FUN_800dfe0c(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final int index = a0.params_20.get(1).deref().get();

    struct.mrgAnimGroup_12e.set(index);
    struct.ub_9d.set((int)_800c6a50.offset(index * 0x4L).get());

    FUN_80020fe0(struct);
    FUN_800e0d18(struct, extendedTmdArr_800c6a00.get(index).deref(), struct.mrg_124.deref().getFile(index * 0x21 + 0x1, TmdAnimationFile::new));

    struct.us_12c.set(0);
    struct.ui_188.set(0);

    return 0;
  }

  @Method(0x800dfec8L)
  public static long FUN_800dfec8(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    struct.mrgAnimGroupIndex_132.set(a0.params_20.get(1).deref().get());
    struct.ub_a2.set(0);
    struct.ub_a3.set(0);

    final long mrgIndex = struct.mrgAnimGroup_12e.get() * 0x21L + struct.mrgAnimGroupIndex_132.get() + 0x1L;
    FUN_80021584(struct, struct.mrg_124.deref().getFile((int)mrgIndex, TmdAnimationFile::new));

    struct.us_12c.set(0);
    struct.ui_190.and(0x9fff_ffffL);

    return 0;
  }

  @Method(0x800dff68L)
  public static long FUN_800dff68(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).mrgAnimGroupIndex_132.get());
    return 0;
  }

  @Method(0x800dffa4L)
  public static long FUN_800dffa4(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_12a.set(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800dffdcL)
  public static long FUN_800dffdc(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_12c.get());
    return 0;
  }

  @Method(0x800e0018L)
  public static long FUN_800e0018(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    final long v1 = 0xc01L - ratan2(a0.params_20.get(3).deref().get() - struct.coord2_14.coord.transfer.getZ(), a0.params_20.get(1).deref().get() - struct.coord2_14.coord.transfer.getX()) & 0xfffL;
    struct.coord2Param_64.rotate.y.set((short)v1);
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800e0094L)
  public static long FUN_800e0094(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).s_128.set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e00ccL)
  public static long FUN_800e00cc(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    final long v0 = FUN_800e9018(struct.coord2_14.coord.transfer.getX(), struct.coord2_14.coord.transfer.getY(), struct.coord2_14.coord.transfer.getZ(), 0);
    a0.params_20.get(1).deref().set((int)v0);
    struct.ui_16c.set((int)v0);
    return 0;
  }

  @Method(0x800e0148L)
  public static long FUN_800e0148(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).s_172.get());
    return 0;
  }

  @Method(0x800e0184L)
  public static long FUN_800e0184(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).s_172.set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e01bcL)
  public static long FUN_800e01bc(final RunningScript a0) {
    FUN_800de138(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e0204L)
  public static long FUN_800e0204(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).smallerStructPtr_a4.deref().uba_04.get(a0.params_20.get(1).deref().get()).set(0);
    return 0;
  }

  @Method(0x800e02c0L)
  public static long FUN_800e02c0(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_170.get());
    return 0;
  }

  @Method(0x800e02fcL)
  public static long FUN_800e02fc(final RunningScript a0) {
    final BigStruct struct1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    struct1.s_178.set((short)a0.params_20.get(1).deref().get());

    if(a0.params_20.get(1).deref().get() != 0) {
      //LAB_800e035c
      for(int i = 0; i < scriptCount_800c6730.get(); i++) {
        final BigStruct struct2 = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.derefAs(BigStruct.class);

        if(struct2.scriptFileIndex_130.get() != struct1.scriptFileIndex_130.get()) {
          struct2.s_178.set((short)0);
        }

        //LAB_800e0390
      }
    }

    //LAB_800e03a0
    return 0;
  }

  @Method(0x800e03a8L)
  public static long FUN_800e03a8(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ObjTable_0c.nobj.get());
    return 0;
  }

  @Method(0x800e03e4L)
  public static long FUN_800e03e4(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final long a0_0 = a0.params_20.get(1).deref().get();

    if(a0_0 >= 0x20L) {
      struct.ui_f8.or(0x1L << a0_0 - 0x20L);
    } else {
      //LAB_800e0430
      struct.ui_f4.or(0x1L << a0_0);
    }

    //LAB_800e0440
    return 0;
  }

  @Method(0x800e0448L)
  public static long FUN_800e0448(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    if(a0.params_20.get(1).deref().get() >= 0x20L) {
      struct.ui_f8.and(~(0x1L << a0.params_20.get(1).deref().get() - 0x20L));
    } else {
      //LAB_800e0498
      struct.ui_f4.and(~(0x1L << a0.params_20.get(1).deref().get()));
    }

    //LAB_800e04ac
    return 0;
  }

  @Method(0x800e04b4L)
  public static long FUN_800e04b4(final RunningScript a0) {
    final BigStruct s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    s0.coord2Param_64.rotate.setY((short)(-ratan2((int)_800c6aa8.get(), (int)_800c6aa0.get()) + 0xc01 & 0xfff));
    s0.ui_188.set(0);
    return 0;
  }

  @Method(0x800e0520L)
  public static long FUN_800e0520(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_190
      .and(~(0x1L << a0.params_20.get(1).deref().get()))
      .or((a0.params_20.get(2).deref().get() & 0x1L) << a0.params_20.get(1).deref().get());

    return 0;
  }

  @Method(0x800e0614L)
  public static long FUN_800e0614(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.flatLightingEnabled_1c4.set(true);
    struct.flatLightRed_1c5.set(a0.params_20.get(1).deref().get());
    struct.flatLightGreen_1c6.set(a0.params_20.get(2).deref().get());
    struct.flatLightBlue_1c7.set(a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800e0684L)
  public static long FUN_800e0684(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.flatLightingEnabled_1c4.set(false);
    struct.flatLightRed_1c5.set(0x80);
    struct.flatLightGreen_1c6.set(0x80);
    struct.flatLightBlue_1c7.set(0x80);
    return 0;
  }

  @Method(0x800e07f0L)
  public static long FUN_800e07f0(final RunningScript a0) {
    final BigStruct s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    s0.mrgAnimGroupIndex_132.set(a0.params_20.get(1).deref().get());
    s0.ub_a3.set(0);
    s0.ub_a2.set(1);
    final long v0 = s0.mrg_124.getPointer() + ((short)s0.mrgAnimGroup_12e.get() * 0x21L + (short)s0.mrgAnimGroupIndex_132.get() + 0x1L) * 0x8L;
    final long a1 = s0.mrg_124.getPointer() + MEMORY.ref(4, v0).offset(0x8L).get();
    FUN_80021584(s0, MEMORY.ref(4, a1, TmdAnimationFile::new)); //TODO
    s0.us_12c.set(0);
    s0.ui_190.and(0x9fff_ffffL);
    return 0;
  }

  @Method(0x800e0894L)
  public static long FUN_800e0894(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.i_1ac.set(a0.params_20.get(1).deref().get());
    struct.i_1b0.set(a0.params_20.get(2).deref().get());
    struct.i_1b4.set(a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800e08f4L)
  public static long FUN_800e08f4(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).i_1a8.get());
    return 0;
  }

  @Method(0x800e0930L)
  public static long scriptSetAmbientColour(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.ambientColourEnabled_1c8.set(true);
    struct.ambientRed_1ca.set(a0.params_20.get(1).deref().get() & 0xffff);
    struct.ambientGreen_1cc.set(a0.params_20.get(2).deref().get() & 0xffff);
    struct.ambientBlue_1ce.set(a0.params_20.get(3).deref().get() & 0xffff);
    return 0;
  }

  @Method(0x800e09a0L)
  public static long scriptResetAmbientColour(final RunningScript a0) {
    final BigStruct obj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    obj.ambientRed_1ca.set(0x800);
    obj.ambientGreen_1cc.set(0x800);
    obj.ambientBlue_1ce.set(0x800);
    obj.ambientColourEnabled_1c8.set(false);
    return 0;
  }

  @Method(0x800e09e0L)
  public static long FUN_800e09e0(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).b_cc.set(1);
    return 0;
  }

  @Method(0x800e0a14L)
  public static long FUN_800e0a14(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).b_cc.set(0);
    return 0;
  }

  @Method(0x800e0a48L)
  public static long FUN_800e0a48(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.vector_10c.setX(a0.params_20.get(1).deref().get());
    struct.vector_10c.setZ(a0.params_20.get(2).deref().get());

    return 0;
  }

  @Method(0x800e0a94L)
  public static long FUN_800e0a94(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.vector_118.setX(a0.params_20.get(1).deref().get());
    struct.vector_118.setY(a0.params_20.get(2).deref().get());
    struct.vector_118.setZ(a0.params_20.get(3).deref().get());

    return 0;
  }

  @Method(0x800e0af4L)
  public static long FUN_800e0af4(final RunningScript a0) {
    a0.params_20.get(0).deref().set(bigStruct_800c6748.i_1ac.get());
    a0.params_20.get(1).deref().set(bigStruct_800c6748.i_1b0.get());
    a0.params_20.get(2).deref().set(bigStruct_800c6748.i_1b4.get());
    return 0;
  }

  @Method(0x800e0b34L)
  public static long FUN_800e0b34(final RunningScript a0) {
    if(a0.params_20.get(0).deref().get() == 0) {
      loadTimImage(_80010544.getAddress());
    }

    //LAB_800e0b68
    if(a0.params_20.get(0).deref().get() == 0x1L) {
      loadTimImage(_800d673c.getAddress());
    }

    //LAB_800e0b8c
    return 0;
  }

  @Method(0x800e0ba0L)
  public static long FUN_800e0ba0(final RunningScript a0) {
    final BigStruct struct = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.i_1b8.set(a0.params_20.get(1).deref().get());
    struct.i_1bc.set(a0.params_20.get(2).deref().get());
    struct.i_1c0.set(a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800e0c00L)
  public static long FUN_800e0c00(final RunningScript a0) {
    mrg10Loaded_800c68e0.setu(0);
    _800c6738.setu(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800e0c24L)
  public static long FUN_800e0c24(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)mrg10Loaded_800c68e0.getSigned());
    return 0;
  }

  @Method(0x800e0c40L)
  public static long FUN_800e0c40(final RunningScript a0) {
    _800c6738.oru(0x80L);
    _800c686e.setu(0);
    _800c687c.setu(a0.params_20.get(0).deref().get());
    _800c687e.setu(a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e0c80L)
  public static long FUN_800e0c80(final RunningScript a0) {
    a0.params_20.get(0).deref().set((int)_800c686e.getSigned());
    return 0;
  }

  @Method(0x800e0c9cL)
  public static long FUN_800e0c9c(final RunningScript a0) {
    _800c673c.setu(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800e0d18L)
  public static void FUN_800e0d18(final BigStruct struct, final ExtendedTmd extendedTmd, final TmdAnimationFile tmdAnimFile) {
    final int transferX = struct.coord2_14.coord.transfer.getX();
    final int transferY = struct.coord2_14.coord.transfer.getY();
    final int transferZ = struct.coord2_14.coord.transfer.getZ();

    //LAB_800e0d5c
    for(int i = 0; i < 7; i++) {
      struct.aub_ec.get(i).set(0);
    }

    final short count = (short)extendedTmd.tmdPtr_00.deref().tmd.header.nobj.get();
    struct.count_c8.set(count);
    long addr = addToLinkedListTail(count * 0x10L + count * 0x50L + count * 0x28L);
    struct.dobj2ArrPtr_00.set(MEMORY.ref(4, addr, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    addr += count * 0x10L;
    struct.coord2ArrPtr_04.set(MEMORY.ref(4, addr, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    addr += count * 0x50L;
    struct.coord2ParamArrPtr_08.set(MEMORY.ref(4, addr, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new)));
    struct.tmd_8c.set(extendedTmd.tmdPtr_00.deref().tmd);
    struct.tmdNobj_ca.set(count);

    if(!extendedTmd.ext_04.isNull()) {
      final SmallerStruct smallerStruct = MEMORY.ref(4, addToLinkedListTail(0x30L), SmallerStruct::new);
      struct.smallerStructPtr_a4.set(smallerStruct);
      smallerStruct.tmdExt_00.set(extendedTmd.ext_04.deref());

      //LAB_800e0e28
      for(int i = 0; i < 4; i++) {
        smallerStruct.tmdSubExtensionArr_20.get(i).set(smallerStruct.tmdExt_00.deref().tmdSubExtensionArr_00.get(i).deref());
        FUN_800de138(struct, i);
      }
    } else {
      //LAB_800e0e70
      struct.smallerStructPtr_a4.clear();
    }

    //LAB_800e0e74
    struct.ui_108.set((int)((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff_0000L) >> 11));
    final long v0_0 = extendedTmd.ptr_08.get();
    if(v0_0 != 0) {
      struct.ptr_a8.set(extendedTmd.getAddress() + v0_0 / 0x4L * 0x4L);

      //LAB_800e0eac
      for(int i = 0; i < 7; i++) {
        long v1 = struct.ptr_a8.get();
        v1 += MEMORY.ref(4, v1).offset(i * 0x4L).get() / 0x4L * 0x4L;
        struct.aui_d0.get(i).set(v1);
        FUN_8002246c(struct, i);
      }
    } else {
      //LAB_800e0ef0
      struct.ptr_a8.set(extendedTmd.ptr_08.getAddress());

      //LAB_800e0f00
      for(int i = 0; i < 7; i++) {
        struct.aui_d0.get(i).set(0);
      }
    }

    //LAB_800e0f10
    adjustTmdPointers(struct.tmd_8c.deref());
    initObjTable2(struct.ObjTable_0c, struct.dobj2ArrPtr_00.deref(), struct.coord2ArrPtr_04.deref(), struct.coord2ParamArrPtr_08.deref(), struct.count_c8.get());

    struct.coord2_14.param.set(struct.coord2Param_64);

    GsInitCoordinate2(null, struct.coord2_14);
    FUN_80021ca0(struct.ObjTable_0c, struct.tmd_8c.deref(), struct.coord2_14, struct.count_c8.get(), (short)(struct.tmdNobj_ca.get() + 1));

    struct.zOffset_a0.set((short)0);
    struct.ub_a2.set(0);
    struct.ub_a3.set(0);
    struct.ui_f4.set(0);
    struct.ui_f8.set(0);

    FUN_80021584(struct, tmdAnimFile);

    struct.coord2_14.coord.transfer.setX(transferX);
    struct.coord2_14.coord.transfer.setY(transferY);
    struct.coord2_14.coord.transfer.setZ(transferZ);

    struct.scaleVector_fc.setX(0x1000);
    struct.scaleVector_fc.setY(0x1000);
    struct.scaleVector_fc.setZ(0x1000);
  }

  @Method(0x800e0ff0L)
  public static void FUN_800e0ff0(final int index, final ScriptState<BigStruct> scriptState, final BigStruct bigStruct) {
    final BigStruct puVar1 = scriptState.innerStruct_00.deref();

    if(puVar1.s_178.get() != 0) {
      SetRotMatrix(matrix_800c3548);
      SetTransMatrix(matrix_800c3548);
      FUN_800e8104(new SVECTOR().set((short)puVar1.coord2_14.coord.transfer.getX(), (short)puVar1.coord2_14.coord.transfer.getY(), (short)puVar1.coord2_14.coord.transfer.getZ()));
    }

    if(puVar1.s_128.get() == 0) {
      if(puVar1.ui_188.get() != 0) {
        puVar1.ui_188.sub(1);
        puVar1.coord2Param_64.rotate.x.add(puVar1.s_17c.get());
        puVar1.coord2Param_64.rotate.y.add(puVar1.s_180.get());
        puVar1.coord2Param_64.rotate.z.add(puVar1.s_184.get());
      }

      if(puVar1.mrgAnimGroup_12e.get() == 0) {
        FUN_800217a4(puVar1);
      } else {
        FUN_800214bc(puVar1);
      }

      if(puVar1.us_12a.get() == 0) {
        FUN_80020b98(puVar1);
        if(puVar1.us_12c.get() == 1 && (puVar1.ui_190.get() & 0x2000_0000L) != 0) {
          puVar1.mrgAnimGroupIndex_132.set(0);
          FUN_80021584(puVar1, puVar1.mrg_124.deref().getFile(puVar1.mrgAnimGroup_12e.get() * 0x21 + 0x1, TmdAnimationFile::new));
          puVar1.us_12c.set(0);
          puVar1.ui_190.and(0x9fff_ffffL);
        }
      }
    }

    if(puVar1.s_9e.get() == 0) {
      puVar1.us_12c.set(1);

      if((puVar1.ui_190.get() & 0x4000_0000L) != 0) {
        puVar1.us_12a.set(1);
      }
    } else {
      puVar1.us_12c.set(0);
    }

    if(puVar1.ui_194.get() != 0) {
      FUN_800e4774(puVar1, puVar1.ui_198.get());
    }

    if((puVar1.ui_190.get() & 0x800_0000L) != 0) {
      FUN_800e4378(puVar1, 0x1000_0000L);
    }

    if((puVar1.ui_190.get() & 0x200_0000L) != 0) {
      FUN_800e4378(puVar1, 0x400_0000L);
    }

    if((puVar1.ui_190.get() & 0x80_0000L) != 0) {
      FUN_800e450c(puVar1, 0x100_0000L);
    }

    if((puVar1.ui_190.get() & 0x20_0000L) != 0) {
      FUN_800e450c(puVar1, 0x40_0000L);
    }
  }

  @Method(0x800e123cL)
  public static void FUN_800e123c(final int index, final ScriptState<BigStruct> scriptState, final BigStruct bigStruct) {
    final BigStruct struct = scriptState.innerStruct_00.deref();

    if(struct.s_128.get() == 0) {
      if(struct.flatLightingEnabled_1c4.get()) {
        final GsF_LIGHT light0 = new GsF_LIGHT();
        light0.direction_00.setX(0);
        light0.direction_00.setY(0x1000);
        light0.direction_00.setZ(0);
        light0.r_0c.set(struct.flatLightRed_1c5);
        light0.g_0d.set(struct.flatLightGreen_1c6);
        light0.b_0e.set(struct.flatLightBlue_1c7);
        GsSetFlatLight(0, light0);

        final GsF_LIGHT light1 = new GsF_LIGHT();
        light1.direction_00.setX(0x1000);
        light1.direction_00.setY(0);
        light1.direction_00.setZ(0);
        light1.r_0c.set(struct.flatLightRed_1c5);
        light1.g_0d.set(struct.flatLightGreen_1c6);
        light1.b_0e.set(struct.flatLightBlue_1c7);
        GsSetFlatLight(1, light1);

        final GsF_LIGHT light2 = new GsF_LIGHT();
        light2.direction_00.setX(0);
        light2.direction_00.setY(0);
        light2.direction_00.setZ(0x1000);
        light2.r_0c.set(struct.flatLightRed_1c5);
        light2.g_0d.set(struct.flatLightGreen_1c6);
        light2.b_0e.set(struct.flatLightBlue_1c7);
        GsSetFlatLight(2, light2);
      }

      //LAB_800e1310
      if(struct.ambientColourEnabled_1c8.get()) {
        GsSetAmbient(struct.ambientRed_1ca.get(), struct.ambientGreen_1cc.get(), struct.ambientBlue_1ce.get());
      }

      //LAB_800e1334
      FUN_800211d8(struct);

      if(struct.flatLightingEnabled_1c4.get()) {
        GsSetFlatLight(0, GsF_LIGHT_0_800c66d8);
        GsSetFlatLight(1, GsF_LIGHT_1_800c66e8);
        GsSetFlatLight(2, GsF_LIGHT_2_800c66f8);
      }

      //LAB_800e1374
      if(struct.ambientColourEnabled_1c8.get()) {
        GsSetAmbient(0x800, 0x800, 0x800);
      }

      //LAB_800e1390
      FUN_800ef0f8(struct, struct._1d0);
    }

    //LAB_800e139c
  }

  @Method(0x800e13b0L)
  public static void executeSceneGraphicsLoadingStage(final int index) {
    switch((int)loadingStage_800c68e4.get()) {
      case 0x0:
        loadTimImage(_80010544.getAddress());

        if(_80050274.get() != submapCut_80052c30.get()) {
          _800bda08.setu(_80050274);
          _80050274.setu(submapCut_80052c30.get());
        }

        //LAB_800e1440
        GsF_LIGHT_0_800c66d8.direction_00.setX(0);
        GsF_LIGHT_0_800c66d8.direction_00.setY(0x1000);
        GsF_LIGHT_0_800c66d8.direction_00.setZ(0);
        GsF_LIGHT_0_800c66d8.r_0c.set(0x80);
        GsF_LIGHT_0_800c66d8.g_0d.set(0x80);
        GsF_LIGHT_0_800c66d8.b_0e.set(0x80);
        GsSetFlatLight(0, GsF_LIGHT_0_800c66d8);
        GsF_LIGHT_1_800c66e8.direction_00.setX(0);
        GsF_LIGHT_1_800c66e8.direction_00.setY(0x1000);
        GsF_LIGHT_1_800c66e8.direction_00.setZ(0);
        GsF_LIGHT_1_800c66e8.r_0c.set(0);
        GsF_LIGHT_1_800c66e8.g_0d.set(0);
        GsF_LIGHT_1_800c66e8.b_0e.set(0);
        GsSetFlatLight(0x1L, GsF_LIGHT_1_800c66e8);
        GsF_LIGHT_2_800c66f8.direction_00.setX(0);
        GsF_LIGHT_2_800c66f8.direction_00.setY(0x1000);
        GsF_LIGHT_2_800c66f8.direction_00.setZ(0);
        GsF_LIGHT_2_800c66f8.r_0c.set(0);
        GsF_LIGHT_2_800c66f8.g_0d.set(0);
        GsF_LIGHT_2_800c66f8.b_0e.set(0);
        GsSetFlatLight(0x2L, GsF_LIGHT_2_800c66f8);

        GsSetAmbient(0x800, 0x800, 0x800);
        loadingStage_800c68e4.addu(0x1L);
        break;

      case 0x1:
        if(index == -0x1L) {
          break;
        }

        final long old = _800bd808.get();
        _800bd808.setu(_800d610c.offset(submapCut_80052c30.get() * 0x2L));

        //LAB_800e15b8
        //LAB_800e15ac
        //LAB_800e15b8
        //LAB_800e15b8
        if(_800bd808.get() != old) {
          FUN_8001ad18();
          unloadSoundFile(4);
          FUN_8001eadc((int)_800bd808.get());
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

        _800bd782.setu(0);

        final long ret = FUN_8001c60c();
        if(ret == -0x1L) {
          FUN_8001ae90();

          //LAB_800e15b8
          _800bd782.setu(0x1L);
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        if(ret == -0x2L) {
          FUN_8001ada0();

          //LAB_800e15b8
          _800bd782.setu(0x1L);
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        if(ret == -0x3L) {
          //LAB_800e15b8
          _800bd782.setu(0x1L);
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        //LAB_800e15c0
        loadMusicPackage(ret, 0);
        loadingStage_800c68e4.addu(0x1L);
        break;

      case 0x2:
        if(_800bd782.get() != 0 && (getLoadedDrgnFiles() & 0x2L) == 0) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x3:
        _800f9eac.setu(0);

        loadSmapMedia();

        if(_800f9eac.get() == 0x1L) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x4:
        loadSmapMedia();

        if(_800f9eac.get() == 0x2L) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x5:
        mrg1Loaded_800c68d0.setu(0);
        mrg0Loaded_800c6874.setu(0);
        mrg1Addr_800c68d8.clear();
        mrg0Addr_800c6878.clear();

        final IntRef drgnIndex = new IntRef();
        final IntRef fileIndex = new IntRef();

        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);

        if(drgnIndex.get() == 0x1L || drgnIndex.get() == 0x2L || drgnIndex.get() == 0x3L || drgnIndex.get() == 0x4L) {
          //LAB_800e1720
          //LAB_800e17c4
          //LAB_800e17d8
          loadDrgnBinFile(drgnIndex.get() + 2, fileIndex.get() + 1, 0, getMethodAddress(SMap.class, "FUN_800e3d80", long.class, long.class, long.class), 0, 0x2L);
          loadDrgnBinFile(drgnIndex.get() + 2, fileIndex.get() + 2, 0, getMethodAddress(SMap.class, "FUN_800e3d80", long.class, long.class, long.class), 0x1L, 0x2L);
        }

        loadingStage_800c68e4.addu(0x1L);
        break;

      case 0x6:
        if(mrg0Loaded_800c6874.get() == 0x1L && mrg1Loaded_800c68d0.get() == 0x1L) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x7:
        _800c6870.setu(0);
        _800c686c.setu(0);
        loadingStage_800c68e4.addu(0x1L);
        callbackIndex_800c6968.setu(_800f5cd4.offset(submapCut_80052c30.get() * 0x2L));
        break;

      case 0x8:
        callbackArr_800f5ad4.get((int)callbackIndex_800c6968.get()).deref().run();

        if(_800c686c.get() != 0) {
          //LAB_800e18a4
          //LAB_800e18a8
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x9:
        FUN_800218f0();

        final int fileCount = (int)mrg1Addr_800c68d8.deref().count.get();
        final long secondLastFileIndex = fileCount - 0x2L;

        _800c672c.setu(secondLastFileIndex);
        scriptCount_800c6730.setu(secondLastFileIndex);

        final long s3;
        final long s4;
        final MrgEntry lastEntry = mrg1Addr_800c68d8.deref().entries.get(fileCount - 1);
        if(lastEntry.size.get() != 0x4L) {
          final ArrayRef<UnsignedIntRef> lastFileData = mrg1Addr_800c68d8.deref().getFile(fileCount - 1, ArrayRef.of(UnsignedIntRef.class, 2, 4, UnsignedIntRef::new));
          s3 = lastFileData.get(0).get(); // Second last int before padding
          s4 = lastFileData.get(1).get(); // Last int before padding
        } else {
          s3 = 0;
          s4 = 0;
        }

        //LAB_800e1914
        _800c69f0.setu(secondLastFileIndex * 0x22L);
        _800c69f4.setu(secondLastFileIndex * 0x22L + 0x1L);
        _800c69f8.setu(secondLastFileIndex * 0x22L + 0x2L);

        loadTimImage(mrg0Addr_800c6878.deref().getFile((int)_800c69f0.get()));
        loadTimImage(mrg0Addr_800c6878.deref().getFile((int)_800c69f4.get()));

        final TimHeader tim = parseTimHeader(MEMORY.ref(4, mrg0Addr_800c6878.deref().getFile((int)_800c69f8.get())).offset(0x4L));
        final RECT imageRect = new RECT();
        imageRect.x.set(tim.imageRect.x);
        imageRect.y.set(tim.imageRect.y);
        imageRect.w.set(tim.imageRect.w);
        imageRect.h.set((short)0x80);

        LoadImage(imageRect, tim.imageAddress.get());
        DrawSync(0);

        final int scriptStateIndex = allocateScriptState(0, 0, false, null, 0);
        scriptIndex_800c6740.set(scriptStateIndex);
        loadScriptFile(scriptStateIndex, mrg1Addr_800c68d8.deref().getFile(0, ScriptFile::new), "SMAP MRG1 File 0", (int)mrg1Addr_800c68d8.deref().entries.get(0).size.get());

        //LAB_800e1a38
        for(int i = 0; i < scriptCount_800c6730.get(); i++) {
          extendedTmdArr_800c6a00.get(i).set(mrg0Addr_800c6878.deref().getFile(i * 0x21, ExtendedTmd::new));
          _800c6a50.offset(i * 0x4L).setu(i + 0x81L);

          if(i + 0x1L == s3) {
            _800c6a50.offset(i * 0x4L).setu(0x92L);
          }

          //LAB_800e1a74
          if(i + 0x1L == s4) {
            _800c6a50.offset(i * 0x4L).setu(0x93L);
          }

          //LAB_800e1a80
        }

        //LAB_800e1a8c
        //LAB_800e1abc
        //TODO make sure these loops are right
        for(int i = 0; i < scriptCount_800c6730.get(); i++) {
          //LAB_800e1ae0
          for(int n = i + 1; n < scriptCount_800c6730.get(); n++) {
            if(extendedTmdArr_800c6a00.get(n).getPointer() == extendedTmdArr_800c6a00.get(i).getPointer()) {
              _800c6a50.offset(n * 0x4L).setu(0x80L);
            }

            //LAB_800e1af4
          }

          //LAB_800e1b0c
        }

        //LAB_800e1b20
        //LAB_800e1b54
        for(int i = 0; i < scriptCount_800c6730.get(); i++) {
          final int index2 = allocateScriptState(0x210L);
          scriptStateIndices_800c6880.get(i).set(index2);
          setScriptTicker(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e0ff0", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriConsumerRef::new));
          setScriptRenderer(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e123c", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriConsumerRef::new));
          setScriptDestructor(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "scriptDestructor", int.class, ScriptState.classFor(BigStruct.class), BigStruct.class), TriConsumerRef::new));
          loadScriptFile(index2, mrg1Addr_800c68d8.deref().getFile(i + 1, ScriptFile::new), "SMAP MRG1 File %d".formatted(i + 1), (int)mrg1Addr_800c68d8.deref().entries.get(i + 1).size.get());

          final BigStruct struct = scriptStatePtrArr_800bc1c0.get(index2).deref().innerStruct_00.derefAs(BigStruct.class);
          struct.ub_9d.set((int)_800c6a50.offset(1, i * 0x4L).get());

          FUN_80020a00(struct, extendedTmdArr_800c6a00.get(i).deref(), mrg0Addr_800c6878.deref().getFile(i * 33 + 1, TmdAnimationFile::new));

          if(i == 0) {
            FUN_800e0d18(bigStruct_800c6748, extendedTmdArr_800c6a00.get(0).deref(), mrg0Addr_800c6878.deref().getFile(1, TmdAnimationFile::new));
            bigStruct_800c6748.coord2_14.coord.transfer.setX(0);
            bigStruct_800c6748.coord2_14.coord.transfer.setY(0);
            bigStruct_800c6748.coord2_14.coord.transfer.setZ(0);
            bigStruct_800c6748.coord2Param_64.rotate.x.set((short)0);
            bigStruct_800c6748.coord2Param_64.rotate.y.set((short)0);
            bigStruct_800c6748.coord2Param_64.rotate.z.set((short)0);
          }

          //LAB_800e1c50
          struct.mrg_124.set(mrg0Addr_800c6878.deref());
          struct.s_128.set((short)0);
          struct.us_12a.set(0);
          struct.us_12c.set(0);
          struct.mrgAnimGroup_12e.set(i);
          struct.scriptFileIndex_130.set(i);
          struct.mrgAnimGroupIndex_132.set(0);
          struct.s_134.set((short)0);
          struct.ui_144.set(0);
          struct.ui_16c.set(-1);
          struct.us_170.set(0);
          struct.s_172.set((short)0);
          struct.ui_188.set(0);
          struct.ui_194.set(0);
          struct.i_19c.set(-1);
          struct.i_1a0.set(20);
          struct.i_1a4.set(20);
          struct.i_1a8.set(-1);
          struct.i_1ac.set(20);
          struct.i_1b0.set(20);
          struct.i_1b4.set(50);
          struct.i_1b8.set(20);
          struct.i_1bc.set(20);
          struct.i_1c0.set(50);
          struct.flatLightingEnabled_1c4.set(false);
          struct.flatLightRed_1c5.set(0x80);
          struct.flatLightGreen_1c6.set(0x80);
          struct.flatLightBlue_1c7.set(0x80);

          if(i == 0) {
            struct.s_178.set((short)1);
          } else {
            //LAB_800e1ce0
            struct.s_178.set((short)0);
          }

          //LAB_800e1ce4
          final long v1 = _800bd818.offset(i * 0x14L).getAddress();

          struct.coord2_14.coord.transfer.setX((int)MEMORY.ref(4, v1).offset(0x00L).get());
          struct.coord2_14.coord.transfer.setY((int)MEMORY.ref(4, v1).offset(0x04L).get());
          struct.coord2_14.coord.transfer.setZ((int)MEMORY.ref(4, v1).offset(0x08L).get());

          struct.coord2Param_64.rotate.x.set((short)MEMORY.ref(2, v1).offset(0x0cL).getSigned());
          struct.coord2Param_64.rotate.y.set((short)MEMORY.ref(2, v1).offset(0x0eL).getSigned());
          struct.coord2Param_64.rotate.z.set((short)MEMORY.ref(2, v1).offset(0x10L).getSigned());

          struct.ui_18c.set(0x7L);
          struct.ui_190.set(0);

          if(i == 0) {
            struct.ui_190.set(0x1L);
            FUN_800e4d88(index, struct);
          }

          //LAB_800e1d60
          FUN_800f04ac(struct._1d0.getAddress());
        }

        //LAB_800e1d88
        _800c6708.setu(0);
        _800c670a.setu(0);
        _800c670c.setu(0);
        _800c670e.setu(0);
        mrg10Addr_800c6710.clear();
        _800c6714.setu(0);
        _800c6718.setu(0);
        _800c671c.setu(0);
        _800c6720.setu(0);

        _800c6738.setu(0);
        _800c673c.setu(0x3cL);

        _800c686e.setu(0);
        _800c687c.setu(0);
        _800c687e.setu(0);
        mrg10Loaded_800c68e0.setu(0);
        loadingStage_800c68e4.addu(0x1L);

        _800c6734.setu(addToLinkedListTail(0x28L));
        _800c69fc.setu(addToLinkedListTail(0x140L));

        _800c6aa0.setu(rview2_800bd7e8.viewpoint_00.getX() - rview2_800bd7e8.refpoint_0c.getX());
        _800c6aa4.setu(rview2_800bd7e8.viewpoint_00.getY() - rview2_800bd7e8.refpoint_0c.getY());
        _800c6aa8.setu(rview2_800bd7e8.viewpoint_00.getZ() - rview2_800bd7e8.refpoint_0c.getZ());

        loadTimImage(timFile_800d689c.getAddress());
        FUN_800f3af8();

        //LAB_800e1ea8
        for(int i = 0; i < 0xa; i++) {
          _800c6734.deref(4).offset(i * 0x4L).setu(0);
        }

        //LAB_800e1ecc
        for(int i = 0; i < 0x20; i++) {
          _800c6970.offset(i * 0x4L).setu(-0x1L);
        }

        clearJoypadInput();
        _800c6aac.setu(0xaL);
        _800bd7b8.setu(0);
        break;

      case 0xa:
        if(_800bb168.get() != 0 || _800c6aac.get() != 0) {
          //LAB_800e1f24
          if(_800c6aac.get() != 0) {
            _800c6aac.subu(0x1L);
          }

          //LAB_800e1f38
          clearJoypadInput();
        }

        //LAB_800e1f40
        _800bd7b4.setu(0x1L);
        if(_800c6738.get() != 0) {
          FUN_800e2648();
        }

        //LAB_800e1f60
    }
  }

  /** Handles cutscene movement */
  @Method(0x800e1f90L)
  public static long FUN_800e1f90(final int index, final ScriptState<BigStruct> scriptState, final BigStruct bigStruct) {
    final BigStruct s1 = scriptState.innerStruct_00.deref();

    if((int)s1.ui_190.get() < 0) {
      return 0;
    }

    long x = 0;
    if((s1.vec_148.getX() & 0x7fff_ffffL) != 0) {
      x = s1.vec_148.getX();
    }

    //LAB_800e1fe4
    long y = 0;
    if((s1.vec_148.getY() & 0x7fff_ffffL) != 0) {
      y = s1.vec_148.getY();
    }

    //LAB_800e1ffc
    long z = 0;
    if((s1.vec_148.getZ() & 0x7fff_ffffL) != 0) {
      z = s1.vec_148.getZ();
    }

    //LAB_800e2014
    s1.vec_160.x.add(s1.vec_154.getX());
    s1.vec_160.z.add(s1.vec_154.getZ());
    s1.vec_160.y.add(s1.vec_154.getY());

    if((s1.vec_160.getX() & 0x1_0000L) != 0) {
      s1.vec_160.x.and(0xffff);

      // Not sure why this signedness check is different than the others, just gonna leave it for now
      if((s1.vec_148.getX() & 0x8000_0000L) == 0) {
        x = x + 0x1L;
      } else {
        //LAB_800e2074
        x = x - 0x1L;
      }
    }

    //LAB_800e2078
    if((s1.vec_160.getY() & 0x1_0000L) != 0) {
      s1.vec_160.y.and(0xffff);

      if(s1.vec_148.getY() >= 0) {
        y = y + 0x1L;
      } else {
        //LAB_800e20a4
        y = y - 0x1L;
      }
    }

    //LAB_800e20a8
    if((s1.vec_160.getZ() & 0x1_0000L) != 0) {
      s1.vec_160.z.and(0xffff);

      if(s1.vec_148.getZ() >= 0) {
        z = z + 0x1L;
      } else {
        //LAB_800e20d4
        z = z - 0x1L;
      }
    }

    //LAB_800e20d8
    s1.ui_144.decr();

    if(s1.s_172.get() == 0) {
      final SVECTOR sp0x20 = new SVECTOR();

      if((s1.ui_190.get() & 0x1L) != 0) {
        final SVECTOR sp0x18 = new SVECTOR();
        sp0x18.set((short)x, (short)y, (short)z);
        SetRotMatrix(matrix_800c3548);
        SetTransMatrix(matrix_800c3548);
        transformToWorldspace(sp0x20, sp0x18);
      } else {
        //LAB_800e2134
        sp0x20.set((short)x, (short)y, (short)z);
      }

      //LAB_800e2140
      final int s3 = FUN_800e88a0(s1.mrgAnimGroup_12e.get(), s1.coord2_14.coord, sp0x20);
      if(s3 >= 0) {
        if(FUN_800e6798(s3, 0, s1.coord2_14.coord.transfer.getX(), s1.coord2_14.coord.transfer.getY(), s1.coord2_14.coord.transfer.getZ(), sp0x20) != 0) {
          s1.coord2_14.coord.transfer.x.add(sp0x20.getX());
          s1.coord2_14.coord.transfer.setY(sp0x20.getY());
          s1.coord2_14.coord.transfer.z.add(sp0x20.getZ());
        }
      }

      //LAB_800e21bc
      s1.ui_16c.set(s3);
    } else {
      //LAB_800e21c4
      s1.coord2_14.coord.transfer.x.add((int)x);
      s1.coord2_14.coord.transfer.z.add((int)z);
      s1.coord2_14.coord.transfer.y.add((int)y);
    }

    //LAB_800e21e8
    if(s1.ui_144.get() != 0) {
      //LAB_800e21f8
      return 0;
    }

    //LAB_800e2200
    s1.us_170.set(0);

    //LAB_800e2204
    return 0x1L;
  }

  @Method(0x800e2220L)
  public static void FUN_800e2220() {
    deallocateScriptAndChildren(scriptIndex_800c6740.get());

    if(!mrg10Addr_800c6710.isNull()) {
      removeFromLinkedList(mrg10Addr_800c6710.getPointer());
    }

    //LAB_800e226c
    _800bd7b4.setu(0);

    //TODO 0x14-byte struct
    long s0 = _800bd818.getAddress();

    //LAB_800e229c
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      final int v1 = scriptStateIndices_800c6880.get(i).get();
      if(v1 != -1) {
        final BigStruct struct = scriptStatePtrArr_800bc1c0.get(v1).deref().innerStruct_00.derefAs(BigStruct.class);
        MEMORY.ref(4, s0).offset(0x00L).setu(struct.coord2_14.coord.transfer.getX());
        MEMORY.ref(4, s0).offset(0x04L).setu(struct.coord2_14.coord.transfer.getY());
        MEMORY.ref(4, s0).offset(0x08L).setu(struct.coord2_14.coord.transfer.getZ());
        MEMORY.ref(2, s0).offset(0x0cL).setu(struct.coord2Param_64.rotate.getX());
        MEMORY.ref(2, s0).offset(0x0eL).setu(struct.coord2Param_64.rotate.getY());
        MEMORY.ref(2, s0).offset(0x10L).setu(struct.coord2Param_64.rotate.getZ());
        deallocateScriptAndChildren(scriptStateIndices_800c6880.get(i).get());
      } else {
        //LAB_800e231c
        MEMORY.ref(4, s0).offset(0x0L).setu(0);
        MEMORY.ref(4, s0).offset(0x4L).setu(0);
        MEMORY.ref(4, s0).offset(0x8L).setu(0);
        MEMORY.ref(2, s0).offset(0xcL).setu(0);
        MEMORY.ref(2, s0).offset(0xeL).setu(0);
        MEMORY.ref(2, s0).offset(0x10L).setu(0);
      }

      //LAB_800e2338
      s0 = s0 + 0x14L;
    }

    //LAB_800e2350
    _800bd7b0.setu(0x1L);
    removeFromLinkedList(mrg0Addr_800c6878.getPointer());
    removeFromLinkedList(mrg1Addr_800c68d8.getPointer());
    FUN_80029e04(null);

    _800c6870.setu(-0x1L);
    callbackArr_800f5ad4.get((int)callbackIndex_800c6968.get()).deref().run();

    _800f9eac.setu(-0x1L);
    loadSmapMedia();
    FUN_80020fe0(bigStruct_800c6748);
    removeFromLinkedList(_800c6734.get());
    removeFromLinkedList(_800c69fc.get());
    loadTimImage(_80010544.getAddress());
  }

  @Method(0x800e2428L)
  public static void FUN_800e2428(long a0) {
    final SVECTOR sp0x10 = new SVECTOR();
    final SVECTOR sp0x18 = new SVECTOR();
    final MATRIX sp0x20 = new MATRIX();
    final MATRIX sp0x40 = new MATRIX();
    long sp60;
    long sp64;
    long sp68;

    GsGetLws(scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14, sp0x40, sp0x20);
    CPU.CTC2((sp0x20.get(1) & 0xffffL) << 16 | sp0x20.get(0) & 0xffffL, 0);
    CPU.CTC2((sp0x20.get(3) & 0xffffL) << 16 | sp0x20.get(2) & 0xffffL, 1);
    CPU.CTC2((sp0x20.get(5) & 0xffffL) << 16 | sp0x20.get(4) & 0xffffL, 2);
    CPU.CTC2((sp0x20.get(7) & 0xffffL) << 16 | sp0x20.get(6) & 0xffffL, 3);
    CPU.CTC2(                                  sp0x20.get(8) & 0xffffL, 4);
    CPU.CTC2(sp0x20.transfer.getX(), 5);
    CPU.CTC2(sp0x20.transfer.getY(), 6);
    CPU.CTC2(sp0x20.transfer.getZ(), 7);

    sp0x10.set((short)0, (short)0, (short)0);
    CPU.MTC2(sp0x10.getXY(), 0);
    CPU.MTC2(sp0x10.getZ(), 1);
    CPU.COP2(0x180001L);
    sp0x18.setXY(CPU.MFC2(14));
    sp60 = CPU.MFC2(8);
    sp64 = CPU.CFC2(31);
    sp68 = (int)CPU.MFC2(19) >> 2;

    a0 = _800c68e8.getAddress();
    sp0x10.set((short)0, (short)-130, (short)0);
    MEMORY.ref(4, a0).offset(0x70L).setu(sp0x18.getX() + 0xc0L);
    MEMORY.ref(4, a0).offset(0x74L).setu(sp0x18.getY() + 0x80L);
    CPU.MTC2(sp0x10.getXY(), 0);
    CPU.MTC2(sp0x10.getZ(), 1);
    CPU.COP2(0x180001L);
    sp0x18.setXY(CPU.MFC2(14));
    sp60 = CPU.MFC2(8);
    sp64 = CPU.CFC2(31);
    sp68 = (int)CPU.MFC2(19) >> 2;
    MEMORY.ref(4, a0).offset(0x78L).setu(sp0x18.getX() + 0xc0L);
    MEMORY.ref(4, a0).offset(0x7cL).setu(sp0x18.getY() + 0x80L);

    sp0x10.set((short)-20, (short)0, (short)0);
    CPU.MTC2(sp0x10.getXY(), 0);
    CPU.MTC2(sp0x10.getZ(), 1);
    CPU.COP2(0x180001L);
    sp0x18.setXY(CPU.MFC2(14));
    sp60 = CPU.MFC2(8);
    sp64 = CPU.CFC2(31);
    sp68 = (int)CPU.MFC2(19) >> 2;
    MEMORY.ref(4, a0).offset(0x68L).setu(sp0x18.getX() + 0xc0L);
    MEMORY.ref(4, a0).offset(0x6cL).setu(sp0x18.getY() + 0x80L);

    sp0x10.set((short)20, (short)0, (short)0);
    CPU.MTC2(sp0x10.getXY(), 0);
    CPU.MTC2(sp0x10.getZ(), 1);
    CPU.COP2(0x180001L);
    sp0x18.setXY(CPU.MFC2(14));
    sp60 = CPU.MFC2(8);
    sp64 = CPU.CFC2(31);
    sp68 = (int)CPU.MFC2(19) >> 2;
    MEMORY.ref(4, a0).offset(0x60L).setu(sp0x18.getX() + 0xc0L);
    MEMORY.ref(4, a0).offset(0x64L).setu(sp0x18.getY() + 0x80L);
  }

  @Method(0x800e2648L)
  public static void FUN_800e2648() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long s0;
    final long s1;
    final long s2;
    final long s3;
    final long s4;
    long s5;
    final long s6;
    final long s7;
    final long fp;

    v1 = _800c6708.getSigned();
    a1 = 0x1L;

    if(v1 == 0) {
      //LAB_800e26c0
      v1 = _800c6738.getSigned();

      if(v1 == 0x1L) {
        //LAB_800e2700
        //LAB_800e2794
        loadDrgnBinFile(0, 6670, 0, getMethodAddress(SMap.class, "FUN_800e3d80", long.class, long.class, long.class), 0x10L, 0x4L);
      } else if(v1 == 0x2L) {
        //LAB_800e2728
        //LAB_800e2794
        loadDrgnBinFile(0, 6671, 0, getMethodAddress(SMap.class, "FUN_800e3d80", long.class, long.class, long.class), 0x10L, 0x4L);
        //LAB_800e26e8
      } else if(v1 == 0x3L) {
        //LAB_800e2750
        //LAB_800e2794
        loadDrgnBinFile(0, 6672, 0, getMethodAddress(SMap.class, "FUN_800e3d80", long.class, long.class, long.class), 0x10L, 0x4L);
      } else if(v1 == 0x4L) {
        //LAB_800e2778
        //LAB_800e2794
        loadDrgnBinFile(0, 6673, 0, getMethodAddress(SMap.class, "FUN_800e3d80", long.class, long.class, long.class), 0x10L, 0x4L);
      }

      //LAB_800e27a4
      _800c6708.addu(0x1L);
      return;
    }

    if(v1 == 0x1L) {
      //LAB_800e27b8
      if(mrg10Loaded_800c68e0.getSigned() == 0x1L && (_800c6738.get() & 0x80L) != 0) {
        _800c6708.addu(0x1L);
      }

      return;
    }

    if((int)v1 < 0x2L) {
      return;
    }

    //LAB_800e26a4
    if(v1 == 0x3L) {
      //LAB_800e3c60
      _800c6738.setu(0);
      _800c687e.setu(0);
      _800c687c.setu(0);
      mrg10Loaded_800c68e0.setu(0);
      _800c670c.setu(0);
      _800c670a.setu(0);
      _800c6708.setu(0);
      _800c686e.setu(a1);
      return;
    }

    if(v1 != 0x2) {
      return;
    }

    //LAB_800e27e8
    s0 = 0x800c_0000L;
    v1 = _800c670a.getSigned();
    a0 = _800c670a.get();

    //LAB_800e284c
    if(v1 == 0xe9L) {
      //LAB_800e376c
      removeFromLinkedList(mrg10Addr_800c6710.getPointer());
      mrg10Addr_800c6710.clear();
      _800c6708.addu(0x1L);
    } else if(v1 == 0x22L) {
      //LAB_800e30c0
      _800c670c.addu(0x1L);

      if(_800c670c.getSigned() == 0x3L) {
        _800c670e.setu(a1);
        _800c670a.addu(0x1L);
      }
    } else if((int)v1 >= 0x23L) {
      //LAB_800e2828
      if((int)v1 < 0xe9L) {
        if((int)v1 >= 0xc9L) {
          //LAB_800e311c
          if(v1 == 0xd4L) {
            TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(1) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e31b8
            DrawSync(0);

            sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(9) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e3248
            DrawSync(0);
            //LAB_800e3254
          } else if(v1 == 0xd8L) {
            TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(2) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e32f4
            DrawSync(0);

            sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(10) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e3384
            DrawSync(0);
            //LAB_800e3390
          } else if(v1 == 0xdcL) {
            TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(3) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e3430
            DrawSync(0);

            sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(11) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e34c0
            DrawSync(0);
            //LAB_800e34cc
          } else if(v1 == 0xe0L) {
            TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(4) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e356c
            DrawSync(0);

            sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(12) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e35fc
            DrawSync(0);
            //LAB_800e3608
          } else if(v1 == 0xe4L) {
            TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(5) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e36a8
            DrawSync(0);

            sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(13) + 0x4L));
            LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

            if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
              LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
            }

            //LAB_800e3738
            DrawSync(0);
          }

          //LAB_800e3744
          a0 = 0x800c_0000L;
          _800c6724.setu(0);
          MEMORY.ref(1, a0).offset(0x6728L).subu(0x4L);
          _800c670a.addu(0x1L);
        } else if(v1 == 0x23L) {
          //LAB_800e30f8
          _800c673c.subu(0x1L);
          if(_800c673c.get() == 0) {
            _800c670a.setu(0xc9L);
          }
        } else {
          //LAB_800e3790
          _800c670a.addu(0x1L);
        }
      }
    } else if((int)v1 >= 0x21L) {
      //LAB_800e3070
      _800c6724.setu(a1);
      _800c6720.setu(0);
      _800c671c.setu(0);
      _800c6718.setu(0);
      _800c6714.setu(0);
      v1 = 0x800c_0000L;
      MEMORY.ref(1, v1).offset(0x6728L).setu(0x80L);
      _800c670a.setu(a0 + 0x1L);
      _800c670c.setu(a1);
      _800c670e.setu(0);
    } else if((int)v1 > 0) {
      //LAB_800e29d4
      if(v1 == 0x4L) {
        TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(4) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2a6c
        DrawSync(0);

        sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(12) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2afc
        DrawSync(0);
        //LAB_800e2b08
      } else if(v1 == 0x8L) {
        TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(3) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2ba8
        DrawSync(0);

        sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(11) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2c38
        DrawSync(0);
        //LAB_800e2c44
      } else if(v1 == 0xcL) {
        TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(2) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2ce4
        DrawSync(0);

        sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(10) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2d74
        DrawSync(0);
        //LAB_800e2d80
      } else if(v1 == 0x10L) {
        TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(1) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2e20
        DrawSync(0);

        sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(9) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2eb0
        DrawSync(0);
        //LAB_800e2ebc
      } else if(v1 == 0x14L) {
        TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(0) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2f5c
        DrawSync(0);

        sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(8) + 0x4L));
        LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

        if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
          LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
        }

        //LAB_800e2fec
        DrawSync(0);
      }

      //LAB_800e2ff8
      v1 = 0x800c_0000L;
      a2 = 0x800c_0000L;
      v0 = MEMORY.ref(1, v1).offset(0x6728L).get();
      a1 = MEMORY.ref(2, a2).offset(0x670aL).get();
      v0 = v0 + 0x4L;
      MEMORY.ref(1, v1).offset(0x6728L).setu(v0);
      v1 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v1).offset(0x6714L).get();
      a0 = a1 & 0x1L;
      v0 = v0 - 0x1L;
      MEMORY.ref(4, v1).offset(0x6714L).setu(v0);
      if(a0 == 0) {
        v1 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v1).offset(0x6718L).get();

        v0 = v0 - 0x1L;
        MEMORY.ref(4, v1).offset(0x6718L).setu(v0);
      }

      //LAB_800e3038
      v1 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v1).offset(0x671cL).get();

      v0 = v0 - 0x2L;
      MEMORY.ref(4, v1).offset(0x671cL).setu(v0);
      if(a0 == 0) {
        v1 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v1).offset(0x6720L).get();

        v0 = v0 - 0x1L;
        MEMORY.ref(4, v1).offset(0x6720L).setu(v0);
      }

      //LAB_800e3064
      v0 = a1 + 0x1L;
      MEMORY.ref(2, a2).offset(0x670aL).setu(v0);
    } else if(v1 == 0) {
      //LAB_800e2860
      TimHeader sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(5) + 0x4L));
      LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

      if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
        LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
      }

      //LAB_800e28f0
      DrawSync(0);

      sp0x20 = parseTimHeader(MEMORY.ref(4, mrg10Addr_800c6710.deref().getFile(13) + 0x4L));
      LoadImage(sp0x20.imageRect, sp0x20.getImageAddress());

      if((sp0x20.flags.get() >>> 3 & 0x1L) != 0) {
        LoadImage(sp0x20.clutRect, sp0x20.getClutAddress());
      }

      //LAB_800e2980
      DrawSync(0);

      v0 = 0x800c_0000L;
      MEMORY.ref(1, v0).offset(0x6728L).setu(0);
      v0 = 0x800c_0000L;
      v1 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(0x6724L).setu(0);
      v0 = 0x20L;
      MEMORY.ref(4, v1).offset(0x6714L).setu(v0);
      v0 = 0x800c_0000L;
      a0 = 0x10L;
      v1 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(0x6718L).setu(a0);
      v0 = 0x40L;
      MEMORY.ref(4, v1).offset(0x671cL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x670aL).get();
      v1 = 0x800c_0000L;
      MEMORY.ref(4, v1).offset(0x6720L).setu(a0);
      v0 = v0 + 0x1L;
      MEMORY.ref(2, s0).offset(0x670aL).setu(v0);
    } else {
      v1 = 0x800c_0000L;

      //LAB_800e3790
      v0 = MEMORY.ref(2, v1).offset(0x670aL).get();
      v0 = v0 + 0x1L;
      MEMORY.ref(2, v1).offset(0x670aL).setu(v0);
    }

    //LAB_800e37a0
    v1 = 0x1f80_0000L;
    a0 = MEMORY.ref(4, v1).offset(0x3d8L).get();
    v0 = a0 + 0x28L;
    MEMORY.ref(4, v1).offset(0x3d8L).setu(v0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6724L).get();
    s0 = a0;
    if(v0 != 0) {
      v1 = 0x2c80_0000L;
    } else {
      v1 = 0x2e80_0000L;
    }

    //LAB_800e37cc
    v1 = v1 | 0x8080L;
    v0 = 0x9L;
    MEMORY.ref(1, s0).offset(0x3L).setu(v0);
    MEMORY.ref(4, s0).offset(0x4L).setu(v1);
    v0 = 0x40L;
    v1 = 0x5bL;
    MEMORY.ref(1, s0).offset(0xdL).setu(v0);
    MEMORY.ref(1, s0).offset(0x15L).setu(v0);
    v0 = 0x63L;
    MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    MEMORY.ref(1, s0).offset(0x25L).setu(v0);
    v0 = 0x800c_0000L;
    MEMORY.ref(1, s0).offset(0x14L).setu(v1);
    MEMORY.ref(1, s0).offset(0x24L).setu(v1);
    v1 = 0x800c_0000L;
    a1 = 0x800c_0000L;
    MEMORY.ref(1, s0).offset(0xcL).setu(0);
    MEMORY.ref(1, s0).offset(0x1cL).setu(0);
    a2 = MEMORY.ref(2, v0).offset(0x6714L).get();
    v0 = 0x800c_0000L;
    a0 = 0x800c_0000L;
    a3 = MEMORY.ref(2, v1).offset(0x687cL).get();
    t1 = MEMORY.ref(2, v0).offset(0x6718L).get();
    a1 = MEMORY.ref(2, a1).offset(0x687eL).get();
    v0 = MEMORY.ref(1, a0).offset(0x6728L).get();
    t0 = a2 - 0x3aL;
    t0 = a3 + t0;
    MEMORY.ref(1, s0).offset(0x4L).setu(v0);
    v0 = t1 - 0x42L;
    v0 = a1 + v0;
    a2 = a2 - 0x22L;
    a3 = a3 - a2;
    v1 = MEMORY.ref(1, a0).offset(0x6728L).get();
    t1 = t1 + 0x1eL;
    MEMORY.ref(2, s0).offset(0xaL).setu(v0);
    MEMORY.ref(2, s0).offset(0x12L).setu(v0);
    MEMORY.ref(1, s0).offset(0x5L).setu(v1);
    v1 = MEMORY.ref(1, a0).offset(0x6728L).get();
    v0 = 0x800c_0000L;
    MEMORY.ref(1, s0).offset(0x6L).setu(v1);
    v0 = MEMORY.ref(4, v0).offset(0x6724L).get();
    a1 = a1 - t1;
    MEMORY.ref(2, s0).offset(0x8L).setu(t0);
    MEMORY.ref(2, s0).offset(0x10L).setu(a3);
    MEMORY.ref(2, s0).offset(0x18L).setu(t0);
    MEMORY.ref(2, s0).offset(0x1aL).setu(a1);
    MEMORY.ref(2, s0).offset(0x20L).setu(a3);
    MEMORY.ref(2, s0).offset(0x22L).setu(a1);
    if(v0 == 0) {
      v0 = 0x38L;
    } else {
      //LAB_800e3898
      v0 = 0x18L;
    }

    //LAB_800e389c
    MEMORY.ref(2, s0).offset(0x16L).setu(v0);
    v0 = GetClut(512, 510);
    v1 = 0x1f80_0000L;
    a0 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    a1 = s0;
    MEMORY.ref(2, a1).offset(0xeL).setu(v0);
    a0 = a0 + 0x70L;
    insertElementIntoLinkedList(a0, a1);
    v1 = 0x1f80_0000L;
    a0 = MEMORY.ref(4, v1).offset(0x3d8L).get();

    v0 = a0 + 0x28L;
    MEMORY.ref(4, v1).offset(0x3d8L).setu(v0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6724L).get();
    s0 = a0;
    if(v0 != 0) {
      v1 = 0x2c80_0000L;
    } else {
      v1 = 0x2e80_0000L;
    }

    //LAB_800e38f0
    v1 = v1 | 0x8080L;
    v0 = 0x9L;
    MEMORY.ref(1, s0).offset(0x3L).setu(v0);
    MEMORY.ref(4, s0).offset(0x4L).setu(v1);
    v1 = 0xffL;
    v0 = 0x3cL;
    MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    MEMORY.ref(1, s0).offset(0x25L).setu(v0);
    v0 = 0x800c_0000L;
    MEMORY.ref(1, s0).offset(0x14L).setu(v1);
    MEMORY.ref(1, s0).offset(0x24L).setu(v1);
    v1 = 0x800c_0000L;
    a1 = 0x800c_0000L;
    MEMORY.ref(1, s0).offset(0xcL).setu(0);
    MEMORY.ref(1, s0).offset(0xdL).setu(0);
    MEMORY.ref(1, s0).offset(0x15L).setu(0);
    MEMORY.ref(1, s0).offset(0x1cL).setu(0);
    a2 = MEMORY.ref(2, v0).offset(0x671cL).get();
    v0 = 0x800c_0000L;
    a0 = 0x800c_0000L;
    a3 = MEMORY.ref(2, v1).offset(0x687cL).get();
    t1 = MEMORY.ref(2, v0).offset(0x6720L).get();
    a1 = MEMORY.ref(2, a1).offset(0x687eL).get();
    v0 = MEMORY.ref(1, a0).offset(0x6728L).get();
    t0 = a2 + 0x8cL;
    t0 = a3 - t0;
    MEMORY.ref(1, s0).offset(0x4L).setu(v0);
    v0 = t1 + 0x10L;
    v0 = a1 - v0;
    a2 = a2 + 0x74L;
    a3 = a3 + a2;
    v1 = MEMORY.ref(1, a0).offset(0x6728L).get();
    t1 = t1 + 0x2dL;
    MEMORY.ref(2, s0).offset(0xaL).setu(v0);
    MEMORY.ref(2, s0).offset(0x12L).setu(v0);
    MEMORY.ref(1, s0).offset(0x5L).setu(v1);
    v1 = MEMORY.ref(1, a0).offset(0x6728L).get();
    v0 = 0x800c_0000L;
    MEMORY.ref(1, s0).offset(0x6L).setu(v1);
    v0 = MEMORY.ref(4, v0).offset(0x6724L).get();
    a1 = a1 + t1;
    MEMORY.ref(2, s0).offset(0x8L).setu(t0);
    MEMORY.ref(2, s0).offset(0x10L).setu(a3);
    MEMORY.ref(2, s0).offset(0x18L).setu(t0);
    MEMORY.ref(2, s0).offset(0x1aL).setu(a1);
    MEMORY.ref(2, s0).offset(0x20L).setu(a3);
    MEMORY.ref(2, s0).offset(0x22L).setu(a1);
    if(v0 == 0) {
      v0 = 0x38L;
    } else {
      //LAB_800e39b8
      v0 = 0x18L;
    }

    //LAB_800e39bc
    MEMORY.ref(2, s0).offset(0x16L).setu(v0);
    v0 = GetClut(512, 508);
    s6 = 0x1f80_0000L;
    a0 = MEMORY.ref(4, s6).offset(0x3d0L).get();
    a1 = s0;
    MEMORY.ref(2, a1).offset(0xeL).setu(v0);
    a0 = a0 + 0x70L;
    insertElementIntoLinkedList(a0, a1);
    s4 = 0x800c_0000L;
    v0 = MEMORY.ref(2, s4).offset(0x670cL).getSigned();

    if(v0 != 0) {
      s5 = 0x2e80_0000L;
      s2 = 0x1f80_0000L;
      s5 = s5 | 0x8080L;
      v1 = 0x5bL;
      fp = 0x800c_0000L;
      s1 = 0x800c_0000L;
      s0 = MEMORY.ref(4, s2).offset(0x3d8L).get();
      s3 = 0x800c_0000L;
      v0 = s0 + 0x28L;
      MEMORY.ref(4, s2).offset(0x3d8L).setu(v0);
      t3 = 0x9L;
      v0 = 0x40L;
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      v0 = 0x63L;
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      v0 = 0x800c_0000L;
      MEMORY.ref(1, s0).offset(0x3L).setu(t3);
      t3 = 0x800c_0000L;
      MEMORY.ref(4, s0).offset(0x4L).setu(s5);
      MEMORY.ref(1, s0).offset(0xcL).setu(0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v1);
      MEMORY.ref(1, s0).offset(0x1cL).setu(0);
      MEMORY.ref(1, s0).offset(0x24L).setu(v1);
      a3 = MEMORY.ref(2, v0).offset(0x6714L).get();
      v0 = 0x800c_0000L;
      t1 = MEMORY.ref(2, t3).offset(0x687cL).get();
      t3 = 0x800c_0000L;
      t0 = MEMORY.ref(2, s4).offset(0x670cL).get();
      a2 = MEMORY.ref(2, v0).offset(0x6718L).get();
      t2 = MEMORY.ref(2, t3).offset(0x687eL).get();
      a1 = MEMORY.ref(2, fp).offset(0x670eL).get();
      v0 = MEMORY.ref(1, s1).offset(0x6728L).get();
      a0 = a3 - 0x3aL;
      a0 = t1 + a0;
      a0 = t0 + a0;
      MEMORY.ref(1, s0).offset(0x4L).setu(v0);
      v0 = a2 - 0x42L;
      v0 = t2 + v0;
      v0 = a1 + v0;
      a3 = a3 - 0x22L;
      t1 = t1 - a3;
      t0 = t0 + t1;
      v1 = MEMORY.ref(1, s1).offset(0x6728L).get();
      a2 = a2 + 0x1eL;
      MEMORY.ref(1, s0).offset(0x5L).setu(v1);
      v1 = MEMORY.ref(1, s1).offset(0x6728L).get();
      t2 = t2 - a2;
      MEMORY.ref(2, s0).offset(0xaL).setu(v0);
      MEMORY.ref(2, s0).offset(0x12L).setu(v0);
      MEMORY.ref(1, s0).offset(0x6L).setu(v1);
      v0 = MEMORY.ref(2, s3).offset(0x6738L).get();
      a1 = a1 + t2;
      MEMORY.ref(2, s0).offset(0x8L).setu(a0);
      MEMORY.ref(2, s0).offset(0x10L).setu(t0);
      MEMORY.ref(2, s0).offset(0x18L).setu(a0);
      MEMORY.ref(2, s0).offset(0x1aL).setu(a1);
      MEMORY.ref(2, s0).offset(0x20L).setu(t0);
      v0 = v0 & 0xfL;
      v0 = v0 - 0x2L;
      MEMORY.ref(2, s0).offset(0x22L).setu(a1);
      if(v0 < 0x3L) {
        v0 = 0x58L;
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
      }

      //LAB_800e3afc
      v0 = MEMORY.ref(2, s3).offset(0x6738L).get();
      s7 = 0x1L;
      v0 = v0 & 0xfL;
      if(v0 == s7) {
        v0 = 0x38L;
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
      }

      //LAB_800e3b14
      v0 = GetClut(512, 511);
      a0 = MEMORY.ref(4, s6).offset(0x3d0L).get();
      a1 = s0;
      MEMORY.ref(2, a1).offset(0xeL).setu(v0);
      a0 = a0 + 0x70L;
      insertElementIntoLinkedList(a0, a1);
      s0 = MEMORY.ref(4, s2).offset(0x3d8L).get();
      v1 = 0xffL;
      v0 = s0 + 0x28L;
      MEMORY.ref(4, s2).offset(0x3d8L).setu(v0);
      t3 = 0x9L;
      v0 = 0x3cL;
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      v0 = 0x800c_0000L;
      MEMORY.ref(1, s0).offset(0x3L).setu(t3);
      t3 = 0x800c_0000L;
      MEMORY.ref(4, s0).offset(0x4L).setu(s5);
      MEMORY.ref(1, s0).offset(0xcL).setu(0);
      MEMORY.ref(1, s0).offset(0xdL).setu(0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v1);
      MEMORY.ref(1, s0).offset(0x15L).setu(0);
      MEMORY.ref(1, s0).offset(0x1cL).setu(0);
      MEMORY.ref(1, s0).offset(0x24L).setu(v1);
      a1 = MEMORY.ref(2, v0).offset(0x671cL).get();
      v0 = 0x800c_0000L;
      a3 = MEMORY.ref(2, t3).offset(0x687cL).get();
      t3 = 0x800c_0000L;
      t1 = MEMORY.ref(2, s4).offset(0x670cL).get();
      t0 = MEMORY.ref(2, v0).offset(0x6720L).get();
      t2 = MEMORY.ref(2, t3).offset(0x687eL).get();
      a2 = MEMORY.ref(2, fp).offset(0x670eL).get();
      v0 = MEMORY.ref(1, s1).offset(0x6728L).get();
      a0 = a1 + 0x8cL;
      a0 = a3 - a0;
      a0 = t1 + a0;
      MEMORY.ref(1, s0).offset(0x4L).setu(v0);
      v0 = t0 + 0x10L;
      v0 = t2 - v0;
      v0 = a2 + v0;
      a1 = a1 + 0x74L;
      a3 = a3 + a1;
      t1 = t1 + a3;
      v1 = MEMORY.ref(1, s1).offset(0x6728L).get();
      t0 = t0 + 0x2dL;
      MEMORY.ref(1, s0).offset(0x5L).setu(v1);
      v1 = MEMORY.ref(1, s1).offset(0x6728L).get();
      t2 = t2 + t0;
      MEMORY.ref(2, s0).offset(0xaL).setu(v0);
      MEMORY.ref(2, s0).offset(0x12L).setu(v0);
      MEMORY.ref(1, s0).offset(0x6L).setu(v1);
      v0 = MEMORY.ref(2, s3).offset(0x6738L).get();
      a2 = a2 + t2;
      MEMORY.ref(2, s0).offset(0x8L).setu(a0);
      MEMORY.ref(2, s0).offset(0x10L).setu(t1);
      MEMORY.ref(2, s0).offset(0x18L).setu(a0);
      MEMORY.ref(2, s0).offset(0x1aL).setu(a2);
      MEMORY.ref(2, s0).offset(0x20L).setu(t1);
      v0 = v0 & 0xfL;
      v0 = v0 - 0x2L;
      MEMORY.ref(2, s0).offset(0x22L).setu(a2);
      if(v0 < 0x3L) {
        v0 = 0x58L;
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
      }

      //LAB_800e3c20
      v0 = MEMORY.ref(2, s3).offset(0x6738L).get();
      v0 = v0 & 0xfL;
      if(v0 == s7) {
        v0 = 0x38L;
        MEMORY.ref(2, s0).offset(0x16L).setu(v0);
      }

      //LAB_800e3c3c
      v0 = GetClut(512, 509);
      a0 = MEMORY.ref(4, s6).offset(0x3d0L).get();
      a1 = s0;
      MEMORY.ref(2, a1).offset(0xeL).setu(v0);
      a0 = a0 + 0x70L;
      insertElementIntoLinkedList(a0, a1);
    }

    //LAB_800e3c98
  }

  @Method(0x800e3cc8L)
  public static void loadTimImage(final long address) {
    final TimHeader header = parseTimHeader(MEMORY.ref(4, address).offset(0x4L));
    LoadImage(header.imageRect, header.imageAddress.get());

    if(header.hasClut()) {
      LoadImage(header.clutRect, header.clutAddress.get());
    }

    DrawSync(0);
  }

  @Method(0x800e3d68L)
  public static void clearJoypadInput() {
    _800bee90.setu(0);
    _800bee94.setu(0);
    _800bee98.setu(0);
  }

  /** Callback for two different MRG files (sectors 136752 and 136849) */
  @Method(0x800e3d80L)
  public static void FUN_800e3d80(final long fileAddress, final long fileSize, final long a2) {
    switch((int)a2) {
      case 0x0 -> {
        mrg0Loaded_800c6874.setu(0x1);
        mrg0Addr_800c6878.set(MEMORY.ref(4, fileAddress, MrgFile::new));
      }

      case 0x1 -> {
        mrg1Loaded_800c68d0.setu(0x1);
        mrg1Addr_800c68d8.set(MEMORY.ref(4, fileAddress, MrgFile::new));
      }

      case 0x10 -> {
        mrg10Loaded_800c68e0.setu(0x1);
        mrg10Addr_800c6710.set(MEMORY.ref(4, fileAddress, MrgFile::new));
      }
    }
  }

  @Method(0x800e3df4L)
  public static void scriptDestructor(final int index, final ScriptState<BigStruct> scriptState, final BigStruct bigStruct) {
    //LAB_800e3e24
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      if(scriptStateIndices_800c6880.get(i).get() == index) {
        scriptStateIndices_800c6880.get(i).set(-1);
      }

      //LAB_800e3e38
    }

    //LAB_800e3e48
    FUN_80020fe0(bigStruct);
  }

  @Method(0x800e3e60L)
  public static long FUN_800e3e60(final int index, final ScriptState<BigStruct> scriptState, final BigStruct bigStruct) {
    bigStruct.us_170.set(0);
    return 0x1L;
  }

  @Method(0x800e3e74L)
  public static long FUN_800e3e74(final int index, final ScriptState<BigStruct> scriptState, final BigStruct bigStruct) {
    bigStruct.coord2_14.coord.transfer.y.add(bigStruct.s_134.get());

    int x = bigStruct.vec_148.getX();
    int z = bigStruct.vec_148.getZ();

    //LAB_800e3ec0
    bigStruct.vec_160.x.add(bigStruct.vec_154.getX());
    bigStruct.vec_160.z.add(bigStruct.vec_154.getZ());

    if((bigStruct.vec_160.getX() & 0x1_0000) != 0) {
      bigStruct.vec_160.x.and(0xffff);

      if(bigStruct.vec_148.getX() >= 0) {
        x++;
      } else {
        //LAB_800e3f08
        x--;
      }
    }

    //LAB_800e3f0c
    if((bigStruct.vec_160.getZ() & 0x1_0000) != 0) {
      bigStruct.vec_160.z.and(0xffff);

      if(bigStruct.vec_148.getZ() >= 0) {
        z++;
      } else {
        //LAB_800e3f38
        z--;
      }
    }

    //LAB_800e3f3c
    bigStruct.coord2_14.coord.transfer.x.add(x);
    bigStruct.coord2_14.coord.transfer.z.add(z);
    bigStruct.s_134.add((short)bigStruct.ui_18c.get());
    bigStruct.ui_144.decr();
    if(bigStruct.ui_144.get() != 0) {
      return 0;
    }

    //LAB_800e3f7c
    bigStruct.us_170.set(0);
    bigStruct.s_134.set((short)0);
    bigStruct.coord2_14.coord.transfer.set(bigStruct.vec_138);
    bigStruct.s_172.set(bigStruct.s_174.get());
    return 1;
  }

  @Method(0x800e3facL)
  public static void FUN_800e3fac() {
    if(_800bd7b8.get() == 0) {
      loadTimImage(mrg0Addr_800c6878.deref().getFile((int)_800c69f0.get()));
      loadTimImage(mrg0Addr_800c6878.deref().getFile((int)_800c69f4.get()));
    }

    //LAB_800e4008
  }

  @Method(0x800e4018L)
  public static void FUN_800e4018() {
    if(gameState_800babc8._4e3.get() != 0) {
      if(_800f64ac.get() == 0) {
        _800f64ac.setu(0x1L);
      }
    } else if(_800f64ac.get() == 0x1L) {
      _800f64ac.setu(0);
      _800c69ec.setu(0);
    }
  }

  @Method(0x800e4378L)
  public static void FUN_800e4378(final BigStruct t2, final long a1) {
    t2.i_19c.set(-1);

    final int t4 = t2.coord2_14.coord.transfer.getY() - t2.i_1a4.get();
    final int a0 = t2.coord2_14.coord.transfer.getY() + t2.i_1a4.get();

    //LAB_800e43b8
    //LAB_800e43ec
    //LAB_800e43f0
    //LAB_800e4414
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      final BigStruct a2 = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.derefAs(BigStruct.class);

      if(a2.getAddress() != t2.getAddress() && (a2.ui_190.get() & a1) != 0) {
        final int dx = a2.coord2_14.coord.transfer.getX() - t2.coord2_14.coord.transfer.getX();
        final int t1 = dx * dx;
        final int dz = a2.coord2_14.coord.transfer.getZ() - t2.coord2_14.coord.transfer.getZ();
        final int t0 = dz * dz;
        final int v0 = t2.i_1a0.get() + a2.i_1a8.get();
        final int t8 = v0 * v0;
        final int a3 = a2.coord2_14.coord.transfer.getY() - a2.i_1a4.get();
        final int v1 = a2.coord2_14.coord.transfer.getY() + a2.i_1a4.get();

        //LAB_800e44d0
        //LAB_800e44e0
        if(t8 >= t1 + t0 && (a0 >= a3 && t4 <= a3 || a0 >= v1 && t4 <= v1) && t2.i_19c.get() == -1) {
          t2.i_19c.set(i);
        }
      }
    }
  }

  @Method(0x800e450cL)
  public static void FUN_800e450c(final BigStruct s0, long a1) {
    long v0;
    long v1;
    final long a0;
    long a3;
    long t0;
    final long t2;
    final long t4;
    long t6;
    long t7;
    final long s1;
    final long s2;

    s2 = a1;
    s0.i_1a8.set(-1);
    v0 = rsin(s0.coord2Param_64.rotate.getY());

    v1 = -s0.i_1b4.get();
    t6 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
    s1 = (int)t6 >> 12;
    v0 = rcos(s0.coord2Param_64.rotate.getY());

    v1 = -s0.i_1b4.get();
    t6 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
    t2 = s0.coord2_14.coord.transfer.getY() - s0.i_1b0.get();
    a0 = s0.coord2_14.coord.transfer.getY() + s0.i_1b0.get();
    t4 = (int)t6 >> 12;

    //LAB_800e45a4
    //TODO this isn't doing anything...?
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      if(scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.getPointer() == s0.getAddress()) {
        break;
      }
    }

    //LAB_800e45d8
    //LAB_800e45dc
    //LAB_800e4600
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      final BigStruct struct = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.derefAs(BigStruct.class);

      if(struct.getAddress() != s0.getAddress()) {
        if((struct.ui_190.get() & s2) != 0) {
          v0 = struct.coord2_14.coord.transfer.getX() - (s0.coord2_14.coord.transfer.getX() + s1);
          t0 = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
          v0 = struct.coord2_14.coord.transfer.getZ() - (s0.coord2_14.coord.transfer.getZ() + t4);
          a3 = (long)(int)v0 * (int)v0 & 0xffff_ffffL;
          v0 = s0.i_1ac.get() + struct.i_1ac.get();
          t7 = (long)(int)v0 * (int)v0 & 0xffff_ffffL;

          v1 = struct.coord2_14.coord.transfer.getY() + struct.i_1b0.get();
          a1 = struct.coord2_14.coord.transfer.getY() - struct.i_1b0.get();
          v0 = t0 + a3;

          if((int)t7 >= (int)v0) {
            //LAB_800e46bc
            if((int)a1 >= (int)t2 && (int)a1 <= (int)a0 || (int)v1 >= (int)t2 && (int)v1 <= (int)a0) {
              //LAB_800e46cc
              if(s0.i_1a8.get() == -0x1L) {
                s0.i_1a8.set(i);
              }
            }
          }
        }
      }

      //LAB_800e46e0
    }

    //LAB_800e46f0
  }

  @Method(0x800e4708L)
  public static void FUN_800e4708() {
    FUN_800f047c();
    FUN_800f3abc();
    FUN_800f4354();
    FUN_800214bc(bigStruct_800c6748);
    callbackArr_800f5ad4.get((int)callbackIndex_800c6968.get()).deref().run();
  }

  @Method(0x800e4774L)
  public static void FUN_800e4774(final BigStruct a0, final long a1) {
    final MATRIX sp0x20 = new MATRIX();
    final MATRIX sp0x40 = new MATRIX();
    GsGetLws(a0.coord2_14, sp0x40, sp0x20);
    CPU.CTC2(sp0x20.getPacked(0), 0);
    CPU.CTC2(sp0x20.getPacked(2), 1);
    CPU.CTC2(sp0x20.getPacked(4), 2);
    CPU.CTC2(sp0x20.getPacked(6), 3);
    CPU.CTC2(sp0x20.getPacked(8), 4);
    CPU.CTC2(sp0x20.transfer.getX(), 5);
    CPU.CTC2(sp0x20.transfer.getY(), 6);
    CPU.CTC2(sp0x20.transfer.getZ(), 7);
    final SVECTOR sp0x10 = new SVECTOR().setY((short)(a1 - 64));
    CPU.MTC2(sp0x10.getXY(), 0);
    CPU.MTC2(sp0x10.getZ(),  1);
    CPU.COP2(0x180001L);
    final DVECTOR sp0x18 = new DVECTOR().setXY(CPU.MFC2(14));
    final long sp60 = CPU.MFC2(8);
    final long sp64 = CPU.CFC2(31);
    final long sp68 = (int)CPU.MFC2(19) >> 2;
    final long packet = linkedListAddress_1f8003d8.get();
    MEMORY.ref(2, packet).offset(0x08L).setu(_800f64b0.get() + sp0x18.getX());
    MEMORY.ref(2, packet).offset(0x0aL).setu(_800f64b0.offset(0x4L).get() + sp0x18.getY());
    MEMORY.ref(2, packet).offset(0x10L).setu(_800f64b0.offset(0x2L).get() + sp0x18.getX());
    MEMORY.ref(2, packet).offset(0x12L).setu(_800f64b0.offset(0x4L).get() + sp0x18.getY());
    MEMORY.ref(2, packet).offset(0x18L).setu(_800f64b0.get() + sp0x18.getX());
    MEMORY.ref(2, packet).offset(0x1aL).setu(_800f64b0.offset(0x6L).get() + sp0x18.getY());
    MEMORY.ref(2, packet).offset(0x20L).setu(_800f64b0.offset(0x2L).get() + sp0x18.getX());
    MEMORY.ref(2, packet).offset(0x22L).setu(_800f64b0.offset(0x6L).get() + sp0x18.getY());
    MEMORY.ref(1, packet).offset(0x0cL).setu(_800f64b0.offset(1, 0x8L).get());
    MEMORY.ref(1, packet).offset(0x0dL).setu(_800f64b0.offset(1, 0xcL).get());
    MEMORY.ref(1, packet).offset(0x14L).setu(_800f64b0.offset(1, 0xaL).get());
    MEMORY.ref(1, packet).offset(0x15L).setu(_800f64b0.offset(1, 0xcL).get());
    MEMORY.ref(1, packet).offset(0x1cL).setu(_800f64b0.offset(1, 0x8L).get());
    MEMORY.ref(1, packet).offset(0x1dL).setu(_800f64b0.offset(1, 0xeL).get());
    MEMORY.ref(1, packet).offset(0x24L).setu(_800f64b0.offset(1, 0xaL).get());
    MEMORY.ref(1, packet).offset(0x25L).setu(_800f64b0.offset(1, 0xeL).get());
    MEMORY.ref(2, packet).offset(0x0eL).setu(GetClut(976, 464));
    MEMORY.ref(1, packet).offset(0x03L).setu(0x9L);
    MEMORY.ref(4, packet).offset(0x04L).setu(0x2e80_8080L);
    MEMORY.ref(2, packet).offset(0x16L).setu(0x1fL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x94L, packet);
    linkedListAddress_1f8003d8.addu(0x28L);
  }

  @Method(0x800e4994L)
  public static void FUN_800e4994() {
    _800c686c.setu(0x1L);
  }

  @Method(0x800e49a4L)
  public static long FUN_800e49a4() {
    final long rand = rand();

    if(rand < 0x2cccL) {
      return 0;
    }

    if(rand < 0x5999L) {
      return 0x1L;
    }

    if(rand < 0x7333L) {
      //LAB_800e49e0
      return 0x2L;
    }

    return 0x3L;
  }

  /** TODO contains the encounter rate bug */
  @Method(0x800e49f0L)
  public static boolean hasPlayerMoved(final MATRIX mat) {
    //LAB_800e4a44
    final boolean moved = prevPlayerPos_800c6ab0.getX() != mat.transfer.getX() || prevPlayerPos_800c6ab0.getY() != mat.transfer.getY() || prevPlayerPos_800c6ab0.getZ() != mat.transfer.getZ();

    //LAB_800e4a4c
    final long dist = (prevPlayerPos_800c6ab0.getX() - mat.transfer.getX() ^ 0x2L) + (prevPlayerPos_800c6ab0.getZ() - mat.transfer.getZ() ^ 0x2L);

    if((int)dist < 0x9L) {
      //LAB_800e4a98
      encounterMultiplier_800c6abc.setu(0x1L);
    } else {
      encounterMultiplier_800c6abc.setu(0x4L);
    }

    //LAB_800e4aa0
    memcpy(prevPlayerPos_800c6ab0.getAddress(), mat.transfer.getAddress(), 0xc);
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

    if(fileCount_8004ddc8.get() != 0) {
      return 0;
    }

    if(gameState_800babc8._4e3.get() != 0) {
      return 0;
    }

    _800c6ae4.addu(0x1L);

    if(_800c6ae4.getSigned() < 0) {
      return 0;
    }

    if(index_80052c38.get() < 0x40L && arr_800cb460.get(index_80052c38.get()).get() != 0) {
      return 0;
    }

    //LAB_800e4bc0
    if(!isScriptLoaded(0)) {
      return 0;
    }

    if(joypadInput_8007a39c.get() == 0) {
      return 0;
    }

    if(!hasPlayerMoved(scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14.coord)) {
      return 0;
    }

    encounterAccumulator_800c6ae8.addu(encounterData_800f64c4.get(submapCut_80052c30.get()).rate_02.get() * encounterMultiplier_800c6abc.get());

    if(encounterAccumulator_800c6ae8.get() > 0x1400L) {
      // Start combat
      encounterId_800bb0f8.set((int)_800f74c4.offset(2, encounterData_800f64c4.get(submapCut_80052c30.get()).scene_00.get() * 0x8L).offset(FUN_800e49a4() * 0x2L).getSigned());
      submapStage_800bb0f4.set(encounterData_800f64c4.get(submapCut_80052c30.get()).stage_03.get());
      return 0x1L;
    }

    //LAB_800e4ce4
    //LAB_800e4ce8
    return 0;
  }

  @Method(0x800e4d00L)
  public static void FUN_800e4d00(final long submapCut, final int index) {
    if(FUN_800e5264(matrix_800c6ac0, submapCut) == 0) {
      //LAB_800e4d34
      final SVECTOR avg = new SVECTOR();
      get3dAverageOfSomething(index, avg);
      matrix_800c6ac0.transfer.setX(avg.getX());
      matrix_800c6ac0.transfer.setY(avg.getY());
      matrix_800c6ac0.transfer.setZ(avg.getZ());
      _800f7e24.setu(0x2L);
    } else {
      _800f7e24.setu(0x1L);
    }

    //LAB_800e4d74
  }

  @Method(0x800e4d88L)
  public static void FUN_800e4d88(final int index, final BigStruct struct) {
    if(_800f7e24.get() != 0) {
      if(_800f7e24.get() == 0x1L) {
        struct.coord2_14.coord.set(matrix_800c6ac0);
      } else {
        //LAB_800e4e04
        struct.coord2_14.coord.transfer.set(matrix_800c6ac0.transfer);
      }

      //LAB_800e4e18
      _800f7e24.setu(0);
    } else {
      //LAB_800e4e20
      final SVECTOR sp10 = new SVECTOR();
      get3dAverageOfSomething(index, sp10);
      struct.coord2_14.coord.transfer.setX(sp10.getX());
      struct.coord2_14.coord.transfer.setY(sp10.getY());
      struct.coord2_14.coord.transfer.setZ(sp10.getZ());
    }

    //LAB_800e4e4c
  }

  @Method(0x800e4e5cL)
  public static void FUN_800e4e5c() {
    //LAB_800e4ecc
    MoveImage(doubleBufferFrame_800bb108.get() != 0 ? _800d69fc : _800d6a04, 640, 0);
    _80052c48.setu(0x1L);
    DrawSync(0);
  }

  @Method(0x800e4f74L)
  public static long FUN_800e4f74(final UnknownStruct2 a0, final long a1) {
    if(a0 == null) {
      return 1;
    }

    a0._00.set(0);
    return 0;
  }

  @Method(0x800e4f8cL)
  public static void FUN_800e4f8c() {
    UnknownStruct a0 = _800c6aec.derefNullable();

    //LAB_800e4fac
    while(a0 != null) {
      _800c6aec.setNullable(a0.parent_00.derefNullable());
      removeFromLinkedList(a0.getAddress());
      a0 = _800c6aec.derefNullable();
    }

    //LAB_800e4fd4
    _800f7e28.set(_800c6aec);
  }

  @Method(0x800e4fecL)
  public static void noop_800e4fec() {
    // empty
  }

  @Method(0x800e4ff4L)
  public static void FUN_800e4ff4() {
    UnknownStruct s0 = _800c6aec.deref();
    Pointer<UnknownStruct> s1 = _800c6aec;

    //LAB_800e5018
    while(s0 != null) {
      if(s0.callback_04.deref().run(s0.inner_08.deref(), s0._0c.get()) != 0) {
        s1.set(s0.parent_00.deref());
        removeFromLinkedList(s0.getAddress());
      } else {
        //LAB_800e5054
        s1 = s0.parent_00;
      }

      //LAB_800e5058
      s0 = s1.derefNullable();
    }

    //LAB_800e5068
    _800f7e28.setNullable(s1);
  }

  @Method(0x800e5084L)
  public static long FUN_800e5084(final BiFunctionRef<UnknownStruct2, Long, Long> callback, final UnknownStruct2 a1, final long a2) {
    final UnknownStruct v0 = MEMORY.ref(4, addToLinkedListTail(0x10L), UnknownStruct::new);

    if(v0 == null) {
      //LAB_800e50e8
      return 0;
    }

    v0.parent_00.clear();
    v0.callback_04.set(callback);
    v0.inner_08.set(a1);
    v0._0c.set(a2);

    _800f7e28.deref().set(v0);
    _800f7e28.set(v0.parent_00); //TODO I dunno if this is right...

    //LAB_800e50ec
    return 0x1L;
  }

  @Method(0x800e5104L)
  public static void FUN_800e5104(final int index, final MediumStruct a1) {
    executeSceneGraphicsLoadingStage(index);

    a1.callback_48.deref().run(a1);

    GsOTPtr_800f64c0.set(orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()));
    _800c6ae0.addu(0x1L);

    if(gameState_800babc8._4e3.get() != 0) {
      _800c6ae4.setu(-0x1eL);
    }

    //LAB_800e5184
    FUN_800e4ff4();
  }

  @Method(0x800e519cL)
  public static void FUN_800e519c() {
    if(GsOTPtr_800f64c0.isNull()) {
      return;
    }

    //LAB_800e51e8
    final MATRIX[] matrices = new MATRIX[(int)scriptCount_800c6730.get()];
    for(int i = 0; i < scriptCount_800c6730.get(); i++) {
      if(!isScriptLoaded(i)) {
        return;
      }

      matrices[i] = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(i).get()).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14.coord;
    }

    //LAB_800e5234
    FUN_800e7954(GsOTPtr_800f64c0.deref(), matrices, scriptCount_800c6730.get());

    //LAB_800e5248
  }

  @Method(0x800e5264L)
  public static long FUN_800e5264(final MATRIX mat, final long submapCut) {
    if(_80052c3c.get() != submapCut) {
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
  public static void loadBackground(final long address, final long fileSize, final long param) {
    backgroundLoaded_800cab10.offset(param * 0x4L).setu(0x1L);

    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    //LAB_800e5374
    for(int i = 3; i < mrg.count.get(); i++) {
      final TimHeader timHeader = parseTimHeader(MEMORY.ref(4, mrg.getFile(i)).offset(0x4L));
      LoadImage(timHeader.imageRect, timHeader.imageAddress.get());

      if(timHeader.hasClut()) {
        LoadImage(timHeader.clutRect, timHeader.clutAddress.get());
      }
    }

    //LAB_800e5430
    loadEnvironment(mrg.getFile(0, EnvironmentFile::new));
    FUN_800e8cd0(mrg.getFile(2, TmdWithId::new), (int)mrg.entries.get(2).size.get(), mrg.getFile(1, UnboundedArrayRef.of(0xc, SomethingStruct2::new)), mrg.entries.get(1).size.get());

    DrawSync(0);

    removeFromLinkedList(mrg.getAddress());

    _80052c44.setu(0x2L);
    _80052c48.setu(0);
    _800cab20.setu(0x2L);
  }

  @Method(0x800e54a4L)
  public static void newrootCallback_800e54a4(final long address, final long fileSize, final long param) {
    newrootPtr_800cab04.set(newroot_800c6af0);
    memcpy(newroot_800c6af0.getAddress(), address, (int)fileSize);
    removeFromLinkedList(address);
    FUN_800e6640(newrootPtr_800cab04.deref());
    newrootLoaded_800cab1c.setu(0x1L);
  }

  @Method(0x800e5518L)
  public static boolean isScriptLoaded(final int index) {
    return scriptStateIndices_800c6880.get(index).get() >= 0 && scriptStateIndices_800c6880.get(index).get() < 0x40;
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
    clearJoypadInput();
    _800bd7b4.setu(0);
    if(_800cab28.get() == 0) {
      if(scriptEffect_800bb140._24.get() == 0) {
        scriptStartEffect(1, 10);
        _800cab28.addu(0x1L);
      }
    } else {
      _800cab28.addu(0x1L);
    }

    final int cut = submapCut_80052c30.get();
    _800f7e4c.setu(0x1L);

    if(newCut > 0x7ff) {
      _800bf0dc.setu(newCut - 0x800L);
      _800bf0ec.setu(newScene);
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
      _80052c34.setu(newScene);
      smapLoadingStage_800cb430.setu(0x4L);
      _800cb450.setu(newCut);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fc) {
      scriptsTickDisabled_800bc0b8.set(true);
      whichMenu_800bdc38.setu(0x1fL);
      smapLoadingStage_800cb430.setu(0xdL);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fa) {
      scriptsTickDisabled_800bc0b8.set(true);
      whichMenu_800bdc38.setu(0x15L);
      smapLoadingStage_800cb430.setu(0xdL);
      _800cb450.setu(cut);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fb) {
      scriptsTickDisabled_800bc0b8.set(true);
      smapLoadingStage_800cb430.setu(0x14L);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fe) {
      scriptsTickDisabled_800bc0b8.set(true);
      whichMenu_800bdc38.setu(0x6L);
      smapLoadingStage_800cb430.setu(0xdL);
      _800f7e4c.setu(0x1L);
      return 1;
    }

    if(newScene == 0x3fd) {
      whichMenu_800bdc38.setu(0x10L);
      smapLoadingStage_800cb430.setu(0xdL);
      _800f7e30.setu(index_80052c38.get());
      index_80052c38.set((int)_800f7e30.offset(gameState_800babc8.chapterIndex_98.get() * 0x8L).get());
      _800cb450.setu(_800f7e2c.offset(gameState_800babc8.chapterIndex_98.get() * 0x8L).get());
      _800cab24.set(FUN_800ea974(-0x1L));
      scriptsTickDisabled_800bc0b8.set(true);
      return 1;
    }

    if(newScene == 0x3ff) {
      scriptsTickDisabled_800bc0b8.set(true);
      whichMenu_800bdc38.setu(0x1L);
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
        scriptsTickDisabled_800bc0b8.set(true);
        _800f7e4c.setu(0x1L);
        return 1;
      }

      scene = newScene;
    }

    encounterId_800bb0f8.set(scene);

    if(isScriptLoaded(0)) {
      final BigStruct struct = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class);
      getScreenOffset(screenOffsetX_800bed50, screenOffsetY_800bed54);
      _80052c3c.setu(cut);
      matrix_800bed30.set(struct.coord2_14.coord);
      matrix_800bed30.transfer.set(struct.coord2_14.coord.transfer);
      _80052c40.setu(0x1L);
    }

    scriptsTickDisabled_800bc0b8.set(true);
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
        DrawSync(0);
        setWidthAndFlags(384L, 0);
        DrawSync(0);
        _800caaf4.setu(submapCut_80052c30.get());
        _800caaf8.setu(_80052c34);
        return;
      }
    }

    //LAB_800e5a30
    //LAB_800e5a34
    if(pregameLoadingStage_800bb10c.get() == 0) {
      pregameLoadingStage_800bb10c.setu(0x1L);
      _800caaf4.setu(submapCut_80052c30.get());
      _800caaf8.setu(_80052c34);
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

    final IntRef drgnIndex = new IntRef();
    final IntRef fileIndex = new IntRef();

    //LAB_800e5ac4
    switch((int)smapLoadingStage_800cb430.get()) {
      case 0x0:
        srand(getTimerValue(0));

        if(_800cb440.get() == 0) {
          setWidthAndFlags(384L, 0);
        }

        //LAB_800e5b2c
        _80052c44.setu(0x2L);
        encounterAccumulator_800c6ae8.setu(0);
        smapLoadingStage_800cb430.setu(0x1L);
        break;

      case 0x1: // Load newroot
        newrootLoaded_800cab1c.setu(0);
        loadFile(_80052c4c, 0, getMethodAddress(SMap.class, "newrootCallback_800e54a4", long.class, long.class, long.class), 0x63L, 0);
        smapLoadingStage_800cb430.setu(0x2L);
        break;

      case 0x2: // Wait for newroot to load
        if(newrootLoaded_800cab1c.get() != 0) {
          smapLoadingStage_800cb430.setu(0x3L);
        }

        break;

      case 0x3:
        _800cab28.setu(0);
        _80052c44.setu(0x1L);
        _800caaf4.setu(submapCut_80052c30.get());
        _800caaf8.setu(_80052c34);

        // Detect if we need to change disks
        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);
        if(drgnIndex.get() != drgnBinIndex_800bc058.get()) {
          //LAB_800e5c9c
          diskNum_8004ddc0.set(drgnIndex.get());
          _800bc05c.setu(0x5L);
          smapLoadingStage_800cb430.setu(0x16L);
          break;
        }

        //LAB_800e5ccc
        backgroundLoaded_800cab10.setu(0);
        loadDrgnBinFile(2, fileIndex.get(), 0, getMethodAddress(SMap.class, "loadBackground", long.class, long.class, long.class), 0, 0x4L);
        noop_800e4fec();
        smapLoadingStage_800cb430.setu(0x6L);
        break;

      case 0x4:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800caafc.setu(submapCut_80052c30.get());
        _800cab00.setu(_80052c34);
        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);
        smapLoadingStage_800cb430.setu(0x11L);
        break;

      case 0x6:
        if(backgroundLoaded_800cab10.get() != 0) {
          smapLoadingStage_800cb430.setu(0x7L);
        }

        break;

      case 0x7:
        if(_800cb440.get() == 0) {
          //LAB_800e5d60
          smapLoadingStage_800cb430.setu(0x9L);
        } else {
          smapLoadingStage_800cb430.setu(0x8L);
        }

        break;

      case 0x9:
        FUN_800e4d00(submapCut_80052c30.get(), (int)_80052c34.get());
        FUN_800e81a0((int)_80052c34.get());
        FUN_800e664c(submapCut_80052c30.get(), 0x1L);
        FUN_800e6d4c();

        if(_800cab2c.get() != 0) { // This might be to transition to another map or something?
          FUN_800e7328();
          buildBackgroundRenderingPacket(envStruct_800cab30);
          FUN_800e74d0();
          _800cab2c.setu(0);
        }

        //LAB_800e5e20
        smapLoadingStage_800cb430.setu(0xaL);
        break;

      case 0xa:
        loadingStage_800c68e4.setu(0);
        executeSceneGraphicsLoadingStage((int)_800caaf8.get());
        smapLoadingStage_800cb430.setu(0xbL);
        break;

      case 0xb:
        executeSceneGraphicsLoadingStage((int)_800caaf8.get());
        if(loadingStage_800c68e4.get() == 0xaL) {
          if(isScriptLoaded(0)) {
            scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.set((int)_800caaf8.get());
          }

          //LAB_800e5e94
          FUN_800e770c();
          _800bdc34.setu(0);
          _80052c44.setu(0);
          scriptStartEffect(0x2L, 0xaL);
          _800cab24.set(FUN_800ea974(_800caaf4.get()));
          cacheHasNoEncounters();
          smapLoadingStage_800cb430.setu(0xcL);
          scriptsTickDisabled_800bc0b8.set(false);
          _800c6ae0.setu(0);
        }

        break;

      case 0xc:
        _80052c44.setu(0);
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        if(joypadPress_8007a398.get(0x10L) != 0 && gameState_800babc8._4e3.get() == 0) {
          FUN_800e5534(-1, 1023);
        }

        break;

      case 0xd:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || scriptEffect_800bb140._24.get() == 0) {
          if(scriptEffect_800bb140._24.get() == 0) {
            scriptStartEffect(0x1L, 0xaL);
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

        break;

      case 0xe:
        _800caaf0.setu(whichMenu_800bdc38);
        _80052c44.setu(0x2L);

        if(whichMenu_800bdc38.get() != 0) {
          FUN_80022590();

          if(whichMenu_800bdc38.get() != 0) {
            break;
          }
        }

        //LAB_800e6018
        _800c6aac.setu(0xaL);

        switch((int)_800caaf0.get()) {
          case 0x5:
            if(gameState_800babc8._4e4.get() != 0) {
              smapLoadingStage_800cb430.setu(0x12L);
              _800f7e4c.setu(0);
              break;
            }

            // Fall through

          case 0x19:
          case 0x23:
          case 0xa:
            smapLoadingStage_800cb430.setu(0xfL);
            break;

          case 0x14:
            smapLoadingStage_800cb430.setu(0xcL);
            _800f7e4c.setu(0);
            FUN_800e5534((int)_800f7e2c.offset(gameState_800babc8.chapterIndex_98.get() * 8).get(), (int)_800f7e30.offset(gameState_800babc8.chapterIndex_98.get() * 8).get());
            index_80052c38.set((int)_800f7e30.get());
            break;
        }

        break;

      case 0xf:
        _80052c44.setu(0);
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        scriptsTickDisabled_800bc0b8.set(false);
        _800f7e4c.setu(0);
        smapLoadingStage_800cb430.setu(0xcL);
        if(_800bdc34.get() != 0) {
          FUN_800e5534(submapCut_80052c30.get(), (int)_80052c34.get());
        }

        break;

      case 0x10:
        smapLoadingStage_800cb430.setu(0x3L);
        _80052c44.setu(0x3L);
        _800f7e4c.setu(0);
        break;

      case 0x11:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());

        if(isScriptLoaded(0)) {
          scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class).us_12a.set(1);
        }

        //LAB_800e61bc
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || scriptEffect_800bb140._24.get() == 0) {
          if(scriptEffect_800bb140._24.get() == 0) {
            scriptStartEffect(0x1L, 0xaL);
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

        break;

      case 0x12:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || scriptEffect_800bb140._24.get() == 0) {
          if(scriptEffect_800bb140._24.get() == 0) {
            scriptStartEffect(0x1L, 0xaL);
          }

          //LAB_800e62b0
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e62cc
        if(a0) {
          mainCallbackIndexOnceLoaded_8004dd24.setu(0x8L);
          pregameLoadingStage_800bb10c.setu(0);
          vsyncMode_8007a3b8.setu(0x2L);
          _80052c44.setu(0x5L);
          _800f7e4c.setu(0);
          scriptsTickDisabled_800bc0b8.set(false);
        }

        break;

      case 0x13:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _80052c44.setu(0x5L);
        mainCallbackIndexOnceLoaded_8004dd24.setu(0x6L);
        pregameLoadingStage_800bb10c.setu(0);
        vsyncMode_8007a3b8.setu(0x2L);
        _800f7e4c.setu(0);
        scriptsTickDisabled_800bc0b8.set(false);
        break;

      case 0x14:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || scriptEffect_800bb140._24.get() == 0) {
          if(scriptEffect_800bb140._24.get() == 0) {
            scriptStartEffect(0x1L, 0xaL);
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
          mainCallbackIndexOnceLoaded_8004dd24.setu(0x2L);
          vsyncMode_8007a3b8.setu(0x2L);
          pregameLoadingStage_800bb10c.setu(0);

          //LAB_800e6484
          _80052c44.setu(0x5L);

          //LAB_800e6490
          _800f7e4c.setu(0);
          scriptsTickDisabled_800bc0b8.set(false);
        }

        break;

      case 0x15:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || scriptEffect_800bb140._24.get() == 0) {
          if(scriptEffect_800bb140._24.get() == 0) {
            scriptStartEffect(0x1L, 0xaL);
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
          mainCallbackIndexOnceLoaded_8004dd24.setu(0x9L);
          pregameLoadingStage_800bb10c.setu(0);
          vsyncMode_8007a3b8.setu(0x2L);
          _800f7e4c.setu(0);
          scriptsTickDisabled_800bc0b8.set(false);
        }

        break;

      case 0x16:
        mainCallbackIndexOnceLoaded_8004dd24.setu(0xaL);
        vsyncMode_8007a3b8.setu(0x2L);
        _80052c44.setu(0x1L);
        pregameLoadingStage_800bb10c.setu(0);
        break;

      case 0x17:
        mainCallbackIndexOnceLoaded_8004dd24.setu(0x2L);
        vsyncMode_8007a3b8.setu(0x2L);
        pregameLoadingStage_800bb10c.setu(0);
        break;
    }

    //caseD_5
  }

  @Method(0x800e6504L)
  public static void getDrgnFileFromNewRoot(final int submapCut, final IntRef drgnIndexOut, final IntRef fileIndexOut) {
    final NewRootEntryStruct entry = newRootPtr_800cb458.deref().entries_0000.get(submapCut);

    final int drgnIndexA = entry.ub_01.get() >>> 5;
    final int chapterIndex = entry.ub_03.get() >>> 5;

    final boolean second;
    if(drgnIndexA == drgnBinIndex_800bc058.get() - 1) {
      drgnIndexOut.set(drgnIndexA);
      second = false;
    } else if(chapterIndex == drgnBinIndex_800bc058.get() - 1 && chapterIndex <= gameState_800babc8.chapterIndex_98.get()) {
      drgnIndexOut.set(chapterIndex);
      second = true;
      //LAB_800e6570
    } else if(chapterIndex >= 4) {
      drgnIndexOut.set(drgnIndexA);
      second = false;
    } else if(chapterIndex <= gameState_800babc8.chapterIndex_98.get()) {
      //LAB_800e6580
      drgnIndexOut.set(chapterIndex);
      second = true;
    } else {
      //LAB_800e658c
      drgnIndexOut.set(drgnIndexA);
      second = false;
    }

    //LAB_800e6594
    final int t0;
    if(drgnIndexOut.get() == 0 || drgnIndexOut.get() == 1 || drgnIndexOut.get() == 2 || drgnIndexOut.get() == 3) {
      //LAB_800e65bc
      //LAB_800e65cc
      t0 = 4;
    } else {
      t0 = 0;
    }

    //LAB_800e65d0
    drgnIndexOut.incr();

    int v1_0;
    int v0_0;
    if(second) {
      v1_0 = entry.ub_02.get();
      v0_0 = entry.ub_03.get();
    } else {
      //LAB_800e6604
      v1_0 = entry.ub_00.get();
      v0_0 = entry.ub_01.get();
    }

    v0_0 &= 0x1f;

    //LAB_800e6624
    v0_0 = v0_0 << 8 | v1_0;
    v1_0 = v0_0 * 3 + t0;
    fileIndexOut.set(v1_0);
  }

  @Method(0x800e6640L)
  public static void FUN_800e6640(final NewRootStruct newRoot) {
    newRootPtr_800cb458.set(newRoot);
  }

  @Method(0x800e664cL)
  public static void FUN_800e664c(final long submapCut, final long a1) {
    fillMemory(arr_800cb460.getAddress(), 0, 0x100L);

    final NewRootEntryStruct entry = newRootPtr_800cb458.deref().entries_0000.get((int)submapCut);
    final short offset = (short)(entry.ub_05.get() << 8 | entry.ub_04.get());

    if(offset < 0) {
      return;
    }

    //LAB_800e66dc
    for(int i = 0; i < (entry.ub_07.get() << 8 | entry.ub_06.get()); i++) {
      final int v1 = (int)newRootPtr_800cb458.deref().uia_2000.get(offset / 4 + i).get();
      arr_800cb460.get((v1 >> 8 & 0xfc) / 4).set(v1);
    }

    //LAB_800e671c
  }

  @Method(0x800e6730L)
  public static int FUN_800e6730(final int index) {
    // This did unsigned comparison, so -1 was >= 0x40
    if(index < 0 || index >= 0x40) {
      return 0;
    }

    return arr_800cb460.get(index).get();
  }

  @Method(0x800e675cL)
  public static void FUN_800e675c(final int a0) {
    arr_800cb460.get((a0 >> 8 & 0xfc) >> 2).set(a0);
  }

  @Method(0x800e6798L)
  public static int FUN_800e6798(final int index, final long a1, final long x, final long y, final long z, final SVECTOR playerMovement) {
    final long v1 = FUN_800e6730(index);

    if(v1 == 0) {
      return 1;
    }

    if((v1 & 0x8L) != 0) {
      return 0;
    }

    //LAB_800e67c4
    return 1;
  }

  @Method(0x800e67d4L)
  public static long FUN_800e67d4(final RunningScript a0) {
    final int scene = a0.params_20.get(1).deref().get();

    FUN_800e5534(a0.params_20.get(0).deref().get(), scene);

    if(scene == 0x3fe || scene == 0x3fa || scene == 0x3ff) {
      return 0;
    }

    //LAB_800e6828
    return (scene ^ 0x3fc) > 0 ? 2 : 0;
  }

  @Method(0x800e683cL)
  public static long FUN_800e683c(final RunningScript a0) {
    if(a0.params_20.get(0).deref().get() < 0x3L) {
      _800f7e50.setu(a0.params_20.get(0).deref().get());
    }

    //LAB_800e686c
    if(a0.params_20.get(0).deref().get() == 0x3L) {
      FUN_800e76b0(0x400L, 0x400L, a0.params_20.get(1).deref().get());
    }

    //LAB_800e688c
    a0.params_20.get(2).deref().set((int)_800f7e50.get());
    return 0;
  }

  @Method(0x800e68b4L)
  public static long FUN_800e68b4(final RunningScript a0) {
    final IntRef x = new IntRef();
    final IntRef y = new IntRef();
    getScreenOffset(x, y);
    a0.params_20.get(0).deref().set(x.get());
    a0.params_20.get(1).deref().set(y.get());
    return 0;
  }

  @Method(0x800e6904L)
  public static long FUN_800e6904(final RunningScript a0) {
    final int x = a0.params_20.get(0).deref().get();
    final int y = a0.params_20.get(1).deref().get();
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
    return 0;
  }

  @Method(0x800e69a4L)
  public static long FUN_800e69a4(final RunningScript a0) {
    a0.params_20.get(0).deref().set(FUN_800e6730(a0.params_20.get(1).deref().get()));
    return 0;
  }

  @Method(0x800e69e8L)
  public static long FUN_800e69e8(final RunningScript a0) {
    return 0;
  }

  @Method(0x800e69f0L)
  public static long FUN_800e69f0(final RunningScript a0) {
    FUN_800e675c(FUN_800e6730(a0.params_20.get(0).deref().get()) | 0x8);
    return 0;
  }

  @Method(0x800e6a28L)
  public static long FUN_800e6a28(final RunningScript a0) {
    FUN_800e675c(FUN_800e6730(a0.params_20.get(0).deref().get()) & 0xffff_fff7);
    return 0;
  }

  @Method(0x800e6a64L)
  public static long FUN_800e6a64(final RunningScript a0) {
    FUN_800e76b0(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800e6aa0L)
  public static long FUN_800e6aa0(final RunningScript a0) {
    a0.params_20.get(3).deref().set((int)FUN_800e7728(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get()));
    return 0;
  }

  @Method(0x800e6af0L)
  public static long FUN_800e6af0(final RunningScript a0) {
    if(a0.params_20.get(1).deref().get() == 0x1L) {
      if(a0.params_20.get(0).deref().get() != 0) {
        _800f7e54.oru(0x1L);
      } else {
        //LAB_800e6b34
        _800f7e54.and(0xffff_fffeL);
      }
    }

    //LAB_800e6b48
    a0.params_20.get(2).deref().set((int)_800f7e54.get());
    return 0;
  }

  @Method(0x800e6b64L)
  public static long FUN_800e6b64(final RunningScript a0) {
    if(a0.params_20.get(0).deref().get() >= 0) {
      final SVECTOR sp0x10 = new SVECTOR();
      get3dAverageOfSomething(a0.params_20.get(0).deref().get(), sp0x10);

      a0.params_20.get(1).deref().set(sp0x10.getX());
      a0.params_20.get(2).deref().set(sp0x10.getY());
      a0.params_20.get(3).deref().set(sp0x10.getZ());
    }

    //LAB_800e6bc8
    return 0;
  }

  @Method(0x800e6bd8L)
  public static long FUN_800e6bd8(final RunningScript a0) {
    return 0;
  }

  @Method(0x800e6be0L)
  public static long FUN_800e6be0(final RunningScript a0) {
    final MATRIX coord = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(a0.params_20.get(0).deref().get()).get()).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14.coord;
    a0.params_20.get(1).deref().set((matrix_800c3548.get(6) * coord.transfer.getX() + matrix_800c3548.get(7) * coord.transfer.getY() + matrix_800c3548.get(8) * coord.transfer.getZ() >> 12) + matrix_800c3548.transfer.getZ() >> 16 - orderingTableBits_1f8003c0.get());
    return 0;
  }

  @Method(0x800e6cacL)
  public static long FUN_800e6cac(final RunningScript a0) {
    FUN_800e80e4(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e6ce0L)
  public static long FUN_800e6ce0(final RunningScript a0) {
    FUN_800e5534(a0.params_20.get(0).deref().get() + 0x800, a0.params_20.get(1).deref().get());
    submapCut_80052c30.set(a0.params_20.get(2).deref().get());
    _800cb450.setu(submapCut_80052c30.get());
    _80052c34.setu(a0.params_20.get(3).deref().get());
    return 0x2L;
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
  public static void FUN_800e6d9c(final UnboundedArrayRef<EnvironmentStruct> envs, final long count) {
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
      final EnvironmentStruct env = envs.get(i);

      if(env.s_06.get() == 0x4eL) {
        v1 = env.pos_08.w.get() + (short)env.us_10.get();
        if(v1 < t1) {
          v1 = t1;
        }

        //LAB_800e6e00
        t1 = v1;
        v1 = (short)env.us_10.get();
        if(t3 < v1) {
          v1 = t3;
        }

        //LAB_800e6e1c
        t3 = v1;
        v1 = env.pos_08.h.get() + (short)env.us_12.get();
        if(v1 < t0) {
          v1 = t0;
        }

        //LAB_800e6e44
        t0 = v1;
        v1 = (short)env.us_12.get();
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
  public static void buildBackgroundRenderingPacket(final UnboundedArrayRef<EnvironmentStruct> env) {
    //LAB_800e6f9c
    for(int i = 0; i < backgroundObjectsCount_800cb584.get(); i++) {
      final EnvironmentStruct s0 = env.get(i);
      final long renderPacket = _800cb710.offset(i * 0x24L).offset(0x8L).getAddress();

      MEMORY.ref(1, renderPacket).offset(0x3L).setu(0x4L); // 4 words
      MEMORY.ref(4, renderPacket).offset(0x4L).setu(0x6480_8080L); // Textured rectangle, variable size, opaque, texture-blending

      long clutY = Math.abs(s0.clutY_22.get());
      if(i < _800cb57c.get()) {
        if(clutY - 0x1f0L >= 0x10L) {
          clutY = i + 0x1f0L;
        }

        //LAB_800e7004
        MEMORY.ref(2, renderPacket).offset(0xeL).setu(clutY << 6 | 0x30L); // CLUT
      } else {
        //LAB_800e7010
        MEMORY.ref(2, renderPacket).offset(0xeL).setu(clutY << 6 | 0x30L); // CLUT
        memcpy(_800cbb90.offset((i - _800cb57c.get()) * 0x8L).getAddress(), s0.svec_14.getAddress(), 8);
        _800cbc90.offset((i - _800cb57c.get()) * 0x4L).setu(s0.ui_1c.get());
      }

      //LAB_800e7074
      MEMORY.ref(1, renderPacket).offset(0x4L).setu(0x80L); // R
      MEMORY.ref(1, renderPacket).offset(0x5L).setu(0x80L); // G
      MEMORY.ref(1, renderPacket).offset(0x6L).setu(0x80L); // B
      MEMORY.ref(1, renderPacket).offset(0x0cL).setu(s0.pos_08.x.get()); // X
      MEMORY.ref(1, renderPacket).offset(0x0dL).setu(s0.pos_08.y.get()); // Y
      MEMORY.ref(2, renderPacket).offset(0x10L).setu(s0.pos_08.w.get()); // Width
      MEMORY.ref(2, renderPacket).offset(0x12L).setu(s0.pos_08.h.get()); // Height

      final long s0_0 = _800cb710.offset(i * 0x24L).getAddress();
      SetDrawTPage(MEMORY.ref(4, s0_0, DR_TPAGE::new), false, true, s0.tpage_20.get());

      if(s0.clutY_22.get() < 0) {
        gpuLinkedListSetCommandTransparency(s0_0, true);
      }

      //LAB_800e70ec
      final long tpagePacket = _800cb710.offset(i * 0x24L).getAddress();
      MargePrim(tpagePacket, renderPacket);

      MEMORY.ref(2, tpagePacket).offset(0x1cL).setu(s0.us_10.get());
      MEMORY.ref(2, tpagePacket).offset(0x1eL).setu(s0.us_12.get());

      if(s0.s_06.get() == 0x4eL) {
        //LAB_800e7148
        MEMORY.ref(2, tpagePacket).offset(0x20L).setu((0x1L << orderingTableBits_1f8003c0.get()) - 0x1L);
      } else if(s0.s_06.get() == 0x4fL) {
        MEMORY.ref(2, tpagePacket).offset(0x20L).setu(0x28L);
      } else {
        //LAB_800e7194
        long a0 =
          matrix_800c3548.get(6) * s0.svec_00.getX() +
          matrix_800c3548.get(7) * s0.svec_00.getY() +
          matrix_800c3548.get(8) * s0.svec_00.getZ();
        a0 >>= 12;
        a0 += matrix_800c3548.transfer.z.get();
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
    GsSetRefView2(rview2_800cbd10);
    clearSmallValuesFromMatrix(matrix_800c3548);
    matrix_800cbd68.set(matrix_800c3548);
    TransposeMatrix(matrix_800cbd68, matrix_800cbd40);
    rview2_800bd7e8.set(rview2_800cbd10);
  }

  @Method(0x800e7418L)
  public static void updateRview2(final long xy0, final long z0, final long xy1, final long z1, final long rotation, final long projectionDistance) {
    rview2_800cbd10.viewpoint_00.setX((short)xy0);
    rview2_800cbd10.viewpoint_00.setY((short)(xy0 >>> 16));
    rview2_800cbd10.viewpoint_00.setZ((short)z0);
    rview2_800cbd10.refpoint_0c.setX((short)xy1);
    rview2_800cbd10.refpoint_0c.setY((short)(xy1 >>> 16));
    rview2_800cbd10.refpoint_0c.setZ((short)z1);
    rview2_800cbd10.viewpointTwist_18.set((int)(rotation << 16) >> 4);
    rview2_800cbd10.super_1c.clear();
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
    backgroundObjectsCount_800cb584.setu(envFile.count_14.get());
    _800cb57c.setu(envFile.ub_15.get());
    _800cb580.setu(envFile.ub_16.get());

    updateRview2(
      envFile.viewpoint_00.getXY(),
      envFile.viewpoint_00.getZ(),
      envFile.refpoint_08.getXY(),
      envFile.refpoint_08.getZ(),
      envFile.rotation_12.get(),
      envFile.projectionDistance_10.get()
    );

    memcpy(envStruct_800cab30.getAddress(), envFile.environments_18.getAddress(), (int)(backgroundObjectsCount_800cb584.get() * 0x24L));
    buildBackgroundRenderingPacket(envStruct_800cab30);
    fillMemory(_800cb590.getAddress(), 0, 0x180L);
  }

  @Method(0x800e7604L)
  public static void setGeomOffsetIfNotSet(final int x, final int y) {
    if(_800cbd3c.deref()._00.get() == 0) {
      _800cbd3c.deref()._00.set(0x1L);
      SetGeomOffset(x, y);
    }
  }

  @Method(0x800e7650L)
  public static void setScreenOffsetIfNotSet(final long x, final long y) {
    // Added null check - bug in game code
    if(!_800cbd38.isNull() && _800cbd38.deref()._00.get() == 0) {
      _800cbd38.deref()._00.set(0x1L);
      screenOffsetX_800cb568.setu(x);
      screenOffsetY_800cb56c.setu(y);
    }
  }

  @Method(0x800e7690L)
  public static void getScreenOffset(final IntRef offsetX, final IntRef offsetY) {
    offsetX.set((int)screenOffsetX_800cb568.get());
    offsetY.set((int)screenOffsetY_800cb56c.get());
  }

  @Method(0x800e76b0L)
  public static void FUN_800e76b0(final long a0, final long a1, final long a2) {
    if(a0 == 0x400L && a1 == 0x400L) {
      _800cb590.offset(4, a2 * 0xcL).offset(0x8L).setu(0x1L);
      return;
    }

    //LAB_800e76e8
    //LAB_800e76ec
    _800cb590.offset(4, a2 * 0xcL).setu(a0);
    _800cb590.offset(4, a2 * 0xcL).offset(0x4L).setu(a1);
    _800cb590.offset(4, a2 * 0xcL).offset(0x8L).setu(0);
  }

  @Method(0x800e770cL)
  public static void FUN_800e770c() {
    _800cbd60.setu(0);
    _800cbd64.setu(scriptCount_800c6730);
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
   * Renderes the background?
   */
  @Method(0x800e7954L)
  public static void FUN_800e7954(final GsOT ot, final MATRIX[] a1, final long a2) {
    long s0;
    long s1;
    long s3;
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t3;
    final long[] sp10 = new long[(int)a2];
    final long[] sp38 = new long[(int)_800cb580.get()];

    s1 = _800cb710.getAddress();

    //LAB_800e79b8
    for(int i = 0; i < _800cb57c.get(); i++) {
      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x1cL);
      v1 = _800cb560.getSigned();
      v1 += screenOffsetX_800cb568.getSigned();
      v1 += MEMORY.ref(2, s1).offset(0x1cL).get();
      MEMORY.ref(2, s1).offset(0x10L).setu(v1);
      v1 = _800cb564.getSigned();
      v1 += screenOffsetY_800cb56c.getSigned();
      v1 += MEMORY.ref(2, s1).offset(0x1eL).get();
      MEMORY.ref(2, s1).offset(0x12L).setu(v1);
      memcpy(s0, s1, 0x1c);
      insertElementIntoLinkedList(ot.org_04.deref().get((int)MEMORY.ref(2, s1).offset(0x20L).get()).getAddress(), s0);
      s1 += 0x24L;
    }

    //LAB_800e7a60
    //LAB_800e7a7c
    for(int i = 0; i < a2; i++) {
      sp10[i] = (matrix_800c3548.get(6) * a1[i].transfer.getX() +
        matrix_800c3548.get(7) * a1[i].transfer.getY() +
        matrix_800c3548.get(8) * a1[i].transfer.getZ() >> 12) + matrix_800c3548.transfer.getZ() >> 0x10L - orderingTableBits_1f8003c0.get();
    }

    //LAB_800e7b08
    s3 = _800cb710.getAddress();
    s0 = _800cb57c.get();
    final long s4 = _800cbb90.getAddress();

    //LAB_800e7b40
    outerForLoop:
    for(int i = 0; i < _800cb580.get(); i++) {
      t3 = 0x7fff_ffffL;
      v1 = MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x22L).getSigned(0xc000L) >> 14;
      if(v1 != 0x1L) {
        if(v1 != 2) {
          //LAB_800e7d78
          sp38[i] = MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).get();
        } else {
          sp38[i] = MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x22L).get() & 0x3fffL;
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
          v1 += MEMORY.ref(2, s4).offset(i * 0x8L).offset(0x0L).getSigned() * a1[(int)a2_0].transfer.getX();
          v1 += MEMORY.ref(2, s4).offset(i * 0x8L).offset(0x2L).getSigned() * a1[(int)a2_0].transfer.getY();
          v1 += MEMORY.ref(2, s4).offset(i * 0x8L).offset(0x4L).getSigned() * a1[(int)a2_0].transfer.getZ();
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
          sp38[i] = Math.max(a3 - 0x32L, 0x28L);
          continue outerForLoop;
        }

        //LAB_800e7d00
        if(t1 == 0) {
          //LAB_800e7d3c
          sp38[i] = Math.min(a3 + 0x32L, (0x1L << orderingTableBits_1f8003c0.get()) - 0x1L);
          continue outerForLoop;
        }

        break;
      } while(true);

      //LAB_800e7d50
      if(a3 > MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).getSigned() || t3 < MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).getSigned()) {
        //LAB_800e7d64
        sp38[i] = (a3 + t3) / 0x2L;
      } else {
        //LAB_800e7d78
        sp38[i] = MEMORY.ref(2, s3).offset((i + s0) * 0x24L).offset(0x20L).getSigned();
      }

      //LAB_800e7d80
    }

    //LAB_800e7d9c
    s1 = _800cb710.offset(_800cb57c.get() * 0x24L).getAddress();

    //LAB_800e7de0
    for(int i = 0; i < _800cb580.get(); i++) {
      s3 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x1cL);
      if(_800cb590.offset(i * 0xcL).offset(0x8L).get() == 0) {
        v0 = _800cb560.getSigned();
        v0 += screenOffsetX_800cb568.getSigned();
        v0 += MEMORY.ref(2, s1).offset(0x1cL).get();
        v0 += _800cb590.offset(i * 0xcL).get();
        MEMORY.ref(2, s1).offset(0x10L).setu(v0);
        v0 = _800cb564.getSigned();
        v0 += screenOffsetY_800cb56c.getSigned();
        v0 += MEMORY.ref(2, s1).offset(0x1eL).get();
        v0 += _800cb590.offset(i * 0xcL).offset(0x4L).get();
        MEMORY.ref(2, s1).offset(0x12L).setu(v0);
        memcpy(s3, s1, 0x1c);
        insertElementIntoLinkedList(ot.org_04.deref().get((int)sp38[i]).getAddress(), s3);
      }

      //LAB_800e7eb4
      s1 += 0x24L;
    }

    //LAB_800e7ed0
  }

  @Method(0x800e7f00L)
  public static void transformVertex(final IntRef x, final IntRef y, final SVECTOR v0) {
    final DVECTOR[] v1 = {new DVECTOR()};
    RotTransPersN(new SVECTOR[] {v0}, v1, new UnsignedShortRef[] {new UnsignedShortRef()}, new UnsignedShortRef[] {new UnsignedShortRef()}, new UnsignedShortRef[] {new UnsignedShortRef()}, 1);
    x.set(v1[0].getX());
    y.set(v1[0].getY());
  }

  @Method(0x800e7f68L)
  public static void FUN_800e7f68(final long x, final long y) {
    if(x < -80L) {
      screenOffsetX_800cb568.subu(80L).subu(x);
      //LAB_800e7f80
    } else if(x > 80L) {
      //LAB_800e7f9c
      screenOffsetX_800cb568.addu(80L).subu(x);
    }

    //LAB_800e7fa8
    if(y < -40L) {
      screenOffsetY_800cb56c.subu(40L).subu(y);
      //LAB_800e7fbc
    } else if(y > 40L) {
      //LAB_800e7fd4
      screenOffsetY_800cb56c.addu(40L).subu(y);
    }

    //LAB_800e7fdc
    if(_800f7f0c.get() != 0) {
      screenOffsetX_800cb568.addu(_800cbd30);
      screenOffsetY_800cb56c.addu(_800cbd34);
      _800f7f0c.setu(0);
      return;
    }

    //LAB_800e8030
    if(screenOffsetX_800cb568.getSigned() < _800cb574.getSigned()) {
      //LAB_800e807c
      screenOffsetX_800cb568.setu(_800cb574);
    } else {
      //LAB_800e8070
      screenOffsetX_800cb568.setu(Math.min(_800cb570.getSigned(), screenOffsetX_800cb568.getSigned()));
    }

    //LAB_800e8080
    //LAB_800e8088
    if(screenOffsetY_800cb56c.getSigned() < -_800cb578.getSigned()) {
      screenOffsetY_800cb56c.setu(-_800cb578.getSigned());
    } else {
      //LAB_800e80d0
      //LAB_800e80d8
      screenOffsetY_800cb56c.setu(Math.min(_800cb578.getSigned(), screenOffsetY_800cb56c.getSigned()));
    }

    //LAB_800e80dc
  }

  @Method(0x800e06c4L)
  public static long FUN_800e06c4(final RunningScript a0) {
    final long a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, a1).offset(0x1a0L).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(4, a1).offset(0x1a4L).setu(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800e0710L)
  public static long FUN_800e0710(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).i_19c.get());
    return 0;
  }

  @Method(0x800e80e4L)
  public static void FUN_800e80e4(final int a0, final int a1) {
    _800cbd30.setu(a0);
    _800cbd34.setu(a1);
    _800f7f0c.setu(0x1L);
  }

  @Method(0x800e8104L)
  public static void FUN_800e8104(final SVECTOR v0) {
    if(_800cbd38.deref()._00.get() == 0) {
      _800cbd38.deref()._00.set(0x1L);

      final IntRef transformedX = new IntRef();
      final IntRef transformedY = new IntRef();
      transformVertex(transformedX, transformedY, v0);
      FUN_800e7f68(transformedX.get(), transformedY.get());
    }

    //LAB_800e8164
    setScreenOffsetIfNotSet(screenOffsetX_800cb568.getSigned(), screenOffsetY_800cb56c.getSigned());
    setGeomOffsetIfNotSet((int)screenOffsetX_800cb568.getSigned(), (int)screenOffsetY_800cb56c.getSigned());
  }

  @Method(0x800e81a0L)
  public static void FUN_800e81a0(final int index) {
    final UnknownStruct2 s0_0 = MEMORY.ref(4, addToLinkedListTail(0x8L), UnknownStruct2::new);
    fillMemory(s0_0.getAddress(), 0, 0x8L);
    FUN_800e5084(getBiFunctionAddress(SMap.class, "FUN_800e4f74", UnknownStruct2.class, long.class, long.class), s0_0, 0);
    _800cbd38.set(s0_0);

    final UnknownStruct2 s0_1 = MEMORY.ref(4, addToLinkedListTail(0x8L), UnknownStruct2::new);
    fillMemory(s0_1.getAddress(), 0, 0x8L);
    FUN_800e5084(getBiFunctionAddress(SMap.class, "FUN_800e4f74", UnknownStruct2.class, long.class, long.class), s0_1, 0);
    _800cbd3c.set(s0_1);

    final SVECTOR avg = new SVECTOR();
    get3dAverageOfSomething(index, avg);
    FUN_800e8104(avg);
  }

  @Method(0x800e828cL)
  public static void FUN_800e828c() {
    removeFromLinkedList(_800cbd38.getPointer());
    removeFromLinkedList(_800cbd3c.getPointer());
  }

  @Method(0x800e82ccL)
  public static void transformToWorldspace(final SVECTOR out, final SVECTOR in) {
    if(matrix_800cbd40.get(2) == 0) {
      out.set(in);
    } else {
      //LAB_800e8318
      PushMatrix();
      ApplyMatrixSV(matrix_800cbd40, in, out);
      PopMatrix();
    }

    //LAB_800e833c
  }

  @Method(0x800e866cL)
  public static void FUN_800e866c() {
    //LAB_800e86a4
    for(int i = 0; i < SomethingStructPtr_800d1a88.deref().count_0c.get(); i++) {
      final long v0 = Math.abs((int)MEMORY.ref(2, SomethingStructPtr_800d1a88.deref().normals_08.get()).offset(i * 0x8L).offset(0x2L).getSigned()); //TODO
      SomethingStructPtr_800d1a88.deref().ptr_14.deref().get(i).bool_01.set(v0 > 0x400L);
    }

    //LAB_800e86f0
  }

  @Method(0x800e88a0L)
  public static int FUN_800e88a0(final long a0, final MATRIX playerTransforms, final SVECTOR playerMovement) {
    if(a0 != 0) {
      return FUN_800e9430(0, playerTransforms.transfer.getX(), playerTransforms.transfer.getY(), playerTransforms.transfer.getZ(), playerMovement);
    }

    //LAB_800e88d8
    if(_800cbe34.deref()._00.get() == 0) {
      _800cbe34.deref()._00.set(0x1L);

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
    final SomethingStruct struct = SomethingStructPtr_800d1a88.deref();

    //LAB_800e89b8
    for(int i = 0; i < struct.count_0c.get(); i++) {
      //LAB_800e89e0
      if(_800f7f14.get() == 0) {
        //LAB_800e89e8
        vec.setX((short)0);
        vec.setY((short)0);
        vec.setZ((short)0);
      } else {
        //LAB_800e89f8
        final SomethingStruct2 struct2 = struct.ptr_14.deref().get(i);
        final long t1 = struct.primitives_10.get() + struct2.ptr_04.get() + 0x6L;

        vec.setX((short)0);
        vec.setY((short)0);
        vec.setZ((short)0);

        //LAB_800e8a38
        for(int t0 = 0; t0 < struct2.count_00.get(); t0++) {
          vec.add(struct.verts_04.deref().get((int)MEMORY.ref(2, t1).offset(t0 * 0x2L).get()));
        }

        //LAB_800e8a9c
        vec.div(struct2.count_00.get());
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
  public static void FUN_800e8b40(final SomethingStruct a0, final UnboundedArrayRef<SomethingStruct2> a1) {
    final int count = (int)a0.count_0c.get();

    memcpy(SomethingStruct2Arr_800cbe78.getAddress(), a1.getAddress(), count * 0xc);
    memcpy(_800cca78.getAddress(), a1.get(count).getAddress(), count * 0x40);

    a0.ptr_14.set(SomethingStruct2Arr_800cbe78);
    a0.ptr_18.set(_800cca78.getAddress());
  }

  @Method(0x800e8bd8L)
  public static void FUN_800e8bd8(final SomethingStruct a0) {
    final TmdObjTable objTable = a0.objTableArrPtr_00.deref().get(0);
    a0.verts_04.set(objTable.vert_top_00.deref());
    a0.normals_08.set(objTable.normal_top_08);
    a0.count_0c.set(objTable.n_primitive_14);
    a0.primitives_10.set(objTable.primitives_10.getPointer());
  }

  @Method(0x800e8c20L)
  public static UnboundedArrayRef<TmdObjTable> adjustTmdPointersAndGetTable(final TmdWithId tmd) {
    adjustTmdPointers(tmd.tmd);
    return tmd.tmd.objTable;
  }

  @Method(0x800e8c50L)
  public static void FUN_800e8c50(final GsDOBJ2 dobj2, final SomethingStruct a1, final TmdWithId tmd, final int tmdSize) {
    memcpy(tmd_800cfa78.getAddress(), tmd.getAddress(), tmdSize);
    a1.tmdPtr_1c.set(tmd_800cfa78);
    final UnboundedArrayRef<TmdObjTable> objTables = adjustTmdPointersAndGetTable(tmd_800cfa78);
    updateTmdPacketIlen(objTables, dobj2, 0);
    a1.objTableArrPtr_00.set(objTables);
    FUN_800e8bd8(a1);
  }

  @Method(0x800e8cd0L)
  public static void FUN_800e8cd0(final TmdWithId tmd, final int tmdSize, final UnboundedArrayRef<SomethingStruct2> a2, final long a3) {
    SomethingStructPtr_800d1a88.set(SomethingStruct_800cbe08);
    SomethingStruct_800cbe08.dobj2Ptr_20.set(GsDOBJ2_800cbdf8);
    SomethingStruct_800cbe08.coord2Ptr_24.set(GsCOORDINATE2_800cbda8);
    GsInitCoordinate2(null, GsCOORDINATE2_800cbda8);

    SomethingStructPtr_800d1a88.deref().dobj2Ptr_20.deref().coord2_04.set(SomethingStructPtr_800d1a88.deref().coord2Ptr_24.deref());
    SomethingStructPtr_800d1a88.deref().dobj2Ptr_20.deref().attribute_00.set(0x4000_0000L);

    FUN_800e8c50(SomethingStructPtr_800d1a88.deref().dobj2Ptr_20.deref(), SomethingStructPtr_800d1a88.deref(), tmd, tmdSize);
    FUN_800e8b40(SomethingStructPtr_800d1a88.deref(), a2);

    _800f7f10.setu(0);
    _800f7f14.setu(0x1L);

    final UnknownStruct2 s0_0 = MEMORY.ref(4, addToLinkedListTail(0x8L), UnknownStruct2::new);
    fillMemory(s0_0.getAddress(), 0, 0x8L);
    FUN_800e5084(getBiFunctionAddress(SMap.class, "FUN_800e4f74", UnknownStruct2.class, long.class, long.class), s0_0, 0);
    _800cbe34.set(s0_0);

    final UnknownStruct2 s0_1 = MEMORY.ref(4, addToLinkedListTail(0x8L), UnknownStruct2::new);
    fillMemory(s0_1.getAddress(), 0, 0x8L);
    FUN_800e5084(getBiFunctionAddress(SMap.class, "FUN_800e4f74", UnknownStruct2.class, long.class, long.class), s0_1, 0);
    _800d1a8c.set(s0_1);

    final UnknownStruct2 s0_2 = MEMORY.ref(4, addToLinkedListTail(0x8L), UnknownStruct2::new);
    fillMemory(s0_2.getAddress(), 0, 0x8L);
    FUN_800e5084(getBiFunctionAddress(SMap.class, "FUN_800e4f74", UnknownStruct2.class, long.class, long.class), s0_2, 0);
    _800cbe38.set(s0_2);

    FUN_800e866c();
  }

  /** Unloads data when transitioning */
  @Method(0x800e8e50L)
  public static void FUN_800e8e50() {
    _800f7f14.setu(0);

    removeFromLinkedList(_800cbe34.getPointer());
    removeFromLinkedList(_800d1a8c.getPointer());
    removeFromLinkedList(_800cbe38.getPointer());
  }

  @Method(0x800e9018L)
  public static long FUN_800e9018(final long x, final long y, final long z, final long a3) {
    long t2 = 0;

    //LAB_800e9040
    for(int i = 0; i < SomethingStructPtr_800d1a88.deref().count_0c.get(); i++) {
      final long a1_0 = SomethingStructPtr_800d1a88.deref().ptr_14.getPointer() + i * 0xcL;
      if(a3 != 0x1L || MEMORY.ref(1, a1_0).offset(0x1L).get() != 0) {
        //LAB_800e9078
        long a0_0 = SomethingStructPtr_800d1a88.deref().ptr_18.get() + MEMORY.ref(2, a1_0).offset(0x2L).get() * 0xcL;

        //LAB_800e90a0
        long v0 = 0x1L;
        for(int n = 0; n < MEMORY.ref(1, a1_0).get(); n++) {
          if((int)(MEMORY.ref(2, a0_0).offset(0x0L).getSigned() * x + MEMORY.ref(2, a0_0).offset(0x2L).getSigned() * z + MEMORY.ref(4, a0_0).offset(0x4L).get()) < 0) {
            //LAB_800e910c
            v0 = 0;
            break;
          }

          a0_0 += 0xcL;
        }

        //LAB_800e90f0
        if(v0 != 0) {
          _800cbe48.offset(t2 * 0x4L).setu(i);
          t2++;
        }
      }

      //LAB_800e9104
    }

    //LAB_800e9114
    if(t2 == 0) {
      return -0x1L;
    }

    if(t2 == 0x1L) {
      return _800cbe48.get();
    }

    //LAB_800e9134
    long t0 = 0x7fff_ffffL;
    long t3 = -0x1L;
    final long t6 = SomethingStructPtr_800d1a88.deref().normals_08.get();
    final long t5 = SomethingStructPtr_800d1a88.deref().ptr_14.getPointer();

    //LAB_800e9164
    long v1;
    for(int i = 0; i < t2; i++) {
      final long a3_0 = _800cbe48.offset(i * 0x4L).get();

      v1 = -MEMORY.ref(2, t6).offset(a3_0 * 0x8L).offset(0x0L).getSigned() * x - MEMORY.ref(2, t6).offset(a3_0 * 0x8L).offset(0x4L).getSigned() * z - MEMORY.ref(4, t5).offset(a3_0 * 0xcL).offset(0x8L).get();

      final short divisor = (short)MEMORY.ref(2, t6).offset(a3_0 * 0x8L).offset(0x2L).getSigned();

      if(divisor != 0) {
        v1 = (int)v1 / MEMORY.ref(2, t6).offset(a3_0 * 0x8L).offset(0x2L).getSigned();
      } else {
        v1 = -1;
      }

      v1 = v1 - (y - 0x14L);
      if((int)v1 > 0 && (int)v1 < (int)t0) {
        t3 = a3_0;
        t0 = v1;
      }

      //LAB_800e91ec
    }

    //LAB_800e91fc
    if(t0 != 0x7fff_ffffL) {
      v1 = t3;
    } else {
      //LAB_800e920c
      v1 = -0x1L;
    }

    //LAB_800e9210
    //LAB_800e9214
    return v1;
  }

  @Method(0x800e92dcL)
  public static long get3dAverageOfSomething(final int index, final SVECTOR out) {
    out.set((short)0, (short)0, (short)0);

    final SomethingStruct ss = SomethingStructPtr_800d1a88.deref();

    if(_800f7f14.get() == 0 || index < 0 || index >= ss.count_0c.get()) {
      //LAB_800e9318
      return 0;
    }

    //LAB_800e932c
    final SomethingStruct2 ss2 = ss.ptr_14.deref().get(index);
    final long t0 = ss.primitives_10.get() + ss2.ptr_04.get() + 0x6L;
    final int count = ss2.count_00.get();

    //LAB_800e937c
    for(int i = 0; i < count; i++) {
      out.add(ss.verts_04.deref().get((int)MEMORY.ref(2, t0).offset(i * 0x2L).get()));
    }

    //LAB_800e93e0
    out.div(count);
    return 0x1L;
  }

  /** TODO collision? */
  @Method(0x800e9430L) //TODO this is almost definitely wrong
  public static int FUN_800e9430(long a0, final int x, final int y, final int z, final SVECTOR playerMovement) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    final int s5;
    final int s6;
    long lo;
    final SVECTOR sp0x28 = new SVECTOR();
    long sp30 = 0; //TODO was uninitialized
    long sp34 = 0; //TODO was uninitialized
    long sp38 = 0; //TODO was uninitialized
    final long sp3c;

    if(smapLoadingStage_800cb430.get() != 0xcL) {
      return -1;
    }
    s3 = 0;

    if(playerMovement.getX() == 0 && playerMovement.getZ() == 0) {
      return -1;
    }

    //LAB_800e94a4
    if(playerMovement.getX() * playerMovement.getX() + playerMovement.getZ() * playerMovement.getZ() > 0x40L) {
      _800cbe30.setu(0xcL);
    } else {
      //LAB_800e94e4
      _800cbe30.setu(0x4L);
    }

    //LAB_800e94ec
    s6 = x + playerMovement.getX();
    sp3c = y;
    s5 = z + playerMovement.getZ();
    t6 = y - 20;
    t0 = 0;
    long t1;
    long t2 = _800cbe48.getAddress();

    //LAB_800e9538
    for(a3 = 0; a3 < SomethingStructPtr_800d1a88.deref().count_0c.get(); a3++) {
      if(SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a3).bool_01.get()) {
        a0 = SomethingStructPtr_800d1a88.deref().ptr_18.get() + SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a3)._02.get() * 0xcL;
        a1 = SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a3).count_00.get();

        //LAB_800e9594
        v0 = 0x1L;
        for(a2 = 0; a2 < a1; a2++) {
          if(MEMORY.ref(2, a0).offset(0x0L).getSigned() * x + MEMORY.ref(2, a0).offset(0x2L).getSigned() * z + MEMORY.ref(4, a0).offset(0x4L).getSigned() < 0) {
            //LAB_800e9604
            v0 = 0;
            break;
          }

          a0 += 0xcL;
        }

        //LAB_800e95e8
        if(v0 != 0) {
          MEMORY.ref(4, t2).offset(t0 * 0x4L).setu(a3);
          t0++;
        }
      }

      //LAB_800e95fc
    }

    //LAB_800e960c
    if(t0 == 0) {
      s4 = -0x1L;
    } else if(t0 == 0x1L) {
      s4 = _800cbe48.get();
    } else {
      //LAB_800e962c
      t1 = 0x7fff_ffffL;
      t2 = -0x1L;
      if((int)t0 > 0) {
        final SomethingStruct struct = SomethingStructPtr_800d1a88.deref();
        t4 = struct.normals_08.get();

        //LAB_800e965c
        for(int i = 0; i < t0; i++) {
          a2 = _800cbe48.offset(i * 0x4L).get();
          v1 = (-MEMORY.ref(2, t4).offset(a2 * 0x8L).offset(0x0L).getSigned() * x - MEMORY.ref(2, t4).offset(a2 * 0x8L).offset(0x4L).getSigned() * z - struct.ptr_14.deref().get((int)a2)._08.get()) / MEMORY.ref(2, t4).offset(a2 * 0x8L).offset(0x2L).getSigned() - t6;

          if((int)v1 > 0 && (int)v1 < (int)t1) {
            t2 = a2;
            t1 = v1;
          }

          //LAB_800e96e8
        }
      }

      //LAB_800e96f8
      if(t1 != 0x7fff_ffffL) {
        s4 = t2;
      } else {
        //LAB_800e9708
        s4 = -0x1L;
      }

      //LAB_800e970c
    }

    //LAB_800e9710
    if((int)s4 < 0) {
      s4 = FUN_800e8990(x, z);

      if(_800f7f14.get() != 0 && (int)s4 >= 0 && (int)s4 < SomethingStructPtr_800d1a88.deref().count_0c.get()) {
        v0 = 1;
      } else {
        v0 = 0;
      }

      //LAB_800e975c
      //LAB_800e9764
      sp0x28.setX((short)0);
      sp0x28.setY((short)0);
      sp0x28.setZ((short)0);

      if(v0 == 0) {
        //LAB_800e9774
        t0 = SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s4).ptr_04.get() + SomethingStructPtr_800d1a88.deref().primitives_10.get() + 0x6L;

        //LAB_800e97c4
        for(a2 = 0; a2 < SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s4).count_00.get(); a2++) {
          sp0x28.add(SomethingStructPtr_800d1a88.deref().verts_04.deref().get((int)MEMORY.ref(2, t0).offset(a2 * 0x2L).get()));
        }

        //LAB_800e9828
        sp0x28.div(SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s4).count_00.get());
      }

      //LAB_800e9870
      playerMovement.setX((short)(sp0x28.getX() - x));
      playerMovement.setZ((short)(sp0x28.getZ() - z));

      a1 = SomethingStructPtr_800d1a88.deref().normals_08.get() + s4 * 0x8L;
      playerMovement.setY((short)((-MEMORY.ref(2, a1).offset(0x0L).getSigned() * sp0x28.getX() - MEMORY.ref(2, a1).offset(0x4L).getSigned() * sp0x28.getZ() - SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s4)._08.get()) / MEMORY.ref(2, a1).offset(0x2L).getSigned()));
    } else {
      //LAB_800e990c
      t6 = sp3c - 0x14L;
      t0 = 0;

      //LAB_800e992c
      for(a3 = 0; a3 < SomethingStructPtr_800d1a88.deref().count_0c.get(); a3++) {
        if(SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a3).bool_01.get()) {
          a0 = SomethingStructPtr_800d1a88.deref().ptr_18.get() + SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a3)._02.get() * 0xcL;

          //LAB_800e9988
          v0 = 0x1L;
          for(a2 = 0; a2 < SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a3).count_00.get(); a2++) {
            if(MEMORY.ref(2, a0).offset(0x0L).getSigned() * s6 + MEMORY.ref(2, a0).offset(0x2L).getSigned() * s5 + MEMORY.ref(4, a0).offset(0x4L).getSigned() < 0) {
              //LAB_800e99f4
              v0 = 0;
              break;
            }

            a0 += 0xcL;
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
        if(t0 != 0x1L) {
          //LAB_800e9a1c
          t1 = 0x7fff_ffffL;
          t2 = -0x1L;

          //LAB_800e9a4c
          for(a3 = 0; a3 < t0; a3++) {
            a2 = _800cbe48.offset(a3 * 0x4L).get();

            a1 = SomethingStructPtr_800d1a88.deref().normals_08.get() + a2 * 0x8L;

            v1 = -MEMORY.ref(2, a1).offset(0x0L).getSigned() * s6 - MEMORY.ref(2, a1).offset(0x4L).getSigned() * s5 - SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)a2)._08.get();
            v0 = MEMORY.ref(2, a1).offset(0x2L).getSigned();

            v1 = (int)v1 / (int)v0 - t6;
            if((int)v1 > 0 && (int)v1 < (int)t1) {
              t2 = a2;
              t1 = v1;
            }

            //LAB_800e9ad4
          }

          //LAB_800e9ae4
          if(t1 != 0x7fff_ffffL) {
            v1 = t2;
          } else {
            //LAB_800e9af4
            v1 = -0x1L;
          }
        } else {
          v1 = _800cbe48.get();
        }

        //LAB_800e9af8
        s3 = v1;
      } else {
        s3 = -0x1L;
      }

      //LAB_800e9afc
      v0 = -0x1L;
      if((int)s3 >= 0) {
        v1 = SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s3).getAddress(); //TODO
        s0 = SomethingStructPtr_800d1a88.deref().ptr_18.get() + MEMORY.ref(2, v1).offset(0x2L).get() * 0xcL;

        //LAB_800e9b50
        for(s1 = 0; s1 < MEMORY.ref(1, v1).get(); s1++) {
          if(MEMORY.ref(4, s0).offset(0x8L).get() != 0) {
            if(Math.abs(MEMORY.ref(2, s0).offset(0x0L).getSigned() * s6 + MEMORY.ref(2, s0).offset(0x2L).getSigned() * s5 + MEMORY.ref(4, s0).offset(0x4L).getSigned() >> 10) < 0xaL) {
              v0 = s1;
              break;
            }
          }

          //LAB_800e9ba4
          s0 += 0xcL;
        }
      }

      //LAB_800e9bbc
      //LAB_800e9bc0
      if((int)s3 >= 0 && (int)v0 < 0) {
        a1 = SomethingStructPtr_800d1a88.deref().normals_08.get() + s3 * 0x8L;
        v1 = SomethingStructPtr_800d1a88.deref().ptr_14.getPointer() + s3 * 0xcL;

        a0 = -MEMORY.ref(2, a1).offset(0x0L).getSigned() * s6 - MEMORY.ref(2, a1).offset(0x4L).getSigned() * s5 - MEMORY.ref(4, v1).offset(0x8L).get();
        a0 = (int)a0 / MEMORY.ref(2, a1).offset(0x2L).getSigned();
        v0 = Math.abs(sp3c - a0);
        if((int)v0 < 0x32L) {
          a0 = SomethingStructPtr_800d1a88.deref().normals_08.get() + s3 * 0x8L;
          a1 = SomethingStructPtr_800d1a88.deref().ptr_14.getPointer() + s3 * 0xcL;

          v0 = -MEMORY.ref(2, a0).offset(0x0L).getSigned() * (x + playerMovement.getX()) - MEMORY.ref(2, a0).offset(0x4L).getSigned() * (z + playerMovement.getZ()) - MEMORY.ref(4, a1).offset(0x8L).get();

          //LAB_800e9e64
          playerMovement.setY((short)((int)v0 / MEMORY.ref(2, a0).offset(0x2L).getSigned()));

          //LAB_800ea390
          if(_800d1a8c.deref()._00.get() == 0) {
            _800d1a8c.deref()._00.set(0x1L);
            //LAB_800ea3b4
            _800d1a84.setu(ratan2(playerMovement.getX(), playerMovement.getZ()) + 0x800L & 0xfffL);
          }

          //LAB_800ea3e0
          return (int)s3;
        }
      }

      //LAB_800e9c58
      if((FUN_800e6730((int)s4) & 0x20L) != 0) {
        return -1;
      }

      t3 = SomethingStructPtr_800d1a88.deref().ptr_18.get();
      t1 = SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s4).count_00.get();

      //LAB_800e9ca0
      a1 = -0x1L;
      for(a2 = 1; a2 < 4; a2++) {
        t0 = x + playerMovement.getX() * a2;
        a3 = z + playerMovement.getZ() * a2;
        a0 = t3 + SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s4)._02.get() * 0xcL;

        //LAB_800e9ce8
        for(long a1_0 = 0; a1_0 < t1; a1_0++) {
          if(MEMORY.ref(4, a0).offset(0x8L).get() != 0) {
            v0 = MEMORY.ref(2, a0).offset(0x0L).getSigned() * t0 + MEMORY.ref(2, a0).offset(0x2L).getSigned() * a3 + MEMORY.ref(4, a0).offset(0x4L).getSigned() >> 10;
            if((int)v0 <= 0) {
              a1 = a1_0;
              break;
            }
          }

          //LAB_800e9d34
          a0 += 0xcL;
        }

        //LAB_800e9d44
        //LAB_800e9d48
        if((int)a1 >= 0) {
          break;
        }
      }

      if((int)a1 >= 0) {
        //LAB_800e9e78
        s2 = s4;

        //LAB_800e9e7c
        v1 = SomethingStructPtr_800d1a88.deref().ptr_18.get() + SomethingStructPtr_800d1a88.deref().ptr_14.deref().get((int)s2)._02.get() * 0xcL + a1 * 0xcL;
        s3 = ratan2(s5 - z, s6 - x);
        s0 = ratan2((int)-MEMORY.ref(2, v1).offset(0x0L).getSigned(), (int)MEMORY.ref(2, v1).offset(0x2L).getSigned());
        v1 = Math.abs(s3 - s0);
        if((int)v1 >= 0x801L) {
          v1 = 0x1000L - v1;
        }

        //LAB_800e9f38
        _800cbe68.setu(0);
        if((v1 - 0x341L & 0xffff_ffffL) > 0x17eL) { //TODO I don't understand this, but it's right
          if((int)v1 >= 0x401L) {
            _800cbe68.setu(0x1L);
            if((int)s0 > 0) {
              s0 = s0 - 0x800L;
            } else {
              //LAB_800e9f6c
              s0 = s0 + 0x800L;
            }
          }

          //LAB_800e9f70
          sp38 = s0;

          if(_800cbe38.deref()._00.get() == 0) {
            _800cbe38.deref()._00.set(0x1L);
          }

          v1 = s0 - s3;

          //LAB_800e9f98
          if(v1 < 0x400L || (int)v1 < -0x800L) {
            //LAB_800e9fb4
            v0 = 0x1L;
          } else {
            v0 = 0;
          }
        } else {
          v0 = -0x1L;
        }

        //LAB_800e9fbc
        if((int)v0 >= 0) {
          if(v0 == 0) {
            s3 = -0x40L;
          } else {
            s3 = 0x40L;
          }

          //LAB_800e9fd0
          s1 = 0x8L;

          sp38 = sp38 - s3;
          v0 = 0x800d_0000L;
          s4 = v0 - 0x41b8L;

          //LAB_800e9ff4
          do {
            sp38 = sp38 + s3;
            v0 = rcos(sp38);
            v1 = _800cbe30.get();

            t7 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
            v0 = (int)t7 >> 12;
            s0 = x + v0;
            v0 = rsin(sp38);
            v1 = _800cbe30.get();

            t7 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
            v0 = (int)t7 >> 12;
            s1 = s1 - 0x1L;
            t5 = z + v0;
            if((int)s1 <= 0) {
              break;
            }

            t6 = y - 20;
            t0 = 0;
            t1 = SomethingStructPtr_800d1a88.getPointer(); //TODO

            //LAB_800ea064
            for(a2 = 0; a2 < MEMORY.ref(4, t1).offset(0xcL).get(); a2++) {
              v1 = MEMORY.ref(4, t1).offset(0x14L).get();
              a1 = v1 + a2 * 0xcL;
              v0 = MEMORY.ref(1, a1).offset(0x1L).get();

              if(v0 != 0) {
                a0 = MEMORY.ref(4, t1).offset(0x18L).get() + MEMORY.ref(2, a1).offset(0x2L).get() * 0xcL;
                a1 = MEMORY.ref(1, a1).offset(0x0L).get();

                //LAB_800ea0c4
                a3 = 0;
                do {
                  if((int)a3 >= (int)a1) {
                    v0 = 0x1L;
                    break;
                  }

                  v0 = MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0 + MEMORY.ref(2, a0).offset(0x2L).getSigned() * t5 + MEMORY.ref(4, a0).offset(0x4L).getSigned();
                  if((int)v0 < 0) {
                    //LAB_800ea130
                    v0 = 0;
                    break;
                  }
                  a3 = a3 + 0x1L;
                  a0 = a0 + 0xcL;
                } while(true);

                //LAB_800ea114
                if(v0 != 0) {
                  v0 = s4 + t0 * 0x4L;
                  MEMORY.ref(4, v0).offset(0x0L).setu(a2);
                  t0 = t0 + 0x1L;
                }
              }

              //LAB_800ea128
            }

            //LAB_800ea138
            if(t0 != 0) {
              if(t0 == 0x1L) {
                t7 = 0x800d_0000L;
                a0 = MEMORY.ref(4, t7).offset(-0x41b8L).get();
              } else {
                //LAB_800ea158
                t1 = 0x7fff_ffffL;
                t2 = -0x1L;

                v0 = SomethingStructPtr_800d1a88.getPointer(); //TODO

                t4 = MEMORY.ref(4, v0).offset(0x8L).get();
                t3 = MEMORY.ref(4, v0).offset(0x14L).get();

                //LAB_800ea17c
                for(a2 = 0; a2 < (int)t0; a2++) {
                  v0 = s4 + a2 * 0x4L;
                  a3 = MEMORY.ref(4, v0).offset(0x0L).get();

                  a1 = t4 + a3 * 0x8L;
                  a0 = t3 + a3 * 0xcL;

                  v1 = -MEMORY.ref(2, a1).offset(0x0L).getSigned() * s0 - MEMORY.ref(2, a1).offset(0x4L).getSigned() * t5 - MEMORY.ref(4, a0).offset(0x8L).get();
                  v1 = (int)v1 / MEMORY.ref(2, a1).offset(0x2L).getSigned();

                  v1 = v1 - t6;
                  if((int)v1 > 0 && (int)v1 < (int)t1) {
                    t2 = a3;
                    t1 = v1;
                  }

                  //LAB_800ea204
                }

                //LAB_800ea214
                if(t1 != 0x7fff_ffffL) {
                  a0 = t2;
                } else {
                  //LAB_800ea224
                  a0 = -0x1L;
                }
              }

              //LAB_800ea228
              s2 = a0;
            } else {
              s2 = -0x1L;
            }

            //LAB_800ea22c
          } while((int)s2 < 0);

          //LAB_800ea234
          s3 = s2;
          if((int)s2 >= 0) {
            sp30 = s0;
            sp34 = t5;
          }
        } else {
          s3 = -0x1L;
        }

        //LAB_800ea254
        if((int)s3 < 0) {
          return -1;
        }

        s2 = 0x800d_0000L;
        a2 = sp30;
        a3 = sp34;
        v1 = MEMORY.ref(4, s2).offset(0x1a88L).get();
        s1 = s3 << 3;
        a1 = MEMORY.ref(4, v1).offset(0x8L).get();

        a1 = s1 + a1;
        v0 = s3 << 1;
        v0 = v0 + s3;
        s0 = v0 << 2;
        v1 = MEMORY.ref(4, v1).offset(0x14L).get();

        v1 = s0 + v1;
        a0 = MEMORY.ref(2, a1).offset(0x0L).getSigned();

        a0 = -a0;
        lo = (long)(int)a0 * (int)a2 & 0xffff_ffffL;
        a0 = lo;
        v0 = MEMORY.ref(2, a1).offset(0x4L).getSigned();

        lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
        t0 = lo;
        a0 = a0 - t0;
        v0 = MEMORY.ref(4, v1).offset(0x8L).get();

        a0 = a0 - v0;
        v0 = MEMORY.ref(2, a1).offset(0x2L).getSigned();

        lo = (int)a0 / (int)v0;
        a0 = lo;
        t7 = y;
        a0 = t7 - a0;
        v0 = Math.abs(a0);
        if((int)v0 >= 0x32L) {
          return -1;
        }

        v0 = MEMORY.ref(4, s2).offset(0x1a88L).get();

        a0 = s1 + MEMORY.ref(4, v0).offset(0x8L).get();
        a1 = s0 + MEMORY.ref(4, v0).offset(0x14L).get();
        v0 = -MEMORY.ref(2, a0).offset(0x0L).getSigned() * sp30 - MEMORY.ref(2, a0).offset(0x4L).getSigned() * sp34 - MEMORY.ref(4, a1).offset(0x8L).get();
        v0 = (int)v0 / MEMORY.ref(2, a0).offset(0x2L).getSigned();

        playerMovement.setY((short)v0);
        v0 = sp30 - x;
        playerMovement.setX((short)v0);
        v0 = sp34 - z;
        playerMovement.setZ((short)v0);
      } else {
        if((int)s3 < 0) {
          return -1;
        }

        s2 = 0x800d_0000L;
        v1 = MEMORY.ref(4, s2).offset(0x1a88L).get();

        s1 = s3 * 0x8L;
        a1 = MEMORY.ref(4, v1).offset(0x8L).get() + s1;

        s0 = s3 * 0xcL;
        v1 = MEMORY.ref(4, v1).offset(0x14L).get() + s0;

        a0 = -MEMORY.ref(2, a1).offset(0x0L).getSigned() * s6 - MEMORY.ref(2, a1).offset(0x4L).getSigned() * s5 - MEMORY.ref(4, v1).offset(0x8L).get();

        v0 = Math.abs(sp3c - (int)a0 / MEMORY.ref(2, a1).offset(0x2L).getSigned());
        if((int)v0 >= 0x32L) {
          return -1;
        }

        //LAB_800e9df4
        a0 = s1 + SomethingStructPtr_800d1a88.deref().normals_08.get();
        a1 = s0 + SomethingStructPtr_800d1a88.deref().ptr_14.getPointer();

        v0 = -MEMORY.ref(2, a0).offset(0x0L).getSigned() * (x + playerMovement.getX()) - MEMORY.ref(2, a0).offset(0x4L).getSigned() * (z + playerMovement.getZ()) - MEMORY.ref(4, a1).offset(0x8L).get();

        //LAB_800e9e64
        playerMovement.setY((short)((int)v0 / MEMORY.ref(2, a0).offset(0x2L).getSigned()));
      }
    }

    //LAB_800ea390
    if(_800d1a8c.deref()._00.get() == 0) {
      _800d1a8c.deref()._00.set(0x1L);
      //LAB_800ea3b4
      _800d1a84.setu(ratan2(playerMovement.getX(), playerMovement.getZ()) + 0x800L & 0xfffL);
    }

    //LAB_800ea3e0
    return (int)s3;
  }

  @Method(0x800ea4c8L)
  public static short FUN_800ea4c8(final short a0) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;

    _800d1a78.subu(0x1L);

    if((int)_800d1a78.get() > 0) {
      _800d1a84.setu(_800d1a80);

      if(_800d1a8c.deref()._00.get() == 0) {
        _800d1a8c.deref()._00.set(0x1L);
      }
    }

    //LAB_800ea534
    //LAB_800ea538
    if((int)_800c6ae0.get() < 0x401L) {
      v0 = 0x1L;
    } else if(_800d1a8c.deref()._00.get() != 0) {
      v0 = 0;
    } else {
      v0 = 0x1L;
      _800d1a8c.deref()._00.set(0x1L);
    }

    //LAB_800ea570
    if(v0 != 0 || _800d1a7c.get() != 0) {
      //LAB_800ea6d0
      //LAB_800ea6d4
      _800d1a7c.setu(0);
      return a0;
    }

    s1 = _800c6ae0.get() - 0x1L;
    if((int)s1 >= 0) {
      v0 = s1;
    } else {
      v0 = _800c6ae0.get() + 0x2L;
    }

    //LAB_800ea5a0
    v0 = (int)v0 >> 2;
    v0 = v0 << 2;
    s1 = s1 - v0;

    if((int)_800c6ae0.get() >= 0) {
      v0 = _800c6ae0.get();
    } else {
      v0 = _800c6ae0.get() + 0x3L;
    }

    //LAB_800ea5c4
    s2 = (int)v0 >> 2;
    v0 = s2 << 2;
    s2 = _800c6ae0.get() - v0;
    s0 = _800f7f6c.offset(s1 * 0x2L).getSigned() - _800d1a84.get();

    if((int)Math.abs(s0) >= 0x801L) {
      _800cbda4.setu((int)s0 > 0 ? 1 : 0);
      s0 = 0x1000L - Math.abs(s0);
    } else {
      //LAB_800ea628
      _800cbda4.setu((int)s0 < 0x1L ? 1 : 0);
      s0 = Math.abs(s0);
    }

    //LAB_800ea63c
    if((int)s0 >= 0x201L || (int)_800d1a78.get() > 0) {
      //LAB_800ea658
      if((int)s0 < 0) {
        v0 = s0 + 0x3L;
      } else {
        v0 = s0;
      }

      //LAB_800ea664
      s0 = (int)v0 >> 2;
    }

    //LAB_800ea66c
    v1 = _800f7f6c.offset(s1 * 0x2L).getSigned();

    if(_800cbda4.get() == 0) {
      v0 = v1 - s0;
    } else {
      //LAB_800ea6a0
      v0 = v1 + s0;
    }

    //LAB_800ea6a4
    _800cbda0.setu(v0); //2b
    v0 = 0x800f_0000L;
    v0 = v0 + 0x7f6cL;
    v1 = s2 << 1;
    v1 = v1 + v0;
    v0 = 0x800d_0000L;
    v0 = MEMORY.ref(2, v0).offset(-0x4260L).get();

    MEMORY.ref(2, v1).offset(0x0L).setu(v0);

    //LAB_800ea6dc
    return (short)v0;
  }

  @Method(0x800ea84cL)
  public static void FUN_800ea84c(final MediumStruct a0) {
    if(isScriptLoaded(0)) {
      if(a0._44.get() != 0) {
        index_80052c38.set(scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.get());

        //LAB_800ea8d4
        for(int i = 0; i < a0.count_40.get(); i++) {
          if(index_80052c38.get() == a0.arr_00.get(i).get()) {
            a0._44.set(0);
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
      index_80052c38.set(scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.get());
    }
  }

  @Method(0x800ea96cL)
  public static void FUN_800ea96c(final MediumStruct a0) {
    // no-op
  }

  @Method(0x800ea974L)
  public static MediumStruct FUN_800ea974(final long a0) {
    if((int)a0 < 0) {
      _800d1a90.callback_48.set(MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800ea96c", MediumStruct.class), ConsumerRef::new));
    } else {
      //LAB_800ea9a4
      fillMemory(_800d1a90.getAddress(), 0, 0x4cL);

      final long a3 = _800f7f74.getAddress();
      final MediumStruct a2 = _800d1a90;

      //LAB_800ea9d8
      for(int i = 0; i < _800f9374.get(); i++) {
        if(a0 != 0) {
          if(MEMORY.ref(2, a3).offset(i * 0x14L).offset(0x4L).get() == a0) {
            a2.arr_00.get((int)a2.count_40.get()).set(MEMORY.ref(2, a3).offset(i * 0x14L).offset(0x6L).get());
            a2.count_40.incr();
          }
        }

        //LAB_800eaa20
      }

      //LAB_800eaa30
      if(_800d1a90.count_40.get() != 0) {
        _800d1a90.callback_48.set(MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800ea84c", MediumStruct.class), ConsumerRef::new));
        _800d1a90._44.set(0x1L);
      } else {
        //LAB_800eaa5c
        _800d1a90.callback_48.set(MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800ea90c", MediumStruct.class), ConsumerRef::new));
      }

      //LAB_800eaa6c
    }

    //LAB_800eaa74
    return _800d1a90;
  }

  @Method(0x800ed5b0L)
  public static void executeSmapPregameLoadingStage() {
    switch((int)pregameLoadingStage_800bb10c.get()) {
      case 0x0:
        creditsLoaded_800d1cb8.setu(0);
        loadDrgnBinFile(0, 5721, 0, getMethodAddress(SMap.class, "loadCreditsMrg", long.class, long.class, long.class), _800bf0dc.get(), 0x4L);

        //LAB_800ed644
        for(int s2 = 0; s2 < 3; s2++) {
          final RECT rect = rectArray3_800f96f4.get(s2);
          final long dest = addToLinkedListTail(rect.w.get() * rect.h.get() * 2);
          textureDataPtrArray3_800d4ba0.get(s2).set(dest);
          StoreImage(rectArray3_800f96f4.get(s2), dest);
        }

        SetDispMask(0);

        ClearImage(new RECT((short)0, (short)0, (short)1023, (short)511), (byte)0, (byte)0, (byte)0);

        DrawSync(0);

        final long a1;
        if(_800f970c.offset(_800bf0dc.get() * 16).get() == 0) {
          //LAB_800ed6f8
          a1 = 0;
        } else {
          a1 = 0x4L;
        }

        //LAB_800ed700
        setWidthAndFlags(_800f9718.offset(_800bf0dc.get() * 16).get(), a1);

        pregameLoadingStage_800bb10c.setu(0x1L);
        break;

      case 0x1:
        if(creditsLoaded_800d1cb8.get() != 0 && fileCount_8004ddc8.get() == 0) {
          pregameLoadingStage_800bb10c.setu(0x2L);
        }

        break;

      case 0x2:
        vsyncMode_8007a3b8.setu(0x4L);
        FUN_800ed8d0(_800bf0dc.get());

        _800bd808.setu(-0x1L);
        pregameLoadingStage_800bb10c.setu(0x3L);
        break;

      case 0x3:
        if(_800bf0d8.get() == 0x5L) {
          pregameLoadingStage_800bb10c.setu(0x4L);
        }

        break;

      case 0x4:
        vsyncMode_8007a3b8.setu(0x2L);
        pregameLoadingStage_800bb10c.setu(0);
        mainCallbackIndexOnceLoaded_8004dd24.setu(_800bf0ec);
        break;
    }
  }

  @Method(0x800ed7e4L)
  public static long FUN_800ed7e4() {
    if(_800bf0b4.get() == 0) {
      return 0;
    }

    if(_800bf0d8.get() != 0x4L) {
      //LAB_800ed818
      return 0;
    }

    //LAB_800ed820
    _800bf0d8.setu(0x5L);
    fileLoadingCallbackIndex_8004ddc4.setu(0x19L);

    ClearImage(new RECT((short)0, (short)0, (short)640, (short)511), (byte)0, (byte)0, (byte)0);
    DrawSync(0);

    //LAB_800ed87c
    for(int s2 = 0; s2 < 3; s2++) {
      LoadImage(rectArray3_800f96f4.get(s2), textureDataPtrArray3_800d4ba0.get(s2).get());
      DrawSync(0);
      removeFromLinkedList(textureDataPtrArray3_800d4ba0.get(s2).get());
    }

    //LAB_800ed8b8
    return 0x1L;
  }

  @Method(0x800ed8d0L)
  public static void FUN_800ed8d0(final long a0) {
    if(_800bf0d8.get() != 0) {
      return;
    }

    _800bf0dc.setu(a0);

    if(_800bf0b4.get() == 0) {
      loadAndRunOverlay(4, getMethodAddress(SMap.class, "FUN_800edc50", long.class), 0);
    } else {
      //LAB_800ed91c
      _800bf0d8.setu(0x1L);
    }

    //LAB_800ed924
  }

  @Method(0x800ed960L)
  public static long loadFmv() {
    // TODO skip FMVs

    _800bf0d8.setu(0x2L);
    fileLoadingCallbackIndex_8004ddc4.set(0x16L);

    //LAB_800ed9d4
    return 0x1L;
  }

  @Method(0x800ed9e4L)
  public static long playFmv() {
    //TODO skip FMV

    //LAB_800eda50
    _800bf0d8.setu(0x3L);
    fileLoadingCallbackIndex_8004ddc4.setu(0x17L);
    scriptStartEffect(0x1L, 0x1L);
    ClearImage(new RECT((short)0, (short)0, (short)1023, (short)511), (byte)0, (byte)0, (byte)0);
    DrawSync(0);
    setWidthAndFlags(640L, 0);

    //LAB_800edab4
    return 0x1L;
  }

  @Method(0x800edac4L)
  public static long stopFmv() {
    //TODO skip FMV
    /*
    if(_800bf0b4.get() == 0) {
      //LAB_800edb30
      return 0;
    }

    if(_800bf0d8.get() != 0x3L) {
      return 0;
    }

    SStrm.stopFmv(_800f970c.offset(_800bf0dc.get() * 16).getAddress());
    */
    FUN_80012bb4();

    fileLoadingCallbackIndex_8004ddc4.setu(0x18L);
    _800bf0d8.setu(0x4L);

    //LAB_800edb34
    return 0x1L;
  }

  @Method(0x800edb44L)
  public static long FUN_800edb44() {
    if(_800bf0b4.get() == 0) {
      return 0;
    }

    //LAB_800edb60
    if(_800bf0d8.get() == 0x5L) {
      _800bf0d8.setu(0);
      _800bf0b4.setu(0);
      fileLoadingCallbackIndex_8004ddc4.setu(0);
      return 0x1L;
    }

    //LAB_800edb84
    return 0;
  }

  @Method(0x800edb8cL)
  public static void FUN_800edb8c() {
    assert false;
  }

  @Method(0x800edc50L)
  public static void FUN_800edc50(final long a0) {
    if(_800bf0d8.get() >= 0x3L) {
      return;
    }

    _800bf0b4.setu(0x1L);
    _800bf0d8.setu(0x1L);
  }

  @Method(0x800edc7cL)
  public static void loadCreditsMrg(final long address, final long fileSize, final long fileIndex) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    if(fileIndex > mrg.count.get()) {
      return;
    }

    final MrgEntry entry = mrg.entries.get((int)fileIndex);

    if(entry.size.get() == 0) {
      return;
    }

    long a3 = _800d1cc0.getAddress();
    long a1 = mrg.getFile((int)fileIndex);

    //LAB_800edcbc
    for(int i = 2999; i >= 0; i--) {
      MEMORY.ref(4, a3).setu(MEMORY.ref(4, a1));
      a1 += 0x4L;
      a3 += 0x4L;
    }

    removeFromLinkedList(address);
    creditsLoaded_800d1cb8.setu(0x1L);

    //LAB_800edcf0
  }

  @Method(0x800edd00L)
  public static void loadMatrixFromFile(final long a0, final MATRIX a1) {
    final long[] sp = new long[12];

    //LAB_800edd14
    for(int i = 0; i < 6; i++) {
      sp[i * 2    ] = MEMORY.ref(2, a0).offset(i * 0x4L).get();
      sp[i * 2 + 1] = MEMORY.ref(2, a0).offset(i * 0x4L).offset(0x2L).getSigned();
    }

    //LAB_800edd5c
    for(int i = 0; i < 3; i++) {
      a1.set(i, 0, (short)sp[i * 3    ]);
      a1.set(i, 1, (short)sp[i * 3 + 1]);
      a1.set(i, 2, (short)sp[i * 3 + 2]);
    }

    a1.transfer.setX((int)sp[ 9]);
    a1.transfer.setY((int)sp[10]);
    a1.transfer.setZ((int)sp[11]);
  }

  @Method(0x800eddb4L)
  public static void FUN_800eddb4() {
    final RECT sp20 = new RECT().set(_800d6b48);

    if(_800c6870.getSigned() == -0x1L) {
      _800f9e5a.setu(-0x1L);
    }

    //LAB_800ede14
    switch((short)(_800f9e5a.get() + 0x1L)) {
      case 0x0:
        if(_800d4bd0.get() != 0) {
          removeFromLinkedList(_800d4bd0.get());
        }

        //LAB_800ee19c
        if(_800d4bd4.get() != 0) {
          removeFromLinkedList(_800d4bd4.get());
        }

        //LAB_800ee1b8
        FUN_80020fe0(bigStruct_800d4bf8);
        removeFromLinkedList(_800d4be8.get());
        _800f9e5a.setu(0);

        //LAB_800ee1e4
        DrawSync(0);
        break;

      case 0x1:
        _800d4bd0.setu(0);
        _800d4bd4.setu(0);

        if(submapCut_80052c30.get() == 0x2a1L) {
          _800d4bd0.setu(addToLinkedListTail(0xb0L));
          _800d4bd4.setu(addToLinkedListTail(0x20L));

          _800d4bd0.deref(2).offset(0x0L).setu(0);
          _800d4bd0.deref(2).offset(0x2L).setu(0);
          _800d4bd0.deref(2).offset(0x4L).setu(0);
          _800d4bd0.deref(2).offset(0x6L).setu(0);
          _800d4bd0.deref(2).offset(0x8L).setu(0);
          _800d4bd0.deref(4).offset(0xcL).setu(0);
        }

        //LAB_800edeb4
        _800d4bdc.setu(0);
        drgn0_mrg_80428_loaded_800d4be0.setu(0);
        _800d4be4.setu(0);
        _800d4be8.setu(0);
        drgn0_mrg_80428_address_800d4bec.setu(0);
        _800d4bf0.setu(0);

        final int fileIndex = smapFileIndices_800f982c.get(submapCut_80052c30.get()).get();
        if(fileIndex != 0) {
          loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(SMap.class, "FUN_800eeddc", long.class, long.class, long.class), 0, 0x2L);
          loadDrgnBinFile(0, fileIndex + 1, 0, getMethodAddress(SMap.class, "FUN_800eeddc", long.class, long.class, long.class), 0x1L, 0x4L);

          if(submapCut_80052c30.get() == 0x2a1) {
            loadDrgnBinFile(0, 7610, 0, getMethodAddress(SMap.class, "FUN_800eeddc", long.class, long.class, long.class), 0x2L, 0x4L);
          }
        }

        _800f9e5a.addu(0x1L);
        break;

      case 0x2:
        if(_800d4bdc.get() == 0x1L && drgn0_mrg_80428_loaded_800d4be0.get() == 0x1L) {
          final TimHeader tim = parseTimHeader(drgn0_mrg_80428_address_800d4bec.deref(4).offset(drgn0_mrg_80428_address_800d4bec.deref(4).offset(0x8L)).offset(0x4L));
          LoadImage(new RECT((short)1008, (short)256, tim.imageRect.w.get(), tim.imageRect.h.get()), tim.imageAddress.get());
          loadMatrixFromFile(drgn0_mrg_80428_address_800d4bec.deref(4).offset(drgn0_mrg_80428_address_800d4bec.deref(4).offset(0x10L)).getAddress(), matrix_800d4bb0);
          _800f9e5a.addu(0x1L);
          removeFromLinkedList(drgn0_mrg_80428_address_800d4bec.get());
          _800c686c.setu(0x1L);
          DrawSync(0);
        }

        break;

      case 0x3:
        if(submapCut_80052c30.get() == 0x2a1L) {
          if(_800d4be4.get() != 0x1L) {
            break;
          }

          FUN_800f4244(_800d4bf0.get(), _800f9e5c.getAddress(), _800f9e5e.getAddress(), TexPageTrans.B_PLUS_F);
          DrawSync(0);
          StoreImage(sp20, _800d4bd4.get());
          removeFromLinkedList(_800d4bf0.get());
        }

        _800f9e5a.addu(0x1L);
        break;

      case 0x4:
        bigStruct_800d4bf8.ub_9d.set(0x91);

        //TODO file types
        FUN_80020a00(bigStruct_800d4bf8, _800d4be8.deref(4).offset(_800d4be8.deref(4).offset(0x08L)).cast(ExtendedTmd::new), _800d4be8.deref(4).offset(_800d4be8.deref(4).offset(0x10L)).cast(TmdAnimationFile::new));

        if(submapCut_80052c30.get() == 0x2a1L) {
          FUN_800eef6c(sp20, _800d4bd4.get(), _800d4bd0.get());
        }

        //LAB_800ee10c
        //LAB_800ee110
        _800f9e5a.addu(0x1L);
        break;

      case 0x5:
        FUN_800eece0(matrix_800d4bb0);

        if(_800d4bd0.get() != 0 && _800d4bd4.get() != 0) {
          FUN_800ee9e0(sp20, _800d4bd4.get(), _800d4bd0.get(), _800f9e5c.getAddress(), _800f9e5e.getAddress());
          syncAndLoadImage(sp20, _800d4bd4.get());
        }

        break;
    }

    //caseD_6
  }

  @Method(0x800ee20cL)
  public static void FUN_800ee20c() {
    long v0;
    long v1;
    long a0;
    final long a1;
    long s0;
    long s1;

    v0 = 0x8010_0000L;
    v1 = MEMORY.ref(4, v0).offset(-0x6154L).get();
    if((int)v1 == -0x1L) {
      v0 = 0x8010_0000L;
      MEMORY.ref(4, v0).offset(-0x619cL).setu(v1);
    }

    //LAB_800ee234
    v0 = 0x8010_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0x619cL).getSigned();

    switch((int)v0) {
      case 0 -> {
        a0 = 0x3cL;
        v0 = addToLinkedListTail(a0);
        a1 = 0x8010_0000L;
        v1 = MEMORY.ref(4, a1).offset(-0x619cL).get();
        a0 = 0x800d_0000L;
        MEMORY.ref(4, a0).offset(0x4bd8L).setu(v0);
        MEMORY.ref(4, v0).offset(0x38L).setu(0);
        v1 = v1 + 0x1L;
        MEMORY.ref(4, a1).offset(-0x619cL).setu(v1);
      }

      case 1 -> {
        v0 = 0x800d_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x4bd8L).get();
        v0 = FUN_800ef034(a0);
        if(v0 != 0) {
          v1 = 0x8010_0000L;

          //LAB_800ee2fc
          v0 = MEMORY.ref(4, v1).offset(-0x619cL).get();
          v0 = v0 + 0x1L;
          MEMORY.ref(4, v1).offset(-0x619cL).setu(v0);
        }
      }

      case 2 -> {
        v0 = 0x800d_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x4bd8L).get();
        s0 = MEMORY.ref(4, v0).offset(0x38L).get();
        s1 = 0;
        if(s0 != 0) {
          //LAB_800ee2d8
          do {
            FUN_800ee558(s0);
            s0 = MEMORY.ref(4, s0).offset(0x38L).get();
            s1 = s1 + 0x1L;
          } while(s0 != 0);
        }

        //LAB_800ee2f0
        if((int)s1 >= 0x100L) {
          v1 = 0x8010_0000L;

          //LAB_800ee2fc
          v0 = MEMORY.ref(4, v1).offset(-0x619cL).get();
          v0 = v0 + 0x1L;
          MEMORY.ref(4, v1).offset(-0x619cL).setu(v0);
        }
      }

      case 3 -> {
        v0 = 0x800d_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x4bd8L).get();
        FUN_800ee368(a0);
      }

      case -1 -> {
        s0 = 0x8010_0000L;
        v0 = MEMORY.ref(2, s0).offset(-0x61a0L).getSigned();
        if(v0 != 0) {
          v0 = 0x800d_0000L;
          a0 = MEMORY.ref(4, v0).offset(0x4bd8L).get();
          FUN_800ef090(a0);
        }

        //LAB_800ee348
        v0 = 0x8010_0000L;
        MEMORY.ref(2, s0).offset(-0x61a0L).setu(0);
        MEMORY.ref(4, v0).offset(-0x619cL).setu(0);
      }
    }

    //LAB_800ee354
  }

  @Method(0x800ee368L)
  public static void FUN_800ee368(final long a0) {
    assert false;
  }

  @Method(0x800ee558L)
  public static void FUN_800ee558(final long a0) {
    assert false;
  }

  @Method(0x800ee9e0L)
  public static void FUN_800ee9e0(final RECT a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
  }

  @Method(0x800eece0L)
  public static void FUN_800eece0(final MATRIX matrix) {
    bigStruct_800d4bf8.coord2_14.coord.transfer.setX(0);
    bigStruct_800d4bf8.coord2_14.coord.transfer.setY(0);
    bigStruct_800d4bf8.coord2_14.coord.transfer.setZ(0);

    bigStruct_800d4bf8.coord2Param_64.rotate.setX((short)0);
    bigStruct_800d4bf8.coord2Param_64.rotate.setY((short)0);
    bigStruct_800d4bf8.coord2Param_64.rotate.setZ((short)0);

    FUN_800214bc(bigStruct_800d4bf8);
    FUN_80020b98(bigStruct_800d4bf8);
    FUN_800eee48(bigStruct_800d4bf8, matrix);
  }

  @Method(0x800eed44L)
  public static long FUN_800eed44(final long a0) {
    final long v0 = addToLinkedListHead(0x3c);
    MEMORY.ref(4, v0).offset(0x38L).setu(MEMORY.ref(4, a0).offset(0x38L).get());
    MEMORY.ref(4, a0).offset(0x38L).setu(v0);
    return v0;
  }

  @Method(0x800eed84L)
  public static void FUN_800eed84(final long a0) {
    if(MEMORY.ref(4, a0).offset(0x38L).get() != 0) {
      //LAB_800eeda8
      long s0;
      do {
        final long a0_0 = MEMORY.ref(4, a0).offset(0x38L).get();
        s0 = MEMORY.ref(4, a0_0).offset(0x38L).get();
        removeFromLinkedList(a0_0);
        MEMORY.ref(4, a0).offset(0x38L).setu(s0);
      } while(s0 != 0);
    }

    //LAB_800eedc8
  }

  @Method(0x800eeddcL)
  public static void FUN_800eeddc(final long address, final long fileSize, final long a2) {
    if(a2 == 0) {
      _800d4bdc.setu(0x1L);
      _800d4be8.setu(address);
    } else if(a2 == 1) {
      drgn0_mrg_80428_loaded_800d4be0.setu(0x1L);
      drgn0_mrg_80428_address_800d4bec.setu(address);
    } else if(a2 == 2) {
      _800d4be4.setu(0x1L);
      _800d4bf0.setu(address);
    }
  }

  @Method(0x800eee48L)
  public static void FUN_800eee48(final BigStruct a0, final MATRIX matrix) {
    zOffset_1f8003e8.set(a0.zOffset_a0.get());
    _1f8003ec.setu(a0.ui_108.get());

    final MATRIX lw = new MATRIX();

    //LAB_800eee94
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      GsGetLw(a0.ObjTable_0c.top.deref().get(0).coord2_04.deref(), lw);
      GsSetLightMatrix(lw);

      PushMatrix();
      CPU.CTC2(matrix.getPacked(0), 0); //
      CPU.CTC2(matrix.getPacked(2), 1); //
      CPU.CTC2(matrix.getPacked(4), 2); // Rotation matrix
      CPU.CTC2(matrix.getPacked(6), 3); //
      CPU.CTC2(matrix.getPacked(8), 4); //

      CPU.CTC2(matrix.transfer.getX(), 5); //
      CPU.CTC2(matrix.transfer.getY(), 6); // Translation vector
      CPU.CTC2(matrix.transfer.getZ(), 7); //
      renderDobj2(a0.ObjTable_0c.top.deref().get(i));
      PopMatrix();
    }

    //LAB_800eef0c
  }

  @Method(0x800eef2cL)
  public static void syncAndLoadImage(final RECT imageRect, final long imageAddress) {
    DrawSync(0);
    LoadImage(imageRect, imageAddress);
  }

  @Method(0x800eef6cL)
  public static void FUN_800eef6c(final RECT imageRect, final long imageAddress, final long a2) {
    //LAB_800eef94
    for(int i = 0; i < 16; i++) {
      final long v1 = MEMORY.ref(2, imageAddress).offset(i * 0x2L).get();
      final long v0;
      if((v1 & 0x8000L) != 0) {
        v0 = v1 & 0x7fffL;
      } else {
        v0 = v1;
      }

      //LAB_800eefac
      MEMORY.ref(2, a2).offset(i * 0x2L).offset(0x90L).setu(v0 & 0x1fL);
      MEMORY.ref(4, a2).offset(i * 0x4L).offset(0x50L).setu(0);
      MEMORY.ref(2, imageAddress).offset(i * 0x2L).setu(0x8000L);
      MEMORY.ref(4, a2).offset(i * 0x4L).offset(0x10L).setu((MEMORY.ref(2, a2).offset(i * 0x2L).offset(0x90L).get() << 16) / 60);
    }

    DrawSync(0);
    LoadImage(imageRect, imageAddress);
  }

  @Method(0x800ef034L)
  public static long FUN_800ef034(final long a0) {
    assert false;
    return 0;
  }

  @Method(0x800ef090L)
  public static void FUN_800ef090(final long a0) {
    assert false;
  }

  @Method(0x800ef0f8L)
  public static void FUN_800ef0f8(final BigStruct struct, final BigSubStruct a1) {
    if(a1._1e.getX() != struct.coord2_14.coord.transfer.getX() || a1._1e.getY() != struct.coord2_14.coord.transfer.getY() || a1._1e.getZ() != struct.coord2_14.coord.transfer.getZ()) {
      //LAB_800ef154
      if(a1._04.get() != 0) {
        if(a1._00.get() % a1._30.get() == 0) {
          final Struct20 a0 = FUN_800f03c0(_800d4ec0);
          a0._00.set(0);
          a0._18.set(a1._38.get());

          final int v0 = a1._28.get();
          if(v0 < 0) {
            a0.scale_08.set(-v0 << 12);
            a0._04.set(-a0._04.get() / 20);
          } else if(v0 > 0) {
            //LAB_800ef1e0
            a0.scale_08.set(0);
            a0._04.set((v0 << 12) / 20);
          } else {
            //LAB_800ef214
            a0.scale_08.set(0);
            a0._04.set(0);
          }

          //LAB_800ef21c
          a0.transfer.set(struct.coord2_14.coord.transfer);
        }
      }

      //LAB_800ef240
      if(a1._08.get() != 0) {
        if(a1._00.get() % a1._34.get() == 0) {
          final SVECTOR sp0x28 = new SVECTOR().set(_800d6b7c.get( 0));
          final SVECTOR sp0x30 = new SVECTOR().set(_800d6b7c.get( 1));
          final SVECTOR sp0x38 = new SVECTOR().set(_800d6b7c.get( 2));
          final SVECTOR sp0x40 = new SVECTOR().set(_800d6b7c.get( 3));
          final SVECTOR sp0x48 = new SVECTOR().set(_800d6b7c.get( 4));
          final SVECTOR sp0x50 = new SVECTOR().set(_800d6b7c.get( 5));
          final SVECTOR sp0x58 = new SVECTOR().set(_800d6b7c.get( 6));
          final SVECTOR sp0x60 = new SVECTOR().set(_800d6b7c.get( 7));
          final SVECTOR sp0x68 = new SVECTOR().set(_800d6b7c.get( 8));
          final SVECTOR sp0x70 = new SVECTOR().set(_800d6b7c.get( 9));
          final SVECTOR sp0x78 = new SVECTOR().set(_800d6b7c.get(10));
          final SVECTOR sp0x80 = new SVECTOR().set(_800d6b7c.get(11));

          //LAB_800ef394
          final Struct54 s1 = FUN_800f0400(_800d4e68);

          if(a1._10.get() != 0) {
            //LAB_800ef3e8
            s1._00.set((short)2);
            s1._02.set((short)3);

            //LAB_800ef3f8
          } else {
            s1._00.set((short)0);
            s1._02.set(a1._1c.get());

            final long v1 = a1._1c.get();
            if(v1 == 0) {
              a1._1c.set((short)1);
              //LAB_800ef3f8
            } else if(v1 == 0x1L) {
              //LAB_800ef3d8
              a1._1c.set((short)0);
            }
          }

          //LAB_800ef3fc
          getScreenOffset(s1.x_18, s1.y_1c);
          s1._04.set((short)0);
          s1._06.set((short)150);

          final MATRIX ls = new MATRIX();
          GsGetLs(struct.coord2_14, ls);
          CPU.CTC2(ls.getPacked(0), 0);
          CPU.CTC2(ls.getPacked(2), 1);
          CPU.CTC2(ls.getPacked(4), 2);
          CPU.CTC2(ls.getPacked(6), 3);
          CPU.CTC2(ls.getPacked(8), 4);
          CPU.CTC2(ls.transfer.getX(), 5);
          CPU.CTC2(ls.transfer.getY(), 6);
          CPU.CTC2(ls.transfer.getZ(), 7);

          final long v1 = s1._02.get();
          if(v1 == 0) {
            //LAB_800ef4b4
            s1.sz3div4_4c.set(RotTransPers4(sp0x48, sp0x50, sp0x58, sp0x60, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new Ref<>(), new Ref<>()));
          } else if(v1 == 0x1L) {
            //LAB_800ef484
            //LAB_800ef4b4
            s1.sz3div4_4c.set(RotTransPers4(sp0x68, sp0x70, sp0x78, sp0x80, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new Ref<>(), new Ref<>()));
          } else if(v1 == 0x3L) {
            //LAB_800ef4a0
            //LAB_800ef4b4
            s1.sz3div4_4c.set(RotTransPers4(sp0x28, sp0x30, sp0x38, sp0x40, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new Ref<>(), new Ref<>()));
          }

          //LAB_800ef4ec
          if(s1.sz3div4_4c.get() < 0x29L) {
            s1.sz3div4_4c.set(0x29L);
          }

          //LAB_800ef504
          s1._40.set(0x4_4444L);
          s1._44.set(0x80_0000L);
          s1.colour_48.set(0x80L);
        }
      }

      //LAB_800ef520
      if(a1._0c.get() != 0) {
        if(a1._00.get() % a1._30.get() == 0) {
          final Struct54 s1 = FUN_800f0400(_800d4e68);
          s1._00.set((short)1);
          s1._02.set((short)2);
          getScreenOffset(s1.x_18, s1.y_1c);

          final SVECTOR vert0 = new SVECTOR().set((short)-a1._28.get(), (short)0, (short)-a1._28.get());
          final SVECTOR vert1 = new SVECTOR().set((short) a1._28.get(), (short)0, (short)-a1._28.get());
          final SVECTOR vert2 = new SVECTOR().set((short)-a1._28.get(), (short)0, (short) a1._28.get());
          final SVECTOR vert3 = new SVECTOR().set((short) a1._28.get(), (short)0, (short) a1._28.get());

          s1._04.set((short)0);
          s1._06.set(a1._38.get());

          final MATRIX ls = new MATRIX();
          GsGetLs(struct.coord2_14, ls);
          CPU.CTC2(ls.getPacked(0), 0);
          CPU.CTC2(ls.getPacked(2), 1);
          CPU.CTC2(ls.getPacked(4), 2);
          CPU.CTC2(ls.getPacked(6), 3);
          CPU.CTC2(ls.getPacked(8), 4);
          CPU.CTC2(ls.transfer.getX(), 5);
          CPU.CTC2(ls.transfer.getY(), 6);
          CPU.CTC2(ls.transfer.getZ(), 7);

          //TODO The real code actually passes the same reference for sxyz 1 and 2, is that a bug?
          s1.sz3div4_4c.set(RotTransPers4(vert0, vert1, vert2, vert3, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new Ref<>(), new Ref<>()));

          if(s1.sz3div4_4c.get() < 0x29L) {
            s1.sz3div4_4c.set(0x29L);
          }

          //LAB_800ef6a0
          final int a0_0 = s1.sxyz3_38.getX() - s1.sxyz0_20.getX() << 16 >> 17 << 16;
          s1._08.set(a0_0);
          s1._0c.set(a0_0 / a1._38.get());
          s1._10.set(0);

          s1.sxyz0_20.setZ((short)((s1.sxyz3_38.getX() + s1.sxyz0_20.getX()) / 2));
          s1.sxyz1_28.setZ((short)((s1.sxyz3_38.getY() + s1.sxyz0_20.getY()) / 2));

          s1._40.set(0x80_0000L / a1._38.get());
          s1._44.set(0x80_0000L);
          s1.colour_48.set(0x80L);
        }
      }

      //LAB_800ef728
      if(a1._18.get() == 1) {
        if(bigStruct_800c6748.s_128.get() != -1) {
          FUN_800f0644(struct, a1);
        }
      }
    }

    //LAB_800ef750
    a1._1e.set(struct.coord2_14.coord.transfer);
    a1._00.incr();
  }

  @Method(0x800ef798L)
  public static void FUN_800ef798() {
    Struct20 s1 = _800d4ec0;
    Struct20 s0 = s1.next_1c.derefNullable();

    //LAB_800ef7c8
    while(s0 != null) {
      if(s0._00.get() >= s0._18.get()) {
        s1.next_1c.setNullable(s0.next_1c.derefNullable());
        removeFromLinkedList(s0.getAddress());
        s0 = s1.next_1c.derefNullable();
      } else {
        //LAB_800ef804
        s0.transfer.y.decr();

        _800d4d40.coord2_14.coord.transfer.set(s0.transfer);

        s0.scale_08.add(s0._04);

        _800d4d40.scaleVector_fc.setX(s0.scale_08.get());
        _800d4d40.scaleVector_fc.setY(s0.scale_08.get());
        _800d4d40.scaleVector_fc.setZ(s0.scale_08.get());

        FUN_800214bc(_800d4d40);
        FUN_800211d8(_800d4d40);

        _800d4d40.s_9e.set((short)0);
        _800d4d40.coord2ArrPtr_04.deref().get(0).flg.sub(0x1L);
        s0._00.incr();

        s1 = s0;
        s0 = s0.next_1c.derefNullable();
      }

      //LAB_800ef888
    }

    //LAB_800ef894
  }

  @Method(0x800ef8acL)
  public static void FUN_800ef8ac() {
    long a0;
    long a2;
    long v0;
    long v1;

    final long[] sp10 = new long[4];
    sp10[0] = _800d6bdc.get();
    sp10[1] = _800d6be0.get();
    sp10[2] = _800d6be4.get();
    sp10[3] = _800d6be8.get();

    final long[] sp20 = new long[4];
    sp20[0] = 0x40L;

    final long[] sp30 = new long[4];
    sp30[0] = _800d6bec.get();
    sp30[1] = _800d6bf0.get();
    sp30[2] = _800d6bf4.get();
    sp30[3] = _800d6bf8.get();

    final long[] sp40 = new long[4];
    sp40[0] = _800d6bfc.get();
    sp40[1] = _800d6c00.get();
    sp40[2] = _800d6c04.get();
    sp40[3] = _800d6c08.get();

    final long[] sp50 = new long[4];
    sp50[0] = _800d6c0c.get();
    sp50[1] = _800d6c10.get();
    sp50[2] = _800d6c14.get();
    sp50[3] = _800d6c18.get();

    final IntRef sox = new IntRef();
    final IntRef soy = new IntRef();
    getScreenOffset(sox, soy);
    final long screenOffsetX = sox.get();
    final long screenOffsetY = soy.get();

    //LAB_800ef9cc
    Struct54 s1 = _800d4e68;
    Struct54 s0 = s1.next_50.derefNullable();
    while(s0 != null) {
      if(s0._04.get() >= s0._06.get()) {
        s1.next_50.setNullable(s0.next_50.derefNullable());
        removeFromLinkedList(s0.getAddress());
        s0 = s1.next_50.derefNullable();
      } else {
        //LAB_800efa08
        final long packet = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, packet).offset(0x3L).setu(0x9L); // 9 words
        MEMORY.ref(4, packet).offset(0x4L).setu(0x2c80_8080L); // Textured four-point polygon, opaque, texture-blending

        v1 = s0._00.get();
        if(v1 == 0 || v1 == 0x2L) {
          //LAB_800efa44
          final long offsetX = screenOffsetX - s0.x_18.get();
          final long offsetY = screenOffsetY - s0.y_1c.get();
          MEMORY.ref(2, packet).offset(0x08L).setu(offsetX + s0.sxyz0_20.getX()); // Vertex 0 X
          MEMORY.ref(2, packet).offset(0x0aL).setu(offsetY + s0.sxyz0_20.getY()); // Vertex 0 Y
          MEMORY.ref(2, packet).offset(0x10L).setu(offsetX + s0.sxyz1_28.getX()); // Vertex 1 X
          MEMORY.ref(2, packet).offset(0x12L).setu(offsetY + s0.sxyz1_28.getY()); // Vertex 1 Y
          MEMORY.ref(2, packet).offset(0x18L).setu(offsetX + s0.sxyz2_30.getX()); // Vertex 2 X
          MEMORY.ref(2, packet).offset(0x1aL).setu(offsetY + s0.sxyz2_30.getY()); // Vertex 2 Y
          MEMORY.ref(2, packet).offset(0x20L).setu(offsetX + s0.sxyz3_38.getX()); // Vertex 3 X
          MEMORY.ref(2, packet).offset(0x22L).setu(offsetY + s0.sxyz3_38.getY()); // Vertex 3 Y

          if(s0._00.get() == 0x2L) {
            MEMORY.ref(2, packet).offset(0x0eL).setu(GetClut(960, 464)); // CLUT
            MEMORY.ref(2, packet).offset(0x16L).setu(GetTPage(TexPageBpp.BITS_4, TexPageTrans.B_MINUS_F, 960, 320)); // TPAGE
          } else {
            //LAB_800efb64
            MEMORY.ref(2, packet).offset(0x0eL).setu(_800d6068.offset(2, 0xeL)); // CLUT
            MEMORY.ref(2, packet).offset(0x16L).setu(_800d6050.offset(2, 0xeL)); // TPAGE
          }
        } else if(v1 == 0x1L) {
          //LAB_800efb7c
          s0._08.add(s0._0c.get());
          s0._10.set(s0._08.get() >> 16);
          v0 = s0.sxyz0_20.getZ();
          v1 = s0._08.get() >> 17;
          v0 -= v1;
          s0.sxyz0_20.setX((short)v0);
          a2 = s0.sxyz0_20.getX();
          v0 = s0.sxyz1_28.getZ() - v1;
          s0.sxyz0_20.setY((short)v0);
          a0 = screenOffsetY - s0.y_1c.get() + v0;
          v1 = screenOffsetX - s0.x_18.get() + a2;
          MEMORY.ref(2, packet).offset(0x08L).setu(v1); // Vertex 0 X
          MEMORY.ref(2, packet).offset(0x0aL).setu(a0); // Vertex 0 Y
          MEMORY.ref(2, packet).offset(0x10L).setu(v1 + s0._10.get()); // Vertex 1 X
          MEMORY.ref(2, packet).offset(0x12L).setu(a0); // Vertex 1 Y
          MEMORY.ref(2, packet).offset(0x18L).setu(v1); // Vertex 2 X
          MEMORY.ref(2, packet).offset(0x1aL).setu(a0 + s0._10.get()); // Vertex 2 Y
          MEMORY.ref(2, packet).offset(0x20L).setu(v1 + s0._10.get()); // Vertex 3 X
          MEMORY.ref(2, packet).offset(0x22L).setu(a0 + s0._10.get()); // Vertex 3 Y

          if((s0._04.get() & 0x3L) == 0) {
            s0.sxyz1_28.z.decr();
          }

          //LAB_800efc4c
          MEMORY.ref(2, packet).offset(0x0eL).setu(_800d6068.offset(2, 0xcL)); // CLUT
          MEMORY.ref(2, packet).offset(0x16L).setu(_800d6050.offset(2, 0xcL)); // TPAGE
        }

        //LAB_800efc64
        if(s0._04.get() >= sp50[s0._00.get()]) {
          s0._44.sub(s0._40);
          v0 = s0._44.get() >>> 16;
          if(v0 >= 0x100L) {
            s0.colour_48.set(0);
          } else {
            s0.colour_48.set(v0);
          }
        }

        //LAB_800efcb8
        MEMORY.ref(4, packet).offset(0x4L).setu((MEMORY.ref(1, packet).offset(0x7L).get() | 0x2L) << 24 | 0x80_8080L);
        MEMORY.ref(4, packet).offset(0x4L).setu((MEMORY.ref(1, packet).offset(0x7L).get() & 0xfeL) << 24 | 0x80_8080L);
        MEMORY.ref(1, packet).offset(0x4L).setu(s0.colour_48.get()); // R
        MEMORY.ref(1, packet).offset(0x5L).setu(s0.colour_48.get()); // G
        MEMORY.ref(1, packet).offset(0x6L).setu(s0.colour_48.get()); // B
        MEMORY.ref(1, packet).offset(0xcL).setu(sp10[s0._02.get()]); // U0
        MEMORY.ref(1, packet).offset(0xdL).setu(sp20[s0._02.get()]); // V0
        MEMORY.ref(1, packet).offset(0x14L).setu(sp10[s0._02.get()] + sp30[s0._02.get()]); // U1
        MEMORY.ref(1, packet).offset(0x15L).setu(sp20[s0._02.get()]); // V1
        MEMORY.ref(1, packet).offset(0x1cL).setu(sp10[s0._02.get()]); // U2
        MEMORY.ref(1, packet).offset(0x1dL).setu(sp20[s0._02.get()] + sp40[s0._02.get()]); // V2
        MEMORY.ref(1, packet).offset(0x24L).setu(sp10[s0._02.get()] + sp30[s0._02.get()]); // U3
        MEMORY.ref(1, packet).offset(0x25L).setu(sp20[s0._02.get()] + sp40[s0._02.get()]); // V3

        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)s0.sz3div4_4c.get()).getAddress(), packet);

        s0._04.incr();
        s1 = s0;
        s0 = s0.next_50.derefNullable();
      }

      //LAB_800efe48
    }

    //LAB_800efe54
  }

  @Method(0x800efe7cL)
  public static void FUN_800efe7c() {
    final IntRef refX = new IntRef();
    final IntRef refY = new IntRef();
    getScreenOffset(refX, refY);

    long s1 = _800d4f50.getAddress();
    long s0 = MEMORY.ref(4, s1).offset(0x38L).get();

    //LAB_800efecc
    while(s0 != 0) {
      if(MEMORY.ref(2, s0).offset(0x6L).getSigned() < MEMORY.ref(2, s0).offset(0x2L).getSigned()) {
        MEMORY.ref(4, s1).offset(0x38L).setu(MEMORY.ref(4, s0).offset(0x38L).get());
        removeFromLinkedList(s0);
        s0 = MEMORY.ref(4, s1).offset(0x38L).get();
      } else {
        //LAB_800eff04
        final int colour = Math.max((int)(MEMORY.ref(4, s0).offset(0x30L).getSigned() - MEMORY.ref(4, s0).offset(0x2cL).getSigned() >> 16), 0);

        MEMORY.ref(4, s0).offset(0x14L).add(MEMORY.ref(4, s0).offset(0x1cL).getSigned());
        MEMORY.ref(4, s0).offset(0x24L).add(MEMORY.ref(4, s0).offset(0x20L).getSigned());
        MEMORY.ref(2, s0).offset(0x28L).setu(MEMORY.ref(4, s0).offset(0x24L).getSigned() >> 16);
        MEMORY.ref(4, s0).offset(0x30L).sub(MEMORY.ref(4, s0).offset(0x2cL).getSigned());

        final int x = (int)(refX.get() - MEMORY.ref(2, s0).offset(0xcL).getSigned() + MEMORY.ref(2, s0).offset(0x10L).getSigned());
        final int y = (int)(refY.get() - MEMORY.ref(2, s0).offset(0xeL).getSigned() + (MEMORY.ref(4, s0).offset(0x14L).getSigned() >> 16) - (MEMORY.ref(4, s0).offset(0x24L).getSigned() >> 16));


        //LAB_800eff7c
        final long a1 = linkedListAddress_1f8003d8.get();
        MEMORY.ref(1, a1).offset(0x3L).setu(0x9L);
        MEMORY.ref(1, a1).offset(0x4L).setu(colour);
        MEMORY.ref(1, a1).offset(0x5L).setu(colour);
        MEMORY.ref(1, a1).offset(0x6L).setu(colour);
        MEMORY.ref(1, a1).offset(0x7L).setu(0x2eL);
        MEMORY.ref(2, a1).offset(0x8L).setu(x);
        MEMORY.ref(2, a1).offset(0xaL).setu(y);
        MEMORY.ref(1, a1).offset(0xcL).setu(0x40L);
        MEMORY.ref(1, a1).offset(0xdL).setu(0x40L);
        MEMORY.ref(2, a1).offset(0xeL).setu(_800d6074.get());
        MEMORY.ref(2, a1).offset(0x10L).setu(x + MEMORY.ref(2, s0).offset(0x28L).get());
        MEMORY.ref(2, a1).offset(0x12L).setu(y);
        MEMORY.ref(1, a1).offset(0x14L).setu(0x5fL);
        MEMORY.ref(1, a1).offset(0x15L).setu(0x40L);
        MEMORY.ref(2, a1).offset(0x16L).setu(_800d6050.offset(2, 0xcL).get());
        MEMORY.ref(2, a1).offset(0x18L).setu(x);
        MEMORY.ref(2, a1).offset(0x1aL).setu(y + MEMORY.ref(2, s0).offset(0x28L).get());
        MEMORY.ref(1, a1).offset(0x1cL).setu(0x40L);
        MEMORY.ref(1, a1).offset(0x1dL).setu(0x5fL);
        MEMORY.ref(2, a1).offset(0x20L).setu(x + MEMORY.ref(2, s0).offset(0x28L).get());
        MEMORY.ref(2, a1).offset(0x22L).setu(y + MEMORY.ref(2, s0).offset(0x28L).get());
        MEMORY.ref(1, a1).offset(0x24L).setu(0x5fL);
        MEMORY.ref(1, a1).offset(0x25L).setu(0x5fL);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0xa0L, a1);
        linkedListAddress_1f8003d8.addu(0x28L);

        MEMORY.ref(2, s0).offset(0x2L).addu(0x1L);
        s1 = s0;
        s0 = MEMORY.ref(4, s0).offset(0x38L).get();
      }
    }
  }

  @Method(0x800f00a4L)
  public static void FUN_800f00a4(final long a0, final long a1) {
    long s2 = _800d4f18.getAddress();
    if(MEMORY.ref(4, a0).offset(0x0L).get() == 0x1L) {
      long s1 = MEMORY.ref(4, s2).offset(0x30L).get();

      //LAB_800f0100
      while(s1 != 0) {
        if(MEMORY.ref(2, s1).offset(0x8L).getSigned() >= MEMORY.ref(2, s1).offset(0x2L).getSigned()) {
          final IntRef refX = new IntRef();
          final IntRef refY = new IntRef();
          getScreenOffset(refX, refY);

          if(MEMORY.ref(2, s1).offset(0x2L).getSigned() % MEMORY.ref(2, s1).offset(0x4L).getSigned() == 0) {
            //LAB_800f0148
            for(int i = 0; i < 4; i++) {
              final long s0 = FUN_800eed44(_800d4f50.getAddress());
              MEMORY.ref(2, s0).offset(0x2L).setu(0);
              MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, s1).offset(0x6L).get());
              MEMORY.ref(2, s0).offset(0xcL).setu(refX.get());
              MEMORY.ref(2, s0).offset(0xeL).setu(refY.get());
              MEMORY.ref(4, s0).offset(0x10L).setu(MEMORY.ref(4, s1).offset(0x1cL).get() + (simpleRand() * MEMORY.ref(2, s1).offset(0x18L).getSigned() >> 16));
              MEMORY.ref(4, s0).offset(0x14L).setu(MEMORY.ref(4, s1).offset(0x20L).get() << 16);
              MEMORY.ref(2, s0).offset(0x28L).setu(0);
              MEMORY.ref(4, s0).offset(0x1cL).setu(-MEMORY.ref(4, s1).offset(0xcL).get());
              MEMORY.ref(4, s0).offset(0x24L).setu(MEMORY.ref(4, s1).offset(0x10L).get());
              MEMORY.ref(4, s0).offset(0x30L).setu(0x80_0000L);
              MEMORY.ref(4, s0).offset(0x20L).setu(MEMORY.ref(4, s1).offset(0x14L).get());
              MEMORY.ref(4, s0).offset(0x2cL).setu(0x80_0000L / MEMORY.ref(2, s0).offset(0x6L).getSigned());
            }
          }

          //LAB_800f01ec
          s2 = s1;
          MEMORY.ref(2, s1).offset(0x2L).addu(0x1L);
          s1 = MEMORY.ref(4, s1).offset(0x30L).get();
        } else {
          //LAB_800f0208
          final long s0 = MEMORY.ref(4, s1).offset(0x30L).get();
          MEMORY.ref(4, s2).offset(0x30L).setu(s0);
          removeFromLinkedList(s1);

          if(_800d4f48.get() == 0) {
            MEMORY.ref(4, a0).offset(0x0L).setu(0);
          }

          s1 = s0;
        }
      }
    }

    //LAB_800f023c
    if(MEMORY.ref(4, a1).offset(0x0L).get() == 0x1L) {
      final long s1 = _800d4ee0.getAddress();
      final IntRef refX = new IntRef();
      final IntRef refY = new IntRef();
      getScreenOffset(refX, refY);

      if(MEMORY.ref(2, s1).offset(0x2L).getSigned() % MEMORY.ref(2, s1).offset(0x4L).getSigned() == 0) {
        //LAB_800f0284
        for(int i = 0; i < 1; i++) {
          final long s0 = FUN_800eed44(_800d4f50.getAddress());
          MEMORY.ref(2, s0).offset(0x2L).setu(0);
          MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, s1).offset(0x6L).get());
          MEMORY.ref(2, s0).offset(0xcL).setu(refX.get());
          MEMORY.ref(2, s0).offset(0xeL).setu(refY.get());
          MEMORY.ref(4, s0).offset(0x10L).setu(MEMORY.ref(4, s1).offset(0x1cL).get() + (simpleRand() * MEMORY.ref(2, s1).offset(0x18L).getSigned() >> 16));
          MEMORY.ref(4, s0).offset(0x14L).setu(MEMORY.ref(4, s1).offset(0x20L).get() << 16);
          MEMORY.ref(2, s0).offset(0x28L).setu(0);
          MEMORY.ref(4, s0).offset(0x1cL).setu(-MEMORY.ref(4, s1).offset(0xcL).get());
          MEMORY.ref(4, s0).offset(0x24L).setu(MEMORY.ref(4, s1).offset(0x10L).get());
          MEMORY.ref(4, s0).offset(0x30L).setu(0x80_0000L);
          MEMORY.ref(4, s0).offset(0x20L).setu(MEMORY.ref(4, s1).offset(0x14L).get());
          MEMORY.ref(4, s0).offset(0x2cL).setu(0x80_0000L / MEMORY.ref(2, s0).offset(0x6L).getSigned());
        }
      }

      //LAB_800f032c
      _800d4ee0.offset(2, 0x2L).addu(0x1L);
    }

    //LAB_800f0344
  }

  @Method(0x800f0370L)
  public static void FUN_800f0370() {
    FUN_80020a00(_800d4d40, mrg_800d6d1c.getFile(4, ExtendedTmd::new), mrg_800d6d1c.getFile(5, TmdAnimationFile::new));
    _800d4e68.next_50.clear();
    _800d4ec0.next_1c.clear();
    FUN_800f0e60();
  }

  @Method(0x800f03c0L)
  public static Struct20 FUN_800f03c0(final Struct20 a0) {
    final Struct20 v0 = MEMORY.ref(4, addToLinkedListHead(0x20L), Struct20::new);
    v0.next_1c.setNullable(a0.next_1c.derefNullable());
    a0.next_1c.set(v0);
    return v0;
  }

  @Method(0x800f0400L)
  public static Struct54 FUN_800f0400(final Struct54 a0) {
    final Struct54 v0 = MEMORY.ref(4, addToLinkedListHead(0x54L), Struct54::new);
    v0.next_50.setNullable(a0.next_50.derefNullable());
    a0.next_50.set(v0);
    return v0;
  }

  @Method(0x800f0440L)
  public static void FUN_800f0440() {
    FUN_80020fe0(_800d4d40);
    FUN_800f058c();
    FUN_800f05e8();
    FUN_800f0e7c();
  }

  @Method(0x800f047cL)
  public static void FUN_800f047c() {
    FUN_800ef798();
    FUN_800ef8ac();
    FUN_800f0970();
  }

  @Method(0x800f04acL)
  public static void FUN_800f04ac(final long a0) {
    MEMORY.ref(4, a0).offset(0x00L).setu(0);
    MEMORY.ref(4, a0).offset(0x04L).setu(0);
    MEMORY.ref(4, a0).offset(0x08L).setu(0);
    MEMORY.ref(4, a0).offset(0x0cL).setu(0);
    MEMORY.ref(4, a0).offset(0x10L).setu(0);
    MEMORY.ref(4, a0).offset(0x18L).setu(0);
    MEMORY.ref(2, a0).offset(0x1cL).setu(0);
    MEMORY.ref(2, a0).offset(0x1eL).setu(0);
    MEMORY.ref(2, a0).offset(0x20L).setu(0);
    MEMORY.ref(2, a0).offset(0x22L).setu(0);
    MEMORY.ref(4, a0).offset(0x28L).setu(0);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(4, a0).offset(0x34L).setu(0);
    MEMORY.ref(2, a0).offset(0x38L).setu(0);
    MEMORY.ref(4, a0).offset(0x3cL).setu(0);
  }

  @Method(0x800f0514L)
  public static void FUN_800f0514() {
    long v0;
    final long v1;
    long a0;
    long s0;
    final long s1;

    v0 = 0x800d_0000L;
    v1 = v0 + 0x4f18L;
    v0 = MEMORY.ref(4, v1).offset(0x30L).get();

    if(v0 != 0) {
      s1 = v1;

      //LAB_800f053c
      do {
        a0 = MEMORY.ref(4, s1).offset(0x30L).get();
        s0 = MEMORY.ref(4, a0).offset(0x30L).get();
        removeFromLinkedList(a0);
        MEMORY.ref(4, s1).offset(0x30L).setu(s0);
      } while(s0 != 0);
    }

    //LAB_800f055c
    a0 = 0x800d_0000L;
    a0 = a0 + 0x4f50L;
    v0 = 0x8010_0000L;
    MEMORY.ref(4, v0).offset(-0x618cL).setu(0);
    FUN_800eed84(a0);
    v0 = 0x8010_0000L;
    MEMORY.ref(4, v0).offset(-0x6190L).setu(0);
  }

  @Method(0x800f058cL)
  public static void FUN_800f058c() {
    long v0;
    final long v1;
    long a0;
    long s0;
    final long s1;

    v0 = 0x800d_0000L;
    v1 = v0 + 0x4ec0L;
    v0 = MEMORY.ref(4, v1).offset(0x1cL).get();

    if(v0 != 0) {
      s1 = v1;

      //LAB_800f05b4
      do {
        a0 = MEMORY.ref(4, s1).offset(0x1cL).get();
        s0 = MEMORY.ref(4, a0).offset(0x1cL).get();
        removeFromLinkedList(a0);
        MEMORY.ref(4, s1).offset(0x1cL).setu(s0);
      } while(s0 != 0);
    }

    //LAB_800f05d4
  }

  @Method(0x800f05e8L)
  public static void FUN_800f05e8() {
    long v0;
    final long v1;
    long a0;
    long s0;
    final long s1;

    v0 = 0x800d_0000L;
    v1 = v0 + 0x4e68L;
    v0 = MEMORY.ref(4, v1).offset(0x50L).get();

    if(v0 != 0) {
      s1 = v1;

      //LAB_800f0610
      do {
        a0 = MEMORY.ref(4, s1).offset(0x50L).get();
        s0 = MEMORY.ref(4, a0).offset(0x50L).get();
        removeFromLinkedList(a0);
        MEMORY.ref(4, s1).offset(0x50L).setu(s0);
      } while(s0 != 0);
    }

    //LAB_800f0630
  }

  @Method(0x800f0644L)
  public static void FUN_800f0644(final BigStruct a0, final BigSubStruct a1) {
    assert false;
  }

  @Method(0x800f0970L)
  public static void FUN_800f0970() {
    long v0;
    long v1;
    long s0;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;

    long s3 = _800d4f90.getAddress();
    long s1 = MEMORY.ref(4, s3).offset(0x30L).get();

    final IntRef sox = new IntRef();
    final IntRef soy = new IntRef();
    getScreenOffset(sox, soy);
    final long screenOffsetX = sox.get();
    final long screenOffsetY = soy.get();

    //LAB_800f09c0
    while(s1 != 0) {
      final long s2 = MEMORY.ref(4, s1).offset(0x2cL).get();
      if(MEMORY.ref(2, s2).offset(0x6L).get() >= MEMORY.ref(2, s1).get()) {
        //LAB_800f0b04
        s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x18L);
        MEMORY.ref(1, s0).offset(0x3L).setu(0x5L);
        MEMORY.ref(4, s0).offset(0x4L).setu(0x2880_8080L);

        s3 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0xcL);
        SetDrawMode(MEMORY.ref(4, s3, DR_MODE::new), false, false, MEMORY.ref(4, s1).offset(0x4L).get(), null);
        MEMORY.ref(4, s0).offset(0x04L).setu((MEMORY.ref(1, s0).offset(0x7L).get() | 0x02L) << 24 | 0x80_8080L);
        MEMORY.ref(4, s0).offset(0x04L).setu((MEMORY.ref(1, s0).offset(0x7L).get() & 0xfeL) << 24 | 0x80_8080L);
        MEMORY.ref(2, s0).offset(0x08L).setu(screenOffsetX + MEMORY.ref(4, s1).offset(0x24L).deref(2).offset(0x0L).get());
        MEMORY.ref(2, s0).offset(0x0aL).setu(screenOffsetY + MEMORY.ref(4, s1).offset(0x24L).deref(2).offset(0x2L).get());
        MEMORY.ref(2, s0).offset(0x0cL).setu(screenOffsetX + MEMORY.ref(4, s1).offset(0x24L).deref(2).offset(0x8L).get());
        MEMORY.ref(2, s0).offset(0x0eL).setu(screenOffsetY + MEMORY.ref(4, s1).offset(0x24L).deref(2).offset(0xaL).get());
        MEMORY.ref(2, s0).offset(0x10L).setu(screenOffsetX + MEMORY.ref(4, s1).offset(0x28L).deref(2).offset(0x0L).get());
        MEMORY.ref(2, s0).offset(0x12L).setu(screenOffsetY + MEMORY.ref(4, s1).offset(0x28L).deref(2).offset(0x2L).get());
        MEMORY.ref(2, s0).offset(0x14L).setu(screenOffsetX + MEMORY.ref(4, s1).offset(0x28L).deref(2).offset(0x8L).get());
        MEMORY.ref(2, s0).offset(0x16L).setu(screenOffsetY + MEMORY.ref(4, s1).offset(0x28L).deref(2).offset(0xaL).get());

        v0 = MEMORY.ref(2, s2).offset(0x2L).get();
        v0--;
        if(v0 >= MEMORY.ref(2, s1).get()) {
          //LAB_800f0d0c
          a2 = MEMORY.ref(2, s1).offset(0x16L).get();
          a0 = MEMORY.ref(2, s1).offset(0x1aL).get();
          a3 = MEMORY.ref(2, s1).offset(0x1eL).get();
        } else {
          a2 = MEMORY.ref(4, s1).offset(0x1cL).get();
          v0 = MEMORY.ref(4, s1).offset(0x10L).get();
          a0 = MEMORY.ref(4, s1).offset(0x8L).get();
          v1 = MEMORY.ref(4, s1).offset(0x18L).get();
          a1 = MEMORY.ref(4, s1).offset(0xcL).get();
          a2 -= v0;
          t0 = a2 >> 16;
          v0 = MEMORY.ref(4, s1).offset(0x14L).get();
          v1 -= a1;
          MEMORY.ref(4, s1).offset(0x18L).setu(v1);
          MEMORY.ref(4, s1).offset(0x1cL).setu(a2);
          v0 -= a0;
          MEMORY.ref(4, s1).offset(0x14L).setu(v0);
          v0 = MEMORY.ref(2, s1).offset(0x16L).get();
          if((int)v0 < 0) {
            a2 = 0;
          } else {
            a2 = v0;
          }

          //LAB_800f0cf0
          v1 = MEMORY.ref(2, s1).offset(0x1aL).get();
          if((int)v1 < 0) {
            a0 = 0;
          } else {
            a0 = v1;
          }

          //LAB_800f0cfc
          if((int)t0 < 0) {
            a3 = 0;
          } else {
            a3 = t0;
          }
        }

        //LAB_800f0d18
        MEMORY.ref(1, s0).offset(0x4L).setu(a2);
        MEMORY.ref(1, s0).offset(0x5L).setu(a0);
        MEMORY.ref(1, s0).offset(0x6L).setu(a3);

        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)MEMORY.ref(4, s1).offset(0x20L).get()).getAddress(), s0);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)MEMORY.ref(4, s1).offset(0x20L).get()).getAddress(), s3);

        s3 = s1;
        MEMORY.ref(2, s1).addu(0x1L);
        s1 = MEMORY.ref(4, s1).offset(0x30L).get();
      } else {
        MEMORY.ref(1, s2).offset(0x1L).subu(0x1L);
        a0 = _800d4fd0.offset(4, 0x10L).get();
        v0 = MEMORY.ref(4, s1).offset(0x24L).get();
        v1 = _800d4fd0.getAddress();

        //LAB_800f09fc
        while(a0 != 0) {
          if(a0 == v0) {
            //LAB_800f0ae8
            MEMORY.ref(4, v1).offset(0x10L).setu(MEMORY.ref(4, a0).offset(0x10L));
            removeFromLinkedList(a0);
            break;
          }

          v1 = a0;
          a0 = MEMORY.ref(4, a0).offset(0x10L).get();
        }

        //LAB_800f0a18
        if(MEMORY.ref(1, s2).offset(0x1L).get() == 0) {
          a0 = _800d4fd0.offset(4, 0x10L).get();
          v0 = MEMORY.ref(4, s1).offset(0x28L).get();
          v1 = _800d4fd0.getAddress();

          //LAB_800f0a38
          while(a0 != 0) {
            if(a0 == v0) {
              //LAB_800f0acc
              v0 = MEMORY.ref(4, a0).offset(0x10L).get();
              MEMORY.ref(4, v1).offset(0x10L).setu(v0);
              removeFromLinkedList(a0);
              break;
            }

            v1 = a0;
            a0 = MEMORY.ref(4, a0).offset(0x10L).get();
          }

          //LAB_800f0a54
        }

        //LAB_800f0a58
        MEMORY.ref(4, s3).offset(0x30L).setu(MEMORY.ref(4, s1).offset(0x30L));
        removeFromLinkedList(s1);

        s1 = MEMORY.ref(4, s3).offset(0x30L).get();
        if(MEMORY.ref(1, s2).offset(0x1L).get() == 0) {
          if(_800d4fc0.deref(4).offset(0x30L).get() == 0) {
            //LAB_800f0aa4
            while(_800d4fd0.offset(4, 0x10L).get() != 0) {
              a0 = _800d4fd0.offset(4, 0x10L).get();
              removeFromLinkedList(a0);
              _800d4fd0.offset(4, 0x10L).setu(MEMORY.ref(4, a0).offset(0x10L));
            }
          }
        }
      }

      //LAB_800f0d68
    }

    //LAB_800f0d74
    if(_800f9e78.get() == 0 && _800d4fc0.get() == 0) {
      //LAB_800f0da8
      while(_800d4fd0.offset(4, 0x10L).get() != 0) {
        a0 = _800d4fd0.offset(4, 0x10L).get();
        removeFromLinkedList(a0);
        _800d4fd0.offset(4, 0x10L).setu(MEMORY.ref(4, a0).offset(0x10L));
      }
    }

    //LAB_800f0dc8
  }

  @Method(0x800f0e60L)
  public static void FUN_800f0e60() {
    _800f9e78.setu(0);
    _800d4fc0.setu(0);
    _800d4fe0.setu(0);
  }

  @Method(0x800f0e7cL)
  public static void FUN_800f0e7c() {
    long v0;
    long a0;
    long s0;
    long s1;

    v0 = 0x800d_0000L;
    s1 = v0 + 0x4f90L;
    v0 = MEMORY.ref(4, s1).offset(0x30L).get();

    if(v0 != 0) {
      //LAB_800f0ea4
      do {
        a0 = MEMORY.ref(4, s1).offset(0x30L).get();
        s0 = MEMORY.ref(4, a0).offset(0x30L).get();
        removeFromLinkedList(a0);
        MEMORY.ref(4, s1).offset(0x30L).setu(s0);
      } while(s0 != 0);
    }

    //LAB_800f0ec8
    v0 = 0x800d_0000L;
    s1 = v0 + 0x4fd0L;
    v0 = MEMORY.ref(4, s1).offset(0x10L).get();

    if(v0 != 0) {
      //LAB_800f0edc
      do {
        a0 = MEMORY.ref(4, s1).offset(0x10L).get();
        s0 = MEMORY.ref(4, a0).offset(0x10L).get();
        removeFromLinkedList(a0);
        MEMORY.ref(4, s1).offset(0x10L).setu(s0);
      } while(s0 != 0);
    }

    //LAB_800f0efc
    FUN_800f0fe8();
    v0 = 0x8010_0000L;
    MEMORY.ref(2, v0).offset(-0x6188L).setu(0);
  }

  @Method(0x800f0fe8L)
  public static void FUN_800f0fe8() {
    long v0;
    long a0;
    long s0;
    long s1;
    final long s2;

    s1 = 0;
    s2 = 0x8010_0000L;
    v0 = 0x8010_0000L;
    s0 = v0 - 0x6184L;

    //LAB_800f100c
    do {
      a0 = MEMORY.ref(4, s0).offset(0x0L).get();

      if(a0 != 0) {
        MEMORY.ref(4, s0).offset(0x0L).setu(0);
        removeFromLinkedList(a0);
        v0 = MEMORY.ref(2, s2).offset(-0x6188L).get();
        v0 = v0 - 0x1L;
        MEMORY.ref(2, s2).offset(-0x6188L).setu(v0);
      }

      //LAB_800f1038
      s1 = s1 + 0x1L;
      s0 = s0 + 0x4L;
    } while((int)s1 < 0x8L);
  }

  @Method(0x800f1060L)
  public static long FUN_800f1060(final RunningScript a0) {
    if(_800d4fe8.getSigned() != 0) {
      _800d4fe8.addu(0x1L);
      return 0;
    }

    final SVECTOR sp0x10 = new SVECTOR();
    final SVECTOR sp0x18 = new SVECTOR();
    final MATRIX sp0x20 = new MATRIX();
    final Memory.TemporaryReservation sp0x40tmp = MEMORY.temp(0x50);
    final GsCOORDINATE2 sp0x40 = new GsCOORDINATE2(sp0x40tmp.get());

    //LAB_800f10ac
    struct34_800d6018.parent_30.clear();
    GsInitCoordinate2(null, sp0x40);

    long s1 = a0.params_20.get(0).getPointer();

    //LAB_800f10dc
    while((int)MEMORY.ref(4, s1).get() != -0x1L) {
      final Struct34 struct = MEMORY.ref(4, addToLinkedListTail(0x34L), Struct34::new);
      struct.parent_30.setNullable(struct34_800d6018.parent_30.derefNullable());
      struct34_800d6018.parent_30.set(struct);

      sp0x40.coord.transfer.setX((int)MEMORY.ref(4, s1).get());
      s1 = s1 + 0x4L;
      sp0x40.coord.transfer.setX((int)MEMORY.ref(4, s1).get());
      s1 = s1 + 0x4L;
      sp0x40.coord.transfer.setX((int)MEMORY.ref(4, s1).get());
      s1 = s1 + 0x4L;
      GsGetLs(sp0x40, sp0x20);

      PushMatrix();
      CPU.CTC2(sp0x20.getPacked(0), 0);
      CPU.CTC2(sp0x20.getPacked(2), 1);
      CPU.CTC2(sp0x20.getPacked(4), 2);
      CPU.CTC2(sp0x20.getPacked(6), 3);
      CPU.CTC2(sp0x20.getPacked(8), 4);
      CPU.CTC2(sp0x20.transfer.getX(), 5);
      CPU.CTC2(sp0x20.transfer.getY(), 6);
      CPU.CTC2(sp0x20.transfer.getZ(), 7);
      sp0x10.set((short)0, (short)0, (short)0);
      CPU.MTC2(sp0x10.getXY(), 0);
      CPU.MTC2(sp0x10.getZ(), 1);
      CPU.COP2(0x180001L);
      sp0x18.setXY(CPU.MFC2(14));

      // These were both writing to 0
      //MEMORY.ref(4, s5).setu(CPU.MFC2(8));
      //MEMORY.ref(4, s4).setu(CPU.CFC2(31));

      struct.sz3_2c.set((int)CPU.MFC2(19) >> 2); // SZ3
      PopMatrix();

      struct._02.set(0);
      struct._04.set(17);
      struct._06.set(100);
      struct._08.set(0);
      struct._0a.set(0);
      struct._0c.set(MEMORY.ref(4, s1).get());
      s1 = s1 + 0x4L;
      struct._10.set(MEMORY.ref(4, s1).get());
      s1 = s1 + 0x4L;
      struct._14.set(MEMORY.ref(4, s1).get());
      s1 = s1 + 0x4L;
      struct._18.set((int)MEMORY.ref(2, s1).get());
      s1 = s1 + 0x4L;
      struct.x_1c.set(sp0x18.getX());
      struct.y_20.set(sp0x18.getY());
      getScreenOffset(struct.screenOffsetX_24, struct.screenOffsetY_28);
    }

    //LAB_800f123c
    _800d4fe8.addu(0x1L);

    sp0x40tmp.release();

    //LAB_800f1250
    return 0;
  }

  @Method(0x800f1274L)
  public static long FUN_800f1274(final RunningScript a0) {
    _800f9e74.setu(0x1L);

    final Struct34 v1 = _800d4f18;
    final Struct34 s1 = MEMORY.ref(4, addToLinkedListHead(0x34L), Struct34::new);
    s1.parent_30.setNullable(v1.parent_30.derefNullable());
    v1.parent_30.set(s1);

    final Memory.TemporaryReservation sp0x48tmp = MEMORY.temp(0x50);
    final GsCOORDINATE2 sp0x48 = new GsCOORDINATE2(sp0x48tmp.get());
    GsInitCoordinate2(null, sp0x48);

    final IntRef sp0x98 = new IntRef();
    final IntRef sp0x9c = new IntRef();
    getScreenOffset(sp0x98, sp0x9c);
    sp0x48.coord.transfer.set(a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get());
    final MATRIX sp0x28 = new MATRIX();
    GsGetLs(sp0x48, sp0x28);
    PushMatrix();
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);
    CPU.MTC2(0, 0);
    CPU.MTC2(0, 1);
    CPU.COP2(0x180001L);

    final DVECTOR sp0x10 = new DVECTOR();
    sp0x10.setXY(CPU.MFC2(14));
    final long spa0 = CPU.MFC2(8);
    final long spa4 = CPU.CFC2(31);
    final int spa8 = (int)CPU.MFC2(19) >> 2;
    PopMatrix();

    if(a0.params_20.get(2).deref().get() < a0.params_20.get(1).deref().get()) {
      a0.params_20.get(2).deref().add(a0.params_20.get(1).deref().get());
    }

    //LAB_800f13f0
    s1._02.set(0);
    s1._04.set(a0.params_20.get(0).deref().get());
    s1._06.set(a0.params_20.get(1).deref().get());
    s1._08.set(a0.params_20.get(2).deref().get());
    s1._0a.set(0);
    s1._0c.set((a0.params_20.get(6).deref().get() << 16) / a0.params_20.get(1).deref().get());
    s1._10.set(a0.params_20.get(8).deref().get() << 16);
    s1._14.set((a0.params_20.get(9).deref().get() << 16) / a0.params_20.get(1).deref().get());
    s1._18.set(a0.params_20.get(7).deref().get());
    s1.x_1c.set(sp0x10.getX());
    s1.y_20.set(sp0x10.getY());
    s1.screenOffsetX_24.set(sp0x98.get());
    s1.screenOffsetY_28.set(sp0x9c.get());
    s1.sz3_2c.set(spa8);

    sp0x48tmp.release();

    return 0;
  }

  @Method(0x800f14f0L)
  public static long FUN_800f14f0(final RunningScript a0) {
    final int v1 = a0.params_20.get(0).deref().get();
    _800f9e70.setu(v1);

    final long a1 = _800d4ee0.getAddress();
    if(v1 != 0) {
      MEMORY.ref(2, a1).offset(0x2L).setu(0);
      MEMORY.ref(2, a1).offset(0x4L).setu(a0.params_20.get(1).deref().get());

      if(a0.params_20.get(2).deref().get() == 0) {
        a0.params_20.get(2).deref().set(1);
      }

      //LAB_800f154c
      MEMORY.ref(2, a1).offset(0x6L).setu(a0.params_20.get(2).deref().get());
      MEMORY.ref(4, a1).offset(0x1cL).setu(a0.params_20.get(3).deref().get());
      MEMORY.ref(4, a1).offset(0x20L).setu(a0.params_20.get(4).deref().get());
      MEMORY.ref(2, a1).offset(0x18L).setu(a0.params_20.get(5).deref().get());
      MEMORY.ref(4, a1).offset(0xcL).setu((a0.params_20.get(6).deref().get() << 16) / a0.params_20.get(2).deref().get());
      MEMORY.ref(4, a1).offset(0x10L).setu(a0.params_20.get(7).deref().get() << 16);
      MEMORY.ref(4, a1).offset(0x14L).setu((a0.params_20.get(8).deref().get() << 16) / a0.params_20.get(2).deref().get());

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
    return 0;
  }

  @Method(0x800f179cL)
  public static long FUN_800f179c(final RunningScript s1) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    final long s0;

    final Memory.TemporaryReservation sp0x78ref = MEMORY.temp(0x50);

    final MATRIX sp0x28 = new MATRIX();
    final DVECTOR sp0x48 = new DVECTOR();
    final DVECTOR sp0x50 = new DVECTOR();
    final SVECTOR sp0x58 = new SVECTOR();
    final SVECTOR sp0x60 = new SVECTOR();
    final SVECTOR sp0x68 = new SVECTOR();
    final SVECTOR sp0x70 = new SVECTOR();
    final GsCOORDINATE2 sp0x78 = new GsCOORDINATE2(sp0x78ref.get());
    final SVECTOR sp0xc8 = new SVECTOR();
    final SVECTOR sp0xd0 = new SVECTOR();
    final Ref<Long> sp0xd8 = new Ref<>(0L);
    final Ref<Long> sp0xdc = new Ref<>(0L);

    sp0x58.set(_800d6c28);
    sp0x60.set(_800d6c30);
    sp0x68.set(_800d6c38);
    sp0x70.set(_800d6c40);

    _800d5620.setu(s1.params_20.get(0).deref().get());
    s0 = _800d5620.getAddress();
    GsInitCoordinate2(null, sp0x78);

    MEMORY.ref(2, s0).offset(0x2L).setu(s1.params_20.get(1).deref().get());
    MEMORY.ref(2, s0).offset(0x4L).setu(s1.params_20.get(2).deref().get());
    MEMORY.ref(2, s0).offset(0x6L).setu(s1.params_20.get(3).deref().get());

    sp0x78.coord.transfer.setX(s1.params_20.get(1).deref().get());
    sp0x78.coord.transfer.setY(s1.params_20.get(2).deref().get());
    sp0x78.coord.transfer.setZ(s1.params_20.get(3).deref().get());

    GsGetLs(sp0x78, sp0x28);
    PushMatrix();

    CPU.CTC2((sp0x28.get(1) & 0xffffL) << 16 | sp0x28.get(0) & 0xffff, 0);
    CPU.CTC2((sp0x28.get(3) & 0xffffL) << 16 | sp0x28.get(2) & 0xffff, 1);
    CPU.CTC2((sp0x28.get(5) & 0xffffL) << 16 | sp0x28.get(4) & 0xffff, 2);
    CPU.CTC2((sp0x28.get(7) & 0xffffL) << 16 | sp0x28.get(6) & 0xffff, 3);
    CPU.CTC2(                                  sp0x28.get(8) & 0xffff, 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);

    //LAB_800f195c
    for(int s3 = 0; s3 < 2; s3++) {
      final SMapStruct44 struct = _800d5598.get(s3);

      struct.sz3div4_40.set((int)RotTransPers4(sp0x58, sp0x60, sp0x68, sp0x70, struct.svec_00, struct.svec_08, struct.svec_10, struct.svec_18, sp0xd8, sp0xdc));

      if(s3 == 0) {
        sp0xc8.set(_800d6c48);
        sp0xd0.set(_800d6c50);

        perspectiveTransform(sp0xc8, sp0x48, new Ref<>(), sp0xdc);
        perspectiveTransform(sp0xd0, sp0x50, new Ref<>(), sp0xdc);

        sp0x48.setX((short)(sp0x50.getY() - sp0x48.getY()));
      }

      //LAB_800f1a34
      a3 = (struct.svec_18.getX() + struct.svec_00.getX()) / 2;
      t0 = (struct.svec_18.getY() + struct.svec_00.getY()) / 2;

      if(s3 == 0) {
        a2 = (_800d5598.get(0).svec_00.getX() - _800d5598.get(0).svec_18.getX()) / 2;
        a0 = a3 - a2;
        v0 = t0 - a2 - sp0x48.getX();
        v1 = a3 + a2;
        _800d5598.get(0).svec_00.setX((short)a0);
        _800d5598.get(0).svec_00.setY((short)v0);
        _800d5598.get(0).svec_08.setY((short)v0);
        v0 = t0 + a2 - sp0x48.getX();
        _800d5598.get(0).svec_08.setX((short)v1);
        _800d5598.get(0).svec_10.setX((short)a0);
        _800d5598.get(0).svec_10.setY((short)v0);
        _800d5598.get(0).svec_18.setX((short)v1);
        _800d5598.get(0).svec_18.setY((short)v0);
      } else {
        //LAB_800f1aa8
        a2 = (struct.svec_00.getX() - struct.svec_18.getX()) / 2;
        a0 = a3 - a2;
        a1 = a3 + a2;
        struct.svec_00.setX((short)a0);
        v0 = t0 - a2;
        struct.svec_08.setX((short)a1);
        struct.svec_00.setY((short)(v0 - sp0x48.getX()));
        struct.svec_08.setY((short)a0);
        struct.svec_10.setY((short)(v0 - sp0x48.getX()));
        v1 = t0 + a2;
        struct.svec_18.setX((short)a1);
        struct.svec_10.setY((short)(v1 - sp0x48.getX()));
        struct.svec_18.setY((short)(v1 - sp0x48.getX()));
      }

      //LAB_800f1b04
      getScreenOffset(struct.screenOffsetX_20, struct.screenOffsetY_24);
    }

    PopMatrix();

    sp0x78ref.release();

    return 0;
  }

  @Method(0x800f1b64L)
  public static long FUN_800f1b64(final RunningScript a0) {
    long v1;
    long a1;
    long s0;
    long s1;

    final Memory.TemporaryReservation sp0x18tmp = MEMORY.temp(0x50);

    final SVECTOR sp0x10 = new SVECTOR();
    final GsCOORDINATE2 sp0x18 = new GsCOORDINATE2(sp0x18tmp.get());
    final SVECTOR sp0x68 = new SVECTOR();
    final MATRIX sp0x70 = new MATRIX();
    final IntRef sp90 = new IntRef();
    final IntRef sp94 = new IntRef();
    long sp98;
    long sp9c;
    long spa0;

    s0 = a0.params_20.get(0).getPointer();
    getScreenOffset(sp90, sp94);
    GsInitCoordinate2(null, sp0x18);

    s1 = 0;
    //LAB_800f1ba8
    while(MEMORY.ref(4, s0).getSigned() != -0x1L) {
      get3dAverageOfSomething((int)MEMORY.ref(4, s0).get(), sp0x10);

      sp0x18.coord.transfer.setX(sp0x10.getX());
      sp0x18.coord.transfer.setY(sp0x10.getY());
      sp0x18.coord.transfer.setZ(sp0x10.getZ());
      GsGetLs(sp0x18, sp0x70);

      PushMatrix();
      CPU.CTC2((sp0x70.get(1) & 0xffffL) << 16 | sp0x70.get(0) & 0xffffL, 0);
      CPU.CTC2((sp0x70.get(3) & 0xffffL) << 16 | sp0x70.get(2) & 0xffffL, 1);
      CPU.CTC2((sp0x70.get(5) & 0xffffL) << 16 | sp0x70.get(4) & 0xffffL, 2);
      CPU.CTC2((sp0x70.get(7) & 0xffffL) << 16 | sp0x70.get(6) & 0xffffL, 3);
      CPU.CTC2(                                  sp0x70.get(8) & 0xffffL, 4);
      CPU.CTC2(sp0x70.transfer.getX(), 5);
      CPU.CTC2(sp0x70.transfer.getY(), 6);
      CPU.CTC2(sp0x70.transfer.getZ(), 7);

      sp0x10.set((short)0, (short)0, (short)0);
      CPU.MTC2(sp0x10.getXY(), 0);
      CPU.MTC2(sp0x10.getZ(), 1);

      CPU.COP2(0x180001L);

      sp0x68.setXY(CPU.MFC2(14)); // SXY2
      sp98 = CPU.MFC2(8); // IR0
      sp9c = CPU.CFC2(31); // FLAG
      spa0 = CPU.MFC2(19) >> 2; // SZ3

      s0 = s0 + 0x4L;
      PopMatrix();
      v1 = MEMORY.ref(2, s0).get();
      s0 = s0 + 0x4L;

      final long a0_0 = _800c69fc.get() + s1 * 0x2L;
      MEMORY.ref(2, a0_0).offset(0x18L).setu(v1);
      s0 = s0 + 0x4L;
      MEMORY.ref(2, a0_0).offset(0x40L).setu(sp0x68.getX() + MEMORY.ref(2, s0).get());
      MEMORY.ref(2, a0_0).offset(0x68L).setu(sp0x68.getY() + MEMORY.ref(2, s0).get());
      a1 = _800c69fc.get() + s1 * 0x4L;
      MEMORY.ref(4, a1).offset(0x90L).setu(sp90.get());
      MEMORY.ref(4, a1).offset(0xe0L).setu(sp94.get());
      s0 = s0 + 0x4L;
      s1 = s1 + 0x1L;
    }

    sp0x18tmp.release();

    //LAB_800f1cf0
    return 0;
  }

  @Method(0x800f1f9cL)
  public static long FUN_800f1f9c(final RunningScript a0) {
    final long a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, a1).offset(0x1d4L).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(4, a1).offset(0x1f8L).setu(a0.params_20.get(2).deref().get());
    MEMORY.ref(4, a1).offset(0x200L).setu(a0.params_20.get(3).deref().get());

    if(a0.params_20.get(4).deref().get() == 0) {
      MEMORY.ref(2, a1).offset(0x208L).setu(0x1L);
    } else {
      MEMORY.ref(2, a1).offset(0x208L).setu(a0.params_20.get(4).deref().get());
    }

    //LAB_800f2018
    if(MEMORY.ref(4, a1).offset(0x1d4L).get() != 0x1L) {
      MEMORY.ref(2, a1).offset(0x1f2L).setu(0);
      MEMORY.ref(2, a1).offset(0x1f0L).setu(0);
      MEMORY.ref(2, a1).offset(0x1eeL).setu(0);
      MEMORY.ref(4, a1).offset(0x1f8L).setu(0x1L);
      MEMORY.ref(4, a1).offset(0x200L).setu(0);
      MEMORY.ref(2, a1).offset(0x208L).setu(0);
    }

    //LAB_800f2040
    return 0;
  }

  @Method(0x800f2048L)
  public static long FUN_800f2048(final RunningScript a0) {
    a0.params_20.get(4).set(a0.params_20.get(3).deref());
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.scriptState_04.deref().storage_44.get(0));
    return FUN_800f1f9c(a0);
  }

  @Method(0x800f23ecL)
  public static long FUN_800f23ec(final RunningScript a0) {
    a0.params_20.get(4).set(a0.scriptState_04.deref().storage_44.get(0));
    final long a2 = a0.params_20.get(0).deref().get();
    final BigStruct a1 = scriptStatePtrArr_800bc1c0.get(a0.scriptState_04.deref().storage_44.get(0).get()).deref().innerStruct_00.derefAs(BigStruct.class);

    if(a2 == 0x1L || a2 == 0x3L) {
      //LAB_800f2430
      a1._1d0._0c.set(0x1L);
      a1._1d0._28.set(a0.params_20.get(1).deref().get());
      a1._1d0._30.set(a0.params_20.get(2).deref().get());
      a1._1d0._38.set((short)a0.params_20.get(3).deref().get());

      if(a1._1d0._38.get() == 0) {
        a1._1d0._38.incr();
      }
    } else {
      //LAB_800f2484
      a1._1d0._0c.set(0);
    }

    //LAB_800f2488
    if(a1._1d0._0c.get() != 0x1L) {
      a1._1d0._0c.set(0);
      a1._1d0._1e.set((short)0, (short)0, (short)0);
    }

    //LAB_800f24a8
    return 0;
  }

  @Method(0x800f23a0L)
  public static long FUN_800f23a0(final RunningScript script) {
    script.params_20.get(1).set(script.scriptState_04.deref().storage_44.get(0));

    final long a0 = scriptStatePtrArr_800bc1c0.get(script.scriptState_04.deref().storage_44.get(0).get()).deref().innerStruct_00.getPointer(); //TODO

    final int v0 = script.params_20.get(0).deref().get();
    MEMORY.ref(4, a0).offset(0x204L).setu(v0);

    if(v0 != 0) {
      MEMORY.ref(4, a0).offset(0x204L).setu(v0);
    } else {
      MEMORY.ref(4, a0).offset(0x204L).setu(0x1L);
    }

    //LAB_800f23e4
    return 0;
  }

  @Method(0x800f24b0L)
  public static long FUN_800f24b0(final RunningScript script) {
    if(script.params_20.get(0).deref().get() == 1) {
      _800f9e70.setu(0x2L);
    }

    //LAB_800f24d0
    return 0;
  }

  @Method(0x800f24d8L)
  public static long FUN_800f24d8(final RunningScript script) {
    if(script.params_20.get(0).deref().get() != 0) {
      _800f9e70.setu(0);
    }

    //LAB_800f24fc
    if(_800f9e70.get() == 0) {
      final long v0 = _800d4ee0.getAddress();
      final long a0 = _800d4f50.getAddress();
      MEMORY.ref(2, v0).offset(0x04L).setu(0);
      MEMORY.ref(2, v0).offset(0x02L).setu(0);
      MEMORY.ref(2, v0).offset(0x06L).setu(0);
      MEMORY.ref(4, v0).offset(0x0cL).setu(0);
      MEMORY.ref(4, v0).offset(0x10L).setu(0);
      MEMORY.ref(4, v0).offset(0x14L).setu(0);
      MEMORY.ref(2, v0).offset(0x18L).setu(0);
      MEMORY.ref(4, v0).offset(0x1cL).setu(0);
      MEMORY.ref(4, v0).offset(0x20L).setu(0);
      FUN_800eed84(a0);
    }

    //LAB_800f2544
    return 0;
  }

  @Method(0x800f2618L)
  public static long FUN_800f2618(final RunningScript script) {
    final IntRef refOffsetX = new IntRef();
    final IntRef refOffsetY = new IntRef();
    getScreenOffset(refOffsetX, refOffsetY);
    final int x = refOffsetX.get();
    final int y = refOffsetY.get();

    long v1 = _800c69fc.get();
    long a1 = v1;

    //LAB_800f266c
    int i = 0;
    final UnboundedArrayRef<IntRef> a0 = script.params_20.get(0).deref().reinterpret(4, UnboundedArrayRef.of(4, IntRef::new));
    while(a0.get(i).get() != -1) {
      MEMORY.ref(2, v1).offset(0x18L).setu(a0.get(i++).get());
      MEMORY.ref(2, v1).offset(0x40L).setu(a0.get(i++).get() + x);
      MEMORY.ref(2, v1).offset(0x68L).setu(a0.get(i++).get() + y);
      MEMORY.ref(4, a1).offset(0x90L).setu(x);
      MEMORY.ref(4, a1).offset(0xe0L).setu(y);
      a1 = a1 + 0x4L;
      v1 = v1 + 0x2L;
    }

    //LAB_800f26b4
    return 0;
  }

  //TODO struct
  @Method(0x800f2788L)
  public static void FUN_800f2788() {
    final long[] sp10 = new long[8];

    FUN_80020a00(_800d5eb0, mrg_800d6d1c.getFile(2, ExtendedTmd::new), mrg_800d6d1c.getFile(3, TmdAnimationFile::new));
    _800d5598.get(0)._28.set(0);
    _800d5598.get(0)._34.set(0x50L);
    _800d5598.get(1)._28.set(0);
    _800d5598.get(1)._2c.set(0x1_f800L);
    _800d5598.get(1)._30.set(0);
    _800d5598.get(1)._34.set(0);
    _800d5598.get(1)._38.set(0);
    sp10[0] = _800d6c58.get();
    sp10[1] = _800d6c5c.get();
    sp10[2] = _800d6c60.get();
    sp10[3] = _800d6c64.get();
    sp10[4] = _800d6c68.get();
    sp10[5] = _800d6c6c.get();
    sp10[6] = _800d6c70.get();
    sp10[7] = _800d6c74.get();

    long a3 = _800d5630.getAddress();
    long t3 = 0xccL;
    long t2 = 0x88L;
    long t1 = 0x44L;

    //LAB_800f285c
    for(int t5 = 0; t5 < 8; t5++) {
      final long a2 = _800d5630.offset(t3).getAddress();
      final long a1 = _800d5630.offset(t2).getAddress();
      final long a0 = _800d5630.offset(t1).getAddress();
      MEMORY.ref(4, a3).offset(0x34L).setu(0x80L);
      MEMORY.ref(4, a3).offset(0x2cL).setu(0x1_fc00L);
      MEMORY.ref(4, a3).offset(0x30L).setu(0);
      MEMORY.ref(2, a3).offset(0x38L).setu(0);
      MEMORY.ref(4, a3).offset(0x28L).setu(sp10[t5]);
      MEMORY.ref(4, a0).offset(0x34L).setu(0x60L);
      MEMORY.ref(4, a1).offset(0x34L).setu(MEMORY.ref(4, a3).offset(0x34L).get() - 0x40L);
      MEMORY.ref(4, a2).offset(0x34L).setu(MEMORY.ref(4, a3).offset(0x34L).get() - 0x60L);
      t3 += 0x110L;
      t2 += 0x110L;
      t1 += 0x110L;
      a3 += 0x110L;
    }
  }

  @Method(0x800f28d8L)
  public static void FUN_800f28d8() {
    long v0;
    long v1;
    long a0;
    long a2;
    long t3;
    long t4;
    long t5;
    long t6;
    long s1;
    long s2;
    long s3;
    long s4;
    long s7;
    long fp;
    long lo;
    long sp10;
    long sp12;
    long sp14;
    long sp16;
    long sp18;
    long sp1a;
    long sp1c;
    long sp1e;
    long sp20 = 0;
    long sp22 = 0;
    long sp24 = 0;
    long sp26 = 0;
    long sp28 = 0;
    long sp2a = 0;
    long sp2c = 0;
    long sp2e = 0;
    final RECT sp0x30 = new RECT();
    final short[] sp0x38 = new short[8];
    final int[] sp0x48 = new int[8];
    final long sp68;
    final long sp6a;
    final long sp70;
    final long sp74;
    final long sp78;
    final long sp80;
    long sp88;
    long sp8c;
    long sp90;

    final IntRef sp0x70 = new IntRef();
    final IntRef sp0x74 = new IntRef();
    getScreenOffset(sp0x70, sp0x74);
    sp70 = sp0x70.get();
    sp74 = sp0x74.get();

    final BigStruct s0 = _800d5eb0;
    s7 = 0;
    v0 = 0x800d_0000L;
    v0 = v0 + 0x5620L;
    s0.scaleVector_fc.setX(0x1800);
    s0.scaleVector_fc.setY(0x3000);
    s0.scaleVector_fc.setZ(0x1800);
    s0.coord2_14.coord.transfer.setX((int)MEMORY.ref(2, v0).offset(0x2L).getSigned());
    s0.coord2_14.coord.transfer.setY((int)MEMORY.ref(2, v0).offset(0x4L).getSigned());
    s0.coord2_14.coord.transfer.setZ((int)MEMORY.ref(2, v0).offset(0x6L).getSigned());

    FUN_800214bc(s0);
    FUN_80020b98(s0);
    FUN_800211d8(s0);

    long s0_0 = linkedListAddress_1f8003d8.get();
    sp0x30.set((short)984, (short)(288 + _800f9ea0.get()), (short)8, (short)(64 - _800f9ea0.get()));
    SetDrawMove(MEMORY.ref(4, s0_0, DR_MOVE::new), sp0x30, 992L, 288L);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, s0_0);
    linkedListAddress_1f8003d8.addu(0x18L);

    s0_0 = linkedListAddress_1f8003d8.get();
    sp0x30.set((short)984, (short)288, (short)8, (short)_800f9ea0.get());
    SetDrawMove(MEMORY.ref(4, s0_0, DR_MOVE::new), sp0x30, 992, 352 - _800f9ea0.getSigned());
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, s0_0);
    linkedListAddress_1f8003d8.addu(0x18L);

    v0 = 0x800d_0000L;
    s3 = v0 + 0x5598L;
    s1 = s3 + 0x44L;
    v0 = _800f9ea0.get();
    s0_0 = s3;
    v0 = v0 + 0x1L;
    v0 = v0 & 0x3fL;
    _800f9ea0.setu(v0);

    //LAB_800f2a44
    do {
      a0 = sp70 - MEMORY.ref(2, s0_0).offset(0x20L).get();
      v1 = sp74 - MEMORY.ref(2, s0_0).offset(0x24L).get();

      sp10 = a0 + MEMORY.ref(2, s0_0).offset(0x00L).get();
      sp18 = v1 + MEMORY.ref(2, s0_0).offset(0x02L).get();
      sp12 = a0 + MEMORY.ref(2, s0_0).offset(0x08L).get();
      sp1a = v1 + MEMORY.ref(2, s0_0).offset(0x0aL).get();
      sp14 = a0 + MEMORY.ref(2, s0_0).offset(0x10L).get();
      sp1c = v1 + MEMORY.ref(2, s0_0).offset(0x12L).get();
      sp16 = a0 + MEMORY.ref(2, s0_0).offset(0x18L).get();
      sp1e = v1 + MEMORY.ref(2, s0_0).offset(0x1aL).get();

      if(s7 == 0) {
        sp20 = sp10;
        sp22 = sp12;
        sp24 = sp14;
        sp26 = sp16;
        sp28 = sp18;
        sp2a = sp1a;
        sp2c = sp1c;
        sp2e = sp1e;
      }

      //LAB_800f2af8
      if(s7 == 0x1L) {
        if(MEMORY.ref(2, s3).offset(0x7cL).getSigned() != 0) {
          v0 = MEMORY.ref(4, s1).offset(0x30L).get() - MEMORY.ref(4, s1).offset(0x2cL).get();
          MEMORY.ref(4, s1).offset(0x30L).setu(v0);
          v0 = (int)v0 >> 16;
          MEMORY.ref(4, s1).offset(0x34L).setu(v0);
          if((int)v0 < 0) {
            MEMORY.ref(4, s1).offset(0x34L).setu(0);
            MEMORY.ref(4, s1).offset(0x30L).setu(0);
            MEMORY.ref(2, s1).offset(0x38L).setu(0);
          }
        } else {
          //LAB_800f2b44
          v0 = MEMORY.ref(4, s1).offset(0x30L).get() + MEMORY.ref(4, s1).offset(0x2cL).get();
          MEMORY.ref(4, s1).offset(0x30L).setu(v0);
          v0 = (int)v0 >> 16;
          MEMORY.ref(4, s1).offset(0x34L).setu(v0);
          if((int)v0 >= 0x80L) {
            MEMORY.ref(4, s1).offset(0x34L).setu(0x7fL);
            MEMORY.ref(4, s1).offset(0x30L).setu(0x7f_0000L);
            MEMORY.ref(2, s1).offset(0x38L).setu(s7);
          }
        }
      }

      //LAB_800f2b80
      a2 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);
      MEMORY.ref(4, a2).offset(0x4L).setu(0x2c80_8080L);
      v0 = MEMORY.ref(1, a2).offset(0x7L).get();
      v0 = v0 | 0x2L;
      v0 = v0 << 24;
      v0 = v0 | 0x80_8080L;
      MEMORY.ref(4, a2).offset(0x4L).setu(v0);
      v0 = MEMORY.ref(1, a2).offset(0x7L).get();
      MEMORY.ref(1, a2).offset(0x3L).setu(0x9L);
      v0 = v0 & 0xfeL;
      v0 = v0 << 24;
      v0 = v0 | 0x80_8080L;
      MEMORY.ref(4, a2).offset(0x4L).setu(v0);

      MEMORY.ref(1, a2).offset(0x4L).setu(MEMORY.ref(1, s0_0).offset(0x34L).get());
      MEMORY.ref(1, a2).offset(0x5L).setu(MEMORY.ref(1, s0_0).offset(0x34L).get());
      MEMORY.ref(1, a2).offset(0x6L).setu(MEMORY.ref(1, s0_0).offset(0x34L).get());

      v0 = 0x800d_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x605aL).get();
      MEMORY.ref(2, a2).offset(0x16L).setu(v0);

      v0 = 0x800d_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x6072L).get();
      MEMORY.ref(2, a2).offset(0xeL).setu(v0);

      MEMORY.ref(2, a2).offset(0x8L).setu(sp10);
      MEMORY.ref(2, a2).offset(0xaL).setu(sp18);
      MEMORY.ref(2, a2).offset(0x10L).setu(sp12);
      MEMORY.ref(2, a2).offset(0x12L).setu(sp1a);
      MEMORY.ref(2, a2).offset(0x18L).setu(sp14);
      MEMORY.ref(2, a2).offset(0x1aL).setu(sp1c);
      MEMORY.ref(2, a2).offset(0x20L).setu(sp16);
      MEMORY.ref(2, a2).offset(0x22L).setu(sp1e);
      MEMORY.ref(1, a2).offset(0xdL).setu(0x40L);
      MEMORY.ref(1, a2).offset(0x15L).setu(0x40L);
      MEMORY.ref(1, a2).offset(0xcL).setu(0xa0L);
      MEMORY.ref(1, a2).offset(0x14L).setu(0xbfL);
      MEMORY.ref(1, a2).offset(0x1cL).setu(0xa0L);
      MEMORY.ref(1, a2).offset(0x1dL).setu(0x5fL);
      MEMORY.ref(1, a2).offset(0x24L).setu(0xbfL);
      MEMORY.ref(1, a2).offset(0x25L).setu(0x5fL);

      if(MEMORY.ref(4, s0_0).offset(0x40L).get() == 0) {
        MEMORY.ref(4, s0_0).offset(0x40L).setu(0x1L);
      }

      //LAB_800f2cb4
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + MEMORY.ref(4, s0_0).offset(0x40L).get() * 0x4L, a2);
      s0_0 = s0_0 + 0x44L;
      s7 = s7 + 0x1L;
    } while((int)s7 < 0x2L);

    fp = 0;
    s3 = 0;
    s1 = 0x800d_0000L;
    s1 = s1 + 0x5630L;
    sp88 = 0x44L;
    sp8c = 0x88L;

    for(int i = 0; i < 8; i++) {
      sp0x38[0] = _800d6c78.get(i).get();
      sp0x48[0] = _800d6c88.get(i).get();
    }

    sp90 = 0xccL;
    sp78 = (short)sp2e - (short)sp28 >>> 1;
    sp80 = (short)sp20 - (short)sp26 >> 1;
    sp68 = (short)sp26 + (short)sp20 >> 1;
    sp6a = (short)sp2e + (short)sp28 >> 1;

    //LAB_800f2de8
    do {
      t4 = 0x800d_0000L;
      t4 = t4 + 0x5630L;
      t6 = 0x800d_0000L;
      t6 = t6 + 0x5630L;
      a0 = sp8c + t4;
      v0 = MEMORY.ref(2, a0).offset(0x0L).get();
      v1 = sp90 + t6;
      MEMORY.ref(2, v1).offset(0x0L).setu(v0);
      v0 = MEMORY.ref(2, a0).offset(0x2L).get();
      t4 = 0x800d_0000L;
      MEMORY.ref(2, v1).offset(0x2L).setu(v0);
      t4 = t4 + 0x5630L;
      v1 = sp88 + t4;
      v0 = MEMORY.ref(2, v1).offset(0x0L).get();

      MEMORY.ref(2, a0).offset(0x0L).setu(v0);
      v0 = MEMORY.ref(2, v1).offset(0x2L).get();

      MEMORY.ref(2, a0).offset(0x2L).setu(v0);
      v0 = MEMORY.ref(2, s1).offset(0x0L).get();

      MEMORY.ref(2, v1).offset(0x0L).setu(v0);
      v0 = MEMORY.ref(2, s1).offset(0x2L).get();

      MEMORY.ref(2, v1).offset(0x2L).setu(v0);
      a0 = MEMORY.ref(4, s1).offset(0x28L).get();
      v0 = rsin(a0);
      v1 = sp0x38[(int)fp];
      t5 = sp80;

      v1 = t5 + v1;
      lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
      a0 = MEMORY.ref(4, s1).offset(0x28L).get();
      v0 = sp68;
      t3 = lo;
      v1 = (int)t3 >> 12;
      v0 = v0 + v1;
      MEMORY.ref(2, s1).offset(0x0L).setu(v0);
      v0 = rcos(a0);
      t4 = sp78;
      a0 = sp0x38[(int)fp];
      v1 = t4 << 16;
      v1 = (int)v1 >> 16;
      v1 = v1 + a0;
      lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
      v0 = sp6a;
      a0 = MEMORY.ref(2, s1).offset(0x38L).getSigned();
      t3 = lo;
      v1 = (int)t3 >> 12;
      v0 = v0 + v1;
      MEMORY.ref(2, s1).offset(0x2L).setu(v0);
      if(a0 != 0) {
        v0 = MEMORY.ref(4, s1).offset(0x30L).get() - MEMORY.ref(4, s1).offset(0x2cL).get();
        MEMORY.ref(4, s1).offset(0x30L).setu(v0);
        v0 = (int)v0 >> 16;
        MEMORY.ref(4, s1).offset(0x34L).setu(v0);

        if((int)v0 < 0) {
          MEMORY.ref(4, s1).offset(0x34L).setu(0);
          MEMORY.ref(4, s1).offset(0x30L).setu(0);
          MEMORY.ref(2, s1).offset(0x38L).setu(0);
        }
      } else {
        //LAB_800f2f0c
        v0 = MEMORY.ref(4, s1).offset(0x30L).get() + MEMORY.ref(4, s1).offset(0x2cL).get();
        MEMORY.ref(4, s1).offset(0x30L).setu(v0);
        v0 = (int)v0 >> 16;
        MEMORY.ref(4, s1).offset(0x34L).setu(v0);

        if((int)v0 >= 0x80L) {
          MEMORY.ref(4, s1).offset(0x34L).setu(0x7fL);
          MEMORY.ref(4, s1).offset(0x30L).setu(0x7f_0000L);
          MEMORY.ref(2, s1).offset(0x38L).setu(0x1L);
        }
      }

      //LAB_800f2f4c
      //LAB_800f2f50
      s4 = 0;
      s2 = linkedListAddress_1f8003d8.get();
      s0_0 = s2 + 0x25L;
      v0 = s2 + 0xa0L;
      linkedListAddress_1f8003d8.setu(v0);

      //LAB_800f2f78
      do {
        t5 = 0x800d_0000L;
        MEMORY.ref(4, s0_0-0x21L).setu(0x2c80_8080L);
        v0 = MEMORY.ref(1, s0_0).offset(-0x1eL).get();
        v0 = v0 | 0x2L;
        v0 = v0 << 24;
        v0 = v0 | 0x80_8080L;
        MEMORY.ref(4, s0_0-0x21L).setu(v0);
        v0 = MEMORY.ref(1, s0_0).offset(-0x1eL).get();
        t5 = t5 + 0x5630L;
        MEMORY.ref(1, s0_0).offset(-0x22L).setu(0x9L);
        v0 = v0 & 0xfeL;
        v0 = v0 << 24;
        v0 = v0 | 0x80_8080L;
        MEMORY.ref(4, s0_0-0x21L).setu(v0);
        v0 = s3 + s4;
        v1 = v0 << 4;
        v1 = v1 + v0;
        v1 = v1 << 2;
        v1 = v1 + t5;

        MEMORY.ref(1, s0_0).offset(-0x21L).setu(MEMORY.ref(1, v1).offset(0x34L).get());
        MEMORY.ref(1, s0_0).offset(-0x20L).setu(MEMORY.ref(1, v1).offset(0x34L).get());
        MEMORY.ref(1, s0_0).offset(-0x1fL).setu(MEMORY.ref(1, v1).offset(0x34L).get());

        v0 = 0x800d_0000L;
        v0 = MEMORY.ref(2, v0).offset(0x6058L).get();
        MEMORY.ref(2, s0_0-0xfL).setu(v0);

        v0 = 0x800d_0000L;
        v0 = MEMORY.ref(2, v0).offset(0x6070L).get();
        MEMORY.ref(2, s0_0-0x17L).setu(v0);

        MEMORY.ref(2, s0_0-0x1dL).setu(MEMORY.ref(2, v1).offset(0x0L).get());
        MEMORY.ref(2, s0_0-0x1bL).setu(MEMORY.ref(2, v1).offset(0x2L).get());
        MEMORY.ref(2, s0_0-0x15L).setu(MEMORY.ref(2, v1).offset(0x0L).get() + 0x6L);
        MEMORY.ref(2, s0_0-0x13L).setu(MEMORY.ref(2, v1).offset(0x2L).get());
        MEMORY.ref(2, s0_0-0x0dL).setu(MEMORY.ref(2, v1).offset(0x0L).get());
        MEMORY.ref(2, s0_0-0x0bL).setu(MEMORY.ref(2, v1).offset(0x2L).get() + 0x6L);
        MEMORY.ref(2, s0_0-0x05L).setu(MEMORY.ref(2, v1).offset(0x0L).get() + 0x6L);
        MEMORY.ref(2, s0_0-0x03L).setu(MEMORY.ref(2, v1).offset(0x2L).get() + 0x6L);

        if((s3 & 0x1L) != 0 || s3 == s3 / 3 * 0x3L) {
          //LAB_800f30d8
          MEMORY.ref(1, s0_0).offset(-0x19L).setu(0xb0L);
          MEMORY.ref(1, s0_0).offset(-0x18L).setu(0x30L);
          MEMORY.ref(1, s0_0).offset(-0x11L).setu(0xb7L);
          MEMORY.ref(1, s0_0).offset(-0x10L).setu(0x30L);
          MEMORY.ref(1, s0_0).offset(-0x9L).setu(0xb0L);
          MEMORY.ref(1, s0_0).offset(-0x8L).setu(0x37L);
          MEMORY.ref(1, s0_0).offset(-0x1L).setu(0xb7L);
        } else {
          MEMORY.ref(1, s0_0).offset(-0x19L).setu(0xb8L);
          MEMORY.ref(1, s0_0).offset(-0x18L).setu(0x30L);
          MEMORY.ref(1, s0_0).offset(-0x11L).setu(0xbfL);
          MEMORY.ref(1, s0_0).offset(-0x10L).setu(0x30L);
          MEMORY.ref(1, s0_0).offset(-0x9L).setu(0xb8L);
          MEMORY.ref(1, s0_0).offset(-0x8L).setu(0x37L);
          MEMORY.ref(1, s0_0).offset(-0x1L).setu(0xbfL);
        }

        //LAB_800f30f4
        MEMORY.ref(1, s0_0).offset(0x0L).setu(0x37L);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0xa4L, s2);
        s0_0 = s0_0 + 0x28L;
        s2 = s2 + 0x28L;
        s4 = s4 + 0x1L;
      } while((int)s4 < 0x4L);

      MEMORY.ref(4, s1).offset(0x28L).addu(sp0x48[(int)fp]).and(0xfffL);

      sp88 = sp88 + 0x110L;
      sp8c = sp8c + 0x110L;
      sp90 = sp90 + 0x110L;
      s1 = s1 + 0x110L;
      s3 = s3 + 0x4L;
      fp = fp + 0x1L;
    } while((int)fp < 0x8L);

    v1 = 0x8010_0000L;
    v0 = MEMORY.ref(4, v1).offset(-0x615cL).get();
    v0 = v0 + 0x1L;
    MEMORY.ref(4, v1).offset(-0x615cL).setu(v0);
  }

  @Method(0x800f31bcL)
  public static void FUN_800f31bc() {
    getScreenOffset(_800c69fc.deref(4).offset(0x10).cast(IntRef::new), _800c69fc.deref(4).offset(0x14).cast(IntRef::new));

    if(gameState_800babc8._4e3.get() != 0) {
      return;
    }

    if(_800bb168.get() != 0) {
      return;
    }

    final long indicatorMode = gameState_800babc8.indicatorMode_4e8.get();
    if(indicatorMode != 1) {
      _800f9e9c.setu(0);
    }

    //LAB_800f321c
    if((joypadPress_8007a398.get() & 0x8L) != 0) {
      if(indicatorMode == 0) {
        gameState_800babc8.indicatorMode_4e8.set(1);
        //LAB_800f3244
      } else if(indicatorMode == 1) {
        gameState_800babc8.indicatorMode_4e8.set(2);
      } else if(indicatorMode == 2) {
        gameState_800babc8.indicatorMode_4e8.set(0);
        _800f9e9c.setu(0);
      }
      //LAB_800f3260
    } else if((joypadPress_8007a398.get() & 0x4) != 0) {
      if(indicatorMode == 0) {
        //LAB_800f3274
        gameState_800babc8.indicatorMode_4e8.set(2);
        //LAB_800f3280
      } else if(indicatorMode == 1) {
        gameState_800babc8.indicatorMode_4e8.set(0);
        _800f9e9c.setu(0);
        //LAB_800f3294
      } else if(indicatorMode == 2) {
        gameState_800babc8.indicatorMode_4e8.set(1);

        //LAB_800f32a4
        _800f9e9c.setu(0);
      }
    }

    //LAB_800f32a8
    //LAB_800f32ac
    if(gameState_800babc8.indicatorMode_4e8.get() == 0) {
      return;
    }

    final long sp140 = _800d6cb8.offset(4, 0x0L).get();
    final long sp144 = _800d6cb8.offset(4, 0x4L).get();
    final long sp148 = _800d6cb8.offset(4, 0x8L).get();
    final long sp14c = _800d6cb8.offset(4, 0xcL).get();

    final long scriptStateAddr = scriptStatePtrArr_800bc1c0.get(scriptStateIndices_800c6880.get(0).get()).deref().getAddress();
    final Memory.TemporaryReservation scriptStateTmpRes = MEMORY.temp(0x100);
    final Value scriptStateTmp = scriptStateTmpRes.get();
    final ScriptState<?> sp10 = new ScriptState<>(scriptStateTmp, BigStruct::new); //NOTE: pointer to inner struct is copied to temp - temp inner struct will ref source struct

    //LAB_800f3328
    for(int i = 0; i < 0x10; i++) {
      scriptStateTmp.offset(i * 0x10L).offset(0x0L).setu(MEMORY.ref(4, scriptStateAddr).offset(i * 0x10L).offset(0x0L));
      scriptStateTmp.offset(i * 0x10L).offset(0x4L).setu(MEMORY.ref(4, scriptStateAddr).offset(i * 0x10L).offset(0x4L));
      scriptStateTmp.offset(i * 0x10L).offset(0x8L).setu(MEMORY.ref(4, scriptStateAddr).offset(i * 0x10L).offset(0x8L));
      scriptStateTmp.offset(i * 0x10L).offset(0xcL).setu(MEMORY.ref(4, scriptStateAddr).offset(i * 0x10L).offset(0xcL));
    }

    final MATRIX ls = new MATRIX();
    GsGetLs(sp10.innerStruct_00.derefAs(BigStruct.class).coord2_14, ls);

    //TODO what's up with all the unused vars? Did I miss something?

    PushMatrix();
    CPU.CTC2(ls.getPacked(0), 0); //
    CPU.CTC2(ls.getPacked(2), 1); //
    CPU.CTC2(ls.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(ls.getPacked(6), 3); //
    CPU.CTC2(ls.getPacked(8), 4); //
    CPU.CTC2(ls.transfer.getX(), 5); //
    CPU.CTC2(ls.transfer.getY(), 6); // Translation vector
    CPU.CTC2(ls.transfer.getZ(), 7); //
    CPU.MTC2(sp140, 0); // Vector XY 0
    CPU.MTC2(sp144, 1); // Vector Z 0
    CPU.COP2(0x180001L); // Perspective transform
    long sp150 = CPU.MFC2(14); // Screen XY 2
    long sp160 = CPU.MFC2(8); // IR0 - 16-bit accumulator (interpolate)
    long sp164 = CPU.CFC2(31); // LZCR - count of leading 1's
    long sp168 = CPU.MFC2(19) / 0x4L; // Screen Z 3

    CPU.MTC2(sp148, 0); // Vector XY 0
    CPU.MTC2(sp14c, 1); // Vector Z 0
    CPU.COP2(0x180001L); // Perspective transform
    final long sp158 = CPU.MFC2(14); // Screen XY 2
    sp160 = CPU.MFC2(8); // IR0 - 16-bit accumulator (interpolate)
    sp164 = CPU.CFC2(31); // LZCR - count of leading 1's
    sp168 = CPU.MFC2(19) / 0x4L; // Screen Z 3

    final long sp110 = (int)(-0x30L - ((sp158 & 0xffff_0000L) - (sp150 & 0xffff_0000L)) << 16);
    final long sp114 = 0; //2b
    sp150 = (int)((sp158 & 0xffff_0000L) - (sp150 & 0xffff_0000L) << 16);
    CPU.MTC2(sp110, 0); // Vector XY 0
    CPU.MTC2(sp114, 1); // Vector Z 0
    CPU.COP2(0x180001L); // Perspective transform
    final long sp118 = CPU.MFC2(14); // Screen XY 2
    sp160 = CPU.MFC2(8); // IR0 - 16-bit accumulator (interpolate)
    sp164 = CPU.CFC2(31); // LZCR - count of leading 1's
    sp168 = CPU.MFC2(19) / 0x4L; // Screen Z 3
    PopMatrix();

    _800c69fc.deref(4).offset(0x8L).setu(sp118 & 0xffffL);
    _800c69fc.deref(4).offset(0xcL).setu(sp118 >>> 16);

    if(gameState_800babc8.indicatorMode_4e8.get() == 0x1L) {
      if(_800f9e9c.get() < 0x21L) {
        renderTriangleIndicators();
        _800f9e9c.addu(0x1L);
      }
      //LAB_800f3508
    } else if(gameState_800babc8.indicatorMode_4e8.get() == 0x2L) {
      renderTriangleIndicators();
    }

    //LAB_800f3518
    scriptStateTmpRes.release();
  }

  @Method(0x800f352cL)
  public static void renderTriangleIndicators() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s6;
    long s7;
    final long[] sp0x10 = new long[8];
    final long[] sp0x20 = new long[3];
    final long[] sp0x30 = new long[3];

    long t0 = -0x2L;
    final long s5 = _800c69fc.get();
    long sp48 = -0x1L;
    long sp4c = 0;

    for(int i = 0; i < 4; i++) {
      sp0x10[i] = _800d6cc8.get(i).get();
    }

    //LAB_800f35b0
    for(int i = 0; i < 21; i++) {
      s1 = _800d4ff0.offset(sp4c).getAddress();
      if(i == 0) {
        v0 = MEMORY.ref(4, s5).offset(0x8L).get();
        MEMORY.ref(4, s1).offset(0x34L).setu(v0);
        v0 = MEMORY.ref(4, s5).offset(0xcL).get() - 0x1cL;
        MEMORY.ref(4, s1).offset(0x38L).setu(v0);

        a0 = anmStruct_800d5588.anm_00.getPointer(); //TODO
        a2 = anmStruct_800d5588.spriteGroup_04.getPointer(); //TODO
      } else {
        //LAB_800f35f4
        if(MEMORY.ref(2, s5).offset(0x18L).offset(t0).getSigned() < 0) {
          break;
        }

        v1 =
          MEMORY.ref(4, s5).offset(0x10L).get() -
          MEMORY.ref(4, s5).offset(0x90L).offset(sp48 * 0x4L).get() +
          MEMORY.ref(2, s5).offset(0x40L).offset(t0).getSigned() -
          0x2L;
        MEMORY.ref(4, s1).offset(0x34L).setu(v1);
        a0 =
          MEMORY.ref(4, s5).offset(0x14L).get() -
          MEMORY.ref(4, s5).offset(0xe0L).offset(sp48 * 0x4L).get() +
          MEMORY.ref(2, s5).offset(0x68L).offset(t0).getSigned() -
          0x20L;
        MEMORY.ref(4, s1).offset(0x38L).setu(a0);

        a0 = anmStruct_800d5588._08.get();
        a2 = anmStruct_800d5588._0c.get();
      }

      //LAB_800f365c
      if((MEMORY.ref(4, s1).get() & 0x1L) == 0) {
        MEMORY.ref(4, s1).offset(0x8L).subu(0x1L);

        if(MEMORY.ref(4, s1).offset(0x8L).getSigned() < 0) {
          MEMORY.ref(4, s1).offset(0x4L).addu(0x1L);

          if(MEMORY.ref(4, s1).offset(0x14L).get() < MEMORY.ref(4, s1).offset(0x4L).get()) {
            MEMORY.ref(4, s1).offset(0x4L).setu(MEMORY.ref(4, s1).offset(0x10L).get());
            MEMORY.ref(4, s1).offset(0xcL).addu(0x1L);
          }

          //LAB_800f36b0
          MEMORY.ref(4, s1).offset(0x8L).setu(MEMORY.ref(1, a0).offset(MEMORY.ref(4, s1).offset(0x4L).get() * 0x8L).offset(0x8L).offset(0x2L).get() - 0x1L);
        }
      }

      //LAB_800f36d0
      v1 = a0 + MEMORY.ref(4, a2).offset(MEMORY.ref(2, a0).offset(0x8L).offset(MEMORY.ref(4, s1).offset(0x4L).get() * 0x8L).get() * 0x4L).get();
      s7 = MEMORY.ref(1, v1).get();
      s6 = 0;
      v1 = v1 + s7 * 0x14L;
      if(s7 != 0) {
        s4 = v1 - 0x10L;
        s2 = s4 + 0xaL;

        //LAB_800f3724
        do {
          s0 = linkedListAddress_1f8003d8.get();
          linkedListAddress_1f8003d8.addu(0x28L);

          MEMORY.ref(1, s0).offset(0x3L).setu(0x9L);
          MEMORY.ref(4, s0).offset(0x4L).setu(0x2c80_8080L);
          a3 = MEMORY.ref(2, s2).offset(-0x6L).get();
          MEMORY.ref(1, s0).offset(0x7L).oru((a3 & 0x8000L) >>> 14);
          MEMORY.ref(1, s0).offset(0x4L).setu(MEMORY.ref(1, s1).offset(0x24L).get());
          a3 = a3 >>> 6;
          MEMORY.ref(1, s0).offset(0x5L).setu(MEMORY.ref(1, s1).offset(0x25L).get());
          s3 = a3 & 0x1ffL;
          MEMORY.ref(1, s0).offset(0x6L).setu(MEMORY.ref(1, s1).offset(0x26L).get());
          v0 = MEMORY.ref(4, s1).offset(0x34L).get() - (MEMORY.ref(2, s2).offset(-0x2L).get() >>> 1);
          a1 = v0 + MEMORY.ref(2, s2).offset(-0x2L).get();
          MEMORY.ref(2, s0).offset(0xaL).setu(MEMORY.ref(4, s1).offset(0x38L).get());
          MEMORY.ref(2, s0).offset(0x12L).setu(MEMORY.ref(4, s1).offset(0x38L).get());
          v1 = MEMORY.ref(4, s1).offset(0x38L).get() + MEMORY.ref(2, s2).offset(0x0L).get();
          MEMORY.ref(2, s0).offset(0x8L).setu(v0);
          MEMORY.ref(2, s0).offset(0x10L).setu(a1);
          MEMORY.ref(2, s0).offset(0x18L).setu(v0);
          MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
          MEMORY.ref(2, s0).offset(0x20L).setu(a1);
          MEMORY.ref(2, s0).offset(0x22L).setu(v1);
          MEMORY.ref(2, s0).offset(0x16L).setu(MEMORY.ref(2, s1).offset(0x18L).get() | MEMORY.ref(2, s2).offset(-0x4L).get() & 0x60L);
          if(i == 0) {
            v0 = 0x800d_0000L;
            t3 = v0 + 0x6cd8L;
            sp0x20[0] = MEMORY.ref(4, t3).offset(0x0L).get();
            sp0x20[1] = MEMORY.ref(4, t3).offset(0x4L).get();
            sp0x20[2] = MEMORY.ref(4, t3).offset(0x8L).get();
            v0 = 0x800d_0000L;
            t3 = v0 + 0x6ce4L;
            sp0x30[0] = MEMORY.ref(4, t3).offset(0x0L).get();
            sp0x30[1] = MEMORY.ref(4, t3).offset(0x4L).get();
            sp0x30[2] = MEMORY.ref(4, t3).offset(0x8L).get();
            v0 = getEncounterTriangleColour();
            v1 = MEMORY.ref(1, s0).offset(0x7L).get() & 0xfdL;
            v1 = v1 << 24;
            v1 = v1 | 0x80_8080L;
            MEMORY.ref(4, s0).offset(0x4L).setu(v1);
            v1 = s3 - sp0x30[(int)v0];
            v1 = v1 << 6;
            v0 = sp0x20[(int)v0] & 0x3f0L;
            v0 = (int)v0 >> 4;
            v1 = v1 | v0;
            MEMORY.ref(2, s0).offset(0xeL).setu(v1);
          } else {
            //LAB_800f3884
            v0 = s5 + t0;

            v0 = sp0x10[(int)MEMORY.ref(2, v0).offset(0x18L).getSigned()];

            v0 = s3 - v0;
            v0 = v0 << 6;
            v0 = v0 | 0x3eL;
            MEMORY.ref(2, s0).offset(0xeL).setu(v0);
          }

          //LAB_800f38b0
          MEMORY.ref(1, s0).offset(0x0cL).setu(MEMORY.ref(1, s1).offset( 0x1cL).get() + MEMORY.ref(1, s4).offset( 0x00L).get());
          MEMORY.ref(1, s0).offset(0x0dL).setu(MEMORY.ref(1, s1).offset( 0x20L).get() + MEMORY.ref(1, s2).offset(-0x09L).get());
          MEMORY.ref(1, s0).offset(0x14L).setu(MEMORY.ref(1, s2).offset(-0x02L).get() + MEMORY.ref(1, s1).offset( 0x1cL).get() + MEMORY.ref(1, s4).offset( 0x0L).get() + 0xffL);
          MEMORY.ref(1, s0).offset(0x15L).setu(MEMORY.ref(1, s1).offset( 0x20L).get() + MEMORY.ref(1, s2).offset(-0x09L).get());
          MEMORY.ref(1, s0).offset(0x1cL).setu(MEMORY.ref(1, s1).offset( 0x1cL).get() + MEMORY.ref(1, s4).offset( 0x00L).get());
          MEMORY.ref(1, s0).offset(0x1dL).setu(MEMORY.ref(1, s2).offset( 0x00L).get() + MEMORY.ref(1, s1).offset( 0x20L).get() + MEMORY.ref(1, s2).offset(-0x9L).get() + 0xffL);
          MEMORY.ref(1, s0).offset(0x24L).setu(MEMORY.ref(1, s2).offset(-0x02L).get() + MEMORY.ref(1, s1).offset( 0x1cL).get() + MEMORY.ref(1, s4).offset( 0x0L).get() + 0xffL);
          MEMORY.ref(1, s0).offset(0x25L).setu(MEMORY.ref(1, s2).offset( 0x00L).get() + MEMORY.ref(1, s1).offset( 0x20L).get() + MEMORY.ref(1, s2).offset(-0x9L).get() + 0xffL);

          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x98L, s0);
          s4 = s4 - 0x14L;
          s2 = s2 - 0x14L;
          s6 = s6 + 0x1L;
        } while((int)s6 < (int)s7);
      }

      //LAB_800f39a8
      t0 = t0 + 0x2L;
      sp48 = sp48 + 0x1L;
      sp4c = sp4c + 0x44L;
    }

    //LAB_800f39d0
  }

  @Method(0x800f3a00L)
  public static long getEncounterTriangleColour() {
    final long acc = encounterAccumulator_800c6ae8.get();

    if(acc <= 0xa00L) {
      return 0;
    }

    //LAB_800f3a20
    if(acc <= 0xf00L) {
      //LAB_800f3a40
      return 0x1L;
    }

    //LAB_800f3a34
    return 0x2L;
  }

  @Method(0x800f3a48L)
  public static void loadSavePointAnimations() {
    parseAnmFile(mrg_800d6d1c.getFile(0, AnmFile::new), anmStruct_800d5588);
    parseAnmFile(mrg_800d6d1c.getFile(1, AnmFile::new), anmStruct_800d5590);
    FUN_800f3b64(mrg_800d6d1c.getFile(0, AnmFile::new), mrg_800d6d1c.getFile(1, AnmFile::new), _800d4ff0.getAddress(), 0x15L);
  }

  @Method(0x800f3abcL)
  public static void FUN_800f3abc() {
    FUN_800f31bc();

    if(_800d5620.get() == 0x1L) {
      FUN_800f28d8();
    }
  }

  @Method(0x800f3af8L)
  public static void FUN_800f3af8() {
    if(gameState_800babc8.indicatorMode_4e8.get() > 0) {
      _800f9e9c.setu(0);
    }

    //LAB_800f3b14
    long v1 = 0x13L;
    long v0 = _800c69fc.get() + 0x26L;

    //LAB_800f3b24
    do {
      MEMORY.ref(2, v0).offset(0x18L).setu(-0x1L);
      v1--;
      v0 -= 0x2L;
    } while((int)v1 >= 0);
  }

  @Method(0x800f3b64L)
  public static void FUN_800f3b64(final AnmFile anm1, final AnmFile anm2, final long a2, final long count) {
    long t2 = a2;

    //LAB_800f3bc4
    for(int i = 0; i < count; i++) {
      final AnmFile anm;
      if(i == 0) {
        MEMORY.ref(2, t2).offset(0x18L).setu(_800d6050);
        MEMORY.ref(2, t2).offset(0x1aL).setu(_800d6068);
        MEMORY.ref(4, t2).offset(0x1cL).setu(_800d6ca8);
        MEMORY.ref(4, t2).offset(0x20L).setu(_800d6cac);
        anm = anm1;
      } else {
        //LAB_800f3bfc
        MEMORY.ref(2, t2).offset(0x18L).setu(_800d6052);
        MEMORY.ref(2, t2).offset(0x1aL).setu(_800d606a);
        MEMORY.ref(4, t2).offset(0x1cL).setu(_800d6cb0);
        MEMORY.ref(4, t2).offset(0x20L).setu(_800d6cb4);
        anm = anm2;
      }

      //LAB_800f3c24
      MEMORY.ref(4, t2).offset(0x00L).setu(0);
      MEMORY.ref(4, t2).offset(0x04L).setu(0);
      MEMORY.ref(4, t2).offset(0x08L).setu(anm.getSequences().get(0).time_02.get());
      MEMORY.ref(4, t2).offset(0x0cL).setu(0);
      MEMORY.ref(4, t2).offset(0x10L).setu(0);
      MEMORY.ref(4, t2).offset(0x14L).setu(anm.n_sequence_06.get() - 0x1L);

      MEMORY.ref(1, t2).offset(0x24L).setu(0x80L);
      MEMORY.ref(1, t2).offset(0x25L).setu(0x80L);
      MEMORY.ref(1, t2).offset(0x26L).setu(0x80L);
      MEMORY.ref(4, t2).offset(0x28L).setu(0x1000L);
      MEMORY.ref(4, t2).offset(0x2cL).setu(0x1000L);
      MEMORY.ref(4, t2).offset(0x30L).setu(0);
      MEMORY.ref(4, t2).offset(0x34L).setu(0);
      MEMORY.ref(4, t2).offset(0x38L).setu(0);
      MEMORY.ref(4, t2).offset(0x3cL).setu(0);

      t2 += 0x44L;
    }

    //LAB_800f3c88
  }

  @Method(0x800f3b3cL)
  public static void FUN_800f3b3c() {
    FUN_80020fe0(_800d5eb0);
    _800d5620.setu(0);
  }

  @Method(0x800f3c98L)
  public static void parseAnmFile(final AnmFile anmFile, final AnmStruct a1) {
    a1.anm_00.set(anmFile);
    a1.spriteGroup_04.set(anmFile.getSpriteGroups());
  }

  /** TODO struct */
  @Method(0x800f3cb8L)
  public static void FUN_800f3cb8() {
    long s1 = _800d5eb0.ui_198.get();

    //LAB_800f3ce8
    while(s1 != 0) {
      if(MEMORY.ref(2, s1).offset(0x8L).getSigned() == 0) {
        if(MEMORY.ref(2, s1).offset(0x2L).getSigned() % MEMORY.ref(2, s1).offset(0x4L).getSigned() == 0) {
          final long s0 = addToLinkedListTail(0x3cL);

          MEMORY.ref(2, s0).offset(0x2L).setu(0);
          MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, s1).offset(0x6L));
          MEMORY.ref(2, s0).offset(0xcL).setu(MEMORY.ref(2, s1).offset(0x24L));
          MEMORY.ref(2, s0).offset(0xeL).setu(MEMORY.ref(2, s1).offset(0x28L));
          MEMORY.ref(4, s0).offset(0x10L).setu((MEMORY.ref(4, s1).offset(0x1cL).get() << 16) - (simpleRand() * MEMORY.ref(2, s1).offset(0x18L).get() & 0xffff_ffffL));
          MEMORY.ref(4, s0).offset(0x14L).setu(MEMORY.ref(4, s1).offset(0x20L).get() << 16);
          MEMORY.ref(4, s0).offset(0x1cL).setu(0x8_0000L / MEMORY.ref(4, s1).offset(0xcL).get());
          MEMORY.ref(4, s0).offset(0x20L).setu((MEMORY.ref(4, s1).offset(0x14L).get() << 16) / MEMORY.ref(2, s1).offset(0x6L).get());
          MEMORY.ref(4, s0).offset(0x24L).setu(MEMORY.ref(4, s1).offset(0x10L).get() << 16);
          MEMORY.ref(2, s0).offset(0x28L).setu(0);
          MEMORY.ref(4, s0).offset(0x2cL).setu(0x80_0000L / MEMORY.ref(2, s0).offset(0x6L).get());
          MEMORY.ref(4, s0).offset(0x30L).setu(0x80_0000L);
          MEMORY.ref(4, s0).offset(0x34L).setu(MEMORY.ref(4, s1).offset(0x2cL));
          MEMORY.ref(4, s0).offset(0x38L).setu(_800d5eb0.vec_160.x.get());

          _800d5eb0.vec_160.x.set((int)s0); //TODO this ain't right
        }

        //LAB_800f3df4
        MEMORY.ref(2, s1).offset(0x2L).addu(0x1L);
      } else {
        //LAB_800f3e08
        if(MEMORY.ref(2, s1).offset(0x2L).getSigned() >= MEMORY.ref(2, s1).offset(0x8L).getSigned()) {
          if(MEMORY.ref(2, s1).offset(0x2L).getSigned() % MEMORY.ref(2, s1).offset(0x4L).getSigned() == 0) {
            final long s0 = addToLinkedListHead(0x3cL);

            MEMORY.ref(2, s0).offset(0x2L).setu(0);
            MEMORY.ref(2, s0).offset(0x6L).setu(MEMORY.ref(2, s1).offset(0x6L));
            MEMORY.ref(2, s0).offset(0xcL).setu(MEMORY.ref(2, s1).offset(0x24L));
            MEMORY.ref(2, s0).offset(0xeL).setu(MEMORY.ref(2, s1).offset(0x28L));
            MEMORY.ref(4, s0).offset(0x10L).setu((MEMORY.ref(4, s1).offset(0x1cL).get() << 16) - simpleRand() * MEMORY.ref(2, s1).offset(0x18L).getSigned());
            MEMORY.ref(4, s0).offset(0x14L).setu(MEMORY.ref(4, s1).offset(0x20L).get() << 16);
            MEMORY.ref(4, s0).offset(0x1cL).setu(0x8_0000L / MEMORY.ref(4, s1).offset(0xcL).get());
            MEMORY.ref(4, s0).offset(0x20L).setu((MEMORY.ref(4, s1).offset(0x14L).get() << 16) / MEMORY.ref(2, s1).offset(0x6L).getSigned());
            MEMORY.ref(4, s0).offset(0x24L).setu(MEMORY.ref(4, s1).offset(0x10L).get() << 16);
            MEMORY.ref(2, s0).offset(0x28L).setu(0);
            MEMORY.ref(4, s0).offset(0x2cL).setu(0x80_0000L / MEMORY.ref(2, s0).offset(0x6L).getSigned());
            MEMORY.ref(4, s0).offset(0x30L).setu(0x80_0000L);
            MEMORY.ref(4, s0).offset(0x34L).setu(MEMORY.ref(4, s1).offset(0x2cL));
            MEMORY.ref(4, s0).offset(0x38L).setu(_800d5eb0.vec_160.x.get());

            _800d5eb0.vec_160.x.set((int)s0); //TODO this ain't right
          }
        }

        //LAB_800f3f14
        MEMORY.ref(2, s1).offset(0x2L).addu(0x1L);

        if(MEMORY.ref(2, s1).offset(0x2L).getSigned() >= MEMORY.ref(2, s1).offset(0xaL).getSigned()) {
          MEMORY.ref(2, s1).offset(0x2L).setu(0);
        }
      }

      //LAB_800f3f3c
      s1 = MEMORY.ref(4, s1).offset(0x30L).get();
    }

    //LAB_800f3f4c
  }

  @Method(0x800f3f68L)
  public static void FUN_800f3f68() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long sp10;
    long sp12;
    long sp18;
    long sp1a;

    //TODO struct with a pointer to itself
    long s1 = _800d5eb0.s_128.getAddress();
    long s0 = MEMORY.ref(4, s1).offset(0x38L).get();

    final IntRef sox = new IntRef();
    final IntRef soy = new IntRef();
    getScreenOffset(sox, soy);
    final int sp20 = sox.get();
    final int sp24 = soy.get();

    //LAB_800f3fb0
    while(s0 != 0) {
      if(MEMORY.ref(2, s0).offset(0x2L).getSigned() >= MEMORY.ref(2, s0).offset(0x6L).getSigned()) {
        MEMORY.ref(4, s1).offset(0x38L).setu(MEMORY.ref(4, s0).offset(0x38L));
        removeFromLinkedList(s0);
        s0 = MEMORY.ref(4, s1).offset(0x38L).get();
      } else {
        //LAB_800f3fe8
        a1 = MEMORY.ref(4, s0).offset(0x24L).get();
        v0 = MEMORY.ref(4, s0).offset(0x20L).get();
        a3 = MEMORY.ref(4, s0).offset(0x14L).get();
        v1 = MEMORY.ref(2, s0).offset(0x12L).getSigned();
        a2 = MEMORY.ref(2, s0).offset(0xeL).get();
        a1 += v0;
        v0 = MEMORY.ref(4, s0).offset(0x1cL).get();
        MEMORY.ref(4, s0).offset(0x24L).setu(a1);
        a1 >>= 16;
        MEMORY.ref(2, s0).offset(0x28L).setu(a1);
        a1 >>= 1;
        a3 -= v0;
        MEMORY.ref(4, s0).offset(0x14L).setu(a3);
        a0 = sp20;
        v0 = MEMORY.ref(2, s0).offset(0xcL).get();
        a3 >>= 16;
        a0 -= v0;
        a0 += v1;
        v0 = a0 - a1;
        v1 = sp24;
        a0 += a1;
        sp10 = v0;
        sp12 = a0;
        v1 -= a2;
        v1 += a3;
        v0 = v1 - a1;
        v1 += a1;
        sp18 = v0;
        sp1a = v1;
        v1 = MEMORY.ref(4, s0).offset(0x30L).get() - MEMORY.ref(4, s0).offset(0x2cL).get();
        v0 = v1 >> 16;
        MEMORY.ref(4, s0).offset(0x30L).setu(v1);
        if(v0 > 0x80L) {
          a2 = 0;
        } else {
          a2 = v0;
        }

        //LAB_800f4084
        a1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, a1).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, a1).offset(0x4L).setu(0x2c80_8080L);
        MEMORY.ref(4, a1).offset(0x4L).setu((MEMORY.ref(1, a1).offset(0x7L).get() | 0x2L) << 24 | 0x80_8080L);
        MEMORY.ref(4, a1).offset(0x4L).setu(MEMORY.ref(1, a1).offset(0x7L).get(0xfeL) << 24 | 0x80_8080L);
        MEMORY.ref(1, a1).offset(0x4L).setu(a2);
        MEMORY.ref(1, a1).offset(0x5L).setu(a2);
        MEMORY.ref(1, a1).offset(0x6L).setu(a2);
        MEMORY.ref(2, a1).offset(0x8L).setu(sp10);
        MEMORY.ref(2, a1).offset(0xaL).setu(sp18);
        MEMORY.ref(1, a1).offset(0xcL).setu(0x40L);
        MEMORY.ref(1, a1).offset(0xdL).setu(0x20L);
        MEMORY.ref(1, a1).offset(0xeL).setu(_800d5eb0.ambientRed_1ca.get());
        MEMORY.ref(2, a1).offset(0x10L).setu(sp12);
        MEMORY.ref(2, a1).offset(0x12L).setu(sp18);
        MEMORY.ref(1, a1).offset(0x14L).setu(0x5fL);
        MEMORY.ref(1, a1).offset(0x15L).setu(0x20L);
        MEMORY.ref(2, a1).offset(0x16L).setu(_800d5eb0.i_1b0.get() & 0xffffL);
        MEMORY.ref(2, a1).offset(0x18L).setu(sp10);
        MEMORY.ref(2, a1).offset(0x1aL).setu(sp1a);
        MEMORY.ref(1, a1).offset(0x1cL).setu(0x40L);
        MEMORY.ref(1, a1).offset(0x1dL).setu(0x3fL);
        MEMORY.ref(2, a1).offset(0x20L).setu(sp12);
        MEMORY.ref(2, a1).offset(0x22L).setu(sp1a);
        MEMORY.ref(1, a1).offset(0x24L).setu(0x5fL);
        MEMORY.ref(1, a1).offset(0x25L).setu(0x3fL);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)MEMORY.ref(4, s0).offset(0x34L).get()).getAddress(), a1);
        s1 = s0;
        MEMORY.ref(2, s0).offset(0x2L).addu(0x1L);
        s0 = MEMORY.ref(4, s0).offset(0x38L).get();
      }

      //LAB_800f41b0
    }

    //LAB_800f41bc
  }

  @Method(0x800f41dcL)
  public static void FUN_800f41dc() {
    //LAB_800f4204
    while(!struct34_800d6018.parent_30.isNull()) {
      final Struct34 a0 = struct34_800d6018.parent_30.deref();
      struct34_800d6018.parent_30.setNullable(a0.parent_30.derefNullable());
      removeFromLinkedList(a0.getAddress());
    }

    //LAB_800f4224
    FUN_800eed84(_800d5fd8.getAddress());
  }

  @Method(0x800f4244L)
  public static void FUN_800f4244(final long a0, final long a1, final long a2, final TexPageTrans transMode) {
    FUN_8003b8f0(a0);

    final Memory.TemporaryReservation tmp = MEMORY.temp(0x14);
    final WeirdTimHeader tim = new WeirdTimHeader(tmp.get()); // sp+0x10

    //LAB_800f427c
    while(FUN_8003b900(tim) != null) {
      if(tim.clutAddress.get() != 0) {
        MEMORY.ref(2, a2).setu(tim.clutRect.deref().y.get() << 6 | (tim.clutRect.deref().x.get() & 0x3f0L) >> 4);
        LoadImage(tim.clutRect.deref(), tim.clutAddress.get());
      }

      //LAB_800f42d0
      if(tim.imageAddress.get() != 0) {
        MEMORY.ref(2, a1).setu(texPages_800bb110.get(TexPageBpp.values()[(int)(tim.flags.get() & 0b11)]).get(transMode).get(TexPageY.fromY(tim.imageRect.deref().y.get())).get() | (tim.imageRect.deref().x.get() & 0x3c0L) >> 6);
        LoadImage(tim.imageRect.deref(), tim.imageAddress.get());
      }
    }

    tmp.release();

    //LAB_800f4338
  }

  @Method(0x800f4354L)
  public static void FUN_800f4354() {
    if(bigStruct_800c6748.s_128.get() == -1) {
      FUN_800f41dc();

      if(_800f9e60.get() - 0x1L < 0x2L) {
        FUN_800ee20c();
      }

      FUN_800f0514();
    } else {
      FUN_800f3cb8();
      FUN_800f3f68();

      if(_800f9e60.get() == 0x1L) {
        FUN_800ee20c();
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
      FUN_800ee20c();
    }

    //LAB_800f4454
    FUN_800f0514();
  }

  /** Things such as the save point, &lt;!&gt; action icon, encounter icon, etc. */
  @Method(0x800f45f8L)
  public static void loadSmapMedia() {
    if(_800f9eac.getSigned() == -0x1L) {
      _800f9ea8.setu(-0x1L);
    }

    //LAB_800f4624
    final long v1 = _800f9ea8.getSigned();
    if(v1 == 0) {
      //LAB_800f4660
      loadMiscTextures(11);
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)992, (short)288, (short)8, (short)64), 984L, 288L); // Copies the save point texture beside itself
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0x18L);
      _800f9ea8.addu(0x1L);
      _800f9eac.setu(0x1L);
      DrawSync(0);
    } else if(v1 == 0x1L) {
      //LAB_800f4650
      //LAB_800f46d8
      _800d4fe8.setu(0);
      loadSavePointAnimations();
      FUN_800f2788();
      FUN_800f0370();
      _800f9ea8.addu(0x1L);
      _800f9eac.setu(0x2L);
      DrawSync(0);
    } else if(v1 == -0x1L) {
      //LAB_800f4714
      FUN_800f3b3c();
      FUN_800f4420();
      FUN_800f0440();
      _800d4fe8.setu(0);
      _800f9ea8.setu(0);
      _800f9eac.setu(0);
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
      LoadImage(header.imageRect, header.imageAddress.get());

      _800d6050.offset(textureIndex * 0x2L).setu(texPages_800bb110.get(TexPageBpp.values()[(int)(header.flags.get() & 0b11)]).get(miscTextureTransModes_800d6cf0.get(textureIndex).get()).get(TexPageY.fromY(header.imageRect.y.get())).get() | (header.imageRect.x.get() & 0x3c0) >>> 6);
      _800d6068.offset(textureIndex * 0x2L).setu(header.clutRect.y.get() << 6 | (header.clutRect.x.get() & 0x3f0) >>> 4);

      LoadImage(header.clutRect, header.clutAddress.get());
    }

    //LAB_800f48a8
  }
}
