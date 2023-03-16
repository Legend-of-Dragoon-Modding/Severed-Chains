package legend.game.unpacker;

import legend.core.MathHelper;

import java.util.HashMap;

public record DirectoryEntry(String name, int entryLength, int sector, int length, boolean isDirectory, IsoReader reader, HashMap<String, DirectoryEntry> children) {
  public static DirectoryEntry fromArray(final IsoReader reader, final byte[] data, final int offset) {
    final int entryLength = data[offset];
    final int sector = (int)MathHelper.get(data, offset +  2, 4);
    final int length = (int)MathHelper.get(data, offset + 10, 4);
    final int flags = data[offset + 25];
    final boolean isDirectory = (flags & 0x2) != 0;

    final int nameLength = data[offset + 32];
    final String originalName = new String(data, offset + 33, nameLength);
    final String name;

    if("\0".equals(originalName)) {
      name = ".";
    } else if("\1".equals(originalName)) {
      name = "..";
    } else if(originalName.contains(";")) {
      name = originalName.substring(0, originalName.lastIndexOf(';'));
    } else {
      name = originalName;
    }

    return new DirectoryEntry(name, entryLength, sector, length, isDirectory, reader, new HashMap<>());
  }
}
