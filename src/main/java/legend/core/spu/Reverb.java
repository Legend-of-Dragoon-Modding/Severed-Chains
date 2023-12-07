package legend.core.spu;

import legend.game.sound.ReverbConfig;

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
    this.dAPF1 = config.dApf1 << 2;
    this.dAPF2 = config.dApf2 << 2;
    this.vIIR = config.vIir / 32768.0f;
    this.vCOMB1 = config.vComb1 / 32768.0f;
    this.vCOMB2 = config.vComb2 / 32768.0f;
    this.vCOMB3 = config.vComb3 / 32768.0f;
    this.vCOMB4 = config.vComb4 / 32768.0f;
    this.vWALL = config.vWall / 32768.0f;
    this.vAPF1 = config.vApf1 / 32768.0f;
    this.vAPF2 = config.vApf2 / 32768.0f;
    this.mLSAME = config.mLSame << 2;
    this.mRSAME = config.mRSame << 2;
    this.mLCOMB1 = config.mLComb1 << 2;
    this.mRCOMB1 = config.mRComb1 << 2;
    this.mLCOMB2 = config.mLComb2 << 2;
    this.mRCOMB2 = config.mRComb2 << 2;
    this.dLSAME = config.dLSame << 2;
    this.dRSAME = config.dRSame << 2;
    this.mLDIFF = config.mLDiff << 2;
    this.mRDIFF = config.mRDiff << 2;
    this.mLCOMB3 = config.mLComb3 << 2;
    this.mRCOMB3 = config.mRComb3 << 2;
    this.mLCOMB4 = config.mLComb4 << 2;
    this.mRCOMB4 = config.mRComb4 << 2;
    this.dLDIFF = config.dLDiff << 2;
    this.dRDIFF = config.dRDiff << 2;
    this.mLAPF1 = config.mLApf1 << 2;
    this.mRAPF1 = config.mRApf1 << 2;
    this.mLAPF2 = config.mLApf2 << 2;
    this.mRAPF2 = config.mRApf2 << 2;
    this.vLIN = config.vLIn / 32768.0f;
    this.vRIN = config.vRIn / 32768.0f;
  }
}
