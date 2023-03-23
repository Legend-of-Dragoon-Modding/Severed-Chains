package legend.game.sound2;

import legend.core.openal.BufferedSound;
import legend.game.sound.Sssq;

final class Channel {
  private final int index;
  private final BufferedSound sound;
  private final int presetIndex;
  private final Sshd sshd;
  private State state = State.KEY_OFF;

  Channel(final int index, final Sssq.ChannelInfo channelInfo, final Sshd sshd) {
    this.index = index;
    this.sshd = sshd;
    this.presetIndex = channelInfo.instrumentIndex_02;
    this.sound = new BufferedSound(false);
  }

  void tick(final float ms) {
    final int sampleRate = 44100 / 4; //TODO actual sample rate Don't do this
    this.sound.process();

    final short[] pcm = this.sshd.getPreset(this.presetIndex).getLayer(0).getSample().get((int)(ms * (sampleRate / 1000.0f)));

    this.sound.bufferSamples(pcm, sampleRate);
    this.sound.play();
  }

  void play() {
    this.sound.play();
  }

  void stop() {
    this.sound.stop();
  }

  void handleKeyOff(final MidiState state) {
    this.state = State.KEY_OFF;

    System.out.println("Channel " + this + " key off");
    state.offset += 2;
  }

  void handleKeyOn(final MidiState state) {
    this.state = State.KEY_ON;

    System.out.println("Channel " + this + " key on");
    state.offset += 2;
  }

  void handlePolyphonicKeyPressure(final MidiState state) {
    System.out.println("Channel " + this + " polyphonic key pressure");
    state.offset += 2;
  }

  void handleControlChange(final MidiState state) {
    System.out.println("Channel " + this + " control change");
    state.offset += 2;
  }

  void handleProgramChange(final MidiState state) {
    System.out.println("Channel " + this + " program change");
    state.offset++;
  }

  void handlePitchBend(final MidiState state) {
    System.out.println("Channel " + this + " pitch bend");
    state.offset++;
  }

  @Override
  public String toString() {
    return "Channel[%d]".formatted(this.index);
  }

  void destroy() {
    this.sound.stop();
    this.sound.destroy();
  }

  enum State {
    KEY_ON,
    KEY_OFF,
  }
}
