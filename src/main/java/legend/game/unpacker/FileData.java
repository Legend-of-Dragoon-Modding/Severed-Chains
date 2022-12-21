package legend.game.unpacker;

public record FileData(byte[] data, int offset, int size) {
  public FileData(final byte[] data) {
    this(data, 0, data.length);
  }
}
