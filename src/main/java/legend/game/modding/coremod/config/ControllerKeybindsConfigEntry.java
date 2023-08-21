package legend.game.modding.coremod.config;

import legend.game.SItem;
import legend.game.Scus94491BpeSegment;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.KeybindsScreen;
import legend.game.inventory.screens.controls.Button;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class ControllerKeybindsConfigEntry extends ConfigEntry<Void> {
  public ControllerKeybindsConfigEntry() {
    super(null, ConfigStorageLocation.GLOBAL, o -> new byte[0], bytes -> null);

    this.setEditControl((current, configCollection) -> {
      final Button button = new Button(I18n.translate(CoreMod.MOD_ID + ".config.controller_keybinds.configure"));
      button.onPressed(() -> button.getScreen().getStack().pushScreen(new KeybindsScreen(configCollection, () -> {
        Scus94491BpeSegment.startFadeEffect(2, 10);
        SItem.menuStack.popScreen();
      })));

      return button;
    });
  }
}
