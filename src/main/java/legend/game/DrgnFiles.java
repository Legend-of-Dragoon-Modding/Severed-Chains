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
import java.util.function.Consumer;

import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;

public final class DrgnFiles {
  private DrgnFiles() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(DrgnFiles.class);

  public static int drgnBinIndex_800bc058;

  public static void loadFile(final String file, final Consumer<FileData> onCompletion) {
    final StackWalker.StackFrame frame = StackWalker.getInstance().walk(frames -> frames
      .skip(1)
      .findFirst())
      .get();

    LOGGER.info("Loading file %s from %s.%s(%s:%d)", file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Loader.loadFile(file, onCompletion);
  }

  public static void loadDir(final String dir, final Consumer<List<FileData>> onCompletion) {
    final StackWalker.StackFrame frame = StackWalker.getInstance().walk(frames -> frames
      .skip(1)
      .findFirst())
      .get();

    LOGGER.info("Loading dir %s from %s.%s(%s:%d)", dir, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Loader.loadDirectory(dir, onCompletion);
  }

  public static void loadDrgnFiles(int drgnBinIndex, final Consumer<List<FileData>> onCompletion, final String... files) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, String.join(", ", files), frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    final String[] paths = new String[files.length];
    for(int i = 0; i < files.length; i++) {
      paths[i] = "SECT/DRGN" + drgnBinIndex + ".BIN/" + files[i];
    }

    Loader.loadFiles(onCompletion, paths);
  }

  public static void loadDrgnFile(final int drgnBinIndex, final int file, final Consumer<FileData> onCompletion) {
    loadDrgnFile(drgnBinIndex, String.valueOf(file), onCompletion);
  }

  public static void loadDrgnFileSync(final int drgnBinIndex, final int file, final Consumer<FileData> onCompletion) {
    loadDrgnFileSync(drgnBinIndex, String.valueOf(file), onCompletion);
  }

  public static void loadDrgnFile(int drgnBinIndex, final String file, final Consumer<FileData> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Loader.loadFile("SECT/DRGN" + drgnBinIndex + ".BIN/" + file, onCompletion);
  }

  public static void loadDrgnFileSync(int drgnBinIndex, final String file, final Consumer<FileData> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d %s from %s.%s(%s:%d)", drgnBinIndex, file, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Loader.loadFile("SECT/DRGN" + drgnBinIndex + ".BIN/" + file));
  }

  public static void loadDrgnDir(int drgnBinIndex, final int directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Loader.loadDirectory("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory, onCompletion);
  }

  public static void loadDrgnDirSync(int drgnBinIndex, final String directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Loader.loadDirectory("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory));
  }

  public static void loadDrgnDirSync(int drgnBinIndex, final int directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %d from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    onCompletion.accept(Loader.loadDirectory("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory));
  }

  public static void loadDrgnDir(int drgnBinIndex, final String directory, final Consumer<List<FileData>> onCompletion) {
    if(drgnBinIndex >= 2) {
      drgnBinIndex = 20 + drgnBinIndex_800bc058;
    }

    final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
    LOGGER.info("Loading DRGN%d dir %s from %s.%s(%s:%d)", drgnBinIndex, directory, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());

    Loader.loadDirectory("SECT/DRGN" + drgnBinIndex + ".BIN/" + directory, onCompletion);
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

  @Method(0x80018944L)
  public static void waitForFilesToLoad() {
    if(Loader.getLoadingFileCount() == 0) {
      pregameLoadingStage_800bb10c++;
    }
  }
}
