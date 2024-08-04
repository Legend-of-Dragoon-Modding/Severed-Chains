package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;
import org.joml.Vector3f;

public class ThunderElement extends Element {
  public ThunderElement() {
    super(0x10, new Vector3f(0x81 / 255.0f, 0x9 / 255.0f, 0xec / 255.0f));
  }

  @Override
  public int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement) {
    if(targetElement == this) {
      return damage * 50 / 100;
    }

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
