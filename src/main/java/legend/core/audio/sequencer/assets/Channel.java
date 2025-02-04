package legend.core.audio.sequencer.assets;

import legend.game.unpacker.FileData;

public final class Channel {
  private final int index;
  private Instrument instrument;
  private float volume;
  private int pan;
  private int modulation;
  private int pitchBend;
  private int priority;
  private int breath;
  private float adjustedVolume;

  private final SoundFont soundFont;

  Channel(final FileData data, final float sssqVolume, final SoundFont soundFont) {
    this.soundFont = soundFont;

    this.index = data.readUByte(0x01);

    final int instrumentIndex = data.readByte(0x02);
    if(instrumentIndex != -1) {
      this.instrument = soundFont.getInstrument(instrumentIndex);
    }

    this.changeVolume(data.readUByte(0x03) / 128.0f, sssqVolume);
    this.pan = data.readUByte(0x04);

    this.modulation = data.readUByte(0x09);
    this.pitchBend = (data.readUByte(0x0a) - 0x40) * 2;
    // TODO this should probably be converted to an Enum
    this.priority = data.readUByte(0x0b);
    this.breath = data.readUByte(0x0c);

//    this.adjustedVolume = data.readUByte(0x0e) / 128f;
  }

  public int getIndex() {
    return this.index;
  }

  public Instrument getInstrument() {
    return this.instrument;
  }

  public void setInstrument(final int instrumentIndex) {
    this.instrument = this.soundFont.getInstrument(instrumentIndex);
  }

  public float getVolume() {
    return this.volume;
  }

  public void changeVolume(final float volume, final float sssqVolume) {
    this.volume = volume;
    this.adjustedVolume = volume * sssqVolume;
  }

  public int getPan() {
    return this.pan;
  }

  public void setPan(final int pan) {
    this.pan = pan;
  }

  public int getModulation() {
    return this.modulation;
  }

  public void setModulation(final int modulation) {
    this.modulation = modulation;
  }

  public int getPitchBend() {
    return this.pitchBend;
  }

  public void setPitchBend(final int pitchBend) {
    this.pitchBend = pitchBend;
  }

  public int getPriority() {
    return this.priority;
  }
  public void setPriority(final int priority) {
    this.priority = priority;
  }

  public int getBreath() {
    return this.breath;
  }

  public void setBreath(final int breath) {
    this.breath = breath;
  }

  public float getAdjustedVolume() {
    return this.adjustedVolume;
  }
}
