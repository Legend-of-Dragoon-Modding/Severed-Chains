package legend.core.gte;

import legend.core.MathHelper;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class Gte {
  private static class Vector2 {
    public float x;
    public float y;

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
  private final Vector3f[] V = {new Vector3f(), new Vector3f(), new Vector3f()};   //R0-1 R2-3 R4-5 s16
  private final Color RGBC = new Color();                     //R6
  private short OTZ;                     //R7
  private final Vector2[] SXY = {new Vector2(), new Vector2(), new Vector2(), new Vector2()}; //R12-15 FIFO
  private final int[] SZ = new int[4];    //R16-19 FIFO
  private int MAC0;                       //R24

  //Control Registers
  private final Matrix3f RT = new Matrix3f();        //R32-36 R40-44 R48-52
  private final Matrix3f lightDirection = new Matrix3f();
  private final Matrix3f lightColour = new Matrix3f();
  private final Vector3f translation = new Vector3f();          //R37-39
  private final Vector3f backgroundColour = new Vector3f();          //R45-47
  private int OFX, OFY;          //R56 57 60
  private float H;                   //R58
  private long FLAG;                  //R63

  private void AVSZ3() {
    //MAC0 = ZSF3 * (SZ1 + SZ2 + SZ3); for AVSZ3
    //OTZ = MAC0 / 1000h;for both(saturated to 0..FFFFh)
    final long avsz3 = (long)0x155 * (this.SZ[1] + this.SZ[2] + this.SZ[3]); // 1/3
    this.MAC0 = (int)this.setMAC0(avsz3);
    this.OTZ = this.setSZ3(avsz3 >> 12);
  }

  private void AVSZ4() {
    //MAC0 = ZSF4 * (SZ0 + SZ1 + SZ2 + SZ3);for AVSZ4
    //OTZ = MAC0 / 1000h;for both(saturated to 0..FFFFh)
    final long avsz4 = (long)0x100 * (this.SZ[0] + this.SZ[1] + this.SZ[2] + this.SZ[3]); // 1/4
    this.MAC0 = (int)this.setMAC0(avsz4);
    this.OTZ = this.setSZ3(avsz4 >> 12);
  }

  private void NCLIP() { //Normal clipping
    // MAC0 = SX0*SY1 + SX1*SY2 + SX2*SY0 - SX0*SY2 - SX1*SY0 - SX2*SY1
    this.MAC0 = (int)this.setMAC0((long)(this.SXY[0].x * (this.SXY[1].y - this.SXY[2].y) + this.SXY[1].x * (this.SXY[2].y - this.SXY[0].y) + this.SXY[2].x * (this.SXY[0].y - this.SXY[1].y)));
  }

  private void RTPT() { //Perspective Transformation Triple
    this.RTPS(0);
    this.RTPS(1);
    this.RTPS(2);
  }

  private final Vector3f positionTemp = new Vector3f();

  private void RTPS(final int r) {
    //IR1 = MAC1 = (TRX*1000h + RT11*VX0 + RT12*VY0 + RT13*VZ0) SAR (sf*12)
    //IR2 = MAC2 = (TRY*1000h + RT21*VX0 + RT22*VY0 + RT23*VZ0) SAR (sf*12)
    //IR3 = MAC3 = (TRZ*1000h + RT31*VX0 + RT32*VY0 + RT33*VZ0) SAR (sf*12)
    this.V[r]
      .mul(this.RT, this.positionTemp)
      .add(this.translation);

    this.positionTemp.z = MathHelper.clamp(this.positionTemp.z, 0.0f, 65536.0f);

    //SZ3 = MAC3 SAR ((1-sf)*12)                           ;ScreenZ FIFO 0..+FFFFh
    this.SZ[0] = this.SZ[1];
    this.SZ[1] = this.SZ[2];
    this.SZ[2] = this.SZ[3];
    this.SZ[3] = (int)this.positionTemp.z;

    final float n;
    if(this.SZ[3] == 0) {
      n = 1.0f;
      this.FLAG |= 0x1 << 17;
    } else {
      n = this.H / this.positionTemp.z;
    }

    //MAC0=(((H*20000h/SZ3)+1)/2)*IR1+OFX, SX2=MAC0/10000h ;ScrX FIFO -400h..+3FFh
    //MAC0=(((H*20000h/SZ3)+1)/2)*IR2+OFY, SY2=MAC0/10000h ;ScrY FIFO -400h..+3FFh
    //MAC0=(((H*20000h/SZ3)+1)/2)*DQA+DQB, IR0=MAC0/1000h  ;Depth cueing 0..+1000h
    this.SXY[0].set(this.SXY[1]);
    this.SXY[1].set(this.SXY[2]);
    this.SXY[2].x = this.setSXY(n * this.positionTemp.x + this.OFX);
    this.SXY[2].y = this.setSXY(n * this.positionTemp.y + this.OFY);
  }

  private float setSXY(final float value) {
    if(value < -0x400) {
      this.FLAG |= 0x4000L >>> 2 - 1;
      return -0x400;
    }

    if(value > 0x3ff) {
      this.FLAG |= 0x4000L >>> 2 - 1;
      return 0x3ff;
    }

    return value;
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

  private long setMAC0(final long value) {
    if(value < -0x8000_0000) {
      this.FLAG |= 0x8000L;
    } else if(value > 0x7fff_ffff) {
      this.FLAG |= 0x1_0000L;
    }

    return value;
  }

  /** Data register 0/1, 2/3, 4/5 */
  public void setVertex(final int index, final int x, final int y, final int z) {
    this.V[index].x = x;
    this.V[index].y = y;
    this.V[index].z = z;
  }

  /** Data register 0/1, 2/3, 4/5 */
  public void setVertex(final int index, final float x, final float y, final float z) {
    this.V[index].x = x;
    this.V[index].y = y;
    this.V[index].z = z;
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
    return (short)this.SXY[index].x;
  }

  /** Data register 12, 13, 14, 15 */
  public short getScreenY(final int index) {
    return (short)this.SXY[index].y;
  }

  /** Data register 16, 17, 18, 19 */
  public int getScreenZ(final int index) {
    return this.SZ[index];
  }

  /** Data register 24 */
  public int getMac0() {
    return this.MAC0;
  }

  /** Control register 0-4 */
  public void getRotationMatrix(final MATRIX matrix) {
    matrix.set(0, (short)(this.RT.m00 * 4096.0f));
    matrix.set(1, (short)(this.RT.m01 * 4096.0f));
    matrix.set(2, (short)(this.RT.m02 * 4096.0f));
    matrix.set(3, (short)(this.RT.m10 * 4096.0f));
    matrix.set(4, (short)(this.RT.m11 * 4096.0f));
    matrix.set(5, (short)(this.RT.m12 * 4096.0f));
    matrix.set(6, (short)(this.RT.m20 * 4096.0f));
    matrix.set(7, (short)(this.RT.m21 * 4096.0f));
    matrix.set(8, (short)(this.RT.m22 * 4096.0f));
  }

  /** Control register 0-4 */
  public void getRotationMatrix(final Matrix3f matrix) {
    matrix.set(this.RT);
  }

  /** Control register 0-4 */
  public void setRotationMatrix(final MATRIX matrix) {
    this.RT.m00 = matrix.get(0) / 4096.0f;
    this.RT.m01 = matrix.get(1) / 4096.0f;
    this.RT.m02 = matrix.get(2) / 4096.0f;
    this.RT.m10 = matrix.get(3) / 4096.0f;
    this.RT.m11 = matrix.get(4) / 4096.0f;
    this.RT.m12 = matrix.get(5) / 4096.0f;
    this.RT.m20 = matrix.get(6) / 4096.0f;
    this.RT.m21 = matrix.get(7) / 4096.0f;
    this.RT.m22 = matrix.get(8) / 4096.0f;
    this.RT.transpose();
  }

  /** Control register 0-4 */
  public void setRotationMatrix(final Matrix3f matrix) {
    this.RT.set(matrix);
  }

  /** Control register 5-7 */
  public void getTranslationVector(final VECTOR vector) {
    vector.setX((int)this.translation.x);
    vector.setY((int)this.translation.y);
    vector.setZ((int)this.translation.z);
  }

  /** Control register 5-7 */
  public void getTranslationVector(final Vector3f vector) {
    vector.set(this.translation);
  }

  /** Control register 5-7 */
  public void setTranslationVector(final VECTOR vector) {
    this.translation.x = vector.getX();
    this.translation.y = vector.getY();
    this.translation.z = vector.getZ();
  }

  /** Control register 5-7 */
  public void setTranslationVector(final Vector3f vector) {
    this.translation.set(vector);
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

  /** Control register 13-15 background colour */
  public void setBackgroundColour(final float r, final float g, final float b) {
    this.backgroundColour.x = r;
    this.backgroundColour.y = g;
    this.backgroundColour.z = b;
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
  public int getProjectionPlaneDistance() {
    return (int)this.H;
  }

  /** Control register 26 projection plane distance (H) */
  public void setProjectionPlaneDistance(final float distance) {
    this.H = distance;
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
  public void perspectiveTransform(final Vector3f v0) {
    this.perspectiveTransform(v0.x, v0.y, v0.z);
  }

  /** 0x1 RTPS - perspective transform single, 12-bit fraction */
  public void perspectiveTransform(final int x, final int y, final int z) {
    this.setVertex(0, x, y, z);
    this.perspectiveTransform();
  }

  /** 0x1 RTPS - perspective transform single, 12-bit fraction */
  public void perspectiveTransform(final float x, final float y, final float z) {
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

    MathHelper.clamp(normal.mul(this.lightDirection, this.colourTemp), 0.0f, Float.MAX_VALUE)
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
