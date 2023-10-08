package legend.game.characters;

import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.function.BiFunction;

public class StatType<StatClass extends Stat> extends RegistryEntry {
  private final BiFunction<StatType<StatClass>, StatCollection, StatClass> constructor;

  public StatType(final BiFunction<StatType<StatClass>, StatCollection, StatClass> constructor) {
    this.constructor = constructor;
  }

  public StatClass make(final StatCollection stats) {
    return this.constructor.apply(this, stats);
  }
}
