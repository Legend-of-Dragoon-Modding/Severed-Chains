package legend.core.audio;

import legend.core.DebugHelper;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

public class AudioThread implements Runnable {
  private final long audioContext;
  private final long audioDevice;
  private final int nanosPerTick;
  private final int samplesPerTick;
  private final boolean stereo;
  private final Voice[] voicePool;

  private MidiSequence sequence;

  private long time;
  private boolean running;
  private boolean paused;

  /**
   *
   * @param frequency Amount of updates per second. Has to be a divisor of 1_000_000_000 and 44_100 (1, 2, 4, 5, 10, 20, 25, 50, 100)
   * @param voiceCount Amount of voices that can play at ones. Retail uses 24
   */
  public AudioThread(final int frequency, final boolean stereo, final int voiceCount) {
    if(1_000_000_000 % frequency != 0) {
      throw new IllegalArgumentException("Nanos (1_000_000_000) is not divisible by frequency " + frequency);
    }

    if(44_100 % frequency != 0) {
      throw new IllegalArgumentException("Sample Rate (44_100) is not divisable by frequency " + frequency);
    }

    this.nanosPerTick = 1_000_000_000 / frequency;
    this.samplesPerTick = 44_100 / frequency;

    final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
    this.audioDevice = alcOpenDevice(defaultDeviceName);

    final int[] attributes = {0};
    this.audioContext = alcCreateContext(this.audioDevice, attributes);
    alcMakeContextCurrent(this.audioContext);

    final ALCCapabilities alcCapabilities = ALC.createCapabilities(this.audioDevice);
    final ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

    //TODO disable audio instead of crashing
    if(!alCapabilities.OpenAL10) {
      throw new UnsupportedOperationException("Device does not support OpenAL10");
    }

    this.stereo = stereo;

    //TODO these tables should probably be pre-calculated
    Offsets.genOffsets();

    this.voicePool = new Voice[voiceCount];
    for(int voice = 0; voice < this.voicePool.length; voice++) {
      this.voicePool[voice] = new Voice(this.samplesPerTick, stereo);
    }
  }

  @Override
  public void run() {
    this.running = true;
    this.paused = false;

    this.time = System.nanoTime();

    while(this.running) {
      while(this.paused) {
        try {
          this.wait();
        } catch(final InterruptedException e) {

        }
      }

      for(final Voice voice : this.voicePool) {
        voice.processBuffers();
      }

      this.tick();

      final long interval = System.nanoTime() - this.time;
      final int toSleep = (int)Math.max(0, this.nanosPerTick - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      this.time += this.nanosPerTick;

      for(final Voice voice : this.voicePool) {
        voice.play();
      }
    }

    for(final Voice voice : this.voicePool) {
      voice.destroy();
    }
    alcDestroyContext(this.audioContext);
    alcCloseDevice(this.audioDevice);
  }

  public void stop() {
    this.running = false;
    this.notify();
  }

  private void tick() {
    for(int sample = 0; sample < this.samplesPerTick; sample++) {
      //TODO set empty

      if(this.sequence != null) {
        this.sequence.tick();
      }

      //TODO Tick SFX??

      for(final Voice voice : this.voicePool) {
        voice.tick(this.stereo);
      }
    }
  }

  public void loadBackgroundMusic(final MidiSequence sequence) {
    sequence.loadVoices(this.voicePool);
    this.sequence = sequence;
  }
}
