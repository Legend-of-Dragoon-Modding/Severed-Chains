package legend.game.characters;

import java.util.ArrayList;
import java.util.List;

public class Stat {
  private final StatCollection stats;
  public final StatType type;
  private final List<StatMod> mods = new ArrayList<>();

  private int value;

  public Stat(final StatCollection stats, final StatType type) {
    this.stats = stats;
    this.type = type;
  }

  public int getRaw() {
    return this.value;
  }

  public void setRaw(final int value) {
    this.value = value;
  }

  public int getModified() {
    int value = this.getRaw();

    for(final StatMod mod : this.mods) {
      value += mod.apply(this.stats, this.type);
    }

    return value;
  }

  public <T extends StatMod> T addMod(final T mod) {
    this.mods.add(mod);
    return mod;
  }
}
