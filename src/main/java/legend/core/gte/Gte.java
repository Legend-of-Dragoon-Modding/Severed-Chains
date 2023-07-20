package legend.core.gte;

import legend.core.MathHelper;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class Gte {
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
    public int r;
    public int g;
    public int b;
    public int c;

    public int val() {
      return this.c << 24 | this.b << 16 | this.g << 8 | this.r;
    }

    public void val(final int val) {
      this.r = val        & 0xff;
      this.g = val >>>  8 & 0xff;
      this.b = val >>> 16 & 0xff;
      this.c = val >>> 24 & 0xff;
    }
  }

  //Data Registers
  private final ShortVector3[] V = {new ShortVector3(), new ShortVector3(), new ShortVector3()};   //R0-1 R2-3 R4-5 s16
  private final Color RGBC = new Color();                     //R6
  private short OTZ;                     //R7
  private final short[] IR = new short[4];      //R8-11
  private final Vector2[] SXY = {new Vector2(), new Vector2(), new Vector2(), new Vector2()}; //R12-15 FIFO
  private final short[] SZ = new short[4];    //R16-19 FIFO
  private int MAC0;                       //R24

  //Control Registers
  private final Matrix RT = new Matrix();        //R32-36 R40-44 R48-52
  private final Matrix3f lightDirection = new Matrix3f();
  private final Matrix3f lightColour = new Matrix3f();
  private int TRX, TRY, TRZ;          //R37-39
  private final Vector3f backgroundColour = new Vector3f();          //R45-47
  private int OFX, OFY;          //R56 57 60
  private short H;                   //R58
  private short ZSF3, ZSF4;      //R61 62 59
  private long FLAG;                  //R63

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
    this.RTPS(0);
    this.RTPS(1);
    this.RTPS(2);
  }

  private void RTPS(final int r) {
    //IR1 = MAC1 = (TRX*1000h + RT11*VX0 + RT12*VY0 + RT13*VZ0) SAR (sf*12)
    //IR2 = MAC2 = (TRY*1000h + RT21*VX0 + RT22*VY0 + RT23*VZ0) SAR (sf*12)
    //IR3 = MAC3 = (TRZ*1000h + RT31*VX0 + RT32*VY0 + RT33*VZ0) SAR (sf*12)
    final int MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.TRX * 0x1000 + this.RT.v00 * this.V[r].x) + (long)this.RT.v01 * this.V[r].y) + (long)this.RT.v02 * this.V[r].z) >> 12);
    final int MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.TRY * 0x1000 + this.RT.v10 * this.V[r].x) + (long)this.RT.v11 * this.V[r].y) + (long)this.RT.v12 * this.V[r].z) >> 12);
    final int MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.TRZ * 0x1000 + this.RT.v20 * this.V[r].x) + (long)this.RT.v21 * this.V[r].y) + (long)this.RT.v22 * this.V[r].z) >> 12);

    this.IR[1] = this.setIR(1, MAC1);
    this.IR[2] = this.setIR(2, MAC2);
    this.IR[3] = this.setIR(3, MAC3);

    //SZ3 = MAC3 SAR ((1-sf)*12)                           ;ScreenZ FIFO 0..+FFFFh
    this.SZ[0] = this.SZ[1];
    this.SZ[1] = this.SZ[2];
    this.SZ[2] = this.SZ[3];
    this.SZ[3] = this.setSZ3(MAC3);

    //NON UNR Div Version
    final long n;
    if(this.SZ[3] == 0) {
      n = 0x1ffff;
    } else {
      final long div = ((this.H & 0xffffL) * 0x20000 / (this.SZ[3] & 0xffff) + 1) / 2;

      if(div > 0x1ffff) {
        n = 0x1ffff;
        this.FLAG |= 0x1 << 17;
      } else {
        n = div;
      }
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

  private short setIR(final int i, final int value) {
    if(value < -0x8000) {
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

  /** Data register 7 OTZ */
  public short getAverageZ() {
    return this.OTZ;
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

  /** Data register 24 */
  public int getMac0() {
    return this.MAC0;
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
  public void setLightSourceMatrix(final Matrix3f matrix) {
    this.lightDirection.set(matrix);
  }

  /** Control register 13-15 background colour */
  public void setBackgroundColour(final int r, final int g, final int b) {
    this.backgroundColour.x = r / 4096.0f;
    this.backgroundColour.y = g / 4096.0f;
    this.backgroundColour.z = b / 4096.0f;
  }

  /** Control register 16-20 light colour matrix */
  public void setLightColourMatrix(final Matrix3f matrix) {
    this.lightColour.set(matrix);
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

  private void startCommand() {
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
    this.startCommand();
    this.RTPS(0);
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
    this.startCommand();
    this.NCLIP();
    this.endCommand();
    return this.getMac0();
  }

  private final Vector3f colourTemp = new Vector3f();

  /**
   * 0x1b NCCS - normal colour colour single vector, 12-bit fraction, saturate IR1/2/3
   *
   * @return colour
   */
  public int normalColour(final Vector3f normal, final int colour) {
    this.RGBC.val(colour);

    MathHelper.clamp(normal.mulTranspose(this.lightDirection, this.colourTemp), 0.0f, Float.MAX_VALUE)
      .mul(this.lightColour)
      .add(this.backgroundColour);

    this.RGBC.r = (int)MathHelper.clamp(this.RGBC.r * this.colourTemp.x, 0, 0xff);
    this.RGBC.g = (int)MathHelper.clamp(this.RGBC.g * this.colourTemp.y, 0, 0xff);
    this.RGBC.b = (int)MathHelper.clamp(this.RGBC.b * this.colourTemp.z, 0, 0xff);

    return this.RGBC.val();
  }

  /**
   * 0x2d AVSZ3 - average Z (triangle), 12-bit fraction
   *
   * @return average Z
   */
  public int averageZ3() {
    this.startCommand();
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
    this.startCommand();
    this.AVSZ4();
    this.endCommand();
    return this.getAverageZ();
  }

  /** 0x30 RTPT - perspective transform triple, 12-bit fraction */
  public void perspectiveTransformTriangle() {
    this.startCommand();
    this.RTPT();
    this.endCommand();
  }

  /** 0x30 RTPT - perspective transform triple, 12-bit fraction */
  public void perspectiveTransformTriangle(final SVECTOR v0, final SVECTOR v1, final SVECTOR v2) {
    this.setVertex(0, v0);
    this.setVertex(1, v1);
    this.setVertex(2, v2);

    this.startCommand();
    this.RTPT();
    this.endCommand();
  }
}
