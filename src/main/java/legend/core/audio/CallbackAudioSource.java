package legend.core.audio;

import org.lwjgl.openal.SOFTCallbackBufferTypeI;

import static legend.core.audio.Constants.ENGINE_SAMPLE_RATE;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.SOFTCallbackBuffer.alBufferCallbackSOFT;

public abstract class CallbackAudioSource {
  private static final int AL_FORMAT_STEREO32 = 0x10011;

  private final long id;

  private int source;
  private int buffer;

  private float volume;

  public CallbackAudioSource(final long id, final float volume) {
    this.id = id;
    this.volume = volume;

    this.init();
  }

  private void init() {
    this.source = alGenSources();
    this.buffer = alGenBuffers();

    final SOFTCallbackBufferTypeI callback = (userPtr, bufferPtr, numBytes) -> {
      // We're using stereo with floats, so we get 8 times the bytes
      this.tick(bufferPtr, numBytes >>> 3);

      return numBytes;
    };

    alBufferCallbackSOFT(
      this.buffer,
      AL_FORMAT_STEREO32,
      ENGINE_SAMPLE_RATE,
      callback,
      this.id
    );

    alSourcei(this.source, AL_BUFFER, this.buffer);

    this.setVolume(this.volume);
  }

  public void reinit() {
    this.destroy();
    this.init();
  }

  /**
   * This needs to produce n interleaved float samples and push them into a provided buffer
   * @param bufferPtr interleaved stereo float buffer (L, R, L, R...)
   * @param sampleCount number of stereo frames
   */
  public abstract void tick(final long bufferPtr, final long sampleCount);

  public void setVolume(final float volume) {
    this.volume = volume;
    alSourcef(this.source, AL_GAIN, volume);
  }

  public void destroy() {
    alSourceStop(this.source);

    alDeleteBuffers(this.buffer);
    alDeleteSources(this.source);
  }

  public void play() {
    alSourcePlay(this.source);
  }
}
