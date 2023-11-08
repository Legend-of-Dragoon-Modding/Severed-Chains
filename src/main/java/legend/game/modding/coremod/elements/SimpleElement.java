package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import legend.game.combat.types.AttackType;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public class SimpleElement extends Element {
  private final RegistryDelegate<Element> opposed;

  public SimpleElement(final int flag, final Vector3f colour, final RegistryDelegate<Element> opposed) {
    super(flag, colour);
    this.opposed = opposed;
  }

  @Override
  public int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement) {
    if(targetElement == this.opposed.get()) {
      return damage * 150 / 100;
    }

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

    if(attackingElement == this.opposed.get()) {
      return damage * 50 / 100;
    }

    return damage;
  }
}
