package legend.game.unpacker;

import com.opencsv.exceptions.CsvException;
import org.legendofdragoon.scripting.Compiler;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Lexer;
import org.legendofdragoon.scripting.OpType;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.meta.Meta;
import org.legendofdragoon.scripting.meta.MetaManager;
import org.legendofdragoon.scripting.meta.NoSuchVersionException;
import org.legendofdragoon.scripting.tokens.Entry;
import org.legendofdragoon.scripting.tokens.Op;
import org.legendofdragoon.scripting.tokens.Param;
import org.legendofdragoon.scripting.tokens.PointerTable;
import org.legendofdragoon.scripting.tokens.Script;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TranslationTransformer {
  private TranslationTransformer() { }

  private static final Pattern pathRegex = Pattern.compile("^SECT/DRGN2\\d\\.BIN/(\\d+)/\\d+$");

  private static final Meta meta;
  private static final Translator translator = new Translator();
  private static final Compiler compiler = new Compiler();
  private static final Lexer lexer;

  static {
    try {
      meta = new MetaManager(null, Path.of("./patches")).loadMeta("meta");
    } catch(final IOException | NoSuchVersionException | CsvException e) {
      throw new RuntimeException(e);
    }

    lexer = new Lexer(meta);
  }

  public static boolean discriminator(final PathNode node, final Set<String> flags) {
    if(node.data.size() <= 0x8) {
      return false;
    }

    final Matcher matcher = pathRegex.matcher(node.fullPath);
    if(matcher.matches()) {
      final int directoryIndex = Integer.parseInt(matcher.group(1));
      return directoryIndex % 3 == 0;
    }

    return false;
  }

  public static void transformer(final PathNode node, final Transformations transformations, final Set<String> flags) {
    final Script script = new Disassembler(meta).disassemble(node.data.getBytes());

    for(int i = 0; i < script.entries.length; i++) {
      final Entry entry = script.entries[i++];

      if(entry instanceof final Op op && op.type == OpType.CALL && op.headerParam == 200) { // subfunc
        for(int paramIndex = 0; paramIndex < 6; paramIndex++) {
          final Param param = (Param)script.entries[i];
          i += param.type.width;
        }

        final Param stringTablePtr = (Param)script.entries[i];

        for(final var labelEntry : script.labels.entrySet()) {
          if(labelEntry.getValue().contains(stringTablePtr.label)) {
            final PointerTable stringTable = (PointerTable)script.entries[labelEntry.getKey() / 4];

            for(final String label : stringTable.labels) {
              for(final var labelEntry2 : script.labels.entrySet()) {
                if(labelEntry2.getValue().contains(label)) {
                  final Entry string = script.entries[labelEntry2.getKey() / 4];
                  System.out.println(string);
                }
              }
            }
          }
        }
      }
    }
  }
}
