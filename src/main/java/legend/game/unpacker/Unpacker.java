package legend.game.unpacker;

import legend.core.IoHelper;
import legend.core.cdrom.IsoReader;
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
    new Unpacker().unpack();
  }

  public void unpack() throws UnpackerException {
    try {
      final DirectoryEntry[] roots = new DirectoryEntry[4];
      final String[] ids = {"SCUS94491", "SCUS94584", "SCUS94585", "SCUS94586"};

      final IsoReader reader4 = new IsoReader(Path.of(".", "isos", "4.iso"));
      final DirectoryEntry root = this.loadRoot(reader4, ids[3], null);

      for(int i = 0; i < roots.length; i++) {
        final IsoReader reader = new IsoReader(Path.of(".", "isos", (i + 1) + ".iso"));
        this.loadRoot(reader, ids[i], root);
      }

      final Map<String, DirectoryEntry> files = new HashMap<>();
      this.getFiles(root, "", files);

      for(final var e : files.entrySet()) {
        final Path path = ROOT.resolve(e.getKey());

        if(!Files.exists(path)) {
          LOGGER.info("Unpacking %s...", e.getKey());

          Files.createDirectories(path.getParent());

          final DirectoryEntry entry = e.getValue();
          final byte[] fileData = new byte[entry.length()];
          entry.reader().readSectors(entry.sector(), fileData);
          Files.write(path, fileData);
        }
      }
    } catch(final IOException e) {
      throw new UnpackerException(e);
    }
  }

  private DirectoryEntry loadRoot(final IsoReader reader, final String id, @Nullable final DirectoryEntry destinationTree) throws IOException, UnpackerException {
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
    this.populateDirectoryTree(rootDir, destinationTree == null ? rootDir : destinationTree);
    return rootDir;
  }

  private void populateDirectoryTree(final DirectoryEntry source, final DirectoryEntry destinationTree) throws IOException {
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
          this.populateDirectoryTree(directory, destinationTree.children().get(directory.name()));
        }
      }

      sectorOffset += directory.entryLength();
    }
  }

  private void getFiles(final DirectoryEntry root, final String path, final Map<String, DirectoryEntry> files) {
    if(!root.isDirectory()) {
      files.put(path + root.name(), root);
    } else {
      for(final DirectoryEntry entry : root.children().values()) {
        if(".".equals(root.name())) {
          this.getFiles(entry, path, files);
        } else {
          this.getFiles(entry, path + root.name() + '/', files);
        }
      }
    }
  }
}
