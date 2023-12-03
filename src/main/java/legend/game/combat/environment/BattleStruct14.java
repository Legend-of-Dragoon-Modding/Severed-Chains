package legend.game.combat.environment;

import legend.core.gte.COLOUR;
import org.joml.Vector3f;

public class BattleStruct14 {
  public final Vector3f lightDirection_00;
  public float x_06;
  public float y_08;
  public final COLOUR lightColour_0a;
  public final COLOUR _0d;
  public float x_10;
  public float y_12;

  public BattleStruct14(final Vector3f lightDirection, final float x_06, final float y_08, final COLOUR lightColour, final COLOUR _0d, final float x_10, final float y_12) {
    this.lightDirection_00 = lightDirection;
    this.x_06 = x_06;
    this.y_08 = y_08;
    this.lightColour_0a = lightColour;
    this._0d = _0d;
    this.x_10 = x_10;
    this.y_12 = y_12;
  }

  public BattleStruct14() {
    this.lightDirection_00 = new Vector3f();
    this.lightColour_0a = new COLOUR();
    this._0d = new COLOUR();
  }
}
