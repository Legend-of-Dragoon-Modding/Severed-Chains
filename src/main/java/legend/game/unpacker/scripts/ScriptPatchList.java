package legend.game.unpacker.scripts;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
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

  public static ScriptPatchList load(final Path file) throws InvalidPatchListException {
    final Map<String, ScriptPatch> patches = new HashMap<>();

    try {
      for(final String[] patch : loadCsvFile(file)) {
        patches.put(patch[0], new ScriptPatch(patch[0], patch[1]));
      }
    } catch(final IOException | CsvException e) {
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
