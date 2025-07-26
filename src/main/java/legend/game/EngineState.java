package legend.game;

import de.jcm.discordgamesdk.activity.Activity;
import legend.core.platform.input.InputAction;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.types.CContainer;
import legend.game.types.GameState52c;
import legend.game.types.Model124;
import org.joml.Math;

import java.util.function.Function;

import static legend.core.GameEngine.PLATFORM;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_DOWN;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_LEFT;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_RIGHT;
import static legend.lodmod.LodMod.INPUT_ACTION_GENERAL_MOVE_UP;

public abstract class EngineState {
  private final Function<RunningScript, FlowControl>[] functions = new Function[1024];

  private float analogueAngle;
  private float analogueMagnitude;

  /** Runs before scripts are ticked */
  public void tick() {
    final float up = PLATFORM.getAxis(INPUT_ACTION_GENERAL_MOVE_UP.get());
    final float down = PLATFORM.getAxis(INPUT_ACTION_GENERAL_MOVE_DOWN.get());
    final float left = PLATFORM.getAxis(INPUT_ACTION_GENERAL_MOVE_LEFT.get());
    final float right = PLATFORM.getAxis(INPUT_ACTION_GENERAL_MOVE_RIGHT.get());
    final float x = left + right;
    final float y = up + down;

    if(x == 0.0f && y == 0.0f) {
      this.analogueMagnitude = 0.0f;
      return;
    }

    this.analogueMagnitude = Math.min(1.0f, Math.sqrt(x * x + y * y));
    this.analogueAngle = Math.atan2(y, x);
  }

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

  public void inputActionPressed(final InputAction action, final boolean repeat) {

  }

  public void inputActionReleased(final InputAction action) {

  }

  public int getInputsPressed() {
    return 0;
  }

  public int getInputsRepeat() {
    return 0;
  }

  public int getInputsHeld() {
    return 0;
  }

  public float getAnalogueAngle() {
    return this.analogueAngle;
  }

  public float getAnalogueMagnitude() {
    return this.analogueMagnitude;
  }

  public void loadGameFromMenu(final GameState52c gameState) {
    throw new RuntimeException("Not implemented");
  }

  public void updateDiscordRichPresence(final Activity activity) {
    activity.setDetails(this.getChapter() + " - " + this.getLocation());
    activity.setState(null);
  }

  public String getChapter() {
    if(gameState_800babc8 != null && gameState_800babc8.chapterIndex_98 > -1 && gameState_800babc8.chapterIndex_98 < chapterNames_80114248.length) {
      return chapterNames_80114248[gameState_800babc8.chapterIndex_98];
    }

    return "Unknown Chapter";
  }

  public String getLocation() {
    if(continentIndex_800bf0b0 > -1 && continentIndex_800bf0b0 < worldMapNames_8011c1ec.length) {
      return worldMapNames_8011c1ec[continentIndex_800bf0b0];
    }

    return "Unknown Location";
  }

  public enum RenderMode {
    /** Used by submaps and title screen, pseudo-perspective using orthographic projection with H division */
    LEGACY,
    /** True perspective */
    PERSPECTIVE,
  }
}
