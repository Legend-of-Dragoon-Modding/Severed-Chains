package legend.game.modding.coremod.elements;

import legend.game.modding.coremod.CoreMod;
import org.joml.Vector3f;

public class WaterElement extends SimpleElement {
  public WaterElement() {
    super(0x1, new Vector3f(0x4c / 255.0f, 0xb7 / 255.0f, 0xe1 / 255.0f), CoreMod.FIRE_ELEMENT);
  }
}
