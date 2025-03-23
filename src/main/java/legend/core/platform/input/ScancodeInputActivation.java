package legend.core.platform.input;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class ScancodeInputActivation extends InputActivation {
  public final InputKey key;
  public final Set<InputMod> mods = EnumSet.noneOf(InputMod.class);

  public ScancodeInputActivation(final InputKey key, final InputMod... mods) {
    this.key = key;
    this.mods.addAll(Arrays.asList(mods));
  }
}
