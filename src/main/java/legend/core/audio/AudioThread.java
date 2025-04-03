package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.opus.XaPlayer;
import legend.core.audio.sequencer.Sequencer;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.game.modding.coremod.CoreMod;
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
import org.lwjgl.openal.ALUtil;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.openal.ALC10.ALC_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetError;
import static org.lwjgl.openal.ALC10.alcGetIntegerv;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.openal.ALC11.ALC_ALL_DEVICES_SPECIFIER;
import static org.lwjgl.openal.EXTDisconnect.ALC_CONNECTED;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class AudioThread implements Runnable {
  private static final Logger LOGGER = LogManager.getFormatterLogger(AudioThread.class);
  private static final Marker AUDIO_THREAD_MARKER = MarkerManager.getMarker("AUDIO_THREAD");
  public static final int BASE_SAMPLE_RATE = 44_100;

  private final int nanosPerTick;
  private long audioContext;
  private long audioDevice;
  private final boolean stereo;
  private final int voiceCount;
  private InterpolationPrecision interpolationPrecision;
  private PitchResolution pitchResolution;
  private SampleRate sampleRate;
  private EffectsOverTimeGranularity effectsGranularity;
  private Sequencer sequencer;
  private XaPlayer xaPlayer;
  private final List<AudioSource> sources = new ArrayList<>();

  private boolean running;
  private boolean paused;

  private ALCapabilities alCapabilities;
  private ALCCapabilities alcCapabilities;

  private IntBuffer tmp;

  public static List<String> getDevices() {
    if(ALC.getCapabilities().ALC_ENUMERATE_ALL_EXT) {
      return ALUtil.getStringList(0, ALC_ALL_DEVICES_SPECIFIER);
    }

    return ALUtil.getStringList(0, ALC_DEVICE_SPECIFIER);
  }

  public AudioThread(final boolean stereo, final int voiceCount, final InterpolationPrecision bitDepth, final PitchResolution pitchResolution, final SampleRate sampleRate, final EffectsOverTimeGranularity granularity) {
    this.nanosPerTick = 1_000_000_000 / 60;
    this.stereo = stereo;
    this.voiceCount = voiceCount;
    this.interpolationPrecision = bitDepth;
    this.pitchResolution = pitchResolution;
    this.sampleRate = sampleRate;
    this.effectsGranularity = granularity;
  }

  public void init() {
    this.initInternal();
    this.addDefaultSources();
  }

  public void reinit() {
    LOGGER.info("Reinitializing audio");

    synchronized(this) {
      final boolean[] playing = new boolean[this.sources.size()];
      for(int i = 0; i < this.sources.size(); i++) {
        playing[i] = this.sources.get(i).isPlaying();
      }

      this.destroyInternal();
      this.initInternal();

      if(this.audioDevice != 0) {
        for(int i = 0; i < this.sources.size(); i++) {
          final AudioSource source = this.sources.get(i);
          source.init();

          if(playing[i]) {
            source.setPlaying(true);
          }
        }
      }
    }
  }

  private void initInternal() {
    this.openDevice();

    if(this.audioDevice != 0) {
      this.tmp = MemoryUtil.memAllocInt(1);

      final int[] attributes = {0};
      this.audioContext = alcCreateContext(this.audioDevice, attributes);

      if(this.audioContext == 0) {
        LOGGER.error("Failed to create audio context: %#x", alcGetError(this.audioDevice));
        this.destroyInternal();
        return;
      }

      alcMakeContextCurrent(this.audioContext);
      LOGGER.info(AUDIO_THREAD_MARKER, "Created audio context %#x", this.audioContext);

      this.alcCapabilities = ALC.createCapabilities(this.audioDevice);
      this.alCapabilities = AL.createCapabilities(this.alcCapabilities);

      if(this.alCapabilities.OpenAL10) {
        synchronized(this) {
          this.paused = false;
          this.notify();
        }

        return;
      }
    } else {
      this.audioContext = 0;
    }

    LOGGER.warn("Device does not support OpenAL10. Disabling audio.");
    this.paused = true;
  }

  public void destroy() {
    synchronized(this) {
      if(!this.running && this.audioDevice != 0) {
        this.destroyInternal();
        return;
      }

      this.running = false;
    }

    while(this.audioDevice != 0) {
      DebugHelper.sleep(1);
    }
  }

  private void destroyInternal() {
    for(final AudioSource source : this.sources) {
      if(source.isInitialized()) {
        source.destroy();
      }
    }

    if(this.audioContext != 0) {
      alcDestroyContext(this.audioContext);
      this.audioContext = 0;
    }

    if(this.audioDevice != 0) {
      alcCloseDevice(this.audioDevice);
      this.audioDevice = 0;

      memFree(this.tmp);
    }
  }

  private void openDevice() {
    final String currentDevice = CONFIG.getConfig(CoreMod.AUDIO_DEVICE_CONFIG.get());
    final List<String> devices = getDevices();

    if(devices.contains(currentDevice)) {
      LOGGER.info(AUDIO_THREAD_MARKER, "Using selected audio device %s", currentDevice);
      this.audioDevice = alcOpenDevice(currentDevice);
    } else if(!devices.isEmpty()) {
      LOGGER.info(AUDIO_THREAD_MARKER, "Using first audio device %s", devices.getFirst());
      this.audioDevice = alcOpenDevice(devices.getFirst());
    } else {
      LOGGER.info(AUDIO_THREAD_MARKER, "No audio devices found");
      this.audioDevice = 0;
    }
  }

  private void addDefaultSources() {
    this.sequencer = this.addSource(new Sequencer(this.stereo, this.voiceCount, this.interpolationPrecision, this.pitchResolution, this.sampleRate, this.effectsGranularity));
    this.xaPlayer = this.addSource(new XaPlayer());
  }

  public <T extends AudioSource> T addSource(final T source) {
    synchronized(this) {
      this.sources.add(source);

      if(this.audioDevice != 0) {
        source.init();
      }

      return source;
    }
  }

  public void removeSource(final AudioSource source) {
    synchronized(this) {
      if(source.isInitialized()) {
        source.destroy();
      }

      this.sources.remove(source);
    }
  }

  @Override
  public void run() {
    this.running = true;

    while(this.running) {
      final long time = System.nanoTime();

      boolean canBuffer = false;

      synchronized(this) {
        while(this.paused) {
          try {
            this.wait();
          } catch(final InterruptedException ignored) { }
        }

        if(!this.running) {
          break;
        }

        if(this.alcCapabilities.ALC_EXT_disconnect) {
          alcGetIntegerv(this.audioDevice, ALC_CONNECTED, this.tmp);
          final int connected = this.tmp.get(0);

          if(connected == 0) {
            LOGGER.warn("Audio device lost");
            this.reinit();
          }
        }

        for(int i = 0; i < this.sources.size(); i++) {
          final AudioSource source = this.sources.get(i);

          synchronized(source) {
            final boolean sourceCanBuffer = source.canBuffer();
            canBuffer = canBuffer || sourceCanBuffer;

            if(sourceCanBuffer) {
              source.tick();
            }

            source.handleProcessedBuffers();
          }
        }
      }

      if(!this.sequencer.canBuffer()) {
        final long interval = System.nanoTime() - time;
        final int toSleep = (int)(Math.max(0, this.nanosPerTick - interval) / 1_000_000);
        DebugHelper.sleep(toSleep);
      }
    }

    synchronized(this) {
      this.destroyInternal();
    }
  }

  public void stop() {
    this.paused = false;
    this.running = false;

    synchronized(this) {
      this.notify();
    }
  }

  public void loadBackgroundMusic(final BackgroundMusic backgroundMusic) {
    synchronized(this) {
      if(this.sequencer.isInitialized()) {
        this.sequencer.loadBackgroundMusic(backgroundMusic);
      }
    }
  }

  public int getSongId() {
    synchronized(this) {
      return this.sequencer.getSongId();
    }
  }

  public void unloadMusic() {
    synchronized(this) {
      this.sequencer.unloadMusic();
    }
  }

  public void setMusicPlayerVolume(final float volume) {
    synchronized(this) {
      this.sequencer.setPlayerVolume(volume);
    }
  }

  public void setXaPlayerVolume(final float volume) {
    synchronized(this) {
      this.xaPlayer.setPlayerVolume(volume);
    }
  }

  public void setMainVolume(final int left, final int right) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Setting main volume to %.2f, %.2f", left / 256.0f, right / 256.0f);

    synchronized(this) {
      this.sequencer.setMainVolume(left, right);
    }
  }

  public int getSequenceVolume() {
    synchronized(this) {
      return this.sequencer.getSequenceVolume();
    }
  }

  public int setSequenceVolume(final int volume) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Setting sequence volume to %.2f", volume / 128.0f);

    synchronized(this) {
      return this.sequencer.setSequenceVolume(volume);
    }
  }

  public int changeSequenceVolumeOverTime(final int volume, final int time) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Setting sequence volume to %.2f over %.2fs", volume / 128.0f, time / 60.0f);

    synchronized(this) {
      return this.sequencer.changeSequenceVolumeOverTime(volume, time);
    }
  }

  public void setReverbVolume(final int left, final int right) {
    synchronized(this) {
      this.sequencer.setReverbVolume(left, right);
    }
  }

  public void fadeIn(final int time, final int volume) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Fading in to %.2f for %.2fs", volume / 256.0f, time / 60.0f);

    synchronized(this) {
      if(this.sequencer.isInitialized()) {
        this.sequencer.fadeIn(time, volume);
      }
    }
  }

  public void fadeOut(final int time) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Fading out for %.2fs", time / 60.0f);

    synchronized(this) {
      if(this.sequencer.isInitialized()) {
        this.sequencer.fadeOut(time);
      }
    }
  }

  public void startSequence() {
    LOGGER.info(AUDIO_THREAD_MARKER, "Starting sequence");

    synchronized(this) {
      if(this.sequencer.isInitialized()) {
        this.sequencer.startSequence();
      }
    }
  }

  public void stopSequence() {
    LOGGER.info(AUDIO_THREAD_MARKER, "Stopping sequence");

    synchronized(this) {
      if(this.sequencer.isInitialized()) {
        this.sequencer.stopSequence();
      }
    }
  }

  public void loadXa(final FileData fileData) {
    synchronized(this) {
      if(this.xaPlayer.isInitialized()) {
        this.xaPlayer.loadXa(fileData);
      }
    }
  }

  public boolean isMusicPlaying() {
    synchronized(this) {
      return this.sequencer.isPlaying();
    }
  }

  public void setReverb(final ReverbConfig config) {
    synchronized(this) {
      this.sequencer.setReverbConfig(config);
    }
  }

  public int getSequenceVolumeOverTimeFlags() {
    synchronized(this) {
      return this.sequencer.getVolumeOverTimeFlags();
    }
  }

  public void changeInterpolationBitDepth(final InterpolationPrecision bitDepth) {
    synchronized(this) {
      if(this.interpolationPrecision != bitDepth) {
        this.interpolationPrecision = bitDepth;
        this.sequencer.changeInterpolationBitDepth(this.interpolationPrecision);
      }
    }
  }

  public void changePitchResolution(final PitchResolution pitchResolution) {
    synchronized(this) {
      if(this.pitchResolution != pitchResolution) {
        this.pitchResolution = pitchResolution;
        this.sequencer.changePitchResolution(this.pitchResolution);
      }
    }
  }

  public void changeSampleRate(final SampleRate sampleRate) {
    synchronized(this) {
      if(this.sampleRate != sampleRate) {
        this.sampleRate = sampleRate;
        this.sequencer.changeSampleRate(sampleRate, this.effectsGranularity);
      }
    }
  }

  public SampleRate getSampleRate() {
    synchronized(this) {
      return this.sampleRate;
    }
  }

  public void changeEffectsOverTimeGranularity(final EffectsOverTimeGranularity effectsGranularity) {
    synchronized(this) {
      if(this.effectsGranularity != effectsGranularity) {
        this.effectsGranularity = effectsGranularity;
        this.sequencer.changeEffectsOverTimeGranularity(effectsGranularity);
      }
    }
  }
}
