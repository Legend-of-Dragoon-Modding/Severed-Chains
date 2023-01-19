package legend.game.unpacker;

import legend.core.MathHelper;

import java.util.Iterator;

public class DeffArchive implements Iterable<FileData> {
  private final FileData data;
  private final int[] ptrs;

  public DeffArchive(final FileData data) {
    this.data = data;

    // Allocate an extra so we can always do n+1 to look up the end of the file n
    this.ptrs = new int[this.getCount() + 1];
    for(int i = 0; i < this.ptrs.length - 1; i++) {
      this.ptrs[i] = (int)MathHelper.get(this.data.data(), this.data.offset() + 8 + i * 8 + 4, 4);
    }

    this.ptrs[this.ptrs.length - 1] = data.size();
  }

  public int getCount() {
    return (int)MathHelper.get(this.data.data(), this.data.offset() + 6, 2);
  }

  public FileData getFile(final int index) {
    final int offset = this.ptrs[index];
    final int size = this.ptrs[index + 1] - offset;

    return new FileData(this.data.data(), this.data.offset() + offset, size);
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
