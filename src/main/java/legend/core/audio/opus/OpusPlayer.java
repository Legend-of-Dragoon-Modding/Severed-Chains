package legend.core.audio.opus;

import legend.core.audio.AudioSource;
import legend.core.audio.AudioTag;
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
import static org.lwjgl.system.MemoryUtil.NULL;

public final class OpusPlayer extends AudioSource {
  private static final Logger LOGGER = LogManager.getFormatterLogger(OpusPlayer.class);
  private static final int BUFFER_SIZE = 4096;
  private static final int OPUS_SAMPLE_RATE = 48_000;
  /** Maximum size of an opus block is a 60 ms, which at a 48kHz sample rate means 2880 samples per channel */
  private static final int OPUS_MAX_BLOCK_SIZE = 2880;

  private ByteBuffer opusFileData;
  private long opusFile;
  private final int channelCount;
  private final ShortBuffer pcmBuffer;

  public OpusPlayer(final FileData opusFile, final float volume, final AudioTag tag) {
    final ByteBuffer buffer = BufferUtils.createByteBuffer(opusFile.size());
    buffer.put(opusFile.getBytes());
    this.opusFileData = buffer.rewind();

    final long opus;
    try(final MemoryStack stack = MemoryStack.stackPush()) {
      final IntBuffer error = stack.mallocInt(1);
      opus = OpusFile.op_open_memory(buffer, error);
      this.opusFile = opus;

      if(error.get(0) != 0) {
        LOGGER.error("Error opening Opus file:  0x%x", error.get(0));
      }
    }

    final int channelCount = OpusFile.op_channel_count(opus, -1);
    this.channelCount = channelCount;
    this.pcmBuffer = BufferUtils.createShortBuffer((BUFFER_SIZE + OPUS_MAX_BLOCK_SIZE) * channelCount);

    super(channelCount == 2 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16, OPUS_SAMPLE_RATE, volume, tag);

    for(int i = 0; i < 3; i++) {
      if(!this.eof) {
        this.fillBuffer();
      }
    }
  }

  @Override
  protected void fillBuffer() {
    final int limit = BUFFER_SIZE * this.channelCount;

    while(this.pcmBuffer.position() < limit) {
      final int decoded = OpusFile.op_read(this.opusFile, this.pcmBuffer, null);

      if(decoded <= 0) {
        this.eof = true;
        this.unloadOpusFile();
        break;
      }

      // op_read doesn't advance the position on the buffer
      this.pcmBuffer.position(this.pcmBuffer.position() + decoded * this.channelCount);
    }

    final int pos = this.pcmBuffer.position();

    // "Flip" the buffer
    this.pcmBuffer.limit(Math.min(pos, limit));
    this.pcmBuffer.position(0);

    this.bufferOutput(this.pcmBuffer);

    // Reset position so we can compact the data past the buffer size
    this.pcmBuffer.limit(pos);
    this.pcmBuffer.position(Math.min(limit, pos));
    this.pcmBuffer.compact();
  }

  public void unloadOpusFile() {
    if(this.opusFile != NULL) {
      OpusFile.op_free(this.opusFile);
      this.opusFile = NULL;
      this.opusFileData = null;
    }
  }

  @Override
  protected void destroy() {
    this.unloadOpusFile();

    super.destroy();
  }
}
