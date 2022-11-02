package legend.core;

import legend.core.cdrom.CdDrive;
import legend.core.dma.DmaManager;
import legend.core.gpu.Gpu;
import legend.core.kernel.Bios;
import legend.core.mdec.Mdec;
import legend.core.memory.Memory;
import legend.core.memory.segments.PrivilegeGate;
import legend.core.memory.segments.RamSegment;
import legend.core.memory.types.RunnableRef;
import legend.core.spu.Spu;
import legend.game.Scus94491;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public final class Hardware {
  private Hardware() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Hardware.class);

  public static final Memory MEMORY = new Memory();
  public static final PrivilegeGate GATE = new PrivilegeGate();

  public static final legend.core.Cpu CPU;
  public static final InterruptController INTERRUPTS;
  public static final DmaManager DMA;
  public static final Gpu GPU;
  public static final Mdec MDEC;
  public static final CdDrive CDROM;
  public static final Spu SPU;

  public static final Thread codeThread;
  public static final Thread hardwareThread;
  public static final Thread gpuThread;
  public static final Thread spuThread;

  @Nullable
  public static final Class<?> ENTRY_POINT;

  public static boolean dumping;
  public static boolean hardwareWaiting;
  public static boolean timerWaiting;
  public static boolean spuWaiting;
  private static final List<Runnable> loadStateListeners = new ArrayList<>();

  private static void dumpLock() {
    dumping = true;

    while(!hardwareWaiting || !timerWaiting || !spuWaiting) {
      DebugHelper.sleep(1);
    }
  }

  private static void dumpUnlock() {
    dumping = false;
  }

  public static void dump(final ByteBuffer stream) throws IOException {
    dumpLock();

    stream.put((byte)'d');
    stream.put((byte)'d');
    stream.put((byte)'m');
    stream.put((byte)'p');
    stream.put((byte)4);

    MEMORY.dump(stream);
    CPU.dump(stream);
    INTERRUPTS.dump(stream);
    DMA.dump(stream);
    GPU.dump(stream);
    MDEC.dump(stream);
    CDROM.dump(stream);
    SPU.dump(stream);

    dumpUnlock();
  }

  public static void load(final ByteBuffer stream) throws ClassNotFoundException, IOException {
    dumpLock();

    if(stream.get() != 'd' || stream.get() != 'd' || stream.get() != 'm' || stream.get() != 'p') {
      LOGGER.error("Failed to load state: invalid file");
      dumpUnlock();
      return;
    }

    final int version = stream.get();

    if(version < 4) {
      LOGGER.error("Failed to load state: version %d too old", version);
    }

    if(version > 4) {
      LOGGER.error("Failed to load state: version %d too new", version);
    }

    // Need to acquire gate to load kernel/bios functions
    GATE.acquire();
    MEMORY.load(stream, version);
    CPU.load(stream, version);
    INTERRUPTS.load(stream, version);
    DMA.load(stream, version);
    GPU.load(stream, version);
    MDEC.load(stream, version);
    CDROM.load(stream, version);
    SPU.load(stream, version);
    GATE.release();

    loadStateListeners.forEach(Runnable::run);

    dumpUnlock();
  }

  public static void registerLoadStateListener(final Runnable listener) {
    loadStateListeners.add(listener);
  }

  static {
    try {
      if(!Config.exists()) {
        Config.save();
      } else {
        Config.load();
      }
    } catch(final IOException e) {
      LOGGER.warn("Failed to load config", e);
    }

    // --- BIOS memory ------------------------

    // 0x80 (0x10) - Exception vector
    MEMORY.addSegment(GATE.wrap(new RamSegment(0x80L, 0x10)));

    // 0x100 (0x58) - Table of tables
    MEMORY.addSegment(GATE.wrap(new RamSegment(0x100L, 0x58)));

    // 0x500 (0xbb00) - Kernel code/data - relocated from ROM
    MEMORY.addSegment(GATE.wrap(new RamSegment(0x500L, 0xbb00)));

    // 0xe000 (0x2000) - Kernel memory (ExCBs, EvCBs, TCBs)
    MEMORY.addSegment(GATE.wrap(new RamSegment(0xe000L, 0x2000)));

    // --- User memory ------------------------

    MEMORY.addSegment(new RamSegment(0x0001_0000L, 0x4f_0000));
    MEMORY.addSegment(new RamSegment(0x1f80_0000L, 0x400));

    // --- Bios ROM ---------------------------

    MEMORY.addSegment(new RamSegment(0x1fc0_0000L, 0x8_0000));

    GATE.acquire();
    MEMORY.addFunctions(Bios.class);
    GATE.release();

    CPU = new Cpu();
    INTERRUPTS = new InterruptController(MEMORY);
    DMA = new DmaManager(MEMORY);
    GPU = new Gpu();
    MDEC = new Mdec(MEMORY);
    CDROM = new CdDrive();
    SPU = new Spu(MEMORY);

    codeThread = new Thread(Hardware::run);
    codeThread.setName("Code");
    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    gpuThread = new Thread(GPU);
    gpuThread.setName("GPU");
    spuThread = new Thread(SPU);
    spuThread.setName("SPU");

    final String entryPointClassName = System.getProperty("entrypoint", "");
    if(entryPointClassName.isEmpty()) {
      ENTRY_POINT = Scus94491.class;
    } else {
      LOGGER.info("Using entrypoint %s", entryPointClassName);

      try {
        ENTRY_POINT = Class.forName(entryPointClassName);
      } catch(final ClassNotFoundException e) {
        throw new RuntimeException("Could not find entrypoint class " + entryPointClassName, e);
      }
    }
  }

  private static boolean running;

  public static boolean isAlive() {
    return running;
  }

  public static void start() {
    codeThread.start();
    gpuThread.start();
    spuThread.start();

    running = true;
    while(running) {
      if(DMA.tick()) {
        INTERRUPTS.set(InterruptType.DMA);
      }

      CPU.tick();

      DebugHelper.sleep(0);
      if(!codeThread.isAlive() || !gpuThread.isAlive() || !spuThread.isAlive()) {
        running = false;
        SPU.stop();
      }

      while(dumping) {
        hardwareWaiting = true;
        DebugHelper.sleep(1);
      }

      hardwareWaiting = false;
    }
  }

  private static void run() {
    LOGGER.info("--- Legend start ---");

    GATE.acquire();
    MEMORY.ref(4, 0xbfc0_0000L).cast(RunnableRef::new).run();
  }

  public static boolean isGpuThread() {
    return Thread.currentThread() == gpuThread;
  }
}
