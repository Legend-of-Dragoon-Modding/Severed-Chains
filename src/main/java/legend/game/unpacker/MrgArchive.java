package legend.game.unpacker;

import java.util.Iterator;

public class MrgArchive implements Iterable<MrgArchive.Entry> {
  public static final int MAGIC = 0x1a47524d;

  private final Entry[] entries;

  public MrgArchive(final FileData data, final boolean sectorAligned) {
    this.entries = new Entry[data.readInt(0x4)];

    // Load non-virtual files
    outer:
    for(int i = 0; i < this.entries.length; i++) {
      final int size = data.readInt(0x8 + i * 0x8 + 0x4);

      if(size != 0) {
        final int offset = data.readInt(0x8 + i * 0x8);

        for(int duplicateIndex = 0; duplicateIndex < i; duplicateIndex++) {
          if(this.entries[duplicateIndex] != null && this.entries[duplicateIndex].offset == offset) {
            this.entries[i] = new Entry(offset, size, size, duplicateIndex);
            continue outer;
          }
        }

        final Entry fileData;
        if(sectorAligned) {
          fileData = new Entry(offset * 0x800, size);
        } else {
          fileData = new Entry(offset, size);
        }

        this.entries[i] = fileData;
      }
    }

    // Load virtual files
    for(int i = 0; i < this.entries.length; i++) {
      final int size = data.readInt(0x8 + i * 0x8 + 0x4);

      if(size == 0) {
        final int offset = data.readInt(0x8 + i * 0x8);
        final int parentIndex = this.getRealEntryByOffset(offset);

        if(parentIndex != -1) {
          final Entry parent = this.getEntry(parentIndex);
          this.entries[i] = new Entry(parent.offset, parent.size, size, parentIndex);
        } else {
          this.entries[i] = new Entry(offset, size);
        }
      }
    }
  }

  public int getCount() {
    return this.entries.length;
  }

  public Entry getEntry(final int index) {
    return this.entries[index];
  }

  private int getRealEntryByOffset(final int offset) {
    for(int i = 0; i < this.entries.length; i++) {
      if(this.entries[i] != null && !this.entries[i].virtual() && this.entries[i].offset() == offset) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public Iterator<Entry> iterator() {
    return new Iterator<>() {
      private int i;

      @Override
      public boolean hasNext() {
        return this.i < MrgArchive.this.entries.length;
      }

      @Override
      public Entry next() {
        return MrgArchive.this.entries[this.i++];
      }
    };
  }

  public record Entry(int offset, int size, int virtualSize, int parent) {
    public Entry(final int offset, final int size) {
      this(offset, size, size, -1);
    }

    public boolean virtual() {
      return this.parent != -1;
    }
  }
}
