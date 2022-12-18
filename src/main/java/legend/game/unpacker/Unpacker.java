package legend.game.unpacker;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class Unpacker {
  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");
  }

  public static final Logger LOGGER = LogManager.getFormatterLogger(Unpacker.class);

  public static Path ROOT = Path.of(".", "files");

  private static final Object IO_LOCK = new Object();

  private static final Map<BiPredicate<String, byte[]>, BiFunction<String, byte[], Map<String, byte[]>>> transformers = new HashMap<>();
  static {
    transformers.put(Unpacker::decompressDescriminator, Unpacker::decompress);
  }

  public static void main(final String[] args) throws UnpackerException {
    unpack();
  }

  public static byte[] loadFile(final String name) {
    LOGGER.info("Loading file %s", name);

    synchronized(IO_LOCK) {
      try {
        return Files.readAllBytes(ROOT.resolve(fixPath(name)));
      } catch(final IOException e) {
        throw new RuntimeException("Failed to load file " + name, e);
      }
    }
  }

  public static boolean exists(final String name) {
    synchronized(IO_LOCK) {
      return Files.exists(ROOT.resolve(fixPath(name)));
    }
  }

  private static String fixPath(String name) {
    if(name.contains(";")) {
      name = name.substring(0, name.lastIndexOf(";"));
    }

    if(name.startsWith("\\")) {
      name = name.substring(1);
    }

    return name.replace('\\', '/');
  }

  public static void unpack() throws UnpackerException {
    synchronized(IO_LOCK) {
      try {
        final DirectoryEntry[] roots = new DirectoryEntry[4];
        final String[] ids = {"SCUS94491", "SCUS94584", "SCUS94585", "SCUS94586"};

        final IsoReader reader4 = new IsoReader(Path.of(".", "isos", "4.iso"));
        final DirectoryEntry root = loadRoot(reader4, ids[3], null);

        for(int i = 0; i < roots.length; i++) {
          final IsoReader reader = new IsoReader(Path.of(".", "isos", (i + 1) + ".iso"));
          loadRoot(reader, ids[i], root);
        }

        final Map<String, DirectoryEntry> files = new HashMap<>();
        getFiles(root, "", files);

        files.entrySet()
          .stream()
          .filter(entry -> !Files.exists(ROOT.resolve(entry.getKey())))
          .map(Unpacker::readFile)
          .map((Tuple<String, byte[]> e) -> transform(e.a(), e.b()))
          .forEach(Unpacker::writeFiles);
      } catch(final IOException e) {
        throw new UnpackerException(e);
      }
    }
  }

  private static DirectoryEntry loadRoot(final IsoReader reader, final String id, @Nullable final DirectoryEntry destinationTree) throws IOException, UnpackerException {
    final byte[] sectorData = new byte[0x800];
    final ByteBuffer sectorBuffer = ByteBuffer.wrap(sectorData);
    sectorBuffer.order(ByteOrder.LITTLE_ENDIAN);

    reader.seekSector(16);
    reader.advance(12);
    reader.read(sectorData);

    if(sectorBuffer.get() != 1) {
      throw new UnpackerException("Invalid volume descriptor, expected primary");
    }

    if(!"CD001".equals(IoHelper.readString(sectorBuffer, 5))) {
      throw new UnpackerException("Invalid volume descriptor, expected CD001");
    }

    if(sectorBuffer.get() != 0x1) {
      throw new UnpackerException("Invalid volume descriptor, expected version 1");
    }

    if(!"PLAYSTATION".equals(IoHelper.readString(sectorBuffer, 32).trim())) {
      throw new UnpackerException("Invalid volume descriptor, expected PLAYSTATION");
    }

    if(!id.equals(IoHelper.readString(sectorBuffer, 32).trim())) {
      throw new UnpackerException("Invalid volume descriptor, expected " + id);
    }

    final DirectoryEntry rootDir = DirectoryEntry.fromArray(reader, sectorData, 0x9c);
    populateDirectoryTree(rootDir, destinationTree == null ? rootDir : destinationTree);
    return rootDir;
  }

  private static void populateDirectoryTree(final DirectoryEntry source, final DirectoryEntry destinationTree) throws IOException {
    final byte[] sectorData = new byte[0x800];

    source.reader().seekSector(source.sector());
    source.reader().advance(12);
    source.reader().read(sectorData);
    int sectorOffset = 0;

    while(sectorData[sectorOffset] != 0) {
      final DirectoryEntry directory = DirectoryEntry.fromArray(source.reader(), sectorData, sectorOffset);

      if(!".".equals(directory.name()) && !"..".equals(directory.name())) {
        destinationTree.children().putIfAbsent(directory.name(), directory);

        if(directory.isDirectory()) {
          populateDirectoryTree(directory, destinationTree.children().get(directory.name()));
        }
      }

      sectorOffset += directory.entryLength();
    }
  }

  private static void getFiles(final DirectoryEntry root, final String path, final Map<String, DirectoryEntry> files) {
    if(!root.isDirectory()) {
      files.put(path + root.name(), root);
    } else {
      for(final DirectoryEntry entry : root.children().values()) {
        if(".".equals(root.name())) {
          getFiles(entry, path, files);
        } else {
          getFiles(entry, path + root.name() + '/', files);
        }
      }
    }
  }

  private static Tuple<String, byte[]> readFile(final Map.Entry<String, DirectoryEntry> e) {
    final DirectoryEntry entry = e.getValue();
    final byte[] fileData = entry.reader().readSectors(entry.sector(), entry.length(), e.getKey().endsWith(".IKI"));
    return new Tuple<>(e.getKey(), fileData);
  }

  private static Map<String, byte[]> transform(final String name, final byte[] data) {
    final Map<String, byte[]> entries = new HashMap<>();

    for(final var entry : transformers.entrySet()) {
      final var descriminator = entry.getKey();
      final var transformer = entry.getValue();

      if(descriminator.test(name, data)) {
        transformer.apply(name, data)
          .entrySet().stream()
          .map(e -> transform(e.getKey(), e.getValue()))
          .forEach(entries::putAll);
        break;
      }

      entries.put(name, data);
    }

    return entries;
  }

  private static boolean decompressDescriminator(final String name, final byte[] data) {
    return MathHelper.get(data, 4, 4) == 0x1a455042;
  }

  private static Map<String, byte[]> decompress(final String name, final byte[] data) {
    return Map.of(name, Unpacker.decompress(data));
  }

  private static void writeFiles(final Map<String, byte[]> files) {
    files.forEach(Unpacker::writeFile);
  }

  private static void writeFile(final String name, final byte[] data) {
    final Path path = ROOT.resolve(name);

    LOGGER.info("Unpacking %s...", name);

    try {
      Files.createDirectories(path.getParent());
      Files.write(path, data);
    } catch(final IOException ex) {
      throw new UnpackerException(ex);
    }
  }

  public static byte[] decompress(final byte[] archive) {
    // Check BPE header - this check is in the BPE block method but not the main EXE method
    if(MathHelper.get(archive, 4, 4) != 0x1a455042L) {
      throw new RuntimeException("Attempted to decompress non-BPE segment");
    }

    LOGGER.info("Decompressing BPE segment");

    final byte[] dest = new byte[(int)MathHelper.get(archive, 0, 4)];

    final Deque<Byte> unresolved_byte_list = new LinkedList<>();
    final byte[] dict_leftch = new byte[0x100];
    final byte[] dict_rightch = new byte[0x100];

    int totalSize = 0;
    int archiveOffset = 8;
    int destinationOffset = 0;

    // Each block is preceded by 4-byte int up to 0x800 giving the number
    // of decompressed bytes in the block. 0x00000000 indicates that there
    // are no further blocks and decompression is complete.
    int bytes_remaining_in_block = (int)MathHelper.get(archive, archiveOffset, 4);
    archiveOffset += 4;

    while(bytes_remaining_in_block != 0) {
      if(bytes_remaining_in_block > 0x800) {
        LOGGER.error("Decompress: 0x%08x at offset 0x%08x is an invalid block size", bytes_remaining_in_block, archiveOffset - 4);
        throw new RuntimeException("Decompression error");
      }

      totalSize += bytes_remaining_in_block;

      // Build the initial dictionary/lookup table. The left-character dict
      // is filled so that each key contains itself as a value, while the
      // right-character dict is filled with empty values.
      Arrays.fill(dict_rightch, (byte)0);
      for(int i = 0; i < 0x100; i++) {
        dict_leftch[i] = (byte)i;
      }

      // Build adaptive dictionary.
      int key = 0;
      while(key < 0x100) {
        // Dictionary is 256 bytes long. Loop until all keys filled.
        // If byte_pairs_to_read is >= 0x80, then only the next byte will
        // be read into the dictionary, placed at the index value calculated
        // using the below formula. Otherwise, the byte indicates how many
        // sequential bytes to read into the dictionary.
        int byte_pairs_to_read = archive[archiveOffset] & 0xff;
        archiveOffset++;

        if(byte_pairs_to_read >= 0x80) {
          key = key - 0x7f + byte_pairs_to_read;
          byte_pairs_to_read = 0;
        }

        // For each byte/byte pair to read, read the next byte and add it
        // to the leftch dict at the current key. If the character matches
        // the key it's at, increment key and continue. If it does not,
        // read the next character and add it to the same key in the
        // rightch dict before incrementing key and continuing.
        if(key < 0x100) {
          // Check that dictionary length not exceeded.
          for(int i = 0; i < byte_pairs_to_read + 1; i++) {
            dict_leftch[key] = archive[archiveOffset];
            archiveOffset++;

            if((dict_leftch[key] & 0xff) != key) {
              dict_rightch[key] = archive[archiveOffset];
              archiveOffset++;
            }

            key++;
          }
        }
      }

      // Decompress block
      // On each pass, read one byte and add it to a list of unresolved bytes.
      while(bytes_remaining_in_block > 0) {
        unresolved_byte_list.clear();
        unresolved_byte_list.push(archive[archiveOffset]);
        archiveOffset++;

        // Pop the first item in the list of unresolved bytes. If the
        // byte key == value in dict_leftch, append it to the list of
        // decompressed bytes. If the byte key !=value in dict_leftch,
        // insert the leftch followed by rightch to the unresolved byte
        // list. Loop until the unresolved byte list is empty.
        while(!unresolved_byte_list.isEmpty()) {
          final byte compressed_byte = unresolved_byte_list.pop();
          if(compressed_byte == dict_leftch[compressed_byte & 0xff]) {
            dest[destinationOffset] = compressed_byte;
            destinationOffset++;
            bytes_remaining_in_block--;
          } else {
            unresolved_byte_list.push(dict_rightch[compressed_byte & 0xff]);
            unresolved_byte_list.push(dict_leftch[compressed_byte & 0xff]);
          }
        }
      }

      if(archiveOffset % 4 != 0) {
        // Word - align the pointer.
        archiveOffset = archiveOffset + 4 - archiveOffset % 4;
      }

      bytes_remaining_in_block = (int)MathHelper.get(archive, archiveOffset, 4);
      archiveOffset += 4;
    }

    LOGGER.info("Archive size: %d, decompressed size: %d", archiveOffset, totalSize);

    return dest;
  }
}
