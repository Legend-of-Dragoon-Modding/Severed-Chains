package legend.game.modding.coremod.elements;

import legend.game.modding.coremod.CoreMod;
import org.joml.Vector3f;

public class WindElement extends SimpleElement {
  public WindElement() {
    super(0x40, new Vector3f(0x48 / 255.0f, 0xff / 255.0f, 0x9f / 255.0f), CoreMod.EARTH_ELEMENT);
  }
}
