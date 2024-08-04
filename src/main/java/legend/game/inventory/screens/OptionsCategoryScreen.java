package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.SItem;
import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Locale;
import java.util.Set;

import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;

public class OptionsCategoryScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  public OptionsCategoryScreen(final ConfigCollection config, final Set<ConfigStorageLocation> validLocations, final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

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
        button.onPressed(() -> button.getScreen().getStack().pushScreen(new OptionsScreen(config, validLocations, category, () -> {
          startFadeEffect(2, 10);
          SItem.menuStack.popScreen();
        })));

        this.addRow(I18n.translate(CoreMod.MOD_ID + ".config.category." + category.name().toLowerCase(Locale.US) + ".label"), button);
      }
    }
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      playMenuSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
