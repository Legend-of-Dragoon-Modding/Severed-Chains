package legend.game.sound2;

import legend.core.openal.BufferedSound;
import legend.game.sound.Sssq;

final class Channel {
  private final int index;
  private final BufferedSound sound;
  private final int presetIndex;
  private final Sshd sshd;
  private State state = State.END;
  private int note;
  private int pitchBend = 64;

  Channel(final int index, final Sssq.ChannelInfo channelInfo, final Sshd sshd) {
    this.index = index;
    this.sshd = sshd;
    this.presetIndex = channelInfo.instrumentIndex_02;
    this.sound = new BufferedSound(false);
  }

  void tick(final float ms) {
    this.sound.process();

    if(this.state == State.END) {
      final short[] pcm = new short[(int) (ms * (44100 / 1000.0f))];
      this.sound.bufferSamples(pcm, 44100);
      this.sound.play();
    } else {
      final Layer layer = this.sshd.getPreset(this.presetIndex).getLayer(this.note);
      final int sampleRate = layer.calculateSampleRate(this.note, this.pitchBend);


      final short[] pcm = layer.getSample().get((int)(ms * (sampleRate / 1000.0f)));
      final boolean isEnd = layer.getAdsr().applyAdsr(pcm);

      if(isEnd) {
        this.state = State.END;
      }

      this.sound.bufferSamples(Resampler.resample(pcm, pcm.length, false, sampleRate, 44100), 44100);
      this.sound.play();
    }
  }

  void play() {
    this.sound.play();
  }

  void stop() {
    this.sound.stop();
  }

  void handleKeyOff(final MidiState state) {
    this.state = State.KEY_OFF;
    this.sshd.getPreset(this.presetIndex).setOff();

    System.out.println("Channel " + this + " key off");
    state.offset += 2;
  }

  void handleKeyOn(final MidiState state, final int note) {
    this.state = State.KEY_ON;
    this.note = note;
    this.sshd.getPreset(this.presetIndex).resetBuffs();
    this.sshd.getPreset(this.presetIndex).resetAdsr();

    System.out.println("Channel " + this + " key on. Note: " + note);
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

  void handlePitchBend(final MidiState state, final int value) {
    System.out.println("Channel " + this + " pitch bend");
    this.pitchBend = value;
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
    END
  }
}
