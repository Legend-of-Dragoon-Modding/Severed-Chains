package legend.game.unpacker;

import legend.core.MathHelper;

import java.util.Iterator;

public class MrgArchive implements Iterable<FileData> {
  private final FileData data;
  private final boolean sectorAligned;

  public MrgArchive(final FileData data, final boolean sectorAligned) {
    this.data = data;
    this.sectorAligned = sectorAligned;
  }

  public int getCount() {
    return (int)MathHelper.get(this.data.data(), this.data.offset() + 4, 4);
  }

  public FileData getFile(final int index) {
    final int offset = (int)MathHelper.get(this.data.data(), this.data.offset() + 8 + index * 8, 4);
    final int size = (int)MathHelper.get(this.data.data(), this.data.offset() + 8 + index * 8 + 4, 4);

    if(this.sectorAligned) {
      return new FileData(this.data.data(), this.data.offset() + offset * 0x800, size);
    }

    return new FileData(this.data.data(), this.data.offset() + offset, size);
  }

  @Override
  public Iterator<FileData> iterator() {
    return new Iterator<>() {
      private int i;

      @Override
      public boolean hasNext() {
        return this.i < MrgArchive.this.getCount();
      }

      @Override
      public FileData next() {
        return MrgArchive.this.getFile(this.i++);
      }
    };
  }
}
