package legend.game.submap;

import legend.game.scripting.ScriptFile;
import legend.game.tmd.UvAdjustmentMetrics14;

import java.util.ArrayList;
import java.util.List;

public abstract class Submap {
  public ScriptFile script;
  public final List<SubmapObject> objects = new ArrayList<>();
  public final List<UvAdjustmentMetrics14> uvAdjustments = new ArrayList<>();

  public abstract void loadAssets(final Runnable onLoaded);
  /** Called when textures need to be reloaded (e.g. after menus are closed) */
  public abstract void restoreAssets();

  public abstract void draw();
  public abstract void unload();

  public abstract int getEncounterRate();
  public abstract void generateEncounter();

  public boolean hasEncounters() {
    return this.getEncounterRate() != 0;
  }
}
