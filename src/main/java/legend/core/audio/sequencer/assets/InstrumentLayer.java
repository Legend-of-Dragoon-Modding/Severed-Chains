package legend.core.audio.sequencer.assets;

import legend.game.unpacker.FileData;

public final class InstrumentLayer {
  private final int keyRangeMinimum;
  private final int keyRangeMaximum;
  private final int keyRoot;
  /** Originally sixteenths. Increased for more accuracy during pitch bending, pitch modulation and portamento */
  private final int sixtyFourths;
  private final byte[] soundBankEntry;
  private final AdsrPhase[] adsr;
  private final int lockedVolume;
  private final float volume;
  private final int pan;
  private final int pitchBendMultiplier;
  private final int breathControlIndex;

  // Flags
  private final boolean highPriority;
  private final boolean noise;
  private final boolean pitchBendMultiplierFromInstrument;
  private final boolean modulation;
  private final boolean breathControlIndexFromInstrument;
  private final boolean reverb;


  InstrumentLayer(final FileData data, final SoundBank soundBank) {
    this.keyRangeMinimum = data.readUByte(0x00);
    this.keyRangeMaximum = data.readUByte(0x01);
    this.keyRoot = data.readUByte(0x02);
    this.sixtyFourths = data.readByte(0x03) * 4;
    this.soundBankEntry = soundBank.getEntry(data.readUShort(0x04) * 8);
    this.adsr = AdsrPhase.getPhases(data.readUShort(0x06), data.readUShort(0x08));
    this.lockedVolume = data.readUByte(0x0A);
    this.volume = data.readUByte(0x0B) / 128f;
    this.pan = data.readUByte(0x0C);
    this.pitchBendMultiplier = data.readUByte(0x0D);
    this.breathControlIndex = data.readUByte(0x0E);

    final int flags = data.readUByte(0x0F);
    this.highPriority = (flags & 0x01) != 0;
    this.noise = (flags & 0x02) != 0;
    this.pitchBendMultiplierFromInstrument = (flags & 0x10) != 0;
    this.modulation = (flags & 0x20) != 0;
    this.breathControlIndexFromInstrument = (flags & 0x40) != 0;
    this.reverb = (flags & 0x80) != 0;
  }

  boolean canPlayNote(final int note) {
    return note >= this.keyRangeMinimum && note <= this.keyRangeMaximum;
  }

  public int getKeyRoot() {
    return this.keyRoot;
  }

  public int getSixtyFourths() {
    return this.sixtyFourths;
  }

  public byte[] getSoundBankEntry() {
    return this.soundBankEntry;
  }

  public AdsrPhase[] getAdsr() {
    return this.adsr;
  }

  public int getLockedVolume() {
    return this.lockedVolume;
  }

  public float getVolume() {
    return this.volume;
  }

  public int getPan() {
    return this.pan;
  }

  public int getPitchBendMultiplier() {
    return this.pitchBendMultiplier;
  }

  public int getBreathControlIndex() {
    return this.breathControlIndex;
  }

  public boolean isHighPriority() {
    return this.highPriority;
  }

  public boolean isNoise() {
    return this.noise;
  }

  public boolean isPitchBendMultiplierFromInstrument() {
    return this.pitchBendMultiplierFromInstrument;
  }

  public boolean isModulation() {
    return this.modulation;
  }

  public boolean isBreathControlIndexFromInstrument() {
    return this.breathControlIndexFromInstrument;
  }

  public boolean isReverb() {
    return this.reverb;
  }
}
