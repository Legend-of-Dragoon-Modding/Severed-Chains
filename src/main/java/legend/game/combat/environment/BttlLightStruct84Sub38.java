package legend.game.combat.environment;

import org.joml.Vector3f;

public class BttlLightStruct84Sub38 {
  public int _00;
  public final Vector3f angle_04 = new Vector3f();
  public final Vector3f vec_10 = new Vector3f();
  public final Vector3f vec_1c = new Vector3f();
  public final Vector3f vec_28 = new Vector3f();
  public int ticksRemaining_34;

  public void clear() {
    this._00 = 0;
    this.angle_04.zero();
    this.vec_10.zero();
    this.vec_1c.zero();
    this.vec_28.zero();
    this.ticksRemaining_34 = 0;
  }
}
