package legend.game.types;

import legend.core.gpu.RECT;
import legend.core.gte.SVECTOR;
import legend.game.unpacker.FileData;

/** 0x24-bytes long */
public class EnvironmentStruct {
  public final SVECTOR svec_00;
  /** Possible values: 0x4e, 0x4f, "anything else" (it's an else branch with no condition, but it's always been 0 in the files I've seen). First in-game cutscene has 0x4e for regular background textures and 0 for everything else. */
  public short s_06;
  public final RECT pos_08;
  public short us_10;
  public short us_12;
  public final SVECTOR svec_14;
  public int ui_1c;
  public int tpage_20;
  public short clutY_22;

  public EnvironmentStruct(final FileData data) {
    this.svec_00 = data.readSvec3(0x0, new SVECTOR());
    this.s_06 = data.readShort(0x6);
    this.pos_08 = data.readRect(0x8, new RECT());
    this.us_10 = data.readShort(0x10);
    this.us_12 = data.readShort(0x12);
    this.svec_14 = data.readSvec3(0x14, new SVECTOR());
    this.ui_1c = data.readInt(0x1c);
    this.tpage_20 = data.readUShort(0x20);
    this.clutY_22 = data.readShort(0x22);
  }
}
