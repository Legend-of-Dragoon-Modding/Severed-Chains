package legend.game.characters;

import legend.game.scripting.Param;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class StatModType<S extends Stat, M extends StatMod<S>, C extends StatModConfig> extends RegistryEntry {
  /** Create a new instance of this stat mod from a config */
  public abstract M make(final C config);
  public abstract C makeConfig();

  public abstract void update(final M mod, final C config);

  public abstract void readConfigFromScript(final C config, final Param params);
  public abstract void writeConfigToScript(final C config, final Param params);
}
