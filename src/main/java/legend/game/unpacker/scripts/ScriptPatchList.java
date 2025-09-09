package legend.game.unpacker.scripts;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static legend.core.IoHelper.loadCsvFile;

public class ScriptPatchList implements Iterable<ScriptPatch> {
  public final Map<String, ScriptPatch> patches;

  private ScriptPatchList(final Map<String, ScriptPatch> patches) {
    this.patches = patches;
  }

  public static ScriptPatchList empty() {
    return new ScriptPatchList(new HashMap<>());
  }

  public static ScriptPatchList load(final Path filesDir, final Path patchListFile) throws InvalidPatchListException {
    final Map<String, ScriptPatch> patches = new HashMap<>();

    try {
      for(final String[] patch : loadCsvFile(patchListFile)) {
        if(Files.exists(filesDir.resolve(patch[0]))) {
          // Old patch without type
          patches.put(patch[0], new ScriptPatch(PatchType.DIFF, patch[0], patch[1]));
        } else {
          patches.put(patch[1], new ScriptPatch(PatchType.valueOf(patch[0].toUpperCase(Locale.US)), patch[1], patch[2]));
        }
      }
    } catch(final Throwable e) {
      throw new InvalidPatchListException("Failed to load script patches", e);
    }

    return new ScriptPatchList(patches);
  }

  @Override
  public Iterator<ScriptPatch> iterator() {
    return this.patches.values().iterator();
  }

  public ScriptPatch getPatchForScript(final String script) {
    return this.patches.get(script);
  }
}
