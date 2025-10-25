package legend.lodmod.items;

import legend.game.inventory.ItemIcon;

public class MindPurifierItem extends RecoverStatusItem {
  public MindPurifierItem() {
    super(10, 0x4e);
  }

  public MindPurifierItem(final ItemIcon icon, final int price, final int status) {
    super(icon, price, status);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 5;
  }
}
