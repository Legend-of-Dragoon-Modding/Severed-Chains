package legend.core.audio.assets;

import legend.game.unpacker.FileData;

public final class Channel {
  private final int index;
  private Instrument instrument;
  private int volume;
  private int pan;
  private int modulation;
  private int pitchBend;
  private int _0b;
  private int breath;
  private int adjustedVolume;

  private final SoundFont soundFont;

  Channel(final FileData data, final SoundFont soundFont) {
    this.soundFont = soundFont;

    this.index = data.readUByte(0x01);

    final int instrumentIndex = data.readByte(0x02);
    if(instrumentIndex != -1) {
      this.instrument = soundFont.getInstrument(instrumentIndex);
    }

    this.volume = data.readUByte(0x03);
    this.pan = data.readUByte(0x04);

    this.modulation = data.readUByte(0x09);
    this.pitchBend = data.readUByte(0x0a);
    this._0b = data.readUByte(0x0b);
    this.breath = data.readUByte(0x0c);

    this.adjustedVolume = data.readUByte(0x0e);
  }

  public Instrument getInstrument() {
    return this.instrument;
  }

  public void setInstrument(final int instrumentIndex) {
    this.instrument = this.soundFont.getInstrument(instrumentIndex);
  }

  public int getVolume() {
    return this.volume;
  }

  public void changeVolume(final int volume, final int sssqVolume) {
    this.volume = volume;
    this.adjustedVolume = (volume * sssqVolume) / 0x80;
  }

  public void setVolume(final int value) {
    this.volume = value;
  }

  public int getPan() {
    return this.pan;
  }

  public void setPan(final int value) {
    this.pan = value;
  }

  public int getModulation() {
    return this.modulation;
  }

  public void setModulation(final int value) {
    this.modulation = value;
  }

  public int getPitchBend() {
    return this.pitchBend;
  }

  public void setPitchBend(final int value) {
    this.pitchBend = value;
  }

  public int get_0b() {
    return this._0b;
  }
  public void set_0b(final int value) {
    this._0b = value;
  }

  public int getBreath() {
    return this.breath;
  }

  public void setBreath(final int value) {
    this.breath = value;
  }



  public int getAdjustedVolume() {
    return this.adjustedVolume;
  }

  public void setAdjustedVolume(final int value) {
    this.adjustedVolume = value;
  }

}
