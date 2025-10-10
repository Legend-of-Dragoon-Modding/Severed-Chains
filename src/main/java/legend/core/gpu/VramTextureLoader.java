package legend.core.gpu;

import legend.core.MathHelper;
import legend.game.tim.Tim;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public final class VramTextureLoader {
  private VramTextureLoader() { }

  public static final VramTexture EMPTY = new VramTextureSingle(Bpp.BITS_15, new Rect4i(0, 0, 0, 0), new int[0]);

  public static VramTexture textureFromTim(final Tim tim) {
    if(!tim.hasClut()) {
      throw new RuntimeException("Not yet supported");
    }

    final FileData imageData = tim.getImageData();
    final Rect4i imageSize = tim.getImageRect();

    final Bpp bpp = tim.getBpp();
    final int width = imageSize.w * bpp.widthDivisor;
    final int height = imageSize.h;

    final int[] data = new int[width * height];

    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        data[y * width + x] = getPixel(imageData, imageSize.w, x, y, bpp);
      }
    }

    return new VramTextureSingle(bpp, new Rect4i(imageSize.x, imageSize.y, width, height), data);
  }

  /** Stitch textures using their absolute VRAM positions */
  public static VramTexture stitch(final VramTexture... textures) {
    return new VramTextureStitched(textures);
  }

  /** Builds a new texture containing the data of each texture in a row. Uses the minimum VRAM coordinates as the coordinates for the new texture. */
  public static VramTexture stitchHorizontal(final VramTexture... textures) {
    if(textures.length == 0) {
      return EMPTY;
    }

    int newWidth = textures[0].rect.w();
    int minVramX = textures[0].rect.x();
    int minVramY = textures[0].rect.y();
    for(int i = 1; i < textures.length; i++) {
      if(textures[0].rect.h() != textures[i].rect.h()) {
        throw new IllegalArgumentException("All textures must have the same height");
      }

      if(textures[0].bpp != textures[i].bpp) {
        throw new IllegalArgumentException("All textures must have the same BPP");
      }

      newWidth += textures[i].rect.w();

      if(textures[i].rect.x() < minVramX) {
        minVramX = textures[i].rect.x();
      }

      if(textures[i].rect.y() < minVramY) {
        minVramY = textures[i].rect.y();
      }
    }

    final int newHeight = textures[0].rect.h();
    final int[] newData = new int[newWidth * newHeight];

    for(int y = 0; y < newHeight; y++) {
      int x = 0;
      for(final VramTexture texture : textures) {
        texture.copyRow(y, newData, y * newWidth + x);
        x += texture.rect.w();
      }
    }

    return new VramTextureSingle(textures[0].bpp, new Rect4i(minVramX, minVramY, newWidth, newHeight), newData);
  }

  /** Builds a new texture containing the data of each texture in a column. Uses the minimum VRAM coordinates as the coordinates for the new texture. */
  public static VramTexture stitchVertical(final VramTexture... textures) {
    if(textures.length == 0) {
      return EMPTY;
    }

    int newHeight = textures[0].rect.h();
    int minVramX = textures[0].rect.x();
    int minVramY = textures[0].rect.y();
    for(int i = 1; i < textures.length; i++) {
      if(textures[0].rect.w() != textures[i].rect.w()) {
        throw new IllegalArgumentException("All textures must have the same width");
      }

      if(textures[0].bpp != textures[i].bpp) {
        throw new IllegalArgumentException("All textures must have the same BPP");
      }

      newHeight += textures[i].rect.h();

      if(textures[i].rect.x() < minVramX) {
        minVramX = textures[i].rect.x();
      }

      if(textures[i].rect.y() < minVramY) {
        minVramY = textures[i].rect.y();
      }
    }

    final int newWidth = textures[0].rect.w();
    final int[] newData = new int[newWidth * newHeight];

    int y2 = 0;
    for(final VramTexture texture : textures) {
      for(int y = 0; y < texture.rect.h(); y++, y2++) {
        texture.copyRow(y, newData, y2 * newWidth);
      }
    }

    return new VramTextureSingle(textures[0].bpp, new Rect4i(minVramX, minVramY, newWidth, newHeight), newData);
  }

  public static VramTexture[] palettesFromTim(final Tim tim) {
    if(!tim.hasClut()) {
      throw new RuntimeException("Not yet supported");
    }

    final FileData clutData = tim.getClutData();
    final Rect4i clutSize = tim.getClutRect();
    final int paletteCount = clutSize.h;
    final int width = clutSize.w;

    final VramTexture[] palettes = new VramTexture[paletteCount];

    for(int paletteIndex = 0; paletteIndex < paletteCount; paletteIndex++) {
      final int[] data = new int[width];

      for(int x = 0; x < width; x++) {
        data[x] = MathHelper.colour15To24(getPixel(clutData, width, x, paletteIndex));
      }

      palettes[paletteIndex] = new VramTextureSingle(tim.getBpp(), new Rect4i(0, clutSize.y + paletteIndex, width, 1), data);
    }

    return palettes;
  }

  public static VramTexture[] palettesFromTims(final Tim... tims) {
    final VramTexture[][] palettes = new VramTexture[tims.length][];
    Arrays.setAll(palettes, i -> palettesFromTim(tims[i]));
    return Arrays.stream(palettes).flatMap(Arrays::stream).toArray(VramTexture[]::new);
  }

  private static int getPixel(final FileData data, final int width, final int x, final int y) {
    return data.readUShort((y * width + x) * 2);
  }

  private static int getPixel(final FileData imageData, final int width, final int x, final int y, final Bpp depth) {
    if(depth == Bpp.BITS_4) {
      return get4bppPixel(imageData, width, x, y);
    }

    if(depth == Bpp.BITS_8) {
      return get8bppPixel(imageData, width, x, y);
    }

    return get16bppPixel(imageData, width, x, y);
  }

  private static int get4bppPixel(final FileData imageData, final int width, final int x, final int y) {
    final int index = getPixel(imageData, width, x / 4, y);
    return index >> (x & 3) * 4 & 0xf;
  }

  private static int get8bppPixel(final FileData imageData, final int width, final int x, final int y) {
    final int index = getPixel(imageData, width, x / 2, y);
    return index >> (x & 1) * 8 & 0xff;
  }

  private static int get16bppPixel(final FileData imageData, final int width, final int x, final int y) {
    return MathHelper.colour15To24(getPixel(imageData, width, x, y));
  }
}
