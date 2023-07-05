package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public class EnvironmentFile {
  public final SVECTOR viewpoint_00;
  public final SVECTOR refpoint_08;
  public int projectionDistance_10;
  public short rotation_12;
  public int count_14;
  public int ub_15;
  public int ub_16;
  public final EnvironmentStruct[] environments_18;

  public EnvironmentFile(final FileData data) {
    this.viewpoint_00 = data.readSvec3(0x0, new SVECTOR());
    this.refpoint_08 = data.readSvec3(0x8, new SVECTOR());
    this.projectionDistance_10 = data.readUShort(0x10);
    this.rotation_12 = data.readShort(0x12);
    this.count_14 = data.readUByte(0x14);
    this.ub_15 = data.readUByte(0x15);
    this.ub_16 = data.readUByte(0x16);
    this.environments_18 = new EnvironmentStruct[this.count_14];
    Arrays.setAll(this.environments_18, i -> new EnvironmentStruct(data.slice(0x18 + i * 0x24)));
  }
}
