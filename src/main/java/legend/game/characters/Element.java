package legend.game.characters;

import legend.core.GameEngine;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.registries.RegistryEntry;
import legend.game.modding.registries.RegistryId;

public abstract class Element extends RegistryEntry {
  /** TODO figure out a way to remove this, mods will add elements with conflicting flags */
  @Deprecated
  public final int flag;

  @Deprecated
  public static Element fromFlag(final int flag) {
    if(flag == 0) {
      return CoreMod.NO_ELEMENT.get();
    }

    for(final RegistryId elementId : GameEngine.REGISTRIES.elements) {
      final Element element = GameEngine.REGISTRIES.elements.getEntry(elementId).get();

      if((flag & element.flag) != 0) {
        return element;
      }
    }

    throw new IllegalArgumentException("Unknown element %x".formatted(flag));
  }

  public Element(final int flag) {
    this.flag = flag;
  }

  public abstract int adjustElementalDamage(final int damage, final Element targetElement);
  public abstract int adjustDragoonSpaceDamage(final int damage, final Element dragoonSpaceElement);
}
