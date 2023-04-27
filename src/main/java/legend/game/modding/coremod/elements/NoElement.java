package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;

public class NoElement extends Element {
  public NoElement() {
    super(0, 0xffffff);
  }

  @Override
  public int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement) {
    return damage;
  }

  @Override
  public int adjustDragoonSpaceDamage(final AttackType attackType, final int damage, final Element attackingElement) {
    return damage;
  }
}
