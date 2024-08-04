package legend.game.modding.coremod.elements;

import legend.lodmod.LodMod;
import org.joml.Vector3f;

public class DarkElement extends SimpleElement {
  public DarkElement() {
    super(0x4, new Vector3f(0x19 / 255.0f, 0xf / 255.0f, 0x80 / 255.0f), LodMod.LIGHT_ELEMENT);
  }
}
