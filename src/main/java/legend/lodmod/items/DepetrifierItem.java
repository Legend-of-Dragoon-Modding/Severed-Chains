package legend.lodmod.items;

public class DepetrifierItem extends RecoverStatusItem {
  public DepetrifierItem() {
    super(15, 0x1);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 3;
  }
}
