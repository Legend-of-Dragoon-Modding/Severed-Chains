package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.SItem;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Label;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Locale;
import java.util.Set;

import static legend.game.Audio.playMenuSound;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;

public class OptionsCategoryScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  public OptionsCategoryScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final Runnable unload) {
    deallocateRenderables(0xff);

    this.unload = unload;
    this.init();

    for(final ConfigCategory category : ConfigCategory.values()) {
      int count = 0;
      for(final RegistryId configId : GameEngine.REGISTRIES.config) {
        final ConfigEntry<?> entry = GameEngine.REGISTRIES.config.getEntry(configId).get();

        if(entry.category == category && validLocations.contains(entry.storageLocation) && entry.hasEditControl()) {
          count++;
        }
      }

      if(count != 0) {
        final Button button = new Button(I18n.translate(CoreMod.MOD_ID + ".config.category.configure"));
        button.onPressed(() -> button.getScreen().getStack().pushScreen(this.createOptionsScreen(config, validLocations, category)));

        this.addRow(I18n.translate(CoreMod.MOD_ID + ".config.category." + category.name().toLowerCase(Locale.US) + ".label"), button);
      }
    }

    if(!validLocations.contains(ConfigStorageLocation.CAMPAIGN)) {
      final Label label = this.addControl(new Label(I18n.translate("lod_core.ui.options_category.some_options_not_available")));
      label.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
      label.setPos(0, 150);
      label.setWidth(this.getWidth());
    }

    this.addHotkey(I18n.translate("lod_core.ui.options_category.back"), INPUT_ACTION_MENU_BACK, this::back);
  }

  protected void init() {
    startFadeEffect(2, 10);
    this.addControl(new Background());
  }

  protected MenuScreen createOptionsScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final ConfigCategory category) {
    return new OptionsScreen(config, validLocations, category, () -> {
      startFadeEffect(2, 10);
      SItem.menuStack.popScreen();
    });
  }

  private void back() {
    playMenuSound(3);
    this.unload.run();
  }
}
