package legend.game.soundFinal;

import legend.game.sound.Sssq;

import java.util.List;

final class Channel {
  final private int channelIndex;
  private final Sshd sshd;
  private int presetIndex;

  //TODO load this from channelInfo
  private double pitchBend = 0;
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

  double getPitchBend() {
    return this.pitchBend;
  }

  void setPitchBend(final int value) {
    this.pitchBend = (64 - value) / 64d;
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
