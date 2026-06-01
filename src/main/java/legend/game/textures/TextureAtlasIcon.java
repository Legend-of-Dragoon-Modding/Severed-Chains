package legend.game.textures;

import legend.core.QueuedModelStandard;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;

public class TextureAtlasIcon {
  public final TextureAtlas atlas;
  public final Rect4i rect;
  private final int vert;

  public TextureAtlasIcon(final TextureAtlas atlas, final Rect4i rect, final int vert) {
    this.atlas = atlas;
    this.rect = rect;
    this.vert = vert;
  }

  public QueuedModelStandard render(final MV transforms) {
    return this.atlas.render(transforms, this.vert);
  }
}
