package legend.lodmod;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.core.gpu.Bpp;
import legend.core.renderer.QueuedModelStandard;
import legend.game.inventory.ItemIcon;
import legend.game.textures.TextureAtlas;
import legend.game.textures.TextureAtlasIcon;
import legend.game.types.RenderableMetrics14;
import legend.game.types.UiPart;
import legend.game.types.UiType;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.getTextureAtlas;

public class GoodsIcon extends ItemIcon {
  public GoodsIcon(final RegistryId id) {
    super(idMap.getInt(id));
  }

  @Override
  protected UiType getUiType() {
    return getType();
  }

  private static UiType type;
  private static final Object2IntMap<RegistryId> idMap = new Object2IntOpenHashMap<>();

  public static UiType getType() {
    if(type == null) {
      type = loadType();
    }

    return type;
  }

  static void clear() {
    type = null;
    idMap.clear();
  }

  private static UiType loadType() {
    final TextureAtlas textureAtlas = getTextureAtlas();
    final float textureWidth = textureAtlas.texture.width;
    final float textureHeight = textureAtlas.texture.height;

    final List<Metrics> metrics = new ArrayList<>();

    for(final RegistryId id : REGISTRIES.goods) {
      final TextureAtlasIcon icon = getTextureAtlas().getIcon(id);

      if(icon != null) {
        idMap.put(id, metrics.size());
        metrics.add(new Metrics(icon.rect.x / textureWidth, icon.rect.y / textureHeight, 0, 0, 16, 16, icon.rect.w / textureWidth, icon.rect.h / textureHeight));
      }
    }

    final UiPart[] parts = metrics.stream()
      .map(metric -> new UiPart(new Metrics[] {metric}, 1))
      .toArray(UiPart[]::new)
    ;

    return new UiType(parts);
  }

  public static class Metrics extends RenderableMetrics14 {
    public Metrics(final float u, final float v, final int x, final int y, final int width, final int height, final float textureWidth, final float textureHeight) {
      super(u, v, x, y, 0, Bpp.BITS_24.ordinal() << 7, width, height, textureWidth, textureHeight);
    }

    @Override
    public void useTexture(final QueuedModelStandard model) {
      model
        .texture(getTextureAtlas().texture)
        .useTextureAlpha()
      ;
    }
  }
}
