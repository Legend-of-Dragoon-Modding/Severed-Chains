package legend.game.unpacker;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public final class RefArchive {
  private static final Pattern MRG_ENTRY = Pattern.compile("[=;]");

  public Int2ObjectMap<Reference> files = new Int2ObjectArrayMap<>();

  public RefArchive(final PathNode mrg) {
    final String mrgContents = mrg.data.readFixedLengthAscii(0x0, mrg.data.size());

    mrgContents.lines().forEach(line -> {
      final String[] parts = MRG_ENTRY.split(line);

      if(parts.length != 3) {
        throw new RuntimeException("Invalid MRG entry! " + line);
      }

      final int virtual = Integer.parseInt(parts[0]);

      // Indicates no file
      if(parts[1].isBlank()) {
        return;
      }

      if(parts[0].equals(parts[1])) {
        this.files.put(virtual, new Reference(false, parts[1], Integer.parseInt(parts[2])));
        return;
      }

      this.files.put(virtual, new Reference(true, parts[1], 0));
    });
  }

  public FileData toFileData() {
    final StringBuilder sb = new StringBuilder();

    for(final var entry : this.files.int2ObjectEntrySet()) {
      final int key = entry.getIntKey();
      final Reference ref = entry.getValue();

      final String r = ref.virtual ? '@' + ref.ref : ref.ref;

      sb.append(key).append('=').append(r).append(';').append(ref.size).append('\n');
    }

    return new FileData(sb.toString().getBytes(StandardCharsets.US_ASCII));
  }

  public static class Reference {
    public boolean virtual;
    public String ref;
    public int size;

    private Reference(final boolean virtual, final String ref, final int size) {
      this.virtual = virtual;
      this.ref = ref;
      this.size = size;
    }
  }
}
