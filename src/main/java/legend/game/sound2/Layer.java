package legend.game.sound2;

import legend.core.IoHelper;
import legend.core.MathHelper;

import java.nio.ByteBuffer;

final class Layer {
  private final int minimumKeyRange;
  private final int maximumKeyRange;
  private final int rootKey;
  private final int cents;
  private final SoundBankBuffer sample;


  Layer(final byte[] data, SoundBank soundBank) {
    this.minimumKeyRange = data[0];
    this.maximumKeyRange = data[1];
    this.rootKey = data[2];
    this.cents = data[3];
    this.sample = soundBank.getSoundBankBuffer(IoHelper.readShort(data, 4) * 8);
  }

  public SoundBankBuffer getSample() {
    return this.sample;
  }
}
