package legend.game.sound2;

import legend.core.IoHelper;
import legend.core.MathHelper;

import java.nio.ByteBuffer;

final class Layer {
  private static final int BASE_SAMPLE_RATE = 37800;
  private final int minimumKeyRange;
  private final int maximumKeyRange;
  private final int rootKey;
  private final int cents;
  private final int volume;
  private final float pan;
  private final int pitchBendMultiplier;
  private final boolean noise;
  private final boolean modulation;
  private final boolean reverb;

  private final ADSR adsr;
  private final SoundBankBuffer sample;


  Layer(final byte[] data, final SoundBank soundBank) {
    this.minimumKeyRange = data[0];
    this.maximumKeyRange = data[1];
    this.rootKey = data[2];
    this.cents = data[3];
    this.sample = soundBank.getSoundBankBuffer(IoHelper.readShort(data, 4) * 8);
    this.adsr = new ADSR(IoHelper.readShort(data, 6), IoHelper.readShort(data, 8));
    this.volume = data[11];
    this.pan = (data[12] & 0xff) / 64f;
    this.pitchBendMultiplier = data[13];

    this.noise = (data[15] & 0x2) != 0;
    this.modulation = (data[15] & 0x20) != 0;
    this.reverb = (data[15] & 0x80) != 0;
  }

  public SoundBankBuffer getSample() {
    return this.sample;
  }

  public ADSR getAdsr() {
    return this.adsr;
  }

  public void resetAdsr() {
    this.adsr.reset();
  }

  public boolean noteInRange(final int note) {
    return (note >= this.minimumKeyRange && note <= this.maximumKeyRange) || note == this.rootKey;
  }

  public void resetBuffer() {
    this.sample.position(0);
  }


  public int calculateSampleRate(final int note, final int pitchBend) {
    final int semitoneOffset;
    final int octaveOffset;
    final double pitchBendMulti = Math.pow(2, (this.pitchBendMultiplier * ((pitchBend - 64) / 64d)) / 12);

    if(note < this.rootKey) {
      octaveOffset = (this.rootKey - note - 1) / 12 + 1;
      semitoneOffset = (octaveOffset * 12 - (this.rootKey - note)) % 12;

      final double semitoneMulti = Math.pow(1.05946d, semitoneOffset);
      final double centsMulti = Math.pow(1.0005946d, this.cents);

      return ((int) (BASE_SAMPLE_RATE * semitoneMulti * centsMulti * pitchBendMulti)) >> octaveOffset;
    } else {
      semitoneOffset = ((note - this.rootKey) % 12);
      octaveOffset = (note - this.rootKey) / 12;

      final double semitoneMulti = Math.pow(1.05946d, semitoneOffset);
      final double centsMulti = Math.pow(1.0005946d, this.cents);

      return ((int)(BASE_SAMPLE_RATE * semitoneMulti * centsMulti * pitchBendMulti)) << octaveOffset;
    }
  }


}
