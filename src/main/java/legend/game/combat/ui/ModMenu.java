package legend.game.combat.ui;

import org.legendofdragoon.modloader.registries.RegistryEntry;

public class ModMenu extends RegistryEntry {
  final String display;
  boolean enabled = true;

  public ModMenu(final String display) {
    this.display = display;
  }

  public void enable() {
    this.enabled = true;
  }

  public void disable() {
    this.enabled = false;
  }
}
