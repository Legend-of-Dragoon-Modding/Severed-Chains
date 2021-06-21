package legend.game;

import legend.core.CpuRegisterType;
import legend.core.DebugHelper;
import legend.core.InterruptType;
import legend.core.cdrom.CdlCOMMAND;
import legend.core.cdrom.CdlPacket;
import legend.core.cdrom.Response;
import legend.core.cdrom.SyncCode;
import legend.core.dma.DmaChannelType;
import legend.core.gpu.DISPENV;
import legend.core.gpu.DRAWENV;
import legend.core.gpu.DR_ENV;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.kernel.jmp_buf;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.FunctionRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.SupplierRef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.DMA;
import static legend.core.Hardware.MEMORY;
import static legend.core.InterruptController.I_MASK;
import static legend.core.InterruptController.I_STAT;
import static legend.core.LibDs.DSL_MAX_COMMAND;
import static legend.core.LibDs.DSL_MAX_RESULTS;
import static legend.core.MathHelper.clamp;
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
import static legend.core.kernel.Bios.ExitCriticalSection;
import static legend.core.memory.segments.MemoryControl1Segment.COMMON_DELAY;
import static legend.core.spu.Spu.CD_VOL_L;
import static legend.core.spu.Spu.CD_VOL_R;
import static legend.core.spu.Spu.CURR_MAIN_VOL_L;
import static legend.core.spu.Spu.CURR_MAIN_VOL_R;
import static legend.core.spu.Spu.SPU_CTRL_REG_CPUCNT;
import static legend.core.spu.Spu.SPU_MAIN_VOL_L;
import static legend.core.spu.Spu.SPU_MAIN_VOL_R;
import static legend.game.Scus94491BpeSegment._1f8003dc;
import static legend.game.Scus94491BpeSegment._1f8003de;
import static legend.game.Scus94491BpeSegment._80011394;
import static legend.game.Scus94491BpeSegment._800113c0;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.functionVectorA_000000a0;
import static legend.game.Scus94491BpeSegment.functionVectorB_000000b0;
import static legend.game.Scus94491BpeSegment.functionVectorC_000000c0;
import static legend.game.Scus94491BpeSegment_8002.ChangeClearPAD;
import static legend.game.Scus94491BpeSegment_8002.setScreenOffset;
import static legend.game.Scus94491BpeSegment_8004.patchC0TableAgain;
import static legend.game.Scus94491BpeSegment_8005.CD_debug_80052f4c;
import static legend.game.Scus94491BpeSegment_8005.DISPENV_80054728;
import static legend.game.Scus94491BpeSegment_8005.DRAWENV_800546cc;
import static legend.game.Scus94491BpeSegment_8005.GsOUT_PACKET_P;
import static legend.game.Scus94491BpeSegment_8005.Vcount;
import static legend.game.Scus94491BpeSegment_8005._80052f50;
import static legend.game.Scus94491BpeSegment_8005._80052f54;
import static legend.game.Scus94491BpeSegment_8005._80052f58;
import static legend.game.Scus94491BpeSegment_8005._80053008;
import static legend.game.Scus94491BpeSegment_8005._80053088;
import static legend.game.Scus94491BpeSegment_8005._80053108;
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
import static legend.game.Scus94491BpeSegment_8005._80054790;
import static legend.game.Scus94491BpeSegment_8005._80054792;
import static legend.game.Scus94491BpeSegment_8005._800547bb;
import static legend.game.Scus94491BpeSegment_8005._800547f4;
import static legend.game.Scus94491BpeSegment_8005._800547f8;
import static legend.game.Scus94491BpeSegment_8005._800547fc;
import static legend.game.Scus94491BpeSegment_8005.array_8005473c;
import static legend.game.Scus94491BpeSegment_8005.array_80054748;
import static legend.game.Scus94491BpeSegment_8005.cdlLoc_80053312;
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
import static legend.game.Scus94491BpeSegment_8005.cdromMode_80052f60;
import static legend.game.Scus94491BpeSegment_8005.cdromParamBuffer_80052f5c;
import static legend.game.Scus94491BpeSegment_8005.cdromParams_800532e9;
import static legend.game.Scus94491BpeSegment_8005.cdromResponse_800532f4;
import static legend.game.Scus94491BpeSegment_8005.cdromStat_800532fc;
import static legend.game.Scus94491BpeSegment_8005.cdromSyncCode_80053220;
import static legend.game.Scus94491BpeSegment_8005.dmaCallbacks_80054640;
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
import static legend.game.Scus94491BpeSegment_8005.oldCdromParam_80053311;
import static legend.game.Scus94491BpeSegment_8005.oldIMask_800547ec;
import static legend.game.Scus94491BpeSegment_8005.oldIMask_800547f0;
import static legend.game.Scus94491BpeSegment_8005.previousCdromCommand_80052f61;
import static legend.game.Scus94491BpeSegment_8005.ptrCdromParams_800532f0;
import static legend.game.Scus94491BpeSegment_8005.ptrCdromSyncCode_80053220_80053224;
import static legend.game.Scus94491BpeSegment_8005.syncCode_80053320;
import static legend.game.Scus94491BpeSegment_8005.syncCode_80053470;
import static legend.game.Scus94491BpeSegment_8005.usingLibDs_80052f64;
import static legend.game.Scus94491BpeSegment_8005.vsyncCallbacks_8005460c;
import static legend.game.Scus94491BpeSegment_800b.CdlPacket_800bf638;
import static legend.game.Scus94491BpeSegment_800b._800bf5e0;
import static legend.game.Scus94491BpeSegment_800b._800bf6f8;
import static legend.game.Scus94491BpeSegment_800b._800bf6fc;
import static legend.game.Scus94491BpeSegment_800b._800bf798;
import static legend.game.Scus94491BpeSegment_800b._800bf79c;
import static legend.game.Scus94491BpeSegment_800b._800bf7a4;
import static legend.game.Scus94491BpeSegment_800b.batch_800bf608;
import static legend.game.Scus94491BpeSegment_800b.batch_800bf618;
import static legend.game.Scus94491BpeSegment_800b.batch_800bf628;
import static legend.game.Scus94491BpeSegment_800b.cdlPacketIndex_800bf700;
import static legend.game.Scus94491BpeSegment_800b.cdromDmaInterruptSubCallbackPtr_800bf7a0;
import static legend.game.Scus94491BpeSegment_800b.cdromResponseBufferIndex_800bf788;
import static legend.game.Scus94491BpeSegment_800b.cdromResponseBuffer_800bf708;
import static legend.game.Scus94491BpeSegment_800b.cdromResponses_800bf5c0;
import static legend.game.Scus94491BpeSegment_800b.cdromResponses_800bf5c8;
import static legend.game.Scus94491BpeSegment_800b.cdromResponses_800bf5d0;
import static legend.game.Scus94491BpeSegment_800b.response_800bf60d;
import static legend.game.Scus94491BpeSegment_800b.response_800bf61d;
import static legend.game.Scus94491BpeSegment_800b.response_800bf62d;
import static legend.game.Scus94491BpeSegment_800b.syncCode_800bf60c;
import static legend.game.Scus94491BpeSegment_800b.syncCode_800bf61c;
import static legend.game.Scus94491BpeSegment_800b.syncCode_800bf62c;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.Scus94491BpeSegment_800c.DRAWENV_800c3450;
import static legend.game.Scus94491BpeSegment_800c._800c1bb0;
import static legend.game.Scus94491BpeSegment_800c._800c1bb8;
import static legend.game.Scus94491BpeSegment_800c._800c1bc0;
import static legend.game.Scus94491BpeSegment_800c._800c1be8;
import static legend.game.Scus94491BpeSegment_800c._800c3423;
import static legend.game.Scus94491BpeSegment_800c._800c3427;
import static legend.game.Scus94491BpeSegment_800c._800c3433;
import static legend.game.Scus94491BpeSegment_800c._800c3437;
import static legend.game.Scus94491BpeSegment_800c._800c34c4;
import static legend.game.Scus94491BpeSegment_800c._800c34c6;
import static legend.game.Scus94491BpeSegment_800c._800c34d0;
import static legend.game.Scus94491BpeSegment_800c._800c34d8;
import static legend.game.Scus94491BpeSegment_800c._800c34dc;
import static legend.game.Scus94491BpeSegment_800c._800c34e0;
import static legend.game.Scus94491BpeSegment_800c.cdromDmaInterruptSubSubCallbackPtr_800c1bb4;
import static legend.game.Scus94491BpeSegment_800c.clip_800c3440;
import static legend.game.Scus94491BpeSegment_800c.clip_800c3448;
import static legend.game.Scus94491BpeSegment_800c.displayRect_800c34c8;
import static legend.game.Scus94491BpeSegment_800c.doubleBufferFrame_800c34d4;
import static legend.game.Scus94491BpeSegment_800c.doubleBufferOffsetMode_800c34d6;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallbackObjPtr_800c1c14;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallbackObj_800c1c1c;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallbackSomething_800c1c18;
import static legend.game.Scus94491BpeSegment_800c.gpuDmaCallback_800c1c10;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3568;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3588;

public final class Scus94491BpeSegment_8003 {
  private Scus94491BpeSegment_8003() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8003.class);

  private static final Object[] EMPTY_OBJ_ARRAY = {};

  @Method(0x800309f0L)
  public static void bzero(final long address, final int size) {
    functionVectorA_000000a0.run(0x28L, new Object[] {address, size});
  }

  /**
   * @return A bitmask telling which callbacks to execute. Bit 1 is set on interrupts 2, 3, 5 (complete callback). Bit 2 is set on interrupts 1, 4, 5 (data callback).
   */
  @Method(0x800319e0L)
  public static long processCdromInterrupt() {
    CDROM_REG0.setu(0x1L); // Index 1

    SyncCode syncCode = SyncCode.fromLong(CDROM_REG3.get(0b111L)); // Read interrupt response

    if(syncCode == SyncCode.NO_INTERRUPT) {
      return 0;
    }

    //LAB_80031a44
    //LAB_80031a34
    while(syncCode != SyncCode.fromLong(CDROM_REG3.get(0b111L))) {
      syncCode = SyncCode.fromLong(CDROM_REG3.get(0b111L)); // Read interrupt response
    }

    int resultIndex;
    final long[] results = new long[DSL_MAX_RESULTS];

    //LAB_80031a5c
    for(resultIndex = 0; resultIndex < DSL_MAX_RESULTS; resultIndex++) {
      if(CDROM_REG0.get(0b10_0000L) == 0) { // Not RSLRRDY
        break;
      }

      results[resultIndex] = CDROM_REG1.get(); // Read response to stack
    }

    //LAB_80031ac8
    CDROM_REG0.setu(0x1L); // Index 1
    CDROM_REG3.setu(0x7L); // Acknowledge interrupts
    CDROM_REG2.setu(0x7L); // Enable interrupts

    final long errors;

    if(syncCode != SyncCode.ACKNOWLEDGE || _80053108.get(previousCdromCommand_80052f61.get().command).get() != 0) {
      //LAB_80031b30
      if(_80052f50.get(0x10L) == 0) {
        if((results[0] & 0x10L) != 0) {
          _80052f58.addu(0x1L);
        }
      }

      //LAB_80031b74
      errors = results[0] & 0b1_1101L; // error, seek error, ID error, or shell open
      _80052f50.setu(results[0]);
      _80052f54.setu(results[1]);
    } else {
      errors = 0;
    }

    //LAB_80031b94
    if(syncCode == SyncCode.DISK_ERROR) {
      if(CD_debug_80052f4c.get() >= 0x3L) {
        LOGGER.error("DiskError:");
        LOGGER.error("com=%s,code=(%02x:%02x)", previousCdromCommand_80052f61.get().name(), _80052f50.get(), _80052f54.get());
      }
    }

    //LAB_80031c14
    LOGGER.debug("Handling CDROM interrupt %s %s", syncCode, Arrays.toString(results));

    switch(syncCode) {
      case ACKNOWLEDGE -> {
        if(errors != 0) {
          cdromSyncCode_80053220.set(SyncCode.DISK_ERROR);

          //LAB_80031c70
          for(int i = 0; i < DSL_MAX_RESULTS; i++) {
            cdromResponses_800bf5c0.get(i).set((byte)results[i]);
          }

          assert false : "Disk error";
          return 0x2L;
        }

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
        if(errors == 0) {
          cdromSyncCode_80053220.set(SyncCode.COMPLETE);
        } else {
          cdromSyncCode_80053220.set(SyncCode.DISK_ERROR);
        }

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
        if(errors == 0 || resultIndex == 0x1L) {
          cdromDataSyncCode_80053221.set(SyncCode.DATA_READY);
        } else {
          cdromDataSyncCode_80053221.set(SyncCode.DISK_ERROR);
          assert false : "Disk error";
        }

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

      case DISK_ERROR -> {
        cdromDataSyncCode_80053221.set(SyncCode.DISK_ERROR);
        cdromSyncCode_80053220.set(SyncCode.DISK_ERROR);

        //LAB_80031ec0
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5c0.get(i).set((byte)results[i]);
        }

        //LAB_80031ed8
        //LAB_80031ef0
        for(int i = 0; i < DSL_MAX_RESULTS; i++) {
          cdromResponses_800bf5c8.get(i).set((byte)results[i]);
        }

        //LAB_80031f08
        return 0x6L;
      }
    }

    LOGGER.error("CDROM: unknown intr (%d)", syncCode);

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
   * TODO this is not actually DsControl
   */
  @Method(0x8003248cL)
  public static long DsControl(final CdlCOMMAND command, final long paramAddress, long responseAddress, final boolean async) {
    if(CD_debug_80052f4c.get() >= 0x2L) {
      LOGGER.info("%s...", command.toString());
    }

    //LAB_800324f0
    if(command.paramCount != 0 && paramAddress == 0) {
      if(CD_debug_80052f4c.get() > 0) {
        LOGGER.info("%s: no param", command.toString());
      }

      return 0xffff_fffeL;
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
    for(long param = 0; param < command.paramCount; param++) {
      CDROM_REG2.setu(MEMORY.ref(1, paramAddress).offset(param));
    }

    //LAB_8003262c
    previousCdromCommand_80052f61.set(command);
    CDROM_REG1.setu(command.command);

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
        do {
          final long callbacks = processCdromInterrupt();
          if(callbacks == 0) {
            break;
          }

          if((callbacks & 0x4L) != 0 && !cdromDataInterruptCallbackPtr_80052f48.isNull()) {
            cdromDataInterruptCallbackPtr_80052f48.deref().run(cdromDataSyncCode_80053221.get(), MEMORY.getBytes(cdromResponses_800bf5c8.getAddress(), DSL_MAX_RESULTS));
          }

          //LAB_800327c8
          //LAB_800327cc
          if((callbacks & 0x2L) != 0 && !cdromCompleteInterruptCallbackPtr_80052f44.isNull()) {
            cdromCompleteInterruptCallbackPtr_80052f44.deref().run(cdromSyncCode_80053220.get(), MEMORY.getBytes(cdromResponses_800bf5c0.getAddress(), DSL_MAX_RESULTS));
          }
        } while(true);

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
    if(CURR_MAIN_VOL_L.get() == 0 && CURR_MAIN_VOL_R.get() == 0) {
      SPU_MAIN_VOL_L.setu(0x3fffL);
      SPU_MAIN_VOL_R.setu(0x3fffL);
    }

    //LAB_80032a30
    //LAB_80032a34
    CD_VOL_L.setu(0x3fffL);
    CD_VOL_R.setu(0x3fffL);

    SPU_CTRL_REG_CPUCNT.setu(0xc001L);
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

    DsControl(CdlCOMMAND.GET_STAT_01, 0, 0, false);

    if(_80052f50.get(0x10L) == 0) {
      DsControl(CdlCOMMAND.GET_STAT_01, 0, 0, false);
    }

    //LAB_80032ca4
    if(DsControl(CdlCOMMAND.INIT_0A, 0, 0, false) != 0) {
      return -0x1L;
    }

    if(DsControl(CdlCOMMAND.DEMUTE_0C, 0, 0, false) != 0) {
      return -0x1L;
    }

    if(FUN_80031f44(0, 0) != SyncCode.COMPLETE) {
      return -0x1L;
    }

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

  @Method(0x80033304L)
  public static void FUN_80033304(final SyncCode syncCode, final byte[] responses) {
    assert false;
    //TODO
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
    cdromDmaInterruptSubSubCallbackPtr_800c1bb4.clear();
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
    setCdromDmaInterruptSubCallbackPtr_800bf7a0(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "cdromDmaInterruptSubCallback", SyncCode.class, byte[].class), BiConsumerRef::new));
    set800bf7a4(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_800344b8", SyncCode.class, byte[].class), BiConsumerRef::new));
    set800bf798(MEMORY.ref(4, getMethodAddress(Scus94491BpeSegment_8003.class, "FUN_80034340"), RunnableRef::new));
    FUN_80036b38();
    set80053498(0);

    //LAB_80033944
    return 1;
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
  public static void cdromDmaInterruptSubCallback(final SyncCode syncCode, final byte[] responses) {
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
    if(!cdromDmaInterruptSubSubCallbackPtr_800c1bb4.isNull()) {
      cdromDmaInterruptSubSubCallbackPtr_800c1bb4.deref().run(syncCode, responses);
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
    cdromDmaInterruptSubCallbackPtr_800bf7a0.clear();

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
  public static void setCdromDmaInterruptSubCallbackPtr_800bf7a0(final BiConsumerRef<SyncCode, byte[]> callback) {
    cdromDmaInterruptSubCallbackPtr_800bf7a0.set(callback);
  }

  @Method(0x80034750L)
  public static void set800bf7a4(final BiConsumerRef<SyncCode, byte[]> callback) {
    _800bf7a4.set(callback);
  }

  @Method(0x8003475cL)
  public static long FUN_8003475c(final long a0) {
    return _80053304.offset(a0 * 4L).get();
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
    if(DsControl(command, ptrCdromParams_800532f0.getAddress(), 0, true) == 0) {
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
    assert false;
    //TODO
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
        if(!cdromDmaInterruptSubCallbackPtr_800bf7a0.isNull()) {
          if(_800532e4.get() != 0) {
            cdromDmaInterruptSubCallbackPtr_800bf7a0.deref().run(SyncCode.DISK_ERROR, responses);
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
    assert false;
    //TODO
  }

  @Method(0x800353b8L)
  public static void cdromDataInterruptCallback(final SyncCode syncCode, final byte[] responses) {
    handleResponseStatus(syncCode, responses);

    if(cdromStat_800532fc.get(0b1_0000L) != 0) { // Shell open
      _80053304.setu(0x2L);
      _80053308.setu(0xcL);
    }

    //LAB_80035400
    if(!cdromDmaInterruptSubCallbackPtr_800bf7a0.isNull() && _800532e4.get() != 0) {
      cdromDmaInterruptSubCallbackPtr_800bf7a0.deref().run(syncCode, responses);
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

  @Method(0x80035630L)
  public static void CdMix(final long cdLeftToSpuLeft, final long cdLeftToSpuRight, final long cdRightToSpuRight, final long cdRightToSpuLeft) {
    CdMix_Impl(cdLeftToSpuLeft, cdLeftToSpuRight, cdRightToSpuRight, cdRightToSpuLeft);
  }

  @Method(0x80035b70L)
  public static void FUN_80035b70(final long a0) {
    if(a0 < 0x2L) {
      _80053448.setu(a0);
    }
  }

  @Method(0x800366d8L)
  public static long set80053498(final long a0) {
    final long oldValue = _80053498.get();
    _80053498.setu(a0);
    return oldValue;
  }

  @Method(0x80036b38L)
  public static void FUN_80036b38() {
    if(_8005349c.get() == 0x1L) {
      setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(_80053490.derefNullable());
      set800c1bb8(_80053494.derefNullable());
    }

    _8005349c.setu(0);
  }

  @Method(0x80037330L)
  public static long setCdDebug(final long value) {
    final long old = CD_debug_80052f4c.get();
    CD_debug_80052f4c.setu(value);
    return old;
  }

  @Method(0x80037420L)
  @Nullable
  public static BiConsumerRef<SyncCode, byte[]> setCdromDmaInterruptSubSubCallbackPtr_800c1bb4(@Nullable final BiConsumerRef<SyncCode, byte[]> callback) {
    final BiConsumerRef<SyncCode, byte[]> old = cdromDmaInterruptSubSubCallbackPtr_800c1bb4.derefNullable();

    if(callback == null){
      cdromDmaInterruptSubSubCallbackPtr_800c1bb4.clear();
    } else {
      cdromDmaInterruptSubSubCallbackPtr_800c1bb4.set(callback);
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
  public static void SetDmaInterruptCallback(final DmaChannelType channel, final long callback) {
    _800545ec.deref(4).offset(0x4L).deref(4).call(channel, callback);
  }

  @Method(0x80037740L)
  public static long SetTmr0InterruptCallback(final long func) {
    return (long)_800545ec.deref(4).offset(0x14L).deref(4).call(InterruptType.TMR0, func);
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

    return getMethodAddress(Scus94491BpeSegment_8003.class, "SetVsyncInterruptCallback_Impl", InterruptType.class, long.class);
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
  public static void SetVsyncInterruptCallback_Impl(final InterruptType interrupt, final long callback) {
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

    return (int)_800546b4.deref(4).offset(0x8L).deref(4).call(
      _800546b4.deref(4).offset(0xcL).deref(4).cast(BiFunctionRef::new),
      rect,
      0x8L,
      (b & 0xffL) << 16 | (g & 0xffL) << 8 | r & 0xffL
    );
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

    buildDrawEnvPacket(env.dr_env, env);
    env.dr_env.tag.set((int)(env.dr_env.tag.get() | 0xff_ffff));
    _800546b4.deref(4).offset(0x8L).deref(4).call(
      _800546b4.deref(4).offset(0x18L).deref(4).cast(BiFunctionRef::new),
      env.dr_env,
      0x40L,
      0L
    );
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

    if(DISPENV_80054728.isinter != env.isinter || DISPENV_80054728.disp.x.get() != env.disp.x.get() || DISPENV_80054728.disp.y.get() != env.disp.y.get() || DISPENV_80054728.disp.w.get() != env.disp.w.get() || DISPENV_80054728.disp.h.get() != env.disp.h.get()) {
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

    //LAB_80039204
    return env;
  }

  @Method(0x80039550L)
  public static void buildDrawEnvPacket(final DR_ENV dr_env, final DRAWENV drawEnv) {
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
        dr_env.code.get(size++).set((drawEnv.clip.x.get() - drawEnv.ofs.get(0).get() & 0xffff) << 16 | drawEnv.clip.y.get() - drawEnv.ofs.get(1).get() & 0xffff);
      } else {
        //LAB_8003974c
        // Monochrome triangle, opaque
        dr_env.code.get(size++).set(0x200_0000L | (drawEnv.b0.get() & 0xff) << 16 | (drawEnv.g0.get() & 0xff) << 8 | drawEnv.r0.get() & 0xff);
        dr_env.code.get(size++).set((drawEnv.clip.x.get() & 0xffff) << 16 | drawEnv.clip.y.get() & 0xffff);
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
    return 0xe500_0000L | (y & 0x7ffL) << 0xbL | x & 0x7ffL;
  }

  @Method(0x8003992cL)
  public static long makeTextureWindowSettingsCommand(final RECT rect) {
    if(rect == null) {
      return 0;
    }

    //LAB_8003993c
    final long v0 = rect.y.get() / 8L << 15;
    final long a1 = rect.x.get() / 8L << 10;
    final long v1 = (-rect.h.get() & 0xffL) / 8L << 5;
    final long a2 = (-rect.w.get() & 0xffL) / 8L;

    //LAB_800399a4
    return 0xe200_0000L | v0 | a1 | v1 | a2;
  }

  @Method(0x80039aa4L)
  public static long ClearImage_Impl(final RECT rect, final long colour) {
    rect.w.set(clamp(rect.w.get(), (short)0, (short)(_800546c0.get() - 1)));
    rect.h.set(clamp(rect.h.get(), (short)0, (short)(_800546c2.get() - 1)));

    if((rect.x.get() & 0x3fL) != 0 || (rect.w.get() & 0x3fL) != 0) {
      //LAB_80039b64
      _800c1bc0.build(builder -> builder
        .add(0xe300_0000L) // Draw area top left (0, 0)
        .add(0xe4ff_ffffL) // Draw area bottom right (1023, 511)
        .add(0xe500_0000L) // Draw offset (0, 0)
        .add(0xe600_0000L) // Mask bit (draw always)
        .add(0xe100_0000L | GPU_REG1.get(0x7ffL) | (int)colour >> 31 << 10) // Draw mode
        .add(0x6000_0000L | colour) // Monochrome rect (var size, opaque) - colour bbrrgg
        .add(rect.y.get() << 16 | rect.x.get()) // - vertex YyyyXxxx
        .add(rect.h.get() << 16 | rect.w.get()) // - w/h HhhhWwww
        .next(_800c1be8)
      );

      _800c1be8.build(builder -> builder
        .add(0xe300_0000L | FUN_8003a234(0x3L)) // Draw area top left
        .add(0xe400_0000L | FUN_8003a234(0x4L)) // Draw area top right
        .add(0xe500_0000L | FUN_8003a234(0x5L)) // Draw offset
      );
    } else {
      //LAB_80039c3c
      _800c1bc0.build(builder -> builder
        .add(0xe600_0000L) // Mask bit (draw always)
        .add(0xe100_0000L | GPU_REG1.get(0x7ffL) | (int)colour >> 31 << 10) // Draw mode
        .add(0x0200_0000L | colour) // Fill rect - BbGgRr
        .add(rect.y.get() << 16 | rect.x.get()) // - YyyyXxxx
        .add(rect.h.get() << 16 | rect.w.get()) // - HhhhWwww
      );
    }

    //LAB_80039cac
    uploadLinkedListToGpu(_800c1bc0);
    return 0;
  }

  @Method(0x8003a190L)
  public static void sendDisplayCommand(final long command) {
    GPU_REG1.setu(command);
  }

  @Method(value = 0x8003a1ecL, ignoreExtraParams = true)
  public static void uploadLinkedListToGpu(final MemoryRef address) {
    GPU_REG1.setu(0x400_0002L);
    DMA.gpu.MADR.setu(address.getAddress());
    DMA.gpu.BCR.setu(0);
    DMA.gpu.CHCR.setu(0x100_0401L);
  }

  @Method(0x8003a234L)
  public static long FUN_8003a234(final long a0) {
    GPU_REG1.setu(0x1000_0000L | a0);
    return GPU_REG0.get(0xff_ffffL);
  }

  @Method(0x8003a288L)
  public static int FUN_8003a288(final BiFunctionRef<MemoryRef, Long, Integer> callback, final MemoryRef arg1, final long arg1Size, final long arg2) {
    recordGpuTime();

    //LAB_8003a2bc
    while((gpuQueueIndex_800547e4.get() + 0x1L & 0x3fL) == gpuQueueTotal_800547e8.get()) {
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

  @Method(0x8003b3f0L)
  public static long FUN_8003b3f0(final long a0, final long a1, final long a2, final long a3) {
    return
      (a0 & 0x3L) << 0x7L |
        (a1 & 0x3L) << 0x5L |
        (a3 & 0x100L) >> 0x4L |
        (a2 & 0x3ffL) >> 0x6L |
        (a3 & 0x200L) << 0x2L;
  }


  @Method(0x8003bc30L)
  public static void FUN_8003bc30(final short displayWidth, final short displayHeight, final int flags, final boolean dither, final boolean use24BitColour) {
    GsInitGraph(displayWidth, displayHeight, flags, dither, use24BitColour);
    initGraphics();

    doubleBufferFrame_800c34d4.setu(0);

    FUN_8003be28(displayWidth, displayHeight);
    updateDrawEnvClip();
    FUN_8003c1c0();
  }

  @Method(0x8003bca4L)
  public static void GsInitGraph(final short displayWidth, final short displayHeight, final int flags, final boolean dither, final boolean use24BitColour) {
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
    DISPENV_800c34b0.screen.set((short)0, (short)0, (short)256, (short)240); // W/H were 0, but docs say they get treated as this

    if(GsGetWorkBase() == 1) {
      DISPENV_800c34b0.screen.y.set((short)0x18);
      DISPENV_800c34b0.pad0.set((byte)1);
    }

    DISPENV_800c34b0.isinter.set((byte)(flags & 0b1));
    DISPENV_800c34b0.isrgb24.set((byte)(use24BitColour ? 1 : 0));

    doubleBufferOffsetMode_800c34d6.setu(flags & 0b100);

    PutDispEnv(DISPENV_800c34b0);
  }

  @Method(0x8003be28L)
  public static void FUN_8003be28(final long displayWidth, final long displayHeight) {
    displayWidth_1f8003e0.setu(displayWidth);
    displayHeight_1f8003e4.setu(displayHeight);

    if(displayWidth == 0) {
      throw new RuntimeException("TRAP: 0x1c00");
    }

    final long a0 = (displayHeight << 14 / displayWidth) / 3;

    matrix_800c3568.set(0, 0, (short)0x1000);
    matrix_800c3568.set(0, 1, (short)0);
    matrix_800c3568.set(0, 2, (short)0);
    matrix_800c3568.set(1, 0, (short)0);
    matrix_800c3568.set(1, 1, (short)0x1000);
    matrix_800c3568.set(1, 2, (short)0);
    matrix_800c3568.set(2, 0, (short)0);
    matrix_800c3568.set(2, 1, (short)0);
    matrix_800c3568.set(2, 2, (short)0x1000);
    matrix_800c3568.setTransferVector(0, 0);
    matrix_800c3568.setTransferVector(1, 0);
    matrix_800c3568.setTransferVector(2, 0);

    matrix_800c3588.set(0, 0, (short)0x1000);
    matrix_800c3588.set(0, 1, (short)0);
    matrix_800c3588.set(0, 2, (short)0);
    matrix_800c3588.set(1, 0, (short)0);
    matrix_800c3588.set(1, 1, (short)a0);
    matrix_800c3588.set(1, 2, (short)0);
    matrix_800c3588.set(2, 0, (short)0);
    matrix_800c3588.set(2, 1, (short)0);
    matrix_800c3588.set(2, 2, (short)0x1000);
    matrix_800c3588.setTransferVector(0, 0);
    matrix_800c3588.setTransferVector(1, 0);
    matrix_800c3588.setTransferVector(2, 0);

    matrix_800c34e8.clear();
    matrix_800c3508.clear();

    clip_800c3448.clear();

    _1f8003de.setu(0);
    _1f8003dc.setu(0);

    displayRect_800c34c8.set((short)0, (short)0, (short)displayWidth, (short)displayHeight);

    _800c3423.setu(0x3L);
    _800c3427.setu(0x2L);
    _800c3433.setu(0x3L);
    _800c3437.setu(0x2L);

    _800c34d0.setu(0x1L);
  }

  @Method(0x8003c1c0L)
  public static void FUN_8003c1c0() {
    final long x = _1f8003dc.get();
    final long y = _1f8003de.get();

    final long clipX;
    final long clipY;
    if(doubleBufferFrame_800c34d4.get() == 0) {
      clipX = clip_800c3440.x2.get();
      clipY = clip_800c3440.y2.get();
    } else {
      clipX = clip_800c3440.x1.get();
      clipY = clip_800c3440.y1.get();
    }

    if(doubleBufferOffsetMode_800c34d6.get() == 0) {
      setScreenOffset((int)(x + clipX), (int)(y + clipY));

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

  @Method(0x8003c2d0L)
  public static void updateDrawEnvClip() {
    DRAWENV_800c3450.clip.set(
      (short)(displayRect_800c34c8.x.get() + (doubleBufferFrame_800c34d4.get() == 0 ? clip_800c3440.x1 : clip_800c3440.x2).get()),
      (short)(displayRect_800c34c8.y.get() + (doubleBufferFrame_800c34d4.get() == 0 ? clip_800c3440.y1 : clip_800c3440.y2).get()),
      displayRect_800c34c8.w.get(),
      displayRect_800c34c8.h.get()
    );

    PutDrawEnv(DRAWENV_800c3450);
  }

  @Method(0x8003c540L)
  public static void setClip(final short x1, final short y1, final short x2, final short y2) {
    clip_800c3440.set(x1, x2, y1, y2);

    if(doubleBufferOffsetMode_800c34d6.get() == 0) {
      //LAB_8003c598
      clip_800c3448.set(x1, x2, y1, y2);
    } else {
      clip_800c3448.set((short)0, (short)0, (short)0, (short)0);
    }

    //LAB_8003c5b8
    updateDrawEnvClip();
    FUN_8003c1c0();
  }

  @Method(0x8003c5e0L)
  public static void FUN_8003c5e0() {
    _1f8003dc.setu(displayWidth_1f8003e0.get() / 2);
    _1f8003de.setu(displayHeight_1f8003e4.get() / 2);
    FUN_8003c1c0();
    _800c34d8.setu(0x3fff);
    _800c34dc.setu(0);
    _800c34e0.setu(10);
  }

  @Method(0x8003cda0L)
  public static void initGraphics() {
    initGte();
    setFarColour(0, 0, 0);
    setScreenOffset(0, 0);
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

  @Method(0x8003e958L)
  public static void initGte() {
    patchC0TableAgain();

    // Set bit 30 of status register to enable GTE
    CPU.MTC0(CpuRegisterType.SR, CPU.MFC0(CpuRegisterType.SR) | 0x40000000);

    // cop2r61 = 0x155, 61 = ZSF3 - Z3 avg scale factor (normally 1/3)
    CPU.CTC2(341, 0x1d);
    // cop2r62 = 0x100, 62 = ZSF4 - Z4 avg scale factor (normally 1/4)
    CPU.CTC2(256, 0x1e);
    // cop2r58 = 0x3e8, 58 = H - Projection plane distance
    CPU.CTC2(1000, 0x1a);
    // cop2r59 = 0xffffef9e, 59 = DQA - Depth queueing param A (coefficient)
    CPU.CTC2(0xffffef9e, 0x1b);
    // cop2r60 = 0x1400000, 60 = DQB - Depth queueing param B (offset)
    CPU.CTC2(0x1400000, 0x1c);
    // cop2r56 = 0, 56 = OFX - Screen offset X
    CPU.CTC2(0, 0x18);
    // cop2r57 = 0, 57 = OFY - Screen offset Y
    CPU.CTC2(0, 0x19);
  }

  @Method(0x8003f8d0L)
  public static void setFarColour(final int r, final int g, final int b) {
    CPU.CTC2(r * 16L, 0x15);
    CPU.CTC2(g * 16L, 0x16);
    CPU.CTC2(b * 16L, 0x17);
  }

  @Method(0x8003f8f0L)
  public static void setProjectionPlaneDistance(final int distance) {
    CPU.CTC2(distance, 0x1a);
  }
}
