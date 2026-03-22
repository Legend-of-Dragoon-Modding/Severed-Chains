package legend.game.modding.coremod.elements;

import legend.game.characters.Element;
import org.joml.Vector3f;

public class ThunderElement extends Element {
  public ThunderElement() {
    super(0x10, new Vector3f(0x81 / 255.0f, 0x9 / 255.0f, 0xec / 255.0f));
  }

  @Override
  public boolean isStrongAgainst(final Element other) {
    return false;
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
