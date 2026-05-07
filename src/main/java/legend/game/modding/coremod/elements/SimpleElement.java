package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public class SimpleElement extends Element {
  private final RegistryDelegate<Element> opposed;

  public SimpleElement(final int flag, final Vector3f colour, final RegistryDelegate<Element> opposed) {
    super(flag, colour);
    this.opposed = opposed;
  }

  @Override
  public boolean isStrongAgainst(final Element other) {
    return other == this.opposed.get();
  }

  @Override
  public boolean isWeakAgainst(final Element other) {
    return other == this;
  }

  @Override
  public boolean boostsElement(final Element other) {
    return other == this;
  }
}
