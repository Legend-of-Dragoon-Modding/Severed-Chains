package legend.lodmod.items;

public class DepetrifierItem extends RecoverStatusItem {
  public DepetrifierItem() {
    super(37, 15, 0x1);
  }

  public DepetrifierItem(final int icon, final int price, final int status) { super(icon, price, status); }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 3;
  }
}
