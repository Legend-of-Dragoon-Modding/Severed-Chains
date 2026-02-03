package legend.game.modding.events.submap;

import legend.game.modding.events.engine.InGameEvent;
import legend.game.scripting.ScriptFile;
import legend.game.submap.SMap;
import legend.game.submap.Submap;
import legend.game.submap.SubmapObject;
import legend.game.types.GameState52c;

import java.util.List;

public class SubmapLoadEvent extends InGameEvent<SMap> implements LoadedSubmapEvent {
  private final Submap submap;
  public ScriptFile submapScript;
  public final List<SubmapObject> submapObjects;

  public SubmapLoadEvent(final SMap engineState, final GameState52c gameState, final Submap submap, final ScriptFile submapScript, final List<SubmapObject> submapObjects) {
    super(engineState, gameState);
    this.submap = submap;
    this.submapScript = submapScript;
    this.submapObjects = submapObjects;
  }

  @Override
  public Submap getSubmap() {
    return this.submap;
  }
}
