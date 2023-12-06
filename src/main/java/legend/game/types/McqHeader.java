package legend.game.types;

import legend.game.unpacker.FileData;
import org.joml.Vector3i;

/** 0x2c bytes */
public class McqHeader {
  public static final long MAGIC_1 = 0x151_434dL;
  public static final long MAGIC_2 = 0x251_434dL;

  public final int magic_00;
  public final int imageDataOffset_04;
  public final int vramWidth_08;
  public final int vramHeight_0a;
  public final int clutX_0c;
  public final int clutY_0e;
  public final int u_10;
  public final int v_12;
  public final int screenWidth_14;
  public final int screenHeight_16;

  public final Vector3i colour0_18;

  public final Vector3i colour1_20;

  public final int screenOffsetX_28;
  public final int screenOffsetY_2a;

  public final FileData imageData;

  public McqHeader(final FileData data) {
    this.magic_00 = data.readInt(0x00);
    this.imageDataOffset_04 = data.readInt(0x04);
    this.vramWidth_08 = data.readShort(0x08);
    this.vramHeight_0a = data.readShort(0x0a);
    this.clutX_0c = data.readUShort(0x0c);
    this.clutY_0e = data.readUShort(0x0e);
    this.u_10 = data.readUShort(0x10);
    this.v_12 = data.readUShort(0x12);
    this.screenWidth_14 = data.readUShort(0x14);
    this.screenHeight_16 = data.readUShort(0x16);

    this.colour0_18 = data.readColour(0x18, new Vector3i());

    this.colour1_20 = data.readColour(0x20, new Vector3i());

    this.screenOffsetX_28 = data.readShort(0x28);
    this.screenOffsetY_2a = data.readShort(0x2a);

    this.imageData = data.slice(this.imageDataOffset_04);
  }
}
