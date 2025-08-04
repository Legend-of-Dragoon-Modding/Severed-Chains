package legend.game.unpacker;

public class GrowableFileData extends FileData {
  private final GrowableFileData parent;

  public GrowableFileData(final int startSize) {
    super(new byte[startSize]);
    this.parent = null;
  }

  protected GrowableFileData(final GrowableFileData parent, final int offset, final int size) {
    super(parent.data, parent.offset + offset, size);
    this.parent = parent;
  }

  @Override
  public FileData slice(final int offset, final int size) {
    this.checkBounds(offset, size);
    return new GrowableFileData(this, offset, size);
  }

  @Override
  public int size() {
    return this.data.length;
  }

  private void updateArray(final byte[] data) {
    this.data = data;

    GrowableFileData parent = this.parent;
    while(parent != null) {
      parent.updateArray(data);
      parent = parent.parent;
    }
  }

  @Override
  protected void checkBounds(final int offset, final int size) {
    if(offset < 0) {
      throw new IndexOutOfBoundsException("Negative offset " + offset);
    }

    if(size < 0) {
      throw new IndexOutOfBoundsException("Negative size " + size);
    }

    final int requiredSize = this.offset + offset + size;
    if(requiredSize > this.size()) {
      int newSize = this.size() * 2;
      while(newSize < requiredSize) {
        newSize *= 2;
      }

      final byte[] newData = new byte[newSize];
      System.arraycopy(this.data, 0, newData, 0, this.size());
      this.updateArray(newData);
    }
  }
}
