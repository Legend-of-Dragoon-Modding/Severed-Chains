package legend.game.modding.coremod.elements;

import legend.lodmod.LodMod;
import org.joml.Vector3f;

public class WaterElement extends SimpleElement {
  public WaterElement() {
    super(0x1, new Vector3f(0x4c / 255.0f, 0xb7 / 255.0f, 0xe1 / 255.0f), LodMod.FIRE_ELEMENT);
  }
}
