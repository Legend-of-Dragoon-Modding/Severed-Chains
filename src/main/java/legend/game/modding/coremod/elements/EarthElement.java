package legend.game.modding.coremod.elements;

import legend.lodmod.LodMod;
import org.joml.Vector3f;

public class EarthElement extends SimpleElement {
  public EarthElement() {
    super(0x2, new Vector3f(0xb6 / 255.0f, 0x70 / 255.0f, 0.0f), LodMod.WIND_ELEMENT);
  }
}
