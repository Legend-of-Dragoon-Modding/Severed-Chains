package legend.game.characters;

import legend.game.combat.bent.BattleEntity27c;

import java.util.ArrayList;
import java.util.List;

public abstract class Stat {
  public final StatType<?> type;
  protected final StatCollection stats;
  protected final List<StatMod> mods = new ArrayList<>();

  public Stat(final StatType<?> type, final StatCollection stats) {
    this.type = type;
    this.stats = stats;
  }

  public <T extends StatMod> T addMod(final T mod) {
    this.mods.add(mod);
    return mod;
  }

  protected void turnFinished(final BattleEntity27c bent) {
    for(final StatMod mod : this.mods) {
      mod.turnFinished(this.stats, this.type, bent);
    }

    this.mods.removeIf(StatMod::isFinished);
  }
}
