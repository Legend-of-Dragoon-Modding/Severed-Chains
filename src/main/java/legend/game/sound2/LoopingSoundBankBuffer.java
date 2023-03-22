package legend.game.sound2;

import java.nio.ShortBuffer;

final class LoopingSoundBankBuffer implements SoundBankBuffer {
  private final ShortBuffer pcm;

  LoopingSoundBankBuffer(final ShortBuffer pcm) {
    this.pcm = pcm;
  }

  @Override
  public short[] get(final int length) {
    final short[] result = new short[length];

    if(length <= this.pcm.remaining()) {
      this.pcm.get(result);
      return result;
    }

    int read = this.pcm.remaining();
    this.pcm.get(0, result, 0, read);

    while (read < length) {
     final int toRead = Math.min(this.pcm.capacity(), length - read);
     this.pcm.get(0, result, read, toRead);
     read += toRead;
    }

    return result;
  }

  @Override
  public void position(final int position) {
    this.pcm.position(position % this.pcm.capacity());
  }

  @Override
  public int position() {
    return this.pcm.position();
  }
}
