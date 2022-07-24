package legend.game;

import legend.core.CpuRegisterType;
import legend.core.DebugHelper;
import legend.core.Hardware;
import legend.core.InterruptType;
import legend.core.MathHelper;
import legend.core.Tuple;
import legend.core.cdrom.CdlCOMMAND;
import legend.core.cdrom.CdlDIR;
import legend.core.cdrom.CdlFILE;
import legend.core.cdrom.CdlLOC;
import legend.core.cdrom.CdlMODE;
import legend.core.cdrom.CdlPacket;
import legend.core.cdrom.Response;
import legend.core.cdrom.SyncCode;
import legend.core.dma.DmaChannel;
import legend.core.dma.DmaChannelType;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.DR_ENV;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.kernel.jmp_buf;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.SupplierRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.DR_MODE;
import legend.game.types.DR_MOVE;
import legend.game.types.DR_TPAGE;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsOT;
import legend.game.types.GsOT_TAG;
import legend.game.types.GsOffsetType;
import legend.game.types.GsRVIEW2;
import legend.game.types.RenderStruct20;
import legend.game.types.WeirdTimHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.CPU;
import static legend.core.Hardware.DMA;
import static legend.core.Hardware.GPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.Hardware.SPU;
import static legend.core.InterruptController.I_MASK;
import static legend.core.InterruptController.I_STAT;
import static legend.core.LibDs.DSL_MAX_COMMAND;
import static legend.core.LibDs.DSL_MAX_DIR;
import static legend.core.LibDs.DSL_MAX_FILE;
import static legend.core.LibDs.DSL_MAX_LEVEL;
import static legend.core.LibDs.DSL_MAX_RESULTS;
import static legend.core.MathHelper.clamp;
import static legend.core.MathHelper.toBcd;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.Timers.TMR_HRETRACE_MODE;
import static legend.core.Timers.TMR_HRETRACE_VAL;
import static legend.core.cdrom.CdDrive.CDROM_REG0;
import static legend.core.cdrom.CdDrive.CDROM_REG1;
import static legend.core.cdrom.CdDrive.CDROM_REG2;
import static legend.core.cdrom.CdDrive.CDROM_REG3;
import static legend.core.dma.DmaManager.DMA_DICR;
import static legend.core.dma.DmaManager.DMA_DPCR;
import static legend.core.gpu.Gpu.GPU_REG0;
import static legend.core.gpu.Gpu.GPU_REG1;
import static legend.core.kernel.Bios.EnterCriticalSection;
import static legend.core.kernel.Bios.ExitCriticalSection;
import static legend.core.memory.segments.MemoryControl1Segment.CDROM_DELAY;
import static legend.core.memory.segments.MemoryControl1Segment.COMMON_DELAY;
import static legend.game.Scus94491BpeSegment._80011394;
import static legend.game.Scus94491BpeSegment._8001139c;
import static legend.game.Scus94491BpeSegment._800113c0;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.functionVectorA_000000a0;
import static legend.game.Scus94491BpeSegment.functionVectorB_000000b0;
import static legend.game.Scus94491BpeSegment.functionVectorC_000000c0;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment_8002.ChangeClearPAD;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002f6f0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002fdc0;
import static legend.game.Scus94491BpeSegment_8002.SetBackColor;
import static legend.game.Scus94491BpeSegment_8002.SetColorMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetLightMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002._card_load;
import static legend.game.Scus94491BpeSegment_8002._card_write;
import static legend.game.Scus94491BpeSegment_8002.awaitMemcard;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8002.strncmp;
import static legend.game.Scus94491BpeSegment_8002.testEvents;
import static legend.game.Scus94491BpeSegment_8004.Lzc;
import static legend.game.Scus94491BpeSegment_8004.patchC0TableAgain;
import static legend.game.Scus94491BpeSegment_8005.CD_debug_80052f4c;
import static legend.game.Scus94491BpeSegment_8005.DISPENV_80054728;
import static legend.game.Scus94491BpeSegment_8005.DRAWENV_800546cc;
import static legend.game.Scus94491BpeSegment_8005.GsOUT_PACKET_P;
import static legend.game.Scus94491BpeSegment_8005.Vcount;
import static legend.game.Scus94491BpeSegment_8005._80052f24;
import static legend.game.Scus94491BpeSegment_8005._80052f50;
import static legend.game.Scus94491BpeSegment_8005._80052f54;
import static legend.game.Scus94491BpeSegment_8005._80052f58;
import static legend.game.Scus94491BpeSegment_8005._80053008;
import static legend.game.Scus94491BpeSegment_8005._80053088;
import static legend.game.Scus94491BpeSegment_8005._80053108;
import static legend.game.Scus94491BpeSegment_8005._8005324c;
import static legend.game.Scus94491BpeSegment_8005._800532e4;
import static legend.game.Scus94491BpeSegment_8005._80053300;
import static legend.game.Scus94491BpeSegment_8005._80053304;
import static legend.game.Scus94491BpeSegment_8005._80053308;
import static legend.game.Scus94491BpeSegment_8005._8005330c;
import static legend.game.Scus94491BpeSegment_8005._8005331c;
import static legend.game.Scus94491BpeSegment_8005._80053324;
import static legend.game.Scus94491BpeSegment_8005._80053328;
import static legend.game.Scus94491BpeSegment_8005._8005332c;
import static legend.game.Scus94491BpeSegment_8005._80053330;
import static legend.game.Scus94491BpeSegment_8005._80053334;
import static legend.game.Scus94491BpeSegment_8005._80053438;
import static legend.game.Scus94491BpeSegment_8005._80053448;
import static legend.game.Scus94491BpeSegment_8005._80053488;
import static legend.game.Scus94491BpeSegment_8005._8005348c;
import static legend.game.Scus94491BpeSegment_8005._80053490;
import static legend.game.Scus94491BpeSegment_8005._80053494;
import static legend.game.Scus94491BpeSegment_8005._80053498;
import static legend.game.Scus94491BpeSegment_8005._8005349c;
import static legend.game.Scus94491BpeSegment_8005._800534fc;
import static legend.game.Scus94491BpeSegment_8005._80053500;
import static legend.game.Scus94491BpeSegment_8005._80053594;
import static legend.game.Scus94491BpeSegment_8005._800535a0;
import static legend.game.Scus94491BpeSegment_8005._8005457c;
import static legend.game.Scus94491BpeSegment_8005._800545ec;
import static legend.game.Scus94491BpeSegment_8005._800545fc;
import static legend.game.Scus94491BpeSegment_8005._80054674;
import static legend.game.Scus94491BpeSegment_8005._800546b4;
import static legend.game.Scus94491BpeSegment_8005._800546bc;
import static legend.game.Scus94491BpeSegment_8005._800546bd;
import static legend.game.Scus94491BpeSegment_8005._800546c0;
import static legend.game.Scus94491BpeSegment_8005._800546c2;
import static legend.game.Scus94491BpeSegment_8005._800546c4;
import static legend.game.Scus94491BpeSegment_8005._8005475c;
import static legend.game.Scus94491BpeSegment_8005._8005477c;
import static legend.game.Scus94491BpeSegment_8005._80054790;
import static legend.game.Scus94491BpeSegment_8005._80054792;
import static legend.game.Scus94491BpeSegment_8005._800547bb;
import static legend.game.Scus94491BpeSegment_8005._800547f4;
import static legend.game.Scus94491BpeSegment_8005._800547f8;
import static legend.game.Scus94491BpeSegment_8005._800547fc;
import static legend.game.Scus94491BpeSegment_8005.array_8005473c;
import static legend.game.Scus94491BpeSegment_8005.array_80054748;
import static legend.game.Scus94491BpeSegment_8005.cdlLoc_80053312;
import static legend.game.Scus94491BpeSegment_8005.cdlPacketBatch_800532cc;
import static legend.game.Scus94491BpeSegment_8005.cdromCommandResponseStatOffsetArray_800533b8;
import static legend.game.Scus94491BpeSegment_8005.cdromCommandSomethingArray_80053338;
import static legend.game.Scus94491BpeSegment_8005.cdromCommand_800532e8;
import static legend.game.Scus94491BpeSegment_8005.cdromCommand_80053310;
import static legend.game.Scus94491BpeSegment_8005.cdromCommand_80053316;
import static legend.game.Scus94491BpeSegment_8005.cdromCommand_80053317;
import static legend.game.Scus94491BpeSegment_8005.cdromCommand_80053335;
import static legend.game.Scus94491BpeSegment_8005.cdromCompleteInterruptCallbackPtr_80052f44;
import static legend.game.Scus94491BpeSegment_8005.cdromDataEndSyncCode_80053222;
import static legend.game.Scus94491BpeSegment_8005.cdromDataInterruptCallbackPtr_80052f48;
import static legend.game.Scus94491BpeSegment_8005.cdromDataSyncCode_80053221;
import static legend.game.Scus94491BpeSegment_8005.cdromDmaPos_8005347c;
import static legend.game.Scus94491BpeSegment_8005.cdromDmaStatusCallback_80053484;
import static legend.game.Scus94491BpeSegment_8005.cdromFilePointer_8005346c;
import static legend.game.Scus94491BpeSegment_8005.cdromMode_80052f60;
import static legend.game.Scus94491BpeSegment_8005.cdromParamBuffer_80052f5c;
import static legend.game.Scus94491BpeSegment_8005.cdromParams_800532e9;
import static legend.game.Scus94491BpeSegment_8005.cdromPreviousDmaPos_80053480;
import static legend.game.Scus94491BpeSegment_8005.cdromResponse_800532f4;
import static legend.game.Scus94491BpeSegment_8005.cdromStat_800532fc;
import static legend.game.Scus94491BpeSegment_8005.cdromSyncCode_80053220;
import static legend.game.Scus94491BpeSegment_8005.dmaCallbacks_80054640;
import static legend.game.Scus94491BpeSegment_8005.doingCdromDmaTransfer_8005345c;
import static legend.game.Scus94491BpeSegment_8005.drawSyncCallback_800546c8;
import static legend.game.Scus94491BpeSegment_8005.gpuQueueIndex_800547e4;
import static legend.game.Scus94491BpeSegment_8005.gpuQueueTotal_800547e8;
import static legend.game.Scus94491BpeSegment_8005.gpuReverseFlag_800546bf;
import static legend.game.Scus94491BpeSegment_8005.gpu_debug;
import static legend.game.Scus94491BpeSegment_8005.inExceptionHandler_80053566;
import static legend.game.Scus94491BpeSegment_8005.interruptCallbacks_80053568;
import static legend.game.Scus94491BpeSegment_8005.interruptHandlersInitialized_80053564;
import static legend.game.Scus94491BpeSegment_8005.isMotorOn_8005331b;
import static legend.game.Scus94491BpeSegment_8005.isPlaying_80053318;
import static legend.game.Scus94491BpeSegment_8005.isReading_8005331a;
import static legend.game.Scus94491BpeSegment_8005.isSeeking_80053319;
import static legend.game.Scus94491BpeSegment_8005.jmp_buf_8005359c;
import static legend.game.Scus94491BpeSegment_8005.matrixStackIndex_80054a08;
import static legend.game.Scus94491BpeSegment_8005.matrixStack_80054a0c;
import static legend.game.Scus94491BpeSegment_8005.oldCdromDmaCallback_80053450;
import static legend.game.Scus94491BpeSegment_8005.oldCdromParam_80053311;
import static legend.game.Scus94491BpeSegment_8005.oldIMask_800547ec;
import static legend.game.Scus94491BpeSegment_8005.oldIMask_800547f0;
import static legend.game.Scus94491BpeSegment_8005.previousCdromCommand_80052f61;
import static legend.game.Scus94491BpeSegment_8005.ptrCdromParams_800532f0;
import static legend.game.Scus94491BpeSegment_8005.ptrCdromSyncCode_80053220_80053224;
import static legend.game.Scus94491BpeSegment_8005.sin_cos_80054d0c;
import static legend.game.Scus94491BpeSegment_8005.syncCode_80053320;
import static legend.game.Scus94491BpeSegment_8005.syncCode_80053470;
import static legend.game.Scus94491BpeSegment_8005.usingLibDs_80052f64;
import static legend.game.Scus94491BpeSegment_8005.vsyncCallbacks_8005460c;
import static legend.game.Scus94491BpeSegment_800b.CdlDIR_800bfda8;
import static legend.game.Scus94491BpeSegment_800b.CdlFILE_800bf7a8;
import static legend.game.Scus94491BpeSegment_800b.CdlPacket_800bf638;
import static legend.game.Scus94491BpeSegment_800b._800bf290;
import static legend.game.Scus94491BpeSegment_800b._800bf4c0;
import static legend.game.Scus94491BpeSegment_800b._800bf4d0;
import static legend.game.Scus94491BpeSegment_800b._800bf550;
import static legend.game.Scus94491BpeSegment_800b._800bf554;
import static legend.game.Scus94491BpeSegment_800b._800bf558;
import static legend.game.Scus94491BpeSegment_800b._800bf55c;
import static legend.game.Scus94491BpeSegment_800b._800bf560;
import static legend.game.Scus94491BpeSegment_800b._800bf564;
import static legend.game.Scus94491BpeSegment_800b._800bf568;
import static legend.game.Scus94491BpeSegment_800b._800bf56c;
import static legend.game.Scus94491BpeSegment_800b._800bf570;
import static legend.game.Scus94491BpeSegment_800b._800bf574;
import static legend.game.Scus94491BpeSegment_800b._800bf578;
import static legend.game.Scus94491BpeSegment_800b._800bf57c;
import static legend.game.Scus94491BpeSegment_800b._800bf580;
import static legend.game.Scus94491BpeSegment_800b._800bf584;
import static legend.game.Scus94491BpeSegment_800b._800bf588;
import static legend.game.Scus94491BpeSegment_800b._800bf58c;
import static legend.game.Scus94491BpeSegment_800b._800bf590;
import static legend.game.Scus94491BpeSegment_800b._800bf594;
import static legend.game.Scus94491BpeSegment_800b._800bf59c;
import static legend.game.Scus94491BpeSegment_800b._800bf5a0;
import static legend.game.Scus94491BpeSegment_800b._800bf5b0;
import static legend.game.Scus94491BpeSegment_800b._800bf5b4;
import static legend.game.Scus94491BpeSegment_800b._800bf5e0;
import static legend.game.Scus94491BpeSegment_800b._800bf6f8;
import static legend.game.Scus94491BpeSegment_800b._800bf6fc;
import static legend.game.Scus94491BpeSegment_800b._800bf798;
import static legend.game.Scus94491BpeSegment_800b._800bf79c;
import static legend.game.Scus94491BpeSegment_800b._800bf7a4;
import static legend.game.Scus94491BpeSegment_800b._800bfd84;
import static legend.game.Scus94491BpeSegment_800b._800bfdd8;
import static legend.game.Scus94491BpeSegment_800b.batch_800bf608;
import static legend.game.Scus94491BpeSegment_800b.batch_800bf618;
import static legend.game.Scus94491BpeSegment_800b.batch_800bf628;
import static legend.game.Scus94491BpeSegment_800b.cdlPacketIndex_800bf700;
import static legend.game.Scus94491BpeSegment_800b.cdromDmaSubCallback_800bf598;
import static legend.game.Scus94491BpeSegment_800b.cdromParamsPtr_800bf644;
import static legend.game.Scus94491BpeSegment_800b.cdromReadCompleteSubCallbackPtr_800bf7a0;
import static legend.game.Scus94491BpeSegment_800b.cdromResponseBufferIndex_800bf788;
import static legend.game.Scus94491BpeSegment_800b.cdromResponseBuffer_800bf708;
import static legend.game.Scus94491BpeSegment_800b.cdromResponse_800bf5f8;
import static legend.game.Scus94491BpeSegment_800b.cdromResponses_800bf5c0;
import static legend.game.Scus94491BpeSegment_800b.cdromResponses_800bf5c8;
import static legend.game.Scus94491BpeSegment_800b.cdromResponses_800bf5d0;
import static legend.game.Scus94491BpeSegment_800b.response_800bf60d;
import static legend.game.Scus94491BpeSegment_800b.response_800bf61d;
import static legend.game.Scus94491BpeSegment_800b.response_800bf62d;
import static legend.game.Scus94491BpeSegment_800b.sectorIsDataOnly_800bf5f0;
import static legend.game.Scus94491BpeSegment_800b.syncCode_800bf60c;
import static legend.game.Scus94491BpeSegment_800b.syncCode_800bf61c;
import static legend.game.Scus94491BpeSegment_800b.syncCode_800bf62c;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.Scus94491BpeSegment_800c.DRAWENV_800c3450;
import static legend.game.Scus94491BpeSegment_800c.PSDCNT_800c34d0;
import static legend.game.Scus94491BpeSegment_800c.PSDIDX_800c34d4;
import static legend.game.Scus94491BpeSegment_800c._800c13a8;
import static legend.game.Scus94491BpeSegment_800c._800c13a9;
import static legend.game.Scus94491BpeSegment_800c._800c1434;
import static legend.game.Scus94491BpeSegment_800c._800c1ba8;
import static legend.game.Scus94491BpeSegment_800c._800c1bb0;
import static legend.game.Scus94491BpeSegment_800c._800c1bb8;
import static legend.game.Scus94491BpeSegment_800c._800c1bc0;
import static legend.game.Scus94491BpeSegment_800c._800c1be8;
import static legend.game.Scus94491BpeSegment_800c._800c3410;
import static legend.game.Scus94491BpeSegment_800c._800c3420;
import static legend.game.Scus94491BpeSegment_800c._800c3423;
import static legend.game.Scus94491BpeSegment_800c._800c3424;
import static legend.game.Scus94491BpeSegment_800c._800c3425;
import static legend.game.Scus94491BpeSegment_800c._800c3426;
import static legend.game.Scus94491BpeSegment_800c._800c3427;
import static legend.game.Scus94491BpeSegment_800c._800c3428;
import static legend.game.Scus94491BpeSegment_800c._800c342a;
import static legend.game.Scus94491BpeSegment_800c._800c342c;
import static legend.game.Scus94491BpeSegment_800c._800c342e;
import static legend.game.Scus94491BpeSegment_800c._800c3433;
import static legend.game.Scus94491BpeSegment_800c._800c3437;
import static legend.game.Scus94491BpeSegment_800c._800c34c4;
import static legend.game.Scus94491BpeSegment_800c._800c34c6;
import static legend.game.Scus94491BpeSegment_800c._800c34d8;
import static legend.game.Scus94491BpeSegment_800c._800c34e0;
import static legend.game.Scus94491BpeSegment_800c.cdromReadCompleteSubSubCallbackPtr_800c1bb4;
import static legend.game.Scus94491BpeSegment_800c.clip_800c3440;
import static legend.game.Scus94491BpeSegment_800c.clip_800c3448;
import static legend.game.Scus94491BpeSegment_800c.coord2s_800c35a8;
import static legend.game.Scus94491BpeSegment_800c.displayRect_800c34c8;
import static legend.game.Scus94491BpeSegment_800c.doubleBufferOffsetMode_800c34d6;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallbackObjPtr_800c1c14;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallbackObj_800c1c1c;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallbackSomething_800c1c18;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallback_800c1c10;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.lightMode_800c34dc;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3528;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3588;

public final class Scus94491BpeSegment_8003 {
  private Scus94491BpeSegment_8003() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8003.class);

  private static final Object[] EMPTY_OBJ_ARRAY = {};

  @Method(0x80030320L)
  public static long FUN_80030320(final int port, final String file, final long a2) {
    long v0;
    long v1;
    long s0 = 0;
    long s5 = 0;

    bzero(_800bf4d0.getAddress(), 0x80);

    //LAB_80030388
    for(int i = 0; i < 8; i++) {
      testEvents();
      _card_read(port, 0, _800bf4d0.getAddress());

      s0 = FUN_8002fdc0();
      if(s0 == 0) {
        break;
      }

      if(s0 == 0x4L) {
        testEvents();
        FUN_8002f6f0(port);
        FUN_8002fdc0();
      }

      //LAB_800303d8
    }

    //LAB_800303e8
    if(s0 != 0) {
      return s0;
    }

    if(_800bf4d0.offset(1, 0x0L).get() != 'M' || _800bf4d0.offset(1, 0x1L).get() != 'C') {
      return -0x2L;
    }

    //LAB_80030430
    for(int sector = 0; sector < 15; sector++) {
      _800bf4c0.offset(sector).setu(0); //1b
      bzero(_800bf4d0.getAddress(), 0x80);

      //LAB_80030458
      for(int i = 0; i < 8; i++) {
        testEvents();
        _card_read(port, sector + 1, _800bf4d0.getAddress());

        s0 = FUN_8002fdc0();
        if(s0 == 0) {
          break;
        }

        if(s0 == 0x4L) {
          testEvents();
          FUN_8002f6f0(port);
          FUN_8002fdc0();
        }

        //LAB_800304a8
      }

      //LAB_800304b8
      if(s0 != 0) {
        return s0;
      }

      memcpy(_800bf290.offset(sector * 0x20L).getAddress(), _800bf4d0.getAddress(), 0x20);
    }

    //LAB_8003056c
    for(int sector = 0; sector < 15; sector++) {
      if(_800bf290.offset(sector * 0x20L).get() == 0x51L) {
        _800bf4c0.offset(sector).setu(0x1L); //1b

        v1 = sector;

        //LAB_800305b0
        while(_800bf290.offset(v1 * 0x20L).offset(2, 0x8L).get() != 0xffffL) {
          v1 = _800bf290.offset(v1 * 0x20L).offset(2, 0x8L).get();

          if(v1 >= 15) {
            break;
          }

          _800bf4c0.offset(v1).setu(0x1L); //1b
        }
      }

      //LAB_800305f0
    }

    //LAB_80030608
    for(int sector = 0; sector < 15; sector++) {
      if(_800bf4c0.offset(sector).getSigned() == 0) { //1b
        _800bf290.offset(sector * 0x20L).setu(0xa0L);
      }

      //LAB_8003062c
    }


    //LAB_8003064c
    for(int sector = 0; sector < 15; sector++) {
      final CString str = _800bf290.offset(sector * 0x20L).offset(0xaL).cast(CString.maxLength(0x14));

      if(_800bf290.offset(sector * 0x20L).get() == 0x51L && strcmp(str.get(), file) == 0) {
        //LAB_800306ec
        throw new RuntimeException("[%s] [%s]".formatted(str, file));
      }

      //LAB_8003067c
    }

    //LAB_80030698
    for(int sector = 0; sector < 15; sector++) {
      _800bf4c0.offset(sector).setu(0); //1b
      if((_800bf290.offset(sector * 0x20L).get() & 0xf0L) == 0xa0L) {
        s5 = s5 + 0x1L;
      }

      //LAB_800306c0
    }

    if((int)s5 < (int)a2) {
      return -0x1L;
    }

    //LAB_80030708
    s5 = 0;
    v1 = 0;

    //LAB_8003073c
    for(int sector = 0; sector < 15; sector++) {
      s0 = _800bf290.offset(sector * 0x20L).getAddress();

      if((MEMORY.ref(4, s0).get() & 0xf0L) == 0xa0L) {
        if(s5 == 0) {
          MEMORY.ref(4, s0).setu(0x51L);
          _800bf290.offset(sector * 0x20L).offset(4, 0x4L).setu(a2 * 0x2000L);
          _800bf290.offset(sector * 0x20L).offset(0xaL).cast(CString.maxLength(0x14)).set(file);
        } else {
          //LAB_80030784
          _800bf290.offset(v1 * 0x20L).offset(2, 0x8L).setu(sector);
          MEMORY.ref(4, s0).setu(0x52L);
        }
        v1 = sector;

        //LAB_8003079c
        _800bf4c0.offset(sector).setu(0x1L); // 1b

        s5++;
        if(s5 >= (int)a2) {
          _800bf290.offset(sector * 0x20L).offset(2, 0x8L).setu(0xffffL);
          if(s5 >= 0x2L) {
            MEMORY.ref(4, s0).setu(0x53L);
          }
          break;
        }
      }

      //LAB_800307d4
      s0 = s0 + 0x20L;
    }

    //LAB_800307f0
    s5 = _800bf4d0.getAddress();

    //LAB_80030808
    for(int sector = 14; sector >= 0; sector--) {
      if(_800bf4c0.offset(sector).getSigned() != 0) { //1b
        bzero(s5, 0x80);
        memcpy(s5, _800bf290.offset(sector).getAddress(), 0x20);

        //LAB_800308bc
        v0 = 0;
        for(v1 = 0; v1 < 0x7f; v1++) {
          v0 = v0 ^ MEMORY.ref(1, s5).offset(v1).get();
        }

        MEMORY.ref(1, s5).offset(0x80L).setu(v0);

        //LAB_800308d8
        for(int i = 0; i < 8; i++) {
          testEvents();
          _card_write(port, sector + 1, _800bf4d0.getAddress());
          s0 = FUN_8002fdc0();
          if(s0 == 0) {
            break;
          }

          if(s0 == 0x4L) {
            testEvents();
            FUN_8002f6f0(port);
            FUN_8002fdc0();
          }

          //LAB_80030928
        }

        //LAB_80030938
        if(s0 != 0) {
          return s0;
        }
      }

      //LAB_80030940
    }

    //LAB_8003094c
    for(int i = 0; i < 8; i++) {
      testEvents();
      _card_load(port);

      s0 = awaitMemcard();
      if(s0 == 0) {
        return 0;
      }

      testEvents();
      FUN_8002f6f0(port);
      FUN_8002fdc0();
    }

    //LAB_800309a0
    //LAB_800309a4
    return s0;
  }

  @Method(0x800309f0L)
  public static void bzero(final long address, final int size) {
    functionVectorA_000000a0.run(0x28L, new Object[] {address, size});
  }

  @Method(0x80030a00L)
  public static boolean _card_read(final int port, final int sector, final long src) {
    return (boolean)functionVectorB_000000b0.run(0x4fL, new Object[] {port, sector, src});
  }

  @Method(0x80030a10L)
  public static void FUN_80030a10(final long address, final long size) {
    _800bf590.setu(address);
    _800bf594.setu(size);
    FUN_80030a40();
  }

  @Method(0x80030a40L)
  public static void FUN_80030a40() {
    _800bf57c.setu(0);
    _800bf578.setu(0);
    _800bf574.setu(0);
    _800bf56c.setu(0);
    FUN_80030c60(0, _800bf594.get());
    _800bf55c.setu(0);
    _800bf554.setu(0);
    _800bf550.setu(0);
  }

  @Method(0x80030aa0L)
  public static void disableCdromDmaCallbacksAndClearDataFifo() {
    EnterCriticalSection();

    if(usingLibDs_80052f64.get() == 0x1L) {
      registerCdDmaCallback(0);
      setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(null);
    } else {
      //LAB_80030adc
      setCdromDmaCallback(0);
      setCdromDataInterruptCallbackPtr(0);
    }

    //LAB_80030aec
    CDROM_REG0.setu(0);
    CDROM_REG3.setu(0);

    ExitCriticalSection();
  }

  @Method(0x80030b20L)
  public static void FUN_80030b20(final long a0, final long a1, final long a2, final long cdromDmaSubCallback, final long a4) {
    FUN_80030d60(0x1L, a1, a2);
    _800bf550.setu(0);
    _800bf554.setu(0);
    _800bf558.setu(a0 & 1);
    _800bf560.setu(0);
    _800bf568.setu(0);
    _800bf580.setu(0);

    if(cdromDmaSubCallback == 0) {
      cdromDmaSubCallback_800bf598.clear();
    } else {
      cdromDmaSubCallback_800bf598.set(MEMORY.ref(4, cdromDmaSubCallback, RunnableRef::new));
    }

    _800bf59c.setu(a4);
  }

  @Method(0x80030bb0L)
  public static long FUN_80030bb0(long a0) {
    a0 -= _800bf594.get() * 32 + _800bf590.get();
    final long a1 = a0 / 4 / 504;

    if(_800bf590.deref(2).offset(a1 * 32).get() != 0x4L) {
      return 0x1L;
    }

    final long v0 = _800bf590.deref(2).offset(0x6L).offset(a1 * 32).getSigned();

    //LAB_80030c20
    for(a0 = 0; a0 < v0; a0++) {
      _800bf590.deref(2).offset((a0 + a1) * 32).setu(0);
    }

    //LAB_80030c44
    _800bf57c.setu(a0 + a1);

    //LAB_80030c54;
    return 0;
  }

  @Method(0x80030c60L)
  public static void FUN_80030c60(final long a0, final long a1) {
    for(long a2 = 0; a2 < a1; a2++) {
      _800bf590.deref(4).offset((a2 + a0) * 32).setu(0);
    }
  }

  @Method(0x80030ca0L)
  public static boolean FUN_80030ca0(final Value a0, final Value a1) {
    Value a2 = _800bf590.deref(2).offset(_800bf57c.get() * 32);

    if(a2.get() == 0x1L) {
      _800bf57c.setu(0);

      if(_800bf584.get() != 0) {
        a2.setu(0);
      }

      //LAB_80030ce4
      a2 = _800bf590.deref(2).offset(_800bf57c.get() * 32);
    }

    //LAB_80030cfc
    if(a2.get() == 0x2L) {
      a2.setu(0x4L);
      a0.setu(_800bf590.deref(4).offset(_800bf57c.get() * 2016).offset(_800bf594.get() * 32).getAddress());
      a1.setu(a2.getAddress());

      //LAB_80030d50
      return false;
    }

    return true;
  }

  @Method(0x80030d60L)
  public static void FUN_80030d60(final long a0, final long a1, final long a2) {
    _800bf564.setu(a1);
    _800bf584.setu(a2);
    _800bf588.setu(a0);
  }

  @Method(0x80030d80L)
  public static void FUN_80030d80() {
    final long t0;

    if(_800bf56c.get() == 0x1L) {
      return;
    }

    if(_800bf558.get() != 0 && DMA.mdecOut.channelControl.isBusy()) {
      _800bf55c.setu(0x1L);
      if(_800bf580.get() != 0) {
        _800bf570.addu(0x1L);
      }

      //LAB_80030df8
      _80052f24.setu(0x1L);
      return;
    }

    //LAB_80030e04
    final byte[] responses = new byte[DSL_MAX_RESULTS];
    if(FUN_80033090(0x1L, responses) == SyncCode.DISK_ERROR) {
      return;
    }

    // Can't find anywhere these are being used
    //sp22 = responses[0] & 0xffL; // 2b
    //sp24 = responses[1] & 0xffL; // 2b

    if((responses[0] & 0x4L) != 0) {
      _80052f24.setu(0x3L);
      return;
    }

    //LAB_80030e48
    _800bf5a0.setu(_800bf590.get() + _800bf574.get() * 32);

    if(_800bf5a0.deref(2).get() != 0) {
      if(_800bf580.get() != 0) {
        _800bf570.addu(0x1L);
      }

      //LAB_80030ea8
      _80052f24.setu(0x4L);
      return;
    }

    //LAB_80030eb4
    CDROM_REG0.setu(0);
    CDROM_REG3.setu(0); // Reset data FIFO
    CDROM_REG0.setu(0);
    CDROM_REG3.setu(0x80L); // Want data
    CDROM_DELAY.setu(0x2_0943);
    COMMON_DELAY.setu(0x1323L);

    final byte[] sp28 = new byte[0x4];
    if(!sectorIsDataOnly_800bf5f0.get()) {
      //LAB_80030f2c
      // Read CdlLOC, mode from header
      for(int i = 0; i < 0x4; i++) {
        sp28[i] = (byte)CDROM_REG2.get();
      }

      //LAB_80030f60
      // Skip the rest of the header
      for(int i = 0; i < DSL_MAX_RESULTS; i++) {
        CDROM_REG2.get();
      }
    }

    //LAB_80030f74
    if(_800bf580.get() == 0) {
      //LAB_80030fb4
      FUN_800316c8(DmaChannelType.CDROM, _800bf5a0.get(), 0, 0x8L, 0x1100_0000L /* start/busy - start/enable/busy; start/trigger - manual */, false);
    } else {
      FUN_8003169c(_800bf5a0.get(), _800bf580.get() + _800bf570.get() * 0x800, 0x8L, 0);
    }

    //LAB_80030fd8
    //LAB_80030ffc
    while(DMA.cdrom.channelControl.isBusy()) {
      DebugHelper.sleep(1);
    }

    //LAB_80031010
    _800bf5a0.deref(4).offset(0x1cL).setu(MathHelper.get(sp28, 0, 4));
    CDROM_DELAY.setu(0x2_0843);
    COMMON_DELAY.setu(0x1325L);

    // I think this is searching for the first sector if the FMV
    if(_800bf588.get() == 0x1L && _800bf564.get() != 0) {
      if(_800bf564.get() != _800bf5a0.deref(2).offset(0x8L).get()) { // Check MDEC sector sequence number
        _800bf5a0.deref(2).setu(0);
        if(_800bf580.get() != 0) {
          _800bf570.addu(0x1L);
        }

        return;
      }

      //LAB_800310c8
      _800bf588.setu(0);
    }

    //LAB_800310d0
    if(_800bf5a0.deref(2).get() != 0x160L || (_800bf5a0.deref(2).offset(0x2L).get() >>> 0xaL & 0x1fL) != _800bf568.get()) {
      //LAB_80031108
      if(_800bf580.get() == 0) {
        //LAB_80031128
        //v0 = MEMORY.ref(2, a0).get(); // unused??
      } else {
        _800bf570.setu(0);
      }

      //LAB_8003112c
      _80052f24.setu(0x5L);
      _800bf5a0.deref(2).setu(0);
      return;
    }

    //LAB_80031148
    if(_800bf554.get() != _800bf5a0.deref(2).offset(0x4L).get() || _800bf550.get() != 0 && _800bf550.get() != _800bf5a0.deref(2).offset(0x8L).get()) {
      //LAB_80031184
      _800bf550.setu(0);
      _800bf554.setu(0);
      FUN_80030c60(_800bf578.get(), _800bf574.get() - _800bf578.get());
      _800bf574.setu(_800bf578);
      _800bf5a0.deref(2).setu(0);
      if(_800bf580.get() != 0) {
        _800bf570.addu(0x1L);
      }

      //LAB_800311f8
      _80052f24.setu(0x6L);
      return;
    }

    //LAB_80031204
    if(_800bf5a0.deref(2).offset(0x4L).get() == 0) {
      _800bf554.setu(0);
      _800bf550.setu(0);
      if(_800bf584.get() != 0 && _800bf584.get() <= _800bf5a0.deref(2).offset(0x8L).get()) {
        _800bf550.setu(_800bf5a0.deref(2).offset(0x8L));
        _800bf554.setu(0);
        FUN_80030c60(_800bf578.get(), _800bf574.get() - _800bf578.get());
        _800bf574.setu(_800bf578);
        _800bf5a0.deref(2).setu(0);
        _800bf588.setu(0x1L);
        if(_800bf59c.get() != 0) {
          _800bf59c.deref(4).call();
        }

        //LAB_800312b4
        if(_800bf580.get() != 0) {
          _800bf570.addu(0x1L);
        }

        //LAB_800312e4
        _80052f24.setu(0x7L);
        return;
      }

      //LAB_800312f0
      if(_800bf594.get() - _800bf574.get() - 0x1L < _800bf5a0.deref(2).offset(0x6L).get()) {
        if(_800bf584.get() == 0) {
          _800bf5a0.deref(2).setu(0x1L);
          _800bf588.setu(0x1L);
          if(_800bf59c.get() != 0) {
            _800bf59c.deref(4).call();
          }

          //LAB_80031358
          if(_800bf580.get() != 0) {
            _800bf570.addu(0x1L);
          }

          //LAB_80031388
          _80052f24.setu(0x8L);
          return;
        }

        //LAB_80031394
        if(_800bf590.deref(2).get() != 0) {
          _800bf5a0.deref(2).setu(0);
          if(_800bf580.get() != 0) {
            _800bf570.addu(0x1L);
          }

          //LAB_800313e4
          _80052f24.setu(0x9L);
          return;
        }

        //LAB_800313f0
        _800bf5a0.deref(2).setu(0x1L);
        _800bf574.setu(0);

        //LAB_80031410
        for(int i = 0; i < DSL_MAX_RESULTS; i++) { // Not 100% sure if this is the right constant, but it's 0x8
          _800bf590.deref(4).offset(i * 0x4L).setu(_800bf5a0.deref(4).offset(i * 0x4L));
        }

        _800bf5a0.setu(_800bf590);
      }

      //LAB_8003143c
      _800bf578.setu(_800bf574);
    }

    //LAB_80031450
    _80052f24.setu(0xaL);
    _800bf554.addu(0x1L);
    _800bf58c.setu(_800bf590.get() + _800bf574.get() * 0x7e0 + _800bf594.get() * 32);
    if(_800bf558.get() == 0) {
      //LAB_800314dc
      CDROM_DELAY.setu(0x2102_0843L);
      t0 = 0x1140_0100L;
    } else {
      CDROM_DELAY.setu(0x2_0943L);
      COMMON_DELAY.setu(0x1323L);
      t0 = 0x1100_0000L;
    }

    //LAB_800314f8
    if(_800bf5a0.deref(2).offset(0x6L).get() - 0x1L == _800bf5a0.deref(2).offset(0x4L).get()) {
      _800bf56c.setu(0x1L);
      if(_800bf580.get() == 0) {
        //LAB_80031570
        FUN_800316c8(DmaChannelType.CDROM, _800bf58c.get(), 0, 0x1f8L, t0, true);
      } else {
        FUN_8003169c(_800bf58c.get(), _800bf580.get() + _800bf570.get() * 0x800 + 0x20L, 0x1f8L, 0x1L);
        _800bf570.addu(0x1L);
      }

      //LAB_80031594
      _800bf554.setu(0);
      _800bf550.setu(0);
      _800bf568.setu(_800bf560);
    } else {
      //LAB_800315b8
      if(_800bf580.get() == 0) {
        //LAB_8003160c
        FUN_800316c8(DmaChannelType.CDROM, _800bf58c.get(), 0, 0x1f8L, t0, false);
      } else {
        FUN_8003169c(_800bf58c.get(), _800bf580.get() + _800bf570.get() * 0x800 + 0x20L, 0x1f8L, 0);
        _800bf570.addu(0x1L);
      }
    }

    //LAB_80031630
    COMMON_DELAY.setu(0x1325L);
    _800bf5a0.deref(2).setu(0x3L);
    _800bf574.addu(0x1L);
    if(_800bf580.get() != 0 && _800bf56c.get() != 0) {
      cdromDmaCallback80031870();
    }

    //LAB_8003168c
  }

  @Method(0x8003169cL)
  public static void FUN_8003169c(final long a0, final long a1, final long a2, final long a3) {
    assert false;
  }

  @Method(0x800316c8L)
  public static void FUN_800316c8(final DmaChannelType dmaChannel, final long dmaAddress, final long blockCount, final long blockSize, final long channelControl, final boolean enableInterrupt) {
    final DmaChannel channel = DMA.channel(dmaChannel);

    //LAB_8003171c
    long a0 = 0;
    while(channel.channelControl.isBusy()) {
      if(a0 == 0x1_0000L) {
        //LAB_80031768
        LOGGER.error("DMA STATUS ERROR %x", DMA.mdecIn.CHCR);
        break;
      }

      a0++;
    }

    //LAB_80031740
    //LAB_80031744
    if(enableInterrupt) {
      DMA.enableInterrupt(dmaChannel);
    } else {
      //LAB_80031788
      DMA.disableInterrupt(dmaChannel);
    }

    //LAB_800317a0
    channel.enable();
    channel.MADR.setu(dmaAddress);
    channel.BCR.setu(blockCount << 16 | blockSize);

    //LAB_80031824
    while(CDROM_REG0.get(0b100_0000L) == 0) { // Wait for data FIFO to become non-empty
      DebugHelper.sleep(1);
    }

    //LAB_80031838
    channel.CHCR.setu(channelControl);
  }

  @Method(0x80031870L)
  public static void cdromDmaCallback80031870() {
    final long v1 = _800bf590.deref(2).offset(_800bf578.get() * 32).getAddress();
    MEMORY.ref(2, v1).setu(0x2L);
    _800bf5b0.setu(MEMORY.ref(4, v1).offset(0x1cL));
    _800bf5b4.setu(MEMORY.ref(4, v1).offset(0x8L));
    _800bf578.setu(_800bf574);

    if(!cdromDmaSubCallback_800bf598.isNull()) {
      cdromDmaSubCallback_800bf598.deref().run();
    }

    //LAB_800318e4
    _800bf56c.setu(0);
  }

  /**
   * @return A bitmask telling which callbacks to execute. Bit 1 is set on interrupts 2, 3, 5 (complete callback). Bit 2 is set on interrupts 1, 4, 5 (data callback).
   */
  @Method(0x800319e0L)
  public static long processCdromInterrupt() {
    SyncCode syncCode = CDROM.readSyncCode(); // Read interrupt response

    if(syncCode == SyncCode.NO_INTERRUPT) {
      return 0;
    }

    //LAB_80031a44
    //LAB_80031a34
    while(syncCode != CDROM.readSyncCode()) {
      syncCode = CDROM.readSyncCode(); // Read interrupt response
    }

    int resultIndex;
    final long[] results = new long[DSL_MAX_RESULTS];

    //LAB_80031a5c
    for(resultIndex = 0; resultIndex < DSL_MAX_RESULTS; resultIndex++) {
      if(CDROM_REG0.get(0b10_0000L) == 0) { // Not RSLRRDY
        break;
      }

      results[resultIndex] = CDROM.readResponse(); // Read response to stack
    }

    //LAB_80031ac8
    CDROM.acknowledgeInterrupts(0x7L); // Acknowledge interrupts
    CDROM.enableInterrupts(0x7L); // Enable interrupts

    if(syncCode != SyncCode.ACKNOWLEDGE || _80053108.get(previousCdromCommand_80052f61.get().command).get() != 0) {
      //LAB_80031b30
      //LAB_80031b74
      // error, seek error, ID error, or shell open
      final long errors = results[0] & 0b1_1101L;

      if(errors != 0) {
        throw new RuntimeException("CDROM errors " + Long.toHexString(errors));
      }

      if(_80052f50.get(0x10L) == 0) {
        if((results[0] & 0x10L) != 0) {
          _80052f58.addu(0x1L);
        }
      }

      _80052f50.setu(results[0]);
      _80052f54.setu(results[1]);
    }

    //LAB_80031b94
    if(syncCode == SyncCode.DISK_ERROR) {
      throw new RuntimeException("com=%s,code=(%02x:%02x)".formatted(previousCdromCommand_80052f61.get().name(), _80052f50.get(), _80052f54.get()));
    }

    //LAB_80031c14
    LOGGER.debug("Handling CDROM interrupt %s %s", syncCode, Arrays.toString(results));

    switch(syncCode) {
      case ACKNOWLEDGE -> {
        //LAB_80031c90
        if(_80053008.get(previousCdromCommand_80052f61.get().command).get() != 0) {
          cdromSyncCode_80053220.set(SyncCode.ACKNOWLEDGE);

          //LAB_80031cdc
          for(int i = 0; i < DSL_MAX_RESULTS; i++) {
            cdromResponses_800bf5c0.get(i).set((byte)results[i]);
          }

          //LAB_80031cf4
          return 0x1L;
        }

        //LAB_80031cfc
        cdromSyncCode_80053220.set(SyncCode.COMPLETE);

        //LAB_80031d24
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5c0.get(i).set((byte)results[i]);
        }

        return 0x2L;
      }

      case COMPLETE -> {
        //LAB_80031d50
        cdromSyncCode_80053220.set(SyncCode.COMPLETE);

        //LAB_80031d70
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5c0.get(i).set((byte)results[i]);
        }

        //LAB_80031d88
        return 0x2L;
      }

      case DATA_READY -> {
        //LAB_80031da4
        //LAB_80031db0
        cdromDataSyncCode_80053221.set(SyncCode.DATA_READY);

        //LAB_80031dd4
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5c8.get(i).set((byte)results[i]);
        }

        //LAB_80031dec
        CDROM_REG0.setu(0); // Index 0
        CDROM_REG3.setu(0); // Reset data FIFO
        return 0x4L;
      }

      case DATA_END -> {
        cdromDataSyncCode_80053221.set(SyncCode.DATA_END);
        cdromDataEndSyncCode_80053222.set(SyncCode.DATA_END);

        //LAB_80031e40
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5d0.get(i).set((byte)results[i]);
        }

        //LAB_80031e58
        //LAB_80031e70
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5c8.get(i).set((byte)results[i]);
        }

        //LAB_80031e88
        return 0x4L;
      }
    }

    LOGGER.error("CDROM: unknown intr (%s)", syncCode);

    //LAB_80031f2c
    //LAB_80031f30
    return 0;
  }

  @Method(0x80031f44L)
  public static SyncCode FUN_80031f44(final long doNotRetry, final long responseAddress) {
    final long timeout = VSync(-1) + 960L;
    long counter = 0;
    _800bf5e0.set(_80011394);

    //LAB_80031fc4
    do {
      if(timeout < VSync(-1) || counter++ > 0x3c_0000L) {
        //LAB_80032010
        LOGGER.error("CD timeout: %s:(%s) Sync=%s, Ready=%s", _800bf5e0.deref().get(), previousCdromCommand_80052f61.get().name(), cdromSyncCode_80053220.get().name, cdromDataSyncCode_80053221.get().name);
        DsFlush();
        throw new RuntimeException("Timeout");
      }

      //LAB_80032078
      if(CheckCallback()) {
        final long s1 = CDROM_REG0.get(0b11L);

        //LAB_800320ac
        do {
          final long callbacks = processCdromInterrupt();
          if(callbacks == 0) {
            break;
          }

          if((callbacks & 0x4L) != 0 && !cdromDataInterruptCallbackPtr_80052f48.isNull()) {
            cdromDataInterruptCallbackPtr_80052f48.deref().run(cdromDataSyncCode_80053221.get(), MEMORY.getBytes(cdromResponses_800bf5c8.getAddress(), DSL_MAX_RESULTS));
          }

          //LAB_800320ec
          //LAB_800320f0
          if((callbacks & 0x2L) != 0 && !cdromCompleteInterruptCallbackPtr_80052f44.isNull()) {
            cdromCompleteInterruptCallbackPtr_80052f44.deref().run(cdromSyncCode_80053220.get(), MEMORY.getBytes(cdromResponses_800bf5c0.getAddress(), DSL_MAX_RESULTS));
          }
        } while(true);

        //LAB_80032124
        CDROM_REG0.setu(s1);
      }

      //LAB_80032134
      final SyncCode syncCode = cdromSyncCode_80053220.get();
      if(syncCode == SyncCode.COMPLETE || syncCode == SyncCode.DISK_ERROR) {
        //LAB_80032150
        cdromSyncCode_80053220.set(SyncCode.COMPLETE);
        if(responseAddress != 0) {
          //LAB_8003216c
          for(int i = 0; i < DSL_MAX_RESULTS; i++) {
            MEMORY.ref(1, responseAddress).offset(i).setu(cdromResponses_800bf5c0.get(i).get());
          }
        }

        //LAB_80032184
        return syncCode;
      }

      //LAB_8003218c
      if(doNotRetry != 0) {
        return SyncCode.NO_INTERRUPT;
      }

      DebugHelper.sleep(1);
    } while(true);
  }

  /**
   * CDROM responses will be written to responses
   */
  @Method(0x800321c4L)
  public static SyncCode FUN_800321c4(final long doNotRetry, @Nullable final byte[] responses) {
    assert responses == null || responses.length == 8;

    final long timeout = VSync(-1) + 960L;
    long counter = 0;
    _800bf5e0.set(_8001139c);

    //LAB_80032244
    do {
      if(timeout < VSync(-1) || counter > 0x3c_0000) {
        //LAB_80032290
        LOGGER.error("CD timeout:");
        LOGGER.error("String: %s:(%s) Sync=%s, Ready=%s", _800bf5e0.deref().get(), previousCdromCommand_80052f61.get().name(), syncCode_80053320.get().name, cdromDataSyncCode_80053221.get().name);
        DsFlush();
        throw new RuntimeException("Timeout");
      }

      counter++;

      //LAB_800322f8
      //LAB_800322fc
      if(CheckCallback()) {
        final long s1 = CDROM_REG0.get(0x3L);

        //LAB_8003232c
        do {
          final long callbacks = processCdromInterrupt();
          if(callbacks == 0) {
            break;
          }

          if((callbacks & 0x4L) != 0 && !cdromDataInterruptCallbackPtr_80052f48.isNull()) {
            final byte[] data = MEMORY.getBytes(cdromResponses_800bf5c8.getAddress(), 8);
            cdromDataInterruptCallbackPtr_80052f48.deref().run(cdromDataSyncCode_80053221.get(), data);
          }

          //LAB_8003236c
          //LAB_80032370
          if((callbacks & 0x2L) != 0 && !cdromCompleteInterruptCallbackPtr_80052f44.isNull()) {
            final byte[] data = MEMORY.getBytes(cdromResponses_800bf5c0.getAddress(), 8);
            cdromCompleteInterruptCallbackPtr_80052f44.deref().run(cdromSyncCode_80053220.get(), data);
          }
        } while(true);

        //LAB_800323a4
        CDROM_REG0.setu(s1);
      }

      //LAB_800323b4
      SyncCode syncCode = cdromDataEndSyncCode_80053222.get();
      if(syncCode != SyncCode.NO_INTERRUPT) {
        cdromDataEndSyncCode_80053222.set(SyncCode.NO_INTERRUPT);

        if(responses != null) {
          //LAB_800323e4
          for(int i = 0; i < DSL_MAX_RESULTS; i++) {
            responses[i] = cdromResponses_800bf5d0.get(i).get();
          }
        }

        return syncCode;
      }

      //LAB_80032404
      final SyncCode syncCode2 = cdromDataSyncCode_80053221.get();
      if(syncCode2 != SyncCode.NO_INTERRUPT) {
        cdromDataSyncCode_80053221.set(SyncCode.NO_INTERRUPT);

        if(responses != null) {
          //LAB_80032434
          for(int i = 0; i < DSL_MAX_RESULTS; i++) {
            responses[i] = cdromResponses_800bf5c8.get(i).get();
          }
        }

        //LAB_8003244c
        return syncCode2;
      }

      //LAB_80032454
    } while(doNotRetry == 0);

    //LAB_8003245c
    return SyncCode.NO_INTERRUPT;
  }

  /**
   * TODO this is not actually DsControl
   */
  @Method(0x8003248cL)
  public static long DsControl(final CdlCOMMAND command, final long paramAddress, long responseAddress, final boolean async) {
    if(CD_debug_80052f4c.get() >= 0x2L) {
      LOGGER.info("%s...", command.toString());
    }

    //LAB_800324f0
    if(command.paramCount != 0 && paramAddress == 0) {
      throw new RuntimeException("Param count set but no param address specified");
    }

    //LAB_8003254c
    FUN_80031f44(0, 0);

    if(command == CdlCOMMAND.SET_LOC_02) {
      //LAB_8003256c
      for(int i = 0; i < 4; i++) {
        cdromParamBuffer_80052f5c.get(i).setUnsigned(MEMORY.ref(1, paramAddress).offset(i).get());
      }

      //LAB_80032594
    } else if(command == CdlCOMMAND.SET_MODE_0E) {
      cdromMode_80052f60.setu(MEMORY.ref(1, paramAddress));
    }

    //LAB_800325a8
    cdromSyncCode_80053220.set(SyncCode.NO_INTERRUPT);
    if(_80053088.get(command.command).get() != 0) {
      cdromDataSyncCode_80053221.set(SyncCode.NO_INTERRUPT);
    }

    //LAB_800325d4
    CDROM_REG0.setu(0);

    //LAB_80032604
    final long[] params = new long[command.paramCount];
    for(int param = 0; param < command.paramCount; param++) {
      params[param] = MEMORY.ref(1, paramAddress).offset(param).get();
    }

    CDROM.sendCommand(command, params);

    //LAB_8003262c
    previousCdromCommand_80052f61.set(command);

    if(async) {
      return 0;
    }

    final long timeout = VSync(-1) + 960L;
    long counter = 0;
    _800bf5e0.set(_800113c0);

    //LAB_800326a0
    while(cdromSyncCode_80053220.get() == SyncCode.NO_INTERRUPT) {
      if(timeout < VSync(-1) || counter++ > 0x3c_0000L) {
        //LAB_800326ec
        LOGGER.error("CD timeout:");

        LOGGER.error(
          "%s:(%s) Sync=%s, Ready=%s",
          _800bf5e0.deref().get(),
          previousCdromCommand_80052f61.get().toString(),
          cdromSyncCode_80053220.get().name,
          cdromDataSyncCode_80053221.get().name
        );
        DsFlush();

        return 0xffff_ffffL;
      }

      //LAB_80032754
      //LAB_80032758

      if(CheckCallback()) {
        final long oldIndex = CDROM_REG0.get(0x3L);

        //LAB_80032788
        long callbacks;
        while((callbacks = processCdromInterrupt()) != 0) {
          if((callbacks & 0x4L) != 0 && !cdromDataInterruptCallbackPtr_80052f48.isNull()) {
            cdromDataInterruptCallbackPtr_80052f48.deref().run(cdromDataSyncCode_80053221.get(), MEMORY.getBytes(cdromResponses_800bf5c8.getAddress(), DSL_MAX_RESULTS));
          }

          //LAB_800327c8
          //LAB_800327cc
          if((callbacks & 0x2L) != 0 && !cdromCompleteInterruptCallbackPtr_80052f44.isNull()) {
            cdromCompleteInterruptCallbackPtr_80052f44.deref().run(cdromSyncCode_80053220.get(), MEMORY.getBytes(cdromResponses_800bf5c0.getAddress(), DSL_MAX_RESULTS));
          }
        }

        //LAB_80032800
        CDROM_REG0.setu(oldIndex);
      }

      //LAB_80032810
      DebugHelper.sleep(1);
    }

    //LAB_80032820
    if(responseAddress != 0) {
      //LAB_80032834
      for(int offset = 0; offset < DSL_MAX_RESULTS; offset++) {
        MEMORY.ref(1, responseAddress++).setu(cdromResponses_800bf5c0.get(offset).get());
      }
    }

    //LAB_8003284c
    if(cdromSyncCode_80053220.get() != SyncCode.DISK_ERROR) {
      return 0;
    }

    //LAB_80032870
    return 0xffff_ffffL;
  }

  @Method(0x80032898L)
  public static void CdMix_Impl(final long cdLeftToSpuLeft, final long cdLeftToSpuRight, final long cdRightToSpuRight, final long cdRightToSpuLeft) {
    CDROM_REG0.setu(0x2L); // Index 2
    CDROM_REG2.setu(cdLeftToSpuLeft); // Audio Volume for Left-CD-Out to Left-SPU-Input
    CDROM_REG3.setu(cdLeftToSpuRight); // Audio Volume for Left-CD-Out to Right-SPU-Input

    CDROM_REG0.setu(0x3L); // Index 3
    CDROM_REG1.setu(cdRightToSpuRight); // Audio Volume for Right-CD-Out to Right-SPU-Input
    CDROM_REG2.setu(cdRightToSpuLeft); // Audio Volume for Right-CD-Out to Left-SPU-Input

    CDROM_REG3.setu(0x20L); // Commit
  }

  /**
   * All commands that have been entered in the command queue are flushed. Currently executing commands
   * are allowed to complete, but the results are not saved and callbacks are not invoked.
   *
   * If a command is executing when this function is called, it is allowed to complete. Subsequent commands
   * are put into a new queue.
   */
  @Method(0x80032920L)
  public static void DsFlush() {
    CDROM_REG0.setu(0x1L); // Index1

    //LAB_80032954
    while(CDROM_REG3.get(0b111L) != 0) { // Get interrupt #
      CDROM_REG0.setu(0x1L); // Index1
      CDROM_REG3.setu(0x7L); // Acknowledge interrupt
      CDROM_REG2.setu(0b111L); // Enable interrupts 1-7
    }

    //LAB_800329a4
    cdromSyncCode_80053220.set(SyncCode.COMPLETE);
    cdromDataSyncCode_80053221.set(SyncCode.NO_INTERRUPT);
    cdromDataEndSyncCode_80053222.set(SyncCode.NO_INTERRUPT);

    CDROM_REG0.setu(0); // Index0
    CDROM_REG3.setu(0); // Reset FIFO

    COMMON_DELAY.setu(0x1325L);
  }

  @Method(0x800329f4L)
  public static void setCdAndSpuVolume() {
    if(SPU.CURR_MAIN_VOL_L.get() == 0 && SPU.CURR_MAIN_VOL_R.get() == 0) {
      SPU.MAIN_VOL_L.set(0x3fff);
      SPU.MAIN_VOL_R.set(0x3fff);
    }

    //LAB_80032a30
    //LAB_80032a34
    SPU.CD_VOL_L.set(0x3fff);
    SPU.CD_VOL_R.set(0x3fff);

    SPU.SPUCNT.set(0xc001);
    CDROM_REG0.setu(0b010L);      // Index2
    CDROM_REG2.setu(0x80L);       // Audio Volume for Left-CD-Out to Left-SPU-Input
    CDROM_REG3.setu(0x00L);       // Audio Volume for Left-CD-Out to Right-SPU-Input
    CDROM_REG0.setu(0b011L);      // Index3
    CDROM_REG1.setu(0x80L);       // Audio Volume for Right-CD-Out to Right-SPU-Input
    CDROM_REG2.setu(0x0L);        // Audio Volume for Right-CD-Out to Left-SPU-Input
    CDROM_REG3.setu(0b00100000L); // Apply changes
  }

  @Method(0x80032b30L)
  public static long initCd() {
    LOGGER.info("CD_init:");
    LOGGER.info("addr=%08x", ptrCdromSyncCode_80053220_80053224.getAddress());

    cdromCompleteInterruptCallbackPtr_80052f44.clear();
    cdromDataInterruptCallbackPtr_80052f48.clear();
    _80052f50.setu(0);
    _80052f54.setu(0);
    cdromMode_80052f60.setu(0);
    previousCdromCommand_80052f61.set(CdlCOMMAND.NONE_00);

    ResetCallback();
    InterruptCallback(InterruptType.CDROM, getMethodAddress(Scus94491BpeSegment_8003.class, "handleCdromInterrupt"));

    CDROM_REG0.setu(0b001L); // Index 1

    //LAB_80032bd4
    while(CDROM_REG3.get(0b111L) != 0) { // Is there an interrupt?
      CDROM_REG0.setu(0b001L); // Index 1
      CDROM_REG3.setu(0b111L); // Acknowledge interrupt - reset response bits
      CDROM_REG2.setu(0b111L); // Enable interrupts 1-3
    }

    //LAB_80032c24
    cdromSyncCode_80053220.set(SyncCode.COMPLETE);
    cdromDataSyncCode_80053221.set(SyncCode.NO_INTERRUPT);
    cdromDataEndSyncCode_80053222.set(SyncCode.NO_INTERRUPT);

    CDROM_REG0.setu(0b000); // Index 0
    CDROM_REG3.setu(0b000);
    COMMON_DELAY.setu(0x1325L);

    CDROM.sendCommand(CdlCOMMAND.GET_STAT_01);
    CDROM.acknowledgeInterrupts();

    if(_80052f50.get(0x10L) == 0) {
      CDROM.sendCommand(CdlCOMMAND.GET_STAT_01);
      CDROM.acknowledgeInterrupts();
    }

    //LAB_80032ca4
    CDROM.sendCommand(CdlCOMMAND.INIT_0A);
    CDROM.acknowledgeInterrupts();

    CDROM.sendCommand(CdlCOMMAND.DEMUTE_0C);
    CDROM.acknowledgeInterrupts();

    //LAB_80032cfc
    //LAB_80032d00
    return 0;
  }

  @Method(0x80032e84L)
  public static void handleCdromInterrupt() {
    final long s2 = CDROM_REG0.get() & 0x3L;

    //LAB_80032eb8
    do {
      final long callbacks = processCdromInterrupt();
      if(callbacks == 0) {
        break;
      }

      if((callbacks & 0x4L) != 0 && !cdromDataInterruptCallbackPtr_80052f48.isNull()) {
        final byte[] responses = MEMORY.getBytes(cdromResponses_800bf5c8.getAddress(), DSL_MAX_RESULTS);
        cdromDataInterruptCallbackPtr_80052f48.deref().run(cdromDataSyncCode_80053221.get(), responses);
      }

      //LAB_80032ef8
      //LAB_80032efc
      if((callbacks & 0x2L) != 0 && !cdromCompleteInterruptCallbackPtr_80052f44.isNull()) {
        final byte[] responses = MEMORY.getBytes(cdromResponses_800bf5c0.getAddress(), DSL_MAX_RESULTS);
        cdromCompleteInterruptCallbackPtr_80052f44.deref().run(cdromSyncCode_80053220.get(), responses);
      }
    } while(true);

    //LAB_80032f30
    CDROM_REG0.setu(s2);
  }

  /**
   * CDROM responses will be written to responses
   */
  @Method(0x80033090L)
  public static SyncCode FUN_80033090(final long doNotRetry, @Nullable final byte[] responses) {
    return FUN_800321c4(doNotRetry, responses);
  }

  @Method(0x800330b0L)
  public static void setCdromDataInterruptCallbackPtr(final long address) {
    cdromDataInterruptCallbackPtr_80052f48.set(MEMORY.ref(4, address, BiConsumerRef::new));
  }

  @Method(0x800330d0L)
  public static void setCdromDmaCallback(final long address) {
    SetDmaInterruptCallback(DmaChannelType.CDROM, address);
  }

  @Method(0x80033100L)
  public static void clearPacket(final CdlPacket packet) {
    packet.clear();
  }

  @Method(0x80033130L)
  public static void clearPacketBatch() {
    final long batch = CdlPacket_800bf638.get((int)_800bf6f8.get()).batch.get();

    //LAB_80033160
    do {
      if(cdlPacketIndex_800bf700.getSigned() <= 0) {
        break;
      }

      CdlPacket_800bf638.get((int)_800bf6f8.get()).clear();

      _800bf6f8.addu(0x1L);
      if(_800bf6f8.get() >= DSL_MAX_COMMAND) {
        _800bf6f8.setu(0);
      }

      //LAB_800331d4
      cdlPacketIndex_800bf700.subu(0x1L);
    } while(CdlPacket_800bf638.get((int)_800bf6f8.get()).batch.get() == batch);

    //LAB_80033208
    _800bf6fc.setu(_800bf6f8);
  }

  @Method(0x8003321cL)
  public static void clearPacketQueue() {
    _800bf6f8.setu(0);
    _800bf6fc.setu(0);
    cdlPacketIndex_800bf700.setu(0);

    //LAB_80033238
    for(int packetIndex = 0; packetIndex < DSL_MAX_COMMAND; packetIndex++) {
      clearPacket(CdlPacket_800bf638.get(packetIndex));
    }
  }

  @Method(0x8003327cL)
  public static CdlPacket getCurrentPacket() {
    if(cdlPacketIndex_800bf700.get() >= DSL_MAX_COMMAND) {
      throw new RuntimeException("CDROM packet queue overflow");
    }

    final long oldIMask = setIMask(0);

    long s0 = _800bf6f8.get() + cdlPacketIndex_800bf700.get();
    if(s0 >= DSL_MAX_COMMAND) {
      s0 -= DSL_MAX_COMMAND;
    }

    //LAB_800332d4
    setIMask(oldIMask);

    //LAB_800332f0
    return CdlPacket_800bf638.get((int)s0);
  }

  @Method(0x80033304L)
  public static void FUN_80033304(final SyncCode syncCode, final byte[] responses) {
    assert false;
  }

  @Method(0x800334f4L)
  public static boolean sendQueuedPacket() {
    if(FUN_8003475c(0) != 0x1L) {
      return false;
    }

    final CdlPacket packet = CdlPacket_800bf638.get((int)_800bf6fc.get());

    if(packet.batch.get() == 0) {
      return false;
    }

    return FUN_800346b0(packet.command.get(), cdromParamsPtr_800bf644.offset(_800bf6fc.get() * 24L).get()) > 0;
  }

  @Method(0x80033564L)
  public static void FUN_80033564(final SyncCode syncCode, final byte[] responses) {
    final CdlPacket packet = CdlPacket_800bf638.get((int)_800bf6fc.get());

    if(packet.batch.get() != 0) {
      batch_800bf608.setu(packet.batch.get());
      syncCode_800bf60c.set(syncCode);

      MEMORY.setBytes(response_800bf60d.getAddress(), responses);

      if(syncCode == SyncCode.COMPLETE) {
        //LAB_800335f0
        //LAB_8003360c
        if(CdlPacket_800bf638.get((int)((_800bf6fc.get() + 0x1L) % DSL_MAX_COMMAND)).batch.get() == packet.batch.get()) {
          //LAB_80033654
          _800bf6fc.setu((_800bf6fc.get() + 0x1L) % DSL_MAX_COMMAND);
        } else {
          cacheCompletedPacketResponse(packet.batch.get(), SyncCode.COMPLETE, responses);

          if(!packet.syncCallback.isNull()) {
            //LAB_800336bc
            packet.syncCallback.deref().run(SyncCode.COMPLETE, responses);
          }

          //LAB_800336c4
          clearPacketBatch();
        }
      } else if(syncCode == SyncCode.DISK_ERROR) {
        //LAB_80033664
        if(packet.retries.get() > 0 || packet.retries.get() == -0x1L) {
          //LAB_8003367c
          _800bf6fc.setu(_800bf6f8);

          if(packet.retries.get() != -0x1L) {
            packet.retries.decr();
          }
        } else {
          //LAB_800336a0
          cacheCompletedPacketResponse(packet.batch.get(), SyncCode.DISK_ERROR, responses);

          if(!packet.syncCallback.isNull()) {
            //LAB_800336bc
            packet.syncCallback.deref().run(SyncCode.DISK_ERROR, responses);
          }

          //LAB_800336c4
          clearPacketBatch();
        }
      }
    }

    //LAB_800336cc
    if(_800c1bb0.get() != 0) {
      _800c1bb0.deref(4).cast(BiConsumerRef::new).run(syncCode, responses);
    }

    //LAB_800336e8
    if(FUN_8003475c(0) != 0x1L) {
      return;
    }

    if(cdlPacketIndex_800bf700.getSigned() <= 0) {
      return;
    }

    if(FUN_8003475c(0) != 0x1L) {
      return;
    }

    final CdlPacket packet2 = CdlPacket_800bf638.get((int)_800bf6fc.get());

    if(packet2.batch.get() != 0) {
      FUN_800346b0(packet2.command.get(), packet2.getArgsAddress());
    }

    //LAB_80033768
  }

  @Method(0x80033788L)
  public static void cacheCompletedPacketResponse(final long packetBatch, final SyncCode syncCode, final byte[] responses) {
    final Response response = cdromResponseBuffer_800bf708.get((int)cdromResponseBufferIndex_800bf788.get());
    response.setBatch(packetBatch);
    response.setSyncCode(syncCode);
    response.setResponses(responses);

    cdromResponseBufferIndex_800bf788.addu(0x1L);
    if(cdromResponseBufferIndex_800bf788.get() >= DSL_MAX_COMMAND) {
      cdromResponseBufferIndex_800bf788.setu(0);
    }

    //LAB_80033804
  }

  @Method(0x80033814L)
  public static long resetCdromStuff() {
    if(get800532e4() != 0) {
      return 0;
    }

    _800c1bb0.setu(0);
    cdromReadCompleteSubSubCallbackPtr_800c1bb4.clear();
    _800c1bb8.clear();

    batch_800bf608.setu(0);
    batch_800bf618.setu(0);
    batch_800bf628.setu(0);

    syncCode_800bf60c.set(SyncCode.NO_INTERRUPT);
    syncCode_800bf61c.set(SyncCode.NO_INTERRUPT);
    syncCode_800bf62c.set(SyncCode.NO_INTERRUPT);

    //LAB_80033868
    for(int i = 0; i < DSL_MAX_COMMAND; i++) {
      response_800bf60d.offset(1, i).setu(0);
      response_800bf61d.offset(1, i).setu(0);
      response_800bf62d.offset(1, i).setu(0);
    }

    //LAB_800338a4
    for(int i = 0; i < DSL_MAX_COMMAND; i++) {
      clearPacket(CdlPacket_800bf638.get(i));
    }

    _800bf6f8.setu(0);
    _800bf6fc.setu(0);
    cdlPacketIndex_800bf700.setu(0);

    //LAB_800338dc
    for(int i = 0; i < DSL_MAX_COMMAND; i++) {
      cdromResponseBuffer_800bf708.get(i).setBatch(0);
    }

    cdromResponseBufferIndex_800bf788.setu(0);

    DsInit();

    set800bf79c(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_80033564", SyncCode.class, byte[].class), BiConsumerRef::new));
    setCdromReadCompleteSubCallbackPtr_800bf7a0(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "cdromReadCompleteSubCallback", SyncCode.class, byte[].class), BiConsumerRef::new));
    set800bf7a4(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_800344b8", SyncCode.class, byte[].class), BiConsumerRef::new));
    set800bf798(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_80034340"), RunnableRef::new));
    FUN_80036b38();
    set80053498(0);

    //LAB_80033944
    return 1;
  }

  @Method(0x80033a64L)
  public static long DsCommand(final CdlCOMMAND command, final long params, final long syncCallback, final int retries) {
    if(_8005324c.offset(command.command * 0x4L).get() != 0 && params != 0) {
      if(cdlPacketIndex_800bf700.get() >= DSL_MAX_COMMAND) {
        return 0;
      }

      cdlPacketBatch_800532cc.add(0x1L);
      if(cdlPacketBatch_800532cc.getSigned() == 0) {
        cdlPacketBatch_800532cc.add(0x1L);
      }

      //LAB_80033afc
      final long currentBatch = cdlPacketBatch_800532cc.get();
      final CdlPacket packet = getCurrentPacket();

      packet.batch.set(currentBatch);
      packet.command.set(CdlCOMMAND.SET_LOC_02);

      if(params == 0) {
        //LAB_80033b30
        packet.clearArgs();
      } else {
        packet.setArgs((byte)MEMORY.ref(1, params).get(), (byte)MEMORY.ref(1, params + 1).get(), (byte)MEMORY.ref(1, params + 2).get(), (byte)MEMORY.ref(1, params + 3).get());
      }

      //LAB_80033b34
      packet.syncCallback.clear();
      packet.retries.set(0);

      final long oldIMask = setIMask(0);
      cdlPacketIndex_800bf700.addu(0x1L);
      setIMask(oldIMask);

      if(FUN_8003475c(0) == 0x1L) {
        if(CdlPacket_800bf638.get((int)_800bf6fc.get()).batch.get() == currentBatch) {
          sendQueuedPacket();
        }
      }

      //LAB_80033bb4
      if(currentBatch == 0) {
        return 0;
      }
    }

    //LAB_80033bbc
    if(cdlPacketIndex_800bf700.get() >= DSL_MAX_COMMAND) {
      throw new RuntimeException("Packet queue overflow");
//      return 0;
    }

    cdlPacketBatch_800532cc.add(0x1L);
    if(cdlPacketBatch_800532cc.getSigned() == 0) {
      cdlPacketBatch_800532cc.add(0x1L);
    }

    //LAB_80033bfc
    final long currentBatch = cdlPacketBatch_800532cc.get();
    final CdlPacket packet = getCurrentPacket();

    packet.batch.set(cdlPacketBatch_800532cc.get());
    packet.command.set(command);

    if(params == 0) {
      //LAB_80033c2c
      packet.clearArgs();
    } else {
      packet.setArgs((byte)MEMORY.ref(1, params).get(), (byte)MEMORY.ref(1, params + 1).get(), (byte)MEMORY.ref(1, params + 2).get(), (byte)MEMORY.ref(1, params + 3).get());
    }

    //LAB_80033c30
    packet.syncCallback.set(MEMORY.ref(4, syncCallback).cast(BiConsumerRef::new));
    packet.retries.set(retries);

    final long oldIMask = setIMask(0);
    cdlPacketIndex_800bf700.addu(0x1L);
    setIMask(oldIMask);

    if(FUN_8003475c(0) == 0x1L && CdlPacket_800bf638.get((int)_800bf6fc.get()).batch.get() == currentBatch) {
      sendQueuedPacket();
    }

    //LAB_80033cb0
    return currentBatch;
  }

  /**
   * Adds a sequence of commands that perform a data read (playback) to the queue.
   *
   * The commands added to the queue are: DslPause; DslSetmode mode; DslSetloc pos; com.
   *
   * The commands that can be specified for com are: DslPlay, DslReadN, DslReadS, DslSeekP, or DslSeekL.
   * If com is DslPlay, DslReadN, or DslReadS, execution is performed up to and including the data read
   * (playback). If com is DslSeekP or DslSeekL, the seek is performed and the system enters a pause state.
   *
   * If any command in the sequence generates an error, a retry is performed starting with the first command,
   * up to a maximum of count times (or unlimited times if count=-1). An error is generated if the operation is
   * not successful after count retries.
   *
   * DsSync() can be used to obtain the execution status. When all the commands in the sequence are
   * successful or if an error is generated, the callback function cbsync is triggered.
   *
   * An error is generated if the queue does not have enough space for the command sequence.
   *
   * @param mode Operating mode
   * @param pos DslLOC structure specifying target position
   * @param command Last command to be executed
   * @param cbSync Callback function to be triggered when all commands have been executed
   * @param retries Retry count (0: no retries, -1: unlimited retries)
   *
   * @return The command ID (>0) if the command was added to the queue; 0 if the command failed.
   */
  @Method(0x80033cd8L)
  public static long DsPacket(final CdlMODE mode, final CdlLOC pos, final CdlCOMMAND command, final long cbSync, final int retries) {
    return DsPacket(mode, pos, command, cbSync, retries, true);
  }

  /**
   * Adds a sequence of commands that perform a data read (playback) to the queue.
   *
   * The commands added to the queue are: DslPause; DslSetmode mode; DslSetloc pos; com.
   *
   * The commands that can be specified for com are: DslPlay, DslReadN, DslReadS, DslSeekP, or DslSeekL.
   * If com is DslPlay, DslReadN, or DslReadS, execution is performed up to and including the data read
   * (playback). If com is DslSeekP or DslSeekL, the seek is performed and the system enters a pause state.
   *
   * If any command in the sequence generates an error, a retry is performed starting with the first command,
   * up to a maximum of count times (or unlimited times if count=-1). An error is generated if the operation is
   * not successful after count retries.
   *
   * DsSync() can be used to obtain the execution status. When all the commands in the sequence are
   * successful or if an error is generated, the callback function cbsync is triggered.
   *
   * An error is generated if the queue does not have enough space for the command sequence.
   *
   * @param mode Operating mode
   * @param pos DslLOC structure specifying target position
   * @param command Last command to be executed
   * @param cbSync Callback function to be triggered when all commands have been executed
   * @param retries Retry count (0: no retries, -1: unlimited retries)
   *
   * @return The command ID (>0) if the command was added to the queue; 0 if the command failed.
   */
  @Method(0x80033d0cL)
  public static long DsPacket(final CdlMODE mode, final CdlLOC pos, final CdlCOMMAND command, final long cbSync, final int retries, final boolean sendPause) {
    LOGGER.info("Queueing CDROM command %s", command);

    final Tuple<CdlCOMMAND, byte[]>[] packets = new Tuple[5];

    int numberOfCommands = 0;
    if(sendPause) {
      packets[numberOfCommands++] = new Tuple<>(CdlCOMMAND.PAUSE_09, new byte[0]);
    }

    //LAB_80033d84
    packets[numberOfCommands++] = new Tuple<>(CdlCOMMAND.SET_MODE_0E, new byte[] {(byte)mode.toLong(), (byte)0, (byte)0, (byte)0});

    if(pos.pack() < 0) {
      assert false : "CDROM position was < 0 (" + pos + ')';
      return 0;
    }

    packets[numberOfCommands++] = new Tuple<>(CdlCOMMAND.SET_LOC_02, new byte[] {(byte)toBcd(pos.getMinute()), (byte)toBcd(pos.getSecond()), (byte)toBcd(pos.getSector()), (byte)toBcd(pos.getTrack())});

    final long batch;
    switch(command) {
      case SEEK_P_16, SEEK_L_15 -> {
        packets[numberOfCommands++] = new Tuple<>(command, new byte[0]);
        if(cdlPacketIndex_800bf700.get() + numberOfCommands > DSL_MAX_COMMAND) {
          assert false : "CDROM packet queue overflow";
          return 0;
        }
        cdlPacketBatch_800532cc.add(0x1L);
        if(cdlPacketBatch_800532cc.getSigned() == 0) {
          cdlPacketBatch_800532cc.add(0x1L);
        }
        batch = cdlPacketBatch_800532cc.getSigned();

        //LAB_80033e74
        //LAB_80033e80
        for(int n = 0; n < numberOfCommands; n++) {
          final CdlPacket packet = getCurrentPacket();

          if(packet == null) {
            assert false : "CDROM could not get packet address";
            return 0;
          }

          packet.batch.set(batch);
          packet.retries.set(retries);
          packet.command.set(packets[n].a());

          if(packets[n].b().length == 0) {
            packet.clearArgs();
          } else {
            packet.setArgs(packets[n].b()[0], packets[n].b()[1], packets[n].b()[2], packets[n].b()[3]);
          }

          if(n == numberOfCommands - 1) {
            if(cbSync == 0) {
              packet.syncCallback.clear();
            } else {
              packet.syncCallback.set(MEMORY.ref(4, cbSync).cast(BiConsumerRef::new));
            }
          }

          final long oldIMask = setIMask(0);
          cdlPacketIndex_800bf700.addu(0x1L);
          setIMask(oldIMask);
        }
      }
      case PLAY_03, READ_N_06, READ_S_1B -> {
        packets[numberOfCommands++] = new Tuple<>(command, new byte[0]);
        if(cdlPacketIndex_800bf700.get() + numberOfCommands > DSL_MAX_COMMAND) {
          assert false : "CDROM packet queue overflow";
          return 0;
        }
        cdlPacketBatch_800532cc.add(0x1L);
        if(cdlPacketBatch_800532cc.getSigned() == 0) {
          cdlPacketBatch_800532cc.add(0x1L);
        }
        batch = cdlPacketBatch_800532cc.getSigned();

        //LAB_80033f84
        //LAB_80033f90
        for(int n = 0; n < numberOfCommands; n++) {
          final CdlPacket packet = getCurrentPacket();

          if(packet == null) {
            assert false : "CDROM could not get packet address";
            return 0;
          }

          packet.batch.set(batch);
          packet.retries.set(retries);
          packet.command.set(packets[n].a());

          if(packets[n].b().length == 0) {
            packet.clearArgs();
          } else {
            packet.setArgs(packets[n].b()[0], packets[n].b()[1], packets[n].b()[2], packets[n].b()[3]);
          }

          if(n == numberOfCommands - 1) {
            if(cbSync == 0) {
              packet.syncCallback.clear();
            } else {
              packet.syncCallback.set(MEMORY.ref(4, cbSync).cast(BiConsumerRef::new));
            }
          }

          final long oldIMask = setIMask(0);
          cdlPacketIndex_800bf700.addu(0x1L);
          setIMask(oldIMask);
        }
      }
      default -> {
        assert false : "Bad command " + command;
        return 0;
      }
    }

    //LAB_80034028
    if(FUN_8003475c(0) != 0x1L) {
      return batch;
    }

    final CdlPacket packet = CdlPacket_800bf638.get((int)_800bf6fc.get());

    if(packet.batch.get() == batch) {
      sendQueuedPacket();
    }

    return batch;
  }

  @Method(0x800340acL)
  public static SyncCode DsSync(final long batch, final long responses) {
    Response s0;

    if(batch == 0) {
      //LAB_800341dc
      int responseBufferIndex = (int)(cdromResponseBufferIndex_800bf788.get() - 1);
      if(responseBufferIndex < 0) {
        responseBufferIndex = 0x7;
      }

      //LAB_800341fc
      if(cdromResponseBuffer_800bf708.get(responseBufferIndex).getBatch() == 0) {
        s0 = null;
      } else {
        //LAB_80034214
        cdromResponse_800bf5f8.setBatch(cdromResponseBuffer_800bf708.get(responseBufferIndex).getBatch());
        cdromResponse_800bf5f8.setSyncCode(cdromResponseBuffer_800bf708.get(responseBufferIndex).getSyncCode());

        for(int response = 0; response < 8; response++) {
          cdromResponse_800bf5f8.setResponse(response, cdromResponseBuffer_800bf708.get(responseBufferIndex).getResponse(response));
        }

        s0 = cdromResponse_800bf5f8;
      }
    } else {
      FUN_80036e20(0);

      int responseBufferIndex = (int)cdromResponseBufferIndex_800bf788.get();

      //LAB_800340e0
      jump1:
      {
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          if(cdromResponseBuffer_800bf708.get(responseBufferIndex).getBatch() == batch) {
            responseBufferIndex = 0x7;
            break jump1;
          }

          responseBufferIndex++;
          if(responseBufferIndex >= DSL_MAX_RESULTS) {
            responseBufferIndex = 0;
          }

          //LAB_80034108
        }

        if(batch < cdromResponseBuffer_800bf708.get((int)cdromResponseBufferIndex_800bf788.get()).getBatch()) {
          //LAB_80034144
          responseBufferIndex = 0x7;
        } else {
          responseBufferIndex = 0;
        }
      }

      //LAB_80034148
      if(responseBufferIndex != 0x7) {
        return SyncCode.NO_INTERRUPT;
      }

      responseBufferIndex = (int)(cdromResponseBufferIndex_800bf788.get() - 1);
      if(responseBufferIndex < 0) {
        responseBufferIndex = 0x7;
      }

      s0 = null;

      //LAB_80034170
      //LAB_8003417c
      for(int i = 0; i < 8; i++) {
        if(cdromResponseBuffer_800bf708.get(responseBufferIndex).getBatch() == batch) {
          //LAB_80034214
          cdromResponse_800bf5f8.setBatch(cdromResponseBuffer_800bf708.get(responseBufferIndex).getBatch());
          cdromResponse_800bf5f8.setSyncCode(cdromResponseBuffer_800bf708.get(responseBufferIndex).getSyncCode());

          for(int response = 0; response < 8; response++) {
            cdromResponse_800bf5f8.setResponse(response, cdromResponseBuffer_800bf708.get(responseBufferIndex).getResponse(response));
          }

          s0 = cdromResponse_800bf5f8;
          break;
        }

        responseBufferIndex--;
        if(responseBufferIndex < 0) {
          responseBufferIndex = 0x7;
        }

        //LAB_800341a0
      }
    }

    //LAB_80034268
    if(s0 == null) {
      //LAB_80034284
      return SyncCode.NO_RESULT;
    }

    copy8Bytes(responses, s0.getAddress() + 0x5L);
    return s0.getSyncCode();
  }

  @Method(0x8003429cL)
  public static SyncCode FUN_8003429c(final long results) {
    FUN_80036e50(null);

    final long a1;

    if(batch_800bf628.get() == 0x1L) {
      //LAB_800342dc
      a1 = response_800bf62d.getAddress();
    } else if((batch_800bf618.get() ^ 0x1L) == 0) {
      //LAB_800342fc
      a1 = response_800bf61d.getAddress();
    } else {
      return SyncCode.NO_INTERRUPT;
    }

    //LAB_80034314
    batch_800bf628.setu(0);
    copy8Bytes(results, a1);

    //LAB_80034320
    return syncCode_800bf62c.get();
  }

  @Method(0x80034330L)
  public static long getCdlPacketIndex800bf700() {
    return cdlPacketIndex_800bf700.get();
  }

  @Method(0x80034340L)
  public static void FUN_80034340() {
    if(FUN_8003475c(0) != 0x1L) {
      return;
    }

    if(cdlPacketIndex_800bf700.getSigned() <= 0) {
      return;
    }

    if(FUN_8003475c(0) != 0x1L) {
      return;
    }

    final CdlPacket packet = CdlPacket_800bf638.get((int)_800bf6fc.get());

    if(packet.batch.get() != 0) {
      FUN_800346b0(packet.command.get(), packet.getArgsAddress());
    }

    //LAB_800343d0
  }

  @Method(0x800343e4L)
  public static void cdromReadCompleteSubCallback(final SyncCode syncCode, final byte[] responses) {
    if(syncCode == SyncCode.DISK_ERROR && (responses[0] & 0x10L) != 0) {
      FUN_80033304(SyncCode.DISK_ERROR, responses);
    }

    //LAB_80034428
    //LAB_8003442c
    if(syncCode == SyncCode.DATA_END) {
      //LAB_80034468
      //LAB_80034474
      batch_800bf628.set(0x1L);
      syncCode_800bf62c.set(syncCode);
      MEMORY.setBytes(response_800bf62d.getAddress(), responses);

      //LAB_8003444c
    } else if(syncCode == SyncCode.DATA_READY || syncCode == SyncCode.DISK_ERROR) {
      //LAB_80034458
      //LAB_80034474
      batch_800bf618.set(0x1L);
      syncCode_800bf61c.set(syncCode);
      MEMORY.setBytes(response_800bf61d.getAddress(), responses);
    }

    //LAB_80034488
    if(!cdromReadCompleteSubSubCallbackPtr_800c1bb4.isNull()) {
      cdromReadCompleteSubSubCallbackPtr_800c1bb4.deref().run(syncCode, responses);
    }

    //LAB_800344a4
  }

  @Method(0x800344b8L)
  public static void FUN_800344b8(final SyncCode syncCode, final byte[] responses) {
    if(!_800c1bb8.isNull()) {
      _800c1bb8.deref().run(syncCode, responses);
    }
  }

  @Method(0x800344f0L)
  public static void DsInit() {
    initCd();
    setCdAndSpuVolume();

    _800bf798.clear();
    _800bf79c.clear();
    cdromReadCompleteSubCallbackPtr_800bf7a0.clear();

    resetCdromVars();

    syncCode_80053470.set(SyncCode.NO_INTERRUPT);

    FUN_80035b70(0);

    cdromCompleteInterruptCallbackPtr_80052f44.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "cdromCompleteInterruptCallback", SyncCode.class, byte[].class), BiConsumerRef::new));
    cdromDataInterruptCallbackPtr_80052f48.set(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "cdromDataInterruptCallback", SyncCode.class, byte[].class), BiConsumerRef::new));

    SetVsyncInterruptCallback(InterruptType.VBLANK, getMethodAddress(Scus94491BpeSegment_8003.class, "cdromVblankInterruptHandler"));

    usingLibDs_80052f64.setu(1);
    _800532e4.setu(1);
  }

  @Method(0x80034590L)
  private static void resetCdromVars() {
    //LAB_800345b4
    for(int i = 0; i < 4; i++) {
      cdromParams_800532e9.get(i).set((byte)0);
    }

    //LAB_800345d8
    for(int i = 0; i < 8; i++) {
      cdromResponse_800532f4.get(i).set((byte)0);
    }

    _800532e4.setu(0);
    cdromCommand_800532e8.set(CdlCOMMAND.NONE_00);
    ptrCdromParams_800532f0.clear();

    cdromStat_800532fc.setu(0);
    _80053300.setu(0);
    _80053304.setu(0x2L);
    _80053308.setu(0xeL);
    _8005330c.setu(0x15L);
    cdromCommand_80053310.set(CdlCOMMAND.NONE_00);
    oldCdromParam_80053311.set((byte)0);

    cdlLoc_80053312.unpack(0);

    cdromCommand_80053316.set(CdlCOMMAND.NONE_00);
    cdromCommand_80053317.set(CdlCOMMAND.NONE_00);
    isPlaying_80053318.set(false);
    isSeeking_80053319.set(false);
    isReading_8005331a.set(false);
    isMotorOn_8005331b.set(false);
    _8005331c.setu(0);
    syncCode_80053320.set(SyncCode.DATA_READY);
    _80053324.setu(0);
    _80053328.setu(0);
    _8005332c.setu(0);
    _80053330.setu(0);
    _80053334.setu(0);
    cdromCommand_80053335.set(CdlCOMMAND.NONE_00);
  }

  @Method(0x800346b0L)
  public static long FUN_800346b0(final CdlCOMMAND command, final long paramAddress) {
    if(_80053328.getSigned() > 0) {
      return 0;
    }

    if(_800532e4.get() == 0) {
      return 0;
    }

    if(_80053304.get() != 0x1L) {
      return 0;
    }

    _80053300.setu(0x1fL);
    _80053304.setu(0x02L);
    _80053308.setu(0x0bL);

    return sendCdromCommand(command, paramAddress);
  }

  @Method(0x8003472cL)
  public static void set800bf798(final RunnableRef callback) {
    _800bf798.set(callback);
  }

  @Method(0x80034738L)
  public static void set800bf79c(final BiConsumerRef<SyncCode, byte[]> callback) {
    _800bf79c.set(callback);
  }

  @Method(0x80034744L)
  public static void setCdromReadCompleteSubCallbackPtr_800bf7a0(final BiConsumerRef<SyncCode, byte[]> callback) {
    cdromReadCompleteSubCallbackPtr_800bf7a0.set(callback);
  }

  @Method(0x80034750L)
  public static void set800bf7a4(final BiConsumerRef<SyncCode, byte[]> callback) {
    _800bf7a4.set(callback);
  }

  @Method(0x8003475cL)
  public static long FUN_8003475c(final long a0) {
    return _80053304.offset(a0 * 4L).get();
  }

  @Method(0x80034784L)
  public static long getOldCdromParams80053311() {
    return oldCdromParam_80053311.get();
  }

  @Method(0x800347b0L)
  public static CdlCOMMAND getCdromCommand80053317() {
    return cdromCommand_80053317.get();
  }

  @Method(0x800347d0L)
  public static SyncCode getSyncCode80053320_2() {
    return syncCode_80053320.get();
  }

  @Method(0x800347e0L)
  public static void FUN_800347e0(final CdlCOMMAND command, final long paramAddress) {
    if(_80053328.get() > 0) {
      return;
    }

    _80053300.setu(0x20L);
    sendCdromCommand(command, paramAddress);

    //LAB_80034810
  }

  @Method(0x80034820L)
  public static long sendCdromCommand(CdlCOMMAND command, final long paramAddress) {
    DsFlush();

    cdromCommand_800532e8.set(command);
    if(paramAddress == 0) {
      //LAB_80034868
      ptrCdromParams_800532f0.clear();
    } else {
      copy4Bytes(cdromParams_800532e9.getAddress(), paramAddress);
      ptrCdromParams_800532f0.set(cdromParams_800532e9);
    }

    //LAB_8003486c
    //LAB_800348a8
    if(cdromCommandSomethingArray_80053338.get(command.command).get() == 0) {
      //LAB_800348a4
      _80053328.set(30L);
    } else {
      _80053328.set(cdromCommandSomethingArray_80053338.get(command.command).get() * 60L);
    }

    _8005332c.set(0);

    if(command == CdlCOMMAND.STANDBY_07) {
      //LAB_80034914
      if(isMotorOn_8005331b.get()) {
        command = CdlCOMMAND.GET_STAT_01;
        cdromCommand_800532e8.set(command);
      }
    } else if(command == CdlCOMMAND.STOP_08) {
      //LAB_8003492c
      if(!isMotorOn_8005331b.get()) {
        command = CdlCOMMAND.GET_STAT_01;
        cdromCommand_800532e8.set(command);
      }
    } else if(command == CdlCOMMAND.PAUSE_09) {
      //LAB_800348e0
      if(_80053334.get() != 0 && cdromCommand_80053335.get() == CdlCOMMAND.PAUSE_09) {
        command = CdlCOMMAND.GET_STAT_01;
        cdromCommand_800532e8.set(command);
      }
    }

    //LAB_80034940
    if(command == CdlCOMMAND.GET_STAT_01) {
      ptrCdromParams_800532f0.clear();

      //LAB_80034990
      if(cdromCommandSomethingArray_80053338.get(command.command).get() == 0) {
        //LAB_8003498c
        _80053328.set(30L);
      } else {
        _80053328.set(cdromCommandSomethingArray_80053338.get(command.command).get() * 60L);
      }
    }

    //LAB_80034994
    if(DsControl(command, ptrCdromParams_800532f0.getPointer(), 0, true) == 0) {
      //LAB_800349c4
      cdromCommand_80053310.set(command);

      if(command != CdlCOMMAND.GET_STAT_01) {
        cdromCommand_80053335.set(command);
        _80053334.set(0);
      }

      return 0x1L;
    }

    _8005332c.set(0);
    _80053328.set(0);

    //LAB_800349e0
    return 0;
  }

  @Method(0x800349f8L)
  public static void cdromVblankInterruptHandler() {
    if(_80053328.getSigned() > 0) {
      _80053328.subu(0x1L);
      if(_80053328.get() == 0) {
        FUN_80034cc8();
        return;
      }
    }

    //LAB_80034a34
    if(_80053324.getSigned() > 0) {
      _80053324.subu(0x1L);
    }

    //LAB_80034a50
    if(_80053330.getSigned() > 0) {
      _80053330.subu(0x1L);
    }

    //LAB_80034a64
    if(_80053304.get() == 0x2L) {
      if(_80053308.get() == 0xcL) {
        if(_80053438.get() == 0) {
          //LAB_80034ad8
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
          }

          //LAB_80034afc
          _80053438.setu(0x1L);
        } else {
          final Memory.TemporaryReservation res = MEMORY.temp();
          final Value temp = res.get();

          temp.setu(0);
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.SET_MODE_0E, temp.getAddress());
          }

          res.release();

          //LAB_80034acc
          _80053438.setu(0);
        }

        //LAB_80034b08
      } else if(_80053308.get() == 0xdL) {
        _80053438.setu(0);
        if(_80053328.getSigned() <= 0) {
          _80053300.setu(0x20L);
          sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
        }

        //LAB_80034b24
      } else if(_80053308.get() == 0xeL) {
        if(_8005330c.get() == 0x15L) {
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
          }
        } else if(_8005330c.get() == 0x16L) {
          _8005331c.addu(0x1L);
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
          }

          //LAB_80034b64
        } else if(_8005330c.get() == 0x17L) {
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_TN_13, 0);
          }

          //LAB_80034b8c
        } else if(_8005330c.get() == 0x18L) {
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
          }
        }

        //LAB_80034b9c
      } else if(_80053308.get() == 0xfL) {
        if(_80053324.get() == 0) {
          _80053304.setu(0x1L);
          _80053308.setu(0xbL);
        }

        //LAB_80034bbc
      } else if(_80053308.get() == 0x10L) {
        if(isPlaying_80053318.get()) {
          _80053304.setu(0x1L);
          _80053308.setu(0xbL);
        } else {
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
          }
        }

        //LAB_80034bdc
      } else if(_80053308.get() == 0x11L) {
        if(isReading_8005331a.get()) {
          _80053304.setu(0x1L);
          _80053308.setu(0xbL);
        } else {
          if(_80053328.getSigned() <= 0) {
            _80053300.setu(0x20L);
            sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);
          }
        }
      }
    }

    //LAB_80034c24
    if(!_800bf798.isNull() && _800532e4.get() != 0) {
      _800bf798.deref().run();
    }

    //LAB_80034c60
    if(_80053304.get() != 0x1L || isMotorOn_8005331b.get()) {
      //LAB_80034c88
      if(_80053304.get() != 0x3L) {
        return;
      }
    }

    //LAB_80034c90
    if(_80053328.getSigned() > 0) {
      return;
    }

    _80053300.setu(0x21L);
    sendCdromCommand(CdlCOMMAND.GET_STAT_01, 0);

    //LAB_80034cb8
  }

  @Method(0x80034cc8L)
  public static void FUN_80034cc8() {
    DsFlush();

    _8005332c.addu(0x1L);

    if(cdromCommandSomethingArray_80053338.get(cdromCommand_800532e8.get().command).get() == 0) {
      //LAB_80034d1c
      _80053328.setu(30L);
    } else {
      _80053328.setu(cdromCommandSomethingArray_80053338.get(cdromCommand_800532e8.get().command).get() * 60);
    }

    //LAB_80034d20
    DsControl(cdromCommand_800532e8.get(), ptrCdromParams_800532f0.getAddress(), 0, true);
  }

  @Method(0x80034d50L)
  public static void cdromCompleteInterruptCallback(final SyncCode syncCode, final byte[] responses) {
    handleResponseStatus(syncCode, responses);

    _8005332c.set(0);
    _80053328.set(0);
    if(syncCode == SyncCode.COMPLETE) {
      if(cdromCommand_800532e8.get() == CdlCOMMAND.PAUSE_09) {
        if(!isPlaying_80053318.get()) {
          //LAB_80034dbc
          _80053334.set(0x1L);
        }
      }
    }

    final SyncCode newSyncCode;

    //LAB_80034dc0
    if(cdromStat_800532fc.get(0b1_0000L) == 0) { // Shell open
      newSyncCode = syncCode;
    } else {
      newSyncCode = SyncCode.DISK_ERROR;
    }

    //LAB_80034de0
    if(_80053300.get() == 0x1fL) {
      //LAB_80034e00
      FUN_80034e60(newSyncCode, responses);
    }

    if(_80053300.get() == 0x20L) {
      //LAB_80034e14
      FUN_80035044(newSyncCode, responses);
    } else {
      //LAB_80034e24
      FUN_800352cc(newSyncCode, responses);
    }

    //LAB_80034e2c
    if(_80053328.get() == 0) {
      _80053300.set(0x21L);
    }

    //LAB_80034e48
  }

  @Method(0x80034e60L)
  public static void FUN_80034e60(final SyncCode syncCode, final byte[] responses) {
    if(syncCode == SyncCode.COMPLETE) {
      final CdlCOMMAND command = cdromCommand_800532e8.get();

      if(command == CdlCOMMAND.SET_MODE_0E) {
        if(((oldCdromParam_80053311.get() ^ cdromParams_800532e9.get(0).get()) & 0b1000_0000L) == 0) { // CDROM speed
          // Normal speed
          //LAB_80034ec8
          _80053304.setu(0x1L);
          _80053308.setu(0xbL);
        } else {
          // Double speed
          _80053304.setu(0x2L);
          _80053308.setu(0xfL);
          _80053324.setu(0x3L);
        }

        //LAB_80034ed8
        oldCdromParam_80053311.set(cdromParams_800532e9.get(0).get());

        //LAB_80034eec
      } else if(command == CdlCOMMAND.PLAY_03) {
        //LAB_80034f10
        _80053304.setu(0x2L);
        _80053308.setu(0x10L);
        cdromCommand_80053317.set(cdromCommand_800532e8.get());
        _80053330.setu(1200L);

        //LAB_80034efc
      } else if(command == CdlCOMMAND.READ_N_06 || command == CdlCOMMAND.READ_S_1B) {
        //LAB_80034f0c
        //LAB_80034f10
        _80053304.setu(0x2L);
        _80053308.setu(0x11L);
        cdromCommand_80053317.set(cdromCommand_800532e8.get());
        _80053330.setu(1200L);
      } else {
        //LAB_80034f28
        switch(command) {
          case SET_LOC_02 -> cdlLoc_80053312.set(new CdlLOC(MEMORY.ref(1, cdromParams_800532e9.getAddress())));
          case SEEK_P_16, SEEK_L_15 -> cdromCommand_80053316.set(cdromCommand_800532e8.get());
          case READ_N_06, PLAY_03 -> cdromCommand_80053317.set(cdromCommand_800532e8.get());
        }

        _80053304.setu(0x1L);
        _80053308.setu(0xbL);
      }
    } else {
      //LAB_80034fc4
      if(cdromStat_800532fc.get(0b1_0000L) == 0) { // Shell open
        //LAB_80034fe8
        _80053304.setu(0x1L);
        _80053308.setu(0xbL);
      } else {
        _80053304.setu(0x2L);
        _80053308.setu(0xcL);
      }

      //LAB_80034ff4
    }

    //LAB_80034ff8
    if(!_800bf79c.isNull() && _800532e4.get() != 0) {
      _800bf79c.deref().run(syncCode, responses);
    }

    //LAB_80035034
  }

  @Method(0x80035044L)
  public static void FUN_80035044(final SyncCode syncCode, final byte[] responses) {
    if(_80053308.get() == 0xcL) {
      _80053308.set(0xdL);
    }

    //LAB_80035078
    if(syncCode == SyncCode.COMPLETE) {
      final long s0 = _80053308.get();

      if(s0 == 0xdL) {
        if(cdromStat_800532fc.get(0b1_0000L) != 0) { // Shell open
          return;
        }

        _80053308.set(0xeL);
        _80053304.set(0x2L);
        _8005330c.set(0x15L);
        syncCode_80053320.set(SyncCode.DATA_READY);
        return;
      }

      final SyncCode newSyncCode;

      //LAB_800350c8
      final BiConsumerRef<SyncCode, byte[]> callback;
      if(s0 == 0xeL) {
        if(_8005330c.get() == 0x15L) {
          if(cdromStat_800532fc.get(0b1_0000L) != 0) { // Shell open
            return;
          }

          _80053304.set(0x2L);
          _80053308.set(s0);
          _8005330c.set(0x16L);
          _8005331c.set(0);
          return;
        }

        //LAB_80035108
        if(_8005330c.get() == 0x16L) {
          if(cdromStat_800532fc.get(0b10L) != 0) { // Motor on
            //LAB_80035168
            _80053304.set(0x2L);
            _80053308.set(s0);
            _8005330c.set(0x17L);
            return;
          }

          if(_8005331c.get() < 0x12dL) {
            return;
          }

          _80053304.set(0x3L);
          if(_800bf7a4.isNull()) {
            return;
          }

          newSyncCode = SyncCode.DISK_ERROR;
        } else {
          //LAB_80035160
          if(_8005330c.get() == 0x17L) {
            //LAB_80035168
            _80053304.set(0x2L);
            _80053308.set(s0);
            _8005330c.set(0x18L);
            return;
          }

          //LAB_80035178
          if(_8005330c.get() != 0x18L) {
            return;
          }

          if(cdromStat_800532fc.get() != 0x2L) { // Motor on
            return;
          }

          _80053304.set(0x1L);
          _80053308.set(0xbL);
          _8005330c.set(0);
          if(_800bf7a4.isNull()) {
            return;
          }

          newSyncCode = SyncCode.COMPLETE;
        }

        callback = _800bf7a4.deref();
      } else {
        //LAB_800351c4
        if(s0 - 0x10L >= 0x2L) {
          return;
        }

        if(_80053330.get() != 0) {
          return;
        }

        if(cdromStat_800532fc.get(0b10L) != 0) { // Motor on
          return;
        }

        _80053304.set(0x1L);
        _80053308.set(0xbL);
        if(!cdromReadCompleteSubCallbackPtr_800bf7a0.isNull()) {
          if(_800532e4.get() != 0) {
            cdromReadCompleteSubCallbackPtr_800bf7a0.deref().run(SyncCode.DISK_ERROR, responses);
          }
        }

        //LAB_80035234
        if(_800bf79c.isNull() || _800532e4.get() == 0) {
          return;
        }

        newSyncCode = SyncCode.DISK_ERROR;
        callback = _800bf79c.deref();
      }

      //LAB_80035268
      callback.run(newSyncCode, responses);
      return;
    }

    //LAB_80035278
    if(cdromStat_800532fc.get(0b1_0000L) != 0) { // Shell open
      _80053304.set(0x2L);
      _80053308.set(0xcL);
      return;
    }

    //LAB_80035298
    if(_80053308.get() - 0x10L >= 0x2L) {
      return;
    }

    _80053304.set(0x1L);
    _80053308.set(0xbL);

    //LAB_800352bc
  }

  @Method(0x800352ccL)
  public static void FUN_800352cc(final SyncCode syncCode, final byte[] responses) {
    if(syncCode == SyncCode.COMPLETE && cdromCommand_800532e8.get() == CdlCOMMAND.SET_MODE_0E) {
      final long v0 = oldCdromParam_80053311.get() ^ cdromParams_800532e9.get(0).get();

      if((v0 & 0x80L) != 0) { // Double speed
        _80053304.setu(syncCode.ordinal());
        _80053308.setu(0xfL);
        _80053324.setu(0x3L);
      }

      //LAB_80035324
      oldCdromParam_80053311.set(cdromParams_800532e9.get(0).get());

      //LAB_80035328
      //LAB_8003532c
    } else if(syncCode == SyncCode.DISK_ERROR) {
      if(cdromStat_800532fc.get(0b1_0000L) == 0) { // Shell open
        //LAB_8003539c
        _80053304.setu(0x1L);
        _80053308.setu(0xbL);
      } else {
        _80053304.setu(0x2L);
        _80053308.setu(0xcL);

        if(!_800bf79c.isNull() && _800532e4.get() == 0) {
          _800bf79c.deref().run(SyncCode.DISK_ERROR, new byte[DSL_MAX_RESULTS]);
        }
      }
    }

    //LAB_800353a8
  }

  @Method(0x800353b8L)
  public static void cdromDataInterruptCallback(final SyncCode syncCode, final byte[] responses) {
    handleResponseStatus(syncCode, responses);

    if(cdromStat_800532fc.get(0b1_0000L) != 0) { // Shell open
      _80053304.setu(0x2L);
      _80053308.setu(0xcL);
    }

    //LAB_80035400
    if(!cdromReadCompleteSubCallbackPtr_800bf7a0.isNull() && _800532e4.get() != 0) {
      cdromReadCompleteSubCallbackPtr_800bf7a0.deref().run(syncCode, responses);
    }

    //LAB_80035438
  }

  @Method(0x8003544cL)
  private static void handleResponseStatus(final SyncCode syncCode, final byte[] responses) {
    final int statOffset;

    if(syncCode == SyncCode.DISK_ERROR) {
      //LAB_80035498
      statOffset = 0;
    } else {
      statOffset = (int)cdromCommandResponseStatOffsetArray_800533b8.get(cdromCommand_800532e8.get().command).get() - 1;
      if(statOffset < 0) {
        return;
      }
    }

    //LAB_800354a0
    final long stat = responses[statOffset] & 0xff;
    isPlaying_80053318.set((stat & 0b1000_0000) != 0); // Playing
    isSeeking_80053319.set((stat & 0b0100_0000) != 0); // Seeking
    isReading_8005331a.set((stat & 0b0010_0000) != 0); // Reading
    isMotorOn_8005331b.set((stat & 0b0000_0010) != 0); // Spindle motor on
    cdromStat_800532fc.set(stat);

    MEMORY.setBytes(cdromResponse_800532f4.getAddress(), responses);

    //LAB_800354ec
  }

  @Method(0x800354fcL)
  public static void FUN_800354fc() {
    _800532e4.setu(0);

    DsFlush();

    _8005332c.setu(0);
    _80053328.setu(0);

    if(_80053304.get() == 0x2L || _80053308.get() == 0xbL || _80053308.get() == 0x11L || _80053308.get() == 0x10L) {
      //LAB_8003554c
      _80053304.setu(0x1L);
      _80053308.setu(0xbL);
    }

    //LAB_80035564
  }

  @Method(0x80035574L)
  public static void FUN_80035574() {
    _800532e4.setu(0x1L);
  }

  @Method(0x80035584L)
  public static long get800532e4() {
    return _800532e4.get();
  }

  @Method(0x80035594L)
  public static void copy4Bytes(final long dest, final long src) {
    if(src != 0) {
      if(dest != 0) {
        //LAB_800355a4
        for(int offset = 0; offset < 4; offset++) {
          MEMORY.ref(1, dest).offset(offset).setu(MEMORY.ref(1, src).offset(offset));
        }
      }

      return;
    }

    //LAB_800355c8
    if(dest != 0) {
      MEMORY.ref(1, dest).setu(0);
    }

    //LAB_800355d4
  }

  @Method(0x800355dcL)
  public static void copy8Bytes(final long dest, final long src) {
    if(src == 0) {
      //LAB_80035610
      if(dest != 0) {
        MEMORY.ref(1, dest).setu(0);
      }

      return;
    }

    //LAB_8003561c
    if(dest == 0) {
      return;
    }

    //LAB_800355ec
    for(int i = 0; i < 8; i++) {
      MEMORY.ref(1, dest + i).setu(MEMORY.ref(1, src + i));
    }
  }

  @Method(0x80035630L)
  public static void CdMix(final long cdLeftToSpuLeft, final long cdLeftToSpuRight, final long cdRightToSpuRight, final long cdRightToSpuLeft) {
    CdMix_Impl(cdLeftToSpuLeft, cdLeftToSpuRight, cdRightToSpuRight, cdRightToSpuLeft);
  }

  @Method(0x80035ad4L)
  public static void handleCdromDmaTimeout(final long a0) {
    if(!doingCdromDmaTransfer_8005345c.get()) {
      return;
    }

    if(a0 == 0) {
      //LAB_80035b28
      FUN_80036ee0();
    } else {
      FUN_800354fc();
      clearPacketQueue();
      FUN_80036b38();
      FUN_80035574();
      FUN_800347e0(CdlCOMMAND.PAUSE_09, 0);
    }

    //LAB_80035b30
    if(_80053448.get(0x1L) != 0) {
      registerCdDmaCallback(oldCdromDmaCallback_80053450.get());
    }

    //LAB_80035b58
    doingCdromDmaTransfer_8005345c.set(false);
  }

  @Method(0x80035b70L)
  public static void FUN_80035b70(final long a0) {
    if(a0 < 0x2L) {
      _80053448.setu(a0);
    }
  }

  @Method(0x80035b90L)
  public static long FUN_80035b90(final CdlLOC pos, final CdlMODE mode) {
    if(!mode.unknownExtraBit) {
      //LAB_80035c30
      return DsPacket(mode, pos, CdlCOMMAND.READ_S_1B, 0, -0x1);
    }

    sectorIsDataOnly_800bf5f0.set(mode.isDataOnly());

    //LAB_80035bd4
    final long oldDmaCallback = registerCdDmaCallback(getMethodAddress(Scus94491BpeSegment_8003.class, "cdromDmaCallback80031870"));
    final BiConsumerRef<SyncCode, byte[]> oldCdromDmaInterruptSubSubCallback = setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "cdromReadCompleteSubSubCallback_80035c64", SyncCode.class, byte[].class), BiConsumerRef::new));
    final long commandId = DsPacket(mode, pos, CdlCOMMAND.READ_S_1B, 0, -0x1);

    if(commandId == 0) {
      registerCdDmaCallback(oldDmaCallback);
      setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(oldCdromDmaInterruptSubSubCallback);
      assert false : "Failed to queue packet for READ_S " + pos;
      return 0;
    }

    //LAB_80035c4c
    return commandId;
  }

  @Method(0x80035c64L)
  public static void cdromReadCompleteSubSubCallback_80035c64(final SyncCode syncCode, final byte[] responses) {
    FUN_80030d80();
  }

  @Method(0x80035c90L)
  @Nullable
  public static CdlFILE DsSearchFile(final CdlFILE file, final String name) {
    if(syncCode_80053470.get().ordinal() < getSyncCode80053320().ordinal()) {
      if(DsNewMedia() == 0) {
        return null;
      }

      syncCode_80053470.set(getSyncCode80053320());
    }

    //LAB_80035cf8
    if(!name.startsWith("\\")) {
      return null;
    }

    //LAB_80035d18
    final StringBuilder str = new StringBuilder();

    long fp = 0x1L;
    int charIndex = 0;
    long pathSegment;

    //LAB_80035d30
    Outer:
    for(pathSegment = 0; pathSegment < DSL_MAX_LEVEL; pathSegment++) {
      str.delete(0, str.length());

      char chr = name.charAt(charIndex);

      //LAB_80035d44
      while(chr != '\\') {
        if(chr == '\0') {
          break Outer;
        }

        charIndex++;
        str.append(chr);
        chr = charIndex < name.length() ? name.charAt(charIndex) : 0;
      }

      //LAB_80035d6c
      charIndex++;

      fp = DsGetChildDirIndex(fp, str.toString());
      if(fp == -0x1L) {
        //LAB_80035d10
        break;
      }
    }

    //LAB_80035da0
    //LAB_80035da4
    if(pathSegment >= DSL_MAX_LEVEL) {
      LOGGER.error("%s: path level (%d) error", name, pathSegment);
      return null;
    }

    //LAB_80035dd8
    if(str.isEmpty()) {
      LOGGER.error("%s: dir was not found", name);
      return null;
    }

    //LAB_80035e08
    if(DsCacheFile(fp) == 0) {
      LOGGER.error("DsSearchFile: disc error");
      return null;
    }

    //LAB_80035e40
    LOGGER.info("DsSearchFile: searching %s...", str.toString());

    //LAB_80035e6c
    //LAB_80035e80
    for(int fileIndex = 0; fileIndex < DSL_MAX_FILE; fileIndex++) {
      final CdlFILE cdFile = CdlFILE_800bf7a8.get(fileIndex);

      if(cdFile.name.isEmpty()) {
        break;
      }

      if(filenameMatches(cdFile.name.get(), str.toString())) {
        LOGGER.info("%s:  found", str.toString());

        //LAB_80035ed0
        file.set(cdFile);
        return cdFile;
      }

      //LAB_80035f08
    }

    //LAB_80035f20
    //LAB_80035f3c
    LOGGER.info("%s: not found", str.toString());

    //LAB_80035f44
    //LAB_80035f48
    return null;
  }

  /**
   * Compares two 16-byte strings
   *
   * @return True if both strings are identical
   */
  @Method(0x80035f70L)
  public static boolean filenameMatches(final String s1, final String s2) {
    return strncmp(s1, s2, 0xc) == 0;
  }

  @Method(0x80035f90L)
  private static long DsNewMedia() {
    if(!DsRead(0x1L, 0x10L, _800c13a8.getAddress())) {
      LOGGER.error("DS_newmedia: Read error in ds_read(PVD)");
      return 0;
    }

    //LAB_80036004
    if(strncmp(_800c13a9.getString(0x5), "CD001", 0x5) != 0) {
      LOGGER.error("DS_newmedia: Disc format error in ds_read(PVD)");
      return 0;
    }

    //LAB_80036044
    if(!DsRead(0x1L, _800c1434.get(), _800c13a8.getAddress())) {
      LOGGER.error("DS_newmedia: Read error (PT:%08x)", _800c1434.get());
      return 0;
    }

    //LAB_8003609c
    LOGGER.info("DS_newmedia: searching dir...");

    Value address = _800c13a8;
    int directoryId;

    //LAB_800360c0
    //LAB_800360e0
    for(directoryId = 0; directoryId < DSL_MAX_DIR; directoryId++) {
      if(address.getAddress() >= _800c1ba8.getAddress()) {
        break;
      }

      final long filenameLength = address.offset(1, 0x0L).get();

      if(filenameLength == 0) {
        break;
      }

      final CdlDIR dir = CdlDIR_800bfda8.get(directoryId);
      dir.id.set(directoryId + 1);
      dir.parentId.set(address.offset(1, 0x6L).get());
      dir.lba.set(address.offset(2, 0x4L).get() << 16 | address.offset(2, 0x2L).get());
      dir.name.set(address.offset(1, 0x8L).getString((int)filenameLength));

      address = address.offset(filenameLength + (filenameLength & 0x1L) + 0x8L);

      LOGGER.info("\t%08x,%04x,%04x,%s", dir.lba.get(), dir.id.get(), dir.parentId.get(), dir.name.get());

      //LAB_800361bc
    }

    //LAB_800361d4
    if(directoryId < DSL_MAX_DIR) {
      _800bfdd8.offset(directoryId * 44L).set(0);
    }

    //LAB_800361fc
    cdromFilePointer_8005346c.set(0);

    LOGGER.info("DS_newmedia: %d dir entries found", directoryId);

    //LAB_8003622c
    return 0x1L;
  }

  @Method(0x80036254L)
  public static long DsGetChildDirIndex(final long parentId, final String str) {
    //LAB_80036288
    for(int dirIndex = 0; dirIndex < DSL_MAX_DIR; dirIndex++) {
      final CdlDIR dir = CdlDIR_800bfda8.get(dirIndex);

      if(dir.parentId.get() == 0) {
        return -0x1L;
      }

      if(dir.parentId.get() == parentId) {
        if(strcmp(str, dir.name.get()) == 0) {
          return dirIndex + 0x1L;
        }
      }

      //LAB_800362c0
      //LAB_800362c4
    }

    //LAB_800362d4
    //LAB_800362d8
    return -0x1L;
  }

  @Method(0x800362f8L)
  public static long DsCacheFile(final long fp) {
    if(fp == cdromFilePointer_8005346c.get()) {
      return 0x1L;
    }

    if(!DsRead(0x1L, _800bfd84.offset(fp * 44L).get(), _800c13a8.getAddress())) {
      LOGGER.error("DS_cachefile: dir not found");
      return -0x1L;
    }

    //LAB_80036394
    LOGGER.info("DS_cachefile: searching...");

    long s0 = _800c13a8.getAddress();

    //LAB_800363bc
    int fileIndex = 0;

    //LAB_800363dc
    while(fileIndex < DSL_MAX_FILE && s0 < _800c1ba8.getAddress()) {
      if(MEMORY.ref(1, s0).get() == 0) {
        break;
      }

      final CdlFILE file = CdlFILE_800bf7a8.get(fileIndex);

      file.pos.unpack(MEMORY.ref(2, s0).offset(0x4L).get() << 16 | MEMORY.ref(2, s0).offset(0x2L).get());
      file.size.set((int)(MEMORY.ref(2, s0).offset(0xcL).get() << 16 | MEMORY.ref(2, s0).offset(0xaL).get()));

      if(fileIndex == 0) {
        //LAB_80036440
        // First entry: .
        file.name.set(".");
      } else if(fileIndex == 0x1L) {
        //LAB_80036450
        // Second entry: ..
        file.name.set("..");
      } else {
        //LAB_8003646c
        file.name.set(MEMORY.ref(1, s0).offset(0x21L).getString((int)MEMORY.ref(1, s0).offset(0x20L).get()));
      }

      //LAB_80036488
      LOGGER.info("\t(%02d:%02d:%02d) %d %s", file.pos.getMinute(), file.pos.getSecond(), file.pos.getSector(), file.size.get(), file.name.get());

      //LAB_800364e4
      fileIndex++;
      s0 += MEMORY.ref(1, s0).get();
    }

    //LAB_80036518
    cdromFilePointer_8005346c.setu(fp);
    if(fileIndex < DSL_MAX_FILE) {
      CdlFILE_800bf7a8.get(fileIndex).name.set("");
    }

    //LAB_80036540
    LOGGER.info("DS_cachefile: %d files found", fileIndex);

    //LAB_80036568
    //LAB_8003656c
    return 0x1L;
  }

  @Method(0x80036594L)
  public static boolean DsRead(final long dmaTransferCount, final long packedCdPos, final long dmaDestAddress) {
    final CdlLOC cdPos = new CdlLOC().unpack(packedCdPos);
    CDROM.readFromDisk(cdPos, (int)dmaTransferCount, dmaDestAddress);
    FUN_8003429c(0);
    handleCdromDmaTimeout(1);
    return true;

    // Pre-optimisation
//    final CdlLOC cdPos = DsIntToPos(packedCdPos);
//    startCdromDmaTransfer(cdPos, dmaTransferCount, dmaDestAddress, new CdlMODE().doubleSpeed());
//
//    long remainingDmaTransfers;
//
    //LAB_800365cc
//    do {
//      remainingDmaTransfers = FUN_80035a30(0);
//
//      try {
//        Thread.sleep(1);
//      } catch(final InterruptedException ignored) { }
//    } while(remainingDmaTransfers > 0);
//
//    return remainingDmaTransfers == 0;
  }

  @Method(0x800365f0L)
  public static boolean resetDmaTransfer(final long dmaStatusCallback, final long a1) {
    if(_8005349c.get() == 0x1L) {
      //LAB_8003665c
      return false;
    }

    cdromDmaPos_8005347c.setu(-0x1L);
    cdromPreviousDmaPos_80053480.setu(0);
    cdromDmaStatusCallback_80053484.setu(dmaStatusCallback);
    _80053488.setu(0);
    _8005348c.setu(a1);
    _80053490.setNullable(setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_800366ec", SyncCode.class, byte[].class)).cast(BiConsumerRef::new)));
    _80053494.setNullable(set800c1bb8(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_800369c8", SyncCode.class, byte[].class)).cast(BiConsumerRef::new)));
    _8005349c.setu(0x1L);

    //LAB_80036660
    return true;
  }

  @Method(0x80036674L)
  public static void FUN_80036674() {
    if(_8005349c.get() == 0x1L) {
      setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(_80053490.derefNullable());
      set800c1bb8(_80053494.derefNullable());
      DsCommand(CdlCOMMAND.PAUSE_09, 0, 0, -0x1);
    }

    //LAB_800366c4
    _8005349c.setu(0);
  }

  @Method(0x800366d8L)
  public static long set80053498(final long a0) {
    final long oldValue = _80053498.get();
    _80053498.setu(a0);
    return oldValue;
  }

  @Method(0x800366ecL)
  public static void FUN_800366ec(final SyncCode syncCode, final byte[] responses) {
    if(cdromDmaPos_8005347c.getSigned() == -0x1L) {
      cdromDmaPos_8005347c.setu(cdlLoc_80053312.pack());
    }

    final Memory.TemporaryReservation reservation = MEMORY.temp(3);

    //LAB_80036734
    //LAB_80036824
    if(syncCode == SyncCode.DATA_READY) {
      if((getOldCdromParams80053311() & 0b10_0000L) == 0) { // Sector size 800 (data only)
        //LAB_800367c8
        if(cdromDmaStatusCallback_80053484.get() != 0) {
          if(cdromPreviousDmaPos_80053480.get() < cdromDmaPos_8005347c.get()) {
            cdromDmaStatusCallback_80053484.deref(4).cast(BiConsumerRef::new).run(SyncCode.DATA_READY, responses);
            cdromPreviousDmaPos_80053480.setu(cdromDmaPos_8005347c);
          }
        }
      } else {
        final long oldCallback = registerCdDmaCallback(0);
        beginCdromTransfer(reservation.address, 0x3L);
        registerCdDmaCallback(oldCallback);

        final long pos = new CdlLOC(MEMORY.ref(4, reservation.address)).pack();
        if(pos == cdromDmaPos_8005347c.get()) {
          if(cdromDmaStatusCallback_80053484.get() != 0) {
            if(cdromPreviousDmaPos_80053480.get() < pos) {
              cdromDmaStatusCallback_80053484.deref(4).cast(BiConsumerRef::new).run(SyncCode.DATA_READY, responses);
              cdromPreviousDmaPos_80053480.setu(pos);
            }
          }
        } else {
          //LAB_800368e8
          _80053488.setu(0x1L);
        }
      }

      //LAB_80036808
      cdromDmaPos_8005347c.addu(0x1L);
    } else if(syncCode == SyncCode.DATA_END) {
      if(cdromDmaStatusCallback_80053484.get() != 0) {
        cdromDmaStatusCallback_80053484.deref(4).cast(TriConsumerRef::new).run(SyncCode.DATA_END, responses, reservation.address);
      }
    } else {
      //LAB_80036850
      if((responses[0] & 0x10L) != 0) {
        if(_80053498.get() == 0x1L) {
          setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(null);
        } else {
          //LAB_80036884
          setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(_80053490.derefNullable());
          set800c1bb8(_80053494.derefNullable());

          _8005349c.setu(0);
          if(cdromDmaStatusCallback_80053484.get() != 0) {
            cdromDmaStatusCallback_80053484.deref(4).cast(TriConsumerRef::new).run(syncCode, responses, reservation.address);
          }
        }

        reservation.release();
        return;
      }

      //LAB_800368c4
      if(getCdlPacketIndex800bf700() == 0) {
        if((responses[0] & 0b1010_0000L) == 0) { // Not playing or reading
          //LAB_800368e8
          _80053488.setu(0x1L);
        }
      }
    }

    reservation.release();

    //LAB_800368ec
    if(_80053488.get() != 0x1L) {
      return;
    }

    if(_8005348c.getSigned() > 0 || _8005348c.getSigned() == -0x1L) {
      //LAB_8003691c
      FUN_80036a80();

      if(_8005348c.getSigned() > 0) {
        _8005348c.setu(0x1L);
      }
    } else {
      //LAB_8003693c
      if(_8005349c.get() == _80053488.get()) {
        setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(_80053490.derefNullable());
        set800c1bb8(_80053494.derefNullable());

        DsCommand(CdlCOMMAND.PAUSE_09, 0, 0, -0x1);
      }

      //LAB_80036984
      _8005349c.setu(0);
      if(cdromDmaStatusCallback_80053484.get() != 0) {
        cdromDmaStatusCallback_80053484.deref(4).cast(BiConsumerRef::new).run(SyncCode.DISK_ERROR, responses);
      }
    }

    //LAB_800369a4
    _80053488.setu(0);

    //LAB_800369ac
  }

  @Method(0x800369c8L)
  public static void FUN_800369c8(final SyncCode syncCode, final byte[] responses) {
    assert false;
  }

  @Method(0x80036a80L)
  public static void FUN_80036a80() {
    setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(null);
    cdromDmaPos_8005347c.setu(cdlLoc_80053312.pack());
    DsPacket(new CdlMODE(getOldCdromParams80053311()), cdlLoc_80053312, getCdromCommand80053317(), getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_80036af8", SyncCode.class, byte[].class), -0x1);
  }

  @Method(0x80036af8L)
  public static void FUN_80036af8(final SyncCode syncCode, final byte responses) {
    if(syncCode == SyncCode.COMPLETE) {
      setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_800366ec", SyncCode.class, byte[].class)).cast(BiConsumerRef::new));
    }
  }

  @Method(0x80036b38L)
  public static void FUN_80036b38() {
    if(_8005349c.get() == 0x1L) {
      setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(_80053490.derefNullable());
      set800c1bb8(_80053494.derefNullable());
    }

    _8005349c.setu(0);
  }

  @Method(0x80036e20L)
  public static void FUN_80036e20(final long a0) {
    FUN_80031f44(0x1L, a0);
  }

  @Method(0x80036e50L)
  public static void FUN_80036e50(@Nullable final byte[] responses) {
    FUN_800321c4(0x1L, responses);
  }

  @Method(0x80036ec0L)
  public static SyncCode getSyncCode80053320() {
    return getSyncCode80053320_2();
  }

  @Method(0x80036ee0L)
  public static void FUN_80036ee0() {
    FUN_800354fc();
    clearPacketQueue();
    FUN_80036674();
    FUN_80035574();
  }

  @Method(0x80036f20L)
  public static long FUN_80036f20() {
    final long s0 = FUN_8003475c(0);

    if(s0 == 0x1L && getCdlPacketIndex800bf700() > 0) {
      //LAB_80036f5c
      return 0x2L;
    }

    return s0;
  }

  @Method(0x80036f90L)
  public static void beginCdromTransfer(final long dest, final long length) {
    LOGGER.info("Beginning CDROM transfer {dest: %x, length: %x}", dest, length);

    assert dest != 0;
    assert length != 0;

    LOGGER.info("Sending WANT DATA...");

    DMA.cdrom.MADR.setu(dest);
    DMA.cdrom.BCR.setu(1L << 16 | length);
    CDROM_REG0.setu(0); // Index 0
    CDROM_REG3.setu(0b1000_0000L); // Want data
    CDROM_DELAY.setu(0x2_0943L);
    COMMON_DELAY.setu(0x1323L);
    DMA.cdrom.enable();

    LOGGER.info("Waiting until data FIFO not empty...");

    //LAB_8003701c
    while(CDROM_REG0.get(0b100_0000L) == 0) { // Wait until data FIFO not empty
      DebugHelper.sleep(1);
    }

    LOGGER.info("Data queued and ready");

    DMA.cdrom.CHCR.setu(0b1_0001_0000_0000_0000_0000_0000_0000L); // Start/busy = start/enable/busy; start/trigger = manual start

    // Start/trigger is automatically cleared upon BEGIN of transfer

    LOGGER.info("Waiting for transfer to begin...");

    //LAB_80037064;
    while(DMA.cdrom.channelControl.isBusy()) {
      DebugHelper.sleep(1);
    }

    LOGGER.info("Data transfer active");

    //LAB_80037078
    COMMON_DELAY.setu(0x1325L);
  }

  @Method(0x80037330L)
  public static long setCdDebug(final long value) {
    final long old = CD_debug_80052f4c.get();
    CD_debug_80052f4c.setu(value);
    return old;
  }

  @Method(0x800373b0L)
  public static boolean DsControl(final CdlCOMMAND command, final long params, final long responses) {
    final long batch = DsCommand(command, params, 0, 0);

    if(batch == 0) {
      return false;
    }

    //LAB_800373e8
    final DebugHelper.Timer timeout = DebugHelper.timer(5_000_000_000L);
    SyncCode syncCode;
    do {
      timeout.check(() -> LOGGER.error("Timeout"));
      syncCode = DsSync(batch, responses);
      DebugHelper.sleep(1);
    } while(syncCode == SyncCode.NO_INTERRUPT);

    //LAB_80037404
    return syncCode == SyncCode.COMPLETE;
  }

  @Method(0x80037420L)
  @Nullable
  public static BiConsumerRef<SyncCode, byte[]> setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(@Nullable final BiConsumerRef<SyncCode, byte[]> callback) {
    final BiConsumerRef<SyncCode, byte[]> old = cdromReadCompleteSubSubCallbackPtr_800c1bb4.derefNullable();

    if(callback == null){
      cdromReadCompleteSubSubCallbackPtr_800c1bb4.clear();
    } else {
      cdromReadCompleteSubSubCallbackPtr_800c1bb4.set(callback);
    }

    return old;
  }

  @Method(0x80037440L)
  @Nullable
  public static BiConsumerRef<SyncCode, byte[]> set800c1bb8(@Nullable final BiConsumerRef<SyncCode, byte[]> callback) {
    final BiConsumerRef<SyncCode, byte[]> old = _800c1bb8.derefNullable();

    if(callback == null) {
      _800c1bb8.clear();
    } else {
      _800c1bb8.set(callback);
    }

    return old;
  }

  @Method(0x80037460L)
  public static long registerCdDmaCallback(final long callback) {
    return SetDmaInterruptCallback(DmaChannelType.CDROM, callback);
  }

  /**
   * Wait for the next vertical blank, or return the vertical blank counter value.
   *
   * Waits for vertical blank using the method specified by mode, as defined below.
   *
   * <table>
   *   <caption>Possible Values</caption>
   *   <tr>
   *     <th>Mode</th>
   *     <th>Operation</th>
   *   </tr>
   *   <tr>
   *     <td>0</td>
   *     <td>Blocks until vertical sync is generated</td>
   *   </tr>
   *   <tr>
   *     <td>1</td>
   *     <td>Returns time elapsed from the point VSync() processing is last completed when mode=1 or n in horizontal sync units</td>
   *   </tr>
   *   <tr>
   *     <td>n (n>1)</td>
   *     <td>Blocks from the point VSync() processing is last completed when mode=1 or n until n number of vertical syncs are generated.</td>
   *   </tr>
   *   <tr>
   *     <td>-n (n>0)</td>
   *     <td>Returns absolute time after program boot in vertical sync interval units.</td>
   *   </tr>
   * </table>
   *
   * Vsync() may generate a timeout if long blocking periods are specified. To prevent deadlocks, rather than
   * using Vsync() to block for an especially long time (say more than 4 vertical blank periods), have your
   * program poll VSync(-1) in a loop instead.
   *
   * @return If mode>=0, returns time elapsed from the point that Vsync() processing is last completed when
   *         mode=1 or n (horizontal blanking units). If mode<0, returns time elapsed after program boot (vertical
   *         blanking units)
   */
  @Method(0x80037490L)
  public static int VSync(final int mode) {
    long v0;
    long v1;

    //LAB_800374b4
    do {
      v1 = TMR_HRETRACE_VAL.get();
      v0 = TMR_HRETRACE_VAL.get();
    } while(v1 != v0);

    final long s1 = v1 - _800534fc.get() & 0xffffL;

    if(mode < 0) {
      return (int)Vcount.get();
    }

    //LAB_80037500
    if(mode != 0x1L) {
      //LAB_8003752c
      //LAB_80037534
      //LAB_80037540
      if(mode > 0) {
        checkVsyncTimeout(_80053500.get() - 1 + mode, mode - 0x1L);
      } else {
        checkVsyncTimeout(_80053500.get(), 0);
      }

      final long s0 = GPU_REG1.get();
      checkVsyncTimeout(Vcount.get() + 1, 0x1L);

      if((s0 & 0x40L) != 0) {
        if((s0 ^ GPU_REG1.get()) >= 0) {
          //LAB_8003759c
          do {
            if(Hardware.isGpuThread()) {
              GPU.tick();
            }

            DebugHelper.sleep(1);
          } while(((s0 ^ GPU_REG1.get()) & 0x80000000L) == 0);
        }
      }

      //LAB_800375b4
      _80053500.setu(Vcount);

      //LAB_800375cc
      do {
        _800534fc.setu(TMR_HRETRACE_VAL);
      } while(_800534fc.get() != TMR_HRETRACE_VAL.get());
    }

    //LAB_800375f0
    //LAB_800375f4
    return (int)s1;
  }

  @Method(0x80037608L)
  public static void checkVsyncTimeout(final long a0, final long a1) {
    if(Vcount.get() >= a0) {
      return;
    }

    if(Hardware.isGpuThread()) {
      GPU.tick();
    }

    //LAB_80037630
    long a2 = a1 << 0xfL;

    do {
      a2--;
      if(a2 == -0x1L) {
        LOGGER.error("VSync: timeout");
        ChangeClearPAD(false);
        ChangeClearRCnt(0x3, false);
        break;
      }

      DebugHelper.sleep(1);

      if(Hardware.isGpuThread()) {
        GPU.tick();
      }

      //LAB_80037678
    } while(Vcount.get() < a0);

    //LAB_80037690
  }

  @Method(0x800376a0L)
  public static boolean ChangeClearRCnt(final int t, final boolean flag) {
    return (boolean)functionVectorC_000000c0.run(0xaL, new Object[] {t, flag});
  }

  @Method(0x800376b0L)
  public static long ResetCallback() {
    return (long)_800545ec.deref(4).offset(0xcL).deref(4).cast(SupplierRef::new).run();
  }

  @Method(0x800376e0L)
  public static long InterruptCallback(final InterruptType interrupt, final long callback) {
    return (long)_800545ec.deref(4).offset(0x8L).deref(4).call(interrupt, callback);
  }

  @Method(0x80037710L)
  public static long SetDmaInterruptCallback(final DmaChannelType channel, final long callback) {
    return (long)_800545ec.deref(4).offset(0x4L).deref(4).call(channel, callback);
  }

  @Method(0x80037740L)
  public static void SetTmr0InterruptCallback(final long func) {
    _800545ec.deref(4).offset(0x14L).deref(4).call(InterruptType.TMR0, func);
  }

  @Method(0x80037774L)
  public static void SetVsyncInterruptCallback(final InterruptType interrupt, final long callback) {
    _800545ec.deref(4).offset(0x14L).deref(4).cast(BiConsumerRef::new).run(interrupt, callback);
  }

  @Method(0x80037804L)
  public static boolean CheckCallback() {
    synchronized(inExceptionHandler_80053566) {
      return inExceptionHandler_80053566.get();
    }
  }

  @Method(0x8003782cL)
  public static long setIMask(final long mask) {
    final long old = I_MASK.get();
    I_MASK.setu(mask);
    return old;
  }

  @Method(0x80037844L)
  public static long ResetCallback_Impl() {
    if(interruptHandlersInitialized_80053564.get()) {
      return 0;
    }

    I_MASK.setu(0);
    I_STAT.setu(0);
    DMA_DPCR.setu(0x3333_3333L);
    zeroMemory_80037d4c(interruptHandlersInitialized_80053564.getAddress(), 1050);
    setjmp(jmp_buf_8005359c, MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "handleException"), RunnableRef::new));
    _800535a0.setu(_8005457c.getAddress());
    SetCustomExitFromException(jmp_buf_8005359c);
    interruptHandlersInitialized_80053564.set(true);
    _800545ec.deref(4).offset(0x14L).setu(startInterruptVsync());
    _800545ec.deref(4).offset(0x4L).setu(startInterruptDma());
    _96_remove();
    ExitCriticalSection();

    //LAB_8003790c
    return interruptHandlersInitialized_80053564.getAddress();
  }

  @Method(0x8003791cL)
  public static void handleException() {
    if(!interruptHandlersInitialized_80053564.get()) {
      LOGGER.error("unexpected interrupt(%04x)", I_STAT.get());
      ReturnFromException();
      return;
    }

    //LAB_80037974
    synchronized(inExceptionHandler_80053566) {
      inExceptionHandler_80053566.set(true);
    }

    long s0 = I_MASK.get(_80053594.get(I_STAT));

    //LAB_800379b0
    while(s0 != 0) {
      long s1 = 0;
      long s2 = 0;

      //LAB_800379bc
      while(s0 != 0 && s1 < 0xbL) {
        if((s0 & 0x1L) != 0) {
          I_STAT.setu(~(0x1L << s1));
          final long v0 = interruptCallbacks_80053568.offset(s2).get();
          if(v0 != 0) {
            MEMORY.ref(4, v0).call();
          }
        }

        //LAB_800379f8
        s2 += 0x4L;
        s0 >>>= 0x1L;
        s1++;
      }

      //LAB_80037a0c
      s0 = I_MASK.get(_80053594.get(I_STAT));
    }

    //LAB_80037a3c
    if(I_STAT.get(I_MASK) == 0) {
      //LAB_80037ab8
      _800545fc.setu(0);
    } else {
      _800545fc.addu(0x1L);
      if(_800545fc.get() > 0x800L) {
        LOGGER.error("intr timeout(%04x:%04x)", I_STAT.get(), I_MASK.get());
        _800545fc.setu(0);
        I_STAT.setu(0);
      }
    }

    //LAB_80037ac0
    synchronized(inExceptionHandler_80053566) {
      inExceptionHandler_80053566.set(false);
    }

    ReturnFromException();
  }

  @Method(0x80037aecL)
  public static long InterruptCallback_Impl(final InterruptType interrupt, final long callback) {
    final long oldCallback = interruptCallbacks_80053568.offset(interrupt.ordinal() * 4L).get();
    if(callback == oldCallback || !interruptHandlersInitialized_80053564.get()) {
      return oldCallback;
    }

    long oldIMask = I_MASK.get();
    I_MASK.setu(0);

    if(callback == 0) {
      //LAB_80037b7c
      interruptCallbacks_80053568.offset(interrupt.ordinal() * 4L).setu(0);

      final long v0 = ~interrupt.getBit();
      oldIMask &= v0;
      _80053594.and(v0);
    } else {
      interruptCallbacks_80053568.offset(interrupt.ordinal() * 4L).setu(callback);

      final long v1 = interrupt.getBit();
      oldIMask |= v1;
      _80053594.oru(v1);
    }

    //LAB_80037b9c
    if(interrupt == InterruptType.VBLANK) {
      ChangeClearPAD(callback == 0);
      ChangeClearRCnt(3, callback == 0);
    }

    //LAB_80037bc0
    if(interrupt == InterruptType.TMR0) {
      ChangeClearRCnt(0, callback == 0);
    }

    //LAB_80037bd8
    if(interrupt == InterruptType.TMR1) {
      ChangeClearRCnt(1, callback == 0);
    }

    //LAB_80037bf0
    if(interrupt == InterruptType.TMR2) {
      ChangeClearRCnt(2, callback == 0);
    }

    //LAB_80037c00
    I_MASK.setu(oldIMask);

    //LAB_80037c10
    //LAB_80037c14
    return oldCallback;
  }

  @Method(0x80037d4cL)
  public static void zeroMemory_80037d4c(final long address, final int words) {
    for(long i = address; i < address + words * 4L; i += 4L) {
      MEMORY.set(i, 4, 0);
    }
  }

  @Method(0x80037d78L)
  public static void _96_remove() {
    functionVectorA_000000a0.run(0x72L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x80037d90L)
  public static void ReturnFromException() {
    functionVectorB_000000b0.run(0x17L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x80037db0L)
  public static void SetCustomExitFromException(final jmp_buf buffer) {
    functionVectorB_000000b0.run(0x19L, new Object[] {buffer});
  }

  @Method(0x80037dc0L)
  public static void setjmp(final jmp_buf buffer, final RunnableRef callback) {
    functionVectorA_000000a0.run(0x13L, new Object[] {buffer, callback});
  }

  @Method(0x80037dd0L)
  public static long startInterruptVsync() {
    TMR_HRETRACE_MODE.setu(0x100L);
    Vcount.setu(0);

    zeroMemory_80037d4c(vsyncCallbacks_8005460c.getAddress(), 8);
    InterruptCallback(InterruptType.VBLANK, getMethodAddress(Scus94491BpeSegment_8003.class, "executeVsyncCallbacks"));

    return getMethodAddress(Scus94491BpeSegment_8003.class, "SetInterruptCallback_Impl", InterruptType.class, long.class);
  }

  @Method(0x80037e28L)
  public static void executeVsyncCallbacks() {
    Vcount.addu(0x1L);

    for(int i = 0; i < 8; i++) {
      final Pointer<RunnableRef> ptr = vsyncCallbacks_8005460c.get(i);

      if(!ptr.isNull()) {
        ptr.deref().run();
      }
    }
  }

  @Method(0x80037e94L)
  public static void SetInterruptCallback_Impl(final InterruptType interrupt, final long callback) {
    vsyncCallbacks_8005460c.get(interrupt.ordinal()).set(MEMORY.ref(4, callback, RunnableRef::new));
  }

  @Method(0x80037ef0L)
  public static long startInterruptDma() {
    DMA_DICR.setu(0);

    zeroMemory_80038168(dmaCallbacks_80054640.getAddress(), 8);
    InterruptCallback(InterruptType.DMA, getMethodAddress(Scus94491BpeSegment_8003.class, "executeDmaInterruptCallbacks"));

    return getMethodAddress(Scus94491BpeSegment_8003.class, "SetDmaInterruptCallback_Impl", DmaChannelType.class, long.class);
  }

  @Method(0x80037f3cL)
  public static void executeDmaInterruptCallbacks() {
    long s1 = DMA_DICR.get() >> 24 & 0x7fL;

    //LAB_80037f90
    while(s1 != 0) {
      //LAB_80037f9c
      for(int i = 0; i < 7 && s1 != 0; i++) {
        if((s1 & 0x1L) != 0) {
          DMA_DICR.and(0xff_ffffL | 0x1L << i + 24);
          final Pointer<RunnableRef> callback = dmaCallbacks_80054640.get(i);
          if(!callback.isNull()) {
            callback.deref().run();
          }
        }

        //LAB_80037fe4
        s1 >>>= 1;
      }

      //LAB_80037ff4
      s1 = DMA_DICR.get() >>> 24 & 0x7fL;
    }

    //LAB_80038018
    if(DMA_DICR.get(0xff00_0000L) == 0x8000_0000L || DMA_DICR.get(0x8000L) != 0) {
      //LAB_80038050
      LOGGER.error("DMA bus error: code=%08x", DMA_DICR.get());

      //LAB_80038064
      for(int i = 0; i < 7; i++) {
        LOGGER.error("MADR[%d]=%08x", i, DMA.mdecIn.MADR.offset(i << 0x4L).get());
      }
    }

    //LAB_80038098
  }

  @Method(0x800380bcL)
  public static long SetDmaInterruptCallback_Impl(final DmaChannelType channel, final long callback) {
    final Pointer<RunnableRef> ptr = dmaCallbacks_80054640.get(channel.ordinal());
    final long previous = ptr.isNull() ? 0 : ptr.deref().getAddress();

    if(callback != previous) {
      if(callback == 0) {
        //LAB_80038124
        ptr.clear();
        DMA_DICR.setu(0x80_0000L | DMA_DICR.get(0xff_ffffL) & ~(0x1L << channel.ordinal() + 16));
      } else {
        ptr.set(MEMORY.ref(4, callback, RunnableRef::new));
        DMA_DICR.setu(0x80_0000L | DMA_DICR.get(0xff_ffffL) | 0x1L << channel.ordinal() + 16);
      }
    }

    //LAB_80038160
    return previous;
  }

  @Method(0x80038168L)
  public static void zeroMemory_80038168(final long address, final int words) {
    for(long i = address; i < address + words * 4L; i += 4L) {
      MEMORY.set(i, 4, 0);
    }
  }

  /**
   * Initialize drawing engine.
   *
   * Resets the graphic system according to mode
   *
   * 0 - Complete reset. The drawing environment and display environment are initialized.
   * 1 - Cancels the current drawing and flushes the command buffer.
   * 3 - Initializes the drawing engine while preserving the current display environment
   * 5 - ?
   *
   * Setting bit 4 seems to allow disabling textures (for debugging?)
   *
   * (i.e. the screen is not cleared or the screen mode changed).
   */
  @Method(0x80038190L)
  public static long ResetGraph(final long mode) {
    final long v1 = mode & 0b111L;
    //LAB_800381cc
    if(v1 != 0 && v1 != 0x3L && v1 != 0x5L) {
      //LAB_800382a0
      LOGGER.trace("ResetGraph(%d)...", mode);

      //LAB_800382d4
      //LAB_800382f0
      return (long)_80054674.deref(4).offset(0x34L).cast(FunctionRef::new).run(mode);
    }

    //LAB_800381dc
    LOGGER.trace("ResetGraph:jtb=%08x,env=%08x", _80054674.getAddress(), _800546bc.getAddress());

    //LAB_800381f8
    fillMemory(_800546bc.getAddress(), (byte)0, 0x80);
    ResetCallback();

    // No idea what this is supposed to do. It's sending GP0 command 0 (which is supposedly NOP)
    // with the 3 LSBs of a pointer to the system version string. I've verified this is what
    // the code is supposed to be doing. Maybe it's just to force a GPU sync...?
    GPU_cw((int)_800546b4.get(0xff_ffffL));

    _800546bc.setu(FUN_8003a798(mode));
    _800546bd.setu(0x1L);
    _800546c0.setu(array_8005473c.offset(_800546bc.get() * 4));
    _800546c2.setu(array_80054748.offset(_800546bc.get() * 4));

    fillMemory(DRAWENV_800546cc.getAddress(), (byte)0xff, 92);
    fillMemory(DISPENV_80054728.getAddress(), (byte)0xff, 20);

    return _800546bc.get();
  }

  /**
   * Set debugging level.
   *
   * Sets a debugging level for the graphics system. level can be one of the following:
   * Table 7-6
   * Level Operation
   * 0 No checks are performed. (Highest speed
   * mode)
   * 1 Checks coordinating registered and drawn
   * primitives.
   * 2 Registered and drawn primitives are
   * dumped.
   *
   * @param level Debugging level
   *
   * @return The previously set debug level.
   */
  @Method(0x80038304L)
  public static long SetGraphDebug(final int level) {
    final long old = gpu_debug.get();
    gpu_debug.setu(level);

    if(level != 0) {
      LOGGER.info("SetGraphDebug:level:%d,type:%d,reverse:%d", gpu_debug.get(), _800546bc.get(), gpuReverseFlag_800546bf.get());
    }

    return old;
  }

  /**
   * Set and cancel display mask.
   *
   * @param mask Display mask
   *
   * Puts display mask into the status specified by mask. mask =0: not displayed on screen; mask = 1;
   * displayed on screen.
   */
  @Method(0x80038474L)
  public static void SetDispMask(final int mask) {
    LOGGER.trace("SetDispMask(%08x)...", mask);

    if(mask == 0) {
      fillMemory(DISPENV_80054728.getAddress(), (byte)0xff, 0x14);
    }

    if(mask == 0) {
      sendDisplayCommand(0x300_0001L); // Disable screen
    } else {
      sendDisplayCommand(0x300_0000L); // Enable screen
    }
  }

  /**
   * <p>Waits for drawing to terminate.</p>
   *
   * <p>If DrawSync(0) is used, and execution of the primitive list takes an exceptionally long time (approximately
   * longer than 8 Vsync) to complete, a timeout is generated and the GPU is reset. Reasons why this might
   * occur include an exceptionally long primitive list, or one that renders exceptionally large numbers of pixels.
   * Another possibility is that the primitive list has been corrupted in some way. To avoid this, the application
   * can use a loop such as:<br>
   * <tt>while(DrawSync(1));</tt>
   * </p>
   *
   * @param mode <ol start="0">
   *               <li>Wait for termination of all non-blocking functions registered in the queue</li>
   *               <li>Return the number of positions in the current queue</li>
   *             </ol>
   *
   * @return Number of positions in the execution queue.
   */
  @Method(0x8003850cL)
  public static int DrawSync(final int mode) {
    if(gpu_debug.get() >= 0x2L) {
      LOGGER.info("DrawSync(%d)...", mode);
    }

    return (int)_800546b4.deref(4).offset(0x3cL).deref(4).call(mode);
  }

  @Method(0x80038574L)
  private static void validateRect(final String text, final RECT rect) {
    if(rect.w.get() > _800546c0.get() || rect.x.get() + rect.w.get() > _800546c0.get() || rect.y.get() > _800546c2.get() || rect.y.get() + rect.h.get() > _800546c2.get() || rect.w.get() < 0 || rect.x.get() < 0 || rect.y.get() < 0 || rect.h.get() < 0) {
      LOGGER.warn("%s:bad RECT", text);
      LOGGER.warn("(%d,%d)-(%d,%d)", rect.x.get(), rect.y.get(), rect.w.get(), rect.h.get());
    }
  }

  @Method(0x80038690L)
  public static int ClearImage(final RECT rect, final byte r, final byte g, final byte b) {
    validateRect("ClearImage", rect);

//    return (int)_800546b4.deref(4).offset(0x8L).deref(4).call(
//      _800546b4.deref(4).offset(0xcL).deref(4).cast(BiFunctionRef::new),
//      rect,
//      0x8L,
//      (b & 0xffL) << 16 | (g & 0xffL) << 8 | r & 0xffL
//    );

    return (int)ClearImage_Impl(rect, (b & 0xffL) << 16 | (g & 0xffL) << 8 | r & 0xffL);
  }

  @Method(0x800387b8L)
  public static void LoadImage(final RECT rect, final long address) {
    validateRect("LoadImage",rect);

    _800546b4.deref(4).offset(0x8L).deref(4).call(
      _800546b4.deref(4).offset(0x20L).deref(4).cast(BiFunctionRef::new),
      rect,
      0x8L,
      address
    );
  }

  @Method(0x80038818L)
  public static long StoreImage(final RECT rect, final long address) {
    validateRect("StoreImage", rect);

    recordGpuTime();

    rect.w.set(MathHelper.clamp(rect.w.get(), (short)0, (short)_800546c0.get()));
    rect.h.set(MathHelper.clamp(rect.h.get(), (short)0, (short)_800546c2.get()));

    //LAB_80039fac
    //LAB_80039fb0
    final long dataSize = rect.w.get() * rect.h.get() + 0x1L;
    if(dataSize / 2 <= 0) {
      return -0x1L;
    }

    //LAB_80039fe4
    //LAB_8003a014
    while(GPU_REG1.get(0x400_0000L) == 0) {
      if(Hardware.isGpuThread()) {
        GPU.tick();
      }

      if(checkForGpuTimeout() != 0) {
        return -0x1L;
      }

      DebugHelper.sleep(1);
    }

    GPU.command01ClearCache();
    GPU.commandC0CopyRectFromVramToCpu(rect, address);

    // Pre-optimisation
    //LAB_8003a044
//    GPU_REG1.setu(0x400_0000L);
//    GPU_REG0.setu(0x100_0000L);
//    GPU_REG0.setu(0xc000_0000L);
//    GPU_REG0.setu((rect.getY() & 0xffffL) << 16 | rect.getX() & 0xffffL);
//    GPU_REG0.setu((rect.getH() & 0xffffL) << 16 | rect.getW() & 0xffffL);

    //LAB_8003a0c0
//    while(GPU_REG1.get(0x800_0000L) == 0) {
//      if(checkForGpuTimeout() != 0) {
//        return -0x1L;
//      }
//
//      DebugHelper.sleep(1);
//    }

    //LAB_8003a0f0
    //LAB_8003a100
//    long numberOfRegTransfers = dataSize / 32;
//    final long numberOfDmaTransfers = numberOfRegTransfers;
//    numberOfRegTransfers = dataSize / 2 - numberOfRegTransfers * 16;
//
//    long s2 = 0;
//    while(numberOfRegTransfers != 0) {
//      MEMORY.ref(4, address).offset(s2).setu(GPU_REG0);
//      s2 += 0x4L;
//      numberOfRegTransfers--;
//    }

    //LAB_8003a120
//    if(numberOfDmaTransfers != 0) {
//      GPU_REG1.setu(0x400_0003L);
//      dma.gpu.MADR.setu(s2);
//      dma.gpu.BCR.setu(numberOfDmaTransfers << 16 | 16);
//      dma.gpu.CHCR.setu(0x100_0200L);
//    }

    //LAB_8003a16c
    //LAB_8003a170
    return 0;
  }

  @Method(0x80038878L)
  public static long MoveImage(final RECT rect, final int x, final int y) {
    validateRect("MoveImage", rect);

    if(rect.w.get() == 0 || rect.h.get() == 0) {
      return -0x1L;
    }

    //LAB_800388d0
    final long v1 = _8005475c.getAddress();
    MEMORY.ref(4, v1).offset(0x0L).setu((rect.y.get() & 0xffffL) << 16 | rect.x.get() & 0xffffL);
    MEMORY.ref(4, v1).offset(0x4L).setu((y & 0xffffL) << 16 | x & 0xffffL);
    MEMORY.ref(4, v1).offset(0x8L).setu((rect.h.get() & 0xffffL) << 16 | rect.w.get() & 0xffffL);

    //LAB_80038918
    return (int)_800546b4.deref(4).offset(0x8L).deref(4).call(_800546b4.deref(4).offset(0x18L).deref(4).cast(BiFunctionRef::new), _8005475c.offset(-0x8L).cast(UnsignedIntRef::new), 0x14L, 0L);
  }

  /**
   * Initialize an array to a linked list for use as an ordering table.
   *
   * @param ot Head pointer of OT
   * @param count Number of entries in OT
   *
   * Walks the array specified by ot and sets each element to be a pointer to the previous element, except the
   * first, which is set to a pointer to a special terminator value which the PlayStation uses to recognize the end
   * of a primitive list. count specifies how many entries are present in the array.
   *
   * To execute the OT initialized by ClearOTagR(), execute DrawOTag(ot+n-1).
   */
  @Method(0x800389f8L)
  public static UnboundedArrayRef<GsOT_TAG> ClearOTagR(final UnboundedArrayRef<GsOT_TAG> ot, final int count) {
    LOGGER.trace("ClearOTagR(%08x,%d)...", ot, count);

    for(int i = 1; i < count; i++) {
      ot.get(i).set(ot.get(i - 1).getAddress() & 0xff_ffffL);
    }

    _8005477c.setu(0x405_4768L);
    ot.get(0).num.set(0);
    ot.get(0).p.set(0x5_477cL);
    return ot;

    // Pre-optimisation:
    //LAB_80038a40
//    DMA_DPCR.oru(0b1000_0000_0000_0000_0000_0000_0000L); // Master enable OTC DMA
//    DMA_OTC_MADR.setu(ot + (count - 1) * 4);
//    DMA_OTC_CHCR.setu(0b1_0001_0000_0000_0000_0000_0000_0010L); // Step: backwards, Start/Busy: Start/Enable/Busy, Start/Trigger: Manual Start
//    DMA_OTC_BCR.setu(count);
//
//    recordGpuTime();
//
//    long ret = -0x1L;
//
//    do {
//      if(DMA_OTC_CHCR.get(0b1_0000_0000_0000_0000_0000_0000L) == 0) { // Start/Busy
//        ret = count;
//        break;
//      }
//
//      try {
//        Thread.sleep(1);
//      } catch(final InterruptedException ignored) { }
//    } while(checkForGpuTimeout() == 0);
//
//    _8005477c.setu(0x405_4768L);
//    MEMORY.ref(4, ot).setu(0x5_477cL);
//    return ot;
  }

  /**
   * Executes the GPU primitives in the linked list ot.
   *
   * DrawOTag() is non-blocking. To detect when execution of the primitive list is complete, use DrawSync() or
   * install a callback routine with DrawSyncCallback().
   *
   * @param address Pointer to a linked list of GPU primitives
   */
  @Method(0x80038b00L)
  public static void DrawOTag(final MemoryRef address) {
    if(gpu_debug.get() > 0) {
      LOGGER.info("DrawOTag(%08x)...", address.getAddress());
    }

    uploadLinkedListToGpu(address);

    // Pre-optimisation:
//    FUN_8003a288(LibGpu::uploadLinkedList, address, 0, 0);
  }

  /**
   * Set the drawing environment.
   *
   * @param env Pointer to drawing environment start address
   *
   * The basic drawing parameters (such as the drawing offset and the drawing clip area) are set according to
   * the values specified in env.
   *
   * The drawing environment is effective until the next time PutDrawEnv() is executed, or until the DR_ENV
   * primitive is executed.
   *
   * @return A pointer to the drawing environment set. On failure, returns 0.
   */
  @Method(0x80038b70L)
  public static DRAWENV PutDrawEnv(final DRAWENV env) {
    if(gpu_debug.get() > 1) {
      LOGGER.info("PutDrawEnv(%08x)...", env.getAddress());
    }

    SetDrawEnv(env.dr_env, env);
    env.dr_env.tag.set((int)(env.dr_env.tag.get() | 0xff_ffff));
//    _800546b4.deref(4).offset(0x8L).deref(4).call(
//      _800546b4.deref(4).offset(0x18L).deref(4).cast(BiFunctionRef::new),
//      env.dr_env,
//      0x40L,
//      0L
//    );
    uploadLinkedListToGpu(env.dr_env);
    DRAWENV_800546cc.set(env);
    return env;
  }

  /**
   * Sets a display environment according to information specified by env.
   *
   * @param env Pointer to display environment start address
   *
   * @return A pointer to the display environment set; on failure, returns 0.
   */
  @Method(0x80038d3cL)
  public static DISPENV PutDispEnv(final DISPENV env) {
    if(gpu_debug.get() > 1) {
      LOGGER.info("PutDispEnv(%s)...", env);
    }

    //LAB_80038d8c
    // GP1(05h) - set start of display area (in VRAM)
    sendDisplayCommand(0x500_0000L | (env.disp.y.get() & 0x3ffL) << 10 | env.disp.x.get() & 0x3ffL);

    long s0 = 0;
    long v1;

    if(DISPENV_80054728.isinter.get() != env.isinter.get() || DISPENV_80054728.disp.x.get() != env.disp.x.get() || DISPENV_80054728.disp.y.get() != env.disp.y.get() || DISPENV_80054728.disp.w.get() != env.disp.w.get() || DISPENV_80054728.disp.h.get() != env.disp.h.get()) {
      //LAB_80038e34
      env.pad0.set((byte)GsGetWorkBase());

      if(env.pad0.get() == 0x1L) {
        s0 |= 0b1000L; // PAL
      }

      //LAB_80038e54
      if(env.isrgb24.get() != 0) {
        s0 |= 0b1_0000L; // 24-bit colour
      }

      //LAB_80038e68
      if(env.isinter.get() != 0) {
        s0 |= 0b10_0000L; // Interlaced
      }

      //LAB_80038e7c
      if(gpuReverseFlag_800546bf.get() != 0) {
        s0 |= 0b1000_0000L; // "Reverse flag" - distorted - unknown use
      }

      //LAB_80038e94
      v1 = env.disp.w.get();

      if(v1 > 280L) {
        //LAB_80038eb8
        if(v1 <= 352L) {
          s0 |= 0b1L; // Horizontal res: 320
        } else if(v1 <= 400L) {
          s0 |= 0b100_0000L; // Horizontal res 2: 368
        } else if(v1 <= 560L) {
          s0 |= 0b10L; // Horizontal res: 512
        } else {
          //LAB_80038ed8
          s0 |= 0b11L; // Horizontal res: 640
        }
      }

      //LAB_80038edc
      //LAB_80038ef0
      if(env.pad0.get() != 0 && env.disp.h.get() > 288L) {
        s0 |= 0b10_0100L; // Vertical res: 480, vertical interlace: on
      } else if(env.pad0.get() == 0 && env.disp.h.get() > 256L) {
        s0 |= 0b10_0100L; // Vertical res: 480, vertical interlace: on
      }

      //LAB_80038efc
      sendDisplayCommand(0x800_0000L | s0); // Display mode

      env.pad0.set((byte)8);
    }

    //LAB_80038f20
    if(DISPENV_80054728.screen.x.get() != env.screen.x.get() || DISPENV_80054728.screen.y.get() != env.screen.y.get() || DISPENV_80054728.screen.w.get() != env.screen.w.get() || DISPENV_80054728.screen.h.get() != env.screen.h.get() || env.pad0.get() == 0x8L) {
      //LAB_80038f98
      env.pad0.set((byte)GsGetWorkBase());

      if(env.pad0.get() == 0) {
        s0 = env.screen.y.get() + 0x10L;
      } else {
        s0 = env.screen.y.get() + 0x13L;
      }

      //LAB_80038fb8
      final long s2;

      if(env.screen.h.get() == 0) {
        s2 = s0 + 0xf0L;
      } else {
        s2 = s0 + env.screen.h.get();
      }

      //LAB_80038fcc
      v1 = env.disp.w.get();

      final long a2;
      if(v1 <= 280L) {
        a2 = 0;
      } else if(v1 <= 352L) {
        a2 = 1L;
      } else if(v1 <= 400L) {
        a2 = 2L;
      } else if(v1 <= 560L) {
        a2 = 3L;
      } else {
        a2 = 4L;
      }

      //LAB_80039008
      long v0 = (env.pad0.get() * 5 + a2) * 4;
      v1 = _80054790.offset(v0).get();
      v0 = _80054792.offset(v0).get() - v1;

      long a0 = v1 + (env.screen.x.get() * _800547bb.offset(a2).get() & 0xffffffffL);
      if(env.screen.w.get() != 0) {
        final long a3 = v0 * env.screen.w.get() & 0xffffffffL;
        v0 = a3 >> 0x8L;
      }

      //LAB_80039070
      v1 = a0 + v0;
      long a1;
      if(env.pad0.get() == 0) {
        //LAB_80039110
        if(a0 < 500L) {
          a1 = 500L;
        } else if(a0 <= 3250L) {
          a1 = a0;
        } else {
          a1 = 3250L;
        }

        //LAB_8003912c
        a0 = a1;

        if(v1 < a1) {
          a1 += _800547bb.offset(a2).get() * 4;
        } else if(v1 <= 3290L) {
          a1 = v1;
        } else {
          a1 = 3290L;
        }

        //LAB_80039160
        //LAB_80039164
        v1 = a1;

        if(s0 < 16L) {
          //LAB_80039180
          a1 = 16L;
        } else if(s0 <= 257L) {
          a1 = s0;
        } else {
          a1 = 257L;
        }

        //LAB_80039184
        s0 = a1;

        //LAB_8003919c
        if(s2 < a1) {
          a1 += 2L;
        } else if(s2 <= 258L) {
          a1 = s2;
        } else {
          a1 = 258L;
        }
      } else {
        if(a0 < 540L) {
          a1 = 540L;
        } else if(a0 <= 3220L) {
          a1 = a0;
        } else {
          a1 = 3220L;
        }

        //LAB_8003909c
        a0 = a1;

        if(v1 < a1) {
          a1 += _800547bb.offset(a2).get() * 4;
        } else if(v1 <= 3260L) {
          a1 = v1;
        } else {
          a1 = 3260L;
        }

        //LAB_800390d0
        //LAB_800390d4
        v1 = a1;

        if(s0 < 19L) {
          //LAB_800390f0
          a1 = 19L;
        } else if(s0 <= 303L) {
          a1 = s0;
        } else {
          a1 = 303L;
        }

        //LAB_800390f4
        s0 = a1;

        //LAB_8003919c
        if(s2 < a1) {
          a1 += 2L;
        } else if(s2 <= 305L) {
          a1 = s2;
        } else {
          a1 = 305L;
        }
      }

      //LAB_800391a8
      sendDisplayCommand(0x600_0000L | (v1 & 0xfffL) << 12 | a0 & 0xfffL); // Horizontal display range
      sendDisplayCommand(0x700_0000L | (a1 & 0x3ffL) << 10 | s0 & 0x3ffL); // Vertical display range
    }

    memcpy(DISPENV_80054728.getAddress(), env.getAddress(), 0x14);

    //LAB_80039204
    return env;
  }

  /**
   * <p>Initializes a DR_ENV primitive using the values contained in a DRAWENV structure. By using AddPrim() to
   * insert a DR_ENV primitive into your primitive list, it is possible to change part of your drawing environment
   * in the middle of drawing.</p>
   *
   * <p>The DR_ENV primitive uses the same information as the DRAWENV structure, but the data format is
   * different and the DRAWENV structure cannot be used as a primitive. When the DR_ENV primitive is
   * executed, the previous drawing environment settings are destroyed.</p>
   *
   * @param dr_env Pointer to drawing environment change primitive
   * @param drawEnv Pointer to drawing environment structure in which the drawing environment is described
   */
  @Method(0x80039550L)
  public static void SetDrawEnv(final DR_ENV dr_env, final DRAWENV drawEnv) {
    dr_env.code.get(0).set(makeSetDrawingAreaTopLeftCommand(drawEnv.clip.x.get(), drawEnv.clip.y.get()));
    dr_env.code.get(1).set(makeSetDrawingAreaBottomRightCommand(drawEnv.clip.x.get() + drawEnv.clip.w.get() - 1, drawEnv.clip.y.get() + drawEnv.clip.h.get() - 1));
    dr_env.code.get(2).set(makeSetDrawingOffsetCommand(drawEnv.ofs.get(0).get(), drawEnv.ofs.get(1).get()));
    dr_env.code.get(3).set(makeDrawModeSettingsCommand(drawEnv.dfe.get(), drawEnv.dtd.get(), drawEnv.tpage.get()));
    dr_env.code.get(4).set(makeTextureWindowSettingsCommand(drawEnv.tw));
    dr_env.code.get(5).set(0xe600_0000L); // Mask bit setting

    int size = 6;

    if(drawEnv.isbg.get() != 0) {
      final long width  = clamp(drawEnv.clip.w.get(), 0, _800546c0.get() - 1);
      final long height = clamp(drawEnv.clip.h.get(), 0, _800546c2.get() - 1);

      //LAB_800396ac
      if((drawEnv.clip.x.get() & 0x3fL) != 0 || (width & 0x3fL) != 0) {
        //LAB_800396d4
        // Monochrome quad
        dr_env.code.get(size++).set(0x6000_0000L | (drawEnv.b0.get() & 0xff) << 16 | (drawEnv.g0.get() & 0xff) << 8 | drawEnv.r0.get() & 0xff);
        dr_env.code.get(size++).set((drawEnv.clip.x.get() - drawEnv.ofs.get(0).get() & 0xffffL) << 16 | drawEnv.clip.y.get() - drawEnv.ofs.get(1).get() & 0xffffL);
      } else {
        //LAB_8003974c
        // Monochrome triangle, opaque
        dr_env.code.get(size++).set(0x200_0000L | (drawEnv.b0.get() & 0xff) << 16 | (drawEnv.g0.get() & 0xff) << 8 | drawEnv.r0.get() & 0xff);
        dr_env.code.get(size++).set((drawEnv.clip.x.get() & 0xffffL) << 16 | drawEnv.clip.y.get() & 0xffffL);
      }

      dr_env.code.get(size++).set(width << 16 | height);
    }

    //LAB_800397a4
    dr_env.tag.set(size << 24);
  }

  @Method(0x800397c0L)
  public static long makeDrawModeSettingsCommand(final long allowDrawing, final long dither, final long texturePage) {
    return 0xe100_0000L | (allowDrawing != 0 ? 0x400L : 0) | (dither != 0 ? 0x200L : 0x0L) | texturePage & 0x9ffL;
  }

  @Method(0x800397e0L)
  public static long makeSetDrawingAreaTopLeftCommand(final long x, final long y) {
    return 0xe300_0000L | clamp(y, 0, _800546c2.get()) << 10 | clamp(x, 0, _800546c0.get());
  }

  @Method(0x80039878L)
  public static long makeSetDrawingAreaBottomRightCommand(final long x, final long y) {
    return 0xe400_0000L | clamp(y, 0, _800546c2.get()) << 10 | clamp(x, 0, _800546c0.get());
  }

  @Method(0x80039910L)
  public static long makeSetDrawingOffsetCommand(final long x, final long y) {
    return 0xe500_0000L | (y & 0x7ffL) << 11 | x & 0x7ffL;
  }

  @Method(0x8003992cL)
  public static long makeTextureWindowSettingsCommand(final RECT rect) {
    if(rect == null) {
      return 0;
    }

    //LAB_8003993c
    final long y = rect.y.get() >> 3 << 15;
    final long x = rect.x.get() >> 3 << 10;
    final long h = (-rect.h.get() & 0xffL) >> 3 << 5;
    final long w = (-rect.w.get() & 0xffL) >> 3;

    //LAB_800399a4
    return 0xe200_0000L | y | x | h | w;
  }

  @Method(0x80039aa4L)
  public static long ClearImage_Impl(final RECT rect, final long colour) {
    rect.w.set(clamp(rect.w.get(), (short)0, (short)(_800546c0.get() - 1)));
    rect.h.set(clamp(rect.h.get(), (short)0, (short)(_800546c2.get() - 1)));

    LOGGER.info("Clearing screen %s %08x", rect, colour);

    if((rect.x.get() & 0x3fL) != 0 || (rect.w.get() & 0x3fL) != 0) {
      //LAB_80039b64
      _800c1bc0.build(builder -> builder
        .add(0xe300_0000L) // Draw area top left (0, 0)
        .add(0xe4ff_ffffL) // Draw area bottom right (1023, 511)
        .add(0xe500_0000L) // Draw offset (0, 0)
        .add(0xe600_0000L) // Mask bit (draw always)
        .add(0xe100_0000L | GPU_REG1.get(0x7ffL) | (int)colour >> 31 << 10) // Draw mode
        .add(0x6000_0000L | colour) // Monochrome rect (var size, opaque) - colour bbrrgg
        .add((rect.y.get() & 0xffffL) << 16 | rect.x.get() & 0xffffL) // - vertex YyyyXxxx
        .add((rect.h.get() & 0xffffL) << 16 | rect.w.get() & 0xffffL) // - w/h HhhhWwww
        .next(_800c1be8)
      );

      _800c1be8.build(builder -> builder
        .add(0xe300_0000L | getGpuInfo(0x3L)) // Draw area top left
        .add(0xe400_0000L | getGpuInfo(0x4L)) // Draw area top right
        .add(0xe500_0000L | getGpuInfo(0x5L)) // Draw offset
      );
    } else {
      //LAB_80039c3c
      _800c1bc0.build(builder -> builder
        .add(0xe600_0000L) // Mask bit (draw always)
        .add(0xe100_0000L | GPU_REG1.get(0x7ffL) | (int)colour >> 31 << 10) // Draw mode
        .add(0x0200_0000L | colour) // Fill rect - BbGgRr
        .add((rect.y.get() & 0xffffL) << 16 | rect.x.get() & 0xffffL) // - YyyyXxxx
        .add((rect.h.get() & 0xffffL) << 16 | rect.w.get() & 0xffffL) // - HhhhWwww
      );
    }

    //LAB_80039cac
    uploadLinkedListToGpu(_800c1bc0);
    return 0;
  }

  @Method(0x80039cd4L)
  public static long uploadImageToGpu(final RECT rect, final long address) {
    recordGpuTime();

    rect.w.set((short)clamp(rect.w.get(), 0, _800546c0.get()));
    rect.h.set((short)clamp(rect.h.get(), 0, _800546c2.get()));

    //LAB_80039d78
    //LAB_80039d7c
    final long dataSize = rect.w.get() * rect.h.get() + 1;
    if(dataSize / 2 <= 0) {
      return -0x1L;
    }

    //LAB_80039db0
//    final long numberOfDmaTransfers = dataSize / 32; // 32 half-words per 16-word DMA block
//    long numberOfRegTransfers = dataSize / 2 - numberOfDmaTransfers * 16;

    //LAB_80039de0
    while(GPU_REG1.get(0x400_0000L) == 0) {
      if(Hardware.isGpuThread()) {
        GPU.tick();
      }

      if(checkForGpuTimeout() != 0) {
        return -0x1L;
      }
    }

    GPU.command01ClearCache();
    GPU.commandA0CopyRectFromCpuToVram(rect, address);

    // Pre-optimisation
    //LAB_80039e10
//    GPU_REG1.setu(0x400_0000L); // DMA direction: off
//    GPU_REG0.setu(0x100_0000L); // Clear cache

    //LAB_80039e44
//    GPU_REG0.setu(0xa000_0000L); // GP0(a0h) - Copy rect from CPU to VRAM
//    GPU_REG0.setu(rect.getY() << 0x10 | rect.getX());
//    GPU_REG0.setu(rect.getH() << 0x10 | rect.getW());

    //LAB_80039e80
//    long s2 = 0;
//    while(numberOfRegTransfers != 0) {
//      GPU_REG0.setu(MEMORY.ref(4, address).offset(s2));
//      s2 += 0x4L;
//      numberOfRegTransfers--;
//    }

    //LAB_80039e9c
//    if(numberOfDmaTransfers != 0) {
//      GPU_REG1.setu(0x400_0002L); // DMA type CPU to GP0
//      DMA_GPU_MADR.setu(address + s2);
//      DMA_GPU_BCR.setu(numberOfDmaTransfers << 0x10L | 0x10L); // Number of blocks | block size 16
//      DMA_GPU_CHCR.setu(0b1_0000_0000_0000_0010_0000_0001L); // Direction: from RAM; sync mode: 1 (sync blocks to DMA requests); start/busy: start/enable/busy;
//    }

    //LAB_80039ee8
    //LAB_80039eec
    return 0;
  }

  @Method(0x8003a190L)
  public static void sendDisplayCommand(final long command) {
    GPU_REG1.setu(command);
  }

  @Method(value = 0x8003a1ecL, ignoreExtraParams = true)
  public static int uploadLinkedListToGpu(final MemoryRef address) {
    return GPU.uploadLinkedList(address.getAddress());

    // Pre-optimization
//    GPU_REG1.setu(0x400_0002L); // DMA direction: CPU to GP0
//    DMA_GPU_MADR.setu(address.getAddress());
//    DMA_GPU_BCR.setu(0);

    // Bit 1    - from main RAM
    // Bit 9-10 - Sync mode 2: linked-list
    // Bit 24   - Start/busy - start/enable/busy
//    DMA_GPU_CHCR.setu(0b1_0000_0000_0000_0100_0000_0001L);
  }

  @Method(0x8003a234L)
  public static long getGpuInfo(final long type) {
    GPU_REG1.setu(0x1000_0000L | type); // Get GPU info
    return GPU_REG0.get(0xff_ffffL);
  }

  @Method(0x8003a288L)
  public static int FUN_8003a288(final BiFunctionRef<MemoryRef, Long, Integer> callback, final MemoryRef arg1, final long arg1Size, final long arg2) {
    recordGpuTime();

    //LAB_8003a2bc
    while((gpuQueueIndex_800547e4.get() + 0x1L & 0x3fL) == gpuQueueTotal_800547e8.get()) {
      if(Hardware.isGpuThread()) {
        GPU.tick();
      }

      if(checkForGpuTimeout() != 0) {
        return 0xffff_ffff;
      }

      //LAB_8003a2d4
    }

    oldIMask_800547ec.setu(setIMask(0));
    _800546c4.setu(0x1L);

    if(_800546bd.get() == 0 || gpuQueueIndex_800547e4.get() == gpuQueueTotal_800547e8.get() && DMA.gpu.CHCR.get(0x100_0000L) == 0 && drawSyncCallback_800546c8.get() == 0) {
      //LAB_8003a368
      //LAB_8003a374
      while(GPU_REG1.get(0x400_0000L) == 0) {
        DebugHelper.sleep(1);
      }

      callback.run(arg1, arg2);

      setIMask(oldIMask_800547ec.get());
      return 0;
    }

    //LAB_8003a3ac
    SetDmaInterruptCallback(DmaChannelType.GPU, getMethodAddress(Scus94491BpeSegment_8003.class, "gpuDmaCallback"));

    if(arg1Size == 0) {
      //LAB_8003a468
      gpuDmaCallbackObjPtr_800c1c14.offset(gpuQueueIndex_800547e4.get() * 96).setu(arg1.getAddress());
    } else {
      //LAB_8003a3d4
      for(int i = 0; i < arg1Size; i += 4) {
        //LAB_8003a3e0
        gpuDmaCallbackObj_800c1c1c.offset(gpuQueueIndex_800547e4.get() * 96L).offset(i).setu(MEMORY.ref(4, arg1.getAddress()).offset(i));
      }

      //LAB_8003a424
      gpuDmaCallbackObjPtr_800c1c14.offset(gpuQueueIndex_800547e4.get() * 96).setu(gpuDmaCallbackObj_800c1c1c.offset(gpuQueueTotal_800547e8.get() * 96).getAddress());
    }

    //LAB_8003a48c
    gpuDmaCallbackSomething_800c1c18.offset(gpuQueueIndex_800547e4.get() * 96).setu(arg2);
    gpuDmaCallback_800c1c10.offset(gpuQueueIndex_800547e4.get() * 96).setu(callback.getAddress());
    gpuQueueIndex_800547e4.addu(0x1L).and(0x3fL);
    setIMask(oldIMask_800547ec.get());
    gpuDmaCallback();

    //LAB_8003a51c
    return (int)(gpuQueueIndex_800547e4.get() - gpuQueueTotal_800547e8.get() & 0x3fL);
  }

  @Method(0x8003a538L)
  public static long gpuDmaCallback() {
    if(DMA.gpu.CHCR.get(0x100_0000L) != 0) {
      return 0x1L;
    }

    oldIMask_800547f0.setu(setIMask(0));

    if(gpuQueueIndex_800547e4.get() != gpuQueueTotal_800547e8.get()) {
      //LAB_8003a5b0
      while(DMA.gpu.CHCR.get(0x100_0000L) == 0) {
        if((gpuQueueTotal_800547e8.get() + 1 & 0x3fL) == gpuQueueIndex_800547e4.get()) {
          if(drawSyncCallback_800546c8.get() == 0) {
            SetDmaInterruptCallback(DmaChannelType.GPU, 0);
          }
        }

        //LAB_8003a5ec
        //LAB_8003a60c
        while(GPU_REG1.get(0x400_0000L) == 0) {
          DebugHelper.sleep(1);
        }

        //LAB_8003a620
        gpuDmaCallback_800c1c10.offset(gpuQueueTotal_800547e8.get() * 96).deref(4).call(
          gpuDmaCallbackObjPtr_800c1c14.offset(gpuQueueTotal_800547e8.get() * 96).get(),
          gpuDmaCallbackSomething_800c1c18.offset(gpuQueueTotal_800547e8.get() * 96).get()
        );

        gpuQueueTotal_800547e8.addu(0x1L).and(0x3fL);

        if(gpuQueueIndex_800547e4.get() == gpuQueueTotal_800547e8.get()) {
          break;
        }
      }
    }

    //LAB_8003a6e8
    setIMask(oldIMask_800547f0.get());

    if(gpuQueueIndex_800547e4.get() == gpuQueueTotal_800547e8.get() && DMA.gpu.CHCR.get(0x100_0000L) == 0 && _800546c4.get() != 0) {
      if(drawSyncCallback_800546c8.get() != 0) {
        _800546c4.setu(0);
        drawSyncCallback_800546c8.deref(4).call();
      }
    }

    //LAB_8003a768
    //LAB_8003a784
    return gpuQueueIndex_800547e4.get() - gpuQueueTotal_800547e8.get() & 0x3fL;
  }

  @Method(0x8003a798L)
  public static long FUN_8003a798(final long mode) {
    _800547f4.setu(setIMask(0));
    gpuQueueIndex_800547e4.setu(0);
    gpuQueueTotal_800547e8.setu(0);

    final long v1 = mode & 0b111L;
    if(v1 == 0x1L || v1 == 0x3L) {
      //LAB_8003a854
      DMA.gpu.CHCR.setu(0b100_0000_0001L); // Direction: from RAM; sync mode: 2 (linked-list)
      DMA.gpu.enable();
      GPU_REG1.setu(0x200_0000L); // Acknowledge GPU interrupt
      GPU_REG1.setu(0x100_0000L); // Reset command buffer
    } else if(v1 == 0 || v1 == 0x5L) {
      //LAB_8003a808
      DMA.gpu.CHCR.setu(0b100_0000_0001L); // Direction: from RAM; sync mode: 2 (linked-list)
      DMA.gpu.enable();
      GPU_REG1.setu(0); // Reset GPU
      fillMemory(gpuDmaCallback_800c1c10.getAddress(), (byte)0, 0x1800);
    }

    //LAB_8003a8a0
    setIMask(_800547f4.get());

    if((mode & 0b111) != 0) {
      return 0;
    }

    //LAB_8003a8c4
    return FUN_8003ab88(mode);
  }

  @Method(0x8003a8d4L)
  public static int DrawSync_Impl(final int mode) {
    if(mode == 0) {
      recordGpuTime();

      //LAB_8003a8f4
      while(gpuQueueIndex_800547e4.get() != gpuQueueTotal_800547e8.get()) {
        if(Hardware.isGpuThread()) {
          GPU.tick();
        }

        gpuDmaCallback();

        if(checkForGpuTimeout() != 0) {
          return 0xffff_ffff;
        }
      }

      //LAB_8003a940
      long v0 = DMA.gpu.CHCR.get(0x100_0000L);

      if(v0 == 0) {
        v0 = GPU_REG1.get(0x400_0000L);
      }

      //LAB_8003a930
      while(v0 == 0) {
        if(Hardware.isGpuThread()) {
          GPU.tick();
        }

        if(checkForGpuTimeout() != 0) {
          return 0xffff_ffff;
        }

        //LAB_8003a940
        v0 = DMA.gpu.CHCR.get(0x100_0000L);

        if(v0 == 0) {
          v0 = GPU_REG1.get(0x400_0000L);
        }
      }

      return 0;
    }

    //LAB_8003a988
    final long s0 = gpuQueueIndex_800547e4.get() - gpuQueueTotal_800547e8.get() & 0x3fL;
    if(s0 != 0) {
      gpuDmaCallback();
    }

    //LAB_8003a9b4
    if(DMA.gpu.CHCR.get(0x100_0000L) == 0 && GPU_REG1.get(0x400_0000L) != 0) {
      return (int)s0;
    }

    //LAB_8003a9f4
    if(s0 == 0) {
      return 1;
    }

    //LAB_8003aa00
    return (int)s0;
  }

  @Method(0x8003aa10L)
  public static void recordGpuTime() {
    _800547f8.setu(VSync(-1) + 0xf0L);
    _800547fc.setu(0);
  }

  @Method(0x8003aa44L)
  public static long checkForGpuTimeout() {
    if(VSync(-1) <= _800547f8.get() && _800547fc.addu(0x1L).get() <= 0xf_0000L) {
      //LAB_8003ab74
      return  0;
    }

    //LAB_8003aa98
    LOGGER.error("GPU timeout:que=%d,stat=%08x,chcr=%08x,madr=%08x", gpuQueueIndex_800547e4.get() - gpuQueueTotal_800547e8.get() & 0x3fL, GPU_REG1.get(), DMA.gpu.CHCR.get(), DMA.gpu.MADR.get());

    _800547f4.setu(setIMask(0));
    gpuQueueTotal_800547e8.setu(0);
    gpuQueueIndex_800547e4.setu(0);
    DMA.gpu.CHCR.setu(0x401L);
    DMA_DPCR.oru(0x800L);
    GPU_REG1.setu(0x200_0000L);
    GPU_REG1.setu(0x100_0000L);
    setIMask(_800547f4.get());

    //LAB_8003ab78
    return -0x1L;
  }

  @Method(0x8003ab88L)
  public static long FUN_8003ab88(final long mode) {
    GPU_REG1.setu(0x1000_0007L); // Get GPU info - GPU version

    // If it's an old GPU
    if(GPU_REG0.get(0xff_ffffL) != 0x2L) {
      // Draw mode settings (aka. texpage)
      // Forces bit 12 - textured rectangle x-flip
      GPU_REG0.setu(0xe100_0000L | 0b1_0000_0000_0000 | GPU_REG1.get(0b11_1111_1111_1111L)); // GPUSTAT
      return 0;
    }

    //LAB_8003abf8
    if((mode & 0b1000L) != 0) {
      GPU_REG1.setu(0x900_0001L); // New texture disable - allow texture disable via GP0(e1h).11
      return 0x2L;
    }

    //LAB_8003ac1c
    //LAB_8003ac20
    return 0x1L;
  }

  @Method(0x8003b068L)
  public static void fillMemory(final long address, final byte fill, final int length) {
    if(length == 0) {
      return;
    }

    //LAB_8003b074
    for(int offset = 0; offset < length; offset++) {
      MEMORY.set(address + offset, fill);
    }
  }

  @Method(0x8003b094L)
  public static void GPU_cw(final int command) {
    functionVectorA_000000a0.run(0x49L, new Object[] {command});
  }

  @Method(0x8003b0b4L)
  public static long GsGetWorkBase() {
    return GsOUT_PACKET_P.get();
  }

  /**
   * BreakDraw?
   */
  @Method(0x8003b0d0L)
  public static long FUN_8003b0d0() {
    if(DMA.gpu.CHCR.get(0x100_0000L) == 0) { // Transfer stopped/completed
      return 0;
    }

    if(DMA.gpu.CHCR.get(0x700L) >>> 8 != 0x4L) { // Chopping enabled, or not linked list
      return -0x1L;
    }

    DMA.gpu.CHCR.and(0xfeff_ffffL);

    if(DMA.gpu.MADR.get(0xff_ffffL) == 0xff_ffffL) {
      return 0;
    }

    //LAB_8003b15c
    return DMA.gpu.MADR.get();
  }

  /**
   * <p>Calculates the texture page ID, and returns it.</p>
   *
   * <p>The semitransparent rate is also effective for polygons on which texture mapping is not performed.
   * The texture page address is limited to a multiple of 64 in the X direction and a multiple of 256 in the Y
   * direction.</p>
   *
   * @param tp Texture mode
   *           <ol start="0">
   *             <li>4-bit CLUT</li>
   *             <li>8-bit CLUT</li>
   *             <li>16-bit Direct</li>
   *           </ol>
   * @param abr Semitransparency rate
   *           <ol start="0">
   *             <li>0.5 x Back + 0.5 x Forward</li>
   *             <li>1.0 x Back + 1.0 x Forward</li>
   *             <li>1.0 x Back - 1.0 x Forward</li>
   *             <li>1.0 x Back + 0.25 x Forward</li>
   *           </ol>
   * @param x X
   * @param y Y
   *
   * @return Texture page ID
   */
  @Method(0x8003b3f0L)
  public static long GetTPage(final long tp, final long abr, final long x, final long y) {
    return
      ((tp & 0x3L) << 0x7L |
      (abr & 0x3L) << 0x5L |
      (y & 0x100L) >> 0x4L |
      (x & 0x3ffL) >> 0x6L |
      (y & 0x200L) << 0x2L) & 0xffffL;
  }

  /**
   * <p>Calculates and returns the texture CLUT ID.</p>
   *
   * <p>The CLUT address is limited to multiples of 16 in the x direction.</p>
   *
   * @param x X
   * @param y Y
   *
   * @return CLUT ID.
   */
  @Method(0x8003b430L)
  public static long GetClut(final long x, final long y) {
    return (y << 6 | x >> 4 & 0x3fL) & 0xffffL;
  }

  @Method(0x8003b450L)
  public static void FUN_8003b450(final long a0, final long a1, final long a2) {
    MEMORY.ref(4, a2).and(0xff00_0000L).oru(MEMORY.ref(4, a0).get() & 0xff_ffffL);
    MEMORY.ref(4, a0).and(0xff00_0000L).oru(a1 & 0xff_ffffL);
  }

  @Method(0x8003b490L)
  public static void gpuLinkedListSetCommandTransparency(final long entryAddress, final boolean transparency) {
    if(transparency) {
      MEMORY.ref(1, entryAddress).offset(0x7L).oru(0x2L);
    } else {
      MEMORY.ref(1, entryAddress).offset(0x7L).and(0xfdL);
    }
  }

  @Method(0x8003b4c0L)
  public static void gpuLinkedListSetCommandTextureUnshaded(final long entryAddress, final boolean unshaded) {
    if(unshaded) {
      MEMORY.ref(1, entryAddress).offset(0x7L).oru(0x1L);
    } else {
      MEMORY.ref(1, entryAddress).offset(0x7L).and(0xfeL);
    }
  }

  @Method(0x8003b530L)
  public static void setGp0_30(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x6L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x30L);
  }

  @Method(0x8003b570L)
  public static void setGp0_28(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x5L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x28L);
  }

  @Method(0x8003b590L)
  public static void setGp0_2c(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x9L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x2cL);
  }

  @Method(0x8003b5b0L)
  public static void setGp0_38(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x8L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x38L);
  }

  @Method(0x8003b630L)
  public static void setGp0_64(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x64L);
  }

  @Method(0x8003b690L)
  public static void setGp0_50(final long a0) {
    MEMORY.ref(1, a0).offset(0x3L).setu(0x4L);
    MEMORY.ref(1, a0).offset(0x7L).setu(0x50L);
  }

  @Method(0x8003b750L)
  public static void SetDrawTPage(final DR_TPAGE p, final long allowDrawing, final long dither, final long tpage) {
    p.tag.and(0xff_ffffL).or(0x100_0000L);

    final long command = 0xe100_0000L; // Texpage/draw mode settings

    final long ditherBit;
    if(dither != 0) {
      ditherBit = 0x200L;
    } else {
      ditherBit = 0;
    }

    //LAB_8003b764
    final long drawingBit;
    if(allowDrawing != 0) {
      drawingBit = 0x400L;
    } else {
      drawingBit = 0;
    }

    final long otherBits = tpage & 0x9ffL; // Texpage X/Y base; trans; texpage colours, tex disable

    //LAB_8003b770
    p.code.get(0).set(command | drawingBit | ditherBit | otherBits);
  }

  @Method(0x8003b780L)
  public static void SetDrawMove(final DR_MOVE p, final RECT src, final long destX, final long destY) {
    p.tag.and(0xff_ffffL);

    //LAB_8003b7a4
    if(src.w.get() != 0 && src.h.get() != 0) {
      p.tag.or(0x500_0000L);
    }

    //LAB_8003b7ac
    p.code.get(0).set(0x100_0000L);
    p.code.get(1).set(0x8000_0000L); // Copy rect VRAM to VRAM
    p.code.get(2).set((src.y.get() & 0xffffL) << 16 | src.x.get() & 0xffffL); // src
    p.code.get(3).set((destY & 0xffffL) << 16 | destX & 0xffffL); // dest
    p.code.get(4).set((src.h.get() & 0xffffL) << 16 | src.w.get() & 0xffffL); // size
  }

  /**
   * <p>Links primitive p0 to primitive p1. The combined primitive size of p0 and p1 must be less than 15 words.
   * Within this size, any number of connections is possible.</p>
   *
   * <p>The resulting linked primitives can be added to an OT using AddPrim().
   * p0 and p1 describe continuous regions of memory. p1 must be the higher address.</p>
   *
   * @param packet1 First primitive
   * @param packet2 Second primitive
   *
   * @return 0 on success, -1 on failure.
   */
  @Method(0x8003b7e0L)
  public static long MargePrim(final long packet1, final long packet2) {
    final long size = MEMORY.ref(1, packet2).offset(0x3L).get() + MEMORY.ref(1, packet1).offset(0x3L).get() + 0x1L;

    if(size < 0x11L) {
      MEMORY.ref(1, packet1).offset(0x3L).setu(size);
      MEMORY.ref(4, packet2).setu(0);
      return 0;
    }

    //LAB_8003b80c
    //LAB_8003b810
    return -0x1L;
  }

  /**
   * Initialize content of a drawing mode primitive.
   * <p>
   * Initializes a DR_MODE primitive. By using AddPrim() to insert a DR_MODE primitive into your primitive list, it
   * is possible to change part of your drawing environment in the middle of drawing.<br>
   * If tw is 0, the texture window is not changed.
   *
   * @param drawMode Pointer to drawing mode primitive
   * @param allowDrawingToDisplayArea 0: drawing not allowed in display area, 1: drawing allowed in display area
   * @param dither 0: dithering off, 1: dithering on
   * @param texturePage Texture page
   * @param textureWindow Pointer to texture window
   */
  @Method(0x8003b850L)
  public static void SetDrawMode(final DR_MODE drawMode, final boolean allowDrawingToDisplayArea, final boolean dither, final long texturePage, @Nullable final RECT textureWindow) {
    drawMode.tag.and(0xff_ffffL).or(0x200_0000L);

    long drawingMode = 0xe100_0000L | texturePage & 0x9ffL;
    if(dither) {
      drawingMode |= 0x200L;
    }

    //LAB_8003b86c
    if(allowDrawingToDisplayArea) {
      drawingMode |= 0x400L;
    }

    //LAB_8003b878
    drawMode.code.get(0).set(drawingMode);

    if(textureWindow == null) {
      //LAB_8003b8d8
      drawMode.code.get(1).set(0);
    } else {
      final long val = 0xe200_0000L |
        textureWindow.y.get() >> 3 << 15 | // Texture window offset Y
        textureWindow.x.get() >> 3 << 10 | // Texture window offset X
        (-textureWindow.h.get() & 0xf8L) << 2 | // Texture window mask Y
        (-textureWindow.w.get() & 0xffL) >> 3; // Texture window mask X
      drawMode.code.get(1).set(val);
    }

    //LAB_8003b8dc
  }

  @Method(0x8003b8f0L)
  public static void FUN_8003b8f0(final long a0) {
    _800c3410.setu(a0);
  }

  @Method(0x8003b900L)
  public static WeirdTimHeader FUN_8003b900(final WeirdTimHeader timHeader) {
    final long ret = FUN_8003b964(_800c3410.get(), timHeader);

    if(ret == -0x1L) {
      //LAB_8003b950
      return null;
    }

    _800c3410.addu(ret * 0x4L);

    //LAB_8003b954
    return timHeader;
  }

  /** TODO figure out what this is doing - looks important... TIM loader? Why is this one different? */
  @Method(0x8003b964L)
  public static long FUN_8003b964(final long a0, final WeirdTimHeader timHeader) {
    if(MEMORY.ref(4, a0).get() != 0x10L) {
      return -0x1L;
    }

    //LAB_8003b998
    timHeader.flags.set(MEMORY.ref(4, a0).offset(0x4L).get());

    LOGGER.info("id  =%08x", 0x10L);
    LOGGER.info("mode=%08x", timHeader.flags.get());
    LOGGER.info("timaddr=%08x", a0 + 0x8L);

    //LAB_8003ba04
    final long a0_0;
    if((timHeader.flags.get() & 0b1000L) != 0) {
      timHeader.clutRect.set(MEMORY.ref(4, a0).offset(0xcL).cast(RECT::new));
      timHeader.clutAddress.set(a0 + 0x14L);
      a0_0 = MEMORY.ref(4, a0).offset(0x8L).get() / 0x4L;
    } else {
      //LAB_8003ba38
      timHeader.clutRect.clear();
      timHeader.clutAddress.set(0);
      a0_0 = 0;
    }

    final long s0 = a0 + a0_0 * 0x4L;

    //LAB_8003ba44
    timHeader.imageRect.set(MEMORY.ref(4, s0).offset(0xcL).cast(RECT::new));
    timHeader.imageAddress.set(s0 + 0x14L);

    //LAB_8003ba64
    return MEMORY.ref(4, s0).offset(0x8L).get() / 0x4L + 0x2L + a0_0; // +8 CLUT data pointer / 4 + 2 (plus CLUT data pointer if CLUT present) ???
  }

  /**
   * <p>Initialize the graphics system.</p>
   *
   * <p>Resets libgpu and initializes the libgs graphic system. libgpu settings are maintained by the global variables
   * GsDISPENV and GsDRAWENV. The programmer can verify and/or modify libgpu by referencing the settings.</p>
   *
   * <p>Vertical 480 line non-interlace mode is effective only when a VGA monitor is connected. In 240-line mode,
   * the top and bottom 8 lines are almost invisible on home-use TV monitors. For PAL mode, the display
   * position should be shifted down by 24 lines.</p>
   *
   * <p>The double buffer offset mode is either GTE or GPU offset; when it is GPU, the packet does not include the
   * offset value and can therefore be handled easily.</p>
   *
   * <p>For 24-bit mode, only the memory image display is available and polygon drawing cannot be done.
   * Since initialization of the graphic system involves initialization of GsIDMATRIX and GsIDMATRIX2 as well,
   * GsInitGraph() must be called prior to all other libgs functions for correct operation.</p>
   *
   * @param displayWidth Horizontal resolution (256/320/384/512/640)
   * @param displayHeight Vertical resolution (240/480 NTSC or 256/512 PAL)
   * @param flags
   * <ul>
   * <li>Interlace display flag (bit 0)<ul>
   * <li>0: Non-interlace GsNONINTER</li>
   * <li>1: Interlace GSINTER</li>
   * </ul></li>
   * <li>Double buffer offset mode (bit 2)<ul>
   * <li>0: GTE offset GsOFSGTE</li>
   * <li>1: GPU offset GsOFSGPU</li>
   * </ul></li>
   * <li>GPU Initialize Parameter (bits 4-5)<ul>
   * <li>0: ResetGraph(0) GsRESET0</li>
   * <li>3: ResetGraph(3) GsRESET3</li>
   * </ul></li>
   * </ul>
   * @param dither Dithering enabled
   * @param use24BitColour VRAM BPP
   */
  @Method(0x8003bc30L)
  public static void GsInitGraph(final short displayWidth, final short displayHeight, final int flags, final boolean dither, final boolean use24BitColour) {
    GsInitGraph2(displayWidth, displayHeight, flags, dither, use24BitColour);
    GsInit3D();

    PSDIDX_800c34d4.setu(0);

    FUN_8003be28(displayWidth, displayHeight);
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();
  }

  /**
   * <p>GsInitGraph2() is different from {@link Scus94491BpeSegment_8003#GsInitGraph}() in that the GPU is not initialized COLD. This function is useful
   * for changing libgs resolution without affecting screen synchronization.</p>
   *
   * <p>Always use GsInitGraph() for the first initialization.</p>
   */
  @Method(0x8003bca4L)
  public static void GsInitGraph2(final short displayWidth, final short displayHeight, final int flags, final boolean dither, final boolean use24BitColour) {
    if((flags >> 4 & 0b11) == 0b11) {
      ResetGraph(3);
    } else {
      ResetGraph(0);
    }

    DRAWENV_800c3450.ofs.get(0).set((short)0);
    DRAWENV_800c3450.ofs.get(1).set((short)0);
    DRAWENV_800c3450.tw.set((short)0, (short)0, (short)0, (short)0);
    DRAWENV_800c3450.tpage.set((short)0);
    DRAWENV_800c3450.dtd.set((byte)(dither ? 1 : 0));
    DRAWENV_800c3450.dfe.set((byte)0);
    DRAWENV_800c3450.isbg.set((byte)0);
    PutDrawEnv(DRAWENV_800c3450);

    DISPENV_800c34b0.disp.set((short)0, (short)0, displayWidth, displayHeight);
    DISPENV_800c34b0.screen.set((short)0, (short)0, (short)0, (short)0); // W/H of 0 are apparently treated as 256x240

    if(GsGetWorkBase() == 1) {
      DISPENV_800c34b0.screen.y.set((short)0x18);
      DISPENV_800c34b0.pad0.set((byte)1);
    }

    DISPENV_800c34b0.isinter.set((byte)(flags & 0b1));
    DISPENV_800c34b0.isrgb24.set((byte)(use24BitColour ? 1 : 0));

    doubleBufferOffsetMode_800c34d6.set(GsOffsetType.fromValue(flags & 0b100));

    PutDispEnv(DISPENV_800c34b0);
  }

  @Method(0x8003be28L)
  public static void FUN_8003be28(final long displayWidth, final long displayHeight) {
    displayWidth_1f8003e0.setu(displayWidth);
    displayHeight_1f8003e4.setu(displayHeight);

    final long a0 = (displayHeight << 14) / displayWidth / 3;

    identityMatrix_800c3568.set(0, 0, (short)0x1000);
    identityMatrix_800c3568.set(0, 1, (short)0);
    identityMatrix_800c3568.set(0, 2, (short)0);
    identityMatrix_800c3568.set(1, 0, (short)0);
    identityMatrix_800c3568.set(1, 1, (short)0x1000);
    identityMatrix_800c3568.set(1, 2, (short)0);
    identityMatrix_800c3568.set(2, 0, (short)0);
    identityMatrix_800c3568.set(2, 1, (short)0);
    identityMatrix_800c3568.set(2, 2, (short)0x1000);
    identityMatrix_800c3568.transfer.setX(0);
    identityMatrix_800c3568.transfer.setY(0);
    identityMatrix_800c3568.transfer.setZ(0);

    matrix_800c3588.set(0, 0, (short)0x1000);
    matrix_800c3588.set(0, 1, (short)0);
    matrix_800c3588.set(0, 2, (short)0);
    matrix_800c3588.set(1, 0, (short)0);
    matrix_800c3588.set(1, 1, (short)a0);
    matrix_800c3588.set(1, 2, (short)0);
    matrix_800c3588.set(2, 0, (short)0);
    matrix_800c3588.set(2, 1, (short)0);
    matrix_800c3588.set(2, 2, (short)0x1000);
    matrix_800c3588.transfer.setX(0);
    matrix_800c3588.transfer.setY(0);
    matrix_800c3588.transfer.setZ(0);

    lightDirectionMatrix_800c34e8.clear();
    lightColourMatrix_800c3508.clear();

    clip_800c3448.clear();

    centreScreenY_1f8003de.setu(0);
    centreScreenX_1f8003dc.setu(0);

    displayRect_800c34c8.set((short)0, (short)0, (short)displayWidth, (short)displayHeight);

    _800c3423.setu(0x3L);
    _800c3427.setu(0x2L);
    _800c3433.setu(0x3L);
    _800c3437.setu(0x2L);

    PSDCNT_800c34d0.setu(0x1L);
  }

  /**
   * <p>Register a screen clear command in the OT.</p>
   *
   * <p>Sets a screen clear command at the start of the OT indicated by otp. Should be called after
   * GsSwapDispBuff(). Note: Actual clearing isn’t executed until GsDrawOt() is used to start drawing.</p>
   *
   * @param r R
   * @param g G
   * @param b B
   * @param ot Ordering table
   */
  @Method(0x8003c048L)
  public static void GsSortClear(final long r, final long g, final long b, final GsOT ot) {
    _800c3424.offset(PSDIDX_800c34d4.get() * 0x10L).setu(r);
    _800c3425.offset(PSDIDX_800c34d4.get() * 0x10L).setu(g);
    _800c3426.offset(PSDIDX_800c34d4.get() * 0x10L).setu(b);

    if(PSDIDX_800c34d4.get() == 0) {
      _800c3428.offset(PSDIDX_800c34d4.get() * 0x10L).setu(clip_800c3440.x1.get());
      _800c342a.offset(PSDIDX_800c34d4.get() * 0x10L).setu(clip_800c3440.y1.get());
    } else {
      _800c3428.offset(PSDIDX_800c34d4.get() * 0x10L).setu(clip_800c3440.x2.get());
      _800c342a.offset(PSDIDX_800c34d4.get() * 0x10L).setu(clip_800c3440.y2.get());
    }

    _800c342e.offset(PSDIDX_800c34d4.get() * 0x10L).setu(displayHeight_1f8003e4);

    if(DISPENV_800c34b0.isrgb24.get() == 0) {
      //LAB_8003c13c
      _800c342c.offset(PSDIDX_800c34d4.get() * 0x10L).setu(displayWidth_1f8003e0);
    } else {
      _800c342c.offset(PSDIDX_800c34d4.get() * 0x10L).setu(displayWidth_1f8003e0.get() * 3 / 2);
    }

    //LAB_8003c150
    AddPrim(ot.tag_10.deref(), _800c3420.offset(PSDIDX_800c34d4.get() * 0x10L).getAddress());
  }

  /**
   * <p>Registers a primitive beginning with the address *p to the OT entry *ot in OT table. ot is an ordering table or
   * pointer to another primitive.</p>
   *
   * <p>A primitive may be added to a primitive list only once in the same frame. Attempting to add it multiple times
   * in the same frame results in a corrupted list.</p>
   *
   * @param ot OT entry
   * @param p Start address of primitive to be registered
   */
  @Method(0x8003c180L)
  public static void AddPrim(final GsOT_TAG ot, final long p) {
    MEMORY.ref(3, p).setu(ot.p.get());
    ot.p.set(p & 0xff_ffffL);
  }

  @Method(0x8003c1c0L)
  public static void GsSetDrawBuffOffset() {
    final short x = (short)centreScreenX_1f8003dc.get();
    final short y = (short)centreScreenY_1f8003de.get();

    final short clipX;
    final short clipY;
    if(PSDIDX_800c34d4.get() == 0) {
      clipX = clip_800c3440.x1.get();
      clipY = clip_800c3440.y1.get();
    } else {
      clipX = clip_800c3440.x2.get();
      clipY = clip_800c3440.y2.get();
    }

    if(doubleBufferOffsetMode_800c34d6.get() == GsOffsetType.GsOFSGTE) {
      SetGeomOffset(x + clipX, y + clipY);

      _800c34c4.setu(x + clipX);
      _800c34c6.setu(y + clipY);
    } else {
      _800c34c4.setu(0);
      _800c34c6.setu(0);

      DRAWENV_800c3450.ofs.get(0).set((short)(x + clipX));
      DRAWENV_800c3450.ofs.get(1).set((short)(y + clipY));

      PutDrawEnv(DRAWENV_800c3450);
    }
  }

  /**
   * Set drawing clipping area.<p>
   * Sets clipping for drawing. The clipping value set by GsSetClip2D() is set in libgs.<p>
   * This value is a relative one within the double buffers, so the clipping position does not change when buffers
   * are swapped.<p>
   * This function does not execute correctly if GPU drawing is in progress. Use ResetGraph(1) to terminate any
   * current drawing process or DrawSync() to wait until the process is completed.
   */
  @Method(0x8003c2d0L)
  public static void GsSetDrawBuffClip() {
    DRAWENV_800c3450.clip.set(
      (short)(displayRect_800c34c8.x.get() + (PSDIDX_800c34d4.get() == 0 ? clip_800c3440.x1 : clip_800c3440.x2).get()),
      (short)(displayRect_800c34c8.y.get() + (PSDIDX_800c34d4.get() == 0 ? clip_800c3440.y1 : clip_800c3440.y2).get()),
      displayRect_800c34c8.w.get(),
      displayRect_800c34c8.h.get()
    );

    PutDrawEnv(DRAWENV_800c3450);
  }

  /**
   * <p>Swaps double buffers.</p>
   *
   * <p>Exchanges the display buffer with the drawing buffer according to data set by GsDefDispBuff(). Normally,
   * swapping is done immediately after beginning vertical blanking. This function<ul>
   * <li>Sets display starting address</li>
   * <li>Cancels blanking</li>
   * <li>Sets double buffer index</li>
   * <li>Switches two-dimensional clipping</li>
   * <li>Sets libgte or libgpu offset</li>
   * <li>Sets libgs offset</li></ul></p>
   *
   * <p>Note: Using the GsOFSGPU or GsOFSGTE macro for the third argument of GsInitGraph() determines
   * whether the libgte or libgpu offset should be set.</p>
   *
   * <p>This function does not execute correctly when GPU drawing is in progress, so it is necessary to call this
   * function after terminating drawing using ResetGraph (1).</p>
   */
  @Method(0x8003c350L)
  public static void GsSwapDispBuff() {
    if(PSDIDX_800c34d4.get() == 0) {
      DISPENV_800c34b0.disp.x.set(clip_800c3440.x1.get());
      DISPENV_800c34b0.disp.y.set(clip_800c3440.y1.get());
    } else {
      DISPENV_800c34b0.disp.x.set(clip_800c3440.x2.get());
      DISPENV_800c34b0.disp.y.set(clip_800c3440.y2.get());
    }

    PutDispEnv(DISPENV_800c34b0);
    SetDispMask(1);

    // GsIncFrame macro
    PSDCNT_800c34d0.addu(0x1L);
    if(PSDCNT_800c34d0.get() == 0) {
      PSDCNT_800c34d0.setu(0x1L);
    }

    //LAB_8003c3bc
    PSDIDX_800c34d4.set(PSDIDX_800c34d4.get() == 0 ? 1 : 0);
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();
  }

  @Method(0x8003c400L)
  public static void GsInitCoordinate2(@Nullable final GsCOORDINATE2 superCoord, final GsCOORDINATE2 newCoord) {
    newCoord.flg.set(0);
    newCoord.coord.set(identityMatrix_800c3568);
    newCoord.super_.setNullable(superCoord);

    if(superCoord != null) {
      superCoord.sub.set(newCoord);
    }

    //LAB_8003c468
  }

  @Method(0x8003c470L)
  public static void setRotTransMatrix(final MATRIX matrix) {
    SetRotMatrix(matrix);
    SetTransMatrix(matrix);
  }

  @Method(0x8003c4a0L)
  public static void GsSetLightMatrix(final MATRIX mp) {
    final MATRIX lightDirection = new MATRIX().set(lightDirectionMatrix_800c34e8);

    PushMatrix();
    MulMatrix(lightDirection, mp);
    PopMatrix();

    SetLightMatrix(lightDirection);
  }

  /**
   * <p>Defines the display areas used for double-buffering.</p>
   *
   * <p>x0 and y0 specify the frame buffer coordinates for buffer #0. x1 and y1 specify the frame buffer coordinates
   * for buffer #0. Normally, buffer #0 is located at (0,0) and buffer #1 is located at (0, yres), where yres is the
   * vertical resolution specified using GsInitGraph().</p>
   *
   * <p>If x0, y0 and x1, y1 are specified as the same coordinates, the double buffers are released. However,
   * double-buffer swapping of even-numbered and odd-numbered fields is performed automatically when x0,
   * y0 and x1, y1 are specified as the same coordinates in interlace mode.</p>
   *
   * <p>GsSwapDispBuffer() is used to swap double buffers. The double buffer is implemented by the GPU or GTE
   * offset. Set the libgpu or libgte offset with GsInitGraph(). When using the libgpu offset, coordinate values
   * based on the coordinate system using the upper left point in the double buffer as the origin are created in
   * the packet (add the offset at the time of drawing, not at the time of packet preparation).</p>
   */
  @Method(0x8003c540L)
  public static void GsDefDispBuff(final short x1, final short y1, final short x2, final short y2) {
    clip_800c3440.set(x1, x2, y1, y2);

    if(doubleBufferOffsetMode_800c34d6.get() == GsOffsetType.GsOFSGTE) {
      //LAB_8003c598
      clip_800c3448.set(x1, x2, y1, y2);
    } else {
      clip_800c3448.set((short)0, (short)0, (short)0, (short)0);
    }

    //LAB_8003c5b8
    GsSetDrawBuffClip();
    GsSetDrawBuffOffset();
  }

  @Method(0x8003c5e0L)
  public static void FUN_8003c5e0() {
    centreScreenX_1f8003dc.setu(displayWidth_1f8003e0.get() / 2);
    centreScreenY_1f8003de.setu(displayHeight_1f8003e4.get() / 2);
    GsSetDrawBuffOffset();
    _800c34d8.setu(0x3fff);
    lightMode_800c34dc.setu(0);
    _800c34e0.setu(10);
  }

  /**
   * TMDs can have either fixed pointers (fixp) or offset pointers. If it uses offsets, we need to add the base address to them.
   */
  @Method(0x8003c660L)
  public static void adjustTmdPointers(final Tmd tmd) {
    if((tmd.header.flags.get() & 0x1L) != 0) {
      return;
    }

    tmd.header.flags.or(0x1L);

    //LAB_8003c694
    for(int i = 0; i < tmd.header.nobj.get(); i++) {
      final TmdObjTable objTable = tmd.objTable.get(i);
      objTable.vert_top_00.add(tmd.objTable.getAddress());
      objTable.normal_top_08.add(tmd.objTable.getAddress());
      objTable.primitives_10.add(tmd.objTable.getAddress());
    }

    //LAB_8003c6c8
  }

  /**
   * Set a parallel light source.
   * <p>
   * Sets the values for one of up to three parallel light sources. Light source data is specified in the GsF_LIGHT structure.
   *
   * @param id Light source number (0, 1, 2)
   * @param light Pointer to light source data
   *
   * @return 0 on success, -1 on failure
   */
  @Method(0x8003c6f0L)
  public static long GsSetFlatLight(final long id, final GsF_LIGHT light) {
    final long x = light.direction_00.getX();
    final long y = light.direction_00.getY();
    final long z = light.direction_00.getZ();
    final long r = light.r_0c.get();
    final long g = light.g_0d.get();
    final long b = light.b_0e.get();

    final MATRIX directionMatrix = new MATRIX().set(lightDirectionMatrix_800c34e8);
    final MATRIX colourMatrix = new MATRIX();

    getLightColour(colourMatrix);

    // Normalize vector - calculate magnitude
    final long mag = SquareRoot0(x * x + y * y + z * z);

    if(mag == 0) {
      return -0x1L;
    }

    if(id == 0) {
      //LAB_8003c7ec
      directionMatrix.set(0, (short)((-light.direction_00.getX() << 12) / mag));
      directionMatrix.set(1, (short)((-light.direction_00.getY() << 12) / mag));
      directionMatrix.set(2, (short)((-light.direction_00.getZ() << 12) / mag));

      colourMatrix.set(0, (short)((r << 12) / 0xff));
      colourMatrix.set(3, (short)((g << 12) / 0xff));
      colourMatrix.set(6, (short)((b << 12) / 0xff));
    } else if(id == 0x1L) {
      //LAB_8003c904
      directionMatrix.set(3, (short)((-light.direction_00.getX() << 12) / mag));
      directionMatrix.set(4, (short)((-light.direction_00.getY() << 12) / mag));
      directionMatrix.set(5, (short)((-light.direction_00.getZ() << 12) / mag));

      colourMatrix.set(1, (short)((r << 12) / 0xff));
      colourMatrix.set(4, (short)((g << 12) / 0xff));
      colourMatrix.set(7, (short)((b << 12) / 0xff));
      //LAB_8003c7dc
    } else if(id == 0x2L) {
      //LAB_8003ca20
      directionMatrix.set(6, (short)((-light.direction_00.getX() << 12) / mag));
      directionMatrix.set(7, (short)((-light.direction_00.getY() << 12) / mag));
      directionMatrix.set(8, (short)((-light.direction_00.getZ() << 12) / mag));

      colourMatrix.set(2, (short)((r << 12) / 0xff));
      colourMatrix.set(5, (short)((g << 12) / 0xff));
      colourMatrix.set(8, (short)((b << 12) / 0xff));
    }

    //LAB_8003cb34
    lightDirectionMatrix_800c34e8.set(directionMatrix);
    setLightColour(colourMatrix);

    //LAB_8003cb88
    return 0;
  }

  @Method(0x8003cba8L)
  public static void setLightColour(final MATRIX mat) {
    lightColourMatrix_800c3508.set(mat);
    SetColorMatrix(mat);
  }

  @Method(0x8003cc0cL)
  public static void getLightColour(final MATRIX mat) {
    mat.set(lightColourMatrix_800c3508);
  }

  @Method(0x8003cc60L)
  public static void setLightMode(final long a0) {
    if(a0 < 0 || a0 > 3) {
      throw new RuntimeException("Invalid lighting mode " + a0);
    }

    lightMode_800c34dc.setu(a0);
  }

  @Method(0x8003cce0L)
  public static void GsSetAmbient(final long r, final long g, final long b) {
    SetBackColor(r >> 4, g >> 4, b >> 4);
  }

  @Method(0x8003cd10L)
  public static void drawOTag(final GsOT ot) {
    DrawOTag(ot.tag_10.deref());
  }

  /**
   * Initialize a libgs ordering table structure.
   * <p>
   * Initializes the libgs-style ordering table specified by the otp parameter. The length field of the GsOT
   * structure must be properly set before this function is called. offset specifies the Z-depth value used for the
   * start of the ordering table. point represents the average Z-depth of the entire ordering table and is used to
   * determine depth priority when linking multiple ordering tables together.
   *
   * @param offset Ordering table offset value
   * @param point Ordering table average Z value
   * @param ot Pointer to ordering table
   */
  @Method(0x8003cd40L)
  public static void GsClearOt(final long offset, final long point, final GsOT ot) {
    ot.offset_08.set(offset);
    ot.point_0c.set(point);
    ot.tag_10.set(ot.org_04.deref().get((1 << ot.length_00.get()) - 1));

    ClearOTagR(ot.org_04.deref(), 1 << ot.length_00.get());
  }

  /**
   * Initializes the libgs three-dimensional graphics system. It must be called before using other 3D functions
   * such as GsSetRefView2(), GsInitCoordinate2(), and GsSortObject3(). It does the following:
   * <ul>
   *   <li>Brings the screen point of origin to the center of the screen.</li>
   *   <li>Sets the light source to the LIGHT_NORMAL default.</li>
   * </ul>
   */
  @Method(0x8003cda0L)
  public static void GsInit3D() {
    InitGeom();
    SetFarColour(0, 0, 0);
    SetGeomOffset(0, 0);
    _800c34c4.setu(0);
    _800c34c6.setu(0);
  }

  @Method(0x8003cdf0L)
  public static TimHeader parseTimHeader(final Value baseAddress) {
    final TimHeader header = new TimHeader();
    header.flags.set(baseAddress.offset(4, 0x0L).get());

    if(baseAddress.get(0b1000L) == 0) { // No CLUT
      //LAB_8003ce94
      final RECT imageRect = new RECT();
      imageRect.x.set((short)baseAddress.offset(2, 0x8L).get()); // Image X
      imageRect.y.set((short)baseAddress.offset(2, 0xaL).get()); // Image Y

      imageRect.w.set((short)baseAddress.offset(2, 0xcL).get()); // Image W
      imageRect.h.set((short)baseAddress.offset(2, 0xeL).get()); // Image H

      header.setImage(imageRect, baseAddress.offset(0x10L).getAddress()); // Pointer to image data
    } else { // Has CLUT
      final RECT clutRect = new RECT();
      clutRect.x.set((short)baseAddress.offset(2, 0x8L).get()); // CLUT X
      clutRect.y.set((short)baseAddress.offset(2, 0xaL).get()); // CLUT Y

      clutRect.w.set((short)baseAddress.offset(2, 0xcL).get()); // CLUT W
      clutRect.h.set((short)baseAddress.offset(2, 0xeL).get()); // CLUT H

      header.setClut(clutRect, baseAddress.offset(0x10L).getAddress()); // Pointer to CLUT table

      final Value imageData = baseAddress.offset(0x4L).offset(baseAddress.offset(2, 0x4L)); // Address to image data block

      final RECT imageRect = new RECT();
      imageRect.x.set((short)imageData.offset(2, 0x4L).get()); // Image X
      imageRect.y.set((short)imageData.offset(2, 0x6L).get()); // Image Y

      imageRect.w.set((short)imageData.offset(2, 0x8L).get()); // Image W
      imageRect.h.set((short)imageData.offset(2, 0xaL).get()); // Image H

      header.setImage(imageRect, imageData.offset(0xcL).getAddress()); // Pointer to image data
    }

    //LAB_8003cecc
    return header;
  }

  @Method(0x8003cee0L)
  public static void FUN_8003cee0(final MATRIX matrix, final long sin, final long cos, final long param_4) {
    matrix.set(identityMatrix_800c3568);

    //TODO this is weird... these are the positions for rotation matrices, but if they were transposed

    switch((byte)(param_4 - 0x58L)) {
      case 0x0, 0x20 -> {
        matrix.set(1, 1, (short)cos);
        matrix.set(1, 2, (short)-sin);
        matrix.set(2, 1, (short)sin);
        matrix.set(2, 2, (short)cos);
      }

      case 0x1, 0x21 -> {
        matrix.set(0, 0, (short)cos);
        matrix.set(0, 2, (short)sin);
        matrix.set(2, 0, (short)-sin);
        matrix.set(2, 2, (short)cos);
      }

      case 0x2, 0x22 -> {
        matrix.set(0, 0, (short)cos);
        matrix.set(0, 1, (short)-sin);
        matrix.set(1, 0, (short)sin);
        matrix.set(1, 1, (short)cos);
      }
    }
  }

  /**
   * <p>Calculates GsWSMATRIX using viewpoint information in pv. GsWSMATRIX doesn't change unless the
   * viewpoint is moved, so this function should be called every frame only if the viewpoint is moved, in order for
   * changes to be updated.</p>
   *
   * <p>It should also be called every frame if the GsRVIEW2 member super is set to anything other than WORLD,
   * because even if the other parameters are not changed, if the parameters of the superior coordinate system
   * are changed, the viewpoint will have moved.</p>
   *
   * @param struct Pointer to view information
   *
   * @return 0 on success; 2 on failure.
   */
  @Method(0x8003cfb0L)
  public static long GsSetRefView2(final GsRVIEW2 struct) {
    long s0;
    long s1;
    long s2;
    long a1;
    long a2;
    long v0;

    matrix_800c3548.set(matrix_800c3588);

    FUN_8003d5d0(matrix_800c3548, -struct.viewpointTwist_18.get());
    final int[] sp10 = new int[6];
    FUN_8003d380(struct, sp10);

    long a0;

    v0 = sp10[3] - sp10[0];
    a0 = v0 * v0;

    v0 = sp10[4] - sp10[1];
    a0 += v0 * v0;

    v0 = sp10[5] - sp10[2];
    a0 += v0 * v0;

    s2 = SquareRoot0(a0);
    if(s2 == 0) {
      return 0x1L;
    }

    s0 = (sp10[1] - sp10[4]) << 12;

    //LAB_8003d0dc
    v0 = sp10[3] - sp10[0];
    a0 = v0 * v0;

    v0 = sp10[5] - sp10[2];
    a0 += v0 * v0;

    s1 = SquareRoot0(a0);
    a2 = s1 << 12;

    //LAB_8003d134
    final MATRIX sp30 = new MATRIX();
    FUN_8003cee0(sp30, -(short)(s0 / s2), (short)(a2 / s2), 0x78L);
    MulMatrix(matrix_800c3548, sp30);

    if(s1 != 0) {
      a1 = (sp10[3] - sp10[0]) << 12;

      //LAB_8003d1c0
      a2 = (sp10[5] - sp10[2]) << 12;

      //LAB_8003d200
      FUN_8003cee0(sp30, -(short)(a1 / s1), (short)(a2 / s1), 0x79L);
      MulMatrix(matrix_800c3548, sp30);
    }

    //LAB_8003d230
    matrix_800c3548.transfer.set(ApplyMatrixLV(matrix_800c3548, new VECTOR().set(struct.viewpoint_00).negate()));

    if(!struct.super_1c.isNull()) {
      final MATRIX lw = new MATRIX();
      GsGetLw(struct.super_1c.deref(), lw);

      final MATRIX transposedLw = new MATRIX();
      TransposeMatrix(lw, transposedLw);
      transposedLw.transfer.set(ApplyMatrixLV(transposedLw, lw.transfer)).negate();
      GsMulCoord2(matrix_800c3548, transposedLw);
      matrix_800c3548.set(transposedLw);
    }

    //LAB_8003d310
    matrix_800c3528.set(matrix_800c3548);

    //LAB_8003d35c
    return 0;
  }

  @Method(0x8003d380L)
  public static void FUN_8003d380(final GsRVIEW2 a0, final int[] a1) {
    int longestComponentBits = log2(getLongestComponent(a0));

    if(longestComponentBits < 16) {
      a1[0] = a0.viewpoint_00.getX();
      a1[1] = a0.viewpoint_00.getY();
      a1[2] = a0.viewpoint_00.getZ();
      a1[3] = a0.refpoint_0c.getX();
      a1[4] = a0.refpoint_0c.getY();
      a1[5] = a0.refpoint_0c.getZ();
    } else {
      longestComponentBits -= 15;

      a1[0] = a0.viewpoint_00.getX() >> longestComponentBits;
      a1[1] = a0.viewpoint_00.getY() >> longestComponentBits;
      a1[2] = a0.viewpoint_00.getZ() >> longestComponentBits;
      a1[3] = a0.refpoint_0c.getX() >> longestComponentBits;
      a1[4] = a0.refpoint_0c.getY() >> longestComponentBits;
      a1[5] = a0.refpoint_0c.getZ() >> longestComponentBits;
    }
  }

  @Method(0x8003d46cL)
  public static long getLongestComponent(final GsRVIEW2 a0) {
    long largest = Math.abs(a0.viewpoint_00.getX());
    long other = Math.abs(a0.viewpoint_00.getY());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.viewpoint_00.getZ());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.refpoint_0c.getX());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.refpoint_0c.getY());
    if(largest < other) {
      largest = other;
    }

    other = Math.abs(a0.refpoint_0c.getZ());
    if(largest < other) {
      largest = other;
    }

    return largest;
  }

  @Method(0x8003d534L)
  public static int log2(final long a0) {
    if(a0 == 0) {
      return 0;
    }

    return 64 - Long.numberOfLeadingZeros(a0);
  }

  /**
   * GsMulCoord2 multiplies the MATRIX m2 by the translation matrix m1and stores the result in m2.
   * <p>
   * m2 = m1 x m2
   */
  @Method(0x8003d550L)
  public static void GsMulCoord2(final MATRIX matrix1, final MATRIX matrix2) {
    final VECTOR out = ApplyMatrixLV(matrix1, matrix2.transfer);
    MulMatrix2(matrix1, matrix2);
    matrix2.transfer.set(matrix1.transfer).add(out);
  }

  @Method(0x8003d5d0L)
  public static void FUN_8003d5d0(final MATRIX matrix, final long angle) {
    if(angle != 0) {
      final short cos = rcos(angle / 360);
      final short sin = rsin(angle / 360);

      final MATRIX rot = new MATRIX();
      rot.set(0, 0, cos);
      rot.set(0, 1, (short)-sin);
      rot.set(0, 2, (short)0);
      rot.set(1, 0, sin);
      rot.set(1, 1, cos);
      rot.set(1, 2, (short)0);
      rot.set(2, 0, (short)0);
      rot.set(2, 1, (short)0);
      rot.set(2, 2, (short)0x1000);
      MulMatrix(matrix, rot);
    }
  }

  /**
   * Calculates a local world perspective transformation matrix from the GsCOORDINATE2 structure pointed to
   * by the coord argument and stores the result in the MATRIX structure pointed to by the m argument.
   * <p>
   * For high speed operation, the function retains the result of calculation at each node of the hierarchical
   * coordinate system. When the next GsGetLw() function is called, calculation up to the node to which no
   * changes have been made is omitted. This is controlled by a GsCOORDINATE2 member flag (libgs replaces
   * 1 in flags already calculated by GsCOORDINATE2).
   * <p>
   * If the contents of a superior node are changed, the effect on a subordinate node is handled by libgs, so it is
   * not necessary to clear the flags of all subordinate nodes of the changed superior node.
   *
   * @param coord Pointer to local coordinate system
   * @param matrix Pointer to matrix
   */
  @Method(0x8003d690L)
  public static void GsGetLw(final GsCOORDINATE2 coord, final MATRIX matrix) {
    GsCOORDINATE2 a3 = coord;
    int s1 = 0;
    int a1_0 = 100;

    //LAB_8003d6c0
    do {
      coord2s_800c35a8.get(s1).set(a3);

      if(a3.super_.isNull()) {
        if(a3.flg.get() == 0 || a3.flg.get() == PSDCNT_800c34d0.get()) {
          //LAB_8003d6fc
          a3.workm.set(a3.coord);
          matrix.set(a3.workm);
          a3.flg.set(PSDCNT_800c34d0.get());
          break;
        }

        //LAB_8003d78c
        if(a1_0 == 100) {
          matrix.set(coord2s_800c35a8.get(0).deref().workm);
          s1 = 0;
          break;
        }

        //LAB_8003d7e8
        s1 = a1_0 + 1;
        matrix.set(coord2s_800c35a8.get(s1).deref().workm);
        break;
      }

      //LAB_8003d83c
      if(a3.flg.get() == PSDCNT_800c34d0.get()) {
        matrix.set(a3.workm);
        break;
      }

      //LAB_8003d898
      if(a3.flg.get() == 0) {
        a1_0 = s1;
      }

      //LAB_8003d8a4
      a3 = a3.super_.deref();
      s1++;
    } while(true);

    //LAB_8003d8ac
    //LAB_8003d8c0
    while(s1 > 0) {
      s1--;
      final GsCOORDINATE2 coord2 = coord2s_800c35a8.get(s1).deref();
      GsMulCoord3(matrix, coord2.coord);
      coord2.workm.set(matrix);
      coord2.flg.set(PSDCNT_800c34d0.get());
    }

    //LAB_8003d930
  }

  /**
   * GsMulCoord3 multiplies the MATRIX m2 by the translation matrix m1and stores the result in m2.
   * <p>
   * m1 = m1 x m2
   */
  @Method(0x8003d950L)
  public static void GsMulCoord3(final MATRIX m1, final MATRIX m2) {
    final VECTOR out = ApplyMatrixLV(m1, m2.transfer);
    MulMatrix(m1, m2);
    m1.transfer.add(out);
  }

  /**
   * Calculates a local screen perspective transformation matrix from the GsCOORDINATE2 structure pointed
   * to by the coord argument and stores the result in the MATRIX structure pointed to by the m argument.
   * <p>
   * For high speed operation, the function retains the result of calculation at each node of the hierarchical
   * coordinate system. When the next GsGetLs() function is called, calculation up to the node to which no
   * changes have been made is omitted. This is controlled by a GsCOORDINATE2 member flag (libgs replaces
   * 1 in flags already calculated by GsCOORDINATE2).
   * <p>
   * If the contents of a superior node are changed, the effect on a subordinate node is handled by libgs, so it is
   * not necessary to clear the flags of all subordinate nodes of the changed superior node.
   *
   * @param coord Pointer to local coordinate system
   * @param matrix Pointer to matrix
   */
  @Method(0x8003d9d0L)
  public static void GsGetLs(final GsCOORDINATE2 coord, final MATRIX matrix) {
    GsCOORDINATE2 a3 = coord;
    int s1 = 0;
    int a1 = 100;

    //LAB_8003da00
    do {
      coord2s_800c35a8.get(s1).set(a3);
      if(a3.super_.isNull()) {
        if(a3.flg.get() == 0 || a3.flg.get() == PSDCNT_800c34d0.get()) {
          //LAB_8003da3c
          a3.workm.set(a3.coord);
          matrix.set(a3.workm);
          a3.flg.set(PSDCNT_800c34d0.get());
          break;
        }

        //LAB_8003dacc
        s1 = a1 + 1;

        if(a1 == 100) {
          matrix.set(coord2s_800c35a8.get(0).deref().workm);
          s1 = 0;
          break;
        }

        //LAB_8003db28
        matrix.set(coord2s_800c35a8.get(s1).deref().workm);
        break;
      }

      //LAB_8003db7c
      if(a3.flg.get() == PSDCNT_800c34d0.get()) {
        matrix.set(a3.workm);
        break;
      }

      //LAB_8003dbd8
      if(a3.flg.get() == 0) {
        a1 = s1;
      }

      //LAB_8003dbe4
      a3 = a3.super_.deref();
      s1++;
    } while(true);

    //LAB_8003dbec
    //LAB_8003dc00
    while(s1 > 0) {
      s1--;
      final GsCOORDINATE2 coord2 = coord2s_800c35a8.get(s1).deref();
      GsMulCoord3(matrix, coord2.coord);
      coord2.workm.set(matrix);
      coord2.flg.set(PSDCNT_800c34d0.get());
    }

    //LAB_8003dc70
    GsMulCoord2(matrix_800c3548, matrix);
  }

  /**
   * Calculate local world and local screen matrices.
   * <p>
   * GsGetLws() calculates local world and local screen coordinates. It is faster than calling GsGetLw() followed
   * by calling GsGetLs(). When you use GsSetLightMatrix(), you pass it the lw matrix.
   *
   * @param coord Pointer to local coordinates
   * @param lw Pointer to matrix that stores the local world coordinates
   * @param ls Pointer to matrix that stores the local screen coordinates
   */
  @Method(0x8003dca0L)
  public static void GsGetLws(GsCOORDINATE2 coord, final MATRIX lw, final MATRIX ls) {
    int s1 = 0;
    int a = 100;

    //LAB_8003dcd8
    do {
      coord2s_800c35a8.get(s1).set(coord);

      if(coord.super_.isNull()) {
        if(coord.flg.get() == PSDCNT_800c34d0.get() || coord.flg.get() == 0) {
          //LAB_8003dd14
          coord.flg.set((int)PSDCNT_800c34d0.get());
          coord.workm.set(coord.coord);
          lw.set(coord.workm);
          break;
        }

        //LAB_8003dda4
        s1 = a + 1;
        if(a == 100) {
          lw.set(coord2s_800c35a8.get(0).deref().workm);
          s1 = 0;
          break;
        }

        //LAB_8003de00
        lw.set(coord2s_800c35a8.get(s1).deref().workm);
        break;
      }

      //LAB_8003de54
      if(coord.flg.get() == PSDCNT_800c34d0.get()) {
        lw.set(coord.workm);
        break;
      }

      //LAB_8003deb0
      if(coord.flg.get() == 0) {
        a = s1;
      }

      //LAB_8003debc
      coord = coord.super_.deref();
      s1++;
    } while(true);

    //LAB_8003dec4
    //LAB_8003ded8
    while(s1 > 0) {
      s1--;
      final GsCOORDINATE2 c = coord2s_800c35a8.get(s1).deref();

      GsMulCoord3(lw, c.coord);

      c.flg.set((int)PSDCNT_800c34d0.get());
      c.workm.set(lw);
    }

    //LAB_8003df48
    ls.set(lw);
    GsMulCoord2(matrix_800c3548, ls);
  }

  @Method(0x8003dfc0L)
  public static long FUN_8003dfc0(final RenderStruct20 s2) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a3;
    long s0;
    long s1;
    long s3;

    matrix_800c3548.set(matrix_800c3588);
    FUN_8003d5d0(matrix_800c3548, -s2._18.get());

    v0 = s2._0c.get() - s2._00.get();
    a3 = v0 * v0;

    v0 = s2._10.get() - s2._04.get();
    a0 = v0 * v0;

    v0 = s2._14.get() - s2._08.get();
    v1 = v0 * v0;

    s0 = a3 + a0 + v1;
    if(s0 == 0) {
      return 0x1L;
    }

    v0 = s2._04.get() - s2._10.get();
    s1 = v0 * v0;
    a1 = 0xcL - Lzc(s1);
    if((int)a1 < 0) {
      //LAB_8003e0e4
      //LAB_8003e0fc
      s3 = (s2._04.get() - s2._10.get()) * -0x1000L / SquareRoot0(s0);
      //LAB_8003e108
    } else if(s2._04.get() - s2._10.get() >= 0) {
      v0 = s0 >>> a1;
      a0 = 0xcL - a1;
      a0 = s1 << a0;

      //LAB_8003e138
      s3 = -FUN_8003e8b4(a0 / v0);
    } else {
      v0 = s0 >>> a1;

      //LAB_8003e14c
      a0 = 0xcL - a1;
      a0 = s1 << a0;

      //LAB_8003e164
      s3 = FUN_8003e8b4(a0 / v0);
    }

    //LAB_8003e174
    v0 = s2._0c.get() - s2._00.get();
    a0 = v0 * v0;

    v0 = s2._14.get() - s2._08.get();
    v1 = v0 * v0;

    s1 = a0 + v1;
    a1 = 0xcL - Lzc(s1);

    if((int)a1 < 0) {
      //LAB_8003e1e8
      //LAB_8003e200
      v0 = SquareRoot0(s1) * 0x1000L / SquareRoot0(s0);
    } else {
      a0 = 0xcL - a1;

      //LAB_8003e20c
      a0 = s1 << a0;
      v0 = s0 >>> a1;

      //LAB_8003e224
      v0 = FUN_8003e8b4(a0 / v0);
    }

    //LAB_8003e230
    final MATRIX sp0x30 = new MATRIX();
    FUN_8003cee0(sp0x30, (short)s3, (short)v0, 0x78L);
    MulMatrix(matrix_800c3548, sp0x30);

    if(s1 != 0) {
      s0 = s1;
      v0 = s2._0c.get() - s2._00.get();
      s1 = v0 * v0;
      a1 = 0xcL - Lzc(s1);
      if((int)a1 < 0) {
        //LAB_8003e2c8
        //LAB_8003e2e0
        s3 = (s2._0c.get() - s2._00.get()) * -0x1000L / SquareRoot0(s0);
        //LAB_8003e2ec
      } else if(s2._0c.get() - s2._00.get() >= 0) {
        v0 = s0 >>> a1;
        a0 = 0xcL - a1;
        a0 = s1 << a0;

        //LAB_8003e31c
        s3 = -FUN_8003e8b4(a0 / v0);
      } else {
        v0 = s0 >>> a1;

        //LAB_8003e330
        a0 = 0xcL - a1;
        a0 = s1 << a0;

        //LAB_8003e348
        s3 = FUN_8003e8b4(a0 / v0);
      }

      //LAB_8003e358
      v0 = s2._14.get() - s2._08.get();
      s1 = v0 * v0;
      a1 = 0xcL - Lzc(s1);
      if((int)a1 < 0) {
        //LAB_8003e3b4
        //LAB_8003e3cc
        v0 = (s2._14.get() - s2._08.get()) * 0x1000L / SquareRoot0(s0);
        //LAB_8003e3d8
      } else if(s2._14.get() - s2._08.get() >= 0) {
        v0 = s0 >>> a1;
        a0 = 0xcL - a1;
        a0 = s1 << a0;

        //LAB_8003e408
        v0 = FUN_8003e8b4(a0 / v0);
      } else {
        v0 = s0 >>> a1;
        //LAB_8003e41c
        a0 = 0xcL - a1;
        a0 = s1 << a0;

        //LAB_8003e434
        v0 = -FUN_8003e8b4(a0 / v0);
      }

      //LAB_8003e444
      //LAB_8003e448
      FUN_8003cee0(sp0x30, (short)s3, (short)v0, 0x79L);
      MulMatrix(matrix_800c3548, sp0x30);
    }

    //LAB_8003e474
    final VECTOR sp0x90 = new VECTOR();
    sp0x90.setX(-s2._00.get());
    sp0x90.setY(-s2._04.get());
    sp0x90.setZ(-s2._08.get());
    matrix_800c3548.transfer.set(ApplyMatrixLV(matrix_800c3548, sp0x90));

    if(!s2._1c.isNull()) {
      GsGetLw(s2._1c.deref(), sp0x30);

      final MATRIX sp0x50 = new MATRIX();
      TransposeMatrix(sp0x30, sp0x50);
      sp0x90.set(ApplyMatrixLV(sp0x50, sp0x30.transfer));
      sp0x50.transfer.setX(-sp0x90.getX());
      sp0x50.transfer.setY(-sp0x90.getY());
      sp0x50.transfer.setZ(-sp0x90.getZ());
      GsMulCoord2(matrix_800c3548, sp0x50);
      matrix_800c3548.set(sp0x50);
    }

    //LAB_8003e55c
    matrix_800c3528.set(matrix_800c3548);

    //LAB_8003e5a8
    return 0;
  }

  /**
   * I think this method reads through all the packets and sort of "combines" ones that have the same MODE and FLAG for efficiency
   */
  @Method(0x8003e5d0L)
  public static void updateTmdPacketIlen(UnboundedArrayRef<TmdObjTable> objTables, final GsDOBJ2 dobj2, final int objIndex) {
    long bytesSinceModeOrFlagChange = 0;
    long mode = 0;
    long flag = 0;

    final TmdObjTable objTable = objTables.get(objIndex);
    dobj2.tmd_08.set(objTable);

    int packetIndex = 0;
    int packetStartIndex = 0;

    //LAB_8003e638
    for(int primitiveIndex = 0; primitiveIndex < objTable.n_primitive_14.get(); primitiveIndex++) {
      long previousMode = mode;
      long previousFlag = flag;

      // Primitive: mode, flag, ilen, olen
      final long primitive = objTable.primitives_10.deref().get(packetIndex / 4).get();

      mode = primitive >>> 24 & 0xffL;
      flag = primitive >>> 16 & 0xffL;

      if(previousMode != 0) {
        if(mode != previousMode || flag != previousFlag) {
          //LAB_8003e668
          objTable.primitives_10.deref().get(packetStartIndex / 4).and(0xffff_0000L).or(bytesSinceModeOrFlagChange);
          bytesSinceModeOrFlagChange = 0;
          packetStartIndex = packetIndex;
        }
      }

      //LAB_8003e674
      //LAB_8003e678
      switch((int)(mode & 0xfdL)) {
        case 0x20: // setPolyF3
          if((flag & 0x4L) == 0) {
            packetIndex += 0x10L;
            break;
          }

        case 0x31:
        case 0x24: // setPolyFT3
          packetIndex += 0x18L;
          break;

        case 0x30: // setPolyG3
          if((flag & 0x4L) == 0) {
            packetIndex += 0x14L;
            break;
          }

        case 0x34: // setPolyGT3
        case 0x39:
        case 0x25:
          packetIndex += 0x1cL;
          break;

        case 0x28: // setPolyF4
          if((flag & 0x4L) == 0) {
            packetIndex += 0x14L;
            break;
          }

        case 0x2d:
        case 0x2c: // setPolyFT4
          packetIndex += 0x20L;
          break;

        case 0x29:
        case 0x21:
          packetIndex += 0x10L;
          break;

        case 0x3d:
          packetIndex += 0x2c;
          break;

        case 0x38: // setPolyG4
          if((flag & 0x4L) == 0) {
            packetIndex += 0x18L;
            break;
          }

        case 0x3c: // setPolyGT4
        case 0x35:
          packetIndex += 0x24L;
          break;

        case 0x23:
        case 0x26:
        case 0x27:
        case 0x2a:
        case 0x2b:
        case 0x2e:
        case 0x2f:
        case 0x32:
        case 0x33:
        case 0x36:
        case 0x37:
        case 0x3a:
        case 0x3b:
        case 0x22:
          LOGGER.error("GPU CODE %02xH not assigned.", mode);
          break;
      }

      //LAB_8003e714
      bytesSinceModeOrFlagChange++;
    }

    //LAB_8003e724
    objTable.primitives_10.deref().get(packetStartIndex / 4).and(0xffff_0000L).or(bytesSinceModeOrFlagChange);
  }

  @Method(0x8003e760L) //TODO using div instead of shifting means some of these values are slightly off, does this matter?
  public static long FUN_8003e760(long a0) {
    final long[] sp0x04 = new long[7];
    final long[] sp0x24 = new long[7];
    sp0x04[0] = a0 + 0x5d_50adL;
    sp0x24[0] = a0 - 0x5d_50adL;

    //LAB_8003e790
    for(int a3 = 1; a3 < 7; a3++) {
      if(a3 != 4) {
        final long v0 = sp0x04[a3 - 1] >> a3;

        if((int)sp0x24[a3 - 1] < 0) {
          //LAB_8003e7d0
          sp0x04[a3] = sp0x04[a3 - 1] + (sp0x24[a3 - 1] >> a3);
          sp0x24[a3] = sp0x24[a3 - 1] + v0;
        } else {
          sp0x04[a3] = sp0x04[a3 - 1] - (sp0x24[a3 - 1] >> a3);
          sp0x24[a3] = sp0x24[a3 - 1] - v0;
        }
      } else {
        //LAB_8003e7fc
        //LAB_8003e87c
        if((int)sp0x24[3] >= 0) {
          sp0x24[3] -= (int)sp0x04[3] >> 4;
          sp0x04[3] -= (int)sp0x24[3] >> 4;
        } else {
          //LAB_8003e844
          sp0x24[3] += (int)sp0x04[3] >> 4;
          sp0x04[3] += (int)sp0x24[3] >> 4;
        }

        if((int)sp0x24[3] < 0) {
          //LAB_8003e87c
          sp0x04[4] = sp0x04[3] + ((int)sp0x24[3] >> 4);
          sp0x24[4] = sp0x24[3] + ((int)sp0x04[3] >> 4);
        } else {
          sp0x04[4] = sp0x04[3] - ((int)sp0x24[3] >> 4);
          sp0x24[4] = sp0x24[3] - ((int)sp0x04[3] >> 4);
        }

        //LAB_8003e890
      }

      //LAB_8003e894
    }

    return sp0x04[6];
  }

  @Method(0x8003e8b4L)
  public static long FUN_8003e8b4(long a0) {
    long v0;
    long s0;

    if(a0 == 0) {
      return 0;
    }

    //LAB_8003e8d4
    v0 = 0x8 - Lzc(a0);
    if((int)v0 >= 0) {
      s0 = (int)v0 >> 1;
      v0 = s0 << 1;
      a0 = (int)a0 >> v0;
    } else {
      //LAB_8003e8f8
      v0 = (int)v0 >> 1;
      s0 = v0 + 0x1L;
      v0 = s0 << 1;
      v0 = -v0;
      a0 = a0 << v0;
    }

    //LAB_8003e90c
    s0 = s0 - 0x6L;
    if((int)s0 < 0) {
      v0 = FUN_8003e760(a0) >> -s0;
    } else {
      //LAB_8003e92c
      v0 = FUN_8003e760(a0) << s0;
    }

    //LAB_8003e938
    return v0;
  }

  @Method(0x8003e958L)
  public static void InitGeom() {
    patchC0TableAgain();

    // Set bit 30 of status register to enable GTE
    CPU.MTC0(CpuRegisterType.SR, CPU.MFC0(CpuRegisterType.SR) | 0x40000000);

    // cop2r61 = 0x155, 61 = ZSF3 - Z3 avg scale factor (normally 1/3)
    CPU.CTC2(0x155, 29);
    // cop2r62 = 0x100, 62 = ZSF4 - Z4 avg scale factor (normally 1/4)
    CPU.CTC2(0x100, 30);
    // cop2r58 = 0x3e8, 58 = H - Projection plane distance
    CPU.CTC2(1000, 26);
    // cop2r59 = 0xffffef9e, 59 = DQA - Depth queueing param A (coefficient)
    CPU.CTC2(0xffffef9e, 27);
    // cop2r60 = 0x1400000, 60 = DQB - Depth queueing param B (offset)
    CPU.CTC2(0x1400000, 28);
    // cop2r56 = 0, 56 = OFX - Screen offset X
    CPU.CTC2(0, 24);
    // cop2r57 = 0, 57 = OFY - Screen offset Y
    CPU.CTC2(0, 25);
  }

  @Method(0x8003eae0L)
  public static void FUN_8003eae0(final Ref<Long> t0, final Ref<Long> t1, final Ref<Long> t2) {
    long v0;
    long v1;
    long t3;
    long t4;
    long t5;
    long t6;
    CPU.MTC2(t0.get(),  9);
    CPU.MTC2(t1.get(), 10);
    CPU.MTC2(t2.get(), 11);
    CPU.COP2(0xa00428L);
    v0 = CPU.MFC2(25) + CPU.MFC2(26) + CPU.MFC2(27);
    CPU.MTC2(v0, 30);
    v1 = CPU.MFC2(31) & 0xffff_fffeL;
    t6 = 31 - v1;
    t6 = (int)t6 >> 1;
    t3 = v1 - 24;
    if(t3 >= 0) {
      t4 = v0 << t3;
    } else {
      //LAB_8003eb40
      t3 = 24 - v1;
      t4 = (int)v0 >> t3;
    }

    //LAB_8003eb4c
    t4 = t4 - 0x80L;
    t4 = t4 << 1;
    t5 = 0x8005_0000L;
    t5 = t5 + t4;
    t5 = MEMORY.ref(2, t5).offset(0x4870L).getSigned();

    CPU.MTC2(t5, 8);
    CPU.MTC2(t0.get(), 9);
    CPU.MTC2(t1.get(), 10);
    CPU.MTC2(t2.get(), 11);
    CPU.COP2(0x190003dL);
    t0.set(CPU.MFC2(25) >> t6);
    t1.set(CPU.MFC2(26) >> t6);
    t2.set(CPU.MFC2(27) >> t6);
  }

  @Method(0x8003eba0L)
  public static void FUN_8003eba0(final MATRIX a0, final MATRIX a1) {
    long v0;
    long v1;
    long a2;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t7;
    long t8;
    long t9;
    t0 = a0.get(0);
    t1 = a0.get(1);
    t2 = a0.get(2);
    t3 = a0.get(3);
    t4 = a0.get(4);
    t5 = a0.get(5);
    v0 = CPU.CFC2(0);
    v1 = CPU.CFC2(2);
    a2 = CPU.CFC2(4);
    CPU.CTC2(t0, 0);
    CPU.CTC2(t1, 2);
    CPU.CTC2(t2, 4);
    CPU.MTC2(t3, 9);
    CPU.MTC2(t4, 10);
    CPU.MTC2(t5, 11);
    CPU.COP2(0x178000cL);
    t7 = CPU.MFC2(25);
    t8 = CPU.MFC2(26);
    t9 = CPU.MFC2(27);
    CPU.CTC2(t3, 0);
    CPU.CTC2(t4, 2);
    CPU.CTC2(t5, 4);
    CPU.COP2(0x178000cL);
    CPU.MTC2(t3, 0);
    CPU.MTC2(t4, 1);
    CPU.MTC2(t5, 2);
    final Ref<Long> t0Ref = new Ref<>(CPU.MFC2(25));
    final Ref<Long> t1Ref = new Ref<>(CPU.MFC2(26));
    final Ref<Long> t2Ref = new Ref<>(CPU.MFC2(27));
    CPU.CTC2(v0, 0);
    CPU.CTC2(v1, 2);
    CPU.CTC2(a2, 4);
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(0, t0Ref.get().shortValue());
    a1.set(1, t1Ref.get().shortValue());
    a1.set(2, t2Ref.get().shortValue());
    t0Ref.set(CPU.MFC2(0));
    t1Ref.set(CPU.MFC2(1));
    t2Ref.set(CPU.MFC2(2));
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(3, t0Ref.get().shortValue());
    a1.set(4, t1Ref.get().shortValue());
    a1.set(5, t2Ref.get().shortValue());
    t0Ref.set(t7);
    t1Ref.set(t8);
    t2Ref.set(t9);
    FUN_8003eae0(t0Ref, t1Ref, t2Ref);
    a1.set(6, t0Ref.get().shortValue());
    a1.set(7, t1Ref.get().shortValue());
    a1.set(8, t2Ref.get().shortValue());
  }

  /**
   * NOTE: a2 moved to return
   */
  @Method(0x8003edf0L)
  public static VECTOR ApplyMatrixLV(final MATRIX matrix, final VECTOR vector) {
    CPU.CTC2(matrix.getPacked(0), 0); //
    CPU.CTC2(matrix.getPacked(2), 1); //
    CPU.CTC2(matrix.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(matrix.getPacked(6), 3); //
    CPU.CTC2(matrix.getPacked(8), 4); //

    long t0 = vector.x.get();
    long t1 = vector.y.get();
    long t2 = vector.z.get();
    long t3;
    if(t0 < 0) {
      t0 = -t0;
      t3 = t0 >> 0xfL;
      t3 = -t3;
      t0 &= 0x7fffL;
      t0 = -t0;
    } else {
      //LAB_8003ee44
      t3 = t0 >> 0xfL;
      t0 &= 0x7fffL;
    }

    //LAB_8003ee4c
    long t4;
    if(t1 < 0) {
      t1 = -t1;
      t4 = t1 >> 0xfL;
      t4 = -t4;
      t1 &= 0x7fffL;
      t1 = -t1;
    } else {
      //LAB_8003ee6c
      t4 = t1 >> 0xfL;
      t1 &= 0x7fffL;
    }

    //LAB_8003ee74
    long t5;
    if(t2 < 0) {
      t2 = -t2;
      t5 = t2 >> 0xfL;
      t5 = -t5;
      t2 &= 0x7fffL;
      t2 = -t2;
    } else {
      //LAB_8003ee94
      t5 = t2 >> 0xfL;
      t2 &= 0x7fffL;
    }

    //LAB_8003ee9c
    CPU.MTC2(t3,  9); // IR1
    CPU.MTC2(t4, 10); // IR2
    CPU.MTC2(t5, 11); // IR3
    CPU.COP2(0x41_e012L); // Multiply vector by matrix and add vector (no translation vector, multiply by IR, rotation, no fraction)
    t3 = CPU.MFC2(25); // MAC1
    t4 = CPU.MFC2(26); // MAC2
    t5 = CPU.MFC2(27); // MAC3

    CPU.MTC2(t0,  9); // IR1
    CPU.MTC2(t1, 10); // IR2
    CPU.MTC2(t2, 11); // IR3
    CPU.COP2(0x49_e012L); // Multiply vector by matrix and add vector (no translation vector, multiply by IR, 12-bit fraction)

    //LAB_8003ef24
    t0 = CPU.MFC2(25); // MAC1
    t1 = CPU.MFC2(26); // MAC2
    t2 = CPU.MFC2(27); // MAC3

    final VECTOR out = new VECTOR();
    out.x.set((int)(t0 + t3 * 8));
    out.y.set((int)(t1 + t4 * 8));
    out.z.set((int)(t2 + t5 * 8));

    return out;
  }

  @Method(0x8003ef50L)
  public static VECTOR FUN_8003ef50(final SVECTOR vec, final VECTOR out) {
    CPU.MTC2(vec.getXY(), 0); // VXY0
    CPU.MTC2(vec.getZ(),  1); // VZ0
    CPU.COP2(0x48_6012L);
    out.setX((int)CPU.MFC2( 9));
    out.setY((int)CPU.MFC2(10));
    out.setZ((int)CPU.MFC2(11));
    return out;
  }

  @Method(0x8003ef80L)
  public static void PushMatrix() {
    final int i = (int)matrixStackIndex_80054a08.get();

    if(i >= 640) {
      throw new RuntimeException("Error: Can't push matrix, stack(max 20) is full!");
    }

    //LAB_8003efc0
    final MATRIX matrix = matrixStack_80054a0c.get(i / 32);
    matrix.setPacked(0, CPU.CFC2(0)); //
    matrix.setPacked(2, CPU.CFC2(1)); //
    matrix.setPacked(4, CPU.CFC2(2)); // Rotation matrix
    matrix.setPacked(6, CPU.CFC2(3)); //
    matrix.setPacked(8, CPU.CFC2(4)); //
    matrix.transfer.x.set((int)CPU.CFC2(5)); //
    matrix.transfer.y.set((int)CPU.CFC2(6)); // Translation vector
    matrix.transfer.z.set((int)CPU.CFC2(7)); //

    matrixStackIndex_80054a08.addu(0x20L);
  }

  @Method(0x8003f024L)
  public static void PopMatrix() {
    int i = (int)matrixStackIndex_80054a08.get();

    if(i == 0) {
      throw new RuntimeException("Error: Can't pop matrix, stack is empty!");
    }

    //LAB_8003f060
    i -= 0x20L;
    matrixStackIndex_80054a08.subu(0x20L);

    final MATRIX matrix = matrixStack_80054a0c.get(i / 32);
    CPU.CTC2(matrix.getPacked(0), 0); //
    CPU.CTC2(matrix.getPacked(2), 1); //
    CPU.CTC2(matrix.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(matrix.getPacked(6), 3); //
    CPU.CTC2(matrix.getPacked(8), 4); //

    CPU.CTC2(matrix.transfer.x.get(), 5); //
    CPU.CTC2(matrix.transfer.y.get(), 6); // Translation vector
    CPU.CTC2(matrix.transfer.z.get(), 7); //
  }

  @Method(0x8003f0d0L)
  public static MATRIX ScaleMatrix(final MATRIX mat, final VECTOR vector) {
    mat.set(0, (short)(mat.get(0) * vector.x.get() >> 12));
    mat.set(1, (short)(mat.get(1) * vector.x.get() >> 12));
    mat.set(2, (short)(mat.get(2) * vector.x.get() >> 12));
    mat.set(3, (short)(mat.get(3) * vector.y.get() >> 12));
    mat.set(4, (short)(mat.get(4) * vector.y.get() >> 12));
    mat.set(5, (short)(mat.get(5) * vector.y.get() >> 12));
    mat.set(6, (short)(mat.get(6) * vector.z.get() >> 12));
    mat.set(7, (short)(mat.get(7) * vector.z.get() >> 12));
    mat.set(8, (short)(mat.get(8) * vector.z.get() >> 12));
    return mat;
  }

  @Method(0x8003f210L)
  public static MATRIX FUN_8003f210(final MATRIX a0, final MATRIX a1, final MATRIX a2) {
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    CPU.CTC2(a0.getPacked(0), 0);
    CPU.CTC2(a0.getPacked(2), 1);
    CPU.CTC2(a0.getPacked(4), 2);
    CPU.CTC2(a0.getPacked(6), 3);
    CPU.CTC2(a0.getPacked(8), 4);

    CPU.MTC2((a1.get(3) & 0xffff) << 16 | a1.get(0) & 0xffff, 0);
    CPU.MTC2(                             a1.get(6) & 0xffff, 1);
    CPU.COP2(0x486012L);
    a2.set(0, (short)CPU.MFC2( 9));
    a2.set(3, (short)CPU.MFC2(10));
    a2.set(6, (short)CPU.MFC2(11));

    CPU.MTC2((a1.get(4) & 0xffff) << 16 | a1.get(1) & 0xffff, 0);
    CPU.MTC2(                             a1.get(7) & 0xffff, 1);
    CPU.COP2(0x486012L);
    a2.set(1, (short)CPU.MFC2( 9));
    a2.set(4, (short)CPU.MFC2(10));
    a2.set(7, (short)CPU.MFC2(11));

    CPU.MTC2((a1.get(5) & 0xffff) << 16 | a1.get(2) & 0xffff, 0);
    CPU.MTC2(                             a1.get(8) & 0xffff, 1);
    CPU.COP2(0x486012L);
    a2.set(2, (short)CPU.MFC2( 9));
    a2.set(5, (short)CPU.MFC2(10));
    a2.set(8, (short)CPU.MFC2(11));

    if(a1.transfer.getX() < 0) {
      t0 = -(-a1.transfer.getX() & 0x7fffL);
    } else {
      //LAB_8003f33c
      t0 = a1.transfer.getX() & 0x7fffL;
    }

    //LAB_8003f344
    if(a1.transfer.getY() < 0) {
      t1 = -(-a1.transfer.getY() & 0x7fffL);
    } else {
      //LAB_8003f364
      t1 = a1.transfer.getY() & 0x7fffL;
    }

    //LAB_8003f36c
    if(a1.transfer.getZ() < 0) {
      t2 = -(-a1.transfer.getZ() & 0x7fffL);
    } else {
      //LAB_8003f38c
      t2 = a1.transfer.getZ() & 0x7fffL;
    }

    //LAB_8003f394
    CPU.MTC2(a1.transfer.getX() >> 15,  9);
    CPU.MTC2(a1.transfer.getY() >> 15, 10);
    CPU.MTC2(a1.transfer.getZ() >> 15, 11);
    CPU.COP2(0x41e012L);
    t3 = CPU.MFC2(25);
    t4 = CPU.MFC2(26);
    t5 = CPU.MFC2(27);

    CPU.MTC2(t0,  9);
    CPU.MTC2(t1, 10);
    CPU.MTC2(t2, 11);
    CPU.COP2(0x49e012L);

    //LAB_8003f3e0
    //LAB_8003f3e4
    //LAB_8003f3fc
    //LAB_8003f400
    //LAB_8003f418
    //LAB_8003f41c
    a2.transfer.setX((int)(CPU.MFC2(25) + t3 * 8 + a0.transfer.getX()));
    a2.transfer.setY((int)(CPU.MFC2(26) + t4 * 8 + a0.transfer.getY()));
    a2.transfer.setZ((int)(CPU.MFC2(27) + t5 * 8 + a0.transfer.getZ()));
    return a2;
  }

  /**
   * Multiply two matrices. Multiplies m1 by m0. The result is stored in m0 and returned.
   *
   * @param m0 Matrix 0 - both modified and returned
   * @param m1 Matrix 1
   *
   * @return m0
   */
  @Method(0x8003f460L)
  public static MATRIX MulMatrix(final MATRIX m0, final MATRIX m1) {
    CPU.CTC2(m0.getPacked(0), 0); //
    CPU.CTC2(m0.getPacked(2), 1); //
    CPU.CTC2(m0.getPacked(4), 2); // Rotation matrix (3x3)
    CPU.CTC2(m0.getPacked(6), 3); //
    CPU.CTC2(m0.getPacked(8), 4); //

    CPU.MTC2((m1.get(3) & 0xffffL) << 16 | m1.get(0) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(6) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(0, (short)CPU.MFC2( 9)); // IR1
    m0.set(3, (short)CPU.MFC2(10)); // IR2
    m0.set(6, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((m1.get(4) & 0xffffL) << 16 | m1.get(1) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(7) & 0xffffL, 1); // VZ0
    CPU.COP2(0x486012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(1, (short)CPU.MFC2( 9)); // IR1
    m0.set(4, (short)CPU.MFC2(10)); // IR2
    m0.set(7, (short)CPU.MFC2(11)); // IR3

    CPU.MTC2((m1.get(5) & 0xffffL) << 16 | m1.get(2) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(8) & 0xffffL, 1); // VZ0
    CPU.COP2(0x486012L); // MVMVA - Multiply vector by matrix and add vector
    m0.set(2, (short)CPU.MFC2( 9)); // IR1
    m0.set(5, (short)CPU.MFC2(10)); // IR2
    m0.set(8, (short)CPU.MFC2(11)); // IR3

    return m0;
  }

  /**
   * Multiply two matrices. Multiplies m1 by m0. m1 is both modified and returned.
   *
   * @param m0 Matrix 0
   * @param m1 Matrix 1 - both modified and returned
   *
   * @return m1
   */
  @Method(0x8003f570L)
  public static MATRIX MulMatrix2(final MATRIX m0, final MATRIX m1) {
    CPU.CTC2(m0.getPacked(0), 0); //
    CPU.CTC2(m0.getPacked(2), 1); //
    CPU.CTC2(m0.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(m0.getPacked(6), 3); //
    CPU.CTC2(m0.getPacked(8), 4); //

    // First row
    CPU.MTC2((m1.get(3) & 0xffffL) << 16 | m1.get(0) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(6) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(0, (short)CPU.MFC2( 9)); // IR1
    m1.set(3, (short)CPU.MFC2(10)); // IR2
    m1.set(6, (short)CPU.MFC2(11)); // IR3

    // Second row
    CPU.MTC2((m1.get(4) & 0xffffL) << 16 | m1.get(1) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(7) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(1, (short)CPU.MFC2( 9)); // IR1
    m1.set(4, (short)CPU.MFC2(10)); // IR2
    m1.set(7, (short)CPU.MFC2(11)); // IR3

    // Third row
    CPU.MTC2((m1.get(5) & 0xffffL) << 16 | m1.get(2) & 0xffffL, 0); // VXY0
    CPU.MTC2(                              m1.get(8) & 0xffffL, 1); // VZ0
    CPU.COP2(0x48_6012L); // Multiply vector by matrix and add vector
    m1.set(2, (short)CPU.MFC2( 9)); // IR1
    m1.set(5, (short)CPU.MFC2(10)); // IR2
    m1.set(8, (short)CPU.MFC2(11)); // IR3

    return m1;
  }

  @Method(0x8003f6d0L)
  public static SVECTOR ApplyMatrixSV(final MATRIX mat, final SVECTOR in, final SVECTOR out) {
    CPU.CTC2(mat.getPacked(0), 0); //
    CPU.CTC2(mat.getPacked(2), 1); //
    CPU.CTC2(mat.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(mat.getPacked(6), 3); //
    CPU.CTC2(mat.getPacked(8), 4); //
    CPU.MTC2(in.getXY(), 0); // VXY0
    CPU.MTC2(in.getZ(),  1); // VZ0

    CPU.COP2(0x48_6012L); // MVMVA - Multiply vector by matrix and add vector

    out.setX((short)CPU.MFC2( 9)); // IR1
    out.setY((short)CPU.MFC2(10)); // IR2
    out.setZ((short)CPU.MFC2(11)); // IR3
    return out;
  }

  /**
   * Gives an amount of parallel transfer expressed by v to the matrix m.
   *
   * @param matrix Pointer to matrix (output)
   * @param vector Pointer to transfer vector (input)
   *
   * @return matrix
   */
  @Method(0x8003f730L)
  public static MATRIX TransMatrix(final MATRIX matrix, final VECTOR vector) {
    matrix.transfer.set(vector);
    return matrix;
  }

  @Method(0x8003f760L)
  public static MATRIX ScaleMatrixL(final MATRIX matrix, final VECTOR vector) {
    final long vx = vector.getX();
    final long vy = vector.getY();
    final long vz = vector.getZ();

    matrix.set(0, (short)(matrix.get(0) * vx >> 12));
    matrix.set(1, (short)(matrix.get(1) * vy >> 12));
    matrix.set(2, (short)(matrix.get(2) * vz >> 12));
    matrix.set(3, (short)(matrix.get(3) * vx >> 12));
    matrix.set(4, (short)(matrix.get(4) * vy >> 12));
    matrix.set(5, (short)(matrix.get(5) * vz >> 12));
    matrix.set(6, (short)(matrix.get(6) * vx >> 12));
    matrix.set(7, (short)(matrix.get(7) * vy >> 12));
    matrix.set(8, (short)(matrix.get(8) * vz >> 12));

    return matrix;
  }

  @Method(0x8003f8c0L)
  public static int getProjectionPlaneDistance() {
    return (int)CPU.CFC2(26);
  }

  @Method(0x8003f8d0L)
  public static void SetFarColour(final int r, final int g, final int b) {
    CPU.CTC2(r << 4, 0x15);
    CPU.CTC2(g << 4, 0x16);
    CPU.CTC2(b << 4, 0x17);
  }

  @Method(0x8003f8f0L) //Also 0x8003c6d0
  public static void setProjectionPlaneDistance(final int distance) {
    CPU.CTC2(distance, 26);
  }

  @Method(0x8003f900L)
  public static long FUN_8003f900(final SVECTOR vxyz0, final DVECTOR sxy2, final Ref<Long> ir0, final Ref<Long> flags) {
    CPU.MTC2(vxyz0.getXY(), 0);
    CPU.MTC2(vxyz0.getZ() & 0xffffL, 1);
    CPU.COP2(0x18_0001L); // Perspective transform single
    sxy2.setXY(CPU.MFC2(14)); // SXY2
    ir0.set(CPU.MFC2(8)); // IR0
    flags.set(CPU.CFC2(31)); // Flags
    return  CPU.MFC2(19) >> 2; // SZ3
  }

  @Method(0x8003f930L)
  public static long FUN_8003f930(final SVECTOR a0, final SVECTOR a1, final SVECTOR a2, final long a3, final long a4, final long a5, final Ref<Long> a6, final Ref<Long> a7) {
    CPU.MTC2(a0.getXY(), 0);
    CPU.MTC2(a0.getZ(), 1);
    CPU.MTC2(a1.getXY(), 2);
    CPU.MTC2(a1.getZ(), 3);
    CPU.MTC2(a2.getXY(), 4);
    CPU.MTC2(a2.getZ(), 5);

    CPU.COP2(0x280030L);

    MEMORY.ref(4, a3).setu(CPU.MFC2(12));
    MEMORY.ref(4, a4).setu(CPU.MFC2(13));
    MEMORY.ref(4, a5).setu(CPU.MFC2(14));
    a6.set(CPU.MFC2(8));
    a7.set(CPU.CFC2(31));

    return (int)CPU.MFC2(19) >> 2;
  }

  @Method(0x8003f990L)
  public static void FUN_8003f990(final SVECTOR v0, final VECTOR out, final UnsignedIntRef flags) {
    CPU.MTC2(v0.getXY(), 0); // VXY0
    CPU.MTC2(v0.getZ(), 1); // VZ0
    CPU.COP2(0x48_0012L); // MVMVA (translation=tr, mul vec=v0, mul mat=rot, 12-bit fraction)
    out.setX((int)CPU.MFC2(25)); // MAC1
    out.setY((int)CPU.MFC2(26)); // MAC2
    out.setZ((int)CPU.MFC2(27)); // MAC3
    flags.set(CPU.CFC2(31)); // Flags
  }

  /**
   * <p>Perform coordinate and perspective transformation for 4 vertices.</p>
   *
   * <p>After transforming the four coordinate vectors v0, v1, v2, and v3 using a rotation matrix, the function
   * performs perspective transformation, and returns four screen coordinates sxy0, sxy1, sxy2, and sxy3. It
   * also returns an interpolation value for depth cueing to p corresponding to v3.</p>
   *
   * @param v0 Vector 0
   * @param v1 Vector 1
   * @param v2 Vector 2
   * @param v3 Vector 3
   * @param sxyz0 Screen coords 0 (out)
   * @param sxyz1 Screen coords 1 (out)
   * @param sxyz2 Screen coords 2 (out)
   * @param sxyz3 Screen coords 3 (out)
   * @param ir0 Interpolated value for depth cueing (out)
   * @param flags Flags (out)
   *
   * @return 1/4 of the Z component sz of the screen coordinates corresponding to v3. TODO should this be signed since it's the Z coord?
   */
  @Method(0x8003f9c0L)
  public static long RotTransPers4(final SVECTOR v0, final SVECTOR v1, final SVECTOR v2, final SVECTOR v3, final SVECTOR sxyz0, final SVECTOR sxyz1, final SVECTOR sxyz2, final SVECTOR sxyz3, final Ref<Long> ir0, final Ref<Long> flags) {
    CPU.MTC2(v0.getXY(), 0); // VXY0
    CPU.MTC2(v0.getZ(),  1); // VZ0
    CPU.MTC2(v1.getXY(), 2); // VXY1
    CPU.MTC2(v1.getZ(),  3); // VZ1
    CPU.MTC2(v2.getXY(), 4); // VXY2
    CPU.MTC2(v2.getZ(),  5); // VZ2
    CPU.COP2(0x28_0030L); // Perspective transformation triple
    sxyz0.setXY(CPU.MFC2(12)); // SXY0
    sxyz1.setXY(CPU.MFC2(13)); // SXY1
    sxyz2.setXY(CPU.MFC2(14)); // SXY2
    final long flags1 = CPU.CFC2(31); // Flags

    CPU.MTC2(v3.getXY(), 0); // SXY0
    CPU.MTC2(v3.getZ(),  1); // SZ0
    CPU.COP2(0x18_0001L); // Perspective transformation single
    sxyz3.setXY(CPU.MFC2(14)); // SXY2
    ir0.set(CPU.MFC2(8)); // IR0
    final long flags2 = CPU.CFC2(31); // Flags

    flags.set(flags2 | flags1);

    return CPU.MFC2(19) >> 2; // SZ3
  }

  /**
   * Perform coordinate and perspective transformation.
   * <p>
   * Executes RotTransPers() for the number of vertices specified by n.<br>
   * The arguments and internal data formats are as follows:<br>
   * <pre>v0 -> vx, vy, vz : (1, 15, 0)</pre>
   * <pre>v1 -> vx, vy : (1, 15, 0)</pre>
   * <pre>sz : (0, 16, 0)</pre>
   * <pre>flag : (0, 16, 0)</pre>
   * The flag must normally be set between bits 27 and 12 of the 32-bit flag.
   *
   * @param v0 Pointer to vertex coordinate vector (input)
   * @param v1 Pointer to vertex coordinate vector (output)
   * @param sz Pointer to SZ value (output)
   * @param interpolation Pointer to interpolation value (output)
   * @param flag Pointer to flag (output)
   * @param count Number of vertices (output)
   */
  @Method(0x8003fa40L)
  public static void RotTransPersN(final SVECTOR[] v0, final DVECTOR[] v1, final UnsignedShortRef[] sz, final UnsignedShortRef[] interpolation, final UnsignedShortRef[] flag, final int count) {
    //LAB_8003fa4c
    for(int i = 0; i < count; i++) {
      CPU.MTC2(v0[i].getXY(), 0); // VXY0
      CPU.MTC2(v0[i].getZ(), 1); // VZ0

      CPU.COP2(0x18_0001L); // Perspective transform single

      v1[i] = new DVECTOR().setXY(CPU.MFC2(14)); // SXY2
      sz[i] = new UnsignedShortRef().set((int)CPU.MFC2(19)); // SZ3
      interpolation[i] = new UnsignedShortRef().set((int)CPU.MFC2( 8)); // IR0
      flag[i] = new UnsignedShortRef().set((int)(CPU.CFC2(31) >>> 12) & 0xffff); // Flag (see no$ "GTE Saturation")
    }
  }

  /**
   * Transposes matrix m0 into m1.
   *
   * @param m0 Input
   * @param m1 Output
   * @return m1
   */
  @Method(0x8003fab0L)
  public static MATRIX TransposeMatrix(final MATRIX m0, final MATRIX m1) {
    m1.set(0, m0.get(0));
    m1.set(1, m0.get(3));
    m1.set(2, m0.get(6));
    m1.set(3, m0.get(1));
    m1.set(4, m0.get(4));
    m1.set(5, m0.get(7));
    m1.set(6, m0.get(2));
    m1.set(7, m0.get(5));
    m1.set(8, m0.get(8));
    return m1;
  }

  /** TODO one of the RotMatrix* methods */
  @Method(0x8003faf0L)
  public static void RotMatrix_8003faf0(final SVECTOR a0, final MATRIX a1) {
    int sinCos;

    final int x = a0.getX();
    final short sinX;
    if(x < 0) {
      //LAB_8003fb0c
      sinCos = (int)sin_cos_80054d0c.offset((-x & 0xfff) * 4).get();
      sinX = (short)-(short)sinCos;
    } else {
      //LAB_8003fb34
      sinCos = (int)sin_cos_80054d0c.offset((x & 0xfff) * 4).get();
      sinX = (short)sinCos;
    }

    final short cosX = (short)(sinCos >> 16);

    //LAB_8003fb54
    final int y = a0.getY();
    final short sinYN;
    final short sinYP;
    if(y < 0) {
      //LAB_8003fb70
      sinCos = (int)sin_cos_80054d0c.offset((-y & 0xfff) * 4).get();
      sinYN = (short)sinCos;
      sinYP = (short)-(short)sinCos;
    } else {
      //LAB_8003fb98
      sinCos = (int)sin_cos_80054d0c.offset((y & 0xfffL) * 4).get();
      sinYN = (short)-(short)sinCos;
      sinYP = (short)sinCos;
    }

    final short cosY = (short)(sinCos >> 16);

    //LAB_8003fbbc
    final int z = a0.getZ();
    final short sinZ;
    if(z < 0) {
      //LAB_8003fbfc
      sinCos = (int)sin_cos_80054d0c.offset((-z & 0xfff) * 4).get();
      sinZ = (short)-(short)sinCos;
    } else {
      //LAB_8003fc24
      sinCos = (int)sin_cos_80054d0c.offset((z & 0xfff) * 4).get();
      sinZ = (short)sinCos;
    }

    final short cosZ = (short)(sinCos >> 16);

    //LAB_8003fc50
    a1.set(0, (short)(cosZ * cosY >> 12));
    a1.set(1, (short)(-(sinZ * cosY) >> 12));
    a1.set(2, sinYP);
    a1.set(3, (short)((sinZ * cosX >> 12) - ((cosZ * sinYN >> 12) * sinX >> 12)));
    a1.set(4, (short)((cosZ * cosX >> 12) + ((sinZ * sinYN >> 12) * sinX >> 12)));
    a1.set(5, (short)(-(cosY * sinX) >> 12));
    a1.set(6, (short)((sinZ * sinX >> 12) + ((cosZ * sinYN >> 12) * cosX >> 12)));
    a1.set(7, (short)((cosZ * sinX >> 12) - ((sinZ * sinYN >> 12) * cosX >> 12)));
    a1.set(8, (short)(cosY * cosX >> 12));
  }

  /** TODO one of the RotMatrix* methods */
  @Method(0x8003fd80L)
  public static void RotMatrix_8003fd80(final SVECTOR svec, final MATRIX mat) {
    assert false;
  }
}
