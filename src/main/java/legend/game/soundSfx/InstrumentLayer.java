package legend.game.soundSfx;

import legend.core.audio.MidiInstrumentLayer;
import legend.game.unpacker.FileData;

final class InstrumentLayer implements MidiInstrumentLayer {
  private final int minimumKeyRange;
  private final int maximumKeyRange;
  private final int rootKey;
  private final int cents;
  private final double lockedVolume;
  private final double volume;
  private final int pan;
  private final int pitchBendMultiplier;

  private final int flags;
  private final boolean lockedPanAndVolume;
  private final boolean noise;
  private final boolean pitchBendMultiplierFromInstrument;
  private final boolean modulation;
  private final boolean reverb;
  private final AdsrPhase[] adsrPhases;
  private final SoundBank soundBank;
  private final int soundBankOffset;
  private final int _0e;

  InstrumentLayer(final FileData data, final SoundBank soundBank) {
    this.minimumKeyRange = data.readUByte(0x00);
    this.maximumKeyRange = data.readUByte(0x01);
    this.rootKey = data.readUByte(0x02);
    this.cents = data.readUByte(0x03);
    this.soundBank = soundBank;
    this.soundBankOffset = data.readUShort(0x04) * 8;
    this.adsrPhases = AdsrPhase.getPhases(data.readUShort(0x06), data.readUShort(0x08));
    this.lockedVolume = data.readUByte(0x0a) / 127d;
    this.volume = data.readUByte(0x0b) / 127d;
    this.pan = data.readUByte(0x0c);
    this.pitchBendMultiplier = data.readUByte(0x0d);
    this._0e = data.readUByte(0x0e);

    /**
     * <li>
     *   <ul>0x01 - This seems to lock pan and volume</ul>
     *   <ul>0x02 - Noise on</ul>
     *   <ul>0x10 - Use pitch bend from instrument instead of layer</ul>
     *   <ul>0x20 - Can use Modulation</ul>
     *   <ul>0x40 - Use the unknown 0x7f value from instrument instead of layer (Only if modulation is on)</ul>
     *   <ul>0x80 - Reverb on</ul>
     * </li>
     */
    this.flags = data.readUByte(0x0f);

    this.lockedPanAndVolume = (this.flags & 0x1) != 0;
    this.noise = (this.flags & 0x2) != 0;
    this.pitchBendMultiplierFromInstrument = (this.flags & 0x10) != 0;
    this.modulation = (this.flags & 0x20) != 0;
    this.reverb = (this.flags & 0x80) != 0;
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
  public double getLockedVolume() {
    return this.lockedVolume;
  }

  @Override
  public double getVolume() {
    return this.volume;
  }

  @Override
  public int getPan() {
    return this.pan;
  }

  @Override
  public boolean isPitchBendMultiplierFromInstrument() {
    return this.pitchBendMultiplierFromInstrument;
  }

  @Override
  public boolean isLockedPanAndVolume() {
    return this.lockedPanAndVolume;
  }

  @Override
  public boolean isModulation() {
    return this.modulation;
  }

  @Override
  public int getFlags() {
    return this.flags;
  }

  @Override
  public int get_0e() {
    return this._0e;
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
