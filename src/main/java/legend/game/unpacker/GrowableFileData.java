package legend.game.unpacker;

public class GrowableFileData extends FileData {
  public GrowableFileData(final int startSize) {
    super(new byte[startSize]);
  }

  protected GrowableFileData(final byte[] data, final int offset, final int size) {
    super(data, offset, size);
  }

  @Override
  public FileData slice(final int offset, final int size) {
    this.checkBounds(offset, size);
    return new GrowableFileData(this.data, this.offset + offset, size);
  }

  @Override
  public int size() {
    return this.data.length;
  }

  @Override
  protected void checkBounds(final int offset, final int size) {
    if(offset < 0) {
      throw new IndexOutOfBoundsException("Negative offset " + offset);
    }

    if(size < 0) {
      throw new IndexOutOfBoundsException("Negative size " + size);
    }

    if(offset + size > this.size()) {
      final byte[] newData = new byte[this.size() * 2];
      System.arraycopy(this.data, 0, newData, 0, this.size());
      this.data = newData;
    }
  }
}
