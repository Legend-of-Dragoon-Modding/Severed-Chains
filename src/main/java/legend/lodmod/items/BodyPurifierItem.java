package legend.lodmod.items;

public class BodyPurifierItem extends RecoverStatusItem {
  public BodyPurifierItem() {
    super(37, 5, 0xb0);
  }

  public BodyPurifierItem(final int icon, final int price, final int status) { super(icon, price, status); }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 4;
  }
}
