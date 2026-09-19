package legend.game.modding.events.worldmap;

import legend.game.wmap.preset.WorldMapPresetEntry;
import org.legendofdragoon.modloader.events.Event;

import java.util.List;

/**
 * Populate the preset catalog after loaded-mod file discovery. May fire again on refresh/mod changes.
 * Add lazy suppliers for blueprints requiring registries; discovery does not initialize game assets.
 * IDs must be unique. Replacing the built-in vanilla choice is not permitted.
 */
public class WorldMapPresetsEvent extends Event {
  public final List<WorldMapPresetEntry> presets;

  public WorldMapPresetsEvent(final List<WorldMapPresetEntry> presets) {
    this.presets = presets;
  }
}
