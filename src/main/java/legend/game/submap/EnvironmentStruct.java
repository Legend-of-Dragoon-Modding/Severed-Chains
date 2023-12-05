package legend.game.submap;

import legend.core.gpu.Rect4i;
import legend.game.unpacker.FileData;
import org.joml.Vector3f;

/** 0x24-bytes long */
public class EnvironmentStruct {
  public final Vector3f svec_00;
  /** Possible values: 0x4e, 0x4f, "anything else" (it's an else branch with no condition, but it's always been 0 in the files I've seen). First in-game cutscene has 0x4e for regular background textures and 0 for everything else. */
  public short s_06;
  public final Rect4i pos_08;
  public short textureOffsetX_10;
  public short textureOffsetY_12;
  public final Vector3f svec_14;
  public float ui_1c;
  public int tpage_20;
  /** Negative means translucent */
  public short clutY_22;

  public EnvironmentStruct(final FileData data) {
    this.svec_00 = data.readSvec3_0(0x0, new Vector3f());
    this.s_06 = data.readShort(0x6);
    this.pos_08 = data.readRect(0x8, new Rect4i());
    this.textureOffsetX_10 = data.readShort(0x10);
    this.textureOffsetY_12 = data.readShort(0x12);
    this.svec_14 = data.readSvec3_12(0x14, new Vector3f());
    this.ui_1c = data.readInt(0x1c) / (float)0x1000;
    this.tpage_20 = data.readUShort(0x20);
    this.clutY_22 = data.readShort(0x22);
  }
}
