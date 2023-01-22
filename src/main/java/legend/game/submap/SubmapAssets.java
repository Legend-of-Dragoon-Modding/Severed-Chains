package legend.game.submap;

import legend.game.tim.Tim;
import legend.game.scripting.ScriptFile;

import java.util.ArrayList;
import java.util.List;

public class SubmapAssets {
  public ScriptFile script;
  public final List<SubmapObject> objects = new ArrayList<>();
  public final List<Tim> pxls = new ArrayList<>();
  /** TODO Two object indices that get special flags, needs research */
  public byte[] lastEntry;
}
