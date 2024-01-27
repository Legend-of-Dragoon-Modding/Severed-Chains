package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.sequencer.Sequencer;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
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

public final class AudioThread implements Runnable {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("AUDIO_THREAD");

  private final long audioContext;
  private final long audioDevice;
  private final int nanosPerTick;
  private final int frequency;
  private final boolean stereo;
  private final Sequencer sequencer;

  private boolean running;
  private boolean paused;
  private boolean disabled;

  public AudioThread(final int frequency, final boolean stereo, final int voiceCount, final int interpolationBitDepth) {
    if(1_000_000_000 % frequency != 0) {
      throw new IllegalArgumentException("Nanos (1_000_000_000) is not divisible by frequency " + frequency);
    }

    this.frequency = frequency;
    this.nanosPerTick = 1_000_000_000 / this.frequency;

    final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
    this.audioDevice = alcOpenDevice(defaultDeviceName);

    final int[] attributes = {0};
    this.audioContext = alcCreateContext(this.audioDevice, attributes);
    alcMakeContextCurrent(this.audioContext);

    final ALCCapabilities alcCapabilities = ALC.createCapabilities(this.audioDevice);
    final ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

    if(!alCapabilities.OpenAL10) {
      LOGGER.warn(SEQUENCER_MARKER, "Device does not support OpenAL10. Disabling audio.");
      this.disabled = true;
    }

    this.stereo = stereo;

    this.sequencer = new Sequencer(this.frequency, this.stereo, voiceCount, interpolationBitDepth);
  }

  @Override
  public void run() {
    if(this.disabled) {
      return;
    }

    this.running = true;
    this.paused = false;

    while(this.running) {
      while(this.paused) {
        try {
          this.wait();
        } catch(InterruptedException e) {

        }
      }
      final long time = System.nanoTime();

      this.sequencer.processBuffers();

      this.sequencer.processMusicQueue();

      final int sequencerBuffersToQueue = this.sequencer.buffersToQueue();

      final int passes = Math.max(sequencerBuffersToQueue, 0);

      for(int i = 0; i < passes; i++) {

        if( i < sequencerBuffersToQueue) {
          this.sequencer.tick();
        }

      }

      // Restarts playback if stopped
      this.sequencer.play();

      final long interval = System.nanoTime() - time;
      final int toSleep = (int)(Math.max(0, this.nanosPerTick - interval) / 1_000_000);
      DebugHelper.sleep(toSleep);
    }

    this.sequencer.destroy();
    alcDestroyContext(this.audioContext);
    alcCloseDevice(this.audioDevice);
  }

  public synchronized void stop() {
    this.paused = false;
    this.running = false;
    this.notify();
  }

  public synchronized void loadBackgroundMusic(final BackgroundMusic backgroundMusic) {
    this.sequencer.loadBackgroundMusic(backgroundMusic);
  }

  public synchronized int getSongId() {
    return this.sequencer.getSongId();
  }

  public synchronized void unloadMusic() {
    this.sequencer.unloadMusic();
  }

  public synchronized void setMainVolume(final int left, final int right) {
    this.sequencer.setMainVolume(left, right);
  }

  public synchronized void setReverbVolume(final int left, final int right) {
    this.sequencer.setReverbVolume(left, right);
  }

  public synchronized void fadeIn(final int time, final int volume) {
    this.sequencer.fadeIn(time, volume);
  }

  public synchronized void fadeOut(final int time) {
    this.sequencer.fadeOut(time);
  }
}
