package legend.core.font;

import legend.core.GameEngine;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.StringConfigEntry;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.FONTS;

public class RetailFontConfigEntry extends StringConfigEntry {
  public RetailFontConfigEntry() {
    super("smooth.json", 2, ConfigStorageLocation.GLOBAL, ConfigCategory.GRAPHICS);

    this.setEditControl((current, gameState) -> {
      final Path fontsDir = Path.of("gfx", "fonts");

      final Dropdown<Font> dropdown = new Dropdown<>(font -> font.name);
      dropdown.onSelection(index -> {
        gameState.setConfig(this, fontsDir.relativize(dropdown.getSelectedOption().path).toString());
        GameEngine.DEFAULT_FONT = dropdown.getSelectedOption();
      });

      final Path currentPath = Path.of(CONFIG.getConfig(this));

      try(final DirectoryStream<Path> dir = Files.newDirectoryStream(fontsDir)) {
        for(final Path path : dir) {
          if(path.toString().endsWith(".json")) {
            final Font font = FONTS.get(path);
            dropdown.addOption(font);

            if(fontsDir.relativize(path).equals(currentPath)) {
              dropdown.setSelectedIndex(dropdown.size() - 1);
            }
          }
        }
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(ConfigCollection configCollection, String oldValue, String newValue) {
    super.onChange(configCollection, oldValue, newValue);
  }
}
