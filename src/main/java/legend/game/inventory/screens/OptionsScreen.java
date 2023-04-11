package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.GameEngine;
import legend.game.input.GlfwController;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.modding.registries.RegistryId;
import legend.game.saves.ConfigEntry;
import legend.game.types.GameState52c;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;

public class OptionsScreen extends VerticalLayoutScreen {
  private final GameState52c state;
  private final Runnable unload;

  private final Dropdown joypadDropdown;
  private final List<GlfwController> joypads = new ArrayList<>();

  public OptionsScreen(final GameState52c state, final Runnable unload) {
    deallocateRenderables(0xff);
    scriptStartEffect(2, 10);

    this.state = state;
    this.unload = unload;

    this.addControl(new Background());

    this.joypadDropdown = this.addDropdown("Joypad");
    this.joypadDropdown.onSelection(this::onJoypadSelected);
    this.updateJoypadList();

    final Dropdown sound = this.addDropdown("Sound", "Stereo", "Mono");
    sound.setSelectedIndex(this.state.mono_4e0 ? 1 : 0);
    sound.onSelection(index -> this.state.mono_4e0 = index == 1);

    final Dropdown transforms = this.addDropdown("Dragoon Transformation", "Normal", "Short");
    transforms.setSelectedIndex(this.state.morphMode_4e2);
    transforms.onSelection(index -> this.state.morphMode_4e2 = index);

    for(final RegistryId configId : GameEngine.REGISTRIES.config) {
      //noinspection rawtypes
      final ConfigEntry configEntry = GameEngine.REGISTRIES.config.getEntry(configId).get();

      if(configEntry.hasEditControl()) {
        //noinspection unchecked
        this.addRow(configId.toString(), configEntry.makeEditControl(this.state.getConfig(configEntry), this.state));
      }
    }
  }

  private Dropdown addDropdown(final String name, final String... options) {
    final Dropdown dropdown = new Dropdown();

    for(final String option : options) {
      dropdown.addOption(option);
    }

    return this.addRow(name, dropdown);
  }

  private void updateJoypadList() {
    this.joypadDropdown.clearOptions();
    this.joypadDropdown.addOption("<none>");

    this.joypads.clear();

    for(final GlfwController controller : Input.controllerManager.getConnectedControllers()) {
      this.joypadDropdown.addOption(controller.getName());
      this.joypads.add(controller);
    }
  }

  private void onJoypadSelected(final int index) {
    if(index == 0) {
      Config.controllerGuid("");
      Input.useController(null);
    } else {
      final GlfwController controller = this.joypads.get(index - 1);
      Input.useController(controller);
      Config.controllerGuid(controller.getGuid());
    }

    try {
      Config.save();
    } catch(final IOException ignored) {}
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
