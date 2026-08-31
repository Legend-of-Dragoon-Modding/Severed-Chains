package legend.core.audio;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcef;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceQueueBuffers;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourceUnqueueBuffers;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;
import static org.lwjgl.system.MemoryUtil.memFree;

public abstract class AudioSource {
  private final int[] buffers;
  private int bufferIndex;
  private int sourceId;

  private boolean active;

  private IntBuffer tmp;

  private float playTime;

  public AudioSource(final int bufferCount) {
    this.buffers = new int[bufferCount];
  }

  protected boolean isInitialized() {
    return this.sourceId != 0;
  }

  protected void init() {
    this.sourceId = alGenSources();
    this.tmp = MemoryUtil.memAllocInt(1);

    alGenBuffers(this.buffers);
    this.bufferIndex = this.buffers.length - 1;

    this.playTime = 0.0f;
  }

  protected void destroy() {
    this.active = false;
    alSourceStop(this.sourceId);

    alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED, this.tmp);
    final int processedBufferCount = this.tmp.get(0);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alDeleteBuffers(this.buffers);
    alDeleteSources(this.sourceId);

    memFree(this.tmp);

    Arrays.fill(this.buffers, 0);
    this.sourceId = 0;
    this.tmp = null;

    this.playTime = 0.0f;
  }

  public void tick() {
    // Restart playback if stopped
    if(this.isActive()) {
      this.play();
    }
  }

  public boolean canBuffer() {
    if(!this.active || !this.isInitialized()) {
      return false;
    }

    return this.bufferIndex >= 0;
  }

  protected void handleProcessedBuffers() {
    if(this.isInitialized() && this.bufferIndex < this.buffers.length - 1) {
      alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED, this.tmp);
      final int processedBufferCount = this.tmp.get(0);

      for(int buffer = 0; buffer < processedBufferCount; buffer++) {
        final int unqueuedBufferId = alSourceUnqueueBuffers(this.sourceId);

/*
        // Calculate how much time was in that specific buffer and add it to the total
        final int sizeBytes = alGetBufferi(unqueuedBufferId, AL_SIZE);
        final int channels = alGetBufferi(unqueuedBufferId, AL_CHANNELS);
        final int freq = alGetBufferi(unqueuedBufferId, AL_FREQUENCY);

        // Bytes / bytes per sample * channels * samples per second
        final float bufferDuration = (float)sizeBytes / (2.0f * channels * freq);
        this.playTime += bufferDuration;
*/

        this.buffers[++this.bufferIndex] = unqueuedBufferId;
      }
    }
  }

  protected void bufferOutput(final int format, final ByteBuffer buffer, final int sampleRate) {
    synchronized(this) {
      if(this.isInitialized() && this.bufferIndex >= 0) {
        final int bufferId = this.buffers[this.bufferIndex--];
        alBufferData(bufferId, format, buffer, sampleRate);
        alSourceQueueBuffers(this.sourceId, bufferId);
      }
    }
  }

  protected void bufferOutput(final int format, final short[] buffer, final int sampleRate) {
    synchronized(this) {
      if(this.isInitialized() && this.bufferIndex >= 0) {
        final int bufferId = this.buffers[this.bufferIndex--];
        alBufferData(bufferId, format, buffer, sampleRate);
        alSourceQueueBuffers(this.sourceId, bufferId);
      }
    }
  }

  protected void bufferOutput(final int format, final float[] buffer, final int sampleRate) {
    synchronized(this) {
      if(this.isInitialized() && this.bufferIndex >= 0) {
        final int bufferId = this.buffers[this.bufferIndex--];
        alBufferData(bufferId, format, buffer, sampleRate);
        alSourceQueueBuffers(this.sourceId, bufferId);
      }
    }
  }

  protected void play() {
    alGetSourcei(this.sourceId, AL_SOURCE_STATE, this.tmp);
    if(this.tmp.get(0) != AL_PLAYING) {
      alSourcePlay(this.sourceId);
    }
  }

  protected void stop() {
    this.active = false;
    this.playTime = 0.0f;

    if(this.isInitialized()) {
      alSourceStop(this.sourceId);
    }
  }

  protected void setActive(final boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return this.active;
  }

  /** NOTE: this method will return the play time of the current buffer, so if you're using more than one buffer it's likely not going to return what you expect */
  public float getPosition() {
    synchronized(this) {
      if(!this.isInitialized()) {
        return 0.0f;
      }

      return alGetSourcef(this.sourceId, AL_SEC_OFFSET);
    }
  }
}
