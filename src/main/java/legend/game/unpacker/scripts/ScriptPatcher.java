package legend.game.unpacker.scripts;

import com.github.difflib.patch.PatchFailedException;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Patcher;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.tokens.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static legend.core.GameEngine.SCRIPTS;
import static legend.core.IoHelper.crc32;
import static legend.core.IoHelper.loadCsvFile;

public class ScriptPatcher {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptPatcher.class);

  private final Disassembler disassembler;
  private final Translator translator = new Translator();

  private final ScriptPatchList patches;
  private final Path patchesDir;
  private final Path filesDir;
  private final Path cacheDir;

  public ScriptPatcher(final Path patchDir, final Path filesDir, final Path cacheDir) {
    this.disassembler = new Disassembler(SCRIPTS.meta());
    this.patches = this.loadPatchList(filesDir, patchDir.resolve("scripts.csv"));
    this.patchesDir = patchDir;
    this.filesDir = filesDir;
    this.cacheDir = cacheDir;
  }

  private ScriptPatchList loadPatchList(final Path filesDir, final Path patchListFile) {
    try {
      return ScriptPatchList.load(filesDir, patchListFile);
    } catch(final InvalidPatchListException e) {
      if(e.getCause() instanceof NoSuchFileException) {
        LOGGER.warn("Patch cache does not exist");
      } else {
        LOGGER.warn("Failed to load patch list", e);
      }

      return ScriptPatchList.empty();
    }
  }

  public void apply() throws IOException, PatchFailedException {
    LOGGER.info("Applying script patches");

    final ScriptPatchList cacheList = this.loadPatchList(this.filesDir, this.cacheDir.resolve("scripts.csv"));

    boolean changed = false;
    // Apply new or changed patches
    for(final ScriptPatch patch : this.patches) {
      final ScriptPatch cachedPatch = cacheList.getPatchForScript(patch.sourceFile);

      if(cachedPatch == null || crc32(this.cacheDir.resolve(cachedPatch.patchFile)) != crc32(this.patchesDir.resolve(patch.patchFile))) {
        LOGGER.info("Patching %s...", patch.sourceFile);
        try {
          this.patchFile(patch);
        } catch(final PatchFailedException error) {
          LOGGER.error("Patch failed for script: %s", patch.patchFile);
          throw error;
        }
        changed = true;
      }
    }

    // Restore any patches that have been deleted
    for(final ScriptPatch cachedPatch : cacheList) {
      if(this.patches.getPatchForScript(cachedPatch.sourceFile) == null) {
        LOGGER.info("Restoring %s...", cachedPatch.sourceFile);
        this.restoreFile(cachedPatch);
        changed = true;
      }
    }

    // Cache changes
    if(changed) {
      FileUtils.copyDirectory(this.patchesDir.toFile(), this.cacheDir.toFile());
    }
  }

  public void patchFile(final ScriptPatch patch) throws IOException, PatchFailedException {
    if(!Files.exists(this.cacheDir.resolve("backups").resolve(patch.sourceFile))) {
      this.backupFile(patch.sourceFile);
    }

    if(patch.type == PatchType.DIFF) {
      this.patchFile(patch.sourceFile, patch.patchFile);
    } else if(patch.type == PatchType.REPLACEMENT) {
      this.replaceFile(patch.sourceFile, patch.patchFile);
    } else {
      throw new RuntimeException("Unsupported patch type " + patch.type);
    }
  }

  public void patchFile(final String source, final String patch) throws IOException, PatchFailedException {
    this.patchFile(this.filesDir.resolve(source), this.cacheDir.resolve("backups").resolve(source), this.patchesDir.resolve(patch));
  }

  public void patchFile(final Path sourceFile, final Path backupFile, final Path patchFile) throws IOException, PatchFailedException {
    final List<String> patchLines = Files.readAllLines(patchFile);

    final Path configPath = this.resolvePatchConfigPath(patchFile);
    final List<Integer> branchList = new ArrayList<>();
    final Map<Integer, Integer> tableLengths = new HashMap<>();

    this.getPatchConfigs(configPath, branchList, tableLengths);

    final List<String> decompiledLines = this.decompile(Files.readAllBytes(backupFile), branchList, tableLengths);
    final String patched = Patcher.applyPatch(decompiledLines, patchLines);
    final byte[] recompiledSource = this.recompile(sourceFile, patched);

    Files.write(sourceFile, recompiledSource, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public void replaceFile(final String source, final String patch) throws IOException {
    this.replaceFile(this.filesDir.resolve(source), this.patchesDir.resolve(patch));
  }

  public void replaceFile(final Path sourceFile, final Path patchFile) throws IOException {
    final String patchContents = Files.readString(patchFile);
    final byte[] recompiledSource = this.recompile(sourceFile, patchContents);
    Files.write(sourceFile, recompiledSource, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private void backupFile(final String scriptPath) throws IOException {
    final Path sourcePath = this.filesDir.resolve(scriptPath);
    final Path destPath = this.cacheDir.resolve("backups").resolve(scriptPath);
    if(!Files.exists(destPath.getParent())) {
      Files.createDirectories(destPath.getParent());
    }
    Files.copy(sourcePath, destPath);
  }

  private void restoreFile(final ScriptPatch cachedPatch) throws IOException {
    final Path sourcePath = this.filesDir.resolve(cachedPatch.sourceFile);
    final Path backupPath = this.cacheDir.resolve("backups").resolve(cachedPatch.sourceFile);
    Files.move(backupPath, sourcePath, StandardCopyOption.REPLACE_EXISTING);

    Path currentPath = backupPath.getParent();
    while(!currentPath.equals(this.cacheDir.resolve("backups"))) {
      if(Objects.requireNonNull(currentPath.toFile().listFiles()).length == 0) {
        Files.delete(currentPath);
      }
      currentPath = currentPath.getParent();
    }
  }

  private List<String> decompile(final byte[] data, final List<Integer> branches, final Map<Integer, Integer> tableLengths) {
    this.disassembler.tableLengths.putAll(tableLengths);
    this.disassembler.extraBranches.addAll(branches);
    this.translator.stripComments = true;
    this.translator.stripNames = true;
    final Script script = this.disassembler.disassemble(data);
    final String decompiledOutput = this.translator.translate(script, SCRIPTS.meta());
    return decompiledOutput.lines().toList();
  }

  private byte[] recompile(final Path path, final String patched) {
    return SCRIPTS.compile(path, patched);
  }

  private Path resolvePatchConfigPath(final Path diffPath) {
    final String patchLocationStr = diffPath.getFileName().toString();
    final String patchName = patchLocationStr.substring(0, patchLocationStr.lastIndexOf('.')) + ".config.csv";
    return diffPath.resolveSibling(patchName);
  }

  private void getPatchConfigs(final Path configPath, final List<Integer> branchList, final Map<Integer, Integer> tableLengthList) {
    List<String[]> patchConfig = new ArrayList<>();

    if(Files.exists(configPath)) {
      try {
        patchConfig = loadCsvFile(configPath);
      } catch(final CsvException | IOException err) {
        LOGGER.error("Patch config error for config: " + configPath);
      }
    }

    for(final String[] strings : patchConfig) {
      switch(strings[0]) {
        case "branch":
          branchList.add(Integer.parseInt(strings[1].trim(), 16));
          break;
        case "table_length":
          tableLengthList.put(Integer.parseInt(strings[1].trim(), 16), Integer.parseInt(strings[2].trim()));
          break;
        default:
          throw new IllegalArgumentException("Invalid patch config option: " + strings[0]);
      }
    }
  }
}
