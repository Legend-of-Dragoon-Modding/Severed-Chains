package legend.game.unpacker;

import java.util.Iterator;

public class DeffArchive implements Iterable<FileData> {
  private final FileData data;
  private final int[] ptrs;

  public DeffArchive(final FileData data) {
    this.data = data;

    // Allocate an extra so we can always do n+1 to look up the end of the file n
    this.ptrs = new int[this.getCount() + 1];
    for(int i = 0; i < this.ptrs.length - 1; i++) {
      this.ptrs[i] = this.data.readInt(8 + i * 8 + 4);
    }

    this.ptrs[this.ptrs.length - 1] = data.size();
  }

  public int getCount() {
    return this.data.readUShort(6);
  }

  public FileData getFile(final int index) {
    final int offset = this.ptrs[index];
    final int size = this.ptrs[index + 1] - offset;

    return this.data.slice(offset, size);
  }

  @Override
  public Iterator<FileData> iterator() {
    return new Iterator<>() {
      private int i;

      @Override
      public boolean hasNext() {
        return this.i < DeffArchive.this.getCount();
      }

      @Override
      public FileData next() {
        return DeffArchive.this.getFile(this.i++);
      }
    };
  }
}
