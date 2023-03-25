package legend.game.types;

import legend.game.unpacker.FileData;

/**
 * @param u_00      ubyte
 * @param v_01      ubyte
 * @param x_02      ubyte
 * @param y_03      ubyte
 * @param clut_04   MSB enables translucency (ushort)
 * @param tpage_06  ushort
 * @param width_08  ushort
 * @param height_0a ushort
 */
public record RenderableMetrics14(int u_00, int v_01, int x_02, int y_03, int clut_04, int tpage_06, int width_08, int height_0a, int textureWidth, int textureHeight, short _10, short _12) {
  public static RenderableMetrics14 fromFile(final FileData data) {
    final int u = data.readUByte(0x0);
    final int v = data.readUByte(0x1);
    final int x = data.readUByte(0x2);
    final int y = data.readUByte(0x3);
    final int clut = data.readUShort(0x4);
    final int tpage = data.readUShort(0x6);
    final int width = data.readUShort(0x8);
    final int height = data.readUShort(0xa);

    final short _10 = data.readShort(0x10);
    final short _12 = data.readShort(0x12);

    return new RenderableMetrics14(u, v, x, y, clut, tpage, width, height, width, height, _10, _12);
  }
}
