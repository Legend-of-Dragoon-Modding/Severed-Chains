package legend.game.unpacker;

public class ExpandableFileData extends FileData {
  private final ExpandableFileData parent;
  private final int maximumSize;

  public ExpandableFileData(final int startSize) {
    this(startSize, Integer.MAX_VALUE);
  }

  public ExpandableFileData(final int startSize, final int maximumSize) {
    super(new byte[startSize]);
    if(startSize < 1 || maximumSize < startSize) throw new IllegalArgumentException("Invalid expandable file capacity");
    this.parent = null;
    this.maximumSize = maximumSize;
  }

  protected ExpandableFileData(final ExpandableFileData parent, final int offset, final int size) {
    super(parent.data, parent.offset + offset, size);
    this.parent = parent;
    this.maximumSize = parent.maximumSize;
  }

  @Override
  public FileData slice(final int offset, final int size) {
    this.checkBounds(offset, size);
    return new ExpandableFileData(this, offset, size);
  }

  @Override
  public int size() {
    return this.data.length;
  }

  private void updateArray(final byte[] data) {
    this.data = data;

    ExpandableFileData parent = this.parent;
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

    final long required = (long)this.offset + offset + size;
    if(required > this.maximumSize) throw new IllegalArgumentException("File exceeds maximum capacity " + this.maximumSize);
    final int requiredSize = (int)required;
    if(requiredSize > this.size()) {
      int newSize = (int)Math.min((long)this.size() * 2, this.maximumSize);
      while(newSize < requiredSize) {
        newSize = (int)Math.min((long)newSize * 2, this.maximumSize);
      }

      final byte[] newData = new byte[newSize];
      System.arraycopy(this.data, 0, newData, 0, this.size());
      this.updateArray(newData);
    }
  }
}
