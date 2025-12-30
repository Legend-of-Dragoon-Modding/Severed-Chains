package legend.game.inventory;

import legend.game.types.UiType;

import static legend.game.Menus.uiFile_800bdc3c;

public class DragoonSpiritIcon extends ItemIcon {
  protected DragoonSpiritIcon(final int icon) {
    super(icon);
  }

  @Override
  protected UiType getUiType() {
    return uiFile_800bdc3c.uiElements_0000();
  }
}
