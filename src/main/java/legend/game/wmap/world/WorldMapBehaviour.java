package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.util.Objects;
import java.util.function.BiConsumer;

/** Registered configuration strategy applied at each world map initialization. */
public class WorldMapBehaviour extends RegistryEntry {
  private final int priority;
  private final BiConsumer<WorldMapDefinition.Builder, WorldMapRules.Builder> configure;

  public WorldMapBehaviour(final int priority, final BiConsumer<WorldMapDefinition.Builder, WorldMapRules.Builder> configure) {
    this.priority = priority;
    this.configure = Objects.requireNonNull(configure, "configure");
  }

  public int priority() {
    return this.priority;
  }

  public void configure(final WorldMapDefinition.Builder definition, final WorldMapRules.Builder rules) {
    this.configure.accept(Objects.requireNonNull(definition, "definition"), Objects.requireNonNull(rules, "rules"));
  }
}
