package legend.game.characters;

import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class LevelUpAction<T> extends RegistryEntry {
  public abstract void apply(final CharacterData2c character, final T options);

  public T cast(final Object options) {
    //noinspection unchecked
    return (T)options;
  }
}
