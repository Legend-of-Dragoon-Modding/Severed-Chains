package legend.core.audio.sequencer;

import static legend.core.audio.Constants.SAMPLE_RATE_RATIO;

final class Reverberizer {
  private static final Config[] configs = {
    new Config(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    new Config(125, 91, 28032, 21688, -16688, 0, 0, -17792, 22528, 21248, 1238, 819, 1008, 551, 884, 495, 820, 437, 0, 0, 0, 0, 0, 0, 0, 0, 436, 310, 184, 92, -32768, -32768),
    new Config(51, 37, 28912, 20392, -17184, 17424, -16144, -25600, 21120, 20160, 996, 795, 932, 687, 882, 614, 796, 605, 604, 398, 559, 309, 466, 183, 399, 181, 180, 128, 76, 38, -32768, -32768),
    new Config(177, 127, 28912, 20392, -17184, 17680, -16656, -19264, 21120, 20160, 2308, 1899, 2084, 1631, 1954, 1558, 1900, 1517, 1516, 1070, 1295, 773, 1122, 695, 1071, 613, 612, 434, 256, 128, -32768, -32768),
    new Config(227, 169, 28512, 20392, -17184, 17680, -16656, -22912, 22144, 21184, 3579, 2904, 3337, 2620, 3033, 2419, 2905, 2266, 2265, 1513, 2028, 1200, 1775, 978, 1514, 797, 796, 568, 340, 170, -32768, -32768),
    new Config(421, 313, 24576, 20480, 19456, -18432, -17408, -16384, 24576, 23552, 5562, 4539, 5314, 4285, 4540, 3521, 4544, 3523, 3520, 2497, 3012, 1985, 2560, 1741, 2498, 1473, 1472, 1050, 628, 314, -32768, -32768),
    new Config(829, 561, 32256, 20480, -19456, -20480, 19456, -20480, 24576, 21504, 7894, 6705, 7444, 6203, 7106, 5810, 6706, 5615, 5614, 4181, 4916, 3885, 4598, 3165, 4182, 2785, 2784, 1954, 1124, 562, -32768, -32768),
    new Config(1, 1, 32767, 32767, 0, 0, 0, -32512, 0, 0, 8191, 4095, 4101, 5, 0, 0, 4101, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4100, 4098, 4, 2, -32768, -32768),
    new Config(1, 1, 32767, 32767, 0, 0, 0, 0, 0, 0, 8191, 4095, 4101, 5, 0, 0, 4101, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4100, 4098, 4, 2, -32768, -32768),
    new Config(23, 19, 28912, 20392, -17184, 17680, -16656, -31488, 24448, 21696, 881, 687, 741, 479, 688, 471, 856, 618, 470, 286, 301, 177, 287, 89, 416, 227, 88, 64, 40, 20, -32768, -32768)
  };

  private Config config = configs[0];
  /** Configs 5, 7, 8 and 9 require larger work area, but they shouldn't be used and 0x8000 is much better at fitting into the L2 cache,
   *  compared to 0x2_0000 needed to support all reverbs
   */
  private final float[] reverbWorkArea = new float[0x8000];
  private int reverbCurrentAddress;

  private float outputLeft;
  private float outputRight;

  private float volumeLeft = 0x3000 / 32_768f;
  private float volumeRight = 0x3000 / 32_768f;

  void processReverb(final float lInput, final float rInput) {
    // Input from mixer
    final float Lin = this.config.vLIN * lInput;
    final float Rin = this.config.vRIN * rInput;

    // Same side reflection L->L and R->R
    final float mlSame = (Lin + this.loadReverb(this.config.dLSAME) * this.config.vWALL - this.loadReverb(this.config.mLSAME - 1)) * this.config.vIIR + this.loadReverb(this.config.mLSAME - 1);
    final float mrSame = (Rin + this.loadReverb(this.config.dRSAME) * this.config.vWALL - this.loadReverb(this.config.mRSAME - 1)) * this.config.vIIR + this.loadReverb(this.config.mRSAME - 1);

    this.writeReverb(this.config.mLSAME, mlSame);
    this.writeReverb(this.config.mRSAME, mrSame);

    // Different side reflection L->R and R->L
    final float mlDiff = (Lin + this.loadReverb(this.config.dRDIFF) * this.config.vWALL - this.loadReverb(this.config.mLDIFF - 1)) * this.config.vIIR + this.loadReverb(this.config.mLDIFF - 1);
    final float mrDiff = (Rin + this.loadReverb(this.config.dLDIFF) * this.config.vWALL - this.loadReverb(this.config.mRDIFF - 1)) * this.config.vIIR + this.loadReverb(this.config.mRDIFF - 1);

    this.writeReverb(this.config.mLDIFF, mlDiff);
    this.writeReverb(this.config.mRDIFF, mrDiff);

    // Early echo (comb filter with input from buffer)
    float l = this.config.vCOMB1 * this.loadReverb(this.config.mLCOMB1) + this.config.vCOMB2 * this.loadReverb(this.config.mLCOMB2) + this.config.vCOMB3 * this.loadReverb(this.config.mLCOMB3) + this.config.vCOMB4 * this.loadReverb(this.config.mLCOMB4);
    float r = this.config.vCOMB1 * this.loadReverb(this.config.mRCOMB1) + this.config.vCOMB2 * this.loadReverb(this.config.mRCOMB2) + this.config.vCOMB3 * this.loadReverb(this.config.mRCOMB3) + this.config.vCOMB4 * this.loadReverb(this.config.mRCOMB4);

    // Late reverb APF1 (All pass filter 1 with input from COMB)
    l = l - this.config.vAPF1 * this.loadReverb(this.config.mLAPF1 - this.config.dAPF1);
    r = r - this.config.vAPF1 * this.loadReverb(this.config.mRAPF1 - this.config.dAPF1);

    this.writeReverb(this.config.mLAPF1, l);
    this.writeReverb(this.config.mRAPF1, r);

    l = l * this.config.vAPF1 + this.loadReverb(this.config.mLAPF1 - this.config.dAPF1);
    r = r * this.config.vAPF1 + this.loadReverb(this.config.mRAPF1 - this.config.dAPF1);

    // Late reverb APF2 (All pass filter 2 with input from APF1)
    l = l - this.config.vAPF2 * this.loadReverb(this.config.mLAPF2 - this.config.dAPF2);
    r = r - this.config.vAPF2 * this.loadReverb(this.config.mRAPF2 - this.config.dAPF2);

    this.writeReverb(this.config.mLAPF2, l);
    this.writeReverb(this.config.mRAPF2, r);

    l = l * this.config.vAPF2 + this.loadReverb(this.config.mLAPF2 - this.config.dAPF2);
    r = r * this.config.vAPF2 + this.loadReverb(this.config.mRAPF2 - this.config.dAPF2);

    // Saturate address
    this.reverbCurrentAddress = this.reverbCurrentAddress + 1 & this.reverbWorkArea.length - 1;

    this.outputLeft = l * this.volumeLeft;
    this.outputRight = r * this.volumeRight;
  }

  private void writeReverb(final int addr, final float value) {
    final int address = this.reverbCurrentAddress + addr & this.reverbWorkArea.length - 1;
    this.reverbWorkArea[address] = value;
  }

  private float loadReverb(final int addr) {
    final int address = this.reverbCurrentAddress + addr & this.reverbWorkArea.length - 1;
    return this.reverbWorkArea[address];
  }

  void setConfig(final int config) {
    this.config = configs[config];
  }

  float getOutputLeft() {
    return this.outputLeft;
  }

  float getOutputRight() {
    return this.outputRight;
  }

  void setVolume(final float left, final float right) {
    this.volumeLeft = left;
    this.volumeRight = right;
  }

  private static class Config {
    private final int dAPF1;
    private final int dAPF2;
    private final float vIIR;
    private final float vCOMB1;
    private final float vCOMB2;
    private final float vCOMB3;
    private final float vCOMB4;
    private final float vWALL;
    private final float vAPF1;
    private final float vAPF2;
    private final int mLSAME;
    private final int mRSAME;
    private final int mLCOMB1;
    private final int mRCOMB1;
    private final int mLCOMB2;
    private final int mRCOMB2;
    private final int dLSAME;
    private final int dRSAME;
    private final int mLDIFF;
    private final int mRDIFF;
    private final int mLCOMB3;
    private final int mRCOMB3;
    private final int mLCOMB4;
    private final int mRCOMB4;
    private final int dLDIFF;
    private final int dRDIFF;
    private final int mLAPF1;
    private final int mRAPF1;
    private final int mLAPF2;
    private final int mRAPF2;
    private final float vLIN;
    private final float vRIN;

    public Config(final int dApf1, final int dApf2, final int vIir, final int vComb1, final int vComb2, final int vComb3, final int vComb4, final int vWall, final int vApf1, final int vApf2,
                  final int mLSame, final int mRSame, final int mLComb1, final int mRComb1, final int mLComb2, final int mRComb2, final int dLSame, final int dRSame, final int mLDiff, final int mRDiff,
                  final int mLComb3, final int mRComb3, final int mLComb4, final int mRComb4, final int dLDiff, final int dRDiff, final int mLApf1, final int mRApf1, final int mLApf2, final int mRApf2, final int vLIn, final int vRIn) {
      this.dAPF1 = convertInt(dApf1);
      this.dAPF2 = convertInt(dApf2);
      this.vIIR = convertFloat(vIir);
      this.vCOMB1 = convertFloat(vComb1);
      this.vCOMB2 = convertFloat(vComb2);
      this.vCOMB3 = convertFloat(vComb3);
      this.vCOMB4 = convertFloat(vComb4);
      this.vWALL = convertFloat(vWall);
      this.vAPF1 = convertFloat(vApf1);
      this.vAPF2 = convertFloat(vApf2);
      this.mLSAME = convertInt(mLSame);
      this.mRSAME = convertInt(mRSame);
      this.mLCOMB1 = convertInt(mLComb1);
      this.mRCOMB1 = convertInt(mRComb1);
      this.mLCOMB2 = convertInt(mLComb2);
      this.mRCOMB2 = convertInt(mRComb2);
      this.dLSAME = convertInt(dLSame);
      this.dRSAME = convertInt(dRSame);
      this.mLDIFF = convertInt(mLDiff);
      this.mRDIFF = convertInt(mRDiff);
      this.mLCOMB3 = convertInt(mLComb3);
      this.mRCOMB3 = convertInt(mRComb3);
      this.mLCOMB4 = convertInt(mLComb4);
      this.mRCOMB4 = convertInt(mRComb4);
      this.dLDIFF = convertInt(dLDiff);
      this.dRDIFF = convertInt(dRDiff);
      this.mLAPF1 = convertInt(mLApf1);
      this.mRAPF1 = convertInt(mRApf1);
      this.mLAPF2 = convertInt(mLApf2);
      this.mRAPF2 = convertInt(mRApf2);
      this.vLIN = convertFloat(vLIn);
      this.vRIN = convertFloat(vRIn);
    }

    private static int convertInt(final int value) {
      return (int)Math.round((value << 3) / SAMPLE_RATE_RATIO);
    }

    private static float convertFloat(final int value) {
      return value / (float)0x8000;
    }
  }
}
