package legend.game;

import legend.core.DebugHelper;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.FileLoadingInfo;
import legend.core.cdrom.SyncCode;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.BigStruct;
import legend.game.types.BiggerStruct;
import legend.game.types.GsOT_TAG;
import legend.game.types.HmdSomethingStruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.MEMORY;
import static legend.core.Hardware.SPU;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SInit.FUN_800fbec8;
import static legend.game.Scus94491.decompress;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020008;
import static legend.game.Scus94491BpeSegment_8002.FUN_800201c8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020360;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020ed8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022518;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a058;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a0e4;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ae0c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c0c8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002c86c;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8003.ClearImage;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_80036674;
import static legend.game.Scus94491BpeSegment_8003.FUN_80036f20;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b0d0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003bc30;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c048;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c350;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003c5e0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003cd40;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.SetDispMask;
import static legend.game.Scus94491BpeSegment_8003.VSync;
import static legend.game.Scus94491BpeSegment_8003.beginCdromTransfer;
import static legend.game.Scus94491BpeSegment_8003.drawOTag;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.resetDmaTransfer;
import static legend.game.Scus94491BpeSegment_8003.setClip;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_80045cb8;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004b834;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004be7c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004bea4;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c114;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c1f8;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c390;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c3f0;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c494;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c558;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c8dc;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cb0c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004ccb0;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cf8c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d41c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d4b4;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d4f8;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d52c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d648;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d6a8;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d91c;
import static legend.game.Scus94491BpeSegment_8004._8004db88;
import static legend.game.Scus94491BpeSegment_8004._8004dbc4;
import static legend.game.Scus94491BpeSegment_8004._8004dbc8;
import static legend.game.Scus94491BpeSegment_8004._8004dbcd;
import static legend.game.Scus94491BpeSegment_8004._8004dd04;
import static legend.game.Scus94491BpeSegment_8004._8004dd08;
import static legend.game.Scus94491BpeSegment_8004._8004dd0c;
import static legend.game.Scus94491BpeSegment_8004._8004dd10;
import static legend.game.Scus94491BpeSegment_8004._8004dd14;
import static legend.game.Scus94491BpeSegment_8004._8004dd18;
import static legend.game.Scus94491BpeSegment_8004._8004dd1c;
import static legend.game.Scus94491BpeSegment_8004._8004dd1e;
import static legend.game.Scus94491BpeSegment_8004._8004dd20;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004dd28;
import static legend.game.Scus94491BpeSegment_8004._8004dd34;
import static legend.game.Scus94491BpeSegment_8004._8004dd36;
import static legend.game.Scus94491BpeSegment_8004._8004dd38;
import static legend.game.Scus94491BpeSegment_8004._8004dd44;
import static legend.game.Scus94491BpeSegment_8004._8004dd48;
import static legend.game.Scus94491BpeSegment_8004._8004dda0;
import static legend.game.Scus94491BpeSegment_8004._8004ddcc;
import static legend.game.Scus94491BpeSegment_8004._8004ddd0;
import static legend.game.Scus94491BpeSegment_8004._8004ddd4;
import static legend.game.Scus94491BpeSegment_8004._8004ddd8;
import static legend.game.Scus94491BpeSegment_8004._8004de58;
import static legend.game.Scus94491BpeSegment_8004._8004e098;
import static legend.game.Scus94491BpeSegment_8004._8004e29c;
import static legend.game.Scus94491BpeSegment_8004._8004f65c;
import static legend.game.Scus94491BpeSegment_8004._8004f664;
import static legend.game.Scus94491BpeSegment_8004._8004f6a4;
import static legend.game.Scus94491BpeSegment_8004._8004f6e4;
import static legend.game.Scus94491BpeSegment_8004._8004f6e8;
import static legend.game.Scus94491BpeSegment_8004._8004f6ec;
import static legend.game.Scus94491BpeSegment_8004._8004fa98;
import static legend.game.Scus94491BpeSegment_8004._8004fa9a;
import static legend.game.Scus94491BpeSegment_8004._8004fa9c;
import static legend.game.Scus94491BpeSegment_8004._8004fb00;
import static legend.game.Scus94491BpeSegment_8004._8004fb02;
import static legend.game.Scus94491BpeSegment_8004._8004fb04;
import static legend.game.Scus94491BpeSegment_8004._8004ff10;
import static legend.game.Scus94491BpeSegment_8004._8004ff14;
import static legend.game.Scus94491BpeSegment_8004.callbackArray_8004dddc;
import static legend.game.Scus94491BpeSegment_8004.callbackIndex_8004ddc4;
import static legend.game.Scus94491BpeSegment_8004.callback_8004dbc0;
import static legend.game.Scus94491BpeSegment_8004.callback_8004dd3c;
import static legend.game.Scus94491BpeSegment_8004.callback_8004dd40;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.fileNamePtr_8004dda4;
import static legend.game.Scus94491BpeSegment_8004.index_8004de4c;
import static legend.game.Scus94491BpeSegment_8004.setCdVolume;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8005._80050068;
import static legend.game.Scus94491BpeSegment_8005._800500e8;
import static legend.game.Scus94491BpeSegment_8005._800500f8;
import static legend.game.Scus94491BpeSegment_8005._80052c30;
import static legend.game.Scus94491BpeSegment_8005._80054d0c;
import static legend.game.Scus94491BpeSegment_8005._8005a1e0;
import static legend.game.Scus94491BpeSegment_8005._8005a1e4;
import static legend.game.Scus94491BpeSegment_8005._8005a1ea;
import static legend.game.Scus94491BpeSegment_8005._8005a2a8;
import static legend.game.Scus94491BpeSegment_8005._8005a2ac;
import static legend.game.Scus94491BpeSegment_8005._8005a2b0;
import static legend.game.Scus94491BpeSegment_8005._8005a398;
import static legend.game.Scus94491BpeSegment_8005.linkedListHead_8005a2a0;
import static legend.game.Scus94491BpeSegment_8005.linkedListTail_8005a2a4;
import static legend.game.Scus94491BpeSegment_8005.orderingTables_8005a370;
import static legend.game.Scus94491BpeSegment_8007._8007a398;
import static legend.game.Scus94491BpeSegment_8007._8007a39c;
import static legend.game.Scus94491BpeSegment_8007._8007a3a0;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007._8007a3ac;
import static legend.game.Scus94491BpeSegment_8007._8007a3b8;
import static legend.game.Scus94491BpeSegment_8007._8007a3c0;
import static legend.game.Scus94491BpeSegment_8009._8009a7c0;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bb4c8;
import static legend.game.Scus94491BpeSegment_800b.SInitBinLoaded_800bbad0;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bac68;
import static legend.game.Scus94491BpeSegment_800b._800bac84;
import static legend.game.Scus94491BpeSegment_800b._800bad04;
import static legend.game.Scus94491BpeSegment_800b._800bad64;
import static legend.game.Scus94491BpeSegment_800b._800bad6c;
import static legend.game.Scus94491BpeSegment_800b._800baf02;
import static legend.game.Scus94491BpeSegment_800b._800baf07;
import static legend.game.Scus94491BpeSegment_800b._800bb0ab;
import static legend.game.Scus94491BpeSegment_800b._800bb0ac;
import static legend.game.Scus94491BpeSegment_800b._800bb0f8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb114;
import static legend.game.Scus94491BpeSegment_800b._800bb118;
import static legend.game.Scus94491BpeSegment_800b._800bb120;
import static legend.game.Scus94491BpeSegment_800b._800bb140;
import static legend.game.Scus94491BpeSegment_800b._800bb144;
import static legend.game.Scus94491BpeSegment_800b._800bb148;
import static legend.game.Scus94491BpeSegment_800b._800bb14c;
import static legend.game.Scus94491BpeSegment_800b._800bb150;
import static legend.game.Scus94491BpeSegment_800b._800bb154;
import static legend.game.Scus94491BpeSegment_800b._800bb158;
import static legend.game.Scus94491BpeSegment_800b._800bb15c;
import static legend.game.Scus94491BpeSegment_800b._800bb160;
import static legend.game.Scus94491BpeSegment_800b._800bb164;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bb228;
import static legend.game.Scus94491BpeSegment_800b._800bb348;
import static legend.game.Scus94491BpeSegment_800b._800bc060;
import static legend.game.Scus94491BpeSegment_800b._800bc070;
import static legend.game.Scus94491BpeSegment_800b._800bc0b8;
import static legend.game.Scus94491BpeSegment_800b._800bc0b9;
import static legend.game.Scus94491BpeSegment_800b._800bc300;
import static legend.game.Scus94491BpeSegment_800b._800bc304;
import static legend.game.Scus94491BpeSegment_800b._800bc308;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc981;
import static legend.game.Scus94491BpeSegment_800b._800bc988;
import static legend.game.Scus94491BpeSegment_800b._800bc9a0;
import static legend.game.Scus94491BpeSegment_800b._800bc9aa;
import static legend.game.Scus94491BpeSegment_800b._800bc9ab;
import static legend.game.Scus94491BpeSegment_800b._800bc9ac;
import static legend.game.Scus94491BpeSegment_800b._800bca68;
import static legend.game.Scus94491BpeSegment_800b._800bca6c;
import static legend.game.Scus94491BpeSegment_800b._800bca78;
import static legend.game.Scus94491BpeSegment_800b._800bcf64;
import static legend.game.Scus94491BpeSegment_800b._800bcf78;
import static legend.game.Scus94491BpeSegment_800b._800bcf80;
import static legend.game.Scus94491BpeSegment_800b._800bcf88;
import static legend.game.Scus94491BpeSegment_800b._800bcf90;
import static legend.game.Scus94491BpeSegment_800b._800bcff0;
import static legend.game.Scus94491BpeSegment_800b._800bcff4;
import static legend.game.Scus94491BpeSegment_800b._800bd000;
import static legend.game.Scus94491BpeSegment_800b._800bd060;
import static legend.game.Scus94491BpeSegment_800b._800bd064;
import static legend.game.Scus94491BpeSegment_800b._800bd070;
import static legend.game.Scus94491BpeSegment_800b._800bd07c;
import static legend.game.Scus94491BpeSegment_800b._800bd080;
import static legend.game.Scus94491BpeSegment_800b._800bd08c;
import static legend.game.Scus94491BpeSegment_800b._800bd098;
import static legend.game.Scus94491BpeSegment_800b._800bd09c;
import static legend.game.Scus94491BpeSegment_800b._800bd0a8;
import static legend.game.Scus94491BpeSegment_800b._800bd0b4;
import static legend.game.Scus94491BpeSegment_800b._800bd0b6;
import static legend.game.Scus94491BpeSegment_800b._800bd0b8;
import static legend.game.Scus94491BpeSegment_800b._800bd0c4;
import static legend.game.Scus94491BpeSegment_800b._800bd0c8;
import static legend.game.Scus94491BpeSegment_800b._800bd0cc;
import static legend.game.Scus94491BpeSegment_800b._800bd0d0;
import static legend.game.Scus94491BpeSegment_800b._800bd0d4;
import static legend.game.Scus94491BpeSegment_800b._800bd0e0;
import static legend.game.Scus94491BpeSegment_800b._800bd0f0;
import static legend.game.Scus94491BpeSegment_800b._800bd0f8;
import static legend.game.Scus94491BpeSegment_800b._800bd0fc;
import static legend.game.Scus94491BpeSegment_800b._800bd100;
import static legend.game.Scus94491BpeSegment_800b._800bd104;
import static legend.game.Scus94491BpeSegment_800b._800bd108;
import static legend.game.Scus94491BpeSegment_800b._800bd110;
import static legend.game.Scus94491BpeSegment_800b._800bd114;
import static legend.game.Scus94491BpeSegment_800b._800bd118;
import static legend.game.Scus94491BpeSegment_800b._800bd11c;
import static legend.game.Scus94491BpeSegment_800b._800bd120;
import static legend.game.Scus94491BpeSegment_800b._800bd122;
import static legend.game.Scus94491BpeSegment_800b._800bd124;
import static legend.game.Scus94491BpeSegment_800b._800bd126;
import static legend.game.Scus94491BpeSegment_800b._800bd128;
import static legend.game.Scus94491BpeSegment_800b._800bd12a;
import static legend.game.Scus94491BpeSegment_800b._800bd12c;
import static legend.game.Scus94491BpeSegment_800b._800bd600;
import static legend.game.Scus94491BpeSegment_800b._800bd680;
import static legend.game.Scus94491BpeSegment_800b._800bd684;
import static legend.game.Scus94491BpeSegment_800b._800bd690;
import static legend.game.Scus94491BpeSegment_800b._800bd6f8;
import static legend.game.Scus94491BpeSegment_800b._800bd700;
import static legend.game.Scus94491BpeSegment_800b._800bd704;
import static legend.game.Scus94491BpeSegment_800b._800bd708;
import static legend.game.Scus94491BpeSegment_800b._800bd70c;
import static legend.game.Scus94491BpeSegment_800b._800bd710;
import static legend.game.Scus94491BpeSegment_800b._800bd714;
import static legend.game.Scus94491BpeSegment_800b._800bd740;
import static legend.game.Scus94491BpeSegment_800b._800bd748;
import static legend.game.Scus94491BpeSegment_800b._800bd780;
import static legend.game.Scus94491BpeSegment_800b._800bd781;
import static legend.game.Scus94491BpeSegment_800b._800bd782;
import static legend.game.Scus94491BpeSegment_800b._800bd808;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b._800bf0d8;
import static legend.game.Scus94491BpeSegment_800b._800bf0e0;
import static legend.game.Scus94491BpeSegment_800b.biggerStructPtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.biggerStruct_800bc0c0;
import static legend.game.Scus94491BpeSegment_800b.currentlyLoadingFileInfo_800bb468;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.drgnBinIndex_800bc058;
import static legend.game.Scus94491BpeSegment_800b.fileLoadingInfoArray_800bbad8;
import static legend.game.Scus94491BpeSegment_800b.fileSize_800bb464;
import static legend.game.Scus94491BpeSegment_800b.fileSize_800bb48c;
import static legend.game.Scus94491BpeSegment_800b.fileTransferDest_800bb488;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bc984;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bcf84;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bd76c;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bd778;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bd784;
import static legend.game.Scus94491BpeSegment_800b.linkedListEntry_800bd788;
import static legend.game.Scus94491BpeSegment_800b.loadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.numberOfTransfers_800bb490;
import static legend.game.Scus94491BpeSegment_800b.timHeader_800bc2e0;
import static legend.game.Scus94491BpeSegment_800b.transferDest_800bb460;
import static legend.game.Scus94491BpeSegment_800b.transferIndex_800bb494;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.Scus94491BpeSegment_800c.doubleBufferFrame_800c34d4;
import static legend.game.Scus94491BpeSegment_800d.sceaTexture_800d05c4;

public final class Scus94491BpeSegment {
  private Scus94491BpeSegment() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment.class);

  private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

  public static final BiFunctionRef<Long, Object[], Object> functionVectorA_000000a0 = MEMORY.ref(4, 0x000000a0L, BiFunctionRef::new);
  public static final BiFunctionRef<Long, Object[], Object> functionVectorB_000000b0 = MEMORY.ref(4, 0x000000b0L, BiFunctionRef::new);
  public static final BiFunctionRef<Long, Object[], Object> functionVectorC_000000c0 = MEMORY.ref(4, 0x000000c0L, BiFunctionRef::new);

  public static final Value temporaryStack_1f8003b4 = MEMORY.ref(4, 0x1f8003b4L);
  public static final Value oldStackPointer_1f8003b8 = MEMORY.ref(4, 0x1f8003b8L);
  public static final BoolRef isStackPointerModified_1f8003bc = MEMORY.ref(2, 0x1f8003bcL, BoolRef::new);
  public static final Value _1f8003c0 = MEMORY.ref(4, 0x1f8003c0L);
  public static final Value _1f8003c4 = MEMORY.ref(4, 0x1f8003c4L);
  public static final Value _1f8003c8 = MEMORY.ref(4, 0x1f8003c8L);
  public static final Value _1f8003cc = MEMORY.ref(4, 0x1f8003ccL);
  public static final Pointer<UnboundedArrayRef<GsOT_TAG>> tags_1f8003d0 = MEMORY.ref(4, 0x1f8003d0L, Pointer.of(4, UnboundedArrayRef.of(4, GsOT_TAG::new)));
  public static final Value _1f8003d4 = MEMORY.ref(4, 0x1f8003d4L);
  public static final Value linkedListAddress_1f8003d8 = MEMORY.ref(4, 0x1f8003d8L);
  public static final Value centreScreenX_1f8003dc = MEMORY.ref(2, 0x1f8003dcL);
  public static final Value centreScreenY_1f8003de = MEMORY.ref(2, 0x1f8003deL);
  /**
   * displayWidth
   */
  public static final Value _1f8003e0 = MEMORY.ref(4, 0x1f8003e0L);
  /**
   * displayHeight
   */
  public static final Value _1f8003e4 = MEMORY.ref(4, 0x1f8003e4L);

  public static final Value displayWidth_1f8003e0 = MEMORY.ref(4, 0x1f8003e0L);
  public static final Value displayHeight_1f8003e4 = MEMORY.ref(4, 0x1f8003e4L);

  public static final Value _1f8003fc = MEMORY.ref(4, 0x1f8003fcL);

  public static final Value _80010000 = MEMORY.ref(4, 0x80010000L);
  public static final Value _80010004 = MEMORY.ref(4, 0x80010004L);

  public static final Value _80010250 = MEMORY.ref(4, 0x80010250L);

  public static final Value _800103d0 = MEMORY.ref(4, 0x800103d0L);

  public static final Value _8001051c = MEMORY.ref(4, 0x8001051cL);

  /** unknown size */
  public static final Value _80010544 = MEMORY.ref(4, 0x80010544L);

  public static final Value ovalBlobTimHeader_80010548 = MEMORY.ref(4, 0x80010548L);

  public static final CString BASCUS_94491drgn00_80010734 = MEMORY.ref(20, 0x80010734L, CString::new);

  public static final ArrayRef<RECT> rectArray28_80010770 = MEMORY.ref(4, 0x80010770L, ArrayRef.of(RECT.class, 28, 8, RECT::new));

  /**
   * String: CD_sync
   */
  public static final CString _80011394 = MEMORY.ref(8, 0x80011394L, CString::new);

  /**
   * String: CD_ready
   */
  public static final CString _8001139c = MEMORY.ref(9, 0x8001139cL, CString::new);

  /**
   * String: CD_cw
   */
  public static final CString _800113c0 = MEMORY.ref(6, 0x800113c0L, CString::new);

  public static final Value _80011db0 = MEMORY.ref(4, 0x80011db0L);

  public static final Value _8011e210 = MEMORY.ref(4, 0x8011e210L);

  @Method(0x80011dc0L)
  public static void spuTimerInterruptCallback() {
    FUN_80045cb8();
    FUN_8002c0c8();

    if(_8004dd20.get() == 0x3L) {
      _800bac68.setu(0);
    } else {
      _800bac68.addu(0x1L);
    }
  }

  @Method(0x80011e1cL)
  public static void FUN_80011e1c() {
    do {
      FUN_80012d58();
      processControllerInput();
      FUN_80011f24();
      FUN_80014d20();
      FUN_80022518();
      FUN_80011ec0();
      FUN_80011ec8();
      FUN_8001b410();
      FUN_80013778();
      FUN_800145c4();
      FUN_8001aa24();
      FUN_8002a058();
      FUN_8002a0e4();
      FUN_80020ed8();
      FUN_80017f94();
      _800bb0fc.addu(0x1L);
      FUN_80012df8();

      DebugHelper.sleep(1);
    } while(true);
  }

  @Method(0x80011ec0L)
  public static void FUN_80011ec0() {
    // Empty
  }

  @Method(0x80011ec8L)
  public static void FUN_80011ec8() {
    if(FUN_800128c4() == 0) {
      return;
    }

    callback_8004dbc0.offset(_8004dd20.get() * 16).deref(4).cast(RunnableRef::new).run();

    FUN_8001575c();
    FUN_800157b8();
  }

  @Method(0x80011f24L)
  public static void FUN_80011f24() {
//    oldStackPointer_1f8003b8.setu(sp);
    isStackPointerModified_1f8003bc.set(true);
//    sp = temporaryStack_1f8003b4.getAddress();

    FUN_80011f6c();

    isStackPointerModified_1f8003bc.set(false);
//    sp = oldStackPointer_1f8003b8.get();
  }

  @Method(0x80011f6cL)
  public static void FUN_80011f6c() {
    //LAB_80011f88
    for(int i = 0; i < 0x10; i++) {
      if(_8005a1ea.offset(i * 12).get() != 0) {
        _8005a1ea.offset(i * 12).subu(0x1L);

        if(_8005a1ea.offset(i * 12).get() << 0x10L == 0) {
          if(_8005a1e4.offset(i * 12).get() == 0) {
            //LAB_80011fdc
            removeFromLinkedList(_8005a1e0.offset(i * 12).get());

            //LAB_80011ffc
          } else {
            FUN_80012444(_8005a1e0.offset(i * 12).get(), _8005a1e4.offset(i * 12).get());
          }
        }
      }

      //LAB_80012000
    }
  }

  @Method(0x80012094L)
  public static void allocateLinkedList(long address, long size) {
    size = size - 0x18L & 0xffff_fffcL;
    address = address + 0x3L & 0xffff_fffcL;

    MEMORY.ref(4, address).offset(0x00L).setu(0);
    MEMORY.ref(4, address).offset(0x04L).setu(0xcL);
    MEMORY.ref(2, address).offset(0x08L).setu(0x3L);
    MEMORY.ref(4, address).offset(0x0cL).setu(address);
    MEMORY.ref(4, address).offset(0x10L).setu(size);
    MEMORY.ref(2, address).offset(0x14L).setu(0);
    MEMORY.ref(4, address).offset(size).offset(0x0cL).setu(address).addu(0xcL);
    MEMORY.ref(4, address).offset(size).offset(0x10L).setu(0);
    MEMORY.ref(2, address).offset(size).offset(0x14L).setu(0x3L);

    linkedListHead_8005a2a0.setu(address).addu(0xcL);
    linkedListTail_8005a2a4.setu(address).addu(0xcL).addu(size);
  }

  @Method(0x800120f0L)
  public static long addToLinkedListHead(long size) {
    size = size + 0xfL & 0xffff_fffcL;

    long currentEntry = linkedListHead_8005a2a0.get();
    long entryType = MEMORY.ref(2, currentEntry).offset(0x8L).get();

    //LAB_80012120
    while(entryType != 0x3L) {
      final long spaceAvailable = MEMORY.ref(4, currentEntry).offset(0x4L).get();

      if(entryType == 0) {
        if(spaceAvailable >= size) {
          MEMORY.ref(2, currentEntry).offset(0x8L).setu(0x1L);

          if(size + 0xcL < spaceAvailable) {
            MEMORY.ref(2, currentEntry).offset(size).offset(0x8L).setu(0);
            MEMORY.ref(4, currentEntry).offset(0x4L).setu(size);
            MEMORY.ref(4, currentEntry).offset(size).offset(0x0L).setu(currentEntry);
            MEMORY.ref(4, currentEntry).offset(size).offset(0x4L).setu(spaceAvailable - size);
            MEMORY.ref(4, currentEntry).offset(spaceAvailable).setu(currentEntry + size);
          }

          return currentEntry + 0xcL;
        }
      }

      //LAB_80012170
      currentEntry += MEMORY.ref(4, currentEntry).offset(0x4L).get();
      entryType = MEMORY.ref(2, currentEntry).offset(0x8L).get();
    }

    //LAB_8001218c
    assert false : "Failed to allocate entry on linked list";
    return 0;
  }

  @Method(0x80012194L)
  public static long addToLinkedListTail(long size) {
    size = size + 0xfL & 0xffff_fffcL;

    Value currentEntry = linkedListTail_8005a2a4;
    Value nextEntry = linkedListTail_8005a2a4.deref(4);
    long entryType = linkedListTail_8005a2a4.deref(4).deref(2).offset(0x8L).get();
    // Known entry types:
    // 0: empty space?
    // 2: used?
    // 3: end of list?

    //LAB_800121cc
    while(entryType != 0x3L) {
      final long spaceAvailable = nextEntry.deref(4).offset(0x4L).get();
      if(entryType == 0 && spaceAvailable >= size) {
        if(spaceAvailable > size + 0xcL) {
          currentEntry.deref(2).offset(-size).offset(0x8L).setu(0x2L); // Mark as used
          nextEntry.deref(2).offset(0x8L).setu(0); // Mark as empty space
          nextEntry.deref(4).offset(0x4L).setu(spaceAvailable - size);
          nextEntry.deref(4).offset(-size).offset(spaceAvailable).setu(nextEntry);
          currentEntry.deref(4).offset(-size).offset(0x4L).setu(size);
          currentEntry.deref(4).setu(currentEntry.get() - size);
          return currentEntry.get() - size + 0xcL;
        }

        //LAB_80012214
        nextEntry.deref(2).offset(0x8L).setu(0x2L); // Mark as used
        return nextEntry.get() + 0xcL;
      }

      //LAB_80012220
      currentEntry = nextEntry;
      nextEntry = nextEntry.deref(4);
      entryType = nextEntry.deref(2).offset(0x8L).get();
    }

    //LAB_8001223c
    assert false : "Failed to allocate entry on linked list";
    return 0;
  }

  @Method(0x80012244L)
  public static long FUN_80012244(final long address, long size) {
    if(address == 0) {
      return 0;
    }

    long s1 = address - 0xcL;
    size += 0xfL;
    size &= 0xffff_fffcL;
    long t1 = MEMORY.ref(4, s1).offset(0x4L).get();
    long v1 = MEMORY.ref(4, s1).get();
    final long s2;
    final long s0;
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      //LAB_800122a0
      s0 = v1;
      s2 = Math.min(size, t1);
      t1 += MEMORY.ref(4, v1).offset(0x4L).get();
    } else {
      //LAB_800122b0
      s0 = s1;
      s2 = 0;
    }

    //LAB_800122b4
    v1 = s1 + MEMORY.ref(4, s1).offset(0x4L).get();
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      t1 += MEMORY.ref(4, v1).offset(0x4L).get();
    }

    //LAB_800122e0
    final long t2 = t1 - size;
    if(t1 >= size) {
      if(s2 != 0) {
        long t0 = s0 + 0xcL;
        long a3 = s1 + 0xcL;
        long a2 = (s2 - 0xcL) / 4;
        if(t0 >= a3) {
          //LAB_8001233c
          if(s1 < s0) {
            //LAB_80012358
            while(a2-- > 0) {
              MEMORY.ref(4, t0).offset(a2 * 4).setu(MEMORY.ref(4, a3).offset(a2 * 4));
            }
          }
        } else {
          //LAB_80012314
          while(a2-- > 0) {
            MEMORY.ref(4, t0).setu(MEMORY.ref(4, a3));
            a3 += 0x4L;
            t0 += 0x4L;
          }
        }

        //LAB_8001237c
      }

      //LAB_80012380
      MEMORY.ref(2, s0).offset(0x8L).setu(0x1L);

      if(t2 >= 0xcL) {
        MEMORY.ref(2, s0).offset(size).offset(0x8L).setu(0);
        MEMORY.ref(4, s0).offset(0x4L).setu(size);
        MEMORY.ref(4, s0).offset(size).setu(s0);
        MEMORY.ref(4, s0).offset(size).offset(0x4L).setu(t2);
        MEMORY.ref(4, s0).offset(size).offset(t2).setu(s0 + size);
      } else {
        //LAB_800123b0
        MEMORY.ref(4, s0).offset(0x4L).setu(t1);
        MEMORY.ref(4, s0).offset(t1).setu(s0);
      }

      //LAB_800123b8
      return s0 + 0xcL;
    }

    //LAB_800123c0
    final long dataAddress = addToLinkedListHead(size - 0xcL);
    if(dataAddress == 0) {
      //LAB_800123dc
      return 0;
    }

    s1 += 0xcL;

    //LAB_800123e4
    v1 = s1;

    //LAB_800123f8
    for(int i = 0; i < (s2 - 0xcL) / 4; i++) {
      MEMORY.ref(4, dataAddress).offset(i * 4L).setu(MEMORY.ref(4, v1));
      v1 += 0x4L;

      //LAB_8001240c
    }

    removeFromLinkedList(s1);

    //LAB_8001242c
    return dataAddress;
  }

  @Method(0x80012444L)
  public static long FUN_80012444(long address, final long size) {
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long v0;
    long v1;
    long t0;
    long t1;
    long a2;
    long a3;

    if(address == 0) {
      return 0;
    }

    s1 = address - 0xcL;
    s2 = size + 0xfL & 0xffff_fffcL;
    long a1;
    s3 = MEMORY.ref(4, s1).offset(0x4L).get();
    v1 = Math.min(s2, s3);

    //LAB_80012494
    s4 = v1;
    s0 = addToLinkedListTail(s2 - 0xcL);
    if(s0 != 0) {
      s0 -= 0xcL;
      a1 = s0 + 0xcL;
      if(s1 < s0) {
        address = s1 + 0xcL;
        v1 = (s4 - 0xcL) / 4;

        //LAB_800124d0
        while(v1-- > 0) {
          MEMORY.ref(4, a1).setu(MEMORY.ref(4, address));
          address += 0x4L;
          a1 += 0x4L;

          //LAB_800124e4
        }

        removeFromLinkedList(s1 + 0xcL);

        return s0 + 0xcL;
      }

      //LAB_800124f8
      removeFromLinkedList(s0 + 0xcL);
    }

    //LAB_80012508
    if(MEMORY.ref(4, s1).deref(2).offset(0x8L).get() == 0) {
      s0 = MEMORY.ref(4, s1).get();
      s3 += MEMORY.ref(4, s0).offset(0x4L).get();
    } else {
      s0 = s1;
    }

    //LAB_80012530
    v1 = s1 + MEMORY.ref(4, s1).offset(0x4L).get();
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      s3 += MEMORY.ref(4, v1).offset(0x4L).get();
    }

    //LAB_8001255c
    t1 = s3 - s2;
    if(s3 >= s2) {
      t0 = s0 + t1;
      if(t1 >= 0xcL) {
        if(t0 != s1) {
          a1 = (s4 - 0xcL) / 4;

          if(t0 >= s1) {
            //LAB_800125c0
            if(s1 < t0) {
              //LAB_800125e0
              while(a1-- > 0) {
                MEMORY.ref(4, t0).offset(0xcL).offset(a1 * 4).setu(MEMORY.ref(4, s1).offset(0xcL).offset(a1 * 4));
              }
            }
          } else {
            if(a1-- > 0) {
              a2 = s1 + 0xcL;
              a3 = t0 + 0xcL;

              //LAB_80012598
              do {
                MEMORY.ref(4, a3).setu(MEMORY.ref(4, a2));
                a2 += 0x4L;
                a3 += 0x4L;
              } while(a1-- > 0);
            }
          }
        }

        //LAB_80012604
        MEMORY.ref(2, s0).offset(0x8L).setu(0);

        //LAB_80012608
        MEMORY.ref(2, t0).offset(0x8L).setu(0x2L);
        MEMORY.ref(4, s0).offset(0x4L).setu(t1);
        MEMORY.ref(4, s0).offset(t1).setu(s0);
        MEMORY.ref(4, t0).offset(0x4L).setu(s2);
        MEMORY.ref(4, t0).offset(s2).setu(t0);
        return t0 + 0xcL;
      }

      //LAB_80012630
      if(s0 != s1) {
        a1 = (s4 - 0xcL) / 4;
        a3 = s0 + 0xcL;

        if(s0 < s1) {
          a2 = s1 + 0xcL;

          //LAB_80012658
          while(a1-- > 0) {
            v0 = MEMORY.ref(4, a2).get();
            MEMORY.ref(4, a3).setu(v0);
            a2 += 0x4L;
            a3 += 0x4L;
          }
        } else {
          //LAB_80012680
          if(s1 < s0) {
            //LAB_8001269c
            a3 = s0 + 0xcL;
            while(a1-- > 0) {
              MEMORY.ref(4, a3).offset(a1 * 4).setu(MEMORY.ref(4, s1).offset(0xcL).offset(a1 * 4));
            }
          }
        }
      }

      //LAB_800126c0
      //LAB_800126c4
      MEMORY.ref(2, s0).offset(0x8L).setu(0x2L);
      MEMORY.ref(4, s0).offset(0x4L).setu(s3);
      MEMORY.ref(4, s0).offset(s3).setu(s0);
    } else {
      //LAB_800126d8
      if(addToLinkedListTail(s2 - 0xcL) == 0) {
        //LAB_800126f4
        return 0;
      }

      //LAB-800126fc
      a1 = s0;
      v1 = s1 + 0xcL;
      address = (s4 - 0xcL) / 4;

      //LAB_80012710
      while(address-- > 0) {
        MEMORY.ref(4, a1).setu(MEMORY.ref(4, v1));
        v1 += 0x4L;
        a1 += 0x4L;

        //LAB_80012724
      }

      //LAB_80012734
      removeFromLinkedList(s1 + 0xcL);
    }

    //LAB_80012740
    //LAB_80012744
    return s0;
  }

  @Method(0x80012764L)
  public static void removeFromLinkedList(long address) {
    address -= 0xcL;
    long a1 = MEMORY.ref(4, address).offset(0x4L).get(); // Remaining size?
    MEMORY.ref(2, address).offset(0x8L).setu(0); // Entry type?

    if(MEMORY.ref(2, address).offset(a1).offset(0x8L).get() == 0) {
      a1 += MEMORY.ref(4, address).offset(a1).offset(0x4L).get();
    }

    //LAB_80012794
    final long v1 = MEMORY.ref(4, address).get();
    if(MEMORY.ref(2, v1).offset(0x8L).get() == 0) {
      a1 += MEMORY.ref(4, v1).offset(0x4L).get();
      address = v1;
    }

    //LAB_800127bc
    MEMORY.ref(4, address).offset(0x4L).setu(a1);
    MEMORY.ref(4, address).offset(a1).setu(address);
  }

  @Method(0x800127ccL)
  public static long FUN_800127cc(final Value a0, final long a1, final long a2, final long a3, final long a4) {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x800128c4L)
  public static long FUN_800128c4() {
    long v0;
    long v1;
    long s0;

    if(_8004dd1e.get() == 0) {
      s0 = _8004dd18.get();
      v0 = s0 << 0x1L;

      //LAB_80012910
      v0 += s0;
      v0 <<= 0x2L;
      v1 = _8005a2a8.offset(v0).getAddress();
      if(_8004dd14.get() != s0) {
        //LAB_80012900
        while(MEMORY.ref(4, v1).offset(0x4L).get() == 0) {
          v0 = s0 + 1;
          v0 &= 0xfL;
          _8004dd18.setu(v0);
          s0 = v0;
          v0 = s0 << 0x1L;

          //LAB_80012910
          v0 += s0;
          v0 <<= 0x2L;
          v1 = _8005a2a8.offset(v0).getAddress();
          if(_8004dd14.get() == s0) {
            break;
          }
        }
      }

      //LAB_80012930
      if(_8004dd1e.get() == 0) {
        if(_8004dd1c.get() == 0) {
          if(_8004dd14.get() != s0) {
            _8004dd1e.setu(0x1L);
            v1 = MEMORY.ref(4, v1).get();
            _8004dd10.setu(v1);
            FUN_8001524c(_8004db88.offset(v1 * 8).getAddress(), _80010004.get(), getMethodAddress(Scus94491BpeSegment.class, "FUN_80012c54", Value.class, long.class, long.class), s0, 0x11L);
            v0 = s0 + 1;
            v0 &= 0xfL;
            _8004dd18.setu(v0);
          }
        }
      }
    }

    //LAB_800129c0
    //LAB_800129c4
    final long s1 = _8004dd24.getSigned();
    if(s1 != -0x1L) {
      if(_8004dd08.get() != 0) {
        return 0;
      }

      v1 = _8004dd20.get();
      loadingStage_800bb10c.setu(0);
      _8004dd20.set(s1);
      _8004dd24.setu(-0x1L);
      _8004dd28.setu(v1);
      FUN_80019710();
      _8007a3b8.setu(0x2L);
      FUN_80012a84(s1);

      if(_8004dd20.get() == 0x6L) {
        FUN_8001c4ec(0);
      }
    }

    //LAB_80012a34
    //LAB_80012a38
    if(_8004dd08.get() == 0 || _8004dbcd.offset(_8004dd20.get() * 16).get() != 0) {
      //LAB_80012a6c
      return 1;
    }

    //LAB_80012a70
    return 0;
  }

  @Method(0x80012a84L)
  public static long FUN_80012a84(final long a0) {
    final long val = _8004dbc4.offset(a0 * 16).get();

    if(val == 0 || val == _8004dd04.get()) {
      //LAB_80012ac0
      FUN_80012bd4(a0);
      _8004dd08.setu(0);
      return 0x1L;
    }

    //LAB_80012ad8
    _8004dd04.setu(val);
    _8004dd08.setu(0x1L);
    FUN_8001524c(val, _80010000.get(), getMethodAddress(Scus94491BpeSegment.class, "FUN_80012c48", Value.class, long.class, long.class), a0, 0x11L);
    return 0;
  }

  @Method(0x80012b1cL)
  public static void FUN_80012b1c(final long a0, final long a1, final long a2) {
    if(_8004dd10.get() == a0 && _8004dd1e.get() == 0) {
      _8004dd1c.addu(0x1L);
      MEMORY.ref(4, a1).cast(ConsumerRef::new).run(a2);
      return;
    }

    //LAB_80012b6c
    //LAB_80012b70
    _8005a2a8.offset(_8004dd14.get() * 12).setu(a0);
    _8005a2ac.offset(_8004dd14.get() * 12).setu(a1);
    _8005a2b0.offset(_8004dd14.get() * 12).setu(a2);
    _8004dd14.addu(0x1L).and(0xfL);

    //LAB_80012ba4
  }

  @Method(0x80012bb4L)
  public static void FUN_80012bb4() {
    if(_8004dd1c.getSigned() > 0) {
      _8004dd1c.subu(1);
    }
  }

  @Method(0x80012bd4L)
  public static void FUN_80012bd4(long a0) {
    long v0 = a0 << 0x4L;
    if(_8004dd0c.get() != 0) {
      _8004dd0c.setu(0);
      return;
    }

    //LAB_80012bf0
    long v1 = _8004dbc8.offset(v0).get();
    if(v1 != 0) {
      v0 = MEMORY.ref(4, v1).offset(0x4L).get();
      a0 = MEMORY.ref(4, v0).get();
      v1 = v0 >> 0x2L;

      v0 = v1;
      v1--;

      //LAB_80012c1c
      while(v0 > 0) {
        MEMORY.ref(4, a0).setu(1);
        a0 += 0x4L;

        //LAB_80012c24
        v0 = v1;
        v1--;
      }
    }
    //LAB_80012c30
  }

  @Method(0x80012c48L)
  public static void FUN_80012c48(final Value a0, final long a1, final long a2) {
    _8004dd08.setu(0);
  }

  @Method(0x80012c54L)
  public static void FUN_80012c54(final Value a0, final long a1, final long a2) {
    _8004dd1e.setu(0);
    FUN_80012c7c(a2);
  }

  @Method(0x80012c7cL)
  public static void FUN_80012c7c(final long a0) {
    long s1 = a0;

    //LAB_80012cd0
    while(s1 != _8004dd14.get()) {
      if(_8004dd10.get() == _8005a2a8.offset(s1 * 12).get()) {
        if(_8005a2ac.offset(s1 * 12).get() != 0) {
          _8004dd1c.addu(0x1L);
          _8005a2ac.offset(s1 * 12).deref(4).cast(ConsumerRef::new).run(_8005a2b0.offset(s1 * 12).get());
        }

        //LAB_80012d14
        _8005a2a8.offset(s1 * 12).setu(-0x1L);
        _8005a2ac.offset(s1 * 12).setu(0);
      }

      //LAB_80012d20
      s1 = s1 + 0x1L & 0xfL;
    }

    //LAB_80012d30
  }

  @Method(0x80012d58L)
  public static void FUN_80012d58() {
    doubleBufferFrame_800bb108.setu(doubleBufferFrame_800c34d4);

    _8007a3ac.setu(_8009a7c0.offset(doubleBufferFrame_800c34d4.get() * 0x20400L).getAddress());

    _1f8003d4.setu(_8007a3ac);
    tags_1f8003d0.set(_8005a398.get((int)doubleBufferFrame_800c34d4.get()));
    linkedListAddress_1f8003d8.setu(_8007a3c0.offset(doubleBufferFrame_800c34d4.get() * 0x20400L).getAddress());

    FUN_8003cd40(0, 0, orderingTables_8005a370.get((int)doubleBufferFrame_800c34d4.get()));
  }

  @Method(0x80012df8L)
  public static void FUN_80012df8() {
    if(_8004dd36.get(0x2L) == 0) {
      long a1 = linkedListAddress_1f8003d8.get();

      MEMORY.ref(1, a1).offset(0x3L).setu(0x2L); // 2 words

      MEMORY.ref(4, a1).offset(0x4L).setu(0xe600_0000L);
      MEMORY.ref(4, a1).offset(0x8L).setu(0);

      insertElementIntoLinkedList(tags_1f8003d0.deref().get(3).getAddress(), a1);
      linkedListAddress_1f8003d8.addu(0xcL);

      a1 = linkedListAddress_1f8003d8.get();

      MEMORY.ref(1, a1).offset(0x3L).setu(0x2L); // 2 words

      MEMORY.ref(4, a1).offset(0x4L).setu(0xe600_0001L);
      MEMORY.ref(4, a1).offset(0x8L).setu(0);

      insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)(_1f8003c8.get() - 1)).getAddress(), a1);
      linkedListAddress_1f8003d8.addu(0xcL);
    }

    //LAB_80012e8c
    callback_8004dd3c.deref().run();
    callback_8004dd40.deref().run();
  }

  @Method(0x80012eccL)
  public static void FUN_80012ecc() {
    if(_8004dd36.get(0x2L) == 0) {
      //LAB_80012efc
      DrawSync(0);
      VSync((int)_8007a3b8.getSigned());
    } else {
      VSync(0);
      FUN_8003b0d0();
    }

    //LAB_80012f14
  }

  @Method(0x80012f24L)
  public static void FUN_80012f24() {
    final long flags;
    if(_8004dd36.get(0x3L) == 0) {
      flags = 0b110100L;
    } else {
      flags = 0b110101L;
    }

    //LAB_80012f5c
    final long use24BitColour = _8004dd36.get() >>> 0x2L & 0x1L;
    final long s0 = _8004dd36.get() & 0x2L;
    final long length = _8004dd38.get();

    _800babc0.setu(0);
    _800bb104.setu(0);
    _8007a3a8.setu(0);

    final RECT rect1 = new RECT((short)0, (short)16, (short)_8004dd34.get(), (short)240);
    final RECT rect2 = new RECT((short)0, (short)256, (short)_8004dd34.get(), (short)240);

    orderingTables_8005a370.get(0).length_00.set(length);
    orderingTables_8005a370.get(1).length_00.set(length);

    _1f8003c0.setu(length);
    _1f8003c4.setu(0xeL - length);
    _1f8003c8.setu(0x1L << length);
    _1f8003cc.setu((0x1L << length) - 0x2L);

    VSync(0);
    SetDispMask(0);
    DrawSync(0);
    VSync(0);

    final long displayHeight;
    if(s0 == 0) {
      //LAB_80013040
      setClip((short)0, (short)16, (short)0, (short)256);
      displayHeight = 240L;
    } else {
      setClip((short)0, (short)16, (short)0, (short)16);
      displayHeight = 480L;
    }

    //LAB_80013060
    FUN_8003bc30((short)_8004dd34.get(), (short)displayHeight, (short)flags, true, use24BitColour != 0);

    if(_8004dd34.get() == 0x180L) {
      DISPENV_800c34b0.screen.x.set((short)9);
    }

    //LAB_80013080
    FUN_8003cd40(0, 0, orderingTables_8005a370.get(0));
    FUN_8003cd40(0, 0, orderingTables_8005a370.get(1));
    ClearImage(rect1, (byte)0, (byte)0, (byte)0);
    ClearImage(rect2, (byte)0, (byte)0, (byte)0);
    FUN_8003c5e0();
    setProjectionPlaneDistance(320);

    DrawSync(0);
    VSync(0);
    SetDispMask(1);

    callback_8004dd3c.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "FUN_80012ecc")).cast(RunnableRef::new));

    if(use24BitColour == 0) {
      callback_8004dd40.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "FUN_80013148")).cast(RunnableRef::new));
    } else {
      callback_8004dd40.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "FUN_800131e0")).cast(RunnableRef::new));
    }
  }

  @Method(0x80013148L)
  public static void FUN_80013148() {
    FUN_8003c350();

    if(_8004dd36.get(0x2L) == 0) {
      FUN_8003c048(_8007a3a8.get(), _800bb104.get(),  _800babc0.get(), orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()));
    }

    //LAB_800131b0
    drawOTag(orderingTables_8005a370.get((int)doubleBufferFrame_800bb108.get()));
  }

  @Method(0x800131e0L)
  public static void FUN_800131e0() {
    FUN_8003c350();
  }

  @Method(0x80013200L)
  public static void FUN_80013200(final long a0, final long a1) {
    if(a0 != displayWidth_1f8003e0.get() || a1 != _8004dd36.get()) {
      callback_8004dd3c.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment.class, "FUN_80012f24")).cast(RunnableRef::new));
      _8004dd34.setu(a0);
      _8004dd36.setu(a1);
    }
  }

  @Method(0x800133acL)
  public static long FUN_800133ac() {
    final long v1;
    if(_8004dd44.get(0xfffL) == 0) {
      v1 = VSync(-1) * 0x9L;
    } else {
      v1 = _8004dd44.get();
    }

    //LAB_800133dc
    _8004dd44.setu(v1 * 0x9L + 0x3711L);
    return _8004dd44.get() / 0x2L & 0xffffL;
  }

  @Method(0x80013434L)
  public static void FUN_80013434(final long a0, final long a1, final long a2, final long a3) {
    long s4 = 0;

    //LAB_8001347c
    while(s4 << 0x1L < a1) {
      s4 += s4 + 0x1L;
    }

    //LAB_80013490
    final long s6 = a1 * a2 & 0xffff_ffffL;
    final long s7 = a2 >>> 0x2L;

    //LAB_800134a4
    while(s4 > 0) {
      final long s5 = s4 * a2 & 0xffff_ffffL;
      long s3 = s5;

      //LAB_800134b8
      while(s3 < s6) {
        long s2 = s3 - s5;

        //LAB_800134c4
        while(s2 >= 0) {
          final long s0 = a0 + s2;
          final long s1 = s0 + s5;

          if((long)MEMORY.ref(4, a3).cast(BiFunctionRef::new).run(s0, s1) <= 0) {
            break;
          }

          //LAB_80013500
          for(int i = 0; i < s7; i++) {
            final long v1 = MEMORY.ref(4, s0).offset(i * 4L).get();
            final long v0 = MEMORY.ref(4, s1).offset(i * 4L).get();
            MEMORY.ref(4, s0).offset(i * 4L).setu(v0);
            MEMORY.ref(4, s1).offset(i * 4L).setu(v1);
          }

          //LAB_80013524
          s2 -= s5;
        }

        //LAB_80013530
        s3 += a2;
      }

      //LAB_80013540
      s4 >>= 0x1L;
    }

    //LAB_8001354c
  }

  @Method(0x8001357cL)
  public static void insertElementIntoLinkedList(final long previousElement, final long newElement) {
    MEMORY.ref(3, newElement).setu(MEMORY.ref(3, previousElement));
    MEMORY.ref(3, previousElement).setu(newElement);
  }

  /**
   * Maybe sin/cos
   */
  @Method(0x80013598L)
  public static long rsin(final long angleDiv360Maybe) {
    return _80054d0c.offset(2, 0x0L).offset((angleDiv360Maybe & 0xfffL) * 4).get();
  }

  /**
   * Maybe sin/cos
   */
  @Method(0x800135b8L)
  public static long rcos(final long angleDiv360Maybe) {
    return _80054d0c.offset(2, 0x2L).offset((angleDiv360Maybe & 0xfffL) * 4).get();
  }

  /**
   * 800135d8
   *
   * Copies the first n bytes of src to dest.
   *
   * @param dest Pointer to copy destination memory block
   * @param src Pointer to copy source memory block
   * @param length Number of bytes copied
   *
   * @return Pointer to destination (dest)
   */
  @Method(0x800135d8L)
  public static long memcpy(final long dest, long src, int length) {
    if(length == 0) {
      return dest;
    }

    final long v0 = (length | dest | src) & 0xfffffffcL;
    long a3 = dest;
    if(v0 == 0) {
      length = length / 4;

      //LAB_80013600
      while(length > 0) {
        MEMORY.ref(4, a3).setu(MEMORY.ref(4, src));
        src += 0x4L;
        a3 += 0x4L;
        length--;
      }

      return dest;
    }

    //LAB_80013630
    while(length > 0) {
      MEMORY.ref(1, a3).setu(MEMORY.ref(1, src));
      src++;
      a3++;
      length--;
    }

    //LAB_8001364c
    return dest;
  }

  @Method(0x80013658L)
  public static void fillMemory(final long address, final long fill, final long size) {
    // Fill in 4-byte segments if possible
    if((address & 0x3L) == 0 && (size & 0x3L) == 0) {
      for(int i = 0; i < size; i += 4) {
        MEMORY.set(address + i, 4, fill);
      }

      return;
    }

    for(int i = 0; i < size; i++) {
      MEMORY.set(address + i, (byte)fill);
    }
  }

  @Method(0x800136dcL)
  public static void FUN_800136dc(final long a0, long a1) {
    if((int)a1 <= 0) {
      a1 = 0xfL;
    }

    //LAB_800136f4
    _800bb140.setu(a0);
    _800bb148.setu(a1);
    _800bb144.setu(VSync(-1));

    _800bb164.setu(_8004dd48.offset(a0 * 2));

    if(_8004dd48.offset(a0 * 2).get() == 0x2L) {
      _800bb14c.setu(0);
      _800bb150.setu(0);
      _800bb154.setu(0);
      _800bb158.setu(0);
      _800bb15c.setu(0);
      _800bb160.setu(0);
    }

    //LAB_80013768
  }

  @Method(0x80013778L)
  public static void FUN_80013778() {
    long a1;

    final long v1 = Math.min(_800bb148.get(), (VSync(-1) - _800bb144.get()) / 2);

    //LAB_800137d0
    final long s2;
    if(_800bb148.get() == 0) {
      s2 = 0;
    } else {
      a1 = _800bb164.get();
      if(a1 == 0x1L) {
        //LAB_80013818
        s2 = v1 * 255 / _800bb148.get();
      } else if(a1 < 0x2L) {
        if(a1 != 0) {
          _800bb140.setu(0);
          _800bb164.setu(0);
        }

        s2 = 0;

        //LAB_80013808
      } else if(a1 != 0x2L) {
        _800bb140.setu(0);
        _800bb164.setu(0);
        s2 = 0;
      } else {
        //LAB_8001383c
        s2 = v1 * 255 / _800bb148.get() ^ 0xffL;

        if(s2 == 0) {
          //LAB_80013874
          _800bb164.setu(0);
        }
      }
    }

    //LAB_80013880
    //LAB_80013884
    _800bb168.setu(s2);

    if(s2 != 0) {
      //LAB_800138f0
      //LAB_80013948
      switch((int)_800bb140.get()) {
        case 1, 2 -> FUN_80013c3c(s2, 0x2L);
        case 3, 4 -> FUN_80013c3c(s2, 0x1L);

        case 5 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_800138f8
            for(int s0 = 0; s0 < 6; s0++) {
              FUN_80013d78(s2 - (0xcL - (s0 + s1)) * 11, s1, s0);
            }
          }
        }

        case 6 -> {
          for(int s1 = 0; s1 < 8; s1++) {
            //LAB_80013950
            for(int s0 = 0; s0 < 6; s0++) {
              FUN_80013d78(s2 - (s1 + s0) * 11, s1, s0);
            }
          }
        }
      }
    }

    //caseD_0
    //LAB_80013994
    if(_800bb160.get() != 0 || _800bb15c.get() != 0 || _800bb154.get() != 0) {
      //LAB_800139c4
      final long s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x10L);

      MEMORY.ref(1, s0).offset(0x3L).setu(0x3L); // 3 words

      MEMORY.ref(1, s0).offset(0x4L).setu(_800bb160.offset(1, 0x0L));
      MEMORY.ref(1, s0).offset(0x5L).setu(_800bb15c.offset(1, 0x0L));
      MEMORY.ref(1, s0).offset(0x6L).setu(_800bb154.offset(1, 0x0L));
      MEMORY.ref(1, s0).offset(0x7L).setu(0x60L);

      MEMORY.ref(2, s0).offset(0x8L).setu(-centreScreenX_1f8003dc.get());
      MEMORY.ref(2, s0).offset(0xaL).setu(-centreScreenY_1f8003de.get());
      MEMORY.ref(2, s0).offset(0xcL).setu(displayWidth_1f8003e0.get() + 0x1L);
      MEMORY.ref(2, s0).offset(0xeL).setu(displayHeight_1f8003e4.get() + 0x1L);

      gpuLinkedListSetCommandTransparency(s0, true);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x27).getAddress(), s0);

      a1 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x8L);

      MEMORY.ref(1, a1).offset(0x3L).setu(0x1L); // 1 word
      MEMORY.ref(4, a1).offset(0x4L).setu(0xe1000205L | _800bb114.get(0x9ffL));

      insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x27).getAddress(), a1);
    }

    //LAB_80013adc
    if(_800bb158.get() == 0 && _800bb150.get() == 0) {
      if(_800bb14c.get() == 0) {
        return;
      }
    }

    //LAB_80013b10
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x10L);

    MEMORY.ref(1, s0).offset(0x3L).setu(0x3L); // 3 words

    MEMORY.ref(1, s0).offset(0x4L).setu(_800bb158.offset(1, 0x0L));
    MEMORY.ref(1, s0).offset(0x5L).setu(_800bb150.offset(1, 0x0L));
    MEMORY.ref(1, s0).offset(0x6L).setu(_800bb14c.offset(1, 0x0L));
    MEMORY.ref(1, s0).offset(0x7L).setu(0x60L);

    MEMORY.ref(2, s0).offset(0x8L).setu(-centreScreenX_1f8003dc.get());
    MEMORY.ref(2, s0).offset(0xaL).setu(-centreScreenY_1f8003de.get());
    MEMORY.ref(2, s0).offset(0xcL).setu(displayWidth_1f8003e0.get() + 0x1L);
    MEMORY.ref(2, s0).offset(0xeL).setu(displayHeight_1f8003e4.get() + 0x1L);

    gpuLinkedListSetCommandTransparency(s0, true);
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x27).getAddress(), s0);

    a1 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x8L);

    MEMORY.ref(1, a1).offset(0x3L).setu(0x1L); // 1 word
    MEMORY.ref(4, a1).offset(0x4L).setu(0xe1000205L | _800bb118.get(0x9ffL));

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x27).getAddress(), a1);

    //LAB_80013c20
  }

  @Method(0x80013c3cL)
  public static void FUN_80013c3c(final long a0, final long a1) {
    long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x10L);

    MEMORY.ref(1, s0).offset(0x3L).setu(0x3L); // 3 words

    MEMORY.ref(1, s0).offset(0x4L).setu(a0); // R
    MEMORY.ref(1, s0).offset(0x5L).setu(a0); // G
    MEMORY.ref(1, s0).offset(0x6L).setu(a0); // B
    MEMORY.ref(1, s0).offset(0x7L).setu(0x60L); // Monochrome rectangle (variable size, opaque)

    MEMORY.ref(2, s0).offset(0x8L).set(-centreScreenX_1f8003dc.get()); // xx
    MEMORY.ref(2, s0).offset(0xaL).set(-centreScreenY_1f8003de.get()); // xx
    MEMORY.ref(2, s0).offset(0xcL).setu(displayWidth_1f8003e0.offset(2, 0x0L).get() + 1); // yy
    MEMORY.ref(2, s0).offset(0xeL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + 1); // yy

    gpuLinkedListSetCommandTransparency(s0, true);
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x1e).getAddress(), s0);

    s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x8L);

    MEMORY.ref(1, s0).offset(0x3L).setu(0x1L); // 1 word
    MEMORY.ref(4, s0).offset(0x4L).setu(0xe1000205L | _800bb110.offset((a1 & 0x3L) * 4).get(0x9ffL)); // Draw mode/texpage
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x1e).getAddress(), s0);
  }

  @Method(0x80013d78L)
  public static void FUN_80013d78(final long a0, final long a1, final long a2) {
    assert false;
    //TODO
  }

  @Method(0x800145c4L)
  public static void FUN_800145c4() {
    long s0;
    long s2 = _800bb228.getAddress();
    final long s5 = _1f8003fc.get();
    long s3 = 0;
    long s1 = 0;

    //LAB_8001461c
    do {
      if(s2 == s5) {
        break;
      }

      long v1 = MEMORY.ref(1, s2).get();
      s2++;
      long a0;
      if(v1 >= 0x80L) {
        //LAB_80014654
        if(v1 < 0xe0L && v1 >= 0xa1L) {
          v1 -= 0x40L;

          //LAB_80014678
          s0 = _1f8003d4.get() - 0x44L;

          a0 = (v1 & 0xe0L) << 0x6L | (v1 & 0x1fL) << 0x3L;
          v1 = s3 - (centreScreenY_1f8003de.get() - 0xcL) - 1 << 0x10L | s1 - (centreScreenX_1f8003dc.get() - 0x4L) - 1 & 0xffffL;
          MEMORY.ref(1, s0).offset(0x3L).setu(0xcL);
          MEMORY.ref(1, s0).offset(0x37L).setu(0x3L);
          MEMORY.ref(4, s0).offset(0x38L).setu(0x7480_8080L);
          MEMORY.ref(4, s0).offset(0x28L).setu(0x7480_8080L);
          MEMORY.ref(4, s0).offset(0x1cL).setu(0x7480_8080L);
          MEMORY.ref(4, s0).offset(0x10L).setu(0x7480_8080L);
          MEMORY.ref(4, s0).offset(0x04L).setu(0x7480_8080L);
          MEMORY.ref(4, s0).offset(0x08L).setu(v1 + 1);
          MEMORY.ref(4, s0).offset(0x14L).setu(0x1_0000L | v1);
          MEMORY.ref(4, s0).offset(0x20L).setu(0x1_0002L + v1);
          MEMORY.ref(4, s0).offset(0x2cL).setu(0x2_0001L + v1);
          MEMORY.ref(4, s0).offset(0x3cL).setu(0x1_0001L + v1);
          MEMORY.ref(4, s0).offset(0x30L).setu(0x69b5_a800L | a0);
          MEMORY.ref(4, s0).offset(0x24L).setu(0x69b5_a800L | a0);
          MEMORY.ref(4, s0).offset(0x18L).setu(0x69b5_a800L | a0);
          MEMORY.ref(4, s0).offset(0x0cL).setu(0x69b5_a800L | a0);
          MEMORY.ref(4, s0).offset(0x40L).setu(0x69b4_a800L | a0);
          insertElementIntoLinkedList(tags_1f8003d0.deref().get(4).getAddress(), _1f8003d4.get() - 0x10L);
          insertElementIntoLinkedList(tags_1f8003d0.deref().get(5).getAddress(), s0);
          _1f8003d4.subu(0x44L);
        }

        //LAB_80014790
        s1 += 0x9L;

        //LAB_80014794
        if(s1 < 0x130L) {
          continue;
        }
      } else if(v1 >= 0x21L) {
        //LAB_80014670
        v1 -= 0x20L;

        //LAB_80014678
        s0 = _1f8003d4.get() - 0x44L;

        a0 = (v1 & 0xe0L) << 0x6L | (v1 & 0x1fL) << 0x3L;
        v1 = s3 - (centreScreenY_1f8003de.get() - 0xcL) - 1 << 0x10L | s1 - (centreScreenX_1f8003dc.get() - 0x4L) - 1 & 0xffffL;
        MEMORY.ref(1, s0).offset(0x3L).setu(0xcL);
        MEMORY.ref(1, s0).offset(0x37L).setu(0x3L);
        MEMORY.ref(4, s0).offset(0x38L).setu(0x7480_8080L);
        MEMORY.ref(4, s0).offset(0x28L).setu(0x7480_8080L);
        MEMORY.ref(4, s0).offset(0x1cL).setu(0x7480_8080L);
        MEMORY.ref(4, s0).offset(0x10L).setu(0x7480_8080L);
        MEMORY.ref(4, s0).offset(0x04L).setu(0x7480_8080L);
        MEMORY.ref(4, s0).offset(0x08L).setu(v1 + 1);
        MEMORY.ref(4, s0).offset(0x14L).setu(0x1_0000L | v1);
        MEMORY.ref(4, s0).offset(0x20L).setu(0x1_0002L + v1);
        MEMORY.ref(4, s0).offset(0x2cL).setu(0x2_0001L + v1);
        MEMORY.ref(4, s0).offset(0x3cL).setu(0x1_0001L + v1);
        MEMORY.ref(4, s0).offset(0x30L).setu(0x69b5_a800L | a0);
        MEMORY.ref(4, s0).offset(0x24L).setu(0x69b5_a800L | a0);
        MEMORY.ref(4, s0).offset(0x18L).setu(0x69b5_a800L | a0);
        MEMORY.ref(4, s0).offset(0x0cL).setu(0x69b5_a800L | a0);
        MEMORY.ref(4, s0).offset(0x40L).setu(0x69b4_a800L | a0);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(4).getAddress(), _1f8003d4.get() - 0x10L);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(5).getAddress(), s0);
        _1f8003d4.subu(0x44L);

        s1 += 0x9L;

        //LAB_80014794
        if(s1 < 0x130L) {
          continue;
        }
      } else if(v1 != 0xaL) {
        s1 += 0x9L;

        //LAB_80014794
        if(s1 < 0x130L) {
          continue;
        }
      } else if(s3 >= 0x101L) {
        //LAB_800147a0
        break;
      }

      // Changed this, no longer loading

      s3 += 0x9L;
      s1 = 0;
    } while(s3 < 0xe0L);

    //LAB_800147b4
    //LAB_800147b8
    s0 = _1f8003d4.get() - 0x8L;
    _1f8003d4.subu(0x8L);

    MEMORY.ref(1, s0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x4L).setu(_800bb348.get(0x9ffL) | 0xe100_0200L);
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(4).getAddress(), s0);

    s0 = _1f8003d4.get() - 0x8L;
    _1f8003d4.subu(0x8L);

    MEMORY.ref(1, s0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x4L).setu(_800bb348.get(0x9ffL) | 0xe100_0200L);
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(5).getAddress(), s0);

    _1f8003fc.setu(_800bb228.getAddress());

    //LAB_80014840
  }

  @Method(0x8001486cL)
  public static void decompressCurrentFile() {
    final FileLoadingInfo file = currentlyLoadingFileInfo_800bb468;

    if(!file.used.get()) {
      return;
    }

    if((file.unknown3.get() & 1) == 0) {
      file.used.set(false);
      return;
    }

    LOGGER.info("Decompressing file %s...", file.namePtr.deref().get());

    final long v1 = file.unknown3.get() & 0b110;

    //LAB_800148b8
    long transferDest = 0;
    //LAB_800148dc
    if(v1 == 0x2L) {
      //LAB_8001491c
      transferDest = transferDest_800bb460.deref(4).offset(-0x8L).get();
      fileSize_800bb464.setu(FUN_80017c44(fileSize_800bb464.get(), transferDest_800bb460.get(), transferDest));

      final long address = FUN_80012444(transferDest, fileSize_800bb464.get());

      //LAB_80014984
      if(address != 0) {
        transferDest = address;
      }
    } else if(v1 == 0x4L) {
      //LAB_80014954
      transferDest = transferDest_800bb460.deref(4).offset(-0x8L).get();
      fileSize_800bb464.setu(FUN_80017c44(fileSize_800bb464.get(), transferDest_800bb460.get(), transferDest));

      final long address = FUN_80012244(transferDest, fileSize_800bb464.get());

      //LAB_80014984
      if(address != 0) {
        transferDest = address;
      }
    } else if(v1 == 0) {
      //LAB_800148ec
      transferDest = file.transferDest.get();
      fileSize_800bb464.setu(FUN_80017c44(fileSize_800bb464.get(), transferDest_800bb460.get(), transferDest));
      removeFromLinkedList(transferDest_800bb460.get());
    } else {
      //LAB_80014994
      file.used.set(false);
      popFirstFileIfUnused();
      callbackIndex_8004ddc4.setu(0);
    }

    //LAB_800149a4
    //LAB_800149a8
    transferDest_800bb460.setu(transferDest);
    file.used.set(false);

    switch(file.namePtr.deref().get()) {
      case "\\OVL\\SMAP.OV_" -> MEMORY.addFunctions(SMap.class);
      case "\\OVL\\S_STRM.OV_" -> MEMORY.addFunctions(SStrm.class);
      case "\\OVL\\TTLE.OV_" -> MEMORY.addFunctions(Ttle.class);
      case "\\OVL\\S_ITEM.OV_" -> MEMORY.addFunctions(SItem.class);
      default -> throw new RuntimeException("Loaded unknown file");
    }

    //LAB_800149b4
  }

  @Method(0x800149ccL)
  public static long FUN_800149cc() {
    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(0);

    if(!file.used.get()) {
      //LAB_80014a74
      if(_8004ddd8.get() != 0x1L) {
        //LAB_80014b1c
        if(_8004ddd4.get() != 0x1L) {
          if(_8004ddd4.get() == 0x2L) {
            if(_8004ddd0.get() == _8004ddd4.get()) {
              callbackIndex_8004ddc4.setu(0xbL);
              _8004ddd0.setu(0x1L);
            } else {
              //LAB_80014b7c
              callbackIndex_8004ddc4.setu(0x7L);
              _8004ddd0.setu(_8004ddd4);
              _8004ddd4.setu(0);
            }

            return 0x1L;
          }

          return 0;
        }

        //LAB_80014b3c
        if(_8004ddd0.get() == 0x2L) {
          //LAB_80014b80
          callbackIndex_8004ddc4.setu(0xbL);
          _8004ddd0.setu(_8004ddd4);
        }

        //LAB_80014b88
        _8004ddd4.setu(0);
        return 0x1L;
      }

      if(_8004ddd0.get() == 0x2L) {
        callbackIndex_8004ddc4.setu(0xbL);
        _8004ddd0.setu(0x1L);
        return 0x1L;
      }

      //LAB_80014aac
      if(_800bf0cf.get() >= 0x2L) {
        _800bf0cf.setu(0x1L);

        //LAB_80014ad0
        setCdVolume(0, 0);
        _800bf0e0.setu(0);
        return 0x1L;
      }

      //LAB_80014ae8
      if(_800bf0d8.get() != 0) {
        //LAB_80014af8
        MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800edb8c")).cast(RunnableRef::new).run();

        //LAB_80014b00
        return 0x1L;
      }

      //LAB_80014b08
      //LAB_80014b10
      callbackIndex_8004ddc4.setu(0x1aL);
      _8004ddd0.setu(0x1L);
      return 0x1L;
    }

    if(_8004ddd0.get() == 0x2L) {
      callbackIndex_8004ddc4.setu(0xbL);
      _8004ddd0.setu(0x1L);
      return 0x1L;
    }

    //LAB_80014a10
    if(_800bf0cf.get() >= 0x2L) {
      _800bf0cf.setu(0x1L);
      setCdVolume(0, 0);
      _800bf0e0.setu(0);
      return 0x1L;
    }

    //LAB_80014a3c
    if(_800bf0d8.get() != 0) {
      MEMORY.ref(4, getMethodAddress(SMap.class, "FUN_800edb8c")).cast(RunnableRef::new).run();
      return 0x1L;
    }

    if(_8004ddcc.get() != 0) {
      return 0;
    }

    callbackIndex_8004ddc4.setu(0x1L);
    _8004ddd0.setu(0x1L);

    //LAB_80014b90
    return 0x1L;
  }

  @Method(0x80014ba0L)
  public static long FUN_80014ba0() {
    if(FUN_80036f20() != 0x1L) {
      return -0x1L;
    }

    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(0);

    fileSize_800bb48c.setu(file.size.get());
    numberOfTransfers_800bb490.setu((file.size.get() + 0x7ffL) / 0x800L);

    switch(file.unknown3.get() & 0b111) {
      case 0 -> {
        fileTransferDest_800bb488.setu(file.transferDest);
        return 0;
      }

      case 1, 4 -> {
        final long size = numberOfTransfers_800bb490.get() * 0x800L;
        final long transferDest = addToLinkedListHead(size);

        if(transferDest == 0) {
          return -0x1L;
        }

        fileTransferDest_800bb488.setu(transferDest);
        return 0;

      }

      case 5 -> {
        final long s1 = numberOfTransfers_800bb490.get() * 0x0800L;
        final long size = numberOfTransfers_800bb490.get() * 0x1000L + 0x100L;
        long transferDest = addToLinkedListHead(size);

        if(transferDest == 0) {
          return -0x1L;
        }

        transferDest += size - s1;
        MEMORY.ref(4, transferDest).offset(0xfff8L).setu(transferDest);
        MEMORY.ref(4, transferDest).offset(0xfffcL).setu(size);
        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      case 3 -> {
        final long s1 = numberOfTransfers_800bb490.get() * 0x0800L;
        final long size = numberOfTransfers_800bb490.get() * 0x1000L + 0x100L;
        long transferDest = addToLinkedListTail(size);

        //LAB_80014c98
        if(transferDest == 0) {
          return -0x1L;
        }

        transferDest += size - s1;
        MEMORY.ref(4, transferDest).offset(0xfff8L).setu(transferDest);
        MEMORY.ref(4, transferDest).offset(0xfffcL).setu(size);
        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      case 2 -> {
        final long size = numberOfTransfers_800bb490.get() * 0x800L;
        final long transferDest = addToLinkedListTail(size);

        //LAB_80014cd0
        if(transferDest == 0) {
          return -0x1L;
        }

        fileTransferDest_800bb488.setu(transferDest);
        return 0;
      }

      default -> {
        file.used.set(false);
        popFirstFileIfUnused();
        callbackIndex_8004ddc4.setu(0);
        return -0x1L;
      }
    }
  }

  @Method(0x80014d20L)
  public static void FUN_80014d20() {
    FUN_80014d50();
    decompressCurrentFile();
    executeCurrentlyLoadingFileCallback();
  }

  @Method(0x80014d50L)
  public static void FUN_80014d50() {
    if(!SInitBinLoaded_800bbad0.get()) {
      return;
    }

    callbackArray_8004dddc.get((int)callbackIndex_8004ddc4.get()).deref().run();
    FUN_8002c86c();

    //LAB_80014d94
  }

  @Method(0x80014da4L)
  public static void executeCurrentlyLoadingFileCallback() {
    final FileLoadingInfo file = currentlyLoadingFileInfo_800bb468;

    if(!file.callback.isNull()) {
      file.callback.deref().run(transferDest_800bb460, fileSize_800bb464.get(), (long)file.callbackParam.get());
      file.callback.clear();
    }
  }

  @Method(0x80014df0L)
  public static long FUN_80014df0() {
    if(FUN_80014ba0() != 0) {
      return 0;
    }

    transferIndex_800bb494.setu(0);
//    DsPacket(new CdlMODE().doubleSpeed().readEntireSector(), new CdlLOC(_800bbad8), CdlCOMMAND.READ_N_06, _80014f64.getAddress(), -0x1L);

    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get(0);

    LOGGER.info("Loading file %s", file.namePtr.deref().get());

    CDROM.readFromDisk(file.pos, (int)numberOfTransfers_800bb490.get(), fileTransferDest_800bb488.get());
    FUN_80014f64(SyncCode.COMPLETE, null);

    transferIndex_800bb494.setu(-0x1L); // Mark transfer complete
    callbackIndex_8004ddc4.setu(0x2L);

    return 1;
  }

  @Method(0x80014e54L)
  public static long FUN_80014e54() {
    if(transferIndex_800bb494.getSigned() >= 0) {
      return 0;
    }

    //TODO here this always returns 0... always resets loading? That method must get overwritten or something...

    if(FUN_80014ef4() == 0) {
      fileLoadingInfoArray_800bbad8.get(0).used.set(false);

      popFirstFileIfUnused();
      callbackIndex_8004ddc4.setu(0);
      return 0;
    }

    //LAB_80014e98
    callbackIndex_8004ddc4.setu(0x3L);

    //LAB_80014ea0
    //LAB_80014ea4
    return 0;
  }

  @Method(0x80014ef4L)
  public static long FUN_80014ef4() {
    transferDest_800bb460.setu(fileTransferDest_800bb488);
    fileSize_800bb464.setu(fileSize_800bb48c);
    currentlyLoadingFileInfo_800bb468.set(fileLoadingInfoArray_800bbad8.get(0));

    return 0;
  }

  @Method(0x80014f64L)
  public static void FUN_80014f64(final SyncCode syncCode, final byte[] responses) {
    if(syncCode == SyncCode.COMPLETE) {
      resetDmaTransfer(getMethodAddress(Scus94491BpeSegment.class, "FUN_80014fac", SyncCode.class, byte[].class), -0x1L);
    } else {
      callbackIndex_8004ddc4.setu(0x1L);
    }
  }

  @Method(0x80014facL)
  public static void FUN_80014fac(final SyncCode syncCode, final byte[] responses) {
    if(syncCode == SyncCode.DATA_READY) {
      beginCdromTransfer(fileTransferDest_800bb488.get() + transferIndex_800bb494.get() * 0x800L, 0x200L);

      transferIndex_800bb494.addu(1);

      if(transferIndex_800bb494.get() >= numberOfTransfers_800bb490.get()) {
        FUN_80036674();
        transferIndex_800bb494.setu(-0x1L);
      }
    } else {
      //LAB_80015024
      callbackIndex_8004ddc4.setu(0x1L);
    }

    //LAB_8001502c
  }

  @Method(0x800151a0L)
  public static void popFirstFileIfUnused() {
    if(fileLoadingInfoArray_800bbad8.get(0).used.get()) {
      return;
    }

    fileCount_8004ddc8.subu(0x1L);

    LOGGER.info("Removing file %s (total: %d)", fileLoadingInfoArray_800bbad8.get(0), fileCount_8004ddc8.get());

    //LAB_800151d4
    for(int i = 0; i < fileCount_8004ddc8.get(); i++) {
      final FileLoadingInfo file1 = fileLoadingInfoArray_800bbad8.get(i + 1);
      final FileLoadingInfo file2 = fileLoadingInfoArray_800bbad8.get(i);
      file2.set(file1);
    }

    //LAB_80015230
    fileLoadingInfoArray_800bbad8.get((int)fileCount_8004ddc8.get()).used.set(false);

    //LAB_80015244
  }

  @Method(0x8001524cL)
  public static long FUN_8001524c(final long param_1, final long transferDest, final long callback, final long callbackParam, final long param_5) {
    final long s0 = FUN_800155b8(MEMORY.ref(2, param_1).getSigned(), transferDest, param_5);

    if(s0 < 0) {
      return -0x1L;
    }

    final FileLoadingInfo file = addFile();
    if(file == null) {
      assert false : "File stack overflow";
      return -0x1L;
    }

    file.callback.set(MEMORY.ref(4, callback).cast(TriConsumerRef::new));
    file.transferDest.setu(transferDest);
    file.namePtr.set(MEMORY.ref(4, param_1).offset(0x4L).deref(20).cast(CString::new));
    file.callbackParam.set((int)callbackParam);
    file.unknown3.set((short)s0);
    file.used.set(true);
    setLoadingFilePosAndSizeFromFile(file, param_1);

    LOGGER.info(file);

    return 0;
  }

  @Method(0x80015310L)
  public static long FUN_80015310(final long param_1, final long param_2, final long fileTransferDest, final long callback, final long callbackParam, final long param_6) {
    final long s1 = Math.min(param_1, 2);

    //LAB_80015388
    //LAB_8001538c
    final long s2 = FUN_800155b8(_8004dda0.offset(s1 * 8).getSigned(), fileTransferDest, param_6);
    if(s2 < 0) {
      return -0x1L;
    }

    final FileLoadingInfo file = addFile();
    if(file == null) {
      //LAB_800153d4
      assert false : "File stack overflow";
      return -0x1L;
    }

    //LAB_800153dc
    file.callback.set(MEMORY.ref(4, callback).cast(TriConsumerRef::new));
    file.transferDest.setu(fileTransferDest);
    file.namePtr.set(fileNamePtr_8004dda4.offset(s1 * 8).deref(16).cast(CString::new));
    file.callbackParam.set((int)callbackParam);
    file.unknown3.set((short)s2);
    file.used.set(true);
    FUN_80015644(file, s1, param_2);

    LOGGER.info(file);

    //LAB_80015424
    return 0;
  }

  @Method(0x8001557cL)
  @Nullable
  public static FileLoadingInfo addFile() {
    if(fileCount_8004ddc8.get() >= 44L) {
      LOGGER.error("File stack overflow");
      return null;
    }

    final FileLoadingInfo file = fileLoadingInfoArray_800bbad8.get((int)fileCount_8004ddc8.get());
    fileCount_8004ddc8.addu(0x1L);

    LOGGER.info("Adding file @ %s (total: %d)", file, fileCount_8004ddc8.get());

    return file;
  }

  @Method(0x800155b8L)
  public static long FUN_800155b8(final long a0, final long fileTransferDest, long a2) {
    if(a0 < 0) {
      return -0x1L;
    }

    a2 = a2 & 0xffffffefL | 0x8L;

    //LAB_800155d0
    if(fileTransferDest == 0 && (a2 & 0x8000L) == 0) {
      //LAB_800155ec
      if((a2 & 0x6L) == 0) {
        a2 |= 0x2L;
      }

      return a2;
    }

    //LAB_800155e4
    //LAB_800155fc
    return a2 & 0xfffffff9L;
  }

  @Method(0x80015604L)
  public static void setLoadingFilePosAndSizeFromFile(final FileLoadingInfo loadingFile, final long pointerToFileIndexMaybe) {
    final CdlFILE file = CdlFILE_800bb4c8.get((int)MEMORY.ref(2, pointerToFileIndexMaybe).get());
    loadingFile.pos.set(file.pos);
    loadingFile.size.set(file.size.get());
  }

  @Method(0x80015644L)
  public static long FUN_80015644(final FileLoadingInfo file, final long a1, final long a2) {
    final long sector = CdlFILE_800bb4c8.get((int)_8004dda0.offset(a1 * 8).get()).pos.pack();

    file.pos.unpack(_800bc060.offset(a1 * 4).deref(4).offset(a2 * 8).offset(0x8L).get() + sector);
    file.size.set((int)_800bc060.offset(a1 * 4).deref(4).offset(a2 * 8).offset(0xcL).get());

    return 0;
  }

  @Method(0x800156f4L)
  public static void FUN_800156f4(final long a0) {
    _8004ddcc.setu(a0 != 0 ? 1 : 0);
  }

  @Method(0x8001575cL)
  public static void FUN_8001575c() {
    isStackPointerModified_1f8003bc.set(true);
//    oldStackPointer_1f8003b8.setu(sp);
//    sp = temporaryStack_1f8003b4.getAddress();
    FUN_80015f6c();
    FUN_8001770c();
    index_8004de4c.setu(0x9L);
    isStackPointerModified_1f8003bc.set(false);
//    sp = oldStackPointer_1f8003b8.get();
  }

  @Method(0x800157b8L)
  public static void FUN_800157b8() {
    isStackPointerModified_1f8003bc.set(true);
//    oldStackPointer_1f8003b8.setu(sp);
//    sp = temporaryStack_1f8003b4.getAddress();
    FUN_80017820();
    isStackPointerModified_1f8003bc.set(false);
//    sp = oldStackPointer_1f8003b8.get();
  }

  @Method(0x80015800L)
  public static long findFreeBiggerStructIndex() {
    index_8004de4c.addu(0x1L);

    if(index_8004de4c.get() >= 0x48L) {
      index_8004de4c.setu(0x9L);
    }

    //LAB_80015824
    //LAB_8001584c
    for(int i = (int)index_8004de4c.get(); i < 0x48; i++) {
      if(biggerStructPtrArr_800bc1c0.get(i).getPointer() == biggerStruct_800bc0c0.getAddress()) {
        //LAB_800158c0
        index_8004de4c.setu(i);
        return i;
      }
    }

    //LAB_8001586c
    //LAB_80015898
    for(int i = 0x9; i < index_8004de4c.get(); i++) {
      if(biggerStructPtrArr_800bc1c0.get(i).getPointer() == biggerStruct_800bc0c0.getAddress()) {
        //LAB_800158c0
        index_8004de4c.setu(i);
        return i;
      }
    }

    //LAB_800158b8
    return -0x1L;
  }

  @Method(0x800158ccL)
  public static long allocateBiggerStruct(final long innerStructSize) {
    final long index = findFreeBiggerStructIndex();

    if(index < 0) {
      return -0x1L;
    }

    return allocateBiggerStruct(index, innerStructSize, false, 0, 0);
  }

  /**
   * @return index, or -1 on failure to allocate memory
   */
  @Method(0x80015918L)
  public static long allocateBiggerStruct(final long index, long innerStructSize, final boolean allocateOnHead, final long a3, final long a4) {
    final long linkedListAddress;
    if(allocateOnHead) {
      linkedListAddress = addToLinkedListHead(innerStructSize + 0x100L);
    } else {
      //LAB_80015954
      linkedListAddress = addToLinkedListTail(innerStructSize + 0x100L);
    }

    //LAB_80015968
    if(linkedListAddress == 0) {
      return -0x1L;
    }

    if(innerStructSize != 0 && innerStructSize != 0x210L) {
      throw new RuntimeException("Can only handle BigStruct as inner struct");
    }

    final BiggerStruct<BigStruct> biggerStruct = MEMORY.ref(4, linkedListAddress, BiggerStruct.of(BigStruct::new));

    //LAB_80015978
    biggerStructPtrArr_800bc1c0.get((int)index).set(biggerStruct);

    if(innerStructSize != 0) {
      final BigStruct innerStruct = MEMORY.ref(4, linkedListAddress).offset(0x100L).cast(BigStruct::new);

      //LAB_800159ac
      for(int i = 0; i < innerStructSize; i += 4) {
        MEMORY.ref(4, innerStruct.getAddress()).offset(i).setu(0);
      }

      biggerStruct.innerStruct_00.set(innerStruct);
    } else {
      biggerStruct.innerStruct_00.clear();
    }

    //LAB_800159c0
    biggerStruct.ui_1c.clear();
    biggerStruct.ui_20.clear();
    biggerStruct.ui_24.clear();
    biggerStruct.ui_28.clear();
    biggerStruct.ui_2c.clear();
    biggerStruct.ui_30.clear();
    biggerStruct.ui_34.clear();
    biggerStruct.ui_38.clear();
    biggerStruct.ui_3c.clear();
    biggerStruct.ui_40.clear();
    biggerStruct.ui_44.get(0).set(index);
    biggerStruct.ui_44.get(1).set(0xffff_ffffL);
    biggerStruct.ui_44.get(2).set(0xffff_ffffL);
    biggerStruct.ui_44.get(3).set(0xffff_ffffL);
    biggerStruct.ui_44.get(4).set(0xffff_ffffL);
    biggerStruct.ui_44.get(5).set(0xffff_ffffL);
    biggerStruct.ui_44.get(6).set(0xffff_ffffL);
    biggerStruct.ui_60.set(0x080f_0000L);
    biggerStruct.ui_44.get(8).set(0xffff_ffffL);
    biggerStruct.ui_44.get(9).set(0xffff_ffffL);
    biggerStruct.ui_44.get(10).set(0xffff_ffffL);
    biggerStruct.ui_44.get(11).set(0xffff_ffffL);
    biggerStruct.ui_44.get(12).set(0xffff_ffffL);
    biggerStruct.ui_44.get(13).set(0xffff_ffffL);
    biggerStruct.ui_44.get(14).set(0xffff_ffffL);
    biggerStruct.ui_44.get(15).set(0xffff_ffffL);
    biggerStruct.ui_44.get(16).set(0xffff_ffffL);
    biggerStruct.ui_44.get(17).set(0xffff_ffffL);
    biggerStruct.ui_44.get(18).set(0xffff_ffffL);
    biggerStruct.ui_44.get(19).set(0xffff_ffffL);
    biggerStruct.ui_44.get(20).set(0xffff_ffffL);
    biggerStruct.ui_44.get(21).set(0xffff_ffffL);
    biggerStruct.ui_44.get(22).set(0xffff_ffffL);
    biggerStruct.ui_44.get(23).set(0xffff_ffffL);
    biggerStruct.ui_44.get(24).set(0xffff_ffffL);

    //LAB_800159f8
    //LAB_80015a14
    biggerStruct.ui_f8.set(a3);
    biggerStruct.ui_fc.set(a4);

    //LAB_80015a34
    return index;
  }

  @Method(0x80015a68L)
  public static void FUN_80015a68(final long index, @Nullable final TriFunctionRef<Integer, BiggerStruct<BigStruct>, BigStruct, Long> callback) {
    final BiggerStruct<?> struct = biggerStructPtrArr_800bc1c0.get((int)index).deref();

    if(callback == null) {
      //LAB_80015aa0
      struct.callback_04.clear();
      struct.ui_60.or(0x0004_0000L);
    } else {
      struct.callback_04.set(callback);
      struct.ui_60.and(0xfffb_ffffL);
    }
  }

  @Method(0x80015ab4L)
  public static void FUN_80015ab4(final long index, @Nullable final TriFunctionRef<Integer, BiggerStruct<BigStruct>, BigStruct, Long> callback) {
    final BiggerStruct<?> struct = biggerStructPtrArr_800bc1c0.get((int)index).deref();

    if(callback == null) {
      //LAB_80015aec
      struct.callback_08.clear();
      struct.ui_60.or(0x0008_0000L);
    } else {
      struct.callback_08.set(callback);
      struct.ui_60.and(0xfff7_ffffL);
    }
  }

  @Method(0x80015b00L)
  public static void FUN_80015b00(final long index, @Nullable final TriFunctionRef<Integer, BiggerStruct<BigStruct>, BigStruct, Long> callback) {
    final BiggerStruct<?> struct = biggerStructPtrArr_800bc1c0.get((int)index).deref();

    if(callback == null) {
      //LAB_80015b38
      struct.callback_0c.clear();
      struct.ui_60.or(0x0800_0000L);
    } else {
      struct.callback_0c.set(callback);
      struct.ui_60.and(0xf7ff_ffffL);
    }
  }

  @Method(0x80015b4cL)
  public static void FUN_80015b4c(final long index, @Nullable final TriFunctionRef<Integer, BiggerStruct<BigStruct>, BigStruct, Long> callback) {
    final BiggerStruct<?> struct = biggerStructPtrArr_800bc1c0.get((int)index).deref();

    if(callback == null) {
      //LAB_80015b80
      struct.callback_10.clear();
      struct.ui_60.and(0xfbff_ffffL);
    } else {
      struct.callback_10.set(callback);
      struct.ui_60.or(0x0400_0000L);
    }
  }

  @Method(0x80015b98L)
  public static void FUN_80015b98(final long index, final long addr) {
    FUN_80015bb8(index, addr, 0);
  }

  @Method(0x80015bb8L)
  public static void FUN_80015bb8(final long index, final long addr, final long a2) {
    final BiggerStruct<?> struct = biggerStructPtrArr_800bc1c0.get((int)index).deref();

    if(addr != 0) {
      struct.ui_14.set(addr);
      struct.ui_18.set(MEMORY.ref(4, addr).offset(MEMORY.ref(4, addr).offset(a2 * 0x4L)).cast(UnsignedIntRef::new));
      struct.ui_60.and(0xfffd_ffffL);
    } else {
      struct.ui_14.set(0);
      struct.ui_18.clear();
      struct.ui_60.or(0x0002_0000L);
    }
  }

  @Method(0x80015f6cL)
  public static void FUN_80015f6c() {
    long v0;
    long v1;

    if(_800bc0b9.get() != 0 || _800bc0b8.get() != 0) {
      return;
    }

    _800bc070.ui_1c.set(0);

    final long s1 = _8004de58.getAddress();

    //LAB_80015fd8
    for(int index = 0; index < 0x48; index++) {
      final BiggerStruct<BigStruct> biggerStruct = biggerStructPtrArr_800bc1c0.get(index).derefAs(BiggerStruct.classFor(BigStruct.class));

      if(biggerStruct.getAddress() != biggerStruct_800bc0c0.getAddress() && (biggerStruct.ui_60.get() & 0x12_0000L) == 0) {
        _800bc070.index_00.set(index);
        _800bc070.biggerStruct_04.set(biggerStruct);
        _800bc070.commandPtr_0c.set(biggerStruct.ui_18.deref());
        _800bc070.ui_08.set(biggerStruct.ui_18.deref());

        //LAB_80016018
        do {
          long a0 = _800bc070.commandPtr_0c.deref().get();
          _800bc070.commandPtr_0c.incr();
          _800bc070.type1_10.set(a0 & 0xffL);
          _800bc070.type2_14.set(a0 >>> 8 & 0xffL);
          _800bc070.driverAndCategory_18.set(a0 >>> 16);

          System.err.println("-- " + Long.toHexString(a0));

          if(_800bc070.type2_14.get() != 0) {
            //LAB_80016050
            for(int t2 = 0; t2 < _800bc070.type2_14.get(); t2++) {
              final long hmdCommand = _800bc070.commandPtr_0c.deref().get();
              final int category = (int)(hmdCommand >>> 24);
              final int driver = (int)(hmdCommand >>> 16 & 0xff);
              final int type2 = (int)(hmdCommand >>> 8 & 0xff);
              final int type1 = (int)(hmdCommand & 0xff);

              System.err.println(Long.toHexString(hmdCommand) + ", " + Long.toHexString(category));

              _800bc070.commandPtr_0c.incr();
              final long commandPtr = _800bc070.commandPtr_0c.getPointer();

              if(category == 0) {
                //LAB_80016574
                _800bc070.uiArr_20.get(t2).set(_800bc070.commandPtr_0c.deref()).decr();
              } else if(category == 0x1L) {
                //LAB_800161f4
                _800bc070.uiArr_20.get(t2).set(_800bc070.commandPtr_0c.deref());
                _800bc070.commandPtr_0c.incr();
              } else if(category == 0x2L) {
                //LAB_80016200
                _800bc070.uiArr_20.get(t2).set(_800bc070.biggerStruct_04.deref().ui_44.get(type1));
              } else if(category == 0x3L) {
                //LAB_800160cc
                //LAB_8001620c
                a0 = _800bc070.biggerStruct_04.deref().ui_44.get(type1).get();
                final long a1_0 = biggerStructPtrArr_800bc1c0.get((int)a0).deref().ui_44.get(type2).get();
                _800bc070.uiArr_20.get(t2).set(biggerStructPtrArr_800bc1c0.get((int)a1_0).deref().ui_44.get(driver));
              } else if(category == 0x4L) {
                //LAB_80016258
                a0 = _800bc070.biggerStruct_04.deref().ui_44.get(type1).get();
                final long a1_0 = type2 + _800bc070.biggerStruct_04.deref().ui_44.get(driver).get();
                _800bc070.uiArr_20.get(t2).set(biggerStructPtrArr_800bc1c0.get((int)a0).deref().ui_44.get((int)a1_0));
              } else if(category == 0x5L) {
                //LAB_80016290
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset(type1 * 0x4L).deref(4).cast(UnsignedIntRef::new));
              } else if(category == 0x6L) {
                //LAB_800162a4
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset((_800bc070.biggerStruct_04.deref().ui_44.get(type2).get() + type1) * 0x4L).deref(4).cast(UnsignedIntRef::new));
              } else if(category == 0x7) {
                //LAB_800162d0
                a0 = _800bc070.biggerStruct_04.deref().ui_44.get(type2).get();
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset(type1 * 0x4L).deref(4).offset(a0 * 0x4L).cast(UnsignedIntRef::new));
              } else if(category == 0x8L) {
                //LAB_800160e8
                //LAB_800162f4
                v0 = _800bc070.biggerStruct_04.deref().ui_44.get(type2).get();
                final long a1_0 = _800bc070.biggerStruct_04.deref().ui_44.get(driver).get();
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset((type1 + v0) * 0x4L).deref(4).offset(a1_0 * 0x4L).cast(UnsignedIntRef::new));
              } else if(category == 0x9L) {
                //LAB_80016328
                v1 = _800bc070.ui_08.getPointer() + (short)hmdCommand * 0x4L;
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v1, UnsignedIntRef::new));
              } else if(category == 0xaL) {
                //LAB_80016118
                //LAB_80016334
                v0 = _800bc070.ui_08.getPointer() + ((short)hmdCommand + _800bc070.biggerStruct_04.deref().ui_44.get(driver).get()) * 0x4L;
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v0, UnsignedIntRef::new));
              } else if(category == 0xbL) {
                //LAB_80016360
                v0 = _800bc070.biggerStruct_04.deref().ui_44.get(driver).get() * 0x4L;
                a0 = _800bc070.ui_08.getPointer() + (short)hmdCommand * 0x4L + MEMORY.ref(4, v0).offset(a0).get() * 0x4L; //TODO I think this is wrong, looks like neither a0 nor v0 are base addresses
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, a0, UnsignedIntRef::new));
              } else if(category == 0xcL) {
                //LAB_800163a0
                _800bc070.commandPtr_0c.incr();
                v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(_800bc070.biggerStruct_04.deref().ui_44.get(type1).get() * 0x4L).get() * 0x4L + _800bc070.biggerStruct_04.deref().ui_44.get(type2).get() * 0x4L;
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v0, UnsignedIntRef::new));
              } else if(category == 0xdL) {
                //LAB_800163e8
                _800bc070.uiArr_20.get(t2).set(biggerStructPtrArr_800bc1c0.get((int)_800bc070.biggerStruct_04.deref().ui_44.get(type1).get()).deref().ui_44.get(type2 + driver));
              } else if(category == 0xeL) {
                //LAB_80016418
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset((type2 + type1) * 0x4L).deref(4).cast(UnsignedIntRef::new));
              } else if(category == 0xfL) {
                //LAB_8001642c
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset(type1 * 0x4L).deref(4).offset(type2 * 0x4L).cast(UnsignedIntRef::new));
              } else if(category == 0x10L) {
                //LAB_80016180
                //LAB_8001643c
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset((type1 + _800bc070.biggerStruct_04.deref().ui_44.get(type2).get()) * 0x4L).deref(4).offset(driver * 0x4L).cast(UnsignedIntRef::new));
              } else if(category == 0x11L) {
                //LAB_80016468
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset(a0 * 0x4L).deref(4).offset(_800bc070.biggerStruct_04.deref().ui_44.get(driver).get() * 0x4L).cast(UnsignedIntRef::new));
              } else if(category == 0x12L) {
                //LAB_80016138
                //LAB_8001648c
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, s1).offset((type1 + type2) * 0x4L).deref(4).offset(driver * 0x4L).cast(UnsignedIntRef::new));
              } else if(category == 0x13L) {
                //LAB_800164a4
                v1 = _800bc070.ui_08.getPointer() + ((short)a0 + driver) / 4;
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v1, UnsignedIntRef::new));
              } else if(category == 0x14L) {
                //LAB_800164b4
                v1 = _800bc070.ui_08.getPointer() + (short)a0 * 0x4L;

                //LAB_800164cc
                v0 = MEMORY.ref(4, v1).offset(driver * 0x4L).get() / 4;

                //LAB_800164d4
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v1 + v0, UnsignedIntRef::new));
              } else if(category == 0x15L) {
                //LAB_800161a0
                //LAB_800164e0
                _800bc070.commandPtr_0c.incr();
                v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(_800bc070.biggerStruct_04.deref().ui_44.get(type1).get() * 0x4L).get() * 0x4L + type2 * 0x4L;

                //LAB_80016580
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v0, UnsignedIntRef::new));
              } else if(category == 0x16L) {
                //LAB_80016518
                _800bc070.commandPtr_0c.incr();
                v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(type1 * 0x4L).get() * 0x4L + _800bc070.biggerStruct_04.deref().ui_44.get(type2).get() * 0x4L;
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v0, UnsignedIntRef::new));
              } else if(category == 0x17L) {
                //LAB_800161d4
                //LAB_8001654c
                _800bc070.commandPtr_0c.incr();
                v0 = commandPtr + MEMORY.ref(4, commandPtr).offset(type1 * 0x4L).get() * 0x4L + type2 * 0x4L;
                _800bc070.uiArr_20.get(t2).set(MEMORY.ref(4, v0, UnsignedIntRef::new));
              } else {
                assert false : "Unknown category";
              }

              //LAB_80016584
            }
          }

          //LAB_80016598
          v0 = _8004e098.get((int)_800bc070.type1_10.get()).deref().run(_800bc070);

          if(v0 == 0 || v0 == 0x1L) {
            //LAB_800165e8
            _800bc070.ui_08.set(_800bc070.commandPtr_0c.deref());
          }
        } while(v0 == 0);

        //LAB_800165f4
        if(biggerStruct.getAddress() != biggerStruct_800bc0c0.getAddress()) {
          biggerStruct.ui_18.set(_800bc070.ui_08.deref());
        }
      }

      //LAB_80016614
    }

    //LAB_80016624
  }

  @Method(0x8001664cL)
  public static long FUN_8001664c(final HmdSomethingStruct a0, final long a1, final long a2, final long a3) {
    return switch((int)a3) {
      case 0x0 -> a1 <= a2 ? 1 : 0;
      case 0x1 -> a1 < a2 ? 1 : 0;
      case 0x2 -> a1 == a2 ? 1 : 0;
      case 0x3 -> a1 != a2 ? 1 : 0;
      case 0x4 -> a1 > a2 ? 1 : 0;
      case 0x5 -> a1 >= a2 ? 1 : 0;
      case 0x6 -> a1 & a2;
      case 0x7 -> (a1 & a2) == 0 ? 1 : 0;
      default -> 0;
    };
  }

  @Method(0x800166d0L)
  public static long FUN_800166d0(final HmdSomethingStruct a0) {
    return 0x1L;
  }

  @Method(0x800166e0L)
  public static long FUN_800166e0(final HmdSomethingStruct a0) {
    if(a0.uiArr_20.get(0).deref().get() != 0) {
      a0.uiArr_20.get(0).deref().sub(0x1L);
      return 0x2L;
    }

    return 0;
  }

  @Method(0x80016774L)
  public static long FUN_80016774(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).deref().set(a0.uiArr_20.get(0).deref());
    return 0;
  }

  @Method(0x80016920L)
  public static long FUN_80016920(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(1).deref().shl(a0.uiArr_20.get(0).deref());
    return 0;
  }

  @Method(0x800169d4L)
  public static long FUN_800169d4(final HmdSomethingStruct a0) {
    a0.uiArr_20.get(0).deref().incr();
    return 0;
  }

  @Method(0x80016cfcL)
  public static long FUN_80016cfc(final HmdSomethingStruct a0) {
    return _8004e29c.get((int)a0.driverAndCategory_18.get()).deref().run(a0);
  }

  @Method(0x80016d38L)
  public static long FUN_80016d38(final HmdSomethingStruct a0) {
    a0.commandPtr_0c.set(a0.uiArr_20.get(0).deref());
    return 0;
  }

  @Method(0x80016d4cL)
  public static long FUN_80016d4c(final HmdSomethingStruct a0) {
    if(FUN_8001664c(a0, a0.uiArr_20.get(0).deref().get(), a0.uiArr_20.get(1).deref().get(), a0.driverAndCategory_18.get()) != 0) {
      a0.commandPtr_0c.set(a0.uiArr_20.get(2).deref());
    }

    //LAB_80016d8c
    return 0;
  }

  @Method(0x80016da0L)
  public static long FUN_80016da0(final HmdSomethingStruct a0) {
    if(FUN_8001664c(a0, 0, a0.uiArr_20.get(0).deref().get(), a0.driverAndCategory_18.get()) != 0) {
      a0.commandPtr_0c.set(a0.uiArr_20.get(1).deref());
    }

    //LAB_80016dd8
    return 0;
  }

  @Method(0x80016e50L)
  public static long FUN_80016e50(final HmdSomethingStruct a0) {
    final BiggerStruct<?> biggerStruct = a0.biggerStruct_04.deref();

    biggerStruct.ui_40.setNullable(biggerStruct.ui_3c.derefNullable());
    biggerStruct.ui_3c.setNullable(biggerStruct.ui_38.derefNullable());
    biggerStruct.ui_38.setNullable(biggerStruct.ui_34.derefNullable());
    biggerStruct.ui_34.setNullable(biggerStruct.ui_30.derefNullable());
    biggerStruct.ui_30.setNullable(biggerStruct.ui_2c.derefNullable());
    biggerStruct.ui_2c.setNullable(biggerStruct.ui_28.derefNullable());
    biggerStruct.ui_28.setNullable(biggerStruct.ui_24.derefNullable());
    biggerStruct.ui_24.setNullable(biggerStruct.ui_20.derefNullable());
    biggerStruct.ui_20.setNullable(biggerStruct.ui_1c.derefNullable());

    biggerStruct.ui_1c.set(a0.commandPtr_0c.deref());
    a0.commandPtr_0c.set(a0.uiArr_20.get(0).deref());
    return 0;
  }

  @Method(0x80016f28L)
  public static long FUN_80016f28(final HmdSomethingStruct a0) {
    a0.commandPtr_0c.set(a0.biggerStruct_04.deref().ui_1c.deref());
    a0.biggerStruct_04.deref().ui_1c.setNullable(a0.biggerStruct_04.deref().ui_20.derefNullable());
    a0.biggerStruct_04.deref().ui_20.setNullable(a0.biggerStruct_04.deref().ui_24.derefNullable());
    a0.biggerStruct_04.deref().ui_24.setNullable(a0.biggerStruct_04.deref().ui_28.derefNullable());
    a0.biggerStruct_04.deref().ui_28.setNullable(a0.biggerStruct_04.deref().ui_2c.derefNullable());
    a0.biggerStruct_04.deref().ui_2c.setNullable(a0.biggerStruct_04.deref().ui_30.derefNullable());
    a0.biggerStruct_04.deref().ui_30.setNullable(a0.biggerStruct_04.deref().ui_34.derefNullable());
    a0.biggerStruct_04.deref().ui_34.setNullable(a0.biggerStruct_04.deref().ui_38.derefNullable());
    a0.biggerStruct_04.deref().ui_38.setNullable(a0.biggerStruct_04.deref().ui_3c.derefNullable());
    a0.biggerStruct_04.deref().ui_3c.setNullable(a0.biggerStruct_04.deref().ui_40.derefNullable());
    a0.biggerStruct_04.deref().ui_40.clear();
    return 0;
  }

  @Method(0x80016ffcL)
  public static long FUN_80016ffc(final HmdSomethingStruct a0) {
    a0.biggerStruct_04.deref().ui_40.setNullable(a0.biggerStruct_04.deref().ui_3c.derefNullable());
    a0.biggerStruct_04.deref().ui_3c.setNullable(a0.biggerStruct_04.deref().ui_38.derefNullable());
    a0.biggerStruct_04.deref().ui_38.setNullable(a0.biggerStruct_04.deref().ui_34.derefNullable());
    a0.biggerStruct_04.deref().ui_34.setNullable(a0.biggerStruct_04.deref().ui_30.derefNullable());
    a0.biggerStruct_04.deref().ui_30.setNullable(a0.biggerStruct_04.deref().ui_2c.derefNullable());
    a0.biggerStruct_04.deref().ui_2c.setNullable(a0.biggerStruct_04.deref().ui_28.derefNullable());
    a0.biggerStruct_04.deref().ui_28.setNullable(a0.biggerStruct_04.deref().ui_24.derefNullable());
    a0.biggerStruct_04.deref().ui_24.setNullable(a0.biggerStruct_04.deref().ui_20.derefNullable());
    a0.biggerStruct_04.deref().ui_20.setNullable(a0.biggerStruct_04.deref().ui_1c.derefNullable());
    a0.biggerStruct_04.deref().ui_1c.set(a0.commandPtr_0c.deref());

    final long v1 = a0.uiArr_20.get(1).getPointer();
    a0.commandPtr_0c.set(MEMORY.ref(4, v1 + MEMORY.ref(4, v1).offset(a0.uiArr_20.get(0).deref().get() * 0x4L).get() * 0x4L, UnsignedIntRef::new));
    return 0;
  }

  @Method(0x80017354L)
  public static long FUN_80017354(final HmdSomethingStruct a0) {
    _800bb0ab.setu(a0.uiArr_20.get(0).deref().get() != 0 ? 1 : 0);
    return 0;
  }

  @Method(0x80017390L)
  public static long FUN_80017390(final HmdSomethingStruct a0) {
    final long a1 = a0.uiArr_20.get(0).deref().get() & 0x1fL;
    final long a2 = a0.uiArr_20.get(0).deref().get() >>> 5;

    if(a0.uiArr_20.get(1).deref().get() != 0) {
      _800bad04.offset(a2 * 0x4L).oru(0x1L << a1);
    } else {
      //LAB_800173dc
      _800bad04.offset(a2 * 0x4L).and(~(0x1L << a1));
    }

    //LAB_800173f4
    return 0;
  }

  @Method(0x800173fcL)
  public static long FUN_800173fc(final HmdSomethingStruct a0) {
    final long a2 = a0.uiArr_20.get(0).deref().get() & 0x1fL;
    final long v0 = a0.uiArr_20.get(0).deref().get() >>> 5;

    a0.uiArr_20.get(1).deref().set(_800bad04.offset(v0 * 0x4L).get(0x1L << a2) != 0 ? 1 : 0);

    return 0;
  }

  @Method(0x800174d8L)
  public static long FUN_800174d8(final HmdSomethingStruct a0) {
    final long v0 = _800bac84.offset((a0.uiArr_20.get(0).deref().get() >>> 5) * 0x4L).get(0x1L << (a0.uiArr_20.get(0).deref().get() & 0x1fL)) != 0 ? 1 : 0;
    a0.uiArr_20.get(1).deref().set(v0);
    return 0;
  }

  @Method(0x800175b4L)
  public static long FUN_800175b4(final HmdSomethingStruct a0) {
    final long a2 = a0.uiArr_20.get(1).deref().get();
    final long a1 = 0x1L << (a2 & 0x1fL);
    if(a0.uiArr_20.get(2).deref().get() != 0) {
      MEMORY.ref(4, a0.uiArr_20.get(0).getPointer()).offset((a2 >>> 5) * 0x4L).oru(a1);
    } else {
      //LAB_800175fc
      MEMORY.ref(4, a0.uiArr_20.get(0).getPointer()).offset((a2 >>> 5) * 0x4L).and(~a1);
    }

    //LAB_80017614
    if(_800bad64.getSigned() < 0) {
      _800baf07.setu(0x5L);
      _800baf02.setu(0x7fffL);
    }

    //LAB_80017640
    return 0;
  }

  @Method(0x8001770cL)
  public static void FUN_8001770c() {
    if(_800bc0b8.get() != 0 || _800bc0b9.get() != 0) {
      return;
    }

    //LAB_80017750
    for(int i = 0; i < 0x48; i++) {
      final BiggerStruct<BigStruct> biggerStruct = (BiggerStruct<BigStruct>)biggerStructPtrArr_800bc1c0.get(i).deref();
      if(biggerStruct.getAddress() != biggerStruct_800bc0c0.getAddress()) {
        if((biggerStruct.ui_60.get() &0x140000L) == 0) {
          biggerStruct.callback_04.deref().run(i, biggerStruct, biggerStruct.innerStruct_00.deref());
        }
      }

      //LAB_80017788
    }

    //LAB_800177ac
    for(int i = 0; i < 0x48; i++) {
      final BiggerStruct<BigStruct> biggerStruct = (BiggerStruct<BigStruct>)biggerStructPtrArr_800bc1c0.get(i).deref();
      if(biggerStruct.getAddress() != biggerStruct_800bc0c0.getAddress()) {
        if((biggerStruct.ui_60.get() & 0x410_0000L) == 0x400_0000L) {
          if(biggerStruct.callback_10.deref().run(i, biggerStruct, biggerStruct.innerStruct_00.deref()) != 0) {
            FUN_80015b4c(i, null);
          }
        }
      }

      //LAB_800177f8
    }

    //LAB_80017808
  }

  @Method(0x80017820L)
  public static void FUN_80017820() {
    if(_800bc0b9.get() != 0) {
      return;
    }

    //LAB_80017854
    for(int i = 0; i < 0x48; i++) {
      final BiggerStruct<BigStruct> biggerStruct = (BiggerStruct<BigStruct>)biggerStructPtrArr_800bc1c0.get(i).deref();
      if(biggerStruct.getAddress() != biggerStruct_800bc0c0.getAddress()) {
        if((biggerStruct.ui_60.get() & 0x18_0000L) == 0) {
          biggerStruct.callback_08.deref().run(i, biggerStruct, biggerStruct.innerStruct_00.deref());
        }
      }

      //LAB_80017888
    }

    //LAB_80017898
  }

  @Method(0x800178b0L)
  public static void loadSceaLogo() {
    if(SInitBinLoaded_800bbad0.get()) {
      FUN_80015310(0, 0x1669L, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_80017924", Value.class, long.class, long.class), 1, 5);
    } else {
      loadSceaLogoTexture(sceaTexture_800d05c4, 0, 0);
    }
  }

  @Method(0x80017924L)
  public static void loadSceaLogoTexture(final Value address, final long param_2, final long param_3) {
    timHeader_800bc2e0.set(parseTimHeader(address.offset(0x4L)));
    final TimHeader header = timHeader_800bc2e0;

    final RECT imageRect = new RECT();
    imageRect.set((short)640, (short)0, header.getImageRect().w.get(), header.getImageRect().h.get());
    LoadImage(imageRect, header.getImageAddress());

    if(header.hasClut()) {
      final RECT clutRect = new RECT();
      clutRect.set((short)640, (short)255, header.getClutRect().w.get(), header.getClutRect().h.get());
      LoadImage(clutRect, header.getClutAddress());
    }

    _800bc300.setu(_800bb120).oru(0xaL);
    _800bc304.setu(_800bb120).oru(0xcL);
    _800bc308.setu(0x3fe8L);

    if(param_3 != 0) {
      FUN_800127cc(address, 0, 1, 0, 0);
    }
  }

  /**
   * Draws the TIM image located at {@link Scus94491BpeSegment_800b#timHeader_800bc2e0}
   *
   * NOTE: elements are added in reverse order
   */
  @Method(0x80017a3cL)
  public static void drawTim(final long colour) {
    final TimHeader tim = timHeader_800bc2e0;

    final Value c0 = linkedListAddress_1f8003d8.deref(4);
    c0.offset(1, 0x03L).setu(0x4L); // OT element size
    c0.offset(1, 0x04L).setu(colour); // R
    c0.offset(1, 0x05L).setu(colour); // G
    c0.offset(1, 0x06L).setu(colour); // B
    c0.offset(1, 0x07L).setu(0x64L); // Textured rectangle, variable size, opaque, texture-blending
    c0.offset(2, 0x08L).setu(-tim.getImageRect().w.get()); // X
    c0.offset(2, 0x0aL).setu(-tim.getImageRect().h.get() / 2); // Y
    c0.offset(1, 0x0cL).setu(0); // TX
    c0.offset(1, 0x0dL).setu(0); // TY
    c0.offset(2, 0x0eL).setu(_800bc308); // CLUT
    c0.offset(2, 0x10L).setu(0x100L); // W
    c0.offset(2, 0x12L).setu(tim.getImageRect().h.get()); // H
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x29).getAddress(), c0.getAddress());

    final Value c1 = linkedListAddress_1f8003d8.deref(4).offset(0x28L);
    c1.offset(1, 0x03L).setu(0x1L); // OT element size
    // Draw mode (texpage), forces dithering and gets the following values from memory:
    // 0-3 texture page x base (n*64)
    // 4   texture page y base (n*256)
    // 5-6 semi-transparency
    // 7-8 texture page colors (0=4-bit, 1=8-bit, 2=15-bit, 3=reserved)
    // 11  texture disable (0=normal, 1=disable if GP1(09h).bit0==1)
    c1.offset(4, 0x04L).setu(0xe1000200L | _800bc300.get(0x9ffL));
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x29).getAddress(), c1.getAddress());

    final Value c2 = linkedListAddress_1f8003d8.deref(4).offset(0x14L);
    c2.offset(1, 0x03L).setu(0x4L); // OT element size
    c2.offset(1, 0x04L).setu(colour); // R
    c2.offset(1, 0x05L).setu(colour); // G
    c2.offset(1, 0x06L).setu(colour); // B
    c2.offset(1, 0x07L).setu(0x64L); // Textured rectangle, variable size, opaque, texture-blending
    c2.offset(2, 0x08L).setu(-tim.getImageRect().w.get() + 0x100L); // X
    c2.offset(2, 0x0aL).setu(-tim.getImageRect().h.get() / 2); // Y
    c2.offset(1, 0x0cL).setu(0); // TX
    c2.offset(1, 0x0dL).setu(0); // TY
    c2.offset(2, 0x0eL).setu(_800bc308); // CLUT
    c2.offset(2, 0x10L).setu(tim.getImageRect().w.get() * 2 - 0x100L); // W
    c2.offset(2, 0x12L).setu(tim.getImageRect().h.get()); // H
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x29).getAddress(), c2.getAddress());

    final Value c3 = linkedListAddress_1f8003d8.deref(4).offset(0x30L);
    c3.offset(1, 0x03L).setu(0x1L); // OT element size
    // Draw mode (texpage), forces dithering and gets the following values from memory:
    // 0-3 texture page x base (n*64)
    // 4   texture page y base (n*256)
    // 5-6 semi-transparency
    // 7-8 texture page colors (0=4-bit, 1=8-bit, 2=15-bit, 3=reserved)
    // 11  texture disable (0=normal, 1=disable if GP1(09h).bit0==1)
    c3.offset(4, 0x04L).setu(0xe1000200L | _800bc304.get(0x9ffL));
    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x29).getAddress(), c3.getAddress());

    linkedListAddress_1f8003d8.addu(0x38L);
  }

  @Method(0x80017c44L)
  public static long FUN_80017c44(final long unused, final long archiveAddress, final long destinationAddress) {
//    MEMORY.ref(4, sp).offset(0x10L).setu(sp);

    final Memory.TemporaryReservation size = MEMORY.temp();

    //LAB_80017c94
//    if(!isStackPointerModified_1f8003bc.get()) {
      //LAB_80017cc4
//      FUN_80017ce0(archiveAddress, destinationAddress, size.address);
//    } else if(sp > _1f800300.getAddress()) {
      FUN_80017d28(archiveAddress, destinationAddress, size.address);
//    } else if(sp > _1f800200.getAddress()) {
//      FUN_80017d58(archiveAddress, destinationAddress, size.address);
//    } else {
    //LAB_80017cb4
//      FUN_80017d8c(archiveAddress, destinationAddress, size.address);
//    }

    final long ret = size.get().get();
    size.release();

    //LAB_80017ccc
    return ret;
  }

  @Method(0x80017ce0L)
  public static void FUN_80017ce0(final long archiveAddress, final long destinationAddress, final long sizePtr) {
    isStackPointerModified_1f8003bc.set(true);
//    oldStackPointer_1f8003b8.setu(sp);
//    sp = temporaryStack_1f8003b4.getAddress();
    FUN_80017d28(archiveAddress, destinationAddress, sizePtr);
    isStackPointerModified_1f8003bc.set(false);
//    sp = oldStackPointer_1f8003b8.get();
  }

  @Method(0x80017d28L)
  public static void FUN_80017d28(final long archiveAddress, final long destinationAddress, final long sizePtr) {
    MEMORY.ref(4, sizePtr).setu(decompress(archiveAddress, destinationAddress));
  }

  @Method(0x80017d58L)
  public static void FUN_80017d58(final long archiveAddress, final long destinationAddress, final long sizePtr) {
    assert false;
    //TODO
  }

  @Method(0x80017d8cL)
  public static void FUN_80017d8c(final long archiveAddress, final long destinationAddress, final long sizePtr) {
    assert false;
    //TODO
  }

  @Method(0x80017f94L)
  public static void FUN_80017f94() {
    isStackPointerModified_1f8003bc.set(true);
//    oldStackPointer_1f8003b8.setu(sp);
//    sp = temporaryStack_1f8003b4.getAddress();
    FUN_80017fdc();

    isStackPointerModified_1f8003bc.set(false);
//    sp = oldStackPointer_1f8003b8.get();
  }

  @Method(0x80017fdcL)
  public static void FUN_80017fdc() {
    // empty
  }

  @Method(0x800184b0L)
  public static void processControllerInput() {
    int a0 = (int)_8007a3b8.get();

    if(a0 == 0) {
      a0 = 1;
    }

    FUN_8002ae0c(a0);

    _8007a398.setu(_800bee94);
    _8007a39c.setu(_800bee90);
    _8007a3a0.setu(_800bee98);
  }

  @Method(0x800194dcL)
  public static void FUN_800194dc() {
    FUN_800fbec8(_8004f65c.getAddress());
  }

  @Method(0x80019500L)
  public static void FUN_80019500() {
    FUN_8004b834();
    FUN_8004ccb0(0, 0);
    FUN_8004c3f0(0x8L);
    FUN_8004c494(0x3L);
    FUN_8004c558(0x30L, 0x30L);

    //LAB_80019548
    for(int i = 0; i < 0xd; i++) {
      FUN_8001aa44(i);
    }

    FUN_8001aa64();
    FUN_8001aa78();
    FUN_8001aa90();

    //LAB_80019580
    for(int i = 0; i < 0x21; i++) {
      _800bd110.offset(i * 0x28L).setu(0);
      _800bd12c.offset(i * 0x28L).setu(0);
    }

    //LAB_800195a8
    for(int i = 0; i < 0xd; i++) {
      _800bcf64.offset(i * 0x1cL).setu(0); //2
    }

    //LAB_800195c8
    for(int i = 0; i < 7; i++) {
      _800bd600.offset(i * 0x10L).setu(0); //2
    }

    _800bd100.setu(0x100L);
    _800bd780.setu(0);
    _800bd781.setu(0);
  }

  @Method(0x80019710L)
  public static void FUN_80019710() {
    if(_8004dd20.get() != 0x5L && _8004dd28.get() == 0x5L) {
      FUN_80020008();
      removeFromLinkedList(linkedListEntry_800bd784.get());
      removeFromLinkedList(linkedListEntry_800bd788.get());

      _800bd780.setu(0);
    }

    //LAB_8001978c
    //LAB_80019790
    if(_8004dd20.get() != 0x6L && _8004dd28.get() == 0x6L) {
      FUN_80020008();

      //LAB_800197c0
      for(long i = 0; i < 3; i++) {
        removeFromLinkedList(linkedListEntry_800bc984.offset(i * 12).get());
      }

      if(_800bd780.get() == 0x1L) {
        removeFromLinkedList(linkedListEntry_800bd784.get());
        removeFromLinkedList(linkedListEntry_800bd788.get());
        _800bd780.setu(0);
      }
    }

    //LAB_80019824
    //LAB_80019828
    //switchD
    switch((int)_8004dd20.get()) {
      case 2:
        setMainVolume(0x7fL, 0x7fL);
        FUN_80020008();
        FUN_8001aa90();

        if(drgnBinIndex_800bc058.get() == 0x1L) {
          FUN_8001f3d0(0x1L, 0);
        } else {
          FUN_8001f3d0(0x62L, 0);
        }

        break;

      case 5:
        FUN_80020008();

        if(_800bd780.get() == 0x1L) {
          break;
        }

        linkedListEntry_800bd784.setu(addToLinkedListTail(0x650L));
        linkedListEntry_800bd788.setu(addToLinkedListTail(0x5c30L));
        _800bd780.setu(0x1L);
        break;

      case 6:
        FUN_80020008();
        final long s4 = (FUN_8001a810() << 1) - 0x1L;

        //LAB_800198e8
        for(long i = 0; i < 3; i++) {
          if(i == 0) {
            _800bc981.set(0);
          } else {
            //LAB_800198f8
            _800bc981.offset(i * 12).setu(_8004f664.offset(i).offset(s4));
          }

          //LAB_80019908
          linkedListEntry_800bc984.offset(i * 12).set(addToLinkedListTail(_8004f6a4.offset(_800bc981.offset(i * 12).get() * 4).get()));
          _800bc988.offset(i * 12).set(_8004f6a4.offset(_800bc981.offset(i * 12).get() * 4));
        }

        if(_800bd780.get() == 0x1L || _800bb0f8.get() != 0x1bbL) {
          break;
        }

        //LAB_80019978
        linkedListEntry_800bd784.setu(addToLinkedListTail(0x650L));
        linkedListEntry_800bd788.setu(addToLinkedListTail(0x5c30L));
        _800bd780.setu(0x1L);
        break;

      case 0xf:
        FUN_8004d91c(0x1L);
        FUN_8004d034(_800bd0f8.get(), 0);
        FUN_8004c390(_800bd0f8.get());
        FUN_8004c114(_800bd0c4.get());
        FUN_8004c114(_800bcf90.get());
        break;

      case 0xc:
        FUN_80020008();

        //LAB_80019a00
        FUN_8001f3d0(60L, 0);
        break;

      case 0xa:
        FUN_8001e29c(0);
        // Fall through intended

      case 0x4:
      case 0x7:
      case 0x8:
      case 0x9:
      case 0xb:
        FUN_80020008();
        break;

      case 0xd:
      case 0xe:
      case 0x3:
    }

    //LAB_80019a24
    if(_8004dd20.get() != 0x5L) {
      _800bd808.setu(-0x1L);
    }

    //LAB_80019a3c
  }

  @Method(0x80019a60L)
  public static void FUN_80019a60(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5) {
    if(_800bcf80.offset(a0 * 28).get() == 0 || _800bcf90.offset(a0 * 28).getSigned() == -0x1L) {
      return;
    }

    switch((int)a0) {
      case 0x0:
        if(_800bcf78.get(0x1L) != 0) {
          return;
        }

        break;

      case 0x8:
        if(_800bcf78.get(0x2L) != 0) {
          return;
        }

        if(_8004dd20.get() != 0x5L) {
          return;
        }

        break;

      case 0x9:
        if(_800bcf78.get(0x4L) != 0) {
          return;
        }

        if(_8004dd20.get() != 0x6L) {
          return;
        }

        break;

      case 0xa:
        if(_800bcf78.get(0x20L) != 0) {
          return;
        }

        break;

      case 0xc:
        if(_800bcf78.get(0x8000L) != 0) {
          return;
        }

        //LAB_80019bd4
        if(_8004dd20.get() != 0x8L) {
          return;
        }

        break;
    }

    //LAB_80019be0
    //LAB_80019c00
    for(int i = 0; i < 0x20L; i++) {
      if(_800bd110.offset(i * 0x28L).get() == 0) {
        if(a0 == 0x8L) {
          //LAB_80019b54
          FUN_8001a714(0x3L, 0x8L, a1, i, _800bd060.offset(2, 0x10L).get(), _800bd060.offset(4, 0x8L).get() + a1 * 2, _800bd060.offset(4, 0xcL).deref(1).offset(a1).get(), -0x1L, -0x1L, -0x1L, a5, a4, -0x1L);
        } else {
          FUN_8001a714(0x3L, a0, a1, i, _800bcf90.offset(a0 * 28).get(), _800bcf88.offset(a0 * 28).get() + a1 * 2, 0, -0x1L, -0x1L, -0x1L, a5, a4, -0x1L);
        }

        return;
      }

      //LAB_80019c60
    }

    //LAB_80019c70
  }

  @Method(0x8001a4e8L)
  public static void FUN_8001a4e8() {
    long s0 = _800bd110.getAddress();
    long s1 = 0;

    //LAB_8001a50c
    do {
      long v0 = MEMORY.ref(1, s0).get();
      if(v0 != 0 && v0 != 0x4L) {
        if(MEMORY.ref(2, s0).offset(0x24L).get() == 0) {
          //LAB_8001a564
          if(MEMORY.ref(2, s0).offset(0x20L).get() == 0) {
            MEMORY.ref(2, s0).offset(0x22L).subu(0x1L);
            v0 = MEMORY.ref(2, s0).offset(0x22L).getSigned() << 0x10L;

            if((int)v0 <= 0) {
              FUN_8001a5fc(s1);

              if(MEMORY.ref(2, s0).offset(0x20L).get() != 0) {
                MEMORY.ref(2, s0).offset(0x22L).setu(MEMORY.ref(2, s0).offset(0x20L));
              }
            }
          } else {
            //LAB_8001a5b0
            FUN_8001a5fc(s1);

            if(MEMORY.ref(2, s0).offset(0x1cL).get() == 0) {
              //LAB_8001a5d0
              MEMORY.ref(1, s0).setu(0);
            } else {
              MEMORY.ref(1, s0).setu(0x4L);
            }
          }
        } else {
          MEMORY.ref(2, s0).offset(0x24L).subu(0x1L);
          v0 = MEMORY.ref(2, s0).offset(0x24L).get() << 0x10L;

          if((int)v0 <= 0) {
            FUN_8001a5fc(s1);

            MEMORY.ref(2, s0).offset(0x24L).setu(0);
            if(MEMORY.ref(2, s0).offset(0x20L).get() == 0) {
              MEMORY.ref(1, s0).setu(0);
            }
          }
        }
      }

      //LAB_8001a5d4
      s1++;
      s0 += 0x28L;
    } while(s1 < 0x20L);
  }

  @Method(0x8001a5fcL)
  public static void FUN_8001a5fc(final long a0) {
    final long s0;

    if(_800bd126.offset(a0 * 40).getSigned() == -0x1L && _800bd128.offset(a0 * 40).getSigned() == -0x1L && _800bd12a.offset(a0 * 40).getSigned() == -0x1L) {
      s0 = (short)FUN_8004d648(
        _800bd120.offset(a0 * 40).getSigned(),
        _800bd122.offset(a0 * 40).getSigned(),
        _800bd124.offset(a0 * 40).getSigned()
      );
    } else {
      s0 = (short)FUN_8004d6a8(
        _800bd120.offset(a0 * 40).getSigned(),
        _800bd122.offset(a0 * 40).getSigned(),
        _800bd124.offset(a0 * 40).getSigned(),
        _800bd128.offset(a0 * 40).getSigned(),
        _800bd126.offset(a0 * 40).getSigned(),
        _800bd12a.offset(a0 * 40).getSigned()
      );
    }

    if(s0 != -0x1L) {
      _800bc9aa.offset(s0 * 8).set(_800bd118.offset(a0 * 40));
      _800bc9ab.offset(s0 * 8).set(_800bd11c.offset(a0 * 40));
      _800bc9ac.offset(s0 * 8).set(_800bd114.offset(a0 * 40));
    }

    //LAB_8001a704
  }

  @Method(0x8001a714L)
  public static void FUN_8001a714(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8, final long a9, final long a10, final long a11, final long a12) {
    final long v0 = _800bd110.offset((a3 * 5) << 0x3L).getAddress();
    MEMORY.ref(1, v0).offset(0x00L).setu(a0);
    MEMORY.ref(4, v0).offset(0x04L).setu(a12);
    MEMORY.ref(4, v0).offset(0x08L).setu(a1);
    MEMORY.ref(4, v0).offset(0x0cL).setu(a2);
    MEMORY.ref(2, v0).offset(0x10L).setu(a4);
    MEMORY.ref(2, v0).offset(0x12L).setu(MEMORY.ref(1, a5));
    MEMORY.ref(2, v0).offset(0x14L).setu(MEMORY.ref(1, a5).offset(0x1L));
    MEMORY.ref(2, v0).offset(0x16L).setu(a7);
    MEMORY.ref(2, v0).offset(0x18L).setu(a8);
    MEMORY.ref(2, v0).offset(0x1aL).setu(a9);
    MEMORY.ref(4, v0).offset(0x1cL).setu(a6 & 0xffL);
    MEMORY.ref(2, v0).offset(0x20L).setu(a10);
    MEMORY.ref(2, v0).offset(0x22L).setu(0x1L);
    MEMORY.ref(2, v0).offset(0x24L).setu(a11);

    long a2_1 = _800bd680.offset(0x3cL).getAddress();
    long a1_1 = 0x50L;
    long t5 = 0x3L;

    //LAB_8001a7b4
    do {
      long v1 = _800bd680.offset(a1_1).getAddress();
      MEMORY.ref(4, v1).offset(0x00L).setu(MEMORY.ref(4, a2_1).offset(0x00L));
      MEMORY.ref(4, v1).offset(0x04L).setu(MEMORY.ref(4, a2_1).offset(0x04L));
      MEMORY.ref(4, v1).offset(0x08L).setu(MEMORY.ref(4, a2_1).offset(0x08L));
      MEMORY.ref(4, v1).offset(0x0cL).setu(MEMORY.ref(4, a2_1).offset(0x0cL));
      MEMORY.ref(4, v1).offset(0x10L).setu(MEMORY.ref(4, a2_1).offset(0x10L));
      a2_1 -= 0x14L;
      a1_1 -= 0x14L;
      t5--;
    } while((int)t5 >= 0);

    _800bd680.setu(a1);
    _800bd684.setu(a2);
    _800bd690.setu(a6 & 0xffL);
  }

  @Method(0x8001a810L)
  public static long FUN_8001a810() {
    assert false;
    //TODO
    return 0;
  }

  @Method(0x8001aa24L)
  public static void FUN_8001aa24() {
    FUN_8001a4e8();
  }

  @Method(0x8001aa44L)
  public static void FUN_8001aa44(final long a0) {
    _800bcf80.offset(a0 * 0x1c).setu(0);
  }

  @Method(0x8001aa64L)
  public static void FUN_8001aa64() {
    _800bd0f0.setu(0);
    _800bd6f8.setu(0);
  }

  @Method(0x8001aa78L)
  public static void FUN_8001aa78() {
    _800bca68.setu(0);
    _800bca6c.setu(0x7f00L);
  }

  @Method(0x8001aa90L)
  public static void FUN_8001aa90() {
    //LAB_8001aaa4
    for(int i = 0; i < 0x18; i++) {
      _800bc9a0.offset(i * 0x8L).setu(0xffffL);
    }
  }

  @Method(0x8001ab34L)
  public static long FUN_8001ab34(final HmdSomethingStruct a0) {
    FUN_80019a60(a0.uiArr_20.get(0).deref().get(), a0.uiArr_20.get(1).deref().get(), a0.uiArr_20.get(2).deref().get(), a0.uiArr_20.get(3).deref().get(), a0.uiArr_20.get(4).deref().get(), a0.uiArr_20.get(5).deref().get());
    return 0;
  }

  @Method(0x8001ad18L)
  public static void FUN_8001ad18() {
    //LAB_8001ad2c
    for(long a0 = 0; a0 < 32; a0++) {
      _800bd110.offset(a0 * 40).setu(0);
      _800bd12c.offset(a0 * 40).setu(0);
    }

    FUN_8004d91c(0x1L);
  }

  @Method(0x8001ada0L)
  public static void FUN_8001ada0() {
    FUN_8004cf8c(_800bd0f8.get());
  }

  @Method(0x8001ae90L)
  public static void FUN_8001ae90() {
    if(_800bd0f0.get() == 0x2L) {
      FUN_8004d034(_800bd0f8.get(), 0);
    }
  }

  @Method(0x8001b1a8L)
  public static void FUN_8001b1a8(final long a0) {
    FUN_8004c8dc(_800bd0f8.get(), (short)a0);
    _800bd108.setu(a0);
  }

  @Method(0x8001b33cL)
  public static long FUN_8001b33c(final HmdSomethingStruct a0) {
    FUN_8004d41c(_800bd0f0.offset(2, 0x8L).getSigned(), (short)a0.uiArr_20.get(0).deref().get(), (short)a0.uiArr_20.get(1).deref().get());
    _800bd0f0.offset(2, 0x18L).setu(a0.uiArr_20.get(1).deref().get());
    return 0;
  }

  @Method(0x8001b3e4L)
  public static long FUN_8001b3e4() {
    long v0 = _800bd0b4.get();

    if(v0 != 0) {
      return _800bd0b6.get();
    }

    //LAB_8001b408
    return v0;
  }

  @Method(0x8001b410L)
  public static void FUN_8001b410() {
    if(_8004f6e4.getSigned() == -0x1L) {
      return;
    }

    FUN_8001b54c();

    if(_8004dd08.get() == 0x1L) {
      return;
    }

    long v0 = _800bd740.get();

    if(v0 >= 0) {
      v0--;
      _800bd740.setu(v0);
      return;
    }

    //LAB_8001b460
    if(_800bd700.get() != 0) {
      FUN_8001c5bc();
    }

    //LAB_8001b480
    if(_800bc960.get(0x2L) != 0) {
      if(_8004f6ec.get() == 0) {
        _8004f6ec.setu(0x1L);
        FUN_8001c594(0x1L, 0x6L);
        FUN_800136dc(0x1L, 0x1L);
      }
    }

    //LAB_8001b4c0
    if(_8004f6ec.get() != 0) {
      //LAB_8001b4d4
      if(_8004f6ec.get() >= 0x7L) {
        if(_8004dd08.get() == 0) {
          _8004f6e4.setu(-0x1L);
          _800bc960.oru(0x1L);
        }
      }

      //LAB_8001b518
      _8004f6ec.addu(0x1L);
    }

    //LAB_8001b528
    _8004f6e8.addu(0x1L);

    //LAB_8001b53c
  }

  @Method(0x8001b54cL)
  public static void FUN_8001b54c() {
    FUN_8001b92c();

    long v0 = -_1f8003e0.get();
    v0 += v0 >>> 0x1fL;
    v0 >>= 0x1L;
    long sp10x4 = v0;

    v0 = -_1f8003e4.get();
    v0 += v0 >>> 0x1fL;
    v0 >>= 0x1L;
    long sp14x4 = v0;

    long a0 = _1f8003e4.getSigned();
    if(_1f8003e4.getSigned() < 0) {
      a0 += 0x7L;
    }

    //LAB_8001b5c0
    a0 >>= 0x3L;
    if(0x64L / a0 == _800bd714.get()) {
      _800bd714.setu(0);
      _800bd710.addu(0x1L);

      final long v1 = a0 - 0x1L;
      if(v1 < _800bd710.get()) {
        _800bd710.setu(v1);
      }
    }

    //LAB_8001b608
    long sp18x4 = 0;
    long sp30x4 = 0x200L;

    //LAB_8001b620
    do {
      long sp1cx4 = 0;
      long sp24x4 = sp30x4 * 0x100L;
      long sp28x4 = sp30x4 * 0x100L + 0x8L;
      long sp2cx4 = sp10x4;

      long s5 = _1f8003e4.get() - (_800bd710.get() + 1) * 8 + (sp18x4 << 0x3L);

      //LAB_8001b664
      do {
        v0 = _1f8003e0.get();
        if(v0 < 0) {
          v0 += 0x1fL;
        }

        //LAB_8001b67c
        v0 >>= 0x5L;
        v0 <<= 0x2L;
        if(sp1cx4 >= v0) {
          break;
        }

        long s6 = sp1cx4 << 0x3L;
        long sp20x4 = sp2cx4;

        //LAB_8001b6a4
        for(int s7 = 0; s7 < 1; s7++) {
          v0 = rand();

          //LAB_8001b6bc
          long s3 = v0 - (v0 >> 0x2L << 0x2L);
          if((rand() & 0x1L) != 0) {
            s3 = -s3;
          }

          //LAB_8001b6dc
          v0 = rand();
          final long s2 = v0 - ((v0 * 0x2aaaaaabL & 0xffffffffL) - (v0 >> 0x1fL)) * 6;

          final long s4;
          if(s6 >= 0xf9L) {
            //LAB_8001b720
            if(s6 >= 0x1f9L) {
              s4 = 0x8L;
            } else {
              s4 = 0x4L;
            }
          } else {
            s4 = 0;
          }

          //LAB_8001b734
          final long s0 = linkedListAddress_1f8003d8.get();
          linkedListAddress_1f8003d8.addu(0x28L);

          MEMORY.ref(1, s0).offset(0x3L).setu(0x9L); // 9 words

          MEMORY.ref(1, s0).offset(0x4L).setu(_800bd708).shra(0x8L);
          MEMORY.ref(1, s0).offset(0x5L).setu(_800bd708).shra(0x8L);
          MEMORY.ref(1, s0).offset(0x6L).setu(_800bd708).shra(0x8L);
          MEMORY.ref(1, s0).offset(0x7L).setu(0x2cL);

          MEMORY.ref(2, s0).offset(0x08L).setu(sp20x4 + s3);
          MEMORY.ref(2, s0).offset(0x0aL).setu(sp14x4 + sp24x4 + s5 + s2);
          MEMORY.ref(1, s0).offset(0x0cL).setu(s6);
          // 0xd set below
          // 0xe-f not set

          MEMORY.ref(2, s0).offset(0x10L).setu(sp20x4 + s3 + 0x8L);
          MEMORY.ref(2, s0).offset(0x12L).setu(sp14x4 + sp24x4 + s5 + s2);
          MEMORY.ref(1, s0).offset(0x14L).setu(s6 + 0x7L);
          // 0x15 set below
          // 0x16-17 set below

          MEMORY.ref(2, s0).offset(0x18L).setu(sp20x4 + s3);
          MEMORY.ref(2, s0).offset(0x1aL).setu(sp14x4 + sp28x4 + s5 + s2);
          MEMORY.ref(1, s0).offset(0x1cL).setu(s6);
          // 0x1e-1f not set

          MEMORY.ref(2, s0).offset(0x20L).setu(sp20x4 + s3 + 0x8L);
          MEMORY.ref(2, s0).offset(0x22L).setu(sp14x4 + sp28x4 + s5 + s2);
          MEMORY.ref(1, s0).offset(0x24L).setu(s6 + 0x7L);
          // 0x25 set below
          // 0x26-27 not set

          if(doubleBufferFrame_800bb108.get() == 0) {
            MEMORY.ref(1, s0).offset(0x0dL).setu(s5 + 0x10L);

            MEMORY.ref(1, s0).offset(0x15L).setu(s5 + 0x10L);
            MEMORY.ref(2, s0).offset(0x16L).setu(s4 + 0x100L);

            MEMORY.ref(1, s0).offset(0x1dL).setu(s5 + 0x18L);

            MEMORY.ref(1, s0).offset(0x25L).setu(s5 + 0x18L);
          } else {
            //LAB_8001b818
            MEMORY.ref(1, s0).offset(0x0dL).setu(s5);

            MEMORY.ref(1, s0).offset(0x15L).setu(s5);
            MEMORY.ref(2, s0).offset(0x16L).setu(s4 + 0x110L);

            MEMORY.ref(1, s0).offset(0x1dL).setu(s5 + 0x8L);

            MEMORY.ref(1, s0).offset(0x25L).setu(s5 + 0x8L);
          }

          //LAB_8001b868
          gpuLinkedListSetCommandTransparency(s0, true);
          insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);
        }

        sp2cx4 += 0x8L;
        sp1cx4++;
      } while(true);

      //LAB_8001b8b8
      sp30x4 += 0x200L;
      sp18x4++;
    } while(_800bd710.get() >= sp18x4);

    _800bd714.addu(0x1L);
    FUN_8001bbcc(sp10x4, sp14x4);
  }

  @Method(0x8001b92cL)
  public static void FUN_8001b92c() {
    long v1;
    long a1;
    long a2;

    a1 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x18L);

    MEMORY.ref(1, a1).offset(0x3L).setu(0x5L); // 5 words

    MEMORY.ref(1, a1).offset(0x4L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x5L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x6L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x7L).setu(0x28L);

    v1 = -displayWidth_1f8003e0.get() / 2;
    a2 = -displayHeight_1f8003e4.get() / 2;
    MEMORY.ref(2, a1).offset(0x8L).setu(v1 - 0x20L);
    MEMORY.ref(2, a1).offset(0xaL).setu(a2 - 0x20L);
    MEMORY.ref(2, a1).offset(0xcL).setu(displayWidth_1f8003e0.get() + v1 + 0x20L);
    MEMORY.ref(2, a1).offset(0xeL).setu(a1);

    MEMORY.ref(2, a1).offset(0x10L).setu(v1 - 0x20L);
    MEMORY.ref(2, a1).offset(0x12L).setu(a2 + 0x4L);
    MEMORY.ref(2, a1).offset(0x14L).setu(displayWidth_1f8003e0.get() + v1 + 0x20L);
    MEMORY.ref(2, a1).offset(0x16L).setu(a1);

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), a1);

    a1 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x18L);

    MEMORY.ref(1, a1).offset(0x3L).setu(0x5L); // 5 words

    MEMORY.ref(1, a1).offset(0x4L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x5L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x6L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x7L).setu(0x28L);

    v1 = -displayWidth_1f8003e0.get() / 2;
    a2 = displayHeight_1f8003e4.get() / 2;
    MEMORY.ref(2, a1).offset(0x8L).setu(v1 - 0x20L);
    MEMORY.ref(2, a1).offset(0xaL).setu(a2 + 0x20L);
    MEMORY.ref(2, a1).offset(0xcL).setu(displayWidth_1f8003e0.get() + v1);
    MEMORY.ref(2, a1).offset(0xeL).setu(a2 + 0x20L);

    MEMORY.ref(2, a1).offset(0x10L).setu(v1 - 0x20L);
    MEMORY.ref(2, a1).offset(0x12L).setu(a2 - 0x4L);
    MEMORY.ref(2, a1).offset(0x14L).setu(displayWidth_1f8003e0.get() + v1);
    MEMORY.ref(2, a1).offset(0x16L).setu(a2 - 0x4L);

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), a1);

    a1 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x18L);

    MEMORY.ref(1, a1).offset(0x3L).setu(0x5L); // 5 words

    MEMORY.ref(1, a1).offset(0x4L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x5L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x6L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x7L).setu(0x28L);

    v1 = -displayWidth_1f8003e0.get() / 2;
    a2 = -displayHeight_1f8003e4.get() / 2;
    MEMORY.ref(2, a1).offset(0x8L).setu(v1 - 0x20L);
    MEMORY.ref(2, a1).offset(0xaL).setu(a2);
    MEMORY.ref(2, a1).offset(0xcL).setu(v1 + 0x4L);
    MEMORY.ref(2, a1).offset(0xeL).setu(a2);

    MEMORY.ref(2, a1).offset(0x10L).setu(v1 - 0x20L);
    MEMORY.ref(2, a1).offset(0x12L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a2);
    MEMORY.ref(2, a1).offset(0x14L).setu(v1 + 0x4L);
    MEMORY.ref(2, a1).offset(0x16L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a2);

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), a1);

    a1 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x18L);

    MEMORY.ref(1, a1).offset(0x3L).setu(0x5L); // 5 words

    MEMORY.ref(1, a1).offset(0x4L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x5L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x6L).setu(0x1L);
    MEMORY.ref(1, a1).offset(0x7L).setu(0x28L);

    a2 = displayWidth_1f8003e0.get() / 2;
    v1 = -displayHeight_1f8003e4.get() / 2;
    MEMORY.ref(2, a1).offset(0x8L).setu(a2 + 0x20L);
    MEMORY.ref(2, a1).offset(0xaL).setu(v1);
    MEMORY.ref(2, a1).offset(0xcL).setu(a2);
    MEMORY.ref(2, a1).offset(0xeL).setu(v1);

    MEMORY.ref(2, a1).offset(0x10L).setu(a2 + 0x20L);
    MEMORY.ref(2, a1).offset(0x12L).setu(displayHeight_1f8003e4.offset(2, 0x0L));
    MEMORY.ref(2, a1).offset(0x14L).setu(a2 - 0x4L);
    MEMORY.ref(2, a1).offset(0x16L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + v1);

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), a1);
  }

  @Method(0x8001bbccL)
  public static void FUN_8001bbcc(final long a0, final long a1) {
    FUN_8001b92c();

    long s0;
    if(doubleBufferFrame_800bb108.get() == 0) {
      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);

      MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

      // Command
      MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L); // R
      MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L); // G
      MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L); // B
      MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL); // Textured four-point polygon, opaque, texture-blending

      // Vertex 1
      MEMORY.ref(2, s0).offset(0x08L).setu(a0 + 0x80L); // X
      MEMORY.ref(2, s0).offset(0x0aL).setu(a1); // Y
      MEMORY.ref(1, s0).offset(0x0cL).setu(0); // U
      MEMORY.ref(1, s0).offset(0x0dL).setu(0x10L); // V
      MEMORY.ref(2, s0).offset(0x0eL).setu(0x102L); // CLUT palette (note: this wasn't being set... pretty sure it needs to be)

      // Vertex 2
      MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0x17fL); // X
      MEMORY.ref(2, s0).offset(0x12L).setu(a1); // Y
      MEMORY.ref(1, s0).offset(0x14L).setu(0xffL); // U
      MEMORY.ref(1, s0).offset(0x15L).setu(0x10L); // V
      MEMORY.ref(2, s0).offset(0x16L).setu(0x102L); // CLUT palette

      // Vertex 3
      MEMORY.ref(2, s0).offset(0x18L).setu(a0 + 0x80L); // X
      MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L); // Y
      MEMORY.ref(1, s0).offset(0x1cL).setu(0); // U
      MEMORY.ref(1, s0).offset(0x1dL).setu(0xffL); // V
      // 0x1e-1f not set (CLUT palette)

      // Vertex 4
      MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0x17fL); // X
      MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L); // Y
      MEMORY.ref(1, s0).offset(0x24L).setu(0xffL); // U
      MEMORY.ref(1, s0).offset(0x25L).setu(0xffL); // V
      // 0x26-27 not set (CLUT palette)

      gpuLinkedListSetCommandTransparency(s0, false);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);

      MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

      // Command
      MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L); // R
      MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L); // G
      MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L); // B
      MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL); // Textured four-point polygon, opaque, texture-blending

      // Vertex 1
      MEMORY.ref(2, s0).offset(0x08L).setu(a0); // X
      MEMORY.ref(2, s0).offset(0x0aL).setu(a1); // Y
      MEMORY.ref(1, s0).offset(0x0cL).setu(0); // U
      MEMORY.ref(1, s0).offset(0x0dL).setu(0x10L); // V
      MEMORY.ref(2, s0).offset(0x0eL).setu(0x100L); // CLUT palette (note: this wasn't being set... pretty sure it needs to be)

      // Vertex 2
      MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0xffL); // X
      MEMORY.ref(2, s0).offset(0x12L).setu(a1); // Y
      MEMORY.ref(1, s0).offset(0x14L).setu(0xffL); // U
      MEMORY.ref(1, s0).offset(0x15L).setu(0x10L); // V
      MEMORY.ref(2, s0).offset(0x16L).setu(0x100L); // CLUT palette

      // Vertex 3
      MEMORY.ref(2, s0).offset(0x18L).setu(a0); // X
      MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L); // Y
      MEMORY.ref(1, s0).offset(0x1cL).setu(0); // U
      MEMORY.ref(1, s0).offset(0x1dL).setu(0xffL); // V
      // 0x1e-1f not set (CLUT palette)

      // Vertex 4
      MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0xffL); // X
      MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L); // Y
      MEMORY.ref(1, s0).offset(0x24L).setu(0xffL); // U
      MEMORY.ref(1, s0).offset(0x25L).setu(0xffL); // V
      // 0x26-27 not set (CLUT palette)

      gpuLinkedListSetCommandTransparency(s0, false);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

      if(displayWidth_1f8003e0.get() == 0x280L) {
        s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);

        MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

        MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL);

        MEMORY.ref(2, s0).offset(0x08L).setu(a0 + 0x100L);
        MEMORY.ref(2, s0).offset(0x0aL).setu(a1);
        MEMORY.ref(1, s0).offset(0x0cL).setu(0);
        MEMORY.ref(1, s0).offset(0x0dL).setu(0x10L);
        MEMORY.ref(2, s0).offset(0x0eL).setu(0x104L);

        MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0x1ffL);
        MEMORY.ref(2, s0).offset(0x12L).setu(a1);
        MEMORY.ref(1, s0).offset(0x14L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x15L).setu(0x10L);
        MEMORY.ref(2, s0).offset(0x16L).setu(0x104L);

        MEMORY.ref(2, s0).offset(0x18L).setu(a0 + 0x100L);
        MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x1cL).setu(0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(0xffL);
        // 0x1e-1f not set

        MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0x1ffL);
        MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x24L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x25L).setu(0xffL);
        // 0x26-27 not set

        gpuLinkedListSetCommandTransparency(s0, false);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

        s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);

        MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

        MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL);

        MEMORY.ref(2, s0).offset(0x08L).setu(a0 + 0x180L);
        MEMORY.ref(2, s0).offset(0x0aL).setu(a1);
        MEMORY.ref(1, s0).offset(0x0cL).setu(0);
        MEMORY.ref(1, s0).offset(0x0dL).setu(0x10L);
        MEMORY.ref(2, s0).offset(0x0eL).setu(0x106L);

        MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0x27fL);
        MEMORY.ref(2, s0).offset(0x12L).setu(a1);
        MEMORY.ref(1, s0).offset(0x14L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x15L).setu(0x10L);
        MEMORY.ref(2, s0).offset(0x16L).setu(0x106L);

        MEMORY.ref(2, s0).offset(0x18L).setu(a0 + 0x180L);
        MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x1cL).setu(0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(0xffL);
        // 0x1e-1f not set

        MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0x27fL);
        MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x24L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x25L).setu(0xffL);
        // 0x26-27 not set

        gpuLinkedListSetCommandTransparency(s0, false);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);
      }
    } else {
      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);

      MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

      MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L);
      MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L);
      MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L);
      MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL);

      MEMORY.ref(2, s0).offset(0x08L).setu(a0 + 0x80L);
      MEMORY.ref(2, s0).offset(0x0aL).setu(a1);
      MEMORY.ref(1, s0).offset(0x0cL).setu(0);
      MEMORY.ref(1, s0).offset(0x0dL).setu(0);
      MEMORY.ref(2, s0).offset(0x0eL).setu(0x112L);

      MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0x17fL);
      MEMORY.ref(2, s0).offset(0x12L).setu(a1);
      MEMORY.ref(1, s0).offset(0x14L).setu(0xffL);
      MEMORY.ref(1, s0).offset(0x15L).setu(0);
      MEMORY.ref(2, s0).offset(0x16L).setu(0x112L);

      MEMORY.ref(2, s0).offset(0x18L).setu(a0 + 0x80L);
      MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
      MEMORY.ref(1, s0).offset(0x1cL).setu(0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(0xefL);
      // 0x1e-1f not set

      MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0x17fL);
      MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
      MEMORY.ref(1, s0).offset(0x24L).setu(0xffL);
      MEMORY.ref(1, s0).offset(0x25L).setu(0xefL);
      // 0x26-27 not set

      gpuLinkedListSetCommandTransparency(s0, false);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);

      MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

      MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L);
      MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L);
      MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L);
      MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL);

      MEMORY.ref(2, s0).offset(0x08L).setu(a0);
      MEMORY.ref(2, s0).offset(0x0aL).setu(a1);
      MEMORY.ref(1, s0).offset(0x0cL).setu(0);
      MEMORY.ref(1, s0).offset(0x0dL).setu(0);
      MEMORY.ref(2, s0).offset(0x0eL).setu(0x110L);

      MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0xffL);
      MEMORY.ref(2, s0).offset(0x12L).setu(a1);
      MEMORY.ref(1, s0).offset(0x14L).setu(0xffL);
      MEMORY.ref(1, s0).offset(0x15L).setu(0);
      MEMORY.ref(2, s0).offset(0x16L).setu(0x110L);

      MEMORY.ref(2, s0).offset(0x18L).setu(a0);
      MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
      MEMORY.ref(1, s0).offset(0x1cL).setu(0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(0xefL);
      // 0x1e-1f not set

      MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0xffL);
      MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
      MEMORY.ref(1, s0).offset(0x24L).setu(0xffL);
      MEMORY.ref(1, s0).offset(0x25L).setu(0xefL);
      // 0x26-27 not set

      gpuLinkedListSetCommandTransparency(s0, false);
      insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

      if(displayWidth_1f8003e0.get() == 0x280L) {
        s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);

        MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

        MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL);

        MEMORY.ref(2, s0).offset(0x08L).setu(a0 + 0x100L);
        MEMORY.ref(2, s0).offset(0x0aL).setu(a1);
        MEMORY.ref(1, s0).offset(0x0cL).setu(0);
        MEMORY.ref(1, s0).offset(0x0dL).setu(0);
        MEMORY.ref(2, s0).offset(0x0eL).setu(0x114L);

        MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0x1ffL);
        MEMORY.ref(2, s0).offset(0x12L).setu(a1);
        MEMORY.ref(1, s0).offset(0x14L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x15L).setu(0);
        MEMORY.ref(2, s0).offset(0x16L).setu(0x114L);

        MEMORY.ref(2, s0).offset(0x18L).setu(a0 + 0x100L);
        MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x1cL).setu(0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(0xefL);
        // 0x1e-1f not set

        MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0x1ffL);
        MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x24L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x25L).setu(0xefL);
        // 0x26-27 not set

        gpuLinkedListSetCommandTransparency(s0, false);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

        s0 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x28L);

        MEMORY.ref(1, s0).offset(0x03L).setu(0x9L); // 9 words

        MEMORY.ref(1, s0).offset(0x04L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x05L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x06L).setu(_800bd708.get() >> 0x8L);
        MEMORY.ref(1, s0).offset(0x07L).setu(0x2cL);

        MEMORY.ref(2, s0).offset(0x08L).setu(a0 + 0x180L);
        MEMORY.ref(2, s0).offset(0x0aL).setu(a1);
        MEMORY.ref(1, s0).offset(0x0cL).setu(0);
        MEMORY.ref(1, s0).offset(0x0dL).setu(0);
        MEMORY.ref(2, s0).offset(0x0eL).setu(0x116L);

        MEMORY.ref(2, s0).offset(0x10L).setu(a0 + 0x27fL);
        MEMORY.ref(2, s0).offset(0x12L).setu(a1);
        MEMORY.ref(1, s0).offset(0x14L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x15L).setu(0);
        MEMORY.ref(2, s0).offset(0x16L).setu(0x116L);

        MEMORY.ref(2, s0).offset(0x18L).setu(a0 + 0x180L);
        MEMORY.ref(2, s0).offset(0x1aL).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x1cL).setu(0);
        MEMORY.ref(1, s0).offset(0x1dL).setu(0xefL);
        // 0x1e-1f not set

        MEMORY.ref(2, s0).offset(0x20L).setu(a0 + 0x27fL);
        MEMORY.ref(2, s0).offset(0x22L).setu(displayHeight_1f8003e4.offset(2, 0x0L).get() + a1 - 0x1L);
        MEMORY.ref(1, s0).offset(0x24L).setu(0xffL);
        MEMORY.ref(1, s0).offset(0x25L).setu(0xefL);
        // 0x26-27 not set

        gpuLinkedListSetCommandTransparency(s0, false);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);
      }
    }

    //LAB_8001c26c
    s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x8L);
    MEMORY.ref(1, s0).offset(0x3L).setu(0x1L); // 1 word
    MEMORY.ref(4, s0).offset(0x4L).setu(0xe1000100L);

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);

    s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x8L);
    MEMORY.ref(1, s0).offset(0x3L).setu(0x1L); // 1 word
    MEMORY.ref(4, s0).offset(0x4L).setu(0xe1000110L);

    insertElementIntoLinkedList(tags_1f8003d0.deref().get(0x6).getAddress(), s0);
  }

  @Method(0x8001c4ecL)
  public static void FUN_8001c4ec(final long a0) {
    assert false;
    //TODO
  }

  @Method(0x8001c594L)
  public static void FUN_8001c594(final long a0, final long a1) {
    _800bd700.setu(a0);
    _800bd704.setu(a1);
    _800bd708.setu(0x8000L);
    _800bd70c.setu(0x8000L / a1);
  }

  @Method(0x8001c5bcL)
  public static void FUN_8001c5bc() {
    if(_800bd700.get() == 0x1L) {
      _800bd704.subu(0x1L);
      _800bd708.subu(_800bd70c);

      if(_800bd704.get() == 0) {
        _800bd700.setu(0);
      }
    }
  }

  @Method(0x8001c60cL)
  public static long FUN_8001c60c() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t1;

    final long s0 = FUN_8001b3e4();
    a3 = 0;
    a2 = 0;

    LAB_8001c63c:
    {
      do {
        if(_800bd808.get() == _8004fb00.offset(a2).get()) {
          t1 = 0x1L << (a3 & 0x1fL);
          v1 = 0;

          //LAB_8001c680
          do {
            if(_800bd808.get() != 0x39L || _8004fb04.offset(a2).deref(2).offset(v1).get() == _80052c30.get()) {
              //LAB_8001c6ac
              if(_800bad6c.get(0x1L) == 0 || (_8004fb04.offset(a2).deref(2).offset(v1).get() == _80052c30.get() && _800bad6c.offset(a3 / 0x20L * 0x4L).get(t1) != 0)) {
                //LAB_8001c7c0
                //LAB_8001c7cc
                a1 = _8004fb02.offset(a2).getSigned();
                break LAB_8001c63c;
              }
            }

            //LAB_8001c6e4
            v1 += 0x2L;
          } while(_8004fb04.offset(a2).deref(2).offset(v1).getSigned() != -0x1L);
        }

        //LAB_8001c700
        a2 += 0x8L;
        v1 = _8004fb00.offset(a2).get();
        v0 = 0x63L;
        a3++;
        if(v1 == v0) {
          v0 = _8004fb02.offset(a2).get();
        }
      } while(v0 != v1);

      a0 = 0;

      //LAB_8001c728
      do {
        if(_800bd808.get() == _8004fa98.offset(a0).get()) {
          v1 = _8004fa9c.offset(a0).get();

          //LAB_8001c748
          do {
            if(MEMORY.ref(2, v1).get() == _80052c30.get()) {
              //LAB_8001c7d8
              return FUN_8001c84c(s0, _8004fa9a.offset(a0).getSigned());
            }

            v1 += 0x2L;
          } while(MEMORY.ref(2, v1).getSigned() != -0x1L);
        }

        //LAB_8001c76c
        a0 += 0x8L;
        v1 = _8004fa98.offset(a0).get();
        v0 = 0x63L;

        if(v1 == v0) {
          v0 = _8004fa9a.offset(a0).get();
        }
      } while(v0 != v1);

      a1 = FUN_8001c874();
    }

    //LAB_8001c7a0
    v1 = FUN_8001c84c(s0, a1);
    if(v1 != -0x2L) {
      return v1;
    }

    //LAB_8001c7ec
    if((FUN_8004d52c(_800bd0f8.get()) & 0x1L) == 0) {
      return -0x2L;
    }

    //LAB_8001c808
    return -0x3L;
  }

  @Method(0x8001c84cL)
  public static long FUN_8001c84c(final long a0, final long a1) {
    if(a0 != a1) {
      return a1;
    }

    if(a0 == -0x1L) {
      return -0x1L;
    }

    return -0x2L;
  }

  @Method(0x8001c874L)
  public static long FUN_8001c874() {
    if(_800bd808.get() == 0x38L) {
      for(int i = 0; ; i += 0x8) {
        if(_8004ff10.offset(i).get() == _80052c30.get()) {
          return _8004ff14.offset(i).get();
        }
      }
    }

    //LAB_8001c8bc
    return _80050068.offset(_800bd808.get() * 0x2L).getSigned();
  }

  @Method(0x8001dabcL)
  public static void FUN_8001dabc(final Value address, long a1, long a2) {
    final long s1;
    long a0;
    long v0;
    long v1;

    final long s5 = a2;
    linkedListEntry_800bd76c.setu(address);
    if(_8004dd20.get() == 0x5L || _8004dd20.get() == 0x6L && _800bb0f8.get() == 0x1bbL) {
      //LAB_8001db1c
      a0 = linkedListEntry_800bd784.get();
      a1 = address.deref(4).offset(0x20L).get();
      a2 = address.deref(4).offset(0x24L).get();
      a1 += address.get();
      memcpy(a0, a1, (int)a2);

      a0 = linkedListEntry_800bd788.get();
      a1 = address.deref(4).offset(0x18L).get();
      a2 = address.deref(4).offset(0x1cL).get();
      a1 += address.get();
      memcpy(a0, a1, (int)a2);

      _800bd0fc.setu(s5);
      v0 = address.deref(4).offset(0x8L).get();
      v0 += address.get();
      v0 = MEMORY.ref(2, v0).get();
      _800bd0c8.setu(0);
      _800bd0b6.setu(v0);

      v0 = address.deref(4).offset(0x10L).get();
      v0 += address.get();
      v0 = MEMORY.ref(1, v0).get();
      a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f810");
      v0--;
      _800bd0cc.setu(v0);
      FUN_8004be7c(a0);
      v0 = 0x2_1f70L;
      a1 = linkedListEntry_800bd784.get();
      a0 = address.deref(4).offset(0x28L).get();
      a2 = _800bd0c8.get();
      a0 += address.get();
      a2 += v0;
      v0 = FUN_8004bea4(a0, a1, a2);
      a0 = (short)v0;
      a1 = linkedListEntry_800bd788.get();
      _800bd0c4.setu(v0);
      v0 = _800bd0c8.get();
      v1 = address.deref(4).offset(0x2cL).get();
      _800bd0b4.setu(0x1L);
      v0 += v1;
      _800bd0c8.setu(v0);
      v0 = FUN_8004c1f8(a0, a1);
      _800bd0f8.setu(v0);
      _800bd781.setu(0x1L);
    } else {
      //LAB_8001dbf0
      v1 = address.deref(4).offset(0x4L).get();
      if(v1 == 0x5L) {
        a0 = address.deref(4).offset(0x28L).get();
        v0 = addToLinkedListTail(a0);
        a0 = v0;
        _800bd0b8.setu(a0);
        a2 = address.deref(4).offset(0x28L).get();
        a1 = address.get();
        memcpy(a0, a1, (int)a2);
        v0 = _800bd0b8.get();
        _800bd0fc.setu(s5);
        v1 = MEMORY.ref(4, v0).offset(0x8L).get();
        v0 += v1;
        v0 = MEMORY.ref(2, v0).get();
        _800bd0c8.setu(0);
        _800bd0b6.setu(v0);
        v0 = address.deref(4).offset(0x10L).get();
        v0 += address.get();
        v0 = MEMORY.ref(1, v0).get();
        a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f810");
        v0--;
        _800bd0cc.setu(v0);
        FUN_8004be7c(a0);
        v1 = 0x2_1f70L;
        a0 = address.deref(4).offset(0x28L).get();
        v0 = _800bd0b8.get();
        a2 = _800bd0c8.get();
        a0 += address.get();
        a1 = MEMORY.ref(4, v0).offset(0x20L).get();
        a2 += v1;
        a1 += v0;
        v0 = FUN_8004bea4(a0, a1, a2);
        a0 = _800bd0c8.get();
        _800bd0c4.setu(v0);
        a1 = address.deref(4).offset(0x2cL).get();
        _800bd0b4.setu(0x1L);
        v1 = _800bd0b8.get();
        a0 += a1;
        _800bd0c8.setu(a0);
        a1 = MEMORY.ref(4, v1).offset(0x18L).get();
        a0 = (short)v0;
        a1 += v1;
        v0 = FUN_8004c1f8(a0, a1);
        _800bd0f8.setu(v0);
      } else {
        //LAB_8001dcdc
        a0 = address.deref(4).offset(0x20L).get();
        v0 = addToLinkedListTail(a0);
        _800bd0b8.setu(v0);
        a2 = address.deref(4).offset(0x20L).get();
        memcpy(v0, address.get(), (int)a2);
        s1 = _800bd0b8.get();
        v0 = MEMORY.ref(4, s1).offset(0x8L).get();
        v0 += s1;
        v1 = MEMORY.ref(2, v0).get();
        _800bd0fc.setu(s5);
        v0 = s5 & 0x1L;
        _800bd0b6.setu(v1);
        if(v0 == 0) {
          a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fa18");
        } else {
          //LAB_8001dd3c
          a0 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001fab4");
        }

        //LAB_8001dd44
        FUN_8004be7c(a0);

        a0 = address.deref(4).offset(0x20L).get();
        a1 = MEMORY.ref(4, s1).offset(0x18L).get();
        a0 += address.get();
        a1 += s1;
        v0 = FUN_8004bea4(a0, a1, 0x2_1f70L);
        _800bd0c4.setu(v0);
        _800bd0b4.setu(0x1L);
        a1 = MEMORY.ref(4, s1).offset(0x10L).get();
        a0 = (short)v0;
        a1 += s1;
        v0 = FUN_8004c1f8(a0, a1);
        _800bd0f8.setu(v0);
      }

      //LAB_8001dd98
      _800bd781.setu(0);
    }

    //LAB_8001dda0
    FUN_8001b1a8(0x28L);
    _800bd0f0.setu(0x2L);
  }

  @Method(0x8001e010L)
  public static void FUN_8001e010(long a0) {
    long a1;
    long s0;
    long v1;

    if(a0 == 0) {
      //LAB_8001e054
      FUN_80020360(_800bd110.getAddress(), _800bca78.getAddress());
      FUN_8001ad18();
      FUN_8001e29c(0x8L);
      FUN_8001e29c(0x8L);

      _800bcf78.oru(0x80L);
      FUN_80015310(0, 0x16b7L, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001dabc", Value.class, long.class, long.class), 0x16_b700L, 0x4L);
      return;
    }

    //LAB_8001e044
    if(a0 == 0x1L) {
      return;
    }

    if((int)a0 > 0) {
      //LAB_8001e094
      FUN_8001ad18();
      FUN_8001e29c(0x8L);
      FUN_8001e29c(0x8L);

      //LAB_8001e0bc
      _800bcf78.oru(0x80L);
      FUN_80015310(0, 0x170cL, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001dabc", Value.class, long.class, long.class), 0x17_0c00L, 0x4L);
      return;
    }

    if(a0 != -0x1L) {
      return;
    }

    //LAB_8001e0f8
    if(_800bdc34.get() != 0) {
      if(_8004dd20.get() == 0x8L && _800bb0ac.get() != 0) {
        FUN_80020008();
        FUN_8001e29c(0x8L);

        _800bcf78.oru(0x80L);
        FUN_80015310(0, 0x16daL, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001dabc", Value.class, long.class, long.class), 0x16_da00L, 0x4L);
      }
    } else {
      //LAB_8001e160
      FUN_800201c8(0x6L);
      FUN_8001e29c(0x8L);
      v1 = _8004dd20.get();
      if(v1 != 0x5L) {
        if(v1 == 0x6L) {
          //LAB_8001e264
          FUN_80020008();
        } else if(v1 == 0x8L) {
          FUN_8001e29c(0x8L);
        }
      } else {
        //LAB_8001e1ac
        s0 = FUN_8001c60c();
        if(s0 == -0x2L) {
          //LAB_8001e1f4
          FUN_8001ada0();
          _800bd782.setu(0x1L);
        } else if(s0 < -0x1L) {
          if(s0 == -0x3L) {
            _800bd782.setu(0x1L);
          } else {
            //LAB_8001e20c
            FUN_8001e29c(0x8L);
            a1 = 0x16b7L + s0 * 5;

            //LAB_8001e23c
            _800bcf78.oru(0x80L);
            FUN_80015310(0, a1, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001dabc", Value.class, long.class, long.class), a1 * 0x100L, 0x4L);
          }

          //LAB_8001e1dc
        } else if(s0 == a0) {
          FUN_8001ae90();

          //LAB_8001e200
          _800bd782.setu(0x1L);
        } else {
          //LAB_8001e20c
          FUN_8001e29c(0x8L);
          a1 = 0x16b7L + s0 * 5;

          //LAB_8001e23c
          _800bcf78.oru(0x80L);
          FUN_80015310(0, a1, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001dabc", Value.class, long.class, long.class), a1 * 0x100L, 0x4L);
        }
      }
    }

    //LAB_8001e26c
    FUN_8001ad18();
    FUN_80020360(_800bca78.getAddress(), _800bd110.getAddress());

    //LAB_8001e288
  }

  @Method(0x8001e29cL)
  public static void FUN_8001e29c(final long a0) {
    switch((int)a0) {
      case 0x0:
        if(_800bcf80.get() != 0) {
          FUN_8004c114(_800bcf90.get());
          removeFromLinkedList(linkedListEntry_800bcf84.get());
          _800bcf80.setu(0);
        }

        break;

      case 0x1:
        //LAB_8001e324
        for(int s1 = 0; s1 < 3; s1++) {
          if(_800bcf80.offset(_800500f8.offset(s1 * 4).get() * 28).get() != 0) {
            FUN_8004c114(_800bcf90.offset(_800500f8.offset(s1 * 4).get() * 28).get());
            _800bcf80.offset(_800500f8.offset(s1 * 4).get() * 28).setu(0);
          }

          //LAB_8001e374
        }

        break;

      case 0x2:
        if(_800bcff0.get() != 0) {
          FUN_8004c114(_800bd000.get());
          removeFromLinkedList(_800bcff4.get());
          _800bcff0.setu(0);
        }

        break;

      case 0x3:
        //LAB_8001e3dc
        for(int s1 = 0; s1 < 4; s1++) {
          if(_800bcf80.offset(_800500e8.offset(s1 * 4).get() * 28).get() != 0) {
            removeFromLinkedList(linkedListEntry_800bcf84.offset(_800500e8.offset(s1 * 4).get() * 28).get());
            FUN_8004c114(_800bcf90.offset(_800500e8.offset(s1 * 4).get() * 28).get());
            _800bcf80.offset(_800500e8.offset(s1 * 4).get() * 28).setu(0);
          }

          //LAB_8001e450
        }

        break;

      case 0x4:
        if(_800bd060.get() != 0) {
          FUN_8004c114(_800bd070.get());
          removeFromLinkedList(_800bd064.get());
          _800bd060.setu(0);
        }

        break;

      case 0x5:
        if(_800bd07c.get() != 0) {
          FUN_8004c114(_800bd08c.get());
          removeFromLinkedList(_800bd080.get());
          _800bd07c.setu(0);
        }

        break;

      case 0x6:
      case 0x7:
        if(_800bd098.get() != 0) {
          FUN_8004c114(_800bd0a8.get());
          removeFromLinkedList(_800bd09c.get());
          _800bd098.setu(0);
        }

        break;

      case 0x8:
        if(_800bd0f0.get() != 0) {
          FUN_8004d034(_800bd0f8.get(), 0x1L);
          FUN_8004c390(_800bd0f8.get());

          if(_800bd781.get() == 0) {
            removeFromLinkedList(_800bd0b8.get());
          }

          //LAB_8001e56c
          _800bd0f0.setu(0);
        }

        //LAB_8001e570
        if(_800bd0b4.get() != 0) {
          FUN_8004c114(_800bd0c4.get());
          _800bd0b4.setu(0);
        }

        break;

      case 0x9:
        if(_800bd0d0.get() != 0) {
          FUN_8004c114(_800bd0e0.get());
          removeFromLinkedList(_800bd0d4.get());
          _800bd0d0.setu(0);
        }

        break;
    }

    //caseD_a
  }

  @Method(0x8001e5ecL)
  public static void FUN_8001e5ec() {
    _800bcf78.oru(0x1L);
    FUN_80015310(0, 0x166bL, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001e694", Value.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x8001e694L)
  public static void FUN_8001e694(final Value a0, final long a1, final long a2) {
    FUN_800156f4(0x1L);

    linkedListEntry_800bd778.setu(addToLinkedListHead(a0.deref(4).offset(0x24L).get()));
    memcpy(linkedListEntry_800bd778.get(), a0.deref(4).offset(0x20L).get() + a0.get(), (int)a0.deref(4).offset(0x24L).get());

    linkedListEntry_800bcf84.setu(addToLinkedListTail(a0.deref(4).offset(0x20L).get()));
    memcpy(linkedListEntry_800bcf84.get(), a0.get(), (int)a0.deref(4).offset(0x20L).get());
    removeFromLinkedList(a0.get());

    _800bcf88.setu(linkedListEntry_800bcf84.deref(4).offset(0x10L).get() + linkedListEntry_800bcf84.get());
    FUN_8004be7c(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001e780"));

    _800bcf90.setu(FUN_8004bea4(linkedListEntry_800bd778.get(), linkedListEntry_800bcf84.deref(4).offset(0x18L).get() + linkedListEntry_800bcf84.get(), 0x1010L));
    _800bcf80.setu(0x1L);
  }

  @Method(0x8001e780L)
  public static void FUN_8001e780() {
    removeFromLinkedList(linkedListEntry_800bd778.get());
    FUN_800156f4(0);
    _800bcf78.setu(_800bcf78.get(0xfffffffeL));
  }

  @Method(0x8001eadcL)
  public static void FUN_8001eadc(final long a0) {
    _800bcf78.oru(0x2L);
    FUN_80015310(0, a0 + 0x1676L, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001eb38", Value.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x8001eb38L)
  public static void FUN_8001eb38(Value address, long a1, long a2) {
    long s0;
    long s1;
    long s2;
    long v0;
    long a0;

    s2 = address.get();
    _800bd748.setu(s2);
    v0 = addToLinkedListTail(MEMORY.ref(4, s2).offset(0x28L).get());
    s1 = _800bcf80.getAddress();
    MEMORY.ref(4, s1).offset(0xe4L).setu(v0);
    memcpy(v0, s2, (int)MEMORY.ref(4, s2).offset(0x28L).get());
    s0 = MEMORY.ref(4, s1).offset(0xe4L).get();
    v0 = MEMORY.ref(4, s0).offset(0x18L).get();
    v0 += s0;
    MEMORY.ref(4, s1).offset(0xe8L).setu(v0);
    v0 = MEMORY.ref(4, s0).offset(0x10L).get();
    v0 += s0;
    MEMORY.ref(4, s1).offset(0xecL).setu(v0);
    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
    v0 += s0;
    MEMORY.ref(2, s1).offset(0xe2L).setu(MEMORY.ref(2, v0));
    FUN_8004be7c(getMethodAddress(Scus94491BpeSegment.class, "FUN_8001ec18"));
    a0 = MEMORY.ref(4, s2).offset(0x28L).get();
    a1 = MEMORY.ref(4, s0).offset(0x20L).getSigned();
    a0 += s2;
    a1 += s0;
    a0 = (short)FUN_8004bea4(a0, a1, 0x4_de90L);
    MEMORY.ref(2, s1).offset(0xf0L).setu(a0);
    FUN_8004cb0c(a0, 0x7fL);
    MEMORY.ref(2, s1).offset(0xe0L).setu(0x1L);
  }

  @Method(0x8001ec18L)
  public static void FUN_8001ec18() {
    removeFromLinkedList(_800bd748.get());
    _800bcf78.and(0xffff_fffdL);
    _800bd782.addu(0x1L);
  }

  @Method(0x8001f3d0L)
  public static void FUN_8001f3d0(final long a0, final long a1) {
    FUN_8001e29c(8);
    _800bcf78.oru(0x80L);
    final long iVar1 = a0 * 5 + 0x16b7;
    FUN_80015310(0, iVar1, 0, getMethodAddress(Scus94491BpeSegment.class, "FUN_8001dabc", Value.class, long.class, long.class), iVar1 * 0x100 | a1, 4);
  }

  @Method(0x8001f810L)
  public static void FUN_8001f810() {
    removeFromLinkedList(linkedListEntry_800bd76c.get());

    long s0 = _800bd0cc.get() - 0x1L;
    long s1 = 0x1L;

    //LAB_8001f860
    while((int)s0 >= 0) {
      final long a3;
      if(s0 != 0) {
        a3 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f8e0", Value.class, long.class, long.class);
      } else {
        //LAB_8001f87c
        a3 = getMethodAddress(Scus94491BpeSegment.class, "FUN_8001f968", Value.class, long.class, long.class);
      }

      //LAB_8001f88c
      FUN_80015310(0, (_800bd0fc.get() >> 8) + s1, 0, a3, 0, 0x4L);
      s0--;
      s1++;
    }

    //LAB_8001f8b4
    _800bd782.addu(0x1L);
  }

  @Method(0x8001f8e0L)
  public static void FUN_8001f8e0(final Value audioAddress, final long spuTransferSize, final long unused) {
    FUN_8004be7c(0);

    SPU.directWrite(0x2_1f70 + (int)_800bd0c8.get(), audioAddress.get(), (int)spuTransferSize);

    _800bd0c8.addu(spuTransferSize);
    removeFromLinkedList(audioAddress.get());
  }

  @Method(0x8001f968L)
  public static void FUN_8001f968(final Value audioAddress, final long spuTransferSize, final long unused) {
    FUN_8004be7c(0);

    SPU.directWrite(0x2_1f70 + (int)_800bd0c8.get(), audioAddress.get(), (int)spuTransferSize);

    if(_800bd0fc.get(0x1L) == 0) {
      FUN_8004cf8c(_800bd0f8.get());
    }

    removeFromLinkedList(audioAddress.get());
    _800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001fa18L)
  public static void FUN_8001fa18() {
    FUN_8004cf8c(_800bd0f8.get());
    _800bd104.setu(FUN_8004d4f8(_800bd0f8.get()) * _800bd100.get());
    FUN_8004d4b4(_800bd0f8.get(), (int)(_800bd104.get() << 8) >> 16);
    removeFromLinkedList(linkedListEntry_800bd76c.get());

    _800bd782.addu(0x1L);
    _800bcf78.and(0xffff_ff7fL);
  }

  @Method(0x8001ffb0L)
  public static long FUN_8001ffb0() {
    return _800bcf78.get();
  }
}
