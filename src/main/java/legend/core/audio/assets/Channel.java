package legend.core.audio.assets;

import legend.game.unpacker.FileData;

public final class Channel {
  private final int index;
  private Instrument instrument;
  private int volume_03;
  private int pan;
  private int modulation;
  private int pitchBend;
  private int _0b;
  private int breath;
  private int volume_0e;

  private final SoundFont soundFont;

  Channel(final FileData data, final SoundFont soundFont) {
    this.soundFont = soundFont;

    this.index = data.readUByte(0x01);

    final int instrumentIndex = data.readByte(0x02);
    if(instrumentIndex != -1) {
      this.instrument = soundFont.getInstrument(instrumentIndex);
    }

    this.volume_03 = data.readUByte(0x03);
    this.pan = data.readUByte(0x04);

    this.modulation = data.readUByte(0x09);
    //this.pitchBend = data.readUByte(0x0a);
    this.pitchBend = 64;
    this._0b = data.readUByte(0x0b);
    this.breath = data.readUByte(0x0c);

    this.volume_0e = data.readUByte(0x0e);
  }

  public Instrument getInstrument() {
    return this.instrument;
  }

  public void setIndex(final int instrumentIndex) {
    this.instrument = this.soundFont.getInstrument(instrumentIndex);
  }

  public int getVolume_03() {
    return this.volume_03;
  }

  public void setVolume_03(final int value) {
    this.volume_03 = value;
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

}
