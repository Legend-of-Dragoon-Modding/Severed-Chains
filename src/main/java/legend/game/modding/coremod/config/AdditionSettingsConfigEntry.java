package legend.game.modding.coremod.config;

import legend.game.SItem;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.AdditionSettingsScreen;
import legend.game.inventory.screens.controls.Button;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import java.util.Set;

import static legend.game.Scus94491BpeSegment.startFadeEffect;

public class AdditionSettingsConfigEntry extends ConfigEntry<Void> {
  public AdditionSettingsConfigEntry() {
    super(null, ConfigStorageLocation.GLOBAL, ConfigCategory.GAMEPLAY, o -> new byte[0], bytes -> null);

    this.setEditControl((current, configCollection) -> {
      final Button button = new Button(I18n.translate(CoreMod.MOD_ID + ".config.addition_settings.configure"));
      button.onPressed(() -> button.getScreen().getStack().pushScreen(new AdditionSettingsScreen(configCollection, Set.of(ConfigStorageLocation.CAMPAIGN), ConfigCategory.ADDITIONS, () -> {
        startFadeEffect(2, 10);
        SItem.menuStack.popScreen();
      })));

      return button;
    });
  }
}
