package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.modding.registries.RegistryDelegate;

public class SimpleElement extends Element {
  private final RegistryDelegate<Element> opposed;

  public SimpleElement(final int flag, final RegistryDelegate<Element> opposed) {
    super(flag);
    this.opposed = opposed;
  }

  @Override
  public int adjustElementalDamage(final int damage, final Element targetElement) {
    if(targetElement == this.opposed.get()) {
      return damage * 150 / 100;
    }

    if(targetElement == this) {
      return damage * 50 / 100;
    }

    return damage;
  }

  @Override
  public int adjustDragoonSpaceDamage(final int damage, final Element dragoonSpaceElement) {
    if(dragoonSpaceElement == this) {
      return damage * 150 / 100;
    }

    if(dragoonSpaceElement == this.opposed.get()) {
      return damage * 50 / 100;
    }

    return damage;
  }
}
