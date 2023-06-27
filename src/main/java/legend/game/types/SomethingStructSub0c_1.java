package legend.game.types;

import legend.game.unpacker.FileData;

public class SomethingStructSub0c_1 {
  public int count_00;
  public boolean bool_01;
  public int _02;
  public int primitivesOffset_04;
  public int _08;

  public SomethingStructSub0c_1(final FileData data) {
    this.count_00 = data.readUByte(0x00);
    this.bool_01 = data.readByte(0x01) != 0;
    this._02 = data.readUShort(0x02);
    this.primitivesOffset_04 = data.readInt(0x04);
    this._08 = data.readInt(0x08);
  }
}
