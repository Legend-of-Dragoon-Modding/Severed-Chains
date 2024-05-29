package legend.core.spu;

import legend.game.sound.ReverbConfig;

import static legend.core.audio.AudioThread.SAMPLE_RATE_MULTIPLIER;

public class Reverb {
  public int dAPF1;
  public int dAPF2;
  public float vIIR;
  public float vCOMB1;
  public float vCOMB2;
  public float vCOMB3;
  public float vCOMB4;
  public float vWALL;
  public float vAPF1;
  public float vAPF2;
  public int mLSAME;
  public int mRSAME;
  public int mLCOMB1;
  public int mRCOMB1;
  public int mLCOMB2;
  public int mRCOMB2;
  public int dLSAME;
  public int dRSAME;
  public int mLDIFF;
  public int mRDIFF;
  public int mLCOMB3;
  public int mRCOMB3;
  public int mLCOMB4;
  public int mRCOMB4;
  public int dLDIFF;
  public int dRDIFF;
  public int mLAPF1;
  public int mRAPF1;
  public int mLAPF2;
  public int mRAPF2;
  public float vLIN;
  public float vRIN;

  public void set(final ReverbConfig config) {
    this.dAPF1 = (int)Math.round((config.dApf1 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.dAPF2 = (int)Math.round((config.dApf2 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.vIIR = config.vIir / 32768.0f;
    this.vCOMB1 = config.vComb1 / 32768.0f;
    this.vCOMB2 = config.vComb2 / 32768.0f;
    this.vCOMB3 = config.vComb3 / 32768.0f;
    this.vCOMB4 = config.vComb4 / 32768.0f;
    this.vWALL = config.vWall / 32768.0f;
    this.vAPF1 = config.vApf1 / 32768.0f;
    this.vAPF2 = config.vApf2 / 32768.0f;
    this.mLSAME = (int)Math.round((config.mLSame << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRSAME = (int)Math.round((config.mRSame << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLCOMB1 = (int)Math.round((config.mLComb1 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRCOMB1 = (int)Math.round((config.mRComb1 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLCOMB2 = (int)Math.round((config.mLComb2 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRCOMB2 = (int)Math.round((config.mRComb2 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.dLSAME = (int)Math.round((config.dLSame << 3) * SAMPLE_RATE_MULTIPLIER);
    this.dRSAME = (int)Math.round((config.dRSame << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLDIFF = (int)Math.round((config.mLDiff << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRDIFF = (int)Math.round((config.mRDiff << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLCOMB3 = (int)Math.round((config.mLComb3 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRCOMB3 = (int)Math.round((config.mRComb3 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLCOMB4 = (int)Math.round((config.mLComb4 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRCOMB4 = (int)Math.round((config.mRComb4 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.dLDIFF = (int)Math.round((config.dLDiff << 3) * SAMPLE_RATE_MULTIPLIER);
    this.dRDIFF = (int)Math.round((config.dRDiff << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLAPF1 = (int)Math.round((config.mLApf1 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRAPF1 = (int)Math.round((config.mRApf1 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mLAPF2 = (int)Math.round((config.mLApf2 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.mRAPF2 = (int)Math.round((config.mRApf2 << 3) * SAMPLE_RATE_MULTIPLIER);
    this.vLIN = config.vLIn / 32768.0f;
    this.vRIN = config.vRIn / 32768.0f;
  }
}
