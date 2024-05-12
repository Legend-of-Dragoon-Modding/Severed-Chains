package legend.game.submap;

import legend.core.gpu.Rect4i;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

public class EnvironmentTextureMetrics24 {
  public final Vector3f worldPosition_00;
  /** Possible values: 0x4e (background texture), 0x4f (never used), "anything else" (cutout; it's an else branch with no condition, but it's always 0). */
  public short tileType_06;
  public final Rect4i vramPos_08;
  public short textureOffsetX_10;
  public short textureOffsetY_12;
  public final Vector3f worldToScreenZTransformVector_14;
  public float screenZ;
  public int tpage_20;
  /** Negative means translucent */
  public short clutY_22;

  public EnvironmentTextureMetrics24(final FileData data) {
    this.worldPosition_00 = data.readSvec3_0(0x0, new Vector3f());
    this.tileType_06 = data.readShort(0x6);
    this.vramPos_08 = data.readRect(0x8, new Rect4i());
    this.textureOffsetX_10 = data.readShort(0x10);
    this.textureOffsetY_12 = data.readShort(0x12);
    this.worldToScreenZTransformVector_14 = data.readSvec3_12(0x14, new Vector3f());
    this.screenZ = data.readInt(0x1c) / (float)0x1000;
    this.tpage_20 = data.readUShort(0x20);
    this.clutY_22 = data.readShort(0x22);
  }
}
