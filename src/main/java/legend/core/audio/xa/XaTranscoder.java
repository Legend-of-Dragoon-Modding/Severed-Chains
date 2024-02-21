package legend.core.audio.xa;

import legend.core.MathHelper;
import legend.core.audio.opus.OpusFile;
import legend.game.unpacker.FileData;
import legend.game.unpacker.PathNode;
import legend.game.unpacker.Transformations;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.opus.Opus;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class XaTranscoder {
  private static final int[] POSITIVE_XA_ADPCM_TABLE = { 0, 60, 115, 98, 122 };
  private static final int[] NEGATIVE_XA_ADPCM_TABLE = { 0, 0, -52, -55, -60 };
  private static final double[][] INTERPOLATION_WEIGHTS = new double[80][];
  static {
    for(int i = 0; i < INTERPOLATION_WEIGHTS.length; i++) {
      final double pow1 = i / (double)INTERPOLATION_WEIGHTS.length;
      final double pow2 = pow1 * pow1;
      final double pow3 = pow2 * pow1;

      INTERPOLATION_WEIGHTS[i] = new double[] {
        0.5d * (-pow3 + 2 * pow2 - pow1),
        0.5d * (3 * pow3 - 5 * pow2 + 2),
        0.5d * (-3 * pow3 + 4 * pow2 + pow1),
        0.5d * (pow3 - pow2)
      };
    }
  }
  private final long encoder;
  private final short[] old = {0, 0};
  private final short[] older = {0, 0};
  private static final short[] EMPTY = {0, 0, 0};
  private static final int[] SKIPS = {0xFFFE, 0xFFFE, 0xEFE, 0x2};

  private static final int OPUS_FRAME_SIZE = 960;
  private static final int XA_ADPCM_BLOCK_SIZE = 112;
  private static final short PRE_SKIP = 312;
  private final short[][] sourceBuffer = new short[2][];
  private int sourceBufferPosition;
  private int interpolationCounter = 160;
  private final ShortBuffer opusInputBuffer;
  private int opusInputBufferPosition;
  private static final int OPUS_OUTPUT_BUFFER_SIZE = 500;
  private final ByteBuffer opusOutputBuffer;
  private OpusFile opusFile;
  private final int channels;

  private XaTranscoder(final int channels) {
    this.channels = channels;
    this.encoder = Opus.opus_encoder_create(48000, channels, Opus.OPUS_APPLICATION_AUDIO, null);
    Opus.opus_encoder_ctl(this.encoder, Opus.OPUS_SET_BITRATE(128_000));

    this.sourceBuffer[0] = new short[XA_ADPCM_BLOCK_SIZE * (3 - this.channels) + EMPTY.length];
    this.sourceBuffer[1] = new short[(XA_ADPCM_BLOCK_SIZE + EMPTY.length) * (this.channels - 1)];

    this.opusInputBuffer = BufferUtils.createShortBuffer(OPUS_FRAME_SIZE * channels);
    this.opusOutputBuffer = BufferUtils.createByteBuffer(OPUS_OUTPUT_BUFFER_SIZE * this.channels);
  }

  public static void transform(final PathNode node, final Transformations transformations) {
    final XaTranscoder transcoder = new XaTranscoder(node.fullPath.endsWith("3.XA") ? 2 : 1);

    transcoder.transcode(node, transformations);
  }

  private void transcode(final PathNode node, final Transformations transformations) {
    final int interleaveMode = node.fullPath.endsWith("3.XA") ? 4 : 16;
    final int sectorCount = node.data.size() / 0x930;
    final int channelSectorCount = sectorCount / interleaveMode;

    final int skipIndex = Integer.parseInt(node.fullPath, 9, 10, 10);

    final int skip = SKIPS[skipIndex];

    for(int track = 1; track < interleaveMode; track++) {
      if(((skip >> track) & 0x1) == 0) {
        continue;
      }

      this.reset();

      this.opusFile = new OpusFile((byte)this.channels, PRE_SKIP, 37800);
      this.opusFile.addComment("tracknumber=%d".formatted(track + 1));
      this.opusFile.addComment("totaltracks=%d".formatted(interleaveMode));
      this.opusFile.addComment("album=" + node.fullPath);

      for(int sector = 0; sector < channelSectorCount; sector++) {
        this.processSector(node.data.slice((sector * interleaveMode + track) * 0x930, 0x930));

        if((node.data.readUByte((sector * interleaveMode + track) * 0x930 + 18) >>> 7 & 0x01) == 1) {
          break;
        }
      }

      // Process partial data
      if(this.opusInputBufferPosition != 0) {
        this.encodeOpusData();
      }

      transformations.addNode(node.fullPath + '/' + track + ".opus", new FileData(this.opusFile.toBytes()));
    }

    Opus.opus_encoder_destroy(this.encoder);
  }

  private void reset() {
    Opus.opus_encoder_ctl(this.encoder, Opus.OPUS_RESET_STATE);
    this.old[0] = 0;
    this.old[1] = 0;
    this.older[0] = 0;
    this.older[1] = 0;

    this.opusInputBuffer.clear();
    this.opusInputBuffer.put(0, new short[PRE_SKIP]);
    this.opusInputBuffer.clear();

    this.opusOutputBuffer.clear();
    this.opusOutputBuffer.put(0, new byte[OPUS_OUTPUT_BUFFER_SIZE * this.channels]);
    this.opusOutputBuffer.clear();

    this.interpolationCounter = 160;
    for(int channel = 0; channel < this.channels; channel++) {
      System.arraycopy(EMPTY, 0, this.sourceBuffer[channel], 0, EMPTY.length);
    }
  }

  private void processSector(final FileData sector) {
    int position = 24;
    //Each sector consists of 12h 80h-byte portions (=900h bytes) (the remaining 14h bytes of the sectors 914h-byte data region are 00h filled).
    for(int portion = 0; portion < 0x12; portion++) {
      this.sourceBufferPosition = 3;

      for(int channel = 0; channel < this.channels; channel++) {
        System.arraycopy(this.sourceBuffer[channel], XA_ADPCM_BLOCK_SIZE * (3 - this.channels), this.sourceBuffer[channel], 0, EMPTY.length);
      }

      for(int blk = 0; blk < 4; blk++) {
        this.decodeNibbles(sector, position, blk, 0, 0);
        this.sourceBufferPosition += 28 * (2 - this.channels);
        this.decodeNibbles(sector, position, blk, 1, this.channels - 1);
        this.sourceBufferPosition += 28;
      }

      position += 0x80;
      this.resampleSourceBuffers();
    }
  }

  private void decodeNibbles(final FileData xaAdPcm, final int position, final int blk, final int nibble, final int lr) {
    final int shift = 12 - (xaAdPcm.readUByte(position + 4 + blk * 2 + nibble) & 0x0f);
    final int filter = (xaAdPcm.readUByte(position + 4 + blk * 2 + nibble) & 0x30) >> 4;

    final int f0 = POSITIVE_XA_ADPCM_TABLE[filter];
    final int f1 = NEGATIVE_XA_ADPCM_TABLE[filter];

    for(int i = 0; i < 28; i++) {
      final int t = signed4bit((byte)(xaAdPcm.readUByte(position + 16 + blk + i * 4) >> nibble * 4 & 0x0f));
      final int s = (t << shift) + (this.old[lr] * f0 + this.older[lr] * f1 + 32) / 64;
      final short sample = (short)MathHelper.clamp(s, -0x8000, 0x7fff);

      this.sourceBuffer[lr][this.sourceBufferPosition + i] = sample;
      this.older[lr] = this.old[lr];
      this.old[lr] = sample;
    }
  }

  private static int signed4bit(final byte value) {
    return value << 28 >> 28;
  }

  private void resampleSourceBuffers() {
    while(this.interpolationCounter < XA_ADPCM_BLOCK_SIZE * 80 * (3 - this.channels)) {
      final int samplePosition = this.interpolationCounter / 80;
      final int interpolationIndex = this.interpolationCounter - (samplePosition * 80);

      final double[] interpolationWeights = INTERPOLATION_WEIGHTS[interpolationIndex];

      for(int channel = 0; channel < this.channels; channel++) {
        final double sample = interpolationWeights[0] * this.sourceBuffer[channel][samplePosition]
          + interpolationWeights[1] * this.sourceBuffer[channel][samplePosition + 1]
          + interpolationWeights[2] * this.sourceBuffer[channel][samplePosition + 2]
          + interpolationWeights[3] * this.sourceBuffer[channel][samplePosition + 3];

        this.opusInputBuffer.put(this.opusInputBufferPosition++, (short) MathHelper.clamp((int)sample >> 1, -0x8000, 0x7fff));
      }

      this.interpolationCounter += 63;

      if(this.opusInputBufferPosition >= OPUS_FRAME_SIZE * this.channels) {
        this.encodeOpusData();
      }
    }

    this.interpolationCounter -= XA_ADPCM_BLOCK_SIZE * 80 * (3 - this.channels);
  }

  private void encodeOpusData() {
    this.opusOutputBuffer.clear();
    final int encoded = Opus.opus_encode(this.encoder, this.opusInputBuffer, OPUS_FRAME_SIZE, this.opusOutputBuffer);

    final byte[] bytes = new byte[encoded];
    this.opusOutputBuffer.get(bytes, 0, encoded);
    this.opusFile.addOpusSegment(bytes);

    this.opusInputBuffer.clear();
    this.opusInputBufferPosition = 0;
  }
}