package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptedObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;

public class ThunderArrowEffect1c implements ScriptedObject {
  public int count_00;
  public int _04;
  public int _08;
  public int count_0c;
  public int _10;
  public float z_14;
  public ThunderArrowEffectBolt1e[][] _18;

  public final MV transforms = new MV();

  @Override
  public Vector3f getPosition() {
    return new Vector3f(this._18[0][0].x_00, this._18[0][0].y_02, 0.0f);
  }

  @Override
  public void renderScriptDebug(final ScriptState<ScriptedObject> state) {
    final Vector2f viewspace = new Vector2f(this._18[0][0].x_00, this._18[0][0].y_02);
    ScriptedObject.renderScriptDebug(viewspace, this.getColour());
    ScriptedObject.renderScriptDebugText(state, viewspace.x + GPU.getOffsetX() - 9.0f, viewspace.y + GPU.getOffsetY() - 9.0f);
  }
}
