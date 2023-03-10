package legend.core.gpu;

import legend.core.MathHelper;
import legend.game.tim.Tim;
import legend.game.unpacker.FileData;

import java.util.Arrays;

public final class VramTextureLoader2 {
  private VramTextureLoader2() { }

  public static final VramTexture2 EMPTY = new VramTexture2(0, 0, 0, 0, new int[0]);

  public static VramTexture2 textureFromTim(final Tim tim) {
    if(!tim.hasClut()) {
      throw new RuntimeException("Not yet supported");
    }

    final FileData imageData = tim.getImageData();
    final RECT imageSize = tim.getImageRect();

    final Bpp bpp = tim.getBpp();
    final int width = imageSize.w.get() * bpp.widthScale;
    final int height = imageSize.h.get();

    final int[] data = new int[width * height];

    for(int y = 0; y < height; y++) {
      for(int x = 0; x < width; x++) {
        data[y * width + x] = getPixel(imageData, imageSize.w.get(), x, y, bpp);
      }
    }

    return new VramTexture2(width, height, imageSize.x.get(), imageSize.y.get(), data);
  }

  public static VramTexture2 stitchHorizontal(final VramTexture2... textures) {
    if(textures.length == 0) {
      return EMPTY;
    }

    int newWidth = textures[0].width;
    int minVramX = textures[0].vramX;
    int minVramY = textures[0].vramY;
    for(int i = 1; i < textures.length; i++) {
      if(textures[0].height != textures[i].height) {
        throw new IllegalArgumentException("All textures must have the same height");
      }

      newWidth += textures[i].width;

      if(textures[i].vramX < minVramX) {
        minVramX = textures[i].vramX;
      }

      if(textures[i].vramY < minVramY) {
        minVramY = textures[i].vramY;
      }
    }

    final int newHeight = textures[0].height;
    final int[] newData = new int[newWidth * newHeight];

    for(int y = 0; y < newHeight; y++) {
      int x = 0;
      for(final VramTexture2 texture : textures) {
        texture.copyRow(y, newData, y * newWidth + x);
        x += texture.width;
      }
    }

    return new VramTexture2(newWidth, newHeight, minVramX, minVramY, newData);
  }

  public static VramTexture2 stitchVertical(final VramTexture2... textures) {
    if(textures.length == 0) {
      return EMPTY;
    }

    int newHeight = textures[0].height;
    int minVramX = textures[0].vramX;
    int minVramY = textures[0].vramY;
    for(int i = 1; i < textures.length; i++) {
      if(textures[0].width != textures[i].width) {
        throw new IllegalArgumentException("All textures must have the same width");
      }

      newHeight += textures[i].height;

      if(textures[i].vramX < minVramX) {
        minVramX = textures[i].vramX;
      }

      if(textures[i].vramY < minVramY) {
        minVramY = textures[i].vramY;
      }
    }

    final int newWidth = textures[0].width;
    final int[] newData = new int[newWidth * newHeight];

    int y2 = 0;
    for(final VramTexture2 texture : textures) {
      for(int y = 0; y < texture.height; y++, y2++) {
        texture.copyRow(y, newData, y2 * newWidth);
      }
    }

    return new VramTexture2(newWidth, newHeight, minVramX, minVramY, newData);
  }

  public static VramTexture2[] palettesFromTim(final Tim tim) {
    if(!tim.hasClut()) {
      throw new RuntimeException("Not yet supported");
    }

    final FileData clutData = tim.getClutData();
    final RECT clutSize = tim.getClutRect();
    final int paletteCount = clutSize.h.get();
    final int width = clutSize.w.get();

    final VramTexture2[] palettes = new VramTexture2[paletteCount];

    for(int paletteIndex = 0; paletteIndex < paletteCount; paletteIndex++) {
      final int[] data = new int[width];

      for(int x = 0; x < width; x++) {
        data[x] = MathHelper.colour15To24(getPixel(clutData, width, x, paletteIndex));
      }

      palettes[paletteIndex] = new VramTexture2(width, 1, 0, clutSize.y.get() + paletteIndex, data);
    }

    return palettes;
  }

  public static VramTexture2[] palettesFromTims(final Tim... tims) {
    final VramTexture2[][] palettes = new VramTexture2[tims.length][];
    Arrays.setAll(palettes, i -> palettesFromTim(tims[i]));
    return Arrays.stream(palettes).flatMap(Arrays::stream).toArray(VramTexture2[]::new);
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
