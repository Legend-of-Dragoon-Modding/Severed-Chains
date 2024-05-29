package legend.core.audio.sequencer;

import legend.core.MathHelper;
import legend.core.spu.Reverb;
import legend.game.sound.ReverbConfig;

import java.util.Arrays;

final class Reverberizer {
  private final Reverb reverb = new Reverb();
  private final float[] reverbWorkArea = new float[0x2_0000];
  private int reverbCurrentAddress;

  private float outputLeft;
  private float outputRight;

  void processReverb(final float lInput, final float rInput) {
    final int dAPF1 = this.reverb.dAPF1;
    final int dAPF2 = this.reverb.dAPF2;
    final float vIIR = this.reverb.vIIR;
    final float vCOMB1 = this.reverb.vCOMB1;
    final float vCOMB2 = this.reverb.vCOMB2;
    final float vCOMB3 = this.reverb.vCOMB3;
    final float vCOMB4 = this.reverb.vCOMB4;
    final float vWALL = this.reverb.vWALL;
    final float vAPF1 = this.reverb.vAPF1;
    final float vAPF2 = this.reverb.vAPF2;
    final int mLSAME = this.reverb.mLSAME;
    final int mRSAME = this.reverb.mRSAME;
    final int mLCOMB1 = this.reverb.mLCOMB1;
    final int mRCOMB1 = this.reverb.mRCOMB1;
    final int mLCOMB2 = this.reverb.mLCOMB2;
    final int mRCOMB2 = this.reverb.mRCOMB2;
    final int dLSAME = this.reverb.dLSAME;
    final int dRSAME = this.reverb.dRSAME;
    final int mLDIFF = this.reverb.mLDIFF;
    final int mRDIFF = this.reverb.mRDIFF;
    final int mLCOMB3 = this.reverb.mLCOMB3;
    final int mRCOMB3 = this.reverb.mRCOMB3;
    final int mLCOMB4 = this.reverb.mLCOMB4;
    final int mRCOMB4 = this.reverb.mRCOMB4;
    final int dLDIFF = this.reverb.dLDIFF;
    final int dRDIFF = this.reverb.dRDIFF;
    final int mLAPF1 = this.reverb.mLAPF1;
    final int mRAPF1 = this.reverb.mRAPF1;
    final int mLAPF2 = this.reverb.mLAPF2;
    final int mRAPF2 = this.reverb.mRAPF2;
    final float vLIN = this.reverb.vLIN;
    final float vRIN = this.reverb.vRIN;

    // Input from mixer
    final float Lin = vLIN * lInput;
    final float Rin = vRIN * rInput;

    // Same side reflection L->L and R->R
    final float mlSame = this.saturateSample((Lin + this.loadReverb(dLSAME) * vWALL - this.loadReverb(mLSAME - 1)) * vIIR + this.loadReverb(mLSAME - 1));
    final float mrSame = this.saturateSample((Rin + this.loadReverb(dRSAME) * vWALL - this.loadReverb(mRSAME - 1)) * vIIR + this.loadReverb(mRSAME - 1));

    this.writeReverb(mLSAME, mlSame);
    this.writeReverb(mRSAME, mrSame);

    // Different side reflection L->R and R->L
    final float mlDiff = this.saturateSample((Lin + this.loadReverb(dRDIFF) * vWALL - this.loadReverb(mLDIFF - 1)) * vIIR + this.loadReverb(mLDIFF - 1));
    final float mrDiff = this.saturateSample((Rin + this.loadReverb(dLDIFF) * vWALL - this.loadReverb(mRDIFF - 1)) * vIIR + this.loadReverb(mRDIFF - 1));

    this.writeReverb(mLDIFF, mlDiff);
    this.writeReverb(mRDIFF, mrDiff);

    // Early echo (comb filter with input from buffer)
    float l = this.saturateSample(vCOMB1 * this.loadReverb(mLCOMB1) + vCOMB2 * this.loadReverb(mLCOMB2) + vCOMB3 * this.loadReverb(mLCOMB3) + vCOMB4 * this.loadReverb(mLCOMB4));
    float r = this.saturateSample(vCOMB1 * this.loadReverb(mRCOMB1) + vCOMB2 * this.loadReverb(mRCOMB2) + vCOMB3 * this.loadReverb(mRCOMB3) + vCOMB4 * this.loadReverb(mRCOMB4));

    // Late reverb APF1 (All pass filter 1 with input from COMB)
    l = this.saturateSample(l - this.saturateSample(vAPF1 * this.loadReverb(mLAPF1 - dAPF1)));
    r = this.saturateSample(r - this.saturateSample(vAPF1 * this.loadReverb(mRAPF1 - dAPF1)));

    this.writeReverb(mLAPF1, l);
    this.writeReverb(mRAPF1, r);

    l = this.saturateSample(l * vAPF1 + this.loadReverb(mLAPF1 - dAPF1));
    r = this.saturateSample(r * vAPF1 + this.loadReverb(mRAPF1 - dAPF1));

    // Late reverb APF2 (All pass filter 2 with input from APF1)
    l = this.saturateSample(l - this.saturateSample(vAPF2 * this.loadReverb(mLAPF2 - dAPF2)));
    r = this.saturateSample(r - this.saturateSample(vAPF2 * this.loadReverb(mRAPF2 - dAPF2)));

    this.writeReverb(mLAPF2, l);
    this.writeReverb(mRAPF2, r);

    l = this.saturateSample(l * vAPF2 + this.loadReverb(mLAPF2 - dAPF2));
    r = this.saturateSample(r * vAPF2 + this.loadReverb(mRAPF2 - dAPF2));

    // Saturate address
    this.reverbCurrentAddress = this.reverbCurrentAddress + 1 & this.reverbWorkArea.length - 1;

    this.outputLeft = l;
    this.outputRight = r;
  }

  private float saturateSample(final float sample) {
    return MathHelper.clamp(sample, -1.0f, 1.0f);
  }

  private void writeReverb(final int addr, final float value) {
    final int address = this.reverbCurrentAddress + addr & this.reverbWorkArea.length - 1;
    this.reverbWorkArea[address] = value;
  }

  private float loadReverb(final int addr) {
    final int address = this.reverbCurrentAddress + addr & this.reverbWorkArea.length - 1;
    return this.reverbWorkArea[address];
  }

  void setConfig(final ReverbConfig config) {
    this.reverb.set(config);
  }

  float getOutputLeft() {
    return this.outputLeft;
  }

  float getOutputRight() {
    return this.outputRight;
  }

  void clear() {
    Arrays.fill(this.reverbWorkArea, 0);
  }
}
