package legend.game.characters;

import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;
import org.legendofdragoon.modloader.registries.RegistryEntry;

public abstract class SpellRing extends RegistryEntry implements ScriptReadable {
  public final int colour;

  public SpellRing(final int colour) {
    this.colour = colour;
  }

  @Override
  public void read(final int index, final Param out) {
    // Colour
    ScriptReadable.super.read(index, out);
  }
}
