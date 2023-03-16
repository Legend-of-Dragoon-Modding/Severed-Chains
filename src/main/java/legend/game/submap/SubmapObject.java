package legend.game.submap;

import legend.game.types.CContainer;
import legend.game.scripting.ScriptFile;
import legend.game.types.TmdAnimationFile;

import java.util.ArrayList;
import java.util.List;

public class SubmapObject {
  public ScriptFile script;
  public CContainer model;
  public final List<TmdAnimationFile> animations = new ArrayList<>();
}
