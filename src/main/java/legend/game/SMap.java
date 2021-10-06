package legend.game;

import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.memory.types.VoidRef;
import legend.game.types.BigStruct;
import legend.game.types.BiggerStruct;
import legend.game.types.DR_MODE;
import legend.game.types.GsOT;
import legend.game.types.GsOT_TAG;
import legend.game.types.GsRVIEW2;
import legend.game.types.HmdSomethingStruct;
import legend.game.types.MathStruct;
import legend.game.types.WeirdTimHeader;

import java.util.function.Function;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SStrm.FUN_800fb7cc;
import static legend.game.SStrm.FUN_800fb90c;
import static legend.game.SStrm.stopFmv;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_80013200;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.FUN_800136dc;
import static legend.game.Scus94491BpeSegment.FUN_8001524c;
import static legend.game.Scus94491BpeSegment.FUN_80015310;
import static legend.game.Scus94491BpeSegment.FUN_80015a68;
import static legend.game.Scus94491BpeSegment.FUN_80015ab4;
import static legend.game.Scus94491BpeSegment.FUN_80015b00;
import static legend.game.Scus94491BpeSegment.FUN_80015b98;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001ada0;
import static legend.game.Scus94491BpeSegment.FUN_8001ae90;
import static legend.game.Scus94491BpeSegment.FUN_8001c60c;
import static legend.game.Scus94491BpeSegment.FUN_8001e29c;
import static legend.game.Scus94491BpeSegment.FUN_8001eadc;
import static legend.game.Scus94491BpeSegment.FUN_8001f3d0;
import static legend.game.Scus94491BpeSegment.FUN_8001ffb0;
import static legend.game.Scus94491BpeSegment._1f8003c0;
import static legend.game.Scus94491BpeSegment._1f8003c4;
import static legend.game.Scus94491BpeSegment._1f8003cc;
import static legend.game.Scus94491BpeSegment._1f8003e8;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._80010544;
import static legend.game.Scus94491BpeSegment.addToLinkedListHead;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateBiggerStruct;
import static legend.game.Scus94491BpeSegment.fillMemory;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
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
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b3f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b430;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b750;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b780;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b7e0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b8f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b900;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c4a0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c6f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003cce0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003cfb0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003d690;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003d9d0;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.PopMatrix;
import static legend.game.Scus94491BpeSegment_8003.PushMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotTransPersN;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.TransposeMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.insertCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setTransferVector;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040780;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040b90;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004ddc0;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8005._80050274;
import static legend.game.Scus94491BpeSegment_8005._800503f8;
import static legend.game.Scus94491BpeSegment_8005._80050424;
import static legend.game.Scus94491BpeSegment_8005._80052c30;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._80052c38;
import static legend.game.Scus94491BpeSegment_8005._80052c3c;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._80052c48;
import static legend.game.Scus94491BpeSegment_8005._80052c4c;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8007._8007a398;
import static legend.game.Scus94491BpeSegment_8007._8007a39c;
import static legend.game.Scus94491BpeSegment_8007._8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc8;
import static legend.game.Scus94491BpeSegment_800b._800bac60;
import static legend.game.Scus94491BpeSegment_800b._800bb0ab;
import static legend.game.Scus94491BpeSegment_800b._800bb0ac;
import static legend.game.Scus94491BpeSegment_800b._800bb0b0;
import static legend.game.Scus94491BpeSegment_800b._800bb0f4;
import static legend.game.Scus94491BpeSegment_800b._800bb0f8;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb164;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc05c;
import static legend.game.Scus94491BpeSegment_800b._800bc0b8;
import static legend.game.Scus94491BpeSegment_800b._800bd782;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7e8;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bd818;
import static legend.game.Scus94491BpeSegment_800b._800bda08;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bdc38;
import static legend.game.Scus94491BpeSegment_800b._800bed30;
import static legend.game.Scus94491BpeSegment_800b._800bed34;
import static legend.game.Scus94491BpeSegment_800b._800bed38;
import static legend.game.Scus94491BpeSegment_800b._800bed3c;
import static legend.game.Scus94491BpeSegment_800b._800bed40;
import static legend.game.Scus94491BpeSegment_800b._800bed44;
import static legend.game.Scus94491BpeSegment_800b._800bed48;
import static legend.game.Scus94491BpeSegment_800b._800bed4c;
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
import static legend.game.Scus94491BpeSegment_800b.biggerStructPtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.loadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;

public final class SMap {
  private SMap() { }

  public static final MathStruct _800c66d8 = MEMORY.ref(4, 0x800c66d8L, MathStruct::new);

  public static final MathStruct _800c66e8 = MEMORY.ref(4, 0x800c66e8L, MathStruct::new);

  public static final MathStruct _800c66f8 = MEMORY.ref(4, 0x800c66f8L, MathStruct::new);

  public static final Value _800c6708 = MEMORY.ref(2, 0x800c6708L);
  public static final Value _800c670a = MEMORY.ref(2, 0x800c670aL);
  public static final Value _800c670c = MEMORY.ref(2, 0x800c670cL);
  public static final Value _800c670e = MEMORY.ref(2, 0x800c670eL);
  public static final Value mrg10Addr_800c6710 = MEMORY.ref(4, 0x800c6710L);
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
  public static final Value mrg0Addr_800c6878 = MEMORY.ref(4, 0x800c6878L);
  public static final Value _800c687c = MEMORY.ref(2, 0x800c687cL);
  public static final Value _800c687e = MEMORY.ref(2, 0x800c687eL);

  public static final Value index_800c6880 = MEMORY.ref(4, 0x800c6880L);

  public static final Value mrg1Loaded_800c68d0 = MEMORY.ref(4, 0x800c68d0L);

  public static final Value mrg1Addr_800c68d8 = MEMORY.ref(4, 0x800c68d8L);

  public static final Value mrg10Loaded_800c68e0 = MEMORY.ref(2, 0x800c68e0L);

  public static final Value loadingStage_800c68e4 = MEMORY.ref(4, 0x800c68e4L);

  public static final Value callbackIndex_800c6968 = MEMORY.ref(2, 0x800c6968L);
  public static final Value _800c6970 = MEMORY.ref(4, 0x800c6970L);

  public static final Value _800c69ec = MEMORY.ref(4, 0x800c69ecL);
  public static final Value _800c69f0 = MEMORY.ref(4, 0x800c69f0L);
  public static final Value _800c69f4 = MEMORY.ref(4, 0x800c69f4L);
  public static final Value _800c69f8 = MEMORY.ref(4, 0x800c69f8L);
  public static final Value _800c69fc = MEMORY.ref(4, 0x800c69fcL);
  public static final Value _800c6a00 = MEMORY.ref(4, 0x800c6a00L);

  /** Written as int, read as bytes? */
  public static final Value _800c6a50 = MEMORY.ref(4, 0x800c6a50L);

  public static final Value _800c6aa0 = MEMORY.ref(4, 0x800c6aa0L);
  public static final Value _800c6aa4 = MEMORY.ref(4, 0x800c6aa4L);
  public static final Value _800c6aa8 = MEMORY.ref(4, 0x800c6aa8L);
  public static final Value _800c6aac = MEMORY.ref(2, 0x800c6aacL);

  public static final Value _800c6abc = MEMORY.ref(4, 0x800c6abcL);
  public static final MATRIX _800c6ac0 = MEMORY.ref(4, 0x800c6ac0L, MATRIX::new);
  public static final Value _800c6ae0 = MEMORY.ref(4, 0x800c6ae0L);
  public static final Value _800c6ae4 = MEMORY.ref(4, 0x800c6ae4L);
  public static final Value _800c6ae8 = MEMORY.ref(4, 0x800c6ae8L);
  public static final Value _800c6aec = MEMORY.ref(4, 0x800c6aecL);
  public static final Value _800c6af0 = MEMORY.ref(4, 0x800c6af0L);

  public static final Value _800caaf0 = MEMORY.ref(4, 0x800caaf0L);
  public static final Value _800caaf4 = MEMORY.ref(4, 0x800caaf4L);
  public static final Value _800caaf8 = MEMORY.ref(4, 0x800caaf8L);
  public static final Value _800caafc = MEMORY.ref(4, 0x800caafcL);
  public static final Value _800cab00 = MEMORY.ref(4, 0x800cab00L);
  public static final Value _800cab04 = MEMORY.ref(4, 0x800cab04L);

  public static final Value _800cab10 = MEMORY.ref(4, 0x800cab10L);

  public static final Value _800cab1c = MEMORY.ref(4, 0x800cab1cL);
  public static final Value _800cab20 = MEMORY.ref(4, 0x800cab20L);
  public static final Value _800cab24 = MEMORY.ref(4, 0x800cab24L);
  public static final Value _800cab28 = MEMORY.ref(4, 0x800cab28L);
  public static final Value _800cab2c = MEMORY.ref(4, 0x800cab2cL);
  /** TODO unknown size - start of struct? */
  public static final Value _800cab30 = MEMORY.ref(4, 0x800cab30L);

  /** TODO unknown size - start of struct? */
  public static final Value _800cb250 = MEMORY.ref(4, 0x800cb250L);

  public static final Value _800cb430 = MEMORY.ref(4, 0x800cb430L);

  public static final Value _800cb440 = MEMORY.ref(4, 0x800cb440L);

  public static final Value _800cb448 = MEMORY.ref(4, 0x800cb448L);

  public static final Value _800cb458 = MEMORY.ref(4, 0x800cb458L);

  /** TODO don't think this is rects */
  public static final ArrayRef<RECT> rectArr_800cb460 = MEMORY.ref(4, 0x800cb460L, ArrayRef.of(RECT.class, 0x20, 0x8, RECT::new));

  public static final Value _800cb560 = MEMORY.ref(4, 0x800cb560L);
  public static final Value _800cb564 = MEMORY.ref(4, 0x800cb564L);
  public static final Value screenOffsetX_800cb568 = MEMORY.ref(4, 0x800cb568L);
  public static final Value screenOffsetY_800cb56c = MEMORY.ref(4, 0x800cb56cL);
  public static final Value _800cb570 = MEMORY.ref(4, 0x800cb570L);
  public static final Value _800cb574 = MEMORY.ref(4, 0x800cb574L);
  public static final Value _800cb578 = MEMORY.ref(4, 0x800cb578L);
  public static final Value _800cb57c = MEMORY.ref(4, 0x800cb57cL);
  public static final Value _800cb580 = MEMORY.ref(4, 0x800cb580L);
  public static final Value _800cb584 = MEMORY.ref(4, 0x800cb584L);

  public static final Value _800cb590 = MEMORY.ref(4, 0x800cb590L);

  /** unknown size */
  public static final Value _800cb710 = MEMORY.ref(1, 0x800cb710L);

  /** unknown size */
  public static final Value _800cb718 = MEMORY.ref(1, 0x800cb718L);

  public static final Value _800cbb90 = MEMORY.ref(2, 0x800cbb90L);

  /** unknown size */
  public static final Value _800cbc90 = MEMORY.ref(1, 0x800cbc90L);

  public static final GsRVIEW2 _800cbd10 = MEMORY.ref(4, 0x800cbd10L, GsRVIEW2::new);
  public static final Value _800cbd30 = MEMORY.ref(4, 0x800cbd30L);
  public static final Value _800cbd34 = MEMORY.ref(4, 0x800cbd34L);
  public static final Value _800cbd38 = MEMORY.ref(4, 0x800cbd38L);
  public static final Value _800cbd3c = MEMORY.ref(4, 0x800cbd3cL);
  public static final MATRIX _800cbd40 = MEMORY.ref(4, 0x800cbd40L, MATRIX::new);
  public static final Value _800cbd60 = MEMORY.ref(4, 0x800cbd60L);
  public static final Value _800cbd64 = MEMORY.ref(4, 0x800cbd64L);
  public static final MATRIX _800cbd68 = MEMORY.ref(4, 0x800cbd68L, MATRIX::new);

  public static final GsCOORDINATE2 GsCOORDINATE2_800cbda8 = MEMORY.ref(4, 0x800cbda8L, GsCOORDINATE2::new);

  /** unknown size */
  public static final Value _800cbdf8 = MEMORY.ref(4, 0x800cbdf8L);

  /** unknown size */
  public static final Value _800cbe08 = MEMORY.ref(4, 0x800cbe08L);

  public static final Value _800cbe28 = MEMORY.ref(4, 0x800cbe28L);
  public static final Pointer<GsCOORDINATE2> GsCOORDINATE2Ptr_800cbe2c = MEMORY.ref(4, 0x800cbe2cL, Pointer.of(4, GsCOORDINATE2::new));

  public static final Value _800cbe34 = MEMORY.ref(4, 0x800cbe34L);
  public static final Value _800cbe38 = MEMORY.ref(4, 0x800cbe38L);

  /** unknown size */
  public static final Value _800cbe78 = MEMORY.ref(4, 0x800cbe78L);

  /** unknown size */
  public static final Value _800cca78 = MEMORY.ref(4, 0x800cca78L);

  public static final TmdWithId _800cfa78 = MEMORY.ref(4, 0x800cfa78L, TmdWithId::new);

  public static final Value _800d1a88 = MEMORY.ref(4, 0x800d1a88L);
  public static final Value _800d1a8c = MEMORY.ref(4, 0x800d1a8cL);
  //TODO 0x4c byte thing?
  public static final Value _800d1a90 = MEMORY.ref(4, 0x800d1a90L);
  public static final Value _800d1ad0 = MEMORY.ref(4, 0x800d1ad0L);
  public static final Value _800d1ad4 = MEMORY.ref(4, 0x800d1ad4L);
  public static final Value _800d1ad8 = MEMORY.ref(4, 0x800d1ad8L);

  public static final Value _800d1cb8 = MEMORY.ref(4, 0x800d1cb8L);

  public static final Value _800d1cc0 = MEMORY.ref(4, 0x800d1cc0L);

  public static final ArrayRef<UnsignedIntRef> textureDataPtrArray3_800d4ba0 = MEMORY.ref(4, 0x800d4ba0L, ArrayRef.of(UnsignedIntRef.class, 3, 4, UnsignedIntRef::new));

  /** TODO unknown size */
  public static final Value _800d4bb0 = MEMORY.ref(4, 0x800d4bb0L);

  public static final Value _800d4bd0 = MEMORY.ref(4, 0x800d4bd0L);
  public static final Value _800d4bd4 = MEMORY.ref(4, 0x800d4bd4L);

  public static final Value _800d4bdc = MEMORY.ref(4, 0x800d4bdcL);
  public static final Value _800d4be0 = MEMORY.ref(4, 0x800d4be0L);
  public static final Value _800d4be4 = MEMORY.ref(4, 0x800d4be4L);
  public static final Value _800d4be8 = MEMORY.ref(4, 0x800d4be8L);
  public static final Value _800d4bec = MEMORY.ref(4, 0x800d4becL);
  public static final Value _800d4bf0 = MEMORY.ref(4, 0x800d4bf0L);

  public static final BigStruct _800d4bf8 = MEMORY.ref(4, 0x800d4bf8L, BigStruct::new);

  public static final BigStruct _800d4d40 = MEMORY.ref(4, 0x800d4d40L, BigStruct::new);

  /** TODO part of previous struct? */
  public static final Value _800d4e68 = MEMORY.ref(4, 0x800d4e68L);

  /** TODO part of previous struct? */
  public static final Value _800d4eb8 = MEMORY.ref(4, 0x800d4eb8L);

  /** TODO part of previous struct? */
  public static final Value _800d4edc = MEMORY.ref(4, 0x800d4edcL);

  public static final Value _800d4f90 = MEMORY.ref(4, 0x800d4f90L);

  public static final Value _800d4fc0 = MEMORY.ref(4, 0x800d4fc0L);

  public static final Value _800d4fd0 = MEMORY.ref(4, 0x800d4fd0L);

  public static final Value _800d4fe0 = MEMORY.ref(4, 0x800d4fe0L);

  public static final Value _800d4fe8 = MEMORY.ref(2, 0x800d4fe8L);

  public static final Value _800d4ff0 = MEMORY.ref(4, 0x800d4ff0L);

  public static final Value _800d5588 = MEMORY.ref(4, 0x800d5588L);

  public static final Value _800d5590 = MEMORY.ref(4, 0x800d5590L);

  public static final Value _800d5598 = MEMORY.ref(2, 0x800d5598L);

  public static final Value _800d5620 = MEMORY.ref(2, 0x800d5620L);

  public static final Value _800d5630 = MEMORY.ref(2, 0x800d5630L);

  public static final BigStruct _800d5eb0 = MEMORY.ref(4, 0x800d5eb0L, BigStruct::new);

  public static final Value _800d6050 = MEMORY.ref(2, 0x800d6050L);
  public static final Value _800d6052 = MEMORY.ref(2, 0x800d6052L);

  public static final Value _800d6068 = MEMORY.ref(2, 0x800d6068L);
  public static final Value _800d606a = MEMORY.ref(2, 0x800d606aL);

  public static final Value _800d610c = MEMORY.ref(2, 0x800d610cL);

  public static final Value _800d689c = MEMORY.ref(4, 0x800d689cL);

  public static final RECT _800d6b48 = MEMORY.ref(4, 0x800d6b48L, RECT::new);

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
  /** TODO struct? */
  public static final Value _800d6d1c = MEMORY.ref(1, 0x800d6d1cL);

  public static final Value _800d6d24 = MEMORY.ref(4, 0x800d6d24L);

  public static final Value _800d6d2c = MEMORY.ref(4, 0x800d6d2cL);

  /** TODO an array of 0x14-long somethings */
  public static final Value _800f5930 = MEMORY.ref(4, 0x800f5930L);

  public static final ArrayRef<Pointer<RunnableRef>> callbackArr_800f5ad4 = (ArrayRef<Pointer<RunnableRef>>)MEMORY.ref(4, 0x800f5ad4L, ArrayRef.of(Pointer.class, 0x80, 4, (Function)Pointer.of(4, RunnableRef::new)));
  public static final Value _800f5cd4 = MEMORY.ref(2, 0x800f5cd4L);

  public static final Value _800f64ac = MEMORY.ref(4, 0x800f64acL);

  public static final Pointer<GsOT> _800f64c0 = MEMORY.ref(4, 0x800f64c0L, Pointer.of(4, GsOT::new));
  public static final Value _800f64c4 = MEMORY.ref(1, 0x800f64c4L);

  public static final Value _800f64c6 = MEMORY.ref(1, 0x800f64c6L);

  public static final Value _800f74c4 = MEMORY.ref(1, 0x800f74c4L);

  public static final Value _800f7e24 = MEMORY.ref(4, 0x800f7e24L);
  public static final Value _800f7e28 = MEMORY.ref(4, 0x800f7e28L);
  public static final Value _800f7e2c = MEMORY.ref(4, 0x800f7e2cL);
  public static final Value _800f7e30 = MEMORY.ref(4, 0x800f7e30L);

  public static final Value _800f7e4c = MEMORY.ref(4, 0x800f7e4cL);
  public static final Value _800f7e50 = MEMORY.ref(4, 0x800f7e50L);
  public static final Value _800f7e54 = MEMORY.ref(4, 0x800f7e54L);
  public static final Value _800f7e58 = MEMORY.ref(4, 0x800f7e58L);

  public static final Value _800f7f0c = MEMORY.ref(4, 0x800f7f0cL);
  public static final Value _800f7f10 = MEMORY.ref(4, 0x800f7f10L);
  public static final Value _800f7f14 = MEMORY.ref(4, 0x800f7f14L);

  public static final Value _800f7f74 = MEMORY.ref(4, 0x800f7f74L);

  public static final Value _800f9374 = MEMORY.ref(4, 0x800f9374L);

  public static final ArrayRef<RECT> rectArray3_800f96f4 = MEMORY.ref(8, 0x800f96f4L, ArrayRef.of(RECT.class, 3, 8, RECT::new));

  public static final Value _800f970c = MEMORY.ref(4, 0x800f970cL);

  public static final Value _800f9718 = MEMORY.ref(2, 0x800f9718L);

  public static final Value _800f982c = MEMORY.ref(2, 0x800f982cL);

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
    long v0 = dobj2.tmd_08.getPointer(); //TODO use this struct
    long s1 = MEMORY.ref(4, v0).offset(0x14L).get();
    long s0 = MEMORY.ref(4, v0).offset(0x10L).get();
    long s2 = a1 & 0x7fL;

    //LAB_800d9e90
    while(s1 != 0) {
      long v1 = MEMORY.ref(4, s0).get(0xff04_0000L);

      if(v1 == 0x3500_0000L || v1 == 0x3700_0000L) {
        //LAB_800da02c
        FUN_800da7f4(s0, MEMORY.ref(2, s0).get(), s2);
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 36;
      } else if(v1 == 0x3004_0000L || v1 == 0x3204_0000L) {
        //LAB_800d9fc8
        FUN_80021058(s0, MEMORY.ref(2, s0).get());
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 28;
        //LAB_800d9ef0
      } else if(v1 == 0x3000_0000L || v1 == 0x3200_0000L) {
        //LAB_800d9fe8
        FUN_80021048(s0, MEMORY.ref(2, s0).get());
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 20;
        //LAB_800d9f00
        //LAB_800d9f28
      } else if(v1 == 0x3400_0000L || v1 == 0x3600_0000L) {
        //LAB_800da00c
        FUN_800da6c8(s0, MEMORY.ref(2, s0).get(), s2);
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 28;
        //LAB_800d9f38
      } else if(v1 == 0x3804_0000L || v1 == 0x3a04_0000L) {
        //LAB_800da050
        FUN_80021060(s0, MEMORY.ref(2, s0).get());
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 36;
        //LAB_800d9f78
      } else if(v1 == 0x3800_0000L || v1 == 0x3a00_0000L) {
        //LAB_800da074
        FUN_80021050(s0, MEMORY.ref(2, s0).get());
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 24;
        //LAB_800d9fac
      } else if(v1 == 0x3c00_0000L || v1 == 0x3e00_0000L) {
        //LAB_800da09c
        FUN_800da754(s0, MEMORY.ref(2, s0).get(), s2);
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;
        s0 += v1 * 36;
        //LAB_800d9f88
      } else if(v1 == 0x3d00_0000L || v1 == 0x3f00_0000L) {
        //LAB_800da0c4
        FUN_800da880(s0, MEMORY.ref(2, s0).get(), s2);
        v1 = MEMORY.ref(2, s0).get();
        s1 -= v1;

        //LAB_800da0e8
        //LAB_800da0ec
        //LAB_800da0f0
        s0 += v1 * 44;
      }

      //LAB_800da0f4
    }

    //LAB_800da0fc
  }

  @Method(0x800da114L)
  public static void FUN_800da114(final BigStruct a0) {
    if(a0.addr_ui_a4.get() != 0) {
      //LAB_800da138
      for(int i = 0; i < 4; i++) {
        if(MEMORY.ref(1, a0.addr_ui_a4.get()).offset(0x4L).offset(i).get() != 0) {
          FUN_800dde70(a0, i);
        }

        //LAB_800da15c
      }
    }

    //LAB_800da16c
    //LAB_800da174
    for(int i = 0; i < 7; i++) {
      if(a0.aub_ec.get(i).get() != 0) {
        FUN_80022018(a0, i);
      }

      //LAB_800da18c
    }

    final long v1 = a0.ub_9c.get();
    if(v1 != 0x2L) {
      if(v1 == 0) {
        if(a0.ub_a2.get() == 0) {
          a0.us_9e.set(a0.us_9a);
        } else {
          //LAB_800da1d0
          a0.us_9e.set((short)a0.us_9a.get() / 2);
        }

        //LAB_800da1e4
        a0.ub_9c.incr();
        a0.ptr_ui_94.set(a0.ptr_ui_90);
      }

      //LAB_800da1f8
      if((a0.us_9e.get() & 0x1L) != 0 || a0.ub_a2.get() != 0) {
        //LAB_800da24c
        FUN_800212d8(a0);
      } else {
        final long s0 = a0.ptr_ui_94.get();

        if(a0.ub_a3.get() == 0) {
          FUN_800da920(a0);
        } else {
          //LAB_800da23c
          FUN_800212d8(a0);
        }

        a0.ptr_ui_94.set(s0);
      }

      //LAB_800da254
      a0.us_9e.decr();

      if(a0.us_9e.get() == 0) {
        a0.ub_9c.set(0);
      }
    }

    //LAB_800da274
  }

  @Method(0x800da288L)
  public static void FUN_800da288(final GsDOBJ2 dobj2) {
    final TmdObjTable objTable = dobj2.tmd_08.deref();
    final long vertices = objTable.vert_top_00.get();
    final long normals = objTable.normal_top_08.get();
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_800da2bc
    while(count != 0) {
      final long length = MEMORY.ref(2, primitives).get();
      final long command = MEMORY.ref(4, primitives).get(0xff04_0000L);
      count -= length;

      if(command == 0x3000_0000L) {
        //LAB_800da3e8
        primitives = FUN_800daeb8(primitives, vertices, normals);
      } else if(command == 0x3004_0000L) {
        //LAB_800da408
        primitives = FUN_800dbb14(primitives, vertices, normals);
      } else if(command == 0x3200_0000L) {
        //LAB_800da3f8
        primitives = FUN_800dce24(primitives, vertices, normals);
      } else if(command == 0x3204_0000L) {
        //LAB_800da41c
        primitives = FUN_800dd4e0(primitives, vertices, normals);
      } else if(command == 0x3400_0000L) {
        //LAB_800da430
        primitives = FUN_800db4c0(primitives, vertices, normals);
      } else if(command == 0x3500_0000L) {
        //LAB_800da450
        primitives = FUN_800dc164(primitives, vertices, length);
      } else if(command == 0x3600_0000L) {
        //LAB_800da440
        primitives = FUN_800dc7c0(primitives, vertices, normals);
      } else if(command == 0x3700_0000L) {
        //LAB_800da464
        primitives = FUN_800ddb8c(primitives, vertices, length);
      } else if(command == 0x3800_0000L) {
        //LAB_800da478
        primitives = FUN_800dab7c(primitives, vertices, normals);
      } else if(command == 3804_0000L) {
        //LAB_800da498
        primitives = FUN_800db790(primitives, vertices, normals);
      } else if(command == 0x3a00_0000L) {
        //LAB_800da488
        primitives = FUN_800dca90(primitives, vertices, normals);
      } else if(command == 0x3a04_0000L) {
        //LAB_800da4ac
        primitives = FUN_800dd0fc(primitives, vertices, normals);
      } else if(command == 0x3c00_0000L) {
        //LAB_800da4c0
        primitives = FUN_800db144(primitives, vertices, normals);
      } else if(command == 0x3d00_0000L) {
        //LAB_800da4e4
        primitives = FUN_800dbde8(primitives, vertices, length);
      } else if(command == 0x3e00_0000L) {
        //LAB_800da4d0
        primitives = FUN_800dc43c(primitives, vertices, normals);
      } else if(command == 0x3f00_0000L) {
        //LAB_800da4f8
        primitives = FUN_800dd808(primitives, vertices, length);
      }

      //LAB_800da504
    }

    //LAB_800da50c
  }

  @Method(0x800da6c8L)
  public static void FUN_800da6c8(final long a0, long a1, long a2) {
    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();
    a2 = a0 + 0xcL;

    //LAB_800da6e8
    while(a1 != 0) {
      long v0 = MEMORY.ref(4, a2).offset(-0x8L).get();
      v0 &= MEMORY.ref(4, a3).offset(0x4L).get();
      v0 |= MEMORY.ref(4, a3).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2).offset(-0x8L).setu(v0);

      v0 = MEMORY.ref(4, a2).offset(-0x4L).get();
      v0 &= MEMORY.ref(4, a3).offset(0xcL).get();
      v0 |= MEMORY.ref(4, a3).offset(0x8L).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2).offset(-0x4L).setu(v0);

      v0 = MEMORY.ref(4, a2).get();
      v0 += MEMORY.ref(4, a3).offset(0x10L).get();
      MEMORY.ref(4, a2).setu(v0);
      a2 += 0x1cL;
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
  public static void FUN_800da880(final long a0, long a1, long a2) {
    if(a1 == 0) {
      return;
    }

    final long a3 = _800f5930.offset(a2 * 0x14L).getAddress();
    a2 = a0 + 0x10L;

    //LAB_800da8a0
    do {
      long v0 = MEMORY.ref(4, a2).offset(-0xcL).get();
      v0 &= MEMORY.ref(4, a3).offset(0x4L).get();
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
      a2 += 0x2cL;
      a1--;
    } while(a1 != 0);

    //LAB_800da918
  }

  @Method(0x800da920L)
  public static void FUN_800da920(final BigStruct a0) {
    long s4 = a0.ptr_ui_94.get();

    //LAB_800da96c
    for(int i = 0; i < a0.us_ca.get(); i++) {
      final MATRIX coord = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref().coord;
      final GsCOORD2PARAM params = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref().param.deref();

      FUN_80040780(params.rotate, coord);

      params.trans.x.add((int)MEMORY.ref(2, s4).offset(0x6L).getSigned());
      params.trans.y.add((int)MEMORY.ref(2, s4).offset(0x8L).getSigned());
      params.trans.z.add((int)MEMORY.ref(2, s4).offset(0xaL).getSigned());
      params.trans.div(2);

      setTransferVector(coord, params.trans);

      s4 += 0xcL;
    }

    //LAB_800daa0c
    a0.ptr_ui_94.set(s4);
  }

  @Method(0x800daa3cL)
  public static void FUN_800daa3c(final BigStruct a0) {
    assert false;
  }

  @Method(0x800dab7cL)
  public static long FUN_800dab7c(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800daeb8L)
  public static long FUN_800daeb8(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800db144L)
  public static long FUN_800db144(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800db4c0L)
  public static long FUN_800db4c0(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800db790L)
  public static long FUN_800db790(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dbb14L)
  public static long FUN_800dbb14(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dbde8L)
  public static long FUN_800dbde8(final long primitives, final long vertices, final long length) {
    assert false;
    return 0;
  }

  @Method(0x800dc164L)
  public static long FUN_800dc164(final long primitives, final long vertices, final long length) {
    assert false;
    return 0;
  }

  @Method(0x800dc43cL)
  public static long FUN_800dc43c(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dc7c0L)
  public static long FUN_800dc7c0(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dca90L)
  public static long FUN_800dca90(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dce24L)
  public static long FUN_800dce24(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dd0fcL)
  public static long FUN_800dd0fc(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dd4e0L)
  public static long FUN_800dd4e0(final long primitives, final long vertices, final long normals) {
    assert false;
    return 0;
  }

  @Method(0x800dd808L)
  public static long FUN_800dd808(final long primitives, final long vertices, final long length) {
    long a2 = length;
    long t4 = primitives;
    long t2 = linkedListAddress_1f8003d8.get();
    final UnboundedArrayRef<GsOT_TAG> tags = tags_1f8003d0.deref();
    long t0 = primitives + 0x22L;
    long a3 = t2 + 0x2aL;

    //LAB_800dd84c
    while(a2 != 0) {
      long t5 = vertices + MEMORY.ref(2, t4).offset(0x24L).get() * 0x8L;
      long t6 = vertices + MEMORY.ref(2, t4).offset(0x26L).get() * 0x8L;
      long t7 = vertices + MEMORY.ref(2, t4).offset(0x28L).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
      CPU.COP2(0x28_0030L);
      MEMORY.ref(4, a3 - 0x1eL).setu(MEMORY.ref(4, t0 - 0x1eL));
      MEMORY.ref(4, a3 - 0x12L).setu(MEMORY.ref(4, t0 - 0x1aL));

      if(CPU.CFC2(31) >= 0) {
        CPU.COP2(0x140_0006L);

        MEMORY.ref(4, a3 + 0x6L).setu(MEMORY.ref(4, t0 - 0x16L));

        if(CPU.MFC2(24) != 0) {
          MEMORY.ref(4, t2).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
          long v0 = vertices + MEMORY.ref(2, t0).offset(0x8L).get() * 0x8L;
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
          CPU.COP2(0x18_0001L);

          MEMORY.ref(1, a3).offset(-0x27L).setu(0xcL);
          MEMORY.ref(4, a3 - 0x26L).setu(0x3e80_8080L);
          MEMORY.ref(4, a3 + 0x6L).setu(MEMORY.ref(4, t0 - 0x12L));

          if(CPU.CFC2(31) >= 0) {
            MEMORY.ref(4, t2).offset(0x2cL).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, a3).offset(-0x22L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0x16L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(0x2L).getSigned() >= -0xc0L) {
              //LAB_800dd98c
              if(MEMORY.ref(2, a3).offset(-0x20L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x14L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(0x4L).getSigned() == 0) {
                //LAB_800dd9dc
                if(MEMORY.ref(2, a3).offset(-0x22L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0x16L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(0x2L).getSigned() < 0xc1L) {
                  //LAB_800dda2c
                  if(MEMORY.ref(2, a3).offset(-0x20L).getSigned() <= 0x80L || MEMORY.ref(2, a3).offset(-0x14L).getSigned() <= 0x80L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() <= 0x80L || MEMORY.ref(2, a3).offset(0x4L).getSigned() <= 0x80L) {
                    //LAB_800dda7c
                    CPU.COP2(0x168_002eL);
                    MEMORY.ref(1, a3).offset(-0x26L).setu(MEMORY.ref(1, t0).offset(-0xeL));
                    MEMORY.ref(1, a3).offset(-0x25L).setu(MEMORY.ref(1, t0).offset(-0xdL));
                    MEMORY.ref(1, a3).offset(-0x24L).setu(MEMORY.ref(1, t0).offset(-0xcL));
                    MEMORY.ref(1, a3).offset(-0x1aL).setu(MEMORY.ref(1, t0).offset(-0xaL));
                    MEMORY.ref(1, a3).offset(-0x19L).setu(MEMORY.ref(1, t0).offset(-0x9L));
                    MEMORY.ref(1, a3).offset(-0x18L).setu(MEMORY.ref(1, t0).offset(-0x8L));
                    MEMORY.ref(1, a3).offset(-0x0eL).setu(MEMORY.ref(1, t0).offset(-0x6L));
                    MEMORY.ref(1, a3).offset(-0x0dL).setu(MEMORY.ref(1, t0).offset(-0x5L));
                    MEMORY.ref(1, a3).offset(-0x0cL).setu(MEMORY.ref(1, t0).offset(-0x4L));
                    MEMORY.ref(1, a3).offset(-0x02L).setu(MEMORY.ref(1, t0).offset(-0x2L));
                    MEMORY.ref(1, a3).offset(-0x01L).setu(MEMORY.ref(1, t0).offset(-0x1L));
                    MEMORY.ref(1, a3).offset(-0x00L).setu(MEMORY.ref(1, t0).offset(-0x0L));
                    long t1 = CPU.MFC2(7) + _1f8003e8.get();
                    long v1 = _1f8003cc.get();
                    t1 >>= _1f8003c4.get();
                    if(t1 >= v1) {
                      t1 = v1;
                    }

                    //LAB_800ddb3c
                    MEMORY.ref(4, t2).setu(0xc00_0000L | tags.get((int)t1).p.get());
                    tags.get((int)t1).p.set(t2 & 0xff_ffffL);
                    a3 += 0x34L;
                    t2 += 0x34L;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800ddb64
      t0 += 0x2cL;
      t4 += 0x2cL;
      a2--;
    }

    //LAB_800ddb70
    linkedListAddress_1f8003d8.setu(t2);
    return t4;
  }

  @Method(0x800ddb8cL)
  public static long FUN_800ddb8c(final long primitives, final long vertices, final long length) {
    assert false;
    return 0;
  }

  @Method(0x800dde70L)
  public static void FUN_800dde70(BigStruct a0, int index) {
    long a2;
    long t1;
    long t2;
    long t3;
    long v1;
    long s0;

    a2 = a0.addr_ui_a4.get();
    long a1 = MEMORY.ref(4, a2).offset(0x20L).offset(index * 0x4L).get() + 0x4L;
    if(MEMORY.ref(4, a2).offset(0x20L).offset(index * 0x4L).get() == 0) {
      MEMORY.ref(1, a2).offset(0x4L).offset(index).setu(0);
    } else {
      //LAB_800ddeac
      v1 = (a0.ub_9d.get() & 0x7fL) * 0x2L;
      t2 = _80050424.offset(v1).get() + 0x70L;
      t1 = _800503f8.offset(v1).get();

      //LAB_800ddef8
      for(int i = 0; i < MEMORY.ref(2, a2).offset(0x8L).offset(index * 0x2L).getSigned(); i++) {
        a1 += 0x4L;
      }

      //LAB_800ddf08
      t3 = MEMORY.ref(2, a1).get();
      a1 += 0x2L;

      final long addr = a0.addr_ui_a4.get();

      MEMORY.ref(2, addr).offset(index * 0x2L).offset(0x10L).addu(0x1L);

      if(MEMORY.ref(4, addr).offset(0x10L).offset(index * 0x2L).get() == (MEMORY.ref(2, a1).get() & 0xffffL)) {
        MEMORY.ref(4, addr).offset(0x10L).offset(index * 0x2L).setu(0);
        if((MEMORY.ref(2, a1).offset(0x2L).get() & 0xffffL) == 0xffffL) {
          MEMORY.ref(2, addr).offset(index * 0x2L).offset(0x8L).setu(0);
        } else {
          //LAB_800ddf70
          MEMORY.ref(2, addr).offset(index * 0x2L).offset(0x8L).addu(0x1L);
        }
      }

      //LAB_800ddf8c
      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x18L);
      FUN_8003b780(s0, new RECT((short)t1, (short)(t2 + t3), (short)16, (short)1), t1 & 0xffffL, (MEMORY.ref(2, addr).offset((long)index * 0x2L).offset(0x18L).get() + t2) & 0xffffL);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), s0);
    }

    //LAB_800ddff4
  }

  @Method(0x800de004L)
  public static void FUN_800de004(final BigStruct a0, final long a1) {
    if(MEMORY.ref(4, a1).offset(0x4L).get() != 0) {
      long v0 = addToLinkedListTail(0x30L);
      a0.addr_ui_a4.set(v0);

      MEMORY.ref(4, v0).setu(a1 + MEMORY.ref(4, a1).offset(0x4L).get() / 4 * 4);

      //LAB_800de05c
      for(int a2 = 0; a2 < 4; a2++) {
        long a0_0 = a0.addr_ui_a4.get();
        long v1 = MEMORY.ref(4, a0_0).get();
        a0_0 += a2 * 0x4L;
        v1 += MEMORY.ref(4, v1).offset(a2 * 0x4L).get() / 4 * 4;
        MEMORY.ref(4, a0_0).offset(0x20L).setu(v1);
        v1 = a0.addr_ui_a4.get();
        v0 = MEMORY.ref(4, v1).offset(a2 * 0x4L).offset(0x20L).get();
        a0_0 = a2 << 1;
        if(v0 == 0) {
          MEMORY.ref(1, v1).offset(a2).offset(0x4L).setu(0);
        } else {
          MEMORY.ref(2, v1).offset(a0_0).offset(0x8L).setu(0);
          v1 = a0.addr_ui_a4.get();
          v0 = MEMORY.ref(4, v1).offset(a2 * 0x4L).offset(0x20L).deref(2).offset(0x2L).get();
          v1 += a0_0;
          MEMORY.ref(2, v1).offset(0x18L).setu(v0);
          v0 = a0.addr_ui_a4.get() + a0_0;
          MEMORY.ref(2, v0).offset(0x10L).setu(0);
          v1 = a0.addr_ui_a4.get();
          a0_0 += v1;
          if(MEMORY.ref(2, a0_0).offset(0x18L).get() == 0xffffL) {
            //LAB_800de0f8
            MEMORY.ref(1, v1).offset(a2).offset(0x4L).setu(0);
          } else {
            //LAB_800de104
            MEMORY.ref(1, v1).offset(a2).offset(0x4L).setu(0x1L);
          }
        }

        //LAB_800de108
      }
    } else {
      //LAB_800de120
      a0.addr_ui_a4.set(0);
    }

    //LAB_800de124
  }

  @Method(0x800de138L)
  public static void FUN_800de138(final BigStruct a0, final long index) {
    if(MEMORY.ref(4, a0.addr_ui_a4.get()).offset(0x20L).offset(index * 0x4L).get() == 0) {
      MEMORY.ref(1, a0.addr_ui_a4.get()).offset(0x4L).offset(index).setu(0);
      return;
    }

    //LAB_800de164
    MEMORY.ref(2, a0.addr_ui_a4.get()).offset(0x8L).offset(index * 0x2L).setu(0);
    MEMORY.ref(2, a0.addr_ui_a4.get()).offset(0x18L).offset(index * 0x2L).setu(MEMORY.ref(4, a0.addr_ui_a4.get()).offset(0x20L).offset(index * 0x4L).deref(2).offset(0x2L));
    MEMORY.ref(2, a0.addr_ui_a4.get()).offset(0x10L).offset(index * 0x2L).setu(0);

    if(MEMORY.ref(2, a0.addr_ui_a4.get()).offset(0x18L).offset(index * 0x2L).get() == 0xffffL) {
      MEMORY.ref(1, a0.addr_ui_a4.get()).offset(0x4L).offset(index).setu(0);
    } else {
      //LAB_800de1c4
      MEMORY.ref(1, a0.addr_ui_a4.get()).offset(0x4L).offset(index).setu(0x1L);
    }
  }

  @Method(0x800df168L)
  public static long FUN_800df168(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).set(a0.uiArr_20.get(0).deref());
    a0.uiArr_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800dfe0c(a0);
  }

  @Method(0x800df198L)
  public static long FUN_800df198(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).set(a0.uiArr_20.get(0).deref());
    a0.uiArr_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800dfec8(a0);
  }

  @Method(0x800df258L)
  public static long FUN_800df258(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.coord2_14.coord.transfer.setX((int)a0.uiArr_20.get(1).deref().get());
    struct.coord2_14.coord.transfer.setY((int)a0.uiArr_20.get(2).deref().get());
    struct.coord2_14.coord.transfer.setZ((int)a0.uiArr_20.get(3).deref().get());
    struct.us_170.set(0);
    return 0;
  }

  @Method(0x800df2b8L)
  public static long FUN_800df2b8(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    a0.uiArr_20.get(1).deref().set(struct.coord2_14.coord.transfer.getX() & 0xffff_ffffL);
    a0.uiArr_20.get(2).deref().set(struct.coord2_14.coord.transfer.getY() & 0xffff_ffffL);
    a0.uiArr_20.get(3).deref().set(struct.coord2_14.coord.transfer.getZ() & 0xffff_ffffL);
    return 0;
  }

  @Method(0x800df314L)
  public static long FUN_800df314(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    struct.coord2Param_64.rotate.x.set((short)a0.uiArr_20.get(1).deref().get());
    struct.coord2Param_64.rotate.y.set((short)a0.uiArr_20.get(2).deref().get());
    struct.coord2Param_64.rotate.z.set((short)a0.uiArr_20.get(3).deref().get());
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800df3d0L)
  public static long FUN_800df3d0(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(3).set(a0.uiArr_20.get(2).deref());
    a0.uiArr_20.get(2).set(a0.uiArr_20.get(1).deref());
    a0.uiArr_20.get(1).set(a0.uiArr_20.get(0).deref());
    a0.uiArr_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e0018(a0);
  }

  @Method(0x800df410L)
  public static long FUN_800df410(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).set(a0.uiArr_20.get(0).deref());
    a0.uiArr_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e0094(a0);
  }

  @Method(0x800df530L)
  public static long FUN_800df530(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).set(a0.uiArr_20.get(0).deref());
    a0.uiArr_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e0184(a0);
  }

  @Method(0x800df650L)
  public static long FUN_800df650(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).set(a0.uiArr_20.get(0).deref());
    a0.uiArr_20.get(0).set(a0.biggerStruct_04.deref().ui_44.get(0));
    return FUN_800e02fc(a0);
  }

  @Method(0x800dfe0cL)
  public static long FUN_800dfe0c(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final long s1 = a0.uiArr_20.get(1).deref().get();

    struct.us_12e.set((int)s1);
    struct.ub_9d.set((int)_800c6a50.offset(s1 * 0x4L).get());

    FUN_80020fe0(struct);
    FUN_800e0d18(struct, _800c6a00.offset(s1 * 0x4L).get(), struct.ui_124.get() + MEMORY.ref(4, struct.ui_124.get()).offset((s1 * 0x21L + 0x1L) * 0x8L).offset(0x8L).get());

    struct.us_12c.set(0);
    struct.ui_188.set(0);

    return 0;
  }

  @Method(0x800dfec8L)
  public static long FUN_800dfec8(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    struct.us_132.set((int)a0.uiArr_20.get(1).deref().get());
    struct.ub_a2.set(0);
    struct.ub_a3.set(0);

    long v0 = struct.us_12e.get() * 0x21L + struct.us_132.get() + 0x1L;
    long a1 = struct.ui_124.get() + MEMORY.ref(4, struct.ui_124.get()).offset(v0 * 0x8L).offset(0x8L).get();

    FUN_80021584(struct, a1);

    struct.us_12c.set(0);
    struct.ui_190.and(0x9fff_ffffL);

    return 0;
  }

  @Method(0x800e0018L)
  public static long FUN_800e0018(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);
    final long v1 = (0xc01L - FUN_80040b90(a0.uiArr_20.get(3).deref().get() - struct.coord2_14.coord.transfer.getZ(), a0.uiArr_20.get(1).deref().get() - struct.coord2_14.coord.transfer.getX())) & 0xfffL;
    struct.coord2Param_64.rotate.y.set((short)v1);
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800e0094L)
  public static long FUN_800e0094(final HmdSomethingStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_128.set((int)a0.uiArr_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e0184L)
  public static long FUN_800e0184(final HmdSomethingStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).us_172.set((int)a0.uiArr_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800e02fcL)
  public static long FUN_800e02fc(final HmdSomethingStruct a0) {
    final BigStruct struct1 = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    struct1.us_178.set((int)a0.uiArr_20.get(1).deref().get());

    if(a0.uiArr_20.get(1).deref().get() != 0) {
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
  public static long FUN_800e03e4(final HmdSomethingStruct a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class);

    final long a0_0 = a0.uiArr_20.get(1).deref().get();

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
  public static long FUN_800e09e0(final HmdSomethingStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ub_cc.set(1);
    return 0;
  }

  @Method(0x800e0a14L)
  public static long FUN_800e0a14(final HmdSomethingStruct a0) {
    biggerStructPtrArr_800bc1c0.get((int)a0.uiArr_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BigStruct.class).ub_cc.set(0);
    return 0;
  }

  @Method(0x800e0d18L)
  public static void FUN_800e0d18(final BigStruct a0, final long a1, final long a2) {
    final int transferX = a0.coord2_14.coord.transfer.getX();
    final int transferY = a0.coord2_14.coord.transfer.getY();
    final int transferZ = a0.coord2_14.coord.transfer.getZ();

    //LAB_800e0d5c
    for(int i = 0; i < 7; i++) {
      a0.aub_ec.get(i).set(0);
    }

    long v0 = (short)MEMORY.ref(2, a1).offset(0x14L).get();
    long v1 = v0 * 0x80L / 0x8L;
    v1 += v0 * 0x280L / 0x8L;
    a0.s_c8.set((short)MEMORY.ref(2, a1).offset(0x14L).get());
    v0 = addToLinkedListTail(v1 + v0 * 0x140L / 0x8L);
    a0.dobj2ArrPtr_00.set(MEMORY.ref(4, v0, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    v0 += a0.s_c8.get() * 0x10L;
    a0.coord2ArrPtr_04.set(MEMORY.ref(4, v0, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    v0 += a0.s_c8.get() * 0x50L;
    a0.coord2ParamArrPtr_08.set(MEMORY.ref(4, v0, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new)));
    a0.ui_8c.set(a1 + 0xcL);
    a0.us_ca.set((int)MEMORY.ref(2, a1).offset(0x14L).get());

    if(MEMORY.ref(4, a1).offset(0x4L).get() != 0) {
      v0 = addToLinkedListTail(0x30L);
      a0.addr_ui_a4.set(v0);
      MEMORY.ref(4, v0).setu(a1 + MEMORY.ref(4, a1).offset(0x4L).get() / 0x4L * 0x4L);

      //LAB_800e0e28
      for(int i = 0; i < 4; i++) {
        v1 = MEMORY.ref(4, a0.addr_ui_a4.get()).get();
        v1 += MEMORY.ref(4, v1).offset(i * 0x4L).get() / 0x4L * 0x4L;
        MEMORY.ref(4, a0.addr_ui_a4.get()).offset(0x20L).offset(i * 0x4L).setu(v1);
        FUN_800de138(a0, i);
      }
    } else {
      //LAB_800e0e70
      a0.addr_ui_a4.set(0);
    }

    //LAB_800e0e74
    a0.scaleVector_fc.setPad((int)(MEMORY.ref(4, a1).offset(0xcL).get(0xffff_0000L) >> 11));
    v0 = MEMORY.ref(4, a1).offset(0x8L).get();
    if(v0 != 0) {
      a0.ui_a8.set(a1 + v0 / 0x4L * 0x4L);

      //LAB_800e0eac
      for(int i = 0; i < 7; i++) {
        v1 = a0.ui_a8.get();
        v1 += MEMORY.ref(4, v1).offset(i * 0x4L).get() / 0x4L * 0x4L;
        a0.aui_d0.get(i).set(v1);
        FUN_8002246c(a0, i);
      }
    } else {
      //LAB_800e0ef0
      a0.ui_a8.set(a1 + 0x8L);

      //LAB_800e0f00
      for(int i = 0; i < 7; i++) {
        a0.aui_d0.get(i).set(0);
      }
    }

    //LAB_800e0f10
    a0.ui_8c.add(0x4L);

    adjustTmdPointers(MEMORY.ref(4, a0.ui_8c.get(), Tmd::new)); //TODO
    FUN_80021b08(a0.ObjTable_0c, a0.dobj2ArrPtr_00.deref(), a0.coord2ArrPtr_04.deref(), a0.coord2ParamArrPtr_08.deref(), a0.s_c8.get());

    a0.coord2_14.param.set(a0.coord2Param_64);

    insertCoordinate2(null, a0.coord2_14);
    FUN_80021ca0(a0.ObjTable_0c, a0.ui_8c.get(), a0.coord2_14, a0.s_c8.get(), (short)(a0.us_ca.get() + 1));

    a0.ub_a2.set(0);
    a0.ub_a3.set(0);
    a0.ui_f4.set(0);
    a0.ui_f8.set(0);
    a0.us_a0.set(0);

    FUN_80021584(a0, a2);

    a0.coord2_14.coord.transfer.setX(transferX);
    a0.coord2_14.coord.transfer.setY(transferY);
    a0.coord2_14.coord.transfer.setZ(transferZ);

    a0.scaleVector_fc.setX(0x1000);
    a0.scaleVector_fc.setY(0x1000);
    a0.scaleVector_fc.setZ(0x1000);
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
          FUN_80021584(puVar1, puVar1.ui_124.get() + MEMORY.ref(4, (puVar1.ui_124.get() + ((short)puVar1.us_12e.get() * 0x21L + 0x1L) * 0x8L + 0x8L)).get());
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
      if(struct.ub_1c4.get() != 0) {
        MathStruct sp10 = new MathStruct();
        sp10.ui_00.set(0);
        sp10.ui_04.set(0x1000L);
        sp10.ui_08.set(0);
        sp10.ub_0c.set(struct.ub_1c5);
        sp10.ub_0d.set(struct.ub_1c6);
        sp10.ub_0e.set(struct.ub_1c7);
        FUN_8003c6f0(0, sp10);

        MathStruct sp20 = new MathStruct();
        sp20.ui_00.set(0x1000L);
        sp20.ui_04.set(0);
        sp20.ui_08.set(0);
        sp20.ub_0c.set(struct.ub_1c5);
        sp20.ub_0d.set(struct.ub_1c6);
        sp20.ub_0e.set(struct.ub_1c7);
        FUN_8003c6f0(0x1L, sp20);

        MathStruct sp30 = new MathStruct();
        sp30.ui_00.set(0);
        sp30.ui_04.set(0);
        sp30.ui_08.set(0x1000L);
        sp30.ub_0c.set(struct.ub_1c5);
        sp30.ub_0d.set(struct.ub_1c6);
        sp30.ub_0e.set(struct.ub_1c7);
        FUN_8003c6f0(0x2L, sp30);
      }

      //LAB_800e1310
      if(struct.ub_1c8.get() != 0) {
        FUN_8003cce0(struct.s_1ca.get(), struct.s_1cc.get(), struct.s_1ce.get());
      }

      //LAB_800e1334
      FUN_800211d8(struct);

      if(struct.ub_1c4.get() != 0) {
        FUN_8003c6f0(0, _800c66d8);
        FUN_8003c6f0(1, _800c66e8);
        FUN_8003c6f0(2, _800c66f8);
      }

      //LAB_800e1374
      if(struct.ub_1c8.get() != 0) {
        FUN_8003cce0(0x800L, 0x800L, 0x800L);
      }

      //LAB_800e1390
      FUN_800ef0f8(struct, struct.v_1d0.getAddress());
    }

    //LAB_800e139c
    return 0;
  }

  @Method(0x800e13b0L)
  public static void FUN_800e13b0(final long a0) {
    long v1;
    long s3;
    long s5;

    switch((int)loadingStage_800c68e4.get()) {
      case 0x0:
        loadTimImage(_80010544.getAddress());

        if(_80050274.get() != _80052c30.get()) {
          _800bda08.setu(_80050274);
          _80050274.setu(_80052c30);
        }

        //LAB_800e1440
        _800c66d8.ui_00.set(0);
        _800c66d8.ui_04.set(0x1000L);
        _800c66d8.ui_08.set(0);
        _800c66d8.ub_0c.set(0x80);
        _800c66d8.ub_0d.set(0x80);
        _800c66d8.ub_0e.set(0x80);
        FUN_8003c6f0(0, _800c66d8);
        _800c66e8.ui_00.set(0);
        _800c66e8.ui_04.set(0x1000L);
        _800c66e8.ui_08.set(0);
        _800c66e8.ub_0c.set(0);
        _800c66e8.ub_0d.set(0);
        _800c66e8.ub_0e.set(0);
        FUN_8003c6f0(0x1L, _800c66e8);
        _800c66f8.ui_00.set(0);
        _800c66f8.ui_04.set(0x1000L);
        _800c66f8.ui_08.set(0);
        _800c66f8.ub_0c.set(0);
        _800c66f8.ub_0d.set(0);
        _800c66f8.ub_0e.set(0);
        FUN_8003c6f0(0x2L, _800c66f8);
        FUN_8003cce0(0x800L, 0x800L, 0x800L);
        loadingStage_800c68e4.addu(0x1L);
        break;

      case 0x1:
        if(a0 == -0x1L) {
          break;
        }

        v1 = _800bd808.get();
        _800bd808.setu(_800d610c.offset(_80052c30.get() * 0x2L));

        //LAB_800e15b8
        //LAB_800e15ac
        //LAB_800e15b8
        //LAB_800e15b8
        if(_800bd808.get() != v1) {
          FUN_8001ad18();
          FUN_8001e29c(0x4L);
          FUN_8001eadc(_800bd808.get());
        } else {
          //LAB_800e1550
          if(_800bda08.get() == _80052c30.get()) {
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
        if(_800bd782.get() != 0 && (FUN_8001ffb0() & 0x2L) == 0) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x3:
        _800f9eac.setu(0);

        FUN_800f45f8();

        if(_800f9eac.get() == 0x1L) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x4:
        FUN_800f45f8();

        if(_800f9eac.get() == 0x2L) {
          loadingStage_800c68e4.addu(0x1L);
        }

        break;

      case 0x5:
        mrg1Loaded_800c68d0.setu(0);
        mrg0Loaded_800c6874.setu(0);
        mrg1Addr_800c68d8.setu(0);
        mrg0Addr_800c6878.setu(0);

        final UnsignedIntRef sp48 = new UnsignedIntRef();
        final UnsignedIntRef sp4c = new UnsignedIntRef();

        FUN_800e6504(_80052c30.get(), sp48, sp4c);

        if((sp48.get() != 0x1L && sp48.get() != 0x2L) && sp48.get() != 0x3L && sp48.get() != 0x4L) {
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        //LAB_800e1720
        //LAB_800e17c4
        //LAB_800e17d8
        FUN_80015310(sp48.get() + 0x2L, sp4c.get() + 0x1L, 0, getMethodAddress(SMap.class, "FUN_800e3d80", Value.class, long.class, long.class), 0, 0x2L);
        FUN_80015310(sp48.get() + 0x2L, sp4c.get() + 0x2L, 0, getMethodAddress(SMap.class, "FUN_800e3d80", Value.class, long.class, long.class), 0x1L, 0x2L);
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
        callbackIndex_800c6968.setu(_800f5cd4.offset(_80052c30.get() * 0x2L));
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

        final long fileCount = mrg1Addr_800c68d8.deref(4).offset(0x4L).get();
        final long secondLastFileInfoPtr = mrg1Addr_800c68d8.deref(4).offset(0x8L).offset((fileCount - 0x2L) * 0x8L).getAddress(); // pointer to second-last file's addr/size
        final long secondLastFileIndex = fileCount - 0x2L;

        _800c672c.setu(secondLastFileIndex);
        _800c6730.setu(secondLastFileIndex); // Number of files that don't have special-case handling?

        long s4;
        if(MEMORY.ref(4, secondLastFileInfoPtr).offset(0xcL).get() != 0x4L) {
          s3 = mrg1Addr_800c68d8.deref(4).offset(MEMORY.ref(4, secondLastFileInfoPtr).offset(0x8L)).get(); // Second last int before padding
          s4 = mrg1Addr_800c68d8.deref(4).offset(MEMORY.ref(4, secondLastFileInfoPtr).offset(0x8L)).offset(0x4L).get(); // Last int before padding
        } else {
          s3 = 0;
          s4 = 0;
        }

        //LAB_800e1914
        _800c69f0.setu(secondLastFileIndex * 0x22L);
        _800c69f4.setu(secondLastFileIndex * 0x22L + 0x1L);
        _800c69f8.setu(secondLastFileIndex * 0x22L + 0x2L);

        loadTimImage(mrg0Addr_800c6878.deref(4).offset(mrg0Addr_800c6878.deref(4).offset(_800c69f0.get() * 0x8L).offset(0x8L)).getAddress());
        loadTimImage(mrg0Addr_800c6878.deref(4).offset(mrg0Addr_800c6878.deref(4).offset(_800c69f4.get() * 0x8L).offset(0x8L)).getAddress());

        final TimHeader tim = parseTimHeader(mrg0Addr_800c6878.deref(4).offset(mrg0Addr_800c6878.deref(4).offset(_800c69f8.get() * 0x8L).offset(0x8L)).offset(0x4L));
        final RECT imageRect = new RECT();
        imageRect.x.set(tim.imageRect.x);
        imageRect.y.set(tim.imageRect.y);
        imageRect.w.set(tim.imageRect.w);
        imageRect.h.set((short)0x80);

        LoadImage(imageRect, tim.imageAddress.get());
        DrawSync(0);

        final long index = allocateBiggerStruct(0, 0, false, 0, 0);
        _800c6740.setu(index);
        FUN_80015b98(index, mrg1Addr_800c68d8.deref(4).offset(mrg1Addr_800c68d8.deref(4).offset(0x8L)).getAddress());

        //LAB_800e1a38
        for(int i = 0; i < _800c6730.get(); i++) {
          _800c6a00.offset(i * 0x4L).setu(mrg0Addr_800c6878.deref(4).offset(mrg0Addr_800c6878.deref(4).offset(i * 0x108L).offset(0x8L)).getAddress());
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
        for(int i = 0; i < _800c6730.get(); i++) {
          //LAB_800e1ae0
          for(int n = i + 1; n < _800c6730.get(); n++) {
            if(_800c6a00.offset((i + 0x1L) * 0x4L).offset(n * 0x4L).get() == _800c6a00.offset(i * 0x4L).get()) {
              _800c6a50.offset((i + 0x1L) * 0x4L).offset(n * 0x4L).setu(0x80L);
            }

            //LAB_800e1af4
          }

          //LAB_800e1b0c
        }

        //LAB_800e1b20
        s5 = 0x8L;

        //LAB_800e1b54
        for(int i = 0; i < _800c6730.get(); i++) {
          final long index2 = allocateBiggerStruct(0x210L);
          index_800c6880.offset(i * 0x4L).setu(index2);
          FUN_80015a68(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e0ff0", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
          FUN_80015ab4(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e123c", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
          FUN_80015b00(index2, MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800e3df4", int.class, BiggerStruct.classFor(BigStruct.class), BigStruct.class), TriFunctionRef::new));
          FUN_80015b98(index2, mrg1Addr_800c68d8.deref(4).offset(mrg1Addr_800c68d8.deref(4).offset((i + 0x1L) * 0x8L).offset(0x8L)).getAddress());

          final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)index2).deref().innerStruct_00.derefAs(BigStruct.class);
          struct.ub_9d.set((int)_800c6a50.offset(1, i * 0x4L).get());

          FUN_80020a00(struct, _800c6a00.offset(i * 0x4L).get(), mrg0Addr_800c6878.deref(4).offset(mrg0Addr_800c6878.deref(4).offset(s5).offset(0x8L)).getAddress());

          if(i == 0) {
            FUN_800e0d18(bigStruct_800c6748, _800c6a00.get(), mrg0Addr_800c6878.deref(4).offset(mrg0Addr_800c6878.deref(4).offset(0x10L)).getAddress());
            bigStruct_800c6748.coord2_14.coord.transfer.setX(0);
            bigStruct_800c6748.coord2_14.coord.transfer.setY(0);
            bigStruct_800c6748.coord2_14.coord.transfer.setZ(0);
            bigStruct_800c6748.coord2Param_64.rotate.x.set((short)0);
            bigStruct_800c6748.coord2Param_64.rotate.y.set((short)0);
            bigStruct_800c6748.coord2Param_64.rotate.z.set((short)0);
          }

          //LAB_800e1c50
          struct.ui_124.set(mrg0Addr_800c6878.get());
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
          struct.ub_1c4.set(0);
          struct.ub_1c5.set(0x80);
          struct.ub_1c6.set(0x80);
          struct.ub_1c7.set(0x80);

          if(i == 0) {
            struct.us_178.set(1);
          } else {
            //LAB_800e1ce0
            struct.us_178.set(0);
          }

          //LAB_800e1ce4
          v1 = _800bd818.offset(i * 0x14L).getAddress();

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
            FUN_800e4d88(a0, struct);
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
        mrg10Addr_800c6710.setu(0);
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

        _800c6aa0.setu(_800bd7e8.viewpoint_00.getX() - _800bd7e8.refpoint_0c.getX());
        _800c6aa4.setu(_800bd7e8.viewpoint_00.getY() - _800bd7e8.refpoint_0c.getY());
        _800c6aa8.setu(_800bd7e8.viewpoint_00.getZ() - _800bd7e8.refpoint_0c.getZ());

        loadTimImage(_800d689c.getAddress());
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
        mrg0Addr_800c6878.setu(fileAddress);
      }

      case 0x1 -> {
        mrg1Loaded_800c68d0.setu(0x1);
        mrg1Addr_800c68d8.setu(fileAddress);
      }

      case 0x10 -> {
        mrg10Loaded_800c68e0.setu(0x1);
        mrg10Addr_800c6710.setu(fileAddress);
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
    final long v0 = _800f64c6.offset(_80052c30.get() * 0x4L).get();
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

    if(_80052c38.get() < 0x40L) {
      if(MEMORY.ref(4, rectArr_800cb460.getAddress()).offset(_80052c38.get() * 0x4L).get() != 0) {
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

    _800c6ae8.addu(_800f64c4.offset(1, _80052c30.get() * 0x4L).offset(0x2L).get() * _800c6abc.get());

    if(_800c6ae8.get() > 0x1400L) {
      _800bb0f8.setu(_800f74c4.offset(2, _800f64c4.offset(2, _80052c30.get() * 0x4L).getSigned() * 0x8L).offset(FUN_800e49a4() * 0x2L).getSigned());
      _800bb0f4.setu(_800f64c4.offset(1, _80052c30.get() * 0x4L).offset(0x3L));
      return 0x1L;
    }

    //LAB_800e4ce4
    //LAB_800e4ce8
    return 0;
  }

  @Method(0x800e4d00L)
  public static void FUN_800e4d00(final long a0, final long a1) {
    if(FUN_800e5264(_800c6ac0.getAddress(), a0) == 0) {
      //LAB_800e4d34
      final SVECTOR avg = new SVECTOR();
      get3dAverageOfSomething(a1, avg);
      _800c6ac0.transfer.setX(avg.getX());
      _800c6ac0.transfer.setY(avg.getY());
      _800c6ac0.transfer.setZ(avg.getZ());
      _800f7e24.setu(0x2L);
    } else {
      _800f7e24.setu(0x1L);
    }

    //LAB_800e4d74
  }

  @Method(0x800e4d88L)
  public static void FUN_800e4d88(final long a0, final BigStruct a1) {
    if(_800f7e24.get() != 0) {
      if(_800f7e24.get() == 0x1L) {
        a1.coord2_14.coord.set(_800c6ac0);
      } else {
        //LAB_800e4e04
        a1.coord2_14.coord.transfer.set(_800c6ac0.transfer);
      }

      //LAB_800e4e18
      _800f7e24.setu(0);
    } else {
      //LAB_800e4e20
      final SVECTOR sp10 = new SVECTOR();
      get3dAverageOfSomething(a1.coord2_14.coord.getAddress() + 0x18L, sp10);
      a1.coord2_14.coord.transfer.setX(sp10.getX());
      a1.coord2_14.coord.transfer.setY(sp10.getY());
      a1.coord2_14.coord.transfer.setZ(sp10.getZ());
    }

    //LAB_800e4e4c
  }

  @Method(0x800e4e5cL)
  public static void FUN_800e4e5c() {
    assert false;
  }

  @Method(0x800e4f74L)
  public static long FUN_800e4f74(final long a0, final long a1) {
    if(a0 == 0) {
      return 1;
    }

    MEMORY.ref(4, a0).setu(0);
    return 0;
  }

  @Method(0x800e4f8cL)
  public static void FUN_800e4f8c() {
    assert false;
  }

  @Method(0x800e4fecL)
  public static void FUN_800e4fec() {
    // empty
  }

  @Method(0x800e4ff4L)
  public static void FUN_800e4ff4() {
    long s0 = _800c6aec.get();
    long s1 = _800c6aec.getAddress();

    //LAB_800e5018
    while(s0 != 0) {
      if((long)MEMORY.ref(4, s0).offset(0x4L).deref(4).call(MEMORY.ref(4, s0).offset(0x8L).get(), MEMORY.ref(4, s0).offset(0xcL).get()) != 0) {
        MEMORY.ref(4, s1).setu(MEMORY.ref(4, s0));
        removeFromLinkedList(s0);
      } else {
        //LAB_800e5054
        s1 = s0;
      }

      //LAB_800e5058
      s0 = MEMORY.ref(4, s1).get();
    }

    //LAB_800e5068
    _800f7e28.setu(s1);
  }

  @Method(0x800e5084L)
  public static long FUN_800e5084(final long a0, final long a1, final long a2) {
    final long v0 = addToLinkedListTail(0x10L);

    if(v0 == 0) {
      //LAB_800e50e8
      return 0;
    }

    MEMORY.ref(4, v0).offset(0x4L).setu(a0);
    MEMORY.ref(4, v0).offset(0x8L).setu(a1);
    MEMORY.ref(4, v0).offset(0xcL).setu(a2);
    MEMORY.ref(4, v0).setu(0);

    _800f7e28.deref(4).setu(v0);
    _800f7e28.setu(v0);

    //LAB_800e50ec
    return 0x1L;
  }

  @Method(0x800e5104L)
  public static void FUN_800e5104(final long a0, final long a1) {
    FUN_800e13b0(a0);

    MEMORY.ref(4, a1).offset(0x48L).deref(4).call(a1);

    _800f64c0.set(orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()));
    _800c6ae0.addu(0x1L);

    if(_800bb0ab.get() != 0) {
      _800c6ae4.setu(-0x1eL);
    }

    //LAB_800e5184
    FUN_800e4ff4();
  }

  @Method(0x800e519cL)
  public static void FUN_800e519c() {
    if(_800f64c0.isNull()) {
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
    FUN_800e7954(_800f64c0.deref(), matrices, _800c6730.get());

    //LAB_800e5248
  }

  @Method(0x800e5264L)
  public static long FUN_800e5264(final long a0, final long a1) {
    if(_80052c3c.get() != a1) {
      _800cb448.get();
      return 0;
    }

    //LAB_800e5294
    if(_80052c40.get() == 0) {
      return 0;
    }

    //LAB_800e52b0
    FUN_800e7650(_800bed50.get(), _800bed54.get());

    MEMORY.ref(4, a0).offset(0x00L).setu(_800bed30);
    MEMORY.ref(4, a0).offset(0x04L).setu(_800bed34);
    MEMORY.ref(4, a0).offset(0x08L).setu(_800bed38);
    MEMORY.ref(4, a0).offset(0x0cL).setu(_800bed3c);
    MEMORY.ref(4, a0).offset(0x10L).setu(_800bed40);
    MEMORY.ref(4, a0).offset(0x14L).setu(_800bed44);
    MEMORY.ref(4, a0).offset(0x18L).setu(_800bed48);
    MEMORY.ref(4, a0).offset(0x1cL).setu(_800bed4c);

    _80052c40.setu(0);
    _800cb448.setu(0x1L);

    //LAB_800e5320
    return 0x1L;
  }

  @Method(0x800e5330L)
  public static void FUN_800e5330(final Value address, final long a1, final long a2) {
    long s1 = address.get();
    _800cab10.offset(a2 * 0x4L).setu(0x1L);
    long s2 = MEMORY.ref(4, s1).offset(0x4L).get();

    //LAB_800e5374
    for(int s0 = 3; s0 < s2; s0++) {
      final long a0 = s1 + MEMORY.ref(4, s1).offset(s0 * 0x8L).offset(0x8L).get() + 0x4L;

      TimHeader timHeader = parseTimHeader(MEMORY.ref(4, a0));
      LoadImage(timHeader.imageRect, timHeader.imageAddress.get());

      if(timHeader.hasClut()) {
        LoadImage(timHeader.clutRect, timHeader.clutAddress.get());
      }
    }

    //LAB_800e5430
    FUN_800e7500(s1 + MEMORY.ref(4, s1).offset(0x8L).get());
    FUN_800e8cd0(s1 + MEMORY.ref(4, s1).offset(0x18L).get(), (int)MEMORY.ref(4, s1).offset(0x1cL).get(), s1 + MEMORY.ref(4, s1).offset(0x10L).get(), MEMORY.ref(4, s1).offset(0x14L).get());

    DrawSync(0);

    removeFromLinkedList(s1);

    _80052c44.setu(0x2L);
    _80052c48.setu(0);
    _800cab20.setu(0x2L);
  }

  @Method(0x800e54a4L)
  public static void FUN_800e54a4(final Value address, final long a1, final long a2) {
    _800cab04.setu(_800c6af0.getAddress());
    memcpy(_800c6af0.getAddress(), address.get(), (int)a1);
    removeFromLinkedList(address.get());
    FUN_800e6640(_800cab04.get());
    _800cab1c.setu(0x1L);
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
  public static void FUN_800e5914() {
    FUN_800e59a4();
  }

  @Method(0x800e59a4L)
  public static void FUN_800e59a4() {
    long v1;
    boolean a0;

    if(_800cb440.get() == 0) {
      _800cab20.subu(0x1L);

      if(_800cab20.getSigned() >= 0) {
        DrawSync(0);
        FUN_80013200(0x180L, 0);
        DrawSync(0);
        _800caaf4.setu(_80052c30);
        _800caaf8.setu(_80052c34);
        return;
      }
    }

    //LAB_800e5a30
    //LAB_800e5a34
    if(loadingStage_800bb10c.get() == 0) {
      loadingStage_800bb10c.setu(0x1L);
      _800caaf4.setu(_80052c30);
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

    final UnsignedIntRef sp20 = new UnsignedIntRef();
    final UnsignedIntRef sp24 = new UnsignedIntRef();

    //LAB_800e5ac4
    switch((int)_800cb430.get()) {
      case 0x0:
        srand(getTimerValue(0));
        if(_800cb440.get() == 0) {
          FUN_80013200(0x180L, 0);
        }

        //LAB_800e5b2c
        _80052c44.setu(0x2L);
        _800c6ae8.setu(0);
        _800cb430.setu(0x1L);
        break;

      case 0x1:
        _800cab1c.setu(0);
        FUN_8001524c(_80052c4c.getAddress(), 0, getMethodAddress(SMap.class, "FUN_800e54a4", Value.class, long.class, long.class), 0x63L, 0);
        _800cb430.setu(0x2L);
        break;

      case 0x2:
        if(_800cab1c.get() != 0) {
          _800cb430.setu(0x3L);
        }

        break;

      case 0x4:
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _800caafc.setu(_80052c30);
        _800cab00.setu(_80052c34);
        FUN_800e6504(_80052c30.get(), sp20, sp24);
        _800cb430.setu(0x11L);
        break;

      case 0x3:
        _800cab28.setu(0);
        _80052c44.setu(0x1L);
        _800caaf4.setu(_80052c30);
        _800caaf8.setu(_80052c34);
        FUN_800e6504(_80052c30.get(), sp20, sp24);
        if(sp20.get() != drgnBinIndex_800bc058.get()) {
          if(_800cb440.get() != 0) {
            _800cb440.setu(0);
            _800cb430.setu(0x8L);
          }

          //LAB_800e5c9c
          _8004ddc0.setu(sp20.get());
          _800bc05c.setu(0x5L);
          _800cb430.setu(0x16L);
          break;
        }

        //LAB_800e5ccc
        _800cab10.setu(0);
        FUN_80015310(0x2L, sp24.get(), 0, getMethodAddress(SMap.class, "FUN_800e5330", Value.class, long.class, long.class), 0, 0x4L);
        FUN_800e4fec();
        _800cb430.setu(0x6L);
        break;

      case 0x6:
        if(_800cab10.get() != 0) {
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

      case 0xa:
        loadingStage_800c68e4.setu(0);
        FUN_800e13b0(_800caaf8.get());
        _800cb430.setu(0xbL);
        break;

      case 0x9:
        FUN_800e4d00(_80052c30.get(), _80052c34.get());
        FUN_800e81a0(_80052c34.get());
        FUN_800e664c(_80052c30.get(), 0x1L);
        FUN_800e6d4c();

        if(_800cab2c.get() != 0) {
          FUN_800e7328();
          FUN_800e6f38(_800cab30.getAddress());
          FUN_800e74d0();
          _800cab2c.setu(0);
        }

        //LAB_800e5e20
        _800cb430.setu(0xaL);
        break;

      case 0xb:
        FUN_800e13b0(_800caaf8.get());
        if(loadingStage_800c68e4.get() == 0xaL) {
          if(FUN_800e5518(0)) {
            biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.set(_800caaf8.get());
          }

          //LAB_800e5e94
          FUN_800e770c();
          _800bdc34.setu(0);
          _80052c44.setu(0);
          FUN_800136dc(0x2L, 0xaL);
          _800cab24.setu(FUN_800ea974(_800caaf4.get()));
          FUN_800e4ac8();
          _800cb430.setu(0xcL);
          _800bc0b8.setu(0);
          _800c6ae0.setu(0);
        }

        break;

      case 0xc:
        _80052c44.setu(0);
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        if(_8007a398.get(0x10L) != 0 && _800bb0ab.get() == 0) {
          FUN_800e5534(-0x1L, 0x3ffL);
        }

        break;

      case 0xd:
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _800bd7b4.setu(0);
        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e5fa4
          v1 = _800cab28.get();
          _800cab28.addu(0x1L);
          a0 = v1 >= 0xaL;
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
              _80052c38.setu(_800f7e30);
              break;
          }
        }

        break;

      case 0xf:
        _80052c44.setu(0);
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _800bc0b8.setu(0);
        _800f7e4c.setu(0);
        _800cb430.setu(0xcL);
        if(_800bdc34.get() != 0) {
          FUN_800e5534(_80052c30.get(), _80052c34.get());
        }

        break;

      case 0x10:
        _800cb430.setu(0x3L);
        _80052c44.setu(0x3L);
        _800f7e4c.setu(0);
        break;

      case 0x11:
        FUN_800e5104(_800caaf8.get(), _800cab24.get());

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
          v1 = _800cab28.get();
          _800cab28.addu(0x1L);
          a0 = v1 >= 0xaL;
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
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e62b0
          v1 = _800cab28.get();
          _800cab28.addu(0x1L);
          a0 = v1 >= 0xaL;
        } else {
          a0 = true;
        }

        //LAB_800e62cc
        if(a0) {
          _8004dd24.setu(0x8L);
          loadingStage_800bb10c.setu(0);
          _8007a3b8.setu(0x2L);
          _80052c44.setu(0x5L);
          _800f7e4c.setu(0);
          _800bc0b8.setu(0);
        }

        break;

      case 0x13:
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _80052c44.setu(0x5L);
        _8004dd24.setu(0x6L);
        loadingStage_800bb10c.setu(0);
        _8007a3b8.setu(0x2L);
        _800f7e4c.setu(0);
        _800bc0b8.setu(0);
        break;

      case 0x15:
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e6394
          v1 = _800cab28.get();
          _800cab28.addu(0x1L);
          a0 = v1 >= 0xaL;
        } else {
          a0 = true;
        }

        //LAB_800e63b0
        if(a0) {
          _80052c44.setu(0x5L);
          _8004dd24.setu(0x9L);
          loadingStage_800bb10c.setu(0);
          _8007a3b8.setu(0x2L);
          _800f7e4c.setu(0);
          _800bc0b8.setu(0);
        }

        break;

      case 0x14:
        FUN_800e5104(_800caaf8.get(), _800cab24.get());
        _800bd7b4.setu(0);

        if(_800cab28.get() != 0 || _800bb164.get() == 0) {
          if(_800bb164.get() == 0) {
            FUN_800136dc(0x1L, 0xaL);
          }

          //LAB_800e643c
          v1 = _800cab28.get();
          _800cab28.addu(0x1L);
          a0 = v1 >= 0xaL;
        } else {
          a0 = true;
        }

        //LAB_800e6458
        if(a0) {
          FUN_8002a9c0();
          _8004dd24.setu(0x2L);
          _8007a3b8.setu(0x2L);
          loadingStage_800bb10c.setu(0);

          //LAB_800e6484
          _80052c44.setu(0x5L);

          //LAB_800e6490
          _800f7e4c.setu(0);
          _800bc0b8.setu(0);
        }

        break;

      case 0x16:
        _8004dd24.setu(0xaL);
        _8007a3b8.setu(0x2L);
        _80052c44.setu(0x1L);
        loadingStage_800bb10c.setu(0);
        break;

      case 0x17:
        _8004dd24.setu(0x2L);
        _8007a3b8.setu(0x2L);
        loadingStage_800bb10c.setu(0);
        break;
    }

    //caseD_5
  }

  @Method(0x800e6504L)
  public static void FUN_800e6504(final long a0, final UnsignedIntRef a1, final UnsignedIntRef a2) {
    long v0;
    long v1;
    long t0;
    long t1;
    long a3;

    t1 = _800bac60.get();
    v1 = _800cb458.get();
    v0 = a0 << 3;
    v0 += v1;
    v1 = MEMORY.ref(1, v0).offset(0x1L).get();
    v1 >>>= 5;
    v0 = MEMORY.ref(1, v0).offset(0x3L).get();
    a3 = v0 >>> 5;
    v0 = drgnBinIndex_800bc058.get();
    v0--;
    t0 = 0;
    if(v0 == v1) {
      a1.set(v1);
      a3 = 0;
    } else {
      v0 = drgnBinIndex_800bc058.get();
      v0--;
      if(v0 == a3 && t1 >= a3) {
        a1.set(a3);
        a3 = 0x1L;
        //LAB_800e6570
      } else if(a3 >= 0x4L) {
        a1.set(v1);
        a3 = 0;
      } else if(t1 >= a3) {
        //LAB_800e6580
        a1.set(a3);
        a3 = 0x1L;
      } else {
        //LAB_800e658c
        a1.set(v1);
        a3 = 0;
      }
    }

    //LAB_800e6594
    v1 = a1.get();
    v0 = 0x1L;
    if(v1 == v0) {
      t0 = 0x4L;
    } else {
      if(v1 < 0x2L) {
        if(v1 == 0) {
          t0 = 0x4L;
        }
        //LAB_800e65bc
      } else if(v1 == 0x2L || v1 == 0x3L) {
        //LAB_800e65cc
        t0 = 0x4L;
      }
    }

    //LAB_800e65d0
    v0 = a1.get();
    v0++;
    a1.set(v0);
    v1 = _800cb458.get();
    v0 = a0 << 3;
    v0 += v1;

    if(a3 != 0) {
      v1 = MEMORY.ref(1, v0).offset(0x2L).get();
      v0 = MEMORY.ref(1, v0).offset(0x3L).get();
    } else {
      //LAB_800e6604
      v1 = MEMORY.ref(1, v0).get();
      v0 = MEMORY.ref(1, v0).offset(0x1L).get();
    }

    v0 &= 0x1fL;

    //LAB_800e6624
    v0 <<= 8;
    v0 |= v1;
    v1 = v0 << 1;
    v1 += v0;
    v1 += t0;
    a2.set(v1);
  }

  @Method(0x800e6640L)
  public static void FUN_800e6640(final long a0) {
    _800cb458.setu(a0);
  }

  @Method(0x800e664cL)
  public static void FUN_800e664c(final long a0, final long a1) {
    fillMemory(rectArr_800cb460.getAddress(), 0, 0x100L);

    long s0 = _800cb458.get() + a0 * 0x8L;
    long v0 = (short)(MEMORY.ref(1, s0).offset(0x5L).get() << 8 | MEMORY.ref(1, s0).offset(0x4L).get());
    if(v0 < 0) {
      return;
    }

    final long a1_0 = _800cb458.get() + v0 + 0x2000L;

    //LAB_800e66dc
    for(int i = 0; i < (MEMORY.ref(1, s0).offset(0x7L).get() << 8 | MEMORY.ref(1, s0).offset(0x6L).get()); i++) {
      final long v1 = MEMORY.ref(4, a1_0).offset(i * 0x4L).get();
      MEMORY.ref(4, rectArr_800cb460.getAddress()).offset(v1 >> 8 & 0xfcL).setu(v1);
    }

    //LAB_800e671c
  }

  @Method(0x800e6730L)
  public static long FUN_800e6730(final long a0) {
    if(a0 >= 0x40L) {
      return 0;
    }

    return MEMORY.ref(4, rectArr_800cb460.getAddress()).offset(a0 * 0x4L).get();
  }

  @Method(0x800e683cL)
  public static long FUN_800e683c(final HmdSomethingStruct a0) {
    if(a0.uiArr_20.get(0).deref().get() < 0x3L) {
      _800f7e50.setu(a0.uiArr_20.get(0).deref().get());
    }

    //LAB_800e686c
    if(a0.uiArr_20.get(0).deref().get() == 0x3L) {
      FUN_800e76b0(0x400L, 0x400L, a0.uiArr_20.get(1).deref().get());
    }

    //LAB_800e688c
    a0.uiArr_20.get(2).deref().set(_800f7e50.get());
    return 0;
  }

  @Method(0x800e68b4L)
  public static long FUN_800e68b4(final HmdSomethingStruct a0) {
    final IntRef x = new IntRef();
    final IntRef y = new IntRef();
    getScreenOffset(x, y);
    a0.uiArr_20.get(0).deref().set(x.get() & 0xffff_ffffL);
    a0.uiArr_20.get(1).deref().set(y.get() & 0xffff_ffffL);
    return 0;
  }

  @Method(0x800e6904L)
  public static long FUN_800e6904(final HmdSomethingStruct a0) {
    final int x = (int)a0.uiArr_20.get(0).deref().get();
    final int y = (int)a0.uiArr_20.get(1).deref().get();
    final long v1 = _800f7e50.get();

    if(v1 == 0x1L) {
      //LAB_800e695c
      FUN_800e7604(x, y);
    } else {
      if(v1 == 0x2L) {
        //LAB_800e6970
        FUN_800e7604(x, y);
      }

      //LAB_800e697c
      FUN_800e7650(x, y);
    }

    //LAB_800e6988
    _800f7e50.setu(0);
    return 0;
  }

  @Method(0x800e6a64L)
  public static long FUN_800e6a64(final HmdSomethingStruct a0) {
    FUN_800e76b0(a0.uiArr_20.get(0).deref().get(), a0.uiArr_20.get(0).deref().get(), a0.uiArr_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800e6aa0L)
  public static long FUN_800e6aa0(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(3).deref().set(FUN_800e7728(a0.uiArr_20.get(0).deref().get(), a0.uiArr_20.get(1).deref().get(), a0.uiArr_20.get(2).deref().get()));
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
  public static void FUN_800e6d9c(long a0, long a1) {
    long s0;
    long s1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long a2;
    long v1;

    t3 = 0x7fffL;
    t1 = -0x8000L;
    t2 = t3;
    t0 = t1;

    //LAB_800e6dc8
    for(int a3 = 0; a3 < a1; a3++) {
      if(MEMORY.ref(2, a0).offset(0x6L).get() == 0x4eL) {
        t4 = MEMORY.ref(2, a0).offset(0x12L).get();
        a2 = MEMORY.ref(2, a0).offset(0x10L).get();
        v1 = MEMORY.ref(2, a0).offset(0xcL).getSigned() + MEMORY.ref(2, a0).offset(0x10L).getSigned();
        if(v1 < t1) {
          v1 = t1;
        }

        //LAB_800e6e00
        t1 = v1;
        v1 = (short)a2;
        if(t3 < v1) {
          v1 = t3;
        }

        //LAB_800e6e1c
        t3 = v1;
        v1 = MEMORY.ref(2, a0).offset(0xeL).getSigned() + (short)t4;
        if(v1 < t0) {
          v1 = t0;
        }

        //LAB_800e6e44
        t0 = v1;
        v1 = (short)t4;
        if(t2 < v1) {
          v1 = t2;
        }

        //LAB_800e6e60
        t2 = v1;
      }

      //LAB_800e6e64
      a0 += 0x24L;
    }

    //LAB_800e6e74
    s0 = t1 - t3;
    s1 = t0 - t2;
    _800cb560.setu(-s0);
    _800cb564.setu(-s1 / 2);
    _800cb570.setu((s0 - 0x180L) / 2);

    //LAB_800e6ecc
    if(s0 == 0x180L && s1 == 0x100L || FUN_800e6d58(_80052c30.get()) != 0) {
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
  public static void FUN_800e6f38(long a0) {
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s8;
    long v0;
    long v1;

    s5 = a0;
    s2 = 0;
    if((int)_800cb584.get() > 0) {
      s8 = _800cbc90.getAddress();
      s3 = _800cb710.getAddress();
      s6 = matrix_800c3548.getAddress();

      //LAB_800e6f9c
      do {
        s1 = _800cb718.offset(s2 * 0x24L).getAddress();

        MEMORY.ref(1, s1).offset(0x3L).setu(0x4L);
        MEMORY.ref(4, s1).offset(0x4L).setu(0x6480_8080L);

        a0 = MEMORY.ref(2, s5).offset(s2 * 0x24L).offset(0x22L).get();
        s4 = a0 >>> 0x1fL;
        a0 = abs((int)a0);
        if(s2 < _800cb57c.get()) {
          if((a0 - 0x1f0L) < 0x10L) {
            //LAB_800e7000
            v0 = a0 << 6;
          } else {
            v0 = s2 + 0x1f0L;
            v0 <<= 6;
          }

          //LAB_800e7004
          v0 |= 0x30L;
          MEMORY.ref(2, s1).offset(0xeL).setu(v0);
        } else {
          //LAB_800e7010
          v0 = a0 << 6;
          v0 |= 0x30L;
          MEMORY.ref(2, s1).offset(0xeL).setu(v0);
          s0 = s5 + s2 * 0x24L;
          memcpy(_800cbb90.offset((s2 - _800cb57c.get()) * 0x8L).getAddress(), s0 + 0x14L, 8);
          MEMORY.ref(4, s8).offset((s2 - _800cb57c.get()) * 0x4L).setu(MEMORY.ref(4, s0).offset(0x1cL));
        }

        //LAB_800e7074
        MEMORY.ref(1, s1).offset(0x4L).setu(0x80L);
        MEMORY.ref(1, s1).offset(0x5L).setu(0x80L);
        MEMORY.ref(1, s1).offset(0x6L).setu(0x80L);
        MEMORY.ref(1, s1).offset(0x0cL).setu(MEMORY.ref(1, s5).offset(s2 * 0x24L).offset(0x8L));
        MEMORY.ref(1, s1).offset(0x0dL).setu(MEMORY.ref(1, s5).offset(s2 * 0x24L).offset(0xaL));
        MEMORY.ref(2, s1).offset(0x10L).setu(MEMORY.ref(2, s5).offset(s2 * 0x24L).offset(0xcL));
        MEMORY.ref(2, s1).offset(0x12L).setu(MEMORY.ref(2, s5).offset(s2 * 0x24L).offset(0xeL));
        s0 = s3 + s2 * 0x24L;
        FUN_8003b750(s0, 0, 0x1L, MEMORY.ref(2, s5).offset(s2 * 0x24L).offset(0x20L).get());

        if(s4 != 0) {
          gpuLinkedListSetCommandTransparency(s0, true);
        }

        //LAB_800e70ec
        s1 = s3 + s2 * 0x24L;
        s0 = s5 + s2 * 0x24L;
        FUN_8003b7e0(s1, s2 * 0x24L + s3 + 0x8L);
        MEMORY.ref(2, s1).offset(0x1cL).setu(MEMORY.ref(2, s0).offset(0x10L));
        MEMORY.ref(2, s1).offset(0x1eL).setu(MEMORY.ref(2, s0).offset(0x12L));
        v1 = MEMORY.ref(2, s0).offset(0x6L).get();
        if(v1 == 0x4eL) {
          //LAB_800e7148
          MEMORY.ref(2, s3).offset(s2 * 0x24L).offset(0x20L).setu((0x1L << _1f8003c0.get()) - 0x1L);
        } else if(v1 == 0x4fL) {
          MEMORY.ref(2, s3).offset(s2 * 0x24L).offset(0x20L).setu(0x28L);
        } else {
          //LAB_800e7194
          a0 = MEMORY.ref(2, s6).offset(0xcL).get() * MEMORY.ref(2, s5).offset(s2 * 0x24L).get() + MEMORY.ref(2, s6).offset(0xeL).get() * MEMORY.ref(2, s5).offset(s2 * 0x24L).offset(0x2L).get();
          a0 += MEMORY.ref(2, s6).offset(0x10L).get() * MEMORY.ref(2, s5).offset(s2 * 0x24L).offset(0x4L).get();
          a0 >>= 12;
          a0 += MEMORY.ref(4, s6).offset(0x1cL).get();
          a0 >>= 0x10L - _1f8003c0.get();
          MEMORY.ref(2, s3).offset(s2 * 0x24L).offset(0x20L).setu(a0);
        }

        //LAB_800e7210
        MEMORY.ref(2, s3).offset(s2 * 0xcL).offset(0x22L).and(0x3fffL);
        s2++;
      } while(s2 < _800cb584.get());

      //LAB_800e724c
    }

    FUN_800e6d9c(s5, _800cb57c.get());
  }

  @Method(0x800e728cL)
  public static void FUN_800e728c(final MATRIX matrix) {
    //LAB_800e72b4
    for(int s2 = 0; s2 < 3; s2++) {
      //LAB_800e72c4
      for(int s1 = 0; s1 < 3; s1++) {
        long s0 = matrix.getAddress() + s2 * 0x6L + s1 * 0x2L;
        if(abs((int)MEMORY.ref(2, s0).get()) < 0x40L) {
          MEMORY.ref(2, s0).setu(0);
        }

        //LAB_800e72e8
      }
    }
  }

  @Method(0x800e7328L)
  public static void FUN_800e7328() {
    setProjectionPlaneDistance((int)projectionPlaneDistance_800bd810.get());
    FUN_8003cfb0(_800cbd10);
    FUN_800e728c(matrix_800c3548);
    _800cbd68.set(matrix_800c3548);
    TransposeMatrix(_800cbd68, _800cbd40);
    _800bd7e8.set(_800cbd10);
  }

  @Method(0x800e7418L)
  public static void FUN_800e7418(final long xy0, final long z0, final long xy1, final long z1, final long rotation, final long projectionDistance) {
    _800cbd10.viewpoint_00.setX((int)(xy0 & 0xffffL));
    _800cbd10.viewpoint_00.setY((int)(xy0 >>> 16));
    _800cbd10.viewpoint_00.setZ((int)(z0 & 0xffffL));
    _800cbd10.refpoint_0c.setX((int)(xy1 & 0xffffL));
    _800cbd10.refpoint_0c.setY((int)(xy1 >>> 16));
    _800cbd10.refpoint_0c.setZ((int)(z1 & 0xffffL));
    _800cbd10.viewpointTwist_18.set((int)(rotation << 16) >> 4);
    _800cbd10.super_1c.clear();
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
  public static void FUN_800e7500(final long a0) {
    _800cb584.setu(MEMORY.ref(1, a0).offset(0x14L));
    _800cb57c.setu(MEMORY.ref(1, a0).offset(0x15L));
    _800cb580.setu(MEMORY.ref(1, a0).offset(0x16L));

    //TODO SVECs
    FUN_800e7418(
      MEMORY.ref(2, a0).offset(0x2L).get() << 16 | MEMORY.ref(2, a0).get(),
      MEMORY.ref(2, a0).offset(0x6L).get() << 16 | MEMORY.ref(2, a0).offset(0x4L).get(),
      MEMORY.ref(2, a0).offset(0xaL).get() << 16 | MEMORY.ref(2, a0).offset(0x8L).get(),
      MEMORY.ref(2, a0).offset(0xeL).get() << 16 | MEMORY.ref(2, a0).offset(0xcL).get(),
      MEMORY.ref(2, a0).offset(0x12L).get(),
      MEMORY.ref(2, a0).offset(0x10L).get()
    );

    memcpy(_800cab30.getAddress(), a0 + 0x18L, (int)(_800cb584.get() * 0x24L));
    FUN_800e6f38(_800cab30.getAddress());
    fillMemory(_800cb590.getAddress(), 0, 0x180L);
  }

  @Method(0x800e7604L)
  public static void FUN_800e7604(final int x, final int y) {
    if(_800cbd3c.deref(4).get() == 0) {
      _800cbd3c.deref(4).setu(0x1L);
      SetGeomOffset(x, y);
    }
  }

  @Method(0x800e7650L)
  public static void FUN_800e7650(final long x, final long y) {
    if(_800cbd38.deref(4).get() == 0) {
      _800cbd38.deref(4).setu(0x1L);
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
      return _800cb710.offset(2, 0x20L).offset(a3 * 0x24L).get();
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
    return _800cb710.offset(2, 0x20L).offset(a3 * 0x24L).get();
  }

  @Method(0x800e7954L)
  public static void FUN_800e7954(final GsOT ot, MATRIX[] a1, final long a2) {
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
    if(_800cbd38.deref(4).get() == 0) {
      _800cbd38.deref(4).setu(0x1L);

      final IntRef transformedX = new IntRef();
      final IntRef transformedY = new IntRef();
      transformVertex(transformedX, transformedY, v0);
      FUN_800e7f68(transformedX.get(), transformedY.get());
    }

    //LAB_800e8164
    FUN_800e7650(screenOffsetX_800cb568.get(), screenOffsetY_800cb56c.get());
    FUN_800e7604((int)screenOffsetX_800cb568.get(), (int)screenOffsetY_800cb56c.get());
  }

  @Method(0x800e81a0L)
  public static void FUN_800e81a0(final long a0) {
    final long s0_0 = addToLinkedListTail(0x8L);
    fillMemory(s0_0, 0, 0x8L);
    FUN_800e5084(getMethodAddress(SMap.class, "FUN_800e4f74", long.class, long.class), s0_0, 0);
    _800cbd38.setu(s0_0);

    final long s0_1 = addToLinkedListTail(0x8L);
    fillMemory(s0_1, 0, 0x8L);
    FUN_800e5084(getMethodAddress(SMap.class, "FUN_800e4f74", long.class, long.class), s0_1, 0);
    _800cbd3c.setu(s0_1);

    final SVECTOR avg = new SVECTOR();
    get3dAverageOfSomething(a0, avg);
    FUN_800e8104(avg);
  }

  @Method(0x800e828cL)
  public static void FUN_800e828c() {
    assert false;
  }

  @Method(0x800e866cL)
  public static void FUN_800e866c() {
    final long s2 = _800d1a88.deref(4).offset(0xcL).get();

    //LAB_800e86a4
    for(int i = 0; i < s2; i++) {
      final long s0 = _800d1a88.deref(4).offset(0x14L).get() + i * 0xcL;
      long v0 = _800d1a88.deref(4).offset(0x08L).get() + i * 0x8L;
      v0 = abs((int)MEMORY.ref(2, v0).offset(0x2L).get());
      MEMORY.ref(1, s0).offset(0x1L).setu(v0 > 0x40L ? 1 : 0);
    }

    //LAB_800e86f0
  }

  @Method(0x800e8b40L)
  public static void FUN_800e8b40(final long a0, final long a1) {
    final int s3 = (int)(MEMORY.ref(4, a0).offset(0xcL).get() * 0x40L);

    memcpy(_800cbe78.getAddress(), a1, s3 * 0xc);
    memcpy(_800cca78.getAddress(), a1 + s3 * 0xcL, s3);

    MEMORY.ref(4, a0).offset(0x14L).setu(_800cbe78.getAddress());
    MEMORY.ref(4, a0).offset(0x18L).setu(_800cca78.getAddress());
  }

  @Method(0x800e8bd8L)
  public static void FUN_800e8bd8(final long a0) {
    final long v1 = MEMORY.ref(4, a0).get();
    MEMORY.ref(4, a0).offset(0x04L).setu(MEMORY.ref(4, v1).offset(0x00L));
    MEMORY.ref(4, a0).offset(0x08L).setu(MEMORY.ref(4, v1).offset(0x08L));
    MEMORY.ref(4, a0).offset(0x0cL).setu(MEMORY.ref(4, v1).offset(0x14L));
    MEMORY.ref(4, a0).offset(0x10L).setu(MEMORY.ref(4, v1).offset(0x10L));
  }

  @Method(0x800e8c20L)
  public static UnboundedArrayRef<TmdObjTable> adjustTmdPointersAndGetTable(final TmdWithId tmd) {
    adjustTmdPointers(tmd.tmd);
    return tmd.tmd.objTable;
  }

  @Method(0x800e8c50L)
  public static void FUN_800e8c50(final GsDOBJ2 dobj2, final long a1, final long src, final int size) {
    memcpy(_800cfa78.getAddress(), src, size);
    MEMORY.ref(4, a1).offset(0x1cL).setu(_800cfa78.getAddress());
    final UnboundedArrayRef<TmdObjTable> objTables = adjustTmdPointersAndGetTable(_800cfa78);
    updateTmdPacketIlen(objTables, dobj2, 0);
    MEMORY.ref(4, a1).setu(objTables.getAddress());
    FUN_800e8bd8(a1);
  }

  @Method(0x800e8cd0L)
  public static void FUN_800e8cd0(final long src, final int size, final long a2, final long a3) {
    _800d1a88.setu(_800cbe08.getAddress());
    GsCOORDINATE2Ptr_800cbe2c.set(GsCOORDINATE2_800cbda8);
    _800cbe28.setu(_800cbdf8.getAddress());
    insertCoordinate2(null, GsCOORDINATE2_800cbda8);

    _800d1a88.deref(4).offset(0x20L).deref(4).offset(0x4L).setu(_800d1a88.deref(4).offset(0x24L));
    _800d1a88.deref(4).offset(0x20L).deref(4).setu(0x4000_0000L);

    FUN_800e8c50(_800d1a88.deref(4).offset(0x20L).deref(4).cast(GsDOBJ2::new), _800d1a88.get(), src, size);
    FUN_800e8b40(_800d1a88.get(), a2);

    _800f7f10.setu(0);
    _800f7f14.setu(0x1L);

    long s0 = addToLinkedListTail(0x8L);
    fillMemory(s0, 0, 0x8L);
    FUN_800e5084(getMethodAddress(SMap.class, "FUN_800e4f74", long.class, long.class), s0, 0);
    _800cbe34.setu(s0);

    s0 = addToLinkedListTail(0x8L);
    fillMemory(s0, 0, 0x8L);
    FUN_800e5084(getMethodAddress(SMap.class, "FUN_800e4f74", long.class, long.class), s0, 0);
    _800d1a8c.setu(s0);

    s0 = addToLinkedListTail(0x8L);
    fillMemory(s0, 0, 0x8L);
    FUN_800e5084(getMethodAddress(SMap.class, "FUN_800e4f74", long.class, long.class), s0, 0);
    _800cbe38.setu(s0);

    FUN_800e866c();
  }

  @Method(0x800e8e50L)
  public static void FUN_800e8e50() {
    assert false;
  }

  @Method(0x800e92dcL)
  public static long get3dAverageOfSomething(final long a0, final SVECTOR out) {
    out.set((short)0, (short)0, (short)0);

    if(_800f7f14.get() == 0 || (int)a0 < 0 || a0 >= _800d1a88.deref(4).offset(0xcL).get()) {
      //LAB_800e9318
      return 0;
    }

    //LAB_800e932c
    final long v0 = _800d1a88.deref(4).offset(0x14L).deref(4).offset(a0 * 0xcL).getAddress();
    final long t0 = MEMORY.ref(4, v0).offset(0x4L).deref(2).offset(_800d1a88.deref(4).offset(0x10L).get() + 0x6L).getAddress();
    final long count = MEMORY.ref(1, v0).get();

    //LAB_800e937c
    for(int i = 0; i < count; i++) {
      out.add(_800d1a88.deref(4).offset(0x4L).deref(4).offset(MEMORY.ref(2, t0).offset(i * 0x2L).get() * 0x8L).cast(SVECTOR::new));
    }

    //LAB_800e93e0
    out.div((int)count);
    return 0x1L;
  }

  @Method(0x800ea4c8L)
  public static short FUN_800ea4c8(final short a0) {
    assert false;
    return 0;
  }

  @Method(0x800ea90cL)
  public static void FUN_800ea90c(final long a0) {
    if(FUN_800e5518(0)) {
      _80052c38.setu(biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.derefAs(BigStruct.class).ui_16c.get());
    }
  }

  @Method(0x800ea974L)
  public static long FUN_800ea974(final long a0) {
    if((int)a0 < 0) {
      _800d1ad8.setu(getMethodAddress(SMap.class, "FUN_800ea96c", long.class));
    } else {
      //LAB_800ea9a4
      fillMemory(_800d1a90.getAddress(), 0, 0x4cL);

      final long a3 = _800f7f74.getAddress();
      final long a2 = _800d1a90.getAddress();

      //LAB_800ea9d8
      for(int i = 0; i < _800f9374.get(); i++) {
        if(a0 != 0) {
          if(MEMORY.ref(2, a3).offset(i * 0x14L).offset(0x4L).get() == a0) {
            MEMORY.ref(4, a2).offset(MEMORY.ref(4, a2).offset(0x40L).get() * 0x4L).setu(MEMORY.ref(2, a3).offset(i * 0x14L).offset(0x6L));
            MEMORY.ref(4, a2).offset(0x40L).addu(0x1L);
          }
        }

        //LAB_800eaa20
      }

      //LAB_800eaa30
      if(_800d1ad0.get() != 0) {
        _800d1ad8.setu(getMethodAddress(SMap.class, "FUN_800ea84c", long.class));
        _800d1ad4.setu(0x1L);
      } else {
        //LAB_800eaa5c
        _800d1ad8.setu(getMethodAddress(SMap.class, "FUN_800ea90c", long.class));
      }

      //LAB_800eaa6c
    }

    //LAB_800eaa74
    return _800d1a90.getAddress();
  }

  @Method(0x800ed5b0L)
  public static void FUN_800ed5b0() {
    switch((int)loadingStage_800bb10c.get()) {
      case 0x0:
        _800d1cb8.setu(0);
        FUN_80015310(0, 0x1659L, 0, getMethodAddress(SMap.class, "FUN_800edc7c", Value.class, long.class, long.class), _800bf0dc.get(), 0x4L);

        //LAB_800ed644
        for(int s2 = 0; s2 < 3; s2++) {
          final RECT rect = rectArray3_800f96f4.get(s2);
          final long dest = addToLinkedListTail(rect.w.get() * rect.h.get() * 2);
          textureDataPtrArray3_800d4ba0.get(s2).set(dest);
          StoreImage(rectArray3_800f96f4.get(s2), dest);
        }

        SetDispMask(0);

        ClearImage(new RECT((short)0, (short)0, (short)0x3ff, (short)0x1ff), (byte)0, (byte)0, (byte)0);

        DrawSync(0);

        final long a1;
        if(_800f970c.offset(_800bf0dc.get() * 16).get() == 0) {
          //LAB_800ed6f8
          a1 = 0;
        } else {
          a1 = 0x4L;
        }

        //LAB_800ed700
        FUN_80013200(_800f9718.offset(_800bf0dc.get() * 16).get(), a1);

        loadingStage_800bb10c.setu(0x1L);
        break;

      case 0x1:
        if(_800d1cb8.get() != 0 && fileCount_8004ddc8.get() == 0) {
          loadingStage_800bb10c.setu(0x2L);
        }

        break;

      case 0x2:
        _8007a3b8.setu(0x4L);
        FUN_800ed8d0(_800bf0dc.get());

        _800bd808.setu(-0x1L);
        loadingStage_800bb10c.setu(0x3L);
        break;

      case 0x3:
        if(_800bf0d8.get() == 0x5L) {
          loadingStage_800bb10c.setu(0x4L);
        }

        break;

      case 0x4:
        FUN_8002c150(0);
        _8007a3b8.setu(0x2L);
        loadingStage_800bb10c.setu(0);
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
    FUN_80013200(640L, 0);

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
  public static void FUN_800edc7c(final Value a0, long a1, long a2) {
    if(a0.offset(0x4L).get() < a2) {
      return;
    }

    final long v1 = a0.deref(4).offset(a2 * 8).getAddress();
    if(MEMORY.ref(4, v1).offset(0xcL).get() == 0) {
      return;
    }

    long a3 = _800d1cc0.getAddress();
    final long v0 = MEMORY.ref(4, v1).offset(0x8L).get();
    a2 = 0xbb7L;
    a1 = a0.deref(4).offset(v0).getAddress();

    //LAB_800edcbc
    do {
      MEMORY.ref(4, a3).setu(MEMORY.ref(4, a1));
      a1 += 0x4L;
      a3 += 0x4L;
      a2--;
    } while(a2 >= 0);

    removeFromLinkedList(a0.get());
    _800d1cb8.setu(0x1L);

    //LAB_800edcf0
  }

  /** TODO having trouble figuring this method out. May have done something wrong. Why are there 12 elements placed on the stack, but only 9 are read? */
  @Method(0x800edd00L)
  public static void FUN_800edd00(final long a0, final long a1) {
    final long[] sp = new long[12];

    //LAB_800edd14
    for(int i = 0; i < 6; i++) {
      sp[i * 2    ] = MEMORY.ref(2, a0).offset(i * 0x4L).get();
      sp[i * 2 + 1] = MEMORY.ref(2, a0).offset(i * 0x4L).offset(0x2L).getSigned();
    }

    //LAB_800edd5c
    for(int i = 0; i < 3; i++) {
      MEMORY.ref(2, a1).offset(i * 0x6L).offset(0x00L).setu(sp[i * 3    ]);
      MEMORY.ref(2, a1).offset(i * 0x6L).offset(0x02L).setu(sp[i * 3 + 1]);
      MEMORY.ref(2, a1).offset(i * 0x6L).offset(0x04L).setu(sp[i * 3 + 2]);
      MEMORY.ref(4, a1).offset(i * 0x4L).offset(0x14L).setu(sp[i     + 6]);
    }
  }

  @Method(0x800eddb4L)
  public static void FUN_800eddb4() {
    final RECT sp20 = new RECT().set(_800d6b48);

    if(_800c6870.get() == -0x1L) {
      _800f9e5a.setu(-0x1L);
    }

    //LAB_800ede14
    switch((int)(_800f9e5a.get() + 0x1L)) {
      case 0x0:
        if(_800d4bd0.get() != 0) {
          removeFromLinkedList(_800d4bd0.get());
        }

        //LAB_800ee19c
        if(_800d4bd4.get() != 0) {
          removeFromLinkedList(_800d4bd4.get());
        }

        //LAB_800ee1b8
        FUN_80020fe0(_800d4bf8);
        removeFromLinkedList(_800d4be8.get());
        _800f9e5a.setu(0);

        //LAB_800ee1e4
        DrawSync(0);
        break;

      case 0x1:
        _800d4bd0.setu(0);
        _800d4bd4.setu(0);

        if(_80052c30.get() == 0x2a1L) {
          _800d4bd0.setu(addToLinkedListTail(0xb0L));
          _800d4bd4.setu(addToLinkedListTail(0x20L));

          _800d4bd0.deref(2).offset(0x8L).setu(0);
          _800d4bd0.deref(2).offset(0x2L).setu(0);
          _800d4bd0.deref(2).offset(0x0L).setu(0);
          _800d4bd0.deref(2).offset(0x6L).setu(0);
          _800d4bd0.deref(2).offset(0x4L).setu(0);
          _800d4bd0.deref(4).offset(0xcL).setu(0);
        }

        //LAB_800edeb4
        _800d4bdc.setu(0);
        _800d4be0.setu(0);
        _800d4be4.setu(0);
        _800d4be8.setu(0);
        _800d4bec.setu(0);
        _800d4bf0.setu(0);

        if(_800f982c.offset(_80052c30.get() * 0x2L).getSigned() != 0) {
          FUN_80015310(0, _800f982c.offset(_80052c30.get() * 0x2L).getSigned(), 0, getMethodAddress(SMap.class, "FUN_800eeddc", Value.class, long.class, long.class), 0, 0x2L);
          FUN_80015310(0, _800f982c.offset(_80052c30.get() * 0x2L).getSigned() + 0x1L, 0, getMethodAddress(SMap.class, "FUN_800eeddc", Value.class, long.class, long.class), 0x1L, 0x4L);

          if(_80052c30.get() == 0x2a1L) {
            FUN_80015310(0, 0x1dbaL, 0, getMethodAddress(SMap.class, "FUN_800eeddc", Value.class, long.class, long.class), 0x2L, 0x4L);
          }
        }

        _800f9e5a.addu(0x1L);
        break;

      case 0x2:
        if(_800d4bdc.get() == 0x1L && _800d4be0.get() == 0x1L) {
          final TimHeader tim = parseTimHeader(_800d4bec.deref(4).offset(_800d4bec.deref(4).offset(0x8L)).offset(0x4L));
          LoadImage(new RECT((short)0x3f0, (short)0x100, tim.imageRect.w.get(), tim.imageRect.h.get()), tim.imageAddress.get());
          FUN_800edd00(_800d4bec.deref(4).offset(_800d4bec.deref(4).offset(0x10L)).getAddress(), _800d4bb0.getAddress());
          _800f9e5a.addu(0x1L);
          removeFromLinkedList(_800d4bec.get());
          _800c686c.setu(0x1L);
          DrawSync(0);
        }

        break;

      case 0x3:
        if(_80052c30.get() == 0x2a1L) {
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
        _800d4bf8.ub_9d.set(0x91);

        FUN_80020a00(_800d4bf8, _800d4be8.deref(4).offset(_800d4be8.deref(4).offset(0x08L)).getAddress(), _800d4be8.deref(4).offset(_800d4be8.deref(4).offset(0x10L)).getAddress());

        if(_80052c30.get() == 0x2a1L) {
          FUN_800eef6c(sp20, _800d4bd4.get(), _800d4bd0.get());
        }

        //LAB_800ee10c
        //LAB_800ee110
        _800f9e5a.addu(0x1L);
        break;

      case 0x5:
        FUN_800eece0(_800d4bb0.getAddress());

        if(_800d4bd0.get() != 0 && _800d4bd4.get() != 0) {
          FUN_800ee9e0(sp20, _800d4bd4.get(), _800d4bd0.get(), _800f9e5c.getAddress(), _800f9e5e.getAddress());
          FUN_800eef2c(sp20, _800d4bd4.get());
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
  public static void FUN_800eece0(final long a0) {
    _800d4bf8.coord2_14.coord.transfer.setX(0);
    _800d4bf8.coord2_14.coord.transfer.setY(0);
    _800d4bf8.coord2_14.coord.transfer.setZ(0);

    _800d4bf8.coord2Param_64.rotate.setX((short)0);
    _800d4bf8.coord2Param_64.rotate.setY((short)0);
    _800d4bf8.coord2Param_64.rotate.setZ((short)0);

    FUN_800214bc(_800d4bf8);
    FUN_80020b98(_800d4bf8);
    FUN_800eee48(_800d4bf8, a0);
  }

  @Method(0x800eeddcL)
  public static void FUN_800eeddc(Value address, long a1, long a2) {
    if(a2 == 0) {
      _800d4bdc.setu(0x1L);
      _800d4be8.setu(address);
    } else if(a2 == 1) {
      _800d4be0.setu(0x1L);
      _800d4bec.setu(address);
    } else if(a2 == 2) {
      _800d4be4.setu(0x1L);
      _800d4bf0.setu(address);
    }
  }

  @Method(0x800eee48L)
  public static void FUN_800eee48(final BigStruct a0, long a1) {
    _1f8003e8.setu(a0.us_a0.get());
    _1f8003ec.setu(a0.scaleVector_fc.pad.get());

    final MATRIX matrix = new MATRIX();

    //LAB_800eee94
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      FUN_8003d690(a0.ObjTable_0c.top.deref().get(0).coord2_04.deref(), matrix);
      FUN_8003c4a0(matrix);

      PushMatrix();
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x00L).get(), 0);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x04L).get(), 1);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x08L).get(), 2);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x0cL).get(), 3);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x10L).get(), 4);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x14L).get(), 5);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x18L).get(), 6);
      CPU.CTC2(MEMORY.ref(4, a1).offset(0x1cL).get(), 7);
      FUN_80021258(a0.ObjTable_0c.top.deref().get(i));
      PopMatrix();
    }

    //LAB_800eef0c
  }

  @Method(0x800eef2cL)
  public static void FUN_800eef2c(final RECT imageRect, final long imageAddress) {
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

      v1 = MEMORY.ref(2, a2).offset(i * 0x2L).offset(0x90L).get() << 16;
      v0 = (v1 * 0x8888_8889L >>> 32) + v1;
      v0 >>= 5;
      v1 >>= 0x1fL;
      v0 -= v1;
      MEMORY.ref(4, a2).offset(i * 0x4L).offset(0x10L).setu(v0);
    }

    DrawSync(0);
    LoadImage(imageRect, imageAddress);
  }

  @Method(0x800ef0f8L)
  public static void FUN_800ef0f8(final BigStruct a0, final long a1) {
    assert false;
  }

  @Method(0x800ef798L)
  public static void FUN_800ef798() {
    long s1 = _800d4d40.usPtr_180.getAddress(); //TODO is this a sub-struct?
    long s0 = MEMORY.ref(4, s1).offset(0x1cL).get();

    //LAB_800ef7c8
    while(s0 != 0) {
      if(MEMORY.ref(4, s0).get() >= MEMORY.ref(2, s0).offset(0x18L).getSigned()) {
        MEMORY.ref(4, s1).offset(0x1cL).setu(MEMORY.ref(4, s0).offset(0x1cL));
        removeFromLinkedList(s0);
        s0 = MEMORY.ref(4, s1).offset(0x1cL).get();
      } else {
        //LAB_800ef804
        MEMORY.ref(4, s0).offset(0x10L).subu(0x1L);

        _800d4d40.coord2_14.coord.transfer.setX((int)MEMORY.ref(4, s0).offset(0xcL).get());
        _800d4d40.coord2_14.coord.transfer.setY((int)MEMORY.ref(4, s0).offset(0x10L).get());
        _800d4d40.coord2_14.coord.transfer.setZ((int)MEMORY.ref(4, s0).offset(0x14L).get());

        MEMORY.ref(4, s0).offset(0x8L).addu(MEMORY.ref(4, s0).offset(0x4L));

        _800d4d40.scaleVector_fc.setX((int)MEMORY.ref(4, s0).offset(0x8L).get());
        _800d4d40.scaleVector_fc.setY((int)MEMORY.ref(4, s0).offset(0x8L).get());
        _800d4d40.scaleVector_fc.setZ((int)MEMORY.ref(4, s0).offset(0x8L).get());

        FUN_800214bc(_800d4d40);
        FUN_800211d8(_800d4d40);

        _800d4d40.us_9e.set(0);
        _800d4d40.coord2ArrPtr_04.deref().get(0).flg.sub(0x1L);
        MEMORY.ref(4, s0).addu(0x1L);

        s0 = MEMORY.ref(4, s0).offset(0x1cL).get();
        s1 = s0;
      }

      //LAB_800ef888
    }

    //LAB_800ef894
  }

  @Method(0x800ef8acL)
  public static void FUN_800ef8ac() {
    long a0;
    long a2;
    long s0;
    long s1;
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
    s1 = _800d4e68.getAddress();
    s0 = MEMORY.ref(4, s1).offset(0x50L).get();
    while(s0 != 0) {
      if(MEMORY.ref(2, s0).offset(0x4L).getSigned() >= MEMORY.ref(2, s0).offset(0x6L).getSigned()) {
        MEMORY.ref(4, s1).offset(0x50L).setu(MEMORY.ref(4, s0).offset(0x50L));
        removeFromLinkedList(s0);
        s0 = MEMORY.ref(4, s1).offset(0x50L).get();
      } else {
        //LAB_800efa08
        s1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);
        MEMORY.ref(1, s1).offset(0x3L).setu(0x9L);
        MEMORY.ref(4, s1).offset(0x4L).setu(0x2c80_8080L);
        v1 = MEMORY.ref(2, s0).getSigned();
        if(v1 != 0 && v1 != 0x2L) {
          //LAB_800efb7c
          if(v1 == 0x1L) {
            v1 = MEMORY.ref(4, s0).offset(0x8L).get() + MEMORY.ref(4, s0).offset(0xcL).get();
            v0 = (int)v1 >> 16;
            MEMORY.ref(4, s0).offset(0x10L).setu(v0);
            v0 = MEMORY.ref(2, s0).offset(0x24L).get();
            MEMORY.ref(4, s0).offset(0x8L).setu(v1);
            v1 = (int)v1 >> 17;
            v0 -= v1;
            MEMORY.ref(2, s0).offset(0x20L).setu(v0);
            a2 = MEMORY.ref(2, s0).offset(0x20L).get();
            v0 = MEMORY.ref(2, s0).offset(0x2cL).get() - v1;
            MEMORY.ref(2, s0).offset(0x22L).setu(v0);
            a0 = screenOffsetY - MEMORY.ref(2, s0).offset(0x1cL).get() + v0;
            MEMORY.ref(2, s1).offset(0xaL).setu(a0);
            v1 = screenOffsetX - MEMORY.ref(2, s0).offset(0x18L).get() + a2;
            MEMORY.ref(2, s1).offset(0x8L).setu(v1);
            MEMORY.ref(2, s1).offset(0x12L).setu(a0);
            MEMORY.ref(2, s1).offset(0x18L).setu(v1);
            MEMORY.ref(2, s1).offset(0x10L).setu(v1 + MEMORY.ref(2, s0).offset(0x10L).get());
            MEMORY.ref(2, s1).offset(0x1aL).setu(a0 + MEMORY.ref(2, s0).offset(0x10L).get());
            MEMORY.ref(2, s1).offset(0x20L).setu(v1 + MEMORY.ref(2, s0).offset(0x10L).get());
            MEMORY.ref(2, s1).offset(0x22L).setu(a0 + MEMORY.ref(2, s0).offset(0x10L).get());

            if(MEMORY.ref(2, s0).offset(0x4L).get(0x3L) == 0) {
              MEMORY.ref(2, s0).offset(0x2cL).subu(0x1L);
            }

            //LAB_800efc4c
            MEMORY.ref(2, s1).offset(0x16L).setu(_800d6050.offset(2, 0xcL));
            MEMORY.ref(2, s1).offset(0xeL).setu(_800d6068.offset(2, 0xcL));
          }
        } else {
          //LAB_800efa44
          a0 = screenOffsetX - MEMORY.ref(2, s0).offset(0x18L).get();
          v1 = screenOffsetY - MEMORY.ref(2, s0).offset(0x1cL).get();
          MEMORY.ref(2, s1).offset(0x08L).setu(a0 + MEMORY.ref(2, s0).offset(0x20L).get());
          MEMORY.ref(2, s1).offset(0x0aL).setu(v1 + MEMORY.ref(2, s0).offset(0x22L).get());
          MEMORY.ref(2, s1).offset(0x10L).setu(a0 + MEMORY.ref(2, s0).offset(0x28L).get());
          MEMORY.ref(2, s1).offset(0x12L).setu(v1 + MEMORY.ref(2, s0).offset(0x2aL).get());
          MEMORY.ref(2, s1).offset(0x18L).setu(a0 + MEMORY.ref(2, s0).offset(0x30L).get());
          MEMORY.ref(2, s1).offset(0x1aL).setu(v1 + MEMORY.ref(2, s0).offset(0x32L).get());
          MEMORY.ref(2, s1).offset(0x20L).setu(a0 + MEMORY.ref(2, s0).offset(0x38L).get());
          MEMORY.ref(2, s1).offset(0x22L).setu(v1 + MEMORY.ref(2, s0).offset(0x3aL).get());

          if(MEMORY.ref(2, s0).getSigned() == 0x2L) {
            MEMORY.ref(2, s1).offset(0x16L).setu(FUN_8003b3f0(0, 0x2L, 0x3c0L, 0x140L));
            MEMORY.ref(2, s1).offset(0xeL).setu(FUN_8003b430(0x3c0L, 0x1d0L));
          } else {
            //LAB_800efb64
            MEMORY.ref(2, s1).offset(0x16L).setu(_800d6050.offset(2, 0xeL));
            MEMORY.ref(2, s1).offset(0xeL).setu(_800d6068.offset(2, 0xeL));
          }
        }

        //LAB_800efc64
        v1 = sp50[(int)MEMORY.ref(2, s0).getSigned()];
        if(MEMORY.ref(2, s0).offset(0x4L).getSigned() >= v1) {
          v0 = MEMORY.ref(4, s0).offset(0x44L).get() - MEMORY.ref(4, s0).offset(0x40L).get();
          MEMORY.ref(4, s0).offset(0x44L).setu(v0);
          v0 >>>= 16;
          MEMORY.ref(4, s0).offset(0x48L).setu(v0);
          if(v0 >= 0x100L) {
            MEMORY.ref(2, s0).offset(0x48L).setu(0);
          }
        }

        //LAB_800efcb8
        MEMORY.ref(4, s1).offset(0x4L).setu((MEMORY.ref(1, s1).offset(0x7L).get() | 0x2L) << 24 | 0x80_8080L);
        MEMORY.ref(4, s1).offset(0x4L).setu((MEMORY.ref(1, s1).offset(0x7L).get() & 0xfeL) << 24 | 0x80_8080L);
        MEMORY.ref(1, s1).offset(0x4L).setu(MEMORY.ref(1, s0).offset(0x48L));
        MEMORY.ref(1, s1).offset(0x5L).setu(MEMORY.ref(1, s0).offset(0x48L));
        MEMORY.ref(1, s1).offset(0x6L).setu(MEMORY.ref(1, s0).offset(0x48L));
        MEMORY.ref(1, s1).offset(0xcL).setu(sp10[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0xdL).setu(sp20[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0x14L).setu(sp10[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()] + sp30[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0x15L).setu(sp20[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0x1cL).setu(sp10[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0x1dL).setu(sp20[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()] + sp40[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0x24L).setu(sp10[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()] + sp30[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);
        MEMORY.ref(1, s1).offset(0x25L).setu(sp20[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()] + sp40[(int)MEMORY.ref(2, s0).offset(0x2L).getSigned()]);

        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)MEMORY.ref(4, s0).offset(0x4cL).get()).getAddress(), s1);

        MEMORY.ref(2, s0).offset(0x4L).addu(0x1L);
        s0 = MEMORY.ref(4, s0).offset(0x50L).get();
        s1 = s0;
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
    FUN_80020a00(_800d4d40, _800d6d1c.offset(_800d6d1c.offset(4, 0x28L)).getAddress(), _800d6d1c.offset(_800d6d1c.offset(4, 0x30L)).getAddress());
    _800d4eb8.setu(0);
    _800d4edc.setu(0);
    FUN_800f0e60();
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
    long[] sp10 = new long[8];

    long v0 = _800d6d1c.getAddress();
    FUN_80020a00(_800d5eb0, v0 + MEMORY.ref(4, v0).offset(0x18L).get(), v0 + MEMORY.ref(4, v0).offset(0x20L).get());
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
    FUN_8003d9d0(sp10.innerStruct_00.derefAs(BigStruct.class).coord2_14, sp0x120);

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
  public static void FUN_800f3a48() {
    FUN_800f3c98(_800d6d1c.offset(_800d6d24).getAddress(), _800d5588.getAddress());
    FUN_800f3c98(_800d6d1c.offset(_800d6d2c).getAddress(), _800d5590.getAddress());
    FUN_800f3b64(_800d6d1c.offset(_800d6d24).getAddress(), _800d6d1c.offset(_800d6d1c.offset(0x10L)).getAddress(), _800d4ff0.getAddress(), 0x15L);
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
  public static void FUN_800f3b64(long a0, long a1, long a2, long a3) {
    long t2 = a2;
    a2 += 0x24L;

    //LAB_800f3bc4
    for(int i = 0; i < a3; i++) {
      final long v1;
      if(i == 0) {
        MEMORY.ref(2, a2).offset(-0xcL).setu(_800d6050);
        MEMORY.ref(2, a2).offset(-0xaL).setu(_800d6068);
        MEMORY.ref(4, a2).offset(-0x8L).setu(_800d6ca8);
        MEMORY.ref(4, a2).offset(-0x4L).setu(_800d6cac);
        v1 = a0;
      } else {
        //LAB_800f3bfc
        MEMORY.ref(2, a2).offset(-0xcL).setu(_800d6052);
        MEMORY.ref(2, a2).offset(-0xaL).setu(_800d606a);
        MEMORY.ref(4, a2).offset(-0x8L).setu(_800d6cb0);
        MEMORY.ref(4, a2).offset(-0x4L).setu(_800d6cb4);
        v1 = a1;
      }

      //LAB_800f3c24
      MEMORY.ref(4, t2).setu(0);
      MEMORY.ref(4, a2).offset(-0x20L).setu(0);
      MEMORY.ref(4, a2).offset(-0x18L).setu(0);
      MEMORY.ref(4, a2).offset(-0x14L).setu(0);
      MEMORY.ref(4, a2).offset(-0x1cL).setu(MEMORY.ref(1, v1).offset(0xaL));
      MEMORY.ref(4, a2).offset(0x18L).setu(0);
      MEMORY.ref(4, a2).offset(0x14L).setu(0);
      MEMORY.ref(4, a2).offset(0x10L).setu(0);
      MEMORY.ref(4, a2).offset(0xcL).setu(0);
      MEMORY.ref(4, a2).offset(0x8L).setu(0x1000L);
      MEMORY.ref(4, a2).offset(0x4L).setu(0x1000L);
      MEMORY.ref(1, a2).offset(0x2L).setu(0x80L);
      MEMORY.ref(1, a2).offset(0x1L).setu(0x80L);
      MEMORY.ref(1, a2).offset(0x0L).setu(0x80L);
      MEMORY.ref(4, a2).offset(-0x10L).setu(MEMORY.ref(2, v1).offset(0x6L).get() - 0x1L);
      t2 += 0x44L;
      a2 += 0x44L;
    }

    //LAB_800f3c88
  }

  @Method(0x800f3b3cL)
  public static void FUN_800f3b3c() {
    assert false;
  }

  @Method(0x800f3c98L)
  public static void FUN_800f3c98(final long a0, final long a1) {
    MEMORY.ref(4, a1).setu(a0);
    MEMORY.ref(4, a1).offset(0x4L).setu(a0 + (MEMORY.ref(2, a0).offset(0x6L).get() + 0x1L) * 0x8L);
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
          MEMORY.ref(4, s0).offset(0x38L).setu(_800d5eb0.ui_160.get());

          _800d5eb0.ui_160.set(s0);
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
            MEMORY.ref(4, s0).offset(0x38L).setu(_800d5eb0.ui_160.get());

            _800d5eb0.ui_160.set(s0);
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
        MEMORY.ref(1, a1).offset(0xeL).setu(_800d5eb0.s_1ca.get() & 0xffffL);
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

  @Method(0x800f45f8L)
  public static void FUN_800f45f8() {
    if(_800f9eac.getSigned() == -0x1L) {
      _800f9ea8.setu(-0x1L);
    }

    //LAB_800f4624
    final long v1 = _800f9ea8.getSigned();
    if(v1 == 0) {
      //LAB_800f4660
      FUN_800f4754(0xbL);
      FUN_8003b780(linkedListAddress_1f8003d8.get(), new RECT((short)992, (short)288, (short)8, (short)64), 984L, 288L);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(1).getAddress(), linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0x18L);
      _800f9ea8.addu(0x1L);
      _800f9eac.setu(0x1L);
      DrawSync(0);
    } else if(v1 == 0x1L) {
      //LAB_800f4650
      //LAB_800f46d8
      _800d4fe8.setu(0);
      FUN_800f3a48();
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

  @Method(0x800f4754L)
  public static void FUN_800f4754(long a0) {
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
