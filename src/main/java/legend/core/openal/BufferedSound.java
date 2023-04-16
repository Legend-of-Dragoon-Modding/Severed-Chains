package legend.core.openal;

import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_STOPPED;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceQueueBuffers;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourceUnqueueBuffers;
import static org.lwjgl.openal.AL10.alSourcef;

public class BufferedSound {
  /**
   * Use powers of 2 to avoid % operator
   */
  private static final int BUFFER_COUNT = 8;
  /**
   * 1/20 of a second at 44100 Hz
   */
  private static final int BUFFER_SIZE = 882;

  private final int[] buffers = new int[BUFFER_COUNT];
  private final int bufferSize;

  private final int format;
  private final int sourceId;

  private int bufferPosition;
  private short[] sampleBuffer;

  private boolean isPlaying;
  private int bufferIndex;

  public BufferedSound(final boolean stereo) {
    this.format = stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
    this.bufferSize = BUFFER_SIZE * (stereo ? 2 : 1);
    this.sampleBuffer = new short[this.bufferSize];

    this.sourceId = alGenSources();

    for(int i = 0; i < BUFFER_COUNT; i++) {
      this.buffers[i] = alGenBuffers();
    }

    alSourcef(this.sourceId, AL_GAIN, 0.3f); //TODO actual volume control

    alSourcePlay(this.sourceId);
  }

  public void bufferSample(final short sample) {
    this.sampleBuffer[this.bufferPosition++] = sample;

    if(this.bufferPosition >= this.bufferSize) {
      this.bufferSamples(this.sampleBuffer);

      this.bufferPosition = 0;
      this.sampleBuffer = new short[this.bufferSize];
    }
  }

  private void bufferSamples(final short[] pcm) {
    this.processBuffer();

    final int buffer = this.buffers[this.bufferIndex++];
    alBufferData(buffer, this.format, pcm, 44100);
    alSourceQueueBuffers(this.sourceId, buffer);
    this.bufferIndex &= BUFFER_COUNT - 1;
  }

  /**
   * Dequeues processed buffers, deletes them, then creates new ones.
   */
  private void processBuffer() {
    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int i = 0; i < processedBufferCount; i++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alGenBuffers(this.buffers);
  }

  public void play() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);
    if (state == AL_STOPPED) {
      this.isPlaying = false;
    }

    if (!this.isPlaying) {
      alSourcePlay(this.sourceId);
      this.isPlaying = true;
    }
  }

  public void stop() {
    if (this.isPlaying) {
      alSourceStop(this.sourceId);
      this.isPlaying = false;
    }
  }

  public void pause() {
    if (this.isPlaying) {
      alSourcePause(this.sourceId);
      this.isPlaying = false;
    }
  }

  public void destroy() {
    alDeleteSources(this.sourceId);
    alDeleteBuffers(this.buffers);
  }

  public boolean isPlaying() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);
    if (state == AL_STOPPED) {
      this.isPlaying = false;
    }

    return this.isPlaying;
  }
}
