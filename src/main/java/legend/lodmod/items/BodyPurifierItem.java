package legend.lodmod.items;

import legend.game.inventory.ItemIcon;

public class BodyPurifierItem extends RecoverStatusItem {
  public BodyPurifierItem() {
    super(5, 0xb0);
  }

  public BodyPurifierItem(final ItemIcon icon, final int price, final int status) {
    super(icon, price, status);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 4;
  }
}
