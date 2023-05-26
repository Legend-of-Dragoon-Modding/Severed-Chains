package legend.core.gpu;

import java.util.Arrays;
import java.util.Comparator;

public class VramTextureStitched extends VramTexture {
  /** Note: sorted by X */
  private final VramTexture[] textures;

  public VramTextureStitched(final VramTexture... textures) {
    super(textures[0].bpp, Rect4i.bound(getRects(textures)));
    this.textures = Arrays.copyOf(textures, textures.length);
    Arrays.sort(this.textures, Comparator.comparingInt(a -> a.rect.x()));
  }

  private static Rect4i[] getRects(final VramTexture[] textures) {
    final Rect4i[] rects = new Rect4i[textures.length];
    for(int i = 0; i < textures.length; i++) {
      rects[i] = textures[i].rect;
    }

    return rects;
  }

  @Override
  public int getTexel(final VramTexture palette, final int pageX, final int x, final int y) {
    for(final VramTexture texture : this.textures) {
      final int textureOffset = (texture.rect.x() - pageX) * this.bpp.widthScale;
      final int textureX = x - textureOffset;

      if(textureX >= 0 && textureX < texture.rect.w()) {
        return texture.getTexel(palette, pageX, x, y);
      }
    }

    throw new IllegalArgumentException("Texture does not contain pixel (%d, %d) in page %d".formatted(x, y, pageX));
  }

  @Override
  public int getTexel(final int pageX, final int x, final int y) {
    for(final VramTexture texture : this.textures) {
      final int textureOffset = (texture.rect.x() - pageX) * this.bpp.widthScale;
      final int textureX = x - textureOffset;

      if(textureX >= 0 && textureX < texture.rect.w()) {
        return texture.getTexel(pageX, x, y);
      }
    }

    throw new IllegalArgumentException("Texture does not contain pixel (%d, %d) in page %d".formatted(x, y, pageX));
  }

  @Override
  public int getPixel(final int x, final int y) {
    throw new IllegalStateException("Can't get raw pixel of stitched texture");
  }

  @Override
  public void setPixel(final int x, final int y, final int colour) {
    throw new IllegalStateException("Can't set raw pixel of stitched texture");
  }

  @Override
  public void copyRow(final int y, final int[] dest, final int destOffset) {

  }

  @Override
  public void getRegion(final Rect4i region, final int[] dest) {
    for(int i = this.textures.length - 1; i >= 0; i--) {
      final VramTexture texture = this.textures[i];

      if(texture.rect.contains(region.x(), region.y())) {
        texture.getRegion(region, dest);
        return;
      }
    }

    throw new IllegalArgumentException("Texture does not contain region %s".formatted(region));
  }

  @Override
  public void setRegion(final Rect4i region, final int[] src) {
    for(int i = this.textures.length - 1; i >= 0; i--) {
      final VramTexture texture = this.textures[i];

      if(texture.rect.contains(region.x(), region.y())) {
        texture.setRegion(region, src);
        return;
      }
    }

    throw new IllegalArgumentException("Texture does not contain region %s".formatted(region));
  }

  @Override
  public void fill(final int colour) {
    for(final VramTexture texture : this.textures) {
      texture.fill(colour);
    }
  }
}
