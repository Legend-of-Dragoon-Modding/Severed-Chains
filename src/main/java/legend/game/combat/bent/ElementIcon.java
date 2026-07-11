package legend.game.combat.bent;

import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.nio.file.Path;

public class ElementIcon extends RegistryEntry {
  public final Path path;

  public ElementIcon(final Path path) {
    this.path = path;
  }
}
