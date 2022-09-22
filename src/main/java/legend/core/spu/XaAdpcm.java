package legend.core.spu;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.memory.Ref;

import java.nio.ByteBuffer;
import java.util.List;

public final class XaAdpcm {
  private XaAdpcm() { }

  private static final int BYTES_PER_HEADER = 24;

  private static short oldL;
  private static short olderL;
  private static short oldR;
  private static short olderR;
  private static int sixStep = 6;
  private static int resamplePointer;
  private static final short[][] resampleRingBuffer = { new short[32], new short[32] };

  private static final int[] positiveXaAdpcmTable = { 0, 60, 115, 98, 122 };
  private static final int[] negativeXaAdpcmTable = { 0, 0, -52, -55, -60 };

  private static final short[][] zigZagTable = {
    new short[] {
            0,       0,       0,       0,       0, -0x0002,  0x000A, -0x0022,
       0x0041, -0x0054,  0x0034,  0x0009, -0x010A,  0x0400, -0x0A78,  0x234C,
       0x6794, -0x1780,  0x0BCD, -0x0623,  0x0350, -0x016D,  0x006B,  0x000A,
      -0x0010,  0x0011, -0x0008,  0x0003, -0x0001,
    },

    new short[] {
            0,       0,       0, -0x0002,       0,  0x0003, -0x0013,  0x003C,
      -0x004B,  0x00A2, -0x00E3,  0x0132, -0x0043, -0x0267,  0x0C9D,  0x74BB,
      -0x11B4,  0x09B8, -0x05BF,  0x0372, -0x01A8,  0x00A6, -0x001B,  0x0005,
       0x0006, -0x0008,  0x0003, -0x0001,       0,
    },

    new short[] {
            0,       0, -0x0001,  0x0003, -0x0002, -0x0005,  0x001F, -0x004A,
       0x00B3, -0x0192,  0x02B1, -0x039E,  0x04F8, -0x05A6,  0x7939, -0x05A6,
       0x04F8, -0x039E,  0x02B1, -0x0192,  0x00B3, -0x004A,  0x001F, -0x0005,
      -0x0002,  0x0003, -0x0001,       0,       0,
    },

    new short[] {
            0, -0x0001,  0x0003, -0x0008,  0x0006,  0x0005, -0x001B,  0x00A6,
      -0x01A8,  0x0372, -0x05BF,  0x09B8, -0x11B4,  0x74BB,  0x0C9D, -0x0267,
      -0x0043,  0x0132, -0x00E3,  0x00A2, -0x004B,  0x003C, -0x0013,  0x0003,
            0, -0x0002,       0,       0,       0,
    },

    new short[] {
      -0x0001,  0x0003, -0x0008,  0x0011, -0x0010,  0x000A,  0x006B, -0x016D,
       0x0350, -0x0623,  0x0BCD, -0x1780,  0x6794,  0x234C, -0x0A78,  0x0400,
      -0x010A,  0x0009,  0x0034, -0x0054,  0x0041, -0x0022,  0x000A, -0x0001,
            0,  0x0001,       0,       0,       0,
    },

    new short[] {
       0x0002, -0x0008,  0x0010, -0x0023,  0x002B,  0x001A, -0x00EB,  0x027B,
      -0x0548,  0x0AFA, -0x16FA,  0x53E0,  0x3C07, -0x1249,  0x080E, -0x0347,
       0x015B, -0x0044, -0x0017,  0x0046, -0x0023,  0x0011, -0x0005,       0,
            0,       0,       0,       0,       0,
    },

    new short[] {
      -0x0005,  0x0011, -0x0023,  0x0046, -0x0017, -0x0044,  0x015B, -0x0347,
       0x080E, -0x1249,  0x3C07,  0x53E0, -0x16FA,  0x0AFA, -0x0548,  0x027B,
      -0x00EB,  0x001A,  0x002B, -0x0023,  0x0010, -0x0008,  0x0002,       0,
            0,       0,       0,       0,       0,
    }
  };

  public static byte[] decode(final byte[] xaadpcm, final byte codingInfo) {
    final ByteList decoded = new ByteArrayList();

    final ShortList l = new ShortArrayList();
    final ShortList r = new ShortArrayList();

    final boolean isStereo = (codingInfo & 0x1) != 0;
    final boolean is18900hz = (codingInfo >>> 2 & 0x1) != 0;
    final boolean is8BitPerSample = (codingInfo >>> 4 & 0x1) != 0;

    //Console.WriteLine($"decoding XAPCDM {xaadpcm.Length} is18900: {is18900hz} is8Bit: {is8BitPerSample} isStereo: {isStereo}");

    int position = BYTES_PER_HEADER; //Skip sync, header and subheader
    for(int i = 0; i < 18; i++) { //Each sector consists of 12h 128-byte portions (=900h bytes) (the remaining 14h bytes of the sectors 914h-byte data region are 00h filled).
      for(int blk = 0; blk < 4; blk++) {
        final Ref<Short> oldLRef = new Ref<>(oldL);
        final Ref<Short> oldRRef = new Ref<>(oldR);
        final Ref<Short> olderLRef = new Ref<>(olderL);
        final Ref<Short> olderRRef = new Ref<>(olderR);

        l.addAll(decodeNibbles(xaadpcm, position, blk, 0, oldLRef, olderLRef));

        if(isStereo) {
          r.addAll(decodeNibbles(xaadpcm, position, blk, 1, oldRRef, olderRRef));
        } else {
          l.addAll(decodeNibbles(xaadpcm, position, blk, 1, oldLRef, olderLRef));
        }
        //Console.WriteLine("nextblock " + blk);

        oldL = oldLRef.get();
        oldR = oldRRef.get();
        olderL = olderLRef.get();
        olderR = olderRRef.get();
      }

      //Console.WriteLine("next i " + i + "position" + position);
      position += 128;
    }

    if(isStereo) {
      final ShortList resampledL = resampleTo44100Hz(l, is18900hz, 0);
      final ShortList resampledR = resampleTo44100Hz(r, is18900hz, 1);
      //Console.WriteLine("Sizes" + resampledL.Count + " " + resampledR.Count);

      for(int sample = 0; sample < resampledL.size(); sample++) {
        decoded.add((byte)resampledL.getShort(sample));
        decoded.add((byte)(resampledL.getShort(sample) >> 8));
        decoded.add((byte)resampledR.getShort(sample));
        decoded.add((byte)(resampledR.getShort(sample) >> 8));
      }
    } else {
      final ShortList resampledMono = resampleTo44100Hz(l, is18900hz, 0);

      for(int sample = 0; sample < resampledMono.size(); sample++) {
        //duplicating because out output expects 44100 Stereo
        decoded.add((byte)resampledMono.getShort(sample));
        decoded.add((byte)(resampledMono.getShort(sample) >> 8));
        decoded.add((byte)resampledMono.getShort(sample));
        decoded.add((byte)(resampledMono.getShort(sample) >> 8));
      }
    }

    return decoded.toByteArray();
  }

  private static ShortList resampleTo44100Hz(final List<Short> samples, final boolean is18900hz, final int channel) {
    final ShortList resamples = new ShortArrayList();

    //todo handle 18900hz

    for(final short sample : samples) {
      resampleRingBuffer[channel][resamplePointer++ & 0x1F] = sample;

      sixStep--;
      if(sixStep == 0) {
        sixStep = 6;
        for(int table = 0; table < 7; table++) {
          resamples.add(zigZagInterpolate(resamplePointer, table, channel));
        }
      }
    }

    return resamples;
  }

  private static short zigZagInterpolate(final int resamplePointer, final int table, final int channel) {
    int sum = 0;
    for(int i = 0; i < 29; i++) {
      sum += resampleRingBuffer[channel][resamplePointer - i & 0x1F] * zigZagTable[table][i] / 0x8000;
    }

    return (short)MathHelper.clamp(sum, -0x8000, 0x7FFF);
  }

  public static ShortList decodeNibbles(final byte[] xaapdcm, final int position, final int blk, final int nibble, final Ref<Short> old, final Ref<Short> older) {
    final ShortList list = new ShortArrayList();

    final int shift = 12 - (xaapdcm[position + 4 + blk * 2 + nibble] & 0x0F);
    final int filter = (xaapdcm[position + 4 + blk * 2 + nibble] & 0x30) >> 4;

    final int f0 = positiveXaAdpcmTable[filter];
    final int f1 = negativeXaAdpcmTable[filter];

    for(int i = 0; i < 28; i++) {
      final int t = signed4bit((byte)(xaapdcm[position + 16 + blk + i * 4] >> nibble * 4 & 0x0F));
      final int s = (t << shift) + (old.get() * f0 + older.get() * f1 + 32) / 64;
      final short sample = (short)MathHelper.clamp(s, -0x8000, 0x7FFF);

      list.add(sample);
      older.set(old.get());
      old.set(sample);
    }

    return list;
  }

  public static int signed4bit(final byte value) {
    return value << 28 >> 28;
  }

  public static void dump(final ByteBuffer stream) {
    IoHelper.write(stream, oldL);
    IoHelper.write(stream, olderL);
    IoHelper.write(stream, oldR);
    IoHelper.write(stream, olderR);
    IoHelper.write(stream, sixStep);
    IoHelper.write(stream, resamplePointer);

    for(final short[] buffer : resampleRingBuffer) {
      for(final short value : buffer) {
        IoHelper.write(stream, value);
      }
    }
  }

  public static void load(final ByteBuffer stream) {
    oldL = IoHelper.readShort(stream);
    olderL = IoHelper.readShort(stream);
    oldR = IoHelper.readShort(stream);
    olderR = IoHelper.readShort(stream);
    sixStep = IoHelper.readInt(stream);
    resamplePointer = IoHelper.readInt(stream);

    for(int i = 0; i < resampleRingBuffer.length; i++) {
      for(int n = 0; n < resampleRingBuffer[i].length; n++) {
        resampleRingBuffer[i][n] = IoHelper.readShort(stream);
      }
    }
  }
}
