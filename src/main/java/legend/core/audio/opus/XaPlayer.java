package legend.core.audio.opus;

import legend.core.audio.AudioSource;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.opus.OpusFile;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class XaPlayer extends AudioSource {
  private static final Logger LOGGER = LogManager.getFormatterLogger(XaPlayer.class);

  private ByteBuffer opusFileData;
  private long opusFile;
  private int channelCount;
  private int format;
  private final int samplesPerTick;
  private short[] pcm;
  private ShortBuffer pcmBuffer;

  private long sampleCount;
  private long samplesRead;

  public XaPlayer() {
    super(8);

    this.samplesPerTick = 48000 / 100;

    this.channelCount = 1;
    this.format = AL_FORMAT_MONO16;
    this.pcm = new short[this.samplesPerTick];
    this.pcmBuffer = BufferUtils.createShortBuffer(this.samplesPerTick);
  }

  public void loadXa(final FileData fileData) {
    this.opusFileData = BufferUtils.createByteBuffer(fileData.size());
    this.opusFileData.put(fileData.getBytes());
    this.opusFileData.rewind();
    this.samplesRead = 0;

    try(final MemoryStack stack = stackPush()) {
      final IntBuffer error = stack.mallocInt(1);
      this.opusFile = OpusFile.op_open_memory(this.opusFileData, error);

      if(error.get(0) != 0) {
        LOGGER.error("Error opening Opus XA file: 0x%x", error.get(0));
      }
    }

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

    this.setPlaying(true);

    if(this.canBuffer()) {
      for(int i = 0; i < 4; i++) {
        this.readFile();
        this.bufferOutput(this.format, this.pcm, 48_000);
      }
    }

    if(this.isPlaying()) {
      this.play();
    }
  }

  @Override
  public void tick() {
    this.readFile();
    this.bufferOutput(this.format, this.pcm, 48_000);
    super.tick();
  }

  private void readFile() {
    this.pcmBuffer.clear();
    OpusFile.op_read(this.opusFile, this.pcmBuffer, null);

    this.pcmBuffer.rewind();
    this.pcmBuffer.get(this.pcm);

    this.samplesRead += this.pcm.length;

    this.setPlaying(this.samplesRead <= this.sampleCount);

    if(!this.isPlaying()) {
      this.unloadOpusFile();
    }
  }

  private void unloadOpusFile() {
    OpusFile.op_free(this.opusFile);
    this.opusFileData = null;
  }

  @Override
  public void destroy() {
    if(this.opusFileData != null) {
      this.unloadOpusFile();
    }

    super.destroy();
  }
}
