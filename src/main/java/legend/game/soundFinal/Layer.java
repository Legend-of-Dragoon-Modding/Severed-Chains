package legend.game.soundFinal;

import legend.core.IoHelper;

final class Layer {
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

  Layer(final byte[] data, final SoundBank soundBank) {
    this.minimumKeyRange = data[0];
    this.maximumKeyRange = data[1];
    this.rootKey = data[2];
    this.cents = data[3];
    this.soundBank = soundBank;
    this.soundBankOffset = IoHelper.readShort(data, 4) * 8;
    this.adsrPhases = AdsrPhase.getPhases(IoHelper.readUShort(data, 6), IoHelper.readUShort(data, 8));
    this.volume = (data[11] & 0xff) / 127d;
    this.pan = (data[12] & 0xff);
    this.pitchBendMultiplier = data[13];

    this.noise = (data[15] & 0x2) != 0;
    this.modulation = (data[15] & 0x20) != 0;
    this.reverb = (data[15] & 0x80) != 0;
  }

  int getMinimumKeyRange() {
    return this.minimumKeyRange;
  }

  int getMaximumKeyRange() {
    return this.maximumKeyRange;
  }

  int getRootKey() {
    return this.rootKey;
  }

  int getPitchBendMultiplier() {
    return this.pitchBendMultiplier;
  }

  int getCents() {
    return this.cents;
  }

  double getVolume() {
    return this.volume;
  }

  int getPan() {
    return this.pan;
  }

  AdsrEnvelope getAdsrEnvelope(final int adsrLevel) {
    return new AdsrEnvelope(this.adsrPhases, adsrLevel);
  }

  SoundBankEntry getSoundBankEntry() {
    return this.soundBank.getEntry(this.soundBankOffset);
  }
}
