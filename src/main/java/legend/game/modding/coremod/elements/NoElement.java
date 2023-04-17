package legend.game.modding.coremod.elements;

import legend.game.characters.Element;

public class NoElement extends Element {
  public NoElement() {
    super(0);
  }

  @Override
  public int adjustElementalDamage(final int damage, final Element targetElement) {
    return damage;
  }

  @Override
  public int adjustDragoonSpaceDamage(final int damage, final Element dragoonSpaceElement) {
    return damage;
  }
}
