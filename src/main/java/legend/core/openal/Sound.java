package legend.core.openal;

import static org.lwjgl.openal.AL10.AL_BUFFER;
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
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;

public class Sound {
  private final int format;
  private final int sourceId;
  private final int bufferId;

  private boolean isPlaying = false;

  public Sound(final boolean stereo, final boolean loops) {
    this.format = stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
    this.bufferId = alGenBuffers();
    this.sourceId = alGenSources();

    alSourcei(this.sourceId, AL_BUFFER, this.bufferId);
    alSourcei(this.sourceId, AL_LOOPING, loops ? 1: 0);
    alSourcef(this.sourceId, AL_GAIN, 1f); //TODO actual volume control
  }

  public Sound(final boolean stereo, final boolean loops, short[] pcm) {
    this.format = stereo ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
    this.bufferId = alGenBuffers();
    this.sourceId = alGenSources();

    alBufferData(this.bufferId, this.format, pcm, 44100/4);
    alSourcei(this.sourceId, AL_BUFFER, this.bufferId);
    alSourcei(this.sourceId, AL_LOOPING, loops ? 1: 0);
    alSourcef(this.sourceId, AL_GAIN, 0.3f); //TODO actual volume control
  }

  public void play() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);
    if (state == AL_STOPPED) {
      this.isPlaying = false;
      alSourcei(this.sourceId, AL_POSITION, 0);
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
    alDeleteBuffers(this.bufferId);
    alDeleteSources(this.sourceId);
  }

  public void bufferData(final short[] pcm, final int sampleRate) {
    alBufferData(this.bufferId, this.format, pcm, sampleRate);
    alSourcei(this.sourceId, AL_BUFFER, this.bufferId);
  }

  public boolean isPlaying() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);
    if (state == AL_STOPPED) {
      this.isPlaying = false;
    }

    return this.isPlaying;
  }
}
