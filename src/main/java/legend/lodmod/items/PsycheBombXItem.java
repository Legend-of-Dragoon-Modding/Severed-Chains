package legend.lodmod.items;

import legend.game.characters.Element;
import legend.lodmod.LodMod;

public class PsycheBombXItem extends AttackItem {
  public PsycheBombXItem() {
    super(38, 200, true, LodMod.NO_ELEMENT.get(), 0x20);
  }

  public PsycheBombXItem(final int icon, final int price, final boolean targetAll, final Element element, final int damageMultiplier) {
    super(icon, price, targetAll, element, damageMultiplier);
  }

  @Override
  public boolean isRepeat() {
    return true;
  }

  @Override
  public boolean isProtected() {
    return true;
  }
}
