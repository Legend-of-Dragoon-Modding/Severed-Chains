package legend.game.characters;

import legend.core.memory.types.IntRef;
import legend.game.scripting.Param;
import legend.game.unpacker.FileData;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class StatModType<S extends Stat, M extends StatMod<S>, C extends StatModConfig> extends RegistryEntry {
  /** Create a new instance of this stat mod from a config */
  public abstract M make(final C config);
  public abstract C makeConfig();

  public abstract void serialize(final M mod, final FileData data, final IntRef offset);
  public abstract M deserialize(final FileData data, final IntRef offset);

  public abstract void update(final M mod, final C config);

  public abstract void readConfigFromScript(final C config, final Param params);
  public abstract void writeConfigToScript(final C config, final Param params);
}
