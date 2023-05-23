package legend.game.inventory.screens;

import legend.core.gpu.Renderable;

import java.util.List;

public class TextRenderable {
  private final List<Renderable> renderables;
  public final int width;
  public final int height;

  public TextRenderable(final List<Renderable> renderables, final int width, final int height) {
    this.renderables = renderables;
    this.width = width;
    this.height = height;
  }

  public void render(final int x, final int y, final int z) {
    for(final Renderable renderable : this.renderables) {
      renderable.render(x, y, z);
    }
  }

  public void render(final int x, final int y, final int z, final int colour) {
    for(final Renderable renderable : this.renderables) {
      renderable.recolour(colour).render(x, y, z);
    }
  }

  public void render() {
    for(final Renderable renderable : this.renderables) {
      renderable.render();
    }
  }
}
