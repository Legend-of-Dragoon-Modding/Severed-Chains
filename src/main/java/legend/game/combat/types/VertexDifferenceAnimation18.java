package legend.game.combat.types;

import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptedObject;
import org.joml.Vector3f;

public class VertexDifferenceAnimation18 implements ScriptedObject {
  public int ticksRemaining_00;
  public float embiggener_04;
  public int vertexCount_08;
  public Vector3f[] sourceVertices_0c;
  public Vector3f[] step_10;
  public Vector3f[] current_14;

  public TmdObjTable1c tmd;

  @Method(0x80109b44L)
  public static void applyVertexDifferenceAnimation(final ScriptState<VertexDifferenceAnimation18> state, final VertexDifferenceAnimation18 animation) {
    animation.ticksRemaining_00--;

    if(animation.ticksRemaining_00 < 0) {
      state.deallocateWithChildren();
      return;
    }

    //LAB_80109b7c
    //LAB_80109b90
    for(int i = 0; i < animation.vertexCount_08; i++) {
      final Vector3f source = animation.sourceVertices_0c[i];
      final Vector3f step = animation.step_10[i];
      final Vector3f current = animation.current_14[i];

      current.add(step);
      source.set(current);

      step.x += step.x * animation.embiggener_04;
      step.y += step.y * animation.embiggener_04;
      step.z += step.z * animation.embiggener_04;
    }

    //LAB_80109ce0
  }

  @Override
  public Vector3f getPosition() {
    return null;
  }

  @Override
  public void renderScriptDebug(final ScriptState<ScriptedObject> state) {

  }
}
