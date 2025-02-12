package legend.game.unpacker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Finderator {
  private Finderator() { }

  public static void main(final String[] args) throws NoSuchAlgorithmException, IOException {
    processDrgnBin(1);
  }

  private static void processDrgnBin(final int num) throws NoSuchAlgorithmException, IOException {
    final MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

    final Path drgnBin = Path.of("./files/SECT/DRGN2" + num + ".BIN");
    final List<FileData> dirs = Loader.loadDirectory("SECT/DRGN2" + num + ".BIN");

    final Map<String, List<String>> modelHashes = new HashMap<>();
    final Map<String, List<String>> animHashes = new HashMap<>();

    for(int dirIndex = 0; dirIndex < dirs.size() / 3; dirIndex++) {
      final Path dirPath = drgnBin.resolve(Integer.toString(dirIndex * 3 + 2));

      if(Files.isDirectory(dirPath)) {
        final List<FileData> modelsAndAnims = Loader.loadDirectory("SECT/DRGN2" + num + ".BIN/" + (dirIndex * 3 + 2));

        for(int sobjIndex = 0; sobjIndex < modelsAndAnims.size() / 33; sobjIndex++) {
          final String modelHash = hashToString(sha1.digest(modelsAndAnims.get(sobjIndex * 33).getBytes()));
          modelHashes.computeIfAbsent(modelHash, k -> new ArrayList<>()).add("SECT/DRGN2" + num + ".BIN/" + (dirIndex * 3 + 2) + '/' + sobjIndex * 33);

          for(int animIndex = 0; animIndex < 32; animIndex++) {
            final String animHash = hashToString(sha1.digest(modelsAndAnims.get(sobjIndex * 33 + animIndex + 1).getBytes()));
            animHashes.computeIfAbsent(animHash, k -> new ArrayList<>()).add("SECT/DRGN2" + num + ".BIN/" + (dirIndex * 3 + 2) + '/' + (sobjIndex * 33 + animIndex + 1));
          }
        }
      }
    }

    final String models = modelHashes.entrySet().stream()
      .sorted(Comparator.comparingInt((Map.Entry<String, List<String>> o) -> o.getValue().size()).reversed())
      .map(stringListEntry -> stringListEntry.getKey() + ": " + stringListEntry.getValue().size() + ' ' + stringListEntry.getValue())
      .collect(Collectors.joining());

    final String animations = animHashes.entrySet().stream()
      .sorted(Comparator.comparingInt((Map.Entry<String, List<String>> o) -> o.getValue().size()).reversed())
      .map(stringListEntry -> stringListEntry.getKey() + ": " + stringListEntry.getValue().size() + ' ' + stringListEntry.getValue() + '\n')
      .collect(Collectors.joining());

    Files.writeString(Path.of("./models.txt"), models, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    Files.writeString(Path.of("./animations.txt"), animations, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private static String hashToString(final byte[] hash) {
    final StringBuilder builder = new StringBuilder(hash.length * 2);

    for(final byte b : hash) {
      builder.append("%02x".formatted(b));
    }

    return builder.toString();
  }
}
