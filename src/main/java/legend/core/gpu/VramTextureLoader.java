package legend.core.gpu;

import legend.core.MathHelper;
import legend.game.tim.Tim;
import legend.game.unpacker.FileData;

public final class VramTextureLoader {
  private VramTextureLoader() { }

  public static final VramTexture EMPTY = new VramTexture(0, 0, new int[0][0]);

  public static VramTexture fromTim(final Tim tim) {
    final FileData imageData = tim.getImageData();
    final RECT imageSize = tim.getImageRect();

    if(!tim.hasClut()) {
      throw new RuntimeException("Not yet supported");
    }

    final Bpp bpp = tim.getBpp();
    final int width = imageSize.w.get() * bpp.widthScale;
    final int height = imageSize.h.get();

    final FileData clutData = tim.getClutData();
    final RECT clutSize = tim.getClutRect();

    final int[][] data = new int[clutSize.h.get()][width * height];

    for(int palette = 0; palette < clutSize.h.get(); palette++) {
      for(int y = 0; y < height; y++) {
        for(int x = 0; x < width; x++) {
          data[palette][y * width + x] = MathHelper.colour15To24(getTexel(imageData, clutData, imageSize.w.get(), palette, x, y, bpp));
        }
      }
    }

    return new VramTexture(width, height, data, new VramTexture.PaletteRegion(new Rect4i(0, 0, width, height), clutSize.y.get()));
  }

  public static VramTexture stitchHorizontal(final VramTexture... textures) {
    if(textures.length == 0) {
      return EMPTY;
    }

    int newWidth = textures[0].width;
    int paletteRegionsCount = textures[0].paletteRegions.length;
    for(int i = 1; i < textures.length; i++) {
      if(textures[0].palettes != textures[i].palettes) {
        throw new IllegalArgumentException("All textures must have the same number of palettes");
      }

      if(textures[0].height != textures[i].height) {
        throw new IllegalArgumentException("All textures must have the same height");
      }

      newWidth += textures[i].width;
      paletteRegionsCount += textures[i].paletteRegions.length;
    }

    final int newPalettes = textures[0].palettes;
    final int newHeight = textures[0].height;
    final int[][] newData = new int[newPalettes][newWidth * newHeight];

    for(int palette = 0; palette < newPalettes; palette++) {
      for(int y = 0; y < newHeight; y++) {
        int x = 0;
        for(final VramTexture texture : textures) {
          texture.copyRow(palette, y, newData[palette], y * newWidth + x);
          x += texture.width;
        }
      }
    }

    final VramTexture.PaletteRegion[] paletteRegions = new VramTexture.PaletteRegion[paletteRegionsCount];
    int paletteRegionIndex = 0;
    int x = 0;
    for(final VramTexture texture : textures) {
      for(final VramTexture.PaletteRegion oldRegion : texture.paletteRegions) {
        paletteRegions[paletteRegionIndex++] = new VramTexture.PaletteRegion(new Rect4i(oldRegion.rect().x() + x, oldRegion.rect().y(), oldRegion.rect().w(), oldRegion.rect().h()), oldRegion.offset());
      }

      x += texture.width;
    }

    return new VramTexture(newWidth, newHeight, newData, paletteRegions);
  }

  public static VramTexture stitchVertical(final VramTexture... textures) {
    if(textures.length == 0) {
      return EMPTY;
    }

    int newHeight = textures[0].height;
    int paletteRegionsCount = textures[0].paletteRegions.length;
    for(int i = 1; i < textures.length; i++) {
      if(textures[0].palettes != textures[i].palettes) {
        throw new IllegalArgumentException("All textures must have the same number of palettes");
      }

      if(textures[0].width != textures[i].width) {
        throw new IllegalArgumentException("All textures must have the same width");
      }

      newHeight += textures[i].height;
      paletteRegionsCount += textures[i].paletteRegions.length;
    }

    final int newPalettes = textures[0].palettes;
    final int newWidth = textures[0].width;
    final int[][] newData = new int[newPalettes][newWidth * newHeight];

    for(int palette = 0; palette < newPalettes; palette++) {
      int y2 = 0;
      for(final VramTexture texture : textures) {
        for(int y = 0; y < texture.height; y++, y2++) {
          texture.copyRow(palette, y, newData[palette], y2 * newWidth);
        }
      }
    }

    final VramTexture.PaletteRegion[] paletteRegions = new VramTexture.PaletteRegion[paletteRegionsCount];
    int paletteRegionIndex = 0;
    int y = 0;
    for(final VramTexture texture : textures) {
      for(final VramTexture.PaletteRegion oldRegion : texture.paletteRegions) {
        paletteRegions[paletteRegionIndex++] = new VramTexture.PaletteRegion(new Rect4i(oldRegion.rect().x(), oldRegion.rect().y() + y, oldRegion.rect().w(), oldRegion.rect().h()), oldRegion.offset());
      }

      y += texture.height;
    }

    return new VramTexture(newWidth, newHeight, newData, paletteRegions);
  }

  private static int getPixel(final FileData data, final int width, final int x, final int y) {
    return data.readUShort((y * width + x) * 2);
  }

  public static int getTexel(final FileData imageData, final FileData clutData, final int width, final int palette, final int x, final int y, final Bpp depth) {
    if(depth == Bpp.BITS_4) {
      return get4bppTexel(imageData, clutData, width, palette, x, y);
    }

    if(depth == Bpp.BITS_8) {
      return get8bppTexel(imageData, clutData, width, palette, x, y);
    }

    return get16bppTexel(imageData, width, x, y);
  }

  private static int get4bppTexel(final FileData imageData, final FileData clutData, final int width, final int palette, final int x, final int y) {
    final int index = getPixel(imageData, width, x / 4, y);
    final int p = index >> (x & 3) * 4 & 0xf;
    return getPixel(clutData, width, palette * 16 + p, 0);
  }

  private static int get8bppTexel(final FileData imageData, final FileData clutData, final int width, final int palette, final int x, final int y) {
    final int index = getPixel(imageData, width, x / 2, y);
    final int p = index >> (x & 1) * 8 & 0xff;
    return getPixel(clutData, width, palette * 256 + p, 0);
  }

  private static int get16bppTexel(final FileData imageData, final int width, final int x, final int y) {
    return getPixel(imageData, width, x, y);
  }
}
