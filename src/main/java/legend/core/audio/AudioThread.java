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
  private boolean disabled;

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

      for(int i = 0; i < this.sources.size(); i++) {
        final AudioSource source = this.sources.get(i);
        source.init();

        if(playing[i]) {
          source.setPlaying(true);
        }
      }
    }
  }

  private void initInternal() {
    this.openDevice();
    this.tmp = MemoryUtil.memAllocInt(1);

    if(this.audioDevice != 0) {
      final int[] attributes = {0};
      this.audioContext = alcCreateContext(this.audioDevice, attributes);
      alcMakeContextCurrent(this.audioContext);

      this.alcCapabilities = ALC.createCapabilities(this.audioDevice);
      this.alCapabilities = AL.createCapabilities(this.alcCapabilities);

      if(this.alCapabilities.OpenAL10) {
        return;
      }
    } else {
      this.audioContext = 0;
    }

    LOGGER.warn("Device does not support OpenAL10. Disabling audio.");
    this.disabled = true;
    this.sequencer = null;
    this.xaPlayer = null;
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
      source.destroy();
    }

    alcDestroyContext(this.audioContext);
    alcCloseDevice(this.audioDevice);

    memFree(this.tmp);

    this.audioContext = 0;
    this.audioDevice = 0;
  }

  private void openDevice() {
    final String currentDevice = CONFIG.getConfig(CoreMod.AUDIO_DEVICE_CONFIG.get());
    final List<String> devices = getDevices();

    if(devices.contains(currentDevice)) {
      this.audioDevice = alcOpenDevice(currentDevice);
    } else if(!devices.isEmpty()) {
      this.audioDevice = alcOpenDevice(devices.getFirst());
    } else {
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
      return source;
    }
  }

  public void removeSource(final AudioSource source) {
    synchronized(this) {
      source.destroy();
      this.sources.remove(source);
    }
  }

  public Sequencer getSequencer() {
    return this.sequencer;
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
          synchronized(this) {
            this.wait();
          }
        } catch(final InterruptedException ignored) { }
      }

      final long time = System.nanoTime();

      boolean canBuffer = false;

      synchronized(this) {
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

      if(!canBuffer) {
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
    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.loadBackgroundMusic(backgroundMusic);
      }
    }
  }

  public int getSongId() {
    if(this.sequencer != null) {
      synchronized(this) {
        return this.sequencer.getSongId();
      }
    }
    return 0;
  }

  public void unloadMusic() {
    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.unloadMusic();
      }
    }
  }

  public void setMainVolume(final int left, final int right) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Setting main volume to %.2f, %.2f", left / 256.0f, right / 256.0f);

    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.setMainVolume(left, right);
      }
    }
  }

  public int getSequenceVolume() {
    if(this.sequencer != null) {
      synchronized(this) {
        return this.sequencer.getSequenceVolume();
      }
    }
    return 0;
  }

  public int setSequenceVolume(final int volume) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Setting sequence volume to %.2f", volume / 128.0f);

    if(this.sequencer != null) {
      synchronized(this) {
        return this.sequencer.setSequenceVolume(volume);
      }
    }
    return 0;
  }

  public int changeSequenceVolumeOverTime(final int volume, final int time) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Setting sequence volume to %.2f over %.2fs", volume / 128.0f, time / 60.0f);

    if(this.sequencer != null) {
      synchronized(this) {
        return this.sequencer.changeSequenceVolumeOverTime(volume, time);
      }
    }
    return 0;
  }

  public void setReverbVolume(final int left, final int right) {
    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.setReverbVolume(left, right);
      }
    }
  }

  public void fadeIn(final int time, final int volume) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Fading in to %.2f for %.2fs", volume / 256.0f, time / 60.0f);

    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.fadeIn(time, volume);
      }
    }
  }

  public void fadeOut(final int time) {
    LOGGER.info(AUDIO_THREAD_MARKER, "Fading out for %.2fs", time / 60.0f);

    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.fadeOut(time);
      }
    }
  }

  public void startSequence() {
    LOGGER.info(AUDIO_THREAD_MARKER, "Starting sequence");

    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.startSequence();
      }
    }
  }

  public void stopSequence() {
    LOGGER.info(AUDIO_THREAD_MARKER, "Stopping sequence");

    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.stopSequence();
      }
    }
  }

  public void loadXa(final FileData fileData) {
    if(this.xaPlayer != null) {
      synchronized(this) {
        this.xaPlayer.loadXa(fileData);
      }
    }
  }

  public boolean isMusicPlaying() {
    if(this.sequencer != null) {
      synchronized(this) {
        return this.sequencer.isPlaying();
      }
    }
    return false;
  }

  public void setReverb(final ReverbConfig config) {
    if(this.sequencer != null) {
      synchronized(this) {
        this.sequencer.setReverbConfig(config);
      }
    }
  }

  public int getSequenceVolumeOverTimeFlags() {
    if(this.sequencer != null) {
      synchronized(this) {
        return this.sequencer.getVolumeOverTimeFlags();
      }
    }
    return 0;
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

  public void changeEffectsOverTimeGranularity(final EffectsOverTimeGranularity effectsGranularity) {
    synchronized(this) {
      if(this.effectsGranularity != effectsGranularity) {
        this.effectsGranularity = effectsGranularity;

        this.sequencer.changeEffectsOverTimeGranularity(effectsGranularity);
      }
    }
  }
}
