package legend.lodmod.characters;

import legend.game.inventory.ItemIcon;
import legend.game.types.Renderable58;
import legend.game.types.UiType;

import static legend.game.Menus.uiFile_800bdc3c;

public class CharacterIcon extends ItemIcon {
  protected CharacterIcon(final int icon) {
    super(icon);
  }

  @Override
  protected UiType getUiType() {
    return uiFile_800bdc3c.portraits_cfac();
  }

  @Override
  public Renderable58 renderManual(final int x, final int y, final int flags) {
    final Renderable58 renderable = super.renderManual(x, y, flags);
    renderable.tpage_2c = 0x1a;
    return renderable;
  }
}
