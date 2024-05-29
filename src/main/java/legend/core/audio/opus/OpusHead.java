package legend.core.audio.opus;


/**
 * Opus Head as defined by <a href="https://datatracker.ietf.org/doc/html/rfc7845#section-5.1">RFC7845</a>
 * <table>
 *  <tr><th></th><th style="text-align: center">0</th><th></th><th style="text-align: center">1</th><th></th><th style="text-align: center">2</th><th></th><th style="text-align: center">3</th></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">O</td><td style="text-align: center">|</td><td style="text-align: center">p</td><td style="text-align: center">|</td><td style="text-align: center">u</td><td style="text-align: center">|</td><td style="text-align: center">s</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">H</td><td style="text-align: center">|</td><td style="text-align: center">e</td><td style="text-align: center">|</td><td style="text-align: center">a</td><td style="text-align: center">|</td><td style="text-align: center">d</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center">{@link OpusHead#VERSION}</td><td style="text-align: center">|</td><td style="text-align: center">{@link OpusHead#channelCount}</td><td style="text-align: center">|</td><td style="text-align: center" colspan=3>{@link OpusHead#preSkip}</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>{@link OpusHead#originalSampleRate}</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=3>{@link OpusHead#outputGain} (Q7.8 in dB)</td><td style="text-align: center">|</td><td style="text-align: center">{@link OpusHead#MAPPING_FAMILY}</td><td style="text-align: center">|</td><td></td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td></td><td style="text-align: center">+</td></tr>
 *  <tr><td style="text-align: center">|</td><td style="text-align: center" colspan=7>Optional Channel Mapping Table</td><td style="text-align: center">|</td></tr>
 *  <tr><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td><td style="text-align: center">---------------------</td><td style="text-align: center">+</td></tr>
 * </table>
 */
final class OpusHead {
  private static final byte[] MAGIC_SIGNATURE = {'O', 'p', 'u', 's', 'H', 'e', 'a', 'd'};
  private static final int VERSION = 1;
  private final int channelCount;
  private final short preSkip;
  private final int originalSampleRate;
  private final int outputGain;
  private static final byte MAPPING_FAMILY = 0;

  OpusHead(final byte channelCount, final short preSkip, final int originalSampleRate, final int outputGain) {
   this.channelCount = channelCount;
   this.preSkip = preSkip;
   this.originalSampleRate = originalSampleRate;
   this.outputGain = outputGain;
  }

  byte[] toBytes() {
    final byte[] output = new byte[0x13];

    System.arraycopy(MAGIC_SIGNATURE, 0, output, 0, MAGIC_SIGNATURE.length);
    output[0x8] = VERSION;
    output[0x9] = (byte)this.channelCount;
    output[0xa] = (byte)this.preSkip;
    output[0xb] = (byte)(this.preSkip >>> 8);
    output[0xc] = (byte)this.originalSampleRate;
    output[0xd] = (byte)(this.originalSampleRate >>> 8);
    output[0xe] = (byte)(this.originalSampleRate >>> 16);
    output[0xf] = (byte)(this.originalSampleRate >>> 24);
    output[0x10] = (byte)this.outputGain;
    output[0x11] = (byte)(this.outputGain >>> 8);
    output[0x12] = MAPPING_FAMILY;
    return output;
  }
}
