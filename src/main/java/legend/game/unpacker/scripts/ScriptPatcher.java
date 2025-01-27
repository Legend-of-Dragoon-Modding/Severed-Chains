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

import javax.annotation.Nullable;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
  private final Path backupsDir;

  public ScriptPatcher(final Path patchDir, final Path filesDir, final Path cacheDir, final Path backupsDir) {
    this.backupsDir = backupsDir;
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

  private boolean needsUpdate(@Nullable final ScriptPatch cachedPatch, final ScriptPatch newPatch) throws IOException {
    // Patch isn't cached, definitely needs update
    if(cachedPatch == null) {
      return true;
    }

    final Map<String, Integer> cachedCrc32s = this.getCrc32s(this.cacheDir, cachedPatch);
    final Map<String, Integer> newCrc32s = this.getCrc32s(this.patchesDir, newPatch);

    for(final var entry : newCrc32s.entrySet()) {
      if(!Objects.equals(cachedCrc32s.get(entry.getKey()), entry.getValue())) {
        return true;
      }
    }

    return false;
  }

  private Map<String, Integer> getCrc32s(final Path baseBath, final ScriptPatch patch) throws IOException {
    final Map<String, Integer> crc32s = new HashMap<>();
    this.getCrc32s(baseBath, patch.patchFile, crc32s);
    this.getCrc32s(baseBath, this.resolvePatchConfigName(patch.patchFile), crc32s);
    return crc32s;
  }

  private static final Pattern INCLUDE_FILE = Pattern.compile("^\\s*#include\\s+(.+?)\\s*$");

  private void getCrc32s(final Path basePath, final String path, final Map<String, Integer> crc32s) throws IOException {
    // We've already checked this file
    if(crc32s.containsKey(path)) {
      return;
    }

    final Path fullPath = basePath.resolve(path);

    // Insert nulls for missing files
    if(!Files.exists(fullPath)) {
      crc32s.put(path, null);
      return;
    }

    // CRC32 for this file
    crc32s.put(path, crc32(fullPath));

    // Parse out includes and CRC32 each of those
    try(final Stream<String> stream = Files.lines(fullPath)) {
      final List<String> includePaths = stream
        .map(INCLUDE_FILE::matcher)
        .filter(Matcher::matches)
        .map(matcher -> matcher.group(1))
        .toList();

      for(final String includePath : includePaths) {
        this.getCrc32s(basePath, basePath.relativize(fullPath.getParent().resolve(includePath)).toString(), crc32s);
      }
    }
  }

  public void apply() throws IOException, PatchFailedException {
    LOGGER.info("Applying script patches");

    final ScriptPatchList cacheList = this.loadPatchList(this.filesDir, this.cacheDir.resolve("scripts.csv"));

    // Apply new or changed patches
    boolean changed = false;
    for(final ScriptPatch patch : this.patches) {
      final ScriptPatch cachedPatch = cacheList.getPatchForScript(patch.sourceFile);

      if(this.needsUpdate(cachedPatch, patch)) {
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
      FileUtils.deleteDirectory(this.cacheDir.toFile());
      FileUtils.copyDirectory(this.patchesDir.toFile(), this.cacheDir.toFile());
    }
  }

  public void patchFile(final ScriptPatch patch) throws IOException, PatchFailedException {
    // Back up the original file (if we're doing a replacement and the original doesn't exist, that's fine)
    if(!Files.exists(this.backupsDir.resolve(patch.sourceFile)) && (patch.type != PatchType.REPLACEMENT || Files.exists(this.filesDir.resolve(patch.sourceFile)))) {
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
    this.patchFile(this.filesDir.resolve(source), this.backupsDir.resolve(source), this.patchesDir.resolve(patch));
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
    final byte[] recompiledSource = this.recompile(patchFile, patchContents);
    Files.write(sourceFile, recompiledSource, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private void backupFile(final String scriptPath) throws IOException {
    final Path sourcePath = this.filesDir.resolve(scriptPath);
    final Path destPath = this.backupsDir.resolve(scriptPath);
    if(!Files.exists(destPath.getParent())) {
      Files.createDirectories(destPath.getParent());
    }
    Files.copy(sourcePath, destPath);
  }

  private void restoreFile(final ScriptPatch cachedPatch) throws IOException {
    final Path sourcePath = this.filesDir.resolve(cachedPatch.sourceFile);
    final Path backupPath = this.backupsDir.resolve(cachedPatch.sourceFile);
    Files.move(backupPath, sourcePath, StandardCopyOption.REPLACE_EXISTING);

    Path currentPath = backupPath.getParent();
    while(!currentPath.equals(this.backupsDir)) {
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

  private String resolvePatchConfigName(final String diffName) {
    return diffName.substring(0, diffName.lastIndexOf('.')) + ".config.csv";
  }

  private Path resolvePatchConfigPath(final Path diffPath) {
    return diffPath.resolveSibling(this.resolvePatchConfigName(diffPath.getFileName().toString()));
  }

  private void getPatchConfigs(final Path configPath, final List<Integer> branchList, final Map<Integer, Integer> tableLengthList) {
    List<String[]> patchConfig = new ArrayList<>();

    if(Files.exists(configPath)) {
      try {
        patchConfig = loadCsvFile(configPath);
      } catch(final CsvException | IOException err) {
        LOGGER.error("Patch config error for config: %s", configPath);
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
