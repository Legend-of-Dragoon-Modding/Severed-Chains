package legend.lodmod.items;

public class BodyPurifierItem extends RecoverStatusItem {
  public BodyPurifierItem() {
    super(5, 0xb0);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 4;
  }
}
