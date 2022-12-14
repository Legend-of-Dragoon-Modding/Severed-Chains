package legend.game.unpacker;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.Tuple;
import legend.core.cdrom.IsoReader;
import legend.game.Scus94491;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Unpacker {
  static {
    System.setProperty("log4j.skipJansi", "false");
    System.setProperty("log4j2.configurationFile", "log4j2.xml");
  }

  public static final Logger LOGGER = LogManager.getFormatterLogger(Unpacker.class);

  public static Path ROOT = Path.of(".", "files");

  public static void main(final String[] args) throws UnpackerException {
    unpack();
  }

  public static byte[] loadFile(String name) {
    if(name.contains(";")) {
      name = name.substring(0, name.lastIndexOf(";"));
    }

    if(name.startsWith("\\")) {
      name = name.substring(1);
    }

    try {
      return Files.readAllBytes(ROOT.resolve(name));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load file " + name, e);
    }
  }

  public static void unpack() throws UnpackerException {
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
        .map(Unpacker::decompress)
        .forEach(Unpacker::writeFile);
    } catch(final IOException e) {
      throw new UnpackerException(e);
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
    final byte[] fileData = new byte[entry.length()];
    entry.reader().readSectors(entry.sector(), fileData);
    return new Tuple<>(e.getKey(), fileData);
  }

  private static Tuple<String, byte[]> decompress(final Tuple<String, byte[]> e) {
    if(MathHelper.get(e.b(), 4, 4) != 0x1a455042L) {
      return e;
    }

    return new Tuple<>(e.a(), Scus94491.decompress(e.b()));
  }

  private static void writeFile(final Tuple<String, byte[]> e) {
    final Path path = ROOT.resolve(e.a());

    LOGGER.info("Unpacking %s...", e.a());

    try {
      Files.createDirectories(path.getParent());
      Files.write(path, e.b());
    } catch(final IOException ex) {
      throw new UnpackerException(ex);
    }
  }
}
