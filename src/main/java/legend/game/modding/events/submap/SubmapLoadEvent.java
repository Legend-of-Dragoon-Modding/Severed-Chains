package legend.game.modding.events.submap;

import legend.game.scripting.ScriptFile;
import legend.game.submap.SubmapObject;

import java.util.List;

public class SubmapLoadEvent extends SubmapEvent {
  public ScriptFile submapScript;
  public final List<SubmapObject> submapObjects;

  public SubmapLoadEvent(final ScriptFile submapScript, final List<SubmapObject> submapObjects) {
    this.submapScript = submapScript;
    this.submapObjects = submapObjects;
  }
}
