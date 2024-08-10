package legend.core.audio;

import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceQueueBuffers;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourceUnqueueBuffers;

public abstract class AudioSource {
  private final int[] buffers;
  private int bufferIndex;
  private int sourceId;

  private boolean playing;

  public AudioSource(final int bufferCount) {
    this.buffers = new int[bufferCount];
    this.init();
  }

  void init() {
    this.sourceId = alGenSources();

    alGenBuffers(this.buffers);
    this.bufferIndex = this.buffers.length - 1;
  }

  public void tick() {
    // Restart playback if stopped
    if(this.isPlaying()) {
      this.play();
    }
  }

  public boolean canBuffer() {
    if(!this.playing) {
      return false;
    }

    return this.bufferIndex >= 0;
  }

  public void handleProcessedBuffers() {
    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      this.buffers[++this.bufferIndex] = alSourceUnqueueBuffers(this.sourceId);
    }
  }

  protected void bufferOutput(final int format, final short[] buffer, final int sampleRate) {
    synchronized(this) {
      final int bufferId = this.buffers[this.bufferIndex--];
      alBufferData(bufferId, format, buffer, sampleRate);
      alSourceQueueBuffers(this.sourceId, bufferId);
    }
  }

  protected void play() {
    if(alGetSourcei(this.sourceId, AL_SOURCE_STATE) != AL_PLAYING) {
      alSourcePlay(this.sourceId);
    }
  }

  protected void stop() {
    this.playing = false;
    alSourceStop(this.sourceId);
  }

  protected void setPlaying(final boolean playing) {
    this.playing = playing;
  }

  public boolean isPlaying() {
    return this.playing;
  }

  public void destroy() {
    this.playing = false;
    alSourceStop(this.sourceId);

    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alDeleteBuffers(this.buffers);
    alDeleteSources(this.sourceId);
  }
}
