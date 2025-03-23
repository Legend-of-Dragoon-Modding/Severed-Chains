package legend.game.sound;

public interface SssqReader {
  int readByteAbsolute(final int absoluteOffset);
  int offset();
  void jump(final int offset);

  default Sssq.ChannelInfo channelInfo(final int index) { throw new UnsupportedOperationException(); }
  default int baseVolume() { throw new UnsupportedOperationException(); }
  default void baseVolume(final int volume) { throw new UnsupportedOperationException(); }

  default int readByte(final int offset) {
    return this.readByteAbsolute(this.offset() + offset);
  }

  default int readShort(final int offset) {
    return this.readByte(offset + 1) << 8 | this.readByte(offset);
  }

  default void advance(final int amount) {
    this.jump(this.offset() + amount);
  }

  default void rewind(final int amount) {
    this.jump(this.offset() - amount);
  }

  default void advance() {
    this.advance(1);
  }

  default void rewind() {
    this.rewind(1);
  }
}
