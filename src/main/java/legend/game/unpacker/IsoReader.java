package legend.game.unpacker;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class IsoReader {
  private static final int SECTOR_SIZE = 2352;
  private static final int SYNC_PATTER_SIZE = 12;

  private final Path path;
  private final RandomAccessFile file;

  public final int lba;

  public IsoReader(final Path path) throws IOException {
    this.path = path;
    this.file = new RandomAccessFile(path.toFile(), "r");
    this.lba = (int)(this.file.length() / SECTOR_SIZE);
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

  public long readByte() throws IOException {
    return this.file.readByte() & 0xffL;
  }

  public void read(final byte[] out) throws IOException {
    this.file.read(out);
  }

  public void read(final byte[] out, final int offset, final int length) throws IOException {
    this.file.read(out, offset, length);
  }

  public byte[] readSectors(int sector, final int sectorCount, final boolean raw) {
    int dataRead = 0;
    boolean endOfRecord = false;

    try {
      this.seekSector(sector);
      final byte[] subheader = new byte[4];
      this.advance(4); //Skip header
      this.read(subheader, 0, 4);

      final int sectorSize = raw || ((subheader[2] >>> 5) & 1) != 0 ? 0x930 : 0x800;
      final byte[] data = new byte[sectorSize * sectorCount];

      while(!endOfRecord) {
        this.seekSector(sector);

        this.advance(4); //Skip header
        this.read(subheader, 0, 4);

        endOfRecord = (subheader[2] >>> 7 & 1) == 1;

        if(raw) {
          this.seekSectorRaw(sector);
        } else {
          this.advance(4); //Skip the other copy of subheader
        }

        this.read(data, dataRead, sectorSize);
        dataRead += sectorSize;
        sector++;
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
