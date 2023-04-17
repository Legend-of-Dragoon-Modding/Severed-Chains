package legend.game.modding.coremod.elements;

import legend.game.characters.Element;

public class ThunderElement extends Element {
  public ThunderElement() {
    super(0x10);
  }

  @Override
  public int adjustElementalDamage(final int damage, final Element targetElement) {
    return damage;
  }

  @Override
  public int adjustDragoonSpaceDamage(final int damage, final Element dragoonSpaceElement) {
    if(dragoonSpaceElement == this) {
      return damage * 150 / 100;
    }

    return damage;
  }
}
