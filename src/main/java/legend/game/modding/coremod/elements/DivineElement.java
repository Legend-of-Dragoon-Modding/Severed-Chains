package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;
import org.joml.Vector3f;

public class DivineElement extends Element {
  public DivineElement() {
    super(0x8, new Vector3f(0.5f, 0.5f, 0.5f));
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
