package legend.core.audio.assets;

import it.unimi.dsi.fastutil.Pair;
import legend.game.unpacker.FileData;

public final class InstrumentLayer {
  private final int keyRangeMinimum;
  private final int keyRangeMaximum;
  private final int keyRoot;
  /** Originally sixteenths. Increased for more accuracy during modulation */
  private final int sixtyFourths;
  private final Pair<short[][], int[]> pcm;
  private final AdsrPhase[] adsr;
  private final int lockedVolume;
  private final int volume;
  private final int pan;
  private final int pitchBendMultiplier;
  private final int breathControlIndex;
  private final int flags;

  InstrumentLayer(final FileData data, final SoundBank soundBank) {
    this.keyRangeMinimum = data.readUByte(0x00);
    this.keyRangeMaximum = data.readUByte(0x01);
    this.keyRoot = data.readUByte(0x02);
    this.sixtyFourths = data.readByte(0x03) * 4;
    this.pcm = soundBank.getEntry(data.readUShort(0x04) * 8);
    this.adsr = AdsrPhase.getPhases(data.readUByte(0x06), data.readUByte(0x08));
    this.lockedVolume = data.readUByte(0x0A);
    this.volume = data.readUByte(0x0B);
    this.pan = data.readUByte(0x0C);
    this.pitchBendMultiplier = data.readUByte(0x0D);
    this.breathControlIndex = data.readUByte(0x0E);
    this.flags = data.readUByte(0x0F);
  }

  boolean canPlayNote(final int note) {
    return note >= this.keyRangeMinimum && note <= this.keyRangeMaximum;
  }

  int getKeyRoot() {
    return this.keyRoot;
  }

  int getSixtyFourths() {
    return this.sixtyFourths;
  }

  Pair<short[][], int[]> getPcm() {
    return this.pcm;
  }

  AdsrPhase[] getAdsr() {
    return this.adsr;
  }

  int getLockedVolume() {
    return this.lockedVolume;
  }

  int getVolume() {
    return this.volume;
  }

  int getPan() {
    return this.pan;
  }

  int getPitchBendMultiplier() {
    return this.pitchBendMultiplier;
  }

  int getBreathControlIndex() {
    return this.breathControlIndex;
  }
}
