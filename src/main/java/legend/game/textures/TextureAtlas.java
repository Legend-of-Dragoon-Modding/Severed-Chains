package legend.game.textures;

import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Texture;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Map;

import static legend.core.GameEngine.RENDERER;

public class TextureAtlas {
  public final Texture texture;
  private final Obj obj;

  private final Map<RegistryId, TextureAtlasIcon> icons;

  public TextureAtlas(final Texture texture, final Obj obj, final Map<RegistryId, TextureAtlasIcon> icons) {
    this.texture = texture;
    this.obj = obj;
    this.icons = icons;
  }

  public TextureAtlasIcon getIcon(final RegistryId id) {
    return this.icons.get(id);
  }

  protected QueuedModelStandard render(final MV transforms, final int vertex) {
    return RENDERER.queueOrthoModel(this.obj, transforms, QueuedModelStandard.class)
      .vertices(vertex, 4)
      .texture(this.texture)
      .useTextureAlpha();
  }

  public void delete() {
    this.obj.delete();
    this.texture.delete();
  }
}
