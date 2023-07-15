package legend.core.gte;

import legend.core.MathHelper;

public class Gte {
  private static final byte[] unrTable = {
    (byte)0xFF, (byte)0xFD, (byte)0xFB, (byte)0xF9, (byte)0xF7, (byte)0xF5, (byte)0xF3, (byte)0xF1, (byte)0xEF, (byte)0xEE, (byte)0xEC, (byte)0xEA, (byte)0xE8, (byte)0xE6, (byte)0xE4, (byte)0xE3,
    (byte)0xE1, (byte)0xDF, (byte)0xDD, (byte)0xDC, (byte)0xDA, (byte)0xD8, (byte)0xD6, (byte)0xD5, (byte)0xD3, (byte)0xD1, (byte)0xD0, (byte)0xCE, (byte)0xCD, (byte)0xCB, (byte)0xC9, (byte)0xC8,
    (byte)0xC6, (byte)0xC5, (byte)0xC3, (byte)0xC1, (byte)0xC0, (byte)0xBE, (byte)0xBD, (byte)0xBB, (byte)0xBA, (byte)0xB8, (byte)0xB7, (byte)0xB5, (byte)0xB4, (byte)0xB2, (byte)0xB1, (byte)0xB0,
    (byte)0xAE, (byte)0xAD, (byte)0xAB, (byte)0xAA, (byte)0xA9, (byte)0xA7, (byte)0xA6, (byte)0xA4, (byte)0xA3, (byte)0xA2, (byte)0xA0, (byte)0x9F, (byte)0x9E, (byte)0x9C, (byte)0x9B, (byte)0x9A,
    (byte)0x99, (byte)0x97, (byte)0x96, (byte)0x95, (byte)0x94, (byte)0x92, (byte)0x91, (byte)0x90, (byte)0x8F, (byte)0x8D, (byte)0x8C, (byte)0x8B, (byte)0x8A, (byte)0x89, (byte)0x87, (byte)0x86,
    (byte)0x85, (byte)0x84, (byte)0x83, (byte)0x82, (byte)0x81, (byte)0x7F, (byte)0x7E, (byte)0x7D, (byte)0x7C, (byte)0x7B, (byte)0x7A, (byte)0x79, (byte)0x78, (byte)0x77, (byte)0x75, (byte)0x74,
    (byte)0x73, (byte)0x72, (byte)0x71, (byte)0x70, (byte)0x6F, (byte)0x6E, (byte)0x6D, (byte)0x6C, (byte)0x6B, (byte)0x6A, (byte)0x69, (byte)0x68, (byte)0x67, (byte)0x66, (byte)0x65, (byte)0x64,
    (byte)0x63, (byte)0x62, (byte)0x61, (byte)0x60, (byte)0x5F, (byte)0x5E, (byte)0x5D, (byte)0x5D, (byte)0x5C, (byte)0x5B, (byte)0x5A, (byte)0x59, (byte)0x58, (byte)0x57, (byte)0x56, (byte)0x55,
    (byte)0x54, (byte)0x53, (byte)0x53, (byte)0x52, (byte)0x51, (byte)0x50, (byte)0x4F, (byte)0x4E, (byte)0x4D, (byte)0x4D, (byte)0x4C, (byte)0x4B, (byte)0x4A, (byte)0x49, (byte)0x48, (byte)0x48,
    (byte)0x47, (byte)0x46, (byte)0x45, (byte)0x44, (byte)0x43, (byte)0x43, (byte)0x42, (byte)0x41, (byte)0x40, (byte)0x3F, (byte)0x3F, (byte)0x3E, (byte)0x3D, (byte)0x3C, (byte)0x3C, (byte)0x3B,
    (byte)0x3A, (byte)0x39, (byte)0x39, (byte)0x38, (byte)0x37, (byte)0x36, (byte)0x36, (byte)0x35, (byte)0x34, (byte)0x33, (byte)0x33, (byte)0x32, (byte)0x31, (byte)0x31, (byte)0x30, (byte)0x2F,
    (byte)0x2E, (byte)0x2E, (byte)0x2D, (byte)0x2C, (byte)0x2C, (byte)0x2B, (byte)0x2A, (byte)0x2A, (byte)0x29, (byte)0x28, (byte)0x28, (byte)0x27, (byte)0x26, (byte)0x26, (byte)0x25, (byte)0x24,
    (byte)0x24, (byte)0x23, (byte)0x22, (byte)0x22, (byte)0x21, (byte)0x20, (byte)0x20, (byte)0x1F, (byte)0x1E, (byte)0x1E, (byte)0x1D, (byte)0x1D, (byte)0x1C, (byte)0x1B, (byte)0x1B, (byte)0x1A,
    (byte)0x19, (byte)0x19, (byte)0x18, (byte)0x18, (byte)0x17, (byte)0x16, (byte)0x16, (byte)0x15, (byte)0x15, (byte)0x14, (byte)0x14, (byte)0x13, (byte)0x12, (byte)0x12, (byte)0x11, (byte)0x11,
    (byte)0x10, (byte)0x0F, (byte)0x0F, (byte)0x0E, (byte)0x0E, (byte)0x0D, (byte)0x0D, (byte)0x0C, (byte)0x0C, (byte)0x0B, (byte)0x0A, (byte)0x0A, (byte)0x09, (byte)0x09, (byte)0x08, (byte)0x08,
    (byte)0x07, (byte)0x07, (byte)0x06, (byte)0x06, (byte)0x05, (byte)0x05, (byte)0x04, (byte)0x04, (byte)0x03, (byte)0x03, (byte)0x02, (byte)0x02, (byte)0x01, (byte)0x01, (byte)0x00, (byte)0x00,
    (byte)0x00,
  };

  private static final class Matrix {
    public short v00;
    public short v01;
    public short v02;
    public short v10;
    public short v11;
    public short v12;
    public short v20;
    public short v21;
    public short v22;

    private Matrix() { }

    private Matrix(final short v00, final short v01, final short v02, final short v10, final short v11, final short v12, final short v20, final short v21, final short v22) {
      this.v00 = v00;
      this.v01 = v01;
      this.v02 = v02;
      this.v10 = v10;
      this.v11 = v11;
      this.v12 = v12;
      this.v20 = v20;
      this.v21 = v21;
      this.v22 = v22;
    }

    public Matrix copy() {
      return new Matrix(this.v00, this.v01, this.v02, this.v10, this.v11, this.v12, this.v20, this.v21, this.v22);
    }
  }

  private static class Vector2 {
    public short x;
    public short y;

    public void set(final Vector2 other) {
      this.x = other.x;
      this.y = other.y;
    }
  }

  private static class Color {
    public byte r;
    public byte g;
    public byte b;
    public byte c;

    public int val() {
      return (this.c & 0xff) << 24 | (this.b & 0xff) << 16 | (this.g & 0xff) << 8 | this.r & 0xff;
    }

    public void val(final int val) {
      this.r = (byte)(val        & 0xff);
      this.g = (byte)(val >>>  8 & 0xff);
      this.b = (byte)(val >>> 16 & 0xff);
      this.c = (byte)(val >>> 24 & 0xff);
    }

    public void set(final Color other) {
      this.r = other.r;
      this.g = other.g;
      this.b = other.b;
      this.c = other.c;
    }
  }

  //Data Registers
  private final ShortVector3[] V = {new ShortVector3(), new ShortVector3(), new ShortVector3()};   //R0-1 R2-3 R4-5 s16
  private final Color RGBC = new Color();                     //R6
  private short OTZ;                     //R7
  private final short[] IR = new short[4];      //R8-11
  private final Vector2[] SXY = {new Vector2(), new Vector2(), new Vector2(), new Vector2()}; //R12-15 FIFO
  private final short[] SZ = new short[4];    //R16-19 FIFO
  private final Color[] RGB = {new Color(), new Color(), new Color()};     //R20-22 FIFO
  private int MAC0;                       //R24
  private int MAC1, MAC2, MAC3;           //R25-27

  //Control Registers
  private final Matrix RT = new Matrix();        //R32-36 R40-44 R48-52
  private final Matrix LM = new Matrix();
  private final Matrix LRGB = new Matrix();
  private int TRX, TRY, TRZ;          //R37-39
  private int RBK, GBK, BBK;          //R45-47
  private int RFC, GFC, BFC;          //R53-55
  private int OFX, OFY, DQB;          //R56 57 60
  private short H;                   //R58
  private short ZSF3, ZSF4, DQA;      //R61 62 59
  private long FLAG;                  //R63

  //Command decode
  private int sf;                     //Shift fraction (0 or 12)
  private boolean lm;                    //Saturate IR1,IR2,IR3 result (0=To -8000h..+7FFFh, 1=To 0..+7FFFh)
  private int currentCommand;        //GTE current command temporary stored for MVMVA decoding

  private void NCCS(final int r) {
    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (LLM * V0) SAR(sf * 12)
    this.MAC1 = (int)(this.setMAC(1, (long)this.LM.v00 * this.V[r].x + this.LM.v01 * this.V[r].y + this.LM.v02 * this.V[r].z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, (long)this.LM.v10 * this.V[r].x + this.LM.v11 * this.V[r].y + this.LM.v12 * this.V[r].z) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, (long)this.LM.v20 * this.V[r].x + this.LM.v21 * this.V[r].y + this.LM.v22 * this.V[r].z) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (BK * 1000h + LCM * IR) SAR(sf * 12)
    // WARNING each multiplication can trigger mac flags so the check is needed on each op! Somehow this only affects the color matrix and not the light one
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.RBK * 0x1000 + this.LRGB.v00 * this.IR[1]) + (long)this.LRGB.v01 * this.IR[2]) + (long)this.LRGB.v02 * this.IR[3]) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.GBK * 0x1000 + this.LRGB.v10 * this.IR[1]) + (long)this.LRGB.v11 * this.IR[2]) + (long)this.LRGB.v12 * this.IR[3]) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.BBK * 0x1000 + this.LRGB.v20 * this.IR[1]) + (long)this.LRGB.v21 * this.IR[2]) + (long)this.LRGB.v22 * this.IR[3]) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [MAC1, MAC2, MAC3] = [R * IR1, G * IR2, B * IR3] SHL 4;< --- for NCDx / NCCx
    this.MAC1 = (int)this.setMAC(1, (this.RGBC.r & 0xff) * this.IR[1] << 4);
    this.MAC2 = (int)this.setMAC(2, (this.RGBC.g & 0xff) * this.IR[2] << 4);
    this.MAC3 = (int)this.setMAC(3, (this.RGBC.b & 0xff) * this.IR[3] << 4);

    // [MAC1, MAC2, MAC3] = [MAC1, MAC2, MAC3] SAR(sf * 12);< --- for NCDx / NCCx
    this.MAC1 = (int)this.setMAC(1, this.MAC1 >> this.sf);
    this.MAC2 = (int)this.setMAC(2, this.MAC2 >> this.sf);
    this.MAC3 = (int)this.setMAC(3, this.MAC3 >> this.sf);

    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE], [IR1, IR2, IR3] = [MAC1, MAC2, MAC3]
    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);
  }

  private void MVMVA() { //WIP
    //Mx = matrix specified by mx; RT / LLM / LCM - Rotation, light or color matrix
    //Vx = vector specified by v; V0, V1, V2, or[IR1, IR2, IR3]
    //Tx = translation vector specified by cv; TR or BK or Bugged / FC, or None

    final int mxIndex = this.currentCommand >>> 17 & 0x3; //MVMVA Multiply Matrix    (0=Rotation. 1=Light, 2=Color, 3=Reserved)
    final int mvIndex = this.currentCommand >>> 15 & 0x3; //MVMVA Multiply Vector    (0=V0, 1=V1, 2=V2, 3=IR/long)
    final int tvIndex = this.currentCommand >>> 13 & 0x3; //MVMVA Translation Vector (0=TR, 1=BK, 2=FC/Bugged, 3=None)

    final Matrix mx;
    final ShortVector3 vx;
    final long tx;
    final long ty;
    final long tz;

    if(mxIndex == 0) {
      mx = this.RT.copy();
    } else if(mxIndex == 1) {
      mx = this.LM.copy();
    } else if(mxIndex == 2) {
      mx = this.LRGB.copy();
    } else {
      mx = new Matrix();
      mx.v00 = (short)-((this.RGBC.r & 0xff) << 4);
      mx.v01 = (short)((this.RGBC.r & 0xff) << 4);
      mx.v02 = this.IR[0];
      mx.v10 = mx.v11 = mx.v12 = this.RT.v02;
      mx.v20 = mx.v21 = mx.v22 = this.RT.v11;
    }

    if(mvIndex == 0) {
      vx = this.V[0].copy();
    } else if(mvIndex == 1) {
      vx = this.V[1].copy();
    } else if(mvIndex == 2) {
      vx = this.V[2].copy();
    } else {
      vx = new ShortVector3(this.IR[1], this.IR[2], this.IR[3]);
    }

    if(tvIndex == 0) {
      tx = this.TRX;
      ty = this.TRY;
      tz = this.TRZ;
    } else if(tvIndex == 1) {
      tx = this.RBK;
      ty = this.GBK;
      tz = this.BBK;
    } else if(tvIndex == 2) {
      //This vector is not added correctly by the hardware
      tx = this.RFC;
      ty = this.GFC;
      tz = this.BFC;

      long mac1 = this.setMAC(1, tx * 0x1000 + mx.v00 * vx.x);
      long mac2 = this.setMAC(2, ty * 0x1000 + mx.v10 * vx.x);
      long mac3 = this.setMAC(3, tz * 0x1000 + mx.v20 * vx.x);

      this.setIR(1, (int)(mac1 >> this.sf), false);
      this.setIR(2, (int)(mac2 >> this.sf), false);
      this.setIR(3, (int)(mac3 >> this.sf), false);

      mac1 = this.setMAC(1, this.setMAC(1, (long)mx.v01 * vx.y) + (long)mx.v02 * vx.z);
      mac2 = this.setMAC(2, this.setMAC(2, (long)mx.v11 * vx.y) + (long)mx.v12 * vx.z);
      mac3 = this.setMAC(3, this.setMAC(3, (long)mx.v21 * vx.y) + (long)mx.v22 * vx.z);

      this.MAC1 = (int)(mac1 >> this.sf);
      this.MAC2 = (int)(mac2 >> this.sf);
      this.MAC3 = (int)(mac3 >> this.sf);

      this.IR[1] = this.setIR(1, this.MAC1, this.lm);
      this.IR[2] = this.setIR(2, this.MAC2, this.lm);
      this.IR[3] = this.setIR(3, this.MAC3, this.lm);

      return;
    } else {
      tx = ty = tz = 0;
    }

    //MAC1 = (Tx1 * 1000h + Mx11 * Vx1 + Mx12 * Vx2 + Mx13 * Vx3) SAR(sf * 12)
    //MAC2 = (Tx2 * 1000h + Mx21 * Vx1 + Mx22 * Vx2 + Mx23 * Vx3) SAR(sf * 12)
    //MAC3 = (Tx3 * 1000h + Mx31 * Vx1 + Mx32 * Vx2 + Mx33 * Vx3) SAR(sf * 12)
    //[IR1, IR2, IR3] = [MAC1, MAC2, MAC3]

    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, tx * 0x1000 + mx.v00 * vx.x) + (long)mx.v01 * vx.y) + (long)mx.v02 * vx.z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, ty * 0x1000 + mx.v10 * vx.x) + (long)mx.v11 * vx.y) + (long)mx.v12 * vx.z) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, tz * 0x1000 + mx.v20 * vx.x) + (long)mx.v21 * vx.y) + (long)mx.v22 * vx.z) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);
  }

  private void GPF() {
    //[MAC1, MAC2, MAC3] = [0,0,0]                            ;<--- for GPF only
    //[MAC1, MAC2, MAC3] = (([IR1, IR2, IR3] * IR0) + [MAC1, MAC2, MAC3]) SAR(sf*12)
    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE], [IR1, IR2, IR3] = [MAC1, MAC2, MAC3]

    this.MAC1 = (int)this.setMAC(1, this.IR[1] * this.IR[0]) >> this.sf;
    this.MAC2 = (int)this.setMAC(2, this.IR[2] * this.IR[0]) >> this.sf;
    this.MAC3 = (int)this.setMAC(3, this.IR[3] * this.IR[0]) >> this.sf;

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;
  }

  private void OP() {
    //[MAC1, MAC2, MAC3] = [IR3*D2-IR2*D3, IR1*D3-IR3*D1, IR2*D1-IR1*D2] SAR(sf*12)
    //[IR1, IR2, IR3]    = [MAC1, MAC2, MAC3]                        ;copy result
    //Calculates the outer product of two signed 16bit vectors.
    //Note: D1,D2,D3 are meant to be the RT11,RT22,RT33 elements of the RT matrix "misused" as vector. lm should be usually zero.

    final short d1 = this.RT.v00;
    final short d2 = this.RT.v11;
    final short d3 = this.RT.v22;

    this.MAC1 = (int)this.setMAC(1, this.IR[3] * d2 - this.IR[2] * d3 >> this.sf);
    this.MAC2 = (int)this.setMAC(2, this.IR[1] * d3 - this.IR[3] * d1 >> this.sf);
    this.MAC3 = (int)this.setMAC(3, this.IR[2] * d1 - this.IR[1] * d2 >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);
  }

  private void SQR() {
    this.MAC1 = (int)this.setMAC(1, this.IR[1] * this.IR[1] >> this.sf);
    this.MAC2 = (int)this.setMAC(2, this.IR[2] * this.IR[2] >> this.sf);
    this.MAC3 = (int)this.setMAC(3, this.IR[3] * this.IR[3] >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);
  }

  private void AVSZ3() {
    //MAC0 = ZSF3 * (SZ1 + SZ2 + SZ3); for AVSZ3
    //OTZ = MAC0 / 1000h;for both(saturated to 0..FFFFh)
    final long avsz3 = (long)this.ZSF3 * ((this.SZ[1] & 0xffff) + (this.SZ[2] & 0xffff) + (this.SZ[3] & 0xffff));
    this.MAC0 = (int)this.setMAC0(avsz3);
    this.OTZ = this.setSZ3(avsz3 >> 12);
  }

  private void AVSZ4() {
    //MAC0 = ZSF4 * (SZ0 + SZ1 + SZ2 + SZ3);for AVSZ4
    //OTZ = MAC0 / 1000h;for both(saturated to 0..FFFFh)
    final long avsz4 = (long)this.ZSF4 * ((this.SZ[0] & 0xffff) + (this.SZ[1] & 0xffff) + (this.SZ[2] & 0xffff) + (this.SZ[3] & 0xffff));
    this.MAC0 = (int)this.setMAC0(avsz4);
    this.OTZ = this.setSZ3(avsz4 >> 12);
  }

  private void NCLIP() { //Normal clipping
    // MAC0 = SX0*SY1 + SX1*SY2 + SX2*SY0 - SX0*SY2 - SX1*SY0 - SX2*SY1
    this.MAC0 = (int)this.setMAC0((long)this.SXY[0].x * (this.SXY[1].y - this.SXY[2].y) + this.SXY[1].x * (this.SXY[2].y - this.SXY[0].y) + this.SXY[2].x * (this.SXY[0].y - this.SXY[1].y));
  }

  private void RTPT() { //Perspective Transformation Triple
    this.RTPS(0, false);
    this.RTPS(1, false);
    this.RTPS(2, true);
  }

  private void RTPS(final int r, final boolean setMac0) {
    //IR1 = MAC1 = (TRX*1000h + RT11*VX0 + RT12*VY0 + RT13*VZ0) SAR (sf*12)
    //IR2 = MAC2 = (TRY*1000h + RT21*VX0 + RT22*VY0 + RT23*VZ0) SAR (sf*12)
    //IR3 = MAC3 = (TRZ*1000h + RT31*VX0 + RT32*VY0 + RT33*VZ0) SAR (sf*12)
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.TRX * 0x1000 + this.RT.v00 * this.V[r].x) + (long)this.RT.v01 * this.V[r].y) + (long)this.RT.v02 * this.V[r].z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.TRY * 0x1000 + this.RT.v10 * this.V[r].x) + (long)this.RT.v11 * this.V[r].y) + (long)this.RT.v12 * this.V[r].z) >> this.sf);
    final long mac3 = this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.TRZ * 0x1000 + this.RT.v20 * this.V[r].x) + (long)this.RT.v21 * this.V[r].y) + (long)this.RT.v22 * this.V[r].z);
    this.MAC3 = (int)(mac3 >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.setIR(3, (int)(mac3 >> 12), false);
    this.IR[3] = (short)MathHelper.clamp(this.MAC3, this.lm ? 0 : -0x8000, 0x7FFF);

    //SZ3 = MAC3 SAR ((1-sf)*12)                           ;ScreenZ FIFO 0..+FFFFh
    this.SZ[0] = this.SZ[1];
    this.SZ[1] = this.SZ[2];
    this.SZ[2] = this.SZ[3];
    this.SZ[3] = this.setSZ3(mac3 >> 12);

    //NON UNR Div Version
    //long result;
    //long div;
    //if (SZ[3] == 0) {
    //    result = 0x1FFFF;
    //} else {
    //    div = (((long)H * 0x20000 / SZ[3]) + 1) / 2;
    //
    //    if (div > 0x1FFFF) {
    //        result = 0x1FFFF;
    //        FLAG |= 0x1 << 17;
    //    } else {
    //        result = div;
    //    }
    //}

    //UNR Div
    long n;
    if((this.H & 0xffff) < (this.SZ[3] & 0xffff) * 2) {
      final int z = MathHelper.leadingZeroBits(this.SZ[3]);
      n = (this.H & 0xffff) << z;
      int d = (this.SZ[3] & 0xffff) << z;
      final short u = (short)((unrTable[(int)((d & 0xffffffffL) - 0x7FC0 >>> 7)] & 0xff) + 0x101);
      d = (int)(0x2000080 - (d & 0xffffffffL) * (u & 0xffff) >>> 8);
      d = (int)(0x0000080 + (d & 0xffffffffL) * (u & 0xffff) >>> 8);
      n = (int)Math.min(0x1ffff, n * (d & 0xffffffffL) + 0x8000 >>> 16);
    } else {
      this.FLAG |= 1L << 17;
      n = 0x1ffff;
    }

    //MAC0=(((H*20000h/SZ3)+1)/2)*IR1+OFX, SX2=MAC0/10000h ;ScrX FIFO -400h..+3FFh
    //MAC0=(((H*20000h/SZ3)+1)/2)*IR2+OFY, SY2=MAC0/10000h ;ScrY FIFO -400h..+3FFh
    //MAC0=(((H*20000h/SZ3)+1)/2)*DQA+DQB, IR0=MAC0/1000h  ;Depth cueing 0..+1000h
    final int x = (int)(this.setMAC0(n * this.IR[1] + this.OFX) >> 16);
    final int y = (int)(this.setMAC0(n * this.IR[2] + this.OFY) >> 16);

    this.SXY[0].set(this.SXY[1]);
    this.SXY[1].set(this.SXY[2]);
    this.SXY[2].x = this.setSXY(1, x);
    this.SXY[2].y = this.setSXY(2, y);

    if(setMac0) {
      final long mac0 = this.setMAC0(n * this.DQA + this.DQB);
      this.MAC0 = (int)mac0;
      this.IR[0] = this.setIR0(mac0 >> 12);
    }
  }

  private short setIR0(final long value) {
    if(value < 0) {
      this.FLAG |= 0x1000L;
      return 0;
    }

    if(value > 0x1000) {
      this.FLAG |= 0x1000L;
      return 0x1000;
    }

    return (short)value;
  }

  private short setSXY(final int i, final int value) {
    if(value < -0x400) {
      this.FLAG |= 0x4000L >>> i - 1;
      return -0x400;
    }

    if(value > 0x3ff) {
      this.FLAG |= 0x4000L >>> i - 1;
      return 0x3ff;
    }

    return (short)value;
  }

  private short setSZ3(final long value) {
    if(value < 0) {
      this.FLAG |= 0x4_0000L; // SZ3 or OTZ saturated to +0000h..+FFFFh
      return 0;
    }

    if(value > 0xffff) {
      this.FLAG |= 0x4_0000L; // SZ3 or OTZ saturated to +0000h..+FFFFh
      return (short)0xffff;
    }

    return (short)value;
  }

  private byte setRGB(final int i, final int value) {
    if(value < 0) {
      this.FLAG |= 0x20_0000L >>> i - 1;
      return 0;
    }

    if(value > 0xff) {
      this.FLAG |= 0x20_0000L >>> i - 1;
      return (byte)0xff;
    }

    return (byte)value;
  }

  private short setIR(final int i, final int value, final boolean lm) {
    if(lm && value < 0) {
      this.FLAG |= 0x100_0000L >>> i - 1;
      return 0;
    }

    if(!lm && value < -0x8000) {
      this.FLAG |= 0x100_0000L >>> i - 1;
      return -0x8000;
    }

    if(value > 0x7fff) {
      this.FLAG |= 0x100_0000L >>> i - 1;
      return 0x7fff;
    }

    return (short)value;
  }

  private long setMAC0(final long value) {
    if(value < -0x8000_0000) {
      this.FLAG |= 0x8000L;
    } else if(value > 0x7fff_ffff) {
      this.FLAG |= 0x1_0000L;
    }

    return value;
  }

  private long setMAC(final int i, final long value) {
    if(value < -0x800_0000_0000L) {
      this.FLAG |= 0x800_0000L >>> i - 1;
    } else if(value > 0x7ff_ffff_ffffL) {
      this.FLAG |= 0x4000_0000L >>> i - 1;
    }

    return value << 20 >> 20;
  }

  public int leadingZeroCount(int v) {
    final int sign = v >>> 31;
    int leadingCount = 0;

    for(int i = 0; i < 32; i++) {
      if(v >>> 31 != sign) {
        break;
      }

      leadingCount++;
      v <<= 1;
    }

    return leadingCount;
  }

  /** Data register 0/2/4 lower half VXY */
  public short getVertexX(final int index) {
    return this.V[index].x;
  }

  /** Data register 0/2/4 upper half VXY */
  public short getVertexY(final int index) {
    return this.V[index].y;
  }

  /** Data register 1/3/5 VZ */
  public short getVertexZ(final int index) {
    return this.V[index].z;
  }

  /** Data register 0/1, 2/3, 4/5 */
  public void setVertex(final int index, final int x, final int y, final int z) {
    this.V[index].x = (short)x;
    this.V[index].y = (short)y;
    this.V[index].z = (short)z;
  }

  /** Data register 0/1, 2/3, 4/5 */
  public void setVertex(final int index, final SVECTOR vert) {
    this.setVertex(index, vert.getX(), vert.getY(), vert.getZ());
  }

  /** Data register 0/1, 2/3, 4/5 */
  public void setVertex(final int index, final VECTOR vert) {
    this.setVertex(index, vert.getX(), vert.getY(), vert.getZ());
  }

  /** Data register 6 RGBC */
  public int getRgbc() {
    return this.RGBC.val();
  }

  /** Data register 6 RGBC */
  public void setRgbc(final int rgbc) {
    this.RGBC.val(rgbc);
  }

  /** Data register 6 RGBC */
  public void setRgbc(final COLOUR rgbc) {
    this.RGBC.val((int)rgbc.pack());
  }

  /** Data register 7 OTZ */
  public short getAverageZ() {
    return this.OTZ;
  }

  /** Data register 8 IR0 */
  public short getIr0() {
    return this.IR[0];
  }

  /** Data register 9 IR1 */
  public short getIr1() {
    return this.IR[1];
  }

  /** Data register 10 IR2 */
  public short getIr2() {
    return this.IR[2];
  }

  /** Data register 11 IR3 */
  public short getIr3() {
    return this.IR[3];
  }

  /** Data register 8 IR0 */
  public void setIr0(final int value) {
    this.IR[0] = (short)value;
  }

  /** Data register 9/10/11 IR123 */
  public void setIr123(final int x, final int y, final int z) {
    this.IR[1] = (short)x;
    this.IR[2] = (short)y;
    this.IR[3] = (short)z;
  }

  /** Data register 9/10/11 IR123 */
  public void setIr123(final SVECTOR vert) {
    this.setIr123(vert.getX(), vert.getY(), vert.getZ());
  }

  /** Data register 9/10/11 IR123 */
  public void setIr123(final VECTOR vert) {
    this.setIr123(vert.getX(), vert.getY(), vert.getZ());
  }

  /** Data register 12, 13, 14, 15 */
  public short getScreenX(final int index) {
    return this.SXY[index].x;
  }

  /** Data register 12, 13, 14, 15 */
  public short getScreenY(final int index) {
    return this.SXY[index].y;
  }

  /** Data register 16, 17, 18, 19 */
  public short getScreenZ(final int index) {
    return this.SZ[index];
  }

  /** Data register 20, 21, 22 */
  public int getRgb(final int index) {
    return this.RGB[index].val();
  }

  /** Data register 24 */
  public int getMac0() {
    return this.MAC0;
  }

  /** Data register 25 */
  public int getMac1() {
    return this.MAC1;
  }

  /** Data register 26 */
  public int getMac2() {
    return this.MAC2;
  }

  /** Data register 27 */
  public int getMac3() {
    return this.MAC3;
  }

  /** Control register 0-4 */
  public void getRotationMatrix(final MATRIX matrix) {
    matrix.set(0, this.RT.v00);
    matrix.set(1, this.RT.v01);
    matrix.set(2, this.RT.v02);
    matrix.set(3, this.RT.v10);
    matrix.set(4, this.RT.v11);
    matrix.set(5, this.RT.v12);
    matrix.set(6, this.RT.v20);
    matrix.set(7, this.RT.v21);
    matrix.set(8, this.RT.v22);
  }

  /** Control register 0-4 */
  public short getRotationMatrixValue(final int matrixIndex) {
    return switch(matrixIndex) {
      case 0 -> this.RT.v00;
      case 1 -> this.RT.v01;
      case 2 -> this.RT.v02;
      case 3 -> this.RT.v10;
      case 4 -> this.RT.v11;
      case 5 -> this.RT.v12;
      case 6 -> this.RT.v20;
      case 7 -> this.RT.v21;
      case 8 -> this.RT.v22;
      default -> throw new IllegalStateException("Unexpected value: " + matrixIndex);
    };
  }

  /** Control register 0-4 */
  public void setRotationMatrix(final MATRIX matrix) {
    this.RT.v00 = matrix.get(0);
    this.RT.v01 = matrix.get(1);
    this.RT.v02 = matrix.get(2);
    this.RT.v10 = matrix.get(3);
    this.RT.v11 = matrix.get(4);
    this.RT.v12 = matrix.get(5);
    this.RT.v20 = matrix.get(6);
    this.RT.v21 = matrix.get(7);
    this.RT.v22 = matrix.get(8);
  }

  /** Control register 0-4 */
  public void setRotationMatrixValue(final short v00, final short v01, final short v02, final short v10, final short v11, final short v12, final short v20, final short v21, final short v22) {
    this.RT.v00 = v00;
    this.RT.v01 = v01;
    this.RT.v02 = v02;
    this.RT.v10 = v10;
    this.RT.v11 = v11;
    this.RT.v12 = v12;
    this.RT.v20 = v20;
    this.RT.v21 = v21;
    this.RT.v22 = v22;
  }

  /** Control register 0-4 */
  public void setRotationMatrixValue(final int matrixIndex, final int value) {
    switch(matrixIndex) {
      case 0 -> this.RT.v00 = (short)value;
      case 1 -> this.RT.v01 = (short)value;
      case 2 -> this.RT.v02 = (short)value;
      case 3 -> this.RT.v10 = (short)value;
      case 4 -> this.RT.v11 = (short)value;
      case 5 -> this.RT.v12 = (short)value;
      case 6 -> this.RT.v20 = (short)value;
      case 7 -> this.RT.v21 = (short)value;
      case 8 -> this.RT.v22 = (short)value;
      default -> throw new IllegalStateException("Unexpected value: " + matrixIndex);
    }
  }

  /** Control register 5-7 */
  public void getTranslationVector(final VECTOR vector) {
    vector.setX(this.TRX);
    vector.setY(this.TRY);
    vector.setZ(this.TRZ);
  }

  /** Control register 5-7 */
  public void setTranslationVector(final VECTOR vector) {
    this.TRX = vector.getX();
    this.TRY = vector.getY();
    this.TRZ = vector.getZ();
  }

  /** Control register 8-12 light source/position matrix */
  public void setLightSourceMatrix(final MATRIX matrix) {
    this.LM.v00 = matrix.get(0);
    this.LM.v01 = matrix.get(1);
    this.LM.v02 = matrix.get(2);
    this.LM.v10 = matrix.get(3);
    this.LM.v11 = matrix.get(4);
    this.LM.v12 = matrix.get(5);
    this.LM.v20 = matrix.get(6);
    this.LM.v21 = matrix.get(7);
    this.LM.v22 = matrix.get(8);
  }

  /** Control register 13-15 background colour */
  public void setBackgroundColour(final int r, final int g, final int b) {
    this.RBK = r;
    this.GBK = g;
    this.BBK = b;
  }

  /** Control register 16-20 light colour matrix */
  public void setLightColourMatrix(final MATRIX matrix) {
    this.LRGB.v00 = matrix.get(0);
    this.LRGB.v01 = matrix.get(1);
    this.LRGB.v02 = matrix.get(2);
    this.LRGB.v10 = matrix.get(3);
    this.LRGB.v11 = matrix.get(4);
    this.LRGB.v12 = matrix.get(5);
    this.LRGB.v20 = matrix.get(6);
    this.LRGB.v21 = matrix.get(7);
    this.LRGB.v22 = matrix.get(8);
  }

  /** Control register 21-23 far colour */
  public void setFarColour(final int r, final int g, final int b) {
    this.RFC = r;
    this.GFC = g;
    this.BFC = b;
  }

  /** Control register 24 screen offset X */
  public int getScreenOffsetX() {
    return this.OFX;
  }

  /** Control register 25 screen offset Y */
  public int getScreenOffsetY() {
    return this.OFY;
  }

  /** Control register 24/25 screen offset */
  public void setScreenOffset(final int x, final int y) {
    this.OFX = x;
    this.OFY = y;
  }

  /** Control register 26 projection plane distance (H) */
  public short getProjectionPlaneDistance() {
    return this.H;
  }

  /** Control register 26 projection plane distance (H) */
  public void setProjectionPlaneDistance(final int distance) {
    this.H = (short)distance;
  }

  /** Control register 27/28 depth queueing parameters (DQA/DQB) */
  public void setDepthQueueingParameters(final int coefficient, final int offset) {
    this.DQA = (short)coefficient;
    this.DQB = offset;
  }

  public void setAverageZScaleFactors(final int zsf3, final int zsf4) {
    this.ZSF3 = (short)zsf3;
    this.ZSF4 = (short)zsf4;
  }

  /** Control register 31 */
  public int getFlags() {
    return (int)this.FLAG;
  }

  /** Control register 31 bit 31 is set */
  public boolean hasError() {
    return this.getFlags() < 0;
  }

  private void startCommand(final boolean fraction, final boolean saturate) {
    this.sf = fraction ? 12 : 0;
    this.lm = saturate;
    this.FLAG = 0;
  }

  private void endCommand() {
    if((this.FLAG & 0x7f87_e000L) != 0) {
//      LOGGER.error("GTE error during command %02x (flags: %08x)", command, this.FLAG);
//      LOGGER.error("Stack trace:", new Throwable());
      this.FLAG |= 0x8000_0000L;
    }
  }

  /** 0x1 RTPS - perspective transform single, 12-bit fraction */
  public void perspectiveTransform() {
    this.startCommand(true, false);
    this.RTPS(0, true);
    this.endCommand();
  }

  /** 0x1 RTPS - perspective transform single, 12-bit fraction */
  public void perspectiveTransform(final SVECTOR v0) {
    this.perspectiveTransform(v0.getX(), v0.getY(), v0.getZ());
  }

  /** 0x1 RTPS - perspective transform single, 12-bit fraction */
  public void perspectiveTransform(final VECTOR v0) {
    this.perspectiveTransform(v0.getX(), v0.getY(), v0.getZ());
  }

  /** 0x1 RTPS - perspective transform single, 12-bit fraction */
  public void perspectiveTransform(final int x, final int y, final int z) {
    this.setVertex(0, x, y, z);
    this.perspectiveTransform();
  }

  /**
   * 0x6 NCLIP - normal clipping
   *
   * @return vertex winding
   */
  public int normalClipping() {
    this.startCommand(true, false);
    this.NCLIP();
    this.endCommand();
    return this.getMac0();
  }

  /** 0xc OP - vector outer product, 12-bit fraction, IR1/2/3 * RT11/22/33 */
  public void outerProduct() {
    this.startCommand(true, false);
    this.OP();
    this.endCommand();
  }

  /** 0x12 MVMVA rotation * IR123 + none, 12-bit fraction, no saturate */
  public void rotateVector(final SVECTOR vector) {
    this.rotateVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x12 MVMVA rotation * IR123 + none, 12-bit fraction, no saturate */
  public void rotateVector(final VECTOR vector) {
    this.rotateVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x12 MVMVA rotation * IR123 + none, 12-bit fraction, no saturate */
  public void rotateVector(final int x, final int y, final int z) {
    this.setIr123(x, y, z);
    this.currentCommand = 0x49e012;
    this.startCommand(true, false);
    this.MVMVA();
    this.endCommand();
  }

  /** 0x12 MVMVA rotation * IR123 + TR, 12-bit fraction, no saturate */
  public void rotateTranslateVector(final SVECTOR vector) {
    this.rotateTranslateVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x12 MVMVA rotation * IR123 + TR, 12-bit fraction, no saturate */
  public void rotateTranslateVector(final VECTOR vector) {
    this.rotateTranslateVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x12 MVMVA rotation * IR123 + TR, 12-bit fraction, no saturate */
  public void rotateTranslateVector(final int x, final int y, final int z) {
    this.setIr123(x, y, z);
    this.currentCommand = 0x480012;
    this.startCommand(true, false);
    this.MVMVA();
    this.endCommand();
  }

  /** 0x12 MVMVA rotation * IR123 + none, 12-bit fraction, no saturate */
  public void rotateVector0(final SVECTOR vector) {
    this.rotateVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x12 MVMVA rotation * IR123 + none, 12-bit fraction, no saturate */
  public void rotateVector0(final VECTOR vector) {
    this.rotateVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x12 MVMVA rotation * IR123 + none, 12-bit fraction, no saturate */
  public void rotateVector0(final int x, final int y, final int z) {
    this.setIr123(x, y, z);
    this.currentCommand = 0x49e012;
    this.startCommand(false, false);
    this.MVMVA();
    this.endCommand();
  }

  /**
   * 0x1b NCCS - normal colour colour single vector, 12-bit fraction, saturate IR1/2/3
   *
   * @return colour
   */
  public int normalColour() {
    this.startCommand(true, true);
    this.NCCS(0);
    this.endCommand();
    return this.getRgb(2);
  }

  /** 0x28 square vector TR123, no fraction */
  public void squareVector(final SVECTOR vector) {
    this.squareVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x28 square vector TR123, no fraction */
  public void squareVector(final VECTOR vector) {
    this.squareVector(vector.getX(), vector.getY(), vector.getZ());
  }

  /** 0x28 square vector TR123, no fraction */
  public void squareVector(final int x, final int y, final int z) {
    this.setIr123(x, y, z);
    this.startCommand(false, false);
    this.SQR();
    this.endCommand();
  }

  /**
   * 0x2d AVSZ3 - average Z (triangle), 12-bit fraction
   *
   * @return average Z
   */
  public int averageZ3() {
    this.startCommand(true, false);
    this.AVSZ3();
    this.endCommand();
    return this.getAverageZ();
  }

  /**
   * 0x2e AVSZ4 - average Z (quad), 12-bit fraction
   *
   * @return average Z
   */
  public int averageZ4() {
    this.startCommand(true, false);
    this.AVSZ4();
    this.endCommand();
    return this.getAverageZ();
  }

  /** 0x30 RTPT - perspective transform triple, 12-bit fraction */
  public void perspectiveTransformTriangle() {
    this.startCommand(true, false);
    this.RTPT();
    this.endCommand();
  }

  /** 0x30 RTPT - perspective transform triple, 12-bit fraction */
  public void perspectiveTransformTriangle(final SVECTOR v0, final SVECTOR v1, final SVECTOR v2) {
    this.setVertex(0, v0);
    this.setVertex(1, v1);
    this.setVertex(2, v2);

    this.startCommand(true, false);
    this.RTPT();
    this.endCommand();
  }

  /** 0x30 RTPT - perspective transform triple, 12-bit fraction */
  public void perspectiveTransformTriangle(final VECTOR v0, final VECTOR v1, final VECTOR v2) {
    this.setVertex(0, v0);
    this.setVertex(1, v1);
    this.setVertex(2, v2);

    this.startCommand(true, false);
    this.RTPT();
    this.endCommand();
  }

  /** 0x3d GPF - general-purpose interpolation, (MAC123 = IR123 * IR0 >> 12) */
  public void generalPurposeInterpolate() {
    this.startCommand(false, false);
    this.GPF();
    this.endCommand();
  }
}
