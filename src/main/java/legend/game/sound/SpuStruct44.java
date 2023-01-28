package legend.game.sound;

public class SpuStruct44 {
  /** ubyte */
  public int _00;
  /** ubyte */
  public int channelIndex_01;

  /** ubyte */
  public int _03;
  /** ubyte */
  public int _04;

  public Sshd sshdPtr_08;
  /** ubyte */
  public boolean _0c;
  /** ubyte */
  public int _0d;

  /** May be -1 (short) */
  public int voiceIndex_10;
  /** Was two ushorts */
  public int reverbMode_12;
  /** Was two ushorts */
  public int noiseMode_16;

  /** No longer used */
  public int eventSpuIrq_1c;
  /** No longer used */
  public boolean spuDmaTransferInProgress_20;
  /** ubyte */
  public boolean pitchShifted_22;
  /** ubyte */
  public boolean reverbEnabled_23;
  /** short */
  public int pitch_24;
  /** short */
  public int pitchShiftVolLeft_26;
  /** short */
  public int pitchShiftVolRight_28;
  /** ubyte */
  public boolean fadingIn_2a;
  /** ubyte */
  public boolean fadingOut_2b;
  /** ushort */
  public int fadeTime_2c;
  /** ushort */
  public int fadeInVol_2e;
  /** ushort */
  public int fadeOutVolL_30;
  /** ushort */
  public int fadeOutVolR_32;
  public int reverbType_34;
  /** ushort */
  public boolean mono_36;
  /** ubyte */
  public boolean hasCallback_38;
  /** No longer used */
  public int dmaIndex_39;
  /** Was two ushorts */
  public int keyOn_3a;
  /** Was two ushorts */
  public int keyOff_3e;
  /** I think this is ticks per second (ushort) */
  public int ticksPerSecond_42;
}
