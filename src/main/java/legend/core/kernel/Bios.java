package legend.core.kernel;

import legend.core.DebugHelper;
import legend.core.cdrom.CdlLOC;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ProcessControlBlock;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.ThreadControlBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CDROM;
import static legend.core.Hardware.CPU;
import static legend.core.Hardware.ENTRY_POINT;
import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.InterruptController.I_MASK;
import static legend.core.InterruptController.I_STAT;
import static legend.core.dma.DmaManager.DMA_DICR;
import static legend.core.dma.DmaManager.DMA_DPCR;
import static legend.core.kernel.Kernel.CloseEvent_Impl_B09;
import static legend.core.kernel.Kernel.DeliverEvent_Impl_B07;
import static legend.core.kernel.Kernel.EnableEvent_Impl_B0c;
import static legend.core.kernel.Kernel.EnqueueSyscallHandler_Impl_C01;
import static legend.core.kernel.Kernel.EnqueueTimerAndVblankIrqs_Impl_C00;
import static legend.core.kernel.Kernel.FileClose_Impl_B36;
import static legend.core.kernel.Kernel.FileOpen_Impl_B32;
import static legend.core.kernel.Kernel.FileRead_Impl_B34;
import static legend.core.kernel.Kernel.InitDefInt_Impl_C0c;
import static legend.core.kernel.Kernel.InstallExceptionHandlers_Impl_C07;
import static legend.core.kernel.Kernel.OpenEvent_Impl_B08;
import static legend.core.kernel.Kernel.ReturnFromException_Impl_B17;
import static legend.core.kernel.Kernel.SetDefaultExitFromException_Impl_B18;
import static legend.core.kernel.Kernel.SysDeqIntRP_Impl_C03;
import static legend.core.kernel.Kernel.SysEnqIntRP_Impl_C02;
import static legend.core.kernel.Kernel.SysInitMemory_Impl_C08;
import static legend.core.kernel.Kernel.TestEvent_Impl_B0b;
import static legend.core.kernel.Kernel.UnDeliverEvent_Impl_B20;
import static legend.core.kernel.Kernel.alloc_kernel_memory_Impl_B00;

public final class Bios {
  private Bios() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Bios.class);

  public static final Pointer<ArrayRef<Pointer<legend.core.kernel.PriorityChainEntry>>> ExceptionChainPtr_a0000100 = MEMORY.ref(4, 0xa0000100L, Pointer.of(0x20, ArrayRef.of(Pointer.classFor(legend.core.kernel.PriorityChainEntry.class), 4, 4, 8, Pointer.of(0x10, legend.core.kernel.PriorityChainEntry::new))));
  public static final Value ExceptionChainSize_a0000104 = MEMORY.ref(4, 0xa0000104L);
  public static final Pointer<ProcessControlBlock> ProcessControlBlockPtr_a0000108 = MEMORY.ref(4, 0xa0000108L, Pointer.of(4, ProcessControlBlock::new));
  public static final Value ProcessControlBlockSize_a000010c = MEMORY.ref(4, 0xa000010cL);
  public static final Value ThreadControlBlockAddr_a0000110 = MEMORY.ref(4, 0xa0000110L);
  public static final Value ThreadControlBlockSize_a0000114 = MEMORY.ref(4, 0xa0000114L);

  public static final Value EventControlBlockAddr_a0000120 = MEMORY.ref(4, 0xa0000120L);
  public static final Value EventControlBlockSize_a0000124 = MEMORY.ref(4, 0xa0000124L);

  public static final Value kernelStart_a0000500 = MEMORY.ref(4, 0xa0000500L);

  public static final Value randSeed_a0009010 = MEMORY.ref(4, 0xa0009010L);

  public static final Value _a0009154 = MEMORY.ref(4, 0xa0009154L);
  public static final Value _a0009158 = MEMORY.ref(4, 0xa0009158L);
  public static final Value _a000915c = MEMORY.ref(4, 0xa000915cL);
  public static final Value _a0009160 = MEMORY.ref(4, 0xa0009160L);

  public static final Value _a00091c4 = MEMORY.ref(4, 0xa00091c4L);
  public static final Value _a00091c8 = MEMORY.ref(4, 0xa00091c8L);
  public static final Value _a00091cc = MEMORY.ref(4, 0xa00091ccL);
  public static final Value _a00091f0 = MEMORY.ref(4, 0xa00091f0L);

  public static final Value _a00091fc = MEMORY.ref(4, 0xa00091fcL);

  public static final Value _a000958c = MEMORY.ref(4, 0xa000958cL);

  public static final Value _a00095b0 = MEMORY.ref(4, 0xa00095b0L);

  public static final Value _a00095bc = MEMORY.ref(4, 0xa00095bcL);

  public static final Value _a0009d70 = MEMORY.ref(4, 0xa0009d70L);
  public static final Value _a0009d74 = MEMORY.ref(4, 0xa0009d74L);
  public static final Value _a0009d78 = MEMORY.ref(4, 0xa0009d78L);
  public static final Value _a0009d7c = MEMORY.ref(4, 0xa0009d7cL);
  public static final Value _a0009d80 = MEMORY.ref(1, 0xa0009d80L);

  public static final Value _a0009e00 = MEMORY.ref(4, 0xa0009e00L);
  public static final Value _a0009e04 = MEMORY.ref(4, 0xa0009e04L);
  public static final Value _a0009e08 = MEMORY.ref(4, 0xa0009e08L);

  public static final Value _a0009e10 = MEMORY.ref(4, 0xa0009e10L);

  public static final Value _a0009e18 = MEMORY.ref(1, 0xa0009e18L);

  public static final Value _a000b070 = MEMORY.ref(4, 0xa000b070L);
  public static final Value _a000b071 = MEMORY.ref(1, 0xa000b071L);

  public static final Value _a000b078 = MEMORY.ref(4, 0xa000b078L);

  public static final Value _a000b080 = MEMORY.ref(1, 0xa000b080L);

  public static final Value _a000b0c0 = MEMORY.ref(4, 0xa000b0c0L);

  public static final Value _a000b0f4 = MEMORY.ref(4, 0xa000b0f4L);

  public static final Value _a000b0fc = MEMORY.ref(4, 0xa000b0fcL);

  public static final Value _a000b10e = MEMORY.ref(1, 0xa000b10eL);
  public static final Value _a000b10f = MEMORY.ref(1, 0xa000b10fL);
  public static final Value _a000b110 = MEMORY.ref(1, 0xa000b110L);
  public static final Value _a000b111 = MEMORY.ref(1, 0xa000b111L);

  public static final legend.core.kernel.EXEC exe_a000b870 = MEMORY.ref(4, 0xa000b870L, legend.core.kernel.EXEC::new);

  public static final Value exeName_a000b8b0 = MEMORY.ref(1, 0xa000b8b0L);

  public static final Value _a000b938 = MEMORY.ref(4, 0xa000b938L);
  public static final Value _a000b93c = MEMORY.ref(4, 0xa000b93cL);

  public static final Value kernelMemoryStart_a000e000 = MEMORY.ref(1, 0xa000e000L);

  public static final Value kernelStartRom_bfc10000 = MEMORY.ref(4, 0xbfc10000L);

  @Method(0xbfc00000L)
  public static void main() {
    LOGGER.info("Executing BIOS");

    bootstrapExecutable("cdrom:SYSTEM.CNF;1", "cdrom:PSX.EXE;1");
  }

  /**
   * See no$ "BIOS Memory Map"
   */
  @Method(0xbfc00420L)
  public static void copyKernelSegment2() {
    //LAB_bfc00434
    MEMORY.memcpy(kernelStart_a0000500.getAddress(), kernelStartRom_bfc10000.getAddress(), 0x8bf0);
    MEMORY.addFunctions(Kernel.class);
  }

  @Method(0xbfc01accL)
  public static long bzero_Impl_A28(final long dst, final int size) {
    if(dst == 0 || size <= 0) {
      return 0;
    }

    MEMORY.waitForLock(() -> {
      long dest = dst;
      long s = size;
      MEMORY.disableAlignmentChecks();
      for(; s >= 8; s -= 8, dest += 8) {
        MEMORY.set(dest, 8, 0);
      }
      MEMORY.enableAlignmentChecks();

      for(int i = 0; i < s; i++) {
        MEMORY.set(dst, (byte)0);
      }
    });

    return dst;
  }

  @Method(0xbfc02200L)
  public static int rand_Impl_A2f() {
    final long v1 = (randSeed_a0009010.get() * 0x41c6_4e6dL & 0xffff_ffffL) + 0x3039L;
    final long v0 = v1 >>> 16;
    randSeed_a0009010.setu(v1);
    return (int)(v0 & 0x7fff);
  }

  @Method(0xbfc02230L)
  public static void srand_Impl_A30(final long seed) {
    randSeed_a0009010.setu(seed);
  }

  @Method(0xbfc02240L)
  public static void setjmp_Impl_A13(final legend.core.kernel.jmp_buf buffer, final RunnableRef callback) {
    buffer.set(callback);
  }

  @Method(0xbfc0227cL)
  public static int longjmp_Impl_A14(final jmp_buf buffer, final int value) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0xbfc02918L)
  public static int abs_Impl_A0e(final int val) {
    return Math.abs(val);
  }

  @Method(0xbfc02b50L)
  public static long memcpy_Impl_A2a(final long dst, final long src, final int size) {
    if(dst == 0) {
      return 0;
    }

    if(size <= 0) {
      return dst;
    }

    MEMORY.memcpy(dst, src, size);
    return dst;
  }

  @Method(0xbfc03288L)
  public static int strcmp_Impl_A17(final String s1, final String s2) {
    return s1.compareToIgnoreCase(s2);
  }

  @Method(0xbfc03310L)
  public static int strncmp_Impl_A18(final String s1, final String s2, final int n) {
    return s1.substring(0, Math.min(s1.length(), n)).compareToIgnoreCase(s2.substring(0, Math.min(s2.length(), n)));
  }

  @Method(0xbfc033c8L)
  @Nullable
  public static CString strcpy_Impl_A19(@Nullable final CString dest, @Nullable final String src) {
    if(dest == null || src == null) {
      return null;
    }

    dest.set(src);
    return dest;
  }

  @Method(0xbfc03418L)
  @Nullable
  public static CString strncpy_Impl_A1a(@Nullable final CString str1, @Nullable final CString str2, final int len) {
    if(str1 == null || str2 == null) {
      //LAB_bfc03428
      return null;
    }

    //LAB_bfc03430
    long a0 = str1.getAddress();
    long a1 = str2.getAddress();

    //LAB_bfc0343c
    for(int i = 0; i < len; i++) {
      final long a3 = MEMORY.ref(1, a1).getSigned();
      MEMORY.ref(1, a0).setu(a3);

      a0++;
      a1++;

      if(a3 == 0) {
        i++;
        //LAB_bfc03460
        while(i < len) {
          MEMORY.ref(1, a0).setu(0);
          i++;
          a0++;
        }

        //LAB_bfc03474
        return MEMORY.ref(len, str1.getAddress(), CString::new);
      }

      //LAB_bfc0347c
    }

    //LAB_bfc03488
    return MEMORY.ref(len, str1.getAddress(), CString::new);
  }

  @Method(0xbfc03a18L)
  public static boolean LoadExeFile_Impl_A42(final String filename, final long header) {
    final int fd = open(filename, 1);
    if(fd < 0) {
      return false;
    }

    //LAB_bfc03a3c
    if(!readExeHeader(fd, header)) {
      close(fd);
      return false;
    }

    //LAB_bfc03a68
    read(fd, MEMORY.ref(4, header).offset(0x8L).get(), (int)MEMORY.ref(4, header).offset(0xcL).get());
    close(fd);

    //LAB_bfc03a94
    return true;
  }

  @Method(0xbfc03c90L)
  public static boolean readExeHeader(final int fd, final long header) {
    final int bytesRead = read(fd, _a000b070.getAddress(), 0x800);

    if(bytesRead >= 0x800) {
      memcpy_Impl_A2a(header, _a000b080.getAddress(), 0x3c);
      return true;
    }

    return false;
  }

  private static boolean entrypointInitialized;

  @Method(0xbfc03cf0L)
  public static long Exec_Impl_A43(final EXEC header, final int argc, final long argv) {
    final Value entry = MEMORY.ref(4, header.pc0.get());

    GATE.release();

    if(ENTRY_POINT != null && !entrypointInitialized) {
      MEMORY.addFunctions(ENTRY_POINT);
      entrypointInitialized = true;
    }

    entry.cast(BiConsumerRef::new).run(argc, argv);

    GATE.acquire();

    return 0x1L;
  }

  @Method(0xbfc04610L)
  public static long allocateExceptionChain(final int count) {
    final int size = count * 8;
    final long mem = alloc_kernel_memory(size);

    if(mem == 0) {
      return 0;
    }

    //LAB_bfc04640
    bzero_Impl_A28(mem, size);
    ExceptionChainPtr_a0000100.set(MEMORY.ref(4, mem, ArrayRef.of(Pointer.classFor(legend.core.kernel.PriorityChainEntry.class), 4, 4, 8, Pointer.of(0x10, legend.core.kernel.PriorityChainEntry::new))));
    ExceptionChainSize_a0000104.setu(size);

    //LAB_bfc04668
    return size;
  }

  @Method(0xbfc04678L)
  public static long allocateEventControlBlock(final int count) {
    LOGGER.info("");
    LOGGER.info("Configuration : EvCB\t0x%02x\t\t", count);

    final int size = count * 28;
    final long addr = alloc_kernel_memory(size);
    if(addr == 0) {
      return 0;
    }

    EventControlBlockAddr_a0000120.setu(addr);
    EventControlBlockSize_a0000124.setu(size);

    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, addr).offset(i * 28L).offset(0x4L).setu(0);
    }

    return size;
  }

  @Method(0xbfc0472cL)
  public static long allocateThreadControlBlock(final int processCount, final int threadCount) {
    LOGGER.info("TCB\t0x%02x", threadCount);

    final int processSize = processCount * 0x4;
    final int threadSize = threadCount * 0xc0;

    ProcessControlBlockSize_a000010c.setu(processSize);
    ThreadControlBlockSize_a0000114.setu(threadSize);

    final long processAddr = alloc_kernel_memory(processSize);
    if(processAddr == 0) {
      return 0;
    }

    //LAB_bfc04798
    final long threadAddr = alloc_kernel_memory(threadSize);
    if(threadAddr == 0) {
      return 0;
    }

    final ProcessControlBlock pcb = MEMORY.ref(4, processAddr, ProcessControlBlock::new);
    final ThreadControlBlock tcb = MEMORY.ref(4, threadAddr, ThreadControlBlock::new);

    //LAB_bfc047b8
    //LAB_bfc047d4
    for(int i = 0; i < processCount; i++) {
      MEMORY.ref(4, processAddr).offset(i * 4L).setu(0);
    }

    //LAB_bfc047e8
    //LAB_bfc0480c
    for(int i = 0; i < threadCount; i++) {
      MEMORY.ref(4, threadAddr).offset(i * 0xc0L).setu(0x1000L); // Uninitialized
    }

    //LAB_bfc0481c
    tcb.status.set(0x4000L); // Initialized
    pcb.threadControlBlockPtr.set(tcb);
    ProcessControlBlockPtr_a0000108.set(pcb);
    ThreadControlBlockAddr_a0000110.setu(threadAddr);

    //LAB_bfc0483c
    return processSize + threadSize;
  }

  @Method(0xbfc04910L)
  public static boolean CdInitSubFunc_Impl_A95() {
    _a00091c4.setu(0);
    _a00091c8.setu(0);
    _a00091cc.setu(0);

    EnterCriticalSection();

    I_MASK.and(0xfffffffbL);
    I_MASK.and(0xfffffff7L);
    I_STAT.setu(0xfffffffbL);
    I_STAT.setu(0xfffffff7L);

    _a000b938.setu(0x1L);
    _a000b93c.setu(0x1L);

    DMA_DPCR.setu(0x9099L);
    DMA_DICR.setu(0x0800_0000L | DMA_DICR.get(0xff_ffffL));

    _a0009154.setu(0xffffL);
    _a0009158.setu(0xffffL);
    _a000915c.setu(0);
    _a0009160.setu(0);

    I_STAT.setu(0xfffffffbL);

    ExitCriticalSection();

    return true;
  }

  @Method(0xbfc067e8L)
  public static void bootstrapExecutable(final String cnf, final String exe) {
    LOGGER.info("Bootstrapping %s / %s", exe, cnf);

    CPU.R12_SR.resetIEc();
    CPU.R12_SR.setIm(CPU.R12_SR.getIm() & 0xffff_fbfeL);

    copyKernelSegment2();

    InstallExceptionHandlers();
    SetDefaultExitFromException();

    I_MASK.setu(0);
    I_STAT.setu(0);

    SysInitMemory(kernelMemoryStart_a000e000.getAddress(), 0x2000);
    allocateExceptionChain(4);
    EnqueueSyscallHandler(0);
    InitDefInt(3);
    allocateEventControlBlock(10);
    allocateThreadControlBlock(1, 4);
    EnqueueTimerAndVblankIrqs(1);

    I_MASK.setu(0);
    I_STAT.setu(0);
    CdInit_Impl_A54();

    //LAB_bfc06a3c
    //LAB_bfc06af4
    exeName_a000b8b0.set("\\SCUS_944.91;1");

    //LAB_bfc06b7c
    //LAB_bfc06bb4
    if(!LoadExeFile_Impl_A42(exeName_a000b8b0.getString(), exe_a000b870.getAddress())) {
      throw new RuntimeException("Failed to load exe");
    }

    //LAB_bfc06be0
    EnterCriticalSection();

    //LAB_bfc06c6c
    Exec_Impl_A43(exe_a000b870, 1, 0);
    LOGGER.info("Exiting");
    System.exit(0);
  }

  @Method(value = 0xbfc06fdcL, ignoreExtraParams = true)
  public static int noop() {
    return 0;
  }

  @Method(0xbfc073a0L)
  public static void CdInit_Impl_A54() {
    CdInitSubFunc();
    cdromPostInit();
  }

  @Method(0xbfc07410L)
  public static long cdromPostInit() {
    CDROM.readFromDisk(new CdlLOC().unpack(0x10L), 1, _a000b070.getAddress());

    //LAB_bfc0744c
    if(strncmp_Impl_A18(_a000b071.getString(), "CD001", 5) != 0) {
      assert false : "Bad CDROM sector";
      return 0;
    }

    //LAB_bfc07474
    _a0009d70.setu(_a000b0f4);
    _a0009d74.setu(_a000b0fc);
    _a0009d78.setu(_a000b111.get() << 24 | _a000b110.get() << 16 | _a000b10f.get() << 8 | _a000b10e.get());
    _a0009d7c.setu(_a000b0c0);

    CDROM.readFromDisk(new CdlLOC().unpack(_a000b0fc.get()), 1, _a000b070.getAddress());

    //LAB_bfc07500
    //LAB_bfc07518
    for(int i = 0; i < 0x800; i += 4) {
      _a0009d7c.xoru(_a000b070.offset(i));
    }

    long s0 = _a000b070.getAddress();
    long s2 = _a00095b0.getAddress();
    long s3 = 0x1L;

    //LAB_bfc07560
    do {
      if(MEMORY.ref(1, s0).get() == 0) {
        break;
      }

      //LAB_bfc07580
      long v0 = MEMORY.ref(1, s0).offset(0x5L).get() << 24 | MEMORY.ref(1, s0).offset(0x4L).get() << 16 | MEMORY.ref(1, s0).offset(0x3L).get() << 8 | MEMORY.ref(1, s0).offset(0x2L).get();
      MEMORY.ref(4, s2).offset(0x8L).setu(v0);
      MEMORY.ref(4, s2).setu(s3);
      MEMORY.ref(4, s2).offset(0x4L).setu(MEMORY.ref(1, s0).offset(0x6L).get() + MEMORY.ref(1, s0).offset(0x7L).get() & 0xffffL);

      final long size = MEMORY.ref(1, s0).get();
      memcpy_Impl_A2a(_a00095bc.getAddress(), _a000b078.getAddress(), (int)size);

      MEMORY.ref(1, s2).offset(size).offset(0xcL).setu(0);

      v0 = MEMORY.ref(1, s0).get();
      s0 += v0;
      if((v0 & 0x1L) == 0x1L) {
        s0 += 0x9L;
      } else {
        //LAB_bfc07604
        s0 += 0x8L;
      }

      //LAB_bfc0760c
      s2 += 0x2cL;
      s3++;
    } while(s0 < exe_a000b870.getAddress() && s3 != 0x2dL);

    //LAB_bfc07620
    //LAB_bfc07630
    _a0009e00.setu(s3 - 0x1L);
    _a0009e08.setu(0);

    //LAB_bfc07650
    return 0x1L;
  }

  @Method(0xbfc07664L)
  public static long FUN_bfc07664(final long a0, final String a1) {
    long s0 = _a00095b0.getAddress();
    long s1 = 0;

    //LAB_bfc0769c
    while(s1 < _a0009e00.get()) {
      if(MEMORY.ref(4, s0).offset(0x4L).get() == a0) {
        if(strcmp_Impl_A17(a1, MEMORY.ref(1, s0 + 0xcL).getString()) == 0) {
          return s1 + 0x1L;
        }
      }

      //LAB_bfc076c8
      s0 += 0x2cL;
      s1++;
    }

    //LAB_bfc076e0
    //LAB_bfc076e4
    return -0x1L;
  }

  @Method(0xbfc07700L)
  public static long FUN_bfc07700(final long a0) {
    if(a0 == _a0009e08.get()) {
      return 0x1L;
    }

    //LAB_bfc07720
    CDROM.readFromDisk(new CdlLOC().unpack(_a000958c.offset(a0 * 44).get()), 1, _a000b070.getAddress());

    //LAB_bfc07774
    long s1 = _a000b070.getAddress();
    long s2 = _a00091f0.getAddress();
    long count = 0;

    //LAB_bfc077a8
    do {
      if(MEMORY.ref(1, s1).get() == 0) {
        break;
      }

      //LAB_bfc077c8
      MEMORY.ref(4, s2).offset(0x4L).setu(MEMORY.ref(1, s1).offset(0x5L).get() << 24 | MEMORY.ref(1, s1).offset(0x4L).get() << 16 | MEMORY.ref(1, s1).offset(0x3L).get() << 8 | MEMORY.ref(1, s1).offset(0x2L).get());
      MEMORY.ref(4, s2).offset(0x8L).setu(MEMORY.ref(1, s1).offset(0xdL).get() << 24 | MEMORY.ref(1, s1).offset(0xcL).get() << 16 | MEMORY.ref(1, s1).offset(0xbL).get() << 8 | MEMORY.ref(1, s1).offset(0xaL).get());

      final int size = (int)MEMORY.ref(1, s1).offset(0x20L).get();
      memcpy_Impl_A2a(s2 + 0xcL, s1 + 0x21L, size);

      MEMORY.ref(1, s2).offset(size).offset(0xcL).setu(0);
      s1 += MEMORY.ref(1, s1).get();
      s2 += 0x18L;
      count++;
    } while(s2 < _a00095b0.getAddress() && s1 < exe_a000b870.getAddress());

    //LAB_bfc07860
    //LAB_bfc07870
    _a0009e08.setu(a0);
    _a0009e04.setu(count);

    //LAB_bfc07894
    return 0x1L;
  }

  @Method(0xbfc078a4L)
  public static int dev_cd_open_Impl_A5f(final long fcb, final String path, final int mode) {
    //LAB_bfc078f8
    final char[] temp = new char[path.length()];

    int s0 = 0;
    int s1 = 0;
    char c = Character.toUpperCase(path.charAt(0));
    temp[0] = c;

    //LAB_bfc07910
    while(c != 0 && s1 < path.length() - 1) {
      s1++;
      s0++;
      c = Character.toUpperCase(path.charAt(s1));
      temp[s0] = c;
    }

    //LAB_bfc07928
    s1 = 0;
    c = temp[s1];

    //LAB_bfc07940
    while(c != '\0' && c != ';') {
      s1++;
      c = temp[s1];
    }

    //LAB_bfc07958
    final String str;
    if(path.charAt(s1) == '\0') {
      str = new String(temp) + ";1";
    } else {
      str = new String(temp);
    }

    //LAB_bfc0797c
    final long v0 = FUN_bfc083cc(0, str);

    //LAB_bfc079a0
    final long v1 = _a00091f0.offset(v0 * 24).getAddress();
    MEMORY.ref(4, fcb).offset(0x24L).setu(MEMORY.ref(4, v1).offset(0x4L));
    MEMORY.ref(4, fcb).offset(0x20L).setu(MEMORY.ref(4, v1).offset(0x8L));
    MEMORY.ref(4, fcb).offset(0x10L).setu(0);
    MEMORY.ref(4, fcb).offset(0x18L).setu(0);
    MEMORY.ref(4, fcb).offset(0x4L).setu(_a0009d7c);

    //LAB_bfc079e0
    return 0;
  }

  @Method(0xbfc07a04L)
  public static int dev_cd_read_Impl_A60(final long fcb, final long dest, final int length) {
    //LAB_bfc07a34
    //LAB_bfc07aec
    //LAB_bfc07b08
    final int sectors = length / 0x800;

    CDROM.readFromDisk(new CdlLOC().unpack(MEMORY.ref(4, fcb).offset(0x24L).get() + MEMORY.ref(4, fcb).offset(0x10L).get() / 0x800L), sectors, dest);

    //LAB_bfc07b40
    final long v = MEMORY.ref(4, fcb).offset(0x10L).get();
    final long v0 = MEMORY.ref(4, fcb).offset(0x20L).get();
    final long v1;
    if(v0 < v + length) {
      v1 = v0 - v;
    } else {
      v1 = length;
    }

    //LAB_bfc07b68
    //LAB_bfc07b6c
    MEMORY.ref(4, fcb).offset(0x10L).setu(v + v1);

    //LAB_bfc07b78
    return (int)v1;
  }

  @Method(0xbfc08020L)
  public static long FUN_bfc08020(long a0, final String a1) {
    //LAB_bfc0803c
    long v0 = MEMORY.ref(1, a0).get();
    int charIndex = 0;
    while(v0 != 0) {
      final char c = a1.charAt(charIndex);
      if(c != '?' && c != v0) {
        return 0;
      }

      //LAB_bfc0805c
      v0 = MEMORY.ref(1, a0).offset(0x1L).get();
      charIndex++;
      a0++;
    }

    //LAB_bfc0806c
    final char c = a1.charAt(charIndex);
    if(c != '\0' && c != '?') {
      //LAB_bfc08090
      return 0;
    }

    //LAB_bfc08084
    return 0x1L;
  }

  @Method(0xbfc083ccL)
  public static long FUN_bfc083cc(final int a0, final String a1) {
    final String str;
    if(a1.charAt(0) == '\\') {
      //LAB_bfc08440
      str = a1;
    } else {
      str = _a0009d80.getString() + '\\' + a1;
    }

    _a0009e18.set(str);

    //LAB_bfc08454
    final char[] out = new char[str.length()];
    int outIndex;

    int index = 1;
    out[0] = '\0';
    long s4 = 0x1L;
    long s3 = 0;

    //LAB_bfc0846c
    do {
      char c = (char)_a0009e18.offset(index).get();
      outIndex = 0;

      //LAB_bfc08484
      while(c != '\0' && c != '\\') {
        out[outIndex] = c;
        index++;
        c = (char)_a0009e18.offset(index).get();
        outIndex++;
      }

      //LAB_bfc084a0
      if(c == '\0') {
        break;
      }

      index++;
      out[outIndex] = 0;
      final long v0 = FUN_bfc07664(s4, new String(out));
      s4 = v0;
      if(v0 == -0x1L) {
        out[0] = 0;
        break;
      }

      //LAB_bfc084cc
      s3++;
    } while(s3 < 0x8L);

    //LAB_bfc084d8
    //LAB_bfc084dc
    if(s3 >= 0x8L) {
      return -0x1L;
    }

    //LAB_bfc084f0
    if(out[0] == '\0') {
      return -0x1L;
    }

    //LAB_bfc08504
    out[outIndex] = '\0';
    if(FUN_bfc07700(s4) == 0) {
      return -0x1L;
    }

    //LAB_bfc08520
    final long s1 = _a0009e04.get();
    s3 = a0;
    if(a0 >= s1) {
      return -0x1L;
    }

    long addr = _a00091fc.offset(s3 * 24).getAddress();

    //LAB_bfc08554
    do {
      if(FUN_bfc08020(addr, new String(out)) != 0) {
        _a0009e10.setu(s3);
        return s3;
      }

      //LAB_bfc08574
      s3++;
      addr += 0x18L;
    } while(s3 < s1);

    //LAB_bfc08588
    return -0x1L;
  }

  @Method(0xbfc0d890L)
  public static int open(final String name, final int mode) {
    return FileOpen_Impl_B32(name, mode);
  }

  @Method(0xbfc0d8a0L)
  public static void close(final int fd) {
    FileClose_Impl_B36(fd);
  }

  @Method(0xbfc0d8b0L)
  public static int read(final int fd, final long buf, final int size) {
    return FileRead_Impl_B34(fd, buf, size);
  }

  @Method(0xbfc0d8c0L)
  public static void ExitCriticalSection() {
    CPU.SYSCALL(2);
  }

  @Method(0xbfc0d8e0L)
  public static void SystemErrorUnresolvedException_Impl_A40() {
    // Normally does infinite loop: functionVectorA_000000a0.run(0x40L, EMPTY_OBJ_ARRAY);
    LOGGER.error("Unresolved exception", new Throwable());
    DebugHelper.pause();
  }

  @Method(0xbfc0d950L)
  public static void SystemErrorBootOrDiskFailure_Impl_Aa1(final char type, final int errorCode) {
    // Normally does infinite loop: functionVectorA_000000a0.run(0xa1L, new Object[] {type, errorCode});
    LOGGER.error("Boot failure %s: %08x", type, errorCode);
    DebugHelper.pause();
  }

  @Method(0xbfc0d960L)
  public static boolean EnterCriticalSection() {
    CPU.SYSCALL(1);

    // The exception handler stores v0 (return value) here
    GATE.acquire();
    final boolean ret = ProcessControlBlockPtr_a0000108.deref().threadControlBlockPtr.deref().registers.get(1).get() != 0;
    GATE.release();
    return ret;
  }

  @Method(0xbfc0d970L)
  public static void DeliverEvent(final long cls, final int spec) {
    DeliverEvent_Impl_B07(cls, spec);
  }

  @Method(0xbfc0d980L)
  public static void ReturnFromException() {
    ReturnFromException_Impl_B17();
  }

  @Method(0xbfc0d990L)
  public static void UnDeliverEvent(final long cls, final int spec) {
    UnDeliverEvent_Impl_B20(cls, spec);
  }

  @Method(0xbfc0d9a0L)
  public static void SetDefaultExitFromException() {
    SetDefaultExitFromException_Impl_B18();
  }

  @Method(0xbfc0d9b0L)
  public static long OpenEvent(final long cls, final int spec, final int mode, final long func) {
    return OpenEvent_Impl_B08(cls, spec, mode, func);
  }

  @Method(0xbfc0d9c0L)
  public static void EnableEvent(final long event) {
    EnableEvent_Impl_B0c(event);
  }

  @Method(0xbfc0d9d0L)
  public static void CloseEvent(final int event) {
    CloseEvent_Impl_B09(event);
  }

  @Method(0xbfc0d9e0L)
  public static int TestEvent(final long event) {
    return TestEvent_Impl_B0b(event);
  }

  @Method(0xbfc0dae0L)
  public static long alloc_kernel_memory(final int size) {
    return alloc_kernel_memory_Impl_B00(size);
  }

  @Method(0xbfc0daf0L)
  public static long SysEnqIntRP(final int priority, final legend.core.kernel.PriorityChainEntry struct) {
    return SysEnqIntRP_Impl_C02(priority, struct);
  }

  @Method(0xbfc0db00L)
  public static legend.core.kernel.PriorityChainEntry SysDeqIntRP(final int priority, final PriorityChainEntry struct) {
    return SysDeqIntRP_Impl_C03(priority, struct);
  }

  @Method(0xbfc0db20L)
  public static void InstallExceptionHandlers() {
    InstallExceptionHandlers_Impl_C07();
  }

  @Method(0xbfc0db40L)
  public static void SysInitMemory(final long address, final int size) {
    SysInitMemory_Impl_C08(address, size);
  }

  @Method(0xbfc0db50L)
  public static long EnqueueSyscallHandler(final int priority) {
    return EnqueueSyscallHandler_Impl_C01(priority);
  }

  @Method(0xbfc0db60L)
  public static long InitDefInt(final int priority) {
    return InitDefInt_Impl_C0c(priority);
  }

  @Method(0xbfc0db70L)
  public static void EnqueueTimerAndVblankIrqs(final int priority) {
    EnqueueTimerAndVblankIrqs_Impl_C00(priority);
  }

  @Method(0xbfc0dc10L)
  public static boolean CdInitSubFunc() {
    return CdInitSubFunc_Impl_A95();
  }
}
