package legend.game.unpacker;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class IsoReader {
  public static final int SECTOR_SIZE = 2352;
  public static final int SYNC_PATTER_SIZE = 12;

  private final Path path;
  private final RandomAccessFile file;

  public final int lba;

  public IsoReader(final Path path) throws IOException {
    this.path = path;
    this.file = new RandomAccessFile(path.toFile(), "r");
    this.lba = (int)(this.file.length() / SECTOR_SIZE);
  }

  public void close() throws IOException {
    this.file.close();
  }

  public long getPos() throws IOException {
    return this.file.getFilePointer();
  }

  public void setPos(final long pos) throws IOException {
    this.file.seek(pos);
  }

  public void seekSector(final long sector) throws IOException {
    this.file.seek(sector * SECTOR_SIZE + SYNC_PATTER_SIZE);
  }

  public void seekSectorRaw(final long sector) throws IOException {
    this.file.seek(sector * SECTOR_SIZE);
  }

  public void advance(final int amount) throws IOException {
    final int skipped = this.file.skipBytes(amount);

    if(skipped != amount) {
      throw new RuntimeException("Skipped the wrong number of bytes. End of file? Negative amount? (requested: " + amount + ", actual: " + skipped + ')');
    }
  }

  public void read(final byte[] out) throws IOException {
    this.file.read(out);
  }

  public void read(final byte[] out, final int offset, final int length) throws IOException {
    this.file.read(out, offset, length);
  }

  public byte[] readSectors(int sector, final int length, final boolean raw) {
    int sectorsRead = 0;
    int dataRead = 0;
    boolean endOfRecord = false;
    final int sectorCount = (length + 0x7ff) / 0x800;

    try {
      final byte[] sectorData = new byte[0x930];

      int sectorSize = 0;
      byte[] data = null;

      while(!endOfRecord) {
        this.seekSectorRaw(sector);
        this.read(sectorData);

        if(data == null) {
          sectorSize = raw || ((sectorData[16 + 2] >>> 5) & 1) != 0 ? 0x930 : 0x800; // Form2
          data = new byte[raw ? sectorSize * sectorCount : length];
        }

        endOfRecord = sectorsRead >= sectorCount - 1;

        if(raw) {
          System.arraycopy(sectorData, 0, data, dataRead, sectorSize);
        } else {
          System.arraycopy(sectorData, 24, data, dataRead, Math.min(sectorSize, length - dataRead));
        }

        dataRead += sectorSize;
        sector++;
        sectorsRead++;
      }

      return data;
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "ISO Reader " + this.path;
  }
}
