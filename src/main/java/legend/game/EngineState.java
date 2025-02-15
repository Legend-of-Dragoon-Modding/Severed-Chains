package legend.game;

import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.types.CContainer;
import legend.game.types.Model124;

import java.util.function.Function;

public abstract class EngineState {
  private final Function<RunningScript, FlowControl>[] functions = new Function[1024];

  /** Runs before scripts are ticked */
  public abstract void tick();

  /** Runs after scripts are ticked */
  public void postScriptTick(final boolean scriptsTicked) {

  }

  /** Runs after everything else is rendered */
  public void overlayTick() {

  }

  /** The amount we've multiplied this engine state's frame rate by (e.g. world map was 20FPS, we multiplied it by 3 to bring it to 60FPS) */
  public int tickMultiplier() {
    return 2;
  }

  public void restoreMusicAfterMenu() {

  }

  public Function<RunningScript, FlowControl>[] getScriptFunctions() {
    return this.functions;
  }

  public void modelLoaded(final Model124 model, final CContainer cContainer) {

  }

  public boolean renderTextOnTopOfAllBoxes() {
    return true;
  }

  public RenderMode getRenderMode() {
    return RenderMode.PERSPECTIVE;
  }

  /** Allows engine states to modify input before a script reads it */
  public int getScriptInput(final int input) {
    return input;
  }

  public enum RenderMode {
    /** Used by submaps and title screen, pseudo-perspective using orthographic projection with H division */
    LEGACY,
    /** True perspective */
    PERSPECTIVE,
  }
}
