package legend.game.characters;

import legend.core.GameEngine;
import legend.game.combat.types.AttackType;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;
import legend.lodmod.LodMod;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

public abstract class Element extends RegistryEntry implements ScriptReadable {
  /** TODO figure out a way to remove this, mods will add elements with conflicting flags */
  @Deprecated
  public final int flag;

  public final Vector3f colour;

  @Deprecated
  public static RegistryDelegate<Element> fromFlag(final int flag) {
    if(flag == 0) {
      return LodMod.NO_ELEMENT;
    }

    for(final RegistryId elementId : GameEngine.REGISTRIES.elements) {
      final RegistryDelegate<Element> element = GameEngine.REGISTRIES.elements.getEntry(elementId);

      if((flag & element.get().flag) != 0) {
        return element;
      }
    }

    throw new IllegalArgumentException("Unknown element %x".formatted(flag));
  }

  public Element(final int flag, final Vector3f colour) {
    this.flag = flag;
    this.colour = colour;
  }

  @Override
  public void read(final int index, final Param out) {
    // Colour
    if(index == 0) {
      final int r = (int)(this.colour.x * 255);
      final int g = (int)(this.colour.y * 255);
      final int b = (int)(this.colour.z * 255);
      out.set(b << 16 | g << 8 | r);
      return;
    }

    ScriptReadable.super.read(index, out);
  }

  public abstract int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement);
  public int adjustDefendingElementalDamage(final AttackType attackType, final int damage, final Element attackerElement) { return damage; }
  public abstract int adjustDragoonSpaceDamage(final AttackType attackType, final int damage, final Element attackingElement);
}
