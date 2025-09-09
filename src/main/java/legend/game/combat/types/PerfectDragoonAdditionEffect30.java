package legend.game.combat.types;

import legend.game.scripting.ScriptState;
import legend.game.scripting.ScriptedObject;
import org.joml.Vector3f;

import java.util.Arrays;

public class PerfectDragoonAdditionEffect30 implements ScriptedObject {
  public final PerfectDragoonAdditionEffectGlyph06[] glyphs_00 = new PerfectDragoonAdditionEffectGlyph06[8];

  public PerfectDragoonAdditionEffect30() {
    Arrays.setAll(this.glyphs_00, i -> new PerfectDragoonAdditionEffectGlyph06());
  }

  @Override
  public Vector3f getPosition() {
    return null;
  }

  @Override
  public void renderScriptDebug(final ScriptState<ScriptedObject> state) {

  }
}
