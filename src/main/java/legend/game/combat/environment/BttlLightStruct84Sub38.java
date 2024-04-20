package legend.game.combat.environment;

import org.joml.Vector3f;

public class BttlLightStruct84Sub38 {
  /** Low byte is type */
  public int typeAndFlags_00;
  public final Vector3f directionOrColour_04 = new Vector3f();
  public final Vector3f directionOrColourSpeed_10 = new Vector3f();
  public final Vector3f directionOrColourAcceleration_1c = new Vector3f();
  public final Vector3f directionOrColourDest_28 = new Vector3f();
  public int ticksRemaining_34;

  public void clear() {
    this.typeAndFlags_00 = 0;
    this.directionOrColour_04.zero();
    this.directionOrColourSpeed_10.zero();
    this.directionOrColourAcceleration_1c.zero();
    this.directionOrColourDest_28.zero();
    this.ticksRemaining_34 = 0;
  }
}
