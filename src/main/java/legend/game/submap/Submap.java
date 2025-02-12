package legend.game.submap;

import legend.core.QueuedModel;
import legend.core.gte.MV;
import legend.game.scripting.ScriptFile;
import legend.game.tmd.UvAdjustmentMetrics14;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class Submap {
  public ScriptFile script;
  public final List<SubmapObject> objects = new ArrayList<>();
  public final List<UvAdjustmentMetrics14> uvAdjustments = new ArrayList<>();

  public abstract void loadEnv(final Runnable onLoaded);
  public abstract void loadAssets(final Runnable onLoaded);

  public abstract void loadMusicAndSounds();
  public abstract void startMusic();

  /** Caches world map transition primitive indices and sets up the collided primitive setter callback */
  public abstract void loadMapTransitionData(final MapTransitionData4c transitionData);

  public abstract void prepareEnv();
  public abstract void prepareSobjModel(final SubmapObject210 sobj);
  public abstract void finishLoading();

  public abstract void preDraw();
  public abstract void draw();
  public abstract void drawEnv(final MV[] sobjMatrices);
  public abstract void unload();

  public abstract void calcGoodScreenOffset(final float x, final float y, final Vector2f out);

  public abstract int getEncounterRate();
  public abstract void prepareEncounter(final boolean useBattleStage);
  public abstract void prepareEncounter(final int encounterId, final boolean useBattleStage);

  public boolean hasEncounters() {
    return this.getEncounterRate() != 0;
  }

  public abstract void storeStateBeforeBattle();
  public abstract boolean isReturningToSameMapAfterBattle();

  void applyCollisionDebugColour(final int collisionPrimitiveIndex, final QueuedModel model) { }
}
