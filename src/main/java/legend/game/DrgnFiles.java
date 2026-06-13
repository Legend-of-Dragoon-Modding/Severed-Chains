package legend.game;

import legend.core.DebugHelper;
import legend.core.memory.Method;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class DrgnFiles {
  private DrgnFiles() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(DrgnFiles.class);

  public static int drgnBinIndex_800bc058;

  public static CompletableFuture<FileData> loadFile(final String file) {
    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading file %s from %s.%s(%s:%d)", file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Loader.loadFile(file);
  }

  public static CompletableFuture<List<FileData>> loadDir(final String dir) {
    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading dir %s from %s.%s(%s:%d)", dir, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Loader.loadDirectory(dir);
  }

  public static CompletableFuture<List<FileData>> loadDrgnFiles(int drgnBinIndex, final String... files) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, String.join(", ", files), frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    final String[] paths = new String[files.length];
    for(int i = 0; i < files.length; i++) {
      paths[i] = "SECT/DRGN" + drgnBinIndex + ".BIN/" + files[i];
    }

    return Loader.loadFiles(paths);
  }

  public static CompletableFuture<FileData> loadDrgnFile(final int drgnBinIndex, final int file) {
    return loadDrgnFile(drgnBinIndex, String.valueOf(file));
  }

  public static void loadDrgnFileSync(final int drgnBinIndex, final int file, final Consumer<FileData> onCompletion) {
    loadDrgnFileSync(drgnBinIndex, String.valueOf(file), onCompletion);
  }

  public static CompletableFuture<FileData> loadDrgnFile(int drgnBinIndex, final String file) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Loader.loadFile("SECT/DRGN" + drgnBinIndex + ".BIN/" + file);
  }

  public static void loadDrgnFileSync(int drgnBinIndex, final String file, final Consumer<FileData> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Loader.loadFileSync("SECT/DRGN" + drgnBinIndex + ".BIN/" + file));
  }

  public static CompletableFuture<List<FileData>> loadDrgnDir(int drgnBinIndex, final int directory) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Loader.loadDirectory("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory);
  }

  public static void loadDrgnDirSync(int drgnBinIndex, final String directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Loader.loadDirectorySync("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory));
  }

  public static void loadDrgnDirSync(int drgnBinIndex, final int directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Loader.loadDirectorySync("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory));
  }

  public static CompletableFuture<List<FileData>> loadDrgnDir(int drgnBinIndex, final String directory) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    return Loader.loadDirectory("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory);
  }

  @Method(0x80017564L)
  @ScriptDescription("Rewinds if there are files currently loading; pauses otherwise")
  public static FlowControl scriptWaitForFilesToLoad(final RunningScript<?> script) {
    final int loadingCount = Loader.getLoadingFileCount();

    if(loadingCount != 0) {
      LOGGER.info("%d files still loading; pausing and rewinding", loadingCount);
      return FlowControl.PAUSE_AND_REWIND;
    }

    LOGGER.info("No files loading");
    return FlowControl.PAUSE;
  }
}
