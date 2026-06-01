package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import org.joml.Vector3f;

public class NoElement extends Element {
  public NoElement() {
    super(0, new Vector3f(1.0f, 1.0f, 1.0f));
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
    return false;
  }
}
