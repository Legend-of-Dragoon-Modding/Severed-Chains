package legend.core.openal;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.opus.OpusFile;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.system.MemoryStack.stackPush;

public class XaFile {
  final static int OGG_SAMPLE_LIMIT = 960;
  final int channelCount;
  final int sourceId;
  final int bufferId;
  final String filePath;

  private boolean isPlaying = false;

  public XaFile(final String filePath, final boolean loops) {
    this.filePath = filePath;

    try(final MemoryStack memoryStack = stackPush()) {
      final IntBuffer error = memoryStack.mallocInt(1);

      final long oggFile = OpusFile.op_open_file(this.filePath, error);

      if (error.get() != 0) {
        throw new RuntimeException("Failed to open file " + this.filePath);
      }

      this.channelCount = OpusFile.op_channel_count(oggFile, -1);

      int format = -1;
      if (this.channelCount == 1) {
        format = AL_FORMAT_MONO16;
      } else if (this.channelCount == 2) {
        format = AL_FORMAT_STEREO16;
      }

      final int sampleCount = (int) OpusFile.op_pcm_total(oggFile, -1);

      final short[] rawPcm = new short[sampleCount];

      final int limit = (int) Math.ceil((double) sampleCount / OGG_SAMPLE_LIMIT * this.channelCount);

      final ShortBuffer pcmBuffer = memoryStack.mallocShort(OGG_SAMPLE_LIMIT * this.channelCount);
      final IntBuffer li = memoryStack.mallocInt(1);

      for(int i = 0; i < limit; i++) {
        pcmBuffer.position(0);
        OpusFile.op_read(oggFile, pcmBuffer, li);
        pcmBuffer.position(0);
        pcmBuffer.get(0, rawPcm, i * OGG_SAMPLE_LIMIT * this.channelCount, Math.min(OGG_SAMPLE_LIMIT * this.channelCount, sampleCount - i * OGG_SAMPLE_LIMIT * this.channelCount));
      }

      OpusFile.op_free(oggFile);

      this.bufferId = alGenBuffers();

      alBufferData(this.bufferId, format, rawPcm, 48000);

      this.sourceId = alGenSources();
      alSourcei(this.sourceId, AL_BUFFER, this.bufferId);
      alSourcei(this.sourceId, AL_LOOPING, loops ? 1: 0);
      alSourcei(this.sourceId, AL_POSITION, 0);
      alSourcef(this.sourceId, AL_GAIN, 0.3f);
    }
  }

  public void delete() {
    alDeleteBuffers(this.bufferId);
    alDeleteSources(this.sourceId);
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

  public String getFilePath() {
    return this.filePath;
  }

  public boolean isPlaying() {
    final int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);
    if (state == AL_STOPPED) {
      this.isPlaying = false;
    }

    return this.isPlaying;
  }
}
