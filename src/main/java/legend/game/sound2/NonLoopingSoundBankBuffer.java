package legend.game.sound2;

import java.nio.ShortBuffer;

final class NonLoopingSoundBankBuffer implements SoundBankBuffer {
  private final ShortBuffer pcm;

  NonLoopingSoundBankBuffer(final ShortBuffer pcm) {
    this.pcm = pcm;
  }

  @Override
  public short[] get(final int length) {
    final short[] result = new short[length];

    if(this.pcm.remaining() > 0) {
      this.pcm.get(this.pcm.position(), result, 0, Math.min(this.pcm.remaining(), result.length));
      this.position(Math.min(this.position() + length, this.pcm.limit()));
    }

    return result;
  }

  @Override
  public void position(final int position) {
    this.pcm.position(position);
  }

  @Override
  public int position() {
    return this.pcm.position();
  }
}
