package legend.game.characters;

import legend.game.types.CharacterData2c;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class LevelUpAction<T> extends RegistryEntry {
  public abstract void apply(final CharacterData2c character, final T options);
}
