package legend.game.characters;

import legend.game.combat.types.AttackType;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;
import legend.lodmod.LodMod;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.GameEngine.REGISTRIES;

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

    for(final RegistryId elementId : REGISTRIES.elements) {
      final RegistryDelegate<Element> element = REGISTRIES.elements.getEntry(elementId);

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

    // Is strong against (expects out to be set to another element's registry ID)
    if(index == 1) {
      final Element opposing = REGISTRIES.elements.getEntry(out.getRegistryId()).get();
      out.set(this.isStrongAgainst(opposing));
      return;
    }

    // Is weak against (expects out to be set to another element's registry ID)
    if(index == 2) {
      final Element opposing = REGISTRIES.elements.getEntry(out.getRegistryId()).get();
      out.set(this.isWeakAgainst(opposing));
      return;
    }

    // Boosts element (expects out to be set to another element's registry ID)
    if(index == 3) {
      final Element opposing = REGISTRIES.elements.getEntry(out.getRegistryId()).get();
      out.set(this.boostsElement(opposing));
      return;
    }

    ScriptReadable.super.read(index, out);
  }

  public abstract boolean isStrongAgainst(final Element other);
  public abstract boolean isWeakAgainst(final Element other);
  public abstract boolean boostsElement(final Element other);

  public int adjustAttackingElementalDamage(final AttackType attackType, final int damage, final Element targetElement) {
    if(this.isStrongAgainst(targetElement)) {
      return damage * 150 / 100;
    }

    if(this.isWeakAgainst(targetElement)) {
      return damage * 50 / 100;
    }

    return damage;
  }

  public int adjustDefendingElementalDamage(final AttackType attackType, final int damage, final Element attackerElement) {
    return damage;
  }

  public int adjustDragoonSpaceDamage(final AttackType attackType, final int damage, final Element attackingElement) {
    if(this.boostsElement(attackingElement)) {
      return damage * 150 / 100;
    }

    if(attackingElement.isWeakAgainst(this)) {
      return damage * 50 / 100;
    }

    return damage;
  }
}
