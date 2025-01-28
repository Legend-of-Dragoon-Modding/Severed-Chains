package legend.game.modding.coremod.config;

import legend.core.IoHelper;
import legend.game.input.Controller;
import legend.game.input.Input;
import legend.game.inventory.screens.controls.Dropdown;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;

/** Convenience class for simple enum-backed configs */
public class ControllerConfigEntry extends ConfigEntry<String> {
  public ControllerConfigEntry() {
    super(
      "",
      ConfigStorageLocation.GLOBAL,
      ConfigCategory.CONTROLS,
      str -> IoHelper.stringToBytes(str, 1),
      bytes -> IoHelper.stringFromBytes(bytes, 1, "")
    );

    this.setEditControl((current, gameState) -> {
      final List<Controller> joypads = new ArrayList<>();

      final Dropdown<String> dropdown = new Dropdown<>();
      dropdown.onSelection(index -> gameState.setConfig(this, index == 0 ? "" : joypads.get(index - 1).getGuid()));
      dropdown.addOption("<none>");

      for(final Controller controller : Input.controllerManager.getConnectedControllers()) {
        dropdown.addOption(controller.getName());
        joypads.add(controller);

        if(controller.getGuid().equals(CONFIG.getConfig(this))) {
          dropdown.setSelectedIndex(joypads.size());
        }
      }

      return dropdown;
    });
  }

  @Override
  public void onChange(final ConfigCollection configCollection, final String oldValue, final String newValue) {
    super.onChange(configCollection, oldValue, newValue);
    Input.useController(Input.controllerManager.getControllerByGuid(newValue));
  }
}
