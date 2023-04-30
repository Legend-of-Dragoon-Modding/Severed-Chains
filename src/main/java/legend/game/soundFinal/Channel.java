package legend.game.soundFinal;

import legend.game.sound.Sssq;

import java.util.List;

final class Channel {
  final private int channelIndex;
  private final Sshd sshd;
  private int presetIndex;

  //TODO load this from channelInfo
  private int pitchBend;
  private double modulationBend;
  private double volume = 1;
  private int pan = 64;

  Channel(final int channelIndex, final Sssq.ChannelInfo channelInfo, final Sshd sshd) {
    this.channelIndex = channelIndex;
    this.sshd = sshd;
    this.presetIndex = channelInfo.instrumentIndex_02;
  }


  int getChannelIndex() {
    return this.channelIndex;
  }

  void setPresetIndex(final int presetIndex) {
    this.presetIndex = presetIndex;
  }

  int getPitchBend() {
    return this.pitchBend;
  }

  void setPitchBend(final int value) {
    this.pitchBend = value;
  }

  double getModulation() {
    return this.modulationBend;
  }

  void setModulation(final int value) {
    this.modulationBend = (value - 64) / 64d;
  }

  double getVolume() {
    return this.volume;
  }

  void setVolume(final int value) {
    this.volume = value / 127d;
  }

  int getPan() {
    return this.pan;
  }

  void setPan(final int value) {
    this.pan = value;
  }

  List<Layer> getLayers(final int note) {
    return this.sshd.getPreset(this.presetIndex).getLayers(note);
  }
}
