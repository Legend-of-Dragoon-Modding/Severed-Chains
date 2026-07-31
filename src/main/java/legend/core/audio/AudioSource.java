package legend.core.audio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_GAIN;
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
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;

public abstract class AudioSource {
  private final AudioTag tag;
  private final int[] buffers = new int[6];
  private int bufferIndex;
  private int sourceId;
  private final int format;
  private final int sampleRate;
  protected boolean eof;

  private boolean active;

  private float playTime;

  public AudioSource(final int format, final int sampleRate, final float volume, final AudioTag tag) {
    this.tag = tag;
    this.format = format;
    this.sampleRate = sampleRate;

    this.sourceId = alGenSources();
    alGenBuffers(this.buffers);
    this.bufferIndex = this.buffers.length - 1;

    this.setVolume(volume);
  }

  public AudioTag getTag() {
    return this.tag;
  }

  protected boolean isInitialized() {
    return this.sourceId != 0;
  }

  protected void init() {
    this.sourceId = alGenSources();

    alGenBuffers(this.buffers);
    this.bufferIndex = this.buffers.length - 1;

    this.playTime = 0.0f;
  }

  protected boolean isFinished() {
    return (this.eof && this.bufferIndex == this.buffers.length - 1);
  }

  protected void destroy() {
    this.active = false;
    alSourceStop(this.sourceId);

    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alDeleteBuffers(this.buffers);
    alDeleteSources(this.sourceId);

    Arrays.fill(this.buffers, 0);
    this.sourceId = 0;

    this.playTime = 0.0f;
  }

  public void tick() {
    this.handleProcessedBuffers();

    // Wait until we have only two buffers left
    if(!this.eof && this.bufferIndex >= this.buffers.length - 3) {
      // Fill up buffers entirely
      while(this.bufferIndex >= 0) {
        if(this.eof) {
          break;
        }

        this.fillBuffer();
      }
    }

    // Restart playback if stopped
    if(this.isActive()) {
      this.play();
    }
  }

  protected abstract void fillBuffer();

  public boolean canBuffer() {
    if(!this.active || !this.isInitialized()) {
      return false;
    }

    return this.bufferIndex >= 2;
  }

  protected void handleProcessedBuffers() {
    if(this.bufferIndex < this.buffers.length - 1) {
      final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

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

  protected void bufferOutput(final short[] buffer) {
    synchronized(this) {
      if(this.bufferIndex >= 0) {
        final int bufferId = this.buffers[this.bufferIndex--];
        alBufferData(bufferId, this.format, buffer, this.sampleRate);
        alSourceQueueBuffers(this.sourceId, bufferId);
      }
    }
  }

  protected void bufferOutput(final ShortBuffer buffer) {
    synchronized(this) {
      if(this.bufferIndex >= 0) {
        final int bufferId = this.buffers[this.bufferIndex--];
        alBufferData(bufferId, this.format, buffer, this.sampleRate);
        alSourceQueueBuffers(this.sourceId, bufferId);
      }
    }
  }

  protected void bufferOutput(final float[] buffer) {
    synchronized(this) {
      if(this.bufferIndex >= 0) {
        final int bufferId = this.buffers[this.bufferIndex--];
        alBufferData(bufferId, this.format, buffer, this.sampleRate);
        alSourceQueueBuffers(this.sourceId, bufferId);
      }
    }
  }

  protected void play() {
   ;
    if(alGetSourcei(this.sourceId, AL_SOURCE_STATE) != AL_PLAYING) {
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

  public void setVolume(final float volume) {
    alSourcef(this.sourceId, AL_GAIN, volume);
  }

  /** NOTE: this method will return the play time of the current buffer, so if you're using more than one buffer it's likely not going to return what you expect */
  public float getPosition() {
    return alGetSourcef(this.sourceId, AL_SEC_OFFSET);
  }
}
