package legend.game.modding.coremod.elements;

import legend.lodmod.LodMod;
import org.joml.Vector3f;

public class LightElement extends SimpleElement {
  public LightElement() {
    super(0x20, new Vector3f(0xd5 / 255.0f, 0xc5 / 255.0f, 0x3a / 255.0f), LodMod.DARK_ELEMENT);
  }
}
