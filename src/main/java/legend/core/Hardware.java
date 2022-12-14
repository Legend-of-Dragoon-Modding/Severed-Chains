package legend.core;

import legend.core.cdrom.CdDrive;
import legend.core.gpu.Gpu;
import legend.core.memory.Memory;
import legend.core.memory.segments.RamSegment;
import legend.core.spu.Spu;
import legend.game.Scus94491;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class Hardware {
  private Hardware() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Hardware.class);

  public static final Memory MEMORY = new Memory();

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

    // --- User memory ------------------------

    MEMORY.addSegment(new RamSegment(0x0001_0000L, 0x4f_0000));
    MEMORY.addSegment(new RamSegment(0x1f80_0000L, 0x400));

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

    try {
      Unpacker.unpack();
    } catch(final UnpackerException e) {
      throw new RuntimeException("Failed to unpack files", e);
    }

    final byte[] fileData = Unpacker.loadFile("SCUS_944.91");
    MEMORY.setBytes(MathHelper.get(fileData, 0x18, 4), fileData, 0x800, (int)MathHelper.get(fileData, 0x1c, 4));

    Scus94491.main();

    LOGGER.info("Exiting");
    System.exit(0);
  }
}
