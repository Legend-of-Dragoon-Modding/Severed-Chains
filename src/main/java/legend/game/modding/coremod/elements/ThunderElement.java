package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;

public class ThunderElement extends Element {
  public ThunderElement() {
    super(0x10, 0x8109ec);
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
