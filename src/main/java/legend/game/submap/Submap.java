package legend.game.submap;

import legend.core.RenderEngine;
import legend.game.scripting.ScriptFile;
import legend.game.tmd.UvAdjustmentMetrics14;

import java.util.ArrayList;
import java.util.List;

public abstract class Submap {
  public ScriptFile script;
  public final List<SubmapObject> objects = new ArrayList<>();
  public final List<UvAdjustmentMetrics14> uvAdjustments = new ArrayList<>();

  public abstract void loadEnv(final Runnable onLoaded);
  public abstract void loadAssets(final Runnable onLoaded);
  /** Called when textures need to be reloaded (e.g. after menus are closed) */
  public abstract void restoreAssets();

  public abstract void loadMusicAndSounds();
  public abstract void startMusic();

  /** Caches world map transition primitive indices and sets up the collided primitive setter callback */
  public abstract void loadMapTransitionData(final MapTransitionData4c transitionData);
  public abstract void loadCollisionAndTransitions();

  public abstract void draw();
  public abstract void unload();

  public abstract void calcGoodScreenOffset(final float x, final float y);

  public abstract int getEncounterRate();
  public abstract void generateEncounter();

  public boolean hasEncounters() {
    return this.getEncounterRate() != 0;
  }

  public abstract Runnable createPostBattleResume();

  void applyCollisionDebugColour(final int collisionPrimitiveIndex, final RenderEngine.QueuedModel model) { }
}
