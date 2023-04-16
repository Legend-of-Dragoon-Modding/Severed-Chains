package legend.core.audio;

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
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceQueueBuffers;
import static org.lwjgl.openal.AL10.alSourceUnqueueBuffers;
import static org.lwjgl.openal.AL10.alSourcef;

final class BufferedSound {
  /**
   * Use powers of 2 to avoid % operator
   */
  private static final int BUFFER_COUNT = 4;

  private final int[] buffers = new int[BUFFER_COUNT];
  private int bufferIndex;
  private final int bufferSize;
  private short[] sampleBuffer;
  private int bufferPosition;
  private final int format;
  private final int sourceId;

  private boolean playing;

  BufferedSound(final int bufferSize, final boolean stereo) {
    this.format = stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
    this.bufferSize = bufferSize * (stereo ? 2 : 1);
    this.sampleBuffer = new short[this.bufferSize];

    this.sourceId = alGenSources();

    alGenBuffers(this.buffers);

    this.bufferSamples(new short[this.bufferSize]);

    alSourcef(this.sourceId, AL_GAIN, 0.3f); //TODO actual volume control

  }

  void bufferSample(final short sample) {
    this.sampleBuffer[this.bufferPosition++] = sample;

    if(this.bufferPosition >= this.bufferSize) {
      this.bufferSamples(this.sampleBuffer);

      this.bufferPosition = 0;
      this.sampleBuffer = new short[this.bufferSize];
    }
  }

  private void bufferSamples(final short[] pcm) {
    this.processBuffers();

    final int bufferId = this.buffers[this.bufferIndex++];
    alBufferData(bufferId, this.format, pcm, 44_100);
    alSourceQueueBuffers(this.sourceId, bufferId);
    this.bufferIndex &= BUFFER_COUNT - 1;
  }

  void processBuffers() {
    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alGenBuffers(this.buffers);
  }

  boolean isStereo() {
    return this.format == AL_FORMAT_STEREO16;
  }

  void play() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);

    if(state == AL_STOPPED) {
      this.playing = false;
    }

    if(!this.playing) {
      alSourcePlay(this.sourceId);
      this.playing = true;
    }
  }

  void destroy() {
    alDeleteSources(this.sourceId);
    alDeleteBuffers(this.buffers);
  }
}
