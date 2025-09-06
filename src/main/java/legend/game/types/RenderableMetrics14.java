package legend.game.types;

import legend.core.QueuedModelStandard;
import legend.game.unpacker.FileData;

public class RenderableMetrics14 {
  /** ubyte */
  public final float u_00;
  /** ubyte */
  public final float v_01;
  /** ubyte */
  public final int x_02;
  /** ubyte */
  public final int y_03;
  /** MSB enables translucency (ushort) */
  public final int clut_04;
  /** ushort */
  public final int tpage_06;
  /** ushort */
  public final int width_08;
  /** ushort */
  public final int height_0a;
  public final float textureWidth;
  public final float textureHeight;
  public final float widthScale_10;
  public final float heightScale_12;

  public int vertexStart;

  public RenderableMetrics14(final float u, final float v, final int x, final int y, final int clut, final int tpage, final int width, final int height, final float textureWidth, final float textureHeight, final float widthScale, final float heightScale) {
    this.u_00 = u;
    this.v_01 = v;
    this.x_02 = x;
    this.y_03 = y;
    this.clut_04 = clut;
    this.tpage_06 = tpage;
    this.width_08 = width;
    this.height_0a = height;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.widthScale_10 = widthScale;
    this.heightScale_12 = heightScale;
  }

  public static RenderableMetrics14 fromFile(final FileData data) {
    final int u = data.readUByte(0x0);
    final int v = data.readUByte(0x1);
    final int x = data.readUByte(0x2);
    final int y = data.readUByte(0x3);
    final int clut = data.readUShort(0x4);
    final int tpage = data.readUShort(0x6);
    final int width = data.readUShort(0x8);
    final int height = data.readUShort(0xa);

    final float widthScale = data.readShort(0x10) / (float)0x1000;
    final float heightScale = data.readShort(0x12) / (float)0x1000;

    return new RenderableMetrics14(u, v, x, y, clut, tpage, width, height, width, height, widthScale, heightScale);
  }

  public RenderableMetrics14(final float u, final float v, final int x, final int y, final int clut, final int tpage, final int width, final int height, final float textureWidth, final float textureHeight) {
    this(u, v, x, y, clut, tpage, width, height, textureWidth, textureHeight, 1.0f, 1.0f);
  }

  public void useTexture(final QueuedModelStandard model) {

  }
}
