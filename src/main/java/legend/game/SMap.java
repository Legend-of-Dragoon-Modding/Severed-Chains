package legend.game;

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
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.memory.types.VoidRef;
import legend.game.types.AnmFile;
import legend.game.types.AnmStruct;
import legend.game.types.BigStruct;
import legend.game.types.BiggerStruct;
import legend.game.types.DR_MODE;
import legend.game.types.DR_MOVE;
import legend.game.types.DR_TPAGE;
import legend.game.types.EnvironmentFile;
import legend.game.types.EnvironmentStruct;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsOT;
import legend.game.types.GsOT_TAG;
import legend.game.types.GsRVIEW2;
import legend.game.types.MediumStruct;
import legend.game.types.MrgEntry;
import legend.game.types.MrgFile;
import legend.game.types.NewRootEntryStruct;
import legend.game.types.NewRootStruct;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptStruct;
import legend.game.types.SmallerStruct;
import legend.game.types.SomethingStruct;
import legend.game.types.SomethingStruct2;
import legend.game.types.Struct20;
import legend.game.types.Struct54;
import legend.game.types.TmdExtension;
import legend.game.types.UnknownStruct;
import legend.game.types.UnknownStruct2;
import legend.game.types.WeirdTimHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.core.MemoryHelper.getFunctionAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SStrm.FUN_800fb7cc;
import static legend.game.SStrm.FUN_800fb90c;
import static legend.game.SStrm.stopFmv;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.FUN_800136dc;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback08;
import static legend.game.Scus94491BpeSegment.setCallback0c;
import static legend.game.Scus94491BpeSegment.setCallback10;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001ada0;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001c60c;
import static legend.game.Scus94491BpeSegment.FUN_8001e29c;
import static legend.game.Scus94491BpeSegment.FUN_8001eadc;
import static legend.game.Scus94491BpeSegment.FUN_8001f3d0;
import static legend.game.Scus94491BpeSegment._1f8003c0;
import static legend.game.Scus94491BpeSegment._1f8003c4;
import static legend.game.Scus94491BpeSegment._1f8003c8;
import static legend.game.Scus94491BpeSegment._1f8003cc;
import static legend.game.Scus94491BpeSegment._1f8003e8;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._80010544;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateBiggerStruct;
import static legend.game.Scus94491BpeSegment.fillMemory;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020a00;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020b98;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020fe0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021048;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021050;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021058;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021060;
import static legend.game.Scus94491BpeSegment_8002.FUN_800211d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021258;
import static legend.game.Scus94491BpeSegment_8002.FUN_800212d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_800217a4;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218f0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021b08;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022018;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022590;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c150;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.abs;
import static legend.game.Scus94491BpeSegment_8002.getTimerValue;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.srand;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b8f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b900;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
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
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
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
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040b90;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040780;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004ddc0;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
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
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8007._8007a398;
import static legend.game.Scus94491BpeSegment_8007._8007a39c;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc8;
import static legend.game.Scus94491BpeSegment_800b._800bac60;
import static legend.game.Scus94491BpeSegment_800b._800bb0ab;
import static legend.game.Scus94491BpeSegment_800b._800bb0ac;
import static legend.game.Scus94491BpeSegment_800b._800bb0b0;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.submapScene_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb164;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc05c;
import static legend.game.Scus94491BpeSegment_800b._800bc0b8;
import static legend.game.Scus94491BpeSegment_800b._800bd782;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bd818;
import static legend.game.Scus94491BpeSegment_800b._800bda08;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bdc38;
import static legend.game.Scus94491BpeSegment_800b._800bed50;
import static legend.game.Scus94491BpeSegment_800b._800bed54;
import static legend.game.Scus94491BpeSegment_800b._800bed58;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static legend.game.Scus94491BpeSegment_800b._800bf0b4;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0dc;
import static legend.game.Scus94491BpeSegment_800b._800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.biggerStructPtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.matrix_800bed30;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800b.rview2_800bd7e8;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;

public final class SMap {
  private SMap() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SMap.class);

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

  public static final Value _800c672c = MEMORY.ref(4, 0x800c672cL);
  public static final Value _800c6730 = MEMORY.ref(4, 0x800c6730L);
  public static final Value _800c6734 = MEMORY.ref(4, 0x800c6734L);
  public static final Value _800c6738 = MEMORY.ref(2, 0x800c6738L);

  public static final Value _800c673c = MEMORY.ref(4, 0x800c673cL);
  public static final Value _800c6740 = MEMORY.ref(4, 0x800c6740L);

  /** TODO some of the values below might overlap this struct (note: data sizes of below vars don't match up with what's in the struct?) */
  public static final BigStruct bigStruct_800c6748 = MEMORY.ref(4, 0x800c6748L, BigStruct::new);

  public static final Value _800c686c = MEMORY.ref(2, 0x800c686cL);
  public static final Value _800c686e = MEMORY.ref(2, 0x800c686eL);
  public static final Value _800c6870 = MEMORY.ref(2, 0x800c6870L);

  public static final Value mrg0Loaded_800c6874 = MEMORY.ref(4, 0x800c6874L);
  public static final Pointer<MrgFile> mrg0Addr_800c6878 = MEMORY.ref(4, 0x800c6878L, Pointer.deferred(4, MrgFile::new));
  public static final Value _800c687c = MEMORY.ref(2, 0x800c687cL);
  public static final Value _800c687e = MEMORY.ref(2, 0x800c687eL);

  public static final Value index_800c6880 = MEMORY.ref(4, 0x800c6880L);

  public static final Value mrg1Loaded_800c68d0 = MEMORY.ref(4, 0x800c68d0L);

  public static final Pointer<MrgFile> mrg1Addr_800c68d8 = MEMORY.ref(4, 0x800c68d8L, Pointer.deferred(4, MrgFile::new));

  public static final Value mrg10Loaded_800c68e0 = MEMORY.ref(2, 0x800c68e0L);

  public static final Value loadingStage_800c68e4 = MEMORY.ref(4, 0x800c68e4L);

  public static final Value callbackIndex_800c6968 = MEMORY.ref(2, 0x800c6968L);
  public static final Value _800c6970 = MEMORY.ref(4, 0x800c6970L);

  public static final Value _800c69ec = MEMORY.ref(4, 0x800c69ecL);
  public static final Value _800c69f0 = MEMORY.ref(4, 0x800c69f0L);
  public static final Value _800c69f4 = MEMORY.ref(4, 0x800c69f4L);
  public static final Value _800c69f8 = MEMORY.ref(4, 0x800c69f8L);
  public static final Value _800c69fc = MEMORY.ref(4, 0x800c69fcL);
  public static final UnboundedArrayRef<Pointer<ExtendedTmd>> extendedTmdArr_800c6a00 = MEMORY.ref(4, 0x800c6a00L, UnboundedArrayRef.of(4, Pointer.of(4, ExtendedTmd::new)));

  /** Written as int, read as bytes? TODO array */
  public static final Value _800c6a50 = MEMORY.ref(4, 0x800c6a50L);

  public static final Value _800c6aa0 = MEMORY.ref(4, 0x800c6aa0L);
  public static final Value _800c6aa4 = MEMORY.ref(4, 0x800c6aa4L);
  public static final Value _800c6aa8 = MEMORY.ref(4, 0x800c6aa8L);
  public static final Value _800c6aac = MEMORY.ref(2, 0x800c6aacL);

  public static final Value _800c6abc = MEMORY.ref(4, 0x800c6abcL);
  public static final MATRIX matrix_800c6ac0 = MEMORY.ref(4, 0x800c6ac0L, MATRIX::new);
  public static final Value _800c6ae0 = MEMORY.ref(4, 0x800c6ae0L);
  public static final Value _800c6ae4 = MEMORY.ref(4, 0x800c6ae4L);
  public static final Value _800c6ae8 = MEMORY.ref(4, 0x800c6ae8L);
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

  public static final Value _800cb430 = MEMORY.ref(4, 0x800cb430L);

  public static final Value _800cb440 = MEMORY.ref(4, 0x800cb440L);

  public static final Value _800cb448 = MEMORY.ref(4, 0x800cb448L);

  public static final Pointer<NewRootStruct> newRootPtr_800cb458 = MEMORY.ref(4, 0x800cb458L, Pointer.deferred(4, NewRootStruct::new));

  public static final ArrayRef<UnsignedIntRef> arr_800cb460 = MEMORY.ref(4, 0x800cb460L, ArrayRef.of(UnsignedIntRef.class, 0x40, 4, UnsignedIntRef::new));

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

  public static final GsCOORDINATE2 GsCOORDINATE2_800cbda8 = MEMORY.ref(4, 0x800cbda8L, GsCOORDINATE2::new);
  public static final GsDOBJ2 GsDOBJ2_800cbdf8 = MEMORY.ref(4, 0x800cbdf8L, GsDOBJ2::new);
  public static final SomethingStruct SomethingStruct_800cbe08 = MEMORY.ref(4, 0x800cbe08L, SomethingStruct::new);

  public static final Pointer<UnknownStruct2> _800cbe34 = MEMORY.ref(4, 0x800cbe34L, Pointer.deferred(4, UnknownStruct2::new));
  public static final Pointer<UnknownStruct2> _800cbe38 = MEMORY.ref(4, 0x800cbe38L, Pointer.deferred(4, UnknownStruct2::new));

  public static final UnboundedArrayRef<SomethingStruct2> SomethingStruct2Arr_800cbe78 = MEMORY.ref(4, 0x800cbe78L, UnboundedArrayRef.of(0xc, SomethingStruct2::new));

  /** unknown size */
  public static final Value _800cca78 = MEMORY.ref(4, 0x800cca78L);

  public static final TmdWithId tmd_800cfa78 = MEMORY.ref(4, 0x800cfa78L, TmdWithId::new);

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

  public static final Value _800d4f90 = MEMORY.ref(4, 0x800d4f90L);

  public static final Value _800d4fc0 = MEMORY.ref(4, 0x800d4fc0L);

  public static final Value _800d4fd0 = MEMORY.ref(4, 0x800d4fd0L);

  public static final Value _800d4fe0 = MEMORY.ref(4, 0x800d4fe0L);

  public static final Value _800d4fe8 = MEMORY.ref(2, 0x800d4fe8L);

  public static final Value _800d4ff0 = MEMORY.ref(4, 0x800d4ff0L);

  public static final AnmStruct anmStruct_800d5588 = MEMORY.ref(4, 0x800d5588L, AnmStruct::new);
  public static final AnmStruct anmStruct_800d5590 = MEMORY.ref(4, 0x800d5590L, AnmStruct::new);
  public static final Value _800d5598 = MEMORY.ref(2, 0x800d5598L);

  public static final Value _800d5620 = MEMORY.ref(2, 0x800d5620L);

  public static final Value _800d5630 = MEMORY.ref(2, 0x800d5630L);

  public static final BigStruct _800d5eb0 = MEMORY.ref(4, 0x800d5eb0L, BigStruct::new);

  public static final Value _800d6050 = MEMORY.ref(2, 0x800d6050L);
  public static final Value _800d6052 = MEMORY.ref(2, 0x800d6052L);

  public static final Value _800d6068 = MEMORY.ref(2, 0x800d6068L);
  public static final Value _800d606a = MEMORY.ref(2, 0x800d606aL);

  public static final Value _800d610c = MEMORY.ref(2, 0x800d610cL);

  public static final Value timFile_800d689c = MEMORY.ref(4, 0x800d689cL);

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

  public static final Value _800d6c58 = MEMORY.ref(4, 0x800d6c58L);
  public static final Value _800d6c5c = MEMORY.ref(4, 0x800d6c5cL);
  public static final Value _800d6c60 = MEMORY.ref(4, 0x800d6c60L);
  public static final Value _800d6c64 = MEMORY.ref(4, 0x800d6c64L);
  public static final Value _800d6c68 = MEMORY.ref(4, 0x800d6c68L);
  public static final Value _800d6c6c = MEMORY.ref(4, 0x800d6c6cL);
  public static final Value _800d6c70 = MEMORY.ref(4, 0x800d6c70L);
  public static final Value _800d6c74 = MEMORY.ref(4, 0x800d6c74L);

  public static final Value _800d6ca8 = MEMORY.ref(4, 0x800d6ca8L);
  public static final Value _800d6cac = MEMORY.ref(4, 0x800d6cacL);
  public static final Value _800d6cb0 = MEMORY.ref(4, 0x800d6cb0L);
  public static final Value _800d6cb4 = MEMORY.ref(4, 0x800d6cb4L);
  public static final Value _800d6cb8 = MEMORY.ref(4, 0x800d6cb8L);

  public static final Value _800d6cf0 = MEMORY.ref(4, 0x800d6cf0L);
  public static final Value _800d6cf4 = MEMORY.ref(4, 0x800d6cf4L);
  public static final Value _800d6cf8 = MEMORY.ref(4, 0x800d6cf8L);
  public static final Value _800d6cfc = MEMORY.ref(4, 0x800d6cfcL);

  public static final Value _800d6d10 = MEMORY.ref(4, 0x800d6d10L);
  public static final Value _800d6d14 = MEMORY.ref(4, 0x800d6d14L);
  public static final Value _800d6d18 = MEMORY.ref(4, 0x800d6d18L);
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

  /** TODO an array of 0x14-long somethings */
  public static final Value _800f5930 = MEMORY.ref(4, 0x800f5930L);

  /**
   * 65 - {@link SMap#FUN_800eddb4()}
   *
   * All other indices are {@link SMap#FUN_800e4994()}
   */
  public static final ArrayRef<Pointer<RunnableRef>> callbackArr_800f5ad4 = MEMORY.ref(4, 0x800f5ad4L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 0x80, 4, Pointer.of(4, RunnableRef::new)));
  public static final Value _800f5cd4 = MEMORY.ref(2, 0x800f5cd4L);

  public static final Value _800f64ac = MEMORY.ref(4, 0x800f64acL);

  public static final Pointer<GsOT> GsOTPtr_800f64c0 = MEMORY.ref(4, 0x800f64c0L, Pointer.of(4, GsOT::new));
  public static final Value _800f64c4 = MEMORY.ref(1, 0x800f64c4L);

  public static final Value _800f64c6 = MEMORY.ref(1, 0x800f64c6L);

  public static final Value _800f74c4 = MEMORY.ref(1, 0x800f74c4L);

  public static final Value _800f7e24 = MEMORY.ref(4, 0x800f7e24L);
  public static final Pointer<UnknownStruct> _800f7e28 = MEMORY.ref(4, 0x800f7e28L, Pointer.deferred(4, UnknownStruct::new));
  public static final Value _800f7e2c = MEMORY.ref(4, 0x800f7e2cL);
  public static final Value _800f7e30 = MEMORY.ref(4, 0x800f7e30L);

  public static final Value _800f7e4c = MEMORY.ref(4, 0x800f7e4cL);
  public static final Value _800f7e50 = MEMORY.ref(4, 0x800f7e50L);
  public static final Value _800f7e54 = MEMORY.ref(4, 0x800f7e54L);
  public static final Value _800f7e58 = MEMORY.ref(4, 0x800f7e58L);

  public static final Value _800f7f0c = MEMORY.ref(4, 0x800f7f0cL);
  public static final Value _800f7f10 = MEMORY.ref(4, 0x800f7f10L);
  public static final Value _800f7f14 = MEMORY.ref(4, 0x800f7f14L);

  //TODO struct
  public static final Value _800f7f74 = MEMORY.ref(4, 0x800f7f74L);

  public static final Value _800f9374 = MEMORY.ref(4, 0x800f9374L);

  public static final ArrayRef<RECT> rectArray3_800f96f4 = MEMORY.ref(8, 0x800f96f4L, ArrayRef.of(RECT.class, 3, 8, RECT::new));

  public static final Value _800f970c = MEMORY.ref(4, 0x800f970cL);

  public static final Value _800f9718 = MEMORY.ref(2, 0x800f9718L);

  public static final Value drgnFileIndices_800f982c = MEMORY.ref(2, 0x800f982cL);

  public static final Value _800f9e5a = MEMORY.ref(2, 0x800f9e5aL);
  public static final Value _800f9e5c = MEMORY.ref(2, 0x800f9e5cL);
  public static final Value _800f9e5e = MEMORY.ref(2, 0x800f9e5eL);
  public static final Value _800f9e60 = MEMORY.ref(2, 0x800f9e60L);

  public static final Value _800f9e70 = MEMORY.ref(4, 0x800f9e70L);
  public static final Value _800f9e74 = MEMORY.ref(4, 0x800f9e74L);
  public static final Value _800f9e78 = MEMORY.ref(2, 0x800f9e78L);

  public static final Value _800f9e9c = MEMORY.ref(4, 0x800f9e9cL);

  public static final Value _800f9ea8 = MEMORY.ref(4, 0x800f9ea8L);
  public static final Value _800f9eac = MEMORY.ref(4, 0x800f9eacL);
  public static final Value _800f9eb0 = MEMORY.ref(4, 0x800f9eb0L);

  @Method(0x800d9e64L)
  public static void FUN_800d9e64(final GsDOBJ2 dobj2, final long a1) {
    TmdObjTable objTable = dobj2.tmd_08.deref();
    long count = objTable.n_primitive_14.get();
    long primitives = objTable.primitives_10.getPointer();
    long s2 = a1 & 0x7fL;

    //LAB_800d9e90
    while(count != 0) {
      long v1 = MEMORY.ref(4, primitives).get(0xff04_0000L);

      if(v1 == 0x3500_0000L || v1 == 0x3700_0000L) {
        //LAB_800da02c
        FUN_800da7f4(primitives, MEMORY.ref(2, primitives).get(), s2);
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x24L;
      } else if(v1 == 0x3004_0000L || v1 == 0x3204_0000L) {
        //LAB_800d9fc8
        FUN_80021058(primitives, MEMORY.ref(2, primitives).get());
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x1cL;
        //LAB_800d9ef0
      } else if(v1 == 0x3000_0000L || v1 == 0x3200_0000L) {
        //LAB_800d9fe8
        FUN_80021048(primitives, MEMORY.ref(2, primitives).get());
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x14L;
        //LAB_800d9f00
        //LAB_800d9f28
      } else if(v1 == 0x3400_0000L || v1 == 0x3600_0000L) {
        //LAB_800da00c
        FUN_800da6c8(primitives, MEMORY.ref(2, primitives).get(), s2);
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x1cL;
        //LAB_800d9f38
      } else if(v1 == 0x3804_0000L || v1 == 0x3a04_0000L) {
        //LAB_800da050
        FUN_80021060(primitives, MEMORY.ref(2, primitives).get());
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x24L;
        //LAB_800d9f78
      } else if(v1 == 0x3800_0000L || v1 == 0x3a00_0000L) {
        //LAB_800da074
        FUN_80021050(primitives, MEMORY.ref(2, primitives).get());
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x18L;
        //LAB_800d9fac
      } else if(v1 == 0x3c00_0000L || v1 == 0x3e00_0000L) {
        //LAB_800da09c
        FUN_800da754(primitives, MEMORY.ref(2, primitives).get(), s2);
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;
        primitives += v1 * 0x24L;
        //LAB_800d9f88
      } else if(v1 == 0x3d00_0000L || v1 == 0x3f00_0000L) {
        //LAB_800da0c4
        FUN_800da880(primitives, MEMORY.ref(2, primitives).get(), s2);
        v1 = MEMORY.ref(2, primitives).get();
        count -= v1;

        //LAB_800da0e8
        //LAB_800da0ec
        //LAB_800da0f0
        primitives += v1 * 0x2cL;
      }

      //LAB_800da0f4
    }

    //LAB_800da0fc
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

    final long v1 = struct.ub_9c.get();
    if(v1 != 0x2L) {
      if(v1 == 0) {
        if(struct.ub_a2.get() == 0) {
          struct.us_9e.set(struct.us_9a);
        } else {
          //LAB_800da1d0
          struct.us_9e.set((short)struct.us_9a.get() / 2);
        }

        //LAB_800da1e4
        struct.ub_9c.incr();
        struct.ptr_ui_94.set(struct.ptr_ui_90);
      }

      //LAB_800da1f8
      if((struct.us_9e.get() & 0x1L) != 0 || struct.ub_a2.get() != 0) {
        //LAB_800da24c
        FUN_800212d8(struct);
      } else {
        final long s0 = struct.ptr_ui_94.get();

        if(struct.ub_a3.get() == 0) {
          FUN_800da920(struct);
        } else {
          //LAB_800da23c
          FUN_800212d8(struct);
        }

        struct.ptr_ui_94.set(s0);
      }

      //LAB_800da254
      struct.us_9e.decr();

      if(struct.us_9e.get() == 0) {
        struct.ub_9c.set(0);
      }
    }

    //LAB_800da274
  }

  @Method(0x800da288L)
  public static void renderDobj2(final GsDOBJ2 dobj2) {
    final TmdObjTable objTable = dobj2.tmd_08.deref();
    final long vertices = objTable.vert_top_00.get();
    final long normals = objTable.normal_top_08.get();
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_800da2bc
    while(count > 0) {
      final long length = MEMORY.ref(2, primitives).get();
      final long command = MEMORY.ref(4, primitives).get(0xff04_0000L);
      count -= length;

      if(count < 0) {
        LOGGER.warn("renderDobj2 count less than 0! %d", count);
      }

      if(command == 0x3000_0000L) {
        //LAB_800da3e8
        primitives = renderPrimitive3000(primitives, vertices, normals, length);
      } else if(command == 0x3004_0000L) {
        //LAB_800da408
        primitives = renderPrimitive3004(primitives, vertices, normals, length);
      } else if(command == 0x3200_0000L) {
        //LAB_800da3f8
        primitives = renderPrimitive3200(primitives, vertices, normals);
      } else if(command == 0x3204_0000L) {
        //LAB_800da41c
        primitives = renderPrimitive3204(primitives, vertices, normals);
      } else if(command == 0x3400_0000L) {
        //LAB_800da430
        primitives = renderPrimitive34(primitives, vertices, normals, length);
      } else if(command == 0x3500_0000L) {
        //LAB_800da450
        primitives = renderPrimitive35(primitives, vertices, length);
      } else if(command == 0x3600_0000L) {
        //LAB_800da440
        primitives = renderPrimitive36(primitives, vertices, normals);
      } else if(command == 0x3700_0000L) {
        //LAB_800da464
        primitives = renderPrimitive37(primitives, vertices, length);
      } else if(command == 0x3800_0000L) {
        //LAB_800da478
        primitives = renderPrimitive3800(primitives, vertices, normals, length);
      } else if(command == 3804_0000L) {
        //LAB_800da498
        primitives = renderPrimitive3804(primitives, vertices, normals);
      } else if(command == 0x3a00_0000L) {
        //LAB_800da488
        primitives = renderPrimitive3a00(primitives, vertices, normals);
      } else if(command == 0x3a04_0000L) {
        //LAB_800da4ac
        primitives = renderPrimitive3a04(primitives, vertices, normals);
      } else if(command == 0x3c00_0000L) {
        //LAB_800da4c0
        primitives = renderPrimitive3c(primitives, vertices, normals, length);
      } else if(command == 0x3d00_0000L) {
        //LAB_800da4e4
        primitives = renderPrimitive3d(primitives, vertices, length);
      } else if(command == 0x3e00_0000L) {
        //LAB_800da4d0
        primitives = renderPrimitive3e(primitives, vertices, normals);
      } else if(command == 0x3f00_0000L) {
        //LAB_800da4f8
        primitives = renderPrimitive3f(primitives, vertices, length);
      }

      //LAB_800da504
    }

    //LAB_800da50c
  }

  @Method(0x800da524L)
  public static void FUN_800da524(BigStruct param_1) {
    GsInitCoordinate2(param_1.coord2_14, bigStruct_800bda10.coord2_14);

    bigStruct_800bda10.coord2_14.coord.transfer.set(param_1.vector_10c);
    bigStruct_800bda10.us_a0.set(param_1.us_a0.get() + 0x10);

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

    CPU.CTC2(0, (lw.get(1) & 0xffffL) << 16 | lw.get(0) & 0xffffL);
    CPU.CTC2(1, (lw.get(3) & 0xffffL) << 16 | lw.get(2) & 0xffffL);
    CPU.CTC2(2, (lw.get(5) & 0xffffL) << 16 | lw.get(4) & 0xffffL);
    CPU.CTC2(3, (lw.get(7) & 0xffffL) << 16 | lw.get(6) & 0xffffL);
    CPU.CTC2(4,                               lw.get(8) & 0xffffL);
    CPU.CTC2(5, lw.transfer.getX());
    CPU.CTC2(6, lw.transfer.getY());
    CPU.CTC2(7, lw.transfer.getZ());

    renderDobj2(bigStruct_800bda10.ObjTable_0c.top.deref().get(0));
    bigStruct_800bda10.coord2ArrPtr_04.deref().get(0).flg.decr();
  }

  @Method(0x800da6c8L)
  public static void FUN_800da6c8(final long a0, long a1, final long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();
    long a2_0 = a0;

    //LAB_800da6e8
    while(a1 != 0) {
      long v0 = MEMORY.ref(4, a2_0).offset(0x4L).get();
      v0 &= MEMORY.ref(4, a3).offset(0x4L).get();
      v0 |= MEMORY.ref(4, a3).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2_0).offset(0x4L).setu(v0);

      v0 = MEMORY.ref(4, a2_0).offset(0x8L).get();
      v0 &= MEMORY.ref(4, a3).offset(0xcL).get();
      v0 |= MEMORY.ref(4, a3).offset(0x8L).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2_0).offset(0x8L).setu(v0);

      v0 = MEMORY.ref(4, a2_0).offset(0xcL).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2_0).offset(0xcL).setu(v0);
      a2_0 += 0x1cL;
      a1--;
    }

    //LAB_800da74c
  }

  @Method(0x800da754L)
  public static void FUN_800da754(long a0, long a1, long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();
    a2 = a0 + 0x10L;

    //LAB_800da774
    while(a1 != 0) {
      long v0 = MEMORY.ref(4, a2).offset(-0xcL).get();
      v0 &= MEMORY.ref(4, a3).get();
      v0 |= MEMORY.ref(4, a3).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2).offset(-0xcL).setu(v0);

      v0 = MEMORY.ref(4, a2).offset(-0x8L).get();
      v0 &= MEMORY.ref(4, a3).offset(0xcL).get();
      v0 |= MEMORY.ref(4, a3).offset(0x8L).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2).offset(-0x8L).setu(v0);

      MEMORY.ref(4, a2).offset(-0x4L).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, a2).addu(MEMORY.ref(4, a3).offset(0x10L));
      a2 += 0x24L;
      a1--;
    }

    //LAB_800da7ec
  }

  @Method(0x800da7f4L)
  public static void FUN_800da7f4(final long a0, final long a1, final long a2) {
    assert false;
  }

  @Method(0x800da880L)
  public static void FUN_800da880(final long primitive, long a1, final long a2) {
    if(a1 == 0) {
      return;
    }

    long prim = primitive;

    //LAB_800da8a0
    do {
      long v0 = MEMORY.ref(4, prim).offset(0x04L).get();
      v0 &= _800f5930.offset(a2 * 0x14L).offset(0x04L).get();
      v0 |= _800f5930.offset(a2 * 0x14L).offset(0x00L).get();
      v0 += _800f5930.offset(a2 * 0x14L).offset(0x10L).get();
      MEMORY.ref(4, prim).offset(0x04L).setu(v0);

      v0 = MEMORY.ref(4, prim).offset(0x08L).get();
      v0 &= _800f5930.offset(a2 * 0x14L).offset(0x0cL).get();
      v0 |= _800f5930.offset(a2 * 0x14L).offset(0x08L).get();
      v0 += _800f5930.offset(a2 * 0x14L).offset(0x10L).get();
      MEMORY.ref(4, prim).offset(0x08L).setu(v0);

      MEMORY.ref(4, prim).offset(0x0cL).addu(_800f5930.offset(a2 * 0x14L).offset(0x10L));
      MEMORY.ref(4, prim).offset(0x10L).addu(_800f5930.offset(a2 * 0x14L).offset(0x10L));
      prim += 0x2cL;
      a1--;
    } while(a1 != 0);

    //LAB_800da918
  }

  @Method(0x800da920L)
  public static void FUN_800da920(final BigStruct a0) {
    long s4 = a0.ptr_ui_94.get();

    //LAB_800da96c
    for(int i = 0; i < a0.tmdNobj_ca.get(); i++) {
      final MATRIX coord = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref().coord;
      final GsCOORD2PARAM params = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref().param.deref();

      RotMatrix_80040780(params.rotate, coord);

      params.trans.x.add((int)MEMORY.ref(2, s4).offset(0x6L).getSigned());
      params.trans.y.add((int)MEMORY.ref(2, s4).offset(0x8L).getSigned());
      params.trans.z.add((int)MEMORY.ref(2, s4).offset(0xaL).getSigned());
      params.trans.div(2);

      TransMatrix(coord, params.trans);

      s4 += 0xcL;
    }

    //LAB_800daa0c
    a0.ptr_ui_94.set(s4);
  }

  @Method(0x800daa3cL)
  public static void FUN_800daa3c(final BigStruct a0) {
    long s0 = 0x1L;
    long s6 = a0.ui_f4.get();

    _1f8003e8.setu((short)a0.us_a0.get());
    _1f8003ec.setu(a0.ui_108.get());

    //LAB_800daaa8
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      if((s0 & s6) == 0) {
        final GsDOBJ2 dobj2 = a0.ObjTable_0c.top.deref().get(i);

        final MATRIX lw = new MATRIX();
        final MATRIX ls = new MATRIX();
        GsGetLws(dobj2.coord2_04.deref(), lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2((ls.get(1) & 0xffffL) << 16 | ls.get(0) & 0xffffL, 0);
        CPU.CTC2((ls.get(3) & 0xffffL) << 16 | ls.get(2) & 0xffffL, 1);
        CPU.CTC2((ls.get(5) & 0xffffL) << 16 | ls.get(4) & 0xffffL, 2);
        CPU.CTC2((ls.get(7) & 0xffffL) << 16 | ls.get(6) & 0xffffL, 3);
        CPU.CTC2(                              ls.get(8) & 0xffffL, 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        renderDobj2(dobj2);
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
    if(a0.ub_cc.get() != 0) {
      FUN_800da524(a0);
    }

    //LAB_800dab4c
  }

  @Method(0x800dab7cL)
  public static long renderPrimitive3800(final long primitives, final long vertices, final long normals, long length) {
    long s0;
    long s1;
    long s2;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long t8;
    long v0;
    long v1;

    t3 = primitives;
    t1 = linkedListAddress_1f8003d8.get();
    s0 = _1f8003e8.get();

    s2 = _1f8003c0.getAddress();
    s1 = _1f8003c8.getAddress();
    t8 = primitives + 0x14L;
    t0 = t1 + 0x1cL;

    //LAB_800dabc4
    while(length != 0) {
      t5 = vertices + MEMORY.ref(2, t3).offset(0x0aL).get() * 0x8L;
      t6 = vertices + MEMORY.ref(2, t3).offset(0x0eL).get() * 0x8L;
      t7 = vertices + MEMORY.ref(2, t3).offset(0x12L).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
      CPU.COP2(0x28_0030L);
      length--;

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x140_0006L);

        if((int)CPU.MFC2(24) > 0) {
          MEMORY.ref(4, t1).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(13));
          MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(14));
          v0 = vertices + MEMORY.ref(2, t8).offset(0x2L).get() * 0x8L;
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
          CPU.COP2(0x18_0001L);
          t4 = CPU.CFC2(31);
          MEMORY.ref(4, t1).offset(0x20L).setu(CPU.MFC2(14));

          if(MEMORY.ref(2, t0).offset(-0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(0x4L).getSigned() >= -0xc0L) {
            //LAB_800dacc0
            if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(0x6L).getSigned() >= -0x80L) {
              //LAB_800dad10
              if(MEMORY.ref(2, t0).offset(-0x14L).getSigned() <= 0xc0L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() <= 0xc0L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() <= 0xc0L || MEMORY.ref(2, t0).offset(0x4L).getSigned() <= 0xc0L) {
                //LAB_800dad60
                if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() <= 0x80L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() <= 0x80L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() <= 0x80L || MEMORY.ref(2, t0).offset(0x6L).getSigned() <= 0x80L) {
                  //LAB_800dadb0
                  CPU.COP2(0x168_002eL);
                  t2 = CPU.MFC2(7) + s0;
                  v1 = MEMORY.ref(4, s1).offset(0x4L).get();
                  t2 = (int)t2 >> MEMORY.ref(4, s2).offset(0x4L).get();
                  if(t2 >= v1) {
                    t2 = v1;
                  }

                  //LAB_800dade0
                  final GsOT_TAG tag = tags_1f8003d0.deref().get((int)t2);
                  CPU.MTC2(MEMORY.ref(4, t3).offset(0x4L).get(), 6);
                  t5 = normals + MEMORY.ref(2, t3).offset(0x08L).get() * 0x8L;
                  t6 = normals + MEMORY.ref(2, t3).offset(0x0cL).get() * 0x8L;
                  t7 = normals + MEMORY.ref(2, t3).offset(0x10L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
                  CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
                  CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
                  CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
                  CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
                  CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
                  CPU.COP2(0x118_043fL);

                  MEMORY.ref(4, t1).offset(0x04L).setu(CPU.MFC2(20));
                  MEMORY.ref(4, t1).offset(0x0cL).setu(CPU.MFC2(21));
                  MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(22));
                  MEMORY.ref(4, t1).setu(0x800_0000L | tag.p.get());
                  tag.set(t1 & 0xff_ffffL);

                  v0 = normals + MEMORY.ref(2, t8).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                  CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                  CPU.COP2(0x108_041bL);
                  MEMORY.ref(4, t0).setu(CPU.MFC2(22));

                  t0 += 0x24L;
                  t1 += 0x24L;
                }
              }
            }
          }
        }
      }

      //LAB_800dae8c
      t8 += 0x18L;
      t3 += 0x18L;
    }

    //LAB_800dae98
    linkedListAddress_1f8003d8.setu(t1);
    return t3;
  }

  @Method(0x800daeb8L)
  public static long renderPrimitive3000(long primitives, long vertices, long normals, long length) {
    long prim = primitives;
    long packet = linkedListAddress_1f8003d8.get();

    while(length != 0) {
      final long vert0 = vertices + MEMORY.ref(2, prim).offset(0x0aL).get() * 0x8L;
      final long vert1 = vertices + MEMORY.ref(2, prim).offset(0x0eL).get() * 0x8L;
      final long vert2 = vertices + MEMORY.ref(2, prim).offset(0x12L).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x0L).get(), 0);
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x4L).get(), 1);
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x0L).get(), 2);
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x4L).get(), 3);
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x0L).get(), 4);
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x4L).get(), 5);
      CPU.COP2(0x28_0030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x140_0006L);

        if((int)CPU.MFC2(24) > 0) {
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14));

          if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0L) {
            //LAB_800dafb8
            if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80L) {
              //LAB_800daff4
              if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0L) {
                //LAB_800db030
                if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80L) {
                  //LAB_800db06c
                  CPU.COP2(0x158_002dL);
                  long t1 = CPU.MFC2(7) + _1f8003e8.get();
                  long v1 = _1f8003cc.get();
                  t1 = (int)t1 >> _1f8003c4.get();
                  if(t1 >= v1) {
                    t1 = v1;
                  }

                  //LAB_800db09c
                  CPU.MTC2(MEMORY.ref(4, prim).offset(0x4L).get(), 6);

                  final long norm0 = normals + MEMORY.ref(2, prim).offset(0x08L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, prim).offset(0x0cL).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, prim).offset(0x10L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
                  CPU.COP2(0x118_043fL);
                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20));
                  MEMORY.ref(4, packet).offset(0x0cL).setu(CPU.MFC2(21));
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22));

                  final GsOT_TAG tag = tags_1f8003d0.deref().get((int)t1);
                  MEMORY.ref(4, packet).setu(0x600_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x1cL;
                }
              }
            }
          }
        }
      }

      //LAB_800db120
      prim += 0x14L;
      length--;
    }

    //LAB_800db128
    linkedListAddress_1f8003d8.setu(packet);
    return prim;
  }

  @Method(0x800db144L)
  public static long renderPrimitive3c(final long primitives, final long vertices, final long normals, long length) {
    long prim = primitives;
    long packet = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x3L).setu(0xcL); // 12 words
    MEMORY.ref(4, packet).offset(0x4L).setu(0x3c80_8080L); // Shaded textured quad, opaque, texture-blending

    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6); // RGBC

    //LAB_800db1a8
    while(length != 0) {
      final long vert0 = vertices + MEMORY.ref(2, prim).offset(0x16L).get() * 0x8L;
      final long vert1 = vertices + MEMORY.ref(2, prim).offset(0x1aL).get() * 0x8L;
      final long vert2 = vertices + MEMORY.ref(2, prim).offset(0x1eL).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x0L).get(), 0); // VXY0
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x4L).get(), 1); // VZ0
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x0L).get(), 2); // VXY1
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x4L).get(), 3); // VZ1
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x0L).get(), 4); // VXY2
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x4L).get(), 5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, prim).offset(0x04L));
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, prim).offset(0x08L));

      if((int)CPU.CFC2(31) >= 0) { // Flags
        CPU.COP2(0x140_0006L); // Normal clipping
        MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, prim).offset(0x0cL));

        if((int)CPU.MFC2(24) > 0) { // MAC0
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // SXY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // SXY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // SXY2

          final long vert3 = vertices + MEMORY.ref(2, prim).offset(0x22L).get() * 0x8L;
          CPU.MTC2(MEMORY.ref(4, vert3).offset(0x0L).get(), 0); // VXY0
          CPU.MTC2(MEMORY.ref(4, vert3).offset(0x4L).get(), 1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transformation single
          final long t4 = CPU.CFC2(31); // Flag
          MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // SXY2

          if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0L) {
            //LAB_800db2c4
            if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= 0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80L) {
              //LAB_800db314
              if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x2cL).getSigned() <= 0xc0L) {
                //LAB_800db364
                if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x2eL).getSigned() <= 0x80L) {
                  //LAB_800db3b4
                  CPU.COP2(0x168_002eL); // Average of four Z values
                  MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, prim).offset(0x10L));
                  // Average Z value
                  long t3 = CPU.MFC2(7) + _1f8003e8.get();
                  long v1 = _1f8003cc.get();
                  t3 = (int)t3 >> _1f8003c4.get();
                  if(t3 >= v1) {
                    t3 = v1;
                  }

                  //LAB_800db3f0
                  final GsOT_TAG tag = tags_1f8003d0.deref().get((int)t3);
                  final long norm0 = normals + MEMORY.ref(2, prim).offset(0x14L).get() * 0x8L;
                  final long norm1 = normals + MEMORY.ref(2, prim).offset(0x18L).get() * 0x8L;
                  final long norm2 = normals + MEMORY.ref(2, prim).offset(0x1cL).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5); // VZ2
                  CPU.COP2(0x118_043fL); // Normal colour triple vector

                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  final long norm3 = normals + MEMORY.ref(2, prim).offset(0x20L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour single vector
                  MEMORY.ref(4, packet).offset(0x28L).setu(CPU.MFC2(22)); // RGB2

                  MEMORY.ref(4, packet).setu(0xc00_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x34L;
                }
              }
            }
          }
        }
      }

      //LAB_800db494
      prim += 0x24L;
      length--;
    }

    //LAB_800db4a0
    linkedListAddress_1f8003d8.setu(packet);
    return prim;
  }

  @Method(0x800db4c0L)
  public static long renderPrimitive34(final long primitives, final long vertices, final long normals, long length) {
    long s0;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long v1;
    long a1;
    long a2;

    t4 = primitives;
    t1 = linkedListAddress_1f8003d8.get();
    s0 = _1f8003e8.get();
    MEMORY.ref(1, t1).offset(0x3L).setu(0x9L);
    MEMORY.ref(4, t1).offset(0x4L).setu(0x3480_8080L);
    CPU.MTC2(MEMORY.ref(4, t1).offset(0x4L).get(), 6);
    if(length != 0) {
      a2 = _1f8003c0.getAddress();
      a1 = _1f8003c8.getAddress();
      t3 = primitives + 0xcL;
      t0 = t1 + 0x22L;

      //LAB_800db52c
      do {
        t5 = vertices + MEMORY.ref(2, t4).offset(0x12L).get() * 0x8L;
        t6 = vertices + MEMORY.ref(2, t4).offset(0x16L).get() * 0x8L;
        t7 = vertices + MEMORY.ref(2, t4).offset(0x1aL).get() * 0x8L;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);
        MEMORY.ref(4, t0-0x16L).setu(MEMORY.ref(4, t3).offset(-0x8L));
        MEMORY.ref(4, t0-0x0aL).setu(MEMORY.ref(4, t3).offset(-0x4L));
        length--;
        if((int)CPU.CFC2(31) >= 0) {
          CPU.COP2(0x140_0006L);
          MEMORY.ref(4, t0 + 0x2L).setu(MEMORY.ref(4, t3));
          if((int)CPU.MFC2(24) > 0) {
            MEMORY.ref(4, t1).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x20L).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, t0).offset(-0x1aL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0xeL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() >= -0xc0L) {
              //LAB_800db604
              if(MEMORY.ref(2, t0).offset(-0x18L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() >= -0x80L || MEMORY.ref(2, t0).getSigned() >= -0x80L) {
                //LAB_800db640
                if(MEMORY.ref(2, t0).offset(-0x1aL).getSigned() <= 0xc0L || MEMORY.ref(2, t0).offset(-0xeL).getSigned() <= 0xc0L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() <= 0xc0L) {
                  //LAB_800db67c
                  if(MEMORY.ref(2, t0).offset(-0x18L).getSigned() <= 0x80L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() <= 0x80L || MEMORY.ref(2, t0).getSigned() <= 0x80L) {
                    //LAB_800db6b8
                    CPU.COP2(0x158_002dL);
                    t2 = CPU.MFC2(7) + s0;
                    v1 = MEMORY.ref(4, a1).offset(0x4L).get();
                    t2 = (int)t2 >> MEMORY.ref(4, a2).offset(0x4L).get();
                    if(t2 >= v1) {
                      t2 = v1;
                    }

                    //LAB_800db6e8
                    final GsOT_TAG tag = tags_1f8003d0.deref().get((int)t2);
                    t5 = normals + MEMORY.ref(2, t4).offset(0x10L).get() * 0x8L;
                    t6 = normals + MEMORY.ref(2, t4).offset(0x14L).get() * 0x8L;
                    t7 = normals + MEMORY.ref(2, t4).offset(0x18L).get() * 0x8L;
                    CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
                    CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
                    CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
                    CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
                    CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
                    CPU.COP2(0x118_043fL);
                    MEMORY.ref(4, t1).offset(0x04L).setu(CPU.MFC2(20));
                    MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(21));
                    MEMORY.ref(4, t1).offset(0x1cL).setu(CPU.MFC2(22));
                    MEMORY.ref(4, t1).setu(0x900_0000L | tag.p.get());
                    tag.set(t1 & 0xff_ffffL);
                    t0 += 0x28L;
                    t1 += 0x28L;
                  }
                }
              }
            }
          }
        }

        //LAB_800db764
        t3 += 0x1cL;
        t4 += 0x1cL;
      } while(length != 0);
    }

    //LAB_800db770
    linkedListAddress_1f8003d8.setu(t1);
    return t4;
  }

  @Method(0x800db790L)
  public static long renderPrimitive3804(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dbb14L)
  public static long renderPrimitive3004(final long primitives, final long vertices, final long normals, long length) {
    long prim = primitives;
    long packet = linkedListAddress_1f8003d8.get();

    //LAB_800dbb60
    while(length != 0) {
      final long vert0 = vertices + MEMORY.ref(2, prim).offset(0x12L).get() * 0x8L;
      final long vert1 = vertices + MEMORY.ref(2, prim).offset(0x16L).get() * 0x8L;
      final long vert2 = vertices + MEMORY.ref(2, prim).offset(0x1aL).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x0L).get(), 0);
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x4L).get(), 1);
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x0L).get(), 2);
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x4L).get(), 3);
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x0L).get(), 4);
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x4L).get(), 5);
      CPU.COP2(0x28_0030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x140_0006L);

        if((int)CPU.MFC2(24) > 0) {
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(13));
          MEMORY.ref(4, packet).offset(0x18L).setu(CPU.MFC2(14));

          if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x10L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x18L).getSigned() >= -0xc0L) {
            //LAB_800dbc18
            if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x12L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x1aL).getSigned() >= -0x80L) {
              //LAB_800dbc54
              if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x10L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x18L).getSigned() <= 0xc0L) {
                //LAB_800dbc90
                if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x12L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x1aL).getSigned() <= 0x80L) {
                  //LAB_800dbccc
                  CPU.COP2(0x158_002dL);
                  long t2 = CPU.MFC2(7) + _1f8003e8.get();
                  final long v1 = _1f8003cc.get();
                  t2 = (int)t2 >> _1f8003c4.get();
                  if(t2 >= v1) {
                    t2 = v1;
                  }

                  //LAB_800dbcfc
                  CPU.MTC2(MEMORY.ref(4, prim).offset(0x4L).get(), 6);
                  final long norm0 = normals + MEMORY.ref(2, prim).offset(0x10L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
                  CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
                  CPU.COP2(0x108_041bL);
                  MEMORY.ref(4, packet).offset(0x4L).setu(CPU.MFC2(22));

                  CPU.MTC2(MEMORY.ref(4, prim).offset(0x8L).get(), 6);
                  final long norm1 = normals + MEMORY.ref(2, prim).offset(0x14L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0);
                  CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1);
                  CPU.COP2(0x108_041bL);
                  MEMORY.ref(4, packet).offset(0xcL).setu(CPU.MFC2(22));

                  CPU.MTC2(MEMORY.ref(4, prim).offset(0xcL).get(), 6);
                  final long norm2 = normals + MEMORY.ref(2, prim).offset(0x18L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0);
                  CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1);
                  CPU.COP2(0x108_041bL);
                  MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(22));

                  final GsOT_TAG tag = tags_1f8003d0.deref().get((int)t2);
                  MEMORY.ref(4, packet).setu(0x600_0000L | tag.p.get());
                  tag.set(packet & 0xff_ffffL);

                  packet += 0x1cL;
                }
              }
            }
          }
        }
      }

      //LAB_800dbdbc
      prim += 0x1cL;
      length--;
    }

    //LAB_800dbdc8
    linkedListAddress_1f8003d8.setu(packet);
    return prim;
  }

  @Method(0x800dbde8L)
  public static long renderPrimitive3d(final long primitives, final long vertices, final long length) {
    assert false;
    return 0;
  }

  @Method(0x800dc164L)
  public static long renderPrimitive35(final long primitives, final long vertices, final long length) {
    assert false;
    return 0;
  }

  @Method(0x800dc43cL)
  public static long renderPrimitive3e(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dc7c0L)
  public static long renderPrimitive36(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dca90L)
  public static long renderPrimitive3a00(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dce24L)
  public static long renderPrimitive3200(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dd0fcL)
  public static long renderPrimitive3a04(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dd4e0L)
  public static long renderPrimitive3204(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dd808L)
  public static long renderPrimitive3f(final long primitives, final long vertices, final long length) {
    long len = length;
    long prim = primitives;
    long packet = linkedListAddress_1f8003d8.get();
    final UnboundedArrayRef<GsOT_TAG> tags = tags_1f8003d0.deref();

    //LAB_800dd84c
    while(len != 0) {
      final long vert0 = vertices + MEMORY.ref(2, prim).offset(0x24L).get() * 0x8L;
      final long vert1 = vertices + MEMORY.ref(2, prim).offset(0x26L).get() * 0x8L;
      final long vert2 = vertices + MEMORY.ref(2, prim).offset(0x28L).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x0L).get(), 0); // VXY0
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x4L).get(), 1); // VZ0
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x0L).get(), 2); // VXY1
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x4L).get(), 3); // VZ1
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x0L).get(), 4); // VXY2
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x4L).get(), 5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, prim).offset(0x04L));
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, prim).offset(0x08L));

      if((int)CPU.CFC2(31) >= 0) { // Flags
        CPU.COP2(0x140_0006L); // Normal clipping

        MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, prim).offset(0x0cL));

        if(CPU.MFC2(24) != 0) { // MAC0
          MEMORY.ref(1, packet).offset(0x03L).setu(0xcL); // 12 words
          MEMORY.ref(4, packet).offset(0x04L).setu(0x3e80_8080L); // Shaded textured four-point polygon, semi-transparent, tex-blend
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // SXY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // SXY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // SXY2

          final long vert3 = vertices + MEMORY.ref(2, prim).offset(0x2aL).get() * 0x8L;
          CPU.MTC2(MEMORY.ref(4, vert3).offset(0x0L).get(), 0); // VXY0
          CPU.MTC2(MEMORY.ref(4, vert3).offset(0x4L).get(), 1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transform single

          MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, prim).offset(0x10L));

          if((int)CPU.CFC2(31) >= 0) { // Flags
            MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // SXY2

            if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0L) {
              //LAB_800dd98c
              if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80L) {
                //LAB_800dd9dc
                if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x2cL).getSigned() <= 0xc0L) {
                  //LAB_800dda2c
                  if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x2eL).getSigned() <= 0x80L) {
                    //LAB_800dda7c
                    CPU.COP2(0x168_002eL); // Average of four Z values

                    MEMORY.ref(1, packet).offset(0x04L).setu(MEMORY.ref(1, prim).offset(0x14L));
                    MEMORY.ref(1, packet).offset(0x05L).setu(MEMORY.ref(1, prim).offset(0x15L));
                    MEMORY.ref(1, packet).offset(0x06L).setu(MEMORY.ref(1, prim).offset(0x16L));
                    MEMORY.ref(1, packet).offset(0x10L).setu(MEMORY.ref(1, prim).offset(0x18L));
                    MEMORY.ref(1, packet).offset(0x11L).setu(MEMORY.ref(1, prim).offset(0x19L));
                    MEMORY.ref(1, packet).offset(0x12L).setu(MEMORY.ref(1, prim).offset(0x1aL));
                    MEMORY.ref(1, packet).offset(0x1cL).setu(MEMORY.ref(1, prim).offset(0x1cL));
                    MEMORY.ref(1, packet).offset(0x1dL).setu(MEMORY.ref(1, prim).offset(0x1dL));
                    MEMORY.ref(1, packet).offset(0x1eL).setu(MEMORY.ref(1, prim).offset(0x1eL));
                    MEMORY.ref(1, packet).offset(0x28L).setu(MEMORY.ref(1, prim).offset(0x20L));
                    MEMORY.ref(1, packet).offset(0x29L).setu(MEMORY.ref(1, prim).offset(0x21L));
                    MEMORY.ref(1, packet).offset(0x2aL).setu(MEMORY.ref(1, prim).offset(0x22L));

                    long t1 = (CPU.MFC2(7) + _1f8003e8.get()) >> _1f8003c4.get();
                    final long v1 = _1f8003cc.get();
                    if(t1 >= v1) {
                      t1 = v1;
                    }

                    //LAB_800ddb3c
                    MEMORY.ref(4, packet).setu(0xc00_0000L | tags.get((int)t1).p.get());
                    tags.get((int)t1).num.set(0);
                    tags.get((int)t1).p.set(packet & 0xff_ffffL);
                    packet += 0x34L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800ddb64
      prim += 0x2cL;
      len--;
    }

    //LAB_800ddb70
    linkedListAddress_1f8003d8.setu(packet);
    return prim;
  }

  @Method(0x800ddb8cL)
  public static long renderPrimitive37(final long primitives, final long vertices, final long length) {
    long len = length;
    long prim = primitives;
    long packet = linkedListAddress_1f8003d8.get();
    final UnboundedArrayRef<GsOT_TAG> tags = tags_1f8003d0.deref();

    //LAB_800ddbd4
    while(len != 0) {
      final long vert0 = vertices + MEMORY.ref(2, prim).offset(0x1cL).get() * 0x8L;
      final long vert1 = vertices + MEMORY.ref(2, prim).offset(0x1eL).get() * 0x8L;
      final long vert2 = vertices + MEMORY.ref(2, prim).offset(0x20L).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x0L).get(), 0); // VXY0
      CPU.MTC2(MEMORY.ref(4, vert0).offset(0x4L).get(), 1); // VZ0
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x0L).get(), 2); // VXY1
      CPU.MTC2(MEMORY.ref(4, vert1).offset(0x4L).get(), 3); // VZ1
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x0L).get(), 4); // VXY2
      CPU.MTC2(MEMORY.ref(4, vert2).offset(0x4L).get(), 5); // VZ2
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0x0cL).setu(MEMORY.ref(4, prim).offset(0x04L));
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, prim).offset(0x08L));

      if((int)CPU.CFC2(31) >= 0) { // Flags
        CPU.COP2(0x140_0006L); // Normal clipping

        MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, prim).offset(0x0cL));

        if(CPU.MFC2(24) != 0) { // MAC0
          MEMORY.ref(1, packet).offset(0x03L).setu(0x9L); // 9 words
          MEMORY.ref(4, packet).offset(0x04L).setu(0x3680_8080L); // Shaded textured three-point polygon, semi-transparent, tex-blend
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // SXY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // SXY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // SXY2

          if((int)CPU.CFC2(31) >= 0) { // Flags
            if(MEMORY.ref(2, packet).offset(0x08L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0L) {
              //LAB_800ddcd0
              if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80L) {
                //LAB_800ddd0c
                if(MEMORY.ref(2, packet).offset(0x08L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() <= 0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() <= 0xc0L) {
                  //LAB_800ddd48
                  if(MEMORY.ref(2, packet).offset(0x0aL).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() <= 0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() <= 0x80L) {
                    //LAB_800ddd84
                    CPU.COP2(0x158_002dL); // Average of three Z values

                    MEMORY.ref(1, packet).offset(0x04L).setu(MEMORY.ref(1, prim).offset(0x10L));
                    MEMORY.ref(1, packet).offset(0x05L).setu(MEMORY.ref(1, prim).offset(0x11L));
                    MEMORY.ref(1, packet).offset(0x06L).setu(MEMORY.ref(1, prim).offset(0x12L));
                    MEMORY.ref(1, packet).offset(0x10L).setu(MEMORY.ref(1, prim).offset(0x14L));
                    MEMORY.ref(1, packet).offset(0x11L).setu(MEMORY.ref(1, prim).offset(0x15L));
                    MEMORY.ref(1, packet).offset(0x12L).setu(MEMORY.ref(1, prim).offset(0x16L));
                    MEMORY.ref(1, packet).offset(0x1cL).setu(MEMORY.ref(1, prim).offset(0x18L));
                    MEMORY.ref(1, packet).offset(0x1dL).setu(MEMORY.ref(1, prim).offset(0x19L));
                    MEMORY.ref(1, packet).offset(0x1eL).setu(MEMORY.ref(1, prim).offset(0x1aL));

                    long t1 = (int)(CPU.MFC2(7) + _1f8003e8.get()) >> _1f8003c4.get();
                    final long v1 = _1f8003cc.get();
                    if(t1 >= v1) {
                      t1 = v1;
                    }

                    //LAB_800dde20
                    MEMORY.ref(4, packet).setu(0x900_0000L | tags.get((int)t1).p.get());
                    tags.get((int)t1).set(packet & 0xff_ffffL);
                    packet += 0x28L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800dde48
      prim += 0x24L;
      len--;
    }

    //LAB_800dde54
    linkedListAddress_1f8003d8.setu(packet);
    return prim;
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
      final long t1 = _800503f8.offset(v1).get();

      long a1 = smallerStruct.tmdSubExtensionArr_20.get(index).getPointer() + 0x4L; //TODO

      //LAB_800ddef8
      for(int i = 0; i < smallerStruct.sa_08.get(index).get(); i++) {
        a1 += 0x4L;
      }

      //LAB_800ddf08
      final long t3 = MEMORY.ref(2, a1).get();
      a1 += 0x2L;

      smallerStruct.sa_10.get(index).incr();

      if(smallerStruct.sa_10.get(index).get() == (MEMORY.ref(2, a1).get() & 0xffffL)) {
        smallerStruct.sa_10.get(index).set((short)0);

        if(MEMORY.ref(2, a1).offset(0x2L).get() == 0xffffL) {
          smallerStruct.sa_08.get(index).set((short)0);
        } else {
          //LAB_800ddf70
          smallerStruct.sa_08.get(index).incr();
        }
      }

      //LAB_800ddf8c
      SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)t1, (short)(t2 + t3), (short)16, (short)1), t1 & 0xffffL, (smallerStruct.sa_18.get(index).get() + t2) & 0xffffL);
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

  @Method(0x800de668L)
  public static long FUN_800de668(final ScriptStruct a0) {
    final BigStruct s0 = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    s0.vec_138.x.set((int)a0.params_20.get(1).deref().get());
    s0.vec_138.y.set((int)a0.params_20.get(2).deref().get());
    s0.vec_138.z.set((int)a0.params_20.get(3).deref().get());
    s0.ui_144.set(a0.params_20.get(4).deref().get());

    s0.us_170.set(1);

    s0.vec_148.set(s0.vec_138).sub(s0.coord2_14.coord.transfer).div((int)s0.ui_144.get());

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
    v0 = v0 / (int)s0.ui_144.get();

    if(s0.vec_148.x.get() < 0) {
      //LAB_800de7e0
      v0 = ~v0 + 1;
    }

    //LAB_800de810
    s0.vec_154.x.set(v0 & 0xffff);

    v0 = s0.vec_138.y.get() - s0.coord2_14.coord.transfer.getY();
    v0 = v0 << 16;
    v0 = v0 / (int)s0.ui_144.get();

    if(s0.vec_148.y.get() < 0) {
      //LAB_800de84c
      v0 = ~v0 + 1;
    }

    //LAB_800de87c
    s0.vec_154.y.set(v0 & 0xffff);

    v0 = s0.vec_138.z.get() - s0.coord2_14.coord.transfer.getZ();
    v0 = v0 << 16;
    v0 = v0 / (int)s0.ui_144.get();

    if(s0.vec_148.z.get() < 0) {
      //LAB_800de8b8
      v0 = ~v0 + 1;
    }

    //LAB_800de8e8
    s0.vec_154.z.set(v0 & 0xffff);

    s0.vec_160.x.set(0);
    s0.vec_160.y.set(0);
    s0.vec_160.z.set(0);

    setCallback10(index_800c6880.offset(s0.us_130.get() * 0x4L).get(), MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e1f90", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));

    s0.ui_190.and(0x7fff_ffffL);
    return 0;
  }

  @Method(0x800df168L)
  public static long FUN_800df168(final ScriptStruct a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800dfe0c(a0);
  }

  @Method(0x800df198L)
  public static long FUN_800df198(final ScriptStruct a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800dfec8(a0);
  }

  @Method(0x800df1f8L)
  public static long FUN_800df1f8(final ScriptStruct a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800dffa4(a0);
  }

  @Method(0x800df258L)
  public static long FUN_800df258(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.coord2_14.coord.transfer.setX((int)a0.params_20.get(1).deref().get());
    struct.coord2_14.coord.transfer.setY((int)a0.params_20.get(2).deref().get());
    struct.coord2_14.coord.transfer.setZ((int)a0.params_20.get(3).deref().get());
    struct.us_170.set(0);
    return 0;
  }

  @Method(0x800df2b8L)
  public static long FUN_800df2b8(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    a0.params_20.get(1).deref().set(struct.coord2_14.coord.transfer.getX() & 0xffff_ffffL);
    a0.params_20.get(2).deref().set(struct.coord2_14.coord.transfer.getY() & 0xffff_ffffL);
    a0.params_20.get(3).deref().set(struct.coord2_14.coord.transfer.getZ() & 0xffff_ffffL);
    return 0;
  }

  @Method(0x800df314L)
  public static long FUN_800df314(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.coord2Param_64.rotate.x.set((short)a0.params_20.get(1).deref().get());
    struct.coord2Param_64.rotate.y.set((short)a0.params_20.get(2).deref().get());
    struct.coord2Param_64.rotate.z.set((short)a0.params_20.get(3).deref().get());
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800df3d0L)
  public static long FUN_800df3d0(final ScriptStruct a0) {
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e0018(a0);
  }

  @Method(0x800df410L)
  public static long FUN_800df410(final ScriptStruct a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e0094(a0);
  }

  @Method(0x800df440L)
  public static long FUN_800df440(final ScriptStruct a0) {
    a0.params_20.get(4).set(a0.params_20.get(3).deref());
    a0.params_20.get(3).set(a0.params_20.get(2).deref());
    a0.params_20.get(2).set(a0.params_20.get(1).deref());
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800de668(a0);
  }

  @Method(0x800df530L)
  public static long FUN_800df530(final ScriptStruct a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e0184(a0);
  }

  @Method(0x800df650L)
  public static long FUN_800df650(final ScriptStruct a0) {
    a0.params_20.get(1).set(a0.params_20.get(0).deref());
    a0.params_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e02fc(a0);
  }

  @Method(0x800dfe0cL)
  public static long FUN_800dfe0c(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final long index = a0.params_20.get(1).deref().get();

    struct.us_12e.set((int)index);
    struct.ub_9d.set((int)_800c6a50.offset(index * 0x4L).get());

    FUN_80020fe0(struct);
    FUN_800e0d18(struct, extendedTmdArr_800c6a00.get((int)index).deref(), struct.mrg_124.deref().getFile((int)(index * 0x21L + 0x1L)));

    struct.us_12c.set(0);
    struct.ui_188.set(0);

    return 0;
  }

  @Method(0x800dfec8L)
  public static long FUN_800dfec8(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    struct.us_132.set((int)a0.params_20.get(1).deref().get());
    struct.ub_a2.set(0);
    struct.ub_a3.set(0);

    long v0 = struct.us_12e.get() * 0x21L + struct.us_132.get() + 0x1L;
    long a1 = struct.mrg_124.deref().getFile((int)v0);

    FUN_80021584(struct, a1);

    struct.us_12c.set(0);
    struct.ui_190.and(0x9fff_ffffL);

    return 0;
  }

  @Method(0x800dffa4L)
  public static long FUN_800dffa4(final ScriptStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_12a.set((int)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e0018L)
  public static long FUN_800e0018(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    final long v1 = (0xc01L - FUN_80040b90(a0.params_20.get(3).deref().get() - struct.coord2_14.coord.transfer.getZ(), a0.params_20.get(1).deref().get() - struct.coord2_14.coord.transfer.getX())) & 0xfffL;
    struct.coord2Param_64.rotate.y.set((short)v1);
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800e0094L)
  public static long FUN_800e0094(final ScriptStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_128.set((int)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e0184L)
  public static long FUN_800e0184(final ScriptStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_172.set((int)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e02fcL)
  public static long FUN_800e02fc(final ScriptStruct a0) {
    final BigStruct struct1 = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    struct1.us_178.set((int)a0.params_20.get(1).deref().get());

    if(a0.params_20.get(1).deref().get() != 0) {
      //LAB_800e035c
      for(int i = 0; i < _800c6730.get(); i++) {
        final BigStruct struct2 = biggerStructPtrArr_800bc1c0.get((int)index_800c6880.offset(i * 0x4L).get()).deref().innerStruct_00.derefAs(BigStruct.class);

        if(struct2.us_130.get() != struct1.us_130.get()) {
          struct2.us_178.set(0);
        }

        //LAB_800e0390
      }
    }

    //LAB_800e03a0
    return 0;
  }

  @Method(0x800e03e4L)
  public static long FUN_800e03e4(final ScriptStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final long a0_0 = a0.params_20.get(1).deref().get();

    if(a0_0 >= 0x20L) {
      struct.ui_f8.or(0x1L << (a0_0 - 0x20L));
    } else {
      //LAB_800e0430
      struct.ui_f4.or(0x1L << a0_0);
    }

    //LAB_800e0440
    return 0;
  }

  @Method(0x800e09e0L)
  public static long FUN_800e09e0(final ScriptStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ub_cc.set(1);
    return 0;
  }

  @Method(0x800e0a14L)
  public static long FUN_800e0a14(final ScriptStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ub_cc.set(0);
    return 0;
  }

  @Method(0x800e0d18L)
  public static void FUN_800e0d18(final BigStruct struct, final ExtendedTmd extendedTmd, final long a2) {
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
    struct.scaleVector_fc.setPad((int)((extendedTmd.tmdPtr_00.deref().id.get() & 0xffff_0000L) >> 11));
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
    FUN_80021b08(struct.ObjTable_0c, struct.dobj2ArrPtr_00.deref(), struct.coord2ArrPtr_04.deref(), struct.coord2ParamArrPtr_08.deref(), struct.count_c8.get());

    struct.coord2_14.param.set(struct.coord2Param_64);

    GsInitCoordinate2(null, struct.coord2_14);
    FUN_80021ca0(struct.ObjTable_0c, struct.tmd_8c.deref(), struct.coord2_14, struct.count_c8.get(), (short)(struct.tmdNobj_ca.get() + 1));

    struct.ub_a2.set(0);
    struct.ub_a3.set(0);
    struct.ui_f4.set(0);
    struct.ui_f8.set(0);
    struct.us_a0.set(0);

    FUN_80021584(struct, a2);

    struct.coord2_14.coord.transfer.setX(transferX);
    struct.coord2_14.coord.transfer.setY(transferY);
    struct.coord2_14.coord.transfer.setZ(transferZ);

    struct.scaleVector_fc.setX(0x1000);
    struct.scaleVector_fc.setY(0x1000);
    struct.scaleVector_fc.setZ(0x1000);
  }

  @Method(0x800e0ff0L)
  public static long FUN_800e0ff0(int index, BiggerStruct<BigStruct> biggerStruct, BigStruct bigStruct) {
    BigStruct puVar1 = biggerStruct.innerStruct_00.deref();

    if(puVar1.us_178.get() != 0) {
      SetRotMatrix(matrix_800c3548);
      SetTransMatrix(matrix_800c3548);
      FUN_800e8104(new SVECTOR().set((short)puVar1.coord2_14.coord.transfer.getX(), (short)puVar1.coord2_14.coord.transfer.getY(), (short)puVar1.coord2_14.coord.transfer.getZ()));
    }

    if(puVar1.us_128.get() == 0) {
      if(puVar1.ui_188.get() != 0) {
        puVar1.ui_188.sub(1);
        puVar1.coord2Param_64.rotate.x.add((short)puVar1.usPtr_17c.deref().get());
        puVar1.coord2Param_64.rotate.y.add((short)puVar1.usPtr_180.deref().get());
        puVar1.coord2Param_64.rotate.z.add((short)puVar1.usPtr_184.deref().get());
      }

      if(puVar1.us_12e.get() == 0) {
        FUN_800217a4(puVar1);
      } else {
        FUN_800214bc(puVar1);
      }

      if(puVar1.us_12a.get() == 0) {
        FUN_80020b98(puVar1);
        if(puVar1.us_12c.get() == 1 && (puVar1.ui_190.get() & 0x2000_0000L) != 0) {
          puVar1.us_132.set(0);
          FUN_80021584(puVar1, puVar1.mrg_124.deref().getFile((int)(puVar1.us_12e.get() * 0x21L + 0x1L)));
          puVar1.us_12c.set(0);
          puVar1.ui_190.and(0x9fff_ffffL);
        }
      }
    }

    if(puVar1.us_9e.get() == 0) {
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

    return 0;
  }

  @Method(0x800e123cL)
  public static long FUN_800e123c(int index, BiggerStruct<BigStruct> biggerStruct, BigStruct bigStruct) {
    final BigStruct struct = biggerStruct.innerStruct_00.deref();

    if(struct.us_128.get() == 0) {
      if(struct.flatLightingEnabled_1c4.get()) {
        GsF_LIGHT light0 = new GsF_LIGHT();
        light0.direction_00.setX(0);
        light0.direction_00.setY(0x1000);
        light0.direction_00.setZ(0);
        light0.r_0c.set(struct.flatLightRed_1c5);
        light0.g_0d.set(struct.flatLightGreen_1c6);
        light0.b_0e.set(struct.flatLightBlue_1c7);
        GsSetFlatLight(0, light0);

        GsF_LIGHT light1 = new GsF_LIGHT();
        light1.direction_00.setX(0x1000);
        light1.direction_00.setY(0);
        light1.direction_00.setZ(0);
        light1.r_0c.set(struct.flatLightRed_1c5);
        light1.g_0d.set(struct.flatLightGreen_1c6);
        light1.b_0e.set(struct.flatLightBlue_1c7);
        GsSetFlatLight(1, light1);

        GsF_LIGHT light2 = new GsF_LIGHT();
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
        GsSetAmbient(0x800L, 0x800L, 0x800L);
      }

      //LAB_800e1390
      FUN_800ef0f8(struct, struct.v_1d0.getAddress());
    }

    //LAB_800e139c
    return 0;
  }

  @Method(0x800e13b0L)
  public static void executeSceneGraphicsLoadingStage(final int index) {
    switch((int)loadingStage_800c68e4.get()) {
      case 0x0:
        loadTimImage(_80010544.getAddress());

        if(_80050274.get() != submapCut_80052c30.get()) {
          _800bda08.setu(_80050274);
          _80050274.setu(submapCut_80052c30);
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

        GsSetAmbient(0x800L, 0x800L, 0x800L);
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
          FUN_8001e29c(0x4L);
          FUN_8001eadc(_800bd808.get());
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
        FUN_8001f3d0(ret, 0);
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

        final UnsignedIntRef drgnIndex = new UnsignedIntRef();
        final UnsignedIntRef fileIndex = new UnsignedIntRef();

        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);

        if(drgnIndex.get() == 0x1L || drgnIndex.get() == 0x2L || drgnIndex.get() == 0x3L || drgnIndex.get() == 0x4L) {
          //LAB_800e1720
          //LAB_800e17c4
          //LAB_800e17d8
          loadDrgnBinFile(drgnIndex.get() + 0x2L, fileIndex.get() + 0x1L, 0, getMethodAddress(SMap.class, "FUN_800e3d80", Value.class, long.class, long.class), 0, 0x2L);
          loadDrgnBinFile(drgnIndex.get() + 0x2L, fileIndex.get() + 0x2L, 0, getMethodAddress(SMap.class, "FUN_800e3d80", Value.class, long.class, long.class), 0x1L, 0x2L);
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
        _800c6730.setu(secondLastFileIndex);

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

        final long biggerStructIndex = allocateBiggerStruct(0, 0, false, 0, 0);
        _800c6740.setu(biggerStructIndex);
        loadScriptFile(biggerStructIndex, mrg1Addr_800c68d8.deref().getFile(0, ScriptFile::new));

        //LAB_800e1a38
        for(int i = 0; i < _800c6730.get(); i++) {
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
        for(int i = 0; i < _800c6730.get(); i++) {
          //LAB_800e1ae0
          for(int n = i + 1; n < _800c6730.get(); n++) {
            if(extendedTmdArr_800c6a00.get(n).getPointer() == extendedTmdArr_800c6a00.get(i).getPointer()) {
              _800c6a50.offset(n * 0x4L).setu(0x80L);
            }

            //LAB_800e1af4
          }

          //LAB_800e1b0c
        }

        //LAB_800e1b20
        long s5 = 0x8L;

        //LAB_800e1b54
        for(int i = 0; i < _800c6730.get(); i++) {
          final long index2 = allocateBiggerStruct(0x210L);
          index_800c6880.offset(i * 0x4L).setu(index2);
          setCallback04(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e0ff0", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
          setCallback08(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e123c", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
          setCallback0c(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e3df4", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
          loadScriptFile(index2, mrg1Addr_800c68d8.deref().getFile(i + 1, ScriptFile::new));

          final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)index2).deref().innerStruct_00.derefAs(BigStruct.class);
          struct.ub_9d.set((int)_800c6a50.offset(1, i * 0x4L).get());

          FUN_80020a00(struct, extendedTmdArr_800c6a00.get(i).deref(), mrg0Addr_800c6878.deref().getFile((int)s5 / 8));

          if(i == 0) {
            FUN_800e0d18(bigStruct_800c6748, extendedTmdArr_800c6a00.get(0).deref(), mrg0Addr_800c6878.deref().getFile(1)); //TODO not sure if this file 1 is right
            bigStruct_800c6748.coord2_14.coord.transfer.setX(0);
            bigStruct_800c6748.coord2_14.coord.transfer.setY(0);
            bigStruct_800c6748.coord2_14.coord.transfer.setZ(0);
            bigStruct_800c6748.coord2Param_64.rotate.x.set((short)0);
            bigStruct_800c6748.coord2Param_64.rotate.y.set((short)0);
            bigStruct_800c6748.coord2Param_64.rotate.z.set((short)0);
          }

          //LAB_800e1c50
          struct.mrg_124.set(mrg0Addr_800c6878.deref());
          struct.us_128.set(0);
          struct.us_12a.set(0);
          struct.us_12c.set(0);
          struct.us_12e.set(i);
          struct.us_130.set(i);
          struct.us_132.set(0);
          struct.us_134.set(0);
          struct.ui_144.set(0);
          struct.ui_16c.set(0xffff_ffffL);
          struct.us_170.set(0);
          struct.us_172.set(0);
          struct.ui_188.set(0);
          struct.ui_194.set(0);
          struct.ui_19c.set(0xffff_ffffL);
          struct.ui_1a0.set(0x14L);
          struct.ui_1a4.set(0x14L);
          struct.ui_1a8.set(0xffff_ffffL);
          struct.ui_1ac.set(0x14L);
          struct.ui_1b0.set(0x14L);
          struct.ui_1b4.set(0x32L);
          struct.ui_1b8.set(0x14L);
          struct.ui_1bc.set(0x14L);
          struct.ui_1c0.set(0x32L);
          struct.flatLightingEnabled_1c4.set(false);
          struct.flatLightRed_1c5.set(0x80);
          struct.flatLightGreen_1c6.set(0x80);
          struct.flatLightBlue_1c7.set(0x80);

          if(i == 0) {
            struct.us_178.set(1);
          } else {
            //LAB_800e1ce0
            struct.us_178.set(0);
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
          FUN_800f04ac(struct.v_1d0.getAddress());

          s5 += 0x108L;
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

        FUN_800e3d68();
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
          FUN_800e3d68();
        }

        //LAB_800e1f40
        _800bd7b4.setu(0x1L);
        if(_800c6738.get() != 0) {
          FUN_800e2648();
        }

        //LAB_800e1f60
    }
  }

  @Method(0x800e1f90L)
  public static long FUN_800e1f90(final int index, final BiggerStruct<BigStruct> biggerStruct, final BigStruct bigStruct) {
    final BigStruct s1 = biggerStruct.innerStruct_00.deref();

    if((int)s1.ui_190.get() < 0) {
      return 0;
    }

    final SVECTOR vec = new SVECTOR();

    if((s1.vec_148.x.get() & 0x7fff_ffffL) != 0) {
      vec.x.set((short)s1.vec_148.x.get());
    }

    //LAB_800e1fe4
    if((s1.vec_148.y.get() & 0x7fff_ffffL) != 0) {
      vec.y.set((short)s1.vec_148.y.get());
    }

    //LAB_800e1ffc
    if((s1.vec_148.z.get() & 0x7fff_ffffL) != 0) {
      vec.z.set((short)s1.vec_148.z.get());
    }

    //LAB_800e2014
    s1.vec_160.add(s1.vec_154);

    if((s1.vec_160.x.get() & 0x1_0000L) != 0) {
      s1.vec_160.x.and(0xffff);

      if(s1.vec_148.x.get() >= 0) {
        s1.vec_148.x.incr();
      } else {
        //LAB_800e2074
        s1.vec_148.x.decr();
      }
    }

    //LAB_800e2078
    if((s1.vec_160.y.get() & 0x1_0000L) != 0) {
      s1.vec_160.y.and(0xffff);

      if(s1.vec_148.y.get() >= 0) {
        s1.vec_148.y.incr();
      } else {
        //LAB_800e20a4
        s1.vec_148.y.decr();
      }
    }

    //LAB_800e20a8
    if((s1.vec_160.z.get() & 0x1_0000L) != 0) {
      s1.vec_160.z.and(0xffff);

      if(s1.vec_148.z.get() >= 0) {
        s1.vec_148.z.incr();
      } else {
        //LAB_800e20d4
        s1.vec_148.z.decr();
      }
    }

    //LAB_800e20d8
    s1.ui_144.decr();

    if(s1.us_172.get() == 0) {
      final SVECTOR svec = new SVECTOR();

      if((s1.ui_190.get() & 0x1L) != 0) {
        SetRotMatrix(matrix_800c3548);
        SetTransMatrix(matrix_800c3548);
        FUN_800e82cc(svec, vec);
      } else {
        //LAB_800e2134
        svec.set(vec);
      }

      //LAB_800e2140
      final long s3 = FUN_800e88a0((short)s1.us_12e.get(), s1.coord2_14.coord, svec);
      if((int)s3 >= 0) {
        if(FUN_800e6798(s3, 0, s1.coord2_14.coord.transfer.getX(), s1.coord2_14.coord.transfer.getY(), s1.coord2_14.coord.transfer.getZ(), svec) != 0) {
          s1.coord2_14.coord.transfer.x.add(svec.x.get());
          s1.coord2_14.coord.transfer.y.set(svec.y.get());
          s1.coord2_14.coord.transfer.z.add(svec.z.get());
        }
      }

      //LAB_800e21bc
      s1.ui_16c.set(s3);
    } else {
      //LAB_800e21c4
      s1.coord2_14.coord.transfer.x.add(vec.x.get());
      s1.coord2_14.coord.transfer.y.add(vec.y.get());
      s1.coord2_14.coord.transfer.z.add(vec.z.get());
    }

    //LAB_800e21e8
    if(s1.ui_144.get() == 0) {
      //LAB_800e2200
      s1.us_170.set(0);
      return 0x1L;
    }

    //LAB_800e21f8
    //LAB_800e2204
    return 0;
  }

  @Method(0x800e2220L)
  public static void FUN_800e2220() {
    assert false;
  }

  @Method(0x800e2648L)
  public static void FUN_800e2648() {
    assert false;
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
  public static void FUN_800e3d68() {
    _800bee90.setu(0);
    _800bee94.setu(0);
    _800bee98.setu(0);
  }

  /** Callback for two different MRG files (sectors 136752 and 136849) */
  @Method(0x800e3d80L)
  public static void FUN_800e3d80(final Value fileAddress, final long fileSize, final long a2) {
    switch((int)a2) {
      case 0x0 -> {
        mrg0Loaded_800c6874.setu(0x1);
        mrg0Addr_800c6878.set(fileAddress.deref(4).cast(MrgFile::new));
      }

      case 0x1 -> {
        mrg1Loaded_800c68d0.setu(0x1);
        mrg1Addr_800c68d8.set(fileAddress.deref(4).cast(MrgFile::new));
      }

      case 0x10 -> {
        mrg10Loaded_800c68e0.setu(0x1);
        mrg10Addr_800c6710.set(fileAddress.deref(4).cast(MrgFile::new));
      }
    }
  }

  @Method(0x800e3df4L)
  public static long FUN_800e3df4(final int index, final BiggerStruct<BigStruct> biggerStruct, final BigStruct bigStruct) {
    assert false;
    return 0;
  }

  @Method(0x800e4018L)
  public static void FUN_800e4018() {
    if(_800bb0ab.get() != 0) {
      if(_800f64ac.get() == 0) {
        _800f64ac.setu(0x1L);
      }
    } else if(_800f64ac.get() == 0x1L) {
      _800f64ac.setu(0);
      _800c69ec.setu(0);
    }
  }

  @Method(0x800e4378L)
  public static void FUN_800e4378(final BigStruct a0, final long a1) {
    assert false;
  }

  @Method(0x800e450cL)
  public static void FUN_800e450c(final BigStruct a0, final long a1) {
    assert false;
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
    assert false;
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

  @Method(0x800e49f0L)
  public static boolean FUN_800e49f0(final MATRIX mat) {
    assert false;
    return false;
  }

  @Method(0x800e4ac8L)
  public static void FUN_800e4ac8() {
    final long v0 = _800f64c6.offset(submapCut_80052c30.get() * 0x4L).get();
    _800bed58.setu(v0 < 0x1L ? 0x1L : 0);
  }

  @Method(0x800e4b20L)
  public static long FUN_800e4b20() {
    if(_800c6ae0.get() < 0xfL) {
      return 0;
    }

    if(fileCount_8004ddc8.get() != 0) {
      return 0;
    }

    if(_800bb0ab.get() != 0) {
      return 0;
    }

    _800c6ae4.addu(0x1L);

    if(_800c6ae4.getSigned() < 0) {
      return 0;
    }

    if(index_80052c38.get() < 0x40L) {
      if(arr_800cb460.get((int)index_80052c38.get()).get() != 0) {
        return 0;
      }
    }

    //LAB_800e4bc0
    if(!FUN_800e5518(0)) {
      return 0;
    }

    if(_8007a39c.get() == 0) {
      return 0;
    }

    if(!FUN_800e49f0(biggerStructPtrArr_800bc1c0.get((int)_8007a39c.deref(4).get()).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14.coord)) {
      return 0;
    }

    _800c6ae8.addu(_800f64c4.offset(1, submapCut_80052c30.get() * 0x4L).offset(0x2L).get() * _800c6abc.get());

    if(_800c6ae8.get() > 0x1400L) {
      submapScene_800bb0f8.setu(_800f74c4.offset(2, _800f64c4.offset(2, submapCut_80052c30.get() * 0x4L).getSigned() * 0x8L).offset(FUN_800e49a4() * 0x2L).getSigned());
      submapStage_800bb0f4.setu(_800f64c4.offset(1, submapCut_80052c30.get() * 0x4L).offset(0x3L));
      return 0x1L;
    }

    //LAB_800e4ce4
    //LAB_800e4ce8
    return 0;
  }

  @Method(0x800e4d00L)
  public static void FUN_800e4d00(final long a0, final int index) {
    if(FUN_800e5264(matrix_800c6ac0, a0) == 0) {
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
    assert false;
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
    assert false;
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
    _800f7e28.setNullable(s1.derefNullable());
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

    _800f7e28.deref().parent_00.set(v0);
    _800f7e28.set(v0);

    //LAB_800e50ec
    return 0x1L;
  }

  @Method(0x800e5104L)
  public static void FUN_800e5104(final int index, final MediumStruct a1) {
    executeSceneGraphicsLoadingStage(index);

    a1.callback_48.deref().run(a1);

    GsOTPtr_800f64c0.set(orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()));
    _800c6ae0.addu(0x1L);

    if(_800bb0ab.get() != 0) {
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
    final MATRIX[] matrices = new MATRIX[10]; // Unsure of this size
    for(int i = 0; i < _800c6730.get(); i++) {
      if(!FUN_800e5518(i)) {
        return;
      }

      matrices[i] = biggerStructPtrArr_800bc1c0.get((int)index_800c6880.offset(i * 0x4L).get()).deref().innerStruct_00.derefAs(BigStruct.class).coord2_14.coord;
    }

    //LAB_800e5234
    FUN_800e7954(GsOTPtr_800f64c0.deref(), matrices, _800c6730.get());

    //LAB_800e5248
  }

  @Method(0x800e5264L)
  public static long FUN_800e5264(final MATRIX mat, final long a1) {
    if(_80052c3c.get() != a1) {
      _800cb448.get();
      return 0;
    }

    //LAB_800e5294
    if(_80052c40.get() == 0) {
      return 0;
    }

    //LAB_800e52b0
    setScreenOffsetIfNotSet(_800bed50.get(), _800bed54.get());

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
  public static void loadBackground(final Value address, final long fileSize, final long param) {
    backgroundLoaded_800cab10.offset(param * 0x4L).setu(0x1L);

    final MrgFile mrg = address.deref(4).cast(MrgFile::new);

    //LAB_800e5374
    for(int i = 3; i < mrg.count.get(); i++) {
      TimHeader timHeader = parseTimHeader(MEMORY.ref(4, mrg.getFile(i)).offset(0x4L));
      LoadImage(timHeader.imageRect, timHeader.imageAddress.get());

      if(timHeader.hasClut()) {
        LoadImage(timHeader.clutRect, timHeader.clutAddress.get());
      }
    }

    //LAB_800e5430
    loadEnvironment(mrg.getFile(0, EnvironmentFile::new));
    FUN_800e8cd0(mrg.getFile(2, TmdWithId::new), (int)mrg.entries.get(2).size.get(), mrg.getFile(1), mrg.entries.get(1).size.get());

    DrawSync(0);

    removeFromLinkedList(mrg.getAddress());

    _80052c44.setu(0x2L);
    _80052c48.setu(0);
    _800cab20.setu(0x2L);
  }

  @Method(0x800e54a4L)
  public static void newrootCallback_800e54a4(final Value address, final long fileSize, final long param) {
    newrootPtr_800cab04.set(newroot_800c6af0);
    memcpy(newroot_800c6af0.getAddress(), address.get(), (int)fileSize);
    removeFromLinkedList(address.get());
    FUN_800e6640(newrootPtr_800cab04.deref());
    newrootLoaded_800cab1c.setu(0x1L);
  }

  @Method(0x800e5518L)
  public static boolean FUN_800e5518(final long a0) {
    return index_800c6880.offset(a0 * 0x4L).get() < 0x40L;
  }

  @Method(0x800e5534L)
  public static void FUN_800e5534(final long a0, final long a1) {
    assert false;
  }

  @Method(0x800e5914L)
  public static void executeSmapLoadingStage() {
    executeSmapLoadingStage_2();
  }

  @Method(0x800e59a4L)
  public static void executeSmapLoadingStage_2() {
    boolean a0;

    if(_800cb440.get() == 0) {
      _800cab20.subu(0x1L);

      if(_800cab20.getSigned() >= 0) {
        DrawSync(0);
        setWidthAndFlags(384L, 0);
        DrawSync(0);
        _800caaf4.setu(submapCut_80052c30);
        _800caaf8.setu(_80052c34);
        return;
      }
    }

    //LAB_800e5a30
    //LAB_800e5a34
    if(pregameLoadingStage_800bb10c.get() == 0) {
      pregameLoadingStage_800bb10c.setu(0x1L);
      _800caaf4.setu(submapCut_80052c30);
      _800caaf8.setu(_80052c34);
      _80052c44.setu(0x2L);

      if(_800cb440.get() != 0) {
        if(_800cb430.get() == 0x8L) {
          _800cb430.setu(0x9L);
        }

        //LAB_800e5aac
        _800cb440.setu(0);
        return;
      }

      //LAB_800e5abc
      _800cb430.setu(0);
    }

    final UnsignedIntRef drgnIndex = new UnsignedIntRef();
    final UnsignedIntRef fileIndex = new UnsignedIntRef();

    //LAB_800e5ac4
    switch((int)_800cb430.get()) {
      case 0x0:
        srand(getTimerValue(0));

        if(_800cb440.get() == 0) {
          setWidthAndFlags(384L, 0);
        }

        //LAB_800e5b2c
        _80052c44.setu(0x2L);
        _800c6ae8.setu(0);
        _800cb430.setu(0x1L);
        break;

      case 0x1: // Load newroot
        newrootLoaded_800cab1c.setu(0);
        loadFile(_80052c4c.getAddress(), 0, getMethodAddress(SMap.class, "newrootCallback_800e54a4", Value.class, long.class, long.class), 0x63L, 0);
        _800cb430.setu(0x2L);
        break;

      case 0x2: // Wait for newroot to load
        if(newrootLoaded_800cab1c.get() != 0) {
          _800cb430.setu(0x3L);
        }

        break;

      case 0x3:
        _800cab28.setu(0);
        _80052c44.setu(0x1L);
        _800caaf4.setu(submapCut_80052c30);
        _800caaf8.setu(_80052c34);
        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);
        if(drgnIndex.get() != drgnBinIndex_800bc058.get()) {
          if(_800cb440.get() != 0) {
            _800cb440.setu(0);
            _800cb430.setu(0x8L);
          }

          //LAB_800e5c9c
          _8004ddc0.setu(drgnIndex.get());
          _800bc05c.setu(0x5L);
          _800cb430.setu(0x16L);
          break;
        }

        //LAB_800e5ccc
        backgroundLoaded_800cab10.setu(0);
        loadDrgnBinFile(0x2L, fileIndex.get(), 0, getMethodAddress(SMap.class, "loadBackground", Value.class, long.class, long.class), 0, 0x4L);
        noop_800e4fec();
        _800cb430.setu(0x6L);
        break;

      case 0x4:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800caafc.setu(submapCut_80052c30);
        _800cab00.setu(_80052c34);
        getDrgnFileFromNewRoot(submapCut_80052c30.get(), drgnIndex, fileIndex);
        _800cb430.setu(0x11L);
        break;

      case 0x6:
        if(backgroundLoaded_800cab10.get() != 0) {
          _800cb430.setu(0x7L);
        }

        break;

      case 0x7:
        if(_800cb440.get() == 0) {
          //LAB_800e5d60
          _800cb430.setu(0x9L);
        } else {
          _800cb430.setu(0x8L);
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
        _800cb430.setu(0xaL);
        break;

      case 0xa:
        loadingStage_800c68e4.setu(0);
        executeSceneGraphicsLoadingStage((int)_800caaf8.get());
        _800cb430.setu(0xbL);
        break;

      case 0xb:
        executeSceneGraphicsLoadingStage((int)_800caaf8.get());
        if(loadingStage_800c68e4.get() == 0xaL) {
          if(FUN_800e5518(0)) {
            biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.set(_800caaf8.get());
          }

          //LAB_800e5e94
          FUN_800e770c();
          _800bdc34.setu(0);
          _80052c44.setu(0);
          FUN_800136dc(0x2L, 0xaL);
          _800cab24.set(FUN_800ea974(_800caaf4.get()));
          FUN_800e4ac8();
          _800cb430.setu(0xcL);
          _800bc0b8.setu(0);
          _800c6ae0.setu(0);
        }

        break;

      case 0xc:
        _80052c44.setu(0);
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        if(_8007a398.get(0x10L) != 0 && _800bb0ab.get() == 0) {
          FUN_800e5534(-0x1L, 0x3ffL);
        }

        break;

      case 0xd:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e5fa4
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e5fc0
        if(a0) {
          _800cb430.setu(0xeL);
          _800cab28.setu(0);
        }

        break;

      case 0xe:
        _800caaf0.setu(_800bdc38);
        _80052c44.setu(0x2L);

        if(_800bdc38.get() == 0 || _800bdc38.get() == 0) {
          if(_800bdc38.get() != 0) {
            FUN_80022590();
          }

          //LAB_800e6018
          _800c6aac.setu(0xaL);

          switch((int)_800caaf0.get()) {
            case 0x5:
              if(_800bb0ac.get() != 0) {
                _800cb430.setu(0x12L);
                _800f7e4c.setu(0);
                break;
              }

              // Fall through

            case 0x19:
            case 0x23:
            case 0xa:
              _800cb430.setu(0xfL);
              break;

            case 0x14:
              _800cb430.setu(0xcL);
              _800f7e4c.setu(0);
              FUN_800e5534(_800f7e2c.offset(_800bac60.get() * 8).get(), _800f7e30.offset(_800bac60.get() * 8).get());
              index_80052c38.set(_800f7e30.get());
              break;
          }
        }

        break;

      case 0xf:
        _80052c44.setu(0);
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bc0b8.setu(0);
        _800f7e4c.setu(0);
        _800cb430.setu(0xcL);
        if(_800bdc34.get() != 0) {
          FUN_800e5534(submapCut_80052c30.get(), _80052c34.get());
        }

        break;

      case 0x10:
        _800cb430.setu(0x3L);
        _80052c44.setu(0x3L);
        _800f7e4c.setu(0);
        break;

      case 0x11:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());

        if(FUN_800e5518(0)) {
          biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.derefAs(BigStruct.class).us_12a.set(1);
        }

        //LAB_800e61bc
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e61fc
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e6218
        if(a0) {
          _800cb430.setu(0x3L);

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

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e62b0
          a0 = _800cab28.get() >= 0xaL;
          _800cab28.addu(0x1L);
        } else {
          a0 = true;
        }

        //LAB_800e62cc
        if(a0) {
          _8004dd24.setu(0x8L);
          pregameLoadingStage_800bb10c.setu(0);
          vsyncMode_8007a3b8.setu(0x2L);
          _80052c44.setu(0x5L);
          _800f7e4c.setu(0);
          _800bc0b8.setu(0);
        }

        break;

      case 0x13:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _80052c44.setu(0x5L);
        _8004dd24.setu(0x6L);
        pregameLoadingStage_800bb10c.setu(0);
        vsyncMode_8007a3b8.setu(0x2L);
        _800f7e4c.setu(0);
        _800bc0b8.setu(0);
        break;

      case 0x14:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
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
          _8004dd24.setu(0x2L);
          vsyncMode_8007a3b8.setu(0x2L);
          pregameLoadingStage_800bb10c.setu(0);

          //LAB_800e6484
          _80052c44.setu(0x5L);

          //LAB_800e6490
          _800f7e4c.setu(0);
          _800bc0b8.setu(0);
        }

        break;

      case 0x15:
        FUN_800e5104((int)_800caaf8.get(), _800cab24.deref());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
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
          _8004dd24.setu(0x9L);
          pregameLoadingStage_800bb10c.setu(0);
          vsyncMode_8007a3b8.setu(0x2L);
          _800f7e4c.setu(0);
          _800bc0b8.setu(0);
        }

        break;

      case 0x16:
        _8004dd24.setu(0xaL);
        vsyncMode_8007a3b8.setu(0x2L);
        _80052c44.setu(0x1L);
        pregameLoadingStage_800bb10c.setu(0);
        break;

      case 0x17:
        _8004dd24.setu(0x2L);
        vsyncMode_8007a3b8.setu(0x2L);
        pregameLoadingStage_800bb10c.setu(0);
        break;
    }

    //caseD_5
  }

  @Method(0x800e6504L)
  public static void getDrgnFileFromNewRoot(final long index, final UnsignedIntRef drgnIndexOut, final UnsignedIntRef fileIndexOut) {
    NewRootEntryStruct entry = newRootPtr_800cb458.deref().entries_0000.get((int)index);

    final long drgnIndexA = entry.ub_01.get() >>> 5;
    final long drgnIndexB = entry.ub_03.get() >>> 5;

    boolean second;
    if(drgnIndexA == drgnBinIndex_800bc058.get() - 1) {
      drgnIndexOut.set(drgnIndexA);
      second = false;
    } else if(drgnIndexB == drgnBinIndex_800bc058.get() - 1 && drgnIndexB <= _800bac60.get()) {
      drgnIndexOut.set(drgnIndexB);
      second = true;
      //LAB_800e6570
    } else if(drgnIndexB >= 0x4L) {
      drgnIndexOut.set(drgnIndexA);
      second = false;
    } else if(drgnIndexB <= _800bac60.get()) {
      //LAB_800e6580
      drgnIndexOut.set(drgnIndexB);
      second = true;
    } else {
      //LAB_800e658c
      drgnIndexOut.set(drgnIndexA);
      second = false;
    }

    //LAB_800e6594
    long t0;
    if(drgnIndexOut.get() == 0 || drgnIndexOut.get() == 0x1L || drgnIndexOut.get() == 0x2L || drgnIndexOut.get() == 0x3L) {
      //LAB_800e65bc
      //LAB_800e65cc
      t0 = 0x4L;
    } else {
      t0 = 0;
    }

    //LAB_800e65d0
    drgnIndexOut.incr();

    long v1_0;
    long v0_0;
    if(second) {
      v1_0 = entry.ub_02.get();
      v0_0 = entry.ub_03.get();
    } else {
      //LAB_800e6604
      v1_0 = entry.ub_00.get();
      v0_0 = entry.ub_01.get();
    }

    v0_0 &= 0x1fL;

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
  public static void FUN_800e664c(final long a0, final long a1) {
    fillMemory(arr_800cb460.getAddress(), 0, 0x100L);

    NewRootEntryStruct entry = newRootPtr_800cb458.deref().entries_0000.get((int)a0);
    short offset = (short)(entry.ub_05.get() << 8 | entry.ub_04.get());
    if(offset < 0) {
      return;
    }

    //LAB_800e66dc
    for(int i = 0; i < (entry.ub_07.get() << 8 | entry.ub_06.get()); i++) {
      final long v1 = newRootPtr_800cb458.deref().uia_2000.get(offset / 4 + i).get();
      arr_800cb460.get((int)((v1 >> 8) / 4)).set(v1);
    }

    //LAB_800e671c
  }

  @Method(0x800e6730L)
  public static long FUN_800e6730(final long a0) {
    if(a0 >= 0x40L) {
      return 0;
    }

    return arr_800cb460.get((int)a0).get();
  }

  @Method(0x800e6798L)
  public static long FUN_800e6798(long a0, long a1, long x, long y, long z, SVECTOR a5) {
    assert false;
    return 0;
  }

  @Method(0x800e683cL)
  public static long FUN_800e683c(final ScriptStruct a0) {
    if(a0.params_20.get(0).deref().get() < 0x3L) {
      _800f7e50.setu(a0.params_20.get(0).deref().get());
    }

    //LAB_800e686c
    if(a0.params_20.get(0).deref().get() == 0x3L) {
      FUN_800e76b0(0x400L, 0x400L, a0.params_20.get(1).deref().get());
    }

    //LAB_800e688c
    a0.params_20.get(2).deref().set(_800f7e50.get());
    return 0;
  }

  @Method(0x800e68b4L)
  public static long FUN_800e68b4(final ScriptStruct a0) {
    final IntRef x = new IntRef();
    final IntRef y = new IntRef();
    getScreenOffset(x, y);
    a0.params_20.get(0).deref().set(x.get() & 0xffff_ffffL);
    a0.params_20.get(1).deref().set(y.get() & 0xffff_ffffL);
    return 0;
  }

  @Method(0x800e6904L)
  public static long FUN_800e6904(final ScriptStruct a0) {
    final int x = (int)a0.params_20.get(0).deref().get();
    final int y = (int)a0.params_20.get(1).deref().get();
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

  @Method(0x800e6a64L)
  public static long FUN_800e6a64(final ScriptStruct a0) {
    FUN_800e76b0(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800e6aa0L)
  public static long FUN_800e6aa0(final ScriptStruct a0) {
    a0.params_20.get(3).deref().set(FUN_800e7728(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get()));
    return 0;
  }

  @Method(0x800e6d4cL)
  public static void FUN_800e6d4c() {
    _800f7e54.setu(0);
  }

  @Method(0x800e6d58L)
  public static long FUN_800e6d58(long a0) {
    for(int i = 0; i < 0x2d; i++) {
      if(_800f7e58.offset(i * 0x4L).get() == a0) {
        return 0x1L;
      }
    }

    return 0;
  }

  @Method(0x800e6d9cL)
  public static void FUN_800e6d9c(final UnboundedArrayRef<EnvironmentStruct> envs, final long count) {
    long s0;
    long s1;
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

      long clutY = abs(s0.clutY_22.get());
      if(i < _800cb57c.get()) {
        if((clutY - 0x1f0L) >= 0x10L) {
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
      SetDrawTPage(MEMORY.ref(4, s0_0, DR_TPAGE::new), 0, 0x1L, s0.tpage_20.get());

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
        MEMORY.ref(2, tpagePacket).offset(0x20L).setu((0x1L << _1f8003c0.get()) - 0x1L);
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
        a0 >>= 16 - _1f8003c0.get();
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
        if(abs(matrix.get(x, y)) < 0x40L) {
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
    assert false;
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
    if(_800cbd38.deref()._00.get() == 0) {
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
    _800cbd64.setu(_800c6730);
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
      } else if((0x1L << _1f8003c0.get()) - 0x1L < a2) {
        a2 = (0x1L << _1f8003c0.get()) - 0x1L;
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
    final long[] sp38 = new long[10]; // dunno how many elements this can hold

    s1 = _800cb710.getAddress();

    //LAB_800e79b8
    for(int i = 0; i < _800cb57c.get(); i++) {
      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x1cL);
      v1 = _800cb560.get();
      v1 += screenOffsetX_800cb568.get();
      v1 += MEMORY.ref(2, s1).offset(0x1cL).get();
      MEMORY.ref(2, s1).offset(0x10L).setu(v1);
      v1 = _800cb564.get();
      v1 += screenOffsetY_800cb56c.get();
      v1 += MEMORY.ref(2, s1).offset(0x1eL).get();
      MEMORY.ref(2, s1).offset(0x12L).setu(v1);
      memcpy(s0, s1, 0x1c);
      insertElementIntoLinkedList(ot.org_04.deref().get((int)MEMORY.ref(2, s1).offset(0x20L).get()).getAddress(), s0);
      s1 += 0x24L;
    }

    //LAB_800e7a60
    //LAB_800e7a7c
    for(int i = 0; i < a2; i++) {
      sp10[i] = (((
        matrix_800c3548.get(6) * a1[i].transfer.getX() +
        matrix_800c3548.get(7) * a1[i].transfer.getY() +
        matrix_800c3548.get(8) * a1[i].transfer.getZ()
      ) >> 12) + matrix_800c3548.transfer.getZ()) >> (0x10L - _1f8003c0.get());
    }

    //LAB_800e7b08
    s3 = _800cb710.getAddress();
    s0 = _800cb57c.get();
    long s4 = _800cbb90.getAddress();

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
          long a1_0 = sp10[(int)a2_0] & 0xffffL;
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
          sp38[i] = Math.min(a3 + 0x32L, (0x1L << _1f8003c0.get()) - 0x1L);
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
        v0 = _800cb560.get();
        v0 += screenOffsetX_800cb568.get();
        v0 += MEMORY.ref(2, s1).offset(0x1cL).get();
        v0 += _800cb590.offset(i * 0xcL).get();
        MEMORY.ref(2, s1).offset(0x10L).setu(v0);
        v0 = _800cb564.get();
        v0 += screenOffsetY_800cb56c.get();
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
    if(x < -0x50L) {
      screenOffsetX_800cb568.subu(0x50L).subu(x);
      //LAB_800e7f80
    } else if(x > 0x50L) {
      //LAB_800e7f9c
      screenOffsetX_800cb568.addu(0x50L).subu(x);
    }

    //LAB_800e7fa8
    if(y < -0x28L) {
      screenOffsetY_800cb56c.subu(0x28L).subu(y);
      //LAB_800e7fbc
    } else if(y > 0x28L) {
      //LAB_800e7fd4
      screenOffsetY_800cb56c.addu(0x28L).subu(y);
    }

    //LAB_800e7fdc
    if(_800f7f0c.get() != 0) {
      screenOffsetX_800cb568.addu(_800cbd30);
      screenOffsetY_800cb56c.addu(_800cbd34);
      _800f7f0c.setu(0);
      return;
    }

    //LAB_800e8030
    if(screenOffsetX_800cb568.get() < _800cb574.get()) {
      //LAB_800e807c
      screenOffsetX_800cb568.setu(_800cb574.get());
    } else {
      //LAB_800e8070
      screenOffsetX_800cb568.setu(Math.min(_800cb570.get(), screenOffsetX_800cb568.get()));
    }

    //LAB_800e8080
    //LAB_800e8088
    if(screenOffsetY_800cb56c.get() < -_800cb578.get()) {
      screenOffsetY_800cb56c.setu(-_800cb578.get());
    } else {
      //LAB_800e80d0
      //LAB_800e80d8
      screenOffsetY_800cb56c.setu(Math.min(_800cb578.get(), screenOffsetY_800cb56c.get()));
    }

    //LAB_800e80dc
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
    setScreenOffsetIfNotSet(screenOffsetX_800cb568.get(), screenOffsetY_800cb56c.get());
    setGeomOffsetIfNotSet((int)screenOffsetX_800cb568.get(), (int)screenOffsetY_800cb56c.get());
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
    assert false;
  }

  @Method(0x800e82ccL)
  public static void FUN_800e82cc(final SVECTOR a0, final SVECTOR a1) {
    assert false;
  }

  @Method(0x800e866cL)
  public static void FUN_800e866c() {
    //LAB_800e86a4
    for(int i = 0; i < SomethingStructPtr_800d1a88.deref().count_0c.get(); i++) {
      final long v0 = abs((int)MEMORY.ref(2, SomethingStructPtr_800d1a88.deref().normals_08.get()).offset(i * 0x8L).offset(0x2L).getSigned()); //TODO
      SomethingStructPtr_800d1a88.deref().ptr_14.deref().get(i).bool_01.set(v0 > 0x400L);
    }

    //LAB_800e86f0
  }

  @Method(0x800e88a0L)
  public static long FUN_800e88a0(long a0, MATRIX a1, SVECTOR a2) {
    assert false;
    return 0;
  }

  @Method(0x800e8b40L)
  public static void FUN_800e8b40(final SomethingStruct a0, final long a1) {
    final int count = (int)a0.count_0c.get();

    memcpy(SomethingStruct2Arr_800cbe78.getAddress(), a1, count * 0xc);
    memcpy(_800cca78.getAddress(), a1 + count * 0xcL, count * 0x40);

    a0.ptr_14.set(SomethingStruct2Arr_800cbe78);
    a0.ptr_18.set(_800cca78.getAddress());
  }

  @Method(0x800e8bd8L)
  public static void FUN_800e8bd8(final SomethingStruct a0) {
    final TmdObjTable objTable = a0.objTableArrPtr_00.deref().get(0);
    a0.verts_04.set(objTable.vert_top_00);
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
  public static void FUN_800e8cd0(final TmdWithId tmd, final int tmdSize, final long a2, final long a3) {
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

  @Method(0x800e8e50L)
  public static void FUN_800e8e50() {
    assert false;
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
      out.add(MEMORY.ref(4, ss.verts_04.get()).offset(MEMORY.ref(2, t0).offset(i * 0x2L).get() * 0x8L).cast(SVECTOR::new)); //TODO
    }

    //LAB_800e93e0
    out.div(count);
    return 0x1L;
  }

  @Method(0x800ea4c8L)
  public static short FUN_800ea4c8(final short a0) {
    assert false;
    return 0;
  }

  @Method(0x800ea90cL)
  public static void FUN_800ea90c(final MediumStruct a0) {
    if(FUN_800e5518(0)) {
      index_80052c38.set(biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.get());
    }
  }

  @Method(0x800ea974L)
  public static MediumStruct FUN_800ea974(final long a0) {
    if((int)a0 < 0) {
      _800d1a90.callback_48.set(getFunctionAddress(SMap.class, "FUN_800ea96c", MediumStruct.class, long.class));
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
        _800d1a90.callback_48.set(getFunctionAddress(SMap.class, "FUN_800ea84c", MediumStruct.class, long.class));
        _800d1a90._44.set(0x1L);
      } else {
        //LAB_800eaa5c
        _800d1a90.callback_48.set(getFunctionAddress(SMap.class, "FUN_800ea90c", MediumStruct.class, long.class));
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
        loadDrgnBinFile(0, 5721, 0, getMethodAddress(SMap.class, "loadCreditsMrg", Value.class, long.class, long.class), _800bf0dc.get(), 0x4L);

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
        FUN_8002c150(0);
        vsyncMode_8007a3b8.setu(0x2L);
        pregameLoadingStage_800bb10c.setu(0);
        _8004dd24.setu(_800bf0ec);
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
    callbackIndex_8004ddc4.setu(0x19L);

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
      FUN_80012b1c(0x4L, getMethodAddress(SMap.class, "FUN_800edc50", long.class), 0);
    } else {
      //LAB_800ed91c
      _800bf0d8.setu(0x1L);
    }

    //LAB_800ed924
  }

  @Method(0x800ed960L)
  public static long FUN_800ed960() {
    if(_800bf0d8.get() != 0x1L || doubleBufferFrame_800bb108.get() == 0) {
      //LAB_800ed9d0
      return 0;
    }

    FUN_8001e29c(0x8L);
    FUN_800fb7cc(_800f970c.offset(_800bf0dc.get() * 16).getAddress(), _800bf0dc.get());

    _800bf0d8.setu(0x2L);
    callbackIndex_8004ddc4.set(0x16L);

    //LAB_800ed9d4
    return 0x1L;
  }

  @Method(0x800ed9e4L)
  public static long FUN_800ed9e4() {
    if(_800bf0d8.get() != 0x2) {
      return 0;
    }

    //LAB_800eda0c
    final long s0;
    if(_800bf0dc.get() == 0 && _8007a398.get(0x800L) != 0) {
      s0 = 0x1L;
    } else {
      s0 = 0;
    }

    //LAB_800eda38
    if(FUN_800fb90c() == 0 && s0 == 0) {
      return 0x1L;
    }

    //LAB_800eda50
    _800bf0d8.setu(0x3L);
    callbackIndex_8004ddc4.setu(0x17L);
    FUN_800136dc(0x1L, 0x1L);
    ClearImage(new RECT((short)0, (short)0, (short)1023, (short)511), (byte)0, (byte)0, (byte)0);
    DrawSync(0);
    setWidthAndFlags(640L, 0);

    //LAB_800edab4
    return 0x1L;
  }

  @Method(0x800edac4L)
  public static long stopIntroFmv() {
    if(_800bf0b4.get() == 0) {
      //LAB_800edb30
      return 0;
    }

    if(_800bf0d8.get() != 0x3L) {
      return 0;
    }

    stopFmv(_800f970c.offset(_800bf0dc.get() * 16).getAddress());
    FUN_80012bb4();

    callbackIndex_8004ddc4.setu(0x18L);
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
      callbackIndex_8004ddc4.setu(0);
      return 0x1L;
    }

    //LAB_800edb84
    return 0;
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
  public static void loadCreditsMrg(final Value address, final long fileSize, final long fileIndex) {
    final MrgFile mrg = address.deref(4).cast(MrgFile::new);

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

    removeFromLinkedList(address.get());
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

    if(_800c6870.get() == -0x1L) {
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

        if(drgnFileIndices_800f982c.offset(submapCut_80052c30.get() * 0x2L).getSigned() != 0) {
          loadDrgnBinFile(0, drgnFileIndices_800f982c.offset(submapCut_80052c30.get() * 0x2L).getSigned(), 0, getMethodAddress(SMap.class, "FUN_800eeddc", Value.class, long.class, long.class), 0, 0x2L);
          loadDrgnBinFile(0, drgnFileIndices_800f982c.offset(submapCut_80052c30.get() * 0x2L).getSigned() + 0x1L, 0, getMethodAddress(SMap.class, "FUN_800eeddc", Value.class, long.class, long.class), 0x1L, 0x4L);

          if(submapCut_80052c30.get() == 0x2a1L) {
            loadDrgnBinFile(0, 7610, 0, getMethodAddress(SMap.class, "FUN_800eeddc", Value.class, long.class, long.class), 0x2L, 0x4L);
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

          FUN_800f4244(_800d4bf0.get(), _800f9e5c.getAddress(), _800f9e5e.getAddress(), 0x1L);
          DrawSync(0);
          StoreImage(sp20, _800d4bd4.get());
          removeFromLinkedList(_800d4bf0.get());
        }

        _800f9e5a.addu(0x1L);
        break;

      case 0x4:
        bigStruct_800d4bf8.ub_9d.set(0x91);

        //TODO
        FUN_80020a00(bigStruct_800d4bf8, _800d4be8.deref(4).offset(_800d4be8.deref(4).offset(0x08L)).cast(ExtendedTmd::new), _800d4be8.deref(4).offset(_800d4be8.deref(4).offset(0x10L)).getAddress());

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

  @Method(0x800eeddcL)
  public static void FUN_800eeddc(final Value address, final long fileSize, final long a2) {
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
    _1f8003e8.setu(a0.us_a0.get());
    _1f8003ec.setu(a0.scaleVector_fc.pad.get());

    final MATRIX lw = new MATRIX();

    //LAB_800eee94
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      GsGetLw(a0.ObjTable_0c.top.deref().get(0).coord2_04.deref(), lw);
      GsSetLightMatrix(lw);

      PushMatrix();
      CPU.CTC2((matrix.get(1) & 0xffffL) << 16 | matrix.get(0) & 0xffffL, 0); //
      CPU.CTC2((matrix.get(3) & 0xffffL) << 16 | matrix.get(2) & 0xffffL, 1); //
      CPU.CTC2((matrix.get(5) & 0xffffL) << 16 | matrix.get(4) & 0xffffL, 2); // Rotation matrix
      CPU.CTC2((matrix.get(7) & 0xffffL) << 16 | matrix.get(6) & 0xffffL, 3); //
      CPU.CTC2(                                  matrix.get(8) & 0xffffL, 4); //

      CPU.CTC2(matrix.transfer.getX(), 5); //
      CPU.CTC2(matrix.transfer.getY(), 6); // Translation vector
      CPU.CTC2(matrix.transfer.getZ(), 7); //
      FUN_80021258(a0.ObjTable_0c.top.deref().get(i));
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
      long v1 = MEMORY.ref(2, imageAddress).offset(i * 0x2L).get();
      long v0;
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

  @Method(0x800ef0f8L)
  public static void FUN_800ef0f8(final BigStruct struct, final long a1) {
    if(MEMORY.ref(2, a1).offset(0x1eL).getSigned() != struct.coord2_14.coord.transfer.getX() || MEMORY.ref(2, a1).offset(0x20L).getSigned() != struct.coord2_14.coord.transfer.getY() || MEMORY.ref(2, a1).offset(0x22L).getSigned() != struct.coord2_14.coord.transfer.getZ()) {
      //LAB_800ef154
      if(MEMORY.ref(4, a1).offset(0x4L).get() != 0) {
        if(MEMORY.ref(4, a1).offset(0x0L).get() % MEMORY.ref(4, a1).offset(0x30L).get() == 0) {
          final Struct20 a0 = FUN_800f03c0(_800d4ec0);
          a0._00.set(0);
          a0._18.set((short)MEMORY.ref(2, a1).offset(0x38L).get());

          final long v0 = MEMORY.ref(4, a1).offset(0x28L).get();
          if((int)v0 < 0) {
            a0._08.set(-v0 << 12);
            a0._04.set(-a0._04.get() / 20);
          } else if((int)v0 > 0) {
            //LAB_800ef1e0
            a0._08.set(0);
            a0._04.set((v0 << 12) / 20);
          } else {
            //LAB_800ef214
            a0._08.set(0);
            a0._04.set(0);
          }

          //LAB_800ef21c
          a0.transfer.set(struct.coord2_14.coord.transfer);
        }
      }

      //LAB_800ef240
      if(MEMORY.ref(4, a1).offset(0x8L).get() != 0) {
        if(MEMORY.ref(4, a1).get() % MEMORY.ref(4, a1).offset(0x34L).get() == 0) {
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

          if(MEMORY.ref(4, a1).offset(0x10L).get() != 0) {
            //LAB_800ef3e8
            s1._00.set((short)2);
            s1._02.set((short)3);

            //LAB_800ef3f8
          } else {
            s1._00.set((short)0);
            s1._02.set((short)MEMORY.ref(2, a1).offset(0x1cL).get());

            final long v1 = MEMORY.ref(2, a1).offset(0x1cL).getSigned();
            if(v1 == 0) {
              MEMORY.ref(2, a1).offset(0x1cL).setu(0x1L);
              //LAB_800ef3f8
            } else if(v1 == 0x1L) {
              //LAB_800ef3d8
              MEMORY.ref(2, a1).offset(0x1cL).setu(0);
            }
          }

          //LAB_800ef3fc
          getScreenOffset(s1.x_18, s1.y_1c);
          s1._04.set((short)0);
          s1._06.set((short)150);

          final MATRIX ls = new MATRIX();
          GsGetLs(struct.coord2_14, ls);
          CPU.CTC2((ls.get(1) & 0xffffL) << 16 | ls.get(0) & 0xffffL, 0);
          CPU.CTC2((ls.get(3) & 0xffffL) << 16 | ls.get(2) & 0xffffL, 1);
          CPU.CTC2((ls.get(5) & 0xffffL) << 16 | ls.get(4) & 0xffffL, 2);
          CPU.CTC2((ls.get(7) & 0xffffL) << 16 | ls.get(6) & 0xffffL, 3);
          CPU.CTC2(                              ls.get(8) & 0xffffL, 4);
          CPU.CTC2(ls.transfer.getX(), 5);
          CPU.CTC2(ls.transfer.getY(), 6);
          CPU.CTC2(ls.transfer.getZ(), 7);

          final long v1 = s1._02.get();
          if(v1 == 0) {
            //LAB_800ef4b4
            s1.sz3div4_4c.set(RotTransPers4(sp0x48, sp0x50, sp0x58, sp0x60, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new UnsignedIntRef(), new UnsignedIntRef()));
          } else if(v1 == 0x1L) {
            //LAB_800ef484
            //LAB_800ef4b4
            s1.sz3div4_4c.set(RotTransPers4(sp0x68, sp0x70, sp0x78, sp0x80, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new UnsignedIntRef(), new UnsignedIntRef()));
          } else if(v1 == 0x3L) {
            //LAB_800ef4a0
            //LAB_800ef4b4
            s1.sz3div4_4c.set(RotTransPers4(sp0x28, sp0x30, sp0x38, sp0x40, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new UnsignedIntRef(), new UnsignedIntRef()));
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
      if(MEMORY.ref(4, a1).offset(0xcL).get() != 0) {
        if(MEMORY.ref(4, a1).get() % MEMORY.ref(4, a1).offset(0x30L).get() == 0) {
          final Struct54 s1 = FUN_800f0400(_800d4e68);
          s1._00.set((short)1);
          s1._02.set((short)2);
          getScreenOffset(s1.x_18, s1.y_1c);

          final SVECTOR vert0 = new SVECTOR().set((short)-MEMORY.ref(2, a1).offset(0x28L).get(), (short)0, (short)-MEMORY.ref(2, a1).offset(0x28L).get());
          final SVECTOR vert1 = new SVECTOR().set((short) MEMORY.ref(2, a1).offset(0x28L).get(), (short)0, (short)-MEMORY.ref(2, a1).offset(0x28L).get());
          final SVECTOR vert2 = new SVECTOR().set((short)-MEMORY.ref(2, a1).offset(0x28L).get(), (short)0, (short) MEMORY.ref(2, a1).offset(0x28L).get());
          final SVECTOR vert3 = new SVECTOR().set((short) MEMORY.ref(2, a1).offset(0x28L).get(), (short)0, (short) MEMORY.ref(2, a1).offset(0x28L).get());

          s1._04.set((short)0);
          s1._06.set((short)MEMORY.ref(2, a1).offset(0x38L).get());

          final MATRIX ls = new MATRIX();
          GsGetLs(struct.coord2_14, ls);
          CPU.CTC2((ls.get(1) & 0xffffL) << 16 | ls.get(0) & 0xffffL, 0);
          CPU.CTC2((ls.get(3) & 0xffffL) << 16 | ls.get(2) & 0xffffL, 1);
          CPU.CTC2((ls.get(5) & 0xffffL) << 16 | ls.get(4) & 0xffffL, 2);
          CPU.CTC2((ls.get(7) & 0xffffL) << 16 | ls.get(6) & 0xffffL, 3);
          CPU.CTC2(                              ls.get(8) & 0xffffL, 4);
          CPU.CTC2(ls.transfer.getX(), 5);
          CPU.CTC2(ls.transfer.getY(), 6);
          CPU.CTC2(ls.transfer.getZ(), 7);

          // The real code actually passes the same reference for sxyz 1 and 2
          s1.sz3div4_4c.set(RotTransPers4(vert0, vert1, vert2, vert3, s1.sxyz0_20, s1.sxyz1_28, s1.sxyz2_30, s1.sxyz3_38, new UnsignedIntRef(), new UnsignedIntRef()));

          if(s1.sz3div4_4c.get() < 0x29L) {
            s1.sz3div4_4c.set(0x29L);
          }

          //LAB_800ef6a0
          final long a0_0 = (s1.sxyz3_38.getX() - s1.sxyz0_20.getX()) / 2 << 16;
          s1._08.set(a0_0);
          s1._0c.set(a0_0 / MEMORY.ref(2, a1).offset(0x38L).getSigned());
          s1._10.set(0);

          s1.sxyz0_20.setZ((short)((s1.sxyz3_38.getX() + s1.sxyz0_20.getX()) / 2));
          s1.sxyz1_28.setZ((short)((s1.sxyz3_38.getY() + s1.sxyz0_20.getY()) / 2));

          s1._40.set(0x80_0000L / MEMORY.ref(2, a1).offset(0x38L).getSigned());
          s1._44.set(0x80_0000L);
          s1.colour_48.set(0x80L);
        }
      }

      //LAB_800ef728
      if(MEMORY.ref(4, a1).offset(0x18L).get() == 0x1L) {
        if((short)bigStruct_800c6748.us_128.get() != -0x1L) {
          FUN_800f0644(struct, a1);
        }
      }
    }

    //LAB_800ef750
    MEMORY.ref(2, a1).offset(0x1eL).setu(struct.coord2_14.coord.transfer.getX());
    MEMORY.ref(2, a1).offset(0x20L).setu(struct.coord2_14.coord.transfer.getY());
    MEMORY.ref(2, a1).offset(0x22L).setu(struct.coord2_14.coord.transfer.getZ());
    MEMORY.ref(4, a1).addu(0x1L);
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
        s0 = s1.next_1c.deref();
      } else {
        //LAB_800ef804
        s0.transfer.y.decr();

        _800d4d40.coord2_14.coord.transfer.set(s0.transfer);

        s0._08.add(s0._04);

        _800d4d40.scaleVector_fc.setX((int)s0._08.get());
        _800d4d40.scaleVector_fc.setY((int)s0._08.get());
        _800d4d40.scaleVector_fc.setZ((int)s0._08.get());

        FUN_800214bc(_800d4d40);
        FUN_800211d8(_800d4d40);

        _800d4d40.us_9e.set(0);
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

    long[] sp10 = new long[4];
    sp10[0] = _800d6bdc.get();
    sp10[1] = _800d6be0.get();
    sp10[2] = _800d6be4.get();
    sp10[3] = _800d6be8.get();

    long[] sp20 = new long[4];
    sp20[0] = 0x40L;

    long[] sp30 = new long[4];
    sp30[0] = _800d6bec.get();
    sp30[1] = _800d6bf0.get();
    sp30[2] = _800d6bf4.get();
    sp30[3] = _800d6bf8.get();

    long[] sp40 = new long[4];
    sp40[0] = _800d6bfc.get();
    sp40[1] = _800d6c00.get();
    sp40[2] = _800d6c04.get();
    sp40[3] = _800d6c08.get();

    long[] sp50 = new long[4];
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
        s0 = s1.next_50.deref();
      } else {
        //LAB_800efa08
        long packet = linkedListAddress_1f8003d8.get();
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
            MEMORY.ref(2, packet).offset(0x0eL).setu(GetClut(960L, 464L)); // CLUT
            MEMORY.ref(2, packet).offset(0x16L).setu(GetTPage(0, 0x2L, 960L, 320L)); // TPAGE
          } else {
            //LAB_800efb64
            MEMORY.ref(2, packet).offset(0x0eL).setu(_800d6068.offset(2, 0xeL)); // CLUT
            MEMORY.ref(2, packet).offset(0x16L).setu(_800d6050.offset(2, 0xeL)); // TPAGE
          }
        } else if(v1 == 0x1L) {
          //LAB_800efb7c
          v1 = s0._08.get() + s0._0c.get();
          s0._08.set(v1);
          v0 = (int)v1 >> 16;
          s0._10.set(v0);
          v0 = s0.sxyz0_20.getZ();
          v1 = (int)v1 >> 17;
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
    assert false;
  }

  @Method(0x800f00a4L)
  public static void FUN_800f00a4(final long a0, final long a1) {
    assert false;
  }

  @Method(0x800f0370L)
  public static void FUN_800f0370() {
    FUN_80020a00(_800d4d40, mrg_800d6d1c.getFile(4, ExtendedTmd::new), mrg_800d6d1c.getFile(5)); //TODO file type
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
    assert false;
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
    assert false;
  }

  @Method(0x800f0644L)
  public static void FUN_800f0644(BigStruct a0, long a1) {
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

  //TODO struct
  @Method(0x800f2788L)
  public static void FUN_800f2788() {
    final long[] sp10 = new long[8];

    FUN_80020a00(_800d5eb0, mrg_800d6d1c.getFile(2, ExtendedTmd::new), mrg_800d6d1c.getFile(3));
    _800d5598.offset(4, 0x28L).setu(0);
    _800d5598.offset(4, 0x34L).setu(0x50L);
    _800d5598.offset(4, 0x6cL).setu(0);
    _800d5598.offset(4, 0x70L).setu(0x1_f800L);
    _800d5598.offset(4, 0x74L).setu(0);
    _800d5598.offset(4, 0x78L).setu(0);
    _800d5598.offset(2, 0x7cL).setu(0);
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
    assert false;
  }

  @Method(0x800f31bcL)
  public static void FUN_800f31bc() {
    getScreenOffset(_800c69fc.deref(4).offset(0x10).cast(IntRef::new), _800c69fc.deref(4).offset(0x14).cast(IntRef::new));

    if(_800babc8.offset(1, 0x4e3L).get() != 0) {
      return;
    }

    if(_800bb168.get() != 0) {
      return;
    }

    final long a0 = _800babc8.offset(4, 0x4e8L).get();
    if(a0 != 0x1L) {
      _800f9e9c.setu(0);
    }

    //LAB_800f321c
    if((_8007a398.get() & 0x8L) != 0) {
      if(a0 == 0) {
        _800babc8.offset(4, 0x4e8L).setu(0x1L);
        //LAB_800f3244
      } else if(a0 == 0x1L) {
        _800babc8.offset(4, 0x4e8L).setu(0x2L);
      } else if(a0 == 0x2L) {
        _800babc8.offset(4, 0x4e8L).setu(0);
        _800f9e9c.setu(0);
      }
      //LAB_800f3260
    } else if((_8007a398.get() & 0x4) != 0) {
      if(a0 == 0) {
        //LAB_800f3274
        _800babc8.offset(4, 0x4e8L).setu(0x2L);
        //LAB_800f3280
      } else if(a0 == 0x1L) {
        _800babc8.offset(4, 0x4e8L).setu(0);
        _800f9e9c.setu(0);
        //LAB_800f3294
      } else if(a0 == 0x2L) {
        _800babc8.offset(4, 0x4e8L).setu(0x1L);

        //LAB_800f32a4
        _800f9e9c.setu(0);
      }
    }

    //LAB_800f32a8
    //LAB_800f32ac
    if(_800bb0b0.get() == 0) {
      return;
    }

    final long sp140 = _800d6cb8.offset(4, 0x0L).get();
    final long sp144 = _800d6cb8.offset(4, 0x4L).get();
    final long sp148 = _800d6cb8.offset(4, 0x8L).get();
    final long sp14c = _800d6cb8.offset(4, 0xcL).get();

    final long biggerStructAddr = biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().getAddress();
    final Memory.TemporaryReservation biggerStructTempRes = MEMORY.temp(0x100);
    final Value biggerStructTemp = biggerStructTempRes.get();
    final BiggerStruct<?> sp10 = new BiggerStruct<>(biggerStructTemp, VoidRef::new);

    //LAB_800f3328
    for(int i = 0; i < 0x10; i++) {
      biggerStructTemp.offset(i * 0x10L).offset(0x0L).setu(MEMORY.ref(4, biggerStructAddr).offset(i * 0x10L).offset(0x0L));
      biggerStructTemp.offset(i * 0x10L).offset(0x4L).setu(MEMORY.ref(4, biggerStructAddr).offset(i * 0x10L).offset(0x4L));
      biggerStructTemp.offset(i * 0x10L).offset(0x8L).setu(MEMORY.ref(4, biggerStructAddr).offset(i * 0x10L).offset(0x8L));
      biggerStructTemp.offset(i * 0x10L).offset(0xcL).setu(MEMORY.ref(4, biggerStructAddr).offset(i * 0x10L).offset(0xcL));
    }

    final MATRIX sp0x120 = new MATRIX();
    GsGetLs(sp10.innerStruct_00.derefAs(BigStruct.class).coord2_14, sp0x120);

    //TODO what's up with all the unused vars? Did I miss something?

    PushMatrix();
    CPU.CTC2((sp0x120.get(1) & 0xffffL) << 16 | sp0x120.get(0) & 0xffffL, 0); //
    CPU.CTC2((sp0x120.get(3) & 0xffffL) << 16 | sp0x120.get(2) & 0xffffL, 1); //
    CPU.CTC2((sp0x120.get(5) & 0xffffL) << 16 | sp0x120.get(4) & 0xffffL, 2); // Rotation matrix
    CPU.CTC2((sp0x120.get(7) & 0xffffL) << 16 | sp0x120.get(6) & 0xffffL, 3); //
    CPU.CTC2(sp0x120.get(8) & 0xffffL, 4); //
    CPU.CTC2(sp0x120.transfer.getX(), 5); //
    CPU.CTC2(sp0x120.transfer.getY(), 6); // Translation vector
    CPU.CTC2(sp0x120.transfer.getZ(), 7); //
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
    long sp114 = 0; //2b
    sp150 = (int)((sp158 & 0xffff_0000L) - (sp150 & 0xffff_0000L) << 16);
    CPU.MTC2(sp110, 0); // Vector XY 0
    CPU.MTC2(sp114, 1); // Vector Z 0
    CPU.COP2(0x180001L); // Perspective transform
    long sp118 = CPU.MFC2(14); // Screen XY 2
    sp160 = CPU.MFC2(8); // IR0 - 16-bit accumulator (interpolate)
    sp164 = CPU.CFC2(31); // LZCR - count of leading 1's
    sp168 = CPU.MFC2(19) / 0x4L; // Screen Z 3
    PopMatrix();

    _800c69fc.deref(4).offset(0x8L).setu(sp118 & 0xffffL);
    _800c69fc.deref(4).offset(0xcL).setu(sp118 >>> 16);

    if(_800bb0b0.get() == 0x1L) {
      if(_800f9e9c.get() < 0x21L) {
        FUN_800f352c();
        _800f9e9c.addu(0x1L);
      }
      //LAB_800f3508
    } else if(_800bb0b0.get() == 0x2L) {
      FUN_800f352c();
    }

    //LAB_800f3518
  }

  @Method(0x800f352cL)
  public static void FUN_800f352c() {
    assert false;
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
    if(_800bb0b0.get() > 0) {
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
    assert false;
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
          MEMORY.ref(4, s0).offset(0x10L).setu((MEMORY.ref(4, s1).offset(0x1cL).get() << 16) - (FUN_800133ac() * MEMORY.ref(2, s1).offset(0x18L).get() & 0xffff_ffffL));
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
            MEMORY.ref(4, s0).offset(0x10L).setu((MEMORY.ref(4, s1).offset(0x1cL).get() << 16) - FUN_800133ac() * MEMORY.ref(2, s1).offset(0x18L).getSigned());
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
    long sp20;
    long sp24;

    //TODO struct with a pointer to itself
    long s1 = _800d5eb0.us_128.getAddress();
    long s0 = MEMORY.ref(4, s1).offset(0x38L).get();

    final IntRef sox = new IntRef();
    final IntRef soy = new IntRef();
    getScreenOffset(sox, soy);
    sp20 = sox.get();
    sp24 = soy.get();

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
        MEMORY.ref(2, a1).offset(0x16L).setu(_800d5eb0.ui_1b0.get() & 0xffffL);
        MEMORY.ref(2, a1).offset(0x18L).setu(sp10);
        MEMORY.ref(2, a1).offset(0x1aL).setu(sp1a);
        MEMORY.ref(1, a1).offset(0x1cL).setu(0x40L);
        MEMORY.ref(1, a1).offset(0x1dL).setu(0x3fL);
        MEMORY.ref(2, a1).offset(0x20L).setu(sp12);
        MEMORY.ref(2, a1).offset(0x22L).setu(sp1a);
        MEMORY.ref(1, a1).offset(0x24L).setu(0x5fL);
        MEMORY.ref(1, a1).offset(0x25L).setu(0x3fL);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)MEMORY.ref(4, s0).offset(0x34L).get()).getAddress(), a1);
        v0 = MEMORY.ref(2, s0).offset(0x2L).get();
        s1 = s0;
        v0++;
        MEMORY.ref(2, s0).offset(0x2L).setu(v0);
        s0 = MEMORY.ref(4, s0).offset(0x38L).get();
      }

      //LAB_800f41b0
    }

    //LAB_800f41bc
  }

  @Method(0x800f41dcL)
  public static void FUN_800f41dc() {
    assert false;
  }

  @Method(0x800f4244L)
  public static void FUN_800f4244(final long a0, final long a1, final long a2, final long a3) {
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
        long v1 = _800bb110.getAddress();
        v1 += (tim.imageRect.deref().y.get() >>> 8 & 0x1L) * 0x2L;
        v1 += (a3 & 0b11) * 0x4L;
        v1 += (tim.flags.get() & 0b11) << 0x10L;
        long v0 = MEMORY.ref(2, v1).get() | (tim.imageRect.deref().x.get() & 0x3c0L) / 0x40L;
        MEMORY.ref(2, a1).setu(v0);
        LoadImage(tim.imageRect.deref(), tim.imageAddress.get());
      }
    }

    tmp.release();

    //LAB_800f4338
  }

  @Method(0x800f4354L)
  public static void FUN_800f4354() {
    if(bigStruct_800c6748.us_128.get() == 0xffffL) {
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
        FUN_800f00a4(_800f9e74.get(), _800f9e70.get());
        FUN_800efe7c();
      }
    }
  }

  @Method(0x800f4420L)
  public static void FUN_800f4420() {
    assert false;
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
      loadMiscTextures(0xbL);
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
  public static void loadMiscTextures(final long a0) {
    long[] sp00 = new long[11];

    //LAB_800f478c
    for(int v0 = 0; v0 < 2; v0++) {
      sp00[v0 * 4    ] = _800d6cf0.offset(v0 * 0x10L).get();
      sp00[v0 * 4 + 1] = _800d6cf4.offset(v0 * 0x10L).get();
      sp00[v0 * 4 + 2] = _800d6cf8.offset(v0 * 0x10L).get();
      sp00[v0 * 4 + 3] = _800d6cfc.offset(v0 * 0x10L).get();
    }

    sp00[ 8] = _800d6d10.get();
    sp00[ 9] = _800d6d14.get();
    sp00[10] = _800d6d18.get();

    //LAB_800f47f0
    for(int s2 = 0; s2 < a0; s2++) {
      final TimHeader sp40 = parseTimHeader(_800f9eb0.offset(s2 * 0x4L).deref(4).offset(0x4L));
      LoadImage(sp40.imageRect, sp40.imageAddress.get());

      long a1 = ((sp40.imageRect.y.get() >>> 8) & 0b1) << 1;
      a1 += (sp00[s2] & 0b11) << 2;
      a1 += (sp40.flags.get() & 0b11) << 4;

      _800d6050.offset(s2 * 0x2L).setu(_800bb110.offset(a1).get() | (sp40.imageRect.x.get() & 0x3c0) >>> 6);
      _800d6068.offset(s2 * 0x2L).setu(sp40.clutRect.y.get() * 0x40 | (sp40.clutRect.x.get() & 0x3f0) >>> 4);

      LoadImage(sp40.clutRect, sp40.clutAddress.get());
    }

    //LAB_800f48a8
  }
}
