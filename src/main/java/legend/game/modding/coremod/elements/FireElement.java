package legend.game.modding.coremod.elements;

import legend.lodmod.LodMod;
import org.joml.Vector3f;

public class FireElement extends SimpleElement {
  public FireElement() {
    super(0x80, new Vector3f(0xee / 255.0f, 0x9 / 255.0f, 0x9 / 255.0f), LodMod.WATER_ELEMENT);
  }
}
