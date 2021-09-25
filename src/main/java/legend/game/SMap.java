package legend.game;

import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.BigStruct;
import legend.game.types.WeirdTimHeader;

import java.util.function.Function;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SStrm.FUN_800fb7cc;
import static legend.game.SStrm.FUN_800fb90c;
import static legend.game.SStrm.stopFmv;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_80013200;
import static legend.game.Scus94491BpeSegment.FUN_800136dc;
import static legend.game.Scus94491BpeSegment.FUN_8001524c;
import static legend.game.Scus94491BpeSegment.FUN_80015310;
import static legend.game.Scus94491BpeSegment.FUN_800158cc;
import static legend.game.Scus94491BpeSegment.FUN_80015918;
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
import static legend.game.Scus94491BpeSegment._1f8003d0;
import static legend.game.Scus94491BpeSegment._80010544;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.fillRects;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020a00;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020fe0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021048;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021050;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021058;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021060;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218f0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021b08;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002246c;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022590;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a9c0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c150;
import static legend.game.Scus94491BpeSegment_8002.abs;
import static legend.game.Scus94491BpeSegment_8002.getTimerValue;
import static legend.game.Scus94491BpeSegment_8002.setScreenOffset;
import static legend.game.Scus94491BpeSegment_8002.srand;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b750;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b780;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b7e0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b8f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b900;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c660;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c6f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003cce0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003cfb0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003e5d0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003fa40;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003fab0;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.insertCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040b90;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004ddc0;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8005._80050274;
import static legend.game.Scus94491BpeSegment_8005._80052c30;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._80052c38;
import static legend.game.Scus94491BpeSegment_8005._80052c3c;
import static legend.game.Scus94491BpeSegment_8005._80052c40;
import static legend.game.Scus94491BpeSegment_8005._80052c44;
import static legend.game.Scus94491BpeSegment_8005._80052c48;
import static legend.game.Scus94491BpeSegment_8005._80052c4c;
import static legend.game.Scus94491BpeSegment_8005._8005a370;
import static legend.game.Scus94491BpeSegment_8007._8007a398;
import static legend.game.Scus94491BpeSegment_8007._8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bac60;
import static legend.game.Scus94491BpeSegment_800b._800bb0ab;
import static legend.game.Scus94491BpeSegment_800b._800bb0ac;
import static legend.game.Scus94491BpeSegment_800b._800bb0b0;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb164;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc05c;
import static legend.game.Scus94491BpeSegment_800b._800bc0b8;
import static legend.game.Scus94491BpeSegment_800b.biggerStructPtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b._800bd782;
import static legend.game.Scus94491BpeSegment_800b._800bd7b4;
import static legend.game.Scus94491BpeSegment_800b._800bd7b8;
import static legend.game.Scus94491BpeSegment_800b._800bd7e8;
import static legend.game.Scus94491BpeSegment_800b._800bd7ec;
import static legend.game.Scus94491BpeSegment_800b._800bd7f0;
import static legend.game.Scus94491BpeSegment_800b._800bd7f4;
import static legend.game.Scus94491BpeSegment_800b._800bd7f8;
import static legend.game.Scus94491BpeSegment_800b._800bd7fc;
import static legend.game.Scus94491BpeSegment_800b._800bd800;
import static legend.game.Scus94491BpeSegment_800b._800bd804;
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
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.loadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.projectionPlaneDistance_800bd810;
import static legend.game.Scus94491BpeSegment_800c._800c6740;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;

public final class SMap {
  private SMap() { }

  public static final Value _800c66d8 = MEMORY.ref(4, 0x800c66d8L);
  public static final Value _800c66dc = MEMORY.ref(4, 0x800c66dcL);
  public static final Value _800c66e0 = MEMORY.ref(4, 0x800c66e0L);
  public static final Value _800c66e4 = MEMORY.ref(1, 0x800c66e4L);
  public static final Value _800c66e5 = MEMORY.ref(1, 0x800c66e5L);
  public static final Value _800c66e6 = MEMORY.ref(1, 0x800c66e6L);
  public static final Value _800c66e7 = MEMORY.ref(1, 0x800c66e7L);
  public static final Value _800c66e8 = MEMORY.ref(4, 0x800c66e8L);
  public static final Value _800c66ec = MEMORY.ref(4, 0x800c66ecL);
  public static final Value _800c66f0 = MEMORY.ref(4, 0x800c66f0L);
  public static final Value _800c66f4 = MEMORY.ref(1, 0x800c66f4L);
  public static final Value _800c66f5 = MEMORY.ref(1, 0x800c66f5L);
  public static final Value _800c66f6 = MEMORY.ref(1, 0x800c66f6L);
  public static final Value _800c66f7 = MEMORY.ref(1, 0x800c66f7L);
  public static final Value _800c66f8 = MEMORY.ref(4, 0x800c66f8L);
  public static final Value _800c66fc = MEMORY.ref(4, 0x800c66fcL);
  public static final Value _800c6700 = MEMORY.ref(4, 0x800c6700L);
  public static final Value _800c6704 = MEMORY.ref(1, 0x800c6704L);
  public static final Value _800c6705 = MEMORY.ref(1, 0x800c6705L);
  public static final Value _800c6706 = MEMORY.ref(1, 0x800c6706L);
  public static final Value _800c6707 = MEMORY.ref(1, 0x800c6707L);
  public static final Value _800c6708 = MEMORY.ref(2, 0x800c6708L);
  public static final Value _800c670a = MEMORY.ref(2, 0x800c670aL);
  public static final Value _800c670c = MEMORY.ref(2, 0x800c670cL);
  public static final Value _800c670e = MEMORY.ref(2, 0x800c670eL);
  public static final Value mrgAddr_800c6710 = MEMORY.ref(4, 0x800c6710L);
  public static final Value _800c6714 = MEMORY.ref(4, 0x800c6714L);
  public static final Value _800c6718 = MEMORY.ref(4, 0x800c6718L);
  public static final Value _800c671c = MEMORY.ref(4, 0x800c671cL);
  public static final Value _800c6720 = MEMORY.ref(4, 0x800c6720L);

  public static final Value _800c672c = MEMORY.ref(4, 0x800c672cL);
  public static final Value _800c6730 = MEMORY.ref(4, 0x800c6730L);
  public static final Value _800c6734 = MEMORY.ref(4, 0x800c6734L);
  public static final Value _800c6738 = MEMORY.ref(2, 0x800c6738L);

  public static final Value _800c673c = MEMORY.ref(4, 0x800c673cL);

  /** TODO some of the values below might overlap this struct */
  public static final BigStruct bigStruct_800c6748 = MEMORY.ref(4, 0x800c6748L, BigStruct::new);

  public static final Value _800c686c = MEMORY.ref(2, 0x800c686cL);
  public static final Value _800c686e = MEMORY.ref(2, 0x800c686eL);
  public static final Value _800c6870 = MEMORY.ref(2, 0x800c6870L);

  public static final Value _800c6874 = MEMORY.ref(4, 0x800c6874L);
  public static final Value mrgAddr_800c6878 = MEMORY.ref(4, 0x800c6878L);
  public static final Value _800c687c = MEMORY.ref(2, 0x800c687cL);
  public static final Value _800c687e = MEMORY.ref(2, 0x800c687eL);

  public static final Value index_800c6880 = MEMORY.ref(4, 0x800c6880L);

  public static final Value _800c68d0 = MEMORY.ref(4, 0x800c68d0L);

  public static final Value mrgAddr_800c68d8 = MEMORY.ref(4, 0x800c68d8L);

  public static final Value _800c68e0 = MEMORY.ref(2, 0x800c68e0L);

  public static final Value loadingStage_800c68e4 = MEMORY.ref(4, 0x800c68e4L);

  public static final Value callbackIndex_800c6968 = MEMORY.ref(2, 0x800c6968L);

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

  public static final MATRIX _800c6ac0 = MEMORY.ref(4, 0x800c6ac0L, MATRIX::new);
  public static final Value _800c6ae0 = MEMORY.ref(4, 0x800c6ae0L);
  public static final Value _800c6ae4 = MEMORY.ref(4, 0x800c6ae4L);
  public static final Value _800c6ae8 = MEMORY.ref(4, 0x800c6ae8L);

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

  public static final Value _800cbd10 = MEMORY.ref(4, 0x800cbd10L);
  public static final Value _800cbd14 = MEMORY.ref(4, 0x800cbd14L);
  public static final Value _800cbd18 = MEMORY.ref(4, 0x800cbd18L);
  public static final Value _800cbd1c = MEMORY.ref(4, 0x800cbd1cL);
  public static final Value _800cbd20 = MEMORY.ref(4, 0x800cbd20L);
  public static final Value _800cbd24 = MEMORY.ref(4, 0x800cbd24L);
  public static final Value _800cbd28 = MEMORY.ref(4, 0x800cbd28L);
  public static final Value _800cbd2c = MEMORY.ref(4, 0x800cbd2cL);
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

  /** unknown size */
  public static final Value _800cfa78 = MEMORY.ref(4, 0x800cfa78L);

  public static final Value _800d1a88 = MEMORY.ref(4, 0x800d1a88L);
  public static final Value _800d1a8c = MEMORY.ref(4, 0x800d1a8cL);
  public static final ArrayRef<RECT> _800d1a90 = MEMORY.ref(4, 0x800d1a90L, ArrayRef.of(RECT.class, 8, 8, RECT::new));
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
  public static final Value _800d4eb8 = MEMORY.ref(4, 0x800d4eb8L);

  /** TODO part of previous struct? */
  public static final Value _800d4edc = MEMORY.ref(4, 0x800d4edcL);

  public static final Value _800d4fc0 = MEMORY.ref(4, 0x800d4fc0L);

  public static final Value _800d4fe0 = MEMORY.ref(4, 0x800d4fe0L);

  public static final Value _800d4fe8 = MEMORY.ref(2, 0x800d4fe8L);

  public static final Value _800d4ff0 = MEMORY.ref(4, 0x800d4ff0L);

  public static final Value _800d5588 = MEMORY.ref(4, 0x800d5588L);

  public static final Value _800d5590 = MEMORY.ref(4, 0x800d5590L);

  public static final Value _800d5598 = MEMORY.ref(2, 0x800d5598L);

  public static final Value _800d5630 = MEMORY.ref(2, 0x800d5630L);

  public static final BigStruct _800d5eb0 = MEMORY.ref(4, 0x800d5eb0L, BigStruct::new);

  public static final Value _800d6050 = MEMORY.ref(2, 0x800d6050L);
  public static final Value _800d6052 = MEMORY.ref(2, 0x800d6052L);

  public static final Value _800d6068 = MEMORY.ref(2, 0x800d6068L);
  public static final Value _800d606a = MEMORY.ref(2, 0x800d606aL);

  public static final Value _800d610c = MEMORY.ref(2, 0x800d610cL);

  public static final Value _800d689c = MEMORY.ref(4, 0x800d689cL);

  public static final RECT _800d6b48 = MEMORY.ref(4, 0x800d6b48L, RECT::new);

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

  /** TODO this address is in the middle of a function...? */
  public static final Value _800e4f74 = MEMORY.ref(4, 0x800e4f74L);

  /** TODO this address is in the middle of a function...? */
  public static final Value _800ea84c = MEMORY.ref(4, 0x800ea84cL);

  /** TODO this address is in the middle of a function...? */
  public static final Value _800ea90c = MEMORY.ref(4, 0x800ea90cL);

  /** TODO this address is in the middle of a function...? */
  public static final Value _800ea96c = MEMORY.ref(4, 0x800ea96cL);

  /** TODO this address is in the middle of a function...? */
  public static final Value _800eeddc = MEMORY.ref(4, 0x800eeddcL);

  /** TODO an array of 0x14-long somethings */
  public static final Value _800f5930 = MEMORY.ref(4, 0x800f5930L);

  public static final ArrayRef<Pointer<RunnableRef>> callbackArr_800f5ad4 = (ArrayRef<Pointer<RunnableRef>>)MEMORY.ref(4, 0x800f5ad4L, ArrayRef.of(Pointer.class, 0x80, 4, (Function)Pointer.of(4, RunnableRef::new)));
  public static final Value _800f5cd4 = MEMORY.ref(2, 0x800f5cd4L);

  public static final Value _800f64ac = MEMORY.ref(4, 0x800f64acL);

  public static final Value _800f64c0 = MEMORY.ref(4, 0x800f64c0L);

  public static final Value _800f64c6 = MEMORY.ref(1, 0x800f64c6L);

  public static final Value _800f7e24 = MEMORY.ref(4, 0x800f7e24L);
  public static final Value _800f7e28 = MEMORY.ref(4, 0x800f7e28L);
  public static final Value _800f7e2c = MEMORY.ref(4, 0x800f7e2cL);
  public static final Value _800f7e30 = MEMORY.ref(4, 0x800f7e30L);

  public static final Value _800f7e4c = MEMORY.ref(4, 0x800f7e4cL);

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

  public static final Value _800f9e78 = MEMORY.ref(2, 0x800f9e78L);

  public static final Value _800f9e9c = MEMORY.ref(4, 0x800f9e9cL);

  public static final Value _800f9ea8 = MEMORY.ref(4, 0x800f9ea8L);
  public static final Value _800f9eac = MEMORY.ref(4, 0x800f9eacL);
  public static final Value _800f9eb0 = MEMORY.ref(4, 0x800f9eb0L);

  @Method(0x800d9e64L)
  public static void FUN_800d9e64(final GsDOBJ2 dobj2, final long a1) {
    long v0 = dobj2.tmd.get();
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
  public static long FUN_800df168(final long a0) {
    MEMORY.ref(4, a0).offset(0x24L).setu(MEMORY.ref(4, a0).offset(0x20L));
    MEMORY.ref(4, a0).offset(0x20L).setu(MEMORY.ref(4, a0).offset(0x4L).get() + 0x44L);
    return FUN_800dfe0c(a0);
  }

  @Method(0x800df198L)
  public static long FUN_800df198(final long a0) {
    MEMORY.ref(4, a0).offset(0x24L).setu(MEMORY.ref(4, a0).offset(0x20L));
    MEMORY.ref(4, a0).offset(0x20L).setu(MEMORY.ref(4, a0).offset(0x4L).get() + 0x44L);
    return FUN_800dfec8(a0);
  }

  @Method(0x800df258L)
  public static long FUN_800df258(final long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();
    struct.coord2_14.coord.setTransferVector(0, (int)MEMORY.ref(4, a0).offset(0x24L).deref(4).get());
    struct.coord2_14.coord.setTransferVector(1, (int)MEMORY.ref(4, a0).offset(0x28L).deref(4).get());
    struct.coord2_14.coord.setTransferVector(2, (int)MEMORY.ref(4, a0).offset(0x2cL).deref(4).get());
    struct.us_170.set(0);
    return 0;
  }

  @Method(0x800df2b8L)
  public static long FUN_800df2b8(final long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();
    MEMORY.ref(4, a0).offset(0x24L).deref(4).setu(struct.coord2_14.coord.getTransferVector(0));
    MEMORY.ref(4, a0).offset(0x28L).deref(4).setu(struct.coord2_14.coord.getTransferVector(1));
    MEMORY.ref(4, a0).offset(0x2cL).deref(4).setu(struct.coord2_14.coord.getTransferVector(2));
    return 0;
  }

  @Method(0x800df314L)
  public static long FUN_800df314(final long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();
    struct.us_74.set((int)MEMORY.ref(4, a0).offset(0x24L).deref(2).get());
    struct.us_76.set((int)MEMORY.ref(4, a0).offset(0x28L).deref(2).get());
    struct.us_78.set((int)MEMORY.ref(4, a0).offset(0x2cL).deref(2).get());
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800df3d0L)
  public static long FUN_800df3d0(final long a0) {
    MEMORY.ref(4, a0).offset(0x2cL).setu(MEMORY.ref(4, a0).offset(0x28L));
    MEMORY.ref(4, a0).offset(0x28L).setu(MEMORY.ref(4, a0).offset(0x24L));
    MEMORY.ref(4, a0).offset(0x24L).setu(MEMORY.ref(4, a0).offset(0x20L));
    MEMORY.ref(4, a0).offset(0x20L).setu(MEMORY.ref(4, a0).offset(0x4L).get() + 0x44L);
    return FUN_800e0018(a0);
  }

  @Method(0x800df410L)
  public static long FUN_800df410(final long a0) {
    MEMORY.ref(4, a0).offset(0x24L).setu(MEMORY.ref(4, a0).offset(0x20L));
    MEMORY.ref(4, a0).offset(0x20L).setu(MEMORY.ref(4, a0).offset(0x4L).get() + 0x44L);
    return FUN_800e0094(a0);
  }

  @Method(0x800df530L)
  public static long FUN_800df530(final long a0) {
    MEMORY.ref(4, a0).offset(0x24L).setu(MEMORY.ref(4, a0).offset(0x20L));
    MEMORY.ref(4, a0).offset(0x20L).setu(MEMORY.ref(4, a0).offset(0x4L).get() + 0x44L);
    return FUN_800e0184(a0);
  }

  @Method(0x800df650L)
  public static long FUN_800df650(final long a0) {
    MEMORY.ref(4, a0).offset(0x24L).setu(MEMORY.ref(4, a0).offset(0x20L));
    MEMORY.ref(4, a0).offset(0x20L).setu(MEMORY.ref(4, a0).offset(0x4L).get() + 0x44L);
    return FUN_800e02fc(a0);
  }

  @Method(0x800dfe0cL)
  public static long FUN_800dfe0c(final long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();

    final long s1 = MEMORY.ref(4, a0).offset(0x24L).deref(4).get();

    struct.us_12e.set((int)s1);
    struct.ub_9d.set((int)_800c6a50.offset(s1 * 0x4L).get());

    FUN_80020fe0(struct);
    FUN_800e0d18(struct, _800c6a00.offset(s1 * 0x4L).get(), struct.ui_124.get() + MEMORY.ref(4, struct.ui_124.get()).offset((s1 * 0x21L + 0x1L) * 0x8L).offset(0x8L).get());

    struct.us_12c.set(0);
    struct.ui_188.set(0);

    return 0;
  }

  @Method(0x800dfec8L)
  public static long FUN_800dfec8(long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();

    struct.us_132.set((int)MEMORY.ref(4, a0).offset(0x24L).deref(2).get());
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
  public static long FUN_800e0018(long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();
    final long v1 = (0xc01L - FUN_80040b90(MEMORY.ref(4, a0).offset(0x2cL).deref(4).get() - struct.coord2_14.coord.getTransferVector(2), MEMORY.ref(4, a0).offset(0x24L).deref(4).get() - struct.coord2_14.coord.getTransferVector(0))) & 0xfffL;
    struct.us_76.set((int)v1);
    struct.ui_188.set(0);
    return 0;
  }

  @Method(0x800e0094L)
  public static long FUN_800e0094(final long a0) {
    biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref().us_128.set((int)MEMORY.ref(4, a0).offset(0x24L).deref(2).get());
    return 0;
  }

  @Method(0x800e0184L)
  public static long FUN_800e0184(final long a0) {
    biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref().us_172.set((int)MEMORY.ref(4, a0).offset(0x24L).deref(2).get());
    return 0;
  }

  @Method(0x800e02fcL)
  public static long FUN_800e02fc(long a0) {
    final BigStruct struct1 = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();

    struct1.us_178.set((int)MEMORY.ref(4, a0).offset(0x24L).deref(2).get());

    if(MEMORY.ref(4, a0).offset(0x24L).deref(2).get() != 0) {
      //LAB_800e035c
      for(int i = 0; i < _800c6740.get(); i++) {
        final BigStruct struct2 = biggerStructPtrArr_800bc1c0.get((int)index_800c6880.offset(i * 0x4L).get()).deref().innerStruct_00.deref();

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
  public static long FUN_800e03e4(final long a0) {
    final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref();

    final long a0_0 = MEMORY.ref(4, a0).offset(0x24L).deref(4).get();

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
  public static long FUN_800e09e0(final long a0) {
    biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref().ub_cc.set(1);
    return 0;
  }

  @Method(0x800e0a14L)
  public static long FUN_800e0a14(final long a0) {
    biggerStructPtrArr_800bc1c0.get((int)MEMORY.ref(4, a0).offset(0x20L).deref(4).get()).deref().innerStruct_00.deref().ub_cc.set(0);
    return 0;
  }

  @Method(0x800e0d18L)
  public static void FUN_800e0d18(final BigStruct a0, final long a1, final long a2) {
    final int sp30 = a0.coord2_14.coord.getTransferVector(0);
    final int sp34 = a0.coord2_14.coord.getTransferVector(1);
    final int sp38 = a0.coord2_14.coord.getTransferVector(2);

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
    a0.ui_108.set(MEMORY.ref(4, a1).offset(0xcL).get(0xffff_0000L) >> 11);
    v0 = MEMORY.ref(4, a1).offset(0x8L).get();
    if(v0 != 0) {
      a0.ui_a8.set(a1 + v0 / 0x4L * 0x4L);

      //LAB_800e0eac
      for(int i = 0; i < 7; i++) {
        v1 = a0.ui_a8.get();
        v1 += MEMORY.ref(4, v1).offset(i * 0x4L).get() / 0x4L * 0x4L;
        a0.aub_d0.get(i).set(v1);
        FUN_8002246c(a0, i);
      }
    } else {
      //LAB_800e0ef0
      a0.ui_a8.set(a1 + 0x8L);

      //LAB_800e0f00
      for(int i = 0; i < 7; i++) {
        a0.aub_d0.get(i).set(0);
      }
    }

    //LAB_800e0f10
    a0.ui_8c.add(0x4L);

    FUN_8003c660(a0.ui_8c.get());
    FUN_80021b08(a0.ObjTable_0c, a0.dobj2ArrPtr_00.deref(), a0.coord2ArrPtr_04.deref(), a0.coord2ParamArrPtr_08.deref(), a0.s_c8.get());

    a0.ui_58.set(a0.v_64.getAddress());

    insertCoordinate2(null, a0.coord2_14);
    FUN_80021ca0(a0.ObjTable_0c, a0.ui_8c.get(), a0.coord2_14, a0.s_c8.get(), (short)(a0.us_ca.get() + 1));

    a0.ub_a2.set(0);
    a0.ub_a3.set(0);
    a0.ui_f4.set(0);
    a0.ui_f8.set(0);
    a0.us_a0.set(0);

    FUN_80021584(a0, a2);

    a0.coord2_14.coord.setTransferVector(0, sp30);
    a0.coord2_14.coord.setTransferVector(1, sp34);
    a0.coord2_14.coord.setTransferVector(2, sp38);

    a0.ui_fc.set(0x1000L);
    a0.ui_100.set(0x1000L);
    a0.ui_104.set(0x1000L);
  }

  @Method(0x800e0ff0L)
  public static void FUN_800e0ff0(final long a0, final long a1) {
    assert false;
  }

  @Method(0x800e123cL)
  public static void FUN_800e123c(final long a0, final long a1) {
    assert false;
  }

  @Method(0x800e13b0L)
  public static void FUN_800e13b0(long a0) {
    long v0;
    long v1;
    long s1;
    long s2;
    long s3;
    long s5;
    long a1;
    long a2;
    long a3;
    long t0;

    long s8 = a0;
    switch((int)loadingStage_800c68e4.get()) {
      case 0x0:
        FUN_800e3cc8(_80010544.getAddress());

        if(_80050274.get() != _80052c30.get()) {
          _800bda08.setu(_80050274);
          _80050274.setu(_80052c30);
        }

        //LAB_800e1440
        _800c66d8.setu(0);
        _800c66dc.setu(0x1000L);
        _800c66e0.setu(0);
        _800c66e4.setu(0x80L);
        _800c66e5.setu(0x80L);
        _800c66e6.setu(0x80L);
        FUN_8003c6f0(0, _800c66d8.getAddress());
        _800c66e8.setu(0);
        _800c66ec.setu(0x1000L);
        _800c66f0.setu(0);
        _800c66f4.setu(0);
        _800c66f5.setu(0);
        _800c66f6.setu(0);
        FUN_8003c6f0(0x1L, _800c66e8.getAddress());
        _800c66f8.setu(0);
        _800c66fc.setu(0x1000L);
        _800c6700.setu(0);
        _800c6704.setu(0);
        _800c6705.setu(0);
        _800c6706.setu(0);
        FUN_8003c6f0(0x2L, _800c66f8.getAddress());
        FUN_8003cce0(0x800L, 0x800L, 0x800L);
        loadingStage_800c68e4.addu(0x1L);
        break;

      case 0x1:
        if(s8 == -0x1L) {
          break;
        }

        v0 = _800d610c.offset(_80052c30.get() * 0x2L).get();
        v1 = _800bd808.get();
        _800bd808.setu(v0);

        //LAB_800e15b8
        //LAB_800e15ac
        //LAB_800e15b8
        //LAB_800e15b8
        if(v0 != v1) {
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
        a0 = FUN_8001c60c();

        if(a0 == -0x1L) {
          FUN_8001ae90();

          //LAB_800e15b8
          _800bd782.setu(0x1L);
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        if(a0 == -0x2L) {
          FUN_8001ada0();

          //LAB_800e15b8
          _800bd782.setu(0x1L);
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        if(a0 == -0x3L) {
          //LAB_800e15b8
          _800bd782.setu(0x1L);
          loadingStage_800c68e4.addu(0x1L);
          break;
        }

        //LAB_800e15c0
        FUN_8001f3d0(a0, 0);
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
        _800c68d0.setu(0);
        _800c6874.setu(0);
        mrgAddr_800c68d8.setu(0);
        mrgAddr_800c6878.setu(0);

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
        if(_800c6874.get() == 0x1L && _800c68d0.get() == 0x1L) {
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

        v0 = mrgAddr_800c68d8.deref(4).offset(0x4L).get();
        a0 = mrgAddr_800c68d8.get() + (v0 - 0x1L) * 0x8L;
        a2 = v0 - 0x2L;

        _800c672c.setu(a2);
        _800c6730.setu(a2);

        long s4;
        if(MEMORY.ref(4, a0).offset(0xcL).get() != 0x4L) {
          v0 = MEMORY.ref(4, a0).offset(0x8L).get() + mrgAddr_800c68d8.get();
          s3 = MEMORY.ref(4, v0).get();
          s4 = MEMORY.ref(4, v0).offset(0x4L).get();
        } else {
          s3 = 0;
          s4 = 0;
        }

        //LAB_800e1914
        _800c69f0.setu(a2 * 0x22L);
        _800c69f4.setu(a2 * 0x22L + 0x1L);
        _800c69f8.setu(a2 * 0x22L + 0x2L);

        FUN_800e3cc8(mrgAddr_800c6878.get() + mrgAddr_800c6878.deref(4).offset(a2 * 0x110L).offset(0x8L).get());
        FUN_800e3cc8(mrgAddr_800c6878.get() + mrgAddr_800c6878.deref(4).offset(_800c69f4.get() * 0x8L).offset(0x8L).get());

        final TimHeader sp20 = parseTimHeader(mrgAddr_800c6878.deref(4).offset(mrgAddr_800c6878.deref(4).offset(_800c69f8.get() * 0x8L).offset(0x8L)).offset(0x4L));
        final RECT sp40 = new RECT();
        sp40.x.set(sp20.imageRect.x);
        sp40.y.set(sp20.imageRect.y);
        sp40.w.set(sp20.imageRect.w);
        sp40.h.set((short)0x80);

        LoadImage(sp40, sp20.imageAddress.get());
        DrawSync(0);

        a0 = FUN_80015918(0, 0, 0, 0, 0);
        _800c6740.setu(a0);
        FUN_80015b98(a0, mrgAddr_800c68d8.deref(4).offset(mrgAddr_800c68d8.deref(4).offset(0x8L)).getAddress());

        //LAB_800e1a38
        for(int i = 0; i < _800c6730.get(); i++) {
          _800c6a00.offset(i * 0x4L).setu(mrgAddr_800c6878.deref(4).offset(i * 0x108L).offset(0x8L).get() + mrgAddr_800c6878.get());
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
        t0 = _800c6a00.getAddress();

        //LAB_800e1abc
        for(int i = 0; i < _800c6730.get(); i++) {
          a3 = MEMORY.ref(4, t0).get();
          a0 = _800c6a50.getAddress() + (i + 0x1L) * 0x4L;
          v1 = _800c6a00.getAddress() + (i + 0x1L) * 0x4L;

          //LAB_800e1ae0
          for(a1 = i + 0x1L; a1 < _800c6730.get(); a1++) {
            if(a3 == MEMORY.ref(4, v1).get()) {
              MEMORY.ref(4, a0).setu(0x80L);
            }

            //LAB_800e1af4
            a0 += 0x4L;
            v1 += 0x4L;
          }

          //LAB_800e1b0c
          t0 += 0x4L;
        }

        //LAB_800e1b20
        s3 = 0;
        s5 = 0x8L;
        s2 = index_800c6880.getAddress();

        //LAB_800e1b54
        for(int i = 0; i < _800c6730.get(); i++) {
          a0 = FUN_800158cc(0x210L);
          MEMORY.ref(4, s2).setu(a0);
          FUN_80015a68(a0, getMethodAddress(SMap.class, "FUN_800e0ff0", long.class, long.class));
          FUN_80015ab4(MEMORY.ref(4, s2).get(), getMethodAddress(SMap.class, "FUN_800e123c", long.class, long.class));
          FUN_80015b00(MEMORY.ref(4, s2).get(), getMethodAddress(SMap.class, "FUN_800e3df4", long.class, long.class));
          FUN_80015b98(MEMORY.ref(4, s2).get(), mrgAddr_800c68d8.deref(4).offset((i + 0x1L) * 0x8L).offset(0x8L).get() + mrgAddr_800c68d8.get());

          final BigStruct struct = biggerStructPtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.deref();
          struct.ub_9d.set((int)_800c6a50.offset(1, s3).get());

          FUN_80020a00(struct, _800c6a00.offset(s3).get(), mrgAddr_800c6878.deref(4).offset(s5).offset(0x8L).get() + mrgAddr_800c6878.get());

          if(i == 0) {
            FUN_800e0d18(bigStruct_800c6748, _800c6a00.get(), mrgAddr_800c6878.get() + mrgAddr_800c6878.deref(4).offset(0x10L).get());
            bigStruct_800c6748.coord2_14.coord.setTransferVector(0, 0);
            bigStruct_800c6748.coord2_14.coord.setTransferVector(1, 0);
            bigStruct_800c6748.coord2_14.coord.setTransferVector(2, 0);
            bigStruct_800c6748.us_74.set(0);
            bigStruct_800c6748.us_76.set(0);
            bigStruct_800c6748.us_78.set(0);
          }

          //LAB_800e1c50
          struct.ui_124.set(mrgAddr_800c6878.get());
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
          v1 = _800bd818.offset((s3 + i) * 0x4L).getAddress();

          struct.coord2_14.coord.setTransferVector(0, (int)MEMORY.ref(4, v1).offset(0x00L).get());
          struct.coord2_14.coord.setTransferVector(1, (int)MEMORY.ref(4, v1).offset(0x04L).get());
          struct.coord2_14.coord.setTransferVector(2, (int)MEMORY.ref(4, v1).offset(0x08L).get());

          struct.us_74.set((int)MEMORY.ref(2, v1).offset(0x0cL).get());
          struct.us_76.set((int)MEMORY.ref(2, v1).offset(0x0eL).get());
          struct.us_78.set((int)MEMORY.ref(2, v1).offset(0x10L).get());

          struct.ui_18c.set(0x7L);
          struct.ui_190.set(0);

          if(i == 0) {
            struct.ui_190.set(0x1L);
            FUN_800e4d88(s8, struct);
          }

          //LAB_800e1d60
          FUN_800f04ac(struct.v_1d0.getAddress());

          s3 += 0x4L;
          s5 += 0x108L;
          s2 += 0x4L;
        }

        //LAB_800e1d88
        _800c6708.setu(0);
        _800c670a.setu(0);
        _800c670c.setu(0);
        _800c670e.setu(0);
        mrgAddr_800c6710.setu(0);
        _800c6714.setu(0);
        _800c6718.setu(0);
        _800c671c.setu(0);
        _800c6720.setu(0);

        _800c6738.setu(0);
        _800c673c.setu(0x3cL);

        _800c686e.setu(0);
        _800c687c.setu(0);
        _800c687e.setu(0);
        _800c68e0.setu(0);
        loadingStage_800c68e4.addu(0x1L);

        _800c6734.setu(addToLinkedListTail(0x28L));
        _800c69fc.setu(addToLinkedListTail(0x140L));

        _800c6aa0.setu(_800bd7e8.get() - _800bd7f4.get());
        _800c6aa4.setu(_800bd7ec.get() - _800bd7f8.get());
        _800c6aa8.setu(_800bd7f0.get() - _800bd7fc.get());

        FUN_800e3cc8(_800d689c.getAddress());
        FUN_800f3af8();

        s1 = 0x9L;
        v0 = _800c6734.get() + 0x24L;

        //LAB_800e1ea8
        do {
          MEMORY.ref(4, v0).setu(0);
          s1--;
          v0 -= 0x4L;
        } while(s1 >= 0);

        s1 = 0x1fL;
        v0 = _800c69ec.getAddress();

        //LAB_800e1ecc
        do {
          MEMORY.ref(4, v0).setu(-0x1L);
          s1--;
          v0 -= 0x4L;
        } while(s1 >= 0);

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
  public static void FUN_800e3cc8(final long a0) {
    final TimHeader header = parseTimHeader(MEMORY.ref(4, a0).offset(0x4L));
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
        _800c6874.setu(0x1);
        mrgAddr_800c6878.setu(fileAddress);
      }

      case 0x1 -> {
        _800c68d0.setu(0x1);
        mrgAddr_800c68d8.setu(fileAddress);
      }

      case 0x10 -> {
        _800c68e0.setu(0x1);
        mrgAddr_800c6710.setu(fileAddress);
      }
    }
  }

  @Method(0x800e3df4L)
  public static void FUN_800e3df4(final long a0, final long a1) {
    assert false;
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

  @Method(0x800e4708L)
  public static void FUN_800e4708() {
    assert false;
    //TODO
  }

  @Method(0x800e4ac8L)
  public static void FUN_800e4ac8() {
    final long v0 = _800f64c6.offset(_80052c30.get() * 0x4L).get();
    _800bed58.setu(v0 < 0x1L ? 0x1L : 0);
  }

  @Method(0x800e4b20L)
  public static long FUN_800e4b20() {
    assert false;
    return 0;
  }

  @Method(0x800e4d00L)
  public static void FUN_800e4d00(final long a0, final long a1) {
    if(FUN_800e5264(_800c6ac0.getAddress(), a0) == 0) {
      //LAB_800e4d34
      final long[] sp10 = new long[3]; // shorts
      FUN_800e92dc(a1, sp10);
      _800c6ac0.setTransferVector(0, (int)sp10[0]);
      _800c6ac0.setTransferVector(1, (int)sp10[1]);
      _800c6ac0.setTransferVector(2, (int)sp10[2]);
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
        a1.coord2_14.coord.setTransferVector(0, _800c6ac0.getTransferVector(0));
        a1.coord2_14.coord.setTransferVector(1, _800c6ac0.getTransferVector(1));
        a1.coord2_14.coord.setTransferVector(2, _800c6ac0.getTransferVector(2));
      }

      //LAB_800e4e18
      _800f7e24.setu(0);
    } else {
      //LAB_800e4e20
      final long[] sp10 = new long[3];
      FUN_800e92dc(a1.coord2_14.coord.getAddress() + 0x18L, sp10);
      a1.coord2_14.coord.setTransferVector(0, (short)sp10[0]);
      a1.coord2_14.coord.setTransferVector(1, (short)sp10[1]);
      a1.coord2_14.coord.setTransferVector(2, (short)sp10[2]);
    }

    //LAB_800e4e4c
  }

  @Method(0x800e4e5cL)
  public static void FUN_800e4e5c() {
    assert false;
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
    assert false;
  }

  @Method(0x800e5084L)
  public static long FUN_800e5084(long a0, long a1, long a2) {
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
  public static void FUN_800e5104(long a0, long a1) {
    FUN_800e13b0(a0);

    MEMORY.ref(4, a1).offset(0x48L).deref(4).call(a1);

    _800f64c0.setu(_8005a370.offset(doubleBufferFrame_800bb108.get() * 0x14L).getAddress());
    _800c6ae0.addu(0x1L);

    if(_800bb0ab.get() != 0) {
      _800c6ae4.setu(-0x1eL);
    }

    //LAB_800e5184
    FUN_800e4ff4();
  }

  @Method(0x800e519cL)
  public static void FUN_800e519c() {
    assert false;
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

    long s0 = _800cb430.getAddress();
    if(_800cb440.get() == 0) {
      v1 = _800cab20.get();
      _800cab20.subu(0x1L);
      if((int)v1 > 0) {
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
            biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.deref().ui_16c.set(_800caaf8.get());
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
          biggerStructPtrArr_800bc1c0.get((int)index_800c6880.get()).deref().innerStruct_00.deref().us_12a.set(1);
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
    final RECT[] rects = new RECT[0x20];
    for(int i = 0; i < rects.length; i++) {
      rects[i] = rectArr_800cb460.get(i);
    }

    fillRects(rects, 0, 0, 0x100L);
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
    assert false;
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
    FUN_8003cfb0(_800cbd10.getAddress());
    FUN_800e728c(matrix_800c3548);
    _800cbd68.set(matrix_800c3548);
    FUN_8003fab0(_800cbd68, _800cbd40);
    _800bd7e8.setu(_800cbd10);
    _800bd7ec.setu(_800cbd14);
    _800bd7f0.setu(_800cbd18);
    _800bd7f4.setu(_800cbd1c);
    _800bd7f8.setu(_800cbd20);
    _800bd7fc.setu(_800cbd24);
    _800bd800.setu(_800cbd28);
    _800bd804.setu(_800cbd2c);
  }

  @Method(0x800e7418L)
  public static void FUN_800e7418(long a0, long a1, long a2, long a3, long a4, long a5) {
    _800cbd10.setu(a0 & 0xffffL);
    _800cbd14.setu(a0 >>> 16);
    _800cbd18.setu(a1 & 0xffffL);
    _800cbd1c.setu(a2 & 0xffffL);
    _800cbd20.setu(a2 >>> 16);
    _800cbd24.setu(a3 & 0xffffL);
    _800cbd28.setu((int)(a4 << 16) >> 4);
    _800cbd2c.setu(0);
    projectionPlaneDistance_800bd810.setu(a5 & 0xffffL);

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

    final RECT[] rects = new RECT[0x30];
    for(int i = 0; i < rects.length; i++) {
      rects[i] = new RECT(_800cb590.offset(i * 0x8L));
    }

    fillRects(rects, 0, 0, 0x180L);
  }

  @Method(0x800e7604L)
  public static void FUN_800e7604(final int x, final int y) {
    if(_800cbd3c.deref(4).get() == 0) {
      _800cbd3c.deref(4).setu(0x1L);
      setScreenOffset(x, y);
    }
  }

  @Method(0x800e7650L)
  public static void FUN_800e7650(long x, long y) {
    if(_800cbd38.deref(4).get() == 0) {
      _800cbd38.deref(4).setu(0x1L);
      screenOffsetX_800cb568.setu(x);
      screenOffsetY_800cb56c.setu(y);
    }
  }

  @Method(0x800e770cL)
  public static void FUN_800e770c() {
    _800cbd60.setu(0);
    _800cbd64.setu(_800c6730);
  }

  @Method(0x800e7f00L)
  public static void FUN_800e7f00(final UnsignedIntRef a0, final UnsignedIntRef a1, final long[] a2) {
    final long[] sp18 = new long[2];
    FUN_8003fa40(a2, sp18, new long[1], new long[1], new long[1], 1);
    a0.set(sp18[0]);
    a1.set(sp18[1]);
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
  public static void FUN_800e8104(long a0, long a1) {
    if(_800cbd38.deref(4).get() == 0) {
      _800cbd38.deref(4).setu(0x1L);

      final UnsignedIntRef sp10 = new UnsignedIntRef();
      final UnsignedIntRef sp14 = new UnsignedIntRef();
      final long[] sp28 = {a0, a1};
      FUN_800e7f00(sp10, sp14, sp28);
      FUN_800e7f68(sp10.get(), sp14.get());
    }

    //LAB_800e8164
    FUN_800e7650(screenOffsetX_800cb568.get(), screenOffsetY_800cb56c.get());
    FUN_800e7604((int)screenOffsetX_800cb568.get(), (int)screenOffsetY_800cb56c.get());
  }

  @Method(0x800e81a0L)
  public static void FUN_800e81a0(long a0) {
    final long s0_0 = addToLinkedListTail(0x8L);
    fillRects(new RECT[] {new RECT(MEMORY.ref(4, s0_0))}, 0, 0, 0x8L);
    FUN_800e5084(_800e4f74.getAddress(), s0_0, 0);
    _800cbd38.setu(s0_0);

    final long s0_1 = addToLinkedListTail(0x8L);
    fillRects(new RECT[] {new RECT(MEMORY.ref(4, s0_1))}, 0, 0, 0x8L);
    FUN_800e5084(_800e4f74.getAddress(), s0_1, 0);
    _800cbd3c.setu(s0_1);

    final long[] sp10 = new long[3];
    FUN_800e92dc(a0, sp10);
    //TODO sp10[3], but I think it's a bug. The method only sets 3 elements.
    FUN_800e8104(sp10[1] << 16 | sp10[0], 0 << 16 | sp10[2]);
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
  public static long FUN_800e8c20(final long a0) {
    FUN_8003c660(a0 + 0x4L);
    return a0 + 0xcL;
  }

  @Method(0x800e8c50L)
  public static void FUN_800e8c50(final GsDOBJ2 dobj2, final long a1, final long src, final int size) {
    final long dest = _800cfa78.getAddress();
    memcpy(dest, src, size);
    MEMORY.ref(4, a1).offset(0x1cL).setu(dest);
    final long v0 = FUN_800e8c20(dest);
    FUN_8003e5d0(v0, dobj2, 0);
    MEMORY.ref(4, a1).setu(v0);
    FUN_800e8bd8(a1);
  }

  @Method(0x800e8cd0L)
  public static void FUN_800e8cd0(final long src, final int size, final long a2, final long a3) {
    _800d1a88.setu(_800cbe08.getAddress());
    GsCOORDINATE2Ptr_800cbe2c.set(GsCOORDINATE2_800cbda8);
    _800cbe28.setu(_800cbdf8.getAddress());

    insertCoordinate2(null, GsCOORDINATE2Ptr_800cbe2c.deref());

    //TODO figure out this struct
    _800cbe28.deref(4).offset(0x4L).setu(GsCOORDINATE2Ptr_800cbe2c.getPointer());
    _800d1a88.deref(4).offset(0x20L).deref(4).setu(GsCOORDINATE2Ptr_800cbe2c.getPointer());

    FUN_800e8c50(_800d1a88.deref(4).offset(0x20L).deref(4).cast(GsDOBJ2::new), _800d1a88.get(), src, size);
    FUN_800e8b40(_800d1a88.get(), a2);

    _800f7f10.setu(0);
    _800f7f14.setu(0x1L);

    long s0 = addToLinkedListTail(0x8L);
    fillRects(new RECT[] {new RECT(MEMORY.ref(8, s0))}, 0, 0, 0x8L);
    FUN_800e5084(_800e4f74.getAddress(), s0, 0);
    _800cbe34.setu(s0);

    s0 = addToLinkedListTail(0x8L);
    fillRects(new RECT[] {new RECT(MEMORY.ref(8, s0))}, 0, 0, 0x8L);
    FUN_800e5084(_800e4f74.getAddress(), s0, 0);
    _800d1a8c.setu(s0);

    s0 = addToLinkedListTail(0x8L);
    fillRects(new RECT[] {new RECT(MEMORY.ref(8, s0))}, 0, 0, 0x8L);
    FUN_800e5084(_800e4f74.getAddress(), s0, 0);
    _800cbe38.setu(s0);

    FUN_800e866c();
  }

  @Method(0x800e8e50L)
  public static void FUN_800e8e50() {
    assert false;
  }

  /**
   * @param a1 an array of shorts
   */
  @Method(0x800e92dcL)
  public static long FUN_800e92dc(final long a0, final long[] a1) {
    a1[0] = 0;
    a1[1] = 0;
    a1[2] = 0;

    if(_800f7f14.get() == 0 || (int)a0 < 0 || a0 >= _800d1a88.deref(4).offset(0xcL).get()) {
      //LAB_800e9318
      return 0;
    }

    //LAB_800e932c
    final long v0 = _800d1a88.deref(4).offset(0x14L).get() + a0 * 0xcL;
    final long t0 = MEMORY.ref(4, v0).offset(0x4L).get() + _800d1a88.deref(4).offset(0x10L).get() + 0x6L;
    final long a3 = MEMORY.ref(1, v0).get();

    //LAB_800e937c
    for(int i = 0; i < a3; i++) {
      final long a0_1 = _800d1a88.deref(4).offset(0x4L).get() + MEMORY.ref(2, t0).offset(i * 0x2L).get() * 0x8L;
      a1[0] += MEMORY.ref(2, a0_1).offset(0x0L).get();
      a1[1] += MEMORY.ref(2, a0_1).offset(0x2L).get();
      a1[2] += MEMORY.ref(2, a0_1).offset(0x4L).get();
    }

    //LAB_800e93e0
    a1[0] /= a3;
    a1[1] /= a3;
    a1[2] /= a3;
    return 0x1L;
  }

  @Method(0x800ea974L)
  public static long FUN_800ea974(final long a0) {
    if((int)a0 < 0) {
      _800d1ad8.setu(_800ea96c.getAddress());
    } else {
      //LAB_800ea9a4
      final RECT[] rects = new RECT[8];
      for(int i = 0; i < rects.length; i++) {
        rects[i] = _800d1a90.get(i);
      }
      fillRects(rects, 0, 0, 0x40L); // 4c...? That'd be 9.5 rects. Seems there are 8 rects. I'm updating it.

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
        _800d1ad8.setu(_800ea84c.getAddress());
        _800d1ad4.setu(0x1L);
      } else {
        //LAB_800eaa5c
        _800d1ad8.setu(_800ea90c.getAddress());
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
      _800f9e5a.setu(_800c6870.get());
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
          FUN_80015310(0, _800f982c.offset(_80052c30.get() * 0x2L).getSigned(), 0, _800eeddc.getAddress(), 0, 0x2L);
          FUN_80015310(0, _800f982c.offset(_80052c30.get() * 0x2L).getSigned() + 0x1L, 0, _800eeddc.getAddress(), 0x1L, 0x4L);

          if(_80052c30.get() == 0x2a1L) {
            FUN_80015310(0, 0x1dbaL, 0, _800eeddc.getAddress(), 0x2L, 0x4L);
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

  @Method(0x800ee9e0L)
  public static void FUN_800ee9e0(final RECT a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
  }

  @Method(0x800eece0L)
  public static void FUN_800eece0(final long a0) {
    assert false;
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

  @Method(0x800f3a48L)
  public static void FUN_800f3a48() {
    FUN_800f3c98(_800d6d1c.offset(_800d6d24).getAddress(), _800d5588.getAddress());
    FUN_800f3c98(_800d6d1c.offset(_800d6d2c).getAddress(), _800d5590.getAddress());
    FUN_800f3b64(_800d6d1c.offset(_800d6d24).getAddress(), _800d6d1c.offset(_800d6d1c.offset(0x10L)).getAddress(), _800d4ff0.getAddress(), 0x15L);
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
      insertElementIntoLinkedList(_1f8003d0.get() + 0x4L, linkedListAddress_1f8003d8.get());
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
