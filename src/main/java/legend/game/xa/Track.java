package legend.game.xa;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.opus.OpusEnc;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;

public class Track {
  private static final int[] positiveXaAdpcmTable = { 0, 60, 115, 98, 122 };
  private static final int[] negativeXaAdpcmTable = { 0, 0, -52, -55, -60 };
  /**
   * Determines how many samples per channel get allocated on the stack.
   * In the worst case scenario 2000 samples in stereo will result in 8000 bytes allocated.
   */
  private static final int SAMPLES_PER_CHANNEL = 2000;

  private final String name;
  private boolean isStereo;
  private final short[] old = { 0, 0 };
  private final short[] older = { 0, 0 };

  private final ShortList leftChannel = new ShortArrayList();
  private final ShortList rightChannel = new ShortArrayList();

  public Track(final String name) {
    this.name = name;
  }

  public void addAudioFrame(final ByteBuffer sector) {
    this.isStereo = (sector.get(19) & 0x1) == 1;

    int position = 24;

    for(int i = 0; i < 18; i++) { //Each sector consists of 12h 128-byte portions (=900h bytes) (the remaining 14h bytes of the sectors 914h-byte data region are 00h filled).
      for(int blk = 0; blk < 4; blk++) {

        this.leftChannel.addAll(this.decodeNibbles(sector, position, blk, 0, 0));

        if(this.isStereo) {
          this.rightChannel.addAll(this.decodeNibbles(sector, position, blk, 1, 1));
        } else {
          this.leftChannel.addAll(this.decodeNibbles(sector, position, blk, 1, 0));
        }
      }

      position += 128;
    }
  }

  public void encode() {
    //temporary fix for unused files
    if (this.leftChannel.size() <= 4032) {
      return;
    }

    final int channels = this.isStereo ? 2 : 1;

    try(final MemoryStack memoryStack = stackPush()) {
      final IntBuffer error = memoryStack.mallocInt(1);

      final long comments = OpusEnc.ope_comments_create();

      final long encoder = OpusEnc.ope_encoder_create_file(this.name, comments, 37800, channels, 0, error);

      final short[] dataArr;

      if (this.isStereo) {
        dataArr = new short[this.leftChannel.size() * 2];

        for (int i = 0; i < this.leftChannel.size(); i++) {
          dataArr[i * 2] = this.leftChannel.getShort(i);
          dataArr[i * 2 + 1] = this.rightChannel.getShort(i);
        }
      } else {
        dataArr = new short[this.leftChannel.size()];

        for (int i = 0; i < this.leftChannel.size(); i++) {
          dataArr[i] = this.leftChannel.getShort(i);
        }
      }

      final ShortBuffer samples = memoryStack.mallocShort(SAMPLES_PER_CHANNEL * channels);

      for (int i = 0; i < dataArr.length;) {
        samples.position(0);
        samples.put(dataArr, i, Math.min(SAMPLES_PER_CHANNEL * channels, dataArr.length - i));
        samples.position(0);
        OpusEnc.ope_encoder_write(encoder, samples, SAMPLES_PER_CHANNEL);
        samples.clear();
        i += SAMPLES_PER_CHANNEL * channels;
      }

      OpusEnc.ope_encoder_drain(encoder);

      OpusEnc.ope_encoder_destroy(encoder);

      OpusEnc.ope_comments_destroy(comments);
    }
  }

  private ShortList decodeNibbles(final ByteBuffer xaapdcm, final int position, final int blk, final int nibble, final int lr) {
    final ShortList list = new ShortArrayList();

    final int shift = 12 - (xaapdcm.get(position + 4 + blk * 2 + nibble) & 0x0F);
    final int filter = (xaapdcm.get(position + 4 + blk * 2 + nibble) & 0x30) >> 4;

    final int f0 = positiveXaAdpcmTable[filter];
    final int f1 = negativeXaAdpcmTable[filter];

    for(int i = 0; i < 28; i++) {
      final int t = signed4bit((byte)(xaapdcm.get(position + 16 + blk + i * 4) >> nibble * 4 & 0x0F));
      final int s = (t << shift) + (this.old[lr] * f0 + this.older[lr] * f1 + 32) / 64;
      final short sample = (short)s;

      list.add(sample);
      this.older[lr] = this.old[lr];
      this.old[lr] = sample;
    }

    return list;
  }

  private static int signed4bit(final byte value) {
    return value << 28 >> 28;
  }
}
