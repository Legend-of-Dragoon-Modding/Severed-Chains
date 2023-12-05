package legend.game.types;

import legend.game.unpacker.FileData;

public class AnmSpriteMetrics14 {
  public final int u_00;
  public final int v_01;
  public final int ofs_x_02;
  public final int ofs_y_03;
  public final int cba_04;
  public final int flag_06;
  public final int w_08;
  public final int h_0a;
  public final int rot_0c;
  public final int flag2_0e;
  public final int x_10;
  public final int y_12;

  public AnmSpriteMetrics14(final FileData data) {
    this.u_00 = data.readUByte(0x0);
    this.v_01 = data.readUByte(0x1);
    this.ofs_x_02 = data.readUByte(0x2);
    this.ofs_y_03 = data.readUByte(0x3);
    this.cba_04 = data.readUShort(0x4);
    this.flag_06 = data.readUShort(0x6);
    this.w_08 = data.readUShort(0x8);
    this.h_0a = data.readUShort(0xa);
    this.rot_0c = data.readUShort(0xc);
    this.flag2_0e = data.readUShort(0xe);
    this.x_10 = data.readUShort(0x10);
    this.y_12 = data.readUShort(0x12);
  }
}
