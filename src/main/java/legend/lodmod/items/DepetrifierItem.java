package legend.lodmod.items;

import legend.game.inventory.ItemIcon;

public class DepetrifierItem extends RecoverStatusItem {
  public DepetrifierItem() {
    super(15, 0x1);
  }

  public DepetrifierItem(final ItemIcon icon, final int price, final int status) {
    super(icon, price, status);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 3;
  }
}
