package legend.game.modding.coremod.elements;

import legend.game.characters.Element;

public class DivineElement extends Element {
  public DivineElement() {
    super(0x8);
  }

  @Override
  public int adjustElementalDamage(final int damage, final Element targetElement) {
    return 100;
  }

  @Override
  public int adjustDragoonSpaceDamage(final int damage, final Element dragoonSpaceElement) {
    if(dragoonSpaceElement == this) {
      return damage * 150 / 100;
    }

    return damage;
  }
}
