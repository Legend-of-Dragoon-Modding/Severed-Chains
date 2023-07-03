package legend.game.types;

import legend.game.unpacker.FileData;

public class SubmapCutInfo {
  public int earlyGameFile_00;
  public int lateGameFile_02;
  public short collisionAndTransitionOffset_04;
  public int collisionAndTransitionCount_06;

  public SubmapCutInfo(final FileData data) {
    this.earlyGameFile_00 = data.readUShort(0x0);
    this.lateGameFile_02 = data.readUShort(0x2);
    this.collisionAndTransitionOffset_04 = data.readShort(0x4);
    this.collisionAndTransitionCount_06 = data.readUShort(0x6);
  }
}
