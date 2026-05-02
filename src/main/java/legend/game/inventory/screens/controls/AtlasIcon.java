package legend.game.inventory.screens.controls;

import legend.core.gte.MV;
import legend.game.inventory.screens.Control;
import legend.game.textures.TextureAtlasIcon;

import javax.annotation.Nullable;

public class AtlasIcon extends Control {
  @Nullable
  private TextureAtlasIcon icon;
  private final MV transforms = new MV();

  public AtlasIcon() {
    this.setSize(32, 32);
  }

  public void setIcon(@Nullable final TextureAtlasIcon icon) {
    this.icon = icon;
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.icon != null) {
      this.transforms.transfer.set(x, y, this.getZ() * 4);
      this.transforms.scaling(this.getWidth(), this.getHeight(), 1.0f);
      this.icon.render(this.transforms);
    }
  }
}
