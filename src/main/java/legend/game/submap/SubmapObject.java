package legend.game.submap;

import legend.game.types.ExtendedTmd;
import legend.game.scripting.ScriptFile;
import legend.game.types.TmdAnimationFile;

import java.util.ArrayList;
import java.util.List;

public class SubmapObject {
  public ScriptFile script;
  public ExtendedTmd model;
  public final List<TmdAnimationFile> animations = new ArrayList<>();
}
