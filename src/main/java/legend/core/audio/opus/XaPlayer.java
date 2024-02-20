package legend.core.audio.opus;

import legend.game.unpacker.FileData;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.opus.OpusFile;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_BUFFERS_QUEUED;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
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

public final class XaPlayer {
  private ByteBuffer opusFileData;
  private long opusFile;
  private int channelCount;
  private int format;
  private final int samplesPerTick;
  private short[] pcm;
  private ShortBuffer pcmBuffer;

  /** Use powers of 2 to avoid % operator */
  private static final int BUFFER_COUNT = 8;
  private final int[] buffers = new int[BUFFER_COUNT];
  private int bufferIndex;
  private final int sourceId;

  private long sampleCount;
  private long samplesRead;
  private boolean playing;

  public XaPlayer(final int frequency) {
    if(48_000 % frequency != 0) {
      throw new IllegalArgumentException("Sample Rate (44_100) is not divisible by frequency");
    }

    this.samplesPerTick = 48_000 / frequency;

    this.channelCount = 1;
    this.format = AL_FORMAT_MONO16;
    this.pcm = new short[this.samplesPerTick];
    this.pcmBuffer = BufferUtils.createShortBuffer(this.samplesPerTick);

    this.sourceId = alGenSources();
    alGenBuffers(this.buffers);
  }

  public void loadXa(final FileData fileData) {
    this.opusFileData = BufferUtils.createByteBuffer(fileData.size());
    this.opusFileData.put(fileData.data());
    this.opusFileData.rewind();
    this.samplesRead = 0;

    this.opusFile = OpusFile.op_open_memory(this.opusFileData, null);

    final int newChannelCount = OpusFile.op_channel_count(this.opusFile, -1);

    if(this.channelCount != newChannelCount) {
      this.channelCount = newChannelCount;
      this.format = this.channelCount == 2 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
      this.pcm = new short[this.samplesPerTick * this.channelCount];
      this.pcmBuffer = BufferUtils.createShortBuffer(this.pcm.length);
    }

    this.sampleCount = OpusFile.op_pcm_total(this.opusFile, -1);

    if(this.sampleCount < this.samplesPerTick * 4) {
      throw new RuntimeException("XA file is less than 4 buffers in length (40ms)");
    }

    for(int i = 0; i < 4; i++) {
      this.readFile();
      this.bufferOutput();
    }

    this.play();
  }

  public boolean canBuffer() {
    if(!this.playing) {
      return false;
    }

    return alGetSourcei(this.sourceId, AL_BUFFERS_QUEUED) < 6;
  }

  public void tick() {
    this.readFile();
    this.bufferOutput();

    // Restart playback if stopped
    this.play();
  }

  public void processBuffers() {
    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alGenBuffers(this.buffers);
  }

  private void readFile() {
    this.pcmBuffer.clear();
    OpusFile.op_read(this.opusFile, this.pcmBuffer, null);

    this.pcmBuffer.rewind();
    this.pcmBuffer.get(this.pcm);

    this.samplesRead += this.pcm.length;

    this.playing = this.samplesRead <= this.sampleCount;

    if(!this.playing) {
      this.unloadOpusFile();
    }
  }

  private void unloadOpusFile() {
    OpusFile.op_free(this.opusFile);
    this.opusFileData = null;
  }

  public void destroy() {
    this.playing = false;
    alSourceStop(this.sourceId);

    if(this.opusFileData != null) {
      this.unloadOpusFile();
    }

    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alDeleteBuffers(this.buffers);
    alDeleteSources(this.sourceId);
  }

  private void bufferOutput() {
    final int bufferId = this.buffers[this.bufferIndex++];
    alBufferData(bufferId, this.format, this.pcm, 48_000);
    alSourceQueueBuffers(this.sourceId, bufferId);
    this.bufferIndex &= BUFFER_COUNT - 1;
  }

  private void play() {
    if(alGetSourcei(this.sourceId, AL_SOURCE_STATE) == AL_PLAYING) {
      return;
    }

    alSourcePlay(this.sourceId);
  }
}
