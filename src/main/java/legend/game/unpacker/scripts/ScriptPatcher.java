package legend.game.unpacker.scripts;

import com.github.difflib.patch.PatchFailedException;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.legendofdragoon.scripting.Compiler;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Lexer;
import org.legendofdragoon.scripting.Patcher;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.meta.Meta;
import org.legendofdragoon.scripting.meta.MetaManager;
import org.legendofdragoon.scripting.meta.NoSuchVersionException;
import org.legendofdragoon.scripting.tokens.Script;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import static legend.core.IoHelper.crc32;
import static legend.core.IoHelper.intsToBytes;

public class ScriptPatcher {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptPatch.class);

  private final Meta meta;
  private final Disassembler disassembler;
  private final Translator translator = new Translator();
  private final Compiler compiler = new Compiler();
  private final Lexer lexer;

  private final ScriptPatchList patches;
  private final Path patchesDir;
  private final Path filesDir;
  private final Path cacheDir;

  public ScriptPatcher(final Path patchDir, final Path filesDir, final Path cacheDir) throws NoSuchVersionException, IOException, CsvException {
    this.meta = new MetaManager(null, patchDir).loadMeta("meta");
    this.disassembler = new Disassembler(this.meta);
    this.lexer = new Lexer(this.meta);
    this.patches = this.loadPatchList(patchDir.resolve("scripts.csv"));
    this.patchesDir = patchDir;
    this.filesDir = filesDir;
    this.cacheDir = cacheDir;
  }

  private ScriptPatchList loadPatchList(final Path file) {
    try {
      return ScriptPatchList.load(file);
    } catch(final InvalidPatchListException e) {
      LOGGER.warn("Failed to load patch list", e);
      return ScriptPatchList.empty();
    }
  }

  public void apply() throws IOException, PatchFailedException {
    LOGGER.info("Applying script patches");

    final ScriptPatchList cacheList = this.loadPatchList(this.cacheDir.resolve("scripts.csv"));

    boolean changed = false;
    // Apply new or changed patches
    for(final ScriptPatch patch : this.patches) {
      final ScriptPatch cachedPatch = cacheList.getPatchForScript(patch.sourceFile);

      if(cachedPatch == null || crc32(this.cacheDir.resolve(cachedPatch.patchFile)) != crc32(this.patchesDir.resolve(patch.patchFile))) {
        LOGGER.info("Patching %s...", patch.sourceFile);
        this.patchFile(patch);
        changed = true;
      }
    }

    // Unpatch any patches that have been deleted
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
    this.patchFile(patch.sourceFile, patch.patchFile);
  }

  public void patchFile(final String source, final String patch) throws IOException, PatchFailedException {
    this.patchFile(this.filesDir.resolve(source), this.cacheDir.resolve("backups").resolve(source), this.patchesDir.resolve(patch));
  }

  public void patchFile(final Path sourceFile, final Path backupFile, final Path patchFile) throws IOException, PatchFailedException {
    final List<String> patchLines = Files.readAllLines(patchFile);

    final List<String> decompiledLines = this.decompile(Files.readAllBytes(backupFile));
    final String patched = Patcher.applyPatch(decompiledLines, patchLines);
    final byte[] recompiledSource = this.recompile(patched);

    Files.write(sourceFile, recompiledSource, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private void backupFile(final String scriptPath) throws IOException {
    final Path sourcePath = this.filesDir.resolve(scriptPath);
    final Path destPath = this.cacheDir.resolve("backups").resolve(scriptPath);
    if(!Files.exists(destPath.getParent())){
      Files.createDirectories(destPath.getParent());
    }
    Files.copy(sourcePath,destPath);
  }
  private void restoreFile(ScriptPatch cachedPatch) throws IOException {
    final Path sourcePath = this.filesDir.resolve(cachedPatch.sourceFile);
    final Path backupPath = this.cacheDir.resolve("backups").resolve(cachedPatch.sourceFile);
    Files.delete(sourcePath);
    Files.copy(backupPath,sourcePath);
    Files.delete(backupPath);
    Path currentPath = backupPath.getParent();
    while(!currentPath.equals(this.cacheDir.resolve("backups"))){
      if(Objects.requireNonNull(currentPath.toFile().listFiles()).length == 0){
        Files.delete(currentPath);
      }
      currentPath = currentPath.getParent();
    }
  }

  private List<String> decompile(final byte[] data) {
    //TODO add support for explicit branches #1269
    final int[] arr = {};
    final Script script = this.disassembler.disassemble(data, arr);
    final String decompiledOutput = this.translator.translate(script, this.meta);
    return decompiledOutput.lines().toList();
  }

  private byte[] recompile(final String patched) {
    final Script lexedDecompiledSource = this.lexer.lex(patched);
    return intsToBytes(this.compiler.compile(lexedDecompiledSource));
  }
}
