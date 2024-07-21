package legend.game.characters;

import legend.game.combat.bent.BattleEntity27c;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Stat {
  public final StatType<?> type;
  protected final StatCollection stats;
  protected final Map<RegistryId, StatMod> mods = new LinkedHashMap<>();

  public Stat(final StatType<?> type, final StatCollection stats) {
    this.type = type;
    this.stats = stats;
  }

  public boolean hasMod(final RegistryId id) {
    return this.mods.containsKey(id);
  }

  public <T extends StatMod> T addMod(final RegistryId id, final T mod) {
    this.mods.put(id, mod);
    return mod;
  }

  public <T extends StatMod> T getMod(final RegistryId id) {
    return (T)this.mods.get(id);
  }

  public void removeMod(final RegistryId id) {
    this.mods.remove(id);
  }

  protected void turnFinished(final BattleEntity27c bent) {
    for(final StatMod mod : this.mods.values()) {
      mod.turnFinished(this.stats, this.type, bent);
    }

    this.mods.values().removeIf(s -> s.isFinished(this.stats, this.type, bent));
  }
}
