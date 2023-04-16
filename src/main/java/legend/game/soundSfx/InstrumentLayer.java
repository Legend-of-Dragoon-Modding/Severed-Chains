package legend.game.soundSfx;

import legend.core.audio.MidiInstrumentLayer;
import legend.game.unpacker.FileData;

final class InstrumentLayer implements MidiInstrumentLayer {
  private final int minimumKeyRange;
  private final int maximumKeyRange;
  private final int rootKey;
  private final int cents;
  private final double volume;
  private final int pan;
  private final int pitchBendMultiplier;
  private final boolean noise;
  private final boolean modulation;
  private final boolean reverb;
  private final AdsrPhase[] adsrPhases;
  private final SoundBank soundBank;
  private final int soundBankOffset;

  InstrumentLayer(final FileData data, final SoundBank soundBank) {
    this.minimumKeyRange = data.readUByte(0);
    this.maximumKeyRange = data.readUByte(1);
    this.rootKey = data.readUByte(2);
    this.cents = data.readUByte(3);
    this.soundBank = soundBank;
    this.soundBankOffset = data.readUShort(4) * 8;
    this.adsrPhases = AdsrPhase.getPhases(data.readUShort(6), data.readUShort(8));
    this.volume = data.readUByte(11) / 127d;
    this.pan = data.readUByte(12);
    this.pitchBendMultiplier = data.readUByte(13);

    this.noise = (data.readUByte(15) & 0x2) != 0;
    this.modulation = (data.readUByte(15) & 0x20) != 0;
    this.reverb = (data.readUByte(15) & 0x80) != 0;
  }

  int getMinimumKeyRange() {
    return this.minimumKeyRange;
  }

  int getMaximumKeyRange() {
    return this.maximumKeyRange;
  }

  @Override
  public int getRootKey() {
    return this.rootKey;
  }

  @Override
  public int getPitchBendMultiplier() {
    return this.pitchBendMultiplier;
  }

  @Override
  public int getCents() {
    return this.cents;
  }

  @Override
  public double getVolume() {
    return this.volume;
  }

  int getPan() {
    return this.pan;
  }


  @Override
  public AdsrEnvelope getAdsrEnvelope() {
    return new AdsrEnvelope(this.adsrPhases);
  }

  @Override
  public SoundBankEntry getSoundFontEntry() {
    return this.soundBank.getEntry(this.soundBankOffset);
  }
}
