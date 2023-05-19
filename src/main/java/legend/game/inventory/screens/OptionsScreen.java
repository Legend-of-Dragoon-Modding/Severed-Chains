package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.modding.registries.RegistryId;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import java.util.Set;

import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;

public class OptionsScreen extends VerticalLayoutScreen {
  private final Runnable unload;

  public OptionsScreen(final ConfigCollection state, final Set<ConfigStorageLocation> validLocations, final Runnable unload) {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.unload = unload;

    this.addControl(new Background());

    for(final RegistryId configId : GameEngine.REGISTRIES.config) {
      //noinspection rawtypes
      final ConfigEntry configEntry = GameEngine.REGISTRIES.config.getEntry(configId).get();

      if(validLocations.contains(configEntry.storageLocation) && configEntry.hasEditControl()) {
        //noinspection unchecked
        this.addRow(configId.toString(), configEntry.makeEditControl(state.getConfig(configEntry), state));
      }
    }
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      playSound(3);
      this.unload.run();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
