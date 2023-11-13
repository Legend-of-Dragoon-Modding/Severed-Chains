package legend.game.combat.ui;

import legend.core.gte.MV;
import org.joml.Vector3f;

import java.util.Arrays;

public class FloatingNumberC4 {
  public int state_00;
  public int flags_02;
  /** Must be the bent that the floating number is attached to */
  public int bentIndex_04;
  public boolean translucent_08;
  public int shade_0c;
  public final Vector3f colour = new Vector3f();

  public int _10;
  public int ticksRemaining_14;
  public int _18;
  public float x_1c;
  public float y_20;
  public final FloatingNumberDigit20[] digits_24 = new FloatingNumberDigit20[10];

  public final MV transforms = new MV();

  public FloatingNumberC4() {
    Arrays.setAll(this.digits_24, i -> new FloatingNumberDigit20());
  }
}
