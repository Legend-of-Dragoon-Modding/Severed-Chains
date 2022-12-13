package legend.core;

import legend.core.cdrom.CdDrive;
import legend.core.gpu.Gpu;
import legend.core.kernel.Bios;
import legend.core.memory.Memory;
import legend.core.memory.segments.PrivilegeGate;
import legend.core.memory.segments.RamSegment;
import legend.core.memory.types.RunnableRef;
import legend.core.spu.Spu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class Hardware {
  private Hardware() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Hardware.class);

  public static final Memory MEMORY = new Memory();
  public static final PrivilegeGate GATE = new PrivilegeGate();

  public static final Cpu CPU;
  public static final Gpu GPU;
  public static final CdDrive CDROM;
  public static final Spu SPU;

  public static final Thread codeThread;
  public static final Thread hardwareThread;
  public static final Thread gpuThread;
  public static final Thread spuThread;

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
    GPU = new Gpu();
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
      DebugHelper.sleep(0);
      if(!codeThread.isAlive() || !gpuThread.isAlive() || !spuThread.isAlive()) {
        running = false;
        SPU.stop();
      }
    }
  }

  private static void run() {
    LOGGER.info("--- Legend start ---");

    GATE.acquire();
    MEMORY.ref(4, 0xbfc0_0000L).cast(RunnableRef::new).run();
  }
}
