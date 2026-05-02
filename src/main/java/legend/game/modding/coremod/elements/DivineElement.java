package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import org.joml.Vector3f;

public class DivineElement extends Element {
  public DivineElement() {
    super(0x8, new Vector3f(0.5f, 0.5f, 0.5f));
  }

  @Override
  public boolean isStrongAgainst(final Element other) {
    return false;
  }

  @Override
  public boolean isWeakAgainst(final Element other) {
    return false;
  }

  @Override
  public boolean boostsElement(final Element other) {
    return other == this;
  }
}
