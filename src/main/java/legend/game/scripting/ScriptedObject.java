package legend.game.scripting;

import legend.core.QueuedModelStandard;
import legend.core.Transformations;
import legend.core.gte.MV;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.TextColour;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.renderText;

public interface ScriptedObject {
  Vector3f getPosition();

  Vector3i GREY = new Vector3i(0x80, 0x80, 0x80);
  FontOptions FONT = new FontOptions().colour(TextColour.WHITE).shadowColour(TextColour.BLACK).size(0.2f);

  default Vector3i getColour() {
    return GREY;
  }

  default void renderScriptDebug(final ScriptState<ScriptedObject> state) {
    final Vector2f viewspace = new Vector2f();
    Transformations.toScreenspace(this.getPosition(), new MV(), viewspace);
    renderScriptDebug(viewspace, this.getColour());
    renderScriptDebugText(state, viewspace.x + GPU.getOffsetX() - 9.0f, viewspace.y + GPU.getOffsetY() - 9.0f);
  }

  static void renderScriptDebug(final Vector2f viewspace, final Vector3i colour) {
    final MV transforms = new MV();

    transforms.scaling(10.0f, 10.0f, 1.0f);
    transforms.transfer.set(viewspace, 10.0f);
    RENDERER.queueOrthoModel(RENDERER.lineBox, transforms, QueuedModelStandard.class)
      .colour(colour.x / 255.0f, colour.y / 255.0f, colour.z / 255.0f)
      .screenspaceOffset(GPU.getOffsetX(), GPU.getOffsetY());
  }

  static void renderScriptDebugText(final ScriptState<ScriptedObject> state, final float x, final float y) {
    renderText("Script " + state.index, x, y, FONT);
    renderText(state.innerStruct_00.getClass().getSimpleName(), x, y + 3.0f, FONT);
    renderText(state.name, x, y + 6.0f, FONT);
  }
}
