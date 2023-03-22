package legend.core.openal;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_POSITION;
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
import static org.lwjgl.openal.AL10.alSourcei;

public class BufferedSound {
  private static final int BUFFER_SIZE = 1024;
  private static final int BUFFER_COUNT = 4;

  private final int[] buffers = new int[BUFFER_COUNT];

  private final int format;
  private final int sourceId;

  private boolean isPlaying = false;
  private int bufferIndex;

  public BufferedSound(final boolean stereo) {
    this.format = stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;

    this.sourceId = alGenSources();

    for(int i = 0; i < BUFFER_COUNT; i++) {
      this.bufferSamples(new short[0], 44100);
    }

    alSourcef(this.sourceId, AL_GAIN, 0.3f); //TODO actual volume control

    alSourcePlay(this.sourceId);
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

  public void process() {
    final int processedBuffers = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);
    for (int i = 0; i < processedBuffers; i++) {
      alDeleteBuffers(alSourceUnqueueBuffers(this.sourceId));
      this.buffers[this.bufferIndex] = alGenBuffers();
    }
  }

  public void bufferSamples(final short[] pcm, final int sampleRate) {
    final int buffer = this.buffers[this.bufferIndex++];
    alBufferData(buffer, this.format, pcm, sampleRate);
    alSourceQueueBuffers(this.sourceId, buffer);
    this.bufferIndex %= BUFFER_COUNT;
  }

  public boolean isPlaying() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);
    if (state == AL_STOPPED) {
      this.isPlaying = false;
    }

    return this.isPlaying;
  }
}
