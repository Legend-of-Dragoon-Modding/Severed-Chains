package legend.core;

import legend.core.gpu.Gpu;
import legend.core.memory.Memory;
import legend.core.memory.Value;
import legend.core.memory.segments.RamSegment;
import legend.core.spu.Spu;
import legend.game.Scus94491BpeSegment;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.Scus94491BpeSegment_8003;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.Scus94491BpeSegment_800e;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class GameEngine {
  private GameEngine() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameEngine.class);

  public static final Memory MEMORY = new Memory();

  public static final Cpu CPU;
  public static final Gpu GPU;
  public static final Spu SPU;

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
    SPU = new Spu(MEMORY);

    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    gpuThread = new Thread(GPU);
    gpuThread.setName("GPU");
    spuThread = new Thread(SPU);
    spuThread.setName("SPU");
  }

  private static final Value _80010000 = MEMORY.ref(4, 0x80010000L);
  private static final Value bpe_80188a88 = MEMORY.ref(4, 0x80188a88L);

  public static void start() {
    gpuThread.start();
    spuThread.start();

    LOGGER.info("--- Legend start ---");

    try {
      Unpacker.unpack();
    } catch(final UnpackerException e) {
      throw new RuntimeException("Failed to unpack files", e);
    }

    final byte[] fileData = Unpacker.loadFile("SCUS_944.91");
    MEMORY.setBytes(MathHelper.get(fileData, 0x18, 4), fileData, 0x800, (int)MathHelper.get(fileData, 0x1c, 4));

    final byte[] archive = MEMORY.getBytes(bpe_80188a88.getAddress(), 221736);
    final byte[] decompressed = Unpacker.decompress(archive);
    MEMORY.setBytes(_80010000.getAddress(), decompressed);

    MEMORY.addFunctions(Scus94491BpeSegment.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8002.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8003.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8004.class);
    MEMORY.addFunctions(Scus94491BpeSegment_800e.class);
    Scus94491BpeSegment_8002.start();

    SPU.stop();

    LOGGER.info("Exiting");
    System.exit(0);
  }

  public static boolean isAlive() {
    return gpuThread.isAlive() && spuThread.isAlive();
  }
}
