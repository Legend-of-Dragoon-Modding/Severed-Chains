package legend.lodmod.items;

public class MindPurifierItem extends RecoverStatusItem {
  public MindPurifierItem() {
    super(10, 0x4e);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 5;
  }
}
