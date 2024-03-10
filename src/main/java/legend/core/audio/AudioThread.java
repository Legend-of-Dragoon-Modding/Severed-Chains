package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.opus.XaPlayer;
import legend.core.audio.sequencer.Sequencer;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.game.sound.ReverbConfig;
import legend.game.unpacker.FileData;
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
  public static final int BASE_SAMPLE_RATE = 44_100;
  public static final int ACTUAL_SAMPLE_RATE = 48_000;
  public static final double SAMPLE_RATE_RATIO = BASE_SAMPLE_RATE / (double) ACTUAL_SAMPLE_RATE;
  public static final double SAMPLE_RATE_MULTIPLIER = ACTUAL_SAMPLE_RATE / (double) BASE_SAMPLE_RATE;

  private final long audioContext;
  private final long audioDevice;
  private final int nanosPerTick;
  private final int frequency;
  private final boolean stereo;
  private final Sequencer sequencer;
  private final XaPlayer xaPlayer;

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

    this.xaPlayer = new XaPlayer(this.frequency);
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
      this.xaPlayer.processBuffers();

      final boolean canBgmBuffer = this.sequencer.canBuffer();
      final boolean canXaBuffer = this.xaPlayer.canBuffer();

      if(canBgmBuffer) {
        synchronized(this) {
          this.sequencer.tick();
        }
      }

      if(canXaBuffer) {
        this.xaPlayer.tick();
      }

      if(!canBgmBuffer && !canXaBuffer) {
        final long interval = System.nanoTime() - time;
        final int toSleep = (int)(Math.max(0, this.nanosPerTick - interval) / 1_000_000);
        DebugHelper.sleep(toSleep);
      }
    }

    this.sequencer.destroy();
    this.xaPlayer.destroy();
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
    LOGGER.info("Setting main volume to %.2f, %.2f", left / 256f, right / 256f);
    this.sequencer.setMainVolume(left, right);
  }

  public synchronized int getSequenceVolume() {
    return this.sequencer.getSequenceVolume();
  }

  public synchronized int setSequenceVolume(final int volume) {
    LOGGER.info("Setting sequence volume to %.2f", volume / 128f);
    return this.sequencer.setSequenceVolume(volume);
  }

  public synchronized int changeSequenceVolumeOverTime(final int volume, final int time) {
    LOGGER.info("Setting sequence volume to %.2f over %.2fs", volume / 128f, time / 60f);
    return this.sequencer.changeSequenceVolumeOverTime(volume, time);
  }

  public synchronized void setReverbVolume(final int left, final int right) {
    this.sequencer.setReverbVolume(left, right);
  }

  public synchronized void fadeIn(final int time, final int volume) {
    LOGGER.info("Fading in to %.2f for %.2fs", volume / 256f, time / 60f);
    this.sequencer.fadeIn(time, volume);
  }

  public synchronized void fadeOut(final int time) {
    LOGGER.info("Fading out for %.2fs", time / 60f);
    this.sequencer.fadeOut(time);
  }

  public synchronized void startSequence() {
    LOGGER.info("Starting sequence");
    this.sequencer.startSequence();
  }

  public synchronized void stopSequence() {
    LOGGER.info("Stopping sequence");
    this.sequencer.stopSequence();
  }

  public synchronized void loadXa(final FileData fileData) {
    this.xaPlayer.loadXa(fileData);
  }

  public synchronized boolean isMusicPlaying() {
    return this.sequencer.isPlaying();
  }
  public synchronized void setReverb(final ReverbConfig config) {
    this.sequencer.setReverbConfig(config);
  }

  public synchronized int getSequenceVolumeOverTimeFlags() {
    return this.sequencer.getVolumeOverTimeFlags();
  }
}
