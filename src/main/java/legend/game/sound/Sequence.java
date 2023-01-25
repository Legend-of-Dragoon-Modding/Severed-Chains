package legend.game.sound;

public class Sequence {
  private final byte[] data;
  private final int offset;

  public Sequence(final byte[] data, final int offset) {
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
      return Sequence.this.data[Sequence.this.offset + absoluteOffset] & 0xff;
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
