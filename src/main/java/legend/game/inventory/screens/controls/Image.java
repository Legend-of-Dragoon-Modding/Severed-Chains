package legend.game.inventory.screens.controls;

import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.game.inventory.screens.Control;

import static legend.core.GameEngine.RENDERER;

public class Image extends Control {
  private Obj obj;
  private final MV transforms = new MV();

  private boolean translucent;
  private Texture texture;
  private int u;
  private int v;
  private int w;
  private int h;

  public void setTranslucent(final boolean translucent) {
    this.translucent = translucent;
  }

  public void setTexture(final Texture texture) {
    if(this.obj != null && texture != this.texture) {
      this.obj.delete();
      this.obj = null;
    }

    this.texture = texture;
  }

  public void setUv(final int u, final int v, final int w, final int h) {
    if(this.obj != null && (u != this.u || v != this.v || w != this.w || h != this.h)) {
      this.obj.delete();
      this.obj = null;
    }

    this.u = u;
    this.v = v;
    this.w = w;
    this.h = h;
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.obj == null && this.texture != null && this.w != 0 && this.h != 0) {
      this.obj = new QuadBuilder("UI image " + this.texture)
        .bpp(Bpp.BITS_24)
        .posSize(1.0f, 1.0f)
        .uv(this.u / (float)this.texture.width, this.v / (float)this.texture.height)
        .uvSize(this.w / (float)this.texture.width, this.h / (float)this.texture.height)
        .build();
    }

    if(this.obj != null) {
      this.transforms.scaling(this.getWidth(), this.getHeight(), 1.0f);
      this.transforms.transfer.set(x, y, this.getZ());

      final RenderEngine.QueuedModel<?> model = RENDERER.queueOrthoModel(this.obj, this.transforms)
        .texture(this.texture);

      if(this.translucent) {
        model.realTranslucency();
      }
    }
  }

  @Override
  protected void delete() {
    super.delete();

    if(this.obj != null) {
      this.obj.delete();
    }
  }
}
