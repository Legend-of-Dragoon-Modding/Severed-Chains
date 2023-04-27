package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;

public class DivineElement extends Element {
  public DivineElement() {
    super(0x8, 0x808080);
  }

  @Override
  public int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement) {
    return damage;
  }

  @Override
  public int adjustDragoonSpaceDamage(final AttackType attackType, final int damage, final Element attackingElement) {
    if(attackingElement == this) {
      return damage * 150 / 100;
    }

    return damage;
  }
}
