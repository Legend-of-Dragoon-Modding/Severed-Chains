import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.meta.MetaManager;

/** Read-only story-order research; never writes retail scripts. */
class Disc1ScriptDump {
  public static void main(String[] args) throws Exception {
    var meta = new MetaManager(null, Path.of("patches")).loadMeta("meta");
    var disassembler = new Disassembler(meta);
    var translator = new Translator();
    for (String directory : args) {
      var input = Path.of("files/SECT/DRGN21.BIN", directory);
      var output = Path.of("build/disc1/scripts", directory);
      Files.createDirectories(output);
      try (var files = Files.list(input)) {
        for (var file : files.filter(Files::isRegularFile).toList()) {
          if (!file.getFileName().toString().matches("[0-9]+")) continue;
          try {
            var script = disassembler.disassemble(file.toString(), Files.readAllBytes(file), List.of(), Map.of());
            Files.writeString(output.resolve(file.getFileName() + ".txt"), translator.translate(script, meta, false, false, false));
          } catch (Exception e) {
            System.err.println(file + ": " + e);
          }
        }
      }
    }
  }
}
