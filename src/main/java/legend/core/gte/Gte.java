package legend.core.gte;

import legend.core.IoHelper;
import legend.core.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

public class Gte {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Gte.class);

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
    public ShortVector3 v1;
    public ShortVector3 v2;
    public ShortVector3 v3;

    private Matrix() {
      this(new ShortVector3(), new ShortVector3(), new ShortVector3());
    }

    private Matrix(final ShortVector3 v1, final ShortVector3 v2, final ShortVector3 v3) {
      this.v1 = v1;
      this.v2 = v2;
      this.v3 = v3;
    }

    public Matrix copy() {
      return new Matrix(this.v1.copy(), this.v2.copy(), this.v3.copy());
    }

    public void dump(final ByteBuffer stream) {
      this.v1.dump(stream);
      this.v2.dump(stream);
      this.v3.dump(stream);
    }

    public void load(final ByteBuffer stream) {
      this.v1.load(stream);
      this.v2.load(stream);
      this.v3.load(stream);
    }
  }

  private static class Vector2 {
    public short x;
    public short y;

    public int val() {
      return (this.y & 0xffff) << 16 | this.x & 0xffff;
    }

    public void val(final int val) {
      this.x = (short)(val & 0xffff);
      this.y = (short)(val >> 16 & 0xffff);
    }

    public void set(final Vector2 other) {
      this.x = other.x;
      this.y = other.y;
    }

    public void dump(final ByteBuffer stream) {
      IoHelper.write(stream, this.x);
      IoHelper.write(stream, this.y);
    }

    public void load(final ByteBuffer stream) {
      this.x = IoHelper.readShort(stream);
      this.y = IoHelper.readShort(stream);
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

    public void dump(final ByteBuffer stream) {
      stream.put(this.r);
      stream.put(this.g);
      stream.put(this.b);
      stream.put(this.c);
    }

    public void load(final ByteBuffer stream) {
      this.r = stream.get();
      this.g = stream.get();
      this.b = stream.get();
      this.c = stream.get();
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
  private int RES1;                      //R23 prohibited
  private int MAC0;                       //R24
  private int MAC1, MAC2, MAC3;           //R25-27
  private short IRGB;//, ORGB;           //R28-29 Orgb is readonly and read by irgb
  private int LZCS, LZCR;                 //R30-31

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

  public void execute(final int command) {
    //Console.WriteLine($"GTE EXECUTE {(command & 0x3F):x2}");

    this.currentCommand = command;
    this.sf = ((command & 0x8_0000) >>> 19) * 12;
    this.lm = (command >>> 10 & 0x1) != 0;
    this.FLAG = 0;

    switch(command & 0x3F) {
      case 0x01 -> this.RTPS(0, true);
      case 0x06 -> this.NCLIP();
      case 0x0C -> this.OP();
      case 0x10 -> this.DPCS(false);
      case 0x11 -> this.INTPL();
      case 0x12 -> this.MVMVA();
      case 0x13 -> this.NCDS(0);
      case 0x14 -> this.CDP();
      case 0x16 -> this.NCDT();
      case 0x1B -> this.NCCS(0);
      case 0x1C -> this.CC();
      case 0x1E -> this.NCS(0);
      case 0x20 -> this.NCT();
      case 0x28 -> this.SQR();
      case 0x29 -> this.DCPL();
      case 0x2A -> this.DCPT();
      case 0x2D -> this.AVSZ3();
      case 0x2E -> this.AVSZ4();
      case 0x30 -> this.RTPT();
      case 0x3D -> this.GPF();
      case 0x3E -> this.GPL();
      case 0x3F -> this.NCCT();
      default -> {
        LOGGER.warn("UNIMPLEMENTED GTE COMMAND %2x", command & 0x3f);
        assert false : "Unimplemented GTE command " + Long.toHexString(command & 0x3f);
      }
    }

    if((this.FLAG & 0x7f87_e000L) != 0) {
//      LOGGER.error("GTE error during command %02x (flags: %08x)", command, this.FLAG);
//      LOGGER.error("Stack trace:", new Throwable());
      this.FLAG |= 0x8000_0000L;
    }
  }

  private void CDP() {
    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (BK * 1000h + LCM * IR) SAR(sf * 12)
    // WARNING each multiplication can trigger mac flags so the check is needed on each op! Somehow this only affects the color matrix and not the light one
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.RBK * 0x1000 + this.LRGB.v1.x * this.IR[1]) + (long)this.LRGB.v1.y * this.IR[2]) + (long)this.LRGB.v1.z * this.IR[3]) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.GBK * 0x1000 + this.LRGB.v2.x * this.IR[1]) + (long)this.LRGB.v2.y * this.IR[2]) + (long)this.LRGB.v2.z * this.IR[3]) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.BBK * 0x1000 + this.LRGB.v3.x * this.IR[1]) + (long)this.LRGB.v3.y * this.IR[2]) + (long)this.LRGB.v3.z * this.IR[3]) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [MAC1, MAC2, MAC3] = [R * IR1, G * IR2, B * IR3] SHL 4;
    this.MAC1 = (int)(this.setMAC(1, (long)(this.RGBC.r & 0xff) * this.IR[1]) << 4);
    this.MAC2 = (int)(this.setMAC(2, (long)(this.RGBC.g & 0xff) * this.IR[2]) << 4);
    this.MAC3 = (int)(this.setMAC(3, (long)(this.RGBC.b & 0xff) * this.IR[3]) << 4);

    this.interpolateColor(this.MAC1, this.MAC2, this.MAC3);

    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE]
    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;
  }

  private void CC() {
    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (BK * 1000h + LCM * IR) SAR(sf * 12)
    // WARNING each multiplication can trigger mac flags so the check is needed on each op! Somehow this only affects the color matrix and not the light one
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.RBK * 0x1000 + this.LRGB.v1.x * this.IR[1]) + (long)this.LRGB.v1.y * this.IR[2]) + (long)this.LRGB.v1.z * this.IR[3]) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.GBK * 0x1000 + this.LRGB.v2.x * this.IR[1]) + (long)this.LRGB.v2.y * this.IR[2]) + (long)this.LRGB.v2.z * this.IR[3]) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.BBK * 0x1000 + this.LRGB.v3.x * this.IR[1]) + (long)this.LRGB.v3.y * this.IR[2]) + (long)this.LRGB.v3.z * this.IR[3]) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [MAC1, MAC2, MAC3] = [R * IR1, G * IR2, B * IR3] SHL 4;
    this.MAC1 = (int)(this.setMAC(1, (long)(this.RGBC.r & 0xff) * this.IR[1]) << 4);
    this.MAC2 = (int)(this.setMAC(2, (long)(this.RGBC.g & 0xff) * this.IR[2]) << 4);
    this.MAC3 = (int)(this.setMAC(3, (long)(this.RGBC.b & 0xff) * this.IR[3]) << 4);

    // [MAC1, MAC2, MAC3] = [MAC1, MAC2, MAC3] SAR(sf * 12);< --- for NCDx / NCCx
    this.MAC1 = (int)(this.setMAC(1, this.MAC1) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.MAC2) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.MAC3) >> this.sf);

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

  private void DCPT() {
    this.DPCS(true);
    this.DPCS(true);
    this.DPCS(true);
  }

  private void DCPL() {
    //[MAC1, MAC2, MAC3] = [R*IR1, G*IR2, B*IR3] SHL 4          ;<--- for DCPL only
    this.MAC1 = (int)(this.setMAC(1, (this.RGBC.r & 0xff) * this.IR[1]) << 4);
    this.MAC2 = (int)(this.setMAC(2, (this.RGBC.g & 0xff) * this.IR[2]) << 4);
    this.MAC3 = (int)(this.setMAC(3, (this.RGBC.b & 0xff) * this.IR[3]) << 4);

    this.interpolateColor(this.MAC1, this.MAC2, this.MAC3);

    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE]
    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;
  }

  private void NCCS(final int r) {
    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (LLM * V0) SAR(sf * 12)
    this.MAC1 = (int)(this.setMAC(1, (long)this.LM.v1.x * this.V[r].x + this.LM.v1.y * this.V[r].y + this.LM.v1.z * this.V[r].z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, (long)this.LM.v2.x * this.V[r].x + this.LM.v2.y * this.V[r].y + this.LM.v2.z * this.V[r].z) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, (long)this.LM.v3.x * this.V[r].x + this.LM.v3.y * this.V[r].y + this.LM.v3.z * this.V[r].z) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (BK * 1000h + LCM * IR) SAR(sf * 12)
    // WARNING each multiplication can trigger mac flags so the check is needed on each op! Somehow this only affects the color matrix and not the light one
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.RBK * 0x1000 + this.LRGB.v1.x * this.IR[1]) + (long)this.LRGB.v1.y * this.IR[2]) + (long)this.LRGB.v1.z * this.IR[3]) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.GBK * 0x1000 + this.LRGB.v2.x * this.IR[1]) + (long)this.LRGB.v2.y * this.IR[2]) + (long)this.LRGB.v2.z * this.IR[3]) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.BBK * 0x1000 + this.LRGB.v3.x * this.IR[1]) + (long)this.LRGB.v3.y * this.IR[2]) + (long)this.LRGB.v3.z * this.IR[3]) >> this.sf);

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

  private void NCCT() {
    this.NCCS(0);
    this.NCCS(1);
    this.NCCS(2);
  }

  private void DPCS(final boolean dpct) {
    byte r = this.RGBC.r;
    byte g = this.RGBC.g;
    byte b = this.RGBC.b;

    // WHEN DCPT it uses RGB FIFO instead RGBC
    if(dpct) {
      r = this.RGB[0].r;
      g = this.RGB[0].g;
      b = this.RGB[0].b;
    }
    //[MAC1, MAC2, MAC3] = [R, G, B] SHL 16                     ;<--- for DPCS/DPCT
    this.MAC1 = (int)(this.setMAC(1, r & 0xff) << 16);
    this.MAC2 = (int)(this.setMAC(2, g & 0xff) << 16);
    this.MAC3 = (int)(this.setMAC(3, b & 0xff) << 16);

    this.interpolateColor(this.MAC1, this.MAC2, this.MAC3);

    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE]
    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;
  }

  private void INTPL() {
    // [MAC1, MAC2, MAC3] = [IR1, IR2, IR3] SHL 12               ;<--- for INTPL only
    this.MAC1 = (int)this.setMAC(1, (long)this.IR[1] << 12);
    this.MAC2 = (int)this.setMAC(2, (long)this.IR[2] << 12);
    this.MAC3 = (int)this.setMAC(3, (long)this.IR[3] << 12);

    this.interpolateColor(this.MAC1, this.MAC2, this.MAC3);

    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE]
    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;
  }

  private void NCT() {
    this.NCS(0);
    this.NCS(1);
    this.NCS(2);
  }

  private void NCS(final int r) {
    //In: V0 = Normal vector(for triple variants repeated with V1 and V2),
    //BK = Background color, RGBC = Primary color / code, LLM = Light matrix, LCM = Color matrix, IR0 = Interpolation value.

    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (LLM * V0) SAR(sf * 12)
    this.MAC1 = (int)(this.setMAC(1, (long)this.LM.v1.x * this.V[r].x + this.LM.v1.y * this.V[r].y + this.LM.v1.z * this.V[r].z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, (long)this.LM.v2.x * this.V[r].x + this.LM.v2.y * this.V[r].y + this.LM.v2.z * this.V[r].z) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, (long)this.LM.v3.x * this.V[r].x + this.LM.v3.y * this.V[r].y + this.LM.v3.z * this.V[r].z) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (BK * 1000h + LCM * IR) SAR(sf * 12)
    // WARNING each multiplication can trigger mac flags so the check is needed on each op! Somehow this only affects the color matrix and not the light one
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.RBK * 0x1000 + this.LRGB.v1.x * this.IR[1]) + (long)this.LRGB.v1.y * this.IR[2]) + (long)this.LRGB.v1.z * this.IR[3]) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.GBK * 0x1000 + this.LRGB.v2.x * this.IR[1]) + (long)this.LRGB.v2.y * this.IR[2]) + (long)this.LRGB.v2.z * this.IR[3]) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.BBK * 0x1000 + this.LRGB.v3.x * this.IR[1]) + (long)this.LRGB.v3.y * this.IR[2]) + (long)this.LRGB.v3.z * this.IR[3]) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

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
      mx.v1.x = (short)-((this.RGBC.r & 0xff) << 4);
      mx.v1.y = (short)((this.RGBC.r & 0xff) << 4);
      mx.v1.z = this.IR[0];
      mx.v2.x = mx.v2.y = mx.v2.z = this.RT.v1.z;
      mx.v3.x = mx.v3.y = mx.v3.z = this.RT.v2.y;
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

      long mac1 = this.setMAC(1, tx * 0x1000 + mx.v1.x * vx.x);
      long mac2 = this.setMAC(2, ty * 0x1000 + mx.v2.x * vx.x);
      long mac3 = this.setMAC(3, tz * 0x1000 + mx.v3.x * vx.x);

      this.setIR(1, (int)(mac1 >> this.sf), false);
      this.setIR(2, (int)(mac2 >> this.sf), false);
      this.setIR(3, (int)(mac3 >> this.sf), false);

      mac1 = this.setMAC(1, this.setMAC(1, (long)mx.v1.y * vx.y) + (long)mx.v1.z * vx.z);
      mac2 = this.setMAC(2, this.setMAC(2, (long)mx.v2.y * vx.y) + (long)mx.v2.z * vx.z);
      mac3 = this.setMAC(3, this.setMAC(3, (long)mx.v3.y * vx.y) + (long)mx.v3.z * vx.z);

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

    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, tx * 0x1000 + mx.v1.x * vx.x) + (long)mx.v1.y * vx.y) + (long)mx.v1.z * vx.z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, ty * 0x1000 + mx.v2.x * vx.x) + (long)mx.v2.y * vx.y) + (long)mx.v2.z * vx.z) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, tz * 0x1000 + mx.v3.x * vx.x) + (long)mx.v3.y * vx.y) + (long)mx.v3.z * vx.z) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);
  }

  private void GPL() {
    //[MAC1, MAC2, MAC3] = [MAC1, MAC2, MAC3] SHL(sf*12);<--- for GPL only
    //[MAC1, MAC2, MAC3] = (([IR1, IR2, IR3] * IR0) + [MAC1, MAC2, MAC3]) SAR(sf*12)
    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE], [IR1, IR2, IR3] = [MAC1, MAC2, MAC3]
    //Note: Although the SHL in GPL is theoretically undone by the SAR, 44bit overflows can occur internally when sf=1.

    final long mac1 = (long)this.MAC1 << this.sf;
    final long mac2 = (long)this.MAC2 << this.sf;
    final long mac3 = (long)this.MAC3 << this.sf;

    this.MAC1 = (int)(this.setMAC(1, this.IR[1] * this.IR[0] + mac1) >> this.sf); // This is a good example of why setMac can't return int directly
    this.MAC2 = (int)(this.setMAC(2, this.IR[2] * this.IR[0] + mac2) >> this.sf); // as you can't >>> before cause it doesn't trigger the flags and if
    this.MAC3 = (int)(this.setMAC(3, this.IR[3] * this.IR[0] + mac3) >> this.sf); // you do it after you get wrong values

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

  private void NCDT() {
    this.NCDS(0);
    this.NCDS(1);
    this.NCDS(2);
  }

  private void OP() {
    //[MAC1, MAC2, MAC3] = [IR3*D2-IR2*D3, IR1*D3-IR3*D1, IR2*D1-IR1*D2] SAR(sf*12)
    //[IR1, IR2, IR3]    = [MAC1, MAC2, MAC3]                        ;copy result
    //Calculates the outer product of two signed 16bit vectors.
    //Note: D1,D2,D3 are meant to be the RT11,RT22,RT33 elements of the RT matrix "misused" as vector. lm should be usually zero.

    final short d1 = this.RT.v1.x;
    final short d2 = this.RT.v2.y;
    final short d3 = this.RT.v3.z;

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

  private void NCDS(final int r) {
    //Normal color depth cue (single vector) //329048 WIP FLAGS
    //In: V0 = Normal vector(for triple variants repeated with V1 and V2),
    //BK = Background color, RGBC = Primary color / code, LLM = Light matrix, LCM = Color matrix, IR0 = Interpolation value.

    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (LLM * V0) SAR(sf * 12)
    this.MAC1 = (int)(this.setMAC(1, (long)this.LM.v1.x * this.V[r].x + this.LM.v1.y * this.V[r].y + this.LM.v1.z * this.V[r].z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, (long)this.LM.v2.x * this.V[r].x + this.LM.v2.y * this.V[r].y + this.LM.v2.z * this.V[r].z) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, (long)this.LM.v3.x * this.V[r].x + this.LM.v3.y * this.V[r].y + this.LM.v3.z * this.V[r].z) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3] = (BK * 1000h + LCM * IR) SAR(sf * 12)
    // WARNING each multiplication can trigger mac flags so the check is needed on each op! Somehow this only affects the color matrix and not the light one
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.RBK * 0x1000 + this.LRGB.v1.x * this.IR[1]) + (long)this.LRGB.v1.y * this.IR[2]) + (long)this.LRGB.v1.z * this.IR[3]) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.GBK * 0x1000 + this.LRGB.v2.x * this.IR[1]) + (long)this.LRGB.v2.y * this.IR[2]) + (long)this.LRGB.v2.z * this.IR[3]) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.BBK * 0x1000 + this.LRGB.v3.x * this.IR[1]) + (long)this.LRGB.v3.y * this.IR[2]) + (long)this.LRGB.v3.z * this.IR[3]) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);

    // [MAC1, MAC2, MAC3] = [R * IR1, G * IR2, B * IR3] SHL 4;< --- for NCDx / NCCx
    this.MAC1 = (int)this.setMAC(1, (long)(this.RGBC.r & 0xff) * this.IR[1] << 4);
    this.MAC2 = (int)this.setMAC(2, (long)(this.RGBC.g & 0xff) * this.IR[2] << 4);
    this.MAC3 = (int)this.setMAC(3, (long)(this.RGBC.b & 0xff) * this.IR[3] << 4);

    this.interpolateColor(this.MAC1, this.MAC2, this.MAC3);

    // Color FIFO = [MAC1 / 16, MAC2 / 16, MAC3 / 16, CODE]
    this.RGB[0].set(this.RGB[1]);
    this.RGB[1].set(this.RGB[2]);

    this.RGB[2].r = this.setRGB(1, this.MAC1 >> 4);
    this.RGB[2].g = this.setRGB(2, this.MAC2 >> 4);
    this.RGB[2].b = this.setRGB(3, this.MAC3 >> 4);
    this.RGB[2].c = this.RGBC.c;
  }

  private void interpolateColor(final int mac1, final int mac2, final int mac3) {
    // PSX SPX is very convoluted about this and it lacks some info
    // [MAC1, MAC2, MAC3] = MAC + (FC - MAC) * IR0;< --- for NCDx only
    // Note: Above "[IR1,IR2,IR3]=(FC-MAC)" is saturated to - 8000h..+7FFFh(ie. as if lm = 0)
    // Details on "MAC+(FC-MAC)*IR0":
    // [IR1, IR2, IR3] = (([RFC, GFC, BFC] SHL 12) - [MAC1, MAC2, MAC3]) SAR(sf * 12)
    // [MAC1, MAC2, MAC3] = (([IR1, IR2, IR3] * IR0) + [MAC1, MAC2, MAC3])
    // [MAC1, MAC2, MAC3] = [MAC1, MAC2, MAC3] SAR(sf * 12);< --- for NCDx / NCCx
    // [IR1, IR2, IR3] = [MAC1, MAC2, MAC3]

    this.MAC1 = (int)(this.setMAC(1, ((long)this.RFC << 12) - mac1) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, ((long)this.GFC << 12) - mac2) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, ((long)this.BFC << 12) - mac3) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, false);
    this.IR[2] = this.setIR(2, this.MAC2, false);
    this.IR[3] = this.setIR(3, this.MAC3, false);

    this.MAC1 = (int)(this.setMAC(1, (long)this.IR[1] * this.IR[0] + mac1) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, (long)this.IR[2] * this.IR[0] + mac2) >> this.sf);
    this.MAC3 = (int)(this.setMAC(3, (long)this.IR[3] * this.IR[0] + mac3) >> this.sf);

    this.IR[1] = this.setIR(1, this.MAC1, this.lm);
    this.IR[2] = this.setIR(2, this.MAC2, this.lm);
    this.IR[3] = this.setIR(3, this.MAC3, this.lm);
  }

  private void NCLIP() { //Normal clipping
    // MAC0 = SX0*SY1 + SX1*SY2 + SX2*SY0 - SX0*SY2 - SX1*SY0 - SX2*SY1
    this.MAC0 = (int)this.setMAC0((long)this.SXY[0].x * this.SXY[1].y + this.SXY[1].x * this.SXY[2].y + this.SXY[2].x * this.SXY[0].y - this.SXY[0].x * this.SXY[2].y - this.SXY[1].x * this.SXY[0].y - this.SXY[2].x * this.SXY[1].y);
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
    this.MAC1 = (int)(this.setMAC(1, this.setMAC(1, this.setMAC(1, (long)this.TRX * 0x1000 + this.RT.v1.x * this.V[r].x) + (long)this.RT.v1.y * this.V[r].y) + (long)this.RT.v1.z * this.V[r].z) >> this.sf);
    this.MAC2 = (int)(this.setMAC(2, this.setMAC(2, this.setMAC(2, (long)this.TRY * 0x1000 + this.RT.v2.x * this.V[r].x) + (long)this.RT.v2.y * this.V[r].y) + (long)this.RT.v2.z * this.V[r].z) >> this.sf);
    final long mac3 = this.setMAC(3, this.setMAC(3, this.setMAC(3, (long)this.TRZ * 0x1000 + this.RT.v3.x * this.V[r].x) + (long)this.RT.v3.y * this.V[r].y) + (long)this.RT.v3.z * this.V[r].z);
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

  private static byte saturateRGB(final int value) {
    if(value < 0x00) {
      return 0x00;
    }

    if(value > 0x1f) {
      return 0x1f;
    }

    return (byte)value;
  }

  private static int leadingCount(int v) {
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

  public int loadData(final int fs) {
    return switch(fs) {
      case 0 -> this.V[0].getXY();
      case 1 -> this.V[0].z;
      case 2 -> this.V[1].getXY();
      case 3 -> this.V[1].z;
      case 4 -> this.V[2].getXY();
      case 5 -> this.V[2].z;
      case 6 -> this.RGBC.val();
      case 7 -> this.OTZ & 0xffff;
      case 8 -> this.IR[0];
      case 9 -> this.IR[1];
      case 10 -> this.IR[2];
      case 11 -> this.IR[3];
      case 12 -> this.SXY[0].val();
      case 13 -> this.SXY[1].val();
      case 14, 15 -> this.SXY[2].val();
      case 16 -> this.SZ[0] & 0xffff;
      case 17 -> this.SZ[1] & 0xffff;
      case 18 -> this.SZ[2] & 0xffff;
      case 19 -> this.SZ[3] & 0xffff;
      case 20 -> this.RGB[0].val();
      case 21 -> this.RGB[1].val();
      case 22 -> this.RGB[2].val();
      case 23 -> this.RES1;
      case 24 -> this.MAC0;
      case 25 -> this.MAC1;
      case 26 -> this.MAC2;
      case 27 -> this.MAC3;
      case 28, 29 -> this.IRGB = (short)(saturateRGB(this.IR[3] / 0x80) << 10 | saturateRGB(this.IR[2] / 0x80) << 5 | saturateRGB(this.IR[1] / 0x80));
      case 30 -> this.LZCS;
      case 31 -> this.LZCR;
      default -> 0xffff_ffff;
    };
  }

  public void writeData(final int fs, final int v) {
    switch(fs) {
      case 0:
        this.V[0].setXY(v);
        break;
      case 1:
        this.V[0].z = (short)v;
        break;
      case 2:
        this.V[1].setXY(v);
        break;
      case 3:
        this.V[1].z = (short)v;
        break;
      case 4:
        this.V[2].setXY(v);
        break;
      case 5:
        this.V[2].z = (short)v;
        break;
      case 6:
        this.RGBC.val(v);
        break;
      case 7:
        this.OTZ = (short)v;
        break;
      case 8:
        this.IR[0] = (short)v;
        break;
      case 9:
        this.IR[1] = (short)v;
        break;
      case 10:
        this.IR[2] = (short)v;
        break;
      case 11:
        this.IR[3] = (short)v;
        break;
      case 12:
        this.SXY[0].val(v);
        break;
      case 13:
        this.SXY[1].val(v);
        break;
      case 14:
        this.SXY[2].val(v);
        break;
      case 15:
        this.SXY[0].set(this.SXY[1]);
        this.SXY[1].set(this.SXY[2]);
        this.SXY[2].val(v);
        break; //On load mirrors 0x14 on write cycles the fifo
      case 16:
        this.SZ[0] = (short)v;
        break;
      case 17:
        this.SZ[1] = (short)v;
        break;
      case 18:
        this.SZ[2] = (short)v;
        break;
      case 19:
        this.SZ[3] = (short)v;
        break;
      case 20:
        this.RGB[0].val(v);
        break;
      case 21:
        this.RGB[1].val(v);
        break;
      case 22:
        this.RGB[2].val(v);
        break;
      case 23:
        this.RES1 = v;
        break;
      case 24:
        this.MAC0 = v;
        break;
      case 25:
        this.MAC1 = v;
        break;
      case 26:
        this.MAC2 = v;
        break;
      case 27:
        this.MAC3 = v;
        break;
      case 28:
        this.IRGB = (short)(v & 0x7fff);
        this.IR[1] = (short)((v & 0x1f) * 0x80);
        this.IR[2] = (short)((v >>> 5 & 0x1f) * 0x80);
        this.IR[3] = (short)((v >>> 10 & 0x1f) * 0x80);
        break;
      case 29: /*ORGB = (ushort)v;*/
        break; //Only Read its set by IRGB
      case 30:
        this.LZCS = v;
        this.LZCR = leadingCount(v);
        break;
      case 31: /*LZCR = (int)v;*/
        break; //Only Read its set by LZCS
    }
  }

  public long loadControl(final int fs) {
    return switch(fs) {
      case  0 -> this.RT.v1.getXY();
      case  1 -> (this.RT.v2.x & 0xffffL) << 16 | this.RT.v1.z & 0xffffL;
      case  2 -> (this.RT.v2.z & 0xffffL) << 16 | this.RT.v2.y & 0xffffL;
      case  3 -> this.RT.v3.getXY();
      case  4 -> this.RT.v3.z;
      case  5 -> this.TRX;
      case  6 -> this.TRY;
      case  7 -> this.TRZ;
      case  8 -> this.LM.v1.getXY();
      case  9 -> (this.LM.v2.x & 0xffffL) << 16 | this.LM.v1.z & 0xffffL;
      case 10 -> (this.LM.v2.z & 0xffffL) << 16 | this.LM.v2.y & 0xffffL;
      case 11 -> this.LM.v3.getXY();
      case 12 -> this.LM.v3.z;
      case 13 -> this.RBK;
      case 14 -> this.GBK;
      case 15 -> this.BBK;
      case 16 -> this.LRGB.v1.getXY();
      case 17 -> (this.LRGB.v2.x & 0xffffL) << 16 | this.LRGB.v1.z & 0xffffL;
      case 18 -> (this.LRGB.v2.z & 0xffffL) << 16 | this.LRGB.v2.y & 0xffffL;
      case 19 -> this.LRGB.v3.getXY();
      case 20 -> this.LRGB.v3.z;
      case 21 -> this.RFC;
      case 22 -> this.GFC;
      case 23 -> this.BFC;
      case 24 -> this.OFX;
      case 25 -> this.OFY;
      case 26 -> this.H;
      case 27 -> this.DQA;
      case 28 -> this.DQB;
      case 29 -> this.ZSF3;
      case 30 -> this.ZSF4;
      case 31 -> this.FLAG;
      default -> 0xffff_ffffL;
    };
  }

  public void writeControl(final int fs, final int v) {
    switch(fs) {
      case 0 -> this.RT.v1.setXY(v);
      case 1 -> {
        this.RT.v1.z = (short)v;
        this.RT.v2.x = (short)(v >>> 16);
      }
      case 2 -> {
        this.RT.v2.y = (short)v;
        this.RT.v2.z = (short)(v >>> 16);
      }
      case 3 -> this.RT.v3.setXY(v);
      case 4 -> this.RT.v3.z = (short)v;
      case 5 -> this.TRX = v;
      case 6 -> this.TRY = v;
      case 7 -> this.TRZ = v;
      case 8 -> this.LM.v1.setXY(v);
      case 9 -> {
        this.LM.v1.z = (short)v;
        this.LM.v2.x = (short)(v >>> 16);
      }
      case 10 -> {
        this.LM.v2.y = (short)v;
        this.LM.v2.z = (short)(v >>> 16);
      }
      case 11 -> this.LM.v3.setXY(v);
      case 12 -> this.LM.v3.z = (short)v;
      case 13 -> this.RBK = v;
      case 14 -> this.GBK = v;
      case 15 -> this.BBK = v;
      case 16 -> this.LRGB.v1.setXY(v);
      case 17 -> {
        this.LRGB.v1.z = (short)v;
        this.LRGB.v2.x = (short)(v >>> 16);
      }
      case 18 -> {
        this.LRGB.v2.y = (short)v;
        this.LRGB.v2.z = (short)(v >>> 16);
      }
      case 19 -> this.LRGB.v3.setXY(v);
      case 20 -> this.LRGB.v3.z = (short)v;
      case 21 -> this.RFC = v;
      case 22 -> this.GFC = v;
      case 23 -> this.BFC = v;
      case 24 -> this.OFX = v;
      case 25 -> this.OFY = v;
      case 26 -> this.H = (short)v;
      case 27 -> this.DQA = (short)v;
      case 28 -> this.DQB = v;
      case 29 -> this.ZSF3 = (short)v;
      case 30 -> this.ZSF4 = (short)v;
      case 31 -> { //flag is u20 with 31 Error Flag (Bit30..23, and 18..13 ORed together)
        this.FLAG = v & 0x7fff_f000L;
        if((this.FLAG & 0x7f87_e000L) != 0) {
          this.FLAG |= 0x8000_0000L;
        }
      }
    }
  }

  public void dump(final ByteBuffer stream) {
    for(final ShortVector3 vec : this.V) {
      vec.dump(stream);
    }

    this.RGBC.dump(stream);
    IoHelper.write(stream, this.OTZ);

    for(final short ir : this.IR) {
      IoHelper.write(stream, ir);
    }

    for(final Vector2 v : this.SXY) {
      v.dump(stream);
    }

    for(final short sz : this.SZ) {
      IoHelper.write(stream, sz);
    }

    for(final Color c : this.RGB) {
      c.dump(stream);
    }

    IoHelper.write(stream, this.RES1);
    IoHelper.write(stream, this.MAC0);
    IoHelper.write(stream, this.MAC1);
    IoHelper.write(stream, this.MAC2);
    IoHelper.write(stream, this.MAC3);
    IoHelper.write(stream, this.IRGB);
    IoHelper.write(stream, this.LZCS);
    IoHelper.write(stream, this.LZCR);

    this.RT.dump(stream);
    this.LM.dump(stream);
    this.LRGB.dump(stream);
    IoHelper.write(stream, this.TRX);
    IoHelper.write(stream, this.TRY);
    IoHelper.write(stream, this.TRZ);
    IoHelper.write(stream, this.RBK);
    IoHelper.write(stream, this.GBK);
    IoHelper.write(stream, this.BBK);
    IoHelper.write(stream, this.RFC);
    IoHelper.write(stream, this.GFC);
    IoHelper.write(stream, this.BFC);
    IoHelper.write(stream, this.OFX);
    IoHelper.write(stream, this.OFY);
    IoHelper.write(stream, this.DQB);
    IoHelper.write(stream, this.H);
    IoHelper.write(stream, this.ZSF3);
    IoHelper.write(stream, this.ZSF4);
    IoHelper.write(stream, this.DQA);
    IoHelper.write(stream, this.FLAG);

    IoHelper.write(stream, this.sf);
    IoHelper.write(stream, this.lm);
    IoHelper.write(stream, this.currentCommand);
  }

  public void load(final ByteBuffer stream) {
    for(final ShortVector3 vec : this.V) {
      vec.load(stream);
    }

    this.RGBC.load(stream);
    this.OTZ = IoHelper.readShort(stream);

    for(int i = 0; i < this.IR.length; i++) {
      this.IR[i] = IoHelper.readShort(stream);
    }

    for(final Vector2 v : this.SXY) {
      v.load(stream);
    }

    for(int i = 0; i < this.SZ.length; i++) {
      this.SZ[i] = IoHelper.readShort(stream);
    }

    for(final Color c : this.RGB) {
      c.load(stream);
    }

    this.RES1 = IoHelper.readInt(stream);
    this.MAC0 = IoHelper.readInt(stream);
    this.MAC1 = IoHelper.readInt(stream);
    this.MAC2 = IoHelper.readInt(stream);
    this.MAC3 = IoHelper.readInt(stream);
    this.IRGB = IoHelper.readShort(stream);
    this.LZCS = IoHelper.readInt(stream);
    this.LZCR = IoHelper.readInt(stream);

    this.RT.load(stream);
    this.LM.load(stream);
    this.LRGB.load(stream);
    this.TRX = IoHelper.readInt(stream);
    this.TRY = IoHelper.readInt(stream);
    this.TRZ = IoHelper.readInt(stream);
    this.RBK = IoHelper.readInt(stream);
    this.GBK = IoHelper.readInt(stream);
    this.BBK = IoHelper.readInt(stream);
    this.RFC = IoHelper.readInt(stream);
    this.GFC = IoHelper.readInt(stream);
    this.BFC = IoHelper.readInt(stream);
    this.OFX = IoHelper.readInt(stream);
    this.OFY = IoHelper.readInt(stream);
    this.DQB = IoHelper.readInt(stream);
    this.H = IoHelper.readShort(stream);
    this.ZSF3 = IoHelper.readShort(stream);
    this.ZSF4 = IoHelper.readShort(stream);
    this.DQA = IoHelper.readShort(stream);
    this.FLAG = IoHelper.readLong(stream);

    this.sf = IoHelper.readInt(stream);
    this.lm = IoHelper.readBool(stream);
    this.currentCommand = IoHelper.readInt(stream);
  }

  private void debug() {
    final StringBuilder gteDebug = new StringBuilder("GTE CONTROL\n");
    for(int i = 0; i < 32; i++) {
      gteDebug.append(String.format(" %02d: %08x", i, this.loadControl(i)));
      if((i + 1) % 4 == 0) {
        gteDebug.append('\n');
      }
    }

    gteDebug.append("GTE DATA\n");
    for(int i = 0; i < 32; i++) {
      gteDebug.append(String.format(" %02d: %08x", i, this.loadData(i)));
      if((i + 1) % 4 == 0) {
        gteDebug.append('\n');
      }
    }

    LOGGER.info(gteDebug.toString());
  }
}
