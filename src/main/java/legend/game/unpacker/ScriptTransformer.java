package legend.game.unpacker;

import com.github.difflib.patch.PatchFailedException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ScriptTransformer {
  private ScriptTransformer() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptTransformer.class);

  private static final Map<String, String> patches = new HashMap<>();
  private static final Meta meta;

  static {
    final Path patchFile = Path.of("./patches/scripts.csv");

    if(Files.exists(patchFile)) {
      try {
        for(final String[] patch : loadCsvFile(patchFile)) {
          patches.put(patch[0], patch[1]);
        }
      } catch(final IOException | CsvException e) {
        throw new UnpackerException("Failed to load script patches", e);
      }
    }

    try {
      meta = new MetaManager(null, Path.of("./patches")).loadMeta("meta");
    } catch(final IOException | CsvException | NoSuchVersionException e) {
      throw new UnpackerException("Failed to load script meta", e);
    }
  }

  public static boolean discriminator(final PathNode node, final Set<String> flags) {
    return patches.containsKey(node.fullPath) && !flags.contains(node.fullPath);
  }

  public static void transformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    flags.add(node.fullPath);

    LOGGER.info("Patching script %s...", node.fullPath);

    // Load patch
    final Path patchFile = Path.of("./patches").resolve(patches.get(node.fullPath));
    final List<String> patchLines;
    try {
      patchLines = Files.readAllLines(patchFile);
    } catch(final IOException e) {
      throw new UnpackerException("Failed to load patch file for " + node.fullPath, e);
    }

    final Disassembler disassembler = new Disassembler(meta);
    final Translator translator = new Translator();
    final Compiler compiler = new Compiler();
    final Lexer lexer = new Lexer(meta);

    // Decompile
    final Script script = disassembler.disassemble(node.data.getBytes());
    final String decompiledOutput = translator.translate(script, meta);
    final List<String> decompiledLines = decompiledOutput.lines().toList();

    // Patch
    final String patched;
    try {
      patched = Patcher.applyPatch(decompiledLines, patchLines);
    } catch(final PatchFailedException e) {
      throw new UnpackerException("Failed to apply patch for " + node.fullPath, e);
    }

    // Recompile
    final Script lexedDecompiledSource = lexer.lex(patched);
    final byte[] recompiledSource = intsToBytes(compiler.compile(lexedDecompiledSource));

    transformations.replaceNode(node, new FileData(recompiledSource));
  }

  private static List<String[]> loadCsvFile(final Path file) throws IOException, CsvException {
    return loadCsv(Files.newInputStream(file));
  }

  private static List<String[]> loadCsv(final InputStream input) throws IOException, CsvException {
    try(final CSVReader reader = new CSVReader(new InputStreamReader(input))) {
      return reader.readAll();
    }
  }

  private static byte[] intsToBytes(final int[] ints) {
    final ByteBuffer buffer = ByteBuffer.allocate(ints.length * 0x4).order(ByteOrder.LITTLE_ENDIAN);
    buffer.asIntBuffer().put(ints);
    return buffer.array();
  }
}
