package legend.core.audio.opus;

import legend.core.MathHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ogg Page as defined by <a href="https://datatracker.ietf.org/doc/html/rfc3533#section-6">RFC3533</a>
 * <table>
 *  <tr><th></th><th style="text-align: center">0</th><th></th><th style="text-align: center">1</th><th></th><th style="text-align: center">2</th><th></th><th style="text-align: center">3</th></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">O</td><td style="text-align: center">|</td><td style="text-align: center">g</td><td style="text-align: center">|</td><td style="text-align: center">g</td><td style="text-align: center">|</td><td style="text-align: center">S</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">{@link OggPage#VERSION}</td><td style="text-align: center">|</td><td style="text-align: center">{@link OggPage#headerTypeFlag}</td><td style="text-align: center">|</td><td colspan=3></td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td></td><td></td><td></td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>{@link OggPage#granulePosition}</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td></td><td></td><td></td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td colspan=3></td><td style="text-align: center">|</td><td style="text-align: center" colspan=3>{@link OggPage#bitstreamSerialNumber}</td><td style="text-align: center">:</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------<td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">:</td><td colspan=3></td><td style="text-align: center">|</td><td style="text-align: center" colspan=3>{@link OggPage#pageSequenceNumber}</td><td style="text-align: center">:</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">:</td><td colspan=3></td><td style="text-align: center">|</td><td style="text-align: center" colspan=3>CRC Checksum</td><td style="text-align: center">:</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">:</td><td colspan=3></td><td style="text-align: center">|</td><td style="text-align: center">Segment Table Length</td><td style="text-align: center">|</td><td style="text-align: center">{@link OggPage#segmentTable}</td><td style="text-align: center">:</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">:</td><td style="text-align: center" colspan=7></td><td style="text-align: center">:</td></tr>
 * </table>
 */
final class OggPage {
  private final static byte[] MAGIC_SIGNATURE = {'O', 'g', 'g', 'S'};
  private final static byte VERSION = 0;
  /**
   * 0x1 Continued packet (data didn't fit into previous one)
   * 0x2 Start of logical stream BoS
   * 0x4 End of logical stream EoS
   */
  private byte headerTypeFlag;
  private long granulePosition;
  private final int bitstreamSerialNumber;
  private final int pageSequenceNumber;
  private final List<byte[]> segmentTable = new ArrayList<>();
  private final int segmentLimit;


  OggPage(final byte headerTypeFlag, final long granulePosition, final int bitstreamSerialNumber, final int pageSequenceNumber, final int segmentLimit) {
    this.headerTypeFlag = headerTypeFlag;
    this.granulePosition = granulePosition;
    this.bitstreamSerialNumber = bitstreamSerialNumber;
    this.pageSequenceNumber = pageSequenceNumber;
    this.segmentLimit = segmentLimit;
  }

  boolean isFull() {
    return this.segmentTable.size() >= this.segmentLimit;
  }

  void addDataSegment(final byte[] data) {
    this.segmentTable.add(data);
  }

  void addAudioSegment(final byte[] opusPacket) {
    this.granulePosition += 960;

    this.segmentTable.add(opusPacket);
  }

  long getGranulePosition() {
    return this.granulePosition;
  }

  void setEndOfStream() {
    this.headerTypeFlag |= 0x4;
  }

  byte[] toBytes() {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    bos.writeBytes(MAGIC_SIGNATURE);
    bos.write(VERSION);
    bos.write(this.headerTypeFlag);
    bos.writeBytes(long2Bytes(this.granulePosition));
    bos.writeBytes(int2Bytes(this.bitstreamSerialNumber));
    bos.writeBytes(int2Bytes(this.pageSequenceNumber));
    bos.writeBytes(new byte[4]);

    final ByteArrayOutputStream segmentSizes = new ByteArrayOutputStream();
    for(final byte[] segment : this.segmentTable) {
      segmentSizes.writeBytes(segmentLength2Bytes(segment.length));
    }
    bos.write(segmentSizes.size());
    bos.writeBytes(segmentSizes.toByteArray());

    for(final byte[] segment : this.segmentTable) {
      bos.writeBytes(segment);
    }

    final byte[] data = bos.toByteArray();
    MathHelper.set(data, 0x16, 0x4, getCrc(data));
    return data;
  }

  private static byte[] segmentLength2Bytes(final int length) {
    final byte[] output = new byte[(length / 255) + 1];
    if(output.length > 1) {
      Arrays.fill(output, 0, output.length - 1, (byte)0xff);
    }

    output[output.length - 1] = (byte)(length % 255);

    return output;
  }

  private static byte[] int2Bytes(final int value) {
    final byte[] output = new byte[4];

    output[0] = (byte)value;
    output[1] = (byte)(value >>> 8);
    output[2] = (byte)(value >>> 16);
    output[3] = (byte)(value >>> 24);

    return output;
  }

  private static byte[] long2Bytes(final long value) {
    final byte[] output = new byte[8];

    output[0] = (byte)value;
    output[1] = (byte)(value >>> 8);
    output[2] = (byte)(value >>> 16);
    output[3] = (byte)(value >>> 24);
    output[4] = (byte)(value >>> 32);
    output[5] = (byte)(value >>> 40);
    output[6] = (byte)(value >>> 48);
    output[7] = (byte)(value >>> 56);

    return output;
  }

  private static final int CRC_POLYNOMIAL = 0x4c11db7;
  private static final int[] CRC_TABLE = new int[256];
  static {
    int crc;
    for (int i = 0; i < 256; i++) {
      crc = i << 24;
      for (int j = 0; j < 8; j++) {
        if ((crc & 0x80000000) != 0) {
          crc = ((crc << 1) ^ CRC_POLYNOMIAL);
        } else {
          crc <<= 1;
        }
      }
      CRC_TABLE[i] = crc;
    }
  }

  private static int getCrc(final byte[] data) {
    int crc = 0;
    int x = 0;
    int y = 0;

    for(final byte b : data) {
      x = crc << 8;
      y = CRC_TABLE[(((crc >>> 24) & 0xff) ^ (b & 0xff))];
      crc = x ^ y;
    }

    return crc;
  }
}
