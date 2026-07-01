package legend.game.combat.bent;

import legend.game.scripting.ScriptReadable;
import org.legendofdragoon.modloader.registries.RegistryEntry;

import java.nio.file.Path;

public class ElementIconString extends RegistryEntry implements ScriptReadable {
  public final Path path;

  public ElementIconString(final Path path) {
    this.path = path;
  }
}
