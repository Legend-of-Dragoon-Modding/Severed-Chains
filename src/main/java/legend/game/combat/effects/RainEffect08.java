package legend.game.combat.effects;

import legend.core.opengl.Obj;
import org.joml.Matrix4f;

import java.util.Arrays;

public class RainEffect08 implements Effect {
  public int count_00;
  public RaindropEffect0c[] raindropArray_04;

  public Obj obj;
  public final Matrix4f transforms = new Matrix4f();

  public RainEffect08(final int count) {
    this.count_00 = count;
    this.raindropArray_04 = new RaindropEffect0c[count];
    Arrays.setAll(this.raindropArray_04, RaindropEffect0c::new);
  }
}
