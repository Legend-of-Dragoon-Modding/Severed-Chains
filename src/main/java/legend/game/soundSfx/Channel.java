package legend.game.soundSfx;

import legend.core.audio.MidiChannel;
import legend.game.unpacker.FileData;

import java.util.List;

final class Channel implements MidiChannel {
  private final int channelIndex_01;
  /** -1 means none */
  private int instrumentIndex_02;
  private int volume_03;
  /** 0x40 is middle, 0 is left, and 0x7f is right. */
  private int pan_04;
  // 05-08 is possibly ADSR lo and hi
  private int modulation_09;
  private int pitchBend_0a;
  final int _0b;
  int breath_0c;
  int volume_0e;

  private final SoundFont soundFont;

  Channel(final FileData channelData, final SoundFont soundFont) {
    this.channelIndex_01 = channelData.readByte(0x01);
    this.instrumentIndex_02 = channelData.readByte(0x02);
    this.volume_03 = channelData.readUByte(0x03);
    this.pan_04 = channelData.readUByte(0x04);
    this.modulation_09 = channelData.readUByte(0x09);
    //this.pitchBend_0a = channelData.readUByte(0x0a);
    this.pitchBend_0a = 64;
    this._0b = channelData.readUByte(0x0b);
    this.breath_0c = channelData.readUShort(0x0c);
    this.volume_0e = channelData.readUByte(0x0e);

    this.soundFont = soundFont;
  }

  @Override
  public double getVolume() {
    //TODO verify volumes
    return this.volume_0e / 127d;
  }

  void setVolume(final int value) {
    //TODO verify volumes
    this.volume_0e = value;
  }

  @Override
  public int getPan() {
    return this.pan_04;
  }

  void setPan(final int value) {
    this.pan_04 = value;
  }

  @Override
  public int getPitchBend() {
    return this.pitchBend_0a;
  }

  void setPitchBend(final int value) {
    this.pitchBend_0a = value;
  }

  void setProgram(final int value) {
    this.instrumentIndex_02 = value;
  }

  @Override
  public int getModulation() {
    return this.modulation_09;
  }

  void setModulation(final int value) {
    this.modulation_09 = value;
  }

  @Override
  public int getBreath() {
    return this.breath_0c;
  }

  void setBreath(final int value) {
    this.breath_0c = value;
  }

  Instrument getInstrument() {
    return this.soundFont.getInstrument(this.instrumentIndex_02);
  }

  List<InstrumentLayer> getLayers(final int note) {
    return this.soundFont.getInstrument(this.instrumentIndex_02).getLayers(note);
  }
}
