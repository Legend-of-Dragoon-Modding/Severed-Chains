package legend.lodmod.items;

public class MindPurifierItem extends RecoverStatusItem {
  public MindPurifierItem() {
    super(37, 10, 0x4e);
  }

  public MindPurifierItem(final int icon, final int price, final int status) { super(icon, price, status); }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 5;
  }
}
