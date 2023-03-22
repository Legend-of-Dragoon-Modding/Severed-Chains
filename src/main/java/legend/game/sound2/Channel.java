package legend.game.sound2;

import legend.core.DebugHelper;
import legend.core.openal.BufferedSound;
import legend.core.openal.Sound;

final class Channel {
  private final BufferedSound sound;
  private final int presetIndex;
  private final Sshd sshd;
  private State state = State.KeyOff;

  Channel(final byte[] data, final Sshd sshd) {
    this.sshd = sshd;
    this.presetIndex = data[2];
    this.sound = new BufferedSound(false);
  }

  /**
  Buffers n ticks of audio. Each tick is 1/60 of a second.
   */
  void tick(final int ticks) {
    final int sampleRate = 44100 / 4; //TODO actual sample rate Don't do this
    this.sound.process();

    final short[] pcm = this.sshd.getPreset(this.presetIndex).getLayer(0).getSample().get(ticks * (sampleRate / 60));

    this.sound.bufferSamples(pcm, sampleRate);
    this.sound.play();
    DebugHelper.sleep(1000); // For testing purposes, so everything doesn't play at once
  }

  void play() {
    this.sound.play();
  }

  void stop() {
    this.sound.stop();
  }

  void KeyOn() {
    this.state = State.KeyOn;
  }

  void KeyOff() {
    this.state = State.KeyOff;
  }

  void destroy() {
    this.sound.stop();
    this.sound.destroy();
  }

  enum State {
    KeyOn,
    KeyOff
  }
}
