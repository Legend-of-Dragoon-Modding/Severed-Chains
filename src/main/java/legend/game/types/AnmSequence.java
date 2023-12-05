package legend.game.types;

import legend.game.unpacker.FileData;

public class AnmSequence {
  public final int spriteGroupNumber_00;
  public final int time_02;
  public final int attr_03;
  public final int x_04;
  public final int y_06;

  public AnmSequence(final FileData data) {
    this.spriteGroupNumber_00 = data.readUShort(0x0);
    this.time_02 = data.readUByte(0x2);
    this.attr_03 = data.readUByte(0x3);
    this.x_04 = data.readUShort(0x4);
    this.y_06 = data.readUShort(0x6);
  }
}
