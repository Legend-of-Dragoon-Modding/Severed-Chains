package legend.game.characters;

import legend.core.GameEngine;
import legend.game.combat.types.AttackType;
import legend.lodmod.LodMod;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

public abstract class Element extends RegistryEntry {
  /** TODO figure out a way to remove this, mods will add elements with conflicting flags */
  @Deprecated
  public final int flag;

  public final Vector3f colour;

  @Deprecated
  public static Element fromFlag(final int flag) {
    if(flag == 0) {
      return LodMod.NO_ELEMENT.get();
    }

    for(final RegistryId elementId : GameEngine.REGISTRIES.elements) {
      final Element element = GameEngine.REGISTRIES.elements.getEntry(elementId).get();

      if((flag & element.flag) != 0) {
        return element;
      }
    }

    throw new IllegalArgumentException("Unknown element %x".formatted(flag));
  }

  public Element(final int flag, final Vector3f colour) {
    this.flag = flag;
    this.colour = colour;
  }

  public abstract int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement);
  public int adjustDefendingElementalDamage(final AttackType attackType, final int damage, final Element attackerElement) { return damage; }
  public abstract int adjustDragoonSpaceDamage(final AttackType attackType, final int damage, final Element attackingElement);
}
