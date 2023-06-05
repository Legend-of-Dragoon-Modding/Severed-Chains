package legend.game.sound;

import legend.game.unpacker.FileData;

public class Sequence {
  private final FileData data;
  private final int offset;

  public Sequence(final FileData data, final int offset) {
    this.data = data;
    this.offset = offset;
  }

  public Reader reader() {
    return new Reader();
  }

  public class Reader implements SssqReader {
    private int offset;

    @Override
    public int readByteAbsolute(final int absoluteOffset) {
      return Sequence.this.data.readUByte(Sequence.this.offset + absoluteOffset);
    }

    @Override
    public int offset() {
      return this.offset;
    }

    @Override
    public void jump(final int offset) {
      this.offset = offset;
    }
  }
}
